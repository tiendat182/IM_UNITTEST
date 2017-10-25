package com.viettel.bccs.im1.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.im1.dto.*;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.ProductOfferingService;
import com.viettel.bccs.inventory.service.ShopService;
import com.viettel.bccs.inventory.service.StaffService;
import com.viettel.bccs.inventory.service.StockTransActionService;
import com.viettel.bccs.inventory.service.common.IMServiceUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.service.BaseServiceImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author DatLT
 * @date 19/10/2017
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({DbUtil.class, BaseStockIm1Service.class, BaseServiceImpl.class, IMServiceUtil.class})
public class BaseStockIm1ServiceTest {
    @InjectMocks
    BaseStockIm1Service baseStockIm1Service;
    @Mock
    private StockTotalIm1Service stockTotalIm1Service;
    @Mock
    private StockTotalAuditIm1Service stockTotalAuditIm1Service;
    @Mock
    private StaffService staffService;
    @Mock
    private ShopService shopService;
    @Mock
    private ProductOfferingService productOfferingService;
    @Mock
    private StaffIm1Service staffIm1Service;
    @Mock
    private ShopIm1Service shopIm1Service;
    @Mock
    private StockTransActionService stockTransActionService;
    @Mock
    private EntityManager em;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method BaseStockIm1Service.doSaveStockTotal
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testDoSaveStockTotal_1() throws Exception {
        //in 62
        StockTransDTO stockTransDTO = new StockTransDTO();
        List<StockTransDetailDTO> lstStockTransDetailDTO = Lists.newArrayList();
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransDTO.setProcessOffline("1");

        baseStockIm1Service.doSaveStockTotal(stockTransDTO, lstStockTransDetailDTO, flagStockDTO, stockTransActionDTO);
    }

    @Test
    public void testDoSaveStockTotal_2() throws Exception {
        //out 62
        //in 65
        StockTransDTO stockTransDTO = new StockTransDTO();
        List<StockTransDetailDTO> lstStockTransDetailDTO = Lists.newArrayList();
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setExportStock(false);
        flagStockDTO.setImportStock(false);

        baseStockIm1Service.doSaveStockTotal(stockTransDTO, lstStockTransDetailDTO, flagStockDTO, stockTransActionDTO);
    }

    @Test
    public void testDoSaveStockTotal_3() throws Exception {
        //out 65
        StockTransDTO stockTransDTO = new StockTransDTO();
        List<StockTransDetailDTO> lstStockTransDetailDTO = Lists.newArrayList();
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setExportStock(true);
//        flagStockDTO.setImportStock(false);

        baseStockIm1Service.doSaveStockTotal(stockTransDTO, lstStockTransDetailDTO, flagStockDTO, stockTransActionDTO);
    }

    @Test
    public void testDoSaveStockTotal_4() throws Exception {
        //out 65
        //in 77
        StockTransDTO stockTransDTO = new StockTransDTO();
        List<StockTransDetailDTO> lstStockTransDetailDTO = Lists.newArrayList();
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setExportStock(false);
        flagStockDTO.setImportStock(true);
        stockTransDTO.setExchangeStockId(1L);

        baseStockIm1Service.doSaveStockTotal(stockTransDTO, lstStockTransDetailDTO, flagStockDTO, stockTransActionDTO);
    }

    @Test
    public void testDoSaveStockTotal_5() throws Exception {
        //out 77
        //in 84
        StockTransDTO stockTransDTO = new StockTransDTO();
        List<StockTransDetailDTO> lstStockTransDetailDTO = Lists.newArrayList();
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setExportStock(false);
        flagStockDTO.setImportStock(true);
        stockTransDTO.setExchangeStockId(null);

        stockTransDTO.setRegionStockId(1L);
        flagStockDTO.setStatusForAgent("");
        flagStockDTO.setShopId(7282L);

        baseStockIm1Service.doSaveStockTotal(stockTransDTO, lstStockTransDetailDTO, flagStockDTO, stockTransActionDTO);
    }

    @Test
    public void testDoSaveStockTotal_6() throws Exception {
        //out 77
        //in 84
        StockTransDTO stockTransDTO = new StockTransDTO();
        List<StockTransDetailDTO> lstStockTransDetailDTO = Lists.newArrayList();
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setExportStock(false);
        flagStockDTO.setImportStock(true);
        stockTransDTO.setExchangeStockId(null);

        flagStockDTO.setStatusForAgent("3");
        stockTransDTO.setRegionStockId(1L);
        flagStockDTO.setShopId(7282L);

        baseStockIm1Service.doSaveStockTotal(stockTransDTO, lstStockTransDetailDTO, flagStockDTO, stockTransActionDTO);
    }

    @Test
    public void testDoSaveStockTotal_7() throws Exception {
        //out 84
        StockTransDTO stockTransDTO = new StockTransDTO();
        List<StockTransDetailDTO> lstStockTransDetailDTO = Lists.newArrayList();
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setExportStock(false);
        flagStockDTO.setImportStock(true);
        stockTransDTO.setExchangeStockId(null);

        stockTransDTO.setRegionStockId(null);
        flagStockDTO.setStatusForAgent("3");
        flagStockDTO.setShopId(7282L);

        baseStockIm1Service.doSaveStockTotal(stockTransDTO, lstStockTransDetailDTO, flagStockDTO, stockTransActionDTO);
    }

    @Test
    public void testDoSaveStockTotal_8() throws Exception {
        //out 84
        StockTransDTO stockTransDTO = new StockTransDTO();
        List<StockTransDetailDTO> lstStockTransDetailDTO = Lists.newArrayList();
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setImportStock(true);
        flagStockDTO.setExportStock(false);
        stockTransDTO.setExchangeStockId(null);

        stockTransDTO.setRegionStockId(1L);
        flagStockDTO.setStatusForAgent("4");

        baseStockIm1Service.doSaveStockTotal(stockTransDTO, lstStockTransDetailDTO, flagStockDTO, stockTransActionDTO);
    }

    @Test
    public void testDoSaveStockTotal_9() throws Exception {
        //out 84
        //in 103,111,126,128
        StockTransDTO stockTransDTO = new StockTransDTO();
        List<StockTransDetailDTO> lstStockTransDetailDTO = Lists.newArrayList();
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setExportStock(true);
        flagStockDTO.setImportStock(true);
        flagStockDTO.setExpAvailableQuantity(1L);
        flagStockDTO.setObjectType("AGENT_EXPORT");
        stockTransDTO.setExchangeStockId(null);
        stockTransDTO.setPayStatus("1");
        StockTransDetailIm1DTO stockTransDetailIm1DTO = mock(StockTransDetailIm1DTO.class);
        List<StockTransDetailIm1DTO> lstStockTransDetailIm1 = Lists.newArrayList(stockTransDetailIm1DTO);
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStaffCode("cd");
        staffDTO.setName("nm");
        ShopDTO shopDTO = new ShopDTO();

        stockTransDTO.setRegionStockId(1L);
        flagStockDTO.setStatusForAgent("3");
        flagStockDTO.setShopId(72L);

        when(stockTotalIm1Service.findStockTransDetail(anyLong())).thenReturn(lstStockTransDetailIm1);
        when(stockTransDetailIm1DTO.getStockModelId()).thenReturn(1L);
        when(stockTransDetailIm1DTO.getStockModelName()).thenReturn("nm");
        when(stockTransDetailIm1DTO.getStateId()).thenReturn(2L);
        when(stockTransDetailIm1DTO.getQuantityReal()).thenReturn(2L);
        when(stockTransDetailIm1DTO.getQuantityReal()).thenReturn(2L);
        when(staffService.findOne(anyLong())).thenReturn(staffDTO);
        when(shopService.findOne(anyLong())).thenReturn(shopDTO);
        baseStockIm1Service.doSaveStockTotal(stockTransDTO, lstStockTransDetailDTO, flagStockDTO, stockTransActionDTO);
    }

    @Test
    public void testDoSaveStockTotal_10() throws Exception {
        //out 128, 155
        StockTransDTO stockTransDTO = new StockTransDTO();
        List<StockTransDetailDTO> lstStockTransDetailDTO = Lists.newArrayList();
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransDTO.setProcessOffline("2");

        flagStockDTO.setExportStock(true);
        flagStockDTO.setImportStock(true);
        flagStockDTO.setExpAvailableQuantity(1L);
        flagStockDTO.setObjectType(null);
        flagStockDTO.setInsertStockTotalAudit(false);

        stockTransDTO.setExchangeStockId(null);
        stockTransDTO.setPayStatus("1");

        StockTransDetailIm1DTO stockTransDetailIm1DTO = mock(StockTransDetailIm1DTO.class);
        List<StockTransDetailIm1DTO> lstStockTransDetailIm1 = Lists.newArrayList(stockTransDetailIm1DTO);
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStaffCode("cd");
        staffDTO.setName("nm");
        ShopDTO shopDTO = new ShopDTO();

        stockTransDTO.setRegionStockId(1L);
        flagStockDTO.setStatusForAgent("3");
        flagStockDTO.setShopId(72L);

        when(stockTotalIm1Service.findStockTransDetail(anyLong())).thenReturn(lstStockTransDetailIm1);
        when(stockTransDetailIm1DTO.getStockModelId()).thenReturn(1L);
        when(stockTransDetailIm1DTO.getStockModelName()).thenReturn("nm");
        when(stockTransDetailIm1DTO.getStateId()).thenReturn(2L);
        when(stockTransDetailIm1DTO.getQuantityReal()).thenReturn(2L);
        when(stockTransDetailIm1DTO.getQuantityReal()).thenReturn(2L);
        when(staffService.findOne(anyLong())).thenReturn(staffDTO);
        when(shopService.findOne(anyLong())).thenReturn(shopDTO);
        baseStockIm1Service.doSaveStockTotal(stockTransDTO, lstStockTransDetailDTO, flagStockDTO, stockTransActionDTO);
    }

    @Test
    public void testDoSaveStockTotal_11() throws Exception {
        //out 128
        StockTransDTO stockTransDTO = new StockTransDTO();
        List<StockTransDetailDTO> lstStockTransDetailDTO = Lists.newArrayList();
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();

        flagStockDTO.setExportStock(true);
        flagStockDTO.setImportStock(true);
        flagStockDTO.setExpAvailableQuantity(1L);
        flagStockDTO.setObjectType("VT_PT");
        flagStockDTO.setNote("EXPORT_RESCUE");

        stockTransDTO.setProcessOffline("2");
        stockTransDTO.setExchangeStockId(null);
        stockTransDTO.setPayStatus("1");
        StockTransDetailIm1DTO stockTransDetailIm1DTO = mock(StockTransDetailIm1DTO.class);
        List<StockTransDetailIm1DTO> lstStockTransDetailIm1 = Lists.newArrayList(stockTransDetailIm1DTO);
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStaffCode("cd");
        staffDTO.setName("nm");
        ShopDTO shopDTO = new ShopDTO();

        stockTransDTO.setRegionStockId(1L);
        flagStockDTO.setStatusForAgent("3");
        flagStockDTO.setShopId(72L);

        when(stockTotalIm1Service.findStockTransDetail(anyLong())).thenReturn(lstStockTransDetailIm1);
        when(stockTransDetailIm1DTO.getStockModelId()).thenReturn(1L);
        when(stockTransDetailIm1DTO.getStockModelName()).thenReturn("nm");
        when(stockTransDetailIm1DTO.getStateId()).thenReturn(2L);
        when(stockTransDetailIm1DTO.getQuantityReal()).thenReturn(2L);
        when(stockTransDetailIm1DTO.getQuantityReal()).thenReturn(2L);
        when(staffService.findOne(anyLong())).thenReturn(staffDTO);
        when(shopService.findOne(anyLong())).thenReturn(shopDTO);
        baseStockIm1Service.doSaveStockTotal(stockTransDTO, lstStockTransDetailDTO, flagStockDTO, stockTransActionDTO);
    }

    @Test
    public void testDoSaveStockTotal_12() throws Exception {
        //out 128
        StockTransDTO stockTransDTO = new StockTransDTO();
        List<StockTransDetailDTO> lstStockTransDetailDTO = Lists.newArrayList();
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();

        flagStockDTO.setExportStock(true);
        flagStockDTO.setImportStock(true);
        flagStockDTO.setExpAvailableQuantity(1L);
        flagStockDTO.setObjectType("AGENT_EXPORT");
        flagStockDTO.setNote("EXPORT_RESCUE");
        flagStockDTO.setStateIdForReasonId(2L);

        stockTransDTO.setProcessOffline("2");
        stockTransDTO.setExchangeStockId(null);

        stockTransDTO.setPayStatus(null);
        StockTransDetailIm1DTO stockTransDetailIm1DTO = mock(StockTransDetailIm1DTO.class);
        List<StockTransDetailIm1DTO> lstStockTransDetailIm1 = Lists.newArrayList(stockTransDetailIm1DTO);
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStaffCode("cd");
        staffDTO.setName("nm");
        ShopDTO shopDTO = new ShopDTO();

        stockTransDTO.setRegionStockId(1L);
        flagStockDTO.setStatusForAgent("3");
        flagStockDTO.setShopId(72L);

        when(stockTotalIm1Service.findStockTransDetail(anyLong())).thenReturn(lstStockTransDetailIm1);
        when(stockTransDetailIm1DTO.getStockModelId()).thenReturn(1L);
        when(stockTransDetailIm1DTO.getStockModelName()).thenReturn("nm");
        when(stockTransDetailIm1DTO.getStateId()).thenReturn(2L);
        when(stockTransDetailIm1DTO.getQuantityReal()).thenReturn(2L);
        when(stockTransDetailIm1DTO.getQuantityReal()).thenReturn(2L);
        when(staffService.findOne(anyLong())).thenReturn(staffDTO);
        when(shopService.findOne(anyLong())).thenReturn(shopDTO);
        baseStockIm1Service.doSaveStockTotal(stockTransDTO, lstStockTransDetailDTO, flagStockDTO, stockTransActionDTO);
    }

    @Test
    public void testDoSaveStockTotal_13() throws Exception {
        //out 128
        //in137
        StockTransDTO stockTransDTO = new StockTransDTO();
        List<StockTransDetailDTO> lstStockTransDetailDTO = Lists.newArrayList();
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransDTO.setProcessOffline("2");

        flagStockDTO.setExportStock(true);
        flagStockDTO.setImportStock(true);
        flagStockDTO.setExpAvailableQuantity(1L);
        flagStockDTO.setObjectType("AGENT_EXPORT");
        flagStockDTO.setNote("RETRIEVE_STOCK");

        stockTransDTO.setExchangeStockId(null);
        stockTransDTO.setPayStatus("2");
        StockTransDetailIm1DTO stockTransDetailIm1DTO = mock(StockTransDetailIm1DTO.class);
        List<StockTransDetailIm1DTO> lstStockTransDetailIm1 = Lists.newArrayList(stockTransDetailIm1DTO);
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStaffCode("cd");
        staffDTO.setName("nm");
        ShopDTO shopDTO = new ShopDTO();

        stockTransDTO.setRegionStockId(1L);
        flagStockDTO.setStatusForAgent("3");
        flagStockDTO.setShopId(72L);

        when(stockTotalIm1Service.findStockTransDetail(anyLong())).thenReturn(lstStockTransDetailIm1);
        when(stockTransDetailIm1DTO.getStockModelId()).thenReturn(1L);
        when(stockTransDetailIm1DTO.getStockModelName()).thenReturn("nm");
        when(stockTransDetailIm1DTO.getStateId()).thenReturn(2L);
        when(stockTransDetailIm1DTO.getQuantityReal()).thenReturn(2L);
        when(stockTransDetailIm1DTO.getQuantityReal()).thenReturn(2L);
        when(staffService.findOne(anyLong())).thenReturn(staffDTO);
        when(shopService.findOne(anyLong())).thenReturn(shopDTO);
        baseStockIm1Service.doSaveStockTotal(stockTransDTO, lstStockTransDetailDTO, flagStockDTO, stockTransActionDTO);
    }

