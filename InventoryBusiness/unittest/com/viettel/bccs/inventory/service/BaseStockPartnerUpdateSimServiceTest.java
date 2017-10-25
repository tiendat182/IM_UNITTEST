package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.IMCommonUtils;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.BaseStock.BaseStockPartnerService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.RequiredRoleMap;
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

import java.sql.PreparedStatement;
import java.util.Iterator;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.mockito.Matchers.any;

/**
 * @author DatLT
 * @date 2017/09/19
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({BaseStockPartnerService.class, IMCommonUtils.class, ImportPartnerRequestDTO.class})
public class BaseStockPartnerUpdateSimServiceTest {

    @InjectMocks
    private BaseStockPartnerUpdateSimService baseStockPartnerUpdateSimService;

    @Mock
    private Connection conn;

    @Mock
    private EntityManager em;

    @Mock
    private ProductOfferingService productOfferingService;

    @Mock
    ShopService shopService;

    @Mock
    ProductWs productWs;

    @Mock
    private List<HashMap<String, String>> lstMapParam;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    /** -------------------------------------------------------------------------------- **/
    /** --- Test for baseStockPartnerUpdateSimService.executeStockTrans method **/
    /*** --------------------------------------------------------------------------------**/
    @Test
    public void testExecuteStockTrans_1() throws Exception {
        PowerMockito.mockStatic(IMCommonUtils.class);
        PowerMockito.when(IMCommonUtils.getDBIMConnection()).thenReturn(conn);

        ImportPartnerRequestDTO importPartnerRequestDTO = new ImportPartnerRequestDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        List<String> lstParam = new ArrayList<>();
        ImportPartnerDetailDTO importPartnerDetailDTO = new ImportPartnerDetailDTO();

        String sessionId = "";
        BaseStockPartnerUpdateSimService spyService = PowerMockito.spy(baseStockPartnerUpdateSimService);
        PowerMockito.doNothing().when(spyService, "doValidate", any());
        importPartnerDetailDTO.setLstParam(lstParam);
        List<ImportPartnerDetailDTO> listImportPartnerDetailDTOs = Lists.newArrayList(importPartnerDetailDTO);
        importPartnerRequestDTO.setListImportPartnerDetailDTOs(listImportPartnerDetailDTOs);

        spyService.executeStockTrans(importPartnerRequestDTO, requiredRoleMap, sessionId);
    }

    @Test(expected = Exception.class)
    public void testExecuteStockTrans_2() throws Exception {
        PowerMockito.mockStatic(IMCommonUtils.class);
        PowerMockito.when(IMCommonUtils.getDBIMConnection()).thenThrow(Exception.class);

        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        ImportPartnerRequestDTO importPartnerRequestDTO = new ImportPartnerRequestDTO();
        List<String> lstParam = new ArrayList<>();
        lstParam.add("1");
        ImportPartnerDetailDTO importPartnerDetailDTO = new ImportPartnerDetailDTO();

        String sessionId = "";
        BaseStockPartnerUpdateSimService spyService = PowerMockito.spy(baseStockPartnerUpdateSimService);
        PowerMockito.doNothing().when(spyService, "doValidate", any());
        importPartnerDetailDTO.setLstParam(lstParam);
        List<ImportPartnerDetailDTO> listImportPartnerDetailDTOs = Lists.newArrayList(importPartnerDetailDTO);
        List<String> lstProfile = new ArrayList<>();
        lstProfile.add("2");
        importPartnerRequestDTO.setListImportPartnerDetailDTOs(listImportPartnerDetailDTOs);
        importPartnerRequestDTO.setListProfile(lstProfile);

        spyService.executeStockTrans(importPartnerRequestDTO, requiredRoleMap, sessionId);
    }

    /** -------------------------------------------------------------------------------- **/
    /** --- Test for baseStockPartnerUpdateSimService.updateSimByFile method    **/
    /*** --------------------------------------------------------------------------------**/
    @Test
    public void testUpdateSimByFile_1() throws Exception {
        PowerMockito.mockStatic(IMCommonUtils.class);
        PowerMockito.when(IMCommonUtils.getDBIMConnection()).thenReturn(conn);
        ImportPartnerRequestDTO importPartnerRequestDTO = new ImportPartnerRequestDTO();
        List<String> lstColumnName = new ArrayList<>();
        ImportPartnerDetailDTO importPartnerDetailDTO = new ImportPartnerDetailDTO();
        ProfileDTO profileDTO = new ProfileDTO();

        String sessionId = "";
        importPartnerDetailDTO.setProdOfferId(1L);
        List<ImportPartnerDetailDTO> lstImportPartnerDetailDTO = Lists.newArrayList(importPartnerDetailDTO);
        importPartnerRequestDTO.setListImportPartnerDetailDTOs(lstImportPartnerDetailDTO);
        importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).setProdOfferId(1L);
        lstColumnName.add("SERIAL");
        lstColumnName.add("CNT");
        profileDTO.setLstColumnName(lstColumnName);

        Mockito.doReturn(profileDTO).when(productOfferingService).getProfileByProductId(any());
        baseStockPartnerUpdateSimService.updateSimByFile(importPartnerRequestDTO, conn, sessionId);
    }

    @Test
    public void testUpdateSimByFile_2() throws Exception {
        PowerMockito.mockStatic(IMCommonUtils.class);
        PowerMockito.when(IMCommonUtils.getDBIMConnection()).thenReturn(conn);
        ImportPartnerRequestDTO importPartnerRequestDTO = new ImportPartnerRequestDTO();
        ImportPartnerDetailDTO importPartnerDetailDTO = new ImportPartnerDetailDTO();
        List<String> lstColumnName = new ArrayList<>();
        ProfileDTO profileDTO = new ProfileDTO();

        String sessionId = "";
        importPartnerDetailDTO.setProdOfferId(1L);
        List<ImportPartnerDetailDTO> lstImportPartnerDetailDTO = Lists.newArrayList(importPartnerDetailDTO);
        importPartnerRequestDTO.setListImportPartnerDetailDTOs(lstImportPartnerDetailDTO);
        importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).setProdOfferId(1L);
        lstColumnName.add("PAWN");
        profileDTO.setLstColumnName(lstColumnName);

        Mockito.doReturn(profileDTO).when(productOfferingService).getProfileByProductId(any());
        baseStockPartnerUpdateSimService.updateSimByFile(importPartnerRequestDTO, conn, sessionId);
    }

    @Test
    public void testUpdateSimByFile_3() throws Exception {
        PowerMockito.mockStatic(IMCommonUtils.class);
        PowerMockito.when(IMCommonUtils.getDBIMConnection()).thenReturn(conn);
        ImportPartnerRequestDTO importPartnerRequestDTO = new ImportPartnerRequestDTO();
        ImportPartnerDetailDTO importPartnerDetailDTO = new ImportPartnerDetailDTO();
        ProfileDTO profileDTO = new ProfileDTO();
        List<String> lstColumnName = new ArrayList<>();
        List<String> lstParam = new ArrayList<>();

        importPartnerDetailDTO.setProdOfferId(1L);
        List<ImportPartnerDetailDTO> lstImportPartnerDetailDTO = Lists.newArrayList(importPartnerDetailDTO);
        importPartnerRequestDTO.setListImportPartnerDetailDTOs(lstImportPartnerDetailDTO);
        importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).setProdOfferId(1L);
        lstParam.add("stat");
        importPartnerDetailDTO.setLstParam(lstParam);
        String sessionId = "";
        lstColumnName.add("SERIAL");
        lstColumnName.add("NOT");
        profileDTO.setLstColumnName(lstColumnName);
        importPartnerRequestDTO.setRegionStockId(1L);
        HashMap<String, String> stringHashMap = new HashMap<>(1);
        stringHashMap.put("1", "2");
        lstMapParam.add(stringHashMap);
        Mockito.doReturn(profileDTO).when(productOfferingService).getProfileByProductId(any());
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        lstParam.add("1");
        BaseStockPartnerUpdateSimService spyService = PowerMockito.spy(baseStockPartnerUpdateSimService);
        PowerMockito.doNothing().when(spyService, "doValidate", any());
        importPartnerDetailDTO.setLstParam(lstParam);
        List<ImportPartnerDetailDTO> listImportPartnerDetailDTOs = Lists.newArrayList(importPartnerDetailDTO);
        List<String> lstProfile = new ArrayList<>();
        lstProfile.add("2");
        importPartnerRequestDTO.setListImportPartnerDetailDTOs(listImportPartnerDetailDTOs);
        importPartnerRequestDTO.setListProfile(lstProfile);
        PreparedStatement updateSim = PowerMockito.mock(PreparedStatement.class);
        PowerMockito.when(conn.prepareStatement(any())).thenReturn(updateSim);
        spyService.executeStockTrans(importPartnerRequestDTO, requiredRoleMap, sessionId);
        baseStockPartnerUpdateSimService.updateSimByFile(importPartnerRequestDTO, conn, sessionId);
    }

    @Test(expected = Exception.class)
    public void testUpdateSimByFile_4() throws Exception {
        PowerMockito.mockStatic(IMCommonUtils.class);
        PowerMockito.when(IMCommonUtils.getDBIMConnection()).thenReturn(conn);
        ImportPartnerRequestDTO importPartnerRequestDTO = new ImportPartnerRequestDTO();
        ImportPartnerDetailDTO importPartnerDetailDTO = new ImportPartnerDetailDTO();
        ProfileDTO profileDTO = new ProfileDTO();
        List<String> lstColumnName = new ArrayList<>();
        List<String> lstParam = new ArrayList<>();

        importPartnerDetailDTO.setProdOfferId(1L);
        List<ImportPartnerDetailDTO> lstImportPartnerDetailDTO = Lists.newArrayList(importPartnerDetailDTO);
        importPartnerRequestDTO.setListImportPartnerDetailDTOs(lstImportPartnerDetailDTO);
        importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).setProdOfferId(1L);
        lstParam.add("stat");
        importPartnerDetailDTO.setLstParam(lstParam);
        lstColumnName.add("SERIAL");
        String sessionId = "";
        profileDTO.setLstColumnName(lstColumnName);
        importPartnerRequestDTO.setRegionStockId(null);
        HashMap<String, String> stringHashMap = new HashMap<>(1);
        stringHashMap.put("1", "2");
        lstMapParam.add(stringHashMap);
        Mockito.doReturn(profileDTO).when(productOfferingService).getProfileByProductId(any());
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        lstParam.add("1");
        BaseStockPartnerUpdateSimService spyService = PowerMockito.spy(baseStockPartnerUpdateSimService);
        PowerMockito.doNothing().when(spyService, "doValidate", any());
        importPartnerDetailDTO.setLstParam(lstParam);
        List<ImportPartnerDetailDTO> listImportPartnerDetailDTOs = Lists.newArrayList(importPartnerDetailDTO);
        List<String> lstProfile = new ArrayList<>();
        lstProfile.add("2");
        importPartnerRequestDTO.setListImportPartnerDetailDTOs(listImportPartnerDetailDTOs);
        importPartnerRequestDTO.setListProfile(lstProfile);
        PreparedStatement updateSim = PowerMockito.mock(PreparedStatement.class);
        PowerMockito.when(conn.prepareStatement(any())).thenReturn(updateSim);
        spyService.executeStockTrans(importPartnerRequestDTO, requiredRoleMap, sessionId);
        baseStockPartnerUpdateSimService.updateSimByFile(importPartnerRequestDTO, conn, sessionId);
    }

    @Test
    public void testUpdateSimByFile_5() throws Exception {
        PowerMockito.mockStatic(IMCommonUtils.class);
        PowerMockito.when(IMCommonUtils.getDBIMConnection()).thenReturn(conn);
        ImportPartnerRequestDTO importPartnerRequestDTO = new ImportPartnerRequestDTO();
        ImportPartnerDetailDTO importPartnerDetailDTO = new ImportPartnerDetailDTO();
        ProfileDTO profileDTO = new ProfileDTO();
        List<String> lstParam = new ArrayList<>();
        List<String> lstColumnName = new ArrayList<>();

        importPartnerDetailDTO.setProdOfferId(1L);
        List<ImportPartnerDetailDTO> lstImportPartnerDetailDTO = Lists.newArrayList(importPartnerDetailDTO);
        importPartnerRequestDTO.setListImportPartnerDetailDTOs(lstImportPartnerDetailDTO);
        importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).setProdOfferId(1L);
        lstParam.add("bong");
        lstParam.add("ping");
        importPartnerDetailDTO.setLstParam(lstParam);
        String sessionId = "";
        lstColumnName.add("SERIAL");
        profileDTO.setLstColumnName(lstColumnName);
        setFinalStatic(Const.class.getDeclaredField("DEFAULT_BATCH_SIZE"), 1L);

        BaseStockPartnerUpdateSimService spyService = Mockito.spy(baseStockPartnerUpdateSimService);
        Mockito.doReturn(10000).when(spyService).countNumberErrorForUpdateSim(any(), any());
        Mockito.doReturn(profileDTO).when(productOfferingService).getProfileByProductId(any());
        spyService.updateSimByFile(importPartnerRequestDTO, conn, sessionId);
    }

    @Test
    public void testUpdateSimByFile_6() throws Exception {
        PowerMockito.mockStatic(IMCommonUtils.class);
        PowerMockito.when(IMCommonUtils.getDBIMConnection()).thenReturn(conn);
        ImportPartnerDetailDTO importPartnerDetailDTO = new ImportPartnerDetailDTO();
        ImportPartnerRequestDTO importPartnerRequestDTO = new ImportPartnerRequestDTO();
        List<String> lstParam = new ArrayList<>();
        ProfileDTO profileDTO = new ProfileDTO();
        List<String> lstColumnName = new ArrayList<>();

        importPartnerDetailDTO.setProdOfferId(1L);
        List<ImportPartnerDetailDTO> lstImportPartnerDetailDTO = Lists.newArrayList(importPartnerDetailDTO);
        importPartnerRequestDTO.setListImportPartnerDetailDTOs(lstImportPartnerDetailDTO);
        importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).setProdOfferId(1L);
        lstParam.add("ping");
        lstParam.add("bong");
        importPartnerDetailDTO.setLstParam(lstParam);
        String sessionId = "";
        lstColumnName.add("SERIAL");
        profileDTO.setLstColumnName(lstColumnName);
        setFinalStatic(Const.class.getDeclaredField("DEFAULT_BATCH_SIZE"), 1L);
        BaseStockPartnerUpdateSimService spyService = Mockito.spy(baseStockPartnerUpdateSimService);
        Mockito.doThrow(new Exception()).when(spyService).countNumberErrorForUpdateSim(any(), any());
        Mockito.doReturn(profileDTO).when(productOfferingService).getProfileByProductId(any());
        spyService.updateSimByFile(importPartnerRequestDTO, conn, sessionId);
    }

    @Test
    public void testUpdateSimByFile_7() throws Exception {
        PowerMockito.mockStatic(IMCommonUtils.class);
        PowerMockito.when(IMCommonUtils.getDBIMConnection()).thenReturn(conn);
        ImportPartnerRequestDTO importPartnerRequestDTO = new ImportPartnerRequestDTO();
        ImportPartnerDetailDTO importPartnerDetailDTO = new ImportPartnerDetailDTO();
        ProfileDTO profileDTO = new ProfileDTO();
        List<String> lstColumnName = new ArrayList<>();
        List<String> lstParam = new ArrayList<>();

        importPartnerDetailDTO.setProdOfferId(1L);
        List<ImportPartnerDetailDTO> lstImportPartnerDetailDTO = Lists.newArrayList(importPartnerDetailDTO);
        importPartnerRequestDTO.setListImportPartnerDetailDTOs(lstImportPartnerDetailDTO);
        importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).setProdOfferId(1L);
        lstParam.add("stat");
        importPartnerDetailDTO.setLstParam(lstParam);
        String sessionId = "";
        lstColumnName.add("SERIAL");
        profileDTO.setLstColumnName(lstColumnName);
        importPartnerRequestDTO.setRegionStockId(1L);
        HashMap<String, String> stringHashMap = new HashMap<>(1);
        stringHashMap.put("1", "2");
        lstMapParam.add(stringHashMap);
        Mockito.doReturn(profileDTO).when(productOfferingService).getProfileByProductId(any());
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        lstParam.add("1");
        BaseStockPartnerUpdateSimService spyService = PowerMockito.spy(baseStockPartnerUpdateSimService);
        PowerMockito.doNothing().when(spyService, "doValidate", any());
        importPartnerDetailDTO.setLstParam(lstParam);
        List<ImportPartnerDetailDTO> listImportPartnerDetailDTOs = Lists.newArrayList(importPartnerDetailDTO);
        List<String> lstProfile = new ArrayList<>();
        lstProfile.add("2");
        importPartnerRequestDTO.setListImportPartnerDetailDTOs(listImportPartnerDetailDTOs);
        importPartnerRequestDTO.setListProfile(lstProfile);
        PreparedStatement updateSim = PowerMockito.mock(PreparedStatement.class);
        PowerMockito.when(conn.prepareStatement(any())).thenReturn(updateSim);
        spyService.executeStockTrans(importPartnerRequestDTO, requiredRoleMap, sessionId);
        setFinalStatic(Const.class.getDeclaredField("DEFAULT_BATCH_SIZE"), 1L);
        Mockito.doReturn(2).when(spyService).countNumberErrorForUpdateSim(any(), any());
        Mockito.doReturn(profileDTO).when(productOfferingService).getProfileByProductId(any());
        spyService.updateSimByFile(importPartnerRequestDTO, conn, sessionId);
    }

    @Test
    public void testUpdateSimByFile_8() throws Exception {
        PowerMockito.mockStatic(IMCommonUtils.class);
        PowerMockito.when(IMCommonUtils.getDBIMConnection()).thenReturn(conn);
        ImportPartnerRequestDTO importPartnerRequestDTO = new ImportPartnerRequestDTO();
        ImportPartnerDetailDTO importPartnerDetailDTO = new ImportPartnerDetailDTO();
        ProfileDTO profileDTO = new ProfileDTO();
        List<String> lstColumnName = new ArrayList<>();
        List<String> lstParam = new ArrayList<>();

        importPartnerDetailDTO.setProdOfferId(1L);
        List<ImportPartnerDetailDTO> lstImportPartnerDetailDTO = Lists.newArrayList(importPartnerDetailDTO);
        importPartnerRequestDTO.setListImportPartnerDetailDTOs(lstImportPartnerDetailDTO);
        importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).setProdOfferId(1L);
        lstParam.add("stat");
        importPartnerDetailDTO.setLstParam(lstParam);
        String sessionId = "";
        lstColumnName.add("SERIAL");
        profileDTO.setLstColumnName(lstColumnName);
        importPartnerRequestDTO.setRegionStockId(1L);
        HashMap<String, String> stringHashMap = new HashMap<>(1);
        stringHashMap.put("1", "2");
        lstMapParam.add(stringHashMap);
        Mockito.doReturn(profileDTO).when(productOfferingService).getProfileByProductId(any());
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        lstParam.add("1");
        BaseStockPartnerUpdateSimService spyService = PowerMockito.spy(baseStockPartnerUpdateSimService);
        PowerMockito.doNothing().when(spyService, "doValidate", any());
        importPartnerDetailDTO.setLstParam(lstParam);
        List<ImportPartnerDetailDTO> listImportPartnerDetailDTOs = Lists.newArrayList(importPartnerDetailDTO);
        List<String> lstProfile = new ArrayList<>();
        lstProfile.add("2");
        importPartnerRequestDTO.setListImportPartnerDetailDTOs(listImportPartnerDetailDTOs);
        importPartnerRequestDTO.setListProfile(lstProfile);
        PreparedStatement updateSim = PowerMockito.mock(PreparedStatement.class);
        PowerMockito.when(conn.prepareStatement(any())).thenReturn(updateSim);
        spyService.executeStockTrans(importPartnerRequestDTO, requiredRoleMap, sessionId);
        setFinalStatic(Const.class.getDeclaredField("DEFAULT_BATCH_SIZE"), 1L);
        Mockito.doReturn(0).when(spyService).countNumberErrorForUpdateSim(any(), any());
        Mockito.doReturn(profileDTO).when(productOfferingService).getProfileByProductId(any());
        spyService.updateSimByFile(importPartnerRequestDTO, conn, sessionId);
    }

    @Test
    public void testUpdateSimByFile_9() throws Exception {
        PowerMockito.mockStatic(IMCommonUtils.class);
        PowerMockito.when(IMCommonUtils.getDBIMConnection()).thenReturn(conn);
        ImportPartnerRequestDTO importPartnerRequestDTO = new ImportPartnerRequestDTO();
        List<String> lstColumnName = new ArrayList<>();
        ImportPartnerDetailDTO importPartnerDetailDTO = new ImportPartnerDetailDTO();
        ProfileDTO profileDTO = new ProfileDTO();

        String sessionId = "";
        importPartnerDetailDTO.setProdOfferId(1L);
        List<ImportPartnerDetailDTO> lstImportPartnerDetailDTO = Lists.newArrayList(importPartnerDetailDTO);
        importPartnerRequestDTO.setListImportPartnerDetailDTOs(lstImportPartnerDetailDTO);
        importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).setProdOfferId(1L);
        lstColumnName.add("SERIAL");
        lstColumnName.add("CNT");
        profileDTO.setLstColumnName(lstColumnName);

        Mockito.doReturn(null).when(productOfferingService).getProfileByProductId(any());
        baseStockPartnerUpdateSimService.updateSimByFile(importPartnerRequestDTO, conn, sessionId);
    }

    @Test
    public void testUpdateSimByFile_10() throws Exception {
        PowerMockito.mockStatic(IMCommonUtils.class);
        PowerMockito.when(IMCommonUtils.getDBIMConnection()).thenReturn(conn);
        ImportPartnerRequestDTO importPartnerRequestDTO = new ImportPartnerRequestDTO();
        List<String> lstColumnName = new ArrayList<>();
        ImportPartnerDetailDTO importPartnerDetailDTO = new ImportPartnerDetailDTO();
        ProfileDTO profileDTO = new ProfileDTO();

        String sessionId = "";
        importPartnerDetailDTO.setProdOfferId(1L);
        List<ImportPartnerDetailDTO> lstImportPartnerDetailDTO = Lists.newArrayList(importPartnerDetailDTO);
        importPartnerRequestDTO.setListImportPartnerDetailDTOs(lstImportPartnerDetailDTO);
        importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).setProdOfferId(1L);
        lstColumnName.add("CNT");
        lstColumnName.add("CNT");
        profileDTO.setLstColumnName(lstColumnName);

        Mockito.doReturn(profileDTO).when(productOfferingService).getProfileByProductId(any());
        baseStockPartnerUpdateSimService.updateSimByFile(importPartnerRequestDTO, conn, sessionId);
    }

    /** -------------------------------------------------------------------------------- **/
    /** --- Test for baseStockPartnerUpdateSimService.doValidate method    **/
    /*** --------------------------------------------------------------------------------**/
    @Test
    public void testDoValidate_1() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        ImportPartnerRequestDTO importPartnerRequestDTO = new ImportPartnerRequestDTO();
        baseStockPartnerUpdateSimService.doValidate(importPartnerRequestDTO);
    }

    @Test
    public void testDoValidate_2() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        ImportPartnerRequestDTO importPartnerRequestDTO = new ImportPartnerRequestDTO();
        ImportPartnerDetailDTO importPartnerDetailDTO = new ImportPartnerDetailDTO();
        importPartnerDetailDTO.setProdOfferId(1L);
        List<ImportPartnerDetailDTO> importPartnerDetailDTOList = Lists.newArrayList(importPartnerDetailDTO);
        importPartnerRequestDTO.setListImportPartnerDetailDTOs(importPartnerDetailDTOList);
        baseStockPartnerUpdateSimService.doValidate(importPartnerRequestDTO);
    }

    @Test
    public void testDoValidate_3() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);
        ImportPartnerRequestDTO importPartnerRequestDTO = new ImportPartnerRequestDTO();
        ImportPartnerDetailDTO importPartnerDetailDTO = new ImportPartnerDetailDTO();
        ShopDTO shopDTO = new ShopDTO();

        importPartnerDetailDTO.setProdOfferId(1L);
        List<ImportPartnerDetailDTO> importPartnerDetailDTOList = Lists.newArrayList(importPartnerDetailDTO);
        importPartnerRequestDTO.setListImportPartnerDetailDTOs(importPartnerDetailDTOList);
        importPartnerRequestDTO.setToOwnerId(1L);
        shopDTO.setBankName("TpBank");

        Mockito.doReturn(null).when(shopService).findOne(any());
        baseStockPartnerUpdateSimService.doValidate(importPartnerRequestDTO);
    }

    @Test
    public void testDoValidate_4() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);
        ImportPartnerRequestDTO importPartnerRequestDTO = new ImportPartnerRequestDTO();
        ImportPartnerDetailDTO importPartnerDetailDTO = new ImportPartnerDetailDTO();
        ShopDTO shopDTO = new ShopDTO();

        importPartnerDetailDTO.setQuantity(1L);
        List<ImportPartnerDetailDTO> importPartnerDetailDTOList = Lists.newArrayList(importPartnerDetailDTO);
        importPartnerRequestDTO.setListImportPartnerDetailDTOs(importPartnerDetailDTOList);
        importPartnerRequestDTO.setToOwnerId(1L);
        shopDTO.setBankName("TpBank");

        Mockito.doReturn(shopDTO).when(shopService).findOne(any());
        baseStockPartnerUpdateSimService.doValidate(importPartnerRequestDTO);
    }

    @Test
    public void testDoValidate_5() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);
        ImportPartnerRequestDTO importPartnerRequestDTO = new ImportPartnerRequestDTO();
        ImportPartnerDetailDTO importPartnerDetailDTO = new ImportPartnerDetailDTO();
        ShopDTO shopDTO = new ShopDTO();

        importPartnerDetailDTO.setQuantity(1L);
        importPartnerDetailDTO.setProdOfferId(1L);
        List<ImportPartnerDetailDTO> importPartnerDetailDTOList = Lists.newArrayList(importPartnerDetailDTO);
        importPartnerRequestDTO.setListImportPartnerDetailDTOs(importPartnerDetailDTOList);
        importPartnerRequestDTO.setToOwnerId(1L);
        shopDTO.setBankName("TpBank");

        Mockito.doReturn(shopDTO).when(shopService).findOne(any());
        Mockito.doReturn(null).when(productOfferingService).findOne(any());
        baseStockPartnerUpdateSimService.doValidate(importPartnerRequestDTO);
    }

    @Test
    public void testDoValidate_6() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        ImportPartnerRequestDTO importPartnerRequestDTO = null;
        baseStockPartnerUpdateSimService.doValidate(importPartnerRequestDTO);
    }

    @Test
    public void testDoValidate_7() throws Exception {
        ImportPartnerRequestDTO importPartnerRequestDTO = new ImportPartnerRequestDTO();
        ImportPartnerDetailDTO importPartnerDetailDTO = new ImportPartnerDetailDTO();
        ShopDTO shopDTO = new ShopDTO();

        importPartnerDetailDTO.setQuantity(1L);
        importPartnerDetailDTO.setProdOfferId(1L);
        List<ImportPartnerDetailDTO> importPartnerDetailDTOList = Lists.newArrayList(importPartnerDetailDTO);
        importPartnerRequestDTO.setListImportPartnerDetailDTOs(importPartnerDetailDTOList);
        importPartnerRequestDTO.setToOwnerId(1L);
        shopDTO.setBankName("TpBank");
        ProductOfferingDTO productOffering = new ProductOfferingDTO();

        Mockito.doReturn(shopDTO).when(shopService).findOne(any());
        Mockito.doReturn(productOffering).when(productOfferingService).findOne(any());
        baseStockPartnerUpdateSimService.doValidate(importPartnerRequestDTO);
    }
    /** -------------------------------------------------------------------------------- **/
    /** --- Test for baseStockPartnerUpdateSimService.getErrorDetails method    **/
    /*** --------------------------------------------------------------------------------**/
    @Test
    public void testGetErrorDetails_1() throws Exception {
        Long productOfferId = 1L;
        String sessionId = "Qsqatswe123";
        ProfileDTO profileDTO = new ProfileDTO();
        Query dQuery = Mockito.mock(Query.class);

        List<String> lstColumnName = Lists.newArrayList();
        lstColumnName.add("col");
        profileDTO.setLstColumnName(lstColumnName);
        Mockito.doReturn(profileDTO).when(productWs).getProfileByProductOfferId(any());
        Mockito.doReturn(dQuery).when(em).createNativeQuery(any());
        baseStockPartnerUpdateSimService.getErrorDetails(sessionId, productOfferId);
    }

    @Test
    public void testGetErrorDetails_2() throws Exception {
        Long productOfferId = 1L;
        String sessionId = "csqatswe123";
        ProfileDTO profileDTO = new ProfileDTO();
        Query dQuery = Mockito.mock(Query.class);
        List lstObject = new ArrayList();

        Mockito.doReturn(profileDTO).when(productWs).getProfileByProductOfferId(any());
        Mockito.doReturn(dQuery).when(em).createNativeQuery(any());
        Mockito.doReturn(lstObject).when(dQuery).getResultList();
        baseStockPartnerUpdateSimService.getErrorDetails(sessionId, productOfferId);
    }

    @Test
    public void testGetErrorDetails_3() throws Exception {
        Long productOfferId = 1L;
        String sessionId = "CR123";
        ProfileDTO profileDTO = new ProfileDTO();
        Query dQuery = Mockito.mock(Query.class);
        List lstObject = new ArrayList();
        Object[] obj1 = new Object[] {"1", "2"};
        Object[] obj2 = new Object[] {"3", "4"};
        lstObject.add(obj1);
        lstObject.add(obj2);

        Mockito.doReturn(profileDTO).when(productWs).getProfileByProductOfferId(any());
        Mockito.doReturn(dQuery).when(em).createNativeQuery(any());
        Mockito.doReturn(lstObject).when(dQuery).getResultList();
        baseStockPartnerUpdateSimService.getErrorDetails(sessionId, productOfferId);
    }

    @Test(expected =Exception.class)
    public void testGetErrorDetails_4() throws Exception {
        Long productOfferId = 1L;
        String sessionId = "Qsqatswe1234";
        ProfileDTO profileDTO = new ProfileDTO();
        Query dQuery = Mockito.mock(Query.class);

        Mockito.doThrow(new Exception()).when(productWs).getProfileByProductOfferId(any());
        Mockito.doReturn(dQuery).when(em).createNativeQuery(any());
        baseStockPartnerUpdateSimService.getErrorDetails(sessionId, productOfferId);
    }

    @Test
    public void testGetErrorDetails_5() throws Exception {
        Long productOfferId = 1L;
        String sessionId = "Qsqatswe123";
        ProfileDTO profileDTO = null;
        Query dQuery = Mockito.mock(Query.class);

        Mockito.doReturn(profileDTO).when(productWs).getProfileByProductOfferId(any());
        Mockito.doReturn(dQuery).when(em).createNativeQuery(any());
        baseStockPartnerUpdateSimService.getErrorDetails(sessionId, productOfferId);
    }

    @Test
    public void testGetErrorDetails_6() throws Exception {
        Long productOfferId = 1L;
        String sessionId = "CR123";
        ProfileDTO profileDTO = new ProfileDTO();
        Query dQuery = Mockito.mock(Query.class);
        List lstObject = new ArrayList();
        Object[] obj1 = new Object[] {"1", "2"};
        Object[] obj2 = new Object[] {"3", "4"};
        lstObject.add(obj1);
        lstObject.add(obj2);

        Mockito.doReturn(profileDTO).when(productWs).getProfileByProductOfferId(any());
        Mockito.doReturn(dQuery).when(em).createNativeQuery(any());
        Mockito.doReturn(null).when(dQuery).getResultList();
        baseStockPartnerUpdateSimService.getErrorDetails(sessionId, productOfferId);
    }

    @Test
    public void testGetErrorDetails_7() throws Exception {
        Long productOfferId = 1L;
        String sessionId = "CR123";
        ProfileDTO profileDTO = new ProfileDTO();
        Query dQuery = Mockito.mock(Query.class);
        List lstObject = new ArrayList();

        Mockito.doReturn(profileDTO).when(productWs).getProfileByProductOfferId(any());
        Mockito.doReturn(dQuery).when(em).createNativeQuery(any());
        Mockito.doReturn(lstObject).when(dQuery).getResultList();
        baseStockPartnerUpdateSimService.getErrorDetails(sessionId, productOfferId);
    }
    /**
     * Re-set  value final static variable when runtime
     */
    static void setFinalStatic(Field field, Object newValue) throws Exception {
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(10000L, newValue);
    }
}