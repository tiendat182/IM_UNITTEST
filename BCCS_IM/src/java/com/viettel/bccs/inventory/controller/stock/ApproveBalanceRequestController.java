package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.util.FileUtil;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.StockBalanceDetailService;
import com.viettel.bccs.inventory.service.StockBalanceRequestService;
import com.viettel.bccs.inventory.service.StockBalanceSerialService;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.JasperReportUtils;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author luannt23.
 * @comment
 * @date 3/14/2016.
 */
@Component
@Scope("view")
@ManagedBean
public class ApproveBalanceRequestController extends TransCommonController {

    private StockBalanceRequestDTO stockBalanceRequestDTO;
    private List<StockBalanceRequestDTO> listStockBalanceRequestDTO;
    private List<StockBalanceDetailDTO> listStockBalanceDetailDTO;
    private List<StockBalanceSerialDTO> listStockBalanceSerialDTO;
    private StockBalanceDetailDTO selectStockBalanceDetailDTO;

    @Autowired
    StockBalanceSerialService stockBalanceSerialService;
    @Autowired
    private ShopInfoNameable shopInfoTag;
    @Autowired
    StockBalanceRequestService stockBalanceRequestService;

    @Autowired
    StockBalanceDetailService stockBalanceDetailService;

    private StockBalanceRequestDTO selectedStockBalanceRequestDTO;

    private boolean showDetail;

