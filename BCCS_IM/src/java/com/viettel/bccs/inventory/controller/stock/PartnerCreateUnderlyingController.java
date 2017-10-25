package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.util.FileUtil;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.bccs.inventory.tag.SignOfficeTagNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.web.common.security.CustomAuthenticationProvider;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.poi.util.StringUtil;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by thanhnt77
 */
@Component
@Scope("view")
@ManagedBean
public class PartnerCreateUnderlyingController extends TransCommonController {

    private int limitAutoComplete;
    private int stateView;
    private boolean rateMoney;
    private boolean isDisableCreateParner;
    private boolean renderedImport = true;
    private boolean renderedError = false;
    private Long rank = 1L;
    private Long totalSerialSelect = 0L;

    private UploadedFile uploadedFile;
    private RequiredRoleMap requiredRoleMap;
    private ImportPartnerRequestDTO importPartnerRequestDTO = new ImportPartnerRequestDTO();
    private ImportPartnerDetailDTO importPartnerDetailDTO;
    private PartnerShipmentWsDTO shipmentWsDTOSelected;
    private StaffDTO staffDTO;
    private PartnerContractWsDTO parnerContractSearch;
    private ProductOfferingDTO prodOfferDTOSelected = new ProductOfferingDTO();

    private List<String> readerData;
    private List<String> listError;
    private List<PartnerDTO> lstPartnerDTOs;
    private List<ProductOfferingTotalDTO> lstProductOfferingTotalDTO;
    private List<StockTransSerialDTO> lsSerialTmp = Lists.newArrayList();
    private List<OptionSetValueDTO> lstCurrencyType;
    private List<OptionSetValueDTO> lstSimType;
    private List<PartnerContractWsDTO> lsPartnerContractDto;
    private List<PartnerShipmentWsDTO> lsPartnerShipmentWsDTOs;
    private List<OptionSetValueDTO> lstImportType;
    //    private List<ProductOfferTypeDTO> lstProductOfferTypeDTO;
    private List<ReasonDTO> lsReasonDTO;
    private Map<Long, String> mapProductOfferType = Maps.newHashMap();
    private Map<Long, String> mapStateName = Maps.newHashMap();

    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private ShopInfoNameable shopInfoTag;
    @Autowired
    private StaffService staffService;
    @Autowired
    private ReasonService reasonService;
    @Autowired
    private PartnerService partnerService;
    @Autowired
    private ProductOfferTypeService productOfferTypeService;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private SignOfficeTagNameable writeOfficeTag;//khai bao tag ky vOffice
    @Autowired
    private KttsService kttsWsService;
    @Autowired
    private CreateUnderlyingFromPartnerService createUnderlyingFromPartnerService;

    @Secured("@")
    @PostConstruct
    public void init() {
        try {
            List<OptionSetValueDTO> options = DataUtil.defaultIfNull(optionSetValueService.getLsOptionSetValueByCode(Const.OPTION_SET.LOGISTIC_SHOP_ID_LIST), new ArrayList<>());
            shopInfoTag.initShopByListShopCode(this, Const.MODE_SHOP.LIST_SHOP_CODE, options.stream().map(obj -> obj.getValue()).collect(Collectors.toList()));
            shopInfoTag.loadShop(BccsLoginSuccessHandler.getStaffDTO().getShopId().toString(), false);

            List<ProductOfferTypeDTO> lstProductOfferTypeDTO = DataUtil.defaultIfNull(productOfferTypeService.getListProduct(), new ArrayList<>());
            lstProductOfferTypeDTO.forEach(obj -> mapProductOfferType.put(obj.getProductOfferTypeId(), obj.getName()));

            mapStateName = getMapStateName();
            requiredRoleMap = CustomAuthenticationProvider.createRequiredRoleMap(Const.PERMISION.ROLE_SYNC_LOGISTIC);
            limitAutoComplete = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_AUTOCOMPLETE));
            lstPartnerDTOs = partnerService.findPartner(new PartnerDTO());

