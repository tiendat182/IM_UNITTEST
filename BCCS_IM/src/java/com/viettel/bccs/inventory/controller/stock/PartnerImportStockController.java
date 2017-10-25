package com.viettel.bccs.inventory.controller.stock;

import com.fss.util.NumberUtil;
import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.util.FileUtil;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.bccs.inventory.tag.SignOfficeTagNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.web.common.security.CustomAuthenticationProvider;
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
import javax.faces.context.FacesContext;
import java.io.*;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by hoangnt14 on 4/28/2016.
 */
@Component
@Scope("view")
@ManagedBean
public class PartnerImportStockController extends ExportFileStockTransController {

    private int limitAutoComplete;
    private boolean threeProvince;
    private RequiredRoleMap requiredRoleMap;
    private ImportPartnerRequestDTO importPartnerRequestDTO;
    private ImportPartnerDetailDTO importPartnerDetailDTO;
    private List<ShopDTO> lsRegionShop;
    private List<PartnerDTO> lstPartnerDTOs;
    private List<ProductOfferTypeDTO> lstProductOfferTypeDTO;
    private ProductOfferTypeDTO productOfferTypeDTO;
    private String productOfferTypeId;
    private List<ProductOfferingTotalDTO> lstProductOfferingTotalDTO;
    private ProductOfferingTotalDTO productOfferingTotalDTO;
    private List<OptionSetValueDTO> lstCurrencyType;
    private List<OptionSetValueDTO> lstSimType;
    private List<OptionSetValueDTO> lstImportType;
    private ProfileDTO profileDTO;
    private List<ProductOfferingDTO> lstProductSim;
    private boolean kit;
    private boolean sim;
    private boolean contractType;
    private boolean rateMoney;
    private UploadedFile uploadedFile;
    private boolean renderedError = false;
    private List<StockTransSerialDTO> listStockTransSerialDTOs;
    private List<String> listError;
    private StockTransSerialDTO stockTransSerialDTO;
    private Long rank;
    private Long totalSerial;
    private boolean renderedImport = true;
    private boolean showDetail;
    private boolean renderPrintExp = false;
    private Long createdTransId;
    private boolean checkErp;

    @Autowired
    private ImsiMadeService imsiMadeService;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private ShopInfoNameable shopInfoTag;
    @Autowired
    private ShopService shopService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private PartnerService partnerService;
    @Autowired
    private ProductOfferTypeService productOfferTypeService;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private SignOfficeTagNameable writeOfficeTag;//khai bao tag ky vOffice
    @Autowired
    private ImportStockFromPartnerService importStockFromPartnerService;
    @Autowired
    private ImportStockFromPartnerToBranchService importStockFromPartnerToBranchService;


    @Secured("@")
    @PostConstruct
    public void init() {
        try {
            threeProvince = false;
            writeOfficeTag.init(this, BccsLoginSuccessHandler.getStaffDTO().getShopId());
            shopInfoTag.initShopAndAllChild(this, BccsLoginSuccessHandler.getStaffDTO().getShopId().toString(), true, Const.OWNER_TYPE.SHOP);
            shopInfoTag.loadShop(BccsLoginSuccessHandler.getStaffDTO().getShopId().toString(), false);
            requiredRoleMap = CustomAuthenticationProvider.createRequiredRoleMap(Const.PERMISION.ROLE_XUAT_KHO_BA_MIEN, Const.PERMISION.ROLE_DONGBO_ERP);
            if (requiredRoleMap.hasRole(Const.PERMISION.ROLE_XUAT_KHO_BA_MIEN)) {
                threeProvince = true;
            }
            limitAutoComplete = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_AUTOCOMPLETE));
            lsRegionShop = DataUtil.defaultIfNull(shopService.getListShopByCodeOption(Const.OPTION_SET.REGION_SHOP), new ArrayList<ShopDTO>());
            lstPartnerDTOs = partnerService.findPartner(new PartnerDTO());

