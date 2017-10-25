package com.viettel.bccs.inventory.tag.impl;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.ManageTransStockDto;
import com.viettel.bccs.inventory.dto.StaffTagConfigDTO;
import com.viettel.bccs.inventory.dto.VShopStaffDTO;
import com.viettel.bccs.inventory.service.StaffService;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.bccs.inventory.tag.StaffInfoNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.web.common.controller.BaseController;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * tag controller danh cho tag location
 *
 * @author ThanhNT77
 */
@Service
@Scope("prototype")
public class StaffInfoTag extends BaseController implements StaffInfoNameable {
    public static final Logger logger = Logger.getLogger(ShopInfoNameable.class);

    private VShopStaffDTO vShopStaffDTO = new VShopStaffDTO();
    private List<VShopStaffDTO> lsShopDto = Lists.newArrayList();
    private Object objectController;
    private String shopId;
    private Boolean isDisableEdit = false;
    private String modeStaff = "";
    List<String> channelTypes;
    private ManageTransStockDto transStock;
    private String staffId;
    private boolean multiTag;

    @Autowired
    private StaffService staffService;

    /**
     * hien thi danh sach goi cuoc theo thong tin nhap tren man hinh
     *
     * @param inputStaff giá trị input của gói cước
     */
    @Secured("@")
    public List<VShopStaffDTO> doSelectStaff(String inputStaff) {
        try {
            if (modeStaff.equals(Const.MODE_SHOP.ISDN_MNGT)) {
                this.lsShopDto = DataUtil.defaultIfNull(staffService.getStaffIsdnMngt(inputStaff, shopId), new ArrayList<VShopStaffDTO>());
            } else if (modeStaff.equals(Const.MODE_SHOP.ISDN_DSTRT)) {
                this.lsShopDto = DataUtil.defaultIfNull(staffService.getStaffDistribute(inputStaff, shopId), new ArrayList<VShopStaffDTO>());
            } else if (modeStaff.equals(Const.MODE_SHOP.ISDN_MNGT_USR_CRT)) {
                if (DataUtil.isNullObject(transStock) || DataUtil.isNullOrEmpty(transStock.getTakeOwnerStockId())) {
                    this.lsShopDto = DataUtil.defaultIfNull(staffService.getStaffIsdnMngtUsrCrt(inputStaff, null), new ArrayList<VShopStaffDTO>());
                } else {
                    this.lsShopDto = DataUtil.defaultIfNull(staffService.getStaffIsdnMngtUsrCrt(inputStaff, transStock.getTakeOwnerStockId()), new ArrayList<VShopStaffDTO>());
                }
            } else if (modeStaff.equals(Const.MODE_SHOP.EMPTY)) {
                this.lsShopDto = Lists.newArrayList();
            } else if (modeStaff.equals(Const.MODE_SHOP.CHILD_OF_PARRENT_SHOP_NO_STAFF)) {
                //lay staff cua parent shop khac staff hien tai
                StaffTagConfigDTO staffTagConfigDTO = new StaffTagConfigDTO(inputStaff, shopId, channelTypes, Const.StaffSearchMode.allChildNotCurrentStaff.value());
                staffTagConfigDTO.setStaffID(staffId);
                this.lsShopDto = DataUtil.defaultIfNull(staffService.getStaffByShopIdAndChanelType(staffTagConfigDTO), new ArrayList<VShopStaffDTO>());
            } else if (modeStaff.equals(Const.MODE_SHOP.CHILD_OF_PARRENT_SHOP)) {
                //lay staff cua parent shop
                this.lsShopDto = DataUtil.defaultIfNull(staffService.getStaffByShopIdAndChanelType(new StaffTagConfigDTO(inputStaff, shopId, channelTypes, Const.StaffSearchMode.connectChild.value())), new ArrayList<VShopStaffDTO>());
            } else if (modeStaff.equals(Const.MODE_SHOP.CHILD_SHOP_ALL_STAFF)) {
                //lay all staff cua shop va parent shop
                this.lsShopDto = DataUtil.defaultIfNull(staffService.getStaffByShopIdAndChanelType(new StaffTagConfigDTO(inputStaff, shopId, channelTypes, Const.StaffSearchMode.allChildShopAndParentShop.value())), new ArrayList<VShopStaffDTO>());
            } else {
                // ListChannelType = null ->> tim kiem tat ca staff
                this.lsShopDto = DataUtil.defaultIfNull(staffService.getStaffByShopIdAndChanelType(new StaffTagConfigDTO(inputStaff, shopId, channelTypes)), new ArrayList<VShopStaffDTO>());
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getKeyMsg(), ex);
            topPage();
        } catch (Exception e) {
            reportError("", "common.error.happened", e);
            logger.error(e.getMessage(), e);
            topPage();
        }
        return lsShopDto;
    }


