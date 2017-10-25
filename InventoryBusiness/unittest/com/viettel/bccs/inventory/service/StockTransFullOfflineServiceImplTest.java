package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.dto.StockTransFullOfflineDTO;
import com.viettel.bccs.inventory.model.StockTransFullOffline;
import com.viettel.bccs.inventory.repo.StockTransFullOfflineRepo;
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
 * @date 22/10/2017
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({DbUtil.class, BaseServiceImpl.class, StockTransFullOfflineService.class})
public class StockTransFullOfflineServiceImplTest {
    @InjectMocks
    StockTransFullOfflineServiceImpl stockTransFullOfflineService;
    @Mock
    private final BaseMapper<StockTransFullOffline, StockTransFullOfflineDTO> mapper = new BaseMapper<>(StockTransFullOffline.class, StockTransFullOfflineDTO.class);
    @Mock
    private StockTransFullOfflineRepo repository;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransFullOfflineServiceImpl.count
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCount_1() throws Exception {
        List<FilterRequest> filters = new ArrayList<>();
        when(repository.count(any())).thenReturn(1L);
        Assert.assertEquals(1L, stockTransFullOfflineService.count(filters).longValue());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransFullOfflineServiceImpl.findOne
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindOne_1() throws  Exception {
        StockTransFullOfflineServiceImpl spyService = PowerMockito.spy( stockTransFullOfflineService);
        StockTransFullOffline stockTransFullOffline = mock(StockTransFullOffline.class);
        StockTransFullOfflineDTO stockTransFullOfflineDTO = new StockTransFullOfflineDTO();
        Mockito.when(repository.findOne(anyLong())).thenReturn(stockTransFullOffline);
        setFinalStatic(StockTransFullOfflineServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(mapper.toDtoBean(stockTransFullOffline)).thenReturn(stockTransFullOfflineDTO);
        spyService.findOne(1L);
        Assert.assertNotNull(stockTransFullOfflineDTO);
    }


    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransFullOfflineServiceImpl.findAll
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindAll_1() throws  Exception {
        StockTransFullOfflineServiceImpl spyService = PowerMockito.spy( stockTransFullOfflineService);
        StockTransFullOffline stockTransFullOffline = mock(StockTransFullOffline.class);
        StockTransFullOfflineDTO stockTransFullOfflineDTO = new StockTransFullOfflineDTO();
        List<StockTransFullOffline> stockTransFullOfflineList = Lists.newArrayList(stockTransFullOffline);
        List<StockTransFullOfflineDTO> stockTransFullOfflineDTOList = Lists.newArrayList(stockTransFullOfflineDTO);
        Mockito.when(repository.findAll()).thenReturn(stockTransFullOfflineList);
        Mockito.when(mapper.toDtoBean(stockTransFullOfflineList)).thenReturn(stockTransFullOfflineDTOList);
        setFinalStatic(StockTransFullOfflineServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findAll();
        Assert.assertNotNull(stockTransFullOfflineDTOList);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransFullOfflineServiceImpl.findByFilter
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindByFilter_1() throws  Exception {
        FilterRequest filterRequest = new FilterRequest();
        List<FilterRequest> filterRequestList = Lists.newArrayList(filterRequest);
        StockTransFullOfflineServiceImpl spyService = PowerMockito.spy( stockTransFullOfflineService);
        StockTransFullOffline stockTransFullOffline = mock(StockTransFullOffline.class);
        StockTransFullOfflineDTO stockTransFullOfflineDTO = new StockTransFullOfflineDTO();
        List<StockTransFullOffline> stockTransFullOfflineList = Lists.newArrayList(stockTransFullOffline);
        List<StockTransFullOfflineDTO> stockTransFullOfflineDTOList = Lists.newArrayList(stockTransFullOfflineDTO);
        Mockito.when(mapper.toDtoBean(stockTransFullOfflineList)).thenReturn(stockTransFullOfflineDTOList);
        setFinalStatic(StockTransFullOfflineServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findByFilter(filterRequestList);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransFullOfflineServiceImpl.create
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void testCreate_1() throws  Exception {
        stockTransFullOfflineService.create(new StockTransFullOfflineDTO());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransFullOfflineServiceImpl.update
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void testUpdate_1() throws  Exception {
        stockTransFullOfflineService.update(new StockTransFullOfflineDTO());
    }

    static void setFinalStatic(Field field, Object newValue, final Object targetObject) throws Exception {
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(targetObject, newValue);
    }
}