package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.im1.service.BaseStockIm1Service;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by tuydv1 on 1/13/2016.
 */
@Service
public class RescueInformationExportStockServiceImpl extends BaseServiceImpl implements RescueInformationExportStockService {

    private static final Pattern PATTERN = Pattern.compile(Const.REGEX.CODE_REGEX);
    public static final Logger logger = Logger.getLogger(RescueInformationExportStockService.class);

    private ProductOfferingDTO productOfferingDTO;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private ReasonService reasonService;
    @Autowired
    private StockHandsetService stockHandsetService;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private StockTransActionService stockTransActionService;
    @Autowired
    private StockTransDetailService stockTransDetailService;
    @Autowired
    private StockHandsetRescueService stockHandsetRescueService;
    @Autowired
    private StockHandsetLendService stockHandsetLendService;
    @Autowired
    private StockTotalService stockTotalService;
    @Autowired
    private StockTransSerialService stockTransSerialService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private StockTotalAuditService stockTotalAuditService;
    @Autowired
    private BaseStockIm1Service baseStockIm1Service;

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;
    private Long productOfferIdUCTT = null;
    private Date currentDate;
    private StaffDTO staffDTO;


    @Override
    public BaseMessage validateStockTransForExport(Long productOfferingId, String state, String unit, Long avaiableQuantity, String quantity, String serialRecover) throws Exception, LogicException {
        BaseMessage result = new BaseMessage(true);
        try {

            //check validate
            if (DataUtil.isNullObject(productOfferingId)) {
                result.setSuccess(false);
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "export.order.stock.inputText.require.msg");
            }
            if (DataUtil.isNullObject(state)) {
                result.setSuccess(false);
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "require.messenge.status");
            }
            if (DataUtil.isNullObject(unit)) {
                result.setSuccess(false);
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "stock.rescueInformation.required.unit");
            }
            if (DataUtil.isNullObject(quantity)) {
                result.setSuccess(false);
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "mn.invoice.invoiceTemplate.amount.require.msg");
            }
            // so luong nhap phai la so nguyen duong
            if (!DataUtil.isNumber(quantity)) {
                result.setSuccess(false);
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "stock.rescueInformation.required.quantity");
            } else if (avaiableQuantity < DataUtil.safeToLong(quantity) && !Const.REASON_TYPE.RETRIEVE_STOCK.equals(serialRecover)) {
                result.setSuccess(false);
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_DUPLICATE, "stock.rescueInformation.quantity.more");
            }
            // kiem tra hang hoa co trong he thong khong

            productOfferingDTO = productOfferingService.findOne(productOfferingId);
            if (DataUtil.isNullObject(productOfferingDTO)) {
                result.setSuccess(false);
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.rescueInformation.required.stock.exist");
            } else {
                if (!Const.REASON_TYPE.RETRIEVE_STOCK.equals(serialRecover)) {
                    if (!Const.STATUS.ACTIVE.equals(productOfferingDTO.getStatus())) {
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.rescueInformation.required.stock.exist");
                    }

                } else {
                    if (Const.STATUS.NO_ACTIVE.equals(productOfferingDTO.getStatus())) {
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.rescueInformation.required.stock.exist");
                    }
                }
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            result.setSuccess(false);
            throw ex;
        }