    /**
     * hàm init shop
     *
     * @author ThanhNT
     */
    @Secured("@")
    public void initStaff(Object objectController, String shopId) {
        this.objectController = objectController;
        this.shopId = shopId;
        vShopStaffDTO = null;
    }

    @Override
    public void initStaffManageIsdn(Object objectController, String shopId, String chanelType, String mode, ManageTransStockDto transStock) {
        this.objectController = objectController;
        this.shopId = shopId;
        vShopStaffDTO = null;
        modeStaff = mode;
        this.transStock = transStock;
        this.channelTypes = Lists.newArrayList(chanelType);
    }

    @Override
    public void initStaff(Object objController, String shopId, String staffId, boolean isDisableEdit) {
        this.objectController = objController;
        this.isDisableEdit = isDisableEdit;
        this.shopId = shopId;
        if (!DataUtil.isNullOrEmpty(staffId)) {
            try {
                vShopStaffDTO = staffService.findStaffById(staffId);
            } catch (Exception ex) {
                reportError("", "common.error.happen", ex);
                topPage();
            }
        } else {
            vShopStaffDTO = null;
        }
    }

    @Override
    public void initStaffWithChanelTypes(Object objectController, String shopId, String staffId, List<String> chanelTypes, boolean isDisableEdit) {
        this.objectController = objectController;
        this.shopId = shopId;
        this.isDisableEdit = isDisableEdit;
        this.channelTypes = chanelTypes;
        this.modeStaff = "";
        if (!DataUtil.isNullOrEmpty(staffId)) {
            try {
                vShopStaffDTO = staffService.findStaffById(staffId);
            } catch (Exception ex) {
                reportError("", "common.error.happen", ex);
                topPage();
            }
        } else {
            vShopStaffDTO = null;
        }
    }

    @Override
    public void initStaffWithChanelTypesMode(Object objectController, String shopId, String staffId, List<String> chanelTypes, boolean isDisableEdit, String mode) {
        this.objectController = objectController;
        this.shopId = shopId;
        this.isDisableEdit = isDisableEdit;
        this.channelTypes = chanelTypes;
        this.modeStaff = mode;
        this.staffId = staffId;
        vShopStaffDTO = null;
    }

    @Override
    public void initStaffWithChanelTypesAndParrentShop(Object objectController, String shopId, String staffId, List<String> chanelTypes, boolean isDisableEdit) {
        this.objectController = objectController;
        this.shopId = shopId;
        this.isDisableEdit = isDisableEdit;
        this.channelTypes = chanelTypes;
        this.modeStaff = Const.MODE_SHOP.CHILD_OF_PARRENT_SHOP;
        if (!DataUtil.isNullOrEmpty(staffId)) {
            try {
                vShopStaffDTO = staffService.findStaffById(staffId);
            } catch (Exception ex) {
                reportError("", "common.error.happen", ex);
                topPage();
            }
        } else {
            vShopStaffDTO = null;
        }
    }

    @Secured("@")
    public void loadStaff(VShopStaffDTO vShopStaffDTO) {
        this.vShopStaffDTO = vShopStaffDTO;
    }

