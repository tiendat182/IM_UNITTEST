package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.ExecuteStockTransService;
import com.viettel.bccs.inventory.service.OptionSetValueService;
import com.viettel.bccs.inventory.service.ReasonService;
import com.viettel.bccs.inventory.service.StockTransService;
import com.viettel.bccs.inventory.tag.ListProductNameable;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.bccs.inventory.tag.SignOfficeTagNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.dto.BaseMessage;
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
 * Created by hoangnt14 on 8/23/2016.
 */
@Component
@Scope("view")
@ManagedBean(name = "createNoteChangeProductController")
public class CreateNoteChangeProductController extends TransCommonController {

    private StockTransInputDTO transInputDTO = new StockTransInputDTO();
    private List<ReasonDTO> lstReason;
    private int limitAutoComplete;
    private RequiredRoleMap requiredRoleMap;
    private boolean canPrint;
    private StaffDTO staffDTO;
    private boolean writeOffice = true;
    private Long stockTransActionId;

    @Autowired
    private ShopInfoNameable shopInfoExportTag;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private ListProductNameable listProductTag;//khai bao tag danh sach hang hoa
    @Autowired
    private ReasonService reasonService;
    @Autowired
    private SignOfficeTagNameable writeOfficeTag;//khai bao tag ky vOffice
    @Autowired
    private ExecuteStockTransService executeStockTransService;
    @Autowired
    private StockTransService stockTransService;

