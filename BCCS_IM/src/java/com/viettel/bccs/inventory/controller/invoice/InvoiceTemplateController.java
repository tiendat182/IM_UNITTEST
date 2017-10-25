package com.viettel.bccs.inventory.controller.invoice;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.InvoiceTemplateDTO;
import com.viettel.bccs.inventory.dto.InvoiceTemplateTypeDTO;
import com.viettel.bccs.inventory.service.InvoiceTemplateService;
import com.viettel.bccs.inventory.service.InvoiceTemplateTypeService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.web.common.controller.BaseController;
import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.util.List;

/**
 * Created by tuyendv8 on 11/10/2015.
 */
@Component
@Scope("view")
@ManagedBean
public class InvoiceTemplateController extends BaseController {
    @Autowired
    InvoiceTemplateService invoiceTemplateSv;

    @Autowired
    InvoiceTemplateTypeService invoiceTemplateTypeSv;

    private InvoiceTemplateDTO forSearchInvoiceTemplate;
    private InvoiceTemplateDTO addInvoiceTemplate;
    private InvoiceTemplateDTO editInvoiceTemplate;
    private List<InvoiceTemplateDTO> invoiceTemplateList = Lists.newArrayList();
    private List<InvoiceTemplateTypeDTO> invoiceTemplateTypeList = Lists.newArrayList();
    private final static String DAYNUM_REGEX = "^[0-9]{0,10}$";
    private boolean render;
    private InvoiceTemplateDTO invoiceSearch = new InvoiceTemplateDTO();


    @PostConstruct
    @Secured("@")
    public void init() {
        try {
            createData();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happened");
        }
    }

    private void createData() throws Exception {
        render = false;
        forSearchInvoiceTemplate = new InvoiceTemplateDTO();
        addInvoiceTemplate = new InvoiceTemplateDTO();
        editInvoiceTemplate = new InvoiceTemplateDTO();
        invoiceTemplateList = Lists.newArrayList();
        invoiceSearch.setOwnerId(BccsLoginSuccessHandler.getStaffDTO().getShopId());
        invoiceTemplateTypeList = invoiceTemplateTypeSv.getInvoiceTemplateType();
//        invoiceTemplateList = invoiceTemplateSv.getAllReceiveInvoiceTemplateByShopId(BccsLoginSuccessHandler.getStaffDTO().getShopId());
        invoiceTemplateList = invoiceTemplateSv.search(invoiceSearch);
    }

    @Secured("@")
    public String getInvoiceTemplateType(Long invoiceTemplateTypeId) {
        String invoiceTemplateConvert = "";
        if (invoiceTemplateTypeId == null && invoiceTemplateTypeList == null) {
        } else {
            for (InvoiceTemplateTypeDTO invoice : invoiceTemplateTypeList) {
                if (DataUtil.safeEqual(invoice.getInvoiceTemplateTypeId(), invoiceTemplateTypeId)) {
                    invoiceTemplateConvert = invoice.getName();
                    break;
                }
            }
        }
        return invoiceTemplateConvert;
    }

    /**
     * ham xu ly xac nhan xem co add moi thog tin hay khong
     *
     * @author tuydv1
     */
    @Secured("@")
    public void doConfirmAdd() {
        render = false;
        forSearchInvoiceTemplate = new InvoiceTemplateDTO();
        if (!DataUtil.isNullOrZero(addInvoiceTemplate.getAmount())
                && !DataUtil.validateStringByPattern(addInvoiceTemplate.getAmount().toString(), DAYNUM_REGEX)) {
            reportError("", "", "mn.invoice.invoiceTemplate.amount.msg.err");
        }
    }

    @Secured("@")
    public void doConfirmEdit() {
        if (!DataUtil.isNullOrZero(editInvoiceTemplate.getAmount())) {
            if (!DataUtil.validateStringByPattern(editInvoiceTemplate.getAmount().toString(), DAYNUM_REGEX)) {
                reportError("", "", "mn.invoice.invoiceTemplate.amount.msg.err");
            }
//            if(editInvoiceTemplate.getUpdateAmount()>(editInvoiceTemplate.getAmount()-editInvoiceTemplate.getUsedAmount())) {
//                reportError("", "", "mn.invoice.invoiceTemplate.amount.err");
//            }
        }
    }

