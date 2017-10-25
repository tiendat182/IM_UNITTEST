package com.viettel.bccs.inventory.controller.limit;

import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.DebitLevelDTO;
import com.viettel.bccs.inventory.dto.OptionSetValueDTO;
import com.viettel.bccs.inventory.service.DebitLevelService;
import com.viettel.bccs.inventory.service.OptionSetValueService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
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
 * Created by ngocdm4 on 12/3/2015.
 */
@Component
@Scope("view")
@ManagedBean
public class EstablishQuotaController extends BaseController {
    @Autowired
    private DebitLevelService debitLevelService;
    @Autowired
    private OptionSetValueService optionSetValueService;
    private List<OptionSetValueDTO> optionSetValueDTOslevel;
    private List<OptionSetValueDTO> optionSetValueDTOstype;
    private List<DebitLevelDTO> lstDebitLevelDTO;
    private List<DebitLevelDTO> selectionlstDebitLevelDTO;
    private DebitLevelDTO forSearchDTO;
    private DebitLevelDTO forDeleteDTO;
    private DebitLevelDTO forAddNewDTO;
    private DebitLevelDTO forEditDTO;

    @PostConstruct
    public void init() {
        try {
            clearCache();
            optionSetValueDTOstype = optionSetValueService.getByOptionSetCode(Const.OPTION_SET.DEBIT_DAY_TYPE);
            optionSetValueDTOslevel = optionSetValueService.getByOptionSetCode(Const.OPTION_SET.DEBIT_LEVEL);
            lstDebitLevelDTO = debitLevelService.searchDebitlevel(forSearchDTO);
        } catch (LogicException le) {
            logger.error(le);
            reportError("", "", le.getDescription());
        } catch (Exception e) {
            logger.error(e);
            reportError("", "common.error.happened", e);
        }

    }

    @Secured("@")
    public void clearCache() {
        forSearchDTO = new DebitLevelDTO();
        forAddNewDTO = new DebitLevelDTO();
        forEditDTO = new DebitLevelDTO();
        selectionlstDebitLevelDTO = new ArrayList<>();
    }

    @Secured("@")
    public List<DebitLevelDTO> search() {
        try {
            selectionlstDebitLevelDTO = new ArrayList<>();
            lstDebitLevelDTO = debitLevelService.searchDebitlevel(forSearchDTO);
        } catch (Exception e) {
            logger.error(e);
            reportError("", "common.error.happened", e);
        }
        return lstDebitLevelDTO;
    }

    @Secured("@")
    public void changeSelect(DebitLevelDTO debitLevelDTO) {
        forEditDTO = DataUtil.cloneBean(debitLevelDTO);
        for (OptionSetValueDTO lst : optionSetValueDTOstype) {
            if (lst.getName().equals(forEditDTO.getDebitDayType()))
                forEditDTO.setDebitDayType(lst.getValue());
        }
        for (OptionSetValueDTO lst : optionSetValueDTOslevel) {
            if (lst.getName().equals(forEditDTO.getDebitLevel()))
                forEditDTO.setDebitLevel(lst.getValue());
        }
    }

    @Secured("@")
    public void updateDebitLevel() {
        try {
            if (forEditDTO.getDebitAmount() != null) {
                forEditDTO.setLastUpdateTime(DateUtil.sysDate());
                forEditDTO.setLastUpdateUser(BccsLoginSuccessHandler.getUserName());
                if (!isNumeric(forEditDTO.getDebitAmount().toString()) || forEditDTO.getDebitAmount().toString().length() > 19) {
                    reportError("", "", "quota.establish.validate.amount");
                    topPage();
                    return;
                }
                debitLevelService.update(forEditDTO);
                lstDebitLevelDTO = new ArrayList<>();
                lstDebitLevelDTO = debitLevelService.searchDebitlevel(forSearchDTO);
                reportSuccess("", "common.update.success");
                topPage();
            } else reportError("", "", "quota.establish.validate.value");
        } catch (Exception e) {
            reportError("", "common.error.happened", e);
            topPage();
        }
    }

