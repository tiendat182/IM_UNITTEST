package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.model.FileExportBean;
import com.viettel.bccs.inventory.common.util.FileUtil;
import com.viettel.bccs.inventory.dto.OptionSetValueDTO;
import com.viettel.bccs.inventory.dto.StaffDTO;
import com.viettel.bccs.inventory.dto.VActionGoodsStatisticDTO;
import com.viettel.bccs.inventory.dto.VShopStaffDTO;
import com.viettel.bccs.inventory.service.OptionSetValueService;
import com.viettel.bccs.inventory.service.StockTransActionService;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.bccs.inventory.tag.StaffInfoNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.ErrorCode;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.util.Date;
import java.util.List;

/**
 * controller tra cu serial don le
 * @author thanhnt77
 */
@Component
@Scope("view")
@ManagedBean
public class GatherTransferOrderController extends TransCommonController {

    private String actionCode;//ma lenh/phieu
    private String transType;//loai giao dich
    private String typeOrderNote;//loai lenh/phieu
    private Long fromOwnerId;//ma kho xuat
    private Long fromOwnerType;
    private Date fromDate;
    private Date endDate;
    private String status;
    private String ownerTypeExport;
    private int limitAutoComplete;

    private StaffDTO staffDTO;
    private List<VActionGoodsStatisticDTO> lsGoodsStatisticDTOs;
    private List<OptionSetValueDTO> stockTransStatus;

    @Autowired
    private ShopInfoNameable shopInfoTagExport;

    @Autowired
    private StaffInfoNameable staffInfoTagExport;

    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private StockTransActionService stockTransActionService;

    @PostConstruct
    public void init() {
        try {
            initInputSearch();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception e) {
            logger.error(e);
            reportError("", "common.error.happened", e);
            topPage();
        }
    }

    /**
     * ham xu ly reset thong tin tim kiem
     * @author thanhnt77
     */

