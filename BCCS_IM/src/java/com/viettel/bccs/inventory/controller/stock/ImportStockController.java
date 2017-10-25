package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.util.FileUtil;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.bccs.inventory.tag.SignOfficeTagNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
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
import java.io.*;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * @author luannt23
 * @comment
 */
@Component
@Scope("view")
@ManagedBean
public class ImportStockController extends ExportFileStockTransController {
    private enum Mode {
        All("ALL"), KIT("KIT"), SIM("SIM");

        Mode(String val) {
            this.value = val;
        }

        private String value;
    }

    private boolean renderedDetail;
    private ImportPartnerRequestDTO requestDTO;
    private List<ImportPartnerRequestDTO> listImportPartnerRequestDTOs;
    private List<PartnerDTO> listPartnerDTOs;
    private ImportPartnerRequestDTO selectedImportPartnerRequestDTO;
    private List<ImportPartnerDetailDTO> listImportPartnerDetailDTO;
    private List<PartnerDTO> lstPartnerDTO;
    private ProductOfferingDTO productOfferingTotalDTO;
    private ImportPartnerDetailDTO importPartnerDetailDTO;
    private StockTransSerialDTO stockTransSerialDTO;
    private boolean signVofficeFlag;
    private boolean notSyncERPFlag;
    private Long rank;
    private List<StockTransSerialDTO> listStockTransSerialDTOs;
    private List<ProductOfferingDTO> lstProductOfferingTotalDTO;
    private Long totalSerial;
    private boolean renderedImport = false;
    private boolean renderPrintExp = false;
    private UploadedFile uploadedFile;
    private Long createdTransId;
    private boolean renderedError = false;
    private List<String> listError;

    @Autowired
    private ImportStockFromPartnerToBranchService importStockFromPartnerToBranchService;
    @Autowired
    private ImportPartnerRequestService importPartnerRequestService;
    @Autowired
    private PartnerService partnerService;
    @Autowired
    ShopInfoNameable shopInfoNameable;
    @Autowired
    private ProductOfferTypeService productOfferTypeService;

    @Autowired
    private ImportPartnerDetailService importPartnerDetailService;

    @Autowired
    private SignOfficeTagNameable writeOfficeTag;

    @Autowired
    private ProductOfferingService productOfferingService;

