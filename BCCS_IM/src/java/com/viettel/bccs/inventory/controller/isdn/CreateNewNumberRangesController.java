package com.viettel.bccs.inventory.controller.isdn;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.inventory.common.model.FileExportBean;
import com.viettel.bccs.inventory.common.util.FileUtil;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.Area;
import com.viettel.bccs.inventory.service.AreaService;
import com.viettel.bccs.inventory.service.NumberActionService;
import com.viettel.bccs.inventory.service.OptionSetValueService;
import com.viettel.bccs.inventory.service.StockNumberService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.BundleUtil;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.web.common.annotation.Security;
import com.viettel.web.common.controller.BaseController;
import org.apache.poi.ss.usermodel.Workbook;
import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by tuyendv8 on 11/19/2015.
 */
@Component
@Scope("view")
@ManagedBean(name = "createNewNumberRangesController")
public class CreateNewNumberRangesController extends BaseController {
    @Autowired
    AreaService areaService;
    @Autowired
    StockNumberService stockNumberService;
    @Autowired
    NumberActionService numberActionService;
    @Autowired
    OptionSetValueService optionSetValueService;

    private List<AreaDTO> listLocate = Lists.newArrayList();
    private List<NumberActionDTO> listRanges = Lists.newArrayList();
    private SearchNumberRangeDTO searchDTO = new SearchNumberRangeDTO();
    private List<OptionSetValueDTO> listService = Lists.newArrayList();
    boolean isAdded = false;

    @PostConstruct
    public void init() {
        try {
            List<FilterRequest> filters = Lists.newArrayList();
            FilterRequest filterRequest = new FilterRequest(Area.COLUMNS.DISTRICT.name(), FilterRequest.Operator.IS_NULL);
            filters.add(filterRequest);
            searchDTO.setUserCreate(BccsLoginSuccessHandler.getStaffDTO().getStaffCode());
            searchDTO.setIpAddress(BccsLoginSuccessHandler.getIpAddress());
            searchDTO.setOwnerId(BccsLoginSuccessHandler.getStaffDTO().getShopId());
            searchDTO.setOwnerType("1");
            listLocate = areaService.findByFilter(filters);
            listService = optionSetValueService.getByOptionSetCode("TELCO_FOR_NUMBER");
            Collections.sort(listService, new Comparator<OptionSetValueDTO>() {
                public int compare(OptionSetValueDTO o1, OptionSetValueDTO o2) {
                    return Long.valueOf(o1.getValue()).compareTo(Long.valueOf(o2.getValue()));
                }
            });
            //Khong su dung overwire ham sort trong DTO
            Collections.sort(listLocate, new Comparator<AreaDTO>() {
                public int compare(AreaDTO o1, AreaDTO o2) {
                    return o1.getName().compareToIgnoreCase(o2.getName());
                }
            });
            initObjectSearch(false);
            searchRanges();
            RequestContext.getCurrentInstance().update("frmExportNote:pnInfoExportOrder");
        } catch (LogicException ex) {
//            logger.error(ex.getMessage(), ex);
//            reportError("id message can hien thi", "ma loi", ex.getDescription());
            logger.error(ex.getMessage(), ex);
            reportError("frmExportNote:messages", "", ex.getDescription());
        } catch (Exception ex) {
//            logger.error(ex);
//            reportError("id message can hien thi", "ma loi", "Loi he thong");
            logger.error(ex.getMessage(), ex);
            reportError("frmExportNote:messages", "", "common.error.happened");
        }
    }

    @Security
    public void initObjectSearch(boolean clearService) {
        if (!isAdded) {
            if (clearService) {
                searchDTO.setServiceType(null);
            }
            searchDTO.setPstnCode(null);
            searchDTO.setAreaName(null);
            searchDTO.setAreaCode(null);
            searchDTO.setStatus(null);
            searchDTO.setStartRange("");
            searchDTO.setEndRange("");
            searchDTO.setPlanningType(null);
        }
        searchDTO.setOwnerId(BccsLoginSuccessHandler.getStaffDTO().getShopId());
        searchDTO.setOwnerType("1");
        isAdded = false;
    }

