package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.ReasonService;
import com.viettel.bccs.inventory.service.StockIsdnTransDetailService;
import com.viettel.bccs.inventory.service.StockIsdnTransService;
import com.viettel.bccs.inventory.service.StockTransService;
import com.viettel.bccs.inventory.tag.impl.ShopInfoTag;
import com.viettel.bccs.inventory.tag.impl.StaffInfoTag;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.BundleUtil;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.web.common.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.text.Collator;
import java.util.*;

/**
 * Created by sinhhv on 1/8/2016.
 */
@Component
@Scope("view")
@ManagedBean
public class ManageTransStockIsdnController extends BaseController {
    private String TYPE_SHOP = "1";
    private String TYPE_STAFF = "2";

    @Autowired
    private ShopInfoTag shopReceiveInfoTag;
    @Autowired
    private StaffInfoTag staffReceiveInfoTag;
    @Autowired
    private ShopInfoTag shopGetInfoTag;
    @Autowired
    private StaffInfoTag staffGetInfoTag;
    @Autowired
    private StaffInfoTag createStaffInfoTag;
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    StockIsdnTransService stockIsdnTransService;
    @Autowired
    StockIsdnTransDetailService stockIsdnTransDetailService;
    @Autowired
    private ReasonService reasonService;

    private String typeReceiptStock;
    private String typeDeliverStock;
    private ManageTransStockDto transStock = new ManageTransStockDto();
    private boolean renderDetail = true;
    private List<StockIsdnTransDTO> listTrans = Lists.newArrayList();
    private List<ReasonDTO> listReason = Lists.newArrayList();
    private List<StockIsdnTransDetailDTO> listTransDetail = Lists.newArrayList();

    @PostConstruct
    public void init() {
        try {
            shopReceiveInfoTag.initShopMangeIsdnTrans(this, BccsLoginSuccessHandler.getStaffDTO().getShopId().toString(), "8", null, Const.MODE_SHOP.ISDN_MNGT);
            shopGetInfoTag.initShopMangeIsdnTrans(this, BccsLoginSuccessHandler.getStaffDTO().getShopId().toString(), "8", null, Const.MODE_SHOP.ISDN_MNGT);
            staffGetInfoTag.initStaffManageIsdn(this, BccsLoginSuccessHandler.getStaffDTO().getShopId().toString(), "14", Const.MODE_SHOP.ISDN_MNGT, null);
            staffReceiveInfoTag.initStaffManageIsdn(this, BccsLoginSuccessHandler.getStaffDTO().getShopId().toString(), "14", Const.MODE_SHOP.ISDN_MNGT, null);
            createStaffInfoTag.initStaffManageIsdn(this, BccsLoginSuccessHandler.getStaffDTO().getShopId().toString(), "14", Const.MODE_SHOP.ISDN_MNGT_USR_CRT, transStock);
            listReason = reasonService.getLsReasonByType(Const.REASON_TYPE.DISTRIBUTE_ISDN_TYPE);
            List<ReasonDTO> lstReasonAuto = reasonService.getLsReasonByType(Const.REASON_TYPE.DISTRIBUTE_ISDN_AUTO_TYPE);
            if (!DataUtil.isNullOrEmpty(lstReasonAuto)) {
                if (DataUtil.isNullOrEmpty(listReason)) {
                    listReason = Lists.newArrayList(lstReasonAuto);
                } else {
                    listReason.addAll(lstReasonAuto);
                }
            }
            if (!DataUtil.isNullOrEmpty(listReason)) {
                Collections.sort(listReason, new Comparator<ReasonDTO>() {
                    @Override
                    public int compare(ReasonDTO o1, ReasonDTO o2) {
                        Collator collate = Collator.getInstance(new Locale("vi"));
                        if (DataUtil.isNullOrEmpty(o1.getReasonName())) {
                            return -1;
                        }
                        if (DataUtil.isNullOrEmpty(o2.getReasonName())) {
                            return -1;
                        }
                        return collate.compare(o1.getReasonName(), o2.getReasonName());
                    }
                });
            }
            transStock.setFromDate(new Date());
            transStock.setToDate(new Date());
            renderDetail = false;
            search();
            focusElementWithCursor(".typeServiceTxt");
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmManaTransDigital:messages", "", "common.error.happen");
        }
    }

