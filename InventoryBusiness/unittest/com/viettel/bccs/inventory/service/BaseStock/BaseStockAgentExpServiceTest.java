package com.viettel.bccs.inventory.service.BaseStock;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.ProductOfferPriceService;
import com.viettel.bccs.inventory.service.ProductOfferingService;
import com.viettel.bccs.inventory.service.ShopService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
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

import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;

/**
 * Created by TruNV on 8/26/2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({DataUtil.class, BaseStockService.class})
@PowerMockIgnore("javax.management.*")
public class BaseStockAgentExpServiceTest {

    @InjectMocks
    private BaseStockAgentExpService baseStockAgentExpService;

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

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockAgentExpService.doPrepareData method --------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoPrepareData_1() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockAgent("1");
        stockTransDTO.setRegionStockId(1L);
        stockTransDTO.setNote("2");
        stockTransDTO.setLogicstic("1");

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        baseStockAgentExpService.doPrepareData(stockTransDTO, stockTransActionDTO, flagStockDTO);
    }

    @Test
    public void testDoPrepareData_2() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockAgent("2");
        stockTransDTO.setRegionStockId(1L);
        stockTransDTO.setNote(null);
        stockTransDTO.setLogicstic("2");

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        baseStockAgentExpService.doPrepareData(stockTransDTO, stockTransActionDTO, flagStockDTO);
    }

    @Test
    public void testDoPrepareData_3() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockAgent("2");
        stockTransDTO.setRegionStockId(null);
        stockTransDTO.setNote(null);
        stockTransDTO.setLogicstic("2");

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        baseStockAgentExpService.doPrepareData(stockTransDTO, stockTransActionDTO, flagStockDTO);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockAgentExpService.doValidate method ------------ **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoValidate_1() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        lstStockTransDetail.add(stockTransDetailDTO);

        Mockito.doNothing().when(baseValidateService).doOrderValidateAgent(any(), any());
        Mockito.doNothing().when(baseValidateService).doSignVofficeValidate(any());
        Mockito.doNothing().when(baseValidateService).doDebitValidate(any(), any());
        Mockito.doNothing().when(baseValidateService).doQuantityAvailableValidate(any(), any());

        ShopDTO shop = new ShopDTO();
        Mockito.doReturn(shop).when(shopService).findOne(any());
        Mockito.doReturn(false).when(shopService).checkChannelIsAgent(any());

        baseStockAgentExpService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    @Test
    public void testDoValidate_2() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        lstStockTransDetail.add(stockTransDetailDTO);

        BaseStockAgentExpService spyService = PowerMockito.spy(baseStockAgentExpService);
        ShopDTO shop = new ShopDTO();
        Mockito.doReturn(shop).when(shopService).findOne(any());
        Mockito.doReturn(true).when(shopService).checkChannelIsAgent(any());

        Mockito.doNothing().when(baseValidateService).doOrderValidateAgent(any(), any());
        Mockito.doNothing().when(baseValidateService).doSignVofficeValidate(any());
        Mockito.doNothing().when(baseValidateService).doDebitValidate(any(), any());
        Mockito.doNothing().when(baseValidateService).doQuantityAvailableValidate(any(), any());

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setCheckSerial(2L);
        Mockito.doReturn(null).when(productOfferingService).findOne(any());

        baseStockAgentExpService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    @Test
    public void testDoValidate_3() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockAgent("1");
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        StockTransDetailDTO stockTransDetailDTO1 = new StockTransDetailDTO();
        stockTransDetailDTO.setStateId(1L);
        stockTransDetailDTO1.setStateId(1L);
        lstStockTransDetail.add(stockTransDetailDTO);
        lstStockTransDetail.add(stockTransDetailDTO1);

        Mockito.doNothing().when(baseValidateService).doOrderValidateAgent(any(), any());
        Mockito.doNothing().when(baseValidateService).doSignVofficeValidate(any());
        Mockito.doNothing().when(baseValidateService).doDebitValidate(any(), any());
        Mockito.doNothing().when(baseValidateService).doQuantityAvailableValidate(any(), any());

        ShopDTO shop = new ShopDTO();
        Mockito.doReturn(shop).when(shopService).findOne(any());
        Mockito.doReturn(true).when(shopService).checkChannelIsAgent(any());

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setCheckSerial(2L);
        Mockito.doReturn(productOfferingDTO).when(productOfferingService).findOne(any());

        baseStockAgentExpService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    @Test
    public void testDoValidate_4() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockAgent("1");
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        StockTransDetailDTO stockTransDetailDTO1 = new StockTransDetailDTO();
        stockTransDetailDTO.setStateId(1L);
        stockTransDetailDTO1.setStateId(1L);
        lstStockTransDetail.add(stockTransDetailDTO);
        lstStockTransDetail.add(stockTransDetailDTO);

        Mockito.doNothing().when(baseValidateService).doOrderValidateAgent(any(), any());
        Mockito.doNothing().when(baseValidateService).doSignVofficeValidate(any());
        Mockito.doNothing().when(baseValidateService).doDebitValidate(any(), any());
        Mockito.doNothing().when(baseValidateService).doQuantityAvailableValidate(any(), any());

        ShopDTO shop = new ShopDTO();
        Mockito.doReturn(shop).when(shopService).findOne(any());
        Mockito.doReturn(true).when(shopService).checkChannelIsAgent(any());

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setCheckSerial(1L);
        productOfferingDTO.setCode("1");
        Mockito.doReturn(productOfferingDTO).when(productOfferingService).findOne(any());

        baseStockAgentExpService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    @Test
    public void testDoValidate_5() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockAgent("2");
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setStateId(1L);
        lstStockTransDetail.add(stockTransDetailDTO);

        Mockito.doNothing().when(baseValidateService).doOrderValidateAgent(any(), any());
        Mockito.doNothing().when(baseValidateService).doSignVofficeValidate(any());
        Mockito.doNothing().when(baseValidateService).doDebitValidate(any(), any());
        Mockito.doNothing().when(baseValidateService).doQuantityAvailableValidate(any(), any());

        ShopDTO shop = new ShopDTO();
        Mockito.doReturn(shop).when(shopService).findOne(any());
        Mockito.doReturn(true).when(shopService).checkChannelIsAgent(any());

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setCheckSerial(1L);
        productOfferingDTO.setCode("1");
        Mockito.doReturn(productOfferingDTO).when(productOfferingService).findOne(any());

        baseStockAgentExpService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    @Test
    public void testDoValidate_6() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockAgent("2");
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();

        Mockito.doNothing().when(baseValidateService).doOrderValidateAgent(any(), any());
        Mockito.doNothing().when(baseValidateService).doSignVofficeValidate(any());
        Mockito.doNothing().when(baseValidateService).doDebitValidate(any(), any());
        Mockito.doNothing().when(baseValidateService).doQuantityAvailableValidate(any(), any());

        ShopDTO shop = new ShopDTO();
        Mockito.doReturn(shop).when(shopService).findOne(any());
        Mockito.doReturn(true).when(shopService).checkChannelIsAgent(any());

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setCheckSerial(1L);
        productOfferingDTO.setCode("1");
        Mockito.doReturn(productOfferingDTO).when(productOfferingService).findOne(any());

        baseStockAgentExpService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockAgentExpService.doIncreaseStockNum method ---- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoIncreaseStockNum_1() throws Exception {
        baseStockAgentExpService.doIncreaseStockNum(null, null, null);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockAgentExpService.doUnlockUser method ---------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoUnlockUser_1() throws Exception {
        baseStockAgentExpService.doUnlockUser(null);
    }


    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockAgentExpService.doSaveStockTransSerial method  **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoSaveStockTransSerial_1() throws Exception {
        baseStockAgentExpService.doSaveStockTransSerial(null, null);
    }

}