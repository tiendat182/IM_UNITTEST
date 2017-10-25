package com.viettel.bccs.inventory.controller.isdn;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.GroupFilterRuleDTO;
import com.viettel.bccs.inventory.dto.OptionSetValueDTO;
import com.viettel.bccs.inventory.service.GroupFilterRuleService;
import com.viettel.bccs.inventory.service.OptionSetValueService;
import com.viettel.bccs.inventory.service.ProductOfferTypeService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.web.common.controller.BaseController;
import com.viettel.web.notify.NotifyService;
import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.util.*;

/**
 * Created by anhnv33 on 12/1/2015.
 */
@Component
@Scope("view")
@ManagedBean(name = "createRuleListController")
public class CreateRuleListController extends BaseController {

    @Autowired
    private GroupFilterRuleService groupFilterRuleService;

    @Autowired
    private ProductOfferTypeService productOfferTypeService;

    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private NotifyService notifyService;

    private GroupFilterRuleDTO searchDto;

    private GroupFilterRuleDTO editDto;

    private List<OptionSetValueDTO> optionSetValueDTOs = Lists.newArrayList();

    private List<GroupFilterRuleDTO> listResultSearch = Lists.newArrayList();

    private List<GroupFilterRuleDTO> listGroupFilterRule = Lists.newArrayList();

    private List<GroupFilterRuleDTO> listRuleListSelection = Lists.newArrayList();

    private int isClose;

    //    private String userName = BccsLoginSuccessHandler.getUserName();
    private String userName = BccsLoginSuccessHandler.getStaffDTO().getStaffCode();

    @PostConstruct
    @Secured("@")
    public void init() {
        try {
            getMetadata();
            searchByDto();
        } catch (Exception ex) {
            logger.error(ex);
            reportErrorValidateFail("frmExportNote:msgs", "", "common.error.happened");
        }
    }


    @Secured("@")
    public void search() {
        searchByDto();
    }

    @Secured("@")
    public void doRemoveItem() {
        try {
            editDto.setStatus(Const.STATUS.NO_ACTIVE);
            groupFilterRuleService.deleteByDto(editDto, userName);
            searchByDto();
            reportSuccess("frmExportNote:msgs", "mn.isdn.rule.delete.success");
            topPage();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmExportNote:msgs", "", ex.getDescription());
            topPage();
        } catch (Exception ex) {
            logger.error(ex);
            reportErrorValidateFail("frmExportNote:msgs", "", "common.error.happened");
            topPage();
        }
    }

    @Secured("@")
    public void preEdit(int index) {
        try {
            GroupFilterRuleDTO tmpDto = listResultSearch.get(index);
            editDto = new GroupFilterRuleDTO();
            editDto.setGroupFilterRuleId(tmpDto.getGroupFilterRuleId());
            editDto.setGroupFilterRuleCode(tmpDto.getGroupFilterRuleCode());
            editDto.setName(tmpDto.getName());
            editDto.setTelecomServiceId(tmpDto.getTelecomServiceId());
            editDto.setTelecomServiceName(tmpDto.getTelecomServiceName());
            editDto.setNotes(tmpDto.getNotes());
        } catch (Exception ex) {
            logger.error(ex);
            reportErrorValidateFail("frmExportNote:msgs", "", "common.error.happened");
        }
    }

    @Secured("@")
    public void save() {
        try {
            editDto.setStatus(Const.STATUS.ACTIVE);
            groupFilterRuleService.update(editDto, userName);
            searchDto = new GroupFilterRuleDTO();
            searchByDto();
            reportSuccess("frmExportNote:msgs", "mn.isdn.rule.update.success");
            topPage();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            if (DataUtil.safeEqual(ex.getKeyMsg(), "mn.isdn.rule.add.findOne.exist.code")
                    || DataUtil.safeEqual(ex.getKeyMsg(), "mn.isdn.rule.msg.special.code"))
                focusElementByRawCSSSlector(".clRuleCode");
            if (DataUtil.safeEqual(ex.getKeyMsg(), "mn.isdn.rule.add.findOne.exist.name"))
                focusElementByRawCSSSlector(".clRuleName");
            reportErrorValidateFail("frmUpdateRuleList:msgs", "", ex.getDescription());
            topPage();
        } catch (Exception ex) {
            logger.error(ex);
            reportErrorValidateFail("frmExportNote:msgs", "", "common.error.happened");
            topPage();
        }
    }

    @Secured("@")
    public void preAdd() {
        editDto = new GroupFilterRuleDTO();
    }

    @Secured("@")
    public void addRuleList() {
        try {
            editDto.setStatus(Const.STATUS.ACTIVE);
            groupFilterRuleService.create(editDto, userName);
            searchDto = new GroupFilterRuleDTO();
            searchByDto();
            if (isClose == 1) {
                reportSuccess("frmExportNote:msgs", "mn.isdn.rule.add.success");
            } else {
                editDto = new GroupFilterRuleDTO();
                reportSuccess("frmAddRuleList:msgAddRuleList", "mn.isdn.rule.add.success");
            }
            topPage();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            if (DataUtil.safeEqual(ex.getKeyMsg(), "mn.isdn.rule.add.findOne.exist.code")
                    || DataUtil.safeEqual(ex.getKeyMsg(), "mn.isdn.rule.msg.special.code"))
                focusElementByRawCSSSlector(".clRuleCode");
            if (DataUtil.safeEqual(ex.getKeyMsg(), "mn.isdn.rule.add.findOne.exist.name"))
                focusElementByRawCSSSlector(".clRuleName");
            reportErrorValidateFail("frmAddRuleList:msgAddRuleList", "", ex.getDescription());
            topPage();
        } catch (Exception ex) {
            logger.error(ex);
            reportErrorValidateFail("frmExportNote:msgs", "", "common.error.happened");
            topPage();
        }
    }

