package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockTransRescue;
import com.viettel.bccs.inventory.repo.StockHandsetRepo;
import com.viettel.bccs.inventory.repo.StockTransRescueRepo;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.service.BaseServiceImpl;
import org.junit.Assert;
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
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author DatLT
 * @date 10/13/2017
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({DbUtil.class, StockTransRescueServiceImpl.class, BaseServiceImpl.class, StockTransRescueService.class})
public class StockTransRescueServiceImplTest {
    @InjectMocks
    StockTransRescueServiceImpl stockTransRescueService;
    @Mock
    private final BaseMapper<StockTransRescue, StockTransRescueDTO> mapper = new BaseMapper<>(StockTransRescue.class, StockTransRescueDTO.class);
    @Mock
    private EntityManager em;
    @Mock
    private ShopService shopService;
    @Mock
    private ProductOfferingService productOfferingService;
    @Mock
    private StaffService staffService;
    @Mock
    private StockHandsetRescueService stockHandsetRescueService;
    @Mock
    private StockTransDetailRescueService stockTransDetailRescueService;
    @Mock
    private StockTransSerialRescueService stockTransSerialRescueService;
    @Mock
    private StockTransActionRescueService stockTransActionRescueService;
    @Mock
    private StockHandsetService stockHandsetService;
    @Mock
    private StockHandsetRepo stockHandsetRepo;
    @Mock
    private StockTransRescueRepo repository;
    @Mock
    private StockTotalService stockTotalService;
    @Mock
    private ReasonService reasonService;
    @Mock
    private StockTransService stockTransService;
    @Mock
    private StockTransActionService stockTransActionService;
    @Mock
    private StockTransDetailService stockTransDetailService;
    @Mock
    private StockTransSerialService stockTransSerialService;
    @Mock
    private OptionSetValueService optionSetValueService;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransRescueServiceImpl.count
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Test
    public void testCount_1() throws Exception {
        List<FilterRequest> filters = Lists.newArrayList();
        Mockito.when(repository.count(any())).thenReturn(1L);
        Assert.assertEquals(1L, stockTransRescueService.count(filters).longValue());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransRescueServiceImpl.findOne
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindOne_1() throws  Exception {
        StockTransRescueServiceImpl spyService = PowerMockito.spy(stockTransRescueService);
        StockTransRescue stockTransRescue = mock(StockTransRescue.class);
        StockTransRescueDTO stockTransRescueDTO = new StockTransRescueDTO();
        Mockito.when(repository.findOne(anyLong())).thenReturn(stockTransRescue);
        setFinalStatic(StockTransRescueServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(mapper.toDtoBean(stockTransRescue)).thenReturn(stockTransRescueDTO);
        spyService.findOne(1L);
        Assert.assertNotNull(stockTransRescueDTO);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransRescueServiceImpl.findAll
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindAll_1() throws  Exception {
        StockTransRescueServiceImpl spyService = PowerMockito.spy(stockTransRescueService);
        StockTransRescue stockTransRescue = mock(StockTransRescue.class);
        StockTransRescueDTO stockTransRescueDTO = new StockTransRescueDTO();
        List<StockTransRescueDTO> stockTransRescueDTOList = Lists.newArrayList(stockTransRescueDTO);
        List<StockTransRescue> stockTransRescueList = Lists.newArrayList(stockTransRescue);
        Mockito.when(repository.findAll()).thenReturn(stockTransRescueList);
        Mockito.when(mapper.toDtoBean(stockTransRescueList)).thenReturn(stockTransRescueDTOList);
        setFinalStatic(StockTransRescueServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findAll();
        Assert.assertNotNull(stockTransRescueDTOList);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransRescueServiceImpl.findByFilter
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Test
    public void testFindByFilter_1() throws  Exception {
        FilterRequest filterRequest = new FilterRequest();
        List<FilterRequest> filterRequestList = Lists.newArrayList(filterRequest);
        StockTransRescueServiceImpl spyService = PowerMockito.spy(stockTransRescueService);
        StockTransRescue stockTransRescue = mock(StockTransRescue.class);
        StockTransRescueDTO stockTransRescueDTO = new StockTransRescueDTO();
        List<StockTransRescueDTO> stockTransRescueDTOList = Lists.newArrayList(stockTransRescueDTO);
        List<StockTransRescue> stockTransRescueList = Lists.newArrayList(stockTransRescue);
        Mockito.when(mapper.toDtoBean(stockTransRescueList)).thenReturn(stockTransRescueDTOList);
        setFinalStatic(StockTransRescueServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findByFilter(filterRequestList);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransRescueServiceImpl.create
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Test(expected = Exception.class)
    public void testCreate_1() throws  Exception {
            stockTransRescueService.create(new StockTransRescueDTO());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransRescueServiceImpl.update
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Test(expected = Exception.class)
    public void testUpdate_1() throws  Exception {
        stockTransRescueService.update(new StockTransRescueDTO());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransRescueServiceImpl.searchStockRescue
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Test(expected = Exception.class)
    public void testSearchStockRescue_1() throws  Exception {
        StockTransRescueDTO stockTransRescueDTO = new StockTransRescueDTO();
        stockTransRescueDTO.setFromDate(new Date());
        stockTransRescueService.searchStockRescue(stockTransRescueDTO);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransRescueServiceImpl.searchStockRescue
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Test(expected = Exception.class)
    public void testSearchStockRescue_2() throws  Exception {
        StockTransRescueDTO stockTransRescueDTO = new StockTransRescueDTO();
        stockTransRescueDTO.setFromDate(new Date());
        stockTransRescueDTO.setToDate(new Date(17, 9,11));
        stockTransRescueService.searchStockRescue(stockTransRescueDTO);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransRescueServiceImpl.searchStockRescue
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Test
    public void testSearchStockRescue_3() throws  Exception {
        StockTransRescueDTO stockTransRescueDTO = new StockTransRescueDTO();
        stockTransRescueDTO.setFromDate(new Date());
        stockTransRescueDTO.setToDate(new Date(2017, 12,11));
        stockTransRescueService.searchStockRescue(stockTransRescueDTO);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransRescueServiceImpl.getMaxId
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Test
    public void testGetMaxId_1() throws  Exception {
        stockTransRescueService.getMaxId();
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransRescueServiceImpl.getReasonId
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Test
    public void testGetReasonId_1() throws  Exception {
        stockTransRescueService.getReasonId();
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransRescueServiceImpl.createRequest
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Test(expected = Exception.class)
    public void testCreateRequest_1() throws  Exception {
        StockTransRescueDTO stockTransRescueAdd = new StockTransRescueDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        requiredRoleMap.hasRole(Const.PERMISION.ROLE_RECEIVE_WARRANTY);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        stockTransRescueService.createRequest(stockTransRescueAdd, requiredRoleMap);
    }

    @Test(expected = Exception.class)
    public void testCreateRequest_2() throws  Exception {
        StockTransRescueDTO stockTransRescueAdd = new StockTransRescueDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        requiredRoleMap.add(Const.PERMISION.ROLE_EXPORT_RETURN_WARRANTY);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        stockTransRescueService.createRequest(stockTransRescueAdd, requiredRoleMap);
    }

    @Test(expected = Exception.class)
    public void testCreateRequest_3() throws  Exception {
        StockTransRescueDTO stockTransRescueAdd = new StockTransRescueDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        requiredRoleMap.add(Const.PERMISION.ROLE_EXPORT_RETURN_WARRANTY);
        stockTransRescueAdd.setRequestCode("1");

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        stockTransRescueService.createRequest(stockTransRescueAdd, requiredRoleMap);
    }

    @Test(expected = Exception.class)
    public void testCreateRequest_4() throws  Exception {
        StockTransRescueDTO stockTransRescueAdd = new StockTransRescueDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        requiredRoleMap.add(Const.PERMISION.ROLE_EXPORT_RETURN_WARRANTY);
        stockTransRescueAdd.setRequestCode("1");
        stockTransRescueAdd.setStaffRequest("1");

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        stockTransRescueService.createRequest(stockTransRescueAdd, requiredRoleMap);
    }

    @Test(expected = Exception.class)
    public void testCreateRequest_5() throws  Exception {
        StockTransRescueDTO stockTransRescueAdd = new StockTransRescueDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        requiredRoleMap.add(Const.PERMISION.ROLE_EXPORT_RETURN_WARRANTY);
        stockTransRescueAdd.setRequestCode("1");
        stockTransRescueAdd.setStaffRequest("1");
        stockTransRescueAdd.setToOwnerId(1L);

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        stockTransRescueService.createRequest(stockTransRescueAdd, requiredRoleMap);
    }

    @Test(expected = Exception.class)
    public void testCreateRequest_6() throws  Exception {
        StockTransRescueDTO stockTransRescueAdd = new StockTransRescueDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        requiredRoleMap.add(Const.PERMISION.ROLE_EXPORT_RETURN_WARRANTY);
        stockTransRescueAdd.setRequestCode("1");
        stockTransRescueAdd.setStaffRequest("1");
        stockTransRescueAdd.setToOwnerId(1L);
        ShopDTO shopDTO = new ShopDTO();

        when(shopService.findOne(stockTransRescueAdd.getToOwnerId())).thenReturn(shopDTO);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        stockTransRescueService.createRequest(stockTransRescueAdd, requiredRoleMap);
    }

    @Test(expected = Exception.class)
    public void testCreateRequest_7() throws  Exception {
        StockTransRescueServiceImpl spyService = PowerMockito.spy(stockTransRescueService);
        StockHandsetRescueDTO stockHandsetRescueDTO = new StockHandsetRescueDTO();
        stockHandsetRescueDTO.setSerial("1");
        List<StockHandsetRescueDTO> lstSelection = Lists.newArrayList(stockHandsetRescueDTO);
        StockTransRescueDTO stockTransRescueAdd = new StockTransRescueDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        requiredRoleMap.add(Const.PERMISION.ROLE_EXPORT_RETURN_WARRANTY);
        stockTransRescueAdd.setRequestCode("1");
        stockTransRescueAdd.setStaffRequest("1");
        stockTransRescueAdd.setToOwnerId(1L);
        stockTransRescueAdd.setLstSelection(lstSelection);

        ShopDTO shopDTO = new ShopDTO();
        StockTransRescue stockTransRescue = mock(StockTransRescue.class);


        setFinalStatic(StockTransRescueServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(shopService.findOne(stockTransRescueAdd.getToOwnerId())).thenReturn(shopDTO);
        when(repository.isDulicateRequestCode(anyString())).thenReturn(true);
        when(mapper.toPersistenceBean(stockTransRescueAdd)).thenReturn(stockTransRescue);
        when(repository.save(stockTransRescue)).thenReturn(stockTransRescue);

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.createRequest(stockTransRescueAdd, requiredRoleMap);
    }

    @Test(expected = Exception.class)
    public void testCreateRequest_8() throws  Exception {
        StockTransRescueServiceImpl spyService = PowerMockito.spy(stockTransRescueService);
        StockHandsetRescueDTO stockHandsetRescueDTO = mock(StockHandsetRescueDTO.class);
        stockHandsetRescueDTO.setSerial("1");
        List<StockHandsetRescueDTO> lstSelection = Lists.newArrayList(stockHandsetRescueDTO);
        StockTransRescueDTO stockTransRescueAdd = new StockTransRescueDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        requiredRoleMap.add(Const.PERMISION.ROLE_EXPORT_RETURN_WARRANTY);
        stockTransRescueAdd.setRequestCode("1");
        stockTransRescueAdd.setStaffRequest("1");
        stockTransRescueAdd.setToOwnerId(1L);
        stockTransRescueAdd.setLstSelection(lstSelection);
        stockTransRescueAdd.setStockTransId(1L);

        ShopDTO shopDTO = new ShopDTO();
        StockTransRescue stockTransRescue = mock(StockTransRescue.class);
        List<StockHandsetRescueDTO> lstStockHandset = Lists.newArrayList(stockHandsetRescueDTO);

        setFinalStatic(StockTransRescueServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(shopService.findOne(stockTransRescueAdd.getToOwnerId())).thenReturn(shopDTO);
        when(repository.isDulicateRequestCode(anyString())).thenReturn(true);
        when(mapper.toPersistenceBean(stockTransRescueAdd)).thenReturn(stockTransRescue);
        when(stockHandsetRescueService.getListProductForRescue(any(), any())).thenReturn(lstStockHandset);
        when(repository.save(stockTransRescue)).thenReturn(stockTransRescue);
        when(stockTransRescue.getStockTransId()).thenReturn(1L);

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.createRequest(stockTransRescueAdd, requiredRoleMap);
    }

    @Test
    public void testCreateRequest_9() throws  Exception {
        StockTransRescueServiceImpl spyService = PowerMockito.spy(stockTransRescueService);
        StockHandsetRescueDTO stockHandsetRescueDTO = mock(StockHandsetRescueDTO.class);
        stockHandsetRescueDTO.setSerial("1");
        List<StockHandsetRescueDTO> lstSelection = Lists.newArrayList(stockHandsetRescueDTO);
        StockTransRescueDTO stockTransRescueAdd = new StockTransRescueDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        requiredRoleMap.add(Const.PERMISION.ROLE_EXPORT_RETURN_WARRANTY);
        stockTransRescueAdd.setRequestCode("1");
        stockTransRescueAdd.setStaffRequest("1");
        stockTransRescueAdd.setToOwnerId(1L);
        stockTransRescueAdd.setLstSelection(lstSelection);
        stockTransRescueAdd.setStockTransId(1L);

        ShopDTO shopDTO = new ShopDTO();
        StockTransRescue stockTransRescue = mock(StockTransRescue.class);
        List<StockHandsetRescueDTO> lstStockHandset = Lists.newArrayList(stockHandsetRescueDTO);
        List<StockHandsetRescueDTO> lstStockSerial =Lists.newArrayList(stockHandsetRescueDTO);

        setFinalStatic(StockTransRescueServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(shopService.findOne(stockTransRescueAdd.getToOwnerId())).thenReturn(shopDTO);
        when(repository.isDulicateRequestCode(anyString())).thenReturn(true);
        when(mapper.toPersistenceBean(stockTransRescueAdd)).thenReturn(stockTransRescue);
        when(stockHandsetRescueService.getListProductForRescue(any(), any())).thenReturn(lstStockHandset);
        when(repository.save(stockTransRescue)).thenReturn(stockTransRescue);
        when(stockTransRescue.getStockTransId()).thenReturn(1L);
        when( stockHandsetRescueService.getListSerialForRescue(any(), any())).thenReturn(lstStockSerial);

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.createRequest(stockTransRescueAdd, requiredRoleMap);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransRescueServiceImpl.actionStockRescue
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Test(expected = Exception.class)
    public void testActionStockRescue_1() throws  Exception {
        StockTransRescueDTO stockTransRescueAction = new StockTransRescueDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        Long statusBefor = 1L;
        Long statusAffer =2L;
        StockTransRescueServiceImpl spyService = PowerMockito.spy(stockTransRescueService);

        stockTransRescueAction.setRole("1");

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        Whitebox.invokeMethod(stockTransRescueService, "actionStockRescue", stockTransRescueAction, requiredRoleMap, statusBefor, statusAffer);
    }

    @Test(expected = Exception.class)
    public void testActionStockRescue_2() throws  Exception {
        StockTransRescueDTO stockTransRescueAction = new StockTransRescueDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        Long statusBefor = 1L;
        Long statusAffer =2L;
        StockTransRescueServiceImpl spyService = PowerMockito.spy(stockTransRescueService);

        StockTransRescue stockTransRescue = mock(StockTransRescue.class);
        StockTransRescueDTO stockTransRescueDTO = new StockTransRescueDTO();
        Mockito.when(repository.findOne(anyLong())).thenReturn(stockTransRescue);
        when(mapper.toDtoBean(stockTransRescue)).thenReturn(stockTransRescueDTO);
        setFinalStatic(StockTransRescueServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(spyService.findOne(anyLong())).thenReturn(null);

        requiredRoleMap.add("1");
        stockTransRescueAction.setRole("1");

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        Whitebox.invokeMethod(spyService, "actionStockRescue", stockTransRescueAction, requiredRoleMap, statusBefor, statusAffer);
    }

    @Test(expected = Exception.class)
    public void testActionStockRescue_3() throws  Exception {
        StockTransRescueDTO stockTransRescueAction = new StockTransRescueDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        Long statusBefor = 1L;
        Long statusAffer =2L;
        StockTransRescueServiceImpl spyService = PowerMockito.spy(stockTransRescueService);

        StockTransRescue stockTransRescue = mock(StockTransRescue.class);
        StockTransRescueDTO stockTransRescueDTO = mock(StockTransRescueDTO.class);
        Mockito.when(repository.findOne(anyLong())).thenReturn(stockTransRescue);
        when(mapper.toDtoBean(stockTransRescue)).thenReturn(stockTransRescueDTO);
        setFinalStatic(StockTransRescueServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(spyService.findOne(anyLong())).thenReturn(stockTransRescueDTO);
        when(stockTransRescueDTO.getStatus()).thenReturn(2L);

        requiredRoleMap.add("1");
        stockTransRescueAction.setRole("1");

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        Whitebox.invokeMethod(spyService, "actionStockRescue", stockTransRescueAction, requiredRoleMap, statusBefor, statusAffer);
    }

    @Test
    public void testActionStockRescue_4() throws  Exception {
        StockTransRescueDTO stockTransRescueAction = new StockTransRescueDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        Long statusBefor = 1L;
        Long statusAffer =2L;
        StockTransSerialRescueDTO stockTransSerialRescueDTO = new StockTransSerialRescueDTO();
        List<StockTransSerialRescueDTO> lstSerial = Lists.newArrayList(stockTransSerialRescueDTO);
        StockTransRescueServiceImpl spyService = PowerMockito.spy(stockTransRescueService);

        StockTransRescue stockTransRescue = mock(StockTransRescue.class);
        StockTransRescueDTO stockTransRescueDTO = mock(StockTransRescueDTO.class);
        Mockito.when(repository.findOne(anyLong())).thenReturn(stockTransRescue);
        when(mapper.toDtoBean(stockTransRescue)).thenReturn(stockTransRescueDTO);
        setFinalStatic(StockTransRescueServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(spyService.findOne(anyLong())).thenReturn(stockTransRescueDTO);
        when(stockTransRescueDTO.getStatus()).thenReturn(1L);
        when(stockTransSerialRescueService.viewDetailSerailByStockTransId(stockTransRescueAction.getStockTransId())).thenReturn(lstSerial);

        requiredRoleMap.add("1");
        stockTransRescueAction.setRole("1");

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        Whitebox.invokeMethod(spyService, "actionStockRescue", stockTransRescueAction, requiredRoleMap, statusBefor, statusAffer);
    }

    @Test(expected = Exception.class)
    public void testActionStockRescue_5() throws  Exception {
        StockTransRescueDTO stockTransRescueAction = new StockTransRescueDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        Long statusBefor = 1L;
        Long statusAffer =2L;
        StockTransSerialRescueDTO stockTransSerialRescueDTO = new StockTransSerialRescueDTO();
        List<StockTransSerialRescueDTO> lstSerial = Lists.newArrayList(stockTransSerialRescueDTO);
        StockTransRescueServiceImpl spyService = PowerMockito.spy(stockTransRescueService);

        StockTransRescue stockTransRescue = mock(StockTransRescue.class);
        StockTransRescueDTO stockTransRescueDTO = mock(StockTransRescueDTO.class);
        Mockito.when(repository.findOne(anyLong())).thenReturn(stockTransRescue);
        when(mapper.toDtoBean(stockTransRescue)).thenReturn(stockTransRescueDTO);
        setFinalStatic(StockTransRescueServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(spyService.findOne(anyLong())).thenReturn(stockTransRescueDTO);
        when(stockTransRescueDTO.getStatus()).thenReturn(1L);
        when(stockTransSerialRescueService.viewDetailSerailByStockTransId(stockTransRescueAction.getStockTransId())).thenReturn(null);

        requiredRoleMap.add("1");
        stockTransRescueAction.setRole("1");

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        Whitebox.invokeMethod(spyService, "actionStockRescue", stockTransRescueAction, requiredRoleMap, statusBefor, statusAffer);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransRescueServiceImpl.deleteStockRescue
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Test
    public void testDeleteStockRescue_1() throws  Exception {
        StockTransRescueServiceImpl spyService = PowerMockito.spy(stockTransRescueService);
        StockTransRescueDTO stockTransRescueAction= new StockTransRescueDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        requiredRoleMap.add(Const.PERMISION.ROLE_EXPORT_RETURN_WARRANTY);
        StockTransSerialRescueDTO stockTransSerialRescueDTO = new StockTransSerialRescueDTO();
        List<StockTransSerialRescueDTO> lstSerial = Lists.newArrayList(stockTransSerialRescueDTO);

        StockTransRescue stockTransRescue = mock(StockTransRescue.class);
        StockTransRescueDTO stockTransRescueDTO = mock(StockTransRescueDTO.class);
        Mockito.when(repository.findOne(anyLong())).thenReturn(stockTransRescue);
        when(mapper.toDtoBean(stockTransRescue)).thenReturn(stockTransRescueDTO);
        setFinalStatic(StockTransRescueServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(stockTransRescueDTO.getStatus()).thenReturn(1L);
        when(spyService.findOne(anyLong())).thenReturn(stockTransRescueDTO);
        when(stockTransSerialRescueService.viewDetailSerailByStockTransId(stockTransRescueAction.getStockTransId())).thenReturn(lstSerial);

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.deleteStockRescue(stockTransRescueAction, requiredRoleMap);
    }

    @Test(expected = Exception.class)
    public void testDeleteStockRescue_2() throws  Exception {
        StockTransRescueServiceImpl spyService = PowerMockito.spy(stockTransRescueService);
        StockTransRescueDTO stockTransRescueAction= new StockTransRescueDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        requiredRoleMap.add(Const.PERMISION.ROLE_EXPORT_RETURN_WARRANTY);
        StockTransSerialRescueDTO stockTransSerialRescueDTO = new StockTransSerialRescueDTO();
        List<StockTransSerialRescueDTO> lstSerial = Lists.newArrayList(stockTransSerialRescueDTO);

        StockTransRescue stockTransRescue = mock(StockTransRescue.class);
        StockTransRescueDTO stockTransRescueDTO = mock(StockTransRescueDTO.class);
        Mockito.when(repository.findOne(anyLong())).thenReturn(stockTransRescue);
        when(mapper.toDtoBean(stockTransRescue)).thenReturn(stockTransRescueDTO);
        setFinalStatic(StockTransRescueServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(stockTransRescueDTO.getStatus()).thenReturn(1L);
        when(spyService.findOne(anyLong())).thenReturn(stockTransRescueDTO);
        when(stockTransSerialRescueService.viewDetailSerailByStockTransId(stockTransRescueAction.getStockTransId())).thenReturn(null);

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        PowerMockito.doReturn(null).when(spyService, "actionStockRescue", any(), any(), any(), any());
        spyService.deleteStockRescue(stockTransRescueAction, requiredRoleMap);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransRescueServiceImpl.receiveStockRescue
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Test(expected = Exception.class)
    public void testReceiveStockRescue_1() throws  Exception {
        StockTransRescueServiceImpl spyService = PowerMockito.spy(stockTransRescueService);
        StockTransRescueDTO stockTransRescueAction = new StockTransRescueDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        requiredRoleMap.add(Const.PERMISION.ROLE_EXPORT_RETURN_WARRANTY);
        StockTransSerialRescueDTO stockTransSerialRescueDTO = new StockTransSerialRescueDTO();
        List<StockTransSerialRescueDTO> lstSerial = Lists.newArrayList(stockTransSerialRescueDTO);

        StockTransRescue stockTransRescue = mock(StockTransRescue.class);
        StockTransRescueDTO stockTransRescueDTO = mock(StockTransRescueDTO.class);
        Mockito.when(repository.findOne(anyLong())).thenReturn(stockTransRescue);
        when(mapper.toDtoBean(stockTransRescue)).thenReturn(stockTransRescueDTO);
        setFinalStatic(StockTransRescueServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(stockTransRescueDTO.getStatus()).thenReturn(1L);
        when(spyService.findOne(anyLong())).thenReturn(stockTransRescueDTO);

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        PowerMockito.doReturn(null).when(spyService, "actionStockRescue", any(), any(), any(), any());
        spyService.receiveStockRescue(stockTransRescueAction, requiredRoleMap);
    }

    @Test(expected = Exception.class)
    public void testReceiveStockRescue_2() throws  Exception {
        StockTransRescueServiceImpl spyService = PowerMockito.spy(stockTransRescueService);
        StockTransRescueDTO stockTransRescueAction = new StockTransRescueDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        requiredRoleMap.add(Const.PERMISION.ROLE_EXPORT_RETURN_WARRANTY);
        StockTransSerialRescueDTO stockTransSerialRescueDTO = new StockTransSerialRescueDTO();
        List<StockTransSerialRescueDTO> lstSerial = Lists.newArrayList(stockTransSerialRescueDTO);
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        StockTransRescue stockTransRescue = mock(StockTransRescue.class);
        StockTransRescueDTO stockTransRescueDTO = mock(StockTransRescueDTO.class);
        StaffDTO staffDTO = new StaffDTO();

        Mockito.when(repository.findOne(anyLong())).thenReturn(stockTransRescue);
        when(mapper.toDtoBean(stockTransRescue)).thenReturn(stockTransRescueDTO);
        setFinalStatic(StockTransRescueServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(stockTransRescueDTO.getStatus()).thenReturn(1L);
        when(spyService.findOne(anyLong())).thenReturn(stockTransRescueDTO);
        when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        when(staffService.findOne(stockTransRescueAction.getFromOwnerId())).thenReturn(staffDTO);

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        PowerMockito.doReturn(lstSerial).when(spyService, "actionStockRescue", any(), any(), any(), any());
        spyService.receiveStockRescue(stockTransRescueAction, requiredRoleMap);
    }

    @Test(expected = Exception.class)
    public void testReceiveStockRescue_3() throws  Exception {
        StockTransRescueServiceImpl spyService = PowerMockito.spy(stockTransRescueService);
        StockTransRescueDTO stockTransRescueAction = new StockTransRescueDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        requiredRoleMap.add(Const.PERMISION.ROLE_EXPORT_RETURN_WARRANTY);
        StockTransSerialRescueDTO stockTransSerialRescueDTO = mock(StockTransSerialRescueDTO.class);
        List<StockTransSerialRescueDTO> lstSerial = Lists.newArrayList(stockTransSerialRescueDTO);
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        StockTransRescue stockTransRescue = mock(StockTransRescue.class);
        StockTransRescueDTO stockTransRescueDTO = mock(StockTransRescueDTO.class);
        StaffDTO staffDTO = new StaffDTO();
        ProductOfferingDTO productOfferingDTO = mock(ProductOfferingDTO.class);

        Mockito.when(repository.findOne(anyLong())).thenReturn(stockTransRescue);
        when(mapper.toDtoBean(stockTransRescue)).thenReturn(stockTransRescueDTO);
        setFinalStatic(StockTransRescueServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(stockTransRescueDTO.getStatus()).thenReturn(1L);
        when(spyService.findOne(anyLong())).thenReturn(stockTransRescueDTO);
        when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        when(staffService.findOne(stockTransRescueAction.getFromOwnerId())).thenReturn(staffDTO);
        when(stockTransSerialRescueDTO.getProdOfferIdReturn()).thenReturn(1L);
        when(stockTransSerialRescueDTO.getProdOfferId()).thenReturn(1L);
        when(stockTransSerialRescueDTO.getFromSerial()).thenReturn("1");
        when(stockTransSerialRescueDTO.getSerialReturn()).thenReturn("1");
        when(productOfferingService.findOne(anyLong())).thenReturn(productOfferingDTO);

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        PowerMockito.doReturn(lstSerial).when(spyService, "actionStockRescue", any(), any(), any(), any());
        spyService.receiveStockRescue(stockTransRescueAction, requiredRoleMap);
    }

    @Test(expected = Exception.class)
    public void testReceiveStockRescue_4() throws  Exception {
        StockTransRescueServiceImpl spyService = PowerMockito.spy(stockTransRescueService);
        StockTransRescueDTO stockTransRescueAction = new StockTransRescueDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        requiredRoleMap.add(Const.PERMISION.ROLE_EXPORT_RETURN_WARRANTY);
        StockTransSerialRescueDTO stockTransSerialRescueDTO = mock(StockTransSerialRescueDTO.class);
        List<StockTransSerialRescueDTO> lstSerial = Lists.newArrayList(stockTransSerialRescueDTO);
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue("1");
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        StockTransRescue stockTransRescue = mock(StockTransRescue.class);
        StockTransRescueDTO stockTransRescueDTO = mock(StockTransRescueDTO.class);
        StaffDTO staffDTO = new StaffDTO();
        ProductOfferingDTO productOfferingDTO = mock(ProductOfferingDTO.class);

        Mockito.when(repository.findOne(anyLong())).thenReturn(stockTransRescue);
        when(mapper.toDtoBean(stockTransRescue)).thenReturn(stockTransRescueDTO);
        setFinalStatic(StockTransRescueServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(stockTransRescueDTO.getStatus()).thenReturn(1L);
        when(spyService.findOne(anyLong())).thenReturn(stockTransRescueDTO);
        when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        when(staffService.findOne(stockTransRescueAction.getFromOwnerId())).thenReturn(staffDTO);
        when(stockTransSerialRescueDTO.getProdOfferIdReturn()).thenReturn(1L);
        when(stockTransSerialRescueDTO.getProdOfferId()).thenReturn(1L);
        when(stockTransSerialRescueDTO.getFromSerial()).thenReturn("1");
        when(stockTransSerialRescueDTO.getSerialReturn()).thenReturn("1");
        when(productOfferingService.findOne(anyLong())).thenReturn(productOfferingDTO);
        when(stockHandsetService.updateForStockRescue(Const.STATE_STATUS.RETRIEVE, stockTransRescueDTO.getFromOwnerId(), stockTransSerialRescueDTO.getFromSerial(), stockTransSerialRescueDTO.getProdOfferId())).thenReturn(1);

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        PowerMockito.doReturn(lstSerial).when(spyService, "actionStockRescue", any(), any(), any(), any());
        spyService.receiveStockRescue(stockTransRescueAction, requiredRoleMap);
    }

    @Test(expected = Exception.class)
    public void testReceiveStockRescue_5() throws  Exception {
        StockTransRescueServiceImpl spyService = PowerMockito.spy(stockTransRescueService);
        StockTransRescueDTO stockTransRescueAction = new StockTransRescueDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        requiredRoleMap.add(Const.PERMISION.ROLE_EXPORT_RETURN_WARRANTY);
        StockTransSerialRescueDTO stockTransSerialRescueDTO = mock(StockTransSerialRescueDTO.class);
        List<StockTransSerialRescueDTO> lstSerial = Lists.newArrayList(stockTransSerialRescueDTO);
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue("1");
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        StockTransRescue stockTransRescue = mock(StockTransRescue.class);
        StockTransRescueDTO stockTransRescueDTO = mock(StockTransRescueDTO.class);
        StaffDTO staffDTO = new StaffDTO();
        ProductOfferingDTO productOfferingDTO = mock(ProductOfferingDTO.class);

        Mockito.when(repository.findOne(anyLong())).thenReturn(stockTransRescue);
        when(mapper.toDtoBean(stockTransRescue)).thenReturn(stockTransRescueDTO);
        setFinalStatic(StockTransRescueServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(stockTransRescueDTO.getStatus()).thenReturn(1L);
        when(spyService.findOne(anyLong())).thenReturn(stockTransRescueDTO);
        when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        when(staffService.findOne(stockTransRescueAction.getFromOwnerId())).thenReturn(staffDTO);
        when(stockTransSerialRescueDTO.getProdOfferIdReturn()).thenReturn(1L);
        when(stockTransSerialRescueDTO.getProdOfferId()).thenReturn(2L);
        when(stockTransSerialRescueDTO.getFromSerial()).thenReturn("1");
        when(stockTransSerialRescueDTO.getSerialReturn()).thenReturn("1");
        when(productOfferingService.findOne(anyLong())).thenReturn(productOfferingDTO);
        when(stockHandsetService.updateForStockRescue(Const.STATE_STATUS.RETRIEVE, stockTransRescueDTO.getFromOwnerId(), stockTransSerialRescueDTO.getFromSerial(), stockTransSerialRescueDTO.getProdOfferId())).thenReturn(1);

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        PowerMockito.doReturn(lstSerial).when(spyService, "actionStockRescue", any(), any(), any(), any());
        spyService.receiveStockRescue(stockTransRescueAction, requiredRoleMap);
    }

    @Test(expected = Exception.class)
    public void testReceiveStockRescue_6() throws  Exception {
        StockTransRescueServiceImpl spyService = PowerMockito.spy(stockTransRescueService);
        StockTransRescueDTO stockTransRescueAction = new StockTransRescueDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        requiredRoleMap.add(Const.PERMISION.ROLE_EXPORT_RETURN_WARRANTY);
        StockTransSerialRescueDTO stockTransSerialRescueDTO = mock(StockTransSerialRescueDTO.class);
        List<StockTransSerialRescueDTO> lstSerial = Lists.newArrayList(stockTransSerialRescueDTO);
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue("1");
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        StockTransRescue stockTransRescue = mock(StockTransRescue.class);
        StockTransRescueDTO stockTransRescueDTO = mock(StockTransRescueDTO.class);
        StaffDTO staffDTO = new StaffDTO();
        ProductOfferingDTO productOfferingDTO = mock(ProductOfferingDTO.class);
        StockHandsetDTO stockHandsetDTO = new StockHandsetDTO();

        Mockito.when(repository.findOne(anyLong())).thenReturn(stockTransRescue);
        when(mapper.toDtoBean(stockTransRescue)).thenReturn(stockTransRescueDTO);
        setFinalStatic(StockTransRescueServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(stockTransRescueDTO.getStatus()).thenReturn(1L);
        when(spyService.findOne(anyLong())).thenReturn(stockTransRescueDTO);
        when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        when(staffService.findOne(stockTransRescueAction.getFromOwnerId())).thenReturn(staffDTO);
        when(stockTransSerialRescueDTO.getProdOfferIdReturn()).thenReturn(1L);
        when(stockTransSerialRescueDTO.getProdOfferId()).thenReturn(2L);
        when(stockTransSerialRescueDTO.getFromSerial()).thenReturn("1");
        when(stockTransSerialRescueDTO.getSerialReturn()).thenReturn("1");
        when(productOfferingService.findOne(anyLong())).thenReturn(productOfferingDTO);
        when(stockHandsetService.updateForStockRescue(Const.STATE_STATUS.RETRIEVE, stockTransRescueDTO.getFromOwnerId(), stockTransSerialRescueDTO.getFromSerial(), stockTransSerialRescueDTO.getProdOfferId())).thenReturn(1);
        when(stockHandsetRepo.getStockHandset(stockTransSerialRescueDTO.getProdOfferId(), stockTransSerialRescueDTO.getFromSerial(),
                Const.OWNER_TYPE.STAFF_LONG, stockTransRescueDTO.getFromOwnerId(), null)).thenReturn(stockHandsetDTO);

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        PowerMockito.doReturn(lstSerial).when(spyService, "actionStockRescue", any(), any(), any(), any());
        spyService.receiveStockRescue(stockTransRescueAction, requiredRoleMap);
    }

    @Test(expected = Exception.class)
    public void testReceiveStockRescue_7() throws  Exception {
        StockTransRescueServiceImpl spyService = PowerMockito.spy(stockTransRescueService);
        StockTransRescueDTO stockTransRescueAction = new StockTransRescueDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        requiredRoleMap.add(Const.PERMISION.ROLE_EXPORT_RETURN_WARRANTY);
        StockTransSerialRescueDTO stockTransSerialRescueDTO = mock(StockTransSerialRescueDTO.class);
        List<StockTransSerialRescueDTO> lstSerial = Lists.newArrayList(stockTransSerialRescueDTO);
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue("2");
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        StockTransRescue stockTransRescue = mock(StockTransRescue.class);
        StockTransRescueDTO stockTransRescueDTO = mock(StockTransRescueDTO.class);
        StaffDTO staffDTO = new StaffDTO();
        ProductOfferingDTO productOfferingDTO = mock(ProductOfferingDTO.class);
        StockHandsetDTO stockHandsetDTO = new StockHandsetDTO();

        Mockito.when(repository.findOne(anyLong())).thenReturn(stockTransRescue);
        when(mapper.toDtoBean(stockTransRescue)).thenReturn(stockTransRescueDTO);
        setFinalStatic(StockTransRescueServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(stockTransRescueDTO.getStatus()).thenReturn(1L);
        when(spyService.findOne(anyLong())).thenReturn(stockTransRescueDTO);
        when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        when(staffService.findOne(stockTransRescueAction.getFromOwnerId())).thenReturn(staffDTO);
        when(stockTransSerialRescueDTO.getProdOfferIdReturn()).thenReturn(1L);
        when(stockTransSerialRescueDTO.getProdOfferId()).thenReturn(2L);
        when(stockTransSerialRescueDTO.getFromSerial()).thenReturn("1");
        when(stockTransSerialRescueDTO.getSerialReturn()).thenReturn("1");
        when(productOfferingService.findOne(anyLong())).thenReturn(productOfferingDTO);
        when(stockHandsetService.updateForStockRescue(Const.STATE_STATUS.RETRIEVE, stockTransRescueDTO.getFromOwnerId(), stockTransSerialRescueDTO.getFromSerial(), stockTransSerialRescueDTO.getProdOfferId())).thenReturn(1);
        when(stockHandsetRepo.getStockHandset(stockTransSerialRescueDTO.getProdOfferId(), stockTransSerialRescueDTO.getFromSerial(),
                Const.OWNER_TYPE.STAFF_LONG, stockTransRescueDTO.getFromOwnerId(), null)).thenReturn(stockHandsetDTO);

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        PowerMockito.doReturn(lstSerial).when(spyService, "actionStockRescue", any(), any(), any(), any());
        spyService.receiveStockRescue(stockTransRescueAction, requiredRoleMap);
    }

    @Test(expected = Exception.class)
    public void testReceiveStockRescue_8() throws  Exception {
        StockTransRescueServiceImpl spyService = PowerMockito.spy(stockTransRescueService);
        StockTransRescueDTO stockTransRescueAction = new StockTransRescueDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        requiredRoleMap.add(Const.PERMISION.ROLE_EXPORT_RETURN_WARRANTY);
        StockTransSerialRescueDTO stockTransSerialRescueDTO = mock(StockTransSerialRescueDTO.class);
        List<StockTransSerialRescueDTO> lstSerial = Lists.newArrayList(stockTransSerialRescueDTO);
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue("2");
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        StockTransRescue stockTransRescue = mock(StockTransRescue.class);
        StockTransRescueDTO stockTransRescueDTO = mock(StockTransRescueDTO.class);
        StaffDTO staffDTO = new StaffDTO();
        ProductOfferingDTO productOfferingDTO = mock(ProductOfferingDTO.class);
        StockHandsetDTO stockHandsetDTO = new StockHandsetDTO();
        StockTotalMessage resultStockTotal = mock(StockTotalMessage.class);


        Mockito.when(repository.findOne(anyLong())).thenReturn(stockTransRescue);
        when(mapper.toDtoBean(stockTransRescue)).thenReturn(stockTransRescueDTO);
        setFinalStatic(StockTransRescueServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(stockTransRescueDTO.getStatus()).thenReturn(1L);
        when(spyService.findOne(anyLong())).thenReturn(stockTransRescueDTO);
        when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        when(staffService.findOne(stockTransRescueAction.getFromOwnerId())).thenReturn(staffDTO);
        when(stockTransSerialRescueDTO.getProdOfferIdReturn()).thenReturn(1L);
        when(stockTransSerialRescueDTO.getProdOfferId()).thenReturn(2L);
        when(stockTransSerialRescueDTO.getFromSerial()).thenReturn("1");
        when(stockTransSerialRescueDTO.getSerialReturn()).thenReturn("1");
        when(productOfferingService.findOne(anyLong())).thenReturn(productOfferingDTO);
        when(stockHandsetService.updateForStockRescue(Const.STATE_STATUS.RETRIEVE, stockTransRescueDTO.getFromOwnerId(), stockTransSerialRescueDTO.getFromSerial(), stockTransSerialRescueDTO.getProdOfferId())).thenReturn(1);
        when(stockHandsetRepo.getStockHandset(stockTransSerialRescueDTO.getProdOfferId(), stockTransSerialRescueDTO.getFromSerial(),
                Const.OWNER_TYPE.STAFF_LONG, stockTransRescueDTO.getFromOwnerId(), null)).thenReturn(stockHandsetDTO);

        when(stockTotalService.changeStockTotal(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(resultStockTotal);
        when( resultStockTotal.isSuccess()).thenReturn( true, false );

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        PowerMockito.doReturn(lstSerial).when(spyService, "actionStockRescue", any(), any(), any(), any());
        spyService.receiveStockRescue(stockTransRescueAction, requiredRoleMap);
    }

    @Test(expected = Exception.class)
    public void testReceiveStockRescue_9() throws  Exception {
        StockTransRescueServiceImpl spyService = PowerMockito.spy(stockTransRescueService);
        StockTransRescueDTO stockTransRescueAction = new StockTransRescueDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        requiredRoleMap.add(Const.PERMISION.ROLE_EXPORT_RETURN_WARRANTY);
        StockTransSerialRescueDTO stockTransSerialRescueDTO = mock(StockTransSerialRescueDTO.class);
        List<StockTransSerialRescueDTO> lstSerial = Lists.newArrayList(stockTransSerialRescueDTO);
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue("2");
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        StockTransRescue stockTransRescue = mock(StockTransRescue.class);
        StockTransRescueDTO stockTransRescueDTO = mock(StockTransRescueDTO.class);
        StaffDTO staffDTO = new StaffDTO();
        ProductOfferingDTO productOfferingDTO = mock(ProductOfferingDTO.class);
        StockHandsetDTO stockHandsetDTO = new StockHandsetDTO();
        StockTotalMessage resultStockTotal = mock(StockTotalMessage.class);

        Mockito.when(repository.findOne(anyLong())).thenReturn(stockTransRescue);
        when(mapper.toDtoBean(stockTransRescue)).thenReturn(stockTransRescueDTO);
        setFinalStatic(StockTransRescueServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(stockTransRescueDTO.getStatus()).thenReturn(1L);
        when(spyService.findOne(anyLong())).thenReturn(stockTransRescueDTO);
        when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        when(staffService.findOne(stockTransRescueAction.getFromOwnerId())).thenReturn(staffDTO);
        when(stockTransSerialRescueDTO.getProdOfferIdReturn()).thenReturn(1L);
        when(stockTransSerialRescueDTO.getProdOfferId()).thenReturn(1L);
        when(stockTransSerialRescueDTO.getFromSerial()).thenReturn("2");
        when(stockTransSerialRescueDTO.getSerialReturn()).thenReturn("1");
        when(productOfferingService.findOne(anyLong())).thenReturn(productOfferingDTO);
        when(stockHandsetService.updateForStockRescue(Const.STATE_STATUS.RETRIEVE, stockTransRescueDTO.getFromOwnerId(), stockTransSerialRescueDTO.getFromSerial(), stockTransSerialRescueDTO.getProdOfferId())).thenReturn(1);
        when(stockHandsetRepo.getStockHandset(stockTransSerialRescueDTO.getProdOfferId(), stockTransSerialRescueDTO.getFromSerial(),
                Const.OWNER_TYPE.STAFF_LONG, stockTransRescueDTO.getFromOwnerId(), null)).thenReturn(stockHandsetDTO);
        when(stockTotalService.changeStockTotal(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(resultStockTotal);
        when(resultStockTotal.isSuccess()).thenReturn(true);
        when(spyService.getText("REASON_EXP_CHANGE_DEMAGED_GOODS")).thenReturn(null);

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        PowerMockito.doReturn(lstSerial).when(spyService, "actionStockRescue", any(), any(), any(), any());
        spyService.receiveStockRescue(stockTransRescueAction, requiredRoleMap);
    }

    @Test(expected = Exception.class)
    public void testReceiveStockRescue_10() throws  Exception {
        StockTransRescueServiceImpl spyService = PowerMockito.spy(stockTransRescueService);
        StockTransRescueDTO stockTransRescueAction = new StockTransRescueDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        requiredRoleMap.add(Const.PERMISION.ROLE_EXPORT_RETURN_WARRANTY);
        StockTransSerialRescueDTO stockTransSerialRescueDTO = mock(StockTransSerialRescueDTO.class);
        List<StockTransSerialRescueDTO> lstSerial = Lists.newArrayList(stockTransSerialRescueDTO);
        OptionSetValueDTO optionSetValueDTO = mock(OptionSetValueDTO.class);
//        optionSetValueDTO.setValue("1");
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        StockTransRescue stockTransRescue = mock(StockTransRescue.class);
        StockTransRescueDTO stockTransRescueDTO = mock(StockTransRescueDTO.class);
        StaffDTO staffDTO = new StaffDTO();
        ProductOfferingDTO productOfferingDTO = mock(ProductOfferingDTO.class);
        StockHandsetDTO stockHandsetDTO = new StockHandsetDTO();
        StockTotalMessage resultStockTotal = mock(StockTotalMessage.class);

        Mockito.when(repository.findOne(anyLong())).thenReturn(stockTransRescue);
        when(mapper.toDtoBean(stockTransRescue)).thenReturn(stockTransRescueDTO);
        setFinalStatic(StockTransRescueServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(stockTransRescueDTO.getStatus()).thenReturn(1L);
        when(spyService.findOne(anyLong())).thenReturn(stockTransRescueDTO);
        when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        when(staffService.findOne(stockTransRescueAction.getFromOwnerId())).thenReturn(staffDTO);
        when(stockTransSerialRescueDTO.getProdOfferIdReturn()).thenReturn(1L);
        when(stockTransSerialRescueDTO.getProdOfferId()).thenReturn(1L);
        when(stockTransSerialRescueDTO.getFromSerial()).thenReturn("2");
        when(stockTransSerialRescueDTO.getSerialReturn()).thenReturn("1");
        when(productOfferingService.findOne(anyLong())).thenReturn(productOfferingDTO);
        when(stockHandsetService.updateForStockRescue(Const.STATE_STATUS.RETRIEVE, stockTransRescueDTO.getFromOwnerId(), stockTransSerialRescueDTO.getFromSerial(), stockTransSerialRescueDTO.getProdOfferId())).thenReturn(1);
        when(stockHandsetRepo.getStockHandset(stockTransSerialRescueDTO.getProdOfferId(), stockTransSerialRescueDTO.getFromSerial(),
                Const.OWNER_TYPE.STAFF_LONG, stockTransRescueDTO.getFromOwnerId(), null)).thenReturn(stockHandsetDTO);
        when(stockTotalService.changeStockTotal(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(resultStockTotal);
        when(stockHandsetRepo.updateStockHandsetIM1(any(), any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(1);
        when(resultStockTotal.isSuccess()).thenReturn(true);
        when(spyService.getText("REASON_EXP_CHANGE_DEMAGED_GOODS")).thenReturn(null);
        when(optionSetValueDTO.getValue()).thenReturn("1");


        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        PowerMockito.doReturn(lstSerial).when(spyService, "actionStockRescue", any(), any(), any(), any());
        spyService.receiveStockRescue(stockTransRescueAction, requiredRoleMap);
    }

    @Test(expected = Exception.class)
    public void testReceiveStockRescue_11() throws  Exception {
        StockTransRescueServiceImpl spyService = PowerMockito.spy(stockTransRescueService);
        StockTransRescueDTO stockTransRescueAction = new StockTransRescueDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        requiredRoleMap.add(Const.PERMISION.ROLE_EXPORT_RETURN_WARRANTY);
        StockTransSerialRescueDTO stockTransSerialRescueDTO = mock(StockTransSerialRescueDTO.class);
        List<StockTransSerialRescueDTO> lstSerial = Lists.newArrayList(stockTransSerialRescueDTO);
        OptionSetValueDTO optionSetValueDTO = mock(OptionSetValueDTO.class);
//        optionSetValueDTO.setValue("1");
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        StockTransRescue stockTransRescue = mock(StockTransRescue.class);
        StockTransRescueDTO stockTransRescueDTO = mock(StockTransRescueDTO.class);
        StaffDTO staffDTO = new StaffDTO();
        ProductOfferingDTO productOfferingDTO = mock(ProductOfferingDTO.class);
        StockHandsetDTO stockHandsetDTO = new StockHandsetDTO();
        StockTotalMessage resultStockTotal = mock(StockTotalMessage.class);

        Mockito.when(repository.findOne(anyLong())).thenReturn(stockTransRescue);
        when(mapper.toDtoBean(stockTransRescue)).thenReturn(stockTransRescueDTO);
        setFinalStatic(StockTransRescueServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(stockTransRescueDTO.getStatus()).thenReturn(1L);
        when(spyService.findOne(anyLong())).thenReturn(stockTransRescueDTO);
        when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        when(staffService.findOne(stockTransRescueAction.getFromOwnerId())).thenReturn(staffDTO);
        when(stockTransSerialRescueDTO.getProdOfferIdReturn()).thenReturn(1L);
        when(stockTransSerialRescueDTO.getProdOfferId()).thenReturn(1L);
        when(stockTransSerialRescueDTO.getFromSerial()).thenReturn("2");
        when(stockTransSerialRescueDTO.getSerialReturn()).thenReturn("1");
        when(productOfferingService.findOne(anyLong())).thenReturn(productOfferingDTO);
        when(stockHandsetService.updateForStockRescue(Const.STATE_STATUS.RETRIEVE, stockTransRescueDTO.getFromOwnerId(), stockTransSerialRescueDTO.getFromSerial(), stockTransSerialRescueDTO.getProdOfferId())).thenReturn(1);
        when(stockHandsetRepo.getStockHandset(stockTransSerialRescueDTO.getProdOfferId(), stockTransSerialRescueDTO.getFromSerial(),
                Const.OWNER_TYPE.STAFF_LONG, stockTransRescueDTO.getFromOwnerId(), null)).thenReturn(stockHandsetDTO);
        when(stockTotalService.changeStockTotal(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(resultStockTotal);
        when(stockHandsetRepo.updateStockHandsetIM1(any(), any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(1);
        when(resultStockTotal.isSuccess()).thenReturn(true);
        when(spyService.getText("REASON_EXP_CHANGE_DEMAGED_GOODS")).thenReturn(null);
        when(optionSetValueDTO.getValue()).thenReturn("2");

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        PowerMockito.doReturn(lstSerial).when(spyService, "actionStockRescue", any(), any(), any(), any());
        spyService.receiveStockRescue(stockTransRescueAction, requiredRoleMap);
    }

    @Test(expected = Exception.class)
    public void testReceiveStockRescue_12() throws  Exception {
        StockTransRescueServiceImpl spyService = PowerMockito.spy(stockTransRescueService);
        StockTransRescueDTO stockTransRescueAction = new StockTransRescueDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        requiredRoleMap.add(Const.PERMISION.ROLE_EXPORT_RETURN_WARRANTY);
        StockTransSerialRescueDTO stockTransSerialRescueDTO = mock(StockTransSerialRescueDTO.class);
        List<StockTransSerialRescueDTO> lstSerial = Lists.newArrayList(stockTransSerialRescueDTO);
        OptionSetValueDTO optionSetValueDTO = mock(OptionSetValueDTO.class);
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        StockTransRescue stockTransRescue = mock(StockTransRescue.class);
        StockTransRescueDTO stockTransRescueDTO = mock(StockTransRescueDTO.class);
        StaffDTO staffDTO = new StaffDTO();
        ProductOfferingDTO productOfferingDTO = mock(ProductOfferingDTO.class);
        StockHandsetDTO stockHandsetDTO = new StockHandsetDTO();
        StockTotalMessage resultStockTotal = mock(StockTotalMessage.class);

        Mockito.when(repository.findOne(anyLong())).thenReturn(stockTransRescue);
        when(mapper.toDtoBean(stockTransRescue)).thenReturn(stockTransRescueDTO);
        setFinalStatic(StockTransRescueServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(stockTransRescueDTO.getStatus()).thenReturn(1L);
        when(spyService.findOne(anyLong())).thenReturn(stockTransRescueDTO);
        when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        when(staffService.findOne(stockTransRescueAction.getFromOwnerId())).thenReturn(staffDTO);
        when(stockTransSerialRescueDTO.getProdOfferIdReturn()).thenReturn(1L);
        when(stockTransSerialRescueDTO.getProdOfferId()).thenReturn(1L);
        when(stockTransSerialRescueDTO.getFromSerial()).thenReturn("2");
        when(stockTransSerialRescueDTO.getSerialReturn()).thenReturn("1");
        when(productOfferingService.findOne(anyLong())).thenReturn(productOfferingDTO);
        when(stockHandsetService.updateForStockRescue(Const.STATE_STATUS.RETRIEVE, stockTransRescueDTO.getFromOwnerId(), stockTransSerialRescueDTO.getFromSerial(), stockTransSerialRescueDTO.getProdOfferId())).thenReturn(1);
        when(stockHandsetRepo.getStockHandset(stockTransSerialRescueDTO.getProdOfferId(), stockTransSerialRescueDTO.getFromSerial(),
                Const.OWNER_TYPE.STAFF_LONG, stockTransRescueDTO.getFromOwnerId(), null)).thenReturn(stockHandsetDTO);
        when(stockTotalService.changeStockTotal(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(resultStockTotal);
        when(stockHandsetRepo.updateStockHandsetIM1(any(), any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(1);
        when(resultStockTotal.isSuccess()).thenReturn(true);
        when(spyService.getText("REASON_EXP_CHANGE_DEMAGED_GOODS")).thenReturn(null);
        when(optionSetValueDTO.getValue()).thenReturn("1");

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        PowerMockito.doReturn(lstSerial).when(spyService, "actionStockRescue", any(), any(), any(), any());
        spyService.receiveStockRescue(stockTransRescueAction, requiredRoleMap);
    }

    @Test(expected = Exception.class)
    public void testReceiveStockRescue_13() throws  Exception {
        StockTransRescueServiceImpl spyService = PowerMockito.spy(stockTransRescueService);
        StockTransRescueDTO stockTransRescueAction = new StockTransRescueDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        requiredRoleMap.add(Const.PERMISION.ROLE_EXPORT_RETURN_WARRANTY);
        StockTransSerialRescueDTO stockTransSerialRescueDTO = mock(StockTransSerialRescueDTO.class);
        List<StockTransSerialRescueDTO> lstSerial = Lists.newArrayList(stockTransSerialRescueDTO);
        OptionSetValueDTO optionSetValueDTO = mock(OptionSetValueDTO.class);
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        StockTransRescue stockTransRescue = mock(StockTransRescue.class);
        StockTransRescueDTO stockTransRescueDTO = mock(StockTransRescueDTO.class);
        StaffDTO staffDTO = new StaffDTO();
        ProductOfferingDTO productOfferingDTO = mock(ProductOfferingDTO.class);
        StockHandsetDTO stockHandsetDTO = new StockHandsetDTO();
        StockTotalMessage resultStockTotal = mock(StockTotalMessage.class);
        StockHandsetDTO stockHandsetOld = mock(StockHandsetDTO.class);

        Mockito.when(repository.findOne(anyLong())).thenReturn(stockTransRescue);
        when(mapper.toDtoBean(stockTransRescue)).thenReturn(stockTransRescueDTO);
        setFinalStatic(StockTransRescueServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(stockTransRescueDTO.getStatus()).thenReturn(1L);
        when(spyService.findOne(anyLong())).thenReturn(stockTransRescueDTO);
        when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        when(staffService.findOne(stockTransRescueAction.getFromOwnerId())).thenReturn(staffDTO);
        when(stockTransSerialRescueDTO.getProdOfferIdReturn()).thenReturn(1L);
        when(stockTransSerialRescueDTO.getProdOfferId()).thenReturn(1L);
        when(stockTransSerialRescueDTO.getFromSerial()).thenReturn("2");
        when(stockTransSerialRescueDTO.getSerialReturn()).thenReturn("1");
        when(productOfferingService.findOne(anyLong())).thenReturn(productOfferingDTO);
        when(stockHandsetService.updateForStockRescue(Const.STATE_STATUS.RETRIEVE, stockTransRescueDTO.getFromOwnerId(), stockTransSerialRescueDTO.getFromSerial(), stockTransSerialRescueDTO.getProdOfferId())).thenReturn(1);
        when(stockHandsetRepo.getStockHandset(stockTransSerialRescueDTO.getProdOfferId(), stockTransSerialRescueDTO.getFromSerial(),
                Const.OWNER_TYPE.STAFF_LONG, stockTransRescueDTO.getFromOwnerId(), null)).thenReturn(stockHandsetDTO);
        when(stockHandsetRepo.getStockHandset(stockTransSerialRescueDTO.getProdOfferId(), stockTransSerialRescueDTO.getSerialReturn(), null, null, null)).thenReturn(stockHandsetOld);
        when(stockTotalService.changeStockTotal(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(resultStockTotal);
        when(stockHandsetRepo.updateStockHandsetIM1(any(), any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(1);
        when(resultStockTotal.isSuccess()).thenReturn(true);
        when(spyService.getText("REASON_EXP_CHANGE_DEMAGED_GOODS")).thenReturn(null);
        when(optionSetValueDTO.getValue()).thenReturn("2");


        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        PowerMockito.doReturn(lstSerial).when(spyService, "actionStockRescue", any(), any(), any(), any());
        spyService.receiveStockRescue(stockTransRescueAction, requiredRoleMap);
    }

    @Test(expected = Exception.class)
    public void testReceiveStockRescue_14() throws  Exception {
        StockTransRescueServiceImpl spyService = PowerMockito.spy(stockTransRescueService);
        StockTransRescueDTO stockTransRescueAction = new StockTransRescueDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        requiredRoleMap.add(Const.PERMISION.ROLE_EXPORT_RETURN_WARRANTY);
        StockTransSerialRescueDTO stockTransSerialRescueDTO = mock(StockTransSerialRescueDTO.class);
        List<StockTransSerialRescueDTO> lstSerial = Lists.newArrayList(stockTransSerialRescueDTO);
        OptionSetValueDTO optionSetValueDTO = mock(OptionSetValueDTO.class);
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        StockTransRescue stockTransRescue = mock(StockTransRescue.class);
        StockTransRescueDTO stockTransRescueDTO = mock(StockTransRescueDTO.class);
        StaffDTO staffDTO = new StaffDTO();
        ProductOfferingDTO productOfferingDTO = mock(ProductOfferingDTO.class);
        StockHandsetDTO stockHandsetDTO = new StockHandsetDTO();
        StockTotalMessage resultStockTotal = mock(StockTotalMessage.class);
        StockHandsetDTO stockHandsetOld = mock(StockHandsetDTO.class);

        Mockito.when(repository.findOne(anyLong())).thenReturn(stockTransRescue);
        when(mapper.toDtoBean(stockTransRescue)).thenReturn(stockTransRescueDTO);
        setFinalStatic(StockTransRescueServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(stockTransRescueDTO.getStatus()).thenReturn(1L);
        when(spyService.findOne(anyLong())).thenReturn(stockTransRescueDTO);
        when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        when(staffService.findOne(stockTransRescueAction.getFromOwnerId())).thenReturn(staffDTO);
        when(stockTransSerialRescueDTO.getProdOfferIdReturn()).thenReturn(1L);
        when(stockTransSerialRescueDTO.getProdOfferId()).thenReturn(1L);
        when(stockTransSerialRescueDTO.getFromSerial()).thenReturn("2");
        when(stockTransSerialRescueDTO.getSerialReturn()).thenReturn("1");
        when(productOfferingService.findOne(anyLong())).thenReturn(productOfferingDTO);
        when(stockHandsetService.updateForStockRescue(Const.STATE_STATUS.RETRIEVE, stockTransRescueDTO.getFromOwnerId(), stockTransSerialRescueDTO.getFromSerial(), stockTransSerialRescueDTO.getProdOfferId())).thenReturn(1);
        when(stockHandsetRepo.getStockHandset(stockTransSerialRescueDTO.getProdOfferId(), stockTransSerialRescueDTO.getFromSerial(),
                Const.OWNER_TYPE.STAFF_LONG, stockTransRescueDTO.getFromOwnerId(), null)).thenReturn(stockHandsetDTO);
        when(stockHandsetRepo.getStockHandset(stockTransSerialRescueDTO.getProdOfferId(), stockTransSerialRescueDTO.getSerialReturn(), null, null, null)).thenReturn(stockHandsetOld);
        when(stockTotalService.changeStockTotal(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(resultStockTotal);
        when(stockHandsetRepo.updateStockHandsetIM1(any(), any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(1);
        when(resultStockTotal.isSuccess()).thenReturn(true);
        when(spyService.getText("REASON_EXP_CHANGE_DEMAGED_GOODS")).thenReturn(null);
        when(optionSetValueDTO.getValue()).thenReturn("1");
        when(stockHandsetRepo.getStockHandset(anyLong(), anyString(), any(),any(), anyLong())).thenReturn(stockHandsetOld);
        when(stockHandsetRepo.updateStockHandsetIM1(Const.STATE_STATUS.RETRIEVE, stockTransRescueDTO.getFromOwnerId(), Const.OWNER_TYPE.STAFF_LONG, null, Const.STATE_STATUS.NEW,
                stockTransRescueDTO.getToOwnerId(), Const.OWNER_TYPE.SHOP_LONG, DataUtil.safeToLong(Const.STATUS_ACTIVE), stockTransSerialRescueDTO.getSerialReturn(), 1L)).thenReturn(0);

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        PowerMockito.doReturn(lstSerial).when(spyService, "actionStockRescue", any(), any(), any(), any());
        spyService.receiveStockRescue(stockTransRescueAction, requiredRoleMap);
    }

    @Test(expected = Exception.class)
    public void testReceiveStockRescue_15() throws  Exception {
        StockTransRescueServiceImpl spyService = PowerMockito.spy(stockTransRescueService);
        StockTransRescueDTO stockTransRescueAction = new StockTransRescueDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        requiredRoleMap.add(Const.PERMISION.ROLE_EXPORT_RETURN_WARRANTY);
        StockTransSerialRescueDTO stockTransSerialRescueDTO = mock(StockTransSerialRescueDTO.class);
        List<StockTransSerialRescueDTO> lstSerial = Lists.newArrayList(stockTransSerialRescueDTO);
        OptionSetValueDTO optionSetValueDTO = mock(OptionSetValueDTO.class);
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        StockTransRescue stockTransRescue = mock(StockTransRescue.class);
        StockTransRescueDTO stockTransRescueDTO = mock(StockTransRescueDTO.class);
        StaffDTO staffDTO = new StaffDTO();
        ProductOfferingDTO productOfferingDTO = mock(ProductOfferingDTO.class);
        StockHandsetDTO stockHandsetDTO = new StockHandsetDTO();
        StockTotalMessage resultStockTotal = mock(StockTotalMessage.class);
        StockHandsetDTO stockHandsetOld = mock(StockHandsetDTO.class);

        Mockito.when(repository.findOne(anyLong())).thenReturn(stockTransRescue);
        when(mapper.toDtoBean(stockTransRescue)).thenReturn(stockTransRescueDTO);
        setFinalStatic(StockTransRescueServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(stockTransRescueDTO.getStatus()).thenReturn(1L);
        when(spyService.findOne(anyLong())).thenReturn(stockTransRescueDTO);
        when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        when(staffService.findOne(stockTransRescueAction.getFromOwnerId())).thenReturn(staffDTO);
        when(stockTransSerialRescueDTO.getProdOfferIdReturn()).thenReturn(1L);
        when(stockTransSerialRescueDTO.getProdOfferId()).thenReturn(1L);
        when(stockTransSerialRescueDTO.getFromSerial()).thenReturn("2");
        when(stockTransSerialRescueDTO.getSerialReturn()).thenReturn("1");
        when(productOfferingService.findOne(anyLong())).thenReturn(productOfferingDTO);
        when(stockHandsetService.updateForStockRescue(Const.STATE_STATUS.RETRIEVE, stockTransRescueDTO.getFromOwnerId(), stockTransSerialRescueDTO.getFromSerial(), stockTransSerialRescueDTO.getProdOfferId())).thenReturn(1);
        when(stockHandsetRepo.getStockHandset(stockTransSerialRescueDTO.getProdOfferId(), stockTransSerialRescueDTO.getFromSerial(),
                Const.OWNER_TYPE.STAFF_LONG, stockTransRescueDTO.getFromOwnerId(), null)).thenReturn(stockHandsetDTO);
        when(stockHandsetRepo.getStockHandset(stockTransSerialRescueDTO.getProdOfferId(), stockTransSerialRescueDTO.getSerialReturn(), null, null, null)).thenReturn(stockHandsetOld);
        when(stockTotalService.changeStockTotal(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(resultStockTotal);
        when(stockHandsetRepo.updateStockHandsetIM1(any(), any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(1);
        when(resultStockTotal.isSuccess()).thenReturn(true);
        when(spyService.getText("REASON_EXP_CHANGE_DEMAGED_GOODS")).thenReturn(null);
        when(optionSetValueDTO.getValue()).thenReturn("1");
        when(stockHandsetRepo.getStockHandset(anyLong(), anyString(), any(),any(), anyLong())).thenReturn(stockHandsetOld);
        when(stockHandsetRepo.updateStockHandsetIM1(Const.STATE_STATUS.RETRIEVE, stockTransRescueDTO.getFromOwnerId(), Const.OWNER_TYPE.STAFF_LONG, null, Const.STATE_STATUS.NEW,
                stockTransRescueDTO.getToOwnerId(), Const.OWNER_TYPE.SHOP_LONG, DataUtil.safeToLong(Const.STATUS_ACTIVE), stockTransSerialRescueDTO.getSerialReturn(), 1L)).thenReturn(1);
        when(stockTotalService.changeStockTotal(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(resultStockTotal);
        when(resultStockTotal.isSuccess()).thenReturn(false);

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        PowerMockito.doReturn(lstSerial).when(spyService, "actionStockRescue", any(), any(), any(), any());
        spyService.receiveStockRescue(stockTransRescueAction, requiredRoleMap);
    }
    @Test(expected = Exception.class)
    public void testReceiveStockRescue_16() throws  Exception {
        StockTransRescueServiceImpl spyService = PowerMockito.spy(stockTransRescueService);
        StockTransRescueDTO stockTransRescueAction = new StockTransRescueDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        requiredRoleMap.add(Const.PERMISION.ROLE_EXPORT_RETURN_WARRANTY);
        StockTransSerialRescueDTO stockTransSerialRescueDTO = mock(StockTransSerialRescueDTO.class);
        List<StockTransSerialRescueDTO> lstSerial = Lists.newArrayList(stockTransSerialRescueDTO);
        OptionSetValueDTO optionSetValueDTO = mock(OptionSetValueDTO.class);
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        StockTransRescue stockTransRescue = mock(StockTransRescue.class);
        StockTransRescueDTO stockTransRescueDTO = mock(StockTransRescueDTO.class);
        StaffDTO staffDTO = new StaffDTO();
        ProductOfferingDTO productOfferingDTO = mock(ProductOfferingDTO.class);
        StockHandsetDTO stockHandsetDTO = new StockHandsetDTO();
        StockTotalMessage resultStockTotal = mock(StockTotalMessage.class);
        StockTotalMessage resultStockTotal1 = mock(StockTotalMessage.class);
        StockHandsetDTO stockHandsetOld = mock(StockHandsetDTO.class);
        Date currentDate = new Date();

        Mockito.when(repository.findOne(anyLong())).thenReturn(stockTransRescue);
        when(mapper.toDtoBean(stockTransRescue)).thenReturn(stockTransRescueDTO);
        setFinalStatic(StockTransRescueServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(stockTransRescueDTO.getStatus()).thenReturn(1L);
        when(spyService.findOne(anyLong())).thenReturn(stockTransRescueDTO);
        when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        when(staffService.findOne(stockTransRescueAction.getFromOwnerId())).thenReturn(staffDTO);
        when(stockTransSerialRescueDTO.getProdOfferIdReturn()).thenReturn(1L);
        when(stockTransSerialRescueDTO.getProdOfferId()).thenReturn(1L);
        when(stockTransSerialRescueDTO.getFromSerial()).thenReturn("2");
        when(stockTransSerialRescueDTO.getSerialReturn()).thenReturn("1");
        when(productOfferingService.findOne(anyLong())).thenReturn(productOfferingDTO);
        when(stockHandsetService.updateForStockRescue(Const.STATE_STATUS.RETRIEVE, stockTransRescueDTO.getFromOwnerId(), stockTransSerialRescueDTO.getFromSerial(), stockTransSerialRescueDTO.getProdOfferId())).thenReturn(1);
        when(stockHandsetRepo.getStockHandset(stockTransSerialRescueDTO.getProdOfferId(), stockTransSerialRescueDTO.getFromSerial(),
                Const.OWNER_TYPE.STAFF_LONG, stockTransRescueDTO.getFromOwnerId(), null)).thenReturn(stockHandsetDTO);
        when(stockHandsetRepo.getStockHandset(stockTransSerialRescueDTO.getProdOfferId(), stockTransSerialRescueDTO.getSerialReturn(), null, null, null)).thenReturn(stockHandsetOld);
        when(stockHandsetRepo.updateStockHandsetIM1(any(), any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(1);
        when(spyService.getText("REASON_EXP_CHANGE_DEMAGED_GOODS")).thenReturn(null);
        when(spyService.getText("REASON_IMP_CHANGE_DEMAGED_GOODS")).thenReturn(null);
        when(optionSetValueDTO.getValue()).thenReturn("1");
        when(stockHandsetRepo.getStockHandset(anyLong(), anyString(), any(),any(), anyLong())).thenReturn(stockHandsetOld);
        when(stockHandsetRepo.updateStockHandsetIM1(Const.STATE_STATUS.RETRIEVE, stockTransRescueDTO.getFromOwnerId(), Const.OWNER_TYPE.STAFF_LONG, null, Const.STATE_STATUS.NEW,
                stockTransRescueDTO.getToOwnerId(), Const.OWNER_TYPE.SHOP_LONG, DataUtil.safeToLong(Const.STATUS_ACTIVE), stockTransSerialRescueDTO.getSerialReturn(), 1L)).thenReturn(1);
        when(stockTotalService.changeStockTotal(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(resultStockTotal);
        when(stockTotalService.changeStockTotal(stockTransRescueDTO.getFromOwnerId(), Const.OWNER_TYPE.STAFF_LONG, stockTransSerialRescueDTO.getProdOfferId(),
                Const.STATE_STATUS.DAMAGE, -1L, -1L, 0L, staffDTO.getStaffId(), stockTransRescueDTO.getReasonId(), stockTransRescueDTO.getStockTransId(), currentDate, "", "1", Const.SourceType.STOCK_TRANS)).thenReturn(resultStockTotal1);
        when(resultStockTotal1.isSuccess()).thenReturn(true);
        when(resultStockTotal.isSuccess()).thenReturn(true);


        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        PowerMockito.doReturn(lstSerial).when(spyService, "actionStockRescue", any(), any(), any(), any());
        spyService.receiveStockRescue(stockTransRescueAction, requiredRoleMap);
    }

    @Test(expected = Exception.class)
    public void testReceiveStockRescue_17() throws  Exception {
        StockTransRescueServiceImpl spyService = PowerMockito.spy(stockTransRescueService);
        StockTransRescueDTO stockTransRescueAction = new StockTransRescueDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        requiredRoleMap.add(Const.PERMISION.ROLE_EXPORT_RETURN_WARRANTY);
        StockTransSerialRescueDTO stockTransSerialRescueDTO = mock(StockTransSerialRescueDTO.class);
        List<StockTransSerialRescueDTO> lstSerial = Lists.newArrayList(stockTransSerialRescueDTO);
        OptionSetValueDTO optionSetValueDTO = mock(OptionSetValueDTO.class);
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        StockTransRescue stockTransRescue = mock(StockTransRescue.class);
        StockTransRescueDTO stockTransRescueDTO = mock(StockTransRescueDTO.class);
        StaffDTO staffDTO = new StaffDTO();
        ProductOfferingDTO productOfferingDTO = mock(ProductOfferingDTO.class);
        StockHandsetDTO stockHandsetDTO = new StockHandsetDTO();
        StockTotalMessage resultStockTotal = mock(StockTotalMessage.class);
        StockTotalMessage resultStockTotal1 = mock(StockTotalMessage.class);
        StockHandsetDTO stockHandsetOld = mock(StockHandsetDTO.class);
        Date currentDate = new Date();
        ReasonDTO reasonDTO = new ReasonDTO();
        List<ReasonDTO> lstReasonExp = Lists.newArrayList(reasonDTO);
        StockTransActionDTO stockTransActionExport = new StockTransActionDTO();
        StockTransDTO stockTransExport = new StockTransDTO();
        StockTransDTO stockTransExpSave = mock(StockTransDTO.class);
        StockTransActionDTO stockTransActionExportSave = mock(StockTransActionDTO.class);

        Mockito.when(repository.findOne(anyLong())).thenReturn(stockTransRescue);
        when(mapper.toDtoBean(stockTransRescue)).thenReturn(stockTransRescueDTO);
        setFinalStatic(StockTransRescueServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(stockTransRescueDTO.getStatus()).thenReturn(1L);
        when(spyService.findOne(anyLong())).thenReturn(stockTransRescueDTO);
        when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        when(staffService.findOne(stockTransRescueAction.getFromOwnerId())).thenReturn(staffDTO);
        when(stockTransSerialRescueDTO.getProdOfferIdReturn()).thenReturn(1L);
        when(stockTransSerialRescueDTO.getProdOfferId()).thenReturn(1L);
        when(stockTransSerialRescueDTO.getFromSerial()).thenReturn("2");
        when(stockTransSerialRescueDTO.getSerialReturn()).thenReturn("1");
        when(productOfferingService.findOne(anyLong())).thenReturn(productOfferingDTO);
        when(stockHandsetService.updateForStockRescue(Const.STATE_STATUS.RETRIEVE, stockTransRescueDTO.getFromOwnerId(), stockTransSerialRescueDTO.getFromSerial(), stockTransSerialRescueDTO.getProdOfferId())).thenReturn(1);
        when(stockHandsetRepo.getStockHandset(stockTransSerialRescueDTO.getProdOfferId(), stockTransSerialRescueDTO.getFromSerial(),
                Const.OWNER_TYPE.STAFF_LONG, stockTransRescueDTO.getFromOwnerId(), null)).thenReturn(stockHandsetDTO);
        when(stockHandsetRepo.getStockHandset(stockTransSerialRescueDTO.getProdOfferId(), stockTransSerialRescueDTO.getSerialReturn(), null, null, null)).thenReturn(stockHandsetOld);
        when(stockHandsetRepo.updateStockHandsetIM1(any(), any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(1);
        when(spyService.getText("REASON_EXP_CHANGE_DEMAGED_GOODS")).thenReturn(null);
        when(optionSetValueDTO.getValue()).thenReturn("1");
        when(stockHandsetRepo.getStockHandset(anyLong(), anyString(), any(),any(), anyLong())).thenReturn(stockHandsetOld);
        when(stockHandsetRepo.updateStockHandsetIM1(Const.STATE_STATUS.RETRIEVE, stockTransRescueDTO.getFromOwnerId(), Const.OWNER_TYPE.STAFF_LONG, null, Const.STATE_STATUS.NEW,
                stockTransRescueDTO.getToOwnerId(), Const.OWNER_TYPE.SHOP_LONG, DataUtil.safeToLong(Const.STATUS_ACTIVE), stockTransSerialRescueDTO.getSerialReturn(), 1L)).thenReturn(1);
        when(stockTotalService.changeStockTotal(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(resultStockTotal);

        when(stockTotalService.changeStockTotal(stockTransRescueDTO.getFromOwnerId(), Const.OWNER_TYPE.STAFF_LONG, stockTransSerialRescueDTO.getProdOfferId(),
                Const.STATE_STATUS.DAMAGE, -1L, -1L, 0L, staffDTO.getStaffId(), stockTransRescueDTO.getReasonId(), stockTransRescueDTO.getStockTransId(),
                currentDate, "", "1", Const.SourceType.STOCK_TRANS)).thenReturn(resultStockTotal1);
        when(resultStockTotal1.isSuccess()).thenReturn(true);
        when(resultStockTotal.isSuccess()).thenReturn(true);
        when(reasonService.getLsReasonByCode(anyString())).thenReturn(lstReasonExp);
        when(stockTransService.save(stockTransExport)).thenReturn(stockTransExpSave);
        when(stockTransExpSave.getStockTransId()).thenReturn(1L);
        when(stockTransActionService.save(stockTransActionExport)).thenReturn(stockTransActionExportSave);
        when(stockTransActionExportSave.getStockTransActionId()).thenReturn(1L);

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        PowerMockito.doReturn(lstSerial).when(spyService, "actionStockRescue", any(), any(), any(), any());
        PowerMockito.doReturn(stockTransExport).when(spyService, "getStockTransExport", any(), any(), any());
        PowerMockito.doReturn(stockTransActionExport).when(spyService, "getStockTransActionExp", any(), any(), any());
        spyService.receiveStockRescue(stockTransRescueAction, requiredRoleMap);
    }

    @Test(expected = Exception.class)
    public void testReceiveStockRescue_18() throws  Exception {
        StockTransRescueServiceImpl spyService = PowerMockito.spy(stockTransRescueService);
        StockTransRescueDTO stockTransRescueAction = new StockTransRescueDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        requiredRoleMap.add(Const.PERMISION.ROLE_EXPORT_RETURN_WARRANTY);
        StockTransSerialRescueDTO stockTransSerialRescueDTO = mock(StockTransSerialRescueDTO.class);
        List<StockTransSerialRescueDTO> lstSerial = Lists.newArrayList(stockTransSerialRescueDTO);
        OptionSetValueDTO optionSetValueDTO = mock(OptionSetValueDTO.class);
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        StockTransRescue stockTransRescue = mock(StockTransRescue.class);
        StockTransRescueDTO stockTransRescueDTO = mock(StockTransRescueDTO.class);
        StaffDTO staffDTO = new StaffDTO();
        ProductOfferingDTO productOfferingDTO = mock(ProductOfferingDTO.class);
        StockHandsetDTO stockHandsetDTO = new StockHandsetDTO();
        StockTotalMessage resultStockTotal = mock(StockTotalMessage.class);
        StockTotalMessage resultStockTotal1 = mock(StockTotalMessage.class);
        StockHandsetDTO stockHandsetOld = mock(StockHandsetDTO.class);
        Date currentDate = new Date();
        ReasonDTO reasonDTO = new ReasonDTO();
        List<ReasonDTO> lstReasonExp = Lists.newArrayList(reasonDTO);
        StockTransActionDTO stockTransActionExport = new StockTransActionDTO();
        StockTransDTO stockTransExport = new StockTransDTO();
        StockTransDTO stockTransExpSave = mock(StockTransDTO.class);
        StockTransActionDTO stockTransActionExportSave = mock(StockTransActionDTO.class);

        Mockito.when(repository.findOne(anyLong())).thenReturn(stockTransRescue);
        when(mapper.toDtoBean(stockTransRescue)).thenReturn(stockTransRescueDTO);
        setFinalStatic(StockTransRescueServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(stockTransRescueDTO.getStatus()).thenReturn(1L);
        when(spyService.findOne(anyLong())).thenReturn(stockTransRescueDTO);
        when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        when(staffService.findOne(stockTransRescueAction.getFromOwnerId())).thenReturn(staffDTO);
        when(stockTransSerialRescueDTO.getProdOfferIdReturn()).thenReturn(1L);
        when(stockTransSerialRescueDTO.getProdOfferId()).thenReturn(1L);
        when(stockTransSerialRescueDTO.getFromSerial()).thenReturn("2");
        when(stockTransSerialRescueDTO.getSerialReturn()).thenReturn("1");
        when(productOfferingService.findOne(anyLong())).thenReturn(productOfferingDTO);
        when(stockHandsetService.updateForStockRescue(Const.STATE_STATUS.RETRIEVE, stockTransRescueDTO.getFromOwnerId(), stockTransSerialRescueDTO.getFromSerial(), stockTransSerialRescueDTO.getProdOfferId())).thenReturn(1);
        when(stockHandsetRepo.getStockHandset(stockTransSerialRescueDTO.getProdOfferId(), stockTransSerialRescueDTO.getFromSerial(),
                Const.OWNER_TYPE.STAFF_LONG, stockTransRescueDTO.getFromOwnerId(), null)).thenReturn(stockHandsetDTO);
        when(stockHandsetRepo.getStockHandset(stockTransSerialRescueDTO.getProdOfferId(), stockTransSerialRescueDTO.getSerialReturn(), null, null, null)).thenReturn(stockHandsetOld);
        when(stockHandsetRepo.updateStockHandsetIM1(any(), any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(1);
        when(spyService.getText("REASON_IMP_CHANGE_DEMAGED_GOODS")).thenReturn(null);
        when(optionSetValueDTO.getValue()).thenReturn("1");
        when(stockHandsetRepo.getStockHandset(anyLong(), anyString(), any(),any(), anyLong())).thenReturn(stockHandsetOld);
        when(stockHandsetRepo.updateStockHandsetIM1(Const.STATE_STATUS.RETRIEVE, stockTransRescueDTO.getFromOwnerId(), Const.OWNER_TYPE.STAFF_LONG, null, Const.STATE_STATUS.NEW,
                stockTransRescueDTO.getToOwnerId(), Const.OWNER_TYPE.SHOP_LONG, DataUtil.safeToLong(Const.STATUS_ACTIVE), stockTransSerialRescueDTO.getSerialReturn(), 1L)).thenReturn(1);
        when(stockTotalService.changeStockTotal(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(resultStockTotal);
        when(stockTotalService.changeStockTotal(stockTransRescueDTO.getFromOwnerId(), Const.OWNER_TYPE.STAFF_LONG, stockTransSerialRescueDTO.getProdOfferId(),
                Const.STATE_STATUS.DAMAGE, -1L, -1L, 0L, staffDTO.getStaffId(), stockTransRescueDTO.getReasonId(), stockTransRescueDTO.getStockTransId(), currentDate, "", "1", Const.SourceType.STOCK_TRANS)).thenReturn(resultStockTotal1);
        when(resultStockTotal1.isSuccess()).thenReturn(true);
        when(resultStockTotal.isSuccess()).thenReturn(true);
        when(reasonService.getLsReasonByCode(anyString())).thenReturn(lstReasonExp);
        when(stockTransService.save(stockTransExport)).thenReturn(stockTransExpSave);
        when(stockTransExpSave.getStockTransId()).thenReturn(1L);
        when(stockTransActionService.save(stockTransActionExport)).thenReturn(stockTransActionExportSave);
        when(stockTransActionExportSave.getStockTransActionId()).thenReturn(1L);

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        PowerMockito.doReturn(lstSerial).when(spyService, "actionStockRescue", any(), any(), any(), any());
        PowerMockito.doReturn(stockTransExport).when(spyService, "getStockTransExport", any(), any(), any());
        PowerMockito.doReturn(stockTransActionExport).when(spyService, "getStockTransActionExp", any(), any(), any());
        spyService.receiveStockRescue(stockTransRescueAction, requiredRoleMap);
    }

    @Test
    public void testReceiveStockRescue_19() throws  Exception {
        StockTransRescueServiceImpl spyService = PowerMockito.spy(stockTransRescueService);
        StockTransRescueDTO stockTransRescueAction = new StockTransRescueDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        requiredRoleMap.add(Const.PERMISION.ROLE_EXPORT_RETURN_WARRANTY);
        StockTransSerialRescueDTO stockTransSerialRescueDTO = mock(StockTransSerialRescueDTO.class);
        List<StockTransSerialRescueDTO> lstSerial = Lists.newArrayList(stockTransSerialRescueDTO);
        OptionSetValueDTO optionSetValueDTO = mock(OptionSetValueDTO.class);
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        StockTransRescue stockTransRescue = mock(StockTransRescue.class);
        StockTransRescueDTO stockTransRescueDTO = mock(StockTransRescueDTO.class);
        StaffDTO staffDTO = new StaffDTO();
        ProductOfferingDTO productOfferingDTO = mock(ProductOfferingDTO.class);
        StockHandsetDTO stockHandsetDTO = new StockHandsetDTO();
        StockTotalMessage resultStockTotal = mock(StockTotalMessage.class);
        StockTotalMessage resultStockTotal1 = mock(StockTotalMessage.class);
        StockHandsetDTO stockHandsetOld = mock(StockHandsetDTO.class);
        Date currentDate = new Date();
        ReasonDTO reasonDTO = new ReasonDTO();
        List<ReasonDTO> lstReasonExp = Lists.newArrayList(reasonDTO);
        StockTransActionDTO stockTransActionExport = new StockTransActionDTO();
        StockTransDTO stockTransExport = new StockTransDTO();
        StockTransDTO stockTransExpSave = mock(StockTransDTO.class);
        StockTransActionDTO stockTransActionExportSave = mock(StockTransActionDTO.class);

        Mockito.when(repository.findOne(anyLong())).thenReturn(stockTransRescue);
        when(mapper.toDtoBean(stockTransRescue)).thenReturn(stockTransRescueDTO);
        setFinalStatic(StockTransRescueServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(stockTransRescueDTO.getStatus()).thenReturn(1L);
        when(spyService.findOne(anyLong())).thenReturn(stockTransRescueDTO);
        when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        when(staffService.findOne(stockTransRescueAction.getFromOwnerId())).thenReturn(staffDTO);
        when(stockTransSerialRescueDTO.getProdOfferIdReturn()).thenReturn(1L);
        when(stockTransSerialRescueDTO.getProdOfferId()).thenReturn(1L);
        when(stockTransSerialRescueDTO.getFromSerial()).thenReturn("2");
        when(stockTransSerialRescueDTO.getSerialReturn()).thenReturn("1");
        when(productOfferingService.findOne(anyLong())).thenReturn(productOfferingDTO);
        when(stockHandsetService.updateForStockRescue(Const.STATE_STATUS.RETRIEVE, stockTransRescueDTO.getFromOwnerId(), stockTransSerialRescueDTO.getFromSerial(), stockTransSerialRescueDTO.getProdOfferId())).thenReturn(1);
        when(stockHandsetRepo.getStockHandset(stockTransSerialRescueDTO.getProdOfferId(), stockTransSerialRescueDTO.getFromSerial(),
                Const.OWNER_TYPE.STAFF_LONG, stockTransRescueDTO.getFromOwnerId(), null)).thenReturn(stockHandsetDTO);
        when(stockHandsetRepo.getStockHandset(stockTransSerialRescueDTO.getProdOfferId(), stockTransSerialRescueDTO.getSerialReturn(), null, null, null)).thenReturn(stockHandsetOld);
        when(stockHandsetRepo.updateStockHandsetIM1(any(), any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(1);
        when(spyService.getText("REASON_IMP_CHANGE_DEMAGED_GOODS")).thenReturn(null);
        when(optionSetValueDTO.getValue()).thenReturn("1");
        when(stockHandsetRepo.getStockHandset(anyLong(), anyString(), any(),any(), anyLong())).thenReturn(stockHandsetOld);
        when(stockHandsetRepo.updateStockHandsetIM1(Const.STATE_STATUS.RETRIEVE, stockTransRescueDTO.getFromOwnerId(), Const.OWNER_TYPE.STAFF_LONG, null, Const.STATE_STATUS.NEW,
                stockTransRescueDTO.getToOwnerId(), Const.OWNER_TYPE.SHOP_LONG, DataUtil.safeToLong(Const.STATUS_ACTIVE), stockTransSerialRescueDTO.getSerialReturn(), 1L)).thenReturn(1);
        when(stockTotalService.changeStockTotal(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(resultStockTotal);
        when(stockTotalService.changeStockTotal(stockTransRescueDTO.getFromOwnerId(), Const.OWNER_TYPE.STAFF_LONG, stockTransSerialRescueDTO.getProdOfferId(),
                Const.STATE_STATUS.DAMAGE, -1L, -1L, 0L, staffDTO.getStaffId(), stockTransRescueDTO.getReasonId(), stockTransRescueDTO.getStockTransId(), currentDate, "", "1", Const.SourceType.STOCK_TRANS)).thenReturn(resultStockTotal1);
        when(resultStockTotal1.isSuccess()).thenReturn(true);
        when(resultStockTotal.isSuccess()).thenReturn(true);
        when(reasonService.getLsReasonByCode(anyString())).thenReturn(lstReasonExp);
        when(stockTransService.save(stockTransExport)).thenReturn(stockTransExpSave);
        when(stockTransExpSave.getStockTransId()).thenReturn(1L);
        when(stockTransActionService.save(stockTransActionExport)).thenReturn(stockTransActionExportSave);
        when(stockTransActionExportSave.getStockTransActionId()).thenReturn(1L);

        StockTransActionDTO stockTransActionImpSave = new StockTransActionDTO();
        StockTransDTO stockTransImpSave = new StockTransDTO();
        StockTransDetailDTO stockTransDetailSave = new StockTransDetailDTO();
        StockTransDetailRescueDTO stockTransDetailRescueDTO = mock(StockTransDetailRescueDTO.class);
        List<StockTransDetailRescueDTO> lstDetailRescue =Lists.newArrayList(stockTransDetailRescueDTO);
        StockTransSerialRescueDTO stockTransSerialRescueDTO1 = mock(StockTransSerialRescueDTO.class);
        List<StockTransSerialRescueDTO> lstSerialRescue = Lists.newArrayList(stockTransSerialRescueDTO1) ;
        when(stockTransSerialRescueService.viewDetailSerail(any(), any(), any())).thenReturn(lstSerialRescue);
        when(stockTransDetailRescueService.viewDetail(anyLong())).thenReturn(lstDetailRescue);
        when(stockTransDetailService.save(any())).thenReturn(stockTransDetailSave);
        when(stockTransService.save(any())).thenReturn(stockTransImpSave);
        when(stockTransActionService.save(any())).thenReturn(stockTransActionImpSave);

        List<StockTransDetailRescueDTO> lstReturnSerial = Lists.newArrayList(stockTransDetailRescueDTO);
        when(stockTransDetailRescueService.getCountLstDetail(any())).thenReturn(lstReturnSerial);
        when(stockTransDetailService.save(any())).thenReturn(stockTransDetailSave);

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        PowerMockito.doReturn(lstSerial).when(spyService, "actionStockRescue", any(), any(), any(), any());
        PowerMockito.doReturn(stockTransExport).when(spyService, "getStockTransExport", any(), any(), any());
        PowerMockito.doReturn(stockTransActionExport).when(spyService, "getStockTransActionExp", any(), any(), any());
        spyService.receiveStockRescue(stockTransRescueAction, requiredRoleMap);
    }

    @Test(expected = Exception.class)
    public void testReceiveStockRescue_20() throws  Exception {
        StockTransRescueServiceImpl spyService = PowerMockito.spy(stockTransRescueService);
        StockTransRescueDTO stockTransRescueAction = new StockTransRescueDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        requiredRoleMap.add(Const.PERMISION.ROLE_EXPORT_RETURN_WARRANTY);
        StockTransSerialRescueDTO stockTransSerialRescueDTO = mock(StockTransSerialRescueDTO.class);
        List<StockTransSerialRescueDTO> lstSerial = Lists.newArrayList(stockTransSerialRescueDTO);
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue("1");
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        StockTransRescue stockTransRescue = mock(StockTransRescue.class);
        StockTransRescueDTO stockTransRescueDTO = mock(StockTransRescueDTO.class);
        StaffDTO staffDTO = new StaffDTO();
        ProductOfferingDTO productOfferingDTO = mock(ProductOfferingDTO.class);
        StockHandsetDTO stockHandsetDTO = new StockHandsetDTO();
        StockTotalMessage resultStockTotal = mock(StockTotalMessage.class);

        StockHandsetDTO stockHandsetSave = new StockHandsetDTO();
        doThrow(new Exception()).when( stockHandsetRepo).insertStockHandset(any());
        when(stockHandsetService.create(any())).thenReturn(stockHandsetSave);
        Mockito.when(repository.findOne(anyLong())).thenReturn(stockTransRescue);
        when(mapper.toDtoBean(stockTransRescue)).thenReturn(stockTransRescueDTO);
        setFinalStatic(StockTransRescueServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(stockTransRescueDTO.getStatus()).thenReturn(1L);
        when(spyService.findOne(anyLong())).thenReturn(stockTransRescueDTO);
        when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        when(staffService.findOne(stockTransRescueAction.getFromOwnerId())).thenReturn(staffDTO);
        when(stockTransSerialRescueDTO.getProdOfferIdReturn()).thenReturn(1L);
        when(stockTransSerialRescueDTO.getProdOfferId()).thenReturn(1L);
        when(stockTransSerialRescueDTO.getFromSerial()).thenReturn("2");
        when(stockTransSerialRescueDTO.getSerialReturn()).thenReturn("1");
        when(productOfferingService.findOne(anyLong())).thenReturn(productOfferingDTO);
        when(stockHandsetService.updateForStockRescue(Const.STATE_STATUS.RETRIEVE, stockTransRescueDTO.getFromOwnerId(), stockTransSerialRescueDTO.getFromSerial(), stockTransSerialRescueDTO.getProdOfferId())).thenReturn(1);
        when(stockHandsetRepo.getStockHandset(stockTransSerialRescueDTO.getProdOfferId(), stockTransSerialRescueDTO.getFromSerial(),
                Const.OWNER_TYPE.STAFF_LONG, stockTransRescueDTO.getFromOwnerId(), null)).thenReturn(stockHandsetDTO);
        when(stockTotalService.changeStockTotal(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(resultStockTotal);
        when(resultStockTotal.isSuccess()).thenReturn(true);
        when(spyService.getText("REASON_EXP_CHANGE_DEMAGED_GOODS")).thenReturn(null);
        when(stockHandsetRepo.updateStockHandsetIM1(null, null, null, DataUtil.safeToLong(Const.STATUS_INACTIVE), null, stockTransRescueDTO.getFromOwnerId()
                , Const.OWNER_TYPE.STAFF_LONG, DataUtil.safeToLong(Const.STATUS_ACTIVE), stockTransSerialRescueDTO.getFromSerial(), stockTransSerialRescueDTO.getProdOfferId())).thenReturn(1);

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        PowerMockito.doReturn(lstSerial).when(spyService, "actionStockRescue", any(), any(), any(), any());
        spyService.receiveStockRescue(stockTransRescueAction, requiredRoleMap);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransRescueServiceImpl.acceptStockRescue
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Test
    public void testAcceptStockRescue_1() throws Exception {
        StockTransRescueServiceImpl spyService = PowerMockito.spy(stockTransRescueService);
        StockTransSerialRescueDTO stockTransSerialRescueDTO = new StockTransSerialRescueDTO();
        List<StockTransSerialRescueDTO> lstSerial = Lists.newArrayList(stockTransSerialRescueDTO);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        PowerMockito.doReturn(lstSerial).when(spyService, "actionStockRescue", any(), any(), any(), any());
        spyService.acceptStockRescue(new StockTransRescueDTO(), new RequiredRoleMap());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransRescueServiceImpl.cancelStockRescue
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Test
    public void testCancelStockRescue_1() throws Exception {
        StockTransRescueServiceImpl spyService = PowerMockito.spy(stockTransRescueService);
        StockTransSerialRescueDTO stockTransSerialRescueDTO = new StockTransSerialRescueDTO();
        List<StockTransSerialRescueDTO> lstSerial = Lists.newArrayList(stockTransSerialRescueDTO);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        PowerMockito.doReturn(lstSerial).when(spyService, "actionStockRescue", any(), any(), any(), any());
        spyService.cancelStockRescue(new StockTransRescueDTO(), new RequiredRoleMap());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransRescueServiceImpl.returnStockRescue
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Test(expected = Exception.class)
    public void testReturnStockRescue_1() throws Exception {
        StockTransRescueDTO stockTransRescueAction = new StockTransRescueDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        StockTransSerialRescueDTO stockTransSerialRescueDTO = mock(StockTransSerialRescueDTO.class);
        List<StockTransSerialRescueDTO> lstSerial = Lists.newArrayList(stockTransSerialRescueDTO);
        List<StockTransSerialRescueDTO> lstSerialRescue= Lists.newArrayList(stockTransSerialRescueDTO);

        StockTransRescueServiceImpl spyService = PowerMockito.spy(stockTransRescueService);
        PowerMockito.doReturn(lstSerialRescue).when(spyService, "actionStockRescue", any(), any(), any(), any());
        when(stockTransSerialRescueDTO.getSerialReturn()).thenReturn("!=");
        spyService.returnStockRescue(stockTransRescueAction, requiredRoleMap, lstSerial);
    }

    @Test(expected = Exception.class)
    public void testReturnStockRescue_2() throws Exception {
        StockTransRescueDTO stockTransRescueAction = new StockTransRescueDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        StockTransSerialRescueDTO stockTransSerialRescueDTO = mock(StockTransSerialRescueDTO.class);
        List<StockTransSerialRescueDTO> lstSerial = Lists.newArrayList(stockTransSerialRescueDTO);
        List<StockTransSerialRescueDTO> lstSerialRescue= Lists.newArrayList(stockTransSerialRescueDTO);
        ProductOfferingTotalDTO productOfferingTotalDTO = mock(ProductOfferingTotalDTO.class);
        ProductOfferingDTO productOfferingDTO = mock(ProductOfferingDTO.class);

        StockTransRescueServiceImpl spyService = PowerMockito.spy(stockTransRescueService);
        PowerMockito.doReturn(lstSerialRescue).when(spyService, "actionStockRescue", any(), any(), any(), any());
        when(stockTransSerialRescueDTO.getSerialReturn()).thenReturn("serial");
        when(stockTransSerialRescueDTO.getProductOfferingTotalDTO()).thenReturn(productOfferingTotalDTO);
        when(productOfferingTotalDTO.getProductOfferingId()).thenReturn(1L);
        when(productOfferingService.findOne(anyLong())).thenReturn(productOfferingDTO);
        spyService.returnStockRescue(stockTransRescueAction, requiredRoleMap, lstSerial);
    }

    // Không thể nhảy vào line 570

    @Test
    public void testReturnStockRescue_3() throws Exception {
        StockTransRescueDTO stockTransRescueAction = new StockTransRescueDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        StockTransSerialRescueDTO stockTransSerialRescueDTO = mock(StockTransSerialRescueDTO.class);
        List<StockTransSerialRescueDTO> lstSerial = Lists.newArrayList(stockTransSerialRescueDTO);
        List<StockTransSerialRescueDTO> lstSerialRescue= Lists.newArrayList(stockTransSerialRescueDTO);
        ProductOfferingTotalDTO productOfferingTotalDTO = mock(ProductOfferingTotalDTO.class);
        ProductOfferingDTO productOfferingDTO = mock(ProductOfferingDTO.class);

        StockTransRescueServiceImpl spyService = PowerMockito.spy(stockTransRescueService);
        PowerMockito.doReturn(lstSerialRescue).when(spyService, "actionStockRescue", any(), any(), any(), any());
        when(stockTransSerialRescueDTO.getSerialReturn()).thenReturn("serial");
        when(stockTransSerialRescueDTO.getProductOfferingTotalDTO()).thenReturn(productOfferingTotalDTO);

        when(stockTransSerialRescueDTO.getStockTransSerialId()).thenReturn(1L);
        when(stockTransSerialRescueDTO.getStockTransSerialId()).thenReturn(1L);

        StockTransSerialRescueDTO stockTransSerialRescue = new StockTransSerialRescueDTO();
        when(productOfferingTotalDTO.getProductOfferingId()).thenReturn(0L);
        when(productOfferingService.findOne(anyLong())).thenReturn(productOfferingDTO);
        when(stockTransSerialRescueService.findOne(any())).thenReturn(stockTransSerialRescue);

        spyService.returnStockRescue(stockTransRescueAction, requiredRoleMap, lstSerial);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransRescueServiceImpl.getStockTransExport
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Test
    public void testGetStockTransExport_1() throws Exception {
        StockTransRescueServiceImpl spyService = PowerMockito.spy(stockTransRescueService);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        Whitebox.invokeMethod(spyService, "getStockTransExport", 1L,1L, new Date());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransRescueServiceImpl.getStockTransActionExp
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Test
    public void testGetStockTransActionExp_1() throws Exception {
        StockTransRescueServiceImpl spyService = PowerMockito.spy(stockTransRescueService);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        Whitebox.invokeMethod(spyService, "getStockTransActionExp", new StaffDTO(), new Date(), 1L);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransRescueServiceImpl.getStockTransDetailExp
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Test
    public void testGetStockTransDetailExp_1() throws Exception {
        StockTransRescueServiceImpl spyService = PowerMockito.spy(stockTransRescueService);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        Whitebox.invokeMethod(spyService, "getStockTransDetailExp", 1L, 1L, new Date(), 1L);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransRescueServiceImpl.getStockTransSerialExp
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Test
    public void testGetStockTransSerialExp_1() throws Exception {
        StockTransRescueServiceImpl spyService = PowerMockito.spy(stockTransRescueService);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        Whitebox.invokeMethod(spyService, "getStockTransSerialExp", 1L,1L, 1L, "1", new Date());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransRescueServiceImpl.getStockTransActionImp
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Test
    public void testGetStockTransActionImp_1() throws Exception {
        StockTransRescueServiceImpl spyService = PowerMockito.spy(stockTransRescueService);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        Whitebox.invokeMethod(spyService, "getStockTransActionImp", new StaffDTO(),new Date(), 1L);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransRescueServiceImpl.getStockTransDetailImp
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Test
    public void testGetStockTransDetailImp_1() throws Exception {
        StockTransRescueServiceImpl spyService = PowerMockito.spy(stockTransRescueService);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        Whitebox.invokeMethod(spyService, "getStockTransDetailImp", 1L, 1L, new Date(), 1L);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransRescueServiceImpl.getStockTransSerialImp
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Test
    public void testGetStockTransSerialImp_1() throws Exception {
        StockTransRescueServiceImpl spyService = PowerMockito.spy(stockTransRescueService);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        Whitebox.invokeMethod(spyService, "getStockTransSerialImp", 1L, 1L, 1L,"1", new Date());
    }










    static void setFinalStatic(Field field, Object newValue, final Object targetObject) throws Exception {
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(targetObject, newValue);
    }
}