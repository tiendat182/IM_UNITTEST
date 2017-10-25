package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.OptionSetValueService;
import com.viettel.bccs.inventory.service.ProductOfferingService;
import com.viettel.bccs.inventory.service.ReasonService;
import com.viettel.bccs.inventory.service.RescueInformationExportStockService;
import com.viettel.bccs.inventory.tag.ListProductNameable;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.bccs.inventory.tag.StaffInfoNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.dto.BaseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tuyendv8 on 11/14/2015.
 */
@Component
@Scope("view")
@ManagedBean
public class RescueInforRetrieveStockController extends TransCommonController {
    //Tuydv1
    private StockTransDTO stockTransDTO;
    private List<ReasonDTO> reasonList;
    private StaffDTO staffDTO;
    private Date currentDate;
    private ConfigListProductTagDTO configListProductTagDTO;
    private StockTransInputDTO transInputDTO = new StockTransInputDTO();
    private boolean created = false;
    @Autowired
    private ListProductNameable listProductTag;//khai bao tag danh sach hang hoa

    @Autowired
    ReasonService reasonSv;
    @Autowired
    private ShopInfoNameable shopInfoTag;
    @Autowired
    ProductOfferingService productOfferingSv;
    @Autowired
    private OptionSetValueService optionSetValueSv;

    @Autowired
    private StaffInfoNameable staffInfoTag;
    @Autowired
    private StaffInfoNameable staffInfoReceiveTag;
    @Autowired
    private RescueInformationExportStockService rescueInformationExportStockService;
    private List<String> lstChanelTypeId = Lists.newArrayList();


