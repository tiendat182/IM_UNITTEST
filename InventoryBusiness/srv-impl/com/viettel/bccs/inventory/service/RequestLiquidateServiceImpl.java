package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.RequestLiquidate;
import com.viettel.bccs.inventory.repo.RequestLiquidateRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.GetTextFromBundleHelper;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.WebMethod;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@Service
public class RequestLiquidateServiceImpl extends BaseServiceImpl implements RequestLiquidateService {

    private final BaseMapper<RequestLiquidate, RequestLiquidateDTO> mapper = new BaseMapper<>(RequestLiquidate.class, RequestLiquidateDTO.class);

    @Autowired
    private RequestLiquidateRepo repository;
    public static final Logger logger = Logger.getLogger(RequestLiquidateService.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;
    @Autowired
    RequestLiquidateDetailService requestLiquidateDetailService;
    @Autowired
    RequestLiquidateSerialService requestLiquidateSerialService;
    @Autowired
    ShopService shopService;
    @Autowired
    StaffService staffService;
    @Autowired
    ProductOfferingService productOfferingService;
    @Autowired
    StockTransService stockTransService;
    @Autowired
    StockTransDetailService stockTransDetailService;
    @Autowired
    StockTransSerialService stockTransSerialService;
    @Autowired
    StockTransActionService stockTransActionService;
    @Autowired
    StockTotalService stockTotalService;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public RequestLiquidateDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<RequestLiquidateDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<RequestLiquidateDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public RequestLiquidateDTO save(RequestLiquidateDTO dto) throws Exception {
        return mapper.toDtoBean(repository.save(mapper.toPersistenceBean(dto)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage doSaveLiquidate(RequestLiquidateDTO requestLiquidateDTO) throws LogicException,Exception {
        BaseMessage msg = new BaseMessage();
        //Validate
        if (DataUtil.isNullObject(requestLiquidateDTO)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.liquidate.propose.invalid.msg");
        }
        if (DataUtil.isNullOrEmpty(requestLiquidateDTO.getRequestCode())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "mn.stock.liquidate.propose.code");
        }
        String test = repository.getRequestCode();
        if (DataUtil.isNullObject(test) || !test.equals(requestLiquidateDTO.getRequestCode())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.liquidate.propose.code.invalid.msg");
        }
        if (DataUtil.isNullOrEmpty(requestLiquidateDTO.getLstRequestLiquidateDetailDTO())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.liquidate.list.product.require.msg");
        }
        Date sysdate = DbUtil.getSysDate(em);
        //Validate kho xuat
        if (DataUtil.isNullObject(requestLiquidateDTO.getCreateShopId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.liquidate.export.stock.invalid");
        }
        ShopDTO shopDTO = shopService.findOne(requestLiquidateDTO.getCreateShopId());
        if (DataUtil.isNullObject(shopDTO)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.liquidate.export.stock.invalid");
        }
        //Ma kho xuat phai la kho cua nhan vien dang nhap
        if (DataUtil.isNullObject(requestLiquidateDTO.getCreateStaffId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.liquidate.create.user.invalid");
        }
        StaffDTO staffDTO = staffService.findOne(requestLiquidateDTO.getCreateStaffId());
        if (DataUtil.isNullObject(staffDTO)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.liquidate.create.user.invalid");
        }
        if (!DataUtil.safeEqual(shopDTO.getShopId(), staffDTO.getShopId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.liquidate.export.stock.not.exist");
        }
        //Voi mat hang serial phai chon serial
        for (RequestLiquidateDetailDTO requestLiquidateDetailDTO : requestLiquidateDTO.getLstRequestLiquidateDetailDTO()) {
            ProductOfferingDTO productOffering = productOfferingService.findOne(requestLiquidateDetailDTO.getProdOfferId());
            if (DataUtil.safeEqual(productOffering.getCheckSerial(), Const.PRODUCT_OFFERING.CHECK_SERIAL)) {
                if (DataUtil.isNullOrEmpty(requestLiquidateDetailDTO.getLstRequestLiquidateSerialDTO())) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.serial.empty");
                }
            }
            //Neu don vi ton tai mat hang dang cho thanh ly thi khong duoc lap yeu cau
            if(repository.checkProdExist(requestLiquidateDTO.getCreateShopId(),requestLiquidateDetailDTO.getProdOfferId())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.liquidate.product.exists", productOffering.getCode());
            }
        }


        //Save bang request_liquidate
        requestLiquidateDTO.setCreateDatetime(sysdate);
        requestLiquidateDTO.setUpdateDatetime(sysdate);
        RequestLiquidateDTO requestLiquidate = save(requestLiquidateDTO);
        //Save bang request_liquidate_detail
        for (RequestLiquidateDetailDTO requestLiquidateDetailDTO : requestLiquidateDTO.getLstRequestLiquidateDetailDTO()) {
            requestLiquidateDetailDTO.setCreateDatetime(sysdate);
            requestLiquidateDetailDTO.setRequestLiquidateId(requestLiquidate.getRequestLiquidateId());
            RequestLiquidateDetailDTO requestLiquidateDetail = requestLiquidateDetailService.save(requestLiquidateDetailDTO);
            //Save bang request_liquidate_serial
            if (!DataUtil.isNullOrEmpty(requestLiquidateDetailDTO.getLstRequestLiquidateSerialDTO())) {
                for (RequestLiquidateSerialDTO requestLiquidateSerialDTO : requestLiquidateDetailDTO.getLstRequestLiquidateSerialDTO()) {
                    requestLiquidateSerialDTO.setRequestLiquidateId(requestLiquidate.getRequestLiquidateId());
                    requestLiquidateSerialDTO.setRequestLiquidateDetailId(requestLiquidateDetail.getRequestLiquidateDetailId());
                    requestLiquidateSerialDTO.setCreateDatetime(sysdate);
                    requestLiquidateSerialService.save(requestLiquidateSerialDTO);
                }
            }
        }
        return msg;
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public void updateRequest(RequestLiquidateDTO requestLiquidateDTO) throws LogicException, Exception {
        if (DataUtil.isNullObject(requestLiquidateDTO) || DataUtil.isNullObject(requestLiquidateDTO.getRequestLiquidateId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.liquidate.approve.selected.require");
        }
        RequestLiquidateDTO requestLiquidate = findOne(requestLiquidateDTO.getRequestLiquidateId());
        if (DataUtil.isNullObject(requestLiquidate)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.liquidate.approve.selected.empty");
        }
        if (!DataUtil.safeEqual(Const.LIQUIDATE_STATUS.NEW, requestLiquidate.getStatus())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.liquidate.approve.selected.invalid");
        }
        Date sysdate = DbUtil.getSysDate(em);
        if (DataUtil.safeEqual(Const.LIQUIDATE_STATUS.REJECT, requestLiquidateDTO.getStatus())) {
            if (DataUtil.isNullObject(requestLiquidateDTO.getRejectNote())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.liquidate.approve.reason.reject.require");
            }
            //cap nhat trang thai ve da tu choi
            requestLiquidate.setStatus(Const.LIQUIDATE_STATUS.REJECT);
            requestLiquidate.setApproveStaffId(requestLiquidateDTO.getApproveStaffId());
            requestLiquidate.setRejectNote(requestLiquidateDTO.getRejectNote());
            requestLiquidate.setUpdateDatetime(sysdate);
            save(requestLiquidate);
        } else {
            List<RequestLiquidateDetailDTO> lstRequestLiquidateDetailDTO = requestLiquidateDetailService.getLstRequestLiquidateDetailDTO(requestLiquidateDTO.getRequestLiquidateId());
            if (DataUtil.isNullOrEmpty(lstRequestLiquidateDetailDTO)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.liquidate.approve.selected.empty");
            }
            //Tao giao dich xuat hang cho Doi tac khac
            //Insert cac bang stockTrans, stockTransAction, stockTransDetail, stockTransSerial
            StockTransDTO stockTransDTO = doSaveStockTrans(requestLiquidateDTO);
            stockTransDTO.setStaffId(requestLiquidateDTO.getCreateStaffId());
            stockTransDTO.setUserCreate(requestLiquidateDTO.getCreateUser());
            doSaveStockTransAction(stockTransDTO);
            doSaveStockTransDetail(stockTransDTO, lstRequestLiquidateDetailDTO);
            //Cap nhat giao dich ve trang thai phe duyet
            requestLiquidate.setStatus(Const.LIQUIDATE_STATUS.APPROVE);
            requestLiquidate.setApproveStaffId(requestLiquidateDTO.getApproveStaffId());
            requestLiquidate.setUpdateDatetime(sysdate);
            requestLiquidate.setStockTransId(stockTransDTO.getStockTransId());
            save(requestLiquidate);
        }

    }

    @Override
    public List<RequestLiquidateDTO> getLstRequestLiquidateDTO(RequestLiquidateDTO requestLiquidateDTO) throws LogicException, Exception {
        return mapper.toDtoBean(repository.getLstRequestLiquidate(requestLiquidateDTO));
    }

    @Override
    public byte[] getAttachFileContent(Long requestID) throws LogicException, Exception {
        return repository.getFileContent(requestID);
    }

    @Override
    public String getRequestCode() throws LogicException, Exception {
        return repository.getRequestCode();
    }

    public StockTransDTO doSaveStockTrans(RequestLiquidateDTO requestLiquidateDTO) throws LogicException, Exception {
        Date sysdate = DbUtil.getSysDate(em);
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setFromOwnerId(requestLiquidateDTO.getCreateShopId());
        stockTransDTO.setFromOwnerType(Const.OWNER_TYPE.SHOP_LONG);
        stockTransDTO.setToOwnerId(Const.OTHER_PARTNER_ID);
        stockTransDTO.setToOwnerType(Const.OWNER_TYPE.PARTNER_LONG);
        stockTransDTO.setStockTransType(Const.STOCK_TRANS_TYPE.EXPORT);
        stockTransDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        stockTransDTO.setCreateDatetime(sysdate);
//        stockTransDTO.setTotalAmount(0L);
        stockTransDTO.setReasonId(Const.REASON_ID.EXP_LIQUIDATE);
        stockTransDTO.setNote(GetTextFromBundleHelper.getText("mn.stock.liquidate.approve.stocktrans.note"));
        return stockTransService.save(stockTransDTO);
    }

    public void doSaveStockTransAction(StockTransDTO stockTransDTO) throws LogicException, Exception {
        StaffDTO staff = staffService.findOne(stockTransDTO.getStaffId());
        ShopDTO shop = shopService.findOne(staff.getShopId());
        staff.setShopCode(shop.getShopCode());
        String transCode = staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PX, Const.STOCK_TRANS_TYPE.EXPORT, staff);
        Long num = 0L;
        num = staff.getStockNum() != null ? staff.getStockNum() : num;
        String actionCodeShop = Const.STOCK_TRANS.TRANS_CODE_PX + DataUtil.customFormat("000000", num + 1);
        staff.setStockNum(DataUtil.safeToLong(staff.getStockNum()) + 1);
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setStockTransId(stockTransDTO.getStockTransId());
        stockTransActionDTO.setActionCode(transCode);
        stockTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
        stockTransActionDTO.setCreateUser(stockTransDTO.getUserCreate());
        stockTransActionDTO.setActionStaffId(stockTransDTO.getStaffId());
        stockTransActionDTO.setCreateDatetime(stockTransDTO.getCreateDatetime());
        stockTransActionDTO.setCreateUser(stockTransDTO.getUserCreate());
        stockTransActionDTO.setNote(stockTransDTO.getNote());
        stockTransActionDTO.setActionCodeShop(actionCodeShop);
        stockTransActionService.save(stockTransActionDTO);

        //increase stockNum
        staff.setStockNum(DataUtil.safeToLong(staff.getStockNum()) + 1);
        staffService.save(staff);

        stockTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
        stockTransActionDTO.setActionCode(null);
        stockTransActionDTO.setNote(null);
        stockTransActionDTO.setActionCodeShop(null);
        stockTransActionService.save(stockTransActionDTO);

        stockTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        stockTransActionService.save(stockTransActionDTO);
    }

    public void doSaveStockTransDetail(StockTransDTO stockTransDTO, List<RequestLiquidateDetailDTO> lstRequestLiquidateDetailDTO) throws LogicException, Exception {
        for (RequestLiquidateDetailDTO requestLiquidateDetail : lstRequestLiquidateDetailDTO) {
            StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
            stockTransDetailDTO.setStockTransId(stockTransDTO.getStockTransId());
            stockTransDetailDTO.setProdOfferId(requestLiquidateDetail.getProdOfferId());
            stockTransDetailDTO.setStateId(requestLiquidateDetail.getStateId());
            stockTransDetailDTO.setQuantity(requestLiquidateDetail.getQuantity());
            stockTransDetailDTO.setCreateDatetime(stockTransDTO.getCreateDatetime());
            StockTransDetailDTO stockTransDetail = stockTransDetailService.save(stockTransDetailDTO);
            //Goi ham save bang stock_trans_serial
            List<RequestLiquidateSerialDTO> lstRequestLiquidateSerialDTO = requestLiquidateSerialService.getLstRequestLiquidateSerialDTO(requestLiquidateDetail.getRequestLiquidateDetailId());
            if (!DataUtil.isNullOrEmpty(lstRequestLiquidateSerialDTO)) {
                doSaveStockTransSerial(stockTransDTO, stockTransDetail, lstRequestLiquidateSerialDTO);
            }
            //Cap nhat stock_total
            StockTotalMessage result = stockTotalService.changeStockTotal(stockTransDTO.getFromOwnerId(), stockTransDTO.getFromOwnerType(), stockTransDetailDTO.getProdOfferId(), stockTransDetailDTO.getStateId(),
                    -stockTransDetailDTO.getQuantity(), -stockTransDetailDTO.getQuantity(), 0L, stockTransDTO.getStaffId(), stockTransDTO.getReasonId(), stockTransDTO.getStockTransId(),
                    stockTransDTO.getCreateDatetime(), "", Const.STOCK_TRANS_TYPE.UNIT, Const.SourceType.STOCK_TRANS);
            if (result.isSuccess()) {
                continue;
            } else {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, result.getKeyMsg());
            }
        }
    }

    public void doSaveStockTransSerial(StockTransDTO stockTransDTO, StockTransDetailDTO stockTransDetailDTO, List<RequestLiquidateSerialDTO> lstRequestLiquidateSerialDTO) throws LogicException, Exception {
        int result = 0;
        for (RequestLiquidateSerialDTO requestLiquidateSerialDTO : lstRequestLiquidateSerialDTO) {
            StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
            stockTransSerialDTO.setStockTransDetailId(stockTransDetailDTO.getStockTransDetailId());
            stockTransSerialDTO.setStockTransId(stockTransDetailDTO.getStockTransId());
            stockTransSerialDTO.setProdOfferId(stockTransDetailDTO.getProdOfferId());
            stockTransSerialDTO.setStateId(stockTransDetailDTO.getStateId());
            stockTransSerialDTO.setQuantity(stockTransDetailDTO.getQuantity());
            stockTransSerialDTO.setFromSerial(requestLiquidateSerialDTO.getFromSerial());
            stockTransSerialDTO.setToSerial(requestLiquidateSerialDTO.getToSerial());
            stockTransSerialDTO.setCreateDatetime(stockTransDetailDTO.getCreateDatetime());
            stockTransSerialService.save(stockTransSerialDTO);
            //update trang thai serial thanh da ban
            result += stockTransSerialService.updateStatusStockSerial(stockTransDTO.getFromOwnerId(), stockTransDTO.getFromOwnerType(), stockTransDetailDTO.getProdOfferId(),
                    stockTransSerialDTO.getFromSerial(), stockTransSerialDTO.getToSerial(), Const.STATE_STATUS.SALE, Const.STATE_STATUS.NEW);
        }
        if (result <= 0 || Long.valueOf(result) < stockTransDetailDTO.getQuantity()) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.liquidate.approve.stocktransaction.serial.error");
        }
    }


}
