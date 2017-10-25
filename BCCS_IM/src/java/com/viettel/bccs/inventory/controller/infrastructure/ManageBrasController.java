package com.viettel.bccs.inventory.controller.infrastructure;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.BrasDTO;
import com.viettel.bccs.inventory.dto.BrasIppoolDTO;
import com.viettel.bccs.inventory.dto.EquipmentDTO;
import com.viettel.bccs.inventory.service.BrasIppoolService;
import com.viettel.bccs.inventory.service.BrasService;
import com.viettel.bccs.inventory.service.EquipmentService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.web.common.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tuydv1 on 05/12/2015.
 */
@Component
@Scope("view")
@ManagedBean
public class ManageBrasController extends BaseController {
    @Autowired
    EquipmentService equipmentSV;

    @Autowired
    BrasService brasSV;

    @Autowired
    BrasIppoolService brasIppoolSv;

    private BrasDTO forSearchBras;
    private BrasDTO addBras;
    private BrasDTO editBras;
    private List<BrasDTO> brasList = Lists.newArrayList();
    private List<BrasDTO> brasListSelect = Lists.newArrayList();
    private List<EquipmentDTO> equipmentList = Lists.newArrayList();
    private BrasDTO brasDelete;


    @PostConstruct
    @Secured("@")
    public void init() {
        try {
            createData();
            executeCommand("updateControls();");
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", ex.getDescription());
            topPage();
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
            topPage();
        }
    }

    private void createData() throws Exception {
        forSearchBras = new BrasDTO();
        equipmentList = equipmentSV.getEquipmentBras();
        brasList = brasSV.search(new BrasDTO());
    }

    @Secured("@")
    public void searchBras() {
        try {
            brasList = brasSV.search(forSearchBras);
            topPage();
//            if (DataUtil.isNullOrEmpty(brasList)) {
//                throw new LogicException("", getText("mn.invoice.invoiceType.nodata"));
//            }
//            reportSuccess("", getTextParam(getText("mn.invoice.invoiceType.searchData"), DataUtil.safeToString(brasList.size())));
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
            topPage();
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happened");
            topPage();
        }

    }

    @Secured("@")
    public void resetForm() {
        forSearchBras = new BrasDTO();
        topPage();

    }

    @Secured("@")
    public String getEquipmentName(Long equipmentId) {
        String result = "";
        if (!DataUtil.isNullOrZero(equipmentId) && !DataUtil.isNullOrEmpty(equipmentList)) {
            for (EquipmentDTO brasConvert : equipmentList) {
                if (DataUtil.safeEqual(brasConvert.getEquipmentId(), equipmentId)) {
                    result = brasConvert.getName();
                    break;
                }
            }
        }
        return result;
    }

    @Secured("@")
    public void preAddBras() throws Exception {
        addBras = new BrasDTO();
        addBras.setStatus(Const.INVOICE_TYPE.STATUS_ACTIVE);
    }

    @Secured("@")
    public void doConfirmAdd() {
        if (DataUtil.isNullObject(addBras.getIp())) {
            reportError("", "", "mn.infrastructure.bras.ip.require.msg");
        }
        if (DataUtil.isNullObject(addBras.getCode())) {
            reportError("", "", "mn.infrastructure.bras.code.require.msg");
        }
        if (DataUtil.isNullObject(addBras.getName())) {
            reportError("", "", "mn.infrastructure.bras.name.require.msg");
        }
        topPage();

    }

    @Secured("@")
    public void addBrasProcess(boolean bl) {
        try {
            if (DataUtil.isNullObject(addBras)) {
                reportError("", "", "common.error.happened");
            } else {
                addBras = convertStrim(addBras);
                brasSV.createNewBras(addBras, BccsLoginSuccessHandler.getStaffDTO().getStaffCode());
                if (bl) {
                    reportSuccess("frmManageBras:msgInforBras", "mn.invoice.invoiceType.success.add");
                } else {
                    reportSuccess("", "mn.invoice.invoiceType.success.add");
                }
                forSearchBras = new BrasDTO();
                addBras = new BrasDTO();
                brasList = brasSV.search(forSearchBras);
                topPage();
            }
            topPage();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", ex);
            topPage();
        }

    }

    @Secured("@")
    public void doConfirmEdit() {
        if (DataUtil.isNullObject(editBras.getIp())) {
            reportError("", "", "mn.infrastructure.bras.ip.require.msg");
        }
        if (DataUtil.isNullObject(editBras.getCode())) {
            reportError("", "", "mn.infrastructure.bras.code.require.msg");
        }
        if (DataUtil.isNullObject(editBras.getName())) {
            reportError("", "", "mn.infrastructure.bras.name.require.msg");
        }
        topPage();
    }

    @Secured("@")
    public void prepareEdit(BrasDTO bras) {
        try {
            if (DataUtil.isNullObject(bras)) {
                reportError("", "", "mn.invoice.invoiceType.no.select");
            } else {
                editBras = DataUtil.cloneBean(bras);
            }
            topPage();
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happened");
            topPage();
        }
    }

    @Secured("@")
    public void editBrasProcess() {
        try {
            editBras = convertStrim(editBras);
            BaseMessage result = brasSV.update(editBras, BccsLoginSuccessHandler.getStaffDTO().getStaffCode());
            if (result.isSuccess()) {
                reportSuccess("frmManageBras:msgInforBras", "mn.invoice.invoiceType.success.edit");
                forSearchBras = new BrasDTO();
                brasList = brasSV.search(forSearchBras);
            } else {
                reportError("", "", "mn.invoice.invoiceType.unSuccess.edit");
            }
            topPage();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }

    }

