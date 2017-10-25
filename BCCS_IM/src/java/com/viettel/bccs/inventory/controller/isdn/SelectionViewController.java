package com.viettel.bccs.inventory.controller.isdn;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.Document;
import com.viettel.bccs.inventory.common.model.FileExportBean;
import com.viettel.bccs.inventory.common.util.FileUtil;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.*;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.BundleUtil;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.web.common.annotation.Security;
import com.viettel.web.common.controller.BaseController;
import org.apache.poi.ss.usermodel.Workbook;
import org.primefaces.context.RequestContext;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.model.CheckboxTreeNode;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

@Component
@ManagedBean
@Scope("view")
public class SelectionViewController extends BaseController implements Serializable {
    private static final long serialVersionUID = 1L;
    private TreeNode root;
    private TreeNode[] selectedNodes;
    private String typeRule;
    private boolean renderedRule;
    private List<ShopDTO> lstShop;
    private List<OptionSetValueDTO> listService = Lists.newArrayList();
    String startRange;
    String endRange;
    Long shopId;
    String[] conditionFilter;
    List<Object[]> listResult = Lists.newArrayList();
    List<Object[]> listSelectedResult = Lists.newArrayList();
    List<FilterSpecialNumberDTO> listSpecialNumber = Lists.newArrayList();
    List<FilterSpecialNumberDTO> listSelectedSpecialNumber = Lists.newArrayList();
    Long currentRuleView;
    boolean refilter;
    NumberFilterRuleDTO resultFilterRule = new NumberFilterRuleDTO();

    private String ruleNameSelected;

    @Autowired
    GroupFilterRuleService groupFilterRuleService;
    @Autowired
    StockNumberService stockNumberService;
    @Autowired
    FilterSpecialNumberService filterSpecialNumberService;
    @Autowired
    ShopService shopService;
    @Autowired
    NumberFilterRuleService numberFilterRuleService;
    @Autowired
    OptionSetValueService optionSetValueService;
    @Autowired
    NumberActionService numberActionService;

