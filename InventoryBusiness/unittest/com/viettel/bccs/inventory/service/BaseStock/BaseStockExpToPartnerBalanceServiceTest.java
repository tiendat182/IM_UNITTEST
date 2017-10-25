package com.viettel.bccs.inventory.service.BaseStock;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.message.BasePartnerMessage;
import com.viettel.bccs.inventory.repo.StockTransSerialRepo;
import com.viettel.bccs.inventory.service.*;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.RequiredRoleMap;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.persistence.EntityManager;
import java.util.Date;

/**
 * Created by TruNV on 8/25/2017.
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({DbUtil.class})
public class BaseStockExpToPartnerBalanceServiceTest {

    @InjectMocks
    private BaseStockExpToPartnerBalanceService baseStockExpToPartnerBalanceService;

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
    private ProductOfferTypeService productOfferTypeService;

    @Mock
    private ProductOfferingService productOfferingService;

    @Mock
    private OptionSetValueService optionSetValueService;

    @Mock
    private BaseValidateService baseValidateService;

    @Mock
    private StockTransSerialRepo stockTransSerialRepo;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockExpToPartnerBalanceService.doPrepareData method -- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testDoPrepareData_1() throws Exception {
        baseStockExpToPartnerBalanceService.doPrepareData(null, null, null);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockExpToPartnerBalanceService.doValidate method -- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testDoValidate_1() throws Exception {
        baseStockExpToPartnerBalanceService.doValidate(null, null, null);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockExpToPartnerBalanceService.executeStockTrans method -- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testCreateTransToPartner_1() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_REQUIRE);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setActionCode(null);
        StaffDTO staffDTO = new StaffDTO();
        String typeExport = null;
        String serialList = null;
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        baseStockExpToPartnerBalanceService.createTransToPartner(stockTransFullDTO, staffDTO, typeExport, serialList, requiredRoleMap);
    }

    @Test
    public void testCreateTransToPartner_2() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setActionCode("1234567890123456789012345678901234567890123456789012345678901234567890");
        StaffDTO staffDTO = new StaffDTO();
        String typeExport = null;
        String serialList = null;
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        baseStockExpToPartnerBalanceService.createTransToPartner(stockTransFullDTO, staffDTO, typeExport, serialList, requiredRoleMap);
    }

    @Test
    public void testCreateTransToPartner_3() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setActionCode("123123");

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(7282L);
        String typeExport = null;
        String serialList = null;
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        Mockito.when(staffService.validateTransCode(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).thenReturn(false);
        baseStockExpToPartnerBalanceService.createTransToPartner(stockTransFullDTO, staffDTO, typeExport, serialList, requiredRoleMap);
    }

    @Test
    public void testCreateTransToPartner_4() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setActionCode("123123");

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(7282L);
        String typeExport = null;
        String serialList = null;
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        Mockito.when(staffService.validateTransCode(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).thenReturn(true);
        Mockito.when(productOfferTypeService.findOne(Mockito.anyLong())).thenReturn(null);
        baseStockExpToPartnerBalanceService.createTransToPartner(stockTransFullDTO, staffDTO, typeExport, serialList, requiredRoleMap);
    }

    @Test
    public void testCreateTransToPartner_5() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setActionCode("123123");
        stockTransFullDTO.setProductOfferTypeId(11L);

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(7282L);
        String typeExport = null;
        String serialList = null;
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        Mockito.when(staffService.validateTransCode(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).thenReturn(true);

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setStatus("1");
        productOfferTypeDTO.setProductOfferTypeId(1L);
        productOfferTypeDTO.setTableName(null);
        Mockito.when(productOfferTypeService.findOne(Mockito.anyLong())).thenReturn(productOfferTypeDTO);

        baseStockExpToPartnerBalanceService.createTransToPartner(stockTransFullDTO, staffDTO, typeExport, serialList, requiredRoleMap);
    }

    @Test
    public void testCreateTransToPartner_6() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setActionCode("123123");
        stockTransFullDTO.setProductOfferTypeId(11L);

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(7282L);
        String typeExport = null;
        String serialList = null;
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        Mockito.when(staffService.validateTransCode(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).thenReturn(true);

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setStatus("1");
        productOfferTypeDTO.setTableName(null);
        Mockito.when(productOfferTypeService.findOne(Mockito.anyLong())).thenReturn(productOfferTypeDTO);
        Mockito.when(productOfferingService.findOne(Mockito.anyLong())).thenReturn(null);

        baseStockExpToPartnerBalanceService.createTransToPartner(stockTransFullDTO, staffDTO, typeExport, serialList, requiredRoleMap);
    }

    @Test
    public void testCreateTransToPartner_7() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setActionCode("123123");
        stockTransFullDTO.setProductOfferTypeId(11L);
        stockTransFullDTO.setStrQuantity(null);

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(7282L);
        String typeExport = "1";
        String serialList = null;
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        Mockito.when(staffService.validateTransCode(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).thenReturn(true);

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setStatus("1");
        productOfferTypeDTO.setTableName(null);
        Mockito.when(productOfferTypeService.findOne(Mockito.anyLong())).thenReturn(productOfferTypeDTO);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus("1");
        Mockito.when(productOfferingService.findOne(Mockito.anyLong())).thenReturn(productOfferingDTO);

        baseStockExpToPartnerBalanceService.createTransToPartner(stockTransFullDTO, staffDTO, typeExport, serialList, requiredRoleMap);
    }

    @Test
    public void testCreateTransToPartner_8() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setActionCode("123123");
        stockTransFullDTO.setProductOfferTypeId(11L);
        stockTransFullDTO.setStrQuantity(null);
        stockTransFullDTO.setFromSerial(null);
        stockTransFullDTO.setToSerial(null);

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(7282L);
        String typeExport = "2";
        String serialList = null;
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        Mockito.when(staffService.validateTransCode(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).thenReturn(true);

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setStatus("1");
        productOfferTypeDTO.setTableName(null);
        Mockito.when(productOfferTypeService.findOne(Mockito.anyLong())).thenReturn(productOfferTypeDTO);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus("1");
        Mockito.when(productOfferingService.findOne(Mockito.anyLong())).thenReturn(productOfferingDTO);

        baseStockExpToPartnerBalanceService.createTransToPartner(stockTransFullDTO, staffDTO, typeExport, serialList, requiredRoleMap);
    }

    @Test
    public void testCreateTransToPartner_9() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setActionCode("1234567890123456789012345678901234567890123456789012345678901234567890");
        stockTransFullDTO.setActionCode("â");
        StaffDTO staffDTO = new StaffDTO();
        String typeExport = null;
        String serialList = null;
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        Mockito.when(productOfferTypeService.findOne(Mockito.anyLong())).thenReturn(null);
        baseStockExpToPartnerBalanceService.createTransToPartner(stockTransFullDTO, staffDTO, typeExport, serialList, requiredRoleMap);
    }

    @Test
    public void testCreateTransToPartner_10() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setActionCode("123123");
        stockTransFullDTO.setProductOfferTypeId(123L);

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(7283L);
        String typeExport = null;
        String serialList = null;
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName(null);
        Mockito.when(productOfferTypeService.findOne(Mockito.anyLong())).thenReturn(productOfferTypeDTO);
        Mockito.when(staffService.validateTransCode(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).thenReturn(true);
        baseStockExpToPartnerBalanceService.createTransToPartner(stockTransFullDTO, staffDTO, typeExport, serialList, requiredRoleMap);
    }

    @Test
    public void testCreateTransToPartner_11() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setActionCode("123123");
        stockTransFullDTO.setProductOfferTypeId(123L);

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(7283L);
        String typeExport = null;
        String serialList = null;
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName(null);
        productOfferTypeDTO.setStatus("1");

        Mockito.when(productOfferTypeService.findOne(Mockito.anyLong())).thenReturn(productOfferTypeDTO);
        Mockito.when(staffService.validateTransCode(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).thenReturn(true);
        baseStockExpToPartnerBalanceService.createTransToPartner(stockTransFullDTO, staffDTO, typeExport, serialList, requiredRoleMap);
    }

    @Test
    public void testCreateTransToPartner_12() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setActionCode("123123");
        stockTransFullDTO.setProductOfferTypeId(1L);

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(7283L);
        String typeExport = null;
        String serialList = null;
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName("Name");
        productOfferTypeDTO.setStatus("1");
        Mockito.when(productOfferTypeService.findOne(Mockito.anyLong())).thenReturn(productOfferTypeDTO);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        Mockito.when(productOfferingService.findOne(Mockito.anyLong())).thenReturn(null);

        Mockito.when(staffService.validateTransCode(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).thenReturn(true);
        baseStockExpToPartnerBalanceService.createTransToPartner(stockTransFullDTO, staffDTO, typeExport, serialList, requiredRoleMap);
    }

    @Test
    public void testCreateTransToPartner_13() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setActionCode("123123");
        stockTransFullDTO.setProductOfferTypeId(1L);

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(7283L);
        String typeExport = null;
        String serialList = null;
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName("Name");
        productOfferTypeDTO.setStatus("1");
        Mockito.when(productOfferTypeService.findOne(Mockito.anyLong())).thenReturn(productOfferTypeDTO);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus("2");
        Mockito.when(productOfferingService.findOne(Mockito.anyLong())).thenReturn(productOfferingDTO);

        Mockito.when(staffService.validateTransCode(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).thenReturn(true);
        baseStockExpToPartnerBalanceService.createTransToPartner(stockTransFullDTO, staffDTO, typeExport, serialList, requiredRoleMap);
    }

    @Test
    public void testCreateTransToPartner_14() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setActionCode("123123");
        stockTransFullDTO.setProductOfferTypeId(1L);
        stockTransFullDTO.setStrQuantity("-1");

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(7283L);
        String typeExport = "1";
        String serialList = null;
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName("Name");
        productOfferTypeDTO.setStatus("1");
        Mockito.when(productOfferTypeService.findOne(Mockito.anyLong())).thenReturn(productOfferTypeDTO);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus("1");
        Mockito.when(productOfferingService.findOne(Mockito.anyLong())).thenReturn(productOfferingDTO);

        Mockito.when(staffService.validateTransCode(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).thenReturn(true);
        baseStockExpToPartnerBalanceService.createTransToPartner(stockTransFullDTO, staffDTO, typeExport, serialList, requiredRoleMap);
    }

    @Test
    public void testCreateTransToPartner_15() throws Exception {
        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setActionCode("123123");
        stockTransFullDTO.setProductOfferTypeId(1L);
        stockTransFullDTO.setStrQuantity("222");

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(7283L);
        String typeExport = "1";
        String serialList = null;
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName("Name");
        productOfferTypeDTO.setStatus("1");
        Mockito.when(productOfferTypeService.findOne(Mockito.anyLong())).thenReturn(productOfferTypeDTO);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus("1");
        Mockito.when(productOfferingService.findOne(Mockito.anyLong())).thenReturn(productOfferingDTO);
        Mockito.when(staffService.validateTransCode(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).thenReturn(true);

        PowerMockito.mockStatic(DbUtil.class);
        PowerMockito.when(DbUtil.getSysDate( Mockito.any(EntityManager.class))).thenReturn( new Date());

        StockTransDTO stockTransDTOSave = new StockTransDTO();
        stockTransDTOSave.setStockTransId(1L);
        stockTransDTOSave.setCreateDatetime(new Date());
        Mockito.when(stockTransService.save(Mockito.any(StockTransDTO.class))).thenReturn(stockTransDTOSave);

        StockTransActionDTO stockTransActionDTOSave = new StockTransActionDTO();
        Mockito.when(stockTransActionService.save(Mockito.any(StockTransActionDTO.class))).thenReturn(stockTransActionDTOSave);

        StockTransDetailDTO stockTransDetailDTOSave = new StockTransDetailDTO();
        Mockito.when(stockTransDetailService.save(Mockito.any(StockTransDetailDTO.class))).thenReturn(stockTransDetailDTOSave);

        Mockito.doNothing().when(baseValidateService).doQuantityAvailableValidate(Mockito.any(), Mockito.anyList());
        Mockito.when(stockTransDetailService.save(Mockito.any(StockTransDetailDTO.class))).thenReturn(stockTransDetailDTOSave);

        BaseStockExpToPartnerBalanceService spyTemp = Mockito.spy(baseStockExpToPartnerBalanceService);
        Mockito.doNothing().when(spyTemp).doSaveStockTotal(Mockito.any(), Mockito.anyList(), Mockito.any(), Mockito.any());
        Mockito.doNothing().when(spyTemp).doIncreaseStockNum(Mockito.any(), Mockito.anyString(), Mockito.any());

        BasePartnerMessage basePartnerMessage = spyTemp.createTransToPartner(stockTransFullDTO, staffDTO, typeExport, serialList, requiredRoleMap);
        Assert.assertNotNull(basePartnerMessage);
    }

    @Test
    public void testCreateTransToPartner_16() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setActionCode("123123");
        stockTransFullDTO.setProductOfferTypeId(7L);
        stockTransFullDTO.setStrQuantity("222");
        stockTransFullDTO.setFromSerial("222");
        stockTransFullDTO.setToSerial("333");

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(7283L);
        String typeExport = "2";
        String serialList = null;
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName("Name");
        productOfferTypeDTO.setStatus("1");
        Mockito.when(productOfferTypeService.findOne(Mockito.anyLong())).thenReturn(productOfferTypeDTO);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus("1");
        Mockito.when(productOfferingService.findOne(Mockito.anyLong())).thenReturn(productOfferingDTO);
        Mockito.when(staffService.validateTransCode(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).thenReturn(true);

        PowerMockito.mockStatic(DbUtil.class);
        PowerMockito.when(DbUtil.getSysDate( Mockito.any(EntityManager.class))).thenReturn( new Date());

        StockTransDTO stockTransDTOSave = new StockTransDTO();
        stockTransDTOSave.setStockTransId(1L);
        stockTransDTOSave.setCreateDatetime(new Date());
        Mockito.when(stockTransService.save(Mockito.any(StockTransDTO.class))).thenReturn(stockTransDTOSave);

        StockTransActionDTO stockTransActionDTOSave = new StockTransActionDTO();
        Mockito.when(stockTransActionService.save(Mockito.any(StockTransActionDTO.class))).thenReturn(stockTransActionDTOSave);

        StockTransDetailDTO stockTransDetailDTOSave = new StockTransDetailDTO();
        Mockito.when(stockTransDetailService.save(Mockito.any(StockTransDetailDTO.class))).thenReturn(stockTransDetailDTOSave);

        Mockito.doNothing().when(baseValidateService).doQuantityAvailableValidate(Mockito.any(), Mockito.anyList());
        Mockito.when(stockTransDetailService.save(Mockito.any(StockTransDetailDTO.class))).thenReturn(stockTransDetailDTOSave);

        BaseStockExpToPartnerBalanceService spyTemp = Mockito.spy(baseStockExpToPartnerBalanceService);
        Mockito.doNothing().when(spyTemp).doSaveStockTotal(Mockito.any(), Mockito.anyList(), Mockito.any(), Mockito.any());
        Mockito.doNothing().when(spyTemp).doIncreaseStockNum(Mockito.any(), Mockito.anyString(), Mockito.any());

        spyTemp.createTransToPartner(stockTransFullDTO, staffDTO, typeExport, serialList, requiredRoleMap);
    }

    @Test
    public void testCreateTransToPartner_17() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setActionCode("123123");
        stockTransFullDTO.setProductOfferTypeId(7L);
        stockTransFullDTO.setStrQuantity("222");
        stockTransFullDTO.setFromSerial("222");
        stockTransFullDTO.setToSerial("222");
        stockTransFullDTO.setQuantity(2L);

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(7283L);
        String typeExport = "2";
        String serialList = null;
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName("Name");
        productOfferTypeDTO.setStatus("1");
        Mockito.when(productOfferTypeService.findOne(Mockito.anyLong())).thenReturn(productOfferTypeDTO);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus("1");
        Mockito.when(productOfferingService.findOne(Mockito.anyLong())).thenReturn(productOfferingDTO);
        Mockito.when(staffService.validateTransCode(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).thenReturn(true);

        PowerMockito.mockStatic(DbUtil.class);
        PowerMockito.when(DbUtil.getSysDate( Mockito.any(EntityManager.class))).thenReturn( new Date());

        StockTransDTO stockTransDTOSave = new StockTransDTO();
        stockTransDTOSave.setStockTransId(1L);
        stockTransDTOSave.setCreateDatetime(new Date());
        Mockito.when(stockTransService.save(Mockito.any(StockTransDTO.class))).thenReturn(stockTransDTOSave);

        StockTransActionDTO stockTransActionDTOSave = new StockTransActionDTO();
        Mockito.when(stockTransActionService.save(Mockito.any(StockTransActionDTO.class))).thenReturn(stockTransActionDTOSave);

        StockTransDetailDTO stockTransDetailDTOSave = new StockTransDetailDTO();
        Mockito.when(stockTransDetailService.save(Mockito.any(StockTransDetailDTO.class))).thenReturn(stockTransDetailDTOSave);

        Mockito.doNothing().when(baseValidateService).doQuantityAvailableValidate(Mockito.any(), Mockito.anyList());
        Mockito.when(stockTransDetailService.save(Mockito.any(StockTransDetailDTO.class))).thenReturn(stockTransDetailDTOSave);

        java.util.List<String> valueLst = Lists.newArrayList();
        valueLst.add("1");
        valueLst.add("2");
        valueLst.add("3");
        Mockito.when(stockTransSerialRepo.getListSerialExist(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(valueLst);

        BaseStockExpToPartnerBalanceService spyTemp = Mockito.spy(baseStockExpToPartnerBalanceService);
        Mockito.doNothing().when(spyTemp).doSaveStockTotal(Mockito.any(), Mockito.anyList(), Mockito.any(), Mockito.any());
        Mockito.doNothing().when(spyTemp).doIncreaseStockNum(Mockito.any(), Mockito.anyString(), Mockito.any());

        spyTemp.createTransToPartner(stockTransFullDTO, staffDTO, typeExport, serialList, requiredRoleMap);
    }

    @Test
    public void testCreateTransToPartner_18() throws Exception {
        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setActionCode("123123");
        stockTransFullDTO.setProductOfferTypeId(7L);
        stockTransFullDTO.setStrQuantity("222");
        stockTransFullDTO.setFromSerial("222");
        stockTransFullDTO.setToSerial("222");
        stockTransFullDTO.setQuantity(3L);

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(7283L);
        String typeExport = "2";
        String serialList = null;
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName("Name");
        productOfferTypeDTO.setStatus("1");
        Mockito.when(productOfferTypeService.findOne(Mockito.anyLong())).thenReturn(productOfferTypeDTO);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus("1");
        Mockito.when(productOfferingService.findOne(Mockito.anyLong())).thenReturn(productOfferingDTO);
        Mockito.when(staffService.validateTransCode(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).thenReturn(true);

        PowerMockito.mockStatic(DbUtil.class);
        PowerMockito.when(DbUtil.getSysDate( Mockito.any(EntityManager.class))).thenReturn( new Date());

        StockTransDTO stockTransDTOSave = new StockTransDTO();
        stockTransDTOSave.setStockTransId(1L);
        stockTransDTOSave.setCreateDatetime(new Date());
        Mockito.when(stockTransService.save(Mockito.any(StockTransDTO.class))).thenReturn(stockTransDTOSave);

        StockTransActionDTO stockTransActionDTOSave = new StockTransActionDTO();
        Mockito.when(stockTransActionService.save(Mockito.any(StockTransActionDTO.class))).thenReturn(stockTransActionDTOSave);

        StockTransDetailDTO stockTransDetailDTOSave = new StockTransDetailDTO();
        Mockito.when(stockTransDetailService.save(Mockito.any(StockTransDetailDTO.class))).thenReturn(stockTransDetailDTOSave);

        Mockito.doNothing().when(baseValidateService).doQuantityAvailableValidate(Mockito.any(), Mockito.anyList());
        Mockito.when(stockTransDetailService.save(Mockito.any(StockTransDetailDTO.class))).thenReturn(stockTransDetailDTOSave);

        java.util.List<String> valueLst = Lists.newArrayList();
        valueLst.add("1");
        valueLst.add("2");
        valueLst.add("3");
        Mockito.when(stockTransSerialRepo.getListSerialExist(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(valueLst);

        BaseStockExpToPartnerBalanceService spyTemp = Mockito.spy(baseStockExpToPartnerBalanceService);
        Mockito.doNothing().when(spyTemp).doSaveStockTotal(Mockito.any(), Mockito.anyList(), Mockito.any(), Mockito.any());
        Mockito.doNothing().when(spyTemp).doIncreaseStockNum(Mockito.any(), Mockito.anyString(), Mockito.any());
        Mockito.doNothing().when(spyTemp).doSaveStockGoods(Mockito.any(), Mockito.anyList(), Mockito.any());

        BasePartnerMessage basePartnerMessage = spyTemp.createTransToPartner(stockTransFullDTO, staffDTO, typeExport, serialList, requiredRoleMap);
        Assert.assertNotNull(basePartnerMessage);
    }

    @Test
    public void testCreateTransToPartner_19() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setActionCode("123123");
        stockTransFullDTO.setProductOfferTypeId(6L);
        stockTransFullDTO.setStrQuantity("222");
        stockTransFullDTO.setFromSerial("aaaa");
        stockTransFullDTO.setToSerial("1");
        stockTransFullDTO.setQuantity(3L);

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(7283L);
        String typeExport = "2";
        String serialList = null;
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName("Name");
        productOfferTypeDTO.setStatus("1");
        Mockito.when(productOfferTypeService.findOne(Mockito.anyLong())).thenReturn(productOfferTypeDTO);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus("1");
        Mockito.when(productOfferingService.findOne(Mockito.anyLong())).thenReturn(productOfferingDTO);
        Mockito.when(staffService.validateTransCode(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).thenReturn(true);

        PowerMockito.mockStatic(DbUtil.class);
        PowerMockito.when(DbUtil.getSysDate( Mockito.any(EntityManager.class))).thenReturn( new Date());

        StockTransDTO stockTransDTOSave = new StockTransDTO();
        stockTransDTOSave.setStockTransId(1L);
        stockTransDTOSave.setCreateDatetime(new Date());
        Mockito.when(stockTransService.save(Mockito.any(StockTransDTO.class))).thenReturn(stockTransDTOSave);

        StockTransActionDTO stockTransActionDTOSave = new StockTransActionDTO();
        Mockito.when(stockTransActionService.save(Mockito.any(StockTransActionDTO.class))).thenReturn(stockTransActionDTOSave);

        StockTransDetailDTO stockTransDetailDTOSave = new StockTransDetailDTO();
        Mockito.when(stockTransDetailService.save(Mockito.any(StockTransDetailDTO.class))).thenReturn(stockTransDetailDTOSave);

        Mockito.doNothing().when(baseValidateService).doQuantityAvailableValidate(Mockito.any(), Mockito.anyList());
        Mockito.when(stockTransDetailService.save(Mockito.any(StockTransDetailDTO.class))).thenReturn(stockTransDetailDTOSave);

        java.util.List<String> valueLst = Lists.newArrayList();
        valueLst.add("1");
        valueLst.add("2");
        valueLst.add("3");
        Mockito.when(stockTransSerialRepo.getListSerialExist(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(valueLst);

        BaseStockExpToPartnerBalanceService spyTemp = Mockito.spy(baseStockExpToPartnerBalanceService);
        Mockito.doNothing().when(spyTemp).doSaveStockTotal(Mockito.any(), Mockito.anyList(), Mockito.any(), Mockito.any());
        Mockito.doNothing().when(spyTemp).doIncreaseStockNum(Mockito.any(), Mockito.anyString(), Mockito.any());
        Mockito.doNothing().when(spyTemp).doSaveStockGoods(Mockito.any(), Mockito.anyList(), Mockito.any());

        spyTemp.createTransToPartner(stockTransFullDTO, staffDTO, typeExport, serialList, requiredRoleMap);
    }

    @Test
    public void testCreateTransToPartner_20() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setActionCode("123123");
        stockTransFullDTO.setProductOfferTypeId(6L);
        stockTransFullDTO.setStrQuantity("222");
        stockTransFullDTO.setFromSerial("1");
        stockTransFullDTO.setToSerial("2");
        stockTransFullDTO.setQuantity(3L);

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(7283L);
        String typeExport = "2";
        String serialList = null;
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName("Name");
        productOfferTypeDTO.setStatus("1");
        Mockito.when(productOfferTypeService.findOne(Mockito.anyLong())).thenReturn(productOfferTypeDTO);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus("1");
        Mockito.when(productOfferingService.findOne(Mockito.anyLong())).thenReturn(productOfferingDTO);
        Mockito.when(staffService.validateTransCode(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).thenReturn(true);

        PowerMockito.mockStatic(DbUtil.class);
        PowerMockito.when(DbUtil.getSysDate( Mockito.any(EntityManager.class))).thenReturn( new Date());

        StockTransDTO stockTransDTOSave = new StockTransDTO();
        stockTransDTOSave.setStockTransId(1L);
        stockTransDTOSave.setCreateDatetime(new Date());
        Mockito.when(stockTransService.save(Mockito.any(StockTransDTO.class))).thenReturn(stockTransDTOSave);

        StockTransActionDTO stockTransActionDTOSave = new StockTransActionDTO();
        Mockito.when(stockTransActionService.save(Mockito.any(StockTransActionDTO.class))).thenReturn(stockTransActionDTOSave);

        StockTransDetailDTO stockTransDetailDTOSave = new StockTransDetailDTO();
        Mockito.when(stockTransDetailService.save(Mockito.any(StockTransDetailDTO.class))).thenReturn(stockTransDetailDTOSave);

        Mockito.doNothing().when(baseValidateService).doQuantityAvailableValidate(Mockito.any(), Mockito.anyList());
        Mockito.when(stockTransDetailService.save(Mockito.any(StockTransDetailDTO.class))).thenReturn(stockTransDetailDTOSave);

        java.util.List<String> valueLst = Lists.newArrayList();
        valueLst.add("1");
        valueLst.add("2");
        valueLst.add("3");
        Mockito.when(stockTransSerialRepo.getListSerialExist(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(valueLst);

        BaseStockExpToPartnerBalanceService spyTemp = Mockito.spy(baseStockExpToPartnerBalanceService);
        Mockito.doNothing().when(spyTemp).doSaveStockTotal(Mockito.any(), Mockito.anyList(), Mockito.any(), Mockito.any());
        Mockito.doNothing().when(spyTemp).doIncreaseStockNum(Mockito.any(), Mockito.anyString(), Mockito.any());
        Mockito.doNothing().when(spyTemp).doSaveStockGoods(Mockito.any(), Mockito.anyList(), Mockito.any());

        spyTemp.createTransToPartner(stockTransFullDTO, staffDTO, typeExport, serialList, requiredRoleMap);
    }

    @Test
    public void testCreateTransToPartner_21() throws Exception {
        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setActionCode("123123");
        stockTransFullDTO.setProductOfferTypeId(6L);
        stockTransFullDTO.setStrQuantity("7");
        stockTransFullDTO.setFromSerial("2");
        stockTransFullDTO.setToSerial("8");
        stockTransFullDTO.setQuantity(3L);

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(7283L);
        String typeExport = "2";
        String serialList = null;
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName("Name");
        productOfferTypeDTO.setStatus("1");
        Mockito.when(productOfferTypeService.findOne(Mockito.anyLong())).thenReturn(productOfferTypeDTO);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus("1");
        Mockito.when(productOfferingService.findOne(Mockito.anyLong())).thenReturn(productOfferingDTO);
        Mockito.when(staffService.validateTransCode(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).thenReturn(true);

        PowerMockito.mockStatic(DbUtil.class);
        PowerMockito.when(DbUtil.getSysDate( Mockito.any(EntityManager.class))).thenReturn( new Date());

        StockTransDTO stockTransDTOSave = new StockTransDTO();
        stockTransDTOSave.setStockTransId(1L);
        stockTransDTOSave.setCreateDatetime(new Date());
        Mockito.when(stockTransService.save(Mockito.any(StockTransDTO.class))).thenReturn(stockTransDTOSave);

        StockTransActionDTO stockTransActionDTOSave = new StockTransActionDTO();
        Mockito.when(stockTransActionService.save(Mockito.any(StockTransActionDTO.class))).thenReturn(stockTransActionDTOSave);

        StockTransDetailDTO stockTransDetailDTOSave = new StockTransDetailDTO();
        Mockito.when(stockTransDetailService.save(Mockito.any(StockTransDetailDTO.class))).thenReturn(stockTransDetailDTOSave);

        Mockito.doNothing().when(baseValidateService).doQuantityAvailableValidate(Mockito.any(), Mockito.anyList());
        Mockito.when(stockTransDetailService.save(Mockito.any(StockTransDetailDTO.class))).thenReturn(stockTransDetailDTOSave);

        java.util.List<String> valueLst = Lists.newArrayList();
        valueLst.add("1");
        valueLst.add("2");
        valueLst.add("3");
        Mockito.when(stockTransSerialRepo.getListSerialExist(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(valueLst);

        BaseStockExpToPartnerBalanceService spyTemp = Mockito.spy(baseStockExpToPartnerBalanceService);
        Mockito.doNothing().when(spyTemp).doSaveStockTotal(Mockito.any(), Mockito.anyList(), Mockito.any(), Mockito.any());
        Mockito.doNothing().when(spyTemp).doIncreaseStockNum(Mockito.any(), Mockito.anyString(), Mockito.any());
        Mockito.doNothing().when(spyTemp).doSaveStockGoods(Mockito.any(), Mockito.anyList(), Mockito.any());

        BasePartnerMessage basePartnerMessage = spyTemp.createTransToPartner(stockTransFullDTO, staffDTO, typeExport, serialList, requiredRoleMap);
        Assert.assertNotNull(basePartnerMessage);
    }

    @Test
    public void testCreateTransToPartner_22() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setActionCode("123123");
        stockTransFullDTO.setProductOfferTypeId(6L);
        stockTransFullDTO.setStrQuantity("222");
        stockTransFullDTO.setFromSerial("1");
        stockTransFullDTO.setToSerial("0");
        stockTransFullDTO.setQuantity(3L);

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(7283L);
        String typeExport = "2";
        String serialList = null;
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName("Name");
        productOfferTypeDTO.setStatus("1");
        Mockito.when(productOfferTypeService.findOne(Mockito.anyLong())).thenReturn(productOfferTypeDTO);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus("1");
        Mockito.when(productOfferingService.findOne(Mockito.anyLong())).thenReturn(productOfferingDTO);
        Mockito.when(staffService.validateTransCode(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).thenReturn(true);

        PowerMockito.mockStatic(DbUtil.class);
        PowerMockito.when(DbUtil.getSysDate( Mockito.any(EntityManager.class))).thenReturn( new Date());

        StockTransDTO stockTransDTOSave = new StockTransDTO();
        stockTransDTOSave.setStockTransId(1L);
        stockTransDTOSave.setCreateDatetime(new Date());
        Mockito.when(stockTransService.save(Mockito.any(StockTransDTO.class))).thenReturn(stockTransDTOSave);

        StockTransActionDTO stockTransActionDTOSave = new StockTransActionDTO();
        Mockito.when(stockTransActionService.save(Mockito.any(StockTransActionDTO.class))).thenReturn(stockTransActionDTOSave);

        StockTransDetailDTO stockTransDetailDTOSave = new StockTransDetailDTO();
        Mockito.when(stockTransDetailService.save(Mockito.any(StockTransDetailDTO.class))).thenReturn(stockTransDetailDTOSave);

        Mockito.doNothing().when(baseValidateService).doQuantityAvailableValidate(Mockito.any(), Mockito.anyList());
        Mockito.when(stockTransDetailService.save(Mockito.any(StockTransDetailDTO.class))).thenReturn(stockTransDetailDTOSave);

        java.util.List<String> valueLst = Lists.newArrayList();
        valueLst.add("1");
        valueLst.add("2");
        valueLst.add("3");
        Mockito.when(stockTransSerialRepo.getListSerialExist(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(valueLst);

        BaseStockExpToPartnerBalanceService spyTemp = Mockito.spy(baseStockExpToPartnerBalanceService);
        Mockito.doNothing().when(spyTemp).doSaveStockTotal(Mockito.any(), Mockito.anyList(), Mockito.any(), Mockito.any());
        Mockito.doNothing().when(spyTemp).doIncreaseStockNum(Mockito.any(), Mockito.anyString(), Mockito.any());
        Mockito.doNothing().when(spyTemp).doSaveStockGoods(Mockito.any(), Mockito.anyList(), Mockito.any());

        spyTemp.createTransToPartner(stockTransFullDTO, staffDTO, typeExport, serialList, requiredRoleMap);
    }

    @Test
    public void testCreateTransToPartner_23() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setActionCode("123123");
        stockTransFullDTO.setProductOfferTypeId(6L);
        stockTransFullDTO.setStrQuantity(null);
        stockTransFullDTO.setFromSerial("1");
        stockTransFullDTO.setToSerial("2");
        stockTransFullDTO.setQuantity(3L);

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(7283L);
        String typeExport = "2";
        String serialList = null;
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName("Name");
        productOfferTypeDTO.setStatus("1");
        Mockito.when(productOfferTypeService.findOne(Mockito.anyLong())).thenReturn(productOfferTypeDTO);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus("1");
        Mockito.when(productOfferingService.findOne(Mockito.anyLong())).thenReturn(productOfferingDTO);
        Mockito.when(staffService.validateTransCode(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).thenReturn(true);

        PowerMockito.mockStatic(DbUtil.class);
        PowerMockito.when(DbUtil.getSysDate( Mockito.any(EntityManager.class))).thenReturn( new Date());

        StockTransDTO stockTransDTOSave = new StockTransDTO();
        stockTransDTOSave.setStockTransId(1L);
        stockTransDTOSave.setCreateDatetime(new Date());
        Mockito.when(stockTransService.save(Mockito.any(StockTransDTO.class))).thenReturn(stockTransDTOSave);

        StockTransActionDTO stockTransActionDTOSave = new StockTransActionDTO();
        Mockito.when(stockTransActionService.save(Mockito.any(StockTransActionDTO.class))).thenReturn(stockTransActionDTOSave);

        StockTransDetailDTO stockTransDetailDTOSave = new StockTransDetailDTO();
        Mockito.when(stockTransDetailService.save(Mockito.any(StockTransDetailDTO.class))).thenReturn(stockTransDetailDTOSave);

        Mockito.doNothing().when(baseValidateService).doQuantityAvailableValidate(Mockito.any(), Mockito.anyList());
        Mockito.when(stockTransDetailService.save(Mockito.any(StockTransDetailDTO.class))).thenReturn(stockTransDetailDTOSave);

        java.util.List<String> valueLst = Lists.newArrayList();
        valueLst.add("1");
        valueLst.add("2");
        valueLst.add("3");
        Mockito.when(stockTransSerialRepo.getListSerialExist(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(valueLst);

        BaseStockExpToPartnerBalanceService spyTemp = Mockito.spy(baseStockExpToPartnerBalanceService);
        Mockito.doNothing().when(spyTemp).doSaveStockTotal(Mockito.any(), Mockito.anyList(), Mockito.any(), Mockito.any());
        Mockito.doNothing().when(spyTemp).doIncreaseStockNum(Mockito.any(), Mockito.anyString(), Mockito.any());
        Mockito.doNothing().when(spyTemp).doSaveStockGoods(Mockito.any(), Mockito.anyList(), Mockito.any());

        spyTemp.createTransToPartner(stockTransFullDTO, staffDTO, typeExport, serialList, requiredRoleMap);
    }

    @Test
    public void testCreateTransToPartner_24() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setActionCode("123123");
        stockTransFullDTO.setProductOfferTypeId(6L);
        stockTransFullDTO.setStrQuantity("-1");
        stockTransFullDTO.setFromSerial("1");
        stockTransFullDTO.setToSerial("2");
        stockTransFullDTO.setQuantity(3L);

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(7283L);
        String typeExport = "2";
        String serialList = null;
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName("Name");
        productOfferTypeDTO.setStatus("1");
        Mockito.when(productOfferTypeService.findOne(Mockito.anyLong())).thenReturn(productOfferTypeDTO);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus("1");
        Mockito.when(productOfferingService.findOne(Mockito.anyLong())).thenReturn(productOfferingDTO);
        Mockito.when(staffService.validateTransCode(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).thenReturn(true);

        PowerMockito.mockStatic(DbUtil.class);
        PowerMockito.when(DbUtil.getSysDate( Mockito.any(EntityManager.class))).thenReturn( new Date());

        StockTransDTO stockTransDTOSave = new StockTransDTO();
        stockTransDTOSave.setStockTransId(1L);
        stockTransDTOSave.setCreateDatetime(new Date());
        Mockito.when(stockTransService.save(Mockito.any(StockTransDTO.class))).thenReturn(stockTransDTOSave);

        StockTransActionDTO stockTransActionDTOSave = new StockTransActionDTO();
        Mockito.when(stockTransActionService.save(Mockito.any(StockTransActionDTO.class))).thenReturn(stockTransActionDTOSave);

        StockTransDetailDTO stockTransDetailDTOSave = new StockTransDetailDTO();
        Mockito.when(stockTransDetailService.save(Mockito.any(StockTransDetailDTO.class))).thenReturn(stockTransDetailDTOSave);

        Mockito.doNothing().when(baseValidateService).doQuantityAvailableValidate(Mockito.any(), Mockito.anyList());
        Mockito.when(stockTransDetailService.save(Mockito.any(StockTransDetailDTO.class))).thenReturn(stockTransDetailDTOSave);

        java.util.List<String> valueLst = Lists.newArrayList();
        valueLst.add("1");
        valueLst.add("2");
        valueLst.add("3");
        Mockito.when(stockTransSerialRepo.getListSerialExist(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(valueLst);

        BaseStockExpToPartnerBalanceService spyTemp = Mockito.spy(baseStockExpToPartnerBalanceService);
        Mockito.doNothing().when(spyTemp).doSaveStockTotal(Mockito.any(), Mockito.anyList(), Mockito.any(), Mockito.any());
        Mockito.doNothing().when(spyTemp).doIncreaseStockNum(Mockito.any(), Mockito.anyString(), Mockito.any());
        Mockito.doNothing().when(spyTemp).doSaveStockGoods(Mockito.any(), Mockito.anyList(), Mockito.any());

        spyTemp.createTransToPartner(stockTransFullDTO, staffDTO, typeExport, serialList, requiredRoleMap);
    }

    @Test
    public void testCreateTransToPartner_25() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setActionCode("123123");
        stockTransFullDTO.setProductOfferTypeId(6L);
        stockTransFullDTO.setStrQuantity("0");
        stockTransFullDTO.setFromSerial("1");
        stockTransFullDTO.setToSerial("2");
        stockTransFullDTO.setQuantity(3L);

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(7283L);
        String typeExport = "2";
        String serialList = null;
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName("Name");
        productOfferTypeDTO.setStatus("1");
        Mockito.when(productOfferTypeService.findOne(Mockito.anyLong())).thenReturn(productOfferTypeDTO);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus("1");
        Mockito.when(productOfferingService.findOne(Mockito.anyLong())).thenReturn(productOfferingDTO);
        Mockito.when(staffService.validateTransCode(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).thenReturn(true);

        PowerMockito.mockStatic(DbUtil.class);
        PowerMockito.when(DbUtil.getSysDate( Mockito.any(EntityManager.class))).thenReturn( new Date());

        StockTransDTO stockTransDTOSave = new StockTransDTO();
        stockTransDTOSave.setStockTransId(1L);
        stockTransDTOSave.setCreateDatetime(new Date());
        Mockito.when(stockTransService.save(Mockito.any(StockTransDTO.class))).thenReturn(stockTransDTOSave);

        StockTransActionDTO stockTransActionDTOSave = new StockTransActionDTO();
        Mockito.when(stockTransActionService.save(Mockito.any(StockTransActionDTO.class))).thenReturn(stockTransActionDTOSave);

        StockTransDetailDTO stockTransDetailDTOSave = new StockTransDetailDTO();
        Mockito.when(stockTransDetailService.save(Mockito.any(StockTransDetailDTO.class))).thenReturn(stockTransDetailDTOSave);

        Mockito.doNothing().when(baseValidateService).doQuantityAvailableValidate(Mockito.any(), Mockito.anyList());
        Mockito.when(stockTransDetailService.save(Mockito.any(StockTransDetailDTO.class))).thenReturn(stockTransDetailDTOSave);

        java.util.List<String> valueLst = Lists.newArrayList();
        valueLst.add("1");
        valueLst.add("2");
        valueLst.add("3");
        Mockito.when(stockTransSerialRepo.getListSerialExist(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(valueLst);

        BaseStockExpToPartnerBalanceService spyTemp = Mockito.spy(baseStockExpToPartnerBalanceService);
        Mockito.doNothing().when(spyTemp).doSaveStockTotal(Mockito.any(), Mockito.anyList(), Mockito.any(), Mockito.any());
        Mockito.doNothing().when(spyTemp).doIncreaseStockNum(Mockito.any(), Mockito.anyString(), Mockito.any());
        Mockito.doNothing().when(spyTemp).doSaveStockGoods(Mockito.any(), Mockito.anyList(), Mockito.any());

        spyTemp.createTransToPartner(stockTransFullDTO, staffDTO, typeExport, serialList, requiredRoleMap);
    }

    @Test
    public void testCreateTransToPartner_26() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setActionCode("123123");
        stockTransFullDTO.setProductOfferTypeId(6L);
        stockTransFullDTO.setStrQuantity("500009");
        stockTransFullDTO.setFromSerial("1");
        stockTransFullDTO.setToSerial("500009");
        stockTransFullDTO.setQuantity(3L);

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(7283L);
        String typeExport = "2";
        String serialList = null;
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName("Name");
        productOfferTypeDTO.setStatus("1");
        Mockito.when(productOfferTypeService.findOne(Mockito.anyLong())).thenReturn(productOfferTypeDTO);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus("1");
        Mockito.when(productOfferingService.findOne(Mockito.anyLong())).thenReturn(productOfferingDTO);
        Mockito.when(staffService.validateTransCode(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).thenReturn(true);

        PowerMockito.mockStatic(DbUtil.class);
        PowerMockito.when(DbUtil.getSysDate( Mockito.any(EntityManager.class))).thenReturn( new Date());

        StockTransDTO stockTransDTOSave = new StockTransDTO();
        stockTransDTOSave.setStockTransId(1L);
        stockTransDTOSave.setCreateDatetime(new Date());
        Mockito.when(stockTransService.save(Mockito.any(StockTransDTO.class))).thenReturn(stockTransDTOSave);

        StockTransActionDTO stockTransActionDTOSave = new StockTransActionDTO();
        Mockito.when(stockTransActionService.save(Mockito.any(StockTransActionDTO.class))).thenReturn(stockTransActionDTOSave);

        StockTransDetailDTO stockTransDetailDTOSave = new StockTransDetailDTO();
        Mockito.when(stockTransDetailService.save(Mockito.any(StockTransDetailDTO.class))).thenReturn(stockTransDetailDTOSave);

        Mockito.doNothing().when(baseValidateService).doQuantityAvailableValidate(Mockito.any(), Mockito.anyList());
        Mockito.when(stockTransDetailService.save(Mockito.any(StockTransDetailDTO.class))).thenReturn(stockTransDetailDTOSave);

        java.util.List<String> valueLst = Lists.newArrayList();
        valueLst.add("1");
        valueLst.add("2");
        valueLst.add("3");
        Mockito.when(stockTransSerialRepo.getListSerialExist(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(valueLst);

        BaseStockExpToPartnerBalanceService spyTemp = Mockito.spy(baseStockExpToPartnerBalanceService);
        Mockito.doNothing().when(spyTemp).doSaveStockTotal(Mockito.any(), Mockito.anyList(), Mockito.any(), Mockito.any());
        Mockito.doNothing().when(spyTemp).doIncreaseStockNum(Mockito.any(), Mockito.anyString(), Mockito.any());
        Mockito.doNothing().when(spyTemp).doSaveStockGoods(Mockito.any(), Mockito.anyList(), Mockito.any());

        spyTemp.createTransToPartner(stockTransFullDTO, staffDTO, typeExport, serialList, requiredRoleMap);
    }

    @Test
    public void testCreateTransToPartner_27() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setActionCode("123123");
        stockTransFullDTO.setProductOfferTypeId(6L);
        stockTransFullDTO.setStrQuantity("7");
        stockTransFullDTO.setFromSerial("2");
        stockTransFullDTO.setToSerial("8");
        stockTransFullDTO.setQuantity(3L);

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(7283L);
        String typeExport = "3";
        String serialList = null;
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName("Name");
        productOfferTypeDTO.setStatus("1");
        Mockito.when(productOfferTypeService.findOne(Mockito.anyLong())).thenReturn(productOfferTypeDTO);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus("1");
        Mockito.when(productOfferingService.findOne(Mockito.anyLong())).thenReturn(productOfferingDTO);
        Mockito.when(staffService.validateTransCode(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).thenReturn(true);

        PowerMockito.mockStatic(DbUtil.class);
        PowerMockito.when(DbUtil.getSysDate( Mockito.any(EntityManager.class))).thenReturn( new Date());

        StockTransDTO stockTransDTOSave = new StockTransDTO();
        stockTransDTOSave.setStockTransId(1L);
        stockTransDTOSave.setCreateDatetime(new Date());
        Mockito.when(stockTransService.save(Mockito.any(StockTransDTO.class))).thenReturn(stockTransDTOSave);

        StockTransActionDTO stockTransActionDTOSave = new StockTransActionDTO();
        Mockito.when(stockTransActionService.save(Mockito.any(StockTransActionDTO.class))).thenReturn(stockTransActionDTOSave);

        StockTransDetailDTO stockTransDetailDTOSave = new StockTransDetailDTO();
        Mockito.when(stockTransDetailService.save(Mockito.any(StockTransDetailDTO.class))).thenReturn(stockTransDetailDTOSave);

        Mockito.doNothing().when(baseValidateService).doQuantityAvailableValidate(Mockito.any(), Mockito.anyList());
        Mockito.when(stockTransDetailService.save(Mockito.any(StockTransDetailDTO.class))).thenReturn(stockTransDetailDTOSave);

        java.util.List<String> valueLst = Lists.newArrayList();
        valueLst.add("1");
        valueLst.add("2");
        valueLst.add("3");
        Mockito.when(stockTransSerialRepo.getListSerialExist(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(valueLst);

        BaseStockExpToPartnerBalanceService spyTemp = Mockito.spy(baseStockExpToPartnerBalanceService);
        Mockito.doNothing().when(spyTemp).doSaveStockTotal(Mockito.any(), Mockito.anyList(), Mockito.any(), Mockito.any());
        Mockito.doNothing().when(spyTemp).doIncreaseStockNum(Mockito.any(), Mockito.anyString(), Mockito.any());
        Mockito.doNothing().when(spyTemp).doSaveStockGoods(Mockito.any(), Mockito.anyList(), Mockito.any());

        spyTemp.createTransToPartner(stockTransFullDTO, staffDTO, typeExport, serialList, requiredRoleMap);
    }

    @Test
    public void testCreateTransToPartner_28() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setActionCode("123123");
        stockTransFullDTO.setProductOfferTypeId(7L);
        stockTransFullDTO.setStrQuantity("7");
        stockTransFullDTO.setFromSerial("2");
        stockTransFullDTO.setToSerial("8");
        stockTransFullDTO.setQuantity(3L);

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(7283L);
        String typeExport = "3";
        String serialList = "5:3&6:4";
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName("Name");
        productOfferTypeDTO.setStatus("1");
        Mockito.when(productOfferTypeService.findOne(Mockito.anyLong())).thenReturn(productOfferTypeDTO);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus("1");
        Mockito.when(productOfferingService.findOne(Mockito.anyLong())).thenReturn(productOfferingDTO);
        Mockito.when(staffService.validateTransCode(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).thenReturn(true);

        PowerMockito.mockStatic(DbUtil.class);
        PowerMockito.when(DbUtil.getSysDate( Mockito.any(EntityManager.class))).thenReturn( new Date());

        StockTransDTO stockTransDTOSave = new StockTransDTO();
        stockTransDTOSave.setStockTransId(1L);
        stockTransDTOSave.setCreateDatetime(new Date());
        Mockito.when(stockTransService.save(Mockito.any(StockTransDTO.class))).thenReturn(stockTransDTOSave);

        StockTransActionDTO stockTransActionDTOSave = new StockTransActionDTO();
        Mockito.when(stockTransActionService.save(Mockito.any(StockTransActionDTO.class))).thenReturn(stockTransActionDTOSave);

        StockTransDetailDTO stockTransDetailDTOSave = new StockTransDetailDTO();
        Mockito.when(stockTransDetailService.save(Mockito.any(StockTransDetailDTO.class))).thenReturn(stockTransDetailDTOSave);

        Mockito.doNothing().when(baseValidateService).doQuantityAvailableValidate(Mockito.any(), Mockito.anyList());
        Mockito.when(stockTransDetailService.save(Mockito.any(StockTransDetailDTO.class))).thenReturn(stockTransDetailDTOSave);

        java.util.List<String> valueLst = Lists.newArrayList();
        valueLst.add("1");
        valueLst.add("2");
        valueLst.add("3");
        Mockito.when(stockTransSerialRepo.getListSerialExist(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(valueLst);

        BaseStockExpToPartnerBalanceService spyTemp = Mockito.spy(baseStockExpToPartnerBalanceService);
        Mockito.doNothing().when(spyTemp).doSaveStockTotal(Mockito.any(), Mockito.anyList(), Mockito.any(), Mockito.any());
        Mockito.doNothing().when(spyTemp).doIncreaseStockNum(Mockito.any(), Mockito.anyString(), Mockito.any());
        Mockito.doNothing().when(spyTemp).doSaveStockGoods(Mockito.any(), Mockito.anyList(), Mockito.any());

        spyTemp.createTransToPartner(stockTransFullDTO, staffDTO, typeExport, serialList, requiredRoleMap);
    }

    @Test
    public void testCreateTransToPartner_29() throws Exception {
        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setActionCode("123123");
        stockTransFullDTO.setProductOfferTypeId(7L);
        stockTransFullDTO.setStrQuantity("7");
        stockTransFullDTO.setFromSerial("2");
        stockTransFullDTO.setToSerial("8");
        stockTransFullDTO.setQuantity(1L);

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(7283L);
        String typeExport = "3";
        String serialList = "5:3&6:4";
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName("Name");
        productOfferTypeDTO.setStatus("1");
        Mockito.when(productOfferTypeService.findOne(Mockito.anyLong())).thenReturn(productOfferTypeDTO);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus("1");
        Mockito.when(productOfferingService.findOne(Mockito.anyLong())).thenReturn(productOfferingDTO);
        Mockito.when(staffService.validateTransCode(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).thenReturn(true);

        PowerMockito.mockStatic(DbUtil.class);
        PowerMockito.when(DbUtil.getSysDate( Mockito.any(EntityManager.class))).thenReturn( new Date());

        StockTransDTO stockTransDTOSave = new StockTransDTO();
        stockTransDTOSave.setStockTransId(1L);
        stockTransDTOSave.setCreateDatetime(new Date());
        Mockito.when(stockTransService.save(Mockito.any(StockTransDTO.class))).thenReturn(stockTransDTOSave);

        StockTransActionDTO stockTransActionDTOSave = new StockTransActionDTO();
        Mockito.when(stockTransActionService.save(Mockito.any(StockTransActionDTO.class))).thenReturn(stockTransActionDTOSave);

        StockTransDetailDTO stockTransDetailDTOSave = new StockTransDetailDTO();
        Mockito.when(stockTransDetailService.save(Mockito.any(StockTransDetailDTO.class))).thenReturn(stockTransDetailDTOSave);

        Mockito.doNothing().when(baseValidateService).doQuantityAvailableValidate(Mockito.any(), Mockito.anyList());
        Mockito.when(stockTransDetailService.save(Mockito.any(StockTransDetailDTO.class))).thenReturn(stockTransDetailDTOSave);

        java.util.List<String> valueLst = Lists.newArrayList();
        valueLst.add("1");
        Mockito.when(stockTransSerialRepo.getListSerialExist(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(valueLst);

        BaseStockExpToPartnerBalanceService spyTemp = Mockito.spy(baseStockExpToPartnerBalanceService);
        Mockito.doNothing().when(spyTemp).doSaveStockTotal(Mockito.any(), Mockito.anyList(), Mockito.any(), Mockito.any());
        Mockito.doNothing().when(spyTemp).doIncreaseStockNum(Mockito.any(), Mockito.anyString(), Mockito.any());
        Mockito.doNothing().when(spyTemp).doSaveStockGoods(Mockito.any(), Mockito.anyList(), Mockito.any());

        spyTemp.createTransToPartner(stockTransFullDTO, staffDTO, typeExport, serialList, requiredRoleMap);
    }

    @Test
    public void testCreateTransToPartner_30() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setActionCode("123123");
        stockTransFullDTO.setProductOfferTypeId(6L);
        stockTransFullDTO.setStrQuantity("7");
        stockTransFullDTO.setFromSerial("2");
        stockTransFullDTO.setToSerial("8");
        stockTransFullDTO.setQuantity(1L);

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(7283L);
        String typeExport = "3";
        String serialList = "5:3&6:4";
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName("Name");
        productOfferTypeDTO.setStatus("1");
        Mockito.when(productOfferTypeService.findOne(Mockito.anyLong())).thenReturn(productOfferTypeDTO);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus("1");
        Mockito.when(productOfferingService.findOne(Mockito.anyLong())).thenReturn(productOfferingDTO);
        Mockito.when(staffService.validateTransCode(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).thenReturn(true);

        PowerMockito.mockStatic(DbUtil.class);
        PowerMockito.when(DbUtil.getSysDate( Mockito.any(EntityManager.class))).thenReturn( new Date());

        StockTransDTO stockTransDTOSave = new StockTransDTO();
        stockTransDTOSave.setStockTransId(1L);
        stockTransDTOSave.setCreateDatetime(new Date());
        Mockito.when(stockTransService.save(Mockito.any(StockTransDTO.class))).thenReturn(stockTransDTOSave);

        StockTransActionDTO stockTransActionDTOSave = new StockTransActionDTO();
        Mockito.when(stockTransActionService.save(Mockito.any(StockTransActionDTO.class))).thenReturn(stockTransActionDTOSave);

        StockTransDetailDTO stockTransDetailDTOSave = new StockTransDetailDTO();
        Mockito.when(stockTransDetailService.save(Mockito.any(StockTransDetailDTO.class))).thenReturn(stockTransDetailDTOSave);

        Mockito.doNothing().when(baseValidateService).doQuantityAvailableValidate(Mockito.any(), Mockito.anyList());
        Mockito.when(stockTransDetailService.save(Mockito.any(StockTransDetailDTO.class))).thenReturn(stockTransDetailDTOSave);

        java.util.List<String> valueLst = Lists.newArrayList();
        valueLst.add("1");
        Mockito.when(stockTransSerialRepo.getListSerialExist(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(valueLst);

        BaseStockExpToPartnerBalanceService spyTemp = Mockito.spy(baseStockExpToPartnerBalanceService);
        Mockito.doNothing().when(spyTemp).doSaveStockTotal(Mockito.any(), Mockito.anyList(), Mockito.any(), Mockito.any());
        Mockito.doNothing().when(spyTemp).doIncreaseStockNum(Mockito.any(), Mockito.anyString(), Mockito.any());
        Mockito.doNothing().when(spyTemp).doSaveStockGoods(Mockito.any(), Mockito.anyList(), Mockito.any());

        spyTemp.createTransToPartner(stockTransFullDTO, staffDTO, typeExport, serialList, requiredRoleMap);
    }

    @Test
    public void testCreateTransToPartner_31() throws Exception {
        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setActionCode("123123");
        stockTransFullDTO.setProductOfferTypeId(6L);
        stockTransFullDTO.setStrQuantity("7");
        stockTransFullDTO.setFromSerial("2");
        stockTransFullDTO.setToSerial("8");
        stockTransFullDTO.setQuantity(1L);

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(7283L);
        String typeExport = "6";
        String serialList = "5:3&6:4";
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName("Name");
        productOfferTypeDTO.setStatus("1");
        Mockito.when(productOfferTypeService.findOne(Mockito.anyLong())).thenReturn(productOfferTypeDTO);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus("1");
        Mockito.when(productOfferingService.findOne(Mockito.anyLong())).thenReturn(productOfferingDTO);
        Mockito.when(staffService.validateTransCode(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).thenReturn(true);

        PowerMockito.mockStatic(DbUtil.class);
        PowerMockito.when(DbUtil.getSysDate( Mockito.any(EntityManager.class))).thenReturn( new Date());

        StockTransDTO stockTransDTOSave = new StockTransDTO();
        stockTransDTOSave.setStockTransId(1L);
        stockTransDTOSave.setCreateDatetime(new Date());
        Mockito.when(stockTransService.save(Mockito.any(StockTransDTO.class))).thenReturn(stockTransDTOSave);

        StockTransActionDTO stockTransActionDTOSave = new StockTransActionDTO();
        Mockito.when(stockTransActionService.save(Mockito.any(StockTransActionDTO.class))).thenReturn(stockTransActionDTOSave);

        StockTransDetailDTO stockTransDetailDTOSave = new StockTransDetailDTO();
        Mockito.when(stockTransDetailService.save(Mockito.any(StockTransDetailDTO.class))).thenReturn(stockTransDetailDTOSave);

        Mockito.doNothing().when(baseValidateService).doQuantityAvailableValidate(Mockito.any(), Mockito.anyList());
        Mockito.when(stockTransDetailService.save(Mockito.any(StockTransDetailDTO.class))).thenReturn(stockTransDetailDTOSave);

        java.util.List<String> valueLst = Lists.newArrayList();
        valueLst.add("1");
        Mockito.when(stockTransSerialRepo.getListSerialExist(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(valueLst);

        BaseStockExpToPartnerBalanceService spyTemp = Mockito.spy(baseStockExpToPartnerBalanceService);
        Mockito.doNothing().when(spyTemp).doSaveStockTotal(Mockito.any(), Mockito.anyList(), Mockito.any(), Mockito.any());
        Mockito.doNothing().when(spyTemp).doIncreaseStockNum(Mockito.any(), Mockito.anyString(), Mockito.any());
        Mockito.doNothing().when(spyTemp).doSaveStockGoods(Mockito.any(), Mockito.anyList(), Mockito.any());

        BasePartnerMessage basePartnerMessage = spyTemp.createTransToPartner(stockTransFullDTO, staffDTO, typeExport, serialList, requiredRoleMap);
        Assert.assertNotNull(basePartnerMessage);
    }

    @Test
    public void testCreateTransToPartner_32() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setActionCode("123123");
        stockTransFullDTO.setProductOfferTypeId(11L);
        stockTransFullDTO.setStrQuantity("2");
        stockTransFullDTO.setFromSerial("2");
        stockTransFullDTO.setToSerial(null);

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(7282L);
        String typeExport = "2";
        String serialList = null;
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        Mockito.when(staffService.validateTransCode(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).thenReturn(true);

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setStatus("1");
        productOfferTypeDTO.setTableName(null);
        Mockito.when(productOfferTypeService.findOne(Mockito.anyLong())).thenReturn(productOfferTypeDTO);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus("1");
        Mockito.when(productOfferingService.findOne(Mockito.anyLong())).thenReturn(productOfferingDTO);

        baseStockExpToPartnerBalanceService.createTransToPartner(stockTransFullDTO, staffDTO, typeExport, serialList, requiredRoleMap);
    }

    @Test
    public void testCreateTransToPartner_33() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setActionCode("123123");
        stockTransFullDTO.setProductOfferTypeId(11L);
        stockTransFullDTO.setStrQuantity("2");
        stockTransFullDTO.setFromSerial(null);
        stockTransFullDTO.setToSerial("2");

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(7282L);
        String typeExport = "2";
        String serialList = null;
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        Mockito.when(staffService.validateTransCode(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).thenReturn(true);

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setStatus("1");
        productOfferTypeDTO.setTableName(null);
        Mockito.when(productOfferTypeService.findOne(Mockito.anyLong())).thenReturn(productOfferTypeDTO);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus("1");
        Mockito.when(productOfferingService.findOne(Mockito.anyLong())).thenReturn(productOfferingDTO);

        baseStockExpToPartnerBalanceService.createTransToPartner(stockTransFullDTO, staffDTO, typeExport, serialList, requiredRoleMap);
    }

    @Test
    public void testCreateTransToPartner_34() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setActionCode("123123");
        stockTransFullDTO.setProductOfferTypeId(6L);
        stockTransFullDTO.setStrQuantity("222");
        stockTransFullDTO.setFromSerial("1");
        stockTransFullDTO.setToSerial("aaa");
        stockTransFullDTO.setQuantity(3L);

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(7283L);
        String typeExport = "2";
        String serialList = null;
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName("Name");
        productOfferTypeDTO.setStatus("1");
        Mockito.when(productOfferTypeService.findOne(Mockito.anyLong())).thenReturn(productOfferTypeDTO);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus("1");
        Mockito.when(productOfferingService.findOne(Mockito.anyLong())).thenReturn(productOfferingDTO);
        Mockito.when(staffService.validateTransCode(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).thenReturn(true);

        PowerMockito.mockStatic(DbUtil.class);
        PowerMockito.when(DbUtil.getSysDate( Mockito.any(EntityManager.class))).thenReturn( new Date());

        StockTransDTO stockTransDTOSave = new StockTransDTO();
        stockTransDTOSave.setStockTransId(1L);
        stockTransDTOSave.setCreateDatetime(new Date());
        Mockito.when(stockTransService.save(Mockito.any(StockTransDTO.class))).thenReturn(stockTransDTOSave);

        StockTransActionDTO stockTransActionDTOSave = new StockTransActionDTO();
        Mockito.when(stockTransActionService.save(Mockito.any(StockTransActionDTO.class))).thenReturn(stockTransActionDTOSave);

        StockTransDetailDTO stockTransDetailDTOSave = new StockTransDetailDTO();
        Mockito.when(stockTransDetailService.save(Mockito.any(StockTransDetailDTO.class))).thenReturn(stockTransDetailDTOSave);

        Mockito.doNothing().when(baseValidateService).doQuantityAvailableValidate(Mockito.any(), Mockito.anyList());
        Mockito.when(stockTransDetailService.save(Mockito.any(StockTransDetailDTO.class))).thenReturn(stockTransDetailDTOSave);

        java.util.List<String> valueLst = Lists.newArrayList();
        valueLst.add("1");
        valueLst.add("2");
        valueLst.add("3");
        Mockito.when(stockTransSerialRepo.getListSerialExist(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(valueLst);

        BaseStockExpToPartnerBalanceService spyTemp = Mockito.spy(baseStockExpToPartnerBalanceService);
        Mockito.doNothing().when(spyTemp).doSaveStockTotal(Mockito.any(), Mockito.anyList(), Mockito.any(), Mockito.any());
        Mockito.doNothing().when(spyTemp).doIncreaseStockNum(Mockito.any(), Mockito.anyString(), Mockito.any());
        Mockito.doNothing().when(spyTemp).doSaveStockGoods(Mockito.any(), Mockito.anyList(), Mockito.any());

        spyTemp.createTransToPartner(stockTransFullDTO, staffDTO, typeExport, serialList, requiredRoleMap);
    }

    @Test
    public void testCreateTransToPartner_35() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setActionCode("123123");
        stockTransFullDTO.setProductOfferTypeId(6L);
        stockTransFullDTO.setStrQuantity("222");
        stockTransFullDTO.setFromSerial("aaa");
        stockTransFullDTO.setToSerial("aaa");
        stockTransFullDTO.setQuantity(3L);

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(7283L);
        String typeExport = "2";
        String serialList = null;
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName("Name");
        productOfferTypeDTO.setStatus("1");
        Mockito.when(productOfferTypeService.findOne(Mockito.anyLong())).thenReturn(productOfferTypeDTO);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus("1");
        Mockito.when(productOfferingService.findOne(Mockito.anyLong())).thenReturn(productOfferingDTO);
        Mockito.when(staffService.validateTransCode(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).thenReturn(true);

        PowerMockito.mockStatic(DbUtil.class);
        PowerMockito.when(DbUtil.getSysDate( Mockito.any(EntityManager.class))).thenReturn( new Date());

        StockTransDTO stockTransDTOSave = new StockTransDTO();
        stockTransDTOSave.setStockTransId(1L);
        stockTransDTOSave.setCreateDatetime(new Date());
        Mockito.when(stockTransService.save(Mockito.any(StockTransDTO.class))).thenReturn(stockTransDTOSave);

        StockTransActionDTO stockTransActionDTOSave = new StockTransActionDTO();
        Mockito.when(stockTransActionService.save(Mockito.any(StockTransActionDTO.class))).thenReturn(stockTransActionDTOSave);

        StockTransDetailDTO stockTransDetailDTOSave = new StockTransDetailDTO();
        Mockito.when(stockTransDetailService.save(Mockito.any(StockTransDetailDTO.class))).thenReturn(stockTransDetailDTOSave);

        Mockito.doNothing().when(baseValidateService).doQuantityAvailableValidate(Mockito.any(), Mockito.anyList());
        Mockito.when(stockTransDetailService.save(Mockito.any(StockTransDetailDTO.class))).thenReturn(stockTransDetailDTOSave);

        java.util.List<String> valueLst = Lists.newArrayList();
        valueLst.add("1");
        valueLst.add("2");
        valueLst.add("3");
        Mockito.when(stockTransSerialRepo.getListSerialExist(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(valueLst);

        BaseStockExpToPartnerBalanceService spyTemp = Mockito.spy(baseStockExpToPartnerBalanceService);
        Mockito.doNothing().when(spyTemp).doSaveStockTotal(Mockito.any(), Mockito.anyList(), Mockito.any(), Mockito.any());
        Mockito.doNothing().when(spyTemp).doIncreaseStockNum(Mockito.any(), Mockito.anyString(), Mockito.any());
        Mockito.doNothing().when(spyTemp).doSaveStockGoods(Mockito.any(), Mockito.anyList(), Mockito.any());

        spyTemp.createTransToPartner(stockTransFullDTO, staffDTO, typeExport, serialList, requiredRoleMap);
    }
}