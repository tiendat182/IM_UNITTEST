package com.viettel.bccs.inventory.controller.limit;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.InventoryController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.DebitDayTypeDTO;
import com.viettel.bccs.inventory.dto.OptionSetValueDTO;
import com.viettel.bccs.inventory.service.DebitDayTypeService;
import com.viettel.bccs.inventory.service.OptionSetValueService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.BundleUtil;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.dto.BaseMessage;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by tuyendv8 on 12/1/2015.
 */
@Component
@Scope("view")
@ManagedBean
public class RangeTimePromotionController extends InventoryController {
    @Autowired
    private DebitDayTypeService debitDayTypeService;
    @Autowired
    OptionSetValueService optionSetValueService;
    private List<OptionSetValueDTO> optionSetValueDTOs;
    private DebitDayTypeDTO forSearchDTO;
    private List<DebitDayTypeDTO> lstDebitDayTypeDTOs;
    private List<DebitDayTypeDTO> lsDebitDayTypeSelect;
    private DebitDayTypeDTO forEditDTO;
    private DebitDayTypeDTO beforeEditDTO;
    private DebitDayTypeDTO rowSelected;
    private UploadedFile fileUpload;
    private String[] fileType = {"doc", "docx", "pdf", "xls", "xlsx", "png", "jpg", "jpeg", "bmp", "gif", "txt"};
    private Date today;
    private boolean renderFileName = false;
    private boolean renderedPnlAdd;

    @PostConstruct
    public void init() {
        try {
            clearCache();
            optionSetValueDTOs = optionSetValueService.getByOptionSetCode(Const.OPTION_SET.DEBIT_DAY_TYPE);
            searchDebitDayType();
            executeCommand("updateScreen()");
        } catch (LogicException le) {
            reportError("", le.getDescription(), le);
        } catch (Exception e) {
            reportError("", "common.error.happened", e);
        }
    }

    public void clearCache() {
        try {
            forSearchDTO = new DebitDayTypeDTO();
            //set ngay bat dau, ngay ket thuc la ngay hien tai
            today = DateUtil.sysDate();
            forSearchDTO.setStartDate(today);
            forSearchDTO.setEndDate(today);
            lstDebitDayTypeDTOs = searchDebitDayType();
            fileUpload = null;
        } catch (Exception e) {
            reportError("", "common.error.happened", e);
        }

    }

    @Secured("@")
    public void resetData() {
        try {
            forSearchDTO = new DebitDayTypeDTO();
            //set ngay bat dau, ngay ket thuc la ngay hien tai
            today = optionSetValueService.getSysdateFromDB(false);
            forSearchDTO.setStartDate(today);
            forSearchDTO.setEndDate(today);
            searchDebitDayType();
        } catch (Exception e) {
            reportError("", "common.error.happened", e);
        }
    }

    public void setDebitDayType() {
        //set debit_day_type
        try {
            List<OptionSetValueDTO> list = optionSetValueService.getByOptionSetCode(Const.OPTION_SET.DEBIT_DAY_TYPE);
            if (!DataUtil.isNullOrEmpty(list) && !DataUtil.isNullOrEmpty(lstDebitDayTypeDTOs)) {
                for (DebitDayTypeDTO dto : lstDebitDayTypeDTOs) {
                    for (OptionSetValueDTO optionSetValueDTO : list) {
                        if (DataUtil.safeEqual(dto.getDebitDayType(), optionSetValueDTO.getValue())) {
                            dto.setDebitDayName(optionSetValueDTO.getName());
                            break;
                        }
                    }
                }
            }

        } catch (Exception e) {
            logger.error(e);
            reportError("", "common.error.happened", e);
        }
    }

