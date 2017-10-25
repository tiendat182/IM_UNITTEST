package com.viettel.bccs.inventory.controller.infrastructure;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.InventoryController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.ValidateService;
import com.viettel.bccs.inventory.common.model.FileExportBean;
import com.viettel.bccs.inventory.common.util.FileUtil;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.*;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.dto.BaseMessage;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.omnifaces.util.Faces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by anhvv4 on 23/11/2015.
 */
@Component
@Scope("view")
@ManagedBean
public class ManageBrasIpPoolController extends InventoryController {

    private final static String WORK_MODE_ADD = "0";
    private final static String WORK_MODE_EDIT = "1";
    private final static String WORK_MODE_SEARCH = "2";

    @Autowired
    private AreaService areaSv;
    @Autowired
    private OptionSetValueService optionSetValueSv;
    @Autowired
    private BrasIppoolService brasIppoolSv;
    @Autowired
    private BrasService brasSv;
    @Autowired
    private DomainService domainSv;

    private BrasIppoolDTO brasIppoolSearch;
    private BrasIppoolDTO brasIppoolEdit = new BrasIppoolDTO();
    private List<BrasIppoolDTO> listSearchBrasIppool;
    private List<BrasIppoolDTO> listSearchBrasIppoolExport;
    private List<BrasIppoolDTO> listSearchBrasIppoolSelecttion;
    private int index; // luu vi tri doi tuong can xoa
    private List<BrasIppoolDTO> listAddBrasIppool;
    private List<BrasIppoolDTO> listImportBrasIppool = Lists.newArrayList();
    private List<BrasIppoolDTO> listImportBrasIppoolAdd = Lists.newArrayList();
    private List<BrasIppoolDTO> listImportBrasIppoolError = Lists.newArrayList();
    private List<BrasIppoolDTO> listImportBrasIppoolEdit = Lists.newArrayList();
    private List<DomainDTO> listDomain;
    private List<DomainDTO> filterDomain;
    private DomainDTO currentDomain;
    private DomainDTO currentDomainEdit;
    private BrasDTO currentBrasEdit;
    private BrasDTO currentBras;
    private AreaDTO curentAreaEdit;
    private AreaDTO curentArea;
    private List<BrasDTO> listBras;
    private List<BrasDTO> filterBras;
    private List<OptionSetValueDTO> listBrasStatus;
    private List<OptionSetValueDTO> listIpSpecific;
    private List<AreaDTO> listArea;
    private List<AreaDTO> filterListArea;
    private List<AreaDTO> listAreaEdit;
    private List<AreaDTO> listAreaSearch;

    private String trackInput;
    private String workMode;

    private boolean addfile;
    private boolean renderAddFile;
    private boolean workModeAdd;
    private boolean workModeEdit;
    private BrasIppoolDTO currentBrasIppool = new BrasIppoolDTO();
    private boolean render;
    private boolean notRender;
    private String titleAddOrCoppy;
    private String buttonAddOrCoppy;
    private String attachFileName = "";
    private boolean showAttachFile = false;
    private InputStream file;
    private Workbook workbook;
    private UploadedFile fileUpload;
    private boolean enableExprotFileError = true;
    private String titleHeaderByFile;
    private boolean showDeleteByFile = false;
    private boolean showEditByFile = false;
    private String buttonFile;
    private String headerConfirm;
    private String messageConfirm;
    private StaffDTO currentStaff;
    private boolean showArea;
    private boolean showAreaEdit;
    private List<String> strListArea = Lists.newArrayList();
    private List<String> strListAreaEdit = Lists.newArrayList();
    private boolean viewIp = false;

    @PostConstruct
    @Secured("@")
    public void init() {
        try {
            create();
            executeCommand("updateControls();");

        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmManageBrasIpPool:msgbrasIppool", ex.getErrorCode(), ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex);
            reportError("frmManageBrasIpPool:msgbrasIppool", "", "common.error.happen");
        }
        topPage();
    }

    private void create() throws Exception, LogicException {
        currentStaff = BccsLoginSuccessHandler.getStaffDTO();
        brasIppoolSearch = new BrasIppoolDTO(false);
        workMode = WORK_MODE_SEARCH;
        trackInput = "0";
        render = false;
        notRender = true;
        workModeEdit = false;
        addfile = false;
        renderAddFile = false;
        showAttachFile = false;
        enableExprotFileError = true;
        titleAddOrCoppy = "brasIpPool.add.bras.ippool";
        buttonAddOrCoppy = "common.add";
        listBrasStatus = optionSetValueSv.getByOptionSetCode("BRAS_IPPOOL_STATUS");
        listIpSpecific = optionSetValueSv.getByOptionSetCode("BRAS_IPPOOL_TYPE_SPECIFIC");
        listBras = brasSv.findAllAction();
        listDomain = domainSv.findAllAction();
        listArea = areaSv.getAllProvince();
        listAreaSearch = Lists.newArrayList();
        listSearchBrasIppoolSelecttion = Lists.newArrayList();
        strListArea.clear();
        strListAreaEdit.clear();
        listSearchBrasIppool = brasIppoolSv.search(brasIppoolSearch);
        topPage();
    }

    @Secured("@")
    public void removeArea(AreaDTO area, String workMode) {
        if (WORK_MODE_SEARCH.equals(workMode)) {
            strListArea.remove(area.getAreaCode());
            if (!DataUtil.isNullOrEmpty(listAreaSearch)) {
                listAreaSearch.remove(area);
            }
        } else {
            strListAreaEdit.remove(area.getAreaCode());
            if (!DataUtil.isNullOrEmpty(listAreaEdit)) {
                listAreaEdit.remove(area);
            }
        }

    }

    @Secured("@")
    public List<BrasDTO> searchListBras(String input) {
        filterBras = listBras;
        CharSequence inputData = DataUtil.isNullOrEmpty(input) ? null : input.trim().toUpperCase();
        if (inputData == null) {
            return filterBras;
        }
        return filterBras.stream().filter(obj -> DataUtil.safeToString(obj.getCode()).toUpperCase().contains(inputData)
                || DataUtil.safeToString(obj.getName()).toUpperCase().contains(inputData)).collect(Collectors.toList());
    }

    /*
    Tuydv1: reset autoCcmplate
     */
    @Secured("@")
    public void resetListBras() {
        filterBras = Lists.newArrayList();
        brasIppoolSearch.setBrasIp(null);
        currentBras = null;
    }

    @Secured("@")
    public void showDetailArea(String workMode, boolean flag) {
        if (WORK_MODE_SEARCH.equals(workMode)) {
            if (flag) {
                showArea = true;
            }
            listAreaSearch = Lists.newArrayList();
            for (String areaCode : strListArea) {
                AreaDTO dto = new AreaDTO();
                for (AreaDTO dtoTmp : listArea) {
                    if (DataUtil.safeEqual(dtoTmp.getAreaCode().trim(), areaCode.trim())) {
                        dto = DataUtil.cloneBean(dtoTmp);
                        break;
                    }
                }
                if (!DataUtil.isNullObject(dto)) {
                    listAreaSearch.add(dto);
                }
            }
        } else {
            if (flag) {
                showAreaEdit = true;
            }
            listAreaEdit = Lists.newArrayList();
            for (String areaCode : strListAreaEdit) {
                AreaDTO dto = new AreaDTO();
                for (AreaDTO dtoTmp : listArea) {
                    if (DataUtil.safeEqual(dtoTmp.getAreaCode().trim(), areaCode.trim())) {
                        dto = DataUtil.cloneBean(dtoTmp);
                        break;
                    }
                }
                if (!DataUtil.isNullObject(dto)) {
                    listAreaEdit.add(dto);
                }
            }
        }

    }

    @Secured("@")
    public void hideDetailArea(String workMode) {
        if (WORK_MODE_SEARCH.equals(workMode)) {
            showArea = false;
        } else {
            showAreaEdit = false;
        }
    }

    //////////////////////////////////////bo autocomplate////////////////////////////////////
    @Secured("@")
    public List<AreaDTO> searchListArea(String input) {
        filterListArea = listArea;
        CharSequence inputData = DataUtil.isNullOrEmpty(input) ? null : input.trim().toUpperCase();
        if (inputData == null) {
            return filterListArea;
        }
        return filterListArea.stream().filter(obj -> DataUtil.safeToString(obj.getAreaCode()).toUpperCase().contains(inputData)
                || DataUtil.safeToString(obj.getName()).toUpperCase().contains(inputData)).collect(Collectors.toList());
    }

    @Secured("@")
    public void selectAreaSearch(SelectEvent event) {
        try {
            String areaCode = ((AreaDTO) event.getObject()).getAreaCode();
            AreaDTO curentAreaDTO = areaSv.findByCode(areaCode);
            checkExtsArea(curentAreaDTO, listAreaSearch);
            listAreaSearch.add(curentAreaDTO);
            curentArea = null;

        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmManageBrasIpPool:msgbrasIppool", ex.getErrorCode(), ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex);
            reportError("frmManageBrasIpPool:msgbrasIppool", "", "common.error.happen");
        }
    }

    /*
   Tuydv1: reset autoCcmplate
    */
    @Secured("@")
    public void resetListArea() {
        filterListArea = Lists.newArrayList();
        listAreaSearch = Lists.newArrayList();
        if (brasIppoolSearch != null) {
            brasIppoolSearch.setProvince("");
        }
        curentArea = null;

    }

