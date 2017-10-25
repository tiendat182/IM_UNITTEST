package com.viettel.bccs.inventory.tag;


import com.viettel.bccs.inventory.common.ExcellUtil;
import com.viettel.bccs.inventory.dto.*;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.springframework.security.access.annotation.Secured;

import java.util.List;

/**
 * interface ta thong tin lenh xuat
 *
 * @author ThanhNT77
 */
public interface ListProductNameable {

    /**
     * ham init tag , mode = 1 : tao moi co serial, mode = 2 tao moi khong co serial,
     *
     * @param objectController
     * @param configTagDto
     * @author ThanhNT
     */
    public void init(Object objectController, ConfigListProductTagDTO configTagDto);

    /**
     * ham load tag( chi dung cho TH edit, can load du lieu mac dinh)
     *
     * @param objectController
     * @param stockTransActionId
     * @param configTagDto
     * @author ThanhNT77
     */
    public void load(Object objectController, Long stockTransActionId, ConfigListProductTagDTO configTagDto);

    /**
     * check hien thi link view serial
     *
     * @return true thi h thi
     * @author ThanhNT77
     */
    public Boolean getIsShowSerialByMode();

    /**
     * ham tra ve danh sach list StockTransDetailDTO
     *
     * @return
     * @author ThanhNT
     */
    public List<StockTransDetailDTO> getDataListSerialDetailDto();

    /**
     * them moi doi tuong add item
     *
     * @author ThanhNT77
     */
    @Secured("@")
    public void doAddItem();

    /**
     * ham xu ly xoa bang ghi
     *
     * @param index
     * @author ThanhNT
     */
    @Secured("@")
    public void doRemoveItem(int index);

    /**
     * action thay doi loai hang hoa
     *
     * @param index
     * @author ThanhNT
     */
    @Secured("@")
    public void doChangeOfferType(int index);

    @Secured("@")
    public void addListSerialAuto();

    /**
     * ham select chon hang hoa
     *
     * @author ThanhNT77
     */
    @Secured("@")
    public void doSelectProductOffering();

    @Secured("@")
    public void doSelectProductOfferingNew();

    @Secured("@")
    public void doChangeFinanceQuantity();

    /**
     * tong gia
     *
     * @author ThanhNT77
     */
    @Secured("@")
    public void doChangePrice();

    /**
     * Ham xu ly tim kiem khi chan ten mat hang
     *
     * @param input
     * @return
     * @author ThanhNT
     */
    @Secured("@")
    public List<ProductOfferingTotalDTO> doChangeOffering(String input);

    @Secured("@")
    public List<ProductOfferingTotalDTO> doChangeOfferingNew(String input);

    /**
     * ham lay ve danh dach chi tien giao dich kho, ham nay ko chia ra ngoai giao dien,
     * nhung van de la public, dong thoi ben trong xu ly validate empty so luong, ten mat hang
     *
     * @return
     * @author ThanhNT77
     */
    public List<StockTransDetailDTO> getListTransDetailDTOs();

    /**
     * ham xu ly disable button xoa
     * @author thanhnt77
     */
    public void setDisableRemove();

    /**
     * ham tai file mau nhap dai serial
     *
     * @return
     * @author ThanhNT77
     */
    @Secured("@")
    public StreamedContent getAddTemplateFile();

    /**
     * ham tai file mau nhap dai serial
     *
     * @return
     * @author ThanhNT77
     */
    @Secured("@")
    public StreamedContent getAddTemplateFileByProd();

    /**
     * tai file excel
     *
     * @return
     * @author ThanhNT77
     */
    @Secured("@")
    public StreamedContent exportSerialsDlg();

    /**
     * tai file excel
     *
     * @return
     * @author ThanhNT77
     */
    @Secured("@")
    public StreamedContent exportSerialsDlgView();

    /**
     * ham xuat excel serial khi xem kho
     * @author thanhnt77
     * @return
     */
    @Secured("@")
    public StreamedContent exportSerialsDlgViewStock();

