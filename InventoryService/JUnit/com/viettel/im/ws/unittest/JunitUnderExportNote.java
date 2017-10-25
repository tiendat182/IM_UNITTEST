package com.viettel.im.ws.unittest;

import com.viettel.bccs.inventory.dto.StockTransActionDTO;
import com.viettel.bccs.inventory.dto.StockTransDTO;
import com.viettel.bccs.inventory.dto.StockTransDetailDTO;
import com.viettel.bccs.inventory.dto.StockTransSerialDTO;
import com.viettel.bccs.inventory.service.BaseStock.BaseStockExpNoteService;
import com.viettel.bccs.inventory.service.BaseStock.BaseStockNoteOrderService;
import com.viettel.bccs.inventory.service.ProductOfferingService;
import com.viettel.bccs.inventory.service.StockTransSerialService;
import com.viettel.bccs.inventory.service.StockTransService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.RequiredRoleMap;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thetm1 on 14/01/2016
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MainApplication.class)
public class JunitUnderExportNote{
    @Autowired
    BaseStockNoteOrderService baseStockNoteOrderService;

    private RequiredRoleMap requiredRoleMap;
    private StockTransDTO stockTransDTO = new StockTransDTO();
    private List<StockTransDetailDTO> lstStockTransDetailDTO = new ArrayList<>();
    private List<StockTransSerialDTO> lstStockTransSerialDTO = new ArrayList();
    private StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();

    @Autowired
    ProductOfferingService productOfferingService;
    @Autowired
    StockTransService stockTransService;
    @Autowired
    StockTransSerialService stockTransSerialService;

    @Before
    public void prepareData() {

    }

