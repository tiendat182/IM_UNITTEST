package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.repo.StockOrderRepo;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.inventory.service.BaseStock.BaseStockService;
import com.viettel.bccs.inventory.service.BaseStock.BaseValidateService;
import com.viettel.bccs.partner.dto.AccountAgentDTO;
import com.viettel.bccs.partner.service.AccountAgentService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class SmartphoneTransactionService extends BaseStockService {
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;
    @Autowired
    private StockTransActionService stockTransActionService;
    @Autowired
    private StockOrderDetailService stockOrderDetailService;
    @Autowired
    private StockOrderService stockOrderService;
    @Autowired
    private BaseValidateService baseValidateService;
    @Autowired
    private MtService mtService;
    @Autowired
    private AccountAgentService accountAgentService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private StockOrderRepo stockOrderRepo;

    // xu ly phe duyet don hang
    @Transactional(rollbackFor = Exception.class)
    public void processApproveOrder(StaffDTO staffDTO, Long shopId, Long staffId, StockOrderDTO stockOrderDTO, List<StockOrderDetailDTO> listOrderDB, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {
        Date currentDate = getSysDate(em);
        for (StockOrderDetailDTO stockOrderDetailDTO : listOrderDB) {
            stockOrderDetailService.save(stockOrderDetailDTO);
        }
        stockOrderDTO.setStatus(Const.STOCK_ORDER.STATUS_APPROVE);
        stockOrderDTO.setApproveDate(currentDate);
        stockOrderDTO.setApproveStaffId(staffDTO.getStaffId());
        stockOrderService.saveStockOrder(stockOrderDTO);
        // Luu thong tin GD kho
        StockTransDTO stockTransDTO = getStockTransForApproveOrder(shopId, staffId, currentDate);
        //Validate han muc kho nhan
        baseValidateService.doDebitValidate(stockTransDTO, lstStockTransDetail);

        // Luu stock_trans
        StockTransDTO stockTransNew = stockTransService.save(stockTransDTO);
        StockTransActionDTO stockTransActionDTO = getStockTransActionForApproverOrder(stockTransNew, staffDTO, stockOrderDTO, currentDate);
        StockTransActionDTO stockTransActionDTONew = stockTransActionService.save(stockTransActionDTO);
        // Luu stock_total
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setExportStock(true);
        flagStockDTO.setExpAvailableQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);
        doSaveStockTotal(stockTransNew, lstStockTransDetail, flagStockDTO, stockTransActionDTONew);
        doSaveStockTransDetail(stockTransNew, lstStockTransDetail);
        // Thuc hien nhan tin
        List<AccountAgentDTO> accountAgentDTOs = accountAgentService.getAccountAgentFromOwnerId(stockOrderDTO.getStaffId(), Const.OWNER_TYPE.STAFF_LONG);
        String isdn = null;
        if (!DataUtil.isNullOrEmpty(accountAgentDTOs)) {
            AccountAgentDTO accountAgentDTO = accountAgentDTOs.get(0);
            Long checkISDN = -1L;
            if (accountAgentDTO.getCheckIsdn() != null) {
                checkISDN = accountAgentDTO.getCheckIsdn();
            }
            if (checkISDN != 0L) {
                isdn = accountAgentDTO.getIsdn();
            }
        } else {
            StaffDTO staffDTO1 = staffService.findOne(stockOrderDTO.getStaffId());
            if (!DataUtil.isNullObject(staffDTO1)) {
                isdn = staffDTO1.getTel();
            }
        }
        if (!DataUtil.isNullOrEmpty(isdn)) {
            if (DataUtil.checkPhone(isdn)) {
                mtService.saveSms(isdn, getText("sp.approve.stockOrder.sendSms.approve"));
            }
        }
    }

    // Tu choi don hang
    @Transactional(rollbackFor = Exception.class)
    public void refuseOrder(StockOrderDTO stockOrderDTO, StaffDTO staffDTO) throws LogicException, Exception {
        // Luu DB
        Date currentDate = getSysDate(em);
        stockOrderDTO.setStatus(Const.STOCK_ORDER.STATUS_REFUSE);
        stockOrderDTO.setRefuseDate(currentDate);
        stockOrderDTO.setRefuseStaffId(staffDTO.getStaffId());
        stockOrderService.saveStockOrder(stockOrderDTO);
        // Thuc hien nhan tin
        List<AccountAgentDTO> accountAgentDTOs = accountAgentService.getAccountAgentFromOwnerId(stockOrderDTO.getStaffId(), Const.OWNER_TYPE.STAFF_LONG);
        String isdn = null;
        if (!DataUtil.isNullOrEmpty(accountAgentDTOs)) {
            AccountAgentDTO accountAgentDTO = accountAgentDTOs.get(0);
            Long checkISDN = -1L;
            if (accountAgentDTO.getCheckIsdn() != null) {
                checkISDN = accountAgentDTO.getCheckIsdn();
            }
            if (checkISDN != 0L) {
                isdn = accountAgentDTO.getIsdn();
            }
        } else {
            StaffDTO staffDTO1 = staffService.findOne(stockOrderDTO.getStaffId());
            if (!DataUtil.isNullObject(staffDTO1)) {
                isdn = staffDTO1.getTel();
            }
        }
        if (!DataUtil.isNullOrEmpty(isdn)) {
            if (DataUtil.checkPhone(isdn)) {
                mtService.saveSms(isdn, getText("sp.approve.stockOrder.sendSms.refuse"));
            }
        }
    }

    // Tao don hang
    @Transactional(rollbackFor = Exception.class)
    public void requestOrder(Long shopId, Long staffId, List<StockOrderDetailDTO> listOrderDetail) throws LogicException, Exception {
        // Tao don hang. Luu stock_order
        Long stockOrderId = getSequence(em, "STOCK_ORDER_SEQ");
        StockOrderDTO stockOrderDTO = new StockOrderDTO();
        stockOrderDTO.setStockOrderId(stockOrderId);
        stockOrderDTO.setStockOrderCode("DHSM" + String.format("%010d", stockOrderId));
        stockOrderDTO.setShopId(shopId);
        stockOrderDTO.setStaffId(staffId);
        stockOrderDTO.setStatus(Const.STOCK_ORDER.STATUS_NEW);
        stockOrderDTO.setCreateDate(getSysDate(em));
        stockOrderService.saveStockOrder(stockOrderDTO);
        // Luu stock_order_detail
        for (StockOrderDetailDTO stockOrderDetailDTO : listOrderDetail) {
            stockOrderDetailDTO.setStockOrderId(stockOrderId);
            stockOrderDetailService.save(stockOrderDetailDTO);
        }
    }

    // Tao GD kho cho phe duyet don dat hang
    private StockTransDTO getStockTransForApproveOrder(Long shopId, Long staffId, Date currentDate) {
        StockTransDTO stockTrans = new StockTransDTO();
        stockTrans.setFromOwnerType(Const.OWNER_TYPE.SHOP_LONG);
        stockTrans.setFromOwnerId(shopId);
        stockTrans.setToOwnerType(Const.OWNER_TYPE.STAFF_LONG);
        stockTrans.setToOwnerId(staffId);
        stockTrans.setStockTransType(Const.STOCK_TRANS_TYPE.EXPORT);
        stockTrans.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_ORDER);
        stockTrans.setReasonId(Const.STOCK_ORDER.REASON_ID);
        stockTrans.setCreateDatetime(currentDate);
        stockTrans.setNote(getText("sp.approve.stockOrder.stockTrans.note"));
        return stockTrans;
    }

    private StockTransActionDTO getStockTransActionForApproverOrder(StockTransDTO stockTransDTO, StaffDTO staffDTO, StockOrderDTO stockOrderDTO, Date currentDate) throws Exception {
        String stt ;
        List<StockOrderDTO> stockOrderDTOs = stockOrderRepo.getStockOrderList(staffDTO.getStaffId(), stockOrderDTO.getCreateDate());
        int number = 0;
        if (!DataUtil.isNullOrEmpty(stockOrderDTOs)) {
            number = stockOrderDTOs.size();
        }
        if (number < 10) {
            stt = "0" + number;
        } else {
            stt = String.valueOf(number);
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
        String strDateFormat = dateFormat.format(stockOrderDTO.getCreateDate());
        String actionCode = "LXSM_" + stt + "_" + strDateFormat;

        StockTransActionDTO transActionDTO = new StockTransActionDTO();
        transActionDTO.setStockTransId(stockTransDTO.getStockTransId());
        transActionDTO.setActionCode(actionCode);
        transActionDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_ORDER);
        transActionDTO.setNote(stockTransDTO.getNote());
        transActionDTO.setCreateDatetime(currentDate);
        transActionDTO.setActionStaffId(staffDTO.getStaffId());
        transActionDTO.setCreateUser(staffDTO.getStaffCode());
        transActionDTO.setNote(stockTransDTO.getNote());
        return transActionDTO;
    }

    @Override
    public void doPrepareData(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, FlagStockDTO flagStockDTO) throws LogicException, Exception {

    }

    @Override
    public void doValidate(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {

    }
}
