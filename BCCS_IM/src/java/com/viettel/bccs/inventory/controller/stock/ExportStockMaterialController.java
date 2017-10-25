package com.viettel.bccs.inventory.controller.stock;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.inventory.tag.ListProductNameable;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.bccs.inventory.tag.SignOfficeTagNameable;
import com.viettel.bccs.inventory.tag.StaffInfoNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.dto.BaseMessage;
import org.primefaces.event.FileUploadEvent;
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
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * controller chuc nang chuyen doi hang DOA
 *
 * @author thanhnt77
 */
@Component
@Scope("view")
@ManagedBean
public class ExportStockMaterialController extends TransCommonController {

    private boolean showSearch = true;
    private int limitAutoComplete;
    private boolean disableChangeMaterial;
    private boolean viewMaterial;
    private String attachFileName;

    private byte[] byteContent;
    private List<Long> lsShopIdConfig;
    private UploadedFile uploadedFile;
    private StaffDTO staffDTO;
    private VStockTransDTO forSearch;
    private VStockTransDTO forSearchSelect;
    private VStockTransDTO forSearchSelectView;
    private ConfigListProductTagDTO configListProductTagDTO;
    private VShopStaffDTO shopDTOSearch;
    private VShopStaffDTO shopDTOCreate;

    private List<StockTransFullDTO> lsStockTransFull;
    private List<StockTransSerialDTO> lsSerial = Lists.newArrayList();
    private List<VShopStaffDTO> lsShop = Lists.newArrayList();
    private List<VStockTransDTO> vStockTransDTOList;

    @Autowired
    StockOrderAgentService stockOrderAgentService;
    @Autowired
    StockOrderAgentDetailService stockOrderAgentDetailService;
    @Autowired
    ShopService shopService;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private ListProductNameable listProductTag;//khai bao tag danh sach hang hoa
    @Autowired
    private StockRequestService stockRequestService;
    @Autowired
    private StockTransActionService stockTransActionService;
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private StockTransMaterialService stockTransMaterialService;
    @Autowired
    private StockTransDocumentService stockTransDocumentService;

    @PostConstruct
    public void init() {
        try {
            initData();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happen");
        }
    }

