package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.controller.InventoryController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.model.FileExportBean;
import com.viettel.bccs.inventory.common.util.FileUtil;
import com.viettel.bccs.inventory.dto.StaffTagConfigDTO;
import com.viettel.bccs.inventory.dto.StockDebitDTO;
import com.viettel.bccs.inventory.dto.VShopStaffDTO;
import com.viettel.bccs.inventory.dto.VStockTransDTO;
import com.viettel.bccs.inventory.service.OptionSetValueService;
import com.viettel.bccs.inventory.service.StockDebitService;
import com.viettel.bccs.inventory.service.StockTransService;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.bccs.inventory.tag.StaffInfoNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.util.List;

/**
 * @author tuyendv8.
 * @comment
 * @date 11/19/2015.
 */
@Component
@Scope("view")
@ManagedBean
public class LimitStockLookupController extends InventoryController {
    @Autowired
    OptionSetValueService optionSetValueService;
    @Autowired
    StockTransService transService;


    @Autowired
    private ShopInfoNameable shopInfoTag;
    @Autowired
    private StaffInfoNameable staffInfoTag;
    @Autowired
    private StockDebitService stockDebitService;
    private StockDebitDTO stockDebitDTO, selectedStockDebit;
    List<StockDebitDTO> stockDebitDTOs;
    private List<VStockTransDTO> suspendDetails;
    private boolean viewHangTrans;
    private boolean inputShop;
    StaffTagConfigDTO staffTagConfigDTO = new StaffTagConfigDTO("", DataUtil.safeToString(getStaffDTO().getShopId()), null);

    @PostConstruct
    public void init() {
        initControls();
    }

    public void initControls() {
        try {
            shopInfoTag.initShopAndAllChild(this, DataUtil.safeToString(getStaffDTO().getShopId()), true,Const.OWNER_TYPE.SHOP);
            //
            staffInfoTag.init(this, staffTagConfigDTO);
            //
            stockDebitDTOs = Lists.newArrayList();
            suspendDetails = Lists.newArrayList();
            stockDebitDTO = new StockDebitDTO();
            stockDebitDTO.setOwnerType(Const.OWNER_TYPE.SHOP);
            viewHangTrans = false;
            executeCommand("updateSelect()");
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    @Secured("@")
    public void doSearch() {
        try {
            viewHangTrans = false;
            suspendDetails = Lists.newArrayList();
            stockDebitDTOs = stockDebitService.findStockDebitNative(stockDebitDTO.getOwnerId(), stockDebitDTO.getOwnerType());
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getKeyMsg());
        } catch (Exception e) {
            reportError("", "common.error.happen", e);
        }
    }

    @Secured("@")
    public void clearOwner() {
        stockDebitDTO.setOwnerId(null);
    }

    @Secured("@")
    public void changeViewHangTrans(StockDebitDTO selected) {
        try {
            if (DataUtil.isNullObject(selected)) {
                viewHangTrans = false;
            } else {
                suspendDetails = transService.findStockSuspendDetail(selected.getOwnerId(), selected.getOwnerType());
                viewHangTrans = true;
            }
            this.selectedStockDebit = selected;
        } catch (Exception ex) {
            reportError("", "common.error.happen", ex);
            topPage();
        }
    }

    @Secured("@")
    public void resetSearch() {
        initControls();
    }

    @Secured("@")
    public void receiveShop(VShopStaffDTO vShopStaffDTO) {
        stockDebitDTO.setOwnerId(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
    }

    @Secured("@")
    public void receiveStaff(VShopStaffDTO vShopStaffDTO) {
        stockDebitDTO.setOwnerId(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
    }

    @Secured("@")
    public StreamedContent exportFile() {
        if (DataUtil.isNullOrEmpty(suspendDetails)) {
            topReportError("", ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "stock.limit.ui.suspend.nonerecords");
        } else {
            try {
                FileExportBean bean = new FileExportBean();
                bean.setOutName("stock_trans_suspend.xls");
                bean.setOutPath(FileUtil.getOutputPath());
                bean.setTempalatePath(FileUtil.getTemplatePath());
                bean.putValue("stock", selectedStockDebit.getOwnerCode());
                bean.setTemplateName("stock_trans_suspend_template.xls");
                bean.putValue("lstData", suspendDetails);
                bean.putValue("createUser", getStaffDTO().getStaffCode());
                bean.putValue("createDate", optionSetValueService.getSysdateFromDB(true));
                return FileUtil.exportToStreamed(bean);
//                FileUtil.exportFile(bean);
//                return bean.getExportFileContent();
            } catch (Exception ex) {
                reportError("", "common.error.happen", ex);
                topPage();
            }
        }
        return null;
    }

    public void changeTypeService() {
        try {
            if (Const.OWNER_TYPE.SHOP.equals(stockDebitDTO.getOwnerType())) {
                inputShop = true;
            } else {
                inputShop = false;
            }
            shopInfoTag.initShopAndAllChild(this, DataUtil.safeToString(getStaffDTO().getShopId()), true,Const.OWNER_TYPE.SHOP);
            staffInfoTag.initStaff(this, DataUtil.safeToString(getStaffDTO().getShopId()));
            //
            staffInfoTag.init(this, staffTagConfigDTO);
            stockDebitDTOs = Lists.newArrayList();
            suspendDetails = Lists.newArrayList();
            if (!DataUtil.isNullObject(stockDebitDTO)) {
                stockDebitDTO.setOwnerId(null);
            }
            executeCommand("updateSelect()");
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }

    }

    public StockDebitDTO getStockDebitDTO() {
        return stockDebitDTO;
    }

    public void setStockDebitDTO(StockDebitDTO stockDebitDTO) {
        this.stockDebitDTO = stockDebitDTO;
    }

    public StockDebitDTO getSelectedStockDebit() {
        return selectedStockDebit;
    }

    public void setSelectedStockDebit(StockDebitDTO selectedStockDebit) {
        this.selectedStockDebit = selectedStockDebit;
    }

    public List<StockDebitDTO> getStockDebitDTOs() {
        return stockDebitDTOs;
    }

    public void setStockDebitDTOs(List<StockDebitDTO> stockDebitDTOs) {
        this.stockDebitDTOs = stockDebitDTOs;
    }

    public boolean isViewHangTrans() {
        return viewHangTrans;
    }

    public void setViewHangTrans(boolean viewHangTrans) {
        this.viewHangTrans = viewHangTrans;
    }

    public boolean isInputShop() {
        return inputShop;
    }

    public void setInputShop(boolean inputShop) {
        this.inputShop = inputShop;
    }

    public List<VStockTransDTO> getSuspendDetails() {
        return suspendDetails;
    }

    public void setSuspendDetails(List<VStockTransDTO> suspendDetails) {
        this.suspendDetails = suspendDetails;
    }

    public StaffInfoNameable getStaffInfoTag() {
        return staffInfoTag;
    }

    public void setStaffInfoTag(StaffInfoNameable staffInfoTag) {
        this.staffInfoTag = staffInfoTag;
    }

    public ShopInfoNameable getShopInfoTag() {
        return shopInfoTag;
    }

    public void setShopInfoTag(ShopInfoNameable shopInfoTag) {
        this.shopInfoTag = shopInfoTag;
    }

}
