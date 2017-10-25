package com.viettel.bccs.inventory.tag.impl;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.VShopStaffDTO;
import com.viettel.bccs.inventory.service.OptionSetValueService;
import com.viettel.bccs.inventory.service.ShopService;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.web.common.controller.BaseController;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
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
public class ShopInfoTag extends BaseController implements ShopInfoNameable {
    public static final Logger logger = Logger.getLogger(ShopInfoNameable.class);

    private VShopStaffDTO vShopStaffDTO = new VShopStaffDTO();
    private List<VShopStaffDTO> lsShopDto = Lists.newArrayList();
    private Object objectController;
    private String parentShopId;
    private boolean isCurrentAndChildShop;
    private boolean isDismissType = false;
    private Boolean isDisableEdit = false;
    private boolean isCurrentAndAllChildShop = false;
    private List<Long> listChannelTypeID;
    private boolean isAgent = false;
    private boolean multiTag = false;
    private String modeShop;//isdn
    private List<String> lstChanelType;
    private List<String> lstShopCode;
    private RequiredRoleMap requiredRoleMap;

    // NVDB/DB
    private boolean isCollaborator = false;
    private String staffOwnerId;
    private String ownerType;

    @Autowired
    private ShopService shopService;
    @Autowired
    private OptionSetValueService optionSetValueService;

