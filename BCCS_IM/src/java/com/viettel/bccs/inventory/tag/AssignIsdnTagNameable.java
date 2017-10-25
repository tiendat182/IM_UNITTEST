package com.viettel.bccs.inventory.tag;

import com.viettel.bccs.inventory.dto.OptionSetValueDTO;
import com.viettel.bccs.inventory.dto.ProductOfferingTotalDTO;
import com.viettel.bccs.inventory.dto.SearchNumberRangeDTO;
import com.viettel.bccs.inventory.service.OptionSetValueService;
import com.viettel.fw.Exception.LogicException;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.security.access.annotation.Secured;

import java.util.List;

/**
 * Created by hoangnt14 on 12/26/2015.
 */
public interface AssignIsdnTagNameable {
    /**
     * author HoangNT
     *
     * @param objectController
     */
    @Secured("@")
    public void init(Object objectController);

    public void changeTypeService();

    public void fileUploadAction(FileUploadEvent event)  throws LogicException;

    public int processInputExcel();

    public void onChangeStartRange();

    public void onChangeEndRange();

    public int checkFileImport();

    @Secured("@")
    public List<ProductOfferingTotalDTO> doChangeOffering(String input);

    public void downloadFileError();

    public List<ProductOfferingTotalDTO> doChangeOfferingNew(String input);

    public void downloadFileTemplate();

    public void clearProductNew();

    public void clearProductOld();

    //geter and setter
    public String getTypeService();

    public void setTypeService(String typeService);

    public String getSelectFromFile();

    public String getEnteredDigitalRanges();

    public boolean isRenderedFromFile();

    public void setRenderedFromFile(boolean renderedFromFile);

    public ShopInfoNameable getShopInfoTag();

    public void setShopInfoTag(ShopInfoNameable shopInfoTag);

    public SearchNumberRangeDTO getSearchStockNumberDTO();

    public void setSearchStockNumberDTO(SearchNumberRangeDTO searchStockNumberDTO);

    public List<OptionSetValueDTO> getOptionSetValueDTOs();

    public void setOptionSetValueDTOs(List<OptionSetValueDTO> optionSetValueDTOs);

    public UploadedFile getUploadedFile();

    public void setUploadedFile(UploadedFile uploadedFile);

    public String getFileName();

    public void setFileName(String fileName);

    public Object getObjectController();

    public void setObjectController(Object objectController);

    public byte[] getByteContent();

    public void setByteContent(byte[] byteContent);

    public OptionSetValueService getOptionSetValueService();

    public void setOptionSetValueService(OptionSetValueService optionSetValueService);


    public ProductOfferingTotalDTO getProductOfferingTotalDTO();

    public void setProductOfferingTotalDTO(ProductOfferingTotalDTO productOfferingTotalDTO);

    public List<ProductOfferingTotalDTO> getLsProductOfferingTotalDTO();

    public void setLsProductOfferingTotalDTO(List<ProductOfferingTotalDTO> lsProductOfferingTotalDTO);

    public int getLimitAutoComplete();

    public void setLimitAutoComplete(int limitAutoComplete);


    public ProductOfferingTotalDTO getProductOfferingTotalNewDTO();

    public void setProductOfferingTotalNewDTO(ProductOfferingTotalDTO productOfferingTotalNewDTO);

    public List<ProductOfferingTotalDTO> getLsProductOfferingTotalNewDTO();

    public void setLsProductOfferingTotalNewDTO(List<ProductOfferingTotalDTO> lsProductOfferingTotalNewDTO);

    public boolean isHasFileError();

    public void setHasFileError(boolean hasFileError);

    public String getServiceType();

    public void setServiceType(String serviceType);

    public void changeServiceType();

    @Secured("@")
    public void setServiceNameById();
}
