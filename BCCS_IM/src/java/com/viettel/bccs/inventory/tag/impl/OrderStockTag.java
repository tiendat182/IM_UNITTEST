package com.viettel.bccs.inventory.tag.impl;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.inventory.tag.OrderStockTagNameable;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.web.common.controller.BaseController;
import com.viettel.web.common.security.CustomAuthenticationProvider;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.Collator;
import java.util.*;

/**
 * Created by thanhnt77 on 12/11/2015.
 */
@Service
@Scope("prototype")
public class OrderStockTag extends BaseController implements OrderStockTagNameable {
    private Boolean writeOffice = true;
    private Boolean autoCreateNote = false;
    private Object objectController;
    private StockTransInputDTO transInputDTO = new StockTransInputDTO();
    private VShopStaffDTO vShopStaffDTO = new VShopStaffDTO();
    private StaffDTO staffDTO;
    private Boolean tranLogistics = false;
    private Boolean tranfer = false;
    private Boolean isDisableEdit = false;
    private int limitAutoComplete;
    private Date currentDate;
    private RequiredRoleMap requiredRoleMap;

    private List<ReasonDTO> lsReasonDto = Lists.newArrayList();
    private List<ChannelTypeDTO> lsChannelTypeDto = Lists.newArrayList();
    private List<OptionSetValueDTO> lsPayMethod = Lists.newArrayList();
    private List<ProgramSaleDTO> lsProgramSaleDto = Lists.newArrayList();
    private List<ShopDTO> lsRegionShop;
    private boolean existLogistic;
    private boolean isExportToStaff = false;

    private boolean enableTransfer = false;

    @Autowired
    private ShopInfoNameable shopInfoReceiveTag;
    @Autowired
    private ShopInfoNameable shopInfoExportTag;
    @Autowired
    private ShopInfoNameable shopInfoExportTagIsdn;
    @Autowired
    private ShopService shopService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private ReasonService reasonService;
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private StockTransActionService stockTransActionService;
    @Autowired
    private OptionSetValueService optionSetValueService;

    private List<OptionSetValueDTO> optionSetValueDTOTransports;
    private String transportType;
    private String transportName;

    @Override
    public void init(Object objectController, Boolean writeOffice) {
        try {
            if (staffDTO == null) {
                staffDTO = BccsLoginSuccessHandler.getStaffDTO();
            }
            this.autoCreateNote = false;
            this.writeOffice = writeOffice;
            this.objectController = objectController;
            shopInfoReceiveTag.initShop(this, DataUtil.safeToString(staffDTO.getShopId()), false);
            shopInfoReceiveTag.setMultiTag(true);
            shopInfoExportTag.initShop(this, DataUtil.safeToString(staffDTO.getShopId()), true);
            shopInfoExportTagIsdn.initShop(this, DataUtil.safeToString(staffDTO.getShopId()), true);
            shopInfoExportTag.loadShop(DataUtil.safeToString(staffDTO.getShopId()), true);
            lsReasonDto = DataUtil.defaultIfNull(reasonService.getLsReasonByType(Const.REASON_TYPE.STOCK_EXP_UNDER), new ArrayList<ReasonDTO>());
            Collections.sort(lsReasonDto, new Comparator<ReasonDTO>() {
                public int compare(ReasonDTO o1, ReasonDTO o2) {
                    Collator collate = Collator.getInstance(new Locale("vi"));
                    return collate.compare(o1.getReasonName().toLowerCase(), o2.getReasonName().toLowerCase());
                }
            });
            limitAutoComplete = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_AUTOCOMPLETE));
            lsRegionShop = DataUtil.defaultIfNull(shopService.getListShopByCodeOption(Const.OPTION_SET.REGION_SHOP), new ArrayList<ShopDTO>());
            requiredRoleMap = CustomAuthenticationProvider.createRequiredRoleMap(Const.PERMISION.ROLE_SYNC_LOGISTIC, Const.PERMISION.ROLE_TRANSPORT_STOCK, Const.PERMISION.ROLE_XUAT_KHO_BA_MIEN,
                    Const.PERMISION.ROLE_AUTO_ORDER_NOTE, Const.PERMISION.ROLE_DONGBO_ERP);
            if (currentDate == null) {
                currentDate = optionSetValueService.getSysdateFromDB(true);
            }
            resetOrderStock();
            initLogistic();
            tranLogistics = getShowLogistic();

