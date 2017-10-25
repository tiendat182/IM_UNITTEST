package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.ChangeModelTransDetailService;
import com.viettel.bccs.inventory.service.ChangeModelTransSerialService;
import com.viettel.bccs.inventory.service.ChangeModelTransService;
import com.viettel.bccs.inventory.service.OptionSetValueService;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.bccs.inventory.tag.SignOfficeTagNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.web.common.security.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by hoangnt14 on 8/25/2016.
 */
@Component
@Scope("view")
@ManagedBean(name = "approveChangeProductController")
public class ApproveChangeProductController extends TransCommonController {

    private ApproveChangeProductDTO searchInputDTO = new ApproveChangeProductDTO();
    private List<OptionSetValueDTO> statusList;
    private boolean writeOffice = true;
    private boolean checkErp = false;
    private List<ApproveChangeProductDTO> lstSearch = Lists.newArrayList();
    private StaffDTO staffDTO;
    private RequiredRoleMap requiredRoleMap;
    private boolean showDetail;
    private boolean showDetailSerial;
    private List<ChangeModelTransDetailDTO> lstChangeDetail = Lists.newArrayList();
    private List<ChangeModelTransSerialDTO> lstChangeDetailSerial = Lists.newArrayList();
    private Long changeModelTransId;
    private SignOfficeDTO signOfficeDTO;

    @Autowired
    private ShopInfoNameable shopInfoTag;
    @Autowired
    private SignOfficeTagNameable writeOfficeTag;//khai bao tag ky vOffice
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private ChangeModelTransService changeModelTransService;
    @Autowired
    private ChangeModelTransDetailService changeModelTransDetailService;
    @Autowired
    private ChangeModelTransSerialService changeModelTransSerialService;

    @Secured("@")
    @PostConstruct
    public void init() {
        try {
            if (staffDTO == null) {
                staffDTO = BccsLoginSuccessHandler.getStaffDTO();
            }
            statusList = optionSetValueService.getByOptionSetCode(Const.CHANGE_PRODUCT.APPROVE_CHANGE_PRODUCT_STATUS);
            doReset();
            requiredRoleMap = CustomAuthenticationProvider.createRequiredRoleMap(Const.PERMISION.ROLE_STOCK_NUM_SHOP, Const.PERMISION.ROLE_DONGBO_ERP);
            doSearch();
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
    public void doSearch() {
        try {
            lstSearch = changeModelTransService.getLstChangeStockModel(searchInputDTO);
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
    public void doReset() {
        writeOffice = true;
        writeOfficeTag.init(this, staffDTO.getShopId());
        showDetail = false;
        showDetailSerial = false;
        shopInfoTag.initShopBranchAndVTT(this, DataUtil.safeToString(staffDTO.getShopId()), Const.MODE_SHOP.LIST_SHOP_BRANCH_AND_VTT);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        searchInputDTO.setStartDate(cal.getTime());
        searchInputDTO.setEndDate(cal.getTime());
        checkErp = false;
        lstSearch = Lists.newArrayList();
    }

    @Secured("@")
    public void doViewDetail(Long changeModelTransId) {
        try {
            showDetail = true;
            showDetailSerial = false;
            lstChangeDetail = changeModelTransDetailService.viewDetail(changeModelTransId);
            topPage();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happened");
            topPage();
        }
    }

    @Secured("@")
    public void doViewDetailSerial(Long changeModelTransDetailId) {
        try {
            showDetailSerial = true;
            lstChangeDetailSerial = changeModelTransSerialService.viewDetailSerial(changeModelTransDetailId);
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happened");
            topPage();
        }
    }

    @Secured("@")
    public void doValidate(Long changeModelTransId, String type) {
        try {
            this.changeModelTransId = changeModelTransId;
            if (writeOffice && DataUtil.safeEqual(type, "1")) {
                signOfficeDTO = writeOfficeTag.validateVofficeAccount();
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
    public void doApprove() {
        try {
            StockTransDTO stockTransDTO = new StockTransDTO();
            stockTransDTO.setCheckErp(checkErp ? Const.STOCK_TRANS.IS_NOT_ERP : null);
            if (writeOffice) {
                stockTransDTO.setUserName(signOfficeDTO.getUserName());
                stockTransDTO.setPassWord(signOfficeDTO.getPassWord());
                stockTransDTO.setSignFlowId(signOfficeDTO.getSignFlowId());
                stockTransDTO.setSignVoffice(writeOffice);
            }
            changeModelTransService.approveChangeProduct(changeModelTransId, staffDTO, stockTransDTO, requiredRoleMap);
            reportSuccess("", "mn.stock.change.product.offering.approve.success");
            doSearch();
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
    public void doCancel() {
        try {
            changeModelTransService.cancelChangeProduct(changeModelTransId, staffDTO);
            reportSuccess("", "mn.stock.change.product.offering.approve.cancel.success");
            doSearch();
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


    @Secured("@")
    public void doComeBack() {
        showDetail = false;
        doSearch();
    }

    @Secured("@")
    public void receiveShop(VShopStaffDTO vShopStaffDTO) {
        if (vShopStaffDTO != null) {
            searchInputDTO.setShopId(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
        }
    }

    @Secured("@")
    public boolean isShowCheckErp() {
        return requiredRoleMap != null && requiredRoleMap.hasRole(Const.PERMISION.ROLE_DONGBO_ERP);
    }

    @Secured("@")
    public void clearCurrentShop() {
        searchInputDTO.setShopId(null);
    }

    @Secured("@")
    public void receiveWriteOffice() {
        //
    }

    public ShopInfoNameable getShopInfoTag() {
        return shopInfoTag;
    }

    public void setShopInfoTag(ShopInfoNameable shopInfoTag) {
        this.shopInfoTag = shopInfoTag;
    }

    public ApproveChangeProductDTO getSearchInputDTO() {
        return searchInputDTO;
    }

    public void setSearchInputDTO(ApproveChangeProductDTO searchInputDTO) {
        this.searchInputDTO = searchInputDTO;
    }

    public List<OptionSetValueDTO> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<OptionSetValueDTO> statusList) {
        this.statusList = statusList;
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

    public List<ApproveChangeProductDTO> getLstSearch() {
        return lstSearch;
    }

    public void setLstSearch(List<ApproveChangeProductDTO> lstSearch) {
        this.lstSearch = lstSearch;
    }

    public boolean isShowDetail() {
        return showDetail;
    }

    public void setShowDetail(boolean showDetail) {
        this.showDetail = showDetail;
    }

    public boolean isShowDetailSerial() {
        return showDetailSerial;
    }

    public void setShowDetailSerial(boolean showDetailSerial) {
        this.showDetailSerial = showDetailSerial;
    }

    public List<ChangeModelTransDetailDTO> getLstChangeDetail() {
        return lstChangeDetail;
    }

    public void setLstChangeDetail(List<ChangeModelTransDetailDTO> lstChangeDetail) {
        this.lstChangeDetail = lstChangeDetail;
    }

    public List<ChangeModelTransSerialDTO> getLstChangeDetailSerial() {
        return lstChangeDetailSerial;
    }

    public void setLstChangeDetailSerial(List<ChangeModelTransSerialDTO> lstChangeDetailSerial) {
        this.lstChangeDetailSerial = lstChangeDetailSerial;
    }

    public boolean isCheckErp() {
        return checkErp;
    }

    public void setCheckErp(boolean checkErp) {
        this.checkErp = checkErp;
    }
}
