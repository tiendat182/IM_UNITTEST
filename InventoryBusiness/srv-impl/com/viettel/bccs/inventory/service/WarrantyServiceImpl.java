package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.message.BaseWarrantyMessage;
import com.viettel.bccs.inventory.repo.ProductOfferTypeRepo;
import com.viettel.bccs.inventory.repo.StockTotalRepo;
import com.viettel.bccs.inventory.service.common.IMServiceUtil;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@Service
public class WarrantyServiceImpl extends BaseServiceImpl implements WarrantyService {

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    public static final Logger logger = Logger.getLogger(WarrantyServiceImpl.class);

    @Autowired
    private StockHandsetService stockHandsetService;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private ProductOfferTypeRepo productOfferTypeRepo;
    @Autowired
    private StockTotalRepo stockTotalRepo;
    @Autowired
    private WarrantyServiceExecute warrantyServiceExecute;

    @Override
    public BaseWarrantyMessage findStockHandSet(Long stockHandsetId, Long prodOfferId, String serial, Long ownerId, Long ownerType, Long stateId, Long status) {

        BaseWarrantyMessage baseMessage = new BaseWarrantyMessage();
        try {
            if (status != null && (status.compareTo(0L) != 0 && status.compareTo(1L) != 0)) {
                throw new LogicException("1", "warranty.serive.handset.status.invalid");
            }
            if (prodOfferId != null) {
                ProductOfferingDTO offeringDTO = productOfferingService.findOne(prodOfferId);
                if (offeringDTO == null || !Const.STATUS_ACTIVE.equals(offeringDTO.getStatus())) {
                    throw new LogicException("3", "validate.stock.inspect.productOffering.notFound2", prodOfferId);
                }
            }
            if (ownerType != null && ownerId != null) {
                if (ownerType.compareTo(1L) == 0) { // kho don vi
                    ShopDTO shopDTO = shopService.findOne(ownerId);
                    if (shopDTO == null || !Const.STATUS_ACTIVE.equals(shopDTO.getStatus())) {
                        throw new LogicException("4", "warranty.serive.handset.ownerid.invalid", ownerId);
                    }
                } else if (ownerType.compareTo(2L) == 0) { // kho NV
                    StaffDTO staffDTO = staffService.findOne(ownerId);
                    if (staffDTO == null || !Const.STATUS_ACTIVE.equals(staffDTO.getStatus())) {
                        throw new LogicException("5", "warranty.serive.handset.ownerid.invalid", ownerId);
                    }
                }
            }
            List<StockHandsetDTO> lsResult = stockHandsetService.findStockHandSet(stockHandsetId, prodOfferId, serial, ownerId, ownerType, stateId, status);
            if (DataUtil.isNullOrEmpty(lsResult)) {
                baseMessage.setErrorCode("6");
                baseMessage.setDescription(getText("warranty.serive.handset.result.epmty"));
            } else {
                baseMessage.setSuccess(true);
                baseMessage.setErrorCode("0");
                baseMessage.setDescription(getText("warranty.serive.handset.result.sucess"));
                baseMessage.setHandsetDTOList(lsResult);
            }
        } catch (LogicException le) {
            baseMessage.setErrorCode(le.getErrorCode());
            baseMessage.setDescription(le.getDescription());
            logger.error(le.getMessage(), le);
        } catch (Exception ex) {
            baseMessage.setErrorCode(ErrorCode.ERROR_NOT_DEFINE);
            baseMessage.setDescription(ex.getMessage());
            logger.error(ex.getMessage(), ex);
        }
        return baseMessage;
    }

