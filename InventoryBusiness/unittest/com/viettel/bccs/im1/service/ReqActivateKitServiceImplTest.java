package com.viettel.bccs.im1.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.im1.dto.ReqActivateKitDTO;
import com.viettel.bccs.im1.model.ReqActivateKit;
import com.viettel.bccs.im1.repo.ReqActivateKitRepo;
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
 * @date 21/10/2017
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({DbUtil.class, ReqActivateKitServiceImpl.class, BaseServiceImpl.class, ReqActivateKitService.class})
public class ReqActivateKitServiceImplTest {
    @InjectMocks
    ReqActivateKitServiceImpl reqActivateKitService;
    @Mock
    private final BaseMapper<ReqActivateKit, ReqActivateKitDTO> mapper = new BaseMapper<>(ReqActivateKit.class, ReqActivateKitDTO.class);
    @Mock
    private ReqActivateKitRepo repository;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }


    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method ReqActivateKitServiceImpl.count
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCount_1() throws  Exception {
        List<FilterRequest> filters = new ArrayList<>();
        when(repository.count(any())).thenReturn(1L);
        Assert.assertEquals(1L, reqActivateKitService.count(filters).longValue());
    }


    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method ReqActivateKitServiceImpl.findOne
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindOne_1() throws  Exception {
        ReqActivateKitServiceImpl spyService = PowerMockito.spy( reqActivateKitService);
        ReqActivateKit reqActivateKit = mock(ReqActivateKit.class);
        ReqActivateKitDTO reqActivateKitDTO = new ReqActivateKitDTO();
        Mockito.when(repository.findOne(anyLong())).thenReturn(reqActivateKit);
        setFinalStatic(ReqActivateKitServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(mapper.toDtoBean(reqActivateKit)).thenReturn(reqActivateKitDTO);
        spyService.findOne(1L);
        Assert.assertNotNull(reqActivateKitDTO);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method ReqActivateKitServiceImpl.findAll
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindAll_1() throws  Exception {
        ReqActivateKitServiceImpl spyService = PowerMockito.spy( reqActivateKitService);
        ReqActivateKit reqActivateKit = mock(ReqActivateKit.class);
        ReqActivateKitDTO reqActivateKitDTO = new ReqActivateKitDTO();
        List<ReqActivateKitDTO> reqActivateKitDTOList = Lists.newArrayList(reqActivateKitDTO);
        List<ReqActivateKit> reqActivateKitList = Lists.newArrayList(reqActivateKit);
        Mockito.when(repository.findAll()).thenReturn(reqActivateKitList);
        Mockito.when(mapper.toDtoBean(reqActivateKitList)).thenReturn(reqActivateKitDTOList);
        setFinalStatic(ReqActivateKitServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findAll();
        Assert.assertNotNull(reqActivateKitDTOList);
    }


    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method ReqActivateKitServiceImpl.findByFilter
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindByFilter_1() throws  Exception {
        FilterRequest filterRequest = new FilterRequest();
        List<FilterRequest> filterRequestList = Lists.newArrayList(filterRequest);
        ReqActivateKitServiceImpl spyService = PowerMockito.spy( reqActivateKitService);
        ReqActivateKit reqActivateKit = mock(ReqActivateKit.class);
        ReqActivateKitDTO reqActivateKitDTO = new ReqActivateKitDTO();
        List<ReqActivateKitDTO> reqActivateKitDTOList = Lists.newArrayList(reqActivateKitDTO);
        List<ReqActivateKit> reqActivateKitList = Lists.newArrayList(reqActivateKit);
        Mockito.when(mapper.toDtoBean(reqActivateKitList)).thenReturn(reqActivateKitDTOList);
        setFinalStatic(ReqActivateKitServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findByFilter(filterRequestList);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method ReqActivateKitServiceImpl.create
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCreate_1() throws  Exception {
        ReqActivateKitServiceImpl spyService = PowerMockito.spy( reqActivateKitService);
        ReqActivateKit reqActivateKit = mock(ReqActivateKit.class);
        ReqActivateKitDTO reqActivateKitDTO = new ReqActivateKitDTO();
        List<ReqActivateKitDTO> reqActivateKitDTOList = Lists.newArrayList(reqActivateKitDTO);
        List<ReqActivateKit> reqActivateKitList = Lists.newArrayList(reqActivateKit);
        setFinalStatic(ReqActivateKitServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);

        Mockito.when(mapper.toPersistenceBean(reqActivateKitDTO)).thenReturn(reqActivateKit);
        Mockito.when(mapper.toDtoBean(reqActivateKitList)).thenReturn(reqActivateKitDTOList);

        spyService.create(new ReqActivateKitDTO());
    }

    @Test(expected = Exception.class)
    public void testCreate_2() throws  Exception {
        ReqActivateKitServiceImpl spyService = PowerMockito.spy( reqActivateKitService);

        spyService.create(new ReqActivateKitDTO());
    }


    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method ReqActivateKitServiceImpl.update
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void testUpdate_1() throws  Exception {
        reqActivateKitService.update(new ReqActivateKitDTO());
    }


    static void setFinalStatic(Field field, Object newValue, final Object targetObject) throws Exception {
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(targetObject, newValue);
    }
}