package com.viettel.bccs.inventory.controller.isdn;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.IsdnUtil;
import com.viettel.bccs.inventory.dto.GroupFilterRuleDTO;
import com.viettel.bccs.inventory.dto.NumberFilterRuleDTO;
import com.viettel.bccs.inventory.dto.ProductOfferingDTO;
import com.viettel.bccs.inventory.dto.ProductOfferingTotalDTO;
import com.viettel.bccs.inventory.service.GroupFilterRuleService;
import com.viettel.bccs.inventory.service.NumberFilterRuleService;
import com.viettel.bccs.inventory.service.ProductOfferingService;
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
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * controller chuc nang luat so dep
 *
 * @author:hungdv24
 */
@Component
@Scope("view")
@ManagedBean(name = "numberFilerRuleController")
public class NumberFilerRuleController extends BaseController {

    private final static String VALID_SUCCESS = "success";

    @Autowired
    private NumberFilterRuleService numberFilterRuleService;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private GroupFilterRuleService groupFilterRuleService;

    private NumberFilterRuleDTO searchDto;
    private NumberFilterRuleDTO editDto;
    private NumberFilterRuleDTO numberDTODeleteOne;

    private List<ProductOfferingTotalDTO> lsProductOfferingTotalDTO = Lists.newArrayList();
    private List<ProductOfferingTotalDTO> lsSearchProductOfferingTotalDTO = Lists.newArrayList();
    private ProductOfferingTotalDTO productOfferingTotalDTO;
    private ProductOfferingTotalDTO searchProductOfferingTotalDTO;
    private List<GroupFilterRuleDTO> aggregateFilterRuleDTOList = Lists.newArrayList();//tap luat
    private List<GroupFilterRuleDTO> groupFilterRuleDTOList = Lists.newArrayList();//nhom luat
    private List<GroupFilterRuleDTO> aggregateFilterRuleDTOListDlg = Lists.newArrayList();//tap luat tren dialog
    private List<GroupFilterRuleDTO> groupFilterRuleDTOListDlg = Lists.newArrayList();//nhom luat tren dialog
    private List<NumberFilterRuleDTO> listResultSearch = Lists.newArrayList();
    private List<NumberFilterRuleDTO> listSelectAllDelete = Lists.newArrayList();
    private String telecomService = "";

