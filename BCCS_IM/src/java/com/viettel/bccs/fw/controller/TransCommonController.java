package com.viettel.bccs.fw.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.model.FileExportBean;
import com.viettel.bccs.inventory.common.util.FileUtil;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.message.BaseExtMessage;
import com.viettel.bccs.inventory.service.*;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.web.common.security.CustomAuthenticationProvider;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.omnifaces.util.Faces;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author luannt23 on 1/12/2016.
 */
public class TransCommonController extends InventoryController {
    @Autowired
    protected CommonService commonService;

    @Autowired
    protected StockTransService transService;

    @Autowired
    protected ShopService shopService;

    @Autowired
    protected StaffService staffService;

    @Autowired
    protected ExecuteStockTransService executeStockTrans;

    @Autowired
    protected StockTransSerialService stockTransSerialService;

    @Autowired
    protected ProductOfferingService productOfferingService;

    @Autowired
    protected PartnerService partnerService;

    @Autowired
    protected StockTransActionService stockTransActionService;


    @Autowired
    protected SignFlowDetailService signFlowService;

    @Autowired
    protected StockTransVofficeService stockTransVofficeService;

    @Autowired
    protected AutoSignVOfficeService autoSignVOfficeService;

    @Autowired
    protected StockDebitService stockDebitService;

    protected static String CREATE_MULTI_DATA_KEY_STATUS = "1";
    protected static String CREATE_MULTI_DATA_KEY_LIST = "2";

    public static final String OTHER = "FROM_OTHER";
    public static final String WRONG_MOD = "WRONG_MOD";
    public static final String FILE_TEMPLATE_STOCK_INSPECT = "exportStockInspect.xls";
    public static final String FILE_TEMPLATE_GPON_ERROR = "updateSerialGponError.xls";
    public static final String FILE_TEMPLATE_MULTI_ERROR = "updateSerialMultiError.xls";
    public static final String FILE_TEMPLATE_PINCODE_RESULT = "updatePincodeCardResult.xls";
    public static final String FILE_TEMPLATE_LICENSE_RESULT = "updateLicenseKeyResult.xls";
    public static final int STOCK_INSPECT_ROW_START = 6;
    public static final int FILE_TEMPLATE_GPON_ROW_START = 1;
    protected String actionCodeNote;
    protected String actionCodeNotePrint;
    protected Boolean showAutoOrderNote = false;
    protected Long currentActionId;
    protected StockDebitDTO stockDebitDTO;

    /**
     * @param selectedStockTransDTO
     * @param lstStockTransDetailDTO
     * @return
     * @throws LogicException
     * @throws Exception
     * @desc chi cho cac chuc nang lap phieu lap lenh, xuat kho dung
     * @author LuanNT23
     */
    public StreamedContent exportStockTransDetail(StockTransDTO selectedStockTransDTO, List lstStockTransDetailDTO) throws LogicException, Exception {
        if (DataUtil.isNullObject(selectedStockTransDTO)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "stock.trans.search.validate.selecttofinddetail");
        }
        validateStockTrans(selectedStockTransDTO);
        if (!DataUtil.isNullOrZero(selectedStockTransDTO.getStockTransActionId())) {
            List<Long> actions = Lists.newArrayList(selectedStockTransDTO.getStockTransActionId());
            lstStockTransDetailDTO = DataUtil.defaultIfNull(transService.searchStockTransDetail(actions), Lists.newArrayList());
        }

