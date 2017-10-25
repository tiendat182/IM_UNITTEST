package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.dto.KcsDTO;
import com.viettel.bccs.inventory.dto.ProductOfferingDTO;
import com.viettel.bccs.inventory.dto.StockTotalMessage;
import com.viettel.bccs.inventory.model.Shop;
import com.viettel.bccs.inventory.model.Staff;
import com.viettel.bccs.inventory.model.StockHandset;
import com.viettel.bccs.inventory.repo.*;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.dto.BaseMessage;
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

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author DatLT
 * @date 10/10/2017
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({DbUtil.class, BaseServiceImpl.class, KcsService.class, DbUtil.class, Staff.class, Shop.class})
public class KcsServiceImplTest {
    @InjectMocks
    KcsServiceImpl kcsService;
    @Mock
    private KcsLogRepo kcsLogRepo;
    @Mock
    private KcsLogDetailRepo kcsLogDetailRepo;
    @Mock
    private KcsLogSerialRepo kcsLogSerialRepo;
    @Mock
    private StockTotalService stockTotalService;
    @Mock
    private ProductOfferingService productOfferingService;
    @Mock
    private StockHandsetRepo stockHandsetRepo;
    @Mock
    private StaffRepo staffRepo;
    @Mock
    private ShopRepo shopRepo;
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
     * Test for method KcsServiceImpl.findProductInStock
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindProductInStock_1() throws  Exception {
        String code ="cd";
        String serial ="sr";
        kcsService.findProductInStock(code, serial);
    }
    @Test
    public void testFindProductInStock_2() throws  Exception {
        String code ="";
        String serial ="sr";
        kcsService.findProductInStock(code, serial);
    }

    @Test
    public void testFindProductInStock_3() throws  Exception {
        String code ="cd";
        String serial ="";
        kcsService.findProductInStock(code, serial);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method KcsServiceImpl.findProductInStockIm1
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindProductInStockIm1_1() throws  Exception {
        String code ="cd";
        String serial ="sr";
        kcsService.findProductInStockIm1(code, serial);
    }

    @Test
    public void testFindProductInStockIm1_2() throws  Exception {
        String code ="cd";
        String serial ="";
        kcsService.findProductInStockIm1(code, serial);
    }

    @Test
    public void testFindProductInStockIm1_3() throws  Exception {
        String code ="";
        String serial ="sr";
        kcsService.findProductInStockIm1(code, serial);
    }
    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method KcsServiceImpl.importKcs
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testImportKcs_1() throws  Exception {
        Long userId=null;
        Long ownerId=1L;
        HashMap<String, KcsDTO> mapKCS = new HashMap<>();
        kcsService.importKcs(userId, ownerId,mapKCS);
    }

    @Test
    public void testImportKcs_2() throws  Exception {
        KcsServiceImpl spyService = PowerMockito.spy(kcsService);
        Long userId=1L;
        Long ownerId=1L;
        KcsDTO kcsDTO = new KcsDTO();
        HashMap<String, KcsDTO> mapKCS = new HashMap<>();
        mapKCS.put("1", kcsDTO);
        Staff staff = mock(Staff.class);
        Shop shop = mock(Shop.class);
        when(staffRepo.findOne(anyLong())).thenReturn(staff);
        when(shopRepo.findOne(anyLong())).thenReturn(shop);
        when(staff.getShopId()).thenReturn(1L);
        when(shop.getShopPath()).thenReturn("1");
        PowerMockito.mockStatic(DbUtil.class);
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );

        List<String> serialList = Lists.newArrayList();
        serialList.add("1");
        List<Long> stateIdList = Lists.newArrayList();
        stateIdList.add(12L);
        kcsDTO.setCode("43");
        kcsDTO.setSerialList(serialList);
        kcsDTO.setStateIdList(stateIdList);
        mapKCS.put("1", kcsDTO);
        BaseMessage updateSerialKcsMessage = mock(BaseMessage.class);
        StockTotalMessage changeStockTotalMessage = mock(StockTotalMessage.class);
        ProductOfferingDTO productOfferingDTO = mock(ProductOfferingDTO.class);
        when(productOfferingService.getProductByCode(any())).thenReturn(productOfferingDTO);
        when(productOfferingDTO.getProductOfferingId()).thenReturn(1L);
        when(spyService.updateSerialKCS(anyLong(), anyLong(), anyLong(), anyList())).thenReturn(updateSerialKcsMessage);
        when(stockTotalService.changeStockTotal(anyLong(), anyLong(), anyLong(), anyLong(),
                anyLong(), anyLong(), anyLong(), anyLong(), anyLong(), anyLong(), any(), anyString(),
                anyString(), any())).thenReturn(changeStockTotalMessage);
        when(updateSerialKcsMessage.isSuccess()).thenReturn(true);
        when(changeStockTotalMessage.isSuccess()).thenReturn(true);

        PowerMockito.mockStatic(DbUtil.class);
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );

