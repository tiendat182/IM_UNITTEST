package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.ExecuteStockTransService;
import com.viettel.bccs.inventory.service.StaffService;
import com.viettel.bccs.inventory.tag.ListProductNameable;
import com.viettel.bccs.inventory.tag.OrderStockTagNameable;
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
import java.util.List;

/**
 * Created by hoangnt14 on 1/21/2016.
 */
@Component
@Scope("view")
@ManagedBean(name = "retrieveStockFromCollaboratorController")
public class RetrieveStockFromCollaboratorController extends TransCommonController {
    @Autowired
    private OrderStockTagNameable orderStockTag;//khai bao tag thong tin lenh xuat
    @Autowired
    private ListProductNameable listProductTag;//khai bao tag danh sach hang hoa
    @Autowired
    private StaffService staffService;
    @Autowired
    private ExecuteStockTransService executeStockTransService;
    private StaffDTO staffDTO;
    private Boolean canPrint;
    private Boolean tagProductList;
    private List<OptionSetValueDTO> listPayMethod;
    private RequiredRoleMap requiredRoleMap;
    private Long stockTransActionId;

    @PostConstruct
    public void init() {
        try {
            if (staffDTO == null) {
                staffDTO = BccsLoginSuccessHandler.getStaffDTO();
            }
            initTagProduct();
            doReset();
        } catch (Exception e) {
            logger.error(e);
            reportError("", "common.error.happened", e);
        }
    }