    @Test
    public void testDoSaveStockTotal_14() throws Exception {
        //out 126
        StockTransDTO stockTransDTO = new StockTransDTO();
        List<StockTransDetailDTO> lstStockTransDetailDTO = Lists.newArrayList();
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransDTO.setProcessOffline("2");

        flagStockDTO.setExportStock(true);
        flagStockDTO.setImportStock(false);
        flagStockDTO.setExpAvailableQuantity(1L);
        flagStockDTO.setObjectType("AGENT_EXPORT");
        flagStockDTO.setNote("RETRIEVE_STOCK");

        stockTransDTO.setExchangeStockId(null);
        stockTransDTO.setPayStatus("2");
        StockTransDetailIm1DTO stockTransDetailIm1DTO = mock(StockTransDetailIm1DTO.class);
        List<StockTransDetailIm1DTO> lstStockTransDetailIm1 = Lists.newArrayList(stockTransDetailIm1DTO);
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStaffCode("cd");
        staffDTO.setName("nm");
        ShopDTO shopDTO = new ShopDTO();

        stockTransDTO.setRegionStockId(1L);
        flagStockDTO.setStatusForAgent("3");
        flagStockDTO.setShopId(72L);

        when(stockTotalIm1Service.findStockTransDetail(anyLong())).thenReturn(lstStockTransDetailIm1);
        when(stockTransDetailIm1DTO.getStockModelId()).thenReturn(1L);
        when(stockTransDetailIm1DTO.getStockModelName()).thenReturn("nm");
        when(stockTransDetailIm1DTO.getStateId()).thenReturn(2L);
        when(stockTransDetailIm1DTO.getQuantityReal()).thenReturn(2L);
        when(stockTransDetailIm1DTO.getQuantityReal()).thenReturn(2L);
        when(staffService.findOne(anyLong())).thenReturn(staffDTO);
        when(shopService.findOne(anyLong())).thenReturn(shopDTO);
        baseStockIm1Service.doSaveStockTotal(stockTransDTO, lstStockTransDetailDTO, flagStockDTO, stockTransActionDTO);
    }

    @Test
    public void testDoSaveStockTotal_15() throws Exception {
        //out 111
        StockTransDTO stockTransDTO = new StockTransDTO();
        List<StockTransDetailDTO> lstStockTransDetailDTO = Lists.newArrayList();
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransDTO.setProcessOffline("2");

        flagStockDTO.setExportStock(false);
        flagStockDTO.setImportStock(false);
        flagStockDTO.setExpAvailableQuantity(1L);
        flagStockDTO.setObjectType("AGENT_EXPORT");
        flagStockDTO.setNote("RETRIEVE_STOCK");

        stockTransDTO.setExchangeStockId(null);
        stockTransDTO.setPayStatus("2");
        StockTransDetailIm1DTO stockTransDetailIm1DTO = mock(StockTransDetailIm1DTO.class);
        List<StockTransDetailIm1DTO> lstStockTransDetailIm1 = Lists.newArrayList(stockTransDetailIm1DTO);
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStaffCode("cd");
        staffDTO.setName("nm");
        ShopDTO shopDTO = new ShopDTO();

        stockTransDTO.setRegionStockId(1L);
        flagStockDTO.setStatusForAgent("3");
        flagStockDTO.setShopId(72L);

        when(stockTotalIm1Service.findStockTransDetail(anyLong())).thenReturn(lstStockTransDetailIm1);
        when(stockTransDetailIm1DTO.getStockModelId()).thenReturn(1L);
        when(stockTransDetailIm1DTO.getStockModelName()).thenReturn("nm");
        when(stockTransDetailIm1DTO.getStateId()).thenReturn(2L);
        when(stockTransDetailIm1DTO.getQuantityReal()).thenReturn(2L);
        when(stockTransDetailIm1DTO.getQuantityReal()).thenReturn(2L);
        when(staffService.findOne(anyLong())).thenReturn(staffDTO);
        when(shopService.findOne(anyLong())).thenReturn(shopDTO);
        baseStockIm1Service.doSaveStockTotal(stockTransDTO, lstStockTransDetailDTO, flagStockDTO, stockTransActionDTO);
    }

    @Test
    public void testDoSaveStockTotal_16() throws Exception {
        //in 94
        StockTransDTO stockTransDTO = new StockTransDTO();
        List<StockTransDetailDTO> lstStockTransDetailDTO = Lists.newArrayList();
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setExportStock(false);
        flagStockDTO.setImportStock(true);
        stockTransDTO.setExchangeStockId(null);

        flagStockDTO.setStatusForAgent("6");
        stockTransDTO.setRegionStockId(1L);
        flagStockDTO.setShopId(7282L);

        baseStockIm1Service.doSaveStockTotal(stockTransDTO, lstStockTransDetailDTO, flagStockDTO, stockTransActionDTO);
    }

    @Test
    public void testDoSaveStockTotal_17() throws Exception {
        //in 94
        StockTransDTO stockTransDTO = new StockTransDTO();
        List<StockTransDetailDTO> lstStockTransDetailDTO = Lists.newArrayList();
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setExportStock(false);
        flagStockDTO.setImportStock(true);
        stockTransDTO.setExchangeStockId(null);

        flagStockDTO.setStatusForAgent("");
        stockTransDTO.setRegionStockId(1L);
        flagStockDTO.setShopId(7282L);

        baseStockIm1Service.doSaveStockTotal(stockTransDTO, lstStockTransDetailDTO, flagStockDTO, stockTransActionDTO);
    }

    @Test
    public void testDoSaveStockTotal_18() throws Exception {
        //out 94
        StockTransDTO stockTransDTO = new StockTransDTO();
        List<StockTransDetailDTO> lstStockTransDetailDTO = Lists.newArrayList();
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setExportStock(false);
        flagStockDTO.setImportStock(true);
        stockTransDTO.setExchangeStockId(null);

        stockTransDTO.setRegionStockId(null);

        baseStockIm1Service.doSaveStockTotal(stockTransDTO, lstStockTransDetailDTO, flagStockDTO, stockTransActionDTO);
    }


    @Test
    public void testDoSaveStockTotal_19() throws Exception {
        //out 94
        StockTransDTO stockTransDTO = new StockTransDTO();
        List<StockTransDetailDTO> lstStockTransDetailDTO = Lists.newArrayList();
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setExportStock(false);
        flagStockDTO.setImportStock(true);
        stockTransDTO.setExchangeStockId(null);

        flagStockDTO.setStatusForAgent("4");
        stockTransDTO.setRegionStockId(1L);
        flagStockDTO.setShopId(7282L);

        baseStockIm1Service.doSaveStockTotal(stockTransDTO, lstStockTransDetailDTO, flagStockDTO, stockTransActionDTO);
    }

