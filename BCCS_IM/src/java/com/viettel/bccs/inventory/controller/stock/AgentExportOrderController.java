package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.ExecuteStockTransService;
import com.viettel.bccs.inventory.service.StockDepositService;
import com.viettel.bccs.inventory.tag.ListProductNameable;
import com.viettel.bccs.inventory.tag.OrderStockTagNameable;
import com.viettel.bccs.inventory.tag.SignOfficeTagNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.web.common.security.CustomAuthenticationProvider;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.util.List;

/**
 * Created by anhvv4 on 14/11/2015.
 */
@Component
@Scope("view")
@ManagedBean(name = "agentExportOrderController")
public class AgentExportOrderController extends TransCommonController {
    private int tabIndex;
    private Boolean writeOffice = true;
    private StaffDTO staffDTO;
    private ShopDTO curentShopDTO;
    private RequiredRoleMap requiredRoleMap;
    @Autowired
    private OrderStockTagNameable orderStockTag;//khai bao tag thong tin lenh xuat
    @Autowired
    private SignOfficeTagNameable writeOfficeTag;//khai bao tag ky vOffice
    @Autowired
    private ListProductNameable listProductTag;//khai bao tag danh sach hang hoa
    @Autowired
    private ExecuteStockTransService executeStockTransService;
    @Autowired
    private StockDepositService stockDepositService;

    private boolean canPrint;
    private String vofficeDiv = "frmAgentExportOrder:numberTabView:agentExportPayCmd:agentExportPayCmdpnExportOffice";
    private String listProductDiv = "frmAgentExportOrder:numberTabView:agentExportPayCmd:agentExportPayCmdpnListProduct";
    private String idGuide = "agentExportPayCmdguide";
    private boolean deposit;
    private Long channelTypeId;

