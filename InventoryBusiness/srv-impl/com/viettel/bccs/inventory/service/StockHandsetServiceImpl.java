package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.IMCommonUtils;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.InvoiceTemplate;
import com.viettel.bccs.inventory.model.Staff;
import com.viettel.bccs.inventory.model.StockHandset;
import com.viettel.bccs.inventory.repo.StockHandsetRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.WebMethod;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import java.sql.Connection;
import java.util.Date;
import java.util.List;

@Service
public class StockHandsetServiceImpl extends BaseServiceImpl implements StockHandsetService {

    private final BaseMapper<StockHandset, StockHandsetDTO> mapper = new BaseMapper(StockHandset.class, StockHandsetDTO.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;
    @Autowired
    private StockHandsetRepo repository;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private OptionSetValueService optionSetValueService;

    public static final Logger logger = Logger.getLogger(StockHandsetService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public StockHandsetDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<StockHandsetDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<StockHandsetDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    public StockHandsetDTO checkSerialSale(Long productOfferingId, String serialRecover, String status) throws Exception {
        List<FilterRequest> lst = Lists.newArrayList();
        StockHandsetDTO stockHandsetDTO = null;
        lst.add(new FilterRequest(StockHandset.COLUMNS.PRODOFFERID.name(), FilterRequest.Operator.EQ, productOfferingId));
        lst.add(new FilterRequest(StockHandset.COLUMNS.SERIAL.name(), FilterRequest.Operator.EQ, serialRecover));
        lst.add(new FilterRequest(StockHandset.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, DataUtil.safeToLong(status)));
        List<StockHandsetDTO> listStockHandset = findByFilter(lst);
        if (!DataUtil.isNullOrEmpty(listStockHandset)) {
            stockHandsetDTO = listStockHandset.get(0);
        }
        return stockHandsetDTO;
    }

    @WebMethod
    public StockHandsetDTO getStockHandsetById(Long productOfferingId) throws Exception {
        List<FilterRequest> lst = Lists.newArrayList();
        StockHandsetDTO stockHandsetDTO = new StockHandsetDTO();
        lst.add(new FilterRequest(StockHandset.COLUMNS.PRODOFFERID.name(), FilterRequest.Operator.EQ, productOfferingId));
        List<StockHandsetDTO> listStockHandset = mapper.toDtoBean(repository.findAll(repository.toPredicate(lst)));
        if (!DataUtil.isNullObject(listStockHandset)) {
            stockHandsetDTO = listStockHandset.get(0);
        }
        return stockHandsetDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(StockHandsetDTO stockHandsetDTO) throws Exception {
        BaseMessage result = new BaseMessage(true);
        try {
            repository.save(mapper.toPersistenceBean(stockHandsetDTO));
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            result.setSuccess(false);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "stockHand.update");
        }
        result.setErrorCode(ErrorCode.ERROR_STANDARD.SUCCESS);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StockHandsetDTO create(StockHandsetDTO stockHandsetDTO) throws Exception {
        try {
            return mapper.toDtoBean(repository.save(mapper.toPersistenceBean(stockHandsetDTO)));
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "stockHand.update");
        }
    }

    @Override
    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage updateUctt(Long productOfferId, String serialRecover, Long ownerId, Long ownerType, Long productOfferIdUCTT, Date updateDatetime) throws Exception, LogicException {
        return repository.updateUctt(productOfferId, serialRecover, ownerId, ownerType, productOfferIdUCTT, updateDatetime);
    }

    @Override
    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage updateUcttIm1(Long productOfferId, String serialRecover, Long ownerId, Long ownerType, Long productOfferIdUCTT, Date updateDatetime) throws Exception, LogicException {
        return repository.updateUcttIm1(productOfferId, serialRecover, ownerId, ownerType, productOfferIdUCTT, updateDatetime);
    }

    @Override
    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public void doSaveStockGoods(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail,
                                 FlagStockDTO flagStockDTO) throws Exception, LogicException {
        repository.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    public int updateForStockRescue(Long stateId, Long ownerId, String serial, Long prodOfferId) throws Exception {
        return repository.updateForStockRescue(stateId, ownerId, serial, prodOfferId);
    }

    public int updateForStockRescueIM1(Long stateId, Long ownerId, String serial, Long prodOfferId) throws Exception {
        return repository.updateForStockRescueIM1(stateId, ownerId, serial, prodOfferId);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<UpdateSerialGponDTO> updateSerialGpon(String updateType, List<UpdateSerialGponDTO> lstSerial) throws LogicException, Exception {
        List<UpdateSerialGponDTO> lst = DataUtil.cloneBean(lstSerial);
        if (!DataUtil.isNullOrEmpty(lst)) {
            //check mat hang ton tai
            if (!DataUtil.isNullOrZero(lst.get(0).getProdOfferingId())) {
                ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(lst.get(0).getProdOfferingId());
                if (DataUtil.isNullObject(productOfferingDTO) || DataUtil.isNullOrZero(productOfferingDTO.getProductOfferingId())) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "validate.stock.inspect.productOffering.notFound");
                }
            }

            //Kiem tra co dong bo sang IM 1
            boolean updateBCCS1 = false;

            List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
            if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1) && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())) {
                if (Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()))) {
                    updateBCCS1 = true;
                }
            }

            if (DataUtil.safeEqual(updateType, Const.UPDATE_SERIAL_GPON.TYPE_GPON)) {
                for (UpdateSerialGponDTO updateSerialGponDTO : lst) {
                    if (!DataUtil.isNullOrEmpty(updateSerialGponDTO.getSerial()) && !DataUtil.isNullOrEmpty(updateSerialGponDTO.getStockGPon())) {
                        if (updateSerialGponDTO.getSerial().length() <= 20 && updateSerialGponDTO.getStockGPon().length() <= 50
                                && DataUtil.checkNotSpecialCharacter(updateSerialGponDTO.getStockGPon())) {
                            if (repository.updateSerialGpon(updateSerialGponDTO.getSerial().trim(), updateSerialGponDTO.getStockGPon().trim(), updateSerialGponDTO.getProdOfferingId())) {
                                if (updateBCCS1) {
                                    if (repository.updateSerialGponIM1(updateSerialGponDTO.getSerial().trim(), updateSerialGponDTO.getStockGPon().trim(), updateSerialGponDTO.getProdOfferingId())) {
                                        updateSerialGponDTO.setStatus(getText("list.update.serial.gpon.status.susscess"));
                                        updateSerialGponDTO.setSuccess(1L);
                                    } else {
                                        updateSerialGponDTO.setStatus(getText("list.update.serial.gpon.status.not.exsit.IM1"));
                                    }
                                } else {
                                    updateSerialGponDTO.setStatus(getText("list.update.serial.gpon.status.susscess"));
                                    updateSerialGponDTO.setSuccess(1L);
                                }

                            } else {
                                updateSerialGponDTO.setStatus(getText("list.update.serial.gpon.status.not.exsit"));
                            }
                        } else {
                            updateSerialGponDTO.setStatus(getText("list.update.serial.gpon.status.invalid.maxlength"));
                        }
                    } else {
                        updateSerialGponDTO.setStatus(getText("list.update.serial.gpon.status.require"));
                    }
                }
            } else {
                for (UpdateSerialGponDTO updateSerialGponDTO : lst) {
                    if (!DataUtil.isNullOrEmpty(updateSerialGponDTO.getSerial()) && !DataUtil.isNullOrEmpty(updateSerialGponDTO.getCadId())
                            && !DataUtil.isNullOrEmpty(updateSerialGponDTO.getMacId())) {
                        if (updateSerialGponDTO.getSerial().length() <= 20 && updateSerialGponDTO.getCadId().length() <= 50
                                && updateSerialGponDTO.getMacId().length() <= 50) {
                            if (repository.updateSerialMultiScreen(updateSerialGponDTO.getSerial().trim(), updateSerialGponDTO.getCadId().trim(), updateSerialGponDTO.getMacId().trim(), updateSerialGponDTO.getProdOfferingId())) {
                                if (updateBCCS1) {
                                    if (repository.updateSerialMultiScreenIM1(updateSerialGponDTO.getSerial().trim(), updateSerialGponDTO.getCadId().trim(), updateSerialGponDTO.getMacId(), updateSerialGponDTO.getProdOfferingId())) {
                                        updateSerialGponDTO.setStatus(getText("list.update.serial.gpon.status.susscess"));
                                        updateSerialGponDTO.setSuccess(1L);
                                    } else {
                                        updateSerialGponDTO.setStatus(getText("list.update.serial.gpon.status.not.exsit.IM1"));
                                    }
                                } else {
                                    updateSerialGponDTO.setStatus(getText("list.update.serial.gpon.status.susscess"));
                                    updateSerialGponDTO.setSuccess(1L);
                                }
                            } else {
                                updateSerialGponDTO.setStatus(getText("list.update.serial.gpon.status.not.exsit"));
                            }
                        } else {
                            updateSerialGponDTO.setStatus(getText("list.update.serial.gpon.status.cad"));
                        }
                    } else {
                        updateSerialGponDTO.setStatus(getText("list.update.serial.gpon.status.cad.require"));
                    }
                }
            }
        }
        return lst;
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public StockHandsetDTO updateLicenseKey(UpdateLicenseDTO updateLicenseDTO) throws LogicException, Exception {
        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        if (!DataUtil.isNullOrZero(updateLicenseDTO.getProdOfferingId())) {
            productOfferingDTO = productOfferingService.findOne(updateLicenseDTO.getProdOfferingId());
            if (DataUtil.isNullObject(productOfferingDTO) || DataUtil.isNullOrZero(productOfferingDTO.getProductOfferingId())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "validate.stock.inspect.productOffering.notFound");
            }
        }
        StockHandsetDTO stockHandsetDTO = repository.checkExsitStockHandset(updateLicenseDTO.getProdOfferingId(), updateLicenseDTO.getSerial().trim(),
                updateLicenseDTO.getShopAntiId(), Const.OWNER_TYPE.STAFF_LONG, Const.STATE_STATUS.NEW, updateLicenseDTO.getUpdateType());
        if (DataUtil.isNullObject(stockHandsetDTO)) {
            throw new LogicException("", "utilities.update.pincode.card.update.check.file.not.found.serial", updateLicenseDTO.getSerial().trim());
        }
        //lock ban ghi duoc giao
        StockHandset stockHandset = repository.findOne(stockHandsetDTO.getId());
        try {
            em.lock(stockHandset, LockModeType.PESSIMISTIC_READ);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "utilities.update.license.key.anti.virus.lock.serial");
        }
        String updateType = updateLicenseDTO.getUpdateType();
        //Neu la cap nhat moi nhung da co license key thi bao loi
        if (updateType != null && Const.UPDATE_PINCODE_CARD.TYPE_NEW.equals(updateType) && stockHandset.getLicenseKey() != null && !"".equals(stockHandset.getLicenseKey())) { //Neu la cap nhat moi
            throw new LogicException("", "utilities.update.license.key.anti.virus.error.type.new", updateLicenseDTO.getSerial().trim(), productOfferingDTO.getCode());
        }
        //Neu la cap nhat lai nhung chua co license key thi bao loi
        if (updateType != null && Const.UPDATE_PINCODE_CARD.TYPE_OLD.equals(updateType) && (stockHandset.getLicenseKey() == null || "".equals(stockHandset.getLicenseKey()))) { //Neu la cap nhat lai
            throw new LogicException("", "utilities.update.license.key.anti.virus.error.type.old", updateLicenseDTO.getSerial().trim(), productOfferingDTO.getCode());
        }
        String tempPincodeEncrypt = DataUtil.encryptByDES64(updateLicenseDTO.getLicenseKey().trim());