    @Test
    public void testValidate_stockFromOwnerNotNull() {
        try {
            initListStockTransDetailDTO();
            initStockTransAction();
            StockTransDTO stockTransDTO =  new StockTransDTO();

            baseStockNoteOrderService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            assert("Kho xuất hàng không được để trống".equals(desc));
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void testValidate_stockFromOwnerNotExist() {
        try {
            initListStockTransDetailDTO();
            initStockTransAction();
            StockTransDTO stockTransDTO =  new StockTransDTO();
            stockTransDTO.setFromOwnerType(1L);
            stockTransDTO.setFromOwnerId(-1L);
            baseStockNoteOrderService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            assert("Kho xuất hàng không tồn tại".equals(desc));
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void testValidate_stockToOwnerNotExist() {
        try {
            initListStockTransDetailDTO();
            initStockTransAction();
            StockTransDTO stockTransDTO =  new StockTransDTO();
            stockTransDTO.setFromOwnerType(1L);
            stockTransDTO.setToOwnerType(1L);
            stockTransDTO.setFromOwnerId(7282L);
            stockTransDTO.setToOwnerId(-1L);
            stockTransDTO.setStockTransId(-1L);
            baseStockNoteOrderService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            assert("Kho nhận không tồn tại".equals(desc));
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void testValidate_reasonNotExist() {
        try {
            initListStockTransDetailDTO();
            initStockTransAction();
            StockTransDTO stockTransDTO =  new StockTransDTO();
//            stockTransDTO.setFromStock("2");
            stockTransDTO.setFromOwnerType(1L);
            stockTransDTO.setToOwnerType(1L);
            stockTransDTO.setFromOwnerId(7282L);
            stockTransDTO.setToOwnerId(104959L);
            stockTransDTO.setReasonId(-1L);
            stockTransDTO.setStockTransId(-1L);
            baseStockNoteOrderService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            assert("Lý do xuất không được để trống".equals(desc));
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

//    @Test
//    public void testValidate_stockTransCodeInvalid() {
//        try {
//            initListStockTransDetailDTO();
//            initStockTransAction();
//            stockTransActionDTO.setActionCode("dghdjglksdjlsdjkgjlsdgjlsdjglsdfjgklsdfjglsdfjkldgjl");
//            stockTransDTO.setFromOwnerType(1L);
//            stockTransDTO.setToOwnerType(1L);
//            stockTransDTO.setToOwnerId(104959L);
//            stockTransDTO.setFromOwnerId(7282L);
//            stockTransDTO.setReasonId(4522L);
//            stockTransDTO.setStockTransId(-1L);
//            baseStockExpNoteService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
//        } catch (LogicException lex) {
//            String desc = lex.getDescription();
//            assert("Mã phiếu xuất không được vượt quá 50 ký tự(Bao gồm cả dấu)!".equals(desc));
//        } catch (Exception ex) {
//            TestCase.assertEquals("Loi he thong", ex.getMessage());
//        }
//    }

    @Test
    public void testValidate_stockTransNotExist() {
        try {
            initListStockTransDetailDTO();
            initStockTransAction();

            stockTransDTO.setFromOwnerType(1L);
            stockTransDTO.setToOwnerType(1L);
            stockTransDTO.setToOwnerId(104959L);
            stockTransDTO.setFromOwnerId(7282L);
            stockTransDTO.setReasonId(4522L);
            stockTransDTO.setStockTransId(-1L);
            baseStockNoteOrderService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            assert("Kho không tồn tại".equals(desc));
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void testValidate_stockTransStatus() {
        try {
            initStockTransAction();

            stockTransDTO.setFromOwnerType(1L);
            stockTransDTO.setToOwnerType(1L);
            stockTransDTO.setToOwnerId(104959L);
            stockTransDTO.setFromOwnerId(7282L);
            stockTransDTO.setReasonId(4522L);
            stockTransDTO.setStockTransId(1709L);
            stockTransDTO.setStatus("1");//sua trang thai trong DB

            baseStockNoteOrderService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            assert("Giao dịch phải ở trạng thái đã lập phiếu".equals(desc));
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void test_stockTransDetailNull() {
        try {
            initStockTransDTO();
            initStockTransAction();

            baseStockNoteOrderService.executeStockTrans(stockTransDTO, stockTransActionDTO, null, requiredRoleMap);
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            assert("Danh sách hàng hóa không được để trống!".equals(desc));
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void test_OK() {
        try {
            initStockTransDTO();
            initStockTransAction();
            initListStockTransDetailDTO();

            baseStockNoteOrderService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            assert("Kho xuất phải là cấp trên liền kề kho nhận".equals(desc));
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    public void initStockTransDTO() throws Exception {
//        stockTransDTO.setFromStock("2");
        stockTransDTO.setStockTransId(21635L);
        stockTransDTO.setToOwnerId(104959L);
        stockTransDTO.setFromOwnerType(1L);
        stockTransDTO.setFromOwnerId(7282L);
        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setStockTransType("1");
        stockTransDTO.setStockTransStatus("3");
        stockTransDTO.setCreateDatetime(DateUtil.string2DateTime("02/02/2016 10:18:50"));
        stockTransDTO.setTotalAmount(10000L);
        stockTransDTO.setTransport("2");
        stockTransDTO.setNote("unitTest");
        stockTransDTO.setStaffId(295160L);
        stockTransDTO.setStatus("3");
        stockTransDTO.setStockTransActionId(481395L);
        stockTransDTO.setReasonId(4522L);
    }

    public void initStockTransAction() {
        stockTransActionDTO.setStockTransActionId(481395L);
        stockTransActionDTO.setStockTransId(21635L);
        stockTransActionDTO.setActionCode("PN_CN_HNI2_THAONT19_000042");
        stockTransActionDTO.setActionStaffId(295160L);
    }

    public void initListStockTransDetailDTO() {
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setStockTransDetailId(2150L);
        stockTransDetailDTO.setStockTransId(1709L);
        stockTransDetailDTO.setProdOfferId(5180L);
        stockTransDetailDTO.setStateId(1L);
        stockTransDetailDTO.setQuantity(1L);
        stockTransDetailDTO.setPrice(10000L);
        stockTransDetailDTO.setAmount(10L);
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