    @Secured("@")
    @PostConstruct
    public void init() {
        try {
            canPrint = false;
            showDialog(idGuide);
            doReset();
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    @Secured("@")
    public void doResetDeposit() {
        doReset();
        vofficeDiv = "frmAgentExportOrder:numberTabView:agentExportDepositCmd:agentExportDepositCmdpnExportOffice";
        canPrint = false;
        deposit = true;
    }

    @Secured("@")
    public void doResetPay() {
        doReset();
        canPrint = false;
        deposit = false;
    }

    @Secured("@")
    public void doReset() {
        try {
            deposit = false;
            vofficeDiv = "frmAgentExportOrder:numberTabView:agentExportPayCmd:agentExportPayCmdpnExportOffice";
            staffDTO = BccsLoginSuccessHandler.getStaffDTO();
            requiredRoleMap = CustomAuthenticationProvider.createRequiredRoleMap(Const.PERMISION.ROLE_SYNC_LOGISTIC, Const.PERMISION.ROLE_TRANSPORT_STOCK,
                    Const.PERMISION.ROLE_XUAT_KHO_BA_MIEN, Const.PERMISION.ROLE_STOCK_NUM_SHOP);
            //init cho vung thong tin ky vOffice
            writeOfficeTag.init(this, staffDTO.getShopId());
            //init cho vung thong tin lenh xuat
            writeOffice = true;
            orderStockTag.initAgent(this, writeOffice);
//            listProductTag.initAgent(this, Const.STOCK_TRANS_TYPE.EXPORT, staffDTO.getShopId());
            listProductTag.init(this, new ConfigListProductTagDTO(Const.MODE_SERIAL.MODE_NO_SERIAL, staffDTO.getShopId(), Const.OWNER_TYPE.SHOP));
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            topReportError("", "common.error.happen", ex);
            topPage();
        }
    }

    @Secured("@")
    public void doCreateAgentExportCmd(String actionType) {
        try {
            //validate ngay nhap phai la dinh dang so, va phai la so nguyen duong
            StockTransActionDTO stockTransActionDTO = orderStockTag.getStockTransActionDTO();
            StockTransDTO stockTransDTO = orderStockTag.getStockTransDTO();
            if (writeOffice) {
                SignOfficeDTO signOfficeDTO = writeOfficeTag.validateVofficeAccount();
                stockTransDTO.setUserName(signOfficeDTO.getUserName());
//                String passwordEncrypt1 = PassWordUtil.getInstance().encrypt(signOfficeDTO.getPassWord());
//                String passwordEncrypt2 = EncryptionUtils.encrypt(passwordEncrypt1, EncryptionUtils.getKey());
                stockTransDTO.setPassWord(signOfficeDTO.getPassWord());
                stockTransDTO.setSignFlowId(signOfficeDTO.getSignFlowId());
                stockTransDTO.setSignVoffice(writeOffice);
            }
            List<StockTransDetailDTO> stockTransDetailDTOs = listProductTag.getListTransDetailDTOs();

            if (DataUtil.isNullOrEmpty(stockTransDetailDTOs)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "export.stock.goods.not.found.list.prod");
            }
            stockTransDTO.setStockAgent(actionType);
            //validate max_stock trong stock_deposit
            for (StockTransDetailDTO stockTransDetailDTO : stockTransDetailDTOs) {
                StockDepositDTO stockDepositDTO = stockDepositService.getStockDeposit(stockTransDetailDTO.getProdOfferId(), channelTypeId, DataUtil.safeToLong(actionType));
                if (DataUtil.isNullObject(stockDepositDTO)) {
                    throw new LogicException("", "agent.export.order.not.config.stock.deposit", stockTransDetailDTO.getProdOfferName());
                }
                if (stockTransDetailDTO.getQuantity() > stockDepositDTO.getMaxStock()) {
                    throw new LogicException("", "agent.export.order.stock.deposit.max", stockTransDetailDTO.getProdOfferName(), stockDepositDTO.getMaxStock());
                }
            }
            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.ORDER_AGENT, Const.STOCK_TRANS_TYPE.EXPORT, stockTransDTO, stockTransActionDTO, stockTransDetailDTOs, requiredRoleMap);
            if (!DataUtil.isNullOrEmpty(message.getErrorCode())) {
                throw new LogicException(message.getErrorCode(), message.getKeyMsg(), message.getParamsMsg());
            }
            reportSuccess("", "export.order.create.success");
            canPrint = true;
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
    public void doCreateAgentExportCmdDeposit() {
        doCreateAgentExportCmd(Const.STOCK_TRANS.STOCK_TRANS_DEPOSIT);
    }

    @Secured("@")
    public void doCreateAgentExportCmdSale() {
        doCreateAgentExportCmd(Const.STOCK_TRANS.STOCK_TRANS_PAY);
    }

    @Secured("@")
    public void doValidateAgentExportCmd() {
    }

    @Secured("@")
    public void onTabChange(TabChangeEvent event) {
        canPrint = false;
        doReset();
        TabView tabView = (TabView) event.getComponent();
        tabIndex = tabView.getChildren().indexOf(event.getTab());
        if (tabIndex == 0) {
            vofficeDiv = "frmAgentExportOrder:numberTabView:agentExportPayCmd:agentExportPayCmdpnExportOffice";
            listProductDiv = "frmAgentExportOrder:numberTabView:agentExportPayCmd:agentExportPayCmdpnListProduct";
            deposit = false;
            idGuide = "agentExportPayCmdguide";
        } else {
            vofficeDiv = "frmAgentExportOrder:numberTabView:agentExportDepositCmd:agentExportDepositCmdpnExportOffice";
            listProductDiv = "frmAgentExportOrder:numberTabView:agentExportDepositCmd:agentExportDepositCmdpnListProduct";
            deposit = true;
            idGuide = "agentExportDepositCmdguide";
        }
    }

    public List<String> getLsStringGuide() {
        return Lists.newArrayList(getText("user.guider.link.export.agent"));
    }

    @Secured("@")
    public void doReceiveThreeStock(Long ownerId, String ownerType) {
        try {
            //init cho tag list danh sach hang hoa
            ConfigListProductTagDTO configListProductTagDTO = new ConfigListProductTagDTO(Const.MODE_SERIAL.MODE_NO_SERIAL, ownerId, ownerType);
            if (!DataUtil.isNullObject(curentShopDTO) && !DataUtil.isNullOrZero(curentShopDTO.getShopId()) && !DataUtil.isNullOrZero(curentShopDTO.getChannelTypeId())) {
                configListProductTagDTO.setChannelTypeId(DataUtil.safeToLong(curentShopDTO.getChannelTypeId()));
                if (deposit) {
                    configListProductTagDTO.setType(Const.CONFIG_PRODUCT.TYPE_DEPOSIT);
                } else {
                    configListProductTagDTO.setType(Const.CONFIG_PRODUCT.TYPE_PAY);
                    Long branchId = shopService.getBranchId(staffDTO.getShopId());
                    configListProductTagDTO.setBranchId(branchId);
                }
            }
            listProductTag.init(this, configListProductTagDTO);
            updateElemetId(listProductDiv);
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    @Secured("@")
    public void receiveWriteOffice(Boolean writeOffice) {
        this.writeOffice = writeOffice;
        updateElemetId(vofficeDiv);
    }

    // Ham in lenh xuat excel
    @Secured("@")
    public StreamedContent exportStockTransDetail() {
        try {
            StockTransDTO stockTransDTO = orderStockTag.getStockTransDTOForPint();
            if (stockTransDTO != null && canPrint) {
                List<StockTransDetailDTO> stockTransDetailDTOs = listProductTag.getListTransDetailDTOs();
                stockTransDTO.setActionType(Const.STOCK_TRANS_ACTION_TYPE.COMMAND);
                stockTransDTO.setStockTransStatus(Const.STOCK_TRANS_TYPE.EXPORT);
                return exportStockTransDetail(stockTransDTO, stockTransDetailDTOs);
            } else {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stockTrans.dto.print.not.exists");
            }
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happen", ex);
        }
        return null;
    }

    @Secured("@")
    public void doReceiveShop(VShopStaffDTO vShopStaffDTO) {
        try {
            curentShopDTO = new ShopDTO();
            curentShopDTO.setShopId(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
            curentShopDTO.setName(vShopStaffDTO.getOwnerName());
            curentShopDTO.setShopCode(vShopStaffDTO.getOwnerCode());
            curentShopDTO.setChannelTypeId(DataUtil.safeToLong(vShopStaffDTO.getChannelTypeId()));
            // Load lai hang hoa
            ConfigListProductTagDTO configListProductTagDTO = new ConfigListProductTagDTO(Const.MODE_SERIAL.MODE_NO_SERIAL, staffDTO.getShopId(), Const.OWNER_TYPE.SHOP);
            Long branchId = shopService.getBranchId(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
            configListProductTagDTO.setBranchId(branchId);
            if (!DataUtil.isNullObject(orderStockTag.getTransInputDTO()) &&
                    !DataUtil.isNullOrZero(orderStockTag.getTransInputDTO().getRegionStockId())) {
                configListProductTagDTO.setOwnerId(orderStockTag.getTransInputDTO().getRegionStockId());
            }
            configListProductTagDTO.setChannelTypeId(DataUtil.safeToLong(vShopStaffDTO.getChannelTypeId()));
            channelTypeId = DataUtil.safeToLong(vShopStaffDTO.getChannelTypeId());
            if (deposit) {
                configListProductTagDTO.setType(Const.CONFIG_PRODUCT.TYPE_DEPOSIT);
            } else {
                configListProductTagDTO.setType(Const.CONFIG_PRODUCT.TYPE_PAY);
            }
            listProductTag.init(this, configListProductTagDTO);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            topReportError("", "common.error.happen", ex);
            topPage();
        }
    }

    @Secured("@")
    public void clearCurrentShop() {
        curentShopDTO = new ShopDTO();
        if (!DataUtil.isNullObject(orderStockTag.getTransInputDTO()) &&
                !DataUtil.isNullOrZero(orderStockTag.getTransInputDTO().getRegionStockId())) {
            listProductTag.init(this, new ConfigListProductTagDTO(Const.MODE_SERIAL.MODE_NO_SERIAL, orderStockTag.getTransInputDTO().getRegionStockId(), Const.OWNER_TYPE.SHOP));
        } else {
            listProductTag.init(this, new ConfigListProductTagDTO(Const.MODE_SERIAL.MODE_NO_SERIAL, staffDTO.getShopId(), Const.OWNER_TYPE.SHOP));
        }
    }

    public int getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    public Boolean getWriteOffice() {
        return writeOffice;
    }

    public void setWriteOffice(Boolean writeOffice) {
        this.writeOffice = writeOffice;
    }

    public OrderStockTagNameable getOrderStockTag() {
        return orderStockTag;
    }

    public void setOrderStockTag(OrderStockTagNameable orderStockTag) {
        this.orderStockTag = orderStockTag;
    }

    public RequiredRoleMap getRequiredRoleMap() {
        return requiredRoleMap;
    }

    public void setRequiredRoleMap(RequiredRoleMap requiredRoleMap) {
        this.requiredRoleMap = requiredRoleMap;
    }

    public StaffDTO getStaffDTO() {
        return staffDTO;
    }

    public void setStaffDTO(StaffDTO staffDTO) {
        this.staffDTO = staffDTO;
    }

    public SignOfficeTagNameable getWriteOfficeTag() {
        return writeOfficeTag;
    }

    public void setWriteOfficeTag(SignOfficeTagNameable writeOfficeTag) {
        this.writeOfficeTag = writeOfficeTag;
    }

    public ListProductNameable getListProductTag() {
        return listProductTag;
    }

    public void setListProductTag(ListProductNameable listProductTag) {
        this.listProductTag = listProductTag;
    }

    public ShopDTO getCurentShopDTO() {
        return curentShopDTO;
    }

    public void setCurentShopDTO(ShopDTO curentShopDTO) {
        this.curentShopDTO = curentShopDTO;
    }

    public boolean isCanPrint() {
        return canPrint;
    }

    public void setCanPrint(boolean canPrint) {
        this.canPrint = canPrint;
    }
}
