package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.dto.LockUserTypeDTO;
import com.viettel.bccs.inventory.model.LockUserType;
import com.viettel.bccs.inventory.repo.LockUserTypeRepo;
import com.viettel.fw.common.util.DataUtil;
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
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author DatLT
 * @date 10/10/2017
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({DataUtil.class, BaseServiceImpl.class, LockUserTypeService.class, DbUtil.class})
public class LockUserTypeServiceImplTest {

    @InjectMocks
    LockUserTypeServiceImpl lockUserTypeService ;
    @Mock
    private final BaseMapper<LockUserType, LockUserTypeDTO> mapper = new BaseMapper<>(LockUserType.class, LockUserTypeDTO.class);
    @Mock
    private LockUserTypeRepo repository;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method LockUserTypeServiceImpl.createNote
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCount_1() throws  Exception {
        List<FilterRequest> filters = Lists.newArrayList();
        Mockito.when(repository.count(any())).thenReturn(1L);
        Assert.assertEquals(1L, lockUserTypeService.count(filters).longValue());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method LockUserTypeServiceImpl.findOne
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindOne_1() throws  Exception {
        LockUserTypeServiceImpl spyService = PowerMockito.spy( lockUserTypeService);
        LockUserType lockUserType = mock(LockUserType.class);
        LockUserTypeDTO lockUserTypeDTO = new LockUserTypeDTO();
        Mockito.when(repository.findOne(anyLong())).thenReturn(lockUserType);
        setFinalStatic(LockUserTypeServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(mapper.toDtoBean(lockUserType)).thenReturn(lockUserTypeDTO);
        spyService.findOne(1L);
        Assert.assertNotNull(lockUserTypeDTO);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method LockUserTypeServiceImpl.findAll
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindAll_1() throws  Exception {
        LockUserTypeServiceImpl spyService = PowerMockito.spy( lockUserTypeService);
        LockUserType lockUserType = mock(LockUserType.class);
        LockUserTypeDTO lockUserTypeDTO = new LockUserTypeDTO();
        List<LockUserTypeDTO> lockUserTypeDTOList = Lists.newArrayList(lockUserTypeDTO);
        List<LockUserType> numberActionDetailList = Lists.newArrayList(lockUserType);
        Mockito.when(repository.findAll()).thenReturn(numberActionDetailList);
        Mockito.when(mapper.toDtoBean(numberActionDetailList)).thenReturn(lockUserTypeDTOList);
        setFinalStatic(LockUserTypeServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findAll();
        Assert.assertNotNull(lockUserTypeDTOList);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method LockUserTypeServiceImpl.findByFilter
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindByFilter_1() throws  Exception {
        FilterRequest filterRequest = new FilterRequest();
        List<FilterRequest> filterRequestList = Lists.newArrayList(filterRequest);
        LockUserTypeServiceImpl spyService = PowerMockito.spy( lockUserTypeService);
        LockUserType lockUserType = mock(LockUserType.class);
        LockUserTypeDTO lockUserTypeDTO = new LockUserTypeDTO();
        List<LockUserTypeDTO> lockUserTypeDTOList = Lists.newArrayList(lockUserTypeDTO);
        List<LockUserType> numberActionDetailList = Lists.newArrayList(lockUserType);
        Mockito.when(mapper.toDtoBean(numberActionDetailList)).thenReturn(lockUserTypeDTOList);
        setFinalStatic(LockUserTypeServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findByFilter(filterRequestList);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method LockUserTypeServiceImpl.create
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void testCreate_1() throws  Exception {
        lockUserTypeService.create(null);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method LockUserTypeServiceImpl.update
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void testUpdate_1() throws  Exception {
        lockUserTypeService.update(null);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method LockUserTypeServiceImpl.getAllUserLockType
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testGetAllUserLockType_1() throws  Exception {
        lockUserTypeService.getAllUserLockType();
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method LockUserTypeServiceImpl.getLockUserInfo
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testGetLockUserInfo_1() throws  Exception {
        lockUserTypeService.getLockUserInfo(1L);
    }

    static void setFinalStatic(Field field, Object newValue, final Object targetObject) throws Exception {
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(targetObject, newValue);
    }
}
