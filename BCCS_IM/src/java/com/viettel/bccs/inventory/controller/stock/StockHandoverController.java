package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.util.FileUtil;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockDeliver;
import com.viettel.bccs.inventory.model.StockDeliverDetail;
import com.viettel.bccs.inventory.service.ShopService;
import com.viettel.bccs.inventory.service.StockDeliverDetailService;
import com.viettel.bccs.inventory.service.StockDeliverService;
import com.viettel.bccs.inventory.service.StockTransVofficeService;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.bccs.inventory.tag.SignOfficeTagNameable;
import com.viettel.bccs.inventory.tag.StaffInfoNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.voffice.autosign.Ver3AutoSign;
import com.viettel.voffice.autosign.ws.FileAttachTranfer;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * Created by hoangnt14 on 1/21/2017.
 */
@Component
@Scope("view")
@ManagedBean(name = "stockHandoverController")
public class StockHandoverController extends TransCommonController {

    private int limitAutoComplete;
    private StockDeliverDTO stockDeliverSearchDTO;
    private ShopDTO shopDeliverDTO;
    private StaffDTO staffDeliveryDTO;
    private StaffDTO staffDeliveryOwnerDTO;
    private boolean checkShopKeeper;
    private List<StockDeliverDetailDTO> lstStockDeliverDetailDTOs;
    private boolean viewStock;
    private StockDeliverDTO deliverHistorySearchDTO;
    private List<StockDeliverDTO> lstDeliverHistory;
    private boolean createStockDeliver;
    private boolean haveDeliver;

    @Autowired
    private StaffInfoNameable staffInfoTagRecieve;

    @Autowired
    private StaffInfoNameable staffInfoTagOwnerRecieve;

    @Autowired
    private ShopService shopService;

    @Autowired
    private SignOfficeTagNameable writeOfficeTag;//khai bao tag ky vOffice

    @Autowired
    private StockDeliverDetailService stockDeliverDetailService;

    @Autowired
    private StockDeliverService stockDeliverService;

    @Autowired
    private Ver3AutoSign ver3AutoSign;

    @Autowired
    private StockTransVofficeService stockTransVofficeService;

    @PostConstruct
    public void init() {
        try {
            checkShopKeeper = true;
            haveDeliver = false;
            createStockDeliver = false;
            shopDeliverDTO = shopService.getShopByShopKeeper(getStaffDTO().getStaffId());
            if (DataUtil.isNullObject(shopDeliverDTO)) {
                checkShopKeeper = false;
                reportError("", "", "mn.stock.handover.not.exsit.keeper.or.director.shop");
                return;
            }
            if (DataUtil.isNullOrZero(shopDeliverDTO.getShopDirectorId())) {
                checkShopKeeper = false;
                reportError("", "", "mn.stock.handover.director.staff.not.found");
                return;
            }
            StockDeliverDTO stockDeliverCheck = stockDeliverService.getStockDeliverByOwnerIdAndStatus(shopDeliverDTO.getShopId(), Const.STOCK_DELIVER.STOCK_DELIVER_STATUS_NEW);
            if (!DataUtil.isNullObject(stockDeliverCheck)) {
                haveDeliver = true;
            }
            initDataSearch();
            //

        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            reportError("", "common.error.happened", e);
            topPage();
        }
    }

