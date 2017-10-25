package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.dto.NumberActionDetailDTO;
import com.viettel.bccs.inventory.model.NumberActionDetail;
import com.viettel.bccs.inventory.repo.NumberActionDetailRepo;
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
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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
 * @date 07/10/2017
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({DbUtil.class, NumberActionDetailServiceImpl.class, BaseServiceImpl.class, NumberActionDetailService.class})
public class NumberActionDetailServiceImplTest {

    @InjectMocks
    NumberActionDetailServiceImpl numberActionDetailService;

    @Mock
    private NumberActionDetailRepo repository;

    @Mock
    private final BaseMapper<NumberActionDetail, NumberActionDetailDTO> mapper = new BaseMapper(NumberActionDetail.class, NumberActionDetailDTO.class);

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method NumberActionDetailServiceImpl.count
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCount_1() throws Exception {
        List<FilterRequest> filters = Lists.newArrayList();
        Mockito.when(repository.count(any())).thenReturn(1L);
        Assert.assertEquals(1L, numberActionDetailService.count(filters).longValue());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method NumberActionDetailServiceImpl.findOne
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindOne_1() throws  Exception {
        NumberActionDetailServiceImpl spyService = PowerMockito.spy( numberActionDetailService);
        NumberActionDetail numberActionDetail = mock(NumberActionDetail.class);
        NumberActionDetailDTO numberActionDetailDTO = new NumberActionDetailDTO();
        Mockito.when(repository.findOne(anyLong())).thenReturn(numberActionDetail);
        setFinalStatic(NumberActionDetailServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(mapper.toDtoBean(numberActionDetail)).thenReturn(numberActionDetailDTO);
        spyService.findOne(1L);
        Assert.assertNotNull(numberActionDetailDTO);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method NumberActionDetailServiceImpl.findAll
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindAll_1() throws  Exception {
        NumberActionDetailServiceImpl spyService = PowerMockito.spy( numberActionDetailService);
        NumberActionDetail numberActionDetail = mock(NumberActionDetail.class);
        NumberActionDetailDTO numberActionDetailDTO = new NumberActionDetailDTO();
        List<NumberActionDetailDTO> numberActionDetailDTOList = Lists.newArrayList(numberActionDetailDTO);
        List<NumberActionDetail> numberActionDetailList = Lists.newArrayList(numberActionDetail);
        Mockito.when(repository.findAll()).thenReturn(numberActionDetailList);
        Mockito.when(mapper.toDtoBean(numberActionDetailList)).thenReturn(numberActionDetailDTOList);
        setFinalStatic(NumberActionDetailServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findAll();
        Assert.assertNotNull(numberActionDetailDTOList);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method NumberActionDetailServiceImpl.findByFilter
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindByFilter_1() throws  Exception {
        FilterRequest filterRequest = new FilterRequest();
        List<FilterRequest> filterRequestList = Lists.newArrayList(filterRequest);
        NumberActionDetailServiceImpl spyService = PowerMockito.spy( numberActionDetailService);
        NumberActionDetail numberActionDetail = mock(NumberActionDetail.class);
        NumberActionDetailDTO numberActionDetailDTO = new NumberActionDetailDTO();
        List<NumberActionDetailDTO> numberActionDetailDTOList = Lists.newArrayList(numberActionDetailDTO);
        List<NumberActionDetail> numberActionDetailList = Lists.newArrayList(numberActionDetail);
        Mockito.when(mapper.toDtoBean(numberActionDetailList)).thenReturn(numberActionDetailDTOList);
        setFinalStatic(NumberActionDetailServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
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
        NumberActionDetail numberActionDetail = mock(NumberActionDetail.class);
        NumberActionDetailDTO numberActionDetailDTO = new NumberActionDetailDTO();
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        Mockito.when(mapper.toPersistenceBean(numberActionDetailDTO)).thenReturn(numberActionDetail);
        Mockito.when(repository.save(numberActionDetail)).thenReturn(numberActionDetail);
        NumberActionDetailServiceImpl spyService = PowerMockito.spy( numberActionDetailService);
        setFinalStatic(NumberActionDetailServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.create(numberActionDetailDTO);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method NumberActionDetailServiceImpl.update
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testUpdate_1() throws  Exception {
        expectedException.expect( NotImplementedException.class );
        NumberActionDetailDTO dto = new NumberActionDetailDTO();
        numberActionDetailService.update(dto);
    }

    static void setFinalStatic(Field field, Object newValue, final Object targetObject) throws Exception {
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(targetObject, newValue);
    }
}