package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockTrans;
import com.viettel.bccs.inventory.repo.StockTransRepo;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.mapper.BaseMapper;
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
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * @author DatLT
 * @date 08/10/2017
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({DataUtil.class, LogisticBaseServiceImpl.class, BaseServiceImpl.class, LogisticBaseService.class, DbUtil.class})
public class LogisticBaseServiceImplTest {

    @InjectMocks
    LogisticBaseServiceImpl logisticBaseService;
    @Mock
    private final BaseMapper<StockTrans, StockTransDTO> mapperStockTrans = new BaseMapper<>(StockTrans.class, StockTransDTO.class);
    @Mock
    private EntityManager em;
    @Mock
    private StockTransService stockTransService;
    @Mock
    private StockTransRepo stockTransRepo;
    @Mock
    private StockTransActionService stockTransActionService;
    @Mock
    private StockTransDetailService stockTransDetailService;
    @Mock
    private StockTransVofficeService stockTransVofficeService;
    @Mock
    private ProductOfferingService productOfferingService;
    @Mock
    private StockTotalService stockTotalService;
    @Mock
    private StockHandsetService stockHandsetService;
    @Mock
    private ShopService shopService;
    @Mock
    private StockTransOfflineService stockTransOfflineService;
    @Mock
    private StockTransDetailOfflineService stockTransDetailOfflineService;
    @Mock
    private StockTransSerialOfflineService stockTransSerialOfflineService;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method LogisticBaseServiceImpl.createNote
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCreateNote_1() throws  Exception {
        //line 68
        String orderAction = "orderAction";
        BillStockDTO billStockDTO = new BillStockDTO();
        logisticBaseService.createNote(orderAction, billStockDTO);
    }

    @Test
    public void testCreateNote_2() throws  Exception {
        //71
        String orderAction = "orderAction-logistics.create.bill.order.action.null";
        BillStockDTO billStockDTO = new BillStockDTO();
        billStockDTO.setOrderCode("orderCd");
        logisticBaseService.createNote(orderAction, billStockDTO);
    }

    @Test
    public void testCreateNote_3() throws  Exception {
        //74
        String orderAction = "orderAction";
        BillStockDTO billStockDTO = new BillStockDTO();
        billStockDTO.setOrderCode("orderCd");
        logisticBaseService.createNote(orderAction, billStockDTO);
    }

    @Test
    public void testCreateNote_4() throws  Exception {
        //80
        String orderAction = "orderAction";
        BillStockDTO billStockDTO = new BillStockDTO();
        billStockDTO.setOrderCode("1");
        StockTrans stockTrans = null;
        when( stockTransRepo.findOne(1L)).thenReturn(stockTrans);
        logisticBaseService.createNote(orderAction, billStockDTO);
    }

    @Test
    public void testCreateNote_5() throws  Exception {
        //85
        String orderAction = "orderAction";
        BillStockDTO billStockDTO = new BillStockDTO();
        billStockDTO.setOrderCode("1");
        StockTrans stockTrans = new StockTrans();
        when( stockTransRepo.findOne(1L)).thenReturn(stockTrans);
        Mockito.doThrow(Exception.class).when(em).lock(stockTrans, LockModeType.PESSIMISTIC_READ);
        logisticBaseService.createNote(orderAction, billStockDTO);
    }

    @Test
    public void testCreateNote_6() throws  Exception {
        //96
        String orderAction = "orderAction";
        BillStockDTO billStockDTO = new BillStockDTO();
        StockTrans stockTrans = new StockTrans();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        billStockDTO.setOrderCode("1");
        List<String> lstStatus = Lists.newArrayList();
        lstStatus.add(Const.STOCK_TRANS_STATUS.EXPORT_ORDER);
        lstStatus.add(Const.STOCK_TRANS_STATUS.IMPORT_ORDER);
        when( stockTransRepo.findOne(1L)).thenReturn(stockTrans);
        when(stockTransActionService.getStockTransActionByIdAndStatus(1L, lstStatus)).thenReturn(stockTransActionDTO);
        logisticBaseService.createNote(orderAction, billStockDTO);
    }

