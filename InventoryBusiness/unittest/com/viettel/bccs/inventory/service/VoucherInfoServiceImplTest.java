package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.dto.VoucherInfoDTO;
import com.viettel.bccs.inventory.model.VoucherInfo;
import com.viettel.bccs.inventory.repo.VoucherInfoRepo;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.DbUtil;
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
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author DatLT
 * @date 15/10/2017
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({DbUtil.class, VoucherInfoServiceImpl.class, BaseServiceImpl.class, VoucherInfoService.class, DateUtil.class})
public class VoucherInfoServiceImplTest {

    @InjectMocks
    VoucherInfoServiceImpl voucherInfoService;
    @Mock
    private final BaseMapper<VoucherInfo, VoucherInfoDTO> mapper = new BaseMapper(VoucherInfo.class, VoucherInfoDTO.class);
    @Mock
    private VoucherInfoRepo voucherInfoRepo;
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
     * Test for method VoucherInfoServiceImpl.count
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindAll_1() throws Exception {
        VoucherInfoServiceImpl spyService = PowerMockito.spy( voucherInfoService);
        VoucherInfo voucherInfo = mock(VoucherInfo.class);
        VoucherInfoDTO voucherInfoDTO = new VoucherInfoDTO();
        List<VoucherInfoDTO> voucherInfoDTOList = Lists.newArrayList(voucherInfoDTO);
        List<VoucherInfo> voucherInfoList = Lists.newArrayList(voucherInfo);
        Mockito.when(mapper.toDtoBean(voucherInfoList)).thenReturn(voucherInfoDTOList);
        setFinalStatic(VoucherInfoServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findAll();
        Assert.assertNotNull(voucherInfoDTOList);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method VoucherInfoServiceImpl.insertListVoucherInfo
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testInsertListVoucherInfo_1() throws Exception {
        List<VoucherInfoDTO> voucherInfoDTOs = Lists.newArrayList();
        VoucherInfoServiceImpl spyService = PowerMockito.spy( voucherInfoService);
        VoucherInfo voucherInfo = mock(VoucherInfo.class);
        VoucherInfoDTO voucherInfoDTO = new VoucherInfoDTO();

        Mockito.when(mapper.toPersistenceBean(voucherInfoDTO)).thenReturn(voucherInfo);
        setFinalStatic(VoucherInfoServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.insertListVoucherInfo(voucherInfoDTOs);
    }

    @Test
    public void testInsertListVoucherInfo_2() throws Exception {
        List<VoucherInfoDTO> voucherInfoDTOs = Lists.newArrayList();
        VoucherInfoServiceImpl spyService = PowerMockito.spy( voucherInfoService);
        VoucherInfo voucherInfo = mock(VoucherInfo.class);
        VoucherInfoDTO voucherInfoDTO = new VoucherInfoDTO();

        doThrow(new Exception()).when(spyService).saveImportVoucher(any());
        Mockito.when(mapper.toPersistenceBean(voucherInfoDTO)).thenReturn(voucherInfo);
        setFinalStatic(VoucherInfoServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.insertListVoucherInfo(voucherInfoDTOs);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method VoucherInfoServiceImpl.findBySerial
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindBySerial_1() throws Exception {
        VoucherInfo voucherInfo = new VoucherInfo();
        VoucherInfoServiceImpl spyService = PowerMockito.spy( voucherInfoService);
        VoucherInfoDTO voucherInfoDTO = new VoucherInfoDTO();

        when(voucherInfoRepo.findBySerial(anyString())).thenReturn(voucherInfo);
        Mockito.when(mapper.toDtoBean(voucherInfo)).thenReturn(voucherInfoDTO);
        setFinalStatic(VoucherInfoServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findBySerial("1");
    }

    @Test
    public void testFindBySerial_2() throws Exception {
        VoucherInfo voucherInfo = new VoucherInfo();
        VoucherInfoServiceImpl spyService = PowerMockito.spy( voucherInfoService);
        VoucherInfoDTO voucherInfoDTO = new VoucherInfoDTO();

        when(voucherInfoRepo.findBySerial(anyString())).thenReturn(null);
        Mockito.when(mapper.toDtoBean(voucherInfo)).thenReturn(voucherInfoDTO);
        setFinalStatic(VoucherInfoServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findBySerial("1");
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method VoucherInfoServiceImpl.deleteVoucherInfo
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testDeleteVoucherInfo_1() throws Exception {
        voucherInfoService.deleteVoucherInfo(1L);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method VoucherInfoServiceImpl.saveImportVoucher
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testSaveImportVoucher_1() throws Exception {
        VoucherInfo voucherInfo = new VoucherInfo();
        List<VoucherInfo> lst = Lists.newArrayList(voucherInfo);

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        voucherInfoService.saveImportVoucher(lst);
    }

    static void setFinalStatic(Field field, Object newValue, final Object targetObject) throws Exception {
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(targetObject, newValue);
    }
}