            lstProductOfferTypeDTO = productOfferTypeService.getListProduct();
            lstCurrencyType = optionSetValueService.getByOptionSetCode(Const.OPTION_SET.CURRENCY_TYPE);
            lstSimType = optionSetValueService.getByOptionSetCode(Const.OPTION_SET.EMPTY_SIM_TYPE);
            lstImportType = optionSetValueService.getByOptionSetCode(Const.OPTION_SET.IMPORT_TYPE_PARTNER);
            doReset();
            executeCommand("updateAll()");
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
    }

    @Secured("@")
    public void doReset() {
        try {
            importPartnerRequestDTO = new ImportPartnerRequestDTO();
            Date currentDate = getSysdateFromDB();
            importPartnerRequestDTO.setCreateDate(currentDate);
            importPartnerRequestDTO.setToOwnerId(BccsLoginSuccessHandler.getStaffDTO().getShopId());
            importPartnerRequestDTO.setToOwnerType(Const.OWNER_TYPE.SHOP_LONG);
            importPartnerRequestDTO.setContractStatus(1L);
            importPartnerDetailDTO = new ImportPartnerDetailDTO();
            kit = false;
            sim = false;
            contractType = true;
            importPartnerRequestDTO.setActionCode(staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PN, Const.STOCK_TRANS_TYPE.AGENT, BccsLoginSuccessHandler.getStaffDTO()));
            productOfferTypeDTO = new ProductOfferTypeDTO();
            importPartnerRequestDTO.setSignVoffice(true);
            lstProductSim = Lists.newArrayList();
            importPartnerRequestDTO.setImpType(DataUtil.safeToLong(lstImportType.get(0).getValue()));
            uploadedFile = null;
            renderedError = false;
            listStockTransSerialDTOs = Lists.newArrayList();
            stockTransSerialDTO = new StockTransSerialDTO();
            rank = Const.LongSimpleItem.importPartnerRankSerial1.getValue();
            renderedImport = true;
            renderPrintExp = false;
            showDetail = false;
            productOfferingTotalDTO = null;
            createdTransId = 0L;
            for (OptionSetValueDTO optionSetValueDTO : lstCurrencyType) {
                if (DataUtil.safeEqual(optionSetValueDTO.getValue(), "VND")) {
                    importPartnerRequestDTO.setCurrencyType("VND");
                    break;
                }
            }
            rateMoney = true;
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
    }

    public StreamedContent exportStockTransDetail(String mode) {
        StreamedContent streamedContent = null;
        try {
            return printStockTransDetail(genStockTransDTO(getVStockTransDTO(mode)));
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
        return streamedContent;
    }


    private VStockTransDTO getVStockTransDTO(String mode) throws LogicException, Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        vStockTransDTO.setOwnerID(getStaffDTO().getShopId());
        vStockTransDTO.setOwnerType(Const.OWNER_TYPE.SHOP_LONG);
        //
        vStockTransDTO.setStartDate(optionSetValueService.getSysdateFromDB(true));
        vStockTransDTO.setEndDate(optionSetValueService.getSysdateFromDB(true));
        if (Const.STOCK_TRANS.TRANS_CODE_PX.equals(mode)) {
            if (createdTransId == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "import.partner.valid.trans");
            }
            vStockTransDTO.setStockTransStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
            vStockTransDTO.setStockTransID(createdTransId);
        } else {
            vStockTransDTO.setStockTransStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
            vStockTransDTO.setActionCode(importPartnerRequestDTO.getActionCode());
            vStockTransDTO.setOwnerType(Const.OWNER_TYPE.PARTNER_LONG);
            vStockTransDTO.setOwnerID(importPartnerRequestDTO.getPartnerId());
        }
        List<VStockTransDTO> vStockTransDTOs = transService.mmSearchVStockTrans(vStockTransDTO);
        if (DataUtil.isNullOrEmpty(vStockTransDTOs)) {
            return null;
        }
        return vStockTransDTOs.get(0);
    }

    @Secured("@")
    public void handlFileUpload(FileUploadEvent event) {
        renderedError = false;
        if (event != null) {
            listStockTransSerialDTOs = Lists.newArrayList();
            uploadedFile = event.getFile();
            try {
                BaseMessage message = validateFileUploadWhiteList(uploadedFile, ALOW_EXTENSION_TEXT_TYPE_LIST, MAX_SIZE_2M);
                if (!message.isSuccess()) {
                    uploadedFile = null;
                    LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                    ex.setDescription(message.getDescription());
                    throw ex;
                }
                readSerialFromFile(uploadedFile.getInputstream());
            } catch (LogicException ex) {
                topReportError("", ex);
            } catch (Exception ex) {
                topReportError("", "common.error.happened", ex);
            }
        }
    }

    private void attachSerialRange(BigInteger fromSerial, BigInteger toSerial) throws LogicException, Exception {
        BigInteger bRange = new BigInteger(rank.toString());
        BigInteger range = bRange.add(new BigInteger("-1"));
        BigInteger maxTo = new BigInteger("1");
        while (maxTo.compareTo(toSerial) < 0) {
            BigInteger from = fromSerial;
            BigInteger to = from.add(range);
            if (to.compareTo(toSerial) > 0) {
                to = toSerial;
            }
            if (from.compareTo(to) > 0) {
                break;
            }
            Long div = to.add(from.negate()).longValue() + 1;
            fromSerial = fromSerial.add(bRange).add(bRange);
            //
            totalSerial += div;
            StockTransSerialDTO serialDTO = new StockTransSerialDTO();
            serialDTO.setFromSerial(from.toString());
            serialDTO.setToSerial(to.toString());
            maxTo = new BigInteger(to.toString());
            serialDTO.setQuantity(to.add(from.negate()).longValue() + 1L);
            listStockTransSerialDTOs.add(serialDTO);
        }
        importPartnerDetailDTO.setRealQuantity(totalSerial);
        importPartnerDetailDTO.setQuantity(totalSerial);
    }

    private void readSerialFromFile(InputStream stream) throws LogicException, Exception {
        if (DataUtil.isNullOrEmpty(importPartnerRequestDTO.getListProfile())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "import.partner.valid.profile");
        }
        List<String> readerData = Lists.newArrayList();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String lines;
        boolean error = false;
        while ((lines = reader.readLine()) != null) {
            if (DataUtil.isNullOrEmpty(lines)) {
                continue;
            }
            String[] splits = lines.split(Const.AppProperties4Sms.profileSparator.getValue().toString());
            if (splits.length != importPartnerRequestDTO.getListProfile().size()) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "import.partner.data.notmatch");
            }
            String regex = "^[0-9a-zA-Z]+";
            for (String serial : splits) {
                if (!serial.trim().matches(regex)) {
                    error = true;
                    lines += Const.AppProperties4Sms.profileSparator.getValue().toString() + " ";
                    lines += getMessage("import.partner.valid.char");
                }
            }
            readerData.add(lines);
        }
        if (readerData.isEmpty()) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "search.isdn.file.empty");
        }
        if (error) {
            uploadedFile = null;
            renderedError = true;
            listError = Lists.newArrayList(readerData);
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "import.partner.valid.fileContent");
        }
        importPartnerDetailDTO.setLstParam(readerData);
    }

    @Secured("@")
    public void doSelectProductOffering() {
        try {
            if (!DataUtil.isNullObject(productOfferingTotalDTO)) {
                importPartnerDetailDTO.setProdOfferId(productOfferingTotalDTO.getProductOfferingId());
                if (!DataUtil.safeEqual(productOfferingTotalDTO.getProductOfferTypeId(), Const.PRODUCT_OFFER_TYPE.PRODUCT_NO_SERIAL)) {
                    profileDTO = productOfferingService.getProfileByProductId(productOfferingTotalDTO.getProductOfferingId());
                    if (profileDTO != null && !DataUtil.isNullOrEmpty(profileDTO.getLstColumnName())) {
                        importPartnerRequestDTO.setListProfile(profileDTO.getLstColumnName());
                    } else {
                        productOfferingTotalDTO = null;
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "import.partner.valid.importByFile");
                    }
                }
            }
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    @Secured("@")
    public boolean getContractTypeDisable() {
        return importPartnerRequestDTO.getContractStatus() != null && DataUtil.safeEqual(importPartnerRequestDTO.getContractStatus(), 2L);
    }

    @Secured("@")
    public void doChangeRateMoney() {
        try {
            if (DataUtil.safeEqual(importPartnerRequestDTO.getCurrencyType(), "VND")) {
                rateMoney = true;
            } else {
                rateMoney = false;
            }
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    @Secured("@")
    public void doChangeSerial() {
        BigInteger maxRange = new BigInteger(Const.LongSimpleItem.maxRange.getValue().toString());
        totalSerial = 0L;
        renderedImport = true;
        listStockTransSerialDTOs = Lists.newArrayList();
        try {
            if (stockTransSerialDTO != null && !DataUtil.isNullOrEmpty(stockTransSerialDTO.getFromSerial())
                    && !DataUtil.isNullOrEmpty(stockTransSerialDTO.getToSerial()) && doValidateInputSerial()) {
                BigInteger fromSerial = new BigInteger(stockTransSerialDTO.getFromSerial());
                BigInteger toSerial = new BigInteger(stockTransSerialDTO.getToSerial());
                if (fromSerial.compareTo(toSerial) > 0) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "import.partner.serialContraints.from.to", maxRange);
                }
                BigInteger total;
                if (rank == 1) {
                    total = toSerial.add(fromSerial.negate()).add(new BigInteger("1"));
                    if (total.compareTo(maxRange) > 0) {
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "import.partner.serialContraints.from.to", maxRange);
                    }
                    totalSerial = total.longValue();
                    importPartnerDetailDTO.setRealQuantity(totalSerial);
                    importPartnerDetailDTO.setQuantity(totalSerial);
                    //add to list serial
                    StockTransSerialDTO serialDTO = new StockTransSerialDTO();
                    serialDTO.setFromSerial(stockTransSerialDTO.getFromSerial());
                    serialDTO.setToSerial(stockTransSerialDTO.getToSerial());
                    serialDTO.setQuantity(total.longValue());
                    listStockTransSerialDTOs.add(serialDTO);
                } else {
                    attachSerialRange(fromSerial, toSerial);
                }
                showDetail = true;
            }
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    @Secured("@")
    public boolean doValidateInputSerial() {
        try {
            if (stockTransSerialDTO == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "stock.validate.inspect.fromSerial.toSerial.not.empty");
            }

            if (stockTransSerialDTO.getFromSerial() == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "stock.validate.inspect.fromSerial.toSerial.not.empty");
            }
            if (stockTransSerialDTO.getFromSerial() == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "stock.validate.inspect.fromSerial.toSerial.not.empty");
            }

            String regex = "^[0-9]+";
            ProductOfferTypeDTO prodType = getProductOfferTypeDTO(productOfferingTotalDTO.getProductOfferTypeId());
            if (prodType == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "validate.stock.inspect.productOfferType.notFound");
            }
            if (prodType.getProductOfferTypeId().equals(Const.STOCK_TYPE.STOCK_HANDSET)) {
                if (stockTransSerialDTO.getFromSerial().length() > 30) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "import.partner.format.serial.maxlength", "mn.stock.fromSerial");
                }
                if (stockTransSerialDTO.getToSerial().length() > 30) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "import.partner.format.serial.maxlength", "mn.stock.fromSerial");
                }
                if (!stockTransSerialDTO.getFromSerial().equals(stockTransSerialDTO.getToSerial())) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "mn.stock.fromSerial.toSerial.equal");
                }
            } else {
                if (stockTransSerialDTO.getFromSerial().length() > 30
                        || !stockTransSerialDTO.getFromSerial().matches(regex)) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "import.partner.format.serial.failed", "mn.stock.fromSerial");
                }
                if (stockTransSerialDTO.getToSerial().length() > 30
                        || !stockTransSerialDTO.getToSerial().matches(regex)) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "import.partner.format.serial.failed", "mn.stock.ToSerial");
                }
            }
            return true;
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
        return false;
    }

    private ProductOfferTypeDTO getProductOfferTypeDTO(Long productOfferTypeId) {
        try {
            return productOfferTypeService.findOne(productOfferTypeId);
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
        return null;
    }

    @Secured("@")
    public void doImportValidate() {
        try {
            if (!DataUtil.isNullOrZero(importPartnerDetailDTO.getQuantity()) && importPartnerDetailDTO.getQuantity() <= 0L) {
                throw new LogicException("", "export.order.stock.number.format.msg");
            }
            if (!DataUtil.isNullOrEmpty(importPartnerDetailDTO.getA3a8()) && (!DataUtil.isNumber(importPartnerDetailDTO.getA3a8()) || DataUtil.safeToLong(importPartnerDetailDTO.getA3a8()) <= 0L)) {
                throw new LogicException("", "import.partner.required.a3a8");
            }
            if (!DataUtil.isNullOrEmpty(importPartnerDetailDTO.getKind()) && (!DataUtil.isNumber(importPartnerDetailDTO.getKind()) || DataUtil.safeToLong(importPartnerDetailDTO.getKind()) <= 0L)) {
                throw new LogicException("", "import.partner.required.kind");
            }
            if (Const.LongSimpleItem.importPartnerInputByFile.getValue().equals(importPartnerRequestDTO.getImpType()) && DataUtil.isNullObject(uploadedFile)) {
                throw new LogicException("", "stock.change.damaged.file.not.found");
            }
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    @Secured("@")
    public void doImport() {
        try {
            //voffice
            if (importPartnerRequestDTO.isSignVoffice()) {
                SignOfficeDTO signOfficeDTO = writeOfficeTag.validateVofficeAccount();
                importPartnerRequestDTO.setStockTransVofficeDTO(getStockTransVofficeDTO(signOfficeDTO));
            }
            //thong tin chi tiet
            importPartnerDetailDTO.setLstStockTransSerialDTO(listStockTransSerialDTOs);
            List<ImportPartnerDetailDTO> detailDTOs = Lists.newArrayList(importPartnerDetailDTO);
            importPartnerRequestDTO.setListImportPartnerDetailDTOs(detailDTOs);
            //thong tin user dang nhap
            importPartnerRequestDTO.setImportStaffId(getStaffDTO().getStaffId());
            importPartnerRequestDTO.setImportStaffCode(getStaffDTO().getStaffCode());

            //neu nhap mat hang la SIM va Import File
            if(Const.LongSimpleItem.importPartnerInputByFile.getValue().equals(importPartnerRequestDTO.getImpType()) &&
                    (productOfferingTotalDTO.getProductOfferTypeId() == Const.PRODUCE_IMSI_RANGE.EMPTY_AND_HOMEPHONE_SIM || productOfferingTotalDTO.getProductOfferTypeId() == Const.PRODUCE_IMSI_RANGE.POST_SIM)){
                validateImsiRange();
            }

            //tao giao dich - tra ve id cua giao dich xuat so
            Long transID = DataUtil.defaultIfNull(importStockFromPartnerService.executeStockTransForPartner(importPartnerRequestDTO, getTransRequiRedRoleMap()), 0L);
            if (DataUtil.safeEqual(-1L, transID)) {
                logger.error("result -1L");
                throw new LogicException("", "import.partner.result.fail");
            }
            if (DataUtil.isNullOrZero(transID)
                    && Const.ConfigIDCheck.KIT.validate(productOfferingTotalDTO.getProductOfferTypeId())) {
                logger.error(" result: " + transID == null ? "null" : transID);
                throw new LogicException("", "import.partner.result.fail");
            }
            //giao dich nhap theo file hoac dai so
            if (Const.LongSimpleItem.importPartnerInputByFile.getValue().equals(importPartnerRequestDTO.getImpType())
                    || Const.LongSimpleItem.importPartnerInputByRank.getValue().equals(importPartnerRequestDTO.getImpType())) {
                //lay giao dich nhap tu doi tac
//                VStockTransDTO vStockTransDTO = getVStockTransDTO(Const.STOCK_TRANS.TRANS_CODE_PN);
                //kiem tra neu bang null thi bao loi
//                if (vStockTransDTO == null || DataUtil.isNullOrZero(vStockTransDTO.getStockTransID())) {
//                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "import.partner.result.fail");
//                }
                //lay danh sach loi
                List<String> listErrors = DataUtil.defaultIfNull(importStockFromPartnerToBranchService.getErrorDetails(transID), Lists.newArrayList());
                //kiem tra so ban ghi loi
                Long total = 0L;
                Long fail = new Long(listErrors.size());
                if (fail.compareTo(0L) > 0) {
                    listError = listErrors;
                    renderedError = true;
                } else {
                    listError = Lists.newArrayList();
                }
                if ((Const.LongSimpleItem.importPartnerInputByFile.getValue().equals(importPartnerRequestDTO.getImpType()))) {
                    total = new Long(importPartnerDetailDTO.getLstParam().size());
                } else {
                    for (StockTransSerialDTO serialDTO : listStockTransSerialDTOs) {
                        total += serialDTO.getQuantity() == null ? 0L : serialDTO.getQuantity();
                    }
                }
                //khong thanh cong ban ghi nao
                if (total.compareTo(fail) == 0) {
                    logger.error(" total compare fail" + total + " - " + fail);
                    throw new LogicException("", "import.partner.result.fail");
                }
                topReportSuccess("", "import.partner.result.success", total - fail, total);
            } else {
                topReportSuccess("", "stock.trans.import.success");
            }
            importPartnerRequestDTO.setStatus(Const.LongSimpleItem.importPartnerRequestStatusImported.getValue());
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }

    }

    private void validateImsiRange() throws LogicException, Exception{
        int indexOfImsi = importPartnerRequestDTO.getListProfile().indexOf(Const.PRODUCE_IMSI_RANGE.IMSI);
        List<Long> imsiRanges = new ArrayList<>();
        for(String params : importPartnerDetailDTO.getLstParam()){
            String[] splits = params.split(Const.AppProperties4Sms.profileSparator.getValue().toString());
            String imsi = splits[indexOfImsi];
            if(!NumberUtil.isNumber(imsi))
                throw new LogicException("", "partnerStock.msg.imsi.characterInvalid", imsi);
            imsiRanges.add(Long.valueOf(imsi));
        }
        Collections.sort(imsiRanges);
        List<ImsiMadeDTO> lstImsi = new ArrayList<>();
        Long fromImsi = imsiRanges.get(0);
        for (int i = 1; i < imsiRanges.size(); i++) {
            if (imsiRanges.get(i) - imsiRanges.get(i - 1) != 1L) {
                ImsiMadeDTO imsiMadeDTO = new ImsiMadeDTO();
                imsiMadeDTO.setFromImsi(String.valueOf(fromImsi));
                imsiMadeDTO.setToImsi(String.valueOf(imsiRanges.get(i - 1)));
                lstImsi.add(imsiMadeDTO);
                fromImsi = imsiRanges.get(i);
            }
        }
        ImsiMadeDTO temp = new ImsiMadeDTO();
        temp.setFromImsi(String.valueOf(fromImsi));
        temp.setToImsi(String.valueOf(imsiRanges.get(imsiRanges.size() - 1).equals(fromImsi) ? fromImsi : imsiRanges.get(imsiRanges.size() - 1)));
        lstImsi.add(temp);

        for(ImsiMadeDTO imsiMadeDTO : lstImsi){
            if(!imsiMadeService.validateImsiRange(imsiMadeDTO.getFromImsi(), imsiMadeDTO.getToImsi())){
                throw new LogicException("", "partnerStock.msg.imsiRange.invalid", imsiMadeDTO.getFromImsi(), imsiMadeDTO.getToImsi());
            }
        }
    }

    /**
     * Load danh sach mat hang theo loai hang hoa
     */
    @Secured("@")
    public void onChangeOfferType() {
        try {
            lstProductOfferingTotalDTO = Lists.newArrayList();
            productOfferingTotalDTO = null;
            if (DataUtil.isNullObject(productOfferTypeDTO)) {
                productOfferTypeDTO = new ProductOfferTypeDTO();
            }
            kit = false;
            sim = false;
            renderPrintExp = false;
            showDetail = false;
            lstProductSim = Lists.newArrayList();
            importPartnerRequestDTO.setProfile(null);
            lstImportType = optionSetValueService.getByOptionSetCode(Const.OPTION_SET.IMPORT_TYPE_PARTNER);
            importPartnerRequestDTO.setImpType(Const.LongSimpleItem.importPartnerInputByFile.getValue());
//            productOfferTypeDTO.setProductOfferTypeId(DataUtil.safeToLong(productOfferTypeId));
            if (!DataUtil.isNullObject(productOfferTypeDTO)) {
                if (DataUtil.safeEqual(productOfferTypeDTO.getProductOfferTypeId(), Const.PRODUCT_OFFER_TYPE.PRODUCT_NO_SERIAL)) {
                    importPartnerRequestDTO.setImpType(Const.LongSimpleItem.importPartnerInputByNumber.getValue());
                    lstImportType.remove(0);
                    lstImportType.remove(0);
                } else {
                    importPartnerRequestDTO.setImpType(Const.LongSimpleItem.importPartnerInputByFile.getValue());
                    lstImportType.remove(2);
                }
                if (DataUtil.safeEqual(productOfferTypeDTO.getProductOfferTypeId(), Const.PRODUCT_OFFER_TYPE.KIT)) {
                    kit = true;
                    sim = false;
                    lstProductSim = productOfferingService.getListProductOfferingByProOfTypeId(Const.STOCK_TYPE.STOCK_SIM_PRE_PAID, null, null);
                    renderPrintExp = true;
                }
                if (DataUtil.safeEqual(productOfferTypeDTO.getProductOfferTypeId(), Const.PRODUCT_OFFER_TYPE.EMPTY_SIM) || DataUtil.safeEqual(productOfferTypeDTO.getProductOfferTypeId(), Const.PRODUCT_OFFER_TYPE.HOMEPHONE)) {
                    kit = false;
                    sim = true;
                }
            }
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    /**
     * Tim kiem mat hang khi nhap tay
     */
    @Secured("@")
    public List<ProductOfferingTotalDTO> searchListProductOffering(String input) {
        try {
            CharSequence inputData = DataUtil.isNullOrEmpty(input) ? "" : input.trim().toUpperCase();
            lstProductOfferingTotalDTO = productOfferingService.getListProductOfferingByProductType(inputData.toString(), productOfferTypeDTO.getProductOfferTypeId());
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
        importPartnerRequestDTO.setProfile(null);
        importPartnerRequestDTO.setListProfile(null);
        importPartnerRequestDTO.setImpType(Const.LongSimpleItem.importPartnerInputByFile.getValue());
        renderedImport = true;
        uploadedFile = null;
        stockTransSerialDTO = new StockTransSerialDTO();
        rank = null;
        showDetail = false;
    }

    /**
     * check cho truong hop nhap theo dai hoac theo file
     *
     * @return
     */
    @Secured("@")
    public void doCheckImpType() {
        try {
            if (DataUtil.isNullOrZero(productOfferTypeDTO.getProductOfferTypeId())) {
                importPartnerRequestDTO.setImpType(Const.LongSimpleItem.importPartnerInputByFile.getValue());
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "mn.stock.branch.type.product.msg.require");
            }
            if (DataUtil.isNullObject(productOfferingTotalDTO)) {
                importPartnerRequestDTO.setImpType(Const.LongSimpleItem.importPartnerInputByFile.getValue());
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "mn.isdn.manage.create.field.validate.productOffer");
            }
            renderedImport = true;
            uploadedFile = null;
            stockTransSerialDTO = new StockTransSerialDTO();
            rank = null;
            showDetail = false;
            if (DataUtil.safeEqual(importPartnerRequestDTO.getImpType(), Const.EXPORT_PARTNER.EXP_BY_QUANTITY)) {
                //theo file
                if (DataUtil.isNullOrEmpty(importPartnerRequestDTO.getListProfile())) {
                    importPartnerRequestDTO.setImpType(Const.LongSimpleItem.importPartnerInputByFile.getValue());
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "import.partner.valid.importByFile");
                }
                showDetail = false;
            } else if (DataUtil.safeEqual(importPartnerRequestDTO.getImpType(), Const.EXPORT_PARTNER.EXP_BY_SERIAL_RANGE)) {
                //theo dai
                renderedImport = false;
                if (DataUtil.isNullOrEmpty(importPartnerRequestDTO.getListProfile()) || (!DataUtil.isNullOrEmpty(importPartnerRequestDTO.getListProfile())
                        && importPartnerRequestDTO.getListProfile().size() > 1)) {
                    importPartnerRequestDTO.setImpType(Const.LongSimpleItem.importPartnerInputByFile.getValue());
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "import.partner.valid.importByRange");
                }
            }
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    /**
     * goi khi thay doi buoc nhay hoac from serial khi nhap serial theo dai
     */

    public StreamedContent exportErrorFile() {
        try {
            if (DataUtil.isNullOrEmpty(listError)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "import.partner.valid.trans");
            }
            return FileUtil.exportToStreamedText(listError, getStaffDTO().getStaffCode().toLowerCase() + "_" + Const.EXPORT_FILE_NAME.IMPORT_PARTNER_SERIAL_ERROR);
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
        return null;
    }

    @Secured("@")
    public void receiveShop(VShopStaffDTO vShopStaffDTO) {
        importPartnerRequestDTO.setToOwnerId(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
    }

    public Boolean getShowCheckErp() {
        return requiredRoleMap != null && requiredRoleMap.hasRole(Const.PERMISION.ROLE_DONGBO_ERP);
    }

    public boolean getShowDisableOffering() {
        return productOfferingTotalDTO == null;
    }

    @Secured("@")
    public void clearShop() {
        importPartnerRequestDTO.setToOwnerId(null);
    }

    public boolean isContractType() {
        return contractType;
    }

    public void setContractType(boolean contractType) {
        this.contractType = contractType;
    }

    public boolean isRenderedError() {
        return renderedError;
    }

    public void setRenderedError(boolean renderedError) {
        this.renderedError = renderedError;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public boolean isRateMoney() {
        return rateMoney;
    }

    public void setRateMoney(boolean rateMoney) {
        this.rateMoney = rateMoney;
    }

    public boolean isKit() {
        return kit;
    }

    public void setKit(boolean kit) {
        this.kit = kit;
    }

    public boolean isSim() {
        return sim;
    }

    public void setSim(boolean sim) {
        this.sim = sim;
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

    public boolean isThreeProvince() {
        return threeProvince;
    }

    public void setThreeProvince(boolean threeProvince) {
        this.threeProvince = threeProvince;
    }

    public List<OptionSetValueDTO> getLstImportType() {
        return lstImportType;
    }

    public void setLstImportType(List<OptionSetValueDTO> lstImportType) {
        this.lstImportType = lstImportType;
    }

    public ImportPartnerRequestDTO getImportPartnerRequestDTO() {
        return importPartnerRequestDTO;
    }

    public void setImportPartnerRequestDTO(ImportPartnerRequestDTO importPartnerRequestDTO) {
        this.importPartnerRequestDTO = importPartnerRequestDTO;
    }

    public List<ShopDTO> getLsRegionShop() {
        return lsRegionShop;
    }

    public void setLsRegionShop(List<ShopDTO> lsRegionShop) {
        this.lsRegionShop = lsRegionShop;
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

    public ProductOfferTypeDTO getProductOfferTypeDTO() {
        return productOfferTypeDTO;
    }

    public void setProductOfferTypeDTO(ProductOfferTypeDTO productOfferTypeDTO) {
        this.productOfferTypeDTO = productOfferTypeDTO;
    }

    public String getProductOfferTypeId() {
        return productOfferTypeId;
    }

    public void setProductOfferTypeId(String productOfferTypeId) {
        this.productOfferTypeId = productOfferTypeId;
    }

    public List<ProductOfferingTotalDTO> getLstProductOfferingTotalDTO() {
        return lstProductOfferingTotalDTO;
    }

    public void setLstProductOfferingTotalDTO(List<ProductOfferingTotalDTO> lstProductOfferingTotalDTO) {
        this.lstProductOfferingTotalDTO = lstProductOfferingTotalDTO;
    }

    public ProductOfferingTotalDTO getProductOfferingTotalDTO() {
        return productOfferingTotalDTO;
    }

    public void setProductOfferingTotalDTO(ProductOfferingTotalDTO productOfferingTotalDTO) {
        this.productOfferingTotalDTO = productOfferingTotalDTO;
    }

    public ImportPartnerDetailDTO getImportPartnerDetailDTO() {
        return importPartnerDetailDTO;
    }

    public void setImportPartnerDetailDTO(ImportPartnerDetailDTO importPartnerDetailDTO) {
        this.importPartnerDetailDTO = importPartnerDetailDTO;
    }

    public List<OptionSetValueDTO> getLstCurrencyType() {
        return lstCurrencyType;
    }

    public void setLstCurrencyType(List<OptionSetValueDTO> lstCurrencyType) {
        this.lstCurrencyType = lstCurrencyType;
    }

    public SignOfficeTagNameable getWriteOfficeTag() {
        return writeOfficeTag;
    }

    public void setWriteOfficeTag(SignOfficeTagNameable writeOfficeTag) {
        this.writeOfficeTag = writeOfficeTag;
    }

    public List<OptionSetValueDTO> getLstSimType() {
        return lstSimType;
    }

    public void setLstSimType(List<OptionSetValueDTO> lstSimType) {
        this.lstSimType = lstSimType;
    }

    public List<ProductOfferingDTO> getLstProductSim() {
        return lstProductSim;
    }

    public void setLstProductSim(List<ProductOfferingDTO> lstProductSim) {
        this.lstProductSim = lstProductSim;
    }

    public StockTransSerialDTO getStockTransSerialDTO() {
        return stockTransSerialDTO;
    }

    public void setStockTransSerialDTO(StockTransSerialDTO stockTransSerialDTO) {
        this.stockTransSerialDTO = stockTransSerialDTO;
    }

    public Long getRank() {
        return rank;
    }

    public void setRank(Long rank) {
        this.rank = rank;
    }

    public boolean isRenderedImport() {
        return renderedImport;
    }

    public void setRenderedImport(boolean renderedImport) {
        this.renderedImport = renderedImport;
    }

    public List<StockTransSerialDTO> getListStockTransSerialDTOs() {
        return listStockTransSerialDTOs;
    }

    public void setListStockTransSerialDTOs(List<StockTransSerialDTO> listStockTransSerialDTOs) {
        this.listStockTransSerialDTOs = listStockTransSerialDTOs;
    }

    public boolean isShowDetail() {
        return showDetail;
    }

    public void setShowDetail(boolean showDetail) {
        this.showDetail = showDetail;
    }

    public boolean isRenderPrintExp() {
        return renderPrintExp;
    }

    public void setRenderPrintExp(boolean renderPrintExp) {
        this.renderPrintExp = renderPrintExp;
    }

    public boolean isCheckErp() {
        return checkErp;
    }

    public void setCheckErp(boolean checkErp) {
        this.checkErp = checkErp;
    }
}
