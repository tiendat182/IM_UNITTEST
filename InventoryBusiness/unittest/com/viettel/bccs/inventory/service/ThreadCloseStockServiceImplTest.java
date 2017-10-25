package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.dto.ThreadCloseStockDTO;
import com.viettel.bccs.inventory.model.ThreadCloseStock;
import com.viettel.bccs.inventory.repo.ThreadCloseStockRepo;
import com.viettel.fw.common.util.DateUtil;
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
import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author DatLT
 * @date 15/10/2017
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({DbUtil.class, ThreadCloseStockServiceImpl.class, BaseServiceImpl.class, ThreadCloseStockService.class, DateUtil.class})
public class ThreadCloseStockServiceImplTest {
    @InjectMocks
    ThreadCloseStockServiceImpl threadCloseStockService;
    @Mock
    private final BaseMapper<ThreadCloseStock, ThreadCloseStockDTO> mapper = new BaseMapper<>(ThreadCloseStock.class, ThreadCloseStockDTO.class);
    @Mock
    private EntityManager em;
    @Mock
    private ThreadCloseStockRepo repository;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method ThreadCloseStockServiceImpl.count
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCount_1() throws Exception {
        List<FilterRequest> filters = Lists.newArrayList();
        Mockito.when(repository.count(any())).thenReturn(1L);
        Assert.assertEquals(1L, threadCloseStockService.count(filters).longValue());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method ThreadCloseStockServiceImpl.findOne
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindOne_1() throws  Exception {
        ThreadCloseStockServiceImpl spyService = PowerMockito.spy( threadCloseStockService);
        ThreadCloseStock threadCloseStock = mock(ThreadCloseStock.class);
        ThreadCloseStockDTO threadCloseStockDTO = new ThreadCloseStockDTO();
        Mockito.when(repository.findOne(anyLong())).thenReturn(threadCloseStock);
        setFinalStatic(ThreadCloseStockServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(mapper.toDtoBean(threadCloseStock)).thenReturn(threadCloseStockDTO);
        spyService.findOne(1L);
        Assert.assertNotNull(threadCloseStockDTO);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method ThreadCloseStockServiceImpl.findAll
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Test
    public void testFindAll_1() throws  Exception {
        ThreadCloseStockServiceImpl spyService = PowerMockito.spy( threadCloseStockService);
        ThreadCloseStock threadCloseStock = mock(ThreadCloseStock.class);
        ThreadCloseStockDTO threadCloseStockDTO = new ThreadCloseStockDTO();
        List<ThreadCloseStockDTO> threadCloseStockDTOList = Lists.newArrayList(threadCloseStockDTO);
        List<ThreadCloseStock> threadCloseStockList = Lists.newArrayList(threadCloseStock);
        Mockito.when(repository.findAll()).thenReturn(threadCloseStockList);
        Mockito.when(mapper.toDtoBean(threadCloseStockList)).thenReturn(threadCloseStockDTOList);
        setFinalStatic(ThreadCloseStockServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findAll();
        Assert.assertNotNull(threadCloseStockDTOList);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method ThreadCloseStockServiceImpl.findByFilter
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Test
    public void testFindByFilter_1() throws  Exception {
        FilterRequest filterRequest = new FilterRequest();
        List<FilterRequest> filterRequestList = Lists.newArrayList(filterRequest);
        ThreadCloseStockServiceImpl spyService = PowerMockito.spy( threadCloseStockService);
        ThreadCloseStock threadCloseStock = mock(ThreadCloseStock.class);
        ThreadCloseStockDTO threadCloseStockDTO = new ThreadCloseStockDTO();
        List<ThreadCloseStockDTO> threadCloseStockDTOList = Lists.newArrayList(threadCloseStockDTO);
        List<ThreadCloseStock> threadCloseStockList = Lists.newArrayList(threadCloseStock);
        Mockito.when(mapper.toDtoBean(threadCloseStockList)).thenReturn(threadCloseStockDTOList);
        setFinalStatic(ThreadCloseStockServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findByFilter(filterRequestList);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method NumberActionDetailServiceImpl.create
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCreate_1() throws  Exception {
        ThreadCloseStockServiceImpl spyService = PowerMockito.spy( threadCloseStockService);
        ThreadCloseStock threadCloseStock = mock(ThreadCloseStock.class);
        ThreadCloseStockDTO threadCloseStockDTO = new ThreadCloseStockDTO();
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        Mockito.when(mapper.toPersistenceBean(threadCloseStockDTO)).thenReturn(threadCloseStock);
        Mockito.when(repository.save(threadCloseStock)).thenReturn(threadCloseStock);
        setFinalStatic(ThreadCloseStockServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.create(threadCloseStockDTO);
    }

    @Test(expected = Exception.class)
    public void testCreate_2() throws  Exception {
        ThreadCloseStockServiceImpl spyService = PowerMockito.spy( threadCloseStockService);
        ThreadCloseStockDTO threadCloseStockDTO = new ThreadCloseStockDTO();
        spyService.create(threadCloseStockDTO);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method ThreadCloseStockServiceImpl.update
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Test(expected = Exception.class)
    public void testUpdate_1() throws  Exception {
        threadCloseStockService.update(new ThreadCloseStockDTO());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method ThreadCloseStockServiceImpl.isExecute
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Test
    public void testIsExecute_1() throws  Exception {
        ThreadCloseStockDTO dto = new ThreadCloseStockDTO();
        dto.setStartHour("0");
        dto.setStartMinute("0");
        dto.setStartSecond("0");

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        threadCloseStockService.isExecute(dto);
    }

    @Test
    public void testIsExecute_2() throws  Exception {
        ThreadCloseStockDTO dto = new ThreadCloseStockDTO();
        Date currentDate = new Date();
        int hour = Integer.valueOf(DateUtil.dateToStringWithPattern(currentDate, "HH"));
        int minute = Integer.valueOf(DateUtil.dateToStringWithPattern(currentDate, "mm"));

        dto.setStartHour(String.valueOf(hour));
        dto.setStartMinute(String.valueOf(minute-1));
        dto.setStartSecond("0");

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        threadCloseStockService.isExecute(dto);
    }

    @Test
    public void testIsExecute_3() throws  Exception {
        ThreadCloseStockDTO dto = new ThreadCloseStockDTO();
        Date currentDate = new Date();
        int hour = Integer.valueOf(DateUtil.dateToStringWithPattern(currentDate, "HH"));
        int minute = Integer.valueOf(DateUtil.dateToStringWithPattern(currentDate, "mm"));
        int second = Integer.valueOf(DateUtil.dateToStringWithPattern(currentDate, "ss"));

        dto.setStartHour(String.valueOf(hour));
        dto.setStartMinute(String.valueOf(minute));
        dto.setStartSecond(String.valueOf(second));

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        threadCloseStockService.isExecute(dto);
    }

    @Test
    public void testIsExecute_4() throws  Exception {
        ThreadCloseStockDTO dto = new ThreadCloseStockDTO();
        Date currentDate = new Date();
        int hour = Integer.valueOf(DateUtil.dateToStringWithPattern(currentDate, "HH"));
        dto.setStartHour(String.valueOf(hour+1));

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        threadCloseStockService.isExecute(dto);
    }




    static void setFinalStatic(Field field, Object newValue, final Object targetObject) throws Exception {
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(targetObject, newValue);
    }
}