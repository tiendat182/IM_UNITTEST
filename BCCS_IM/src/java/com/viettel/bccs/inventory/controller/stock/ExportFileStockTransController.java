package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.model.FileExportBean;
import com.viettel.bccs.inventory.common.util.FileUtil;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.PartnerContractService;
import com.viettel.bccs.inventory.service.StockTransActionService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author luannt23.
 * @comment
 * @date 2/25/2016.
 */
public class ExportFileStockTransController extends TransCommonController {

    @Autowired
    private PartnerContractService partnerContractService;

    @Autowired
    private StockTransActionService stockTransActionService;

    /**
     * @param stockTransDTO
     * @return
     * @throws LogicException
     * @throws Exception
     * @desc chi dung cho quan ly giao dich kho
     * @required stock_trans_id required, stock_trans_action_id
     */
    public StreamedContent printStockTransDetail(StockTransDTO stockTransDTO) throws LogicException, Exception {

        if (DataUtil.isNullObject(stockTransDTO)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "stock.trans.search.validate.selecttofinddetail");
        }
        if (stockTransDTO.getStockTransId() == null) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "stock.trans.search.validate.validStockTrans");
        }
        if (stockTransDTO.getStockTransActionId() == null) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "stock.trans.search.validate.validStockTrans");
        }
        FileExportBean fileExportBean = new FileExportBean();
        //
        prepare(fileExportBean, stockTransDTO);
        //
        return FileUtil.exportToStreamed(fileExportBean);
