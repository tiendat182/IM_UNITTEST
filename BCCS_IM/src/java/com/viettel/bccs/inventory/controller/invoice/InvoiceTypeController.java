package com.viettel.bccs.inventory.controller.invoice;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.InvoiceTypeDTO;
import com.viettel.bccs.inventory.service.InvoiceTypeService;
import com.viettel.bccs.inventory.service.OptionSetValueService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.web.common.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;
import com.viettel.bccs.inventory.dto.OptionSetValueDTO;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.util.List;

/**
 * Created by tuydv1 on 11/10/2015.
 */
@Component
@Scope("view")
@ManagedBean
public class InvoiceTypeController extends BaseController {


    @Autowired
    InvoiceTypeService invoiceTypeSv;
    @Autowired
    private OptionSetValueService optionSetValueSv;

    private InvoiceTypeDTO forSearchInvoiceType;
    private InvoiceTypeDTO addInvoiceType;
    private InvoiceTypeDTO editInvoiceType;
    private List<InvoiceTypeDTO> invoiceTypeList = Lists.newArrayList();
    private List<InvoiceTypeDTO> invoiceTypeSelection = Lists.newArrayList();
    private List<OptionSetValueDTO> listInvoiceType = Lists.newArrayList();
    private List<OptionSetValueDTO> listType = Lists.newArrayList();
    private InvoiceTypeDTO invoiceTypeDelete;
    private String[] listTypeSelect;
    private int close;


    @PostConstruct
    @Secured("@")
    public void init() {
        try {
            createData();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
            topPage();
        } catch (Exception ex) {
            reportError("", "", ex);
            topPage();
        }
    }

    private void createData() throws Exception {
        listInvoiceType = optionSetValueSv.getByOptionSetCode(Const.INVOICE_SERIAL.INVOICE_TYPE);
        listType = optionSetValueSv.getByOptionSetCode(Const.INVOICE_TYPE.TYPE);
        forSearchInvoiceType = new InvoiceTypeDTO();
//        listTypeSelectDefault();
        invoiceTypeList = invoiceTypeSv.search(forSearchInvoiceType);
    }


    @Secured("@")
    public String getType(String type) {
        String typeConvert = "";
        if (!DataUtil.isNullObject(type) || DataUtil.isNullOrEmpty(listType)) {
            for (OptionSetValueDTO invoice : listType) {
                if (DataUtil.safeEqual(invoice.getValue(), type)) {
                    typeConvert = invoice.getName();
                    break;
                }
            }
        }
        return typeConvert;
    }

    @Secured("@")
    public String getInvoiceType(String invoiceType) {
        String invoiceTypeConvert = "";

        if (!DataUtil.isNullObject(invoiceType) || DataUtil.isNullOrEmpty(listInvoiceType)) {
            for (OptionSetValueDTO invoice : listInvoiceType) {
                if (DataUtil.safeEqual(invoice.getValue(), invoiceType)) {
                    invoiceTypeConvert = invoice.getName();
                    break;
                }
            }
        }
        return invoiceTypeConvert;
    }