    @Override
    public Date findExportDateBySerial(String prodOfferCode, String serial) {
        try {
            return stockHandsetService.findExportDateBySerial(prodOfferCode, serial);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return null;
    }

    @Override
    public BaseWarrantyMessage viewStockDetail(Long ownerId, Long ownerType, Long prodOfferId, Long stateId) {
        BaseWarrantyMessage baseMessage = new BaseWarrantyMessage();
        try {
            if (ownerId == null || DataUtil.isNullOrZero(DataUtil.safeToLong(ownerId))) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "warranty.stock.ownerid.invalid.empty.format");
            }
            if (ownerType == null || DataUtil.isNullOrZero(DataUtil.safeToLong(ownerType))) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "warranty.stock.ownerType.invalid.empty.format");
            } else if (Const.OWNER_TYPE.SHOP_LONG.equals(ownerType)) {
                ShopDTO shopDTO = shopService.findOne(ownerId);
                if (shopDTO == null || !Const.STATUS_ACTIVE.equals(shopDTO.getStatus())) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "warranty.serive.handset.ownerid.invalid", ownerId);
                }
            } else if (Const.OWNER_TYPE.STAFF_LONG.equals(ownerType)) {
                StaffDTO staffDTO = staffService.findOne(ownerId);
                if (staffDTO == null || !Const.STATUS_ACTIVE.equals(staffDTO.getStatus())) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "warranty.serive.handset.ownerid.invalid", ownerId);
                }
            }
            if (prodOfferId != null) {
                ProductOfferingDTO offeringDTO = productOfferingService.findOne(prodOfferId);
                if (offeringDTO == null) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "input.list.product.file.prod.not.valid");
                }
            }
            List<ProductOfferTypeDTO> lsProOfferType = productOfferTypeRepo.getListProdOffeTypeForWarranty(ownerId, ownerType, prodOfferId);
            if (DataUtil.isNullOrEmpty(lsProOfferType)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "warranty.stock.ownerType.empty.prodOfferType");
            }
            if (prodOfferId != null) {
                List<StockTotalFullDTO> lsStockTotal = stockTotalRepo.getListStockStockTotalFullForWarranty(ownerId, ownerType, null, prodOfferId, stateId);
                for (ProductOfferTypeDTO productOfferTypeDTO : lsProOfferType) {
                    productOfferTypeDTO.setLsStockTotalFullDto(lsStockTotal);
                }
            } else {
                for (ProductOfferTypeDTO productOfferTypeDTO : lsProOfferType) {
                    List<StockTotalFullDTO> lsStockTotal = stockTotalRepo.getListStockStockTotalFullForWarranty(ownerId, ownerType, productOfferTypeDTO.getProductOfferTypeId(), null, stateId);
                    productOfferTypeDTO.setLsStockTotalFullDto(lsStockTotal);
                }
            }
            baseMessage.setSuccess(true);
            baseMessage.setErrorCode("0");
            baseMessage.setDescription(getText("warranty.serive.handset.result.sucess"));
            baseMessage.setLsProductTypeDTO(lsProOfferType);

        } catch (LogicException le) {
            baseMessage.setErrorCode(le.getErrorCode());
            baseMessage.setDescription(le.getDescription());
            logger.error(le.getMessage(), le);
        } catch (Exception ex) {
            baseMessage.setErrorCode(ErrorCode.ERROR_NOT_DEFINE);
            baseMessage.setDescription(ex.getMessage());
            logger.error(ex.getMessage(), ex);
        }
        return baseMessage;
    }

    @Override
    public BaseWarrantyMessage exportStock(List<ImpExpStockDTO> listSerial, Long fromOwnerId, Long fromOwnerType, Long toOwnerId, Long toOwnerType) {
        return exportImpoStockByTypeAction(listSerial, fromOwnerId, fromOwnerType, toOwnerId, toOwnerType, null, Const.WARRANTY_TYPE_ACTION.EXPORT, null, "exportStock");
    }

    @Override
    public BaseWarrantyMessage impportStock(List<ImpExpStockDTO> listSerial, Long fromOwnerId, Long fromOwnerType, Long toOwnerId, Long toOwnerType) {
        return exportImpoStockByTypeAction(listSerial, fromOwnerId, fromOwnerType, toOwnerId, toOwnerType, null, Const.WARRANTY_TYPE_ACTION.IMPORT, null, "impportStock");
    }

    @Override
    public BaseWarrantyMessage exportImportStock(List<ImpExpStockDTO> listSerial, Long fromOwnerId, Long fromOwnerType, Long toOwnerId, Long toOwnerType, String staffCode) {
        return exportImpoStockByTypeAction(listSerial, fromOwnerId, fromOwnerType, toOwnerId, toOwnerType, null, Const.WARRANTY_TYPE_ACTION.EXP_IMP_FOR_STAFF, staffCode, "exportImportStock");
    }

    @Override
    public BaseWarrantyMessage exportStockWarranty(List<ImpExpStockDTO> listSerial, Long newStateId, Long fromOwnerId, Long fromOwnerType, Long toOwnerId, Long toOwnerType) {
        return exportImpoStockByTypeAction(listSerial, fromOwnerId, fromOwnerType, toOwnerId, toOwnerType, newStateId, Const.WARRANTY_TYPE_ACTION.EXPORT_CHANGE_STATEID, null, "exportStockWarranty");
    }

    @Override
    public BaseWarrantyMessage importStockGPON(List<ImpExpStockDTO> listSerial, Long fromOwnerId, Long fromOwnerType, Long toOwnerId, Long toOwnerType) {
        return exportImpoStockByTypeAction(listSerial, fromOwnerId, fromOwnerType, toOwnerId, toOwnerType, null, Const.WARRANTY_TYPE_ACTION.IMPORT_GPON, null, "importStockGPON");
    }


    /**
     * ham xu ly xuat kho chung
     *
     * @param listSerial
     * @param newStateId
     * @param fromOwnerId
     * @param fromOwnerType
     * @param toOwnerId
     * @param toOwnerType
     * @param typeAction
     * @return
     */
    private BaseWarrantyMessage exportImpoStockByTypeAction(List<ImpExpStockDTO> listSerial, Long fromOwnerId,
                                                            Long fromOwnerType, Long toOwnerId, Long toOwnerType, Long newStateId, String typeAction, String staffCode, String methodName) {
        BaseWarrantyMessage baseMessage = new BaseWarrantyMessage();
        Date startDate = null;
        Long stockTransId = 0L;
        String paramsInput = IMServiceUtil.connectData(" fromOwnerId=" + fromOwnerId, " fromOwnerType=" + fromOwnerType, " toOwnerId=" + toOwnerId, " toOwnerType=" + toOwnerType, " staffCode=" + staffCode, "listSerial=", listSerial);
        String resultCall = "";
        String responseCode = "";
        String description = "";
        try {
            startDate = getSysDate(em);
            stockTransId = warrantyServiceExecute.importExportStock(listSerial, fromOwnerId, fromOwnerType, toOwnerId, toOwnerType, newStateId, typeAction, staffCode);
            baseMessage.setSuccess(true);
            baseMessage.setErrorCode("0");
            baseMessage.setDescription(getText("warranty.serive.handset.result.sucess"));
            baseMessage.setStockTransId(stockTransId);

            resultCall = IMServiceUtil.connectData(" responseCode=" + baseMessage.getErrorCode(), "description=" + baseMessage.getDescription(), "stockTransId=" + DataUtil.safeToLong(stockTransId));
            responseCode = baseMessage.getErrorCode();
            description = getText("warranty.serive.handset.result.sucess");

        } catch (LogicException le) {
            baseMessage.setErrorCode(le.getErrorCode());
            baseMessage.setDescription(le.getDescription());
            logger.error(le.getMessage(), le);

            resultCall = IMServiceUtil.connectData(" responseCode=" + le.getErrorCode(), "description=" + le.getDescription(), "stockTransId=" + DataUtil.safeToLong(stockTransId));
            responseCode = le.getErrorCode();
            description = le.getDescription();

        } catch (Exception ex) {
            baseMessage.setErrorCode("12");
            baseMessage.setDescription(ex.getMessage());
            logger.error(ex.getMessage(), ex);
        }
        //ghi log du thanh cong hay that bai
        try {
            warrantyServiceExecute.saveLog(resultCall, responseCode, description, methodName, paramsInput, startDate, stockTransId);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return baseMessage;
    }

    @Override
    public BaseWarrantyMessage getWarrantyStockLog(Long logId, String methodName, String paramsInput, String responseCode, String description, Date fromDate, Date endDate) {
        BaseWarrantyMessage baseMessage = new BaseWarrantyMessage();
        try {
            if (logId == null
                    && fromDate == null
                    && endDate == null
                    && DataUtil.isNullOrEmpty(methodName)
                    && DataUtil.isNullOrEmpty(paramsInput)
                    && DataUtil.isNullOrEmpty(responseCode)
                    && DataUtil.isNullOrEmpty(description)) {
                throw new LogicException("1", "warranty.search.log.empty");
            }
            if ((fromDate == null && endDate != null) || (fromDate != null && endDate == null)) {
                throw new LogicException("2", "warranty.search.startDate.endDate.empty");
            }
            if (fromDate != null) {
                String fDate = DateUtil.date2ddMMyyNoSlashString(fromDate);
                String eDate = DateUtil.date2ddMMyyNoSlashString(endDate);
                if (fDate.compareTo(eDate) > 0) {
                    throw new LogicException("3", "validate.start.end.date");
                }
            }
            List<WarrantyStockLog> lsStockLog = warrantyServiceExecute.getWarrantyStockLog(logId, methodName, paramsInput, responseCode, description, fromDate, endDate);

            if (DataUtil.isNullOrEmpty(lsStockLog)) {
                baseMessage.setSuccess(true);
                baseMessage.setErrorCode("6");
                baseMessage.setDescription(getText("mn.invoice.invoiceType.nodata"));
                return baseMessage;
            }

            baseMessage.setSuccess(true);
            baseMessage.setErrorCode("0");
            baseMessage.setDescription(getText("warranty.serive.handset.result.sucess"));
            baseMessage.setListWarrantyStockLog(lsStockLog);

        } catch (LogicException le) {
            baseMessage.setErrorCode(le.getErrorCode());
            baseMessage.setDescription(le.getDescription());
            logger.error(le.getMessage(), le);
        } catch (Exception ex) {
            baseMessage.setErrorCode(ErrorCode.ERROR_NOT_DEFINE);
            baseMessage.setDescription(ex.getMessage());
            logger.error(ex.getMessage(), ex);
        }
        return baseMessage;
    }

    public BaseWarrantyMessage viewStockKV(String shopCode, Long prodOfferId, Long stateId) {

        BaseWarrantyMessage baseMessage = new BaseWarrantyMessage();

        try {
            if (DataUtil.isNullOrEmpty(shopCode)) {
                throw new LogicException("3", "process.stock.shop.mgs.require");
            }
            ShopDTO shopDTO = shopService.getShopByCodeAndActiveStatus(shopCode);
            if (shopDTO == null) {
                throw new LogicException("3", "mn.divide.shop.code.invalid", shopCode);
            }
            List<StockManagementForWarranty> lstStockManagementForWarranty = stockTotalRepo.viewStockKV(shopDTO.getShopId(), prodOfferId, stateId);
            baseMessage.setSuccess(true);
            baseMessage.setErrorCode("0");
            baseMessage.setDescription(getText("warranty.serive.handset.result.sucess"));
            baseMessage.setLstStockModelWarranty(lstStockManagementForWarranty);
        } catch (LogicException le) {
            baseMessage.setErrorCode(le.getErrorCode());
            baseMessage.setDescription(le.getDescription());
            logger.error(le.getMessage(), le);
        } catch (Exception ex) {
            baseMessage.setErrorCode("12");
            baseMessage.setDescription(ex.getMessage());
            logger.error(ex.getMessage(), ex);
        }
        return baseMessage;

    }

    @Override
    public BaseWarrantyMessage viewStockModelWarranty(Long ownerType, Long ownerId, String stockModelCode, String stockModelName, Long stateId) {
        BaseWarrantyMessage baseMessage = new BaseWarrantyMessage();
        try {
            if (ownerId == null) {
                throw new LogicException("1", "warranty.search.view.warranty.ownerId.empty");
            }

            if (stateId == null) {
                throw new LogicException("6", "warranty.search.view.warranty.stateId.empty");
            }
            if (Const.OWNER_TYPE.SHOP_LONG.equals(ownerType)) {//Neu la cua hang/ don vi
                ShopDTO shopDTO = shopService.findOne(ownerId);
                if (shopDTO == null) {
                    throw new LogicException("2", "warranty.search.view.warranty.stock.empty");
                }
            }
            if (Const.OWNER_TYPE.STAFF_LONG.equals(ownerType)) {//Neu la nhan vien
                StaffDTO staffDTO = staffService.findOne(ownerId);
                if (staffDTO == null) {
                    throw new LogicException("2", "warranty.search.view.warranty.stock.empty");
                }
            }
            //check StockModelId
            if (DataUtil.isNullOrEmpty(stockModelCode) && DataUtil.isNullOrEmpty(stockModelName)) {
                throw new LogicException("4", "warranty.search.view.warranty.stockModelCode.stockModelName.empty");
            }
            List<StockManagementForWarranty> lsWarranty = warrantyServiceExecute.viewStockModelWarranty(ownerType, ownerId, safeToStringValue(stockModelCode),
                    safeToStringValue(stockModelName), stateId);

            if (DataUtil.isNullOrEmpty(lsWarranty)) {
                baseMessage.setSuccess(true);
                baseMessage.setErrorCode("6");
                baseMessage.setDescription(getText("mn.invoice.invoiceType.nodata"));
                return baseMessage;
            }

            baseMessage.setSuccess(true);
            baseMessage.setErrorCode("0");
            baseMessage.setDescription(getText("warranty.serive.handset.result.sucess"));
            baseMessage.setLstStockModelWarranty(lsWarranty);

        } catch (LogicException le) {
            baseMessage.setErrorCode(le.getErrorCode());
            baseMessage.setDescription(le.getDescription());
            logger.error(le.getMessage(), le);
        } catch (Exception ex) {
            baseMessage.setErrorCode(ErrorCode.ERROR_NOT_DEFINE);
            baseMessage.setDescription(ex.getMessage());
            logger.error(ex.getMessage(), ex);
        }
        return baseMessage;
    }

    private String safeToStringValue(String param) {
        String result = DataUtil.safeToString(param).trim();
        return "?".equals(result) ? "" : result;
    }

    @Override
    public BaseWarrantyMessage getInventoryInfoByListProdCode(List<String> listProdOfferCode) {
        BaseWarrantyMessage baseMessage = new BaseWarrantyMessage();
        try {
            if (DataUtil.isNullOrEmpty(listProdOfferCode)) {
                throw new LogicException("1", "stock.validate.warranty.list.code.empty");
            }

            List<Long> listProdOfferId = Lists.newArrayList();

            List<ProductOfferingDTO> lstProductResult = Lists.newArrayList();


            for (String prodOfferCode : listProdOfferCode) {

                ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();

                if (DataUtil.isNullOrEmpty(prodOfferCode)) {
                    productOfferingDTO.setDescription(getTextParam("g6.quantity.ecom.stock.prod.code.not.null", prodOfferCode));
                    lstProductResult.add(productOfferingDTO);
                    continue;
                }

                productOfferingDTO.setCode(prodOfferCode.toUpperCase());

                if (!DataUtil.validateStringByPattern(prodOfferCode, Const.REGEX.CODE_REGEX)) {
                    productOfferingDTO.setDescription(getTextParam("stock.validate.warranty.code.is.special", prodOfferCode));
                    lstProductResult.add(productOfferingDTO);
                    continue;
                }

                ProductOfferingDTO product = productOfferingService.findByProductOfferCode(prodOfferCode, Const.STATUS.ACTIVE);

                if (DataUtil.isNullObject(product)) {
                    productOfferingDTO.setDescription(getTextParam("stock.validate.warranty.code.not.exist", prodOfferCode));
                    lstProductResult.add(productOfferingDTO);
                    continue;
                }

                productOfferingDTO.setName(product.getName());
                productOfferingDTO.setProductOfferingId(product.getProductOfferingId());
                productOfferingDTO.setDescription(getText("stock.validate.warranty.no.good"));
                lstProductResult.add(productOfferingDTO);

                listProdOfferId.add(product.getProductOfferingId());

            }

            List<ProductOfferingDTO> lstProduct = productOfferingService.getInventoryInfoWarranty(listProdOfferId);

            if (!DataUtil.isNullOrEmpty(lstProduct) && !DataUtil.isNullOrEmpty(lstProductResult)) {
                for (ProductOfferingDTO prod : lstProduct) {
                    for (ProductOfferingDTO prodResult : lstProductResult) {
                        if (DataUtil.safeEqual(prod.getProductOfferingId(), prodResult.getProductOfferingId())) {
                            prodResult.setQuantity(prod.getQuantity());
                            prodResult.setOwnerCode(prod.getOwnerCode());
                            prodResult.setOwnerType(prod.getOwnerType());
                            prodResult.setDescription(getText("stock.validate.warranty.have.good"));
                            break;
                        }
                    }
                }
            }

            baseMessage.setSuccess(true);
            baseMessage.setDescription(getText("warranty.serive.handset.result.sucess"));
            baseMessage.setLsProductOfferingDTO(lstProductResult);

        } catch (LogicException le) {
            baseMessage.setErrorCode(le.getErrorCode());
            baseMessage.setDescription(le.getDescription());
            logger.error(le.getMessage(), le);
        } catch (Exception ex) {
            baseMessage.setErrorCode(ErrorCode.ERROR_NOT_DEFINE);
            baseMessage.setDescription(ex.getMessage());
            logger.error(ex.getMessage(), ex);
        }
        return baseMessage;
    }

}
