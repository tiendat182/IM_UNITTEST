package com.viettel.bccs.inventory.controller.list;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.web.common.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by tuydv1 on 11/10/2015.
 */
@Component
@Scope("view")
@ManagedBean
public class SignFlowController extends BaseController {


    @Autowired
    SignFlowService signFlowSv;
    @Autowired
    SignFlowDetailService signFlowDetailService;
    @Autowired
    private ShopInfoNameable shopInfoExportTag;
    @Autowired
    private ShopService shopService;

    @Autowired
    private ShopInfoNameable shopInfoExportTagAdd;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    VofficeRoleService vofficeRoleService;

    private SignFlowDTO forSearchSignFlow;
    private SignFlowDTO addSignFlow;
    private List<SignFlowDetailDTO> listAddSignFlowDetail = Lists.newArrayList();
    private List<SignFlowDTO> signFlowList = Lists.newArrayList();
    private List<SignFlowDTO> signFlowSelection = Lists.newArrayList();
    private SignFlowDTO signFlowDelete;
    private StaffDTO staffDTO;
    private int limitAutoComplete;
    private List<VofficeRoleDTO> listVofficeRole = Lists.newArrayList();
    private SignFlowDetailDTO addSignFlowDetailDTO;
    private static final Pattern PATTERN = Pattern.compile(Const.REGEX.CODE_REGEX);


