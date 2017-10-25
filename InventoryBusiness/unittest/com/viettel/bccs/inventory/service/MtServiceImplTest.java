package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.dto.MtDTO;
import com.viettel.bccs.inventory.model.Mt;
import com.viettel.bccs.inventory.repo.MtRepo;
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
@PrepareForTest({DbUtil.class, MtServiceImpl.class, BaseServiceImpl.class, MtService.class})
public class MtServiceImplTest {
    @InjectMocks
    MtServiceImpl mtService;

    @Mock
    private final BaseMapper<Mt, MtDTO> mapper = new BaseMapper<>(Mt.class, MtDTO.class);
    @Mock
    private EntityManager em;
    @Mock
    private MtRepo repository;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method MtServiceImpl.count
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCount_1() throws  Exception {
        List<FilterRequest> filters = new ArrayList<>();
        when(repository.count(any())).thenReturn(1L);
        Assert.assertEquals(1L, mtService.count(filters).longValue());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method MtServiceImpl.findOne
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindOne_1() throws  Exception {
        MtServiceImpl spyService = PowerMockito.spy( mtService);
        Mt mt = mock(Mt.class);
        MtDTO mtDTO = new MtDTO();
        Mockito.when(repository.findOne(anyLong())).thenReturn(mt);
        setFinalStatic(MtServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(mapper.toDtoBean(mt)).thenReturn(mtDTO);
        spyService.findOne(1L);
        Assert.assertNotNull(mtDTO);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method MtServiceImpl.findAll
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindAll_1() throws  Exception {
        MtServiceImpl spyService = PowerMockito.spy( mtService);
        Mt mt = mock(Mt.class);
        MtDTO mtDTO = new MtDTO();
        List<MtDTO> mtDTOList = Lists.newArrayList(mtDTO);
        List<Mt> mtList = Lists.newArrayList(mt);
        Mockito.when(repository.findAll()).thenReturn(mtList);
        Mockito.when(mapper.toDtoBean(mtList)).thenReturn(mtDTOList);
        setFinalStatic(MtServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findAll();
        Assert.assertNotNull(mtDTOList);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method MtServiceImpl.findByFilter
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindByFilter_1() throws  Exception {
        FilterRequest filterRequest = new FilterRequest();
        List<FilterRequest> filterRequestList = Lists.newArrayList(filterRequest);
        MtServiceImpl spyService = PowerMockito.spy( mtService);
        Mt mt = mock(Mt.class);
        MtDTO mtDTO = new MtDTO();
        List<MtDTO> mtDTOList = Lists.newArrayList(mtDTO);
        List<Mt> mtList = Lists.newArrayList(mt);
        Mockito.when(mapper.toDtoBean(mtList)).thenReturn(mtDTOList);
        setFinalStatic(MtServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findByFilter(filterRequestList);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method MtServiceImpl.create
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCreate_1() throws  Exception {
        MtServiceImpl spyService = PowerMockito.spy( mtService);
        Mt mt = mock(Mt.class);
        MtDTO mtDTO = new MtDTO();
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        Mockito.when(mapper.toPersistenceBean(mtDTO)).thenReturn(mt);
        Mockito.when(repository.save(mt)).thenReturn(mt);
        setFinalStatic(MtServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.create(mtDTO);
    }

    @Test(expected = Exception.class)
    public void testCreate_2() throws  Exception {
        MtDTO mtDTO = new MtDTO();
        MtServiceImpl spyService = PowerMockito.spy( mtService);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.create(mtDTO);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method MtServiceImpl.update
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public  void testUpdate_1() throws Exception {
        MtServiceImpl spyService = PowerMockito.spy( mtService);
        MtDTO mtDTO = new MtDTO();
        Mt mt = mock(Mt.class);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        Mockito.when(mapper.toPersistenceBean(mtDTO)).thenReturn(mt);
        Mockito.when(repository.save(mt)).thenReturn(mt);
        setFinalStatic(MtServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.update(mtDTO);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method MtServiceImpl.saveSms
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testSaveSms_1() throws  Exception {
        String isdn = "id";
        String content = "content";
        MtServiceImpl spyService = PowerMockito.spy( mtService);
        MtDTO mtDTO = new MtDTO();
        Mt mt = mock(Mt.class);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        Mockito.when(mapper.toPersistenceBean(mtDTO)).thenReturn(mt);
        Mockito.when(repository.save(mt)).thenReturn(mt);
        setFinalStatic(MtServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.saveSms(isdn, content);
    }

    @Test(expected = Exception.class)
    public void testSaveSms_2() throws  Exception {
        String isdn = "id";
        String content = "content";
        MtServiceImpl spyService = PowerMockito.spy( mtService);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.saveSms(isdn, content);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method MtServiceImpl.delete
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testDelete_1() throws  Exception {
        Long mtID =2L;
        mtService.delete(mtID);
    }

    static void setFinalStatic(Field field, Object newValue, final Object targetObject) throws Exception {
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(targetObject, newValue);
    }



}