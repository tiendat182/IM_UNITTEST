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
 * Created by sinhhv on 2/25/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MainApplication.class)
public class JunitTransferStockIsdn {
    @Autowired
    private ExecuteStockTransService executeStockTransService;
    StockTransDTO stockTransDTO;
    StockTransActionDTO stockTransActionDTO;
    List<StockTransDetailDTO> lsDetailDTOs;
    RequiredRoleMap requiredRoleMap;

    private void prepareInput() {
        stockTransDTO = new StockTransDTO();
        stockTransDTO.setFromOwnerId(7282L);
        stockTransDTO.setFromOwnerType(1L);
        stockTransDTO.setNote("xuat kho");
        stockTransDTO.setReasonId(4456L);
        stockTransDTO.setStockTransId(21641L);
        stockTransDTO.setToOwnerId(24926L);
        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setFromOwnerCode("VT");
        stockTransDTO.setFromOwnerName("Tổng Công ty Viễn thông Viettel");
        stockTransDTO.setToOwnerCode("CN_HNI");
        stockTransDTO.setToOwnerName("Chi nhánh Hà Nội");
        stockTransDTO.setStockTransActionId(485086L);
        stockTransDTO.setUserCreate("product");
        stockTransDTO.setStaffId(9999999993L);
        stockTransDTO.setReasonName("Xuất điều chuyển xuống kho cấp dưới");
        stockTransDTO.setReasonCode("XDCD");
        stockTransDTO.setUserName("product1");
        stockTransDTO.setTransport("1");
        stockTransDTO.setSignVoffice(false);
        stockTransDTO.setCreateUserIpAdress("127.0.0.1");

        stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setActionStaffId(9999999993L);
        stockTransActionDTO.setCreateUser("product");
        stockTransActionDTO.setStockTransActionId(485086L);
        stockTransActionDTO.setIsRegionExchange(false);

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
        lsDetailDTOs = Lists.newArrayList(stockTransDetailDTO);
        //cb role
        requiredRoleMap = new RequiredRoleMap();
        requiredRoleMap.setValues(Lists.newArrayList("role_stockNumShop"));
    }

