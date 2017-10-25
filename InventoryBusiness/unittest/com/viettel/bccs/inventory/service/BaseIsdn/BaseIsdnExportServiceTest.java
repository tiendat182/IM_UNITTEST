package com.viettel.bccs.inventory.service.BaseIsdn;

import com.google.common.collect.Lists;
import com.viettel.bccs.im1.dto.StaffIm1DTO;
import com.viettel.bccs.im1.service.StaffIm1Service;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.repo.StockTransRepo;
import com.viettel.bccs.inventory.service.BaseStock.BaseStockService;
import com.viettel.bccs.inventory.service.BaseStock.BaseValidateService;
import com.viettel.bccs.inventory.service.*;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.RequiredRoleMap;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.any;

/**
 * Created by TruNV on 9/8/2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({DataUtil.class, BaseStockService.class})
@PowerMockIgnore("javax.management.*")
public class BaseIsdnExportServiceTest {

    @InjectMocks
    private BaseIsdnExportService baseIsdnExportService;

    @Mock
    private BaseValidateService baseValidateService;

    @Mock
    private ReasonService reasonService;

    @Mock
    private ShopService shopService;

    @Mock
    private StockTotalService stockTotalService;

    @Mock
    private StockTransSerialService stockTransSerialService;

    @Mock
    private StaffService staffService;

    @Mock
    private StockTransService stockTransService;

    @Mock
    private ProductOfferingService productOfferingService;

    @Mock
    private StockTransRepo stockTransRepo;

    @Mock
    private OptionSetValueService optionSetValueService;

    @Mock
    private StaffIm1Service staffIm1Service;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseIsdnExportService.doPrepareData method ------------ **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoPrepareData_1() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        baseIsdnExportService.doPrepareData(stockTransDTO, stockTransActionDTO, flagStockDTO);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseIsdnExportService.doValidate method --------------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoValidate_1() throws Exception {
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetailDTO);
        BaseIsdnExportService spyService = Mockito.spy(baseIsdnExportService);
        Mockito.doNothing().when(spyService).doOrderValidate(any());
        Mockito.doNothing().when(baseValidateService).doSignVofficeValidate(any());
        Mockito.doNothing().when(spyService).doValidateStockTransDetail(any());
        spyService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseIsdnExportService.doOrderValidate method ---------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoOrderValidate_1() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDTO stockTrans = new StockTransDTO();
        stockTrans.setStatus("2");
        Mockito.doReturn(stockTrans).when(stockTransService).findOne(any());
        baseIsdnExportService.doOrderValidate(stockTransDTO);
    }

    @Test
    public void testDoOrderValidate_2() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setFromOwnerId(0L);
        stockTransDTO.setFromOwnerType(0L);

        StockTransDTO stockTrans = new StockTransDTO();
        stockTrans.setStatus("1");
        Mockito.doReturn(stockTrans).when(stockTransService).findOne(any());
        baseIsdnExportService.doOrderValidate(stockTransDTO);
    }

    @Test
    public void testDoOrderValidate_3() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setFromOwnerId(0L);
        stockTransDTO.setFromOwnerType(1L);

        StockTransDTO stockTrans = new StockTransDTO();
        stockTrans.setStatus("1");
        Mockito.doReturn(stockTrans).when(stockTransService).findOne(any());
        baseIsdnExportService.doOrderValidate(stockTransDTO);
    }

    @Test
    public void testDoOrderValidate_4() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setFromOwnerId(1L);
        stockTransDTO.setFromOwnerType(0L);

        StockTransDTO stockTrans = new StockTransDTO();
        stockTrans.setStatus("1");
        Mockito.doReturn(stockTrans).when(stockTransService).findOne(any());
        baseIsdnExportService.doOrderValidate(stockTransDTO);
    }

    @Test
    public void testDoOrderValidate_5() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setFromOwnerId(1L);
        stockTransDTO.setFromOwnerType(1L);

        StockTransDTO stockTrans = new StockTransDTO();
        stockTrans.setStatus("1");
        Mockito.doReturn(stockTrans).when(stockTransService).findOne(any());

        List<VShopStaffDTO> exportShopList = Lists.newArrayList();

        Mockito.doReturn(null).when(shopService).getListShopStaff(any(), any(), any());
        baseIsdnExportService.doOrderValidate(stockTransDTO);
    }

    @Test
    public void testDoOrderValidate_6() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setFromOwnerId(1L);
        stockTransDTO.setFromOwnerType(1L);
        stockTransDTO.setToOwnerId(0L);
        stockTransDTO.setToOwnerType(0L);

        StockTransDTO stockTrans = new StockTransDTO();
        stockTrans.setStatus("1");
        Mockito.doReturn(stockTrans).when(stockTransService).findOne(any());

        VShopStaffDTO vShopStaffDTO = new VShopStaffDTO();
        List<VShopStaffDTO> exportShopList = Lists.newArrayList(vShopStaffDTO);

        Mockito.doReturn(exportShopList).when(shopService).getListShopStaff(any(), any(), any());
        baseIsdnExportService.doOrderValidate(stockTransDTO);
    }

    @Test
    public void testDoOrderValidate_7() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setFromOwnerId(1L);
        stockTransDTO.setFromOwnerType(1L);
        stockTransDTO.setToOwnerId(1L);
        stockTransDTO.setToOwnerType(0L);

        StockTransDTO stockTrans = new StockTransDTO();
        stockTrans.setStatus("1");
        Mockito.doReturn(stockTrans).when(stockTransService).findOne(any());

        VShopStaffDTO vShopStaffDTO = new VShopStaffDTO();
        List<VShopStaffDTO> exportShopList = Lists.newArrayList(vShopStaffDTO);

        Mockito.doReturn(exportShopList).when(shopService).getListShopStaff(any(), any(), any());
        baseIsdnExportService.doOrderValidate(stockTransDTO);
    }

    @Test
    public void testDoOrderValidate_8() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setFromOwnerId(1L);
        stockTransDTO.setFromOwnerType(1L);
        stockTransDTO.setToOwnerId(0L);
        stockTransDTO.setToOwnerType(1L);

        StockTransDTO stockTrans = new StockTransDTO();
        stockTrans.setStatus("1");
        Mockito.doReturn(stockTrans).when(stockTransService).findOne(any());

        VShopStaffDTO vShopStaffDTO = new VShopStaffDTO();
        List<VShopStaffDTO> exportShopList = Lists.newArrayList(vShopStaffDTO);

        Mockito.doReturn(exportShopList).when(shopService).getListShopStaff(any(), any(), any());
        baseIsdnExportService.doOrderValidate(stockTransDTO);
    }

    @Test
    public void testDoOrderValidate_9() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setFromOwnerId(1L);
        stockTransDTO.setFromOwnerType(1L);
        stockTransDTO.setToOwnerId(2L);
        stockTransDTO.setFromOwnerId(2L);
        stockTransDTO.setToOwnerType(1L);

        StockTransDTO stockTrans = new StockTransDTO();
        stockTrans.setStatus("1");
        Mockito.doReturn(stockTrans).when(stockTransService).findOne(any());

        VShopStaffDTO vShopStaffDTO = new VShopStaffDTO();
        List<VShopStaffDTO> exportShopList = Lists.newArrayList(vShopStaffDTO);

        Mockito.doReturn(exportShopList).when(shopService).getListShopStaff(any(), any(), any());
        baseIsdnExportService.doOrderValidate(stockTransDTO);
    }

    @Test
    public void testDoOrderValidate_10() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setFromOwnerId(1L);
        stockTransDTO.setFromOwnerType(1L);
        stockTransDTO.setToOwnerId(2L);
        stockTransDTO.setFromOwnerId(3L);
        stockTransDTO.setToOwnerType(1L);

        StockTransDTO stockTrans = new StockTransDTO();
        stockTrans.setStatus("1");
        Mockito.doReturn(stockTrans).when(stockTransService).findOne(any());

        VShopStaffDTO vShopStaffDTO = new VShopStaffDTO();
        List<VShopStaffDTO> exportShopList = Lists.newArrayList(vShopStaffDTO);
        Mockito.doReturn(exportShopList).when(shopService).getListShopStaff(any(), any(), any());

        ShopDTO shopFrom = new ShopDTO();
        shopFrom.setStatus("2");
        Mockito.doReturn(shopFrom).when(shopService).findOne(any());

        baseIsdnExportService.doOrderValidate(stockTransDTO);
    }

    @Test
    public void testDoOrderValidate_11() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setFromOwnerId(1L);
        stockTransDTO.setFromOwnerType(1L);
        stockTransDTO.setToOwnerId(2L);
        stockTransDTO.setFromOwnerId(3L);
        stockTransDTO.setToOwnerType(1L);

        StockTransDTO stockTrans = new StockTransDTO();
        stockTrans.setStatus("1");
        Mockito.doReturn(stockTrans).when(stockTransService).findOne(any());

        VShopStaffDTO vShopStaffDTO = new VShopStaffDTO();
        List<VShopStaffDTO> exportShopList = Lists.newArrayList(vShopStaffDTO);
        Mockito.doReturn(exportShopList).when(shopService).getListShopStaff(any(), any(), any());

        ShopDTO shopFrom = new ShopDTO();
        shopFrom.setStatus("1");
        shopFrom.setChannelTypeId(null);
        Mockito.doReturn(shopFrom).when(shopService).findOne(any());

        baseIsdnExportService.doOrderValidate(stockTransDTO);
    }
    // TODO không vào được line 127 vì line 119 đã bắt rồi.
    @Test
    public void testDoOrderValidate_12() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setFromOwnerId(1L);
        stockTransDTO.setFromOwnerType(1L);
        stockTransDTO.setToOwnerId(2L);
        stockTransDTO.setFromOwnerId(3L);
        stockTransDTO.setToOwnerType(1L);

        StockTransDTO stockTrans = new StockTransDTO();
        stockTrans.setStatus("1");
        Mockito.doReturn(stockTrans).when(stockTransService).findOne(any());

        VShopStaffDTO vShopStaffDTO = new VShopStaffDTO();
        List<VShopStaffDTO> exportShopList = Lists.newArrayList(vShopStaffDTO);
        Mockito.doReturn(exportShopList).when(shopService).getListShopStaff(any(), any(), any());

        ShopDTO shopFrom = new ShopDTO();
        shopFrom.setStatus("1");
        shopFrom.setChannelTypeId(1L);
        Mockito.doReturn(shopFrom).when(shopService).findOne(any());

        baseIsdnExportService.doOrderValidate(stockTransDTO);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseIsdnExportService.doDebitIsdnValidate method ------ **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoDebitIsdnValidate_1() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetailDTO);
        baseIsdnExportService.doDebitIsdnValidate(stockTransDTO, null);
    }

    @Test
    public void testDoDebitIsdnValidate_2() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setRegionStockId(2L);

        StockTransDetailDTO stockTransDetail = new StockTransDetailDTO();
        stockTransDetail.setProdOfferId(0L);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        baseIsdnExportService.doDebitIsdnValidate(stockTransDTO, lstStockTransDetail);
    }

    @Test
    public void testDoDebitIsdnValidate_3() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setRegionStockId(null);

        StockTransDetailDTO stockTransDetail = new StockTransDetailDTO();
        stockTransDetail.setProdOfferId(1L);
        stockTransDetail.setStateId(0L);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        baseIsdnExportService.doDebitIsdnValidate(stockTransDTO, lstStockTransDetail);
    }

    @Test
    public void testDoDebitIsdnValidate_4() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setRegionStockId(2L);

        StockTransDetailDTO stockTransDetail = new StockTransDetailDTO();
        stockTransDetail.setProdOfferId(1L);
        stockTransDetail.setStateId(1L);
        stockTransDetail.setQuantity(0L);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        baseIsdnExportService.doDebitIsdnValidate(stockTransDTO, lstStockTransDetail);
    }

    @Test
    public void testDoDebitIsdnValidate_5() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setRegionStockId(null);

        StockTransDetailDTO stockTransDetail = new StockTransDetailDTO();
        stockTransDetail.setProdOfferId(1L);
        stockTransDetail.setStateId(1L);
        stockTransDetail.setQuantity(-1L);

        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        baseIsdnExportService.doDebitIsdnValidate(stockTransDTO, lstStockTransDetail);
    }

    @Test
    public void testDoDebitIsdnValidate_6() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setRegionStockId(null);

        StockTransDetailDTO stockTransDetail = new StockTransDetailDTO();
        stockTransDetail.setProdOfferId(1L);
        stockTransDetail.setStateId(1L);
        stockTransDetail.setQuantity(1L);

        List<StockTotalDTO> lstStockTotal = Lists.newArrayList();
        Mockito.doReturn(null).when(stockTotalService).searchStockTotal(any());
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        baseIsdnExportService.doDebitIsdnValidate(stockTransDTO, lstStockTransDetail);
    }

    @Test
    public void testDoDebitIsdnValidate_7() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setRegionStockId(null);

        StockTransDetailDTO stockTransDetail = new StockTransDetailDTO();
        stockTransDetail.setProdOfferId(1L);
        stockTransDetail.setStateId(1L);
        stockTransDetail.setQuantity(2L);

        StockTotalDTO stockTotalDTO = new StockTotalDTO();
        stockTotalDTO.setAvailableQuantity(1L);

        List<StockTotalDTO> lstStockTotal = Lists.newArrayList(stockTotalDTO);
        Mockito.doReturn(lstStockTotal).when(stockTotalService).searchStockTotal(any());
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        baseIsdnExportService.doDebitIsdnValidate(stockTransDTO, lstStockTransDetail);
    }

    @Test
    public void testDoDebitIsdnValidate_8() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setRegionStockId(null);

        StockTransDetailDTO stockTransDetail = new StockTransDetailDTO();
        stockTransDetail.setProdOfferId(1L);
        stockTransDetail.setStateId(1L);
        stockTransDetail.setQuantity(1000000001L);

        StockTotalDTO stockTotalDTO = new StockTotalDTO();
        stockTotalDTO.setAvailableQuantity(1000000002L);

        List<StockTotalDTO> lstStockTotal = Lists.newArrayList(stockTotalDTO);
        Mockito.doReturn(lstStockTotal).when(stockTotalService).searchStockTotal(any());
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        baseIsdnExportService.doDebitIsdnValidate(stockTransDTO, lstStockTransDetail);
    }

    @Test
    public void testDoDebitIsdnValidate_9() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setRegionStockId(null);

        StockTransDetailDTO stockTransDetail = new StockTransDetailDTO();
        stockTransDetail.setProdOfferId(1L);
        stockTransDetail.setStateId(1L);
        stockTransDetail.setQuantity(10000000L);

        StockTotalDTO stockTotalDTO = new StockTotalDTO();
        stockTotalDTO.setAvailableQuantity(1000000002L);

        List<StockTotalDTO> lstStockTotal = Lists.newArrayList(stockTotalDTO);
        Mockito.doReturn(lstStockTotal).when(stockTotalService).searchStockTotal(any());
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        baseIsdnExportService.doDebitIsdnValidate(stockTransDTO, lstStockTransDetail);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseIsdnExportService.doValidateStockTransDetail method **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoValidateStockTransDetail_1() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        baseIsdnExportService.doValidateStockTransDetail(null);
    }

    @Test
    public void testDoValidateStockTransDetail_2() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetailDTO);

        ProductOfferingDTO prodOffering = new ProductOfferingDTO();
        Mockito.doReturn(prodOffering).when(productOfferingService).findOne(any());
        prodOffering.setProductOfferTypeId(4L);
        baseIsdnExportService.doValidateStockTransDetail(lstStockTransDetail);
    }

    @Test
    public void testDoValidateStockTransDetail_3() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetailDTO);

        ProductOfferingDTO prodOffering = new ProductOfferingDTO();
        Mockito.doReturn(prodOffering).when(productOfferingService).findOne(any());
        prodOffering.setProductOfferTypeId(2L);
        prodOffering.setStatus("2");
        baseIsdnExportService.doValidateStockTransDetail(lstStockTransDetail);
    }

    @Test
    public void testDoValidateStockTransDetail_4() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetailDTO);

        ProductOfferingDTO prodOffering = new ProductOfferingDTO();
        Mockito.doReturn(prodOffering).when(productOfferingService).findOne(any());
        prodOffering.setProductOfferTypeId(3L);
        prodOffering.setStatus("2");
        baseIsdnExportService.doValidateStockTransDetail(lstStockTransDetail);
    }

    @Test
    public void testDoValidateStockTransDetail_5() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        List<StockTransSerialDTO> lstStockTransSerialDTO = Lists.newArrayList(stockTransSerialDTO);
        stockTransDetailDTO.setLstStockTransSerial(lstStockTransSerialDTO);
        stockTransDetailDTO.setQuantity(2L);
        stockTransSerialDTO.setQuantity(3L);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetailDTO);

        ProductOfferingDTO prodOffering = new ProductOfferingDTO();
        Mockito.doReturn(prodOffering).when(productOfferingService).findOne(any());
        prodOffering.setProductOfferTypeId(1L);
        prodOffering.setStatus("1");

        baseIsdnExportService.doValidateStockTransDetail(lstStockTransDetail);
    }

    @Test
    public void testDoValidateStockTransDetail_6() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        List<StockTransSerialDTO> lstStockTransSerialDTO = Lists.newArrayList();
        stockTransDetailDTO.setQuantity(2L);
        stockTransSerialDTO.setQuantity(3L);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetailDTO);

        ProductOfferingDTO prodOffering = new ProductOfferingDTO();
        Mockito.doReturn(prodOffering).when(productOfferingService).findOne(any());
        prodOffering.setProductOfferTypeId(1L);
        prodOffering.setStatus("1");

        baseIsdnExportService.doValidateStockTransDetail(lstStockTransDetail);
    }

    @Test
    public void testDoValidateStockTransDetail_7() throws Exception {

        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        stockTransDetailDTO.setQuantity(3L);
        stockTransSerialDTO.setQuantity(3L);
        List<StockTransSerialDTO> lstStockTransSerialDTO = Lists.newArrayList(stockTransSerialDTO);
        stockTransDetailDTO.setLstStockTransSerial(lstStockTransSerialDTO);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetailDTO);

        ProductOfferingDTO prodOffering = new ProductOfferingDTO();
        Mockito.doReturn(prodOffering).when(productOfferingService).findOne(any());
        prodOffering.setProductOfferTypeId(1L);
        prodOffering.setStatus("1");

        baseIsdnExportService.doValidateStockTransDetail(lstStockTransDetail);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseIsdnExportService.doSaveStockTransSerial method --- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoSaveStockTransSerial_1() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setCreateDatetime(new Date());
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        List<StockTransSerialDTO> stockTransSerialDTOS = Lists.newArrayList(stockTransSerialDTO);
        stockTransDetailDTO.setLstStockTransSerial(stockTransSerialDTOS);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetailDTO);
        StockTransSerialDTO stockTransSerialDTO1 = new StockTransSerialDTO();
        Mockito.doReturn(stockTransSerialDTO1).when(stockTransSerialService).save(any());
        baseIsdnExportService.doSaveStockTransSerial(stockTransDTO, lstStockTransDetail);
    }

    @Test
    public void testDoSaveStockTransSerial_2() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setCreateDatetime(new Date());
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        List<StockTransSerialDTO> stockTransSerialDTOS = Lists.newArrayList();
        stockTransDetailDTO.setLstStockTransSerial(stockTransSerialDTOS);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetailDTO);
        StockTransSerialDTO stockTransSerialDTO1 = new StockTransSerialDTO();
        Mockito.doReturn(stockTransSerialDTO1).when(stockTransSerialService).save(any());
        baseIsdnExportService.doSaveStockTransSerial(stockTransDTO, lstStockTransDetail);
    }

    @Test
    public void testDoSaveStockTransSerial_3() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setCreateDatetime(new Date());
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        List<StockTransSerialDTO> stockTransSerialDTOS = Lists.newArrayList();
        stockTransDetailDTO.setLstStockTransSerial(stockTransSerialDTOS);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        StockTransSerialDTO stockTransSerialDTO1 = new StockTransSerialDTO();
        Mockito.doReturn(stockTransSerialDTO1).when(stockTransSerialService).save(any());
        baseIsdnExportService.doSaveStockTransSerial(stockTransDTO, lstStockTransDetail);
    }


    /** -------------------------------------------------------------------- **/
    /** --- Test for baseIsdnExportService.doSaveStockGoods method --------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoSaveStockGoods_1() throws Exception {
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseIsdnExportService.doSaveRegionStockTrans method --- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoSaveRegionStockTrans_1() throws Exception {
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseIsdnExportService.doSaveStockTotal method --------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoSaveStockTotal_1() throws Exception {
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseIsdnExportService.doSyncLogistic method ----------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoSyncLogistic_1() throws Exception {
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseIsdnExportService.doIncreaseStockNum method ------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoIncreaseStockNum_1() throws Exception {

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        String prefixActionCode = "PX";
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue("1");
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        Mockito.doReturn(lstConfigEnableBccs1).when(optionSetValueService).getByOptionSetCode(any());

        StaffIm1DTO staff = new StaffIm1DTO();
        staff.setStockNum(1L);
        staff.setStockNumImp(1L);
        Mockito.doReturn(staff).when(staffIm1Service).findOne(any());

        StaffIm1DTO staffIm1DTO = new StaffIm1DTO();
        Mockito.doReturn(staffIm1DTO).when(staffIm1Service).save(any());
        baseIsdnExportService.doIncreaseStockNum(stockTransActionDTO, prefixActionCode, requiredRoleMap);
    }

    @Test
    public void testDoIncreaseStockNum_2() throws Exception {

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        String prefixActionCode = "PXS";
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue("1");
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        Mockito.doReturn(lstConfigEnableBccs1).when(optionSetValueService).getByOptionSetCode(any());

        StaffIm1DTO staff = new StaffIm1DTO();
        staff.setStockNum(1L);
        staff.setStockNumImp(1L);
        Mockito.doReturn(staff).when(staffIm1Service).findOne(any());

        StaffIm1DTO staffIm1DTO = new StaffIm1DTO();
        Mockito.doReturn(staffIm1DTO).when(staffIm1Service).save(any());
        baseIsdnExportService.doIncreaseStockNum(stockTransActionDTO, prefixActionCode, requiredRoleMap);
    }

    @Test
    public void testDoIncreaseStockNum_3() throws Exception {

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        String prefixActionCode = "PN";
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue("1");
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        Mockito.doReturn(lstConfigEnableBccs1).when(optionSetValueService).getByOptionSetCode(any());

        StaffIm1DTO staff = new StaffIm1DTO();
        staff.setStockNum(1L);
        staff.setStockNumImp(1L);
        Mockito.doReturn(staff).when(staffIm1Service).findOne(any());

        StaffIm1DTO staffIm1DTO = new StaffIm1DTO();
        Mockito.doReturn(staffIm1DTO).when(staffIm1Service).save(any());
        baseIsdnExportService.doIncreaseStockNum(stockTransActionDTO, prefixActionCode, requiredRoleMap);
    }

    @Test
    public void testDoIncreaseStockNum_4() throws Exception {

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        String prefixActionCode = "LN";
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue("1");
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        Mockito.doReturn(lstConfigEnableBccs1).when(optionSetValueService).getByOptionSetCode(any());

        StaffIm1DTO staff = new StaffIm1DTO();
        staff.setStockNum(1L);
        staff.setStockNumImp(1L);
        Mockito.doReturn(staff).when(staffIm1Service).findOne(any());

        StaffIm1DTO staffIm1DTO = new StaffIm1DTO();
        Mockito.doReturn(staffIm1DTO).when(staffIm1Service).save(any());
        baseIsdnExportService.doIncreaseStockNum(stockTransActionDTO, prefixActionCode, requiredRoleMap);
    }

    @Test
    public void testDoIncreaseStockNum_5() throws Exception {

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        String prefixActionCode = "LCCN";
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue("1");
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        Mockito.doReturn(lstConfigEnableBccs1).when(optionSetValueService).getByOptionSetCode(any());

        StaffIm1DTO staff = new StaffIm1DTO();
        staff.setStockNum(1L);
        staff.setStockNumImp(1L);
        Mockito.doReturn(staff).when(staffIm1Service).findOne(any());

        StaffIm1DTO staffIm1DTO = new StaffIm1DTO();
        Mockito.doReturn(staffIm1DTO).when(staffIm1Service).save(any());
        baseIsdnExportService.doIncreaseStockNum(stockTransActionDTO, prefixActionCode, requiredRoleMap);
    }

    @Test
    public void testDoIncreaseStockNum_6() throws Exception {

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        String prefixActionCode = "PX";
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue("1");
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        Mockito.doReturn(null).when(optionSetValueService).getByOptionSetCode(any());

        Mockito.doReturn(null).when(staffIm1Service).findOne(any());

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStockNum(1L);
        staffDTO.setStockNumImp(1L);
        Mockito.doReturn(staffDTO).when(staffService).findOne(any());
        Mockito.doReturn(staffDTO).when(staffService).save(any());

        baseIsdnExportService.doIncreaseStockNum(stockTransActionDTO, prefixActionCode, requiredRoleMap);
    }

    @Test
    public void testDoIncreaseStockNum_7() throws Exception {

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        String prefixActionCode = "PXS";
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue("1");
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        Mockito.doReturn(null).when(optionSetValueService).getByOptionSetCode(any());

        Mockito.doReturn(null).when(staffIm1Service).findOne(any());

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStockNum(1L);
        staffDTO.setStockNumImp(1L);
        Mockito.doReturn(staffDTO).when(staffService).findOne(any());
        Mockito.doReturn(staffDTO).when(staffService).save(any());

        baseIsdnExportService.doIncreaseStockNum(stockTransActionDTO, prefixActionCode, requiredRoleMap);
    }

    @Test
    public void testDoIncreaseStockNum_8() throws Exception {

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        String prefixActionCode = "PN";
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue("1");
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        Mockito.doReturn(null).when(optionSetValueService).getByOptionSetCode(any());

        Mockito.doReturn(null).when(staffIm1Service).findOne(any());

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStockNum(1L);
        staffDTO.setStockNumImp(1L);
        Mockito.doReturn(staffDTO).when(staffService).findOne(any());
        Mockito.doReturn(staffDTO).when(staffService).save(any());

        baseIsdnExportService.doIncreaseStockNum(stockTransActionDTO, prefixActionCode, requiredRoleMap);
    }

    @Test
    public void testDoIncreaseStockNum_9() throws Exception {

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        String prefixActionCode = "LN";
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue("1");
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        Mockito.doReturn(null).when(optionSetValueService).getByOptionSetCode(any());

        Mockito.doReturn(null).when(staffIm1Service).findOne(any());

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStockNum(1L);
        staffDTO.setStockNumImp(1L);
        Mockito.doReturn(staffDTO).when(staffService).findOne(any());
        Mockito.doReturn(staffDTO).when(staffService).save(any());

        baseIsdnExportService.doIncreaseStockNum(stockTransActionDTO, prefixActionCode, requiredRoleMap);
    }

    @Test
    public void testDoIncreaseStockNum_10() throws Exception {

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        String prefixActionCode = "LNC";
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue("1");
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        Mockito.doReturn(null).when(optionSetValueService).getByOptionSetCode(any());

        Mockito.doReturn(null).when(staffIm1Service).findOne(any());

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStockNum(1L);
        staffDTO.setStockNumImp(1L);
        Mockito.doReturn(staffDTO).when(staffService).findOne(any());
        Mockito.doReturn(staffDTO).when(staffService).save(any());

        baseIsdnExportService.doIncreaseStockNum(stockTransActionDTO, prefixActionCode, requiredRoleMap);
    }

    @Test
    public void testDoIncreaseStockNum_11() throws Exception {

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        String prefixActionCode = "LNC";
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue("1");
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        Mockito.doReturn(null).when(optionSetValueService).getByOptionSetCode(any());

        Mockito.doReturn(null).when(staffIm1Service).findOne(any());

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStockNum(1L);
        staffDTO.setStockNumImp(1L);
        Mockito.doReturn(null).when(staffService).findOne(any());
        Mockito.doReturn(staffDTO).when(staffService).save(any());

        baseIsdnExportService.doIncreaseStockNum(stockTransActionDTO, prefixActionCode, requiredRoleMap);
    }

    @Test
    public void testDoIncreaseStockNum_12() throws Exception {

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        String prefixActionCode = "LCCN";
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue("1");
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        Mockito.doReturn(lstConfigEnableBccs1).when(optionSetValueService).getByOptionSetCode(any());

        StaffIm1DTO staff = new StaffIm1DTO();
        staff.setStockNum(1L);
        staff.setStockNumImp(1L);
        Mockito.doReturn(null).when(staffIm1Service).findOne(any());

        StaffIm1DTO staffIm1DTO = new StaffIm1DTO();
        Mockito.doReturn(staffIm1DTO).when(staffIm1Service).save(any());
        baseIsdnExportService.doIncreaseStockNum(stockTransActionDTO, prefixActionCode, requiredRoleMap);
    }

    @Test
    public void testDoIncreaseStockNum_13() throws Exception {

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        String prefixActionCode = "LCCN";
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        //optionSetValueDTO.setValue("1");
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        Mockito.doReturn(lstConfigEnableBccs1).when(optionSetValueService).getByOptionSetCode(any());

        StaffIm1DTO staff = new StaffIm1DTO();
        staff.setStockNum(1L);
        staff.setStockNumImp(1L);
        Mockito.doReturn(staff).when(staffIm1Service).findOne(any());

        StaffIm1DTO staffIm1DTO = new StaffIm1DTO();
        Mockito.doReturn(staffIm1DTO).when(staffIm1Service).save(any());
        baseIsdnExportService.doIncreaseStockNum(stockTransActionDTO, prefixActionCode, requiredRoleMap);
    }

    @Test
    public void testDoIncreaseStockNum_14() throws Exception {

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        String prefixActionCode = "LCCN";
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue("2");
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        Mockito.doReturn(lstConfigEnableBccs1).when(optionSetValueService).getByOptionSetCode(any());

        StaffIm1DTO staff = new StaffIm1DTO();
        staff.setStockNum(1L);
        staff.setStockNumImp(1L);
        Mockito.doReturn(staff).when(staffIm1Service).findOne(any());

        StaffIm1DTO staffIm1DTO = new StaffIm1DTO();
        Mockito.doReturn(staffIm1DTO).when(staffIm1Service).save(any());
        baseIsdnExportService.doIncreaseStockNum(stockTransActionDTO, prefixActionCode, requiredRoleMap);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseIsdnExportService.doSaveStockTransDetail method --- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void doSaveStockTransDetail() throws Exception {
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseIsdnExportService.doUnlockUser method ------------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoUnlockUser_1() throws Exception {
        StockTransDTO stockTransDTO = new StockTransDTO();
        Mockito.doNothing().when(stockTransRepo).unlockUserInfo(any());
        baseIsdnExportService.doUnlockUser(stockTransDTO);
    }

}