    @PostConstruct
    public void init() {
        try {
            created = false;
            staffDTO = BccsLoginSuccessHandler.getStaffDTO();
            stockTransDTO = new StockTransDTO();
            reasonList = reasonSv.getLsReasonByType("IMPORT_RESCUE");
            if (currentDate == null) {
                currentDate = optionSetValueSv.getSysdateFromDB(true);
            }

            resetRetrieveStock();
            lstChanelTypeId.add(Const.CHANNEL_TYPE_ID.CHANNEL_TYPE_STAFF);
            lstChanelTypeId.add(Const.CHANNEL_TYPE_ID.CHANNEL_TYPE_INSURRANCE);
            staffInfoReceiveTag.initStaffWithChanelTypes(this, DataUtil.safeToString(staffDTO.getShopId()), null, lstChanelTypeId, false);
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
        topPage();
    }

    @Secured("@")
    public String getCodeAndName() {
        String code = staffDTO.getStaffCode();
        String name = staffDTO.getName();
        return code + "-" + name;

    }


    public void createInit(Long ownerId, String ownerType) throws Exception {
        configListProductTagDTO = new ConfigListProductTagDTO(Const.PRODUCT_OFFERING._CHECK_SERIAL, ownerId
                , ownerType, Const.GOODS_STATE.RESCUE);
        configListProductTagDTO.setValidateAvaiableQuantity(false);
        configListProductTagDTO.setShowTotalPrice(false);
        listProductTag.init(this, configListProductTagDTO);
        listProductTag.setShowForRetrieveStock(true);
        //gan lai list trang thai hang ung cuu. value = 5
        List<String> list = Lists.newArrayList();
        list.add(DataUtil.safeToString(Const.GOODS_STATE.RESCUE));
        List<OptionSetValueDTO> lsProductStatus = DataUtil.defaultIfNull(optionSetValueSv.getStatusOptionSetValueByStockState(Const.OPTION_SET.STOCK_STATE, list), new ArrayList<OptionSetValueDTO>());
        listProductTag.setLsProductStatus(lsProductStatus);
        // gan lai list cho hang hong = 3 va hang thu hoi =4
        list.clear();
        list.add(DataUtil.safeToString(Const.GOODS_STATE.BROKEN));
        list.add(DataUtil.safeToString(Const.GOODS_STATE.RETRIVE));
        List<OptionSetValueDTO> lsProductStatusAfter = DataUtil.defaultIfNull(optionSetValueSv.getStatusOptionSetValueByStockState(Const.OPTION_SET.STOCK_STATE, list), new ArrayList<OptionSetValueDTO>());
        listProductTag.setLsProductStatusAfter(lsProductStatusAfter);


    }

    @Secured("@")
    public void validateRetrieveStock() {
        try {

            List<ListProductOfferDTO> lsListProductOfferDTO = listProductTag.getLsListProductOfferDTO();
            if (DataUtil.isNullOrEmpty(lsListProductOfferDTO)) {
                reportError("", "", "stock.rescueInformation.validate.list");
                topPage();
            } else {
                List<StockTransDetailDTO> lsStockTransDetail = listProductTag.getListTransDetailDTOs();
                for (StockTransDetailDTO stockTransDetailDTO : lsStockTransDetail) {
                    if (DataUtil.isNullOrEmpty(stockTransDetailDTO.getLstStockTransSerial()) && (DataUtil.safeToLong(Const.PRODUCT_OFFERING._CHECK_SERIAL)).equals(stockTransDetailDTO.getCheckSerial())) {
                        reportError("", ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.confirm.serial.empty", stockTransDetailDTO.getProdOfferName());
                        break;
                    }
                }
                doValidateListDetail(lsStockTransDetail);
            }
        } catch (Exception ex) {
            logger.error(ex);
            topPage();
        }

    }

    @Secured("@")
    public void addRetrieveStock() {
        try {
            List<ListProductOfferDTO> lsListProductOfferDTO = listProductTag.getLsListProductOfferDTO();
            List<StockTransDetailDTO> lsStockTransDetail = listProductTag.getListTransDetailDTOs();
            if (!DataUtil.isNullOrEmpty(lsListProductOfferDTO)) {
                int size = lsListProductOfferDTO.size();
                for (int i = 0; i < size; i++) {
                    ListProductOfferDTO dto = lsListProductOfferDTO.get(i);
                    //validate them moi mat hang UCTT
                    Long productOfferingId = null;
                    String unit = null;
                    Long avaiableQuantity = null;

                    if (!DataUtil.isNullObject(dto.getProductOfferingTotalDTO())) {
                        productOfferingId = dto.getProductOfferingTotalDTO().getProductOfferingId();
                        unit = dto.getProductOfferingTotalDTO().getUnitName();
                        avaiableQuantity = dto.getProductOfferingTotalDTO().getAvailableQuantity();


                    }
                    String state = dto.getStrStateId();
                    String quantity = dto.getNumber();
                    BaseMessage message = rescueInformationExportStockService.validateStockTransForExport(productOfferingId, state, unit, avaiableQuantity, quantity, Const.REASON_TYPE.RETRIEVE_STOCK);
                    if (!DataUtil.isNullOrEmpty(message.getErrorCode())) {
                        throw new LogicException(message.getErrorCode(), message.getKeyMsg());
                    }
                }
                    String shopCode = staffDTO.getStaffCode();
//                    ProductOfferingDTO stockRecoverCodeDTO = new ProductOfferingDTO();
                    if (!DataUtil.isNullObject(staffInfoReceiveTag.getvShopStaffDTO()) && !DataUtil.isNullObject(staffInfoReceiveTag.getvShopStaffDTO().getOwnerId())) {
                        transInputDTO.setToOwnerId(DataUtil.safeToLong(staffInfoReceiveTag.getvShopStaffDTO().getOwnerId()));

                    } else {
                        transInputDTO.setToOwnerId(staffDTO.getStaffId());
                    }
                    transInputDTO.setFromOwnerId(staffDTO.getStaffId());

                    BaseMessage message = rescueInformationExportStockService.executeStockTransForExport(transInputDTO, shopCode, null, Const.REASON_TYPE.RETRIEVE_STOCK, lsStockTransDetail, false);
                    if (!DataUtil.isNullOrEmpty(message.getErrorCode())) {
                        throw new LogicException(message.getErrorCode(), message.getKeyMsg());
                    } else {
                        reportSuccess("", "stock.rescueInformation.retrieve.sussces");
                        created = true;
                        topPage();
                    }

            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happen");
            topPage();
        }
    }


    public void resetRetrieveStock() {
        try {
            transInputDTO = new StockTransInputDTO();
            transInputDTO.setToOwnerId(staffDTO.getUserId());
            transInputDTO.setToOwnerType(DataUtil.safeToLong(Const.OWNER_TYPE.STAFF));
            transInputDTO.setFromOwnerType(DataUtil.safeToLong(Const.OWNER_TYPE.CUSTOMER));
            transInputDTO.setCreateUser(staffDTO.getStaffCode());
            transInputDTO.setActionStaffId(staffDTO.getStaffId());
            transInputDTO.setCreateDatetime(currentDate);
            createInit(staffDTO.getStaffId(), Const.OWNER_TYPE.STAFF);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happen");
            topPage();
        }
    }

    @Secured("@")
    public void receiveStaff(VShopStaffDTO dto) {
//        try {
//            if (!DataUtil.isNullObject(dto.getOwnerId())
//                    && !DataUtil.isNullObject(dto.getOwnerType())) {
//                createInit(DataUtil.safeToLong(dto.getOwnerId()), dto.getOwnerType());
//            } else {
//            createInit(staffDTO.getStaffId(), Const.OWNER_TYPE.STAFF);
//            }
//        }catch (Exception ex){
//            logger.error(ex.getMessage(), ex);
//            reportError("", "", "common.error.happen");
//            topPage();
//        }
    }

    @Secured("@")
    public void clearCurrentStaff() {
        try {
            staffInfoReceiveTag.setvShopStaffDTO(null);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happen");
            topPage();
        }
    }

    @Secured("@")
    public void resetForm() {
        created = false;
        resetRetrieveStock();
        clearCurrentStaff();
    }


    //tuydv1

    public StockTransDTO getStockTransDTO() {
        return stockTransDTO;
    }

    public void setStockTransDTO(StockTransDTO stockTransDTO) {
        this.stockTransDTO = stockTransDTO;
    }

    public List<ReasonDTO> getReasonList() {
        return reasonList;
    }

    public void setReasonList(List<ReasonDTO> reasonList) {
        this.reasonList = reasonList;
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

    public StockTransInputDTO getTransInputDTO() {
        return transInputDTO;
    }

    public void setTransInputDTO(StockTransInputDTO transInputDTO) {
        this.transInputDTO = transInputDTO;
    }

    public ListProductNameable getListProductTag() {
        return listProductTag;
    }

    public void setListProductTag(ListProductNameable listProductTag) {
        this.listProductTag = listProductTag;
    }

    public StaffInfoNameable getStaffInfoReceiveTag() {
        return staffInfoReceiveTag;
    }

    public void setStaffInfoReceiveTag(StaffInfoNameable staffInfoReceiveTag) {
        this.staffInfoReceiveTag = staffInfoReceiveTag;
    }

    public boolean isCreated() {
        return created;
    }

    public void setCreated(boolean created) {
        this.created = created;
    }
}
