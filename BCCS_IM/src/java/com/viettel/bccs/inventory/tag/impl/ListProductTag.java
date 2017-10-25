package com.viettel.bccs.inventory.tag.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Range;
import com.viettel.bccs.fw.controller.InventoryController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.ExcellUtil;
import com.viettel.bccs.inventory.common.model.FileExportBean;
import com.viettel.bccs.inventory.common.util.FileUtil;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.OptionSetValue;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.inventory.tag.ListProductNameable;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.dto.BaseMessage;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.omnifaces.util.Faces;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by thanhnt77 on 12/11/2015.
 */
@Service
@Scope("prototype")
public class ListProductTag extends InventoryController implements ListProductNameable {

    private StockTransSerialDTO serialViewSelected;
    private Object objectController;
    private List<ListProductOfferDTO> lsListProductOfferDTO;
    private List<OptionSetValueDTO> lsProductStatus;
    private List<ProductOfferTypeDTO> lsProductOfferTypeDTOTmp;
    private List<ProductOfferingTotalDTO> lsProductOfferingTotalDTOTmp;
    private List<StockTransFullDTO> lsStockTransFull;
    private List<StockTransSerialDTO> listSerialView;
    private List<StockTransSerialDTO> listSerialRangeSelect = Lists.newArrayList();
    private List<OptionSetValueDTO> lsProductStatusAfter;
    private List<StockTransSerialDTO> lstErrorSerial;
    private List<StockTransFullDTO> lstErrorSerialProd;
    private List<Long> lstProdOfferId;
    private boolean isShowForRetrieveStock;

    private boolean isAddNewProduct;
    private boolean isShowSerial;
    private boolean isViewBtnExportStock;
    //danh dau import file
    private boolean errorImport = false;
    private boolean errorImportProd = false;
    private int index;
    private int limitAutoComplete;
    private int limitProductOffer;
    private Long ownerId;
    private Long totalPriceAmount = 0L;
    private String typePrint = "2";//mac dinh la chon theo dai
    private String ownerType = Const.OWNER_TYPE.SHOP;
    private String attachFileName = "";
    private String attachFileNameByProd = "";
    private byte[] byteContent;
    private byte[] byteContentByProd;
    private UploadedFile fileUpload;
    private UploadedFile fileUploadByProd;
    private ExcellUtil processingFile;
    private ExcellUtil processingFileProd;
    private ConfigListProductTagDTO configTagDto;
    private StockTransFullDTO stockTransDetail;
    private Long ownerIdExport;//ownerid cua kho xuat

    @Autowired
    private DeviceConfigService deviceConfigService;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private ProductOfferTypeService productOfferTypeService;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private ShopInfoNameable shopInfoTagExportDlg;
    @Autowired
    private StockTransSerialService stockTransSerialService;
    @Autowired
    private ProductOfferPriceService productOfferPriceService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private StockTotalService stockTotalService;
    @Autowired
    private StockBalanceRequestService stockBalanceRequestService;
    @Autowired
    private OrderDivideDeviceService orderDivideDeviceService;