//        result.setErrorCode(ErrorCode.ERROR_STANDARD.SUCCESS);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage executeStockTransForExport(StockTransInputDTO stockTransDTO, String shopCode, ProductOfferingDTO stockRecoverCode,
                                                  String serialRecover, List<StockTransDetailDTO> listStockTransDetailDTO, boolean render) throws Exception, LogicException {
        BaseMessage result = new BaseMessage(true);
        currentDate = optionSetValueService.getSysdateFromDB(false);
        stockTransDTO.setCreateDatetime(currentDate);
        checkValidateForExport(stockTransDTO, shopCode, stockRecoverCode, serialRecover, listStockTransDetailDTO, render);

        try {
            //B1. insert vao bang stock_Trans
            StockTransDTO dtoStockTrans = new StockTransDTO();
            StockTransDTO dtoStockTransUctt = new StockTransDTO();
            Long stockTransId;
            Long stockTransIdUctt = 0L;
            List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
            StockTotalDTO stockTotalDTOImport = new StockTotalDTO();
            // insert ban ghi dau tien
            dtoStockTrans.setFromOwnerType(stockTransDTO.getFromOwnerType());
            dtoStockTrans.setToOwnerType(stockTransDTO.getToOwnerType());
            dtoStockTrans.setToOwnerId(stockTransDTO.getToOwnerId());
            if (!Const.REASON_TYPE.RETRIEVE_STOCK.equals(serialRecover)) {
                dtoStockTrans.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
                dtoStockTrans.setFromOwnerId(stockTransDTO.getFromOwnerId());
            } else {
                dtoStockTrans.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
            }
            dtoStockTrans.setReasonId(stockTransDTO.getReasonId());
            if (!DataUtil.isNullObject(stockTransDTO.getNote())) {
                stockTransDTO.setNote(stockTransDTO.getNote().trim());
            }
            dtoStockTrans.setNote(stockTransDTO.getNote());
            dtoStockTrans.setCreateDatetime(stockTransDTO.getCreateDatetime());
            dtoStockTrans.setStockTransType(Const.STOCK_TRANS_TYPE.EXPORT);
            dtoStockTrans = stockTransService.save(dtoStockTrans);
            stockTransId = dtoStockTrans.getStockTransId();

            String note = getTextParam("stock.rescueInformation.stock.import", serialRecover) + " " + stockTransDTO.getCreateUser();
            if (render) {
                dtoStockTransUctt.setFromOwnerType(DataUtil.safeToLong(Const.OWNER_TYPE.CUSTOMER));
                dtoStockTransUctt.setToOwnerId(stockTransDTO.getFromOwnerId());
                dtoStockTransUctt.setToOwnerType(DataUtil.safeToLong(Const.OWNER_TYPE.STAFF));
                dtoStockTransUctt.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
                dtoStockTransUctt.setNote(note);
                List<ReasonDTO> listReason = reasonService.getLsReasonByCode(Const.REASON_CODE.IMPORT_RESCUE);
                Long reason;
                if (!DataUtil.isNullOrEmpty(listReason) && !DataUtil.isNullObject(listReason.get(0).getReasonId())) {
                    reason = listReason.get(0).getReasonId();
                } else {
                    reason = 2L;
                }
                dtoStockTransUctt.setReasonId(reason);
                dtoStockTransUctt.setStockTransType(Const.STOCK_TRANS_TYPE.EXPORT);
                dtoStockTransUctt.setCreateDatetime(stockTransDTO.getCreateDatetime());
                dtoStockTransUctt = stockTransService.save(dtoStockTransUctt);
                stockTransIdUctt = dtoStockTransUctt.getStockTransId();


            }

            //B2. Insert stock_trans_action
            //voi Xuat UCTT:
            // Xuat: 3 ban ghi status =2  day du thong tin, status =3,6 chua cac cai co ban
            // Xuat co thu hoi: 3 ban ghi giong Xuat + 2 ban ghi: status =5 (day du thong tin), 6 thi thieu.
            // Voi Thu hoi: giong co 2 ban ghi: status =5,6
            StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
            StockTransActionDTO stockTransActionDTOUctt = new StockTransActionDTO();
            stockTransActionDTO.setActionCode(stockTransDTO.getActionCode());
            if (Const.REASON_TYPE.RETRIEVE_STOCK.equals(serialRecover)) {
                stockTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
            } else {
                stockTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
            }
            stockTransActionDTO.setNote(stockTransDTO.getNote());
            stockTransActionDTO.setCreateDatetime(stockTransDTO.getCreateDatetime());
            stockTransActionDTO.setCreateUser(stockTransDTO.getCreateUser());
            stockTransActionDTO.setActionStaffId(stockTransDTO.getFromOwnerId());
            stockTransActionDTO.setStockTransId(stockTransId);
            stockTransActionService.save(stockTransActionDTO);
            //Status =6
            StockTransActionDTO stockTransActionSix = new StockTransActionDTO();
            stockTransActionSix.setCreateDatetime(stockTransDTO.getCreateDatetime());
            stockTransActionSix.setCreateUser(stockTransDTO.getCreateUser());
            stockTransActionSix.setActionStaffId(stockTransDTO.getFromOwnerId());
            stockTransActionSix.setStockTransId(stockTransId);
            stockTransActionSix.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
            stockTransActionService.save(stockTransActionSix);
            // Status =3
            if (!Const.REASON_TYPE.RETRIEVE_STOCK.equals(serialRecover)) {
                StockTransActionDTO stockTransActionThree = new StockTransActionDTO();
                stockTransActionThree.setCreateDatetime(stockTransDTO.getCreateDatetime());
                stockTransActionThree.setCreateUser(stockTransDTO.getCreateUser());
                stockTransActionThree.setActionStaffId(stockTransDTO.getFromOwnerId());
                stockTransActionThree.setStockTransId(stockTransId);
                stockTransActionThree.setStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
                stockTransActionService.save(stockTransActionThree);
            }

            if (render) {
                Long stockTransActionId = stockTransActionService.getSequence() + 1L;
                if (DataUtil.isNullObject(stockTransActionId)) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.rescueInformation.getActionId");
                } else {
                    stockTransActionDTOUctt.setActionCode(Const.STOCK_TRANS_ACTION.PN0000 + stockTransActionId.toString());
                }
                stockTransActionDTOUctt.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
                stockTransActionDTOUctt.setNote(note);
                stockTransActionDTOUctt.setCreateDatetime(stockTransDTO.getCreateDatetime());
                stockTransActionDTOUctt.setCreateUser(stockTransDTO.getCreateUser());
                stockTransActionDTOUctt.setActionStaffId(stockTransDTO.getFromOwnerId());
                stockTransActionDTOUctt.setStockTransId(stockTransIdUctt);
                stockTransActionDTOUctt = stockTransActionService.save(stockTransActionDTOUctt);

                StockTransActionDTO stockTransActionDTOUcttSix = new StockTransActionDTO();
                stockTransActionDTOUcttSix.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
                stockTransActionDTOUcttSix.setCreateDatetime(stockTransDTO.getCreateDatetime());
                stockTransActionDTOUcttSix.setCreateUser(stockTransDTO.getCreateUser());
                stockTransActionDTOUcttSix.setActionStaffId(stockTransDTO.getFromOwnerId());
                stockTransActionDTOUcttSix.setStockTransId(stockTransIdUctt);
                stockTransActionService.save(stockTransActionDTOUcttSix);

            }

            // B3: Insert chi tiet phieu xuat vao bang stock_trans_detail
            for (StockTransDetailDTO stockTransDetailDTO : listStockTransDetailDTO) {

                if (render) {
                    //Cap nhat bang stock_handset cua mat hang thu hoi ve mat hang UCTT id cu sang id ung cu
                    stockHandsetService.updateUctt(stockRecoverCode.getProductOfferingId(), serialRecover,
                            stockTransDTO.getFromOwnerId(), DataUtil.safeToLong(Const.OWNER_TYPE.STAFF),
                            productOfferIdUCTT, stockTransDTO.getCreateDatetime());
                    // Insert vao bang stock_handset_rescue
                    StockHandsetRescueDTO stockHandsetRescueDTO = new StockHandsetRescueDTO();
                    stockHandsetRescueDTO.setOwnerId(stockTransDTO.getFromOwnerId());
                    stockHandsetRescueDTO.setOwnerType(DataUtil.safeToLong(Const.OWNER_TYPE.STAFF));
                    stockHandsetRescueDTO.setSerial(serialRecover);
                    stockHandsetRescueDTO.setCreateDate(stockTransDTO.getCreateDatetime());
                    stockHandsetRescueDTO.setLastModify(stockTransDTO.getCreateDatetime());
                    stockHandsetRescueDTO.setOldProdOfferId(stockRecoverCode.getProductOfferingId());
                    stockHandsetRescueDTO.setNewProdOfferlId(productOfferIdUCTT);
                    stockHandsetRescueDTO.setStatus(DataUtil.safeToLong(Const.STATUS.NO_ACTIVE));
                    stockHandsetRescueService.create(stockHandsetRescueDTO);

                    // luu thong tin vao bang stock_total: nhap hang hong UCTT ve kho nhan vien
                    stockTotalDTOImport.setOwnerId(stockTransDTO.getFromOwnerId());
                    stockTotalDTOImport.setOwnerType(DataUtil.safeToLong(Const.OWNER_TYPE.STAFF));
                    stockTotalDTOImport.setProdOfferId(productOfferIdUCTT);//Cong kho mat hang thu hoi UCTT
                    stockTotalDTOImport.setStateId(Const.GOODS_STATE.BROKEN);
                    // Luu stock_total_audit
                    StockTotalAuditDTO stockTotalAuditDTO = new StockTotalAuditDTO();
                    List<StockTotalDTO> listStockTotal;
                    listStockTotal = stockTotalService.searchStockTotal(stockTotalDTOImport);
                    // neu co thi up date; neu chua co thi insert.
                    StockTotalDTO dtoStockTotal;
                    if (!DataUtil.isNullOrEmpty(listStockTotal)) {
                        // Neu ton tai update
                        dtoStockTotal = listStockTotal.get(0);
                        stockTotalDTOImport = DataUtil.cloneBean(dtoStockTotal);
                        stockTotalAuditDTO.setAvailableQuantityBf(dtoStockTotal.getAvailableQuantity());
                        stockTotalAuditDTO.setCurrentQuantityBf(dtoStockTotal.getCurrentQuantity());
                        Long availableQuantity;
                        Long currentQuantity;
                        if (DataUtil.isNullObject(dtoStockTotal.getAvailableQuantity())) {
                            availableQuantity = 1L;
                        } else {
                            availableQuantity = dtoStockTotal.getAvailableQuantity() + 1L;
                        }
                        stockTotalDTOImport.setAvailableQuantity(availableQuantity);
                        if (DataUtil.isNullObject(dtoStockTotal.getAvailableQuantity())) {
                            currentQuantity = 1L;
                        } else {
                            currentQuantity = dtoStockTotal.getCurrentQuantity() + 1L;
                        }
                        stockTotalDTOImport.setCurrentQuantity(currentQuantity);
                        stockTotalDTOImport.setModifiedDate(stockTransDTO.getCreateDatetime());
                    } else {
                        //Neu chua ton tai insert
                        stockTotalDTOImport.setAvailableQuantity(1L);
                        stockTotalDTOImport.setCurrentQuantity(1L);
                        stockTotalDTOImport.setModifiedDate(stockTransDTO.getCreateDatetime());
                        stockTotalDTOImport.setStatus(DataUtil.safeToLong(Const.STATUS.ACTIVE));
                        stockTotalAuditDTO.setAvailableQuantityBf(0L);
                        stockTotalAuditDTO.setCurrentQuantityBf(0L);
                    }
                    //tuydv1: tam bo check ban ghi co duoc xu ly giao dich khong
//                    if (!DataUtil.isNullOrZero(stockTotalService.checkAction(stockTotalDTO))) {
//                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTotal.error.update");
//                    }
                    stockTotalService.save(stockTotalDTOImport);

                    // Chi tiet giao dich nhap kho hang hong
                    StockTransDetailDTO stockTransDetailDTOUctt = new StockTransDetailDTO();
                    stockTransDetailDTOUctt.setStockTransId(stockTransIdUctt);
                    stockTransDetailDTOUctt.setCreateDatetime(stockTransDTO.getCreateDatetime());
                    stockTransDetailDTOUctt.setStateId(Const.GOODS_STATE.BROKEN);
                    stockTransDetailDTOUctt.setQuantity(1L);
                    stockTransDetailDTOUctt.setProdOfferId(productOfferIdUCTT);
                    StockTransDetailDTO sttdDTO = stockTransDetailService.save(stockTransDetailDTOUctt);

                    // insert vao bang stock_trans_serial;
                    StockTransSerialDTO stockTransSerialDTOUctt = new StockTransSerialDTO();
                    stockTransSerialDTOUctt.setFromSerial(serialRecover);
                    stockTransSerialDTOUctt.setToSerial(serialRecover);
                    stockTransSerialDTOUctt.setQuantity(1L);
                    stockTransSerialDTOUctt.setCreateDatetime(stockTransDTO.getCreateDatetime());
                    stockTransSerialDTOUctt.setStockTransDetailId(sttdDTO.getStockTransDetailId());
                    stockTransSerialDTOUctt.setProdOfferId(productOfferIdUCTT);
                    stockTransSerialDTOUctt.setStateId(Const.GOODS_STATE.BROKEN);
                    stockTransSerialDTOUctt.setStockTransId(sttdDTO.getStockTransId());
                    stockTransSerialService.save(stockTransSerialDTOUctt);

                    // Luu stock_total_audit
                    stockTotalAuditDTO.setUserId(stockTransActionDTO.getActionStaffId());
                    stockTotalAuditDTO.setCreateDate(stockTransDTO.getCreateDatetime());
                    stockTotalAuditDTO.setReasonId(stockTransDTO.getReasonId());
                    stockTotalAuditDTO.setTransId(dtoStockTransUctt.getStockTransId());
                    stockTotalAuditDTO.setTransCode(stockTransActionDTOUctt.getActionCode());
                    stockTotalAuditDTO.setTransType(Const.STOCK_TOTAL_AUDIT.TRANS_TYPE);
                    stockTotalAuditDTO.setUserCode(stockTransActionDTO.getCreateUser());
                    stockTotalAuditDTO.setUserName(staffDTO.getName());
                    stockTotalAuditDTO.setOwnerCode(stockTransActionDTO.getCreateUser());
                    stockTotalAuditDTO.setOwnerName(staffDTO.getName());
                    stockTotalAuditDTO.setSourceType(Const.STOCK_TOTAL_AUDIT.SOURCE_TYPE_STOCK_TRANS);
                    stockTotalAuditDTO.setOwnerType(stockTotalDTOImport.getOwnerType());
                    stockTotalAuditDTO.setOwnerId(stockTransActionDTO.getActionStaffId());
                    stockTotalAuditDTO.setStateId(stockTotalDTOImport.getStateId());
                    stockTotalAuditDTO.setProdOfferId(productOfferIdUCTT);
                    stockTotalAuditDTO.setProdOfferCode(stockRecoverCode.getCode());
                    stockTotalAuditDTO.setProdOfferName(stockRecoverCode.getName());
                    stockTotalAuditDTO.setProdOfferTypeId(stockRecoverCode.getProductOfferTypeId());
                    stockTotalAuditDTO.setAvailableQuantityAf(stockTotalDTOImport.getAvailableQuantity());
                    stockTotalAuditDTO.setCurrentQuantityAf(stockTotalDTOImport.getCurrentQuantity());
                    stockTotalAuditDTO.setAvailableQuantity(1L);
                    stockTotalAuditDTO.setCurrentQuantity(1L);
                    stockTotalAuditService.save(stockTotalAuditDTO);
                }

                //Neu chon ly do: "Xuat hang cho muon" va mat hang dien thoai thi luu thong tin ve hang cho muon
                if (!render && Const.PRODUCT_OFFER_TYPE.PHONE.equals(stockTransDetailDTO.getProdOfferTypeId())
                        && !Const.REASON_TYPE.RETRIEVE_STOCK.equals(serialRecover)) {
                    StockHandsetLendDTO stockHandsetLendDTO = new StockHandsetLendDTO();
                    stockHandsetLendDTO.setOwnerId(stockTransDTO.getFromOwnerId());
                    stockHandsetLendDTO.setOwnerType(Const.OWNER_TYPE.STAFF_LONG);
                    stockHandsetLendDTO.setCreateStaffId(stockTransDTO.getFromOwnerId());
                    stockHandsetLendDTO.setProdOfferId(stockTransDetailDTO.getProdOfferId());
                    if (!DataUtil.isNullOrEmpty(stockTransDetailDTO.getLstStockTransSerial())) {
                        String serial = stockTransDetailDTO.getLstStockTransSerial().get(0).getFromSerial();
                        stockHandsetLendDTO.setSerial(serial);
                    }
                    stockHandsetLendDTO.setCreateDate(stockTransDTO.getCreateDatetime());
                    stockHandsetLendDTO.setLastModify(stockTransDTO.getCreateDatetime());
                    stockHandsetLendDTO.setStatus(DataUtil.safeToLong(Const.STATUS.NO_ACTIVE));
                    stockHandsetLendService.create(stockHandsetLendDTO);

                }

                stockTransDetailDTO.setStockTransId(stockTransId);
                stockTransDetailDTO.setCreateDatetime(stockTransDTO.getCreateDatetime());
                StockTransDetailDTO dto = stockTransDetailService.save(stockTransDetailDTO);
                dto.setTableName(stockTransDetailDTO.getTableName());
                dto.setLstStockTransSerial(stockTransDetailDTO.getLstStockTransSerial());
                dto.setProdOfferTypeId(stockTransDetailDTO.getProdOfferTypeId());
                dto.setStrStateIdAfter(stockTransDetailDTO.getStrStateIdAfter());
                lstStockTransDetail.add(dto);

                //•	B4: Nếu mặt hàng xuất có thông tin serial thì lưu thông tin chi tiết serial vào bảng stock_trans_serial.
//            o	From_serial: Nhập từ giao diện
//            o	To_serial: Nhập từ giao diện
//            o	Quantity: Số lượng xuất từ giao diện.
//            o	Create_datetime: sysdate
                if (!DataUtil.isNullOrEmpty(stockTransDetailDTO.getLstStockTransSerial())) {
                    for (StockTransSerialDTO stockTransSerialDTO : stockTransDetailDTO.getLstStockTransSerial()) {
                        stockTransSerialDTO.setCreateDatetime(stockTransDTO.getCreateDatetime());
                        stockTransSerialDTO.setStockTransDetailId(dto.getStockTransDetailId());
                        stockTransSerialDTO.setProdOfferId(stockTransDetailDTO.getProdOfferId());
                        stockTransSerialDTO.setStateId(stockTransDetailDTO.getStateId());
                        stockTransSerialDTO.setStockTransId(stockTransDetailDTO.getStockTransId());
                        stockTransSerialService.save(stockTransSerialDTO);
                    }
                }

                //B7: Cập nhật giảm số lượng hàng hóa trong kho nhân viên theo số lượng ứng cứu thông tin
                StockTotalDTO stockTotalDTO = new StockTotalDTO();
                stockTotalDTO.setOwnerType(DataUtil.safeToLong(Const.OWNER_TYPE.STAFF));
                stockTotalDTO.setProdOfferId(stockTransDetailDTO.getProdOfferId());
                // Luu stock_total_audit
                StockTotalAuditDTO stockTotalAuditDTO = new StockTotalAuditDTO();
                if (!Const.REASON_TYPE.RETRIEVE_STOCK.equals(serialRecover)) {
                    stockTotalDTO.setStateId(stockTransDetailDTO.getStateId());
                    stockTotalDTO.setOwnerId(stockTransDTO.getFromOwnerId());
                } else {
                    stockTotalDTO.setStateId(DataUtil.safeToLong(stockTransDetailDTO.getStrStateIdAfter()));
                    stockTotalDTO.setOwnerId(stockTransDTO.getToOwnerId());
                }
                stockTotalAuditDTO.setStateId(stockTotalDTO.getStateId());
                stockTotalAuditDTO.setOwnerId(stockTotalDTO.getOwnerId());
                List<StockTotalDTO> listStockTotal = stockTotalService.searchStockTotal(stockTotalDTO);
                StockTotalDTO stockTotalDTOUpdate = new StockTotalDTO();
                stockTotalDTOUpdate.setModifiedDate(stockTransDTO.getCreateDatetime());
                if (DataUtil.isNullOrEmpty(listStockTotal)) {
                    if (Const.REASON_TYPE.RETRIEVE_STOCK.equals(serialRecover)) {
                        stockTotalDTO.setCurrentQuantity(stockTransDetailDTO.getQuantity());
                        stockTotalDTO.setAvailableQuantity(stockTransDetailDTO.getQuantity());
                        stockTotalDTO.setModifiedDate(stockTransDTO.getCreateDatetime());
                        stockTotalDTO.setStatus(DataUtil.safeToLong(Const.STATUS.ACTIVE));
                        stockTotalService.save(stockTotalDTO);
                        stockTotalAuditDTO.setAvailableQuantityAf(stockTotalDTO.getAvailableQuantity());
                        stockTotalAuditDTO.setCurrentQuantityAf(stockTotalDTO.getCurrentQuantity());
                        stockTotalAuditDTO.setAvailableQuantity(stockTotalDTO.getAvailableQuantity());
                        stockTotalAuditDTO.setCurrentQuantity(stockTotalDTO.getCurrentQuantity());
                        stockTotalAuditDTO.setAvailableQuantityBf(0L);
                        stockTotalAuditDTO.setCurrentQuantityBf(0L);
                    } else {
                        throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "stock.hand.select");
                    }
                } else {
                    stockTotalDTOUpdate = listStockTotal.get(0);
                    stockTotalDTOUpdate.setModifiedDate(stockTransDTO.getCreateDatetime());
                    if (!Const.REASON_TYPE.RETRIEVE_STOCK.equals(serialRecover)) {
                        if (stockTotalDTOUpdate.getAvailableQuantity() < stockTransDetailDTO.getQuantity()) {
                            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.stockTotal.notAvailable", stockTransDetailDTO.getProdOfferName());
                        }
                        if (stockTotalDTOUpdate.getCurrentQuantity() < stockTransDetailDTO.getQuantity()) {
                            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "validate.stockTotal.current.smaller.input.quantity", stockTransDetailDTO.getProdOfferName());
                        }

                        stockTotalAuditDTO.setAvailableQuantityBf(stockTotalDTOUpdate.getAvailableQuantity());
                        stockTotalAuditDTO.setCurrentQuantityBf(stockTotalDTOUpdate.getCurrentQuantity());

                        //Cap nhat stock_total: tru kho mat hang xuat UCTT
                        stockTotalDTOUpdate.setCurrentQuantity(stockTotalDTOUpdate.getCurrentQuantity() - stockTransDetailDTO.getQuantity());
                        stockTotalDTOUpdate.setAvailableQuantity(stockTotalDTOUpdate.getAvailableQuantity() - stockTransDetailDTO.getQuantity());

                        //cap nhat stock_total_audit
                        stockTotalAuditDTO.setAvailableQuantityAf(stockTotalDTOUpdate.getAvailableQuantity());
                        stockTotalAuditDTO.setCurrentQuantityAf(stockTotalDTOUpdate.getCurrentQuantity());
                        stockTotalAuditDTO.setAvailableQuantity(-(stockTransDetailDTO.getQuantity()));
                        stockTotalAuditDTO.setCurrentQuantity(-(stockTransDetailDTO.getQuantity()));
                    } else {
                        stockTotalAuditDTO.setAvailableQuantityBf(stockTotalDTOUpdate.getAvailableQuantity());
                        stockTotalAuditDTO.setCurrentQuantityBf(stockTotalDTOUpdate.getCurrentQuantity());

                        stockTotalDTOUpdate.setCurrentQuantity(stockTotalDTOUpdate.getCurrentQuantity() + stockTransDetailDTO.getQuantity());
                        stockTotalDTOUpdate.setAvailableQuantity(stockTotalDTOUpdate.getAvailableQuantity() + stockTransDetailDTO.getQuantity());

                        stockTotalAuditDTO.setAvailableQuantityAf(stockTotalDTOUpdate.getAvailableQuantity());
                        stockTotalAuditDTO.setCurrentQuantityAf(stockTotalDTOUpdate.getCurrentQuantity());
                        stockTotalAuditDTO.setAvailableQuantity(stockTransDetailDTO.getQuantity());
                        stockTotalAuditDTO.setCurrentQuantity(stockTransDetailDTO.getQuantity());
                    }
                    stockTotalService.save(stockTotalDTOUpdate);
                }

                stockTotalAuditDTO.setUserId(stockTransActionDTO.getActionStaffId());
                stockTotalAuditDTO.setCreateDate(stockTransDTO.getCreateDatetime());
                stockTotalAuditDTO.setReasonId(stockTransDTO.getReasonId());
                stockTotalAuditDTO.setTransId(dtoStockTrans.getStockTransId());
                stockTotalAuditDTO.setTransCode(stockTransActionDTO.getActionCode());
                stockTotalAuditDTO.setTransType(Const.STOCK_TOTAL_AUDIT.TRANS_TYPE);
                stockTotalAuditDTO.setUserCode(stockTransActionDTO.getCreateUser());
                stockTotalAuditDTO.setUserName(staffDTO.getName());
                StaffDTO staffDTOOwner = staffService.findOne(stockTotalDTO.getOwnerId());

                stockTotalAuditDTO.setOwnerCode(staffDTOOwner.getStaffCode());
                stockTotalAuditDTO.setOwnerName(staffDTOOwner.getName());
                stockTotalAuditDTO.setSourceType(Const.STOCK_TOTAL_AUDIT.SOURCE_TYPE_STOCK_TRANS);
                stockTotalAuditDTO.setOwnerType(stockTotalDTO.getOwnerType());
                stockTotalAuditDTO.setProdOfferId(stockTransDetailDTO.getProdOfferId());
                stockTotalAuditDTO.setProdOfferCode(stockTransDetailDTO.getProdOfferCode());
                stockTotalAuditDTO.setProdOfferName(stockTransDetailDTO.getProdOfferName());
                stockTotalAuditDTO.setProdOfferTypeId(stockTransDetailDTO.getProdOfferTypeId());
                stockTotalAuditService.save(stockTotalAuditDTO);
            }

            //B6: cập nhật trạng thái mặt hàng UCTT có serial.
            FlagStockDTO flagStockDTO = new FlagStockDTO();
            if (Const.REASON_TYPE.RETRIEVE_STOCK.equals(serialRecover)) {

                flagStockDTO.setOldStatus(0L);
                flagStockDTO.setNewStatus(1L);
                flagStockDTO.setStateId(5L);
                flagStockDTO.setNote(serialRecover);
                flagStockDTO.setImpAvailableQuantity(1L);
                flagStockDTO.setImpCurrentQuantity(1L);
                flagStockDTO.setImpHangQuantity(1L);
                flagStockDTO.setImportStock(true);
                flagStockDTO.setUpdateRescue(true);
                dtoStockTrans.setFromOwnerId(stockTransDTO.getFromOwnerId());

            } else {
                flagStockDTO.setOldStatus(1L);
                flagStockDTO.setNewStatus(0L);
                flagStockDTO.setStateId(5L);
                flagStockDTO.setExpAvailableQuantity(-1L);
                flagStockDTO.setExpCurrentQuantity(-1L);
                flagStockDTO.setExpHangQuantity(-1L);
                flagStockDTO.setExportStock(true);
                flagStockDTO.setUpdateRescue(true);
                flagStockDTO.setNote(Const.REASON_TYPE.EXPORT_RESCUE);
            }
            dtoStockTrans.setUserName(stockTransDTO.getCreateUser());
            stockHandsetService.doSaveStockGoods(dtoStockTrans, lstStockTransDetail, flagStockDTO);

            // Cap nhat du lieu IM1
            List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
            if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1) && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())) {
                if (Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()))) {
                    if (render) {
                        //cap nhat serial hang hong thu hoi
                        stockHandsetService.updateUcttIm1(stockRecoverCode.getProductOfferingId(), serialRecover, stockTransDTO.getFromOwnerId(),
                                DataUtil.safeToLong(Const.OWNER_TYPE.STAFF), productOfferIdUCTT, stockTransDTO.getCreateDatetime());

                        //Cong kho mat hang hong thu hoi
                        StockTotalDTO stockTotal = new StockTotalDTO();
                        stockTotal.setOwnerId(stockTransDTO.getFromOwnerId());
                        stockTotal.setOwnerType(DataUtil.safeToLong(Const.OWNER_TYPE.STAFF));
                        stockTotal.setProdOfferId(productOfferIdUCTT);
                        stockTotal.setStateId(Const.GOODS_STATE.BROKEN);
                        stockTotal.setModifiedDate(stockTransDTO.getCreateDatetime());
                        baseStockIm1Service.doSaveStockTotalUctt(stockTotal, dtoStockTransUctt, stockTransActionDTOUctt);
                    }

                    //Cap nhat serial xuat UCTT
                    baseStockIm1Service.doSaveStockGoods(dtoStockTrans, lstStockTransDetail, flagStockDTO);

                    //Tru kho mat hang UCTT
                    baseStockIm1Service.doSaveStockTotal(dtoStockTrans, lstStockTransDetail, flagStockDTO, stockTransActionDTO);
                }
            }

        } catch (LogicException lex) {
            logger.error(lex.getMessage(), lex);
            result.setSuccess(false);
            throw lex;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            result.setSuccess(false);
            throw ex;
        }

        return result;
    }

    private void checkValidateForExport(StockTransInputDTO transInputDTO, String shopCode, ProductOfferingDTO stockRecoverCode, String serialRecover, List<StockTransDetailDTO> listStockTransDetailDTO, boolean render) throws Exception {
        try {
            if (DataUtil.isNullObject(transInputDTO.getFromOwnerId())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "staff.code.invalid.name");
            } else {
                staffDTO = staffService.findOne(transInputDTO.getFromOwnerId());
                if (DataUtil.isNullObject(staffDTO) || Const.STATUS.NO_ACTIVE.equals(staffDTO.getStatus())) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "staff.code.invalid.name");
                }
            }
            //check validate
            if (DataUtil.isNullOrEmpty(listStockTransDetailDTO) && !Const.REASON_TYPE.RETRIEVE_STOCK.equals(serialRecover)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "stock.rescueInformation.validate.list");
            } else {
                //1. kiem tra ma code
                if (DataUtil.isNullOrEmpty(transInputDTO.getActionCode())) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "export.order.transCode.require.msg");
                }
                if (!PATTERN.matcher(transInputDTO.getActionCode()).matches()) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.code.under.format.msg");
                }
                if (!DataUtil.isNullOrEmpty(transInputDTO.getActionCode()) && transInputDTO.getActionCode().length() > 50) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.code.under.format.msg");
                }
                //2. kiem tra nguoi xuat/nhap
                if (DataUtil.isNullOrEmpty(transInputDTO.getCreateUser())) {
                    if (!Const.REASON_TYPE.RETRIEVE_STOCK.equals(serialRecover)) {
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "export.order.by.whom.require.msg");
                    } else {
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "mn.stock.agency.retrieve.expense.receive.human.require");
                    }
                }
                // kiem tra kho xuat/nhap
                if (!Const.REASON_TYPE.RETRIEVE_STOCK.equals(serialRecover)) {
                    if (DataUtil.isNullOrZero(transInputDTO.getFromOwnerId()) || DataUtil.isNullOrZero(transInputDTO.getFromOwnerType())) {
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.stock.require.msg");
                    }

                    StaffDTO staffDTO = staffService.findOne(transInputDTO.getFromOwnerId());
                    if (DataUtil.isNullObject(staffDTO) || !staffDTO.getStatus().equals(Const.STATUS_ACTIVE)) {
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.isdn.manage.creat.field.export.fromStopInvalid");
                    }
                } else {
                    if (DataUtil.isNullOrZero(transInputDTO.getToOwnerId())) {
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.isdn.manage.create.field.validate.toOwner");
                    }

                    StaffDTO staffDTO = staffService.findOne(transInputDTO.getToOwnerId());
                    if (DataUtil.isNullObject(staffDTO) || !staffDTO.getStatus().equals(Const.STATUS_ACTIVE)) {
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.toStock.notExists");
                    }

                }

                //3. kiem tra ly do co ton tai khong
                if (DataUtil.isNullOrZero(transInputDTO.getReasonId())) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "create.note.reason.export.require.msg");
                }
                ReasonDTO reason = reasonService.findOne(transInputDTO.getReasonId());
                if (DataUtil.isNullObject(reason) || DataUtil.isNullOrZero(reason.getReasonId())
                        || !Const.STATUS.ACTIVE.equals(reason.getReasonStatus())) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.reason.notExists");
                }
                // 4. kiem tra shop
                if (DataUtil.isNullOrEmpty(shopCode)) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.stock.require.msg");
                }
                // kho xuat
