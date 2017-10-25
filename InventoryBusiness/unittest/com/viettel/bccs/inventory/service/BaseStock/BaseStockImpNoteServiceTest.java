package com.viettel.bccs.inventory.service.BaseStock;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.StaffService;
import com.viettel.bccs.inventory.service.StockTransDetailService;
import com.viettel.bccs.inventory.service.StockTransSerialService;
import com.viettel.bccs.inventory.service.StockTransService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.ErrorCode;
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

import java.util.Calendar;
import java.util.List;

import static org.mockito.Matchers.any;

/**
 * Created by TruNV on 9/2/2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({BaseStockImpNoteService.class, BaseStockService.class})
public class BaseStockImpNoteServiceTest {

    @InjectMocks
    private BaseStockImpNoteService baseStockImpNoteService;

    @Mock
    private BaseValidateService baseValidateService;

    @Mock
    private StockTransService stockTransService;

    @Mock
    private StockTransDetailService stockTransDetailService;

    @Mock
    private StockTransSerialService stockTransSerialService;

    @Mock
    private StaffService staffService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void initMocks() throws NoSuchFieldException {
        MockitoAnnotations.initMocks(this);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockImpNoteService.doPrepareData method ---------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoPrepareData_1() throws Exception {
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setExchangeStockId(0L);
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        baseStockImpNoteService.doPrepareData(stockTransDTO, stockTransActionDTO, flagStockDTO);
    }

    @Test
    public void testDoPrepareData_2() throws Exception {
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setExchangeStockId(2L);
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        baseStockImpNoteService.doPrepareData(stockTransDTO, stockTransActionDTO, flagStockDTO);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockImpNoteService.doValidate method ------------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoValidate_1() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();

        Mockito.doNothing().when(baseValidateService).doOrderValidateCommon(any(), any());

        StockTransDTO oldStockTransDTO = new StockTransDTO();
        Mockito.doReturn(null).when(stockTransService).findOne(any());

        baseStockImpNoteService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    @Test
    public void testDoValidate_2() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();

        Mockito.doNothing().when(baseValidateService).doOrderValidateCommon(any(), any());

        StockTransDTO oldStockTransDTO = new StockTransDTO();
        oldStockTransDTO.setStatus("6");
        Mockito.doReturn(oldStockTransDTO).when(stockTransService).findOne(any());

        baseStockImpNoteService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    @Test
    public void testDoValidate_3() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();

        Mockito.doNothing().when(baseValidateService).doOrderValidateCommon(any(), any());

        StockTransDTO oldStockTransDTO = new StockTransDTO();
        oldStockTransDTO.setStatus("5");
        Mockito.doReturn(oldStockTransDTO).when(stockTransService).findOne(any());

        baseStockImpNoteService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    @Test
    public void testDoValidate_4() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetailDTO);

        Mockito.doNothing().when(baseValidateService).doOrderValidateCommon(any(), any());

        StockTransDTO oldStockTransDTO = new StockTransDTO();
        oldStockTransDTO.setStatus("5");
        Mockito.doReturn(oldStockTransDTO).when(stockTransService).findOne(any());
        Mockito.doNothing().when(baseValidateService).doSignVofficeValidate(any());
        Mockito.doNothing().when(baseValidateService).doDebitValidate(any(), any());

        baseStockImpNoteService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockImpNoteService.doSaveStockTransDetail method - **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoSaveStockTransDetail_1() throws Exception {
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockImpNoteService.doSaveStockTransSerial method - **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoSaveStockTransSerial_1() throws Exception {
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setRegionStockTransId(0L);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        baseStockImpNoteService.doSaveStockTransSerial(stockTransDTO, lstStockTransDetail);
    }

    @Test
    public void testDoSaveStockTransSerial_2() throws Exception {
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setRegionStockTransId(1L);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();

        StockTransDTO stockTrans = new StockTransDTO();
        Mockito.doReturn(stockTrans).when(stockTransService).findOne(any());

        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> lstStockTransDetailRegion = Lists.newArrayList();
        Mockito.doReturn(lstStockTransDetailRegion).when(stockTransDetailService).findByFilter(any());
        baseStockImpNoteService.doSaveStockTransSerial(stockTransDTO, lstStockTransDetail);
    }

    @Test
    public void testDoSaveStockTransSerial_3() throws Exception {
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setRegionStockTransId(1L);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();

        StockTransDTO stockTrans = new StockTransDTO();
        Mockito.doReturn(stockTrans).when(stockTransService).findOne(any());

        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> lstStockTransDetailRegion = Lists.newArrayList(stockTransDetailDTO);
        Mockito.doReturn(lstStockTransDetailRegion).when(stockTransDetailService).findByFilter(any());
        baseStockImpNoteService.doSaveStockTransSerial(stockTransDTO, lstStockTransDetail);
    }

    @Test
    public void testDoSaveStockTransSerial_4() throws Exception {
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setRegionStockTransId(1L);
        StockTransDetailDTO stockTransDetailDTO1 = new StockTransDetailDTO();
        stockTransDetailDTO1.setProdOfferId(1L);
        stockTransDetailDTO1.setStateId(1L);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetailDTO1);

        StockTransDTO stockTrans = new StockTransDTO();
        Mockito.doReturn(stockTrans).when(stockTransService).findOne(any());

        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId(1L);
        stockTransDetailDTO.setStateId(1L);
        List<StockTransDetailDTO> lstStockTransDetailRegion = Lists.newArrayList(stockTransDetailDTO);
        Mockito.doReturn(lstStockTransDetailRegion).when(stockTransDetailService).findByFilter(any());

        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        stockTransSerialDTO.setCreateDatetime( Calendar.getInstance().getTime());
        List<StockTransSerialDTO> lstStockTransSerial = Lists.newArrayList(stockTransSerialDTO);
        Mockito.doReturn(lstStockTransSerial).when(stockTransSerialService).findByStockTransDetailId(any());
        StockTransSerialDTO stockTransSerialDTO1 = new StockTransSerialDTO();
        Mockito.doReturn(stockTransSerialDTO1).when(stockTransSerialService).save(any());
        baseStockImpNoteService.doSaveStockTransSerial(stockTransDTO, lstStockTransDetail);
    }

    @Test
    public void testDoSaveStockTransSerial_5() throws Exception {
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setRegionStockTransId(1L);
        StockTransDetailDTO stockTransDetailDTO1 = new StockTransDetailDTO();
        stockTransDetailDTO1.setProdOfferId(1L);
        stockTransDetailDTO1.setStateId(1L);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetailDTO1);

        StockTransDTO stockTrans = new StockTransDTO();
        Mockito.doReturn(stockTrans).when(stockTransService).findOne(any());

        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId(1L);
        stockTransDetailDTO.setStateId(1L);
        List<StockTransDetailDTO> lstStockTransDetailRegion = Lists.newArrayList(stockTransDetailDTO);
        Mockito.doReturn(lstStockTransDetailRegion).when(stockTransDetailService).findByFilter(any());

        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        stockTransSerialDTO.setCreateDatetime( Calendar.getInstance().getTime());
        List<StockTransSerialDTO> lstStockTransSerial = Lists.newArrayList(stockTransSerialDTO);
        Mockito.doReturn(lstStockTransSerial).when(stockTransSerialService).findByStockTransDetailId(any());
        StockTransSerialDTO stockTransSerialDTO1 = new StockTransSerialDTO();
        Mockito.doReturn(stockTransSerialDTO1).when(stockTransSerialService).save(any());
        BaseStockImpNoteService spy = PowerMockito.spy(baseStockImpNoteService);
        StockTransDetailDTO stocktransDetail = new StockTransDetailDTO();
        PowerMockito.doReturn(stocktransDetail).when(spy, "findStockTransDetailRegion", any(), any(), any());
        baseStockImpNoteService.doSaveStockTransSerial(stockTransDTO, lstStockTransDetail);
    }

    @Test
    public void testDoSaveStockTransSerial_6() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setRegionStockTransId(1L);
        StockTransDetailDTO stockTransDetailDTO1 = new StockTransDetailDTO();
        stockTransDetailDTO1.setProdOfferId(1L);
        stockTransDetailDTO1.setStateId(1L);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetailDTO1);

        StockTransDTO stockTrans = new StockTransDTO();
        Mockito.doReturn(stockTrans).when(stockTransService).findOne(any());

        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId(1L);
        stockTransDetailDTO.setStateId(1L);
        List<StockTransDetailDTO> lstStockTransDetailRegion = Lists.newArrayList(stockTransDetailDTO);
        Mockito.doReturn(lstStockTransDetailRegion).when(stockTransDetailService).findByFilter(any());

        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        stockTransSerialDTO.setCreateDatetime( Calendar.getInstance().getTime());
        List<StockTransSerialDTO> lstStockTransSerial = Lists.newArrayList(stockTransSerialDTO);
        Mockito.doReturn(null).when(stockTransSerialService).findByStockTransDetailId(any());
        StockTransSerialDTO stockTransSerialDTO1 = new StockTransSerialDTO();
        Mockito.doReturn(stockTransSerialDTO1).when(stockTransSerialService).save(any());
        BaseStockImpNoteService spy = PowerMockito.spy(baseStockImpNoteService);
        StockTransDetailDTO stocktransDetail = new StockTransDetailDTO();
        PowerMockito.doReturn(stocktransDetail).when(spy, "findStockTransDetailRegion", any(), any(), any());
        baseStockImpNoteService.doSaveStockTransSerial(stockTransDTO, lstStockTransDetail);
    }

    @Test
    public void testDoSaveStockTransSerial_7() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setRegionStockTransId(1L);
        StockTransDetailDTO stockTransDetailDTO1 = new StockTransDetailDTO();
        stockTransDetailDTO1.setProdOfferId(1L);
        stockTransDetailDTO1.setStateId(1L);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();

        StockTransDTO stockTrans = new StockTransDTO();
        Mockito.doReturn(stockTrans).when(stockTransService).findOne(any());

        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId(1L);
        stockTransDetailDTO.setStateId(1L);
        List<StockTransDetailDTO> lstStockTransDetailRegion = Lists.newArrayList(stockTransDetailDTO);
        Mockito.doReturn(lstStockTransDetailRegion).when(stockTransDetailService).findByFilter(any());

        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        stockTransSerialDTO.setCreateDatetime( Calendar.getInstance().getTime());
        List<StockTransSerialDTO> lstStockTransSerial = Lists.newArrayList(stockTransSerialDTO);
        Mockito.doReturn(null).when(stockTransSerialService).findByStockTransDetailId(any());
        StockTransSerialDTO stockTransSerialDTO1 = new StockTransSerialDTO();
        Mockito.doReturn(stockTransSerialDTO1).when(stockTransSerialService).save(any());
        BaseStockImpNoteService spy = PowerMockito.spy(baseStockImpNoteService);
        StockTransDetailDTO stocktransDetail = new StockTransDetailDTO();
        PowerMockito.doReturn(stocktransDetail).when(spy, "findStockTransDetailRegion", any(), any(), any());
        baseStockImpNoteService.doSaveStockTransSerial(stockTransDTO, lstStockTransDetail);
    }

    @Test
    public void testDoSaveStockTransSerial_8() throws Exception {
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setRegionStockTransId(1L);
        StockTransDetailDTO stockTransDetailDTO1 = new StockTransDetailDTO();
        stockTransDetailDTO1.setProdOfferId(2L);
        stockTransDetailDTO1.setStateId(2L);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetailDTO1);

        StockTransDTO stockTrans = new StockTransDTO();
        Mockito.doReturn(stockTrans).when(stockTransService).findOne(any());

        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId(1L);
        stockTransDetailDTO.setStateId(1L);
        List<StockTransDetailDTO> lstStockTransDetailRegion = Lists.newArrayList();
        Mockito.doReturn(lstStockTransDetailRegion).when(stockTransDetailService).findByFilter(any());

        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        stockTransSerialDTO.setCreateDatetime( Calendar.getInstance().getTime());
        List<StockTransSerialDTO> lstStockTransSerial = Lists.newArrayList(stockTransSerialDTO);
        Mockito.doReturn(lstStockTransSerial).when(stockTransSerialService).findByStockTransDetailId(any());
        StockTransSerialDTO stockTransSerialDTO1 = new StockTransSerialDTO();
        Mockito.doReturn(stockTransSerialDTO1).when(stockTransSerialService).save(any());
        baseStockImpNoteService.doSaveStockTransSerial(stockTransDTO, lstStockTransDetail);
    }

    /** -------------------------------------------------------------------- **/
    /** --Test for baseStockImpNoteService.findStockTransDetailRegion method **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testFindStockTransDetailRegion_1() throws Exception {
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId(1L);
        stockTransDetailDTO.setStateId(1L);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetailDTO);
        Whitebox.invokeMethod(baseStockImpNoteService, "findStockTransDetailRegion", lstStockTransDetail, 1L, 1L);
    }

    @Test
    public void testFindStockTransDetailRegion_2() throws Exception {
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId(1L);
        stockTransDetailDTO.setStateId(1L);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetailDTO);
        Whitebox.invokeMethod(baseStockImpNoteService, "findStockTransDetailRegion", lstStockTransDetail, 2L, 1L);
    }

    @Test
    public void testFindStockTransDetailRegion_3() throws Exception {
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId(1L);
        stockTransDetailDTO.setStateId(1L);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetailDTO);
        Whitebox.invokeMethod(baseStockImpNoteService, "findStockTransDetailRegion", lstStockTransDetail, 1L, 2L);
    }

    @Test
    public void testFindStockTransDetailRegion_4() throws Exception {
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId(1L);
        stockTransDetailDTO.setStateId(1L);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetailDTO);
        Whitebox.invokeMethod(baseStockImpNoteService, "findStockTransDetailRegion", lstStockTransDetail, 2L, 2L);
    }

    @Test
    public void testFindStockTransDetailRegion_5() throws Exception {
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId(1L);
        stockTransDetailDTO.setStateId(1L);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        Whitebox.invokeMethod(baseStockImpNoteService, "findStockTransDetailRegion", lstStockTransDetail, 2L, 2L);
    }
}