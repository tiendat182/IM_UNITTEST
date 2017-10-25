package com.viettel.bccs.inventory.service.BaseStock;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.*;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.support.membermodification.MemberMatcher;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Calendar;
import java.util.List;

import static org.mockito.Matchers.anyList;
import static org.ocpsoft.rewrite.config.Or.any;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({DbUtil.class, BaseStockService.class})
public class BaseStockNoteChangeProductServiceTest {

    @InjectMocks
    private BaseStockNoteChangeProductService baseStockNoteChangeProductService;

    @Mock
    private StaffService staffService;

    @Mock
    private BaseValidateService baseValidateService;

    @Mock
    private ChangeModelTransService changeModelTransService;

    @Mock
    private ChangeModelTransDetailService changeModelTransDetailService;

    @Mock
    private ChangeModelTransSerialService changeModelTransSerialService;

    @Mock
    private ProductOfferingService productOfferingService;

    @Mock
    private StockTransActionService stockTransActionService;

    @Mock
    private StockTransDetailService stockTransDetailService;

    @Mock
    private OptionSetValueService optionSetValueService;

    @Mock
    private BaseStockService baseStockService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockNoteChangeProductService.doPrepareData method  **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoPrepareData_1() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        baseStockNoteChangeProductService.doPrepareData(stockTransDTO, stockTransActionDTO, flagStockDTO);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockNoteChangeProductService.doValidate method --- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoValidate_1() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_REQUIRE );

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        lstStockTransDetail.add(stockTransDetailDTO);

        Mockito.when(staffService.findOne(Mockito.anyLong())).thenReturn(null);
        baseStockNoteChangeProductService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    @Test
    public void testDoValidate_2() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_REQUIRE );

        StockTransDTO stockTransDTO = new StockTransDTO();

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setActionCode(null);

        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        lstStockTransDetail.add(stockTransDetailDTO);

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(7282L);
        Mockito.when(staffService.findOne(Mockito.anyLong())).thenReturn(staffDTO);
        baseStockNoteChangeProductService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    @Test
    public void testDoValidate_3() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT );

        StockTransDTO stockTransDTO = new StockTransDTO();

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setActionCode("1234567890123456789012345678901234567890123456789012345678901234567890");

        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        lstStockTransDetail.add(stockTransDetailDTO);

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(7282L);
        Mockito.when(staffService.findOne(Mockito.anyLong())).thenReturn(staffDTO);
        baseStockNoteChangeProductService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    @Test
    public void testDoValidate_4() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT );

        StockTransDTO stockTransDTO = new StockTransDTO();

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setActionCode("123");

        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        lstStockTransDetail.add(stockTransDetailDTO);

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(7282L);
        Mockito.when(staffService.findOne(Mockito.anyLong())).thenReturn(staffDTO);
        Mockito.when(staffService.validateTransCode(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).thenReturn(false);
        baseStockNoteChangeProductService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    @Test
    public void testDoValidate_5() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT );

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setNote("1234567890123456789012345678901" +
                "234567890123456789012345678901234567890123456789" +
                "0123456789012345678901234567890123456789012345678901" +
                "234567890123456789012345678901234567890123456789" +
                "01234567890123456789012345678901234567890123456789012345" +
                "678906789067890678901234567890123456789012345678901234567890123456" +
                "7890123456789012345678901234567890123456789012345678901234567890123456789012" +
                "34567890123456789012345678901234567890123456789012345678901234567" +
                "89012345678901234567890123456789012345678901234567890678906789067890");

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setActionCode("123");

        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        lstStockTransDetail.add(stockTransDetailDTO);

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(7283L);
        Mockito.when(staffService.findOne(Mockito.anyLong())).thenReturn(staffDTO);
        Mockito.when(staffService.validateTransCode(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).thenReturn(false);
        baseStockNoteChangeProductService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    @Test
    public void testDoValidate_6() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setActionCode("123");

        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProductOfferPriceId(2L);
        lstStockTransDetail.add(stockTransDetailDTO);

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(7282L);
        Mockito.when(staffService.findOne(Mockito.anyLong())).thenReturn(staffDTO);
        Mockito.when(staffService.validateTransCode(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).thenReturn(true);
        baseStockNoteChangeProductService.doValidate(stockTransDTO, stockTransActionDTO, null);
    }

    @Test
    public void testDoValidate_7() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setActionCode("123");
        stockTransDTO.setNote("1234567890123456");

        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProductOfferPriceId(2L);
        lstStockTransDetail.add(stockTransDetailDTO);

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(7282L);
        Mockito.when(staffService.findOne(Mockito.anyLong())).thenReturn(staffDTO);
        Mockito.when(staffService.validateTransCode(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).thenReturn(true);
        Mockito.when(productOfferingService.findOne( Mockito.anyLong())).thenReturn(null);
        baseStockNoteChangeProductService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    @Test
    public void testDoValidate_8() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setActionCode("123");
        stockTransDTO.setNote("1234567890123456");

        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProductOfferPriceId(2L);
        lstStockTransDetail.add(stockTransDetailDTO);

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(7282L);
        Mockito.when(staffService.findOne(Mockito.anyLong())).thenReturn(staffDTO);
        Mockito.when(staffService.validateTransCode(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).thenReturn(true);

        ProductOfferingDTO productOfferingDTOOld = new ProductOfferingDTO();
        productOfferingDTOOld.setStatus("0");
        Mockito.when(productOfferingService.findOne( Mockito.anyLong())).thenReturn(productOfferingDTOOld);
        baseStockNoteChangeProductService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    @Test
    public void testDoValidate_9() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setActionCode("123");
        stockTransDTO.setNote("1234567890123456");

        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProductOfferPriceId(2L);
        lstStockTransDetail.add(stockTransDetailDTO);

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(7282L);
        Mockito.when(staffService.findOne(Mockito.anyLong())).thenReturn(staffDTO);
        Mockito.when(staffService.validateTransCode(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).thenReturn(true);

        ProductOfferingDTO productOfferingDTOOld = new ProductOfferingDTO();
        productOfferingDTOOld.setStatus("1");
        Mockito.when(productOfferingService.findOne( Mockito.anyLong())).thenReturn(productOfferingDTOOld);
        Mockito.doNothing().when(baseValidateService).doQuantityAvailableValidate(Mockito.any(), Mockito.any());
        baseStockNoteChangeProductService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    @Test
    public void testDoValidate_10() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setActionCode("123");
        stockTransDTO.setNote("1234567890123456");

        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        StockTransDetailDTO stockTransDetailDTO1 = new StockTransDetailDTO();
        StockTransDetailDTO stockTransDetailDTO2 = new StockTransDetailDTO();
        stockTransDetailDTO.setProductOfferPriceId(2L);
        lstStockTransDetail.add(stockTransDetailDTO);

        stockTransDetailDTO1.setProdOfferId(2L);
        stockTransDetailDTO1.setStateId(2L);
        lstStockTransDetail.add(stockTransDetailDTO1);

        stockTransDetailDTO2.setProdOfferId(2L);
        stockTransDetailDTO2.setStateId(2L);
        lstStockTransDetail.add(stockTransDetailDTO2);

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(7282L);
        Mockito.when(staffService.findOne(Mockito.anyLong())).thenReturn(staffDTO);
        Mockito.when(staffService.validateTransCode(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString())).thenReturn(true);

        ProductOfferingDTO productOfferingDTOOld = new ProductOfferingDTO();
        productOfferingDTOOld.setStatus("1");
        Mockito.when(productOfferingService.findOne( Mockito.anyLong())).thenReturn(productOfferingDTOOld);
        Mockito.doNothing().when(baseValidateService).doQuantityAvailableValidate(Mockito.any(), Mockito.any());
        baseStockNoteChangeProductService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockNoteChangeProductService.executeStockTrans method  **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testExecuteStockTrans_1() throws Exception {
        StockTransDTO stockTransDTO1 = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO1 = new StockTransActionDTO();

        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO1 = new StockTransDetailDTO();
        List<StockTransSerialDTO> stockTransSerialDTOS = Lists.newArrayList();
        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        stockTransSerialDTO.setStateId(1L);
        stockTransSerialDTOS.add(stockTransSerialDTO);
        stockTransDetailDTO1.setProductOfferPriceId(2L);
        stockTransDetailDTO1.setLstStockTransSerial(stockTransSerialDTOS);
        StockTransDetailDTO stockTransDetailDTO2 = new StockTransDetailDTO();
        stockTransDetailDTO2.setProductOfferPriceId(2L);
        stockTransDetailDTO2.setLstStockTransSerial(stockTransSerialDTOS);
        lstStockTransDetail.add(stockTransDetailDTO1);
        lstStockTransDetail.add(stockTransDetailDTO2);

        RequiredRoleMap requiredRoleMap1 = new RequiredRoleMap();

        ChangeModelTransDTO changeModelTransSave = new ChangeModelTransDTO();
        Mockito.when(changeModelTransService.findOne(Mockito.any())).thenReturn(changeModelTransSave);

        ChangeModelTransDetailDTO modelTransDetailSave = new ChangeModelTransDetailDTO();
        Mockito.when(changeModelTransDetailService.save(Mockito.any())).thenReturn(modelTransDetailSave);

        ChangeModelTransSerialDTO changeModelTransSerialDTO = new ChangeModelTransSerialDTO();
        Mockito.when(changeModelTransSerialService.save(Mockito.any())).thenReturn(changeModelTransSerialDTO);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStatus( Const.STOCK_TRANS_STATUS.IMPORTED );
        stockTransDTO.setStockTransDate( Calendar.getInstance().getTime() );

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        List<StockTransDetailDTO> stockTransDetailList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1076L );
        stockTransDetailDTO.setQuantity( 3000L );
        stockTransDetailList.add( stockTransDetailDTO );

        //StockTransActionDTO transActionDTO = new StockTransActionDTO();
        //PowerMockito.suppress( MemberMatcher.methodsDeclaredIn(BaseStockService.class));
        BaseStockNoteChangeProductService spyService = PowerMockito.spy(baseStockNoteChangeProductService);
        Mockito.doNothing().when(spyService).doPrepareData(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.doNothing().when(spyService).doValidate(Mockito.any(), Mockito.any(), Mockito.anyList());
        Mockito.doNothing().when(spyService).doSaveRegionStockTrans(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.doNothing().when(spyService).doSaveExchangeStockTrans(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
        StockTransDTO stockTransNew = new StockTransDTO();
        Mockito.doReturn(stockTransNew).when(spyService).doSaveStockTrans(Mockito.any());
        Mockito.doNothing().when(spyService).doSaveStockOffline(Mockito.any(), Mockito.any());
        Mockito.doReturn(stockTransActionDTO).when(spyService).doSaveStockTransAction(Mockito.any(), Mockito.any());
        Mockito.doNothing().when(spyService).doSaveStockTransDetail(Mockito.any(), Mockito.any());
        Mockito.doNothing().when(spyService).doSaveStockTransSerial(Mockito.any(), Mockito.any());
        Mockito.doNothing().when(spyService).doUpdateBankplusBCCS(Mockito.any(), Mockito.any());
        Mockito.doNothing().when(spyService).doSaveReceiptExpense(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
        StaffDTO staff = new StaffDTO();
        staff.setStaffId(1L);
        Mockito.when(staffService.findOne(Mockito.any())).thenReturn(staff);
        Mockito.doNothing().when(spyService).doSaveStockGoods(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.doNothing().when(spyService).doSaveStockTotal(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.doNothing().when(spyService).doSignVoffice(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.doNothing().when(spyService).doSyncLogistic(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.doNothing().when(spyService).doIncreaseStockNum(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.doNothing().when(spyService).doUnlockUser(Mockito.any());
        //TODO  nó bị trả về null
        //PowerMockito.doReturn(new StockTransActionDTO()).when(spyService, "executeStockTrans", null, null, null, null);

        ChangeModelTransDTO changeModelTransSave1 = new ChangeModelTransDTO();
        Mockito.when(changeModelTransService.save(Mockito.any())).thenReturn(changeModelTransSave1);
        spyService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetail, requiredRoleMap);
    }

}