package com.viettel.bccs.inventory.service.BaseStock;

import com.google.common.collect.Lists;
import com.viettel.bccs.im1.service.StaffIm1Service;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.exception.HandleException;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.message.BasePartnerMessage;
import com.viettel.bccs.inventory.repo.StockTransRepo;
import com.viettel.bccs.inventory.repo.StockTransSerialRepo;
import com.viettel.bccs.inventory.service.*;
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
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import javax.persistence.EntityManager;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.util.*;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;

/**
 * Created by TruNV on 8/17/2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(BaseStockImpToPartnerBalanceService.class)
public class BaseStockImpToPartnerBalanceServiceTest {
    @InjectMocks
    private BaseStockImpToPartnerBalanceService baseStockImpToPartnerBalanceService;

    @Mock
    private EntityManager em;

    @Mock
    private StockTransActionService stockTransActionService;

    @Mock
    private StockTransDetailService stockTransDetailService;

    @Mock
    private StockTransSerialService stockTransSerialService;

    @Mock
    private StockTransService stockTransService;

    @Mock
    private StaffService staffService;

    @Mock
    private ShopService shopService;

    @Mock
    private StaffIm1Service staffIm1Service;

    @Mock
    private ProductOfferTypeService productOfferTypeService;

    @Mock
    private ProductOfferingService productOfferingService;

    @Mock
    private StockTransRepo stockTransRepo;

    @Mock
    private BaseValidateService baseValidateService;

    @Mock
    private OptionSetValueService optionSetValueService;

    @Mock
    private StockTransSerialRepo stockTransSerialRepo;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockImpToPartnerBalanceService.doPrepareData method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testDoPrepareData_1() throws Exception {
        baseStockImpToPartnerBalanceService.doPrepareData(null, null, null);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockImpToPartnerBalanceService.doValidate method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testDoValidate_1() throws Exception {
        baseStockImpToPartnerBalanceService.doValidate(null, null, null);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockImpToPartnerBalanceService.createTransToPartner method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testCreateTransToPartner_1() throws Exception {
        expectedException.expect(HandleException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);
        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        PowerMockito.doNothing().when(spyTemp, "validateInput", any(), any(), any());

        Mockito.doReturn(new Date()).when(spyTemp).getSysDate(any());

        StockTransDTO stockTransDTOSave = new StockTransDTO();
        Mockito.when(stockTransService.save(any())).thenReturn(stockTransDTOSave);

        StockTransActionDTO stockTransActionDTO1 = new StockTransActionDTO();
        Mockito.when(stockTransActionService.save(any())).thenReturn(stockTransActionDTO1);

        StockTransDetailDTO stockTransDetailDTOSave = new StockTransDetailDTO();
        Mockito.when(stockTransDetailService.save(any())).thenReturn(stockTransDetailDTOSave);

        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue("1");
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        Mockito.when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(null);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        StaffDTO staffDTO = new StaffDTO();
        String typeImport = Const.EXPORT_PARTNER.EXP_BY_QUANTITY;
        String serialList = "";
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        spyTemp.createTransToPartner(stockTransFullDTO, staffDTO, typeImport, serialList, requiredRoleMap);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockImpToPartnerBalanceService.createTransToPartner method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testCreateTransToPartner_2() throws Exception {
        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        PowerMockito.doNothing().when(spyTemp, "validateInput", any(), any(), any());

        Mockito.doReturn(new Date()).when(spyTemp).getSysDate(any());

        StockTransDTO stockTransDTOSave = new StockTransDTO();
        Mockito.when(stockTransService.save(any())).thenReturn(stockTransDTOSave);

        StockTransActionDTO stockTransActionDTO1 = new StockTransActionDTO();
        Mockito.when(stockTransActionService.save(any())).thenReturn(stockTransActionDTO1);

        StockTransDetailDTO stockTransDetailDTOSave = new StockTransDetailDTO();
        Mockito.when(stockTransDetailService.save(any())).thenReturn(stockTransDetailDTOSave);

        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue(null);
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        Mockito.when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setStrQuantity("5");
        StaffDTO staffDTO = new StaffDTO();
        String typeImport = Const.EXPORT_PARTNER.EXP_BY_QUANTITY;
        String serialList = "";
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        Mockito.when(stockTransDetailService.save(any())).thenReturn(stockTransDetailDTOSave);

        PowerMockito.doNothing().when(spyTemp, "doSaveStockTotal", any(), any(), any(), any());
        PowerMockito.doNothing().when(spyTemp, "doIncreaseStockNum", any(), any(), any());

        BasePartnerMessage basePartnerMessage = spyTemp.createTransToPartner(stockTransFullDTO, staffDTO, typeImport, serialList, requiredRoleMap);
        String[] paramsMsg = new String[2];
        paramsMsg[0] = "5";
        paramsMsg[1] = "5";
        Assert.assertEquals("create.partner.balance.sucess.import", basePartnerMessage.getDescription());
        Assert.assertEquals("create.partner.balance.sucess.import", basePartnerMessage.getKeyMsg());
        Assert.assertEquals(paramsMsg.length, basePartnerMessage.getParamsMsg().length);
        Assert.assertEquals(paramsMsg[0], basePartnerMessage.getParamsMsg()[0]);
        Assert.assertEquals(paramsMsg[1], basePartnerMessage.getParamsMsg()[1]);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockImpToPartnerBalanceService.createTransToPartner method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testCreateTransToPartner_3() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);
        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        PowerMockito.doNothing().when(spyTemp, "validateInput", any(), any(), any());

        Mockito.doReturn(new Date()).when(spyTemp).getSysDate(any());

        StockTransDTO stockTransDTOSave = new StockTransDTO();
        Mockito.when(stockTransService.save(any())).thenReturn(stockTransDTOSave);

        StockTransActionDTO stockTransActionDTO1 = new StockTransActionDTO();
        Mockito.when(stockTransActionService.save(any())).thenReturn(stockTransActionDTO1);

        StockTransDetailDTO stockTransDetailDTOSave = new StockTransDetailDTO();
        Mockito.when(stockTransDetailService.save(any())).thenReturn(stockTransDetailDTOSave);

        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue("1");
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        Mockito.when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setStrQuantity("5");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_NO_SERIAL);
        StaffDTO staffDTO = new StaffDTO();
        String typeImport = Const.EXPORT_PARTNER.EXP_BY_SERIAL_RANGE;
        String serialList = "";
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        spyTemp.createTransToPartner(stockTransFullDTO, staffDTO, typeImport, serialList, requiredRoleMap);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockImpToPartnerBalanceService.createTransToPartner method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testCreateTransToPartner_4() throws Exception {
        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        PowerMockito.doNothing().when(spyTemp, "validateInput", any(), any(), any());

        Mockito.doReturn(new Date()).when(spyTemp).getSysDate(any());

        StockTransDTO stockTransDTOSave = new StockTransDTO();
        Mockito.when(stockTransService.save(any())).thenReturn(stockTransDTOSave);

        StockTransActionDTO stockTransActionDTO1 = new StockTransActionDTO();
        Mockito.when(stockTransActionService.save(any())).thenReturn(stockTransActionDTO1);

        StockTransDetailDTO stockTransDetailDTOSave = new StockTransDetailDTO();
        Mockito.when(stockTransDetailService.save(any())).thenReturn(stockTransDetailDTOSave);

        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue("1");
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        Mockito.when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setStrQuantity("5");
        StaffDTO staffDTO = new StaffDTO();
        String typeImport = Const.EXPORT_PARTNER.EXP_BY_SERIAL_RANGE;
        String serialList = "";
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        Map mapValid = Mockito.mock(HashMap.class);
        Mockito.when(mapValid.get(any())).thenReturn(100);
        PowerMockito.doReturn(mapValid).when(spyTemp, "importDataBySerialRange", any(), any(), any(), any(), anyBoolean());

        Mockito.when(stockTransDetailService.save(any())).thenReturn(stockTransDetailDTOSave);

        PowerMockito.doNothing().when(spyTemp, "doSaveStockTotal", any(), any(), any(), any());
        PowerMockito.doNothing().when(spyTemp, "doIncreaseStockNum", any(), any(), any());

        BasePartnerMessage basePartnerMessage = spyTemp.createTransToPartner(stockTransFullDTO, staffDTO, typeImport, serialList, requiredRoleMap);
        String[] paramsMsg = new String[2];
        paramsMsg[0] = "100";
        paramsMsg[1] = "100";
        Assert.assertEquals("create.partner.balance.sucess.import", basePartnerMessage.getDescription());
        Assert.assertEquals("create.partner.balance.sucess.import", basePartnerMessage.getKeyMsg());
        Assert.assertEquals(paramsMsg.length, basePartnerMessage.getParamsMsg().length);
        Assert.assertEquals(paramsMsg[0], basePartnerMessage.getParamsMsg()[0]);
        Assert.assertEquals(paramsMsg[1], basePartnerMessage.getParamsMsg()[1]);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockImpToPartnerBalanceService.createTransToPartner method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testCreateTransToPartner_5() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);
        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        PowerMockito.doNothing().when(spyTemp, "validateInput", any(), any(), any());

        Mockito.doReturn(new Date()).when(spyTemp).getSysDate(any());

        StockTransDTO stockTransDTOSave = new StockTransDTO();
        Mockito.when(stockTransService.save(any())).thenReturn(stockTransDTOSave);

        StockTransActionDTO stockTransActionDTO1 = new StockTransActionDTO();
        Mockito.when(stockTransActionService.save(any())).thenReturn(stockTransActionDTO1);

        StockTransDetailDTO stockTransDetailDTOSave = new StockTransDetailDTO();
        Mockito.when(stockTransDetailService.save(any())).thenReturn(stockTransDetailDTOSave);

        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue("1");
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        Mockito.when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setStrQuantity("5");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_NO_SERIAL);
        StaffDTO staffDTO = new StaffDTO();
        String typeImport = Const.EXPORT_PARTNER.EXP_BY_FILE;
        String serialList = "";
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        spyTemp.createTransToPartner(stockTransFullDTO, staffDTO, typeImport, serialList, requiredRoleMap);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockImpToPartnerBalanceService.createTransToPartner method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testCreateTransToPartner_6() throws Exception {
        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        PowerMockito.doNothing().when(spyTemp, "validateInput", any(), any(), any());

        Mockito.doReturn(new Date()).when(spyTemp).getSysDate(any());

        StockTransDTO stockTransDTOSave = new StockTransDTO();
        Mockito.when(stockTransService.save(any())).thenReturn(stockTransDTOSave);

        StockTransActionDTO stockTransActionDTO1 = new StockTransActionDTO();
        Mockito.when(stockTransActionService.save(any())).thenReturn(stockTransActionDTO1);

        StockTransDetailDTO stockTransDetailDTOSave = new StockTransDetailDTO();
        Mockito.when(stockTransDetailService.save(any())).thenReturn(stockTransDetailDTOSave);

        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue("1");
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        Mockito.when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setStrQuantity("5");
        StaffDTO staffDTO = new StaffDTO();
        String typeImport = Const.EXPORT_PARTNER.EXP_BY_FILE;
        String serialList = "";
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        Map mapValid = Mockito.mock(HashMap.class);
        Mockito.when(mapValid.get(any())).thenReturn(new ArrayList<String>()).thenReturn(100);
        PowerMockito.doReturn(mapValid).when(spyTemp, "importDataByFile", any(), any(), any(), any(), any(), anyBoolean());

        Mockito.when(stockTransDetailService.save(any())).thenReturn(stockTransDetailDTOSave);

        PowerMockito.doNothing().when(spyTemp, "doSaveStockTotal", any(), any(), any(), any());
        PowerMockito.doNothing().when(spyTemp, "doIncreaseStockNum", any(), any(), any());

        BasePartnerMessage basePartnerMessage = spyTemp.createTransToPartner(stockTransFullDTO, staffDTO, typeImport, serialList, requiredRoleMap);
        String[] paramsMsg = new String[2];
        paramsMsg[0] = "100";
        paramsMsg[1] = "100";
        Assert.assertEquals("create.partner.balance.sucess.import", basePartnerMessage.getDescription());
        Assert.assertEquals("create.partner.balance.sucess.import", basePartnerMessage.getKeyMsg());
        Assert.assertEquals(paramsMsg.length, basePartnerMessage.getParamsMsg().length);
        Assert.assertEquals(paramsMsg[0], basePartnerMessage.getParamsMsg()[0]);
        Assert.assertEquals(paramsMsg[1], basePartnerMessage.getParamsMsg()[1]);
    }

    @Test
    public void testCreateTransToPartner_7() throws Exception {
        expectedException.expect(HandleException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);
        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        PowerMockito.doNothing().when(spyTemp, "validateInput", any(), any(), any());

        Mockito.doReturn(new Date()).when(spyTemp).getSysDate(any());

        StockTransDTO stockTransDTOSave = new StockTransDTO();
        Mockito.when(stockTransService.save(any())).thenReturn(stockTransDTOSave);

        StockTransActionDTO stockTransActionDTO1 = new StockTransActionDTO();
        Mockito.when(stockTransActionService.save(any())).thenReturn(stockTransActionDTO1);

        StockTransDetailDTO stockTransDetailDTOSave = new StockTransDetailDTO();
        Mockito.when(stockTransDetailService.save(any())).thenReturn(stockTransDetailDTOSave);

        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue("1");
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        Mockito.when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(null);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        StaffDTO staffDTO = new StaffDTO();
        String typeImport = "223423";
        String serialList = "";
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        spyTemp.createTransToPartner(stockTransFullDTO, staffDTO, typeImport, serialList, requiredRoleMap);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockTransVofficeService.importDataBySerialRange method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testImportDataBySerialRange_1() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        Mockito.when(shopService.findOne(any())).thenReturn(null);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("0");
        stockTransFullDTO.setToSerial("1");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_CARD);
        Date date = new Date();

        PowerMockito.doReturn(1).when(spyTemp, "updateStockX", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean(), anyBoolean());

        PowerMockito.doReturn(2).when(spyTemp, "updateStockXIM1", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean(), anyBoolean());

        Whitebox.invokeMethod(spyTemp, "importDataBySerialRange", 1L, 1L, stockTransFullDTO, date, true);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockTransVofficeService.importDataBySerialRange method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testImportDataBySerialRange_2() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        Mockito.when(shopService.findOne(any())).thenReturn(null);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("0");
        stockTransFullDTO.setToSerial("1");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_CARD);
        Date date = new Date();

        PowerMockito.doReturn(1).when(spyTemp, "updateStockX", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean(), anyBoolean());

        PowerMockito.doReturn(1).when(spyTemp, "updateStockXIM1", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean(), anyBoolean());

        String serialExist = "1";
        String serialExist1 = "2";
        List<String> lsSerialExist = Lists.newArrayList(serialExist, serialExist1);
        Mockito.when(stockTransSerialRepo.getListSerialExist(any(), any(), any(), any(), any(), any(), any())).thenReturn(lsSerialExist);

        Whitebox.invokeMethod(spyTemp, "importDataBySerialRange", 1L, 1L, stockTransFullDTO, date, true);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockTransVofficeService.importDataBySerialRange method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testImportDataBySerialRange_3() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        Mockito.when(shopService.findOne(any())).thenReturn(null);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("0");
        stockTransFullDTO.setToSerial("1");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_CARD);
        Date date = new Date();

        PowerMockito.doReturn(1).when(spyTemp, "updateStockX", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean(), anyBoolean());

        PowerMockito.doReturn(1).when(spyTemp, "updateStockXIM1", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean(), anyBoolean());

        String serialExist = "1";
        List<String> lsSerialExist = Lists.newArrayList(serialExist);
        Mockito.when(stockTransSerialRepo.getListSerialExist(any(), any(), any(), any(), any(), any(), any())).thenReturn(lsSerialExist);

        PowerMockito.doReturn(1).when(spyTemp, "insertStockX", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());
        PowerMockito.doReturn(2).when(spyTemp, "insertStockXIM1", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());

        Whitebox.invokeMethod(spyTemp, "importDataBySerialRange", 1L, 1L, stockTransFullDTO, date, true);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockTransVofficeService.importDataBySerialRange method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testImportDataBySerialRange_4() throws Exception {
        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        Mockito.when(shopService.findOne(any())).thenReturn(null);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("0");
        stockTransFullDTO.setToSerial("1");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_CARD);
        Date date = new Date();

        PowerMockito.doReturn(1).when(spyTemp, "updateStockX", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean(), anyBoolean());

        PowerMockito.doReturn(1).when(spyTemp, "updateStockXIM1", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean(), anyBoolean());

        String serialExist = "1";
        List<String> lsSerialExist = Lists.newArrayList(serialExist);
        Mockito.when(stockTransSerialRepo.getListSerialExist(any(), any(), any(), any(), any(), any(), any())).thenReturn(lsSerialExist);

        PowerMockito.doReturn(1).when(spyTemp, "insertStockX", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());
        PowerMockito.doReturn(1).when(spyTemp, "insertStockXIM1", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());

        setFinalStatic(BaseStockImpToPartnerBalanceService.class.getDeclaredField("HASHMAP_KEY_NUMBER_SUCCESS_RECORD"), 1, spyTemp);
        setFinalStatic(BaseStockImpToPartnerBalanceService.class.getDeclaredField("HASHMAP_KEY_TOTAL_RECORD"), 2, spyTemp);

        Map result = Whitebox.invokeMethod(spyTemp, "importDataBySerialRange", 1L, 1L, stockTransFullDTO, date, true);
        Assert.assertEquals(2, result.get(1));
        Assert.assertEquals(BigInteger.valueOf(2), result.get(2));
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockTransVofficeService.importDataBySerialRange method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testImportDataBySerialRange_5() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);
        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        Mockito.when(shopService.findOne(any())).thenReturn(null);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("0");
        stockTransFullDTO.setToSerial("3");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_HANDSET);
        Date date = new Date();

        PowerMockito.doReturn(2).when(spyTemp, "updateStockX", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean(), anyBoolean());

        PowerMockito.doReturn(1).when(spyTemp, "updateStockXIM1", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean(), anyBoolean());

        String serialExist = "1";
        List<String> lsSerialExist = Lists.newArrayList(serialExist);
        Mockito.when(stockTransSerialRepo.getListSerialExist(any(), any(), any(), any(), any(), any(), any())).thenReturn(lsSerialExist);

        PowerMockito.doReturn(0).when(spyTemp, "insertStockX", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());

        Whitebox.invokeMethod(spyTemp, "importDataBySerialRange", 1L, 1L, stockTransFullDTO, date, false);
    }

    @Test
    public void testImportDataBySerialRange_6() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);
        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        Mockito.when(shopService.findOne(any())).thenReturn(null);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("0");
        stockTransFullDTO.setToSerial("1");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_HANDSET);
        Date date = new Date();

        PowerMockito.doReturn(2).when(spyTemp, "updateStockX", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean(), anyBoolean());

        PowerMockito.doReturn(2).when(spyTemp, "updateStockXIM1", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean(), anyBoolean());

        String serialExist = "1";
        List<String> lsSerialExist = Lists.newArrayList(serialExist);
        Mockito.when(stockTransSerialRepo.getListSerialExist(any(), any(), any(), any(), any(), any(), any())).thenReturn(lsSerialExist);

        PowerMockito.doReturn(1).when(spyTemp, "insertStockX", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());
        PowerMockito.doReturn(0).when(spyTemp, "insertStockXIM1", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());

        setFinalStatic(BaseStockImpToPartnerBalanceService.class.getDeclaredField("HASHMAP_KEY_NUMBER_SUCCESS_RECORD"), 1, spyTemp);
        setFinalStatic(BaseStockImpToPartnerBalanceService.class.getDeclaredField("HASHMAP_KEY_TOTAL_RECORD"), 2, spyTemp);

        Whitebox.invokeMethod(spyTemp, "importDataBySerialRange", 1L, 1L, stockTransFullDTO, date, true);
    }

    @Test
    public void testImportDataBySerialRange_7() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);
        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        Mockito.when(shopService.findOne(any())).thenReturn(null);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("0");
        stockTransFullDTO.setToSerial("1");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_HANDSET);
        Date date = new Date();

        PowerMockito.doReturn(2).when(spyTemp, "updateStockX", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean(), anyBoolean());

        PowerMockito.doReturn(2).when(spyTemp, "updateStockXIM1", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean(), anyBoolean());

        String serialExist = "1";
        List<String> lsSerialExist = Lists.newArrayList(serialExist);
        Mockito.when(stockTransSerialRepo.getListSerialExist(any(), any(), any(), any(), any(), any(), any())).thenReturn(lsSerialExist);

        PowerMockito.doReturn(1).when(spyTemp, "insertStockX", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());
        PowerMockito.doReturn(3).when(spyTemp, "insertStockXIM1", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());

        setFinalStatic(BaseStockImpToPartnerBalanceService.class.getDeclaredField("HASHMAP_KEY_NUMBER_SUCCESS_RECORD"), 1, spyTemp);
        setFinalStatic(BaseStockImpToPartnerBalanceService.class.getDeclaredField("HASHMAP_KEY_TOTAL_RECORD"), 2, spyTemp);

        Whitebox.invokeMethod(spyTemp, "importDataBySerialRange", 1L, 1L, stockTransFullDTO, date, true);
    }

    @Test
    public void testImportDataBySerialRange_8() throws Exception {
        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        Mockito.when(shopService.findOne(any())).thenReturn(null);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("0");
        stockTransFullDTO.setToSerial("1");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_HANDSET);
        Date date = new Date();

        PowerMockito.doReturn(2).when(spyTemp, "updateStockX", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean(), anyBoolean());

        PowerMockito.doReturn(2).when(spyTemp, "updateStockXIM1", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean(), anyBoolean());

        String serialExist = "1";
        List<String> lsSerialExist = Lists.newArrayList(serialExist);
        Mockito.when(stockTransSerialRepo.getListSerialExist(any(), any(), any(), any(), any(), any(), any())).thenReturn(lsSerialExist);

        PowerMockito.doReturn(1).when(spyTemp, "insertStockX", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());
        PowerMockito.doReturn(1).when(spyTemp, "insertStockXIM1", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());

        setFinalStatic(BaseStockImpToPartnerBalanceService.class.getDeclaredField("HASHMAP_KEY_NUMBER_SUCCESS_RECORD"), 1, spyTemp);
        setFinalStatic(BaseStockImpToPartnerBalanceService.class.getDeclaredField("HASHMAP_KEY_TOTAL_RECORD"), 2, spyTemp);

        Map result = Whitebox.invokeMethod(spyTemp, "importDataBySerialRange", 1L, 1L, stockTransFullDTO, date, true);
        Assert.assertEquals(3, result.get(1));
        Assert.assertEquals(BigInteger.valueOf(1), result.get(2));
    }

    @Test
    public void testImportDataBySerialRange_9() throws Exception {
        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        Mockito.when(shopService.findOne(any())).thenReturn(null);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("0");
        stockTransFullDTO.setToSerial("1");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_HANDSET);
        Date date = new Date();

        PowerMockito.doReturn(0).when(spyTemp, "updateStockX", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean(), anyBoolean());

        PowerMockito.doReturn(2).when(spyTemp, "updateStockXIM1", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean(), anyBoolean());

        String serialExist = "1";
        List<String> lsSerialExist = Lists.newArrayList(serialExist);
        Mockito.when(stockTransSerialRepo.getListSerialExist(any(), any(), any(), any(), any(), any(), any())).thenReturn(lsSerialExist);

        PowerMockito.doReturn(1).when(spyTemp, "insertStockX", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());
        PowerMockito.doReturn(1).when(spyTemp, "insertStockXIM1", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());

        setFinalStatic(BaseStockImpToPartnerBalanceService.class.getDeclaredField("HASHMAP_KEY_NUMBER_SUCCESS_RECORD"), 1, spyTemp);
        setFinalStatic(BaseStockImpToPartnerBalanceService.class.getDeclaredField("HASHMAP_KEY_TOTAL_RECORD"), 2, spyTemp);

        Map result = Whitebox.invokeMethod(spyTemp, "importDataBySerialRange", 1L, 1L, stockTransFullDTO, date, false);
        Assert.assertEquals(1, result.get(1));
        Assert.assertEquals(BigInteger.valueOf(1), result.get(2));
    }

    @Test
    public void testImportDataBySerialRange_10() throws Exception {
        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        Mockito.when(shopService.findOne(any())).thenReturn(null);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("0");
        stockTransFullDTO.setToSerial("1");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_HANDSET);
        Date date = new Date();

        PowerMockito.doReturn(2).when(spyTemp, "updateStockX", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean(), anyBoolean());

        PowerMockito.doReturn(2).when(spyTemp, "updateStockXIM1", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean(), anyBoolean());

        String serialExist = "1";
        List<String> lsSerialExist = Lists.newArrayList(serialExist);
        Mockito.when(stockTransSerialRepo.getListSerialExist(any(), any(), any(), any(), any(), any(), any())).thenReturn(lsSerialExist);

        PowerMockito.doReturn(1).when(spyTemp, "insertStockX", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());
        PowerMockito.doReturn(1).when(spyTemp, "insertStockXIM1", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());

        setFinalStatic(BaseStockImpToPartnerBalanceService.class.getDeclaredField("HASHMAP_KEY_NUMBER_SUCCESS_RECORD"), 1, spyTemp);
        setFinalStatic(BaseStockImpToPartnerBalanceService.class.getDeclaredField("HASHMAP_KEY_TOTAL_RECORD"), 2, spyTemp);

        Map result = Whitebox.invokeMethod(spyTemp, "importDataBySerialRange", 1L, 1L, stockTransFullDTO, date, false);
        Assert.assertEquals(3, result.get(1));
        Assert.assertEquals(BigInteger.valueOf(1), result.get(2));
    }

    @Test
    public void testImportDataBySerialRange_11() throws Exception {
        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        Mockito.when(shopService.findOne(any())).thenReturn(null);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("0");
        stockTransFullDTO.setToSerial("1");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_HANDSET);
        Date date = new Date();

        PowerMockito.doReturn(0).when(spyTemp, "updateStockX", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean(), anyBoolean());

        PowerMockito.doReturn(2).when(spyTemp, "updateStockXIM1", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean(), anyBoolean());

        String serialExist = "1";
        List<String> lsSerialExist = Lists.newArrayList(serialExist);
        Mockito.when(stockTransSerialRepo.getListSerialExist(any(), any(), any(), any(), any(), any(), any())).thenReturn(lsSerialExist);

        PowerMockito.doReturn(1).when(spyTemp, "insertStockX", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());
        PowerMockito.doReturn(1).when(spyTemp, "insertStockXIM1", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());

        setFinalStatic(BaseStockImpToPartnerBalanceService.class.getDeclaredField("HASHMAP_KEY_NUMBER_SUCCESS_RECORD"), 1, spyTemp);
        setFinalStatic(BaseStockImpToPartnerBalanceService.class.getDeclaredField("HASHMAP_KEY_TOTAL_RECORD"), 2, spyTemp);

        Map result = Whitebox.invokeMethod(spyTemp, "importDataBySerialRange", 1L, 1L, stockTransFullDTO, date, true);
        Assert.assertEquals(1, result.get(1));
        Assert.assertEquals(BigInteger.valueOf(1), result.get(2));
    }

    @Test
    public void testImportDataBySerialRange_12() throws Exception {
        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        Mockito.when(shopService.findOne(any())).thenReturn(null);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("0");
        stockTransFullDTO.setToSerial("1");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_HANDSET);
        Date date = new Date();

        PowerMockito.doReturn(1).when(spyTemp, "updateStockX", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean(), anyBoolean());

        PowerMockito.doReturn(2).when(spyTemp, "updateStockXIM1", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean(), anyBoolean());

        String serialExist = "1";
        List<String> lsSerialExist = Lists.newArrayList(serialExist);
        Mockito.when(stockTransSerialRepo.getListSerialExist(any(), any(), any(), any(), any(), any(), any())).thenReturn(lsSerialExist);

        PowerMockito.doReturn(1).when(spyTemp, "insertStockX", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());
        PowerMockito.doReturn(1).when(spyTemp, "insertStockXIM1", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());

        setFinalStatic(BaseStockImpToPartnerBalanceService.class.getDeclaredField("HASHMAP_KEY_NUMBER_SUCCESS_RECORD"), 1, spyTemp);
        setFinalStatic(BaseStockImpToPartnerBalanceService.class.getDeclaredField("HASHMAP_KEY_TOTAL_RECORD"), 2, spyTemp);

        Map result = Whitebox.invokeMethod(spyTemp, "importDataBySerialRange", 1L, 1L, stockTransFullDTO, date, false);
        Assert.assertEquals(1, result.get(1));
        Assert.assertEquals(BigInteger.valueOf(1), result.get(2));
    }

    @Test
    public void testImportDataBySerialRange_13() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        Mockito.when(shopService.findOne(any())).thenReturn(null);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("0");
        stockTransFullDTO.setToSerial("1");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_CARD);
        Date date = new Date();

        PowerMockito.doReturn(1).when(spyTemp, "updateStockX", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean(), anyBoolean());

        PowerMockito.doReturn(1).when(spyTemp, "updateStockXIM1", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean(), anyBoolean());

        List<String> lsSerialExist = Lists.newArrayList();
        Mockito.when(stockTransSerialRepo.getListSerialExist(any(), any(), any(), any(), any(), any(), any())).thenReturn(lsSerialExist);

        PowerMockito.doReturn(1).when(spyTemp, "insertStockX", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());
        PowerMockito.doReturn(2).when(spyTemp, "insertStockXIM1", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());

        Whitebox.invokeMethod(spyTemp, "importDataBySerialRange", 1L, 1L, stockTransFullDTO, date, true);
    }

    @Test
    public void testImportDataBySerialRange_14() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        Mockito.when(shopService.findOne(any())).thenReturn(null);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("0");
        stockTransFullDTO.setToSerial("1");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_CARD);
        Date date = new Date();

        PowerMockito.doReturn(1).when(spyTemp, "updateStockX", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean(), anyBoolean());

        PowerMockito.doReturn(1).when(spyTemp, "updateStockXIM1", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean(), anyBoolean());
        String serialExist = "1";
        List<String> lsSerialExist = Lists.newArrayList(serialExist);
        Mockito.when(stockTransSerialRepo.getListSerialExist(any(), any(), any(), any(), any(), any(), any())).thenReturn(lsSerialExist);

        PowerMockito.doReturn(1).when(spyTemp, "insertStockX", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());
        PowerMockito.doReturn(2).when(spyTemp, "insertStockXIM1", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());

        Whitebox.invokeMethod(spyTemp, "importDataBySerialRange", 1L, 1L, stockTransFullDTO, date, true);
    }

    @Test
    public void testImportDataBySerialRange_15() throws Exception {

        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        Mockito.when(shopService.findOne(any())).thenReturn(null);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("0");
        stockTransFullDTO.setToSerial("1");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_CARD);
        Date date = new Date();

        PowerMockito.doReturn(3).when(spyTemp, "updateStockX", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean(), anyBoolean());

        PowerMockito.doReturn(1).when(spyTemp, "updateStockXIM1", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean(), anyBoolean());

        List<String> lsSerialExist = Lists.newArrayList();
        lsSerialExist.add("0");
        lsSerialExist.add("1");
        lsSerialExist.add("3");
        Mockito.when(stockTransSerialRepo.getListSerialExist(any(), any(), any(), any(), any(), any(), any())).thenReturn(lsSerialExist);

        PowerMockito.doReturn(1).when(spyTemp, "insertStockX", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());
        PowerMockito.doReturn(2).when(spyTemp, "insertStockXIM1", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());

        Whitebox.invokeMethod(spyTemp, "importDataBySerialRange", 1L, 1L, stockTransFullDTO, date, false);
    }

    @Test
    public void testImportDataBySerialRange_16() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        Mockito.when(shopService.findOne(any())).thenReturn(null);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("0");
        stockTransFullDTO.setToSerial("1");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_CARD);
        Date date = new Date();

        PowerMockito.doReturn(1).when(spyTemp, "updateStockX", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean(), anyBoolean());

        PowerMockito.doReturn(1).when(spyTemp, "updateStockXIM1", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean(), anyBoolean());
        String serialExist = "1";
        List<String> lsSerialExist = Lists.newArrayList(serialExist);
        Mockito.when(stockTransSerialRepo.getListSerialExist(any(), any(), any(), any(), any(), any(), any())).thenReturn(lsSerialExist);

        PowerMockito.doReturn(1).when(spyTemp, "insertStockX", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());
        PowerMockito.doReturn(2).when(spyTemp, "insertStockXIM1", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());

        Whitebox.invokeMethod(spyTemp, "importDataBySerialRange", 1L, 1L, stockTransFullDTO, date, false);
    }

    @Test
    public void testImportDataBySerialRange_17() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        Mockito.when(shopService.findOne(any())).thenReturn(null);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("0");
        stockTransFullDTO.setToSerial("1");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_CARD);
        Date date = new Date();

        PowerMockito.doReturn(1).when(spyTemp, "updateStockX", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean(), anyBoolean());

        PowerMockito.doReturn(1).when(spyTemp, "updateStockXIM1", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean(), anyBoolean());

        String serialExist = "1";
        List<String> lsSerialExist = Lists.newArrayList(serialExist);
        Mockito.when(stockTransSerialRepo.getListSerialExist(any(), any(), any(), any(), any(), any(), any())).thenReturn(lsSerialExist);

        PowerMockito.doReturn(3).when(spyTemp, "insertStockX", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());
        PowerMockito.doReturn(3).when(spyTemp, "insertStockXIM1", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());

        setFinalStatic(BaseStockImpToPartnerBalanceService.class.getDeclaredField("HASHMAP_KEY_NUMBER_SUCCESS_RECORD"), 1, spyTemp);
        setFinalStatic(BaseStockImpToPartnerBalanceService.class.getDeclaredField("HASHMAP_KEY_TOTAL_RECORD"), 2, spyTemp);

        Map result = Whitebox.invokeMethod(spyTemp, "importDataBySerialRange", 1L, 1L, stockTransFullDTO, date, true);
        Assert.assertEquals(2, result.get(1));
        Assert.assertEquals(BigInteger.valueOf(2), result.get(2));
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockTransVofficeService.importDataByFile method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testImportDataByFile_1() throws Exception {
        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        Mockito.when(shopService.findOne(any())).thenReturn(null);

        Long stockTransId = 1L;
        Long stockTransDetailId = 1L;
        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("0");
        stockTransFullDTO.setToSerial("1");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_CARD);
        Date date = new Date();
        String serialList = "A @  2";
        boolean isCheckIm1 = true;

        PowerMockito.doReturn(1).when(spyTemp, "updateStockX", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean(), anyBoolean());
        PowerMockito.doReturn(2).when(spyTemp, "updateStockXIM1", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean(), anyBoolean());

        setFinalStatic(BaseStockImpToPartnerBalanceService.class.getDeclaredField("HASHMAP_KEY_NUMBER_SUCCESS_RECORD"), 1, spyTemp);
        setFinalStatic(BaseStockImpToPartnerBalanceService.class.getDeclaredField("HASHMAP_KEY_TOTAL_RECORD"), 2, spyTemp);
        setFinalStatic(BaseStockImpToPartnerBalanceService.class.getDeclaredField("HASHMAP_KEY_ERROR_LIST_SERIAL"), 3, spyTemp);

        Map result = Whitebox.invokeMethod(spyTemp, "importDataByFile", stockTransId, stockTransDetailId, stockTransFullDTO, date, serialList, isCheckIm1);
        Assert.assertEquals(0L, result.get(1));
        Assert.assertEquals(3L, result.get(2));
        List<String> errorSerial = (List<String>) result.get(3);
        Assert.assertEquals(3, errorSerial.size());
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockTransVofficeService.importDataByFile method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testImportDataByFile_2() throws Exception {
        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        Mockito.when(shopService.findOne(any())).thenReturn(null);

        Long stockTransId = 1L;
        Long stockTransDetailId = 1L;
        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("0");
        stockTransFullDTO.setToSerial("1");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_CARD);
        Date date = new Date();
        String serialList = "A @  2";
        boolean isCheckIm1 = true;

        PowerMockito.doReturn(2).when(spyTemp, "updateStockX", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean(), anyBoolean());
        PowerMockito.doReturn(3).when(spyTemp, "updateStockXIM1", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean(), anyBoolean());

        PowerMockito.doReturn(2).when(spyTemp, "insertStockX", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());
        PowerMockito.doReturn(2).when(spyTemp, "insertStockXIM1", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());

        setFinalStatic(BaseStockImpToPartnerBalanceService.class.getDeclaredField("HASHMAP_KEY_NUMBER_SUCCESS_RECORD"), 1, spyTemp);
        setFinalStatic(BaseStockImpToPartnerBalanceService.class.getDeclaredField("HASHMAP_KEY_TOTAL_RECORD"), 2, spyTemp);
        setFinalStatic(BaseStockImpToPartnerBalanceService.class.getDeclaredField("HASHMAP_KEY_ERROR_LIST_SERIAL"), 3, spyTemp);

        Map result = Whitebox.invokeMethod(spyTemp, "importDataByFile", stockTransId, stockTransDetailId, stockTransFullDTO, date, serialList, isCheckIm1);
        Assert.assertEquals(0L, result.get(1));
        Assert.assertEquals(3L, result.get(2));
        List<String> errorSerial = (List<String>) result.get(3);
        Assert.assertEquals(3, errorSerial.size());
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockTransVofficeService.importDataByFile method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testImportDataByFile_3() throws Exception {
        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        Mockito.when(shopService.findOne(any())).thenReturn(null);

        Long stockTransId = 1L;
        Long stockTransDetailId = 1L;
        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("0");
        stockTransFullDTO.setToSerial("1");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_CARD);
        Date date = new Date();
        String serialList = "A @  2";
        boolean isCheckIm1 = true;

        PowerMockito.doReturn(2).when(spyTemp, "updateStockX", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean(), anyBoolean());
        PowerMockito.doReturn(3).when(spyTemp, "updateStockXIM1", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean(), anyBoolean());

        PowerMockito.doReturn(1).when(spyTemp, "insertStockX", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());
        PowerMockito.doReturn(2).when(spyTemp, "insertStockXIM1", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());

        setFinalStatic(BaseStockImpToPartnerBalanceService.class.getDeclaredField("HASHMAP_KEY_NUMBER_SUCCESS_RECORD"), 1, spyTemp);
        setFinalStatic(BaseStockImpToPartnerBalanceService.class.getDeclaredField("HASHMAP_KEY_TOTAL_RECORD"), 2, spyTemp);
        setFinalStatic(BaseStockImpToPartnerBalanceService.class.getDeclaredField("HASHMAP_KEY_ERROR_LIST_SERIAL"), 3, spyTemp);

        Map result = Whitebox.invokeMethod(spyTemp, "importDataByFile", stockTransId, stockTransDetailId, stockTransFullDTO, date, serialList, isCheckIm1);
        Assert.assertEquals(0L, result.get(1));
        Assert.assertEquals(3L, result.get(2));
        List<String> errorSerial = (List<String>) result.get(3);
        Assert.assertEquals(3, errorSerial.size());
    }

    @Test
    public void testImportDataByFile_4() throws Exception {
        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        Mockito.when(shopService.findOne(any())).thenReturn(null);

        Long stockTransId = 1L;
        Long stockTransDetailId = 1L;
        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("0");
        stockTransFullDTO.setToSerial("1");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_HANDSET);
        Date date = new Date();
        String serialList = "2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 ";
        boolean isCheckIm1 = true;

        PowerMockito.doReturn(1).when(spyTemp, "updateStockX", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean(), anyBoolean());
        PowerMockito.doReturn(1).when(spyTemp, "updateStockXIM1", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean(), anyBoolean());

        setFinalStatic(BaseStockImpToPartnerBalanceService.class.getDeclaredField("HASHMAP_KEY_NUMBER_SUCCESS_RECORD"), 1, spyTemp);
        setFinalStatic(BaseStockImpToPartnerBalanceService.class.getDeclaredField("HASHMAP_KEY_TOTAL_RECORD"), 2, spyTemp);
        setFinalStatic(BaseStockImpToPartnerBalanceService.class.getDeclaredField("HASHMAP_KEY_ERROR_LIST_SERIAL"), 3, spyTemp);

        Map result = Whitebox.invokeMethod(spyTemp, "importDataByFile", stockTransId, stockTransDetailId, stockTransFullDTO, date, serialList, isCheckIm1);
    }

    @Test
    public void testImportDataByFile_5() throws Exception {
        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        Mockito.when(shopService.findOne(any())).thenReturn(null);

        Long stockTransId = 1L;
        Long stockTransDetailId = 1L;
        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("0");
        stockTransFullDTO.setToSerial("1");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_HANDSET);
        Date date = new Date();
        String serialList = "2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 ";
        boolean isCheckIm1 = false;

        PowerMockito.doReturn(1).when(spyTemp, "updateStockX", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean(), anyBoolean());
        PowerMockito.doReturn(1).when(spyTemp, "updateStockXIM1", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean(), anyBoolean());

        setFinalStatic(BaseStockImpToPartnerBalanceService.class.getDeclaredField("HASHMAP_KEY_NUMBER_SUCCESS_RECORD"), 1, spyTemp);
        setFinalStatic(BaseStockImpToPartnerBalanceService.class.getDeclaredField("HASHMAP_KEY_TOTAL_RECORD"), 2, spyTemp);
        setFinalStatic(BaseStockImpToPartnerBalanceService.class.getDeclaredField("HASHMAP_KEY_ERROR_LIST_SERIAL"), 3, spyTemp);

        Map result = Whitebox.invokeMethod(spyTemp, "importDataByFile", stockTransId, stockTransDetailId, stockTransFullDTO, date, serialList, isCheckIm1);
    }

    @Test
    public void testImportDataByFile_6() throws Exception {
        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        Mockito.when(shopService.findOne(any())).thenReturn(null);

        Long stockTransId = 1L;
        Long stockTransDetailId = 1L;
        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("0");
        stockTransFullDTO.setToSerial("1");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_CARD);
        Date date = new Date();
        String serialList = "A @  2";
        boolean isCheckIm1 = false;

        PowerMockito.doReturn(2).when(spyTemp, "updateStockX", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean(), anyBoolean());
        PowerMockito.doReturn(3).when(spyTemp, "updateStockXIM1", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean(), anyBoolean());

        PowerMockito.doReturn(1).when(spyTemp, "insertStockX", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());
        PowerMockito.doReturn(2).when(spyTemp, "insertStockXIM1", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());

        setFinalStatic(BaseStockImpToPartnerBalanceService.class.getDeclaredField("HASHMAP_KEY_NUMBER_SUCCESS_RECORD"), 1, spyTemp);
        setFinalStatic(BaseStockImpToPartnerBalanceService.class.getDeclaredField("HASHMAP_KEY_TOTAL_RECORD"), 2, spyTemp);
        setFinalStatic(BaseStockImpToPartnerBalanceService.class.getDeclaredField("HASHMAP_KEY_ERROR_LIST_SERIAL"), 3, spyTemp);

        Map result = Whitebox.invokeMethod(spyTemp, "importDataByFile", stockTransId, stockTransDetailId, stockTransFullDTO, date, serialList, isCheckIm1);
    }

    @Test
    public void testImportDataByFile_7() throws Exception {
        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        Mockito.when(shopService.findOne(any())).thenReturn(null);

        Long stockTransId = 1L;
        Long stockTransDetailId = 1L;
        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("0");
        stockTransFullDTO.setToSerial("1");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_CARD);
        Date date = new Date();
        String serialList = "9 0 2";
        boolean isCheckIm1 = true;

        PowerMockito.doReturn(1).when(spyTemp, "updateStockX", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean(), anyBoolean());
        PowerMockito.doReturn(2).when(spyTemp, "updateStockXIM1", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean(), anyBoolean());

        setFinalStatic(BaseStockImpToPartnerBalanceService.class.getDeclaredField("HASHMAP_KEY_NUMBER_SUCCESS_RECORD"), 1, spyTemp);
        setFinalStatic(BaseStockImpToPartnerBalanceService.class.getDeclaredField("HASHMAP_KEY_TOTAL_RECORD"), 2, spyTemp);
        setFinalStatic(BaseStockImpToPartnerBalanceService.class.getDeclaredField("HASHMAP_KEY_ERROR_LIST_SERIAL"), 3, spyTemp);

        Map result = Whitebox.invokeMethod(spyTemp, "importDataByFile", stockTransId, stockTransDetailId, stockTransFullDTO, date, serialList, isCheckIm1);
    }

    @Test
    public void testImportDataByFile_8() throws Exception {
        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        Mockito.when(shopService.findOne(any())).thenReturn(null);

        Long stockTransId = 1L;
        Long stockTransDetailId = 1L;
        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("0");
        stockTransFullDTO.setToSerial("1");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_CARD);
        Date date = new Date();
        String serialList = "111111#@1111 11111111111111111111111111111111111111111111111111111111111111111111 111111111111111111#@11111111111111111111111111111111111111111111111111111111111111 1111111";
        boolean isCheckIm1 = true;

        PowerMockito.doReturn(1).when(spyTemp, "updateStockX", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean(), anyBoolean());
        PowerMockito.doReturn(2).when(spyTemp, "updateStockXIM1", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean(), anyBoolean());

        setFinalStatic(BaseStockImpToPartnerBalanceService.class.getDeclaredField("HASHMAP_KEY_NUMBER_SUCCESS_RECORD"), 1, spyTemp);
        setFinalStatic(BaseStockImpToPartnerBalanceService.class.getDeclaredField("HASHMAP_KEY_TOTAL_RECORD"), 2, spyTemp);
        setFinalStatic(BaseStockImpToPartnerBalanceService.class.getDeclaredField("HASHMAP_KEY_ERROR_LIST_SERIAL"), 3, spyTemp);

        Map result = Whitebox.invokeMethod(spyTemp, "importDataByFile", stockTransId, stockTransDetailId, stockTransFullDTO, date, serialList, isCheckIm1);
        Assert.assertEquals(0L, result.get(1));
        Assert.assertEquals(4L, result.get(2));
    }

    @Test
    public void testImportDataByFile_9() throws Exception {
        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        Mockito.when(shopService.findOne(any())).thenReturn(null);

        Long stockTransId = 1L;
        Long stockTransDetailId = 1L;
        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("0");
        stockTransFullDTO.setToSerial("1");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_CARD);
        Date date = new Date();
        String serialList = "A @  2";
        boolean isCheckIm1 = true;

        PowerMockito.doReturn(2).when(spyTemp, "updateStockX", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean(), anyBoolean());
        PowerMockito.doReturn(3).when(spyTemp, "updateStockXIM1", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), anyBoolean(), anyBoolean());

        PowerMockito.doReturn(1).when(spyTemp, "insertStockX", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());
        PowerMockito.doReturn(1).when(spyTemp, "insertStockXIM1", any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());

        setFinalStatic(BaseStockImpToPartnerBalanceService.class.getDeclaredField("HASHMAP_KEY_NUMBER_SUCCESS_RECORD"), 1, spyTemp);
        setFinalStatic(BaseStockImpToPartnerBalanceService.class.getDeclaredField("HASHMAP_KEY_TOTAL_RECORD"), 2, spyTemp);
        setFinalStatic(BaseStockImpToPartnerBalanceService.class.getDeclaredField("HASHMAP_KEY_ERROR_LIST_SERIAL"), 3, spyTemp);

        Map result = Whitebox.invokeMethod(spyTemp, "importDataByFile", stockTransId, stockTransDetailId, stockTransFullDTO, date, serialList, isCheckIm1);
        Assert.assertEquals(1L, result.get(1));
        Assert.assertEquals(3L, result.get(2));
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockTransVofficeService.validateInput method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testValidateInput_1() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);

        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("0");
        stockTransFullDTO.setToSerial("1");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_CARD);
        String typeExport = Const.EXPORT_PARTNER.EXP_BY_QUANTITY;
        String serialList = "A @  2";

        Whitebox.invokeMethod(spyTemp, "validateInput", stockTransFullDTO, typeExport, serialList);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockTransVofficeService.validateInput method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testValidateInput_2() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);

        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("0");
        stockTransFullDTO.setToSerial("1");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_CARD);
        stockTransFullDTO.setActionCode("1@");
        String typeExport = Const.EXPORT_PARTNER.EXP_BY_QUANTITY;
        String serialList = "A @  2";

        Whitebox.invokeMethod(spyTemp, "validateInput", stockTransFullDTO, typeExport, serialList);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockTransVofficeService.validateInput method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testValidateInput_3() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage("04");

        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("0");
        stockTransFullDTO.setToSerial("1");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_CARD);
        stockTransFullDTO.setActionCode("1");
        String typeExport = Const.EXPORT_PARTNER.EXP_BY_QUANTITY;
        String serialList = "A @  2";

        Mockito.when(shopService.findOne(any())).thenReturn(null);

        Whitebox.invokeMethod(spyTemp, "validateInput", stockTransFullDTO, typeExport, serialList);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockTransVofficeService.validateInput method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testValidateInput_4() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);

        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("0");
        stockTransFullDTO.setToSerial("1");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_CARD);
        stockTransFullDTO.setActionCode("1");
        String typeExport = Const.EXPORT_PARTNER.EXP_BY_QUANTITY;
        String serialList = "A @  2";

        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setStatus(Const.STATUS_ACTIVE);
        Mockito.when(shopService.findOne(any())).thenReturn(shopDTO);

        Mockito.when(productOfferTypeService.findOne(any())).thenReturn(null);

        Whitebox.invokeMethod(spyTemp, "validateInput", stockTransFullDTO, typeExport, serialList);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockTransVofficeService.validateInput method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testValidateInput_5() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);

        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("0");
        stockTransFullDTO.setToSerial("1");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_CARD);
        stockTransFullDTO.setActionCode("1");
        String typeExport = Const.EXPORT_PARTNER.EXP_BY_QUANTITY;
        String serialList = "A @  2";

        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setStatus(Const.STATUS_ACTIVE);
        Mockito.when(shopService.findOne(any())).thenReturn(shopDTO);

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        Mockito.when(productOfferTypeService.findOne(any())).thenReturn(productOfferTypeDTO);

        Whitebox.invokeMethod(spyTemp, "validateInput", stockTransFullDTO, typeExport, serialList);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockTransVofficeService.validateInput method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testValidateInput_6() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);

        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("0");
        stockTransFullDTO.setToSerial("1");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_NO_SERIAL);
        stockTransFullDTO.setActionCode("1");
        String typeExport = Const.EXPORT_PARTNER.EXP_BY_QUANTITY;
        String serialList = "A @  2";

        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setStatus(Const.STATUS_ACTIVE);
        Mockito.when(shopService.findOne(any())).thenReturn(shopDTO);

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        Mockito.when(productOfferTypeService.findOne(any())).thenReturn(productOfferTypeDTO);

        Mockito.when(productOfferingService.findOne(any())).thenReturn(null);

        Whitebox.invokeMethod(spyTemp, "validateInput", stockTransFullDTO, typeExport, serialList);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockTransVofficeService.validateInput method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testValidateInput_7() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);

        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("0");
        stockTransFullDTO.setToSerial("1");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_NO_SERIAL);
        stockTransFullDTO.setActionCode("1");
        String typeExport = Const.EXPORT_PARTNER.EXP_BY_QUANTITY;
        String serialList = "A @  2";

        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setStatus(Const.STATUS_ACTIVE);
        Mockito.when(shopService.findOne(any())).thenReturn(shopDTO);

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        Mockito.when(productOfferTypeService.findOne(any())).thenReturn(productOfferTypeDTO);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus(Const.STATUS.ACTIVE);
        Mockito.when(productOfferingService.findOne(any())).thenReturn(productOfferingDTO);

        Whitebox.invokeMethod(spyTemp, "validateInput", stockTransFullDTO, typeExport, serialList);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockTransVofficeService.validateInput method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testValidateInput_8() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);

        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("0");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_NO_SERIAL);
        stockTransFullDTO.setActionCode("1");
        String typeExport = Const.EXPORT_PARTNER.EXP_BY_SERIAL_RANGE;
        String serialList = "A @  2";

        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setStatus(Const.STATUS_ACTIVE);
        Mockito.when(shopService.findOne(any())).thenReturn(shopDTO);

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        Mockito.when(productOfferTypeService.findOne(any())).thenReturn(productOfferTypeDTO);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus(Const.STATUS.ACTIVE);
        Mockito.when(productOfferingService.findOne(any())).thenReturn(productOfferingDTO);

        Whitebox.invokeMethod(spyTemp, "validateInput", stockTransFullDTO, typeExport, serialList);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockTransVofficeService.validateInput method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testValidateInput_9() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);

        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("0");
        stockTransFullDTO.setToSerial("1");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_HANDSET);
        stockTransFullDTO.setActionCode("1");
        String typeExport = Const.EXPORT_PARTNER.EXP_BY_SERIAL_RANGE;
        String serialList = "A @  2";

        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setStatus(Const.STATUS_ACTIVE);
        Mockito.when(shopService.findOne(any())).thenReturn(shopDTO);

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName("A");
        Mockito.when(productOfferTypeService.findOne(any())).thenReturn(productOfferTypeDTO);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus(Const.STATUS.ACTIVE);
        Mockito.when(productOfferingService.findOne(any())).thenReturn(productOfferingDTO);

        Whitebox.invokeMethod(spyTemp, "validateInput", stockTransFullDTO, typeExport, serialList);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockTransVofficeService.validateInput method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testValidateInput_10() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);

        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("1A");
        stockTransFullDTO.setToSerial("1B");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_CARD);
        stockTransFullDTO.setActionCode("1");
        String typeExport = Const.EXPORT_PARTNER.EXP_BY_SERIAL_RANGE;
        String serialList = "A @  2";

        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setStatus(Const.STATUS_ACTIVE);
        Mockito.when(shopService.findOne(any())).thenReturn(shopDTO);

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName("A");
        Mockito.when(productOfferTypeService.findOne(any())).thenReturn(productOfferTypeDTO);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus(Const.STATUS.ACTIVE);
        Mockito.when(productOfferingService.findOne(any())).thenReturn(productOfferingDTO);

        Whitebox.invokeMethod(spyTemp, "validateInput", stockTransFullDTO, typeExport, serialList);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockTransVofficeService.validateInput method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testValidateInput_11() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);

        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("2");
        stockTransFullDTO.setToSerial("1");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_CARD);
        stockTransFullDTO.setActionCode("1");
        String typeExport = Const.EXPORT_PARTNER.EXP_BY_SERIAL_RANGE;
        String serialList = "A @  2";

        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setStatus(Const.STATUS_ACTIVE);
        Mockito.when(shopService.findOne(any())).thenReturn(shopDTO);

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName("A");
        Mockito.when(productOfferTypeService.findOne(any())).thenReturn(productOfferTypeDTO);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus(Const.STATUS.ACTIVE);
        Mockito.when(productOfferingService.findOne(any())).thenReturn(productOfferingDTO);

        Whitebox.invokeMethod(spyTemp, "validateInput", stockTransFullDTO, typeExport, serialList);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockTransVofficeService.validateInput method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testValidateInput_12() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);

        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("1");
        stockTransFullDTO.setToSerial("2");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_CARD);
        stockTransFullDTO.setActionCode("1");
        stockTransFullDTO.setStrQuantity(null);
        String typeExport = Const.EXPORT_PARTNER.EXP_BY_SERIAL_RANGE;
        String serialList = "A @  2";

        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setStatus(Const.STATUS_ACTIVE);
        Mockito.when(shopService.findOne(any())).thenReturn(shopDTO);

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName("A");
        Mockito.when(productOfferTypeService.findOne(any())).thenReturn(productOfferTypeDTO);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus(Const.STATUS.ACTIVE);
        Mockito.when(productOfferingService.findOne(any())).thenReturn(productOfferingDTO);

        Whitebox.invokeMethod(spyTemp, "validateInput", stockTransFullDTO, typeExport, serialList);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockTransVofficeService.validateInput method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testValidateInput_13() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);

        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("1");
        stockTransFullDTO.setToSerial("2");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_CARD);
        stockTransFullDTO.setActionCode("1");
        stockTransFullDTO.setStrQuantity("3");
        String typeExport = Const.EXPORT_PARTNER.EXP_BY_SERIAL_RANGE;
        String serialList = "A @  2";

        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setStatus(Const.STATUS_ACTIVE);
        Mockito.when(shopService.findOne(any())).thenReturn(shopDTO);

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName("A");
        Mockito.when(productOfferTypeService.findOne(any())).thenReturn(productOfferTypeDTO);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus(Const.STATUS.ACTIVE);
        Mockito.when(productOfferingService.findOne(any())).thenReturn(productOfferingDTO);

        Whitebox.invokeMethod(spyTemp, "validateInput", stockTransFullDTO, typeExport, serialList);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockTransVofficeService.validateInput method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testValidateInput_14() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE);

        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("1");
        stockTransFullDTO.setToSerial("2000");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_CARD);
        stockTransFullDTO.setActionCode("1");
        stockTransFullDTO.setStrQuantity("2000");
        String typeExport = Const.EXPORT_PARTNER.EXP_BY_SERIAL_RANGE;
        String serialList = "A @  2";

        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setStatus(Const.STATUS_ACTIVE);
        Mockito.when(shopService.findOne(any())).thenReturn(shopDTO);

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName("A");
        Mockito.when(productOfferTypeService.findOne(any())).thenReturn(productOfferTypeDTO);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus(Const.STATUS.ACTIVE);
        Mockito.when(productOfferingService.findOne(any())).thenReturn(productOfferingDTO);

        Whitebox.invokeMethod(spyTemp, "validateInput", stockTransFullDTO, typeExport, serialList);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockTransVofficeService.validateInput method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testValidateInput_15() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);

        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("1");
        stockTransFullDTO.setToSerial("1000");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_CARD);
        stockTransFullDTO.setActionCode("1");
        stockTransFullDTO.setStrQuantity("999");
        String typeExport = Const.EXPORT_PARTNER.EXP_BY_FILE;
        String serialList = null;

        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setStatus(Const.STATUS_ACTIVE);
        Mockito.when(shopService.findOne(any())).thenReturn(shopDTO);

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName("A");
        Mockito.when(productOfferTypeService.findOne(any())).thenReturn(productOfferTypeDTO);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus(Const.STATUS.ACTIVE);
        Mockito.when(productOfferingService.findOne(any())).thenReturn(productOfferingDTO);

        Whitebox.invokeMethod(spyTemp, "validateInput", stockTransFullDTO, typeExport, serialList);
    }

    @Test
    public void testValidateInput_16() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage("04");

        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("0");
        stockTransFullDTO.setToSerial("1");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_CARD);
        stockTransFullDTO.setActionCode("1");
        String typeExport = Const.EXPORT_PARTNER.EXP_BY_QUANTITY;
        String serialList = "A @  2";
        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setStatus("2");
        Mockito.when(shopService.findOne(any())).thenReturn(shopDTO);

        Whitebox.invokeMethod(spyTemp, "validateInput", stockTransFullDTO, typeExport, serialList);
    }

    @Test
    public void testValidateInput_17() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);

        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("1");
        stockTransFullDTO.setToSerial("2");
        stockTransFullDTO.setProductOfferTypeId(11L);
        stockTransFullDTO.setActionCode("1");
        stockTransFullDTO.setStrQuantity(null);
        String typeExport = Const.EXPORT_PARTNER.EXP_BY_SERIAL_RANGE;
        String serialList = "A @  2";

        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setStatus(Const.STATUS_ACTIVE);
        Mockito.when(shopService.findOne(any())).thenReturn(shopDTO);

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName("A");
        Mockito.when(productOfferTypeService.findOne(any())).thenReturn(productOfferTypeDTO);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus("2");
        Mockito.when(productOfferingService.findOne(any())).thenReturn(productOfferingDTO);

        Whitebox.invokeMethod(spyTemp, "validateInput", stockTransFullDTO, typeExport, serialList);
    }

    @Test
    public void testValidateInput_18() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);

        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("1");
        stockTransFullDTO.setToSerial("2");
        stockTransFullDTO.setProductOfferTypeId(11L);
        stockTransFullDTO.setActionCode("1");
        stockTransFullDTO.setStrQuantity(null);
        String typeExport = Const.EXPORT_PARTNER.EXP_BY_SERIAL_RANGE;
        String serialList = "A @  2";

        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setStatus(Const.STATUS_ACTIVE);
        Mockito.when(shopService.findOne(any())).thenReturn(shopDTO);

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName(null);
        Mockito.when(productOfferTypeService.findOne(any())).thenReturn(productOfferTypeDTO);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus("2");
        Mockito.when(productOfferingService.findOne(any())).thenReturn(productOfferingDTO);

        Whitebox.invokeMethod(spyTemp, "validateInput", stockTransFullDTO, typeExport, serialList);
    }

    @Test
    public void testValidateInput_19() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);

        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("1");
        stockTransFullDTO.setToSerial("2");
        stockTransFullDTO.setProductOfferTypeId(11L);
        stockTransFullDTO.setActionCode("1");
        stockTransFullDTO.setStrQuantity(null);
        String typeExport = Const.EXPORT_PARTNER.EXP_BY_SERIAL_RANGE;
        String serialList = "A @  2";

        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setStatus(Const.STATUS_ACTIVE);
        Mockito.when(shopService.findOne(any())).thenReturn(shopDTO);

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName("A");
        Mockito.when(productOfferTypeService.findOne(any())).thenReturn(productOfferTypeDTO);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus("2");
        Mockito.when(productOfferingService.findOne(any())).thenReturn(productOfferingDTO);

        Whitebox.invokeMethod(spyTemp, "validateInput", stockTransFullDTO, typeExport, serialList);
    }

    @Test
    public void testValidateInput_20() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);

        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("0");
        stockTransFullDTO.setToSerial("1");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_CARD);
        stockTransFullDTO.setActionCode("111212121212111212121212111212121212111212121212111212121212111212121212111212121212111212121212111212121212111212121212111212121212111212121212");
        String typeExport = Const.EXPORT_PARTNER.EXP_BY_QUANTITY;
        String serialList = "A @  2";

        Whitebox.invokeMethod(spyTemp, "validateInput", stockTransFullDTO, typeExport, serialList);
    }

    @Test
    public void testValidateInput_21() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);

        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("0");
        stockTransFullDTO.setToSerial("1");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_CARD);
        stockTransFullDTO.setActionCode("@11212121212111212121212111212121212111212121212111212121212111212121212111212121212111212121212111212121212111212121212111212121212111212121212");
        String typeExport = Const.EXPORT_PARTNER.EXP_BY_QUANTITY;
        String serialList = "A @  2";

        Whitebox.invokeMethod(spyTemp, "validateInput", stockTransFullDTO, typeExport, serialList);
    }

    @Test
    public void testValidateInput_22() throws Exception {

        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("1");
        stockTransFullDTO.setToSerial("2000");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_CARD);
        stockTransFullDTO.setActionCode("1");
        stockTransFullDTO.setStrQuantity("2000");
        String typeExport = "4";
        String serialList = "A @  2";

        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setStatus(Const.STATUS_ACTIVE);
        Mockito.when(shopService.findOne(any())).thenReturn(shopDTO);

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName("A");
        Mockito.when(productOfferTypeService.findOne(any())).thenReturn(productOfferTypeDTO);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus(Const.STATUS.ACTIVE);
        Mockito.when(productOfferingService.findOne(any())).thenReturn(productOfferingDTO);

        Whitebox.invokeMethod(spyTemp, "validateInput", stockTransFullDTO, typeExport, serialList);
    }

    @Test
    public void testValidateInput_23() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);

        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial(null);
        stockTransFullDTO.setToSerial("0");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_NO_SERIAL);
        stockTransFullDTO.setActionCode("1");
        String typeExport = Const.EXPORT_PARTNER.EXP_BY_SERIAL_RANGE;
        String serialList = "A @  2";

        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setStatus(Const.STATUS_ACTIVE);
        Mockito.when(shopService.findOne(any())).thenReturn(shopDTO);

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        Mockito.when(productOfferTypeService.findOne(any())).thenReturn(productOfferTypeDTO);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus(Const.STATUS.ACTIVE);
        Mockito.when(productOfferingService.findOne(any())).thenReturn(productOfferingDTO);

        Whitebox.invokeMethod(spyTemp, "validateInput", stockTransFullDTO, typeExport, serialList);
    }

    @Test
    public void testValidateInput_24() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);

        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial(null);
        stockTransFullDTO.setToSerial(null);
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_NO_SERIAL);
        stockTransFullDTO.setActionCode("1");
        String typeExport = Const.EXPORT_PARTNER.EXP_BY_SERIAL_RANGE;
        String serialList = "A @  2";

        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setStatus(Const.STATUS_ACTIVE);
        Mockito.when(shopService.findOne(any())).thenReturn(shopDTO);

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        Mockito.when(productOfferTypeService.findOne(any())).thenReturn(productOfferTypeDTO);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus(Const.STATUS.ACTIVE);
        Mockito.when(productOfferingService.findOne(any())).thenReturn(productOfferingDTO);

        Whitebox.invokeMethod(spyTemp, "validateInput", stockTransFullDTO, typeExport, serialList);
    }

    @Test
    public void testValidateInput_25() throws Exception {

        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("0");
        stockTransFullDTO.setToSerial("1");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_NO_SERIAL);
        stockTransFullDTO.setActionCode("1");
        stockTransFullDTO.setStrQuantity("1");
        String typeExport = Const.EXPORT_PARTNER.EXP_BY_QUANTITY;
        String serialList = "A @  2";

        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setStatus(Const.STATUS_ACTIVE);
        Mockito.when(shopService.findOne(any())).thenReturn(shopDTO);

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        Mockito.when(productOfferTypeService.findOne(any())).thenReturn(productOfferTypeDTO);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus(Const.STATUS.ACTIVE);
        Mockito.when(productOfferingService.findOne(any())).thenReturn(productOfferingDTO);

        Whitebox.invokeMethod(spyTemp, "validateInput", stockTransFullDTO, typeExport, serialList);
    }

    @Test
    public void testValidateInput_26() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);

        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("0");
        stockTransFullDTO.setToSerial("1");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_NO_SERIAL);
        stockTransFullDTO.setActionCode("1");
        stockTransFullDTO.setStrQuantity("-1");
        String typeExport = Const.EXPORT_PARTNER.EXP_BY_QUANTITY;
        String serialList = "A @  2";

        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setStatus(Const.STATUS_ACTIVE);
        Mockito.when(shopService.findOne(any())).thenReturn(shopDTO);

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        Mockito.when(productOfferTypeService.findOne(any())).thenReturn(productOfferTypeDTO);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus(Const.STATUS.ACTIVE);
        Mockito.when(productOfferingService.findOne(any())).thenReturn(productOfferingDTO);

        Whitebox.invokeMethod(spyTemp, "validateInput", stockTransFullDTO, typeExport, serialList);
    }

    @Test
    public void testValidateInput_27() throws Exception {

        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("1");
        stockTransFullDTO.setToSerial("1");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_HANDSET);
        stockTransFullDTO.setActionCode("1");
        String typeExport = Const.EXPORT_PARTNER.EXP_BY_SERIAL_RANGE;
        String serialList = "A @  2";

        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setStatus(Const.STATUS_ACTIVE);
        Mockito.when(shopService.findOne(any())).thenReturn(shopDTO);

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName("A");
        Mockito.when(productOfferTypeService.findOne(any())).thenReturn(productOfferTypeDTO);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus(Const.STATUS.ACTIVE);
        Mockito.when(productOfferingService.findOne(any())).thenReturn(productOfferingDTO);

        Whitebox.invokeMethod(spyTemp, "validateInput", stockTransFullDTO, typeExport, serialList);
    }

    @Test
    public void testValidateInput_28() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);

        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("1#@A");
        stockTransFullDTO.setToSerial("1@#B");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_CARD);
        stockTransFullDTO.setActionCode("1");
        String typeExport = Const.EXPORT_PARTNER.EXP_BY_SERIAL_RANGE;
        String serialList = "A @  2";

        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setStatus(Const.STATUS_ACTIVE);
        Mockito.when(shopService.findOne(any())).thenReturn(shopDTO);

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName("A");
        Mockito.when(productOfferTypeService.findOne(any())).thenReturn(productOfferTypeDTO);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus(Const.STATUS.ACTIVE);
        Mockito.when(productOfferingService.findOne(any())).thenReturn(productOfferingDTO);

        Whitebox.invokeMethod(spyTemp, "validateInput", stockTransFullDTO, typeExport, serialList);
    }

    @Test
    public void testValidateInput_29() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);

        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("1#@A");
        stockTransFullDTO.setToSerial("B");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_CARD);
        stockTransFullDTO.setActionCode("1");
        String typeExport = Const.EXPORT_PARTNER.EXP_BY_SERIAL_RANGE;
        String serialList = "A @  2";

        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setStatus(Const.STATUS_ACTIVE);
        Mockito.when(shopService.findOne(any())).thenReturn(shopDTO);

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName("A");
        Mockito.when(productOfferTypeService.findOne(any())).thenReturn(productOfferTypeDTO);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus(Const.STATUS.ACTIVE);
        Mockito.when(productOfferingService.findOne(any())).thenReturn(productOfferingDTO);

        Whitebox.invokeMethod(spyTemp, "validateInput", stockTransFullDTO, typeExport, serialList);
    }

    @Test
    public void testValidateInput_30() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);

        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("A");
        stockTransFullDTO.setToSerial("1#@A");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_CARD);
        stockTransFullDTO.setActionCode("1");
        String typeExport = Const.EXPORT_PARTNER.EXP_BY_SERIAL_RANGE;
        String serialList = "A @  2";

        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setStatus(Const.STATUS_ACTIVE);
        Mockito.when(shopService.findOne(any())).thenReturn(shopDTO);

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName("A");
        Mockito.when(productOfferTypeService.findOne(any())).thenReturn(productOfferTypeDTO);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus(Const.STATUS.ACTIVE);
        Mockito.when(productOfferingService.findOne(any())).thenReturn(productOfferingDTO);

        Whitebox.invokeMethod(spyTemp, "validateInput", stockTransFullDTO, typeExport, serialList);
    }

    @Test
    public void testValidateInput_31() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);

        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("A");
        stockTransFullDTO.setToSerial("A");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_CARD);
        stockTransFullDTO.setActionCode("1");
        String typeExport = Const.EXPORT_PARTNER.EXP_BY_SERIAL_RANGE;
        String serialList = "A @  2";

        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setStatus(Const.STATUS_ACTIVE);
        Mockito.when(shopService.findOne(any())).thenReturn(shopDTO);

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName("A");
        Mockito.when(productOfferTypeService.findOne(any())).thenReturn(productOfferTypeDTO);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus(Const.STATUS.ACTIVE);
        Mockito.when(productOfferingService.findOne(any())).thenReturn(productOfferingDTO);

        Whitebox.invokeMethod(spyTemp, "validateInput", stockTransFullDTO, typeExport, serialList);
    }

    @Test
    public void testValidateInput_32() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);

        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("1");
        stockTransFullDTO.setToSerial("2");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_CARD);
        stockTransFullDTO.setActionCode("1");
        stockTransFullDTO.setStrQuantity("A");
        String typeExport = Const.EXPORT_PARTNER.EXP_BY_SERIAL_RANGE;
        String serialList = "A @  2";

        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setStatus(Const.STATUS_ACTIVE);
        Mockito.when(shopService.findOne(any())).thenReturn(shopDTO);

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName("A");
        Mockito.when(productOfferTypeService.findOne(any())).thenReturn(productOfferTypeDTO);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus(Const.STATUS.ACTIVE);
        Mockito.when(productOfferingService.findOne(any())).thenReturn(productOfferingDTO);

        Whitebox.invokeMethod(spyTemp, "validateInput", stockTransFullDTO, typeExport, serialList);
    }

    @Test
    public void testValidateInput_33() throws Exception {

        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("1A");
        stockTransFullDTO.setToSerial("1B");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_CARD);
        stockTransFullDTO.setActionCode("1");
        String typeExport = "12123";
        String serialList = "A @  2";

        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setStatus(Const.STATUS_ACTIVE);
        Mockito.when(shopService.findOne(any())).thenReturn(shopDTO);

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName("A");
        Mockito.when(productOfferTypeService.findOne(any())).thenReturn(productOfferTypeDTO);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus(Const.STATUS.ACTIVE);
        Mockito.when(productOfferingService.findOne(any())).thenReturn(productOfferingDTO);

        Whitebox.invokeMethod(spyTemp, "validateInput", stockTransFullDTO, typeExport, serialList);
    }

    @Test
    public void testValidateInput_34() throws Exception {

        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("1");
        stockTransFullDTO.setToSerial("1000");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_CARD);
        stockTransFullDTO.setActionCode("1");
        stockTransFullDTO.setStrQuantity("999");
        String typeExport = Const.EXPORT_PARTNER.EXP_BY_FILE;
        String serialList = "22";

        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setStatus(Const.STATUS_ACTIVE);
        Mockito.when(shopService.findOne(any())).thenReturn(shopDTO);

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName("A");
        Mockito.when(productOfferTypeService.findOne(any())).thenReturn(productOfferTypeDTO);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus(Const.STATUS.ACTIVE);
        Mockito.when(productOfferingService.findOne(any())).thenReturn(productOfferingDTO);

        Whitebox.invokeMethod(spyTemp, "validateInput", stockTransFullDTO, typeExport, serialList);
    }

    @Test
    public void testValidateInput_35() throws Exception {

        BaseStockImpToPartnerBalanceService spyTemp = PowerMockito.spy(baseStockImpToPartnerBalanceService);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("1");
        stockTransFullDTO.setToSerial("900");
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_CARD);
        stockTransFullDTO.setActionCode("1");
        stockTransFullDTO.setStrQuantity("900");
        String typeExport = Const.EXPORT_PARTNER.EXP_BY_SERIAL_RANGE;
        String serialList = "A @  2";

        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setStatus(Const.STATUS_ACTIVE);
        Mockito.when(shopService.findOne(any())).thenReturn(shopDTO);

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName("A");
        Mockito.when(productOfferTypeService.findOne(any())).thenReturn(productOfferTypeDTO);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus(Const.STATUS.ACTIVE);
        Mockito.when(productOfferingService.findOne(any())).thenReturn(productOfferingDTO);

        Whitebox.invokeMethod(spyTemp, "validateInput", stockTransFullDTO, typeExport, serialList);
    }

    static void setFinalStatic(Field field, Object newValue, final Object targetObject) throws Exception {
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(targetObject, newValue);
    }
}