            //outsource add 29.6.2017
            tranfer = false;
            optionSetValueDTOTransports = optionSetValueService.getByOptionSetCode("TRANSPORT_TYPE");
        } catch (Exception e) {
            this.reportError("", "common.error.happened", e);
        }
    }

    private void initLogistic() throws LogicException, Exception {
        List<OptionSetValueDTO> options = DataUtil.defaultIfNull(optionSetValueService.getByOptionsetCodeAndValue(Const.OPTION_SET.LOGISTIC_SHOP_ID_LIST, staffDTO.getShopCode()), new ArrayList<>());
        existLogistic = !DataUtil.isNullOrEmpty(options);
    }

    @Override
    public void initAgent(Object objectController, Boolean writeOffice) {
        initCommon(objectController, writeOffice, Const.REASON_TYPE.STOCK_EXP_UNDER, null);
    }

    @Override
    public void initCollaborator(Object objectController) {
        isExportToStaff = true;
        initCommon(objectController, false, Const.REASON_TYPE.STOCK_EXP_COLL, Const.CHANNEL_TYPE.CHANNEL_TYPE_COLLABORATOR);
    }

    @Override
    public void initCollaboratorRetrieve(Object objectController) {
        initCommon(objectController, false, Const.REASON_TYPE.STOCK_IMP_COLL, Const.CHANNEL_TYPE.CHANNEL_TYPE_COLLABORATOR);

    }

    private void initCommon(Object objectController, Boolean writeOffice, String reasonType, Long isCollaborator) {
        try {
            if (staffDTO == null) {
                staffDTO = BccsLoginSuccessHandler.getStaffDTO();
            }
            this.writeOffice = writeOffice;
            this.autoCreateNote = false;
            this.objectController = objectController;
            if (DataUtil.safeEqual(isCollaborator, Const.CHANNEL_TYPE.CHANNEL_TYPE_COLLABORATOR)) {
                lsChannelTypeDto = DataUtil.defaultIfNull(shopService.getChannelTypeCollaborator(), new ArrayList<ChannelTypeDTO>());
//                lsProgramSaleDto = DataUtil.defaultIfNull(saleWs.getProgram(), new ArrayList<ProgramSaleDTO>());
            }
            if (DataUtil.safeEqual(isCollaborator, Const.CHANNEL_TYPE.CHANNEL_TYPE_COLLABORATOR)) {
                List<Long> lstChannelType = Lists.newArrayList();
                if (!DataUtil.isNullOrEmpty(lsChannelTypeDto)) {
                    for (ChannelTypeDTO channelTypeDTO : lsChannelTypeDto) {
                        lstChannelType.add(channelTypeDTO.getChannelTypeId());
                    }
                }
                shopInfoReceiveTag.initCollAndPointOfSale(this, DataUtil.safeToString(staffDTO.getShopId()),
                        lstChannelType, DataUtil.safeToString(staffDTO.getStaffId()));
                shopInfoExportTag.initShopAndAllChild(this, DataUtil.safeToString(staffDTO.getStaffId()), true, Const.OWNER_TYPE.STAFF);
                shopInfoExportTag.loadStaff(DataUtil.safeToString(staffDTO.getStaffId()), true);
            } else {
                shopInfoReceiveTag.initAgent(this, DataUtil.safeToString(staffDTO.getShopId()));
                shopInfoExportTag.initShopAndAllChild(this, DataUtil.safeToString(staffDTO.getShopId()), true, Const.OWNER_TYPE.STAFF);
                shopInfoExportTag.loadShop(DataUtil.safeToString(staffDTO.getShopId()), true);
            }
            lsReasonDto = DataUtil.defaultIfNull(reasonService.getLsReasonByType(reasonType), new ArrayList<ReasonDTO>());
            Collections.sort(lsReasonDto, new Comparator<ReasonDTO>() {
                public int compare(ReasonDTO o1, ReasonDTO o2) {
                    Collator collate = Collator.getInstance(new Locale("vi"));
                    return collate.compare(o1.getReasonName().toLowerCase(), o2.getReasonName().toLowerCase());
                }
            });
            limitAutoComplete = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_AUTOCOMPLETE));
            if (!DataUtil.safeEqual(isCollaborator, Const.CHANNEL_TYPE.CHANNEL_TYPE_COLLABORATOR)) {
                lsRegionShop = DataUtil.defaultIfNull(shopService.getListShopByCodeOption(Const.OPTION_SET.REGION_SHOP), new ArrayList<ShopDTO>());
            }
            requiredRoleMap = CustomAuthenticationProvider.createRequiredRoleMap(Const.PERMISION.ROLE_SYNC_LOGISTIC, Const.PERMISION.ROLE_TRANSPORT_STOCK, Const.PERMISION.ROLE_XUAT_KHO_BA_MIEN,
                    Const.PERMISION.ROLE_DONGBO_ERP);
            if (currentDate == null) {
                currentDate = optionSetValueService.getSysdateFromDB(true);
            }
            resetOrderStock();
            initLogistic();
            tranLogistics = getShowLogistic();