    @Override
    public void init(Object objectController, ConfigListProductTagDTO configTagDto) {
        try {
            lstProdOfferId = null;
            totalPriceAmount = 0L;
            errorImport = false;
            isAddNewProduct = true;
            this.objectController = objectController;
            this.configTagDto = configTagDto;
            this.ownerId = configTagDto.getOwnerId();
            this.ownerType = configTagDto.getOwnerType();
            this.isShowSerial = !Const.MODE_SERIAL.MODE_NO_SERIAL.equals(configTagDto.getModeSerial());
            //tuydv1
            this.isShowForRetrieveStock = false;
            lsListProductOfferDTO = Lists.newArrayList();
            lsStockTransFull = Lists.newArrayList();
            lsProductStatus = DataUtil.defaultIfNull(optionSetValueService.getByOptionSetCode(Const.OPTION_SET.PRODUCT_STATUS), new ArrayList<OptionSetValueDTO>());
            if (!DataUtil.isNullOrEmpty(configTagDto.getLsProductStatus())) {
                lsProductStatus = configTagDto.getLsProductStatus();
            }
            //get max gioi han autocomplete
            limitAutoComplete = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_AUTOCOMPLETE));
            limitProductOffer = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_SHOW_PRODUCT_OFFER));
            for (int i = 0; i < limitProductOffer; i++) {
                addNewItem();
            }
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    @Override
    public void load(Object objectController, Long stockTransActionId, ConfigListProductTagDTO configTagDto) {
        try {
            errorImport = false;
            isAddNewProduct = false;
            errorImportProd = false;
            this.objectController = objectController;
            this.isShowSerial = !Const.MODE_SERIAL.MODE_NO_SERIAL.equals(configTagDto.getModeSerial());
            this.configTagDto = configTagDto;
            List<Long> lsStockTransId = Lists.newArrayList(stockTransActionId);
            lsStockTransFull = DataUtil.defaultIfNull(stockTransService.searchStockTransDetail(lsStockTransId), new ArrayList<StockTransFullDTO>());
            ownerIdExport = !DataUtil.isNullOrEmpty(lsStockTransFull) ? lsStockTransFull.get(0).getFromOwnerId() : null;
            ownerIdExport = !DataUtil.isNullOrEmpty(lsStockTransFull) ? lsStockTransFull.get(0).getFromOwnerId() : null;
            if (!DataUtil.isNullOrEmpty(lsStockTransFull) && !DataUtil.isNullOrZero(lsStockTransFull.get(0).getRegionStockTransId())
                    && Const.L_VT_SHOP_ID.equals(ownerIdExport)) {
                StockTransDTO stockTrans = stockTransService.findOne(lsStockTransFull.get(0).getRegionStockTransId());
                ownerIdExport = !DataUtil.isNullObject(stockTrans) ? stockTrans.getFromOwnerId() : ownerIdExport;
            }
            if (configTagDto.isFillAllSerial() && !DataUtil.isNullOrEmpty(lsStockTransFull)) {
                for (int i = 0; i < lsStockTransFull.size(); i++) {
                    doOpenDlgSerial(i);
                }
            }
            if (configTagDto.isAgent() || configTagDto.isRetrieveExpense()) {
                for (StockTransFullDTO stockTransFullDTO : lsStockTransFull) {
                    if (configTagDto.isAgent()) {
                        configTagDto.setPricePolicyId(Const.PRODUCT_OFFERING.PRICE_POLICY_VT);
                    }
                    List<ProductOfferPriceDTO> lstPrice = productOfferPriceService.getPriceDepositAmount(stockTransFullDTO.getProdOfferId(),
                            configTagDto.getBranchId(), configTagDto.getPricePolicyId(), configTagDto.getChannelTypeId(), null, null);

                    if (DataUtil.isNullOrEmpty(lstPrice) || DataUtil.isNullObject(lstPrice.get(0))) {
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "export.stock.coll.price.not.define");
                    }
                    ProductOfferPriceDTO productOfferPriceDTO = lstPrice.get(0);
                    stockTransFullDTO.setDepositPrice(productOfferPriceDTO.getPrice());
                }
            }
            clearFileUploadProd();
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    public Boolean getIsShowSerialByMode() {
        return !Const.MODE_SERIAL.MODE_NO_SERIAL.equals(this.configTagDto.getModeSerial());
    }

    @Override
    public List<StockTransDetailDTO> getDataListSerialDetailDto() {
        List<StockTransDetailDTO> lsDetailDTOs = Lists.newArrayList();
        for (StockTransFullDTO stockTransFullDTO : lsStockTransFull) {
            StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
            stockTransDetailDTO.setStockTransDetailId(stockTransFullDTO.getStockTransDetailId());
            stockTransDetailDTO.setProdOfferId(stockTransFullDTO.getProdOfferId());
            stockTransDetailDTO.setStateId(stockTransFullDTO.getStateId());
            stockTransDetailDTO.setQuantity(stockTransFullDTO.getQuantity());
            stockTransDetailDTO.setLstStockTransSerial(stockTransFullDTO.getLstSerial());
            stockTransDetailDTO.setTableName(stockTransFullDTO.getTableName());
            stockTransDetailDTO.setProdOfferTypeId(stockTransFullDTO.getProductOfferTypeId());

            lsDetailDTOs.add(stockTransDetailDTO);
        }
        return lsDetailDTOs;
    }

    /**
     * ham xu ly add moi item
     *
     * @throws LogicException
     * @throws Exception
     * @author ThanhNT77
     */
    private void addNewItem() throws LogicException, Exception {
        //set mac dinh 1 bang ghi trong bang
        ListProductOfferDTO listProductOfferDTO = createNewListProOffer();
        if (listProductOfferDTO != null) {
            if (isAddNewProduct && isShowSerial) {
                lsStockTransFull.add(new StockTransFullDTO());
            }
            lsListProductOfferDTO.add(listProductOfferDTO);
        }
    }

    /**
     * ham xu ly tao moi doi tuong trong bang
     *
     * @return
     * @throws LogicException
     * @throws Exception
     * @author ThanhNT77
     */
    private ListProductOfferDTO createNewListProOffer() throws LogicException, Exception {
        ListProductOfferDTO listProductOfferDTO = new ListProductOfferDTO();
        //xu ly load danh sach loai mat hang
        if (!DataUtil.isNullObject(configTagDto.getType()) && DataUtil.safeEqual(configTagDto.getType(), Const.CONFIG_PRODUCT.TYPE_DEPOSIT)) {
            lsProductOfferTypeDTOTmp = DataUtil.defaultIfNull(productOfferTypeService.getListProductOfferTypeByDeposit(ownerId, ownerType, configTagDto.getChannelTypeId(), configTagDto.getBranchId()), new ArrayList<>());
        } else if (!DataUtil.isNullObject(configTagDto.getType()) && DataUtil.safeEqual(configTagDto.getType(), Const.CONFIG_PRODUCT.TYPE_PAY)) {
            lsProductOfferTypeDTOTmp = DataUtil.defaultIfNull(productOfferTypeService.getListProductOfferTypeByPay(ownerId, ownerType, configTagDto.getChannelTypeId(), configTagDto.getBranchId()), new ArrayList<>());
        } else if (!DataUtil.isNullObject(configTagDto.getType()) && DataUtil.safeEqual(configTagDto.getType(), Const.CONFIG_PRODUCT.TYPE_DOA)) {
            lsProductOfferTypeDTOTmp = DataUtil.defaultIfNull(productOfferTypeService.getListProductOfferTypeByOwnerId(ownerId, ownerType, Const.PRODUCT_OFFER_TYPE.PRODUCT_NO_SERIAL), new ArrayList<>());
        } else if (!DataUtil.isNullObject(configTagDto.getType()) && DataUtil.safeEqual(configTagDto.getType(), Const.CONFIG_PRODUCT.TYPE_MATERIAL)) {
            lsProductOfferTypeDTOTmp = DataUtil.defaultIfNull(productOfferTypeService.getListProductOfferTypeByOwnerIdMaterial(ownerId, ownerType, Const.PRODUCT_OFFER_TYPE.PRODUCT_NO_SERIAL), new ArrayList<>());
        } else if (DataUtil.isNullObject(configTagDto.getProductOfferTable())
                && DataUtil.isNullObject(configTagDto.getStateId())) {
            lsProductOfferTypeDTOTmp = DataUtil.defaultIfNull(productOfferTypeService.getListProductOfferTypeByOwnerId(ownerId, ownerType, null), new ArrayList<>());
        } else if (!DataUtil.isNullObject(configTagDto.getStateId())) {
            lsProductOfferTypeDTOTmp = DataUtil.defaultIfNull(productOfferTypeService.getListByOwnerIdOwnerTypeState(ownerId,
                    ownerType, configTagDto.getStateId()), new ArrayList<>());
        } else {
            lsProductOfferTypeDTOTmp = DataUtil.defaultIfNull(productOfferTypeService.getListProductOfferTypeByOwnerIdAndTableName(ownerId,
                    ownerType, configTagDto.getProductOfferTable()), new ArrayList<>());

        }
        //TH loai mat hang ko co thi xu ly bao loi
        if (DataUtil.isNullOrEmpty(lsProductOfferTypeDTOTmp)) {
            if (DataUtil.safeEqual(this.ownerType, Const.OWNER_TYPE.STAFF)) {
                StaffDTO staffDTO = staffService.findOne(this.ownerId);
                if (staffDTO != null) {
                    if (Const.GOODS_STATE.RESCUE.equals(configTagDto.getStateId()) || Const.GOODS_STATE.DEMO.equals(configTagDto.getStateId())) {
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.rescueInformation.staff", staffDTO.getName());
                    } else {
                        if (DataUtil.safeEqual(configTagDto.getType(), Const.CONFIG_PRODUCT.TYPE_DEPOSIT)) {
                            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stockTrans.validate.export.stock.staff.deposit.empty", staffDTO.getName());
                        }
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stockTrans.validate.export.stock.staff.empty", staffDTO.getName());
                    }
                }
            } else {
                if (!DataUtil.isNullObject(this.ownerId)) {
                    ShopDTO shopDTO = shopService.findOne(this.ownerId);
                    if (shopDTO != null) {
                        if (DataUtil.safeEqual(configTagDto.getType(), Const.CONFIG_PRODUCT.TYPE_DEPOSIT)) {
                            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stockTrans.validate.export.stock.deposit.empty", shopDTO.getName());
                        }
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stockTrans.validate.export.stock.empty", shopDTO.getName());
                    }
                }
            }
            return listProductOfferDTO;
        } else {
            List<Long> lsProductOfferTypeIds = Lists.newArrayList();
            lsProductOfferTypeDTOTmp.forEach(obj -> lsProductOfferTypeIds.add(obj.getProductOfferTypeId()));
            listProductOfferDTO.setLsProdOfferTypeIds(lsProductOfferTypeIds);
            listProductOfferDTO.setStrStateId(Const.STOCK_STRANS_DETAIL.STATUS_NEW);
            listProductOfferDTO.setStateId(Const.GOODS_STATE.NEW);

            if (lsProductStatus != null && lsProductStatus.size() == 1) {
                listProductOfferDTO.setStrStateId(DataUtil.safeToString(lsProductStatus.get(0).getValue()));
                listProductOfferDTO.setStateId(DataUtil.safeToLong(lsProductStatus.get(0).getValue()));
            }

            lsProductOfferingTotalDTOTmp = loadProductTotal(listProductOfferDTO);


            listProductOfferDTO.setLsProductOfferTypeDTO(lsProductOfferTypeDTOTmp);
            listProductOfferDTO.setLsProductOfferingTotalDTO(lsProductOfferingTotalDTOTmp);
            // listProductOfferDTO.setProductOfferTypeDTO(!DataUtil.isNullOrEmpty(lsProductOfferTypeDTOTmp) ? lsProductOfferTypeDTOTmp.get(0) : new ProductOfferTypeDTO());
            listProductOfferDTO.setProductOfferingTotalDTO(new ProductOfferingTotalDTO());
        }
        return listProductOfferDTO;
    }


    private List<ProductOfferingTotalDTO> loadProductTotal(ListProductOfferDTO listProductOfferDTO) throws Exception {
        //hoangnt: Lay danh sach mat hang cho can kho
        List<ProductOfferingTotalDTO> lsResult;
        if (configTagDto.isBalanceStock()) {//lay danh sach mat hang can kho
            //List<Long> lstProdOfferId = Lists.newArrayList();
            if (DataUtil.isNullOrEmpty(lstProdOfferId)) {
                if (DataUtil.safeEqual(configTagDto.getBalanceStockType(), Const.STOCK_BALANCE.BALANCE_TYPE_EXPORT)) {
                    lstProdOfferId = productOfferingService.getProdOfferIdForRequestBalanceExport(ownerId, DataUtil.safeToLong(ownerType), null);
                } else {
                    if (DataUtil.safeEqual(configTagDto.getBalanceStockType(), Const.STOCK_BALANCE.BALANCE_TYPE_IMPORT)) {
                        lstProdOfferId = productOfferingService.getProdOfferIdForRequestBalanceImport(ownerId, DataUtil.safeToLong(ownerType), null);
                    }
                }
            }

            if (!DataUtil.isNullOrEmpty(lstProdOfferId)) {
                lsResult = DataUtil.defaultIfNull(productOfferingService.getLsProductOfferingDTO(listProductOfferDTO.getInputSearch(), null,
                        ownerId, ownerType, listProductOfferDTO.getStrStateId(), lstProdOfferId), Lists.newArrayList());
            } else {
                lsResult = Lists.newArrayList();
            }
        } else if (Const.PRODUCT_OFFER_TABLE.STOCK_NUMBER.equals(configTagDto.getProductOfferTable())) {//lay danh sach mat hang kho cho chuc nang kho so
            lsResult = DataUtil.defaultIfNull(productOfferingService.getLsProductOfferingIsdnDTO(listProductOfferDTO.getLsProdOfferTypeIds(), ownerId,
                    ownerType, DataUtil.safeToString(listProductOfferDTO.getStateId())), new ArrayList<>());
        } else if (Const.GOODS_STATE.RESCUE.equals(configTagDto.getStateId())) {//tuydv1 thu hoi thi lay tat ca
            lsResult = DataUtil.defaultIfNull(productOfferingService.getLsProductOfferingDTO(listProductOfferDTO.getInputSearch(), listProductOfferDTO.getLsProdOfferTypeIds(),
                    ownerId, ownerType, null, null), new ArrayList<>());
            List<ProductOfferingTotalDTO> productOfferingTotalDTOs = Lists.newArrayList();
            List<Long> prodOfferIds = Lists.newArrayList();
            for (ProductOfferingTotalDTO productOfferingTotalDTO : lsResult) {
                if (prodOfferIds.contains(productOfferingTotalDTO.getProductOfferingId())) {
                    continue;
                }
                prodOfferIds.add(productOfferingTotalDTO.getProductOfferingId());
                productOfferingTotalDTOs.add(productOfferingTotalDTO);
            }
            lsResult = productOfferingTotalDTOs;
        } else {
            if (!DataUtil.isNullObject(configTagDto.getType()) && DataUtil.safeEqual(configTagDto.getType(), Const.CONFIG_PRODUCT.TYPE_DEPOSIT)) {
                lsResult = DataUtil.defaultIfNull(productOfferingService.getLsProductOfferingDTOByPrice(listProductOfferDTO.getInputSearch(), listProductOfferDTO.getLsProdOfferTypeIds(),
                        ownerId, ownerType, listProductOfferDTO.getStrStateId(), Const.PRODUCT_OFFER_PRICE.PRICE_TYPE_DEPOSIT, configTagDto.getBranchId(), configTagDto.getChannelTypeId()), new ArrayList<>());
            } else if (!DataUtil.isNullObject(configTagDto.getType()) && DataUtil.safeEqual(configTagDto.getType(), Const.CONFIG_PRODUCT.TYPE_PAY)) {
                lsResult = DataUtil.defaultIfNull(productOfferingService.getLsProductOfferingDTOByPrice(listProductOfferDTO.getInputSearch(), listProductOfferDTO.getLsProdOfferTypeIds(),
                        ownerId, ownerType, listProductOfferDTO.getStrStateId(), Const.PRODUCT_OFFER_PRICE.PRICE_TYPE_PAY, configTagDto.getBranchId(), configTagDto.getChannelTypeId()), new ArrayList<>());
            } else if (!DataUtil.isNullObject(configTagDto.getType()) && DataUtil.safeEqual(configTagDto.getType(), Const.CONFIG_PRODUCT.TYPE_MATERIAL)) {
                lsResult = DataUtil.defaultIfNull(productOfferingService.getLsProductOfferingDTOMaterial(listProductOfferDTO.getInputSearch(), listProductOfferDTO.getLsProdOfferTypeIds(),
                        ownerId, ownerType, listProductOfferDTO.getStrStateId(), null, configTagDto.getStockModelType()), new ArrayList<>());

            } else if (configTagDto.isChangeProduct()) {
                List<ProductOfferTypeDTO> lstOfferType = productOfferTypeService.getListOfferTypeSerial();
                List<Long> lsOfferTypeIds = Lists.newArrayList();
                for (ProductOfferTypeDTO productOfferTypeDTO : lstOfferType) {
                    lsOfferTypeIds.add(productOfferTypeDTO.getProductOfferTypeId());
                }
                lsResult = DataUtil.defaultIfNull(productOfferingService.getLsProductOfferingDTO(listProductOfferDTO.getInputSearch(), lsOfferTypeIds,
                        ownerId, ownerType, listProductOfferDTO.getStrStateId(), null), new ArrayList<>());
            } else if (configTagDto.isStockOrderAgent()) {
                lsResult = DataUtil.defaultIfNull(productOfferingService.getAllLsProductOfferingDTOForProcessStock(listProductOfferDTO.getInputSearch(), Lists.newArrayList()), new ArrayList<>());
            } else if (!DataUtil.isNullObject(configTagDto.getType()) && DataUtil.safeEqual(configTagDto.getType(), Const.CONFIG_PRODUCT.TYPE_DIVIDE)) {
                lsResult = DataUtil.defaultIfNull(orderDivideDeviceService.getLsProductOfferingDTO(listProductOfferDTO.getInputSearch(),
                        ownerId, ownerType, listProductOfferDTO.getStrStateId()), new ArrayList<>());
            } else if (DataUtil.safeEqual(configTagDto.getStockDemo(), Const.STOCK_DEMO.EXPORT)) {
                lsResult = DataUtil.defaultIfNull(productOfferingService.getProductExportDemo(listProductOfferDTO.getInputSearch(), ownerId, ownerType, listProductOfferDTO.getStrStateId()), new ArrayList<>());
            } else if (DataUtil.safeEqual(configTagDto.getStockDemo(), Const.STOCK_DEMO.IMPORT)) {
                lsResult = DataUtil.defaultIfNull(productOfferingService.getProductExportDemo(listProductOfferDTO.getInputSearch(), null, null, null), new ArrayList<>());
            } else {
                lsResult = DataUtil.defaultIfNull(productOfferingService.getLsProductOfferingDTO(listProductOfferDTO.getInputSearch(), listProductOfferDTO.getLsProdOfferTypeIds(),
                        ownerId, ownerType, listProductOfferDTO.getStrStateId(), null), new ArrayList<>());
            }
        }

        return lsResult;
    }

    /**
     * them moi doi tuong add item
     *
     * @author ThanhNT77
     */
    @Override
    public void doAddItem() {
        try {
            if (lsListProductOfferDTO != null && lsListProductOfferDTO.size() == 100) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.limit.detail");
            } else if (lsListProductOfferDTO != null && configTagDto.isShowRetrive() &&
                    (lsListProductOfferDTO.size() >= 1 || (lsListProductOfferDTO.size() == 1 && DataUtil.safeToInt(lsListProductOfferDTO.get(0).getNumber()) > 1))) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.rescueInformation.validate.list.resontrue");
            }
            addNewItem();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    /**
     * ham xu ly xoa bang ghi
     *
     * @param index
     * @author ThanhNT
     */
    @Override
    public void doRemoveItem(int index) {
        Long price = lsListProductOfferDTO.get(index).getTotalPriceAmount();
        if (price > 0L) {
            totalPriceAmount -= price;
        }
        lsListProductOfferDTO.remove(index);
    }

    @Override
    public void doChangeOfferType(int index) {
        try {
            ListProductOfferDTO listProductOfferDTO = lsListProductOfferDTO.get(index);
            this.stockTransDetail = new StockTransFullDTO();
            listProductOfferDTO.setNumber(null);
            if (!DataUtil.isNullObject(listProductOfferDTO)) {
                List<ProductOfferingTotalDTO> lsProdOfferTotal = loadProductTotal(listProductOfferDTO);
                listProductOfferDTO.setLsProductOfferingTotalDTO(lsProdOfferTotal);
                listProductOfferDTO.setProductOfferingTotalDTO(new ProductOfferingTotalDTO());
            }
            doChangePrice();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    @Override
    public void doSelectProductOffering() {
        try {
            List<ProductOfferingTotalDTO> lsProductOfferingTotalDTOTmp;
            int index = DataUtil.safeToInt(UIComponent.getCurrentComponent(FacesContext.getCurrentInstance()).getAttributes().get("index"));
            ListProductOfferDTO listProductOfferDTO = lsListProductOfferDTO.get(index);
            ProductOfferingTotalDTO productOfferingTotalDTO = listProductOfferDTO.getProductOfferingTotalDTO();
            listProductOfferDTO.setProductOfferTypeDTO(productOfferTypeService.findOne(productOfferingTotalDTO.getProductOfferTypeId()));

            if (!Const.PRODUCT_OFFER_TABLE.STOCK_NUMBER.equals(configTagDto.getProductOfferTable())) {
                StockTotalDTO stockTotalDTO = stockTotalService.getStockTotalForProcessStock(productOfferingTotalDTO.getOwnerId(), DataUtil.safeToLong(productOfferingTotalDTO.getOwnerType()),
                        productOfferingTotalDTO.getProductOfferingId(), listProductOfferDTO.getStateId());
                productOfferingTotalDTO.setAvailableQuantity(stockTotalDTO != null ? stockTotalDTO.getAvailableQuantity() : 0L);
            }
            //Set list mat hang cung ma tai chinh
            lsProductOfferingTotalDTOTmp = DataUtil.defaultIfNull(productOfferingService.getLsProductOfferingForChangeProduct("",
                    productOfferingTotalDTO.getProductOfferingId(), true), new ArrayList<>());
            listProductOfferDTO.setLsProductOfferingTotalDTONew(lsProductOfferingTotalDTOTmp);
            listProductOfferDTO.setProductOfferingTotalDTONew(null);
            //lay ra gia ban le
            if (!DataUtil.isNullObject(configTagDto) && configTagDto.isCollaborator()) {
                List<ProductOfferPriceDTO> lstPrice = productOfferPriceService.getPriceDepositAmount(productOfferingTotalDTO.getProductOfferingId(),
                        null, Const.PRODUCT_OFFERING.PRICE_POLICY_VT, null, null, null);
                if (DataUtil.isNullOrEmpty(lstPrice) || DataUtil.isNullObject(lstPrice.get(0))) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "export.stock.coll.price.not.define");
                }
                ProductOfferPriceDTO productOfferPriceDTO = lstPrice.get(0);
                productOfferingTotalDTO.setPriceAmount(productOfferPriceDTO.getPrice());
            } else {
                productOfferingTotalDTO.setPriceAmount(productOfferPriceService.getPriceAmount(productOfferingTotalDTO.getProductOfferingId(),
                        Const.PRODUCT_OFFERING.PRICE_TYPE_ROOT, Const.PRODUCT_OFFERING.PRICE_POLICY_VT));
            }
            //hoangnt: Lay so lieu ERP
            if (configTagDto.isBalanceStock()) {
                List<ProductOfferingTotalDTO> lsProductOfferingTotalDTOs = Lists.newArrayList();
                if (DataUtil.safeEqual(configTagDto.getBalanceStockType(), Const.STOCK_BALANCE.BALANCE_TYPE_EXPORT)) {
                    lsProductOfferingTotalDTOs = stockBalanceRequestService.getProductOfferingExport(ownerId, DataUtil.safeToLong(ownerType), productOfferingTotalDTO.getProductOfferingId());
                } else {
                    if (DataUtil.safeEqual(configTagDto.getBalanceStockType(), Const.STOCK_BALANCE.BALANCE_TYPE_IMPORT)) {
                        lsProductOfferingTotalDTOs = stockBalanceRequestService.getProductOfferingImport(ownerId, DataUtil.safeToLong(ownerType), productOfferingTotalDTO.getProductOfferingId());
                    }
                }
                if (!DataUtil.isNullOrEmpty(lsProductOfferingTotalDTOs)) {
                    productOfferingTotalDTO.setQuantityBccs(lsProductOfferingTotalDTOs.get(0).getQuantityBccs());
                    productOfferingTotalDTO.setQuantityInspect(lsProductOfferingTotalDTOs.get(0).getQuantityInspect());
                    productOfferingTotalDTO.setQuantityFinance(lsProductOfferingTotalDTOs.get(0).getQuantityFinance());
                } else {
                    //reset
                    this.stockTransDetail = new StockTransFullDTO();
                    listProductOfferDTO.setLstSerial(null);
                    listProductOfferDTO.setNumber(null);
                    listProductOfferDTO.setProductOfferingTotalDTO(new ProductOfferingTotalDTO());
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "mn.balance.stock.request.not.diff", productOfferingTotalDTO.getCode());
                }
            }
            //reset thong tin
            this.stockTransDetail = new StockTransFullDTO();
            listProductOfferDTO.setLstSerial(null);
            listProductOfferDTO.setNumber(null);
            doChangePrice();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    public void doSelectProductOfferingNew() {
        //
    }

    @Override
    public void doChangeFinanceQuantity() {
        try {
            if (configTagDto.isBalanceStock()) {
                ListProductOfferDTO listProductOfferDTO = lsListProductOfferDTO.get(index);
                ProductOfferingTotalDTO productOfferingTotalDTO = listProductOfferDTO.getProductOfferingTotalDTO();
                if (DataUtil.safeEqual(configTagDto.getBalanceStockType(), Const.STOCK_BALANCE.BALANCE_TYPE_EXPORT)) {
                    if (!DataUtil.isNullObject(productOfferingTotalDTO.getQuantityFinance()) && productOfferingTotalDTO.getQuantityFinance() >= productOfferingTotalDTO.getQuantityBccs()) {
                        throw new LogicException("", "stock.balance.request.finance.export");
                    }
                } else {
                    if (DataUtil.safeEqual(configTagDto.getBalanceStockType(), Const.STOCK_BALANCE.BALANCE_TYPE_IMPORT)) {
                        if (!DataUtil.isNullObject(productOfferingTotalDTO.getQuantityFinance()) && productOfferingTotalDTO.getQuantityFinance() <= productOfferingTotalDTO.getQuantityBccs()) {
                            throw new LogicException("", "stock.balance.request.finance.import");
                        }
                    }
                }
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    @Override
    public void doChangePrice() {
        totalPriceAmount = 0L;
        if (!DataUtil.isNullObject(configTagDto) && configTagDto.isCollaborator()) {
            lsListProductOfferDTO.stream().filter(a -> a.getTotalPriceAmountNoState() > 0L).forEach(b -> {
                totalPriceAmount += b.getTotalPriceAmountNoState();
            });
        } else {
            lsListProductOfferDTO.stream().filter(a -> Const.GOODS_STATE.NEW.equals(a.getStateId()) && a.getTotalPriceAmount() > 0L).forEach(b -> {
                totalPriceAmount += b.getTotalPriceAmount();
            });
        }
    }

    @Override
    public List<ProductOfferingTotalDTO> doChangeOffering(String input) {
        List<ProductOfferingTotalDTO> lsProductOfferingTotalDTOTmp = Lists.newArrayList();
        try {
            int index = DataUtil.safeToInt(UIComponent.getCurrentComponent(FacesContext.getCurrentInstance()).getAttributes().get("index"));
            CharSequence inputData = DataUtil.isNullOrEmpty(input) ? null : input.trim().toUpperCase();
            ListProductOfferDTO listProductOfferDTO = lsListProductOfferDTO.get(index);
            listProductOfferDTO.setInputSearch(input);
            if (!Const.PRODUCT_OFFER_TABLE.STOCK_NUMBER.equals(configTagDto.getProductOfferTable())) {
                lsProductOfferingTotalDTOTmp = loadProductTotal(listProductOfferDTO);
                listProductOfferDTO.setLsProductOfferingTotalDTO(lsProductOfferingTotalDTOTmp);
            }
            List<ProductOfferingTotalDTO> lsAllDataTmp = Lists.newArrayList(listProductOfferDTO.getLsProductOfferingTotalDTO());
            List<ProductOfferingTotalDTO> lsAllData = Lists.newArrayList();
            for (ProductOfferingTotalDTO dto : lsAllDataTmp) {
                if (!lsAllData.contains(dto)) {
                    lsAllData.add(dto);
                }
            }
            if (inputData == null) {
                return lsAllData;
            }
            return lsAllData.stream().filter(obj -> lsAllData.contains(obj)
                    && (DataUtil.safeToString(obj.getCode()).toUpperCase().contains(inputData)
                    || DataUtil.safeToString(obj.getName()).toUpperCase().contains(inputData))).collect(Collectors.toList());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return lsProductOfferingTotalDTOTmp;
    }

    public List<ProductOfferingTotalDTO> doChangeOfferingNew(String input) {
        List<ProductOfferingTotalDTO> lsProductOfferingTotalDTOTmp = Lists.newArrayList();
        try {
            int index = DataUtil.safeToInt(UIComponent.getCurrentComponent(FacesContext.getCurrentInstance()).getAttributes().get("index"));
            CharSequence inputData = DataUtil.isNullOrEmpty(input) ? null : input.trim().toUpperCase();
            ListProductOfferDTO listProductOfferDTO = lsListProductOfferDTO.get(index);
            listProductOfferDTO.setInputSearch(input);
            lsProductOfferingTotalDTOTmp = DataUtil.defaultIfNull(productOfferingService.getLsProductOfferingForChangeProduct(listProductOfferDTO.getInputSearch(),
                    listProductOfferDTO.getProductOfferingTotalDTO().getProductOfferingId(), true), new ArrayList<>());
            listProductOfferDTO.setLsProductOfferingTotalDTONew(lsProductOfferingTotalDTOTmp);
            List<ProductOfferingTotalDTO> lsAllDataTmp = Lists.newArrayList(listProductOfferDTO.getLsProductOfferingTotalDTONew());
            List<ProductOfferingTotalDTO> lsAllData = Lists.newArrayList();
            for (ProductOfferingTotalDTO dto : lsAllDataTmp) {
                if (!lsAllData.contains(dto)) {
                    lsAllData.add(dto);
                }
            }
            if (inputData == null) {
                return lsAllData;
            }
            return lsAllData.stream().filter(obj -> lsAllData.contains(obj)
                    && (DataUtil.safeToString(obj.getCode()).toUpperCase().contains(inputData)
                    || DataUtil.safeToString(obj.getName()).toUpperCase().contains(inputData))).collect(Collectors.toList());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return lsProductOfferingTotalDTOTmp;
    }

    @Override
    public List<StockTransDetailDTO> getListTransDetailDTOs() {
        List<StockTransDetailDTO> lsDetail = Lists.newArrayList();
        if (isAddNewProduct) {
            if (this.lsListProductOfferDTO != null) {
                for (ListProductOfferDTO listProdOffer : this.lsListProductOfferDTO) {
                    if (listProdOffer.getProductOfferingTotalDTO() == null
                            || DataUtil.isNullOrZero(listProdOffer.getProductOfferingTotalDTO().getProductOfferingId())) {
                        continue;
                    }
                    StockTransDetailDTO stockDetail = new StockTransDetailDTO();
                    stockDetail.setOwnerType(DataUtil.safeToLong(ownerType));
                    stockDetail.setOwnerID(ownerId);
                    if (listProdOffer.getProductOfferingTotalDTO() != null) {
                        stockDetail.setProdOfferId(listProdOffer.getProductOfferingTotalDTO().getProductOfferingId());
                        stockDetail.setProdOfferName(listProdOffer.getProductOfferingTotalDTO().getName());
                        stockDetail.setUnit(listProdOffer.getProductOfferingTotalDTO().getUnitName());
                        stockDetail.setStateName(listProdOffer.getProductOfferingTotalDTO().getStateName());
                        stockDetail.setAvaiableQuantity(listProdOffer.getProductOfferingTotalDTO().getAvailableQuantity());
                        stockDetail.setCheckSerial(DataUtil.safeToLong(listProdOffer.getProductOfferingTotalDTO().getCheckSerial()));
                        stockDetail.setBasisPrice(listProdOffer.getProductOfferingTotalDTO().getPriceAmount());
                        stockDetail.setPrice(listProdOffer.getProductOfferingTotalDTO().getPriceAmount());
                        stockDetail.setQuantityBccs(listProdOffer.getProductOfferingTotalDTO().getQuantityBccs());
                        stockDetail.setQuantityInspect(listProdOffer.getProductOfferingTotalDTO().getQuantityInspect());
                        stockDetail.setQuantityFinance(listProdOffer.getProductOfferingTotalDTO().getQuantityFinance());
                        stockDetail.setLstStockDeviceTransfer(listProdOffer.getLstStockDeviceTransfer());
                        if (!DataUtil.isNullObject(listProdOffer.getProductOfferingTotalDTONew())) {
                            stockDetail.setProdOfferIdChange(listProdOffer.getProductOfferingTotalDTONew().getProductOfferingId());
                        }
                    }
                    //tuydv1
                    stockDetail.setProdOfferTypeId(listProdOffer.getProductOfferTypeDTO().getProductOfferTypeId());
                    stockDetail.setStrStateIdAfter(listProdOffer.getStrStateIdAfter());
                    stockDetail.setStateId(listProdOffer.getStateId());
                    stockDetail.setQuantity(DataUtil.safeToLong(listProdOffer.getNumber()));
                    stockDetail.setTableName(listProdOffer.getProductOfferTypeDTO().getTableName());
                    stockDetail.setLstStockTransSerial(listProdOffer.getLstSerial());
                    if (DataUtil.safeEqual(stockDetail.getStateId(), Const.GOODS_STATE.NEW)) {
                        if (stockDetail.getQuantity() != null && stockDetail.getPrice() != null) {
                            stockDetail.setAmount(stockDetail.getQuantity() * stockDetail.getPrice());
                        }
                    } else {
                        stockDetail.setAmount(0L);
                        stockDetail.setPrice(0L);
                    }
                    lsDetail.add(stockDetail);
                }
            }
        } else {
            if (lsStockTransFull != null) {
                for (StockTransFullDTO stockFull : lsStockTransFull) {
                    StockTransDetailDTO stockDetail = new StockTransDetailDTO();
                    stockDetail.setStockTransDetailId(stockFull.getStockTransDetailId());
                    stockDetail.setProdOfferName(stockFull.getProdOfferName());
                    stockDetail.setProdOfferId(stockFull.getProdOfferId());
                    stockDetail.setStateId(stockFull.getStateId());
                    stockDetail.setQuantity(stockFull.getQuantity());
                    stockDetail.setLstStockTransSerial(stockFull.getLstSerial());
                    stockDetail.setTableName(stockFull.getTableName());
                    stockDetail.setProdOfferTypeId(stockFull.getProductOfferTypeId());
                    stockDetail.setCheckSerial(DataUtil.safeToLong(stockFull.getCheckSerial()));
                    stockDetail.setBasisPrice(stockFull.getBasisPrice());
                    stockDetail.setStateName(stockFull.getStateName());
                    stockDetail.setUnit(stockFull.getUnit());
                    stockDetail.setPrice(stockFull.getPrice());
                    stockDetail.setAmount(stockFull.getAmount());
                    lsDetail.add(stockDetail);
                }
            }
        }
        return lsDetail;
    }

    @Override
    public void setDisableRemove() {
        if (lsListProductOfferDTO != null) {
            for (ListProductOfferDTO listProductOfferDTO : lsListProductOfferDTO) {
                listProductOfferDTO.setDisAbleRemove(true);
            }
        }
    }

    @Override
    public void doOpenDlgSerial(int index) {
        try {
            this.index = index;
            //reset cac thong tin dialgo trc
            listSerialRangeSelect = Lists.newArrayList();
            isViewBtnExportStock = false;
            errorImport = false;
            attachFileName = "";
            typePrint = "2";
//            doChangeTypePrint();
            //TH mo dialog serial trong case add moi
            StockTransFullDTO transFullDTOTmp = lsStockTransFull.get(index);
            if (isAddNewProduct && Const.MODE_SERIAL.MODE_ADD_ALL.equals(this.configTagDto.getModeSerial())) {
                ListProductOfferDTO select = lsListProductOfferDTO.get(index);
                if (select.getProductOfferTypeDTO() == null || select.getProductOfferTypeDTO().getProductOfferTypeId() == null) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "export.order.type.product.not.blank");
                } else if (select.getProductOfferingTotalDTO() == null || select.getProductOfferingTotalDTO().getProductOfferingId() == null) {
                    executeCommand("focusElementErrorByListClass('txtProductName','" + index + "')");
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "export.order.stock.inputText.require.msg");
                } else if (configTagDto.isChangeProduct() && (select.getProductOfferingTotalDTONew() == null || select.getProductOfferingTotalDTONew().getProductOfferingId() == null)) {
                    executeCommand("focusElementErrorByListClass('txtProductNameNew','" + index + "')");
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "export.order.stock.inputText.require.msg.new");
                } else if (select.getStateId() == null) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "mn.stock.require.status.msg");
                } else if (DataUtil.isNullOrEmpty(select.getNumber()) || DataUtil.safeToLong(select.getNumber()) <= 0) {
                    executeCommand("focusElementErrorByListClass('txtNumber','" + index + "')");
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "common.number.not.emty");
                } else if (configTagDto.isShowRetrive() && DataUtil.safeToInt(lsListProductOfferDTO.get(0).getNumber()) > 1) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.rescueInformation.validate.list.resontrue");
                }
                //<editor-fold defaultstate="collapsed" desc="Kiểm tra số lượng đáp ứng">
                if (configTagDto.isValidateAvaiableQuantity()) {
                    StockTotalDTO stockTotalDTO = new StockTotalDTO();
                    stockTotalDTO.setOwnerType(DataUtil.safeToLong(ownerType));
                    stockTotalDTO.setOwnerId(ownerId);
                    stockTotalDTO.setProdOfferId(select.getProductOfferingTotalDTO().getProductOfferingId());
                    stockTotalDTO.setStateId(select.getStateId());
                    List<StockTotalDTO> stockTotalDTOs = stockTotalService.searchStockTotal(stockTotalDTO);
                    if (DataUtil.isNullOrEmpty(stockTotalDTOs)) {
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.stockTotal.empty", select.getProductOfferingTotalDTO().getName());
                    }
                    if (stockTotalDTOs.get(0).getAvailableQuantity() < DataUtil.safeToLong(select.getNumber())) {
                        if (!select.getProductOfferingTotalDTO().getAvailableQuantity().equals(stockTotalDTOs.get(0).getAvailableQuantity())) {
                            select.getProductOfferingTotalDTO().setAvailableQuantity(stockTotalDTOs.get(0).getAvailableQuantity());
                            select.setAvaiableQuantity(stockTotalDTOs.get(0).getAvailableQuantity());
                        }
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.stockTotal.notAvailable", select.getProductOfferingTotalDTO().getName());
                    }
                }
                //</editor-fold>
                stockTransDetail.setProdOfferId(select.getProductOfferingTotalDTO().getProductOfferingId());
                stockTransDetail.setProductOfferTypeId(select.getProductOfferTypeDTO().getProductOfferTypeId());
                stockTransDetail.setProdOfferName(select.getProductOfferingTotalDTO().getName());
                stockTransDetail.setStateId(select.getStateId());
                stockTransDetail.setStateName(getStateName(select.getStrStateId()));
                stockTransDetail.setQuantity(DataUtil.safeToLong(select.getNumber()));
                stockTransDetail.setFromOwnerType(ownerType);
                stockTransDetail.setTableName(select.getProductOfferTypeDTO().getTableName());
                stockTransDetail.setFromOwnerId(ownerId);
                stockTransDetail.setLstSerial(select.getLstSerial());
                if (DataUtil.safeEqual(ownerType, Const.OWNER_TYPE.STAFF)) {
                    shopInfoTagExportDlg.loadStaff(DataUtil.safeToString(ownerId), true);
                } else {
                    shopInfoTagExportDlg.loadShop(DataUtil.safeToString(ownerId), true);
                }
            } else if (Const.MODE_SERIAL.MODE_ADD_SERIAL.equals(this.configTagDto.getModeSerial())) {
                this.stockTransDetail = DataUtil.cloneBean(transFullDTOTmp);
                Long fromOwnerId = stockTransDetail.getFromOwnerId();
                if (!DataUtil.isNullOrZero(stockTransDetail.getRegionStockTransId())) {
                    Long stockTransId = stockTransDetail.getRegionStockTransId();
                    StockTransDTO stockTransDTO = stockTransService.findOne(stockTransId);
                    fromOwnerId = stockTransDTO.getFromOwnerId();
                }
                if (DataUtil.safeEqual(ownerType, Const.OWNER_TYPE.STAFF)) {
                    shopInfoTagExportDlg.loadStaff(DataUtil.safeToString(ownerId), true);
                } else {
                    shopInfoTagExportDlg.loadShop(DataUtil.safeToString(fromOwnerId), true);
                }
                //TH mo dialog serial trong edit
                if (this.stockTransDetail.getHaveListSerial()) {
                    return;
                }
                //khi click xem chi tiet xu ly reset thong tin upload file
                fileUpload = null;
                byteContent = null;
                attachFileName = null;
                processingFile = null;
            } else {
                List<StockTransSerialDTO> lsSerial = DataUtil.defaultIfNull(stockTransSerialService.findByStockTransDetailId(transFullDTOTmp.getStockTransDetailId()), new ArrayList<StockTransSerialDTO>());
                transFullDTOTmp.setLstSerial(lsSerial);
                this.stockTransDetail = DataUtil.cloneBean(transFullDTOTmp);
            }
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception e) {
            topReportError("", "common.error.happened", e);
        }
    }

    @Override
    public StreamedContent getAddTemplateFile() {
        InputStream addStream = Faces.getResourceAsStream(configTagDto.getTemplateName());
        return new DefaultStreamedContent(addStream, "application/xls", configTagDto.getFileName());
    }

    @Override
    public StreamedContent getAddTemplateFileByProd() {
//        InputStream addStream = Faces.getResourceAsStream(configTagDto.getTemplateNameByProd());
//        return new DefaultStreamedContent(addStream, "application/xls", configTagDto.getFileNameProd());
        try {
            FileExportBean bean = new FileExportBean();
            bean.setTempalatePath(FileUtil.getTemplatePath());
            bean.setTemplateName(Const.REPORT_TEMPLATE.IMPORT_LIST_PRODUCT);
            List<StockTransFullDTO> lsPrint = Lists.newArrayList();
            if (lsStockTransFull != null) {
                for (StockTransFullDTO stock : lsStockTransFull) {
                    if ((Const.STOCK_TYPE.STOCK_HANDSET.equals(stock.getProductOfferTypeId()) || Const.STOCK_TYPE.STOCK_ACCESSORIES.equals(stock.getProductOfferTypeId()))
                            && DataUtil.safeToLong(stock.getQuantity()).compareTo(1L) > 0) {
                        for (Long i = 0L; DataUtil.safeToLong(stock.getQuantity()).compareTo(i) > 0; i++) {
                            StockTransFullDTO stockTmp = DataUtil.cloneBean(stock);
                            stockTmp.setQuantity(1L);
                            lsPrint.add(stockTmp);
                        }
                        continue;
                    }
                    lsPrint.add(stock);
                }
            }
            bean.putValue("lstStockTransFull", lsPrint);
            bean.setOutName("file_nhap_serial_nhieu_mat_hang" + "_" + getStaffDTO().getStaffCode() + "_" + DateUtil.date2LongUpdateDateTime(Calendar.getInstance().getTime()) + ".xls");
            return FileUtil.exportToStreamed(bean);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
        return null;
    }

    @Secured("@")
    public StreamedContent exportSerialsDlg() {
        try {
            if (DataUtil.isNullOrEmpty(listSerialRangeSelect)) {
                topReportError("", ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.trans.search.validate.noserialresult");
                return null;
            }
            FileExportBean fileExportBean = new FileExportBean();
            fileExportBean.setOutName("detail_serial_stock_trans.xls");
            fileExportBean.setTempalatePath(FileUtil.getTemplatePath());
            fileExportBean.setOutPath(FileUtil.getOutputPath());
            fileExportBean.setTemplateName(Const.REPORT_TEMPLATE.SERIAL_DETAIL_TEMPLATE);
            fileExportBean.putValue("state", stockTransDetail.getStateName());
            fileExportBean.putValue("stockModelName", stockTransDetail.getProdOfferName());
            fileExportBean.putValue("userCreate", getStaffDTO().getStaffCode());
            fileExportBean.putValue("dateCreate", optionSetValueService.getSysdateFromDB(true));
            fileExportBean.putValue("listStockTransSerials", listSerialRangeSelect);
            fileExportBean.putValue("totalReq", stockTransDetail.getQuantity());
            return FileUtil.exportToStreamed(fileExportBean);
//            FileUtil.exportFile(fileExportBean);
//            return fileExportBean.getExportFileContent();
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
        return null;
    }

    @Secured("@")
    public StreamedContent exportSerialsDlgView() {
        try {
            StockTransFullDTO stockFull = lsStockTransFull.get(index);
            if (isAddNewProduct) {
                ListProductOfferDTO offerDTO = lsListProductOfferDTO.get(index);
                if (!DataUtil.isNullObject(offerDTO)) {
                    stockFull.setStateName(getStateName(offerDTO.getStrStateId()));
                    stockFull.setProdOfferName(offerDTO.getProductOfferingDTO().getName());
                    stockFull.setLstSerial(offerDTO.getLstSerial());
                    stockFull.setQuantity(DataUtil.safeToLong(offerDTO.getNumber()));
                }
            }
            if (DataUtil.isNullOrEmpty(stockFull.getLstSerial())) {
                topReportError("", ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.trans.search.validate.noserialresult");
                return null;
            }
            FileExportBean fileExportBean = new FileExportBean();
            fileExportBean.setOutName("detail_serial_stock_trans.xls");
            fileExportBean.setTempalatePath(FileUtil.getTemplatePath());
            fileExportBean.setOutPath(FileUtil.getOutputPath());
            fileExportBean.setTemplateName(Const.REPORT_TEMPLATE.SERIAL_DETAIL_TEMPLATE);
            fileExportBean.putValue("state", stockFull.getStateName());
            fileExportBean.putValue("stockModelName", stockFull.getProdOfferName());
            fileExportBean.putValue("userCreate", getStaffDTO().getStaffCode());
            fileExportBean.putValue("dateCreate", optionSetValueService.getSysdateFromDB(true));
            fileExportBean.putValue("listStockTransSerials", stockFull.getLstSerial());
            fileExportBean.putValue("totalReq", stockFull.getQuantity());
            return FileUtil.exportToStreamed(fileExportBean);
//            FileUtil.exportFile(fileExportBean);
//            return fileExportBean.getExportFileContent();
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
        return null;
    }

    @Secured("@")
    public StreamedContent exportSerialsDlgViewStock() {
        try {
            StockTransFullDTO stockFull = DataUtil.cloneBean(lsStockTransFull.get(index));
            if (isAddNewProduct) {
                ListProductOfferDTO offerDTO = lsListProductOfferDTO.get(index);
                if (!DataUtil.isNullObject(offerDTO)) {
                    stockFull.setStateName(getStateName(offerDTO.getStrStateId()));
                    stockFull.setProdOfferName(offerDTO.getProductOfferingDTO().getName());
                    stockFull.setLstSerial(offerDTO.getLstSerial());
                }
            }
            Long quantity = 0L;
            for (StockTransSerialDTO serialDTO : listSerialView) {
                quantity += DataUtil.safeToLong(serialDTO.getQuantity());
            }
            stockFull.setQuantity(quantity);
            FileExportBean fileExportBean = new FileExportBean();
            fileExportBean.setOutName("detail_serial_stock_trans.xls");
            fileExportBean.setTempalatePath(FileUtil.getTemplatePath());
            fileExportBean.setOutPath(FileUtil.getOutputPath());
            fileExportBean.setTemplateName(Const.REPORT_TEMPLATE.SERIAL_DETAIL_TEMPLATE);
            fileExportBean.putValue("state", stockFull.getStateName());
            fileExportBean.putValue("stockModelName", stockFull.getProdOfferName());
            fileExportBean.putValue("userCreate", getStaffDTO().getStaffCode());
            fileExportBean.putValue("dateCreate", optionSetValueService.getSysdateFromDB(true));
            fileExportBean.putValue("listStockTransSerials", listSerialView);
            fileExportBean.putValue("totalReq", stockFull.getQuantity());
            return FileUtil.exportToStreamed(fileExportBean);
//            FileUtil.exportFile(fileExportBean);
//            return fileExportBean.getExportFileContent();
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
        return null;
    }

    public boolean getDisableSerialViewStock() {
        return !DataUtil.isNullOrEmpty(listSerialView);
    }

    @Override
    public void doViewStockTransDetail(Long stockTransActionId) {
        try {
            List<Long> lsStockTransId = Lists.newArrayList(stockTransActionId);
            lsStockTransFull = DataUtil.defaultIfNull(stockTransService.searchStockTransDetail(lsStockTransId), new ArrayList<StockTransFullDTO>());
            isViewBtnExportStock = false;
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            topReportError("", "common.error.happened", "");
        }
    }

    @Override
    public void handleFileUpload(FileUploadEvent event) {
        try {
            clearFileUpload();
            errorImport = false;
            if (event == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "common.error.upload.file");
            }
            fileUpload = event.getFile();

            BaseMessage message = validateFileUploadWhiteList(fileUpload, ALOW_EXTENSION_EXCEL_TYPE_LIST, MAX_SIZE_5M);
            if (!message.isSuccess()) {
                clearFileUpload();
                LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                ex.setDescription(message.getDescription());
                throw ex;
            }

            byteContent = fileUpload.getContents();
            attachFileName = new String(fileUpload.getFileName().getBytes(), "UTF-8");
            processingFile = new ExcellUtil(fileUpload, byteContent);

        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception e) {
            topReportError("", "common.error.happened", e);
        }
    }

    /**
     * xu ly clear xoa file upload mat hang
     *
     * @author ThanhNT77
     */
    private void clearFileUploadProd() {
        attachFileNameByProd = null;
        processingFileProd = null;
        fileUploadByProd = null;
        byteContentByProd = null;
    }

    /**
     * xu ly clear xoa file upload cua nhap serial
     *
     * @author ThanhNT77
     */
    private void clearFileUpload() {
        attachFileName = null;
        processingFile = null;
        fileUpload = null;
        byteContent = null;
    }

    @Override
    public void handleFileUploadByProd(FileUploadEvent event) {
        try {
            if (event == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "common.error.upload.file");
            }
            fileUploadByProd = event.getFile();

            BaseMessage message = validateFileUploadWhiteList(fileUploadByProd, ALOW_EXTENSION_EXCEL_TYPE_LIST, MAX_SIZE_5M);
            if (!message.isSuccess()) {
                clearFileUploadProd();
                LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                ex.setDescription(message.getDescription());
                throw ex;
            }

            byteContentByProd = fileUploadByProd.getContents();
            errorImportProd = false;
            attachFileNameByProd = new String(fileUploadByProd.getFileName().getBytes(), "UTF-8");
            processingFileProd = new ExcellUtil(fileUploadByProd, byteContentByProd);
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception e) {
            topReportError("", "common.error.happened", e);
        }
    }

    /**
     * ham tao stockDetail theo loai mat hang va serial
     *
     * @param processingFile
     * @return
     * @throws Exception
     * @author ThanhNT77
     */
    private void createStockDetailProductdByFile(ExcellUtil processingFile) throws LogicException, Exception {
        errorImportProd = false;
        if (processingFile == null) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.status.isdn.delete.valid.file.size");
        }

        List<OptionSetValueDTO> lsOptionState = DataUtil.defaultIfNull(optionSetValueService.getByOptionSetCode(Const.OPTION_SET.GOODS_STATE), new ArrayList<>());
        Map<String, String> mapState = lsOptionState.stream().collect(Collectors.toMap(OptionSetValueDTO::getValue, OptionSetValueDTO::getName));

        List<StockTransFullDTO> lsStockFullFile = Lists.newArrayList();
        Sheet sheetProcess = processingFile.getSheetAt(0);
        /*if (test == null || processingFile.getTotalColumnAtRow(test) != 5) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "list.product.validate.fileError");
        }*/
        //xu ly clear het serial nhap lai tu dau moi lan click chon serial
        for (StockTransFullDTO stockData : lsStockTransFull) {
            if (!Const.PRODUCT_OFFERING._CHECK_SERIAL.equals(stockData.getCheckSerial())) {
                continue;
            }
            stockData.setLstSerial(new ArrayList<>());
        }
        //map stockFull luu tru mat hang
        Map<String, List<StockTransFullDTO>> mapStockFull = Maps.newHashMap();
        Integer indexData = 0;
        int totalRow = 0;
        for (Row row : sheetProcess) {
            totalRow++;
            if (totalRow <= 5) {
                continue;
            }
            StockTransFullDTO stockFull = new StockTransFullDTO();
            stockFull.setIndex(indexData);
            indexData++;
            lsStockFullFile.add(stockFull);
            StockTransSerialDTO stockTransSerialDTOPrint = new StockTransSerialDTO();
            stockFull.setStockTransSerialDTOPrint(stockTransSerialDTOPrint);

            String prodOfferCode = DataUtil.safeToString(processingFile.getStringValue(row.getCell(0))).trim();
            String state = DataUtil.safeToString(processingFile.getStringValue(row.getCell(1))).trim();
            String fromSerial = DataUtil.safeToString(processingFile.getStringValue(row.getCell(2))).trim();
            String toSerial = DataUtil.safeToString(processingFile.getStringValue(row.getCell(3))).trim();
            String quantity = DataUtil.safeToString(processingFile.getStringValue(row.getCell(4))).trim();

            stockTransSerialDTOPrint.setFromSerial(fromSerial);
            stockTransSerialDTOPrint.setToSerial(toSerial);

            stockFull.setProdOfferCode(prodOfferCode);
            stockFull.setStrStateId(state);
            stockFull.setStrQuantity(quantity);
            stockFull.setQuantity(DataUtil.safeToLong(quantity));

            //I.Case validate
            //1.validate noi dung ma code, trang thai, tu serial, den serial, so luong khong duoc de trong
            if (DataUtil.isNullOrEmpty(prodOfferCode) && DataUtil.isNullOrEmpty(state) && DataUtil.isNullOrEmpty(fromSerial) && DataUtil.isNullOrEmpty(toSerial) && DataUtil.isNullOrEmpty(quantity)) {
                continue;
            } else if (DataUtil.isNullOrEmpty(prodOfferCode) || DataUtil.isNullOrEmpty(state) || DataUtil.isNullOrEmpty(fromSerial) || DataUtil.isNullOrEmpty(toSerial) || DataUtil.isNullOrEmpty(quantity)) {
                stockFull.setMsgError(getText("input.list.product.file.content.empty"));
                errorImportProd = true;
                continue;
            }
            //2.1validate truong trang thai, va so luong phai la kieu so
            if (!DataUtil.validateStringByPattern(state, Const.REGEX.NUMBER_REGEX) || !DataUtil.validateStringByPattern(quantity, Const.REGEX.NUMBER_REGEX)) {
                stockFull.setMsgError(getText("input.list.product.file.status.number.format"));
                errorImportProd = true;
                continue;
            }
            stockFull.setStateId(DataUtil.safeToLong(state));

            //2.2validate trang thai chi chap nhan cac gia tri 1: moi, 2: hong, 3: hang thu hoi
            if (!mapState.containsKey(DataUtil.safeToString(stockFull.getStateId()))) {
                stockFull.setMsgError(getText("input.list.product.file.status.permision"));
                errorImportProd = true;
                continue;
            }

            /*if (!(Const.GOODS_STATE.NEW.equals(stockFull.getStateId()) || Const.GOODS_STATE.BROKEN.equals(stockFull.getStateId()) || Const.GOODS_STATE.RETRIVE.equals(stockFull.getStateId()))) {
                stockFull.setMsgError(getText("input.list.product.file.status.permision"));
                errorImportProd = true;
                continue;
            }*/
            //3.validate do dai serial bat dau va ket thuc phai bang nhau
            if (fromSerial.length() != toSerial.length()) {
                stockFull.setMsgError(getText("mn.stock.partner.length.fromSerial.toSerial.valid"));
                errorImportProd = true;
                continue;
            }
            //4.validate productOfferCode, stateId phai mapping voi list danh sach hang hoa
            if (!DataUtil.isNullOrEmpty(this.lsStockTransFull)) {
                boolean validErrorCodeState = true;
                for (StockTransFullDTO stock : lsStockTransFull) {
                    if (!Const.PRODUCT_OFFERING._CHECK_SERIAL.equals(stock.getCheckSerial())) {
                        continue;
                    }
                    if (prodOfferCode.equals(stock.getProdOfferCode()) && state.equals(DataUtil.safeToString(stock.getStateId()))) {
                        validErrorCodeState = false;
                        stockFull.setProdOfferId(stock.getProdOfferId());
                        stockFull.setTableName(stock.getTableName());
                        stockFull.setFromOwnerId(stock.getFromOwnerId());
                        stockFull.setFromOwnerType(stock.getFromOwnerType());
                        stockFull.setRegionStockTransId(stock.getRegionStockTransId());
                        stockFull.setProductOfferTypeId(stock.getProductOfferTypeId());
                        stockFull.setStateId(stock.getStateId());
                        stockFull.setQuantityRemain(stock.getQuantity());
                        break;
                    }
                }
                String msgError = validErrorCodeState ? getTextParam("input.list.product.file.proCode.stateId.notExist", prodOfferCode, state) : "";
                if (!"".equals(msgError)) {
                    stockFull.setMsgError(msgError);
                    errorImportProd = true;
                    continue;
                }
            }
            //5.validate ma code + state phai ton tai hang hoa nay trong DB
            ProductOfferingDTO prodOfferDto = productOfferingService.getProdOfferDtoByCodeAndStock(prodOfferCode, ownerIdExport, stockFull.getStateId());
            if (prodOfferDto == null) {
                stockFull.setMsgError(getText("input.list.product.file.prod.not.valid"));
                errorImportProd = true;
                continue;
            }
            //6.validate neu ko tim thay loai mat hang voi hang hoa tuong ung thi cung bao loi
            ProductOfferTypeDTO prodOfferTypeDto = productOfferTypeService.findOne(prodOfferDto.getProductOfferTypeId());
            if (prodOfferTypeDto == null || Const.STATUS.NO_ACTIVE.equals(prodOfferTypeDto.getStatus())) {
                stockFull.setMsgError(getText("input.list.product.file.prodType.not.valid"));
                errorImportProd = true;
                continue;
            }
            stockFull.setTableName(prodOfferTypeDto.getTableName());
            //7.valdiate voi mat hang handset
            if (Const.STOCK_TYPE.STOCK_HANDSET.equals(prodOfferTypeDto.getProductOfferTypeId())) {
                //7.1 validate neu fromSerial khac toSerial
                if (!fromSerial.equals(toSerial)) {
                    stockFull.setMsgError(getText("mn.stock.partner.serial.valid.hanset.valid.file"));
                    errorImportProd = true;
                    continue;
                }
                //7.2 valdiate neu so so luong  != 1 tthi bao loi
                if (!"1".equals(quantity)) {
                    stockFull.setMsgError(getText("mn.stock.partner.serial.valid.hanset.valid.number"));
                    errorImportProd = true;
                    continue;
                }
                //7.3 validate trung lap voi serial handset trong file
                boolean isDuplicate = false;
                for (int j = 0; j < lsStockFullFile.size(); j++) {
                    if (j == lsStockFullFile.size() - 1) {
                        continue;
                    }
                    StockTransFullDTO stockTmp = lsStockFullFile.get(j);
                    if (fromSerial.equals(stockTmp.getStockTransSerialDTOPrint().getFromSerial()) && toSerial.equals(stockTmp.getStockTransSerialDTOPrint().getToSerial())) {
                        isDuplicate = true;
                        break;
                    }
                }
                if (isDuplicate) {
                    errorImportProd = true;
                    stockFull.setMsgError(getText("mn.stock.partner.range.duplidate.prod"));
                    continue;
                }
            } else {//8.validate voi mat hang khong phai handset
                //8.1 validate tu serial den serial phai la kieu so
                if (!DataUtil.validateStringByPattern(fromSerial, Const.REGEX.NUMBER_REGEX) || !DataUtil.validateStringByPattern(toSerial, Const.REGEX.NUMBER_REGEX)) {
                    stockFull.setMsgError(getMessage("mn.stock.partner.serial.valid.format", stockFull.getProdOfferName()));
                    errorImportProd = true;
                    continue;
                }
                //8.2 validate den serial > tu serial va neu den seri - tu serial > 500.000
                BigInteger fromSerialFile = new BigInteger(fromSerial);
                BigInteger toSerialFile = new BigInteger(toSerial);
                BigInteger result = toSerialFile.subtract(fromSerialFile);
                if (result.compareTo(new BigInteger("0")) < 0 || result.compareTo(new BigInteger("500000")) > 0) {
                    stockFull.setMsgError(getMessage("mn.stock.partner.serial.valid.range"));
                    errorImportProd = true;
                    continue;
                }
                //8.3 valadiate so luong serial, neu toSerial - fromSerial > so luong serial thuc te trong file thi cung bao loi
                result = result.add(BigInteger.ONE);
                if (result.compareTo(new BigInteger(quantity)) != 0) {
                    stockFull.setMsgError(getMessage("input.list.product.file.number.valid.serial"));
                    errorImportProd = true;
                    continue;
                }
                //8.4 validate check trung lap dai serial trong file
                boolean isDuplicate = false;

                Range<BigInteger> ranFile = Range.closed(fromSerialFile, toSerialFile);
                Range<BigInteger> ranSelect;

                for (int j = 0; j < lsStockFullFile.size(); j++) {
                    if (j == lsStockFullFile.size() - 1) {
                        continue;
                    }
                    StockTransFullDTO stockTmp = lsStockFullFile.get(j);
                    boolean isContinue = DataUtil.isNullOrEmpty(stockTmp.getTableName()) || Const.STOCK_TYPE.STOCK_HANDSET_NAME.equalsIgnoreCase(stockTmp.getTableName())
                            || !DataUtil.isNullOrEmpty(stockTmp.getMsgError()) || DataUtil.isNullOrEmpty(stockTmp.getStockTransSerialDTOPrint().getFromSerial()) || DataUtil.isNullOrEmpty(stockTmp.getStockTransSerialDTOPrint().getToSerial());
                    if (isContinue) {
                        continue;
                    }
                    BigInteger fromSerialSelect = new BigInteger(stockTmp.getStockTransSerialDTOPrint().getFromSerial());
                    BigInteger toSerialSelect = new BigInteger(stockTmp.getStockTransSerialDTOPrint().getToSerial());

                    ranSelect = Range.closed(fromSerialSelect, toSerialSelect);

                    if (ranSelect.isConnected(ranFile)) {
                        isDuplicate = true;
                        break;
                    }
                }
                if (isDuplicate) {
                    errorImportProd = true;
                    stockFull.setMsgError(getText("mn.stock.partner.range.duplidate.prod"));
                    continue;
                }
            }
            String key = prodOfferCode + state;
            List<StockTransFullDTO> lsFull = mapStockFull.get(key);
            if (lsFull == null) {
                lsFull = Lists.newArrayList(stockFull);
                mapStockFull.put(key, lsFull);
            } else {
                lsFull.add(stockFull);
            }
        }
        //9. xu lu check so luong serial, so luong serial phai bang so luong serial yeu cau va dong thoi xu li validate ghep dai serial, mat hang trong DB
        for (String key : mapStockFull.keySet()) {
            List<StockTransFullDTO> lsStockMap = mapStockFull.get(key);
            if (!DataUtil.isNullOrEmpty(lsStockMap)) {
                Long quantityRemain = lsStockMap.get(0).getQuantityRemain();
                Long totalQuantityFile = 0L;
                for (StockTransFullDTO stockTransFullDTO : lsStockMap) {
                    totalQuantityFile += stockTransFullDTO.getQuantity();
                }
                //9.1 check neu tong so luong serial yeu cau ma khac bang tong so luong serial trong file ma khac nhau thi bao loi
                if (totalQuantityFile.compareTo(quantityRemain) != 0) {
                    errorImportProd = true;
                    setMsgErrorList(lsStockFullFile, lsStockMap, "mn.stock.partner.range.serial.invalid.number");
                } else {//9.2 neu tong so luong da thoa man, xu ly tim kiem trong kho (tim trong DB)

                    List<StockTransSerialDTO> listSerialAll = Lists.newArrayList();
                    for (StockTransFullDTO stockTmp : lsStockMap) {
                        List<StockTransSerialDTO> listSerialTmp = getListSerialFile(stockTmp, Lists.newArrayList(stockTmp.getStockTransSerialDTOPrint()));
                        if (!DataUtil.isNullOrEmpty(listSerialTmp)) {
                            listSerialAll.addAll(listSerialTmp);
                        }
                    }
                    //9.2.1 neu tong so serial tuong ung tim trong kho ma rong thi bao loi
                    if (DataUtil.isNullOrEmpty(listSerialAll)) {
                        errorImportProd = true;
                        setMsgErrorList(lsStockFullFile, lsStockMap, "input.list.product.file.empty.list.serial");
                    } else {
                        Long totalQuantityDB = 0L;
                        for (StockTransSerialDTO stockSerial : listSerialAll) {
                            totalQuantityDB += stockSerial.getQuantity();
                        }
                        //9.2.2 neu tong so serial tuong ung tim trong kho khong bang so luong yeu cau
                        if (totalQuantityDB.compareTo(0L) == 0) {
                            setMsgErrorList(lsStockFullFile, lsStockMap, "input.list.product.file.prod.not.valid");
                        } else if (!totalQuantityDB.equals(totalQuantityFile)) {//9.2.3 neu tong so serial tuong ung tim trong kho khong bang so luong yeu cau
                            errorImportProd = true;
                            setMsgErrorList(lsStockFullFile, lsStockMap, "mn.stock.partner.range.serial.invalid.stock.number");
                        } else {
                            for (StockTransFullDTO stockRequest : lsStockTransFull) {
                                if (!Const.PRODUCT_OFFERING._CHECK_SERIAL.equals(stockRequest.getCheckSerial()) || stockRequest.getHaveListSerial()) {
                                    continue;
                                }
                                String keyRequest = stockRequest.getProdOfferCode() + DataUtil.safeToString(stockRequest.getStateId());
                                if (key.equals(keyRequest)) {
                                    stockRequest.setLstSerial(listSerialAll);
                                    break;
                                }
                            }
                        }

                    }
                }
            }
        }
        if (errorImportProd && getShowUploadProdByFile()) {
            clearFileUploadProd();
            lstErrorSerialProd = Lists.newArrayList(lsStockFullFile);
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "list.product.validate.msg.errRead");
        }

    }

    /**
     * ham set msg error cho ca list
     *
     * @param lsStockTransFull
     * @param key
     * @author ThanhNT77
     */
    private void setMsgErrorList(List<StockTransFullDTO> lsStockTransFile, List<StockTransFullDTO> lsStockTransFull, String key) {
        if (lsStockTransFull != null) {
            for (StockTransFullDTO stock : lsStockTransFull) {
                StockTransFullDTO stockFile = lsStockTransFile.get(stock.getIndex().intValue());
                if (stockFile != null) {
                    stockFile.setMsgError(getText(key));
                }
            }
        }
    }

    /**
     * ham tra ve danh sach sach serial
     *
     * @param stockTransFullDTO
     * @return
     * @throws Exception
     * @author ThanhNT77
     */
    private void createStockDetailByFile(ExcellUtil processingFile, StockTransFullDTO stockTransFullDTO, List<StockTransSerialDTO> listSerialRangeSelect) throws LogicException, Exception {
        errorImport = false;
        if (processingFile == null) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "limit.stock.valid.file");
        }
        List<StockTransSerialDTO> listSerialAll = Lists.newArrayList();

        Sheet sheetProcess = processingFile.getSheetAt(0);
        int totalRow = processingFile.getTotalRowAtSheet(sheetProcess);
        if (totalRow <= 5) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.status.isdn.file.row.null");
        }

        Row test = sheetProcess.getRow(4);
        if (test == null || processingFile.getTotalColumnAtRow(test) < 2) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "list.product.validate.fileError");
        }
        int rowIndex = 0;
        Range<BigInteger> ranSelect;
        Range<BigInteger> ranFile;
        for (int i = 5; i < totalRow; i++) {
            Row row = sheetProcess.getRow(i);
            //neu gap dong trong thi bo qua
            if (row == null) {
                continue;
            }


            String fromSerial = DataUtil.safeToString(processingFile.getStringValue(row.getCell(0))).trim();
            String toSerial = DataUtil.safeToString(processingFile.getStringValue(row.getCell(1))).trim();

            StockTransSerialDTO stockTransSerialDTOFile = new StockTransSerialDTO("", "", 0L);
            stockTransSerialDTOFile.setIndex((long) i);
            listSerialAll.add(stockTransSerialDTOFile);

            //setValue
            stockTransSerialDTOFile.setFromSerial(fromSerial);
            stockTransSerialDTOFile.setToSerial(toSerial);

            //neu gap dong trong thi bo qua
            if (DataUtil.isNullOrEmpty(fromSerial) || DataUtil.isNullOrEmpty(toSerial)) {
//                stockTransSerialDTOFile.putError(getMessage("mn.stock.partner.serial.valid.check.empty", stockTransFullDTO.getProdOfferName()));
//                errorImport = true;
                continue;
            }
            rowIndex++;
            if (rowIndex > Const.MAX_SIZE_ROW_UPLOAD) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "list.product.validate.maxRow", Const.MAX_SIZE_ROW_UPLOAD);
            }
            //validate do dai fromSerial, toSerial phai bang nhau
            if (fromSerial.length() != toSerial.length()) {
                stockTransSerialDTOFile.putError(getMessage("mn.stock.partner.length.fromSerial.toSerial.valid"));
                errorImport = true;
                continue;
            }
            if (Const.STOCK_TYPE.STOCK_HANDSET.equals(stockTransFullDTO.getProductOfferTypeId())) {
                if (!fromSerial.equals(toSerial)) {
                    stockTransSerialDTOFile.putError(getMessage("mn.stock.partner.serial.valid.hanset.valid", stockTransFullDTO.getProdOfferName()));
                    errorImport = true;
                    continue;
                }
                //Neu khong trung thi check trong list noi tai file
                for (StockTransSerialDTO serialDetail : listSerialAll) {
                    if (DataUtil.isNullOrEmpty(serialDetail.getErrorMessage()) && !DataUtil.safeEqual(serialDetail.getIndex(), (long) i)) {
                        if (fromSerial.equalsIgnoreCase(serialDetail.getFromSerial()) && toSerial.equalsIgnoreCase(serialDetail.getToSerial())) {
                            stockTransSerialDTOFile.putError(getMessage("mn.stock.partner.range.duplidate", fromSerial, toSerial));
                            errorImport = true;
                            break;
                        }
                    }
                }
                if (errorImport) {
                    continue;
                }
                //Neu khong trung thi check trong list noi tai hien thi
                for (StockTransSerialDTO serialDetail : listSerialRangeSelect) {
                    if (fromSerial.equalsIgnoreCase(serialDetail.getFromSerial()) && toSerial.equalsIgnoreCase(serialDetail.getToSerial())) {
                        stockTransSerialDTOFile.putError(getMessage("mn.stock.partner.range.duplidate", fromSerial, toSerial));
                        errorImport = true;
                        break;
                    }
                }
            } else {
                if (DataUtil.isNullOrEmpty(fromSerial) || DataUtil.isNullOrEmpty(toSerial)
                        || !DataUtil.validateStringByPattern(fromSerial, Const.REGEX.NUMBER_REGEX)
                        || !DataUtil.validateStringByPattern(toSerial, Const.REGEX.NUMBER_REGEX)) {
                    stockTransSerialDTOFile.putError(getMessage("mn.stock.partner.serial.valid.format", stockTransFullDTO.getProdOfferName()));
                    errorImport = true;
                    continue;
                }
                BigInteger fromSerialNum = new BigInteger(fromSerial);
                BigInteger toSerialNum = new BigInteger(toSerial);
                BigInteger result = toSerialNum.subtract(fromSerialNum);
                //neu serial ket thuc nho hon serial bat dau or serial ket thuc lon hon 500000 thi bao loi
                if (result.compareTo(BigInteger.ZERO) < 0 || result.add(BigInteger.ONE).compareTo(new BigInteger("500000")) > 0) {
                    stockTransSerialDTOFile.putError(getMessage("mn.stock.partner.serial.valid.range"));
                    errorImport = true;
                    continue;
                }
                //Check trong list tren datatable
                ranFile = Range.closed(fromSerialNum, toSerialNum);
                //Neu khong trung thi check trong list noi tai
                for (StockTransSerialDTO serialDetail : listSerialAll) {
                    if (DataUtil.isNullOrEmpty(serialDetail.getErrorMessage()) && !DataUtil.safeEqual(serialDetail.getIndex(), (long) i)) {
                        ranSelect = Range.closed(new BigInteger(serialDetail.getFromSerial()), new BigInteger(serialDetail.getToSerial()));
                        if (ranSelect.isConnected(ranFile)) {
                            stockTransSerialDTOFile.putError(getMessage("mn.stock.partner.range.duplidate", fromSerial, toSerial));
                            errorImport = true;
                            break;
                        }
                    }
                }
                if (errorImport) {
                    continue;
                }
                for (StockTransSerialDTO serialDetail : listSerialRangeSelect) {
                    ranSelect = Range.closed(new BigInteger(serialDetail.getFromSerial()), new BigInteger(serialDetail.getToSerial()));
                    if (ranSelect.isConnected(ranFile)) {
                        stockTransSerialDTOFile.putError(getMessage("mn.stock.partner.range.duplidate", fromSerial, toSerial));
                        errorImport = true;
                        break;
                    }
                }
            }
        }
        if (!listSerialAll.isEmpty()) {
            //xu ly tim kiem trong DB, ghep dai voi list serial doc tu file
            List<StockTransSerialDTO> listSerialTmp = getListSerialFile(stockTransDetail, listSerialAll);
            if (!listSerialTmp.isEmpty()) {
                listSerialRangeSelect.addAll(listSerialTmp);
            }
            for (StockTransSerialDTO stock : listSerialAll) {
                if (!DataUtil.isNullOrEmpty(stock.getErrorMessage())) {
                    errorImport = true;
                    break;
                }
            }
            if (errorImport) {
                lstErrorSerial = Lists.newArrayList(listSerialAll);
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "list.product.validate.errRead");
            }
        }
    }

    @Override
    public void doChangeTypePrint() {
        errorImport = false;
        listSerialRangeSelect = Lists.newArrayList();
        isViewBtnExportStock = false;
        //khi click xem chi tiet xu ly reset thong tin upload file
        fileUpload = null;
        byteContent = null;
        attachFileName = null;
        processingFile = null;
        this.stockTransDetail.setFromSerial(null);
        this.stockTransDetail.setToSerial(null);
        if (Const.PRINT_TYPE.PRINT_BY_INPUT.equals(this.typePrint)) {
            executeCommand("focusFromSerial");
        }
    }

    /**
     * ham tinh tong so luong hien co trong list serial
     *
     * @param lsSerial
     * @return
     * @author ThanhNT
     */
    private Long getTotalQuantity(List<StockTransSerialDTO> lsSerial) {
        Long totalQuantity = 0L;
        if (!DataUtil.isNullOrEmpty(lsSerial)) {
            for (StockTransSerialDTO serial : lsSerial) {
                totalQuantity += DataUtil.safeToLong(serial.getQuantity());
            }
        }
        return totalQuantity;
    }

    @Override
    public void doSelectSerial() {
        try {
            //check so luong mat hang hoa danh sach, neu =0 or = null thi bao loi
            if (DataUtil.isNullOrZero(stockTransDetail.getQuantity())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "mn.stock.serial.quantity.limit.zezo");
            }
            //xu ly check tong so luong serial
            Long totalQuantity = getTotalQuantity(listSerialRangeSelect);
            if (totalQuantity.compareTo(stockTransDetail.getQuantity()) >= 0) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "mn.stock.serial.quantity.limit");
            }
            //tinh so luong serial con lai
            Long quantityRemain = DataUtil.isNullOrEmpty(listSerialRangeSelect) ? stockTransDetail.getQuantity() : stockTransDetail.getQuantity() - totalQuantity;
            stockTransDetail.setQuantityRemain(quantityRemain);
            if (configTagDto.isBalanceStock() && DataUtil.safeEqual(configTagDto.getBalanceStockType(), Const.STOCK_BALANCE.BALANCE_TYPE_IMPORT)) {
                stockTransDetail.setSerialStatus(DataUtil.safeToLong(Const.SERIAL_STATUS_NOT_DIVIDE));
            }
            if (Const.PRINT_TYPE.PRINT_BY_INPUT.equals(typePrint)) {
                //xu ly validate, ghep dai serial nhap tren man hinh
                createStockDetailByInputSerial(this.stockTransDetail, this.listSerialRangeSelect);
            } else {
                BaseMessage message = validateFileUploadWhiteList(fileUpload, ALOW_EXTENSION_EXCEL_TYPE_LIST, MAX_SIZE_5M);
                if (!message.isSuccess()) {
                    clearFileUpload();
                    LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                    ex.setDescription(message.getDescription());
                    throw ex;
                }
                //xu ly valdiate, ghep dai serial nhap tu file
                createStockDetailByFile(this.processingFile, stockTransDetail, this.listSerialRangeSelect);
            }

        } catch (LogicException ex) {
            topReportError("", ex);
            if ("1".equals(typePrint)) {
                focusElementError("outputAttach");
            } else {
                focusElementError("fromSerial");
                focusElementError("toSerial");
            }
        } catch (Exception e) {
            topReportError("", "common.error.happened", e);
        }
        //xu ly check hien thi button xac nhan
        isViewBtnExportStock = checkViewBtnExportStock();
        stockTransDetail.setFromSerial(null);
        stockTransDetail.setToSerial(null);
        RequestContext.getCurrentInstance().execute("PrimeFaces.focus('frmDlgDetailSerial:fromSerial');");
    }

    @Override
    public void addListSerialAuto() {
        try {
            //check so luong mat hang hoa danh sach, neu =0 or = null thi bao loi
            if (DataUtil.isNullOrZero(stockTransDetail.getQuantity())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "mn.stock.serial.quantity.limit.zezo");
            }
            //xu ly check tong so luong serial
            Long totalQuantity = getTotalQuantity(listSerialRangeSelect);
            if (totalQuantity.compareTo(stockTransDetail.getQuantity()) >= 0) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "mn.stock.serial.quantity.limit");
            }
            //tinh so luong serial con lai
            Long quantityRemain = DataUtil.isNullOrEmpty(listSerialRangeSelect) ? stockTransDetail.getQuantity() : stockTransDetail.getQuantity() - totalQuantity;
            stockTransDetail.setQuantityRemain(quantityRemain);
            if (Const.PRINT_TYPE.PRINT_BY_INPUT.equals(typePrint)) {
                //xu ly validate, ghep dai serial nhap tren man hinh
                createStockDetailByInputSerial(this.stockTransDetail, this.listSerialRangeSelect);
            } else {
                //xu ly valdiate, ghep dai serial nhap tu file
                createStockDetailByFile(this.processingFile, stockTransDetail, this.listSerialRangeSelect);
            }
            //xu ly check hien thi button xac nhan
            isViewBtnExportStock = checkViewBtnExportStock();
        } catch (LogicException ex) {
            topReportError("", ex);
            if ("1".equals(typePrint)) {
                focusElementError("outputAttach");
            } else {
                focusElementError("fromSerial");
                focusElementError("toSerial");
            }
        } catch (Exception e) {
            topReportError("", "common.error.happened", e);
        }
        stockTransDetail.setFromSerial(null);
        stockTransDetail.setToSerial(null);
        RequestContext.getCurrentInstance().execute("PrimeFaces.focus('frmDlgDetailSerial:fromSerial');");
    }

    @Override
    public void doSelectSerialByProd() {
        try {
            errorImportProd = false;
            if (Const.MODE_SERIAL.MODE_ADD_SERIAL.equals(this.configTagDto.getModeSerial())) {
                BaseMessage message = validateFileUploadWhiteList(fileUploadByProd, ALOW_EXTENSION_EXCEL_TYPE_LIST, MAX_SIZE_5M);
                if (!message.isSuccess()) {
                    clearFileUploadProd();
                    LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                    ex.setDescription(message.getDescription());
                    throw ex;
                }

                createStockDetailProductdByFile(processingFileProd);
            }
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception e) {
            topReportError("", "common.error.happened", e);
        }
        //xu ly clear thong tin file upload
        clearFileUploadProd();
    }

    /**
     * /ham xu ly tao data serail bang giao dien
     *
     * @param stockDetail
     * @param listSerialRangeSelect
     * @throws Exception
     * @author ThanhNT
     */
    private void createStockDetailByInputSerial(StockTransFullDTO stockDetail, List<StockTransSerialDTO> listSerialRangeSelect) throws LogicException, Exception {
        String fromSerial = DataUtil.safeToString(stockDetail.getFromSerial()).trim();
        String toSerial = DataUtil.safeToString(stockDetail.getToSerial()).trim();
        if (DataUtil.isNullOrEmpty(fromSerial) || DataUtil.isNullOrEmpty(toSerial)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "mn.stock.partner.serial.valid.require");
        }
        if (fromSerial.length() != toSerial.length()) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.partner.length.fromSerial.toSerial.valid");
        }
        //check trung lap serial voi mat hang handset
        if (getHandset()) {
            for (StockTransSerialDTO serialDetail : listSerialRangeSelect) {
                if (fromSerial.equalsIgnoreCase(serialDetail.getFromSerial()) && toSerial.equalsIgnoreCase(serialDetail.getToSerial())) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.partner.range.duplidate", fromSerial, toSerial);
                }
            }
        } else { //check trung lap serial voi mat hang khac
            if (!DataUtil.validateStringByPattern(fromSerial, Const.REGEX.NUMBER_REGEX) || !DataUtil.validateStringByPattern(toSerial, Const.REGEX.NUMBER_REGEX)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "mn.stock.partner.serial.valid.format", stockTransDetail.getProdOfferName());
            }
            BigInteger fromSerialNum = new BigInteger(fromSerial);
            BigInteger toSerialNum = new BigInteger(toSerial);
            BigInteger result = toSerialNum.subtract(fromSerialNum);
            //neu serial ket thuc nho hon serial bat dau or serial ket thuc lon hon 500000 thi bao loi
            if (result.compareTo(new BigInteger("0")) < 0 || result.add(BigInteger.ONE).compareTo(new BigInteger("500000")) > 0) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "mn.stock.partner.serial.valid.range");
            }
            Range<BigInteger> ranInput = Range.closed(fromSerialNum, toSerialNum);
            Range<BigInteger> ranCurrent;
            for (StockTransSerialDTO serialDetail : listSerialRangeSelect) {
                BigInteger fromSerialCurrent = new BigInteger(serialDetail.getFromSerial());
                BigInteger toSerialCurrent = new BigInteger(serialDetail.getToSerial());
                ranCurrent = Range.closed(fromSerialCurrent, toSerialCurrent);
                if (ranCurrent.isConnected(ranInput)) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.partner.range.duplidate", fromSerial, toSerial);
                }
            }
        }
        //cu ly tach dai, validate serial

        List<StockTransSerialDTO> lsSelectSerialTmp = DataUtil.defaultIfNull(stockTransSerialService.getListSerialSelect(stockDetail), new ArrayList<StockTransSerialDTO>());
        if (!DataUtil.isNullOrEmpty(lsSelectSerialTmp)) {
            listSerialRangeSelect.addAll(lsSelectSerialTmp);
        }
    }

    /**
     * ham tra ve danh sach list serial hop le(list dai serial)
     *
     * @param stockTransFullDTO
     * @param stockTransSerialDTOs
     * @return
     * @throws LogicException
     * @throws Exception
     * @author ThanhNT77
     */
    private List<StockTransSerialDTO> getListSerialFile(StockTransFullDTO stockTransFullDTO, List<StockTransSerialDTO> stockTransSerialDTOs) throws Exception {
        List<StockTransSerialDTO> lsResult = Lists.newArrayList();
        //lay tong so serial can
        Long totalQuantity = stockTransFullDTO.getQuantityRemain();
        String status = Const.STATUS.ACTIVE;
        if (Const.GOODS_STATE.RESCUE.equals(stockTransFullDTO.getStateId()) || Const.GOODS_STATE.DEMO.equals(stockTransFullDTO.getStateId())) {
            status = Const.STATUS.NO_ACTIVE;
        }
        if (configTagDto.isBalanceStock() && DataUtil.safeEqual(configTagDto.getBalanceStockType(), Const.STOCK_BALANCE.BALANCE_TYPE_IMPORT)) {
            status = Const.STATUS.NO_ACTIVE;
            stockTransFullDTO.setSerialStatus(DataUtil.safeToLong(Const.STATUS.NO_ACTIVE));
        }
        Long fromOwnerId = stockTransFullDTO.getFromOwnerId();
        if (!DataUtil.isNullOrZero(stockTransFullDTO.getRegionStockTransId())) {
            Long stockTransId = stockTransFullDTO.getRegionStockTransId();
            StockTransDTO stockTransDTO = stockTransService.findOne(stockTransId);
            fromOwnerId = stockTransDTO.getFromOwnerId();
        }
        //neu la stock_card
        if (Const.STOCK_TYPE.STOCK_CARD_NAME.equals(stockTransFullDTO.getTableName())) {
            List<String> lsSerialNumber = Lists.newArrayList();
            for (StockTransSerialDTO stockTransSerialDTO : stockTransSerialDTOs) {
                if (!DataUtil.isNullOrEmpty(stockTransSerialDTO.getErrorMessage())
                        || DataUtil.isNullOrEmpty(stockTransSerialDTO.getFromSerial())
                        || DataUtil.isNullOrEmpty(stockTransSerialDTO.getToSerial())) {
                    continue;
                }
                //lay ra danh sach serial stockcard
                List<String> lsSerialFromDB = DataUtil.defaultIfNull(stockTransSerialService.getRangeSerialStockCardValid(DataUtil.safeToLong(stockTransFullDTO.getFromOwnerType()), fromOwnerId,
                        stockTransFullDTO.getProdOfferId(), stockTransFullDTO.getStateId(), stockTransSerialDTO.getFromSerial(), stockTransSerialDTO.getToSerial(), totalQuantity, status), new ArrayList<>());
                //tinh so luong serial con lai
                if (!DataUtil.isNullOrEmpty(lsSerialFromDB)) {
                    Long countSerial = DataUtil.safeToLong(lsSerialFromDB.size());
                    if (totalQuantity >= countSerial) {
                        totalQuantity = totalQuantity - countSerial;
                        lsSerialNumber.addAll(lsSerialFromDB);
                    } else {
                        lsSerialNumber.addAll(lsSerialFromDB.subList(0, DataUtil.safeToInt(totalQuantity)));
                    }
                } else {
                    stockTransSerialDTO.putError(getText("list.product.validate.noserial"));
                }
            }
            //xu ly ghep dai
            lsResult = DataUtil.defaultIfNull(stockTransSerialService.getListSerialGroup(lsSerialNumber), new ArrayList<>());
        } else {
            List<String> lsSerialAll = Lists.newArrayList();
            for (StockTransSerialDTO stockTransSerialDTO : stockTransSerialDTOs) {
                if (!DataUtil.isNullOrEmpty(stockTransSerialDTO.getErrorMessage())
                        || DataUtil.isNullOrEmpty(stockTransSerialDTO.getFromSerial())
                        || DataUtil.isNullOrEmpty(stockTransSerialDTO.getToSerial())) {
                    continue;
                }
                //tim trong database xem serial co thoa man hay ko
                List<String> lsSerialFromDB = DataUtil.defaultIfNull(stockTransSerialService.getListSerialValid(DataUtil.safeToLong(stockTransFullDTO.getFromOwnerType()), fromOwnerId,
                        stockTransFullDTO.getTableName(), stockTransFullDTO.getProdOfferId(), stockTransFullDTO.getStateId(),
                        stockTransSerialDTO.getFromSerial(), stockTransSerialDTO.getToSerial(), totalQuantity, stockTransFullDTO.getSerialStatus()), new ArrayList<>());
                //tinh so luong serial con lai
                if (!DataUtil.isNullOrEmpty(lsSerialFromDB)) {
                    Long countSerial = DataUtil.safeToLong(lsSerialFromDB.size());
                    if (totalQuantity >= countSerial) {
                        totalQuantity = totalQuantity - countSerial;
                        lsSerialAll.addAll(lsSerialFromDB);
                    } else {
                        lsSerialAll.addAll(lsSerialFromDB.subList(0, DataUtil.safeToInt(totalQuantity)));
                    }
                } else {
                    stockTransSerialDTO.putError(getText("list.product.validate.noserial"));
                }
            }
            Collections.sort(lsSerialAll);
            //neu mat hang la handset
            if (Const.STOCK_TYPE.STOCK_HANDSET.equals(stockTransFullDTO.getProductOfferTypeId())) {
                //sap xep theo chieu tang dan cua serial handset
                for (String serial : lsSerialAll) {
                    lsResult.add(new StockTransSerialDTO(serial, serial, 1L));
                }
            } else {
                //neu mat hang ko phai la handset chuyen no sang kieu so
                //voi serial kieu so xu ly ghep dai
                lsResult = DataUtil.defaultIfNull(stockTransSerialService.getListSerialGroup(lsSerialAll), new ArrayList<>());
            }
        }
        return lsResult;
    }

    public String getTitleSerialProduct() {
        return getTextParam("mn.invoice.invoiceSerial.infor.detail", stockTransDetail != null && !DataUtil.isNullOrEmpty(stockTransDetail.getProdOfferName()) ? stockTransDetail.getProdOfferName() : "");
    }

    @Override
    public boolean getHandset() {
        return stockTransDetail != null && Const.STOCK_TYPE.STOCK_HANDSET.equals(stockTransDetail.getProductOfferTypeId());
    }

    /**
     * ham check hien thi button xac nhan
     *
     * @return true neu co hien thi button len
     */
    private boolean checkViewBtnExportStock() {
        Long totalAccount = 0L;
        for (StockTransSerialDTO stockTransSerialDTO : listSerialRangeSelect) {
            totalAccount += stockTransSerialDTO.getQuantity();
        }
        return stockTransDetail != null && stockTransDetail.getQuantity() != null && totalAccount.equals(stockTransDetail.getQuantity());
    }

    /**
     * ham check hien thi vung data upload
     *
     * @return true neu co hien thi button len
     */
    public boolean getShowUploadProdByFile() {
        if (DataUtil.safeEqual(Const.MODE_SERIAL.MODE_ADD_SERIAL, this.configTagDto.getModeSerial())) {
            for (StockTransFullDTO stock : lsStockTransFull) {
                if (Const.PRODUCT_OFFERING._CHECK_SERIAL.equals(stock.getCheckSerial()) && !stock.getHaveListSerial()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void doShowHideListSerial() {
        try {
            listSerialView = Lists.newArrayList();
            Long fromOwnerId = stockTransDetail.getFromOwnerId();
            if (!DataUtil.isNullOrZero(stockTransDetail.getRegionStockTransId())) {
                Long stockTransId = stockTransDetail.getRegionStockTransId();
                StockTransDTO stockTransDTO = stockTransService.findOne(stockTransId);
                fromOwnerId = stockTransDTO.getFromOwnerId();
            }
            Long serialStatus = null;
            if (configTagDto.isBalanceStock() && DataUtil.safeEqual(configTagDto.getBalanceStockType(), Const.STOCK_BALANCE.BALANCE_TYPE_IMPORT)) {
                serialStatus = Const.STOCK_BALANCE.BALANCE_TYPE_IMPORT;
            }
            listSerialView = DataUtil.defaultIfNull(stockTransSerialService.getRangeSerial(DataUtil.safeToLong(stockTransDetail.getFromOwnerType()), fromOwnerId,
                    stockTransDetail.getProdOfferId(), stockTransDetail.getTableName(), stockTransDetail.getStateId(), "", serialStatus), new ArrayList<StockTransSerialDTO>());
           /* DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("frmDlgDetailSerial:lstDataExport");
            if (dataTable != null) {
                dataTable.setFirst(0);
                dataTable.setRows(DataUtil.safeToInt(getText("common.paging.rows.short")));
            }*/
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            topReportError("", ex);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            topReportError("", "common.error.happened", ex);
        }
    }

    @Override
    public void doConfirmSerial() {
        if (DataUtil.isNullOrEmpty(listSerialRangeSelect)) {
            reportError("", ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.trans.search.empty.serialList");
            return;
        }
        if (isAddNewProduct) {
            ListProductOfferDTO offerDTO = lsListProductOfferDTO.get(index);
            if (offerDTO != null) {
                offerDTO.setLstSerial(listSerialRangeSelect);
            }
        } else {
            stockTransDetail.setLstSerial(listSerialRangeSelect);
            lsStockTransFull.set(index, this.stockTransDetail);
        }
    }

    /**
     * ham open view serial tren dialog
     *
     * @author ThanhNT77
     */
    @Override
    public void doRemoveSerial(int index) {
        try {
            listSerialRangeSelect.remove(index);
            isViewBtnExportStock = checkViewBtnExportStock();
        } catch (Exception e) {
            topReportError("", "common.error.happened", e);
        }
    }

    /**
     * xu ly lua chon row tren dialog list serial
     *
     * @author ThanhNT77
     */
    @Override
    public void onRowSelect(SelectEvent event) {
        StockTransSerialDTO detailDTO = (StockTransSerialDTO) event.getObject();
        this.stockTransDetail.setFromSerial(detailDTO.getFromSerial());
        this.stockTransDetail.setToSerial(detailDTO.getToSerial());
    }

    /**
     * ham tra ve trang thai san pham
     *
     * @param stateId
     * @return
     * @author ThanhNT77
     */
    private String getStateName(String stateId) {
        if (!DataUtil.isNullOrEmpty(lsProductStatus) && stateId != null) {
            for (OptionSetValueDTO option : lsProductStatus) {
                if (stateId.equals(option.getValue())) {
                    return option.getName();
                }
            }
        }
        return "";
    }

    @Override
    public void preToShowDeviceConfig(int index) {
        try {
            ListProductOfferDTO listProductOfferDTO = getLsListProductOfferDTO().get(index);
            this.index = index;
            if (!DataUtil.isNullOrEmpty(listProductOfferDTO.getLstStockDeviceTransfer()))
                return;
            List<DeviceConfigDTO> deviceConfigByProductAndState = DataUtil.defaultIfNull(deviceConfigService.getDeviceConfigByProductAndState(listProductOfferDTO.getProductOfferingTotalDTO().getProductOfferingId(), listProductOfferDTO.getStateId()), new ArrayList<>());
            List<StockDeviceTransferDTO> lstStockDeviceTransfer = new ArrayList<>();
            for (DeviceConfigDTO deviceConfigDTO : deviceConfigByProductAndState) {
                StockDeviceTransferDTO stockDeviceTransferDTO = new StockDeviceTransferDTO();
                stockDeviceTransferDTO.setProdOfferId(deviceConfigDTO.getProdOfferId());
                stockDeviceTransferDTO.setNewProdOfferId(deviceConfigDTO.getNewProdOfferId());
                String stateId = listProductOfferDTO.getProductOfferingTotalDTO().getStateId().toString();
                stockDeviceTransferDTO.setStateId(DataUtil.safeToLong(stateId));
                stockDeviceTransferDTO.setNewStateId(stateId.equals(Const.GOODS_STATE.NEW.toString()) ? Long.valueOf(Const.GOODS_STATE.NEW.toString()) : null);
                ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(deviceConfigDTO.getNewProdOfferId());
                stockDeviceTransferDTO.setNewProbOfferCode(productOfferingDTO.getCode());
                stockDeviceTransferDTO.setNewProbOfferName(productOfferingDTO.getName());

                lstStockDeviceTransfer.add(stockDeviceTransferDTO);
            }
            listProductOfferDTO.setLstStockDeviceTransfer(lstStockDeviceTransfer);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            reportError("", "", "common.error.happened");
        }
    }

    @Override
    public List<StockDeviceTransferDTO> getCurrentDeviceConfig() {

        try {
            return getLsListProductOfferDTO().get(index).getLstStockDeviceTransfer();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    @Override
    public ListProductOfferDTO getCurrentProduct() {

        try {
            return getLsListProductOfferDTO().get(index);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return new ListProductOfferDTO();

    }

    @Override
    public StreamedContent exportImportErrorFile() {
        try {
            FileExportBean bean = new FileExportBean();
            bean.setTempalatePath(FileUtil.getTemplatePath());
            bean.setOutName("error_import_serial.xls");
            bean.setOutPath(FileUtil.getOutputPath());
            bean.setTemplateName(Const.REPORT_TEMPLATE.IMPORT_SERIAL_ERROR);
            bean.putValue("create_date", getSysdateFromDB());
            bean.putValue("lstSerial", lstErrorSerial);
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

    @Override
    public StreamedContent exportImportListErrorFile() {
        try {
            FileExportBean bean = new FileExportBean();
            bean.setTempalatePath(FileUtil.getTemplatePath());
            bean.setOutName("err_import_list_product.xls");
            bean.setOutPath(FileUtil.getOutputPath());
            bean.setTemplateName(Const.REPORT_TEMPLATE.IMPORT_LIST_PRODUCT_ERROR);
            bean.putValue("create_date", getSysdateFromDB());
            bean.putValue("lstErrorSerialProd", lstErrorSerialProd);
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
    public String getTotalSerialSelect() {
        if (!DataUtil.isNullOrEmpty(listSerialRangeSelect)) {
            Long total = 0L;
            for (StockTransSerialDTO dto : listSerialRangeSelect) {
                total += DataUtil.safeToLong(dto.getQuantity());
            }
            return ": " + total;
        }
        return "";
    }

    //getter and setter
    @Override
    public Object getObjectController() {
        return objectController;
    }

    @Override
    public void setObjectController(Object objectController) {
        this.objectController = objectController;
    }

    @Override
    public int getLimitAutoComplete() {
        return limitAutoComplete;
    }

    @Override
    public void setLimitAutoComplete(int limitAutoComplete) {
        this.limitAutoComplete = limitAutoComplete;
    }

    @Override
    public List<ListProductOfferDTO> getLsListProductOfferDTO() {
        return lsListProductOfferDTO;
    }

    @Override
    public void setLsListProductOfferDTO(List<ListProductOfferDTO> lsListProductOfferDTO) {
        this.lsListProductOfferDTO = lsListProductOfferDTO;
    }

    @Override
    public List<OptionSetValueDTO> getLsProductStatus() {
        return lsProductStatus;
    }

    @Override
    public void setLsProductStatus(List<OptionSetValueDTO> lsProductStatus) {
        this.lsProductStatus = lsProductStatus;
    }

    @Override
    public List<ProductOfferTypeDTO> getLsProductOfferTypeDTOTmp() {
        return lsProductOfferTypeDTOTmp;
    }

    @Override
    public void setLsProductOfferTypeDTOTmp(List<ProductOfferTypeDTO> lsProductOfferTypeDTOTmp) {
        this.lsProductOfferTypeDTOTmp = lsProductOfferTypeDTOTmp;
    }

    public List<ProductOfferingTotalDTO> getLsProductOfferingTotalDTOTmp() {
        return lsProductOfferingTotalDTOTmp;
    }

    public void setLsProductOfferingTotalDTOTmp(List<ProductOfferingTotalDTO> lsProductOfferingTotalDTOTmp) {
        this.lsProductOfferingTotalDTOTmp = lsProductOfferingTotalDTOTmp;
    }

    public List<StockTransFullDTO> getLsStockTransFull() {
        return lsStockTransFull;
    }

    public void setLsStockTransFull(List<StockTransFullDTO> lsStockTransFull) {
        this.lsStockTransFull = lsStockTransFull;
    }

    public List<StockTransSerialDTO> getListSerialView() {
        return listSerialView;
    }

    public void setListSerialView(List<StockTransSerialDTO> listSerialView) {
        this.listSerialView = listSerialView;
    }

    public StockTransSerialDTO getSerialViewSelected() {
        return serialViewSelected;
    }

    public void setSerialViewSelected(StockTransSerialDTO serialViewSelected) {
        this.serialViewSelected = serialViewSelected;
    }

    public List<StockTransSerialDTO> getListSerialRangeSelect() {
        return listSerialRangeSelect;
    }

    public void setListSerialRangeSelect(List<StockTransSerialDTO> listSerialRangeSelect) {
        this.listSerialRangeSelect = listSerialRangeSelect;
    }

    public ExcellUtil getProcessingFile() {
        return processingFile;
    }

    public void setProcessingFile(ExcellUtil processingFile) {
        this.processingFile = processingFile;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public boolean getShowSerial() {
        return isShowSerial;
    }

    public void setShowSerial(boolean isShowSerial) {
        this.isShowSerial = isShowSerial;
    }

    public boolean isAddNewProduct() {
        return isAddNewProduct;
    }

    public void setAddNewProduct(boolean isAddNewProduct) {
        this.isAddNewProduct = isAddNewProduct;
    }

    public String getAttachFileName() {
        return attachFileName;
    }

    public void setAttachFileName(String attachFileName) {
        this.attachFileName = attachFileName;
    }

    public byte[] getByteContent() {
        return byteContent;
    }

    public void setByteContent(byte[] byteContent) {
        this.byteContent = byteContent;
    }

    public UploadedFile getFileUpload() {
        return fileUpload;
    }

    public void setFileUpload(UploadedFile fileUpload) {
        this.fileUpload = fileUpload;
    }

    public String getTypePrint() {
        return typePrint;
    }

    public void setTypePrint(String typePrint) {
        this.typePrint = typePrint;
    }

    public boolean isViewBtnExportStock() {
        return isViewBtnExportStock;
    }

    public void setViewBtnExportStock(boolean isViewBtnExportStock) {
        this.isViewBtnExportStock = isViewBtnExportStock;
    }

    public StockTransFullDTO getStockTransDetail() {
        return stockTransDetail;
    }

    public void setStockTransDetail(StockTransFullDTO stockTransDetail) {
        this.stockTransDetail = stockTransDetail;
    }

    public ShopInfoNameable getShopInfoTagExportDlg() {
        return shopInfoTagExportDlg;
    }

    public void setShopInfoTagExportDlg(ShopInfoNameable shopInfoTagExportDlg) {
        this.shopInfoTagExportDlg = shopInfoTagExportDlg;
    }

    public Long getTotalPriceAmount() {
        return totalPriceAmount;
    }

    public void setTotalPriceAmount(Long totalPriceAmount) {
        this.totalPriceAmount = totalPriceAmount;
    }

    public ConfigListProductTagDTO getConfigTagDto() {
        return configTagDto;
    }

    public void setConfigTagDto(ConfigListProductTagDTO configTagDto) {
        this.configTagDto = configTagDto;
    }

    //tuydv1

    public List<OptionSetValueDTO> getLsProductStatusAfter() {
        return lsProductStatusAfter;
    }

    public void setLsProductStatusAfter(List<OptionSetValueDTO> lsProductStatusAfter) {
        this.lsProductStatusAfter = lsProductStatusAfter;
    }

    public boolean getShowForRetrieveStock() {
        return this.isShowForRetrieveStock;
    }

    public void setShowForRetrieveStock(boolean isShowForRetrieveStock) {
        this.isShowForRetrieveStock = isShowForRetrieveStock;
    }

    @Override
    public boolean isErrorImport() {
        return errorImport;
    }

    public ExcellUtil getProcessingFileProd() {
        return processingFileProd;
    }

    public void setProcessingFileProd(ExcellUtil processingFileProd) {
        this.processingFileProd = processingFileProd;
    }

    public UploadedFile getFileUploadByProd() {
        return fileUploadByProd;
    }

    public void setFileUploadByProd(UploadedFile fileUploadByProd) {
        this.fileUploadByProd = fileUploadByProd;
    }

    public byte[] getByteContentByProd() {
        return byteContentByProd;
    }

    public void setByteContentByProd(byte[] byteContentByProd) {
        this.byteContentByProd = byteContentByProd;
    }

    public String getAttachFileNameByProd() {
        return attachFileNameByProd;
    }

    public void setAttachFileNameByProd(String attachFileNameByProd) {
        this.attachFileNameByProd = attachFileNameByProd;
    }

    public void setErrorImport(boolean errorImport) {
        this.errorImport = errorImport;
    }

    public boolean isShowSerial() {
        return isShowSerial;
    }

    public boolean isShowForRetrieveStock() {
        return isShowForRetrieveStock;
    }

    public List<StockTransSerialDTO> getLstErrorSerial() {
        return lstErrorSerial;
    }

    public void setLstErrorSerial(List<StockTransSerialDTO> lstErrorSerial) {
        this.lstErrorSerial = lstErrorSerial;
    }

    public boolean isErrorImportProd() {
        return errorImportProd;
    }

    public void setErrorImportProd(boolean errorImportProd) {
        this.errorImportProd = errorImportProd;
    }

    public List<StockTransFullDTO> getLstErrorSerialProd() {
        return lstErrorSerialProd;
    }

    public void setLstErrorSerialProd(List<StockTransFullDTO> lstErrorSerialProd) {
        this.lstErrorSerialProd = lstErrorSerialProd;
    }
}
