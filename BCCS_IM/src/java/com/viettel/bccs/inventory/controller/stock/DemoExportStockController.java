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
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.RequiredRoleMap;
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
 * Created by hoangnt14 on 5/5/2017.
 */
@Component
@Scope("view")
@ManagedBean
public class DemoExportStockController extends TransCommonController {
    private StockTransDTO transInputDTO;
    private StaffDTO staffDTO;
    private boolean created = false;
    private ConfigListProductTagDTO configListProductTagDTO;
    private List<ReasonDTO> reasonList;

    @Autowired
    private ListProductNameable listProductTag;//khai bao tag danh sach hang hoa
    @Autowired
    private OptionSetValueService optionSetValueSv;
    @Autowired
    private ExecuteStockTransService executeStockTransService;
    @Autowired
    ReasonService reasonSv;
    @Autowired
    OptionSetValueService optionSetValueService;

    @PostConstruct
    public void init() {
        try {
            created = false;
            staffDTO = BccsLoginSuccessHandler.getStaffDTO();
            resetOrderStock();
            List<OptionSetValueDTO> lstCogfigDemo = optionSetValueService.getByOptionSetCode("PRODUCT_OFFER_DEMO");
            if (DataUtil.isNullOrEmpty(lstCogfigDemo)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.demo.stock.no.config");
            }
            reasonList = reasonSv.getLsReasonByType("EXPORT_DEMO");
            configListProductTagDTO = new ConfigListProductTagDTO(Const.MODE_SERIAL.MODE_ADD_ALL, staffDTO.getStaffId(), Const.OWNER_TYPE.STAFF);
            configListProductTagDTO.setShowTotalPrice(false);
            configListProductTagDTO.setShowRetrive(false);
            configListProductTagDTO.setStockDemo(Const.STOCK_DEMO.EXPORT);
            //init cho tag list danh sach hang hoa
            listProductTag.init(this, configListProductTagDTO);

            //gan lai list trang thai hang moi value = 1, thu hoi value =4
            List<String> list = Lists.newArrayList();
            list.add(DataUtil.safeToString(Const.GOODS_STATE.NEW));
            list.add(DataUtil.safeToString(Const.GOODS_STATE.RETRIVE));
            List<OptionSetValueDTO> lsProductStatus = DataUtil.defaultIfNull(optionSetValueSv.getStatusOptionSetValueByStockState(Const.OPTION_SET.GOODS_STATE, list), new ArrayList<OptionSetValueDTO>());
            listProductTag.setLsProductStatus(lsProductStatus);
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
        topPage();
    }

    public void resetOrderStock() {
        try {
            transInputDTO = new StockTransDTO();
            transInputDTO.setFromOwnerId(staffDTO.getStaffId());
            transInputDTO.setFromOwnerType(Const.OWNER_TYPE.STAFF_LONG);
            transInputDTO.setFromOwnerType(DataUtil.safeToLong(Const.OWNER_TYPE.STAFF));
            transInputDTO.setToOwnerType(DataUtil.safeToLong(Const.OWNER_TYPE.CUSTOMER));
            transInputDTO.setToOwnerId(Const.OWNER_CUST_ID);
            transInputDTO.setCreateDatetime(optionSetValueService.getSysdateFromDB(true));
            transInputDTO.setUserCreate(staffDTO.getStaffCode());
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
    }

    @Secured("@")
    public void resetForm() {
        init();
    }

    @Secured("@")
    public void addExportStock() {
        try {
            List<StockTransDetailDTO> stockTransDetailDTOs = listProductTag.getListTransDetailDTOs();
            StockTransActionDTO transActionDTO = new StockTransActionDTO();
            transActionDTO.setActionCode(transInputDTO.getActionCode());
            transActionDTO.setCreateUser(staffDTO.getStaffCode());
            transActionDTO.setActionStaffId(staffDTO.getStaffId());

            BaseMessageStockTrans message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.EXPORT_DEMO, Const.STOCK_TRANS_TYPE.EXPORT, transInputDTO, transActionDTO, stockTransDetailDTOs, null);
            if (!DataUtil.isNullOrEmpty(message.getErrorCode())) {
                throw new LogicException(message.getErrorCode(), message.getKeyMsg(), message.getParamsMsg());
            }
            reportSuccess("", "stock.demo.export.sussces");
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
    public void validateExportStock() {
        try {

            List<ListProductOfferDTO> lsListProductOfferDTO = listProductTag.getLsListProductOfferDTO();
            if (DataUtil.isNullOrEmpty(lsListProductOfferDTO)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.rescueInformation.validate.list");
            }
            for (ListProductOfferDTO listProductOfferDTO : lsListProductOfferDTO) {
                if (DataUtil.isNullObject(listProductOfferDTO.getProductOfferingTotalDTO())) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.isdn.manage.create.field.validate.productOffer");
                }
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

    public List<ReasonDTO> getReasonList() {
        return reasonList;
    }

    public void setReasonList(List<ReasonDTO> reasonList) {
        this.reasonList = reasonList;
    }
}
