package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.ExcellUtil;
import com.viettel.bccs.inventory.common.model.FileExportBean;
import com.viettel.bccs.inventory.common.util.FileUtil;
import com.viettel.bccs.inventory.dto.DivideSerialActionDTO;
import com.viettel.bccs.inventory.dto.StockTransFullDTO;
import com.viettel.bccs.inventory.dto.StockTransSerialDTO;
import com.viettel.bccs.inventory.service.DivideSerialActionService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.dto.BaseMessage;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.util.List;

/**
 * @author luannt23.
 * @comment
 * @date 3/9/2016.
 */

@Component
@Scope("view")
@ManagedBean
public class ShareSerialExportFileController extends TransCommonController {

    /**
     * 1 chon 1 ban ghi de chia serial
     * 2 chon 1 ban ghi de xuat excel
     * 3 chon tat ca de chia serial
     * 4 chon tat ca de xuat excel
     * 5 xuat file ket qua chia serial
     * 6 xua file serial da chia
     */
    private enum Mode {
        Divide(1), Export(2),
        AllDivide(3), AllExport(4),
        ExportResult(5), ExportDetail(6);
        int value;

        Mode(int val) {
            this.value = val;
        }

        public int getValue() {
            return value;
        }
    }

    @Autowired
    private DivideSerialActionService divideSerialActionService;
    private DivideSerialActionDTO divideSerialActionDTO;
    private List<StockTransFullDTO> listStockTransFullDTO;
    private List<StockTransFullDTO> listStockTransFullDTO4Export;
    private List<StockTransFullDTO> listStockTransFullDTO4Divide;
    private DivideSerialActionDTO divideSerialActionDTOForUpdate;
    private List<DivideSerialActionDTO> listDivideSerialActionDTOError;
    private boolean all4Divide;
    private boolean all4Export;
    private boolean hasDivide;
    private boolean hasExport;
    private boolean hasSerial;
    private boolean hasError;
    private boolean hasSuccess;


