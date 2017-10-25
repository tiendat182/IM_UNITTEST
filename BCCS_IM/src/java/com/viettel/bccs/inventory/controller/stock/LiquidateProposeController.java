package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.PartnerService;
import com.viettel.bccs.inventory.service.RequestLiquidateService;
import com.viettel.bccs.inventory.tag.ListProductNameable;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.dto.BaseMessage;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.util.Date;
import java.util.List;

/**
 * Created by tuannm33 on 2/16/2016.
 */
@Component
@Scope("view")
@ManagedBean(name = "liquidateProposeController")
public class LiquidateProposeController extends TransCommonController {

    private byte[] byteContent;
    private String[] fileType = {".doc", ".docx", ".pdf", ".xls", ".xlsx", ".png", ".jpg", ".jpeg", ".bmp"};
    private String attachFileName = "";
    private UploadedFile fileUpload;
    private StaffDTO staffDTO;
    private PartnerDTO partnerDTO;
    private RequestLiquidateDTO requestLiquidateDTO;

    @Autowired
    private ShopInfoNameable shopInfoExportTag;
    @Autowired
    private ListProductNameable listProductTag;
    @Autowired
    private PartnerService partnerService;
    @Autowired
    private RequestLiquidateService requestLiquidateService;

    @Secured("@")
    @PostConstruct
    public void init() {
        try {
            doSetDefault();
            executeCommand("updateControls()");
        } catch (Exception e) {
            topReportError("", "common.error.happened", e);
        }
    }

    @Secured("@")
    public void doSetDefault() {
        clearCache();
        try {
            staffDTO = BccsLoginSuccessHandler.getStaffDTO();
            String shopId = DataUtil.safeToString(staffDTO.getShopId());
            //shopInfoExportTag.initShop(this, shopId, false);
            shopInfoExportTag.loadShop(shopId, true);
            partnerDTO = partnerService.findOne(481L);
            //init cho tag list danh sach hang hoa
            listProductTag.init(this, new ConfigListProductTagDTO(Const.MODE_SERIAL.MODE_ADD_ALL, staffDTO.getShopId(), Const.OWNER_TYPE.SHOP));
            listProductTag.setTotalPriceAmount(0L);
            //
            requestLiquidateDTO.setRequestCode(requestLiquidateService.getRequestCode());
        } catch (LogicException e) {
            logger.error(e.getMessage(), e);
            topReportError("", e.getErrorCode(), e.getKeyMsg());
        } catch (Exception e) {
            topReportError("", "common.error.happened", e);
        }
    }

    private void clearCache() {
        requestLiquidateDTO = new RequestLiquidateDTO();
        Date currentDate = getSysdateFromDB();
        requestLiquidateDTO.setCreateDatetime(currentDate);
        partnerDTO = new PartnerDTO();
        attachFileName = "";
        byteContent = null;
    }

