package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.dto.SignFlowDetailDTO;
import com.viettel.bccs.inventory.dto.StaffDTO;
import com.viettel.bccs.inventory.dto.VofficeRoleDTO;
import com.viettel.bccs.inventory.model.VofficeRole;
import com.viettel.bccs.inventory.repo.VofficeRoleRepo;
import com.viettel.fw.Exception.LogicException;
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
import org.powermock.reflect.Whitebox;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author DatLT
 * @date 15/10/2017
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({DbUtil.class, VofficeRoleServiceImpl.class, BaseServiceImpl.class, VofficeRoleService.class, EntityManager.class})
public class VofficeRoleServiceImplTest {
    @InjectMocks
    VofficeRoleServiceImpl vofficeRoleService;
    @Mock
    private final BaseMapper<VofficeRole, VofficeRoleDTO> mapper = new BaseMapper(VofficeRole.class, VofficeRoleDTO.class);
    @Mock
    private EntityManager em;
    @Mock
    private SignFlowDetailService signFlowDetailService;
    @Mock
    private StaffService staffService;
    @Mock
    private StaffDTO staffDTO;
    @Mock
    private VofficeRoleRepo repository;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method VofficeRoleServiceImpl.count
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCount_1() throws Exception {
        List<FilterRequest> filters = Lists.newArrayList();
        Mockito.when(repository.count(any())).thenReturn(1L);
        Assert.assertEquals(1L, vofficeRoleService.count(filters).longValue());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method VofficeRoleServiceImpl.findOne
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindOne_1() throws  Exception {
        VofficeRoleServiceImpl spyService = PowerMockito.spy( vofficeRoleService);
        VofficeRole vofficeRole = mock(VofficeRole.class);
        VofficeRoleDTO vofficeRoleDTO = new VofficeRoleDTO();
        Mockito.when(repository.findOne(anyLong())).thenReturn(vofficeRole);
        setFinalStatic(VofficeRoleServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(mapper.toDtoBean(vofficeRole)).thenReturn(vofficeRoleDTO);
        spyService.findOne(1L);
        Assert.assertNotNull(vofficeRoleDTO);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method VofficeRoleServiceImpl.findAll
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Test
    public void testFindAll_1() throws  Exception {
        VofficeRoleServiceImpl spyService = PowerMockito.spy( vofficeRoleService);
        VofficeRole vofficeRole = mock(VofficeRole.class);
        VofficeRoleDTO vofficeRoleDTO = new VofficeRoleDTO();
        List<VofficeRoleDTO> vofficeRoleDTOList = Lists.newArrayList(vofficeRoleDTO);
        List<VofficeRole> vofficeRoleList = Lists.newArrayList(vofficeRole);
        Mockito.when(repository.findAll()).thenReturn(vofficeRoleList);
        Mockito.when(mapper.toDtoBean(vofficeRoleList)).thenReturn(vofficeRoleDTOList);
        setFinalStatic(VofficeRoleServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findAll();
        Assert.assertNotNull(vofficeRoleDTOList);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method VofficeRoleServiceImpl.findByFilter
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Test
    public void testFindByFilter_1() throws  Exception {
        FilterRequest filterRequest = new FilterRequest();
        List<FilterRequest> filterRequestList = Lists.newArrayList(filterRequest);
        VofficeRoleServiceImpl spyService = PowerMockito.spy( vofficeRoleService);
        VofficeRole vofficeRole = mock(VofficeRole.class);
        VofficeRoleDTO vofficeRoleDTO = new VofficeRoleDTO();
        List<VofficeRoleDTO> vofficeRoleDTOList = Lists.newArrayList(vofficeRoleDTO);
        List<VofficeRole> vofficeRoleList = Lists.newArrayList(vofficeRole);
        Mockito.when(mapper.toDtoBean(vofficeRoleList)).thenReturn(vofficeRoleDTOList);
        setFinalStatic(VofficeRoleServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findByFilter(filterRequestList);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method VofficeRoleServiceImpl.create
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Test(expected = Exception.class)
    public void testCreate_1() throws  Exception {
        vofficeRoleService.create(new VofficeRoleDTO());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method VofficeRoleServiceImpl.delete
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Test
    public void testDelete_1() throws  Exception {
        VofficeRoleServiceImpl spyService = PowerMockito.spy( vofficeRoleService);
        List<Long> lstId = null;
        String staffCode = "1";

        spyService.delete(lstId, staffCode);
    }

    @Test(expected = Exception.class)
    public void testDelete_2() throws  Exception {
        VofficeRoleServiceImpl spyService = PowerMockito.spy( vofficeRoleService);
        VofficeRole vofficeRole = mock(VofficeRole.class);
        VofficeRoleDTO vofficeRoleDTO = mock(VofficeRoleDTO.class);
        List<VofficeRoleDTO> vofficeRoleDTOList = Lists.newArrayList(vofficeRoleDTO);
        List<VofficeRole> vofficeRoleList = Lists.newArrayList(vofficeRole);
        List<Long> lstId = Lists.newArrayList();
        lstId.add(1L);
        String staffCode = "1";

        Mockito.when(repository.findOne(anyLong())).thenReturn(vofficeRole);
        setFinalStatic(VofficeRoleServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        Mockito.when(mapper.toDtoBean(vofficeRoleList)).thenReturn(vofficeRoleDTOList);
        when(spyService.findOne(anyLong())).thenReturn(null);
        spyService.delete(lstId, staffCode);
    }

    @Test(expected = Exception.class)
    public void testDelete_3() throws  Exception {
        VofficeRoleServiceImpl spyService = PowerMockito.spy( vofficeRoleService);
        VofficeRole vofficeRole = mock(VofficeRole.class);
        VofficeRoleDTO vofficeRoleDTO = mock(VofficeRoleDTO.class);
        List<VofficeRoleDTO> vofficeRoleDTOList = Lists.newArrayList(vofficeRoleDTO);
        List<VofficeRole> vofficeRoleList = Lists.newArrayList(vofficeRole);
        List<Long> lstId = Lists.newArrayList();
        lstId.add(1L);
        String staffCode = "1";

        Mockito.when(repository.findOne(anyLong())).thenReturn(vofficeRole);
        setFinalStatic(VofficeRoleServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        Mockito.when(mapper.toDtoBean(vofficeRoleList)).thenReturn(vofficeRoleDTOList);
        when(spyService.findOne(anyLong())).thenReturn(vofficeRoleDTO);
        PowerMockito.doReturn(true).when(spyService, "isExistSignFlowDetail" ,anyLong());
        spyService.delete(lstId, staffCode);
    }

    @Test
    public void testDelete_4() throws  Exception {
        VofficeRoleServiceImpl spyService = PowerMockito.spy( vofficeRoleService);
        VofficeRole vofficeRole = mock(VofficeRole.class);
        VofficeRoleDTO vofficeRoleDTO = mock(VofficeRoleDTO.class);
        List<VofficeRoleDTO> vofficeRoleDTOList = Lists.newArrayList(vofficeRoleDTO);
        List<VofficeRole> vofficeRoleList = Lists.newArrayList(vofficeRole);
        List<Long> lstId = Lists.newArrayList();
        lstId.add(1L);
        String staffCode = "1";

        Mockito.when(repository.findOne(anyLong())).thenReturn(vofficeRole);
        setFinalStatic(VofficeRoleServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        Mockito.when(mapper.toDtoBean(vofficeRoleList)).thenReturn(vofficeRoleDTOList);
        Mockito.when(mapper.toPersistenceBean(vofficeRoleDTO)).thenReturn(vofficeRole);
        when(spyService.findOne(anyLong())).thenReturn(vofficeRoleDTO);
        PowerMockito.doReturn(false).when(spyService, "isExistSignFlowDetail" ,anyLong());
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.delete(lstId, staffCode);
    }

    @Test(expected = Exception.class)
    public void testDelete_5() throws  Exception {
        VofficeRoleServiceImpl spyService = PowerMockito.spy( vofficeRoleService);
        VofficeRole vofficeRole = mock(VofficeRole.class);
        VofficeRoleDTO vofficeRoleDTO = mock(VofficeRoleDTO.class);
        List<VofficeRoleDTO> vofficeRoleDTOList = Lists.newArrayList(vofficeRoleDTO);
        List<VofficeRole> vofficeRoleList = Lists.newArrayList(vofficeRole);
        List<Long> lstId = Lists.newArrayList();
        lstId.add(1L);
        String staffCode = "1";

        Mockito.when(repository.findOne(anyLong())).thenReturn(vofficeRole);
        setFinalStatic(VofficeRoleServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        Mockito.when(mapper.toDtoBean(vofficeRoleList)).thenReturn(vofficeRoleDTOList);
        Mockito.when(mapper.toPersistenceBean(vofficeRoleDTO)).thenReturn(vofficeRole);
        when(spyService.findOne(anyLong())).thenReturn(vofficeRoleDTO);
        PowerMockito.doReturn(false).when(spyService, "isExistSignFlowDetail" ,anyLong());
        spyService.delete(lstId, staffCode);
    }

    @Test(expected = Exception.class)
    public void testDelete_6() throws  Exception {
        VofficeRoleServiceImpl spyService = PowerMockito.spy( vofficeRoleService);
        VofficeRole vofficeRole = mock(VofficeRole.class);
        VofficeRoleDTO vofficeRoleDTO = mock(VofficeRoleDTO.class);
        List<VofficeRoleDTO> vofficeRoleDTOList = Lists.newArrayList(vofficeRoleDTO);
        List<VofficeRole> vofficeRoleList = Lists.newArrayList(vofficeRole);
        List<Long> lstId = Lists.newArrayList();
        lstId.add(1L);
        String staffCode = "1";

        Mockito.when(repository.findOne(anyLong())).thenReturn(vofficeRole);
        setFinalStatic(VofficeRoleServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        Mockito.when(mapper.toDtoBean(vofficeRoleList)).thenReturn(vofficeRoleDTOList);
        Mockito.when(mapper.toPersistenceBean(vofficeRoleDTO)).thenReturn(vofficeRole);
        when(spyService.findOne(anyLong())).thenReturn(vofficeRoleDTO);
        PowerMockito.doReturn(false).when(spyService, "isExistSignFlowDetail" ,anyLong());
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenThrow(new LogicException());
        spyService.delete(lstId, staffCode);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method VofficeRoleServiceImpl.isExistSignFlowDetail
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Test
    public void testIsExistSignFlowDetail_1() throws  Exception {
        VofficeRoleServiceImpl spyService = PowerMockito.spy( vofficeRoleService);
        when(signFlowDetailService.findByVofficeRoleId(anyLong())).thenReturn(null);
        Whitebox.invokeMethod(spyService, "isExistSignFlowDetail", 1L);
    }

    @Test
    public void testIsExistSignFlowDetail_2() throws  Exception {
        VofficeRoleServiceImpl spyService = PowerMockito.spy( vofficeRoleService);
        SignFlowDetailDTO signFlowDetailDTO = new SignFlowDetailDTO();
        List<SignFlowDetailDTO> lstSignFlowDetai = Lists.newArrayList(signFlowDetailDTO);
        when(signFlowDetailService.findByVofficeRoleId(anyLong())).thenReturn(lstSignFlowDetai);
        Whitebox.invokeMethod(spyService, "isExistSignFlowDetail", 1L);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method VofficeRoleServiceImpl.search
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Test
    public void testSearch_1() throws  Exception {
        VofficeRoleServiceImpl spyService = PowerMockito.spy( vofficeRoleService);
        VofficeRoleDTO vofficeRoleDTO = mock(VofficeRoleDTO.class);
        VofficeRole vofficeRole = mock(VofficeRole.class);
        List<VofficeRoleDTO> vofficeRoleDTOList = Lists.newArrayList(vofficeRoleDTO);
        List<VofficeRole> vofficeRoleList = Lists.newArrayList(vofficeRole);
        boolean flag = true;
        Query queryObject = PowerMockito.mock(Query.class);
        List<VofficeRole> list = Lists.newArrayList(vofficeRole);

        setFinalStatic(VofficeRoleServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        Mockito.when(mapper.toDtoBean(vofficeRoleList)).thenReturn(vofficeRoleDTOList);
        when(vofficeRoleDTO.getRoleCode()).thenReturn("1");
        when(vofficeRoleDTO.getRoleName()).thenReturn("1");

        when(em.createNativeQuery(anyString(), any(Class.class))).thenReturn(queryObject);
        when(queryObject.getResultList()).thenReturn(list);
        spyService.search(vofficeRoleDTO, flag);
    }

    @Test
    public void testSearch_2() throws  Exception {
        VofficeRoleServiceImpl spyService = PowerMockito.spy( vofficeRoleService);
        VofficeRoleDTO vofficeRoleDTO = mock(VofficeRoleDTO.class);
        VofficeRole vofficeRole = mock(VofficeRole.class);
        List<VofficeRoleDTO> vofficeRoleDTOList = Lists.newArrayList(vofficeRoleDTO);
        List<VofficeRole> vofficeRoleList = Lists.newArrayList(vofficeRole);
        boolean flag = false;
        Query queryObject = PowerMockito.mock(Query.class);
        List<VofficeRole> list = Lists.newArrayList(vofficeRole);

        setFinalStatic(VofficeRoleServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        Mockito.when(mapper.toDtoBean(vofficeRoleList)).thenReturn(vofficeRoleDTOList);
        when(vofficeRoleDTO.getRoleCode()).thenReturn("1");
        when(vofficeRoleDTO.getRoleName()).thenReturn("1");

        when(em.createNativeQuery(anyString(), any(Class.class))).thenReturn(queryObject);
        when(queryObject.getResultList()).thenReturn(list);
        spyService.search(vofficeRoleDTO, flag);
    }

    @Test(expected = Exception.class)
    public void testSearch_3() throws  Exception {
        VofficeRoleServiceImpl spyService = PowerMockito.spy( vofficeRoleService);
        VofficeRoleDTO vofficeRoleDTO = mock(VofficeRoleDTO.class);
        VofficeRole vofficeRole = mock(VofficeRole.class);
        List<VofficeRoleDTO> vofficeRoleDTOList = Lists.newArrayList(vofficeRoleDTO);
        List<VofficeRole> vofficeRoleList = Lists.newArrayList(vofficeRole);
        boolean flag = false;
        Query queryObject = PowerMockito.mock(Query.class);
        List<VofficeRole> list = Lists.newArrayList(vofficeRole);

        when(vofficeRoleDTO.getRoleCode()).thenReturn("1");
        when(vofficeRoleDTO.getRoleName()).thenReturn("1");

        when(em.createNativeQuery(anyString(), any(Class.class))).thenReturn(queryObject);
        when(queryObject.getResultList()).thenReturn(list);
        spyService.search(vofficeRoleDTO, flag);
    }

    @Test
    public void testSearch_4() throws  Exception {
        VofficeRoleServiceImpl spyService = PowerMockito.spy( vofficeRoleService);
        VofficeRoleDTO vofficeRoleDTO = mock(VofficeRoleDTO.class);
        VofficeRole vofficeRole = mock(VofficeRole.class);
        List<VofficeRoleDTO> vofficeRoleDTOList = Lists.newArrayList(vofficeRoleDTO);
        List<VofficeRole> vofficeRoleList = Lists.newArrayList(vofficeRole);
        boolean flag = true;
        Query queryObject = PowerMockito.mock(Query.class);
        List<VofficeRole> list = Lists.newArrayList(vofficeRole);

        setFinalStatic(VofficeRoleServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        Mockito.when(mapper.toDtoBean(vofficeRoleList)).thenReturn(vofficeRoleDTOList);
        when(vofficeRoleDTO.getRoleCode()).thenReturn("");
        when(vofficeRoleDTO.getRoleName()).thenReturn("");

        when(em.createNativeQuery(anyString(), any(Class.class))).thenReturn(queryObject);
        when(queryObject.getResultList()).thenReturn(list);
        spyService.search(vofficeRoleDTO, flag);
    }

    @Test
    public void testSearch_5() throws  Exception {
        VofficeRoleServiceImpl spyService = PowerMockito.spy( vofficeRoleService);
        VofficeRoleDTO vofficeRoleDTO = mock(VofficeRoleDTO.class);
        VofficeRole vofficeRole = mock(VofficeRole.class);
        List<VofficeRoleDTO> vofficeRoleDTOList = Lists.newArrayList(vofficeRoleDTO);
        List<VofficeRole> vofficeRoleList = Lists.newArrayList(vofficeRole);
        boolean flag = true;
        Query queryObject = PowerMockito.mock(Query.class);
        List<VofficeRole> list = Lists.newArrayList(vofficeRole);

        setFinalStatic(VofficeRoleServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        Mockito.when(mapper.toDtoBean(vofficeRoleList)).thenReturn(vofficeRoleDTOList);
        when(vofficeRoleDTO.getRoleCode()).thenReturn("1");
        when(vofficeRoleDTO.getRoleName()).thenReturn("1");

        when(em.createNativeQuery(anyString(), any(Class.class))).thenReturn(null);
        when(queryObject.getResultList()).thenReturn(list);
        spyService.search(vofficeRoleDTO, flag);
    }

    @Test
    public void testSearch_6() throws  Exception {
        VofficeRoleServiceImpl spyService = PowerMockito.spy( vofficeRoleService);
        VofficeRoleDTO vofficeRoleDTO = mock(VofficeRoleDTO.class);
        VofficeRole vofficeRole = mock(VofficeRole.class);
        List<VofficeRoleDTO> vofficeRoleDTOList = Lists.newArrayList(vofficeRoleDTO);
        List<VofficeRole> vofficeRoleList = Lists.newArrayList(vofficeRole);
        boolean flag = true;
        Query queryObject = PowerMockito.mock(Query.class);
        List<VofficeRole> list = Lists.newArrayList(vofficeRole);

        setFinalStatic(VofficeRoleServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        Mockito.when(mapper.toDtoBean(vofficeRoleList)).thenReturn(vofficeRoleDTOList);
        when(vofficeRoleDTO.getRoleCode()).thenReturn("1");
        when(vofficeRoleDTO.getRoleName()).thenReturn("1");

        when(em.createNativeQuery(anyString(), any(Class.class))).thenReturn(queryObject);
        when(queryObject.getResultList()).thenReturn(null);
        spyService.search(vofficeRoleDTO, flag);
    }
    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method VofficeRoleServiceImpl.createNewVofficeRole
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Test(expected = Exception.class)
    public void testCreateNewVofficeRole_1() throws  Exception {
        VofficeRoleServiceImpl spyService = PowerMockito.spy( vofficeRoleService);
        VofficeRoleDTO dto= new VofficeRoleDTO();
        dto.setRoleCode("1");
        dto.setRoleName("NAME_REGEX");
        String staffCode ="1";

        PowerMockito.doReturn(true).when(spyService, "isDulicateVofficeRole", dto, dto.getVofficeRoleId(), true);
        PowerMockito.doNothing().when(spyService, "checkValidate", dto, staffCode, true);
        when(staffService.getStaffByStaffCode(staffCode)).thenReturn(new StaffDTO());
        spyService.createNewVofficeRole(dto, staffCode);
    }

    @Test(expected = Exception.class)
    public void testCreateNewVofficeRole_2() throws  Exception {
        VofficeRoleServiceImpl spyService = PowerMockito.spy( vofficeRoleService);
        VofficeRoleDTO dto= new VofficeRoleDTO();
        dto.setRoleCode("1");
        dto.setRoleName("NAME_REGEX");
        String staffCode ="1";

        PowerMockito.doReturn(false).when(spyService, "isDulicateVofficeRole", dto, dto.getVofficeRoleId(), true);
        PowerMockito.doReturn(true).when(spyService, "isDulicateVofficeRole", dto, dto.getVofficeRoleId(), false);
        PowerMockito.doNothing().when(spyService, "checkValidate", dto, staffCode, true);
        when(staffService.getStaffByStaffCode(staffCode)).thenReturn(new StaffDTO());
        spyService.createNewVofficeRole(dto, staffCode);
    }

    @Test(expected = Exception.class)
    public void testCreateNewVofficeRole_3() throws  Exception {
        VofficeRoleServiceImpl spyService = PowerMockito.spy( vofficeRoleService);
        VofficeRoleDTO dto= new VofficeRoleDTO();
        dto.setRoleCode("1");
        dto.setRoleName("NAME_REGEX");
        String staffCode ="1";

        PowerMockito.doReturn(false).when(spyService, "isDulicateVofficeRole", dto, dto.getVofficeRoleId(), true);
        PowerMockito.doReturn(false).when(spyService, "isDulicateVofficeRole", dto, dto.getVofficeRoleId(), false);
        PowerMockito.doNothing().when(spyService, "checkValidate", dto, staffCode, true);
        when(staffService.getStaffByStaffCode(staffCode)).thenReturn(new StaffDTO());
        spyService.createNewVofficeRole(dto, staffCode);
    }

    @Test
    public void testCreateNewVofficeRole_4() throws  Exception {
        VofficeRoleServiceImpl spyService = PowerMockito.spy( vofficeRoleService);
        VofficeRoleDTO dto= new VofficeRoleDTO();
        VofficeRole vofficeRole = new VofficeRole();
        dto.setRoleCode("1");
        dto.setRoleName("NAME_REGEX");
        String staffCode ="1";

        PowerMockito.doReturn(false).when(spyService, "isDulicateVofficeRole", dto, dto.getVofficeRoleId(), true);
        PowerMockito.doReturn(false).when(spyService, "isDulicateVofficeRole", dto, dto.getVofficeRoleId(), false);
        PowerMockito.doNothing().when(spyService, "checkValidate", dto, staffCode, true);
        when(staffService.getStaffByStaffCode(staffCode)).thenReturn(new StaffDTO());
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        Mockito.when(mapper.toPersistenceBean(dto)).thenReturn(vofficeRole);
        setFinalStatic(VofficeRoleServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.createNewVofficeRole(dto, staffCode);
    }

    @Test(expected = Exception.class)
    public void testCreateNewVofficeRole_5() throws  Exception {
        VofficeRoleServiceImpl spyService = PowerMockito.spy( vofficeRoleService);
        VofficeRoleDTO dto= new VofficeRoleDTO();
        dto.setRoleCode("");
        dto.setRoleName("NAME_REGEX");
        String staffCode ="";

        PowerMockito.doReturn(false).when(spyService, "isDulicateVofficeRole", dto, dto.getVofficeRoleId(), true);
        PowerMockito.doReturn(true).when(spyService, "isDulicateVofficeRole", dto, dto.getVofficeRoleId(), false);
        PowerMockito.doNothing().when(spyService, "checkValidate", dto, staffCode, true);
        when(staffService.getStaffByStaffCode(staffCode)).thenReturn(new StaffDTO());
        spyService.createNewVofficeRole(dto, staffCode);
    }
    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method VofficeRoleServiceImpl.update
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Test(expected = Exception.class)
    public void testUpdate_1() throws  Exception {
        VofficeRoleServiceImpl spyService = PowerMockito.spy( vofficeRoleService);
        VofficeRoleDTO dto= new VofficeRoleDTO();
        dto.setRoleCode("1");
        dto.setRoleName("NAME_REGEX");
        String staffCode ="1";

        PowerMockito.doReturn(true).when(spyService, "isDulicateVofficeRole", dto, dto.getVofficeRoleId(), true);
        PowerMockito.doNothing().when(spyService, "checkValidate", dto, staffCode, true);
        when(staffService.getStaffByStaffCode(staffCode)).thenReturn(new StaffDTO());
        spyService.update(dto, staffCode);
    }

    @Test(expected = Exception.class)
    public void testUpdate_2() throws  Exception {
        VofficeRoleServiceImpl spyService = PowerMockito.spy( vofficeRoleService);
        VofficeRoleDTO dto= new VofficeRoleDTO();
        dto.setRoleCode("1");
        dto.setRoleName("NAME_REGEX");
        String staffCode ="1";

        PowerMockito.doReturn(false).when(spyService, "isDulicateVofficeRole", dto, dto.getVofficeRoleId(), true);
        PowerMockito.doReturn(true).when(spyService, "isDulicateVofficeRole", dto, dto.getVofficeRoleId(), false);
        PowerMockito.doNothing().when(spyService, "checkValidate", dto, staffCode, true);
        when(staffService.getStaffByStaffCode(staffCode)).thenReturn(new StaffDTO());
        spyService.update(dto, staffCode);
    }

    @Test(expected = Exception.class)
    public void testUpdate_3() throws  Exception {
        VofficeRoleServiceImpl spyService = PowerMockito.spy( vofficeRoleService);
        VofficeRoleDTO dto= new VofficeRoleDTO();
        dto.setRoleCode("1");
        dto.setRoleName("NAME_REGEX");
        String staffCode ="1";
        VofficeRole vofficeRole = mock(VofficeRole.class);
        VofficeRoleDTO vofficeRoleDTO = new VofficeRoleDTO();

        PowerMockito.doReturn(false).when(spyService, "isDulicateVofficeRole", dto, dto.getVofficeRoleId(), true);
        PowerMockito.doReturn(false).when(spyService, "isDulicateVofficeRole", dto, dto.getVofficeRoleId(), false);
        PowerMockito.doNothing().when(spyService, "checkValidate", dto, staffCode, true);
        when(staffService.getStaffByStaffCode(staffCode)).thenReturn(new StaffDTO());
        Mockito.when(repository.findOne(anyLong())).thenReturn(vofficeRole);
        setFinalStatic(VofficeRoleServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(mapper.toDtoBean(vofficeRole)).thenReturn(null);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.update(dto, staffCode);
    }

    @Test
    public void testUpdate_4() throws  Exception {
        VofficeRoleServiceImpl spyService = PowerMockito.spy( vofficeRoleService);
        VofficeRoleDTO dto= new VofficeRoleDTO();
        dto.setRoleCode("1");
        dto.setRoleName("NAME_REGEX");
        String staffCode ="1";
        VofficeRole vofficeRole = mock(VofficeRole.class);
        VofficeRoleDTO vofficeRoleDTO = new VofficeRoleDTO();

        PowerMockito.doReturn(false).when(spyService, "isDulicateVofficeRole", dto, dto.getVofficeRoleId(), true);
        PowerMockito.doReturn(false).when(spyService, "isDulicateVofficeRole", dto, dto.getVofficeRoleId(), false);
        PowerMockito.doNothing().when(spyService, "checkValidate", dto, staffCode, true);
        when(staffService.getStaffByStaffCode(staffCode)).thenReturn(new StaffDTO());
        Mockito.when(repository.findOne(anyLong())).thenReturn(vofficeRole);
        setFinalStatic(VofficeRoleServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(mapper.toDtoBean(vofficeRole)).thenReturn(vofficeRoleDTO);
        Mockito.when(mapper.toPersistenceBean(dto)).thenReturn(vofficeRole);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.update(dto, staffCode);
    }

    @Test(expected = Exception.class)
    public void testUpdate_5() throws  Exception {
        VofficeRoleServiceImpl spyService = PowerMockito.spy( vofficeRoleService);
        VofficeRoleDTO dto= new VofficeRoleDTO();
        dto.setRoleCode("1");
        dto.setRoleName("NAME_REGEX");
        String staffCode ="1";
        VofficeRole vofficeRole = mock(VofficeRole.class);
        VofficeRoleDTO vofficeRoleDTO = new VofficeRoleDTO();

        PowerMockito.doReturn(false).when(spyService, "isDulicateVofficeRole", dto, dto.getVofficeRoleId(), true);
        PowerMockito.doReturn(false).when(spyService, "isDulicateVofficeRole", dto, dto.getVofficeRoleId(), false);
        PowerMockito.doNothing().when(spyService, "checkValidate", dto, staffCode, true);
        when(staffService.getStaffByStaffCode(staffCode)).thenReturn(new StaffDTO());
        Mockito.when(repository.findOne(anyLong())).thenReturn(vofficeRole);
        setFinalStatic(VofficeRoleServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(mapper.toDtoBean(vofficeRole)).thenReturn(vofficeRoleDTO);
        Mockito.when(mapper.toPersistenceBean(dto)).thenReturn(vofficeRole);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenThrow(new Exception());
        spyService.update(dto, staffCode);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method VofficeRoleServiceImpl.checkValidate
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Test(expected = Exception.class)
    public void testCheckValidate_1() throws  Exception {
        VofficeRoleServiceImpl spyService = PowerMockito.spy( vofficeRoleService);
        VofficeRoleDTO dto = null;
        String staffCode = "1";
        boolean isUpdate = true;

        Whitebox.invokeMethod(spyService, "checkValidate", dto, staffCode, isUpdate);
    }

    @Test(expected = Exception.class)
    public void testCheckValidate_2() throws  Exception {
        VofficeRoleServiceImpl spyService = PowerMockito.spy( vofficeRoleService);
        VofficeRoleDTO dto = new VofficeRoleDTO();
        String staffCode = "1";
        boolean isUpdate = true;

        Whitebox.invokeMethod(spyService, "checkValidate", dto, staffCode, isUpdate);
    }

    @Test(expected = Exception.class)
    public void testCheckValidate_3() throws  Exception {
        VofficeRoleServiceImpl spyService = PowerMockito.spy( vofficeRoleService);
        VofficeRoleDTO dto = new VofficeRoleDTO();
        dto.setVofficeRoleId(1L);
        String staffCode = "1";
        boolean isUpdate = true;

        Whitebox.invokeMethod(spyService, "checkValidate", dto, staffCode, isUpdate);
    }

    @Test(expected = Exception.class)
    public void testCheckValidate_4() throws  Exception {
        VofficeRoleServiceImpl spyService = PowerMockito.spy( vofficeRoleService);
        VofficeRoleDTO dto = new VofficeRoleDTO();
        dto.setVofficeRoleId(1L);
        dto.setRoleCode("U");
        String staffCode = "1";
        boolean isUpdate = true;

        Whitebox.invokeMethod(spyService, "checkValidate", dto, staffCode, isUpdate);
    }

    @Test(expected = Exception.class)
    public void testCheckValidate_5() throws  Exception {
        VofficeRoleServiceImpl spyService = PowerMockito.spy( vofficeRoleService);
        VofficeRoleDTO dto = new VofficeRoleDTO();
        dto.setVofficeRoleId(1L);
        dto.setRoleCode("U");
        dto.setRoleName("U");
        String staffCode = "1";
        boolean isUpdate = true;

        Whitebox.invokeMethod(spyService, "checkValidate", dto, staffCode, isUpdate);
    }

    @Test(expected = Exception.class)
    public void testCheckValidate_6() throws  Exception {
        VofficeRoleServiceImpl spyService = PowerMockito.spy( vofficeRoleService);
        VofficeRoleDTO dto = new VofficeRoleDTO();
        dto.setVofficeRoleId(1L);
        dto.setRoleCode("!=");
        dto.setRoleName("U");
        String staffCode = "1";
        boolean isUpdate = true;

        Whitebox.invokeMethod(spyService, "checkValidate", dto, staffCode, isUpdate);
    }

    @Test(expected = Exception.class)
    public void testCheckValidate_7() throws  Exception {
        VofficeRoleServiceImpl spyService = PowerMockito.spy( vofficeRoleService);
        VofficeRoleDTO dto = new VofficeRoleDTO();
        dto.setVofficeRoleId(1L);
        dto.setRoleCode("G");
        dto.setRoleName("NAME_REGEX");
        String staffCode = "1";
        boolean isUpdate = true;
        StaffDTO staffDTO = new StaffDTO() ;

        when(staffService.getStaffByStaffCode(any())).thenReturn(null);
        Whitebox.invokeMethod(spyService, "checkValidate", dto, staffCode, isUpdate);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method VofficeRoleServiceImpl.isDulicateVofficeRole
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Test
    public void testIsDulicateVofficeRole_1() throws  Exception {
        VofficeRoleDTO dto= new VofficeRoleDTO();
        Long vofficeRoleId = 1L;
        boolean flag = true;

        VofficeRoleServiceImpl spyService = PowerMockito.spy( vofficeRoleService);
        Whitebox.invokeMethod(spyService, "isDulicateVofficeRole", dto, vofficeRoleId, flag);
    }

    @Test
    public void testIsDulicateVofficeRole_2() throws  Exception {
        VofficeRoleDTO dto= new VofficeRoleDTO();
        Long vofficeRoleId = 1L;
        boolean flag = false;

        VofficeRoleServiceImpl spyService = PowerMockito.spy( vofficeRoleService);
        Whitebox.invokeMethod(spyService, "isDulicateVofficeRole", dto, vofficeRoleId, flag);
    }


    @Test(expected = Exception.class)
    public void testIsDulicateVofficeRole_3() throws  Exception {
        VofficeRoleDTO dto= null;
        Long vofficeRoleId = 1L;
        boolean flag = false;

        when(repository.count(any())).thenReturn(1L);
        VofficeRoleServiceImpl spyService = PowerMockito.spy( vofficeRoleService);
        Whitebox.invokeMethod(spyService, "isDulicateVofficeRole", dto, vofficeRoleId, flag);
    }

    @Test
    public void testIsDulicateVofficeRole_4() throws  Exception {
        VofficeRoleDTO dto= new VofficeRoleDTO();
        Long vofficeRoleId = null;
        boolean flag = false;

        when(repository.count(any())).thenReturn(-1L);
        VofficeRoleServiceImpl spyService = PowerMockito.spy( vofficeRoleService);
        Whitebox.invokeMethod(spyService, "isDulicateVofficeRole", dto, vofficeRoleId, flag);
    }

    static void setFinalStatic(Field field, Object newValue, final Object targetObject) throws Exception {
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(targetObject, newValue);
    }
}