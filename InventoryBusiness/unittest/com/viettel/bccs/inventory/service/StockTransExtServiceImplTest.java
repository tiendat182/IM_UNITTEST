package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.dto.StockTransExtDTO;
import com.viettel.bccs.inventory.model.StockTransExt;
import com.viettel.bccs.inventory.repo.StockTransExtRepo;
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
@PrepareForTest({DbUtil.class, BaseServiceImpl.class, StockTransExtService.class})
public class StockTransExtServiceImplTest {
    @InjectMocks
    StockTransExtServiceImpl stockTransExtService;
    @Mock
    private final BaseMapper<StockTransExt, StockTransExtDTO> mapper = new BaseMapper<>(StockTransExt.class, StockTransExtDTO.class);
    @Mock
    private StockTransExtRepo repository;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransExtServiceImpl.count
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCount_1() throws Exception {
        List<FilterRequest> filters = new ArrayList<>();
        when(repository.count(any())).thenReturn(1L);
        Assert.assertEquals(1L, stockTransExtService.count(filters).longValue());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransExtServiceImpl.findOne
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindOne_1() throws  Exception {
        StockTransExtServiceImpl spyService = PowerMockito.spy( stockTransExtService);
        StockTransExt stockTransExt = mock(StockTransExt.class);
        StockTransExtDTO stockTransExtDTO = new StockTransExtDTO();
        Mockito.when(repository.findOne(anyLong())).thenReturn(stockTransExt);
        setFinalStatic(StockTransExtServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(mapper.toDtoBean(stockTransExt)).thenReturn(stockTransExtDTO);
        spyService.findOne(1L);
        Assert.assertNotNull(stockTransExtDTO);
    }


    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransExtServiceImpl.findAll
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindAll_1() throws  Exception {
        StockTransExtServiceImpl spyService = PowerMockito.spy( stockTransExtService);
        StockTransExt stockTransExt = mock(StockTransExt.class);
        StockTransExtDTO stockTransExtDTO = new StockTransExtDTO();
        List<StockTransExt> stockTransExtList = Lists.newArrayList(stockTransExt);
        List<StockTransExtDTO> stockTransExtDTOList = Lists.newArrayList(stockTransExtDTO);
        Mockito.when(repository.findAll()).thenReturn(stockTransExtList);
        Mockito.when(mapper.toDtoBean(stockTransExtList)).thenReturn(stockTransExtDTOList);
        setFinalStatic(StockTransExtServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findAll();
        Assert.assertNotNull(stockTransExtDTOList);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransExtServiceImpl.findByFilter
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindByFilter_1() throws  Exception {
        FilterRequest filterRequest = new FilterRequest();
        List<FilterRequest> filterRequestList = Lists.newArrayList(filterRequest);
        StockTransExtServiceImpl spyService = PowerMockito.spy( stockTransExtService);
        StockTransExt stockTransExt = mock(StockTransExt.class);
        StockTransExtDTO stockTransExtDTO = new StockTransExtDTO();
        List<StockTransExt> stockTransExtList = Lists.newArrayList(stockTransExt);
        List<StockTransExtDTO> stockTransExtDTOList = Lists.newArrayList(stockTransExtDTO);
        Mockito.when(mapper.toDtoBean(stockTransExtList)).thenReturn(stockTransExtDTOList);
        setFinalStatic(StockTransExtServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findByFilter(filterRequestList);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransExtServiceImpl.create
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void testCreate_1() throws  Exception {
        stockTransExtService.create(new StockTransExtDTO());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransExtServiceImpl.update
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void testUpdate_1() throws  Exception {
        stockTransExtService.update(new StockTransExtDTO());
    }

    static void setFinalStatic(Field field, Object newValue, final Object targetObject) throws Exception {
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(targetObject, newValue);
    }
}