    @Secured("@")
    public void viewStock() {
        try {
            lstStockDeliverDetailDTOs = stockDeliverDetailService.viewStockTotalFull(stockDeliverSearchDTO.getOwnerId(), Const.OWNER_TYPE.SHOP_LONG);
            viewStock = true;
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

    private void initDataSearch() {
        Date sysDate = getSysdateFromDB();
        deliverHistorySearchDTO = new StockDeliverDTO();
        deliverHistorySearchDTO.setOwnerId(shopDeliverDTO.getShopId());
        deliverHistorySearchDTO.setOwnerType(Const.OWNER_TYPE.SHOP_LONG);
        deliverHistorySearchDTO.setFromDate(sysDate);
        deliverHistorySearchDTO.setToDate(sysDate);
        deliverHistorySearchDTO.setStatus(Const.STOCK_DELIVER.STOCK_DELIVER_STATUS_NEW);
        lstDeliverHistory = Lists.newArrayList();
    }

    @Secured("@")
    public void doResetHistory() {
        initDataSearch();
    }

    @Secured("@")
    public void doCreateDeliver() {
        try {
            createStockDeliver = true;
            StockDeliverDTO stockDeliverCheck = stockDeliverService.getStockDeliverByOwnerIdAndStatus(shopDeliverDTO.getShopId(), Const.STOCK_DELIVER.STOCK_DELIVER_STATUS_NEW);
            if (!DataUtil.isNullObject(stockDeliverCheck)) {
                haveDeliver = true;
            }
            limitAutoComplete = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_AUTOCOMPLETE));
            writeOfficeTag.init(this, getStaffDTO().getShopId());
            doReset();
            getStaffDelivery();
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
    public void doComeBack() {
        createStockDeliver = false;
        doSearchHistory();
    }


    @Secured("@")
    public void resetCode() {
        try {
            stockDeliverSearchDTO.setCode("BBBG_" + stockDeliverService.getMaxId());
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
    public void doSearchHistory() {
        try {
            if (DataUtil.isNullObject(deliverHistorySearchDTO.getFromDate())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "mn.stock.from.date.not.blank");
            }
            if (DataUtil.isNullObject(deliverHistorySearchDTO.getToDate())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "mn.stock.to.date.not.blank");
            }
            long day = DateUtil.daysBetween2Dates(deliverHistorySearchDTO.getToDate(), deliverHistorySearchDTO.getFromDate());
            if (day < 0 || day > 30) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.trans.from.to.valid");
            }
            //search
            lstDeliverHistory = stockDeliverService.searchHistoryStockDeliver(deliverHistorySearchDTO.getCode(),
                    deliverHistorySearchDTO.getFromDate(), deliverHistorySearchDTO.getToDate(), DataUtil.safeToString(deliverHistorySearchDTO.getStatus()),
                    shopDeliverDTO.getShopId(), Const.OWNER_TYPE.SHOP_LONG);
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
    public StreamedContent downloadFileAttach(Long stockDeliverId) {
        try {
            StockTransVofficeDTO stockTransVofficeDTO = stockTransVofficeService.findStockTransVofficeByActionId(stockDeliverId);
            if (DataUtil.isNullObject(stockTransVofficeDTO)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_PERMISSION, "mn.stock.deliver.voffice.file.valid.content");
            }
            ver3AutoSign.setAppCode("BCCS2_IM");
            FileAttachTranfer fileAttachTranfer = ver3AutoSign.getFileAttach(stockTransVofficeDTO.getStockTransVofficeId());
            ver3AutoSign.setAppCode("BCCSCA");
            byte[] content = null;
            if (fileAttachTranfer != null && fileAttachTranfer.getAttachBytes() != null) {
                content = fileAttachTranfer.getAttachBytes();
            }
            if (content == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_PERMISSION, "mn.stock.deliver.voffice.file.valid.content");
            }
            return FileUtil.getStreamedContent(content, "trinh_ky_ban_giao_kho.pdf");
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happened");
        }
        return null;
    }

    @Secured("@")
    public void doDeliverValidate() {
        try {
            if (!viewStock) {
                throw new LogicException("", "mn.stock.handover.deliver.not.view.stock");
            }
            if (!DataUtil.isNullOrEmpty(lstStockDeliverDetailDTOs)) {
                for (StockDeliverDetailDTO stockDeliverDetailDTO : lstStockDeliverDetailDTOs) {
                    if (!DataUtil.safeEqual(stockDeliverDetailDTO.getQuantityReal(), stockDeliverDetailDTO.getQuantitySys())) {
                        throw new LogicException("", "mn.stock.handover.not.equal.quantity.sys.real", stockDeliverDetailDTO.getProductCode() + " - " + stockDeliverDetailDTO.getProductName() + " - " + stockDeliverDetailDTO.getStateName());
                    }
                }
            }

            //check trung ben giao va ben nhan
//            if (DataUtil.safeEqual(staffDeliveryDTO.getStaffId(), stockDeliverSearchDTO.getNewStaffId())
//                    && DataUtil.safeEqual(staffDeliveryOwnerDTO.getStaffId(), stockDeliverSearchDTO.getNewStaffOwnerId())) {
//                throw new LogicException("", "mn.stock.handover.duplicate.staff");
//            }
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
    public void doDeliver() {
        try {
            StockDeliverDTO stockDeliverCheck = stockDeliverService.getStockDeliverByOwnerIdAndStatus(shopDeliverDTO.getShopId(), Const.STOCK_DELIVER.STOCK_DELIVER_STATUS_NEW);
            if (!DataUtil.isNullObject(stockDeliverCheck)) {
                throw new LogicException("", "mn.stock.handover.exsit.stock.deliver");
            }
            //sign voffice
            SignOfficeDTO signOfficeDTO = writeOfficeTag.validateVofficeAccount();
            stockDeliverSearchDTO.setUserName(signOfficeDTO.getUserName());
            stockDeliverSearchDTO.setPassWord(signOfficeDTO.getPassWord());
            stockDeliverSearchDTO.setSignFlowId(signOfficeDTO.getSignFlowId());
            //set du lieu
            stockDeliverSearchDTO.setOwnerType(Const.OWNER_TYPE.SHOP_LONG);
            stockDeliverSearchDTO.setCreateUserId(BccsLoginSuccessHandler.getStaffDTO().getStaffId());
            stockDeliverSearchDTO.setCreateUser(BccsLoginSuccessHandler.getStaffDTO().getStaffCode());
            stockDeliverSearchDTO.setCreateUserName(BccsLoginSuccessHandler.getStaffDTO().getStaffCode());
            stockDeliverSearchDTO.setOldStaffId(staffDeliveryDTO.getStaffId());
            stockDeliverSearchDTO.setOldStaffOwnerId(staffDeliveryOwnerDTO.getStaffId());
            //
            stockDeliverService.deliverStock(stockDeliverSearchDTO);
            haveDeliver = true;
            reportSuccess("", "mn.stock.handover.deliver.success");
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happened");
            topPage();
        }
        topPage();
    }

    @Secured("@")
    public void doReset() {
        try {
            viewStock = false;
            lstStockDeliverDetailDTOs = Lists.newArrayList();
            stockDeliverSearchDTO = new StockDeliverDTO();
            stockDeliverSearchDTO.setOwnerId(shopDeliverDTO.getShopId());
            stockDeliverSearchDTO.setCode("BBBG_" + stockDeliverService.getMaxId());
            List<String> lstChanelTypeId = Lists.newArrayList();
            lstChanelTypeId.add(Const.CHANNEL_TYPE_ID.CHANNEL_TYPE_STAFF);
            staffInfoTagRecieve.initStaffWithChanelTypesMode(this, DataUtil.safeToString(shopDeliverDTO.getShopId()), DataUtil.safeToString(getStaffDTO().getStaffId()),
                    lstChanelTypeId, false, Const.MODE_SHOP.CHILD_OF_PARRENT_SHOP);

            if (shopService.checkShopIsBranch(shopDeliverDTO.getShopId()) || DataUtil.safeEqual(shopDeliverDTO.getShopId(), Long.parseLong(Const.VT_SHOP_ID))) {
                staffInfoTagOwnerRecieve.initStaffWithChanelTypesMode(this, DataUtil.safeToString(shopDeliverDTO.getShopId()), DataUtil.safeToString(getStaffDTO().getStaffId()),
                        lstChanelTypeId, false, Const.MODE_SHOP.CHILD_OF_PARRENT_SHOP);
            } else {
                staffInfoTagOwnerRecieve.initStaffWithChanelTypesMode(this, DataUtil.safeToString(shopDeliverDTO.getShopId()), DataUtil.safeToString(getStaffDTO().getStaffId()),
                        lstChanelTypeId, false, Const.MODE_SHOP.CHILD_SHOP_ALL_STAFF);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            reportError("", "common.error.happened", e);
        }
    }

    @Secured("@")
    public void getStaffDelivery() throws LogicException, Exception {
        staffDeliveryDTO = staffService.findOne(shopDeliverDTO.getShopKeeperId());
        if (DataUtil.isNullObject(staffDeliveryDTO)) {
            throw new LogicException("", "mn.stock.handover.not.exsit.keeper.staff");
        }
        staffDeliveryOwnerDTO = staffService.findOne(shopDeliverDTO.getShopDirectorId());
        if (DataUtil.isNullObject(staffDeliveryOwnerDTO)) {
            throw new LogicException("", "mn.stock.handover.not.exsit.director.staff");
        }

    }

    @Secured("@")
    public void receiveStaffRecieve(VShopStaffDTO vShopStaffDTO) {
        try {
            stockDeliverSearchDTO.setNewStaffId(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happen", ex);
            topPage();
        }
    }

    @Secured("@")
    public void clearStaffRecieve() {
        stockDeliverSearchDTO.setNewStaffId(null);
    }

    @Secured("@")
    public void receiveStaffOwnerRecieve(VShopStaffDTO vShopStaffDTO) {
        try {
            stockDeliverSearchDTO.setNewStaffOwnerId(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happen", ex);
            topPage();
        }
    }

    @Secured("@")
    public void clearStaffOwnerRecieve() {
        stockDeliverSearchDTO.setNewStaffOwnerId(null);
    }

    public StaffInfoNameable getStaffInfoTagRecieve() {
        return staffInfoTagRecieve;
    }

    public void setStaffInfoTagRecieve(StaffInfoNameable staffInfoTagRecieve) {
        this.staffInfoTagRecieve = staffInfoTagRecieve;
    }

    public int getLimitAutoComplete() {
        return limitAutoComplete;
    }

    public void setLimitAutoComplete(int limitAutoComplete) {
        this.limitAutoComplete = limitAutoComplete;
    }


    public StaffInfoNameable getStaffInfoTagOwnerRecieve() {
        return staffInfoTagOwnerRecieve;
    }

    public void setStaffInfoTagOwnerRecieve(StaffInfoNameable staffInfoTagOwnerRecieve) {
        this.staffInfoTagOwnerRecieve = staffInfoTagOwnerRecieve;
    }

    public StockDeliverDTO getStockDeliverSearchDTO() {
        return stockDeliverSearchDTO;
    }

    public void setStockDeliverSearchDTO(StockDeliverDTO stockDeliverSearchDTO) {
        this.stockDeliverSearchDTO = stockDeliverSearchDTO;
    }

    public StaffDTO getStaffDeliveryDTO() {
        return staffDeliveryDTO;
    }

    public void setStaffDeliveryDTO(StaffDTO staffDeliveryDTO) {
        this.staffDeliveryDTO = staffDeliveryDTO;
    }

    public StaffDTO getStaffDeliveryOwnerDTO() {
        return staffDeliveryOwnerDTO;
    }

    public void setStaffDeliveryOwnerDTO(StaffDTO staffDeliveryOwnerDTO) {
        this.staffDeliveryOwnerDTO = staffDeliveryOwnerDTO;
    }

    public boolean isCheckShopKeeper() {
        return checkShopKeeper;
    }

    public void setCheckShopKeeper(boolean checkShopKeeper) {
        this.checkShopKeeper = checkShopKeeper;
    }

    public SignOfficeTagNameable getWriteOfficeTag() {
        return writeOfficeTag;
    }

    public void setWriteOfficeTag(SignOfficeTagNameable writeOfficeTag) {
        this.writeOfficeTag = writeOfficeTag;
    }

    public ShopDTO getShopDeliverDTO() {
        return shopDeliverDTO;
    }

    public void setShopDeliverDTO(ShopDTO shopDeliverDTO) {
        this.shopDeliverDTO = shopDeliverDTO;
    }

    public List<StockDeliverDetailDTO> getLstStockDeliverDetailDTOs() {
        return lstStockDeliverDetailDTOs;
    }

    public void setLstStockDeliverDetailDTOs(List<StockDeliverDetailDTO> lstStockDeliverDetailDTOs) {
        this.lstStockDeliverDetailDTOs = lstStockDeliverDetailDTOs;
    }

    public boolean isViewStock() {
        return viewStock;
    }

    public void setViewStock(boolean viewStock) {
        this.viewStock = viewStock;
    }

    public StockDeliverDTO getDeliverHistorySearchDTO() {
        return deliverHistorySearchDTO;
    }

    public void setDeliverHistorySearchDTO(StockDeliverDTO deliverHistorySearchDTO) {
        this.deliverHistorySearchDTO = deliverHistorySearchDTO;
    }

    public List<StockDeliverDTO> getLstDeliverHistory() {
        return lstDeliverHistory;
    }

    public void setLstDeliverHistory(List<StockDeliverDTO> lstDeliverHistory) {
        this.lstDeliverHistory = lstDeliverHistory;
    }

    public boolean isCreateStockDeliver() {
        return createStockDeliver;
    }

    public void setCreateStockDeliver(boolean createStockDeliver) {
        this.createStockDeliver = createStockDeliver;
    }

    public boolean isHaveDeliver() {
        return haveDeliver;
    }

    public void setHaveDeliver(boolean haveDeliver) {
        this.haveDeliver = haveDeliver;
    }
}