            lstCurrencyType = optionSetValueService.getByOptionSetCode(Const.OPTION_SET.CURRENCY_TYPE);
            lstSimType = optionSetValueService.getByOptionSetCode(Const.OPTION_SET.EMPTY_SIM_TYPE);
            lsPartnerContractDto = Lists.newArrayList();
            staffDTO = BccsLoginSuccessHandler.getStaffDTO();
            writeOfficeTag.init(this, staffDTO.getShopId());
            lsReasonDTO = reasonService.getLsReasonByType(Const.REASON_TYPE.NHDT);
            doReset();
            initPartner(null);
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
    }

    private Map<Long, String> getMapStateName() {
        Map<Long, String> mapStateName = Maps.newHashMap();
        mapStateName.put(1L, getText("create.request.import.product.status1"));
        mapStateName.put(2L, getText("create.request.import.product.status2"));
        mapStateName.put(3L, getText("create.request.import.product.status3"));
        mapStateName.put(4L, getText("create.request.import.product.status4"));
        mapStateName.put(5L, getText("create.request.import.product.status5"));
        mapStateName.put(6L, getText("create.request.import.product.status6"));
        mapStateName.put(7L, getText("create.request.import.product.status7"));
        mapStateName.put(8L, getText("create.request.import.product.status8"));
        return mapStateName;
    }

    /**
     * ham reset thong thon chi tiet
     *
     * @throws LogicException
     * @throws Exception
     * @author ThanhNT77
     */
    private void initPartner(PartnerShipmentWsDTO shipmentWsDTOSelected) throws LogicException, Exception {
        Date currentDate = getSysdateFromDB();
        importPartnerRequestDTO = new ImportPartnerRequestDTO();
        importPartnerRequestDTO.setToOwnerId(BccsLoginSuccessHandler.getStaffDTO().getShopId());
        importPartnerRequestDTO.setToOwnerType(Const.OWNER_TYPE.SHOP_LONG);
        importPartnerRequestDTO.setCreateDate(currentDate);
        importPartnerRequestDTO.setActionCode(staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_LN, Const.STOCK_TRANS_TYPE.IMPORT, BccsLoginSuccessHandler.getStaffDTO()));
        importPartnerRequestDTO.setSignVoffice(true);
        if (shipmentWsDTOSelected != null) {
            importPartnerRequestDTO.setContractCode(shipmentWsDTOSelected.getContractCode());
            importPartnerRequestDTO.setContractDate(shipmentWsDTOSelected.getCntSignDate());
            importPartnerRequestDTO.setCurrencyType(shipmentWsDTOSelected.getCurrencyCode());
            importPartnerRequestDTO.setPoCode(shipmentWsDTOSelected.getContractCode());
            importPartnerRequestDTO.setPartnerName(shipmentWsDTOSelected.getPartnerName());
            importPartnerRequestDTO.setCodePackage(shipmentWsDTOSelected.getShipmentCode());
            importPartnerRequestDTO.setCodeKCS(shipmentWsDTOSelected.getReportKCSCode());
            importPartnerRequestDTO.setDateKCS(shipmentWsDTOSelected.getReportKCSDate());
            //convert kieu dieu lieu tu mer sang productOffering
            //importPartnerRequestDTO.setLsProductOfferDto(shipmentWsDTOSelected.convertToLsProductOffering());
        }
    }

    /**
     * ham xem chi thiet lenh  kcs
     *
     * @param contractCode
     * @author thanhnt77
     */
    @Secured("@")
    public void showInfoOrderDetail(String contractCode) {
        try {
            lsPartnerShipmentWsDTOs = kttsWsService.searchShipmentBCCS(contractCode);
            if (DataUtil.isNullOrEmpty(lsPartnerShipmentWsDTOs)) {
                reportError("", ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.invoice.invoiceType.nodata");
                return;
            }
            for (PartnerShipmentWsDTO wsDTO : lsPartnerShipmentWsDTOs) {
                wsDTO.setStatusBccsName(getStateName(wsDTO.getStatusBccs()));
            }
            stateView = 1;
            shipmentWsDTOSelected = null;
        } catch (LogicException ex) {
            logger.error(ex);
            reportError("", ex);
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
    }

    public List<PartnerShipmentWsDTO> getLsSortPartnerShipment() {
        if (!DataUtil.isNullOrEmpty(this.lsPartnerShipmentWsDTOs)) {
            Collections.sort(lsPartnerShipmentWsDTOs, new Comparator<PartnerShipmentWsDTO>() {
                @Override
                public int compare(PartnerShipmentWsDTO o1, PartnerShipmentWsDTO o2) {
                    if (DataUtil.isNullOrEmpty(o1.getShipmentCode()) || DataUtil.isNullOrEmpty(o2.getShipmentCode())) {
                        return -1;
                    }
                    int result = o1.getShipmentCode().compareTo(o2.getShipmentCode());
                    if (result == 0) {
                        if (DataUtil.isNullOrEmpty(o1.getReportKCSCode()) || DataUtil.isNullOrEmpty(o2.getReportKCSCode())) {
                            return -1;
                        }
                        result = o1.getReportKCSCode().compareTo(o2.getReportKCSCode());
                    }
                    return result;
                }
            });
        }
        return lsPartnerShipmentWsDTOs;
    }

    /**
     * ham hien thi ten trang thai kcs
     *
     * @param statusBccs
     * @return
     * @author thanhnt77
     */
    private String getStateName(String statusBccs) {
        String stateName = "";
        if (Const.STATUS_KCS.STATUS_ORDER.equals(statusBccs)) {
            stateName = getText("create.order.file.create.order");
        } else if (Const.STATUS_KCS.STATUS_NOTE.equals(statusBccs)) {
            stateName = getText("create.note");
        } else if (Const.STATUS_KCS.STATUS_IMPORT.equals(statusBccs)) {
            stateName = getText("mn.stock.above.ImportStock");
        }
        return stateName;
    }

    /**
     * ham xu ly nhap so luong kcs
     *
     * @param index
     * @author thanhnt77
     */
    public void doChangeQuantity(int index) {
        try {
            if (importPartnerRequestDTO.getLsProductOfferDto() != null && importPartnerRequestDTO.getLsProductOfferDto().size() > index) {
                ProductOfferingDTO offeringDTO = importPartnerRequestDTO.getLsProductOfferDto().get(index);
                Long quantity = DataUtil.safeToLong(offeringDTO.getQuantity()) - DataUtil.safeToLong(offeringDTO.getStrQuantityKcs());
                offeringDTO.setAvailableQuantity(quantity);
                if (quantity.compareTo(0L) <= 0) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "partner.create.underlying.report.ktts.validate.quantity.kcs", offeringDTO.getName());
                }
            }
        } catch (LogicException ex) {
            logger.error(ex);
            reportError("", ex);
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
    }

    /**
     * @param index
     * @author thanhnt
     */
    public void doRemoveItem(int index) {
        if (!DataUtil.isNullOrEmpty(lsSerialTmp) && lsSerialTmp.size() > index) {
            lsSerialTmp.remove(index);
            totalSerialSelect = getTotalSerialView(lsSerialTmp);
        }
    }

    /**
     * @param index
     * @author thanhnt
     */
    public void doRemoveItemFile(int index) {
        if (!DataUtil.isNullOrEmpty(readerData) && readerData.size() > index) {
            readerData.remove(index);
        }
    }


    /**
     * ham xem chi tiet lenh
     *
     * @param shipmentCode
     * @param reportKCSStatus
     * @author thanhnt77
     */
    @Secured("@")
    public void showInfoNextDetail(String shipmentCode, String reportKCSStatus) {
        try {
            if (DataUtil.isNullOrEmpty(shipmentCode)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "partner.create.underlying.shipmentCode.empty");
            }
            if (DataUtil.isNullOrEmpty(reportKCSStatus)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "partner.create.underlying.reportKct.status.empty");
            }

            if (DataUtil.isNullOrEmpty(lsPartnerShipmentWsDTOs)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.invoice.invoiceType.nodata");
            }
            shipmentWsDTOSelected = null;
            for (PartnerShipmentWsDTO shipmentWsDTO : lsPartnerShipmentWsDTOs) {
                if (shipmentCode.equals(shipmentWsDTO.getShipmentCode()) && reportKCSStatus.equals(shipmentWsDTO.getReportKCSStatus())) {
                    shipmentWsDTOSelected = DataUtil.cloneBean(shipmentWsDTO);
                    initPartner(shipmentWsDTOSelected);
                    break;
                }
            }

            if (shipmentWsDTOSelected == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.invoice.invoiceType.nodata");
            }
            if (!"1".equals(shipmentWsDTOSelected.getReportKCSStatus())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "partner.create.underlying.show.state.bbkt.error");
            }
            List<ProductOfferingDTO> lsProdOffer = convertProductOfferingFromListMerDto(shipmentWsDTOSelected.getLsMerEntityBo());
            if (DataUtil.isNullOrEmpty(lsProdOffer)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "partner.create.underlying.list.offer");
            }
            importPartnerRequestDTO.setLsProductOfferDto(lsProdOffer);
            importPartnerRequestDTO.setLogistic(false);
            stateView = 2;
            isDisableCreateParner = false;
        } catch (LogicException ex) {
            logger.error(ex);
            reportError("", ex);
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
    }

    /**
     * ham xu ly chuyen doi kieu du lieu mat hang cua ktts sang producoffer
     *
     * @param lsMerEntity
     * @return
     * @throws LogicException
     * @throws Exception
     * @author thanhnt77
     */
    private List<ProductOfferingDTO> convertProductOfferingFromListMerDto(List<PartnerShipmentMerEntityWsDTO> lsMerEntity) throws LogicException, Exception {
        prodOfferDTOSelected = new ProductOfferingDTO();
        if (lsMerEntity != null) {
            List<ProductOfferingDTO> lsResult = Lists.newArrayList();
            List<ProductOfferingDTO> lsResultSim = DataUtil.defaultIfNull(productOfferingService.getLsProductOfferDtoByTypeId(Const.PRODUCT_OFFER_TYPE.EMPTY_SIM), new ArrayList<>());

            for (PartnerShipmentMerEntityWsDTO dto : lsMerEntity) {
                ProductOfferingDTO offeringDTO = productOfferingService.findByProductOfferCode(dto.getMerCode(), Const.STATUS_ACTIVE);
                if (offeringDTO != null) {
                    offeringDTO.setTelecomServiceId(offeringDTO.getTelecomServiceId());
                    offeringDTO.setPrice(DataUtil.safeToDouble(dto.getUnitPrice()).longValue());
                    offeringDTO.setQuantity(DataUtil.safeToDouble(dto.getCount()).longValue());
                    offeringDTO.setProductOfferTypeIdName(mapProductOfferType.get(offeringDTO.getProductOfferTypeId()));
                    offeringDTO.setStateId(Const.GOODS_STATE.NEW);
                    offeringDTO.setStateIdName(mapStateName.get(offeringDTO.getStateId()));
                    offeringDTO.setImpType(DataUtil.safeToString(Const.IMP_TYPE.IMP_BY_FILE));
                    if (Const.PRODUCT_OFFER_TYPE.KIT.equals(offeringDTO.getProductOfferTypeId())) {
                        offeringDTO.setLsProdOfferSim(lsResultSim);
                        offeringDTO.setProdOfferSimId(!DataUtil.isNullOrEmpty(lsResultSim) ? lsResultSim.get(0).getProdOfferSimId() : null);
                    }
                    if (Const.PRODUCT_OFFER_TYPE.PRODUCT_NO_SERIAL.equals(offeringDTO.getProductOfferTypeId())) {
                        offeringDTO.setImpType(DataUtil.safeToString(Const.IMP_TYPE.IMP_BY_QUANTITY));
                    }
                    lsResult.add(offeringDTO);
                }
            }
            //thanhnt code tam
            /*ProductOfferingDTO offeringDTO = productOfferingService.findOne(236L);
            if (offeringDTO != null) {
                offeringDTO.setTelecomServiceId(offeringDTO.getTelecomServiceId());
                offeringDTO.setPrice(200L);
                offeringDTO.setQuantity(4L);
                offeringDTO.setProductOfferTypeIdName(mapProductOfferType.get(offeringDTO.getProductOfferTypeId()));
                offeringDTO.setStateId(Const.GOODS_STATE.NEW);
                offeringDTO.setStateIdName(mapStateName.get(offeringDTO.getStateId()));
                offeringDTO.setImpType(DataUtil.safeToString(Const.IMP_TYPE.IMP_BY_FILE));
                if (Const.PRODUCT_OFFER_TYPE.KIT.equals(offeringDTO.getProductOfferTypeId())) {
                    offeringDTO.setLsProdOfferSim(lsResultSim);
                }
                if (Const.PRODUCT_OFFER_TYPE.PRODUCT_NO_SERIAL.equals(offeringDTO.getProductOfferTypeId()) ) {
                    offeringDTO.setImpType(DataUtil.safeToString(Const.IMP_TYPE.IMP_BY_QUANTITY));
                }
                lsResult.add(offeringDTO);
            }*/
            /*offeringDTO = productOfferingService.findOne(637L);//22105
            if (offeringDTO != null) {
                offeringDTO.setTelecomServiceId(offeringDTO.getTelecomServiceId());
                offeringDTO.setPrice(200L);
                offeringDTO.setQuantity(4L);
                offeringDTO.setProductOfferTypeIdName(mapProductOfferType.get(offeringDTO.getProductOfferTypeId()));
                offeringDTO.setStateId(Const.GOODS_STATE.NEW);
                offeringDTO.setStateIdName(mapStateName.get(offeringDTO.getStateId()));
                offeringDTO.setImpType(DataUtil.safeToString(Const.IMP_TYPE.IMP_BY_FILE));
                if (Const.PRODUCT_OFFER_TYPE.KIT.equals(offeringDTO.getProductOfferTypeId())) {
                    offeringDTO.setLsProdOfferSim(lsResultSim);
                }
                if (Const.PRODUCT_OFFER_TYPE.PRODUCT_NO_SERIAL.equals(offeringDTO.getProductOfferTypeId()) ) {
                    offeringDTO.setImpType(DataUtil.safeToString(Const.IMP_TYPE.IMP_BY_QUANTITY));
                }
                lsResult.add(offeringDTO);
            }*/
            /*ProductOfferingDTO offeringDTO = productOfferingService.findOne(22105L);//22105
            if (offeringDTO != null) {
                offeringDTO.setTelecomServiceId(offeringDTO.getTelecomServiceId());
                offeringDTO.setPrice(303L);
                offeringDTO.setQuantity(4L);
                offeringDTO.setProductOfferTypeIdName(mapProductOfferType.get(offeringDTO.getProductOfferTypeId()));
                offeringDTO.setStateId(Const.GOODS_STATE.NEW);
                offeringDTO.setStateIdName(mapStateName.get(offeringDTO.getStateId()));
                offeringDTO.setImpType(DataUtil.safeToString(Const.IMP_TYPE.IMP_BY_FILE));
                if (Const.PRODUCT_OFFER_TYPE.KIT.equals(offeringDTO.getProductOfferTypeId())) {
                    offeringDTO.setLsProdOfferSim(lsResultSim);
                }
                if (Const.PRODUCT_OFFER_TYPE.PRODUCT_NO_SERIAL.equals(offeringDTO.getProductOfferTypeId()) ) {
                    offeringDTO.setImpType(DataUtil.safeToString(Const.IMP_TYPE.IMP_BY_QUANTITY));
                }
                lsResult.add(offeringDTO);
            }*/

            return lsResult;
        }
        return Lists.newArrayList();
    }

    /**
     * ham xu ly validate nhap kho tu doi tac
     *
     * @author thanhnt77
     */
    @Secured("@")
    public void doValidateCreateNote() {

    }

    /**
     * ham lap lenh nhap kho tu doi tac
     *
     * @author thanhtnt77
     */
    @Secured("@")
    public void doCreateUnderlyingPartner() {
        try {
            for (ProductOfferingDTO offeringDTO : importPartnerRequestDTO.getLsProductOfferDto()) {
                if (DataUtil.isNullOrEmpty(offeringDTO.getStrQuantityKcs())) {
                    offeringDTO.setAvailableQuantity(offeringDTO.getQuantity());
                }
                if (DataUtil.safeToLong(offeringDTO.getAvailableQuantity()).compareTo(0L) <= 0) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "partner.create.underlying.report.ktts.validate.quantity.kcs", offeringDTO.getName());
                }
                if (!importPartnerRequestDTO.isLogistic() && offeringDTO.getShowInputSerial()) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "partner.create.underlying.report.ktts.validate.import.serial.empty", offeringDTO.getName());
                }
            }
            importPartnerRequestDTO.setSignOfficeDTO(null);
            if (importPartnerRequestDTO.isSignVoffice()) {
                SignOfficeDTO signOfficeDTO = writeOfficeTag.validateVofficeAccount();
                importPartnerRequestDTO.setSignOfficeDTO(signOfficeDTO);
            }
            importPartnerRequestDTO.setImportStaffCode(staffDTO.getStaffCode());
            importPartnerRequestDTO.setImportStaffId(staffDTO.getStaffId());
            createUnderlyingFromPartnerService.executeStockTransForPartner(importPartnerRequestDTO, requiredRoleMap);
            isDisableCreateParner = true;
            reportSuccess("", "partner.create.underlying.header.msg.sucess");
        } catch (LogicException ex) {
            logger.error(ex);
            reportError("", ex);
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
    }

    /**
     * ham back tro ve man hinh list
     *
     * @author thanhtnt77
     */
    @Secured("@")
    public void doBackPageTop() {
        stateView = 0;
        doSearchPartnerContract();
    }

    /**
     * ham back tro ve man hinh list
     *
     * @author thanhtnt77
     */
    @Secured("@")
    public void doBackPageTwo() {
        stateView = 1;
    }

    @Secured("@")
    public void doReset() {
        try {
            isDisableCreateParner = false;
            rateMoney = false;
            parnerContractSearch = new PartnerContractWsDTO(DateUtil.sysDate(), DateUtil.sysDate(), null);
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
    }

    @Secured("@")
    public void doSearchPartnerContract() {
        try {
            if (!DataUtil.isNullOrEmpty(parnerContractSearch.getContractCode()) && !DataUtil.validateStringByPattern(parnerContractSearch.getContractCode().trim(), Const.REGEX.CODE_REGEX)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.import.contracCode.error.format.msg");
            }
            validateDate(parnerContractSearch.getFromDate(), parnerContractSearch.getToDate());
            lsPartnerContractDto = DataUtil.defaultIfNull(kttsWsService.searchContractBCCS(DateUtil.date2ddMMyyyyString(parnerContractSearch.getFromDate()),
                    DateUtil.date2ddMMyyyyString(parnerContractSearch.getToDate()), parnerContractSearch.getContractCode()), new ArrayList<>());
        } catch (LogicException ex) {
            logger.error(ex);
            reportError("", ex);
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
    }

    @Secured("@")
    public void receiveShop(VShopStaffDTO vShopStaffDTO) {
        importPartnerRequestDTO.setToOwnerId(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
    }

    @Secured("@")
    public void clearShop() {
        importPartnerRequestDTO.setToOwnerId(null);
    }

    @Secured("@")
    public void doCheckSerial() {
        try {
            if (Const.LongSimpleItem.importPartnerInputByFile.getValue().equals(DataUtil.safeToLong(prodOfferDTOSelected.getImpType()))) {
                if (uploadedFile == null) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.check.report.file.not.exsit");
                }
                readSerialFromFile(uploadedFile.getContents());
            } else {
                renderedImport = true;

                StockTransSerialDTO stockSerialInput = prodOfferDTOSelected.getStockTransSerialDTO();
                String fromSerialStr = stockSerialInput.getFromSerial();
                String toSerialStr = stockSerialInput.getToSerial();

                if (!DataUtil.isNullOrEmpty(stockSerialInput.getFromSerial())
                        && !DataUtil.isNullOrEmpty(stockSerialInput.getToSerial()) && doValidateInputSerial()) {
                    List<StockTransSerialDTO> lsSerialCheck = Lists.newArrayList();
                    //check trung lap serial voi mat hang handset
                    if (Const.STOCK_TYPE.STOCK_HANDSET.equals(prodOfferDTOSelected.getProductOfferTypeId())) {
                        for (StockTransSerialDTO serialDetail : lsSerialTmp) {
                            if (stockSerialInput.getFromSerial().equalsIgnoreCase(serialDetail.getFromSerial())
                                    && stockSerialInput.getToSerial().equalsIgnoreCase(serialDetail.getToSerial())) {
                                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.partner.range.duplidate",
                                        stockSerialInput.getFromSerial(), stockSerialInput.getToSerial());
                            }
                        }
                    } else { //check trung lap serial voi mat hang khac
                        if (!DataUtil.validateStringByPattern(fromSerialStr, Const.REGEX.NUMBER_REGEX) || !DataUtil.validateStringByPattern(toSerialStr, Const.REGEX.NUMBER_REGEX)) {
                            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "mn.stock.partner.serial.valid.format", prodOfferDTOSelected.getName());
                        }
                        BigInteger fromSerialNum = new BigInteger(fromSerialStr);
                        BigInteger toSerialNum = new BigInteger(toSerialStr);
                        //neu serial ket thuc nho hon serial bat dau or serial ket thuc lon hon 500000 thi bao loi
                        if (fromSerialNum.compareTo(toSerialNum) > 0) {
                            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "mm.divide.upload.valid.serial.length");
                        }
                        for (StockTransSerialDTO serialDetail : lsSerialTmp) {
                            BigInteger fromSerialCurrent = new BigInteger(serialDetail.getFromSerial());
                            BigInteger toSerialCurrent = new BigInteger(serialDetail.getToSerial());
                            if (fromSerialNum.compareTo(fromSerialCurrent) >= 0 && fromSerialNum.compareTo(toSerialCurrent) <= 0
                                    || toSerialNum.compareTo(fromSerialCurrent) >= 0 && toSerialNum.compareTo(toSerialCurrent) <= 0) {
                                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.partner.range.duplidate", fromSerialStr, toSerialStr);
                            }
                        }

                        BigInteger total;
                        if (rank == 1) {
                            total = toSerialNum.add(fromSerialNum.negate()).add(new BigInteger("1"));
                            //add to list serial
                            StockTransSerialDTO serialDTO = new StockTransSerialDTO();
                            serialDTO.setFromSerial(stockSerialInput.getFromSerial());
                            serialDTO.setToSerial(stockSerialInput.getToSerial());
                            serialDTO.setQuantity(total.longValue());
                            lsSerialCheck.add(serialDTO);
                        } else {
                            lsSerialCheck = attachSerialRange(fromSerialNum, toSerialNum);
                        }
                    }
                    lsSerialTmp.addAll(lsSerialCheck);
                }
            }
            totalSerialSelect = getTotalSerialView(lsSerialTmp);
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            topReportError("", ex);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            topReportError("", "common.error.happened", ex);
        }
    }

    public Long getTotalSerialView(List<StockTransSerialDTO> lsSerial) {
        Long total = 0L;
        if (lsSerial != null) {
            for (StockTransSerialDTO serial : lsSerial) {
                total += DataUtil.safeToLong(serial.getQuantity());
            }
        }
        return total;
    }

    @Secured("@")
    public void doImport() {
        try {
            if (Const.LongSimpleItem.importPartnerInputByFile.getValue().equals(DataUtil.safeToLong(prodOfferDTOSelected.getImpType()))) {
                if (DataUtil.safeToLong(prodOfferDTOSelected.getAvailableQuantity()).intValue() != readerData.size()) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "mn.stock.partner.range.serial.limit", prodOfferDTOSelected.getAvailableQuantity());
                }
                prodOfferDTOSelected.setLstParam(readerData);
                return;
            }
            if (totalSerialSelect.compareTo(DataUtil.safeToLong(prodOfferDTOSelected.getAvailableQuantity())) != 0) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "mn.stock.partner.range.serial.limit", prodOfferDTOSelected.getAvailableQuantity());
            }
            prodOfferDTOSelected.setListStockTransSerialDTOs(lsSerialTmp);
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    @Secured("@")
    public void doResetDlg() {
        resetDlgSerial();
    }

    public Long getTotalSerialSelect() {
        return this.totalSerialSelect;
    }

    private List<StockTransSerialDTO> attachSerialRange(BigInteger fromSerial, BigInteger toSerial) throws LogicException, Exception {
        BigInteger bRange = new BigInteger(rank.toString());
        BigInteger range = bRange.add(new BigInteger("-1"));
        BigInteger maxTo = new BigInteger("1");
        List<StockTransSerialDTO> lsSerialDto = Lists.newArrayList();
        while (maxTo.compareTo(toSerial) < 0) {
            BigInteger from = fromSerial;
            BigInteger to = from.add(range);
            if (to.compareTo(toSerial) > 0) {
                to = toSerial;
            }
            if (from.compareTo(to) > 0) {
                break;
            }
            fromSerial = fromSerial.add(bRange).add(bRange);

            StockTransSerialDTO serialDTO = new StockTransSerialDTO();
            serialDTO.setFromSerial(from.toString());
            serialDTO.setToSerial(to.toString());
            maxTo = new BigInteger(to.toString());
            serialDTO.setQuantity(to.add(from.negate()).longValue() + 1L);
            lsSerialDto.add(serialDTO);
        }
        return lsSerialDto;
    }

    @Secured("@")
    public boolean doValidateInputSerial() {
        try {
            StockTransSerialDTO stockTransSerialDTO = prodOfferDTOSelected.getStockTransSerialDTO();
            if (prodOfferDTOSelected.getStockTransSerialDTO() == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "stock.validate.inspect.fromSerial.toSerial.not.empty");
            }

            if (stockTransSerialDTO.getFromSerial() == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "stock.validate.inspect.fromSerial.toSerial.not.empty");
            }
            if (stockTransSerialDTO.getFromSerial() == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "stock.validate.inspect.fromSerial.toSerial.not.empty");
            }

            String regex = "^[0-9]+";
            if (prodOfferDTOSelected.getProductOfferTypeId().equals(Const.STOCK_TYPE.STOCK_HANDSET)) {
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
                        || !stockTransSerialDTO.getFromSerial().matches(Const.REGEX.NUMBER_REGEX)) {
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

    /**
     * ham xu ly lua chon file
     *
     * @param event
     * @author thanhnt77
     */
    @Secured("@")
    public void handlFileUpload(FileUploadEvent event) {
        if (event != null) {
            try {
                resetDlgSerial();
                uploadedFile = event.getFile();
                BaseMessage message = validateFileUploadWhiteList(uploadedFile, ALOW_EXTENSION_TEXT_TYPE_LIST, MAX_SIZE_2M);
                if (!message.isSuccess()) {
                    uploadedFile = null;
                    LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                    ex.setDescription(message.getDescription());
                    throw ex;
                }

            } catch (LogicException ex) {
                topReportError("", ex);
            } catch (Exception ex) {
                topReportError("", "common.error.happened", ex);
            }
        }
    }

    private void readSerialFromFile(byte[] content) throws LogicException, Exception {
        InputStream stream = new ByteArrayInputStream(content);
        listError = Lists.newArrayList();
        if (prodOfferDTOSelected == null || DataUtil.isNullOrEmpty(prodOfferDTOSelected.getListProfile())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "import.partner.valid.profile");
        }
        prodOfferDTOSelected.setLstParam(new ArrayList<>());
        readerData = Lists.newArrayList();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String lines;
        boolean error = false;
        String regex = Const.REGEX.SERIAL_REGEX;
        while ((lines = reader.readLine()) != null) {
            if (DataUtil.isNullOrEmpty(lines)) {
                continue;
            }
            Long countSerial = 0L;
            String[] splits = lines.split(Const.AppProperties4Sms.profileSparator.getValue().toString());
            if (DataUtil.isNullOrEmpty(splits)) {
                continue;
            }
            if (splits.length != prodOfferDTOSelected.getListProfile().size()) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "import.partner.data.notmatch");
            }
            for (String serial : splits) {
                if (DataUtil.isNullOrEmpty(serial)) {
                    continue;
                }
                if (!serial.trim().matches(regex)) {
                    error = true;
                    lines += Const.AppProperties4Sms.profileSparator.getValue().toString() + " ";
                    lines += getMessage("import.partner.valid.char");
                } else {
                    countSerial++;
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
            readerData.stream().forEach(obj -> listError.add(obj += "\n"));
            readerData = Lists.newArrayList();
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "import.partner.valid.fileContent");
        }
        if (DataUtil.safeToLong(prodOfferDTOSelected.getAvailableQuantity()).intValue() != readerData.size()) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "mn.stock.partner.range.serial.limit", prodOfferDTOSelected.getAvailableQuantity());
        }
    }

    @Secured("@")
    public void doOpenDlgInputSerial(int index) {
        try {
            prodOfferDTOSelected = importPartnerRequestDTO.getLsProductOfferDto().get(index);
            Long quantity = DataUtil.safeToLong(prodOfferDTOSelected.getQuantity()) - DataUtil.safeToLong(prodOfferDTOSelected.getStrQuantityKcs());
            prodOfferDTOSelected.setAvailableQuantity(quantity);
            if (prodOfferDTOSelected.getAvailableQuantity() != null && DataUtil.safeToLong(prodOfferDTOSelected.getAvailableQuantity()).compareTo(0L) <= 0) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "mn.stock.partner.range.serial.limit", prodOfferDTOSelected.getAvailableQuantity());
            }

            prodOfferDTOSelected.setImpType(DataUtil.safeToString(Const.LongSimpleItem.importPartnerInputByFile.getValue()));
            resetDlgSerial();

            ProfileDTO profileDTO = productOfferingService.getProfileByProductId(prodOfferDTOSelected.getProductOfferingId());

            if (profileDTO == null || DataUtil.isNullOrEmpty(profileDTO.getLstColumnName())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "import.partner.valid.importByFile");
            }
            //lay danh loai import
            lstImportType = DataUtil.defaultIfNull(optionSetValueService.getByOptionSetCode(Const.OPTION_SET.IMPORT_TYPE_PARTNER), new ArrayList<>());
            Collections.sort(lstImportType, new Comparator<OptionSetValueDTO>() {
                @Override
                public int compare(OptionSetValueDTO o1, OptionSetValueDTO o2) {
                    return o1.getValue().compareTo(o2.getValue());
                }
            });
            //xoa loai import nhap theo so luong di
            lstImportType.remove(lstImportType.size() - 1);
            //neu do dai cua mau profile lon hon 1 thi xoa loai import theo dai so
            if (profileDTO.getLstColumnName().size() > 1) {
                lstImportType.remove(lstImportType.size() - 1);
            }
            prodOfferDTOSelected.createProfile(profileDTO.getLstColumnName());
            listError = Lists.newArrayList();
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    /**
     * @author thanhnt77
     */
    @Secured("@")
    public void doViewSeial(int index) {
        prodOfferDTOSelected = importPartnerRequestDTO.getLsProductOfferDto().get(index);
    }

    /**
     * check cho truong hop nhap theo dai hoac theo file
     *
     * @return
     */
    @Secured("@")
    public void doChangeImportType() {
        try {
            resetDlgSerial();
            if (DataUtil.safeEqual(prodOfferDTOSelected.getImpType(), Const.EXPORT_PARTNER.EXP_BY_SERIAL_RANGE)) {
                renderedImport = false;
                if (DataUtil.isNullOrEmpty(prodOfferDTOSelected.getListProfile()) || prodOfferDTOSelected.getListProfile().size() > 1) {
                    prodOfferDTOSelected.setImpType(DataUtil.safeToString(Const.LongSimpleItem.importPartnerInputByFile.getValue()));
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "import.partner.valid.importByRange");
                }
            }
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    private void resetDlgSerial() {
        renderedImport = true;
        uploadedFile = null;
        renderedError = false;
        rank = 1L;
        totalSerialSelect = 0L;
        readerData = Lists.newArrayList();
        lsSerialTmp = Lists.newArrayList();
        prodOfferDTOSelected.setStockTransSerialDTO(new StockTransSerialDTO());
        prodOfferDTOSelected.setListStockTransSerialDTOs(new ArrayList<>());
        prodOfferDTOSelected.setLstParam(new ArrayList<>());
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

    public boolean isShowDownloadFileError() {
        return !DataUtil.isNullOrEmpty(listError);
    }

    //getter and setter

    public int getLimitAutoComplete() {
        return limitAutoComplete;
    }

    public void setLimitAutoComplete(int limitAutoComplete) {
        this.limitAutoComplete = limitAutoComplete;
    }

    public boolean isRateMoney() {
        return rateMoney;
    }

    public void setRateMoney(boolean rateMoney) {
        this.rateMoney = rateMoney;
    }

    public RequiredRoleMap getRequiredRoleMap() {
        return requiredRoleMap;
    }

    public void setRequiredRoleMap(RequiredRoleMap requiredRoleMap) {
        this.requiredRoleMap = requiredRoleMap;
    }

    public ImportPartnerRequestDTO getImportPartnerRequestDTO() {
        return importPartnerRequestDTO;
    }

    public void setImportPartnerRequestDTO(ImportPartnerRequestDTO importPartnerRequestDTO) {
        this.importPartnerRequestDTO = importPartnerRequestDTO;
    }

    public ImportPartnerDetailDTO getImportPartnerDetailDTO() {
        return importPartnerDetailDTO;
    }

    public void setImportPartnerDetailDTO(ImportPartnerDetailDTO importPartnerDetailDTO) {
        this.importPartnerDetailDTO = importPartnerDetailDTO;
    }

    public List<PartnerDTO> getLstPartnerDTOs() {
        return lstPartnerDTOs;
    }

    public void setLstPartnerDTOs(List<PartnerDTO> lstPartnerDTOs) {
        this.lstPartnerDTOs = lstPartnerDTOs;
    }

    public List<ProductOfferingTotalDTO> getLstProductOfferingTotalDTO() {
        return lstProductOfferingTotalDTO;
    }

    public void setLstProductOfferingTotalDTO(List<ProductOfferingTotalDTO> lstProductOfferingTotalDTO) {
        this.lstProductOfferingTotalDTO = lstProductOfferingTotalDTO;
    }

    public List<OptionSetValueDTO> getLstCurrencyType() {
        return lstCurrencyType;
    }

    public void setLstCurrencyType(List<OptionSetValueDTO> lstCurrencyType) {
        this.lstCurrencyType = lstCurrencyType;
    }

    public List<OptionSetValueDTO> getLstSimType() {
        return lstSimType;
    }

    public void setLstSimType(List<OptionSetValueDTO> lstSimType) {
        this.lstSimType = lstSimType;
    }

    public List<PartnerContractWsDTO> getLsPartnerContractDto() {
        return lsPartnerContractDto;
    }

    public void setLsPartnerContractDto(List<PartnerContractWsDTO> lsPartnerContractDto) {
        this.lsPartnerContractDto = lsPartnerContractDto;
    }

    public PartnerContractWsDTO getParnerContractSearch() {
        return parnerContractSearch;
    }

    public void setParnerContractSearch(PartnerContractWsDTO parnerContractSearch) {
        this.parnerContractSearch = parnerContractSearch;
    }

    public ShopInfoNameable getShopInfoTag() {
        return shopInfoTag;
    }

    public void setShopInfoTag(ShopInfoNameable shopInfoTag) {
        this.shopInfoTag = shopInfoTag;
    }

    public List<PartnerShipmentWsDTO> getLsPartnerShipmentWsDTOs() {
        return lsPartnerShipmentWsDTOs;
    }

    public void setLsPartnerShipmentWsDTOs(List<PartnerShipmentWsDTO> lsPartnerShipmentWsDTOs) {
        this.lsPartnerShipmentWsDTOs = lsPartnerShipmentWsDTOs;
    }

    public SignOfficeTagNameable getWriteOfficeTag() {
        return writeOfficeTag;
    }

    public void setWriteOfficeTag(SignOfficeTagNameable writeOfficeTag) {
        this.writeOfficeTag = writeOfficeTag;
    }

    public PartnerShipmentWsDTO getShipmentWsDTOSelected() {
        return shipmentWsDTOSelected;
    }

    public void setShipmentWsDTOSelected(PartnerShipmentWsDTO shipmentWsDTOSelected) {
        this.shipmentWsDTOSelected = shipmentWsDTOSelected;
    }

    public int getStateView() {
        return stateView;
    }

    public void setStateView(int stateView) {
        this.stateView = stateView;
    }

    public List<ReasonDTO> getLsReasonDTO() {
        return lsReasonDTO;
    }

    public void setLsReasonDTO(List<ReasonDTO> lsReasonDTO) {
        this.lsReasonDTO = lsReasonDTO;
    }

    public boolean isDisableCreateParner() {
        return isDisableCreateParner;
    }

    public void setDisableCreateParner(boolean isDisableCreateParner) {
        this.isDisableCreateParner = isDisableCreateParner;
    }

    public List<OptionSetValueDTO> getLstImportType() {
        return lstImportType;
    }

    public void setLstImportType(List<OptionSetValueDTO> lstImportType) {
        this.lstImportType = lstImportType;
    }

    public ProductOfferingDTO getProdOfferDTOSelected() {
        return prodOfferDTOSelected;
    }

    public void setProdOfferDTOSelected(ProductOfferingDTO prodOfferDTOSelected) {
        this.prodOfferDTOSelected = prodOfferDTOSelected;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public Long getRank() {
        return rank;
    }

    public void setRank(Long rank) {
        this.rank = rank;
    }

    public boolean isRenderedError() {
        return renderedError;
    }

    public void setRenderedError(boolean renderedError) {
        this.renderedError = renderedError;
    }

    public boolean isRenderedImport() {
        return renderedImport;
    }

    public void setRenderedImport(boolean renderedImport) {
        this.renderedImport = renderedImport;
    }

    public List<String> getListError() {
        return listError;
    }

    public void setListError(List<String> listError) {
        this.listError = listError;
    }

    public List<StockTransSerialDTO> getLsSerialTmp() {
        return lsSerialTmp;
    }

    public void setLsSerialTmp(List<StockTransSerialDTO> lsSerialTmp) {
        this.lsSerialTmp = lsSerialTmp;
    }

    public List<String> getReaderData() {
        return readerData;
    }

    public void setReaderData(List<String> readerData) {
        this.readerData = readerData;
    }
}