    @Secured("@")
    @PostConstruct
    public void init() {
        try {
            renderedDetail = false;
            selectedImportPartnerRequestDTO = null;
            listImportPartnerDetailDTO = null;
            importPartnerDetailDTO = null;
            //
            shopInfoNameable.initShop(this, Const.VT_SHOP_ID, true);
            Date sysdate = optionSetValueService.getSysdateFromDB(true);
            listPartnerDTOs = partnerService.findPartner(new PartnerDTO());
            requestDTO = new ImportPartnerRequestDTO();
            requestDTO.setFromDate(sysdate);
            requestDTO.setToDate(sysdate);
            requestDTO.setStatus(Const.LongSimpleItem.importPartnerRequestStatusApproved.getValue());
            requestDTO.setToOwnerId(getStaffDTO().getShopId());
            requestDTO.setToOwnerName(getStaffDTO().getShopName());
            listImportPartnerRequestDTOs = Lists.newArrayList();
            lstPartnerDTO = partnerService.findPartner(new PartnerDTO());
            doSearchRequest();
            executeCommand("updateControls();");
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    @Secured("@")
    public void doSearchRequest() {
        try {
            renderedDetail = false;
            validateDate(requestDTO.getFromDate(), requestDTO.getToDate());
            if (requestDTO.getRequestCode() != null) {
                requestDTO.setRequestCode(requestDTO.getRequestCode().trim());
            }
            listImportPartnerRequestDTOs = DataUtil.defaultIfNull(importPartnerRequestService.findImportPartnerRequest(requestDTO), Lists.newArrayList());
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    @Secured("@")
    public StreamedContent downloadAttackFile(ImportPartnerRequestDTO importPartnerRequestDTO) {
        try {
            byte[] content = importPartnerRequestService.getAttachFileContent(importPartnerRequestDTO.getImportPartnerRequestId());
            if (content == null || content.length == 0) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "import.partner.request.valid.attach");
            }
            InputStream is = new ByteArrayInputStream(content);
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            StreamedContent streamedContent = new DefaultStreamedContent(is, externalContext.getMimeType(importPartnerRequestDTO.getDocumentName()), importPartnerRequestDTO.getDocumentName());
            return streamedContent;
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
        return null;
    }

    @Secured("@")
    public void onDetail(ImportPartnerRequestDTO requestDTO) {
        try {
            if (requestDTO != null) {
                //flag
                signVofficeFlag = true;
                notSyncERPFlag = false;
                rank = Const.LongSimpleItem.importPartnerRankSerial1.getValue();
                renderedImport = false;
                renderPrintExp = false;
                uploadedFile = null;
                createdTransId = 0L;
                renderedError = false;
                renderedDetail = false;
                //
                writeOfficeTag.init(this, getStaffDTO().getShopId());
                //
                stockTransSerialDTO = new StockTransSerialDTO();
                //
                listStockTransSerialDTOs = Lists.newArrayList();
                //
                selectedImportPartnerRequestDTO = requestDTO;
                selectedImportPartnerRequestDTO.setImpType(Const.LongSimpleItem.importPartnerInputByRank.getValue());
                //action code
                selectedImportPartnerRequestDTO.setActionCode(staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PN, Const.STOCK_TRANS_TYPE.IMPORT, getStaffDTO()));
                //
                ImportPartnerDetailDTO search = new ImportPartnerDetailDTO();
                search.setImportPartnerRequestId(selectedImportPartnerRequestDTO.getImportPartnerRequestId());
                listImportPartnerDetailDTO = DataUtil.defaultIfNull(importPartnerDetailService.findImportPartnerDetail(search), Lists.newArrayList());
                if (!DataUtil.isNullOrEmpty(listImportPartnerDetailDTO)) {
                    importPartnerDetailDTO = listImportPartnerDetailDTO.get(0);
                    productOfferingTotalDTO = getProductOffering(importPartnerDetailDTO.getProdOfferId());
                    if (productOfferingTotalDTO != null) {
                        if (!DataUtil.safeEqual(productOfferingTotalDTO.getCheckSerial(), 1L)) {
                            //Nhap theo so luong
                            renderedImport = true;
                            importPartnerDetailDTO.setRealQuantity(importPartnerDetailDTO.getQuantity());
                            selectedImportPartnerRequestDTO.setImpType(Const.LongSimpleItem.importPartnerInputByNumber.getValue());
                        } else {
                            //Nhap theo dai -file
                            ProfileDTO profileDTO = productOfferingService.getProfileByProductId(importPartnerDetailDTO.getProdOfferId());
                            if (profileDTO != null
                                    && !DataUtil.isNullOrEmpty(profileDTO.getLstColumnName())) {
                                selectedImportPartnerRequestDTO.setListProfile(profileDTO.getLstColumnName());
                                importPartnerDetailDTO.setRealQuantity(0L);
                                if (profileDTO.getLstColumnName().size() > 1
                                        || Const.ConfigIDCheck.HANDSET.validate(productOfferingTotalDTO.getProductOfferTypeId())) {
                                    selectedImportPartnerRequestDTO.setImpType(Const.LongSimpleItem.importPartnerInputByFile.getValue());
                                }
                            } else {
                                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "import.partner.valid.profile");
                            }
                        }
                        if (Const.STOCK_TYPE.STOCK_KIT.equals(productOfferingTotalDTO.getProductOfferTypeId())) {
                            lstProductOfferingTotalDTO = Lists.newArrayList();
                            List<ProductOfferingDTO> prePaid = productOfferingService.getListProductOfferingByProOfTypeId(Const.STOCK_TYPE.STOCK_SIM_PRE_PAID, null, null);
                            List<ProductOfferingDTO> postPaid = productOfferingService.getListProductOfferingByProOfTypeId(Const.STOCK_TYPE.STOCK_SIM_POST_PAID, null, null);
                            if (prePaid != null) {
                                lstProductOfferingTotalDTO.addAll(prePaid);
                            }
                            if (postPaid != null) {
                                lstProductOfferingTotalDTO.addAll(postPaid);
                            }
                        }
                        //hien thi px sim:
                        if (Const.ConfigIDCheck.KIT.validate(productOfferingTotalDTO.getProductOfferTypeId())) {
                            renderPrintExp = true;
                        }
                    } else {
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "import.partner.valid.detail");
                    }
                }
                stockTransSerialDTO = new StockTransSerialDTO();
                renderedDetail = true;
            } else {
                renderedDetail = false;
                selectedImportPartnerRequestDTO = null;
                listImportPartnerDetailDTO = null;
            }
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    @Secured("@")
    public String getProductOfferType() {
        try {
            if (productOfferingTotalDTO != null) {
                return getProductOfferTypeDTO(productOfferingTotalDTO.getProductOfferTypeId()).getName();
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return "";
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

    @Secured("@")
    public void doChangeSerial() {
        BigInteger maxRange = new BigInteger(Const.LongSimpleItem.maxRange.getValue().toString());
        totalSerial = 0L;
        renderedImport = false;
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
                    if (totalSerial.compareTo(importPartnerDetailDTO.getQuantity()) != 0) {
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "import.partner.serialContraints.realQuantity");
                    }
                    //add to list serial
                    StockTransSerialDTO serialDTO = new StockTransSerialDTO();
                    serialDTO.setFromSerial(stockTransSerialDTO.getFromSerial());
                    serialDTO.setToSerial(stockTransSerialDTO.getToSerial());
                    serialDTO.setQuantity(total.longValue());
                    listStockTransSerialDTOs.add(serialDTO);
                } else {
                    attachSerialRange(fromSerial, toSerial);
                }
            }
            if (importPartnerDetailDTO.getRealQuantity().compareTo(importPartnerDetailDTO.getQuantity()) == 0) {
                renderedImport = true;
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
            if (signVofficeFlag) {
                SignOfficeDTO signOfficeDTO = writeOfficeTag.validateVofficeAccount();
                selectedImportPartnerRequestDTO.setSignVoffice(true);
                selectedImportPartnerRequestDTO.setStockTransVofficeDTO(getStockTransVofficeDTO(signOfficeDTO));
            } else {
                selectedImportPartnerRequestDTO.setSignVoffice(false);
            }
            //erp
            selectedImportPartnerRequestDTO.setSyncERP(!notSyncERPFlag);
            //thong tin chi tiet
            importPartnerDetailDTO.setLstStockTransSerialDTO(listStockTransSerialDTOs);
            List<ImportPartnerDetailDTO> detailDTOs = Lists.newArrayList(importPartnerDetailDTO);
            selectedImportPartnerRequestDTO.setListImportPartnerDetailDTOs(detailDTOs);
            //thong tin user dang nhap
            selectedImportPartnerRequestDTO.setImportStaffId(getStaffDTO().getStaffId());
            selectedImportPartnerRequestDTO.setImportStaffCode(getStaffDTO().getStaffCode());
            //tao giao dich - tra ve id cua giao dich xuat so
            Long transID = DataUtil.defaultIfNull(importStockFromPartnerToBranchService.executeStockTrans(selectedImportPartnerRequestDTO, getTransRequiRedRoleMap()), 0L);
            if (DataUtil.safeEqual(-1L, transID)) {
                logger.error("result -1L");
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "import.partner.result.fail");
            }
            if (DataUtil.isNullOrZero(transID)
                    && Const.ConfigIDCheck.KIT.validate(productOfferingTotalDTO.getProductOfferTypeId())) {
                logger.error(" result: " + transID == null ? "null" : transID);
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "import.partner.result.fail");
            }
            //giao dich nhap theo file hoac dai so
            if (Const.LongSimpleItem.importPartnerInputByFile.getValue().equals(selectedImportPartnerRequestDTO.getImpType())
                    || Const.LongSimpleItem.importPartnerInputByRank.getValue().equals(selectedImportPartnerRequestDTO.getImpType())) {
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
                if ((Const.LongSimpleItem.importPartnerInputByFile.getValue().equals(selectedImportPartnerRequestDTO.getImpType()))) {
                    total = new Long(importPartnerDetailDTO.getLstParam().size());
                } else {
                    for (StockTransSerialDTO serialDTO : listStockTransSerialDTOs) {
                        total += serialDTO.getQuantity() == null ? 0L : serialDTO.getQuantity();
                    }
                }
                //khong thanh cong ban ghi nao
                if (total.compareTo(fail) == 0) {
                    logger.error(" total compare fail" + total + " - " + fail);
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "import.partner.result.fail");
                }
                topReportSuccess("", "import.partner.result.success", total - fail, total);
            } else {
                topReportSuccess("", "stock.trans.import.success");
            }
            selectedImportPartnerRequestDTO.setStatus(Const.LongSimpleItem.importPartnerRequestStatusImported.getValue());
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }

    }

    @Secured("@")
    public void doValidateImport() {
        try {
            if (Const.LongSimpleItem.importPartnerInputByRank.getValue().equals(selectedImportPartnerRequestDTO.getImpType())) {
                if (DataUtil.isNullOrEmpty(listStockTransSerialDTOs) || DataUtil.isNullOrZero(importPartnerDetailDTO.getRealQuantity())) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "import.partner.required.serial");
                }
                if (totalSerial.compareTo(importPartnerDetailDTO.getQuantity()) != 0) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "import.partner.serialContraints.realQuantity");
                }
            } else {
                if (Const.LongSimpleItem.importPartnerInputByFile.getValue().equals(selectedImportPartnerRequestDTO.getImpType())) {
                    if (DataUtil.isNullOrEmpty(importPartnerDetailDTO.getLstParam())) {
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "import.partner.required.serial");
                    }
                }
            }
            doValidateOtherInput();
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    private void doValidateOtherInput() throws LogicException, Exception {
        if (Const.ConfigIDCheck.SIM.validate(productOfferingTotalDTO.getProductOfferTypeId())) {
            String regex = "^[0-9]+";
            if (DataUtil.isNullOrEmpty(importPartnerDetailDTO.getKind())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "import.partner.required.kind");
            }
            importPartnerDetailDTO.setKind(importPartnerDetailDTO.getKind().trim());
            if (!importPartnerDetailDTO.getKind().matches(regex)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "import.partner.required.kind");
            }
            if (DataUtil.isNullOrEmpty(importPartnerDetailDTO.getA3a8())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "import.partner.required.a3a8");
            }
            if (!importPartnerDetailDTO.getA3a8().matches(regex)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "import.partner.required.a3a8");
            }
            importPartnerDetailDTO.setA3a8(importPartnerDetailDTO.getA3a8().trim());
        }
        if (Const.ConfigIDCheck.KIT.validate(productOfferingTotalDTO.getProductOfferTypeId())) {
            if (DataUtil.isNullOrZero(importPartnerDetailDTO.getProdOfferSimId())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "import.partner.required.sim");
            }
        }
    }

    private void attachSerialRange(BigInteger fromSerial, BigInteger toSerial) throws LogicException, Exception {
        BigInteger bRange = new BigInteger(rank.toString());
        BigInteger range = bRange.add(new BigInteger("-1"));
        while (totalSerial.compareTo(importPartnerDetailDTO.getQuantity()) < 0) {
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
            if (totalSerial.compareTo(importPartnerDetailDTO.getQuantity()) > 0) {
                div = totalSerial - importPartnerDetailDTO.getQuantity();
                totalSerial = importPartnerDetailDTO.getQuantity();
                to = to.add(new BigInteger(div.toString()).negate());
            }
            StockTransSerialDTO serialDTO = new StockTransSerialDTO();
            serialDTO.setFromSerial(from.toString());
            serialDTO.setToSerial(to.toString());
            serialDTO.setQuantity(to.add(from.negate()).longValue() + 1L);
            listStockTransSerialDTOs.add(serialDTO);
        }

        importPartnerDetailDTO.setRealQuantity(totalSerial);
        if (totalSerial.compareTo(importPartnerDetailDTO.getQuantity()) != 0) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "import.partner.serialContraints.realQuantity");
        }
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

    private void readSerialFromFile(InputStream stream) throws LogicException, Exception {
        if (DataUtil.isNullOrEmpty(selectedImportPartnerRequestDTO.getListProfile())) {
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
            if (splits.length != selectedImportPartnerRequestDTO.getListProfile().size()) {
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
            renderedError = true;
            listError = Lists.newArrayList(readerData);
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "import.partner.valid.fileContent");
        }
        importPartnerDetailDTO.setLstParam(readerData);
        if (importPartnerDetailDTO.getQuantity() == null) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "import.partner.valid.requestQuantity");
        }
        if (importPartnerDetailDTO.getQuantity().compareTo(new Long(readerData.size())) != 0) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "import.partner.serialContraints.realQuantity");
        }
        renderedImport = true;
    }

    public boolean initOtherInput(String mode) {
        try {
            if (productOfferingTotalDTO != null) {
                ProductOfferTypeDTO offerTypeDTO = getProductOfferTypeDTO(productOfferingTotalDTO.getProductOfferTypeId());
                if (offerTypeDTO == null) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "common.msg.exception");
                }
                if (Mode.All.value.equals(mode)) {
                    //hien thi pnl nhap thong tin bo xung cho kit hoac sim
                    return Const.ConfigIDCheck.importPartnerOtherInput.validate(offerTypeDTO.getProductOfferTypeId());
                } else if (Mode.KIT.value.equals(mode)) {
                    //hien thi vung thong tin bo xung cho kit
                    return Const.ConfigIDCheck.importPartnerKit.validate(offerTypeDTO.getProductOfferTypeId());
                } else if (Mode.SIM.value.equals(mode)) {
                    //hien thi vung thong tin bo xung cho sim
                    return Const.ConfigIDCheck.importPartnerSim.validate(offerTypeDTO.getProductOfferTypeId());
                }
            }
        } catch (LogicException ex) {
            importPartnerRequestService = null;
            importPartnerDetailService = null;
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
        return false;
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
            vStockTransDTO.setActionCode(selectedImportPartnerRequestDTO.getActionCode());
            vStockTransDTO.setOwnerType(Const.OWNER_TYPE.PARTNER_LONG);
            vStockTransDTO.setOwnerID(selectedImportPartnerRequestDTO.getPartnerId());
        }
        List<VStockTransDTO> vStockTransDTOs = transService.mmSearchVStockTrans(vStockTransDTO);
        if (DataUtil.isNullOrEmpty(vStockTransDTOs)) {
            return null;
        }
        return vStockTransDTOs.get(0);
    }

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

    /**
     * check cho truong hop nhap theo dai hoac theo file
     *
     * @return
     */
    @Secured("@")
    public boolean doCheckImpType() {
        try {
            if (Const.LongSimpleItem.importPartnerInputByFile.equals(selectedImportPartnerRequestDTO.getImpType())) {
                //theo file
                if (DataUtil.isNullOrEmpty(selectedImportPartnerRequestDTO.getListProfile())) {
                    selectedImportPartnerRequestDTO.setImpType(Const.LongSimpleItem.importPartnerInputByFile.getValue());
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "import.partner.valid.importByFile");
                }
            } else {
                //theo dai
                if (!DataUtil.isNullOrEmpty(selectedImportPartnerRequestDTO.getListProfile())
                        && selectedImportPartnerRequestDTO.getListProfile().size() > 1) {
                    selectedImportPartnerRequestDTO.setImpType(Const.LongSimpleItem.importPartnerInputByFile.getValue());
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "import.partner.valid.importByRange");
                }
                if (Const.ConfigIDCheck.HANDSET.validate(productOfferingTotalDTO.getProductOfferTypeId())) {
                    selectedImportPartnerRequestDTO.setImpType(Const.LongSimpleItem.importPartnerInputByFile.getValue());
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "import.partner.valid.handset");
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

    /**
     * goi khi thay doi buoc nhay hoac from serial khi nhap serial theo dai
     */
    @Secured("@")
    public void onChangeSerial() {
        if (stockTransSerialDTO == null || DataUtil.isNullOrEmpty(stockTransSerialDTO.getFromSerial())) {
            return;
        }
        try {
            BigInteger pre = new BigInteger("-1");
            BigInteger bRank = new BigInteger(rank.toString());
            String regex = "^[0-9]+";
            if (stockTransSerialDTO.getFromSerial().length() > 30
                    || !stockTransSerialDTO.getFromSerial().matches(regex)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "import.partner.format.serial.failed", "mn.stock.fromSerial");
            }
            BigInteger from = new BigInteger(stockTransSerialDTO.getFromSerial());
            stockTransSerialDTO.setQuantity(importPartnerDetailDTO.getQuantity());
            if (rank.compareTo(1L) == 0) {
                stockTransSerialDTO.setToSerial(from.add(new BigInteger(importPartnerDetailDTO.getQuantity().toString()).add(new BigInteger("-1"))).toString());
            } else {
                Long total = 0l;
                BigInteger to = new BigInteger(from.toString());
                while (total.compareTo(stockTransSerialDTO.getQuantity()) < 0) {
                    to = to.add(bRank);
                    to = to.add(bRank);
                    total += rank;
                }
                to = to.add(bRank.negate());
                if (total.compareTo(importPartnerDetailDTO.getQuantity()) > 0) {
                    Long sub = total - importPartnerDetailDTO.getQuantity();
                    to = to.add(new BigInteger(sub.toString()).negate());
                    stockTransSerialDTO.setQuantity(total - sub);
                }
                to = to.add(pre);
                stockTransSerialDTO.setToSerial(to.toString());
            }
            importPartnerDetailDTO.setRealQuantity(stockTransSerialDTO.getQuantity());
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }

    }

    public Date getCurrentDate() {
        return Calendar.getInstance().getTime();
    }

    public boolean isRenderedDetail() {
        return renderedDetail;
    }

    public void setRenderedDetail(boolean renderedDetail) {
        this.renderedDetail = renderedDetail;
    }

    public ImportPartnerRequestDTO getRequestDTO() {
        return requestDTO;
    }

    public void setRequestDTO(ImportPartnerRequestDTO requestDTO) {
        this.requestDTO = requestDTO;
    }

    public ImportPartnerRequestService getImportPartnerRequestService() {
        return importPartnerRequestService;
    }

    public void setImportPartnerRequestService(ImportPartnerRequestService importPartnerRequestService) {
        this.importPartnerRequestService = importPartnerRequestService;
    }

    public List<ImportPartnerRequestDTO> getListImportPartnerRequestDTOs() {
        return listImportPartnerRequestDTOs;
    }

    public void setListImportPartnerRequestDTOs(List<ImportPartnerRequestDTO> listImportPartnerRequestDTOs) {
        this.listImportPartnerRequestDTOs = listImportPartnerRequestDTOs;
    }

    public List<PartnerDTO> getListPartnerDTOs() {
        return listPartnerDTOs;
    }

    public void setListPartnerDTOs(List<PartnerDTO> listPartnerDTOs) {
        this.listPartnerDTOs = listPartnerDTOs;
    }

    public ShopInfoNameable getShopInfoNameable() {
        return shopInfoNameable;
    }

    public void setShopInfoNameable(ShopInfoNameable shopInfoNameable) {
        this.shopInfoNameable = shopInfoNameable;
    }

    public ImportPartnerRequestDTO getSelectedImportPartnerRequestDTO() {
        return selectedImportPartnerRequestDTO;
    }

    public void setSelectedImportPartnerRequestDTO(ImportPartnerRequestDTO selectedImportPartnerRequestDTO) {
        this.selectedImportPartnerRequestDTO = selectedImportPartnerRequestDTO;
    }

    public List<ImportPartnerDetailDTO> getListImportPartnerDetailDTO() {
        return listImportPartnerDetailDTO;
    }

    public void setListImportPartnerDetailDTO(List<ImportPartnerDetailDTO> listImportPartnerDetailDTO) {
        this.listImportPartnerDetailDTO = listImportPartnerDetailDTO;
    }

    public List<PartnerDTO> getLstPartnerDTO() {
        return lstPartnerDTO;
    }

    public void setLstPartnerDTO(List<PartnerDTO> lstPartnerDTO) {
        this.lstPartnerDTO = lstPartnerDTO;
    }

    public boolean isSignVofficeFlag() {
        return signVofficeFlag;
    }

    public void setSignVofficeFlag(boolean signVofficeFlag) {
        this.signVofficeFlag = signVofficeFlag;
    }

    public boolean isNotSyncERPFlag() {
        return notSyncERPFlag;
    }

    public void setNotSyncERPFlag(boolean notSyncERPFlag) {
        this.notSyncERPFlag = notSyncERPFlag;
    }

    public ProductOfferingDTO getProductOfferingTotalDTO() {
        return productOfferingTotalDTO;
    }

    public void setProductOfferingTotalDTO(ProductOfferingDTO productOfferingTotalDTO) {
        this.productOfferingTotalDTO = productOfferingTotalDTO;
    }

    public SignOfficeTagNameable getWriteOfficeTag() {
        return writeOfficeTag;
    }

    public void setWriteOfficeTag(SignOfficeTagNameable writeOfficeTag) {
        this.writeOfficeTag = writeOfficeTag;
    }

    public ImportPartnerDetailDTO getImportPartnerDetailDTO() {
        return importPartnerDetailDTO;
    }

    public void setImportPartnerDetailDTO(ImportPartnerDetailDTO importPartnerDetailDTO) {
        this.importPartnerDetailDTO = importPartnerDetailDTO;
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

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public List<ProductOfferingDTO> getLstProductOfferingTotalDTO() {
        return lstProductOfferingTotalDTO;
    }

    public void setLstProductOfferingTotalDTO(List<ProductOfferingDTO> lstProductOfferingTotalDTO) {
        this.lstProductOfferingTotalDTO = lstProductOfferingTotalDTO;
    }

    public boolean isRenderedError() {
        return renderedError;
    }

    public void setRenderedError(boolean renderedError) {
        this.renderedError = renderedError;
    }

    public List<StockTransSerialDTO> getListStockTransSerialDTOs() {
        return listStockTransSerialDTOs;
    }

    public void setListStockTransSerialDTOs(List<StockTransSerialDTO> listStockTransSerialDTOs) {
        this.listStockTransSerialDTOs = listStockTransSerialDTOs;
    }

    public boolean isRenderPrintExp() {
        return renderPrintExp;
    }

    public void setRenderPrintExp(boolean renderPrintExp) {
        this.renderPrintExp = renderPrintExp;
    }
}
