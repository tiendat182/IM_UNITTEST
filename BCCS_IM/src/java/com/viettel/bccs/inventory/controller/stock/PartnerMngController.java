package com.viettel.bccs.inventory.controller.stock;


import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.Partner;
import com.viettel.bccs.inventory.service.PartnerMngService;
import com.viettel.fw.common.util.BundleUtil;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.web.common.controller.BaseController;
import org.primefaces.context.RequestContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
@Scope("view")
@ManagedBean(name = "partnerMngController")
public class PartnerMngController extends BaseController {

    @Autowired
    private PartnerMngService partnerMngService;

    private PartnerInputSearch partnerInputSearch = new PartnerInputSearch();
    private List<Partner> partnerList;
    private boolean validateCreateOrUpdate = true;
    private boolean isCreate = true;
    private String dlgTitle;
    private String btnCreateOrUpdate;
    private String headerConfirm;
    private String messageConfirm;
    private List<Partner> partnerListSelection;
    private String partnerTypeVendorSim = "4";

    private boolean validateDelete = true;

    private Partner partnerNew;
    private Partner partnerOld;

    private Partner partnerDelete;

    @PostConstruct
    public void init() {
        try {
            partnerList = new ArrayList<Partner>();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            reportError("frmSearch", "", "common.error.happened");
        }
    }

