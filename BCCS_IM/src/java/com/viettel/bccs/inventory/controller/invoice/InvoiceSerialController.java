package com.viettel.bccs.inventory.controller.invoice;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.InvoiceSerialService;
import com.viettel.bccs.inventory.service.InvoiceTypeService;
import com.viettel.bccs.inventory.service.OptionSetValueService;
import com.viettel.bccs.inventory.service.ShopService;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.web.common.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tuyendv8 on 11/10/2015.
 */
@Component
@Scope("view")
@ManagedBean(name = "invoiceSerialController")
public class InvoiceSerialController extends BaseController {

    @Autowired
    private InvoiceSerialService invoiceSerialSv;
    @Autowired
    private InvoiceTypeService invoiceTypeSv;
    @Autowired
    private ShopService shopService;
    @Autowired
    private OptionSetValueService optionSetValueSv;

    private InvoiceSerialDTO forSearchInvoiceSerial;
    private InvoiceSerialDTO invoiceSerialDTO = new InvoiceSerialDTO();

    private InvoiceSerialDTO forDeleteInvoiceSerial;
    private List<InvoiceSerialDTO> invoiceSerialList = Lists.newArrayList();
    private List<InvoiceSerialDTO> invoiceListSerialSelect = Lists.newArrayList();
    private List<InvoiceTypeDTO> invoiceTypeListSearch = Lists.newArrayList();
    private List<InvoiceTypeDTO> invoiceTypeList = Lists.newArrayList();

    private List<ShopDTO> shopList = Lists.newArrayList();
    private List<ShopDTO> fillterListBranch = Lists.newArrayList();
    private int index; //Luu thong tin vi tri ban ghi can sua
    private List<OptionSetValueDTO> listInvoiceType = Lists.newArrayList();
    private ShopDTO curentShop;

    private List<VShopStaffDTO> listBranch = Lists.newArrayList();
    private List<VShopStaffDTO> listBranchTable = Lists.newArrayList();
    private List<String> listBranchSelect;
    private boolean showBranch;
    private boolean showInvoiceTransType = false; // loai hoa don khong tru kho
    private boolean noStock = false; // loai hoa don khong tru kho

    @Autowired
    private ShopInfoNameable shopInfoTag;
    private boolean addOK;