    private boolean validate() {
        if (DataUtil.isNullObject(transStock.getFromDate())) {
            reportError("frmManaTransDigital:messages", "", "mn.stock.from.date.not.blank");
            focusElementError("txtFromDate");
            return false;
        }
        if (DataUtil.isNullObject(transStock.getToDate())) {
            reportError("frmManaTransDigital:messages", "", "mn.stock.to.date.not.blank");
            focusElementError("txtToDate");
            return false;
        }
        if (transStock.getFromDate().after(transStock.getToDate())) {
            reportError("frmManaTransDigital:messages", "", "stock.trans.from.than.to");
            focusElementError("txtToDate");
            return false;
        }
        long days = 11;
        try {
            days = DateUtil.daysBetween2Dates(transStock.getToDate(), transStock.getFromDate());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        if (days > 10) {
            reportError("frmManaTransDigital:messages", "", "trans.stock.from.to.over");
            focusElementError("txtToDate");
            return false;
        }
        return true;
    }

    @Secured("@")
    public void search() {
        try {
            if (validate()) {
                listTrans = stockIsdnTransService.search(transStock);
                reportSuccess("frmManaTransDigital:messages", "mn.invoice.invoiceType.searchData", DataUtil.isNullOrEmpty(listTrans) ? 0 : listTrans.size());
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmManaTransDigital:messages", ex.getErrorCode(), ex.getKeyMsg());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmManaTransDigital:messages", "", "common.error.happen");
        }
    }

    public void viewDetail(Long stockTransId) {
        try {
            renderDetail = true;
            listTransDetail = stockIsdnTransDetailService.searchByStockIsdnTransId(stockTransId);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmManaTransDigital:messages", "", "common.error.happen");
        }
    }

    @Secured("@")
    public void resetForm() {
        this.transStock = new ManageTransStockDto();
//        listTrans = Lists.newArrayList();
        transStock.setFromDate(new Date());
        transStock.setToDate(new Date());
        this.renderDetail = false;
        typeDeliverStock = TYPE_SHOP;
        typeReceiptStock = TYPE_SHOP;
        shopReceiveInfoTag.doClearShop("clearReceiveStock");
        staffReceiveInfoTag.doClearStaff("clearReceiveStock");
        shopGetInfoTag.doClearShop("clearTakeStock");
        staffGetInfoTag.doClearStaff("clearTakeStock");
        createStaffInfoTag.doClearStaff("clearCreateStaff");
    }

    @Secured("@")
    public void receiveStock(VShopStaffDTO dto) {
        this.transStock.setTypeReceiveOwner(dto.getOwnerType());
        this.transStock.setReceiveOwnerStockId(dto.getOwnerId());
        if (!DataUtil.isNullObject(shopReceiveInfoTag) && !DataUtil.isNullObject(shopReceiveInfoTag.getvShopStaffDTO())) {
            this.transStock.setFromShopId(shopReceiveInfoTag.getvShopStaffDTO().getOwnerId());
        }
    }

    @Secured("@")
    public void clearReceiveStock() {
        this.transStock.setTypeReceiveOwner(null);
        this.transStock.setReceiveOwnerStockId(null);
    }

    @Secured("@")
    public void takeStock(VShopStaffDTO dto) {
        this.transStock.setTypeTakeOwner(dto.getOwnerType());
        this.transStock.setTakeOwnerStockId(dto.getOwnerId());
        createStaffInfoTag.initStaffManageIsdn(this, BccsLoginSuccessHandler.getStaffDTO().getShopId().toString(), "14", Const.MODE_SHOP.ISDN_MNGT_USR_CRT, transStock);
    }

    @Secured("@")
    public void clearTakeStock() {
        this.transStock.setTypeTakeOwner(null);
        this.transStock.setTakeOwnerStockId(null);
        createStaffInfoTag.initStaffManageIsdn(this, BccsLoginSuccessHandler.getStaffDTO().getShopId().toString(), "14", Const.MODE_SHOP.ISDN_MNGT_USR_CRT, transStock);
    }

    @Secured("@")
    public String getStatus(Long status) {
        if (status.longValue() == 1) {
            return getText("mn.isdn.manage.transaction.stock.status.sccs");
        } else {
            return getText("mn.isdn.manage.transaction.stock.status.fail");
        }
    }

    @Secured("@")
    public void createStaff(VShopStaffDTO dto) {
        this.transStock.setOwnerCreateId(dto.getOwnerId());
    }

    @Secured("@")
    public void clearCreateStaff() {
        this.transStock.setOwnerCreateId(null);
    }

    public ShopInfoTag getShopReceiveInfoTag() {
        return shopReceiveInfoTag;
    }

    public void setShopReceiveInfoTag(ShopInfoTag shopReceiveInfoTag) {
        this.shopReceiveInfoTag = shopReceiveInfoTag;
    }

    public StaffInfoTag getStaffReceiveInfoTag() {
        return staffReceiveInfoTag;
    }

    public void setStaffReceiveInfoTag(StaffInfoTag staffReceiveInfoTag) {
        this.staffReceiveInfoTag = staffReceiveInfoTag;
    }

    public ShopInfoTag getShopGetInfoTag() {
        return shopGetInfoTag;
    }

    public void setShopGetInfoTag(ShopInfoTag shopGetInfoTag) {
        this.shopGetInfoTag = shopGetInfoTag;
    }

    public StaffInfoTag getStaffGetInfoTag() {
        return staffGetInfoTag;
    }

    public StaffInfoTag getCreateStaffInfoTag() {
        return createStaffInfoTag;
    }

    public void setCreateStaffInfoTag(StaffInfoTag createStaffInfoTag) {
        this.createStaffInfoTag = createStaffInfoTag;
    }

    public void setStaffGetInfoTag(StaffInfoTag staffGetInfoTag) {
        this.staffGetInfoTag = staffGetInfoTag;
    }

    public ManageTransStockDto getTransStock() {
        return transStock;
    }

    public void setTransStock(ManageTransStockDto transStock) {
        this.transStock = transStock;
    }

    public String getTypeReceiptStock() {
        return typeReceiptStock;
    }

    public void setTypeReceiptStock(String typeReceiptStock) {
        this.typeReceiptStock = typeReceiptStock;
    }

    public String getTypeDeliverStock() {
        return typeDeliverStock;
    }

    public boolean isRenderDetail() {
        return renderDetail;
    }

    public void setRenderDetail(boolean renderDetail) {
        this.renderDetail = renderDetail;
    }

    public void setTypeDeliverStock(String typeDeliverStock) {
        this.typeDeliverStock = typeDeliverStock;
    }

    public String getTYPE_SHOP() {
        return TYPE_SHOP;
    }

    public void setTYPE_SHOP(String TYPE_SHOP) {
        this.TYPE_SHOP = TYPE_SHOP;
    }

    public String getTYPE_STAFF() {
        return TYPE_STAFF;
    }

    public List<StockIsdnTransDTO> getListTrans() {
        return listTrans;
    }

    public void setListTrans(List<StockIsdnTransDTO> listTrans) {
        this.listTrans = listTrans;
    }

    public List<StockIsdnTransDetailDTO> getListTransDetail() {
        return listTransDetail;
    }

    public void setListTransDetail(List<StockIsdnTransDetailDTO> listTransDetail) {
        this.listTransDetail = listTransDetail;
    }

    public void setTYPE_STAFF(String TYPE_STAFF) {
        this.TYPE_STAFF = TYPE_STAFF;
    }

    public String getStockTransStatusName(String status) {
        return BundleUtil.getText("stock.trans.status." + status);
    }

    public List<ReasonDTO> getListReason() {
        return listReason;
    }

    public void setListReason(List<ReasonDTO> listReason) {
        this.listReason = listReason;
    }
}