    @PostConstruct
    public void init() {
        try {
            root = null;
            typeRule = null;
            conditionFilter = new String[20];
            conditionFilter[0] = "1";
            conditionFilter[1] = "2";
            conditionFilter[2] = "3";
            listService = optionSetValueService.getByOptionSetCode(Const.TELCO_FOR_NUMBER);
            Collections.sort(listService, new Comparator<OptionSetValueDTO>() {
                public int compare(OptionSetValueDTO o1, OptionSetValueDTO o2) {
                    return Long.valueOf(o1.getValue()).compareTo(Long.valueOf(o2.getValue()));
                }
            });
            ShopDTO shop = new ShopDTO();
            shop.setParentShopId(DataUtil.safeToLong(Const.VT_SHOP_ID));
            shop.setChannelTypeId(Const.FUNC_CHANEL_TYPE_ID);
            renderedRule = false;
            lstShop = shopService.getListShopByObject(shop);
            Collections.sort(lstShop, new Comparator<ShopDTO>() {
                @Override
                public int compare(ShopDTO o1, ShopDTO o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmFilterNiceIsdn:messages", "", ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmFilterNiceIsdn:messages", "", "common.error.happened");
        }
    }

    @Security
    public void resetForm() {
        typeRule = null;
        selectedNodes = null;
        startRange = null;
        endRange = null;
        shopId = null;
        conditionFilter = new String[20];
        conditionFilter[0] = "1";
        conditionFilter[1] = "2";
        conditionFilter[2] = "3";
        root = null;
        renderedRule = false;
        listResult = Lists.newArrayList();
    }

    @Security
    public void actionFilterNumber() {
        try {
            List<NumberFilterRuleDTO> listRule = Lists.newArrayList();
            for (TreeNode node : selectedNodes) {
                if (node.getData() instanceof NumberFilterRuleDTO) {
                    NumberFilterRuleDTO filterRule = (NumberFilterRuleDTO) node.getData();
                    if (!DataUtil.isNullObject(filterRule.getRuleOrder())) {
                        listRule.add(filterRule);
                    }
                }
            }
            // list luat da chon
            if (DataUtil.isNullOrEmpty(listRule)) {
                reportErrorValidateFail("frmFilterNiceIsdn:messages", "", "rule.isdn.must.select");
                focusElementByRawCSSSlector(".treeRule");
                return;
            }
            if (DataUtil.isNullObject(startRange)) {
                reportErrorValidateFail("frmFilterNiceIsdn:messages", "", "start.range.require");
                focusElementByRawCSSSlector(".digitalStartRanges");
                return;
            }
            if (!startRange.matches("^[0-9]+$")) {
                reportErrorValidateFail("frmFilterNiceIsdn:messages", "", "component.invalid", "digital.start.ranges");
                focusElementByRawCSSSlector(".digitalStartRanges");
                return;
            }
            if (DataUtil.isNullObject(endRange)) {
                reportErrorValidateFail("frmFilterNiceIsdn:messages", "", "end.range.require");
                return;
            }
            if (!endRange.matches("^[0-9]+$")) {
                reportErrorValidateFail("frmFilterNiceIsdn:messages", "", "component.invalid", "digital.end.ranges");
                focusElementByRawCSSSlector(".digitalEndRanges");
                return;
            }
            if (DataUtil.safeToLong(startRange) > DataUtil.safeToLong(endRange)) {
                reportErrorValidateFail("frmFilterNiceIsdn:messages", "", "ws.error.range");
                focusElementByRawCSSSlector(".digitalStartRanges");
                focusElementByRawCSSSlector(".digitalEndRanges");
                return;
            }
            if (startRange.length() != endRange.length()) {
                reportErrorValidateFail("frmFilterNiceIsdn:messages", "", "ws.error.range.length");
                focusElementByRawCSSSlector(".digitalEndRanges");
                return;
            }
            if ((startRange.length() < 8 || startRange.length() > 11)) {
                reportErrorValidateFail("frmFilterNiceIsdn:messages", "", "ws.range.start.length.invalid");
                focusElementByRawCSSSlector(".digitalStartRanges");
                return;
            }
            if ((endRange.length() < 8 || endRange.length() > 11)) {
                reportErrorValidateFail("frmFilterNiceIsdn:messages", "", "ws.range.end.length.invalid");
                focusElementByRawCSSSlector(".digitalEndRanges");
                return;
            }
            if (DataUtil.safeToLong(endRange) - DataUtil.safeToLong(startRange) > 999999) {
                reportErrorValidateFail("frmFilterNiceIsdn:messages", "", "ws.number.per.range.over");
                focusElementByRawCSSSlector(".digitalStartRanges");
                focusElementByRawCSSSlector(".digitalEndRanges");
                return;
            }
            BigDecimal startNumberConvert = DataUtil.safeToBigDecimal(startRange);
            BigDecimal endNumberConvert = DataUtil.safeToBigDecimal(endRange);
            Long ownerId = DataUtil.isNullObject(shopId) ? BccsLoginSuccessHandler.getStaffDTO().getShopId() : shopId; //owner_id
            String userCode = BccsLoginSuccessHandler.getStaffDTO().getStaffCode();
            // lay gia tri order nho nhat trong list
            Long minOrder = listRule.get(0).getRuleOrder();
            for (NumberFilterRuleDTO dto : listRule) {
                if (!DataUtil.isNullObject(minOrder) && !DataUtil.isNullObject(dto.getRuleOrder()) && (minOrder > dto.getRuleOrder())) {
                    minOrder = dto.getRuleOrder();
                }
            }
            Long telecomServiceId = Long.valueOf(typeRule);
            List<String> lstStatus = Lists.newArrayList();
            refilter = false;
            if (!DataUtil.isNullObject(conditionFilter)) {
                for (String condition : conditionFilter) {
                    if ("1".equals(condition)) { // So moi
                        lstStatus.add(Const.ISDN.NEW_NUMBER);
                    } else if ("2".equals(condition)) { //So ngung su dung
                        lstStatus.add(Const.ISDN.STOP_USE);
                    } else if ("3".equals(condition)) { //So dang su dung
                        lstStatus.add(Const.ISDN.USING);
                        lstStatus.add(Const.ISDN.KITED);
                    } else if ("4".equals(condition)) {
                        refilter = true;
                    }
                }
            }
            if (DataUtil.isNullOrEmpty(lstStatus)) {
                reportErrorValidateFail("frmFilterNiceIsdn:messages", "", "mn.isdn.number.nice.required.type");
                focusElementByRawCSSSlector(".checkBoxFilter");
                return;
            }
            filterSpecialNumberService.filterNumber(userCode, refilter, minOrder, listRule, ownerId, lstStatus, telecomServiceId, startNumberConvert, endNumberConvert);
            NumberActionDTO numberActionDTO = new NumberActionDTO();
            numberActionDTO.setFromIsdn(startRange);
            numberActionDTO.setToIsdn(endRange);
            numberActionDTO.setActionType(Const.NUMBER_ACTION_TYPE.LOC_SO);
            numberActionDTO.setTelecomServiceId(DataUtil.safeToLong(typeRule));
            numberActionDTO.setNote(getText("common.filter.nice"));
            numberActionDTO.setUserCreate(BccsLoginSuccessHandler.getStaffDTO().getStaffCode());
            numberActionDTO.setUserIpAddress(BccsLoginSuccessHandler.getIpAddress());
            numberActionService.create(numberActionDTO);
            reportSuccess("frmFilterNiceIsdn:messages", "mn.isdn.filter.isdn.start.filter");
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmFilterNiceIsdn:messages", "", ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmFilterNiceIsdn:messages", "", "common.error.happened");
        }

    }

    @Security
    public void actionGetResultFilter() {
        try {
            listResult = filterSpecialNumberService.getResultFilter();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmFilterNiceIsdn:messages", "", ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmFilterNiceIsdn:messages", "", "common.error.happened");
        }
    }