    @Test
    public void testDoSaveStockTotal_20() throws Exception {
        //out 94
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDetailDTO stockTransDetailDTO = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetailDTO = Lists.newArrayList(stockTransDetailDTO);
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setExportStock(false);
        flagStockDTO.setImportStock(true);
        stockTransDTO.setExchangeStockId(null);

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStaffCode("cd");
        staffDTO.setName("nm");
        ShopDTO shopDTO = new ShopDTO();
        when(staffService.findOne(anyLong())).thenReturn(staffDTO);
        when(shopService.findOne(anyLong())).thenReturn(shopDTO);

        flagStockDTO.setStatusForAgent("6");
        stockTransDTO.setRegionStockId(1L);
        flagStockDTO.setShopId(72L);

        when(stockTransDetailDTO.getCheckSerial()).thenReturn(1L);
        baseStockIm1Service.doSaveStockTotal(stockTransDTO, lstStockTransDetailDTO, flagStockDTO, stockTransActionDTO);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method BaseStockIm1Service.doSaveStockTotal (protected)
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testDoSaveStockTotal1_1() throws Exception {
        //in 174
        StockTotalAuditIm1DTO stockTotalAuditDTO = new StockTotalAuditIm1DTO();
        List<StockTotalIm1DTO> lstStockTotalDTO = Lists.newArrayList();
        StockTransActionDTO stockTransAction = new StockTransActionDTO();
        baseStockIm1Service.doSaveStockTotal(stockTotalAuditDTO, lstStockTotalDTO, stockTransAction);
    }

    @Test(expected = Exception.class)
    public void testDoSaveStockTotal1_2() throws Exception {
        //out 174
        //in 181, 183
        StockTotalAuditIm1DTO stockTotalAuditDTO = new StockTotalAuditIm1DTO();
        StockTotalIm1DTO stockTotalIm1DTO = mock(StockTotalIm1DTO.class);
        List<StockTotalIm1DTO> lstStockTotalDTO = Lists.newArrayList(stockTotalIm1DTO);
        StockTransActionDTO stockTransAction = new StockTransActionDTO();

        List<StockTotalIm1DTO> lstStockTotal = Lists.newArrayList(stockTotalIm1DTO);
        lstStockTotal.add(stockTotalIm1DTO);
        StockModelIm1DTO offeringDTO = new StockModelIm1DTO();


        when(stockTotalIm1Service.findStockTotal(any())).thenReturn(lstStockTotal);
        when(stockTotalIm1Service.findOneStockModel(anyLong())).thenReturn(offeringDTO);
        when(stockTotalIm1DTO.getStockModelId()).thenReturn(1L);
        baseStockIm1Service.doSaveStockTotal(stockTotalAuditDTO, lstStockTotalDTO, stockTransAction);
    }

    @Test
    public void testDoSaveStockTotal1_3() throws Exception {
        //out 183
        StockTotalAuditIm1DTO stockTotalAuditDTO = null;
        StockTotalIm1DTO stockTotalIm1DTO = mock(StockTotalIm1DTO.class);
        List<StockTotalIm1DTO> lstStockTotalDTO = Lists.newArrayList(stockTotalIm1DTO);
        StockTransActionDTO stockTransAction = new StockTransActionDTO();

        List<StockTotalIm1DTO> lstStockTotal = Lists.newArrayList(stockTotalIm1DTO);
        lstStockTotal.add(stockTotalIm1DTO);
        StockModelIm1DTO offeringDTO = null;


        when(stockTotalIm1Service.findStockTotal(any())).thenReturn(lstStockTotal);
        when(stockTotalIm1Service.findOneStockModel(anyLong())).thenReturn(offeringDTO);
        when(stockTotalIm1DTO.getStockModelId()).thenReturn(1L);
        baseStockIm1Service.doSaveStockTotal(stockTotalAuditDTO, lstStockTotalDTO, stockTransAction);
    }

    @Test(expected = Exception.class)
    public void testDoSaveStockTotal1_4() throws Exception {
        //out 181
        StockTotalAuditIm1DTO stockTotalAuditDTO = new StockTotalAuditIm1DTO();
        StockTotalIm1DTO stockTotalIm1DTO = mock(StockTotalIm1DTO.class);
        List<StockTotalIm1DTO> lstStockTotalDTO = Lists.newArrayList(stockTotalIm1DTO);
        StockTransActionDTO stockTransAction = new StockTransActionDTO();

        List<StockTotalIm1DTO> lstStockTotal = Lists.newArrayList();
        when(stockTotalIm1Service.findStockTotal(any())).thenReturn(lstStockTotal);
        when(stockTotalIm1DTO.getStockModelId()).thenReturn(1L);
        baseStockIm1Service.doSaveStockTotal(stockTotalAuditDTO, lstStockTotalDTO, stockTransAction);
    }


    @Test(expected = Exception.class)
    public void testDoSaveStockTotal1_5() throws Exception {
        //out 181, 197
        StockTotalAuditIm1DTO stockTotalAuditDTO = new StockTotalAuditIm1DTO();
        StockTotalIm1DTO stockTotalIm1DTO = mock(StockTotalIm1DTO.class);
        List<StockTotalIm1DTO> lstStockTotalDTO = Lists.newArrayList(stockTotalIm1DTO);
        StockTransActionDTO stockTransAction = new StockTransActionDTO();

        List<StockTotalIm1DTO> lstStockTotal = Lists.newArrayList(stockTotalIm1DTO);


        when(stockTotalIm1Service.findStockTotal(any())).thenReturn(lstStockTotal);
        when(stockTotalIm1DTO.getStockModelId()).thenReturn(1L);
        baseStockIm1Service.doSaveStockTotal(stockTotalAuditDTO, lstStockTotalDTO, stockTransAction);
    }


    @Test(expected = Exception.class)
    public void testDoSaveStockTotal1_6() throws Exception {
        //in 197
        StockTotalAuditIm1DTO stockTotalAuditDTO = new StockTotalAuditIm1DTO();
        StockTotalIm1DTO stockTotalIm1DTO = mock(StockTotalIm1DTO.class);
        List<StockTotalIm1DTO> lstStockTotalDTO = Lists.newArrayList(stockTotalIm1DTO);
        StockTransActionDTO stockTransAction = new StockTransActionDTO();

        StockTotalIm1DTO stockTotalDTO = mock(StockTotalIm1DTO.class);
        List<StockTotalIm1DTO> lstStockTotal = Lists.newArrayList(stockTotalDTO);


        when(stockTotalIm1Service.findStockTotal(any())).thenReturn(lstStockTotal);
        when(stockTotalIm1DTO.getStockModelId()).thenReturn(1L);
        when(stockTotalIm1DTO.getQuantityIssue()).thenReturn(-1L);
        when(stockTotalIm1DTO.getQuantity()).thenReturn(-1L);
        when(stockTotalDTO.getQuantityIssue()).thenReturn(-1L);
        when(stockTotalDTO.getQuantity()).thenReturn(1L);
        baseStockIm1Service.doSaveStockTotal(stockTotalAuditDTO, lstStockTotalDTO, stockTransAction);
    }

    @Test(expected = Exception.class)
    public void testDoSaveStockTotal1_7() throws Exception {
        //in 197 199
        StockTotalAuditIm1DTO stockTotalAuditDTO = new StockTotalAuditIm1DTO();
        StockTotalIm1DTO stockTotalIm1DTO = mock(StockTotalIm1DTO.class);
        List<StockTotalIm1DTO> lstStockTotalDTO = Lists.newArrayList(stockTotalIm1DTO);
        StockTransActionDTO stockTransAction = new StockTransActionDTO();

        StockTotalIm1DTO stockTotalDTO = mock(StockTotalIm1DTO.class);
        List<StockTotalIm1DTO> lstStockTotal = Lists.newArrayList(stockTotalDTO);
        ProductOfferingDTO offeringDTO = new ProductOfferingDTO();

        when(stockTotalIm1Service.findStockTotal(any())).thenReturn(lstStockTotal);
        when(stockTotalIm1DTO.getStockModelId()).thenReturn(1L);
        when(stockTotalIm1DTO.getQuantityIssue()).thenReturn(1L);
        when(stockTotalIm1DTO.getQuantity()).thenReturn(-1L);
        when(stockTotalDTO.getQuantityIssue()).thenReturn(-1L);
        when(stockTotalDTO.getQuantity()).thenReturn(-1L);
        when(productOfferingService.findOne(anyLong())).thenReturn(offeringDTO);
        baseStockIm1Service.doSaveStockTotal(stockTotalAuditDTO, lstStockTotalDTO, stockTransAction);
    }

    @Test(expected = Exception.class)
    public void testDoSaveStockTotal1_8() throws Exception {
        //out 199
        StockTotalAuditIm1DTO stockTotalAuditDTO = new StockTotalAuditIm1DTO();
        StockTotalIm1DTO stockTotalIm1DTO = mock(StockTotalIm1DTO.class);
        List<StockTotalIm1DTO> lstStockTotalDTO = Lists.newArrayList(stockTotalIm1DTO);
        StockTransActionDTO stockTransAction = new StockTransActionDTO();

        StockTotalIm1DTO stockTotalDTO = mock(StockTotalIm1DTO.class);
        List<StockTotalIm1DTO> lstStockTotal = Lists.newArrayList(stockTotalDTO);
        ProductOfferingDTO offeringDTO = null;

        when(stockTotalIm1Service.findStockTotal(any())).thenReturn(lstStockTotal);
        when(stockTotalIm1DTO.getStockModelId()).thenReturn(1L);
        when(stockTotalIm1DTO.getQuantityIssue()).thenReturn(1L);
        when(stockTotalIm1DTO.getQuantity()).thenReturn(-1L);
        when(stockTotalDTO.getQuantityIssue()).thenReturn(-1L);
        when(stockTotalDTO.getQuantity()).thenReturn(-1L);
        when(productOfferingService.findOne(anyLong())).thenReturn(offeringDTO);
        baseStockIm1Service.doSaveStockTotal(stockTotalAuditDTO, lstStockTotalDTO, stockTransAction);
    }

    @Test(expected = Exception.class)
    public void testDoSaveStockTotal1_9() throws Exception {
        //in 225
        StockTotalAuditIm1DTO stockTotalAuditDTO = null;
        StockTotalIm1DTO stockTotalIm1DTO = mock(StockTotalIm1DTO.class);
        List<StockTotalIm1DTO> lstStockTotalDTO = Lists.newArrayList(stockTotalIm1DTO);
        StockTransActionDTO stockTransAction = new StockTransActionDTO();

        StockTotalIm1DTO stockTotalDTO = mock(StockTotalIm1DTO.class);
        List<StockTotalIm1DTO> lstStockTotal = Lists.newArrayList();
        ProductOfferingDTO offeringDTO = new ProductOfferingDTO();

        when(stockTotalIm1Service.findStockTotal(any())).thenReturn(lstStockTotal);
        when(stockTotalIm1DTO.getStockModelId()).thenReturn(1L);
        when(stockTotalIm1DTO.getQuantityIssue()).thenReturn(-11L);
        when(stockTotalIm1DTO.getQuantity()).thenReturn(-11L);
        when(stockTotalDTO.getQuantityIssue()).thenReturn(-1L);
        when(stockTotalDTO.getQuantity()).thenReturn(-1L);
        when(productOfferingService.findOne(anyLong())).thenReturn(offeringDTO);
        baseStockIm1Service.doSaveStockTotal(stockTotalAuditDTO, lstStockTotalDTO, stockTransAction);
    }

    @Test
    public void testDoSaveStockTotal1_10() throws Exception {
        //out 230
        StockTotalAuditIm1DTO stockTotalAuditDTO = null;
        StockTotalIm1DTO stockTotalIm1DTO = mock(StockTotalIm1DTO.class);
        List<StockTotalIm1DTO> lstStockTotalDTO = Lists.newArrayList(stockTotalIm1DTO);
        StockTransActionDTO stockTransAction = new StockTransActionDTO();

        StockTotalIm1DTO stockTotalDTO = mock(StockTotalIm1DTO.class);
        List<StockTotalIm1DTO> lstStockTotal = Lists.newArrayList();
        ProductOfferingDTO offeringDTO = null;

        when(stockTotalIm1Service.findStockTotal(any())).thenReturn(lstStockTotal);
        when(stockTotalIm1DTO.getStockModelId()).thenReturn(1L);
        when(stockTotalIm1DTO.getQuantityIssue()).thenReturn(-11L);
        when(stockTotalIm1DTO.getQuantity()).thenReturn(-11L);
        when(stockTotalDTO.getQuantityIssue()).thenReturn(-1L);
        when(stockTotalDTO.getQuantity()).thenReturn(-1L);
        when(productOfferingService.findOne(anyLong())).thenReturn(offeringDTO);
        baseStockIm1Service.doSaveStockTotal(stockTotalAuditDTO, lstStockTotalDTO, stockTransAction);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method BaseStockIm1Service.doSaveStockTotalAudit (protected)
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testDoSaveStockTotalAudit_1() throws Exception {
        //in 265
        StockTotalAuditIm1DTO stockTotalAuditDTO = null;
        baseStockIm1Service.doSaveStockTotalAudit(stockTotalAuditDTO);
    }

    @Test
    public void testDoSaveStockTotalAudit_2() throws Exception {
        //out 265
        StockTotalAuditIm1DTO stockTotalAuditDTO = new StockTotalAuditIm1DTO();

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStaffCode("cd");
        staffDTO.setName("nm");
        ShopDTO shopDTO = null;
        when(staffService.findOne(anyLong())).thenReturn(staffDTO);
        when(shopService.findOne(anyLong())).thenReturn(shopDTO);
        baseStockIm1Service.doSaveStockTotalAudit(stockTotalAuditDTO);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method BaseStockIm1Service.doSaveStockGoods
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testDoSaveStockGoods_1() throws Exception {
        //in 282
        StockTransDTO stockTransDTO = new StockTransDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("1");

        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test
    public void testDoSaveStockGoods_2() throws Exception {
        //out 282
        //in 285
        StockTransDTO stockTransDTO = new StockTransDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(null);

        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test
    public void testDoSaveStockGoods_3() throws Exception {
        //out 282
        //in 285
        StockTransDTO stockTransDTO = new StockTransDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(null);

        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test
    public void testDoSaveStockGoods_4() throws Exception {
        //out 285
        //in 289
        StockTransDTO stockTransDTO = new StockTransDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        stockTransDTO.setExchangeStockId(1L);

        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test
    public void testDoSaveStockGoods_5() throws Exception {
        //out 289
        //in 296, 297
        StockTransDTO stockTransDTO = new StockTransDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        stockTransDTO.setExchangeStockId(null);
        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setToOwnerId(2L);
        flagStockDTO.setUpdateOwnerId(true);
        stockTransDTO.setRegionStockId(null);

        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test
    public void testDoSaveStockGoods_6() throws Exception {
        //out 289
        //in 296, 297
        StockTransDTO stockTransDTO = new StockTransDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        stockTransDTO.setExchangeStockId(null);
        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setToOwnerId(2L);
        flagStockDTO.setUpdateOwnerId(true);
        stockTransDTO.setRegionStockId(1L);
        flagStockDTO.setStatusForAgent("3");

        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test
    public void testDoSaveStockGoods_7() throws Exception {
        //out 289
        //in 296, 297
        StockTransDTO stockTransDTO = new StockTransDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        stockTransDTO.setExchangeStockId(null);
        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setToOwnerId(2L);
        flagStockDTO.setUpdateOwnerId(true);
        stockTransDTO.setRegionStockId(1L);
        flagStockDTO.setStatusForAgent("4");
        flagStockDTO.setShopId(72L);

        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test
    public void testDoSaveStockGoods_8() throws Exception {
        //out 297
        StockTransDTO stockTransDTO = new StockTransDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        stockTransDTO.setExchangeStockId(null);
        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setToOwnerId(2L);
        flagStockDTO.setUpdateOwnerId(true);

        stockTransDTO.setRegionStockId(1L);
        flagStockDTO.setStatusForAgent("4");
        flagStockDTO.setShopId(7282L);

        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }


    @Test
    public void testDoSaveStockGoods_9() throws Exception {
        //out 296
        StockTransDTO stockTransDTO = new StockTransDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        stockTransDTO.setExchangeStockId(null);
        stockTransDTO.setToOwnerType(null);
        stockTransDTO.setToOwnerId(2L);
        flagStockDTO.setUpdateOwnerId(true);

        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test
    public void testDoSaveStockGoods_10() throws Exception {
        //out 296
        StockTransDTO stockTransDTO = new StockTransDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        stockTransDTO.setExchangeStockId(null);

        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setToOwnerId(null);

        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test
    public void testDoSaveStockGoods_11() throws Exception {
        //out 296
        StockTransDTO stockTransDTO = new StockTransDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        stockTransDTO.setExchangeStockId(null);

        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setToOwnerId(1L);
        flagStockDTO.setUpdateOwnerId(false);

        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test
    public void testDoSaveStockGoods_12() throws Exception {
        //in 309
        StockTransDTO stockTransDTO = new StockTransDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        stockTransDTO.setExchangeStockId(null);
        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setToOwnerId(1L);
        flagStockDTO.setUpdateOwnerId(false);

        stockTransDTO.setStatus("3");
        stockTransDTO.setRegionStockId(1L);

        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test
    public void testDoSaveStockGoods_13() throws Exception {
        //in 309
        //out 318
        StockTransDTO stockTransDTO = new StockTransDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        flagStockDTO.setUpdateOwnerId(false);
        stockTransDTO.setExchangeStockId(null);
        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setToOwnerId(1L);

        stockTransDTO.setStatus("1");
        flagStockDTO.setStatusForAgent("3");
        stockTransDTO.setRegionStockId(1L);
        flagStockDTO.setShopId(7282L);

        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test
    public void testDoSaveStockGoods_14() throws Exception {
        //in 309, 318
        StockTransDTO stockTransDTO = new StockTransDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        flagStockDTO.setUpdateOwnerId(false);
        stockTransDTO.setExchangeStockId(null);
        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setToOwnerId(1L);

        stockTransDTO.setStatus("6");
        flagStockDTO.setStatusForAgent("1");
        stockTransDTO.setRegionStockId(1L);
        flagStockDTO.setShopId(7282L);

        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test
    public void testDoSaveStockGoods_16() throws Exception {
        //in 309, 318
        StockTransDTO stockTransDTO = new StockTransDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        flagStockDTO.setUpdateOwnerId(false);
        stockTransDTO.setExchangeStockId(null);
        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setToOwnerId(1L);

        stockTransDTO.setStatus("3");
        flagStockDTO.setStatusForAgent("1");
        stockTransDTO.setRegionStockId(1L);
        flagStockDTO.setShopId(82L);

        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test
    public void testDoSaveStockGoods_17() throws Exception {
        //out 309
        StockTransDTO stockTransDTO = new StockTransDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        flagStockDTO.setUpdateOwnerId(false);
        stockTransDTO.setExchangeStockId(null);
        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setToOwnerId(1L);

        stockTransDTO.setStatus("2");
        flagStockDTO.setStatusForAgent("1");

        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test
    public void testDoSaveStockGoods_18() throws Exception {
        //out 309
        //in 328, 329
        StockTransDTO stockTransDTO = new StockTransDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        flagStockDTO.setUpdateOwnerId(false);
        stockTransDTO.setExchangeStockId(null);
        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setToOwnerId(1L);

        stockTransDTO.setStatus("2");
        flagStockDTO.setStatusForAgent("1");
        stockTransDTO.setRegionStockId(null);

        flagStockDTO.setUpdateRescue(true);
        flagStockDTO.setStateId(1L);
        flagStockDTO.setNote("STOCK");

        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test
    public void testDoSaveStockGoods_19() throws Exception {
        //out 329
        //in 335
        StockTransDTO stockTransDTO = new StockTransDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        flagStockDTO.setUpdateOwnerId(false);
        stockTransDTO.setExchangeStockId(null);
        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setToOwnerId(1L);

        stockTransDTO.setStatus("2");
        flagStockDTO.setStatusForAgent("1");
        stockTransDTO.setRegionStockId(null);

        flagStockDTO.setUpdateRescue(true);
        flagStockDTO.setStateId(1L);
        flagStockDTO.setNote("RETRIEVE_STOCK");
        stockTransDTO.setFromOwnerId(1L);
        stockTransDTO.setToOwnerId(1L);

        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test
    public void testDoSaveStockGoods_20() throws Exception {
        //out 335
        StockTransDTO stockTransDTO = new StockTransDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        flagStockDTO.setUpdateOwnerId(false);
        stockTransDTO.setExchangeStockId(null);
        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setToOwnerId(1L);

        stockTransDTO.setStatus("2");
        flagStockDTO.setStatusForAgent("1");
        stockTransDTO.setRegionStockId(null);

        flagStockDTO.setUpdateRescue(true);
        flagStockDTO.setStateId(1L);
        flagStockDTO.setNote("RETRIEVE_STOCK");
        stockTransDTO.setFromOwnerId(null);

        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test
    public void testDoSaveStockGoods_21() throws Exception {
        //out 335
        StockTransDTO stockTransDTO = new StockTransDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        flagStockDTO.setUpdateOwnerId(false);
        stockTransDTO.setExchangeStockId(null);
        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setToOwnerId(1L);

        stockTransDTO.setStatus("2");
        flagStockDTO.setStatusForAgent("1");
        stockTransDTO.setRegionStockId(null);

        flagStockDTO.setUpdateRescue(true);
        flagStockDTO.setStateId(1L);
        flagStockDTO.setNote("RETRIEVE_STOCK");
        stockTransDTO.setFromOwnerId(1L);
        stockTransDTO.setToOwnerId(null);

        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test
    public void testDoSaveStockGoods_22() throws Exception {
        //out 328
        StockTransDTO stockTransDTO = new StockTransDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        flagStockDTO.setUpdateOwnerId(false);
        stockTransDTO.setExchangeStockId(null);
        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setToOwnerId(1L);

        stockTransDTO.setStatus("2");
        flagStockDTO.setStatusForAgent("1");
        stockTransDTO.setRegionStockId(null);

        flagStockDTO.setUpdateRescue(false);

        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test
    public void testDoSaveStockGoods_23() throws Exception {
        //out 328
        StockTransDTO stockTransDTO = new StockTransDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        flagStockDTO.setUpdateOwnerId(false);
        stockTransDTO.setExchangeStockId(null);
        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setToOwnerId(1L);

        stockTransDTO.setStatus("2");
        flagStockDTO.setStatusForAgent("1");
        stockTransDTO.setRegionStockId(null);

        flagStockDTO.setUpdateRescue(true);
        flagStockDTO.setStateId(0L);

        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test(expected = Exception.class)
    public void testDoSaveStockGoods_24() throws Exception {
        //IN 349
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDetailDTO stockTransDetail = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        flagStockDTO.setUpdateOwnerId(false);
        stockTransDTO.setExchangeStockId(null);
        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setToOwnerId(1L);

        stockTransDTO.setStatus("2");
        flagStockDTO.setStatusForAgent("1");
        stockTransDTO.setRegionStockId(null);

        flagStockDTO.setUpdateRescue(true);
        flagStockDTO.setStateId(0L);

        when(stockTransDetail.getProdOfferId()).thenReturn(null);
        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test
    public void testDoSaveStockGoods_25() throws Exception {
        //out 349
        //in 356, 371
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDetailDTO stockTransDetail = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        flagStockDTO.setUpdateOwnerId(false);
        stockTransDTO.setExchangeStockId(null);
        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setToOwnerId(1L);

        stockTransDTO.setStatus("2");
        flagStockDTO.setStatusForAgent("1");
        stockTransDTO.setRegionStockId(null);

        flagStockDTO.setUpdateRescue(true);
        flagStockDTO.setStateId(0L);

        StockTypeIm1DTO stockTypeIm1DTO = new StockTypeIm1DTO();
        stockTypeIm1DTO.setTableName("KCS");
        flagStockDTO.setUpdateSaleDate(true);

        when(stockTransDetail.getProdOfferId()).thenReturn(1L);
        when(stockTotalIm1Service.findOneStockType(anyLong())).thenReturn(stockTypeIm1DTO);
        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test(expected = Exception.class)
    public void testDoSaveStockGoods_26() throws Exception {
        //out 356
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDetailDTO stockTransDetail = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        flagStockDTO.setUpdateOwnerId(false);
        stockTransDTO.setExchangeStockId(null);
        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setToOwnerId(1L);

        stockTransDTO.setStatus("2");
        flagStockDTO.setStatusForAgent("1");
        stockTransDTO.setRegionStockId(null);

        flagStockDTO.setUpdateRescue(true);
        flagStockDTO.setStateId(0L);

        StockTypeIm1DTO stockTypeIm1DTO = null;

        when(stockTransDetail.getProdOfferId()).thenReturn(1L);
        when(stockTotalIm1Service.findOneStockType(anyLong())).thenReturn(stockTypeIm1DTO);
        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test
    public void testDoSaveStockGoods_27() throws Exception {
        //in 364
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDetailDTO stockTransDetail = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        flagStockDTO.setUpdateOwnerId(false);
        stockTransDTO.setExchangeStockId(null);
        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setToOwnerId(1L);

        stockTransDTO.setStatus("2");
        flagStockDTO.setStatusForAgent("1");
        stockTransDTO.setRegionStockId(null);

        flagStockDTO.setUpdateRescue(true);
        flagStockDTO.setStateId(0L);

        StockTypeIm1DTO stockTypeIm1DTO = new StockTypeIm1DTO();
        stockTypeIm1DTO.setTableName("");

        when(stockTransDetail.getProdOfferId()).thenReturn(1L);
        when(stockTotalIm1Service.findOneStockType(anyLong())).thenReturn(stockTypeIm1DTO);
        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test
    public void testDoSaveStockGoods_28() throws Exception {
        //out 371
        //in 374
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDetailDTO stockTransDetail = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        flagStockDTO.setUpdateOwnerId(false);
        stockTransDTO.setExchangeStockId(null);

        stockTransDTO.setStatus("2");
        flagStockDTO.setStatusForAgent("1");
        stockTransDTO.setRegionStockId(null);

        flagStockDTO.setUpdateRescue(true);
        flagStockDTO.setStateId(0L);

        StockTypeIm1DTO stockTypeIm1DTO = new StockTypeIm1DTO();
        stockTypeIm1DTO.setTableName("KCS");
        flagStockDTO.setUpdateSaleDate(false);

        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setToOwnerId(1L);
        flagStockDTO.setUpdateOwnerId(true);

        when(stockTransDetail.getProdOfferId()).thenReturn(1L);
        when(stockTotalIm1Service.findOneStockType(anyLong())).thenReturn(stockTypeIm1DTO);
        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test
    public void testDoSaveStockGoods_29() throws Exception {
        //out 374
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDetailDTO stockTransDetail = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        flagStockDTO.setUpdateOwnerId(false);
        stockTransDTO.setExchangeStockId(null);

        stockTransDTO.setStatus("2");
        flagStockDTO.setStatusForAgent("1");
        stockTransDTO.setRegionStockId(null);

        flagStockDTO.setUpdateRescue(true);
        flagStockDTO.setStateId(0L);

        StockTypeIm1DTO stockTypeIm1DTO = new StockTypeIm1DTO();
        stockTypeIm1DTO.setTableName("KCS");
        flagStockDTO.setUpdateSaleDate(false);

        stockTransDTO.setToOwnerType(null);
        stockTransDTO.setToOwnerId(1L);
        flagStockDTO.setUpdateOwnerId(true);

        when(stockTransDetail.getProdOfferId()).thenReturn(1L);
        when(stockTotalIm1Service.findOneStockType(anyLong())).thenReturn(stockTypeIm1DTO);
        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test
    public void testDoSaveStockGoods_30() throws Exception {
        //out 374
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDetailDTO stockTransDetail = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        flagStockDTO.setUpdateOwnerId(false);
        stockTransDTO.setExchangeStockId(null);

        stockTransDTO.setStatus("2");
        flagStockDTO.setStatusForAgent("1");
        stockTransDTO.setRegionStockId(null);

        flagStockDTO.setUpdateRescue(true);
        flagStockDTO.setStateId(0L);

        StockTypeIm1DTO stockTypeIm1DTO = new StockTypeIm1DTO();
        stockTypeIm1DTO.setTableName("KCS");
        flagStockDTO.setUpdateSaleDate(false);

        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setToOwnerId(null);
        flagStockDTO.setUpdateOwnerId(true);

        when(stockTransDetail.getProdOfferId()).thenReturn(1L);
        when(stockTotalIm1Service.findOneStockType(anyLong())).thenReturn(stockTypeIm1DTO);
        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test
    public void testDoSaveStockGoods_31() throws Exception {
        //out 374
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDetailDTO stockTransDetail = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        flagStockDTO.setUpdateOwnerId(false);
        stockTransDTO.setExchangeStockId(null);

        stockTransDTO.setStatus("2");
        flagStockDTO.setStatusForAgent("1");
        stockTransDTO.setRegionStockId(null);

        flagStockDTO.setUpdateRescue(true);
        flagStockDTO.setStateId(0L);

        StockTypeIm1DTO stockTypeIm1DTO = new StockTypeIm1DTO();
        stockTypeIm1DTO.setTableName("KCS");
        flagStockDTO.setUpdateSaleDate(false);

        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setToOwnerId(1L);
        flagStockDTO.setUpdateOwnerId(false);

        when(stockTransDetail.getProdOfferId()).thenReturn(1L);
        when(stockTotalIm1Service.findOneStockType(anyLong())).thenReturn(stockTypeIm1DTO);
        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test
    public void testDoSaveStockGoods_32() throws Exception {
        //in 380, 382
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDetailDTO stockTransDetail = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        flagStockDTO.setUpdateOwnerId(false);
        stockTransDTO.setExchangeStockId(null);

        stockTransDTO.setStatus("2");
        flagStockDTO.setStatusForAgent("1");
        stockTransDTO.setRegionStockId(null);

        flagStockDTO.setUpdateRescue(true);
        flagStockDTO.setStateId(0L);

        StockTypeIm1DTO stockTypeIm1DTO = new StockTypeIm1DTO();
        stockTypeIm1DTO.setTableName("KCS");
        flagStockDTO.setUpdateSaleDate(false);

        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setToOwnerId(1L);
        flagStockDTO.setUpdateOwnerId(false);

        flagStockDTO.setUpdateAccountBalance(true);
        stockTransDTO.setBankplusStatus(0L);

        when(stockTransDetail.getProdOfferId()).thenReturn(1L);
        when(stockTotalIm1Service.findOneStockType(anyLong())).thenReturn(stockTypeIm1DTO);
        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test
    public void testDoSaveStockGoods_33() throws Exception {
        //out 382
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDetailDTO stockTransDetail = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        flagStockDTO.setUpdateOwnerId(false);
        stockTransDTO.setExchangeStockId(null);

        stockTransDTO.setStatus("2");
        flagStockDTO.setStatusForAgent("1");
        stockTransDTO.setRegionStockId(null);

        flagStockDTO.setUpdateRescue(true);
        flagStockDTO.setStateId(0L);

        StockTypeIm1DTO stockTypeIm1DTO = new StockTypeIm1DTO();
        stockTypeIm1DTO.setTableName("KCS");
        flagStockDTO.setUpdateSaleDate(false);

        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setToOwnerId(1L);
        flagStockDTO.setUpdateOwnerId(false);

        flagStockDTO.setUpdateAccountBalance(true);
        stockTransDTO.setBankplusStatus(1L);

        when(stockTransDetail.getProdOfferId()).thenReturn(1L);
        when(stockTotalIm1Service.findOneStockType(anyLong())).thenReturn(stockTypeIm1DTO);
        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test
    public void testDoSaveStockGoods_34() throws Exception {
        //out 382
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDetailDTO stockTransDetail = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        flagStockDTO.setUpdateOwnerId(false);
        stockTransDTO.setExchangeStockId(null);

        stockTransDTO.setStatus("2");
        flagStockDTO.setStatusForAgent("1");
        stockTransDTO.setRegionStockId(null);

        flagStockDTO.setUpdateRescue(true);
        flagStockDTO.setStateId(0L);

        StockTypeIm1DTO stockTypeIm1DTO = new StockTypeIm1DTO();
        stockTypeIm1DTO.setTableName("KCS");
        flagStockDTO.setUpdateSaleDate(false);

        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setToOwnerId(1L);
        flagStockDTO.setUpdateOwnerId(false);

        flagStockDTO.setUpdateAccountBalance(false);

        when(stockTransDetail.getProdOfferId()).thenReturn(1L);
        when(stockTotalIm1Service.findOneStockType(anyLong())).thenReturn(stockTypeIm1DTO);
        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test
    public void testDoSaveStockGoods_35() throws Exception {
        //out 382
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDetailDTO stockTransDetail = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        flagStockDTO.setUpdateOwnerId(false);
        stockTransDTO.setExchangeStockId(null);

        stockTransDTO.setStatus("2");
        flagStockDTO.setStatusForAgent("1");
        stockTransDTO.setRegionStockId(null);

        flagStockDTO.setUpdateRescue(true);
        flagStockDTO.setStateId(0L);

        StockTypeIm1DTO stockTypeIm1DTO = new StockTypeIm1DTO();
        stockTypeIm1DTO.setTableName("KCS");
        flagStockDTO.setUpdateSaleDate(false);

        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setToOwnerId(1L);
        flagStockDTO.setUpdateOwnerId(false);

        flagStockDTO.setUpdateAccountBalance(true);
        stockTransDTO.setBankplusStatus(null);

        when(stockTransDetail.getProdOfferId()).thenReturn(1L);
        when(stockTotalIm1Service.findOneStockType(anyLong())).thenReturn(stockTypeIm1DTO);
        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test
    public void testDoSaveStockGoods_36() throws Exception {
        //in 387,390,393
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDetailDTO stockTransDetail = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        flagStockDTO.setUpdateOwnerId(false);
        stockTransDTO.setExchangeStockId(null);

        stockTransDTO.setStatus("2");
        flagStockDTO.setStatusForAgent("1");
        stockTransDTO.setRegionStockId(null);

        flagStockDTO.setUpdateRescue(true);
        flagStockDTO.setStateId(0L);

        StockTypeIm1DTO stockTypeIm1DTO = new StockTypeIm1DTO();
        stockTypeIm1DTO.setTableName("KCS");
        flagStockDTO.setUpdateSaleDate(false);

        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setToOwnerId(1L);
        flagStockDTO.setUpdateOwnerId(false);

        flagStockDTO.setUpdateAccountBalance(false);
        stockTransDTO.setBankplusStatus(null);

        when(stockTransDetail.getDepositPrice()).thenReturn(1L);//and isUpdateDepositStatus=false
        flagStockDTO.setStateIdForReasonId(1L);
        flagStockDTO.setUpdateProdOfferId(true);

        when(stockTransDetail.getProdOfferId()).thenReturn(1L);
        when(stockTotalIm1Service.findOneStockType(anyLong())).thenReturn(stockTypeIm1DTO);
        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test
    public void testDoSaveStockGoods_37() throws Exception {
        //out 387,390,393
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDetailDTO stockTransDetail = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        flagStockDTO.setUpdateOwnerId(false);
        stockTransDTO.setExchangeStockId(null);

        stockTransDTO.setStatus("2");
        flagStockDTO.setStatusForAgent("1");
        stockTransDTO.setRegionStockId(null);

        flagStockDTO.setUpdateRescue(true);
        flagStockDTO.setStateId(0L);

        StockTypeIm1DTO stockTypeIm1DTO = new StockTypeIm1DTO();
        stockTypeIm1DTO.setTableName("KCS");
        flagStockDTO.setUpdateSaleDate(false);

        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setToOwnerId(1L);
        flagStockDTO.setUpdateOwnerId(false);

        flagStockDTO.setUpdateAccountBalance(false);
        stockTransDTO.setBankplusStatus(null);

        when(stockTransDetail.getDepositPrice()).thenReturn(0L);//and isUpdateDepositStatus=false
        flagStockDTO.setStateIdForReasonId(0L);
        flagStockDTO.setUpdateProdOfferId(false);

        when(stockTransDetail.getProdOfferId()).thenReturn(1L);
        when(stockTotalIm1Service.findOneStockType(anyLong())).thenReturn(stockTypeIm1DTO);
        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test
    public void testDoSaveStockGoods_38() throws Exception {
        //out 387,390,393
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDetailDTO stockTransDetail = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        flagStockDTO.setUpdateOwnerId(false);
        stockTransDTO.setExchangeStockId(null);

        stockTransDTO.setStatus("2");
        flagStockDTO.setStatusForAgent("1");
        stockTransDTO.setRegionStockId(null);

        StockTypeIm1DTO stockTypeIm1DTO = new StockTypeIm1DTO();
        stockTypeIm1DTO.setTableName("KCS");
        flagStockDTO.setUpdateSaleDate(false);

        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setToOwnerId(1L);
        flagStockDTO.setUpdateOwnerId(false);

        flagStockDTO.setUpdateAccountBalance(true);
        stockTransDTO.setBankplusStatus(0L);

        when(stockTransDetail.getDepositPrice()).thenReturn(2L);//and isUpdateDepositStatus=false
        flagStockDTO.setStateIdForReasonId(0L);
        flagStockDTO.setUpdateProdOfferId(false);

        flagStockDTO.setUpdateRescue(true);
        flagStockDTO.setStateId(0L);

        when(stockTransDetail.getProdOfferId()).thenReturn(1L);
        when(stockTotalIm1Service.findOneStockType(anyLong())).thenReturn(stockTypeIm1DTO);
        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test
    public void testDoSaveStockGoods_39() throws Exception {
        //in 398,399,402
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDetailDTO stockTransDetail = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        flagStockDTO.setUpdateOwnerId(false);
        stockTransDTO.setExchangeStockId(null);

        stockTransDTO.setStatus("2");
        flagStockDTO.setStatusForAgent("1");
        stockTransDTO.setRegionStockId(null);

        StockTypeIm1DTO stockTypeIm1DTO = new StockTypeIm1DTO();
        stockTypeIm1DTO.setTableName("KCS");
        flagStockDTO.setUpdateSaleDate(false);

        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setToOwnerId(1L);
        flagStockDTO.setUpdateOwnerId(false);

        flagStockDTO.setUpdateAccountBalance(true);
        stockTransDTO.setBankplusStatus(0L);

        when(stockTransDetail.getDepositPrice()).thenReturn(2L);//and isUpdateDepositStatus=false
        flagStockDTO.setStateIdForReasonId(0L);
        flagStockDTO.setUpdateProdOfferId(false);

        flagStockDTO.setUpdateRescue(true);
        flagStockDTO.setNote("RETRIEVE_STOCK");
        flagStockDTO.setStateId(1L);

        when(stockTransDetail.getProdOfferId()).thenReturn(1L);
        when(stockTotalIm1Service.findOneStockType(anyLong())).thenReturn(stockTypeIm1DTO);
        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test
    public void testDoSaveStockGoods_40() throws Exception {
        //out 402
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDetailDTO stockTransDetail = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        flagStockDTO.setUpdateOwnerId(false);
        stockTransDTO.setExchangeStockId(null);
        stockTransDTO.setProcessOffline("2");

        stockTransDTO.setStatus("2");
        flagStockDTO.setStatusForAgent("1");
        stockTransDTO.setRegionStockId(null);

        StockTypeIm1DTO stockTypeIm1DTO = new StockTypeIm1DTO();
        stockTypeIm1DTO.setTableName("KCS");
        flagStockDTO.setUpdateSaleDate(false);

        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setToOwnerId(1L);
        flagStockDTO.setUpdateOwnerId(false);

        flagStockDTO.setUpdateAccountBalance(true);
        stockTransDTO.setBankplusStatus(0L);

        when(stockTransDetail.getDepositPrice()).thenReturn(2L);//and isUpdateDepositStatus=false
        flagStockDTO.setStateIdForReasonId(0L);
        flagStockDTO.setUpdateProdOfferId(false);

        flagStockDTO.setUpdateRescue(true);
        flagStockDTO.setNote("RETRIEVE_STOCK");
        flagStockDTO.setStateId(1L);
//        when(flagStockDTO.getStateId()).thenReturn(1L).thenReturn(0L);

        when(stockTransDetail.getProdOfferId()).thenReturn(1L);
        when(stockTotalIm1Service.findOneStockType(anyLong())).thenReturn(stockTypeIm1DTO);
        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test
    public void testDoSaveStockGoods_41() throws Exception {
        //out 399
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDetailDTO stockTransDetail = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        flagStockDTO.setUpdateOwnerId(false);
        stockTransDTO.setExchangeStockId(null);

        stockTransDTO.setStatus("2");
        flagStockDTO.setStatusForAgent("1");
        stockTransDTO.setRegionStockId(null);

        StockTypeIm1DTO stockTypeIm1DTO = new StockTypeIm1DTO();
        stockTypeIm1DTO.setTableName("KCS");
        flagStockDTO.setUpdateSaleDate(false);

        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setToOwnerId(1L);
        flagStockDTO.setUpdateOwnerId(false);

        flagStockDTO.setUpdateAccountBalance(true);
        stockTransDTO.setBankplusStatus(0L);

        when(stockTransDetail.getDepositPrice()).thenReturn(2L);//and isUpdateDepositStatus=false
        flagStockDTO.setStateIdForReasonId(0L);
        flagStockDTO.setUpdateProdOfferId(false);

        flagStockDTO.setUpdateRescue(true);
        flagStockDTO.setNote("RETRIEVE");
        flagStockDTO.setStateId(null);

        when(stockTransDetail.getProdOfferId()).thenReturn(1L);
        when(stockTotalIm1Service.findOneStockType(anyLong())).thenReturn(stockTypeIm1DTO);
        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test
    public void testDoSaveStockGoods_42() throws Exception {
        //out 398
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDetailDTO stockTransDetail = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        flagStockDTO.setUpdateOwnerId(false);
        stockTransDTO.setExchangeStockId(null);

        stockTransDTO.setStatus("2");
        flagStockDTO.setStatusForAgent("1");
        stockTransDTO.setRegionStockId(null);

        StockTypeIm1DTO stockTypeIm1DTO = new StockTypeIm1DTO();
        stockTypeIm1DTO.setTableName("KCS");
        flagStockDTO.setUpdateSaleDate(false);

        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setToOwnerId(1L);
        flagStockDTO.setUpdateOwnerId(false);

        flagStockDTO.setUpdateAccountBalance(true);
        stockTransDTO.setBankplusStatus(0L);

        when(stockTransDetail.getDepositPrice()).thenReturn(2L);//and isUpdateDepositStatus=false
        flagStockDTO.setStateIdForReasonId(0L);
        flagStockDTO.setUpdateProdOfferId(false);

        flagStockDTO.setUpdateRescue(false);
        flagStockDTO.setNote("RETRIEVE");

        when(stockTransDetail.getProdOfferId()).thenReturn(1L);
        when(stockTotalIm1Service.findOneStockType(anyLong())).thenReturn(stockTypeIm1DTO);
        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test
    public void testDoSaveStockGoods_43() throws Exception {
        //out 398
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDetailDTO stockTransDetail = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        flagStockDTO.setUpdateOwnerId(false);
        stockTransDTO.setExchangeStockId(null);

        stockTransDTO.setStatus("2");
        flagStockDTO.setStatusForAgent("1");
        stockTransDTO.setRegionStockId(null);

        StockTypeIm1DTO stockTypeIm1DTO = new StockTypeIm1DTO();
        stockTypeIm1DTO.setTableName("KCS");
        flagStockDTO.setUpdateSaleDate(false);

        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setToOwnerId(1L);
        flagStockDTO.setUpdateOwnerId(false);

        flagStockDTO.setUpdateAccountBalance(true);
        stockTransDTO.setBankplusStatus(0L);

        when(stockTransDetail.getDepositPrice()).thenReturn(2L);//and isUpdateDepositStatus=false
        flagStockDTO.setStateIdForReasonId(0L);
        flagStockDTO.setUpdateProdOfferId(false);

        flagStockDTO.setUpdateRescue(true);
        flagStockDTO.setNote("RETRIEVE");
        flagStockDTO.setStateId(null);

        when(stockTransDetail.getProdOfferId()).thenReturn(1L);
        when(stockTotalIm1Service.findOneStockType(anyLong())).thenReturn(stockTypeIm1DTO);
        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test
    public void testDoSaveStockGoods_44() throws Exception {
        //in 408,409
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDetailDTO stockTransDetail = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        flagStockDTO.setUpdateOwnerId(false);
        stockTransDTO.setExchangeStockId(null);

        stockTransDTO.setStatus("2");
        flagStockDTO.setStatusForAgent("1");
        stockTransDTO.setRegionStockId(null);

        StockTypeIm1DTO stockTypeIm1DTO = new StockTypeIm1DTO();
        stockTypeIm1DTO.setTableName("KCS");
        flagStockDTO.setUpdateSaleDate(false);

        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setToOwnerId(1L);
        flagStockDTO.setUpdateOwnerId(false);

        flagStockDTO.setUpdateAccountBalance(true);
        stockTransDTO.setBankplusStatus(0L);

        when(stockTransDetail.getDepositPrice()).thenReturn(2L);//and isUpdateDepositStatus=false
        flagStockDTO.setStateIdForReasonId(0L);
        flagStockDTO.setUpdateProdOfferId(false);

        flagStockDTO.setUpdateDamageProduct(false);
        flagStockDTO.setUpdateRescue(true);
        flagStockDTO.setNote("RETRIEVE_STOCK");
        flagStockDTO.setStateId(null);

        when(stockTransDetail.getProdOfferId()).thenReturn(1L);
        when(stockTotalIm1Service.findOneStockType(anyLong())).thenReturn(stockTypeIm1DTO);
        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test
    public void testDoSaveStockGoods_45() throws Exception {
        //out 409
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDetailDTO stockTransDetail = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        flagStockDTO.setUpdateOwnerId(false);
        stockTransDTO.setExchangeStockId(null);

        stockTransDTO.setStatus("2");
        flagStockDTO.setStatusForAgent("1");
        stockTransDTO.setRegionStockId(null);

        StockTypeIm1DTO stockTypeIm1DTO = new StockTypeIm1DTO();
        stockTypeIm1DTO.setTableName("KCS");
        flagStockDTO.setUpdateSaleDate(false);

        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setToOwnerId(1L);
        flagStockDTO.setUpdateOwnerId(false);

        flagStockDTO.setUpdateAccountBalance(true);
        stockTransDTO.setBankplusStatus(0L);

        when(stockTransDetail.getDepositPrice()).thenReturn(2L);//and isUpdateDepositStatus=false
        flagStockDTO.setStateIdForReasonId(0L);
        flagStockDTO.setUpdateProdOfferId(false);

        flagStockDTO.setUpdateDamageProduct(false);
        flagStockDTO.setUpdateRescue(false);
        flagStockDTO.setNote("RETRIEVE_STOCK");
        flagStockDTO.setStateId(null);

        when(stockTransDetail.getProdOfferId()).thenReturn(1L);
        when(stockTotalIm1Service.findOneStockType(anyLong())).thenReturn(stockTypeIm1DTO);
        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test
    public void testDoSaveStockGoods_46() throws Exception {
        //out 409
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDetailDTO stockTransDetail = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        flagStockDTO.setUpdateOwnerId(false);
        stockTransDTO.setExchangeStockId(null);

        stockTransDTO.setStatus("2");
        flagStockDTO.setStatusForAgent("1");
        stockTransDTO.setRegionStockId(null);

        StockTypeIm1DTO stockTypeIm1DTO = new StockTypeIm1DTO();
        stockTypeIm1DTO.setTableName("KCS");
        flagStockDTO.setUpdateSaleDate(false);

        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setToOwnerId(1L);
        flagStockDTO.setUpdateOwnerId(false);

        flagStockDTO.setUpdateAccountBalance(true);
        stockTransDTO.setBankplusStatus(0L);

        when(stockTransDetail.getDepositPrice()).thenReturn(2L);//and isUpdateDepositStatus=false
        flagStockDTO.setStateIdForReasonId(0L);
        flagStockDTO.setUpdateProdOfferId(false);

        flagStockDTO.setUpdateDamageProduct(false);
        flagStockDTO.setUpdateRescue(true);
        flagStockDTO.setNote("RETCK");
        flagStockDTO.setStateId(null);

        when(stockTransDetail.getProdOfferId()).thenReturn(1L);
        when(stockTotalIm1Service.findOneStockType(anyLong())).thenReturn(stockTypeIm1DTO);
        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test
    public void testDoSaveStockGoods_47() throws Exception {
        //out 409
        //in 417
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDetailDTO stockTransDetail = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        flagStockDTO.setUpdateOwnerId(false);
        stockTransDTO.setExchangeStockId(null);

        stockTransDTO.setStatus("2");
        flagStockDTO.setStatusForAgent("1");
        stockTransDTO.setRegionStockId(null);

        StockTypeIm1DTO stockTypeIm1DTO = new StockTypeIm1DTO();
        stockTypeIm1DTO.setTableName("KCS");
        flagStockDTO.setUpdateSaleDate(false);

        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setToOwnerId(1L);
        flagStockDTO.setUpdateOwnerId(false);

        flagStockDTO.setUpdateAccountBalance(true);
        stockTransDTO.setBankplusStatus(0L);

        when(stockTransDetail.getDepositPrice()).thenReturn(2L);//and isUpdateDepositStatus=false
        flagStockDTO.setStateIdForReasonId(0L);
        flagStockDTO.setUpdateProdOfferId(false);

        flagStockDTO.setUpdateDamageProduct(true);
//        flagStockDTO.setUpdateRescue(true);
//        flagStockDTO.setNote("RETCK");
//        flagStockDTO.setStateId(null);

        when(stockTransDetail.getProdOfferTypeId()).thenReturn(7L);
        when(stockTransDetail.getProdOfferId()).thenReturn(1L);
        when(stockTotalIm1Service.findOneStockType(anyLong())).thenReturn(stockTypeIm1DTO);
        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test
    public void testDoSaveStockGoods_48() throws Exception {
        //out 417
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDetailDTO stockTransDetail = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        flagStockDTO.setUpdateOwnerId(false);
        stockTransDTO.setExchangeStockId(null);

        stockTransDTO.setStatus("2");
        flagStockDTO.setStatusForAgent("1");
        stockTransDTO.setRegionStockId(null);

        StockTypeIm1DTO stockTypeIm1DTO = new StockTypeIm1DTO();
        stockTypeIm1DTO.setTableName("KCS");
        flagStockDTO.setUpdateSaleDate(false);

        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setToOwnerId(1L);
        flagStockDTO.setUpdateOwnerId(false);

        flagStockDTO.setUpdateAccountBalance(true);
        stockTransDTO.setBankplusStatus(0L);

        when(stockTransDetail.getDepositPrice()).thenReturn(2L);//and isUpdateDepositStatus=false
        flagStockDTO.setStateIdForReasonId(0L);
        flagStockDTO.setUpdateProdOfferId(false);

        flagStockDTO.setUpdateDamageProduct(true);

        when(stockTransDetail.getProdOfferTypeId()).thenReturn(5L);
        when(stockTransDetail.getProdOfferId()).thenReturn(1L);
        when(stockTotalIm1Service.findOneStockType(anyLong())).thenReturn(stockTypeIm1DTO);
        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test(expected = Exception.class)
    public void testDoSaveStockGoods_49() throws Exception {
        //in 437,440,447
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDetailDTO stockTransDetail = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        flagStockDTO.setUpdateOwnerId(false);
        stockTransDTO.setExchangeStockId(null);

        stockTransDTO.setStatus("2");
        flagStockDTO.setStatusForAgent("1");
        stockTransDTO.setRegionStockId(null);

        StockTypeIm1DTO stockTypeIm1DTO = new StockTypeIm1DTO();
        stockTypeIm1DTO.setTableName("KCS");
        flagStockDTO.setUpdateSaleDate(false);

        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setToOwnerId(1L);
        flagStockDTO.setUpdateOwnerId(false);

        flagStockDTO.setUpdateAccountBalance(true);
        stockTransDTO.setBankplusStatus(0L);

        when(stockTransDetail.getDepositPrice()).thenReturn(2L);//and isUpdateDepositStatus=false
        flagStockDTO.setStateIdForReasonId(0L);
        flagStockDTO.setUpdateProdOfferId(false);
        flagStockDTO.setUpdateDamageProduct(true);

        StockTransSerialDTO stockTransSerialDTO = mock(StockTransSerialDTO.class);
        List<StockTransSerialDTO> stockTransSerialDTOList = Lists.newArrayList(stockTransSerialDTO);
        when(stockTransDetail.getLstStockTransSerial()).thenReturn(stockTransSerialDTOList);

        when(stockTransDetail.getProdOfferTypeId()).thenReturn(5L);
        when(stockTransSerialDTO.getFromSerial()).thenReturn("2");
        when(stockTransSerialDTO.getToSerial()).thenReturn("2");
        flagStockDTO.setObjectType("AGENT_EXPORT");
        stockTransDTO.setPayStatus("1");

        Query query = mock(Query.class);
        when(em.createNativeQuery(anyString())).thenReturn(query);

        when(stockTransDetail.getProdOfferId()).thenReturn(1L);
        when(stockTotalIm1Service.findOneStockType(anyLong())).thenReturn(stockTypeIm1DTO);
        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test(expected = Exception.class)
    public void testDoSaveStockGoods_50() throws Exception {
        //out 447
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDetailDTO stockTransDetail = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        flagStockDTO.setUpdateOwnerId(false);
        stockTransDTO.setExchangeStockId(null);

        StockTypeIm1DTO stockTypeIm1DTO = new StockTypeIm1DTO();
        stockTypeIm1DTO.setTableName("KCS");
        flagStockDTO.setUpdateSaleDate(false);

        StockTransSerialDTO stockTransSerialDTO = mock(StockTransSerialDTO.class);
        List<StockTransSerialDTO> stockTransSerialDTOList = Lists.newArrayList(stockTransSerialDTO);
        when(stockTransDetail.getLstStockTransSerial()).thenReturn(stockTransSerialDTOList);

        when(stockTransDetail.getProdOfferTypeId()).thenReturn(5L);
        when(stockTransSerialDTO.getFromSerial()).thenReturn("2");
        when(stockTransSerialDTO.getToSerial()).thenReturn("2");
        flagStockDTO.setObjectType("");

        Query query = mock(Query.class);
        when(em.createNativeQuery(anyString())).thenReturn(query);

        when(stockTransDetail.getProdOfferId()).thenReturn(1L);
        when(stockTotalIm1Service.findOneStockType(anyLong())).thenReturn(stockTypeIm1DTO);
        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test(expected = Exception.class)
    public void testDoSaveStockGoods_51() throws Exception {
        //out 447
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDetailDTO stockTransDetail = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        flagStockDTO.setUpdateOwnerId(false);
        stockTransDTO.setExchangeStockId(null);

        StockTypeIm1DTO stockTypeIm1DTO = new StockTypeIm1DTO();
        stockTypeIm1DTO.setTableName("KCS");
        flagStockDTO.setUpdateSaleDate(false);

        StockTransSerialDTO stockTransSerialDTO = mock(StockTransSerialDTO.class);
        List<StockTransSerialDTO> stockTransSerialDTOList = Lists.newArrayList(stockTransSerialDTO);
        when(stockTransDetail.getLstStockTransSerial()).thenReturn(stockTransSerialDTOList);

        when(stockTransDetail.getProdOfferTypeId()).thenReturn(5L);
        when(stockTransSerialDTO.getFromSerial()).thenReturn("2");
        when(stockTransSerialDTO.getToSerial()).thenReturn("2");
        flagStockDTO.setObjectType("exp");
//        stockTransDTO.setPayStatus("1");

        Query query = mock(Query.class);
        when(em.createNativeQuery(anyString())).thenReturn(query);

        when(stockTransDetail.getProdOfferId()).thenReturn(1L);
        when(stockTotalIm1Service.findOneStockType(anyLong())).thenReturn(stockTypeIm1DTO);
        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test(expected = Exception.class)
    public void testDoSaveStockGoods_52() throws Exception {
        //out 447
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDetailDTO stockTransDetail = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        flagStockDTO.setUpdateOwnerId(false);
        stockTransDTO.setExchangeStockId(null);

        StockTypeIm1DTO stockTypeIm1DTO = new StockTypeIm1DTO();
        stockTypeIm1DTO.setTableName("KCS");
        flagStockDTO.setUpdateSaleDate(false);

        StockTransSerialDTO stockTransSerialDTO = mock(StockTransSerialDTO.class);
        List<StockTransSerialDTO> stockTransSerialDTOList = Lists.newArrayList(stockTransSerialDTO);
        when(stockTransDetail.getLstStockTransSerial()).thenReturn(stockTransSerialDTOList);

        when(stockTransDetail.getProdOfferTypeId()).thenReturn(5L);
        when(stockTransSerialDTO.getFromSerial()).thenReturn("2");
        when(stockTransSerialDTO.getToSerial()).thenReturn("2");
        flagStockDTO.setObjectType("AGENT_EXPORT");
        stockTransDTO.setPayStatus("");

        Query query = mock(Query.class);
        when(em.createNativeQuery(anyString())).thenReturn(query);

        when(stockTransDetail.getProdOfferId()).thenReturn(1L);
        when(stockTotalIm1Service.findOneStockType(anyLong())).thenReturn(stockTypeIm1DTO);
        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test(expected = Exception.class)
    public void testDoSaveStockGoods_53() throws Exception {
        //out 447
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDetailDTO stockTransDetail = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        flagStockDTO.setUpdateOwnerId(false);
        stockTransDTO.setExchangeStockId(null);

        StockTypeIm1DTO stockTypeIm1DTO = new StockTypeIm1DTO();
        stockTypeIm1DTO.setTableName("KCS");
        flagStockDTO.setUpdateSaleDate(false);

        StockTransSerialDTO stockTransSerialDTO = mock(StockTransSerialDTO.class);
        List<StockTransSerialDTO> stockTransSerialDTOList = Lists.newArrayList(stockTransSerialDTO);
        when(stockTransDetail.getLstStockTransSerial()).thenReturn(stockTransSerialDTOList);

        when(stockTransDetail.getProdOfferTypeId()).thenReturn(5L);
        when(stockTransSerialDTO.getFromSerial()).thenReturn("2");
        when(stockTransSerialDTO.getToSerial()).thenReturn("2");
        flagStockDTO.setObjectType("AGENT_EXPORT");
        stockTransDTO.setPayStatus("0");

        Query query = mock(Query.class);
        when(em.createNativeQuery(anyString())).thenReturn(query);

        when(stockTransDetail.getProdOfferId()).thenReturn(1L);
        when(stockTotalIm1Service.findOneStockType(anyLong())).thenReturn(stockTypeIm1DTO);
        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }


    @Test(expected = Exception.class)
    public void testDoSaveStockGoods_54() throws Exception {
        //out 440
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDetailDTO stockTransDetail = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        flagStockDTO.setUpdateOwnerId(false);
        stockTransDTO.setExchangeStockId(null);

        StockTypeIm1DTO stockTypeIm1DTO = new StockTypeIm1DTO();
        stockTypeIm1DTO.setTableName("KCS");
        flagStockDTO.setUpdateSaleDate(false);

        StockTransSerialDTO stockTransSerialDTO = mock(StockTransSerialDTO.class);
        List<StockTransSerialDTO> stockTransSerialDTOList = Lists.newArrayList(stockTransSerialDTO);
        when(stockTransDetail.getLstStockTransSerial()).thenReturn(stockTransSerialDTOList);

        when(stockTransDetail.getProdOfferTypeId()).thenReturn(7L);
        when(stockTransSerialDTO.getFromSerial()).thenReturn("2");
        when(stockTransSerialDTO.getToSerial()).thenReturn("2");
        flagStockDTO.setObjectType("AGENT_EXPORT");
        stockTransDTO.setPayStatus("1");

        Query query = mock(Query.class);
        when(em.createNativeQuery(anyString())).thenReturn(query);

        when(stockTransDetail.getProdOfferId()).thenReturn(1L);
        when(stockTotalIm1Service.findOneStockType(anyLong())).thenReturn(stockTypeIm1DTO);
        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test(expected = Exception.class)
    public void testDoSaveStockGoods_55() throws Exception {
        //in 454,459,462,465
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDetailDTO stockTransDetail = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        stockTransDTO.setExchangeStockId(null);

        StockTypeIm1DTO stockTypeIm1DTO = new StockTypeIm1DTO();
        stockTypeIm1DTO.setTableName("KCS");
        flagStockDTO.setUpdateSaleDate(false);

        StockTransSerialDTO stockTransSerialDTO = mock(StockTransSerialDTO.class);
        List<StockTransSerialDTO> stockTransSerialDTOList = Lists.newArrayList(stockTransSerialDTO);
        when(stockTransDetail.getLstStockTransSerial()).thenReturn(stockTransSerialDTOList);

        when(stockTransDetail.getProdOfferTypeId()).thenReturn(7L);
        flagStockDTO.setObjectType("AGENT_EXPORT");
        stockTransDTO.setPayStatus("1");

        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setToOwnerId(1L);
        flagStockDTO.setUpdateOwnerId(true);
        when(stockTransDetail.getDepositPrice()).thenReturn(2L);
        flagStockDTO.setUpdateAccountBalance(false);//isUpdateDepositStatus
        stockTransDTO.setBankplusStatus(1L);//isUpdateDepositStatus
        flagStockDTO.setStateIdForReasonId(1L);
        flagStockDTO.setUpdateProdOfferId(true);

        Query query = mock(Query.class);
        when(em.createNativeQuery(anyString())).thenReturn(query);
        when(stockTransDetail.getProdOfferId()).thenReturn(1L);
        when(stockTotalIm1Service.findOneStockType(anyLong())).thenReturn(stockTypeIm1DTO);
        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test(expected = Exception.class)
    public void testDoSaveStockGoods_56() throws Exception {
        //out 454
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDetailDTO stockTransDetail = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        stockTransDTO.setExchangeStockId(null);

        StockTypeIm1DTO stockTypeIm1DTO = new StockTypeIm1DTO();
        stockTypeIm1DTO.setTableName("KCS");
        flagStockDTO.setUpdateSaleDate(false);

        StockTransSerialDTO stockTransSerialDTO = mock(StockTransSerialDTO.class);
        List<StockTransSerialDTO> stockTransSerialDTOList = Lists.newArrayList(stockTransSerialDTO);
        when(stockTransDetail.getLstStockTransSerial()).thenReturn(stockTransSerialDTOList);

        when(stockTransDetail.getProdOfferTypeId()).thenReturn(7L);
        flagStockDTO.setObjectType("AGENT_EXPORT");
        stockTransDTO.setPayStatus("1");

        stockTransDTO.setToOwnerType(null);
//        stockTransDTO.setToOwnerId(1L);

        Query query = mock(Query.class);
        when(em.createNativeQuery(anyString())).thenReturn(query);
        when(stockTransDetail.getProdOfferId()).thenReturn(1L);
        when(stockTotalIm1Service.findOneStockType(anyLong())).thenReturn(stockTypeIm1DTO);
        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test(expected = Exception.class)
    public void testDoSaveStockGoods_57() throws Exception {
        //out 454
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDetailDTO stockTransDetail = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        stockTransDTO.setExchangeStockId(null);

        StockTypeIm1DTO stockTypeIm1DTO = new StockTypeIm1DTO();
        stockTypeIm1DTO.setTableName("KCS");
        flagStockDTO.setUpdateSaleDate(false);

        StockTransSerialDTO stockTransSerialDTO = mock(StockTransSerialDTO.class);
        List<StockTransSerialDTO> stockTransSerialDTOList = Lists.newArrayList(stockTransSerialDTO);
        when(stockTransDetail.getLstStockTransSerial()).thenReturn(stockTransSerialDTOList);

        when(stockTransDetail.getProdOfferTypeId()).thenReturn(7L);
        flagStockDTO.setObjectType("AGENT_EXPORT");
        stockTransDTO.setPayStatus("1");

        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setToOwnerId(null);

        Query query = mock(Query.class);
        when(em.createNativeQuery(anyString())).thenReturn(query);
        when(stockTransDetail.getProdOfferId()).thenReturn(1L);
        when(stockTotalIm1Service.findOneStockType(anyLong())).thenReturn(stockTypeIm1DTO);
        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test(expected = Exception.class)
    public void testDoSaveStockGoods_58() throws Exception {
        //out 468
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDetailDTO stockTransDetail = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        stockTransDTO.setExchangeStockId(null);

        StockTypeIm1DTO stockTypeIm1DTO = new StockTypeIm1DTO();
        stockTypeIm1DTO.setTableName("KCS");
        flagStockDTO.setUpdateSaleDate(false);

        StockTransSerialDTO stockTransSerialDTO = mock(StockTransSerialDTO.class);
        List<StockTransSerialDTO> stockTransSerialDTOList = Lists.newArrayList(stockTransSerialDTO);
        when(stockTransDetail.getLstStockTransSerial()).thenReturn(stockTransSerialDTOList);

        when(stockTransDetail.getProdOfferTypeId()).thenReturn(7L);
        flagStockDTO.setObjectType("AGENT_EXPORT");
        stockTransDTO.setPayStatus("1");

        flagStockDTO.setUpdateAccountBalance(false);

        Query query = mock(Query.class);
        when(em.createNativeQuery(anyString())).thenReturn(query);
        when(stockTransDetail.getProdOfferId()).thenReturn(1L);
        when(stockTotalIm1Service.findOneStockType(anyLong())).thenReturn(stockTypeIm1DTO);
        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test(expected = Exception.class)
    public void testDoSaveStockGoods_59() throws Exception {
        //in 480, 481
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDetailDTO stockTransDetail = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        stockTransDTO.setExchangeStockId(null);

        StockTypeIm1DTO stockTypeIm1DTO = new StockTypeIm1DTO();
        stockTypeIm1DTO.setTableName("KCS");
        flagStockDTO.setUpdateSaleDate(false);

        StockTransSerialDTO stockTransSerialDTO = mock(StockTransSerialDTO.class);
        List<StockTransSerialDTO> stockTransSerialDTOList = Lists.newArrayList(stockTransSerialDTO);
        when(stockTransDetail.getLstStockTransSerial()).thenReturn(stockTransSerialDTOList);

        when(stockTransDetail.getProdOfferTypeId()).thenReturn(7L);
        flagStockDTO.setObjectType("AGENT_EXPORT");
        stockTransDTO.setPayStatus("1");

        flagStockDTO.setUpdateRescue(true);
        flagStockDTO.setStateId(1L);
        flagStockDTO.setNote("_STOCK");

        Query query = mock(Query.class);
        when(em.createNativeQuery(anyString())).thenReturn(query);
        when(stockTransDetail.getProdOfferId()).thenReturn(1L);
        when(stockTotalIm1Service.findOneStockType(anyLong())).thenReturn(stockTypeIm1DTO);
        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test(expected = Exception.class)
    public void testDoSaveStockGoods_60() throws Exception {
        //out 481
        //in 487
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDetailDTO stockTransDetail = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        stockTransDTO.setExchangeStockId(null);

        StockTypeIm1DTO stockTypeIm1DTO = new StockTypeIm1DTO();
        stockTypeIm1DTO.setTableName("KCS");
        flagStockDTO.setUpdateSaleDate(false);

        StockTransSerialDTO stockTransSerialDTO = mock(StockTransSerialDTO.class);
        List<StockTransSerialDTO> stockTransSerialDTOList = Lists.newArrayList(stockTransSerialDTO);
        when(stockTransDetail.getLstStockTransSerial()).thenReturn(stockTransSerialDTOList);

        when(stockTransDetail.getProdOfferTypeId()).thenReturn(7L);
        flagStockDTO.setObjectType("AGENT_EXPORT");
        stockTransDTO.setPayStatus("1");

        flagStockDTO.setUpdateRescue(true);
        flagStockDTO.setStateId(1L);
        flagStockDTO.setNote("RETRIEVE_STOCK");
        stockTransDTO.setFromOwnerId(1L);
        stockTransDTO.setToOwnerId(1L);

        Query query = mock(Query.class);
        when(em.createNativeQuery(anyString())).thenReturn(query);
        when(stockTransDetail.getProdOfferId()).thenReturn(1L);
        when(stockTotalIm1Service.findOneStockType(anyLong())).thenReturn(stockTypeIm1DTO);
        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test(expected = Exception.class)
    public void testDoSaveStockGoods_61() throws Exception {
        //out 487
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDetailDTO stockTransDetail = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        stockTransDTO.setExchangeStockId(null);

        StockTypeIm1DTO stockTypeIm1DTO = new StockTypeIm1DTO();
        stockTypeIm1DTO.setTableName("KCS");
        flagStockDTO.setUpdateSaleDate(false);

        StockTransSerialDTO stockTransSerialDTO = mock(StockTransSerialDTO.class);
        List<StockTransSerialDTO> stockTransSerialDTOList = Lists.newArrayList(stockTransSerialDTO);
        when(stockTransDetail.getLstStockTransSerial()).thenReturn(stockTransSerialDTOList);

        when(stockTransDetail.getProdOfferTypeId()).thenReturn(7L);
        flagStockDTO.setObjectType("AGENT_EXPORT");
        stockTransDTO.setPayStatus("1");

        flagStockDTO.setUpdateRescue(true);
        flagStockDTO.setStateId(1L);
        flagStockDTO.setNote("RETRIEVE_STOCK");
        stockTransDTO.setFromOwnerId(null);
//        stockTransDTO.setToOwnerId(1L);

        Query query = mock(Query.class);
        when(em.createNativeQuery(anyString())).thenReturn(query);
        when(stockTransDetail.getProdOfferId()).thenReturn(1L);
        when(stockTotalIm1Service.findOneStockType(anyLong())).thenReturn(stockTypeIm1DTO);
        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test(expected = Exception.class)
    public void testDoSaveStockGoods_62() throws Exception {
        //out 487
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDetailDTO stockTransDetail = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        stockTransDTO.setExchangeStockId(null);

        StockTypeIm1DTO stockTypeIm1DTO = new StockTypeIm1DTO();
        stockTypeIm1DTO.setTableName("KCS");
        flagStockDTO.setUpdateSaleDate(false);

        StockTransSerialDTO stockTransSerialDTO = mock(StockTransSerialDTO.class);
        List<StockTransSerialDTO> stockTransSerialDTOList = Lists.newArrayList(stockTransSerialDTO);
        when(stockTransDetail.getLstStockTransSerial()).thenReturn(stockTransSerialDTOList);

        when(stockTransDetail.getProdOfferTypeId()).thenReturn(7L);
        flagStockDTO.setObjectType("AGENT_EXPORT");
        stockTransDTO.setPayStatus("1");

        flagStockDTO.setUpdateRescue(true);
        flagStockDTO.setStateId(1L);
        flagStockDTO.setNote("RETRIEVE_STOCK");
        stockTransDTO.setFromOwnerId(1L);
        stockTransDTO.setToOwnerId(null);

        Query query = mock(Query.class);
        when(em.createNativeQuery(anyString())).thenReturn(query);
        when(stockTransDetail.getProdOfferId()).thenReturn(1L);
        when(stockTotalIm1Service.findOneStockType(anyLong())).thenReturn(stockTypeIm1DTO);
        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test(expected = Exception.class)
    public void testDoSaveStockGoods_63() throws Exception {
        //out 480
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDetailDTO stockTransDetail = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        stockTransDTO.setExchangeStockId(null);

        StockTypeIm1DTO stockTypeIm1DTO = new StockTypeIm1DTO();
        stockTypeIm1DTO.setTableName("KCS");
        flagStockDTO.setUpdateSaleDate(false);

        StockTransSerialDTO stockTransSerialDTO = mock(StockTransSerialDTO.class);
        List<StockTransSerialDTO> stockTransSerialDTOList = Lists.newArrayList(stockTransSerialDTO);
        when(stockTransDetail.getLstStockTransSerial()).thenReturn(stockTransSerialDTOList);

        when(stockTransDetail.getProdOfferTypeId()).thenReturn(7L);
        flagStockDTO.setObjectType("AGENT_EXPORT");
        stockTransDTO.setPayStatus("1");

        flagStockDTO.setUpdateRescue(false);
//        flagStockDTO.setStateId(1L);

        Query query = mock(Query.class);
        when(em.createNativeQuery(anyString())).thenReturn(query);
        when(stockTransDetail.getProdOfferId()).thenReturn(1L);
        when(stockTotalIm1Service.findOneStockType(anyLong())).thenReturn(stockTypeIm1DTO);
        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test(expected = Exception.class)
    public void testDoSaveStockGoods_64() throws Exception {
        //out 480
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDetailDTO stockTransDetail = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        stockTransDTO.setExchangeStockId(null);

        StockTypeIm1DTO stockTypeIm1DTO = new StockTypeIm1DTO();
        stockTypeIm1DTO.setTableName("KCS");
        flagStockDTO.setUpdateSaleDate(false);

        StockTransSerialDTO stockTransSerialDTO = mock(StockTransSerialDTO.class);
        List<StockTransSerialDTO> stockTransSerialDTOList = Lists.newArrayList(stockTransSerialDTO);
        when(stockTransDetail.getLstStockTransSerial()).thenReturn(stockTransSerialDTOList);

        when(stockTransDetail.getProdOfferTypeId()).thenReturn(7L);
        flagStockDTO.setObjectType("AGENT_EXPORT");
        stockTransDTO.setPayStatus("1");

        flagStockDTO.setUpdateRescue(true);
        flagStockDTO.setStateId(0L);

        Query query = mock(Query.class);
        when(em.createNativeQuery(anyString())).thenReturn(query);
        when(stockTransDetail.getProdOfferId()).thenReturn(1L);
        when(stockTotalIm1Service.findOneStockType(anyLong())).thenReturn(stockTypeIm1DTO);
        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test(expected = Exception.class)
    public void testDoSaveStockGoods_65() throws Exception {
        //out 468
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDetailDTO stockTransDetail = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        stockTransDTO.setExchangeStockId(null);

        StockTypeIm1DTO stockTypeIm1DTO = new StockTypeIm1DTO();
        stockTypeIm1DTO.setTableName("KCS");
        flagStockDTO.setUpdateSaleDate(false);

        StockTransSerialDTO stockTransSerialDTO = mock(StockTransSerialDTO.class);
        List<StockTransSerialDTO> stockTransSerialDTOList = Lists.newArrayList(stockTransSerialDTO);
        when(stockTransDetail.getLstStockTransSerial()).thenReturn(stockTransSerialDTOList);

        when(stockTransDetail.getProdOfferTypeId()).thenReturn(7L);
        flagStockDTO.setObjectType("AGENT_EXPORT");
        stockTransDTO.setPayStatus("1");

        flagStockDTO.setUpdateAccountBalance(true);
        stockTransDTO.setBankplusStatus(null);

        Query query = mock(Query.class);
        when(em.createNativeQuery(anyString())).thenReturn(query);
        when(stockTransDetail.getProdOfferId()).thenReturn(1L);
        when(stockTotalIm1Service.findOneStockType(anyLong())).thenReturn(stockTypeIm1DTO);
        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test(expected = Exception.class)
    public void testDoSaveStockGoods_67() throws Exception {
        //in 505,506
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDetailDTO stockTransDetail = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        stockTransDTO.setExchangeStockId(null);

        StockTypeIm1DTO stockTypeIm1DTO = new StockTypeIm1DTO();
        stockTypeIm1DTO.setTableName("KCS");
        flagStockDTO.setUpdateSaleDate(false);

        StockTransSerialDTO stockTransSerialDTO = mock(StockTransSerialDTO.class);
        List<StockTransSerialDTO> stockTransSerialDTOList = Lists.newArrayList(stockTransSerialDTO);
        when(stockTransDetail.getLstStockTransSerial()).thenReturn(stockTransSerialDTOList);
        flagStockDTO.setObjectType("AGENT_EXPORT");
        stockTransDTO.setPayStatus("1");

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        when(productOfferingService.findOne(anyLong())).thenReturn(productOfferingDTO);
        Query query = mock(Query.class);
        when(em.createNativeQuery(anyString())).thenReturn(query);
        when(stockTransDetail.getProdOfferTypeId()).thenReturn(7L);
        when(query.executeUpdate()).thenReturn(0);

        when(stockTransDetail.getProdOfferId()).thenReturn(1L);
        when(stockTotalIm1Service.findOneStockType(anyLong())).thenReturn(stockTypeIm1DTO);
        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test(expected = Exception.class)
    public void testDoSaveStockGoods_68() throws Exception {
        //out 506
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDetailDTO stockTransDetail = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        stockTransDTO.setExchangeStockId(null);

        StockTypeIm1DTO stockTypeIm1DTO = new StockTypeIm1DTO();
        stockTypeIm1DTO.setTableName("KCS");
        flagStockDTO.setUpdateSaleDate(false);

        StockTransSerialDTO stockTransSerialDTO = mock(StockTransSerialDTO.class);
        List<StockTransSerialDTO> stockTransSerialDTOList = Lists.newArrayList(stockTransSerialDTO);
        when(stockTransDetail.getLstStockTransSerial()).thenReturn(stockTransSerialDTOList);
        flagStockDTO.setObjectType("AGENT_EXPORT");
        stockTransDTO.setPayStatus("1");

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        when(productOfferingService.findOne(anyLong())).thenReturn(productOfferingDTO);
        Query query = mock(Query.class);
        when(em.createNativeQuery(anyString())).thenReturn(query);
        when(stockTransDetail.getProdOfferTypeId()).thenReturn(7L);
        when(query.executeUpdate()).thenReturn(1);

        when(stockTransDetail.getProdOfferId()).thenReturn(1L);
        when(stockTotalIm1Service.findOneStockType(anyLong())).thenReturn(stockTypeIm1DTO);
        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test(expected = Exception.class)
    public void testDoSaveStockGoods_69() throws Exception {
        //out 505
        //in 515 527
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDetailDTO stockTransDetail = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        stockTransDTO.setExchangeStockId(null);

        StockTypeIm1DTO stockTypeIm1DTO = new StockTypeIm1DTO();
        stockTypeIm1DTO.setTableName("KCS");
        flagStockDTO.setUpdateSaleDate(false);

        StockTransSerialDTO stockTransSerialDTO = mock(StockTransSerialDTO.class);
        List<StockTransSerialDTO> stockTransSerialDTOList = Lists.newArrayList(stockTransSerialDTO);
        when(stockTransDetail.getLstStockTransSerial()).thenReturn(stockTransSerialDTOList);
        flagStockDTO.setObjectType("AGENT_EXPORT");
        stockTransDTO.setPayStatus("1");

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        when(productOfferingService.findOne(anyLong())).thenReturn(productOfferingDTO);
        Query query = mock(Query.class);
        when(em.createNativeQuery(anyString())).thenReturn(query);
        when(stockTransDetail.getProdOfferTypeId()).thenReturn(4L);
        when(query.executeUpdate()).thenReturn(2);
        when(stockTransSerialDTO.getFromSerial()).thenReturn("2");
        when(stockTransSerialDTO.getToSerial()).thenReturn("2");

        when(stockTransDetail.getProdOfferId()).thenReturn(1L);
        when(stockTotalIm1Service.findOneStockType(anyLong())).thenReturn(stockTypeIm1DTO);
        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test
    public void testDoSaveStockGoods_70() throws Exception {
        //out 515
        //out 527
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDetailDTO stockTransDetail = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        stockTransDTO.setExchangeStockId(null);

        StockTypeIm1DTO stockTypeIm1DTO = new StockTypeIm1DTO();
        stockTypeIm1DTO.setTableName("KCS");
        flagStockDTO.setUpdateSaleDate(false);

        StockTransSerialDTO stockTransSerialDTO = mock(StockTransSerialDTO.class);
        List<StockTransSerialDTO> stockTransSerialDTOList = Lists.newArrayList(stockTransSerialDTO);
        when(stockTransDetail.getLstStockTransSerial()).thenReturn(stockTransSerialDTOList);
        flagStockDTO.setObjectType("AGENT_EXPORT");
        stockTransDTO.setPayStatus("1");

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        when(productOfferingService.findOne(anyLong())).thenReturn(productOfferingDTO);
        Query query = mock(Query.class);
        when(em.createNativeQuery(anyString())).thenReturn(query);
        when(stockTransDetail.getProdOfferTypeId()).thenReturn(4L);
        when(query.executeUpdate()).thenReturn(1);
        when(stockTransSerialDTO.getFromSerial()).thenReturn("2");
        when(stockTransSerialDTO.getToSerial()).thenReturn("2");

        when(stockTransDetail.getQuantity()).thenReturn(1L);

        when(stockTransDetail.getProdOfferId()).thenReturn(1L);
        when(stockTotalIm1Service.findOneStockType(anyLong())).thenReturn(stockTypeIm1DTO);
        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test(expected = Exception.class)
    public void testDoSaveStockGoods_71() throws Exception {
        //out 515
        //out 527
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDetailDTO stockTransDetail = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        stockTransDTO.setExchangeStockId(null);

        StockTypeIm1DTO stockTypeIm1DTO = new StockTypeIm1DTO();
        stockTypeIm1DTO.setTableName("KCS");
        flagStockDTO.setUpdateSaleDate(false);

        StockTransSerialDTO stockTransSerialDTO = mock(StockTransSerialDTO.class);
        List<StockTransSerialDTO> stockTransSerialDTOList = Lists.newArrayList(stockTransSerialDTO);
        when(stockTransDetail.getLstStockTransSerial()).thenReturn(stockTransSerialDTOList);
        flagStockDTO.setObjectType("AGENT_EXPORT");
        stockTransDTO.setPayStatus("1");

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        when(productOfferingService.findOne(anyLong())).thenReturn(productOfferingDTO);
        Query query = mock(Query.class);
        when(em.createNativeQuery(anyString())).thenReturn(query);
        when(stockTransDetail.getProdOfferTypeId()).thenReturn(4L);
        when(query.executeUpdate()).thenReturn(1);
        when(stockTransSerialDTO.getFromSerial()).thenReturn("2");
        when(stockTransSerialDTO.getToSerial()).thenReturn("2");

        when(stockTransDetail.getQuantity()).thenReturn(9L);

        when(stockTransDetail.getProdOfferId()).thenReturn(1L);
        when(stockTotalIm1Service.findOneStockType(anyLong())).thenReturn(stockTypeIm1DTO);
        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test(expected = Exception.class)
    public void testDoSaveStockGoods_72() throws Exception {
        //out 437
        //in 532
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDetailDTO stockTransDetail = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        flagStockDTO.setUpdateOwnerId(false);
        stockTransDTO.setExchangeStockId(null);

        stockTransDTO.setStatus("2");
        flagStockDTO.setStatusForAgent("1");
        stockTransDTO.setRegionStockId(null);

        StockTypeIm1DTO stockTypeIm1DTO = new StockTypeIm1DTO();
        stockTypeIm1DTO.setTableName("KCS");
        flagStockDTO.setUpdateSaleDate(false);

        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setToOwnerId(1L);
        flagStockDTO.setUpdateOwnerId(false);
        flagStockDTO.setUpdateAccountBalance(true);
        stockTransDTO.setBankplusStatus(0L);

        when(stockTransDetail.getDepositPrice()).thenReturn(2L);//and isUpdateDepositStatus=false
        flagStockDTO.setStateIdForReasonId(0L);
        flagStockDTO.setUpdateProdOfferId(false);
        flagStockDTO.setUpdateDamageProduct(true);
        when(stockTransDetail.getLstStockTransSerial()).thenReturn(null);

        when(stockTransDetail.getCheckSerial()).thenReturn(1L);

        when(stockTransDetail.getProdOfferId()).thenReturn(1L);
        when(stockTotalIm1Service.findOneStockType(anyLong())).thenReturn(stockTypeIm1DTO);
        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Test
    public void testDoSaveStockGoods_73() throws Exception {
        //out 532
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDetailDTO stockTransDetail = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        stockTransDTO.setProcessOffline("2");
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        flagStockDTO.setUpdateOwnerId(false);
        stockTransDTO.setExchangeStockId(null);

        stockTransDTO.setStatus("2");
        flagStockDTO.setStatusForAgent("1");
        stockTransDTO.setRegionStockId(null);

        StockTypeIm1DTO stockTypeIm1DTO = new StockTypeIm1DTO();
        stockTypeIm1DTO.setTableName("KCS");
        flagStockDTO.setUpdateSaleDate(false);

        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setToOwnerId(1L);
        flagStockDTO.setUpdateOwnerId(false);
        flagStockDTO.setUpdateAccountBalance(true);
        stockTransDTO.setBankplusStatus(0L);

        when(stockTransDetail.getDepositPrice()).thenReturn(2L);//and isUpdateDepositStatus=false
        flagStockDTO.setStateIdForReasonId(0L);
        flagStockDTO.setUpdateProdOfferId(false);
        flagStockDTO.setUpdateDamageProduct(true);
        when(stockTransDetail.getLstStockTransSerial()).thenReturn(null);

        when(stockTransDetail.getCheckSerial()).thenReturn(2L);

        when(stockTransDetail.getProdOfferId()).thenReturn(1L);
        when(stockTotalIm1Service.findOneStockType(anyLong())).thenReturn(stockTypeIm1DTO);
        baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method BaseStockIm1Service.doSaveStockTotalUctt
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testDoSaveStockTotalUctt_1() throws Exception {
        //in 599
        StockTotalDTO stockTotalDTO = new StockTotalDTO();
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        StockTotalIm1DTO stockTotalIm1DTO = mock(StockTotalIm1DTO.class);

        List<StockTotalIm1DTO> listStockTotal = Lists.newArrayList(stockTotalIm1DTO);
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStaffCode("cd");
        staffDTO.setName("nm");
        ShopDTO shopDTO = new ShopDTO();
        when(staffService.findOne(anyLong())).thenReturn(staffDTO);
        when(shopService.findOne(anyLong())).thenReturn(shopDTO);

        when(stockTotalIm1Service.findStockTotal(any())).thenReturn(listStockTotal);
        baseStockIm1Service.doSaveStockTotalUctt(stockTotalDTO, stockTransDTO, stockTransActionDTO);
    }

    @Test
    public void testDoSaveStockTotalUctt_2() throws Exception {
        //out 599
        StockTotalDTO stockTotalDTO = new StockTotalDTO();
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();

        List<StockTotalIm1DTO> listStockTotal = Lists.newArrayList();
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStaffCode("cd");
        staffDTO.setName("nm");
        ShopDTO shopDTO = new ShopDTO();
        when(staffService.findOne(anyLong())).thenReturn(staffDTO);
        when(shopService.findOne(anyLong())).thenReturn(shopDTO);

        when(stockTotalIm1Service.findStockTotal(any())).thenReturn(listStockTotal);
        baseStockIm1Service.doSaveStockTotalUctt(stockTotalDTO, stockTransDTO, stockTransActionDTO);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method BaseStockIm1Service.updateSerialForRevoke
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testUpdateSerialForRevoke_1() throws Exception {
        //in 659
        Long prodOfferTypeId = 1L;
        Long stockModelId = 1L;
        String serial = "1";
        Long ownerId = 1L;
        Long stateId = 1L;
        Long oldOwnerType = 1L;
        Long oldOwnerId = 1L;
        Long revokeType = 1L;

        PowerMockito.mockStatic(IMServiceUtil.class);
        PowerMockito.when(IMServiceUtil.getTableNameByOfferType(prodOfferTypeId)).thenReturn("");

        baseStockIm1Service.updateSerialForRevoke(prodOfferTypeId, stockModelId, serial, ownerId, stateId, oldOwnerType, oldOwnerId, revokeType);
    }

    @Test
    public void testUpdateSerialForRevoke_2() throws Exception {
        //out 659
        //in 671
        Long prodOfferTypeId = 7L;
        Long stockModelId = 1L;
        String serial = "1";
        Long ownerId = 1L;
        Long stateId = 1L;
        Long oldOwnerType = 1L;
        Long oldOwnerId = 1L;
        Long revokeType = 1L;

        PowerMockito.mockStatic(IMServiceUtil.class);
        PowerMockito.when(IMServiceUtil.getTableNameByOfferType(anyLong())).thenReturn("DUAL");

        baseStockIm1Service.updateSerialForRevoke(prodOfferTypeId, stockModelId, serial, ownerId, stateId, oldOwnerType, oldOwnerId, revokeType);
    }

    @Test
    public void testUpdateSerialForRevoke_3() throws Exception {
        //out 671
        //in 687
        Long prodOfferTypeId = 5L;
        Long stockModelId = 1L;
        String serial = "1";
        Long ownerId = 1L;
        Long stateId = 1L;
        Long oldOwnerType = 1L;
        Long oldOwnerId = 1L;
        Long revokeType = 1L;

        PowerMockito.mockStatic(IMServiceUtil.class);
        PowerMockito.when(IMServiceUtil.getTableNameByOfferType(anyLong())).thenReturn("DUAL");

        Query query = mock(Query.class);
        when(em.createNativeQuery(anyString())).thenReturn(query);
        baseStockIm1Service.updateSerialForRevoke(prodOfferTypeId, stockModelId, serial, ownerId, stateId, oldOwnerType, oldOwnerId, revokeType);
    }

    @Test
    public void testUpdateSerialForRevoke_4() throws Exception {
        //out 687
        //in 701
        Long prodOfferTypeId = 5L;
        Long stockModelId = 1L;
        String serial = "1";
        Long ownerId = 1L;
        Long stateId = 1L;
        Long oldOwnerType = 1L;
        Long oldOwnerId = 1L;
        Long revokeType = 2L;

        PowerMockito.mockStatic(IMServiceUtil.class);
        PowerMockito.when(IMServiceUtil.getTableNameByOfferType(anyLong())).thenReturn("DUAL");

        Query query = mock(Query.class);
        when(em.createNativeQuery(anyString())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(0);
        baseStockIm1Service.updateSerialForRevoke(prodOfferTypeId, stockModelId, serial, ownerId, stateId, oldOwnerType, oldOwnerId, revokeType);
    }

    @Test
    public void testUpdateSerialForRevoke_5() throws Exception {
        //out 701
        Long prodOfferTypeId = 5L;
        Long stockModelId = 1L;
        String serial = "1";
        Long ownerId = 1L;
        Long stateId = 1L;
        Long oldOwnerType = 1L;
        Long oldOwnerId = 1L;
        Long revokeType = 2L;

        PowerMockito.mockStatic(IMServiceUtil.class);
        PowerMockito.when(IMServiceUtil.getTableNameByOfferType(anyLong())).thenReturn("DUAL");

        Query query = mock(Query.class);
        when(em.createNativeQuery(anyString())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(1);
        baseStockIm1Service.updateSerialForRevoke(prodOfferTypeId, stockModelId, serial, ownerId, stateId, oldOwnerType, oldOwnerId, revokeType);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method BaseStockIm1Service.doIncreaseStockNum
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testDoIncreaseStockNum_1() throws Exception {
        //in 715
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        String prefixActionCode = "";
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        baseStockIm1Service.doIncreaseStockNum(stockTransActionDTO, prefixActionCode, requiredRoleMap);
    }

    @Test
    public void testDoIncreaseStockNum_2() throws Exception {
        //out 715
        //in 720, 722
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        String prefixActionCode = "PX";
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        stockTransActionDTO.setActionStaffId(1L);
        StaffIm1DTO staff = new StaffIm1DTO();

        when(staffIm1Service.findOne(anyLong())).thenReturn(staff);
        baseStockIm1Service.doIncreaseStockNum(stockTransActionDTO, prefixActionCode, requiredRoleMap);
    }

    @Test
    public void testDoIncreaseStockNum_3() throws Exception {
        //out 722
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        String prefixActionCode = "PN";
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        stockTransActionDTO.setActionStaffId(1L);
        StaffIm1DTO staff = new StaffIm1DTO();

        when(staffIm1Service.findOne(anyLong())).thenReturn(staff);
        baseStockIm1Service.doIncreaseStockNum(stockTransActionDTO, prefixActionCode, requiredRoleMap);
    }

    @Test
    public void testDoIncreaseStockNum_4() throws Exception {
        //out 722
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        String prefixActionCode = "LN";
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        stockTransActionDTO.setActionStaffId(1L);
        StaffIm1DTO staff = new StaffIm1DTO();

        when(staffIm1Service.findOne(anyLong())).thenReturn(staff);
        baseStockIm1Service.doIncreaseStockNum(stockTransActionDTO, prefixActionCode, requiredRoleMap);
    }

    @Test
    public void testDoIncreaseStockNum_5() throws Exception {
        //out 722
        //in 730
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        String prefixActionCode = "other";
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        requiredRoleMap.add(Const.PERMISION.ROLE_STOCK_NUM_SHOP);

        stockTransActionDTO.setActionStaffId(1L);
        StaffIm1DTO staff = new StaffIm1DTO();

        when(staffIm1Service.findOne(anyLong())).thenReturn(staff);
        baseStockIm1Service.doIncreaseStockNum(stockTransActionDTO, prefixActionCode, requiredRoleMap);
    }

    @Test
    public void testDoIncreaseStockNum_6() throws Exception {
        //out 730
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        String prefixActionCode = "other";
        RequiredRoleMap requiredRoleMap = null;
//        requiredRoleMap.add(Const.PERMISION.ROLE_STOCK_NUM_SHOP);

        stockTransActionDTO.setActionStaffId(1L);
        StaffIm1DTO staff = new StaffIm1DTO();

        when(staffIm1Service.findOne(anyLong())).thenReturn(staff);
        baseStockIm1Service.doIncreaseStockNum(stockTransActionDTO, prefixActionCode, requiredRoleMap);
    }

    @Test(expected = Exception.class)
    public void testDoIncreaseStockNum_8() throws Exception {
        //out 720
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        String prefixActionCode = "other";
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        requiredRoleMap.add(Const.PERMISION.ROLE_XUAT_KHO_BA_MIEN);

        stockTransActionDTO.setActionStaffId(1L);
        StaffIm1DTO staff = null;

        when(staffIm1Service.findOne(anyLong())).thenReturn(staff);
        baseStockIm1Service.doIncreaseStockNum(stockTransActionDTO, prefixActionCode, requiredRoleMap);
    }

    @Test
    public void testDoIncreaseStockNum_7() throws Exception {
        //out 730
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        String prefixActionCode = "other";
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        requiredRoleMap.add(Const.PERMISION.ROLE_XUAT_KHO_BA_MIEN);

        stockTransActionDTO.setActionStaffId(1L);
        StaffIm1DTO staff = new StaffIm1DTO();

        when(staffIm1Service.findOne(anyLong())).thenReturn(staff);
        baseStockIm1Service.doIncreaseStockNum(stockTransActionDTO, prefixActionCode, requiredRoleMap);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method BaseStockIm1Service.updateStockNumShop
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testUpdateStockNumShop_1() throws Exception {
        //in 742,748,757
        ShopIm1DTO shopDTO = new ShopIm1DTO();
        shopDTO.setStockNum(null);

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        when(shopIm1Service.findOne(anyLong())).thenReturn(shopDTO);
        when(stockTransActionService.findOne(anyLong())).thenReturn(stockTransActionDTO);
        baseStockIm1Service.updateStockNumShop(1L,Const.STOCK_TRANS.TRANS_CODE_PX, 1L);
    }

    @Test
    public void testUpdateStockNumShop_2() throws Exception {
        //out 757
        ShopIm1DTO shopDTO = new ShopIm1DTO();

        when(shopIm1Service.findOne(anyLong())).thenReturn(shopDTO);
        when(stockTransActionService.findOne(anyLong())).thenReturn(null);
        baseStockIm1Service.updateStockNumShop(1L, Const.STOCK_TRANS.TRANS_CODE_PX, 1L);
    }

    @Test
    public void testUpdateStockNumShop_3() throws Exception {
        //in 752
        ShopIm1DTO shopDTO = new ShopIm1DTO();

        when(shopIm1Service.findOne(anyLong())).thenReturn(shopDTO);
        when(stockTransActionService.findOne(anyLong())).thenReturn(null);
        baseStockIm1Service.updateStockNumShop(1L, Const.STOCK_TRANS.TRANS_CODE_PN, 1L);
    }

    @Test
    public void testUpdateStockNumShop_4() throws Exception {
        //out 752
        ShopIm1DTO shopDTO = new ShopIm1DTO();

        when(shopIm1Service.findOne(anyLong())).thenReturn(shopDTO);
        when(stockTransActionService.findOne(anyLong())).thenReturn(null);
        baseStockIm1Service.updateStockNumShop(1L,Const.STOCK_TRANS.TRANS_CODE_PARTNER, 1L);
    }

    @Test
    public void testUpdateStockNumShop_6() throws Exception {
        //in/out 749
        ShopIm1DTO shopDTO = new ShopIm1DTO();
        shopDTO.setStockNum(1L);

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        when(shopIm1Service.findOne(anyLong())).thenReturn(shopDTO);
        when(stockTransActionService.findOne(anyLong())).thenReturn(stockTransActionDTO);
        baseStockIm1Service.updateStockNumShop(1L,"PX", 1L);
    }
}