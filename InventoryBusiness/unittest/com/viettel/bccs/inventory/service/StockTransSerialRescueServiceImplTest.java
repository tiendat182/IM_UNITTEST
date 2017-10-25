package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.dto.StockTransSerialRescueDTO;
import com.viettel.bccs.inventory.model.StockTransSerialRescue;
import com.viettel.bccs.inventory.repo.StockTransSerialRescueRepo;
import com.viettel.fw.common.util.DbUtil;
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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author DatLT
 * @date 12/10/2017
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({DbUtil.class, StockTransSerialRescueServiceImpl.class, BaseServiceImpl.class, StockTransSerialRescueService.class})
public class StockTransSerialRescueServiceImplTest {
    @InjectMocks
    StockTransSerialRescueServiceImpl stockTransSerialRescueService;
    @Mock
    private final BaseMapper<StockTransSerialRescue, StockTransSerialRescueDTO> mapper = new BaseMapper<>(StockTransSerialRescue.class, StockTransSerialRescueDTO.class);
    @Mock
    private StockTransSerialRescueRepo repository;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransSerialRescueServiceImpl.count
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCount_1() throws  Exception {
        List<FilterRequest> filters = new ArrayList<>();
        when(repository.count(any())).thenReturn(1L);
        Assert.assertEquals(1L, stockTransSerialRescueService.count(filters).longValue());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransSerialRescueServiceImpl.findOne
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindOne_1() throws  Exception {
        StockTransSerialRescueServiceImpl spyService = PowerMockito.spy( stockTransSerialRescueService);
        StockTransSerialRescue stockTransSerialRescue = mock(StockTransSerialRescue.class);
        StockTransSerialRescueDTO stockTransSerialRescueDTO = new StockTransSerialRescueDTO();
        Mockito.when(repository.findOne(anyLong())).thenReturn(stockTransSerialRescue);
        setFinalStatic(StockTransSerialRescueServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(mapper.toDtoBean(stockTransSerialRescue)).thenReturn(stockTransSerialRescueDTO);
        spyService.findOne(1L);
        Assert.assertNotNull(stockTransSerialRescueDTO);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransSerialRescueServiceImpl.findAll
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindAll_1() throws  Exception {
        StockTransSerialRescueServiceImpl spyService = PowerMockito.spy( stockTransSerialRescueService);
        StockTransSerialRescue stockTransSerialRescue = mock(StockTransSerialRescue.class);
        StockTransSerialRescueDTO stockTransSerialRescueDTO = new StockTransSerialRescueDTO();
        List<StockTransSerialRescueDTO> stockTransSerialRescueDTOList = Lists.newArrayList(stockTransSerialRescueDTO);
        List<StockTransSerialRescue> stockTransSerialRescueList = Lists.newArrayList(stockTransSerialRescue);
        Mockito.when(repository.findAll()).thenReturn(stockTransSerialRescueList);
        Mockito.when(mapper.toDtoBean(stockTransSerialRescueList)).thenReturn(stockTransSerialRescueDTOList);
        setFinalStatic(StockTransSerialRescueServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findAll();
        Assert.assertNotNull(stockTransSerialRescueDTOList);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransSerialRescueServiceImpl.findByFilter
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindByFilter_1() throws  Exception {
        FilterRequest filterRequest = new FilterRequest();
        List<FilterRequest> filterRequestList = Lists.newArrayList(filterRequest);
        StockTransSerialRescueServiceImpl spyService = PowerMockito.spy( stockTransSerialRescueService);
        StockTransSerialRescue stockTransSerialRescue = mock(StockTransSerialRescue.class);
        StockTransSerialRescueDTO stockTransSerialRescueDTO = new StockTransSerialRescueDTO();
        List<StockTransSerialRescueDTO> stockTransSerialRescueDTOList = Lists.newArrayList(stockTransSerialRescueDTO);
        List<StockTransSerialRescue> stockTransSerialRescueList = Lists.newArrayList(stockTransSerialRescue);
        Mockito.when(mapper.toDtoBean(stockTransSerialRescueList)).thenReturn(stockTransSerialRescueDTOList);
        setFinalStatic(StockTransSerialRescueServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findByFilter(filterRequestList);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransSerialRescueServiceImpl.create
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void testCreate_1() throws  Exception {
        stockTransSerialRescueService.create(new StockTransSerialRescueDTO());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransSerialRescueServiceImpl.update
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void testUpdate_1() throws  Exception {
        stockTransSerialRescueService.update(new StockTransSerialRescueDTO());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransSerialRescueServiceImpl.save
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testSave_1() throws  Exception {
        StockTransSerialRescueDTO stockTransSerialRescueDTO = new StockTransSerialRescueDTO();
        StockTransSerialRescueServiceImpl spyService = PowerMockito.spy( stockTransSerialRescueService);
        setFinalStatic(StockTransSerialRescueServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(mapper.toDtoBean(repository.save(mapper.toPersistenceBean(stockTransSerialRescueDTO)))).thenReturn(stockTransSerialRescueDTO);
        spyService.save(stockTransSerialRescueDTO);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransSerialRescueServiceImpl.viewDetailSerail
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testViewDetailSerail_1() throws  Exception {
        stockTransSerialRescueService.viewDetailSerail(1L,1L,1L);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransSerialRescueServiceImpl.viewDetailSerailByStockTransId
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testViewDetailSerailByStockTransId_1() throws  Exception {
        stockTransSerialRescueService.viewDetailSerailByStockTransId(1L);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransSerialRescueServiceImpl.getListDetailSerial
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testGetListDetailSerial_1() throws  Exception {
        stockTransSerialRescueService.getListDetailSerial(1L);
    }

    static void setFinalStatic(Field field, Object newValue, final Object targetObject) throws Exception {
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(targetObject, newValue);
    }
}