    @Secured("@")
    public List<DebitDayTypeDTO> searchDebitDayType() {
        try {
            lstDebitDayTypeDTOs = debitDayTypeService.searchDebitDayType(forSearchDTO);
            if (DataUtil.isNullOrEmpty(lstDebitDayTypeDTOs)) {
                lstDebitDayTypeDTOs = Lists.newArrayList();
            }

            if (!DataUtil.isNullObject(forSearchDTO.getEndDate()) && !DataUtil.isNullObject(forSearchDTO.getStartDate())) {
                if (forSearchDTO.getEndDate().before(forSearchDTO.getStartDate())) {
                    reportError("", ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.trans.from.than.to");
                    topPage();
                    return Lists.newArrayList();
                }
            }

            setDebitDayType();
            for (DebitDayTypeDTO dto : lstDebitDayTypeDTOs) {
                if (!DataUtil.isNullOrEmpty(dto.getFileName())) {
                    String newName = new String(dto.getFileName().getBytes(), "UTF-8");
                    String detailFile = getText("upload.file.file.name") + ": " + newName + ".";
                    dto.setDetailFileUpload(detailFile);
                }
            }
            lsDebitDayTypeSelect = Lists.newArrayList();
            return lstDebitDayTypeDTOs;
        } catch (Exception e) {
            logger.error(e);
            reportError("", "common.error.happened", e);
            topPage();
        }
        topPage();
        lsDebitDayTypeSelect = Lists.newArrayList();
        return lstDebitDayTypeDTOs;
    }

    @Secured("@")
    public String formatDate(Date date) {
        if (DataUtil.isNullObject(date)) {
            return "";
        } else {
            return DateUtil.dateToStringWithPattern(date, "dd/MM/yyyy");
        }
    }

    @Secured("@")
    public void doOpenAddNew() {
        forEditDTO = new DebitDayTypeDTO();
        clearCache();
        renderFileName = false;
        executeCommand("updateScreen()");
    }


    public boolean validateCommon(DebitDayTypeDTO dto) {
        try {
            today = optionSetValueService.getSysdateFromDB(false);
        } catch (Exception e) {
            reportError("", "common.error.happened", e);
        }
        if (parseToSimpleDate(dto.getStartDate()).compareTo((parseToSimpleDate(today))) > 0
                && (DataUtil.isNullObject(dto.getEndDate()))) {
            reportError("", ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.limit.currentdate.startdate");
            return true;
        } else if (parseToSimpleDate(dto.getStartDate()).compareTo((parseToSimpleDate(today))) < 0) {
            reportError("", ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "validate.enddate.current.date");
            return true;
        } else if (parseToSimpleDate(dto.getStartDate()).compareTo(parseToSimpleDate(dto.getEndDate())) > 0) {
            reportError("", ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "validate.start.end.date");
            return true;
        }
        return false;
    }

    @Secured("@")
    public boolean disableLinkEdit(DebitDayTypeDTO dto) {
        try {
            today = optionSetValueService.getSysdateFromDB(false);

        } catch (Exception e) {
            reportError("", "common.error.happened", e);
        }
        if ((dto.getStartDate().compareTo((parseToSimpleDate(today))) > 0
                && (DataUtil.isNullObject(dto.getEndDate()))) || (dto.getEndDate().compareTo((parseToSimpleDate(today))) < 0)
                || (dto.getStartDate().compareTo(dto.getEndDate()) > 0)) {
            return true;
        }
        return false;
    }

    @Secured("@")
    public void validateBeforeUpdate(DebitDayTypeDTO updateDTO) {
        try {
            forEditDTO = DataUtil.cloneBean(updateDTO);
            if (!DataUtil.isNullOrEmpty(forEditDTO.getFileName())) {
                forEditDTO.setDetailFileUpload(forEditDTO.getDetailFileUpload());
            }
        } catch (Exception e) {
            reportError("", "common.error.happened", e);
            return;
        }
    }

    @Secured("@")
    public void updateDebitDayType() {
        try {
            //check ngay sua doi co hop le k
            if (validateCommon(forEditDTO)) return;
            //validate ngay sua da ton tai chua
            if (validateExistRange(forEditDTO)) {
                reportError("", ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "validate.input.for.add.new");
                return;
            }

            if (!DataUtil.isNullObject(fileUpload) && !DataUtil.isNullOrEmpty(fileUpload.getFileName()) && fileUpload.getContents() != null) {
                forEditDTO.setFileName(fileUpload.getFileName());
                forEditDTO.setFileContent(fileUpload.getContents());
            }

            BaseMessage baseMessage = debitDayTypeService.update(forEditDTO, BccsLoginSuccessHandler.getStaffDTO());
            if (!DataUtil.isNullOrEmpty(baseMessage.getKeyMsg())) {
                reportError("", baseMessage.getErrorCode(), baseMessage.getKeyMsg());
                return;
            }
            //update data table
            lstDebitDayTypeDTOs = searchDebitDayType();
            forEditDTO = new DebitDayTypeDTO();
            reportSuccess("", "common.update.success");
            updateElemetId("frmDebitDayType");
        } catch (Exception e) {
            reportError("", "common.error.happened", e);
        }
    }

    @Secured("@")
    public void validateBeforeAdd() {
        try {
            validateData();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    @Secured("@")
    public void doAddAndNoClose() {
        try {
            validateData();
            BaseMessage message = validateFileUploadWhiteList(fileUpload, ALOW_EXTENSION_EXCEL_TYPE_LIST, MAX_SIZE_5M);
            if (!message.isSuccess()) {
                LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                ex.setDescription(message.getDescription());
                throw ex;
            }
            addData();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    private void validateData() throws LogicException, Exception {
        //check ngay bat dau ngay ket thuc
        if (parseToSimpleDate(forEditDTO.getStartDate()).compareTo(parseToSimpleDate(forEditDTO.getEndDate())) > 0) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "validate.start.end.date");
        }
        //check ngay bat dau phai sau ngay hien tai
        if (parseToSimpleDate(optionSetValueService.getSysdateFromDB(true)).compareTo(parseToSimpleDate(forEditDTO.getStartDate())) > 0) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "validate.enddate.current.date");
        }
//        if (DataUtil.isNullOrEmpty(forEditDTO.getFileName()) || forEditDTO.getFileContent() == null) {
//            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "range.time.promotion.dispatch.require");
//        }
    }

    public boolean validateExistRange(DebitDayTypeDTO dtoUpdate) {
        //validate khoang cau hinh them co bi trung khoang cu k
        boolean exist = false;
        try {
            List<DebitDayTypeDTO> lst;
            lst = debitDayTypeService.findAll();
            if (DataUtil.isNullOrEmpty(lst))
                return exist;
            for (DebitDayTypeDTO dto : lst) {
                if (dtoUpdate.getId() != null && dto.getId().compareTo(dtoUpdate.getId()) == 0) {
                    break;
                }
                if (DataUtil.safeEqual(dto.getStatus(), Const.STATUS.ACTIVE) && parseToSimpleDate(dto.getStartDate()).compareTo(parseToSimpleDate(dtoUpdate.getEndDate())) <= 0 &&
                        parseToSimpleDate(dto.getEndDate()).compareTo(parseToSimpleDate(dtoUpdate.getStartDate())) >= 0) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            logger.error(e.getMessage());
            reportError("", "common.error.happened", e);
        }
        return exist;

    }

    @Secured("@")
    public void doAddNew(boolean close) {
        try {
            addData();
            if (close) {
                updateElemetId("frmDebitDayType");
                clearCache();
                searchDebitDayType();
            }
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    private void addData() throws LogicException, Exception {
        //validate khoang cau hinh them co bi trung khoang cu k
        if (validateExistRange(forEditDTO)) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "validate.input.for.add.new");
        }
        if (!DataUtil.isNullObject(forEditDTO)) {
            if (!DataUtil.isNullObject(fileUpload) && !DataUtil.isNullOrEmpty(fileUpload.getFileName()) && fileUpload.getContents() != null) {
                forEditDTO.setFileName(fileUpload.getFileName());
                forEditDTO.setFileContent(fileUpload.getContents());
            }
            BaseMessage baseMessage = debitDayTypeService.create(forEditDTO, BccsLoginSuccessHandler.getStaffDTO());
            if (!DataUtil.isNullOrEmpty(baseMessage.getKeyMsg())) {
                throw new LogicException(baseMessage.getErrorCode(), baseMessage.getKeyMsg());
            }
            lstDebitDayTypeDTOs = searchDebitDayType();
            forEditDTO = new DebitDayTypeDTO();
            reportSuccess("", "common.add.success");
        }
    }

    @Secured("@")
    public void validateBeforeDelete(DebitDayTypeDTO debitDayTypeDTO) {
        rowSelected = DataUtil.cloneBean(debitDayTypeDTO);
    }


    @Secured("@")
    public void delete() {
        try {
            lsDebitDayTypeSelect.add(rowSelected);
            doDeleteListFinance();
        } catch (Exception e) {
            logger.error(e.getMessage());
            reportError("", "common.error.happened", e);
            topPage();
        }
    }

    public void doConfirmListDelete() {
        if (DataUtil.isNullOrEmpty(lsDebitDayTypeSelect)) {
            reportError("", ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "common.error.noselect.record");
            topPage();
        }
        try {
            today = optionSetValueService.getSysdateFromDB(false);

        } catch (Exception e) {
            reportError("", "common.error.happened", e);
        }
        for (DebitDayTypeDTO dto : lsDebitDayTypeSelect) {
            if (dto.getStartDate().compareTo((parseToSimpleDate(today))) > 0
                    && (DataUtil.isNullObject(dto.getEndDate()))) {

            }
            if (dto.getEndDate().compareTo((parseToSimpleDate(today))) < 0) {
                reportError("", ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "validate.before.delete.enddate.current");
                topPage();
                return;
            }
            if (dto.getStartDate().compareTo(dto.getEndDate()) > 0) {
                reportError("", ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "validate.before.delete.enddate.startdate");
                topPage();
                return;
            }
        }

    }

    @Secured("@")
    public void doDeleteListFinance() {
        try {
            for (DebitDayTypeDTO dto : lsDebitDayTypeSelect) {
                if (disableLinkEdit(dto)) {
                    reportError("", ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "validate.before.delete");
                    topPage();
                    return;
                }
            }
            if (DataUtil.isNullOrEmpty(lsDebitDayTypeSelect)) {
                reportError("", ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "common.error.noselect.record");
            }
            for (DebitDayTypeDTO dto : lsDebitDayTypeSelect) {
                dto.setStatus(Const.STATUS.NO_ACTIVE);
                dto.setLastUpdateUser(BccsLoginSuccessHandler.getUserName());
            }
            BaseMessage baseMessage = debitDayTypeService.delete(lsDebitDayTypeSelect, BccsLoginSuccessHandler.getStaffDTO());
            if (!DataUtil.isNullOrEmpty(baseMessage.getKeyMsg())) {
                reportError("", baseMessage.getErrorCode(), baseMessage.getKeyMsg());
                topPage();
                return;
            }
            lsDebitDayTypeSelect = Lists.newArrayList();
            lstDebitDayTypeDTOs = searchDebitDayType();
            reportSuccess("", "common.delete.success");
            topPage();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getKeyMsg());
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    public Date parseToSimpleDate(Date date) {
        String pattern = "dd/MM/yyyy";
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date dateFormat = new Date();
        try {
            dateFormat = format.parse(format.format(date));
        } catch (ParseException e) {
            e.getMessage();
        }
        return dateFormat;
    }

    @Secured("@")
    public String getTitleDlgDebit() {
        if (forEditDTO != null && forEditDTO.getId() == null) {
            return getText("range.time.promotion.addnew.title.header");
        }
        return getText("range.time.promotion.edit.title.header");
    }

    @Secured("@")
    public StreamedContent downloadFileAttach(DebitDayTypeDTO dto) {
        try {
            byte[] content = debitDayTypeService.getAttachFileContent(dto.getId());
            InputStream is = new ByteArrayInputStream(content);
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            StreamedContent streamedContent = new DefaultStreamedContent(is, externalContext.getMimeType(dto.getFileName()), dto.getFileName());
            return streamedContent;
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
        }
        return null;
    }

    @Secured("@")
    public void handleFileUpload(FileUploadEvent event) {
        try {
            renderFileName = false;
            fileUpload = event.getFile();
            BaseMessage message = validateFileUploadWhiteList(fileUpload, ALOW_EXTENSION_ALL_TYPE_LIST, MAX_SIZE_5M);
            if (!message.isSuccess()) {
                LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                ex.setDescription(message.getDescription());
                throw ex;
            }

            String newName = new String(event.getFile().getFileName().getBytes(), "UTF-8");
            String detailFileUpload = getText("upload.file.file.name") + ": " + newName + ". " + getText("upload.file.capacity")
                    + ": " + event.getFile().getSize() / 1024 + getText("common.text.size.kb");
            forEditDTO.setDetailFileUpload(detailFileUpload);
            forEditDTO.setFileName(fileUpload.getFileName());
            forEditDTO.setFileContent(fileUpload.getContents());
            renderFileName = true;
        } catch (LogicException le) {
            reportError("", le);
        } catch (Exception ex) {
            reportError("", "common.upload.file.error", ex);
        }
    }

    public void validatePreImport(UploadedFile file) throws IOException, Exception {
        if (DataUtil.isNullObject(file)) {
            BundleUtil.addFacesMsg(FacesMessage.SEVERITY_FATAL, "", "common.msg.upload", "common.msg.fail", "");
        }
        String fileExtension = StringUtils.substringAfterLast(file.getFileName(), ".");
        if (!DataUtil.isNullOrEmpty(fileExtension)) {
            if (!isCorrectExtension(fileExtension)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "validate.format.file.dont.accept");
            }
            if (file.getFileName().length() > Const.MAX_LENGTH_FILE_UPLOAD) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "validate.max.length.file");
            }

        } else {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "common.error.happened");
        }

        if (file.getSize() > Const.FILE_MAX) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "msg.error.file.size");
        }
    }


    /**
     * kiem tra dinh dang file
     *
     * @param extension
     * @author
     */
    public boolean isCorrectExtension(String extension) {
        boolean correct = false;
        for (int i = 0; i < fileType.length; i++) {
            if (DataUtil.safeEqual(fileType[i], extension)) {
                correct = true;
                break;
            }
        }
        return correct;
    }

    public List<OptionSetValueDTO> getOptionSetValueDTOs() {
        return optionSetValueDTOs;
    }

    public void setOptionSetValueDTOs(List<OptionSetValueDTO> optionSetValueDTOs) {
        this.optionSetValueDTOs = optionSetValueDTOs;
    }

    public DebitDayTypeDTO getForSearchDTO() {
        return forSearchDTO;
    }

    public void setForSearchDTO(DebitDayTypeDTO forSearchDTO) {
        this.forSearchDTO = forSearchDTO;
    }

    public List<DebitDayTypeDTO> getLstDebitDayTypeDTOs() {
        return lstDebitDayTypeDTOs;
    }

    public void setLstDebitDayTypeDTOs(List<DebitDayTypeDTO> lstDebitDayTypeDTOs) {
        this.lstDebitDayTypeDTOs = lstDebitDayTypeDTOs;
    }

    public DebitDayTypeDTO getForEditDTO() {
        return forEditDTO;
    }

    public void setForEditDTO(DebitDayTypeDTO forEditDTO) {
        this.forEditDTO = forEditDTO;
    }

    public DebitDayTypeDTO getRowSelected() {
        return rowSelected;
    }

    public void setRowSelected(DebitDayTypeDTO rowSelected) {
        this.rowSelected = rowSelected;
    }

    public UploadedFile getFileUpload() {
        return fileUpload;
    }

    public void setFileUpload(UploadedFile fileUpload) {
        this.fileUpload = fileUpload;
    }

    public DebitDayTypeDTO getBeforeEditDTO() {
        return beforeEditDTO;
    }

    public void setBeforeEditDTO(DebitDayTypeDTO beforeEditDTO) {
        this.beforeEditDTO = beforeEditDTO;
    }

    public List<DebitDayTypeDTO> getLsDebitDayTypeSelect() {
        return lsDebitDayTypeSelect;
    }

    public void setLsDebitDayTypeSelect(List<DebitDayTypeDTO> lsDebitDayTypeSelect) {
        this.lsDebitDayTypeSelect = lsDebitDayTypeSelect;
    }

    public boolean isRenderFileName() {
        return renderFileName;
    }

    public void setRenderFileName(boolean renderFileName) {
        this.renderFileName = renderFileName;
    }

    public boolean isRenderedPnlAdd() {
        return renderedPnlAdd;
    }

    public void setRenderedPnlAdd(boolean renderedPnlAdd) {
        this.renderedPnlAdd = renderedPnlAdd;
    }
}