//        FileUtil.exportFile(fileExportBean);
//        return fileExportBean.getExportFileContent();
    }

    private void prepare(FileExportBean fileExportBean, StockTransDTO stockTransDTO) throws LogicException, Exception {
        initOwner(stockTransDTO);
        //
        initStockTransDetail(fileExportBean, stockTransDTO);
        //
        fileExportBean.setTempalatePath(FileUtil.getTemplatePath());
        fileExportBean.setOutPath(FileUtil.getOutputPath());
        fileExportBean.setOutName(getReportDetailOutName(stockTransDTO));
        fileExportBean.setTemplateName(getExportDetailTemplateFileName(stockTransDTO));
        logger.error("Template: " + fileExportBean.getTemplateName());
        //
        if (stockTransDTO.getFromOwnerType() != null
                && Const.OWNER_TYPE.PARTNER_LONG.equals(stockTransDTO.getFromOwnerType())) {
            initContractProperties(fileExportBean, stockTransDTO);
        }
    }

    private void initOwner(StockTransDTO stockTransDTO) throws LogicException, Exception {
        if (!DataUtil.isNullOrZero(stockTransDTO.getFromOwnerId())
                && DataUtil.isNullOrEmpty(stockTransDTO.getFromOwnerAddress())
                && !DataUtil.isNullOrZero(stockTransDTO.getFromOwnerType())) {
            if (Const.OWNER_TYPE.SHOP_LONG.equals(stockTransDTO.getFromOwnerType()) || Const.OWNER_TYPE.STAFF_LONG.equals(stockTransDTO.getFromOwnerType())) {
                List<VShopStaffDTO> listShopStaff = shopService.getListShopStaff(stockTransDTO.getFromOwnerId(), DataUtil.safeToString(stockTransDTO.getFromOwnerType()), null);
                if (!DataUtil.isNullOrEmpty(listShopStaff)) {
                    stockTransDTO.setFromOwnerAddress(listShopStaff.get(0).getOwnerAddress());
                    stockTransDTO.setFromOwnerName(listShopStaff.get(0).getOwnerName());
                }
            } else {
                PartnerDTO partnerDTO = partnerService.findOne(stockTransDTO.getFromOwnerId());
                if (partnerDTO != null) {
                    stockTransDTO.setFromOwnerAddress(partnerDTO.getAddress());
                    stockTransDTO.setFromOwnerName(partnerDTO.getPartnerName());
                }
            }
        }
        if (!DataUtil.isNullOrZero(stockTransDTO.getToOwnerId())
                && DataUtil.isNullOrEmpty(stockTransDTO.getToOwnerAddress())
                && !DataUtil.isNullOrZero(stockTransDTO.getToOwnerType())) {
            if (Const.OWNER_TYPE.SHOP_LONG.equals(stockTransDTO.getToOwnerType())
                    || Const.OWNER_TYPE.STAFF_LONG.equals(stockTransDTO.getToOwnerType())) {
                List<VShopStaffDTO> listShopStaff = shopService.getListShopStaff(stockTransDTO.getToOwnerId(), DataUtil.safeToString(stockTransDTO.getToOwnerType()), null);
                if (!DataUtil.isNullOrEmpty(listShopStaff)) {
                    stockTransDTO.setToOwnerAddress(listShopStaff.get(0).getOwnerAddress());
                    stockTransDTO.setToOwnerName(listShopStaff.get(0).getOwnerName());
                }
            } else {
                PartnerDTO partnerDTO = partnerService.findOne(stockTransDTO.getFromOwnerId());
                if (partnerDTO != null) {
                    stockTransDTO.setToOwnerAddress(partnerDTO.getAddress());
                    stockTransDTO.setToOwnerName(partnerDTO.getPartnerName());
                }
            }
        }
    }

    private void initStockTransDetail(FileExportBean bean, StockTransDTO stockTransDTO) throws LogicException, Exception {
        List<Long> stockTransActionIds = Lists.newArrayList();
        stockTransActionIds.add(stockTransDTO.getStockTransActionId());
        List<StockTransFullDTO> stockTransDetailDTOs = DataUtil.defaultIfNull(transService.searchStockTransDetail(stockTransActionIds), new ArrayList<StockTransFullDTO>());
        int index = 0;
        for (StockTransFullDTO stockTransFullDTO : stockTransDetailDTOs) {
            stockTransFullDTO.setIndex(++index);
            StockTransSerialDTO search = new StockTransSerialDTO();
            search.setStockTransDetailId(stockTransFullDTO.getStockTransDetailId());
            List<StockTransSerialDTO> stockTransSerials = DataUtil.defaultIfNull(stockTransSerialService.findStockTransSerialByDTO(search), new ArrayList<>());
            stockTransFullDTO.setLstSerial(stockTransSerials);
        }
        bean.putValue("lstStockTransFull", stockTransDetailDTOs);

        StockTransActionDTO stockTransActionDTO = stockTransActionService.findOne(stockTransDTO.getStockTransActionId());

        String actionCode = stockTransActionDTO.getActionCode();
        bean.putValue("actionCodeFull", actionCode == null ? "" : actionCode.toUpperCase());
        //
        if (DataUtil.isNullOrEmpty(stockTransActionDTO.getActionCodeShop())) {
            if (!Const.STOCK_TRANS_STATUS.IMPORT_ORDER.equals(stockTransActionDTO.getStatus())
                    && !Const.STOCK_TRANS_STATUS.EXPORT_ORDER.equals(stockTransActionDTO.getStatus())) {
                String[] subAction = stockTransDTO.getActionCode().split("_");
                actionCode = subAction[subAction.length - 1];
            }
        } else {
            actionCode = stockTransActionDTO.getActionCodeShop();
        }
        bean.putValue("receiptNo", actionCode == null ? "" : actionCode.toUpperCase());
        bean.putValue("actionCode", actionCode == null ? "" : actionCode.toUpperCase());
        //
        bean.putValue("userCreate", BccsLoginSuccessHandler.getStaffDTO().getStaffCode());
        bean.putValue("dateCreate", optionSetValueService.getSysdateFromDB(true));
        bean.putValue("toOwnerName", stockTransDTO.getToOwnerName());
        bean.putValue("reasonName", buildReasonName(stockTransDTO));
        bean.putValue("fromOwnerName", stockTransDTO.getFromOwnerName());
        bean.putValue("fromOwnerAddress", stockTransDTO.getFromOwnerAddress());
        bean.putValue("toOwnerAddress", stockTransDTO.getToOwnerAddress());
        bean.putValue("note", stockTransActionDTO.getNote());
    }

    private void initContractProperties(FileExportBean bean, StockTransDTO stockTransDTO) throws LogicException, Exception {
        if (DataUtil.safeEqual(Const.OWNER_TYPE.PARTNER_LONG, stockTransDTO.getFromOwnerType())) {
            PartnerContractDTO partnerContractDTO = partnerContractService.findByStockTransID(stockTransDTO.getStockTransId());
            bean.putValue("contractCode", partnerContractDTO.getContractCode());
            bean.putValue("contractDate", partnerContractDTO.getContractDate());
            bean.putValue("poCode", partnerContractDTO.getPoCode());
            bean.putValue("poDate", partnerContractDTO.getPoDate());
            bean.putValue("requestImportDate", partnerContractDTO.getRequestDate());
            bean.putValue("importStockDate", partnerContractDTO.getImportDate());
            bean.putValue("deliveryLocation", partnerContractDTO.getDeliveryLocation());
        }
    }
}