        List lstStockTransDetail = Lists.newArrayList();
        int index = 1;
        if (!DataUtil.isNullOrEmpty(lstStockTransDetailDTO)) {
            for (Object o : lstStockTransDetailDTO) {
                if (o instanceof StockTransFullDTO) {
                    StockTransFullDTO stockTransFullDTO = (StockTransFullDTO) o;
                    stockTransFullDTO.setIndex(index++);
                    lstStockTransDetail.add(stockTransFullDTO);
                } else if (o instanceof StockTransDetailDTO) {
                    StockTransDetailDTO stockTransDetailDTO = (StockTransDetailDTO) o;
                    stockTransDetailDTO.setIndex(index++);
                    lstStockTransDetail.add(stockTransDetailDTO);
                }
            }
        }
        FileExportBean fileExportBean = new FileExportBean();
        fileExportBean.setTempalatePath(FileUtil.getTemplatePath());
        fileExportBean.setTemplateName(getExportDetailTemplateFileName(selectedStockTransDTO, OTHER));
        fileExportBean.setOutPath(FileUtil.getOutputPath());
        fileExportBean.setOutName(getReportDetailOutName(selectedStockTransDTO, OTHER));
        fileExportBean.putValue("lstStockTransFull", lstStockTransDetail);
        fileExportBean.putValue("userCreate", BccsLoginSuccessHandler.getStaffDTO().getName());
        fileExportBean.putValue("dateCreate", optionSetValueService.getSysdateFromDB(true));
        fileExportBean.putValue("toOwnerName", selectedStockTransDTO.getToOwnerName());
        fileExportBean.putValue("reasonName", buildReasonName(selectedStockTransDTO, OTHER));
        fileExportBean.putValue("fromOwnerName", selectedStockTransDTO.getFromOwnerName());
        fileExportBean.putValue("fromOwnerAddress", selectedStockTransDTO.getFromOwnerAddress());
        fileExportBean.putValue("toOwnerAddress", selectedStockTransDTO.getToOwnerAddress());
        buildActionCode(fileExportBean, selectedStockTransDTO);
        logger.error("Owner: " + selectedStockTransDTO.getFromOwnerId() + " Type:" + selectedStockTransDTO.getFromOwnerType() + " Template: " + fileExportBean.getTemplateName());
        return FileUtil.exportToStreamed(fileExportBean);
//                FileUtil.exportFile(fileExportBean);
//                return fileExportBean.getExportFileContent();
    }

    public List<String> getLsStringGuide() {
        return Lists.newArrayList(getText("user.guider.link.update.voffice"));
    }

    //goi tu quan ly gd kho thi de param=null nguoc lai de param bat ky
    public String buildReasonName(StockTransDTO selectedStockTransDTO, String... param) {
        String reason = selectedStockTransDTO.getReasonName();
        if (param == null || param.length == 0) {
            if (Const.STOCK_TRANS_STATUS.EXPORT_ORDER.equals(selectedStockTransDTO.getActionType())) {
                reason = getMessage("export.trans.reason", selectedStockTransDTO.getActionCode(),
                        DateUtil.date2ddMMyyyyString(selectedStockTransDTO.getCreateDatetime()));
                if (!DataUtil.isNullOrEmpty(selectedStockTransDTO.getNote())) {
                    reason += "(" + selectedStockTransDTO.getNote() + ")";
                }
            } else if (selectedStockTransDTO.getStockTransId() != null) {
                StockTransActionDTO search = new StockTransActionDTO();
                search.setStockTransId(selectedStockTransDTO.getStockTransId());
                search.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_ORDER);
                StockTransActionDTO actionExp = stockTransActionService.findStockTransActionDTO(search);
                String note = "";
                if (DataUtil.isNullObject(actionExp)) {
                    search.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_ORDER);
                    StockTransActionDTO actionImp = stockTransActionService.findStockTransActionDTO(search);
                    if (actionImp != null) {
                        reason = getMessage("export.trans.reason", actionImp.getActionCode(),
                                DateUtil.date2ddMMyyyyString(actionImp.getCreateDatetime()));
                        note = actionImp.getNote();
                    }
                } else {
                    reason = getMessage("export.trans.reason", actionExp.getActionCode(),
                            DateUtil.date2ddMMyyyyString(actionExp.getCreateDatetime()));
                    note = actionExp.getNote();
                }
                if (!DataUtil.isNullOrEmpty(note)) {
                    reason += "(" + note + ")";
                }
            }
        } else {
            String note = "";
            if (Const.STOCK_TRANS_ACTION_TYPE.COMMAND.equals(selectedStockTransDTO.getActionType())
                    && Const.STOCK_TRANS_TYPE.EXPORT.equals(selectedStockTransDTO.getStockTransStatus())) {
                reason = getMessage("export.trans.reason", selectedStockTransDTO.getActionCode(),
                        DateUtil.date2ddMMyyyyString(selectedStockTransDTO.getCreateDatetime()));
                note = selectedStockTransDTO.getNote();
            } else if (selectedStockTransDTO.getStockTransId() != null) {
                StockTransActionDTO search = new StockTransActionDTO();
                search.setStockTransId(selectedStockTransDTO.getStockTransId());
                search.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_ORDER);
                StockTransActionDTO actionExp = stockTransActionService.findStockTransActionDTO(search);

                if (DataUtil.isNullObject(actionExp)) {
                    search.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_ORDER);
                    StockTransActionDTO actionImp = stockTransActionService.findStockTransActionDTO(search);
                    if (actionImp != null) {
                        reason = getMessage("export.trans.reason", actionImp.getActionCode(),
                                DateUtil.date2ddMMyyyyString(actionImp.getCreateDatetime()));
                        note = actionImp.getNote();
                    }
                } else {
                    reason = getMessage("export.trans.reason", actionExp.getActionCode(),
                            DateUtil.date2ddMMyyyyString(actionExp.getCreateDatetime()));
                    note = actionExp.getNote();
                }
            }
            if (!DataUtil.isNullOrEmpty(note) && !DataUtil.isNullOrEmpty(reason)) {
                reason += "(" + note + ")";
            }
        }
        return reason;
    }

    //goi tu quan ly gd kho thi de param=null nguoc lai de param bat ky
    public String buildReasonNameBBBG(StockTransDTO selectedStockTransDTO, String... param) {
        String reason = selectedStockTransDTO.getReasonName();
        if (param == null || param.length == 0) {
            //goi tu quan ly giao dich kho
            String note = "";
            if (Const.STOCK_TRANS_STATUS.EXPORT_NOTE.equals(selectedStockTransDTO.getActionType())
                    || Const.STOCK_TRANS_STATUS.EXPORTED.equals(selectedStockTransDTO.getActionType())) {
                reason = getMessage("export.trans.reasonBBBG", selectedStockTransDTO.getActionCode(),
                        DateUtil.date2ddMMyyyyString(selectedStockTransDTO.getCreateDatetime()));
            } else if (selectedStockTransDTO.getStockTransId() != null) {
                StockTransActionDTO search = new StockTransActionDTO();
                search.setStockTransId(selectedStockTransDTO.getStockTransId());
                search.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
                StockTransActionDTO actionExp = stockTransActionService.findStockTransActionDTO(search);
                if (actionExp != null) {
                    reason = getMessage("export.trans.reasonBBBG", actionExp.getActionCode(),
                            DateUtil.date2ddMMyyyyString(actionExp.getCreateDatetime()));
                }
            }
            if (!DataUtil.isNullOrEmpty(note) && !DataUtil.isNullOrEmpty(reason)) {
                reason += "(" + note + ")";
            }
        } else {
            //goi tu cac chuc nang khac
            String note = "";
            if (Const.STOCK_TRANS_ACTION_TYPE.NOTE.equals(selectedStockTransDTO.getActionType())
                    && Const.STOCK_TRANS_TYPE.EXPORT.equals(selectedStockTransDTO.getStockTransStatus())) {
                reason = getMessage("export.trans.reasonBBBG", selectedStockTransDTO.getActionCode(),
                        DateUtil.date2ddMMyyyyString(selectedStockTransDTO.getCreateDatetime()));
                note = selectedStockTransDTO.getNote();
            } else if (selectedStockTransDTO.getStockTransId() != null) {
                StockTransActionDTO search = new StockTransActionDTO();
                search.setStockTransId(selectedStockTransDTO.getStockTransId());
                search.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
                StockTransActionDTO actionExp = stockTransActionService.findStockTransActionDTO(search);
                if (actionExp != null) {
                    reason = getMessage("export.trans.reasonBBBG", actionExp.getActionCode(),
                            DateUtil.date2ddMMyyyyString(actionExp.getCreateDatetime()));
                    note = actionExp.getNote();
                }

                search.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
                StockTransActionDTO actionImp = stockTransActionService.findStockTransActionDTO(search);
                if (actionImp != null) {
                    reason = getMessage("export.trans.reasonBBBG", actionImp.getActionCode(),
                            DateUtil.date2ddMMyyyyString(actionImp.getCreateDatetime()));
                    note = actionImp.getNote();
                }
            }
            if (!DataUtil.isNullOrEmpty(note) && !DataUtil.isNullOrEmpty(reason)) {
                reason += "(" + note + ")";
            }
        }
        return reason;
    }

    private void buildActionCode(FileExportBean fileExportBean, StockTransDTO selectedStockTransDTO) throws Exception {
        StockTransActionDTO stockTransActionDTO;
        if (DataUtil.isNullOrZero(selectedStockTransDTO.getStockTransActionId())) {
            StockTransActionDTO search = new StockTransActionDTO();
            search.setCreateUser(getStaffDTO().getStaffCode());
            search.setActionCode(selectedStockTransDTO.getActionCode());
            search.setStatus(selectedStockTransDTO.getStatus());
            stockTransActionDTO = stockTransActionService.findStockTransActionDTO(search);
        } else {
            stockTransActionDTO = stockTransActionService.findOne(selectedStockTransDTO.getStockTransActionId());
        }
        if (stockTransActionDTO != null && !DataUtil.isNullOrEmpty(stockTransActionDTO.getActionCode())) {
            String actionCode = stockTransActionDTO.getActionCode();
            fileExportBean.putValue("actionCodeFull", actionCode == null ? "" : actionCode.toUpperCase());
            if (DataUtil.isNullOrEmpty(stockTransActionDTO.getActionCodeShop())) {
                if (!Const.STOCK_TRANS_STATUS.IMPORT_ORDER.equals(stockTransActionDTO.getStatus())
                        && !Const.STOCK_TRANS_STATUS.EXPORT_ORDER.equals(stockTransActionDTO.getStatus())) {
                    String[] subAction = stockTransActionDTO.getActionCode().split("_");
                    actionCode = subAction[subAction.length - 1];
                }
            } else {
                actionCode = stockTransActionDTO.getActionCodeShop();
            }
            fileExportBean.putValue("receiptNO", actionCode == null ? "" : actionCode.toUpperCase());
            fileExportBean.putValue("actionCode", actionCode == null ? "" : actionCode.toUpperCase());
        }
    }

    public String getExportDetailTemplateFileName(StockTransDTO stockTransDTO, String... params) throws LogicException, Exception {
        //action type = trang thai cua stock_trans_action
        String prefix = getPrefixName(stockTransDTO, params);
        String sub = getSubName(stockTransDTO, params);
        return "stock_trans_template" + "/" + prefix + "_" + sub + ".xls";
    }

    /**
     * @param stockTransDTO
     * @param params
     * @return
     * @throws Exception
     * @desc neu goi tu quan ly giao dich,  de trong
     */
    public String getSubName(StockTransDTO stockTransDTO, String... params) throws Exception {
        String sub = "PARTNER";
        Long ownerId = stockTransDTO.getFromOwnerId();
        Long ownerType = stockTransDTO.getFromOwnerType();
        if (params != null && params.length > 0) {
            //goi tu cac chuc nang khac
            if (Const.STOCK_TRANS_TYPE.IMPORT.equals(stockTransDTO.getStockTransStatus())) {
                ownerId = stockTransDTO.getToOwnerId();
                ownerType = stockTransDTO.getToOwnerType();
                //neu nhap hang cho doi tac
                if (Const.OWNER_TYPE.PARTNER_LONG.equals(stockTransDTO.getToOwnerType())) {
                    return sub;
                }
            }
        } else {
            //goi tu quan ly giao dich kho
            if (Const.STOCK_TRANS_STATUS.IMPORT_ORDER.equals(stockTransDTO.getActionType())
                    || Const.STOCK_TRANS_STATUS.IMPORT_NOTE.equals(stockTransDTO.getActionType())
                    || Const.STOCK_TRANS_STATUS.IMPORTED.equals(stockTransDTO.getActionType())) {
                ownerId = stockTransDTO.getToOwnerId();
                ownerType = stockTransDTO.getToOwnerType();
                //neu nhap tu doi tac
                if (Const.OWNER_TYPE.PARTNER_LONG.equals(stockTransDTO.getToOwnerType())) {
                    return sub;
                }
            }
        }
        if (Const.OWNER_TYPE.PARTNER_LONG.equals(stockTransDTO.getFromOwnerType())) {
            return sub;
        }
        String check = commonService.getStockReportTemplate(ownerId, DataUtil.safeToString(ownerType));
        if (DataUtil.isNullOrEmpty(check)) {
            sub = "CN";
        } else {
            sub = check;
        }

        return sub;
    }

    //Goi tu cac chuc nang lap phieu lap lenh thi truyen command bat ky vao
    public String getPrefixName(StockTransDTO stockTransDTO, String... command) throws Exception {
        String prefix = "LX";
        if (command != null && command.length > 0) {
            //action type giong im 1  {1:command, 2:note}
            //stock trans status giong im 1  {1:export, 2:import}
            if (DataUtil.safeEqual(stockTransDTO.getStockTransType(), Const.STOCK_TRANS_TYPE.ISDN)
                    && Const.STOCK_TRANS_ACTION_TYPE.NOTE.equals(stockTransDTO.getActionType())) {
                prefix = "PXS";
            } else {
                //phieu thu hoi thi sao
                String prefix1 = Const.STOCK_TEMPLATE_PREFIX.COMMAND;//L
                String prefix2 = Const.STOCK_TEMPLATE_PREFIX.EXPORT;//X
                if (Const.STOCK_TRANS_ACTION_TYPE.NOTE.equals(stockTransDTO.getActionType())) {
                    prefix1 = Const.STOCK_TEMPLATE_PREFIX.NOTE;
                }
                if (Const.STOCK_TRANS_TYPE.IMPORT.equals(stockTransDTO.getStockTransStatus())) {
                    prefix2 = Const.STOCK_TEMPLATE_PREFIX.IMPORT;
                }
                prefix = prefix1 + prefix2;
            }
        } else {
            //Goi tu quan ly giao dich kho, khong co pxs
            if (Const.STOCK_TRANS_STATUS.EXPORT_NOTE.equals(stockTransDTO.getActionType())
                    || Const.STOCK_TRANS_STATUS.EXPORTED.equals(stockTransDTO.getActionType())) {
                prefix = "PX";
            }
            if (Const.STOCK_TRANS_STATUS.IMPORT_ORDER.equals(stockTransDTO.getActionType())) {
                prefix = "LN";
            }
            if (Const.STOCK_TRANS_STATUS.IMPORT_NOTE.equals(stockTransDTO.getActionType())
                    || Const.STOCK_TRANS_STATUS.IMPORTED.equals(stockTransDTO.getActionType())) {
                prefix = "PN";
            }
        }
        return prefix;
    }


    // Ham in lenh xuat excel
    public StreamedContent exportDepositDetail(StockTransDTO stockTransDTO, ReceiptExpenseDTO receiptExpenseDTO, List lstStockTransDetail) {
        try {
            if (DataUtil.isNullObject(stockTransDTO)) {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stockTrans.dto.print.not.exists");
            } else {
                FileExportBean fileExportBean = new FileExportBean();
                fileExportBean.setTempalatePath(FileUtil.getTemplatePath());
                fileExportBean.setTemplateName(Const.REPORT_TEMPLATE.STOCK_TRANS_TEMPLATE_SUB_PATH + Const.FILE_TEMPLATE.FILE_NAME_PT_DL_TEMPLATE);
                fileExportBean.setOutPath(FileUtil.getOutputPath());
                fileExportBean.setOutName(Const.FILE_TEMPLATE.FILE_NAME_PT_DL_TEMPLATE);

                fileExportBean.putValue("lstReceiptBillDetail", lstStockTransDetail);
                fileExportBean.putValue("deliverer", receiptExpenseDTO.getDeliverer());
                fileExportBean.putValue("receiptNo", receiptExpenseDTO.getReceiptNo());
                fileExportBean.putValue("payMethod", "");
                fileExportBean.putValue("reasonName", buildReasonName(stockTransDTO, OTHER));

                fileExportBean.putValue("actionCode", stockTransDTO.getActionCode());
                fileExportBean.putValue("userCreate", BccsLoginSuccessHandler.getStaffDTO().getName());
                fileExportBean.putValue("dateCreate", optionSetValueService.getSysdateFromDB(true));
                fileExportBean.putValue("toOwnerName", stockTransDTO.getToOwnerName());
                fileExportBean.putValue("fromOwnerName", stockTransDTO.getFromOwnerName());
                fileExportBean.putValue("fromOwnerAddress", stockTransDTO.getFromOwnerAddress());
                fileExportBean.putValue("toOwnerAddress", stockTransDTO.getToOwnerAddress());
                return FileUtil.exportToStreamed(fileExportBean);
//                FileUtil.exportFile(fileExportBean);
//                return fileExportBean.getExportFileContent();
            }
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happen", ex);
        }
        return null;
    }


    public StreamedContent exportStockTransDetail(StockTransDTO selectedStockTransDTO, String... command) throws LogicException, Exception {
        if (DataUtil.isNullObject(selectedStockTransDTO)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "stock.trans.search.validate.selecttofinddetail");
        }
        validateStockTrans(selectedStockTransDTO);
        List<Long> stockTransActionIds = Lists.newArrayList();
        stockTransActionIds.add(selectedStockTransDTO.getStockTransActionId());
        List<StockTransFullDTO> stockTransDetailDTOs = DataUtil.defaultIfNull(transService.searchStockTransDetail(stockTransActionIds), new ArrayList<StockTransFullDTO>());
        for (StockTransFullDTO stockTransFullDTO : stockTransDetailDTOs) {
            StockTransSerialDTO search = new StockTransSerialDTO();
            search.setStockTransDetailId(stockTransFullDTO.getStockTransDetailId());
            List<StockTransSerialDTO> stockTransSerials = DataUtil.defaultIfNull(stockTransSerialService.findStockTransSerialByDTO(search), new ArrayList<>());
            stockTransFullDTO.setLstSerial(stockTransSerials);
        }
        FileExportBean fileExportBean = new FileExportBean();
        fileExportBean.setTempalatePath(FileUtil.getTemplatePath());
        if (command == null || command.length == 0) {
            fileExportBean.setTemplateName(getExportDetailTemplate(selectedStockTransDTO));
        } else {
            fileExportBean.setTemplateName(getExportDetailTemplate(selectedStockTransDTO, command[0]));
        }
        fileExportBean.setOutPath(FileUtil.getOutputPath());
        fileExportBean.setOutName(getReportDetailOutName(selectedStockTransDTO, command));
        fileExportBean.putValue("lstStockTransFull", stockTransDetailDTOs);
        buildActionCode(fileExportBean, selectedStockTransDTO);
        fileExportBean.putValue("userCreate", BccsLoginSuccessHandler.getStaffDTO().getName());
        fileExportBean.putValue("dateCreate", optionSetValueService.getSysdateFromDB(true));
        fileExportBean.putValue("toOwnerName", selectedStockTransDTO.getToOwnerName());
        fileExportBean.putValue("reasonName", buildReasonName(selectedStockTransDTO, OTHER));
        fileExportBean.putValue("fromOwnerName", selectedStockTransDTO.getFromOwnerName());
        fileExportBean.putValue("fromOwnerAddress", selectedStockTransDTO.getFromOwnerAddress());
        fileExportBean.putValue("toOwnerAddress", selectedStockTransDTO.getToOwnerAddress());
        fileExportBean.putValue("note", selectedStockTransDTO.getNote());
        logger.error("Owner: " + selectedStockTransDTO.getFromOwnerId() + " Type:" + selectedStockTransDTO.getFromOwnerType() + " Template: " + fileExportBean.getTemplateName());

        return FileUtil.exportToStreamed(fileExportBean);
