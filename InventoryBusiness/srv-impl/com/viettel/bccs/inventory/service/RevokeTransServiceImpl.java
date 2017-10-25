package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.im1.service.StockTotalIm1Service;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.RevokeMessage;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.*;
import com.viettel.bccs.inventory.repo.*;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.GetTextFromBundleHelper;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.jws.WebMethod;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class RevokeTransServiceImpl extends BaseServiceImpl implements RevokeTransService {
    private final BaseMapper<RevokeTrans, RevokeTransDTO> mapper = new BaseMapper<>(RevokeTrans.class, RevokeTransDTO.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Autowired
    private RevokeTransRepo repository;
    @Autowired
    private ProductOfferingService productOfferingInventoryService;
    @Autowired
    private RevokeTransDetailService revokeTransDetailService;
    @Autowired
    private InventoryWsRepo inventoryWsRepo;
    @Autowired
    private StaffService staffService;
    @Autowired
    private ProductWs productWs;
    @Autowired
    private ShopService shopService;
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private StaffRepo staffRepo;
    @Autowired
    private StockTransExtRepo stockTransExtRepo;

    public static final Logger logger = Logger.getLogger(RevokeTransService.class);
    public static final String ERR_STOCK_MODEL = "ERR_STOCK_MODEL";
    public static final String ERR_NO_REVOKE_SERIAL = "ERR_NO_REVOKE_SERIAL";
    public static final String ERR_REVOKE_SERIAL_NOT_SAME = "ERR_REVOKE_SERIAL_NOT_SAME";
    public static final String ERR_SERIAL_NOT_FOUND = "ERR_SERIAL_NOT_FOUND";
    public static final String ERR_SERIAL_STATUS_NOT_VALID = "ERR_SERIAL_STATUS_NOT_VALID";

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public RevokeTransDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<RevokeTransDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<RevokeTransDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(RevokeTransDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(RevokeTransDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @Transactional(rollbackFor = Exception.class)
    public RevokeTransDTO save(RevokeTransDTO revokeTransDTO) throws Exception {
        return mapper.toDtoBean(repository.saveAndFlush(mapper.toPersistenceBean(revokeTransDTO)));
    }

    /**
     * ham tra ve doi tuong nhan vien, check ton tai nhan vien, trung lap
     *
     * @param shopId
     * @param areaCode
     * @param staffIdNo
     * @return
     * @throws Exception
     * @author ThanhNT
     */
    private StaffDTO getStaffByIdNo(Long shopId, String areaCode, String staffIdNo) throws Exception {
        StaffDTO staff;
        if (DataUtil.isNullOrEmpty(staffIdNo)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.ws.notfound.staff.idno.deploy");
        }
        List<FilterRequest> requests = Lists.newArrayList();
        requests.add(new FilterRequest(Staff.COLUMNS.IDNO.name(), FilterRequest.Operator.EQ, staffIdNo));
        requests.add(new FilterRequest(Staff.COLUMNS.CHANNELTYPEID.name(), FilterRequest.Operator.EQ, 14L));
        requests.add(new FilterRequest(Staff.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, Const.STATUS.ACTIVE));
        List<StaffDTO> lsStaff = staffService.findByFilter(requests);

        if (DataUtil.isNullOrEmpty(lsStaff)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.ws.notfound.staff.idno.active", staffIdNo);
        }

        if (lsStaff.size() > 1) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.ws.notfound.staff.idno.active.duplicate", staffIdNo);
        }
        staff = lsStaff.get(0);
        //Kiem tra nhan vien co thuoc TTH khong
        if (!DataUtil.isNullOrEmpty(areaCode) && (staff.getShopId() == null || !DataUtil.safeEqual(staff.getShopId(), shopId))) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.ws.staff.idno.check.exist.area", staffIdNo, areaCode);
        }
        return staff;
    }

    @Transactional(rollbackFor = Exception.class)
    public RevokeMessage revoke(String telecomSerivce, String posCodeVTN, String staffIdNo, List<StockWarrantyDTO> stockList,
                                String contractNo, String accountId) throws LogicException, Exception {
        RevokeMessage result = new RevokeMessage();

        Date createDate = getSysDate(em);
        String strStock = "";
        if (stockList != null) {
            for (StockWarrantyDTO sw : stockList) {
                strStock += sw.toString();
            }
        }
        String strParams = MessageFormat.format("(telecomSerivceId:{0};posCodeVTN:{1};staffIdNo{2};stockList:{3};contractNo:{4};accountId:{5})",
                telecomSerivce, posCodeVTN, staffIdNo, strStock, contractNo, accountId);

        //Buoc 1: validate du lieu
        if (DataUtil.isNullOrEmpty(telecomSerivce) || DataUtil.isNullOrEmpty(posCodeVTN) || DataUtil.isNullOrEmpty(stockList) || DataUtil.isNullOrEmpty(accountId)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "rk.err.param.not.valid");
        }

        for (StockWarrantyDTO sw : stockList) {
            if (sw.getProdOfferId() == null || sw.getQuantity() == 0) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "rk.err.param.offer.empty");
            }
        }

        ShopDTO pos = null;
        ShopMapDTO shopMapDTO = productWs.findByShopCodeVTN(posCodeVTN);
        if (shopMapDTO != null && !DataUtil.isNullOrZero(shopMapDTO.getShopId())) {
            List<FilterRequest> requests = Lists.newArrayList();
            requests.add(new FilterRequest(Shop.COLUMNS.SHOPID.name(), FilterRequest.Operator.EQ, shopMapDTO.getShopId()));
//            requests.add(new FilterRequest(Shop.COLUMNS.SHOPID.name(), FilterRequest.Operator.EQ, 24926));
            requests.add(new FilterRequest(Shop.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, Const.STATUS.ACTIVE));
            List<ShopDTO> shopDTOs = shopService.findByFilter(requests);
            if (!DataUtil.isNullOrEmpty(shopDTOs)) {
                pos = shopDTOs.get(0);
            }
        }
        //check ton tai dia ban
        if (pos == null) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.ws.notfound.unit.vtnet", posCodeVTN);
        }

        //Lay thong tin nhan vien trien khai
        StaffDTO staffDTO = getStaffByIdNo(pos.getShopId(), posCodeVTN, staffIdNo);

        //Khai bao list serial hop le se thu hoi ve kho to doi
        List<StockWarrantyDTO> lstValidSerial = new ArrayList<>();
        Long stockTransId = getSequence(em, "STOCK_TRANS_SEQ");

        //Buoc 2: Ghi nhan GD thu hoi
        RevokeTransDTO revokeTrans = new RevokeTransDTO();
        createRevokeTrans(strParams, createDate, telecomSerivce, pos, stockTransId, stockList, contractNo, accountId, lstValidSerial, revokeTrans, staffDTO, 2L);

        //Buoc 3: Phat sinh GD kho la GD nhap
        //Neu co serial hop le
        if (!DataUtil.isNullOrEmpty(lstValidSerial)) {
            stockTransService.createStockTrans(createDate, stockTransId, pos, lstValidSerial, staffDTO, 2L);
        } else {
            //Neu khong co serial nao hop le de tao giao dich kho thi update stock_trans_id trong bang revoke_trans ve null
            revokeTrans.setStockTransId(null);
            save(revokeTrans);
        }

        result.setSuccess(true);
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public void createRevokeTrans(
            String strParams,
            Date createDate,
            String telecomSerivce,
            ShopDTO pos,
            Long stockTransId,
            List<StockWarrantyDTO> stockList,
            String contractNo,
            String accountId,
            List<StockWarrantyDTO> lstValidSerial,
            RevokeTransDTO revokeTrans,
            StaffDTO staff,
            Long revokeType) throws LogicException, Exception {

        RevokeMessage result = new RevokeMessage();

        if (revokeType.compareTo(1L) == 0) {
            if (pos == null) {
                throw new LogicException("", "crt.err.pos.not.found");
            }
        } else {
            if (staff == null) {
                throw new LogicException("", "crt.err.staff.not.found");
            }
        }

        //
        //1: Luu log thong tin thu hoi
        //

        //1.1: Luu trong REVOKE_TRANS
        Long revokeTransId = getSequence(em, "REVOKE_TRANS_SEQ");

        result.setRevokeTransId(revokeTransId);
        revokeTrans.setRevokeTransId(revokeTransId);
        revokeTrans.setStockTransId(stockTransId);
        revokeTrans.setAccountId(accountId);
        revokeTrans.setContractNo(contractNo);
        revokeTrans.setCreateDate(createDate);

        //Neu to doi thuc hien thu hoi
        if (revokeType.compareTo(1L) == 0) {
            revokeTrans.setPosId(pos.getShopId());
        } else {
            //Nhan vien cua hang thuc hien thu hoi
            revokeTrans.setPosId(staff.getStaffId());
            revokeTrans.setShopId(staff.getShopId());
            revokeTrans.setStaffId(staff.getStaffId());
        }

        revokeTrans.setStatus(Const.REVOKE_TRANS.STATUS_NORMAL);

        //revokeTrans.setTelecomServiceId(telecomSerivceId);
        revokeTrans.setCmRequest(strParams);
        save(revokeTrans);

        //Luu stock_trans

        //2.2.2: Luu trong REVOKE_TRANS_DETAIL
        RevokeTransDetailDTO rtd;
        ProductOfferingDTO prodOffer;
        for (StockWarrantyDTO sw : stockList) {

            rtd = new RevokeTransDetailDTO();
            rtd.setRevokeTransDetailId(getSequence(em, "REVOKE_TRANS_DETAIL_SEQ"));
            rtd.setRevokeTransId(revokeTransId);
            rtd.setProdOfferId(sw.getProdOfferId());
            rtd.setCreateDate(createDate);

            if (DataUtil.isNullOrEmpty(sw.getOldSerial())) {
                throw new LogicException("", "crt.param.not.valid");
            }
            rtd.setOldSerial(sw.getOldSerial());
            rtd.setQuantity(1L);
            //Kiem tra thong tin nhap vao
            prodOffer = productOfferingInventoryService.findOne(sw.getProdOfferId());
            if (prodOffer == null) {
                rtd.setErrCode(ERR_STOCK_MODEL);
                rtd.setDescription(getTextParam("crt.err.offer.not.found", DataUtil.safeToString(sw.getProdOfferId())));
                //Tra ve gia tri loi cho QLCVKT
                sw.setErrCode(ERR_STOCK_MODEL);
                sw.setDescription(getTextParam("crt.err.offer.not.found", DataUtil.safeToString(sw.getProdOfferId())));
                revokeTransDetailService.save(rtd);
                //bo qua xu ly ban ghi tiep theo
                continue;
            }
            rtd.setProdOfferTypeId(prodOffer.getProductOfferTypeId());
            sw.setProdOfferTypeId(prodOffer.getProductOfferTypeId());
            //Kiem tra serial thu hoi duoc
            if (DataUtil.isNullOrEmpty(sw.getRevokedSerial())) {
                rtd.setErrCode(ERR_NO_REVOKE_SERIAL);

                if (revokeType.compareTo(1L) == 0) {
                    rtd.setDescription(getText("crt.err.team.not.allow"));
                    sw.setDescription(getText("crt.err.team.not.allow"));
                } else {
                    rtd.setDescription(getText("crt.err.staff.not.allow"));
                    sw.setDescription(getText("crt.err.staff.not.allow"));
                }

                //Tra ve gia tri loi cho QLCVKT/CM
                sw.setErrCode(ERR_NO_REVOKE_SERIAL);

                revokeTransDetailService.save(rtd);
                //bo qua xu ly ban ghi tiep theo
                continue;
            } else {
                //Comment khong xu ly
                /*if (!StringUtils.chkNumber(sw.getRevokedSerial().trim())) {
                    rtd.setErrCode(ERR_SERIAL_NEED_REVOKE_NOT_VALID);

                    if (revokeType.compareTo(1L) == 0) {
                        rtd.setDescription(""); // bo do loi font gay loi jenkin
                        sw.setDescription(""); // bo do loi font gay loi jenkin
                    } else {
                        rtd.setDescription(""); // bo do loi font gay loi jenkin
                        sw.setDescription(""); // bo do loi font gay loi jenkin
                    }

                    //Tra ve gia tri loi cho QLCVKT/CM
                    sw.setErrCode(ERR_SERIAL_NEED_REVOKE_NOT_VALID);

                    session.save(rtd);
                    //bo qua xu ly ban ghi tiep theo
                    continue;
                }*/

            }

            rtd.setRevokedSerial(sw.getRevokedSerial().trim());

            //Serial thu hoi ve khac serial phai thu hoi --> Cap nhat loi
            if (!sw.getOldSerial().trim().equals(sw.getRevokedSerial().trim())) {
                rtd.setErrCode(ERR_REVOKE_SERIAL_NOT_SAME);
                rtd.setDescription(getText("crt.err.serial.not.same"));

                //Tra ve gia tri loi cho QLCVKT
                sw.setErrCode(ERR_REVOKE_SERIAL_NOT_SAME);
                sw.setDescription(getText("crt.err.serial.not.same"));
                revokeTransDetailService.save(rtd);
                //bo qua xu ly ban ghi tiep theo
                continue;
            }

            //Kiem tra thong tin serial truyen vao co dang o trang thai cho thue, cho muon ko
            StockBaseDTO stockBase = inventoryWsRepo.checkValidOfferOfSerial(prodOffer.getProductOfferTypeId(), prodOffer.getProductOfferingId(), sw.getRevokedSerial().trim());
            if (stockBase == null) {
                rtd.setErrCode(ERR_SERIAL_NOT_FOUND);
                rtd.setDescription(getTextParam("crt.err.serial.not.found", sw.getRevokedSerial(), prodOffer.getCode(), prodOffer.getName()));

                sw.setErrCode(ERR_SERIAL_NOT_FOUND);
                sw.setDescription(getTextParam("crt.err.serial.not.found", sw.getRevokedSerial(), prodOffer.getCode(), prodOffer.getName()));

                sw.setIsExists(0L);
                lstValidSerial.add(sw);

                revokeTransDetailService.save(rtd);
                //bo qua xu ly ban ghi tiep theo
                continue;
            }

            // Neu thu hoi thiet bi da thu hoi truoc do
            if (stockBase.getStatus().equals(Const.STATE_STATUS.NEW.toString()) && stockBase.getStateId().compareTo(4L) == 0) {
                throw new LogicException("", getTextParam("im.err.serial.revoke.msg"), sw.getRevokedSerial());
            }
            //Neu trang thai cua serial la da cho thue cho muon --> Tao giao dich kho
            if (DataUtil.safeToString(Const.STATE_STATUS.RENT).equals(stockBase.getStatus()) || DataUtil.safeToString(Const.STATE_STATUS.DEPOSIT).equals(stockBase.getStatus())
                    || stockBase.getStatus().equals(Const.STATE_STATUS.SALE.toString())) {
                rtd.setOldOwnerId(stockBase.getOwnerId());
                rtd.setOldOwnerType(stockBase.getOwnerType());
                rtd.setErrCode("");
                rtd.setDescription(GetTextFromBundleHelper.getText("crt.success"));

                sw.setOldOwnerId(stockBase.getOwnerId());
                sw.setOldOwnerType(stockBase.getOwnerType());
                sw.setErrCode("");
                sw.setDescription(GetTextFromBundleHelper.getText("crt.success"));
                //Add vao list de tao giao dich kho
                lstValidSerial.add(sw);
            } else {
                rtd.setErrCode(ERR_SERIAL_STATUS_NOT_VALID);
                rtd.setDescription(getTextParam("crt.err.serial.invalid", sw.getRevokedSerial()));

                sw.setErrCode(ERR_SERIAL_STATUS_NOT_VALID);
                sw.setDescription(getTextParam("crt.err.serial.invalid", sw.getRevokedSerial()));
                revokeTransDetailService.save(rtd);
                //bo qua xu ly ban ghi tiep theo
                continue;
            }
            revokeTransDetailService.save(rtd);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveAPDeploymentByIdNo(String staffIdNo, String account, String troubleType, List<StockTransDetailDTO> lstStockTransDetail, String transId) throws LogicException, Exception {

        if (DataUtil.isNullOrEmpty(account)) {
            throw new LogicException("NOT_PASS_ACCOUNT", "sap.not.pass.account");
        }

        if (account.trim().length() > 50) {
            throw new LogicException("ACCOUNT_INVALID", "sap.account.invalid");
        }

        if (DataUtil.isNullOrEmpty(troubleType)) {
            throw new LogicException("NOT_PASS_TROUBLE_TYPE", "sap.not.pass.trouble.type");
        }

        if (troubleType.trim().length() > 500) {
            throw new LogicException("TROUBLE_TYPE_INVALID", "sap.trouble.type.invalid");
        }

        if (DataUtil.isNullOrEmpty(transId)) {
            throw new LogicException("NOT_PASS_TRANS_ID", "sap.not.pass.transId");
        }

        //Lay thong tin nhan vien trien khai
        StaffDTO staffDTO = getStaffByIdNo(null, null, staffIdNo);

        //Kiem tra trung giao dich voi transId
        if (stockTransExtRepo.checkDuplicateTrans("TROUBLE_ID", transId)) {
            throw new LogicException("DUPLICATE_TRANS_ID", "sap.duplicate.transId");
        }

        stockTransService.saveAPDeployment(staffDTO, account, troubleType, lstStockTransDetail, transId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void restoreAPDeploymentByIdNo(String staffIdNo, String account, String troubleType, List<StockTransDetailDTO> lstStockTransDetail, String transId) throws LogicException, Exception {

        if (DataUtil.isNullOrEmpty(account)) {
            throw new LogicException("NOT_PASS_ACCOUNT", "sap.not.pass.account");
        }

        if (account.trim().length() > 50) {
            throw new LogicException("ACCOUNT_INVALID", "sap.account.invalid");
        }

        if (DataUtil.isNullOrEmpty(troubleType)) {
            throw new LogicException("NOT_PASS_TROUBLE_TYPE", "sap.not.pass.trouble.type");
        }

        if (troubleType.trim().length() > 500) {
            throw new LogicException("TROUBLE_TYPE_INVALID", "sap.trouble.type.invalid");
        }

//        if (DataUtil.isNullOrEmpty(transId)) {
//            throw new LogicException("NOT_PASS_TRANS_ID", "sap.not.pass.transId");
//        }

        //Lay thong tin nhan vien trien khai
        StaffDTO staffDTO = getStaffByIdNo(null, null, staffIdNo);

        //Kiem tra trung giao dich voi transId
//        if (stockTransExtRepo.checkDuplicateTrans("CANCEL_TROUBLE_ID", transId)) {
//            throw new LogicException("DUPLICATE_TRANS_ID", "sap.duplicate.transId");
//        }

        stockTransService.restoreAPDeployment(staffDTO, account, troubleType, lstStockTransDetail, transId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveAPDeploymentByColl(String staffCode, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {

        if (DataUtil.isNullOrEmpty(staffCode)) {
            throw new LogicException("STAFF_CODE_NULL", "staffCode.validate.require");
        }
        //Lay thong tin nhan vien trien khai
        StaffDTO staffDTO = staffRepo.getStaffByStaffCode(staffCode);

        if (DataUtil.isNullObject(staffDTO) || DataUtil.isNullOrZero(staffDTO.getStaffId())) {
            throw new LogicException("STAFF_CODE_INVALID", "common.valid.staff", staffCode);
        }

        stockTransService.saveAPDeployment(staffDTO, null, null, lstStockTransDetail, null);
    }

    private void checkDuplicateTransId(String transId, String key) throws Exception {

    }
}