    /**
     * author thanhnt77
     *
     * @throws LogicException
     * @throws Exception
     */
    private void initData() throws LogicException, Exception {
        staffDTO = BccsLoginSuccessHandler.getStaffDTO();
        ShopDTO shopDTO = shopService.findOne(staffDTO.getShopId());
        if (shopDTO == null || !Const.STATUS_ACTIVE.equals(shopDTO.getStatus())) {
            throw new LogicException("04", "warranty.create.underlying.fromOwnerId.exp.shop.notfound", staffDTO.getShopCode());
        }

        limitAutoComplete = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_AUTOCOMPLETE));
        Date currentDate = optionSetValueService.getSysdateFromDB(true);
        forSearch = new VStockTransDTO();
        forSearch.setStartDate(currentDate);
        forSearch.setEndDate(currentDate);
        forSearch.setFromOwnerID(null);
        forSearch.setFromOwnerName(null);
        forSearch.setFromOwnerType(null);
        shopDTOSearch = new VShopStaffDTO();

        String shopCodes = DataUtil.safeToString(optionSetValueService.getValueByTwoCodeOption(Const.OPTION_SET.MASTERIAL_STOCK, Const.OPTION_SET.MASTERIAL_STOCK));
        lsShop = DataUtil.defaultIfNull(shopService.getListShopByListShopCode("", DataUtil.safeToString(shopDTO.getShopPath()), Arrays.asList(shopCodes.split(","))), new ArrayList<>());

        lsShopIdConfig = lsShop.stream().map(x-> DataUtil.safeToLong(x.getOwnerId())).collect(Collectors.toList());

        if (lsShopIdConfig.contains(staffDTO.getShopId())) {
            forSearch.setFromOwnerID(staffDTO.getShopId());
            forSearch.setFromOwnerName(Joiner.on("-").join(Lists.newArrayList(staffDTO.getShopCode(), staffDTO.getShopName())));
            forSearch.setFromOwnerType(Const.OWNER_TYPE.SHOP_LONG);
            shopDTOSearch = shopService.getShopByOwnerId(DataUtil.safeToString(staffDTO.getShopId()), Const.OWNER_TYPE.SHOP);
        }

        configListProductTagDTO = new ConfigListProductTagDTO(Const.CONFIG_PRODUCT.TYPE_MATERIAL, Const.MODE_SERIAL.MODE_NO_SERIAL, staffDTO.getShopId(), Const.OWNER_TYPE.SHOP, "2");//hang vat tu
        listProductTag.init(this, configListProductTagDTO);
        disableChangeMaterial = false;
        vStockTransDTOList = Lists.newArrayList();
    }

    /**
     * ham check neu la vt shop
     * @athor thanhnt77
     * @return
     */
    public boolean isVTShop() {
        return (Const.L_VT_SHOP_ID.equals(staffDTO.getShopId()));
    }

    /**
     * ham xu ly tim kiem DOA
     *
     * @author thanhnt77
     */
    @Secured("@")
    public void doSearch() {
        try {
            forSearch.setActionType(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
            forSearch.setStockTransStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
            vStockTransDTOList = Lists.newArrayList();
            vStockTransDTOList = stockTransService.searchMaterialVStockTrans(forSearch);
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception e) {
            logger.error(e);
            reportError("", "common.error.happened", e);
            topPage();
        }
    }

    /**
     * ham xu ly reset thong tin tim kiem
     *
     * @author thanhnt77
     */
    @Secured("@")
    public void doResetSearch() {
        try {
            initData();
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
    }

    /**
     * xu ly lap yeu cau, hien thi vung thong tin lap yeu cau
     *
     * @author thanhnt77
     */
    @Secured("@")
    public void doCreateRequest() {
        try {
            showSearch = false;
            forSearchSelect = new VStockTransDTO();

            forSearchSelect.setStaffId(staffDTO.getStaffId());
            configListProductTagDTO = new ConfigListProductTagDTO(Const.CONFIG_PRODUCT.TYPE_MATERIAL, Const.MODE_SERIAL.MODE_NO_SERIAL, staffDTO.getShopId(), Const.OWNER_TYPE.SHOP, "2");//hang vat tu
            listProductTag.init(this, configListProductTagDTO);
            disableChangeMaterial = false;


            forSearchSelect.setFromOwnerID(null);
            forSearchSelect.setFromOwnerName(null);
            forSearchSelect.setFromOwnerType(null);
            shopDTOCreate = new VShopStaffDTO();

            if (lsShopIdConfig.contains(staffDTO.getShopId())) {
                forSearchSelect.setFromOwnerType(Const.OWNER_TYPE.SHOP_LONG);
                forSearchSelect.setFromOwnerID(staffDTO.getShopId());
                forSearchSelect.setFromOwnerName(DataUtil.safeToString(staffDTO.getShopCode()+ "-" + DataUtil.safeToString(staffDTO.getShopName())));
                shopDTOCreate = shopService.getShopByOwnerId(DataUtil.safeToString(staffDTO.getShopId()), Const.OWNER_TYPE.SHOP);
            }

            clearUpload();
        } catch (Exception e) {
            logger.error(e);
            reportError("", "common.error.happened", e);
            topPage();
        }
    }

    /**
     * ham validate yeu cau
     *
     * @author thanhnt77
     */
    @Secured("@")
    public void doValidRequest() {
        try {
            if (DataUtil.isNullOrEmpty(attachFileName)
                    || DataUtil.isNullObject(byteContent)
                    || attachFileName.length() > 200
                    || byteContent.length == 0
                    || byteContent.length > Const.FILE_MAX) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.export.material.limit.stock.valid.file");
            }

            if (lsShopIdConfig != null && !lsShopIdConfig.contains(forSearchSelect.getFromOwnerID())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.export.material.invalid.shopcode");
            }
            List<StockTransDetailDTO> lsStockTransFull = listProductTag.getListTransDetailDTOs();
            if (DataUtil.isNullOrEmpty(lsStockTransFull)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "export.stock.goods.not.found.list.prod");
            }
            for (StockTransDetailDTO stockTransFullDTO : lsStockTransFull) {
                if (Const.PRODUCT_OFFERING.CHECK_SERIAL.equals(stockTransFullDTO.getCheckSerial()) && DataUtil.isNullOrEmpty(stockTransFullDTO.getLstSerial())) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.confirm.serial.empty", stockTransFullDTO.getProdOfferName());
                }
            }
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

    public boolean isDisabledShopSearch() {
        return !DataUtil.isNullObject(shopDTOSearch) && !DataUtil.isNullOrEmpty(shopDTOSearch.getOwnerId());
    }

    public boolean isDisabledShopCreate() {
        return !DataUtil.isNullObject(shopDTOCreate) && !DataUtil.isNullOrEmpty(shopDTOCreate.getOwnerId());
    }

    /**
     * ham chuyen doi yeu cau
     *
     * @author thanhnt77
     */
    @Secured("@")
    public void doExportMaterial() {
        try {
            //lay thong tin mat hang
            List<StockTransDetailDTO> stockTransDetailDTOs = listProductTag.getListTransDetailDTOs();
            forSearchSelect.setTransDetailDTOs(stockTransDetailDTOs);
            forSearchSelect.setStaffId(staffDTO.getStaffId());
            forSearchSelect.setUserCreate(staffDTO.getStaffCode());
            forSearchSelect.setTransDetailDTOs(stockTransDetailDTOs);
            forSearchSelect.setFileAttach(byteContent);
            //thuc hien xuat kho vat tu
            stockTransMaterialService.excuteCreateTransMaterial(forSearchSelect);
            reportSuccess("", "stock.export.material.sucess");
            disableChangeMaterial = true;
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception e) {
            logger.error(e);
            reportError("", "common.error.happened", e);
            topPage();
        }
    }


    /**
     * ham validate yeu cau
     *
     * @author thanhnt77
     */
    @Secured("@")
    public void doResetRequest() {
        doCreateRequest();
        disableChangeMaterial = false;
    }

    /**
     * ham xem chi tiet yeu cau
     *
     * @author thanhnt77
     */
    public void doShowInfoOrderDetail(VStockTransDTO vStockTransDTO) {
        try {
            Long stockTransId = vStockTransDTO.getStockTransID();
            StockTransActionDTO stockTransActionDTO = stockTransActionService.getStockTransActionByIdAndStatus(stockTransId, Lists.newArrayList(Const.STOCK_TRANS_STATUS.EXPORT_NOTE));
            if (stockTransActionDTO != null) {
                lsStockTransFull = DataUtil.defaultIfNull(stockTransService.searchStockTransDetail(Lists.newArrayList(stockTransActionDTO.getStockTransActionId())), new ArrayList<>());
                for (StockTransFullDTO stockTransFullDTO : lsStockTransFull) {
                    if ("1".equals(stockTransFullDTO.getCheckSerial())) {
                        stockTransFullDTO.setLstSerial(DataUtil.defaultIfNull(stockTransSerialService.findByStockTransDetailId(stockTransFullDTO.getStockTransDetailId()), new ArrayList<>()));
                    }
                }
            }
            viewMaterial = true;
            forSearchSelectView = DataUtil.cloneBean(vStockTransDTO);
            if (Const.OWNER_TYPE.SHOP_LONG.equals(vStockTransDTO.getFromOwnerType())) {
                ShopDTO shopDTO = shopService.findOne(vStockTransDTO.getFromOwnerID());
                if (shopDTO != null) {
                    forSearchSelectView.setFromOwnerName(Joiner.on("-").skipNulls().join(Lists.newArrayList(shopDTO.getShopCode(), shopDTO.getName())));
                }
            }
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

    /**
     * ham xem serial
     *
     * @param stockTransDetailId
     * @author thanhnt77
     */
    public void doShowViewSerial(Long stockTransDetailId) {
        try {
            lsSerial = DataUtil.defaultIfNull(stockTransSerialService.findByStockTransDetailId(stockTransDetailId), new ArrayList<>());
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

    /**
     * ham validate yeu cau
     *
     * @author thanhnt77
     */
    @Secured("@")
    public void doBackPage() {
        showSearch = true;
        viewMaterial = false;
        doSearch();
    }

    /**
     * hien thi danh sach goi cuoc theo thong tin nhap tren man hinh
     *
     * @param inputShop giá trị input của gói cước
     * @return List<Product> danh sach sản phẩm
     */
    public List<VShopStaffDTO> doSelectShop(String inputShop) {
        CharSequence charInput = DataUtil.isNullOrEmpty(inputShop) ? "" : inputShop.toLowerCase().trim();
        return DataUtil.defaultIfNull(lsShop.stream().filter(x -> (DataUtil.safeToString(x.getOwnerCode()).toLowerCase().contains(charInput)
                || DataUtil.safeToString(x.getOwnerName()).toLowerCase().contains(charInput))).collect(Collectors.toList()), new ArrayList<>());
    }

    /**
     * ham nhan du lieu tu tag shop
     *
     * @author thanhnt77
     */
    @Secured("@")
    public void receiveSearchShop() {
        forSearch.setFromOwnerID(DataUtil.safeToLong(shopDTOSearch.getOwnerId()));
        forSearch.setFromOwnerType(Const.OWNER_TYPE.SHOP_LONG);
    }

    /**
     * ham clear du lieu tu tag shop
     *
     * @author thanhnt77
     */
    @Secured("@")
    public void clearSearchShop() {
        shopDTOSearch = new VShopStaffDTO();
    }

    /**
     * ham nhan du lieu tu tag shop
     *
     * @author thanhnt77
     */
    @Secured("@")
    public void receiveInput() {
        forSearchSelect.setFromOwnerID(DataUtil.safeToLong(shopDTOCreate.getOwnerId()));
        forSearchSelect.setFromOwnerType(Const.OWNER_TYPE.SHOP_LONG);
        configListProductTagDTO = new ConfigListProductTagDTO(Const.CONFIG_PRODUCT.TYPE_MATERIAL, Const.MODE_SERIAL.MODE_NO_SERIAL, forSearchSelect.getFromOwnerID(), Const.OWNER_TYPE.SHOP, "2");//hang vat tu
        listProductTag.init(this, configListProductTagDTO);
        updateElemetId("frmChangeDOA:pnlListProduct");
        updateElemetId("frmChangeDOA:msgSearch");
    }

    /**
     * ham clear du lieu tu tag shop
     *
     * @author thanhnt77
     */
    @Secured("@")
    public void clearInputShop() {
        forSearchSelect.setFromOwnerID(null);
        forSearchSelect.setFromOwnerType(null);
        forSearchSelect.setFromOwnerName(null);
        shopDTOCreate = new VShopStaffDTO();
        listProductTag.setDisableRemove();
    }

    @Secured("@")
    public String getStrStatus(Long status) {
        String strStatus = "";
        if (status == 0L) {
            strStatus = getText("stockOrderAgent.staus0");
        } else if (status == 1L) {
            strStatus = getText("stockOrderAgent.staus1");
        } else if (status == 2L) {
            strStatus = getText("stockOrderAgent.staus2");
        }
        if (status == 3L) {
            strStatus = getText("mn.stock.agency.retrieve.import.optionset.destroy");
        }
        return strStatus;
    }

    /**
     * ham xu ly upload file
     *
     * @param event
     * @author ThanhNT77
     */
    @Secured("@")
    public void doFileUploadAction(FileUploadEvent event) {
        try {
            uploadedFile = event.getFile();
            BaseMessage message = validateFileUploadWhiteList(uploadedFile, ALOW_EXTENSION_PDF_TYPE_LIST, MAX_SIZE_5M);
            if (!message.isSuccess()) {
                clearUpload();
                LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                ex.setDescription(message.getDescription());
                throw ex;
            }
            byteContent = uploadedFile.getContents();
            attachFileName = event.getFile().getFileName();
            if (DataUtil.isNullOrEmpty(attachFileName)
                    || DataUtil.isNullObject(byteContent)
                    || attachFileName.length() > 200
                    || byteContent.length == 0
                    || byteContent.length > Const.FILE_MAX) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "limit.stock.valid.file");
            }
        } catch (LogicException ex) {
            reportError("", ex);
            topPage();
            clearUpload();
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
            clearUpload();
        }
    }

    @Secured("@")
    public StreamedContent downloadFileAttach(VStockTransDTO vStockTransDTO) {
        try {
            byte[] content = stockTransDocumentService.getAttachFileContent(vStockTransDTO.getStockTransID());
            InputStream is = new ByteArrayInputStream(content);
            return new DefaultStreamedContent(is, "application/xlsx/doc/docx/jpg/gpeg/pdf/jpe?g/png/gif/image/txt",
                    getTextParam("stock.export.material.fileName.download", vStockTransDTO.getActionCode())+".pdf");
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
        }
        return null;
    }

    private void clearUpload() {
        uploadedFile = null;
        attachFileName = "";
        byteContent = null;
    }

    public boolean isShowListProduct() {
        return forSearchSelect != null && forSearchSelect.getFromOwnerID() != null;
    }

    //getter and setter

    public boolean isShowSearch() {
        return showSearch;
    }

    public void setShowSearch(boolean showSearch) {
        this.showSearch = showSearch;
    }

    public boolean isDisableChangeMaterial() {
        return disableChangeMaterial;
    }

    public void setDisableChangeMaterial(boolean disableChangeMaterial) {
        this.disableChangeMaterial = disableChangeMaterial;
    }

    public boolean isViewMaterial() {
        return viewMaterial;
    }

    public void setViewMaterial(boolean viewMaterial) {
        this.viewMaterial = viewMaterial;
    }

    public VStockTransDTO getForSearch() {
        return forSearch;
    }

    public void setForSearch(VStockTransDTO forSearch) {
        this.forSearch = forSearch;
    }

    public VStockTransDTO getForSearchSelect() {
        return forSearchSelect;
    }

    public void setForSearchSelect(VStockTransDTO forSearchSelect) {
        this.forSearchSelect = forSearchSelect;
    }

    public VStockTransDTO getForSearchSelectView() {
        return forSearchSelectView;
    }

    public void setForSearchSelectView(VStockTransDTO forSearchSelectView) {
        this.forSearchSelectView = forSearchSelectView;
    }

    public VShopStaffDTO getShopDTOSearch() {
        return shopDTOSearch;
    }

    public void setShopDTOSearch(VShopStaffDTO shopDTOSearch) {
        this.shopDTOSearch = shopDTOSearch;
    }



    public List<StockTransFullDTO> getLsStockTransFull() {
        return lsStockTransFull;
    }

    public void setLsStockTransFull(List<StockTransFullDTO> lsStockTransFull) {
        this.lsStockTransFull = lsStockTransFull;
    }

    public List<StockTransSerialDTO> getLsSerial() {
        return lsSerial;
    }

    public void setLsSerial(List<StockTransSerialDTO> lsSerial) {
        this.lsSerial = lsSerial;
    }

    public List<VShopStaffDTO> getLsShop() {
        return lsShop;
    }

    public void setLsShop(List<VShopStaffDTO> lsShop) {
        this.lsShop = lsShop;
    }

    public List<VStockTransDTO> getvStockTransDTOList() {
        return vStockTransDTOList;
    }

    public void setvStockTransDTOList(List<VStockTransDTO> vStockTransDTOList) {
        this.vStockTransDTOList = vStockTransDTOList;
    }

    public ListProductNameable getListProductTag() {
        return listProductTag;
    }

    public void setListProductTag(ListProductNameable listProductTag) {
        this.listProductTag = listProductTag;
    }

    public int getLimitAutoComplete() {
        return limitAutoComplete;
    }

    public void setLimitAutoComplete(int limitAutoComplete) {
        this.limitAutoComplete = limitAutoComplete;
    }

    public VShopStaffDTO getShopDTOCreate() {
        return shopDTOCreate;
    }

    public void setShopDTOCreate(VShopStaffDTO shopDTOCreate) {
        this.shopDTOCreate = shopDTOCreate;
    }

    public String getAttachFileName() {
        return attachFileName;
    }

    public void setAttachFileName(String attachFileName) {
        this.attachFileName = attachFileName;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }
}