//                FileUtil.exportFile(fileExportBean);
//                return fileExportBean.getExportFileContent();
    }

    /**
     * Check from, to trong giao dich, truyen bo xung, name, address
     *
     * @param stockTransDTO
     * @throws Exception
     */
    private void validateStockTrans(StockTransDTO stockTransDTO) throws Exception {
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
            if (Const.OWNER_TYPE.SHOP_LONG.equals(stockTransDTO.getToOwnerType()) || Const.OWNER_TYPE.STAFF_LONG.equals(stockTransDTO.getToOwnerType())) {
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

    public String getReportDetailOutName(StockTransDTO stockTransDTO, String... params) {
        try {
            doValidateActionCode(stockTransDTO.getActionCode());
            StringBuilder builder = new StringBuilder(getPrefixName(stockTransDTO, params));
            builder.append("_");
            builder.append(getSafeActionCode(stockTransDTO.getActionCode())).append("_").append(getSubName(stockTransDTO, params)).append(".xls");
            return builder.toString();
        } catch (Exception ex) {
            StringBuilder log = new StringBuilder(this.getClass().getSimpleName());
            log.append("||").append(new Date()).append("||action_code:").append(stockTransDTO.getActionCode()).append(" khong hop le!");
            logger.error(log.toString());
            logger.error(ex.getMessage(), ex);
        }
        return getText("common.report.trans.detail.name.default") + ".xls";
    }

    public String getSafeActionCode(String actionCode) throws Exception {
        return actionCode.replaceAll("\\W", "");
    }

    /**
     * @param fromManager   neu goi tu manage thi truyen a vao
     * @param stockTransDTO
     * @return biên bản bàn giao
     * @throws LogicException
     * @throws Exception
     * @author LuanNT23
     */
    public StreamedContent exportHandOverReport(StockTransDTO stockTransDTO, String... fromManager) throws LogicException, Exception {
        if (stockTransDTO == null) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "stock.trans.search.validate.selecttohanover");
        }
        //goi tu quan ly giao dich
        String[] params = {};
        if (fromManager == null || fromManager.length == 0) {
            //goi tu chuc nang khac
            params = new String[]{OTHER};
        }
        List<Long> stockTransActionIds = Lists.newArrayList();
        stockTransActionIds.add(stockTransDTO.getStockTransActionId());
        List<StockTransFullDTO> stockTransDetailDTOs = transService.searchStockTransDetail(stockTransActionIds);
        Long index = 1L;
        for (StockTransFullDTO stockTransDetail : stockTransDetailDTOs) {
            StockTransSerialDTO search = new StockTransSerialDTO();
            search.setStockTransDetailId(stockTransDetail.getStockTransDetailId());
            //
            stockTransDetail.setActionId(index++);
            stockTransDetail.setIndex(stockTransDetail.getActionId().intValue());
            //
            List<StockTransSerialDTO> stockTransSerials = stockTransSerialService.findStockTransSerialByDTO(search);
            if (DataUtil.isNullOrEmpty(stockTransSerials)) {
                stockTransSerials = Lists.newArrayList();
            }
            stockTransDetail.setLstSerial(stockTransSerials);
        }

        FileExportBean fileExportBean = new FileExportBean();
        fileExportBean.setTempalatePath(FileUtil.getTemplatePath());
        fileExportBean.setTemplateName(getHandOverReportTemplateCommon(stockTransDTO));
        fileExportBean.setOutPath(FileUtil.getOutputPath());
        fileExportBean.setOutName(new StringBuilder("BBBGCT_").append(getSafeActionCode(stockTransDTO.getActionCode()).toUpperCase()).append("_").append(getSubName(stockTransDTO, params)).append(".xls").toString());
        fileExportBean.putValue("lstStockTransFull", stockTransDetailDTOs);
        fileExportBean.putValue("actionCode", stockTransDTO.getActionCode());
        fileExportBean.putValue("reasonNameBBBG", buildReasonNameBBBG(stockTransDTO, params));
//        fileExportBean.putValue("userCreate", BccsLoginSuccessHandler.getStaffDTO().getName());
        fileExportBean.putValue("dateCreate", optionSetValueService.getSysdateFromDB(true));
        fileExportBean.putValue("toOwnerName", stockTransDTO.getToOwnerName());
        fileExportBean.putValue("reasonName", buildReasonName(stockTransDTO, params));
        fileExportBean.putValue("fromOwnerName", stockTransDTO.getFromOwnerName());
        fileExportBean.putValue("fromOwnerAddress", stockTransDTO.getFromOwnerAddress());
        fileExportBean.putValue("toOwnerAddress", stockTransDTO.getToOwnerAddress());
        return FileUtil.exportToStreamed(fileExportBean);
//                FileUtil.exportFile(fileExportBean);
//                return fileExportBean.getExportFileContent();
    }

    /**
     * @param selectedStockTransDTO
     * @return
     * @throws LogicException
     * @throws Exception
     * @author LuanNT23
     */
    public String getExportDetailTemplate(StockTransDTO selectedStockTransDTO, String... command) throws LogicException, Exception {
        String subPath = Const.REPORT_TEMPLATE.STOCK_TRANS_TEMPLATE_SUB_PATH;
        if (!DataUtil.isNullObject(selectedStockTransDTO)) {
            String prefix1 = Const.STOCK_TEMPLATE_PREFIX.COMMAND;//L
            String prefix2 = Const.STOCK_TEMPLATE_PREFIX.EXPORT;//X
            if (command == null || command.length == 0 || WRONG_MOD.equals(command[0])) {
                if (Const.STOCK_TRANS_ACTION_TYPE.NOTE.equals(selectedStockTransDTO.getActionType())) {
                    prefix1 = Const.STOCK_TEMPLATE_PREFIX.NOTE;
                }
                if (Const.STOCK_TRANS_TYPE.IMPORT.equals(selectedStockTransDTO.getStockTransStatus())) {
                    prefix2 = Const.STOCK_TEMPLATE_PREFIX.IMPORT;
                }
            } else {
                if (DataUtil.isNullOrEmpty(selectedStockTransDTO.getActionType()) && DataUtil.isNullOrEmpty(selectedStockTransDTO.getStockTransType())) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "ws.delete.err.status");
                } else {
                    if (Const.STOCK_TRANS_ACTION_TYPE.NOTE.equals(selectedStockTransDTO.getActionType())) {
                        prefix1 = Const.STOCK_TEMPLATE_PREFIX.NOTE;
                    }
                    if (Const.STOCK_TRANS_TYPE.IMPORT.equals(selectedStockTransDTO.getStockTransType())) {
                        prefix2 = Const.STOCK_TEMPLATE_PREFIX.IMPORT;
                    }
                }
            }
            Long ownerId = selectedStockTransDTO.getFromOwnerId();
            Long ownerType = selectedStockTransDTO.getFromOwnerType();
            //
            if (Const.STOCK_TRANS_TYPE.IMPORT.equals(selectedStockTransDTO.getStockTransStatus())) {
                ownerId = selectedStockTransDTO.getToOwnerId();
                ownerType = selectedStockTransDTO.getToOwnerType();
            }
            //
            String prefix3 = commonService.getStockReportTemplate(ownerId, DataUtil.safeToString(ownerType));
            if (DataUtil.isNullObject(prefix3)) {
                prefix3 = "CN";
            }
            return subPath + prefix1 + prefix2 + "_" + prefix3 + ".xls";
        }
        return "";
    }

    public String getHandOverReportTemplateCommon(StockTransDTO selectedStockTransDTO, String... params) throws LogicException, Exception {
        String subPath = Const.REPORT_TEMPLATE.STOCK_TRANS_TEMPLATE_SUB_PATH;
        if (!DataUtil.isNullObject(selectedStockTransDTO)) {
            String prefix1 = Const.REPORT_TEMPLATE.STOCK_TRANS_HANDOVER_REPORT;
            String prefix3 = getSubName(selectedStockTransDTO, params);
            return subPath + prefix1 + "_" + prefix3 + ".xls";
        }
        return "";
    }

    public void validateDate(Date fromDate, Date toDate) throws LogicException, Exception {
        Date sysdate = getSysdateFromDB();
        if (fromDate == null || toDate == null) {
            if (fromDate == null) {
                executeCommand("focusElementError('fromDate')");
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.from.date.not.blank");
            } else {
                executeCommand("focusElementError('toDate')");
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.to.date.not.blank");
            }
        } else {
            if (fromDate.after(sysdate)) {
                executeCommand("focusElementError('fromDate')");
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.trans.from.valid");
            }
            if (toDate.before(fromDate)) {
                executeCommand("focusElementError('fromDate')");
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.trans.from.than.to");
            }
            if (toDate.getTime() - fromDate.getTime() > Const.MONTH_IN_MILLISECOND) {
                executeCommand("focusElementError('fromDate')");
                executeCommand("focusElementError('toDate')");
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.trans.from.to.valid");
            }
        }
    }

    public StockTransDTO genStockTransDTO(VStockTransDTO vStockTransDTO) throws LogicException, Exception {
        if (DataUtil.isNullObject(vStockTransDTO)) {
            return new StockTransDTO();
        }
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setCreateDatetime(vStockTransDTO.getCreateDateTime());
        stockTransDTO.setUserCreate(vStockTransDTO.getUserCreate());
        stockTransDTO.setActionCode(vStockTransDTO.getActionCode());
        stockTransDTO.setActionType(vStockTransDTO.getActionType());
        stockTransDTO.setReasonName(vStockTransDTO.getReasonName());
        stockTransDTO.setReasonId(vStockTransDTO.getReasonID());
        stockTransDTO.setNote(vStockTransDTO.getNote());
        stockTransDTO.setStockTransActionId(vStockTransDTO.getActionID());
        stockTransDTO.setStockTransId(vStockTransDTO.getStockTransID());
        stockTransDTO.setStatus(vStockTransDTO.getStockTransStatus());
        stockTransDTO.setStockTransStatus(DataUtil.safeToString(vStockTransDTO.getStockTransType()));
        stockTransDTO.setIsAutoGen(vStockTransDTO.getIsAutoGen());
        //stock trans type
        stockTransDTO.setStockTransType(DataUtil.safeToString(vStockTransDTO.getStockTransType()));
        //from
        stockTransDTO.setFromOwnerId(vStockTransDTO.getFromOwnerID());
        stockTransDTO.setFromOwnerType(vStockTransDTO.getFromOwnerType());
        stockTransDTO.setFromOwnerName(vStockTransDTO.getFromOwnerName());
        if (Const.OWNER_TYPE.SHOP_LONG.equals(vStockTransDTO.getFromOwnerType())) {
            ShopDTO shop = shopService.findOne(stockTransDTO.getFromOwnerId());
            if (!DataUtil.isNullObject(shop)) {
                stockTransDTO.setFromOwnerAddress(shop.getAddress());
                stockTransDTO.setFromOwnerName(shop.getName());
                stockTransDTO.setFromOwnerCode(shop.getShopCode());
            }
        } else {
            StaffDTO staff = staffService.findOne(stockTransDTO.getFromOwnerId());
            if (!DataUtil.isNullObject(staff)) {
                stockTransDTO.setFromOwnerAddress(staff.getAddress());
                stockTransDTO.setFromOwnerName(staff.getName());
                stockTransDTO.setFromOwnerCode(staff.getStaffCode());
            }
        }
        //to
        stockTransDTO.setToOwnerId(vStockTransDTO.getToOwnerID());
        stockTransDTO.setToOwnerType(vStockTransDTO.getToOwnerType());
        stockTransDTO.setToOwnerName(vStockTransDTO.getToOwnerName());
        if (Const.OWNER_TYPE.SHOP_LONG.equals(vStockTransDTO.getToOwnerType())) {
            ShopDTO shop = shopService.findOne(stockTransDTO.getToOwnerId());
            if (!DataUtil.isNullObject(shop)) {
                stockTransDTO.setToOwnerAddress(shop.getAddress());
                stockTransDTO.setToOwnerName(shop.getName());
                stockTransDTO.setToOwnerCode(shop.getShopCode());
            }
        } else {
            StaffDTO staff = staffService.findOne(stockTransDTO.getToOwnerId());
            if (!DataUtil.isNullObject(staff)) {
                stockTransDTO.setToOwnerAddress(staff.getAddress());
                stockTransDTO.setToOwnerName(staff.getName());
                stockTransDTO.setToOwnerCode(staff.getStaffCode());
            }
        }

        return stockTransDTO;
    }

    /**
     * @param stockTransDetails
     * @author ThanhNT77
     * @copy LuanNT23 :v
     */
    public void doValidateListDetail(List<StockTransDetailDTO> stockTransDetails) {
        if (DataUtil.isNullOrEmpty(stockTransDetails)) {
            topReportError("", ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "stock.trans.staff.export.detailRequired");
            return;
        }
        Map<String, String> mapValidate = validateEmptyStockTransDetail(stockTransDetails);
        if (!DataUtil.isNullOrEmpty(mapValidate)) {
            boolean isError = false;
            if (!DataUtil.isNullOrEmpty(mapValidate.get("VALID_ERROR_EMPTY_PROD_NAME"))) {
                isError = true;
                reportError("", ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "export.order.stock.inputText.require.msg");
                executeCommand("focusElementErrorByListClass('txtProductName','" + mapValidate.get("VALID_ERROR_EMPTY_PROD_NAME") + "')");
            }
            if (!DataUtil.isNullOrEmpty(mapValidate.get("VALID_ERROR_EMPTY_QUANTITY"))) {
                isError = true;
                reportError("", ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "export.order.stock.number.format.msg");
                executeCommand("focusElementErrorByListClass('txtNumber','" + mapValidate.get("VALID_ERROR_EMPTY_QUANTITY") + "')");
            }
            if (!DataUtil.isNullOrEmpty(mapValidate.get("VALID_ERROR_QUANTITY"))) {
                isError = true;
                reportError("", ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.stock.quantity.msg");
                executeCommand("focusElementErrorByListClass('txtNumber','" + mapValidate.get("VALID_ERROR_QUANTITY") + "')");
            }
            if (isError) {
                topPage();
            } else {
                doValidateDuplicateListDetail(stockTransDetails);
            }
        }
    }

    private void doValidateDuplicateListDetail(List<StockTransDetailDTO> stockTransDetails) {
        Map<String, String> mapValidate = validateDuplidateStockTransDetail(stockTransDetails);
        if (!DataUtil.isNullOrEmpty(mapValidate)) {
            boolean isError = false;
            if (!DataUtil.isNullOrEmpty(mapValidate.get("VALID_ERROR_DUPLICATE"))) {
                isError = true;
                reportError("", ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "list.product.validate.duplicate");
                executeCommand("focusElementErrorByListClass('txtProductName','" + mapValidate.get("VALID_ERROR_DUPLICATE") + "')");
                executeCommand("focusElementErrorByListClass('txtState','" + mapValidate.get("VALID_ERROR_QUANTITY") + "')");
            }
            if (isError) {
                topPage();
            }
        }
    }

    public void doValidateActionCode(String actionCode) throws LogicException, Exception {
        if (DataUtil.isNullOrEmpty(actionCode)) {
            throw new LogicException("", "");
        }
        if (actionCode.getBytes("UTF-8").length > 50) {
            throw new LogicException("", "");
        }
        if (!actionCode.matches("^[A-Za-z0-9_-]+")) {
            throw new LogicException("", "");
        }
    }

    @Secured("@")
    public String getOwnerCode(Object ownerID, String ownerType) {
        try {
            if (!DataUtil.isNullObject(ownerID)) {
                return commonService.getOwnerName(DataUtil.safeToString(ownerID), ownerType);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return "";
    }

    @Secured("@")
    public ProductOfferingDTO getProductOffering(Long productOfferID) {
        try {
            return productOfferingService.findOne(productOfferID);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return new ProductOfferingDTO();
    }

    public RequiredRoleMap getTransRequiRedRoleMap() {
        return CustomAuthenticationProvider.createRequiredRoleMap(Const.PERMISION.ROLE_SYNC_LOGISTIC, Const.PERMISION.ROLE_TRANSPORT_STOCK, Const.PERMISION.ROLE_XUAT_KHO_BA_MIEN, Const.PERMISION.ROLE_STOCK_NUM_SHOP);
    }

    @Secured("@")
    public PartnerDTO getPartnerCode(Object partnerID) {
        try {
            return partnerService.findOne(DataUtil.safeToLong(partnerID));
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return new PartnerDTO();
    }

    //ham xuat chi tiet loi khi lap nhieu phieu cung luc
    public StreamedContent exportMultiNoteErrorDetail(List<FieldExportFileDTO> listReport) throws LogicException, Exception {
        if (DataUtil.isNullObject(listReport)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "stock.trans.search.validate.selecttofinddetail");
        }
        if (DataUtil.isNullOrEmpty(listReport)) {
            listReport = Lists.newArrayList();
        }
        FileExportBean fileExportBean = new FileExportBean();
        fileExportBean.setTempalatePath(FileUtil.getTemplatePath());
        fileExportBean.setTemplateName("errorExpTransNoteFromFile.xls");
        fileExportBean.setOutPath(FileUtil.getOutputPath());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        fileExportBean.setOutName("errorExpTransNoteFromFile" + "_" + sdf.format(new Date()) + ".xls");
        fileExportBean.putValue("listReport", listReport);

        List<FieldExportFileDTO> listDetail = converStockTransDetailFile(listReport);
        fileExportBean.putValue("listDetail", listDetail);

        return FileUtil.exportToStreamed(fileExportBean);
//        FileUtil.exportFile(fileExportBean);
//        return fileExportBean.getExportFileContent();
    }

    private List<FieldExportFileDTO> converStockTransDetailFile(List<FieldExportFileDTO> listReport) {
        List<FieldExportFileDTO> listDetail = Lists.newArrayList();
        for (FieldExportFileDTO fileDTO : listReport) {
            for (StockTransDetailDTO stockTransDetailDTO : fileDTO.getLsStockTransDetail()) {
                FieldExportFileDTO fileDetailDTO = new FieldExportFileDTO();
                fileDetailDTO.setActionCode(fileDTO.getActionCode());
                fileDetailDTO.setFromOwnerName(fileDTO.getFromOwnerName());
                fileDetailDTO.setToOwnerName(fileDTO.getToOwnerName());
                fileDetailDTO.setFromOwnerCode(fileDTO.getFromOwnerCode());
                fileDetailDTO.setToOwnerCode(fileDTO.getToOwnerCode());
                fileDetailDTO.setActionCodeOrder(fileDTO.getActionCodeOrder());
                fileDetailDTO.setStrCreateDate(fileDTO.getStrCreateDate());
                if (stockTransDetailDTO.getStateId() != null) {
                    if (stockTransDetailDTO.getStateId().equals(1L)) {
                        fileDetailDTO.setStateName(getText("stock.total.state.new"));
                    } else if (stockTransDetailDTO.getStateId().equals(2L)) {
                        fileDetailDTO.setStateName(getText("stock.total.state.old"));
                    } else if (stockTransDetailDTO.getStateId().equals(3L)) {
                        fileDetailDTO.setStateName(getText("stock.total.state.broken"));
                    } else if (stockTransDetailDTO.getStateId().equals(4L)) {
                        fileDetailDTO.setStateName(getText("stock.total.state.retake"));
                    } else if (stockTransDetailDTO.getStateId().equals(5L)) {
                        fileDetailDTO.setStateName(getText("stock.total.state.rescue"));
                    }
                }
                fileDetailDTO.setQuanlity(stockTransDetailDTO.getQuantity());
                fileDetailDTO.setProdOfferCode(stockTransDetailDTO.getProdOfferCode());
                fileDetailDTO.setProdOfferName(stockTransDetailDTO.getProdOfferName());
                listDetail.add(fileDetailDTO);
            }
        }

        return listDetail;
    }

    public Map convertListErrorFile(List<BaseExtMessage> lsMessage, String keyMsgSuccess) throws Exception {
        Map validMap = Maps.newHashMap();

        List<FieldExportFileDTO> fieldExportFileDTOList = Lists.newArrayList();
        boolean isSucess = false;
        for (BaseExtMessage message : lsMessage) {
            FieldExportFileDTO fileDTO = new FieldExportFileDTO();
            if (DataUtil.isNullObject(message.getDescription())) {
                //thanh cong
                fileDTO.setErrorMsg(getText(keyMsgSuccess));
                isSucess = true;
            } else {
                fileDTO.setErrorMsg(message.getDescription());
            }

            StockTransFileDTO stockTransFile = message.getStockTransFileDTO();
            fileDTO.setActionCode(stockTransFile.getStockTransActionDTO().getActionCode());
            fileDTO.setFromOwnerName(stockTransFile.getStockTransDTO().getFromOwnerName());
            fileDTO.setToOwnerName(stockTransFile.getStockTransDTO().getToOwnerName());
            fileDTO.setFromOwnerCode(stockTransFile.getStockTransDTO().getFromOwnerCode());
            fileDTO.setToOwnerCode(stockTransFile.getStockTransDTO().getToOwnerCode());
            fileDTO.setStrState(stockTransFile.getStockTransDTO().getStatusName());

            StockTransActionDTO transActionDTO = stockTransActionService.findOne(stockTransFile.getStockTransActionDTO().getStockTransActionId());
            fileDTO.setActionCodeOrder(transActionDTO.getActionCode());
            fileDTO.setStrCreateDate(DateUtil.date2ddMMyyyyString(transActionDTO.getCreateDatetime()));
            fileDTO.setLsStockTransDetail(stockTransFile.getLstStockTransDetail());

            fieldExportFileDTOList.add(fileDTO);
        }
        validMap.put(CREATE_MULTI_DATA_KEY_STATUS, isSucess);
        validMap.put(CREATE_MULTI_DATA_KEY_LIST, fieldExportFileDTOList);
        return validMap;
    }

    protected void setValueAndStyle(Cell cell, Object value, HSSFCellStyle style) {
        if (value instanceof String) {
            cell.setCellValue(DataUtil.safeToString(value));
        } else {
            cell.setCellValue(DataUtil.safeToLong(value));
        }
        cell.setCellStyle(style);
    }

    public HSSFWorkbook getContentExportStockInspect(List<ApproveStockInspectDTO> lstRealSerial, List<ApproveStockInspectDTO> lstSysSerial, List<ApproveStockInspectDTO> lstProductNotApprove, List<ApproveStockInspectDTO> lstTotal) {
        try {
            InputStream createStream = Faces.getResourceAsStream(Const.BRAS_IPPOOL.RESOURCE_TEMPLATE_PATH + FILE_TEMPLATE_STOCK_INSPECT);
            HSSFWorkbook workbook = new HSSFWorkbook(createStream);
            HSSFCellStyle style = workbook.createCellStyle();
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            Sheet realSerialSheet = workbook.getSheetAt(0);
            if (!DataUtil.isNullOrEmpty(lstRealSerial)) {
                for (int i = 0; i < lstRealSerial.size(); i++) {
                    Row realSerial = realSerialSheet.createRow(i + STOCK_INSPECT_ROW_START);
                    setValueAndStyle(realSerial.createCell(0), i + 1, style);
                    setValueAndStyle(realSerial.createCell(1), lstRealSerial.get(i).getProductCode(), style);
                    setValueAndStyle(realSerial.createCell(2), lstRealSerial.get(i).getProductName(), style);
                    setValueAndStyle(realSerial.createCell(3), lstRealSerial.get(i).getFromSerial(), style);
                    setValueAndStyle(realSerial.createCell(4), lstRealSerial.get(i).getToSearial(), style);
                    setValueAndStyle(realSerial.createCell(5), lstRealSerial.get(i).getQuantity(), style);
                    setValueAndStyle(realSerial.createCell(6), lstRealSerial.get(i).getProductStatus(), style);
                }
            }

            Sheet sysSerialSheet = workbook.getSheetAt(1);
            if (!DataUtil.isNullOrEmpty(lstSysSerial)) {
                for (int i = 0; i < lstSysSerial.size(); i++) {
                    Row sysSerial = sysSerialSheet.createRow(i + STOCK_INSPECT_ROW_START);
                    setValueAndStyle(sysSerial.createCell(0), i + 1, style);
                    setValueAndStyle(sysSerial.createCell(1), lstSysSerial.get(i).getProductCode(), style);
                    setValueAndStyle(sysSerial.createCell(2), lstSysSerial.get(i).getProductName(), style);
                    setValueAndStyle(sysSerial.createCell(3), lstSysSerial.get(i).getFromSerial(), style);
                    setValueAndStyle(sysSerial.createCell(4), lstSysSerial.get(i).getToSearial(), style);
                    setValueAndStyle(sysSerial.createCell(5), lstSysSerial.get(i).getQuantity(), style);
                    setValueAndStyle(sysSerial.createCell(6), lstSysSerial.get(i).getProductStatus(), style);
                }
            }
            Sheet productNotApproveSheet = workbook.getSheetAt(2);
            if (!DataUtil.isNullOrEmpty(lstProductNotApprove)) {
                for (int i = 0; i < lstProductNotApprove.size(); i++) {
                    Row productNotApproveSheetRow = productNotApproveSheet.createRow(i + STOCK_INSPECT_ROW_START);
                    setValueAndStyle(productNotApproveSheetRow.createCell(0), i + 1, style);
                    setValueAndStyle(productNotApproveSheetRow.createCell(1), lstProductNotApprove.get(i).getProductCode(), style);
                    setValueAndStyle(productNotApproveSheetRow.createCell(2), lstProductNotApprove.get(i).getProductName(), style);
                    setValueAndStyle(productNotApproveSheetRow.createCell(3), lstProductNotApprove.get(i).getQuantity(), style);
                    setValueAndStyle(productNotApproveSheetRow.createCell(4), lstProductNotApprove.get(i).getProductStatus(), style);
                }
            }
            Sheet totalSheet = workbook.getSheetAt(3);
            if (!DataUtil.isNullOrEmpty(lstTotal)) {
                for (int i = 0; i < lstTotal.size(); i++) {
                    Row totalSheetRow = totalSheet.createRow(i + STOCK_INSPECT_ROW_START);
                    setValueAndStyle(totalSheetRow.createCell(0), i + 1, style);
                    setValueAndStyle(totalSheetRow.createCell(1), lstTotal.get(i).getProductCode(), style);
                    setValueAndStyle(totalSheetRow.createCell(2), lstTotal.get(i).getProductName(), style);
                    setValueAndStyle(totalSheetRow.createCell(3), lstTotal.get(i).getProductStatus(), style);
                    setValueAndStyle(totalSheetRow.createCell(4), lstTotal.get(i).getQuantitySystem(), style);
                    setValueAndStyle(totalSheetRow.createCell(5), lstTotal.get(i).getQuantityReal(), style);
                    setValueAndStyle(totalSheetRow.createCell(6), lstTotal.get(i).getDiff(), style);
                    setValueAndStyle(totalSheetRow.createCell(7), lstTotal.get(i).getRequestUser(), style);
                    setValueAndStyle(totalSheetRow.createCell(8), lstTotal.get(i).getApproveUser(), style);
                    setValueAndStyle(totalSheetRow.createCell(9), lstTotal.get(i).getApproveStatusString(), style);
                    setValueAndStyle(totalSheetRow.createCell(10), lstTotal.get(i).getIssueDateTime(), style);
                }
            }

            return workbook;
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
        }
        return null;
    }

    public HSSFWorkbook getContentExportUpdateGpon(List<UpdateSerialGponDTO> lstError, String updateType) {
        try {
            InputStream createStream = null;
            if (!DataUtil.isNullOrEmpty(lstError)) {
                if (DataUtil.safeEqual(updateType, Const.UPDATE_SERIAL_GPON.TYPE_GPON)) {
                    createStream = Faces.getResourceAsStream(Const.BRAS_IPPOOL.RESOURCE_TEMPLATE_PATH + FILE_TEMPLATE_GPON_ERROR);
                } else {
                    createStream = Faces.getResourceAsStream(Const.BRAS_IPPOOL.RESOURCE_TEMPLATE_PATH + FILE_TEMPLATE_MULTI_ERROR);
                }
            }
            HSSFWorkbook workbook = new HSSFWorkbook(createStream);
            HSSFCellStyle style = workbook.createCellStyle();
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            Sheet realSerialSheet = workbook.getSheetAt(0);
            if (!DataUtil.isNullOrEmpty(lstError)) {
                if (DataUtil.safeEqual(updateType, Const.UPDATE_SERIAL_GPON.TYPE_GPON)) {
                    for (int i = 0; i < lstError.size(); i++) {
                        Row realSerial = realSerialSheet.createRow(i + FILE_TEMPLATE_GPON_ROW_START);
                        setValueAndStyle(realSerial.createCell(0), lstError.get(i).getSerial(), style);
                        setValueAndStyle(realSerial.createCell(1), lstError.get(i).getStockGPon(), style);
                        setValueAndStyle(realSerial.createCell(2), lstError.get(i).getStatus(), style);
                    }
                } else {
                    for (int i = 0; i < lstError.size(); i++) {
                        Row realSerial = realSerialSheet.createRow(i + FILE_TEMPLATE_GPON_ROW_START);
                        setValueAndStyle(realSerial.createCell(0), lstError.get(i).getSerial(), style);
                        setValueAndStyle(realSerial.createCell(1), lstError.get(i).getCadId(), style);
                        setValueAndStyle(realSerial.createCell(2), lstError.get(i).getMacId(), style);
                        setValueAndStyle(realSerial.createCell(3), lstError.get(i).getStatus(), style);
                    }
                }
            }
            return workbook;
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
        }
        return null;
    }

    public HSSFWorkbook getContentExportUpdatePincodeCard(List<UpdatePincodeDTO> lstResult) {
        try {
            InputStream createStream = Faces.getResourceAsStream(Const.BRAS_IPPOOL.RESOURCE_TEMPLATE_PATH + FILE_TEMPLATE_PINCODE_RESULT);
            HSSFWorkbook workbook = new HSSFWorkbook(createStream);
            HSSFCellStyle style = workbook.createCellStyle();
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            Sheet realSerialSheet = workbook.getSheetAt(0);
            if (!DataUtil.isNullOrEmpty(lstResult)) {
                for (int i = 0; i < lstResult.size(); i++) {
                    Row realSerial = realSerialSheet.createRow(i + FILE_TEMPLATE_GPON_ROW_START);
                    setValueAndStyle(realSerial.createCell(0), i + 1, style);
                    setValueAndStyle(realSerial.createCell(1), lstResult.get(i).getSerial(), style);
                    setValueAndStyle(realSerial.createCell(2), lstResult.get(i).getPincode(), style);
                    setValueAndStyle(realSerial.createCell(3), lstResult.get(i).getStatus(), style);
                    setValueAndStyle(realSerial.createCell(4), lstResult.get(i).getDesc(), style);
                }
            }
            return workbook;
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
        }
        return null;
    }

    public HSSFWorkbook getContentExportUpdateLicenseKey(List<UpdateLicenseDTO> lstResult) {
        try {
            InputStream createStream = Faces.getResourceAsStream(Const.BRAS_IPPOOL.RESOURCE_TEMPLATE_PATH + FILE_TEMPLATE_LICENSE_RESULT);
            HSSFWorkbook workbook = new HSSFWorkbook(createStream);
            HSSFCellStyle style = workbook.createCellStyle();
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            Sheet realSerialSheet = workbook.getSheetAt(0);
            if (!DataUtil.isNullOrEmpty(lstResult)) {
                for (int i = 0; i < lstResult.size(); i++) {
                    Row realSerial = realSerialSheet.createRow(i + FILE_TEMPLATE_GPON_ROW_START);
                    setValueAndStyle(realSerial.createCell(0), i + 1, style);
                    setValueAndStyle(realSerial.createCell(1), lstResult.get(i).getSerial(), style);
                    setValueAndStyle(realSerial.createCell(2), lstResult.get(i).getLicenseKey(), style);
                    setValueAndStyle(realSerial.createCell(3), lstResult.get(i).getStatus(), style);
                    setValueAndStyle(realSerial.createCell(4), lstResult.get(i).getDesc(), style);
                }
            }
            return workbook;
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
        }
        return null;
    }

    public StockTransVofficeDTO getStockTransVofficeDTO(SignOfficeDTO signOfficeDTO) throws LogicException, Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        stockTransVofficeDTO.setAccountName(signOfficeDTO.getUserName());
        stockTransVofficeDTO.setAccountPass(signOfficeDTO.getPassWord());
        List<SignFlowDetailDTO> signFlowDetailDTOs = signFlowService.findBySignFlowId(signOfficeDTO.getSignFlowId());
        if (DataUtil.isNullOrEmpty(signFlowDetailDTOs)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stockTrans.validate.signFlow.emptyDetail");
        }
        StringBuilder signList = new StringBuilder("");
        for (SignFlowDetailDTO signDetail : signFlowDetailDTOs) {
            if (DataUtil.isNullOrEmpty(signDetail.getEmail())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.signFlow.emptyEmail");
            }
            if (!DataUtil.isNullOrEmpty(signList.toString())) {
                signList.append(",");
            }
            signList.append(signDetail.getEmail().trim().substring(0, signDetail.getEmail().lastIndexOf("@")));

        }
        stockTransVofficeDTO.setSignUserList(signList.toString());
        return stockTransVofficeDTO;
    }

    protected String validateOfficeMulti(StockTransActionDTO actionDTO) {
        String result = "";
        try {
            stockTransVofficeService.doSignedVofficeValidate(actionDTO);
        } catch (LogicException ex) {
            result = ex.getDescription();
            logger.error(ex.getMessage(), ex);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return result;
    }

    /**
     * ham xu ly validate/cap nhap trang thai vOffice
     *
     * @param stockTransActionId
     * @throws LogicException
     * @throws Exception
     * @author thanhnt77
     */
    protected void doSaveStatusOffice(Long stockTransActionId) throws LogicException, Exception {
        StockTransActionDTO stockTransActionDTO = stockTransActionService.findOne(stockTransActionId);
        if (DataUtil.isNullObject(stockTransActionDTO)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.trans.search.validate.validStockTrans");
        }
        if (!DataUtil.safeEqual(Const.SIGN_VOFFICE, stockTransActionDTO.getSignCaStatus())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "voffice.sign.not.flow.office");
        }
        StockTransVofficeDTO stockTransVofficeDTO = stockTransVofficeService.findStockTransVofficeByActionId(stockTransActionId);
        if (stockTransVofficeDTO == null) {//1608100000001182   1608310000001366 1609100000001401
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "logistics.create.bill.lock.voffice.not.found");
        }
        if (Const.STATUS.ACTIVE.equals(stockTransVofficeDTO.getStatus())) {

            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "voffice.sign.status.sign");
        }
        String statusOffice = autoSignVOfficeService.getSignStatus(stockTransVofficeDTO.getStockTransVofficeId());
        if (DataUtil.isNullOrEmpty(statusOffice)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "voffice.status.is.empty");
        }
        if (!Const.SIGN_STATUS_VOFFICE.DA_KI.equals(statusOffice)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "voffice.status.not.sign", getStateNameOffice(statusOffice));
        }
        stockTransVofficeDTO.setStatus(Const.STATUS.ACTIVE);
        stockTransVofficeService.save(stockTransVofficeDTO);
    }

    private String getStateNameOffice(String status) {
        String stateName = "";
        switch (status) {
            case Const.SIGN_STATUS_VOFFICE.CHUA_XU_LY:
                stateName = getText("voffice.sign.status_0");
                break;
            case Const.SIGN_STATUS_VOFFICE.CHO_XU_LY:
                stateName = getText("voffice.sign.status_1");
                break;
            case Const.SIGN_STATUS_VOFFICE.TU_CHOI:
                stateName = getText("voffice.sign.status_2");
                break;
            case Const.SIGN_STATUS_VOFFICE.CHO_KI_CHUNG:
                stateName = getText("voffice.sign.status_3");
                break;
            case Const.SIGN_STATUS_VOFFICE.DA_KI:
                stateName = getText("voffice.sign.status_4");
                break;
            case Const.SIGN_STATUS_VOFFICE.CHO_KI_NHAY:
                stateName = getText("voffice.sign.status_5");
                break;
        }
        return stateName;
    }

    /**
     * check shop vtt
     *
     * @return
     * @author ThanhNT
     */
    public boolean getShopVTT() {
        return BccsLoginSuccessHandler.getStaffDTO() != null && Const.SHOP.SHOP_VTT_ID.equals(BccsLoginSuccessHandler.getStaffDTO().getShopId());
    }

    public boolean isShowStockDebit() {
        return stockDebitDTO != null;
    }


    public String getActionCodeNote() {
        return actionCodeNote;
    }

    public void setActionCodeNote(String actionCodeNote) {
        this.actionCodeNote = actionCodeNote;
    }

    public Boolean getShowAutoOrderNote() {
        return showAutoOrderNote;
    }

    public void setShowAutoOrderNote(Boolean showAutoOrderNote) {
        this.showAutoOrderNote = showAutoOrderNote;
    }

    public StockDebitDTO getStockDebitDTO() {
        return stockDebitDTO;
    }

    public void setStockDebitDTO(StockDebitDTO stockDebitDTO) {
        this.stockDebitDTO = stockDebitDTO;
    }
}
