package com.viettel.bccs.inventory.service.BaseStock;

import com.viettel.bccs.inventory.service.BaseStock.*;
import com.viettel.bccs.inventory.service.ProductExchangeService;
import com.viettel.bccs.inventory.service.ProductExchangeServiceImpl;
import org.springframework.stereotype.Service;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.service.BaseIsdn.BaseIsdnExportService;
import com.viettel.bccs.inventory.service.BaseIsdn.BaseIsdnOrderService;
import com.viettel.bccs.inventory.service.BaseIsdn.BaseStockCancelTransIsdnService;
import com.viettel.bccs.inventory.service.BaseIsdn.BaseStockIsdnExpService;
import com.viettel.fw.common.util.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by thetm1 on 9/12/2015.
 */
@Service
public class StockTransFactory {


    @Autowired
    @Qualifier("baseStockOutOrderService")
    BaseStockOutOrderService baseStockOutOrderService;

    @Autowired
    @Qualifier("baseStockNoteOrderService")
    BaseStockNoteOrderService baseStockNoteOrderService;

    @Autowired
    @Qualifier("baseStockNoteAgentService")
    BaseStockNoteAgentService baseStockNoteAgentService;

    @Autowired
    @Qualifier("baseStockExpNoteService")
    BaseStockExpNoteService baseStockExpNoteService;

    @Autowired
    @Qualifier("baseIsdnOrderService")
    BaseIsdnOrderService baseIsdnOrderService;

    @Autowired
    @Qualifier("baseStockIsdnExpService")
    BaseStockIsdnExpService baseStockIsdnExpService;

    @Autowired
    @Qualifier("baseStockInNoteService")
    BaseStockInNoteService baseStockInNoteService;

    @Autowired
    @Qualifier("baseStockImpNoteService")
    BaseStockImpNoteService baseStockImpNoteService;

    @Autowired
    @Qualifier("baseStockStaffImpService")
    BaseStockStaffImpService baseStockStaffImpService;

    @Autowired
    @Qualifier("baseStockStaffExpService")
    BaseStockStaffExpService baseStockStaffExpService;

    @Autowired
    @Qualifier("baseStockAgentExpService")
    BaseStockAgentExpService baseStockAgentExpService;

    @Autowired
    @Qualifier("baseStockInOrderService")
    BaseStockInOrderService baseStockInOrderService;

    @Autowired
    @Qualifier("baseStockOutNoteService")
    BaseStockOutNoteService baseStockOutNoteService;

    @Autowired
    @Qualifier("baseStockAgentRetrieveService")
    BaseStockAgentRetrieveService baseStockAgentRetrieveService;
    @Autowired
    @Qualifier("baseStockCancelTransService")
    BaseStockCancelTransService baseStockCancelTransService;

    @Autowired
    @Qualifier("baseStockRejectTransService")
    BaseStockRejectTransService baseStockRejectTransService;

    @Autowired
    @Qualifier("baseStockStaffOrderService")
    BaseStockStaffOrderService baseStockStaffOrderService;

    @Autowired
    @Qualifier("baseStockStaffNoteService")
    BaseStockStaffNoteService baseStockStaffNoteService;
    @Autowired
    @Qualifier("baseStockAgentRetrieveImportService")
    BaseStockAgentRetrieveImportService baseStockAgentRetrieveImportService;
    @Autowired
    @Qualifier("baseStockAgentRetrieveCancelService")
    BaseStockAgentRetrieveCancelService baseStockAgentRetrieveCancelService;

    @Autowired
    @Qualifier("baseStockExpNoteAgentService")
    BaseStockExpNoteAgentService baseStockExpNoteAgentService;

    @Autowired
    @Qualifier("baseIsdnExportService")
    BaseIsdnExportService baseIsdnExportService;

    @Autowired
    @Qualifier("baseStockCancelTransAgentService")
    BaseStockCancelTransAgentService baseStockCancelTransAgentService;

    @Autowired
    @Qualifier("baseStockCollaboratorExpService")
    BaseStockCollaboratorExpService baseStockCollaboratorExpService;

    @Autowired
    @Qualifier("baseStockCollaboratorRetrieveService")
    BaseStockCollaboratorRetrieveService baseStockCollaboratorRetrieveService;

    @Autowired
    @Qualifier("baseStockCancelTransIsdnService")
    BaseStockCancelTransIsdnService baseStockCancelTransIsdnService;

    @Autowired
    @Qualifier("baseStockAcencyDepositMoneyServices")
    BaseStockAcencyDepositMoneyServices baseStockAcencyDepositMoneyServices;