    @Test
    public void fromStockNull() {
        try {
            prepareInput();
            stockTransDTO.setFromOwnerId(null);
            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.EXPORT, Const.STOCK_TRANS_TYPE.ISDN, stockTransDTO, stockTransActionDTO, lsDetailDTOs, requiredRoleMap);
            TestCase.assertEquals(message.getKeyMsg(), "transfer.isdn.from.stock.not.null");
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "transfer.isdn.from.stock.not.null");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void fromStockTypeNull() {
        try {
            prepareInput();
            stockTransDTO.setFromOwnerType(null);
            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.EXPORT, Const.STOCK_TRANS_TYPE.ISDN, stockTransDTO, stockTransActionDTO, lsDetailDTOs, requiredRoleMap);
            TestCase.assertEquals(message.getKeyMsg(), "transfer.isdn.from.stock.not.null");
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "transfer.isdn.from.stock.not.null");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void fromStockInvalid() {
        try {
            prepareInput();
            stockTransDTO.setFromOwnerId(-10L);
            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.EXPORT, Const.STOCK_TRANS_TYPE.ISDN, stockTransDTO, stockTransActionDTO, lsDetailDTOs, requiredRoleMap);
            TestCase.assertEquals(message.getKeyMsg(), "transfer.isdn.from.stock.invalid");
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "transfer.isdn.from.stock.invalid");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void toStockNUll() {
        try {
            prepareInput();
            stockTransDTO.setToOwnerId(null);
            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.EXPORT, Const.STOCK_TRANS_TYPE.ISDN, stockTransDTO, stockTransActionDTO, lsDetailDTOs, requiredRoleMap);
            TestCase.assertEquals(message.getKeyMsg(), "transfer.isdn.to.stock.not.null");
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "transfer.isdn.to.stock.not.null");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void toStockStypeNUll() {
        try {
            prepareInput();
            stockTransDTO.setToOwnerId(null);
            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.EXPORT, Const.STOCK_TRANS_TYPE.ISDN, stockTransDTO, stockTransActionDTO, lsDetailDTOs, requiredRoleMap);
            TestCase.assertEquals(message.getKeyMsg(), "transfer.isdn.to.stock.not.null");
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "transfer.isdn.to.stock.not.null");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void toStockInvalid() {
        try {
            prepareInput();
            stockTransDTO.setToOwnerId(-10L);
            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.EXPORT, Const.STOCK_TRANS_TYPE.ISDN, stockTransDTO, stockTransActionDTO, lsDetailDTOs, requiredRoleMap);
            TestCase.assertEquals(message.getKeyMsg(), "transfer.isdn.to.stock.invalid");
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "transfer.isdn.to.stock.invalid");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void fromToStockSame() {
        try {
            prepareInput();
            stockTransDTO.setFromOwnerId(7282L);
            stockTransDTO.setToOwnerId(7282L);
            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.EXPORT, Const.STOCK_TRANS_TYPE.ISDN, stockTransDTO, stockTransActionDTO, lsDetailDTOs, requiredRoleMap);
            TestCase.assertEquals(message.getKeyMsg(), "stockTrans.validate.fromtoStock.equal");
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "stockTrans.validate.fromtoStock.equal");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void reasonNull() {
        try {
            prepareInput();
            stockTransDTO.setReasonId(null);
            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.EXPORT, Const.STOCK_TRANS_TYPE.ISDN, stockTransDTO, stockTransActionDTO, lsDetailDTOs, requiredRoleMap);
            TestCase.assertEquals(message.getKeyMsg(), "create.note.reason.export.require.msg");
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "create.note.reason.export.require.msg");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void reasonNotExist() {
        try {
            prepareInput();
            stockTransDTO.setReasonId(-19L);
            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.EXPORT, Const.STOCK_TRANS_TYPE.ISDN, stockTransDTO, stockTransActionDTO, lsDetailDTOs, requiredRoleMap);
            TestCase.assertEquals(message.getKeyMsg(), "stockTrans.validate.reason.notExists");
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "stockTrans.validate.reason.notExists");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void noteOverMaxLength() {
        try {
            prepareInput();
            stockTransDTO.setNote("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.EXPORT, Const.STOCK_TRANS_TYPE.ISDN, stockTransDTO, stockTransActionDTO, lsDetailDTOs, requiredRoleMap);
            TestCase.assertEquals(message.getKeyMsg(), "stockTrans.validate.note.overLength");
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "stockTrans.validate.note.overLength");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void statusNotValid() {
        try {
            prepareInput();
            stockTransDTO.setStockTransId(2009042897L);
            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.EXPORT, Const.STOCK_TRANS_TYPE.ISDN, stockTransDTO, stockTransActionDTO, lsDetailDTOs, requiredRoleMap);
            TestCase.assertEquals(message.getKeyMsg(), "transfer.isdn.status.invalid");
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "transfer.isdn.status.invalid");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void passVofficeNull() {
        try {
            prepareInput();
            stockTransDTO.setSignVoffice(true);
            stockTransDTO.setUserName("test");
            stockTransDTO.setPassWord(null);
            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.EXPORT, Const.STOCK_TRANS_TYPE.ISDN, stockTransDTO, stockTransActionDTO, lsDetailDTOs, requiredRoleMap);
            TestCase.assertEquals(message.getKeyMsg(), "common.password.require.msg");
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "common.password.require.msg");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void userVofficeNull() {
        try {
            prepareInput();
            stockTransDTO.setSignVoffice(true);
            stockTransDTO.setUserName(null);
            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.EXPORT, Const.STOCK_TRANS_TYPE.ISDN, stockTransDTO, stockTransActionDTO, lsDetailDTOs, requiredRoleMap);
            TestCase.assertEquals(message.getKeyMsg(), "common.user.require.msg");
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "common.user.require.msg");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void signFlowNull() {
        try {
            prepareInput();
            stockTransDTO.setSignVoffice(true);
            stockTransDTO.setUserName("abc");
            stockTransDTO.setPassWord("xyz");
            stockTransDTO.setSignFlowId(null);
            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.EXPORT, Const.STOCK_TRANS_TYPE.ISDN, stockTransDTO, stockTransActionDTO, lsDetailDTOs, requiredRoleMap);
            TestCase.assertEquals(message.getKeyMsg(), "export.order.airflow.require.msg");
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "export.order.airflow.require.msg");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void userVofficeOverLength() {
        try {
            prepareInput();
            stockTransDTO.setSignVoffice(true);
            stockTransDTO.setPassWord("asdasd");
            stockTransDTO.setSignFlowId(1L);
            stockTransDTO.setUserName("A1AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.EXPORT, Const.STOCK_TRANS_TYPE.ISDN, stockTransDTO, stockTransActionDTO, lsDetailDTOs, requiredRoleMap);
            TestCase.assertEquals(message.getKeyMsg(), "stockTransVoffice.validate.user.overLength");
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "stockTransVoffice.validate.user.overLength1");
        } catch (Exception ex) {
            TestCase.assertEquals(ex.getMessage(), "stockTransVoffice.validate.user.overLength1");
        }
    }

     @Test
     public void signFlowNotExist() {
         try {
             prepareInput();
             stockTransDTO.setSignVoffice(true);
             stockTransDTO.setPassWord("asdasd");
             stockTransDTO.setSignFlowId(-1L);
             stockTransDTO.setUserName("A1AAAAAAA");
             BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.EXPORT, Const.STOCK_TRANS_TYPE.ISDN, stockTransDTO, stockTransActionDTO, lsDetailDTOs, requiredRoleMap);
             TestCase.assertEquals(message.getKeyMsg(), "stockTrans.validate.signFlow.notExists");
         } catch (LogicException lex) {
             TestCase.assertEquals(lex.getKeyMsg(), "");
         } catch (Exception ex) {
             TestCase.assertEquals("Loi he thong", ex.getMessage());
         }
     }
}
