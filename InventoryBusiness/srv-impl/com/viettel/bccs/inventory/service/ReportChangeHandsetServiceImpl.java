package com.viettel.bccs.inventory.service;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.viettel.bccs.im1.dto.StockModelIm1DTO;
import com.viettel.bccs.im1.repo.StockTotalIm1Repo;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.OptionSetValue;
import com.viettel.bccs.inventory.model.ReportChangeHandset;
import com.viettel.bccs.inventory.repo.OptionSetValueRepo;
import com.viettel.bccs.inventory.repo.ReportChangeHandsetRepo;
import com.viettel.bccs.inventory.repo.StockHandsetRepo;
import com.viettel.bccs.inventory.service.common.IMServiceUtil;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.jws.WebMethod;
import java.util.List;
import java.util.StringJoiner;

@Service
public class ReportChangeHandsetServiceImpl extends BaseServiceImpl implements ReportChangeHandsetService {

    private final BaseMapper<ReportChangeHandset, ReportChangeHandsetDTO> mapper = new BaseMapper<>(ReportChangeHandset.class, ReportChangeHandsetDTO.class);


    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private StockHandsetRepo stockHandsetRepo;
    @Autowired
    private StaffService staffService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private SaleWs saleWs;
    @Autowired
    private OptionSetValueRepo optionSetValueRepo;
    @Autowired
    private StockTotalIm1Repo stockTotalIm1Repo;