    @PostConstruct
    public void prepare() {
        try {
            divideSerialActionDTO = new DivideSerialActionDTO();
            divideSerialActionDTO.setFromDate(optionSetValueService.getSysdateFromDB(true));
            divideSerialActionDTO.setToDate(optionSetValueService.getSysdateFromDB(true));
            executeCommand("updateControls");
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    @Secured("@")
    public void doSearchDivideSerialAction() {
        try {
            validateDate(divideSerialActionDTO.getFromDate(), divideSerialActionDTO.getToDate());
            listStockTransFullDTO = DataUtil.defaultIfNull(divideSerialActionService.searchStockTrans(divideSerialActionDTO), Lists.newArrayList());
            all4Divide = false;
            all4Export = false;
            hasDivide = false;
            hasExport = false;
            hasSerial = false;
            hasError = false;
            listStockTransFullDTO4Divide = Lists.newArrayList();
            listStockTransFullDTO4Export = Lists.newArrayList();
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    @Secured("@")
    public void doChooseStockTrans(StockTransFullDTO stockTransFullDTO, int mode) {
        hasSuccess = false;
        if (stockTransFullDTO == null) {
            if (Mode.AllDivide.value == mode) {
                if (all4Divide == true) {
                    for (StockTransFullDTO stockTransFull : listStockTransFullDTO) {
                        if (Const.ConfigIDCheck.nhung_trang_thai_duoc_phep_divide.validate(stockTransFull.getSerialStatus())) {
                            stockTransFull.setChoose4Divide(true);
                            listStockTransFullDTO4Divide.add(stockTransFull);
                        }
                    }
                } else {
                    for (StockTransFullDTO changeDTO : listStockTransFullDTO) {
                        changeDTO.setChoose4Divide(false);
                    }
                    listStockTransFullDTO4Divide = Lists.newArrayList();
                }
            } else {
                if (all4Export) {
                    for (StockTransFullDTO stockTransFull : listStockTransFullDTO) {
                        if (Const.ConfigIDCheck.nhung_trang_thai_duoc_phep_exoport.validate(stockTransFull.getSerialStatus())) {
                            stockTransFull.setChoose4Export(true);
                            listStockTransFullDTO4Export.add(stockTransFull);
                        }
                    }
                } else {
                    for (StockTransFullDTO changeDTO : listStockTransFullDTO) {
                        changeDTO.setChoose4Export(false);
                    }
                    listStockTransFullDTO4Export = Lists.newArrayList();
                }
            }
        } else {
            if (Mode.Divide.value == mode) {
                if (listStockTransFullDTO4Divide.contains(stockTransFullDTO)) {
                    stockTransFullDTO.setChoose4Divide(false);
                    listStockTransFullDTO4Divide.remove(stockTransFullDTO);
                    all4Divide = false;
                } else {
                    if (Const.ConfigIDCheck.nhung_trang_thai_duoc_phep_divide.validate(stockTransFullDTO.getSerialStatus())) {
                        stockTransFullDTO.setChoose4Divide(true);
                        listStockTransFullDTO4Divide.add(stockTransFullDTO);
                    }
                }
            } else {
                if (listStockTransFullDTO4Export.contains(stockTransFullDTO)) {
                    stockTransFullDTO.setChoose4Export(false);
                    listStockTransFullDTO4Export.remove(stockTransFullDTO);
                    all4Export = false;
                } else {
                    if (Const.ConfigIDCheck.nhung_trang_thai_duoc_phep_exoport.validate(stockTransFullDTO.getSerialStatus())) {
                        stockTransFullDTO.setChoose4Export(true);
                        listStockTransFullDTO4Divide.add(stockTransFullDTO);
                    }
                }
            }
        }
        hasDivide = !listStockTransFullDTO4Divide.isEmpty();
        hasExport = !listStockTransFullDTO4Export.isEmpty();
        executeCommand("updateTable");
    }

    @Secured("@")
    public void handleFileUpload(FileUploadEvent event) {
        if (event != null) {
            try {
                listDivideSerialActionDTOError = Lists.newArrayList();
                divideSerialActionDTOForUpdate = new DivideSerialActionDTO();
                hasError = false;
                BaseMessage message = validateFileUploadWhiteList(event.getFile(), ALOW_EXTENSION_EXCEL_TYPE_LIST, MAX_SIZE_5M);
                if (!message.isSuccess()) {
                    LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                    ex.setDescription(message.getDescription());
                    throw ex;
                }
                attackFile(event.getFile());
            } catch (LogicException ex) {
                topReportError("", ex);
            } catch (Exception ex) {
                topReportError("", "common.error.happened", ex);
            }

        }
    }

    private void attackFile(UploadedFile file) throws LogicException, Exception {
        ExcellUtil excellUtil = new ExcellUtil(file, file.getContents());
        Sheet sheetProcess = excellUtil.getSheetAt(0);
        //sub folder.
        List<StockTransSerialDTO> listSerial = Lists.newArrayList();
        List<String> listStockImp = Lists.newArrayList();
        divideSerialActionDTOForUpdate.setLstStockImpInFile(listStockImp);
        divideSerialActionDTOForUpdate.setLstStockTransSerial(listSerial);

        for (Row row : sheetProcess) {
            if (row == null) continue;
            DivideSerialActionDTO check = new DivideSerialActionDTO();
            String from = excellUtil.getStringValue(row.getCell(0));
            String to = excellUtil.getStringValue(row.getCell(1));
            String unit = excellUtil.getStringValue(row.getCell(2));
            String regex = Const.REGEX.SERIAL_REGEX;
            if (!DataUtil.isNullOrEmpty(unit)) {
                listStockImp.add(unit);
            }
            check.setFromSerial(from);
            check.setToSerial(to);
            check.setOwnerCode(unit);
            if (DataUtil.isNullOrEmpty(from) && DataUtil.isNullOrEmpty(to)) {
                continue;
            }
            if (DataUtil.isNullOrEmpty(from) || DataUtil.isNullOrEmpty(to)) {
                check.setSerialContent(getText("mm.divide.upload.valid.serial.none"));
                hasError = true;
            } else if (from.length() > 19 || to.length() > 19
                    || !from.matches(regex)
                    || !to.matches(regex)) {
                check.setSerialContent(getText("mm.divide.upload.valid.serial.length"));
                hasError = true;
            }
            listDivideSerialActionDTOError.add(check);
            if (!hasError) {
                StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
                stockTransSerialDTO.setFromSerial(from.trim());
                stockTransSerialDTO.setToSerial(to.trim());
                listSerial.add(stockTransSerialDTO);
            }
        }
        if (!hasError) {
            if (DataUtil.isNullOrEmpty(listSerial)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mm.divide.upload.valid.listSerial");
            }
            if (DataUtil.isNullOrEmpty(listStockImp)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mm.divide.upload.valid.listImp");
            }
            hasSerial = true;
        } else {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mm.divide.upload.valid.noData");
        }
    }

    @Secured("@")
    public void doValidate() {
        try {
            if (DataUtil.isNullOrEmpty(divideSerialActionDTOForUpdate.getLstStockTransSerial())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "mm.divide.upload.valid.listSerial");
            }
            if (DataUtil.isNullOrEmpty(divideSerialActionDTOForUpdate.getLstStockImpInFile())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "mm.divide.upload.valid.listImp");
            }
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    @Secured("@")
    public void doDivideSerial() {
        try {
            divideSerialActionDTOForUpdate.setLstStockTransActionId(Lists.newArrayList());
            divideSerialActionDTOForUpdate.setFromDate(divideSerialActionDTO.getFromDate());
            divideSerialActionDTOForUpdate.setToDate(divideSerialActionDTO.getToDate());
            divideSerialActionDTOForUpdate.setCreateUser(getStaffDTO().getStaffCode());
            for (StockTransFullDTO stockTransFullDTO : listStockTransFullDTO4Divide) {
                divideSerialActionDTOForUpdate.getLstStockTransActionId().add(stockTransFullDTO.getActionId());
            }
            divideSerialActionService.divideSerial(divideSerialActionDTOForUpdate);
            hasSuccess = true;
            topReportSuccess("", "mm.divide.upload.result.success");
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    @Secured("@")
    public StreamedContent doExportSerial(int mode) {
        try {
            DivideSerialActionDTO searchDTO = new DivideSerialActionDTO();
            searchDTO.setFromDate(divideSerialActionDTO.getFromDate());
            searchDTO.setToDate(divideSerialActionDTO.getToDate());
            searchDTO.setLstStockTransActionId(Lists.newArrayList());
            if (Mode.ExportResult.getValue() == mode) {
                for (StockTransFullDTO stockTransFullDTO : listStockTransFullDTO4Divide) {
                    searchDTO.getLstStockTransActionId().add(stockTransFullDTO.getActionId());
                }
            } else {
                for (StockTransFullDTO stockTransFullDTO : listStockTransFullDTO4Export) {
                    searchDTO.getLstStockTransActionId().add(stockTransFullDTO.getActionId());
                }
            }
            List<StockTransFullDTO> listSerial = divideSerialActionService.getListSerial(searchDTO);
            if (DataUtil.isNullOrEmpty(listSerial)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.invoice.invoiceType.nodata");
            }
            FileExportBean bean = new FileExportBean();
            bean.putValue("lstStockTransFull", listSerial);
            bean.setTemplateName(Const.REPORT_TEMPLATE.DIVIDE_SERIAL_RESULT);
            bean.setOutName("danh_sach_chia_dai_serial.xls");
            return FileUtil.exportToStreamed(bean);
//            FileUtil.exportFile(bean);
//            return bean.getExportFileContent();
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
        return null;
    }

    @Secured("@")
    public StreamedContent doExportError() {
        try {
            FileExportBean bean = new FileExportBean();
            bean.setTemplateName(Const.REPORT_TEMPLATE.DIVIDE_SERIAL_ERROR);
            bean.setOutName("file_import_loi.xls");
            bean.putValue("listData", listDivideSerialActionDTOError);
            bean.setOutPath(FileUtil.getOutputPath());
            return FileUtil.exportToStreamed(bean);
//            FileUtil.exportFile(bean);
//            return bean.getExportFileContent();
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
        return null;
    }

    @Secured("@")
    public StreamedContent doExportTemplate() throws Exception {
        return FileUtil.getTemplateFileContent(Const.REPORT_TEMPLATE.DIVIDE_SERIAL);
    }

    public DivideSerialActionDTO getDivideSerialActionDTO() {
        return divideSerialActionDTO;
    }

    public void setDivideSerialActionDTO(DivideSerialActionDTO divideSerialActionDTO) {
        this.divideSerialActionDTO = divideSerialActionDTO;
    }

    public List<StockTransFullDTO> getListStockTransFullDTO() {
        return listStockTransFullDTO;
    }

    public void setListStockTransFullDTO(List<StockTransFullDTO> listStockTransFullDTO) {
        this.listStockTransFullDTO = listStockTransFullDTO;
    }

    public boolean isAll4Divide() {
        return all4Divide;
    }

    public void setAll4Divide(boolean all4Divide) {
        this.all4Divide = all4Divide;
    }

    public boolean isAll4Export() {
        return all4Export;
    }

    public void setAll4Export(boolean all4Export) {
        this.all4Export = all4Export;
    }

    public boolean isHasDivide() {
        return hasDivide;
    }

    public void setHasDivide(boolean hasDivide) {
        this.hasDivide = hasDivide;
    }

    public boolean isHasExport() {
        return hasExport;
    }

    public void setHasExport(boolean hasExport) {
        this.hasExport = hasExport;
    }

    public List<StockTransFullDTO> getListStockTransFullDTO4Divide() {
        return listStockTransFullDTO4Divide;
    }

    public void setListStockTransFullDTO4Divide(List<StockTransFullDTO> listStockTransFullDTO4Divide) {
        this.listStockTransFullDTO4Divide = listStockTransFullDTO4Divide;
    }

    public List<StockTransFullDTO> getListStockTransFullDTO4Export() {
        return listStockTransFullDTO4Export;
    }

    public void setListStockTransFullDTO4Export(List<StockTransFullDTO> listStockTransFullDTO4Export) {
        this.listStockTransFullDTO4Export = listStockTransFullDTO4Export;
    }

    public boolean isHasSerial() {
        return hasSerial;
    }

    public void setHasSerial(boolean hasSerial) {
        this.hasSerial = hasSerial;
    }

    public boolean isHasError() {
        return hasError;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }

    public DivideSerialActionDTO getDivideSerialActionDTOForUpdate() {
        return divideSerialActionDTOForUpdate;
    }

    public void setDivideSerialActionDTOForUpdate(DivideSerialActionDTO divideSerialActionDTOForUpdate) {
        this.divideSerialActionDTOForUpdate = divideSerialActionDTOForUpdate;
    }

    public List<DivideSerialActionDTO> getListDivideSerialActionDTOError() {
        return listDivideSerialActionDTOError;
    }

    public void setListDivideSerialActionDTOError(List<DivideSerialActionDTO> listDivideSerialActionDTOError) {
        this.listDivideSerialActionDTOError = listDivideSerialActionDTOError;
    }

    public boolean isHasSuccess() {
        return hasSuccess;
    }

    public void setHasSuccess(boolean hasSuccess) {
        this.hasSuccess = hasSuccess;
    }
}
