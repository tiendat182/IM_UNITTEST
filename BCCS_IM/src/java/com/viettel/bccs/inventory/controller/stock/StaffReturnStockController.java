package com.viettel.bccs.inventory.controller.stock;

import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.ReasonService;
import com.viettel.bccs.inventory.service.ShopService;
import com.viettel.bccs.inventory.service.StaffService;
import com.viettel.bccs.inventory.service.StockDebitService;
import com.viettel.bccs.inventory.tag.ListFifoProductNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.dto.BaseMessage;
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
 * Created by tuyendv8 on 11/11/2015.
 */
@Component
@Scope("view")
@ManagedBean
public class StaffReturnStockController extends TransCommonController {

    private BaseMessage baseMessage;
    private StockDebitDTO stockDebitDTO;
    @Autowired
    private StockDebitService stockDebitService;

    @PostConstruct
    public void init() {
        initControl();
    }

    private void initControl() {
        try {
            created = false;
            stockDebitDTO = null;
            inputStockTransDTO = new StockTransDTO();
            inputStockTransDTO.setActionCode(staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PX, Const.STOCK_TRANS_TYPE.EXPORT, getStaffDTO()));
            inputStockTransDTO.setUserCreate(getStaffDTO().getStaffCode());
            inputStockTransDTO.setStaffId(getStaffDTO().getStaffId());
            //
            inputStockTransDTO.setActionType(Const.STOCK_TRANS_ACTION_TYPE.NOTE);
            inputStockTransDTO.setStockTransStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
            //
            StaffDTO staffDTO = staffService.findOne(getStaffDTO().getStaffId());
            inputStockTransDTO.setUserCreate(staffDTO.getName());
            inputStockTransDTO.setFromOwnerId(getStaffDTO().getStaffId());
            inputStockTransDTO.setFromOwnerType(Const.OWNER_TYPE.STAFF_LONG);
            inputStockTransDTO.setFromOwnerName(getStaffDTO().getName());
            inputStockTransDTO.setFromOwnerAddress(staffDTO.getAddress());
            //
            ShopDTO shopDTO = shopService.findOne(getStaffDTO().getShopId());
            inputStockTransDTO.setToOwnerName(getStaffDTO().getShopName());
            inputStockTransDTO.setToOwnerId(getStaffDTO().getShopId());
            inputStockTransDTO.setToOwnerType(Const.OWNER_TYPE.SHOP_LONG);
            inputStockTransDTO.setToOwnerAddress(shopDTO.getAddress());
            //
            inputStockTransDTO.setCreateDatetime(getSysdateFromDB());
            inputStockTransDTO.setTransport(Const.STOCK_TRANSPORT_TYPE.YES);
            inputStockTransDTO.setIsAutoGen(Const.STOCK_TRANS.IS_AUTO_GEN_LOGISTIC);
            inputStockTransDTO.setLogicstic(Const.STOCK_TRANS.IS_LOGISTIC);
            //
            reasons = DataUtil.defaultIfNull(reasonService.getLsReasonByType(Const.REASON_TYPE.STOCK_EXP_STAFF_SHOP), new ArrayList<ReasonDTO>());
            //
            lstProductTag.init(this, new ConfigListProductTagDTO(Const.MODE_SERIAL.MODE_ADD_ALL, BccsLoginSuccessHandler.getStaffDTO().getStaffId(), Const.OWNER_TYPE.STAFF));
            //
            transport = true;
            //
            logistics = true;
            //
            if (!hasLogsitics()) {
                inputStockTransDTO.setIsAutoGen(null);
                inputStockTransDTO.setLogicstic(null);
            }
            if (!hasTransport()) {
                inputStockTransDTO.setTransport(null);
            }
            //xu ly load han muc kho nhan neu kho nhan khong phai la VT
            if (!(Const.SHOP.SHOP_VTT_ID.equals(inputStockTransDTO.getToOwnerId()) || Const.SHOP.SHOP_PARENT_VTT_ID.equals(inputStockTransDTO.getToOwnerId()))) {
                if (inputStockTransDTO.getToOwnerId() != null && inputStockTransDTO.getToOwnerType() != null) {
                    stockDebitDTO = stockDebitService.findStockDebitValue(DataUtil.safeToLong(inputStockTransDTO.getToOwnerId()), DataUtil.safeToString(inputStockTransDTO.getToOwnerType()));
                }
            }
            executeCommand("updateControls()");
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    public boolean isShowStockDebit() {
        return stockDebitDTO != null;
    }

    @Autowired
    StaffService staffService;
    @Autowired
    ShopService shopService;

    @Autowired
    ReasonService reasonService;
    @Autowired
    private ListFifoProductNameable lstProductTag;

    List<ReasonDTO> reasons;
    StockTransDTO inputStockTransDTO;
    List<StockTransFullDTO> stockTransDetailDTOs;
    private boolean transport = true;
    private boolean logistics = true;

    public boolean isLogistics() {
        return logistics;
    }

    public void setLogistics(boolean logistics) {
        this.logistics = logistics;
    }

    public boolean isTransport() {
        return transport;
    }

    public void setTransport(boolean transport) {
        this.transport = transport;
    }

    public ListFifoProductNameable getLstProductTag() {
        return lstProductTag;
    }

    public void setLstProductTag(ListFifoProductNameable lstProductTag) {
        this.lstProductTag = lstProductTag;
    }

    public List<ReasonDTO> getReasons() {
        return reasons;
    }

    public void setReasons(List<ReasonDTO> reasons) {
        this.reasons = reasons;
    }

    public List<StockTransFullDTO> getStockTransDetailDTOs() {
        return stockTransDetailDTOs;
    }

    public void setStockTransDetailDTOs(List<StockTransFullDTO> stockTransDetailDTOs) {
        this.stockTransDetailDTOs = stockTransDetailDTOs;
    }

    public StockTransDTO getInputStockTransDTO() {
        return inputStockTransDTO;
    }

    public void setInputStockTransDTO(StockTransDTO inputStockTransDTO) {
        this.inputStockTransDTO = inputStockTransDTO;
    }

    private boolean infoOrderDetail;
    private boolean created = false;

    public boolean isCreated() {
        return created;
    }

    public void setCreated(boolean created) {
        this.created = created;
    }

    public void showInfoOrderDetail() {
        infoOrderDetail = true;
    }

    public boolean isInfoOrderDetail() {
        return infoOrderDetail;
    }

    public void setInfoOrderDetail(boolean infoOrderDetail) {
        this.infoOrderDetail = infoOrderDetail;
    }

    @Secured("@")
    public StreamedContent printStockTransDetail() {
        try {
            doValidateListDetail(lstProductTag.getListTransDetailDTOs());
            List<StockTransDetailDTO> details = lstProductTag.getListTransDetailDTOs();
            inputStockTransDTO.setFromOwnerAddress(null);
            inputStockTransDTO.setToOwnerAddress(null);
            return exportStockTransDetail(inputStockTransDTO, details);
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
        return null;
    }


    @Secured("@")
    public void doCreate() {
        try {
            StockTransActionDTO stockTransActionDTO = createAction();
            List<StockTransDetailDTO> listTransDetailDTOs = lstProductTag.getListTransDetailDTOs();
            if (inputStockTransDTO.getActionCode().getBytes("UTF-8").length > 50) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.note.transCode.valid.maxlength");
            }
            if (DataUtil.isNullOrEmpty(listTransDetailDTOs)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "stock.trans.staff.export.detailRequired");
            }
            for (StockTransDetailDTO detailDTO : listTransDetailDTOs) {
                if (DataUtil.safeEqual(detailDTO.getCheckSerial(), Const.PRODUCT_OFFERING.CHECK_SERIAL)
                        && DataUtil.isNullOrEmpty(detailDTO.getLstSerial())) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "staff.return.stock.valid.serial", detailDTO.getProdOfferName());
                }
            }
            inputStockTransDTO.setFromStock(Const.FROM_STOCK.FROM_STAFF);
            //hoangnt: check neu NV chua co tren TTNS thi khong cho xuat tra hang
            StaffDTO staffDTO = staffService.findOne(inputStockTransDTO.getFromOwnerId());
            if (DataUtil.isNullOrEmpty(staffDTO.getTtnsCode())) {
                throw new LogicException("", "export.order.staff.ttns.not.found.export");
            }
            BaseMessage msg = executeStockTrans.executeStockTrans(Const.STOCK_TRANS.EXPORT, Const.STOCK_TRANS_TYPE.STAFF_EXP, inputStockTransDTO, stockTransActionDTO, listTransDetailDTOs, getTransRequiRedRoleMap());
            if (msg == null) {
                throw new Exception("Khong nhan duoc ket qua tu webservice!");
            }
            if (!DataUtil.isNullOrEmpty(msg.getErrorCode())) {
                throw new LogicException(msg.getErrorCode(), msg.getKeyMsg(), msg.getParamsMsg());
            }
            topReportSuccess("", "stock.trans.staff.export.susscess");
            created = true;
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    @Secured("@")
    public void onChangeTransportType() {
        if (transport) {
            inputStockTransDTO.setTransport(Const.STOCK_TRANSPORT_TYPE.YES);
        } else {
            inputStockTransDTO.setTransport(null);
        }
    }

