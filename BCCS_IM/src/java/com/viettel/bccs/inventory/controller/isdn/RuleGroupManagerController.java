package com.viettel.bccs.inventory.controller.isdn;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.GroupFilterRuleDTO;
import com.viettel.bccs.inventory.service.GroupFilterRuleService;
import com.viettel.bccs.inventory.service.NumberFilterRuleService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.BundleUtil;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.web.common.annotation.Security;
import com.viettel.web.common.controller.BaseController;
import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by sinhhv on 12/3/2015.
 */
@Component
@Scope("view")
@ManagedBean(name = "ruleGroupManagerController")
public class RuleGroupManagerController extends BaseController {

    List<GroupFilterRuleDTO> listRule = Lists.newArrayList();
    List<GroupFilterRuleDTO> listGroups = Lists.newArrayList();
    @Autowired
    private GroupFilterRuleService groupFilterRuleService;
    private GroupFilterRuleDTO currentGroup;
    private GroupFilterRuleDTO currentGroupDelete;
    private int currentIndex = -1;
    private String name;
    private String headerDialog;
    private List<GroupFilterRuleDTO> listGroupRuleSelection = Lists.newArrayList();
    @Autowired
    private NumberFilterRuleService numberFilterRuleService;

    @PostConstruct
    public void init() {
        try {
            currentGroup = new GroupFilterRuleDTO();
            listRule = groupFilterRuleService.getListNumberFiler(null, true);
            Collections.sort(listRule);
            actionSearch();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("formRuleGroup:messages", "", "common.error.happen");
        }
    }

    @Security
    public void actionSearch() {
        try {
            listGroups = groupFilterRuleService.search(currentGroup, true);
            if (!DataUtil.isNullOrEmpty(listGroups)) {
                Collections.sort(listGroups, new Comparator<GroupFilterRuleDTO>() {
                    public int compare(GroupFilterRuleDTO o1, GroupFilterRuleDTO o2) {
                        return o2.getLastUpdateTime().compareTo(o1.getLastUpdateTime());
                    }
                });
//                reportSuccess("formRuleGroup:messages", "mn.invoice.invoiceType.searchData", DataUtil.isNullOrEmpty(listGroups) ? 0 : listGroups.size());
            } else {
//                reportSuccess("formRuleGroup:messages", "mn.invoice.invoiceType.searchData", 0);
            }

        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("formRuleGroup:messages", "", ex.getKeyMsg());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("formRuleGroup:messages", "", "common.error.happen");
        }
    }

    @Security
    public void actionSave(boolean close) {
        try {
            currentGroup.setStatus(Const.STATUS.ACTIVE);
            if (DataUtil.isNullObject(currentGroup.getGroupFilterRuleId())) {
                currentGroup.setCreateUser(BccsLoginSuccessHandler.getStaffDTO().getStaffCode());
                currentGroup.setCreateDate(null);
            }
            currentGroup.setLastUpdateTime(null);
            currentGroup.setLastUpdateUser(BccsLoginSuccessHandler.getStaffDTO().getStaffCode());
            currentGroup.setTelecomServiceId(null);
            currentGroup.setTelecomServiceName(null);
            GroupFilterRuleDTO dto = groupFilterRuleService.save(currentGroup);
            if (currentGroup.getGroupFilterRuleId() == null) {
                if (listGroups == null) {
                    listGroups = Lists.newArrayList();
                }
                listGroups.add(0, dto);
                if (close) {
                    reportSuccess("formRuleGroup:messages", "mn.invoice.invoiceType.success.add");
                } else {
                    reportSuccess("formRuleGroup:dlgRuleGroupPanel:messagesDlg", "mn.invoice.invoiceType.success.add");
                    RequestContext.getCurrentInstance().execute("setTimeout(function() {$('.txtRuleGroupCode').focus();}, 350);");
                }
            } else {
                listGroups.set(currentIndex, dto);
                reportSuccess("formRuleGroup:messages", "mn.invoice.invoiceType.success.edit");
            }
            currentGroup = new GroupFilterRuleDTO();
            actionSearch();
            topPage();
            RequestContext.getCurrentInstance().update("formRuleGroup:pnInfoExportOrder");
        } catch (LogicException ex) {
            if (DataUtil.safeEqual(ex.getKeyMsg(), "ws.group.name.existed")) {
                focusElementError("txtRuleGroupName");
                RequestContext.getCurrentInstance().execute("setTimeout(function() {$('.txtRuleGroupName').focus();}, 350);");
            } else if (DataUtil.safeEqual(ex.getKeyMsg(), "ws.rule.notexist")) {
                focusElementError("cbxRule");
            } else if (DataUtil.safeEqual(ex.getKeyMsg(), "ws.group.code.existed")) {
                focusElementError("txtRuleGroupCode");
            }
            logger.error(ex.getMessage(), ex);
            reportError("formRuleGroup:dlgRuleGroupPanel:messagesDlg", "", ex.getKeyMsg());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("formRuleGroup:dlgRuleGroupPanel:messagesDlg", "", "common.error.happen");
        }
    }

