package com.viettel.bccs.inventory.service.BaseStock;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.ReportUtil;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockTransAction;
import com.viettel.bccs.inventory.service.*;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.GetTextFromBundleHelper;
import com.viettel.webservice.voffice.FileAttachTranfer;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hoangnt14 on 12/5/2016.
 */
@Service
public class BaseVofficeStockService extends BaseVofficeService {

    public static final Logger logger = Logger.getLogger(BaseVofficeStockService.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Autowired
    private StockTransService stockTransService;

    @Autowired
    private StockTransActionService stockTransActionService;

    @Autowired
    private BaseReportService baseReportService;

    @Autowired
    private CommonService commonService;

    @Autowired
    private ReportUtil fileUtil;

    @Autowired
    private PartnerContractService partnerContractService;

    @Override
    public void doValidate(StockTransVofficeDTO stockTransVofficeDTO) throws LogicException, Exception {
        //set tittle
        stockTransVofficeDTO.setTittle(getText("voffice.doctitle.trans"));
        //Lay thong tin giao dich
        StockTransActionDTO stockTransActionDTO = stockTransActionService.findOne(stockTransVofficeDTO.getStockTransActionId());
        if (DataUtil.isNullObject(stockTransActionDTO)) {
            throw new LogicException("", "stock.trans.voffice.valid.trans");
        }
        if (!DataUtil.safeEqual(stockTransActionDTO.getSignCaStatus(), Const.SIGN_VOFFICE)) {
            throw new LogicException("", "stock.trans.voffice.status.not.valid");
        }
    }

    @Override
    public List<FileAttachTranfer> doCreateFileAttach(StockTransVofficeDTO stockTransVofficeDTO, VStockTransDTO vStockTransDTO, List<String> lstEmail) throws LogicException, Exception {
        stockTransVofficeDTO.setTittle(getText("voffice.doctitle.trans") + "_" + getFileName(vStockTransDTO));
        ReportDTO reportDTO = createReportConfig(vStockTransDTO, stockTransVofficeDTO);
        //
        List<StockTransFullDTO> data = stockTransService.searchStockTransDetail(Lists.newArrayList(vStockTransDTO.getActionID()));
        List<StockTransDetailDTO> lstStockTransDetailDTOs = getData(data, vStockTransDTO);
        List<FileAttachTranfer> lstFile = Lists.newArrayList();
        byte[] fileContent = baseReportService.exportWithDataSource(reportDTO, new JRBeanCollectionDataSource(lstStockTransDetailDTOs));
        //insert comment
        if (fileContent == null || fileContent.length == 0) {
            return lstFile;
        }
        fileContent = fileUtil.insertComment(fileContent, lstEmail);

        FileAttachTranfer fileAttachTranfer = new FileAttachTranfer();
        fileAttachTranfer.setAttachBytes(fileContent);
        fileAttachTranfer.setFileSign(1L);
        fileAttachTranfer.setFileName(getFileAttachName(vStockTransDTO));
        lstFile.add(fileAttachTranfer);
        return lstFile;
    }

    private ReportDTO createReportConfig(VStockTransDTO vStockTransDTO, StockTransVofficeDTO stockTransVofficeDTO) throws Exception {
        ReportDTO reportDTO = new ReportDTO();

        HashMap<String, Object> params = new HashMap<>();
        String path = fileUtil.getTemplatePath();
        String name = getTemplateName(vStockTransDTO, stockTransVofficeDTO.getPrefixTemplate());

        StockTransActionDTO stockTransActionDTO = stockTransActionService.findOne(vStockTransDTO.getActionID());

        if (stockTransActionDTO != null) {
            String actionCode = stockTransActionDTO.getActionCode();
            params.put(Const.REPORT_PARAMS.ACTIONCODE.toString(), actionCode);
        }
        params.put(Const.REPORT_PARAMS.RECEIPTNO.toString(), stockTransVofficeDTO.getReceiptNo());

        params.put(Const.REPORT_PARAMS.DATEREQUEST.toString(), getCurrentDateString());

        params.put(Const.REPORT_PARAMS.FROMADDRESS.toString(), getAddress(vStockTransDTO.getFromOwnerID(), vStockTransDTO.getFromOwnerType()));
        params.put(Const.REPORT_PARAMS.FROMOWNER.toString(), getOwnerName(vStockTransDTO.getFromOwnerID(), vStockTransDTO.getFromOwnerType()));

        params.put(Const.REPORT_PARAMS.TOADDRESS.toString(), getAddress(vStockTransDTO.getToOwnerID(), vStockTransDTO.getToOwnerType()));
        params.put(Const.REPORT_PARAMS.TOOWNER.toString(), getOwnerName(vStockTransDTO.getToOwnerID(), vStockTransDTO.getToOwnerType()));
        //
        String reasonName = vStockTransDTO.getReasonName();
        String note = "";
        StockTransActionDTO stockTransActionOrder = null;
        StockTransActionDTO stockTransActionVoffice = stockTransActionService.findOne(stockTransVofficeDTO.getStockTransActionId());
        if (DataUtil.safeEqual(stockTransActionVoffice.getStatus(), Const.STOCK_TRANS_STATUS.EXPORT_ORDER)
                || DataUtil.safeEqual(stockTransActionVoffice.getStatus(), Const.STOCK_TRANS_STATUS.EXPORT_NOTE)) {
            stockTransActionOrder = stockTransActionService.getStockTransActionByIdAndStatus(vStockTransDTO.getStockTransID(), Lists.newArrayList(Const.STOCK_TRANS_STATUS.EXPORT_ORDER));
        } else {
            stockTransActionOrder = stockTransActionService.getStockTransActionByIdAndStatus(vStockTransDTO.getStockTransID(), Lists.newArrayList(Const.STOCK_TRANS_STATUS.IMPORT_ORDER));
        }
        if (!DataUtil.isNullObject(stockTransActionOrder) && !DataUtil.isNullObject(stockTransActionOrder.getStockTransActionId())) {
            reasonName = getTextParam("voffice.stock.reasonName", stockTransActionOrder.getActionCode(), DateUtil.date2ddMMyyyyString(vStockTransDTO.getCreateDateTime()));
            note = stockTransActionOrder.getNote() == null ? "" : stockTransActionOrder.getNote();
        }
        params.put(Const.REPORT_PARAMS.REASONNAME.toString(), reasonName);
        //
        params.put(Const.REPORT_PARAMS.NOTE.toString(), note);
        //
        if (Const.ConfigListIDCheck.EXPORT.sValidate(vStockTransDTO.getActionType())) {
            params.put(Const.REPORT_PARAMS.STOCKTRANSTYPE.toString(), Const.ConfigListIDCheck.EXPORT.getValue().toString());
        } else {
            params.put(Const.REPORT_PARAMS.STOCKTRANSTYPE.toString(), Const.ConfigListIDCheck.IMPORT.getValue().toString());
        }
        //
        if (DataUtil.safeEqual(stockTransActionVoffice.getStatus(), Const.STOCK_TRANS_STATUS.EXPORT_ORDER)
                || DataUtil.safeEqual(stockTransActionVoffice.getStatus(), Const.STOCK_TRANS_STATUS.IMPORT_ORDER)) {
            params.put(Const.REPORT_PARAMS.ACTIONTYPE.toString(), Const.STOCK_TRANS_ACTION_TYPE.COMMAND);
        } else {
            params.put(Const.REPORT_PARAMS.ACTIONTYPE.toString(), Const.STOCK_TRANS_ACTION_TYPE.NOTE);
        }
        //init partnercontract info
        initContractProperties(params, vStockTransDTO);

        reportDTO.setParams(params);
        reportDTO.setJasperFilePath(path + name);
        return reportDTO;
    }

    private String getCurrentDateString() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String key = "good.revoke.msg.signDate";
        String vOfficeDate = GetTextFromBundleHelper.getTextParam(key, DataUtil.safeToString(day), "" + month, "" + year);
        return vOfficeDate;
    }