    @Secured("@")
    public void searchInvoiceType() {
        try {
            forSearchInvoiceType.setInvoiceType("");
            if (DataUtil.isNullOrEmpty(listTypeSelect)) {
                invoiceTypeList = invoiceTypeSv.search(forSearchInvoiceType);
            } else {
                if (!DataUtil.isNullOrEmpty(invoiceTypeList)) {
                    invoiceTypeList.clear();
                }
                List<InvoiceTypeDTO> list;
                for (String type : listTypeSelect) {
                    forSearchInvoiceType.setInvoiceType(type);
                    if (DataUtil.isNullOrEmpty(invoiceTypeList)) {
                        invoiceTypeList = invoiceTypeSv.search(forSearchInvoiceType);
                    } else {
                        list = invoiceTypeSv.search(forSearchInvoiceType);
                        if (!DataUtil.isNullOrEmpty(list)) {
                            for (InvoiceTypeDTO invoiceTypeDTO : list) {
                                invoiceTypeList.add(invoiceTypeDTO);
                            }
                        }
                    }
                }
            }
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

        forSearchInvoiceType = new InvoiceTypeDTO();
        listTypeSelect = new String[20];
        topPage();
    }

    @Secured("@")
    public void preAddInvoiceType() throws Exception {
        addInvoiceType = new InvoiceTypeDTO();
        addInvoiceType.setStatus(Const.INVOICE_TYPE.STATUS_ACTIVE);
        if (!DataUtil.isNullOrEmpty(listInvoiceType)) {
            addInvoiceType.setInvoiceType(listInvoiceType.get(0).getValue());
            addInvoiceType.setInvoiceTypeName(listInvoiceType.get(0).getName());
        }
    }

    @Secured("@")
    public void changeInvoiceTypeAdd() {
        try {

            for (OptionSetValueDTO invoiceType : listInvoiceType) {
                if (DataUtil.safeEqual(invoiceType.getValue(), addInvoiceType.getInvoiceType())) {
                    addInvoiceType.setInvoiceTypeName(invoiceType.getName());
                    break;
                }
            }
            if (!DataUtil.isNullOrEmpty(listInvoiceType)) {
                addInvoiceType.setInvoiceTypeText(listInvoiceType.get(0).getValue());
            }
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happened");
        }
    }


    @Secured("@")
    public void addInvoiceTypeProcess() {
        try {
            if (DataUtil.isNullObject(addInvoiceType)) {
                reportError("pnlAddInvoiceType:mesageAdd", "", "mn.invoice.invoiceType.no.selected");
            } else {
                convertStrim(addInvoiceType);
                invoiceTypeSv.createNewInvoiceType(addInvoiceType, BccsLoginSuccessHandler.getStaffDTO().getStaffCode());
                if (close == 1) {
                    reportSuccess("pnlAddInvoiceType:mesageAdd", "mn.invoice.invoiceType.success.add");
                } else {
                    reportSuccess("frmInvoiceType:msgInforType", "mn.invoice.invoiceType.success.add");
                }
                forSearchInvoiceType = new InvoiceTypeDTO();
                addInvoiceType = new InvoiceTypeDTO();
                addInvoiceType.setStatus(Const.INVOICE_TYPE.STATUS_ACTIVE);
                if (!DataUtil.isNullOrEmpty(listInvoiceType)) {
                    addInvoiceType.setInvoiceType(listInvoiceType.get(0).getValue());
                    addInvoiceType.setInvoiceTypeName(listInvoiceType.get(0).getName());
                }
                invoiceTypeList = invoiceTypeSv.search(forSearchInvoiceType);
            }
            topPage();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("pnlAddInvoiceType:mesageAdd", ex.getErrorCode(), ex.getDescription());
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("pnlAddInvoiceType:mesageAdd", "", ex);
            topPage();
        }

    }

    /*
    // dung de update lai du lieu ban dau
     */
    private void listTypeSelectDefault() {
        listTypeSelect = new String[20];
        if (!DataUtil.isNullOrEmpty(listInvoiceType)) {
            int size = listInvoiceType.size();
            for (int i = 0; i < size; i++) {
                listTypeSelect[i] = listInvoiceType.get(i).getValue();
            }
        }
    }

    /**
     * ham xu ly xac nhan xem co add moi thog tin hay khong
     *
     * @author tuydv1
     */
    @Secured("@")
    public void doConfirmAdd(int i) {
        close = i;
        if (!DataUtil.isNullOrZero(addInvoiceType.getInvoiceNoLength())
                && !DataUtil.validateStringByPattern(addInvoiceType.getInvoiceNoLength().toString(), getText("DAYNUM_REGEX"))) {
            reportError("", "", "mn.invoice.invoiceType.invoiceNoLength.msg.err");
        }
        convertStrim(addInvoiceType);
        if (DataUtil.isNullOrEmpty(addInvoiceType.getInvoiceName())) {
            reportError("", "", "mn.invoice.invoiceType.invoiceName.require.msg");
        }
        if (DataUtil.isNullOrEmpty(addInvoiceType.getTypeInv())) {
            reportError("", "", "mn.invoice.invoiceType.typeInv.require.msg");
        }
    }

    @Secured("@")
    public void doConfirmEdit() {
        if (!DataUtil.isNullOrZero(editInvoiceType.getInvoiceNoLength())
                && !DataUtil.validateStringByPattern(editInvoiceType.getInvoiceNoLength().toString(), getText("DAYNUM_REGEX"))) {
            reportError("", "", "mn.invoice.invoiceType.invoiceNoLength.msg.err");
        }
        convertStrim(editInvoiceType);
        if (DataUtil.isNullOrEmpty(editInvoiceType.getInvoiceName())) {
            reportError("", "", "mn.invoice.invoiceType.invoiceName.require.msg");
        }
        if (DataUtil.isNullOrEmpty(editInvoiceType.getTypeInv())) {
            reportError("", "", "mn.invoice.invoiceType.typeInv.require.msg");
        }
    }


    @Secured("@")
    public void prepareEdit(InvoiceTypeDTO invoiceType) {
        try {
            if (DataUtil.isNullObject(invoiceType)) {
                reportError("", "", "mn.invoice.invoiceType.no.select");
            } else {
                editInvoiceType = DataUtil.cloneBean(invoiceType);
            }
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happened!");
        }
    }

    @Secured("@")
    public void prepareCopy(InvoiceTypeDTO invoiceType) {
        try {
            if (DataUtil.isNullObject(invoiceType)) {
                reportError("", "", "mn.invoice.invoiceType.no.select");
            } else {
                addInvoiceType = DataUtil.cloneBean(invoiceType);
                addInvoiceType.setInvoiceTypeId(null);
            }
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happened!");
        }
    }

    @Secured("@")
    public void editInvoiceTypeProcess() {
        try {
            BaseMessage result = invoiceTypeSv.update(editInvoiceType, BccsLoginSuccessHandler.getStaffDTO().getStaffCode());
            if (result.isSuccess()) {
                reportSuccess("frmInvoiceType:msgInforType", "mn.invoice.invoiceType.success.edit");
                forSearchInvoiceType = new InvoiceTypeDTO();
//                listTypeSelectDefault();
                invoiceTypeList = invoiceTypeSv.search(forSearchInvoiceType);
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
    public void preDeleteOneInvoiceType(InvoiceTypeDTO dto) {
        if (DataUtil.isNullObject(dto)) {
            reportError("", "", "mn.invoice.invoiceType.no.select");
            topPage();
        } else {
            invoiceTypeDelete = DataUtil.cloneBean(dto);
        }
    }

    @Secured("@")
    public void deleteOneInvoiceType() {
        try {
            if (DataUtil.isNullObject(invoiceTypeDelete)) {
                reportError("", "", "mn.invoice.invoiceType.no.select");
            } else {
                List<Long> lstId = Lists.newArrayList();
                lstId.add(invoiceTypeDelete.getInvoiceTypeId());

                BaseMessage result = invoiceTypeSv.delete(lstId, BccsLoginSuccessHandler.getStaffDTO().getStaffCode());
                if (result.isSuccess()) {
                    reportSuccess("frmInvoiceType:msgInforType", "common.msg.success.delete");
                    reportSuccess("", getTextParam(getText("mn.invoice.invoiceType.deleteNameData"), invoiceTypeDelete.getInvoiceName()));
                    forSearchInvoiceType = new InvoiceTypeDTO();
//                    listTypeSelectDefault();
                    invoiceTypeList = invoiceTypeSv.search(forSearchInvoiceType);
//                    List<InvoiceTypeDTO> listInvoiceType = Lists.newArrayList();
//                    for (InvoiceTypeDTO dto : invoiceTypeList) {
//                        boolean isDelete = false;
//                        for (Long invoiceTypeId : lstId) {
//                            if (invoiceTypeId.equals(dto.getInvoiceTypeId())) {
//                                isDelete = true;
//                                break;
//                            }
//                        }
//                        if (isDelete) {
//                            continue;
//                        }
//                        listInvoiceType.add(dto);
//                    }
//                    invoiceTypeList = listInvoiceType;
                } else {
                    reportError("frmInvoiceType:msgInforType", "", "common.msg.unsuccess.delete");
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
    public void validateDeleteInvoiceType() {
        if (DataUtil.isNullOrEmpty(invoiceTypeSelection)) {
            reportError("", "", "mn.invoice.invoiceType.no.select");
            topPage();
        }
    }

    @Secured("@")
    public void deleteInvoiceType() {
        try {
            if (DataUtil.isNullObject(invoiceTypeSelection)) {
                reportError("", "", "mn.invoice.invoiceType.no.select");
            } else {
                List<Long> lstId = Lists.newArrayList();
                for (InvoiceTypeDTO invoiceTypeDTO : invoiceTypeSelection) {
                    lstId.add(invoiceTypeDTO.getInvoiceTypeId());
                }
                BaseMessage result = invoiceTypeSv.delete(lstId, BccsLoginSuccessHandler.getStaffDTO().getStaffCode());
                if (result.isSuccess()) {
                    reportSuccess("frmInvoiceType:msgInforType", "common.msg.success.delete");
                    forSearchInvoiceType = new InvoiceTypeDTO();
//                    listTypeSelectDefault();
                    invoiceTypeList = invoiceTypeSv.search(forSearchInvoiceType);
                } else {
                    reportError("frmInvoiceType:msgInforType", "", "common.msg.unsuccess.delete");
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

    private InvoiceTypeDTO convertStrim(InvoiceTypeDTO invoiceTypeDTO) {
        if (!DataUtil.isNullObject(invoiceTypeDTO)) {
            if (!DataUtil.isNullOrEmpty(invoiceTypeDTO.getInvoiceName())) {
                invoiceTypeDTO.setInvoiceName(invoiceTypeDTO.getInvoiceName().trim());
            }
            if (!DataUtil.isNullOrEmpty(invoiceTypeDTO.getTypeInv())) {
                invoiceTypeDTO.setTypeInv(invoiceTypeDTO.getTypeInv().trim());
            }

        }
        return invoiceTypeDTO;
    }


    public InvoiceTypeDTO getForSearchInvoiceType() {
        return forSearchInvoiceType;
    }

    public void setForSearchInvoiceType(InvoiceTypeDTO forSearchInvoiceType) {
        this.forSearchInvoiceType = forSearchInvoiceType;
    }

    public List<InvoiceTypeDTO> getInvoiceTypeList() {
        return invoiceTypeList;
    }

    public void setInvoiceTypeList(List<InvoiceTypeDTO> invoiceTypeList) {
        this.invoiceTypeList = invoiceTypeList;
    }

    public InvoiceTypeDTO getAddInvoiceType() {
        return addInvoiceType;
    }

    public void setAddInvoiceType(InvoiceTypeDTO addInvoiceType) {
        this.addInvoiceType = addInvoiceType;
    }

    public InvoiceTypeDTO getEditInvoiceType() {
        return editInvoiceType;
    }

    public void setEditInvoiceType(InvoiceTypeDTO editInvoiceType) {
        this.editInvoiceType = editInvoiceType;
    }

    public List<OptionSetValueDTO> getListInvoiceType() {
        return listInvoiceType;
    }

    public void setListInvoiceType(List<OptionSetValueDTO> listInvoiceType) {
        this.listInvoiceType = listInvoiceType;
    }

    public List<OptionSetValueDTO> getListType() {
        return listType;
    }

    public void setListType(List<OptionSetValueDTO> listType) {
        this.listType = listType;
    }

    public List<InvoiceTypeDTO> getInvoiceTypeSelection() {
        return invoiceTypeSelection;
    }

    public void setInvoiceTypeSelection(List<InvoiceTypeDTO> invoiceTypeSelection) {
        this.invoiceTypeSelection = invoiceTypeSelection;
    }

    public String[] getListTypeSelect() {
        return listTypeSelect;
    }

    public void setListTypeSelect(String[] listTypeSelect) {
        this.listTypeSelect = listTypeSelect;
    }
}