    @Security
    public void initObject() {
        currentGroup = new GroupFilterRuleDTO();
    }

    @Security
    public void actionEdit(GroupFilterRuleDTO dto) {
        currentIndex = listGroups.indexOf(dto);
        currentGroup = DataUtil.cloneBean(dto);
        headerDialog = BundleUtil.getText("common.edit.record");
        RequestContext.getCurrentInstance().update("formRuleGroup:dlgRuleGroup");
    }

    public void onConfirmDelete(GroupFilterRuleDTO dto) {
        this.currentGroupDelete = dto;
    }

    @Security
    public void actionDelete() {
        try {
            if (numberFilterRuleService.checkBeforeDelete(currentGroupDelete.getGroupFilterRuleId())) {
                currentGroupDelete.setStatus(Const.STATUS.NO_ACTIVE);
                currentGroupDelete.setLastUpdateTime(null);
                currentGroupDelete.setLastUpdateUser(BccsLoginSuccessHandler.getStaffDTO().getStaffCode());
                groupFilterRuleService.save(currentGroupDelete);
                listGroups.remove(currentGroupDelete);
                reportSuccess("formRuleGroup:messages", "common.msg.success.delete");
            } else {
                reportError("formRuleGroup:messages", "", "group.rule.has.child", currentGroupDelete.getName());
            }
            topPage();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("formRuleGroup:messages", "", ex.getKeyMsg());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("formRuleGroup:messages", "", "common.error.happen");
        }
    }