    @Secured("@")
    public void addInvoiceTemplateProcess() {
        try {
            addInvoiceTemplate.setOwnerId(BccsLoginSuccessHandler.getStaffDTO().getShopId());
            addInvoiceTemplate.setOwnerType("1");
            invoiceTemplateSv.createNewInvoiceTemplate(addInvoiceTemplate, BccsLoginSuccessHandler.getStaffDTO().getStaffCode(), Const.INVOICE_TYPE.TEMPLATE_TYPE_ADD_NEW);
            reportSuccess("frmInvoiceTemplate:msgInforTemplate", getText("mn.invoice.invoiceTemplate.add"));
            reportSuccess("frmInvoiceTemplate:msgInforTemplate", addInvoiceTemplate.getAmount() + " " + getInvoiceTemplateType(addInvoiceTemplate.getInvoiceTemplateTypeId()));
            addInvoiceTemplate.setAmount(null);
            invoiceTemplateList = invoiceTemplateSv.search(invoiceSearch);
//            invoiceTemplateList = invoiceTemplateSv.getAllReceiveInvoiceTemplateByShopId(BccsLoginSuccessHandler.getStaffDTO().getShopId());
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmInvoiceTemplate:msgInforTemplate", "", ex.getMessage());
            topPage();
        }

    }


    @Secured("@")
    public void prepareEdit(SelectEvent event) {
        try {
            addInvoiceTemplate.setAmount(null);
            editInvoiceTemplate = (InvoiceTemplateDTO) event.getObject();
            render = true;
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happened!");
        }
    }

    @Secured("@")
    public void editInvoiceTemplateProcess() {
        try {
            InvoiceTemplateDTO invoiceTemplateDTO;
            editInvoiceTemplate.setOwnerId(BccsLoginSuccessHandler.getStaffDTO().getShopId());
            invoiceTemplateDTO = DataUtil.cloneBean(editInvoiceTemplate);
            invoiceTemplateDTO.setAmount(editInvoiceTemplate.getUpdateAmount());
            BaseMessage result = invoiceTemplateSv.update(invoiceTemplateDTO, BccsLoginSuccessHandler.getStaffDTO().getStaffCode());
            if (result.isSuccess()) {
                forSearchInvoiceTemplate = new InvoiceTemplateDTO();
                reportSuccess("frmInvoiceTemplate:msgInforTemplate", "mn.invoice.invoiceType.success.edit");
                reportSuccess("frmInvoiceTemplate:msgInforTemplate", getInvoiceTemplateType(editInvoiceTemplate.getInvoiceTemplateTypeId()));
                invoiceTemplateList = invoiceTemplateSv.search(invoiceSearch);
//                invoiceTemplateList = invoiceTemplateSv.getAllReceiveInvoiceTemplateByShopId(BccsLoginSuccessHandler.getStaffDTO().getShopId());
                render = false;
            } else {
                reportError("frmInvoiceType:msgInforType", "", "mn.invoice.invoiceType.unSuccess.edit");
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

    public String getShopName() {
        String result = "";
        if (!DataUtil.isNullObject(BccsLoginSuccessHandler.getStaffDTO())) {
            result = BccsLoginSuccessHandler.getStaffDTO().getShopName();
        }
        return result;
    }

    public InvoiceTemplateDTO getForSearchInvoiceTemplate() {
        return forSearchInvoiceTemplate;
    }

    public void setForSearchInvoiceTemplate(InvoiceTemplateDTO forSearchInvoiceTemplate) {
        this.forSearchInvoiceTemplate = forSearchInvoiceTemplate;
    }

    public List<InvoiceTemplateDTO> getInvoiceTemplateList() {
        return invoiceTemplateList;
    }

    public void setInvoiceTemplateList(List<InvoiceTemplateDTO> invoiceTemplateList) {
        this.invoiceTemplateList = invoiceTemplateList;
    }

    public InvoiceTemplateDTO getAddInvoiceTemplate() {
        return addInvoiceTemplate;
    }

    public void setAddInvoiceTemplate(InvoiceTemplateDTO addInvoiceTemplate) {
        this.addInvoiceTemplate = addInvoiceTemplate;
    }

    public InvoiceTemplateDTO getEditInvoiceTemplate() {
        return editInvoiceTemplate;
    }

    public void setEditInvoiceTemplate(InvoiceTemplateDTO editInvoiceTemplate) {
        this.editInvoiceTemplate = editInvoiceTemplate;
    }

    public List<InvoiceTemplateTypeDTO> getInvoiceTemplateTypeList() {
        return invoiceTemplateTypeList;
    }

    public void setInvoiceTemplateTypeList(List<InvoiceTemplateTypeDTO> invoiceTemplateTypeList) {
        this.invoiceTemplateTypeList = invoiceTemplateTypeList;
    }

    public boolean isRender() {
        return render;
    }

    public void setRender(boolean render) {
        this.render = render;
    }
}
