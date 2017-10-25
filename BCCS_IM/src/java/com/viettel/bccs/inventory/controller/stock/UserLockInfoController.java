package com.viettel.bccs.inventory.controller.stock;

import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.LockUserInfoDTO;
import com.viettel.bccs.inventory.dto.StaffDTO;
import com.viettel.bccs.inventory.dto.StaffTagConfigDTO;
import com.viettel.bccs.inventory.dto.VShopStaffDTO;
import com.viettel.bccs.inventory.service.LockUserInfoService;
import com.viettel.bccs.inventory.service.StaffService;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.bccs.inventory.tag.StaffInfoNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.web.common.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.util.List;

/**
 * Created by HoangAnh on 8/25/2016.
 */

@ManagedBean
@Component
@Scope("view")
public class UserLockInfoController extends BaseController {

    @Autowired
    private ShopInfoNameable shopInfoTag;

    @Autowired
    private StaffInfoNameable staffInfoTag;

    @Autowired
    private LockUserInfoService lockUserInfoService;

    @Autowired
    private StaffService staffService;

    private StaffDTO staffDTOLogin;

    private StaffDTO staffDTOSearch;

    StaffTagConfigDTO staffTagConfigDTO;

    private String transType;

    private String lockType;

    List<LockUserInfoDTO> lockUserInfoDTOList;

    private boolean displayDeleteButton = false;

    @PostConstruct
    public void init() {
        try {
            displayDeleteButton = false;
            staffDTOLogin       = BccsLoginSuccessHandler.getStaffDTO();

            shopInfoTag.initShopAndAllChild(this, DataUtil.safeToString(staffDTOLogin.getShopId()), true, Const.OWNER_TYPE.SHOP);
            shopInfoTag.loadShop(DataUtil.safeToString(staffDTOLogin.getShopId()), false);

            reloadStaff(DataUtil.safeToString(staffDTOLogin.getShopId()));
            staffInfoTag.loadStaff(staffService.findStaffById(DataUtil.safeToString(staffDTOLogin.getStaffId())));
            staffDTOSearch = new StaffDTO();
            staffDTOSearch.setStaffCode(staffDTOLogin.getStaffCode());
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    public void reloadStaff(String shopId) throws Exception {
        staffInfoTag.resetProduct();
        staffTagConfigDTO = new StaffTagConfigDTO("", shopId, null);
        staffInfoTag.init(this, staffTagConfigDTO);
    }

    @Secured("@")
    public void searchLockUserInfo() {
        try {
            displayDeleteButton = false;
            lockUserInfoDTOList = lockUserInfoService.getListLockUser(staffDTOSearch.getStaffCode(), lockType, transType);
            if (lockUserInfoDTOList != null) {
                displayDeleteButton = true;
            }
        } catch (Exception e) {
            reportError("", "", "common.error.happened");
            logger.debug(e.getMessage(), e);
        }
    }

    @Secured("@")
    public void deleteLockUserInfo() {
        try {
            String deleteId             = staffInfoTag.getvShopStaffDTO().getOwnerId().toString();
            String userId               = staffService.getStaffByStaffCode(BccsLoginSuccessHandler.getUserName()).getStaffId().toString();
            BaseMessage responseMessage = lockUserInfoService.delete(deleteId, userId);

            if (responseMessage.isSuccess()) {
                reportSuccess("","common.msg.success.delete");
            } else {
                reportError("", "", "mn.stock.utilities.error.msg");
            }
            searchLockUserInfo();
        } catch (Exception e) {
            logger.debug(e.getMessage(), e);
        }
    }

    @Secured("@")
    public void receiveShop(VShopStaffDTO vShopStaffDTO) {
        try {
            reloadStaff(vShopStaffDTO.getOwnerId());
        } catch (Exception e) {
            logger.debug(e.getMessage(), e);
        }
        //stockDebitDTO.setOwnerId(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
    }

    @Secured("@")
    public void receiveStaff(VShopStaffDTO vShopStaffDTO) {
        staffDTOSearch.setStaffId(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
        staffDTOSearch.setStaffCode(vShopStaffDTO.getOwnerCode());
    }

    @Secured("@")
    public void clearOwnerShop() {
        try {
            shopInfoTag.resetShop();
            reloadStaff(DataUtil.safeToString(staffDTOLogin.getShopId()));
        } catch (Exception e) {
            logger.debug(e.getMessage(), e);
        }
    }

    @Secured("@")
    public void clearOwnerStaff() {
        staffDTOSearch.setStaffCode(null);
    }

    public void topReportError(String displayArea, LogicException ex) {
        if (ex.getParamsMsg() != null) {
            String[] params = ex.getParamsMsg();
            for (int i = 0; i < params.length; i++) {
                params[i] = getText(params[i]);
            }
            ex = new LogicException(ex.getErrorCode(), ex.getKeyMsg(), params);
        }
        super.reportError(displayArea, ex);
        super.topPage();
    }

    public void topReportError(String displayArea, String keyMsg, Exception ex) {
        super.reportError(displayArea, keyMsg, ex);
        super.topPage();
    }

    public void topReportError(String displayArea, String errorCode, String keyMsg) {
        super.reportError(displayArea, errorCode, keyMsg);
        super.topPage();
    }

    /* ********************************** SETTER AND GETTERS **************************** */
    public StaffService getStaffService() {
        return staffService;
    }

    public void setStaffService(StaffService staffService) {
        this.staffService = staffService;
    }

    public ShopInfoNameable getShopInfoTag() {
        return shopInfoTag;
    }

    public void setShopInfoTag(ShopInfoNameable shopInfoTag) {
        this.shopInfoTag = shopInfoTag;
    }

    public StaffInfoNameable getStaffInfoTag() {
        return staffInfoTag;
    }

    public void setStaffInfoTag(StaffInfoNameable staffInfoTag) {
        this.staffInfoTag = staffInfoTag;
    }

    public StaffTagConfigDTO getStaffTagConfigDTO() {
        return staffTagConfigDTO;
    }

    public void setStaffTagConfigDTO(StaffTagConfigDTO staffTagConfigDTO) {
        this.staffTagConfigDTO = staffTagConfigDTO;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getLockType() {
        return lockType;
    }

    public void setLockType(String lockType) {
        this.lockType = lockType;
    }

    public List<LockUserInfoDTO> getLockUserInfoDTOList() {
        return lockUserInfoDTOList;
    }

    public void setLockUserInfoDTOList(List<LockUserInfoDTO> lockUserInfoDTOList) {
        this.lockUserInfoDTOList = lockUserInfoDTOList;
    }

    public boolean isDisplayDeleteButton() {
        return displayDeleteButton;
    }

    public void setDisplayDeleteButton(boolean displayDeleteButton) {
        this.displayDeleteButton = displayDeleteButton;
    }

    public StaffDTO getStaffDTOLogin() {
        return staffDTOLogin;
    }

    public void setStaffDTOLogin(StaffDTO staffDTOLogin) {
        this.staffDTOLogin = staffDTOLogin;
    }

    public StaffDTO getStaffDTOSearch() {
        return staffDTOSearch;
    }

    public void setStaffDTOSearch(StaffDTO staffDTOSearch) {
        this.staffDTOSearch = staffDTOSearch;
    }
}