    @Security
    public void onChangeStartRange(String areaId) {
        String startRange = searchDTO.getStartRange();
        startRange = startRange.replaceAll("[^0-9]", "");
        if (!DataUtil.isNullObject(searchDTO.getServiceType()) && "1".equals(searchDTO.getServiceType())) {
            startRange = startRange.replaceAll("^0+", "");
        }
        searchDTO.setStartRange(startRange);
        RequestContext.getCurrentInstance().update(areaId);
    }

    @Security
    public void onChangeEndRange(String areaId) {
        String endRange = searchDTO.getEndRange();
        endRange = endRange.replaceAll("[^0-9]", "");
        if (!DataUtil.isNullObject(searchDTO.getServiceType()) && "1".equals(searchDTO.getServiceType())) {
            endRange = endRange.replaceAll("^0+", "");
        }
        searchDTO.setEndRange(endRange);
        RequestContext.getCurrentInstance().update(areaId);
    }

    @Security
    public void searchRanges() {
        try {
            listRanges = numberActionService.search(searchDTO);
            if (DataUtil.isNullOrEmpty(listRanges)) {
                listRanges = Lists.newArrayList();
            } else {
                Collections.sort(listRanges);
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmExportNote:messages", "", ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmExportNote:messages", "", "common.error.happened");
        }

    }

