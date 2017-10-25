package com.viettel.bccs.inventory.controller.invoice;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.InvoiceListDTO;
import com.viettel.bccs.inventory.dto.InvoiceSerialDTO;
import com.viettel.bccs.inventory.dto.InvoiceTypeDTO;
import com.viettel.bccs.inventory.dto.OptionSetValueDTO;
import com.viettel.bccs.inventory.service.InvoiceListService;
import com.viettel.bccs.inventory.service.InvoiceSerialService;
import com.viettel.bccs.inventory.service.InvoiceTypeService;
import com.viettel.bccs.inventory.service.OptionSetValueService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.web.common.controller.BaseController;
import org.primefaces.context.RequestContext;
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
public class InvoiceListController extends BaseController {

    public static final String WORK_MODE_EDIT = "0";
    public static final String WORK_MODE_ADD = "1";
    public static final String WORK_MODE_SEARCH = "2";


    @Autowired
    private OptionSetValueService optionSetValueSv;
    @Autowired
    private InvoiceListService invoiceListSv;
    @Autowired
    private InvoiceSerialService invoiceSerialSv;
    @Autowired
    private InvoiceTypeService invoiceTypeSv;

    private List<OptionSetValueDTO> listTypeInvoice;
    private List<InvoiceTypeDTO> listInvoiceTypeDTOEdit = Lists.newArrayList();
    private List<InvoiceTypeDTO> listInvoiceTypeDTOSearch;
    private List<InvoiceSerialDTO> listInvoiceSerialEdit = Lists.newArrayList();
    private List<InvoiceSerialDTO> listInvoiceSerialSearch;
    private List<InvoiceListDTO> listInvoiceListSelection = Lists.newArrayList();
    private InvoiceListDTO forEditInvoiceList;
    private InvoiceListDTO forSearchInvoiceList;
    private List<InvoiceListDTO> curInvoiceList;
    private String workMode; // 1 la sua - 0 la them moi - 2 Tim kiem
    private int index; //Thong tin vi tri ban ghi can sua
    private boolean ok;
    //hoa don khong tru kho
    private boolean showInvoiceTransType;
    private boolean noStock;