    @Autowired
    @Qualifier("baseStockAcencyCancelDepositMoneyServices")
    BaseStockAcencyCancelDepositMoneyServices baseStockAcencyCancelDepositMoneyServices;

    @Autowired
    @Qualifier("baseStockAgencyRetrieveExpenseService")
    BaseStockAgencyRetrieveExpenseService baseStockAgencyRetrieveExpenseService;

    @Autowired
    @Qualifier("baseStockAgentExpRequestService")
    BaseStockAgentExpRequestService baseStockAgentExpRequestService;

    @Autowired
    @Qualifier("baseStockExpToPartnerService")
    BaseStockExpToPartnerService baseStockExpToPartnerService;

    @Autowired
    @Qualifier("baseStockExpToPartnerBalanceService")
    BaseStockExpToPartnerBalanceService baseStockExpToPartnerBalanceService;

    @Autowired
    @Qualifier("baseStockImpToPartnerBalanceService")
    BaseStockImpToPartnerBalanceService baseStockImpToPartnerBalanceService;

    @Autowired
    @Qualifier("baseStockExportOrderAndNoteService")
    BaseStockExportOrderAndNoteService baseStockExportOrderAndNoteService;

    @Autowired
    @Qualifier("baseStaffImportOrderAndNoteService")
    BaseStaffImportOrderAndNoteService baseStaffImportOrderAndNoteService;

    @Autowired
    @Qualifier("baseStockImportOrderAndNoteService")
    BaseStockImportOrderAndNoteService baseStockImportOrderAndNoteService;

    @Autowired
    @Qualifier("baseStockNoteChangeProductService")
    BaseStockNoteChangeProductService baseStockNoteChangeProductService;

    @Autowired
    @Qualifier("baseChangeTerminalDevideService")
    BaseChangeTerminalDevideService baseChangeTerminalDevideService;

    @Autowired
    @Qualifier("productExchangeServiceImpl")
    ProductExchangeServiceImpl productExchangeServiceImpl;

    @Autowired
    @Qualifier("baseStockDemoExportService")
    BaseStockDemoExportService baseStockDemoExportService;

    @Autowired
    @Qualifier("baseStockDemoImportService")
    BaseStockDemoImportService baseStockDemoImportService;

