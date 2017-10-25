package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.dto.StockTransLogisticDTO;
import com.viettel.bccs.inventory.model.StockTransLogistic;
import com.viettel.bccs.inventory.repo.StockTransLogisticRepo;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.dto.BaseMessage;
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
import java.util.List;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author DatLT
 * @date 15/10/2017
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({DbUtil.class, StockTransLogisticServiceImpl.class, BaseServiceImpl.class, StockTransLogisticService.class, DateUtil.class})
public class StockTransLogisticServiceImplTest {
    @InjectMocks
    StockTransLogisticServiceImpl stockTransLogisticService;
    @Mock
    private final BaseMapper<StockTransLogistic, StockTransLogisticDTO> mapper = new BaseMapper(StockTransLogistic.class, StockTransLogisticDTO.class);
    @Mock
    private StockTransLogisticRepo repository;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransLogisticServiceImpl.count
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCount_1() throws Exception {
        List<FilterRequest> filters = Lists.newArrayList();
        Mockito.when(repository.count(any())).thenReturn(1L);
        Assert.assertEquals(1L, stockTransLogisticService.count(filters).longValue());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransLogisticServiceImpl.findOne
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindOne_1() throws  Exception {
        StockTransLogisticServiceImpl spyService = PowerMockito.spy( stockTransLogisticService);
        StockTransLogistic stockTransLogistic = mock(StockTransLogistic.class);
        StockTransLogisticDTO stockTransLogisticDTO = new StockTransLogisticDTO();
        Mockito.when(repository.findOne(anyLong())).thenReturn(stockTransLogistic);
        setFinalStatic(StockTransLogisticServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(mapper.toDtoBean(stockTransLogistic)).thenReturn(stockTransLogisticDTO);
        spyService.findOne(1L);
        Assert.assertNotNull(stockTransLogisticDTO);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransLogisticServiceImpl.findAll
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Test
    public void testFindAll_1() throws  Exception {
        StockTransLogisticServiceImpl spyService = PowerMockito.spy( stockTransLogisticService);
        StockTransLogistic stockTransLogistic = mock(StockTransLogistic.class);
        StockTransLogisticDTO stockTransLogisticDTO = new StockTransLogisticDTO();
        List<StockTransLogisticDTO> stockTransLogisticDTOList = Lists.newArrayList(stockTransLogisticDTO);
        List<StockTransLogistic> stockTransLogisticList = Lists.newArrayList(stockTransLogistic);
        Mockito.when(repository.findAll()).thenReturn(stockTransLogisticList);
        Mockito.when(mapper.toDtoBean(stockTransLogisticList)).thenReturn(stockTransLogisticDTOList);
        setFinalStatic(StockTransLogisticServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findAll();
        Assert.assertNotNull(stockTransLogisticDTOList);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransLogisticServiceImpl.findByFilter
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Test
    public void testFindByFilter_1() throws  Exception {
        FilterRequest filterRequest = new FilterRequest();
        List<FilterRequest> filterRequestList = Lists.newArrayList(filterRequest);
        StockTransLogisticServiceImpl spyService = PowerMockito.spy( stockTransLogisticService);
        StockTransLogistic stockTransLogistic = mock(StockTransLogistic.class);
        StockTransLogisticDTO stockTransLogisticDTO = new StockTransLogisticDTO();
        List<StockTransLogisticDTO> stockTransLogisticDTOList = Lists.newArrayList(stockTransLogisticDTO);
        List<StockTransLogistic> stockTransLogisticList = Lists.newArrayList(stockTransLogistic);
        Mockito.when(mapper.toDtoBean(stockTransLogisticList)).thenReturn(stockTransLogisticDTOList);
        setFinalStatic(StockTransLogisticServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findByFilter(filterRequestList);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransLogisticServiceImpl.create
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void testCreate_1() throws Exception {
        stockTransLogisticService.create(new StockTransLogisticDTO());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransLogisticServiceImpl.update
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void testUpdate_1() throws Exception {
        stockTransLogisticService.update(new StockTransLogisticDTO());
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for StockTransLogisticServiceImpl.save method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testSave_1() throws Exception {
        StockTransLogisticServiceImpl spyService = PowerMockito.spy(stockTransLogisticService);
        StockTransLogisticDTO stockTransLogisticDTO = new StockTransLogisticDTO();

        setFinalStatic(StockTransLogisticServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(mapper.toDtoBean(repository.save(mapper.toPersistenceBean(stockTransLogisticDTO)))).thenReturn(stockTransLogisticDTO);
        spyService.save(stockTransLogisticDTO);
    }

/** -------------------------------------------------------------------- **/
    /** --- Test for StockTransLogisticServiceImpl.findByStockTransId method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testFindByStockTransId_1() throws Exception {
        Long stockTransId = 1L;

        StockTransLogisticServiceImpl spyService = PowerMockito.spy(stockTransLogisticService);
        StockTransLogistic stockTransLogistic = mock(StockTransLogistic.class);
        StockTransLogisticDTO stockTransLogisticDTO = new StockTransLogisticDTO();
        List<StockTransLogisticDTO> stockTransLogisticDTOList = Lists.newArrayList(stockTransLogisticDTO);
        List<StockTransLogistic> stockTransLogisticList = Lists.newArrayList(stockTransLogistic);

        setFinalStatic(StockTransLogisticServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        Mockito.when(mapper.toDtoBean(stockTransLogisticList)).thenReturn(stockTransLogisticDTOList);
        when(spyService.findByFilter(anyList())).thenReturn(stockTransLogisticDTOList);
        spyService.findByStockTransId(stockTransId);
    }

    @Test
    public void testFindByStockTransId_2() throws Exception {
        Long stockTransId = 1L;

        StockTransLogisticServiceImpl spyService = PowerMockito.spy(stockTransLogisticService);
        StockTransLogistic stockTransLogistic = mock(StockTransLogistic.class);
        StockTransLogisticDTO stockTransLogisticDTO = new StockTransLogisticDTO();
        List<StockTransLogisticDTO> stockTransLogisticDTOList = Lists.newArrayList(stockTransLogisticDTO);
        List<StockTransLogistic> stockTransLogisticList = Lists.newArrayList(stockTransLogistic);

        setFinalStatic(StockTransLogisticServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        Mockito.when(mapper.toDtoBean(stockTransLogisticList)).thenReturn(stockTransLogisticDTOList);
        when(spyService.findByFilter(anyList())).thenReturn(null);
        spyService.findByStockTransId(stockTransId);
    }

/** -------------------------------------------------------------------- **/
    /** --- Test for StockTransLogisticServiceImpl.getLstOrderLogistics method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testGetLstOrderLogistics_1() throws Exception {
        stockTransLogisticService.getLstOrderLogistics(1L,1L,1L,1L);
    }

/** -------------------------------------------------------------------- **/
    /** --- Test for StockTransLogisticServiceImpl.updateStockTransLogistics method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testUpdateStockTransLogistics_1() throws Exception {
        BaseMessage baseMessage = new BaseMessage();
        baseMessage.setErrorCode("404");
        baseMessage.setDescription("Not found");

        StockTransLogisticServiceImpl spyService = PowerMockito.spy( stockTransLogisticService);
        StockTransLogistic stockTransLogistic = mock(StockTransLogistic.class);
        StockTransLogisticDTO stockTransLogisticDTO = mock(StockTransLogisticDTO.class);

        Mockito.when(repository.findOne(anyLong())).thenReturn(stockTransLogistic);
        setFinalStatic(StockTransLogisticServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(mapper.toDtoBean(stockTransLogistic)).thenReturn(stockTransLogisticDTO);
        when(mapper.toPersistenceBean(stockTransLogisticDTO)).thenReturn(stockTransLogistic);
        when(spyService.findOne(anyLong())).thenReturn(stockTransLogisticDTO);
        spyService.updateStockTransLogistics(1L,1L,baseMessage);
    }

    @Test
    public void testUpdateStockTransLogistics_2() throws Exception {
        BaseMessage baseMessage = new BaseMessage();
        baseMessage.setErrorCode("404");
        baseMessage.setDescription("Not found");

        StockTransLogisticServiceImpl spyService = PowerMockito.spy( stockTransLogisticService);
        StockTransLogistic stockTransLogistic = mock(StockTransLogistic.class);
        StockTransLogisticDTO stockTransLogisticDTO = mock(StockTransLogisticDTO.class);

        Mockito.when(repository.findOne(anyLong())).thenReturn(stockTransLogistic);
        setFinalStatic(StockTransLogisticServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(mapper.toDtoBean(stockTransLogistic)).thenReturn(stockTransLogisticDTO);
        when(mapper.toPersistenceBean(stockTransLogisticDTO)).thenReturn(stockTransLogistic);
        when(spyService.findOne(anyLong())).thenReturn(null);
        spyService.updateStockTransLogistics(1L,1L,baseMessage);
    }

/** -------------------------------------------------------------------- **/
    /** --- Test for StockTransLogisticServiceImpl.updateStockTransLogisticRetry method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testUpdateStockTransLogisticRetry_1() throws Exception {
        BaseMessage baseMessage = new BaseMessage();
        baseMessage.setErrorCode("404");
        baseMessage.setDescription("Not found");

        StockTransLogisticServiceImpl spyService = PowerMockito.spy( stockTransLogisticService);
        StockTransLogistic stockTransLogistic = mock(StockTransLogistic.class);
        StockTransLogisticDTO stockTransLogisticDTO = mock(StockTransLogisticDTO.class);

        Mockito.when(repository.findOne(anyLong())).thenReturn(stockTransLogistic);
        setFinalStatic(StockTransLogisticServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(mapper.toDtoBean(stockTransLogistic)).thenReturn(stockTransLogisticDTO);
        when(mapper.toPersistenceBean(stockTransLogisticDTO)).thenReturn(stockTransLogistic);
        when(spyService.findOne(anyLong())).thenReturn(stockTransLogisticDTO);
        when(stockTransLogisticDTO.getLogisticRetry()).thenReturn(1L);
        spyService.updateStockTransLogisticRetry(1L,1L,baseMessage);
    }

    @Test
    public void testUpdateStockTransLogisticRetry_2() throws Exception {
        BaseMessage baseMessage = new BaseMessage();
        baseMessage.setErrorCode("404");
        baseMessage.setDescription("Not found");

        StockTransLogisticServiceImpl spyService = PowerMockito.spy( stockTransLogisticService);
        StockTransLogistic stockTransLogistic = mock(StockTransLogistic.class);
        StockTransLogisticDTO stockTransLogisticDTO = mock(StockTransLogisticDTO.class);

        Mockito.when(repository.findOne(anyLong())).thenReturn(stockTransLogistic);
        setFinalStatic(StockTransLogisticServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(mapper.toDtoBean(stockTransLogistic)).thenReturn(stockTransLogisticDTO);
        when(mapper.toPersistenceBean(stockTransLogisticDTO)).thenReturn(stockTransLogistic);
        when(spyService.findOne(anyLong())).thenReturn(stockTransLogisticDTO);
        when(stockTransLogisticDTO.getLogisticRetry()).thenReturn(1L);
        spyService.updateStockTransLogisticRetry(2L,1L,baseMessage);
    }

    @Test
    public void testUpdateStockTransLogisticRetry_3() throws Exception {
        BaseMessage baseMessage = new BaseMessage();
        baseMessage.setErrorCode("404");
        baseMessage.setDescription("Not found");

        StockTransLogisticServiceImpl spyService = PowerMockito.spy( stockTransLogisticService);
        StockTransLogistic stockTransLogistic = mock(StockTransLogistic.class);
        StockTransLogisticDTO stockTransLogisticDTO = mock(StockTransLogisticDTO.class);

        Mockito.when(repository.findOne(anyLong())).thenReturn(stockTransLogistic);
        setFinalStatic(StockTransLogisticServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(mapper.toDtoBean(stockTransLogistic)).thenReturn(stockTransLogisticDTO);
        when(mapper.toPersistenceBean(stockTransLogisticDTO)).thenReturn(stockTransLogistic);
        when(spyService.findOne(anyLong())).thenReturn(stockTransLogisticDTO);
        when(stockTransLogisticDTO.getLogisticRetry()).thenReturn(0L);
        spyService.updateStockTransLogisticRetry(2L,1L,baseMessage);
    }

    @Test
    public void testUpdateStockTransLogisticRetry_4() throws Exception {
        BaseMessage baseMessage = new BaseMessage();
        baseMessage.setErrorCode("404");
        baseMessage.setDescription("Not found");

        StockTransLogisticServiceImpl spyService = PowerMockito.spy( stockTransLogisticService);
        StockTransLogistic stockTransLogistic = mock(StockTransLogistic.class);
        StockTransLogisticDTO stockTransLogisticDTO = mock(StockTransLogisticDTO.class);

        Mockito.when(repository.findOne(anyLong())).thenReturn(stockTransLogistic);
        setFinalStatic(StockTransLogisticServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(mapper.toDtoBean(stockTransLogistic)).thenReturn(stockTransLogisticDTO);
        when(mapper.toPersistenceBean(stockTransLogisticDTO)).thenReturn(stockTransLogistic);
        when(spyService.findOne(anyLong())).thenReturn(null);
        when(stockTransLogisticDTO.getLogisticRetry()).thenReturn(1L);
        spyService.updateStockTransLogisticRetry(1L,1L,baseMessage);
    }
    static void setFinalStatic(Field field, Object newValue, final Object targetObject) throws Exception {
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(targetObject, newValue);
    }
}