    @PostConstruct
    public void init() {
        try {
            initControls();
            executeCommand("updateControls");
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    private void initControls() throws Exception {
        showDetail = false;
        stockBalanceRequestDTO = new StockBalanceRequestDTO();
        stockBalanceRequestDTO.setShopId(getStaffDTO().getShopId());
        stockBalanceRequestDTO.setFromDate(getSysdateFromDB());
        stockBalanceRequestDTO.setToDate(getSysdateFromDB());
        stockBalanceRequestDTO.setStatus(Const.BalanceRequestStatus.create.toLong());


        List<Long> listChannelType = commonService.getChannelTypeByVtUnit(Const.OWNER_TYPE.SHOP, Long.valueOf(Const.VT_UNIT.VT));
        shopInfoTag.initShopAndAllChildWithChanelType(this, getStaffDTO().getShopId().toString(), listChannelType);

        doSearchStockBalanceRequest();
    }

    @Secured("@")
    public void doSearchStockBalanceRequest() {
        try {
            showDetail = false;
            validateDate(stockBalanceRequestDTO.getFromDate(), stockBalanceRequestDTO.getToDate());
            listStockBalanceRequestDTO = DataUtil
                    .defaultIfNull(stockBalanceRequestService.searchStockBalanceRequest(stockBalanceRequestDTO), Lists.newArrayList());
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    @Secured("@")
    public void onDetail(StockBalanceRequestDTO stockBalanceRequestDTO) {
        try {
            if (stockBalanceRequestDTO == null) {
                showDetail = false;
                selectedStockBalanceRequestDTO = null;
                listStockBalanceDetailDTO = Lists.newArrayList();
            } else {
                showDetail = true;
                selectedStockBalanceRequestDTO = stockBalanceRequestDTO;
                listStockBalanceDetailDTO = DataUtil.defaultIfNull(stockBalanceDetailService.getListStockBalanceDetail(selectedStockBalanceRequestDTO.getStockRequestId()), Lists.newArrayList());
            }
        } catch (LogicException ex) {
            showDetail = false;
            selectedStockBalanceRequestDTO = null;
            topReportError("", ex);
        } catch (Exception ex) {
            showDetail = false;
            selectedStockBalanceRequestDTO = null;
            topReportError("", "common.error.happened", ex);
        }
    }

    @Secured("@")
    public void onDetailSerial(StockBalanceDetailDTO stockBalanceDetailDTO) {
        if (stockBalanceDetailDTO != null) {
            try {
                listStockBalanceSerialDTO = DataUtil.defaultIfNull(stockBalanceSerialService.getListStockBalanceSerialDTO(stockBalanceDetailDTO.getStockBalanceDetailId()), Lists.newArrayList());
                if (listStockBalanceSerialDTO.isEmpty()) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mm.stock.trans.search.validate.serial", stockBalanceDetailDTO.getProdOfferName());
                }
                selectStockBalanceDetailDTO = stockBalanceDetailDTO;
            } catch (LogicException ex) {
                topReportError("", ex);
            } catch (Exception ex) {
                topReportError("", "common.error.happened", ex);
            }
        }
    }

    @Secured("@")
    public void onApporve(int status) {
        try {
            selectedStockBalanceRequestDTO.setStatus(new Long(status));
            selectedStockBalanceRequestDTO.setShopId(getStaffDTO().getShopId());
            selectedStockBalanceRequestDTO.setStaffId(getStaffDTO().getStaffId());
            selectedStockBalanceRequestDTO.setUpdateUser(getStaffDTO().getStaffCode());
            stockBalanceRequestService.doApproveStockBalanceRequest(selectedStockBalanceRequestDTO);
            if (Const.BalanceRequestStatus.approved.toLong().equals(new Long(status))) {
                topReportSuccess("", "mn.stock.balance.approved");
            } else {
                topReportSuccess("", "mn.stock.balance.rejected");
            }
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    @Secured("@")
    public void onSelect(StockBalanceRequestDTO stockBalanceRequestDTO) {
        selectedStockBalanceRequestDTO = stockBalanceRequestDTO;
    }

    @Secured("@")
    public String getConfirmMessage(String key) {
        if (selectedStockBalanceRequestDTO == null) {
            return "";
        }
        String msg = "mn.stock.balance.cfg.reject";
        if ("APPROVE".equals(key)) {
            msg = "mn.stock.balance.cfg.approve";
        }
        return getMessage(msg, selectedStockBalanceRequestDTO.getRequestCode());
    }

    @Secured("@")
    public StreamedContent getAttachFile() {
        try {
            String ownerName = getOwnerCode(selectedStockBalanceRequestDTO.getOwnerId(), Const.OWNER_TYPE.SHOP);
            for (StockBalanceDetailDTO stockBalanceDetailDTO : listStockBalanceDetailDTO) {
                stockBalanceDetailDTO.setType(DataUtil.safeToString(selectedStockBalanceRequestDTO.getType()));
                stockBalanceDetailDTO.setOwnerName(ownerName);
                Long bccs = stockBalanceDetailDTO.getQuantityBccs();
                bccs = bccs == null ? 0L : bccs;
                Long real = stockBalanceDetailDTO.getQuantityReal();
                real = real == null ? 0L : real;
                Long erp = stockBalanceDetailDTO.getQuantityErp();
                erp = erp == null ? 0L : erp;
                DecimalFormat decimalFormat = new DecimalFormat("###,###");
                stockBalanceDetailDTO.setDivBCCSFN(decimalFormat.format(bccs - erp));
                stockBalanceDetailDTO.setDivInspectFN(decimalFormat.format(real - erp));
            }
            StaffDTO staffDTO = staffService.getStaffByStaffCode(selectedStockBalanceRequestDTO.getCreateUser());
            if (staffDTO == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "common.valid.staff", selectedStockBalanceRequestDTO.getCreateUser());
            }
            Map bean = new HashMap<>();
            bean.put("lstStockModel", listStockBalanceDetailDTO);
            bean.put("createUser", staffDTO.getStaffCode());
            bean.put("ownerName", ownerName);
            bean.put("createIsdn", staffDTO.getIsdn());
            String templateName = FileUtil.getTemplatePath() + Const.REPORT_TEMPLATE.STOCK_BALANCE_REPORT;
            byte[] bytes = JasperReportUtils.exportPdfByte(bean, templateName);
            return FileUtil.getStreamedContent(bytes, "file+trinh+ky+de+xuat.pdf");
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
        return null;
    }

    @Secured("@")
    public StreamedContent getSerialAttachFile() {
        try {
            DecimalFormat decimalFormat = new DecimalFormat("###,###");
            String ownerName = getOwnerCode(selectedStockBalanceRequestDTO.getOwnerId(), Const.OWNER_TYPE.SHOP);
            List<StockBalanceSerialDTO> lstBalanceSerialDTO = Lists.newArrayList();
            for (StockBalanceDetailDTO stockBalanceDetailDTO : listStockBalanceDetailDTO) {
                List<StockBalanceSerialDTO> subs = DataUtil.defaultIfNull(stockBalanceSerialService.getListStockBalanceSerialDTO(stockBalanceDetailDTO.getStockBalanceDetailId()), Lists.newArrayList());
                for (StockBalanceSerialDTO stockBalanceSerialDTO : subs) {
                    stockBalanceSerialDTO.setOwnerName(ownerName);
                    stockBalanceSerialDTO.setProdOfferName(stockBalanceDetailDTO.getProdOfferName());
                    Long quantity = stockBalanceDetailDTO.getQuantity();
                    quantity = quantity == null ? 0L : quantity;
                    stockBalanceSerialDTO.setQuantity(decimalFormat.format(quantity));
                }
                lstBalanceSerialDTO.addAll(subs);
            }
            if (DataUtil.isNullOrEmpty(lstBalanceSerialDTO)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.balance.valid.serial");
            }
            Map bean = new HashMap<>();
            bean.put("lstStockModel", lstBalanceSerialDTO);
            String templateName = FileUtil.getTemplatePath() + Const.REPORT_TEMPLATE.STOCK_BALANCE_ATTACH;
            byte[] bytes = JasperReportUtils.exportPdfByte(bean, templateName);
            return FileUtil.getStreamedContent(bytes, "phu+luc+serial.pdf");
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
        return null;
    }

    @Secured("@")
    public void doSelectShop(VShopStaffDTO vShopStaffDTO) {
        stockBalanceRequestDTO.setOwnerId(DataUtil.safeToLong(vShopStaffDTO.getOwnerId(), null));
    }


    @Secured("@")
    public void doClearShop() {
        stockBalanceRequestDTO.setOwnerId(null);
    }

    public StockBalanceRequestDTO getStockBalanceRequestDTO() {
        return stockBalanceRequestDTO;
    }

    public void setStockBalanceRequestDTO(StockBalanceRequestDTO stockBalanceRequestDTO) {
        this.stockBalanceRequestDTO = stockBalanceRequestDTO;
    }

    public ShopInfoNameable getShopInfoTag() {
        return shopInfoTag;
    }

    public void setShopInfoTag(ShopInfoNameable shopInfoTag) {
        this.shopInfoTag = shopInfoTag;
    }

    public List<StockBalanceRequestDTO> getListStockBalanceRequestDTO() {
        return listStockBalanceRequestDTO;
    }

    public void setListStockBalanceRequestDTO(List<StockBalanceRequestDTO> listStockBalanceRequestDTO) {
        this.listStockBalanceRequestDTO = listStockBalanceRequestDTO;
    }

    public StockBalanceRequestDTO getSelectedStockBalanceRequestDTO() {
        return selectedStockBalanceRequestDTO;
    }

    public void setSelectedStockBalanceRequestDTO(StockBalanceRequestDTO selectedStockBalanceRequestDTO) {
        this.selectedStockBalanceRequestDTO = selectedStockBalanceRequestDTO;
    }

    public boolean isShowDetail() {
        return showDetail;
    }

    public void setShowDetail(boolean showDetail) {
        this.showDetail = showDetail;
    }

    public List<StockBalanceDetailDTO> getListStockBalanceDetailDTO() {
        return listStockBalanceDetailDTO;
    }

    public void setListStockBalanceDetailDTO(List<StockBalanceDetailDTO> listStockBalanceDetailDTO) {
        this.listStockBalanceDetailDTO = listStockBalanceDetailDTO;
    }

    public List<StockBalanceSerialDTO> getListStockBalanceSerialDTO() {
        return listStockBalanceSerialDTO;
    }

    public void setListStockBalanceSerialDTO(List<StockBalanceSerialDTO> listStockBalanceSerialDTO) {
        this.listStockBalanceSerialDTO = listStockBalanceSerialDTO;
    }

    public StockBalanceDetailDTO getSelectStockBalanceDetailDTO() {
        return selectStockBalanceDetailDTO;
    }

    public void setSelectStockBalanceDetailDTO(StockBalanceDetailDTO selectStockBalanceDetailDTO) {
        this.selectStockBalanceDetailDTO = selectStockBalanceDetailDTO;
    }

    @Secured("@")
    public String getQuantity(String fromSerial, String toSerial) {
        try {
            BigDecimal from = new BigDecimal(fromSerial);
            BigDecimal to = new BigDecimal(toSerial);
            Long val = (to.add(from.negate())).longValue() + 1L;
            return formatNumber(val);
        } catch (Exception x) {
            logger.error(x.getMessage(), x);
        }
        return "1";
    }

    public static void main(String[] args) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        decimalFormat.format(100100000);
        decimalFormat.format(0l);
    }
}