    private void initInputSearch() throws Exception {
        endDate = getSysdateFromDB();
        fromDate = endDate;
        staffDTO = BccsLoginSuccessHandler.getStaffDTO();
        fromOwnerId = staffDTO.getShopId();
        fromOwnerType = Const.OWNER_TYPE.SHOP_LONG;
        ownerTypeExport = null;


        staffInfoTagExport.initStaff(this, DataUtil.safeToString(staffDTO.getShopId()));

        shopInfoTagExport.initShopTransfer(this, DataUtil.safeToString(staffDTO.getShopId()), Const.MODE_SHOP.LIST_SHOP_TRANSFER);

        actionCode = null;
        limitAutoComplete = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_AUTOCOMPLETE));
        lsGoodsStatisticDTOs = Lists.newArrayList();
        stockTransStatus = getOptionValByCode(Const.OPTION_SET.STOCK_TRANS_STATUS);
    }

    @Secured("@")
    public void doSearchOrderTransfer() {
        try {
            if (fromDate == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.from.date.not.blank");
            }
            if (endDate == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.to.date.not.blank");
            }
            if ((DateUtil.compareDateToDate(fromDate, endDate) > 0) || DateUtil.daysBetween2Dates(endDate, fromDate) > 30L) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT,  "view.stock.offer.cycel.fromDate.endDate", 30);
            }
            if (DataUtil.isNullOrEmpty(ownerTypeExport)) {
                fromOwnerId = null;
                fromOwnerType = null;
            }

            lsGoodsStatisticDTOs = stockTransActionService.getListVActionStatistic(actionCode, fromOwnerId, fromOwnerType,staffDTO.getShopId(), Const.OWNER_TYPE.SHOP_LONG,
                    fromDate, endDate, this.transType, this.typeOrderNote );
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception e) {
            logger.error(e);
            reportError("", "common.error.happened", e);
            topPage();
        }
    }

    @Secured("@")
    public void doResetOrderTransfer() {
        init();
    }

    @Secured("@")
    public StreamedContent doExportAction() {
        try {
            Date currentDate = getSysdateFromDB();
            FileExportBean fileExportBean = new FileExportBean();
            fileExportBean.setTempalatePath(FileUtil.getTemplatePath());
            fileExportBean.setTemplateName(Const.REPORT_TEMPLATE.STOCK_TRANS_TEMPLATE_SUB_PATH + Const.FILE_TEMPLATE.TEMPLATE_GATHER_TRANSFER_ORDER);
            fileExportBean.setOutPath(FileUtil.getOutputPath());
            fileExportBean.setOutName(BccsLoginSuccessHandler.getStaffDTO().getStaffCode()
                    + "_" + DateUtil.dateTime2StringNoSlash(currentDate) + "_" + Const.FILE_TEMPLATE.TEMPLATE_GATHER_TRANSFER_ORDER);
            fileExportBean.putValue("reportDate", DateUtil.date2ddMMyyyyString(currentDate));
            fileExportBean.putValue("lsGoodsStatisticDTOs", lsGoodsStatisticDTOs);
            return FileUtil.exportToStreamed(fileExportBean);
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception e) {
            logger.error(e);
            reportError("", "common.error.happened", e);
            topPage();
        }
        return null;
    }

    /**
     * ham xu ly nhan action ky office hay ko
     *
     * @author ThanhNT77
     */
    @Secured("@")
    public void receiveExport(VShopStaffDTO vShopStaffDTO) {
        if (vShopStaffDTO.getOwnerId() != null && !DataUtil.isNullOrEmpty(vShopStaffDTO.getOwnerType())) {
            fromOwnerId = DataUtil.safeToLong(vShopStaffDTO.getOwnerId());
            fromOwnerType = DataUtil.safeToLong(vShopStaffDTO.getOwnerType());
        }
    }

    /**
     * clear xuat
     * @author thanhnt77
     */
    @Secured("@")
    public void clearExport() {
        fromOwnerId = null;
        //fromOwnerType = null;
    }


    /**
     * thay doi loai kho xuat
     * @author thanhnt77
     */
    @Secured("@")
    public void changeOwnerTypeExport() {
        try {
            fromOwnerId = null;
            fromOwnerType = null;
            shopInfoTagExport.resetShop();
            staffInfoTagExport.resetProduct();
            if (Const.OWNER_TYPE.SHOP.equals(ownerTypeExport)) {
                fromOwnerId = staffDTO.getShopId();
                fromOwnerType = Const.OWNER_TYPE.SHOP_LONG;
                shopInfoTagExport.loadShop(DataUtil.safeToString(fromOwnerId), false);
            } else if (Const.OWNER_TYPE.STAFF.equals(ownerTypeExport)) {
                fromOwnerId = staffDTO.getStaffId();
                fromOwnerType = Const.OWNER_TYPE.STAFF_LONG;
                VShopStaffDTO vShopStaffDTO = staffService.findStaffById(DataUtil.safeToString(fromOwnerId));
                staffInfoTagExport.loadStaff(vShopStaffDTO);
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception e) {
            logger.error(e);
            reportError("", "common.error.happened", e);
            topPage();
        }
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getTypeOrderNote() {
        return typeOrderNote;
    }

    public void setTypeOrderNote(String typeOrderNote) {
        this.typeOrderNote = typeOrderNote;
    }

    public Long getFromOwnerId() {
        return fromOwnerId;
    }

    public void setFromOwnerId(Long fromOwnerId) {
        this.fromOwnerId = fromOwnerId;
    }

    public Long getFromOwnerType() {
        return fromOwnerType;
    }

    public void setFromOwnerType(Long fromOwnerType) {
        this.fromOwnerType = fromOwnerType;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getLimitAutoComplete() {
        return limitAutoComplete;
    }

    public void setLimitAutoComplete(int limitAutoComplete) {
        this.limitAutoComplete = limitAutoComplete;
    }

    @Override
    public StaffDTO getStaffDTO() {
        return staffDTO;
    }

    public void setStaffDTO(StaffDTO staffDTO) {
        this.staffDTO = staffDTO;
    }

    public List<VActionGoodsStatisticDTO> getLsGoodsStatisticDTOs() {
        return lsGoodsStatisticDTOs;
    }

    public void setLsGoodsStatisticDTOs(List<VActionGoodsStatisticDTO> lsGoodsStatisticDTOs) {
        this.lsGoodsStatisticDTOs = lsGoodsStatisticDTOs;
    }

    public ShopInfoNameable getShopInfoTagExport() {
        return shopInfoTagExport;
    }

    public void setShopInfoTagExport(ShopInfoNameable shopInfoTagExport) {
        this.shopInfoTagExport = shopInfoTagExport;
    }


    public String getOwnerTypeExport() {
        return ownerTypeExport;
    }

    public void setOwnerTypeExport(String ownerTypeExport) {
        this.ownerTypeExport = ownerTypeExport;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OptionSetValueDTO> getStockTransStatus() {
        return stockTransStatus;
    }

    public void setStockTransStatus(List<OptionSetValueDTO> stockTransStatus) {
        this.stockTransStatus = stockTransStatus;
    }

    public StaffInfoNameable getStaffInfoTagExport() {
        return staffInfoTagExport;
    }

    public void setStaffInfoTagExport(StaffInfoNameable staffInfoTagExport) {
        this.staffInfoTagExport = staffInfoTagExport;
    }
}

