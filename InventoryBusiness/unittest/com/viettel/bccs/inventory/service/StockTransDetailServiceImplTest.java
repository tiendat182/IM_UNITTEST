package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.dto.StockTransDetailDTO;
import com.viettel.bccs.inventory.model.StockTransDetail;
import com.viettel.bccs.inventory.repo.StockTransDetailRepo;
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
@PrepareForTest({DbUtil.class, BaseServiceImpl.class, StockTransDetailService.class})
public class StockTransDetailServiceImplTest {
    @InjectMocks
    StockTransDetailServiceImpl stockTransDetailService;
    @Mock
    private final BaseMapper<StockTransDetail, StockTransDetailDTO> mapper = new BaseMapper(StockTransDetail.class, StockTransDetailDTO.class);
    @Mock
    private StockTransDetailRepo repository;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransDetailServiceImpl.count
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCount_1() throws Exception {
        List<FilterRequest> filters = new ArrayList<>();
        when(repository.count(any())).thenReturn(1L);
        Assert.assertEquals(1L, stockTransDetailService.count(filters).longValue());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransDetailServiceImpl.findOne
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindOne_1() throws  Exception {
        StockTransDetailServiceImpl spyService = PowerMockito.spy( stockTransDetailService);
        StockTransDetail stockTransDetail = mock(StockTransDetail.class);
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        Mockito.when(repository.findOne(anyLong())).thenReturn(stockTransDetail);
        setFinalStatic(StockTransDetailServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(mapper.toDtoBean(stockTransDetail)).thenReturn(stockTransDetailDTO);
        spyService.findOne(1L);
        Assert.assertNotNull(stockTransDetailDTO);
    }


    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransDetailServiceImpl.findAll
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindAll_1() throws  Exception {
        StockTransDetailServiceImpl spyService = PowerMockito.spy( stockTransDetailService);
        StockTransDetail stockTransDetail = mock(StockTransDetail.class);
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetail> stockTransDetailList = Lists.newArrayList(stockTransDetail);
        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList(stockTransDetailDTO);
        Mockito.when(repository.findAll()).thenReturn(stockTransDetailList);
        Mockito.when(mapper.toDtoBean(stockTransDetailList)).thenReturn(stockTransDetailDTOList);
        setFinalStatic(StockTransDetailServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findAll();
        Assert.assertNotNull(stockTransDetailDTOList);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransDetailServiceImpl.findByFilter
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindByFilter_1() throws  Exception {
        FilterRequest filterRequest = new FilterRequest();
        List<FilterRequest> filterRequestList = Lists.newArrayList(filterRequest);
        StockTransDetailServiceImpl spyService = PowerMockito.spy( stockTransDetailService);
        StockTransDetail stockTransDetail = mock(StockTransDetail.class);
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetail> stockTransDetailList = Lists.newArrayList(stockTransDetail);
        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList(stockTransDetailDTO);
        Mockito.when(mapper.toDtoBean(stockTransDetailList)).thenReturn(stockTransDetailDTOList);
        setFinalStatic(StockTransDetailServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findByFilter(filterRequestList);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransDetailServiceImpl.save
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testSave_1() throws  Exception {
        StockTransDetailServiceImpl spyService = PowerMockito.spy( stockTransDetailService);
        StockTransDetail stockTransDetail = mock(StockTransDetail.class);
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetail> stockTransDetailList = Lists.newArrayList(stockTransDetail);
        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList(stockTransDetailDTO);
        Mockito.when(mapper.toDtoBean(stockTransDetailList)).thenReturn(stockTransDetailDTOList);
        Mockito.when(mapper.toPersistenceBean(stockTransDetailDTO)).thenReturn(stockTransDetail);
        setFinalStatic(StockTransDetailServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.save(new StockTransDetailDTO());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransDetailServiceImpl.findByStockTransId
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindByStockTransId_1() throws  Exception {
        stockTransDetailService.findByStockTransId(1L);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransDetailServiceImpl.getSingleDetail
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testGetSingleDetail_1() throws  Exception {
        FilterRequest filterRequest = new FilterRequest();
        List<FilterRequest> filterRequestList = Lists.newArrayList(filterRequest);
        StockTransDetailServiceImpl spyService = PowerMockito.spy( stockTransDetailService);
        StockTransDetail stockTransDetail = mock(StockTransDetail.class);
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetail> stockTransDetailList = Lists.newArrayList(stockTransDetail);
        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList(stockTransDetailDTO);
        Mockito.when(mapper.toDtoBean(stockTransDetailList)).thenReturn(stockTransDetailDTOList);
        setFinalStatic(StockTransDetailServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);

        List<StockTransDetailDTO> lstDetail = Lists.newArrayList(stockTransDetailDTO);
        when(spyService.findByFilter(filterRequestList)).thenReturn(lstDetail);
        spyService.getSingleDetail(1L, 1L, 1L);
    }

    @Test
    public void testGetSingleDetail_2() throws  Exception {
        FilterRequest filterRequest = new FilterRequest();
        List<FilterRequest> filterRequestList = Lists.newArrayList(filterRequest);
        StockTransDetailServiceImpl spyService = PowerMockito.spy( stockTransDetailService);
        StockTransDetail stockTransDetail = mock(StockTransDetail.class);
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetail> stockTransDetailList = Lists.newArrayList(stockTransDetail);
        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList(stockTransDetailDTO);
        Mockito.when(mapper.toDtoBean(stockTransDetailList)).thenReturn(stockTransDetailDTOList);
        setFinalStatic(StockTransDetailServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);

        List<StockTransDetailDTO> lstDetail = Lists.newArrayList();
        when(spyService.findByFilter(filterRequestList)).thenReturn(lstDetail);
        spyService.getSingleDetail(1L, 1L, 1L);
    }

    static void setFinalStatic(Field field, Object newValue, final Object targetObject) throws Exception {
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(targetObject, newValue);
    }
}