    @PostConstruct
    @Secured("@")
    public void init() {
        try {
            shopInfoTag.initShop(this, BccsLoginSuccessHandler.getStaffDTO().getShopId().toString(), true);
            listInvoiceType = optionSetValueSv.getByOptionSetCode(Const.INVOICE_SERIAL.INVOICE_TYPE);
            forSearchInvoiceSerial = new InvoiceSerialDTO();
            forSearchInvoiceSerial.setShopId(BccsLoginSuccessHandler.getStaffDTO().getShopId());
            invoiceSerialList = invoiceSerialSv.searchInvoiceSerial(forSearchInvoiceSerial);
            showInvoiceTransType = false;
            noStock = false;
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ErrorCode.ERROR_NOT_DEFINE, "common.error.happen");

        }
    }

    @Secured("@")
    public void doResetSearchInvoiceType() {
        try {
            forSearchInvoiceSerial = new InvoiceSerialDTO();
            curentShop = new ShopDTO();
            shopInfoTag.resetShop();
            invoiceListSerialSelect = Lists.newArrayList();
            showInvoiceTransType = false;
            noStock = false;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    @Secured("@")
    public void clearCurrentShop() {
        curentShop = new ShopDTO();
        listBranchSelect = Lists.newArrayList();
    }

    @Secured("@")
    public void changeInvoiceTypeSearch() {
        try {
            invoiceTypeListSearch = invoiceTypeSv.getInvoiceType(DataUtil.safeToLong(forSearchInvoiceSerial.getInvoiceType()));
            for (OptionSetValueDTO invoiceType : listInvoiceType) {
                if (DataUtil.safeEqual(invoiceType.getValue(), forSearchInvoiceSerial.getInvoiceType())) {
                    forSearchInvoiceSerial.setInvoiceTypeName(invoiceType.getName());
                    break;
                }
            }
            forSearchInvoiceSerial.setInvoiceTypeId(null);
            forSearchInvoiceSerial.setInvoiceTypeText("");
            showInvoiceTransType = Const.INVOICE_TYPE.INVOICE_TYPE_SALE.equals(forSearchInvoiceSerial.getInvoiceType()) ? true : false;
            noStock = false;
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ErrorCode.ERROR_NOT_DEFINE, "common.error.happen");
        }
    }

    @Secured("@")
    public void changeInvoiceType() {
        try {
            invoiceTypeList = Lists.newArrayList();
            invoiceSerialDTO.setInvoiceTypeId(null);
            invoiceTypeList = invoiceTypeSv.getInvoiceType(DataUtil.safeToLong(invoiceSerialDTO.getInvoiceType()));
            showInvoiceTransType = Const.INVOICE_TYPE.INVOICE_TYPE_SALE.equals(invoiceSerialDTO.getInvoiceType()) ? true : false;
            noStock = false;
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ErrorCode.ERROR_NOT_DEFINE, "common.error.happen");
        }
    }

    @Secured("@")
    public void searchInvoiceSerial() {
        try {
            forSearchInvoiceSerial.setShopId(BccsLoginSuccessHandler.getStaffDTO().getShopId());
            if (!DataUtil.isNullObject(curentShop) && !DataUtil.isNullObject(curentShop.getShopId())) {
                forSearchInvoiceSerial.setShopUsedId(curentShop.getShopId());
            } else {
                forSearchInvoiceSerial.setShopUsedId(null);
            }
            forSearchInvoiceSerial.setInvoiceTrans(noStock ? Const.INVOICE_SERIAL.INVOICETRANS_NOSTOCK : Const.INVOICE_SERIAL.INVOICETRANS_NORMAL);
            invoiceSerialList = invoiceSerialSv.searchInvoiceSerial(forSearchInvoiceSerial);
//            if (DataUtil.isNullOrEmpty(invoiceSerialList)) {
//                throw new LogicException("", "mn.invoice.serial.not.find");
//            }
//            reportSuccess("", getTextParam("mn.invoice.serial.result.find", DataUtil.safeToString(invoiceSerialList.size())));
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
        } catch (Exception ex) {
            logger.error("searchInvoiceSerial", ex);
            reportError("", "", "common.error.happen");
        }
    }

    @Secured("@")
    public void preDeleteOneInvoiceSerial(InvoiceSerialDTO deleteInvoiceSerial) {
        if (DataUtil.isNullObject(deleteInvoiceSerial)) {
            reportError("", "", "mn.invoice.serial.no.choose.delete");
        } else {
            forDeleteInvoiceSerial = DataUtil.cloneBean(deleteInvoiceSerial);
        }
        topPage();
    }

    @Secured("@")
    public void deleteOneInvoiceSerial() {
        try {
            if (DataUtil.isNullObject(forDeleteInvoiceSerial)) {
                throw new LogicException("", "mn.invoice.serial.not.select");
            }
            List<Long> listSerialId = Lists.newArrayList();
            listSerialId.add(forDeleteInvoiceSerial.getInvoiceSerialId());
            invoiceSerialSv.delete(listSerialId, BccsLoginSuccessHandler.getStaffDTO().getStaffCode());
            List<InvoiceSerialDTO> listInvoiceSerial = Lists.newArrayList();
            for (InvoiceSerialDTO invoiceSerialDTO : invoiceSerialList) {
                if (invoiceSerialDTO.getInvoiceSerialId().equals(forDeleteInvoiceSerial.getInvoiceSerialId())) {
                    continue;
                }
                listInvoiceSerial.add(invoiceSerialDTO);
            }
            invoiceSerialList = listInvoiceSerial;
            reportSuccess("", "mn.invoice.serial.delete.success");
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happen");
        }
        topPage();
    }

    @Secured("@")
    public void validateDelete() {
        if (DataUtil.isNullOrEmpty(invoiceListSerialSelect)) {
            reportError("", "", "mn.invoice.serial.not.select");
            topPage();
        }
    }

    @Secured("@")
    public void deleteInvoiceSerial() {
        try {
            if (DataUtil.isNullObject(invoiceListSerialSelect)) {
                throw new LogicException("", "mn.invoice.serial.not.select");
            }
            List<Long> listSerialId = Lists.newArrayList();
            for (InvoiceSerialDTO invoiceSerialDTO : invoiceListSerialSelect) {
                listSerialId.add(invoiceSerialDTO.getInvoiceSerialId());
            }
            invoiceSerialSv.delete(listSerialId, BccsLoginSuccessHandler.getStaffDTO().getStaffCode());
            List<InvoiceSerialDTO> listInvoiceSerial = Lists.newArrayList();
            for (InvoiceSerialDTO invoiceSerialDTO : invoiceSerialList) {
                boolean isDelete = false;
                for (Long invoiceSerialId : listSerialId) {
                    if (invoiceSerialId.equals(invoiceSerialDTO.getInvoiceSerialId())) {
                        isDelete = true;
                        break;
                    }
                }
                if (isDelete) {
                    continue;
                }
                listInvoiceSerial.add(invoiceSerialDTO);
            }
            invoiceSerialList = listInvoiceSerial;
            reportSuccess("", "mn.invoice.serial.delete.success");
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happen");
        }
        topPage();
    }

    @Secured("@")
    public void prepareEdit(InvoiceSerialDTO editInvoiceSerial) {
        try {
            if (DataUtil.isNullObject(editInvoiceSerial)) {
                throw new LogicException("", "mn.invoice.serial.select.edit");
            }
            invoiceSerialDTO = invoiceSerialSv.findOne(editInvoiceSerial.getInvoiceSerialId());
            InvoiceTypeDTO invoiceTypeDTO = invoiceTypeSv.findOne(editInvoiceSerial.getInvoiceTypeId());
            invoiceSerialDTO.setInvoiceType(invoiceTypeDTO.getInvoiceType());
            invoiceTypeList = invoiceTypeSv.getInvoiceType(DataUtil.safeToLong(invoiceSerialDTO.getInvoiceType()));
            shopInfoTag.loadShop(DataUtil.safeToString(invoiceSerialDTO.getShopUsedId()), false);
            showInvoiceTransType = Const.INVOICE_TYPE.INVOICE_TYPE_SALE.equals(invoiceSerialDTO.getInvoiceType()) ? true : false;
            noStock = Const.INVOICE_SERIAL.INVOICETRANS_NOSTOCK.equals(invoiceSerialDTO.getInvoiceTrans()) ? true : false;
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happen");
        }
        topPage();
    }

    @Secured("@")
    public void prepareCopy(InvoiceSerialDTO editInvoiceSerial) {
        try {
            listBranchSelect = Lists.newArrayList();
            if (DataUtil.isNullObject(editInvoiceSerial)) {
                throw new LogicException("", "mn.invoice.serial.select.edit");
            }
            invoiceSerialDTO = invoiceSerialSv.findOne(editInvoiceSerial.getInvoiceSerialId());
            InvoiceTypeDTO invoiceTypeDTO = invoiceTypeSv.findOne(editInvoiceSerial.getInvoiceTypeId());
            invoiceSerialDTO.setInvoiceType(invoiceTypeDTO.getInvoiceType());
            invoiceTypeList = invoiceTypeSv.getInvoiceType(DataUtil.safeToLong(invoiceSerialDTO.getInvoiceType()));
            shopInfoTag.loadShop(DataUtil.safeToString(invoiceSerialDTO.getShopUsedId()), false);
            showInvoiceTransType = Const.INVOICE_TYPE.INVOICE_TYPE_SALE.equals(invoiceSerialDTO.getInvoiceType()) ? true : false;
            noStock = Const.INVOICE_SERIAL.INVOICETRANS_NOSTOCK.equals(invoiceSerialDTO.getInvoiceTrans()) ? true : false;

            listBranchSelect.add(DataUtil.safeToString(invoiceSerialDTO.getShopUsedId()));
            invoiceSerialDTO.setInvoiceSerialId(null);
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happen");
        }
        topPage();
    }

    @Secured("@")
    public void validateEdit() {
        if (DataUtil.isNullObject(invoiceSerialDTO.getInvoiceTypeId())) {
            reportError("", "", "mn.invoice.serial.not.invoice.text");
        }
        if (DataUtil.isNullObject(invoiceSerialDTO.getSerialNo())) {
            reportError("", "", "mn.invoice.serial.not.input.serial.no");
        }
        topPage();
    }

    @Secured("@")
    public void editInvoiceSerial() {
        try {
            if (!DataUtil.isNullObject(curentShop) && !DataUtil.isNullObject(curentShop.getShopId())) {
                invoiceSerialDTO.setShopUsedId(curentShop.getShopId());
            }
            invoiceSerialDTO.setInvoiceTrans(noStock ? Const.INVOICE_SERIAL.INVOICETRANS_NOSTOCK : Const.INVOICE_SERIAL.INVOICETRANS_NORMAL);
            invoiceSerialSv.update(invoiceSerialDTO, BccsLoginSuccessHandler.getStaffDTO().getStaffCode());
            forSearchInvoiceSerial = new InvoiceSerialDTO();
            forSearchInvoiceSerial.setShopId(BccsLoginSuccessHandler.getStaffDTO().getShopId());
            invoiceSerialList = invoiceSerialSv.searchInvoiceSerial(forSearchInvoiceSerial);
            reportSuccess("", "mn.invoice.serial.edit.success");
            shopInfoTag.resetShop();
            curentShop = new ShopDTO();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happen");
        }
        topPage();
    }

    @Secured("@")
    public void prepareAdd() {
        try {
            invoiceSerialDTO = new InvoiceSerialDTO();
            showBranch = false;
            invoiceSerialDTO.setInvoiceType(Const.INVOICE_TYPE.INVOICE_TYPE_SALE);
            invoiceTypeList = invoiceTypeSv.getInvoiceType(DataUtil.safeToLong(invoiceSerialDTO.getInvoiceType()));
            if (!DataUtil.isNullOrEmpty(invoiceTypeList)) {
                invoiceSerialDTO.setInvoiceTypeId(invoiceTypeList.get(0).getInvoiceTypeId());
            }
            if (DataUtil.isNullOrEmpty(listBranch)) {
                listBranch = DataUtil.defaultIfNull(shopService.getListShopAndChildShop(BccsLoginSuccessHandler.getStaffDTO().getShopPath(), BccsLoginSuccessHandler.getStaffDTO().getShopId().toString()), new ArrayList<VShopStaffDTO>());
            }
            listBranchSelect = Lists.newArrayList();
            listBranchSelect.add(BccsLoginSuccessHandler.getStaffDTO().getShopId().toString());
            if (Const.INVOICE_TYPE.INVOICE_TYPE_SALE.equals(invoiceSerialDTO.getInvoiceType())) {
                showInvoiceTransType = true;
            }
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
    public void validateAdd() {
        if (DataUtil.isNullObject(invoiceSerialDTO.getInvoiceTypeId())) {
            reportError("", "", "mn.invoice.serial.not.invoice.text");
        }
        if (DataUtil.isNullObject(invoiceSerialDTO.getSerialNo())) {
            reportError("", "", "mn.invoice.serial.not.input.serial.no");
        }
        if (DataUtil.isNullOrEmpty(listBranchSelect)) {
            reportError("", "", "mn.invoice.serial.no.branch");
//            focusElementError("selManyBranch");
        }
        topPage();
    }

    @Secured("@")
    public void addInvoiceSerial() {
        try {
            addOK = false;
            List<InvoiceSerialDTO> lstInvoiceSerial = Lists.newArrayList();
            StaffDTO staffDTO = BccsLoginSuccessHandler.getStaffDTO();
            if (DataUtil.isNullObject(staffDTO) || DataUtil.isNullObject(staffDTO.getStaffCode())) {
                throw new LogicException("", "staff.code.invalid.name");
            }
            for (String shop : listBranchSelect) {
                InvoiceSerialDTO invoiceSerialDTOs = DataUtil.cloneBean(invoiceSerialDTO);
                invoiceSerialDTOs.setDescription(DataUtil.trim(invoiceSerialDTO.getDescription()));
                invoiceSerialDTOs.setSerialNo(DataUtil.trim(invoiceSerialDTO.getSerialNo()));
                invoiceSerialDTOs.setShopId(staffDTO.getShopId());
                invoiceSerialDTOs.setShopUsedId(DataUtil.safeToLong(shop));
                invoiceSerialDTOs.setInvoiceTrans(noStock ? Const.INVOICE_SERIAL.INVOICETRANS_NOSTOCK : Const.INVOICE_SERIAL.INVOICETRANS_NORMAL);
                lstInvoiceSerial.add(invoiceSerialDTOs);
            }
            invoiceSerialSv.createNewInvoiceSerial(lstInvoiceSerial, staffDTO.getStaffCode());
            forSearchInvoiceSerial = new InvoiceSerialDTO();
            forSearchInvoiceSerial.setShopId(BccsLoginSuccessHandler.getStaffDTO().getShopId());
            invoiceSerialList = invoiceSerialSv.searchInvoiceSerial(forSearchInvoiceSerial);
            reportSuccess("", "mn.invoice.serial.not.input.create.success");
            addOK = true;
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
            addOK = false;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happen");
            addOK = false;
        }
        topPage();
    }

    @Secured("@")
    public void addInvoiceSerialNoClose() {
        addInvoiceSerial();
        if (addOK) {
            prepareAdd();
        }
    }

    @Secured("@")
    public void receiveShop(VShopStaffDTO vShopStaffDTO) {
        curentShop = new ShopDTO();
        curentShop.setShopId(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
        curentShop.setName(vShopStaffDTO.getOwnerName());
        curentShop.setShopCode(vShopStaffDTO.getOwnerCode());
        listBranchSelect.add(vShopStaffDTO.getOwnerId());
    }

    @Secured("@")
    public void showDetailBranch() {
        showBranch = true;
        listBranchTable = Lists.newArrayList();
        for (String shopId : listBranchSelect) {
            VShopStaffDTO vShopStaffDTO = getShopFromShopId(shopId);
            if (DataUtil.isNullObject(vShopStaffDTO)) {
                continue;
            }
            listBranchTable.add(vShopStaffDTO);
        }
    }

    @Secured("@")
    public void hideDetailBranch() {
        showBranch = false;
    }

    @Secured("@")
    public void removeBranch(VShopStaffDTO vShopStaffDTO) {
        if (listBranchSelect.size() == 1) {
            reportError("", "", "mn.invoice.serial.no.branch");
            return;
        }
        listBranchTable.remove(vShopStaffDTO);
        for (int i = 0; i < listBranchSelect.size(); i++) {
            String shopId = listBranchSelect.get(i);
            if (DataUtil.safeEqual(vShopStaffDTO.getOwnerId(), shopId)) {
                listBranchSelect.remove(i);
                break;
            }
        }
    }

    private VShopStaffDTO getShopFromShopId(String shopId) {
        for (VShopStaffDTO vShopStaffDTO : listBranch) {
            if (DataUtil.safeEqual(shopId, vShopStaffDTO.getOwnerId())) {
                return vShopStaffDTO;
            }
        }
        return null;
    }

    public List<OptionSetValueDTO> getListInvoiceType() {
        return listInvoiceType;
    }

    public void setListInvoiceType(List<OptionSetValueDTO> listInvoiceType) {
        this.listInvoiceType = listInvoiceType;
    }

    public List<ShopDTO> getShopList() {
        return shopList;
    }

    public void setShopList(List<ShopDTO> shopList) {
        this.shopList = shopList;
    }

    public List<InvoiceTypeDTO> getInvoiceTypeListSearch() {
        return invoiceTypeListSearch;
    }

    public void setInvoiceTypeListSearch(List<InvoiceTypeDTO> invoiceTypeListSearch) {
        this.invoiceTypeListSearch = invoiceTypeListSearch;
    }

    public List<InvoiceTypeDTO> getInvoiceTypeList() {
        return invoiceTypeList;
    }

    public void setInvoiceTypeList(List<InvoiceTypeDTO> invoiceTypeList) {
        this.invoiceTypeList = invoiceTypeList;
    }

    public InvoiceSerialDTO getForSearchInvoiceSerial() {
        return forSearchInvoiceSerial;
    }

    public void setForSearchInvoiceSerial(InvoiceSerialDTO forSearchInvoiceSerial) {
        this.forSearchInvoiceSerial = forSearchInvoiceSerial;
    }

    public List<InvoiceSerialDTO> getInvoiceSerialList() {
        return invoiceSerialList;
    }

    public void setInvoiceSerialList(List<InvoiceSerialDTO> invoiceSerialList) {
        this.invoiceSerialList = invoiceSerialList;
    }

    public List<InvoiceSerialDTO> getInvoiceListSerialSelect() {
        return invoiceListSerialSelect;
    }

    public void setInvoiceListSerialSelect(List<InvoiceSerialDTO> invoiceListSerialSelect) {
        this.invoiceListSerialSelect = invoiceListSerialSelect;
    }

    public List<ShopDTO> getFillterListBranch() {
        return fillterListBranch;
    }

    public void setFillterListBranch(List<ShopDTO> fillterListBranch) {
        this.fillterListBranch = fillterListBranch;
    }

    public ShopDTO getCurentShop() {
        return curentShop;
    }

    public void setCurentShop(ShopDTO curentShop) {
        this.curentShop = curentShop;
    }

    public InvoiceSerialDTO getForDeleteInvoiceSerial() {
        return forDeleteInvoiceSerial;
    }

    public void setForDeleteInvoiceSerial(InvoiceSerialDTO forDeleteInvoiceSerial) {
        this.forDeleteInvoiceSerial = forDeleteInvoiceSerial;
    }

    public ShopInfoNameable getShopInfoTag() {
        return shopInfoTag;
    }

    public void setShopInfoTag(ShopInfoNameable shopInfoTag) {
        this.shopInfoTag = shopInfoTag;
    }

    public InvoiceSerialDTO getInvoiceSerialDTO() {
        return invoiceSerialDTO;
    }

    public void setInvoiceSerialDTO(InvoiceSerialDTO invoiceSerialDTO) {
        this.invoiceSerialDTO = invoiceSerialDTO;
    }

    public List<VShopStaffDTO> getListBranch() {
        return listBranch;
    }

    public void setListBranch(List<VShopStaffDTO> listBranch) {
        this.listBranch = listBranch;
    }

    public List<String> getListBranchSelect() {
        return listBranchSelect;
    }

    public void setListBranchSelect(List<String> listBranchSelect) {
        this.listBranchSelect = listBranchSelect;
    }

    public boolean isShowBranch() {
        return showBranch;
    }

    public void setShowBranch(boolean showBranch) {
        this.showBranch = showBranch;
    }

    public List<VShopStaffDTO> getListBranchTable() {
        return listBranchTable;
    }

    public void setListBranchTable(List<VShopStaffDTO> listBranchTable) {
        this.listBranchTable = listBranchTable;
    }

    public boolean isShowInvoiceTransType() {
        return showInvoiceTransType;
    }

    public void setShowInvoiceTransType(boolean showInvoiceTransType) {
        this.showInvoiceTransType = showInvoiceTransType;
    }

    public boolean isNoStock() {
        return noStock;
    }

    public void setNoStock(boolean noStock) {
        this.noStock = noStock;
    }
}