//        String tempPincodeEncrypt = updateLicenseDTO.getLicenseKey().trim();
        stockHandset.setLicenseKey(tempPincodeEncrypt);
        stockHandset.setUpdateDatetime(getSysDate(em));
        StockHandset result = repository.save(stockHandset);
        return mapper.toDtoBean(result);
    }

    @Override
    public StockHandsetDTO checkExsitStockHandset(Long prodOfferId, String serial, Long ownerId, Long ownerType, Long stateId, String updateType) throws LogicException, Exception {
        return repository.checkExsitStockHandset(prodOfferId, serial, ownerId, ownerType, stateId, updateType);
    }

    @Override
    public int updateListLicenseKey(List<UpdateLicenseDTO> lstUpdateLicenseDTOs, Long ownerId, Long prodOfferId, String updateType) throws LogicException, Exception {
        if (!DataUtil.isNullOrZero(prodOfferId)) {
            ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(prodOfferId);
            if (DataUtil.isNullObject(productOfferingDTO) || DataUtil.isNullOrZero(productOfferingDTO.getProductOfferingId())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "validate.stock.inspect.productOffering.notFound");
            }
        }
        Connection conn = null;
        int result = 0;
        try {
            conn = IMCommonUtils.getDBIMConnection();
            conn.setAutoCommit(false);
            result = repository.updateListLicenseKey(conn, lstUpdateLicenseDTOs, ownerId, prodOfferId, updateType);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            if (conn != null && !conn.isClosed()) {
                conn.rollback();
            }
            throw ex;
        } finally {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        }
        return result;
    }

    @Override
    public List<StockHandsetDTO> findStockHandSet(Long stockHandsetId, Long prodOfferId, String serial, Long ownerId, Long ownerType, Long stateId, Long status) throws LogicException, Exception {
        List<FilterRequest> lsRequest = Lists.newArrayList();
        if (stockHandsetId != null) {
            lsRequest.add(new FilterRequest(StockHandset.COLUMNS.ID.name(), FilterRequest.Operator.EQ, stockHandsetId));
        }
        if (prodOfferId != null) {
            lsRequest.add(new FilterRequest(StockHandset.COLUMNS.PRODOFFERID.name(), FilterRequest.Operator.EQ, prodOfferId));
        }
        if (!DataUtil.isNullOrEmpty(serial)) {
            lsRequest.add(new FilterRequest(StockHandset.COLUMNS.SERIAL.name(), FilterRequest.Operator.EQ, serial));
        }
        if (ownerId != null) {
            lsRequest.add(new FilterRequest(StockHandset.COLUMNS.OWNERID.name(), FilterRequest.Operator.EQ, ownerId));
        }
        if (stateId != null) {
            lsRequest.add(new FilterRequest(StockHandset.COLUMNS.STATEID.name(), FilterRequest.Operator.EQ, stateId));
        }
        if (status != null) {
            lsRequest.add(new FilterRequest(StockHandset.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, status));
        }
        if (ownerType != null) {
            lsRequest.add(new FilterRequest(StockHandset.COLUMNS.OWNERTYPE.name(), FilterRequest.Operator.EQ, ownerType));
        }
        return findByFilter(lsRequest);
    }

    @Override
    public Date findExportDateBySerial(String productOfferCode, String serial) throws Exception, LogicException {

        ProductOfferingDTO productOffering = productOfferingService.findByProductOfferCode(productOfferCode, Const.STATUS.ACTIVE);

        if (!DataUtil.isNullObject(productOffering)) {
            return repository.findExportDateBySerial(productOffering.getProductOfferingId(), serial);
        }

        return null;
    }

    public Long getQuantityStockX(Long ownerId, Long ownerType, Long prodOfferId, List<Serial> lstSerial, Long status, Long stateId) throws LogicException, Exception {
        return repository.getQuantityStockX(ownerId, ownerType, prodOfferId, lstSerial, status, stateId);
    }

    public StockHandsetDTO getStockModelSoPin(String serial) {
        return repository.getStockModelSoPin(serial);
    }

    public boolean checkExistSerial(String serial) {
        return repository.checkExistSerial(serial);
    }

    public boolean checkUnlockSerial(String serial) {
        return repository.checkUnlockSerial(serial);
    }

    public Object[] getInfomationSerial(String serial) {
        return repository.getInfomationSerial(serial);
    }

    public boolean saveUnlockG6(String serial, Long prodOfferId) {
        return repository.saveUnlockG6(serial, prodOfferId);
    }

    @Override
    public StockHandsetDTO findStockHandsetByProdIdAndSerial(Long prodOfferId, String serial) throws Exception {
        List<FilterRequest> requests = Lists.newArrayList(new FilterRequest(StockHandset.COLUMNS.PRODOFFERID.name(), FilterRequest.Operator.EQ, prodOfferId),
                new FilterRequest(StockHandset.COLUMNS.SERIAL.name(), FilterRequest.Operator.EQ, serial));
        List<StockHandsetDTO> lsHandsetDTOs = findByFilter(requests);
        if (!DataUtil.isNullOrEmpty(lsHandsetDTOs)) {
            return lsHandsetDTOs.get(0);
        }
        return null;
    }
}
