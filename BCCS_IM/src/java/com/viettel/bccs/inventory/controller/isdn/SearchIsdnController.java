package com.viettel.bccs.inventory.controller.isdn;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.InventoryController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.ExcellUtil;
import com.viettel.bccs.inventory.common.model.FileExportBean;
import com.viettel.bccs.inventory.common.util.FileUtil;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockNumber;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.inventory.service.common.IMServiceUtil;
import com.viettel.bccs.inventory.tag.ProductInfoNameable;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.bccs.inventory.tag.StaffInfoNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.*;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.passport.CustomConnector;
import com.viettel.web.common.security.CustomAuthenticationProvider;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;
import viettel.passport.client.ObjectToken;
import viettel.passport.client.UserToken;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.text.Collator;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by sinhhv on 2/4/2016.
 */
@Component
@Scope("view")
@ManagedBean
public class SearchIsdnController extends InventoryController {
    @Autowired
    private ShopInfoNameable shopInfoTag;
    @Autowired
    private StaffInfoNameable staffInfoTag;
    @Autowired
    private StaffInfoNameable staffInfoTagLock;
    @Autowired
    private ProductInfoNameable productInfoTag;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private GroupFilterRuleService groupFilterRuleService;
    @Autowired
    private NumberFilterRuleService numberFilterRuleService;
    @Autowired
    private StockNumberService stockNumberService;
    @Autowired
    private ProductOfferPriceService productOfferPriceService;
    @Autowired
    private StockIsdnTransService stockIsdnTransService;
    @Autowired
    private StaffService staffService;

    private InfoSearchIsdnDTO infoSearch = new InfoSearchIsdnDTO();
    private InfoSearchIsdnDTO infoSearchExport = new InfoSearchIsdnDTO();
    private List<OptionSetValueDTO> listTelecomService = Lists.newArrayList();
    private List<OptionSetValueDTO> listTypeIsdn = Lists.newArrayList();
    private List<OptionSetValueDTO> listStatus = Lists.newArrayList();
    private List<GroupFilterRuleDTO> listFilterRule = Lists.newArrayList();
    private List<GroupFilterRuleDTO> listGroupFilterRule = Lists.newArrayList();
    private List<NumberFilterRuleDTO> listRuleNiceDigit = Lists.newArrayList();
    private List<IsdnViewDetailDTO> listIsdn = Lists.newArrayList();
    private List<ProductOfferPriceDTO> listIsdnCommitment = Lists.newArrayList();
    private List<StockIsdnTransDTO> listIsdnHistory = Lists.newArrayList();
    private Map<String, String> mapStatusName;

    private UploadedFile uploadedFile;
    private StaffDTO currentStaffDto;
    private StaffDTO staffLock = new StaffDTO();
    private RequiredRoleMap requiredRoleMap;
    private IsdnViewDetailDTO currentViewHistory;
    private ManageTransStockDto transStock;
    private byte[] contentByte;
    private boolean checkCapcha = false;
    private boolean lookUpStockExt = false;
    private boolean enableCheckBoxViewSpecialIsdn = false;
    private String isdn;
    private int index;

    private static final String FILE_TEMPLATE_PATH = "exportIsdn.xls";

    private boolean directLink;

    @PostConstruct
    public void init() {
        try {
            helperSystem();
            currentStaffDto = BccsLoginSuccessHandler.getStaffDTO();
            requiredRoleMap = CustomAuthenticationProvider.createRequiredRoleMap(Const.PERMISION.ROLE_LOOK_UP_SPECIAL_STOCK, Const.PERMISION.BCCS2_IM_QLSO_TRACUUSO_GIUSO);
            enableCheckBoxViewSpecialIsdn = !CustomAuthenticationProvider.hasRole(Const.PERMISION.ROLE_LOOK_UP_SPECIAL_STOCK);
            lookUpStockExt = CustomAuthenticationProvider.hasRole(Const.PERMISION.ROLE_LOOK_UP_STOCK_EXT);
            shopInfoTag.initShopMangeIsdnTrans(this, currentStaffDto.getShopId().toString(), "8", null, Const.MODE_SHOP.ISDN_MNGT);
            staffInfoTag.initStaffManageIsdn(this, currentStaffDto.getShopId().toString(), "8", Const.MODE_SHOP.ISDN_MNGT, null);

            staffInfoTagLock.initStaff(this, null);

            ConfigProductTagDTO config = new ConfigProductTagDTO();
            ProductOfferingTotalDTO searchDto = new ProductOfferingTotalDTO();
            searchDto.setProductOfferTypeId(Const.PRODUCT_OFFER_TYPE.MOBILE);
            config.setSearchProduct(searchDto);
            productInfoTag.initProduct(this, config);
            listTelecomService = optionSetValueService.getByOptionSetCode(Const.TELCO_FOR_NUMBER);
            listTypeIsdn = optionSetValueService.getByOptionSetCode(Const.OPTION_SET.ISDN_LOOKUP_TYPE);
            listStatus = DataUtil.defaultIfNull(optionSetValueService.getByOptionSetCode(Const.OPTION_SET.ISDN_LOOKUP), new ArrayList<>());
            mapStatusName = listStatus.stream().collect(Collectors.toMap(OptionSetValueDTO::getValue, OptionSetValueDTO::getName));
            GroupFilterRuleDTO searchRule = new GroupFilterRuleDTO();
            infoSearch.setTelecomServiceId(Const.PRODUCT_OFFER_TYPE.MOBILE);
            searchRule.setTelecomServiceId(infoSearch.getTelecomServiceId());
            listFilterRule = DataUtil.defaultIfNull(groupFilterRuleService.search(searchRule, false), Lists.newArrayList());
            Collections.sort(listFilterRule, new Comparator<GroupFilterRuleDTO>() {
                public int compare(GroupFilterRuleDTO o1, GroupFilterRuleDTO o2) {
                    Collator collate = Collator.getInstance(new Locale("vi"));
                    return collate.compare(o1.getName(), o2.getName());
                }
            });
            checkCapcha = checkCapchaFunction();
            shopInfoTag.loadShop(currentStaffDto.getShopId().toString(), false);
            infoSearch.setOwnerId(currentStaffDto.getShopId().toString());
            infoSearch.setOwnerType(Const.OWNER_TYPE.SHOP);
            infoSearch.setOwnerCode(currentStaffDto.getShopCode());
            infoSearch.setOwnerName(currentStaffDto.getShopName());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmSearchIsdn:messages", "", "common.error.happen");
        }
    }

