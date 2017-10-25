package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.RevokeMessage;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.dto.ws.StockTotalResult;
import com.viettel.bccs.inventory.dto.ws.StockTotalWsDTO;
import com.viettel.bccs.inventory.message.StockTransMessage;
import com.viettel.bccs.inventory.model.ProductOffering;
import com.viettel.bccs.inventory.model.Staff;
import com.viettel.bccs.inventory.repo.InventoryWsRepo;
import com.viettel.bccs.inventory.repo.PartnerContractRepo;
import com.viettel.bccs.inventory.repo.ProductOfferingRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryServiceImpl extends BaseServiceImpl implements InventoryService {

    public final Logger logger = Logger.getLogger(InventoryService.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Autowired
    private InventoryWsRepo repository;
    @Autowired
    private StaffService staffService;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private ProductWs productWs;
    @Autowired
    private RevokeTransService revokeTransService;
    @Autowired
    private AreaService areaService;
    @Autowired
    private ProductOfferingRepo productOfferingRepo;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private StockHandsetService stockHandsetService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private StockTransMaterialService stockTransMaterialService;
    @Autowired
    private PartnerContractRepo partnerContractRepo;

    @Autowired
    private StockTotalService stockTotalService;

    @Override
    public StockTotalResult getStockTotalShop(String departmentCode, Long type, String prodOfferName) throws Exception {
        StockTotalResult stockTotalResult = new StockTotalResult();
        stockTotalResult.setSuccess(true);

        try {
            if (DataUtil.isNullOrEmpty(departmentCode)) {
                stockTotalResult.setDescription(getText("stock.ws.not.tranfer.information"));
                stockTotalResult.setErrorCode("DEPARTMENT_CODE_NOT_PASS");
                return stockTotalResult;
            }
            //lay ownerId tu ws Product
            ShopMapDTO shopMapDTO = productWs.findByShopCodeVTN(departmentCode);
            Long ownerId = shopMapDTO != null ? shopMapDTO.getShopId() : null;

            if (DataUtil.isNullOrZero(ownerId)) {
                stockTotalResult.setDescription(getTextParam("stock.ws.notfound.team", departmentCode));
                stockTotalResult.setErrorCode("DEPARTMENT_CODE_INVALID");
                return stockTotalResult;
            }
            //set listDetailSerial
            List<StockTransSerialDTO> lstSerial;
            int maxValue = 100;
            int tmp = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue("MIN_QUANTITY_EXPORT_OFFLINE"));
            maxValue = tmp == 0 ? maxValue : tmp;

            List<StockTotalWsDTO> stockTotalDTOList = repository.getStockTotalShop(ownerId, Const.OWNER_TYPE.SHOP_LONG, type, prodOfferName);
            for (StockTotalWsDTO stockTotalWsDTO : stockTotalDTOList) {
                lstSerial = repository.getStockTransSerial(stockTotalWsDTO, maxValue);
                stockTotalWsDTO.setLstSerial(lstSerial);
            }
            stockTotalResult.setLstStockGoods(stockTotalDTOList);
            return stockTotalResult;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            stockTotalResult.setDescription(ex.getMessage());
            stockTotalResult.setErrorCode(Const.CODE_SERVICE.ERR_EXCEPTION);
            stockTotalResult.setSuccess(false);
            return stockTotalResult;
        }

    }

    @Override
    public StockTotalResult getStockTotalStaffByIdNo(String staffIdNo, Long type, String stockModelName) {
        StockTotalResult stockTotalResult = new StockTotalResult();
        try {
            stockTotalResult = new StockTotalResult();
            stockTotalResult.setSuccess(true);

            if (DataUtil.isNullOrEmpty(staffIdNo)) {
                stockTotalResult.setDescription(getText("stock.ws.not.tranfer.cmt"));
                stockTotalResult.setErrorCode("STAFF_ID_NO_NOT_PASS");
                return stockTotalResult;
            }

            List<FilterRequest> listFilter = Lists.newArrayList();
            listFilter.add(new FilterRequest(Staff.COLUMNS.IDNO.name(), FilterRequest.Operator.EQ, staffIdNo));
            listFilter.add(new FilterRequest(Staff.COLUMNS.CHANNELTYPEID.name(), FilterRequest.Operator.EQ, 14L));
            listFilter.add(new FilterRequest(Staff.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, 1L));
            List<StaffDTO> lstStaffs = staffService.findByFilter(listFilter);
            if (DataUtil.isNullOrEmpty(lstStaffs)) {
                stockTotalResult.setDescription(getTextParam("stock.ws.notfound.staff.with.idNo", staffIdNo));
                stockTotalResult.setErrorCode("STAFF_ID_NO_INVALID");
                return stockTotalResult;
            }

            if (lstStaffs.size() > 1) {
                stockTotalResult.setDescription(getTextParam("stock.ws.notfound.staff.exist.idNo", staffIdNo));
                stockTotalResult.setErrorCode("STAFF_ID_NO_DUPLICATE");
                return stockTotalResult;
            }

            Long ownerType = 2L;
            Long ownerId = lstStaffs.get(0).getStaffId();
            List<StockTotalWsDTO> stockTotalDTOList = repository.getStockTotalShop(ownerId, ownerType, type, stockModelName);
            stockTotalResult.setLstStockGoods(stockTotalDTOList);
            return stockTotalResult;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            stockTotalResult.setDescription(ex.getMessage());
            stockTotalResult.setErrorCode(Const.CODE_SERVICE.ERR_EXCEPTION);
            stockTotalResult.setSuccess(false);
            return stockTotalResult;
        }
    }

    @Override
    public StockTotalResult getStockTotalDetailByArea(String departmentCode, List<Long> lstProdOfferId) {
        StockTotalResult stockTotalResult = new StockTotalResult();
        try {

            stockTotalResult = new StockTotalResult();
            stockTotalResult.setSuccess(true);
            if (DataUtil.isNullOrEmpty(departmentCode)) {
                stockTotalResult.setDescription(getText("stock.ws.not.tranfer.information"));
                stockTotalResult.setErrorCode("DEPARTMENT_CODE_NOT_PASS");
                return stockTotalResult;
            }
            ShopMapDTO shopMapDTO = productWs.findByShopCodeVTN(departmentCode);
            Long ownerId = shopMapDTO != null ? shopMapDTO.getShopId() : null;
            if (DataUtil.isNullOrZero(ownerId)) {
                stockTotalResult.setDescription(getTextParam("stock.ws.notfound.team", departmentCode));
                stockTotalResult.setErrorCode("DEPARTMENT_CODE_INVALID");
                return stockTotalResult;
            }
            List<StockTotalWsDTO> stockTotalDTOList = repository.getLstStockTotalDetail(ownerId, lstProdOfferId);
            stockTotalResult.setLstStockGoods(stockTotalDTOList);
            stockTotalResult.setSuccess(true);
            return stockTotalResult;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            stockTotalResult.setDescription(ex.getMessage());
            stockTotalResult.setErrorCode(Const.CODE_SERVICE.ERR_EXCEPTION);
            stockTotalResult.setSuccess(false);
            return stockTotalResult;
        }
    }

    @Override
    public StockTotalResult getStockTotalDetail(String departmentCode, List<Long> lstProdOfferId) {
        StockTotalResult stockTotalResult = new StockTotalResult();
        try {
            stockTotalResult = new StockTotalResult();
            stockTotalResult.setSuccess(true);
            if (DataUtil.isNullOrEmpty(departmentCode)) {
                stockTotalResult.setDescription(getText("stock.ws.not.tranfer.information"));
                stockTotalResult.setErrorCode("DEPARTMENT_CODE_NOT_PASS");
                return stockTotalResult;
            }
            ShopMapDTO shopMapDTO = productWs.findByShopCodeVTNOrig(departmentCode);
            Long ownerId = shopMapDTO != null ? shopMapDTO.getShopId() : null;

            if (DataUtil.isNullOrZero(ownerId)) {
                stockTotalResult.setDescription(getTextParam("stock.ws.notfound.team", departmentCode));
                stockTotalResult.setErrorCode("DEPARTMENT_CODE_INVALID");
                return stockTotalResult;
            }
            List<StockTotalWsDTO> stockTotalDTOList = repository.getLstStockTotalDetail(ownerId, lstProdOfferId);
            stockTotalResult.setLstStockGoods(stockTotalDTOList);
            return stockTotalResult;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            stockTotalResult.setDescription(ex.getMessage());
            stockTotalResult.setErrorCode(Const.CODE_SERVICE.ERR_EXCEPTION);
            stockTotalResult.setSuccess(false);
            return stockTotalResult;
        }
    }

    @Override
    public RevokeMessage revoke(String telecomSerivce, String posCodeVTN, String staffIdNo, List<StockWarrantyDTO> stockList, String contractNo, String accountId) {
        RevokeMessage revokeMessage = new RevokeMessage();
        try {
            revokeMessage = revokeTransService.revoke(telecomSerivce, posCodeVTN, staffIdNo, stockList, contractNo, accountId);
            revokeMessage.setSuccess(true);
        } catch (LogicException ex) {
            revokeMessage.setDescription(ex.getDescription());
            logger.error(ex.getMessage(), ex);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            revokeMessage.setDescription(ex.getMessage());
        }
        return revokeMessage;
    }

    public BaseMessage saveAPDeploymentByIdNo(String staffIdNo, String account, String troubleType, List<StockTransDetailDTO> lstStockTransDetail, String transId) {

        BaseMessage baseMessage = new BaseMessage();

        try {
            revokeTransService.saveAPDeploymentByIdNo(staffIdNo, account, troubleType, lstStockTransDetail, transId);
            baseMessage.setSuccess(true);
        } catch (LogicException ex) {
            baseMessage.setErrorCode(ex.getErrorCode());
            baseMessage.setDescription(ex.getDescription());
            logger.error(ex.getMessage(), ex);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            baseMessage.setDescription(ex.getMessage());
        }
        return baseMessage;
    }

    public BaseMessage restoreAPDeploymentByIdNo(String staffIdNo, String account, String troubleType, List<StockTransDetailDTO> lstStockTransDetail, String transId) {

        BaseMessage baseMessage = new BaseMessage();

        try {
            revokeTransService.restoreAPDeploymentByIdNo(staffIdNo, account, troubleType, lstStockTransDetail, transId);
            baseMessage.setSuccess(true);
        } catch (LogicException ex) {
            baseMessage.setErrorCode(ex.getErrorCode());
            baseMessage.setDescription(ex.getDescription());
            logger.error(ex.getMessage(), ex);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            baseMessage.setDescription(ex.getMessage());
        }
        return baseMessage;
    }

    public BaseMessage saveAPDeploymentByColl(String staffCode, List<StockTransDetailDTO> lstStockTransDetail) {

        BaseMessage baseMessage = new BaseMessage();

        try {
            revokeTransService.saveAPDeploymentByColl(staffCode, lstStockTransDetail);
            baseMessage.setSuccess(true);
        } catch (LogicException ex) {
            baseMessage.setErrorCode(ex.getErrorCode());
            baseMessage.setDescription(ex.getDescription());
            logger.error(ex.getMessage(), ex);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            baseMessage.setDescription(ex.getMessage());
        }
        return baseMessage;
    }

    @Override
    public StockTotalResult getListSearchProductByArea(String areaCode, String productType, String inputSearch) {
        StockTotalResult baseMessage = new StockTotalResult();
        try {
            if (DataUtil.isNullOrEmpty(areaCode)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.total.product.ws.area.empty");
            }
            if (DataUtil.isNullOrEmpty(productType)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.total.product.ws.area.productType");
            }
            if (!("1".equals(productType) || "2".equals(productType))) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.total.product.ws.area.productType.list");
            }
            AreaDTO areaDTO = areaService.findByCode(areaCode);
            if (areaDTO == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.total.product.ws.area.notfound", areaCode);
            }
            List<StockTotalFullDTO> lsResult = productOfferingRepo.getListSearchProductByArea(areaDTO.getProvince(), areaDTO.getDistrict(), productType, inputSearch);
            baseMessage.setSuccess(true);
            if (!DataUtil.isNullOrEmpty(lsResult)) {
                baseMessage.setLstProduct(lsResult);
            } else {
                baseMessage.setDescription(getText("mn.stock.status.isdn.export.nodata"));
            }
        } catch (LogicException ex) {
            baseMessage.setErrorCode(ex.getErrorCode());
            baseMessage.setDescription(ex.getDescription());
            logger.error(ex.getMessage(), ex);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            baseMessage.setDescription(getText("common.error.happen"));
        }
        return baseMessage;
    }

    @Override
    public StockTotalResult getListSerialByProductCode(String prodOfferCode, Long ownerId, Long ownerType) {
        StockTotalResult baseMessage = new StockTotalResult();
        try {
            if (DataUtil.isNullOrEmpty(prodOfferCode)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.total.product.ws.area.prodOfferCode.empty");
            }
            if (ownerId == null || ownerType == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.total.product.ws.area.ownerId.empty");
            }
            ProductOfferingDTO offeringDTO = productOfferingService.getProductByCode(prodOfferCode);
            if (offeringDTO == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.total.product.ws.area.product.empty", prodOfferCode);
            }
            if (!Const.STOCK_TYPE.STOCK_HANDSET.equals(offeringDTO.getProductOfferTypeId())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.total.product.ws.area.stockhandset.empty");
            }
            List<StockHandsetDTO> lsHandset = stockHandsetService.findStockHandSet(null, offeringDTO.getProductOfferingId(), null, ownerId, ownerType, Const.GOODS_STATE.NEW, Const.NEW);

            baseMessage.setSuccess(true);
            if (!DataUtil.isNullOrEmpty(lsHandset)) {
                baseMessage.setLstSerial(lsHandset.stream().map(x -> x.getSerial()).collect(Collectors.toList()));
            } else {
                baseMessage.setDescription(getText("mn.stock.status.isdn.export.nodata"));
            }
        } catch (LogicException ex) {
            baseMessage.setErrorCode(ex.getErrorCode());
            baseMessage.setDescription(ex.getDescription());
            logger.error(ex.getMessage(), ex);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            baseMessage.setDescription(getText("common.error.happen"));
        }
        return baseMessage;
    }

    @Override
    public StockTotalResult getListFullSerialByProductCode(String prodOfferCode, Long ownerId, Long ownerType) {
        StockTotalResult baseMessage = new StockTotalResult();
        try {
            if (DataUtil.isNullOrEmpty(prodOfferCode)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.total.product.ws.area.prodOfferCode.empty");
            }
            if (ownerId == null || ownerType == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.total.product.ws.area.ownerId.empty");
            }
            ProductOfferingDTO offeringDTO = productOfferingService.getProductByCode(prodOfferCode);
            if (offeringDTO == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.total.product.ws.area.product.empty", prodOfferCode);
            }
            if (!Const.STOCK_TYPE.STOCK_HANDSET.equals(offeringDTO.getProductOfferTypeId())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.total.product.ws.area.stockhandset.empty");
            }
            List<StockHandsetDTO> lsHandset = stockHandsetService.findStockHandSet(null, offeringDTO.getProductOfferingId(), null, ownerId, ownerType, Const.GOODS_STATE.NEW, Const.NEW);

            baseMessage.setSuccess(true);
            if (!DataUtil.isNullOrEmpty(lsHandset)) {
                List<StockHandsetDTO> lsResult = Lists.newArrayList();
                for (StockHandsetDTO stockHandsetDTO : lsHandset) {
                    StockHandsetDTO dto = new StockHandsetDTO();
                    dto.setSerial(stockHandsetDTO.getSerial());
                    dto.setSerialGpon(stockHandsetDTO.getSerialGpon());
                    dto.setStatusName(getText("mn.stock.utilities.look.serial.prod.status1"));
                    dto.setStateIdName(getText("create.request.import.product.status1"));
                    lsResult.add(dto);
                }

                baseMessage.setLstSerialHandset(lsResult);
            } else {
                baseMessage.setDescription(getText("mn.stock.status.isdn.export.nodata"));
            }
        } catch (LogicException ex) {
            baseMessage.setErrorCode(ex.getErrorCode());
            baseMessage.setDescription(ex.getDescription());
            logger.error(ex.getMessage(), ex);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            baseMessage.setDescription(getText("common.error.happen"));
        }
        return baseMessage;
    }

    public boolean checkExistVirtualShop(Long shopId) throws LogicException, Exception {
        try {
            if (shopId != null) {
                String virtualShop = optionSetValueService.getValueByTwoCodeOption("VIRTUAL_SHOP", "VIRTUAL_SHOP");
                if (DataUtil.isNullOrEmpty(virtualShop)) {
                    return false;
                }
                String[] arrShopCode = virtualShop.split(",");
                if (DataUtil.isNullOrEmpty(arrShopCode)) {
                    return false;
                }
                List<Long> lsShopId = Lists.newArrayList();
                for (String shopCode : arrShopCode) {
                    ShopDTO shopDTO = shopService.getShopByCodeAndActiveStatus(shopCode);
                    if (shopDTO == null) {
                        continue;
                    }
                    lsShopId.add(shopDTO.getShopId());
                }
                return lsShopId.contains(shopId);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return false;
    }

    @Override
    public BaseMessage excuteCreateTransGift(String staffCode, List<StockTransDetailDTO> lsTransDetailDTOs) throws LogicException, Exception {
        StockTransMessage baseMessage = new StockTransMessage();

        try {
            String actionCode = stockTransMaterialService.excuteCreateTransGift(staffCode, lsTransDetailDTOs);
            baseMessage.setActionCode(actionCode);
            baseMessage.setSuccess(true);
            baseMessage.setDescription(getText("stock.export.gift.staff.sucess"));
        } catch (LogicException ex) {
            baseMessage.setErrorCode(ex.getErrorCode());
            baseMessage.setDescription(ex.getDescription());
//            baseMessage.setParamsMsg(ex.getParamsMsg());
            logger.error(ex.getMessage(), ex);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            baseMessage.setDescription(ex.getMessage());
        }
        return baseMessage;
    }

    @Override
    public BaseMessage validateStockTotal(String staffCode, List<StockTransDetailDTO> lsTransDetailDTOs) throws LogicException, Exception {
        StockTransMessage baseMessage = new StockTransMessage();
        try {
            stockTransMaterialService.validateStockTotal(staffCode, lsTransDetailDTOs);
            baseMessage.setSuccess(true);
        } catch (LogicException ex) {
            baseMessage.setErrorCode(ex.getErrorCode());
            baseMessage.setDescription(ex.getDescription());
//            baseMessage.setParamsMsg(ex.getParamsMsg());
            logger.error(ex.getMessage(), ex);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            baseMessage.setDescription(ex.getMessage());
        }
        return baseMessage;
    }

    @Override
    public List<String> searchContract(String contractCode, String prodOfferCode, Date fromDate, Date toDate) throws Exception {
        Date sysdate = getSysDate(em);
        if (fromDate == null) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.from.date.not.blank");
        }
        if (toDate == null) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.to.date.not.blank");
        }
        if (DateUtil.compareDateToDate(fromDate, sysdate) > 0) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.trans.from.valid");
        }
        if (DateUtil.compareDateToDate(fromDate, toDate) > 0) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.trans.from.than.to");
        }
        long dayMax = DateUtil.daysBetween2Dates(sysdate, fromDate);
        if (dayMax > Const.DAY_MAX_CONTRACT) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.trans.mbccs.from.to.valid", Const.DAY_MAX_CONTRACT);
        }
        return partnerContractRepo.searchContract(contractCode, prodOfferCode, fromDate, toDate);
    }

    @Override
    public List<ProductOfferingTotalDTO> searchProductMaterial(String inputSearch) throws Exception {
        return productOfferingService.getProductOfferingMaterial(inputSearch);
    }

    @Override
    public StockTotalResult validateStockTotalByStaffIdNo(String staffIdNo, List<StockTotalFullDTO> lsStockTotalFullDTO) throws Exception {
        StockTotalResult stockTotalResult = new StockTotalResult();
        List<String> lstError = new ArrayList<>();
        try {
            stockTotalResult = new StockTotalResult();
            stockTotalResult.setErrorCode("NOK");
            stockTotalResult.setSuccess(false);

            if (DataUtil.isNullOrEmpty(staffIdNo)) {
                stockTotalResult.setDescription(getText("stock.ws.not.tranfer.cmt"));
                lstError.add(stockTotalResult.getDescription());
                return stockTotalResult;
            }

            if (DataUtil.isNullOrEmpty(lsStockTotalFullDTO)) {
                stockTotalResult.setDescription(getText("sp.staffExportStockToShop.listProduct.isNull"));
                lstError.add(stockTotalResult.getDescription());
                return stockTotalResult;
            }

            //Check validate CMND
            List<FilterRequest> listFilter = Lists.newArrayList();
            listFilter.add(new FilterRequest(Staff.COLUMNS.IDNO.name(), FilterRequest.Operator.EQ, staffIdNo));
            listFilter.add(new FilterRequest(Staff.COLUMNS.CHANNELTYPEID.name(), FilterRequest.Operator.EQ, 14L));
            listFilter.add(new FilterRequest(Staff.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, 1L));
            List<StaffDTO> lstStaffs = staffService.findByFilter(listFilter);
            if (DataUtil.isNullOrEmpty(lstStaffs)) {
                stockTotalResult.setDescription(getTextParam("stock.ws.notfound.staff.with.idNo", staffIdNo));
                lstError.add(stockTotalResult.getDescription());
                return stockTotalResult;
            }

            if (lstStaffs.size() > 1) {
                stockTotalResult.setDescription(getTextParam("stock.ws.notfound.staff.exist.idNo", staffIdNo));
                lstError.add(stockTotalResult.getDescription());
                return stockTotalResult;
            }

            StaffDTO staffDTO = lstStaffs.get(0);

            //Check validate Danh sach mat hang kem so luong
            boolean haveError = false;
            for (StockTotalFullDTO stockTotalFullDTO : lsStockTotalFullDTO) {
                //Kiem tra ma mat hang
                if (DataUtil.isNullOrEmpty(stockTotalFullDTO.getProdOfferCode())) {
                    lstError.add(getText("g6.quantity.ecom.stock.prod.code.not.null"));
                    haveError = true;
                    continue;
                }

                String formalRegex = getText("FORMAL_REGEX");
                if ((!DataUtil.validateStringByPattern(stockTotalFullDTO.getProdOfferCode(), formalRegex))) {
                    lstError.add(getTextParam("validate.stock.inspect.productOffering.character.special", stockTotalFullDTO.getProdOfferCode()));
                    haveError = true;
                    continue;
                }

                //Kiem tra mat hang
                ProductOffering productOffering = productOfferingRepo.getProductByCode(stockTotalFullDTO.getProdOfferCode());
                if (DataUtil.isNullObject(productOffering)) {
                    lstError.add(getTextParam("validate.stock.inspect.productOffering.notFound3", stockTotalFullDTO.getProdOfferCode()));
                    haveError = true;
                    continue;
                }

                //Kiem tra so luong mat hang
                if (DataUtil.isNullOrZero(stockTotalFullDTO.getCurrentQuantity())) {
                    lstError.add(getTextParam("mn.stock.serial.quantity.limit.zezo2", stockTotalFullDTO.getProdOfferCode()));
                    haveError = true;
                    continue;
                }

                if (stockTotalFullDTO.getCurrentQuantity() < 1L) {
                    lstError.add(getTextParam("mn.stock.serial.quantity.limit.zezo2", stockTotalFullDTO.getProdOfferCode()));
                    haveError = true;
                    continue;
                }

                //Kiem tra mat hang + so luong co dap ung duoc hay khong
                StockTotalDTO stockTotalDTO = stockTotalService.getStockTotalForProcessStock(staffDTO.getStaffId(), Const.OWNER_TYPE.STAFF_LONG, productOffering.getProductOfferingId(), Const.STATE_STATUS.NEW);
                if (DataUtil.isNullObject(stockTotalDTO)) {
                    lstError.add(getTextParam("validate.stock.inspect.productOffering.notFound.stock", stockTotalFullDTO.getProdOfferCode()));
                    haveError = true;
                    continue;
                }

                if (stockTotalDTO.getCurrentQuantity() < stockTotalFullDTO.getCurrentQuantity()) {
                    lstError.add(getTextParam("stockTrans1.validate.quantity.not.enable", stockTotalFullDTO.getProdOfferCode()));
                    haveError = true;
                    continue;
                }
            }

            if (!haveError) {
                stockTotalResult.setErrorCode("OK");
                stockTotalResult.setSuccess(true);
            }

            stockTotalResult.setLstError(lstError);
            return stockTotalResult;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            stockTotalResult.setDescription(ex.getMessage());
            stockTotalResult.setErrorCode(Const.CODE_SERVICE.ERR_EXCEPTION);
            stockTotalResult.setSuccess(false);
            return stockTotalResult;
        }
    }
}
