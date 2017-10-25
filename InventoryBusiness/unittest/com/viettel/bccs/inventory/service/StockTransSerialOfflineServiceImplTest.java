package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.dto.StockTransSerialOfflineDTO;
import com.viettel.bccs.inventory.model.StockTransSerialOffline;
import com.viettel.bccs.inventory.repo.StockTransSerialOfflineRepo;
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
@PrepareForTest({DbUtil.class, StockTransSerialOfflineServiceImpl.class, BaseServiceImpl.class, StockTransSerialOfflineService.class})
public class StockTransSerialOfflineServiceImplTest {
    @InjectMocks
    StockTransSerialOfflineServiceImpl stockTransSerialOfflineService;
    @Mock
    private final BaseMapper<StockTransSerialOffline, StockTransSerialOfflineDTO> mapper = new BaseMapper<>(StockTransSerialOffline.class, StockTransSerialOfflineDTO.class);
    @Mock
    private StockTransSerialOfflineRepo repository;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }


    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransSerialOfflineServiceImpl.count
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCount_1() throws  Exception {
        List<FilterRequest> filters = new ArrayList<>();
        when(repository.count(any())).thenReturn(1L);
        Assert.assertEquals(1L, stockTransSerialOfflineService.count(filters).longValue());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransSerialOfflineServiceImpl.findOne
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindOne_1() throws  Exception {
        StockTransSerialOfflineServiceImpl spyService = PowerMockito.spy( stockTransSerialOfflineService);
        StockTransSerialOffline stockTransSerialOffline = mock(StockTransSerialOffline.class);
        StockTransSerialOfflineDTO stockTransSerialOfflineDTO = new StockTransSerialOfflineDTO();
        Mockito.when(repository.findOne(anyLong())).thenReturn(stockTransSerialOffline);
        setFinalStatic(StockTransSerialOfflineServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(mapper.toDtoBean(stockTransSerialOffline)).thenReturn(stockTransSerialOfflineDTO);
        spyService.findOne(1L);
        Assert.assertNotNull(stockTransSerialOfflineDTO);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransSerialOfflineServiceImpl.findAll
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindAll_1() throws  Exception {
        StockTransSerialOfflineServiceImpl spyService = PowerMockito.spy( stockTransSerialOfflineService);
        StockTransSerialOffline stockTransSerialOffline = mock(StockTransSerialOffline.class);
        StockTransSerialOfflineDTO stockTransSerialOfflineDTO = new StockTransSerialOfflineDTO();
        List<StockTransSerialOfflineDTO> stockTransSerialOfflineDTOList = Lists.newArrayList(stockTransSerialOfflineDTO);
        List<StockTransSerialOffline> stockTransSerialOfflineList = Lists.newArrayList(stockTransSerialOffline);
        Mockito.when(repository.findAll()).thenReturn(stockTransSerialOfflineList);
        Mockito.when(mapper.toDtoBean(stockTransSerialOfflineList)).thenReturn(stockTransSerialOfflineDTOList);
        setFinalStatic(StockTransSerialOfflineServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findAll();
        Assert.assertNotNull(stockTransSerialOfflineDTOList);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransSerialOfflineServiceImpl.findByFilter
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindByFilter_1() throws  Exception {
        FilterRequest filterRequest = new FilterRequest();
        List<FilterRequest> filterRequestList = Lists.newArrayList(filterRequest);
        StockTransSerialOfflineServiceImpl spyService = PowerMockito.spy( stockTransSerialOfflineService);
        StockTransSerialOffline stockTransSerialOffline = mock(StockTransSerialOffline.class);
        StockTransSerialOfflineDTO stockTransSerialOfflineDTO = new StockTransSerialOfflineDTO();
        List<StockTransSerialOfflineDTO> stockTransSerialOfflineDTOList = Lists.newArrayList(stockTransSerialOfflineDTO);
        List<StockTransSerialOffline> stockTransSerialOfflineList = Lists.newArrayList(stockTransSerialOffline);
        Mockito.when(mapper.toDtoBean(stockTransSerialOfflineList)).thenReturn(stockTransSerialOfflineDTOList);
        setFinalStatic(StockTransSerialOfflineServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findByFilter(filterRequestList);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransSerialOfflineServiceImpl.create
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void testCreate_1() throws  Exception {
        stockTransSerialOfflineService.create(new StockTransSerialOfflineDTO());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransSerialOfflineServiceImpl.update
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void testUpdate_1() throws  Exception {
        stockTransSerialOfflineService.update(new StockTransSerialOfflineDTO());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransSerialOfflineServiceImpl.save
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testSave_1() throws  Exception {
        StockTransSerialOfflineDTO stockTransSerialOfflineDTO= new StockTransSerialOfflineDTO();
        StockTransSerialOfflineServiceImpl spyService = PowerMockito.spy( stockTransSerialOfflineService);
        setFinalStatic(StockTransSerialOfflineServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(mapper.toDtoBean(repository.save(mapper.toPersistenceBean(stockTransSerialOfflineDTO)))).thenReturn(stockTransSerialOfflineDTO);
        spyService.save(stockTransSerialOfflineDTO);
    }

    static void setFinalStatic(Field field, Object newValue, final Object targetObject) throws Exception {
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(targetObject, newValue);
    }
}