    /**
     * ham xu ly enable/disable button excel
     * @author ThanhNT77
     * @return
     */
    public boolean getDisableSerialViewStock();

    /**
     * ham open view serial tren dialog
     *
     * @author ThanhNT77
     */
    @Secured("@")
    public void doOpenDlgSerial(int index);

    /**
     * ham view chi tiet
     *
     * @param stockTransActionId
     * @author ThanhNT77
     */
    @Secured("@")
    public void doViewStockTransDetail(Long stockTransActionId);

    /**
     * ham xu ly upload file theo hang hoa chi tiet
     *
     * @param event
     * @author ThanhNT77
     */
    @Secured("@")
    public void handleFileUpload(FileUploadEvent event);

    /**
     * ham xu ly upload file theo danh sach mat hang
     *
     * @param event
     * @author ThanhNT77
     */
    @Secured("@")
    public void handleFileUploadByProd(FileUploadEvent event);

    /**
     * ham reset back tro ve man tim kiem
     *
     * @author ThanhNT77
     */
    @Secured("@")
    public void doChangeTypePrint();

    /**
     * ham open view serial tren dialog
     *
     * @author ThanhNT77
     */
    @Secured("@")
    public void doSelectSerial();

    /**
     * ham chon serila theo mat hang
     *
     * @author ThanhNT77
     */
    @Secured("@")
    public void doSelectSerialByProd();

    /**
     * hien thi title serial cua hang hoa
     *
     * @return
     * @author ThanhNT
     */
    public String getTitleSerialProduct();

    /**
     * ham check handset  neu la hanset thi tra ve true
     *
     * @return
     * @author ThanhNT77
     */
    @Secured("@")
    public boolean getHandset();

    /**
     * ham open view tat ca serial tren dialog
     *
     * @author ThanhNT77
     */
    @Secured("@")
    public void doShowHideListSerial();

    /**
     * ham check hien thi button xac nhan
     *
     * @return true neu co hien thi button len
     */
    @Secured("@")
    public boolean getShowUploadProdByFile();

    /**
     * ham open view serial tren dialog
     *
     * @author ThanhNT77
     */
    @Secured("@")
    public void doConfirmSerial();

    /**
     * ham open view serial tren dialog
     *
     * @author ThanhNT77
     */
    @Secured("@")
    public void doRemoveSerial(int index);

    /**
     * xu ly lua chon row tren dialog list serial
     *
     * @author ThanhNT77
     */
    @Secured("@")
    public void onRowSelect(SelectEvent event);

    @Secured("@")
    public StreamedContent exportImportErrorFile();

    @Secured("@")
    public StreamedContent exportImportListErrorFile();

    @Secured("@")
    public String getTotalSerialSelect();

    /**
     * ham lay du lieu cau hinh phan ra thiet bi truoc khi show dialog cau hinh trang thai
     * @param index
     */
    @Secured("@")
    public void preToShowDeviceConfig(int index);

    /**
     * ham lay danh sach cau hinh phan ra thiet bi de show len dialog
     */
    @Secured("@")
    public List<StockDeviceTransferDTO> getCurrentDeviceConfig();

    /**
     * ham lay mat hang dang cau de check trang thai, phuc vu cho viec disable trang thai tren dialog
     * @return
     */
    @Secured("@")
    public ListProductOfferDTO getCurrentProduct();

    //getter and setter
    public Object getObjectController();

    public void setObjectController(Object objectController);

    public int getLimitAutoComplete();

    public void setLimitAutoComplete(int limitAutoComplete);

    public List<ListProductOfferDTO> getLsListProductOfferDTO();

    public void setLsListProductOfferDTO(List<ListProductOfferDTO> lsListProductOfferDTO);

    public List<OptionSetValueDTO> getLsProductStatus();

    public void setLsProductStatus(List<OptionSetValueDTO> lsProductStatus);

