package com.viettel.bccs.inventory.controller.isdn;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.InventoryController;
import com.viettel.bccs.inventory.common.ValidateService;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.StockNumberService;
import com.viettel.bccs.inventory.tag.AssignIsdnTagNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.web.common.annotation.Security;
import com.viettel.web.common.controller.BaseController;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.TabChangeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.util.HashMap;
import java.util.List;

@Component
@Scope("view")
@ManagedBean(name = "assigningModelForIsdnController")
public class AssigningModelForIsdnController extends InventoryController {

    @Autowired
    private StockNumberService stockNumberService;
    @Autowired
    private AssignIsdnTagNameable assignIdsnTag;
    private VShopStaffDTO curShopStaff;
    private List<TableNumberRangeDTO> listRangeGrant = Lists.newArrayList();
    private List<TableNumberRangeDTO> listRangeGrantSelected = Lists.newArrayList();
    private SearchNumberRangeDTO checkUpdateDTO;
    private HashMap<String, String> mapISDN = new HashMap<String, String>();

    private int tabIndex;

    @PostConstruct
    @Security("@")
    public void init() {
        assignIdsnTag.init(this);
    }

    @Secured("@")
    public void receiveShop(VShopStaffDTO vShopStaffDTO) {
        curShopStaff = DataUtil.cloneBean(vShopStaffDTO);
    }

    public void clearCurrentShop() {
        curShopStaff = new VShopStaffDTO();
    }

