package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.inventory.tag.ListProductNameable;
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
import java.util.stream.Collectors;

/**
 * Created by tuyendv8 on 11/14/2015.
 */
@Component
@Scope("view")
@ManagedBean
public class RescueInforExportStockController extends TransCommonController {
    private StockTransDTO stockTransDTO;
    private List<ReasonDTO> reasonList;
    private List<StockTransDetailDTO> listStockTransDetail = Lists.newArrayList();
    private List<ProductOfferTypeDTO> listProductOfferType = Lists.newArrayList();
    List<ProductOfferingDTO> listProductOffering;
    private ProductOfferingDTO productOfferingDTO;
    private ProductOfferingDTO stockRecoverCode;
    private boolean render;
    private String serialRecover;
    private ProductOfferTypeDTO productOfferTypeDTO;
    private StaffDTO staffDTO;
    private Date currentDate;
    private StockTransInputDTO transInputDTO = new StockTransInputDTO();
    private String codeAndName;
    private boolean created = false;
    private ConfigListProductTagDTO configListProductTagDTO;
    private String valueReasonId;
    @Autowired
    private ListProductNameable listProductTag;//khai bao tag danh sach hang hoa
    @Autowired
    ReasonService reasonSv;
    @Autowired
    ProductOfferTypeService proOfferTypeSv;
    @Autowired
    ProductOfferingService productOfferingSv;
    @Autowired
    private OptionSetValueService optionSetValueSv;
    @Autowired
    private RescueInformationExportStockService rescueInformationExportStockService;

    @PostConstruct
    public void init() {
        try {
            created = false;
            staffDTO = BccsLoginSuccessHandler.getStaffDTO();
            stockTransDTO = new StockTransDTO();
            listStockTransDetail = Lists.newArrayList();
            reasonList = reasonSv.getLsReasonByType(Const.REASON_TYPE.EXPORT_RESCUE);

            listProductOffering = new ArrayList<>();
            render = false;
            if (currentDate == null) {
                currentDate = optionSetValueSv.getSysdateFromDB(true);
            }
            stockTransDTO.setCreateDatetime(currentDate);
            resetOrderStock();
            configListProductTagDTO = new ConfigListProductTagDTO(Const.MODE_SERIAL.MODE_ADD_ALL, staffDTO.getStaffId(), Const.OWNER_TYPE.STAFF);
            configListProductTagDTO.setShowTotalPrice(false);
            configListProductTagDTO.setShowRetrive(false);
            //init cho tag list danh sach hang hoa
            listProductTag.init(this, configListProductTagDTO);

            //gan lai list trang thai hang moi value = 1, thu hoi value =4
            List<String> list = Lists.newArrayList();
            list.add(DataUtil.safeToString(Const.GOODS_STATE.NEW));
            list.add(DataUtil.safeToString(Const.GOODS_STATE.RETRIVE));
            List<OptionSetValueDTO> lsProductStatus = DataUtil.defaultIfNull(optionSetValueSv.getStatusOptionSetValueByStockState(Const.OPTION_SET.STOCK_STATE, list), new ArrayList<OptionSetValueDTO>());
            listProductTag.setLsProductStatus(lsProductStatus);
            executeCommand("updateControls();");
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
    public List<ProductOfferingDTO> searchListProductOffering(String input) {
        try {
            listProductOffering = DataUtil.defaultIfNull(productOfferingSv.getLsProductOfferDTOByType(input, Lists.newArrayList(Const.STOCK_TYPE.STOCK_HANDSET)), new ArrayList<>());
        } catch (LogicException lex) {
            reportError(lex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex);
            topPage();
        }
        return listProductOffering;
    }

    /*
  Tuydv1: reset autoCcmplate
   */
    @Secured("@")
    public void resetListProductOffering() {
        stockRecoverCode = null;
    }

    @Secured("@")
    public void changeRenderExport() {
        render = false;
        if(!DataUtil.isNullObject(valueReasonId)){
            transInputDTO.setReasonId(DataUtil.safeToLong(valueReasonId));
            for (ReasonDTO dto : reasonList) {
                if (dto.getReasonId().equals(DataUtil.safeToLong(valueReasonId))) {
                    if (!DataUtil.isNullObject(dto.getReasonCode()) && (Const.PRODUCT_OFFERING.EXPORT_RESCUE_INFO_2).equals(dto.getReasonCode())) {
                        render = true;
                    }
                }
            }
        }else{
            transInputDTO.setReasonId(null);
        }
        configListProductTagDTO.setShowRetrive(render);
    }

    @Secured("@")
    public void validateExportStock() {
        try {

            List<ListProductOfferDTO> lsListProductOfferDTO = listProductTag.getLsListProductOfferDTO();
            if (DataUtil.isNullOrEmpty(lsListProductOfferDTO)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.rescueInformation.validate.list");
            }
            if (render && (lsListProductOfferDTO.size() > 1 ||
                    (lsListProductOfferDTO.size() == 1 && DataUtil.safeToInt(lsListProductOfferDTO.get(0).getNumber()) > 1))) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.rescueInformation.validate.list.resontrue");
            }
            if (lsListProductOfferDTO.size() >= 2 && validateProductOfferingState(lsListProductOfferDTO)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.rescueInformation.validate.list.duplicate");
            }
            List<StockTransDetailDTO> lsStockTransDetail = listProductTag.getListTransDetailDTOs();
            for (StockTransDetailDTO stockTransDetailDTO : lsStockTransDetail) {
                if (DataUtil.isNullOrEmpty(stockTransDetailDTO.getLstStockTransSerial()) && (DataUtil.safeToLong(Const.PRODUCT_OFFERING._CHECK_SERIAL)).equals(stockTransDetailDTO.getCheckSerial())) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.confirm.serial.empty", stockTransDetailDTO.getProdOfferName());
                }
            }
            doValidateListDetail(lsStockTransDetail);
        } catch (LogicException lex) {
            reportError(lex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex);
            topPage();
        }
    }