    //tim kiem like theo ma quyen
    private RequiredRoleMap getListRoleTypeKho(String roleCode) {
        RequiredRoleMap roleMap = new RequiredRoleMap();
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        UserToken userToken = (UserToken) request.getSession().getAttribute(CustomConnector.VSA_USER_TOKEN);
        for (ObjectToken token : userToken.getComponentList()) {
            if (!DataUtil.isNullObject(token.getObjectCode()) && token.getObjectCode().contains(roleCode)) {
                roleMap.add(token.getObjectCode());
            }
        }
        return roleMap;
    }

    private boolean checkCapchaFunction() {
        try {
            // kiem tra kenh cua hang
            List<OptionSetValueDTO> listChanelShop = DataUtil.defaultIfNull(optionSetValueService.getByOptionSetCode(Const.OPTION_SET.CAPTCHA_CHANNEL_OF_SHOP), Lists.newArrayList());
            if (!DataUtil.isNullObject(currentStaffDto.getShopChanelTypeId())) {
                for (OptionSetValueDTO option : listChanelShop) {
                    if (option.getValue().equals(currentStaffDto.getShopChanelTypeId().toString())) {
                        return true;
                    }
                }
            }
            //kiem tra kenh nhan vien
            StaffDTO staff = staffService.findOne(currentStaffDto.getStaffId());
            List<OptionSetValueDTO> listChanelStaff = DataUtil.defaultIfNull(optionSetValueService.getByOptionSetCode(Const.OPTION_SET.CAPTCHA_CHANNEL_OF_STAFF), Lists.newArrayList());
            if (!DataUtil.isNullObject(staff.getChannelTypeId())) {
                for (OptionSetValueDTO option : listChanelStaff) {
                    if (option.getValue().equals(staff.getChannelTypeId().toString())) {
                        return true;
                    }
                }
            }
            //kiem tra shop
            List<OptionSetValueDTO> listShop = DataUtil.defaultIfNull(optionSetValueService.getByOptionSetCode(Const.OPTION_SET.CAPTCHA_FOR_SHOP), Lists.newArrayList());
            if (!DataUtil.isNullObject(currentStaffDto.getShopCode())) {
                for (OptionSetValueDTO option : listShop) {
                    if (option.getValue().equals(currentStaffDto.getShopCode())) {
                        return true;
                    }
                }
            }
            //kiem tra user
            List<OptionSetValueDTO> listUser = DataUtil.defaultIfNull(optionSetValueService.getByOptionSetCode(Const.OPTION_SET.CAPTCHA_FOR_STAFF), Lists.newArrayList());
            if (!DataUtil.isNullObject(currentStaffDto.getStaffCode())) {
                for (OptionSetValueDTO option : listUser) {
                    if (option.getValue().equals(currentStaffDto.getStaffCode())) {
                        return true;
                    }
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        return false;
    }

    public void onCheckInputFile() {
        if (!infoSearch.getInputFile()) {
            infoSearch.setAttachFileName(null);
            contentByte = null;
        }
    }

    @Secured("@")
    public void fileUploadAction(FileUploadEvent event) {

        try {
            uploadedFile = event.getFile();
            BaseMessage message = validateFileUploadWhiteList(uploadedFile, ALOW_EXTENSION_EXCEL_TYPE_LIST, MAX_SIZE_5M);
            if (!message.isSuccess()) {
                contentByte = null;
                LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                ex.setDescription(message.getDescription());
                throw ex;
            }
            if (uploadedFile != null) {
                contentByte = uploadedFile.getContents();
                infoSearch.setAttachFileName(uploadedFile.getFileName());
            }
        } catch (LogicException ex) {
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    @Secured("@")
    public void onChangeTelecomService() {
        try {
            ConfigProductTagDTO config = new ConfigProductTagDTO();
            ProductOfferingTotalDTO searchDto = new ProductOfferingTotalDTO();
            if (infoSearch.getTelecomServiceId().equals(Const.STOCK_TYPE.STOCK_ISDN_MOBILE) || infoSearch.getTelecomServiceId().equals(Const.STOCK_TYPE.STOCK_ISDN_HOMEPHONE)) {
                searchDto.setProductOfferTypeId(infoSearch.getTelecomServiceId());
            } else {
                searchDto.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_ISDN_PSTN);
            }
            config.setSearchProduct(searchDto);
            productInfoTag.initProduct(this, config);
            productInfoTag.doClearProduct("doClearProduct");
            GroupFilterRuleDTO searchRule = new GroupFilterRuleDTO();
            searchRule.setTelecomServiceId(infoSearch.getTelecomServiceId());
            listFilterRule = DataUtil.defaultIfNull(groupFilterRuleService.search(searchRule, false), Lists.newArrayList());
            Collections.sort(listFilterRule, new Comparator<GroupFilterRuleDTO>() {
                public int compare(GroupFilterRuleDTO o1, GroupFilterRuleDTO o2) {
                    Collator collate = Collator.getInstance(new Locale("vi"));
                    return collate.compare(o1.getName(), o2.getName());
                }
            });
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmSearchIsdn:messages", "", "common.error.happen");
        }
    }

    public boolean checkPermissionViewIsdn(String ownerCode) {
        List<OptionSetValueDTO> listSpecialShop = Lists.newArrayList();
        if (!requiredRoleMap.hasRole(Const.PERMISION.ROLE_LOOK_UP_SPECIAL_STOCK)) {
            try {
                listSpecialShop = optionSetValueService.getByOptionSetCode(Const.OPTION_SET.LOOK_UP_SPECIAL_STOCK);
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
            for (OptionSetValueDTO option : listSpecialShop) {
                if (option.getValue().equals(ownerCode)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkValidate() {
        //check quyen tra cuu so dep
        if (!checkPermissionViewIsdn(infoSearch.getOwnerCode())) {
            reportError("frmSearchIsdn:messages", "", "search.isdn.permision.error", infoSearch.getFullNameOwner());
            return false;
        }
        //kiem tra dai so
        if (!DataUtil.isNullObject(infoSearch.getStartRange()) && !DataUtil.isNumber(infoSearch.getStartRange())) {
            reportError("frmSearchIsdn:messages", "", "search.isdn.start.range.invalid");
            focusElementByRawCSSSlector(".fromNumberTxt");
            return false;
        }
        if (!DataUtil.isNullObject(infoSearch.getEndRange()) && !DataUtil.isNumber(infoSearch.getEndRange())) {
            reportError("frmSearchIsdn:messages", "", "search.isdn.end.range.invalid");
            focusElementByRawCSSSlector(".toNumberTxt");
            return false;
        }
        if (!DataUtil.isNullObject(infoSearch.getEndRange()) && !DataUtil.isNullObject(infoSearch.getStartRange())) {
            if (DataUtil.safeToLong(infoSearch.getEndRange()) < DataUtil.safeToLong(infoSearch.getStartRange())) {
                reportError("frmSearchIsdn:messages", "", "search.isdn.start.end.range.invalid");
                focusElementByRawCSSSlector(".toNumberTxt");
                return false;
            }
            /*if (DataUtil.safeToLong(infoSearch.getEndRange()) - DataUtil.safeToLong(infoSearch.getStartRange()) > 100000) {
                reportError("frmSearchIsdn:messages", "", "search.isdn.over.range");
                focusElementByRawCSSSlector(".toNumberTxt");
                return false;
            }*/
        }
        //kiem tra dai gia
        if (!DataUtil.isNullObject(infoSearch.getFromPrice()) && !DataUtil.isNumber(infoSearch.getFromPrice())) {
            reportError("frmSearchIsdn:messages", "", "search.isdn.from.price.invalid");
            focusElementByRawCSSSlector(".fromPriceTxt");
            return false;
        }
        if (!DataUtil.isNullObject(infoSearch.getToPrice()) && !DataUtil.isNumber(infoSearch.getToPrice())) {
            reportError("frmSearchIsdn:messages", "", "search.isdn.to.price.invalid");
            focusElementByRawCSSSlector(".toPriceTxt");
            return false;
        }
        if (!DataUtil.isNullObject(infoSearch.getFromPrice()) && !DataUtil.isNullObject(infoSearch.getToPrice())
                && DataUtil.safeToLong(infoSearch.getToPrice()) < DataUtil.safeToLong(infoSearch.getFromPrice())) {
            reportError("frmSearchIsdn:messages", "", "search.isdn.from.to.price.invalid");
            focusElementByRawCSSSlector(".toPriceTxt");
            return false;
        }
        //kiem tra tu ngay den ngay
        if (!DataUtil.isNullObject(infoSearch.getFromDate()) && !DataUtil.isNullObject(infoSearch.getToDate())) {
            if (DateUtil.compareDateToDate(infoSearch.getFromDate(), infoSearch.getToDate()) > 0) {
                reportError("frmSearchIsdn:messages", "", "stock.trans.from.to.valid");
                focusElementByRawCSSSlector(".txtToDateSearch");
                return false;
            }

            if (DateUtil.daysBetween2Dates(infoSearch.getToDate(), infoSearch.getFromDate()) > 30) {
                reportError("frmSearchIsdn:messages", "", "stock.trans.from.to.valid");
                focusElementByRawCSSSlector(".txtToDateSearch");
                return false;
            }

        }

        return true;
    }


    @Secured("@")
    public void searchIsdn() {
        try {
            if (checkCapcha) {
                validateCapCha();
            }
            if (infoSearch.getInputFile()) {
                infoSearch.setOwnerType(Const.OWNER_TYPE.SHOP);
                BaseMessage message = validateFileUploadWhiteList(uploadedFile, ALOW_EXTENSION_EXCEL_TYPE_LIST, MAX_SIZE_5M);
                if (!message.isSuccess()) {
                    LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                    ex.setDescription(message.getDescription());
                    throw ex;
                }
                ExcellUtil excel = new ExcellUtil(uploadedFile, contentByte);
                List<String> list = Lists.newArrayList();
                Sheet sheet = excel.getSheetAt(0);
                if (!DataUtil.isNullObject(sheet)) {
                    int totalRow = sheet.getPhysicalNumberOfRows();
                    for (int i = 1; i < totalRow; i++) {
                        Row row = sheet.getRow(i);
                        if (!DataUtil.isNullObject(row)) {
                            Cell cell = row.getCell(0);
                            if (!DataUtil.isNullObject(cell)) {
                                list.add(excel.getStringValue(cell));
                            }
                        }
                    }
                }
                if (DataUtil.isNullOrEmpty(list)) {
                    reportError("frmSearchIsdn:messages", "", "search.isdn.file.empty");
                    return;
                }
                if (list.size() > 15000) {
                    reportError("frmSearchIsdn:messages", "", "mn.stock.status.isdn.delete.maxline", 15000);
                    return;
                }
                List<Long> lstIsdnFromFile = Lists.newArrayList();
                for (String ob : list) {
                    if (!DataUtil.isNullObject(ob) && DataUtil.isNumber(ob)) {
                        lstIsdnFromFile.add(DataUtil.safeToLong(ob.trim()));
                    }
                }
                if (DataUtil.isNullOrEmpty(lstIsdnFromFile)) {
                    reportError("frmSearchIsdn:messages", "", "search.isdn.file.empty");
                    return;
                }
                infoSearch.setListIsdnReadFromFile(lstIsdnFromFile);
                //kiem tra tu ngay den ngay
                if (!DataUtil.isNullObject(infoSearch.getFromDate()) && !DataUtil.isNullObject(infoSearch.getToDate())) {
                    if (DateUtil.compareDateToDate(infoSearch.getFromDate(), infoSearch.getToDate()) > 0) {
                        reportError("frmSearchIsdn:messages", "", "stock.trans.from.to.valid");
                        focusElementByRawCSSSlector(".txtToDateSearch");
                        return;
                    }

                    try {
                        if (DateUtil.daysBetween2Dates(infoSearch.getToDate(), infoSearch.getFromDate()) > 30) {
                            reportError("frmSearchIsdn:messages", "", "stock.trans.from.to.valid");
                            focusElementByRawCSSSlector(".txtToDateSearch");
                            return;
                        }
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            } else if (!checkValidate()) {
                return;
            }
            RequiredRoleMap roleTypeKho = getListRoleTypeKho(Const.PERMISION.LOOK_UP_ISDN_ROLE_TYPE_KHO);
            infoSearch.setRoleTypeKho(roleTypeKho);
            infoSearch.setRequiredRoleMap(requiredRoleMap);
            infoSearchExport = DataUtil.cloneBean(infoSearch);
            listIsdn = DataUtil.defaultIfNull(stockNumberService.searchIsdn(infoSearch, 10000, 100), Lists.newArrayList());
            for (IsdnViewDetailDTO dto : listIsdn) {
                dto.setHasPerimisionUnlockIsdn(DataUtil.safeEqual(currentStaffDto.getStaffId(), dto.getLockByStaff()) && Const.ISDN.LOCK.equals(dto.getStatus()));
                if (dto.getHasPermissionView()) {
                    dto.setStatusName(DataUtil.safeToString(mapStatusName.get(dto.getStatus())));
                    dto.setTelecomServiceLabel(getTelecomServiceName(dto.getTelecomServiceId()));
                } else {
                    dto.setStatusName(getLabelNotPermissionViewSpecialIsdn());
                    dto.setProName(getLabelNotPermissionViewSpecialIsdn());
                    dto.setRuleName(getLabelNotPermissionViewSpecialIsdn());
                    dto.setTelecomServiceLabel(getLabelNotPermissionViewSpecialIsdn());
                }
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmSearchIsdn:messages", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmSearchIsdn:messages", "", "common.error.happen");
            topPage();
        }
    }

    @Secured("@")
    public boolean getHasRoleKeepIsdn() {
//        return requiredRoleMap != null && requiredRoleMap.hasRole(Const.PERMISION.BCCS2_IM_QLSO_TRACUUSO_GIUSO);
        return true;
    }


    @Secured("@")
    public void doLockIsdn(String type) {
        try {
            String isdnResult = "";
            if ("0".equals(type)) {
                isdnResult = stockNumberService.lockIsdnByStaff(getStaffDTO().getStaffId(), isdn, BccsLoginSuccessHandler.getIpAddress(), requiredRoleMap);//IMServiceUtil.getIpClient()
            } else if ("1".equals(type)) {
                isdnResult = stockNumberService.lockNiceIsdnByStaff(getStaffDTO().getStaffId(), isdn, BccsLoginSuccessHandler.getIpAddress());//IMServiceUtil.getIpClient()
            }
            if (!"".equals(isdnResult)) {
                reportSuccess("", "stock.number.isdn.lock.isdn.option.lock.sucess", isdnResult);
                if (listIsdn != null && listIsdn.size() > index) {
                    IsdnViewDetailDTO dto = listIsdn.get(index);
                    dto.setStatus(Const.ISDN.LOCK);
                    dto.setStatusName(DataUtil.safeToString(mapStatusName.get(dto.getStatus())));
                    dto.setLockByStaff(getStaffDTO().getStaffId());
                    dto.setHasPerimisionUnlockIsdn(true);
                }
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmSearchIsdn:messages", ex);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmSearchIsdn:messages", "", "common.error.happen");
        }
        topPage();
    }

    @Secured("@")
    public void doUnLockIsdn() {
        try {
            stockNumberService.unlockIsdnByStaff(getStaffDTO().getStaffId(), isdn, BccsLoginSuccessHandler.getIpAddress());
            reportSuccess("", "stock.number.isdn.unlock.isdn.option.lock.sucess", isdn);
            if (listIsdn != null && listIsdn.size() > index) {
                IsdnViewDetailDTO dto = listIsdn.get(index);
                dto.setStatus(Const.ISDN.NEW_NUMBER);
                dto.setStatusName(DataUtil.safeToString(mapStatusName.get(dto.getStatus())));
                dto.setHasPerimisionUnlockIsdn(false);
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmSearchIsdn:messages", ex);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmSearchIsdn:messages", "", "common.error.happen");
        }
        topPage();
    }

    @Secured("@")
    public void doValidateLockIsdn(String isdn, int index) {
        this.index = index;
        this.isdn = isdn;

    }

    @Secured("@")
    public void onChangeTypeStock() {
    }

    @Secured("@")
    public void onChangeFilterRule() {
        try {
            if (DataUtil.isNullOrZero(infoSearch.getFilterrRuleId())) {
                listGroupFilterRule = Lists.newArrayList();
            } else {
                GroupFilterRuleDTO searchRule = new GroupFilterRuleDTO();
                searchRule.setParentId(infoSearch.getFilterrRuleId());
                listGroupFilterRule = DataUtil.defaultIfNull(groupFilterRuleService.search(searchRule, true), Lists.newArrayList());
                Collections.sort(listGroupFilterRule, new Comparator<GroupFilterRuleDTO>() {
                    public int compare(GroupFilterRuleDTO o1, GroupFilterRuleDTO o2) {
                        Collator collate = Collator.getInstance(new Locale("vi"));
                        return collate.compare(o1.getName(), o2.getName());
                    }
                });
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmSearchIsdn:messages", "", "common.error.happen");
        }
    }

    @Secured("@")
    public void onChangeGroupFilterRule() {
        try {
            if (DataUtil.isNullOrZero(infoSearch.getGroupFilterRuleId())) {
                listRuleNiceDigit = Lists.newArrayList();
            } else {
                NumberFilterRuleDTO searchRule = new NumberFilterRuleDTO();
                searchRule.setGroupFilterRuleId(infoSearch.getGroupFilterRuleId());
                listRuleNiceDigit = DataUtil.defaultIfNull(numberFilterRuleService.getListNumerFilterRule(searchRule), Lists.newArrayList());
                Collections.sort(listRuleNiceDigit, new Comparator<NumberFilterRuleDTO>() {
                    public int compare(NumberFilterRuleDTO o1, NumberFilterRuleDTO o2) {
                        Collator collate = Collator.getInstance(new Locale("vi"));
                        return collate.compare(o1.getName(), o2.getName());
                    }
                });
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmSearchIsdn:messages", "", "common.error.happen");
        }
    }

    @Secured("@")
    public void onViewIsdnCommitment(IsdnViewDetailDTO dto) {
        try {
            if (DataUtil.isNullOrZero(dto.getProOfferId())) {
                listIsdnCommitment = Lists.newArrayList();
            } else {
                listIsdnCommitment = productOfferPriceService.getProductOfferPriceByProdOfferId(dto.getProOfferId());
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmSearchIsdn:messages", "", "common.error.happen");
        }
    }

    @Secured("@")
    public void searchHistory() {
        try {
            if (DataUtil.isNullObject(transStock.getFromDate())) {
                reportError("frmIsdnHistory:messages", "", "mn.stock.from.date.not.blank");
                focusElementByRawCSSSlector(".txtFromDate");
                return;
            }
            if (DataUtil.isNullObject(transStock.getToDate())) {
                reportError("frmIsdnHistory:messages", "", "mn.stock.to.date.not.blank");
                focusElementByRawCSSSlector(".txtToDate");
                return;
            }
            if (DateUtil.compareDateToDate(transStock.getFromDate(), transStock.getToDate()) > 0) {
                reportError("frmIsdnHistory:messages", "", "stock.trans.from.to.valid");
                focusElementByRawCSSSlector(".txtToDate");
                return;
            }

            if (DateUtil.daysBetween2Dates(transStock.getToDate(), transStock.getFromDate()) > 30) {
                reportError("frmIsdnHistory:messages", "", "stock.trans.from.to.valid");
                focusElementByRawCSSSlector(".txtToDate");
                return;
            }
            listIsdnHistory = stockIsdnTransService.searchHistory(transStock);
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmIsdnHistory:messages", "", ex.getKeyMsg());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmIsdnHistory:messages", "", "common.error.happen");
        }
    }

    @Secured("@")
    public void onViewIsdnHistory(IsdnViewDetailDTO dto) {
        try {
            transStock = new ManageTransStockDto();
            transStock.setIsdn(dto.getIsdn());
            transStock.setFromDate(DateUtil.getFirstDayOfMonth(new Date()));
            transStock.setToDate(new Date());
            listIsdnHistory = stockIsdnTransService.searchHistory(transStock);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmIsdnHistory:messages", "", "common.error.happen");
        }
    }

    @Secured("@")
    public void onViewInfoLockStaff(Long staffId) {
        try {
            staffLock = staffService.findOne(staffId);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmIsdnHistory:messages", "", "common.error.happen");
        }
    }

    @Secured("@")
    public void exportIsdn() {
        try {
            List<IsdnViewDetailDTO> listIsdnExport = DataUtil.defaultIfNull(stockNumberService.searchIsdn(infoSearchExport, 10000, 0), Lists.newArrayList());
            for (IsdnViewDetailDTO dto : listIsdnExport) {
                if (dto.getHasPermissionView()) {
                    dto.setStatusName(DataUtil.safeToString(mapStatusName.get(dto.getStatus())));
                    dto.setTelecomServiceLabel(getTelecomServiceName(dto.getTelecomServiceId()));
                } else {
                    dto.setStatusName(getLabelNotPermissionViewSpecialIsdn());
                    dto.setProName(getLabelNotPermissionViewSpecialIsdn());
                    dto.setRuleName(getLabelNotPermissionViewSpecialIsdn());
                    dto.setTelecomServiceLabel(getLabelNotPermissionViewSpecialIsdn());
                }
            }

            FileExportBean bean = new FileExportBean();
            bean.setTempalatePath(FileUtil.getTemplatePath());
            bean.setTemplateName("exportIsdnTemplate.xls");
            bean.putValue("lstData", listIsdnExport);
            bean.putValue("searchIsdnController", this);
            bean.putValue("fromDate", DateUtil.date2ddMMyyyyString(infoSearchExport.getFromDate()));
            bean.putValue("toDate", DateUtil.date2ddMMyyyyString(infoSearchExport.getToDate()));
            bean.putValue("createUser", BccsLoginSuccessHandler.getStaffDTO().getName());
            bean.putValue("createDate", DateUtil.date2ddMMyyyyString(new Date()));
            bean.putValue("notHasPermission", getLabelNotPermissionViewSpecialIsdn());

            Workbook workbook = FileUtil.exportWorkBook(bean);

            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + "Isdn_Externd_List_" + currentStaffDto.getName() + "_" + DateUtil.date2ddMMyyyyHHMMssNoSlash(new Date()) + ".xls" + "\"");
            externalContext.setResponseContentType("application/vnd.ms-excel");
            workbook.write(externalContext.getResponseOutputStream());
            facesContext.responseComplete();
            return;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmSearchIsdn:messages", "", "common.error.happen");
        }
    }

    @Secured("@")
    public void exportIsdnText() {
        try {
            List<IsdnViewDetailDTO> listIsdnExport = DataUtil.defaultIfNull(stockNumberService.searchIsdn(infoSearchExport, 10000, 0), Lists.newArrayList());
            for (IsdnViewDetailDTO dto : listIsdnExport) {
                if (dto.getHasPermissionView()) {
                    dto.setStatusName(DataUtil.safeToString(mapStatusName.get(dto.getStatus())));
                    dto.setTelecomServiceLabel(getTelecomServiceName(dto.getTelecomServiceId()));
                } else {
                    dto.setStatusName(getLabelNotPermissionViewSpecialIsdn());
                    dto.setProName(getLabelNotPermissionViewSpecialIsdn());
                    dto.setRuleName(getLabelNotPermissionViewSpecialIsdn());
                    dto.setTelecomServiceLabel(getLabelNotPermissionViewSpecialIsdn());
                }
            }

            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + "Isdn_Externd_List_" + currentStaffDto.getName() + "_" + DateUtil.date2ddMMyyyyHHMMssNoSlash(new Date()) + ".txt" + "\"");
            externalContext.setResponseContentType("application/txt");
            String header = BundleUtil.getText("search.isdn.header.export.text");
            externalContext.getResponseOutputStream().write(header.getBytes(Charset.forName("UTF-8")));
            for (int i = 0; i < listIsdnExport.size(); i++) {
                IsdnViewDetailDTO dto = listIsdnExport.get(i);
                String line = i + 1 + "|" + (DataUtil.isNullObject(dto.getIsdn()) ? "NULL" : dto.getIsdn())
                        + "|" + (DataUtil.isNullOrEmpty(dto.getCodeShopStaff()) ? "NULL" : dto.getCodeShopStaff())
                        + "|" + (DataUtil.isNullObject(dto.getStatusName()) ? "NULL" : dto.getStatusName())
                        + "|" + (DataUtil.isNullObject(dto.getProName()) ? "NULL" : dto.getProName())
                        + "|" + (DataUtil.isNullObject(dto.getRuleName()) ? "NULL" : dto.getRuleName())
                        + "|" + (DataUtil.isNullObject(dto.getTelecomServiceLabel()) ? "NULL" : dto.getTelecomServiceLabel())
                        + "|" + (DataUtil.isNullObject(dto.getEffectDateTime()) ? "NULL" : dto.getEffectDateTime()) + "\r\n";
                externalContext.getResponseOutputStream().write(line.getBytes(Charset.forName("UTF-8")));
            }
            facesContext.responseComplete();
            return;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmSearchIsdn:messages", "", "common.error.happen");
        }
    }

    public String getTelecomServiceName(Long telecomServiceId) {
        if (!DataUtil.isNullOrZero(telecomServiceId)) {
            for (OptionSetValueDTO op : listTelecomService) {
                if (op.getValue().equals(telecomServiceId + "")) {
                    return op.getName();
                }
            }
        }
        return "";
    }

    public void doClearStock() {
        infoSearch.setOwnerId(null);
        infoSearch.setOwnerCode(null);
        infoSearch.setOwnerName(null);
    }

    public void doSelectedStock(VShopStaffDTO dto) {
        infoSearch.setOwnerId(dto.getOwnerId());
        infoSearch.setOwnerName(dto.getOwnerName());
        infoSearch.setOwnerCode(dto.getOwnerCode());
    }

    public void doClearStockLock() {
        infoSearch.setOwnerIdLock(null);
        infoSearch.setOwnerCodeLock(null);
        infoSearch.setOwnerNameLock(null);
    }

    public void doSelectedStockLock(VShopStaffDTO dto) {
        infoSearch.setOwnerIdLock(dto.getOwnerId());
        infoSearch.setOwnerNameLock(dto.getOwnerName());
        infoSearch.setOwnerCodeLock(dto.getOwnerCode());
    }

    /**
     * Kiem tra co phai link direct tu sale hay khong
     */
    private void helperSystem() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String sys = request.getParameter(Const.SYSTEM_SALE);
        directLink = DataUtil.safeEqual(sys, Const.SYSTEM_SALE);
    }

    public void doClearProduct() {
        infoSearch.setProOfferId(null);
    }

    public void doSelectedProduct(ProductOfferingTotalDTO dto) {
        infoSearch.setProOfferId(dto.getProductOfferingId());
    }

    public InfoSearchIsdnDTO getInfoSearch() {
        return infoSearch;
    }

    public void setInfoSearch(InfoSearchIsdnDTO infoSearch) {
        this.infoSearch = infoSearch;
    }

    public List<OptionSetValueDTO> getListTelecomService() {
        return listTelecomService;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public ShopInfoNameable getShopInfoTag() {
        return shopInfoTag;
    }

    public void setShopInfoTag(ShopInfoNameable shopInfoTag) {
        this.shopInfoTag = shopInfoTag;
    }

    public StaffInfoNameable getStaffInfoTag() {
        return staffInfoTag;
    }

    public void setStaffInfoTag(StaffInfoNameable staffInfoTag) {
        this.staffInfoTag = staffInfoTag;
    }

    public List<OptionSetValueDTO> getListTypeIsdn() {
        return listTypeIsdn;
    }

    public void setListTypeIsdn(List<OptionSetValueDTO> listTypeIsdn) {
        this.listTypeIsdn = listTypeIsdn;
    }

    public List<OptionSetValueDTO> getListStatus() {
        return listStatus;
    }

    public void setListStatus(List<OptionSetValueDTO> listStatus) {
        this.listStatus = listStatus;
    }

    public List<GroupFilterRuleDTO> getListFilterRule() {
        return listFilterRule;
    }

    public void setListFilterRule(List<GroupFilterRuleDTO> listFilterRule) {
        this.listFilterRule = listFilterRule;
    }

    public void setListTelecomService(List<OptionSetValueDTO> listTelecomService) {
        this.listTelecomService = listTelecomService;
    }

    public List<GroupFilterRuleDTO> getListGroupFilterRule() {
        return listGroupFilterRule;
    }

    public List<NumberFilterRuleDTO> getListRuleNiceDigit() {
        return listRuleNiceDigit;
    }

    public void setListRuleNiceDigit(List<NumberFilterRuleDTO> listRuleNiceDigit) {
        this.listRuleNiceDigit = listRuleNiceDigit;
    }

    public ProductInfoNameable getProductInfoTag() {
        return productInfoTag;
    }

    public void setProductInfoTag(ProductInfoNameable productInfoTag) {
        this.productInfoTag = productInfoTag;
    }

    public void setListGroupFilterRule(List<GroupFilterRuleDTO> listGroupFilterRule) {
        this.listGroupFilterRule = listGroupFilterRule;
    }

    public List<IsdnViewDetailDTO> getListIsdn() {
        return listIsdn;
    }

    public List<ProductOfferPriceDTO> getListIsdnCommitment() {
        return listIsdnCommitment;
    }

    public void setListIsdnCommitment(List<ProductOfferPriceDTO> listIsdnCommitment) {
        this.listIsdnCommitment = listIsdnCommitment;
    }

    public void setListIsdn(List<IsdnViewDetailDTO> listIsdn) {
        this.listIsdn = listIsdn;
    }

    public IsdnViewDetailDTO getCurrentViewHistory() {
        return currentViewHistory;
    }

    public ManageTransStockDto getTransStock() {
        return transStock;
    }

    public void setTransStock(ManageTransStockDto transStock) {
        this.transStock = transStock;
    }

    public void setCurrentViewHistory(IsdnViewDetailDTO currentViewHistory) {
        this.currentViewHistory = currentViewHistory;
    }

    public List<StockIsdnTransDTO> getListIsdnHistory() {
        return listIsdnHistory;
    }

    public boolean getCheckCapcha() {
        return checkCapcha;
    }

    public void setCheckCapcha(boolean checkCapcha) {
        this.checkCapcha = checkCapcha;
    }

    public void setListIsdnHistory(List<StockIsdnTransDTO> listIsdnHistory) {
        this.listIsdnHistory = listIsdnHistory;
    }

    public boolean isLookUpStockExt() {
        return lookUpStockExt;
    }

    public void setLookUpStockExt(boolean lookUpStockExt) {
        this.lookUpStockExt = lookUpStockExt;
    }

    public boolean getEnableCheckBoxViewSpecialIsdn() {
        return enableCheckBoxViewSpecialIsdn;
    }

    public void setEnableCheckBoxViewSpecialIsdn(boolean enableCheckBoxViewSpecialIsdn) {
        this.enableCheckBoxViewSpecialIsdn = enableCheckBoxViewSpecialIsdn;
    }

    public String getLabelNotPermissionViewSpecialIsdn() {

        return BundleUtil.getText("search.isdn.not.permission.view");
    }

    public boolean isDirectLink() {
        return directLink;
    }

    public void setDirectLink(boolean directLink) {
        this.directLink = directLink;
    }

    public StaffInfoNameable getStaffInfoTagLock() {
        return staffInfoTagLock;
    }

    public void setStaffInfoTagLock(StaffInfoNameable staffInfoTagLock) {
        this.staffInfoTagLock = staffInfoTagLock;
    }

    public StaffDTO getStaffLock() {
        return staffLock;
    }

    public void setStaffLock(StaffDTO staffLock) {
        this.staffLock = staffLock;
    }
}
