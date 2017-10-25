package com.viettel.bccs.inventory.service.BaseStock;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.repo.OptionSetValueRepo;
import com.viettel.bccs.inventory.repo.ProductOfferingRepo;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;

/**
 * Created by TruNV on 8/17/2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(BaseChangeTerminalDevideService.class)
public class BaseChangeTerminalDevideServiceTest {
    @InjectMocks
    private BaseChangeTerminalDevideService changeTerminalDevideService;

    @Mock
    private EntityManager em;

    @Mock
    private StockTransService stockTransService;

    @Mock
    private ReportChangeHandsetService reportChangeHandsetService;

    @Mock
    private ProductWs productWs;

    @Mock
    private StaffService staffService;

    @Mock
    private StockTotalService stockTotalService;

    @Mock
    private StockTransActionService stockTransActionService;

    @Mock
    private StockTransDetailService stockTransDetailService;

    @Mock
    private StockTransSerialService stockTransSerialService;

    @Mock
    private ReasonService reasonService;

    @Mock
    private ProductOfferingService productOfferingService;

    @Mock
    private ProductOfferingRepo productOfferingRepo;

    @Mock
    private SaleWs saleWs;

    @Mock
    private OptionSetValueRepo optionSetValueRepo;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for changeTerminalDevideService.executeStockTrans method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testChangeTerminalDevideService_1() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);

        Mockito.when(reportChangeHandsetService.getLsChangeHandsetTerminalDevide(any(), any(), any())).thenReturn(null);

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        List<StockTransDetailDTO> lstStockTransDetail = new ArrayList<>();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        changeTerminalDevideService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetail, requiredRoleMap);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for changeTerminalDevideService.executeStockTrans method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testChangeTerminalDevideService_2() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        ReportChangeHandsetDTO reportChangeHandsetDTO = new ReportChangeHandsetDTO();
        reportChangeHandsetDTO.setPriceSaled(null);
        List<ReportChangeHandsetDTO> lstFindSaleTrans = Lists.newArrayList(reportChangeHandsetDTO);
        Mockito.when(reportChangeHandsetService.getLsChangeHandsetTerminalDevide(any(), any(), any())).thenReturn(lstFindSaleTrans);

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        List<StockTransDetailDTO> lstStockTransDetail = new ArrayList<>();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        changeTerminalDevideService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetail, requiredRoleMap);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for changeTerminalDevideService.executeStockTrans method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testChangeTerminalDevideService_3() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);

        ReportChangeHandsetDTO reportChangeHandsetDTO = new ReportChangeHandsetDTO();
        reportChangeHandsetDTO.setPriceSaled(1L);
        List<ReportChangeHandsetDTO> lstFindSaleTrans = Lists.newArrayList(reportChangeHandsetDTO);
        Mockito.when(reportChangeHandsetService.getLsChangeHandsetTerminalDevide(any(), any(), any())).thenReturn(lstFindSaleTrans);

        StaffDTO staffDTO = new StaffDTO();
        Mockito.when(staffService.findOne(any())).thenReturn(staffDTO);

        BaseChangeTerminalDevideService spyTemp = Mockito.spy(changeTerminalDevideService);
        Mockito.doReturn("").when(spyTemp).getText("REASON_EXP_CHANGE_DEMAGED_GOODS");

        Mockito.when(reasonService.getLsReasonByCode(anyString())).thenReturn(null);

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        StockTransDetailDTO stockTransDetailDTO1 = new StockTransDetailDTO();
        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        stockTransSerialDTO.setFromSerial("1");
        StockTransSerialDTO stockTransSerialDTO1 = new StockTransSerialDTO();
        stockTransSerialDTO1.setFromSerial("1");
        List<StockTransSerialDTO> stockTransSerialDTOs = Lists.newArrayList(stockTransSerialDTO, stockTransSerialDTO1);
        stockTransDetailDTO.setLstStockTransSerial(stockTransSerialDTOs);
        stockTransDetailDTO1.setLstStockTransSerial(stockTransSerialDTOs);

        Mockito.doReturn(new Date()).when(spyTemp).getSysDate(any());

        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetailDTO, stockTransDetailDTO1);
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        spyTemp.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetail, requiredRoleMap);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for changeTerminalDevideService.executeStockTrans method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testChangeTerminalDevideService_4() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);
        BaseChangeTerminalDevideService spyTemp = PowerMockito.spy(changeTerminalDevideService);

        ReportChangeHandsetDTO reportChangeHandsetDTO = new ReportChangeHandsetDTO();
        reportChangeHandsetDTO.setPriceSaled(1L);
        List<ReportChangeHandsetDTO> lstFindSaleTrans = Lists.newArrayList(reportChangeHandsetDTO);
        Mockito.when(reportChangeHandsetService.getLsChangeHandsetTerminalDevide(any(), any(), any())).thenReturn(lstFindSaleTrans);

        StaffDTO staffDTO = new StaffDTO();
        Mockito.when(staffService.findOne(any())).thenReturn(staffDTO);

        ReasonDTO reasonDTO = new ReasonDTO();
        List<ReasonDTO> lsReasonExpDTO = Lists.newArrayList(reasonDTO);
        PowerMockito.doReturn("").when(spyTemp, "getText", "REASON_EXP_CHANGE_DEMAGED_GOODS");
        Mockito.when(reasonService.getLsReasonByCode(anyString())).thenReturn(lsReasonExpDTO);

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setQuantity(2L);
        stockTransDetailDTO.setTableName("A");
        StockTransDetailDTO stockTransDetailDTO1 = new StockTransDetailDTO();
        stockTransDetailDTO1.setQuantity(2L);
        stockTransDetailDTO1.setTableName("A");
        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        stockTransSerialDTO.setFromSerial("1");
        StockTransSerialDTO stockTransSerialDTO1 = new StockTransSerialDTO();
        stockTransSerialDTO1.setFromSerial("1");
        List<StockTransSerialDTO> stockTransSerialDTOs = Lists.newArrayList(stockTransSerialDTO, stockTransSerialDTO1);
        stockTransDetailDTO.setLstStockTransSerial(stockTransSerialDTOs);
        stockTransDetailDTO1.setLstStockTransSerial(stockTransSerialDTOs);

        Mockito.doReturn(new Date()).when(spyTemp).getSysDate(any());

        StockTransDTO stockTransSaveExport = new StockTransDTO();
        stockTransSaveExport.setStockTransId(1L);
        Mockito.when(stockTransService.save(any())).thenReturn(stockTransSaveExport);

        StockTransActionDTO actionExportSave = new StockTransActionDTO();
        Mockito.when(stockTransActionService.save(any())).thenReturn(actionExportSave);

        StockTransDetailDTO detailSaveExport = new StockTransDetailDTO();
        Mockito.when(stockTransDetailService.save(any())).thenReturn(detailSaveExport);

        StockTotalDTO stockTotalDTOExport = new StockTotalDTO();
        stockTotalDTOExport.setAvailableQuantity(1L);
        PowerMockito.doReturn(stockTotalDTOExport).when(spyTemp, "getStockTotalDTOByOwnerId", any(), any(), any(), any());

        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetailDTO, stockTransDetailDTO1);
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        spyTemp.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetail, requiredRoleMap);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for changeTerminalDevideService.executeStockTrans method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testChangeTerminalDevideService_5() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);
        BaseChangeTerminalDevideService spyTemp = PowerMockito.spy(changeTerminalDevideService);

        ReportChangeHandsetDTO reportChangeHandsetDTO = new ReportChangeHandsetDTO();
        reportChangeHandsetDTO.setPriceSaled(1L);
        List<ReportChangeHandsetDTO> lstFindSaleTrans = Lists.newArrayList(reportChangeHandsetDTO);
        Mockito.when(reportChangeHandsetService.getLsChangeHandsetTerminalDevide(any(), any(), any())).thenReturn(lstFindSaleTrans);

        StaffDTO staffDTO = new StaffDTO();
        Mockito.when(staffService.findOne(any())).thenReturn(staffDTO);

        ReasonDTO reasonDTO = new ReasonDTO();
        List<ReasonDTO> lsReasonExpDTO = Lists.newArrayList(reasonDTO);
        PowerMockito.doReturn("").when(spyTemp, "getText", "REASON_EXP_CHANGE_DEMAGED_GOODS");
        Mockito.when(reasonService.getLsReasonByCode(anyString())).thenReturn(lsReasonExpDTO).thenReturn(null);

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setQuantity(2L);
        stockTransDetailDTO.setTableName("A");
        StockTransDetailDTO stockTransDetailDTO1 = new StockTransDetailDTO();
        stockTransDetailDTO1.setQuantity(2L);
        stockTransDetailDTO1.setTableName("A");
        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        stockTransSerialDTO.setFromSerial("1");
        StockTransSerialDTO stockTransSerialDTO1 = new StockTransSerialDTO();
        stockTransSerialDTO1.setFromSerial("1");
        List<StockTransSerialDTO> stockTransSerialDTOs = Lists.newArrayList(stockTransSerialDTO, stockTransSerialDTO1);
        stockTransDetailDTO.setLstStockTransSerial(stockTransSerialDTOs);
        stockTransDetailDTO1.setLstStockTransSerial(stockTransSerialDTOs);

        Mockito.doReturn(new Date()).when(spyTemp).getSysDate(any());

        StockTransDTO stockTransSaveExport = new StockTransDTO();
        stockTransSaveExport.setStockTransId(1L);
        Mockito.when(stockTransService.save(any())).thenReturn(stockTransSaveExport);

        StockTransActionDTO actionExportSave = new StockTransActionDTO();
        Mockito.when(stockTransActionService.save(any())).thenReturn(actionExportSave);

        StockTransDetailDTO detailSaveExport = new StockTransDetailDTO();
        Mockito.when(stockTransDetailService.save(any())).thenReturn(detailSaveExport);

        StockTotalDTO stockTotalDTOExport = new StockTotalDTO();
        stockTotalDTOExport.setAvailableQuantity(2L);
        stockTotalDTOExport.setCurrentQuantity(2L);
        PowerMockito.doReturn(stockTotalDTOExport).when(spyTemp, "getStockTotalDTOByOwnerId", any(), any(), any(), any());

        PowerMockito.doNothing().when(spyTemp, "doSaveStockTotal", any(), any(), any(), any());

        PowerMockito.doReturn("").when(spyTemp, "getText", "REASON_IMP_CHANGE_DEMAGED_GOODS");

        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetailDTO, stockTransDetailDTO1);
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        spyTemp.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetail, requiredRoleMap);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for changeTerminalDevideService.executeStockTrans method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testChangeTerminalDevideService_6() throws Exception {
        BaseChangeTerminalDevideService spyTemp = PowerMockito.spy(changeTerminalDevideService);

        ReportChangeHandsetDTO reportChangeHandsetDTO = new ReportChangeHandsetDTO();
        reportChangeHandsetDTO.setPriceSaled(1L);
        List<ReportChangeHandsetDTO> lstFindSaleTrans = Lists.newArrayList(reportChangeHandsetDTO);
        Mockito.when(reportChangeHandsetService.getLsChangeHandsetTerminalDevide(any(), any(), any())).thenReturn(lstFindSaleTrans);

        StaffDTO staffDTO = new StaffDTO();
        Mockito.when(staffService.findOne(any())).thenReturn(staffDTO);

        ReasonDTO reasonDTO = new ReasonDTO();
        List<ReasonDTO> lsReasonExpDTO = Lists.newArrayList(reasonDTO);
        PowerMockito.doReturn("").when(spyTemp, "getText", "REASON_EXP_CHANGE_DEMAGED_GOODS");
        Mockito.when(reasonService.getLsReasonByCode(anyString())).thenReturn(lsReasonExpDTO).thenReturn(lsReasonExpDTO);

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setQuantity(2L);
        stockTransDetailDTO.setTableName("A");
        StockTransDetailDTO stockTransDetailDTO1 = new StockTransDetailDTO();
        stockTransDetailDTO1.setQuantity(2L);
        stockTransDetailDTO1.setTableName("A");
        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        stockTransSerialDTO.setFromSerial("1");
        StockTransSerialDTO stockTransSerialDTO1 = new StockTransSerialDTO();
        stockTransSerialDTO1.setFromSerial("1");
        List<StockTransSerialDTO> stockTransSerialDTOs = Lists.newArrayList(stockTransSerialDTO, stockTransSerialDTO1);
        stockTransDetailDTO.setLstStockTransSerial(stockTransSerialDTOs);
        stockTransDetailDTO1.setLstStockTransSerial(stockTransSerialDTOs);

        Mockito.doReturn(new Date()).when(spyTemp).getSysDate(any());

        StockTransDTO stockTransSaveExport = new StockTransDTO();
        stockTransSaveExport.setStockTransId(1L);
        Mockito.when(stockTransService.save(any())).thenReturn(stockTransSaveExport);

        StockTransActionDTO actionExportSave = new StockTransActionDTO();
        Mockito.when(stockTransActionService.save(any())).thenReturn(actionExportSave);

        StockTransDetailDTO detailSaveExport = new StockTransDetailDTO();
        Mockito.when(stockTransDetailService.save(any())).thenReturn(detailSaveExport);

        StockTotalDTO stockTotalDTOExport = new StockTotalDTO();
        stockTotalDTOExport.setAvailableQuantity(2L);
        stockTotalDTOExport.setCurrentQuantity(2L);
        PowerMockito.doReturn(stockTotalDTOExport).when(spyTemp, "getStockTotalDTOByOwnerId", any(), any(), any(), any());

        PowerMockito.doNothing().when(spyTemp, "doSaveStockTotal", any(), any(), any(), any());

        PowerMockito.doReturn("").when(spyTemp, "getText", "REASON_IMP_CHANGE_DEMAGED_GOODS");

        PowerMockito.doNothing().when(spyTemp, "doSaveStockGoods", any(), any(), any());

        Mockito.when(optionSetValueRepo.getValueByOptionSetIdAndCode(Const.OPTION_SET.AMOUNT_DAY_TO_CHANGE_HANDSET, Const.OPTION_SET.AMOUNT_DAY_TO_CHANGE_HANDSET)).thenReturn(null);

        ProductOfferingDTO damageProductOfferingDTO = new ProductOfferingDTO();
        damageProductOfferingDTO.setProductOfferingId(1L);
        ProductOfferingDTO damageProductOfferingDTO1 = new ProductOfferingDTO();
        damageProductOfferingDTO1.setProductOfferingId(1L);
        Mockito.when(productOfferingService.findOne(any())).thenReturn(damageProductOfferingDTO).thenReturn(damageProductOfferingDTO1);

        ReportChangeHandsetDTO reportChangeHandsetDTOSave = new ReportChangeHandsetDTO();
        reportChangeHandsetDTOSave.setReportChangeHandsetId(1L);
        Mockito.when(reportChangeHandsetService.create(any())).thenReturn(reportChangeHandsetDTOSave);

        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetailDTO, stockTransDetailDTO1);
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        StockTransActionDTO result = spyTemp.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetail, requiredRoleMap);

        Assert.assertEquals(new Long(1), result.getStockTransActionId());
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for changeTerminalDevideService.executeStockTrans method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testChangeTerminalDevideService_7() throws Exception {
        BaseChangeTerminalDevideService spyTemp = PowerMockito.spy(changeTerminalDevideService);

        ReportChangeHandsetDTO reportChangeHandsetDTO = new ReportChangeHandsetDTO();
        reportChangeHandsetDTO.setPriceSaled(1L);
        List<ReportChangeHandsetDTO> lstFindSaleTrans = Lists.newArrayList(reportChangeHandsetDTO);
        Mockito.when(reportChangeHandsetService.getLsChangeHandsetTerminalDevide(any(), any(), any())).thenReturn(lstFindSaleTrans);

        StaffDTO staffDTO = new StaffDTO();
        Mockito.when(staffService.findOne(any())).thenReturn(staffDTO);

        ReasonDTO reasonDTO = new ReasonDTO();
        List<ReasonDTO> lsReasonExpDTO = Lists.newArrayList(reasonDTO);
        PowerMockito.doReturn("").when(spyTemp, "getText", "REASON_EXP_CHANGE_DEMAGED_GOODS");
        Mockito.when(reasonService.getLsReasonByCode(anyString())).thenReturn(lsReasonExpDTO).thenReturn(lsReasonExpDTO);

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setQuantity(2L);
        stockTransDetailDTO.setTableName("A");
        StockTransDetailDTO stockTransDetailDTO1 = new StockTransDetailDTO();
        stockTransDetailDTO1.setQuantity(2L);
        stockTransDetailDTO1.setTableName("A");
        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        stockTransSerialDTO.setFromSerial("1");
        StockTransSerialDTO stockTransSerialDTO1 = new StockTransSerialDTO();
        stockTransSerialDTO1.setFromSerial("1");
        List<StockTransSerialDTO> stockTransSerialDTOs = Lists.newArrayList(stockTransSerialDTO, stockTransSerialDTO1);
        stockTransDetailDTO.setLstStockTransSerial(stockTransSerialDTOs);
        stockTransDetailDTO1.setLstStockTransSerial(stockTransSerialDTOs);

        Mockito.doReturn(new Date()).when(spyTemp).getSysDate(any());

        StockTransDTO stockTransSaveExport = new StockTransDTO();
        stockTransSaveExport.setStockTransId(1L);
        Mockito.when(stockTransService.save(any())).thenReturn(stockTransSaveExport);

        StockTransActionDTO actionExportSave = new StockTransActionDTO();
        Mockito.when(stockTransActionService.save(any())).thenReturn(actionExportSave);

        StockTransDetailDTO detailSaveExport = new StockTransDetailDTO();
        Mockito.when(stockTransDetailService.save(any())).thenReturn(detailSaveExport);

        StockTotalDTO stockTotalDTOExport = new StockTotalDTO();
        stockTotalDTOExport.setAvailableQuantity(2L);
        stockTotalDTOExport.setCurrentQuantity(2L);
        PowerMockito.doReturn(stockTotalDTOExport).when(spyTemp, "getStockTotalDTOByOwnerId", any(), any(), any(), any());

        PowerMockito.doNothing().when(spyTemp, "doSaveStockTotal", any(), any(), any(), any());

        PowerMockito.doReturn("").when(spyTemp, "getText", "REASON_IMP_CHANGE_DEMAGED_GOODS");

        PowerMockito.doNothing().when(spyTemp, "doSaveStockGoods", any(), any(), any());

        Mockito.when(optionSetValueRepo.getValueByOptionSetIdAndCode(Const.OPTION_SET.AMOUNT_DAY_TO_CHANGE_HANDSET, Const.OPTION_SET.AMOUNT_DAY_TO_CHANGE_HANDSET)).thenReturn("10000");

        ProductOfferingDTO damageProductOfferingDTO = new ProductOfferingDTO();
        damageProductOfferingDTO.setProductOfferingId(1L);
        damageProductOfferingDTO.setAccountingModelCode("1");
        ProductOfferingDTO damageProductOfferingDTO1 = new ProductOfferingDTO();
        damageProductOfferingDTO1.setProductOfferingId(2L);
        damageProductOfferingDTO1.setAccountingModelCode("1");
        Mockito.when(productOfferingService.findOne(any())).thenReturn(damageProductOfferingDTO).thenReturn(damageProductOfferingDTO1);

        ReportChangeHandsetDTO reportChangeHandsetDTOSave = new ReportChangeHandsetDTO();
        reportChangeHandsetDTOSave.setReportChangeHandsetId(1L);
        Mockito.when(reportChangeHandsetService.create(any())).thenReturn(reportChangeHandsetDTOSave);

        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetailDTO, stockTransDetailDTO1);
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        StockTransActionDTO result = spyTemp.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetail, requiredRoleMap);

        Assert.assertEquals(new Long(1), result.getStockTransActionId());
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for changeTerminalDevideService.executeStockTrans method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testChangeTerminalDevideService_8() throws Exception {
        BaseChangeTerminalDevideService spyTemp = PowerMockito.spy(changeTerminalDevideService);

        ReportChangeHandsetDTO reportChangeHandsetDTO = new ReportChangeHandsetDTO();
        reportChangeHandsetDTO.setPriceSaled(1L);
        List<ReportChangeHandsetDTO> lstFindSaleTrans = Lists.newArrayList(reportChangeHandsetDTO);
        Mockito.when(reportChangeHandsetService.getLsChangeHandsetTerminalDevide(any(), any(), any())).thenReturn(lstFindSaleTrans);

        StaffDTO staffDTO = new StaffDTO();
        Mockito.when(staffService.findOne(any())).thenReturn(staffDTO);

        ReasonDTO reasonDTO = new ReasonDTO();
        List<ReasonDTO> lsReasonExpDTO = Lists.newArrayList(reasonDTO);
        PowerMockito.doReturn("").when(spyTemp, "getText", "REASON_EXP_CHANGE_DEMAGED_GOODS");
        Mockito.when(reasonService.getLsReasonByCode(anyString())).thenReturn(lsReasonExpDTO).thenReturn(lsReasonExpDTO);

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setQuantity(2L);
        stockTransDetailDTO.setTableName("A");
        StockTransDetailDTO stockTransDetailDTO1 = new StockTransDetailDTO();
        stockTransDetailDTO1.setQuantity(2L);
        stockTransDetailDTO1.setTableName("A");
        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        stockTransSerialDTO.setFromSerial("1");
        StockTransSerialDTO stockTransSerialDTO1 = new StockTransSerialDTO();
        stockTransSerialDTO1.setFromSerial("1");
        List<StockTransSerialDTO> stockTransSerialDTOs = Lists.newArrayList(stockTransSerialDTO, stockTransSerialDTO1);
        stockTransDetailDTO.setLstStockTransSerial(stockTransSerialDTOs);
        stockTransDetailDTO1.setLstStockTransSerial(stockTransSerialDTOs);

        Mockito.doReturn(new Date()).when(spyTemp).getSysDate(any());

        StockTransDTO stockTransSaveExport = new StockTransDTO();
        stockTransSaveExport.setStockTransId(1L);
        Mockito.when(stockTransService.save(any())).thenReturn(stockTransSaveExport);

        StockTransActionDTO actionExportSave = new StockTransActionDTO();
        Mockito.when(stockTransActionService.save(any())).thenReturn(actionExportSave);

        StockTransDetailDTO detailSaveExport = new StockTransDetailDTO();
        Mockito.when(stockTransDetailService.save(any())).thenReturn(detailSaveExport);

        StockTotalDTO stockTotalDTOExport = new StockTotalDTO();
        stockTotalDTOExport.setAvailableQuantity(2L);
        stockTotalDTOExport.setCurrentQuantity(2L);
        PowerMockito.doReturn(stockTotalDTOExport).when(spyTemp, "getStockTotalDTOByOwnerId", any(), any(), any(), any());

        PowerMockito.doNothing().when(spyTemp, "doSaveStockTotal", any(), any(), any(), any());

        PowerMockito.doReturn("").when(spyTemp, "getText", "REASON_IMP_CHANGE_DEMAGED_GOODS");

        PowerMockito.doNothing().when(spyTemp, "doSaveStockGoods", any(), any(), any());

        Mockito.when(optionSetValueRepo.getValueByOptionSetIdAndCode(Const.OPTION_SET.AMOUNT_DAY_TO_CHANGE_HANDSET, Const.OPTION_SET.AMOUNT_DAY_TO_CHANGE_HANDSET)).thenReturn("10000");

        ProductOfferingDTO damageProductOfferingDTO = new ProductOfferingDTO();
        damageProductOfferingDTO.setProductOfferingId(1L);
        damageProductOfferingDTO.setAccountingModelCode("1");
        ProductOfferingDTO damageProductOfferingDTO1 = new ProductOfferingDTO();
        damageProductOfferingDTO1.setProductOfferingId(2L);
        damageProductOfferingDTO1.setAccountingModelCode("2");
        Mockito.when(productOfferingService.findOne(any())).thenReturn(damageProductOfferingDTO).thenReturn(damageProductOfferingDTO1);

        ReportChangeHandsetDTO reportChangeHandsetDTOSave = new ReportChangeHandsetDTO();
        reportChangeHandsetDTOSave.setReportChangeHandsetId(1L);
        Mockito.when(reportChangeHandsetService.create(any())).thenReturn(reportChangeHandsetDTOSave);

        Mockito.when(productOfferingRepo.getStockModelPrefixById(any())).thenReturn("pre");

        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetailDTO, stockTransDetailDTO1);
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        StockTransActionDTO result = spyTemp.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetail, requiredRoleMap);

        Assert.assertEquals(new Long(1), result.getStockTransActionId());
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for changeTerminalDevideService.executeStockTrans method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testChangeTerminalDevideService_9() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_CONNNECT.ERROR_SYSTEM_SALE);

        BaseChangeTerminalDevideService spyTemp = PowerMockito.spy(changeTerminalDevideService);

        ReportChangeHandsetDTO reportChangeHandsetDTO = new ReportChangeHandsetDTO();
        reportChangeHandsetDTO.setPriceSaled(1L);
        List<ReportChangeHandsetDTO> lstFindSaleTrans = Lists.newArrayList(reportChangeHandsetDTO);
        Mockito.when(reportChangeHandsetService.getLsChangeHandsetTerminalDevide(any(), any(), any())).thenReturn(lstFindSaleTrans);

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStaffId(1L);
        Mockito.when(staffService.findOne(any())).thenReturn(staffDTO);

        ReasonDTO reasonDTO = new ReasonDTO();
        List<ReasonDTO> lsReasonExpDTO = Lists.newArrayList(reasonDTO);
        PowerMockito.doReturn("").when(spyTemp, "getText", "REASON_EXP_CHANGE_DEMAGED_GOODS");
        Mockito.when(reasonService.getLsReasonByCode(anyString())).thenReturn(lsReasonExpDTO).thenReturn(lsReasonExpDTO);

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setFromDate(new Date());
        stockTransActionDTO.setToDate(new Date());
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setQuantity(2L);
        stockTransDetailDTO.setTableName("A");
        StockTransDetailDTO stockTransDetailDTO1 = new StockTransDetailDTO();
        stockTransDetailDTO1.setQuantity(2L);
        stockTransDetailDTO1.setTableName("A");
        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        stockTransSerialDTO.setFromSerial("1");
        StockTransSerialDTO stockTransSerialDTO1 = new StockTransSerialDTO();
        stockTransSerialDTO1.setFromSerial("1");
        List<StockTransSerialDTO> stockTransSerialDTOs = Lists.newArrayList(stockTransSerialDTO, stockTransSerialDTO1);
        stockTransDetailDTO.setLstStockTransSerial(stockTransSerialDTOs);
        stockTransDetailDTO1.setLstStockTransSerial(stockTransSerialDTOs);

        Mockito.doReturn(new Date()).when(spyTemp).getSysDate(any());

        StockTransDTO stockTransSaveExport = new StockTransDTO();
        stockTransSaveExport.setStockTransId(1L);
        Mockito.when(stockTransService.save(any())).thenReturn(stockTransSaveExport);

        StockTransActionDTO actionExportSave = new StockTransActionDTO();
        Mockito.when(stockTransActionService.save(any())).thenReturn(actionExportSave);

        StockTransDetailDTO detailSaveExport = new StockTransDetailDTO();
        Mockito.when(stockTransDetailService.save(any())).thenReturn(detailSaveExport);

        StockTotalDTO stockTotalDTOExport = new StockTotalDTO();
        stockTotalDTOExport.setAvailableQuantity(2L);
        stockTotalDTOExport.setCurrentQuantity(2L);
        PowerMockito.doReturn(stockTotalDTOExport).when(spyTemp, "getStockTotalDTOByOwnerId", any(), any(), any(), any());

        PowerMockito.doNothing().when(spyTemp, "doSaveStockTotal", any(), any(), any(), any());

        PowerMockito.doReturn("").when(spyTemp, "getText", "REASON_IMP_CHANGE_DEMAGED_GOODS");

        PowerMockito.doNothing().when(spyTemp, "doSaveStockGoods", any(), any(), any());

        Mockito.when(optionSetValueRepo.getValueByOptionSetIdAndCode(Const.OPTION_SET.AMOUNT_DAY_TO_CHANGE_HANDSET, Const.OPTION_SET.AMOUNT_DAY_TO_CHANGE_HANDSET)).thenReturn("10000");

        ProductOfferingDTO damageProductOfferingDTO = new ProductOfferingDTO();
        damageProductOfferingDTO.setProductOfferingId(1L);
        damageProductOfferingDTO.setAccountingModelCode("1");
        ProductOfferingDTO damageProductOfferingDTO1 = new ProductOfferingDTO();
        damageProductOfferingDTO1.setProductOfferingId(2L);
        damageProductOfferingDTO1.setAccountingModelCode("2");
        Mockito.when(productOfferingService.findOne(any())).thenReturn(damageProductOfferingDTO).thenReturn(damageProductOfferingDTO1);

        ReportChangeHandsetDTO reportChangeHandsetDTOSave = new ReportChangeHandsetDTO();
        reportChangeHandsetDTOSave.setReportChangeHandsetId(1L);
        Mockito.when(reportChangeHandsetService.create(any())).thenReturn(reportChangeHandsetDTOSave);

        Mockito.when(productOfferingRepo.getStockModelPrefixById(any())).thenReturn("pre").thenReturn("suf");

        Mockito.when(saleWs.changeGood(1L, "1", 1L, "1", 1, 1L, new Date(), new Date())).thenReturn(null);

        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetailDTO, stockTransDetailDTO1);
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        StockTransActionDTO result = spyTemp.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetail, requiredRoleMap);

        Assert.assertEquals(new Long(1), result.getStockTransActionId());
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for changeTerminalDevideService.executeStockTrans method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testChangeTerminalDevideService_10() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        BaseChangeTerminalDevideService spyTemp = PowerMockito.spy(changeTerminalDevideService);

        ReportChangeHandsetDTO reportChangeHandsetDTO = new ReportChangeHandsetDTO();
        reportChangeHandsetDTO.setPriceSaled(1L);
        List<ReportChangeHandsetDTO> lstFindSaleTrans = Lists.newArrayList(reportChangeHandsetDTO);
        Mockito.when(reportChangeHandsetService.getLsChangeHandsetTerminalDevide(any(), any(), any())).thenReturn(lstFindSaleTrans);

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStaffId(1L);
        Mockito.when(staffService.findOne(any())).thenReturn(staffDTO);

        ReasonDTO reasonDTO = new ReasonDTO();
        List<ReasonDTO> lsReasonExpDTO = Lists.newArrayList(reasonDTO);
        PowerMockito.doReturn(null).when(spyTemp, "getText", "REASON_EXP_CHANGE_DEMAGED_GOODS");
        Mockito.when(reasonService.getLsReasonByCode(anyString())).thenReturn(lsReasonExpDTO).thenReturn(lsReasonExpDTO);

        Date date = new Date();
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setFromDate(date);
        stockTransActionDTO.setToDate(date);
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setQuantity(2L);
        stockTransDetailDTO.setTableName("A");
        StockTransDetailDTO stockTransDetailDTO1 = new StockTransDetailDTO();
        stockTransDetailDTO1.setQuantity(2L);
        stockTransDetailDTO1.setTableName("A");
        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        stockTransSerialDTO.setFromSerial("1");
        StockTransSerialDTO stockTransSerialDTO1 = new StockTransSerialDTO();
        stockTransSerialDTO1.setFromSerial("1");
        List<StockTransSerialDTO> stockTransSerialDTOs = Lists.newArrayList(stockTransSerialDTO, stockTransSerialDTO1);
        stockTransDetailDTO.setLstStockTransSerial(stockTransSerialDTOs);
        stockTransDetailDTO1.setLstStockTransSerial(stockTransSerialDTOs);

        Mockito.doReturn(new Date()).when(spyTemp).getSysDate(any());

        StockTransDTO stockTransSaveExport = new StockTransDTO();
        stockTransSaveExport.setStockTransId(1L);
        Mockito.when(stockTransService.save(any())).thenReturn(stockTransSaveExport);

        StockTransActionDTO actionExportSave = new StockTransActionDTO();
        Mockito.when(stockTransActionService.save(any())).thenReturn(actionExportSave);

        StockTransDetailDTO detailSaveExport = new StockTransDetailDTO();
        Mockito.when(stockTransDetailService.save(any())).thenReturn(detailSaveExport);

        StockTotalDTO stockTotalDTOExport = new StockTotalDTO();
        stockTotalDTOExport.setAvailableQuantity(2L);
        stockTotalDTOExport.setCurrentQuantity(2L);
        PowerMockito.doReturn(stockTotalDTOExport).when(spyTemp, "getStockTotalDTOByOwnerId", any(), any(), any(), any());

        PowerMockito.doNothing().when(spyTemp, "doSaveStockTotal", any(), any(), any(), any());

        PowerMockito.doReturn("").when(spyTemp, "getText", "REASON_IMP_CHANGE_DEMAGED_GOODS");

        PowerMockito.doNothing().when(spyTemp, "doSaveStockGoods", any(), any(), any());

        Mockito.when(optionSetValueRepo.getValueByOptionSetIdAndCode(Const.OPTION_SET.AMOUNT_DAY_TO_CHANGE_HANDSET, Const.OPTION_SET.AMOUNT_DAY_TO_CHANGE_HANDSET)).thenReturn("10000");

        ProductOfferingDTO damageProductOfferingDTO = new ProductOfferingDTO();
        damageProductOfferingDTO.setProductOfferingId(1L);
        damageProductOfferingDTO.setAccountingModelCode("1");
        ProductOfferingDTO damageProductOfferingDTO1 = new ProductOfferingDTO();
        damageProductOfferingDTO1.setProductOfferingId(2L);
        damageProductOfferingDTO1.setAccountingModelCode("2");
        Mockito.when(productOfferingService.findOne(any())).thenReturn(damageProductOfferingDTO).thenReturn(damageProductOfferingDTO1);

        ReportChangeHandsetDTO reportChangeHandsetDTOSave = new ReportChangeHandsetDTO();
        reportChangeHandsetDTOSave.setReportChangeHandsetId(1L);
        Mockito.when(reportChangeHandsetService.create(any())).thenReturn(reportChangeHandsetDTOSave);

        Mockito.when(productOfferingRepo.getStockModelPrefixById(any())).thenReturn("pre").thenReturn("suf");

        ChangeGoodMessage message = new ChangeGoodMessage();
        message.setResponseCode("200");
        Mockito.when(saleWs.changeGood(null, "1", null, "1", 10000, 1L, date, date)).thenReturn(message);

        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetailDTO, stockTransDetailDTO1);
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        StockTransActionDTO result = spyTemp.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetail, requiredRoleMap);

        Assert.assertEquals(new Long(1), result.getStockTransActionId());
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for changeTerminalDevideService.executeStockTrans method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testChangeTerminalDevideService_11() throws Exception {
        BaseChangeTerminalDevideService spyTemp = PowerMockito.spy(changeTerminalDevideService);

        ReportChangeHandsetDTO reportChangeHandsetDTO = new ReportChangeHandsetDTO();
        reportChangeHandsetDTO.setPriceSaled(1L);
        List<ReportChangeHandsetDTO> lstFindSaleTrans = Lists.newArrayList(reportChangeHandsetDTO);
        Mockito.when(reportChangeHandsetService.getLsChangeHandsetTerminalDevide(any(), any(), any())).thenReturn(lstFindSaleTrans);

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStaffId(1L);
        Mockito.when(staffService.findOne(any())).thenReturn(staffDTO);

        ReasonDTO reasonDTO = new ReasonDTO();
        List<ReasonDTO> lsReasonExpDTO = Lists.newArrayList(reasonDTO);
        PowerMockito.doReturn("22").when(spyTemp, "getText", "REASON_EXP_CHANGE_DEMAGED_GOODS");
        Mockito.when(reasonService.getLsReasonByCode(anyString())).thenReturn(lsReasonExpDTO).thenReturn(lsReasonExpDTO);

        Date date = new Date();
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setFromDate(date);
        stockTransActionDTO.setToDate(date);
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setQuantity(2L);
        stockTransDetailDTO.setTableName("A");
        StockTransDetailDTO stockTransDetailDTO1 = new StockTransDetailDTO();
        stockTransDetailDTO1.setQuantity(2L);
        stockTransDetailDTO1.setTableName("A");
        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        stockTransSerialDTO.setFromSerial("1");
        StockTransSerialDTO stockTransSerialDTO1 = new StockTransSerialDTO();
        stockTransSerialDTO1.setFromSerial("1");
        List<StockTransSerialDTO> stockTransSerialDTOs = Lists.newArrayList(stockTransSerialDTO, stockTransSerialDTO1);
        stockTransDetailDTO.setLstStockTransSerial(stockTransSerialDTOs);
        stockTransDetailDTO1.setLstStockTransSerial(stockTransSerialDTOs);

        Mockito.doReturn(new Date()).when(spyTemp).getSysDate(any());

        StockTransDTO stockTransSaveExport = new StockTransDTO();
        stockTransSaveExport.setStockTransId(1L);
        Mockito.when(stockTransService.save(any())).thenReturn(stockTransSaveExport);

        StockTransActionDTO actionExportSave = new StockTransActionDTO();
        Mockito.when(stockTransActionService.save(any())).thenReturn(actionExportSave);

        StockTransDetailDTO detailSaveExport = new StockTransDetailDTO();
        Mockito.when(stockTransDetailService.save(any())).thenReturn(detailSaveExport);

        StockTotalDTO stockTotalDTOExport = new StockTotalDTO();
        stockTotalDTOExport.setAvailableQuantity(2L);
        stockTotalDTOExport.setCurrentQuantity(2L);
        PowerMockito.doReturn(stockTotalDTOExport).when(spyTemp, "getStockTotalDTOByOwnerId", any(), any(), any(), any());

        PowerMockito.doNothing().when(spyTemp, "doSaveStockTotal", any(), any(), any(), any());

        PowerMockito.doReturn("22").when(spyTemp, "getText", "REASON_IMP_CHANGE_DEMAGED_GOODS");

        PowerMockito.doNothing().when(spyTemp, "doSaveStockGoods", any(), any(), any());

        Mockito.when(optionSetValueRepo.getValueByOptionSetIdAndCode(Const.OPTION_SET.AMOUNT_DAY_TO_CHANGE_HANDSET, Const.OPTION_SET.AMOUNT_DAY_TO_CHANGE_HANDSET)).thenReturn("10000");

        ProductOfferingDTO damageProductOfferingDTO = new ProductOfferingDTO();
        damageProductOfferingDTO.setProductOfferingId(1L);
        damageProductOfferingDTO.setAccountingModelCode("1");
        ProductOfferingDTO damageProductOfferingDTO1 = new ProductOfferingDTO();
        damageProductOfferingDTO1.setProductOfferingId(2L);
        damageProductOfferingDTO1.setAccountingModelCode("2");
        Mockito.when(productOfferingService.findOne(any())).thenReturn(damageProductOfferingDTO).thenReturn(damageProductOfferingDTO1);

        ReportChangeHandsetDTO reportChangeHandsetDTOSave = new ReportChangeHandsetDTO();
        reportChangeHandsetDTOSave.setReportChangeHandsetId(1L);
        Mockito.when(reportChangeHandsetService.create(any())).thenReturn(reportChangeHandsetDTOSave);

        Mockito.when(productOfferingRepo.getStockModelPrefixById(any())).thenReturn("pre").thenReturn("suf");

        ChangeGoodMessage message = new ChangeGoodMessage();
        message.setResponseCode("0");
        Mockito.when(saleWs.changeGood(null, "1", null, "1", 10000, 1L, date, date)).thenReturn(message);

        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetailDTO, stockTransDetailDTO1);
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        StockTransActionDTO result = spyTemp.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetail, requiredRoleMap);

        Assert.assertEquals(new Long(1), result.getStockTransActionId());
    }

    @Test
    public void testChangeTerminalDevideService_12() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);
        BaseChangeTerminalDevideService spyTemp = PowerMockito.spy(changeTerminalDevideService);

        ReportChangeHandsetDTO reportChangeHandsetDTO = new ReportChangeHandsetDTO();
        reportChangeHandsetDTO.setPriceSaled(1L);
        List<ReportChangeHandsetDTO> lstFindSaleTrans = Lists.newArrayList(reportChangeHandsetDTO);
        Mockito.when(reportChangeHandsetService.getLsChangeHandsetTerminalDevide(any(), any(), any())).thenReturn(lstFindSaleTrans);

        StaffDTO staffDTO = new StaffDTO();
        Mockito.when(staffService.findOne(any())).thenReturn(staffDTO);

        ReasonDTO reasonDTO = new ReasonDTO();
        List<ReasonDTO> lsReasonExpDTO = Lists.newArrayList(reasonDTO);
        PowerMockito.doReturn("").when(spyTemp, "getText", "REASON_EXP_CHANGE_DEMAGED_GOODS");
        Mockito.when(reasonService.getLsReasonByCode(anyString())).thenReturn(lsReasonExpDTO);

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setQuantity(2L);
        stockTransDetailDTO.setTableName("A");
        StockTransDetailDTO stockTransDetailDTO1 = new StockTransDetailDTO();
        stockTransDetailDTO1.setQuantity(3L);
        stockTransDetailDTO1.setTableName("A");
        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        stockTransSerialDTO.setFromSerial("1");
        StockTransSerialDTO stockTransSerialDTO1 = new StockTransSerialDTO();
        stockTransSerialDTO1.setFromSerial("1");
        List<StockTransSerialDTO> stockTransSerialDTOs = Lists.newArrayList(stockTransSerialDTO, stockTransSerialDTO1);
        stockTransDetailDTO.setLstStockTransSerial(stockTransSerialDTOs);
        stockTransDetailDTO1.setLstStockTransSerial(stockTransSerialDTOs);

        Mockito.doReturn(new Date()).when(spyTemp).getSysDate(any());

        StockTransDTO stockTransSaveExport = new StockTransDTO();
        stockTransSaveExport.setStockTransId(1L);
        Mockito.when(stockTransService.save(any())).thenReturn(stockTransSaveExport);

        StockTransActionDTO actionExportSave = new StockTransActionDTO();
        Mockito.when(stockTransActionService.save(any())).thenReturn(actionExportSave);

        StockTransDetailDTO detailSaveExport = new StockTransDetailDTO();
        Mockito.when(stockTransDetailService.save(any())).thenReturn(detailSaveExport);

        StockTotalDTO stockTotalDTOExport = new StockTotalDTO();
        stockTotalDTOExport.setAvailableQuantity(4L);
        stockTotalDTOExport.setCurrentQuantity(1L);
        PowerMockito.doReturn(stockTotalDTOExport).when(spyTemp, "getStockTotalDTOByOwnerId", any(), any(), any(), any());

        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetailDTO, stockTransDetailDTO1);
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        spyTemp.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetail, requiredRoleMap);
    }


    /** -------------------------------------------------------------------- **/
    /** --- Test for changeTerminalDevideService.doPrepareData method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testDoPrepareData_1() throws Exception {
        changeTerminalDevideService.doPrepareData(null, null, null);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for changeTerminalDevideService.doValidate method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testDoValidate_1() throws Exception {
        changeTerminalDevideService.doValidate(null, null, null);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for changeTerminalDevideService.formatTransCode method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testFormatTransCode_1() throws Exception {
        Whitebox.invokeMethod(changeTerminalDevideService, "formatTransCode", 0L);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for changeTerminalDevideService.getStockTotalDTOByOwnerId method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testGetStockTotalDTOByOwnerId_1() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        Mockito.when(stockTotalService.getStockTotalForProcessStock(0L, 0L, 0L, 0L)).thenReturn(null);
        Whitebox.invokeMethod(changeTerminalDevideService, "getStockTotalDTOByOwnerId", 0L, 0L, 0L, 0L);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for changeTerminalDevideService.getStockTotalDTOByOwnerId method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testGetStockTotalDTOByOwnerId_2() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTotalDTO stockTotalDTO = new StockTotalDTO();
        stockTotalDTO.setAvailableQuantity(0L);
        Mockito.when(stockTotalService.getStockTotalForProcessStock(0L, 0L, 0L, 0L)).thenReturn(stockTotalDTO);
        Whitebox.invokeMethod(changeTerminalDevideService, "getStockTotalDTOByOwnerId", 0L, 0L, 0L, 0L);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for changeTerminalDevideService.getStockTotalDTOByOwnerId method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testGetStockTotalDTOByOwnerId_3() throws Exception {
        StockTotalDTO stockTotalDTO = new StockTotalDTO();
        stockTotalDTO.setAvailableQuantity(1L);
        stockTotalDTO.setCurrentQuantity(1L);
        Mockito.when(stockTotalService.getStockTotalForProcessStock(0L, 0L, 0L, 0L)).thenReturn(stockTotalDTO);
        StockTotalDTO result = Whitebox.invokeMethod(changeTerminalDevideService, "getStockTotalDTOByOwnerId", 0L, 0L, 0L, 0L);

        Assert.assertEquals(new Long(1), result.getAvailableQuantity());
        Assert.assertEquals(new Long(1), result.getCurrentQuantity());
    }
}