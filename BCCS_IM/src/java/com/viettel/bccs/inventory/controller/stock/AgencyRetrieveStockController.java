package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.inventory.tag.ListProductNameable;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.web.common.security.CustomAuthenticationProvider;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by hoangnt on 14/1/2016.
 */
@Component
@Scope("view")
@ManagedBean(name = "agencyRetrieveStockController")
public class AgencyRetrieveStockController extends TransCommonController {

    @Autowired
    private ShopInfoNameable shopInfoReceiveTag;
    @Autowired
    private ShopInfoNameable shopInfoExportTag;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private ListProductNameable listProductTag;//khai bao tag danh sach hang hoa
    @Autowired
    private StaffService staffService;
    @Autowired
    private ReasonService reasonService;
    @Autowired
    private ExecuteStockTransService executeStockTransService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private ProductOfferPriceService productOfferPriceService;

    private StockTransInputDTO transInputDTO = new StockTransInputDTO();
    private int limitAutoComplete;
    private StaffDTO staffDTO;
    private Boolean tranfer;
    private List<ReasonDTO> lstReason;
    private RequiredRoleMap requiredRoleMap;
    private Boolean canPrint;
    private Boolean tagProductList;
    private Long stockTransActionId;


    @Secured("@")
    @PostConstruct
    public void init() {
        try {
            if (staffDTO == null) {
                staffDTO = BccsLoginSuccessHandler.getStaffDTO();
            }
            requiredRoleMap = CustomAuthenticationProvider.createRequiredRoleMap(Const.PERMISION.ROLE_SYNC_LOGISTIC, Const.PERMISION.ROLE_TRANSPORT_STOCK,
                    Const.PERMISION.ROLE_XUAT_KHO_BA_MIEN, Const.PERMISION.ROLE_STOCK_NUM_SHOP);
            tranfer = true;

            shopInfoReceiveTag.initAgent(this, DataUtil.safeToString(staffDTO.getShopId()));
            shopInfoExportTag.initShop(this, DataUtil.safeToString(staffDTO.getShopId()), true);
            shopInfoExportTag.loadShop(DataUtil.safeToString(staffDTO.getShopId()), true);
            limitAutoComplete = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_AUTOCOMPLETE));
            transInputDTO.setCreateUser(staffDTO.getStaffCode());
            transInputDTO.setCreateDatetime(new Date());
            transInputDTO.setActionCode(staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PN, Const.STOCK_TRANS_TYPE.AGENT, BccsLoginSuccessHandler.getStaffDTO()));
            lstReason = reasonService.getLsReasonByType(Const.REASON_CODE.STOCK_EXP_RECOVER);
            canPrint = false;
            initTagProduct();
            transInputDTO.setFromOwnerType(DataUtil.safeToLong(Const.OWNER_TYPE.SHOP));
            transInputDTO.setToOwnerId(staffDTO.getShopId());
            transInputDTO.setToOwnerType(DataUtil.safeToLong(Const.OWNER_TYPE.SHOP));
            stockTransActionId = null;
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
            topPage();
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
            topPage();
        }
    }

    @Secured("@")
    public void doCreateAgentRetrieve() {
        try {
            stockTransActionId = null;
            StockTransActionDTO stockTransActionDTO = getStockTransActionDTO();
            StockTransDTO stockTransDTO = getStockTransDTO();
            //validate ngay nhap phai la dinh dang so, va phai la so nguyen duong
            List<StockTransDetailDTO> stockTransDetailDTOs = listProductTag.getListTransDetailDTOs();
            stockTransDTO.setDepositStatus(Const.STOCK_STRANS_DEPOSIT.DEPOSIT_RETRIEVE_EXPENSE);
            stockTransDTO.setStockTransType(Const.STOCK_TRANS_TYPE.AGENT);
            BaseMessageStockTrans message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.NOTE_AGENT_RETRIEVE, Const.STOCK_TRANS_TYPE.AGENT, stockTransDTO, stockTransActionDTO, stockTransDetailDTOs, requiredRoleMap);
            if (!DataUtil.isNullOrEmpty(message.getErrorCode())) {
                throw new LogicException(message.getErrorCode(), message.getKeyMsg(), message.getParamsMsg());
            }
            reportSuccess("", "export.note.deposit.retrieve.create.success");
            stockTransActionId = message.getStockTransActionId();
            canPrint = true;
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    private StockTransActionDTO getStockTransActionDTO() {
        StockTransActionDTO transActionDTO = new StockTransActionDTO();
        transActionDTO.setActionCode(transInputDTO.getActionCode().trim());
        transActionDTO.setStockTransActionId(transInputDTO.getStockTransActionId());
        transActionDTO.setCreateUser(staffDTO.getStaffCode());
        transActionDTO.setActionStaffId(staffDTO.getStaffId());
        transActionDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
        return transActionDTO;
    }


    private StockTransDTO getStockTransDTO() {
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setFromOwnerId(transInputDTO.getFromOwnerId());
        stockTransDTO.setFromOwnerType(transInputDTO.getFromOwnerType());
        stockTransDTO.setToOwnerId(transInputDTO.getToOwnerId());
        stockTransDTO.setToOwnerType(transInputDTO.getToOwnerType());
        stockTransDTO.setStockTransId(transInputDTO.getStockTransId());
        stockTransDTO.setReasonId(transInputDTO.getReasonId());
//        stockTransDTO.setImportNote(transInputDTO.getNote());
        stockTransDTO.setReasonName(transInputDTO.getReasonName());
        stockTransDTO.setNote(transInputDTO.getNote());
        stockTransDTO.setTransport(this.tranfer ? Const.STOCK_TRANS.IS_TRANFER : null);
        return stockTransDTO;
    }

    // Ham in lenh xuat excel
    public StreamedContent exportStockTransDetail() {
        try {
            StockTransDTO stockTransDTO = getStockTransDTOForPint();
            if (stockTransDTO != null && canPrint) {
                List<StockTransDetailDTO> stockTransDetailDTOs = listProductTag.getListTransDetailDTOs();
                stockTransDTO.setActionType(Const.STOCK_TRANS_ACTION_TYPE.NOTE);
                stockTransDTO.setStockTransStatus(Const.STOCK_TRANS_TYPE.IMPORT);
                stockTransDTO.setStockTransActionId(stockTransActionId);
                return exportStockTransDetail(stockTransDTO, stockTransDetailDTOs);
            } else {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stockTrans.dto.print.not.exists");
            }
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happen", ex);
        }
        return null;
    }

    private StockTransDTO getStockTransDTOForPint() {
        StockTransDTO stockTransDTO = getStockTransDTO();
        try {
            stockTransDTO.setActionCode(transInputDTO.getActionCode());
            String fromOwnerName = null;
            String fromOwnerAddress = null;
            String toOwnerName = null;
            String toOwnerAddress = null;
            if (DataUtil.safeEqual(transInputDTO.getFromOwnerType(), Const.OWNER_TYPE.SHOP_LONG)) {
                ShopDTO shopDTO = shopService.findOne(transInputDTO.getFromOwnerId());
                if (shopDTO != null) {
                    fromOwnerName = shopDTO.getName();
                    fromOwnerAddress = shopDTO.getAddress();
                }
            } else if (DataUtil.safeEqual(transInputDTO.getFromOwnerType(), Const.OWNER_TYPE.STAFF_LONG)) {
                StaffDTO staffDTO = staffService.findOne(transInputDTO.getFromOwnerId());
                if (staffDTO != null) {
                    fromOwnerName = staffDTO.getName();
                    fromOwnerAddress = staffDTO.getAddress();
                }
            }

            if (DataUtil.safeEqual(transInputDTO.getToOwnerType(), Const.OWNER_TYPE.SHOP_LONG)) {
                ShopDTO shopDTO = shopService.findOne(transInputDTO.getToOwnerId());
                if (shopDTO != null) {
                    toOwnerName = shopDTO.getName();
                    toOwnerAddress = shopDTO.getAddress();
                }
            } else if (DataUtil.safeEqual(transInputDTO.getToOwnerType(), Const.OWNER_TYPE.STAFF_LONG)) {
                StaffDTO staffDTO = staffService.findOne(transInputDTO.getToOwnerId());
                if (staffDTO != null) {
                    toOwnerName = staffDTO.getName();
                    toOwnerAddress = staffDTO.getAddress();
                }
            }
            stockTransDTO.setFromOwnerName(fromOwnerName);
            stockTransDTO.setFromOwnerAddress(fromOwnerAddress);
            stockTransDTO.setToOwnerName(toOwnerName);
            stockTransDTO.setToOwnerAddress(toOwnerAddress);
            if (!DataUtil.isNullOrZero(transInputDTO.getReasonId())) {
                ReasonDTO reasonDTO = reasonService.findOne(transInputDTO.getReasonId());
                stockTransDTO.setReasonName(reasonDTO.getReasonName());
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return stockTransDTO;
    }

    private void initTagProduct() {
        tagProductList = false;
        ConfigListProductTagDTO config = new ConfigListProductTagDTO(Const.MODE_SERIAL.MODE_NO_SERIAL, staffDTO.getShopId(), Const.OWNER_TYPE.SHOP);
        listProductTag.init(this, config);
        listProductTag.setLsListProductOfferDTO(new ArrayList<ListProductOfferDTO>());
        listProductTag.setLsProductOfferTypeDTOTmp(new ArrayList<ProductOfferTypeDTO>());
        listProductTag.setLsProductOfferingTotalDTOTmp(new ArrayList<ProductOfferingTotalDTO>());
    }

    @Secured("@")
    public void clearCurrentShop(String method) {
        tagProductList = false;
        listProductTag.setLsListProductOfferDTO(new ArrayList<ListProductOfferDTO>());
        listProductTag.setLsProductOfferTypeDTOTmp(new ArrayList<ProductOfferTypeDTO>());
        listProductTag.setLsProductOfferingTotalDTOTmp(new ArrayList<ProductOfferingTotalDTO>());
    }

    @Secured("@")
    public Boolean getShowTransfer() {
        return requiredRoleMap != null && requiredRoleMap.hasRole(Const.PERMISION.ROLE_TRANSPORT_STOCK);
    }

    @Secured("@")
    public void receiveShop(VShopStaffDTO vShopStaffDTO) {
        try {
            transInputDTO.setFromOwnerId(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
            ConfigListProductTagDTO config = new ConfigListProductTagDTO(Const.MODE_SERIAL.MODE_NO_SERIAL, DataUtil.safeToLong(vShopStaffDTO.getOwnerId()), Const.OWNER_TYPE.SHOP);
//            config.setChannelTypeId(DataUtil.safeToLong(vShopStaffDTO.getChannelTypeId()));
//            config.setType(Const.CONFIG_PRODUCT.TYPE_DEPOSIT);
            listProductTag.init(this, config);
            List<String> listValue = Lists.newArrayList();
            listValue.add("1");
            listProductTag.setLsProductStatus(DataUtil.defaultIfNull(optionSetValueService.getStatusOptionSetValueByStockState(Const.OPTION_SET.STOCK_STATE, listValue), new ArrayList<OptionSetValueDTO>()));
            if (DataUtil.isNullOrEmpty(listProductTag.getLsProductOfferTypeDTOTmp())) {
                tagProductList = false;
            } else {
                tagProductList = true;
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    @Secured("@")
    public void doReset() {
        try {
            tagProductList = false;
            canPrint = false;
            shopInfoReceiveTag.initAgent(this, DataUtil.safeToString(staffDTO.getShopId()));
            listProductTag.setLsListProductOfferDTO(new ArrayList<ListProductOfferDTO>());
            listProductTag.setLsProductOfferTypeDTOTmp(new ArrayList<ProductOfferTypeDTO>());
            listProductTag.setLsProductOfferingTotalDTOTmp(new ArrayList<ProductOfferingTotalDTO>());
            transInputDTO.setActionCode(staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PN, Const.STOCK_TRANS_TYPE.AGENT, BccsLoginSuccessHandler.getStaffDTO()));
            transInputDTO.setReasonId(null);
            transInputDTO.setNote(null);
            stockTransActionId = null;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    public StockTransInputDTO getTransInputDTO() {
        return transInputDTO;
    }

    public void setTransInputDTO(StockTransInputDTO transInputDTO) {
        this.transInputDTO = transInputDTO;
    }

    public ShopInfoNameable getShopInfoReceiveTag() {
        return shopInfoReceiveTag;
    }

    public void setShopInfoReceiveTag(ShopInfoNameable shopInfoReceiveTag) {
        this.shopInfoReceiveTag = shopInfoReceiveTag;
    }

    public ShopInfoNameable getShopInfoExportTag() {
        return shopInfoExportTag;
    }

    public void setShopInfoExportTag(ShopInfoNameable shopInfoExportTag) {
        this.shopInfoExportTag = shopInfoExportTag;
    }

    public int getLimitAutoComplete() {
        return limitAutoComplete;
    }

    public void setLimitAutoComplete(int limitAutoComplete) {
        this.limitAutoComplete = limitAutoComplete;
    }

    public StaffDTO getStaffDTO() {
        return staffDTO;
    }

    public void setStaffDTO(StaffDTO staffDTO) {
        this.staffDTO = staffDTO;
    }

    public Boolean getTranfer() {
        return tranfer;
    }

    public void setTranfer(Boolean tranfer) {
        this.tranfer = tranfer;
    }

    public List<ReasonDTO> getLstReason() {
        return lstReason;
    }

    public void setLstReason(List<ReasonDTO> lstReason) {
        this.lstReason = lstReason;
    }

    public ListProductNameable getListProductTag() {
        return listProductTag;
    }

    public void setListProductTag(ListProductNameable listProductTag) {
        this.listProductTag = listProductTag;
    }

    public Boolean getCanPrint() {
        return canPrint;
    }

    public void setCanPrint(Boolean canPrint) {
        this.canPrint = canPrint;
    }

    public Boolean getTagProductList() {
        return tagProductList;
    }

    public void setTagProductList(Boolean tagProductList) {
        this.tagProductList = tagProductList;
    }

    public Long getStockTransActionId() {
        return stockTransActionId;
    }

    public void setStockTransActionId(Long stockTransActionId) {
        this.stockTransActionId = stockTransActionId;
    }
}