    private void checkExtsArea(AreaDTO area, List<AreaDTO> list) throws Exception {
        if (!DataUtil.isNullOrEmpty(list)) {
            for (AreaDTO areaDTO : list) {
                if (DataUtil.safeEqual(area.getAreaCode(), areaDTO.getAreaCode())) {
                    throw new LogicException("", "brasIpPool.privince.exits.list");
                }
            }
        }
    }

    @Secured("@")
    public void selectAreaEdit(SelectEvent event) {
        try {
            String areaCode = ((AreaDTO) event.getObject()).getAreaCode();
            AreaDTO curentAreaDTO = areaSv.findByCode(areaCode);
            if (listAreaEdit.size() >= 20) {
                reportError("", "", "brasIpPool.privince.max20");
                topPage();
            } else {
                checkExtsArea(curentAreaDTO, listAreaEdit);
                listAreaEdit.add(curentAreaDTO);
                curentAreaEdit = null;
            }

        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmManageBrasIpPool:msgbrasIppool", ex.getErrorCode(), ex.getDescription());
            topPage();
        } catch (Exception ex) {
            logger.error(ex);
            reportError("frmManageBrasIpPool:msgbrasIppool", "", "common.error.happen");
            topPage();
        }
    }

    /*
  Tuydv1: reset autoCcmplate
   */
    @Secured("@")
    public void resetListAreaEdit() {
        filterListArea = Lists.newArrayList();
        if (brasIppoolSearch != null) {
            brasIppoolSearch.setProvince("");
        }
        curentAreaEdit = null;
        listAreaEdit = Lists.newArrayList();
        strListAreaEdit.clear();
    }

    //////////////////////////////////////bo autocomplate////////////////////////////////////

    @Secured("@")
    public void selectBrasSearch(SelectEvent event) {
        try {
            Long brasId = ((BrasDTO) event.getObject()).getBrasId();
            currentBras = brasSv.findOne(brasId);
            brasIppoolSearch.setBrasIp(currentBras.getBrasId());
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmManageBrasIpPool:msgbrasIppool", ex.getErrorCode(), ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex);
            reportError("frmManageBrasIpPool:msgbrasIppool", "", "common.error.happen");
        }
    }

    @Secured("@")
    public void selectBrasEdit(SelectEvent event) {
        try {
            Long brasId = ((BrasDTO) event.getObject()).getBrasId();
            currentBrasEdit = brasSv.findOne(brasId);
            brasIppoolEdit.setBrasIp(currentBrasEdit.getBrasId());
            brasIppoolEdit.setBrasId(currentBrasEdit.getBrasId());
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmManageBrasIpPool:msgbrasIppool", ex.getErrorCode(), ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex);
            reportError("frmManageBrasIpPool:msgbrasIppool", "", "common.error.happen");
        }
    }

    /*
   Tuydv1: reset autoCcmplate
    */
    @Secured("@")
    public void resetListBrasEdit() {
        filterBras = Lists.newArrayList();
        if (brasIppoolEdit != null) {
            brasIppoolEdit.setBrasIp(null);
        }
        currentBrasEdit = null;
    }


    @Secured("@")
    public List<DomainDTO> searchListDomain(String input) {
        filterDomain = listDomain;
        CharSequence inputData = DataUtil.isNullOrEmpty(input) ? null : input.trim().toUpperCase();
        if (inputData == null) {
            return filterDomain;
        }
        return filterDomain.stream().filter(obj -> DataUtil.safeToString(obj.getDomain()).toUpperCase().contains(inputData)
                || DataUtil.safeToString(obj.getDescription()).toUpperCase().contains(inputData)).collect(Collectors.toList());
    }

    @Secured("@")
    public void selectDomainSearch(SelectEvent event) {
        try {
            Long domainId = ((DomainDTO) event.getObject()).getId();
            currentDomain = domainSv.findOne(domainId);
            brasIppoolSearch.setDomain(currentDomain.getId());
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmManageBrasIpPool:msgbrasIppool", ex.getErrorCode(), ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex);
            reportError("frmManageBrasIpPool:msgbrasIppool", "", "common.error.happen");
        }
    }

    /*
   Tuydv1: reset autoCcmplate
    */
    @Secured("@")
    public void resetListDomain() {
        filterDomain = Lists.newArrayList();
        if (brasIppoolSearch != null) {
            brasIppoolSearch.setDomain(null);
        }
        currentDomain = null;

    }

    /*
  Tuydv1: reset autoCcmplate
   */
    @Secured("@")
    public void resetListDomainEdit() {
        filterDomain = Lists.newArrayList();
        if (brasIppoolEdit != null) {
            brasIppoolEdit.setDomain(null);
            brasIppoolEdit.setDomainId(null);
        }
        currentDomainEdit = null;

    }


    @Secured("@")
    public void selectDomainEdit(SelectEvent event) {
        try {
            Long domainId = ((DomainDTO) event.getObject()).getId();
            currentDomainEdit = domainSv.findOne(domainId);
            brasIppoolEdit.setDomain(currentDomainEdit.getId());
            brasIppoolEdit.setDomainId(currentDomainEdit.getId());
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmManageBrasIpPool:msgbrasIppool", ex.getErrorCode(), ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex);
            reportError("frmManageBrasIpPool:msgbrasIppool", "", "common.error.happen");
        }
    }

