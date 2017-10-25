package com.viettel.bccs.inventory.controller.invoice;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.inventory.dto.InvoiceTemplateDTO;
import com.viettel.bccs.inventory.dto.InvoiceTemplateTypeDTO;
import com.viettel.bccs.inventory.dto.VShopStaffDTO;
import com.viettel.bccs.inventory.service.InvoiceTemplateService;
import com.viettel.bccs.inventory.service.InvoiceTemplateTypeService;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.bccs.inventory.tag.StaffInfoNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.web.common.controller.BaseController;
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
public class AssignInvoiceTemplateController extends BaseController {
    public static final String DELIVERY = "0";
    public static final String RETRIEVE = "1";
    public static final String TYPE_STAFF = "2";
    public static final String TYPE_SHOP = "1";

    @Autowired
    private StaffInfoNameable staffInfoTag;
    @Autowired
    private ShopInfoNameable shopInfoTag;
    @Autowired
    private InvoiceTemplateService invoiceTemplateSv;
    @Autowired
    private InvoiceTemplateTypeService invoiceTemplateTypeSv;

    private List<InvoiceTemplateDTO> lstParentInvoiceTemplate;
    private List<InvoiceTemplateDTO> lstChildInvoiceTemplate;
    private InvoiceTemplateDTO forInvoiceTemplate;
    private String workMode;
    private String type;
    private InvoiceTemplateDTO parInTemplate;
    private boolean viewProcess;
    private VShopStaffDTO curShopStaff;