    @PostConstruct
    @Secured("@")
    public void init() {
        try {
            if (staffDTO == null) {
                staffDTO = BccsLoginSuccessHandler.getStaffDTO();
            }
            create();
            listVofficeRole = vofficeRoleService.search(new VofficeRoleDTO(), false);
            shopInfoExportTag.initShopAndAllChild(this, DataUtil.safeToString(staffDTO.getShopId()), true, Const.OWNER_TYPE.SHOP);
            shopInfoExportTagAdd.initShopAndAllChild(this, DataUtil.safeToString(staffDTO.getShopId()), true, Const.OWNER_TYPE.SHOP);
//            shopInfoExportTag.initShop(this, DataUtil.safeToString(staffDTO.getShopId()), true);
//            shopInfoExportTagAdd.initShop(this, DataUtil.safeToString(staffDTO.getShopId()), true);
            limitAutoComplete = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_AUTOCOMPLETE));
            executeCommand("updateControls();");
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
            topPage();
        } catch (Exception ex) {
            reportError("", "", ex);
            topPage();
        }
    }

    public void test() {
    }

    public void create() throws LogicException, Exception {
        forSearchSignFlow = new SignFlowDTO();
        forSearchSignFlow.setCurrentShopId(BccsLoginSuccessHandler.getStaffDTO().getShopId());
        forSearchSignFlow.setStatus(Const.INVOICE_TYPE.STATUS_ACTIVE);
        signFlowList = signFlowSv.search(forSearchSignFlow);
    }

    @Secured("@")
    public void exportShop(VShopStaffDTO vShopStaffDTO) {
        shopInfoExportTag.setvShopStaffDTO(vShopStaffDTO);
        if (!DataUtil.isNullObject(vShopStaffDTO)) {
            forSearchSignFlow.setShopId(Long.parseLong(vShopStaffDTO.getOwnerId()));
        } else {
            forSearchSignFlow.setShopId(null);
        }
    }

    @Secured("@")
    public void clearExportShop() {
        shopInfoExportTag.setvShopStaffDTO(null);
        forSearchSignFlow.setShopId(null);
    }

    @Secured("@")
    public void exportShopAdd(VShopStaffDTO vShopStaffDTO) {
        shopInfoExportTagAdd.setvShopStaffDTO(vShopStaffDTO);
        if (!DataUtil.isNullObject(vShopStaffDTO)) {
            addSignFlow.setShopId(Long.parseLong(vShopStaffDTO.getOwnerId()));
        } else {
            addSignFlow.setShopId(null);
        }
    }

    @Secured("@")
    public void clearExportShopAdd() {
        shopInfoExportTagAdd.setvShopStaffDTO(null);
        addSignFlow.setShopId(null);
    }

    @Secured("@")
    public void searchSignFlow() {
        try {
            signFlowSelection = Lists.newArrayList();
            signFlowList = signFlowSv.search(forSearchSignFlow);
            topPage();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
            topPage();
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happened");
            topPage();
        }

    }

    @Secured("@")
    public String getTitleDlgSignFlow() {
        if (addSignFlow != null && addSignFlow.getSignFlowId() != null) {
            return getText("signFlow.edit.signFlow");
        }
        return getText("signFlow.add.signFlow");
    }

    @Secured("@")
    public String getStrStatus(String str) {
        if (!DataUtil.isNullObject(str) && str.equals(Const.STATUS_ACTIVE)) {
            return getText("common.type.status.active");
        }
        return getText("common.type.status.inactive");
    }

    @Secured("@")
    public void resetForm() {
        forSearchSignFlow = new SignFlowDTO();
        forSearchSignFlow.setCurrentShopId(BccsLoginSuccessHandler.getStaffDTO().getShopId());
        forSearchSignFlow.setStatus(Const.INVOICE_TYPE.STATUS_ACTIVE);
        clearExportShop();
        topPage();
    }

    @Secured("@")
    public void preAddSignFlow() throws Exception {
        addSignFlow = new SignFlowDTO();
        clearExportShopAdd();
        addSignFlow.setStatus(Const.INVOICE_TYPE.STATUS_ACTIVE);
        listAddSignFlowDetail = Lists.newArrayList();
        addSignFlowDetailDTO = new SignFlowDetailDTO();
        addNewItem();
        addNewItem();
    }


    @Secured("@")
    public void addSignFlowProcess(Boolean isClose) {
        try {
            signFlowSv.createNewSignFlow(addSignFlow, listAddSignFlowDetail, staffDTO.getStaffCode());
            if (isClose) {
                reportSuccess("frmSignFlow:msgSignFlow", "mn.invoice.invoiceType.success.add");
            } else {
                reportSuccess("frmAddRoleVoffice:mesageAdd", "mn.invoice.invoiceType.success.add");
            }
            preAddSignFlow();
            create();
            topPage();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("pnlAddSignFlow:mesageAdd", ex.getErrorCode(), ex.getDescription());
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("pnlAddSignFlow:mesageAdd", ex.getMessage(), ex);
            topPage();
        }

    }

    @Secured("@")
    public void doConfirmAdd() {
        try {
            if (DataUtil.isNullOrEmpty(listAddSignFlowDetail)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "signFlow.validate.listSignFlowDetail");
            } else {
                int i = 0;
                int k;
                boolean checkVofficeRole = false;
                boolean checkEmail = false;
                boolean checkSignOrder = false;
                boolean duplicateVofficeRole = false;
                boolean duplicateEmail = false;
                boolean duplicateSignOrder = false;
                boolean checkExits = true;
                boolean checkEmailFormat = false;
                for (SignFlowDetailDTO dtoDetail : listAddSignFlowDetail) {
                    if (Const.STATUS_ACTIVE.equals(dtoDetail.getStatus())) {
                        checkExits = false;
                    }
                    if (DataUtil.isNullObject(dtoDetail.getVofficeRoleId())) {
                        focusElementByRawCSSSlector("." + "voficeRole" + i);
                        checkVofficeRole = true;
                    } else {
                        k = 0;
                        for (SignFlowDetailDTO signFlowDetailDTO : listAddSignFlowDetail) {
                            if (i != k && dtoDetail.getVofficeRoleId().equals(signFlowDetailDTO.getVofficeRoleId())
                                    && dtoDetail.getStatus().trim().equals(signFlowDetailDTO.getStatus().trim())) {
                                focusElementByRawCSSSlector("." + "voficeRole" + i);
                                duplicateVofficeRole = true;
                            }
                            k++;
                        }
                    }
                    if (DataUtil.isNullObject(dtoDetail.getEmail().trim())) {
                        focusElementByRawCSSSlector("." + "email" + i);
                        checkEmail = true;
                    } else if (!dtoDetail.getEmail().trim().endsWith(Const.FORMAT_EMAIL)) {
                        focusElementByRawCSSSlector("." + "email" + i);
                        checkEmailFormat = true;
                    } else if (!PATTERN.matcher(dtoDetail.getEmail().trim().substring(0,
                            dtoDetail.getEmail().trim().length() - Const.FORMAT_EMAIL.length())).matches()) {
                        focusElementByRawCSSSlector("." + "email" + i);
                        checkEmailFormat = true;
                    }
                    if (DataUtil.isNullObject(dtoDetail.getSignOrder())) {
                        focusElementByRawCSSSlector("." + "signOrder" + i);
                        checkSignOrder = true;
                    } else if (DataUtil.safeToLong(dtoDetail.getSignOrder()) <= 0) {
                        focusElementByRawCSSSlector("." + "signOrder" + i);
                        checkSignOrder = true;
                    } else {
                        k = 0;
                        for (SignFlowDetailDTO signFlowDetailDTO : listAddSignFlowDetail) {
                            if (i != k && dtoDetail.getSignOrder().equals(signFlowDetailDTO.getSignOrder())
                                    && dtoDetail.getStatus().trim().equals(signFlowDetailDTO.getStatus().trim())) {
                                focusElementByRawCSSSlector("." + "signOrder" + i);
                                duplicateSignOrder = true;
                            }
                            k++;
                        }
                    }
                    i++;
                }
                boolean isError = false;
                if (checkExits) {
                    isError = true;
                    reportError("", ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "signFlow.validate.listSignFlowDetail");
                }
                if (checkVofficeRole) {
                    isError = true;
                    reportError("", ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "signRoleVoffice.require.roleName");
                }
                if (checkEmail) {
                    isError = true;
                    reportError("", ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "signFlow.require.email");
                }
                if (checkEmailFormat) {
                    isError = true;
                    reportError("", ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "signFlow.validate.email");
                }
                if (checkSignOrder) {
                    isError = true;
                    reportError("", ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "signFlow.require.signOrder");
                }
                if (duplicateEmail) {
                    isError = true;
                    reportError("", ErrorCode.ERROR_USER.ERROR_USER_DUPLICATE_OBJECT, "signFlow.duplicate.email");
                }
                if (duplicateSignOrder) {
                    isError = true;
                    reportError("", ErrorCode.ERROR_USER.ERROR_USER_DUPLICATE_OBJECT, "signFlow.duplicate.signOrder");
                }
                if (duplicateVofficeRole) {
                    isError = true;
                    reportError("", ErrorCode.ERROR_USER.ERROR_USER_DUPLICATE_OBJECT, "signFlow.duplicate.roleName");
                }
                if (isError) {
                    topPage();
                }
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getKeyMsg());
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    @Secured("@")
    public void prepareEdit(SignFlowDTO signFlow) {
        try {
            addSignFlow = new SignFlowDTO();
            listAddSignFlowDetail.clear();
            if (DataUtil.isNullObject(signFlow)) {
                reportError("", "", "mn.invoice.invoiceType.no.selected");
                topPage();
            } else {
                addSignFlow = DataUtil.cloneBean(signFlow);
                shopInfoExportTagAdd.setvShopStaffDTO(shopService.getShopByShopId(addSignFlow.getShopId()));
                listAddSignFlowDetail = signFlowDetailService.findBySignFlowId(addSignFlow.getSignFlowId());

            }
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happened");
        }
    }

    @Secured("@")
    public void editSignFlowProcess() {
        try {
            BaseMessage result = signFlowSv.update(addSignFlow, listAddSignFlowDetail, staffDTO.getStaffCode());
            if (result.isSuccess()) {
                reportSuccess("frmSignFlow:msgSignFlow", "mn.invoice.invoiceType.success.edit");
                create();
            } else {
                reportError("", "", "mn.invoice.invoiceType.unSuccess.edit");
            }
            topPage();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getMessage(), ex);
            topPage();
        }

    }

    @Secured("@")
    public void preDeleteOneSignFlow(SignFlowDTO dto) {
        if (DataUtil.isNullObject(dto)) {
            reportError("", "", "common.error.noselect.record");
            topPage();
        } else {
            signFlowDelete = DataUtil.cloneBean(dto);
        }
    }

    @Secured("@")
    public void deleteOneSignFlow() {
        try {
            if (DataUtil.isNullObject(signFlowDelete)) {
                reportError("", "", "common.error.noselect.record");
            } else {
                List<Long> lstId = Lists.newArrayList();
                lstId.add(signFlowDelete.getSignFlowId());

                BaseMessage result = signFlowSv.delete(lstId, BccsLoginSuccessHandler.getStaffDTO().getStaffCode());
                if (result.isSuccess()) {
                    reportSuccess("frmSignFlow:msgSignFlow", "common.msg.success.delete");
                    create();
                } else {
                    reportError("frmSignFlow:msgSignFlow", "", "common.msg.unsuccess.delete");
                }
            }
            topPage();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", ex);
            topPage();
        }
    }


    @Secured("@")
    public void validateDeleteSignFlow() {
        if (DataUtil.isNullOrEmpty(signFlowSelection)) {
            reportError("", "", "common.error.noselect.record");
            topPage();
        }
    }

    @Secured("@")
    public void deleteSignFlow() {
        try {
            if (DataUtil.isNullObject(signFlowSelection)) {
                reportError("", "", "common.error.noselect.record");
            } else {
                List<Long> lstId = Lists.newArrayList();
                for (SignFlowDTO signFlowDTO : signFlowSelection) {
                    lstId.add(signFlowDTO.getSignFlowId());
                }
                BaseMessage result = signFlowSv.delete(lstId, BccsLoginSuccessHandler.getStaffDTO().getStaffCode());
                if (result.isSuccess()) {
                    reportSuccess("frmSignFlow:msgSignFlow", "common.msg.success.delete");
                    create();
                } else {
                    reportError("frmSignFlow:msgSignFlow", "", "common.msg.unsuccess.delete");
                }
            }
            topPage();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", ex);
            topPage();
        }
    }

    //////////////////////////////_____ DETAIL _____//////////////////////////////////////////

    public void doAddItem() {
        try {
            if (listAddSignFlowDetail != null && listAddSignFlowDetail.size() == 100) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "signFlow.validate.limit.detail");
            }
            addNewItem();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    @Secured("@")
    public void doRemoveItem(SignFlowDetailDTO item) {
        try {
            if (listAddSignFlowDetail.contains(item)) {
                if (DataUtil.isNullOrZero(item.getSignFlowDetailId())) {
                    listAddSignFlowDetail.remove(item);
                } else {
                    item.setStatus(Const.STATUS.NO_ACTIVE);
                }
            }
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
        }
    }

    private void addNewItem() throws LogicException, Exception {
        SignFlowDetailDTO dto = new SignFlowDetailDTO();
        dto.setStatus(Const.STATUS_ACTIVE);
        listAddSignFlowDetail.add(dto);
    }

    public void doChangeDetail(int index) {
        try {
            SignFlowDetailDTO dto = listAddSignFlowDetail.get(index);
            int i = 0;
            for (SignFlowDetailDTO dtoTmp : listAddSignFlowDetail) {
                if (index != i && !DataUtil.isNullObject(dto) && !DataUtil.isNullObject(dto.getVofficeRoleId())
                        && !DataUtil.isNullObject(dtoTmp) && dto.getVofficeRoleId().equals(dtoTmp.getVofficeRoleId())) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "signFlow.validate.VofficeRole.detail");
                }
                i++;
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }


    public SignFlowService getSignFlowSv() {
        return signFlowSv;
    }

    public void setSignFlowSv(SignFlowService signFlowSv) {
        this.signFlowSv = signFlowSv;
    }

    public SignFlowDTO getForSearchSignFlow() {
        return forSearchSignFlow;
    }

    public void setForSearchSignFlow(SignFlowDTO forSearchSignFlow) {
        this.forSearchSignFlow = forSearchSignFlow;
    }

    public SignFlowDTO getAddSignFlow() {
        return addSignFlow;
    }

    public void setAddSignFlow(SignFlowDTO addSignFlow) {
        this.addSignFlow = addSignFlow;
    }

    public List<SignFlowDTO> getSignFlowList() {
        return signFlowList;
    }

    public void setSignFlowList(List<SignFlowDTO> signFlowList) {
        this.signFlowList = signFlowList;
    }

    public List<SignFlowDTO> getSignFlowSelection() {
        return signFlowSelection;
    }

    public void setSignFlowSelection(List<SignFlowDTO> signFlowSelection) {
        this.signFlowSelection = signFlowSelection;
    }

    public SignFlowDTO getSignFlowDelete() {
        return signFlowDelete;
    }

    public void setSignFlowDelete(SignFlowDTO signFlowDelete) {
        this.signFlowDelete = signFlowDelete;
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

    public List<SignFlowDetailDTO> getListAddSignFlowDetail() {
        return listAddSignFlowDetail;
    }

    public void setListAddSignFlowDetail(List<SignFlowDetailDTO> listAddSignFlowDetail) {
        this.listAddSignFlowDetail = listAddSignFlowDetail;
    }

    public List<VofficeRoleDTO> getListVofficeRole() {
        return listVofficeRole;
    }

    public void setListVofficeRole(List<VofficeRoleDTO> listVofficeRole) {
        this.listVofficeRole = listVofficeRole;
    }

    public SignFlowDetailDTO getAddSignFlowDetailDTO() {
        return addSignFlowDetailDTO;
    }

    public void setAddSignFlowDetailDTO(SignFlowDetailDTO addSignFlowDetailDTO) {
        this.addSignFlowDetailDTO = addSignFlowDetailDTO;
    }

    public ShopInfoNameable getShopInfoExportTagAdd() {
        return shopInfoExportTagAdd;
    }

    public void setShopInfoExportTagAdd(ShopInfoNameable shopInfoExportTagAdd) {
        this.shopInfoExportTagAdd = shopInfoExportTagAdd;
    }
}