    private List<FilterSpecialNumberDTO> getData(List<FilterSpecialNumberDTO> data) {
        if (data != null) {
            List<FilterSpecialNumberDTO> result = Lists.newArrayList();
            for (FilterSpecialNumberDTO dto : data) {
                if (!DataUtil.isNullObject(dto.getStatus()) && (dto.getStatus().equals(Const.STATUS.ACTIVE) || dto.getStatus().equals(Const.STATUS.NO_ACTIVE))) {
                    result.add(dto);
                }
            }
            return result;
        }
        return new ArrayList<FilterSpecialNumberDTO>();
    }

    @Security
    public void actionViewDetail(Long selectedFilterRuleId) {
        try {
//            listSpecialNumber = DataUtil.cloneBean(filterSpecialNumberService.getListSprecialNumberByRule(this.currentRuleView));
            listSpecialNumber = DataUtil.cloneBean(filterSpecialNumberService.getListSprecialNumberByRule(selectedFilterRuleId));
            resultFilterRule = numberFilterRuleService.findOne(selectedFilterRuleId);
            listSelectedSpecialNumber = getData(listSpecialNumber);
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmFilterNiceIsdn:messages", "", ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmFilterNiceIsdn:messages", "", "common.error.happened");
        }
    }

    @Security
    public void submitSpecialNumber() {
        try {
            filterSpecialNumberService.updateFiltered(listSpecialNumber, listSelectedSpecialNumber, BccsLoginSuccessHandler.getStaffDTO().getStaffCode(), BccsLoginSuccessHandler.getIpAddress());
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmFilterNiceIsdn:messages", "", ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("messagesDlg", "", "common.error.happened");
        }
    }

    @Security
    public void submitAllFiltered() {
        try {
            List<Long> listFilterId = Lists.newArrayList();
            if (DataUtil.isNullOrEmpty(listSelectedResult)) {
                reportErrorValidateFail("frmFilterNiceIsdn:messages", "", "mn.isdn.number.nice.emprty.filter.rule");
                return;
            } else {
                for (Object[] obj : listSelectedResult) {
                    listFilterId.add(DataUtil.safeToLong(obj[0]));
                }
            }
            filterSpecialNumberService.updateAllFiltered(BccsLoginSuccessHandler.getStaffDTO().getStaffCode(), BccsLoginSuccessHandler.getIpAddress(), listFilterId);
            listResult.removeAll(listSelectedResult);
            reportSuccess("frmExportNote:msgs", "mn.isdn.filter.isdn.update.start");
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmFilterNiceIsdn:messages", "", ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("messagesDlg", "", "common.error.happened");
        }
    }

    @Security
    public void renderedListRule() {
        renderedRule = !DataUtil.isNullOrEmpty(typeRule);
        if (DataUtil.isNullObject(typeRule)) {
            root = null;
        } else {
            root = getListRule();
        }
        RequestContext.getCurrentInstance().update("frmFilterNiceIsdn:pnlTreeSelectRule");
    }

    @Security
    public void onNodeExpand(NodeExpandEvent event) {
        try {
            selectedNodes = null;
            event.getTreeNode().setExpanded(true);
            TreeNode node = event.getTreeNode();
            if (node.getData() instanceof GroupFilterRuleDTO) {
                GroupFilterRuleDTO rule = (GroupFilterRuleDTO) node.getData();
                if (DataUtil.isNullObject(rule.getParentId())) {
                    root = getListGroupRule(node);
                } else {
                    root = getNumberRule(node);
                }
            }
            selectedNodes = new TreeNode[0];
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmFilterNiceIsdn:messages", "", "common.error.happened");
        }
    }


    @Security
    public TreeNode getNumberRule(TreeNode parent) {
        try {
            GroupFilterRuleDTO obj = (GroupFilterRuleDTO) parent.getData();
            NumberFilterRuleDTO numberFilterRule = new NumberFilterRuleDTO();
            numberFilterRule.setGroupFilterRuleId(obj.getGroupFilterRuleId());
            List<NumberFilterRuleDTO> lstNumberRule = numberFilterRuleService.getListNumerFilterRule(numberFilterRule);

            if (!DataUtil.isNullOrEmpty(lstNumberRule)) {
                for (NumberFilterRuleDTO numberRule : lstNumberRule) {
                    new CheckboxTreeNode(numberRule, parent);
                }
            }
            return root;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmFilterNiceIsdn:messages", "", "common.error.happened");
        }
        return null;
    }

    @Security
    public TreeNode getListGroupRule(TreeNode parent) {
        try {
            GroupFilterRuleDTO obj = (GroupFilterRuleDTO) parent.getData();
            GroupFilterRuleDTO groupRule = new GroupFilterRuleDTO();
            groupRule.setParentId(obj.getGroupFilterRuleId());
            List<GroupFilterRuleDTO> lstRule = groupFilterRuleService.search(groupRule, true);
            boolean selected = parent.isSelected();
            if (!DataUtil.isNullOrEmpty(lstRule)) {
                for (GroupFilterRuleDTO rule : lstRule) {
                    TreeNode rule1 = new CheckboxTreeNodeNotLeaf(rule, parent, selected);
                    getNumberRule(rule1);
                }
            }
            return root;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmFilterNiceIsdn:messages", "", "common.error.happened");
        }
        return null;
    }

    @Security
    public TreeNode getListRule() {
        try {
            TreeNode root = new CheckboxTreeNode(new Document(getText("common.select.all")), null);
            TreeNode all = new CheckboxTreeNodeNotLeaf(new Document(getText("common.select.all")), root, false);
            GroupFilterRuleDTO groupRule = new GroupFilterRuleDTO();
            groupRule.setTelecomServiceId(Long.valueOf(typeRule));
            List<GroupFilterRuleDTO> lstRule = groupFilterRuleService.search(groupRule, false);
            if (!DataUtil.isNullOrEmpty(lstRule)) {
                for (GroupFilterRuleDTO rule : lstRule) {
                    TreeNode rule1 = new CheckboxTreeNodeNotLeaf(rule, all, false);
                    getListGroupRule(rule1);
                }
            }
            return root;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmFilterNiceIsdn:messages", "", "common.error.happened");
        }
        return null;
    }

    public String getStatusName(String status) {
        if (!DataUtil.isNullObject(status)) {
            if ("0".equals(status)) {
                return BundleUtil.getText("status.special.number1");
            } else if ("1".equals(status)) {
                return BundleUtil.getText("status.special.number0");
            } else if ("2".equals(status)) {
                return BundleUtil.getText("status.special.number2");
            }
        }
        return "";
    }

    public boolean isRenderedRule() {
        return renderedRule;
    }

    public void setRenderedRule(boolean renderedRule) {
        this.renderedRule = renderedRule;
    }

    public String isTypeRule() {
        return typeRule;
    }

    public String getTypeRule() {
        return typeRule;
    }

    public void setTypeRule(String typeRule) {
        this.typeRule = typeRule;
    }

    public TreeNode getRoot() {
        return root;
    }

    public TreeNode[] getSelectedNodes() {
        return selectedNodes;
    }

    public List<ShopDTO> getLstShop() {
        return lstShop;
    }

    public void setLstShop(List<ShopDTO> lstShop) {
        this.lstShop = lstShop;
    }

    public void setSelectedNodes(TreeNode[] selectedNodes) {
        this.selectedNodes = selectedNodes;
    }

    public List<OptionSetValueDTO> getListService() {
        return listService;
    }

    public void setListService(List<OptionSetValueDTO> listService) {
        this.listService = listService;
    }

    public String getStartRange() {
        return startRange;
    }

    public void setStartRange(String startRange) {
        this.startRange = startRange;
    }

    public String getEndRange() {
        return endRange;
    }

    public void setEndRange(String endRange) {
        this.endRange = endRange;
    }

    public Long getShopId() {
        return shopId;
    }

    public String[] getConditionFilter() {
        return conditionFilter;
    }

    public void setConditionFilter(String[] conditionFilter) {
        this.conditionFilter = conditionFilter;
    }

    public List<Object[]> getListResult() {
        return listResult;
    }

    public void setListResult(List<Object[]> listResult) {
        this.listResult = listResult;
    }

    public List<FilterSpecialNumberDTO> getListSpecialNumber() {
        return listSpecialNumber;
    }

    public void setListSpecialNumber(List<FilterSpecialNumberDTO> listSpecialNumber) {
        this.listSpecialNumber = listSpecialNumber;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getRuleNameSelected() {
        return ruleNameSelected;
    }

    public List<FilterSpecialNumberDTO> getListSelectedSpecialNumber() {
        return listSelectedSpecialNumber;
    }

    public void setListSelectedSpecialNumber(List<FilterSpecialNumberDTO> listSelectedSpecialNumber) {
        this.listSelectedSpecialNumber = listSelectedSpecialNumber;
    }

    public void setRuleNameSelected(String ruleNameSelected) {
        this.ruleNameSelected = ruleNameSelected;
    }

    public Long getCurrentRuleView() {
        return currentRuleView;
    }

    public void setCurrentRuleView(Long currentRuleView) {
        this.currentRuleView = currentRuleView;
    }

    public boolean isRefilter() {
        return refilter;
    }

    public void setRefilter(boolean refilter) {
        this.refilter = refilter;
    }

    public List<Object[]> getListSelectedResult() {
        return listSelectedResult;
    }

    public void setListSelectedResult(List<Object[]> listSelectedResult) {
        this.listSelectedResult = listSelectedResult;
    }

    public NumberFilterRuleDTO getResultFilterRule() {
        return resultFilterRule;
    }

    public void setResultFilterRule(NumberFilterRuleDTO resultFilterRule) {
        this.resultFilterRule = resultFilterRule;
    }

    @Security
    public void exportExcel() {
        try {
            List<Object[]> listObjectExport = Lists.newArrayList();
            if (!DataUtil.isNullOrEmpty(listSelectedResult)) {
                for (Object[] ob : listSelectedResult) {
                    actionViewDetail(DataUtil.safeToLong(ob[0]));
                    for (FilterSpecialNumberDTO dto : listSpecialNumber) {
                        Object[] obExport = new Object[5];
                        obExport[0] = dto.getIsdn();
                        obExport[1] = ob[1];
                        obExport[2] = ob[3];
                        obExport[3] = ob[5];
                        obExport[4] = dto.getProductName();
                        listObjectExport.add(obExport);
                    }
                }
                FileExportBean bean = new FileExportBean();
                bean.setTempalatePath(FileUtil.getTemplatePath());
                bean.setTemplateName("/filterRuleNiceDigitTemplate.xls");
                bean.putValue("lstData", listObjectExport);
                bean.putValue("createUser", BccsLoginSuccessHandler.getStaffDTO().getStaffCode());
                bean.putValue("createDate", DateUtil.date2ddMMyyyyString(new Date()));
                Workbook bookError = FileUtil.exportWorkBook(bean);

                FacesContext facesContext = FacesContext.getCurrentInstance();
                ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
                externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + "FILTER_NICE_DIGIT.xls" + "\"");
                externalContext.setResponseContentType("application/vnd.ms-excel");
                bookError.write(externalContext.getResponseOutputStream());
                facesContext.responseComplete();
                return;
            } else {
                reportErrorValidateFail("frmFilterNiceIsdn:messages", "", "mn.isdn.number.nice.emprty.filter.rule.export");
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmFilterNiceIsdn:messages", "", "common.error.happened");
        }
    }
}