    @Secured("@")
    @PostConstruct
    public void init() {
        try {
            createData();
            noStock = false;
            showInvoiceTransType = false;
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
    }

    private void createData() throws Exception {
        index = 0;
        doResetSearchInvoiceList();
        initCurInvoiceList(forSearchInvoiceList);
        forEditInvoiceList = new InvoiceListDTO();
        workMode = WORK_MODE_SEARCH;
        listTypeInvoice = optionSetValueSv.getByOptionSetCode(Const.INVOICE_SERIAL.INVOICE_TYPE);
    }

    @Secured("@")
    public void searchInvoiceList() {
        try {

            validateFromAndToInvoice(forSearchInvoiceList, WORK_MODE_SEARCH);
            initCurInvoiceList(forSearchInvoiceList);
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
    }

    @Secured("@")
    public void doResetSearchInvoiceList() {
        try {
            forSearchInvoiceList = new InvoiceListDTO();
            forSearchInvoiceList.setShopId(BccsLoginSuccessHandler.getStaffDTO().getShopId());
            forSearchInvoiceList.setShopName(BccsLoginSuccessHandler.getStaffDTO().getShopName());
            listInvoiceListSelection = Lists.newArrayList();
            showInvoiceTransType = false;
            noStock = false;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    @Secured("@")
    public void prepareAdd() {
        try {
            workMode = WORK_MODE_ADD;
            forEditInvoiceList = new InvoiceListDTO();
            forEditInvoiceList.setShopId(BccsLoginSuccessHandler.getStaffDTO().getShopId());
            forEditInvoiceList.setShopName(BccsLoginSuccessHandler.getStaffDTO().getShopName());
            forEditInvoiceList.setInvoiceType(Const.INVOICE_TYPE.INVOICE_TYPE_SALE);
            listInvoiceTypeDTOEdit = invoiceTypeSv.getInvoiceType(DataUtil.safeToLong(forEditInvoiceList.getInvoiceType()));
            if (!DataUtil.isNullOrEmpty(listInvoiceTypeDTOEdit)) {
                forEditInvoiceList.setInvoiceTypeId(listInvoiceTypeDTOEdit.get(0).getInvoiceTypeId());
            }
            listInvoiceSerialEdit = invoiceSerialSv.getAllSerialByInvoiceType(BccsLoginSuccessHandler.getStaffDTO().getShopId(), forEditInvoiceList.getInvoiceTypeId());
            if (!DataUtil.isNullOrEmpty(listInvoiceSerialEdit)) {
                forEditInvoiceList.setInvoiceSerialId(listInvoiceSerialEdit.get(0).getInvoiceSerialId());
            }
            showInvoiceTransType = Const.INVOICE_TYPE.INVOICE_TYPE_SALE.equals(forSearchInvoiceList.getInvoiceType()) ? true : false;
            noStock = false;
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ErrorCode.ERROR_NOT_DEFINE, "common.error.happen");
        }
        topPage();
    }

    @Secured("@")
    public void validatePreAdd() {
        try {
            validateFromAndToInvoice(forEditInvoiceList, WORK_MODE_ADD);
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
        topPage();
    }

    @Secured("@")
    public void addInvoiceList() {
        try {
            ok = false;
            invoiceListSv.createNewInvoiceList(forEditInvoiceList, BccsLoginSuccessHandler.getStaffDTO().getStaffCode());
            ok = true;
            reportSuccess("", "mn.invoice.list.add.success");
            doResetSearchInvoiceList();
            initCurInvoiceList(forSearchInvoiceList);
            RequestContext.getCurrentInstance().update("frmExportNote:lstInvoice");
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
        topPage();
    }

    @Secured("@")
    public void addInvoiceListNoClose() {
        addInvoiceList();
        if (ok) {
            prepareAdd();
        }
    }

    @Secured("@")
    public void prepareEdit(InvoiceListDTO invoiceListEdit) {
        try {
            workMode = WORK_MODE_EDIT;
            forEditInvoiceList = DataUtil.cloneBean(invoiceListEdit);
            if (forEditInvoiceList.getFromInvoice() <= forEditInvoiceList.getCurrInvoice()) {
                forEditInvoiceList.setIsUpdate(true);
            } else {
                forEditInvoiceList.setIsUpdate(false);
            }
            index = curInvoiceList.indexOf(invoiceListEdit);
            InvoiceTypeDTO invoiceTypeDTO = invoiceTypeSv.findOne(forEditInvoiceList.getInvoiceTypeId());
            listInvoiceTypeDTOEdit = Lists.newArrayList();
            listInvoiceTypeDTOEdit.add(invoiceTypeDTO);
            InvoiceSerialDTO invoiceSerialDTO = invoiceSerialSv.findOne(forEditInvoiceList.getInvoiceSerialId());
            listInvoiceSerialEdit = Lists.newArrayList();
            listInvoiceSerialEdit.add(invoiceSerialDTO);
            showInvoiceTransType = Const.INVOICE_TYPE.INVOICE_TYPE_SALE.equals(forSearchInvoiceList.getInvoiceType()) ? true : false;
            noStock = Const.INVOICE_SERIAL.INVOICETRANS_NOSTOCK.equals(invoiceSerialDTO.getInvoiceTrans()) ? true : false;

        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
    }

    @Secured("@")
    public void prepareCopy(InvoiceListDTO invoiceListEdit) {
        try {
            workMode = WORK_MODE_ADD;
            forEditInvoiceList = DataUtil.cloneBean(invoiceListEdit);
            if (forEditInvoiceList.getFromInvoice() <= forEditInvoiceList.getCurrInvoice()) {
                forEditInvoiceList.setIsUpdate(true);
            } else {
                forEditInvoiceList.setIsUpdate(false);
            }
            index = curInvoiceList.indexOf(invoiceListEdit);
            listInvoiceTypeDTOEdit = getInvoiceTypeList(forEditInvoiceList);
            listInvoiceSerialEdit = getListInvoiceSerial(forEditInvoiceList, listInvoiceTypeDTOEdit);
            InvoiceSerialDTO invoiceSerialDTO = invoiceSerialSv.findOne(forEditInvoiceList.getInvoiceSerialId());
            showInvoiceTransType = Const.INVOICE_TYPE.INVOICE_TYPE_SALE.equals(forSearchInvoiceList.getInvoiceType()) ? true : false;
            noStock = Const.INVOICE_SERIAL.INVOICETRANS_NOSTOCK.equals(invoiceSerialDTO.getInvoiceTrans()) ? true : false;

            forEditInvoiceList.setInvoiceListId(null);

        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
    }

    private void initCurInvoiceList(InvoiceListDTO invoiceList) {
        try {

            invoiceList.setInvoiceTrans(noStock ? Const.INVOICE_SERIAL.INVOICETRANS_NOSTOCK : null);
            curInvoiceList = Lists.newArrayList();
            List<InvoiceListDTO> lst = invoiceListSv.searchInvoiceList(invoiceList);
            if (!DataUtil.isNullObject(lst)) {
                for (InvoiceListDTO dto : lst) {
                    if (dto.getFromInvoice() <= dto.getCurrInvoice()) {
                        dto.setIsUpdate(true);
                    } else {
                        dto.setIsUpdate(false);
                    }
                    curInvoiceList.add(dto);
                }
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
    }

    @Secured("@")
    public void validateEditInvoiceList() {
        try {
            validateFromAndToInvoice(forEditInvoiceList, WORK_MODE_EDIT);
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
        topPage();
    }

    @Secured("@")
    public void editInvoiceList() {
        try {
            validateFromAndToInvoice(forEditInvoiceList, WORK_MODE_EDIT);
            invoiceListSv.update(forEditInvoiceList, BccsLoginSuccessHandler.getStaffDTO().getStaffCode());
            reportSuccess("", "mn.invoice.list.edit.success");
            doResetSearchInvoiceList();
            initCurInvoiceList(forSearchInvoiceList);
            RequestContext.getCurrentInstance().update("frmExportNote:lstInvoice");
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
        topPage();
    }

    @Secured("@")
    public void preDeleteOneInvoiceList(InvoiceListDTO invoiceList) {

        if (DataUtil.isNullObject(invoiceList)) {
            reportError("", "", "invoice.invoiceList.no.select.invoice");
        } else {
            forEditInvoiceList = DataUtil.cloneBean(invoiceList);
            index = curInvoiceList.indexOf(invoiceList);
        }
        topPage();
    }

    @Secured("@")
    public void deleteOneInvoiceList() {
        try {
            invoiceListSv.delete(Lists.newArrayList(forEditInvoiceList.getInvoiceListId()), BccsLoginSuccessHandler.getStaffDTO().getStaffCode());
            reportSuccess("", "mn.invoice.list.delete.success");
            doResetSearchInvoiceList();
            initCurInvoiceList(forSearchInvoiceList);
            RequestContext.getCurrentInstance().update("frmExportNote:lstInvoice");
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
        topPage();
    }

    @Secured("@")
    public void validateDeleteInvoiceList() {
        if (DataUtil.isNullOrEmpty(listInvoiceListSelection)) {
            reportError("", "", "invoice.invoiceList.no.select.invoice");
            topPage();
        }
        for (InvoiceListDTO dto : listInvoiceListSelection) {
            if (dto.getIsUpdate()) {
                reportError("", "", "invoice.invoiceList.select.not.delete");
                topPage();
                break;
            }
        }
    }

    @Secured("@")
    public void deleteInvoiceList() {
        try {
            List<Long> invoiceListIDList = Lists.newArrayList();
            for (InvoiceListDTO invoiceListDTO : listInvoiceListSelection) {
                invoiceListIDList.add(invoiceListDTO.getInvoiceListId());
            }
            invoiceListSv.delete(invoiceListIDList, BccsLoginSuccessHandler.getStaffDTO().getStaffCode());
            reportSuccess("", "common.msg.success.delete");
            doResetSearchInvoiceList();
            initCurInvoiceList(forSearchInvoiceList);
            RequestContext.getCurrentInstance().update("frmExportNote:lstInvoice");
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
        topPage();
    }

    @Secured("@")
    public void changeInvoiceType(String workMode) {
        try {
            if (WORK_MODE_SEARCH.equals(workMode)) {
                listInvoiceTypeDTOSearch = getInvoiceTypeList(forSearchInvoiceList);
            } else {
                listInvoiceTypeDTOEdit = getInvoiceTypeList(forEditInvoiceList);
            }
            showInvoiceTransType = Const.INVOICE_TYPE.INVOICE_TYPE_SALE.equals(forSearchInvoiceList.getInvoiceType()) ? true : false;
            noStock = false;
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
    }

    private List<InvoiceTypeDTO> getInvoiceTypeList(InvoiceListDTO invoiceList) throws Exception {
        if (DataUtil.isNullObject(invoiceList.getInvoiceType())) {
            invoiceList.setInvoiceName("");
            invoiceList.setInvoiceInv("");
            invoiceList.setInvoiceTypeId(null);
            return Lists.newArrayList();
        } else {
            return getListTypeInvoice(invoiceList.getInvoiceType());
        }
    }

    private List<InvoiceTypeDTO> getListTypeInvoice(String invoiceType) throws Exception {
        List<InvoiceTypeDTO> invoiceTypeList = invoiceTypeSv.getInvoiceType(Long.valueOf(invoiceType));
        if (DataUtil.isNullOrEmpty(invoiceTypeList)) {
            invoiceTypeList = Lists.newArrayList();
        }
        return invoiceTypeList;
    }

    //Ham thay doi mau so
    @Secured("@")
    public void changeTypeInv(String workMode) {
        try {
            if (WORK_MODE_SEARCH.equals(workMode)) {
                listInvoiceSerialSearch = getListInvoiceSerial(forSearchInvoiceList, listInvoiceTypeDTOSearch);
            } else {
                listInvoiceSerialEdit = getListInvoiceSerial(forEditInvoiceList, listInvoiceTypeDTOEdit);
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
    }


    private List<InvoiceSerialDTO> getListInvoiceSerial(InvoiceListDTO invoiceList, List<InvoiceTypeDTO> listInvoiceType) throws Exception {
        if (DataUtil.isNullObject(invoiceList.getInvoiceTypeId())) {
            invoiceList.setInvoiceInv("");
            invoiceList.setInvoiceName("");
            return Lists.newArrayList();
        }
        for (InvoiceTypeDTO invoiceType : listInvoiceType) {
            if (DataUtil.safeEqual(invoiceType.getInvoiceTypeId(), invoiceList.getInvoiceTypeId())) {
                invoiceList.setInvoiceInv(invoiceType.getTypeInv());
                invoiceList.setInvoiceName(invoiceType.getInvoiceName());
                break;
            }
        }
        return invoiceSerialSv.getAllSerialByInvoiceType(BccsLoginSuccessHandler.getStaffDTO().getShopId(), invoiceList.getInvoiceTypeId());
//        return Lists.newArrayList();
    }

    @Secured("@")
    public void checkNoInvoice(String workMode) {
        try {
            if (WORK_MODE_SEARCH.equals(workMode)) {
                validateFromAndToInvoice(forSearchInvoiceList, WORK_MODE_SEARCH);
            } else {
                validateFromAndToInvoice(forEditInvoiceList, workMode);
            }

        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());

        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }

    }

    @Secured("@")
    public void changeInvoiceSerialNo(String workMode) {
        try {
            if (WORK_MODE_SEARCH.equals(workMode)) {
                getSerialNo(forSearchInvoiceList);
            } else {
                getSerialNo(forEditInvoiceList);
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());

        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
    }

    private void getSerialNo(InvoiceListDTO invoiceList) throws Exception {
        if (DataUtil.isNullObject(invoiceList.getInvoiceSerialId())) {
            invoiceList.setSerialNo("");
            return;
        }
        InvoiceSerialDTO invoiceSerial = invoiceSerialSv.findOne(invoiceList.getInvoiceSerialId());
        invoiceList.setSerialNo(invoiceSerial.getSerialNo());
        noStock = Const.INVOICE_SERIAL.INVOICETRANS_NOSTOCK.equals(invoiceSerial.getInvoiceTrans()) ? true : false;
    }

    private void validateFromAndToInvoice(InvoiceListDTO invoiceListDTO, String workMode) throws Exception {
        if (!DataUtil.isNullObject(invoiceListDTO.getFromInvoice())
                && !DataUtil.isNullObject(invoiceListDTO.getToInvoice())) {
            if (invoiceListDTO.getFromInvoice() > invoiceListDTO.getToInvoice()) {
                throw new LogicException("", "invoce.invoiceList.validateFromTo");
            }
        }
        if (!DataUtil.isNullObject(invoiceListDTO.getCurrInvoice())
                && !DataUtil.isNullObject(invoiceListDTO.getToInvoice())) {
            if (invoiceListDTO.getCurrInvoice() > invoiceListDTO.getToInvoice()) {
                throw new LogicException("", "invoce.invoiceList.validateCurTo");
            }
        }
    }

    public List<OptionSetValueDTO> getListTypeInvoice() {
        return listTypeInvoice;
    }

    public void setListTypeInvoice(List<OptionSetValueDTO> listTypeInvoice) {
        this.listTypeInvoice = listTypeInvoice;
    }

    public List<InvoiceSerialDTO> getListInvoiceSerialEdit() {
        return listInvoiceSerialEdit;
    }

    public List<InvoiceTypeDTO> getListInvoiceTypeDTOEdit() {
        return listInvoiceTypeDTOEdit;
    }

    public void setListInvoiceTypeDTOEdit(List<InvoiceTypeDTO> listInvoiceTypeDTOEdit) {
        this.listInvoiceTypeDTOEdit = listInvoiceTypeDTOEdit;
    }

    public List<InvoiceTypeDTO> getListInvoiceTypeDTOSearch() {
        return listInvoiceTypeDTOSearch;
    }

    public void setListInvoiceTypeDTOSearch(List<InvoiceTypeDTO> listInvoiceTypeDTOSearch) {
        this.listInvoiceTypeDTOSearch = listInvoiceTypeDTOSearch;
    }

    public void setListInvoiceSerialEdit(List<InvoiceSerialDTO> listInvoiceSerialEdit) {
        this.listInvoiceSerialEdit = listInvoiceSerialEdit;
    }

    public List<InvoiceSerialDTO> getListInvoiceSerialSearch() {
        return listInvoiceSerialSearch;
    }


    public void setListInvoiceSerialSearch(List<InvoiceSerialDTO> listInvoiceSerialSearch) {
        this.listInvoiceSerialSearch = listInvoiceSerialSearch;
    }

    public InvoiceListDTO getForEditInvoiceList() {
        return forEditInvoiceList;
    }

    public void setForEditInvoiceList(InvoiceListDTO forEditInvoiceList) {
        this.forEditInvoiceList = forEditInvoiceList;
    }

    public InvoiceListDTO getForSearchInvoiceList() {
        return forSearchInvoiceList;
    }

    public void setForSearchInvoiceList(InvoiceListDTO forSearchInvoiceList) {
        this.forSearchInvoiceList = forSearchInvoiceList;
    }

    public List<InvoiceListDTO> getCurInvoiceList() {
        return curInvoiceList;
    }

    public void setCurInvoiceList(List<InvoiceListDTO> curInvoiceList) {
        this.curInvoiceList = curInvoiceList;
    }

    public String getWorkMode() {
        return workMode;
    }

    public void setWorkMode(String workMode) {
        this.workMode = workMode;
    }

    public List<InvoiceListDTO> getListInvoiceListSelection() {
        return listInvoiceListSelection;
    }

    public void setListInvoiceListSelection(List<InvoiceListDTO> listInvoiceListSelection) {
        this.listInvoiceListSelection = listInvoiceListSelection;
    }

    public boolean isNoStock() {
        return noStock;
    }

    public void setNoStock(boolean noStock) {
        this.noStock = noStock;
    }

    public boolean isShowInvoiceTransType() {
        return showInvoiceTransType;
    }

    public void setShowInvoiceTransType(boolean showInvoiceTransType) {
        this.showInvoiceTransType = showInvoiceTransType;
    }
}
