package com.viettel.bccs.inventory.service.BaseStock;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.ProductOfferPriceService;
import com.viettel.bccs.inventory.service.ProductOfferingService;
import com.viettel.bccs.inventory.service.ShopService;
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
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;

import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;

/**
 * Created by TruNV on 9/2/2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({BaseStockAgentRetrieveService.class, BaseStockService.class})
public class BaseStockAgentRetrieveServiceTest {

    @InjectMocks
    private BaseStockAgentRetrieveService baseStockAgentRetrieveService;

    @Mock
    private EntityManager em;

    @Mock
    private BaseValidateService baseValidateService;

    @Mock
    private ShopService shopService;

    @Mock
    private ProductOfferPriceService productOfferPriceService;

    @Mock
    private ProductOfferingService productOfferingService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void initMocks() throws NoSuchFieldException {
        MockitoAnnotations.initMocks(this);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockAgentRetrieveService.doPrepareData method ---- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoPrepareData_1() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        BaseStockAgentRetrieveService spy = PowerMockito.spy(baseStockAgentRetrieveService);
        PowerMockito.doReturn( Calendar.getInstance().getTime()).when(spy).getSysDate(any());

        spy.doPrepareData(stockTransDTO, stockTransActionDTO, flagStockDTO);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockAgentRetrieveService.doValidate method ------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoValidate_1() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setActionCode("123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        baseStockAgentRetrieveService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    @Test
    public void testDoValidate_2() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setActionCode(null);
        stockTransDTO.setNote("1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901" +
                "234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567" +
                "890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678" +
                "901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890" +
                "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456" +
                "789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012" +
                "3456789012345678901234567890123456789012345678901234567890");
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setActionCode(null);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        baseStockAgentRetrieveService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    @Test
    public void testDoValidate_3() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setNote(null);
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setActionCode("12312");
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();

        Mockito.doNothing().when(baseValidateService).doDebitValidate(any(), any());
        Mockito.doNothing().when(baseValidateService).doQuantityAvailableValidate(any(), any());
        BaseStockAgentRetrieveService spy = PowerMockito.spy(baseStockAgentRetrieveService);
        PowerMockito.doNothing().when(spy,"validateAgentExp", any(), any());
        spy.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    @Test
    public void testDoValidate_4() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();

        stockTransDTO.setNote("121");
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransDTO.setActionCode("12312");
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();

        Mockito.doNothing().when(baseValidateService).doDebitValidate(any(), any());
        Mockito.doNothing().when(baseValidateService).doQuantityAvailableValidate(any(), any());
        BaseStockAgentRetrieveService spy = PowerMockito.spy(baseStockAgentRetrieveService);
        PowerMockito.doNothing().when(spy,"validateAgentExp", any(), any());
        spy.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockAgentRetrieveService.validateAgentExp method - **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testValidateAgentExp_1() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);

        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setActionCode("12312");
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        StockTransDTO stockTransDTO = new StockTransDTO();
        Whitebox.invokeMethod(baseStockAgentRetrieveService, "validateAgentExp", stockTransDTO, lstStockTransDetail);
    }

    @Test
    public void testValidateAgentExp_2() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);

        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setActionCode("12312");
        StockTransDetailDTO stockTransDetailDTO1 = new StockTransDetailDTO();
        stockTransDetailDTO1.setActionCode("12312");
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetailDTO, stockTransDetailDTO1);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setCode("1");
        Mockito.doReturn(productOfferingDTO).when(productOfferingService).findOne(any());
        StockTransDTO stockTransDTO = new StockTransDTO();
        Whitebox.invokeMethod(baseStockAgentRetrieveService, "validateAgentExp", stockTransDTO, lstStockTransDetail);
    }

    @Test
    public void testValidateAgentExp_3() throws Exception {

        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setActionCode("12312");
        StockTransDetailDTO stockTransDetailDTO1 = new StockTransDetailDTO();
        stockTransDetailDTO1.setActionCode("12312");
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetailDTO);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setCode("1");
        Mockito.doReturn(productOfferingDTO).when(productOfferingService).findOne(any());
        StockTransDTO stockTransDTO = new StockTransDTO();
        Whitebox.invokeMethod(baseStockAgentRetrieveService, "validateAgentExp", stockTransDTO, lstStockTransDetail);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockAgentRetrieveService.doUnlockUser method ----- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoUnlockUser_1() throws Exception {
    }

    /** -------------------------------------------------------------------- **/
    /** Test for baseStockAgentRetrieveService.doSaveStockTransSerial method **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoSaveStockTransSerial_1() throws Exception {
    }



}