    @Secured("@")
    public void doReset() {
        try {
            stockTransActionId = null;
            requiredRoleMap = CustomAuthenticationProvider.createRequiredRoleMap(Const.PERMISION.ROLE_STOCK_NUM_SHOP);
            canPrint = false;
            listPayMethod = optionSetValueService.getByOptionSetCode(Const.STOCK_STRANS_DEPOSIT.DEPOSIT_PAY_METHOD);
            orderStockTag.initCollaboratorRetrieve(this);
            orderStockTag.getTransInputDTO().setActionCode(staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PN, Const.STOCK_TRANS_TYPE.AGENT, BccsLoginSuccessHandler.getStaffDTO()));
            orderStockTag.setLsPayMethod(listPayMethod);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        }
    }

    private void initTagProduct() {
        tagProductList = false;
        ConfigListProductTagDTO config = new ConfigListProductTagDTO(Const.MODE_SERIAL.MODE_ADD_SERIAL, staffDTO.getShopId(), Const.OWNER_TYPE.SHOP);
        listProductTag.init(this, config);
        listProductTag.setLsListProductOfferDTO(new ArrayList<ListProductOfferDTO>());
        listProductTag.setLsProductOfferTypeDTOTmp(new ArrayList<ProductOfferTypeDTO>());
        listProductTag.setLsProductOfferingTotalDTOTmp(new ArrayList<ProductOfferingTotalDTO>());
    }

    @Secured("@")
    public void doReceiveShop(VShopStaffDTO vShopStaffDTO) {
        try {
            orderStockTag.getTransInputDTO().setFromOwnerId(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
            orderStockTag.getTransInputDTO().setFromOwnerType(Const.OWNER_TYPE.STAFF_LONG);
            ConfigListProductTagDTO configListProductTagDTO = new ConfigListProductTagDTO(Const.MODE_SERIAL.MODE_ADD_ALL, DataUtil.safeToLong(vShopStaffDTO.getOwnerId()), Const.OWNER_TYPE.STAFF);
            configListProductTagDTO.setCollaborator(true);
//            configListProductTagDTO.setChannelTypeId(DataUtil.safeToLong(vShopStaffDTO.getChannelTypeId()));
//            configListProductTagDTO.setType(Const.CONFIG_PRODUCT.TYPE_DEPOSIT);
//            Long branchId = shopService.getBranchId(staffDTO.getShopId());
//            configListProductTagDTO.setBranchId(branchId);
            listProductTag.init(this, configListProductTagDTO);
            List<String> listValue = Lists.newArrayList("1", "3", "4");
            listProductTag.setLsProductStatus(DataUtil.defaultIfNull(optionSetValueService.getStatusOptionSetValueByStockState(Const.OPTION_SET.STOCK_STATE, listValue), new ArrayList<OptionSetValueDTO>()));
            if (DataUtil.isNullOrEmpty(listProductTag.getLsProductOfferTypeDTOTmp())) {
                tagProductList = false;
//                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stockTrans.validate.collaborator.stock.empty", vShopStaffDTO.getOwnerCode());
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
    public void clearCurrentShop() {
        tagProductList = false;
        listProductTag.setLsListProductOfferDTO(new ArrayList<ListProductOfferDTO>());
        listProductTag.setLsProductOfferTypeDTOTmp(new ArrayList<ProductOfferTypeDTO>());
        listProductTag.setLsProductOfferingTotalDTOTmp(new ArrayList<ProductOfferingTotalDTO>());
    }

    @Secured("@")
    public void doRetrieveStockFromCollaborator() {
        try {
            stockTransActionId = null;
            //validate ngay nhap phai la dinh dang so, va phai la so nguyen duong
            StockTransActionDTO stockTransActionDTO = orderStockTag.getStockTransActionDTO();
            stockTransActionDTO.setActionStaffId(staffDTO.getStaffId());
            StockTransDTO stockTransDTO = orderStockTag.getStockTransDTO();
            stockTransDTO.setIsAutoGen(null);
            stockTransDTO.setShopId(staffDTO.getShopId());
            stockTransDTO.setUserCreate(staffDTO.getStaffCode());
            stockTransDTO.setToOwnerId(staffDTO.getStaffId());
            stockTransDTO.setToOwnerType(Const.OWNER_TYPE.STAFF_LONG);
            List<StockTransDetailDTO> stockTransDetailDTOs = listProductTag.getListTransDetailDTOs();
            stockTransDTO.setCreateUserIpAdress(BccsLoginSuccessHandler.getIpAddress());
            BaseMessageStockTrans message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.COOLLABORATOR_RETRIEVE, Const.STOCK_TRANS_TYPE.IMPORT, stockTransDTO, stockTransActionDTO, stockTransDetailDTOs, requiredRoleMap);
            if (!DataUtil.isNullOrEmpty(message.getErrorCode())) {
                throw new LogicException(message.getErrorCode(), message.getKeyMsg(), message.getParamsMsg());
            }
            reportSuccess("", "export.order.create.retrieve.success");
            canPrint = true;
            stockTransActionId = message.getStockTransActionId();
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
    public void doValidateStockFromCollaborator() {
        try {
            List<ListProductOfferDTO> lstProductOfferDTO = listProductTag.getLsListProductOfferDTO();
            for (ListProductOfferDTO listProductOfferDTO : lstProductOfferDTO) {
                if (!DataUtil.isNullObject(listProductOfferDTO.getProductOfferingTotalDTO())
                        && DataUtil.safeEqual(listProductOfferDTO.getProductOfferingTotalDTO().getCheckSerial(), Const.PRODUCT_OFFERING._CHECK_SERIAL)
                        && !listProductOfferDTO.getHaveListSerial()) {
                    reportError("", ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.confirm.serial.empty", listProductOfferDTO.getProductOfferingTotalDTO().getName());
                    break;
                }
            }
            if (!validateExpNoteCode(orderStockTag.getTransInputDTO().getActionCode())) {
                throw new LogicException("", "mn.stock.expNote.invalid");
            }
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happen", ex);
        }
    }

    @Secured("@")
    public void doResetExportCollaborator() {
        initTagProduct();
        doReset();
    }

    public StreamedContent retrieveStockFromCollaboratorExcel() {
        try {
            orderStockTag.getTransInputDTO().setToOwnerType(Const.OWNER_TYPE.STAFF_LONG);
            orderStockTag.getTransInputDTO().setToOwnerId(staffDTO.getStaffId());
            StockTransDTO stockTransDTO = orderStockTag.getStockTransDTOForPint();
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

    private boolean validateExpNoteCode(String noteCode) throws LogicException {
        try {
            if (DataUtil.safeEqual(Const.L_VT_SHOP_ID, staffDTO.getShopId())) {
                String actionCodeNote = staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PN, Const.STOCK_TRANS_TYPE.AGENT, BccsLoginSuccessHandler.getStaffDTO());
                if (!DataUtil.safeEqual(actionCodeNote, noteCode)) {
                    return false;
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return false;
        }
        return true;
    }

    public StaffDTO getStaffDTO() {
        return staffDTO;
    }

    public void setStaffDTO(StaffDTO staffDTO) {
        this.staffDTO = staffDTO;
    }

    public OrderStockTagNameable getOrderStockTag() {
        return orderStockTag;
    }

    public void setOrderStockTag(OrderStockTagNameable orderStockTag) {
        this.orderStockTag = orderStockTag;
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

    public List<OptionSetValueDTO> getListPayMethod() {
        return listPayMethod;
    }

    public void setListPayMethod(List<OptionSetValueDTO> listPayMethod) {
        this.listPayMethod = listPayMethod;
    }

    public Long getStockTransActionId() {
        return stockTransActionId;
    }

    public void setStockTransActionId(Long stockTransActionId) {
        this.stockTransActionId = stockTransActionId;
    }
}