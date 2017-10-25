package com.viettel.bccs.inventory.controller.stock;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.ExcellUtil;
import com.viettel.bccs.inventory.common.model.FileExportBean;
import com.viettel.bccs.inventory.common.util.FileUtil;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.message.BasePartnerMessage;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.bccs.inventory.tag.StaffInfoNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.web.common.security.CustomAuthenticationProvider;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.omnifaces.util.Faces;
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
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * controller chuc nang xuat hang can kho
 * 17/08/2016
 * Created by thanhnt77
 */
@Component
@Scope("view")
@ManagedBean
public class PartnerImportStockBalanceController extends TransCommonController {

    private int limitAutoComplete;
    private byte[] byteContent;
    private boolean checkErp;
    private boolean renderedImport = false;
    private String productOfferTypeId;
    private String typeExport = "2";
    private String attachFileName = "";
    private Long totalSerial;
    private String serialList = "";
    private String profileName = "";

    private StockTransFullDTO stockTransFullDTO;
    private ProductOfferingTotalDTO productOfferingTotalDTO;
    private StaffDTO staffDTO;
    private UploadedFile uploadedFile;
    private VShopStaffDTO vShopStaffDTOShop;
    private RequiredRoleMap requiredRoleMap;

    private List<PartnerDTO> lstPartnerDTOs;
    private List<ProductOfferTypeDTO> lstProductOfferTypeDTO;
    private List<ProductOfferingTotalDTO> lstProductOfferingTotalDTO;
    private List<StockTransSerialDTO> listStockTransSerialDTOs;
    private List<String> lstErrorSerial;

    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private ShopInfoNameable shopInfoTag;
    @Autowired
    private StaffService staffService;
    @Autowired
    private PartnerService partnerService;
    @Autowired
    private ProductOfferTypeService productOfferTypeService;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private ExecuteStockTransService executeStockTransService;

