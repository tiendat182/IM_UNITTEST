package com.viettel.bccs.inventory.controller.stock;

import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.ReasonService;
import com.viettel.bccs.inventory.service.ShopService;
import com.viettel.bccs.inventory.service.StaffService;
import com.viettel.bccs.inventory.service.StockDebitService;
import com.viettel.bccs.inventory.tag.ListProductNameable;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by tuyendv8 on 11/11/2015.
 */
@Component
@Scope("view")
@ManagedBean
public class AboveExportNoteController extends TransCommonController {

    private RequiredRoleMap requiredRoleMap;
    private boolean infoOrderDetail;
    private boolean writeVoffice;
    private boolean created = false;
    private StockTransDTO inputStockTransDTO;
    private List<ReasonDTO> reasons;
    private StockDebitDTO stockDebitDTO;
    @Autowired
    private StockDebitService stockDebitService;

    @Autowired
    private ShopService shopService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private SignOfficeTagNameable writeOfficeTag;
    @Autowired
    private ReasonService reasonService;
    @Autowired
    private ListProductNameable lstProductTag;

    private List<OptionSetValueDTO> optionSetValueDTOTransports;
    private Boolean tranfer;

    @PostConstruct
    public void init() {
        initControl();
    }

    private void initControl() {
        try {
            //sonat outsource
            tranfer = false;
            optionSetValueDTOTransports = optionSetValueService.getByOptionSetCode("TRANSPORT_TYPE");
            //sonat outsource
            created = false;
            inputStockTransDTO = new StockTransDTO();
            inputStockTransDTO.setActionCode(staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PX, Const.STOCK_TRANS_TYPE.EXPORT, getStaffDTO()));
            inputStockTransDTO.setUserCreate(getStaffDTO().getStaffCode());
            inputStockTransDTO.setFromOwnerName(getStaffDTO().getShopName());
            inputStockTransDTO.setFromOwnerId(getStaffDTO().getShopId());
            inputStockTransDTO.setFromOwnerType(Const.OWNER_TYPE.SHOP_LONG);

            VShopStaffDTO parrentShop = shopService.getParentShopByShopId(DataUtil.safeToLong(getStaffDTO().getShopId()));
            if (DataUtil.isNullObject(parrentShop)) {
                inputStockTransDTO.setFromOwnerName(getStaffDTO().getShopName());
                inputStockTransDTO.setFromOwnerId(getStaffDTO().getShopId());
            } else {
                StringBuilder name = new StringBuilder(parrentShop.getOwnerCode()).append("-").append(parrentShop.getOwnerName());
                inputStockTransDTO.setToOwnerName(name.toString());
                inputStockTransDTO.setToOwnerId(DataUtil.safeToLong(parrentShop.getOwnerId()));
            }

            inputStockTransDTO.setToOwnerType(Const.OWNER_TYPE.SHOP_LONG);
            inputStockTransDTO.setCreateDatetime(getSysdateFromDB());
            inputStockTransDTO.setTransport(null);
            inputStockTransDTO.setActionType(Const.STOCK_TRANS_ACTION_TYPE.NOTE);
            inputStockTransDTO.setUserCreate(getStaffDTO().getStaffCode());
            inputStockTransDTO.setStaffId(getStaffDTO().getStaffId());
            //
            reasons = DataUtil.defaultIfNull(reasonService.getLsReasonByType(Const.REASON_CODE.STOCK_EXP_SERNIOR), new ArrayList<ReasonDTO>());
            //
            lstProductTag.init(this, new ConfigListProductTagDTO(Const.MODE_SERIAL.MODE_NO_SERIAL, getStaffDTO().getShopId(), Const.OWNER_TYPE.SHOP));
            //
            writeVoffice = true;
            //
            writeOfficeTag.init(this, getStaffDTO().getShopId());
            //
            requiredRoleMap = CustomAuthenticationProvider.createRequiredRoleMap(Const.PERMISION.ROLE_SYNC_LOGISTIC,
                    Const.PERMISION.ROLE_TRANSPORT_STOCK, Const.PERMISION.ROLE_XUAT_KHO_BA_MIEN, Const.PERMISION.ROLE_STOCK_NUM_SHOP);
            //
            executeCommand("updateControls()");
            //xu ly load han muc kho nhan neu kho nhan khong phai la VT
            if (!(Const.SHOP.SHOP_VTT_ID.equals(inputStockTransDTO.getToOwnerId()) || Const.SHOP.SHOP_PARENT_VTT_ID.equals(inputStockTransDTO.getToOwnerId()))) {
                if (inputStockTransDTO.getToOwnerId() != null && inputStockTransDTO.getToOwnerType() != null) {

                    stockDebitDTO = stockDebitService.findStockDebitValue(DataUtil.safeToLong(inputStockTransDTO.getToOwnerId()), DataUtil.safeToString(inputStockTransDTO.getToOwnerType()));
                }
            }
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    public boolean isShowStockDebit() {
        return stockDebitDTO != null;
    }

    public void doReset() {}

    public boolean isCreated() {
        return created;
    }

    public void setCreated(boolean created) {
        this.created = created;
    }

    public SignOfficeTagNameable getWriteOfficeTag() {
        return writeOfficeTag;
    }

    public void setWriteOfficeTag(SignOfficeTagNameable writeOfficeTag) {
        this.writeOfficeTag = writeOfficeTag;
    }

    public ListProductNameable getLstProductTag() {
        return lstProductTag;
    }

    public void setLstProductTag(ListProductNameable lstProductTag) {
        this.lstProductTag = lstProductTag;
    }

    public StockTransDTO getInputStockTransDTO() {
        return inputStockTransDTO;
    }

    public void setInputStockTransDTO(StockTransDTO inputStockTransDTO) {
        this.inputStockTransDTO = inputStockTransDTO;
    }

    public List<ReasonDTO> getReasons() {
        return reasons;
    }

    public void setReasons(List<ReasonDTO> reasons) {
        this.reasons = reasons;
    }

    @Secured("@")
    public void changeWriteVoffice() {
        //TODO xu li khi thay doi ki Voffice // update vao truong voffice status
        if (writeVoffice) {

        } else {

        }
    }

    public Date getCurrentDate() {
        return Calendar.getInstance().getTime();
    }

    public boolean isInfoOrderDetail() {
        return infoOrderDetail;
    }

    public void setInfoOrderDetail(boolean infoOrderDetail) {
        this.infoOrderDetail = infoOrderDetail;
    }

    public boolean isWriteVoffice() {
        return writeVoffice;
    }

    public void setWriteVoffice(boolean writeVoffice) {
        this.writeVoffice = writeVoffice;
    }

    @Secured("@")
    public void doCreateNote() {
        try {
            StockTransActionDTO stockTransActionDTO = createAction();

            //outsource
            if (!tranfer) {
                inputStockTransDTO.setTransport(null);
                inputStockTransDTO.setTransportSource(null);
            }
            //end outsource

            //action code
            try {
                doValidateActionCode(inputStockTransDTO.getActionCode());
            } catch (LogicException ex) {
                logger.error(ex.getMessage(), ex);
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.note.transCode.valid");
            }
            //voffice
            if (writeVoffice) {
                SignOfficeDTO signOfficeDTO = writeOfficeTag.validateVofficeAccount();
                inputStockTransDTO.setUserName(signOfficeDTO.getUserName());
                inputStockTransDTO.setPassWord(signOfficeDTO.getPassWord());
                inputStockTransDTO.setSignFlowId(signOfficeDTO.getSignFlowId());
                inputStockTransDTO.setSignVoffice(writeVoffice);
            }
            List<StockTransDetailDTO> detail = lstProductTag.getListTransDetailDTOs();
            if (DataUtil.isNullOrEmpty(detail)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "stock.trans.staff.export.detailRequired");
            }
            BaseMessage msg = executeStockTrans.executeStockTrans(Const.STOCK_TRANS.NOTE, Const.STOCK_TRANS_TYPE.EXPORT, inputStockTransDTO, stockTransActionDTO, detail, requiredRoleMap);
            if (DataUtil.isNullObject(msg)) {
                throw new Exception("Khong nhan duoc ban tin tra ve tu WS");
            } else {
                if (!DataUtil.isNullOrEmpty(msg.getErrorCode())) {
                    throw new LogicException(msg.getErrorCode(), msg.getKeyMsg(), msg.getParamsMsg());
                }
            }
            topReportSuccess("", "export.note.create.success", inputStockTransDTO.getActionCode());
            created = true;
            lstProductTag.setDisableRemove();
            updateElemetId("frmExportNote");
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
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

    @Secured("@")
    public StreamedContent printStockTransDetail() {
        try {
            List<StockTransDetailDTO> details = lstProductTag.getListTransDetailDTOs();
            return exportStockTransDetail(inputStockTransDTO, details);
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
        return null;
    }

    @Secured("@")
    public void doValidate() {
        try {
            if (inputStockTransDTO.getActionCode().length() > 50) {
                throw new Exception();
            }
            List<StockTransDetailDTO> details = lstProductTag.getListTransDetailDTOs();
            doValidateListDetail(details);
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    private StockTransActionDTO createAction() {
        StockTransActionDTO action = new StockTransActionDTO();
        action.setActionCode(inputStockTransDTO.getActionCode());
        action.setActionStaffId(getStaffDTO().getStaffId());
        action.setNote(inputStockTransDTO.getNote());
        action.setCreateUser(getStaffDTO().getStaffCode());
        return action;
    }

    public StockDebitDTO getStockDebitDTO() {
        return stockDebitDTO;
    }

    public void setStockDebitDTO(StockDebitDTO stockDebitDTO) {
        this.stockDebitDTO = stockDebitDTO;
    }

    public List<OptionSetValueDTO> getOptionSetValueDTOTransports() {
        return optionSetValueDTOTransports;
    }

    public void setOptionSetValueDTOTransports(List<OptionSetValueDTO> optionSetValueDTOTransports) {
        this.optionSetValueDTOTransports = optionSetValueDTOTransports;
    }

    public Boolean getTranfer() {
        return tranfer;
    }

    public void setTranfer(Boolean tranfer) {
        this.tranfer = tranfer;
    }
}