    @Secured("@")
    @PostConstruct
    public void init() {
        try {
            shopInfoTag.initShop(this, BccsLoginSuccessHandler.getStaffDTO().getShopId().toString(), false);
            staffInfoTag.initStaff(this, BccsLoginSuccessHandler.getStaffDTO().getShopId().toString());
            lstChildInvoiceTemplate = Lists.newArrayList();
            viewProcess = false;
            lstParentInvoiceTemplate = invoiceTemplateSv.getInvoiceTemplateList(BccsLoginSuccessHandler.getStaffDTO().getShopId());
            if (DataUtil.isNullOrEmpty(lstParentInvoiceTemplate)) {
                throw new LogicException("", "mn.invoice.info.not.template");
            }
            for (InvoiceTemplateDTO invoiceTemp : lstParentInvoiceTemplate) {
                InvoiceTemplateTypeDTO invoiceTempTypeDTO = invoiceTemplateTypeSv.findOne(invoiceTemp.getInvoiceTemplateTypeId());
                invoiceTemp.setInvoiceTemplateTypeName(invoiceTempTypeDTO.getName());
                invoiceTemp.setFromShopName(BccsLoginSuccessHandler.getStaffDTO().getShopName());
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
    public void selectInvoiceParent() {
        if (!DataUtil.isNullObject(parInTemplate)) {
            viewProcess = true;
            workMode = DELIVERY;
            forInvoiceTemplate = new InvoiceTemplateDTO();
            forInvoiceTemplate.setOwnerType(TYPE_SHOP);
            lstChildInvoiceTemplate = Lists.newArrayList();
        }
    }

    @Secured("@")
    public void selectWorkMode() {
        lstChildInvoiceTemplate = Lists.newArrayList();
        forInvoiceTemplate = new InvoiceTemplateDTO();
        forInvoiceTemplate.setOwnerType(TYPE_SHOP);
    }

    @Secured("@")
    public void changeOwnerType() {
        lstChildInvoiceTemplate = Lists.newArrayList();
    }

    @Secured("@")
    public void receiveShop(VShopStaffDTO vShopStaffDTO) {
        curShopStaff = DataUtil.cloneBean(vShopStaffDTO);
    }

    private void getOwnerId(VShopStaffDTO vShopStaffDTO) throws Exception {
        InvoiceTemplateDTO invoiceTemplate;
        List<InvoiceTemplateDTO> list = invoiceTemplateSv.getInvoiceTemplateExsitByOwnerType(Long.parseLong(vShopStaffDTO.getOwnerId()), parInTemplate.getInvoiceTemplateTypeId(), forInvoiceTemplate.getOwnerType());
        if (DELIVERY.equals(workMode)) {
            if (DataUtil.isNullOrEmpty(list)) {
                invoiceTemplate = new InvoiceTemplateDTO();
                invoiceTemplate.setInvoiceTemplateTypeId(parInTemplate.getInvoiceTemplateTypeId());
                invoiceTemplate.setOwnerType(forInvoiceTemplate.getOwnerType());
                invoiceTemplate.setOwnerId(Long.parseLong(vShopStaffDTO.getOwnerId()));
                invoiceTemplate.setAmount(0l);
                invoiceTemplate.setUsedAmount(0l);
            } else {
                invoiceTemplate = list.get(0);
            }
        } else {
            if (DataUtil.isNullOrEmpty(list)) {
                throw new LogicException("", "mn.invoice.info.revoke.invoice.not.find.staff.shop");
            }
            invoiceTemplate = list.get(0);
        }
        invoiceTemplate.setOwnerdName(vShopStaffDTO.getOwnerName());
        validateInvoiceTemplate(invoiceTemplate, lstChildInvoiceTemplate);
        lstChildInvoiceTemplate.add(invoiceTemplate);

    }

    public void addInvoiceTemp() {
        try {
//            if (DataUtil.isNullObject(curShopStaff)) {
//                throw new LogicException("", "mn.invoice.info.require.shop");
//            }
            getOwnerId(curShopStaff);
            shopInfoTag.resetShop();
            staffInfoTag.resetProduct();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
    }

    @Secured("@")
    public void receiveStaff(VShopStaffDTO vShopStaffDTO) {
        curShopStaff = DataUtil.cloneBean(vShopStaffDTO);
    }

    @Secured("@")
    public void validatePreRetrieveInvoice() {
        try {
            if (DataUtil.isNullOrEmpty(lstChildInvoiceTemplate)) {
                throw new LogicException("", "mn.invoice.info.revoke.invoice.not.template");
            }
            for (InvoiceTemplateDTO invoiceTemplate : lstChildInvoiceTemplate) {
                if (DataUtil.isNullOrZero(invoiceTemplate.getAmountRetrieve())) {
                    throw new LogicException("", "mn.invoice.info.retreve.num.not");
                }
                if (invoiceTemplate.getAmountRetrieve() > invoiceTemplate.getAmount()) {
                    throw new LogicException("", "mn.invoice.info.revoke.invoice.template", invoiceTemplate.getOwnerdName());
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
    public void retrieveInvoice() {
        try {
            if (DataUtil.isNullOrEmpty(lstChildInvoiceTemplate)) {
                throw new LogicException("", "mn.invoice.info.revoke.invoice.not.template");
            }
            Long totalAmount = 0l;
            for (InvoiceTemplateDTO invoiceTemplate : lstChildInvoiceTemplate) {
                Long amountNotUsed = invoiceTemplate.getAmount();
                invoiceTemplate.setAmountNotUsed(amountNotUsed);
                invoiceTemplate.setAmountDelivery(invoiceTemplate.getAmount());
                invoiceTemplate.setAmount(invoiceTemplate.getAmountRetrieve());
                totalAmount = totalAmount + invoiceTemplate.getAmount();
            }
            BaseMessage ret = invoiceTemplateSv.retrieveInvoiceTempalte(parInTemplate.getInvoiceTemplateId(), lstChildInvoiceTemplate, BccsLoginSuccessHandler.getStaffDTO().getStaffCode());
            if ("0".equals(ret.getErrorCode())) {
                reportSuccess("", "mn.invoice.info.revoke.invoice.success");
                for (InvoiceTemplateDTO invoiceTemplate : lstChildInvoiceTemplate) {
                    Long amount = invoiceTemplate.getAmountDelivery() - invoiceTemplate.getAmount();
                    invoiceTemplate.setAmount(amount);
                    invoiceTemplate.setAmountRetrieve(null);
                }
                Long totalAmountPar = parInTemplate.getAmount() + totalAmount;
                parInTemplate.setAmount(totalAmountPar);
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
    public void validatePreDeliveryInvoice() {
        try {
            if (DataUtil.isNullOrEmpty(lstChildInvoiceTemplate)) {
                throw new LogicException("", "mn.invoice.info.revoke.invoice.not.input.child");
            }
            Long totalAmount = 0l;
            for (InvoiceTemplateDTO invoiceTemplate : lstChildInvoiceTemplate) {
                if (DataUtil.isNullOrZero(invoiceTemplate.getAmountDelivery())) {
                    throw new LogicException("", "mn.invoice.info.deleive.num.not");
                }
                totalAmount = totalAmount + invoiceTemplate.getAmountDelivery();
            }
            if (totalAmount > parInTemplate.getAmount()) {
                throw new LogicException("", "mn.invoice.info.revoke.invoice.validate.faild");
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
    public void remove(InvoiceTemplateDTO invoiceTemplate) {
        if (!DataUtil.isNullOrEmpty(lstChildInvoiceTemplate)) {
            lstChildInvoiceTemplate.remove(invoiceTemplate);
        }
    }

    @Secured("@")
    public void deliveryInvoice() {
        try {
            if (DataUtil.isNullOrEmpty(lstChildInvoiceTemplate)) {
                throw new LogicException("", "mn.invoice.info.revoke.invoice.not.input.deliver");
            }
            Long totalAmount = 0l;
            for (InvoiceTemplateDTO invoiceTemplate : lstChildInvoiceTemplate) {
                Long amountNotUsed = invoiceTemplate.getAmount();
                invoiceTemplate.setAmountNotUsed(amountNotUsed);
                invoiceTemplate.setAmountRetrieve(invoiceTemplate.getAmount());
                invoiceTemplate.setAmount(invoiceTemplate.getAmountDelivery());
                totalAmount = totalAmount + invoiceTemplate.getAmountDelivery();
            }
            BaseMessage ret = invoiceTemplateSv.deliveryInvoiceTempalte(parInTemplate.getInvoiceTemplateId(), lstChildInvoiceTemplate, BccsLoginSuccessHandler.getStaffDTO().getStaffCode());
            if (ret.isSuccess()) {
                reportSuccess("", "mn.invoice.info.deleive.success");
                for (InvoiceTemplateDTO invoiceTemplate : lstChildInvoiceTemplate) {
                    Long amount = invoiceTemplate.getAmount() + invoiceTemplate.getAmountRetrieve();
                    invoiceTemplate.setAmount(amount);
                    invoiceTemplate.setAmountDelivery(null);
                }
                Long totalAmountPar = parInTemplate.getAmount() - totalAmount;
                parInTemplate.setAmount(totalAmountPar);

            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());

        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
    }

    private void validateInvoiceTemplate(InvoiceTemplateDTO invoiceTemplate, List<InvoiceTemplateDTO> list) throws Exception {
        if (!DataUtil.isNullOrEmpty(list)) {
            for (InvoiceTemplateDTO invoiceTemp : list) {
                if (DataUtil.safeEqual(invoiceTemp.getOwnerId(), invoiceTemplate.getOwnerId())) {
                    if (TYPE_SHOP.equals(invoiceTemplate.getOwnerType())) {
                        throw new LogicException("", "mn.invoice.info.deleive.unit.exits");
                    } else {
                        throw new LogicException("", "mn.invoice.info.deleive.staff.exits");
                    }
                }
            }
        }
    }

    public InvoiceTemplateDTO getForInvoiceTemplate() {
        return forInvoiceTemplate;
    }

    public void setForInvoiceTemplate(InvoiceTemplateDTO forInvoiceTemplate) {
        this.forInvoiceTemplate = forInvoiceTemplate;
    }

    public boolean isViewProcess() {
        return viewProcess;
    }

    public void setViewProcess(boolean viewProcess) {
        this.viewProcess = viewProcess;
    }

    public InvoiceTemplateDTO getParInTemplate() {
        return parInTemplate;
    }

    public void setParInTemplate(InvoiceTemplateDTO parInTemplate) {
        this.parInTemplate = parInTemplate;
    }

    public List<InvoiceTemplateDTO> getLstParentInvoiceTemplate() {
        return lstParentInvoiceTemplate;
    }

    public void setLstParentInvoiceTemplate(List<InvoiceTemplateDTO> lstParentInvoiceTemplate) {
        this.lstParentInvoiceTemplate = lstParentInvoiceTemplate;
    }

    public List<InvoiceTemplateDTO> getLstChildInvoiceTemplate() {
        return lstChildInvoiceTemplate;
    }

    public void setLstChildInvoiceTemplate(List<InvoiceTemplateDTO> lstChildInvoiceTemplate) {
        this.lstChildInvoiceTemplate = lstChildInvoiceTemplate;
    }

    public String getWorkMode() {
        return workMode;
    }

    public void setWorkMode(String workMode) {
        this.workMode = workMode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ShopInfoNameable getShopInfoTag() {
        return shopInfoTag;
    }

    public void setShopInfoTag(ShopInfoNameable shopInfoTag) {
        this.shopInfoTag = shopInfoTag;
    }

    public StaffInfoNameable getStaffInfoTag() {
        return staffInfoTag;
    }

    public void setStaffInfoTag(StaffInfoNameable staffInfoTag) {
        this.staffInfoTag = staffInfoTag;
    }
}