    @Secured("@")
    public void resetProduct() {
        this.vShopStaffDTO = new VShopStaffDTO();
    }

    @Secured("@")
    public void initEmptyStaff() {
        this.vShopStaffDTO = new VShopStaffDTO();
        modeStaff = Const.MODE_SHOP.EMPTY;
    }

    /**
     * get data from method of object
     *
     * @return methodName tên method set data
     * @author ThanhNT
     */
    @Secured("@")
    public void doChangeStaff(String methodName) {
        try {
            if (DataUtil.isNullOrEmpty(methodName)) {
                return;
            }
            Class<?> c = this.objectController.getClass();
            String param = DataUtil.safeToString(UIComponent.getCurrentComponent(FacesContext.getCurrentInstance()).getAttributes().get("param"));
            if (!DataUtil.isNullOrEmpty(param)) {
                Method method = c.getMethod(methodName, VShopStaffDTO.class, String.class);
                method.invoke(this.objectController, this.vShopStaffDTO, param);
            } else {
                Method method = c.getMethod(methodName, VShopStaffDTO.class);
                method.invoke(this.objectController, this.vShopStaffDTO);
            }
        } catch (NoSuchMethodException ex) {
            logger.error("", ex);
            reportError("", "common.error.happened", ex);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            reportError("", "common.error.happened", ex);
            logger.error("", ex);
        }


    }

    @Override
    public void doClearStaff(String methodName) {
        this.vShopStaffDTO = null;
        try {
            if (DataUtil.isNullOrEmpty(methodName)) {
                return;
            }
            Class<?> c = this.objectController.getClass();
            if (multiTag) {
                Method method = c.getMethod(methodName, String.class);
                method.invoke(this.objectController, methodName);
            } else {
                Method method = c.getMethod(methodName);
                method.invoke(this.objectController);
            }
        } catch (NoSuchMethodException ex) {
            logger.error("", ex);
            reportError("", "common.error.happened", ex);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            reportError("", "common.error.happened", ex);
            logger.error("", ex);
        }
    }

    //getter and setter
    public List<VShopStaffDTO> getLsShopDto() {
        return lsShopDto;
    }

    public void setLsShopDto(List<VShopStaffDTO> lsShopDto) {
        this.lsShopDto = lsShopDto;
    }

    public VShopStaffDTO getvShopStaffDTO() {
        return vShopStaffDTO;
    }

    public void setvShopStaffDTO(VShopStaffDTO vShopStaffDTO) {
        this.vShopStaffDTO = vShopStaffDTO;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    @Override
    public boolean disabledState() {
        return DataUtil.isNullObject(vShopStaffDTO) ? false : DataUtil.isNullOrEmpty(vShopStaffDTO.getOwnerId()) ? false : true;
    }

    @Override
    public Boolean getIsDisableEdit() {
        return isDisableEdit;
    }

    @Override
    public void setIsDisableEdit(Boolean isDisableEdit) {
        this.isDisableEdit = isDisableEdit;
    }

    @Override
    public void init(Object controller, StaffTagConfigDTO staffTagConfigDTO) throws Exception {
        this.objectController = controller;
        this.shopId = staffTagConfigDTO.getShopID();
        this.isDisableEdit = staffTagConfigDTO.isDisableEdit();
        this.channelTypes = staffTagConfigDTO.getChannelTypes();
        if (!DataUtil.isNullOrEmpty(staffTagConfigDTO.getStaffID())) {
            try {
                vShopStaffDTO = staffService.findStaffById(staffTagConfigDTO.getStaffID());
            } catch (Exception ex) {
                reportError("", "common.error.happen", ex);
                topPage();
            }
        } else {
            vShopStaffDTO = null;
        }
    }

    public boolean isMultiTag() {
        return multiTag;
    }

    public void setMultiTag(boolean multiTag) {
        this.multiTag = multiTag;
    }
}
