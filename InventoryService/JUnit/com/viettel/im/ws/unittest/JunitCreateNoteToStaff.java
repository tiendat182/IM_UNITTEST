package com.viettel.im.ws.unittest;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.StockTransActionDTO;
import com.viettel.bccs.inventory.dto.StockTransDTO;
import com.viettel.bccs.inventory.dto.StockTransDetailDTO;
import com.viettel.bccs.inventory.service.BaseStock.BaseStockNoteOrderService;
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
public class JunitCreateNoteToStaff {
    @Autowired
    private BaseStockNoteOrderService baseStockNoteOrderService;

    private RequiredRoleMap requiredRoleMap;
    private StockTransDTO stockTransDTO = new StockTransDTO();
    private List<StockTransDetailDTO> lstStockTransDetailDTO = new ArrayList<>();
    private StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();

    @Test
    public void testValidate_allObjectNull() {
        try {
            baseStockNoteOrderService.executeStockTrans(null, null, null, requiredRoleMap);
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
            baseStockNoteOrderService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
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
            baseStockNoteOrderService.executeStockTrans(null, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
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
            baseStockNoteOrderService.executeStockTrans(stockTransDTO, null, lstStockTransDetailDTO, requiredRoleMap);
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
            baseStockNoteOrderService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
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
            baseStockNoteOrderService.executeStockTrans(stockTransDTO, stockTransActionDTO, null, requiredRoleMap);
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
            baseStockNoteOrderService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, null);
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
            baseStockNoteOrderService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            TestCase.assertEquals(lex.getKeyMsg(), desc);
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void testValidate_Notecode() {
        try {
            initStockTransDTO();
            initStockTransAction();
            initListStockTransDetailDTO();
            stockTransActionDTO.setActionCode("PX_000001");
            baseStockNoteOrderService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            TestCase.assertEquals(lex.getKeyMsg(), "export.order.transCode.error.format.msg");
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
            baseStockNoteOrderService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
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
            baseStockNoteOrderService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
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
            baseStockNoteOrderService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
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
            baseStockNoteOrderService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            TestCase.assertEquals(lex.getKeyMsg(), "stockTrans.validate.stock.notConsistent");
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
            baseStockNoteOrderService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
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
            baseStockNoteOrderService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
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
            baseStockNoteOrderService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            TestCase.assertEquals(lex.getKeyMsg(), "export.order.airflow.require.msg");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    //Validate voffice not signed
    @Test
    public void testValidate_voffice_notSigned() {
        try {
            initStockTransDTO();
            initStockTransAction();
            initListStockTransDetailDTO();
            stockTransActionDTO.setSignCaStatus("2"); //Giao dich co ky voffice
            baseStockNoteOrderService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            TestCase.assertEquals(lex.getKeyMsg(), "mn.stock.voffice.empty");
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
            stockTransActionDTO.setSignCaStatus("2"); //Giao dich co ky voffice
            //Insert ban ghi vao bang StockTransVoffice
            baseStockNoteOrderService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            TestCase.assertEquals(lex.getKeyMsg(), "mn.stock.voffice.empty");
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
            baseStockNoteOrderService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            TestCase.assertEquals(lex.getKeyMsg(), "stockTrans.validate.note.status.invalid");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }


    public void initStockTransDTO() {
        stockTransDTO.setStockTransId(743L);
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
        stockTransActionDTO.setStockTransActionId(478399L);
        stockTransActionDTO.setActionCode("PX_000034");
        stockTransActionDTO.setActionStaffId(998877L);
    }

    public void initListStockTransDetailDTO() {
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId(5180L);
        stockTransDetailDTO.setStateId(1L);
        stockTransDetailDTO.setQuantity(1L);
        lstStockTransDetailDTO.add(stockTransDetailDTO);
    }

}
