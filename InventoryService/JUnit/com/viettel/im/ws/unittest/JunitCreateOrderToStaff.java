package com.viettel.im.ws.unittest;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.StockTransActionDTO;
import com.viettel.bccs.inventory.dto.StockTransDTO;
import com.viettel.bccs.inventory.dto.StockTransDetailDTO;
import com.viettel.bccs.inventory.service.BaseStock.BaseStockOutOrderService;
import com.viettel.fw.Exception.LogicException;
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
public class JunitCreateOrderToStaff {
    @Autowired
    private BaseStockOutOrderService baseStockOutOrderService;

    private RequiredRoleMap requiredRoleMap;
    private StockTransDTO stockTransDTO = new StockTransDTO();
    private List<StockTransDetailDTO> lstStockTransDetailDTO = new ArrayList<>();
    private StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();

    @Test
    public void testValidate_allObjectNull() {
        try {
            baseStockOutOrderService.executeStockTrans(null, null, null, requiredRoleMap);
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
            baseStockOutOrderService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
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
            baseStockOutOrderService.executeStockTrans(null, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
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
            baseStockOutOrderService.executeStockTrans(stockTransDTO, null, lstStockTransDetailDTO, requiredRoleMap);
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
            baseStockOutOrderService.executeStockTrans(stockTransDTO, stockTransActionDTO, null, requiredRoleMap);
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
            baseStockOutOrderService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, null);
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            TestCase.assertEquals(lex.getKeyMsg(), "export.order.stock.require.msg");
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
            baseStockOutOrderService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            TestCase.assertEquals(lex.getKeyMsg(), desc);
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
            baseStockOutOrderService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            TestCase.assertEquals(lex.getKeyMsg(), "stockTrans.validate.fromStock.notExists");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    //Validate kho xuat
    @Test
    public void testValidate_ToOwnerId() {
        try {
            initStockTransDTO();
            initStockTransAction();
            initListStockTransDetailDTO();
            stockTransDTO.setToOwnerId(9999999999999999L);
            baseStockOutOrderService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
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
            baseStockOutOrderService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            TestCase.assertEquals(lex.getKeyMsg(), "stockTrans.validate.stock.notConsistent");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    //Validate reasonID
    @Test
    public void testValidate_Reason() {
        try {
            initStockTransDTO();
            initStockTransAction();
            initListStockTransDetailDTO();
            stockTransDTO.setReasonId(999999999999999L);
            baseStockOutOrderService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            TestCase.assertEquals(lex.getKeyMsg(), "stockTrans.validate.reason.notExists");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    //Validate han muc
    @Test
    public void testValidate_Debit() {
        try {
            initStockTransDTO();
            initStockTransAction();
            initListStockTransDetailDTO();
            StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
            stockTransDetailDTO.setProdOfferId(5180L);
            stockTransDetailDTO.setStateId(1L);
            stockTransDetailDTO.setQuantity(1000000L);
            lstStockTransDetailDTO.add(stockTransDetailDTO);
            baseStockOutOrderService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            TestCase.assertEquals(lex.getKeyMsg(), "stockTrans.validate.debit.over");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    //Validate Note
    @Test
    public void testValidate_Note() {
        try {
            initStockTransDTO();
            initStockTransAction();
            initListStockTransDetailDTO();
            stockTransDTO.setNote("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            baseStockOutOrderService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            TestCase.assertEquals(lex.getKeyMsg(), "stockTrans.validate.note.overLength");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    //Validate Can cu xuat kho
    @Test
    public void testValidate_StockBase() {
        try {
            initStockTransDTO();
            initStockTransAction();
            initListStockTransDetailDTO();
            stockTransDTO.setStockBase("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            baseStockOutOrderService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            TestCase.assertEquals(lex.getKeyMsg(), "stockTrans.validate.stockBase.overLength");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    //Validate voffice userName
    @Test
    public void testValidate_voffice_userName() {
        try {
            initStockTransDTO();
            initStockTransAction();
            initListStockTransDetailDTO();
            stockTransDTO.setSignVoffice(true);
            baseStockOutOrderService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            TestCase.assertEquals(lex.getKeyMsg(), "common.user.require.msg");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    //Validate voffice password
    @Test
    public void testValidate_voffice_passWord() {
        try {
            initStockTransDTO();
            initStockTransAction();
            initListStockTransDetailDTO();
            stockTransDTO.setSignVoffice(true);
            stockTransDTO.setUserName("xxx");
            baseStockOutOrderService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            TestCase.assertEquals(lex.getKeyMsg(), "common.password.require.msg");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    //Validate voffice luong ky
    @Test
    public void testValidate_voffice_signFlow() {
        try {
            initStockTransDTO();
            initStockTransAction();
            initListStockTransDetailDTO();
            stockTransDTO.setSignVoffice(true);
            stockTransDTO.setUserName("xxx");
            stockTransDTO.setPassWord("xxx");
            baseStockOutOrderService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            TestCase.assertEquals(lex.getKeyMsg(), "export.order.airflow.require.msg");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void testValidate_voffice_OK() {
        try {
            initStockTransDTO();
            initStockTransAction();
            initListStockTransDetailDTO();
            stockTransDTO.setSignVoffice(true);
            stockTransDTO.setUserName("xxx");
            stockTransDTO.setPassWord("xxx");
            baseStockOutOrderService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            TestCase.assertEquals(lex.getKeyMsg(), "export.order.airflow.require.msg");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    public void initStockTransDTO() {
        stockTransDTO.setFromOwnerId(7282L);
        stockTransDTO.setFromOwnerType(1L);
        stockTransDTO.setToOwnerId(998877L);
        stockTransDTO.setToOwnerType(2L);
        stockTransDTO.setReasonId(9166L);
        stockTransDTO.setCreateDatetime(new Date());
        stockTransDTO.setStaffId(998877L);
        stockTransDTO.setNote("unitTest");

    }

    public void initStockTransAction() {
        stockTransActionDTO.setActionCode("LX_01");
    }

    public void initListStockTransDetailDTO() {
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId(5180L);
        stockTransDetailDTO.setStateId(1L);
        stockTransDetailDTO.setQuantity(1L);
        lstStockTransDetailDTO.add(stockTransDetailDTO);
    }

}