//            tranfer = false;

            //outsource sonat 29.6.2017
            optionSetValueDTOTransports = optionSetValueService.getByOptionSetCode("TRANSPORT_TYPE");
            tranfer = false;

        } catch (Exception e) {
            this.reportError("", "common.error.happened", e);
        }
    }

    @Override
    public void resetOrderStock() {
        transInputDTO = new StockTransInputDTO();
        if (isExportToStaff) {
            transInputDTO.setFromOwnerType(DataUtil.safeToLong(Const.OWNER_TYPE.STAFF));
            transInputDTO.setFromOwnerId(staffDTO.getStaffId());
        } else {
            transInputDTO.setFromOwnerType(DataUtil.safeToLong(Const.OWNER_TYPE.SHOP));
            transInputDTO.setFromOwnerId(staffDTO.getShopId());
        }
        transInputDTO.setCreateUser(staffDTO.getStaffCode());
        transInputDTO.setActionStaffId(staffDTO.getStaffOwnerId());
        transInputDTO.setCreateDatetime(currentDate);
    }

    /**
     * ham tra ve ten kho 3 mien
     *
     * @param regionId
     * @return
     * @author ThanhNT77
     */
    private String getRegionThree(Long regionId) {
        if (lsRegionShop != null && regionId != null) {
            for (ShopDTO shopDTO : lsRegionShop) {
                if (regionId.equals(shopDTO.getShopId())) {
                    return shopDTO.getName();
                }
            }
        }
        return "";
    }

    @Override
    public void loadOrderStock(Long stockTransActionId, boolean isDisableEdit) {
        //load thong tin PHIEU XUAT
        try {
            initLogistic();
            tranLogistics = false;
            if (transInputDTO == null) {
                transInputDTO = new StockTransInputDTO();
            }
            StockTransActionDTO transActionDTO = stockTransActionService.findOne(stockTransActionId);
            if (transActionDTO != null) {
                StockTransDTO transDTO = stockTransService.findOne(transActionDTO.getStockTransId());
                if (transDTO != null) {
                    BeanUtils.copyProperties(transDTO, transInputDTO);
                    transInputDTO.setCreateDatetime(transActionDTO.getCreateDatetime());
                    transInputDTO.setCreateUser(transActionDTO.getCreateUser());
                    transInputDTO.setActionStaffId(transActionDTO.getActionStaffId());
                    transInputDTO.setStockTransActionId(transActionDTO.getStockTransActionId());
                    transInputDTO.setActionCode(transActionDTO.getActionCode());
                    transInputDTO.setStockTransId(transDTO.getStockTransId());
                    transInputDTO.setRegionStockTransId(transDTO.getRegionStockTransId());
                    transInputDTO.setIsAutoGen(transDTO.getIsAutoGen());
                    transInputDTO.setReasonId(transDTO.getReasonId());
                    transInputDTO.setCheckErp(Const.STOCK_TRANS.IS_NOT_ERP.equals(transDTO.getCheckErp()));
                    transInputDTO.setPayStatus(transDTO.getPayStatus());
                    transInputDTO.setRegionStockId(transActionDTO.getRegionOwnerId());

                    //this.tranfer = Const.STOCK_TRANS.IS_TRANFER.equals(transDTO.getTransport());
                    //outsource
                    this.tranfer = !DataUtil.isNullOrEmpty(transDTO.getTransport());
                    this.transportType = transDTO.getTransport();
                    transInputDTO.setTransportSource(transDTO.getTransportSource());
                    //end outsource

                    this.tranLogistics = Const.STOCK_TRANS.IS_LOGISTIC.equals(transDTO.getLogicstic());
                    //load du lieu cho 2 cai tag kho xuat hang/kho nhan hang
                    if (transInputDTO.getFromOwnerId() != null) {
                        shopInfoExportTag.loadShop(DataUtil.safeToString(transInputDTO.getFromOwnerId()), true);
                    }
                    if (transInputDTO.getToOwnerId() != null) {
                        shopInfoReceiveTag.loadShop(DataUtil.safeToString(transInputDTO.getToOwnerId()), true);
                    }
                    //set reasionCode va reasonName
                    setReasonData(transDTO.getReasonId(), transInputDTO);
                    transInputDTO.setTotalAmount(transDTO.getTotalAmount());
                }
            }
            this.isDisableEdit = isDisableEdit;
            this.enableTransfer = !isDisableEdit;//outsource add
        } catch (Exception e) {
            this.reportError("", "common.error.happened", e);
        }
    }

    @Override
    public void loadOrderStockImport(Long stockTransActionId, boolean isDisableEdit) {
        //load thong tin PHIEU NHAP
        try {
            if (transInputDTO == null) {
                transInputDTO = new StockTransInputDTO();
            }
            StockTransActionDTO transActionDTO = stockTransActionService.findOne(stockTransActionId);
            if (transActionDTO != null) {
                StockTransDTO transDTO = stockTransService.findOne(transActionDTO.getStockTransId());
                if (transDTO != null) {
                    BeanUtils.copyProperties(transDTO, transInputDTO);
                    transInputDTO.setCreateDatetime(transActionDTO.getCreateDatetime());
                    transInputDTO.setCreateUser(transActionDTO.getCreateUser());
                    transInputDTO.setActionStaffId(transActionDTO.getActionStaffId());
                    transInputDTO.setStockTransActionId(transActionDTO.getStockTransActionId());
                    transInputDTO.setActionCode(transActionDTO.getActionCode());
                    transInputDTO.setStockTransId(transDTO.getStockTransId());
                    transInputDTO.setRegionStockTransId(transDTO.getRegionStockTransId());
                    transInputDTO.setRegionStockId(transActionDTO.getRegionOwnerId());
                    ReasonDTO reasonImportDTO = reasonService.findOne(transDTO.getImportReasonId());
                    transInputDTO.setReasonId(transDTO.getImportReasonId());
                    transInputDTO.setReasonName(reasonImportDTO.getReasonName());
                    transInputDTO.setNote(transDTO.getImportNote());
                    transInputDTO.setCheckErp(Const.STOCK_TRANS.IS_NOT_ERP.equals(transDTO.getCheckErp()));


                    //this.tranfer = Const.STOCK_TRANS.IS_TRANFER.equals(transDTO.getTransport());
                    //outsource
                    this.tranfer = !DataUtil.isNullOrEmpty(transDTO.getTransport());
                    this.transportType = transDTO.getTransport();
                    transInputDTO.setTransportSource(transDTO.getTransportSource());
                    //end outsource

                    this.tranLogistics = Const.STOCK_TRANS.IS_LOGISTIC.equals(transDTO.getLogicstic());
                    //load du lieu cho 2 cai tag kho xuat hang/kho nhan hang
                    if (transInputDTO.getFromOwnerId() != null) {
                        shopInfoExportTag.loadShop(DataUtil.safeToString(transInputDTO.getFromOwnerId()), true);
                    }
                    if (transInputDTO.getToOwnerId() != null) {
                        shopInfoReceiveTag.loadShop(DataUtil.safeToString(transInputDTO.getToOwnerId()), true);
                    }
                    transInputDTO.setTotalAmount(transDTO.getTotalAmount());
                }
            }
            this.isDisableEdit = isDisableEdit;
            this.enableTransfer = !isDisableEdit;//outsource add
            initLogistic();
        } catch (Exception e) {
            this.reportError("", "common.error.happened", e);
        }
    }


    /**
     * ham lay ve regionshop name
     *
     * @param regionShopId
     * @return
     * @author ThanhNT77
     */
    private String getRegionShopName(Long regionShopId, List<ShopDTO> lsShopDto) {
        if (lsShopDto != null && !DataUtil.isNullOrEmpty(lsShopDto)) {
            for (ShopDTO shop : lsShopDto) {
                if (regionShopId.equals(shop.getShopId())) {
                    return shop.getName();
                }
            }
        }
        return "";
    }

    /**
     * ham lay ve reason name, code
     *
     * @param reasonId //     * @param lsReasonDto
     * @return
     * @author ThanhNT77
     */
    private void setReasonData(Long reasonId, StockTransInputDTO transInputDTO) throws Exception {
        lsReasonDto = DataUtil.defaultIfNull(reasonService.getLsReasonByType(Const.REASON_TYPE.STOCK_EXP_UNDER), new ArrayList<ReasonDTO>());
        if (reasonId != null && !DataUtil.isNullOrEmpty(lsReasonDto)) {
            ReasonDTO reason = reasonService.findOne(reasonId);
            if (!DataUtil.isNullObject(reason)) {
                transInputDTO.setReasonCode(reason.getReasonCode());
                transInputDTO.setReasonName(reason.getReasonName());
            }
        }
    }

    @Override
    public void doChangeWriteOffice(String methodName) {
        try {
            if (DataUtil.isNullOrEmpty(methodName)) {
                return;
            }
            callBackMethod(this.objectController, methodName, writeOffice, null);
        } catch (Exception e) {
            this.reportError("", "common.error.happened", e);
        }
    }

    @Override
    public void doChangeAutoCreateNote(String methodName) {
        try {
            if (DataUtil.isNullOrEmpty(methodName)) {
                return;
            }
            callBackMethod(this.objectController, methodName, autoCreateNote, null);
        } catch (Exception e) {
            this.reportError("", "common.error.happened", e);
        }
    }

    @Override
    public void receiveShop(VShopStaffDTO vShopStaffDTO, String methodNameReceiveShop) {
        this.vShopStaffDTO = vShopStaffDTO;
        transInputDTO.setToOwnerId(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
        if (isExportToStaff) {
            transInputDTO.setToOwnerType(DataUtil.safeToLong(Const.OWNER_TYPE.STAFF));
        } else {
            transInputDTO.setToOwnerType(DataUtil.safeToLong(Const.OWNER_TYPE.SHOP));
        }
        if (DataUtil.isNullOrEmpty(methodNameReceiveShop)) {
            return;
        }
        try {
            Class<?> c = this.objectController.getClass();
            Method method = c.getMethod(methodNameReceiveShop, VShopStaffDTO.class);
            method.invoke(this.objectController, this.vShopStaffDTO);
        } catch (NoSuchMethodException ex) {
            logger.error("", ex);
            reportError("", "msg.error.happened", ex);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            reportError("", "msg.error.happened", ex);
            logger.error("", ex);
        }
    }

    @Override
    public void doChangeThreeStock(String methodThreeStock) {
        if (DataUtil.isNullOrEmpty(methodThreeStock)) {
            return;
        }
        Long ownerId = this.staffDTO.getShopId();
        String ownerType = Const.OWNER_TYPE.SHOP;
        if (transInputDTO.getRegionStockId() != null && transInputDTO.getRegionStockId() != -1L) {
            ownerId = transInputDTO.getRegionStockId();
        }
        try {
            Class<?> c = this.objectController.getClass();
            Method method = c.getMethod(methodThreeStock, Long.class, String.class);
            method.invoke(this.objectController, ownerId, ownerType);
        } catch (NoSuchMethodException ex) {
            logger.error("", ex);
            reportError("", "msg.error.happened", ex);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            reportError("", "msg.error.happened", ex);
            logger.error("", ex);
        }
    }

    @Override
    public void clearCurrentShop(String methodName) {
        this.vShopStaffDTO = null;
        try {
            Class<?> c = this.objectController.getClass();
            Method method = c.getMethod(methodName);
            method.invoke(this.objectController);
        } catch (NoSuchMethodException ex) {
            logger.error("", ex);
            reportError("", "common.error.happened", ex);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            reportError("", "common.error.happened", ex);
            logger.error("", ex);
        }
    }

    @Override
    public void clearExportShop() {
        transInputDTO.setFromOwnerId(null);
    }

    @Override
    public void clearReceiveShop() {
        transInputDTO.setToOwnerId(null);
    }

    @Override
    public void exportShop(VShopStaffDTO vShopStaffDTO) {
        this.vShopStaffDTO = vShopStaffDTO;
        transInputDTO.setFromOwnerId(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
        if (isExportToStaff) {
            transInputDTO.setFromOwnerType(DataUtil.safeToLong(Const.OWNER_TYPE.STAFF));
        } else {
            transInputDTO.setFromOwnerType(DataUtil.safeToLong(Const.OWNER_TYPE.SHOP));
        }
    }

    @Override
    public StockTransActionDTO getStockTransActionDTO() {
        StockTransActionDTO transActionDTO = new StockTransActionDTO();
        transActionDTO.setActionCode(transInputDTO.getActionCode());
        transActionDTO.setStockTransActionId(transInputDTO.getStockTransActionId());
        transActionDTO.setCreateUser(staffDTO.getStaffCode());
        transActionDTO.setActionStaffId(staffDTO.getStaffId());
        transActionDTO.setRegionOwnerId(transInputDTO.getRegionStockId());
        return transActionDTO;
    }

    @Override
    public StockTransDTO getStockTransDTO() {
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setFromOwnerId(transInputDTO.getFromOwnerId());
        stockTransDTO.setFromOwnerType(transInputDTO.getFromOwnerType());
        stockTransDTO.setFromOwnerName(staffDTO.getName());
        stockTransDTO.setUserName(BccsLoginSuccessHandler.getUserName());
        stockTransDTO.setToOwnerId(transInputDTO.getToOwnerId());
        stockTransDTO.setToOwnerType(transInputDTO.getToOwnerType());
        stockTransDTO.setStockTransId(transInputDTO.getStockTransId());
        stockTransDTO.setReasonId(transInputDTO.getReasonId());
        stockTransDTO.setRegionStockId(transInputDTO.getRegionStockId());
        stockTransDTO.setRegionStockTransId(transInputDTO.getRegionStockTransId());
        stockTransDTO.setNote(transInputDTO.getNote());
        stockTransDTO.setStockBase(transInputDTO.getStockBase());
        stockTransDTO.setCheckErp(transInputDTO.getCheckErp() ? Const.STOCK_TRANS.IS_NOT_ERP : null);
        if (!DataUtil.isNullObject(requiredRoleMap)) {
            stockTransDTO.setTransport(!requiredRoleMap.hasRole(Const.PERMISION.ROLE_TRANSPORT_STOCK) || !tranfer ? null : Const.STOCK_TRANS.IS_TRANFER);
            stockTransDTO.setIsAutoGen(!getShowLogistic() || !tranLogistics ? null : Const.STOCK_TRANS.IS_AUTO_GEN_LOGISTIC);
        }
        if (transInputDTO.getIsAutoGen() != null) {
            stockTransDTO.setIsAutoGen(transInputDTO.getIsAutoGen());
        }
        stockTransDTO.setLogicstic(!getShowLogistic() || !tranLogistics ? null : Const.STOCK_TRANS.IS_LOGISTIC);
        stockTransDTO.setChannelTypeId(transInputDTO.getChannelTypeId());
        stockTransDTO.setPayMethod(transInputDTO.getPayMethod());
        stockTransDTO.setProgramSaleId(transInputDTO.getProgramSaleId());
        stockTransDTO.setStockTransActionId(transInputDTO.getStockTransActionId());
        stockTransDTO.setExchangeStockId(transInputDTO.getExchangeStockId());
        stockTransDTO.setTotalAmount(transInputDTO.getTotalAmount());
        stockTransDTO.setPayStatus(transInputDTO.getPayStatus());
        stockTransDTO.setDepositStatus(transInputDTO.getDepositStatus());
        stockTransDTO.setStockAgent(transInputDTO.getAgentOrderType() != null ? transInputDTO.getAgentOrderType().toString() : null);

        //outsource them transporttype
        if (tranfer) {
            if (DataUtil.isNullOrEmpty(transportType)) {
                transportType = (optionSetValueDTOTransports == null || optionSetValueDTOTransports.isEmpty()) ? null : optionSetValueDTOTransports.get(0).getValue();
            }
            stockTransDTO.setTransport(transportType);
            stockTransDTO.setTransportSource(transInputDTO.getTransportSource());
        } else {
            stockTransDTO.setTransport(null);
            stockTransDTO.setTransportSource(null);
        }

        //end
        return stockTransDTO;
    }

    @Override
    public StockTransDTO getStockTransDTOForPint() {
        StockTransDTO stockTransDTO = getStockTransDTO();
        try {
            stockTransDTO.setActionCode(transInputDTO.getActionCode());
            String fromOwnerName = null;
            String fromOwnerAddress = null;
            String toOwnerName = null;
            String toOwnerAddress = null;
            if (DataUtil.safeEqual(transInputDTO.getFromOwnerType(), Const.OWNER_TYPE.SHOP_LONG)) {
                ShopDTO shopDTO = shopService.findOne(transInputDTO.getFromOwnerId());
                if (shopDTO != null) {
                    fromOwnerName = shopDTO.getName();
                    fromOwnerAddress = shopDTO.getAddress();
                }
            } else if (DataUtil.safeEqual(transInputDTO.getFromOwnerType(), Const.OWNER_TYPE.STAFF_LONG)) {
                StaffDTO staffDTO = staffService.findOne(transInputDTO.getFromOwnerId());
                if (staffDTO != null) {
                    fromOwnerName = staffDTO.getName();
                    fromOwnerAddress = staffDTO.getAddress();
                }
            }

            if (DataUtil.safeEqual(transInputDTO.getToOwnerType(), Const.OWNER_TYPE.SHOP_LONG)) {
                ShopDTO shopDTO = shopService.findOne(transInputDTO.getToOwnerId());
                if (shopDTO != null) {
                    toOwnerName = shopDTO.getName();
                    toOwnerAddress = shopDTO.getAddress();
                }
            } else if (DataUtil.safeEqual(transInputDTO.getToOwnerType(), Const.OWNER_TYPE.STAFF_LONG)) {
                StaffDTO staffDTO = staffService.findOne(transInputDTO.getToOwnerId());
                if (staffDTO != null) {
                    toOwnerName = staffDTO.getName();
                    toOwnerAddress = staffDTO.getAddress();
                }
            }
            stockTransDTO.setFromOwnerName(fromOwnerName);
            stockTransDTO.setFromOwnerAddress(fromOwnerAddress);
            stockTransDTO.setToOwnerName(toOwnerName);
            stockTransDTO.setToOwnerAddress(toOwnerAddress);
            if (!DataUtil.isNullOrZero(transInputDTO.getReasonId())) {
                ReasonDTO reasonDTO = reasonService.findOne(transInputDTO.getReasonId());
                stockTransDTO.setReasonName(reasonDTO.getReasonName());
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return stockTransDTO;
    }

    @Override
    public Boolean getShowCheckErp() {
        return requiredRoleMap != null && requiredRoleMap.hasRole(Const.PERMISION.ROLE_DONGBO_ERP);
    }

    @Override
    public Boolean getShowThreeStock() {
        return requiredRoleMap != null && requiredRoleMap.hasRole(Const.PERMISION.ROLE_XUAT_KHO_BA_MIEN);
    }

    @Override
    public Boolean getShowLogistic() {
        return existLogistic && requiredRoleMap != null && requiredRoleMap.hasRole(Const.PERMISION.ROLE_SYNC_LOGISTIC);
    }

    @Override
    public Boolean getShowTransfer() {
        return requiredRoleMap != null && requiredRoleMap.hasRole(Const.PERMISION.ROLE_TRANSPORT_STOCK);
    }

    @Override
    public Boolean getShowAutoOrderNote() {
        return requiredRoleMap != null && requiredRoleMap.hasRole(Const.PERMISION.ROLE_AUTO_ORDER_NOTE);
    }

    @Override
    public boolean getShowStockVTT() {
        if (staffDTO == null) {
            staffDTO = BccsLoginSuccessHandler.getStaffDTO();
        }
        return Const.SHOP.SHOP_VTT_ID.equals(staffDTO.getShopId());
    }

    @Override
    public void setNameThreeRegion(String typeRegion) {
        try {
            if (!DataUtil.isNullOrZero(transInputDTO.getRegionStockTransId())) {
                StockTransDTO regionTrans = stockTransService.findOne(transInputDTO.getRegionStockTransId());
                Long shopId = "1".equals(typeRegion) ? regionTrans.getFromOwnerId() : regionTrans.getToOwnerId();
                ShopDTO shopDTO = shopService.findOne(shopId);
                if (DataUtil.safeEqual(regionTrans.getIsAutoGen(), 1L)) {
                    this.transInputDTO.setRegionStockId(shopDTO.getShopId());
                } else {
                    this.transInputDTO.setExchangeStockId(shopDTO.getShopId());
                }
                this.transInputDTO.setRegionStockName(shopDTO.getName());
            }
        } catch (Exception e) {
            this.reportError("", "common.error.happened", e);
        }
    }

    //getter and setter
    public Boolean getWriteOffice() {
        return writeOffice;
    }

    public void setWriteOffice(Boolean writeOffice) {
        this.writeOffice = writeOffice;
    }

    public Object getObjectController() {
        return objectController;
    }

    public void setObjectController(Object objectController) {
        this.objectController = objectController;
    }

    public StockTransInputDTO getTransInputDTO() {
        return transInputDTO;
    }

    public void setTransInputDTO(StockTransInputDTO transInputDTO) {
        this.transInputDTO = transInputDTO;
    }

    public VShopStaffDTO getvShopStaffDTO() {
        return vShopStaffDTO;
    }

    public void setvShopStaffDTO(VShopStaffDTO vShopStaffDTO) {
        this.vShopStaffDTO = vShopStaffDTO;
    }

    public ShopInfoNameable getShopInfoReceiveTag() {
        return shopInfoReceiveTag;
    }

    public void setShopInfoReceiveTag(ShopInfoNameable shopInfoReceiveTag) {
        this.shopInfoReceiveTag = shopInfoReceiveTag;
    }

    public ShopInfoNameable getShopInfoExportTag() {
        return shopInfoExportTag;
    }

    public void setShopInfoExportTag(ShopInfoNameable shopInfoExportTag) {
        this.shopInfoExportTag = shopInfoExportTag;
    }

    public List<ReasonDTO> getLsReasonDto() {
        return lsReasonDto;
    }

    public void setLsReasonDto(List<ReasonDTO> lsReasonDto) {
        this.lsReasonDto = lsReasonDto;
    }

    public Boolean getTranLogistics() {
        return tranLogistics;
    }

    public void setTranLogistics(Boolean tranLogistics) {
        this.tranLogistics = tranLogistics;
    }

    public Boolean getTranfer() {
        return tranfer;
    }

    public void setTranfer(Boolean tranfer) {
        this.tranfer = tranfer;
    }

    public int getLimitAutoComplete() {
        return limitAutoComplete;
    }

    public void setLimitAutoComplete(int limitAutoComplete) {
        this.limitAutoComplete = limitAutoComplete;
    }

    public List<ShopDTO> getLsRegionShop() {
        return lsRegionShop;
    }

    public void setLsRegionShop(List<ShopDTO> lsRegionShop) {
        this.lsRegionShop = lsRegionShop;
    }

    public Boolean getIsDisableEdit() {
        return isDisableEdit;
    }

    public void setIsDisableEdit(Boolean isDisableEdit) {
        this.isDisableEdit = isDisableEdit;
    }

    public ShopInfoNameable getShopInfoExportTagIsdn() {
        return shopInfoExportTagIsdn;
    }

    public void setShopInfoExportTagIsdn(ShopInfoNameable shopInfoExportTagIsdn) {
        this.shopInfoExportTagIsdn = shopInfoExportTagIsdn;
    }

    public List<ChannelTypeDTO> getLsChannelTypeDto() {
        return lsChannelTypeDto;
    }

    public void setLsChannelTypeDto(List<ChannelTypeDTO> lsChannelTypeDto) {
        this.lsChannelTypeDto = lsChannelTypeDto;
    }

    @Override
    public List<OptionSetValueDTO> getLsPayMethod() {
        return lsPayMethod;
    }

    @Override
    public void setLsPayMethod(List<OptionSetValueDTO> lsPayMethod) {
        this.lsPayMethod = lsPayMethod;
    }

    @Override
    public List<ProgramSaleDTO> getLsProgramSaleDto() {
        return lsProgramSaleDto;
    }

    @Override
    public void setLsProgramSaleDto(List<ProgramSaleDTO> lsProgramSaleDto) {
        this.lsProgramSaleDto = lsProgramSaleDto;
    }

    public boolean isExportToStaff() {
        return isExportToStaff;
    }

    public void setIsExportToStaff(boolean isExportToStaff) {
        this.isExportToStaff = isExportToStaff;
    }

    @Override
    public void changeChannelType() {
        shopInfoReceiveTag.resetShop();
        shopInfoReceiveTag.setChannelTypeId(transInputDTO.getChannelTypeId());
    }

    public Boolean getAutoCreateNote() {
        return autoCreateNote;
    }

    public void setAutoCreateNote(Boolean autoCreateNote) {
        this.autoCreateNote = autoCreateNote;
    }

    public List<OptionSetValueDTO> getOptionSetValueDTOTransports() {
        return optionSetValueDTOTransports;
    }

    public void setOptionSetValueDTOTransports(List<OptionSetValueDTO> optionSetValueDTOTransports) {
        this.optionSetValueDTOTransports = optionSetValueDTOTransports;
    }

    public String getTransportType() {
        return transportType;
    }
    public String getTransportName() {
        if (optionSetValueDTOTransports != null && !optionSetValueDTOTransports.isEmpty()) {
            for (OptionSetValueDTO transfer : optionSetValueDTOTransports) {
                if (DataUtil.safeToString(transfer.getValue()).equals(getTransportType())) {
                    return transfer.getName();
                }
            }
        }
        return "";
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }

    public boolean getEnableTransfer() {
        return enableTransfer;
    }

    public void setEnableTransfer(boolean enableTransfer) {
        this.enableTransfer = enableTransfer;
    }
}