    @Secured("@")
    public void prepareDelete() {
        try {
            if (DataUtil.isNullOrEmpty(brasListSelect)) {
                throw new LogicException("", "mn.invoice.invoiceType.no.select");
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happened");
        }
    }

    @Secured("@")
    public void deleteBras() {
        try {
            if (DataUtil.isNullObject(brasListSelect)) {
                reportError("", "", "mn.invoice.invoiceType.no.selected");
            } else {
                List<Long> lstId = Lists.newArrayList();
                for (BrasDTO dto : brasListSelect) {
                    if (!brasIppoolSv.checkBrasIdExist(dto.getBrasId())) {
                        lstId.add(dto.getBrasId());
                    }
                }

//                BaseMessage result = brasSV.delete(lstId);
                if (DataUtil.isNullOrEmpty(lstId)) {
                    String strResult = Integer.toString(lstId.size()) + "/" + Integer.toString(brasListSelect.size());
                    reportSuccess("frmManageBras:msgInforBras", getTextParam("quota.establish.delete.multi", strResult));
                } else {
                    BaseMessage result = brasSV.delete(lstId);
                    if (result.isSuccess()) {
                        String strResult = Integer.toString(lstId.size()) + "/" + Integer.toString(brasListSelect.size());
                        reportSuccess("frmManageBras:msgInforBras", getTextParam("quota.establish.delete.multi", strResult));
//                    reportSuccess("frmManageBras:msgInforBras", "common.msg.success.delete");

                        forSearchBras = new BrasDTO();
                        brasListSelect = null;
                        brasList = brasSV.search(forSearchBras);
                    } else {
                        reportError("frmManageBras:msgInforBras", "", "common.msg.unsuccess.delete");
                    }
                }
            }
            topPage();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", ex);
            topPage();
        }
    }


    @Secured("@")
    public void deleteBrasProcess() {
        try {
            if (DataUtil.isNullObject(brasDelete)) {
                reportError("", "", "mn.invoice.invoiceType.no.select");
            } else {
                List<Long> lstId = new ArrayList<Long>();
                lstId.add(brasDelete.getBrasId());
                BaseMessage result = brasSV.delete(lstId);
                if (result.isSuccess()) {
                    reportSuccess("", getTextParam(getText("mn.infrastructure.bras.deleteNameData"), brasDelete.getName()));
                    forSearchBras = new BrasDTO();
                    brasList = brasSV.search(forSearchBras);
                } else {
                    reportSuccess("frmManageBras:msgInforBras", "common.msg.unsuccess.delete");
                }
            }
            topPage();

        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", ex);
            topPage();
        }

    }

    @Secured("@")
    public void validateDeleteBras() {
        if (DataUtil.isNullOrEmpty(brasListSelect)) {
            reportError("", "", "mn.invoice.invoiceType.no.selected");
        }
        topPage();
    }

    public void onConfirmDelete(BrasDTO dto) {
        try {
            BrasIppoolDTO searchCheckDTO = new BrasIppoolDTO();
            searchCheckDTO.setBrasIp(dto.getBrasId());
            if (!DataUtil.isNullOrEmpty(brasIppoolSv.search(searchCheckDTO))) {
                reportError("", "", "bras.delete.isBond");
            }
            this.brasDelete = dto;
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", ex);
            topPage();
        }
    }


    private BrasDTO convertStrim(BrasDTO brasDTO) {
        if (!DataUtil.isNullObject(brasDTO)) {
            if (!DataUtil.isNullOrEmpty(brasDTO.getAaaIp())) {
                brasDTO.setAaaIp(brasDTO.getAaaIp().trim());
            }
            if (!DataUtil.isNullOrEmpty(brasDTO.getIp())) {
                brasDTO.setIp(brasDTO.getIp().trim());
            }
            if (!DataUtil.isNullOrEmpty(brasDTO.getCode())) {
                brasDTO.setCode(brasDTO.getCode().trim());
            }
            if (!DataUtil.isNullOrEmpty(brasDTO.getDescription())) {
                brasDTO.setDescription(brasDTO.getDescription().trim());
            }
            if (!DataUtil.isNullOrEmpty(brasDTO.getName())) {
                brasDTO.setName(brasDTO.getName().trim());
            }
            if (!DataUtil.isNullOrEmpty(brasDTO.getPort())) {
                brasDTO.setPort(brasDTO.getPort().trim());
            }
            if (!DataUtil.isNullOrEmpty(brasDTO.getSlot())) {
                brasDTO.setSlot(brasDTO.getSlot().trim());
            }
        }
        return brasDTO;
    }


    public BrasDTO getAddBras() {
        return addBras;
    }

    public void setAddBras(BrasDTO addBras) {
        this.addBras = addBras;
    }

    public BrasDTO getForSearchBras() {
        return forSearchBras;
    }

    public void setForSearchBras(BrasDTO forSearchBras) {
        this.forSearchBras = forSearchBras;
    }

    public BrasDTO getEditBras() {
        return editBras;
    }

    public void setEditBras(BrasDTO editBras) {
        this.editBras = editBras;
    }

    public List<BrasDTO> getBrasList() {
        return brasList;
    }

    public void setBrasList(List<BrasDTO> brasList) {
        this.brasList = brasList;
    }

    public List<EquipmentDTO> getEquipmentList() {
        return equipmentList;
    }

    public void setEquipmentList(List<EquipmentDTO> equipmentList) {
        this.equipmentList = equipmentList;
    }

    public List<BrasDTO> getBrasListSelect() {
        return brasListSelect;
    }

    public void setBrasListSelect(List<BrasDTO> brasListSelect) {
        this.brasListSelect = brasListSelect;
    }

}