    @Secured("@")
    public void validate(GroupFilterRuleDTO groupFilterRuleDTO) {
        editDto = groupFilterRuleDTO;
        RequestContext.getCurrentInstance().update("frmUpdateRuleList:pnlUpdateRuleList");
    }

    @Secured("@")
    public void validateAdd(GroupFilterRuleDTO groupFilterRuleDTO, int isClosed) {
        isClose = isClosed;
        editDto = groupFilterRuleDTO;
        RequestContext.getCurrentInstance().update("frmAddRuleList:pnlAddRuleList");
    }

    @Secured("@")
    public void validateDeleteRuleList() {
        if (DataUtil.isNullOrEmpty(listRuleListSelection)) {
            reportErrorValidateFail("frmExportNote:msgs", "", "mn.isdn.rule.no.select.delete");
        }
    }

    @Secured("@")
    public void deleteRuleList() {
        try {
            groupFilterRuleService.deleteByListDto(listRuleListSelection, userName);
            searchByDto();
            reportSuccess("frmExportNote:msgs", "mn.isdn.rule.delete.success");
            topPage();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmExportNote:msgInvoiceList", ex.getErrorCode(), ex.getDescription());
            topPage();
        } catch (Exception ex) {
            logger.error(ex);
            reportErrorValidateFail("frmExportNote:msgInvoiceList", "", "common.error.happen");
            topPage();
        }
    }

    @Secured("@")
    public void doResetSearchRuleList() {
        searchDto = new GroupFilterRuleDTO();
    }

    private void getMetadata() {
        try {
            searchDto = new GroupFilterRuleDTO();
            editDto = new GroupFilterRuleDTO();
            listResultSearch = Lists.newArrayList();
            List<OptionSetValueDTO> listTelco;
            listTelco = optionSetValueService.getByOptionSetCode(Const.TELCO_FOR_NUMBER);
            Collections.sort(listTelco, new Comparator<OptionSetValueDTO>() {
                public int compare(OptionSetValueDTO o1, OptionSetValueDTO o2) {
                    return Long.valueOf(o1.getValue()).compareTo(Long.valueOf(o2.getValue()));
                }
            });
            optionSetValueDTOs = listTelco;
        } catch (Exception ex) {
            logger.error(ex);
            reportErrorValidateFail("frmExportNote:msgs", "", "common.error.happened");
        }
    }

    private void searchByDto() {
        try {
            listResultSearch = Lists.newArrayList();
            listResultSearch = groupFilterRuleService.searchByDto(searchDto);
            if (!DataUtil.isNullOrEmpty(listResultSearch)) {
                for (GroupFilterRuleDTO groupFilterRuleDTO : listResultSearch) {
                    for (OptionSetValueDTO dto : optionSetValueDTOs) {
                        if (Objects.equals(dto.getValue(), groupFilterRuleDTO.getTelecomServiceId().toString())) {
                            groupFilterRuleDTO.setTelecomServiceName(dto.getName());
                            break;
                        }
                    }
                }
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmUpdateRuleList:msgs", "", ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex);
            reportErrorValidateFail("frmExportNote:msgs", "", "common.error.happened");
        }
    }

    //getter and setter
    public Date getCurrentDate() {
        return Calendar.getInstance().getTime();
    }

    public GroupFilterRuleDTO getSearchDto() {
        return searchDto;
    }

    public void setSearchDto(GroupFilterRuleDTO searchDto) {
        this.searchDto = searchDto;
    }

    public List<OptionSetValueDTO> getOptionSetValueDTOs() {
        return optionSetValueDTOs;
    }

    public void setOptionSetValueDTOs(List<OptionSetValueDTO> optionSetValueDTOs) {
        this.optionSetValueDTOs = optionSetValueDTOs;
    }

    public GroupFilterRuleService getGroupFilterRuleService() {
        return groupFilterRuleService;
    }

    public void setGroupFilterRuleService(GroupFilterRuleService groupFilterRuleService) {
        this.groupFilterRuleService = groupFilterRuleService;
    }

    public ProductOfferTypeService getProductOfferTypeService() {
        return productOfferTypeService;
    }

    public void setProductOfferTypeService(ProductOfferTypeService productOfferTypeService) {
        this.productOfferTypeService = productOfferTypeService;
    }

    public List<GroupFilterRuleDTO> getListResultSearch() {
        return listResultSearch;
    }

    public void setListResultSearch(List<GroupFilterRuleDTO> listResultSearch) {
        this.listResultSearch = listResultSearch;
    }

    public GroupFilterRuleDTO getEditDto() {
        return editDto;
    }

    public void setEditDto(GroupFilterRuleDTO editDto) {
        this.editDto = editDto;
    }

    public List<GroupFilterRuleDTO> getListGroupFilterRule() {
        return listGroupFilterRule;
    }

    public void setListGroupFilterRule(List<GroupFilterRuleDTO> listGroupFilterRule) {
        this.listGroupFilterRule = listGroupFilterRule;
    }

    public List<GroupFilterRuleDTO> getListRuleListSelection() {
        return listRuleListSelection;
    }

    public void setListRuleListSelection(List<GroupFilterRuleDTO> listRuleListSelection) {
        this.listRuleListSelection = listRuleListSelection;
    }

    public int getIsClose() {
        return isClose;
    }

    public void setIsClose(int isClose) {
        this.isClose = isClose;
    }
}
