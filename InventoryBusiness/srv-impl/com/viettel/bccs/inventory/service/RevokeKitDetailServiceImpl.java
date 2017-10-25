package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.viettel.bccs.im1.dto.StockModelIm1DTO;
import com.viettel.bccs.im1.repo.StockTotalIm1Repo;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.IMCommonUtils;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.RevokeKitDetail;
import com.viettel.bccs.inventory.repo.RevokeKitDetailRepo;
import com.viettel.bccs.inventory.repo.StockKitRepo;
import com.viettel.bccs.inventory.service.BaseStock.BaseStockService;
import com.viettel.bccs.sale.dto.SubscriberDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.mmserver.base.Log;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.WebMethod;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RevokeKitDetailServiceImpl extends BaseStockService implements RevokeKitDetailService {

    private static final BaseMapper<RevokeKitDetail, RevokeKitDetailDTO> mapper = new BaseMapper<>(RevokeKitDetail.class, RevokeKitDetailDTO.class);

    @Autowired
    private RevokeKitDetailRepo repository;
    public static final Logger logger = Logger.getLogger(RevokeKitDetailService.class);

    private static final String TYPE_RESULT_SUSCESS = "1";//thu hoi thanh cong het
    private static final String TYPE_RESULT_EXIST_ERROR = "2";//thu hoi thanh cong + co ban ghi loi

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM_LIB)
    private EntityManager emLib;

    @Autowired
    private StaffService staffService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private ReasonService reasonService;
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private StockTransActionService stockTransActionService;
    @Autowired
    private StockTransDetailService stockTransDetailService;
    @Autowired
    private StockTransSerialService stockTransSerialService;
    @Autowired
    private RevokeKitTransService revokeKitTransService;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private StockTotalIm1Repo stockTotalIm1Repo;
    @Autowired
    private StockKitRepo stockKitRepo;


    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public RevokeKitDetailDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<RevokeKitDetailDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<RevokeKitDetailDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public RevokeKitDetailDTO save(RevokeKitDetailDTO dto) throws LogicException, Exception {
        return mapper.toDtoBean(repository.save(mapper.toPersistenceBean(dto)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public RevokeKitDetailDTO update(RevokeKitDetailDTO dto) throws LogicException, Exception {
        if (DataUtil.isNullObject(dto)
                || DataUtil.isNullOrZero(dto.getId())) {
            return null;
        }
        RevokeKitDetailDTO checkResult = this.findOne(dto.getId());
        if (DataUtil.isNullObject(checkResult)
                || DataUtil.isNullOrZero(checkResult.getId())) {
            return null;
        }
        return mapper.toDtoBean(repository.save(mapper.toPersistenceBean(dto)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public RevokeKitResultDTO executeRevokeKit(RevokeKitResultDTO revokeKitResultDTO) throws LogicException, Exception {
        RevokeKitResultDTO resultDTO = null;
        String typeResult = TYPE_RESULT_SUSCESS;
        if (revokeKitResultDTO != null) {
            //lay thong tin NV dang nhap
            StaffDTO actionStaffDTO = staffService.findOne(revokeKitResultDTO.getActionStaffId());
            if (actionStaffDTO == null || !Const.STATUS.ACTIVE.equals(actionStaffDTO.getStatus())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "staff.code.invalid.name");
            }
            ShopDTO shopLogin = shopService.findOne(actionStaffDTO.getShopId());
            if (shopService.checkChannelIsAgent(shopLogin.getChannelTypeId())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, getText("revoke.kit.agent.not.allow"));
            }
            List<RevokeKitFileImportDTO> lsFileRevokeKitImportAll = revokeKitResultDTO.getLsRevokeKitFileImportDTOs();
            //map danh sach theo ownerCode+ownerType | list danh sach thu hoi kit
            HashMap<String, List<RevokeKitFileImportDTO>> mapOwnerCodeValidFull = Maps.newHashMap();
            if (!DataUtil.isNullOrEmpty(lsFileRevokeKitImportAll)) {
                Long toOwnerId = revokeKitResultDTO.getOwnerId();
                Long toOwnerType = revokeKitResultDTO.getOwnerType();

                int index = 0;
                String key;
                String contentRow;
                List<String> lsValidDuplicate = Lists.newArrayList();
                //1. validate format du lieu
                for (RevokeKitFileImportDTO dto : lsFileRevokeKitImportAll) {
                    dto.setIndex(index);
                    index++;
                    String isdn = DataUtil.safeToString(dto.getIsdn()).trim();
                    String serial = DataUtil.safeToString(dto.getSerial()).trim();
                    String ownerCode = DataUtil.safeToString(dto.getOwnerCode()).trim();
                    String ownerType = DataUtil.safeToString(dto.getOwnerType()).trim();
                    String revokeType = DataUtil.safeToString(dto.getRevokeType()).trim();
                    contentRow = isdn + serial;

                    //kiem tra du lieu trong
                    if (DataUtil.isNullOrEmpty(isdn) && DataUtil.isNullOrEmpty(serial) && DataUtil.isNullOrEmpty(ownerCode)
                            && DataUtil.isNullOrEmpty(ownerType) && DataUtil.isNullOrEmpty(revokeType)) {
                        continue;
                    }
                    if (DataUtil.isNullOrEmpty(isdn) || DataUtil.isNullOrEmpty(serial)
                            || DataUtil.isNullOrEmpty(ownerCode) || DataUtil.isNullOrEmpty(ownerType) || DataUtil.isNullOrEmpty(revokeType)) {
                        dto.setResultRevoke(getText("revoke.stock.kit.data.empty"));
                        typeResult = TYPE_RESULT_EXIST_ERROR;
                        continue;
                    }
                    //check dinh dang isdn
                    if (!DataUtil.validateStringByPattern(isdn, Const.REGEX.NUMBER_REGEX)) {
                        dto.setResultRevoke(getText("revoke.stock.kit.data.isdn.invalid.format"));
                        typeResult = TYPE_RESULT_EXIST_ERROR;
                        continue;
                    }
                    //check do dai isdn
                    if (isdn.length() > 12) {
                        dto.setResultRevoke(getText("revoke.stock.kit.data.isdn.invalid.length"));
                        typeResult = TYPE_RESULT_EXIST_ERROR;
                        continue;
                    }
                    //check dinh dang serial
                    if (!DataUtil.validateStringByPattern(serial, Const.REGEX.NUMBER_REGEX)) {
                        dto.setResultRevoke(getText("stock.import.to.partner.balance.serial.not.handset.invalid"));
                        typeResult = TYPE_RESULT_EXIST_ERROR;
                        continue;
                    }
                    //check do dai serial
                    if (serial.length() > 19) {
                        dto.setResultRevoke(getText("revoke.stock.kit.data.serial.too.long"));
                        typeResult = TYPE_RESULT_EXIST_ERROR;
                        continue;
                    }
                    //check dinh dang ownerCode
                    if (!DataUtil.validateStringByPattern(ownerCode, Const.REGEX.CODE_REGEX)) {
                        dto.setResultRevoke(getText("revoke.stock.kit.data.ownercode.invalid.format"));
                        typeResult = TYPE_RESULT_EXIST_ERROR;
                        continue;
                    }
                    //check dinh dang ownerType
                    if (!("1".equals(ownerType) || "2".equals(ownerType))) {
                        dto.setResultRevoke(getText("revoke.stock.kit.data.ownertype.invalid.format"));
                        typeResult = TYPE_RESULT_EXIST_ERROR;
                        continue;
                    }
                    //check dinh dang revokeType
                    if (!("1".equals(revokeType) || "2".equals(revokeType))) {
                        dto.setResultRevoke(getText("revoke.stock.kit.data.revokeType.invalid.format"));
                        typeResult = TYPE_RESULT_EXIST_ERROR;
                        continue;
                    }
                    //check trung lap noi dung (isdn+serial)
                    if (lsValidDuplicate.contains(contentRow)) {
                        dto.setResultRevoke(getText("revoke.stock.kit.data.duplicate.content.row"));
                        typeResult = TYPE_RESULT_EXIST_ERROR;
                        continue;
                    }
                    lsValidDuplicate.add(contentRow);

                    isdn = IMCommonUtils.validateIsdn(isdn);
                    dto.setIsdn(isdn);
                    dto.setSerial(serial);
                    dto.setOwnerCode(ownerCode);
                    dto.setOwnerType(ownerType);
                    dto.setRevokeType(revokeType);
                    key = ownerCode + "&" + ownerType;

                    //day du lieu vao map de validate
                    if (mapOwnerCodeValidFull.containsKey(key)) {
                        List<RevokeKitFileImportDTO> lsImport = mapOwnerCodeValidFull.get(key);
                        lsImport.add(dto);
                    } else {
                        mapOwnerCodeValidFull.put(key, Lists.newArrayList(dto));
                    }
                }
                //validate + tao gd thu hoi kit
                String arrTmp[];
                String ownerCode;
                Long fromOwnerType;
                Long fromOwnerId;

                //2. validate du lieu ton tai trong DB
                Date sysdate = getSysDate(em);
                ReasonDTO reasonDTO = reasonService.getReason("IMP_REVOKE_KIT", "IMP_REVOKE_KIT");
                if (reasonDTO == null) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "revoke.stock.kit.data.reason.export.not.found");
                }

                //map tong quat luu tru du lieu phan theo : ma kenh+loai kenh > mat hang > serial
                Map<String, Map<Long, List<RevokeKitFileImportDTO>>> mapValidInsertStockTrans = Maps.newHashMap();

                //map tong quat luu tru du lieu phan theo : ma kenh+loai kenh voi value la ownerId + & + ownerType
                Map<String, String> mapSaveOwnerIdOwnerType = Maps.newHashMap();

                for (String keyCodeTmp : mapOwnerCodeValidFull.keySet()) {
                    arrTmp = keyCodeTmp.split("&");
                    ownerCode = arrTmp[0];
                    fromOwnerType = DataUtil.safeToLong(arrTmp[1]);
                    List<RevokeKitFileImportDTO> lsImport = mapOwnerCodeValidFull.get(keyCodeTmp);

                    //2.1 validate ma kenh
                    if (Const.OWNER_TYPE.STAFF_LONG.equals(fromOwnerType)) {
                        StaffDTO staffDTO = staffService.getStaffByStaffCode(ownerCode);
                        if (staffDTO == null) {
                            for (RevokeKitFileImportDTO dto : lsImport) {
                                dto.setResultRevoke(getText("revoke.stock.kit.data.invalid.ownercode"));
                                typeResult = TYPE_RESULT_EXIST_ERROR;
                            }
                            continue;
                        }
                        fromOwnerId = staffDTO.getStaffId();
                    } else {
                        ShopDTO shopDTO = shopService.getShopByCodeAndActiveStatus(ownerCode);
                        if (DataUtil.isNullObject(shopDTO)
                                || Const.CHANNEL_TYPE.CHANNEL_TYPE_BH.equals(shopDTO.getChannelTypeId())
                                || Const.CHANNEL_TYPE.CHANNEL_TYPE_KCN.equals(shopDTO.getChannelTypeId())
                                || Const.CHANNEL_TYPE.CHANNEL_TYPE_TDKT.equals(shopDTO.getChannelTypeId())) {
                            for (RevokeKitFileImportDTO dto : lsImport) {
                                dto.setResultRevoke(getText("revoke.stock.kit.data.invalid.ownercode"));
                                typeResult = TYPE_RESULT_EXIST_ERROR;
                            }
                            continue;
                        }
                        fromOwnerId = shopDTO.getShopId();
                    }
                    mapSaveOwnerIdOwnerType.put(keyCodeTmp, DataUtil.safeToString(fromOwnerId) + "&" + DataUtil.safeToString(fromOwnerType));
                    //map luu tru mat hang
                    Map<Long, List<RevokeKitFileImportDTO>> mapValidInsertStockTransDetail = Maps.newHashMap();
                    //2.2 validate thong tin thu hoi + update vao bang thong tin thu hoi neu co loi
                    for (RevokeKitFileImportDTO dto : lsImport) {
                        //2.2.1 validate thong tin thu hoi du lieu trong DB
                        List<RevokeKitDetailDTO> lsKitDetailDTO = new ArrayList<>();
                        RevokeKitDetailDTO kitDetailDTO;

                        try {
                            lsKitDetailDTO = repository.findRevokeDetailBySerialAndIsdn(dto.getSerial(), dto.getIsdn());
                        } catch (Exception ex) {
                            logger.error(ex.getMessage(), ex);
                            dto.setResultRevoke(getText("revoke.stock.kit.data.exception.invalid.updateNowait"));
                            typeResult = TYPE_RESULT_EXIST_ERROR;
                            continue;
                        }
                        if (lsKitDetailDTO.size() > 1) {
                            dto.setResultRevoke(getTextParam("revoke.stock.kit.data.invalid.kitdetail", dto.getIsdn(), dto.getSerial()));
                            typeResult = TYPE_RESULT_EXIST_ERROR;
                            continue;
                        }
                        if (!DataUtil.isNullOrEmpty(lsKitDetailDTO)) {
                            if (Const.REVOKE_KIT.REVOKE_STATUS1.equals(DataUtil.safeToLong(lsKitDetailDTO.get(0).getRevokeStatus()))) {
                                dto.setResultRevoke(getTextParam("revoke.stock.kit.data.revoked", dto.getIsdn(), dto.getSerial()));
                                typeResult = TYPE_RESULT_EXIST_ERROR;
                                continue;
                            }
                        }

                        if (DataUtil.isNullOrEmpty(lsKitDetailDTO)) {
                            //getText("revoke.stock.kit.data.notfound")
                            kitDetailDTO = validateRevokeKitSub(dto.getIsdn(), dto.getSerial(), sysdate);
                            if (!DataUtil.isNullObject(kitDetailDTO)
                                    && !Const.REVOKE_KIT.VERIFY_STATUS1.equals(kitDetailDTO.getVerifyStatus())) {
                                dto.setResultRevoke(kitDetailDTO.getVerifyDescription());
                                typeResult = TYPE_RESULT_EXIST_ERROR;
                                continue;
                            }
                        } else {
                            kitDetailDTO = lsKitDetailDTO.get(0);
                        }


                        /*if (!DataUtil.safeEqual(fromOwnerId, kitDetailDTO.getOrgOwnerId()) || !DataUtil.safeEqual(fromOwnerType, kitDetailDTO.getOrgOwnerType())) {
                            dto.setResultRevoke(getText("revoke.stock.kit.data.orgownerid.invalid"));
                            typeResult = TYPE_RESULT_EXIST_ERROR;
                        } else if (!DataUtil.safeEqual(dto.getRevokeType(), kitDetailDTO.getRevokeType())) {
                            dto.setResultRevoke(getText("revoke.stock.kit.data.revoketype.invalid"));
                            typeResult = TYPE_RESULT_EXIST_ERROR;
                        } else */
                        if (DataUtil.safeEqual(Const.REVOKE_KIT.VERIFY_STATUS0, kitDetailDTO.getVerifyStatus())) {
                            dto.setResultRevoke(getText("revoke.stock.kit.data.verifystatus0.invalid"));
                            typeResult = TYPE_RESULT_EXIST_ERROR;
                        } else if (!DataUtil.safeEqual(Const.REVOKE_KIT.VERIFY_STATUS1, kitDetailDTO.getVerifyStatus())) {
                            dto.setResultRevoke(getTextParam("revoke.stock.kit.data.verifystatus.other.invalid", DataUtil.safeToString(kitDetailDTO.getVerifyDescription())));
                            typeResult = TYPE_RESULT_EXIST_ERROR;
                        }
                        //2.2.2 cap nhap thu hoi that bai
                        if (!DataUtil.isNullOrEmpty(dto.getResultRevoke())) {
                            kitDetailDTO.setRevokeStatus(Const.REVOKE_KIT.REVOKE_STATUS2);//thu hoi that bai
                            kitDetailDTO.setRevokeDescription(dto.getResultRevoke());
                            try {
                                save(kitDetailDTO);
                            } catch (Exception ex) {
                                typeResult = TYPE_RESULT_EXIST_ERROR;
                                logger.error(ex.getMessage(), ex);
                                dto.setResultRevoke(getText("revoke.stock.kit.data.exception.invalid"));
                            }
                        } else {
                            //2.2.3 cap nhap thu hoi thanh cong
                            kitDetailDTO.setOrgOwnerId(fromOwnerId);
                            kitDetailDTO.setOrgOwnerType(fromOwnerType);
                            kitDetailDTO.setOrgOwnerCode(ownerCode);
                            if (!DataUtil.safeEqual(Const.STRING_CONST_TWO, kitDetailDTO.getVerifyType())) {
                                kitDetailDTO.setRevokeType(DataUtil.safeToLong(dto.getRevokeType()));
                            }
                            try {
                                update(kitDetailDTO);
                            } catch (Exception ex) {
                                typeResult = TYPE_RESULT_EXIST_ERROR;
                                logger.error(ex.getMessage(), ex);
                                dto.setResultRevoke(getText("revoke.stock.kit.data.exception.invalid"));
                            }

                            dto.setRevokeKitDetailDTO(kitDetailDTO);
                            dto.setFromOwnerId(fromOwnerId);
                            dto.setFromOwnerType(fromOwnerType);
                            dto.setReasonCode(kitDetailDTO.getReasonCode());
                            if (mapValidInsertStockTransDetail.containsKey(kitDetailDTO.getProdOfferId())) {
                                List<RevokeKitFileImportDTO> lsImportTrans = mapValidInsertStockTransDetail.get(kitDetailDTO.getProdOfferId());
                                lsImportTrans.add(dto);
                            } else {
                                List<RevokeKitFileImportDTO> lsImportTrans = Lists.newArrayList(dto);
                                mapValidInsertStockTransDetail.put(kitDetailDTO.getProdOfferId(), lsImportTrans);
                            }
                        }
                    }
                    mapValidInsertStockTrans.put(keyCodeTmp, mapValidInsertStockTransDetail);
                }
                //3. xu ly sinh giao dich kho
                //3.3 insert revokeKitTrans
                RevokeKitTransDTO revokeKitTransDTO = new RevokeKitTransDTO();
                revokeKitTransDTO.setCreateDate(sysdate);
                revokeKitTransDTO.setCreateStaffId(actionStaffDTO.getStaffId());
                revokeKitTransDTO.setAddMoneyDate(null);
                revokeKitTransDTO.setAddMoneyStatus(0L);
                revokeKitTransDTO.setCreateShopId(actionStaffDTO.getShopId());
                RevokeKitTransDTO revokeKitTransSaveDTO = revokeKitTransService.save(revokeKitTransDTO);

//                boolean isInsertRevokeTrans = false;

                for (String keyCode : mapValidInsertStockTrans.keySet()) {
                    try {
                        if (!DataUtil.isNullOrEmpty(mapValidInsertStockTrans.get(keyCode))) {
                            insertStockTrans(actionStaffDTO, toOwnerId, toOwnerType, sysdate, reasonDTO.getReasonId(), mapValidInsertStockTrans, mapSaveOwnerIdOwnerType, keyCode, revokeKitTransSaveDTO.getId());
//                            isInsertRevokeTrans = true;
                        }
                    } catch (LogicException ex) {
                        logger.error(ex.getMessage(), ex);
                        typeResult = TYPE_RESULT_EXIST_ERROR;
                        List<RevokeKitFileImportDTO> lsData = mapOwnerCodeValidFull.get(keyCode);
                        for (RevokeKitFileImportDTO dto : lsData) {
                            if (DataUtil.isNullOrEmpty(dto.getResultRevoke())) {
                                dto.setResultRevoke(ex.getDescription());
                            }
                        }
                    } catch (Exception ex) {
                        typeResult = TYPE_RESULT_EXIST_ERROR;
                        logger.error(ex.getMessage(), ex);
                        List<RevokeKitFileImportDTO> lsData = mapOwnerCodeValidFull.get(keyCode);
                        for (RevokeKitFileImportDTO dto : lsData) {
                            if (DataUtil.isNullOrEmpty(dto.getResultRevoke())) {
                                dto.setResultRevoke(getText("revoke.stock.kit.data.exception.invalid"));
                            }
                        }
                    }
                }
//                if (isInsertRevokeTrans) {
//                }
            }
            for (List<RevokeKitFileImportDTO> lsFileRevokeImport : mapOwnerCodeValidFull.values()) {
                for (RevokeKitFileImportDTO dto : lsFileRevokeImport) {
                    lsFileRevokeKitImportAll.get(dto.getIndex()).setResultRevoke(dto.getResultRevoke());
                }
            }
            resultDTO = DataUtil.cloneBean(revokeKitResultDTO);
            resultDTO.setTypeResult(typeResult);

        }
        return resultDTO;
    }

    @Transactional(rollbackFor = Exception.class)
    private void insertStockTrans(StaffDTO actionStaffDTO, Long toOwnerId, Long toOwnerType, Date sysdate, Long reasonId,
                                  Map<String, Map<Long, List<RevokeKitFileImportDTO>>> mapValidInsertStockTrans,
                                  Map<String, String> mapSaveOwnerIdOwnerType, String keyCode, Long revokeKitTransId) throws LogicException, Exception {

        Map<Long, List<RevokeKitFileImportDTO>> mapKitInsert = mapValidInsertStockTrans.get(keyCode);
        if (mapKitInsert.size() > 0) {
            String[] arrOwnerIdAndOwnerType = mapSaveOwnerIdOwnerType.get(keyCode).split("&");
            Long fromOwnerIdTmp = DataUtil.safeToLong(arrOwnerIdAndOwnerType[0]);
            Long fromOwnerTypeTmp = DataUtil.safeToLong(arrOwnerIdAndOwnerType[1]);
            //3.Luu giao dich nhap
            //3.1 luu stock_trans
            StockTransDTO stockTransImport = new StockTransDTO();
            stockTransImport.setFromOwnerId(fromOwnerIdTmp);
            stockTransImport.setFromOwnerType(fromOwnerTypeTmp);
            stockTransImport.setToOwnerId(toOwnerId);
            stockTransImport.setToOwnerType(toOwnerType);
            stockTransImport.setCreateDatetime(sysdate);
            stockTransImport.setStockTransType(Const.STOCK_TRANS_TYPE.EXPORT);
            stockTransImport.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
            stockTransImport.setReasonId(reasonId);
            stockTransImport.setNote(getText("revoke.stock.kit.data.reason.note"));
            StockTransDTO stockTransSaveImport = stockTransService.save(stockTransImport);
            //3.2 luu stock_trans_action
            //tao moi action status =5
            StockTransActionDTO actionImport1 = new StockTransActionDTO();
            actionImport1.setActionCode("IMP_REVOKE_STOCK_KIT_" + stockTransSaveImport.getStockTransId());
            actionImport1.setStockTransId(stockTransSaveImport.getStockTransId());
            actionImport1.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
            actionImport1.setCreateUser(actionStaffDTO.getStaffCode());
            actionImport1.setCreateDatetime(sysdate);
            actionImport1.setActionStaffId(actionStaffDTO.getStaffId());
            actionImport1.setNote(getText("revoke.stock.kit.data.reason.note"));
            StockTransActionDTO actionImportSave = stockTransActionService.save(actionImport1);

            //tao moi action status = 6
            StockTransActionDTO actionImport3 = DataUtil.cloneBean(actionImport1);
            actionImport3.setNote(null);
            actionImport3.setActionCode(null);
            actionImport3.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
            stockTransActionService.save(actionImport3);

            for (Long prodOfferId : mapKitInsert.keySet()) {
                List<RevokeKitFileImportDTO> lsRevokeForInsertSerial = mapKitInsert.get(prodOfferId);

                List<RevokeKitFileImportDTO> lsRevokeStateRetrive = lsRevokeForInsertSerial.stream().filter(x -> Const.GOODS_STATE.RETRIVE.equals(x.getStateId())).collect(Collectors.toList());

                List<RevokeKitFileImportDTO> lsRevokeStateNew = lsRevokeForInsertSerial.stream().filter(x -> Const.GOODS_STATE.NEW.equals(x.getStateId())).collect(Collectors.toList());

                if (!DataUtil.isNullOrEmpty(lsRevokeStateRetrive)) {
                    insertStockDetailByStateId(toOwnerId, toOwnerType, sysdate, revokeKitTransId, stockTransSaveImport, actionImportSave, prodOfferId, lsRevokeStateRetrive, Const.GOODS_STATE.RETRIVE);
                }
                if (!DataUtil.isNullOrEmpty(lsRevokeStateNew)) {
                    //20170222: thay doir : stateId NEW --> RETRIVE - luon lu STATEID = RETRIVE
                    insertStockDetailByStateId(toOwnerId, toOwnerType, sysdate, revokeKitTransId, stockTransSaveImport, actionImportSave, prodOfferId, lsRevokeStateNew, Const.GOODS_STATE.RETRIVE);
                }
            }

        }
    }

    private void insertStockDetailByStateId(Long toOwnerId, Long toOwnerType, Date sysdate, Long revokeKitTransId, StockTransDTO stockTransSaveImport, StockTransActionDTO actionImportSave,
                                            Long prodOfferId, List<RevokeKitFileImportDTO> lsRevokeForInsertSerial, Long stateId) throws Exception {
        //3.4 luu stock_trans_detail
        StockTransDetailDTO detailImport = new StockTransDetailDTO();
        detailImport.setStockTransId(stockTransSaveImport.getStockTransId());
        detailImport.setProdOfferId(prodOfferId);
        detailImport.setStateId(stateId);
        detailImport.setQuantity(DataUtil.safeToLong(lsRevokeForInsertSerial.size()));
        detailImport.setPrice(null);
        detailImport.setAmount(null);
        detailImport.setCreateDatetime(sysdate);
        StockTransDetailDTO detailSaveImport = stockTransDetailService.save(detailImport);

        detailSaveImport.setTableName(Const.STOCK_TYPE.STOCK_KIT_NAME);
        detailSaveImport.setProdOfferTypeId(Const.STOCK_TYPE.STOCK_KIT);

        //3.5 cong kho nhap stock_total
        FlagStockDTO flagStockImport = new FlagStockDTO();
        flagStockImport.setImportStock(true);
        flagStockImport.setInsertStockTotalAudit(true);
        flagStockImport.setImpCurrentQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
        flagStockImport.setImpAvailableQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
        doSaveStockTotal(stockTransSaveImport, Lists.newArrayList(detailSaveImport), flagStockImport, actionImportSave);

        List<StockTransSerialDTO> transSerialDTOs = Lists.newArrayList();

        //3.6 luu stock_trans_serial
        for (RevokeKitFileImportDTO dtoSerial : lsRevokeForInsertSerial) {
            StockTransSerialDTO serialImport = new StockTransSerialDTO();
            serialImport.setFromSerial(dtoSerial.getSerial());
            serialImport.setToSerial(dtoSerial.getSerial());
            serialImport.setQuantity(1L);
            serialImport.setStateId(stateId);
            serialImport.setStockTransId(stockTransSaveImport.getStockTransId());
            serialImport.setCreateDatetime(sysdate);
            serialImport.setProdOfferId(prodOfferId);
            serialImport.setStockTransDetailId(detailSaveImport.getStockTransDetailId());
            StockTransSerialDTO serialDTOSave = stockTransSerialService.save(serialImport);

            transSerialDTOs.add(serialDTOSave);
            //3.7 cap nhap revoke_kist_detail
            RevokeKitDetailDTO kitDetailDTO = DataUtil.cloneBean(dtoSerial.getRevokeKitDetailDTO());
            kitDetailDTO.setRevokeDate(sysdate);
            kitDetailDTO.setRevokeStatus(Const.REVOKE_KIT.REVOKE_STATUS1);//thu hoi kit thanh cong
            kitDetailDTO.setStockTransId(stockTransSaveImport.getStockTransId());
            kitDetailDTO.setRevokeDescription(getText("revoke.stock.kit.data.sucess"));
            kitDetailDTO.setRevokeKitTransId(revokeKitTransId);
            save(kitDetailDTO);
            //3.8 cap nhap stock_kit
            excuteStockKit(detailImport.getProdOfferId(), kitDetailDTO.getIsdn(), dtoSerial.getSerial(), sysdate, toOwnerId, toOwnerType, Const.STATUS.ACTIVE, stateId, sysdate);
        }
    }

    /**
     * ham tru kho
     *
     * @param ownerId
     * @param ownerType
     * @param prodOfferId
     * @param stateId
     * @throws Exception
     * @author thanhnt77
     */
    @Transactional(rollbackFor = Exception.class)
    private void addStockTotal(Long ownerId, Long ownerType, Long prodOfferId, Long stateId) throws LogicException, Exception {
        StringBuilder sqlBuider = new StringBuilder("");
        sqlBuider.append(" UPDATE bccs_im.stock_total  SET current_quantity  = current_quantity  + 1, available_quantity  = available_quantity + 1 ");
        sqlBuider.append(" WHERE owner_id = #ownerId AND owner_type = #onwerType AND prod_offer_id = #prodOfferId AND state_id = #stateId  ");
        Query queryObject = em.createNativeQuery(sqlBuider.toString());
        queryObject.setParameter("ownerId", ownerId);
        queryObject.setParameter("onwerType", ownerType);
        queryObject.setParameter("prodOfferId", prodOfferId);
        queryObject.setParameter("stateId", stateId);
        int result = queryObject.executeUpdate();
        if (result == 0) {
            ProductOfferingDTO offeringDTO = productOfferingService.findOne(prodOfferId);
            throw new LogicException("", "mn.stock.request.notfound.product", offeringDTO != null ? offeringDTO.getCode() : "");
        }
        List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
        if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1) && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())) {
            if (Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()))) {
                addStockTotalIM1(ownerId, ownerType, prodOfferId, stateId);
            }
        }
    }

    /**
     * ham tru kho
     *
     * @param ownerId
     * @param ownerType
     * @param prodOfferId
     * @param stateId
     * @throws Exception
     * @author thanhnt77
     */
    private void addStockTotalIM1(Long ownerId, Long ownerType, Long prodOfferId, Long stateId) throws LogicException, Exception {
        String sql = " UPDATE bccs_im.stock_total  SET quantity = quantity + 1, quantity_issue = quantity_issue + 1  " +
                "WHERE owner_id = ? AND owner_type = ? AND stock_model_id = ? AND state_id = ? AND quantity > 0 AND quantity_issue > 0";
        Query queryObject = emLib.createNativeQuery(sql);
        queryObject.setParameter(1, ownerId);
        queryObject.setParameter(2, ownerType);
        queryObject.setParameter(3, prodOfferId);
        queryObject.setParameter(4, stateId);
        int result = queryObject.executeUpdate();
        if (result == 0) {
            StockModelIm1DTO stockModelIm1DTO = stockTotalIm1Repo.findOneStockModel(prodOfferId);
            throw new LogicException("", "mn.stock.request.notfound.product.bccs1", stockModelIm1DTO != null ? stockModelIm1DTO.getStockModelCode() : "");
        }
    }

    /**
     * ham update stock_kit
     *
     * @param serial
     * @throws Exception
     * @author thanhnt77
     */
    @Transactional(rollbackFor = Exception.class)
    private void excuteStockKit(Long prodOfferId, String isdn, String serial, Date createDate, Long ownerId, Long ownerType, String status, Long stateId, Date sysdate) throws Exception {
        StringBuilder sqlBuider = new StringBuilder("");
        sqlBuider.append(" UPDATE bccs_im.stock_kit  SET owner_id = #ownerId, owner_type = 1, status = 1, update_datetime=#update_datetime, state_id=#stateId  ");
        sqlBuider.append(" WHERE to_number(serial) = to_number(#serial) AND prod_offer_id = #prodOfferId ");

        Query queryUpdate = em.createNativeQuery(sqlBuider.toString());
        queryUpdate.setParameter("ownerId", ownerId);
        queryUpdate.setParameter("serial", serial);
        queryUpdate.setParameter("prodOfferId", prodOfferId);
        queryUpdate.setParameter("update_datetime", sysdate);
        queryUpdate.setParameter("stateId", stateId);
        int result = queryUpdate.executeUpdate();
        if (result == 0) {
            sqlBuider = new StringBuilder("");
            sqlBuider.append(" INSERT INTO bccs_im.stock_kit (id, prod_offer_id, isdn, serial, create_date, owner_id, owner_type, status,state_id,update_datetime,telecom_service_id) ");
            sqlBuider.append("  VALUES   (bccs_im.stock_kit_seq.nextval, #prodOfferId, #isdn, #serial, #createDate, #ownerId, #ownerType, #status, #stateId,#update_datetime,1) ");

            Query queryInsert = em.createNativeQuery(sqlBuider.toString());
            queryInsert.setParameter("prodOfferId", prodOfferId);
            queryInsert.setParameter("isdn", isdn);
            queryInsert.setParameter("serial", serial);
            queryInsert.setParameter("createDate", createDate);
            queryInsert.setParameter("ownerId", ownerId);
            queryInsert.setParameter("ownerType", ownerType);
            queryInsert.setParameter("status", status);
            queryInsert.setParameter("stateId", stateId);
            queryInsert.setParameter("update_datetime", sysdate);
            result = queryInsert.executeUpdate();
            if (result == 0) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.quantity.stockDetail");
            }
        }

        List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
        if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1) && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())) {
            if (Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()))) {
                excuteStockKitIM1(prodOfferId, isdn, serial, createDate, ownerId, ownerType, Const.STATUS.ACTIVE, stateId);
            }
        }
    }

    /**
     * ham update stock_kit
     *
     * @param serial
     * @throws Exception
     * @author thanhnt77
     */
    private void excuteStockKitIM1(Long prodOfferId, String isdn, String serial, Date createDate, Long ownerId, Long ownerType, String status, Long stateId) throws Exception {
        StringBuilder sqlBuider = new StringBuilder("");
        sqlBuider.append(" UPDATE bccs_im.stock_kit  SET owner_id = #ownerId, owner_type = 1, status = 1,state_id=#stateId   ");
        sqlBuider.append(" WHERE to_number(serial) = to_number(#serial) AND stock_model_id = #prodOfferId ");

        Query queryUpdate = emLib.createNativeQuery(sqlBuider.toString());
        queryUpdate.setParameter("ownerId", ownerId);
        queryUpdate.setParameter("serial", serial);
        queryUpdate.setParameter("prodOfferId", prodOfferId);
        queryUpdate.setParameter("stateId", stateId);
        int result = queryUpdate.executeUpdate();
        if (result == 0) {
            sqlBuider = new StringBuilder("");
            sqlBuider.append(" INSERT INTO bccs_im.stock_kit (id, stock_model_id, isdn, serial, create_date, owner_id, owner_type, status,state_id, telecom_service_id) ");
            sqlBuider.append("  VALUES   (bccs_im.stock_kit_seq.nextval, #prodOfferId, #isdn, #serial, #createDate, #ownerId, #ownerType, #status, #stateId, 1) ");

            Query queryInsert = emLib.createNativeQuery(sqlBuider.toString());
            queryInsert.setParameter("prodOfferId", prodOfferId);
            queryInsert.setParameter("isdn", isdn);
            queryInsert.setParameter("serial", serial);
            queryInsert.setParameter("createDate", createDate);
            queryInsert.setParameter("ownerId", ownerId);
            queryInsert.setParameter("ownerType", ownerType);
            queryInsert.setParameter("status", status);
            queryInsert.setParameter("stateId", stateId);
            result = queryInsert.executeUpdate();
            if (result == 0) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.quantity.stockDetail.bccs1");
            }
        }
    }

    @Override
    public List<RevokeKitDetailDTO> searchRevokeKitDetailByShopAndDate(Long shopId, Date fromDate, Date toDate) throws LogicException, Exception {
        return repository.searchRevokeKitDetailByShopAndDate(shopId, fromDate, toDate);
    }

    @Override
    public void doPrepareData(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, FlagStockDTO flagStockDTO) throws LogicException, Exception {

    }

    @Override
    public void doValidate(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {

    }

    @WebMethod
    public RevokeKitResultDTO searchRevokeKitBySerialAndIsdn(RevokeKitResultDTO revokeKitResultDTO) throws LogicException, Exception {
        RevokeKitResultDTO resultDTO = new RevokeKitResultDTO();
        Date sysdate = getSysDate(em);

        if (revokeKitResultDTO != null) {

            //lay thong tin NV dang nhap
            List<RevokeKitFileImportDTO> lsFileRevokeKitImportAll = Lists.newArrayList(revokeKitResultDTO.getLsRevokeKitFileImportDTOs());
            List<RevokeKitFileImportDTO> lsRevokeSearch = Lists.newArrayList();
            String typeResult = TYPE_RESULT_SUSCESS;

            if ("1".equals(revokeKitResultDTO.getTypeSearch())) {//tra cuu don le
                for (RevokeKitFileImportDTO revokekit : lsFileRevokeKitImportAll) {
                    if (DataUtil.isNullObject(revokekit.getSerial())) {
                        continue;
                    } else if (!DataUtil.isNumber(revokekit.getSerial())) {
                        throw new LogicException("RKSER01", getText("stock.import.to.partner.balance.serial.not.handset.invalid"));
                    }
                }
                List<RevokeKitDetailDTO> lsKitDetailDTO = repository.findRevokeDetailByListSearch(lsFileRevokeKitImportAll);
                if (DataUtil.isNullOrEmpty(lsKitDetailDTO)) {
                    for (RevokeKitFileImportDTO validateKitSub : lsFileRevokeKitImportAll) {
                        if (DataUtil.isNullOrEmpty(validateKitSub.getIsdn()) ||
                                DataUtil.isNullOrEmpty(validateKitSub.getSerial())) {
                            continue;
                        }
                        RevokeKitDetailDTO revokeKitSub = validateRevokeKitSub(validateKitSub.getIsdn(), validateKitSub.getSerial(), sysdate);
                        if (!DataUtil.isNullObject(revokeKitSub)
                                && !Const.REVOKE_KIT.VERIFY_STATUS1.equals(revokeKitSub.getVerifyStatus())) {
                            throw new LogicException("RVK", revokeKitSub.getVerifyDescription());
                        }
                        lsKitDetailDTO.add(revokeKitSub);
                    }
                }
                resultDTO.setLsKitDetailDTO(lsKitDetailDTO);
            } else {//tra cuu theo file
                if (!DataUtil.isNullOrEmpty(lsFileRevokeKitImportAll)) {
                    int index = 0;
                    String contentRow;
                    List<String> lsValidDuplicate = Lists.newArrayList();
                    //1. validate format du lieu
                    for (RevokeKitFileImportDTO dto : lsFileRevokeKitImportAll) {
                        dto.setIndex(index);
                        index++;
                        String isdn = DataUtil.safeToString(dto.getIsdn()).trim();
                        String serial = DataUtil.safeToString(dto.getSerial()).trim();
                        contentRow = isdn + serial;

                        //kiem tra du lieu trong
                        if (DataUtil.isNullOrEmpty(isdn) && DataUtil.isNullOrEmpty(serial)) {
                            continue;
                        }
                        //check dinh dang isdn
                        if (!DataUtil.isNullOrEmpty(isdn) && !DataUtil.validateStringByPattern(isdn, Const.REGEX.NUMBER_REGEX)) {
                            dto.setResultRevoke(getText("revoke.stock.kit.data.isdn.invalid.format"));
                            typeResult = TYPE_RESULT_EXIST_ERROR;
                            continue;
                        }
                        //check do dai isdn
                        if (!DataUtil.isNullOrEmpty(isdn) && isdn.length() > 12) {
                            typeResult = TYPE_RESULT_EXIST_ERROR;
                            dto.setResultRevoke(getText("revoke.stock.kit.data.isdn.invalid.length"));
                            continue;
                        }
                        //check dinh dang serial
                        if (!DataUtil.isNullOrEmpty(serial)
                                && !DataUtil.validateStringByPattern(serial, Const.REGEX.NUMBER_REGEX)
                                && DataUtil.isNumber(serial)) {
                            typeResult = TYPE_RESULT_EXIST_ERROR;
                            dto.setResultRevoke(getText("stock.import.to.partner.balance.serial.not.handset.invalid"));
                            continue;
                        }
                        //check do dai serial
                        if (!DataUtil.isNullOrEmpty(serial) && serial.length() > 19) {
                            typeResult = TYPE_RESULT_EXIST_ERROR;
                            dto.setResultRevoke(getText("revoke.stock.kit.data.serial.too.long"));
                            continue;
                        }
                        //check trung lap noi dung (isdn+serial)
                        if (lsValidDuplicate.contains(contentRow)) {
                            typeResult = TYPE_RESULT_EXIST_ERROR;
                            dto.setResultRevoke(getText("revoke.stock.kit.data.duplicate.content.row"));
                            continue;
                        }
                        lsValidDuplicate.add(contentRow);

                        isdn = IMCommonUtils.validateIsdn(isdn);
                        dto.setIsdn(isdn);
                        dto.setSerial(serial);
                        lsRevokeSearch.add(dto);
                    }
                    //2. validate info
                    if (!DataUtil.isNullOrEmpty(lsRevokeSearch)) {
                        List<RevokeKitDetailDTO> lsKitDetailDTO = repository.findRevokeDetailByListSearch(lsRevokeSearch);
                        //Tim thong tin Kit thu hoi:
                        if (!DataUtil.isNullOrEmpty(lsKitDetailDTO)) {
                            resultDTO.setLsKitDetailDTO(lsKitDetailDTO);

                            Map<String, String> mapSerialValid = Maps.newHashMap();

                            for (RevokeKitDetailDTO kitDetailDTO : lsKitDetailDTO) {
                                mapSerialValid.put(kitDetailDTO.getIsdn(), kitDetailDTO.getSerial());
                            }

                            List<String> lsSerial = Lists.newArrayList(mapSerialValid.values());

                            for (RevokeKitFileImportDTO importDTO : lsRevokeSearch) {
                                if (!DataUtil.isNullOrEmpty(importDTO.getIsdn())
                                        && !DataUtil.isNullOrEmpty(importDTO.getSerial())
                                        && !importDTO.getSerial().equals(mapSerialValid.get(importDTO.getIsdn()))) {
//                                    typeResult = TYPE_RESULT_EXIST_ERROR;
//                                    //lsFileRevokeKitImportAll.get(importDTO.getIndex()).setResultRevoke(getText("revoke.stock.kit.data.search.notfound"));
//                                    lsFileRevokeKitImportAll.get(importDTO.getIndex()).setResultRevoke(revokeKitSub.getVerifyDescription());
//                                } else {
                                    RevokeKitDetailDTO revokeKitSub = validateRevokeKitSub(importDTO.getIsdn(), importDTO.getSerial(), sysdate);
                                    if (!DataUtil.isNullObject(revokeKitSub)
                                            && !Const.REVOKE_KIT.REVOKE_STATUS1.equals(revokeKitSub.getVerifyStatus())) {
                                        typeResult = TYPE_RESULT_EXIST_ERROR;
                                        lsFileRevokeKitImportAll.get(importDTO.getIndex()).setResultRevoke(revokeKitSub.getVerifyDescription());
                                        continue;
                                    }
                                    lsKitDetailDTO.add(revokeKitSub);
                                }


//                                else if (!DataUtil.isNullOrEmpty(importDTO.getIsdn()) // check ton tai isdn
//                                        && !mapSerialValid.containsKey(importDTO.getIsdn())) {
//                                    typeResult = TYPE_RESULT_EXIST_ERROR;
//                                    lsFileRevokeKitImportAll.get(importDTO.getIndex()).setResultRevoke(getText("revoke.stock.kit.data.search.notfound"));
//
//                                } else if (!DataUtil.isNullOrEmpty(importDTO.getSerial()) // check ton tai serial
//                                        && !lsSerial.contains(importDTO.getSerial())) {
//                                    typeResult = TYPE_RESULT_EXIST_ERROR;
//                                    lsFileRevokeKitImportAll.get(importDTO.getIndex()).setResultRevoke(getText("revoke.stock.kit.data.search.notfound"));
//                                }
                            }

                        } else {
                            //update Thu hoi Kit: 20170221 truong hop kit khong thuoc bang RevokeKitDetail, thong tin kit thu hoi qua Thue Bao
                            for (RevokeKitFileImportDTO importDTO : lsRevokeSearch) {
                                RevokeKitDetailDTO revokeKitSub = validateRevokeKitSub(importDTO.getIsdn(), importDTO.getSerial(), sysdate);
                                if (!DataUtil.isNullObject(revokeKitSub)
                                        && !Const.REVOKE_KIT.REVOKE_STATUS1.equals(revokeKitSub.getVerifyStatus())) {
                                    typeResult = TYPE_RESULT_EXIST_ERROR;
                                    lsFileRevokeKitImportAll.get(importDTO.getIndex()).setResultRevoke(revokeKitSub.getVerifyDescription());
                                    continue;
                                }
                                lsKitDetailDTO.add(revokeKitSub);

                            }
                            resultDTO.setLsKitDetailDTO(lsKitDetailDTO);
                            // code cu
//                            typeResult = TYPE_RESULT_EXIST_ERROR;
//                            for (RevokeKitFileImportDTO importDTO : lsRevokeSearch) {
//                                lsFileRevokeKitImportAll.get(importDTO.getIndex()).setResultRevoke(getText("revoke.stock.kit.data.search.notfound"));
//                            }
                        }
                    }
                    resultDTO.setLsRevokeKitFileImportDTOs(Lists.newArrayList());
                    if (TYPE_RESULT_EXIST_ERROR.equals(typeResult)) {
                        resultDTO.setLsRevokeKitFileImportDTOs(lsFileRevokeKitImportAll);
                    }
                }
            }
        }
        return resultDTO;
    }

    public RevokeKitDetailDTO validateRevokeKitSub(String isdn, String serial, Date sysdate) throws Exception, LogicException {
//        Date commonDate = DateUtil.string2Date("20160401", "yyyyMMdd");
        Date commonDate = DateUtil.string2Date("20161101", "yyyyMMdd");
        RevokeKitDetailDTO revokeKitSave = new RevokeKitDetailDTO();
        List<SubscriberDTO> lstSub = repository.findSubsToRevokeBySerialAndIsdn(isdn, serial);
        if (DataUtil.isNullOrEmpty(lstSub)) {
            revokeKitSave.setVerifyDescription("ERRSUB01" + getText("revoke.stock.kit.data.isdn.serial.err01"));
            return revokeKitSave;
        }
        Long revokeType = 2L;
        Long verifyStatus = Const.REVOKE_KIT.VERIFY_STATUS1;
        String verifyDescription = null;
//        revokeKitSave.setVerifyDate(sysdate);
        Long prodOfferId = null;
        try {
            // neu danh sach tra cuu Sub co result != 1
            SubscriberDTO subscriberDTO = new SubscriberDTO();
            if (!DataUtil.isNullOrEmpty(lstSub)) {
                subscriberDTO = lstSub.get(0);
                for (SubscriberDTO sub : lstSub) {
                    if (Const.SUBSCRIBER.STATUS_ACTIVE.equals(sub.getStatus())) {
                        subscriberDTO = sub;
                    }
                }
//                verifyDescription = getText("revoke.stock.kit.data.isdn.serial.err01");
//                throw new LogicException("ERRSUB01", verifyDescription);
            }
            if (!DataUtil.isNullObject(subscriberDTO)) {
                revokeKitSave.setSubId(subscriberDTO.getSubId());
                revokeKitSave.setIsdn(isdn);
                revokeKitSave.setSerial(serial);
                revokeKitSave.setStaDatetime(subscriberDTO.getStaDateTime());
                revokeKitSave.setProductCode(subscriberDTO.getProductCode());
            }

            //validate Sub: kiem tra ly do
            if (DataUtil.isNullOrEmpty(subscriberDTO.getRegTypeID())) {
                subscriberDTO.setReasonCode("KIT_DONLE");
            } else if (!DataUtil.isNullOrEmpty(subscriberDTO.getRegTypeID())) {
                ReasonDTO reason = repository.findReasonWithReasonId(subscriberDTO.getRegTypeID());
                if (DataUtil.isNullObject(reason)) {
                    subscriberDTO.setReasonCode("KIT_DONLE");
                } else {
                    subscriberDTO.setReasonCode(reason.getReasonCode());
                }
                revokeKitSave.setReasonCode(subscriberDTO.getReasonCode());
            } /*else if (!(!Const.SUBSCRIBER.ACT_STATUS_INACTIVE.equals(subscriberDTO.getActStatus()) // khog kiem tra Status
                    && !DataUtil.isNullObject(subscriberDTO.getStaDateTime()))) {
                verifyDescription = getText("revoke.stock.kit.data.isdn.serial.err03");
                throw new LogicException("ERRSUB03", verifyDescription);
            }  else */
            if (DataUtil.isNullObject(subscriberDTO.getActStatus())) {
                verifyDescription = getText("revoke.stock.kit.data.isdn.serial.err02");
                throw new LogicException("ERRSUB02", verifyDescription);
            } else {
                List<StockKitIm1DTO> lstCheckStockIm1 = stockKitRepo.findStockKitAndHisStockKitBySerial(serial);
                if (DataUtil.isNullOrEmpty(lstCheckStockIm1)) {
                    subscriberDTO.setReasonCode("KIT_DONLE");
//                    verifyDescription = getText("revoke.stock.kit.data.isdn.serial.err09");
//                    throw new LogicException("ERRSUB05", verifyDescription);
                }
            }

//                                else if (Const.SUBSCRIBER.ACT_STATUS_INACTIVE.equals(subscriberDTO.getActStatus())
//                                        && (!Const.SUBSCRIBER.STATUS_ACTIVE.equals(subscriberDTO.getStatus())
//                                        || !DataUtil.isNullObject(subscriberDTO.getStaDateTime()))
//                                        ) {
//                                    typeResult = TYPE_RESULT_EXIST_ERROR;
//                                    lsFileRevokeKitImportAll.get(importDTO.getIndex()).setResultRevoke("ERRSUB03" + getText("revoke.stock.kit.data.isdn.serial.err03"));
//                                }
//                                else if (!Const.SUBSCRIBER.ACT_STATUS_INACTIVE.equals(subscriberDTO.getActStatus())
//                                        && DataUtil.isNullObject(subscriberDTO.getStaDateTime())) {
//                                    typeResult = TYPE_RESULT_EXIST_ERROR;
//                                    lsFileRevokeKitImportAll.get(importDTO.getIndex()).setResultRevoke("ERRSUB04" + getText("revoke.stock.kit.data.isdn.serial.err04"));
//                                }
            //END validate Sub
            // thue bao o trang thai dau KIT:
            //set Ngay kich hoat
            if (Const.SUBSCRIBER.REASON_DNTD.equals(subscriberDTO.getReasonCode())) {
                if (subscriberDTO.getStaDateTime().compareTo(commonDate) >= 0) {
                    verifyDescription = getText("revoke.stock.kit.data.isdn.serial.err04");
                    throw new LogicException("ERRSUB04", verifyDescription);
                } else {
                    List<StockKitIm1DTO> lstCheck = stockKitRepo.findStockKitAndHisStockKitBySerial(serial);
                    if (DataUtil.isNullOrEmpty(lstCheck)) {
                        verifyDescription = getText("revoke.stock.kit.data.isdn.serial.err09");
                        throw new LogicException("ERRSUB05", verifyDescription);
                    } else if (!DataUtil.isNullOrEmpty(lstCheck) && !Const.LONG_OBJECT_ONE.equals(DataUtil.safeToLong(lstCheck.size()))) {
                        verifyDescription = getText("revoke.stock.kit.data.isdn.serial.err05");
                        throw new LogicException("ERRSUB05", verifyDescription);
                    }
                    StockKitIm1DTO stockKit = lstCheck.get(0);
                    if (!Const.REVOKE_KIT.VERIFY_STATUS0.equals(stockKit.getStatus())) {
                        verifyDescription = getText("revoke.stock.kit.data.isdn.serial.err06");
                        throw new LogicException("ERRSUB06", verifyDescription);
                    }
                    prodOfferId = stockKit.getStockModelId();
                }

            } // thue bao dau noi don le
            else {
                // kiem tra cau hinh KIT thu hoi
                prodOfferId = repository.findProductOfferCodeToRevokeKit();
                if (DataUtil.isNullOrZero(prodOfferId)) {
                    verifyDescription = getText("revoke.stock.kit.data.isdn.serial.err07");
                    throw new LogicException("ERRSUB07", verifyDescription);
                }
                List<StockKitDTO> lstStockKit = stockKitRepo.getStatusStockKit(prodOfferId, serial);
                if (!DataUtil.isNullOrEmpty(lstStockKit)) {
                    verifyDescription = getText("revoke.stock.kit.data.isdn.serial.err08");
                    throw new LogicException("ERRSUB06", verifyDescription);
                }
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            verifyStatus = Const.REVOKE_KIT.VERIFY_STATUS2;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            revokeKitSave.setVerifyDescription("ERRRKSUB01" + getText("common.error.process.happened"));
            return revokeKitSave;
        }

        if (Const.REVOKE_KIT.VERIFY_STATUS1.equals(verifyStatus)) {
            revokeKitSave.setPrice(0L);
            verifyDescription = null;
        }
        revokeKitSave.setRevokeType(revokeType);
        revokeKitSave.setVerifyStatus(verifyStatus);
        revokeKitSave.setVerifyDescription(DataUtil.isNullOrEmpty(verifyDescription) ? "Thanh cong" : verifyDescription);
        revokeKitSave.setVerifyDate(sysdate);
        revokeKitSave.setProdOfferId(prodOfferId);
        revokeKitSave.setVerifyType("2");
        RevokeKitDetailDTO revokeKitResult = save(revokeKitSave);
        return revokeKitResult;
    }

}