    @Secured("@")
    public void addExportStock() {
        try {
            List<ListProductOfferDTO> lsListProductOfferDTO = listProductTag.getLsListProductOfferDTO();
            List<StockTransDetailDTO> lsStockTransDetail = listProductTag.getListTransDetailDTOs();
            listProductOfferType = listProductTag.getLsProductOfferTypeDTOTmp();
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
                    BaseMessage message = rescueInformationExportStockService.validateStockTransForExport(productOfferingId, state, unit, avaiableQuantity, quantity, serialRecover);
                    if (!DataUtil.isNullOrEmpty(message.getErrorCode())) {
                        throw new LogicException(message.getErrorCode(), message.getKeyMsg());
                    }
                }
                String shopCode = staffDTO.getStaffCode();
                ProductOfferingDTO stockRecoverCodeDTO = new ProductOfferingDTO();
                String strSerialRecover = null;
                if (render) {
                    stockRecoverCodeDTO = DataUtil.cloneBean(stockRecoverCode);
                    strSerialRecover = serialRecover;
                }
                transInputDTO.setFromOwnerId(staffDTO.getStaffId());
                BaseMessage message = rescueInformationExportStockService.executeStockTransForExport(transInputDTO, shopCode, stockRecoverCodeDTO, strSerialRecover, lsStockTransDetail, render);
                if (!DataUtil.isNullOrEmpty(message.getErrorCode())) {
                    throw new LogicException(message.getErrorCode(), message.getKeyMsg());
                } else {
                    reportSuccess("", "stock.rescueInformation.export.sussces");
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

    @Secured("@")
    public void resetForm() {
        init();
    }


    // check validate
    private boolean validateProductOfferingState(List<ListProductOfferDTO> lsListProductOfferDTO) {
        if (!DataUtil.isNullOrEmpty(lsListProductOfferDTO)) {
            int size = lsListProductOfferDTO.size();
            for (int i = 0; i < size; i++) {
                for (int j = i + 1; j < size; j++) {
                    ListProductOfferDTO dtoBefor = lsListProductOfferDTO.get(i);
                    ListProductOfferDTO dtoAffer = lsListProductOfferDTO.get(j);
                    if (!DataUtil.isNullObject(dtoAffer.getProductOfferingTotalDTO())) {
                        if (DataUtil.safeEqual(dtoBefor.getProductOfferingTotalDTO().getProductOfferingId(), dtoAffer.getProductOfferingTotalDTO().getProductOfferingId())
                                && DataUtil.safeEqual(dtoBefor.getStrStateId(), dtoAffer.getStrStateId())) {
                            return true;
                        }

                    }
                }
            }
        }
        return false;
    }

    public void resetOrderStock() {
        transInputDTO = new StockTransInputDTO();
        transInputDTO.setFromOwnerId(staffDTO.getUserId());
        transInputDTO.setFromOwnerType(DataUtil.safeToLong(Const.OWNER_TYPE.STAFF));
        transInputDTO.setToOwnerType(DataUtil.safeToLong(Const.OWNER_TYPE.CUSTOMER));
        transInputDTO.setCreateUser(staffDTO.getStaffCode());
        transInputDTO.setActionStaffId(staffDTO.getStaffId());
        transInputDTO.setCreateDatetime(currentDate);
        stockRecoverCode = null;
        serialRecover = null;
    }

    @Secured("@")
    public String getCodeAndName() {
        String code = staffDTO.getStaffCode();
        String name = staffDTO.getName();
        return code + "-" + name;

    }
    //tuydv1 getter and setter

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

    public List<StockTransDetailDTO> getListStockTransDetail() {
        return listStockTransDetail;
    }

    public void setListStockTransDetail(List<StockTransDetailDTO> listStockTransDetail) {
        this.listStockTransDetail = listStockTransDetail;
    }

    public ProductOfferingDTO getProductOfferingDTO() {
        return productOfferingDTO;
    }

    public void setProductOfferingDTO(ProductOfferingDTO productOfferingDTO) {
        this.productOfferingDTO = productOfferingDTO;
    }

    public ProductOfferingDTO getStockRecoverCode() {
        return stockRecoverCode;
    }

    public void setStockRecoverCode(ProductOfferingDTO stockRecoverCode) {
        this.stockRecoverCode = stockRecoverCode;
    }

    public boolean isRender() {
        return render;
    }

    public void setRender(boolean render) {
        this.render = render;
    }

    public String getSerialRecover() {
        return serialRecover;
    }

    public void setSerialRecover(String serialRecover) {
        this.serialRecover = serialRecover;
    }

    public ProductOfferTypeDTO getProductOfferTypeDTO() {
        return productOfferTypeDTO;
    }

    public void setProductOfferTypeDTO(ProductOfferTypeDTO productOfferTypeDTO) {
        this.productOfferTypeDTO = productOfferTypeDTO;
    }

    public List<ProductOfferingDTO> getListProductOffering() {
        return listProductOffering;
    }

    public void setListProductOffering(List<ProductOfferingDTO> listProductOffering) {
        this.listProductOffering = listProductOffering;
    }

    public List<ProductOfferTypeDTO> getListProductOfferType() {
        return listProductOfferType;
    }

    public void setListProductOfferType(List<ProductOfferTypeDTO> listProductOfferType) {
        this.listProductOfferType = listProductOfferType;
    }

    public ListProductNameable getListProductTag() {
        return listProductTag;
    }

    public void setListProductTag(ListProductNameable listProductTag) {
        this.listProductTag = listProductTag;
    }

    public StockTransInputDTO getTransInputDTO() {
        return transInputDTO;
    }

    public void setTransInputDTO(StockTransInputDTO transInputDTO) {
        this.transInputDTO = transInputDTO;
    }

    public void setCodeAndName(String codeAndName) {
        this.codeAndName = codeAndName;
    }

    public boolean isCreated() {
        return created;
    }

    public void setCreated(boolean created) {
        this.created = created;
    }

    public String getValueReasonId() {
        return valueReasonId;
    }

    public void setValueReasonId(String valueReasonId) {
        this.valueReasonId = valueReasonId;
    }
}
