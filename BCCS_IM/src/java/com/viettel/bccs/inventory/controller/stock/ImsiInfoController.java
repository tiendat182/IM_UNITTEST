package com.viettel.bccs.inventory.controller.stock;

import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.inventory.dto.ImsiInfoDTO;
import com.viettel.bccs.inventory.dto.ImsiInfoInputSearch;
import com.viettel.bccs.inventory.service.ImsiInfoService;
import com.viettel.fw.common.util.BundleUtil;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.web.common.annotation.Security;
import com.viettel.web.common.controller.BaseController;
import org.primefaces.context.RequestContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by HoangAnh on 5/29/2017.
 */

@Component
@Scope("view")
@ManagedBean(name = "imsiInfoController")
public class ImsiInfoController extends BaseController{

    @Autowired
    private ImsiInfoService imsiInfoService;
    private List<ImsiInfoDTO> imsiInfoDTOs;
    private ImsiInfoInputSearch imsiInfoInputSearch;
    private String dlgTitle;
    private ImsiInfoDTO imsiInfoDTO;
    boolean validateCreate = true;
    boolean validateDelete = true;
    boolean validateUpdate = true;

    @PostConstruct
    public void init() {
        try {
            imsiInfoInputSearch = new ImsiInfoInputSearch();
            imsiInfoDTOs = new ArrayList<ImsiInfoDTO>();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            reportError("frmSearch", "", "common.error.happened");
        }
    }

