package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.dto.BillStockDTO;
import com.viettel.bccs.inventory.dto.OrderObjectDTO;
import com.viettel.bccs.inventory.dto.ResultLogisticsDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.service.BaseServiceImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;

import static org.mockito.Mockito.mock;

/**
 * @author DatLT
 * @date 08/10/2017
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({DbUtil.class, LogisticServiceImpl.class, BaseServiceImpl.class, LogisticService.class, BillStockDTO.class})
public class LogisticServiceImplTest {
    @InjectMocks
    LogisticServiceImpl logisticService;
    @Mock
    private LogisticBaseService logisticBaseService;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method LogisticServiceImpl.createBill
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCreateBill_1() throws  Exception {
        List<BillStockDTO> lstBillStockDTO = Lists.newArrayList();
        logisticService.createBill(lstBillStockDTO);
    }

    @Test
    public void testCreateBill_2() throws  Exception {
        BillStockDTO billStockDTO = mock(BillStockDTO.class);
        Mockito.doNothing().when(billStockDTO).setOrderAction("OrderAction");
        ResultLogisticsDTO resultLogisticsDTO = mock(ResultLogisticsDTO.class);
        Mockito.when(logisticBaseService.createNote(billStockDTO.getOrderAction(), billStockDTO)).thenReturn(resultLogisticsDTO);
        Mockito.when(resultLogisticsDTO.getResponseCode()).thenReturn("resCd");
        List<BillStockDTO> lstBillStockDTO = Lists.newArrayList(billStockDTO);
        logisticService.createBill(lstBillStockDTO);
    }

    @Test
    public void testCreateBill_3() throws  Exception {
        BillStockDTO billStockDTO = new BillStockDTO();
        billStockDTO.setOrderAction("OrderAction");
        List<BillStockDTO> lstBillStockDTO = Lists.newArrayList(billStockDTO);
        Mockito.when(logisticBaseService.createNote(billStockDTO.getOrderAction(), billStockDTO)).thenThrow(new LogicException());
        logisticService.createBill(lstBillStockDTO);
    }

    @Test
    public void testCreateBill_4() throws  Exception {
        BillStockDTO billStockDTO = new BillStockDTO();
        billStockDTO.setOrderAction("OrderAction");
        List<BillStockDTO> lstBillStockDTO = Lists.newArrayList(billStockDTO);
        Mockito.when(logisticBaseService.createNote(billStockDTO.getOrderAction(), billStockDTO)).thenThrow(new Exception());
        logisticService.createBill(lstBillStockDTO);
    }
    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method LogisticServiceImpl.transStock
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test(expected = NullPointerException.class)
    public void testTransStock_1() throws  Exception {
        BillStockDTO billStockDTO = new BillStockDTO();
        logisticService.transStock(billStockDTO);
    }

    @Test
    public void testTransStock_2() throws  Exception {
        BillStockDTO billStockDTO = new BillStockDTO();
        Mockito.when( logisticBaseService.transStock(billStockDTO)).thenThrow(new LogicException());
        logisticService.transStock(billStockDTO);
    }

    @Test
    public void testTransStock_3() throws  Exception {
        BillStockDTO billStockDTO = new BillStockDTO();
        Mockito.when( logisticBaseService.transStock(billStockDTO)).thenThrow(new Exception());
        logisticService.transStock(billStockDTO);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method LogisticServiceImpl.cancelOrderOrBill
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test(expected = NullPointerException.class)
    public void testCancelOrderOrBill_() throws  Exception {
        OrderObjectDTO orderObjectDTO = new OrderObjectDTO();
        logisticService.cancelOrderOrBill(orderObjectDTO);
    }

    @Test
    public void testCancelOrderOrBill_2() throws  Exception {
        OrderObjectDTO orderObjectDTO = new OrderObjectDTO();
        Mockito.when( logisticBaseService.cancelOrderOrBill(orderObjectDTO)).thenThrow(new LogicException());
        logisticService.cancelOrderOrBill(orderObjectDTO);
    }

    @Test
    public void testCancelOrderOrBill_3() throws  Exception {
        OrderObjectDTO orderObjectDTO = new OrderObjectDTO();
        Mockito.when( logisticBaseService.cancelOrderOrBill(orderObjectDTO)).thenThrow(new Exception());
        logisticService.cancelOrderOrBill(orderObjectDTO);
    }
}