    @Secured("@")
    public void addNew(Boolean workMode) {
        try {
            if (!DataUtil.isNullObject(forAddNewDTO)) {
                forAddNewDTO.setCreateDate(DateUtil.sysDate());
                forAddNewDTO.setCreateUser(BccsLoginSuccessHandler.getUserName());
                forAddNewDTO.setLastUpdateTime(DateUtil.sysDate());
                forAddNewDTO.setLastUpdateUser(BccsLoginSuccessHandler.getUserName());
                forAddNewDTO.setStatus("1");
                debitLevelService.create(forAddNewDTO);
                lstDebitLevelDTO = debitLevelService.searchDebitlevel(forSearchDTO);
                String messenge;
                if (workMode) {
                    messenge = "frmAddQuota:msgadddlg";
                } else {
                    messenge = "frmEstablishQuota:msgform";
                }
                reportSuccess(messenge, "common.add.success");
                topPage();
                forAddNewDTO = new DebitLevelDTO();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            reportError("", "common.error.happened", e);
            topPage();
        }
    }

    @Secured("@")
    public void selectitemDelete(DebitLevelDTO debitLevelDTO) {
        forDeleteDTO = DataUtil.cloneBean(debitLevelDTO);
    }

    @Secured("@")
    public void resetFrom() {
        try {
            clearCache();
            lstDebitLevelDTO = debitLevelService.searchDebitlevel(forSearchDTO);
        } catch (Exception e) {
            logger.error(e.getMessage());
            reportError("", "common.error.happened", e);
            topPage();
        }
    }

    @Secured("@")
    public void delete() {
        try {
            if (DataUtil.isNullOrEmpty(selectionlstDebitLevelDTO) && (DataUtil.isNullObject(forDeleteDTO) || forDeleteDTO.getId() == null)) {
                reportError("", "", "quota.establish.validate.select");
                topPage();
                return;
            }
            if (DataUtil.isNullOrEmpty(selectionlstDebitLevelDTO) && !DataUtil.isNullObject(forDeleteDTO)) {
                selectionlstDebitLevelDTO = new ArrayList<>();
                for (OptionSetValueDTO lst : optionSetValueDTOstype) {
                    if (lst.getName().equals(forDeleteDTO.getDebitDayType()))
                        forDeleteDTO.setDebitDayType(lst.getValue());
                }
                for (OptionSetValueDTO lst : optionSetValueDTOslevel) {
                    if (lst.getName().equals(forDeleteDTO.getDebitLevel()))
                        forDeleteDTO.setDebitLevel(lst.getValue());
                }
                selectionlstDebitLevelDTO.add(forDeleteDTO);
            }
            if (!DataUtil.isNullOrEmpty(selectionlstDebitLevelDTO)) {
                List<DebitLevelDTO> lstFilterDebitlevelDTO = new ArrayList<>();
                for (DebitLevelDTO lstFilter : selectionlstDebitLevelDTO) {
                    if (DataUtil.safeEqual(lstFilter.getStatus(), "1"))
                        lstFilterDebitlevelDTO.add(lstFilter);
                }
                for (DebitLevelDTO lstDelete : lstFilterDebitlevelDTO) {
                    lstDelete.setStatus("0");
                    lstDelete.setLastUpdateTime(DateUtil.sysDate());
                    lstDelete.setLastUpdateUser(BccsLoginSuccessHandler.getUserName());
                    for (OptionSetValueDTO lst : optionSetValueDTOstype) {
                        if (lst.getName().equals(lstDelete.getDebitDayType()))
                            lstDelete.setDebitDayType(lst.getValue());
                    }
                    for (OptionSetValueDTO lst : optionSetValueDTOslevel) {
                        if (lst.getName().equals(lstDelete.getDebitLevel()))
                            lstDelete.setDebitLevel(lst.getValue());
                    }
                    debitLevelService.update(lstDelete);
                }
                reportSuccess("", getTextParam("quota.establish.delete.multi", String.valueOf(lstFilterDebitlevelDTO.size())));
                lstDebitLevelDTO = debitLevelService.searchDebitlevel(forSearchDTO);
                forDeleteDTO = new DebitLevelDTO();
                selectionlstDebitLevelDTO = new ArrayList<>();
                topPage();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            reportError("", "common.error.happened", e);
            topPage();
        }
    }

    @Secured("@")
    public void validateBeforeAdd() {
        try {
            DebitLevelDTO debitLevelDTO = new DebitLevelDTO();
            debitLevelDTO.setDebitDayType(forAddNewDTO.getDebitDayType());
            debitLevelDTO.setDebitLevel(forAddNewDTO.getDebitLevel());
            if (!isNumeric(forAddNewDTO.getDebitAmount().toString()) || forAddNewDTO.getDebitAmount().toString().length() > 19) {
                reportError("", "", "quota.establish.validate.amount");
                topPage();
                return;
            }
            if (!DataUtil.isNullOrEmpty(debitLevelService.searchDebitlevel(debitLevelDTO))) {
                reportError("", "", "quota.establish.validate.duplicate");
                topPage();
                return;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            reportError("", "common.error.happened", e);
            topPage();
        }
    }

    @Secured("@")
    public boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    @Secured("@")
    public void validateListDelete() {
        if (DataUtil.isNullOrEmpty(selectionlstDebitLevelDTO)) {
            reportError("", "", "quota.establish.validate.select");
            selectionlstDebitLevelDTO = new ArrayList<>();
            forDeleteDTO = new DebitLevelDTO();
            topPage();
        }
    }


    public List<DebitLevelDTO> getLstDebitLevelDTO() {
        return lstDebitLevelDTO;
    }

    public void setLstDebitLevelDTO(List<DebitLevelDTO> lstDebitLevelDTO) {
        this.lstDebitLevelDTO = lstDebitLevelDTO;
    }

    public DebitLevelDTO getForEditDTO() {
        return forEditDTO;
    }

    public void setForEditDTO(DebitLevelDTO forEditDTO) {
        this.forEditDTO = forEditDTO;
    }

    public DebitLevelDTO getForAddNewDTO() {
        return forAddNewDTO;
    }

    public void setForAddNewDTO(DebitLevelDTO forAddNewDTO) {
        this.forAddNewDTO = forAddNewDTO;
    }

    public DebitLevelDTO getForSearchDTO() {
        return forSearchDTO;
    }

    public void setForSearchDTO(DebitLevelDTO forSearchDTO) {
        this.forSearchDTO = forSearchDTO;
    }

    public List<OptionSetValueDTO> getOptionSetValueDTOstype() {
        return optionSetValueDTOstype;
    }

    public void setOptionSetValueDTOstype(List<OptionSetValueDTO> optionSetValueDTOstype) {
        this.optionSetValueDTOstype = optionSetValueDTOstype;
    }

    public List<OptionSetValueDTO> getOptionSetValueDTOslevel() {
        return optionSetValueDTOslevel;
    }

    public void setOptionSetValueDTOslevel(List<OptionSetValueDTO> optionSetValueDTOslevel) {
        this.optionSetValueDTOslevel = optionSetValueDTOslevel;
    }

    public List<DebitLevelDTO> getSelectionlstDebitLevelDTO() {
        return selectionlstDebitLevelDTO;
    }

    public void setSelectionlstDebitLevelDTO(List<DebitLevelDTO> selectionlstDebitLevelDTO) {
        this.selectionlstDebitLevelDTO = selectionlstDebitLevelDTO;
    }

    public DebitLevelDTO getForDeleteDTO() {
        return forDeleteDTO;
    }

    public void setForDeleteDTO(DebitLevelDTO forDeleteDTO) {
        this.forDeleteDTO = forDeleteDTO;
    }
}