    @Autowired
    private ReportChangeHandsetRepo repository;
    public static final Logger logger = Logger.getLogger(ReportChangeHandsetService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public ReportChangeHandsetDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<ReportChangeHandsetDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<ReportChangeHandsetDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public ReportChangeHandsetDTO create(ReportChangeHandsetDTO dto) throws Exception {
        try {
            ReportChangeHandset reportChangeHandset = repository.save(mapper.toPersistenceBean(dto));
            return mapper.toDtoBean(reportChangeHandset);
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(ReportChangeHandsetDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @Override
    public List<ReportChangeHandsetDTO> getLsReportChangeHandsetTerminal(Long prodOfferId, String serial, String shopPath) throws Exception {
        return repository.getLsReportChangeHandsetTerminal(prodOfferId, serial, shopPath);
    }

    @Override
    public List<ReportChangeHandsetDTO> getLsChangeHandsetTerminalDevide(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO,
                                                                         List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {

        StaffDTO staffDTO = staffService.findOne(stockTransActionDTO.getActionStaffId());
        if (staffDTO == null || Const.STATUS.NO_ACTIVE.equals(staffDTO.getStatus())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "staff.code.invalid.name");
        }

        if (Const.L_VT_SHOP_ID.equals(staffDTO.getShopId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.sale.change.terminal.vtt");
        }

        ShopDTO shopDTO = shopService.findOne(staffDTO.getShopId());

        if (shopDTO == null || !Const.STATUS_ACTIVE.equals(shopDTO.getStatus())) {
            throw new LogicException("04", "stock.sale.change.terminal.shopId.user.login");
        }

//        Long changeMachineType = stockTransDTO.getSourceType(); // 2 la doi cung model, 1 la doi khac model
        Long changeMachineType = 2L; // 2 la doi cung model, 1 la doi khac model

        if (!(changeMachineType.compareTo(1L) != 0 || changeMachineType.compareTo(2L) != 0)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.sale.change.terminal.empty.EmpModelType");
        }

        if (DataUtil.isNullOrEmpty(lstStockTransDetail) || lstStockTransDetail.size() != 2) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.sale.change.terminal.stock.detail.list.not.enough");
        }

        StockTransDetailDTO damageDetailDTO = lstStockTransDetail.get(0);
        StockTransDetailDTO changeDetailDTO = lstStockTransDetail.get(1);
        //1.1.1 validate mat hang
        if (damageDetailDTO == null || damageDetailDTO.getProdOfferId() == null) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.sale.change.terminal.empty.product.damage");
        }

        ProductOfferingDTO damageOfferDTO = productOfferingService.findOne(damageDetailDTO.getProdOfferId());
        if (damageOfferDTO == null || !Const.STATUS.ACTIVE.equals(damageOfferDTO.getStatus())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.sale.change.terminal.empty.product.status.damage");
        }
        damageDetailDTO.setTableName(IMServiceUtil.getTableNameByOfferType(damageOfferDTO.getProductOfferTypeId()));
        changeDetailDTO.setTableName(damageDetailDTO.getTableName());

        if (!Const.STOCK_TYPE.STOCK_HANDSET.equals(damageOfferDTO.getProductOfferTypeId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.sale.change.terminal.support.for.handset");
        }

        ProductOfferingDTO changeOfferDTO;

        if (changeDetailDTO.getProdOfferId() == null) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.sale.change.terminal.empty.product.change");
        }
        changeOfferDTO = productOfferingService.findOne(changeDetailDTO.getProdOfferId());

        if (changeOfferDTO == null || !Const.STATUS.ACTIVE.equals(changeOfferDTO.getStatus())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.sale.change.terminal.empty.product.status.change");
        }

        if (!Const.STOCK_TYPE.STOCK_HANDSET.equals(changeOfferDTO.getProductOfferTypeId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.sale.change.terminal.support.for.handset");
        }

        /*if (damageOfferDTO.getProductOfferingId().equals(changeOfferDTO.getProductOfferingId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.sale.change.terminal.ModelMachineIsSame");
        }*/
        changeDetailDTO.setTableName(IMServiceUtil.getTableNameByOfferType(changeOfferDTO.getProductOfferTypeId()));


        if (DataUtil.isNullOrEmpty(damageDetailDTO.getLstSerial())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.sale.change.terminal.empty.serial.damage");
        }
        StockTransSerialDTO damageSerialDTO = damageDetailDTO.getLstSerial().get(0);

        if (damageSerialDTO == null || DataUtil.isNullOrEmpty(damageSerialDTO.getFromSerial()) || DataUtil.isNullOrEmpty(damageSerialDTO.getToSerial())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.sale.change.terminal.empty.serial.damage");
        }

        if (!DataUtil.validateStringByPattern(damageSerialDTO.getFromSerial(), Const.REGEX.SERIAL_REGEX)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.import.to.partner.balance.serial.length");
        }
        if (DataUtil.isNullOrEmpty(changeDetailDTO.getLstSerial())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.sale.change.terminal.empty.serial.change");
        }
        StockTransSerialDTO changeSerialDTO = changeDetailDTO.getLstSerial().get(0);

        if (changeSerialDTO == null || DataUtil.isNullOrEmpty(changeSerialDTO.getFromSerial()) || DataUtil.isNullOrEmpty(changeSerialDTO.getToSerial())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.sale.change.terminal.empty.serial.change");
        }

        if (!DataUtil.validateStringByPattern(changeSerialDTO.getFromSerial(), Const.REGEX.SERIAL_REGEX)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.import.to.partner.balance.serial.length");
        }
        //TH doi hang cung loai thi moi check serial trung nhau
        if (DataUtil.safeEqual(changeOfferDTO.getProductOfferingId(), damageOfferDTO.getProductOfferingId()) && damageSerialDTO.getFromSerial().equals(changeSerialDTO.getFromSerial())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.sale.change.terminal.SerialIsNotSame");
        }


        StockHandsetDTO damageHandsetDTO = stockHandsetRepo.getStockHandset(damageOfferDTO.getProductOfferingId(), damageSerialDTO.getFromSerial(), null, null, null);

        if (damageHandsetDTO == null) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.sale.change.terminal.not.found.serial.in.stock",
                    damageSerialDTO.getFromSerial(), damageOfferDTO.getCode());
        }

        if (!Const.STOCK_GOODS.STATUS_SALE.equals(damageHandsetDTO.getStatus())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.sale.change.terminal.ModelMachineIsNotStatusSaleOrNew.damage", damageSerialDTO.getFromSerial());
        }

        if (!Const.GOODS_STATE.NEW.equals(damageHandsetDTO.getStateId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.sale.change.terminal.ModelMachineIsNotStateSaleOrNew.damage", damageSerialDTO.getFromSerial());
        }

        List<OptionSetValue> lstConfigEnableBccs1 = optionSetValueRepo.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
        if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1)
                && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())
                && Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()))) {
            StockHandsetDTO stockHandsetDTOIm1 = stockTotalIm1Repo.getStockHandsetIm1(damageOfferDTO.getProductOfferingId(), damageSerialDTO.getFromSerial(), null, null, null);

            if (stockHandsetDTOIm1 == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.sale.change.terminal.not.found.serial.in.stock.bccs1",
                        damageSerialDTO.getFromSerial(), damageOfferDTO.getCode());
            }

            if (!Const.STOCK_GOODS.STATUS_SALE.equals(stockHandsetDTOIm1.getStatus())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.sale.change.terminal.ModelMachineIsNotStatusSaleOrNew.damage.bccs1", damageSerialDTO.getFromSerial());
            }

            if (!Const.GOODS_STATE.NEW.equals(stockHandsetDTOIm1.getStateId())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.sale.change.terminal.ModelMachineIsNotStateSaleOrNew.damage.bccs1", damageSerialDTO.getFromSerial());
            }
        }

        StockHandsetDTO changeHandsetDTO = stockHandsetRepo.getStockHandset(changeOfferDTO.getProductOfferingId(), changeSerialDTO.getFromSerial(), null, null, null);

        if (changeHandsetDTO == null) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.sale.change.terminal.not.found.serial.in.stock",
                    changeSerialDTO.getFromSerial(), changeOfferDTO.getCode());
        }

        if (!Const.STOCK_GOODS.STATUS_NEW.equals(changeHandsetDTO.getStateId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.sale.change.terminal.ModelMachineIsNotStatusSaleOrNew.change", changeSerialDTO.getFromSerial());
        }

        if (!Const.GOODS_STATE.NEW.equals(changeHandsetDTO.getStatus())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.sale.change.terminal.ModelMachineIsNotStateSaleOrNew.change", changeSerialDTO.getFromSerial());
        }

        if (DataUtil.isNullOrZero(changeHandsetDTO.getOwnerId()) || !changeHandsetDTO.getOwnerId().equals(staffDTO.getStaffId())
                || DataUtil.isNullOrZero(changeHandsetDTO.getOwnerType()) || !changeHandsetDTO.getOwnerType().equals(Const.OWNER_TYPE.STAFF_LONG)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.sale.change.terminal.ModelMachineIsNotInStock", changeSerialDTO.getFromSerial());
        }
        /*if (isSameModel && !damageHandsetDTO.getProdOfferId().equals(changeHandsetDTO.getProdOfferId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.sale.change.terminal.ModelMachineIsNotSame");
        }*/


        int numberDate = Const.AMOUNT_DAY_TO_CHANGE_HANDSET_DEFAULT;//so ngay duoc phep doi thiet bi 3

        String strNumberDate = optionSetValueRepo.getValueByOptionSetIdAndCode(Const.OPTION_SET.AMOUNT_DAY_TO_CHANGE_HANDSET, Const.OPTION_SET.AMOUNT_DAY_TO_CHANGE_HANDSET);
        if (!DataUtil.isNullOrEmpty(strNumberDate)) {
            numberDate = DataUtil.safeToInt(strNumberDate);
        }
        //goi WS ben sale lay thong tin GD cu
//        ReportChangeHandsetDTO reportChangeHandsetDTO = saleWs.viewChangeGood(damageDetailDTO.getProdOfferId(), damageSerialDTO.getFromSerial(), numberDate, stockTransActionDTO.getActionStaffId(), stockTransActionDTO.getFromDate(), stockTransActionDTO.getToDate());
        ChangeGoodMessage message = saleWs.viewChangeGood(damageDetailDTO.getProdOfferId(), damageSerialDTO.getFromSerial(), numberDate, stockTransActionDTO.getActionStaffId(), stockTransActionDTO.getFromDate(), stockTransActionDTO.getToDate());
        ReportChangeHandsetDTO reportChangeHandsetDTO = null;
        if(message != null && ErrorCode.ERROR_STANDARD.SUCCESS.equals(message.getResponseCode())){
            reportChangeHandsetDTO = message.getReportChangeHandsetDTO();
        }else{
            throw new LogicException(message.getResponseCode(), message.getDescription());
        }
        if (reportChangeHandsetDTO != null) {
//            reportChangeHandsetDTO.setShopName(DataUtil.safeToString(shopDTO.getShopCode()) + "_" + DataUtil.safeToString(shopDTO.getName()));
//            reportChangeHandsetDTO.setProdOfferName(DataUtil.safeToString(damageOfferDTO.getCode()) + "_" + DataUtil.safeToString(damageOfferDTO.getName()));
//            reportChangeHandsetDTO.setStaffName(DataUtil.safeToString(staffDTO.getStaffCode()) + "_" + DataUtil.safeToString(staffDTO.getName()));
            return Lists.newArrayList(reportChangeHandsetDTO);
        }
        return Lists.newArrayList();
    }
}