        spyService.importKcs(userId, ownerId,mapKCS);
    }

    @Test
    public void testImportKcs_3() throws  Exception {
        Long userId=1L;
        Long ownerId=2L;
        KcsDTO kcsDTO = new KcsDTO();
        kcsDTO.setCode("43");
        HashMap<String, KcsDTO> mapKCS = new HashMap<>();
        mapKCS.put("1", kcsDTO);
        Staff staff = mock(Staff.class);
        Staff staff1 = mock(Staff.class);
        Shop shop = mock(Shop.class);
        Shop shop1 = mock(Shop.class);
        Long ownerShopId=1L;
        Long userShopId=2L;
        when(staffRepo.findOne(ownerId)).thenReturn(staff);
        when(staffRepo.findOne(userId)).thenReturn(staff1);
        when(staff.getShopId()).thenReturn(ownerShopId);
        when(staff1.getShopId()).thenReturn(userShopId);

        when(shopRepo.findOne(userShopId)).thenReturn(shop);//userShopPath
        when(shopRepo.findOne(ownerShopId)).thenReturn(shop1);//ownerShopPath
        when(shop.getShopPath()).thenReturn("2");//userShopPath
        when(shop1.getShopPath()).thenReturn("3");//ownerShopPath

        PowerMockito.mockStatic(DbUtil.class);
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        kcsService.importKcs(userId, ownerId,mapKCS);
    }

    @Test
    public void testImportKcs_4() throws  Exception {
        Long userId=1L;
        Long ownerId=1L;
        HashMap<String, KcsDTO> mapKCS = new HashMap<>();
        kcsService.importKcs(userId, ownerId,mapKCS);
    }

    @Test
    public void testImportKcs_5() throws  Exception {
        Long userId=1L;
        Long ownerId=null;
        KcsDTO kcsDTO = new KcsDTO();
        HashMap<String, KcsDTO> mapKCS = new HashMap<>();
        mapKCS.put("1", kcsDTO);
        kcsService.importKcs(userId, ownerId,mapKCS);
    }
    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method KcsServiceImpl.executeImportKcs
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void testExecuteImportKcs_1() throws  Exception {
        Long userId=1L;
        Long ownerId=1L;
        List<String> serialList = Lists.newArrayList();
        serialList.add("1");
        List<Long> stateIdList = Lists.newArrayList();
        stateIdList.add(3L);
        KcsDTO kcsDTO = new KcsDTO();
        kcsDTO.setCode("43");
        kcsDTO.setSerialList(serialList);
        kcsDTO.setStateIdList(stateIdList);
        HashMap<String, KcsDTO> mapKCS = new HashMap<>();
        mapKCS.put("1", kcsDTO);
        BaseMessage message = new BaseMessage();
        ProductOfferingDTO productOfferingDTO = mock(ProductOfferingDTO.class);
        when(productOfferingService.getProductByCode(any())).thenReturn(productOfferingDTO);
        when(productOfferingDTO.getProductOfferingId()).thenReturn(1L);

        PowerMockito.mockStatic(DbUtil.class);
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        kcsService.executeImportKcs(userId, ownerId, mapKCS, message);
    }

    @Test(expected = Exception.class)
    public void testExecuteImportKcs_2() throws  Exception {
        Long userId=1L;
        Long ownerId=1L;
        List<String> serialList = Lists.newArrayList();
        serialList.add("1");
        List<Long> stateIdList = Lists.newArrayList();
        stateIdList.add(12L);
        KcsDTO kcsDTO = new KcsDTO();
        kcsDTO.setCode("43");
        kcsDTO.setSerialList(serialList);
        kcsDTO.setStateIdList(stateIdList);
        HashMap<String, KcsDTO> mapKCS = new HashMap<>();
        mapKCS.put("1", kcsDTO);
        BaseMessage message = new BaseMessage();
        ProductOfferingDTO productOfferingDTO = mock(ProductOfferingDTO.class);
        when(productOfferingService.getProductByCode(any())).thenReturn(productOfferingDTO);
        when(productOfferingDTO.getProductOfferingId()).thenReturn(1L);

        PowerMockito.mockStatic(DbUtil.class);
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        kcsService.executeImportKcs(userId, ownerId, mapKCS, message);
    }

    @Test(expected = Exception.class)
    public void testExecuteImportKcs_3() throws  Exception {
        Long userId=1L;
        Long ownerId=1L;
        List<String> serialList = Lists.newArrayList();
        serialList.add("1");
        List<Long> stateIdList = Lists.newArrayList();
        stateIdList.add(2L);
        KcsDTO kcsDTO = new KcsDTO();
        kcsDTO.setCode("43");
        kcsDTO.setSerialList(serialList);
        kcsDTO.setStateIdList(stateIdList);
        HashMap<String, KcsDTO> mapKCS = new HashMap<>();
        mapKCS.put("1", kcsDTO);
        BaseMessage message = new BaseMessage();
        ProductOfferingDTO productOfferingDTO = mock(ProductOfferingDTO.class);
        when(productOfferingService.getProductByCode(any())).thenReturn(productOfferingDTO);
        when(productOfferingDTO.getProductOfferingId()).thenReturn(1L);

        PowerMockito.mockStatic(DbUtil.class);
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        kcsService.executeImportKcs(userId, ownerId, mapKCS, message);
    }

    @Test(expected = Exception.class)
    public void testExecuteImportKcs_4() throws  Exception {
        KcsServiceImpl spyService = PowerMockito.spy(kcsService);
        Long userId=1L;
        Long ownerId=1L;
        List<String[]> serialStrList = Lists.newArrayList();
        List<String> serialList = Lists.newArrayList();
        serialList.add("1");
        List<Long> stateIdList = Lists.newArrayList();
        stateIdList.add(3L);
        KcsDTO kcsDTO = new KcsDTO();
        kcsDTO.setCode("43");
        kcsDTO.setSerialList(serialList);
        kcsDTO.setStateIdList(stateIdList);
        HashMap<String, KcsDTO> mapKCS = new HashMap<>();
        mapKCS.put("1", kcsDTO);
        BaseMessage message = new BaseMessage();
        BaseMessage updateSerialKcsMessage = mock(BaseMessage.class);
        ProductOfferingDTO productOfferingDTO = mock(ProductOfferingDTO.class);
        when(productOfferingService.getProductByCode(any())).thenReturn(productOfferingDTO);
        when(productOfferingDTO.getProductOfferingId()).thenReturn(1L);
        when(spyService.updateSerialKCS(anyLong(), anyLong(), anyLong(), anyList())).thenReturn(updateSerialKcsMessage);
        when(updateSerialKcsMessage.isSuccess()).thenReturn(true);

        PowerMockito.mockStatic(DbUtil.class);
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.executeImportKcs(userId, ownerId, mapKCS, message);
    }

    @Test(expected = Exception.class)
    public void testExecuteImportKcs_5() throws  Exception {
        KcsServiceImpl spyService = PowerMockito.spy(kcsService);
        Long ownerId=1L;
        Long userId=1L;
        List<String[]> serialStrList = Lists.newArrayList();
        List<String> serialList = Lists.newArrayList();
        serialList.add("1");
        List<Long> stateIdList = Lists.newArrayList();
        stateIdList.add(3L);
        KcsDTO kcsDTO = new KcsDTO();
        kcsDTO.setCode("43");
        kcsDTO.setSerialList(serialList);
        kcsDTO.setStateIdList(stateIdList);
        HashMap<String, KcsDTO> mapKCS = new HashMap<>();
        mapKCS.put("1", kcsDTO);
        BaseMessage message = new BaseMessage();
        BaseMessage updateSerialKcsMessage = mock(BaseMessage.class);
        StockTotalMessage changeStockTotalMessage = mock(StockTotalMessage.class);
        ProductOfferingDTO productOfferingDTO = mock(ProductOfferingDTO.class);
        when(productOfferingService.getProductByCode(any())).thenReturn(productOfferingDTO);
        when(productOfferingDTO.getProductOfferingId()).thenReturn(1L);
        when(spyService.updateSerialKCS(anyLong(), anyLong(), anyLong(), anyList())).thenReturn(updateSerialKcsMessage);
        when(stockTotalService.changeStockTotal(anyLong(), anyLong(), anyLong(), anyLong(),
                anyLong(), anyLong(), anyLong(), anyLong(), anyLong(), anyLong(), any(), anyString(),
                anyString(), any())).thenReturn(changeStockTotalMessage);
        when(updateSerialKcsMessage.isSuccess()).thenReturn(true);
        when(changeStockTotalMessage.isSuccess()).thenReturn(false);

        PowerMockito.mockStatic(DbUtil.class);
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.executeImportKcs(userId, ownerId, mapKCS, message);
    }

    @Test(expected = Exception.class)
    public void testExecuteImportKcs_6() throws  Exception {
        KcsServiceImpl spyService = PowerMockito.spy(kcsService);
        Long ownerId=1L;
        Long userId=1L;
        List<String[]> serialStrList = Lists.newArrayList();
        List<String> serialList = Lists.newArrayList();
        serialList.add("1");
        List<Long> stateIdList = Lists.newArrayList();
        stateIdList.add(12L);
        KcsDTO kcsDTO = new KcsDTO();
        kcsDTO.setCode("43");
        kcsDTO.setSerialList(serialList);
        kcsDTO.setStateIdList(stateIdList);
        HashMap<String, KcsDTO> mapKCS = new HashMap<>();
        mapKCS.put("1", kcsDTO);
        BaseMessage message = new BaseMessage();
        BaseMessage updateSerialKcsMessage = mock(BaseMessage.class);
        StockTotalMessage changeStockTotalMessage = mock(StockTotalMessage.class);
        ProductOfferingDTO productOfferingDTO = mock(ProductOfferingDTO.class);
        when(productOfferingService.getProductByCode(any())).thenReturn(productOfferingDTO);
        when(productOfferingDTO.getProductOfferingId()).thenReturn(1L);
        when(spyService.updateSerialKCS(anyLong(), anyLong(), anyLong(), anyList())).thenReturn(updateSerialKcsMessage);
        when(stockTotalService.changeStockTotal(anyLong(), anyLong(), anyLong(), anyLong(),
                anyLong(), anyLong(), anyLong(), anyLong(), anyLong(), anyLong(), any(), anyString(),
                anyString(), any())).thenReturn(changeStockTotalMessage);
        when(updateSerialKcsMessage.isSuccess()).thenReturn(true);
        when(changeStockTotalMessage.isSuccess()).thenReturn(false);

        PowerMockito.mockStatic(DbUtil.class);
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.executeImportKcs(userId, ownerId, mapKCS, message);
    }

    @Test(expected = Exception.class)
    public void testExecuteImportKcs_7() throws  Exception {
        KcsServiceImpl spyService = PowerMockito.spy(kcsService);
        Long ownerId=1L;
        Long userId=1L;
        List<String[]> serialStrList = Lists.newArrayList();
        List<String> serialList = Lists.newArrayList();
//        serialList.add("1");
        List<Long> stateIdList = Lists.newArrayList();
        stateIdList.add(3L);
        KcsDTO kcsDTO = new KcsDTO();
        kcsDTO.setCode("43");
        kcsDTO.setSerialList(serialList);
        kcsDTO.setStateIdList(stateIdList);
        HashMap<String, KcsDTO> mapKCS = new HashMap<>();
        mapKCS.put("1", kcsDTO);
        BaseMessage message = new BaseMessage();
        BaseMessage updateSerialKcsMessage = mock(BaseMessage.class);
        StockTotalMessage changeStockTotalMessage = mock(StockTotalMessage.class);
        ProductOfferingDTO productOfferingDTO = mock(ProductOfferingDTO.class);
        when(productOfferingService.getProductByCode(any())).thenReturn(productOfferingDTO);
        when(productOfferingDTO.getProductOfferingId()).thenReturn(1L);
        when(spyService.updateSerialKCS(anyLong(), anyLong(), anyLong(), anyList())).thenReturn(updateSerialKcsMessage);
        when(stockTotalService.changeStockTotal(anyLong(), anyLong(), anyLong(), anyLong(),
                anyLong(), anyLong(), anyLong(), anyLong(), anyLong(), anyLong(), any(), anyString(),
                anyString(), any())).thenReturn(changeStockTotalMessage);
        when(updateSerialKcsMessage.isSuccess()).thenReturn(true);
        when(changeStockTotalMessage.isSuccess()).thenReturn(false);

        PowerMockito.mockStatic(DbUtil.class);
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.executeImportKcs(userId, ownerId, mapKCS, message);
    }

    @Test
    public void testExecuteImportKcs_8() throws  Exception {
        KcsServiceImpl spyService = PowerMockito.spy(kcsService);
        Long ownerId=1L;
        Long userId=1L;
        List<String> serialList = Lists.newArrayList();
        List<Long> stateIdList = Lists.newArrayList();
        stateIdList.add(3L);
        KcsDTO kcsDTO = new KcsDTO();
        kcsDTO.setCode("43");
        kcsDTO.setSerialList(serialList);
        kcsDTO.setStateIdList(stateIdList);
        HashMap<String, KcsDTO> mapKCS = new HashMap<>();
        mapKCS.put("1", kcsDTO);
        BaseMessage message = new BaseMessage();
        BaseMessage updateSerialKcsMessage = mock(BaseMessage.class);
        StockTotalMessage changeStockTotalMessage = mock(StockTotalMessage.class);
        ProductOfferingDTO productOfferingDTO = mock(ProductOfferingDTO.class);
        when(productOfferingService.getProductByCode(any())).thenReturn(productOfferingDTO);
        when(productOfferingDTO.getProductOfferingId()).thenReturn(1L);
        when(spyService.updateSerialKCS(anyLong(), anyLong(), anyLong(), anyList())).thenReturn(updateSerialKcsMessage);
        when(stockTotalService.changeStockTotal(anyLong(), anyLong(), anyLong(), anyLong(),
                anyLong(), anyLong(), anyLong(), anyLong(), anyLong(), anyLong(), any(), anyString(),
                anyString(), any())).thenReturn(changeStockTotalMessage);
        when(updateSerialKcsMessage.isSuccess()).thenReturn(true);
        when(changeStockTotalMessage.isSuccess()).thenReturn(true);

        PowerMockito.mockStatic(DbUtil.class);
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.executeImportKcs(userId, ownerId, mapKCS, message);
    }

    @Test
    public void testExecuteImportKcs_9() throws  Exception {
        KcsServiceImpl spyService = PowerMockito.spy(kcsService);
        Long ownerId=1L;
        Long userId=1L;
        List<String[]> serialStrList = Lists.newArrayList();
        List<String> serialList = Lists.newArrayList();
        serialList.add("1");
        List<Long> stateIdList = Lists.newArrayList();
        stateIdList.add(3L);
        KcsDTO kcsDTO = new KcsDTO();
        kcsDTO.setCode("43");
        kcsDTO.setSerialList(serialList);
        kcsDTO.setStateIdList(stateIdList);
        HashMap<String, KcsDTO> mapKCS = new HashMap<>();
        mapKCS.put("1", kcsDTO);
        BaseMessage message = new BaseMessage();
        BaseMessage updateSerialKcsMessage = mock(BaseMessage.class);
        StockTotalMessage changeStockTotalMessage = mock(StockTotalMessage.class);
        ProductOfferingDTO productOfferingDTO = mock(ProductOfferingDTO.class);
        when(productOfferingService.getProductByCode(any())).thenReturn(productOfferingDTO);
        when(productOfferingDTO.getProductOfferingId()).thenReturn(1L);
        when(spyService.updateSerialKCS(anyLong(), anyLong(), anyLong(), anyList())).thenReturn(updateSerialKcsMessage);
        when(stockTotalService.changeStockTotal(anyLong(), anyLong(), anyLong(), anyLong(),
                anyLong(), anyLong(), anyLong(), anyLong(), anyLong(), anyLong(), any(), anyString(),
                anyString(), any())).thenReturn(changeStockTotalMessage);
        when(updateSerialKcsMessage.isSuccess()).thenReturn(true);
        when(changeStockTotalMessage.isSuccess()).thenReturn(true);

        PowerMockito.mockStatic(DbUtil.class);
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.executeImportKcs(userId, ownerId, mapKCS, message);
    }

    @Test
    public void testExecuteImportKcs_10() throws  Exception {
        KcsServiceImpl spyService = PowerMockito.spy(kcsService);
        Long ownerId=1L;
        Long userId=1L;
        List<String[]> serialStrList = Lists.newArrayList();
        List<String> serialList = Lists.newArrayList();
        serialList.add("1");
        List<Long> stateIdList = Lists.newArrayList();
        stateIdList.add(12L);
        KcsDTO kcsDTO = new KcsDTO();
        kcsDTO.setCode("43");
        kcsDTO.setSerialList(serialList);
        kcsDTO.setStateIdList(stateIdList);
        HashMap<String, KcsDTO> mapKCS = new HashMap<>();
        mapKCS.put("1", kcsDTO);
        BaseMessage message = new BaseMessage();
        BaseMessage updateSerialKcsMessage = mock(BaseMessage.class);
        StockTotalMessage changeStockTotalMessage = mock(StockTotalMessage.class);
        ProductOfferingDTO productOfferingDTO = mock(ProductOfferingDTO.class);
        when(productOfferingService.getProductByCode(any())).thenReturn(productOfferingDTO);
        when(productOfferingDTO.getProductOfferingId()).thenReturn(1L);
        when(spyService.updateSerialKCS(anyLong(), anyLong(), anyLong(), anyList())).thenReturn(updateSerialKcsMessage);
        when(stockTotalService.changeStockTotal(anyLong(), anyLong(), anyLong(), anyLong(),
                anyLong(), anyLong(), anyLong(), anyLong(), anyLong(), anyLong(), any(), anyString(),
                anyString(), any())).thenReturn(changeStockTotalMessage);
        when(updateSerialKcsMessage.isSuccess()).thenReturn(true);
        when(changeStockTotalMessage.isSuccess()).thenReturn(true);

        PowerMockito.mockStatic(DbUtil.class);
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.executeImportKcs(userId, ownerId, mapKCS, message);
    }
    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method KcsServiceImpl.updateSerialKCS
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testUpdateSerialKCS_1() throws  Exception {
        Long ownerId=1L;
        Long kcsLogId=1L;
        Long prodOfferId=1L;
        List<String[]> serialList = Lists.newArrayList();
        kcsService.updateSerialKCS(ownerId,kcsLogId,prodOfferId,serialList);
    }

    @Test
    public void testUpdateSerialKCS_2() throws  Exception {
        Long ownerId=1L;
        Long kcsLogId=1L;
        Long prodOfferId=1L;
        String[] strings = new String[]{"a","3"};
        List<String[]> serialList = Lists.newArrayList();
        serialList.add(strings);
        StockHandset stockHandset = new StockHandset();

        when(stockHandsetRepo.findBySerialForUpdate(any(), any(), any())).thenReturn(stockHandset);
        when(stockHandsetRepo.updateForStockRescue(any(), any(), any(), any())).thenReturn(0);

        PowerMockito.mockStatic(DbUtil.class);
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        kcsService.updateSerialKCS(ownerId,kcsLogId,prodOfferId,serialList);
    }

    @Test
    public void testUpdateSerialKCS_3() throws  Exception {
        Long ownerId=1L;
        Long kcsLogId=1L;
        Long prodOfferId=1L;
        String[] strings = new String[]{"a","3"};
        List<String[]> serialList = Lists.newArrayList();
        serialList.add(strings);
        StockHandset stockHandset = new StockHandset();

        when(stockHandsetRepo.findBySerialForUpdate(any(), any(), any())).thenReturn(stockHandset);
        when(stockHandsetRepo.updateForStockRescue(any(), any(), any(), any())).thenReturn(1);
        when(stockHandsetRepo.updateForStockRescueIM1(any(), any(), any(), any())).thenReturn(0);

        PowerMockito.mockStatic(DbUtil.class);
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        kcsService.updateSerialKCS(ownerId,kcsLogId,prodOfferId,serialList);
    }

    @Test
    public void testUpdateSerialKCS_4() throws  Exception {
        Long ownerId=1L;
        Long kcsLogId=1L;
        Long prodOfferId=1L;
        String[] strings = new String[]{"a","3"};
        List<String[]> serialList = Lists.newArrayList();
        serialList.add(strings);
        StockHandset stockHandset = new StockHandset();

        when(stockHandsetRepo.findBySerialForUpdate(any(), any(), any())).thenReturn(stockHandset);
        when(stockHandsetRepo.updateForStockRescue(any(), any(), any(), any())).thenReturn(1);
        when(stockHandsetRepo.updateForStockRescueIM1(any(), any(), any(), any())).thenReturn(1);

        PowerMockito.mockStatic(DbUtil.class);
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        kcsService.updateSerialKCS(ownerId,kcsLogId,prodOfferId,serialList);
    }

    @Test
    public void testUpdateSerialKCS_5() throws  Exception {
        Long ownerId=1L;
        Long kcsLogId=1L;
        Long prodOfferId=1L;
        String[] strings = new String[]{"a","1"};
        List<String[]> serialList = Lists.newArrayList();
        serialList.add(strings);

        PowerMockito.mockStatic(DbUtil.class);
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        kcsService.updateSerialKCS(ownerId,kcsLogId,prodOfferId,serialList);
    }

    @Test
    public void testUpdateSerialKCS_6() throws  Exception {
        Long ownerId=null;
        Long kcsLogId=1L;
        Long prodOfferId=1L;
        List<String[]> serialList = Lists.newArrayList();
        serialList.add(new String[]{"1"});
        kcsService.updateSerialKCS(ownerId,kcsLogId,prodOfferId,serialList);
    }

    @Test
    public void testUpdateSerialKCS_7() throws  Exception {
        Long ownerId=1L;
        Long kcsLogId=null;
        Long prodOfferId=1L;
        List<String[]> serialList = Lists.newArrayList();
        serialList.add(new String[]{"1"});
        kcsService.updateSerialKCS(ownerId,kcsLogId,prodOfferId,serialList);
    }

    @Test
    public void testUpdateSerialKCS_8() throws  Exception {
        Long ownerId=1L;
        Long kcsLogId=1L;
        Long prodOfferId=null;
        List<String[]> serialList = Lists.newArrayList();
        serialList.add(new String[]{"1"});
        kcsService.updateSerialKCS(ownerId,kcsLogId,prodOfferId,serialList);
    }

    @Test
    public void testUpdateSerialKCS_9() throws  Exception {
        Long ownerId=1L;
        Long kcsLogId=1L;
        Long prodOfferId=1L;
        String[] strings = new String[]{"a","12"};
        List<String[]> serialList = Lists.newArrayList();
        serialList.add(strings);
        StockHandset stockHandset = new StockHandset();

        when(stockHandsetRepo.findBySerialForUpdate(any(), any(), any())).thenReturn(stockHandset);
        when(stockHandsetRepo.updateForStockRescue(any(), any(), any(), any())).thenReturn(0);

        PowerMockito.mockStatic(DbUtil.class);
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        kcsService.updateSerialKCS(ownerId,kcsLogId,prodOfferId,serialList);
    }

    @Test
    public void testUpdateSerialKCS_10() throws  Exception {
        Long ownerId=1L;
        Long kcsLogId=1L;
        Long prodOfferId=1L;
        String[] strings = new String[]{"a","12"};
        List<String[]> serialList = Lists.newArrayList();
        serialList.add(strings);
        StockHandset stockHandset = null;

        when(stockHandsetRepo.findBySerialForUpdate(any(), any(), any())).thenReturn(stockHandset);
        when(stockHandsetRepo.updateForStockRescue(any(), any(), any(), any())).thenReturn(0);

        PowerMockito.mockStatic(DbUtil.class);
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        kcsService.updateSerialKCS(ownerId,kcsLogId,prodOfferId,serialList);
    }

    @Test
    public void testUpdateSerialKCS_11() throws  Exception {
        Long ownerId=1L;
        Long kcsLogId=1L;
        Long prodOfferId=1L;
        String[] strings = new String[]{"","12"};
        List<String[]> serialList = Lists.newArrayList();
        serialList.add(strings);
        StockHandset stockHandset = new StockHandset();

        when(stockHandsetRepo.findBySerialForUpdate(any(), any(), any())).thenReturn(stockHandset);
        when(stockHandsetRepo.updateForStockRescue(any(), any(), any(), any())).thenReturn(0);

        PowerMockito.mockStatic(DbUtil.class);
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        kcsService.updateSerialKCS(ownerId,kcsLogId,prodOfferId,serialList);
    }

    @Test
    public void testUpdateSerialKCS_12() throws  Exception {
        Long ownerId=1L;
        Long kcsLogId=1L;
        Long prodOfferId=1L;
        String[] strings = new String[]{"1", "1" };
        List<String[]> serialList = Lists.newArrayList();
        serialList.add(strings);
        StockHandset stockHandset = new StockHandset();

        when(stockHandsetRepo.findBySerialForUpdate(any(), any(), any())).thenReturn(stockHandset);
        when(stockHandsetRepo.updateForStockRescue(any(), any(), any(), any())).thenReturn(0);

        PowerMockito.mockStatic(DbUtil.class);
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        kcsService.updateSerialKCS(ownerId,kcsLogId,prodOfferId,serialList);
    }
}