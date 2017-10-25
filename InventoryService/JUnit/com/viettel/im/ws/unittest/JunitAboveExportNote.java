package com.viettel.im.ws.unittest;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.inventory.service.BaseStock.BaseStockNoteOrderService;
import com.viettel.bccs.inventory.service.BaseStock.BaseStockOutNoteService;
import com.viettel.bccs.inventory.service.BaseStock.BaseStockStaffNoteService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.dto.BaseMessage;
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
public class JunitAboveExportNote{

    @Autowired
    BaseStockOutNoteService baseStockOutNoteService;

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
//        ownerType = "1";
//        ownerId = "7282";
//        stockCode = null;
//        productOfferType = "7";
//        prodOfferId = "5180";
//        stateId = "1";

    }





    @Test
    public void testValidate_stockFromOwnerNotNull() {
        try {
            initListStockTransDetailDTO();
            initStockTransAction();
            StockTransDTO stockTransDTO =  new StockTransDTO();
            stockTransDTO.setStaffId(295160L);

            baseStockOutNoteService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
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
            stockTransDTO.setStaffId(295160L);
            stockTransDTO.setFromOwnerType(1L);
            stockTransDTO.setFromOwnerId(-1L);
            baseStockOutNoteService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
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
            stockTransDTO.setStaffId(295160L);
            stockTransDTO.setFromOwnerType(1L);
            stockTransDTO.setToOwnerType(1L);
            stockTransDTO.setFromOwnerId(104959L);
            stockTransDTO.setToOwnerId(-1L);
            stockTransDTO.setStockTransId(-1L);
            baseStockOutNoteService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
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
            stockTransDTO.setStaffId(295160L);
            stockTransDTO.setFromStock("2");
            stockTransDTO.setFromOwnerType(1L);
            stockTransDTO.setToOwnerType(1L);
            stockTransDTO.setFromOwnerId(104959L);
            stockTransDTO.setToOwnerId(7282L);
            stockTransDTO.setReasonId(-1L);
            stockTransDTO.setStockTransId(-1L);
            baseStockOutNoteService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            assert("Lý do xuất không được để trống".equals(desc));
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }


    @Test
    public void test_stockTransDetailNull() {
        try {
            initStockTransDTO();
            initStockTransAction();
            StockTransDTO stockTransDTO =  new StockTransDTO();
            stockTransDTO.setStaffId(295160L);
            stockTransDTO.setFromStock("2");
            stockTransDTO.setFromOwnerType(1L);
            stockTransDTO.setToOwnerType(1L);
            stockTransDTO.setFromOwnerId(104959L);
            stockTransDTO.setToOwnerId(7282L);
            stockTransDTO.setReasonId(4568L);
            stockTransDTO.setStockTransId(21632L);
            stockTransDTO.setStatus("1");
            baseStockOutNoteService.executeStockTrans(stockTransDTO, stockTransActionDTO, null, requiredRoleMap);
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            assert("Danh sách mặt hàng trống".equals(desc));
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

            baseStockOutNoteService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            assert("Kho xuất phải là cấp trên liền kề kho nhận".equals(desc));
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    public void initStockTransDTO() throws Exception {
        stockTransDTO.setStaffId(295160L);
        stockTransDTO.setFromStock("2");
        stockTransDTO.setStockTransId(21632L);
        stockTransDTO.setFromOwnerId(104959L);
        stockTransDTO.setFromOwnerType(1L);
        stockTransDTO.setToOwnerId(7282L);
        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setStockTransType("1");
        stockTransDTO.setStockTransStatus("2");
        stockTransDTO.setCreateDatetime(DateUtil.string2DateTime("02/02/2016 10:18:50"));
        stockTransDTO.setTotalAmount(10000L);
        stockTransDTO.setReasonId(4568L);
        stockTransDTO.setTransport("2");
        stockTransDTO.setNote("unitTest");
//        stockTransDTO.setStaffId(9999999993L);
        stockTransDTO.setStatus("1");
        stockTransDTO.setStockTransActionId(484728L);

    }

    public void initStockTransAction() {
        stockTransActionDTO.setStockTransActionId(484728L);
        stockTransActionDTO.setStockTransId(21632L);
        stockTransActionDTO.setActionCode("PN_VT_PRODUCT_000192");
        stockTransActionDTO.setActionStaffId(9999999993L);
    }

    public void initListStockTransDetailDTO() {
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setStockTransDetailId(2150L);
        stockTransDetailDTO.setStockTransId(21632L);
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
