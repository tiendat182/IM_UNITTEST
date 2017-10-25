package com.viettel.im.ws.unittest;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.StockTransActionDTO;
import com.viettel.bccs.inventory.dto.StockTransDTO;
import com.viettel.bccs.inventory.dto.StockTransDetailDTO;
import com.viettel.bccs.inventory.dto.StockTransSerialDTO;
import com.viettel.bccs.inventory.service.ExecuteStockTransService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.dto.BaseMessage;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by sinhhv on 2/24/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MainApplication.class)
public class JunitExportStockIsdn {

    @Autowired
    private ExecuteStockTransService executeStockTransService;

    StockTransDTO stockTransDTO;
    StockTransActionDTO stockTransActionDTO;
    List<StockTransDetailDTO> stockTransDetailDTOs;
    RequiredRoleMap requiredRoleMap;

    @Test
    public void statusInvalid() {
        try {
            prepareInput();
            stockTransDTO.setStockTransId(2009042897L);
            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.EXPORT_NOTE, Const.STOCK_TRANS_TYPE.ISDN, stockTransDTO, stockTransActionDTO, stockTransDetailDTOs, requiredRoleMap);
            TestCase.assertEquals(message.getKeyMsg(), "mn.stock.status.invalid");
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "mn.stock.status.invalid");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void fromOwnerIdNull() {
        try {
            prepareInput();
            stockTransDTO.setFromOwnerId(null);
            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.EXPORT_NOTE, Const.STOCK_TRANS_TYPE.ISDN, stockTransDTO, stockTransActionDTO, stockTransDetailDTOs, requiredRoleMap);
            TestCase.assertEquals(message.getKeyMsg(), "export.order.stock.require.msg");
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "export.order.stock.require.msg");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void fromOwnerTypeNull() {
        try {
            prepareInput();
            stockTransDTO.setFromOwnerType(null);
            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.EXPORT_NOTE, Const.STOCK_TRANS_TYPE.ISDN, stockTransDTO, stockTransActionDTO, stockTransDetailDTOs, requiredRoleMap);
            TestCase.assertEquals(message.getKeyMsg(), "export.order.stock.require.msg");
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "export.order.stock.require.msg");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void fromOwnerInvalid() {
        try {
            prepareInput();
            stockTransDTO.setFromOwnerId(-1L);
            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.EXPORT_NOTE, Const.STOCK_TRANS_TYPE.ISDN, stockTransDTO, stockTransActionDTO, stockTransDetailDTOs, requiredRoleMap);
            TestCase.assertEquals(message.getKeyMsg(), "stockTrans.validate.fromStock.notExists");
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "stockTrans.validate.fromStock.notExists");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void toOwnerIdNull() {
        try {
            prepareInput();
            stockTransDTO.setToOwnerId(null);
            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.EXPORT_NOTE, Const.STOCK_TRANS_TYPE.ISDN, stockTransDTO, stockTransActionDTO, stockTransDetailDTOs, requiredRoleMap);
            TestCase.assertEquals(message.getKeyMsg(), "store.import.stock.not.blank");
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "store.import.stock.not.blank");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void toOwnerTypeNull() {
        try {
            prepareInput();
            stockTransDTO.setToOwnerType(null);
            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.EXPORT_NOTE, Const.STOCK_TRANS_TYPE.ISDN, stockTransDTO, stockTransActionDTO, stockTransDetailDTOs, requiredRoleMap);
            TestCase.assertEquals(message.getKeyMsg(), "store.import.stock.not.blank");
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "store.import.stock.not.blank");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void fromToOwnerSame() {
        try {
            prepareInput();
            stockTransDTO.setFromOwnerId(7282L);
            stockTransDTO.setToOwnerId(7282L);
            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.EXPORT_NOTE, Const.STOCK_TRANS_TYPE.ISDN, stockTransDTO, stockTransActionDTO, stockTransDetailDTOs, requiredRoleMap);
            TestCase.assertEquals(message.getKeyMsg(), "stockTrans.validate.fromtoStock.equal");
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "stockTrans.validate.fromtoStock.equal");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void fromOwnerStop() {
        try {
            prepareInput();
            stockTransDTO.setStockTransId(21735L);
            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.EXPORT_NOTE, Const.STOCK_TRANS_TYPE.ISDN, stockTransDTO, stockTransActionDTO, stockTransDetailDTOs, requiredRoleMap);
            TestCase.assertEquals(message.getKeyMsg(), "mn.isdn.manage.creat.field.export.fromStop");
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "mn.isdn.manage.creat.field.export.fromStop");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void toOwnerStop() {
        try {
            prepareInput();
            stockTransDTO.setStockTransId(21734L);
            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.EXPORT_NOTE, Const.STOCK_TRANS_TYPE.ISDN, stockTransDTO, stockTransActionDTO, stockTransDetailDTOs, requiredRoleMap);
            TestCase.assertEquals(message.getKeyMsg(), "mn.isdn.manage.creat.field.export.toStop");
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "mn.isdn.manage.creat.field.export.toStop");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void lstProductNull() {
        try {
            prepareInput();
            stockTransDetailDTOs = null;
            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.EXPORT_NOTE, Const.STOCK_TRANS_TYPE.ISDN, stockTransDTO, stockTransActionDTO, stockTransDetailDTOs, requiredRoleMap);
            TestCase.assertEquals(message.getKeyMsg(), "mn.isdn.manage.creat.field.export.notDetail");
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "mn.isdn.manage.creat.field.export.notDetail");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void productInvalid() {
        try {
            prepareInput();
            stockTransDetailDTOs.get(0).setProdOfferId(516L);
            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.EXPORT_NOTE, Const.STOCK_TRANS_TYPE.ISDN, stockTransDTO, stockTransActionDTO, stockTransDetailDTOs, requiredRoleMap);
            TestCase.assertEquals(message.getKeyMsg(), "mn.isdn.manage.creat.field.export.invalidProd");
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "mn.isdn.manage.creat.field.export.invalidProd");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void proStatusInvalid() {
        try {
            prepareInput();
            stockTransDetailDTOs.get(0).setProdOfferId(1259L);
            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.EXPORT_NOTE, Const.STOCK_TRANS_TYPE.ISDN, stockTransDTO, stockTransActionDTO, stockTransDetailDTOs, requiredRoleMap);
            TestCase.assertEquals(message.getKeyMsg(), "mn.isdn.manage.creat.field.export.invalidProd");
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "mn.isdn.manage.creat.field.export.invalidProd");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void quantityNotEnough() {
        try {
            prepareInput();
            stockTransDetailDTOs.get(0).setQuantity(0L);
            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.EXPORT_NOTE, Const.STOCK_TRANS_TYPE.ISDN, stockTransDTO, stockTransActionDTO, stockTransDetailDTOs, requiredRoleMap);
            TestCase.assertEquals(message.getKeyMsg(), "mn.isdn.manage.creat.field.export.notEnoughIsdn");
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "mn.isdn.manage.creat.field.export.notEnoughIsdn");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    private void prepareInput() {
        //cb du lieu stocktrans
        stockTransDTO = new StockTransDTO();
        stockTransDTO.setFromOwnerId(7282L);
        stockTransDTO.setFromOwnerType(1L);
        stockTransDTO.setNote("xuat kho");
        stockTransDTO.setReasonId(4456L);
        stockTransDTO.setStatus("1");
        stockTransDTO.setStockTransId(21881L);
        stockTransDTO.setToOwnerId(24926L);
        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setStockTransActionId(484750L);
        stockTransDTO.setUserName("sinhhv");
        stockTransDTO.setPassWord("76-118-186-121-30-181-97-240-213-93-16-145-103-162-27-41-66-72-242-80-120-187-233-91-62-244-62-213-244-188-4-244");
        stockTransDTO.setSignFlowId(1L);
        stockTransDTO.setTransport("1");
        stockTransDTO.setSignVoffice(true);
        stockTransDTO.setStockTransType("6");

        //cb du lieu stocktransaction
        stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setActionCode("PX_VT_PRODUCT_000171");
        stockTransActionDTO.setActionStaffId(9999999993L);
        stockTransActionDTO.setCreateUser("product");
        stockTransActionDTO.setStatus("1");
        stockTransActionDTO.setStockTransActionId(484750L);
        stockTransActionDTO.setSignCaStatus("2");
        stockTransActionDTO.setIsRegionExchange(false);

        //cb du lieu list stocktransdetail
        stockTransDetailDTOs = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setAmount(null);
        stockTransDetailDTO.setProdOfferId(11992L);
        stockTransDetailDTO.setQuantity(5L);
        stockTransDetailDTO.setStateId(1L);
        stockTransDetailDTO.setStockTransDetailId(3141L);
        stockTransDetailDTO.setProdOfferTypeId(1L);
        stockTransDetailDTO.setTableName("STOCK_NUMBER");
        StockTransSerialDTO serial = new StockTransSerialDTO();
        serial.setFromSerial("980068991");
        serial.setToSerial("980068995");
        serial.setQuantity(5L);
        stockTransDetailDTO.setLstStockTransSerial(Lists.newArrayList(serial));
        stockTransDetailDTOs = Lists.newArrayList(stockTransDetailDTO);
        //cb role
        requiredRoleMap = new RequiredRoleMap();
        requiredRoleMap.setValues(Lists.newArrayList("role_stockNumShop"));
    }

}
