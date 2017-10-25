package com.viettel.bccs.inventory.controller.stock;

import com.viettel.bccs.fw.controller.InventoryController;
import com.viettel.bccs.inventory.dto.LookupInvoiceUsedDTO;
import com.viettel.bccs.inventory.service.InvoiceUsedService;
import com.viettel.fw.common.util.BundleUtil;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.dto.BaseMessage;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.util.*;
import java.util.prefs.BackingStoreException;
import java.util.regex.Pattern;

/**
 * Created by pham on 9/26/2016.
 */

@ManagedBean
@Component
@Scope("view")
public class LookupInvoiceUsedController extends InventoryController {
    private Date fromDate;
    private Date toDate;
    private String fromInvoice;
    private String toInvoice;
    private LookupInvoiceUsedDTO lookupInvoiceUsedDTO;
    private List<LookupInvoiceUsedDTO> lookupInvoiceUsedDTOs;
    private List<LookupInvoiceUsedDTO> listserial = new ArrayList<>();
    private List<LookupInvoiceUsedDTO> listAllSerial = new ArrayList<>();
    private LookupInvoiceUsedDTO selectserial;
    @Autowired
    private InvoiceUsedService invoiceUsedService;

    private static final Integer MAX_RESULT = 20;

    @PostConstruct
    public void init() {
        try {
            lookupInvoiceUsedDTOs = new ArrayList<LookupInvoiceUsedDTO>();
            fromDate = getSysdateFromDB();
            toDate = getSysdateFromDB();
            listAllSerial = invoiceUsedService.getAllSerial();
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,null,getText("common.error.happened")));
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }
    public void searchInvoiceUsed() {
        lookupInvoiceUsedDTOs = new ArrayList<>();
        try {
            BaseMessage message = validate();
            if (message != null && !message.isSuccess()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, message.getDescription()));
            } else if(message == null){
                return ;
            } else {
                if (!DataUtil.isNullOrEmpty(selectserial.getSerial()) && fromDate != null && toDate != null) {
                    lookupInvoiceUsedDTO = invoiceUsedService.getInvoiceUsed(selectserial.getSerial(), fromDate, toDate);
                    if (lookupInvoiceUsedDTO != null) {
                        lookupInvoiceUsedDTOs.add(lookupInvoiceUsedDTO);
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, getText("lookup.invoice.used.result.error")));
                        return;
                    }
                }
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,null,getText("common.error.happened")));
            logger.debug(e.getMessage(), e);
        }
    }

    public List<LookupInvoiceUsedDTO> searchSerial(String input) throws Exception {
            listserial = new ArrayList<LookupInvoiceUsedDTO>();
            if (listAllSerial == null || listAllSerial.isEmpty()) {
                return listserial;
            }

            if (input == null || "".equals(input.trim())) {
                if (listAllSerial.size() > MAX_RESULT) {
                    listserial = listAllSerial.subList(0,MAX_RESULT);
                } else {
                    listserial = listAllSerial;
                }
            } else {
                for (LookupInvoiceUsedDTO data : listAllSerial) {
                    if(data.getSerial().toLowerCase().contains(input.toLowerCase())
                            || data.getName().toLowerCase().contains(input.toLowerCase())) {
                        listserial.add(data);
                    }

                    if (listserial.size() >= MAX_RESULT) break;
                }
            }

            return listserial;
        }
     public void onSelectItem(SelectEvent event){
         LookupInvoiceUsedDTO  invoiceUsedDTO = (LookupInvoiceUsedDTO)event.getObject();
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"::add invoid::" + invoiceUsedDTO.getSerial() +"::name::"+invoiceUsedDTO.getName()," "));
     }
    private BaseMessage validate() {
        BaseMessage message = new BaseMessage(false);
        boolean isValidateOK = true;
        if (selectserial == null ||selectserial.getSerial().trim() == "" || selectserial.getSerial().trim().length() > 30) {
            message.setDescription(BundleUtil.getText("lookup.invoice.used.serial.error"));
            //reportError("frmLookupInvoiceUsed:serial", "", message.getDescription());
            FacesContext.getCurrentInstance().addMessage("frmLookupInvoiceUsed:serial", new FacesMessage(FacesMessage.SEVERITY_ERROR, null, message.getDescription()));
            isValidateOK = false;
        }
        if (fromDate == null) {
            message.setDescription(BundleUtil.getText("lookup.invoice.used.date.label.message.empty"));
            //reportError("frmLookupInvoiceUsed:startDate", "", message.getDescription());
            FacesContext.getCurrentInstance().addMessage("frmLookupInvoiceUsed:startDate", new FacesMessage(FacesMessage.SEVERITY_ERROR, null, message.getDescription()));
            isValidateOK = false;
        }
        if (toDate == null) {
            message.setDescription(BundleUtil.getText("lookup.invoice.used.date.label.message.empty"));
            //reportError("frmLookupInvoiceUsed:endDate", "", message.getDescription());
            FacesContext.getCurrentInstance().addMessage("frmLookupInvoiceUsed:endDate", new FacesMessage(FacesMessage.SEVERITY_ERROR, null, message.getDescription()));
            isValidateOK = false;
        }

        if (toDate != null && fromDate != null && fromDate.compareTo(toDate) > 0) {
            message.setDescription(BundleUtil.getText("lookup.invoice.used.from.greater.than.to.error"));
            //reportError("frmLookupInvoiceUsed:startDate", "", message.getDescription());
            FacesContext.getCurrentInstance().addMessage("frmLookupInvoiceUsed:startDate", new FacesMessage(FacesMessage.SEVERITY_ERROR, null, message.getDescription()));
            isValidateOK = false;
        }

        if (!isValidateOK) return null;
        message.setSuccess(true);
        return message;
    }


    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public String getFromInvoice() {
        return fromInvoice;
    }

    public void setFromInvoice(String fromInvoice) {
        this.fromInvoice = fromInvoice;
    }

    public String getToInvoice() {
        return toInvoice;
    }

    public void setToInvoice(String toInvoice) {
        this.toInvoice = toInvoice;
    }

    public LookupInvoiceUsedDTO getLookupInvoiceUsedDTO() {
        return lookupInvoiceUsedDTO;
    }

    public void setLookupInvoiceUsedDTO(LookupInvoiceUsedDTO lookupInvoiceUsedDTO) {
        this.lookupInvoiceUsedDTO = lookupInvoiceUsedDTO;
    }

    public List<LookupInvoiceUsedDTO> getListserial() {
        return listserial;
    }

    public void setListserial(List<LookupInvoiceUsedDTO> listserial) {
        this.listserial = listserial;
    }

    public LookupInvoiceUsedDTO getSelectserial() {
        return selectserial;
    }

    public void setSelectserial(LookupInvoiceUsedDTO selectserial) {
        this.selectserial = selectserial;
    }

    public List<LookupInvoiceUsedDTO> getLookupInvoiceUsedDTOs() {
        return lookupInvoiceUsedDTOs;
    }

    public void setLookupInvoiceUsedDTOs(List<LookupInvoiceUsedDTO> lookupInvoiceUsedDTOs) {
        this.lookupInvoiceUsedDTOs = lookupInvoiceUsedDTOs;
    }

    public List<LookupInvoiceUsedDTO> getListAllSerial() {
        return listAllSerial;
    }

    public void setListAllSerial(List<LookupInvoiceUsedDTO> listAllSerial) {
        this.listAllSerial = listAllSerial;
    }
}
