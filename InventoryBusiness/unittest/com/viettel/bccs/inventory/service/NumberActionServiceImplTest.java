package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.dto.NumberActionDTO;
import com.viettel.bccs.inventory.dto.SearchNumberRangeDTO;
import com.viettel.bccs.inventory.model.NumberAction;
import com.viettel.bccs.inventory.repo.NumberActionRepo;
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
 * @date 07/10/2017
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({DbUtil.class, NumberActionServiceImpl.class, BaseServiceImpl.class, NumberActionService.class})
public class NumberActionServiceImplTest {
    @InjectMocks
    NumberActionServiceImpl numberActionService;

    @Mock
    private final BaseMapper<NumberAction, NumberActionDTO> mapper = new BaseMapper(NumberAction.class, NumberActionDTO.class);

    @Mock
    private EntityManager em;

    @Mock
    private NumberActionRepo repository;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method NumberActionServiceImpl.count
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCount_1() throws  Exception{
        List<FilterRequest> filters = new ArrayList<>();
        when(repository.count(any())).thenReturn(1L);
        Assert.assertEquals(1L, numberActionService.count(filters).longValue());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method NumberActionServiceImpl.findOne
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindOne_1() throws  Exception {
        NumberActionServiceImpl spyService = PowerMockito.spy( numberActionService);
        NumberAction numberAction = mock(NumberAction.class);
        NumberActionDTO numberActionDTO = new NumberActionDTO();
        Mockito.when(repository.findOne(anyLong())).thenReturn(numberAction);
        setFinalStatic(NumberActionServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(mapper.toDtoBean(numberAction)).thenReturn(numberActionDTO);
        spyService.findOne(1L);
        Assert.assertNotNull(numberActionDTO);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method NumberActionServiceImpl.findAll
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindAll_1() throws  Exception {
        NumberActionServiceImpl spyService = PowerMockito.spy( numberActionService);
        NumberAction numberAction = mock(NumberAction.class);
        NumberActionDTO numberActionDTO = new NumberActionDTO();
        List<NumberActionDTO> numberActionDTOList = Lists.newArrayList(numberActionDTO);
        List<NumberAction> numberActionList = Lists.newArrayList(numberAction);
        Mockito.when(repository.findAll()).thenReturn(numberActionList);
        Mockito.when(mapper.toDtoBean(numberActionList)).thenReturn(numberActionDTOList);
        setFinalStatic(NumberActionServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findAll();
        Assert.assertNotNull(numberActionDTOList);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method NumberActionServiceImpl.findByFilter
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindByFilter_1() throws  Exception {
        FilterRequest filterRequest = new FilterRequest();
        List<FilterRequest> filterRequestList = Lists.newArrayList(filterRequest);
        NumberActionDTO numberActionDTO = new NumberActionDTO();
        NumberAction numberAction = mock(NumberAction.class);
        List<NumberAction> numberActionList = Lists.newArrayList(numberAction);
        List<NumberActionDTO> numberActionDTOList = Lists.newArrayList(numberActionDTO);
        Mockito.when(mapper.toDtoBean(numberActionList)).thenReturn(numberActionDTOList);
        NumberActionServiceImpl spyService = PowerMockito.spy( numberActionService);
        setFinalStatic(NumberActionServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findByFilter(filterRequestList);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method NumberActionServiceImpl.create
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCreate_1() throws  Exception{
        NumberActionDTO numberActionDTO = new NumberActionDTO();
        NumberAction numberAction = mock(NumberAction.class);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        Mockito.when(mapper.toPersistenceBean(numberActionDTO)).thenReturn(numberAction);
        Mockito.when(repository.save(numberAction)).thenReturn(numberAction);
        NumberActionServiceImpl spyService = PowerMockito.spy( numberActionService);
        setFinalStatic(NumberActionServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.create(numberActionDTO);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method NumberActionServiceImpl.update
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testUpdate_1() throws  Exception {
        NumberActionDTO numberActionDTO = new NumberActionDTO();
        NumberAction numberAction = mock(NumberAction.class);
        NumberActionServiceImpl spyService = PowerMockito.spy( numberActionService);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        Mockito.when(repository.findOne(anyLong())).thenReturn(numberAction);
        setFinalStatic(NumberActionServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(mapper.toDtoBean(numberAction)).thenReturn(numberActionDTO);
        when(spyService.findOne(1L)).thenReturn(null);
        spyService.update(numberActionDTO);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method NumberActionServiceImpl.checkOverlap
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCheckOverlap_1() throws  Exception {
        Long fromIsdn =1L;
        Long toIsdn=2L;
        String telecomServiceId = "telecomServiceId";
        Mockito.when(repository.checkOverlap(fromIsdn, toIsdn, telecomServiceId)).thenReturn(true);
        numberActionService.checkOverlap(fromIsdn, toIsdn, telecomServiceId);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method NumberActionServiceImpl.search
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testSearch_1() throws  Exception {
        NumberActionDTO numberActionDTO = new NumberActionDTO();
        List<NumberActionDTO> numberActionDTOList = Lists.newArrayList(numberActionDTO);
        SearchNumberRangeDTO searchDTO = new SearchNumberRangeDTO();
        Mockito.when(repository.search(searchDTO)).thenReturn(numberActionDTOList);
        numberActionService.search(searchDTO);
    }




























    static void setFinalStatic(Field field, Object newValue, final Object targetObject) throws Exception {
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(targetObject, newValue);
    }
}