    @Secured("@")
    public void doExportResultSearch() {
        try {
            if (DataUtil.isNullOrEmpty(listSearchBrasIppoolExport)) {
                return;
            }
            FileExportBean bean = new FileExportBean();
            bean.setTempalatePath(FileUtil.getTemplatePath());
            bean.setTemplateName(Const.BRAS_IPPOOL.BRAS_IPPOOL_SEARCH_EXPORT);
            for (BrasIppoolDTO baBrasIppoolDTO : listSearchBrasIppoolExport) {
                baBrasIppoolDTO.setStatusStr(getStatus(baBrasIppoolDTO.getStatus()));
                baBrasIppoolDTO.setUpdateDatetimeStr(DateUtil.date2ddMMyyyyString(baBrasIppoolDTO.getUpdateDatetime()));
                baBrasIppoolDTO.setStrDomain(getDomain(baBrasIppoolDTO.getDomainId()));
                baBrasIppoolDTO.setStrBrasIp(getBrasIp(baBrasIppoolDTO.getBrasId()));

            }
            bean.putValue("lstData", listSearchBrasIppoolExport);
            Workbook workbook = FileUtil.exportWorkBook(bean);

            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + "ResultSearch" + Const.BRAS_IPPOOL.XLS + "\"");
            externalContext.setResponseContentType("application/vnd.ms-excel");
            workbook.write(externalContext.getResponseOutputStream());
            facesContext.responseComplete();
            return;
        } catch (LogicException lex) {
            reportError(lex);
            topPage();
        } catch (Exception ex) {
            logger.error("", ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    @Secured("@")
    public void searchIpPoolExcel() {
        try {
            if (!brasIppoolSearch.isSpecificIp()) {
                brasIppoolSearch.setIpType(null);
                brasIppoolSearch.setIpLabel(null);
            }
            workMode = WORK_MODE_SEARCH;
            trackInput = "0";
            workModeEdit = false;
            addfile = false;
            renderAddFile = false;
            if (!DataUtil.isNullOrEmpty(strListArea)) {
//                List<String> listCodeArea = DataUtil.collectProperty(listAreaSearch, "areaCode", String.class);
                String area = Joiner.on(",").skipNulls().join(strListArea);
                brasIppoolSearch.setProvince(area);
            } else {
                brasIppoolSearch.setProvince("");
            }
            brasIppoolSearch.setExportExcel(true);
            listSearchBrasIppoolExport = brasIppoolSv.search(brasIppoolSearch);
//            if (DataUtil.isNullOrEmpty(listSearchBrasIppool)) {
//                throw new LogicException("", "brasIpPool.not.search.bras.ippool");
//            }

//            reportSuccess("frmManageBrasIpPool:msgbrasIppool", getTextParam("brasIpPool.result.search.bras.ippool", DataUtil.safeToString(listSearchBrasIppool.size())));
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmManageBrasIpPool:msgbrasIppool", ex.getErrorCode(), ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex);
            reportError("frmManageBrasIpPool:msgbrasIppool", "", "common.error.happen");
        }
    }

    @Secured("@")
    public void searchIpPool() {
        try {
            if (!brasIppoolSearch.isSpecificIp()) {
                brasIppoolSearch.setIpType(null);
                brasIppoolSearch.setIpLabel(null);
            }
            workMode = WORK_MODE_SEARCH;
            trackInput = "0";
            workModeEdit = false;
            addfile = false;
            renderAddFile = false;
            if (!DataUtil.isNullOrEmpty(strListArea)) {
//                List<String> listCodeArea = DataUtil.collectProperty(listAreaSearch, "areaCode", String.class);
                String area = Joiner.on(",").skipNulls().join(strListArea);
                brasIppoolSearch.setProvince(area);
            } else {
                brasIppoolSearch.setProvince("");
            }
            brasIppoolSearch.setExportExcel(false);
            listSearchBrasIppool = brasIppoolSv.search(brasIppoolSearch);
//            if (DataUtil.isNullOrEmpty(listSearchBrasIppool)) {
//                throw new LogicException("", "brasIpPool.not.search.bras.ippool");
//            }

//            reportSuccess("frmManageBrasIpPool:msgbrasIppool", getTextParam("brasIpPool.result.search.bras.ippool", DataUtil.safeToString(listSearchBrasIppool.size())));
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmManageBrasIpPool:msgbrasIppool", ex.getErrorCode(), ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex);
            reportError("frmManageBrasIpPool:msgbrasIppool", "", "common.error.happen");
        }
    }

    @Secured("@")
    public void prepareAdd() {
        workMode = WORK_MODE_ADD;
        addfile = false;
        brasIppoolEdit = new BrasIppoolDTO(addfile);
//        curentAreaEdit = new AreaDTO();
//        currentBrasEdit = new BrasDTO();
//        currentDomainEdit = new DomainDTO();
        listAreaEdit = Lists.newArrayList();
        resetListBrasEdit();
        resetListDomainEdit();
        resetListAreaEdit();
        titleAddOrCoppy = "brasIpPool.add.bras.ippool";
        buttonAddOrCoppy = "common.add";

    }

    @Secured("@")
    public void prepareAddFile() {
        titleHeaderByFile = "brasIpPool.add.bras.ippool.file";
        buttonFile = "brasIpPool.add.file";
        headerConfirm = "brasIpPool.add.confirm.title";
        messageConfirm = "common.msg.add.confirm";
        resetForFile();
        showDeleteByFile = false;

    }

    @Secured("@")
    public void prepareDeleteFile() {
        titleHeaderByFile = "brasIpPool.delete.bras.ippool.byFile";
        buttonFile = "brasIpPool.delete.bras.ippool.file";
        headerConfirm = "brasIpPool.delete.bras.ippool.byFile";
        messageConfirm = "common.msg.delete.confirm";
        resetForFile();
        showDeleteByFile = true;
        showEditByFile = false;
    }

    @Secured("@")
    public void prepareEditFile() {
        titleHeaderByFile = "brasIpPool.edit.bras.ippool.byFile";
        buttonFile = "brasIpPool.edit.bras.ippool.file";
        headerConfirm = "brasIpPool.edit.bras.ippool.byFile";
        messageConfirm = "common.msg.edit.confirm";
        resetForFile();
        showDeleteByFile = true;
        showEditByFile = true;
    }

    @Secured("@")
    public void prepareViewFile() {
        titleHeaderByFile = "brasIpPool.view.bras.ippool.cm";
        buttonFile = "brasIpPool.view.bras.ippool";
        resetForFile();
        viewIp = true;
    }

    public void resetForFile() {
        listImportBrasIppool = Lists.newArrayList();
        listImportBrasIppoolAdd = Lists.newArrayList();
        listImportBrasIppoolError = Lists.newArrayList();
        workMode = WORK_MODE_ADD;
        addfile = true;
        renderAddFile = false;
        listAddBrasIppool = Lists.newArrayList();
        curentAreaEdit = new AreaDTO();
        currentBrasEdit = new BrasDTO();
        currentDomainEdit = new DomainDTO();
        listAreaEdit = Lists.newArrayList();
        workbook = null;
        enableExprotFileError = true;
        attachFileName = null;
        viewIp = false;
    }


    @Secured("@")
    public void validateForEditOrUpdate() {
        try {
            if (DataUtil.isNullOrEmpty(strListAreaEdit)) {
                if (WORK_MODE_ADD.equals(workMode)) {
                    focusElementByRawCSSSlector(".clAreaAdd");
                } else {
                    focusElementByRawCSSSlector(".clAreaEdit");
                }
                throw new LogicException("", "brasIpPool.input.provice");
            }
            String area = Joiner.on(",").skipNulls().join(strListAreaEdit);
            brasIppoolEdit.setProvince(area);
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmManageBrasIpPool:msgbrasIppool", ex.getErrorCode(), ex.getDescription());
            topPage();
        } catch (Exception ex) {
            logger.error(ex);
            reportError("frmManageBrasIpPool:msgbrasIppool", "", "common.error.happen");
            topPage();
        }
    }

    @Secured("@")
    public void addBrasIppool() {
        try {
            if (!brasIppoolEdit.isSpecificIp()) {
                brasIppoolEdit.setIpLabel(null);
                brasIppoolEdit.setIpType(null);
            }
            brasIppoolEdit = brasIppoolSv.createNewBrasIppool(brasIppoolEdit, currentStaff.getStaffCode());
            reportSuccess("frmManageBrasIpPool:msgbrasIppool", "brasIpPool.add.success");
            create();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmManageBrasIpPool:msgbrasIppool", ex.getErrorCode(), ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex);
            reportError("frmManageBrasIpPool:msgbrasIppool", "", "common.error.happen");
        }
        topPage();
    }

    @Secured("@")
    public void reset() throws Exception, LogicException {
        resetListBras();
        resetListDomain();
        resetListArea();
        resetListBrasEdit();
        resetListDomainEdit();
        resetListAreaEdit();
        create();
        viewIp = false;
    }

    @Secured("@")
    public void back() {
        workMode = WORK_MODE_SEARCH;
        currentBrasEdit = new BrasDTO();
        currentDomainEdit = new DomainDTO();
        addfile = false;
        renderAddFile = false;
    }

    @Secured("@")
    public void prepareEdit(BrasIppoolDTO brasEdit) {
        try {
            copyBrasIppool(brasEdit);
            workMode = WORK_MODE_EDIT;

        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmManageBrasIpPool:msgbrasIppool", ex.getErrorCode(), ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex);
            reportError("frmManageBrasIpPool:msgbrasIppool", "", "common.error.happen");
        }
    }

    @Secured("@")
    public void prepareCopy(BrasIppoolDTO brasEdit) {
        try {
            copyBrasIppool(brasEdit);
            brasIppoolEdit.setPoolId(null);
            workMode = WORK_MODE_ADD;
            addfile = false;
            titleAddOrCoppy = "brasIpPool.coppy.bras.ippool";
            buttonAddOrCoppy = "common.coppy";

        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmManageBrasIpPool:msgbrasIppool", ex.getErrorCode(), ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex);
            reportError("frmManageBrasIpPool:msgbrasIppool", "", "common.error.happen");
        }
    }

    private void copyBrasIppool(BrasIppoolDTO brasEdit) throws Exception {
        listAreaEdit = Lists.newArrayList();
        strListAreaEdit.clear();
        brasIppoolEdit = DataUtil.cloneBean(brasEdit);

        currentBrasEdit = new BrasDTO();
        currentBrasEdit = brasSv.findByBrasId(brasIppoolEdit.getBrasId());
        if (!DataUtil.isNullObject(currentBrasEdit)) {
            brasIppoolEdit.setBrasId(currentBrasEdit.getBrasId());
        }
        currentDomainEdit = new DomainDTO();
        currentDomainEdit = domainSv.findByDomainId(brasIppoolEdit.getDomainId());
        if (!DataUtil.isNullObject(currentDomainEdit)) {
            brasIppoolEdit.setDomainId(currentDomainEdit.getId());
        }
        if (!DataUtil.isNullOrEmpty(brasIppoolEdit.getIpType())
                || !DataUtil.isNullOrEmpty(brasIppoolEdit.getIpLabel())) {
            brasIppoolEdit.setSpecificIp(true);
        }
        if (!DataUtil.isNullOrEmpty(brasIppoolEdit.getProvince())) {
            strListAreaEdit = new ArrayList<>(Arrays.asList(brasIppoolEdit.getProvince().split(",")));
            for (String areaCode : strListAreaEdit) {
                AreaDTO areaDTO = areaSv.findByCode(areaCode.trim());
                listAreaEdit.add(areaDTO);
            }
        }
    }

    @Secured("@")
    public void editBrasIppool() {
        try {
            if (!brasIppoolEdit.isSpecificIp()) {
                brasIppoolEdit.setIpLabel(null);
                brasIppoolEdit.setIpType(null);
            }
            brasIppoolEdit.setUpdateDatetime(new Date());
            BaseMessage ret = brasIppoolSv.update(brasIppoolEdit, currentStaff.getStaffCode());
            if (ret.isSuccess()) {
                reportSuccess("frmManageBrasIpPool:msgbrasIppool", "brasIpPool.edit.success");
                create();
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmManageBrasIpPool:msgbrasIppool", ex.getErrorCode(), ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex);
            reportError("frmManageBrasIpPool:msgbrasIppool", "", "common.error.happen");
        }
        topPage();
    }

    @Secured("@")
    public void validateDelete(BrasIppoolDTO brasIppoolDTO) {
        if (DataUtil.isNullObject(brasIppoolDTO)
                || DataUtil.isNullObject(brasIppoolDTO.getPoolId())) {
            reportError("frmManageBrasIpPool:msgbrasIppool", "", "brasIpPool.deleteOne.validate");
            topPage();
        }
        index = listSearchBrasIppool.indexOf(brasIppoolDTO);

    }

    @Secured("@")
    public void deleteBrasIppool() {
        try {
            BrasIppoolDTO brasIppoolDTO = listSearchBrasIppool.get(index);
            BaseMessage ret = brasIppoolSv.delete(Lists.newArrayList(brasIppoolDTO.getPoolId()));
            if (ret.isSuccess()) {
                reportSuccess("frmManageBrasIpPool:msgbrasIppool", "brasIpPool.deleteOne.success");
//                listSearchBrasIppool.remove(brasIppoolDTO);
                listSearchBrasIppool = brasIppoolSv.search(brasIppoolSearch);
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmManageBrasIpPool:msgbrasIppool", ex.getErrorCode(), ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex);
            reportError("frmManageBrasIpPool:msgbrasIppool", "", "common.error.happen");
        }
        topPage();
    }

    @Secured("@")
    public void validateDeleteList() {
        try {
            if (DataUtil.isNullOrEmpty(listSearchBrasIppoolSelecttion)) {
                throw new LogicException("", "brasIpPool.delete.validate");
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmManageBrasIpPool:msgbrasIppool", ex.getErrorCode(), ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex);
            reportError("frmManageBrasIpPool:msgbrasIppool", "", "common.error.happen");
        }
        topPage();
    }

    @Secured("@")
    public void deleteBrasIppoolList() {
        try {
            if (!DataUtil.isNullOrEmpty(listSearchBrasIppoolSelecttion)) {
                List<Long> listId = DataUtil.collectProperty(listSearchBrasIppoolSelecttion, "poolId", Long.class);
                BaseMessage ret = brasIppoolSv.delete(listId);
                if (ret.isSuccess()) {
                    reportSuccess("frmManageBrasIpPool:msgbrasIppool", "brasIpPool.deleteList.success");
                    listSearchBrasIppool.removeAll(listSearchBrasIppoolSelecttion);
                }
            }

        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmManageBrasIpPool:msgbrasIppool", ex.getErrorCode(), ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex);
            reportError("frmManageBrasIpPool:msgbrasIppool", "", "common.error.happen");
        }
        topPage();
    }

    @Secured("@")
    public void previewImport(FileUploadEvent event) {
        try {
            file = null;
            fileUpload = null;
            workbook = null;
            attachFileName = null;
            renderAddFile = false;
            listImportBrasIppool = Lists.newArrayList();
            listImportBrasIppoolAdd = Lists.newArrayList();
            listImportBrasIppoolError = Lists.newArrayList();
            if (DataUtil.isNullObject(event)) {
                showAttachFile = false;
                reportError("", ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "common.error.upload.file");
                topPage();

            } else {

                fileUpload = event.getFile();
                showAttachFile = true;
                BaseMessage message = validateFileUploadWhiteList(fileUpload, ALOW_EXTENSION_EXCEL_TYPE_LIST, MAX_SIZE_5M);
                if (!message.isSuccess()) {
                    LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                    ex.setDescription(message.getDescription());
                    throw ex;
                }
                String name = new String(event.getFile().getFileName().getBytes(), "UTF-8");
                attachFileName = name + "( " + event.getFile().getSize() / 1024L + "kB )";
                file = event.getFile().getInputstream();
                if (DataUtil.safeEqual("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", event.getFile().getContentType())) {
                    workbook = new XSSFWorkbook(file);
                } else {
                    workbook = new HSSFWorkbook(file);
                }
            }

        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmManageBrasIpPool:msgbrasIppool", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex);
            reportError("frmManageBrasIpPool:msgbrasIppool", "", "common.error.happen");
            topPage();
        }
    }

    private void getListBrasIppoolFromFile(Sheet sheet, FormulaEvaluator evaluator) throws Exception {
        long indexRow = 0;
        long index = 1;
        long colNum;
        if (showDeleteByFile) {
            colNum = 2;
        } else {
            colNum = 10;
            listSearchBrasIppool = brasIppoolSv.search(brasIppoolSearch);
        }
        listImportBrasIppoolAdd = Lists.newArrayList();
        listImportBrasIppoolError = Lists.newArrayList();
        listImportBrasIppool = Lists.newArrayList();
        listImportBrasIppoolEdit = Lists.newArrayList();
        List<BrasIppoolDTO> listSearchByIp;
        for (Row row : sheet) {
            if (indexRow < 1) {
                indexRow++;
                continue;
            }
            String ip = null;
            String poolName = null;
            String domain = null;
            String brasIp = null;
            String status = null;
            String ipMark = null;
            String poolUniq = null;
            String province = null;
            String ipType = null;
            String labelIp = null;
            List<String> errorList = new ArrayList<>();

            for (int col = 0; col <= colNum; col++) {
                Cell cell = row.getCell(col);
                Object object;
                if (cell == null) {
                    object = null;
                } else {
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    object = ImportBrasIppoolHander.handleCell(cell.getCellType(), cell, evaluator);
                }

                if (showDeleteByFile) {
                    if (showEditByFile) {
                        switch (col) {
                            case 0:
                                break;
                            case 1:
                                ip = DataUtil.safeToString(object).trim();
                                break;
                            default:
                                status = DataUtil.safeToString(object).trim();
                        }
                    } else {
                        switch (col) {
                            case 0:
                                break;
                            case 1:
                                ip = DataUtil.safeToString(object).trim();
                                break;
                            default:
                                province = DataUtil.safeToString(object).trim();
                        }
                    }
                } else {
                    switch (col) {
                        case 0:
                            break;
                        case 1:
                            ip = DataUtil.safeToString(object).trim();
                            break;
                        case 2:
                            poolName = DataUtil.safeToString(object).trim();
                            break;
                        case 3:
                            domain = DataUtil.safeToString(object).trim();
                            break;
                        case 4:
                            brasIp = DataUtil.safeToString(object).trim();
                            break;
                        case 5:
                            status = DataUtil.safeToString(object).trim();
                            break;
                        case 6:
                            ipMark = DataUtil.safeToString(object).trim();
                            break;
                        case 7:
                            poolUniq = DataUtil.safeToString(object).trim();
                            break;
//                    case 8:
//                        strDateUpdate = DataUtil.safeToString(object).trim();
//                        break;
                        case 8:
                            province = DataUtil.safeToString(object).trim();
                            break;
                        case 9:
                            ipType = DataUtil.safeToString(object).trim();
                            break;
                        default:
                            labelIp = DataUtil.safeToString(object).trim();

                    }
                }

            }
            currentBrasIppool = new BrasIppoolDTO();

            //set cac kieu
            currentBrasIppool.setIpPool(ip);
            if (showDeleteByFile) {
                if (showEditByFile) {
                    if (DataUtil.isNullObject(ip)) {
                        errorList.add(getText("brasIpPool.validate.require.ipPool"));
                    } else {
                        //kiem tra status
                        if (DataUtil.isNullOrEmpty(status)) {
                            errorList.add(getText("brasIpPool.validate.require.status"));
                        } else {
                            //Check co ton tai khong
                            if (checkExtsBrasStatus(status, listBrasStatus)) {
                                errorList.add(getText("brasIpPool.validate.not.contains.status"));
                            } else {
                                listSearchByIp = brasIppoolSv.searchByIp(currentBrasIppool);
                                if (DataUtil.isNullOrEmpty(listSearchByIp)) {
                                    errorList.add(getText("brasIpPool.validate.not.contains.ip"));
                                } else {
                                    currentBrasIppool = (listSearchByIp.get(0));
                                    currentBrasIppool.setStatus(status);
                                }
                            }
                        }
                    }
                    if (DataUtil.isNullOrEmpty(errorList)) {
                        listImportBrasIppoolAdd.add(currentBrasIppool);
                    } else {
                        String err = Joiner.on(", ").skipNulls().join(errorList);
                        currentBrasIppool.setMsgError(err);
                        listImportBrasIppoolError.add(currentBrasIppool);
                    }
                    listImportBrasIppool.add(currentBrasIppool);
                } else {
                    boolean checkExitProvince = false;
                    List<String> listAreaCodeDB = Lists.newArrayList();
                    //Kiem tra ip
                    if (DataUtil.isNullObject(ip)) {
                        errorList.add(getText("brasIpPool.validate.require.ipPool"));
                    } else {
                        listSearchByIp = brasIppoolSv.searchByIp(currentBrasIppool);
                        if (DataUtil.isNullOrEmpty(listSearchByIp)) {
                            errorList.add(getText("brasIpPool.validate.not.contains.ip"));
                        } else {

                            currentBrasIppool = (listSearchByIp.get(0));
                            BrasDTO brasDTO = brasSv.findByBrasId(currentBrasIppool.getBrasId());
                            DomainDTO domainDTO = domainSv.findByDomainId(currentBrasIppool.getDomainId());
                            if (!DataUtil.isNullObject(brasDTO)) {
                                currentBrasIppool.setStrBrasIp(brasDTO.getIp());
                                currentBrasIppool.setBrasId(brasDTO.getBrasId());
                            }
                            if (!DataUtil.isNullObject(domainDTO)) {
                                currentBrasIppool.setStrDomain(domainDTO.getDomain());
                                currentBrasIppool.setDomainId(domainDTO.getId());
                            }
                            //kiem tra province neu province ==null khong kiem tra; province # null  kiem tra
                            if (!DataUtil.isNullOrEmpty(province)) {
                                listAreaCodeDB.clear();
                                if (!DataUtil.isNullObject(currentBrasIppool.getProvince())) {
                                    listAreaCodeDB = new ArrayList<>(Arrays.asList(currentBrasIppool.getProvince().split(",")));
                                    for (String areaCode : listAreaCodeDB) {
                                        AreaDTO areaDTO = areaSv.findByCode(areaCode);
                                        if (DataUtil.isNullObject(areaDTO)) {
                                            errorList.add(getTextParam("brasIpPool.validate.province.notExits", areaCode));
                                        }
                                    }
                                }
                                List<String> listAreaCodeFile = new ArrayList<>(Arrays.asList(province.split(",")));
                                for (String areaCode : listAreaCodeFile) {
                                    if (listAreaCodeDB.contains(areaCode)) {
                                        checkExitProvince = true;
                                        listAreaCodeDB.remove(areaCode);
                                    }
                                }
                                if (checkExitProvince) {
                                    String provinceUpdate = Joiner.on(", ").skipNulls().join(listAreaCodeDB);
                                    currentBrasIppool.setProvince(provinceUpdate);
                                } else {
                                    currentBrasIppool.setProvince(province);
                                    errorList.add(getText("brasIpPool.validate.not.province"));
                                }

                            }
                        }
                    }
                    if (DataUtil.isNullOrEmpty(errorList)) {
                        if (checkExitProvince && !DataUtil.isNullOrEmpty(listAreaCodeDB)) {
                            listImportBrasIppoolEdit.add(currentBrasIppool);
                        } else {
                            listImportBrasIppoolAdd.add(currentBrasIppool);
                        }
                    } else {
                        String err = Joiner.on(", ").skipNulls().join(errorList);
                        currentBrasIppool.setMsgError(err);
                        listImportBrasIppoolError.add(currentBrasIppool);
                    }
                    listImportBrasIppool.add(currentBrasIppool);
                }
            } else {
                currentBrasIppool.setPoolName(poolName);
                currentBrasIppool.setStrDomain(domain);
                currentBrasIppool.setStrBrasIp(brasIp);
                currentBrasIppool.setStatus(status);
                currentBrasIppool.setIpMask(ipMark);
                currentBrasIppool.setPoolUniq(poolUniq);
                currentBrasIppool.setProvince(province);
                currentBrasIppool.setIpType(ipType);
                currentBrasIppool.setIpLabel(labelIp);

                if (!(DataUtil.isNullObject(ip) && DataUtil.isNullObject(poolName)
                        && DataUtil.isNullObject(domain) && DataUtil.isNullObject(brasIp)
                        && DataUtil.isNullObject(status) && DataUtil.isNullObject(ipMark)
                        && DataUtil.isNullObject(poolUniq) && DataUtil.isNullObject(province)
                        && DataUtil.isNullObject(ipType) && DataUtil.isNullObject(labelIp))) {
                    currentBrasIppool.setRowIndex(indexRow);
                    //Kiem tra ip
                    if (DataUtil.isNullObject(ip)) {
                        errorList.add(getText("brasIpPool.validate.require.ipPool"));
                    } else {
                        if (!DataUtil.validateStringByPattern(ip, Const.BRAS_IPPOOL.IP_REGEX)) {
                            errorList.add(getText("brasIpPool.validate.ip_regex"));
                        } else {
                            //Check max length
                            if (ip.length() > 20) {
                                errorList.add(getText("brasIpPool.validate.maxlength.ipPool"));
                            } else {
                                // Check ip daton tai trong list ko chua chua
                                if (checkExtsIp(ip, listImportBrasIppoolAdd)) {
                                    errorList.add(getTextParam("brasIpPool.duplicate", ip));
                                } else {
                                    //Check kiem tra xem da ton tai ip trong db chua
                                    if (brasIppoolSv.checkDulicateBrasIpPool(ip)) {
                                        errorList.add(getTextParam("brasIpPool.duplicate", ip));
                                    }
                                }
                            }
                        }
                    }
                    //kiem tra ten goi
                    if (DataUtil.isNullOrEmpty(poolName)) {
                        errorList.add(getText("brasIpPool.validate.require.name"));
                    } else {
                        //Check max length
                        if (poolName.length() > 20) {
                            errorList.add(getText("brasIpPool.validate.maxlength.name"));
                        }
                    }
                    //kiem tra domain
                    if (DataUtil.isNullOrEmpty(domain)) {
                        errorList.add(getText("brasIpPool.validate.require.domainId"));
                    } else {
                        //Check co ton tai khong
                        if (checkExtsDomain(domain, listDomain)) {
                            errorList.add(getText("brasIpPool.validate.not.contains.domainId"));
                        } else {
                            // gan id
                            Long id = convertDomainId(domain);
                            if (DataUtil.isNullObject(id)) {
                                errorList.add(getText("brasIpPool.validate.not.contains.domainId"));
                            } else {
                                currentBrasIppool.setDomainId(id);
                                currentBrasIppool.setDomain(id);
                            }
                        }

                    }
                    //kiem tra status
                    if (DataUtil.isNullOrEmpty(status)) {
                        errorList.add(getText("brasIpPool.validate.require.status"));
                    } else {
                        //Check co ton tai khong
                        if (checkExtsBrasStatus(status, listBrasStatus)) {
                            errorList.add(getText("brasIpPool.validate.not.contains.status"));
                        }

                    }
                    //kiem tra brasIp
                    if (DataUtil.isNullOrEmpty(brasIp)) {
                        errorList.add(getText("brasIpPool.validate.require.brasId"));
                    } else {
                        if (!DataUtil.validateStringByPattern(ip, Const.BRAS_IPPOOL.IP_REGEX)) {
                            errorList.add(getText("brasIpPool.validate.ip_regex"));
                        }
                        //Check co ton tai khong
                        if (checkExtsBrasIp(brasIp, listBras)) {
                            errorList.add(getText("brasIpPool.validate.not.contains.brasId"));
                        } else {
                            // gan id
                            Long id = convertBrasId(brasIp);
                            if (DataUtil.isNullObject(id)) {
                                errorList.add(getText("brasIpPool.validate.not.contains.brasId"));
                            } else {
                                currentBrasIppool.setBrasId(id);
                                currentBrasIppool.setBrasIp(id);
                            }
                        }

                    }
                    //kiem tra poolUniq
                    if (!DataUtil.isNullOrEmpty(poolUniq) && poolUniq.length() > 30) {
                        errorList.add(getText("brasIpPool.validate.maxlength.poooUniq"));
                    }
                    //kiem tra ipMark
                    if (!DataUtil.isNullOrEmpty(ipMark) && ipMark.length() > 200) {
                        errorList.add(getText("brasIpPool.validate.maxlength.ipMask"));
                    }
                    //kiem tra province
                    if (DataUtil.isNullOrEmpty(province)) {
                        errorList.add(getText("brasIpPool.validate.require.province"));
                    } else if (!DataUtil.isNullOrEmpty(province) && province.length() > 100) {
                        errorList.add(getText("brasIpPool.validate.maxlength.province"));
                    } else {
                        List<String> listAreaCodeFile = new ArrayList<>(Arrays.asList(province.split(",")));
                        String areaTmp = "";
                        boolean checkStrArea = false;
                        for (String areaCode : listAreaCodeFile) {
                            AreaDTO areaDTO = areaSv.findByCode(areaCode.trim());
                            if (!DataUtil.isNullObject(areaTmp)) {
                                areaTmp += ",";
                            }
                            areaTmp += areaCode.trim();
                            if (DataUtil.isNullObject(areaDTO)) {
                                errorList.add(getTextParam("brasIpPool.validate.province.notExits", areaCode));
                                checkStrArea = true;
                            }
                        }
                        if (!checkStrArea) {
                            currentBrasIppool.setProvince(areaTmp);
                        }

                    }
                    // check neu co IpType thi label ip deu co
                    if ((!DataUtil.isNullOrEmpty(ipType) && DataUtil.isNullOrEmpty(labelIp)) || (!DataUtil.isNullOrEmpty(labelIp) && DataUtil.isNullOrEmpty(ipType))) {
                        errorList.add(getText("brasIpPool.validate.require.iplabel.iptype"));
                    } else if (!DataUtil.isNullOrEmpty(ipType) && !DataUtil.isNullOrEmpty(labelIp)) {
                        //check loai ip dac thu
                        if (!ValidateService.checkValueContainOptionSet(ipType, listIpSpecific)) {
                            errorList.add(getText("brasIpPool.validate.not.contains.ipType"));
                        }
                        if (labelIp.length() > 100) {
                            errorList.add(getText("brasIpPool.validate.maxlength.ipLable"));
                        }
                    }
                    currentBrasIppool.setRowIndex(index++);
                    if (DataUtil.isNullOrEmpty(errorList)) {
                        listImportBrasIppoolAdd.add(currentBrasIppool);
                    } else {
                        String err = Joiner.on(", ").skipNulls().join(errorList);
                        currentBrasIppool.setMsgError(err);
                        listImportBrasIppoolError.add(currentBrasIppool);
                    }
                    listImportBrasIppool.add(currentBrasIppool);
                }
            }
        }
    }

    private boolean checkExtsIp(String ip, List<BrasIppoolDTO> list) throws Exception {
        boolean result = false;
        if (!DataUtil.isNullOrEmpty(list)) {
            for (BrasIppoolDTO dto : list) {
                if (DataUtil.safeEqual(ip, dto.getIpPool())) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    private boolean checkExtsDomain(String domain, List<DomainDTO> list) throws Exception {
        boolean result = true;
        if (!DataUtil.isNullOrEmpty(list)) {
            for (DomainDTO dto : list) {
                if (DataUtil.safeEqual(domain, dto.getDomain())) {
                    currentBrasIppool.setDomainId(dto.getId());
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    private boolean checkExtsBrasStatus(String status, List<OptionSetValueDTO> list) throws Exception {
        boolean result = true;
        if (!DataUtil.isNullOrEmpty(list)) {
            for (OptionSetValueDTO dto : list) {
                if (DataUtil.safeEqual(status, dto.getValue())) {
                    currentBrasIppool.setStatus(dto.getValue());
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    private boolean checkExtsBrasIp(String brasIp, List<BrasDTO> list) throws Exception {
        boolean result = true;
        if (!DataUtil.isNullOrEmpty(list)) {
            for (BrasDTO dto : list) {
                if (DataUtil.safeEqual(brasIp, dto.getIp())) {
                    currentBrasIppool.setBrasId(dto.getBrasId());
                    result = false;
                    break;

                }
            }
        }
        return result;
    }

//    @Secured("@")
//    public StreamedContent addTemplateFile() {
//        InputStream createStream = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream("/resources/templates/ThemSerial.xls");
//        StreamedContent createTemplateFile = new DefaultStreamedContent(createStream, "application/xls", "brasIppoolPattern.xls");
//        return createTemplateFile;
//    }

    /**
     * Tuydv1
     *
     * @return
     */
    public StreamedContent getAddTemplateFile() {
        String fileName;
        String filePath;
        if (showDeleteByFile) {
            if (showEditByFile) {
                fileName = Const.BRAS_IPPOOL.BRAS_IPPOOL_INFO_EDIT;
                filePath = Const.BRAS_IPPOOL.RESOURCE_TEMPLATE_PATH + Const.BRAS_IPPOOL.BRAS_IPPOOL_INFO_EDIT;
            } else {
                fileName = Const.BRAS_IPPOOL.BRAS_IPPOOL_INFO_DELETE;
                filePath = Const.BRAS_IPPOOL.RESOURCE_TEMPLATE_PATH + Const.BRAS_IPPOOL.BRAS_IPPOOL_INFO_DELETE;
            }
        } else {
            fileName = Const.BRAS_IPPOOL.BRAS_IPPOOL_INFO;
            filePath = Const.BRAS_IPPOOL.RESOURCE_TEMPLATE_PATH + Const.BRAS_IPPOOL.BRAS_IPPOOL_INFO;
        }
        if (viewIp) {
            fileName = Const.BRAS_IPPOOL.BRAS_IPPOOL_VIEW_IP;
            filePath = Const.BRAS_IPPOOL.RESOURCE_TEMPLATE_PATH + Const.BRAS_IPPOOL.BRAS_IPPOOL_VIEW_IP;
        }
        InputStream createStream = Faces.getResourceAsStream(filePath);
        return new DefaultStreamedContent(createStream, "application/xls", fileName);
    }

    @Secured("@")
    public void preViewImportFile() {
        try {
            listImportBrasIppool = Lists.newArrayList();
            listImportBrasIppoolError = Lists.newArrayList();
            listImportBrasIppoolAdd = Lists.newArrayList();
            if (!DataUtil.isNullObject(workbook)) {
                FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                Sheet sheet = workbook.getSheetAt(0);
                getListBrasIppoolFromFile(sheet, evaluator);
                enableExprotFileError = true;
                renderAddFile = true;
                if (DataUtil.isNullOrEmpty(listImportBrasIppool)) {
                    reportError("frmManageBrasIpPool:msgbrasIppool", "", "brasIpPool.button.import.bras.validate");
                }
            } else {
                reportError("frmManageBrasIpPool:msgbrasIppool", "", "brasIpPool.button.import.bras.validate");
            }
        } catch (Exception ex) {
            logger.error(ex);
            reportError("frmManageBrasIpPool:msgbrasIppool", "", "common.error.happen");
        }
    }

    @Secured("@")
    public void validateImportBrasIppool() {
        try {
            listImportBrasIppool = Lists.newArrayList();
            listImportBrasIppoolError = Lists.newArrayList();
            listImportBrasIppoolAdd = Lists.newArrayList();
            listImportBrasIppoolEdit = Lists.newArrayList();
            if (!DataUtil.isNullObject(workbook)) {
                FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                Sheet sheet = workbook.getSheetAt(0);
                getListBrasIppoolFromFile(sheet, evaluator);
                enableExprotFileError = false;
                renderAddFile = true;
                if (DataUtil.isNullOrEmpty(listImportBrasIppool) && DataUtil.isNullOrEmpty(listImportBrasIppoolAdd)) {
                    reportError("frmManageBrasIpPool:msgbrasIppool", "", "brasIpPool.button.import.bras.validate");
                } else {
                    if (DataUtil.isNullOrEmpty(listImportBrasIppoolAdd) &&
                            DataUtil.isNullOrEmpty(listImportBrasIppoolEdit)) {
                        reportError("frmManageBrasIpPool:msgbrasIppool", "", "brasIpPool.button.import.bras");
                    }

                }
            } else {
                reportError("frmManageBrasIpPool:msgbrasIppool", "", "brasIpPool.button.import.bras.validate");
            }
        } catch (Exception ex) {
            logger.error(ex);
            reportError("frmManageBrasIpPool:msgbrasIppool", "", "common.error.happen");
        }
    }

    @Secured("@")
    public void exportViewIp() {
        //hoangnt
        try {
            if (DataUtil.isNullObject(workbook)) {
                reportError("frmManageBrasIpPool:msgbrasIppool", "", "brasIpPool.button.import.bras.validate");
                return;
            }
            List<BrasIppoolDTO> lstViewIpExport = Lists.newArrayList();
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            Sheet sheet = workbook.getSheetAt(0);
            long indexRow = 0;
            for (Row row : sheet) {
                if (indexRow < 1) {
                    indexRow++;
                    continue;
                }
                Cell cell = row.getCell(1);
                Object object;
                if (cell == null) {
                    object = null;
                } else {
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    object = ImportBrasIppoolHander.handleCell(cell.getCellType(), cell, evaluator);
                }
                String ip = DataUtil.safeToString(object).trim();
                BrasIppoolDTO brasIppoolDTO = new BrasIppoolDTO();
                brasIppoolDTO.setIpPool(ip);
                if (DataUtil.isNullOrEmpty(ip)) {
                    brasIppoolDTO.setNote(getText("brasIpPool.view.ip.empty"));
                    lstViewIpExport.add(brasIppoolDTO);
                    continue;
                }

                brasIppoolDTO = brasIppoolSv.getBrasViewIpFromCM(ip);
                if (DataUtil.isNullObject(brasIppoolDTO)) {
                    brasIppoolDTO = new BrasIppoolDTO();
                    brasIppoolDTO.setIpPool(ip);
                    brasIppoolDTO.setNote(getText("brasIpPool.view.ip.not.found.cm"));
                } else {
                    brasIppoolDTO.setStatusStr(convertStatusStr(brasIppoolDTO.getStatus()));
                    List<BrasIppoolDTO> listSearchByIp = brasIppoolSv.searchByIp(brasIppoolDTO);
                    if (DataUtil.isNullOrEmpty(listSearchByIp)) {
                        brasIppoolDTO.setNote(getText("brasIpPool.view.ip.not.found.im"));
                        lstViewIpExport.add(brasIppoolDTO);
                        continue;
                    }
                }
                lstViewIpExport.add(brasIppoolDTO);
            }
            //xuat file
            FileExportBean bean = new FileExportBean();
            bean.setTempalatePath(FileUtil.getTemplatePath());
            bean.setTemplateName(Const.BRAS_IPPOOL.BRAS_IPPOOL_VIEW_IP_EXPORT);
            bean.putValue("lstData", lstViewIpExport);
            Workbook workbook = FileUtil.exportWorkBook(bean);

            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + Const.BRAS_IPPOOL.BRAS_IPPOOL_VIEW_IP_EXPORT + "\"");
            externalContext.setResponseContentType("application/vnd.ms-excel");
            workbook.write(externalContext.getResponseOutputStream());
            facesContext.responseComplete();
            return;


        } catch (LogicException lex) {
            reportError(lex);
            topPage();
        } catch (Exception ex) {
            logger.error("", ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    private String convertStatusStr(String status) {
        if (DataUtil.safeEqual(status, "1")) {
            return getText("brasIpPool.view.ip.status.1");
        } else {
            return getText("brasIpPool.view.ip.status.2");
        }
    }

    @Secured("@")
    public void exportExcelError() {
        try {
            if (DataUtil.isNullOrEmpty(listImportBrasIppoolError)) {
                enableExprotFileError = true;
                reportErrorValidateFail("", "", "common.emty.records");
            } else {
                enableExprotFileError = false;
                FileExportBean bean = new FileExportBean();
                bean.setTempalatePath(FileUtil.getTemplatePath());
                if (showDeleteByFile) {
                    if (showEditByFile) {
                        bean.setTemplateName(Const.BRAS_IPPOOL.BRAS_IPPOOL_EDIT_ERROR_FILE);
                    } else {
                        bean.setTemplateName(Const.BRAS_IPPOOL.BRAS_IPPOOL_DELETE_ERROR_FILE);
                    }
                } else {
                    bean.setTemplateName(Const.BRAS_IPPOOL.BRAS_IPPOOL_ERROR_FILE);
                }
                bean.putValue("lstData", listImportBrasIppoolError);
                Workbook workbook = FileUtil.exportWorkBook(bean);

                FacesContext facesContext = FacesContext.getCurrentInstance();
                ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
                externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + Const.BRAS_IPPOOL.BRAS_IPPOOL_ERROR + "_" + currentStaff.getStaffCode() + "_" + DateUtil.date2ddMMyyyyHHMMssNoSlash(new Date()) + Const.BRAS_IPPOOL.XLS + "\"");
                externalContext.setResponseContentType("application/vnd.ms-excel");
                workbook.write(externalContext.getResponseOutputStream());
                facesContext.responseComplete();
                return;
            }
        } catch (LogicException lex) {
            reportError(lex);
            topPage();
        } catch (Exception ex) {
            logger.error("", ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    @Secured("@")
    public void importBrasIppoolFromFile() {
        try {
            renderAddFile = true;
            List<Long> listId;
            List<BrasIppoolDTO> listImportBrasIppoolAddTmd = DataUtil.cloneBean(listImportBrasIppoolAdd);
            for (BrasIppoolDTO dto : listImportBrasIppoolAddTmd) {
                try {
                    if (showDeleteByFile) {
                        if (showEditByFile) {
                            brasIppoolSv.updateStatus(dto, currentStaff.getStaffCode());
                        } else {
                            listId = Lists.newArrayList();
                            listId.add(dto.getPoolId());
                            brasIppoolSv.delete(listId);
                        }
                        renderAddFile = false;
                    } else {
                        brasIppoolSv.createNewBrasIppool(dto, currentStaff.getStaffCode());
                    }
                } catch (LogicException ex) {
                    logger.error(ex.getMessage(), ex);
                    listImportBrasIppoolAdd = removeBrasIppool(listImportBrasIppoolAdd, dto);
                    dto.setMsgError(ex.getDescription());
                    listImportBrasIppoolError.add(dto);

                } catch (Exception ex) {
                    logger.error(ex);
                    listImportBrasIppoolAdd = removeBrasIppool(listImportBrasIppoolAdd, dto);
                    dto.setMsgError(getText("common.error.happen"));
                    listImportBrasIppoolError.add(dto);
                }
            }
            // update nhung anh sach
            if (!DataUtil.isNullOrEmpty(listImportBrasIppoolEdit) && showDeleteByFile) {
                listImportBrasIppoolAddTmd = DataUtil.cloneBean(listImportBrasIppoolEdit);
                for (BrasIppoolDTO dto : listImportBrasIppoolAddTmd) {
                    try {
                        brasIppoolSv.update(dto, currentStaff.getStaffCode());
                    } catch (LogicException ex) {
                        logger.error(ex.getMessage(), ex);
                        listImportBrasIppoolEdit = removeBrasIppool(listImportBrasIppoolEdit, dto);
                        dto.setMsgError(ex.getDescription());
                        listImportBrasIppoolError.add(dto);

                    } catch (Exception ex) {
                        logger.error(ex);
                        listImportBrasIppoolEdit = removeBrasIppool(listImportBrasIppoolEdit, dto);
                        dto.setMsgError(getText("common.error.happen"));
                        listImportBrasIppoolError.add(dto);
                    }
                }
            }

            String result;
            if (showDeleteByFile) {
                if (showEditByFile) {
                    result = Integer.toString(listImportBrasIppoolAdd.size()) + "/" +
                            Integer.toString(listImportBrasIppool.size());
                    reportSuccess("frmManageBrasIpPool:msgbrasIppool", getTextParam("quota.establish.edit.multi", result));
                } else {
                    result = Integer.toString(listImportBrasIppoolAdd.size() + listImportBrasIppoolEdit.size()) + "/" +
                            Integer.toString(listImportBrasIppool.size());
                    reportSuccess("frmManageBrasIpPool:msgbrasIppool", getTextParam("quota.establish.delete.multi", result));
                }
            } else {
                result = Integer.toString(listImportBrasIppoolAdd.size()) + "/" +
                        Integer.toString(listImportBrasIppool.size());
                reportSuccess("frmManageBrasIpPool:msgbrasIppool", getTextParam("brasIpPool.add.bras.ippool.result", result));
            }
            listImportBrasIppoolAdd = Lists.newArrayList();
            listImportBrasIppool = Lists.newArrayList();
            listImportBrasIppoolEdit = Lists.newArrayList();
            if (!DataUtil.isNullOrEmpty(listImportBrasIppoolError)) {
                enableExprotFileError = false;
            }
        } catch (Exception ex) {
            logger.error(ex);
            reportError("frmManageBrasIpPool:msgbrasIppool", "", "common.error.happen");
        }
        topPage();
    }


    /**
     * tuydv1
     *
     * @param status
     * @return
     */
    @Secured("@")
    public String getStatus(String status) {
        String statusConvert = "";

        if (!DataUtil.isNullObject(status) || DataUtil.isNullOrEmpty(status)) {
            for (OptionSetValueDTO brasStatus : listBrasStatus) {
                if (DataUtil.safeEqual(brasStatus.getValue(), status)) {
                    statusConvert = brasStatus.getName();
                    break;
                }
            }
        }
        return statusConvert;
    }

    /**
     * tuydv1
     *
     * @param domain
     * @return
     */
    @Secured("@")
    public String getDomain(Long domain) {
        String domainConvert = "";

        if (!DataUtil.isNullObject(domain)) {
            for (DomainDTO dto : listDomain) {
                if (DataUtil.safeEqual(dto.getId(), domain)) {
                    domainConvert = dto.getDomain();
                    break;
                }
            }
        }
        return domainConvert;
    }

    /**
     * tuydv1
     *
     * @param domain
     * @return
     */
    @Secured("@")
    public Long convertDomainId(String domain) {
        Long domainConvert = null;

        if (!DataUtil.isNullObject(domain)) {
            for (DomainDTO dto : listDomain) {
                if (DataUtil.safeEqual(dto.getDomain(), domain)) {
                    domainConvert = dto.getId();
                    break;
                }
            }
        }
        return domainConvert;
    }

    /**
     * tuydv1
     *
     * @param brasIp
     * @return
     */
    @Secured("@")
    public String getBrasIp(Long brasIp) {
        String brasConvert = "";

        if (!DataUtil.isNullObject(brasIp)) {
            for (BrasDTO dto : listBras) {
                if (DataUtil.safeEqual(dto.getBrasId(), brasIp)) {
                    brasConvert = dto.getIp();
                    break;
                }
            }
        }
        return brasConvert;
    }

    /**
     * tuydv1
     *
     * @param brasIp
     * @return
     */
    @Secured("@")
    public Long convertBrasId(String brasIp) {
        Long brasConvert = null;

        if (!DataUtil.isNullObject(brasIp)) {
            for (BrasDTO dto : listBras) {
                if (DataUtil.safeEqual(dto.getIp(), brasIp)) {
                    brasConvert = dto.getBrasId();
                    break;
                }
            }
        }
        return brasConvert;
    }

    @Secured("@")
    public String getProvince(String province) {
        String provinceConvert = "";
        if (!DataUtil.isNullObject(province) || DataUtil.isNullOrEmpty(province)) {
            List<String> listAreaCode = new ArrayList<>(Arrays.asList(province.split(",")));
            for (String areaCode : listAreaCode) {
                String provincetmp = "";
                for (AreaDTO areaDTO : listArea) {
                    if (DataUtil.safeEqual(areaDTO.getAreaCode().trim(), areaCode.trim())) {
                        provincetmp = areaDTO.getName();
                        break;
                    }
                }
                if (!DataUtil.isNullObject(provincetmp)) {
                    if (!DataUtil.isNullObject(provinceConvert)) {
                        provinceConvert = provinceConvert + "," + provincetmp;
                    } else {
                        provinceConvert = provincetmp;
                    }

                }
            }
        }
        return provinceConvert;
    }

    @Secured("@")
    public void checkRender(boolean bl) {
        render = bl;
        notRender = !bl;
        showArea = false;

    }

    @Secured("@")
    public void reSetFile() {
        addfile = false;
        workMode = WORK_MODE_SEARCH;
    }

    private List<BrasIppoolDTO> removeBrasIppool(List<BrasIppoolDTO> list, BrasIppoolDTO removeDto) {
        if (!DataUtil.isNullOrEmpty(list)) {
            for (BrasIppoolDTO dto : list) {
                if (compare(dto.getIpPool(), removeDto.getIpPool()) && compare(dto.getPoolName(), removeDto.getPoolName())
                        && compare(dto.getStrDomain(), removeDto.getStrDomain()) && compare(dto.getStrBrasIp(), removeDto.getStrBrasIp())
                        && compare(dto.getStatus(), removeDto.getStatus()) && compare(dto.getIpMask(), removeDto.getIpMask())
                        && compare(dto.getPoolUniq(), removeDto.getPoolUniq()) && compare(dto.getProvince(), removeDto.getProvince())
                        && compare(dto.getIpType(), removeDto.getIpType()) && compare(dto.getIpLabel(), removeDto.getIpLabel())) {
                    list.remove(dto);
                    break;
                }
            }
        }
        return list;
    }

    private boolean compare(Object a, Object b) {
        if (!DataUtil.isNullObject(a)) {
            if (a.equals(b)) {
                return true;
            } else {
                return false;
            }
        } else if (DataUtil.isNullObject(a) && DataUtil.isNullObject(b)) {
            return true;
        } else {
            return false;
        }
    }

    public List<BrasDTO> getFilterBras() {
        return filterBras;
    }

    public void setFilterBras(List<BrasDTO> filterBras) {
        this.filterBras = filterBras;
    }

    public List<DomainDTO> getFilterDomain() {
        return filterDomain;
    }

    public void setFilterDomain(List<DomainDTO> filterDomain) {
        this.filterDomain = filterDomain;
    }

    public DomainDTO getCurrentDomain() {
        return currentDomain;
    }

    public void setCurrentDomain(DomainDTO currentDomain) {
        this.currentDomain = currentDomain;
    }

    public BrasDTO getCurrentBras() {
        return currentBras;
    }

    public void setCurrentBras(BrasDTO currentBras) {
        this.currentBras = currentBras;
    }

    public List<BrasDTO> getListBras() {
        return listBras;
    }

    public void setListBras(List<BrasDTO> listBras) {
        this.listBras = listBras;
    }

    public BrasIppoolDTO getBrasIppoolSearch() {
        return brasIppoolSearch;
    }

    public void setBrasIppoolSearch(BrasIppoolDTO brasIppoolSearch) {
        this.brasIppoolSearch = brasIppoolSearch;
    }

    public BrasIppoolDTO getBrasIppoolEdit() {
        return brasIppoolEdit;
    }

    public void setBrasIppoolEdit(BrasIppoolDTO brasIppoolEdit) {
        this.brasIppoolEdit = brasIppoolEdit;
    }

    public List<BrasIppoolDTO> getListSearchBrasIppool() {
        return listSearchBrasIppool;
    }

    public void setListSearchBrasIppool(List<BrasIppoolDTO> listSearchBrasIppool) {
        this.listSearchBrasIppool = listSearchBrasIppool;
    }

    public List<BrasIppoolDTO> getListAddBrasIppool() {
        return listAddBrasIppool;
    }

    public void setListAddBrasIppool(List<BrasIppoolDTO> listAddBrasIppool) {
        this.listAddBrasIppool = listAddBrasIppool;
    }

    public List<DomainDTO> getListDomain() {
        return listDomain;
    }

    public void setListDomain(List<DomainDTO> listDomain) {
        this.listDomain = listDomain;
    }

    public List<OptionSetValueDTO> getListBrasStatus() {
        return listBrasStatus;
    }

    public void setListBrasStatus(List<OptionSetValueDTO> listBrasStatus) {
        this.listBrasStatus = listBrasStatus;
    }

    public List<OptionSetValueDTO> getListIpSpecific() {
        return listIpSpecific;
    }

    public void setListIpSpecific(List<OptionSetValueDTO> listIpSpecific) {
        this.listIpSpecific = listIpSpecific;
    }

    public List<AreaDTO> getListArea() {
        return listArea;
    }

    public void setListArea(List<AreaDTO> listArea) {
        this.listArea = listArea;
    }

    public List<AreaDTO> getListAreaEdit() {
        return listAreaEdit;
    }

    public void setListAreaEdit(List<AreaDTO> listAreaEdit) {
        this.listAreaEdit = listAreaEdit;
    }

    public List<AreaDTO> getListAreaSearch() {
        return listAreaSearch;
    }

    public void setListAreaSearch(List<AreaDTO> listAreaSearch) {
        this.listAreaSearch = listAreaSearch;
    }

    public boolean isWorkModeEdit() {
        return workModeEdit;
    }

    public void setWorkModeEdit(boolean workModeEdit) {
        this.workModeEdit = workModeEdit;
    }

    public boolean isWorkModeAdd() {
        return workModeAdd;
    }

    public void setWorkModeAdd(boolean workModeAdd) {
        this.workModeAdd = workModeAdd;
    }

    public String getTrackInput() {
        return trackInput;
    }

    public void setTrackInput(String trackInput) {
        this.trackInput = trackInput;
    }

    public List<AreaDTO> getFilterListArea() {
        return filterListArea;
    }

    public void setFilterListArea(List<AreaDTO> filterListArea) {
        this.filterListArea = filterListArea;
    }

    public AreaDTO getCurentArea() {
        return curentArea;
    }

    public void setCurentArea(AreaDTO curentArea) {
        this.curentArea = curentArea;
    }

    public DomainDTO getCurrentDomainEdit() {
        return currentDomainEdit;
    }

    public void setCurrentDomainEdit(DomainDTO currentDomainEdit) {
        this.currentDomainEdit = currentDomainEdit;
    }

    public BrasDTO getCurrentBrasEdit() {
        return currentBrasEdit;
    }

    public void setCurrentBrasEdit(BrasDTO currentBrasEdit) {
        this.currentBrasEdit = currentBrasEdit;
    }

    public AreaDTO getCurentAreaEdit() {
        return curentAreaEdit;
    }

    public void setCurentAreaEdit(AreaDTO curentAreaEdit) {
        this.curentAreaEdit = curentAreaEdit;
    }

    public String getWorkMode() {
        return workMode;
    }

    public void setWorkMode(String workMode) {
        this.workMode = workMode;
    }

    public List<BrasIppoolDTO> getListSearchBrasIppoolSelecttion() {
        return listSearchBrasIppoolSelecttion;
    }

    public void setListSearchBrasIppoolSelecttion(List<BrasIppoolDTO> listSearchBrasIppoolSelecttion) {
        this.listSearchBrasIppoolSelecttion = listSearchBrasIppoolSelecttion;
    }

    public boolean isAddfile() {
        return addfile;
    }

    public void setAddfile(boolean addfile) {
        this.addfile = addfile;
    }

    public List<BrasIppoolDTO> getListImportBrasIppool() {
        return listImportBrasIppool;
    }

    public void setListImportBrasIppool(List<BrasIppoolDTO> listImportBrasIppool) {
        this.listImportBrasIppool = listImportBrasIppool;
    }

    public boolean isRender() {
        return render;
    }

    public void setRender(boolean render) {
        this.render = render;
    }

    public boolean isNotRender() {
        return notRender;
    }

    public void setNotRender(boolean notRender) {
        this.notRender = notRender;
    }

    public String getTitleAddOrCoppy() {
        return titleAddOrCoppy;
    }

    public void setTitleAddOrCoppy(String titleAddOrCoppy) {
        this.titleAddOrCoppy = titleAddOrCoppy;
    }

    public String getButtonAddOrCoppy() {
        return buttonAddOrCoppy;
    }

    public void setButtonAddOrCoppy(String buttonAddOrCoppy) {
        this.buttonAddOrCoppy = buttonAddOrCoppy;
    }

    public String getAttachFileName() {
        return attachFileName;
    }

    public void setAttachFileName(String attachFileName) {
        this.attachFileName = attachFileName;
    }

    public boolean isShowAttachFile() {
        return showAttachFile;
    }

    public void setShowAttachFile(boolean showAttachFile) {
        this.showAttachFile = showAttachFile;
    }

    public boolean isRenderAddFile() {
        return renderAddFile;
    }

    public void setRenderAddFile(boolean renderAddFile) {
        this.renderAddFile = renderAddFile;
    }

    public boolean isEnableExprotFileError() {
        return enableExprotFileError;
    }

    public void setEnableExprotFileError(boolean enableExprotFileError) {
        this.enableExprotFileError = enableExprotFileError;
    }

    public boolean isShowDeleteByFile() {
        return showDeleteByFile;
    }

    public void setShowDeleteByFile(boolean showDeleteByFile) {
        this.showDeleteByFile = showDeleteByFile;
    }

    public String getTitleHeaderByFile() {
        return titleHeaderByFile;
    }

    public void setTitleHeaderByFile(String titleHeaderByFile) {
        this.titleHeaderByFile = titleHeaderByFile;
    }

    public String getMessageConfirm() {
        return messageConfirm;
    }

    public void setMessageConfirm(String messageConfirm) {
        this.messageConfirm = messageConfirm;
    }

    public String getHeaderConfirm() {
        return headerConfirm;
    }

    public void setHeaderConfirm(String headerConfirm) {
        this.headerConfirm = headerConfirm;
    }

    public String getButtonFile() {
        return buttonFile;
    }

    public void setButtonFile(String buttonFile) {
        this.buttonFile = buttonFile;
    }

    public boolean isShowArea() {
        return showArea;
    }

    public void setShowArea(boolean showArea) {
        this.showArea = showArea;
    }

    public List<String> getStrListArea() {
        return strListArea;
    }

    public void setStrListArea(List<String> strListArea) {
        this.strListArea = strListArea;
    }

    public List<String> getStrListAreaEdit() {
        return strListAreaEdit;
    }

    public void setStrListAreaEdit(List<String> strListAreaEdit) {
        this.strListAreaEdit = strListAreaEdit;
    }

    public boolean isShowAreaEdit() {
        return showAreaEdit;
    }

    public void setShowAreaEdit(boolean showAreaEdit) {
        this.showAreaEdit = showAreaEdit;
    }

    public boolean isViewIp() {
        return viewIp;
    }

    public void setViewIp(boolean viewIp) {
        this.viewIp = viewIp;
    }
}
