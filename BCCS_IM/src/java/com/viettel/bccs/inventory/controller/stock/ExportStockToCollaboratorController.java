package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.ExecuteStockTransService;
import com.viettel.bccs.inventory.service.StaffService;
import com.viettel.bccs.inventory.service.StockDepositService;
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
 * Anonymous
 */
@Component
@Scope("view")
@ManagedBean(name = "exportStockToCollaboratorController")
public class ExportStockToCollaboratorController extends TransCommonController {
    private StaffDTO staffDTO;
    private boolean canPrint;
    @Autowired
    private OrderStockTagNameable orderStockTag;//khai bao tag thong tin lenh xuat
    @Autowired
    private ListProductNameable listProductTag;//khai bao tag danh sach hang hoa
    @Autowired
    private ExecuteStockTransService executeStockTransService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private StockDepositService stockDepositService;

    private RequiredRoleMap requiredRoleMap;
    private Long stockTransActionId;
    private Long channelTypeId;

    @PostConstruct
    public void init() {
        try {
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
            String actionCodeNote = staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PX, Const.STOCK_TRANS_TYPE.EXPORT, BccsLoginSuccessHandler.getStaffDTO());
            canPrint = false;
            orderStockTag.initCollaborator(this);
            orderStockTag.getTransInputDTO().setActionCode(actionCodeNote);
            staffDTO = BccsLoginSuccessHandler.getStaffDTO();
            ConfigListProductTagDTO configListProductTagDTO = new ConfigListProductTagDTO(Const.MODE_SERIAL.MODE_ADD_ALL, staffDTO.getStaffId(), Const.OWNER_TYPE.STAFF);
            configListProductTagDTO.setCollaborator(true);
            listProductTag.init(this, configListProductTagDTO);
            List<String> listValue = Lists.newArrayList("1", "3", "4");
            listProductTag.setLsProductStatus(DataUtil.defaultIfNull(optionSetValueService.getStatusOptionSetValueByStockState(Const.OPTION_SET.STOCK_STATE, listValue), new ArrayList<OptionSetValueDTO>()));
//            if (DataUtil.isNullOrEmpty(listProductTag.getLsProductOfferTypeDTOTmp())) {
//                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stockTrans.validate.export.stock.empty", staffDTO.getName());
//            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception e) {
            logger.error(e);
            reportError("", "common.error.happened", e);
        }
    }

    @Secured("@")
    public void doExportStockToCollaborator() {
        try {
            stockTransActionId = null;
            //validate ngay nhap phai la dinh dang so, va phai la so nguyen duong
            StockTransActionDTO stockTransActionDTO = orderStockTag.getStockTransActionDTO();
            stockTransActionDTO.setActionStaffId(staffDTO.getStaffId());
            StockTransDTO stockTransDTO = orderStockTag.getStockTransDTO();
            stockTransDTO.setIsAutoGen(null);
            stockTransDTO.setShopId(staffDTO.getShopId());
            stockTransDTO.setUserCreate(staffDTO.getStaffCode());
            stockTransDTO.setTotalAmount(listProductTag.getTotalPriceAmount());
            stockTransDTO.setCreateUserIpAdress(BccsLoginSuccessHandler.getIpAddress());
            List<StockTransDetailDTO> stockTransDetailDTOs = listProductTag.getListTransDetailDTOs();
            //validate max_stock trong stock_deposit
            for (StockTransDetailDTO stockTransDetailDTO : stockTransDetailDTOs) {
                StockDepositDTO stockDepositDTO = stockDepositService.getStockDeposit(stockTransDetailDTO.getProdOfferId(), channelTypeId, Const.CONFIG_PRODUCT.TYPE_DEPOSIT);
                if (DataUtil.isNullObject(stockDepositDTO)) {
                    throw new LogicException("", "agent.export.order.not.config.stock.deposit", stockTransDetailDTO.getProdOfferName());
                }
                if (stockTransDetailDTO.getQuantity() > stockDepositDTO.getMaxStock()) {
                    throw new LogicException("", "agent.export.order.stock.deposit.max", stockTransDetailDTO.getProdOfferName(), stockDepositDTO.getMaxStock());
                }
            }
            BaseMessageStockTrans message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.EXPORT_COLLABORATOR, Const.STOCK_TRANS_TYPE.EXPORT, stockTransDTO, stockTransActionDTO, stockTransDetailDTOs, requiredRoleMap);
            if (!DataUtil.isNullOrEmpty(message.getErrorCode())) {
                throw new LogicException(message.getErrorCode(), message.getKeyMsg(), message.getParamsMsg());
            }
            reportSuccess("", "export.stock.success");
            canPrint = true;
            stockTransActionId = message.getStockTransActionId();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            topReportError("", ex);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    @Secured("@")
    public void doValidateExportStockToCollaborator() {
        try {
            if (!DataUtil.isNullObject(orderStockTag.getTransInputDTO()) &&
                    DataUtil.isNullOrEmpty(orderStockTag.getTransInputDTO().getActionCode())) {
                throw new LogicException("", "export.order.stock.code.require.msg");
            }
            if (!DataUtil.isNullObject(orderStockTag.getTransInputDTO()) &&
                    !DataUtil.isNullOrEmpty(orderStockTag.getTransInputDTO().getActionCode()) &&
                    !DataUtil.validateStringByPattern(orderStockTag.getTransInputDTO().getActionCode(), getText("ACTION_CODE_REGEX"))) {
                throw new LogicException("", "export.order.transCode.error.format.msg");
            }
            if (!validateExpNoteCode(orderStockTag.getTransInputDTO().getActionCode())) {
                throw new LogicException("", "mn.stock.expNote.invalid");
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        }
    }

    public StreamedContent exportStockToCollaboratorExcel() {
        try {
            StockTransDTO stockTransDTO = orderStockTag.getStockTransDTOForPint();
            if (stockTransDTO != null && canPrint) {
                List<StockTransDetailDTO> stockTransDetailDTOs = listProductTag.getListTransDetailDTOs();
                stockTransDTO.setActionType(Const.STOCK_TRANS_ACTION_TYPE.NOTE);
                stockTransDTO.setStockTransStatus(Const.STOCK_TRANS_TYPE.EXPORT);
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

    @Secured("@")
    public void doResetExportCollaborator() {
        doReset();
    }

    @Secured("@")
    public void doReceiveShop(VShopStaffDTO vShopStaffDTO) {
        try {
            Long branchId = shopService.getBranchId(staffDTO.getShopId());
            ConfigListProductTagDTO configListProductTagDTO = new ConfigListProductTagDTO(Const.MODE_SERIAL.MODE_ADD_ALL, staffDTO.getStaffId(), Const.OWNER_TYPE.STAFF);
            configListProductTagDTO.setCollaborator(true);
            configListProductTagDTO.setChannelTypeId(DataUtil.safeToLong(vShopStaffDTO.getChannelTypeId()));
            channelTypeId = DataUtil.safeToLong(vShopStaffDTO.getChannelTypeId());
            configListProductTagDTO.setType(Const.CONFIG_PRODUCT.TYPE_DEPOSIT);
            configListProductTagDTO.setBranchId(branchId);
            listProductTag.init(this, configListProductTagDTO);
            List<String> listValue = Lists.newArrayList("1", "3", "4");
            listProductTag.setLsProductStatus(DataUtil.defaultIfNull(optionSetValueService.getStatusOptionSetValueByStockState(Const.OPTION_SET.STOCK_STATE, listValue), new ArrayList<OptionSetValueDTO>()));
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            topReportError("", "common.error.happen", ex);
            topPage();
        }
    }

    @Secured("@")
    public void clearCurrentShop() {
        try {
            ConfigListProductTagDTO configListProductTagDTO = new ConfigListProductTagDTO(Const.MODE_SERIAL.MODE_ADD_ALL, staffDTO.getStaffId(), Const.OWNER_TYPE.STAFF);
            configListProductTagDTO.setCollaborator(true);
            listProductTag.init(this, configListProductTagDTO);
            List<String> listValue = Lists.newArrayList("1", "3", "4");
            listProductTag.setLsProductStatus(DataUtil.defaultIfNull(optionSetValueService.getStatusOptionSetValueByStockState(Const.OPTION_SET.STOCK_STATE, listValue), new ArrayList<OptionSetValueDTO>()));
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            topReportError("", "common.error.happen", ex);
            topPage();
        }
    }

    private boolean validateExpNoteCode(String noteCode) throws LogicException {
        try {
            if (DataUtil.safeEqual(Const.L_VT_SHOP_ID, staffDTO.getShopId())) {
                String actionCodeNote = staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PX, Const.STOCK_TRANS_TYPE.EXPORT, BccsLoginSuccessHandler.getStaffDTO());
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

    public boolean isCanPrint() {
        return canPrint;
    }

    public void setCanPrint(boolean canPrint) {
        this.canPrint = canPrint;
    }

    public Long getStockTransActionId() {
        return stockTransActionId;
    }

    public void setStockTransActionId(Long stockTransActionId) {
        this.stockTransActionId = stockTransActionId;
    }
}
