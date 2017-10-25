package com.viettel.bccs.inventory.service.BaseIsdn;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.BaseStock.BaseStockService;
import com.viettel.bccs.inventory.service.BaseStock.BaseValidateService;
import com.viettel.bccs.inventory.service.ReasonService;
import com.viettel.bccs.inventory.service.ShopService;
import com.viettel.bccs.inventory.service.StockTotalService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
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
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;

import static org.mockito.Matchers.any;

/**
 * Created by TruNV on 9/8/2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({DataUtil.class, BaseStockService.class})
@PowerMockIgnore("javax.management.*")
public class BaseIsdnOrderServiceTest {

    @InjectMocks
    private BaseIsdnOrderService baseIsdnOrderService;

    @Mock
    private BaseValidateService baseValidateService;

    @Mock
    private ReasonService reasonService;

    @Mock
    private ShopService shopService;

    @Mock
    private StockTotalService stockTotalService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseIsdnOrderService.doPrepareData method ------------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoPrepareData_1() throws Exception {
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        baseIsdnOrderService.doPrepareData(stockTransDTO, stockTransActionDTO, flagStockDTO);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseIsdnOrderService.doValidate method ---------------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoValidate_1() throws Exception {
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetailDTO);
        BaseIsdnOrderService spyService = Mockito.spy(baseIsdnOrderService);
        Mockito.doNothing().when(spyService).doOrderValidate(any());
        Mockito.doNothing().when(baseValidateService).doSignVofficeValidate(any());
        Mockito.doNothing().when(spyService).doDebitIsdnValidate(any(), any());
        spyService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseIsdnOrderService.doOrderValidate method ----------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoOrderValidate_1() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setFromOwnerId(null);
        stockTransDTO.setFromOwnerType(null);
        baseIsdnOrderService.doOrderValidate(stockTransDTO);
    }

    @Test
    public void testDoOrderValidate_2() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setFromOwnerId(null);
        stockTransDTO.setFromOwnerType(1L);
        baseIsdnOrderService.doOrderValidate(stockTransDTO);
    }

    @Test
    public void testDoOrderValidate_3() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setFromOwnerId(1L);
        stockTransDTO.setFromOwnerType(null);
        baseIsdnOrderService.doOrderValidate(stockTransDTO);
    }

    @Test
    public void testDoOrderValidate_4() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setFromOwnerId(1L);
        stockTransDTO.setFromOwnerType(1L);

        ShopDTO fromShop = new ShopDTO();
        Mockito.doReturn(null).when(shopService).findOne(any());
        baseIsdnOrderService.doOrderValidate(stockTransDTO);
    }

    @Test
    public void testDoOrderValidate_5() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setFromOwnerId(1L);
        stockTransDTO.setFromOwnerType(1L);

        ShopDTO fromShop = new ShopDTO();
        fromShop.setStatus("2");
        Mockito.doReturn(fromShop).when(shopService).findOne(any());
        baseIsdnOrderService.doOrderValidate(stockTransDTO);
    }

    @Test
    public void testDoOrderValidate_6() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setFromOwnerId(1L);
        stockTransDTO.setFromOwnerType(1L);
        stockTransDTO.setToOwnerId(0L);
        stockTransDTO.setToOwnerType(0L);

        ShopDTO fromShop = new ShopDTO();
        fromShop.setStatus("1");

        Mockito.doReturn(fromShop).when(shopService).findOne(any());
        baseIsdnOrderService.doOrderValidate(stockTransDTO);
    }

    @Test
    public void testDoOrderValidate_7() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setFromOwnerId(1L);
        stockTransDTO.setFromOwnerType(1L);
        stockTransDTO.setToOwnerId(0L);
        stockTransDTO.setToOwnerType(1L);

        ShopDTO fromShop = new ShopDTO();
        fromShop.setStatus("1");

        Mockito.doReturn(fromShop).when(shopService).findOne(any());
        baseIsdnOrderService.doOrderValidate(stockTransDTO);
    }

    @Test
    public void testDoOrderValidate_8() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setFromOwnerId(1L);
        stockTransDTO.setFromOwnerType(1L);
        stockTransDTO.setToOwnerId(1L);
        stockTransDTO.setToOwnerType(0L);

        ShopDTO fromShop = new ShopDTO();
        fromShop.setStatus("1");

        Mockito.doReturn(fromShop).when(shopService).findOne(any());
        baseIsdnOrderService.doOrderValidate(stockTransDTO);
    }

    @Test
    public void testDoOrderValidate_9() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setFromOwnerId(1L);
        stockTransDTO.setFromOwnerType(1L);
        stockTransDTO.setToOwnerId(1L);
        stockTransDTO.setToOwnerType(1L);

        ShopDTO fromShop = new ShopDTO();
        fromShop.setStatus("1");

        Mockito.doReturn(fromShop).when(shopService).findOne(any());
        baseIsdnOrderService.doOrderValidate(stockTransDTO);
    }

    @Test
    public void testDoOrderValidate_10() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setFromOwnerId(1L);
        stockTransDTO.setFromOwnerType(1L);
        stockTransDTO.setToOwnerId(2L);
        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setReasonId(0L);

        ShopDTO fromShop = new ShopDTO();
        fromShop.setStatus("1");

        Mockito.doReturn(fromShop).when(shopService).findOne(any());
        baseIsdnOrderService.doOrderValidate(stockTransDTO);
    }

    @Test
    public void testDoOrderValidate_11() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setFromOwnerId(1L);
        stockTransDTO.setFromOwnerType(1L);
        stockTransDTO.setToOwnerId(2L);
        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setReasonId(1L);

        ShopDTO fromShop = new ShopDTO();
        fromShop.setStatus("1");

        Mockito.doReturn(fromShop).when(shopService).findOne(any());

        ReasonDTO reason = new ReasonDTO();
        Mockito.doReturn(null).when(reasonService).findOne(any());
        baseIsdnOrderService.doOrderValidate(stockTransDTO);
    }

    @Test
    public void testDoOrderValidate_12() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setFromOwnerId(1L);
        stockTransDTO.setFromOwnerType(1L);
        stockTransDTO.setToOwnerId(2L);
        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setReasonId(1L);

        ShopDTO fromShop = new ShopDTO();
        fromShop.setStatus("1");

        Mockito.doReturn(fromShop).when(shopService).findOne(any());

        ReasonDTO reason = new ReasonDTO();
        reason.setReasonId(0L);
        Mockito.doReturn(reason).when(reasonService).findOne(any());
        baseIsdnOrderService.doOrderValidate(stockTransDTO);
    }

    @Test
    public void testDoOrderValidate_13() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setFromOwnerId(1L);
        stockTransDTO.setFromOwnerType(1L);
        stockTransDTO.setToOwnerId(2L);
        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setReasonId(1L);
        stockTransDTO.setNote("12345678901234567890123456789012345678901234567890123456789012345678901" +
                "3456789012345678901234567890123456789012345678901234567890123456789012" +
                "34567890123456789012345678901234567890123456789012345678901234567890123456789012345678901" +
                "23456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012" +
                "345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123" +
                "45678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890" +
                "12345678901234567890123456789012345678901234567890");

        ShopDTO fromShop = new ShopDTO();
        fromShop.setStatus("1");

        Mockito.doReturn(fromShop).when(shopService).findOne(any());

        ReasonDTO reason = new ReasonDTO();
        reason.setReasonId(1L);
        Mockito.doReturn(reason).when(reasonService).findOne(any());
        baseIsdnOrderService.doOrderValidate(stockTransDTO);
    }

    @Test
    public void testDoOrderValidate_14() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setFromOwnerId(1L);
        stockTransDTO.setFromOwnerType(1L);
        stockTransDTO.setToOwnerId(2L);
        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setReasonId(1L);
        stockTransDTO.setNote(null);

        ShopDTO fromShop = new ShopDTO();
        fromShop.setStatus("1");

        Mockito.doReturn(fromShop).when(shopService).findOne(any());

        ReasonDTO reason = new ReasonDTO();
        reason.setReasonId(1L);
        Mockito.doReturn(reason).when(reasonService).findOne(any());
        baseIsdnOrderService.doOrderValidate(stockTransDTO);
    }

    @Test
    public void testDoOrderValidate_15() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setFromOwnerId(1L);
        stockTransDTO.setFromOwnerType(1L);
        stockTransDTO.setToOwnerId(2L);
        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setReasonId(1L);
        stockTransDTO.setNote("123");

        ShopDTO fromShop = new ShopDTO();
        fromShop.setStatus("1");

        Mockito.doReturn(fromShop).when(shopService).findOne(any());

        ReasonDTO reason = new ReasonDTO();
        reason.setReasonId(1L);
        Mockito.doReturn(reason).when(reasonService).findOne(any());
        baseIsdnOrderService.doOrderValidate(stockTransDTO);
    }


    /** -------------------------------------------------------------------- **/
    /** --- Test for baseIsdnOrderService.doDebitIsdnValidate method ------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoDebitIsdnValidate_1() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetailDTO);
        baseIsdnOrderService.doDebitIsdnValidate(stockTransDTO, null);
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
        baseIsdnOrderService.doDebitIsdnValidate(stockTransDTO, lstStockTransDetail);
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
        baseIsdnOrderService.doDebitIsdnValidate(stockTransDTO, lstStockTransDetail);
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
        baseIsdnOrderService.doDebitIsdnValidate(stockTransDTO, lstStockTransDetail);
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
        baseIsdnOrderService.doDebitIsdnValidate(stockTransDTO, lstStockTransDetail);
    }

    @Test
    public void testDoDebitIsdnValidate_6() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setRegionStockId(null);

        StockTransDetailDTO stockTransDetail = new StockTransDetailDTO();
        stockTransDetail.setProdOfferId(1L);
        stockTransDetail.setStateId(1L);
        stockTransDetail.setQuantity(1L);

        List<StockTotalDTO> lstStockTotal = Lists.newArrayList();
        Mockito.doReturn(null).when(stockTotalService).searchStockTotal(any());
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetail);
        baseIsdnOrderService.doDebitIsdnValidate(stockTransDTO, lstStockTransDetail);
    }

    @Test
    public void testDoDebitIsdnValidate_7() throws Exception {

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
        baseIsdnOrderService.doDebitIsdnValidate(stockTransDTO, lstStockTransDetail);
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
        baseIsdnOrderService.doDebitIsdnValidate(stockTransDTO, lstStockTransDetail);
    }

    @Test
    public void testDoDebitIsdnValidate_9() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

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
        baseIsdnOrderService.doDebitIsdnValidate(stockTransDTO, lstStockTransDetail);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseIsdnOrderService.doSaveStockTransSerial method ---- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoSaveStockTransSerial_1() throws Exception {
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseIsdnOrderService.doSaveStockGoods method ---------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoSaveStockGoods_1() throws Exception {
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseIsdnOrderService.doSaveRegionStockTrans method ---- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoSaveRegionStockTrans_1() throws Exception {
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseIsdnOrderService.doSaveStockTotal method ---------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void doSaveStockTotal() throws Exception {
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseIsdnOrderService.doSyncLogistic method ------------ **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoSyncLogistic_1() throws Exception {
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseIsdnOrderService.doIncreaseStockNum method -------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoIncreaseStockNum_1() throws Exception {
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseIsdnOrderService.doUnlockUser method -------------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoUnlockUser_1() throws Exception {
    }

}