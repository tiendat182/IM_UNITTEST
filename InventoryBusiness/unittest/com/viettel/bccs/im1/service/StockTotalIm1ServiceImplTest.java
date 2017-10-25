package com.viettel.bccs.im1.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.im1.dto.StockTotalIm1DTO;
import com.viettel.bccs.im1.dto.StockTransDetailIm1DTO;
import com.viettel.bccs.im1.model.StockTotalIm1;
import com.viettel.bccs.im1.repo.StockTotalIm1Repo;
import com.viettel.bccs.inventory.common.Const;
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

import javax.persistence.EntityManager;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;
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
@PrepareForTest({DbUtil.class, BaseServiceImpl.class, StockTotalIm1Service.class})
public class StockTotalIm1ServiceImplTest {
    @InjectMocks
    StockTotalIm1ServiceImpl stockTotalIm1Service;
    @Mock
    private final BaseMapper<StockTotalIm1, StockTotalIm1DTO> mapper = new BaseMapper<>(StockTotalIm1.class, StockTotalIm1DTO.class);
    @Mock
    private StockTotalIm1Repo repository;
    @Mock
    private StockTotalAuditIm1Service stockTotalAuditIm1Service;
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
     * Test for method StockTotalIm1ServiceImpl.count
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCount_1() throws  Exception {
        List<FilterRequest> filters = new ArrayList<>();
        when(repository.count(any())).thenReturn(1L);
        Assert.assertEquals(1L, stockTotalIm1Service.count(filters).longValue());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTotalIm1ServiceImpl.findStockTotal
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void testFindStockTotal_1() throws  Exception {
        StockTotalIm1DTO stockTotalDTO = null;

        stockTotalIm1Service.findStockTotal(stockTotalDTO);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTotalIm1ServiceImpl.findStockTotal
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void testFindStockTotal_2() throws  Exception {
        StockTotalIm1DTO stockTotalDTO = new StockTotalIm1DTO();

        when(repository.findOneStockTotal(any())).thenThrow(new Exception());
        stockTotalIm1Service.findStockTotal(stockTotalDTO);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTotalIm1ServiceImpl.findStockTotal
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindStockTotal_3() throws  Exception {
        StockTotalIm1DTO stockTotalDTO = new StockTotalIm1DTO();
        List<StockTotalIm1DTO> stockTotalIm1DTOList = Lists.newArrayList();

        when(repository.findOneStockTotal(any())).thenReturn(stockTotalIm1DTOList);
        stockTotalIm1Service.findStockTotal(stockTotalDTO);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTotalIm1ServiceImpl.findAll
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindAll_1() throws  Exception {
        StockTotalIm1ServiceImpl spyService = PowerMockito.spy(stockTotalIm1Service);
        StockTotalIm1 stockTotalIm1 = mock(StockTotalIm1.class);
        StockTotalIm1DTO stockTotalIm1DTO = new StockTotalIm1DTO();
        List<StockTotalIm1DTO> stockTotalIm1DTOList = Lists.newArrayList(stockTotalIm1DTO);
        List<StockTotalIm1> stockTotalIm1List = Lists.newArrayList(stockTotalIm1);
        Mockito.when(repository.findAll()).thenReturn(stockTotalIm1List);
        Mockito.when(mapper.toDtoBean(stockTotalIm1List)).thenReturn(stockTotalIm1DTOList);
        setFinalStatic(StockTotalIm1ServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findAll();
        Assert.assertNotNull(stockTotalIm1DTOList);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTotalIm1ServiceImpl.findByFilter
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindByFilter_1() throws  Exception {
        FilterRequest filterRequest = new FilterRequest();
        List<FilterRequest> filterRequestList = Lists.newArrayList(filterRequest);
        StockTotalIm1ServiceImpl spyService = PowerMockito.spy(stockTotalIm1Service);
        StockTotalIm1 stockTotalIm1 = mock(StockTotalIm1.class);
        StockTotalIm1DTO stockTotalIm1DTO = new StockTotalIm1DTO();
        List<StockTotalIm1DTO> stockTotalIm1DTOList = Lists.newArrayList(stockTotalIm1DTO);
        List<StockTotalIm1> stockTotalIm1List = Lists.newArrayList(stockTotalIm1);
        Mockito.when(mapper.toDtoBean(stockTotalIm1List)).thenReturn(stockTotalIm1DTOList);
        setFinalStatic(StockTotalIm1ServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findByFilter(filterRequestList);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTotalIm1ServiceImpl.create
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCreate_1() throws  Exception {
        stockTotalIm1Service.create(new StockTotalIm1DTO());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTotalIm1ServiceImpl.update
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testUpdate_1() throws  Exception {
        stockTotalIm1Service.update(new StockTotalIm1DTO());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTotalIm1ServiceImpl.save
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testSave_1() throws  Exception {
        StockTotalIm1ServiceImpl spyService = PowerMockito.spy(stockTotalIm1Service);
        StockTotalIm1 stockTotalIm1 = mock(StockTotalIm1.class);
        StockTotalIm1DTO stockTotalIm1DTO = new StockTotalIm1DTO();
        List<StockTotalIm1DTO> stockTotalIm1DTOList = Lists.newArrayList(stockTotalIm1DTO);
        List<StockTotalIm1> stockTotalIm1List = Lists.newArrayList(stockTotalIm1);

        Mockito.when(mapper.toDtoBean(stockTotalIm1List)).thenReturn(stockTotalIm1DTOList);
        Mockito.when(mapper.toPersistenceBean(stockTotalIm1DTO)).thenReturn(stockTotalIm1);
        setFinalStatic(StockTotalIm1ServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        stockTotalIm1Service.save(new StockTotalIm1DTO());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTotalIm1ServiceImpl.findStockTransDetail
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void testFindStockTransDetail_1() throws  Exception {
        stockTotalIm1Service.findStockTransDetail(null);
    }

    @Test
    public void testFindStockTransDetail_2() throws  Exception {
        stockTotalIm1Service.findStockTransDetail(1L);
    }

    @Test(expected = Exception.class)
    public void testFindStockTransDetail_3() throws  Exception {
        when(repository.findStockTransDetail(anyLong())).thenThrow(new Exception());
        stockTotalIm1Service.findStockTransDetail(1L);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTotalIm1ServiceImpl.findOneStockModel
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void testFindOneStockModel_1() throws  Exception {
        stockTotalIm1Service.findOneStockModel(null);
    }

    @Test
    public void testFindOneStockModel_2() throws  Exception {
        stockTotalIm1Service.findOneStockModel(1L);
    }

    @Test(expected = Exception.class)
    public void testFindOneStockModel_3() throws  Exception {
        when(repository.findOneStockModel(anyLong())).thenThrow(new Exception());
        stockTotalIm1Service.findOneStockModel(1L);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTotalIm1ServiceImpl.findOneStockType
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void testFindOneStockType_1() throws  Exception {
        stockTotalIm1Service.findOneStockType(null);
    }

    @Test
    public void testFindOneStockType_2() throws  Exception {
        stockTotalIm1Service.findOneStockType(1L);
    }

    @Test(expected = Exception.class)
    public void testFindOneStockType_3() throws  Exception {
        when(repository.findOneStockType(anyLong())).thenThrow(new Exception());
        stockTotalIm1Service.findOneStockType(1L);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTotalIm1ServiceImpl.findSerialByStockTransDetail
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void testFindSerialByStockTransDetail_1() throws  Exception {
        stockTotalIm1Service.findSerialByStockTransDetail(null);
    }

    @Test
    public void testFindSerialByStockTransDetail_2() throws  Exception {
        stockTotalIm1Service.findSerialByStockTransDetail(new StockTransDetailIm1DTO());
    }

    @Test(expected = Exception.class)
    public void testFindSerialByStockTransDetail_3() throws  Exception {
        when(repository.findSerialByStockTransDetail(any())).thenThrow(new Exception());
        stockTotalIm1Service.findSerialByStockTransDetail(new StockTransDetailIm1DTO());
    }


    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTotalIm1ServiceImpl.changeStockTotal
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testChangeStockTotal_1() throws  Exception {
        //in 169
        StockTotalIm1ServiceImpl spyService = PowerMockito.spy(stockTotalIm1Service);
        StockTotalIm1DTO stockTotalIm1DTO1 = new StockTotalIm1DTO();
        List<StockTotalIm1DTO> stockTotalIm1DTOList = Lists.newArrayList(stockTotalIm1DTO1);
        when(repository.findOneStockTotal(any())).thenReturn(stockTotalIm1DTOList);

        List<StockTotalIm1DTO> lsStockTotalDto = Lists.newArrayList(stockTotalIm1DTO1);
        when(spyService.findStockTotal(stockTotalIm1DTO1)).thenReturn(lsStockTotalDto);

        spyService.changeStockTotal(1L,1L,1L,1L,1L,1L,1L,
                1L,1L,1L,new Date(),"1","1",Const.SourceType.STOCK_TRANS);
    }

    @Test
    public void testChangeStockTotal_2() throws  Exception {
        //out 169
        //in 177
        StockTotalIm1ServiceImpl spyService = PowerMockito.spy(stockTotalIm1Service);
        StockTotalIm1DTO stockTotalIm1DTO1 = new StockTotalIm1DTO();
        List<StockTotalIm1DTO> stockTotalIm1DTOList = Lists.newArrayList();
        when(repository.findOneStockTotal(any())).thenReturn(stockTotalIm1DTOList);

        List<StockTotalIm1DTO> lsStockTotalDto = Lists.newArrayList();
        when(spyService.findStockTotal(stockTotalIm1DTO1)).thenReturn(lsStockTotalDto);

        spyService.changeStockTotal(1L,1L,1L,1L,-1L,1L,1L,
                1L,1L,1L,new Date(),"1","1",Const.SourceType.STOCK_TRANS);
    }

    @Test
    public void testChangeStockTotal_3() throws  Exception {
        //out 169
        //in 177
        StockTotalIm1ServiceImpl spyService = PowerMockito.spy(stockTotalIm1Service);
        StockTotalIm1DTO stockTotalIm1DTO1 = new StockTotalIm1DTO();
        List<StockTotalIm1DTO> stockTotalIm1DTOList = Lists.newArrayList();
        when(repository.findOneStockTotal(any())).thenReturn(stockTotalIm1DTOList);

        List<StockTotalIm1DTO> lsStockTotalDto = Lists.newArrayList();
        when(spyService.findStockTotal(stockTotalIm1DTO1)).thenReturn(lsStockTotalDto);

        spyService.changeStockTotal(1L,1L,1L,1L,1L,-1L,1L,
                1L,1L,1L,new Date(),"1","1",Const.SourceType.STOCK_TRANS);
    }

    @Test
    public void testChangeStockTotal_4() throws  Exception {
        //out 169
        //in 177
        StockTotalIm1ServiceImpl spyService = PowerMockito.spy(stockTotalIm1Service);
        StockTotalIm1DTO stockTotalIm1DTO1 = new StockTotalIm1DTO();
        List<StockTotalIm1DTO> stockTotalIm1DTOList = Lists.newArrayList();
        when(repository.findOneStockTotal(any())).thenReturn(stockTotalIm1DTOList);

        List<StockTotalIm1DTO> lsStockTotalDto = Lists.newArrayList();
        when(spyService.findStockTotal(stockTotalIm1DTO1)).thenReturn(lsStockTotalDto);

        spyService.changeStockTotal(1L,1L,1L,1L,1L,1L,-1L,
                1L,1L,1L,new Date(),"1","1",Const.SourceType.STOCK_TRANS);
    }

    @Test
    public void testChangeStockTotal_5() throws  Exception {
        //out 177
        StockTotalIm1ServiceImpl spyService = PowerMockito.spy(stockTotalIm1Service);
        StockTotalIm1DTO stockTotalIm1DTO1 = new StockTotalIm1DTO();
        List<StockTotalIm1DTO> stockTotalIm1DTOList = Lists.newArrayList(stockTotalIm1DTO1);
        when(repository.findOneStockTotal(any())).thenReturn(stockTotalIm1DTOList);

        List<StockTotalIm1DTO> lsStockTotalDto = Lists.newArrayList();
        when(spyService.findStockTotal(stockTotalIm1DTO1)).thenReturn(lsStockTotalDto);

        spyService.changeStockTotal(1L,1L,1L,1L,-1L,-1L,-1L,
                1L,1L,1L,new Date(),"1","1",Const.SourceType.STOCK_TRANS);
    }

    @Test
    public void testChangeStockTotal_6() throws  Exception {
        //out 177
        //in 182
        StockTotalIm1ServiceImpl spyService = PowerMockito.spy(stockTotalIm1Service);
        StockTotalIm1DTO stockTotalIm1DTO1 = new StockTotalIm1DTO();
        List<StockTotalIm1DTO> stockTotalIm1DTOList = Lists.newArrayList();
        when(repository.findOneStockTotal(any())).thenReturn(stockTotalIm1DTOList);

        List<StockTotalIm1DTO> lsStockTotalDto = Lists.newArrayList();
        when(spyService.findStockTotal(stockTotalIm1DTO1)).thenReturn(lsStockTotalDto);

        spyService.changeStockTotal(1L,1L,1L,1L,0L,0L,0L,
                1L,1L,1L,new Date(),"1","1",Const.SourceType.STOCK_TRANS);
    }

    @Test
    public void testChangeStockTotal_7() throws  Exception {
        //out 182
        StockTotalIm1ServiceImpl spyService = PowerMockito.spy(stockTotalIm1Service);
        StockTotalIm1DTO stockTotalIm1DTO1 = new StockTotalIm1DTO();
        List<StockTotalIm1DTO> stockTotalIm1DTOList = Lists.newArrayList();
        when(repository.findOneStockTotal(any())).thenReturn(stockTotalIm1DTOList);

        List<StockTotalIm1DTO> lsStockTotalDto = Lists.newArrayList();
        when(spyService.findStockTotal(stockTotalIm1DTO1)).thenReturn(lsStockTotalDto);

        spyService.changeStockTotal(1L,1L,1L,1L,10L,0L,0L,
                1L,1L,1L,new Date(),"1","1",Const.SourceType.STOCK_TRANS);
    }

    @Test
    public void testChangeStockTotal_8() throws  Exception {
        //out 182
        StockTotalIm1ServiceImpl spyService = PowerMockito.spy(stockTotalIm1Service);
        StockTotalIm1DTO stockTotalIm1DTO1 = new StockTotalIm1DTO();
        List<StockTotalIm1DTO> stockTotalIm1DTOList = Lists.newArrayList();
        when(repository.findOneStockTotal(any())).thenReturn(stockTotalIm1DTOList);

        List<StockTotalIm1DTO> lsStockTotalDto = Lists.newArrayList();
        when(spyService.findStockTotal(stockTotalIm1DTO1)).thenReturn(lsStockTotalDto);

        spyService.changeStockTotal(1L,1L,1L,1L,0L,10L,0L,
                1L,1L,1L,new Date(),"1","1",Const.SourceType.STOCK_TRANS);
    }

    @Test
    public void testChangeStockTotal_9() throws  Exception {
        //out 182
        StockTotalIm1ServiceImpl spyService = PowerMockito.spy(stockTotalIm1Service);
        StockTotalIm1DTO stockTotalIm1DTO1 = new StockTotalIm1DTO();
        List<StockTotalIm1DTO> stockTotalIm1DTOList = Lists.newArrayList();
        when(repository.findOneStockTotal(any())).thenReturn(stockTotalIm1DTOList);

        List<StockTotalIm1DTO> lsStockTotalDto = Lists.newArrayList();
        when(spyService.findStockTotal(stockTotalIm1DTO1)).thenReturn(lsStockTotalDto);

        spyService.changeStockTotal(1L,1L,1L,1L,0L,0L,10L,
                1L,1L,1L,new Date(),"1","1",Const.SourceType.STOCK_TRANS);
    }

    @Test
    public void testChangeStockTotal_10() throws  Exception {
        //out 177
        StockTotalIm1ServiceImpl spyService = PowerMockito.spy(stockTotalIm1Service);
        StockTotalIm1DTO stockTotalIm1DTO1 = mock(StockTotalIm1DTO.class);
        List<StockTotalIm1DTO> stockTotalIm1DTOList = Lists.newArrayList(stockTotalIm1DTO1);
        when(repository.findOneStockTotal(any())).thenReturn(stockTotalIm1DTOList);

        List<StockTotalIm1DTO> lsStockTotalDto = Lists.newArrayList();
        when(spyService.findStockTotal(stockTotalIm1DTO1)).thenReturn(lsStockTotalDto);
        when(stockTotalIm1DTO1.getQuantity()).thenReturn(2L);
        when(stockTotalIm1DTO1.getQuantityIssue()).thenReturn(-2L);

        spyService.changeStockTotal(1L,1L,1L,1L,-1L,-1L,-1L,
                1L,1L,1L,new Date(),"1","1",Const.SourceType.STOCK_TRANS);
    }

    @Test(expected = Exception.class)
    public void testChangeStockTotal_11() throws  Exception {
        //out 177
        StockTotalIm1ServiceImpl spyService = PowerMockito.spy(stockTotalIm1Service);
        StockTotalIm1DTO stockTotalIm1DTO1 = mock(StockTotalIm1DTO.class);
        List<StockTotalIm1DTO> stockTotalIm1DTOList = Lists.newArrayList(stockTotalIm1DTO1);
        when(repository.findOneStockTotal(any())).thenReturn(stockTotalIm1DTOList);

        when(spyService.findStockTotal(any())).thenThrow(new Exception());
        when(stockTotalIm1DTO1.getQuantity()).thenReturn(2L);
        when(stockTotalIm1DTO1.getQuantityIssue()).thenReturn(-2L);

        spyService.changeStockTotal(null,1L,1L,1L,-1L,-1L,-1L,
                1L,1L,1L,new Date(),"1","1",Const.SourceType.STOCK_TRANS);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTotalIm1ServiceImpl.getStockTotalForProcessStock
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testGetStockTotalForProcessStock_1() throws  Exception {
        StockTotalIm1DTO stockTotalIm1DTO = new StockTotalIm1DTO();
        List<StockTotalIm1DTO> stockTotalIm1DTOList = Lists.newArrayList(stockTotalIm1DTO);

        when(repository.findOneStockTotal(any())).thenReturn(null);
        stockTotalIm1Service.getStockTotalForProcessStock(1L,1L,1L,1L);
    }

    @Test
    public void testGetStockTotalForProcessStock_2() throws  Exception {
        StockTotalIm1DTO stockTotalIm1DTO = new StockTotalIm1DTO();
        List<StockTotalIm1DTO> stockTotalIm1DTOList = Lists.newArrayList(stockTotalIm1DTO);

        when(repository.findOneStockTotal(any())).thenReturn(stockTotalIm1DTOList);
        stockTotalIm1Service.getStockTotalForProcessStock(1L,1L,1L,1L);
    }

    @Test
    public void testGetStockTotalForProcessStock_3() throws  Exception {
        when(repository.findOneStockTotal(any())).thenThrow(new Exception());
        stockTotalIm1Service.getStockTotalForProcessStock(1L,1L,1L,1L);
    }



    static void setFinalStatic(Field field, Object newValue, final Object targetObject) throws Exception {
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(targetObject, newValue);
    }
}