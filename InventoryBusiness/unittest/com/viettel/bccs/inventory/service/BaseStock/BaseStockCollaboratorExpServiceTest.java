package com.viettel.bccs.inventory.service.BaseStock;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.ProductOfferPriceService;
import com.viettel.bccs.inventory.service.ProductOfferingService;
import com.viettel.bccs.inventory.service.ShopService;
import com.viettel.bccs.inventory.service.StaffService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import oracle.net.aso.s;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;

/**
 * Created by TruNV on 8/26/2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({DataUtil.class, BaseStockService.class})
@PowerMockIgnore("javax.management.*")
public class BaseStockCollaboratorExpServiceTest {

    @InjectMocks
    private BaseStockCollaboratorExpService baseStockCollaboratorExpService;

    @Mock
    private BaseValidateService baseValidateService;

    @Mock
    private ShopService shopService;

    @Mock
    private StaffService staffService;

    @Mock
    private ProductOfferingService productOfferingService;

    @Mock
    private ProductOfferPriceService productOfferPriceService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockCollaboratorExpService.doPrepareData method -- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoPrepareData_1() throws Exception {
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        baseStockCollaboratorExpService.doPrepareData(stockTransDTO, stockTransActionDTO, flagStockDTO);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockCollaboratorExpService.doValidate method ----- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoValidate_1() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setActionCode("123456789012345678901234567890123456789012345678900");
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();

        Mockito.doNothing().when(baseValidateService).doOrderValidateCommon(any(), any());
        baseStockCollaboratorExpService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    @Test
    public void testDoValidate_2() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();

        StaffDTO staffDTO = new StaffDTO();
        Mockito.doNothing().when(baseValidateService).doOrderValidateCommon(any(), any());
        Mockito.doReturn(staffDTO).when(staffService).findOne(anyLong());
        Mockito.doReturn(false).when(shopService).checkChannelIsCollAndPointOfSale(anyLong());
        baseStockCollaboratorExpService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    @Test
    public void testDoValidate_3() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setActionCode("12345678901234567890123456789012345678901");
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setPointOfSale("1");
        Mockito.doNothing().when(baseValidateService).doOrderValidateCommon(any(), any());
        Mockito.doReturn(staffDTO).when(staffService).findOne(anyLong());
        Mockito.doReturn(true).when(shopService).checkChannelIsCollAndPointOfSale(anyLong());
        Mockito.doReturn(1L).when(shopService).findBranchId(anyLong());
        baseStockCollaboratorExpService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    @Test
    public void testDoValidate_4() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setActionCode("12345678901234567890123456789012345678901");

        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        StockTransDetailDTO stockTransDetailDTO1 = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId(null);
        stockTransDetailDTO1.setProdOfferId(1L);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetailDTO, stockTransDetailDTO1);

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setPointOfSale("2");
        Mockito.doNothing().when(baseValidateService).doOrderValidateCommon(any(), any());
        Mockito.doReturn(staffDTO).when(staffService).findOne(anyLong());
        Mockito.doReturn(true).when(shopService).checkChannelIsCollAndPointOfSale(anyLong());
        Mockito.doReturn(1L).when(shopService).findBranchId(anyLong());

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        Mockito.doReturn(null).when(productOfferingService).findOne(anyLong());
        baseStockCollaboratorExpService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    @Test
    public void testDoValidate_5() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setActionCode("12345678901234567890123456789012345678901");

        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        StockTransDetailDTO stockTransDetailDTO1 = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId(null);
        stockTransDetailDTO1.setProdOfferId(1L);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetailDTO, stockTransDetailDTO1);

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setPointOfSale("2");
        Mockito.doNothing().when(baseValidateService).doOrderValidateCommon(any(), any());
        Mockito.doReturn(staffDTO).when(staffService).findOne(anyLong());
        Mockito.doReturn(true).when(shopService).checkChannelIsCollAndPointOfSale(anyLong());
        Mockito.doReturn(1L).when(shopService).findBranchId(anyLong());

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus("2");
        Mockito.doReturn(productOfferingDTO).when(productOfferingService).findOne(anyLong());
        baseStockCollaboratorExpService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    @Test
    public void testDoValidate_6() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setActionCode("12345678901234567890123456789012345678901");

        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        StockTransDetailDTO stockTransDetailDTO1 = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId(1L);
        stockTransDetailDTO.setStateId(1L);
        stockTransDetailDTO1.setProdOfferId(1L);
        stockTransDetailDTO1.setStateId(1L);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetailDTO, stockTransDetailDTO1);

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setPointOfSale("2");
        Mockito.doNothing().when(baseValidateService).doOrderValidateCommon(any(), any());
        Mockito.doReturn(staffDTO).when(staffService).findOne(anyLong());
        Mockito.doReturn(true).when(shopService).checkChannelIsCollAndPointOfSale(anyLong());
        Mockito.doReturn(1L).when(shopService).findBranchId(anyLong());

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus("1");
        productOfferingDTO.setCode("1");
        Mockito.doReturn(productOfferingDTO).when(productOfferingService).findOne(anyLong());

        Mockito.doReturn(1L).when(productOfferPriceService).getPriceAmount(any(), any(), any());

        ProductOfferPriceDTO productOfferPriceDTO = new ProductOfferPriceDTO();
        productOfferPriceDTO.setCreateUser("Test");
        List<ProductOfferPriceDTO> lstPrice = Lists.newArrayList(productOfferPriceDTO);
        Mockito.doReturn(lstPrice).when(productOfferPriceService).getPriceDepositAmount(any(), any(), any(), any(), any(), any());
        baseStockCollaboratorExpService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    @Test
    public void testDoValidate_7() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_REQUIRE);

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setActionCode("12345678901234567890123456789012345678901");

        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        StockTransDetailDTO stockTransDetailDTO1 = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId(1L);
        stockTransDetailDTO.setStateId(2L);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetailDTO);

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setPointOfSale("2");
        Mockito.doNothing().when(baseValidateService).doOrderValidateCommon(any(), any());
        Mockito.doReturn(staffDTO).when(staffService).findOne(anyLong());
        Mockito.doReturn(true).when(shopService).checkChannelIsCollAndPointOfSale(anyLong());
        Mockito.doReturn(1L).when(shopService).findBranchId(anyLong());

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus("1");
        productOfferingDTO.setCode("1");
        Mockito.doReturn(productOfferingDTO).when(productOfferingService).findOne(anyLong());

        Mockito.doReturn(1L).when(productOfferPriceService).getPriceAmount(any(), any(), any());

        ProductOfferPriceDTO productOfferPriceDTO = new ProductOfferPriceDTO();
        productOfferPriceDTO.setCreateUser("Test");
        List<ProductOfferPriceDTO> lstPrice = Lists.newArrayList(productOfferPriceDTO);
        Mockito.doReturn(null).when(productOfferPriceService).getPriceDepositAmount(any(), any(), any(), any(), any(), any());
        baseStockCollaboratorExpService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    @Test
    public void testDoValidate_8() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_REQUIRE);

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setActionCode("12345678901234567890123456789012345678901");

        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        StockTransDetailDTO stockTransDetailDTO1 = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId(1L);
        stockTransDetailDTO.setStateId(2L);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetailDTO);

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setPointOfSale("2");
        Mockito.doNothing().when(baseValidateService).doOrderValidateCommon(any(), any());
        Mockito.doReturn(staffDTO).when(staffService).findOne(anyLong());
        Mockito.doReturn(true).when(shopService).checkChannelIsCollAndPointOfSale(anyLong());
        Mockito.doReturn(1L).when(shopService).findBranchId(anyLong());

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus("1");
        productOfferingDTO.setCode("1");
        Mockito.doReturn(productOfferingDTO).when(productOfferingService).findOne(anyLong());

        Mockito.doReturn(1L).when(productOfferPriceService).getPriceAmount(any(), any(), any());

        ProductOfferPriceDTO productOfferPriceDTO = null;
        List<ProductOfferPriceDTO> lstPrice = Lists.newArrayList(productOfferPriceDTO);
        Mockito.doReturn(lstPrice).when(productOfferPriceService).getPriceDepositAmount(any(), any(), any(), any(), any(), any());
        baseStockCollaboratorExpService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    @Test
    public void testDoValidate_9() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_REQUIRE);

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setActionCode("12345678901234567890123456789012345678901");

        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        StockTransDetailDTO stockTransDetailDTO1 = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId(1L);
        stockTransDetailDTO.setStateId(2L);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetailDTO);

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setPointOfSale("2");
        Mockito.doNothing().when(baseValidateService).doOrderValidateCommon(any(), any());
        Mockito.doReturn(staffDTO).when(staffService).findOne(anyLong());
        Mockito.doReturn(true).when(shopService).checkChannelIsCollAndPointOfSale(anyLong());
        Mockito.doReturn(1L).when(shopService).findBranchId(anyLong());

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus("1");
        productOfferingDTO.setCode("1");
        Mockito.doReturn(productOfferingDTO).when(productOfferingService).findOne(anyLong());

        Mockito.doReturn(1L).when(productOfferPriceService).getPriceAmount(any(), any(), any());

        ProductOfferPriceDTO productOfferPriceDTO = new ProductOfferPriceDTO();
        ProductOfferPriceDTO productOfferPriceDTO1 = new ProductOfferPriceDTO();
        productOfferPriceDTO.setCreateUser("2");
        productOfferPriceDTO1.setCreateUser("2");
        List<ProductOfferPriceDTO> lstPrice = Lists.newArrayList(productOfferPriceDTO, productOfferPriceDTO1);
        Mockito.doReturn(lstPrice).when(productOfferPriceService).getPriceDepositAmount(any(), any(), any(), any(), any(), any());
        baseStockCollaboratorExpService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    @Test
    public void testDoValidate_10() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setActionCode("12345678901234567890123456789012345678901");

        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        StockTransDetailDTO stockTransDetailDTO1 = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId(1L);
        stockTransDetailDTO.setStateId(2L);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetailDTO);

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setPointOfSale("2");
        Mockito.doNothing().when(baseValidateService).doOrderValidateCommon(any(), any());
        Mockito.doReturn(staffDTO).when(staffService).findOne(anyLong());
        Mockito.doReturn(true).when(shopService).checkChannelIsCollAndPointOfSale(anyLong());
        Mockito.doReturn(1L).when(shopService).findBranchId(anyLong());

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus("1");
        productOfferingDTO.setCode("1");
        Mockito.doReturn(productOfferingDTO).when(productOfferingService).findOne(anyLong());

        Mockito.doReturn(1L).when(productOfferPriceService).getPriceAmount(any(), any(), any());

        ProductOfferPriceDTO productOfferPriceDTO = new ProductOfferPriceDTO();
        productOfferPriceDTO.setCreateUser("2");
        List<ProductOfferPriceDTO> lstPrice = Lists.newArrayList(productOfferPriceDTO);
        Mockito.doReturn(lstPrice).when(productOfferPriceService).getPriceDepositAmount(any(), any(), any(), any(), any(), any());
        Mockito.doReturn(false).when(baseValidateService).doAccountBankplusValidate(any());
        Mockito.doNothing().when(baseValidateService).doDebitValidate(any(), any());
        Mockito.doNothing().when(baseValidateService).doInputSerialValidate(any(), any());

        baseStockCollaboratorExpService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    @Test
    public void testDoValidate_11() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setActionCode("12345678901234567890123456789012345678901");

        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        StockTransDetailDTO stockTransDetailDTO1 = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId(1L);
        stockTransDetailDTO.setStateId(2L);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetailDTO);

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setPointOfSale("2");
        Mockito.doNothing().when(baseValidateService).doOrderValidateCommon(any(), any());
        Mockito.doReturn(staffDTO).when(staffService).findOne(anyLong());
        Mockito.doReturn(true).when(shopService).checkChannelIsCollAndPointOfSale(anyLong());
        Mockito.doReturn(1L).when(shopService).findBranchId(anyLong());

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus("1");
        productOfferingDTO.setCode("1");
        Mockito.doReturn(productOfferingDTO).when(productOfferingService).findOne(anyLong());

        Mockito.doReturn(1L).when(productOfferPriceService).getPriceAmount(any(), any(), any());

        ProductOfferPriceDTO productOfferPriceDTO = new ProductOfferPriceDTO();
        productOfferPriceDTO.setCreateUser("2");
        List<ProductOfferPriceDTO> lstPrice = Lists.newArrayList(productOfferPriceDTO);
        Mockito.doReturn(lstPrice).when(productOfferPriceService).getPriceDepositAmount(any(), any(), any(), any(), any(), any());
        Mockito.doReturn(true).when(baseValidateService).doAccountBankplusValidate(any());
        Mockito.doNothing().when(baseValidateService).doDebitValidate(any(), any());
        Mockito.doNothing().when(baseValidateService).doInputSerialValidate(any(), any());

        baseStockCollaboratorExpService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockCollaboratorExpService.doUnlockUser method --- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoUnlockUser_1() throws Exception {
        baseStockCollaboratorExpService.doUnlockUser(null);
    }

    /** -------------------------------------------------------------------- **/
    /** Test for baseStockCollaboratorExpService.doSaveStockTransAction method **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoSaveStockTransAction_1() throws Exception {
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline("1");
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO = baseStockCollaboratorExpService.doSaveStockTransAction(stockTransDTO, stockTransActionDTO);
        Assert.assertNull(stockTransActionDTO.getActionCode());
    }

}