    private void previewRange() {
        try {
            List<TableNumberRangeDTO> lstResult;
            lstResult = stockNumberService.previewFromRange(assignIdsnTag.getSearchStockNumberDTO());
            if (!DataUtil.isNullObject(lstResult)) {
                for (TableNumberRangeDTO tableNumberRangeDTO : lstResult) {
//                    tableNumberRangeDTO.setProductCodeNew(assignIdsnTag.getProductOfferingTotalNewDTO().getCode() + "-" + assignIdsnTag.getProductOfferingTotalNewDTO().getName());
//                    tableNumberRangeDTO.setProductOfferNewId(assignIdsnTag.getProductOfferingTotalNewDTO().getProductOfferingId());
                    if (!DataUtil.isNullOrZero(tableNumberRangeDTO.getProductOfferId())) {
                        tableNumberRangeDTO.setProductCodeOld(tableNumberRangeDTO.getProductCodeOld() + "-" + tableNumberRangeDTO.getProductNameOld());
                    }
                    listRangeGrant.add(tableNumberRangeDTO);
                }
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happened");
        }
    }

    private void previewFile() {
        try {
            List<TableNumberRangeDTO> lstResult;
            lstResult = stockNumberService.previewFromFile(assignIdsnTag.getSearchStockNumberDTO());
            if (!DataUtil.isNullObject(lstResult)) {
                for (TableNumberRangeDTO tableNumberRangeDTO : lstResult) {
                    ProductOfferingTotalDTO productOfferingTotalDTO = ValidateService.getProductOfferingByCode(tableNumberRangeDTO.getProductCodeNew(), assignIdsnTag.getLsProductOfferingTotalDTO());
                    tableNumberRangeDTO.setProductCodeNew(productOfferingTotalDTO.getCode() + "-" + productOfferingTotalDTO.getName());
                    tableNumberRangeDTO.setProductOfferNewId(productOfferingTotalDTO.getProductOfferingId());

                    if (!DataUtil.isNullOrZero(tableNumberRangeDTO.getProductOfferId())) {
                        tableNumberRangeDTO.setProductCodeOld(tableNumberRangeDTO.getProductCodeOld() + "-" + tableNumberRangeDTO.getProductNameOld());
                    }
                    listRangeGrant.add(tableNumberRangeDTO);
                }
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happened");
        }
    }

    private boolean checkBeforeUpdateIsdn(List<TableNumberRangeDTO> listRangeGrant) {
        boolean ok = true;
        try {
            checkUpdateDTO = new SearchNumberRangeDTO();
            checkUpdateDTO.setShopId(listRangeGrant.get(0).getShopId());
            checkUpdateDTO.setServiceType(listRangeGrant.get(0).getTelecomService());
            mapISDN = new HashMap<String, String>();
            Long fromIsdn;
            Long toIsdn;
            for (TableNumberRangeDTO dto : listRangeGrant) {
                fromIsdn = Long.parseLong(dto.getFromISDN());
                toIsdn = Long.parseLong(dto.getToISDN());
                for (Long isdn = fromIsdn; isdn <= toIsdn; isdn++) {
                    mapISDN.put(isdn.toString(), "");
                }
            }
            checkUpdateDTO.setMapISDN(mapISDN);
            List<String> lstError = stockNumberService.checkListStockNumber(checkUpdateDTO);
            if (!DataUtil.isNullObject(lstError)) {
                return false;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happened");
        }
        return ok;
    }

//    @Secured("@")
//    public String getServiceNameById() {
//        return assignIdsnTag.getServiceNameById();
//    }

    @Secured("@")
    public void previewModel(boolean renderedFromFile) throws Exception {

        try {

            listRangeGrant = Lists.newArrayList();
            if (!renderedFromFile) {
                //len danh sach
                if (!DataUtil.isNullObject(curShopStaff)) {
                    assignIdsnTag.getSearchStockNumberDTO().setShopId(DataUtil.safeToLong(curShopStaff.getOwnerId()));
                }
//                if (DataUtil.isNullOrZero(assignIdsnTag.getSearchStockNumberDTO().getShopId())) {
//                    assignIdsnTag.getSearchStockNumberDTO().setShopId(BccsLoginSuccessHandler.getStaffDTO().getShopId());
//                }
                if (!DataUtil.isNullObject(assignIdsnTag.getProductOfferingTotalDTO())) {
                    assignIdsnTag.getSearchStockNumberDTO().setProductOldId(assignIdsnTag.getProductOfferingTotalDTO().getProductOfferingId());
                } else {
                    assignIdsnTag.getSearchStockNumberDTO().setProductOldId(null);
                }
                String startRange = assignIdsnTag.getSearchStockNumberDTO().getStartRange();
                String endRange = assignIdsnTag.getSearchStockNumberDTO().getEndRange();

                if (DataUtil.isNullOrEmpty(startRange)) {
                    focusElementByRawCSSSlector(".fromNumberTxt");
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "start.range.require");
                }
                if (DataUtil.isNullOrEmpty(endRange)) {
                    focusElementByRawCSSSlector(".toNumberTxt");
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "end.range.require");
                }

                Long start = DataUtil.safeToLong(startRange);
                Long end = DataUtil.safeToLong(endRange);

                if ( (start + "").length() != (end + "").length()) {
                    focusElementByRawCSSSlector(".fromNumberTxt");
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "mn.stock.status.isdn.range.matchlength");
                }

                if (end < start || (end - start > 100000)) {
                    focusElementByRawCSSSlector(".fromNumberTxt");
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "mn.stock.status.isdn.update.status.end.notmatch");
                }

                if (!DataUtil.isNullOrZero(assignIdsnTag.getProductOfferingTotalNewDTO().getProductOfferingId())) {
                    assignIdsnTag.getSearchStockNumberDTO().setProductNewId(assignIdsnTag.getProductOfferingTotalNewDTO().getProductOfferingId());
                }
                assignIdsnTag.setServiceType(assignIdsnTag.getSearchStockNumberDTO().getServiceType());
                assignIdsnTag.setServiceNameById();
                previewRange();
            } else {
//                if (DataUtil.isNullOrZero(assignIdsnTag.getSearchStockNumberDTO().getShopId())) {
//                    assignIdsnTag.getSearchStockNumberDTO().setShopId(BccsLoginSuccessHandler.getStaffDTO().getShopId());
//                }

                BaseMessage message = validateFileUploadWhiteList(assignIdsnTag.getUploadedFile(), ALOW_EXTENSION_EXCEL_TYPE_LIST, MAX_SIZE_5M);
                if (!message.isSuccess()) {
                    assignIdsnTag.setUploadedFile(null);
                    assignIdsnTag.setFileName(null);
                    LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                    ex.setDescription(message.getDescription());
                    focusElementByRawCSSSlector(".outputAttachFile");
                    throw ex;
                }

                //len danh sach
                int process = assignIdsnTag.processInputExcel();
                int check = assignIdsnTag.checkFileImport();
                if (process == -1 || check == -1) {
                    focusElementByRawCSSSlector(".outputAttachFile");
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "mn.stock.status.isdn.update.file.empty");
                }
                if (process == -2) {
                    focusElementByRawCSSSlector(".outputAttachFile");
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "mn.stock.status.isdn.update.file.max");
                }
                previewFile();
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happened");
        }
    }

    @Secured("@")
    public void doConfirmUpdateDlg() {
        if (DataUtil.isNullObject(assignIdsnTag) || DataUtil.isNullObject(assignIdsnTag.getProductOfferingTotalNewDTO())
                || DataUtil.isNullObject(assignIdsnTag.getProductOfferingTotalNewDTO().getProductOfferingId())) {
            reportError("", "", "search.number.range.validate.require.productNewId");
            return;
        }
       // getConfirmMessage();
    }

    @Secured("@")
    public void doUpdateModel() {
        try {
            stockNumberService.updateStockNumberByProOfferId(listRangeGrantSelected, BccsLoginSuccessHandler.getStaffDTO().getStaffCode(), BccsLoginSuccessHandler.getIpAddress(), assignIdsnTag.getProductOfferingTotalNewDTO(), false);
            assignIdsnTag.setProductOfferingTotalNewDTO(new ProductOfferingTotalDTO());
            reportSuccess("", "search.number.range.update.success");
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", ex.getKeyMsg());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happened");
        }
        topPage();
        return;
    }

    @Secured("@")
    public void doUpdateModelFromFile() {
        try {
            stockNumberService.updateStockNumberByProOfferId(listRangeGrantSelected, BccsLoginSuccessHandler.getStaffDTO().getStaffCode(), BccsLoginSuccessHandler.getIpAddress(), null, true);
//            assignIdsnTag.setProductOfferingTotalNewDTO(new ProductOfferingTotalDTO());
            reportSuccess("", "search.number.range.update.success");
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", ex.getKeyMsg());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happened");
        }
        topPage();
        return;
    }

    @Secured("@")
    public String getConfirmMessage() {
        if (!DataUtil.isNullObject(assignIdsnTag.getProductOfferingTotalNewDTO())
                && !DataUtil.isNullOrEmpty(assignIdsnTag.getProductOfferingTotalNewDTO().getName())) {
            return getText("mn.stock.utilities.update.model.confim").replace("{0}", assignIdsnTag.getProductOfferingTotalNewDTO().getName());
        } else {
            return null;
        }
    }


    @Secured("@")
    public void doConfirmModelUpdate() {
        if (DataUtil.isNullOrEmpty(listRangeGrantSelected)) {
            reportError("", "", "mn.stock.status.isdn.update.noselect");
            return;
        }

    }

    @Secured("@")
    public void doReset() {

        listRangeGrantSelected = Lists.newArrayList();
        listRangeGrant = Lists.newArrayList();
        assignIdsnTag.setSearchStockNumberDTO(new SearchNumberRangeDTO());
        assignIdsnTag.setUploadedFile(null);
        assignIdsnTag.setHasFileError(false);
        curShopStaff = new VShopStaffDTO();
        assignIdsnTag.setFileName(null);
        assignIdsnTag.setByteContent(null);
        assignIdsnTag.setProductOfferingTotalDTO(new ProductOfferingTotalDTO());
        assignIdsnTag.setProductOfferingTotalNewDTO(new ProductOfferingTotalDTO());
    }

    @Secured("@")
    public void onTabChange(TabChangeEvent event) {
        TabView tabView = (TabView) event.getComponent();
        tabIndex = tabView.getChildren().indexOf(event.getTab());
        doReset();
        listRangeGrant = Lists.newArrayList();
        if (tabIndex == 0) {
            assignIdsnTag.setRenderedFromFile(false);
        } else {
            assignIdsnTag.setRenderedFromFile(true);
        }
    }


    @Secured("@")
    public String getTelecomServiceName(String id) {
        if (!DataUtil.isNullOrEmpty(assignIdsnTag.getOptionSetValueDTOs())) {
            for (OptionSetValueDTO dto : assignIdsnTag.getOptionSetValueDTOs()) {
                if (com.google.common.base.Objects.equal(dto.getValue(), id)) {
                    return dto.getName();
                }
            }
        }
        return id;
    }

    public List<TableNumberRangeDTO> getListRangeGrant() {
        return listRangeGrant;
    }

    public void setListRangeGrant(List<TableNumberRangeDTO> listRangeGrant) {
        this.listRangeGrant = listRangeGrant;
    }

    public List<TableNumberRangeDTO> getListRangeGrantSelected() {
        return listRangeGrantSelected;
    }

    public void setListRangeGrantSelected(List<TableNumberRangeDTO> listRangeGrantSelected) {
        this.listRangeGrantSelected = listRangeGrantSelected;
    }

    public AssignIsdnTagNameable getAssignIdsnTag() {
        return assignIdsnTag;
    }

    public void setAssignIdsnTag(AssignIsdnTagNameable assignIdsnTag) {
        this.assignIdsnTag = assignIdsnTag;
    }

    public int getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    public VShopStaffDTO getCurShopStaff() {
        return curShopStaff;
    }

    public void setCurShopStaff(VShopStaffDTO curShopStaff) {
        this.curShopStaff = curShopStaff;
    }
}
