package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.dto.NumberFilterRuleDTO;
import com.viettel.bccs.inventory.dto.StaffDTO;
import com.viettel.bccs.inventory.model.NumberFilterRule;
import com.viettel.bccs.inventory.repo.NumberFilterRuleRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.ErrorCode;
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
 * @date 04/10/2017
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({DbUtil.class, NumberFilterRuleServiceImpl.class, BaseServiceImpl.class, NumberFilterRuleService.class})
public class NumberFilterRuleServiceImplTest {

    @InjectMocks
    NumberFilterRuleServiceImpl numberFilterRuleService;

    @Mock
    private final BaseMapper<NumberFilterRule, NumberFilterRuleDTO> mapper = new BaseMapper(NumberFilterRule.class, NumberFilterRuleDTO.class);

    @Mock
    private EntityManager em;

    @Mock
    private NumberFilterRuleRepo repository;

    @Mock
    private StockNumberService stockNumberService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method NumberFilterRuleServiceImpl.count
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCount_1() throws  Exception {
        List<FilterRequest> filters = new ArrayList<>();
        when(repository.count(any())).thenReturn(1L);
        Assert.assertEquals(1L, numberFilterRuleService.count(filters).longValue());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method NumberFilterRuleServiceImpl.findOne
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindOne_1() throws  Exception {
        NumberFilterRuleServiceImpl spyService = PowerMockito.spy( numberFilterRuleService);
        NumberFilterRule numberFilterRule = mock(NumberFilterRule.class);
        NumberFilterRuleDTO numberFilterRuleDTO = new NumberFilterRuleDTO();
        Mockito.when(repository.findOne(anyLong())).thenReturn(numberFilterRule);
        setFinalStatic(NumberFilterRuleServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
         when(mapper.toDtoBean(numberFilterRule)).thenReturn(numberFilterRuleDTO);
        spyService.findOne(1L);
        Assert.assertNotNull(numberFilterRuleDTO);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method NumberFilterRuleServiceImpl.findAll
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindAll_1() throws  Exception {
        NumberFilterRuleServiceImpl spyService = PowerMockito.spy( numberFilterRuleService);
        NumberFilterRule numberFilterRule = mock(NumberFilterRule.class);
        NumberFilterRuleDTO numberFilterRuleDTO = new NumberFilterRuleDTO();
        List<NumberFilterRuleDTO> numberFilterRuleDTOList = Lists.newArrayList(numberFilterRuleDTO);
        List<NumberFilterRule> numberFilterRuleList = Lists.newArrayList(numberFilterRule);
        Mockito.when(repository.findAll()).thenReturn(numberFilterRuleList);
        Mockito.when(mapper.toDtoBean(numberFilterRuleList)).thenReturn(numberFilterRuleDTOList);
        setFinalStatic(NumberFilterRuleServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findAll();
        Assert.assertNotNull(numberFilterRuleDTOList);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method NumberFilterRuleServiceImpl.findByFilter
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindByFilter_1() throws  Exception {
        FilterRequest filterRequest = new FilterRequest();
        List<FilterRequest> filterRequestList = Lists.newArrayList(filterRequest);
        NumberFilterRuleDTO numberFilterRuleDTO = new NumberFilterRuleDTO();
        NumberFilterRule numberFilterRule = new NumberFilterRule();
        List<NumberFilterRule> numberFilterRuleList = Lists.newArrayList(numberFilterRule);
        List<NumberFilterRuleDTO> numberFilterRuleDTOList = Lists.newArrayList(numberFilterRuleDTO);
        Mockito.when(mapper.toDtoBean(numberFilterRuleList)).thenReturn(numberFilterRuleDTOList);
        NumberFilterRuleServiceImpl spyService = PowerMockito.spy( numberFilterRuleService);
        setFinalStatic(NumberFilterRuleServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findByFilter(filterRequestList);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method NumberFilterRuleServiceImpl.checkExistNameNumberFilter
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test(expected =  Exception.class)
    public void testCheckExistNameNumberFilter_1() throws  Exception {
        String name = "";
        Long filterId = 1L;
        numberFilterRuleService.checkExistNameNumberFilter(name,filterId);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method NumberFilterRuleServiceImpl.checkInsertConditionTelecomServiceId
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCheckInsertConditionTelecomServiceId_1() throws  Exception {
        NumberFilterRuleDTO numberFilterRuleDTO = new NumberFilterRuleDTO();
        Mockito.when(repository.checkInsertConditionTelecomServiceId(numberFilterRuleDTO)).thenReturn(1L);
        numberFilterRuleService.checkInsertConditionTelecomServiceId(numberFilterRuleDTO);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method NumberFilterRuleServiceImpl.checkEditConditionTelecomServiceId
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCheckEditConditionTelecomServiceId_1() throws  Exception {
        NumberFilterRuleDTO numberFilterRuleDTO = new NumberFilterRuleDTO();
        Mockito.when(repository.checkEditConditionTelecomServiceId(numberFilterRuleDTO)).thenReturn(2L);
        numberFilterRuleService.checkEditConditionTelecomServiceId(numberFilterRuleDTO);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method NumberFilterRuleServiceImpl.findRuleAggregate
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindRuleAggregate_1() throws  Exception {
        NumberFilterRuleDTO numberFilterRuleDTO = new NumberFilterRuleDTO();
        List<NumberFilterRuleDTO> numberFilterRuleDTOList = Lists.newArrayList(numberFilterRuleDTO);
        boolean extract = true;
        Mockito.when(repository.findRuleAggregate(numberFilterRuleDTO, extract)).thenReturn(numberFilterRuleDTOList);
        numberFilterRuleService.findRuleAggregate(numberFilterRuleDTO,extract);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method NumberFilterRuleServiceImpl.create
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void testCreate_1() throws  Exception {
        NumberFilterRuleDTO dto = null;
        StaffDTO staffDTO = new StaffDTO();
        numberFilterRuleService.create(dto,staffDTO);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method NumberFilterRuleServiceImpl.create
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCreate_2() throws  Exception {
        NumberFilterRuleDTO dto = new NumberFilterRuleDTO();
        NumberFilterRule numberFilterRule = new NumberFilterRule();
        StaffDTO staffDTO = new StaffDTO();
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        Mockito.when(mapper.toPersistenceBean(dto)).thenReturn(numberFilterRule);
        Mockito.when(repository.save(numberFilterRule)).thenReturn(numberFilterRule);
        NumberFilterRuleServiceImpl spyService = PowerMockito.spy( numberFilterRuleService);
        setFinalStatic(NumberFilterRuleServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.create(dto,staffDTO);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method NumberFilterRuleServiceImpl.update
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public  void testUpdate_1() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage(ErrorCode.ERROR_STANDARD.ERROR_UPDATE);

        NumberFilterRuleDTO dto = null;
        StaffDTO staffDTO = new StaffDTO();
        numberFilterRuleService.update(dto, staffDTO);
    }

    @Test
    public  void testUpdate_2() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage(ErrorCode.ERROR_STANDARD.ERROR_UPDATE);

        NumberFilterRule numberFilterRule = mock(NumberFilterRule.class);
        NumberFilterRuleDTO dto = new NumberFilterRuleDTO();
        StaffDTO staffDTO = new StaffDTO();
        NumberFilterRuleService spyService = PowerMockito.spy(numberFilterRuleService);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        Mockito.when(repository.findOne(anyLong())).thenReturn(numberFilterRule);
        setFinalStatic(NumberFilterRuleServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(mapper.toDtoBean(numberFilterRule)).thenReturn(dto);
        when(spyService.findOne(1L)).thenReturn(null);
        spyService.update(dto, staffDTO);
    }

    @Test
    public  void testUpdate_3() throws Exception {
        NumberFilterRule numberFilterRule = mock(NumberFilterRule.class);
        NumberFilterRuleDTO dto = new NumberFilterRuleDTO();
        StaffDTO staffDTO = new StaffDTO();
        NumberFilterRuleService spyService = PowerMockito.spy(numberFilterRuleService);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        Mockito.when(mapper.toPersistenceBean(dto)).thenReturn(numberFilterRule);
        Mockito.when(repository.save(numberFilterRule)).thenReturn(numberFilterRule);
        Mockito.when(repository.findOne(anyLong())).thenReturn(numberFilterRule);
        setFinalStatic(NumberFilterRuleServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(mapper.toDtoBean(numberFilterRule)).thenReturn(dto);
        when(spyService.findOne(1L)).thenReturn(dto);
        spyService.update(dto, staffDTO);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method NumberFilterRuleServiceImpl.deleteListNumberFiler
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testDeleteListNumberFiler_1() throws Exception{
        NumberFilterRuleDTO numberFilterRuleDTO = new NumberFilterRuleDTO();
        List<NumberFilterRuleDTO> lst = Lists.newArrayList(numberFilterRuleDTO);
        StaffDTO staffDTO = new StaffDTO();
        Mockito.when(stockNumberService.checkExistNumberByListFilterRuleId(any())).thenReturn(true);
        numberFilterRuleService.deleteListNumberFiler(lst, staffDTO);
    }

    @Test
    public void testDeleteListNumberFiler_2() throws Exception{
        NumberFilterRule numberFilterRule = new NumberFilterRule();
        NumberFilterRuleDTO numberFilterRuleDTO = new NumberFilterRuleDTO();
        List<NumberFilterRuleDTO> lst = Lists.newArrayList(numberFilterRuleDTO);
        StaffDTO staffDTO = new StaffDTO();
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        NumberFilterRuleService spyService = PowerMockito.spy(numberFilterRuleService);
        setFinalStatic(NumberFilterRuleServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        Mockito.when(mapper.toPersistenceBean(numberFilterRuleDTO)).thenReturn(numberFilterRule);
        Mockito.when(repository.save(numberFilterRule)).thenReturn(numberFilterRule);
        Mockito.when(stockNumberService.checkExistNumberByListFilterRuleId(any())).thenReturn(false);
        spyService.deleteListNumberFiler(lst, staffDTO);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method NumberFilterRuleServiceImpl.searchHightOrderRule
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testSearchHightOrderRule_1() throws  Exception {
        Long telecomServiceId = 1L;
        Long minNumber = 2L;
        NumberFilterRuleDTO numberFilterRuleDTO = new NumberFilterRuleDTO();
        List<NumberFilterRuleDTO> numberFilterRuleDTOList = Lists.newArrayList(numberFilterRuleDTO);
        Mockito.when(repository.searchHightOrderRule(telecomServiceId, minNumber)).thenReturn(numberFilterRuleDTOList);
        numberFilterRuleService.searchHightOrderRule(telecomServiceId, minNumber);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method NumberFilterRuleServiceImpl.getListNumerFilterRule
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void testGetListNumerFilterRule_1() throws  Exception{
        NumberFilterRuleDTO numberFilterRuleDTO = new NumberFilterRuleDTO();
        numberFilterRuleDTO.setGroupFilterRuleId(2L);
        numberFilterRuleService.getListNumerFilterRule(numberFilterRuleDTO);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method NumberFilterRuleServiceImpl.checkBeforeDelete
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCheckBeforeDelete_1() throws  Exception{
        Long groupFilterId = 1L;
        NumberFilterRuleDTO numberFilterRuleDTO = new NumberFilterRuleDTO();
        NumberFilterRule numberFilterRule = new NumberFilterRule();
        List<NumberFilterRule> numberFilterRuleList = Lists.newArrayList(numberFilterRule);
        List<FilterRequest> listRq = Lists.newArrayList();
        List<NumberFilterRuleDTO> numberFilterRuleDTOList = Lists.newArrayList(numberFilterRuleDTO);
        Mockito.when(mapper.toDtoBean(numberFilterRuleList)).thenReturn(numberFilterRuleDTOList);
        NumberFilterRuleServiceImpl spyService = PowerMockito.spy( numberFilterRuleService);
        setFinalStatic(NumberFilterRuleServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(spyService.findByFilter(listRq)).thenReturn(null);
        spyService.checkBeforeDelete(groupFilterId);
    }

    static void setFinalStatic(Field field, Object newValue, final Object targetObject) throws Exception {
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(targetObject, newValue);
    }
}