    @Security
    public void onChangeArea() {
        try {
            searchDTO.setPstnCode("");
            for (AreaDTO area : listLocate) {
                if (area.getAreaCode().equals(searchDTO.getAreaCode())) {
                    searchDTO.setAreaName(area.getName());
                    searchDTO.setPstnCode(area.getPstnCode());
                    break;
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmExportNote:messages", "", "common.error.happened");
        }
    }

    @Security
    public void validDataBeforeCreateRange() {
        if (DataUtil.isNullObject(searchDTO.getServiceType())) {
            focusElementByRawCSSSlector(".typeServiceTxt");
            reportError("messagesDlg", "", "ws.service.type.not.null");
        }
        boolean checkService = false;
        for (OptionSetValueDTO option : listService) {
            if (option.getValue().equals(searchDTO.getServiceType())) {
                checkService = true;
                break;
            }
        }
        if (!checkService) {
            focusElementByRawCSSSlector(".typeServiceTxt");
            reportError("messagesDlg", "", "ws.service.type.invalid");
        }
        long start = 0L;
        long end = 0L;
        try {
            start = Long.valueOf((DataUtil.isNullObject(searchDTO.getPstnCode()) ? "" : searchDTO.getPstnCode()) + searchDTO.getStartRange());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            focusElementByRawCSSSlector(".digitalStartRanges");
            reportError("messagesDlg", "", "ws.start.range.error");
        }

        try {
            end = Long.valueOf((DataUtil.isNullObject(searchDTO.getPstnCode()) ? "" : searchDTO.getPstnCode()) + searchDTO.getEndRange());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            focusElementByRawCSSSlector(".digitalEndRanges");
            reportError("messagesDlg", "", "ws.end.range.error");
        }
        if (start > end) {
            focusElementByRawCSSSlector(".digitalEndRanges");
            reportError("messagesDlg", "", "ws.error.range");
        }
        if ((start + "").length() != (end + "").length()) {
            focusElementByRawCSSSlector(".digitalEndRanges");
            reportError("messagesDlg", "", "ws.error.range.length");
        }
        if ("1".equals(searchDTO.getServiceType()) && ((start + "").length() < 9 || (start + "").length() > 10)) {
            focusElementByRawCSSSlector(".digitalStartRanges");
            reportError("messagesDlg", "", "ws.range.start.mobile.invalid");
        }
        if ("1".equals(searchDTO.getServiceType()) && ((end + "").length() < 9 || (end + "").length() > 10)) {
            focusElementByRawCSSSlector(".digitalEndRanges");
            reportError("messagesDlg", "", "ws.range.end.mobile.invalid");
        }
        if ((!"1".equals(searchDTO.getServiceType())) && ((start + "").length() < 8 || (start + "").length() > 11)) {
            focusElementByRawCSSSlector(".digitalStartRanges");
            reportError("messagesDlg", "", "ws.range.start.homephone.pstn.invalid");
        }
        if ((!"1".equals(searchDTO.getServiceType())) && ((end + "").length() < 8 || (end + "").length() > 11)) {
            focusElementByRawCSSSlector(".digitalEndRanges");
            reportError("messagesDlg", "", "ws.range.end.homephone.pstn.invalid");
        }
        if (!"1".equals(searchDTO.getServiceType()) && DataUtil.isNullObject(searchDTO.getAreaCode())) {
            focusElementByRawCSSSlector(".cbxLocality");
            reportError("messagesDlg", "", "common.locality.require");
        }
        if (end - start + 1 > 1000000L) {
            focusElementByRawCSSSlector(".digitalEndRanges");
            reportError("messagesDlg", "", "ws.number.per.range.over");
        }
    }

    public List<OptionSetValueDTO> getListService() {
        return listService;
    }

    public void setListService(List<OptionSetValueDTO> listService) {
        this.listService = listService;
    }

    @Security
    public void createNewNumberRanges(boolean close) {
        try {
//            NumberActionDTO logAction = new NumberActionDTO();
//            logAction.setProdOfferTypeId(Long.valueOf(searchDTO.getServiceType()));
//            logAction.setFromIsdn((!DataUtil.isNullObject(searchDTO.getPstnCode()) ? searchDTO.getPstnCode() : "") + searchDTO.getStartRange());
//            logAction.setToIsdn((!DataUtil.isNullObject(searchDTO.getPstnCode()) ? searchDTO.getPstnCode() : "") + searchDTO.getEndRange());
//            logAction.setUserCreate(searchDTO.getUserCreate());
//            logAction.setUserIpAddress(searchDTO.getIpAddress());
//            logAction.setCreateDate(new Date());
//            logAction.setServiceType(searchDTO.getPlanningType());
//            logAction.setActionType("1");
            NumberActionDTO logAction = stockNumberService.insertBatch(searchDTO);
//            numberActionService.create(logAction);
            listRanges = Lists.newArrayList();
            listRanges.add(logAction);
//            boolean check = numberActionService.checkOverlap(logAction);//true khong trung chom
//            List<StockNumberDTO> listStock = Lists.newArrayList();
//            if (check) {
//                numberActionService.create(logAction);
//                listRanges.add(logAction);
//                stockNumberService.insertBatch(searchDTO);
//            } else {
//                //log message error
//                System.out.println("trung chom");
//            }
//            initObjectSearch();
            if (close) {
                reportSuccess("frmExportNote:messages", "mn.invoice.invoiceType.success.add");
                RequestContext.getCurrentInstance().execute("setTimeout(function() {$('.typeServiceTxt').focus();}, 350);");
            } else {
                reportSuccess("messagesDlg", "mn.invoice.invoiceType.success.add");

            }
            initObjectSearch(true);
            isAdded = true;
            searchRanges();
            topPage();
            RequestContext.getCurrentInstance().update("frmExportNote:pnInfoExportOrder");
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            if (ex.getDescription().equals(BundleUtil.getText("ws.service.type.not.null"))) {
                focusElementByRawCSSSlector(".typeServiceTxt");
            } else if (ex.getDescription().equals(BundleUtil.getText("ws.service.type.invalid"))) {
                focusElementByRawCSSSlector(".typeServiceTxt");
            } else if (ex.getDescription().equals(BundleUtil.getText("ws.start.range.error"))) {
                focusElementByRawCSSSlector(".digitalStartRanges");
            } else if (ex.getDescription().equals(BundleUtil.getText("ws.end.range.error"))) {
                focusElementByRawCSSSlector(".digitalEndRanges");
            } else if (ex.getDescription().equals(BundleUtil.getText("ws.error.range"))) {
                focusElementByRawCSSSlector(".digitalEndRanges");
            } else if (ex.getDescription().equals(BundleUtil.getText("ws.error.range.length"))) {
                focusElementByRawCSSSlector(".digitalEndRanges");
            } else if (ex.getDescription().equals(BundleUtil.getText("ws.range.mobile.invalid"))) {
                focusElementByRawCSSSlector(".digitalEndRanges");
            } else if (ex.getDescription().equals(BundleUtil.getText("ws.range.homephone.pstn.invalid"))) {
                focusElementByRawCSSSlector(".digitalEndRanges");
            } else if (ex.getDescription().equals(BundleUtil.getText("common.locality.require"))) {
                focusElementByRawCSSSlector(".cbxLocality");
            } else if (ex.getDescription().equals(BundleUtil.getText("ws.number.per.range.over"))) {
                focusElementByRawCSSSlector(".digitalEndRanges");
            } else if (ex.getDescription().equals(BundleUtil.getText("ws.area.invalid"))) {
                focusElementByRawCSSSlector(".cbxLocality");
            } else if (ex.getDescription().equals(BundleUtil.getText("ws.planning.type.not.null"))) {
                focusElementByRawCSSSlector(".typeDigitalNeedQH");
            } else if (ex.getDescription().equals(BundleUtil.getText("ws.planning.type.invalid"))) {
                focusElementByRawCSSSlector(".typeDigitalNeedQH");
            } else if (ex.getDescription().equals(BundleUtil.getText("ws.range.number.overlap"))) {
                focusElementByRawCSSSlector(".digitalEndRanges");
            }
            reportError("messagesDlg", "", ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("messagesDlg", "", "common.error.happened");
        }

    }

    @Security
    public String getStatusName(String status) {
        if (!DataUtil.isNullObject(status)) {
            if ("0".equals(status)) {
                return BundleUtil.getText("range.create.inprocess");
            } else if ("1".equals(status)) {
                return BundleUtil.getText("range.create.finished");
            } else if ("2".equals(status)) {
                return BundleUtil.getText("range.create.error");
            }
        }
        return "";
    }

    @Security
    public Long quantity(String from, String to) {
        try {
            long fromInt = Long.valueOf(from.replaceAll("^(0)+", ""));
            long toInt = Long.valueOf(to.replaceAll("^(0)+", ""));
            return (toInt - fromInt + 1);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmExportNote:messages", "", "common.error.happened");
        }
        return null;
    }

    @Security
    public String getServiceName(String serviceId) {
        for (OptionSetValueDTO op : listService) {
            if (op.getValue().equals(serviceId)) {
                return op.getName();
            }
        }
        return "";
    }

    @Secured("@")
    public void exportIsdn() {
        try {
            FileExportBean bean = new FileExportBean();
            bean.setTempalatePath(FileUtil.getTemplatePath());
            bean.setTemplateName("exportRangeIsdnTemplate.xls");
            bean.putValue("lstData", listRanges);
            bean.putValue("controller", this);
            bean.putValue("createUser", BccsLoginSuccessHandler.getStaffDTO().getName());
            bean.putValue("createDate", DateUtil.date2ddMMyyyyString(new Date()));

            Workbook workbook = FileUtil.exportWorkBook(bean);

            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + "rangeIsdn.xls" + "\"");
            externalContext.setResponseContentType("application/vnd.ms-excel");
            workbook.write(externalContext.getResponseOutputStream());
            facesContext.responseComplete();
            return;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmExportNote:messages", "", "common.error.happen");
        }
    }


    public List<AreaDTO> getListLocate() {
        return listLocate;
    }

    public SearchNumberRangeDTO getSearchDTO() {
        return searchDTO;
    }

    public void setSearchDTO(SearchNumberRangeDTO searchDTO) {
        this.searchDTO = searchDTO;
    }

    public void setListLocate(List<AreaDTO> listLocate) {
        this.listLocate = listLocate;
    }

    public List<NumberActionDTO> getListRanges() {
        return listRanges;
    }

    public void setListRanges(List<NumberActionDTO> listRanges) {
        this.listRanges = listRanges;
    }
}