    @Secured("@")
    public void resetProposeCode() {
        try {
            requestLiquidateDTO.setRequestCode(requestLiquidateService.getRequestCode());
        } catch (LogicException e) {
            logger.error(e.getMessage(), e);
            topReportError("", e.getErrorCode(), e.getKeyMsg());
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    @Secured("@")
    public void handleFileUpload(FileUploadEvent event) {
        try {
            if (DataUtil.isNullObject(event)) {
                reportError("", ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "common.error.upload.file");
                topPage();
                return;
            } else {
                fileUpload = event.getFile();
                BaseMessage message = validateFileUploadWhiteList(fileUpload, ALOW_EXTENSION_EXCEL_TYPE_LIST, MAX_SIZE_5M);
                if (!message.isSuccess()) {
                    LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                    ex.setDescription(message.getDescription());
                    throw ex;
                }
                byteContent = fileUpload.getContents();
                attachFileName = new String(event.getFile().getFileName().getBytes(), "UTF-8");
            }
        } catch (LogicException ex) {
            reportError("", ex);
            topPage();
        } catch (Exception e) {
            topReportError("", "common.error.happened", e);
        }
    }

    /**
     * kiem tra dinh dang file
     *
     * @param extension
     * @author
     */
    private boolean isCorrectExtension(String extension) {
        boolean test = false;
        for (int i = 0; i < fileType.length; i++) {
            if (extension.contains(fileType[i])) {
                test = true;
                break;
            }
        }
        return test;
    }

    @Secured("@")
    public void doValidate() {
        try {
            if (DataUtil.isNullObject(fileUpload) || DataUtil.isNullOrEmpty(attachFileName)) {
                reportError("", ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.liquidate.file.msg.empty");
                topPage();
                return;
            }
            doValidateListDetail(listProductTag.getListTransDetailDTOs());
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    /**
     * Lap yeu cau
     */
    @Secured("@")
    public void onCreateLiquidatePropose() {
        try {
            /*if (!isCorrectExtension(fileUpload.getFileName())) {
                reportError("", ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.liquidate.file.msg.invalid");
                topPage();
                return;
            }*/

            BaseMessage messageFile = validateFileUploadWhiteList(fileUpload, ALOW_EXTENSION_EXCEL_TYPE_LIST, MAX_SIZE_5M);
            if (!messageFile.isSuccess()) {
                LogicException ex = new LogicException(messageFile.getErrorCode(), messageFile.getKeyMsg());
                ex.setDescription(messageFile.getDescription());
                throw ex;
            }

            List<StockTransDetailDTO> listStockTransDetailDTOs = listProductTag.getListTransDetailDTOs();
            if (DataUtil.isNullOrEmpty(listStockTransDetailDTOs)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "stock.trans.staff.export.detailRequired");
            }
            //
            List<RequestLiquidateDetailDTO> lstRequestLiquidateDtlDTO = Lists.newArrayList();
            for (StockTransDetailDTO stockTransDtl : listStockTransDetailDTOs) {
                if (DataUtil.safeEqual(stockTransDtl.getCheckSerial(), Const.PRODUCT_OFFERING.CHECK_SERIAL)
                        && DataUtil.isNullOrEmpty(stockTransDtl.getLstSerial())) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, getTextParam(getText("staff.return.stock.valid.serial"), stockTransDtl.getProdOfferName()));
                }
                RequestLiquidateDetailDTO liquidateDetailDTO = new RequestLiquidateDetailDTO();
                List<RequestLiquidateSerialDTO> lstRequestLiquidateSerialDTO = Lists.newArrayList();
                List<StockTransSerialDTO> lstSerial = stockTransDtl.getLstStockTransSerial();
                // Check mat hang co serial khong, neu co thi add
                if (!DataUtil.isNullOrEmpty(lstSerial)) {
                    for (StockTransSerialDTO stockTransSerial : lstSerial) {
                        RequestLiquidateSerialDTO liquidateSerial = new RequestLiquidateSerialDTO();
                        liquidateSerial.setFromSerial(stockTransSerial.getFromSerial());
                        liquidateSerial.setToSerial(stockTransSerial.getToSerial());
                        liquidateSerial.setQuantity(stockTransSerial.getQuantity());
                        // Danh sach serial tung mat hang
                        lstRequestLiquidateSerialDTO.add(liquidateSerial);
                    }
                    liquidateDetailDTO.setLstRequestLiquidateSerialDTO(lstRequestLiquidateSerialDTO);
                }
                liquidateDetailDTO.setProdOfferId(stockTransDtl.getProdOfferId());
                liquidateDetailDTO.setQuantity(stockTransDtl.getQuantity());
                liquidateDetailDTO.setStateId(stockTransDtl.getStateId());
                // Danh sach chi tiet mat hang thanh ly
                lstRequestLiquidateDtlDTO.add(liquidateDetailDTO);
            }
            requestLiquidateDTO.setStatus(Const.LIQUIDATE_STATUS.NEW);
            requestLiquidateDTO.setCreateShopId(staffDTO.getShopId());
            requestLiquidateDTO.setCreateStaffId(staffDTO.getStaffId());
            requestLiquidateDTO.setCreateUser(staffDTO.getStaffCode());
            requestLiquidateDTO.setDocumentName(attachFileName);
            requestLiquidateDTO.setDocumentContent(byteContent);
            requestLiquidateDTO.setLstRequestLiquidateDetailDTO(lstRequestLiquidateDtlDTO);
            BaseMessage message = requestLiquidateService.doSaveLiquidate(requestLiquidateDTO);
            if (!DataUtil.isNullOrEmpty(message.getErrorCode())) {
                throw new LogicException(message.getErrorCode(), message.getKeyMsg());
            }
            reportSuccess("frmLiquidatePropose:msgLiquidatePropose", getTextParam(getText("mn.stock.liquidate.create.success"), requestLiquidateDTO.getRequestCode()));
            topPage();
            doSetDefault();
        } catch (LogicException e) {
            topReportError("", e);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

//    @Secured("@")
//    public void doReset() {
//        try {
//            doSetDefault();
//            executeCommand("updateControls()");
//        } catch (Exception ex) {
//            topReportError("", "common.error.happened", ex);
//        }
//    }

    @Override
    public StaffDTO getStaffDTO() {
        return staffDTO;
    }

    public void setStaffDTO(StaffDTO staffDTO) {
        this.staffDTO = staffDTO;
    }

    public PartnerDTO getPartnerDTO() {
        return partnerDTO;
    }

    public void setPartnerDTO(PartnerDTO partnerDTO) {
        this.partnerDTO = partnerDTO;
    }

    public ShopInfoNameable getShopInfoExportTag() {
        return shopInfoExportTag;
    }

    public void setShopInfoExportTag(ShopInfoNameable shopInfoExportTag) {
        this.shopInfoExportTag = shopInfoExportTag;
    }

    public RequestLiquidateDTO getRequestLiquidateDTO() {
        return requestLiquidateDTO;
    }

    public void setRequestLiquidateDTO(RequestLiquidateDTO requestLiquidateDTO) {
        this.requestLiquidateDTO = requestLiquidateDTO;
    }

    public String getAttachFileName() {
        return attachFileName;
    }

    public void setAttachFileName(String attachFileName) {
        this.attachFileName = attachFileName;
    }

    public UploadedFile getFileUpload() {
        return fileUpload;
    }

    public void setFileUpload(UploadedFile fileUpload) {
        this.fileUpload = fileUpload;
    }

    public ListProductNameable getListProductTag() {
        return listProductTag;
    }

    public void setListProductTag(ListProductNameable listProductTag) {
        this.listProductTag = listProductTag;
    }

}