    @Secured("@")
    @PostConstruct
    public void init() {
        try {
            searchDto = new NumberFilterRuleDTO();
            editDto = new NumberFilterRuleDTO();
            //Load combo tap luat
            aggregateFilterRuleDTOList = DataUtil.defaultIfNull(groupFilterRuleService.getListNumberFiler(null, true), new ArrayList<GroupFilterRuleDTO>());//tap luat

            if (!DataUtil.isNullOrEmpty(aggregateFilterRuleDTOList)) {

                Collections.sort(aggregateFilterRuleDTOList, new Comparator<GroupFilterRuleDTO>() {
                    @Override
                    public int compare(GroupFilterRuleDTO o1, GroupFilterRuleDTO o2) {
                        if (o1 != null && o2 != null && o1.getName() != null && o2.getName() != null) {
                            return o1.getName().trim().compareTo(o2.getName().trim());
                        }
                        return 0;
                    }
                });
            }

            aggregateFilterRuleDTOListDlg.addAll(aggregateFilterRuleDTOList);
            groupFilterRuleDTOList = Lists.newArrayList();
            groupFilterRuleDTOListDlg = Lists.newArrayList();
//            groupFilterRuleDTOList = DataUtil.defaultIfNull(groupFilterRuleService.getListNumberFiler(null, false), new ArrayList<GroupFilterRuleDTO>());//nhom luat
            //xu ly tim kiem
            listResultSearch = getListNumberFilter(new NumberFilterRuleDTO(), false);

            //load combobox product
//            List<FilterRequest> requestsPro = Lists.newArrayList();
//            requestsPro.add(new FilterRequest(ProductOffering.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, "1"));
//            List<ProductOfferingDTO> resultPro = productOfferingService.findByFilter(requestsPro);

            lsProductOfferingTotalDTO = DataUtil.defaultIfNull(productOfferingService.getLsProductOfferingByProductType("", 0L), new ArrayList<ProductOfferingTotalDTO>());
            lsSearchProductOfferingTotalDTO = DataUtil.defaultIfNull(productOfferingService.getLsProductOfferingByProductType("", 0L), new ArrayList<ProductOfferingTotalDTO>());
//            lsProductOfferingTotalDTO = DataUtil.defaultIfNull(productOfferingService.getAllLsProductOfferingDTO(""), new ArrayList<ProductOfferingTotalDTO>());

        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getKeyMsg());
            topPage();
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    /**
     * ham xu ly tim kiem
     *
     * @param condition
     * @return
     * @throws LogicException
     * @throws Exception
     * @author hungdv24
     */
    private List<NumberFilterRuleDTO> getListNumberFilter(NumberFilterRuleDTO condition, boolean isExtract) throws LogicException, Exception {
        return numberFilterRuleService.findRuleAggregate(condition, isExtract);
    }

    /**
     * ham reset tim kiem
     *
     * @author Hungdv24
     */
    @Secured("@")
    public void doResetSearchNumberFilter() {
        searchDto = new NumberFilterRuleDTO();
//        groupFilterRuleDTOList = Lists.newArrayList();
        searchProductOfferingTotalDTO = new ProductOfferingTotalDTO();
        productOfferingTotalDTO = new ProductOfferingTotalDTO();
        editDto = new NumberFilterRuleDTO();
    }

    /**
     * ham tim kiem
     *
     * @author Hungdv24
     */
    @Secured("@")
    public void doSearchNumberFilter() {
        try {
            listResultSearch = getListNumberFilter(searchDto, false);
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getKeyMsg());
            topPage();
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    @Secured("@")
    public void doConfirmOneDelete(NumberFilterRuleDTO numberFilterRuleDTO) {
        this.numberDTODeleteOne = numberFilterRuleDTO;
    }

    @Secured("@")
    public void changeServiceType() {

    }

    @Secured("@")
    public void doConfirmAddEdit() {

    }

    @Secured("@")
    public void doOneDelete() {
        try {
            if (DataUtil.isNullObject(numberDTODeleteOne)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.isdn.rule.delete.unselect");
            }
            BaseMessage baseMessage = deleteListNumberFiler(Lists.newArrayList(numberDTODeleteOne));
            if (baseMessage != null && !DataUtil.isNullOrEmpty(baseMessage.getErrorCode())) {
                throw new LogicException(baseMessage.getErrorCode(), baseMessage.getKeyMsg());
            }
            //xu ly tim kiem
            listResultSearch = getListNumberFilter(searchDto, false);
            reportSuccess("frmNiceDigit:msgs", "common.msg.success.delete");
            listSelectAllDelete = Lists.newArrayList();
            numberDTODeleteOne = new NumberFilterRuleDTO();
            groupFilterRuleDTOListDlg = Lists.newArrayList();
            topPage();
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

    /**
     * ham xu ly xoa list
     * hungdvd24
     *
     * @param ruleDelete
     * @return
     * @throws LogicException
     * @throws Exception
     */
    private BaseMessage deleteListNumberFilterRule(List<NumberFilterRuleDTO> ruleDelete) throws LogicException, Exception {
        return numberFilterRuleService.deleteListNumberFiler(ruleDelete, BccsLoginSuccessHandler.getStaffDTO());
    }

    /**
     * ham xu ly xac nhan xoa list so dep
     *
     * @author hungdv24
     */
    @Secured("@")
    public void doConfirmListDelete() {
        if (DataUtil.isNullOrEmpty(listSelectAllDelete)) {
            reportError("frmNiceDigit:msgs", "", "mn.isdn.rule.delete.unselect");
        }
    }

    /**
     * ham xu ly xoa list so dep
     *
     * @author hungdv24
     */
    @Secured("@")
    public void doDeleteListNumberFilterRule() {
        try {
            if (DataUtil.isNullOrEmpty(listSelectAllDelete)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.isdn.rule.delete.unselect");
            }
            BaseMessage baseMessage = deleteListNumberFiler(listSelectAllDelete);
            if (baseMessage != null && !DataUtil.isNullOrEmpty(baseMessage.getErrorCode())) {
                throw new LogicException(baseMessage.getErrorCode(), baseMessage.getKeyMsg());
            }
            //xu ly tim kiem
            listResultSearch = getListNumberFilter(new NumberFilterRuleDTO(), false);
            reportSuccess("frmNiceDigit:msgs", "common.msg.success.delete");
            listSelectAllDelete = Lists.newArrayList();
            numberDTODeleteOne = new NumberFilterRuleDTO();
            groupFilterRuleDTOListDlg = Lists.newArrayList();
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

    /**
     * ham validate dieu kien
     *
     * @param format
     * @param condition
     * @return
     * @author hungdv24
     */
    private String checkValidCondition(String format, String condition) {
        String formated = format.trim();
        char[] cFormat = formated.toCharArray();
        char[] cFormatPattern = new char[cFormat.length];
        for (int i = 0; i < cFormatPattern.length; i++) {
            cFormatPattern[i] = '0';
        }

        //kiem tra condition co dung khong
        Map<String, BigDecimal> variables = new LinkedHashMap<String, BigDecimal>();
        for (int idx = 0; idx < formated.length(); idx++) {
            Long num = Long.valueOf(String.valueOf(cFormatPattern[idx]));
            variables.put(String.valueOf(cFormat[idx]), BigDecimal.valueOf(num));
        }
        try {
            return VALID_SUCCESS;
        } catch (RuntimeException ex) {
            logger.error(ex.getMessage(), ex);
            return ex.getMessage();
        }
    }

    /**
     * ham xu ly xoa list danh sach so dep
     *
     * @param lsNumberFilter
     * @return
     * @throws LogicException
     * @throws Exception
     * @author hungdv24
     */
    private BaseMessage deleteListNumberFiler(List<NumberFilterRuleDTO> lsNumberFilter) throws LogicException, Exception {
        return numberFilterRuleService.deleteListNumberFiler(lsNumberFilter, BccsLoginSuccessHandler.getStaffDTO());
    }

    /**
     * ham thay doi tap luat
     *
     * @author hungdv24
     */
    @Secured("@")
    public void onChangeSearchGroup() {
        try {
            searchDto.setGroupFilterRuleId(null);
            if (searchDto.getAggregateFilterRuleId() != null && searchDto.getAggregateFilterRuleId() > 0) {
                groupFilterRuleDTOList = DataUtil.defaultIfNull(groupFilterRuleService.getListNumberFiler(searchDto.getAggregateFilterRuleId(), true), new ArrayList<GroupFilterRuleDTO>());
                searchDto.setLsFilterRuleId(DataUtil.collectProperty(groupFilterRuleDTOList, "groupFilterRuleId", Long.class)); //chuyen sang list
                if (!DataUtil.isNullOrEmpty(groupFilterRuleDTOList)) {

                    Collections.sort(groupFilterRuleDTOList, new Comparator<GroupFilterRuleDTO>() {
                        @Override
                        public int compare(GroupFilterRuleDTO o1, GroupFilterRuleDTO o2) {
                            if (o1 != null && o2 != null && o1.getName() != null && o2.getName() != null) {
                                return o1.getName().trim().compareTo(o2.getName().trim());
                            }
                            return 0;
                        }
                    });
                }
            } else {
                groupFilterRuleDTOList = Lists.newArrayList();
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getKeyMsg());
            topPage();
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    /**
     * ham thay doi nhom luat
     *
     * @author hungdv24
     */
    @Secured("@")
    public void onChangeEditGroup() {
        try {
            editDto.setGroupFilterRuleId(null);
            if (editDto.getAggregateFilterRuleId() != null && editDto.getAggregateFilterRuleId() > 0) {
                groupFilterRuleDTOListDlg = DataUtil.defaultIfNull(groupFilterRuleService.getListNumberFiler(editDto.getAggregateFilterRuleId(), true), new ArrayList<GroupFilterRuleDTO>());
                editDto.setLsFilterRuleId(DataUtil.collectProperty(groupFilterRuleDTOListDlg, "groupFilterRuleId", Long.class));
                long id = editDto.getAggregateFilterRuleId();
                GroupFilterRuleDTO group = null;
                for (GroupFilterRuleDTO groupFilterRule : aggregateFilterRuleDTOListDlg) {
                    if (groupFilterRule.getGroupFilterRuleId().longValue() == id) {
                        group = groupFilterRule;
                        break;
                    }
                }
                if (!DataUtil.isNullObject(group)) {
                    if (DataUtil.isNullOrEmpty(telecomService) || (!IsdnUtil.checkTelecomService(telecomService, group))) {
                        telecomService = IsdnUtil.getTelecomService(group.getTelecomServiceId());
//                        lsProductOfferingTotalDTO = Lists.newArrayList();
                        productOfferingTotalDTO = new ProductOfferingTotalDTO();
                    } else {
                        telecomService = IsdnUtil.getTelecomService(group.getTelecomServiceId());
                    }
                }
            } else {
                groupFilterRuleDTOListDlg = Lists.newArrayList();
            }

            if (!DataUtil.isNullOrEmpty(groupFilterRuleDTOListDlg)) {

                Collections.sort(groupFilterRuleDTOListDlg, new Comparator<GroupFilterRuleDTO>() {
                    @Override
                    public int compare(GroupFilterRuleDTO o1, GroupFilterRuleDTO o2) {
                        if (o1 != null && o2 != null && o1.getName() != null && o2.getName() != null) {
                            return o1.getName().trim().compareTo(o2.getName().trim());
                        }
                        return 0;
                    }
                });
            }


        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getKeyMsg());
            topPage();
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }


    @Secured("@")
    public void search() {
        try {
            if (!DataUtil.isNullObject(searchProductOfferingTotalDTO)) {
                searchDto.setProdOfferId(searchProductOfferingTotalDTO.getProductOfferingId());
            } else {
                searchDto.setProdOfferId(null);
            }
            if (searchDto.getAggregateFilterRuleId() != null && searchDto.getAggregateFilterRuleId() > 0L
                    && DataUtil.isNullOrEmpty(groupFilterRuleDTOList)) {
                listResultSearch = Lists.newArrayList();
                return;
            }

            if (searchDto.getGroupFilterRuleId() != null && searchDto.getGroupFilterRuleId() > 0L) {
                searchDto.setLsFilterRuleId(Lists.newArrayList(searchDto.getGroupFilterRuleId()));
            }
            listResultSearch = getListNumberFilter(searchDto, false);
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

    /**
     * ham mo dialog add moi finance
     *
     * @author ThanhNT
     */
    @Secured("@")
    public void doOpenEditNumberFilter(Long id) {
        try {
            editDto = numberFilterRuleService.findOne(id);
            if (editDto != null) {
                GroupFilterRuleDTO groupFilterRuleDTO = groupFilterRuleService.findOne(editDto.getGroupFilterRuleId());
                GroupFilterRuleDTO g = groupFilterRuleService.findOne(groupFilterRuleDTO.getParentId());
                telecomService = IsdnUtil.getTelecomService(g.getTelecomServiceId());
                editDto.setAggregateFilterRuleId(groupFilterRuleDTO.getParentId());
                aggregateFilterRuleDTOListDlg = DataUtil.defaultIfNull(groupFilterRuleService.getListNumberFiler(null, true), new ArrayList<GroupFilterRuleDTO>());//tap luat
                groupFilterRuleDTOListDlg = DataUtil.defaultIfNull(groupFilterRuleService.getListNumberFiler(groupFilterRuleDTO.getParentId(), true), new ArrayList<GroupFilterRuleDTO>());//nhom luat
                if (!DataUtil.isNullObject(editDto.getProdOfferId())) {
                    productOfferingTotalDTO = productOfferingService.getProductOffering(editDto.getProdOfferId());
                }
            }

        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    /**
     * ham mo dialog add moi finance
     *
     * @author ThanhNT
     */
    @Secured("@")
    public void doOpenAddNumberFilter() {
        editDto = new NumberFilterRuleDTO();
        productOfferingTotalDTO = new ProductOfferingTotalDTO();
        groupFilterRuleDTOListDlg = Lists.newArrayList();
        telecomService = "";
    }

    @Secured("@")
    public void addRuleList(boolean close) {
        try {

            if (DataUtil.isNullObject(productOfferingTotalDTO) || DataUtil.isNullOrZero(productOfferingTotalDTO.getProductOfferingId())) {
                reportError("frmAddRuleList:msgAddRuleList", "", "export.order.stock.inputText.require.msg");
                focusElementByRawCSSSlector(".cbxRuleItem");
                return;
            }

            if (!DataUtil.isNullObject(productOfferingTotalDTO)) {
                editDto.setProdOfferId(productOfferingTotalDTO.getProductOfferingId());
            }

            //validate name
            if (DataUtil.isNullObject(editDto.getName())) {
                reportError("frmAddRuleList:msgAddRuleList", "", "mn.isdn.rule.nice.digit.name.notnull");
                focusElementByRawCSSSlector(".focusName");
                return;
            }
            // tap luat
            if (DataUtil.isNullObject(editDto.getAggregateFilterRuleId())) {
                reportError("frmAddRuleList:msgAddRuleList", "", "mn.isdn.rule.nice.digit.aggregateRule.notnull");
                focusElementByRawCSSSlector(".cbxAggregateRule");
                return;
            }
            //nhom luat
            if (DataUtil.isNullObject(editDto.getGroupFilterRuleId())) {
                reportError("frmAddRuleList:msgAddRuleList", "", "mn.isdn.rule.nice.digit.group.notnull");
                focusElementByRawCSSSlector(".cbxRuleGroup1");
                return;
            }
            //cap uu tien
            if (DataUtil.isNullObject(editDto.getRuleOrder())) {
                reportError("frmAddRuleList:msgAddRuleList", "", "mn.isdn.rule.nice.digit.priority.notnull");
                focusElementByRawCSSSlector(".focusPriorities");
                return;
            }

            if (numberFilterRuleService.checkExistNameNumberFilter(editDto.getName(), editDto.getFilterRuleId())) {
                reportError("frmAddRuleList:msgAddRuleList", "", "mn.isdn.rule.nice.digit.name.exit");
                focusElementByRawCSSSlector(".focusName");
                return;
            }


            //vailidate minlength for mask

            if (editDto.getMaskMapping().length() < 8) {
                reportError("frmAddRuleList:msgAddRuleList", "", "mn.isdn.rule.nice.digit.mask.minLength");
                focusElementByRawCSSSlector(".focusMask");
                return;
            }
//            //validate is string for mask
//            if(!isNumeric(editDto.getMaskMapping())){
//                reportError("frmAddRuleList:msgAddRuleList", "", "mn.isdn.rule.nice.digit.mask.isString");
//                focusElementByRawCSSSlector(".focusMask");
//                return;
//            }

            // validate priority
            if (editDto.getFilterRuleId() != null && editDto.getFilterRuleId() > 0) {
                if (!DataUtil.isNullOrEmpty(editDto.getCondition())) {
                    //kiem tra condition voi telecomServiceId
                    Long check = numberFilterRuleService.checkEditConditionTelecomServiceId(editDto);
                    if (!DataUtil.isNullOrZero(check)) {
                        reportError("frmAddRuleList:msgAddRuleList", "", "mn.isdn.rule.nice.digit.conditionPriority.exit");
                        focusElementByRawCSSSlector(".focusPriorities");
                        return;
                    }
                }
            } else {
                if (!DataUtil.isNullOrEmpty(editDto.getCondition())) {
                    //kiem tra condition voi telecomServiceId
                    Long check = numberFilterRuleService.checkInsertConditionTelecomServiceId(editDto);
                    if (!DataUtil.isNullOrZero(check)) {
                        reportError("frmAddRuleList:msgAddRuleList", "", "mn.isdn.rule.nice.digit.conditionPriority.exit");
                        focusElementByRawCSSSlector(".focusPriorities");
                        return;
                    }
                }
            }

            //validate condition
            if (editDto.getCondition() != null && editDto.getMaskMapping() != null) {
                String checkConditionStr = checkValidCondition(editDto.getMaskMapping(), editDto.getCondition());
                if (!VALID_SUCCESS.equals(checkConditionStr)) {
                    reportError("frmAddRuleList:msgAddRuleList", "", "mn.isdn.rule.nice.digit.condition.notmap");
                    focusElementByRawCSSSlector(".focusCondition");
                    return;
                }
            } else {
                reportError("frmAddRuleList:msgAddRuleList", "", "mn.isdn.rule.nice.digit.condition.notnull");
                focusElementByRawCSSSlector(".focusCondition"); //inputRuleName:inputRuleName
                return;
            }

            if (editDto.getFilterRuleId() != null && editDto.getFilterRuleId() > 0) {
                numberFilterRuleService.update(editDto, BccsLoginSuccessHandler.getStaffDTO());
            } else {
                numberFilterRuleService.create(editDto, BccsLoginSuccessHandler.getStaffDTO());
            }

            Long filterRuleId = editDto.getFilterRuleId();
            searchDto = new NumberFilterRuleDTO();
            editDto = new NumberFilterRuleDTO();
//            listResultSearch = getListNumberFilter(searchDto, false);
            listResultSearch = getListNumberFilter(new NumberFilterRuleDTO(), false);
            productOfferingTotalDTO = new ProductOfferingTotalDTO();
            onChangeEditGroup();
            if (close || (filterRuleId != null && filterRuleId > 0)) {
                updateElemetId("frmNiceDigit"); //Neu = true thi update frmNiceDigit
            } else {
                updateElemetId("frmUpdateRuleList");
//                updateElemetId("frmNiceDigit:lstOrderExport");
            }
            if (filterRuleId != null && filterRuleId > 0) {
                reportSuccess("frmNiceDigit:msgs", "common.msg.success.update");
            } else {
                reportSuccess("", "common.msg.success.add");
            }
            topPage();

        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getKeyMsg());
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmNiceDigit:msgs", "", "common.error.happened");
            topPage();
        }
    }

    private boolean isNumeric(final String value) {
        final char c = value.charAt(0);
        return (c >= '0' && c <= '9');
    }

    public int checkStringCondition(String str) {
        return 1;
    }

    /**
     * hien thi title dialog
     *
     * @author HungDV24
     */
    @Secured("@")
    public String getTitleDlgNiceDigit() {
        if (editDto.getFilterRuleId() != null && editDto.getFilterRuleId() > 0) {
            return getText("mn.isdn.rule.nice.digit.edit");
        } else {
            return getText("mn.isdn.rule.nice.digit.add");
        }
    }


    /**
     * @param status
     * @return
     * @author HungDV24 ham xu ly lay ra ten trang thai
     */
    @Secured("@")
    public String getStrStatus(String status) {
        String result = "";
        if (!DataUtil.isNullOrEmpty(status)) {
            switch (status) {
                case "1":
                    result = getText("common.type.status.active");
                    break;
                case "0":
                    result = getText("common.type.status.inactive");
                    break;
            }
        }
        return result;
    }

    /**
     * @param groupId
     * @return
     * @author HungDV24 ham xu ly lay ra ten nhom luat
     */
    @Secured("@")
    public String getStrNameGroup(Long groupId) {
        String result = "";
        if (!DataUtil.isNullOrZero(groupId)) {
            try {
                GroupFilterRuleDTO group = groupFilterRuleService.findOne(groupId);
                if (!DataUtil.isNullObject(group))
                    result = group.getName();
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
        return result;
    }

    /**
     * @param proId
     * @return
     * @author HungDV24 ham xu ly lay ra ten mat hang
     */
    @Secured("@")
    public String getStrNameProduct(Long proId) {
        String result = "";
        if (!DataUtil.isNullOrZero(proId)) {
            try {
                ProductOfferingDTO pro = productOfferingService.findOne(proId);
                if (!DataUtil.isNullObject(pro))
                    result = pro.getName();
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
        return result;
    }

    //getter and setter

    @Secured("@")
    public String formatNumber(Long total) {
        DecimalFormat format = new DecimalFormat("####.###");
        return format.format(total);
    }

    @Secured("@")
    public List<ProductOfferingTotalDTO> doChangeOffering(String inputProduct) {
        try {
            String input = DataUtil.isNullOrEmpty(inputProduct) ? "" : inputProduct.trim();
            List<ProductOfferingTotalDTO> test = productOfferingService.getLsProductOfferingByProductType(input, DataUtil.safeToLong(telecomService));
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
        return new ArrayList<ProductOfferingTotalDTO>();
    }

    @Secured("@")
    public List<ProductOfferingTotalDTO> doSearchChangeOffering(String inputProduct) {
        try {
            String input = DataUtil.isNullOrEmpty(inputProduct) ? "" : inputProduct.trim();
            if (!DataUtil.isNullObject(searchDto.getAggregateFilterRuleId())) {
                for (GroupFilterRuleDTO filterRule : aggregateFilterRuleDTOList) {
                    if (filterRule.getGroupFilterRuleId().equals(searchDto.getAggregateFilterRuleId())) {
                        Long productOfferTypeId;
                        if (filterRule.getTelecomServiceId().equals(Const.STOCK_TYPE.STOCK_ISDN_MOBILE)) {
                            productOfferTypeId = Const.STOCK_TYPE.STOCK_ISDN_MOBILE;
                        } else if (filterRule.getTelecomServiceId().equals(Const.STOCK_TYPE.STOCK_ISDN_HOMEPHONE)) {
                            productOfferTypeId = Const.STOCK_TYPE.STOCK_ISDN_HOMEPHONE;
                        } else {
                            productOfferTypeId = Const.STOCK_TYPE.STOCK_ISDN_PSTN;
                        }
                        return productOfferingService.getLsProductOfferingByProductType(input, productOfferTypeId);
                    }
                }
            } else {
                return productOfferingService.getLsProductOfferingByProductType(input, null);
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
        return new ArrayList<ProductOfferingTotalDTO>();
    }

    public NumberFilterRuleDTO getSearchDto() {
        return searchDto;
    }

    public void setSearchDto(NumberFilterRuleDTO searchDto) {
        this.searchDto = searchDto;
    }

    public NumberFilterRuleDTO getEditDto() {
        return editDto;
    }

    public void setEditDto(NumberFilterRuleDTO editDto) {
        this.editDto = editDto;
    }

    public NumberFilterRuleDTO getNumberDTODeleteOne() {
        return numberDTODeleteOne;
    }

    public void setNumberDTODeleteOne(NumberFilterRuleDTO numberDTODeleteOne) {
        this.numberDTODeleteOne = numberDTODeleteOne;
    }

    public List<GroupFilterRuleDTO> getAggregateFilterRuleDTOList() {
        return aggregateFilterRuleDTOList;
    }

    public void setAggregateFilterRuleDTOList(List<GroupFilterRuleDTO> aggregateFilterRuleDTOList) {
        this.aggregateFilterRuleDTOList = aggregateFilterRuleDTOList;
    }

    public List<GroupFilterRuleDTO> getGroupFilterRuleDTOList() {
        return groupFilterRuleDTOList;
    }

    public void setGroupFilterRuleDTOList(List<GroupFilterRuleDTO> groupFilterRuleDTOList) {
        this.groupFilterRuleDTOList = groupFilterRuleDTOList;
    }

    public List<GroupFilterRuleDTO> getAggregateFilterRuleDTOListDlg() {
        return aggregateFilterRuleDTOListDlg;
    }

    public void setAggregateFilterRuleDTOListDlg(List<GroupFilterRuleDTO> aggregateFilterRuleDTOListDlg) {
        this.aggregateFilterRuleDTOListDlg = aggregateFilterRuleDTOListDlg;
    }

    public List<GroupFilterRuleDTO> getGroupFilterRuleDTOListDlg() {
        return groupFilterRuleDTOListDlg;
    }

    public void setGroupFilterRuleDTOListDlg(List<GroupFilterRuleDTO> groupFilterRuleDTOListDlg) {
        this.groupFilterRuleDTOListDlg = groupFilterRuleDTOListDlg;
    }

    public List<NumberFilterRuleDTO> getListResultSearch() {
        return listResultSearch;
    }

    public void setListResultSearch(List<NumberFilterRuleDTO> listResultSearch) {
        this.listResultSearch = listResultSearch;
    }

    public List<NumberFilterRuleDTO> getListSelectAllDelete() {
        return listSelectAllDelete;
    }

    public void setListSelectAllDelete(List<NumberFilterRuleDTO> listSelectAllDelete) {
        this.listSelectAllDelete = listSelectAllDelete;
    }

    public List<ProductOfferingTotalDTO> getLsProductOfferingTotalDTO() {
        return lsProductOfferingTotalDTO;
    }

    public void setLsProductOfferingTotalDTO(List<ProductOfferingTotalDTO> lsProductOfferingTotalDTO) {
        this.lsProductOfferingTotalDTO = lsProductOfferingTotalDTO;
    }

    public ProductOfferingTotalDTO getProductOfferingTotalDTO() {
        return productOfferingTotalDTO;
    }

    public void setProductOfferingTotalDTO(ProductOfferingTotalDTO productOfferingTotalDTO) {
        this.productOfferingTotalDTO = productOfferingTotalDTO;
    }

    public ProductOfferingTotalDTO getSearchProductOfferingTotalDTO() {
        return searchProductOfferingTotalDTO;
    }

    public void setSearchProductOfferingTotalDTO(ProductOfferingTotalDTO searchProductOfferingTotalDTO) {
        this.searchProductOfferingTotalDTO = searchProductOfferingTotalDTO;
    }
}
