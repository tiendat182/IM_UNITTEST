package com.viettel.bccs.inventory.controller.list;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.VofficeRoleDTO;
import com.viettel.bccs.inventory.service.VofficeRoleService;
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
 * Created by tuydv1 on 11/10/2015.
 */
@Component
@Scope("view")
@ManagedBean
public class VofficeRoleController extends BaseController {


    @Autowired
    VofficeRoleService vofficeRoleSv;

    private VofficeRoleDTO forSearchVofficeRole;
    private VofficeRoleDTO addVofficeRole;
    private VofficeRoleDTO editVofficeRole;
    private List<VofficeRoleDTO> vofficeRoleList = Lists.newArrayList();
    private List<VofficeRoleDTO> vofficeRoleSelection = Lists.newArrayList();
    private VofficeRoleDTO vofficeRoleDelete;


    @PostConstruct
    @Secured("@")
    public void init() {
        try {
            create();
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

    public void create() throws LogicException, Exception {
        forSearchVofficeRole = new VofficeRoleDTO();
        forSearchVofficeRole.setStatus(Const.INVOICE_TYPE.STATUS_ACTIVE);
        vofficeRoleList = vofficeRoleSv.search(forSearchVofficeRole,true);
    }

    @Secured("@")
    public void searchVofficeRole() {
        try {
            vofficeRoleSelection = Lists.newArrayList();
            vofficeRoleList = vofficeRoleSv.search(forSearchVofficeRole,true);
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
    public void resetForm() {
        forSearchVofficeRole = new VofficeRoleDTO();
        forSearchVofficeRole.setStatus(Const.INVOICE_TYPE.STATUS_ACTIVE);
        topPage();
    }

    @Secured("@")
    public void preAddVofficeRole() throws Exception {
        addVofficeRole = new VofficeRoleDTO();
        addVofficeRole.setStatus(Const.INVOICE_TYPE.STATUS_ACTIVE);
    }

    @Secured("@")
    public void changeVofficeRoleAdd() {
        try {
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happened");
        }
    }


    @Secured("@")
    public void addVofficeRoleProcess(Boolean isClose) {
        try {
            vofficeRoleSv.createNewVofficeRole(addVofficeRole, BccsLoginSuccessHandler.getStaffDTO().getStaffCode());
            if (isClose) {
                reportSuccess("frmVofficeRole:msgVofficeRole", "mn.invoice.invoiceType.success.add");
            } else {
                reportSuccess("frmAddRoleVoffice:mesageAdd", "mn.invoice.invoiceType.success.add");
            }
            addVofficeRole = new VofficeRoleDTO();
            addVofficeRole.setStatus(Const.INVOICE_TYPE.STATUS_ACTIVE);
            create();
            topPage();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("pnlAddVofficeRole:mesageAdd", ex.getErrorCode(), ex.getDescription());
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("pnlAddVofficeRole:mesageAdd", "", ex);
            topPage();
        }

    }

    @Secured("@")
    public void prepareEdit(VofficeRoleDTO vofficeRole) {
        try {
            if (DataUtil.isNullObject(vofficeRole)) {
                reportError("", "", "mn.invoice.invoiceType.no.selected ");
            } else {
                editVofficeRole = DataUtil.cloneBean(vofficeRole);
            }
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happened!");
        }
    }

    @Secured("@")
    public void editVofficeRoleProcess() {
        try {
            BaseMessage result = vofficeRoleSv.update(editVofficeRole, BccsLoginSuccessHandler.getStaffDTO().getStaffCode());
            if (result.isSuccess()) {
                reportSuccess("frmVofficeRole:msgVofficeRole", "mn.invoice.invoiceType.success.edit");
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
            reportError("", "", ex);
            topPage();
        }

    }

    @Secured("@")
    public void preDeleteOneVofficeRole(VofficeRoleDTO dto) {
        if (DataUtil.isNullObject(dto)) {
            reportError("", "", "common.error.noselect.record");
            topPage();
        } else {
            vofficeRoleDelete = DataUtil.cloneBean(dto);
        }
    }

    @Secured("@")
    public void deleteOneVofficeRole() {
        try {
            if (DataUtil.isNullObject(vofficeRoleDelete)) {
                reportError("", "", "common.error.noselect.record");
            } else {
                List<Long> lstId = Lists.newArrayList();
                lstId.add(vofficeRoleDelete.getVofficeRoleId());

                BaseMessage result = vofficeRoleSv.delete(lstId, BccsLoginSuccessHandler.getStaffDTO().getStaffCode());
                if (result.isSuccess()) {
                    reportSuccess("frmVofficeRole:msgVofficeRole", "common.msg.success.delete");
                    create();
                } else {
                    reportError("frmVofficeRole:msgVofficeRole", "", "common.msg.unsuccess.delete");
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
    public void validateDeleteVofficeRole() {
        if (DataUtil.isNullOrEmpty(vofficeRoleSelection)) {
            reportError("", "", "common.error.noselect.record");
            topPage();
        }
    }

    @Secured("@")
    public void deleteVofficeRole() {
        try {
            if (DataUtil.isNullObject(vofficeRoleSelection)) {
                reportError("", "", "common.error.noselect.record");
            } else {
                List<Long> lstId = Lists.newArrayList();
                for (VofficeRoleDTO vofficeRoleDTO : vofficeRoleSelection) {
                    lstId.add(vofficeRoleDTO.getVofficeRoleId());
                }
                BaseMessage result = vofficeRoleSv.delete(lstId, BccsLoginSuccessHandler.getStaffDTO().getStaffCode());
                if (result.isSuccess()) {
                    reportSuccess("frmVofficeRole:msgVofficeRole", "common.msg.success.delete");
                    create();
                } else {
                    reportError("frmVofficeRole:msgVofficeRole", "", "common.msg.unsuccess.delete");
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

    public VofficeRoleDTO getForSearchVofficeRole() {
        return forSearchVofficeRole;
    }

    public void setForSearchVofficeRole(VofficeRoleDTO forSearchVofficeRole) {
        this.forSearchVofficeRole = forSearchVofficeRole;
    }

    public List<VofficeRoleDTO> getVofficeRoleList() {
        return vofficeRoleList;
    }

    public void setVofficeRoleList(List<VofficeRoleDTO> vofficeRoleList) {
        this.vofficeRoleList = vofficeRoleList;
    }

    public VofficeRoleDTO getAddVofficeRole() {
        return addVofficeRole;
    }

    public void setAddVofficeRole(VofficeRoleDTO addVofficeRole) {
        this.addVofficeRole = addVofficeRole;
    }

    public VofficeRoleDTO getEditVofficeRole() {
        return editVofficeRole;
    }

    public void setEditVofficeRole(VofficeRoleDTO editVofficeRole) {
        this.editVofficeRole = editVofficeRole;
    }

    public List<VofficeRoleDTO> getVofficeRoleSelection() {
        return vofficeRoleSelection;
    }

    public void setVofficeRoleSelection(List<VofficeRoleDTO> vofficeRoleSelection) {
        this.vofficeRoleSelection = vofficeRoleSelection;
    }
}