    @Secured("@")
    @PostConstruct
    public void init() {
        try {
            if (staffDTO == null) {
                staffDTO = BccsLoginSuccessHandler.getStaffDTO();
            }
            shopInfoExportTag.initShop(this, DataUtil.safeToString(staffDTO.getShopId()), true);
            shopInfoExportTag.loadShop(DataUtil.safeToString(staffDTO.getShopId()), true);
            limitAutoComplete = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_AUTOCOMPLETE));
            lstReason = reasonService.getLsReasonByType(Const.REASON_CODE.CDMH);
            transInputDTO = new StockTransInputDTO();
            transInputDTO.setCreateDatetime(new Date());
            transInputDTO.setActionCode(staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PX, Const.STOCK_TRANS_TYPE.EXPORT, BccsLoginSuccessHandler.getStaffDTO()));
            transInputDTO.setCheckErp(false);
            transInputDTO.setFromOwnerType(DataUtil.safeToLong(Const.OWNER_TYPE.SHOP));
            transInputDTO.setFromOwnerId(staffDTO.getShopId());
            initTagProduct();
            //init cho vung thong tin ky vOffice
            writeOfficeTag.init(this, staffDTO.getShopId());
            requiredRoleMap = CustomAuthenticationProvider.createRequiredRoleMap(Const.PERMISION.ROLE_STOCK_NUM_SHOP, Const.PERMISION.ROLE_DONGBO_ERP);
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

    private void initTagProduct() {
        try {
            ConfigListProductTagDTO config = new ConfigListProductTagDTO(Const.MODE_SERIAL.MODE_ADD_ALL, DataUtil.safeToLong(staffDTO.getShopId()), Const.OWNER_TYPE.SHOP);
            config.setChangeProduct(true);
            config.setShowTotalPrice(false);
            listProductTag.init(this, config);
            List<String> listValue = Lists.newArrayList();
            listValue.add(DataUtil.safeToString(Const.STATE_STATUS.NEW));
            listValue.add(DataUtil.safeToString(Const.STATE_STATUS.DAMAGE));
            listValue.add(DataUtil.safeToString(Const.STATE_STATUS.RETRIEVE));
            listProductTag.setLsProductStatus(DataUtil.defaultIfNull(optionSetValueService.getStatusOptionSetValueByStockState(Const.OPTION_SET.STOCK_STATE, listValue), new ArrayList<OptionSetValueDTO>()));
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
    public void doValidateCreateNote() {
        List<StockTransDetailDTO> lsDetailDTOs = listProductTag.getListTransDetailDTOs();
        for (StockTransDetailDTO stockTransDetailDTO : lsDetailDTOs) {
            if (DataUtil.safeEqual(stockTransDetailDTO.getCheckSerial(), Const.PRODUCT_OFFERING._CHECK_SERIAL)
                    && DataUtil.isNullOrEmpty(stockTransDetailDTO.getLstSerial())) {
                reportError("", ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.confirm.serial.empty", stockTransDetailDTO.getProdOfferName());
                break;
            }
        }
    }

    @Secured("@")
    public void doCreateNote() {
        try {
            StockTransDTO stockTransDTO = getStockTransExport();
            StockTransActionDTO stockTransActionDTO = getStockTransActionExport();
            if (writeOffice) {
                SignOfficeDTO signOfficeDTO = writeOfficeTag.validateVofficeAccount();
                stockTransDTO.setUserName(signOfficeDTO.getUserName());
                stockTransDTO.setPassWord(signOfficeDTO.getPassWord());
                stockTransDTO.setSignFlowId(signOfficeDTO.getSignFlowId());
                stockTransDTO.setSignVoffice(writeOffice);
            }
            List<StockTransDetailDTO> lsDetailDTOs = listProductTag.getListTransDetailDTOs();
            BaseMessageStockTrans message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.NOTE_CHANGE_PRODUCT, Const.STOCK_TRANS_TYPE.EXPORT, stockTransDTO, stockTransActionDTO, lsDetailDTOs, requiredRoleMap);
            if (!DataUtil.isNullOrEmpty(message.getErrorCode()) || !DataUtil.isNullOrEmpty(message.getKeyMsg())) {
                throw new LogicException(message.getErrorCode(), message.getKeyMsg(), message.getParamsMsg());
            }
            stockTransActionId = message.getStockTransActionId();
            canPrint = true;
            reportSuccess("", "mn.stock.change.product.offering.success");
            topPage();
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

    public StockTransDTO getStockTransDTOForPint() {
        StockTransDTO stockTransDTO = getStockTransExport();
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
            }
            if (DataUtil.safeEqual(stockTransDTO.getToOwnerType(), Const.OWNER_TYPE.SHOP_LONG)) {
                ShopDTO shopDTO = shopService.findOne(stockTransDTO.getToOwnerId());
                if (shopDTO != null) {
                    toOwnerName = shopDTO.getName();
                    toOwnerAddress = shopDTO.getAddress();
                }
            } else if (DataUtil.safeEqual(stockTransDTO.getToOwnerType(), Const.OWNER_TYPE.PARTNER_LONG)) {
                PartnerDTO partnerDTO = partnerService.findOne(stockTransDTO.getToOwnerId());
                if (partnerDTO != null) {
                    toOwnerName = partnerDTO.getPartnerName();
                    toOwnerAddress = partnerDTO.getAddress();
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

    @Secured("@")
    public void doReset() {
        try {
            writeOffice = true;
            canPrint = false;
            transInputDTO.setActionCode(staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PX, Const.STOCK_TRANS_TYPE.EXPORT, BccsLoginSuccessHandler.getStaffDTO()));
            transInputDTO.setReasonId(null);
            transInputDTO.setNote(null);
            initTagProduct();
            writeOfficeTag.init(this, staffDTO.getShopId());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    @Secured("@")
    public void receiveWriteOffice() {
        //
    }

    private StockTransDTO getStockTransExport() {
        StockTransDTO stockTransExport = new StockTransDTO();
        stockTransExport.setFromOwnerType(transInputDTO.getFromOwnerType());
        stockTransExport.setFromOwnerId(transInputDTO.getFromOwnerId());
        if (DataUtil.safeEqual(transInputDTO.getFromOwnerId(), DataUtil.safeToLong(Const.VT_SHOP_ID))) {
            stockTransExport.setToOwnerType(Const.OWNER_TYPE.PARTNER_LONG);
            stockTransExport.setToOwnerId(Const.TD_PARTNER_ID);
        } else {
            stockTransExport.setToOwnerType(Const.OWNER_TYPE.SHOP_LONG);
            stockTransExport.setToOwnerId(DataUtil.safeToLong(Const.VT_SHOP_ID));
        }
        stockTransExport.setStockTransType(Const.STOCK_TRANS_TYPE.EXPORT);
        stockTransExport.setStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
        stockTransExport.setReasonId(transInputDTO.getReasonId());
        stockTransExport.setNote(transInputDTO.getNote());
        stockTransExport.setCheckErp(transInputDTO.getCheckErp() ? Const.STOCK_TRANS.IS_NOT_ERP : null);
        return stockTransExport;
    }

    public StockTransActionDTO getStockTransActionExport() {
        StockTransActionDTO transActionDTO = new StockTransActionDTO();
        transActionDTO.setActionCode(transInputDTO.getActionCode());
        transActionDTO.setCreateUser(staffDTO.getStaffCode());
        transActionDTO.setActionStaffId(staffDTO.getStaffId());
        transActionDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
        return transActionDTO;
    }

    @Secured("@")
    public boolean isShowCheckErp() {
         return requiredRoleMap != null && requiredRoleMap.hasRole(Const.PERMISION.ROLE_DONGBO_ERP);
    }

    public StreamedContent exportStockTransDetail() {
        try {
            StockTransDTO stockTransDTO = getStockTransDTOForPint();
            if (stockTransDTO != null && canPrint) {
                stockTransDTO.setActionType(Const.STOCK_TRANS_ACTION_TYPE.NOTE);
                stockTransDTO.setStockTransStatus(Const.STOCK_TRANS_TYPE.EXPORT);
                List<Long> lstActionID = Lists.newArrayList();
                lstActionID.add(stockTransActionId);
                List<StockTransFullDTO> lsStockTransFull = DataUtil.defaultIfNull(stockTransService.searchStockTransDetail(lstActionID), new ArrayList<StockTransFullDTO>());
                stockTransDTO.setStockTransActionId(stockTransActionId);
                return exportStockTransDetail(stockTransDTO, lsStockTransFull);
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

    public StockTransInputDTO getTransInputDTO() {
        return transInputDTO;
    }

    public void setTransInputDTO(StockTransInputDTO transInputDTO) {
        this.transInputDTO = transInputDTO;
    }

    public List<ReasonDTO> getLstReason() {
        return lstReason;
    }

    public void setLstReason(List<ReasonDTO> lstReason) {
        this.lstReason = lstReason;
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


    public ListProductNameable getListProductTag() {
        return listProductTag;
    }

    public void setListProductTag(ListProductNameable listProductTag) {
        this.listProductTag = listProductTag;
    }

    public boolean isCanPrint() {
        return canPrint;
    }

    public void setCanPrint(boolean canPrint) {
        this.canPrint = canPrint;
    }

    public boolean isWriteOffice() {
        return writeOffice;
    }

    public void setWriteOffice(boolean writeOffice) {
        this.writeOffice = writeOffice;
    }

    public SignOfficeTagNameable getWriteOfficeTag() {
        return writeOfficeTag;
    }

    public void setWriteOfficeTag(SignOfficeTagNameable writeOfficeTag) {
        this.writeOfficeTag = writeOfficeTag;
    }
}
