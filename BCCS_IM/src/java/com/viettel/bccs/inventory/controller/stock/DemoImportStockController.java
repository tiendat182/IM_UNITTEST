package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.ExecuteStockTransService;
import com.viettel.bccs.inventory.service.OptionSetValueService;
import com.viettel.bccs.inventory.service.ReasonService;
import com.viettel.bccs.inventory.tag.ListProductNameable;
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
import java.util.List;

/**
 * Created by hoangnt14 on 5/9/2017.
 */
@Component
@Scope("view")
@ManagedBean
public class DemoImportStockController extends TransCommonController {
    private StockTransDTO transInputDTO;
    private StaffDTO staffDTO;
    private List<ReasonDTO> reasonList;
    private boolean created = false;
    private ConfigListProductTagDTO configListProductTagDTO;

    @Autowired
    private StaffInfoNameable staffInfoReceiveTag;
    @Autowired
    private ListProductNameable listProductTag;//khai bao tag danh sach hang hoa
    @Autowired
    private OptionSetValueService optionSetValueSv;
    @Autowired
    ReasonService reasonSv;
    @Autowired
    private ExecuteStockTransService executeStockTransService;


    @PostConstruct
    public void init() {
        try {
            created = false;
            staffDTO = BccsLoginSuccessHandler.getStaffDTO();
            reasonList = reasonSv.getLsReasonByType("IMPORT_DEMO");
            resetRetrieveStock();
            List<String> lstChanelTypeId = Lists.newArrayList();
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
    public void addRetrieveStock() {
        try {
            List<StockTransDetailDTO> stockTransDetailDTOs = listProductTag.getListTransDetailDTOs();
            for (StockTransDetailDTO stockTransDetailDTO : stockTransDetailDTOs) {
                stockTransDetailDTO.setStateId(DataUtil.safeToLong(stockTransDetailDTO.getStrStateIdAfter()));
            }
            StockTransActionDTO transActionDTO = new StockTransActionDTO();
            transActionDTO.setActionCode(transInputDTO.getActionCode());
            transActionDTO.setCreateUser(staffDTO.getStaffCode());
            transActionDTO.setActionStaffId(staffDTO.getStaffId());

            BaseMessageStockTrans message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.EXPORT_DEMO, Const.STOCK_TRANS_TYPE.IMPORT, transInputDTO, transActionDTO, stockTransDetailDTOs, null);
            if (!DataUtil.isNullOrEmpty(message.getErrorCode())) {
                throw new LogicException(message.getErrorCode(), message.getKeyMsg(), message.getParamsMsg());
            }
            reportSuccess("", "stock.demo.import.sussces");
            created = true;
            topPage();
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
    public void validateRetrieveStock() {
        try {

            List<ListProductOfferDTO> lsListProductOfferDTO = listProductTag.getLsListProductOfferDTO();
            if (DataUtil.isNullOrEmpty(lsListProductOfferDTO)) {
                reportError("", "", "stock.rescueInformation.validate.list");
                topPage();
            } else {
                for (ListProductOfferDTO listProductOfferDTO : lsListProductOfferDTO) {
                    if (DataUtil.isNullObject(listProductOfferDTO.getProductOfferingTotalDTO())) {
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.isdn.manage.create.field.validate.productOffer");
                    }
                }
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

    public void createInit(Long ownerId, String ownerType) throws Exception {
        configListProductTagDTO = new ConfigListProductTagDTO(Const.PRODUCT_OFFERING._CHECK_SERIAL, ownerId
                , ownerType, Const.GOODS_STATE.DEMO);
        configListProductTagDTO.setValidateAvaiableQuantity(false);
        configListProductTagDTO.setShowTotalPrice(false);
        configListProductTagDTO.setStockDemo(Const.STOCK_DEMO.IMPORT);
        listProductTag.init(listProductTag, configListProductTagDTO);
        listProductTag.setShowForRetrieveStock(true);
        //gan lai list trang thai hang demo
        List<String> list = Lists.newArrayList();
        list.add(DataUtil.safeToString(Const.GOODS_STATE.DEMO));
        List<OptionSetValueDTO> lsProductStatus = DataUtil.defaultIfNull(optionSetValueSv.getStatusOptionSetValueByStockState(Const.OPTION_SET.GOODS_STATE, list), new ArrayList<OptionSetValueDTO>());
        listProductTag.setLsProductStatus(lsProductStatus);
        // gan lai list cho hang hong = 3 va hang thu hoi =4
        list.clear();
        list.add(DataUtil.safeToString(Const.GOODS_STATE.BROKEN));
        list.add(DataUtil.safeToString(Const.GOODS_STATE.RETRIVE));
        List<OptionSetValueDTO> lsProductStatusAfter = DataUtil.defaultIfNull(optionSetValueSv.getStatusOptionSetValueByStockState(Const.OPTION_SET.GOODS_STATE, list), new ArrayList<OptionSetValueDTO>());
        listProductTag.setLsProductStatusAfter(lsProductStatusAfter);

    }

    public void resetRetrieveStock() {
        try {
            transInputDTO = new StockTransDTO();
            transInputDTO.setToOwnerId(staffDTO.getStaffId());
            transInputDTO.setToOwnerType(DataUtil.safeToLong(Const.OWNER_TYPE.STAFF));
            transInputDTO.setFromOwnerType(DataUtil.safeToLong(Const.OWNER_TYPE.CUSTOMER));
            transInputDTO.setFromOwnerId(Const.OWNER_CUST_ID);
            transInputDTO.setCreateDatetime(optionSetValueSv.getSysdateFromDB(true));
            transInputDTO.setUserCreate(staffDTO.getStaffCode());
            createInit(staffDTO.getStaffId(), Const.OWNER_TYPE.STAFF);
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

    @Secured("@")
    public void receiveStaff(VShopStaffDTO dto) {
        transInputDTO.setToOwnerId(DataUtil.safeToLong(dto.getOwnerId()));
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

    public StockTransDTO getTransInputDTO() {
        return transInputDTO;
    }

    public void setTransInputDTO(StockTransDTO transInputDTO) {
        this.transInputDTO = transInputDTO;
    }

    @Override
    public StaffDTO getStaffDTO() {
        return staffDTO;
    }

    public void setStaffDTO(StaffDTO staffDTO) {
        this.staffDTO = staffDTO;
    }

    public StaffInfoNameable getStaffInfoReceiveTag() {
        return staffInfoReceiveTag;
    }

    public void setStaffInfoReceiveTag(StaffInfoNameable staffInfoReceiveTag) {
        this.staffInfoReceiveTag = staffInfoReceiveTag;
    }

    public List<ReasonDTO> getReasonList() {
        return reasonList;
    }

    public void setReasonList(List<ReasonDTO> reasonList) {
        this.reasonList = reasonList;
    }

    public ListProductNameable getListProductTag() {
        return listProductTag;
    }

    public void setListProductTag(ListProductNameable listProductTag) {
        this.listProductTag = listProductTag;
    }

    public boolean isCreated() {
        return created;
    }

    public void setCreated(boolean created) {
        this.created = created;
    }
}