    @Secured("@")
    public void onChangeLogistics() {
        if (logistics) {
            inputStockTransDTO.setIsAutoGen(Const.STOCK_TRANS.IS_AUTO_GEN_LOGISTIC);
            inputStockTransDTO.setLogicstic(Const.STOCK_TRANS.IS_LOGISTIC);
        } else {
            inputStockTransDTO.setIsAutoGen(null);
            inputStockTransDTO.setLogicstic(null);
        }
    }

    @Secured("@")
    public void onChangeReason() {
        if (!DataUtil.isNullOrEmpty(reasons) && inputStockTransDTO.getReasonId() != null) {
            for (ReasonDTO reason : reasons) {
                if (reason.getReasonId().equals(inputStockTransDTO.getReasonId())) {
                    inputStockTransDTO.setReasonName(reason.getReasonName());
                    break;
                }
            }
        }
    }

    private StockTransActionDTO createAction() {
        StockTransActionDTO action = new StockTransActionDTO();
        action.setActionCode(inputStockTransDTO.getActionCode());
        action.setCreateUser(getStaffDTO().getStaffCode());
        action.setActionStaffId(getStaffDTO().getStaffId());
        action.setNote(inputStockTransDTO.getNote());
        return action;
    }

    @Secured("@")
    public void doValidate() {
        try {
            if (inputStockTransDTO.getActionCode().getBytes("UTF-8").length > 50) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.note.transCode.valid.maxlength");
            }
            doValidateListDetail(lstProductTag.getListTransDetailDTOs());
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    @Secured("@")
    public boolean hasLogsitics() {
        try {
            if (getTransRequiRedRoleMap().hasRole(Const.PERMISION.ROLE_SYNC_LOGISTIC)) {
                List<OptionSetValueDTO> options = DataUtil.defaultIfNull(optionSetValueService.getByOptionsetCodeAndValue(Const.OPTION_SET.LOGISTIC_SHOP_ID_LIST, getStaffDTO().getShopCode()), new ArrayList<>());
                return !DataUtil.isNullOrEmpty(options);
            }
        } catch (Exception exx) {
            logger.error(exx.getMessage(), exx);
        }
        return false;
    }

    @Secured("@")
    public boolean hasTransport() {
        return getTransRequiRedRoleMap().hasRole(Const.PERMISION.ROLE_TRANSPORT_STOCK);
    }

    public StockDebitDTO getStockDebitDTO() {
        return stockDebitDTO;
    }

    public void setStockDebitDTO(StockDebitDTO stockDebitDTO) {
        this.stockDebitDTO = stockDebitDTO;
    }
}