    /**
     * hien thi danh sach goi cuoc theo thong tin nhap tren man hinh
     *
     * @param inputShop giá trị input của gói cước
     * @return List<Product> danh sach sản phẩm
     */
    @Override
    public List<VShopStaffDTO> doSelectShop(String inputShop) {
        try {
            String input = DataUtil.isNullOrEmpty(inputShop) ? "" : inputShop.trim();
            List<VShopStaffDTO> test = getLsVShopStaff(input, parentShopId, isCurrentAndChildShop);
            this.lsShopDto = test;
            return test;
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getKeyMsg(), ex);
            topPage();
        } catch (Exception e) {
            reportError("", "common.error.happened", e);
            logger.error(e.getMessage(), e);
            topPage();
        }
        return new ArrayList<VShopStaffDTO>();
    }

    /**
     * hàm init shop
     *
     * @author ThanhNT
     */
    @Override
    public void initShop(Object objectController, String parentShopId, boolean isCurrentAndChildShop) {
        this.objectController = objectController;
        this.isCurrentAndChildShop = isCurrentAndChildShop;
        this.parentShopId = parentShopId;
        this.vShopStaffDTO = null;
        this.ownerType = Const.OWNER_TYPE.SHOP;
        try {
            this.lsShopDto = getLsVShopStaff("", parentShopId, isCurrentAndChildShop);
        } catch (LogicException ex) {
            reportError("", ex.getKeyMsg(), ex);
            topPage();
        } catch (Exception e) {
            reportError("", "common.error.happened", e);
            topPage();
        }
    }

    /**
     * hàm init shop
     *
     * @author ThanhNT
     */
    @Override
    public void initShopTransfer(Object objectController, String parentShopId, String modeShop) {
        this.objectController = objectController;
        this.parentShopId = parentShopId;
        this.vShopStaffDTO = null;
        this.ownerType = Const.OWNER_TYPE.SHOP;
        this.modeShop = modeShop;
        try {
            this.lsShopDto = getLsVShopStaff("", parentShopId, false);
        } catch (LogicException ex) {
            reportError("", ex.getKeyMsg(), ex);
            topPage();
        } catch (Exception e) {
            reportError("", "common.error.happened", e);
            topPage();
        }
    }

    public void initShopBranchAndVTT(Object objectController, String parentShopId, String modeShop) {
        this.objectController = objectController;
        this.parentShopId = parentShopId;
        this.vShopStaffDTO = null;
        this.ownerType = Const.OWNER_TYPE.SHOP;
        this.modeShop = modeShop;
        try {
            this.lsShopDto = getLsVShopStaff("", parentShopId, false);
        } catch (LogicException ex) {
            reportError("", ex.getKeyMsg(), ex);
            topPage();
        } catch (Exception e) {
            reportError("", "common.error.happened", e);
            topPage();
        }
    }

    /**
     * hàm init shop
     *
     * @author ThanhNT
     */
    @Override
    public void initShopByListShopCode(Object objectController, String modeShop, List<String> lsShopCode) {
        this.objectController = objectController;
        this.vShopStaffDTO = null;
        this.ownerType = Const.OWNER_TYPE.SHOP;
        this.lstShopCode = lsShopCode;
        this.modeShop = modeShop;
        try {
            this.lsShopDto = getLsVShopStaff("", null, false);
        } catch (LogicException ex) {
            reportError("", ex.getKeyMsg(), ex);
            topPage();
        } catch (Exception e) {
            reportError("", "common.error.happened", e);
            topPage();
        }
    }

    @Override
    public void initAllChildNotCurrentShop(Object objectController, String parentShopId) {
        this.objectController = objectController;
        this.parentShopId = parentShopId;
        this.vShopStaffDTO = null;
        try {
            this.lsShopDto = getLsAllChildNotCurShop("", parentShopId);
        } catch (LogicException ex) {
            reportError("", ex.getKeyMsg(), ex);
            topPage();
        } catch (Exception e) {
            reportError("", "common.error.happened", e);
            topPage();
        }
    }

    @Override
    public void initShopAndAllChildWithChanelType(Object objectController, String currentShopId, List<Long> chanelTypes) {
        this.objectController = objectController;
        this.isCurrentAndChildShop = true;
        this.parentShopId = currentShopId;
        this.vShopStaffDTO = null;
        this.isDismissType = true;
        this.listChannelTypeID = chanelTypes;
        this.ownerType = Const.OWNER_TYPE.SHOP;
        try {
            this.lsShopDto = getLsVShopStaff("", parentShopId, isCurrentAndChildShop);
        } catch (LogicException ex) {
            reportError("", ex.getKeyMsg(), ex);
            topPage();
        } catch (Exception e) {
            reportError("", "common.error.happened", e);
            topPage();
        }
    }

    @Override
    public void initShopMangeIsdnTrans(Object objectController, String currentShopId, String chanelType, RequiredRoleMap requiredRoleMap, String mode) {
        this.objectController = objectController;
        this.isCurrentAndChildShop = true;
        this.parentShopId = currentShopId;
        this.vShopStaffDTO = null;
        this.isDismissType = true;
        this.modeShop = mode;
        this.requiredRoleMap = requiredRoleMap;
        //
        if (!DataUtil.isNullOrEmpty(chanelType)) {
            listChannelTypeID = Lists.newArrayList(Long.valueOf(chanelType));
        }
        try {
            this.lsShopDto = getLsVShopStaff("", parentShopId, isCurrentAndChildShop);
        } catch (LogicException ex) {
            reportError("", ex.getKeyMsg(), ex);
            topPage();
        } catch (Exception e) {
            reportError("", "common.error.happened", e);
            topPage();
        }
    }

    @Override
    public void initShopAndAllChild(Object objectController, String currentShopId, boolean isDismissType, String ownerType) {
        this.objectController = objectController;
        this.isCurrentAndChildShop = true;
        this.parentShopId = currentShopId;
        this.vShopStaffDTO = null;
        this.isCurrentAndAllChildShop = true;
        this.isDismissType = isDismissType;
        this.ownerType = ownerType;
        try {
            this.lsShopDto = getLsVShopStaff("", currentShopId, isCurrentAndChildShop);
        } catch (LogicException ex) {
            reportError("", ex.getKeyMsg(), ex);
            topPage();
        } catch (Exception e) {
            reportError("", "common.error.happened", e);
            topPage();
        }
    }

    @Override
    public void initShopNotChanelTypeIdCurrentAndChild(Object objectController, String modeShop, String parentShopId, String ownerType, Long chanelTypeId) {
        this.objectController = objectController;
        this.parentShopId = parentShopId;
        this.vShopStaffDTO = null;
        this.ownerType = ownerType;
        this.modeShop = modeShop;
        this.lstChanelType = Lists.newArrayList(DataUtil.safeToString(chanelTypeId));
        try {
            this.lsShopDto = DataUtil.defaultIfNull(shopService.getListNotChanelTypeId(parentShopId, DataUtil.safeToString(chanelTypeId), ""), new ArrayList<>());
        } catch (LogicException ex) {
            reportError("", ex.getKeyMsg(), ex);
            topPage();
        } catch (Exception e) {
            reportError("", "common.error.happened", e);
            topPage();
        }
    }

    private List<VShopStaffDTO> getLsVShopStaff(String input, String parentShopId, boolean isCurrentAndChildShop) throws LogicException, Exception {
        if (!DataUtil.isNullObject(modeShop)) {
            if (modeShop.equals(Const.MODE_SHOP.ISDN)) {
                return DataUtil.defaultIfNull(shopService.getListShopForIsdn(input, parentShopId, this.isCurrentAndChildShop, this.lstChanelType), new ArrayList<VShopStaffDTO>());
            } else if (modeShop.equals(Const.MODE_SHOP.ISDN_MNGT)) {
                return DataUtil.defaultIfNull(shopService.getListShopHierachy(BccsLoginSuccessHandler.getStaffDTO().getShopId() + "", "8", input), new ArrayList<VShopStaffDTO>());
            } else if (modeShop.equals(Const.MODE_SHOP.ISDN_DSTRT)) {
                if (DataUtil.isNullObject(requiredRoleMap) || !requiredRoleMap.hasRole(Const.PERMISION.ROLE_DISTRIBUTION)) {
                    return DataUtil.defaultIfNull(shopService.getListShopHierachy(BccsLoginSuccessHandler.getStaffDTO().getShopId() + "", !DataUtil.isNullObject(listChannelTypeID) ? listChannelTypeID.get(0).toString() : null, input), new ArrayList<VShopStaffDTO>());
                } else {
                    return DataUtil.defaultIfNull(shopService.getAllShop(input, null), new ArrayList<VShopStaffDTO>());
                }
            } else if (modeShop.equals(Const.MODE_SHOP.PAYNOTEANDREPORT)) {
                return DataUtil.defaultIfNull(shopService.getListShopForPay(input, parentShopId), new ArrayList<VShopStaffDTO>());
            } else if (modeShop.equals(Const.MODE_SHOP.PARTNER)) {
                this.lsShopDto = DataUtil.defaultIfNull(shopService.getListNotChanelTypeId(parentShopId, this.lstChanelType != null && this.lstChanelType.size() > 0 ? this.lstChanelType.get(0) : "4", input), new ArrayList<>());
                return lsShopDto;
            } else if (Const.MODE_SHOP.LIST_SHOP_TRANSFER.equals(modeShop)) {
                this.lsShopDto = DataUtil.defaultIfNull(shopService.getListShopTransfer(input, parentShopId), new ArrayList<>());
                return lsShopDto;
            } else if (Const.MODE_SHOP.LIST_SHOP_BRANCH_AND_VTT.equals(modeShop)) {
                this.lsShopDto = DataUtil.defaultIfNull(shopService.getListShopBranchAndVTT(input, parentShopId), new ArrayList<>());
                return lsShopDto;
            } else if (Const.MODE_SHOP.LIST_SHOP_KV.equals(modeShop)) {
                this.lsShopDto = DataUtil.defaultIfNull(shopService.getListShopKV(input), new ArrayList<>());
                return lsShopDto;
            } else if (Const.MODE_SHOP.LIST_SHOP_CODE.equals(modeShop)) {
                this.lsShopDto = DataUtil.defaultIfNull(shopService.getListShopByListShopCode(input, null, lstShopCode), new ArrayList<>());
                return lsShopDto;
            }
        }
        if (isCollaborator) {
            return DataUtil.defaultIfNull(shopService.getListCollaboratorAndPointOfSale(input, DataUtil.safeToLong(parentShopId),
                    DataUtil.isNullOrEmpty(listChannelTypeID) ? null : listChannelTypeID,
                    DataUtil.safeToLong(staffOwnerId)), new ArrayList<VShopStaffDTO>());
        }
        if (isAgent) {
            return DataUtil.defaultIfNull(shopService.getListAgent(input, DataUtil.safeToLong(parentShopId)), new ArrayList<VShopStaffDTO>());
        }

        if (isCurrentAndChildShop) {
            return DataUtil.defaultIfNull(shopService.getListShopAndAllChildShop(input, DataUtil.safeToLong(parentShopId), isDismissType, true, this.listChannelTypeID, ownerType), new ArrayList<VShopStaffDTO>());
        }
        return DataUtil.defaultIfNull(shopService.getListShop(BccsLoginSuccessHandler.getStaffDTO().getShopPath(), input, parentShopId, isCurrentAndChildShop, isDismissType), new ArrayList<VShopStaffDTO>());
    }

    /**
     * author HoangNT
     *
     * @param objectController
     */

    public void initAllShop(Object objectController) {
        this.objectController = objectController;
        this.vShopStaffDTO = null;
        try {
            this.lsShopDto = getLsAllVShop();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getKeyMsg(), ex);
            topPage();
        } catch (Exception e) {
            reportError("", "common.error.happened", e);
            logger.error(e.getMessage(), e);
            topPage();
        }
    }

    @Override
    public void initAgent(Object objectController, String parentShopId) {
        this.objectController = objectController;
        this.parentShopId = parentShopId;
        this.vShopStaffDTO = null;
        this.isAgent = true;
        this.multiTag = true;
        try {
            this.lsShopDto = DataUtil.defaultIfNull(shopService.getListAgent("", DataUtil.safeToLong(parentShopId)), new ArrayList<VShopStaffDTO>());
        } catch (LogicException ex) {
            reportError("", ex.getKeyMsg(), ex);
            topPage();
        } catch (Exception e) {
            reportError("", "common.error.happened", e);
            topPage();
        }
    }

    @Override
    public void initCollAndPointOfSale(Object objectController, String currentShopId, List<Long> lstChannelType, String staffOwnerId) {
        this.objectController = objectController;
        this.vShopStaffDTO = null;
        this.multiTag = true;
        this.parentShopId = currentShopId;
        this.staffOwnerId = staffOwnerId;
        this.isCollaborator = true;
        //
        if (!DataUtil.isNullOrEmpty(lstChannelType)) {
            listChannelTypeID = Lists.newArrayList(lstChannelType);
        }
        try {
            this.lsShopDto = DataUtil.defaultIfNull(shopService.getListCollaboratorAndPointOfSale("", DataUtil.safeToLong(parentShopId),
                    lstChannelType, DataUtil.safeToLong(staffOwnerId)), new ArrayList<VShopStaffDTO>());
        } catch (LogicException ex) {
            reportError("", ex.getKeyMsg(), ex);
            topPage();
        } catch (Exception e) {
            reportError("", "common.error.happened", e);
            topPage();
        }
    }

    private List<VShopStaffDTO> getLsAllVShop() throws LogicException, Exception {
        return DataUtil.defaultIfNull(shopService.getListAllShop(), new ArrayList<VShopStaffDTO>());
    }

    private List<VShopStaffDTO> getLsAllChildNotCurShop(String input, String parentShopId) throws LogicException, Exception {
        return DataUtil.defaultIfNull(shopService.getListShopAndAllChildShop(input, DataUtil.safeToLong(parentShopId), true, false, null, ownerType), new ArrayList<VShopStaffDTO>());
    }

    @Override
    public void loadShop(String shopId, boolean disableEdit) {
        try {
            vShopStaffDTO = shopService.getShopByOwnerId(DataUtil.isNullOrEmpty(shopId) ? "" : shopId.trim(), Const.OWNER_TYPE.SHOP);
            this.isDisableEdit = disableEdit;
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getKeyMsg(), ex);
            topPage();
        } catch (Exception e) {
            reportError("", "common.error.happened", e);
            logger.error(e.getMessage(), e);
            topPage();
        }
    }

    @Override
    public void loadStaff(String shopId, boolean disableEdit) {
        try {
            vShopStaffDTO = shopService.getShopByOwnerId(DataUtil.isNullOrEmpty(shopId) ? "" : shopId.trim(), Const.OWNER_TYPE.STAFF);
            this.isDisableEdit = disableEdit;
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getKeyMsg(), ex);
            topPage();
        } catch (Exception e) {
            reportError("", "common.error.happened", e);
            logger.error(e.getMessage(), e);
            topPage();
        }
    }

    @Override
    public void resetShop() {
        this.vShopStaffDTO = new VShopStaffDTO();
    }

    /**
     * get data from method of object
     *
     * @return methodName tên method set data
     * @author ThanhNT
     */
    @Override
    public void doChangeShop(String methodName) {
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
            reportError("", "msg.error.happened", ex);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            reportError("", "msg.error.happened", ex);
            logger.error("", ex);
        }
    }

    @Override
    public void doClearShop(String methodName) {
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

    @Override
    public void initShopForIsdn(Object objectController, String parentShopId, boolean isCurrentAndChildShop, List<String> lstChanelType) {
        this.modeShop = Const.MODE_SHOP.ISDN;
        try {
            this.objectController = objectController;
            this.parentShopId = parentShopId;
            this.lstChanelType = lstChanelType;
            this.isCurrentAndChildShop = isCurrentAndChildShop;
            this.lsShopDto = getLsVShopStaff("", parentShopId, isCurrentAndChildShop);
        } catch (LogicException ex) {
            reportError("", ex.getKeyMsg(), ex);
            topPage();
        } catch (Exception e) {
            reportError("", "common.error.happened", e);
            topPage();
        }
    }

    @Override
    public void loadStaffShop(Long staffId) {
        try {
            vShopStaffDTO = shopService.getShopByOwnerId(DataUtil.safeToString(staffId), Const.OWNER_TYPE.STAFF);
            this.isDisableEdit = true;
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getKeyMsg(), ex);
            topPage();
        } catch (Exception e) {
            reportError("", "common.error.happened", e);
            logger.error(e.getMessage(), e);
            topPage();
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

    public String getParentShopId() {
        return parentShopId;
    }

    public void setParentShopId(String parentShopId) {
        this.parentShopId = parentShopId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
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
    public void setChannelTypeId(Long channelTypeId) {
        if (channelTypeId != null) {
            listChannelTypeID = Lists.newArrayList(channelTypeId);
        }
    }

    public boolean isAgent() {
        return isAgent;
    }

    public void setIsAgent(boolean isAgent) {
        this.isAgent = isAgent;
    }

    public boolean isMultiTag() {
        return multiTag;
    }

    public void setMultiTag(boolean multiTag) {
        this.multiTag = multiTag;
    }

    public boolean isCollaborator() {
        return isCollaborator;
    }

    public void setIsCollaborator(boolean isCollaborator) {
        this.isCollaborator = isCollaborator;
    }

    public List<String> getLstShopCode() {
        return lstShopCode;
    }

    public void setLstShopCode(List<String> lstShopCode) {
        this.lstShopCode = lstShopCode;
    }
}