    @Secured("@")
    @PostConstruct
    public void init() {
        try {
            requiredRoleMap = CustomAuthenticationProvider.createRequiredRoleMap(Const.PERMISION.ROLE_STOCK_NUM_SHOP);
            stockTransFullDTO = new StockTransFullDTO();
            staffDTO = BccsLoginSuccessHandler.getStaffDTO();

            shopInfoTag.initShopNotChanelTypeIdCurrentAndChild(this, Const.MODE_SHOP.PARTNER, staffDTO.getShopId().toString(), Const.OWNER_TYPE.SHOP, Const.CHANNEL_TYPE.CHANNEL_TYPE_AGENT);
            shopInfoTag.loadShop(staffDTO.getShopId().toString(), false);

            vShopStaffDTOShop = shopService.getShopByOwnerId(DataUtil.safeToString(staffDTO.getShopId()), Const.OWNER_TYPE.SHOP);

            stockTransFullDTO.setActionCode(staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PN, Const.STOCK_TRANS_TYPE.IMPORT, staffDTO));
            limitAutoComplete = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_AUTOCOMPLETE));
            lstPartnerDTOs = partnerService.findPartner(new PartnerDTO());
            Date currentDate = getSysdateFromDB();
            stockTransFullDTO.setCreateDatetime(currentDate);
            lstProductOfferTypeDTO = productOfferTypeService.getListProduct();
            doResetTypeExport();
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
    }

    /**
     * ham xu ly reset vung thon tin kieu xuat hang, serial imei
     * @author thanhnt77
     */
    @Secured("@")
    public void doResetTypeExport() {
        listStockTransSerialDTOs = Lists.newArrayList();
        resetUploadFile();
        resetTypeRangeNumber();
        resetImportNumber();
    }

    /**
     * ham reset thong tin file
     * @author thanhnt77
     */
    private void resetUploadFile() {
        attachFileName = null;
        uploadedFile = null;
        byteContent = null;
        renderedImport = false;
        lstErrorSerial = Lists.newArrayList();
        serialList = "";
        profileName = "";
    }


    /**
     * ham reset thong tin nhap theo dai so
     * @author thanhnt77
     */
    private void resetTypeRangeNumber() {
        stockTransFullDTO.setFromSerial(null);
        stockTransFullDTO.setToSerial(null);
    }

    /**
     * ham reset thong tin nhap so luong
     * @author thanhnt77
     */
    private void resetImportNumber() {
        stockTransFullDTO.setQuantity(null);
        stockTransFullDTO.setStrQuantity(null);
    }

    @Secured("@")
    public void handlFileUpload(FileUploadEvent event) {
        try {
            resetUploadFile();
            if (event == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "common.error.upload.file");
            }
            uploadedFile = event.getFile();

            BaseMessage message = validateFileUploadWhiteList(uploadedFile, ALOW_EXTENSION_TEXT_TYPE_LIST, MAX_SIZE_2M);
            if (!message.isSuccess()) {
                LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                ex.setDescription(message.getDescription());
                throw ex;
            }

            byteContent = uploadedFile.getContents();
            attachFileName = new String(uploadedFile.getFileName().getBytes(), "UTF-8");

            serialList = "";

            List<String> readerData = Lists.newArrayList();
            BufferedReader reader = new BufferedReader(new InputStreamReader(uploadedFile.getInputstream(), "UTF-8"));
            String lines;
            while ((lines = reader.readLine()) != null) {
                if (DataUtil.isNullOrEmpty(lines)) {
                    continue;
                }
                readerData.add(lines);
            }
            if (!DataUtil.isNullOrEmpty(readerData)) {
                serialList = Joiner.on(" ").join(readerData);
            }
        } catch (LogicException ex) {
            reportError("", ex);
            topPage();
        } catch (Exception e) {
            topReportError("", "common.error.happened", e);
        }
    }

    @Secured("@")
    public void doSelectProductOffering() {
        try {
            stockTransFullDTO.setProdOfferCode(this.productOfferingTotalDTO.getCode());
            stockTransFullDTO.setProdOfferId(this.productOfferingTotalDTO.getProductOfferingId());
            stockTransFullDTO.setProdOfferName(this.productOfferingTotalDTO.getName());
            stockTransFullDTO.setProductOfferTypeId(this.productOfferingTotalDTO.getProductOfferTypeId());
            /*this.profileName = "";
            if (!DataUtil.safeEqual(productOfferingTotalDTO.getProductOfferTypeId(), Const.PRODUCT_OFFER_TYPE.PRODUCT_NO_SERIAL)) {
                ProfileDTO profileDTO = productOfferingService.getProfileByProductId(productOfferingTotalDTO.getProductOfferingId());
                if (profileDTO != null && !DataUtil.isNullOrEmpty(profileDTO.getLstColumnName())) {
                    this.profileName = Joiner.on(",").join(profileDTO.getLstColumnName());
                }
            }*/
        }  catch (Exception e) {
            topReportError("", "common.error.happened", e);
        }
    }

    /**
     * ham xu ly nhap hang
     * @author thanhnt77
     */
    @Secured("@")
    public void doImportParner() {
        try {
            lstErrorSerial = Lists.newArrayList();
            //xu ly validate serial case nhap theo dai
            if (Const.EXPORT_PARTNER.EXP_BY_SERIAL_RANGE.equals(typeExport)) {
                validateSerialRange(this.stockTransFullDTO);
                //xu ly validate serial case nhap theo file
            } else if (Const.EXPORT_PARTNER.EXP_BY_FILE.equals(typeExport)) {
                if (uploadedFile == null) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.limit.attachFile.msg.require");
                }
                //validate neu ko lay dc ve serial nao trong file thi bao loi
                if (DataUtil.isNullOrEmpty(serialList)) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mm.divide.upload.valid.listSerial");
                }
            }
            stockTransFullDTO.setSyncERP(null);
            if (vShopStaffDTOShop == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.liquidate.receive.stock.required");
            }

            stockTransFullDTO.setToOwnerId(DataUtil.safeToLong(vShopStaffDTOShop.getOwnerId()));
            stockTransFullDTO.setToOwnerType(Const.OWNER_TYPE.SHOP);

            BasePartnerMessage message = executeStockTransService.createExportToPartner(Const.STOCK_TRANS.PARTNER_BALANCE,
                    Const.STOCK_TRANS_TYPE.IMPORT, stockTransFullDTO, getStaffDTO(), typeExport, serialList, requiredRoleMap);
            lstErrorSerial = message.getLsSerialErorr();
            if (!DataUtil.isNullOrEmpty(message.getErrorCode())) {
                LogicException logicException = new LogicException(message.getErrorCode(), message.getKeyMsg(), message.getParamsMsg());
                throw logicException;
            }
            stockTransFullDTO.setActionCode(staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PN, Const.STOCK_TRANS_TYPE.IMPORT, staffDTO));
            reportSuccess("frmExportStockPartner:msgSearch", message.getKeyMsg(), message.getParamsMsg());
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmExportStockPartner:msgSearch", ex);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmExportStockPartner:msgSearch", "common.error.happened", ex);
        }
        topPage();
    }

    /**
     * ham xu ly reset mat hang
     * @author thanhnt77
     */
    @Secured("@")
    public void doReset() {
        init();
    }

    /**
     * ham xu ly validate dinh dang serial voi case nhap serial theo dai
     * @author thanhnt77
     * @param stockTransFullDTO
     * @throws com.viettel.fw.Exception.LogicException
     */
    private void validateSerialRange(StockTransFullDTO stockTransFullDTO) throws LogicException {
        boolean isHandset = Const.STOCK_TYPE.STOCK_HANDSET.equals(stockTransFullDTO.getProductOfferTypeId());
        String fromSerial = DataUtil.safeToString(stockTransFullDTO.getFromSerial()).trim();
        String toSerial = DataUtil.safeToString(stockTransFullDTO.getToSerial()).trim();
        // validate empty
        if (DataUtil.isNullOrEmpty(fromSerial)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.partner.serial.valid.start.require");
        }
        if (DataUtil.isNullOrEmpty(toSerial)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.partner.serial.valid.end.require");
        }
        //validate dinh dang
        if (!DataUtil.validateStringByPattern(fromSerial, Const.REGEX.SERIAL_REGEX) || !DataUtil.validateStringByPattern(toSerial, Const.REGEX.SERIAL_REGEX)
                || fromSerial.length() > 19 || toSerial.length() > 19) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mm.divide.upload.valid.serial.length");
        }
        if (fromSerial.length() != toSerial.length()) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.partner.length.fromSerial.toSerial.valid");
        }

        if (isHandset) {
            if (!fromSerial.equals(toSerial)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.partner.serial.valid.hanset.valid.file");
            }
            stockTransFullDTO.setQuantity(1L);
        } else {
            if (!DataUtil.validateStringByPattern(fromSerial, Const.REGEX.NUMBER_REGEX) || !DataUtil.validateStringByPattern(toSerial, Const.REGEX.NUMBER_REGEX)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.trans.validate.fromSerial.toSerial.format", stockTransFullDTO.getProdOfferName());
            }
            BigInteger fromSerialNum = new BigInteger(fromSerial);
            BigInteger toSerialNum = new BigInteger(toSerial);
            BigInteger result = toSerialNum.subtract(fromSerialNum);
            //neu serial ket thuc nho hon serial bat dau or serial ket thuc lon hon 500000 thi bao loi
            if (result.compareTo(BigInteger.ZERO) < 0 || result.compareTo(new BigInteger("500000")) > 0) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.partner.serial.valid.range");
            }
            stockTransFullDTO.setQuantity(result.longValue() + 1L);
        }
    }

    /**
     * check shop vtt
     *
     * @return
     * @author ThanhNT
     */
    public boolean getShopVTT() {
        return staffDTO != null && Const.SHOP.SHOP_VTT_ID.equals(staffDTO.getShopId());
    }

    /**
     * validate tam
     * @author ThanhNT77
     */
    @Secured("@")
    public void doValidate() {
    }

    /**
     * goi khi thay doi buoc nhay hoac from serial khi nhap serial theo dai
     */

    public StreamedContent exportErrorFile() {
        try {
            if (!DataUtil.isNullOrEmpty(lstErrorSerial)) {
                return FileUtil.exportToStreamedText(lstErrorSerial, getStaffDTO().getStaffCode().toLowerCase() + "_" + Const.EXPORT_FILE_NAME.IMPORT_PARTNER_SERIAL_ERROR);
            }
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
        return null;
    }

    /**
     * goi khi thay doi buoc nhay hoac from serial khi nhap serial theo dai
     */

    public StreamedContent exportTemplateFile() {
        try {
            return FileUtil.exportToStreamedText(Lists.newArrayList("98100000011","98100000008"), getStaffDTO().getStaffCode().toLowerCase() + "_" + Const.EXPORT_FILE_NAME.IMPORT_PARTNER_SERIAL_ERROR);
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
        return null;
    }

    /**
     * Load danh sach mat hang theo loai hang hoa
     */
    @Secured("@")
    public void onChangeOfferType() {
        try {
            lstProductOfferingTotalDTO = Lists.newArrayList();
            productOfferingTotalDTO = null;
            //neu chon mat hang no-serial thi rest kieu nhap hang ve so luong
            if (getIsNoSerial()) {
                typeExport = "1";
                doResetTypeExport();
            } else if ("1".equals(typeExport)) {
                typeExport = "2";
            }
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    /**
     * nau chon loai mat hang handset thi tra ve true
     * @author thanhnt
     * @return
     */
    public boolean getIsHandset() {
        return Const.STOCK_TYPE.STOCK_HANDSET.equals(stockTransFullDTO.getProductOfferTypeId()) || Const.STOCK_TYPE.STOCK_ACCESSORIES.equals(stockTransFullDTO.getProductOfferTypeId());
    }

    /**
     * nau chon loai mat hang handset thi tra ve true
     * @author thanhnt
     * @return
     */
    public boolean getIsNoSerial() {
        return Const.STOCK_TYPE.STOCK_NO_SERIAL.equals(stockTransFullDTO.getProductOfferTypeId());
    }

    /**
     * Tim kiem mat hang khi nhap tay
     */
    @Secured("@")
    public List<ProductOfferingTotalDTO> searchListProductOffering(String input) {
        try {
            lstProductOfferingTotalDTO = Lists.newArrayList();
            if (!DataUtil.isNullOrZero(stockTransFullDTO.getProductOfferTypeId())) {
                CharSequence inputData = DataUtil.isNullOrEmpty(input) ? "" : input.trim().toUpperCase();
                lstProductOfferingTotalDTO = productOfferingService.getListProductOfferingByProductType(inputData.toString(), stockTransFullDTO.getProductOfferTypeId());
            }
        } catch (LogicException e) {
            logger.error(e.getMessage(), e);
            topReportError("", e.getErrorCode(), e.getKeyMsg());
        } catch (Exception e) {
            topReportError("", "common.error.happened", e);
        }
        return lstProductOfferingTotalDTO;
    }

    /**
     * reset mat hang da chon
     */
    @Secured("@")
    public void resetLstProductOfferingTotal() {
        productOfferingTotalDTO = null;
        stockTransFullDTO.setProdOfferId(null);
        stockTransFullDTO.setProdOfferName(null);
        profileName = null;
    }

    @Secured("@")
    public void receiveShop(VShopStaffDTO vShopStaffDTO) {
        this.vShopStaffDTOShop = vShopStaffDTO;
        //reset nhan vien
        updateElemetId("frmExportStockPartner:auCplStaff:pnProauCplStaff");
    }

    @Secured("@")
    public void clearShop() {
        this.vShopStaffDTOShop = null;
        //reset nhan vien
        updateElemetId("frmExportStockPartner:auCplStaff:pnProauCplStaff");
    }

    /**
     * ham xu ly blur
     * @author thanhnt77
     */
    @Secured("@")
    public void onBlurToSerial() {
        stockTransFullDTO.setStrQuantity(null);
        if (!DataUtil.isNullOrEmpty(stockTransFullDTO.getFromSerial()) && !DataUtil.isNullOrEmpty(stockTransFullDTO.getToSerial())) {
            if (getIsHandset()) {
                if (stockTransFullDTO.getFromSerial().equals(stockTransFullDTO.getToSerial())) {
                    stockTransFullDTO.setStrQuantity("1");
                    return;
                }
                reportError("", ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.partner.serial.valid.hanset.valid.file");
            } else {
                if (DataUtil.validateStringByPattern(stockTransFullDTO.getFromSerial(), Const.REGEX.NUMBER_REGEX) && DataUtil.validateStringByPattern(stockTransFullDTO.getToSerial(), Const.REGEX.NUMBER_REGEX)) {
                    BigInteger result = new BigInteger(stockTransFullDTO.getToSerial()).subtract(new BigInteger(stockTransFullDTO.getFromSerial())).add(BigInteger.ONE);
                    if (result.compareTo(BigInteger.ZERO) > 0 ) {
                        stockTransFullDTO.setStrQuantity(result.toString());
                        return;
                    }
                    reportError("", ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.validate.inspect.fromSerial.less.toSerial");
                }
            }
        }
    }

    public int getLimitAutoComplete() {
        return limitAutoComplete;
    }

    public void setLimitAutoComplete(int limitAutoComplete) {
        this.limitAutoComplete = limitAutoComplete;
    }

    public byte[] getByteContent() {
        return byteContent;
    }

    public void setByteContent(byte[] byteContent) {
        this.byteContent = byteContent;
    }

    public boolean isRenderedImport() {
        return renderedImport;
    }

    public void setRenderedImport(boolean renderedImport) {
        this.renderedImport = renderedImport;
    }

    public String getProductOfferTypeId() {
        return productOfferTypeId;
    }

    public void setProductOfferTypeId(String productOfferTypeId) {
        this.productOfferTypeId = productOfferTypeId;
    }

    public String getTypeExport() {
        return typeExport;
    }

    public void setTypeExport(String typeExport) {
        this.typeExport = typeExport;
    }

    public String getAttachFileName() {
        return attachFileName;
    }

    public void setAttachFileName(String attachFileName) {
        this.attachFileName = attachFileName;
    }

    public Long getTotalSerial() {
        return totalSerial;
    }

    public void setTotalSerial(Long totalSerial) {
        this.totalSerial = totalSerial;
    }

    public StockTransFullDTO getStockTransFullDTO() {
        return stockTransFullDTO;
    }

    public void setStockTransFullDTO(StockTransFullDTO stockTransFullDTO) {
        this.stockTransFullDTO = stockTransFullDTO;
    }

    public ProductOfferingTotalDTO getProductOfferingTotalDTO() {
        return productOfferingTotalDTO;
    }

    public void setProductOfferingTotalDTO(ProductOfferingTotalDTO productOfferingTotalDTO) {
        this.productOfferingTotalDTO = productOfferingTotalDTO;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public List<PartnerDTO> getLstPartnerDTOs() {
        return lstPartnerDTOs;
    }

    public void setLstPartnerDTOs(List<PartnerDTO> lstPartnerDTOs) {
        this.lstPartnerDTOs = lstPartnerDTOs;
    }

    public List<ProductOfferTypeDTO> getLstProductOfferTypeDTO() {
        return lstProductOfferTypeDTO;
    }

    public void setLstProductOfferTypeDTO(List<ProductOfferTypeDTO> lstProductOfferTypeDTO) {
        this.lstProductOfferTypeDTO = lstProductOfferTypeDTO;
    }

    public List<ProductOfferingTotalDTO> getLstProductOfferingTotalDTO() {
        return lstProductOfferingTotalDTO;
    }

    public void setLstProductOfferingTotalDTO(List<ProductOfferingTotalDTO> lstProductOfferingTotalDTO) {
        this.lstProductOfferingTotalDTO = lstProductOfferingTotalDTO;
    }

    public List<StockTransSerialDTO> getListStockTransSerialDTOs() {
        return listStockTransSerialDTOs;
    }

    public void setListStockTransSerialDTOs(List<StockTransSerialDTO> listStockTransSerialDTOs) {
        this.listStockTransSerialDTOs = listStockTransSerialDTOs;
    }

    public ShopInfoNameable getShopInfoTag() {
        return shopInfoTag;
    }

    public void setShopInfoTag(ShopInfoNameable shopInfoTag) {
        this.shopInfoTag = shopInfoTag;
    }

    public boolean isCheckErp() {
        return checkErp;
    }

    public void setCheckErp(boolean checkErp) {
        this.checkErp = checkErp;
    }

    public boolean isErrorImport() {
        return !DataUtil.isNullOrEmpty(lstErrorSerial);
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }
}
