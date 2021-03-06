package com.viettel.im.ws.unittest;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.StockTransActionDTO;
import com.viettel.bccs.inventory.dto.StockTransDTO;
import com.viettel.bccs.inventory.dto.StockTransDetailDTO;
import com.viettel.bccs.inventory.dto.StockTransSerialDTO;
import com.viettel.bccs.inventory.service.BaseStock.BaseStockImpNoteService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.RequiredRoleMap;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by HaiNT41 on 1/22/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MainApplication.class)
public class JunitImportFromStaff {
    @Autowired
    private BaseStockImpNoteService baseStockImpNoteService;

    private RequiredRoleMap requiredRoleMap;
    private StockTransDTO stockTransDTO = new StockTransDTO();
    private List<StockTransDetailDTO> lstStockTransDetailDTO = new ArrayList();
    private StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
    private List<StockTransSerialDTO> lstStockTransSerialDTO = new ArrayList();

    @Test
    public void testValidate_allObjectNull() {
        try {
            baseStockImpNoteService.executeStockTrans(null, null, null, requiredRoleMap);
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            TestCase.assertEquals(lex.getKeyMsg(), desc);
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void testValidate_allObjectEmpty() {
        try {
            baseStockImpNoteService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            TestCase.assertEquals(lex.getKeyMsg(), "export.order.stock.require.msg");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void testValidate_stockTransNull() {
        try {
            initListStockTransDetailDTO();
            initStockTransAction();
            baseStockImpNoteService.executeStockTrans(null, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            TestCase.assertEquals(lex.getKeyMsg(), desc);
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void testValidate_stockTransActionNull() {
        try {
            initStockTransDTO();
            initListStockTransDetailDTO();
            baseStockImpNoteService.executeStockTrans(stockTransDTO, null, lstStockTransDetailDTO, requiredRoleMap);
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            TestCase.assertEquals(lex.getKeyMsg(), desc);
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void testValidate_stockTransIdNull() {
        try {
            initStockTransDTO();
            stockTransDTO.setStockTransId(null);
            initStockTransAction();
            initListStockTransDetailDTO();
            baseStockImpNoteService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            TestCase.assertEquals(lex.getKeyMsg(), desc);
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void testValidate_stockTransDetailNull() {
        try {
            initStockTransDTO();
            initStockTransAction();
            baseStockImpNoteService.executeStockTrans(stockTransDTO, stockTransActionDTO, null, requiredRoleMap);
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            TestCase.assertEquals(lex.getKeyMsg(), "export.order.stock.require.msg");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void testValidate_requiredRoleMapNull() {
        try {
            initStockTransDTO();
            initListStockTransDetailDTO();
            initStockTransAction();
            baseStockImpNoteService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, null);
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            TestCase.assertEquals(lex.getKeyMsg(), "export.order.stock.require.msg");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void testValidate_oldStockTransActionId() {
        try {
            initStockTransDTO();
            initStockTransAction();
            initListStockTransDetailDTO();
            stockTransActionDTO.setStockTransActionId(121212L);
            baseStockImpNoteService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            TestCase.assertEquals(lex.getKeyMsg(), "export.stock.code.export.not.found");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    //Validate kho xuat
    @Test
    public void testValidate_FromOwnerId() {
        try {
            initStockTransDTO();
            initStockTransAction();
            initListStockTransDetailDTO();
            stockTransDTO.setFromOwnerId(9999999999999999L);
            baseStockImpNoteService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            TestCase.assertEquals(lex.getKeyMsg(), "stockTrans.validate.fromStock.notExists");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    //Validate kho nhan
    @Test
    public void testValidate_ToOwnerId() {
        try {
            initStockTransDTO();
            initStockTransAction();
            initListStockTransDetailDTO();
            stockTransDTO.setToOwnerId(9999999999999999L);
            baseStockImpNoteService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            TestCase.assertEquals(lex.getKeyMsg(), "stockTrans.validate.toStock.notExists");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    //Validate kho xuat la SHOP cua kho nhan
    @Test
    public void testValidate_Shop() {
        try {
            initStockTransDTO();
            initStockTransAction();
            initListStockTransDetailDTO();
            stockTransDTO.setToOwnerId(20459013L);
            baseStockImpNoteService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            TestCase.assertEquals(lex.getKeyMsg(), "stockTrans.validate.stock.notConsistent");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void testValidate_invalidStatus() {
        try {
            initStockTransDTO();
            initStockTransAction();
            initListStockTransDetailDTO();
            stockTransDTO.setStatus("50");
            baseStockImpNoteService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            TestCase.assertEquals(lex.getKeyMsg(), "stock.import.stock.status.invalid");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }


    @Test
    public void testValidate_OK() {
        try {
            initStockTransDTO();
            initStockTransAction();
            initListStockTransDetailDTO();
            baseStockImpNoteService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            TestCase.assertEquals(lex.getKeyMsg(), desc);
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    public void initStockTransDTO() throws Exception {
        stockTransDTO.setStockTransId(1844L);
        stockTransDTO.setFromOwnerId(9999999993L);
        stockTransDTO.setFromOwnerType(2L);
        stockTransDTO.setToOwnerId(7282L);
        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setStockTransType("1");
        stockTransDTO.setStockTransStatus("5");
        stockTransDTO.setCreateDatetime(DateUtil.string2DateTime("02/02/2016 10:18:50"));
        stockTransDTO.setTotalAmount(10000L);
        stockTransDTO.setReasonId(4568L);
        stockTransDTO.setTransport("2");
        stockTransDTO.setNote("unitTest");
        stockTransDTO.setStaffId(9999999993L);
        stockTransDTO.setStockTransActionId(481918L);
        stockTransDTO.setImportReasonId(4514L);
        stockTransDTO.setFromStock(Const.FROM_STOCK.STAFF_SHOP);
    }

    public void initStockTransAction() {
        stockTransActionDTO.setStockTransActionId(481918L);
        stockTransActionDTO.setStockTransId(1844L);
        stockTransActionDTO.setActionCode("PN_VT_PRODUCT_000192");
        stockTransActionDTO.setActionStaffId(9999999993L);
    }

    public void initListStockTransDetailDTO() {
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setStockTransDetailId(2150L);
        stockTransDetailDTO.setStockTransId(1844L);
        stockTransDetailDTO.setProdOfferId(5180L);
        stockTransDetailDTO.setStateId(1L);
        stockTransDetailDTO.setQuantity(1L);
        stockTransDetailDTO.setPrice(10000L);
        stockTransDetailDTO.setAmount(10000L);
        initListStockTransSerialDTO();
        stockTransDetailDTO.setLstStockTransSerial(lstStockTransSerialDTO);
        lstStockTransDetailDTO.add(stockTransDetailDTO);
    }

    public void initListStockTransSerialDTO() {
        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        stockTransSerialDTO.setStockTransSerialId(1109L);
        stockTransSerialDTO.setStockTransDetailId(2150L);
        stockTransSerialDTO.setFromSerial("VN00014223");
        stockTransSerialDTO.setToSerial("VN00014223");
        stockTransSerialDTO.setQuantity(1L);
        lstStockTransSerialDTO.add(stockTransSerialDTO);
    }
}