    public List<ProductOfferTypeDTO> getLsProductOfferTypeDTOTmp();

    public void setLsProductOfferTypeDTOTmp(List<ProductOfferTypeDTO> lsProductOfferTypeDTOTmp);

    public List<ProductOfferingTotalDTO> getLsProductOfferingTotalDTOTmp();

    public void setLsProductOfferingTotalDTOTmp(List<ProductOfferingTotalDTO> lsProductOfferingTotalDTOTmp);

    public List<StockTransFullDTO> getLsStockTransFull();

    public void setLsStockTransFull(List<StockTransFullDTO> lsStockTransFull);

    public List<StockTransSerialDTO> getListSerialView();

    public void setListSerialView(List<StockTransSerialDTO> listSerialView);

    public StockTransSerialDTO getSerialViewSelected();

    public void setSerialViewSelected(StockTransSerialDTO serialViewSelected);

    public List<StockTransSerialDTO> getListSerialRangeSelect();

    public void setListSerialRangeSelect(List<StockTransSerialDTO> listSerialRangeSelect);

    public ExcellUtil getProcessingFile();

    public void setProcessingFile(ExcellUtil processingFile);

    public int getIndex();

    public void setIndex(int index);

    public Long getOwnerId();

    public void setOwnerId(Long ownerId);

    public String getOwnerType();

    public void setOwnerType(String ownerType);

    public boolean getShowSerial();

    public void setShowSerial(boolean isShowSerial);

    public boolean isAddNewProduct();

    public void setAddNewProduct(boolean isAddNewProduct);

    public String getAttachFileName();

    public void setAttachFileName(String attachFileName);

    public byte[] getByteContent();

    public void setByteContent(byte[] byteContent);

    public UploadedFile getFileUpload();

    public void setFileUpload(UploadedFile fileUpload);

    public String getTypePrint();

    public void setTypePrint(String typePrint);

    public boolean isViewBtnExportStock();

    public void setViewBtnExportStock(boolean isViewBtnExportStock);

    public StockTransFullDTO getStockTransDetail();

    public void setStockTransDetail(StockTransFullDTO stockTransDetail);

    public ShopInfoNameable getShopInfoTagExportDlg();

    public void setShopInfoTagExportDlg(ShopInfoNameable shopInfoTagExportDlg);

    public Long getTotalPriceAmount();

    public void setTotalPriceAmount(Long totalPriceAmount);

    public ConfigListProductTagDTO getConfigTagDto();

    public void setConfigTagDto(ConfigListProductTagDTO configTagDto);

    //tuydv1
    public List<OptionSetValueDTO> getLsProductStatusAfter();

    public void setLsProductStatusAfter(List<OptionSetValueDTO> lsProductStatusAfter);

    public boolean getShowForRetrieveStock();

    public void setShowForRetrieveStock(boolean isShowForRetrieveStock);

    public boolean isErrorImport();

    public ExcellUtil getProcessingFileProd();

    public void setProcessingFileProd(ExcellUtil processingFileProd);

    public UploadedFile getFileUploadByProd();

    public void setFileUploadByProd(UploadedFile fileUploadByProd);

    public byte[] getByteContentByProd();

    public void setByteContentByProd(byte[] byteContentByProd);

    public String getAttachFileNameByProd();

    public void setAttachFileNameByProd(String attachFileNameByProd);

    public void setErrorImport(boolean errorImport);

    public boolean isShowSerial();

    public boolean isShowForRetrieveStock();

    public List<StockTransSerialDTO> getLstErrorSerial();

    public void setLstErrorSerial(List<StockTransSerialDTO> lstErrorSerial);

    public boolean isErrorImportProd();

    public void setErrorImportProd(boolean errorImportProd);

    public List<StockTransFullDTO> getLstErrorSerialProd();

    public void setLstErrorSerialProd(List<StockTransFullDTO> lstErrorSerialProd);
}