    @Test
    public void testCreateNote_7() throws  Exception {
        //100
        String orderAction = "orderAction";
        BillStockDTO billStockDTO = new BillStockDTO();
        StockTrans stockTrans = new StockTrans();
        StockTransVofficeDTO stockTransVofficeDTO = mock(StockTransVofficeDTO.class);
        StockTransActionDTO stockTransActionDTO = mock(StockTransActionDTO.class);
        stockTransActionDTO.setStockTransId(1L);
        stockTransActionDTO.setStockTransActionId(1L);
        billStockDTO.setOrderCode("1");
        when( stockTransRepo.findOne(1L)).thenReturn(stockTrans);
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(),any())).thenReturn(stockTransActionDTO);
        when(stockTransActionDTO.getStockTransActionId()).thenReturn(1L);
        when(stockTransActionDTO.getSignCaStatus()).thenReturn("2");
        when(stockTransVofficeService.findStockTransVofficeByActionId(any())).thenReturn(stockTransVofficeDTO);
        logisticBaseService.createNote(orderAction, billStockDTO);
    }

    @Test
    public void testCreateNote_8() throws  Exception {
        //110
        String orderAction = "orderAction";
        BillStockDTO billStockDTO = new BillStockDTO();
        StockTrans stockTrans = new StockTrans();
        StockTransVofficeDTO stockTransVofficeDTO = mock(StockTransVofficeDTO.class);
        StockTransActionDTO stockTransActionDTO = mock(StockTransActionDTO.class);
        stockTransActionDTO.setStockTransId(1L);
        stockTransActionDTO.setStockTransActionId(1L);
        billStockDTO.setOrderCode("1");
        when( stockTransRepo.findOne(1L)).thenReturn(stockTrans);
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(),any())).thenReturn(stockTransActionDTO);
        when(stockTransVofficeService.findStockTransVofficeByActionId(any())).thenReturn(stockTransVofficeDTO);
        logisticBaseService.createNote(orderAction, billStockDTO);
    }

    @Test
    public void testCreateNote_9() throws  Exception {
        //114
        String orderAction = "orderAction";
        BillStockDTO billStockDTO = new BillStockDTO();
        StockTrans stockTrans = new StockTrans();
        StockTransVofficeDTO stockTransVofficeDTO = mock(StockTransVofficeDTO.class);
        StockTransActionDTO stockTransActionDTO = mock(StockTransActionDTO.class);
        stockTransActionDTO.setStockTransId(1L);
        stockTransActionDTO.setStockTransActionId(1L);
        billStockDTO.setOrderCode("1");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        when( stockTransRepo.findOne(1L)).thenReturn(stockTrans);
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(),any())).thenReturn(stockTransActionDTO);
        when(stockTransVofficeService.findStockTransVofficeByActionId(any())).thenReturn(stockTransVofficeDTO);
        PowerMockito.doReturn(null).when(spyService,"validateBillStockGeneral", billStockDTO);
        spyService.createNote(orderAction, billStockDTO);
    }

    @Test
    public void testCreateNote_10() throws  Exception {
        //117
        String orderAction = "orderAction";
        BillStockDTO billStockDTO = new BillStockDTO();
        StockTrans stockTrans = mock(StockTrans.class);
        StockTransVofficeDTO stockTransVofficeDTO = mock(StockTransVofficeDTO.class);
        StockTransActionDTO stockTransActionDTO = mock(StockTransActionDTO.class);
        stockTransActionDTO.setStockTransId(1L);
        stockTransActionDTO.setStockTransActionId(1L);
        billStockDTO.setOrderCode("1");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        when( stockTransRepo.findOne(1L)).thenReturn(stockTrans);
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(),any())).thenReturn(stockTransActionDTO);
        when(stockTransVofficeService.findStockTransVofficeByActionId(any())).thenReturn(stockTransVofficeDTO);
        PowerMockito.doReturn(null).when(spyService,"validateBillStockGeneral", billStockDTO);
        PowerMockito.doReturn(null).when(spyService,"validateLstGoods", any(), any());
        when(stockTrans.getStatus()).thenReturn("1");
        when(stockTrans.getRegionStockTransId()).thenReturn(1L);
        spyService.createNote(orderAction, billStockDTO);
    }

    @Test
    public void testCreateNote_11() throws  Exception {
        //117
        String orderAction = "orderAction";
        BillStockDTO billStockDTO = new BillStockDTO();
        StockTrans stockTrans = mock(StockTrans.class);
        StockTransVofficeDTO stockTransVofficeDTO = mock(StockTransVofficeDTO.class);
        StockTransActionDTO stockTransActionDTO = mock(StockTransActionDTO.class);
        stockTransActionDTO.setStockTransId(1L);
        stockTransActionDTO.setStockTransActionId(1L);
        billStockDTO.setOrderCode("1");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        when( stockTransRepo.findOne(1L)).thenReturn(stockTrans);
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(),any())).thenReturn(stockTransActionDTO);
        when(stockTransVofficeService.findStockTransVofficeByActionId(any())).thenReturn(stockTransVofficeDTO);
        PowerMockito.doReturn(null).when(spyService,"validateBillStockGeneral", billStockDTO);
        PowerMockito.doReturn(null).when(spyService,"validateLstGoods", any(), any());
        PowerMockito.doReturn(null).when(spyService,"validateStockTotalGeneral", any(), any(), any());
        when(stockTrans.getStatus()).thenReturn("2");
        when(stockTrans.getRegionStockTransId()).thenReturn(1L);
        spyService.createNote(orderAction, billStockDTO);
    }

    @Test(expected = Exception.class)
    public void testCreateNote_12() throws  Exception {
        //148
        String orderAction = "orderAction";
        BillStockDTO billStockDTO = new BillStockDTO();
        StockTrans stockTrans = mock(StockTrans.class);
        StockTransVofficeDTO stockTransVofficeDTO = mock(StockTransVofficeDTO.class);
        StockTransActionDTO stockTransActionDTO = mock(StockTransActionDTO.class);
        stockTransActionDTO.setStockTransId(1L);
        stockTransActionDTO.setStockTransActionId(1L);
        billStockDTO.setOrderCode("1");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        when( stockTransRepo.findOne(1L)).thenReturn(stockTrans);
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(),any())).thenReturn(stockTransActionDTO);
        when(stockTransVofficeService.findStockTransVofficeByActionId(any())).thenReturn(stockTransVofficeDTO);
        PowerMockito.doReturn(null).when(spyService,"validateBillStockGeneral", billStockDTO);
        PowerMockito.doReturn(null).when(spyService,"validateLstGoods", any(), any());
        PowerMockito.doReturn(null).when(spyService,"validateStockTotalGeneral", any(), any(), any());
        when(stockTrans.getStatus()).thenReturn("4");
        when(stockTrans.getRegionStockTransId()).thenReturn(1L);
        spyService.createNote(orderAction, billStockDTO);
    }

    @Test(expected = Exception.class)
    public void testCreateNote_13() throws  Exception {
        //150
        String orderAction = "orderAction";
        BillStockDTO billStockDTO = new BillStockDTO();
        StockTrans stockTrans = mock(StockTrans.class);
        StockTransVofficeDTO stockTransVofficeDTO = mock(StockTransVofficeDTO.class);
        StockTransActionDTO stockTransActionDTO = mock(StockTransActionDTO.class);
        stockTransActionDTO.setStockTransId(1L);
        stockTransActionDTO.setStockTransActionId(1L);
        billStockDTO.setOrderCode("1");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        when( stockTransRepo.findOne(1L)).thenReturn(stockTrans);
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(),any())).thenReturn(stockTransActionDTO);
        when(stockTransVofficeService.findStockTransVofficeByActionId(any())).thenReturn(stockTransVofficeDTO);
        PowerMockito.doReturn(null).when(spyService,"validateBillStockGeneral", billStockDTO);
        PowerMockito.doReturn(null).when(spyService,"validateLstGoods", any(), any());
        PowerMockito.doReturn(null).when(spyService,"validateStockTotalGeneral", any(), any(), any());
        when(stockTrans.getStatus()).thenReturn("1");
        when(stockTrans.getRegionStockTransId()).thenReturn(1L);
        spyService.createNote(orderAction, billStockDTO);
    }

    @Test(expected = Exception.class)
    public void testCreateNote_14() throws  Exception {
        //148:182
        // Khong the nhay vao line 165
        String orderAction = "orderAction";
        BillStockDTO billStockDTO = new BillStockDTO();
        StockTrans stockTrans = mock(StockTrans.class);
        StockTransVofficeDTO stockTransVofficeDTO = mock(StockTransVofficeDTO.class);
        StockTransActionDTO stockTransActionDTO = mock(StockTransActionDTO.class);
        stockTransActionDTO.setStockTransId(1L);
        stockTransActionDTO.setStockTransActionId(1L);
        billStockDTO.setOrderCode("1");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        when( stockTransRepo.findOne(1L)).thenReturn(stockTrans);
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(),any())).thenReturn(stockTransActionDTO);
        when(stockTransVofficeService.findStockTransVofficeByActionId(any())).thenReturn(stockTransVofficeDTO);
        PowerMockito.doReturn(null).when(spyService,"validateBillStockGeneral", billStockDTO);
        PowerMockito.doReturn(null).when(spyService,"validateLstGoods", any(), any());
        PowerMockito.doReturn(null).when(spyService,"validateStockTotalGeneral", any(), any(), any());
        when(stockTrans.getStatus()).thenReturn("1");
        when(stockTrans.getFromOwnerType()).thenReturn(1L);
        when(stockTrans.getRegionStockTransId()).thenReturn(1L);
        when(stockTrans.getToOwnerType()).thenReturn(1L);
        spyService.createNote(orderAction, billStockDTO);
    }

    @Test(expected = Exception.class)
    public void testCreateNote_15() throws  Exception {
        // Do khong the nhay vao line 165 nên luon nhay val line 167 -> không thể nhảy vào line 186 -> không thể nhảy vào line 204
        // 189
        String orderAction = "orderAction";
        BillStockDTO billStockDTO = new BillStockDTO();
        StockTrans stockTrans = mock(StockTrans.class);
        StockTransVofficeDTO stockTransVofficeDTO = mock(StockTransVofficeDTO.class);
        StockTransActionDTO stockTransActionDTO = mock(StockTransActionDTO.class);
        ShopDTO shopDTO = new   ShopDTO();
        stockTransActionDTO.setStockTransId(1L);
        stockTransActionDTO.setStockTransActionId(1L);
        billStockDTO.setOrderCode("1");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        when( stockTransRepo.findOne(1L)).thenReturn(stockTrans);
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(),any())).thenReturn(stockTransActionDTO);
        when(stockTransVofficeService.findStockTransVofficeByActionId(any())).thenReturn(stockTransVofficeDTO);
        when(stockTransActionDTO.getStockTransActionId()).thenReturn(0L);
        PowerMockito.doReturn(null).when(spyService,"validateBillStockGeneral", billStockDTO);
        PowerMockito.doReturn(null).when(spyService,"validateLstGoods", any(), any());
        PowerMockito.doReturn(null).when(spyService,"validateStockTotalGeneral", any(), any(), any());
        when(stockTrans.getStatus()).thenReturn("4");
        when(stockTrans.getFromOwnerType()).thenReturn(1L);
        when(stockTrans.getRegionStockTransId()).thenReturn(1L);
        when(stockTrans.getToOwnerType()).thenReturn(1L);
        when(shopService.findOne(anyLong())).thenReturn(shopDTO);
        spyService.createNote(orderAction, billStockDTO);
    }

    @Test
    public void testCreateNote_16() throws  Exception {
        //189
        String orderAction = "orderAction";
        BillStockDTO billStockDTO = new BillStockDTO();
        StockTrans stockTrans = mock(StockTrans.class);
        StockTransVofficeDTO stockTransVofficeDTO = mock(StockTransVofficeDTO.class);
        StockTransActionDTO stockTransActionDTO = mock(StockTransActionDTO.class);
        ShopDTO shopDTO = new   ShopDTO();
        stockTransActionDTO.setStockTransId(1L);
        stockTransActionDTO.setStockTransActionId(1L);
        billStockDTO.setOrderCode("1");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        when( stockTransRepo.findOne(1L)).thenReturn(stockTrans);
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(),any())).thenReturn(stockTransActionDTO);
        when(stockTransVofficeService.findStockTransVofficeByActionId(any())).thenReturn(stockTransVofficeDTO);
        when(stockTransActionDTO.getStockTransActionId()).thenReturn(0L);
        PowerMockito.doReturn(null).when(spyService,"validateBillStockGeneral", billStockDTO);
        PowerMockito.doReturn(null).when(spyService,"validateLstGoods", any(), any());
        PowerMockito.doReturn(null).when(spyService,"validateStockTotalGeneral", any(), any(), any());
        when(stockTrans.getStatus()).thenReturn("3");
        when(stockTrans.getFromOwnerType()).thenReturn(1L);
        when(stockTrans.getRegionStockTransId()).thenReturn(1L);
        when(stockTrans.getToOwnerType()).thenReturn(1L);
        when(shopService.findOne(anyLong())).thenReturn(shopDTO);
        spyService.createNote(orderAction, billStockDTO);
    }

    @Test
    public void testCreateNote_17() throws  Exception {
        String orderAction = "orderAction";
        BillStockDTO billStockDTO = new BillStockDTO();
        StockTrans stockTrans = mock(StockTrans.class);
        StockTransVofficeDTO stockTransVofficeDTO = mock(StockTransVofficeDTO.class);
        StockTransActionDTO stockTransActionDTO = mock(StockTransActionDTO.class);
        ShopDTO shopDTO = new   ShopDTO();
        stockTransActionDTO.setStockTransId(1L);
        stockTransActionDTO.setStockTransActionId(1L);
        billStockDTO.setOrderCode("1");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        when( stockTransRepo.findOne(1L)).thenReturn(stockTrans);
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(),any())).thenReturn(stockTransActionDTO);
        when(stockTransVofficeService.findStockTransVofficeByActionId(any())).thenReturn(stockTransVofficeDTO);
        when(stockTransActionDTO.getStockTransActionId()).thenReturn(0L);
        PowerMockito.doReturn(null).when(spyService,"validateBillStockGeneral", billStockDTO);
        PowerMockito.doReturn(null).when(spyService,"validateLstGoods", any(), any());
        PowerMockito.doReturn(null).when(spyService,"validateStockTotalGeneral", any(), any(), any());
        when(stockTrans.getStatus()).thenReturn("4");
        when(stockTrans.getFromOwnerType()).thenReturn(1L);
        when(stockTrans.getRegionStockTransId()).thenReturn(0L);
        when(stockTrans.getToOwnerType()).thenReturn(1L);
        when(shopService.findOne(anyLong())).thenReturn(shopDTO);
        spyService.createNote(orderAction, billStockDTO);
    }


    @Test
    public void testCreateNote_18() throws  Exception {
        //71
        String orderAction = "";
        BillStockDTO billStockDTO = new BillStockDTO();
        billStockDTO.setOrderCode("orderCd");
        logisticBaseService.createNote(orderAction, billStockDTO);
    }

    @Test
    public void testCreateNote_19() throws  Exception {
        //110
        String orderAction = "orderAction";
        BillStockDTO billStockDTO = new BillStockDTO();
        StockTrans stockTrans = new StockTrans();
        StockTransVofficeDTO stockTransVofficeDTO = mock(StockTransVofficeDTO.class);
        StockTransActionDTO stockTransActionDTO = mock(StockTransActionDTO.class);
        stockTransActionDTO.setStockTransId(1L);
        stockTransActionDTO.setStockTransActionId(1L);
        billStockDTO.setOrderCode("1");
        when( stockTransRepo.findOne(1L)).thenReturn(stockTrans);
        when(stockTransActionDTO.getSignCaStatus()).thenReturn("1");
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(),any())).thenReturn(stockTransActionDTO);
        when(stockTransVofficeService.findStockTransVofficeByActionId(any())).thenReturn(stockTransVofficeDTO);
        logisticBaseService.createNote(orderAction, billStockDTO);
    }

    @Test
    public void testCreateNote_20() throws  Exception {
        //100
        String orderAction = "orderAction";
        BillStockDTO billStockDTO = new BillStockDTO();
        StockTrans stockTrans = new StockTrans();
        StockTransActionDTO stockTransActionDTO = mock(StockTransActionDTO.class);
        stockTransActionDTO.setStockTransId(1L);
        stockTransActionDTO.setStockTransActionId(1L);
        billStockDTO.setOrderCode("1");
        when( stockTransRepo.findOne(1L)).thenReturn(stockTrans);
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(),any())).thenReturn(stockTransActionDTO);
        when(stockTransActionDTO.getStockTransActionId()).thenReturn(1L);
        when(stockTransActionDTO.getSignCaStatus()).thenReturn("2");
        when(stockTransVofficeService.findStockTransVofficeByActionId(any())).thenReturn(null);
        logisticBaseService.createNote(orderAction, billStockDTO);
    }

    @Test
    public void testCreateNote_21() throws  Exception {
        //100
        String orderAction = "orderAction";
        BillStockDTO billStockDTO = new BillStockDTO();
        StockTrans stockTrans = new StockTrans();
        StockTransVofficeDTO stockTransVofficeDTO = mock(StockTransVofficeDTO.class);
//        stockTransVofficeDTO.setStatus("1");
        StockTransActionDTO stockTransActionDTO = mock(StockTransActionDTO.class);
        stockTransActionDTO.setStockTransId(1L);
        stockTransActionDTO.setStockTransActionId(1L);
        billStockDTO.setOrderCode("1");
        when( stockTransRepo.findOne(1L)).thenReturn(stockTrans);
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(),any())).thenReturn(stockTransActionDTO);
        when(stockTransActionDTO.getStockTransActionId()).thenReturn(1L);
        when(stockTransActionDTO.getSignCaStatus()).thenReturn("2");
        when(stockTransVofficeDTO.getStatus()).thenReturn("1");
        when(stockTransVofficeService.findStockTransVofficeByActionId(any())).thenReturn(stockTransVofficeDTO);
        logisticBaseService.createNote(orderAction, billStockDTO);
    }

    @Test
    public void testCreateNote_22() throws  Exception {
        String orderAction = null;
        BillStockDTO billStockDTO = new BillStockDTO();
        billStockDTO.setOrderCode("orderCd");
        logisticBaseService.createNote(orderAction, billStockDTO);
    }

    @Test
    public void testCreateNote_23() throws  Exception {
        //117
        String orderAction = "orderAction";
        BillStockDTO billStockDTO = new BillStockDTO();
        StockTrans stockTrans = mock(StockTrans.class);
        StockTransVofficeDTO stockTransVofficeDTO = mock(StockTransVofficeDTO.class);
        StockTransActionDTO stockTransActionDTO = mock(StockTransActionDTO.class);
        stockTransActionDTO.setStockTransId(1L);
        stockTransActionDTO.setStockTransActionId(1L);
        billStockDTO.setOrderCode("1");
        ResultLogisticsDTO resultLstGood = new ResultLogisticsDTO();
        resultLstGood.setResponseCode("0");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        when( stockTransRepo.findOne(1L)).thenReturn(stockTrans);
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(),any())).thenReturn(stockTransActionDTO);
        when(stockTransVofficeService.findStockTransVofficeByActionId(any())).thenReturn(stockTransVofficeDTO);
        PowerMockito.doReturn(null).when(spyService,"validateBillStockGeneral", billStockDTO);
        PowerMockito.doReturn(resultLstGood).when(spyService,"validateLstGoods", any(), any());
        when(stockTrans.getStatus()).thenReturn("1");
        when(stockTrans.getRegionStockTransId()).thenReturn(1L);
        spyService.createNote(orderAction, billStockDTO);
    }

    @Test
    public void testCreateNote_24() throws  Exception {
        //117
        String orderAction = "orderAction";
        BillStockDTO billStockDTO = new BillStockDTO();
        StockTrans stockTrans = mock(StockTrans.class);
        StockTransVofficeDTO stockTransVofficeDTO = mock(StockTransVofficeDTO.class);
        StockTransActionDTO stockTransActionDTO = mock(StockTransActionDTO.class);
        stockTransActionDTO.setStockTransId(1L);
        stockTransActionDTO.setStockTransActionId(1L);
        billStockDTO.setOrderCode("1");
        ResultLogisticsDTO resultLstGood = new ResultLogisticsDTO();
        resultLstGood.setResponseCode("");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        when( stockTransRepo.findOne(1L)).thenReturn(stockTrans);
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(),any())).thenReturn(stockTransActionDTO);
        when(stockTransVofficeService.findStockTransVofficeByActionId(any())).thenReturn(stockTransVofficeDTO);
        PowerMockito.doReturn(null).when(spyService,"validateBillStockGeneral", billStockDTO);
        PowerMockito.doReturn(resultLstGood).when(spyService,"validateLstGoods", any(), any());
        when(stockTrans.getStatus()).thenReturn("1");
        when(stockTrans.getRegionStockTransId()).thenReturn(1L);
        spyService.createNote(orderAction, billStockDTO);
    }

    @Test
    public void testCreateNote_25() throws  Exception {
        //117
        String orderAction = "orderAction";
        BillStockDTO billStockDTO = new BillStockDTO();
        StockTrans stockTrans = mock(StockTrans.class);
        StockTransVofficeDTO stockTransVofficeDTO = mock(StockTransVofficeDTO.class);
        StockTransActionDTO stockTransActionDTO = mock(StockTransActionDTO.class);
        stockTransActionDTO.setStockTransId(1L);
        stockTransActionDTO.setStockTransActionId(1L);
        billStockDTO.setOrderCode("1");
        ResultLogisticsDTO resultLstGood = new ResultLogisticsDTO();
        resultLstGood.setResponseCode("1");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        when( stockTransRepo.findOne(1L)).thenReturn(stockTrans);
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(),any())).thenReturn(stockTransActionDTO);
        when(stockTransVofficeService.findStockTransVofficeByActionId(any())).thenReturn(stockTransVofficeDTO);
        PowerMockito.doReturn(null).when(spyService,"validateBillStockGeneral", billStockDTO);
        PowerMockito.doReturn(resultLstGood).when(spyService,"validateLstGoods", any(), any());
        when(stockTrans.getStatus()).thenReturn("1");
        when(stockTrans.getRegionStockTransId()).thenReturn(1L);
        spyService.createNote(orderAction, billStockDTO);
    }

    @Test
    public void testCreateNote_26() throws  Exception {
        //114
        String orderAction = "orderAction";
        BillStockDTO billStockDTO = new BillStockDTO();
        StockTrans stockTrans = new StockTrans();
        StockTransVofficeDTO stockTransVofficeDTO = mock(StockTransVofficeDTO.class);
        StockTransActionDTO stockTransActionDTO = mock(StockTransActionDTO.class);
        stockTransActionDTO.setStockTransId(1L);
        stockTransActionDTO.setStockTransActionId(1L);
        billStockDTO.setOrderCode("1");
        ResultLogisticsDTO stockGeneralDTO = new ResultLogisticsDTO();
        stockGeneralDTO.setResponseCode("");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        when( stockTransRepo.findOne(1L)).thenReturn(stockTrans);
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(),any())).thenReturn(stockTransActionDTO);
        when(stockTransVofficeService.findStockTransVofficeByActionId(any())).thenReturn(stockTransVofficeDTO);
        PowerMockito.doReturn(stockGeneralDTO).when(spyService,"validateBillStockGeneral", billStockDTO);
        spyService.createNote(orderAction, billStockDTO);
    }

    @Test
    public void testCreateNote_27() throws  Exception {
        //114
        String orderAction = "orderAction";
        BillStockDTO billStockDTO = new BillStockDTO();
        StockTrans stockTrans = new StockTrans();
        StockTransVofficeDTO stockTransVofficeDTO = mock(StockTransVofficeDTO.class);
        StockTransActionDTO stockTransActionDTO = mock(StockTransActionDTO.class);
        stockTransActionDTO.setStockTransId(1L);
        stockTransActionDTO.setStockTransActionId(1L);
        billStockDTO.setOrderCode("1");
        ResultLogisticsDTO stockGeneralDTO = new ResultLogisticsDTO();
        stockGeneralDTO.setResponseCode("1");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        when( stockTransRepo.findOne(1L)).thenReturn(stockTrans);
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(),any())).thenReturn(stockTransActionDTO);
        when(stockTransVofficeService.findStockTransVofficeByActionId(any())).thenReturn(stockTransVofficeDTO);
        PowerMockito.doReturn(stockGeneralDTO).when(spyService,"validateBillStockGeneral", billStockDTO);
        spyService.createNote(orderAction, billStockDTO);
    }

    @Test
    public void testCreateNote_28() throws  Exception {
        //117
        String orderAction = "orderAction";
        BillStockDTO billStockDTO = new BillStockDTO();
        StockTrans stockTrans = mock(StockTrans.class);
        StockTransVofficeDTO stockTransVofficeDTO = mock(StockTransVofficeDTO.class);
        StockTransActionDTO stockTransActionDTO = mock(StockTransActionDTO.class);
        stockTransActionDTO.setStockTransId(1L);
        stockTransActionDTO.setStockTransActionId(1L);
        billStockDTO.setOrderCode("1");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        when( stockTransRepo.findOne(1L)).thenReturn(stockTrans);
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(),any())).thenReturn(stockTransActionDTO);
        when(stockTransVofficeService.findStockTransVofficeByActionId(any())).thenReturn(stockTransVofficeDTO);
        PowerMockito.doReturn(null).when(spyService,"validateBillStockGeneral", billStockDTO);
        PowerMockito.doReturn(null).when(spyService,"validateLstGoods", any(), any());
        when(stockTrans.getStatus()).thenReturn("1");
        when(stockTrans.getRegionStockTransId()).thenReturn(null);
        spyService.createNote(orderAction, billStockDTO);
    }

    @Test
    public void testCreateNote_29() throws  Exception {
        //117
        String orderAction = "orderAction";
        BillStockDTO billStockDTO = new BillStockDTO();
        StockTrans stockTrans = mock(StockTrans.class);
        StockTransVofficeDTO stockTransVofficeDTO = mock(StockTransVofficeDTO.class);
        StockTransActionDTO stockTransActionDTO = mock(StockTransActionDTO.class);
        stockTransActionDTO.setStockTransId(1L);
        stockTransActionDTO.setStockTransActionId(1L);
        billStockDTO.setOrderCode("1");
        ResultLogisticsDTO resultStockTotal = new ResultLogisticsDTO();
        resultStockTotal.setResponseCode("");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        when( stockTransRepo.findOne(1L)).thenReturn(stockTrans);
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(),any())).thenReturn(stockTransActionDTO);
        when(stockTransVofficeService.findStockTransVofficeByActionId(any())).thenReturn(stockTransVofficeDTO);
        PowerMockito.doReturn(null).when(spyService,"validateBillStockGeneral", billStockDTO);
        PowerMockito.doReturn(null).when(spyService,"validateLstGoods", any(), any());
        PowerMockito.doReturn(resultStockTotal).when(spyService,"validateStockTotalGeneral", any(), any(), any());
        when(stockTrans.getStatus()).thenReturn("2");
        when(stockTrans.getRegionStockTransId()).thenReturn(1L);
        spyService.createNote(orderAction, billStockDTO);
    }

    @Test
    public void testCreateNote_30() throws  Exception {
        //117
        String orderAction = "orderAction";
        BillStockDTO billStockDTO = new BillStockDTO();
        StockTrans stockTrans = mock(StockTrans.class);
        StockTransVofficeDTO stockTransVofficeDTO = mock(StockTransVofficeDTO.class);
        StockTransActionDTO stockTransActionDTO = mock(StockTransActionDTO.class);
        stockTransActionDTO.setStockTransId(1L);
        stockTransActionDTO.setStockTransActionId(1L);
        billStockDTO.setOrderCode("1");
        ResultLogisticsDTO resultStockTotal = new ResultLogisticsDTO();
        resultStockTotal.setResponseCode("1");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        when( stockTransRepo.findOne(1L)).thenReturn(stockTrans);
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(),any())).thenReturn(stockTransActionDTO);
        when(stockTransVofficeService.findStockTransVofficeByActionId(any())).thenReturn(stockTransVofficeDTO);
        PowerMockito.doReturn(null).when(spyService,"validateBillStockGeneral", billStockDTO);
        PowerMockito.doReturn(null).when(spyService,"validateLstGoods", any(), any());
        PowerMockito.doReturn(resultStockTotal).when(spyService,"validateStockTotalGeneral", any(), any(), any());
        when(stockTrans.getStatus()).thenReturn("2");
        when(stockTrans.getRegionStockTransId()).thenReturn(1L);
        spyService.createNote(orderAction, billStockDTO);
    }

    @Test
    public void testCreateNote_31() throws  Exception {
        //117
        String orderAction = "orderAction";
        BillStockDTO billStockDTO = new BillStockDTO();
        StockTrans stockTrans = mock(StockTrans.class);
        StockTransVofficeDTO stockTransVofficeDTO = mock(StockTransVofficeDTO.class);
        StockTransActionDTO stockTransActionDTO = mock(StockTransActionDTO.class);
        stockTransActionDTO.setStockTransId(1L);
        stockTransActionDTO.setStockTransActionId(1L);
        billStockDTO.setOrderCode("1");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        when( stockTransRepo.findOne(1L)).thenReturn(stockTrans);
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(),any())).thenReturn(stockTransActionDTO);
        when(stockTransVofficeService.findStockTransVofficeByActionId(any())).thenReturn(stockTransVofficeDTO);
        PowerMockito.doReturn(null).when(spyService,"validateBillStockGeneral", billStockDTO);
        PowerMockito.doReturn(null).when(spyService,"validateLstGoods", any(), any());
        PowerMockito.doReturn(null).when(spyService,"validateStockTotalGeneral", any(), any(), any());
        when(stockTrans.getStatus()).thenReturn("5");
        when(stockTrans.getRegionStockTransId()).thenReturn(1L);
        spyService.createNote(orderAction, billStockDTO);
    }

    @Test
    public void testCreateNote_32() throws  Exception {
        //117
        String orderAction = "orderAction";
        BillStockDTO billStockDTO = new BillStockDTO();
        StockTrans stockTrans = mock(StockTrans.class);
        StockTransVofficeDTO stockTransVofficeDTO = mock(StockTransVofficeDTO.class);
        StockTransActionDTO stockTransActionDTO = mock(StockTransActionDTO.class);
        stockTransActionDTO.setStockTransId(1L);
        stockTransActionDTO.setStockTransActionId(1L);
        billStockDTO.setOrderCode("1");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        when( stockTransRepo.findOne(1L)).thenReturn(stockTrans);
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(),any())).thenReturn(stockTransActionDTO).thenReturn(null);
        when(stockTransVofficeService.findStockTransVofficeByActionId(any())).thenReturn(stockTransVofficeDTO);
        PowerMockito.doReturn(null).when(spyService,"validateBillStockGeneral", billStockDTO);
        PowerMockito.doReturn(null).when(spyService,"validateLstGoods", any(), any());
        PowerMockito.doReturn(null).when(spyService,"validateStockTotalGeneral", any(), any(), any());
        when(stockTrans.getStatus()).thenReturn("2");
        when(stockTrans.getRegionStockTransId()).thenReturn(1L);
        spyService.createNote(orderAction, billStockDTO);
    }

    @Test
    public void testCreateNote_33() throws  Exception {
        String orderAction = "orderAction";
        BillStockDTO billStockDTO = new BillStockDTO();
        StockTrans stockTrans = mock(StockTrans.class);
        StockTransVofficeDTO stockTransVofficeDTO = mock(StockTransVofficeDTO.class);
        StockTransActionDTO stockTransActionDTO = mock(StockTransActionDTO.class);
        ShopDTO shopDTO = new   ShopDTO();
        stockTransActionDTO.setStockTransId(1L);
        stockTransActionDTO.setStockTransActionId(1L);
        billStockDTO.setOrderCode("1");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        when( stockTransRepo.findOne(1L)).thenReturn(stockTrans);
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(),any())).thenReturn(stockTransActionDTO).thenReturn(null);
        when(stockTransVofficeService.findStockTransVofficeByActionId(any())).thenReturn(stockTransVofficeDTO);
        when(stockTransActionDTO.getStockTransActionId()).thenReturn(0L);
        when(stockTransActionDTO.getRegionOwnerId()).thenReturn(5L);
        PowerMockito.doReturn(null).when(spyService,"validateBillStockGeneral", billStockDTO);
        PowerMockito.doReturn(null).when(spyService,"validateLstGoods", any(), any());
        PowerMockito.doReturn(null).when(spyService,"validateStockTotalGeneral", any(), any(), any());
        when(stockTrans.getStatus()).thenReturn("1");
        when(stockTrans.getFromOwnerType()).thenReturn(1L);
        when(stockTrans.getRegionStockTransId()).thenReturn(null);
        when(stockTrans.getToOwnerType()).thenReturn(1L);
        when(shopService.findOne(anyLong())).thenReturn(shopDTO);
        spyService.createNote(orderAction, billStockDTO);
    }
    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method LogisticBaseServiceImpl.transStock
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testTransStock_1() throws  Exception{
        //231
        BillStockDTO billStockDTO = new BillStockDTO();
        logisticBaseService.transStock(billStockDTO);
    }

    @Test
    public void testTransStock_2() throws  Exception{
        //234
        BillStockDTO billStockDTO = new BillStockDTO();
        billStockDTO.setTransCode("java.lang.NullPointerException");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        PowerMockito.doReturn(null).when(spyService,"validateBillStockGeneral", any());
        spyService.transStock(billStockDTO);
    }

    @Test
    public void testTransStock_3() throws  Exception{
        //239
        BillStockDTO billStockDTO = new BillStockDTO();
        billStockDTO.setTransCode("1");
        billStockDTO.setOrderCode("2");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        PowerMockito.doReturn(null).when(spyService,"validateBillStockGeneral", any());
        when(stockTransRepo.findOne(anyLong())).thenReturn(null);
        spyService.transStock(billStockDTO);
    }

    @Test
    public void testTransStock_4() throws  Exception{
        //242
        StockTrans stockTrans = mock(StockTrans.class);
        BillStockDTO billStockDTO = new BillStockDTO();
        billStockDTO.setTransCode("1");
        billStockDTO.setOrderCode("2");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        PowerMockito.doReturn(null).when(spyService,"validateBillStockGeneral", any());
        when(stockTransRepo.findOne(anyLong())).thenReturn(stockTrans);
        when(stockTrans.getStatus()).thenReturn("1");
        spyService.transStock(billStockDTO);
    }

    @Test
    public void testTransStock_5() throws  Exception{
        //250
        StockTrans stockTrans = mock(StockTrans.class);
        StockTransActionDTO stockTransActionNote = mock(StockTransActionDTO.class);
        BillStockDTO billStockDTO = new BillStockDTO();
        billStockDTO.setTransCode("1");
        billStockDTO.setOrderCode("2");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        PowerMockito.doReturn(null).when(spyService,"validateBillStockGeneral", any());
        when(stockTransRepo.findOne(anyLong())).thenReturn(stockTrans);
        when(stockTrans.getStatus()).thenReturn("2");
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(), any())).thenReturn(stockTransActionNote);
        when(stockTransActionNote.getStockTransActionId()).thenReturn(null);
        spyService.transStock(billStockDTO);
    }

    @Test
    public void testTransStock_6() throws  Exception{
        //254
        StockTrans stockTrans = mock(StockTrans.class);
        StockTransActionDTO stockTransActionNote = mock(StockTransActionDTO.class);
        BillStockDTO billStockDTO = new BillStockDTO();
        billStockDTO.setTransCode("1");
        billStockDTO.setOrderCode("2");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        PowerMockito.doReturn(null).when(spyService,"validateBillStockGeneral", any());
        when(stockTransRepo.findOne(anyLong())).thenReturn(stockTrans);
        when(stockTrans.getStatus()).thenReturn("2");
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(), any())).thenReturn(stockTransActionNote);
        when(stockTransActionNote.getStockTransActionId()).thenReturn(2L);
        when(stockTransActionNote.getSignCaStatus()).thenReturn("2");
        when(stockTransVofficeService.findStockTransVofficeByActionId(any())).thenReturn(null);
        spyService.transStock(billStockDTO);
    }

    @Test
    public void testTransStock_7() throws  Exception{
        //262
        StockTrans stockTrans = mock(StockTrans.class);
        StockTransActionDTO stockTransActionNote = mock(StockTransActionDTO.class);
        BillStockDTO billStockDTO = new BillStockDTO();
        billStockDTO.setTransCode("1");
        billStockDTO.setOrderCode("2");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        PowerMockito.doReturn(null).when(spyService,"validateBillStockGeneral", any());
        when(stockTransRepo.findOne(anyLong())).thenReturn(stockTrans);
        when(stockTrans.getStatus()).thenReturn("2");
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(), any())).thenReturn(stockTransActionNote);
        when(stockTransActionNote.getStockTransActionId()).thenReturn(2L);
        when(stockTransActionNote.getSignCaStatus()).thenReturn("3");
        spyService.transStock(billStockDTO);
    }

    @Test
    public void testTransStock_8() throws  Exception{
        //266-278
        StockTrans stockTrans = mock(StockTrans.class);
        StockTransActionDTO stockTransActionNote = mock(StockTransActionDTO.class);
        BillStockDTO billStockDTO = new BillStockDTO();
        billStockDTO.setTransCode("1");
        billStockDTO.setOrderCode("2");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        PowerMockito.doReturn(null).when(spyService,"validateBillStockGeneral", any());
        PowerMockito.doReturn(null).when(spyService,"validateLstGoods", any(), any());
        when(stockTransRepo.findOne(anyLong())).thenReturn(stockTrans);
        when(stockTrans.getStatus()).thenReturn("2");
        when(stockTrans.getFromOwnerType()).thenReturn(1L);
        when(stockTrans.getRegionStockTransId()).thenReturn(1L);
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(), any())).thenReturn(stockTransActionNote);
        when(stockTransActionNote.getStockTransActionId()).thenReturn(2L);
        when(stockTransActionNote.getSignCaStatus()).thenReturn("3");
        spyService.transStock(billStockDTO);
    }

    @Test
    public void testTransStock_9() throws  Exception{
        //282
        StockTrans stockTrans = mock(StockTrans.class);
        StockTransActionDTO stockTransActionNote = mock(StockTransActionDTO.class);
        BillStockDTO billStockDTO = new BillStockDTO();
        billStockDTO.setTransCode("1");
        billStockDTO.setOrderCode("2");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        PowerMockito.doReturn(null).when(spyService,"validateBillStockGeneral", any());
        PowerMockito.doReturn(null).when(spyService,"validateLstGoods", any(), any());
        PowerMockito.doReturn(null).when(spyService,"validateStockTotalGeneral", any(), any(), any());
        when(stockTransRepo.findOne(anyLong())).thenReturn(stockTrans);
        when(stockTrans.getStatus()).thenReturn("20");
        when(stockTrans.getFromOwnerType()).thenReturn(1L);
        when(stockTrans.getRegionStockTransId()).thenReturn(1L);
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(), any())).thenReturn(stockTransActionNote);
        when(stockTransActionNote.getStockTransActionId()).thenReturn(2L);
        when(stockTransActionNote.getSignCaStatus()).thenReturn("3");
        spyService.transStock(billStockDTO);
    }

    @Test(expected = Exception.class)
    public void testTransStock_10() throws  Exception{
        //282
        StockTrans stockTrans = mock(StockTrans.class);
        StockTransActionDTO stockTransActionNote = mock(StockTransActionDTO.class);
        BillStockDTO billStockDTO = new BillStockDTO();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> lstStockTransDetailDTOs = Lists.newArrayList(stockTransDetailDTO);
        billStockDTO.setTransCode("1");
        billStockDTO.setOrderCode("2");
        billStockDTO.setOrderType("2");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        PowerMockito.doReturn(null).when(spyService,"validateBillStockGeneral", any());
        PowerMockito.doReturn(null).when(spyService,"validateLstGoods", any(), any());
        PowerMockito.doReturn(null).when(spyService,"validateStockTotalGeneral", any(), any(), any());
        when(stockTransRepo.findOne(anyLong())).thenReturn(stockTrans);
        when(stockTrans.getStatus()).thenReturn("3");
        when(stockTrans.getFromOwnerType()).thenReturn(1L);
        when(stockTrans.getRegionStockTransId()).thenReturn(1L);
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(), any())).thenReturn(stockTransActionNote);
        when(stockTransActionNote.getStockTransActionId()).thenReturn(2L);
        when(stockTransActionNote.getSignCaStatus()).thenReturn("3");
        when(stockTransDetailService.findByStockTransId(any())).thenReturn(lstStockTransDetailDTOs);
        spyService.transStock(billStockDTO);
    }

    @Test(expected = Exception.class)
    public void testTransStock_11() throws  Exception{
        //282
        StockTrans stockTrans = mock(StockTrans.class);
        StockTransActionDTO stockTransActionNote = mock(StockTransActionDTO.class);
        BillStockDTO billStockDTO = new BillStockDTO();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> lstStockTransDetailDTOs = Lists.newArrayList(stockTransDetailDTO);
        billStockDTO.setTransCode("1");
        billStockDTO.setOrderCode("2");
        billStockDTO.setOrderType("3");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        PowerMockito.doReturn(null).when(spyService,"validateBillStockGeneral", any());
        PowerMockito.doReturn(null).when(spyService,"validateLstGoods", any(), any());
        PowerMockito.doReturn(null).when(spyService,"validateStockTotalGeneral", any(), any(), any());
        when(stockTransRepo.findOne(anyLong())).thenReturn(stockTrans);
        when(stockTrans.getStatus()).thenReturn("6");
        when(stockTrans.getFromOwnerType()).thenReturn(1L);
        when(stockTrans.getRegionStockTransId()).thenReturn(1L);
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(), any())).thenReturn(stockTransActionNote);
        when(stockTransActionNote.getStockTransActionId()).thenReturn(2L);
        when(stockTransActionNote.getSignCaStatus()).thenReturn("3");
        when(stockTransDetailService.findByStockTransId(any())).thenReturn(lstStockTransDetailDTOs);
        spyService.transStock(billStockDTO);
    }

    @Test(expected = Exception.class)
    public void testTransStock_12() throws  Exception{
        //282
        StockTrans stockTrans = mock(StockTrans.class);
        StockTransActionDTO stockTransActionNote = mock(StockTransActionDTO.class);
        BillStockDTO billStockDTO = new BillStockDTO();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> lstStockTransDetailDTOs = Lists.newArrayList(stockTransDetailDTO);
        billStockDTO.setTransCode("1");
        billStockDTO.setOrderCode("2");
        billStockDTO.setOrderType("3");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        PowerMockito.doReturn(null).when(spyService,"validateBillStockGeneral", any());
        PowerMockito.doReturn(null).when(spyService,"validateLstGoods", any(), any());
        PowerMockito.doReturn(null).when(spyService,"validateStockTotalGeneral", any(), any(), any());
        when(stockTransRepo.findOne(anyLong())).thenReturn(stockTrans);
        when(stockTrans.getStatus()).thenReturn("5");
        when(stockTrans.getFromOwnerType()).thenReturn(1L);
        when(stockTrans.getRegionStockTransId()).thenReturn(1L);
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(), any())).thenReturn(stockTransActionNote);
        when(stockTransActionNote.getStockTransActionId()).thenReturn(2L);
        when(stockTransActionNote.getSignCaStatus()).thenReturn("3");
        when(stockTransDetailService.findByStockTransId(any())).thenReturn(lstStockTransDetailDTOs);
        Mockito.doThrow(Exception.class).when(em).lock(stockTrans, LockModeType.PESSIMISTIC_READ);
        spyService.transStock(billStockDTO);
    }

    @Test(expected = Exception.class)
    public void testTransStock_13() throws  Exception{
        //282
        StockTrans stockTrans = mock(StockTrans.class);
        StockTransActionDTO stockTransActionNote = mock(StockTransActionDTO.class);
        BillStockDTO billStockDTO = new BillStockDTO();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId(0L);
        stockTransDetailDTO.setStateId(0L);
        StockTransDTO stockTransDTO = new StockTransDTO();
        GoodsDTO goodsDTO = new GoodsDTO();
        StockTransDetailOfflineDTO stockTransDetailOfflineResult = mock(StockTransDetailOfflineDTO.class);
        goodsDTO.setStockModelId(0L);
        goodsDTO.setGoodsState("0");
        List<GoodsDTO> lstGoods = Lists.newArrayList(goodsDTO);
        List<StockTransDetailDTO> lstStockTransDetailDTOs = Lists.newArrayList(stockTransDetailDTO);
        billStockDTO.setTransCode("1");
        billStockDTO.setOrderCode("2");
        billStockDTO.setOrderType("3");
        billStockDTO.setLstGoods(lstGoods);
        billStockDTO.getLstGoods().get(0).setCheckSerial(1L);
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        PowerMockito.doReturn(null).when(spyService,"validateBillStockGeneral", any());
        PowerMockito.doReturn(null).when(spyService,"validateLstGoods", any(), any());
        PowerMockito.doReturn(null).when(spyService,"validateStockTotalGeneral", any(), any(), any());
        when(stockTransRepo.findOne(anyLong())).thenReturn(stockTrans);
        when(stockTrans.getStatus()).thenReturn("5");
        when(stockTrans.getFromOwnerType()).thenReturn(1L);
        when(stockTrans.getRegionStockTransId()).thenReturn(1L);
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(), any())).thenReturn(stockTransActionNote);
        when(stockTransActionNote.getStockTransActionId()).thenReturn(2L);
        when(stockTransActionNote.getSignCaStatus()).thenReturn("3");
        when(stockTransDetailService.findByStockTransId(any())).thenReturn(lstStockTransDetailDTOs);
        when(stockTransDetailOfflineService.save(any())).thenReturn(stockTransDetailOfflineResult);
        when(stockTransDetailOfflineResult.getStockTransDetailId()).thenReturn(1L);
        Mockito.doNothing().when(em).lock(stockTrans, LockModeType.PESSIMISTIC_READ);
        setFinalStatic(LogisticBaseServiceImpl.class.getDeclaredField("mapperStockTrans"), mapperStockTrans, spyService);
        when(mapperStockTrans.toDtoBean(stockTrans)).thenReturn(stockTransDTO);
        spyService.transStock(billStockDTO);
    }

    @Test
    public void testTransStock_14() throws  Exception{
        //282
        StockTrans stockTrans = mock(StockTrans.class);
        StockTransActionDTO stockTransActionNote = mock(StockTransActionDTO.class);
        BillStockDTO billStockDTO = new BillStockDTO();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId(0L);
        stockTransDetailDTO.setStateId(0L);
        StockTransDTO stockTransDTO = new StockTransDTO();
        GoodsDTO goodsDTO = new GoodsDTO();
        Serial serial = new Serial();
        serial.setQuantity("2");
        StockTransDetailOfflineDTO stockTransDetailOfflineResult = mock(StockTransDetailOfflineDTO.class);
        List<Serial> lstSerial = Lists.newArrayList(serial);
        goodsDTO.setStockModelId(0L);
        goodsDTO.setGoodsState("0");
        goodsDTO.setCheckSerial(2L);
        goodsDTO.setLstSerial(lstSerial);
        List<GoodsDTO> lstGoods = Lists.newArrayList(goodsDTO);
        List<StockTransDetailDTO> lstStockTransDetailDTOs = Lists.newArrayList(stockTransDetailDTO);
        billStockDTO.setTransCode("1");
        billStockDTO.setOrderCode("2");
        billStockDTO.setOrderType("3");
        billStockDTO.setLstGoods(lstGoods);
        billStockDTO.getLstGoods().get(0).setLstSerial(lstSerial);
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        PowerMockito.doReturn(null).when(spyService,"validateBillStockGeneral", any());
        PowerMockito.doReturn(null).when(spyService,"validateLstGoods", any(), any());
        PowerMockito.doReturn(null).when(spyService,"validateStockTotalGeneral", any(), any(), any());
        when(stockTransRepo.findOne(anyLong())).thenReturn(stockTrans);
        when(stockTrans.getStatus()).thenReturn("5");
        when(stockTrans.getFromOwnerType()).thenReturn(1L);
        when(stockTrans.getRegionStockTransId()).thenReturn(1L);
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(), any())).thenReturn(stockTransActionNote);
        when(stockTransActionNote.getStockTransActionId()).thenReturn(2L);
        when(stockTransActionNote.getSignCaStatus()).thenReturn("3");
        when(stockTransDetailService.findByStockTransId(any())).thenReturn(lstStockTransDetailDTOs);
        when(stockTransDetailOfflineService.save(any())).thenReturn(stockTransDetailOfflineResult);
        when(stockTransDetailOfflineResult.getStockTransDetailId()).thenReturn(1L);
        Mockito.doNothing().when(em).lock(stockTrans, LockModeType.PESSIMISTIC_READ);
        setFinalStatic(LogisticBaseServiceImpl.class.getDeclaredField("mapperStockTrans"), mapperStockTrans, spyService);
        when(mapperStockTrans.toDtoBean(stockTrans)).thenReturn(stockTransDTO);
        spyService.transStock(billStockDTO);
    }

    @Test
    public void testTransStock_15() throws  Exception{
        //254
        StockTransVofficeDTO stockTransVofficeDTO = mock(StockTransVofficeDTO.class);
        StockTrans stockTrans = mock(StockTrans.class);
        StockTransActionDTO stockTransActionNote = mock(StockTransActionDTO.class);
        BillStockDTO billStockDTO = new BillStockDTO();
        billStockDTO.setTransCode("1");
        billStockDTO.setOrderCode("2");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        ResultLogisticsDTO stockGeneralDTO = mock(ResultLogisticsDTO.class);
        PowerMockito.doReturn(stockGeneralDTO).when(spyService,"validateBillStockGeneral", any());
        when(stockGeneralDTO.getResponseCode()).thenReturn(null);
        when(stockTransRepo.findOne(anyLong())).thenReturn(stockTrans);
        when(stockTrans.getStatus()).thenReturn("2");
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(), any())).thenReturn(stockTransActionNote);
        when(stockTransActionNote.getStockTransActionId()).thenReturn(2L);
        when(stockTransActionNote.getSignCaStatus()).thenReturn("2");
        when(stockTransVofficeService.findStockTransVofficeByActionId(any())).thenReturn(stockTransVofficeDTO);
        when(stockTransVofficeDTO.getStatus()).thenReturn("1");
        spyService.transStock(billStockDTO);
    }

    @Test
    public void testTransStock_16() throws  Exception{
        //254
        StockTrans stockTrans = mock(StockTrans.class);
        StockTransActionDTO stockTransActionNote = mock(StockTransActionDTO.class);
        BillStockDTO billStockDTO = new BillStockDTO();
        billStockDTO.setTransCode("1");
        billStockDTO.setOrderCode("2");
        StockTransVofficeDTO stockTransVofficeDTO = mock(StockTransVofficeDTO.class);
        stockTransVofficeDTO.setStatus("3");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        PowerMockito.doReturn(null).when(spyService,"validateBillStockGeneral", any());
        when(stockTransRepo.findOne(anyLong())).thenReturn(stockTrans);
        when(stockTrans.getStatus()).thenReturn("2");
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(), any())).thenReturn(stockTransActionNote);
        when(stockTransActionNote.getStockTransActionId()).thenReturn(2L);
        when(stockTransActionNote.getSignCaStatus()).thenReturn("2");
        when(stockTransVofficeService.findStockTransVofficeByActionId(any())).thenReturn(stockTransVofficeDTO);
        spyService.transStock(billStockDTO);
    }

    @Test
    public void testTransStock_17() throws  Exception{
        //254
        StockTransVofficeDTO stockTransVofficeDTO = mock(StockTransVofficeDTO.class);
        StockTrans stockTrans = mock(StockTrans.class);
        StockTransActionDTO stockTransActionNote = mock(StockTransActionDTO.class);
        BillStockDTO billStockDTO = new BillStockDTO();
        billStockDTO.setTransCode("1");
        billStockDTO.setOrderCode("2");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        ResultLogisticsDTO stockGeneralDTO = mock(ResultLogisticsDTO.class);
        PowerMockito.doReturn(stockGeneralDTO).when(spyService,"validateBillStockGeneral", any());
        when(stockGeneralDTO.getResponseCode()).thenReturn("0");
        when(stockTransRepo.findOne(anyLong())).thenReturn(stockTrans);
        when(stockTrans.getStatus()).thenReturn("2");
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(), any())).thenReturn(stockTransActionNote);
        when(stockTransActionNote.getStockTransActionId()).thenReturn(2L);
        when(stockTransActionNote.getSignCaStatus()).thenReturn("2");
        when(stockTransVofficeService.findStockTransVofficeByActionId(any())).thenReturn(stockTransVofficeDTO);
        when(stockTransVofficeDTO.getStatus()).thenReturn("1");
        spyService.transStock(billStockDTO);
    }

    @Test
    public void testTransStock_18() throws  Exception{
        //254
        StockTrans stockTrans = mock(StockTrans.class);
        StockTransActionDTO stockTransActionNote = mock(StockTransActionDTO.class);
        BillStockDTO billStockDTO = new BillStockDTO();
        billStockDTO.setTransCode("1");
        billStockDTO.setOrderCode("2");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        PowerMockito.doReturn(null).when(spyService,"validateBillStockGeneral", any());
        when(stockTransRepo.findOne(anyLong())).thenReturn(stockTrans);
        when(stockTrans.getStatus()).thenReturn("2");
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(), any())).thenReturn(stockTransActionNote);
        when(stockTransActionNote.getStockTransActionId()).thenReturn(2L);
        when(stockTransActionNote.getSignCaStatus()).thenReturn(null);
        when(stockTransVofficeService.findStockTransVofficeByActionId(any())).thenReturn(null);
        spyService.transStock(billStockDTO);
    }

    @Test
    public void testTransStock_19() throws  Exception{
        //234
        BillStockDTO billStockDTO = new BillStockDTO();
        billStockDTO.setTransCode("java.lang.NullPointerException");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);

        ResultLogisticsDTO stockGeneralDTO = new ResultLogisticsDTO();
        PowerMockito.doReturn(stockGeneralDTO).when(spyService,"validateBillStockGeneral", any());
        spyService.transStock(billStockDTO);
    }

    @Test
    public void testTransStock_20() throws  Exception{
        //234
        BillStockDTO billStockDTO = new BillStockDTO();
        billStockDTO.setTransCode("java.lang.NullPointerException");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);

        ResultLogisticsDTO stockGeneralDTO = new ResultLogisticsDTO();
        stockGeneralDTO.setResponseCode("cd");
        PowerMockito.doReturn(stockGeneralDTO).when(spyService,"validateBillStockGeneral", any());
        spyService.transStock(billStockDTO);
    }

    @Test
    public void testTransStock_21() throws  Exception{
        //234
        BillStockDTO billStockDTO = new BillStockDTO();
        billStockDTO.setTransCode("java.lang.NullPointerException");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);

        ResultLogisticsDTO stockGeneralDTO = new ResultLogisticsDTO();
        stockGeneralDTO.setResponseCode("1");
        PowerMockito.doReturn(stockGeneralDTO).when(spyService,"validateBillStockGeneral", any());
        spyService.transStock(billStockDTO);
    }

    @Test
    public void testTransStock_22() throws  Exception{
        //266-278
        StockTrans stockTrans = mock(StockTrans.class);
        StockTransActionDTO stockTransActionNote = mock(StockTransActionDTO.class);
        BillStockDTO billStockDTO = new BillStockDTO();
        billStockDTO.setTransCode("1");
        billStockDTO.setOrderCode("2");
        ResultLogisticsDTO resultLogisticsDTO = new ResultLogisticsDTO();

        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        PowerMockito.doReturn(null).when(spyService,"validateBillStockGeneral", any());
        PowerMockito.doReturn(resultLogisticsDTO).when(spyService,"validateLstGoods", any(), any());
        when(stockTransRepo.findOne(anyLong())).thenReturn(stockTrans);
        when(stockTrans.getStatus()).thenReturn("2");
        when(stockTrans.getFromOwnerType()).thenReturn(1L);
        when(stockTrans.getRegionStockTransId()).thenReturn(1L);
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(), any())).thenReturn(stockTransActionNote);
        when(stockTransActionNote.getStockTransActionId()).thenReturn(2L);
        when(stockTransActionNote.getSignCaStatus()).thenReturn("3");
        spyService.transStock(billStockDTO);
    }

    @Test
    public void testTransStock_23() throws  Exception{
        //266-278
        StockTrans stockTrans = mock(StockTrans.class);
        StockTransActionDTO stockTransActionNote = mock(StockTransActionDTO.class);
        BillStockDTO billStockDTO = new BillStockDTO();
        billStockDTO.setTransCode("1");
        billStockDTO.setOrderCode("2");
        ResultLogisticsDTO resultLogisticsDTO = new ResultLogisticsDTO();
        resultLogisticsDTO.setResponseCode("1");

        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        PowerMockito.doReturn(null).when(spyService,"validateBillStockGeneral", any());
        PowerMockito.doReturn(resultLogisticsDTO).when(spyService,"validateLstGoods", any(), any());
        when(stockTransRepo.findOne(anyLong())).thenReturn(stockTrans);
        when(stockTrans.getStatus()).thenReturn("2");
        when(stockTrans.getFromOwnerType()).thenReturn(1L);
        when(stockTrans.getRegionStockTransId()).thenReturn(1L);
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(), any())).thenReturn(stockTransActionNote);
        when(stockTransActionNote.getStockTransActionId()).thenReturn(2L);
        when(stockTransActionNote.getSignCaStatus()).thenReturn("3");
        spyService.transStock(billStockDTO);
    }

    @Test
    public void testTransStock_24() throws  Exception{
        //282
        StockTrans stockTrans = mock(StockTrans.class);
        StockTransActionDTO stockTransActionNote = mock(StockTransActionDTO.class);
        BillStockDTO billStockDTO = new BillStockDTO();
        billStockDTO.setTransCode("1");
        billStockDTO.setOrderCode("2");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        PowerMockito.doReturn(null).when(spyService,"validateBillStockGeneral", any());
        PowerMockito.doReturn(null).when(spyService,"validateLstGoods", any(), any());
        PowerMockito.doReturn(null).when(spyService,"validateStockTotalGeneral", any(), any(), any());
        when(stockTransRepo.findOne(anyLong())).thenReturn(stockTrans);
        when(stockTrans.getStatus()).thenReturn("2");
        when(stockTrans.getFromOwnerType()).thenReturn(1L);
        when(stockTrans.getRegionStockTransId()).thenReturn(null);
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(), any())).thenReturn(stockTransActionNote);
        when(stockTransActionNote.getStockTransActionId()).thenReturn(2L);
        when(stockTransActionNote.getSignCaStatus()).thenReturn("3");
        spyService.transStock(billStockDTO);
    }

    @Test
    public void testTransStock_25() throws  Exception{
        //282
        StockTrans stockTrans = mock(StockTrans.class);
        StockTransActionDTO stockTransActionNote = mock(StockTransActionDTO.class);
        BillStockDTO billStockDTO = new BillStockDTO();
        billStockDTO.setTransCode("1");
        billStockDTO.setOrderCode("2");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        PowerMockito.doReturn(null).when(spyService,"validateBillStockGeneral", any());
        PowerMockito.doReturn(null).when(spyService,"validateLstGoods", any(), any());
        PowerMockito.doReturn(null).when(spyService,"validateStockTotalGeneral", any(), any(), any());
        when(stockTransRepo.findOne(anyLong())).thenReturn(stockTrans);
        when(stockTrans.getStatus()).thenReturn("2");
        when(stockTrans.getFromOwnerType()).thenReturn(1L);
        when(stockTrans.getRegionStockTransId()).thenReturn(null);
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(), any())).thenReturn(stockTransActionNote);
        when(stockTransActionNote.getStockTransActionId()).thenReturn(2L);
        when(stockTransActionNote.getSignCaStatus()).thenReturn("3");
        spyService.transStock(billStockDTO);
    }

    @Test
    public void testTransStock_26() throws  Exception{
        //282
        StockTrans stockTrans = mock(StockTrans.class);
        StockTransActionDTO stockTransActionNote = mock(StockTransActionDTO.class);
        BillStockDTO billStockDTO = new BillStockDTO();
        billStockDTO.setTransCode("1");
        billStockDTO.setOrderCode("2");
        ResultLogisticsDTO resultLogisticsDTO = new ResultLogisticsDTO();

        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        PowerMockito.doReturn(null).when(spyService,"validateBillStockGeneral", any());
        PowerMockito.doReturn(null).when(spyService,"validateLstGoods", any(), any());
        PowerMockito.doReturn(resultLogisticsDTO).when(spyService,"validateStockTotalGeneral", any(), any(), any());
        when(stockTransRepo.findOne(anyLong())).thenReturn(stockTrans);
        when(stockTrans.getStatus()).thenReturn("2");
        when(stockTrans.getFromOwnerType()).thenReturn(1L);
        when(stockTrans.getRegionStockTransId()).thenReturn(null);
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(), any())).thenReturn(stockTransActionNote);
        when(stockTransActionNote.getStockTransActionId()).thenReturn(2L);
        when(stockTransActionNote.getSignCaStatus()).thenReturn("3");
        spyService.transStock(billStockDTO);
    }

    @Test
    public void testTransStock_27() throws  Exception{
        //282
        StockTrans stockTrans = mock(StockTrans.class);
        StockTransActionDTO stockTransActionNote = mock(StockTransActionDTO.class);
        BillStockDTO billStockDTO = new BillStockDTO();
        billStockDTO.setTransCode("1");
        billStockDTO.setOrderCode("2");
        ResultLogisticsDTO resultLogisticsDTO = new ResultLogisticsDTO();
        resultLogisticsDTO.setResponseCode("1");

        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        PowerMockito.doReturn(null).when(spyService,"validateBillStockGeneral", any());
        PowerMockito.doReturn(null).when(spyService,"validateLstGoods", any(), any());
        PowerMockito.doReturn(resultLogisticsDTO).when(spyService,"validateStockTotalGeneral", any(), any(), any());
        when(stockTransRepo.findOne(anyLong())).thenReturn(stockTrans);
        when(stockTrans.getStatus()).thenReturn("2");
        when(stockTrans.getFromOwnerType()).thenReturn(1L);
        when(stockTrans.getRegionStockTransId()).thenReturn(null);
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(), any())).thenReturn(stockTransActionNote);
        when(stockTransActionNote.getStockTransActionId()).thenReturn(2L);
        when(stockTransActionNote.getSignCaStatus()).thenReturn("3");
        spyService.transStock(billStockDTO);
    }

    @Test
    public void testTransStock_28() throws  Exception{
        StockTrans stockTrans = mock(StockTrans.class);
        StockTransActionDTO stockTransActionNote = mock(StockTransActionDTO.class);
        BillStockDTO billStockDTO = new BillStockDTO();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId(0L);
        stockTransDetailDTO.setStateId(0L);
        StockTransDTO stockTransDTO = new StockTransDTO();
        GoodsDTO goodsDTO = new GoodsDTO();
        Serial serial = new Serial();
        serial.setQuantity("2");
        StockTransDetailOfflineDTO stockTransDetailOfflineResult = mock(StockTransDetailOfflineDTO.class);
        List<Serial> lstSerial = Lists.newArrayList(serial);
        goodsDTO.setStockModelId(0L);
        goodsDTO.setGoodsState("0");
        goodsDTO.setCheckSerial(2L);
        goodsDTO.setLstSerial(lstSerial);
        List<GoodsDTO> lstGoods = Lists.newArrayList();
        List<StockTransDetailDTO> lstStockTransDetailDTOs = Lists.newArrayList(stockTransDetailDTO);
        billStockDTO.setTransCode("1");
        billStockDTO.setOrderCode("2");
        billStockDTO.setOrderType("3");
        billStockDTO.setLstGoods(lstGoods);
//        billStockDTO.getLstGoods().get(0).setLstSerial(lstSerial);
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        PowerMockito.doReturn(null).when(spyService,"validateBillStockGeneral", any());
        PowerMockito.doReturn(null).when(spyService,"validateLstGoods", any(), any());
        PowerMockito.doReturn(null).when(spyService,"validateStockTotalGeneral", any(), any(), any());
        when(stockTransRepo.findOne(anyLong())).thenReturn(stockTrans);
        when(stockTrans.getStatus()).thenReturn("5");
        when(stockTrans.getFromOwnerType()).thenReturn(1L);
        when(stockTrans.getRegionStockTransId()).thenReturn(1L);
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(), any())).thenReturn(stockTransActionNote);
        when(stockTransActionNote.getStockTransActionId()).thenReturn(2L);
        when(stockTransActionNote.getSignCaStatus()).thenReturn("3");
        when(stockTransDetailService.findByStockTransId(any())).thenReturn(lstStockTransDetailDTOs);
        when(stockTransDetailOfflineService.save(any())).thenReturn(stockTransDetailOfflineResult);
        when(stockTransDetailOfflineResult.getStockTransDetailId()).thenReturn(1L);
        Mockito.doNothing().when(em).lock(stockTrans, LockModeType.PESSIMISTIC_READ);
        setFinalStatic(LogisticBaseServiceImpl.class.getDeclaredField("mapperStockTrans"), mapperStockTrans, spyService);
        when(mapperStockTrans.toDtoBean(stockTrans)).thenReturn(stockTransDTO);
        spyService.transStock(billStockDTO);
    }


    @Test(expected = Exception.class)
    public void testTransStock_29() throws  Exception{
        //282
        StockTrans stockTrans = mock(StockTrans.class);
        StockTransActionDTO stockTransActionNote = mock(StockTransActionDTO.class);
        BillStockDTO billStockDTO = new BillStockDTO();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> lstStockTransDetailDTOs = Lists.newArrayList(stockTransDetailDTO);
        billStockDTO.setTransCode("1");
        billStockDTO.setOrderCode("2");
        billStockDTO.setOrderType("2");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        PowerMockito.doReturn(null).when(spyService,"validateBillStockGeneral", any());
        PowerMockito.doReturn(null).when(spyService,"validateLstGoods", any(), any());
        PowerMockito.doReturn(null).when(spyService,"validateStockTotalGeneral", any(), any(), any());
        when(stockTransRepo.findOne(anyLong())).thenReturn(stockTrans);
        when(stockTrans.getStatus()).thenReturn("5");
        when(stockTrans.getFromOwnerType()).thenReturn(1L);
        when(stockTrans.getRegionStockTransId()).thenReturn(1L);
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(), any())).thenReturn(stockTransActionNote);
        when(stockTransActionNote.getStockTransActionId()).thenReturn(2L);
        when(stockTransActionNote.getSignCaStatus()).thenReturn("3");
        when(stockTransDetailService.findByStockTransId(any())).thenReturn(lstStockTransDetailDTOs);
        spyService.transStock(billStockDTO);
    }


    @Test(expected = Exception.class)
    public void testTransStock_30() throws  Exception{
        //282
        StockTrans stockTrans = mock(StockTrans.class);
        StockTransActionDTO stockTransActionNote = mock(StockTransActionDTO.class);
        BillStockDTO billStockDTO = new BillStockDTO();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId(0L);
        stockTransDetailDTO.setStateId(0L);
        StockTransDTO stockTransDTO = new StockTransDTO();
        GoodsDTO goodsDTO = new GoodsDTO();
        StockTransDetailOfflineDTO stockTransDetailOfflineResult = mock(StockTransDetailOfflineDTO.class);
        goodsDTO.setStockModelId(0L);
        goodsDTO.setGoodsState("0");
        List<GoodsDTO> lstGoods = Lists.newArrayList(goodsDTO);
        List<StockTransDetailDTO> lstStockTransDetailDTOs = Lists.newArrayList(stockTransDetailDTO);
        billStockDTO.setTransCode("1");
        billStockDTO.setOrderCode("2");
        billStockDTO.setOrderType("3");
        billStockDTO.setLstGoods(lstGoods);
        billStockDTO.getLstGoods().get(0).setCheckSerial(1L);

        List<Serial> lstSerial = Lists.newArrayList();
        billStockDTO.getLstGoods().get(0).setLstSerial(lstSerial);

        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        PowerMockito.doReturn(null).when(spyService,"validateLstGoods", any(), any());
        PowerMockito.doReturn(null).when(spyService,"validateBillStockGeneral", any());
        PowerMockito.doReturn(null).when(spyService,"validateStockTotalGeneral", any(), any(), any());
        when(stockTransRepo.findOne(anyLong())).thenReturn(stockTrans);
        when(stockTrans.getStatus()).thenReturn("5");
        when(stockTrans.getFromOwnerType()).thenReturn(1L);
        when(stockTrans.getRegionStockTransId()).thenReturn(1L);
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(), any())).thenReturn(stockTransActionNote);
        when(stockTransActionNote.getStockTransActionId()).thenReturn(2L);
        when(stockTransActionNote.getSignCaStatus()).thenReturn("3");
        when(stockTransDetailService.findByStockTransId(any())).thenReturn(lstStockTransDetailDTOs);
        when(stockTransDetailOfflineService.save(any())).thenReturn(stockTransDetailOfflineResult);
        when(stockTransDetailOfflineResult.getStockTransDetailId()).thenReturn(1L);
        Mockito.doNothing().when(em).lock(stockTrans, LockModeType.PESSIMISTIC_READ);
        setFinalStatic(LogisticBaseServiceImpl.class.getDeclaredField("mapperStockTrans"), mapperStockTrans, spyService);
        when(mapperStockTrans.toDtoBean(stockTrans)).thenReturn(stockTransDTO);
        spyService.transStock(billStockDTO);
    }

    @Test
    public void testTransStock_31() throws  Exception{
        //282
        StockTrans stockTrans = mock(StockTrans.class);
        StockTransActionDTO stockTransActionNote = mock(StockTransActionDTO.class);
        BillStockDTO billStockDTO = new BillStockDTO();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId(0L);
        stockTransDetailDTO.setStateId(0L);
        StockTransDTO stockTransDTO = new StockTransDTO();
        GoodsDTO goodsDTO = new GoodsDTO();
        StockTransDetailOfflineDTO stockTransDetailOfflineResult = mock(StockTransDetailOfflineDTO.class);
        goodsDTO.setStockModelId(0L);
        goodsDTO.setGoodsState("0");
        List<GoodsDTO> lstGoods = Lists.newArrayList(goodsDTO);
        List<StockTransDetailDTO> lstStockTransDetailDTOs = Lists.newArrayList(stockTransDetailDTO);
        billStockDTO.setTransCode("1");
        billStockDTO.setOrderCode("2");
        billStockDTO.setOrderType("3");
        billStockDTO.setLstGoods(lstGoods);
        billStockDTO.getLstGoods().get(0).setCheckSerial(0L);

        List<Serial> lstSerial = Lists.newArrayList();
        billStockDTO.getLstGoods().get(0).setLstSerial(lstSerial);

        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        PowerMockito.doReturn(null).when(spyService,"validateBillStockGeneral", any());
        PowerMockito.doReturn(null).when(spyService,"validateLstGoods", any(), any());
        PowerMockito.doReturn(null).when(spyService,"validateStockTotalGeneral", any(), any(), any());
        when(stockTransRepo.findOne(anyLong())).thenReturn(stockTrans);
        when(stockTrans.getStatus()).thenReturn("5");
        when(stockTrans.getFromOwnerType()).thenReturn(1L);
        when(stockTrans.getRegionStockTransId()).thenReturn(1L);
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(), any())).thenReturn(stockTransActionNote);
        when(stockTransActionNote.getStockTransActionId()).thenReturn(2L);
        when(stockTransActionNote.getSignCaStatus()).thenReturn("3");
        when(stockTransDetailService.findByStockTransId(any())).thenReturn(lstStockTransDetailDTOs);
        when(stockTransDetailOfflineService.save(any())).thenReturn(stockTransDetailOfflineResult);
        when(stockTransDetailOfflineResult.getStockTransDetailId()).thenReturn(1L);
        Mockito.doNothing().when(em).lock(stockTrans, LockModeType.PESSIMISTIC_READ);
        setFinalStatic(LogisticBaseServiceImpl.class.getDeclaredField("mapperStockTrans"), mapperStockTrans, spyService);
        when(mapperStockTrans.toDtoBean(stockTrans)).thenReturn(stockTransDTO);
        spyService.transStock(billStockDTO);
    }
    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method LogisticBaseServiceImpl.cancelOrderOrBill
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCancelOrderOrBill_1() throws  Exception {
        OrderObjectDTO orderObjectDTO =null;
        logisticBaseService.cancelOrderOrBill(orderObjectDTO);
    }

    @Test
    public void testCancelOrderOrBill_2() throws  Exception {
        OrderObjectDTO orderObjectDTO = new OrderObjectDTO();
        logisticBaseService.cancelOrderOrBill(orderObjectDTO);
    }

    @Test
    public void testCancelOrderOrBill_3() throws  Exception {
        OrderObjectDTO orderObjectDTO = new OrderObjectDTO();
        orderObjectDTO.setOrderCode("cd");
        logisticBaseService.cancelOrderOrBill(orderObjectDTO);
    }

    @Test
    public void testCancelOrderOrBill_4() throws  Exception {
        OrderObjectDTO orderObjectDTO = new OrderObjectDTO();
        StockTrans stockTrans = new StockTrans();;
        orderObjectDTO.setOrderCode("1");
        when(stockTransRepo.findOne(anyLong())).thenReturn(null);
        logisticBaseService.cancelOrderOrBill(orderObjectDTO);
    }

    @Test
    public void testCancelOrderOrBill_5() throws  Exception {
        OrderObjectDTO orderObjectDTO = new OrderObjectDTO();
        StockTrans stockTrans = new StockTrans();;
        orderObjectDTO.setOrderCode("1");
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        when(stockTransRepo.findOne(anyLong())).thenReturn(stockTrans);
//        Mockito.doNothing().when(em).lock(stockTrans, LockModeType.PESSIMISTIC_READ);
        Mockito.doThrow(Exception.class).when(em).lock(stockTrans, LockModeType.PESSIMISTIC_READ);
        logisticBaseService.cancelOrderOrBill(orderObjectDTO);
    }

    @Test
    public void testCancelOrderOrBill_6() throws  Exception {
        OrderObjectDTO orderObjectDTO = new OrderObjectDTO();
        StockTrans stockTrans = new StockTrans();
        stockTrans.setStatus("7");
        orderObjectDTO.setOrderCode("1");
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        when(stockTransRepo.findOne(anyLong())).thenReturn(stockTrans);
        Mockito.doNothing().when(em).lock(stockTrans, LockModeType.PESSIMISTIC_READ);
        logisticBaseService.cancelOrderOrBill(orderObjectDTO);
    }

    @Test
    public void testCancelOrderOrBill_7() throws  Exception {
        OrderObjectDTO orderObjectDTO = new OrderObjectDTO();
        StockTrans stockTrans = new StockTrans();
        stockTrans.setStatus("3");
        orderObjectDTO.setOrderCode("1");
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        when(stockTransRepo.findOne(anyLong())).thenReturn(stockTrans);
        Mockito.doNothing().when(em).lock(stockTrans, LockModeType.PESSIMISTIC_READ);
        logisticBaseService.cancelOrderOrBill(orderObjectDTO);
    }

    @Test
    public void testCancelOrderOrBill_8() throws  Exception {
        OrderObjectDTO orderObjectDTO = new OrderObjectDTO();
        StockTrans stockTrans = new StockTrans();
        stockTrans.setStatus("1");
        stockTrans.setPayStatus("1");
        orderObjectDTO.setOrderCode("1");
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        when(stockTransRepo.findOne(anyLong())).thenReturn(stockTrans);
        Mockito.doNothing().when(em).lock(stockTrans, LockModeType.PESSIMISTIC_READ);
        logisticBaseService.cancelOrderOrBill(orderObjectDTO);
    }

    @Test
    public void testCancelOrderOrBill_9() throws  Exception {
        OrderObjectDTO orderObjectDTO = new OrderObjectDTO();
        StockTrans stockTrans = new StockTrans();
        stockTrans.setStatus("1");
        stockTrans.setPayStatus("2");
        stockTrans.setDepositStatus("1");
        orderObjectDTO.setOrderCode("1");
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        when(stockTransRepo.findOne(anyLong())).thenReturn(stockTrans);
        Mockito.doNothing().when(em).lock(stockTrans, LockModeType.PESSIMISTIC_READ);
        logisticBaseService.cancelOrderOrBill(orderObjectDTO);
    }

    @Test
    public void testCancelOrderOrBill_10() throws  Exception {
        OrderObjectDTO orderObjectDTO = new OrderObjectDTO();
        StockTrans stockTrans = new StockTrans();
        StockTransDTO stockTransRegionDto = new StockTransDTO();
        StockTransActionDTO stockTransActionRegion = mock(StockTransActionDTO.class);
        stockTrans.setStatus("1");
        stockTrans.setPayStatus("2");
        stockTrans.setDepositStatus("0");
        stockTrans.setRegionStockTransId(1L);
        orderObjectDTO.setOrderCode("1");
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        when(stockTransRepo.findOne(anyLong())).thenReturn(stockTrans);
        Mockito.doNothing().when(em).lock(stockTrans, LockModeType.PESSIMISTIC_READ);
        when(stockTransService.findOne(stockTrans.getRegionStockTransId())).thenReturn(stockTransRegionDto);
        when(stockTransActionService.getStockTransActionByIdAndStatus(stockTrans.getRegionStockTransId(), Lists.newArrayList(Const.STOCK_TRANS_STATUS.EXPORT_ORDER))).thenReturn(stockTransActionRegion) ;
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(), any())).thenReturn(stockTransActionRegion);
        when(stockTransActionRegion.getRegionOwnerId()).thenReturn(1L);
        when(stockTransActionRegion.getStockTransActionId()).thenReturn(null);
        logisticBaseService.cancelOrderOrBill(orderObjectDTO);
    }

    @Test(expected = Exception.class)
    public void testCancelOrderOrBill_11() throws  Exception {
        OrderObjectDTO orderObjectDTO = new OrderObjectDTO();
        StockTrans stockTrans = new StockTrans();
        StockTransDTO stockTransRegionDto = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = mock(StockTransActionDTO.class);
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> lstStockTransDetailDTOs = Lists.newArrayList(stockTransDetailDTO);
        StockTotalDTO stockTotalDTO = new StockTotalDTO();
        stockTrans.setStatus("1");
        stockTrans.setPayStatus("2");
        stockTrans.setDepositStatus("0");
        stockTrans.setRegionStockTransId(1L);
        orderObjectDTO.setOrderCode("1");
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        when(stockTransRepo.findOne(anyLong())).thenReturn(stockTrans);
        Mockito.doNothing().when(em).lock(stockTrans, LockModeType.PESSIMISTIC_READ);
        when(stockTransService.findOne(stockTrans.getRegionStockTransId())).thenReturn(stockTransRegionDto);
        when(stockTransActionService.getStockTransActionByIdAndStatus(stockTrans.getRegionStockTransId(), Lists.newArrayList(Const.STOCK_TRANS_STATUS.EXPORT_ORDER))).thenReturn(stockTransActionDTO) ;
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(), any())).thenReturn(stockTransActionDTO);
        when(stockTransActionDTO.getRegionOwnerId()).thenReturn(1L);
        when(stockTransActionDTO.getStockTransActionId()).thenReturn(1L);
        when(stockTransDetailService.findByStockTransId(any())).thenReturn(lstStockTransDetailDTOs);
        when(stockTotalService.getStockTotalForProcessStock(1L, stockTrans.getFromOwnerType(), stockTransDetailDTO.getProdOfferId(), stockTransDetailDTO.getStateId())).thenReturn(stockTotalDTO);
        logisticBaseService.cancelOrderOrBill(orderObjectDTO);
    }

    @Test(expected = Exception.class)
    public void testCancelOrderOrBill_12() throws  Exception {
        OrderObjectDTO orderObjectDTO = new OrderObjectDTO();
        StockTrans stockTrans = new StockTrans();
        StockTransDTO stockTransRegionDto = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = mock(StockTransActionDTO.class);
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> lstStockTransDetailDTOs = Lists.newArrayList(stockTransDetailDTO);
        StockTotalDTO stockTotalDTO = new StockTotalDTO();
        stockTrans.setStatus("2");
        stockTrans.setPayStatus("2");
        stockTrans.setDepositStatus("0");
        stockTrans.setRegionStockTransId(1L);
        orderObjectDTO.setOrderCode("1");
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        when(stockTransRepo.findOne(anyLong())).thenReturn(stockTrans);
        Mockito.doNothing().when(em).lock(stockTrans, LockModeType.PESSIMISTIC_READ);
        when(stockTransService.findOne(stockTrans.getRegionStockTransId())).thenReturn(stockTransRegionDto);
        when(stockTransActionService.getStockTransActionByIdAndStatus(stockTrans.getRegionStockTransId(), Lists.newArrayList(Const.STOCK_TRANS_STATUS.EXPORT_ORDER))).thenReturn(stockTransActionDTO) ;
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(), any())).thenReturn(stockTransActionDTO);
        when(stockTransActionDTO.getRegionOwnerId()).thenReturn(1L);
        when(stockTransActionDTO.getStockTransActionId()).thenReturn(1L);
        when(stockTransDetailService.findByStockTransId(any())).thenReturn(lstStockTransDetailDTOs);
        when(stockTotalService.getStockTotalForProcessStock(1L, stockTrans.getFromOwnerType(), stockTransDetailDTO.getProdOfferId(), stockTransDetailDTO.getStateId())).thenReturn(stockTotalDTO);
        logisticBaseService.cancelOrderOrBill(orderObjectDTO);
    }


    @Test
    public void testCancelOrderOrBill_13() throws  Exception {
        OrderObjectDTO orderObjectDTO = new OrderObjectDTO();
        StockTrans stockTrans = new StockTrans();
        StockTransDTO stockTransRegionDto = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = mock(StockTransActionDTO.class);
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> lstStockTransDetailDTOs = Lists.newArrayList(stockTransDetailDTO);
        StockTotalDTO stockTotalDTO = new StockTotalDTO();
        stockTrans.setStatus("4");
        stockTrans.setPayStatus("2");
        stockTrans.setDepositStatus("0");
        stockTrans.setRegionStockTransId(1L);
        orderObjectDTO.setOrderCode("1");
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        when(stockTransRepo.findOne(anyLong())).thenReturn(stockTrans);
        Mockito.doNothing().when(em).lock(stockTrans, LockModeType.PESSIMISTIC_READ);
        when(stockTransService.findOne(stockTrans.getRegionStockTransId())).thenReturn(stockTransRegionDto);
        when(stockTransActionService.getStockTransActionByIdAndStatus(stockTrans.getRegionStockTransId(), Lists.newArrayList(Const.STOCK_TRANS_STATUS.EXPORT_ORDER))).thenReturn(stockTransActionDTO) ;
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(), any())).thenReturn(stockTransActionDTO);
        when(stockTransActionDTO.getRegionOwnerId()).thenReturn(1L);
        when(stockTransActionDTO.getStockTransActionId()).thenReturn(1L);
        when(stockTransDetailService.findByStockTransId(any())).thenReturn(lstStockTransDetailDTOs);
        when(stockTotalService.getStockTotalForProcessStock(1L, stockTrans.getFromOwnerType(), stockTransDetailDTO.getProdOfferId(), stockTransDetailDTO.getStateId())).thenReturn(stockTotalDTO);
        logisticBaseService.cancelOrderOrBill(orderObjectDTO);
    }

    @Test
    public void testCancelOrderOrBill_14() throws  Exception {
        OrderObjectDTO orderObjectDTO = new OrderObjectDTO();
        StockTrans stockTrans = new StockTrans();
        StockTransDTO stockTransRegionDto = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = mock(StockTransActionDTO.class);
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> lstStockTransDetailDTOs = Lists.newArrayList(stockTransDetailDTO);
        StockTotalDTO stockTotalDTO = new StockTotalDTO();
        stockTrans.setStatus("1");
        stockTrans.setPayStatus("2");
        stockTrans.setDepositStatus("0");
        stockTrans.setRegionStockTransId(1L);
        orderObjectDTO.setOrderCode("1");
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        when(stockTransRepo.findOne(anyLong())).thenReturn(stockTrans);
        Mockito.doNothing().when(em).lock(stockTrans, LockModeType.PESSIMISTIC_READ);
        when(stockTransService.findOne(stockTrans.getRegionStockTransId())).thenReturn(stockTransRegionDto);
        when(stockTransActionService.getStockTransActionByIdAndStatus(stockTrans.getRegionStockTransId(), Lists.newArrayList(Const.STOCK_TRANS_STATUS.EXPORT_ORDER))).thenReturn(stockTransActionDTO) ;
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(), any())).thenReturn(stockTransActionDTO);
        when(stockTransActionDTO.getRegionOwnerId()).thenReturn(1L);
        when(stockTransActionDTO.getStockTransActionId()).thenReturn(1L);
        when(stockTransDetailService.findByStockTransId(any())).thenReturn(null);
        when(stockTotalService.getStockTotalForProcessStock(1L, stockTrans.getFromOwnerType(), stockTransDetailDTO.getProdOfferId(), stockTransDetailDTO.getStateId())).thenReturn(stockTotalDTO);
        logisticBaseService.cancelOrderOrBill(orderObjectDTO);
    }

    @Test
    public void testCancelOrderOrBill_15() throws  Exception {
        OrderObjectDTO orderObjectDTO = new OrderObjectDTO();
        StockTrans stockTrans = new StockTrans();
        StockTransDTO stockTransRegionDto = new StockTransDTO();
        StockTransActionDTO stockTransActionRegion = mock(StockTransActionDTO.class);
        stockTrans.setStatus("1");
        stockTrans.setPayStatus("2");
        stockTrans.setDepositStatus("0");
        stockTrans.setRegionStockTransId(1L);
        orderObjectDTO.setOrderCode("1");
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        when(stockTransRepo.findOne(anyLong())).thenReturn(stockTrans);
        Mockito.doNothing().when(em).lock(stockTrans, LockModeType.PESSIMISTIC_READ);
        when(stockTransService.findOne(stockTrans.getRegionStockTransId())).thenReturn(null);
        when(stockTransActionService.getStockTransActionByIdAndStatus(stockTrans.getRegionStockTransId(), Lists.newArrayList(Const.STOCK_TRANS_STATUS.EXPORT_ORDER))).thenReturn(stockTransActionRegion) ;
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(), any())).thenReturn(stockTransActionRegion);
        when(stockTransActionRegion.getRegionOwnerId()).thenReturn(1L);
        when(stockTransActionRegion.getStockTransActionId()).thenReturn(null);
        logisticBaseService.cancelOrderOrBill(orderObjectDTO);
    }

    @Test
    public void testCancelOrderOrBill_16() throws  Exception {
        OrderObjectDTO orderObjectDTO = new OrderObjectDTO();
        StockTrans stockTrans = new StockTrans();
        StockTransDTO stockTransRegionDto = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = mock(StockTransActionDTO.class);
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> lstStockTransDetailDTOs = Lists.newArrayList(stockTransDetailDTO);
        StockTotalDTO stockTotalDTO = new StockTotalDTO();
        stockTrans.setStatus("1");
        stockTrans.setPayStatus("2");
        stockTrans.setDepositStatus("0");
        stockTrans.setRegionStockTransId(1L);
        orderObjectDTO.setOrderCode("1");
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        when(stockTransRepo.findOne(anyLong())).thenReturn(stockTrans);
        Mockito.doNothing().when(em).lock(stockTrans, LockModeType.PESSIMISTIC_READ);
        when(stockTransService.findOne(stockTrans.getRegionStockTransId())).thenReturn(stockTransRegionDto);
        when(stockTransActionService.getStockTransActionByIdAndStatus(stockTrans.getRegionStockTransId(), Lists.newArrayList(Const.STOCK_TRANS_STATUS.EXPORT_ORDER))).thenReturn(stockTransActionDTO) ;
        when(stockTransActionService.getStockTransActionByIdAndStatus(any(), any())).thenReturn(stockTransActionDTO);
        when(stockTransActionDTO.getRegionOwnerId()).thenReturn(1L);
        when(stockTransActionDTO.getStockTransActionId()).thenReturn(1L);
        when(stockTransDetailService.findByStockTransId(any())).thenReturn(lstStockTransDetailDTOs);
        when(stockTotalService.getStockTotalForProcessStock(1L, stockTrans.getFromOwnerType(), stockTransDetailDTO.getProdOfferId(), stockTransDetailDTO.getStateId())).thenReturn(null);
        logisticBaseService.cancelOrderOrBill(orderObjectDTO);
    }
    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method LogisticBaseServiceImpl.createRegionReceiveNote
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public  void testCreateRegionReceiveNote_1() throws  Exception {
        StockTrans stockTrans = new StockTrans();
        Date createDate = new Date();
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        Whitebox.invokeMethod(spyService, "createRegionReceiveNote", stockTrans, createDate);
    }

    @Test
    public  void testCreateRegionReceiveNote_2() throws  Exception {
        StockTrans stockTrans = new StockTrans();
        Date createDate = new Date();
        StockTransDTO expStockTransDTO = new StockTransDTO();
        ShopDTO shopDTO = mock(ShopDTO.class);
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        PowerMockito.mockStatic(DbUtil.class);
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        when(stockTransService.findOne(stockTrans.getRegionStockTransId())).thenReturn(expStockTransDTO);
        when(spyService.getSequence(Mockito.any(EntityManager.class), anyString())).thenReturn(1L);
        when(shopService.findOne(expStockTransDTO.getFromOwnerId())).thenReturn(shopDTO);
        when(shopDTO.getShopCode()).thenReturn("code");
        Whitebox.invokeMethod(spyService, "createRegionReceiveNote", stockTrans, createDate);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method LogisticBaseServiceImpl.normalActionCodeShop
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public  void testNormalActionCodeShop_1() throws Exception {
        String actionCodeShop = "1";
        Long length = 2L;
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        Whitebox.invokeMethod(spyService, "normalActionCodeShop", actionCodeShop, length);
    }

    @Test
    public  void testNormalActionCodeShop_2() throws Exception {
        String actionCodeShop = "";
        Long length = 2L;
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        Whitebox.invokeMethod(spyService, "normalActionCodeShop", actionCodeShop, length);
    }

    @Test
    public  void testNormalActionCodeShop_3() throws Exception {
        String actionCodeShop = null;
        Long length = 2L;
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        Whitebox.invokeMethod(spyService, "normalActionCodeShop", actionCodeShop, length);
    }
    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method LogisticBaseServiceImpl.validateStockModelQuantity
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public  void testValidateStockModelQuantity_1() throws Exception {
        GoodsDTO goodsDTO = new GoodsDTO();
        goodsDTO.setAmountOrder("amount");
        goodsDTO.setGoodsCode("goodsCd");
        Long ownerId=1L;
        Long ownerType=2L;
        List<GoodsDTO> lstGoods= Lists.newArrayList();
        Long status = 3L;
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        Whitebox.invokeMethod(spyService, "validateStockModelQuantity", ownerId, ownerType, lstGoods, status);
    }

    @Test
    public  void testValidateStockModelQuantity_2() throws Exception {
        GoodsDTO goodsDTO = new GoodsDTO();
        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        Serial serial = new Serial();
        serial.setQuantity("1");
        List<Serial> lstSerial = Lists.newArrayList(serial);
        goodsDTO.setAmountOrder("amount");
        goodsDTO.setGoodsCode("goodsCd");
        goodsDTO.setLstSerial(lstSerial);
        Long ownerId=1L;
        Long ownerType=2L;
        List<GoodsDTO> lstGoods= Lists.newArrayList(goodsDTO);
        Long status = 3L;
        when(productOfferingService.findByProductOfferCode(goodsDTO.getGoodsCode(), Const.STATUS_ACTIVE)).thenReturn(null);
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        when(stockHandsetService.getQuantityStockX(any(), any(), any(), any(), anyLong(), anyLong())).thenReturn(2L);
        Whitebox.invokeMethod(spyService, "validateStockModelQuantity", ownerId, ownerType, lstGoods, status);
    }

    @Test
    public  void testValidateStockModelQuantity_3() throws Exception {
        GoodsDTO goodsDTO = new GoodsDTO();
        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        Serial serial = new Serial();
        serial.setQuantity("1");
        List<Serial> lstSerial = Lists.newArrayList(serial);
        goodsDTO.setAmountOrder("amount");
        goodsDTO.setGoodsCode("goodsCd");
        goodsDTO.setLstSerial(lstSerial);
        Long ownerId=1L;
        Long ownerType=2L;
        List<GoodsDTO> lstGoods= Lists.newArrayList(goodsDTO);
        Long status = 3L;
        when(productOfferingService.findByProductOfferCode(goodsDTO.getGoodsCode(), Const.STATUS_ACTIVE)).thenReturn(productOfferingDTO);
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        Whitebox.invokeMethod(spyService, "validateStockModelQuantity", ownerId, ownerType, lstGoods, status);
    }

    @Test(expected = Exception.class)
    public  void testValidateStockModelQuantity_4() throws Exception {
        GoodsDTO goodsDTO = new GoodsDTO();
        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        Serial serial = new Serial();
        serial.setQuantity("cd");
        List<Serial> lstSerial = Lists.newArrayList(serial);
        goodsDTO.setAmountOrder("amount");
        goodsDTO.setGoodsCode("goodsCd");
        goodsDTO.setLstSerial(lstSerial);
        Long ownerId=1L;
        Long ownerType=2L;
        List<GoodsDTO> lstGoods= Lists.newArrayList(goodsDTO);
        Long status = 3L;
        when(productOfferingService.findByProductOfferCode(goodsDTO.getGoodsCode(), Const.STATUS_ACTIVE)).thenReturn(productOfferingDTO);
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        Whitebox.invokeMethod(spyService, "validateStockModelQuantity", ownerId, ownerType, lstGoods, status);
    }

    @Test
    public  void testValidateStockModelQuantity_5() throws Exception {
        GoodsDTO goodsDTO = new GoodsDTO();
        goodsDTO.setAmountOrder("amount");
        goodsDTO.setGoodsCode("goodsCd");
        Long ownerId=1L;
        Long ownerType=2L;
        List<GoodsDTO> lstGoods= null;
        Long status = 3L;
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        Whitebox.invokeMethod(spyService, "validateStockModelQuantity", ownerId, ownerType, lstGoods, status);
    }

    @Test
    public  void testValidateStockModelQuantity_6() throws Exception {
        GoodsDTO goodsDTO = new GoodsDTO();
        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        Serial serial = new Serial();
        serial.setQuantity("1");
        List<Serial> lstSerial = Lists.newArrayList();
        goodsDTO.setAmountOrder("amount");
        goodsDTO.setGoodsCode("goodsCd");
        goodsDTO.setLstSerial(lstSerial);
        Long ownerId=1L;
        Long ownerType=2L;
        List<GoodsDTO> lstGoods= Lists.newArrayList(goodsDTO);
        Long status = 3L;
        when(productOfferingService.findByProductOfferCode(goodsDTO.getGoodsCode(), Const.STATUS_ACTIVE)).thenReturn(productOfferingDTO);
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        Whitebox.invokeMethod(spyService, "validateStockModelQuantity", ownerId, ownerType, lstGoods, status);
    }

    @Test
    public  void testValidateStockModelQuantity_7() throws Exception {
        GoodsDTO goodsDTO = new GoodsDTO();
        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        Serial serial = new Serial();
        serial.setQuantity("1");
        List<Serial> lstSerial = Lists.newArrayList(serial);
        goodsDTO.setAmountOrder("amount");
        goodsDTO.setGoodsCode("goodsCd");
        goodsDTO.setLstSerial(lstSerial);
        Long ownerId=1L;
        Long ownerType=2L;
        List<GoodsDTO> lstGoods= Lists.newArrayList(goodsDTO);
        Long status = 3L;
        when(productOfferingService.findByProductOfferCode(goodsDTO.getGoodsCode(), Const.STATUS_ACTIVE)).thenReturn(productOfferingDTO);
        when(stockHandsetService.getQuantityStockX(any(), any(), any(), any(), any(), any())).thenReturn(0L);
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        Whitebox.invokeMethod(spyService, "validateStockModelQuantity", ownerId, ownerType, lstGoods, status);
    }
    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method LogisticBaseServiceImpl.validateStockTotalQuantity
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public  void testValidateStockTotalQuantity_1() throws Exception {
        GoodsDTO goodsDTO = new GoodsDTO();
        goodsDTO.setAmountOrder("amount1");
        goodsDTO.setGoodsCode("goodsCd");
        Long ownerId=1L;
        Long ownerType=2L;
        List<GoodsDTO> lstGoods= Lists.newArrayList();
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        Whitebox.invokeMethod(spyService, "validateStockTotalQuantity", ownerId, ownerType, lstGoods);
    }

    @Test
    public  void testValidateStockTotalQuantity_2() throws Exception {
        GoodsDTO goodsDTO = new GoodsDTO();
        Serial serial = new Serial();
        serial.setQuantity("1");
        List<Serial> lstSerial = Lists.newArrayList(serial);
        goodsDTO.setAmountOrder("amount2");
        goodsDTO.setGoodsCode("goodsCd");
        goodsDTO.setLstSerial(lstSerial);
        Long ownerId=1L;
        Long ownerType=2L;
        List<GoodsDTO> lstGoods= Lists.newArrayList(goodsDTO);
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        when(productOfferingService.findByProductOfferCode(goodsDTO.getGoodsCode(), Const.STATUS_ACTIVE)).thenReturn(null);
        Whitebox.invokeMethod(spyService, "validateStockTotalQuantity", ownerId, ownerType, lstGoods);
    }

    @Test(expected = Exception.class)
    public  void testValidateStockTotalQuantity_3() throws Exception {
        GoodsDTO goodsDTO = new GoodsDTO();
        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        Serial serial = new Serial();
        serial.setQuantity("1");
        List<Serial> lstSerial = Lists.newArrayList(serial);
        goodsDTO.setAmountOrder("amount2");
        goodsDTO.setGoodsCode("goodsCd");
        goodsDTO.setLstSerial(lstSerial);
        goodsDTO.setGoodsState("2");
        Long ownerId=1L;
        Long ownerType=2L;
        List<GoodsDTO> lstGoods= Lists.newArrayList(goodsDTO);
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        when(productOfferingService.findByProductOfferCode(goodsDTO.getGoodsCode(), Const.STATUS_ACTIVE)).thenReturn(productOfferingDTO);
        Whitebox.invokeMethod(spyService, "validateStockTotalQuantity", ownerId, ownerType, lstGoods);
    }

    @Test
    public  void testValidateStockTotalQuantity_4() throws Exception {
        GoodsDTO goodsDTO = new GoodsDTO();
        StockTotalDTO stockTotalDTO = mock(StockTotalDTO.class);
        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        Serial serial = new Serial();
        serial.setQuantity("1");
        List<Serial> lstSerial = Lists.newArrayList(serial);
        goodsDTO.setAmountOrder("amount2");
        goodsDTO.setGoodsCode("goodsCd");
        goodsDTO.setLstSerial(lstSerial);
        goodsDTO.setAmountReal("2");
        Long ownerId=1L;
        Long ownerType=2L;
        List<GoodsDTO> lstGoods= Lists.newArrayList(goodsDTO);
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        when(productOfferingService.findByProductOfferCode(goodsDTO.getGoodsCode(), Const.STATUS_ACTIVE)).thenReturn(productOfferingDTO);
        when(stockTotalService.getStockTotalForProcessStock(anyLong(), anyLong(), anyLong(), anyLong())).thenReturn(stockTotalDTO);
        when(stockTotalDTO.getCurrentQuantity()).thenReturn(1L);
        Whitebox.invokeMethod(spyService, "validateStockTotalQuantity", ownerId, ownerType, lstGoods);
    }

    @Test
    public  void testValidateStockTotalQuantity_5() throws Exception {
        GoodsDTO goodsDTO = new GoodsDTO();
        goodsDTO.setAmountOrder("amount1");
        goodsDTO.setGoodsCode("goodsCd");
        Long ownerId=1L;
        Long ownerType=2L;
        List<GoodsDTO> lstGoods= null;
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        Whitebox.invokeMethod(spyService, "validateStockTotalQuantity", ownerId, ownerType, lstGoods);
    }

    @Test
    public  void testValidateStockTotalQuantity_6() throws Exception {
        GoodsDTO goodsDTO = new GoodsDTO();
        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        Serial serial = new Serial();
        serial.setQuantity("1");
        List<Serial> lstSerial = Lists.newArrayList(serial);
        goodsDTO.setAmountOrder("amount2");
        goodsDTO.setGoodsCode("goodsCd");
        goodsDTO.setLstSerial(lstSerial);
        goodsDTO.setGoodsState("2");
        goodsDTO.setAmountReal("3");
        Long ownerId=1L;
        Long ownerType=2L;
        List<GoodsDTO> lstGoods= Lists.newArrayList(goodsDTO);
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        when(productOfferingService.findByProductOfferCode(goodsDTO.getGoodsCode(), Const.STATUS_ACTIVE)).thenReturn(productOfferingDTO);
        when(stockTotalService.getStockTotalForProcessStock(any(), any(), any(), any())).thenReturn(null);
        Whitebox.invokeMethod(spyService, "validateStockTotalQuantity", ownerId, ownerType, lstGoods);
    }

    @Test
    public  void testValidateStockTotalQuantity_7() throws Exception {
        GoodsDTO goodsDTO = new GoodsDTO();
        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        Serial serial = new Serial();
        serial.setQuantity("1");
        List<Serial> lstSerial = Lists.newArrayList(serial);
        goodsDTO.setAmountOrder("amount2");
        goodsDTO.setGoodsCode("goodsCd");
        goodsDTO.setLstSerial(lstSerial);
        goodsDTO.setGoodsState("2");
        goodsDTO.setAmountReal("0");
        Long ownerId=1L;
        Long ownerType=2L;
        List<GoodsDTO> lstGoods= Lists.newArrayList(goodsDTO);
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        when(productOfferingService.findByProductOfferCode(goodsDTO.getGoodsCode(), Const.STATUS_ACTIVE)).thenReturn(productOfferingDTO);
        when(stockTotalService.getStockTotalForProcessStock(any(), any(), any(), any())).thenReturn(null);
        Whitebox.invokeMethod(spyService, "validateStockTotalQuantity", ownerId, ownerType, lstGoods);
    }
    /** ------------------------------------------------------------------------------ **/
    /** ------ Test for LogisticBaseServiceImpl.validateStockTotalGeneral method ------- **/
    /*** -----------------------------------------------------------------------------**/
    @Test
    public  void testValidateStockTotalGeneral_1() throws Exception {
        Long ownerId = 1L;
        Long ownerType = 2L;
        BillStockDTO billStockDTO = new BillStockDTO();
        ResultLogisticsDTO resultBOStockModelQuantity = mock(ResultLogisticsDTO.class);
        billStockDTO.setOrderType("1");
        billStockDTO.setInputType("2");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        PowerMockito.doReturn(resultBOStockModelQuantity).when(spyService,"validateStockModelQuantity", any(), any(), any(), any());
        when(resultBOStockModelQuantity.getResponseCode()).thenReturn("0");
        Whitebox.invokeMethod(spyService, "validateStockTotalGeneral", ownerId, ownerType, billStockDTO);
    }

    @Test
    public  void testValidateStockTotalGeneral_2() throws Exception {
        Long ownerId = 1L;
        Long ownerType = 2L;
        BillStockDTO billStockDTO = new BillStockDTO();
        ResultLogisticsDTO resultBOStockTotalQuantity = mock(ResultLogisticsDTO.class);
        billStockDTO.setOrderType("2");
        billStockDTO.setInputType("2");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        PowerMockito.doReturn(resultBOStockTotalQuantity).when(spyService,"validateStockTotalQuantity", any(), any(), any());
        when(resultBOStockTotalQuantity.getResponseCode()).thenReturn("0");
        Whitebox.invokeMethod(spyService, "validateStockTotalGeneral", ownerId, ownerType, billStockDTO);
    }

    @Test
    public  void testValidateStockTotalGeneral_3() throws Exception {
        Long ownerId = 1L;
        Long ownerType = 2L;
        BillStockDTO billStockDTO = new BillStockDTO();
        ResultLogisticsDTO resultBOStockModelQuantity = mock(ResultLogisticsDTO.class);
        billStockDTO.setOrderType("1");
        billStockDTO.setInputType("1");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        PowerMockito.doReturn(resultBOStockModelQuantity).when(spyService,"validateStockModelQuantity", any(), any(), any(), any());
        when(resultBOStockModelQuantity.getResponseCode()).thenReturn("0");
        Whitebox.invokeMethod(spyService, "validateStockTotalGeneral", ownerId, ownerType, billStockDTO);
    }

    @Test
    public  void testValidateStockTotalGeneral_4() throws Exception {
        Long ownerId = 1L;
        Long ownerType = 2L;
        BillStockDTO billStockDTO = new BillStockDTO();
        ResultLogisticsDTO resultBOStockModelQuantity = mock(ResultLogisticsDTO.class);
        billStockDTO.setOrderType("2");
        billStockDTO.setInputType("1");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        PowerMockito.doReturn(null).when(spyService,"validateStockTotalQuantity", any(), any(), any());
        PowerMockito.doReturn(resultBOStockModelQuantity).when(spyService,"validateStockModelQuantity", any(), any(), any(), any());
        when(resultBOStockModelQuantity.getResponseCode()).thenReturn("0");
        Whitebox.invokeMethod(spyService, "validateStockTotalGeneral", ownerId, ownerType, billStockDTO);
    }

    @Test
    public  void testValidateStockTotalGeneral_5() throws Exception {
        Long ownerId = 1L;
        Long ownerType = 2L;
        BillStockDTO billStockDTO = new BillStockDTO();
        billStockDTO.setOrderType("1");
        billStockDTO.setInputType("1");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        Whitebox.invokeMethod(spyService, "validateStockTotalGeneral", ownerId, ownerType, billStockDTO);
    }

    @Test
    public  void testValidateStockTotalGeneral_6() throws Exception {
        Long ownerId = 1L;
        Long ownerType = 2L;
        BillStockDTO billStockDTO = new BillStockDTO();
        ResultLogisticsDTO resultBOStockModelQuantity = mock(ResultLogisticsDTO.class);
        billStockDTO.setOrderType("1");
        billStockDTO.setInputType("2");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        PowerMockito.doReturn(null).when(spyService,"validateStockModelQuantity", any(), any(), any(), any());
        when(resultBOStockModelQuantity.getResponseCode()).thenReturn("0");
        Whitebox.invokeMethod(spyService, "validateStockTotalGeneral", ownerId, ownerType, billStockDTO);
    }

    @Test
    public  void testValidateStockTotalGeneral_7() throws Exception {
        Long ownerId = 1L;
        Long ownerType = 2L;
        BillStockDTO billStockDTO = new BillStockDTO();
        ResultLogisticsDTO resultBOStockModelQuantity = mock(ResultLogisticsDTO.class);
        billStockDTO.setOrderType("1");
        billStockDTO.setInputType("2");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        PowerMockito.doReturn(resultBOStockModelQuantity).when(spyService,"validateStockModelQuantity", any(), any(), any(), any());
        when(resultBOStockModelQuantity.getResponseCode()).thenReturn("");
        Whitebox.invokeMethod(spyService, "validateStockTotalGeneral", ownerId, ownerType, billStockDTO);
    }

    @Test
    public  void testValidateStockTotalGeneral_8() throws Exception {
        Long ownerId = 1L;
        Long ownerType = 2L;
        BillStockDTO billStockDTO = new BillStockDTO();
        ResultLogisticsDTO resultBOStockModelQuantity = mock(ResultLogisticsDTO.class);
        billStockDTO.setOrderType("1");
        billStockDTO.setInputType("2");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        PowerMockito.doReturn(resultBOStockModelQuantity).when(spyService,"validateStockModelQuantity", any(), any(), any(), any());
        when(resultBOStockModelQuantity.getResponseCode()).thenReturn("1");
        Whitebox.invokeMethod(spyService, "validateStockTotalGeneral", ownerId, ownerType, billStockDTO);
    }

    @Test
    public  void testValidateStockTotalGeneral_9() throws Exception {
        Long ownerId = 1L;
        Long ownerType = 2L;
        BillStockDTO billStockDTO = new BillStockDTO();
        ResultLogisticsDTO resultBOStockModelQuantity = mock(ResultLogisticsDTO.class);
        billStockDTO.setOrderType("2");
        billStockDTO.setInputType("1");
        ResultLogisticsDTO resultBOStockTotalQuantity = mock(ResultLogisticsDTO.class);
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        PowerMockito.doReturn(resultBOStockTotalQuantity).when(spyService,"validateStockTotalQuantity", any(), any(), any());
        PowerMockito.doReturn(resultBOStockModelQuantity).when(spyService,"validateStockModelQuantity", any(), any(), any(), any());
        when(resultBOStockTotalQuantity.getResponseCode()).thenReturn(null);
        when(resultBOStockModelQuantity.getResponseCode()).thenReturn(null);
        Whitebox.invokeMethod(spyService, "validateStockTotalGeneral", ownerId, ownerType, billStockDTO);
    }

    @Test
    public  void testValidateStockTotalGeneral_10() throws Exception {
        Long ownerId = 1L;
        Long ownerType = 2L;
        BillStockDTO billStockDTO = new BillStockDTO();
        ResultLogisticsDTO resultBOStockModelQuantity = mock(ResultLogisticsDTO.class);
        billStockDTO.setOrderType("2");
        billStockDTO.setInputType("1");
        ResultLogisticsDTO resultBOStockTotalQuantity = mock(ResultLogisticsDTO.class);
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        PowerMockito.doReturn(resultBOStockTotalQuantity).when(spyService,"validateStockTotalQuantity", any(), any(), any());
        PowerMockito.doReturn(resultBOStockModelQuantity).when(spyService,"validateStockModelQuantity", any(), any(), any(), any());
        when(resultBOStockTotalQuantity.getResponseCode()).thenReturn("1");
        when(resultBOStockModelQuantity.getResponseCode()).thenReturn("1");
        Whitebox.invokeMethod(spyService, "validateStockTotalGeneral", ownerId, ownerType, billStockDTO);
    }

    @Test
    public  void testValidateStockTotalGeneral_11() throws Exception {
        Long ownerId = 1L;
        Long ownerType = 2L;
        BillStockDTO billStockDTO = new BillStockDTO();
        ResultLogisticsDTO resultBOStockModelQuantity = mock(ResultLogisticsDTO.class);
        billStockDTO.setOrderType("2");
        billStockDTO.setInputType("1");
        ResultLogisticsDTO resultBOStockTotalQuantity = mock(ResultLogisticsDTO.class);
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        PowerMockito.doReturn(resultBOStockTotalQuantity).when(spyService,"validateStockTotalQuantity", any(), any(), any());
        PowerMockito.doReturn(null).when(spyService,"validateStockModelQuantity", any(), any(), any(), any());
        when(resultBOStockTotalQuantity.getResponseCode()).thenReturn("1");
        Whitebox.invokeMethod(spyService, "validateStockTotalGeneral", ownerId, ownerType, billStockDTO);
    }
    /** ------------------------------------------------------------------------------ **/
    /** ------ Test for LogisticBaseServiceImpl.validateLstGoods method ------- **/
    /*** -----------------------------------------------------------------------------**/
    @Test
    public  void testValidateLstGoods_1() throws Exception {
        GoodsDTO goodsDTO = new GoodsDTO();
        String orderCode = "";
        List<GoodsDTO> lstGoods = Lists.newArrayList(goodsDTO);
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        Whitebox.invokeMethod(spyService, "validateLstGoods", orderCode, lstGoods);
    }

    @Test
    public  void testValidateLstGoods_2() throws Exception {
        GoodsDTO goodsDTO = new GoodsDTO();
        String orderCode = "cd";
        List<GoodsDTO> lstGoods = Lists.newArrayList(goodsDTO);
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        Whitebox.invokeMethod(spyService, "validateLstGoods", orderCode, lstGoods);
    }

    @Test
    public  void testValidateLstGoods_3() throws Exception {
        GoodsDTO goodsDTO = new GoodsDTO();
        String orderCode = "1";
        List<GoodsDTO> lstGoods = Lists.newArrayList(goodsDTO);
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        Whitebox.invokeMethod(spyService, "validateLstGoods", orderCode, lstGoods);
    }

    @Test
    public  void testValidateLstGoods_4() throws Exception {
        GoodsDTO goodsDTO = new GoodsDTO();
        StockTrans stockTrans = new StockTrans();
        String orderCode = "1";
        List<GoodsDTO> lstGoods = Lists.newArrayList(goodsDTO);
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        when(stockTransRepo.findOne(anyLong())).thenReturn(stockTrans);
        Whitebox.invokeMethod(spyService, "validateLstGoods", orderCode, lstGoods);
    }

    @Test
    public  void testValidateLstGoods_5() throws Exception {
        GoodsDTO goodsDTO = new GoodsDTO();
        StockTrans stockTrans = new StockTrans();
        ResultLogisticsDTO resultGoodCode = mock(ResultLogisticsDTO.class);
        String orderCode = "1";
        List<GoodsDTO> lstGoods = Lists.newArrayList(goodsDTO);
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        when(stockTransRepo.findOne(anyLong())).thenReturn(stockTrans);
        when(stockTransDetailService.findByStockTransId(any())).thenReturn(null);
        PowerMockito.doReturn(null).when(spyService,"validateGoodsCode", any(), any());
        when(resultGoodCode.getResponseCode()).thenReturn("0");
        Whitebox.invokeMethod(spyService, "validateLstGoods", orderCode, lstGoods);
    }

    @Test
    public  void testValidateLstGoods_6() throws Exception {
        GoodsDTO goodsDTO = new GoodsDTO();
        StockTrans stockTrans = new StockTrans();
        ResultLogisticsDTO resultGoodCode = mock(ResultLogisticsDTO.class);
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> lstStockTransDetailDTOs = Lists.newArrayList(stockTransDetailDTO);
        String orderCode = "1";
        List<GoodsDTO> lstGoods = Lists.newArrayList();
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        when(stockTransRepo.findOne(anyLong())).thenReturn(stockTrans);
        PowerMockito.doReturn(null).when(spyService,"validateGoodsCode", any(), any());
        when(resultGoodCode.getResponseCode()).thenReturn("0");
        when(stockTransDetailService.findByStockTransId(any())).thenReturn(lstStockTransDetailDTOs);
        Whitebox.invokeMethod(spyService, "validateLstGoods", orderCode, lstGoods);
    }

    @Test(expected = Exception.class)
    public  void testValidateLstGoods_7() throws Exception {
        GoodsDTO goodsDTO = new GoodsDTO();
        StockTrans stockTrans = new StockTrans();
        ResultLogisticsDTO resultGoodCode = mock(ResultLogisticsDTO.class);
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> lstStockTransDetailDTOs = Lists.newArrayList(stockTransDetailDTO);
        String orderCode = "1";
        List<GoodsDTO> lstGoods = Lists.newArrayList(goodsDTO);
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        when(stockTransRepo.findOne(anyLong())).thenReturn(stockTrans);
        PowerMockito.doReturn(null).when(spyService,"validateGoodsCode", any(), any());
        when(resultGoodCode.getResponseCode()).thenReturn("0");
        when(stockTransDetailService.findByStockTransId(any())).thenReturn(lstStockTransDetailDTOs);
        Whitebox.invokeMethod(spyService, "validateLstGoods", orderCode, lstGoods);
    }

    // Note : K lam dc testcase cho line 665

    @Test
    public  void testValidateLstGoods_8() throws Exception {
        GoodsDTO goodsDTO = new GoodsDTO();
        Serial serial = new Serial();
        serial.setFromSerial("3");
        serial.setToSerial("4");
        serial.setQuantity("5");
        List<Serial> serialList = Lists.newArrayList(serial);
        goodsDTO.setGoodsCode("1");
        goodsDTO.setAmountOrder("1");
        goodsDTO.setAmountReal("1");
        goodsDTO.setGoodsState("2");
        goodsDTO.setLstSerial(serialList);
        goodsDTO.setStockModelId(1L);

        StockTrans stockTrans = new StockTrans();
        ResultLogisticsDTO resultGoodCode = mock(ResultLogisticsDTO.class);
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId(1L); //lstStockModelIdBCCS
        stockTransDetailDTO.setStateId(3L);
        stockTransDetailDTO.setQuantity(2L);
        ProductOfferingDTO productOfferingDTO = mock(ProductOfferingDTO.class);
        List<StockTransDetailDTO> lstStockTransDetailDTOs = Lists.newArrayList(stockTransDetailDTO);
        String orderCode = "1";
        List<GoodsDTO> lstGoods = Lists.newArrayList(goodsDTO);
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        when(stockTransRepo.findOne(anyLong())).thenReturn(stockTrans);

        when(productOfferingService.findByProductOfferCode(goodsDTO.getGoodsCode(), Const.STATUS_ACTIVE)).thenReturn(productOfferingDTO);
        when(productOfferingDTO.getCheckSerial()).thenReturn(2L);
        when(productOfferingDTO.getProductOfferingId()).thenReturn(1L); //lstStockModelIdLogistics

        when(resultGoodCode.getResponseCode()).thenReturn("1");
        when(stockTransDetailService.findByStockTransId(any())).thenReturn(lstStockTransDetailDTOs);
        Whitebox.invokeMethod(spyService, "validateLstGoods", orderCode, lstGoods);
    }

    @Test(expected = Exception.class)
    public  void testValidateLstGoods_9() throws Exception {
        GoodsDTO goodsDTO = new GoodsDTO();
        StockTrans stockTrans = new StockTrans();
        ResultLogisticsDTO resultGoodCode = mock(ResultLogisticsDTO.class);
        String orderCode = "1";
        List<GoodsDTO> lstGoods = Lists.newArrayList(goodsDTO);
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        when(stockTransRepo.findOne(anyLong())).thenReturn(stockTrans);
        PowerMockito.doReturn(null).when(spyService,"validateGoodsCode", any(), any());
        when(resultGoodCode.getResponseCode()).thenReturn("0");
        when(stockTransDetailService.findByStockTransId(any())).thenThrow(new Exception());
        Whitebox.invokeMethod(spyService, "validateLstGoods", orderCode, lstGoods);
    }

    @Test
    public  void testValidateLstGoods_10() throws Exception {
        GoodsDTO goodsDTO = new GoodsDTO();
        StockTrans stockTrans = new StockTrans();
        ResultLogisticsDTO resultGoodCode = mock(ResultLogisticsDTO.class);
        String orderCode = "1";
        List<GoodsDTO> lstGoods = Lists.newArrayList(goodsDTO);

        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        when(stockTransRepo.findOne(anyLong())).thenReturn(stockTrans);
        when(stockTransDetailService.findByStockTransId(any())).thenReturn(null);
        PowerMockito.doReturn(resultGoodCode).when(spyService,"validateGoodsCode", any(), any());
        when(resultGoodCode.getResponseCode()).thenReturn(null);
        Whitebox.invokeMethod(spyService, "validateLstGoods", orderCode, lstGoods);
    }

    @Test
    public  void testValidateLstGoods_11() throws Exception {
        GoodsDTO goodsDTO = new GoodsDTO();
        Serial serial = new Serial();
        serial.setFromSerial("3");
        serial.setToSerial("4");
        serial.setQuantity("5");
        List<Serial> serialList = Lists.newArrayList(serial);
        goodsDTO.setGoodsCode("1");
        goodsDTO.setAmountOrder("1");
        goodsDTO.setAmountReal("1");
        goodsDTO.setGoodsState("2");
        goodsDTO.setLstSerial(serialList);
        goodsDTO.setStockModelId(1L);

        StockTrans stockTrans = new StockTrans();
        ResultLogisticsDTO resultGoodCode = mock(ResultLogisticsDTO.class);
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId(1L); //lstStockModelIdBCCS
        stockTransDetailDTO.setStateId(3L);
        stockTransDetailDTO.setQuantity(1L);
        ProductOfferingDTO productOfferingDTO = mock(ProductOfferingDTO.class);
        List<StockTransDetailDTO> lstStockTransDetailDTOs = Lists.newArrayList(stockTransDetailDTO);
        String orderCode = "1";
        List<GoodsDTO> lstGoods = Lists.newArrayList(goodsDTO);
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        when(stockTransRepo.findOne(anyLong())).thenReturn(stockTrans);

        when(productOfferingService.findByProductOfferCode(goodsDTO.getGoodsCode(), Const.STATUS_ACTIVE)).thenReturn(productOfferingDTO);
        when(productOfferingDTO.getCheckSerial()).thenReturn(2L);
        when(productOfferingDTO.getProductOfferingId()).thenReturn(1L); //lstStockModelIdLogistics

        when(resultGoodCode.getResponseCode()).thenReturn("1");
        when(stockTransDetailService.findByStockTransId(any())).thenReturn(lstStockTransDetailDTOs);
        Whitebox.invokeMethod(spyService, "validateLstGoods", orderCode, lstGoods);
    }
    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method LogisticBaseServiceImpl.validateGoodsCode
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testValidateGoodsCode_1() throws Exception {
        GoodsDTO goodsDTO = new GoodsDTO();
        goodsDTO.setGoodsCode("1");
        goodsDTO.setAmountOrder("0");
        goodsDTO.setAmountReal("1");

        List<GoodsDTO> lstGoods = Lists.newArrayList(goodsDTO);
        List<Long> lstStockModelIdLogistics = Lists.newArrayList();
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        Whitebox.invokeMethod(spyService, "validateGoodsCode", lstGoods, lstStockModelIdLogistics);
    }

    @Test
    public void testValidateGoodsCode_2() throws Exception {
        GoodsDTO goodsDTO = new GoodsDTO();
        goodsDTO.setGoodsCode("1");
        goodsDTO.setAmountOrder("1");
        goodsDTO.setAmountReal("1");

        List<GoodsDTO> lstGoods = Lists.newArrayList(goodsDTO);
        List<Long> lstStockModelIdLogistics = Lists.newArrayList();
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        Whitebox.invokeMethod(spyService, "validateGoodsCode", lstGoods, lstStockModelIdLogistics);
    }

    @Test
    public void testValidateGoodsCode_3() throws Exception {
        GoodsDTO goodsDTO = new GoodsDTO();
        goodsDTO.setGoodsCode("1");
        goodsDTO.setAmountOrder("1");
        goodsDTO.setAmountReal("1");
        goodsDTO.setGoodsState("0");
        List<GoodsDTO> lstGoods = Lists.newArrayList(goodsDTO);
        List<Long> lstStockModelIdLogistics = Lists.newArrayList();
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        Whitebox.invokeMethod(spyService, "validateGoodsCode", lstGoods, lstStockModelIdLogistics);
    }

    @Test
    public void testValidateGoodsCode_4() throws Exception {
        GoodsDTO goodsDTO = new GoodsDTO();
        goodsDTO.setGoodsCode("1");
        goodsDTO.setAmountOrder("1");
        goodsDTO.setAmountReal("1");
        goodsDTO.setGoodsState("1");
        List<GoodsDTO> lstGoods = Lists.newArrayList(goodsDTO);
        List<Long> lstStockModelIdLogistics = Lists.newArrayList();
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        Whitebox.invokeMethod(spyService, "validateGoodsCode", lstGoods, lstStockModelIdLogistics);
    }

    @Test
    public void testValidateGoodsCode_5() throws Exception {
        GoodsDTO goodsDTO = new GoodsDTO();
        goodsDTO.setGoodsCode("1");
        goodsDTO.setAmountOrder("1");
        goodsDTO.setAmountReal("1");
        goodsDTO.setGoodsState("1");
        List<GoodsDTO> lstGoods = Lists.newArrayList(goodsDTO);
        List<Long> lstStockModelIdLogistics = Lists.newArrayList();
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        ProductOfferingDTO productOfferingDTO = mock(ProductOfferingDTO.class);
        when(productOfferingService.findByProductOfferCode(goodsDTO.getGoodsCode(), Const.STATUS_ACTIVE)).thenReturn(productOfferingDTO);
        when(productOfferingDTO.getCheckSerial()).thenReturn(1L);
        Whitebox.invokeMethod(spyService, "validateGoodsCode", lstGoods, lstStockModelIdLogistics);
    }

    @Test
    public void testValidateGoodsCode_6() throws Exception {
        GoodsDTO goodsDTO = new GoodsDTO();
        Serial serial = new Serial();
        serial.setQuantity("1");
        List<Serial> serialList = Lists.newArrayList(serial);
        goodsDTO.setGoodsCode("1");
        goodsDTO.setAmountOrder("1");
        goodsDTO.setAmountReal("1");
        goodsDTO.setGoodsState("1");
        goodsDTO.setLstSerial(serialList);
        List<GoodsDTO> lstGoods = Lists.newArrayList(goodsDTO);
        List<Long> lstStockModelIdLogistics = Lists.newArrayList();
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        ProductOfferingDTO productOfferingDTO = mock(ProductOfferingDTO.class);
        when(productOfferingService.findByProductOfferCode(goodsDTO.getGoodsCode(), Const.STATUS_ACTIVE)).thenReturn(productOfferingDTO);
        when(productOfferingDTO.getCheckSerial()).thenReturn(1L);
        Whitebox.invokeMethod(spyService, "validateGoodsCode", lstGoods, lstStockModelIdLogistics);
    }

    @Test
    public void testValidateGoodsCode_7() throws Exception {
        GoodsDTO goodsDTO = new GoodsDTO();
        Serial serial = new Serial();
        serial.setQuantity("1");
        serial.setFromSerial("1");
        List<Serial> serialList = Lists.newArrayList(serial);
        goodsDTO.setGoodsCode("1");
        goodsDTO.setAmountOrder("1");
        goodsDTO.setAmountReal("1");
        goodsDTO.setGoodsState("1");
        goodsDTO.setLstSerial(serialList);
        List<GoodsDTO> lstGoods = Lists.newArrayList(goodsDTO);
        List<Long> lstStockModelIdLogistics = Lists.newArrayList();
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        ProductOfferingDTO productOfferingDTO = mock(ProductOfferingDTO.class);
        when(productOfferingService.findByProductOfferCode(goodsDTO.getGoodsCode(), Const.STATUS_ACTIVE)).thenReturn(productOfferingDTO);
        when(productOfferingDTO.getCheckSerial()).thenReturn(1L);
        Whitebox.invokeMethod(spyService, "validateGoodsCode", lstGoods, lstStockModelIdLogistics);
    }

    @Test
    public void testValidateGoodsCode_8() throws Exception {
        GoodsDTO goodsDTO = new GoodsDTO();
        Serial serial = new Serial();
        //serial.setQuantity("1");
        serial.setFromSerial("1");
        serial.setToSerial("1");
        List<Serial> serialList = Lists.newArrayList(serial);
        goodsDTO.setGoodsCode("1");
        goodsDTO.setAmountOrder("1");
        goodsDTO.setAmountReal("1");
        goodsDTO.setGoodsState("1");
        goodsDTO.setLstSerial(serialList);
        List<GoodsDTO> lstGoods = Lists.newArrayList(goodsDTO);
        List<Long> lstStockModelIdLogistics = Lists.newArrayList();
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        ProductOfferingDTO productOfferingDTO = mock(ProductOfferingDTO.class);
        when(productOfferingService.findByProductOfferCode(goodsDTO.getGoodsCode(), Const.STATUS_ACTIVE)).thenReturn(productOfferingDTO);
        when(productOfferingDTO.getCheckSerial()).thenReturn(1L);
        Whitebox.invokeMethod(spyService, "validateGoodsCode", lstGoods, lstStockModelIdLogistics);
    }

    @Test
    public void testValidateGoodsCode_9() throws Exception {
        GoodsDTO goodsDTO = new GoodsDTO();
        Serial serial = new Serial();
        serial.setQuantity("0");
        serial.setFromSerial("1");
        serial.setToSerial("1");
        List<Serial> serialList = Lists.newArrayList(serial);
        goodsDTO.setGoodsCode("1");
        goodsDTO.setAmountOrder("1");
        goodsDTO.setAmountReal("1");
        goodsDTO.setGoodsState("1");
        goodsDTO.setLstSerial(serialList);
        List<GoodsDTO> lstGoods = Lists.newArrayList(goodsDTO);
        List<Long> lstStockModelIdLogistics = Lists.newArrayList();
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        ProductOfferingDTO productOfferingDTO = mock(ProductOfferingDTO.class);
        when(productOfferingService.findByProductOfferCode(goodsDTO.getGoodsCode(), Const.STATUS_ACTIVE)).thenReturn(productOfferingDTO);
        when(productOfferingDTO.getCheckSerial()).thenReturn(1L);
        Whitebox.invokeMethod(spyService, "validateGoodsCode", lstGoods, lstStockModelIdLogistics);
    }

    @Test
    public void testValidateGoodsCode_10() throws Exception {
        GoodsDTO goodsDTO = new GoodsDTO();
        Serial serial = new Serial();
        serial.setQuantity("1");
        serial.setToSerial("1");
        serial.setFromSerial("1");
        List<Serial> serialList = Lists.newArrayList(serial);
        goodsDTO.setGoodsCode("1");
        goodsDTO.setAmountOrder("1");
        goodsDTO.setAmountReal("1");
        goodsDTO.setGoodsState("1");
        goodsDTO.setLstSerial(serialList);
        List<GoodsDTO> lstGoods = Lists.newArrayList(goodsDTO);
        lstGoods.add(goodsDTO);
        List<Long> lstStockModelIdLogistics = Lists.newArrayList();
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        ProductOfferingDTO productOfferingDTO = mock(ProductOfferingDTO.class);
        when(productOfferingService.findByProductOfferCode(goodsDTO.getGoodsCode(), Const.STATUS_ACTIVE)).thenReturn(productOfferingDTO);
        when(productOfferingDTO.getCheckSerial()).thenReturn(1L);
        Whitebox.invokeMethod(spyService, "validateGoodsCode", lstGoods, lstStockModelIdLogistics);
    }

    @Test
    public void testValidateGoodsCode_11() throws Exception {
        GoodsDTO goodsDTO = new GoodsDTO();
        Serial serial = new Serial();
        serial.setQuantity("1");
        serial.setToSerial("1");
        serial.setFromSerial("1");
        List<Serial> serialList = Lists.newArrayList(serial);
        goodsDTO.setGoodsCode("1");
        goodsDTO.setAmountOrder("cd");
        goodsDTO.setAmountReal("1");
        goodsDTO.setGoodsState("1");
        goodsDTO.setLstSerial(serialList);
        List<GoodsDTO> lstGoods = Lists.newArrayList(goodsDTO);
        lstGoods.add(goodsDTO);
        List<Long> lstStockModelIdLogistics = Lists.newArrayList();
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        ProductOfferingDTO productOfferingDTO = mock(ProductOfferingDTO.class);
        when(productOfferingService.findByProductOfferCode(goodsDTO.getGoodsCode(), Const.STATUS_ACTIVE)).thenReturn(productOfferingDTO);
        when(productOfferingDTO.getCheckSerial()).thenReturn(1L);
        Whitebox.invokeMethod(spyService, "validateGoodsCode", lstGoods, lstStockModelIdLogistics);
    }

    @Test
    public void testValidateGoodsCode_12() throws Exception {
        GoodsDTO goodsDTO = new GoodsDTO();
        Serial serial = new Serial();
        serial.setQuantity("1");
        serial.setToSerial("1");
        serial.setFromSerial("1");
        List<Serial> serialList = Lists.newArrayList(serial);
        goodsDTO.setGoodsCode("1");
        goodsDTO.setAmountOrder("1");
        goodsDTO.setAmountReal("1");
        goodsDTO.setGoodsState("sts");
        goodsDTO.setLstSerial(serialList);
        List<GoodsDTO> lstGoods = Lists.newArrayList(goodsDTO);
        lstGoods.add(goodsDTO);
        List<Long> lstStockModelIdLogistics = Lists.newArrayList();
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        ProductOfferingDTO productOfferingDTO = mock(ProductOfferingDTO.class);
        when(productOfferingService.findByProductOfferCode(goodsDTO.getGoodsCode(), Const.STATUS_ACTIVE)).thenReturn(productOfferingDTO);
        when(productOfferingDTO.getCheckSerial()).thenReturn(1L);
        Whitebox.invokeMethod(spyService, "validateGoodsCode", lstGoods, lstStockModelIdLogistics);
    }

    @Test
    public void testValidateGoodsCode_13() throws Exception {
        GoodsDTO goodsDTO = new GoodsDTO();
        Serial serial = new Serial();
        serial.setQuantity("q");
        serial.setToSerial("1");
        serial.setFromSerial("1");
        List<Serial> serialList = Lists.newArrayList(serial);
        goodsDTO.setGoodsCode("1");
        goodsDTO.setAmountOrder("1");
        goodsDTO.setAmountReal("1");
        goodsDTO.setGoodsState("1");
        goodsDTO.setLstSerial(serialList);
        List<GoodsDTO> lstGoods = Lists.newArrayList(goodsDTO);
        lstGoods.add(goodsDTO);
        List<Long> lstStockModelIdLogistics = Lists.newArrayList();
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        ProductOfferingDTO productOfferingDTO = mock(ProductOfferingDTO.class);
        when(productOfferingService.findByProductOfferCode(goodsDTO.getGoodsCode(), Const.STATUS_ACTIVE)).thenReturn(productOfferingDTO);
        when(productOfferingDTO.getCheckSerial()).thenReturn(1L);
        Whitebox.invokeMethod(spyService, "validateGoodsCode", lstGoods, lstStockModelIdLogistics);
    }

    @Test(expected = Exception.class)
    public void testValidateGoodsCode_14() throws Exception {
        GoodsDTO goodsDTO = new GoodsDTO();
        Serial serial = new Serial();
        serial.setQuantity("1");
        serial.setToSerial("1");
        serial.setFromSerial("1");
        List<Serial> serialList = Lists.newArrayList(serial);
        goodsDTO.setGoodsCode("1");
        goodsDTO.setAmountOrder("1");
        goodsDTO.setAmountReal("1");
        goodsDTO.setGoodsState("1");
        goodsDTO.setLstSerial(serialList);
        List<GoodsDTO> lstGoods = Lists.newArrayList(goodsDTO);
        lstGoods.add(goodsDTO);
        List<Long> lstStockModelIdLogistics = Lists.newArrayList();
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        ProductOfferingDTO productOfferingDTO = mock(ProductOfferingDTO.class);
        when(productOfferingService.findByProductOfferCode(goodsDTO.getGoodsCode(), Const.STATUS_ACTIVE)).thenThrow(new Exception());
        when(productOfferingDTO.getCheckSerial()).thenReturn(1L);
        Whitebox.invokeMethod(spyService, "validateGoodsCode", lstGoods, lstStockModelIdLogistics);
    }


    @Test
    public void testValidateGoodsCode_15() throws Exception {
        GoodsDTO goodsDTO = new GoodsDTO();
        goodsDTO.setGoodsCode("1");
        goodsDTO.setAmountReal("1");

        List<GoodsDTO> lstGoods = Lists.newArrayList(goodsDTO);
        List<Long> lstStockModelIdLogistics = Lists.newArrayList();
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        Whitebox.invokeMethod(spyService, "validateGoodsCode", lstGoods, lstStockModelIdLogistics);
    }

    @Test
    public void testValidateGoodsCode_16() throws Exception {
        GoodsDTO goodsDTO = new GoodsDTO();
        goodsDTO.setGoodsCode("1");
        goodsDTO.setAmountOrder("0");
        goodsDTO.setAmountReal("1");

        List<GoodsDTO> lstGoods = Lists.newArrayList();
        List<Long> lstStockModelIdLogistics = Lists.newArrayList();
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        Whitebox.invokeMethod(spyService, "validateGoodsCode", lstGoods, lstStockModelIdLogistics);
    }

    @Test
    public void testValidateGoodsCode_17() throws Exception {
        GoodsDTO goodsDTO = new GoodsDTO();
        goodsDTO.setGoodsCode("1");
        goodsDTO.setAmountOrder("");
        goodsDTO.setAmountReal("1");

        List<GoodsDTO> lstGoods = Lists.newArrayList(goodsDTO);
        List<Long> lstStockModelIdLogistics = Lists.newArrayList();
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        Whitebox.invokeMethod(spyService, "validateGoodsCode", lstGoods, lstStockModelIdLogistics);
    }

    @Test
    public void testValidateGoodsCode_18() throws Exception {
        GoodsDTO goodsDTO = new GoodsDTO();
        goodsDTO.setGoodsCode("1");
        goodsDTO.setAmountOrder("1");
        goodsDTO.setAmountReal("");

        List<GoodsDTO> lstGoods = Lists.newArrayList(goodsDTO);
        List<Long> lstStockModelIdLogistics = Lists.newArrayList();
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        Whitebox.invokeMethod(spyService, "validateGoodsCode", lstGoods, lstStockModelIdLogistics);
    }

    @Test
    public void testValidateGoodsCode_19() throws Exception {
        GoodsDTO goodsDTO = new GoodsDTO();
        goodsDTO.setGoodsCode("1");
        goodsDTO.setAmountOrder("0");
        goodsDTO.setAmountReal("1");
        goodsDTO.setGoodsState("0");
        List<GoodsDTO> lstGoods = Lists.newArrayList(goodsDTO);
        List<Long> lstStockModelIdLogistics = Lists.newArrayList();
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        Whitebox.invokeMethod(spyService, "validateGoodsCode", lstGoods, lstStockModelIdLogistics);
    }

    @Test
    public void testValidateGoodsCode_20() throws Exception {
        GoodsDTO goodsDTO = new GoodsDTO();
        goodsDTO.setGoodsCode("1");
        goodsDTO.setAmountOrder("1");
        goodsDTO.setAmountReal("0");
        goodsDTO.setGoodsState("0");
        List<GoodsDTO> lstGoods = Lists.newArrayList(goodsDTO);
        List<Long> lstStockModelIdLogistics = Lists.newArrayList();
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        Whitebox.invokeMethod(spyService, "validateGoodsCode", lstGoods, lstStockModelIdLogistics);
    }

    @Test
    public void testValidateGoodsCode_21() throws Exception {
        GoodsDTO goodsDTO = new GoodsDTO();
        Serial serial = new Serial();
        serial.setQuantity("1");
        serial.setToSerial("1");
        serial.setFromSerial("1");
        List<Serial> serialList = Lists.newArrayList(serial);
        goodsDTO.setGoodsCode("1");
        goodsDTO.setAmountOrder("1");
        goodsDTO.setAmountReal("1");
        goodsDTO.setGoodsState("1");
        goodsDTO.setLstSerial(serialList);
        List<GoodsDTO> lstGoods = Lists.newArrayList(goodsDTO);
        lstGoods.add(goodsDTO);
        List<Long> lstStockModelIdLogistics = Lists.newArrayList();
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        ProductOfferingDTO productOfferingDTO = mock(ProductOfferingDTO.class);
        when(productOfferingService.findByProductOfferCode(goodsDTO.getGoodsCode(), Const.STATUS_ACTIVE)).thenReturn(productOfferingDTO);
        when(productOfferingDTO.getCheckSerial()).thenReturn(null);
        Whitebox.invokeMethod(spyService, "validateGoodsCode", lstGoods, lstStockModelIdLogistics);
    }

    @Test
    public void testValidateGoodsCode_22() throws Exception {
        GoodsDTO goodsDTO = new GoodsDTO();
        Serial serial = new Serial();
        serial.setQuantity("1");
        serial.setToSerial("1");
        serial.setFromSerial("1");
        List<Serial> serialList = Lists.newArrayList(serial);
        goodsDTO.setGoodsCode("1");
        goodsDTO.setAmountOrder("1");
        goodsDTO.setAmountReal("1");
        goodsDTO.setGoodsState("1");
        goodsDTO.setLstSerial(serialList);
        List<GoodsDTO> lstGoods = Lists.newArrayList(goodsDTO);
        lstGoods.add(goodsDTO);
        List<Long> lstStockModelIdLogistics = Lists.newArrayList();
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        ProductOfferingDTO productOfferingDTO = mock(ProductOfferingDTO.class);
        when(productOfferingService.findByProductOfferCode(goodsDTO.getGoodsCode(), Const.STATUS_ACTIVE)).thenReturn(productOfferingDTO);
        when(productOfferingDTO.getCheckSerial()).thenReturn(2L);
        Whitebox.invokeMethod(spyService, "validateGoodsCode", lstGoods, lstStockModelIdLogistics);
    }

    @Test
    public void testValidateGoodsCode_23() throws Exception {
        GoodsDTO goodsDTO = new GoodsDTO();
        Serial serial = new Serial();
        serial.setQuantity("1");
        serial.setToSerial("1");
        serial.setFromSerial("1");
        List<Serial> serialList = Lists.newArrayList();
        goodsDTO.setGoodsCode("1");
        goodsDTO.setAmountOrder("1");
        goodsDTO.setAmountReal("1");
        goodsDTO.setGoodsState("1");
        goodsDTO.setLstSerial(serialList);
        List<GoodsDTO> lstGoods = Lists.newArrayList(goodsDTO);
        lstGoods.add(goodsDTO);
        List<Long> lstStockModelIdLogistics = Lists.newArrayList();
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        ProductOfferingDTO productOfferingDTO = mock(ProductOfferingDTO.class);
        when(productOfferingService.findByProductOfferCode(goodsDTO.getGoodsCode(), Const.STATUS_ACTIVE)).thenReturn(productOfferingDTO);
        when(productOfferingDTO.getCheckSerial()).thenReturn(1L);
        Whitebox.invokeMethod(spyService, "validateGoodsCode", lstGoods, lstStockModelIdLogistics);
    }

    @Test
    public void testValidateGoodsCode_24() throws Exception {
        GoodsDTO goodsDTO = new GoodsDTO();
        Serial serial = new Serial();
        serial.setQuantity("1");
        serial.setToSerial("1");
        serial.setFromSerial("1");
        List<Serial> serialList = Lists.newArrayList();
        goodsDTO.setGoodsCode("1");
        goodsDTO.setAmountOrder("1");
        goodsDTO.setAmountReal("1");
        goodsDTO.setGoodsState("1");
        goodsDTO.setLstSerial(null);
        List<GoodsDTO> lstGoods = Lists.newArrayList(goodsDTO);
        lstGoods.add(goodsDTO);
        List<Long> lstStockModelIdLogistics = Lists.newArrayList();
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        ProductOfferingDTO productOfferingDTO = mock(ProductOfferingDTO.class);
        when(productOfferingService.findByProductOfferCode(goodsDTO.getGoodsCode(), Const.STATUS_ACTIVE)).thenReturn(productOfferingDTO);
        when(productOfferingDTO.getCheckSerial()).thenReturn(1L);
        Whitebox.invokeMethod(spyService, "validateGoodsCode", lstGoods, lstStockModelIdLogistics);
    }
    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method LogisticBaseServiceImpl.validateBillStockGeneral
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testValidateBillStockGeneral_1() throws Exception {
        BillStockDTO billStockDTO = null;
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        Whitebox.invokeMethod(spyService, "validateBillStockGeneral", billStockDTO);
    }

    @Test
    public void testValidateBillStockGeneral_2() throws Exception {
        BillStockDTO billStockDTO = new BillStockDTO();
        billStockDTO.setOrderCode("1");
        billStockDTO.setOrderType("3");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        Whitebox.invokeMethod(spyService, "validateBillStockGeneral", billStockDTO);
    }

    @Test
    public void testValidateBillStockGeneral_3() throws Exception {
        BillStockDTO billStockDTO = new BillStockDTO();
        billStockDTO.setOrderCode("1");
        billStockDTO.setOrderType("1");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        Whitebox.invokeMethod(spyService, "validateBillStockGeneral", billStockDTO);
    }

    @Test
    public void testValidateBillStockGeneral_4() throws Exception {
        BillStockDTO billStockDTO = new BillStockDTO();
        billStockDTO.setOrderCode("1");
        billStockDTO.setOrderType("1");
        billStockDTO.setInputType("3");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        Whitebox.invokeMethod(spyService, "validateBillStockGeneral", billStockDTO);
    }
    @Test
    public void testValidateBillStockGeneral_5() throws Exception {
        BillStockDTO billStockDTO = new BillStockDTO();
        billStockDTO.setOrderCode("1");
        billStockDTO.setOrderType("2");
        billStockDTO.setInputType("3");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        Whitebox.invokeMethod(spyService, "validateBillStockGeneral", billStockDTO);
    }

    @Test
    public void testValidateBillStockGeneral_6() throws Exception {
        BillStockDTO billStockDTO = new BillStockDTO();
        billStockDTO.setOrderCode("1");
        billStockDTO.setOrderType("2");
        billStockDTO.setInputType("3");
        billStockDTO.setOutputType("1");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        Whitebox.invokeMethod(spyService, "validateBillStockGeneral", billStockDTO);
    }

    @Test
    public void testValidateBillStockGeneral_7() throws Exception {
        BillStockDTO billStockDTO = new BillStockDTO();
        billStockDTO.setOrderCode("1");
        billStockDTO.setOrderType("2");
        billStockDTO.setInputType("3");
        billStockDTO.setOutputType("3");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        Whitebox.invokeMethod(spyService, "validateBillStockGeneral", billStockDTO);
    }

    @Test
    public void testValidateBillStockGeneral_8() throws Exception {
        BillStockDTO billStockDTO = new BillStockDTO();
        billStockDTO.setOrderCode("2");
        billStockDTO.setOrderType("1");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        Whitebox.invokeMethod(spyService, "validateBillStockGeneral", billStockDTO);
    }

    @Test
    public void testValidateBillStockGeneral_9() throws Exception {
        BillStockDTO billStockDTO = new BillStockDTO();
        billStockDTO.setOrderCode("1");
        billStockDTO.setOrderType("1");
        billStockDTO.setInputType("1");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        Whitebox.invokeMethod(spyService, "validateBillStockGeneral", billStockDTO);
    }

    @Test
    public void testValidateBillStockGeneral_10() throws Exception {
        BillStockDTO billStockDTO = new BillStockDTO();
        billStockDTO.setOrderCode("1");
        billStockDTO.setOrderType("1");
        billStockDTO.setInputType("2");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        Whitebox.invokeMethod(spyService, "validateBillStockGeneral", billStockDTO);
    }

    @Test
    public void testValidateBillStockGeneral_11() throws Exception {
        BillStockDTO billStockDTO = new BillStockDTO();
        billStockDTO.setOrderCode("1");
        billStockDTO.setOrderType("2");
        billStockDTO.setInputType("3");
        billStockDTO.setOutputType("2");
        LogisticBaseServiceImpl spyService = PowerMockito.spy(logisticBaseService);
        Whitebox.invokeMethod(spyService, "validateBillStockGeneral", billStockDTO);
    }

    static void setFinalStatic(Field field, Object newValue, final Object targetObject) throws Exception {
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(targetObject, newValue);
    }
}