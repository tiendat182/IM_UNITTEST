package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.dto.LockUserInfoDTO;
import com.viettel.bccs.inventory.dto.StaffDTO;
import com.viettel.bccs.inventory.model.LockUserInfo;
import com.viettel.bccs.inventory.model.Shop;
import com.viettel.bccs.inventory.model.Staff;
import com.viettel.bccs.inventory.repo.LockUserInfoRepo;
import com.viettel.bccs.inventory.repo.LogUnlockUserInfoRepo;
import com.viettel.bccs.inventory.repo.ShopRepo;
import com.viettel.bccs.inventory.repo.StaffRepo;
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

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author DatLT
 * @date 10/10/2017
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({DataUtil.class, BaseServiceImpl.class, LockUserInfoService.class, DbUtil.class, Staff.class, Shop.class, EntityManager.class})
public class LockUserInfoServiceImplTest {

    @InjectMocks
    LockUserInfoServiceImpl lockUserInfoService ;
    @Mock
    private final BaseMapper<LockUserInfo, LockUserInfoDTO> mapper = new BaseMapper<>(LockUserInfo.class, LockUserInfoDTO.class);
    @Mock
    private EntityManager em;
    @Mock
    private LockUserInfoRepo repository;
    @Mock
    private StaffRepo staffRepository;
    @Mock
    private ShopRepo shopRepository;
    @Mock
    private LockUserInfoRepo lockUserInfoRepo;
    @Mock
    private LogUnlockUserInfoRepo logUnlockUserInfoRepo;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method LockUserInfoServiceImpl.createNote
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCount_1() throws  Exception {
        List<FilterRequest> filters = Lists.newArrayList();
        Mockito.when(repository.count(any())).thenReturn(1L);
        Assert.assertEquals(1L, lockUserInfoService.count(filters).longValue());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method LockUserInfoServiceImpl.findOne
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindOne_1() throws  Exception {
        LockUserInfoServiceImpl spyService = PowerMockito.spy( lockUserInfoService);
        LockUserInfo lockUserInfo = mock(LockUserInfo.class);
        LockUserInfoDTO lockUserInfoDTO = new LockUserInfoDTO();
        Mockito.when(repository.findOne(anyLong())).thenReturn(lockUserInfo);
        setFinalStatic(LockUserInfoServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(mapper.toDtoBean(lockUserInfo)).thenReturn(lockUserInfoDTO);
        spyService.findOne(1L);
        Assert.assertNotNull(lockUserInfoDTO);
    }
    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method LockUserInfoServiceImpl.findAll
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindAll_1() throws  Exception {
        LockUserInfoServiceImpl spyService = PowerMockito.spy( lockUserInfoService);
        LockUserInfo lockUserInfo = mock(LockUserInfo.class);
        LockUserInfoDTO lockUserInfoDTO = new LockUserInfoDTO();
        List<LockUserInfo> lockUserInfoList = Lists.newArrayList(lockUserInfo);
        List<LockUserInfoDTO> lockUserInfoDTOList = Lists.newArrayList(lockUserInfoDTO);
        Mockito.when(repository.findAll()).thenReturn(lockUserInfoList);
        Mockito.when(mapper.toDtoBean(lockUserInfoList)).thenReturn(lockUserInfoDTOList);
        setFinalStatic(LockUserInfoServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findAll();
        Assert.assertNotNull(lockUserInfoDTOList);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method LockUserInfoServiceImpl.findByFilter
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindByFilter_1() throws  Exception {
        FilterRequest filterRequest = new FilterRequest();
        List<FilterRequest> filterRequestList = Lists.newArrayList(filterRequest);
        LockUserInfoServiceImpl spyService = PowerMockito.spy( lockUserInfoService);
        LockUserInfo lockUserInfo = mock(LockUserInfo.class);
        LockUserInfoDTO lockUserInfoDTO = new LockUserInfoDTO();
        List<LockUserInfo> lockUserInfoList = Lists.newArrayList(lockUserInfo);
        List<LockUserInfoDTO> lockUserInfoDTOList = Lists.newArrayList(lockUserInfoDTO);
        Mockito.when(mapper.toDtoBean(lockUserInfoList)).thenReturn(lockUserInfoDTOList);
        setFinalStatic(LockUserInfoServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findByFilter(filterRequestList);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method LockUserInfoServiceImpl.create
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCreate_1() throws  Exception {
        LockUserInfo lockUserInfo = mock(LockUserInfo.class);
        LockUserInfoDTO lockUserInfoDTO = new LockUserInfoDTO();
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        Mockito.when(mapper.toPersistenceBean(lockUserInfoDTO)).thenReturn(lockUserInfo);
        Mockito.when(repository.save(lockUserInfo)).thenReturn(lockUserInfo);
        LockUserInfoServiceImpl spyService = PowerMockito.spy( lockUserInfoService);
        setFinalStatic(LockUserInfoServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.create(lockUserInfoDTO);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method LockUserInfoServiceImpl.update
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void testUpdate_1() throws  Exception {
        lockUserInfoService.update(null);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method LockUserInfoServiceImpl.deleteLockUser
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testDeleteLockUser_1() throws  Exception {
        List<Long> lstId = Lists.newArrayList();
        lockUserInfoService.deleteLockUser(lstId);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method LockUserInfoServiceImpl.getLockUserInfo
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testGetLockUserInfo_1() throws  Exception {
        String sql ="sql";
        Long lockTypeId =1L;
        Long validRange = 1L;
        Long checkRange = 1L;
        lockUserInfoService.getLockUserInfo(sql, lockTypeId,validRange,checkRange);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method LockUserInfoServiceImpl.checkExistLockUserInfo
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCheckExistLockUserInfo_1() throws  Exception {
        lockUserInfoService.checkExistLockUserInfo(1L,1L,1L);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method LockUserInfoServiceImpl.getLstLockUserInfo
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testGetLstLockUserInfo_1() throws  Exception {
        lockUserInfoService.getLstLockUserInfo();
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method LockUserInfoServiceImpl.canUnlock
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCanUnlock_1() throws  Exception {
        lockUserInfoService.canUnlock("sql", 1L, new Date());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method LockUserInfoServiceImpl.getListLockUser
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testGetListLockUser_1() throws  Exception {
        String staffCode="1";
        String lockTypeId="id";
        String stockTransType = "1";
        lockUserInfoService.getListLockUser(staffCode, lockTypeId, stockTransType);
    }

    @Test
    public void testGetListLockUser_2() throws  Exception {
        String staffCode="1";
        String lockTypeId="";
        String stockTransType = "transType";
        lockUserInfoService.getListLockUser(staffCode, lockTypeId, stockTransType);
    }

    @Test
    public void testGetListLockUser_3() throws  Exception {
        String staffCode="1";
        String lockTypeId="";
        String stockTransType = "1";
        StaffDTO staffDTO = mock(StaffDTO.class);
        when(staffRepository.getStaffByStaffCode(staffCode)).thenReturn(staffDTO);
        when(staffDTO.getStaffId()).thenReturn(1L);
        lockUserInfoService.getListLockUser(staffCode, lockTypeId, stockTransType);
    }

    @Test
    public void testGetListLockUser_4() throws  Exception {
        String staffCode="1";
        String lockTypeId="";
        String stockTransType = "1";
        StaffDTO staffDTO = mock(StaffDTO.class);
        when(staffRepository.getStaffByStaffCode(any())).thenThrow(new Exception());
        when(staffDTO.getStaffId()).thenReturn(1L);
        lockUserInfoService.getListLockUser(staffCode, lockTypeId, stockTransType);
    }

    @Test
    public void testGetListLockUser_5() throws  Exception {
        String staffCode="";
        String lockTypeId="";
        String stockTransType = "1";
        lockUserInfoService.getListLockUser(staffCode, lockTypeId, stockTransType);
    }

    @Test
    public void testGetListLockUser_6() throws  Exception {
        String staffCode="1";
        String lockTypeId="1";
        String stockTransType = "";
        lockUserInfoService.getListLockUser(staffCode, lockTypeId, stockTransType);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method LockUserInfoServiceImpl.deleteLogUserInfo
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testDeleteLogUserInfo_1() throws  Exception {
        LockUserInfoServiceImpl spyService = PowerMockito.spy(lockUserInfoService);
        String deleteId="1";
        String userId ="1";
        Staff staff = mock(Staff.class);
        Shop shop = mock(Shop.class);
        List queryResult = Lists.newArrayList();
        queryResult.add(1);
        when(staffRepository.findOne(anyLong())).thenReturn(staff);
        when(shopRepository.findOne(anyLong())).thenReturn(shop);
        when(staff.getShopId()).thenReturn(1L);
        when(shop.getShopPath()).thenReturn("1");
        Query queryObject = PowerMockito.mock(Query.class);
        when(em.createNativeQuery(anyString())).thenReturn(queryObject);
        when(queryObject.getResultList()).thenReturn(queryResult);
        spyService.deleteLogUserInfo(deleteId, userId);
    }

    @Test
    public void testDeleteLogUserInfo_2() throws  Exception {
        LockUserInfoServiceImpl spyService = PowerMockito.spy(lockUserInfoService);
        String deleteId="";
        String userId ="1";
        spyService.deleteLogUserInfo(deleteId, userId);
    }

    @Test
    public void testDeleteLogUserInfo_3() throws  Exception {
        LockUserInfoServiceImpl spyService = PowerMockito.spy(lockUserInfoService);
        String deleteId="1";
        String userId ="";
        Staff staff = mock(Staff.class);
        Shop shop = mock(Shop.class);
        List queryResult = Lists.newArrayList();
        queryResult.add(1);
        when(staffRepository.findOne(anyLong())).thenReturn(staff);
        when(shopRepository.findOne(anyLong())).thenReturn(shop);
        when(staff.getShopId()).thenReturn(1L);
        when(shop.getShopPath()).thenReturn("1");
        Query queryObject = PowerMockito.mock(Query.class);
        when(em.createNativeQuery(anyString())).thenReturn(queryObject);
        when(queryObject.getResultList()).thenReturn(queryResult);
        spyService.deleteLogUserInfo(deleteId, userId);
    }

    @Test
    public void testDeleteLogUserInfo_4() throws  Exception {
        LockUserInfoServiceImpl spyService = PowerMockito.spy(lockUserInfoService);
        String deleteId="1";
        String userId ="a";
        Staff staff = mock(Staff.class);
        Shop shop = mock(Shop.class);
        List queryResult = Lists.newArrayList();
        queryResult.add(1);
        when(staffRepository.findOne(anyLong())).thenReturn(staff);
        when(shopRepository.findOne(anyLong())).thenReturn(shop);
        when(staff.getShopId()).thenReturn(1L);
        when(shop.getShopPath()).thenReturn("1");
        Query queryObject = PowerMockito.mock(Query.class);
        when(em.createNativeQuery(anyString())).thenReturn(queryObject);
        when(queryObject.getResultList()).thenReturn(queryResult);
        spyService.deleteLogUserInfo(deleteId, userId);
    }

    @Test
    public void testDeleteLogUserInfo_5() throws  Exception {
        LockUserInfoServiceImpl spyService = PowerMockito.spy(lockUserInfoService);
        String deleteId="1";
        String userId ="3";
        Staff staff = mock(Staff.class);
        Shop shop = mock(Shop.class);
        List queryResult = Lists.newArrayList();
//        queryResult.add(1);
        when(staffRepository.findOne(anyLong())).thenReturn(staff);
        when(shopRepository.findOne(anyLong())).thenReturn(shop);
        when(staff.getShopId()).thenReturn(1L);
        when(shop.getShopPath()).thenReturn("1");
        Query queryObject = PowerMockito.mock(Query.class);
        when(em.createNativeQuery(anyString())).thenReturn(queryObject);
        when(queryObject.getResultList()).thenReturn(queryResult);
        spyService.deleteLogUserInfo(deleteId, userId);
    }

    @Test
    public void testDeleteLogUserInfo_6() throws  Exception {
        LockUserInfoServiceImpl spyService = PowerMockito.spy(lockUserInfoService);
        String deleteId="2";
        String userId ="2";
        Staff staff = mock(Staff.class);
        Shop shop = mock(Shop.class);
        List queryResult = Lists.newArrayList();
        when(staffRepository.findOne(anyLong())).thenReturn(staff);
        when(shopRepository.findOne(anyLong())).thenReturn(shop);
        when(staff.getShopId()).thenReturn(1L);
        when(shop.getShopPath()).thenReturn("1");
        Query queryObject = PowerMockito.mock(Query.class);
        when(em.createNativeQuery(anyString())).thenReturn(queryObject);
        when(queryObject.getResultList()).thenReturn(queryResult);
        spyService.deleteLogUserInfo(deleteId, userId);
    }
    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method LockUserInfoServiceImpl.delete
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testDelete_1() throws  Exception {
        lockUserInfoService.delete("del", "id");
    }

    @Test
    public void testDelete_2() throws  Exception {
        LockUserInfoServiceImpl spyService = PowerMockito.spy(lockUserInfoService);
        when(spyService.deleteLogUserInfo("del", "id")).thenThrow(new Exception());
        spyService.delete("del", "id");
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method LockUserInfoServiceImpl.processProcedureLockUserDamage
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testProcessProcedureLockUserDamage_1() throws  Exception {
        StoredProcedureQuery procedure = mock(StoredProcedureQuery.class);
        when(em.createStoredProcedureQuery(anyString())).thenReturn(procedure);
        lockUserInfoService.processProcedureLockUserDamage("ik");
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method LockUserInfoServiceImpl.unlockUserStockInspect
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testUnlockUserStockInspect_1() throws  Exception {
        StoredProcedureQuery procedure = mock(StoredProcedureQuery.class);
        when(em.createStoredProcedureQuery(anyString())).thenReturn(procedure);
        lockUserInfoService.unlockUserStockInspect();
    }












    static void setFinalStatic(Field field, Object newValue, final Object targetObject) throws Exception {
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(targetObject, newValue);
    }
}