    public void search() {
        try {
            boolean validateSearch = true;

            if (!DataUtil.isNullOrEmpty(partnerInputSearch.getPhone()) && !DataUtil.isNumber(partnerInputSearch.getPhone())) {
                validateSearch = false;
                reportError("frmSearch:phone", "", "partner.mng.phone.invalid");
            }

            if (!DataUtil.isNullOrEmpty(partnerInputSearch.getFax()) && !DataUtil.isNumber(partnerInputSearch.getFax())) {
                validateSearch = false;
                reportError("frmSearch:fax", "", "partner.mng.fax.invalid");
            }

            if (partnerInputSearch.getStartDate() != null && partnerInputSearch.getEndDate() != null) {
                if (partnerInputSearch.getStartDate().getTime() > partnerInputSearch.getEndDate().getTime()) {
                    validateSearch = false;
                    reportError("frmSearch", "", "partner.mng.start.enddate.invalid");
                }
            }
            if (validateSearch) {
                partnerList = partnerMngService.searchPartner(partnerInputSearch);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            reportError("frmSearch", "", "common.error.happened");
        }
    }
    public void onUpdate(Partner partner) {
        isCreate = false;
        partnerOld = new Partner();
        partnerNew = new Partner();
        dlgTitle = BundleUtil.getText("partner.mng.update.title");
        BeanUtils.copyProperties(partner, partnerOld);
        BeanUtils.copyProperties(partner, partnerNew);

        btnCreateOrUpdate= BundleUtil.getText("partner.mng.update.button");
        headerConfirm  = BundleUtil.getText("mn.stock.limit.financeType.header.edit");
        messageConfirm = BundleUtil.getText("common.msg.edit.confirm");
    }

    public void onCreate() {
        isCreate = true;
        dlgTitle = BundleUtil.getText("partner.mng.create.title");

        btnCreateOrUpdate= BundleUtil.getText("mn.stock.limit.createNew");
        headerConfirm  = BundleUtil.getText("mn.stock.limit.financeType.header.add");
        messageConfirm = BundleUtil.getText("common.msg.add.confirm");
        partnerNew = new Partner();
    }

    public void doCreateOrUpdate() {
        boolean isSuccess = true;
        try {
            String partnerCodeCheck = "";
            String a4KeyCheck = "";

            if (!partnerTypeVendorSim.equals(partnerNew.getPartnerType() + "")) {//reset value a4Key truoc khi them moi hoac cap nhap neu parnertype != vendor sim
                partnerNew.setA4Key("");
            }

            if (isCreate) {
                partnerCodeCheck = partnerNew.getPartnerCode();
                a4KeyCheck = partnerNew.getA4Key();
            } else {
                if (!partnerNew.getPartnerCode().equalsIgnoreCase(partnerOld.getPartnerCode())) {
                    partnerCodeCheck = partnerNew.getPartnerCode();
                }

                if (partnerNew.getA4Key() != null && !partnerNew.getA4Key().equalsIgnoreCase(partnerOld.getA4Key())) {
                    a4KeyCheck = partnerNew.getA4Key();
                }
            }
            if (!DataUtil.isNullOrEmpty(partnerCodeCheck)) {
                if (partnerMngService.countPartnerByPartnerCode(partnerCodeCheck) > 0) {
                    isSuccess = false;
                    reportError("frmCreate", "", "partner.mng.code.exist");
                }
            }
            if (!DataUtil.isNullOrEmpty(a4KeyCheck) && isSuccess) {
                if (partnerMngService.countPartnerByA4Key(a4KeyCheck) > 0) {
                    isSuccess = false;
                    reportError("frmCreate", "", "partner.mng.a4key.exist");
                }
            }

            if (isSuccess) {
                if (!partnerTypeVendorSim.equals(partnerNew.getPartnerType() + "")) {//reset value a4Key truoc khi them moi hoac cap nhap neu parnertype != vendor sim
                    partnerNew.setA4Key("");
                }

                partnerMngService.saveOrUpdate(partnerNew);
                partnerList = partnerMngService.searchPartner(partnerInputSearch);
            }

        } catch(Exception e) {
            isSuccess = false;
            logger.error(e.getMessage(), e);
            reportError("frmCreate", "", "common.error.happened");
        }

        if (!isSuccess) {
            FacesContext.getCurrentInstance().validationFailed();
        } else {
            reportSuccess("frmSearch",(isCreate ? "common.msg.success.create" : "common.msg.success.update"));
            RequestContext.getCurrentInstance().update("frmSearch");
            RequestContext.getCurrentInstance().update("lstPartner");
        }
    }


    public void validateBeforeCreateOrUpdate() {
        validateCreateOrUpdate = true;
        //code
        if (DataUtil.isNullOrEmpty(partnerNew.getPartnerCode())) {
            validateCreateOrUpdate = false;
            reportError("frmCreate:partnerCode", "", "partner.mng.code.blank");
        } else if (!validateCode(partnerNew.getPartnerCode())) {
            validateCreateOrUpdate = false;
            reportError("frmCreate:partnerCode", "", "partner.mng.code.invalid");
        }
        //name
        if (DataUtil.isNullOrEmpty(partnerNew.getPartnerName())) {
            validateCreateOrUpdate = false;
            reportError("frmCreate:partnerName", "", "partner.mng.name.blank");
        }
        //type
        if (partnerNew.getPartnerType() == null) {
            validateCreateOrUpdate = false;
            reportError("frmCreate:partnerType", "", "partner.mng.type.blank");
        }

        //address
        if (DataUtil.isNullOrEmpty(partnerNew.getAddress())) {
            validateCreateOrUpdate = false;
            reportError("frmCreate:address", "", "partner.mng.address.blank");
        }
        //phone
        if (!DataUtil.isNullOrEmpty(partnerNew.getPhone()) && !DataUtil.isNumber(partnerNew.getPhone())) {
            validateCreateOrUpdate = false;
            reportError("frmCreate:phone", "", "partner.mng.phone.invalid");
        }
        //fax
        if (!DataUtil.isNullOrEmpty(partnerNew.getFax()) && !DataUtil.isNumber(partnerNew.getFax())) {
            validateCreateOrUpdate = false;
            reportError("frmCreate:fax", "", "partner.mng.fax.invalid");
        }

        //status
        if (partnerNew.getStatus() == null) {
            validateCreateOrUpdate = false;
            reportError("frmCreate:status", "", "partner.mng.status.blank");
        }

        if (partnerNew.getStaDate() != null && partnerNew.getEndDate() != null) {
            if (partnerNew.getStaDate().getTime() > partnerNew.getEndDate().getTime()) {
                validateCreateOrUpdate = false;
                reportError("frmCreate", "", "partner.mng.start.enddate.invalid");
            }
        }

        //a4key
        if(partnerTypeVendorSim.equals(partnerNew.getPartnerType() + "") && DataUtil.isNullOrEmpty(partnerNew.getA4Key())) {
            validateCreateOrUpdate = false;
            reportError("frmCreate:a4key", "", "partner.mng.a4key.blank");
        }

        if (!validateCreateOrUpdate) {
            RequestContext.getCurrentInstance().update("pnCreate");
            FacesContext.getCurrentInstance().validationFailed();
        }
    }

    public boolean validateCode(String code) {
        String regex = "^[a-zA-Z0-9_]*$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(code).matches();
    }

    public void resetSearchFrom() {
        partnerInputSearch = new PartnerInputSearch();
    }

    public String getPartnerTypeName(Long partnerType) {
        if ("1".equals(DataUtil.safeToString(partnerType))) {
            return BundleUtil.getText("partner.mng.type1");
        }

        if ("2".equals(DataUtil.safeToString(partnerType))) {
            return BundleUtil.getText("partner.mng.type2");
        }

        if ("3".equals(DataUtil.safeToString(partnerType))) {
            return BundleUtil.getText("partner.mng.type3");
        }

        if ("4".equals(DataUtil.safeToString(partnerType))) {
            return BundleUtil.getText("partner.mng.type4");
        }

        return "";
    }

    public String getStatusName(Long status) {
        if ("1".equals(DataUtil.safeToString(status))) {
            return BundleUtil.getText("partner.mng.status1");
        }
        if ("0".equals(DataUtil.safeToString(status))) {
            return BundleUtil.getText("partner.mng.status0");
        }
        return "";
    }

    public void onChangePartnerTypeFormSearch() {
        partnerInputSearch.setA4Key("");
    }

    public void resetFormCreateOrUpdate() {
        if (isCreate) {
            partnerNew = new Partner();
        } else {
            BeanUtils.copyProperties(partnerOld, partnerNew);
        }
    }

    public void preToDelete(){
        if(DataUtil.isNullOrEmpty(partnerListSelection)){
            reportError("frmSearch", "", "partner.mng.delete.blanklist");
            FacesContext.getCurrentInstance().validationFailed();
        }
    }

    public void doDelete() {
        for (Partner p : partnerListSelection) {
            p.setStatus(2L);
        }
        try {
            partnerMngService.saveOrUpdateList(partnerListSelection);
            partnerList = partnerMngService.searchPartner(partnerInputSearch);
            reportSuccess("frmSearch", "common.msg.success.delete");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            reportError("frmSearch", "", "common.error.happened");
        }
    }

    public void doDeleteOneValidate(Partner partner) {
        partnerDelete = new Partner();
        BeanUtils.copyProperties(partner, partnerDelete);
    }

    public void doDeleteOne() {
        try {
            partnerDelete.setStatus(2L);
            partnerMngService.saveOrUpdate(partnerDelete);
            partnerList = partnerMngService.searchPartner(partnerInputSearch);
            reportSuccess("frmSearch", "common.msg.success.delete");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            reportError("frmSearch", "", "common.error.happened");
        }
    }

    public PartnerInputSearch getPartnerInputSearch() {
        return partnerInputSearch;
    }

    public void setPartnerInputSearch(PartnerInputSearch partnerInputSearch) {
        this.partnerInputSearch = partnerInputSearch;
    }
    public List<Partner> getPartnerList() {
        return partnerList;
    }

    public void setPartnerList(List<Partner> partnerList) {
        this.partnerList = partnerList;
    }

    public String getDlgTitle() {
        return dlgTitle;
    }

    public void setDlgTitle(String dlgTitle) {
        this.dlgTitle = dlgTitle;
    }

    public Partner getPartnerNew() {
        return partnerNew;
    }

    public void setPartnerNew(Partner partnerNew) {
        this.partnerNew = partnerNew;
    }

    public Partner getPartnerOld() {
        return partnerOld;
    }

    public void setPartnerOld(Partner partnerOld) {
        this.partnerOld = partnerOld;
    }

    public String getBtnCreateOrUpdate() {
        return btnCreateOrUpdate;
    }

    public void setBtnCreateOrUpdate(String btnCreateOrUpdate) {
        this.btnCreateOrUpdate = btnCreateOrUpdate;
    }

    public String getHeaderConfirm() {
        return headerConfirm;
    }

    public void setHeaderConfirm(String headerConfirm) {
        this.headerConfirm = headerConfirm;
    }

    public String getMessageConfirm() {
        return messageConfirm;
    }

    public void setMessageConfirm(String messageConfirm) {
        this.messageConfirm = messageConfirm;
    }

    public List<Partner> getPartnerListSelection() {
        return partnerListSelection;
    }

    public void setPartnerListSelection(List<Partner> partnerListSelection) {
        this.partnerListSelection = partnerListSelection;
    }
}