    @Secured("@")
    public void search() {
        try {

            boolean validateSearch = true;
            String imsiFrom = DataUtil.safeToString(imsiInfoInputSearch.getImsiFrom());
            String imsiTo   = DataUtil.safeToString(imsiInfoInputSearch.getImsiTo());
            if (!DataUtil.isNullOrEmpty(imsiFrom) && !DataUtil.isNumber(imsiFrom)) {
                validateSearch = false;
                reportError("frmSearch:imsiFrom", "", "imsi.info.imsi.from.invalid");
            }

            if (!DataUtil.isNullOrEmpty(imsiTo) && !DataUtil.isNumber(imsiTo)) {
                validateSearch = false;
                reportError("frmSearch:imsiTo", "", "imsi.info.imsi.to.invalid");
            }

            if (validateSearch && !DataUtil.isNullOrEmpty(imsiFrom) && !DataUtil.isNullOrEmpty(imsiTo)) {//truong hop co nhap dai imsi, kiem tra so dau dai va cuoi dai
                if (Long.valueOf(imsiFrom) > Long.valueOf(imsiTo)) {
                    validateSearch = false;
                    reportError("frmSearch", "", "imsi.info.imsi.from.to.invalid");
                }
            }

            if (imsiInfoInputSearch.getFromDate() != null && imsiInfoInputSearch.getToDate() != null) {
                if (imsiInfoInputSearch.getFromDate().getTime() > imsiInfoInputSearch.getToDate().getTime()) {
                    validateSearch = false;
                    reportError("frmSearch", "", "imsi.info.from.end.date.invalid");
                }
            }

            if (!DataUtil.isNullOrEmpty(imsiInfoInputSearch.getDocNo())) {
                Pattern pattern = Pattern.compile("[\\w\\d_\\-\\/\\\\]+");
                if(!pattern.matcher(DataUtil.convertCharacter(imsiInfoInputSearch.getDocNo())).matches()){
                    FacesContext.getCurrentInstance().addMessage("frmSearch:docNo", new FacesMessage(FacesMessage.SEVERITY_ERROR, null, getText("imsi.info.doc.no.invalid")));
                    validateSearch = false;
                }
            }

            if (validateSearch) {
                imsiInfoDTOs = imsiInfoService.search(imsiInfoInputSearch);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            reportError("frmSearch", "", "common.error.happened");
        }
    }


    @Security
    public void onChangeImsiFrom(String areaId) {
        String fromImsi = imsiInfoInputSearch.getImsiFrom();
        fromImsi        = fromImsi.replaceAll("[^0-9]", "");
        imsiInfoInputSearch.setImsiFrom(fromImsi);
        RequestContext.getCurrentInstance().update(areaId);
    }

    @Security
    public void onChangeImsiTo(String areaId) {
        String toImsi = imsiInfoInputSearch.getImsiTo();
        toImsi        = toImsi.replaceAll("[^0-9]", "");
        imsiInfoInputSearch.setImsiTo(toImsi);
        RequestContext.getCurrentInstance().update(areaId);
    }


    @Security
    public void onChangeImsiFromAddForm(String areaId) {
        String fromImsi = imsiInfoDTO.getFromImsi();
        fromImsi        = fromImsi.replaceAll("[^0-9]", "");
        imsiInfoDTO.setFromImsi(fromImsi);
        RequestContext.getCurrentInstance().update(areaId);
    }

    @Security
    public void onChangeImsiToAddForm(String areaId) {
        String toImsi = imsiInfoDTO.getToImsi();
        toImsi        = toImsi.replaceAll("[^0-9]", "");
        imsiInfoDTO.setToImsi(toImsi);
        RequestContext.getCurrentInstance().update(areaId);
    }


    public void resetSearchFrom() {
        imsiInfoInputSearch = new ImsiInfoInputSearch();
    }

    public String getStatusName(String status) {
        if ("1".equals(status)) {
            return BundleUtil.getText("imsi.info.status.1");
        }
        if ("2".equals(status)) {
            return BundleUtil.getText("imsi.info.status.2");
        }
        if ("3".equals(status)) {
            return BundleUtil.getText("imsi.info.status.3");
        }
        if ("4".equals(status)) {
            return BundleUtil.getText("imsi.info.status.4");
        }
        return "";
    }

    public void onCreate() {
        validateCreate = true;
        imsiInfoDTO = new ImsiInfoDTO();
        dlgTitle    = BundleUtil.getText("imsi.info.create");
    }

    public void doCreate() {
        boolean isSuccess = true;
        if (validateCreate) {
            try {
                imsiInfoDTO.setStatus("1");
                imsiInfoDTO.setUserCreate(BccsLoginSuccessHandler.getUserName());
                imsiInfoDTO.setLastUpdateUser(BccsLoginSuccessHandler.getUserName());
                imsiInfoService.insertBatch(imsiInfoDTO);
                imsiInfoDTOs = imsiInfoService.search(imsiInfoInputSearch);
            } catch (Exception e) {
                FacesContext.getCurrentInstance().validationFailed();
                logger.error(e.getMessage(), e);
                reportError("pnCreateImsiInfo", "", "common.error.process.happened");
                RequestContext.getCurrentInstance().update("pnCreateImsiInfo");
                isSuccess = false;
            }
            if (isSuccess) {
                reportSuccess("frmSearch","common.msg.success.create");
                RequestContext.getCurrentInstance().update("frmSearch");
                RequestContext.getCurrentInstance().update("lstImsiInfo");
            }
        }
    }


    public void onDelete() {
        validateDelete  = true;
        imsiInfoDTO     = new ImsiInfoDTO();
        dlgTitle        = BundleUtil.getText("imsi.info.delete");
    }

    public void doDelete() {
        boolean isSuccess = true;
        if (validateDelete) {
            try {
                imsiInfoService.deleteImsi(imsiInfoDTO.getFromImsi(), imsiInfoDTO.getToImsi());
                imsiInfoDTOs = imsiInfoService.search(imsiInfoInputSearch);
            } catch (Exception e) {
                FacesContext.getCurrentInstance().validationFailed();
                logger.error(e.getMessage(), e);
                reportError("pnDeleteImsiInfo", "", "common.error.process.happened");
                RequestContext.getCurrentInstance().update("frmCreateImsiInfo");
                isSuccess = false;
            }
            if (isSuccess) {
                reportSuccess("frmSearch","common.msg.success.delete");
                RequestContext.getCurrentInstance().update("lstImsiInfo");
                RequestContext.getCurrentInstance().update("frmSearch");
            }
        }
    }

    public void validateBeforeDelete() {
        validateDelete  = true;
        String imsiFrom = DataUtil.safeToString(imsiInfoDTO.getFromImsi());
        String imsiTo   = DataUtil.safeToString(imsiInfoDTO.getToImsi());
        if(!validateImsi(imsiFrom)) {
            validateDelete = false;
            reportError("frmDeleteImsiInfo:imsiFrom", "", "imsi.info.imsi.invalid");
        }

        if (!validateImsi(imsiTo)) {
            validateDelete = false;
            reportError("frmDeleteImsiInfo:imsiTo", "", "imsi.info.imsi.invalid");
        }
        if (validateDelete) {//truong hop co nhap dai imsi, kiem tra so dau dai va cuoi dai
            if (Long.valueOf(imsiFrom) > Long.valueOf(imsiTo)) {
                validateDelete   = false;
                reportError("pnDeleteImsiInfo", "", "imsi.info.imsi.from.to.invalid");
            }
        }

        if (validateDelete) {//call service check from-to imsi
            try {
                Long countImsiFromDB = imsiInfoService.countImsiRangeToCheckDelete(imsiInfoDTO.getFromImsi(), imsiInfoDTO.getToImsi());
                Long countImsiFromUI = Long.valueOf(imsiTo) - Long.valueOf(imsiFrom)  + 1;
                if (!countImsiFromUI.equals(countImsiFromDB)) {
                    validateDelete = false;
                    reportError("pnDeleteImsiInfo", "", getTextParam("imsi.info.imsi.from.to.delete.invalid", imsiFrom, imsiTo ));
                }
            } catch (Exception e) {
                validateDelete = false;
                logger.error(e.getMessage(), e);
                reportError("pnDeleteImsiInfo", "", "common.error.process.happened");
            }
        }

        if (!validateDelete) {
            RequestContext.getCurrentInstance().update("pnDeleteImsiInfo");
            FacesContext.getCurrentInstance().validationFailed();
        }
    }

    public void validateBeforeCreate() {
        validateCreate = true;
        String imsiFrom = DataUtil.safeToString(imsiInfoDTO.getFromImsi());
        String imsiTo   = DataUtil.safeToString(imsiInfoDTO.getToImsi());


        if(!validateImsi(imsiFrom)) {
            validateCreate = false;
            reportError("frmCreateImsiInfo:imsiFrom", "", "imsi.info.imsi.invalid");
        }

        if (!validateImsi(imsiTo)) {
            validateCreate = false;
            reportError("frmCreateImsiInfo:imsiTo", "", "imsi.info.imsi.invalid");
        }
        if (validateCreate) {//truong hop co nhap dai imsi, kiem tra so dau dai va cuoi dai
            if (Long.valueOf(imsiFrom) > Long.valueOf(imsiTo)) {
                validateCreate   = false;
                reportError("pnCreateImsiInfo", "", "imsi.info.imsi.from.to.invalid");
            }
        }

        //doc_no
        if (!DataUtil.isNullOrEmpty(imsiInfoDTO.getDocNo())) {
            Pattern pattern = Pattern.compile("[\\w\\d_\\-\\/\\\\]+");
            if(!pattern.matcher(DataUtil.convertCharacter(imsiInfoDTO.getDocNo())).matches()){
                reportError("frmCreateImsiInfo:docNo", "", "imsi.info.doc.no.invalid");
                validateCreate = false;
            } else if (imsiInfoDTO.getDocNo().length() > 20) {
                reportError("frmCreateImsiInfo:docNo", "", "imsi.info.doc.no.maxlength");
                validateCreate = false;
            }
        } else {
            reportError("frmCreateImsiInfo:docNo", "", "imsi.info.doc.no.blank");
            validateCreate = false;
        }

        if (imsiInfoDTO.getStartDate() == null) {
            reportError("frmCreateImsiInfo:fromDate", "", "imsi.info.start.date.blank");
            validateCreate = false;
        }

        if (imsiInfoDTO.getEndDate() == null) {
            reportError("frmCreateImsiInfo:toDate", "", "imsi.info.end.date.blank");
            validateCreate = false;
        }

        if (imsiInfoDTO.getStartDate() != null && imsiInfoDTO.getEndDate() != null
                && imsiInfoDTO.getStartDate().getTime() > imsiInfoDTO.getEndDate().getTime()) {
            validateCreate = false;
            reportError("pnCreateImsiInfo", "", "imsi.info.from.end.date.invalid");
        }

        if (validateCreate) {//call service check from-to imsi
            try {
                if (imsiInfoService.countImsiRange(imsiInfoDTO.getFromImsi(), imsiInfoDTO.getToImsi()) > 0) {
                    validateCreate = false;
                    reportError("pnCreateImsiInfo", "", getTextParam("imsi.info.imsi.from.to.exist", imsiFrom, imsiTo ));
                }
            } catch (Exception e) {
                validateCreate = false;
                logger.error(e.getMessage(), e);
                reportError("pnCreateImsiInfo", "", "common.error.process.happened");
            }
        }


        if (!validateCreate) {
            RequestContext.getCurrentInstance().update("pnCreateImsiInfo");
            FacesContext.getCurrentInstance().validationFailed();
        }
    }

    public void resetFormAdd() {
        validateCreate = true;
        imsiInfoDTO = new ImsiInfoDTO();
    }

    public void resetFormDelete() {
        validateDelete = true;
        imsiInfoDTO = new ImsiInfoDTO();
    }


    public void onUpdate(ImsiInfoDTO imsiInfoDTOUpdate) {
        imsiInfoDTO     = new ImsiInfoDTO();
        BeanUtils.copyProperties(imsiInfoDTOUpdate, imsiInfoDTO);
        dlgTitle    = BundleUtil.getText("imsi.info.update");
    }

    public void validateBeforeUpdate() {
        String imsiFrom = DataUtil.safeToString(imsiInfoDTO.getFromImsi());
        String imsiTo   = DataUtil.safeToString(imsiInfoDTO.getToImsi());
        validateUpdate = true;
        //doc_no
        if (!DataUtil.isNullOrEmpty(imsiInfoDTO.getDocNo())) {
            Pattern pattern = Pattern.compile("[\\w\\d_\\-\\/\\\\]+");
            if(!pattern.matcher(DataUtil.convertCharacter(imsiInfoDTO.getDocNo())).matches()){
                reportError("frmUpdateImsiInfo:docNo", "", "imsi.info.doc.no.invalid");
                validateUpdate = false;
            } else if (imsiInfoDTO.getDocNo().length() > 20) {
                reportError("frmUpdateImsiInfo:docNo", "", "imsi.info.doc.no.maxlength");
                validateUpdate = false;
            }
        } else {
            reportError("frmUpdateImsiInfo:docNo", "", "imsi.info.doc.no.blank");
            validateUpdate = false;
        }

        if (imsiInfoDTO.getStartDate() == null) {
            reportError("frmUpdateImsiInfo:fromDate", "", "imsi.info.start.date.blank");
            validateUpdate = false;
        }

        if (imsiInfoDTO.getEndDate() == null) {
            reportError("frmUpdateImsiInfo:toDate", "", "imsi.info.end.date.blank");
            validateUpdate = false;
        }

        if (imsiInfoDTO.getStartDate() != null && imsiInfoDTO.getEndDate() != null
                && imsiInfoDTO.getStartDate().getTime() > imsiInfoDTO.getEndDate().getTime()) {
            validateUpdate = false;
            reportError("pnUpdateImsiInfo", "", "imsi.info.from.end.date.invalid");
        }

        if (validateUpdate) {//call service check from-to imsi
            try {
                Long countImsiFromDB = imsiInfoService.countImsiRangeToCheckDelete(imsiInfoDTO.getFromImsi(), imsiInfoDTO.getToImsi());
                Long countImsiFromUI = Long.valueOf(imsiTo) - Long.valueOf(imsiFrom)  + 1;
                if (!countImsiFromUI.equals(countImsiFromDB)) {
                    validateUpdate = false;
                    reportError("pnUpdateImsiInfo", "", getTextParam("imsi.info.imsi.from.to.delete.invalid", imsiFrom, imsiTo ));
                }
            } catch (Exception e) {
                validateUpdate = false;
                logger.error(e.getMessage(), e);
                reportError("pnUpdateImsiInfo", "", "common.error.process.happened");
            }
        }


        if (!validateUpdate) {
            RequestContext.getCurrentInstance().update("pnUpdateImsiInfo");
            FacesContext.getCurrentInstance().validationFailed();
        }
    }

    public void doUpdate() {
        boolean isSuccess = true;
        if (validateUpdate) {
            try {
                imsiInfoDTO.setLastUpdateUser(BccsLoginSuccessHandler.getUserName());
                imsiInfoService.updateBatch(imsiInfoDTO);
                imsiInfoDTOs = imsiInfoService.search(imsiInfoInputSearch);
            } catch (Exception e) {
                FacesContext.getCurrentInstance().validationFailed();
                logger.error(e.getMessage(), e);
                reportError("pnUpdateImsiInfo", "", "common.error.process.happened");
                RequestContext.getCurrentInstance().update("pnUpdateImsiInfo");
                isSuccess = false;
            }
            if (isSuccess) {
                reportSuccess("frmSearch","common.msg.success.update");
                RequestContext.getCurrentInstance().update("lstImsiInfo");
                RequestContext.getCurrentInstance().update("frmSearch");
            }
        }
    }


    public boolean validateImsi(String imsi) {
        imsi = DataUtil.safeToString(imsi).trim();
        if (DataUtil.isNullOrEmpty(imsi)) return false;
        if (!DataUtil.isNumber(imsi)) return false;
        if (imsi.trim().length() != 15) return false;
        String validPrefixesImsi = "45204";
        String prefixesImsi = imsi.substring(0,5);
        return validPrefixesImsi.equals(prefixesImsi);
    }

    /* ************************ SETTERS AND GETTERS ******************** */
    public ImsiInfoService getImsiInfoService() {
        return imsiInfoService;
    }

    public void setImsiInfoService(ImsiInfoService imsiInfoService) {
        this.imsiInfoService = imsiInfoService;
    }

    public List<ImsiInfoDTO> getImsiInfoDTOs() {
        return imsiInfoDTOs;
    }

    public void setImsiInfoDTOs(List<ImsiInfoDTO> imsiInfoDTOs) {
        this.imsiInfoDTOs = imsiInfoDTOs;
    }

    public ImsiInfoInputSearch getImsiInfoInputSearch() {
        return imsiInfoInputSearch;
    }

    public void setImsiInfoInputSearch(ImsiInfoInputSearch imsiInfoInputSearch) {
        this.imsiInfoInputSearch = imsiInfoInputSearch;
    }

    public String getDlgTitle() {
        return dlgTitle;
    }

    public void setDlgTitle(String dlgTitle) {
        this.dlgTitle = dlgTitle;
    }

    public ImsiInfoDTO getImsiInfoDTO() {
        return imsiInfoDTO;
    }

    public void setImsiInfoDTO(ImsiInfoDTO imsiInfoDTO) {
        this.imsiInfoDTO = imsiInfoDTO;
    }
}