    public BaseStockService getInstance(String mode, String type) {

        if (DataUtil.safeEqual(Const.STOCK_TRANS.ORDER, mode) && DataUtil.safeEqual(Const.STOCK_TRANS_TYPE.EXPORT, type)) {
            return baseStockOutOrderService; //Lap lenh xuat kho
        } else if (DataUtil.safeEqual(Const.STOCK_TRANS.NOTE_ORDER, mode) && DataUtil.safeEqual(Const.STOCK_TRANS_TYPE.EXPORT, type)) {
            return baseStockNoteOrderService; //lap phieu xuat kho da lap lenh
        } else if (DataUtil.safeEqual(Const.STOCK_TRANS.NOTE_AGENT, mode) && DataUtil.safeEqual(Const.STOCK_TRANS_TYPE.EXPORT, type)) {
            return baseStockNoteAgentService; //lap phieu xuat kho da lap lenh
        } else if (DataUtil.safeEqual(Const.STOCK_TRANS.EXPORT_NOTE, mode) && DataUtil.safeEqual(Const.STOCK_TRANS_TYPE.EXPORT, type)) {
            return baseStockExpNoteService; //Xuat kho khi da lap phieu xuat
        } else if (DataUtil.safeEqual(Const.STOCK_TRANS.NOTE, mode) && DataUtil.safeEqual(Const.STOCK_TRANS_TYPE.IMPORT, type)) {
            return baseStockInNoteService; //Lap phieu nhap kho
        } else if (DataUtil.safeEqual(Const.STOCK_TRANS.EXPORT_NOTE, mode) && DataUtil.safeEqual(Const.STOCK_TRANS_TYPE.IMPORT, type)) {
            return baseStockImpNoteService; //Nhap kho
        } else if (DataUtil.safeEqual(Const.STOCK_TRANS.NOTE, mode) && DataUtil.safeEqual(Const.STOCK_TRANS_TYPE.EXPORT, type)) {
            return baseStockOutNoteService; //Lap phieu xuat kho
        } else if (DataUtil.safeEqual(Const.STOCK_TRANS.ORDER, mode) && DataUtil.safeEqual(Const.STOCK_TRANS_TYPE.IMPORT, type)) {
            return baseStockInOrderService; //Lap lenh nhap kho tu cap duoi
        } else if (DataUtil.safeEqual(Const.STOCK_TRANS.ORDER, mode) && DataUtil.safeEqual(Const.STOCK_TRANS_TYPE.ISDN, type)) {
            return baseIsdnOrderService; //Lap lenh xuat so
        } else if (DataUtil.safeEqual(Const.STOCK_TRANS.EXPORT_NOTE, mode) && DataUtil.safeEqual(Const.STOCK_TRANS_TYPE.ISDN, type)) {
            return baseIsdnExportService; //Lap phieu xuat so
        } else if (DataUtil.safeEqual(Const.STOCK_TRANS.EXPORT, mode) && DataUtil.safeEqual(Const.STOCK_TRANS_TYPE.ISDN, type)) {
            return baseStockIsdnExpService; //Xuat so
        } else if (DataUtil.safeEqual(Const.STOCK_TRANS.CANCEL_TRANS, mode) && DataUtil.safeEqual(Const.STOCK_TRANS_TYPE.ISDN, type)) {
            return baseStockCancelTransIsdnService; //Huy giao dich so
        } else if (DataUtil.safeEqual(Const.STOCK_TRANS.EXPORT, mode) && DataUtil.safeEqual(Const.STOCK_TRANS_TYPE.STAFF_IMP, type)) {
            return baseStockStaffImpService; //Nhan vien xac nhan nhap hang
        } else if (DataUtil.safeEqual(Const.STOCK_TRANS.EXPORT, mode) && DataUtil.safeEqual(Const.STOCK_TRANS_TYPE.STAFF_EXP, type)) {
            return baseStockStaffExpService; //Nhan vien xuat tra hang
        } else if (DataUtil.safeEqual(Const.STOCK_TRANS.ORDER_AGENT, mode) && DataUtil.safeEqual(Const.STOCK_TRANS_TYPE.EXPORT, type)) {
            return baseStockAgentExpService; //Lap lenh xuat kho dai ly
        } else if (DataUtil.safeEqual(Const.STOCK_TRANS.NOTE_AGENT_RETRIEVE, mode) && DataUtil.safeEqual(Const.STOCK_TRANS_TYPE.AGENT, type)) {
            return baseStockAgentRetrieveService; //Lap phieu thu hoi dai ly
        } else if (DataUtil.safeEqual(Const.STOCK_TRANS.CANCEL_TRANS, mode)) {
            return baseStockCancelTransService; //Huy giao dich
        } else if (DataUtil.safeEqual(Const.STOCK_TRANS.REJECT_TRANS, mode)) {
            return baseStockRejectTransService; //Tu choi giao dich
        } else if (DataUtil.safeEqual(Const.STOCK_TRANS.ORDER_FROM_STAFF, mode)) {
            return baseStockStaffOrderService; //Lap lenh nhap kho tu nhan vien
        } else if (DataUtil.safeEqual(Const.STOCK_TRANS.NOTE_FROM_STAFF, mode)) {
            return baseStockStaffNoteService; //Lap phieu nhap kho tu nhan vien
        } else if (DataUtil.safeEqual(Const.STOCK_TRANS.NOTE_AGENT_RETRIEVE, mode) && DataUtil.safeEqual(Const.STOCK_TRANS_TYPE.IMPORT, type)) {
            return baseStockAgentRetrieveImportService; //Nhap kho thu hoi dai ly
        } else if (DataUtil.safeEqual(Const.STOCK_TRANS.NOTE_AGENT_RETRIEVE, mode) && DataUtil.safeEqual(Const.STOCK_STRANS_DEPOSIT.DEPOSIT_IMPORT, type)) {
            return baseStockAgentRetrieveCancelService; //Huy phieu thu hoi
        } else if (DataUtil.safeEqual(Const.STOCK_TRANS.EXPORT_AGENT, mode) && DataUtil.safeEqual(Const.STOCK_TRANS_TYPE.EXPORT, type)) {
            return baseStockExpNoteAgentService; // xuat kho cho dai ly
        } else if (DataUtil.safeEqual(Const.STOCK_TRANS.CANCEL_TRANS_AGENT, mode)) {
            return baseStockCancelTransAgentService;
        } else if (DataUtil.safeEqual(Const.STOCK_TRANS.EXPORT_COLLABORATOR, mode) && DataUtil.safeEqual(Const.STOCK_TRANS_TYPE.EXPORT, type)) {
            return baseStockCollaboratorExpService; //Lap lenh xuat kho NVDB/DB
        } else if (DataUtil.safeEqual(Const.STOCK_TRANS.COOLLABORATOR_RETRIEVE, mode) && DataUtil.safeEqual(Const.STOCK_TRANS_TYPE.IMPORT, type)) {
            return baseStockCollaboratorRetrieveService; //Lap phieu thu hoi CTV/DB
        } else if (DataUtil.safeEqual(Const.STOCK_TRANS.STOCK_TRANS_DEPOSIT, mode) && DataUtil.safeEqual(Const.STOCK_TRANS_TYPE.EXPORT, type)) {
            return baseStockAcencyDepositMoneyServices; //Lap phieu thu hoi CTV/DB
        } else if (DataUtil.safeEqual(Const.STOCK_TRANS.STOCK_TRANS_DEPOSIT_CANCEl, mode) && DataUtil.safeEqual(Const.STOCK_TRANS_TYPE.EXPORT, type)) {
            return baseStockAcencyCancelDepositMoneyServices; //Lap phieu thu hoi CTV/DB
        } else if (DataUtil.safeEqual(Const.STOCK_TRANS.RETRIEVE_EXPENSE, mode) && DataUtil.safeEqual(Const.STOCK_TRANS_TYPE.EXPORT, type)) {
            return baseStockAgencyRetrieveExpenseService; //Lap phieu chi tra tien dat coc
        } else if (DataUtil.safeEqual(Const.STOCK_TRANS.ORDER_AGENT, mode) && DataUtil.safeEqual(Const.STOCK_TRANS_TYPE.EXPORT_REQUEST, type)) {
            return baseStockAgentExpRequestService; //Lap lenh xuat kho dai ly tu yeu cau
        } else if (DataUtil.safeEqual(Const.STOCK_TRANS.PARTNER, mode) && DataUtil.safeEqual(Const.STOCK_TRANS_TYPE.EXPORT, type)) {
            return baseStockExpToPartnerService; //xuat kho cho doi tac
        } else if (DataUtil.safeEqual(Const.STOCK_TRANS.PARTNER_BALANCE, mode) && DataUtil.safeEqual(Const.STOCK_TRANS_TYPE.EXPORT, type)) {
            return baseStockExpToPartnerBalanceService; //xuat hang can kho
        } else if (DataUtil.safeEqual(Const.STOCK_TRANS.PARTNER_BALANCE, mode) && DataUtil.safeEqual(Const.STOCK_TRANS_TYPE.IMPORT, type)) {
            return baseStockImpToPartnerBalanceService; //nhap hang can kho
        } else if (DataUtil.safeEqual(Const.STOCK_TRANS.ORDER_AND_NOTE, mode) && DataUtil.safeEqual(Const.STOCK_TRANS_TYPE.EXPORT, type)) {
            return baseStockExportOrderAndNoteService; //lap lenh, lap phieu xuat tu dong
        } else if (DataUtil.safeEqual(Const.STOCK_TRANS.ORDER_AND_NOTE_STAFF, mode) && DataUtil.safeEqual(Const.STOCK_TRANS_TYPE.IMPORT, type)) {
            return baseStaffImportOrderAndNoteService; //lap lenh, lap phieu nhap tu dong
        } else if (DataUtil.safeEqual(Const.STOCK_TRANS.ORDER_AND_NOTE, mode) && DataUtil.safeEqual(Const.STOCK_TRANS_TYPE.IMPORT, type)) {
            return baseStockImportOrderAndNoteService; //lap lenh, lap phieu nhap tu dong
        } else if (DataUtil.safeEqual(Const.STOCK_TRANS.NOTE_CHANGE_PRODUCT, mode) && DataUtil.safeEqual(Const.STOCK_TRANS_TYPE.EXPORT, type)) {
            return baseStockNoteChangeProductService; //lap phieu chuyen doi mat hang
        } else if (DataUtil.safeEqual(Const.STOCK_TRANS.CHANGE_PRODUCT_DAMAGE, mode) && DataUtil.safeEqual(Const.STOCK_TRANS_TYPE.EXPORT, type)) {
            return baseChangeTerminalDevideService; //doi thiet bi dau cuoi
        } else if (DataUtil.safeEqual(Const.STOCK_TRANS.PRODUCT_EXCHANGE_KIT, mode) && DataUtil.safeEqual(Const.STOCK_TRANS_TYPE.EXPORT, type)) {
            return productExchangeServiceImpl; //doi thiet bi dau cuoi
        } else if (DataUtil.safeEqual(Const.STOCK_TRANS.EXPORT_DEMO, mode) && DataUtil.safeEqual(Const.STOCK_TRANS_TYPE.EXPORT, type)) {
            return baseStockDemoExportService; //xuat hang demo
        } else if (DataUtil.safeEqual(Const.STOCK_TRANS.EXPORT_DEMO, mode) && DataUtil.safeEqual(Const.STOCK_TRANS_TYPE.IMPORT, type)) {
            return baseStockDemoImportService; //thu hoi hang demo
        }
        return null;
    }

}
