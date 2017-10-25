package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.OptionSetValueService;
import com.viettel.bccs.inventory.service.SaleWs;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by hoangnt14 on 3/15/2016.
 */
@Component
@Scope("view")
@ManagedBean(name = "approveSaleAgentController")
public class ApproveSaleAgentController extends BaseController {
    @Autowired
    private ShopInfoNameable shopInfoTag;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private SaleWs saleWs;
    private int limitAutoComplete;
    private List<OptionSetValueDTO> listStatus = Lists.newArrayList();
    private ApproveSaleAgentDTO saleAgentSearch = new ApproveSaleAgentDTO();
    private List<StockApproveAgentDTO> lstApproveSaleAgentDTOs = Lists.newArrayList();
    private List<StockApproveAgentDTO> lstApproveSaleAgentSelection = Lists.newArrayList();
    private List<StockApproveAgentDetailDTO> lstApproveSaleDetailDTOs = Lists.newArrayList();
    private boolean selectAll;
    private boolean detail;
    private VShopStaffDTO vShopStaffDTO;
    private StockApproveAgentDTO saleTransBankplus;

    @Secured("@")
    @PostConstruct
    public void init() {
        try {
            saleTransBankplus = null;
            lstApproveSaleDetailDTOs = Lists.newArrayList();
            detail = false;
            shopInfoTag.initAgent(this, DataUtil.safeToString(BccsLoginSuccessHandler.getStaffDTO().getShopId()));
            listStatus = optionSetValueService.getByOptionSetCode(Const.AGENT_APPROVE_SALE.APPROVE_SALE_STATUS);
            limitAutoComplete = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_AUTOCOMPLETE));
            saleAgentSearch.setEndDate(new Date());
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.set(Calendar.DAY_OF_MONTH, 1);
            saleAgentSearch.setStartDate(cal.getTime());
        } catch (Exception e) {
            logger.error(e);
            reportError("", "common.error.happened", e);
        }
    }

    @Secured("@")
    public void doSearchSaleTrans() {
        try {
            saleTransBankplus = null;
            lstApproveSaleDetailDTOs = Lists.newArrayList();
            detail = false;
            String receiverShopCode = null;
            if (!DataUtil.isNullObject(vShopStaffDTO)) {
                receiverShopCode = vShopStaffDTO.getOwnerCode();
            }
            Long approveStatus = null;
            if (!DataUtil.isNullObject(saleAgentSearch.getApproveStatus())) {
                approveStatus = DataUtil.safeToLong(saleAgentSearch.getApproveStatus());
            }
            lstApproveSaleAgentDTOs = saleWs.getStockOrderAgent(saleAgentSearch.getStartDate(), saleAgentSearch.getEndDate(),
                    BccsLoginSuccessHandler.getStaffDTO().getShopId(),
                    approveStatus, receiverShopCode);
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happened");
            topPage();
        }
    }

    @Secured("@")
    public void doReset() {
        init();
    }

    @Secured("@")
    public void showDetail(Long saleTransId) {
        try {
            lstApproveSaleDetailDTOs = saleWs.getStockOrderAgentDetail(saleTransId, saleAgentSearch.getStartDate(), saleAgentSearch.getEndDate());
            detail = true;
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happened");
            topPage();
        }
    }

    @Secured("@")
    public void receiveShop(VShopStaffDTO vShopStaffDTO) {
        this.vShopStaffDTO = vShopStaffDTO;
    }

    @Secured("@")
    public void validateApproveRequest(StockApproveAgentDTO saleTrans) {
        try {
            if (DataUtil.isNullObject(saleTrans) || DataUtil.isNullObject(saleTrans.getSaleTransId())) {
                throw new LogicException("", "approve.sale.agent.approve.value.isNull");
            }
            saleTransBankplus = saleTrans;
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happened");
            topPage();
        }

    }

    @Secured("@")
    public void approveRequest() {
        try {
            if (DataUtil.isNullObject(saleTransBankplus) || DataUtil.isNullObject(saleTransBankplus.getSaleTransId())) {
                throw new LogicException("", "approve.sale.agent.approve.value.isNull");
            }
            StaffDTO staffDTO = BccsLoginSuccessHandler.getStaffDTO();
            BaseMessage baseMessage = saleWs.approveRequest(saleTransBankplus.getSaleTransId(), saleTransBankplus.getSaleTransDate(), staffDTO.getStaffId());
            if (baseMessage.isSuccess()) {
                reportSuccess("", "approve.sale.agent.approve.success");
            } else {
                String str = getText("approve.sale.agent.approve.fail");
                reportError("", "", str + " " + baseMessage.getDescription());
            }
            doSearchSaleTrans();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happened");
            topPage();
        }
    }

    @Secured("@")
    public void validateDenyRequest(StockApproveAgentDTO saleTrans) {
        try {
            if (DataUtil.isNullObject(saleTrans) || DataUtil.isNullObject(saleTrans.getSaleTransId())) {
                throw new LogicException("", "approve.sale.agent.approve.value.isNull");
            }
            saleTransBankplus = saleTrans;
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happened");
            topPage();
        }
    }

    @Secured("@")
    public void denyRequest() {
        try {
            if (DataUtil.isNullObject(saleTransBankplus) || DataUtil.isNullObject(saleTransBankplus.getSaleTransId())) {
                throw new LogicException("", "approve.sale.agent.approve.value.isNull");
            }
            StaffDTO staffDTO = BccsLoginSuccessHandler.getStaffDTO();
            BaseMessage baseMessage = saleWs.denyRequest(saleTransBankplus.getSaleTransId(), saleTransBankplus.getSaleTransDate(), staffDTO.getStaffId(), staffDTO.getStaffCode());
            if (baseMessage.isSuccess()) {
                reportSuccess("", "approve.sale.agent.deny.success");
            } else {
                String str = getText("approve.sale.agent.deny.fail");
                reportError("", "", str + " " + baseMessage.getDescription());
            }
            doSearchSaleTrans();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happened");
            topPage();
        }
    }

    @Secured("@")
    public void selectAllRow() {
        try {
            for (StockApproveAgentDTO approveAgentDTO : lstApproveSaleAgentDTOs) {
                if (DataUtil.safeEqual(approveAgentDTO.getApproveStatus(), Const.INSPECT_APPROVE_STATUS.NOT_HAVE_APPROVE)) {
                    if (selectAll) {
                        approveAgentDTO.setSelected(true);
                    } else {
                        approveAgentDTO.setSelected(false);
                    }
                }
            }
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    @Secured("@")
    public void selectRow(ApproveStockInspectDTO dto) {
        try {
            int countChecked = 0;
            int countUnChecked = 0;
            int countNotApprove = 0;
            for (StockApproveAgentDTO approveAgentDTO : lstApproveSaleAgentDTOs) {
                if (DataUtil.safeEqual(approveAgentDTO.getApproveStatus(), Const.INSPECT_APPROVE_STATUS.NOT_HAVE_APPROVE)) {
                    if (!DataUtil.isNullObject(approveAgentDTO.isSelected()) && approveAgentDTO.isSelected()) {
                        countChecked++;
                    } else {
                        countUnChecked++;
                    }
                    countNotApprove++;
                }
            }
            // Check so luong checked
            if ((countChecked == countNotApprove) || (countUnChecked == countNotApprove)) {
                if (dto.getSelected()) {
                    selectAll = true;
                } else {
                    selectAll = false;
                }
            }
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    public boolean disableCheckAll() {
        if (!DataUtil.isNullObject(lstApproveSaleAgentDTOs)) {
            for (StockApproveAgentDTO approveAgentDTO : lstApproveSaleAgentDTOs) {
                if (DataUtil.safeEqual(approveAgentDTO.getApproveStatus(), Const.INSPECT_APPROVE_STATUS.NOT_HAVE_APPROVE)) {
                    return false;
                }
            }
        }
        return true;
    }

    public List<StockApproveAgentDTO> getLstApproveSaleAgentDTOs() {
        return lstApproveSaleAgentDTOs;
    }

    public void setLstApproveSaleAgentDTOs(List<StockApproveAgentDTO> lstApproveSaleAgentDTOs) {
        this.lstApproveSaleAgentDTOs = lstApproveSaleAgentDTOs;
    }

    public List<StockApproveAgentDTO> getLstApproveSaleAgentSelection() {
        return lstApproveSaleAgentSelection;
    }

    public void setLstApproveSaleAgentSelection(List<StockApproveAgentDTO> lstApproveSaleAgentSelection) {
        this.lstApproveSaleAgentSelection = lstApproveSaleAgentSelection;
    }

    public ApproveSaleAgentDTO getSaleAgentSearch() {
        return saleAgentSearch;
    }

    public void setSaleAgentSearch(ApproveSaleAgentDTO saleAgentSearch) {
        this.saleAgentSearch = saleAgentSearch;
    }

    public List<OptionSetValueDTO> getListStatus() {
        return listStatus;
    }

    public void setListStatus(List<OptionSetValueDTO> listStatus) {
        this.listStatus = listStatus;
    }

    public ShopInfoNameable getShopInfoTag() {
        return shopInfoTag;
    }

    public void setShopInfoTag(ShopInfoNameable shopInfoTag) {
        this.shopInfoTag = shopInfoTag;
    }

    public int getLimitAutoComplete() {
        return limitAutoComplete;
    }

    public void setLimitAutoComplete(int limitAutoComplete) {
        this.limitAutoComplete = limitAutoComplete;
    }

    public boolean isSelectAll() {
        return selectAll;
    }

    public void setSelectAll(boolean selectAll) {
        this.selectAll = selectAll;
    }

    public List<StockApproveAgentDetailDTO> getLstApproveSaleDetailDTOs() {
        return lstApproveSaleDetailDTOs;
    }

    public void setLstApproveSaleDetailDTOs(List<StockApproveAgentDetailDTO> lstApproveSaleDetailDTOs) {
        this.lstApproveSaleDetailDTOs = lstApproveSaleDetailDTOs;
    }

    public boolean isDetail() {
        return detail;
    }

    public void setDetail(boolean detail) {
        this.detail = detail;
    }

    public StockApproveAgentDTO getSaleTransBankplus() {
        return saleTransBankplus;
    }

    public void setSaleTransBankplus(StockApproveAgentDTO saleTransBankplus) {
        this.saleTransBankplus = saleTransBankplus;
    }
}