//                List<VShopStaffDTO> exportShopList = shopService.getListShopStaff(transInputDTO.getFromOwnerId(), transInputDTO.getFromOwnerType().toString(), Const.VT_UNIT.VT);
//                if (DataUtil.isNullOrEmpty(exportShopList)) {
//                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.fromStock.notExists");
//                }
                // 5. kiem tra ghi chu
                if (!DataUtil.isNullOrEmpty(transInputDTO.getNote()) && transInputDTO.getNote().getBytes("UTF-8").length > 500) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.note.overLength");
                }
                // kiem tra  so luong dap ung co du khong
                if (!Const.REASON_TYPE.RETRIEVE_STOCK.equals(serialRecover)) {
                    if (checkAvailableQuantity(transInputDTO, listStockTransDetailDTO)) {
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.stockTotal.notAvailable");
                    }
                }
                // kiem tra loai hang hoa con khong
                //Neu chon ly do xuat co thu hoi hang hong
                if (render) {
                    //5. kiem tra hang thu hoi
                    if (DataUtil.isNullObject(stockRecoverCode)) {
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "stock.rescueInformation.required.stock.recover");
                    } else {
                        ProductOfferingDTO dto = productOfferingService.findOne(stockRecoverCode.getProductOfferingId());
                        if (DataUtil.isNullObject(dto)) {
                            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.rescueInformation.stock.recover.notExist");
                        } else {
                            // hang hoa phai la dien thoai
                            List<ProductOfferingDTO> lsProductOffering = productOfferingService.findByProductOfferTypeId(Const.PRODUCT_OFFER_TYPE.PHONE, stockRecoverCode.getProductOfferingId());
                            if (DataUtil.isNullOrEmpty(lsProductOffering)) {
                                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.rescueInformation.stock.recover.phone");
                            } else {
                                String code = stockRecoverCode.getCode();
                                if (DataUtil.isNullObject(code)) {
                                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.rescueInformation.stock.no.recode");
                                } else {
                                    List<OptionSetValueDTO> lstCogfigUCTT = optionSetValueService.getByOptionSetCode("PRODUCT_OFFER_RESCUE");
                                    if (DataUtil.isNullOrEmpty(lstCogfigUCTT)) {
                                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.rescueInformation.stock.no.config.uctt");
                                    }
//                                    String codeNew = code + Const.PRODUCT_OFFERING._UCTT;
                                    ProductOfferingDTO productOfferingDTO = null;
                                    for (int i = 0; i <= lstCogfigUCTT.size() - 1; i++) {
                                        String codeNew = code + "_" + lstCogfigUCTT.get(i).getValue();
                                        productOfferingDTO = productOfferingService.getProductByCode(codeNew);
                                        if (!DataUtil.isNullObject(productOfferingDTO)) {
                                            break;
                                        }
                                    }

                                    if (DataUtil.isNullObject(productOfferingDTO)) {
                                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.rescueInformation.stock.no.recode");
                                    } else {
                                        // lay mat hang uctt
                                        productOfferIdUCTT = productOfferingDTO.getProductOfferingId();
//                                        if (!productOfferIdUCTT.equals(listStockTransDetailDTO.get(0).getProdOfferId())) {
//                                            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.rescueInformation.stock.ok", codeNew);
//                                        }
                                    }
                                }
                            }
                        }

                        //6. kiem tra Serial hang thu hoi
                        if (DataUtil.isNullOrEmpty(serialRecover)) {
                            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "stock.rescueInformation.required.serial");
                        } else {
                            // Kiem tra mat hang co serial hang thu hoi co trang thai ban khong (ban: status!=0)
                            Long productOfferingId = stockRecoverCode.getProductOfferingId();
                            StockHandsetDTO stockHandsetDTO = stockHandsetService.checkSerialSale(productOfferingId, serialRecover, Const.STATUS.NO_ACTIVE);
                            if (DataUtil.isNullObject(stockHandsetDTO)) {
                                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "stock.rescueInformation.stock.no.serial");
                            } else {
                                if (!DataUtil.isNullObject(stockRecoverCode.getTelecomServiceId())) {
                                    String strTelecomServiceId = stockRecoverCode.getTelecomServiceId().toString();
                                    List<OptionSetValueDTO> checkAlwaysArranty = optionSetValueService.getByOptionsetCodeAndValue(Const.OPTION_SET.ALWAYS_WARRANTY, strTelecomServiceId);
                                    if (DataUtil.isNullOrEmpty(checkAlwaysArranty)) {
                                        Date saleDate = stockHandsetDTO.getSaleDate();
                                        Date currentDate = getSysDate(em);
                                        if (DateUtil.daysBetween2Dates(currentDate, saleDate) > 365) {
                                            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.rescueInformation.stock.maxDate", serialRecover);

                                        }

                                    }
                                }

                            }
                        }
                    }
                    // neu xuat UCTT >1 mat hang
                    if (listStockTransDetailDTO.size() > 1 || listStockTransDetailDTO.get(0).getQuantity() > 1L) {
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.rescueInformation.validate.list.resontrue");
                    }
                }
            }
        } catch (LogicException ex) {
            throw ex;
        }

    }

    private boolean checkAvailableQuantity(StockTransInputDTO transInputDTO, List<StockTransDetailDTO> listStockTransDetailDTO) throws Exception {
        boolean result = false;
        try {
            for (StockTransDetailDTO dto : listStockTransDetailDTO) {
                StockTotalDTO stockTotalDTO = new StockTotalDTO();
                stockTotalDTO.setProdOfferId(dto.getProdOfferId());
                stockTotalDTO.setOwnerId(transInputDTO.getFromOwnerId());
                stockTotalDTO.setOwnerType(transInputDTO.getFromOwnerType());
                stockTotalDTO.setStateId(dto.getStateId());
                stockTotalDTO.setStatus(DataUtil.safeToLong(Const.STATUS.ACTIVE));
                stockTotalDTO.setOwnerType(transInputDTO.getFromOwnerType());
                List<StockTotalDTO> lsSttDTO = stockTotalService.searchStockTotal(stockTotalDTO);
                if (DataUtil.isNullOrEmpty(lsSttDTO)) {
                    result = true;
                    break;
                } else {
                    Long availableQuantity = lsSttDTO.get(0).getAvailableQuantity();
                    if (availableQuantity < dto.getQuantity()) {
                        result = true;
                        break;
                    }

                }

            }
        } catch (LogicException ex) {
            throw ex;
        }
        return result;
    }
}
