package com.viettel.im.ws.unittest;

import com.viettel.bccs.inventory.dto.StockTransActionDTO;
import com.viettel.bccs.inventory.dto.StockTransDTO;
import com.viettel.bccs.inventory.dto.StockTransDetailDTO;
import com.viettel.bccs.inventory.dto.StockTransSerialDTO;
import com.viettel.bccs.inventory.service.BaseStock.BaseStockImpNoteService;
import com.viettel.bccs.inventory.service.BaseStock.BaseStockInOrderService;
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
public class JunitUnderImportOrder{
    @Autowired
    BaseStockInOrderService baseStockInOrderService;

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

            baseStockInOrderService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
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
            baseStockInOrderService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
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
            baseStockInOrderService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
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
            baseStockInOrderService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            assert("Lý do xuất không được để trống".equals(desc));
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void testValidate_stockTransCodeInvalid() {
        try {
            initListStockTransDetailDTO();
            initStockTransAction();
            stockTransActionDTO.setActionCode("42334234fdsghdfgsdgsgsdfgdgsdgdgsdgdgsdgdgdgdfgdsgdgdfgdfgdgsđgdfgfgfdgfgd");
            StockTransDTO stockTransDTO =  new StockTransDTO();
            stockTransDTO.setFromOwnerType(1L);
            stockTransDTO.setToOwnerType(1L);
            stockTransDTO.setToOwnerId(104959L);
            stockTransDTO.setFromOwnerId(7282L);
            stockTransDTO.setReasonId(4522L);
//            stockTransDTO.setStockTransId(-1L);
            baseStockInOrderService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            assert("Mã lệnh nhập không quá 50 ký tự".equals(desc));
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

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
            baseStockInOrderService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            assert("Không tìm thấy dữ liệu thoả mãn điều kiện nhập vào".equals(desc));
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
            stockTransDTO.setStockTransId(1694L);
            stockTransDTO.setStatus("1");//sua trang thai trong DB

            baseStockInOrderService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            assert("Chỉ cho phép thực hiện ở trạng thái Đã xuất kho".equals(desc));
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void test_stockTransDetailNull() {
        try {
            initStockTransDTO();
            initStockTransAction();

            baseStockInOrderService.executeStockTrans(stockTransDTO, stockTransActionDTO, null, requiredRoleMap);
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

            baseStockInOrderService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO, requiredRoleMap);
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            assert("Kho xuất phải là cấp trên liền kề kho nhận".equals(desc));
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    public void initStockTransDTO() throws Exception {
        stockTransDTO.setFromStock("2");
        stockTransDTO.setStockTransId(1694L);
        stockTransDTO.setToOwnerId(7282L);
        stockTransDTO.setFromOwnerType(1L);
        stockTransDTO.setFromOwnerId(104959L);
        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setStockTransType("1");
        stockTransDTO.setStockTransStatus("3");
        stockTransDTO.setCreateDatetime(DateUtil.string2DateTime("02/02/2016 10:18:50"));
        stockTransDTO.setTotalAmount(10000L);
        stockTransDTO.setTransport("2");
        stockTransDTO.setNote("unitTest");
        stockTransDTO.setStaffId(295160L);
        stockTransDTO.setStatus("3");
        stockTransDTO.setStockTransActionId(484992L);
        stockTransDTO.setReasonId(4522L);
    }

    public void initStockTransAction() {
        stockTransActionDTO.setStockTransActionId(484992L);
        stockTransActionDTO.setStockTransId(1694L);
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