    private String getAddress(Long ownerID, Long ownerTyp) {
        try {
            return commonService.getOwnerAddress(ownerID.toString(), ownerTyp.toString());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return "";
    }

    private String getOwnerName(Long ownerID, Long ownerTyp) {
        try {
            return commonService.getOwnerName(ownerID.toString(), ownerTyp.toString());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return "";
    }

    private String getTemplateName(VStockTransDTO vStockTransDTO, String prefixTemplate) throws Exception {
        String preFix = prefixTemplate;
        String stockReportTemplate = "";
        if (DataUtil.safeEqual(vStockTransDTO.getFromOwnerType(), Const.OWNER_TYPE.SHOP_LONG)) {
            if (!DataUtil.isNullOrZero(vStockTransDTO.getFromOwnerID()) && DataUtil.safeEqual(vStockTransDTO.getFromOwnerID(), Const.SHOP.SHOP_VTT_ID)) {
                preFix += "_VT";
            }
        }
        if (DataUtil.safeEqual(vStockTransDTO.getStockTransStatus(), Const.STOCK_TRANS_STATUS.IMPORT_ORDER) || DataUtil.safeEqual(vStockTransDTO.getStockTransStatus(), Const.STOCK_TRANS_STATUS.IMPORT_NOTE)
                || DataUtil.safeEqual(vStockTransDTO.getStockTransStatus(), Const.STOCK_TRANS_STATUS.IMPORTED)) {
            if (!DataUtil.isNullOrZero(vStockTransDTO.getToOwnerID()) && DataUtil.safeEqual(vStockTransDTO.getToOwnerID(), Const.SHOP.SHOP_VTT_ID)) {
                preFix += "_VT";
            }
        }
        if (DataUtil.safeEqual(vStockTransDTO.getFromOwnerType(), Const.OWNER_TYPE.SHOP_LONG)
                || DataUtil.safeEqual(vStockTransDTO.getFromOwnerType(), Const.OWNER_TYPE.STAFF_LONG)) {
            stockReportTemplate = commonService.getStockReportTemplate(vStockTransDTO.getFromOwnerID(), DataUtil.safeToString(vStockTransDTO.getFromOwnerType()));
        }
        //neu la doi tac
        if (DataUtil.safeEqual(vStockTransDTO.getFromOwnerType(), Const.OWNER_TYPE.PARTNER_LONG)) {
            stockReportTemplate = commonService.getStockReportTemplate(Const.SHOP.SHOP_VTT_ID, DataUtil.safeToString(Const.OWNER_TYPE.SHOP));
        }

        String templateName = "";
        //hoangnt
        if (DataUtil.safeEqual(vStockTransDTO.getStockTransStatus(), Const.STOCK_TRANS_STATUS.EXPORT_ORDER) || DataUtil.safeEqual(vStockTransDTO.getStockTransStatus(), Const.STOCK_TRANS_STATUS.EXPORT_NOTE)
                || DataUtil.safeEqual(vStockTransDTO.getStockTransStatus(), Const.STOCK_TRANS_STATUS.EXPORTED)) {
            if (preFix != null && preFix.endsWith("_TTH_CN")) {
                templateName = preFix + "_2007.jasper";
            } else {
                templateName = preFix + "_" + stockReportTemplate + "_2007.jasper";
            }
        }
        if (DataUtil.safeEqual(vStockTransDTO.getStockTransStatus(), Const.STOCK_TRANS_STATUS.IMPORT_ORDER) || DataUtil.safeEqual(vStockTransDTO.getStockTransStatus(), Const.STOCK_TRANS_STATUS.IMPORT_NOTE)
                || DataUtil.safeEqual(vStockTransDTO.getStockTransStatus(), Const.STOCK_TRANS_STATUS.IMPORTED)) {
            templateName = preFix + "_" + stockReportTemplate + "_2007.jasper";
        }
        if (DataUtil.safeEqual(vStockTransDTO.getStockTransType(), DataUtil.safeToLong(Const.STOCK_TRANS_TYPE.AGENT))) {
            templateName = preFix + "_" + stockReportTemplate + ".jasper";
        }
        if (DataUtil.safeEqual(vStockTransDTO.getStockTransType(), Const.STOCK_TRANS_TYPE.REVOKE_KIT) && DataUtil.safeEqual(vStockTransDTO.getStockTransType(), Const.STOCK_TRANS_TYPE.EXPORT_KIT)) {
            templateName = preFix + "_RC" + ".jasper";
        }

        return templateName;
    }

    private String getReceiptNO(StockTransActionDTO stockTransActionDTO) {
        String actionCode = stockTransActionDTO.getActionCodeShop();
        if (DataUtil.isNullOrEmpty(actionCode)) {
            actionCode = stockTransActionDTO.getActionCode();
            if (!DataUtil.isNullOrEmpty(actionCode)) {
                if (Const.ConfigStockTrans.PN.validate(stockTransActionDTO.getStatus())
                        || Const.ConfigStockTrans.PX.validate(stockTransActionDTO.getStatus())) {
                    String[] split = actionCode.split("_");
                    actionCode = split[split.length - 1];
                }
            }
        }
        return actionCode;
    }

    public void initContractProperties(HashMap bean, VStockTransDTO stockTransDTO) throws Exception {
        if (stockTransDTO != null
                && DataUtil.safeEqual(Const.STOCK_TRANS_VOFFICE.OWNERTYPE_PARTNER, stockTransDTO.getFromOwnerType())) {
            PartnerContractDTO partnerContractDTO = partnerContractService.findByStockTransID(stockTransDTO.getStockTransID());
            bean.put("contractCode", partnerContractDTO.getContractCode());
            bean.put("contractDate", partnerContractDTO.getContractDate());
            bean.put("poCode", partnerContractDTO.getPoCode());
            bean.put("poDate", partnerContractDTO.getPoDate());
            bean.put("requestImportDate", partnerContractDTO.getRequestDate());
            bean.put("importStockDate", partnerContractDTO.getImportDate());
            bean.put("deliveryLocation", partnerContractDTO.getDeliveryLocation());
        }
    }

    public List<StockTransDetailDTO> getData(List<StockTransFullDTO> stockTransFullDTOs, VStockTransDTO vStockTransDTO) {
        List<StockTransDetailDTO> lstData = Lists.newArrayList();
        if (!DataUtil.isNullOrEmpty(stockTransFullDTOs)) {
            for (StockTransFullDTO stockTransFullDTO : stockTransFullDTOs) {
                StockTransDetailDTO detailDTO = new StockTransDetailDTO(stockTransFullDTO.getProdOfferName(), stockTransFullDTO.getUnit());
                detailDTO.setTransDetailID(stockTransFullDTO.getStockTransDetailId());
                detailDTO.setQuantity(stockTransFullDTO.getQuantity());
                detailDTO.setPrice(stockTransFullDTO.getPrice());
                detailDTO.setTotalPrice(stockTransFullDTO.getAmount());
                detailDTO.setFromSerial(stockTransFullDTO.getFromSerial());
                detailDTO.setToSerial(stockTransFullDTO.getToSerial());
                detailDTO.setReceivingUnit(vStockTransDTO.getToOwnerName());
                detailDTO.setNote(vStockTransDTO.getNote());
                lstData.add(detailDTO);
            }
        }
        return lstData;
    }

    private String getFileAttachName(VStockTransDTO vStockTransDTO) throws Exception {
        String actionCode = vStockTransDTO.getActionCode();
        if (DataUtil.isNullOrEmpty(actionCode)) {
            vStockTransDTO.setActionCode(Const.TEMPLATE.DEFAULT.toString());
        }
        return getFileName(vStockTransDTO) + Const.TEMPLATE.MEXTENSION;
    }

    private String getFileName(VStockTransDTO vStockTransDTO) throws Exception {
        String prefix = getPrefix(vStockTransDTO);
        String subPrefix = getSubPrefix(vStockTransDTO);
        return prefix + Const.TEMPLATE.SEPARATOR.toString()
                + vStockTransDTO.getActionCode().replaceAll("\\W", "")
                + Const.TEMPLATE.SEPARATOR.toString()
                + subPrefix;
    }

    private String getSubPrefix(VStockTransDTO vStockTransDTO) throws Exception {
        String subPrefix = Const.ConfigStockTrans.FR_PARTNER.toString();
        if (DataUtil.safeEqual(vStockTransDTO.getFromOwnerType(), Const.OWNER_TYPE.SHOP_LONG)
                || DataUtil.safeEqual(vStockTransDTO.getFromOwnerType(), Const.OWNER_TYPE.STAFF_LONG)) {
            Long ownerId = vStockTransDTO.getFromOwnerID();
            Long ownerType = vStockTransDTO.getFromOwnerType();
            if (Const.ConfigStockTrans.PN.validate(vStockTransDTO.getActionType())
                    || Const.ConfigStockTrans.NK.validate(vStockTransDTO.getActionType())
                    || Const.ConfigStockTrans.LN.validate(vStockTransDTO.getActionType())) {
                ownerId = vStockTransDTO.getToOwnerID();
                ownerType = vStockTransDTO.getToOwnerType();
            }
            subPrefix = commonService.getStockReportTemplate(ownerId, DataUtil.safeToString(ownerType));
        }
        return subPrefix;
    }

    private String getPrefix(VStockTransDTO vStockTransDTO) {
        String prefix = Const.ConfigStockTrans.LX.toString();
        if (Const.ConfigStockTrans.PX.validate(vStockTransDTO.getActionType()) &&
                Const.ConfigStockTrans.PXS.lValidate(vStockTransDTO.getStockTransType())) {
            prefix = Const.ConfigStockTrans.PXS.toString();
        } else {
            if (Const.ConfigStockTrans.PX.validate(vStockTransDTO.getActionType())
                    || Const.ConfigStockTrans.XK.validate(vStockTransDTO.getActionType())) {
                prefix = Const.ConfigStockTrans.PX.toString();
            } else if (Const.ConfigStockTrans.PN.validate(vStockTransDTO.getActionType())
                    || Const.ConfigStockTrans.NK.validate(vStockTransDTO.getActionType())) {
                prefix = Const.ConfigStockTrans.PN.toString();
            } else if (Const.ConfigStockTrans.LN.validate(vStockTransDTO.getActionType())) {
                prefix = Const.ConfigStockTrans.LN.toString();
            }
        }
        return prefix;
    }
}