    @Security
    public String getNameRule(Long id) {
        try {
            for (GroupFilterRuleDTO dto : listRule) {
                if (dto.getGroupFilterRuleId().equals(id)) {
                    return dto.getName();
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("formRuleGroup:messages", "", "common.error.happen");
        }
        return "";
    }

    @Secured("@")
    public void validateDeleteRuleList() {
        if (DataUtil.isNullOrEmpty(listGroupRuleSelection)) {
            reportError("formRuleGroup:messages", "", "rule.group.must.select");
        }
    }

    public void deleteRuleList() {
        try {
            boolean check = true;
            String nameRuleError = "";
            int count = 0;
            for (GroupFilterRuleDTO dto : listGroupRuleSelection) {
                if (!numberFilterRuleService.checkBeforeDelete(dto.getGroupFilterRuleId())) {
                    count++;
                    if (count == 5) {
                        break;
                    }
                    check = false;
                    nameRuleError += dto.getName() + "; ";
                }
            }
            if (check) {
                for (GroupFilterRuleDTO dto : listGroupRuleSelection) {
                    dto.setLastUpdateTime(null);
                    dto.setLastUpdateUser(BccsLoginSuccessHandler.getStaffDTO().getStaffCode());
                    dto.setStatus(Const.STATUS.NO_ACTIVE);
                    groupFilterRuleService.save(dto);
                    listGroups.remove(dto);
                }
                reportSuccess("formRuleGroup:messages", "mn.isdn.rule.delete.success");
            } else {
                reportError("formRuleGroup:messages", "", "group.rule.has.child", nameRuleError.substring(0, nameRuleError.length() - 2));
            }
            topPage();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("formRuleGroup:messages", ex.getErrorCode(), ex.getKeyMsg());
        } catch (Exception ex) {
            logger.error(ex);
            reportError("formRuleGroup:messages", "", "common.error.happen");
        }
    }

    public void validDataBeforeCreateRange() {
        if (DataUtil.isNullObject(currentGroup.getGroupFilterRuleCode())) {
            focusElementError("txtRuleGroupCode");
            reportError("formRuleGroup:dlgRuleGroupPanel:messagesDlg", "", "ws.group.code.not.null");
        }
        if (currentGroup.getGroupFilterRuleCode().length() > 20) {
            focusElementError("txtRuleGroupCode");
            reportError("formRuleGroup:dlgRuleGroupPanel:messagesDlg", "", "ws.group.code.not.null");
        }

        if (!currentGroup.getGroupFilterRuleCode().matches("[a-zA-Z0-9_ ]*")) {
            focusElementError("txtRuleGroupCode");
            reportError("formRuleGroup:dlgRuleGroupPanel:messagesDlg", "", "component.invalid", "rule.group.code");
        }

        if (DataUtil.isNullObject(currentGroup.getName())) {
            focusElementError("txtRuleGroupName");
            reportError("formRuleGroup:dlgRuleGroupPanel:messagesDlg", "", "ws.group.name.not.null");
        }
        if (currentGroup.getName().length() > 100) {
            focusElementError("txtRuleGroupName");
            reportError("formRuleGroup:dlgRuleGroupPanel:messagesDlg", "", "ws.group.name.over.maxlength");
        }

        if (DataUtil.isNullObject(currentGroup.getParentId())) {
            focusElementError("cbxRule");
            reportError("formRuleGroup:dlgRuleGroupPanel:messagesDlg", "", "ws.rule.not.null");
        }
        if (DataUtil.isNullObject(currentGroup.getGroupType())) {
            focusElementError("cbxRuleGroupType");
            reportError("formRuleGroup:dlgRuleGroupPanel:messagesDlg", "", "ws.group.type.not.null");
        }
        if (!DataUtil.isNullObject(currentGroup.getNotes()) && currentGroup.getNotes().length() > 500) {
            focusElementError("txtDescription");
            reportError("formRuleGroup:dlgRuleGroupPanel:messagesDlg", "", "ws.notes.over.maxlength");
        }
    }

    public List<GroupFilterRuleDTO> getListRule() {
        return listRule;
    }

    public void setListRule(List<GroupFilterRuleDTO> listRule) {
        this.listRule = listRule;
    }

    public GroupFilterRuleDTO getCurrentGroup() {
        return currentGroup;
    }

    public void setCurrentGroup(GroupFilterRuleDTO currentGroup) {
        this.currentGroup = currentGroup;
    }

    public List<GroupFilterRuleDTO> getListGroups() {
        return listGroups;
    }

    public void setListGroups(List<GroupFilterRuleDTO> listGroups) {
        this.listGroups = listGroups;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeaderDialog() {
        return headerDialog;
    }

    public List<GroupFilterRuleDTO> getListGroupRuleSelection() {
        return listGroupRuleSelection;
    }

    public void setListGroupRuleSelection(List<GroupFilterRuleDTO> listGroupRuleSelection) {
        this.listGroupRuleSelection = listGroupRuleSelection;
    }

    public void setHeaderDialog(String headerDialog) {
        this.headerDialog = headerDialog;
    }
}
