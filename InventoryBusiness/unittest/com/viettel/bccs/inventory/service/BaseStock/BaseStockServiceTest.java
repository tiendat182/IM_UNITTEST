package com.viettel.bccs.inventory.service.BaseStock;

import com.google.common.collect.Lists;
import com.viettel.bccs.im1.dto.StockTransIm1DTO;
import com.viettel.bccs.im1.service.BaseStockIm1Service;
import com.viettel.bccs.im1.service.StockTransIm1Service;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockTotal;
import com.viettel.bccs.inventory.repo.StockTransRepo;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.partner.dto.AccountBalanceDTO;
import com.viettel.bccs.partner.dto.AccountBookBankplusDTO;
import com.viettel.bccs.partner.service.AccountBalanceService;
import com.viettel.bccs.partner.service.AccountBookBankplusService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
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
import javax.persistence.LockModeType;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

/**
 * Created by TruNV on 8/20/2017.
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({DbUtil.class, BaseStockService.class})
public class BaseStockServiceTest {

    @InjectMocks
    private BaseStockService baseStockService = new BaseStockService() {
        @Override
        public void doPrepareData(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, FlagStockDTO flagStockDTO) throws Exception {

        }

        @Override
        public void doValidate(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws Exception {

        }
    };

    @Mock
    private BaseMapper<StockTotal, StockTotalDTO> totalMapper;

    @Mock
    private EntityManager em;

    @Mock
    private StockTransService stockTransService;

    @Mock
    private StockTransIm1Service stockTransServiceIm1;

    @Mock
    private StockTransActionService stockTransActionService;

    @Mock
    private StockTransSerialService stockTransSerialService;

    @Mock
    private StockTransDetailService stockTransDetailService;

    @Mock
    private StockTotalService stockTotalService;

    @Mock
    private StockTransVofficeService stockTransVofficeService;

    @Mock
    private SignFlowDetailService signFlowDetailService;

    @Mock
    private ShopService shopService;

    @Mock
    private StockTransLogisticService stockTransLogisticService;

    @Mock
    private StaffService staffService;

    @Mock
    private ProductOfferingService productOfferingService;

    @Mock
    private ProductOfferTypeService productOfferTypeService;

    @Mock
    private StockTransRepo stockTransRepo;

    @Mock
    private StockTransOfflineService stockTransOfflineService;

    @Mock
    private StockTransSerialOfflineService stockTransSerialOfflineService;

    @Mock
    private StockTransDetailOfflineService stockTransDetailOfflineService;

    @Mock
    private ReceiptExpenseService receiptExpenseService;

    @Mock
    private DepositService depositService;

    @Mock
    private DepositDetailService depositDetailService;

    @Mock
    private StockCardService stockCardService;

    @Mock
    private StockTotalAuditService stockTotalAuditService;

    @Mock
    private AccountBookBankplusService accountBookBankplusService;

    @Mock
    private AccountBalanceService accountBalanceService;

    @Mock
    private OptionSetValueService optionSetValueService;

    @Mock
    private BaseStockIm1Service baseStockIm1Service;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void createTransToPartner() throws Exception {
    }



    @Test
    public void testCreateTransToPartner_1() throws Exception {
        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        StaffDTO staffDTO = new StaffDTO();
        String typeExport = null;
        String serialList = null;
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        baseStockService.createTransToPartner( stockTransFullDTO, staffDTO, typeExport, serialList, requiredRoleMap );
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockService.executeStockTrans method ------------- **/
    /**
     * --------------------------------------------------------------------
     **/
    @Test
    public void testExecuteStockTrans_1() throws Exception {

        expectedException.expect( LogicException.class );
        expectedException.expectMessage( "ESDSSTD0001" );
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStatus( Const.STOCK_TRANS_STATUS.IMPORTED );

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();

        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        List<StockTransDetailDTO> stockTransDetailList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1077L );
        stockTransDetailDTO.setQuantity( 4000L );
        stockTransDetailList.add( stockTransDetailDTO );

        FilterRequest filterRequest = new FilterRequest();
        Mockito.when( stockTransDetailService.findByFilter( Lists.newArrayList( Mockito.anyCollection() ) ) ).thenReturn( stockTransDetailList );

        List<OptionSetValueDTO> valueLst = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "3000" );
        valueLst.add( optionSetValueDTO );
        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( valueLst );
        baseStockService.executeStockTrans( stockTransDTO, stockTransActionDTO, stockTransDetailDTOList, requiredRoleMap );
    }

    @Test
    public void testExecuteStockTrans_2() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT );

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStatus( Const.STOCK_TRANS_STATUS.IMPORTED );

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        List<StockTransDetailDTO> stockTransDetailList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1076L );
        stockTransDetailDTO.setQuantity( 3000L );
        stockTransDetailList.add( stockTransDetailDTO );

        FilterRequest filterRequest = new FilterRequest();
        Mockito.when( stockTransDetailService.findByFilter( Lists.newArrayList( Mockito.anyCollection() ) ) ).thenReturn( stockTransDetailList );
        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( null );

        BaseStockService spyTemp = Mockito.spy( baseStockService );

        Mockito.doNothing().when( spyTemp ).doValidate( stockTransDTO, stockTransActionDTO, stockTransDetailDTOList );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        Mockito.doNothing().when( spyTemp ).doSaveRegionStockTrans( stockTransDTO, stockTransActionDTO, flagStockDTO, stockTransDetailDTOList );

        Mockito.doNothing().when( spyTemp ).doSaveExchangeStockTrans( stockTransDTO, stockTransActionDTO, flagStockDTO, stockTransDetailDTOList );

        Mockito.doReturn( stockTransDTO ).when( spyTemp ).doSaveStockTrans( stockTransDTO );

        Mockito.doNothing().when( spyTemp ).doSaveStockOffline( stockTransDTO, stockTransDetailDTOList );
        Mockito.doReturn( stockTransActionDTO ).when( spyTemp ).doSaveStockTransAction( stockTransDTO, stockTransActionDTO );

        Mockito.doNothing().when( spyTemp ).doSaveStockTransDetail( Mockito.any(), Mockito.any() );
        Mockito.doNothing().when( spyTemp ).doSaveStockTransSerial( stockTransDTO, stockTransDetailDTOList );
        Mockito.doNothing().when( spyTemp ).doUpdateBankplusBCCS( flagStockDTO, stockTransDTO );
        Mockito.doNothing().when( spyTemp ).doSaveReceiptExpense( flagStockDTO, stockTransDTO, stockTransActionDTO, stockTransDetailDTOList );

        StaffDTO staff = new StaffDTO();
        Mockito.when( staffService.findOne( stockTransActionDTO.getActionStaffId() ) ).thenReturn( staff );

        spyTemp.executeStockTrans( stockTransDTO, stockTransActionDTO, stockTransDetailDTOList, requiredRoleMap );
    }

    @Test
    public void testExecuteStockTrans_3() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT );

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStatus( Const.STOCK_TRANS_STATUS.IMPORTED );

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        List<StockTransDetailDTO> stockTransDetailList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1076L );
        stockTransDetailDTO.setQuantity( 3000L );
        stockTransDetailList.add( stockTransDetailDTO );

        FilterRequest filterRequest = new FilterRequest();
        Mockito.when( stockTransDetailService.findByFilter( Lists.newArrayList( Mockito.anyCollection() ) ) ).thenReturn( stockTransDetailList );

        List<OptionSetValueDTO> valueLst = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "4000" );
        valueLst.add( optionSetValueDTO );
        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( valueLst );

        BaseStockService spyTemp = Mockito.spy( baseStockService );

        Mockito.doNothing().when( spyTemp ).doValidate( stockTransDTO, stockTransActionDTO, stockTransDetailDTOList );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        Mockito.doNothing().when( spyTemp ).doSaveRegionStockTrans( stockTransDTO, stockTransActionDTO, flagStockDTO, stockTransDetailDTOList );

        Mockito.doNothing().when( spyTemp ).doSaveExchangeStockTrans( stockTransDTO, stockTransActionDTO, flagStockDTO, stockTransDetailDTOList );

        Mockito.doReturn( stockTransDTO ).when( spyTemp ).doSaveStockTrans( stockTransDTO );

        Mockito.doNothing().when( spyTemp ).doSaveStockOffline( stockTransDTO, stockTransDetailDTOList );
        Mockito.doReturn( stockTransActionDTO ).when( spyTemp ).doSaveStockTransAction( stockTransDTO, stockTransActionDTO );

        Mockito.doNothing().when( spyTemp ).doSaveStockTransDetail( Mockito.any(), Mockito.any() );
        Mockito.doNothing().when( spyTemp ).doSaveStockTransSerial( stockTransDTO, stockTransDetailDTOList );
        Mockito.doNothing().when( spyTemp ).doUpdateBankplusBCCS( flagStockDTO, stockTransDTO );
        Mockito.doNothing().when( spyTemp ).doSaveReceiptExpense( flagStockDTO, stockTransDTO, stockTransActionDTO, stockTransDetailDTOList );

        StaffDTO staff = new StaffDTO();
        staff.setStaffId( null );
        Mockito.when( staffService.findOne( stockTransActionDTO.getActionStaffId() ) ).thenReturn( staff );

        spyTemp.executeStockTrans( stockTransDTO, stockTransActionDTO, stockTransDetailDTOList, requiredRoleMap );
    }

    @Test
    public void testExecuteStockTrans_4() throws Exception {
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

        FilterRequest filterRequest = new FilterRequest();
        Mockito.when( stockTransDetailService.findByFilter( Lists.newArrayList( Mockito.anyCollection() ) ) ).thenReturn( stockTransDetailList );

        List<OptionSetValueDTO> valueLst = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "4000" );
        valueLst.add( optionSetValueDTO );
        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( valueLst );

        BaseStockService spyTemp = Mockito.spy( baseStockService );

        Mockito.doNothing().when( spyTemp ).doValidate( stockTransDTO, stockTransActionDTO, stockTransDetailDTOList );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        Mockito.doNothing().when( spyTemp ).doSaveRegionStockTrans( stockTransDTO, stockTransActionDTO, flagStockDTO, stockTransDetailDTOList );

        Mockito.doNothing().when( spyTemp ).doSaveExchangeStockTrans( stockTransDTO, stockTransActionDTO, flagStockDTO, stockTransDetailDTOList );

        Mockito.doReturn( stockTransDTO ).when( spyTemp ).doSaveStockTrans( stockTransDTO );

        Mockito.doNothing().when( spyTemp ).doSaveStockOffline( stockTransDTO, stockTransDetailDTOList );
        Mockito.doReturn( stockTransActionDTO ).when( spyTemp ).doSaveStockTransAction( stockTransDTO, stockTransActionDTO );

        Mockito.doNothing().when( spyTemp ).doSaveStockTransDetail( Mockito.any(), Mockito.any() );
        Mockito.doNothing().when( spyTemp ).doSaveStockTransSerial( stockTransDTO, stockTransDetailDTOList );
        Mockito.doNothing().when( spyTemp ).doUpdateBankplusBCCS( flagStockDTO, stockTransDTO );
        Mockito.doNothing().when( spyTemp ).doSaveReceiptExpense( flagStockDTO, stockTransDTO, stockTransActionDTO, stockTransDetailDTOList );

        StaffDTO staff = new StaffDTO();
        staff.setStaffId( Mockito.anyLong() );
        staff.setShopId( 1L );
        Mockito.when( staffService.findOne( stockTransActionDTO.getActionStaffId() ) ).thenReturn( staff );

        Mockito.doNothing().when( spyTemp ).doSaveStockGoods( Mockito.any(), Mockito.any(), Mockito.any() );
        Mockito.doNothing().when( spyTemp ).doSaveStockTotal( Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any() );
        Mockito.doNothing().when( spyTemp ).doSignVoffice( Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any() );
        Mockito.doNothing().when( spyTemp ).doSyncLogistic( Mockito.any(), Mockito.any(), Mockito.any() );
        Mockito.doNothing().when( spyTemp ).doIncreaseStockNum( Mockito.any(), Mockito.anyString(), Mockito.any() );
        Mockito.doNothing().when( spyTemp ).doUnlockUser( Mockito.any() );

        stockTransActionDTO = spyTemp.executeStockTrans( stockTransDTO, stockTransActionDTO, stockTransDetailDTOList, requiredRoleMap );
        Assert.assertNotNull( stockTransActionDTO );

    }

    @Test
    public void testExecuteStockTrans_5() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT );

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStatus( "5" );
        stockTransDTO.setStockTransDate( Calendar.getInstance().getTime() );

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        List<StockTransDetailDTO> stockTransDetailList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1076L );
        stockTransDetailDTO.setQuantity( 3000L );
        stockTransDetailList.add( stockTransDetailDTO );

        FilterRequest filterRequest = new FilterRequest();
        Mockito.when( stockTransDetailService.findByFilter( Lists.newArrayList( Mockito.anyCollection() ) ) ).thenReturn( stockTransDetailList );

        List<OptionSetValueDTO> valueLst = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "4000" );
        valueLst.add( optionSetValueDTO );
        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( valueLst );

        BaseStockService spyTemp = Mockito.spy( baseStockService );

        Mockito.doNothing().when( spyTemp ).doValidate( Mockito.any(), Mockito.any(), Mockito.any() );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        Mockito.doNothing().when( spyTemp ).doSaveRegionStockTrans( Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any() );

        Mockito.doNothing().when( spyTemp ).doSaveExchangeStockTrans( Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any() );

        Mockito.doReturn( stockTransDTO ).when( spyTemp ).doSaveStockTrans( Mockito.any() );

        Mockito.doNothing().when( spyTemp ).doSaveStockOffline( Mockito.any(), Mockito.any() );
        Mockito.doReturn( stockTransActionDTO ).when( spyTemp ).doSaveStockTransAction( Mockito.any(), Mockito.any() );

        Mockito.doNothing().when( spyTemp ).doSaveStockTransDetail( Mockito.any(), Mockito.any() );
        Mockito.doNothing().when( spyTemp ).doSaveStockTransSerial( Mockito.any(), Mockito.any() );
        Mockito.doNothing().when( spyTemp ).doUpdateBankplusBCCS( Mockito.any(), Mockito.any() );
        Mockito.doNothing().when( spyTemp ).doSaveReceiptExpense( Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any() );

        StaffDTO staff = new StaffDTO();
        staff.setStaffId( Mockito.anyLong() );
        staff.setShopId( 1L );
        Mockito.when( staffService.findOne( stockTransActionDTO.getActionStaffId() ) ).thenReturn( null );

        Mockito.doNothing().when( spyTemp ).doSaveStockGoods( Mockito.any(), Mockito.any(), Mockito.any() );
        Mockito.doNothing().when( spyTemp ).doSaveStockTotal( Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any() );
        Mockito.doNothing().when( spyTemp ).doSignVoffice( Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any() );
        Mockito.doNothing().when( spyTemp ).doSyncLogistic( Mockito.any(), Mockito.any(), Mockito.any() );
        Mockito.doNothing().when( spyTemp ).doIncreaseStockNum( Mockito.any(), Mockito.anyString(), Mockito.any() );
        Mockito.doNothing().when( spyTemp ).doUnlockUser( Mockito.any() );

        stockTransActionDTO = spyTemp.executeStockTrans( stockTransDTO, stockTransActionDTO, null, requiredRoleMap );
        Assert.assertNotNull( stockTransActionDTO );

    }

    @Test
    public void testExecuteStockTrans_6() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT );

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStatus( Const.STOCK_TRANS_STATUS.IMPORTED );

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        List<StockTransDetailDTO> stockTransDetailList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1077L );
        stockTransDetailDTO.setQuantity( 3000L );
        stockTransDetailList.add( stockTransDetailDTO );

        FilterRequest filterRequest = new FilterRequest();
        Mockito.when( stockTransDetailService.findByFilter( Lists.newArrayList( Mockito.anyCollection() ) ) ).thenReturn( stockTransDetailList );

        List<OptionSetValueDTO> valueLst = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "4000" );
        valueLst.add( optionSetValueDTO );
        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( valueLst );

        BaseStockService spyTemp = Mockito.spy( baseStockService );

        Mockito.doNothing().when( spyTemp ).doValidate( stockTransDTO, stockTransActionDTO, stockTransDetailDTOList );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        Mockito.doNothing().when( spyTemp ).doSaveRegionStockTrans( stockTransDTO, stockTransActionDTO, flagStockDTO, stockTransDetailDTOList );

        Mockito.doNothing().when( spyTemp ).doSaveExchangeStockTrans( stockTransDTO, stockTransActionDTO, flagStockDTO, stockTransDetailDTOList );

        Mockito.doReturn( stockTransDTO ).when( spyTemp ).doSaveStockTrans( stockTransDTO );

        Mockito.doNothing().when( spyTemp ).doSaveStockOffline( stockTransDTO, stockTransDetailDTOList );
        Mockito.doReturn( stockTransActionDTO ).when( spyTemp ).doSaveStockTransAction( stockTransDTO, stockTransActionDTO );

        Mockito.doNothing().when( spyTemp ).doSaveStockTransDetail( Mockito.any(), Mockito.any() );
        Mockito.doNothing().when( spyTemp ).doSaveStockTransSerial( stockTransDTO, stockTransDetailDTOList );
        Mockito.doNothing().when( spyTemp ).doUpdateBankplusBCCS( flagStockDTO, stockTransDTO );
        Mockito.doNothing().when( spyTemp ).doSaveReceiptExpense( flagStockDTO, stockTransDTO, stockTransActionDTO, stockTransDetailDTOList );

        Mockito.when( staffService.findOne( stockTransActionDTO.getActionStaffId() ) ).thenReturn( null );

        spyTemp.executeStockTrans( stockTransDTO, stockTransActionDTO, stockTransDetailDTOList, requiredRoleMap );
    }

    @Test
    public void testExecuteStockTrans_7() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT );

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStatus( Const.STOCK_TRANS_STATUS.IMPORTED );

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();

        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        List<StockTransDetailDTO> stockTransDetailList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1076L );
        stockTransDetailDTO.setQuantity( 4000L );
        stockTransDetailList.add( stockTransDetailDTO );

        FilterRequest filterRequest = new FilterRequest();
        Mockito.when( stockTransDetailService.findByFilter( Lists.newArrayList( Mockito.anyCollection() ) ) ).thenReturn( stockTransDetailList );
        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( null );

        BaseStockService spyTemp = Mockito.spy( baseStockService );

        Mockito.doNothing().when( spyTemp ).doValidate( stockTransDTO, stockTransActionDTO, stockTransDetailDTOList );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        Mockito.doNothing().when( spyTemp ).doSaveRegionStockTrans( stockTransDTO, stockTransActionDTO, flagStockDTO, stockTransDetailDTOList );

        Mockito.doNothing().when( spyTemp ).doSaveExchangeStockTrans( stockTransDTO, stockTransActionDTO, flagStockDTO, stockTransDetailDTOList );

        Mockito.doReturn( stockTransDTO ).when( spyTemp ).doSaveStockTrans( stockTransDTO );

        Mockito.doNothing().when( spyTemp ).doSaveStockOffline( stockTransDTO, stockTransDetailDTOList );
        Mockito.doReturn( stockTransActionDTO ).when( spyTemp ).doSaveStockTransAction( stockTransDTO, stockTransActionDTO );

        Mockito.doNothing().when( spyTemp ).doSaveStockTransDetail( Mockito.any(), Mockito.any() );
        Mockito.doNothing().when( spyTemp ).doSaveStockTransSerial( stockTransDTO, stockTransDetailDTOList );
        Mockito.doNothing().when( spyTemp ).doUpdateBankplusBCCS( flagStockDTO, stockTransDTO );
        Mockito.doNothing().when( spyTemp ).doSaveReceiptExpense( flagStockDTO, stockTransDTO, stockTransActionDTO, stockTransDetailDTOList );

        StaffDTO staff = new StaffDTO();
        Mockito.when( staffService.findOne( stockTransActionDTO.getActionStaffId() ) ).thenReturn( staff );

        spyTemp.executeStockTrans( stockTransDTO, stockTransActionDTO, stockTransDetailDTOList, requiredRoleMap );
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockService.doSaveRegionStockTrans method --- **/
    /**
     * --------------------------------------------------------------------
     **/
    @Test
    public void testDoSaveRegionStockTrans_1() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setRegionStockId( null );

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTOList.add( stockTransDetailDTO );

        baseStockService.doSaveRegionStockTrans( stockTransDTO, stockTransActionDTO, flagStockDTO, stockTransDetailDTOList );
    }

    @Test
    public void testDoSaveRegionStockTrans_2() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setRegionStockId( 0L );

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTOList.add( stockTransDetailDTO );

        baseStockService.doSaveRegionStockTrans( stockTransDTO, stockTransActionDTO, flagStockDTO, stockTransDetailDTOList );
    }

    @Test
    public void testDoSaveRegionStockTrans_3() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setRegionStockId( 1L );
        stockTransDTO.setRegionStockTransId( null );
        stockTransDTO.setStatus( Const.STOCK_TRANS_STATUS.IMPORT_ORDER );

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTOList.add( stockTransDetailDTO );

        StaffDTO staff = new StaffDTO();
        staff.setShopId( 7282L );

        Mockito.when( staffService.findOne( Mockito.anyLong() ) ).thenReturn( staff );

        BaseStockService spyTemp = Mockito.spy( baseStockService );
        Mockito.doReturn( stockTransDTO ).when( spyTemp ).doSaveStockTrans( Mockito.any() );
        Mockito.doNothing().when( spyTemp ).doSaveStockTransDetail( Mockito.any(), Mockito.any() );

        spyTemp.doSaveRegionStockTrans( stockTransDTO, stockTransActionDTO, flagStockDTO, stockTransDetailDTOList );
    }

    @Test
    public void testDoSaveRegionStockTrans_4() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setRegionStockId( 1L );
        stockTransDTO.setRegionStockTransId( null );
        stockTransDTO.setStatus( Const.STOCK_TRANS_STATUS.IMPORT_ORDER_PARTNER );

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTOList.add( stockTransDetailDTO );

        StaffDTO staff = new StaffDTO();
        staff.setShopId( 7282L );

        Mockito.when( staffService.findOne( Mockito.anyLong() ) ).thenReturn( staff );

        BaseStockService spyTemp = Mockito.spy( baseStockService );
        Mockito.doReturn( stockTransDTO ).when( spyTemp ).doSaveStockTrans( Mockito.any() );
        Mockito.doNothing().when( spyTemp ).doSaveStockTransDetail( Mockito.any(), Mockito.any() );

        spyTemp.doSaveRegionStockTrans( stockTransDTO, stockTransActionDTO, flagStockDTO, stockTransDetailDTOList );
    }

    @Test
    public void testDoSaveRegionStockTrans_5() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setRegionStockId( 1L );
        stockTransDTO.setRegionStockTransId( 1L );
        stockTransDTO.setStatus( Const.STOCK_TRANS_STATUS.IMPORT_ORDER_PARTNER );

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTOList.add( stockTransDetailDTO );

        StaffDTO staff = new StaffDTO();
        staff.setShopId( 7282L );

        Mockito.when( staffService.findOne( Mockito.anyLong() ) ).thenReturn( staff );

        BaseStockService spyTemp = Mockito.spy( baseStockService );
        Mockito.doReturn( stockTransDTO ).when( spyTemp ).doSaveStockTrans( Mockito.any() );
        Mockito.doNothing().when( spyTemp ).doSaveStockTransDetail( Mockito.any(), Mockito.any() );

        spyTemp.doSaveRegionStockTrans( stockTransDTO, stockTransActionDTO, flagStockDTO, stockTransDetailDTOList );
    }

    @Test
    public void testDoSaveRegionStockTrans_6() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setRegionStockId( 1L );
        stockTransDTO.setRegionStockTransId( null );
        stockTransDTO.setStatus( Const.STOCK_TRANS_STATUS.IMPORT_ORDER );

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTOList.add( stockTransDetailDTO );

        StaffDTO staff = new StaffDTO();
        staff.setShopId( 7283L );

        Mockito.when( staffService.findOne( Mockito.anyLong() ) ).thenReturn( staff );

        BaseStockService spyTemp = Mockito.spy( baseStockService );
        Mockito.doReturn( stockTransDTO ).when( spyTemp ).doSaveStockTrans( Mockito.any() );
        Mockito.doNothing().when( spyTemp ).doSaveStockTransDetail( Mockito.any(), Mockito.any() );

        spyTemp.doSaveRegionStockTrans( stockTransDTO, stockTransActionDTO, flagStockDTO, stockTransDetailDTOList );
    }

    @Test
    public void testDoSaveRegionStockTrans_7() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setRegionStockId( 1L );
        stockTransDTO.setRegionStockTransId( null );
        stockTransDTO.setStatus( Const.STOCK_TRANS_STATUS.IMPORT_ORDER );

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTOList.add( stockTransDetailDTO );

        StaffDTO staff = new StaffDTO();
        staff.setShopId( 7280L );

        Mockito.when( staffService.findOne( Mockito.anyLong() ) ).thenReturn( staff );

        BaseStockService spyTemp = Mockito.spy( baseStockService );
        Mockito.doReturn( stockTransDTO ).when( spyTemp ).doSaveStockTrans( Mockito.any() );
        Mockito.doNothing().when( spyTemp ).doSaveStockTransDetail( Mockito.any(), Mockito.any() );

        spyTemp.doSaveRegionStockTrans( stockTransDTO, stockTransActionDTO, flagStockDTO, stockTransDetailDTOList );
    }

    @Test
    public void testDoSaveRegionStockTrans_8() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setRegionStockId( 1L );
        stockTransDTO.setRegionStockTransId( null );
        stockTransDTO.setStatus( "1" );

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTOList.add( stockTransDetailDTO );

        StaffDTO staff = new StaffDTO();
        staff.setShopId( 1L );

        Mockito.when( staffService.findOne( Mockito.anyLong() ) ).thenReturn( staff );

        BaseStockService spyTemp = Mockito.spy( baseStockService );
        Mockito.doReturn( stockTransDTO ).when( spyTemp ).doSaveStockTrans( Mockito.any() );
        Mockito.doNothing().when( spyTemp ).doSaveStockTransDetail( Mockito.any(), Mockito.any() );

        spyTemp.doSaveRegionStockTrans( stockTransDTO, stockTransActionDTO, flagStockDTO, stockTransDetailDTOList );
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockService.doSaveStockTrans method --- **/
    /**
     * --------------------------------------------------------------------
     **/
    @Test
    public void testDoSaveStockTrans_1() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT );

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransId( 1L );

        StockTransDTO stockTransToUpdate = new StockTransDTO();
        Mockito.when( stockTransService.findOneLock( Mockito.anyLong() ) ).thenReturn( stockTransToUpdate );

        baseStockService.doSaveStockTrans( stockTransDTO );
    }

    @Test
    public void testDoSaveStockTrans_2() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT );

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransId( 1L );
        stockTransDTO.setStatus( "same" );

        StockTransDTO stockTransToUpdate = new StockTransDTO();
        stockTransToUpdate.setStatus( "same" );
        Mockito.when( stockTransService.findOneLock( Mockito.anyLong() ) ).thenReturn( stockTransToUpdate );

        baseStockService.doSaveStockTrans( stockTransDTO );
    }

    @Test
    public void testDoSaveStockTrans_3() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT );

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransId( 1L );
        stockTransDTO.setStatus( Const.STOCK_TRANS_STATUS.EXPORTED );

        StockTransDTO stockTransToUpdate = new StockTransDTO();
        stockTransToUpdate.setStatus( "notSame" );
        Mockito.when( stockTransService.findOneLock( Mockito.anyLong() ) ).thenReturn( stockTransToUpdate );

        List<OptionSetValueDTO> optionSetValueDTOList = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        optionSetValueDTOList.add( optionSetValueDTO );
        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( optionSetValueDTOList );

        StockTransIm1DTO stockTransToUpdateIM1 = new StockTransIm1DTO();
        stockTransToUpdateIM1.setStockTransStatus( 4L );
        Mockito.when( stockTransServiceIm1.findOneLock( stockTransDTO.getStockTransId() ) ).thenReturn( stockTransToUpdateIM1 );

        baseStockService.doSaveStockTrans( stockTransDTO );
    }

    @Test
    public void testDoSaveStockTrans_4() throws Exception {
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransId( 1L );
        stockTransDTO.setStatus( Const.STOCK_TRANS_STATUS.EXPORTED );
        stockTransDTO.setProcessOffline( Const.PROCESS_OFFLINE );
        stockTransDTO.setDepositStatus( "not null" );
        stockTransDTO.setPayStatus( "not null" );
        stockTransDTO.setBankplusStatus( 1L );
        stockTransDTO.setCreateUserIpAdress( "not null" );
        stockTransDTO.setDepositPrice( 1L );
        stockTransDTO.setImportReasonId( 1L );

        StockTransDTO stockTransToUpdate = new StockTransDTO();
        stockTransToUpdate.setStatus( "notSame" );
        Mockito.when( stockTransService.findOneLock( Mockito.anyLong() ) ).thenReturn( stockTransToUpdate );

        List<OptionSetValueDTO> optionSetValueDTOList = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        optionSetValueDTOList.add( optionSetValueDTO );
        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( optionSetValueDTOList );

        StockTransIm1DTO stockTransToUpdateIM1 = new StockTransIm1DTO();
        stockTransToUpdateIM1.setStockTransStatus( 2L );
        Mockito.when( stockTransServiceIm1.findOneLock( stockTransDTO.getStockTransId() ) ).thenReturn( stockTransToUpdateIM1 );
        Mockito.when( stockTransServiceIm1.updateStatusStockTrans( Mockito.any() ) ).thenReturn( 1 );
        Mockito.when( stockTransService.save( stockTransDTO ) ).thenReturn( stockTransDTO );

        StockTransDTO result = baseStockService.doSaveStockTrans( stockTransDTO );
        Assert.assertNotNull( result );
    }

    @Test
    public void testDoSaveStockTrans_5() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT );

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransId( 1L );
        stockTransDTO.setStatus( Const.STOCK_TRANS_STATUS.IMPORTED );
        stockTransDTO.setProcessOffline( Const.PROCESS_OFFLINE );

        StockTransDTO stockTransToUpdate = new StockTransDTO();
        stockTransToUpdate.setStatus( "notSame" );
        Mockito.when( stockTransService.findOneLock( Mockito.anyLong() ) ).thenReturn( stockTransToUpdate );

        List<OptionSetValueDTO> optionSetValueDTOList = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        optionSetValueDTOList.add( optionSetValueDTO );
        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( optionSetValueDTOList );

        StockTransIm1DTO stockTransToUpdateIM1 = new StockTransIm1DTO();
        stockTransToUpdateIM1.setStockTransStatus( 5L );
        Mockito.when( stockTransServiceIm1.findOne( stockTransDTO.getStockTransId() ) ).thenReturn( stockTransToUpdateIM1 );

        baseStockService.doSaveStockTrans( stockTransDTO );
    }

    @Test
    public void testDoSaveStockTrans_6() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT );

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransId( 1L );
        stockTransDTO.setStatus( Const.STOCK_TRANS_STATUS.IMPORTED );
        stockTransDTO.setProcessOffline( Const.PROCESS_OFFLINE );

        StockTransDTO stockTransToUpdate = new StockTransDTO();
        stockTransToUpdate.setStatus( "notSame" );
        Mockito.when( stockTransService.findOneLock( Mockito.anyLong() ) ).thenReturn( stockTransToUpdate );

        List<OptionSetValueDTO> optionSetValueDTOList = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        optionSetValueDTOList.add( optionSetValueDTO );
        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( optionSetValueDTOList );

        StockTransIm1DTO stockTransToUpdateIM1 = new StockTransIm1DTO();
        stockTransToUpdateIM1.setStockTransStatus( 6L );
        Mockito.when( stockTransServiceIm1.findOne( stockTransDTO.getStockTransId() ) ).thenReturn( stockTransToUpdateIM1 );

        baseStockService.doSaveStockTrans( stockTransDTO );
    }

    @Test
    public void testDoSaveStockTrans_7() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT );

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransId( 1L );
        stockTransDTO.setStatus( Const.STOCK_TRANS_STATUS.IMPORTED );
        stockTransDTO.setProcessOffline( Const.PROCESS_OFFLINE );

        StockTransDTO stockTransToUpdate = new StockTransDTO();
        stockTransToUpdate.setStatus( "notSame" );
        Mockito.when( stockTransService.findOneLock( Mockito.anyLong() ) ).thenReturn( stockTransToUpdate );

        List<OptionSetValueDTO> optionSetValueDTOList = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        optionSetValueDTOList.add( optionSetValueDTO );
        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( optionSetValueDTOList );

        StockTransIm1DTO stockTransToUpdateIM1 = new StockTransIm1DTO();
        stockTransToUpdateIM1.setStockTransStatus( 0L );
        Mockito.when( stockTransServiceIm1.findOne( stockTransDTO.getStockTransId() ) ).thenReturn( stockTransToUpdateIM1 );

        baseStockService.doSaveStockTrans( stockTransDTO );
    }

    @Test
    public void testDoSaveStockTrans_8() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT );

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransId( 1L );
        stockTransDTO.setStatus( Const.STOCK_TRANS_STATUS.IMPORTED );
        stockTransDTO.setProcessOffline( Const.PROCESS_OFFLINE );

        StockTransDTO stockTransToUpdate = new StockTransDTO();
        stockTransToUpdate.setStatus( "notSame" );
        Mockito.when( stockTransService.findOneLock( Mockito.anyLong() ) ).thenReturn( stockTransToUpdate );

        List<OptionSetValueDTO> optionSetValueDTOList = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        optionSetValueDTOList.add( optionSetValueDTO );
        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( optionSetValueDTOList );

        StockTransIm1DTO stockTransToUpdateIM1 = new StockTransIm1DTO();
        stockTransToUpdateIM1.setStockTransStatus( 8L );
        Mockito.when( stockTransServiceIm1.findOne( stockTransDTO.getStockTransId() ) ).thenReturn( stockTransToUpdateIM1 );

        StockTransIm1DTO stockTransImport = new StockTransIm1DTO();
        stockTransImport.setStockTransStatus( 3L );
        Mockito.when( stockTransServiceIm1.findFromStockTransIdLock( Mockito.anyLong() ) ).thenReturn( stockTransImport );

        baseStockService.doSaveStockTrans( stockTransDTO );
    }

    @Test
    public void testDoSaveStockTrans_9() throws Exception {
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransId( 1L );
        stockTransDTO.setStatus( Const.STOCK_TRANS_STATUS.IMPORTED );
        stockTransDTO.setProcessOffline( Const.PROCESS_OFFLINE );
        stockTransDTO.setDepositStatus( "not null" );
        stockTransDTO.setPayStatus( "not null" );
        stockTransDTO.setBankplusStatus( 1L );
        stockTransDTO.setCreateUserIpAdress( "not null" );
        stockTransDTO.setDepositPrice( 1L );
        stockTransDTO.setImportReasonId( 1L );

        StockTransDTO stockTransToUpdate = new StockTransDTO();
        stockTransToUpdate.setStatus( "notSame" );
        Mockito.when( stockTransService.findOneLock( Mockito.anyLong() ) ).thenReturn( stockTransToUpdate );

        List<OptionSetValueDTO> optionSetValueDTOList = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        optionSetValueDTOList.add( optionSetValueDTO );
        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( optionSetValueDTOList );

        StockTransIm1DTO stockTransToUpdateIM1 = new StockTransIm1DTO();
        stockTransToUpdateIM1.setStockTransStatus( 8L );
        Mockito.when( stockTransServiceIm1.findOne( stockTransDTO.getStockTransId() ) ).thenReturn( stockTransToUpdateIM1 );

        StockTransIm1DTO stockTransImport = new StockTransIm1DTO();
        stockTransImport.setStockTransStatus( 1L );
        Mockito.when( stockTransServiceIm1.findFromStockTransIdLock( Mockito.anyLong() ) ).thenReturn( stockTransImport );
        Mockito.when( stockTransServiceIm1.updateStatusStockTrans( Mockito.any() ) ).thenReturn( 1 );
        StockTransDTO result = baseStockService.doSaveStockTrans( stockTransDTO );
        Assert.assertNotNull( result );
    }

    @Test
    public void testDoSaveStockTrans_10() throws Exception {
        StockTransDTO stockTransDTO = new StockTransDTO();
        Mockito.when( stockTransService.findOneLock( Mockito.anyLong() ) ).thenReturn( stockTransDTO );
        Mockito.when( stockTransService.save( stockTransDTO ) ).thenReturn( stockTransDTO );
        StockTransDTO result = baseStockService.doSaveStockTrans( stockTransDTO );
        Assert.assertNotNull( result );
    }

    @Test
    public void testDoSaveStockTrans_11() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT );

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransId( 1L );

        Mockito.when( stockTransService.findOneLock( Mockito.anyLong() ) ).thenReturn( null );

        baseStockService.doSaveStockTrans( stockTransDTO );
    }

    @Test
    public void testDoSaveStockTrans_12() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransId( 1L );
        stockTransDTO.setStatus( Const.STOCK_TRANS_STATUS.IMPORTED );
        stockTransDTO.setProcessOffline( Const.PROCESS_OFFLINE );

        StockTransDTO stockTransToUpdate = new StockTransDTO();
        stockTransToUpdate.setStatus( "notSame" );
        Mockito.when( stockTransService.findOneLock( Mockito.anyLong() ) ).thenReturn( stockTransToUpdate );

        List<OptionSetValueDTO> optionSetValueDTOList = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "2" );
        optionSetValueDTOList.add( optionSetValueDTO );
        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( optionSetValueDTOList );

        StockTransIm1DTO stockTransToUpdateIM1 = new StockTransIm1DTO();
        stockTransToUpdateIM1.setStockTransStatus( 8L );
        Mockito.when( stockTransServiceIm1.findOne( stockTransDTO.getStockTransId() ) ).thenReturn( stockTransToUpdateIM1 );

        StockTransIm1DTO stockTransImport = new StockTransIm1DTO();
        stockTransImport.setStockTransStatus( 3L );
        Mockito.when( stockTransServiceIm1.findFromStockTransIdLock( Mockito.anyLong() ) ).thenReturn( stockTransImport );

        baseStockService.doSaveStockTrans( stockTransDTO );
    }

    @Test
    public void testDoSaveStockTrans_13() throws Exception {
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransId( 1L );
        stockTransDTO.setStatus( Const.STOCK_TRANS_STATUS.EXPORTED );
        stockTransDTO.setProcessOffline( Const.PROCESS_OFFLINE );
        stockTransDTO.setDepositStatus( "not null" );
        stockTransDTO.setPayStatus( "not null" );
        stockTransDTO.setBankplusStatus( 1L );
        stockTransDTO.setCreateUserIpAdress( "not null" );
        stockTransDTO.setDepositPrice( 1L );
        stockTransDTO.setImportReasonId( 1L );

        StockTransDTO stockTransToUpdate = new StockTransDTO();
        stockTransToUpdate.setStatus( "notSame" );
        Mockito.when( stockTransService.findOneLock( Mockito.anyLong() ) ).thenReturn( stockTransToUpdate );

        List<OptionSetValueDTO> optionSetValueDTOList = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        optionSetValueDTOList.add( optionSetValueDTO );
        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( optionSetValueDTOList );

        StockTransIm1DTO stockTransToUpdateIM1 = new StockTransIm1DTO();
        stockTransToUpdateIM1.setStockTransStatus( 2L );
        Mockito.when( stockTransServiceIm1.findOneLock( stockTransDTO.getStockTransId() ) ).thenReturn( null );
        Mockito.when( stockTransServiceIm1.updateStatusStockTrans( Mockito.any() ) ).thenReturn( 1 );
        Mockito.when( stockTransService.save( stockTransDTO ) ).thenReturn( stockTransDTO );

        StockTransDTO result = baseStockService.doSaveStockTrans( stockTransDTO );
        Assert.assertNotNull( result );
    }

    @Test
    public void testDoSaveStockTrans_14() throws Exception {
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransId( 1L );
        stockTransDTO.setStatus( Const.STOCK_TRANS_STATUS.EXPORTED );
        stockTransDTO.setProcessOffline( Const.PROCESS_OFFLINE );
        stockTransDTO.setDepositStatus( "not null" );
        stockTransDTO.setPayStatus( "not null" );
        stockTransDTO.setBankplusStatus( 1L );
        stockTransDTO.setCreateUserIpAdress( "not null" );
        stockTransDTO.setDepositPrice( 1L );
        stockTransDTO.setImportReasonId( 1L );

        StockTransDTO stockTransToUpdate = new StockTransDTO();
        stockTransToUpdate.setStatus( "notSame" );
        Mockito.when( stockTransService.findOneLock( Mockito.anyLong() ) ).thenReturn( stockTransToUpdate );

        List<OptionSetValueDTO> optionSetValueDTOList = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        optionSetValueDTOList.add( optionSetValueDTO );
        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( optionSetValueDTOList );

        StockTransIm1DTO stockTransToUpdateIM1 = new StockTransIm1DTO();
        stockTransToUpdateIM1.setStockTransStatus( 1L );
        Mockito.when( stockTransServiceIm1.findOneLock( stockTransDTO.getStockTransId() ) ).thenReturn( stockTransToUpdateIM1 );
        Mockito.when( stockTransServiceIm1.updateStatusStockTrans( Mockito.any() ) ).thenReturn( 1 );
        Mockito.when( stockTransService.save( stockTransDTO ) ).thenReturn( stockTransDTO );

        StockTransDTO result = baseStockService.doSaveStockTrans( stockTransDTO );
        Assert.assertNotNull( result );
    }

    @Test
    public void testDoSaveStockTrans_15() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransId( 1L );
        stockTransDTO.setStatus( Const.STOCK_TRANS_STATUS.IMPORTED );
        stockTransDTO.setProcessOffline( Const.PROCESS_OFFLINE );

        StockTransDTO stockTransToUpdate = new StockTransDTO();
        stockTransToUpdate.setStatus( "notSame" );
        Mockito.when( stockTransService.findOneLock( Mockito.anyLong() ) ).thenReturn( stockTransToUpdate );

        List<OptionSetValueDTO> optionSetValueDTOList = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        optionSetValueDTOList.add( optionSetValueDTO );
        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( optionSetValueDTOList );

        Mockito.when( stockTransServiceIm1.findOne( stockTransDTO.getStockTransId() ) ).thenReturn( null );

        baseStockService.doSaveStockTrans( stockTransDTO );
    }

    @Test
    public void testDoSaveStockTrans_16() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransId( 1L );
        stockTransDTO.setStatus( Const.STOCK_TRANS_STATUS.IMPORTED );
        stockTransDTO.setProcessOffline( Const.PROCESS_OFFLINE );

        StockTransDTO stockTransToUpdate = new StockTransDTO();
        stockTransToUpdate.setStatus( "notSame" );
        Mockito.when( stockTransService.findOneLock( Mockito.anyLong() ) ).thenReturn( stockTransToUpdate );

        List<OptionSetValueDTO> optionSetValueDTOList = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        optionSetValueDTOList.add( optionSetValueDTO );
        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( optionSetValueDTOList );

        StockTransIm1DTO stockTransToUpdateIM1 = new StockTransIm1DTO();
        stockTransToUpdateIM1.setStockTransStatus( 8L );
        Mockito.when( stockTransServiceIm1.findOne( stockTransDTO.getStockTransId() ) ).thenReturn( stockTransToUpdateIM1 );
        Mockito.when( stockTransServiceIm1.findFromStockTransIdLock( Mockito.anyLong() ) ).thenReturn( null );

        baseStockService.doSaveStockTrans( stockTransDTO );
    }

    @Test
    public void testDoSaveStockTrans_17() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransId( 1L );
        stockTransDTO.setStatus( Const.STOCK_TRANS_STATUS.IMPORTED );
        stockTransDTO.setProcessOffline( "2" );

        StockTransDTO stockTransToUpdate = new StockTransDTO();
        stockTransToUpdate.setStatus( "notSame" );
        Mockito.when( stockTransService.findOneLock( Mockito.anyLong() ) ).thenReturn( stockTransToUpdate );

        List<OptionSetValueDTO> optionSetValueDTOList = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "2" );
        optionSetValueDTOList.add( optionSetValueDTO );
        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( optionSetValueDTOList );

        StockTransIm1DTO stockTransToUpdateIM1 = new StockTransIm1DTO();
        stockTransToUpdateIM1.setStockTransStatus( 8L );
        Mockito.when( stockTransServiceIm1.findOne( stockTransDTO.getStockTransId() ) ).thenReturn( stockTransToUpdateIM1 );

        StockTransIm1DTO stockTransImport = new StockTransIm1DTO();
        stockTransImport.setStockTransStatus( 3L );
        Mockito.when( stockTransServiceIm1.findFromStockTransIdLock( Mockito.anyLong() ) ).thenReturn( stockTransImport );

        baseStockService.doSaveStockTrans( stockTransDTO );
    }

    @Test
    public void testDoSaveStockTrans_18() throws Exception {
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransType( "2" );
        Mockito.when( stockTransService.findOneLock( Mockito.anyLong() ) ).thenReturn( stockTransDTO );
        Mockito.when( stockTransService.save( stockTransDTO ) ).thenReturn( stockTransDTO );
        StockTransDTO result = baseStockService.doSaveStockTrans( stockTransDTO );
        Assert.assertNotNull( result );
    }

    @Test
    public void testDoSaveStockTrans_19() throws Exception {
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransId( 1L );
        stockTransDTO.setStatus( Const.STOCK_TRANS_STATUS.EXPORTED );
        stockTransDTO.setProcessOffline( Const.PROCESS_OFFLINE );
        stockTransDTO.setDepositStatus( "not null" );
        stockTransDTO.setPayStatus( "not null" );
        stockTransDTO.setBankplusStatus( 1L );
        stockTransDTO.setCreateUserIpAdress( "not null" );
        stockTransDTO.setDepositPrice( 1L );
        stockTransDTO.setImportReasonId( 1L );

        StockTransDTO stockTransToUpdate = new StockTransDTO();
        stockTransToUpdate.setStatus( "notSame" );
        Mockito.when( stockTransService.findOneLock( Mockito.anyLong() ) ).thenReturn( stockTransToUpdate );

        List<OptionSetValueDTO> optionSetValueDTOList = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "2" );
        optionSetValueDTOList.add( optionSetValueDTO );
        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( optionSetValueDTOList );

        StockTransIm1DTO stockTransToUpdateIM1 = new StockTransIm1DTO();
        stockTransToUpdateIM1.setStockTransStatus( 2L );
        Mockito.when( stockTransServiceIm1.findOneLock( stockTransDTO.getStockTransId() ) ).thenReturn( null );
        Mockito.when( stockTransServiceIm1.updateStatusStockTrans( Mockito.any() ) ).thenReturn( 1 );
        Mockito.when( stockTransService.save( stockTransDTO ) ).thenReturn( stockTransDTO );

        StockTransDTO result = baseStockService.doSaveStockTrans( stockTransDTO );
        Assert.assertNotNull( result );
    }

    @Test
    public void testDoSaveStockTrans_20() throws Exception {
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransId( 1L );
        stockTransDTO.setStatus( Const.STOCK_TRANS_STATUS.EXPORTED );
        stockTransDTO.setProcessOffline( Const.PROCESS_OFFLINE );
        stockTransDTO.setDepositStatus( "not null" );
        stockTransDTO.setPayStatus( "not null" );
        stockTransDTO.setBankplusStatus( 1L );
        stockTransDTO.setCreateUserIpAdress( "not null" );
        stockTransDTO.setDepositPrice( 1L );
        stockTransDTO.setImportReasonId( 1L );

        StockTransDTO stockTransToUpdate = new StockTransDTO();
        stockTransToUpdate.setStatus( "notSame" );
        Mockito.when( stockTransService.findOneLock( Mockito.anyLong() ) ).thenReturn( stockTransToUpdate );

        List<OptionSetValueDTO> optionSetValueDTOList = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "2" );
        optionSetValueDTOList.add( optionSetValueDTO );
        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( null );

        StockTransIm1DTO stockTransToUpdateIM1 = new StockTransIm1DTO();
        stockTransToUpdateIM1.setStockTransStatus( 2L );
        Mockito.when( stockTransServiceIm1.findOneLock( stockTransDTO.getStockTransId() ) ).thenReturn( null );
        Mockito.when( stockTransServiceIm1.updateStatusStockTrans( Mockito.any() ) ).thenReturn( 1 );
        Mockito.when( stockTransService.save( stockTransDTO ) ).thenReturn( stockTransDTO );

        StockTransDTO result = baseStockService.doSaveStockTrans( stockTransDTO );
        Assert.assertNotNull( result );
    }

    @Test
    public void testDoSaveStockTrans_21() throws Exception {
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransId( 1L );
        stockTransDTO.setStatus( Const.STOCK_TRANS_STATUS.EXPORTED );
        stockTransDTO.setProcessOffline( Const.PROCESS_OFFLINE );
        stockTransDTO.setDepositStatus( "not null" );
        stockTransDTO.setPayStatus( "not null" );
        stockTransDTO.setBankplusStatus( 1L );
        stockTransDTO.setCreateUserIpAdress( "not null" );
        stockTransDTO.setDepositPrice( 1L );
        stockTransDTO.setImportReasonId( 1L );

        StockTransDTO stockTransToUpdate = new StockTransDTO();
        stockTransToUpdate.setStatus( "notSame" );
        Mockito.when( stockTransService.findOneLock( Mockito.anyLong() ) ).thenReturn( stockTransToUpdate );

        List<OptionSetValueDTO> optionSetValueDTOList = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( null );
        optionSetValueDTOList.add( optionSetValueDTO );
        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( optionSetValueDTOList );

        StockTransIm1DTO stockTransToUpdateIM1 = new StockTransIm1DTO();
        stockTransToUpdateIM1.setStockTransStatus( 2L );
        Mockito.when( stockTransServiceIm1.findOneLock( stockTransDTO.getStockTransId() ) ).thenReturn( null );
        Mockito.when( stockTransServiceIm1.updateStatusStockTrans( Mockito.any() ) ).thenReturn( 1 );
        Mockito.when( stockTransService.save( stockTransDTO ) ).thenReturn( stockTransDTO );

        StockTransDTO result = baseStockService.doSaveStockTrans( stockTransDTO );
        Assert.assertNotNull( result );
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockService.doSaveStockTransAction method --- **/
    /**
     * --------------------------------------------------------------------
     **/
    @Test
    public void testDoSaveStockTransAction_1() throws Exception {
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( Const.PROCESS_OFFLINE );

        StockTransActionDTO result = baseStockService.doSaveStockTransAction( stockTransDTO, stockTransActionDTO );
        Assert.assertNotNull( result );
    }

    @Test
    public void testDoSaveStockTransAction_2() throws Exception {
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( null );
        stockTransDTO.setSignVoffice( true );

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setIsRegionExchange( false );
        stockTransActionDTO.setStatus( "3" );
        Mockito.when( stockTransActionService.save( Mockito.any() ) ).thenReturn( stockTransActionDTO );
        StockTransActionDTO result = baseStockService.doSaveStockTransAction( stockTransDTO, stockTransActionDTO );
        Assert.assertNotNull( result );
    }

    @Test
    public void testDoSaveStockTransAction_3() throws Exception {
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( null );
        stockTransDTO.setSignVoffice( true );

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setIsRegionExchange( true );
        stockTransActionDTO.setStatus( "1" );
        Mockito.when( stockTransActionService.save( Mockito.any() ) ).thenReturn( stockTransActionDTO );
        StockTransActionDTO result = baseStockService.doSaveStockTransAction( stockTransDTO, stockTransActionDTO );
        Assert.assertNotNull( result );
    }

    @Test
    public void testDoSaveStockTransAction_4() throws Exception {
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( null );
        stockTransDTO.setSignVoffice( false );

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setIsRegionExchange( true );
        stockTransActionDTO.setStatus( "1" );
        Mockito.when( stockTransActionService.save( Mockito.any() ) ).thenReturn( stockTransActionDTO );
        StockTransActionDTO result = baseStockService.doSaveStockTransAction( stockTransDTO, stockTransActionDTO );
        Assert.assertNotNull( result );
    }

    @Test
    public void testDoSaveStockTransAction_5() throws Exception {
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( null );
        stockTransDTO.setSignVoffice( true );

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setIsRegionExchange( true );
        stockTransActionDTO.setStatus( "1" );
        Mockito.when( stockTransActionService.save( Mockito.any() ) ).thenReturn( stockTransActionDTO );
        StockTransActionDTO result = baseStockService.doSaveStockTransAction( stockTransDTO, stockTransActionDTO );
        Assert.assertNotNull( result );
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockService.doSaveStockTransDetail method --- **/
    /**
     * --------------------------------------------------------------------
     **/
    @Test
    public void testDoSaveStockTransDetail_1() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        stockTransDetailDTOList.add( stockTransDetailDTO );

        Mockito.when( stockTransDetailService.save( Mockito.any() ) ).thenReturn( stockTransDetailDTO );

        baseStockService.doSaveStockTransDetail( stockTransDTO, stockTransDetailDTOList );
    }

    @Test
    public void testDoSaveStockTransDetail_2() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();

        baseStockService.doSaveStockTransDetail( stockTransDTO, stockTransDetailDTOList );
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockService.doSaveStockTransSerial method --- **/
    /**
     * --------------------------------------------------------------------
     **/
    @Test
    public void testDoSaveStockTransSerial_1() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "1" );
        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTOList.add( stockTransDetailDTO );

        baseStockService.doSaveStockTransSerial( stockTransDTO, stockTransDetailDTOList );
    }

    @Test
    public void testDoSaveStockTransSerial_2() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTOList.add( stockTransDetailDTO );

        List<StockTransSerialDTO> lstStockTransSerial = Lists.newArrayList();
        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();

        stockTransDetailDTO.setLstStockTransSerial( lstStockTransSerial );
        lstStockTransSerial.add( stockTransSerialDTO );
        Mockito.when( stockTransDetailService.save( Mockito.any() ) ).thenReturn( stockTransDetailDTO );
        baseStockService.doSaveStockTransSerial( stockTransDTO, stockTransDetailDTOList );
    }

    @Test
    public void testDoSaveStockTransSerial_3() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();

        baseStockService.doSaveStockTransSerial( stockTransDTO, stockTransDetailDTOList );
    }

    @Test
    public void testDoSaveStockTransSerial_4() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();

        List<StockTransSerialDTO> lstStockTransSerial = Lists.newArrayList();
        stockTransDetailDTO.setLstStockTransSerial( lstStockTransSerial );
        stockTransDetailDTOList.add( stockTransDetailDTO );
        Mockito.when( stockTransDetailService.save( Mockito.any() ) ).thenReturn( stockTransDetailDTO );
        baseStockService.doSaveStockTransSerial( stockTransDTO, stockTransDetailDTOList );
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockService.doSaveStockTotal method --- **/
    /**
     * --------------------------------------------------------------------
     **/
    @Test
    public void testDoSaveStockTotal_1() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "1" );

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTOList.add( stockTransDetailDTO );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();

        baseStockService.doSaveStockTotal( stockTransDTO, stockTransDetailDTOList, flagStockDTO, stockTransActionDTO );
    }

    @Test
    public void testDoSaveStockTotal_2() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );
        stockTransDTO.setExchangeStockId( 0L );
        stockTransDTO.setRegionStockId( 1L );

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        List<StockTransDetailDTO> result = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        result.add( stockTransDetailDTO );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setImportStock( false );
        flagStockDTO.setExportStock( false );

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();

        baseStockService.doSaveStockTotal( stockTransDTO, stockTransDetailDTOList, flagStockDTO, stockTransActionDTO );
    }

    @Test
    public void testDoSaveStockTotal_3() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );
        stockTransDTO.setExchangeStockId( 0L );
        stockTransDTO.setRegionStockId( 1L );
        stockTransDTO.setPayStatus( Const.PAY_STATUS.PAY_HAVE );
        stockTransDTO.setReasonId( 1L );

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        List<StockTransDetailDTO> result = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1L );
        stockTransDetailDTO.setStateId( 1L );
        stockTransDetailDTO.setQuantity( 1L );
        stockTransDetailDTO.setProdOfferId( 1L );

        result.add( stockTransDetailDTO );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setImportStock( true );
        flagStockDTO.setExportStock( true );
        flagStockDTO.setStatusForAgent( null );
        flagStockDTO.setShopId( Const.L_VT_SHOP_ID );
        flagStockDTO.setExpAvailableQuantity( 1L );
        flagStockDTO.setExpCurrentQuantity( 1L );
        flagStockDTO.setExpHangQuantity( 1L );
        flagStockDTO.setExpAvailableQuantity( 1L );
        flagStockDTO.setExpAvailableQuantity( 1L );
        flagStockDTO.setObjectType( Const.STOCK_TRANS.OBJECT_TYPE_AGENT_EXPORT );
        flagStockDTO.setInsertStockTotalAudit( true );

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();

        FilterRequest filterRequest = new FilterRequest();
        Mockito.when( stockTransDetailService.findByFilter( Lists.newArrayList( Mockito.anyCollection() ) ) ).thenReturn( result );

        List<OptionSetValueDTO> optionSetValueDTOList = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        optionSetValueDTOList.add( optionSetValueDTO );
        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( optionSetValueDTOList );
        Mockito.doNothing().when( baseStockIm1Service ).doSaveStockTotal( Mockito.any(), Mockito.anyList(), Mockito.any(), Mockito.any() );
        baseStockService.doSaveStockTotal( stockTransDTO, stockTransDetailDTOList, flagStockDTO, stockTransActionDTO );
    }

    @Test
    public void testDoSaveStockTotal_4() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );
        stockTransDTO.setExchangeStockId( 0L );
        stockTransDTO.setRegionStockId( 1L );
        stockTransDTO.setPayStatus( Const.PAY_STATUS.PAY_HAVE );
        stockTransDTO.setReasonId( null ); // 563

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        List<StockTransDetailDTO> result = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1L );
        stockTransDetailDTO.setStateId( 1L );
        stockTransDetailDTO.setQuantity( 1L );
        stockTransDetailDTO.setProdOfferId( 1L );

        result.add( stockTransDetailDTO );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setImportStock( true );
        flagStockDTO.setExportStock( true );
        flagStockDTO.setStatusForAgent( null );
        flagStockDTO.setShopId( Const.L_VT_SHOP_ID );
        flagStockDTO.setExpAvailableQuantity( 1L );
        flagStockDTO.setExpCurrentQuantity( 1L );
        flagStockDTO.setExpHangQuantity( 1L );
        flagStockDTO.setExpAvailableQuantity( 1L );
        flagStockDTO.setExpAvailableQuantity( 1L );
        flagStockDTO.setObjectType( Const.STOCK_TRANS.OBJECT_TYPE_AGENT_EXPORT );
        flagStockDTO.setInsertStockTotalAudit( true );


        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();

        FilterRequest filterRequest = new FilterRequest();
        Mockito.when( stockTransDetailService.findByFilter( Lists.newArrayList( Mockito.anyCollection() ) ) ).thenReturn( result );

        List<OptionSetValueDTO> optionSetValueDTOList = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        optionSetValueDTOList.add( optionSetValueDTO );
        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( optionSetValueDTOList );
        Mockito.doNothing().when( baseStockIm1Service ).doSaveStockTotal( Mockito.any(), Mockito.anyList(), Mockito.any(), Mockito.any() );
        baseStockService.doSaveStockTotal( stockTransDTO, stockTransDetailDTOList, flagStockDTO, stockTransActionDTO );
    }

    @Test
    public void testDoSaveStockTotal_5() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );
        stockTransDTO.setExchangeStockId( 0L );
        stockTransDTO.setRegionStockId( 1L );
        stockTransDTO.setPayStatus( Const.PAY_STATUS.NOT_PAY );
        stockTransDTO.setReasonId( 1L );

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        List<StockTransDetailDTO> result = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1L );
        stockTransDetailDTO.setStateId( 1L );
        stockTransDetailDTO.setQuantity( 1L );
        stockTransDetailDTO.setProdOfferId( 1L );

        result.add( stockTransDetailDTO );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setImportStock( true );
        flagStockDTO.setExportStock( true );
        flagStockDTO.setStatusForAgent( null );
        flagStockDTO.setShopId( Const.L_VT_SHOP_ID );
        flagStockDTO.setExpAvailableQuantity( 1L );
        flagStockDTO.setExpCurrentQuantity( 1L );
        flagStockDTO.setExpHangQuantity( 1L );
        flagStockDTO.setExpAvailableQuantity( 1L );
        flagStockDTO.setExpAvailableQuantity( 1L );
        flagStockDTO.setObjectType( Const.STOCK_TRANS.OBJECT_TYPE_AGENT_EXPORT );
        flagStockDTO.setInsertStockTotalAudit( true );
        flagStockDTO.setStateIdForReasonId( 1L );
        flagStockDTO.setImpAvailableQuantity( 1L );
        flagStockDTO.setImpCurrentQuantity( 1L );
        flagStockDTO.setImpHangQuantity( 1L );

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();

        FilterRequest filterRequest = new FilterRequest();
        Mockito.when( stockTransDetailService.findByFilter( Lists.newArrayList( Mockito.anyCollection() ) ) ).thenReturn( result );

        List<OptionSetValueDTO> optionSetValueDTOList = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        optionSetValueDTOList.add( optionSetValueDTO );
        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( optionSetValueDTOList );
        Mockito.doNothing().when( baseStockIm1Service ).doSaveStockTotal( Mockito.any(), Mockito.anyList(), Mockito.any(), Mockito.any() );
        baseStockService.doSaveStockTotal( stockTransDTO, stockTransDetailDTOList, flagStockDTO, stockTransActionDTO );
    }

    @Test
    public void testDoSaveStockTotal_6() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );
        stockTransDTO.setExchangeStockId( 0L );
        stockTransDTO.setRegionStockId( 1L );
        stockTransDTO.setPayStatus( Const.PAY_STATUS.NOT_PAY );
        stockTransDTO.setReasonId( 1L );

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        List<StockTransDetailDTO> result = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1L );
        stockTransDetailDTO.setStateId( 1L );
        stockTransDetailDTO.setQuantity( 1L );
        stockTransDetailDTO.setProdOfferId( 1L );

        result.add( stockTransDetailDTO );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setImportStock( true );
        flagStockDTO.setExportStock( true );
        flagStockDTO.setStatusForAgent( null );
        flagStockDTO.setShopId( Const.L_VT_SHOP_ID );
        flagStockDTO.setExpAvailableQuantity( 1L );
        flagStockDTO.setExpCurrentQuantity( 1L );
        flagStockDTO.setExpHangQuantity( 1L );
        flagStockDTO.setExpAvailableQuantity( 1L );
        flagStockDTO.setExpAvailableQuantity( 1L );
        flagStockDTO.setObjectType( Const.STOCK_TRANS.OBJECT_TYPE_AGENT_EXPORT );
        flagStockDTO.setInsertStockTotalAudit( true );
        flagStockDTO.setStateIdForReasonId( null );
        flagStockDTO.setImpAvailableQuantity( 1L );
        flagStockDTO.setImpCurrentQuantity( 1L );
        flagStockDTO.setImpHangQuantity( 1L );

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();

        FilterRequest filterRequest = new FilterRequest();
        Mockito.when( stockTransDetailService.findByFilter( Lists.newArrayList( Mockito.anyCollection() ) ) ).thenReturn( result );

        List<OptionSetValueDTO> optionSetValueDTOList = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        optionSetValueDTOList.add( optionSetValueDTO );
        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( optionSetValueDTOList );
        Mockito.doNothing().when( baseStockIm1Service ).doSaveStockTotal( Mockito.any(), Mockito.anyList(), Mockito.any(), Mockito.any() );
        baseStockService.doSaveStockTotal( stockTransDTO, stockTransDetailDTOList, flagStockDTO, stockTransActionDTO );
    }

    @Test
    public void testDoSaveStockTotal_7() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );
        stockTransDTO.setExchangeStockId( 1L );
        stockTransDTO.setRegionStockId( 1L );

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        List<StockTransDetailDTO> result = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        result.add( stockTransDetailDTO );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setImportStock( true );
        flagStockDTO.setExportStock( true );

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();

        baseStockService.doSaveStockTotal( stockTransDTO, stockTransDetailDTOList, flagStockDTO, stockTransActionDTO );
    }

    @Test
    public void testDoSaveStockTotal_8() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );
        stockTransDTO.setExchangeStockId( 0L );
        stockTransDTO.setRegionStockId( 0L );
        stockTransDTO.setPayStatus( "2" );
        stockTransDTO.setReasonId( 1L );

        List<StockTransDetailDTO> result = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1L );
        stockTransDetailDTO.setStateId( 1L );
        stockTransDetailDTO.setQuantity( 1L );
        stockTransDetailDTO.setProdOfferId( 1L );
        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        stockTransDetailDTOList.add( stockTransDetailDTO );
        result.add( stockTransDetailDTO );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setImportStock( true );
        flagStockDTO.setExportStock( true );
        flagStockDTO.setStatusForAgent( null );
        flagStockDTO.setShopId( 12L );
        flagStockDTO.setExpAvailableQuantity( 1L );
        flagStockDTO.setExpCurrentQuantity( 1L );
        flagStockDTO.setExpHangQuantity( 1L );
        flagStockDTO.setExpAvailableQuantity( 1L );
        flagStockDTO.setExpAvailableQuantity( 1L );
        flagStockDTO.setObjectType( Const.STOCK_TRANS.OBJECT_TYPE_AGENT_EXPORT );
        flagStockDTO.setInsertStockTotalAudit( false );

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();

        FilterRequest filterRequest = new FilterRequest();
        Mockito.when( stockTransDetailService.findByFilter( Lists.newArrayList( Mockito.anyCollection() ) ) ).thenReturn( result );

        List<OptionSetValueDTO> optionSetValueDTOList = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "2" );
        optionSetValueDTOList.add( optionSetValueDTO );
        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( optionSetValueDTOList );
        Mockito.doNothing().when( baseStockIm1Service ).doSaveStockTotal( Mockito.any(), Mockito.anyList(), Mockito.any(), Mockito.any() );
        baseStockService.doSaveStockTotal( stockTransDTO, stockTransDetailDTOList, flagStockDTO, stockTransActionDTO );
    }

    @Test
    public void testDoSaveStockTotal_9() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );
        stockTransDTO.setExchangeStockId( 0L );
        stockTransDTO.setRegionStockId( 0L );
        stockTransDTO.setPayStatus( "2" );
        stockTransDTO.setReasonId( 1L );

        List<StockTransDetailDTO> result = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1L );
        stockTransDetailDTO.setStateId( 1L );
        stockTransDetailDTO.setQuantity( 1L );
        stockTransDetailDTO.setProdOfferId( 1L );
        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        stockTransDetailDTOList.add( stockTransDetailDTO );
        result.add( stockTransDetailDTO );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setImportStock( false );
        flagStockDTO.setExportStock( false );
        flagStockDTO.setStatusForAgent( null );
        flagStockDTO.setShopId( 12L );
        flagStockDTO.setExpAvailableQuantity( 1L );
        flagStockDTO.setExpCurrentQuantity( 1L );
        flagStockDTO.setExpHangQuantity( 1L );
        flagStockDTO.setExpAvailableQuantity( 1L );
        flagStockDTO.setExpAvailableQuantity( 1L );
        flagStockDTO.setObjectType( Const.STOCK_TRANS.OBJECT_TYPE_AGENT_EXPORT );
        flagStockDTO.setInsertStockTotalAudit( false );

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();

        FilterRequest filterRequest = new FilterRequest();
        Mockito.when( stockTransDetailService.findByFilter( Lists.newArrayList( Mockito.anyCollection() ) ) ).thenReturn( result );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( null );
        Mockito.doNothing().when( baseStockIm1Service ).doSaveStockTotal( Mockito.any(), Mockito.anyList(), Mockito.any(), Mockito.any() );
        baseStockService.doSaveStockTotal( stockTransDTO, stockTransDetailDTOList, flagStockDTO, stockTransActionDTO );
    }

    @Test
    public void testDoSaveStockTotal_12() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );
        stockTransDTO.setExchangeStockId( 0L );
        stockTransDTO.setRegionStockId( 1L );
        stockTransDTO.setPayStatus( Const.PAY_STATUS.PAY_HAVE );
        stockTransDTO.setReasonId( 1L );

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        List<StockTransDetailDTO> result = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1L );
        stockTransDetailDTO.setStateId( 1L );
        stockTransDetailDTO.setQuantity( 1L );
        stockTransDetailDTO.setProdOfferId( 1L );

        result.add( stockTransDetailDTO );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setImportStock( false );
        flagStockDTO.setExportStock( true );
        flagStockDTO.setStatusForAgent( null );
        flagStockDTO.setShopId( Const.L_VT_SHOP_ID );
        flagStockDTO.setExpAvailableQuantity( 1L );
        flagStockDTO.setExpCurrentQuantity( 1L );
        flagStockDTO.setExpHangQuantity( 1L );
        flagStockDTO.setExpAvailableQuantity( 1L );
        flagStockDTO.setExpAvailableQuantity( 1L );
        flagStockDTO.setObjectType( Const.STOCK_TRANS.OBJECT_TYPE_AGENT_EXPORT );
        flagStockDTO.setInsertStockTotalAudit( true );

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();

        FilterRequest filterRequest = new FilterRequest();
        Mockito.when( stockTransDetailService.findByFilter( Lists.newArrayList( Mockito.anyCollection() ) ) ).thenReturn( result );

        List<OptionSetValueDTO> optionSetValueDTOList = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( null );
        optionSetValueDTOList.add( optionSetValueDTO );
        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( optionSetValueDTOList );
        Mockito.doNothing().when( baseStockIm1Service ).doSaveStockTotal( Mockito.any(), Mockito.anyList(), Mockito.any(), Mockito.any() );
        baseStockService.doSaveStockTotal( stockTransDTO, stockTransDetailDTOList, flagStockDTO, stockTransActionDTO );
    }

    @Test
    public void testDoSaveStockTotal_13() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );
        stockTransDTO.setExchangeStockId( 0L );
        stockTransDTO.setRegionStockId( 1L );
        stockTransDTO.setPayStatus( Const.PAY_STATUS.PAY_HAVE );
        stockTransDTO.setReasonId( 1L );

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        List<StockTransDetailDTO> result = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1L );
        stockTransDetailDTO.setStateId( 1L );
        stockTransDetailDTO.setQuantity( 1L );
        stockTransDetailDTO.setProdOfferId( 1L );

        result.add( stockTransDetailDTO );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setImportStock( true );
        flagStockDTO.setExportStock( false );
        flagStockDTO.setStatusForAgent( "3" );
        flagStockDTO.setShopId( Const.L_VT_SHOP_ID );
        flagStockDTO.setExpAvailableQuantity( 1L );
        flagStockDTO.setExpCurrentQuantity( 1L );
        flagStockDTO.setExpHangQuantity( 1L );
        flagStockDTO.setExpAvailableQuantity( 1L );
        flagStockDTO.setExpAvailableQuantity( 1L );
        flagStockDTO.setObjectType( Const.STOCK_TRANS.OBJECT_TYPE_AGENT_EXPORT );
        flagStockDTO.setInsertStockTotalAudit( true );

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();

        FilterRequest filterRequest = new FilterRequest();
        Mockito.when( stockTransDetailService.findByFilter( Lists.newArrayList( Mockito.anyCollection() ) ) ).thenReturn( result );

        List<OptionSetValueDTO> optionSetValueDTOList = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        optionSetValueDTOList.add( optionSetValueDTO );
        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( null );
        Mockito.doNothing().when( baseStockIm1Service ).doSaveStockTotal( Mockito.any(), Mockito.anyList(), Mockito.any(), Mockito.any() );
        baseStockService.doSaveStockTotal( stockTransDTO, stockTransDetailDTOList, flagStockDTO, stockTransActionDTO );
    }

    @Test
    public void testDoSaveStockTotal_17() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );
        stockTransDTO.setExchangeStockId( 0L );
        stockTransDTO.setRegionStockId( 1L );
        stockTransDTO.setPayStatus( "null" );
        stockTransDTO.setReasonId( 1L );

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        List<StockTransDetailDTO> result = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1L );
        stockTransDetailDTO.setStateId( 1L );
        stockTransDetailDTO.setQuantity( 1L );
        stockTransDetailDTO.setProdOfferId( 1L );

        result.add( stockTransDetailDTO );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setImportStock( true );
        flagStockDTO.setExportStock( false );
        flagStockDTO.setStatusForAgent( "3" );
        flagStockDTO.setShopId( Const.L_VT_SHOP_ID );
        flagStockDTO.setExpAvailableQuantity( 1L );
        flagStockDTO.setExpCurrentQuantity( 1L );
        flagStockDTO.setExpHangQuantity( 1L );
        flagStockDTO.setExpAvailableQuantity( 1L );
        flagStockDTO.setExpAvailableQuantity( 1L );
        flagStockDTO.setObjectType( null );
        flagStockDTO.setInsertStockTotalAudit( true );

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();

        FilterRequest filterRequest = new FilterRequest();
        Mockito.when( stockTransDetailService.findByFilter( Lists.newArrayList( Mockito.anyCollection() ) ) ).thenReturn( result );

        List<OptionSetValueDTO> optionSetValueDTOList = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        optionSetValueDTOList.add( optionSetValueDTO );
        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( null );
        Mockito.doNothing().when( baseStockIm1Service ).doSaveStockTotal( Mockito.any(), Mockito.anyList(), Mockito.any(), Mockito.any() );
        baseStockService.doSaveStockTotal( stockTransDTO, stockTransDetailDTOList, flagStockDTO, stockTransActionDTO );
    }

    @Test
    public void testDoSaveStockTotal_18() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );
        stockTransDTO.setExchangeStockId( 0L );
        stockTransDTO.setRegionStockId( 1L );
        stockTransDTO.setPayStatus( "null" );
        stockTransDTO.setReasonId( 1L );

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        List<StockTransDetailDTO> result = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1L );
        stockTransDetailDTO.setStateId( 1L );
        stockTransDetailDTO.setQuantity( 1L );
        stockTransDetailDTO.setProdOfferId( 1L );

        result.add( stockTransDetailDTO );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setImportStock( true );
        flagStockDTO.setExportStock( false );
        flagStockDTO.setStatusForAgent( "3" );
        flagStockDTO.setShopId( Const.L_VT_SHOP_ID );
        flagStockDTO.setExpAvailableQuantity( 1L );
        flagStockDTO.setExpCurrentQuantity( 1L );
        flagStockDTO.setExpHangQuantity( 1L );
        flagStockDTO.setExpAvailableQuantity( 1L );
        flagStockDTO.setExpAvailableQuantity( 1L );
        flagStockDTO.setObjectType( "ABC" );
        flagStockDTO.setInsertStockTotalAudit( true );

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();

        FilterRequest filterRequest = new FilterRequest();
        Mockito.when( stockTransDetailService.findByFilter( Lists.newArrayList( Mockito.anyCollection() ) ) ).thenReturn( result );

        List<OptionSetValueDTO> optionSetValueDTOList = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        optionSetValueDTOList.add( optionSetValueDTO );
        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( null );
        Mockito.doNothing().when( baseStockIm1Service ).doSaveStockTotal( Mockito.any(), Mockito.anyList(), Mockito.any(), Mockito.any() );
        baseStockService.doSaveStockTotal( stockTransDTO, stockTransDetailDTOList, flagStockDTO, stockTransActionDTO );
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for stockTransSerialService.doSaveStockTotal(stockTotalAuditDTO, lstStockTotalDTO) method --- **/
    /**
     * --------------------------------------------------------------------
     **/
    @Test
    public void testDoSaveStockTotal_14() throws Exception {

        StockTotalAuditDTO stockTotalAuditDTO = new StockTotalAuditDTO();
        List<StockTotalDTO> stockTotalDTOList = Lists.newArrayList();
        baseStockService.doSaveStockTotal( stockTotalAuditDTO, stockTotalDTOList );
    }

    @Test
    public void testDoSaveStockTotal_15() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT );

        StockTotalAuditDTO stockTotalAuditDTO = new StockTotalAuditDTO();
        List<StockTotalDTO> stockTotalDTOList = Lists.newArrayList();
        StockTotalDTO stockTotalDTO = new StockTotalDTO();
        stockTotalDTOList.add( stockTotalDTO );

        List<StockTotalDTO> lstStockTotal = Lists.newArrayList();
        StockTotalDTO stockTotalDTO2 = new StockTotalDTO();
        stockTotalDTO.setStockTotalId( 1L );
        stockTotalDTO2.setStockTotalId( 2L );
        lstStockTotal.add( stockTotalDTO );
        lstStockTotal.add( stockTotalDTO2 );

        Mockito.when( stockTotalService.searchStockTotal( Mockito.any() ) ).thenReturn( lstStockTotal );
        ProductOfferingDTO offeringDTO = new ProductOfferingDTO();
        Mockito.when( productOfferingService.findOne( Mockito.anyLong() ) ).thenReturn( offeringDTO );

        baseStockService.doSaveStockTotal( stockTotalAuditDTO, stockTotalDTOList );
    }

    @Test
    public void testDoSaveStockTotal_16() throws Exception {

        StockTotalAuditDTO stockTotalAuditDTO = new StockTotalAuditDTO();
        List<StockTotalDTO> stockTotalDTOList = Lists.newArrayList();
        StockTotalDTO stockTotalDTO = new StockTotalDTO();
        stockTotalDTO.setAvailableQuantity( 1L );
        stockTotalDTO.setCurrentQuantity( 1L );
        stockTotalDTO.setHangQuantity( 1L );
        stockTotalDTOList.add( stockTotalDTO );

        List<StockTotalDTO> lstStockTotal = Lists.newArrayList();
        StockTotalDTO stockTotalDTO1 = new StockTotalDTO();
        stockTotalDTO1.setStockTotalId( 1L );
        stockTotalDTO1.setAvailableQuantity( 1L );
        stockTotalDTO1.setCurrentQuantity( 1L );
        stockTotalDTO1.setHangQuantity( 1L );

        StockTotalDTO stockTotalDTO2 = new StockTotalDTO();
        stockTotalDTO2.setStockTotalId( 2L );

        lstStockTotal.add( stockTotalDTO1 );
        lstStockTotal.add( stockTotalDTO2 );

        Mockito.when( stockTotalService.searchStockTotal( Mockito.any() ) ).thenReturn( lstStockTotal );

        ProductOfferingDTO offeringDTO = null;
        Mockito.when( productOfferingService.findOne( Mockito.anyLong() ) ).thenReturn( offeringDTO );

        StockTotal stockTotalUpdate = new StockTotal();
        Mockito.when( em.find( StockTotal.class, 1L, LockModeType.PESSIMISTIC_READ ) ).thenReturn( stockTotalUpdate );

        StockTotalDTO stockTotalToUpdate = new StockTotalDTO();
        stockTotalToUpdate.setAvailableQuantity( 1L );
        Mockito.when( totalMapper.toDtoBean( stockTotalUpdate ) ).thenReturn( stockTotalToUpdate );

        StockTotalDTO result = new StockTotalDTO();
        Mockito.when( stockTotalService.save( Mockito.any() ) ).thenReturn( result );

        BaseStockService spyTemp = Mockito.spy( baseStockService );
        Mockito.doNothing().when( spyTemp ).doSaveStockTotalAudit( Mockito.any() );

        baseStockService.doSaveStockTotal( stockTotalAuditDTO, stockTotalDTOList );
    }

    @Test
    public void testDoSaveStockTotal_19() throws Exception {

        StockTotalAuditDTO stockTotalAuditDTO = new StockTotalAuditDTO();
        List<StockTotalDTO> stockTotalDTOList = Lists.newArrayList();
        StockTotalDTO stockTotalDTO = new StockTotalDTO();
        stockTotalDTO.setAvailableQuantity( 1L );
        stockTotalDTO.setCurrentQuantity( 1L );
        stockTotalDTO.setHangQuantity( 1L );
        stockTotalDTOList.add( stockTotalDTO );

        Mockito.when( stockTotalService.searchStockTotal( Mockito.any() ) ).thenReturn( null );

        ProductOfferingDTO offeringDTO = null;
        Mockito.when( productOfferingService.findOne( Mockito.anyLong() ) ).thenReturn( offeringDTO );

        StockTotal stockTotalUpdate = new StockTotal();
        Mockito.when( em.find( StockTotal.class, 1L, LockModeType.PESSIMISTIC_READ ) ).thenReturn( stockTotalUpdate );

        StockTotalDTO stockTotalToUpdate = new StockTotalDTO();
        stockTotalToUpdate.setAvailableQuantity( 1L );
        Mockito.when( totalMapper.toDtoBean( stockTotalUpdate ) ).thenReturn( stockTotalToUpdate );

        StockTotalDTO result = new StockTotalDTO();
        Mockito.when( stockTotalService.save( Mockito.any() ) ).thenReturn( result );

        BaseStockService spyTemp = Mockito.spy( baseStockService );
        Mockito.doNothing().when( spyTemp ).doSaveStockTotalAudit( Mockito.any() );

        baseStockService.doSaveStockTotal( stockTotalAuditDTO, stockTotalDTOList );
    }

    @Test
    public void testDoSaveStockTotal_20() throws Exception {

        StockTotalAuditDTO stockTotalAuditDTO = new StockTotalAuditDTO();
        List<StockTotalDTO> stockTotalDTOList = Lists.newArrayList();
        StockTotalDTO stockTotalDTO = new StockTotalDTO();
        stockTotalDTO.setAvailableQuantity( 1L );
        stockTotalDTO.setCurrentQuantity( 1L );
        stockTotalDTO.setHangQuantity( 1L );
        stockTotalDTOList.add( stockTotalDTO );

        List<StockTotalDTO> lstStockTotal = Lists.newArrayList();
        StockTotalDTO stockTotalDTO1 = new StockTotalDTO();
        stockTotalDTO1.setStockTotalId( 1L );
        stockTotalDTO1.setAvailableQuantity( 1L );
        stockTotalDTO1.setCurrentQuantity( 1L );
        stockTotalDTO1.setHangQuantity( 1L );

        StockTotalDTO stockTotalDTO2 = new StockTotalDTO();
        stockTotalDTO2.setStockTotalId( 2L );

        lstStockTotal.add( stockTotalDTO1 );
        lstStockTotal.add( stockTotalDTO2 );

        Mockito.when( stockTotalService.searchStockTotal( Mockito.any() ) ).thenReturn( lstStockTotal );

        ProductOfferingDTO offeringDTO = null;
        Mockito.when( productOfferingService.findOne( Mockito.anyLong() ) ).thenReturn( offeringDTO );

        StockTotal stockTotalUpdate = new StockTotal();
        Mockito.when( em.find( StockTotal.class, 1L, LockModeType.PESSIMISTIC_READ ) ).thenReturn( stockTotalUpdate );

        StockTotalDTO stockTotalToUpdate = new StockTotalDTO();
        stockTotalToUpdate.setAvailableQuantity( 1L );
        Mockito.when( totalMapper.toDtoBean( stockTotalUpdate ) ).thenReturn( stockTotalToUpdate );

        StockTotalDTO result = new StockTotalDTO();
        Mockito.when( stockTotalService.save( Mockito.any() ) ).thenReturn( result );

        BaseStockService spyTemp = Mockito.spy( baseStockService );
        Mockito.doNothing().when( spyTemp ).doSaveStockTotalAudit( Mockito.any() );

        baseStockService.doSaveStockTotal( null, stockTotalDTOList );
    }

    @Test
    public void testDoSaveStockTotal_21() throws Exception {

        StockTotalAuditDTO stockTotalAuditDTO = new StockTotalAuditDTO();
        List<StockTotalDTO> stockTotalDTOList = Lists.newArrayList();
        StockTotalDTO stockTotalDTO = new StockTotalDTO();
        stockTotalDTO.setAvailableQuantity( 1L );
        stockTotalDTO.setCurrentQuantity( 1L );
        stockTotalDTO.setHangQuantity( 1L );
        stockTotalDTOList.add( stockTotalDTO );

        List<StockTotalDTO> lstStockTotal = Lists.newArrayList();
        StockTotalDTO stockTotalDTO1 = new StockTotalDTO();
        stockTotalDTO1.setStockTotalId( 1L );
        stockTotalDTO1.setAvailableQuantity( 1L );
        stockTotalDTO1.setCurrentQuantity( 1L );
        stockTotalDTO1.setHangQuantity( 1L );

        StockTotalDTO stockTotalDTO2 = new StockTotalDTO();
        stockTotalDTO2.setStockTotalId( 2L );

        lstStockTotal.add( stockTotalDTO1 );
        lstStockTotal.add( stockTotalDTO2 );

        Mockito.when( stockTotalService.searchStockTotal( Mockito.any() ) ).thenReturn( null );

        ProductOfferingDTO offeringDTO = null;
        Mockito.when( productOfferingService.findOne( Mockito.anyLong() ) ).thenReturn( offeringDTO );

        StockTotal stockTotalUpdate = new StockTotal();
        Mockito.when( em.find( StockTotal.class, 1L, LockModeType.PESSIMISTIC_READ ) ).thenReturn( stockTotalUpdate );

        StockTotalDTO stockTotalToUpdate = new StockTotalDTO();
        stockTotalToUpdate.setAvailableQuantity( 1L );
        Mockito.when( totalMapper.toDtoBean( stockTotalUpdate ) ).thenReturn( stockTotalToUpdate );

        StockTotalDTO result = new StockTotalDTO();
        Mockito.when( stockTotalService.save( Mockito.any() ) ).thenReturn( result );

        BaseStockService spyTemp = Mockito.spy( baseStockService );
        Mockito.doNothing().when( spyTemp ).doSaveStockTotalAudit( Mockito.any() );

        baseStockService.doSaveStockTotal( null, stockTotalDTOList );
    }

    @Test
    public void testDoSaveStockTotal_22() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );
        stockTransDTO.setExchangeStockId( 0L );
        stockTransDTO.setRegionStockId( 2L );
        stockTransDTO.setPayStatus( Const.PAY_STATUS.PAY_HAVE );
        stockTransDTO.setReasonId( 1L );

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        List<StockTransDetailDTO> result = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1L );
        stockTransDetailDTO.setStateId( 1L );
        stockTransDetailDTO.setQuantity( 1L );
        stockTransDetailDTO.setProdOfferId( 1L );

        result.add( stockTransDetailDTO );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setImportStock( false );
        flagStockDTO.setExportStock( true );
        flagStockDTO.setStatusForAgent( "3" );
        flagStockDTO.setShopId( 123L );
        flagStockDTO.setExpAvailableQuantity( 1L );
        flagStockDTO.setExpCurrentQuantity( 1L );
        flagStockDTO.setExpHangQuantity( 1L );
        flagStockDTO.setObjectType( Const.STOCK_TRANS.OBJECT_TYPE_AGENT_EXPORT );
        flagStockDTO.setInsertStockTotalAudit( true );

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();

        FilterRequest filterRequest = new FilterRequest();
        Mockito.when( stockTransDetailService.findByFilter( Lists.newArrayList( Mockito.anyCollection() ) ) ).thenReturn( result );

        List<OptionSetValueDTO> optionSetValueDTOList = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( null );
        optionSetValueDTOList.add( optionSetValueDTO );
        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( optionSetValueDTOList );
        Mockito.doNothing().when( baseStockIm1Service ).doSaveStockTotal( Mockito.any(), Mockito.anyList(), Mockito.any(), Mockito.any() );
        baseStockService.doSaveStockTotal( stockTransDTO, stockTransDetailDTOList, flagStockDTO, stockTransActionDTO );
    }

    @Test
    public void testDoSaveStockTotal_23() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );
        stockTransDTO.setExchangeStockId( 0L );
        stockTransDTO.setRegionStockId( 0L );
        stockTransDTO.setPayStatus( Const.PAY_STATUS.PAY_HAVE );
        stockTransDTO.setReasonId( 1L );

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        List<StockTransDetailDTO> result = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1L );
        stockTransDetailDTO.setStateId( 1L );
        stockTransDetailDTO.setQuantity( 1L );
        stockTransDetailDTO.setProdOfferId( 1L );

        result.add( stockTransDetailDTO );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setImportStock( false );
        flagStockDTO.setExportStock( true );
        flagStockDTO.setStatusForAgent( "2" );
        flagStockDTO.setShopId( 123L );
        flagStockDTO.setExpAvailableQuantity( 1L );
        flagStockDTO.setExpCurrentQuantity( 1L );
        flagStockDTO.setExpHangQuantity( 1L );
        flagStockDTO.setObjectType( Const.STOCK_TRANS.OBJECT_TYPE_AGENT_EXPORT );
        flagStockDTO.setInsertStockTotalAudit( true );

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();

        FilterRequest filterRequest = new FilterRequest();
        Mockito.when( stockTransDetailService.findByFilter( Lists.newArrayList( Mockito.anyCollection() ) ) ).thenReturn( result );

        List<OptionSetValueDTO> optionSetValueDTOList = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( null );
        optionSetValueDTOList.add( optionSetValueDTO );
        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( optionSetValueDTOList );
        Mockito.doNothing().when( baseStockIm1Service ).doSaveStockTotal( Mockito.any(), Mockito.anyList(), Mockito.any(), Mockito.any() );
        baseStockService.doSaveStockTotal( stockTransDTO, stockTransDetailDTOList, flagStockDTO, stockTransActionDTO );
    }

    @Test
    public void testDoSaveStockTotal_24() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );
        stockTransDTO.setExchangeStockId( 0L );
        stockTransDTO.setRegionStockId( 1L );
        stockTransDTO.setPayStatus( Const.PAY_STATUS.NOT_PAY );
        stockTransDTO.setReasonId( 1L );

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        List<StockTransDetailDTO> result = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1L );
        stockTransDetailDTO.setStateId( 1L );
        stockTransDetailDTO.setQuantity( 1L );
        stockTransDetailDTO.setProdOfferId( 1L );

        result.add( stockTransDetailDTO );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setImportStock( true );
        flagStockDTO.setExportStock( true );
        flagStockDTO.setStatusForAgent( null );
        flagStockDTO.setShopId( Const.L_VT_SHOP_ID );
        flagStockDTO.setExpAvailableQuantity( 1L );
        flagStockDTO.setExpCurrentQuantity( 1L );
        flagStockDTO.setExpHangQuantity( 1L );
        flagStockDTO.setExpAvailableQuantity( 1L );
        flagStockDTO.setExpAvailableQuantity( 1L );
        flagStockDTO.setObjectType( null );
        flagStockDTO.setInsertStockTotalAudit( true );
        flagStockDTO.setStateIdForReasonId( 1L );
        flagStockDTO.setImpAvailableQuantity( 1L );
        flagStockDTO.setImpCurrentQuantity( 1L );
        flagStockDTO.setImpHangQuantity( 1L );

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();

        FilterRequest filterRequest = new FilterRequest();
        Mockito.when( stockTransDetailService.findByFilter( Lists.newArrayList( Mockito.anyCollection() ) ) ).thenReturn( result );

        List<OptionSetValueDTO> optionSetValueDTOList = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        optionSetValueDTOList.add( optionSetValueDTO );
        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( optionSetValueDTOList );
        Mockito.doNothing().when( baseStockIm1Service ).doSaveStockTotal( Mockito.any(), Mockito.anyList(), Mockito.any(), Mockito.any() );
        baseStockService.doSaveStockTotal( stockTransDTO, stockTransDetailDTOList, flagStockDTO, stockTransActionDTO );
    }

    /** -------------------------------------------------------------------------- **/
    /** --- Test for stockTransSerialService.doSaveStockGoods method-------------- **/
    /**
     * --------------------------------------------------------------------------
     **/
    @Test
    public void testDoSaveStockGoods_1() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( Const.PROCESS_OFFLINE );

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        baseStockService.doSaveStockGoods( stockTransDTO, stockTransDetailDTOList, flagStockDTO );
    }

    @Test
    public void testDoSaveStockGoods_2() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setOldStatus( null );
        flagStockDTO.setNewStatus( 1L );

        baseStockService.doSaveStockGoods( stockTransDTO, stockTransDetailDTOList, flagStockDTO );
    }

    @Test
    public void testDoSaveStockGoods_3() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setOldStatus( 1L );
        flagStockDTO.setNewStatus( null );

        baseStockService.doSaveStockGoods( stockTransDTO, stockTransDetailDTOList, flagStockDTO );
    }

    @Test
    public void testDoSaveStockGoods_4() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT );

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );
        stockTransDTO.setExchangeStockId( 2L );
        stockTransDTO.setToOwnerType( 2L );
        stockTransDTO.setToOwnerId( 2L );
        stockTransDTO.setRegionStockId( null );
        stockTransDTO.setStatus( "6" );

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1L );
        stockTransDetailDTOList.add( stockTransDetailDTO );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setOldStatus( 1L );
        flagStockDTO.setNewStatus( 1L );
        flagStockDTO.setUpdateOwnerId( true );
        flagStockDTO.setStatusForAgent( "6" );
        flagStockDTO.setShopId( 7282L );

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );

        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        lstConfigEnableBccs1.add( optionSetValueDTO );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigEnableBccs1 );
        Mockito.when( productOfferingService.findOne( Mockito.anyLong() ) ).thenReturn( null );

        baseStockService.doSaveStockGoods( stockTransDTO, stockTransDetailDTOList, flagStockDTO );
    }

    @Test
    public void testDoSaveStockGoods_5() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT );

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );
        stockTransDTO.setExchangeStockId( 2L );
        stockTransDTO.setToOwnerType( 2L );
        stockTransDTO.setToOwnerId( 2L );
        stockTransDTO.setRegionStockId( 1L );
        stockTransDTO.setStatus( "6" );

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1L );
        stockTransDetailDTOList.add( stockTransDetailDTO );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setOldStatus( 1L );
        flagStockDTO.setNewStatus( 1L );
        flagStockDTO.setUpdateOwnerId( true );
        flagStockDTO.setStatusForAgent( "6" );
        flagStockDTO.setShopId( 7282L );

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );

        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        lstConfigEnableBccs1.add( optionSetValueDTO );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigEnableBccs1 );
        Mockito.when( productOfferingService.findOne( Mockito.anyLong() ) ).thenReturn( null );

        baseStockService.doSaveStockGoods( stockTransDTO, stockTransDetailDTOList, flagStockDTO );
    }

    @Test
    public void testDoSaveStockGoods_6() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );
        stockTransDTO.setExchangeStockId( 2L );
        stockTransDTO.setToOwnerType( 2L );
        stockTransDTO.setToOwnerId( 2L );
        stockTransDTO.setRegionStockId( 1L );
        stockTransDTO.setStatus( "6" );

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1L );
        stockTransDetailDTO.setTableName( null );
        stockTransDetailDTOList.add( stockTransDetailDTO );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setOldStatus( 1L );
        flagStockDTO.setNewStatus( 1L );
        flagStockDTO.setUpdateOwnerId( true );
        flagStockDTO.setStatusForAgent( "6" );
        flagStockDTO.setShopId( 7282L );

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );

        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        lstConfigEnableBccs1.add( optionSetValueDTO );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigEnableBccs1 );

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setProductOfferTypeId( 1L );

        Mockito.when( productOfferingService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferingDTO );

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName( null );
        Mockito.when( productOfferTypeService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferTypeDTO );

        baseStockService.doSaveStockGoods( stockTransDTO, stockTransDetailDTOList, flagStockDTO );
    }

    @Test
    public void testDoSaveStockGoods_7() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );
        stockTransDTO.setExchangeStockId( 2L );
        stockTransDTO.setToOwnerType( 2L );
        stockTransDTO.setToOwnerId( 2L );
        stockTransDTO.setRegionStockId( 1L );
        stockTransDTO.setStatus( "6" );

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1L );
        stockTransDetailDTO.setTableName( null );
        stockTransDetailDTOList.add( stockTransDetailDTO );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setOldStatus( 1L );
        flagStockDTO.setNewStatus( 1L );
        flagStockDTO.setUpdateOwnerId( true );
        flagStockDTO.setStatusForAgent( "6" );
        flagStockDTO.setShopId( 7282L );

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );

        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        lstConfigEnableBccs1.add( optionSetValueDTO );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigEnableBccs1 );

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setProductOfferTypeId( 1L );

        Mockito.when( productOfferingService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferingDTO );

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName( "STOCK_CARD" );
        Mockito.when( productOfferTypeService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferTypeDTO );

        List<OptionSetValueDTO> lstConfigStockCard = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO1 = new OptionSetValueDTO();
        optionSetValueDTO1.setValue( "1" );
        lstConfigStockCard.add( optionSetValueDTO1 );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigStockCard );

        Mockito.doNothing().when( stockCardService ).doSaveStockCard( Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any() );
        baseStockService.doSaveStockGoods( stockTransDTO, stockTransDetailDTOList, flagStockDTO );
    }

    @Test
    public void testDoSaveStockGoods_8() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT );

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );
        stockTransDTO.setExchangeStockId( 2L );
        stockTransDTO.setToOwnerType( 2L );
        stockTransDTO.setToOwnerId( 2L );
        stockTransDTO.setRegionStockId( 1L );
        stockTransDTO.setStatus( "6" );
        stockTransDTO.setPayStatus( "1" );

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1L );
        stockTransDetailDTO.setTableName( null );
        stockTransDetailDTO.setCheckSerial( 1L );
        stockTransDetailDTOList.add( stockTransDetailDTO );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setOldStatus( 1L );
        flagStockDTO.setNewStatus( 1L );
        flagStockDTO.setUpdateOwnerId( true );
        flagStockDTO.setStatusForAgent( "6" );
        flagStockDTO.setShopId( 7282L );
        flagStockDTO.setObjectType( "AGENT_EXPORT" );
        flagStockDTO.setSerialListFromTransDetail( true );

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );

        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        lstConfigEnableBccs1.add( optionSetValueDTO );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigEnableBccs1 );

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setProductOfferTypeId( 1L );

        Mockito.when( productOfferingService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferingDTO );

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName( "STOCK_NUMBER" );
        Mockito.when( productOfferTypeService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferTypeDTO );

        List<OptionSetValueDTO> lstConfigStockCard = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO1 = new OptionSetValueDTO();
        optionSetValueDTO1.setValue( "1" );
        lstConfigStockCard.add( optionSetValueDTO1 );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigStockCard );

        Mockito.doNothing().when( stockCardService ).doSaveStockCard( Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any() );

        baseStockService.doSaveStockGoods( stockTransDTO, stockTransDetailDTOList, flagStockDTO );
    }

    @Test
    public void testDoSaveStockGoods_9() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT );

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );
        stockTransDTO.setExchangeStockId( 2L );
        stockTransDTO.setToOwnerType( 2L );
        stockTransDTO.setToOwnerId( 2L );
        stockTransDTO.setRegionStockId( 1L );
        stockTransDTO.setStatus( "6" );
        stockTransDTO.setPayStatus( "5" );
        stockTransDTO.setBankplusStatus( 0L );

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1L );
        stockTransDetailDTO.setTableName( null );
        stockTransDetailDTO.setQuantity( 3L );

        stockTransDetailDTO.setCheckSerial( 2L );
        stockTransDetailDTOList.add( stockTransDetailDTO );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setOldStatus( 1L );
        flagStockDTO.setNewStatus( 1L );
        flagStockDTO.setUpdateOwnerId( true );
        flagStockDTO.setStatusForAgent( "6" );
        flagStockDTO.setShopId( 7282L );
        flagStockDTO.setObjectType( "AGENT_EXPORT" );
        flagStockDTO.setUpdateSaleDate( true );
        flagStockDTO.setGpon( true );
        flagStockDTO.setUpdateAccountBalance( false );
        flagStockDTO.setUpdateDepositPrice( true );
        flagStockDTO.setStateIdForReasonId( 1L );
        flagStockDTO.setUpdateStateDemo( true );
        flagStockDTO.setUpdateProdOfferId( true );

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );

        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        lstConfigEnableBccs1.add( optionSetValueDTO );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigEnableBccs1 );

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setProductOfferTypeId( 7L );

        Mockito.when( productOfferingService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferingDTO );

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName( "STOCK_HANDSET" );
        Mockito.when( productOfferTypeService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferTypeDTO );

        List<OptionSetValueDTO> lstConfigStockCard = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO1 = new OptionSetValueDTO();
        optionSetValueDTO1.setValue( "1" );
        lstConfigStockCard.add( optionSetValueDTO1 );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigStockCard );

        Mockito.doNothing().when( stockCardService ).doSaveStockCard( Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any() );
        List<StockTransSerialDTO> lstStockTransSerial = Lists.newArrayList();
        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        stockTransSerialDTO.setStateId( 1L );
        stockTransSerialDTO.setFromSerial( "1" );
        stockTransSerialDTO.setToSerial( "1" );
        lstStockTransSerial.add( stockTransSerialDTO );
        StockTotalDTO stockTotalDTOExport = new StockTotalDTO();
        stockTotalDTOExport.setAvailableQuantity( 1L );

        BaseStockService spyTemp = PowerMockito.spy( baseStockService );
        Mockito.when( stockTransSerialService.findByStockTransDetailId( Mockito.anyLong() ) ).thenReturn( lstStockTransSerial );
        PowerMockito.doReturn( 2L ).when( spyTemp, "updateSerialPairCollection", any(), any() );

        spyTemp.doSaveStockGoods( stockTransDTO, stockTransDetailDTOList, flagStockDTO );
    }

    @Test
    public void testDoSaveStockGoods_10() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );
        stockTransDTO.setExchangeStockId( 2L );
        stockTransDTO.setToOwnerType( 2L );
        stockTransDTO.setToOwnerId( 2L );
        stockTransDTO.setRegionStockId( 1L );
        stockTransDTO.setStatus( "6" );
        stockTransDTO.setPayStatus( "5" );
        stockTransDTO.setBankplusStatus( 0L );

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1L );
        stockTransDetailDTO.setTableName( null );
        stockTransDetailDTO.setQuantity( 2L );

        stockTransDetailDTO.setCheckSerial( 2L );
        stockTransDetailDTOList.add( stockTransDetailDTO );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setOldStatus( 1L );
        flagStockDTO.setNewStatus( 1L );
        flagStockDTO.setUpdateOwnerId( true );
        flagStockDTO.setStatusForAgent( "6" );
        flagStockDTO.setShopId( 7282L );
        flagStockDTO.setObjectType( "AGENT_EXPORT" );
        flagStockDTO.setUpdateSaleDate( true );
        flagStockDTO.setGpon( true );
        flagStockDTO.setUpdateAccountBalance( false );
        flagStockDTO.setUpdateDepositPrice( true );
        flagStockDTO.setStateIdForReasonId( 1L );
        flagStockDTO.setUpdateStateDemo( true );
        flagStockDTO.setUpdateProdOfferId( true );

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );

        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        lstConfigEnableBccs1.add( optionSetValueDTO );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigEnableBccs1 );

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setProductOfferTypeId( 4L );

        Mockito.when( productOfferingService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferingDTO );

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName( "STOCK_HANDSET" );
        Mockito.when( productOfferTypeService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferTypeDTO );

        List<OptionSetValueDTO> lstConfigStockCard = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO1 = new OptionSetValueDTO();
        optionSetValueDTO1.setValue( "1" );
        lstConfigStockCard.add( optionSetValueDTO1 );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigStockCard );

        Mockito.doNothing().when( stockCardService ).doSaveStockCard( Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any() );
        List<StockTransSerialDTO> lstStockTransSerial = Lists.newArrayList();
        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        stockTransSerialDTO.setStateId( 1L );
        stockTransSerialDTO.setFromSerial( "1" );
        stockTransSerialDTO.setToSerial( "1" );
        lstStockTransSerial.add( stockTransSerialDTO );
        StockTotalDTO stockTotalDTOExport = new StockTotalDTO();
        stockTotalDTOExport.setAvailableQuantity( 1L );

        BaseStockService spyTemp = PowerMockito.spy( baseStockService );
        Mockito.when( stockTransSerialService.findByStockTransDetailId( Mockito.anyLong() ) ).thenReturn( lstStockTransSerial );
        PowerMockito.doReturn( 2L ).when( spyTemp, "updateSerialPairCollection", any(), any() );

        spyTemp.doSaveStockGoods( stockTransDTO, stockTransDetailDTOList, flagStockDTO );
    }

    @Test
    public void testDoSaveStockGoods_11() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );
        stockTransDTO.setExchangeStockId( 2L );
        stockTransDTO.setToOwnerType( 2L );
        stockTransDTO.setToOwnerId( 2L );
        stockTransDTO.setRegionStockId( 1L );
        stockTransDTO.setStatus( "6" );
        stockTransDTO.setPayStatus( "5" );
        stockTransDTO.setBankplusStatus( 0L );

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1L );
        stockTransDetailDTO.setTableName( null );
        stockTransDetailDTO.setQuantity( 2L );

        stockTransDetailDTO.setCheckSerial( 2L );
        stockTransDetailDTOList.add( stockTransDetailDTO );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setOldStatus( 1L );
        flagStockDTO.setNewStatus( 1L );
        flagStockDTO.setUpdateOwnerId( true );
        flagStockDTO.setStatusForAgent( "6" );
        flagStockDTO.setShopId( 7282L );
        flagStockDTO.setObjectType( "AGENT_EXPORT" );
        flagStockDTO.setUpdateSaleDate( true );
        flagStockDTO.setGpon( true );
        flagStockDTO.setUpdateAccountBalance( false );
        flagStockDTO.setUpdateDepositPrice( true );
        flagStockDTO.setStateIdForReasonId( 1L );
        flagStockDTO.setUpdateStateDemo( true );
        flagStockDTO.setUpdateProdOfferId( true );

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );

        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        lstConfigEnableBccs1.add( optionSetValueDTO );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigEnableBccs1 );

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setProductOfferTypeId( 6L );

        Mockito.when( productOfferingService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferingDTO );

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName( "STOCK_HANDSET" );
        Mockito.when( productOfferTypeService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferTypeDTO );

        List<OptionSetValueDTO> lstConfigStockCard = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO1 = new OptionSetValueDTO();
        optionSetValueDTO1.setValue( "1" );
        lstConfigStockCard.add( optionSetValueDTO1 );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigStockCard );

        Mockito.doNothing().when( stockCardService ).doSaveStockCard( Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any() );
        List<StockTransSerialDTO> lstStockTransSerial = Lists.newArrayList();
        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        stockTransSerialDTO.setStateId( 1L );
        stockTransSerialDTO.setFromSerial( "1" );
        stockTransSerialDTO.setToSerial( "1" );
        lstStockTransSerial.add( stockTransSerialDTO );
        StockTotalDTO stockTotalDTOExport = new StockTotalDTO();
        stockTotalDTOExport.setAvailableQuantity( 1L );

        BaseStockService spyTemp = PowerMockito.spy( baseStockService );
        Mockito.when( stockTransSerialService.findByStockTransDetailId( Mockito.anyLong() ) ).thenReturn( lstStockTransSerial );
        PowerMockito.doReturn( 2L ).when( spyTemp, "updateSerialPairCollection", any(), any() );

        spyTemp.doSaveStockGoods( stockTransDTO, stockTransDetailDTOList, flagStockDTO );
    }

    @Test
    public void testDoSaveStockGoods_12() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );
        stockTransDTO.setExchangeStockId( 2L );
        stockTransDTO.setToOwnerType( 2L );
        stockTransDTO.setToOwnerId( 2L );
        stockTransDTO.setRegionStockId( 1L );
        stockTransDTO.setStatus( "6" );
        stockTransDTO.setPayStatus( "5" );
        stockTransDTO.setBankplusStatus( 0L );

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1L );
        stockTransDetailDTO.setTableName( null );
        stockTransDetailDTO.setQuantity( 2L );

        stockTransDetailDTO.setCheckSerial( 2L );
        stockTransDetailDTOList.add( stockTransDetailDTO );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setOldStatus( 1L );
        flagStockDTO.setNewStatus( 1L );
        flagStockDTO.setUpdateOwnerId( true );
        flagStockDTO.setStatusForAgent( "6" );
        flagStockDTO.setShopId( 7282L );
        flagStockDTO.setObjectType( "AGENT_EXPORT" );
        flagStockDTO.setUpdateSaleDate( true );
        flagStockDTO.setGpon( true );
        flagStockDTO.setUpdateAccountBalance( false );
        flagStockDTO.setUpdateDepositPrice( true );
        flagStockDTO.setStateIdForReasonId( 1L );
        flagStockDTO.setUpdateStateDemo( true );
        flagStockDTO.setUpdateProdOfferId( true );

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );

        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        lstConfigEnableBccs1.add( optionSetValueDTO );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigEnableBccs1 );

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setProductOfferTypeId( 10L );

        Mockito.when( productOfferingService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferingDTO );

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName( "STOCK_HANDSET" );
        Mockito.when( productOfferTypeService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferTypeDTO );

        List<OptionSetValueDTO> lstConfigStockCard = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO1 = new OptionSetValueDTO();
        optionSetValueDTO1.setValue( "1" );
        lstConfigStockCard.add( optionSetValueDTO1 );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigStockCard );

        Mockito.doNothing().when( stockCardService ).doSaveStockCard( Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any() );
        List<StockTransSerialDTO> lstStockTransSerial = Lists.newArrayList();
        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        stockTransSerialDTO.setStateId( 1L );
        stockTransSerialDTO.setFromSerial( "1" );
        stockTransSerialDTO.setToSerial( "1" );
        lstStockTransSerial.add( stockTransSerialDTO );
        StockTotalDTO stockTotalDTOExport = new StockTotalDTO();
        stockTotalDTOExport.setAvailableQuantity( 1L );

        BaseStockService spyTemp = PowerMockito.spy( baseStockService );
        Mockito.when( stockTransSerialService.findByStockTransDetailId( Mockito.anyLong() ) ).thenReturn( lstStockTransSerial );
        PowerMockito.doReturn( 2L ).when( spyTemp, "updateSerialPairCollection", any(), any() );

        spyTemp.doSaveStockGoods( stockTransDTO, stockTransDetailDTOList, flagStockDTO );
    }

    @Test
    public void testDoSaveStockGoods_13() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );
        stockTransDTO.setExchangeStockId( 2L );
        stockTransDTO.setToOwnerType( 2L );
        stockTransDTO.setToOwnerId( 2L );
        stockTransDTO.setRegionStockId( 1L );
        stockTransDTO.setStatus( "6" );
        stockTransDTO.setPayStatus( "5" );
        stockTransDTO.setBankplusStatus( 0L );

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1L );
        stockTransDetailDTO.setTableName( null );
        stockTransDetailDTO.setQuantity( 2L );

        stockTransDetailDTO.setCheckSerial( 2L );
        stockTransDetailDTOList.add( stockTransDetailDTO );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setOldStatus( 1L );
        flagStockDTO.setNewStatus( 1L );
        flagStockDTO.setUpdateOwnerId( true );
        flagStockDTO.setStatusForAgent( "6" );
        flagStockDTO.setShopId( 7282L );
        flagStockDTO.setObjectType( "AGENT_EXPORT" );
        flagStockDTO.setUpdateSaleDate( true );
        flagStockDTO.setGpon( true );
        flagStockDTO.setUpdateAccountBalance( false );
        flagStockDTO.setUpdateDepositPrice( true );
        flagStockDTO.setStateIdForReasonId( 1L );
        flagStockDTO.setUpdateStateDemo( true );
        flagStockDTO.setUpdateProdOfferId( true );

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );

        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        lstConfigEnableBccs1.add( optionSetValueDTO );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigEnableBccs1 );

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setProductOfferTypeId( 7L );

        Mockito.when( productOfferingService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferingDTO );

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName( "STOCK_HANDSET" );
        Mockito.when( productOfferTypeService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferTypeDTO );

        List<OptionSetValueDTO> lstConfigStockCard = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO1 = new OptionSetValueDTO();
        optionSetValueDTO1.setValue( "1" );
        lstConfigStockCard.add( optionSetValueDTO1 );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigStockCard );

        Mockito.doNothing().when( stockCardService ).doSaveStockCard( Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any() );
        List<StockTransSerialDTO> lstStockTransSerial = Lists.newArrayList();
        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        stockTransSerialDTO.setStateId( 1L );
        stockTransSerialDTO.setFromSerial( "1" );
        stockTransSerialDTO.setToSerial( "1" );
        lstStockTransSerial.add( stockTransSerialDTO );
        StockTotalDTO stockTotalDTOExport = new StockTotalDTO();
        stockTotalDTOExport.setAvailableQuantity( 1L );

        BaseStockService spyTemp = PowerMockito.spy( baseStockService );
        Mockito.when( stockTransSerialService.findByStockTransDetailId( Mockito.anyLong() ) ).thenReturn( lstStockTransSerial );
        PowerMockito.doReturn( 2L ).when( spyTemp, "updateSerialPairCollection", any(), any() );

        spyTemp.doSaveStockGoods( stockTransDTO, stockTransDetailDTOList, flagStockDTO );
    }

    @Test
    public void testDoSaveStockGoods_14() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( "ESGUSF_001" );

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );
        stockTransDTO.setExchangeStockId( 2L );
        stockTransDTO.setToOwnerType( 2L );
        stockTransDTO.setToOwnerId( 2L );
        stockTransDTO.setRegionStockId( 1L );
        stockTransDTO.setStatus( "6" );
        stockTransDTO.setPayStatus( "5" );
        stockTransDTO.setBankplusStatus( 0L );

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1L );
        stockTransDetailDTO.setTableName( null );
        stockTransDetailDTO.setQuantity( 2L );

        stockTransDetailDTO.setCheckSerial( 2L );
        stockTransDetailDTOList.add( stockTransDetailDTO );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setOldStatus( 1L );
        flagStockDTO.setNewStatus( 1L );
        flagStockDTO.setUpdateOwnerId( true );
        flagStockDTO.setStatusForAgent( "6" );
        flagStockDTO.setShopId( 7282L );
        flagStockDTO.setObjectType( "AGENT_EXPORT" );
        flagStockDTO.setUpdateSaleDate( true );
        flagStockDTO.setGpon( true );
        flagStockDTO.setUpdateAccountBalance( false );
        flagStockDTO.setUpdateDepositPrice( true );
        flagStockDTO.setStateIdForReasonId( 1L );
        flagStockDTO.setUpdateStateDemo( true );
        flagStockDTO.setUpdateProdOfferId( true );

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );

        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        lstConfigEnableBccs1.add( optionSetValueDTO );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigEnableBccs1 );

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setProductOfferTypeId( 2L );

        Mockito.when( productOfferingService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferingDTO );

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName( "STOCK_HANDSET" );
        Mockito.when( productOfferTypeService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferTypeDTO );

        List<OptionSetValueDTO> lstConfigStockCard = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO1 = new OptionSetValueDTO();
        optionSetValueDTO1.setValue( "1" );
        lstConfigStockCard.add( optionSetValueDTO1 );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigStockCard );

        Mockito.doNothing().when( stockCardService ).doSaveStockCard( Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any() );
        List<StockTransSerialDTO> lstStockTransSerial = Lists.newArrayList();
        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        stockTransSerialDTO.setStateId( 1L );
        stockTransSerialDTO.setFromSerial( "1" );
        stockTransSerialDTO.setToSerial( "1" );
        lstStockTransSerial.add( stockTransSerialDTO );
        StockTotalDTO stockTotalDTOExport = new StockTotalDTO();
        stockTotalDTOExport.setAvailableQuantity( 1L );

        BaseStockService spyTemp = PowerMockito.spy( baseStockService );
        Mockito.when( stockTransSerialService.findByStockTransDetailId( Mockito.anyLong() ) ).thenReturn( lstStockTransSerial );
        PowerMockito.doReturn( 2L ).when( spyTemp, "updateSerialPairCollection", any(), any() );

        spyTemp.doSaveStockGoods( stockTransDTO, stockTransDetailDTOList, flagStockDTO );
    }

    @Test
    public void testDoSaveStockGoods_15() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( "ESGUSF_001" );

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );
        stockTransDTO.setExchangeStockId( 2L );
        stockTransDTO.setToOwnerType( 2L );
        stockTransDTO.setToOwnerId( 2L );
        stockTransDTO.setRegionStockId( 1L );
        stockTransDTO.setStatus( "6" );
        stockTransDTO.setPayStatus( "5" );
        stockTransDTO.setBankplusStatus( 0L );

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1L );
        stockTransDetailDTO.setTableName( null );
        stockTransDetailDTO.setQuantity( 2L );

        stockTransDetailDTO.setCheckSerial( 2L );
        stockTransDetailDTOList.add( stockTransDetailDTO );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setOldStatus( 1L );
        flagStockDTO.setNewStatus( 1L );
        flagStockDTO.setUpdateOwnerId( true );
        flagStockDTO.setStatusForAgent( "6" );
        flagStockDTO.setShopId( 7282L );
        flagStockDTO.setObjectType( "AGENT_EXPORT" );
        flagStockDTO.setUpdateSaleDate( true );
        flagStockDTO.setGpon( true );
        flagStockDTO.setUpdateAccountBalance( false );
        flagStockDTO.setUpdateDepositPrice( true );
        flagStockDTO.setStateIdForReasonId( 1L );
        flagStockDTO.setUpdateStateDemo( true );
        flagStockDTO.setUpdateProdOfferId( true );

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );

        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        lstConfigEnableBccs1.add( optionSetValueDTO );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigEnableBccs1 );

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setProductOfferTypeId( 1L );

        Mockito.when( productOfferingService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferingDTO );

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName( "STOCK_HANDSET" );
        Mockito.when( productOfferTypeService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferTypeDTO );

        List<OptionSetValueDTO> lstConfigStockCard = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO1 = new OptionSetValueDTO();
        optionSetValueDTO1.setValue( "1" );
        lstConfigStockCard.add( optionSetValueDTO1 );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigStockCard );

        Mockito.doNothing().when( stockCardService ).doSaveStockCard( Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any() );
        List<StockTransSerialDTO> lstStockTransSerial = Lists.newArrayList();
        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        stockTransSerialDTO.setStateId( 1L );
        stockTransSerialDTO.setFromSerial( "1" );
        stockTransSerialDTO.setToSerial( "1" );
        lstStockTransSerial.add( stockTransSerialDTO );
        StockTotalDTO stockTotalDTOExport = new StockTotalDTO();
        stockTotalDTOExport.setAvailableQuantity( 1L );

        BaseStockService spyTemp = PowerMockito.spy( baseStockService );
        Mockito.when( stockTransSerialService.findByStockTransDetailId( Mockito.anyLong() ) ).thenReturn( lstStockTransSerial );
        PowerMockito.doReturn( 2L ).when( spyTemp, "updateSerialPairCollection", any(), any() );

        spyTemp.doSaveStockGoods( stockTransDTO, stockTransDetailDTOList, flagStockDTO );
    }

    @Test
    public void testDoSaveStockGoods_16() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( "ESGUSF_001" );

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );
        stockTransDTO.setExchangeStockId( 2L );
        stockTransDTO.setToOwnerType( 2L );
        stockTransDTO.setToOwnerId( 2L );
        stockTransDTO.setRegionStockId( 1L );
        stockTransDTO.setStatus( "6" );
        stockTransDTO.setPayStatus( "5" );
        stockTransDTO.setBankplusStatus( 0L );

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1L );
        stockTransDetailDTO.setTableName( null );
        stockTransDetailDTO.setQuantity( 2L );

        stockTransDetailDTO.setCheckSerial( 2L );
        stockTransDetailDTOList.add( stockTransDetailDTO );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setOldStatus( 1L );
        flagStockDTO.setNewStatus( 1L );
        flagStockDTO.setUpdateOwnerId( true );
        flagStockDTO.setStatusForAgent( "6" );
        flagStockDTO.setShopId( 7282L );
        flagStockDTO.setObjectType( "AGENT_EXPORT" );
        flagStockDTO.setUpdateSaleDate( true );
        flagStockDTO.setGpon( true );
        flagStockDTO.setUpdateAccountBalance( false );
        flagStockDTO.setUpdateDepositPrice( true );
        flagStockDTO.setStateIdForReasonId( 1L );
        flagStockDTO.setUpdateStateDemo( true );
        flagStockDTO.setUpdateProdOfferId( true );

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );

        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        lstConfigEnableBccs1.add( optionSetValueDTO );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigEnableBccs1 );

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setProductOfferTypeId( 3L );

        Mockito.when( productOfferingService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferingDTO );

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName( "STOCK_HANDSET" );
        Mockito.when( productOfferTypeService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferTypeDTO );

        List<OptionSetValueDTO> lstConfigStockCard = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO1 = new OptionSetValueDTO();
        optionSetValueDTO1.setValue( "1" );
        lstConfigStockCard.add( optionSetValueDTO1 );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigStockCard );

        Mockito.doNothing().when( stockCardService ).doSaveStockCard( Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any() );
        List<StockTransSerialDTO> lstStockTransSerial = Lists.newArrayList();
        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        stockTransSerialDTO.setStateId( 1L );
        stockTransSerialDTO.setFromSerial( "1" );
        stockTransSerialDTO.setToSerial( "1" );
        lstStockTransSerial.add( stockTransSerialDTO );
        StockTotalDTO stockTotalDTOExport = new StockTotalDTO();
        stockTotalDTOExport.setAvailableQuantity( 1L );

        BaseStockService spyTemp = PowerMockito.spy( baseStockService );
        Mockito.when( stockTransSerialService.findByStockTransDetailId( Mockito.anyLong() ) ).thenReturn( lstStockTransSerial );
        PowerMockito.doReturn( 2L ).when( spyTemp, "updateSerialPairCollection", any(), any() );

        spyTemp.doSaveStockGoods( stockTransDTO, stockTransDetailDTOList, flagStockDTO );
    }

    @Test
    public void testDoSaveStockGoods_17() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );
        stockTransDTO.setExchangeStockId( 2L );
        stockTransDTO.setToOwnerType( 2L );
        stockTransDTO.setToOwnerId( 2L );
        stockTransDTO.setRegionStockId( 1L );
        stockTransDTO.setStatus( "6" );
        stockTransDTO.setPayStatus( "5" );
        stockTransDTO.setBankplusStatus( 0L );

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1L );
        stockTransDetailDTO.setTableName( null );
        stockTransDetailDTO.setQuantity( 2L );

        stockTransDetailDTO.setCheckSerial( 2L );
        stockTransDetailDTOList.add( stockTransDetailDTO );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setOldStatus( 1L );
        flagStockDTO.setNewStatus( 1L );
        flagStockDTO.setUpdateOwnerId( true );
        flagStockDTO.setStatusForAgent( "6" );
        flagStockDTO.setShopId( 7282L );
        flagStockDTO.setObjectType( "AGENT_EXPORT" );
        flagStockDTO.setUpdateSaleDate( true );
        flagStockDTO.setGpon( true );
        flagStockDTO.setUpdateAccountBalance( false );
        flagStockDTO.setUpdateDepositPrice( true );
        flagStockDTO.setStateIdForReasonId( 1L );
        flagStockDTO.setUpdateStateDemo( true );
        flagStockDTO.setUpdateProdOfferId( true );

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );

        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        lstConfigEnableBccs1.add( optionSetValueDTO );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigEnableBccs1 );

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setProductOfferTypeId( 8L );

        Mockito.when( productOfferingService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferingDTO );

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName( "STOCK_HANDSET" );
        Mockito.when( productOfferTypeService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferTypeDTO );

        List<OptionSetValueDTO> lstConfigStockCard = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO1 = new OptionSetValueDTO();
        optionSetValueDTO1.setValue( "1" );
        lstConfigStockCard.add( optionSetValueDTO1 );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigStockCard );

        Mockito.doNothing().when( stockCardService ).doSaveStockCard( Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any() );
        List<StockTransSerialDTO> lstStockTransSerial = Lists.newArrayList();
        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        stockTransSerialDTO.setStateId( 1L );
        stockTransSerialDTO.setFromSerial( "1" );
        stockTransSerialDTO.setToSerial( "1" );
        lstStockTransSerial.add( stockTransSerialDTO );
        StockTotalDTO stockTotalDTOExport = new StockTotalDTO();
        stockTotalDTOExport.setAvailableQuantity( 1L );

        BaseStockService spyTemp = PowerMockito.spy( baseStockService );
        Mockito.when( stockTransSerialService.findByStockTransDetailId( Mockito.anyLong() ) ).thenReturn( lstStockTransSerial );
        PowerMockito.doReturn( 2L ).when( spyTemp, "updateSerialPairCollection", any(), any() );

        spyTemp.doSaveStockGoods( stockTransDTO, stockTransDetailDTOList, flagStockDTO );
    }

    @Test
    public void testDoSaveStockGoods_18() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );
        stockTransDTO.setExchangeStockId( 2L );
        stockTransDTO.setToOwnerType( 2L );
        stockTransDTO.setToOwnerId( 2L );
        stockTransDTO.setRegionStockId( 1L );
        stockTransDTO.setStatus( "6" );
        stockTransDTO.setPayStatus( "5" );
        stockTransDTO.setBankplusStatus( 0L );

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1L );
        stockTransDetailDTO.setTableName( null );
        stockTransDetailDTO.setQuantity( 2L );

        stockTransDetailDTO.setCheckSerial( 2L );
        stockTransDetailDTOList.add( stockTransDetailDTO );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setOldStatus( 1L );
        flagStockDTO.setNewStatus( 1L );
        flagStockDTO.setUpdateOwnerId( true );
        flagStockDTO.setStatusForAgent( "6" );
        flagStockDTO.setShopId( 7282L );
        flagStockDTO.setObjectType( "AGENT_EXPORT" );
        flagStockDTO.setUpdateSaleDate( true );
        flagStockDTO.setGpon( true );
        flagStockDTO.setUpdateAccountBalance( false );
        flagStockDTO.setUpdateDepositPrice( true );
        flagStockDTO.setStateIdForReasonId( 1L );
        flagStockDTO.setUpdateStateDemo( true );
        flagStockDTO.setUpdateProdOfferId( true );

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );

        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        lstConfigEnableBccs1.add( optionSetValueDTO );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigEnableBccs1 );

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setProductOfferTypeId( 4L );

        Mockito.when( productOfferingService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferingDTO );

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName( "STOCK_HANDSET" );
        Mockito.when( productOfferTypeService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferTypeDTO );

        List<OptionSetValueDTO> lstConfigStockCard = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO1 = new OptionSetValueDTO();
        optionSetValueDTO1.setValue( "1" );
        lstConfigStockCard.add( optionSetValueDTO1 );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigStockCard );

        Mockito.doNothing().when( stockCardService ).doSaveStockCard( Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any() );
        List<StockTransSerialDTO> lstStockTransSerial = Lists.newArrayList();
        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        stockTransSerialDTO.setStateId( 1L );
        stockTransSerialDTO.setFromSerial( "1" );
        stockTransSerialDTO.setToSerial( "1" );
        lstStockTransSerial.add( stockTransSerialDTO );
        StockTotalDTO stockTotalDTOExport = new StockTotalDTO();
        stockTotalDTOExport.setAvailableQuantity( 1L );

        BaseStockService spyTemp = PowerMockito.spy( baseStockService );
        Mockito.when( stockTransSerialService.findByStockTransDetailId( Mockito.anyLong() ) ).thenReturn( lstStockTransSerial );
        PowerMockito.doReturn( 2L ).when( spyTemp, "updateSerialPairCollection", any(), any() );

        spyTemp.doSaveStockGoods( stockTransDTO, stockTransDetailDTOList, flagStockDTO );
    }

    @Test
    public void testDoSaveStockGoods_19() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );
        stockTransDTO.setExchangeStockId( 2L );
        stockTransDTO.setToOwnerType( 2L );
        stockTransDTO.setToOwnerId( 2L );
        stockTransDTO.setRegionStockId( 1L );
        stockTransDTO.setStatus( "6" );
        stockTransDTO.setPayStatus( "5" );
        stockTransDTO.setBankplusStatus( 0L );

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1L );
        stockTransDetailDTO.setTableName( null );
        stockTransDetailDTO.setQuantity( 2L );

        stockTransDetailDTO.setCheckSerial( 2L );
        stockTransDetailDTOList.add( stockTransDetailDTO );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setOldStatus( 1L );
        flagStockDTO.setNewStatus( 1L );
        flagStockDTO.setUpdateOwnerId( true );
        flagStockDTO.setStatusForAgent( "6" );
        flagStockDTO.setShopId( 7282L );
        flagStockDTO.setObjectType( "AGENT_EXPORT" );
        flagStockDTO.setUpdateSaleDate( true );
        flagStockDTO.setGpon( true );
        flagStockDTO.setUpdateAccountBalance( false );
        flagStockDTO.setUpdateDepositPrice( true );
        flagStockDTO.setStateIdForReasonId( 1L );
        flagStockDTO.setUpdateStateDemo( true );
        flagStockDTO.setUpdateProdOfferId( true );

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );

        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        lstConfigEnableBccs1.add( optionSetValueDTO );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigEnableBccs1 );

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setProductOfferTypeId( 5L );

        Mockito.when( productOfferingService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferingDTO );

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName( "STOCK_HANDSET" );
        Mockito.when( productOfferTypeService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferTypeDTO );

        List<OptionSetValueDTO> lstConfigStockCard = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO1 = new OptionSetValueDTO();
        optionSetValueDTO1.setValue( "1" );
        lstConfigStockCard.add( optionSetValueDTO1 );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigStockCard );

        Mockito.doNothing().when( stockCardService ).doSaveStockCard( Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any() );
        List<StockTransSerialDTO> lstStockTransSerial = Lists.newArrayList();
        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        stockTransSerialDTO.setStateId( 1L );
        stockTransSerialDTO.setFromSerial( "1" );
        stockTransSerialDTO.setToSerial( "1" );
        lstStockTransSerial.add( stockTransSerialDTO );
        StockTotalDTO stockTotalDTOExport = new StockTotalDTO();
        stockTotalDTOExport.setAvailableQuantity( 1L );

        BaseStockService spyTemp = PowerMockito.spy( baseStockService );
        Mockito.when( stockTransSerialService.findByStockTransDetailId( Mockito.anyLong() ) ).thenReturn( lstStockTransSerial );
        PowerMockito.doReturn( 2L ).when( spyTemp, "updateSerialPairCollection", any(), any() );

        spyTemp.doSaveStockGoods( stockTransDTO, stockTransDetailDTOList, flagStockDTO );
    }

    @Test
    public void testDoSaveStockGoods_20() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( "ESGUSF_001" );

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );
        stockTransDTO.setExchangeStockId( 2L );
        stockTransDTO.setToOwnerType( 2L );
        stockTransDTO.setToOwnerId( 2L );
        stockTransDTO.setRegionStockId( 1L );
        stockTransDTO.setStatus( "6" );
        stockTransDTO.setPayStatus( "5" );
        stockTransDTO.setBankplusStatus( 0L );

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1L );
        stockTransDetailDTO.setTableName( null );
        stockTransDetailDTO.setQuantity( 2L );

        stockTransDetailDTO.setCheckSerial( 2L );
        stockTransDetailDTOList.add( stockTransDetailDTO );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setOldStatus( 1L );
        flagStockDTO.setNewStatus( 1L );
        flagStockDTO.setUpdateOwnerId( true );
        flagStockDTO.setStatusForAgent( "6" );
        flagStockDTO.setShopId( 7282L );
        flagStockDTO.setObjectType( "AGENT_EXPORT" );
        flagStockDTO.setUpdateSaleDate( true );
        flagStockDTO.setGpon( true );
        flagStockDTO.setUpdateAccountBalance( false );
        flagStockDTO.setUpdateDepositPrice( true );
        flagStockDTO.setStateIdForReasonId( 1L );
        flagStockDTO.setUpdateStateDemo( true );
        flagStockDTO.setUpdateProdOfferId( true );

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );

        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        lstConfigEnableBccs1.add( optionSetValueDTO );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigEnableBccs1 );

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setProductOfferTypeId( 233L );

        Mockito.when( productOfferingService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferingDTO );

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName( "STOCK_HANDSET" );
        Mockito.when( productOfferTypeService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferTypeDTO );

        List<OptionSetValueDTO> lstConfigStockCard = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO1 = new OptionSetValueDTO();
        optionSetValueDTO1.setValue( "1" );
        lstConfigStockCard.add( optionSetValueDTO1 );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigStockCard );

        Mockito.doNothing().when( stockCardService ).doSaveStockCard( Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any() );
        List<StockTransSerialDTO> lstStockTransSerial = Lists.newArrayList();
        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        stockTransSerialDTO.setStateId( 1L );
        stockTransSerialDTO.setFromSerial( "1" );
        stockTransSerialDTO.setToSerial( "1" );
        lstStockTransSerial.add( stockTransSerialDTO );
        StockTotalDTO stockTotalDTOExport = new StockTotalDTO();
        stockTotalDTOExport.setAvailableQuantity( 1L );

        BaseStockService spyTemp = PowerMockito.spy( baseStockService );
        Mockito.when( stockTransSerialService.findByStockTransDetailId( Mockito.anyLong() ) ).thenReturn( lstStockTransSerial );
        PowerMockito.doReturn( 2L ).when( spyTemp, "updateSerialPairCollection", any(), any() );

        spyTemp.doSaveStockGoods( stockTransDTO, stockTransDetailDTOList, flagStockDTO );
    }

    @Test
    public void testDoSaveStockGoods_21() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );
        stockTransDTO.setExchangeStockId( null );
        stockTransDTO.setToOwnerType( null );
        stockTransDTO.setToOwnerId( null );
        stockTransDTO.setRegionStockId( 1L );
        stockTransDTO.setStatus( "3" );
        stockTransDTO.setPayStatus( "5" );
        stockTransDTO.setBankplusStatus( 0L );

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1L );
        stockTransDetailDTO.setTableName( null );
        stockTransDetailDTO.setQuantity( 2L );

        stockTransDetailDTO.setCheckSerial( 2L );
        stockTransDetailDTOList.add( stockTransDetailDTO );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setOldStatus( 1L );
        flagStockDTO.setNewStatus( 1L );
        flagStockDTO.setUpdateOwnerId( false );
        flagStockDTO.setStatusForAgent( "3" );
        flagStockDTO.setShopId( 7283L );
        flagStockDTO.setObjectType( "AGENT_EXPORT" );
        flagStockDTO.setUpdateSaleDate( true );
        flagStockDTO.setGpon( true );
        flagStockDTO.setUpdateAccountBalance( true );
        flagStockDTO.setUpdateDepositPrice( true );
        flagStockDTO.setStateIdForReasonId( 1L );
        flagStockDTO.setUpdateStateDemo( true );
        flagStockDTO.setUpdateProdOfferId( true );

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );

        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        lstConfigEnableBccs1.add( optionSetValueDTO );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigEnableBccs1 );

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setAmount( 1L );
        Mockito.when( productOfferingService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferingDTO );

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName( "STOCK_HANDSET" );
        Mockito.when( productOfferTypeService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferTypeDTO );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( null );

        Mockito.doNothing().when( stockCardService ).doSaveStockCard( Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any() );
        List<StockTransSerialDTO> lstStockTransSerial = Lists.newArrayList();
        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        stockTransSerialDTO.setStateId( 1L );
        stockTransSerialDTO.setFromSerial( "1" );
        stockTransSerialDTO.setToSerial( "1" );
        lstStockTransSerial.add( stockTransSerialDTO );
        StockTotalDTO stockTotalDTOExport = new StockTotalDTO();
        stockTotalDTOExport.setAvailableQuantity( 1L );

        BaseStockService spyTemp = PowerMockito.spy( baseStockService );
        Mockito.when( stockTransSerialService.findByStockTransDetailId( Mockito.anyLong() ) ).thenReturn( lstStockTransSerial );
        PowerMockito.doReturn( 2L ).when( spyTemp, "updateSerialPairCollection", any(), any() );

        spyTemp.doSaveStockGoods( stockTransDTO, stockTransDetailDTOList, flagStockDTO );
    }

    @Test
    public void testDoSaveStockGoods_22() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );
        stockTransDTO.setExchangeStockId( null );
        stockTransDTO.setToOwnerType( null );
        stockTransDTO.setToOwnerId( null );
        stockTransDTO.setRegionStockId( 1L );
        stockTransDTO.setStatus( "3" );
        stockTransDTO.setPayStatus( "5" );
        stockTransDTO.setBankplusStatus( 5L );

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1L );
        stockTransDetailDTO.setTableName( null );
        stockTransDetailDTO.setQuantity( 2L );

        //TODO cho vào tạm thời khi nào xử lý được updateSerialPairCollection.
        stockTransDetailDTO.setCheckSerial( 2L );
        stockTransDetailDTOList.add( stockTransDetailDTO );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setOldStatus( 1L );
        flagStockDTO.setNewStatus( 1L );
        flagStockDTO.setUpdateOwnerId( false );
        flagStockDTO.setStatusForAgent( "3" );
        flagStockDTO.setShopId( 7283L );
        flagStockDTO.setObjectType( "AGENT_EXPORT" );
        flagStockDTO.setUpdateSaleDate( true );
        flagStockDTO.setGpon( true );
        flagStockDTO.setUpdateAccountBalance( true );
        flagStockDTO.setUpdateDepositPrice( false );
        flagStockDTO.setStateIdForReasonId( 0L );
        flagStockDTO.setUpdateStateDemo( false );
        flagStockDTO.setUpdateProdOfferId( false );

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );

        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        lstConfigEnableBccs1.add( optionSetValueDTO );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigEnableBccs1 );

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setAmount( 1L );
        Mockito.when( productOfferingService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferingDTO );

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName( "STOCK_HANDSET" );
        Mockito.when( productOfferTypeService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferTypeDTO );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( null );

        Mockito.doNothing().when( stockCardService ).doSaveStockCard( Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any() );
        List<StockTransSerialDTO> lstStockTransSerial = Lists.newArrayList();
        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        stockTransSerialDTO.setStateId( 1L );
        stockTransSerialDTO.setFromSerial( "1" );
        stockTransSerialDTO.setToSerial( "1" );
        lstStockTransSerial.add( stockTransSerialDTO );
        StockTotalDTO stockTotalDTOExport = new StockTotalDTO();
        stockTotalDTOExport.setAvailableQuantity( 1L );

        BaseStockService spyTemp = PowerMockito.spy( baseStockService );
        Mockito.when( stockTransSerialService.findByStockTransDetailId( Mockito.anyLong() ) ).thenReturn( lstStockTransSerial );
        PowerMockito.doReturn( 2L ).when( spyTemp, "updateSerialPairCollection", any(), any() );

        spyTemp.doSaveStockGoods( stockTransDTO, stockTransDetailDTOList, flagStockDTO );
    }

    @Test
    public void testDoSaveStockGoods_23() throws Exception {
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );
        stockTransDTO.setExchangeStockId( 2L );
        stockTransDTO.setToOwnerType( 2L );
        stockTransDTO.setToOwnerId( 2L );
        stockTransDTO.setRegionStockId( 1L );
        stockTransDTO.setStatus( "6" );
        stockTransDTO.setPayStatus( "5" );
        stockTransDTO.setBankplusStatus( 0L );

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 7L );
        stockTransDetailDTO.setTableName( null );
        stockTransDetailDTO.setQuantity( 2L );

        stockTransDetailDTO.setCheckSerial( 2L );
        stockTransDetailDTOList.add( stockTransDetailDTO );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setOldStatus( 1L );
        flagStockDTO.setNewStatus( 1L );
        flagStockDTO.setUpdateOwnerId( true );
        flagStockDTO.setStatusForAgent( "6" );
        flagStockDTO.setShopId( 7282L );
        flagStockDTO.setObjectType( "AGENT_EXPORT" );
        flagStockDTO.setUpdateSaleDate( true );
        flagStockDTO.setGpon( true );
        flagStockDTO.setUpdateAccountBalance( false );
        flagStockDTO.setUpdateDepositPrice( true );
        flagStockDTO.setStateIdForReasonId( 1L );
        flagStockDTO.setUpdateStateDemo( true );
        flagStockDTO.setUpdateProdOfferId( true );

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );

        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        lstConfigEnableBccs1.add( optionSetValueDTO );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigEnableBccs1 );

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setProductOfferTypeId( 4L );

        Mockito.when( productOfferingService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferingDTO );

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName( "STOCK_HANDSET" );
        Mockito.when( productOfferTypeService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferTypeDTO );

        List<OptionSetValueDTO> lstConfigStockCard = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO1 = new OptionSetValueDTO();
        optionSetValueDTO1.setValue( "1" );
        lstConfigStockCard.add( optionSetValueDTO1 );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigStockCard );

        Mockito.doNothing().when( stockCardService ).doSaveStockCard( Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any() );
        List<StockTransSerialDTO> lstStockTransSerial = Lists.newArrayList();
        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        stockTransSerialDTO.setStateId( 1L );
        stockTransSerialDTO.setFromSerial( "1" );
        stockTransSerialDTO.setToSerial( "1" );
        lstStockTransSerial.add( stockTransSerialDTO );
        StockTotalDTO stockTotalDTOExport = new StockTotalDTO();
        stockTotalDTOExport.setAvailableQuantity( 1L );

        BaseStockService spyTemp = PowerMockito.spy( baseStockService );
        Mockito.when( stockTransSerialService.findByStockTransDetailId( Mockito.anyLong() ) ).thenReturn( lstStockTransSerial );
        PowerMockito.doReturn( 2L ).when( spyTemp, "updateSerialPairCollection", any(), any() );

        spyTemp.doSaveStockGoods( stockTransDTO, stockTransDetailDTOList, flagStockDTO );
    }

    @Test
    public void testDoSaveStockGoods_24() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );
        stockTransDTO.setExchangeStockId( 2L );
        stockTransDTO.setToOwnerType( 2L );
        stockTransDTO.setToOwnerId( 2L );
        stockTransDTO.setRegionStockId( 1L );
        stockTransDTO.setStatus( "6" );
        stockTransDTO.setPayStatus( "5" );
        stockTransDTO.setBankplusStatus( 0L );

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1L );
        stockTransDetailDTO.setTableName( null );
        stockTransDetailDTO.setQuantity( 2L );

        stockTransDetailDTO.setCheckSerial( 2L );
        stockTransDetailDTOList.add( stockTransDetailDTO );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setOldStatus( 1L );
        flagStockDTO.setNewStatus( 1L );
        flagStockDTO.setUpdateOwnerId( true );
        flagStockDTO.setStatusForAgent( "6" );
        flagStockDTO.setShopId( 7282L );
        flagStockDTO.setObjectType( "AGENT_EXPORT" );
        flagStockDTO.setUpdateSaleDate( true );
        flagStockDTO.setGpon( true );
        flagStockDTO.setUpdateAccountBalance( false );
        flagStockDTO.setUpdateDepositPrice( true );
        flagStockDTO.setStateIdForReasonId( 1L );
        flagStockDTO.setUpdateStateDemo( true );
        flagStockDTO.setUpdateProdOfferId( true );
        flagStockDTO.setUpdateDamageProduct( true );

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );

        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        lstConfigEnableBccs1.add( optionSetValueDTO );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigEnableBccs1 );

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setProductOfferTypeId( 4L );

        Mockito.when( productOfferingService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferingDTO );

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName( "STOCK_ACCESSORIES" );
        Mockito.when( productOfferTypeService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferTypeDTO );

        List<OptionSetValueDTO> lstConfigStockCard = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO1 = new OptionSetValueDTO();
        optionSetValueDTO1.setValue( "1" );
        lstConfigStockCard.add( optionSetValueDTO1 );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigStockCard );

        Mockito.doNothing().when( stockCardService ).doSaveStockCard( Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any() );
        List<StockTransSerialDTO> lstStockTransSerial = Lists.newArrayList();
        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        stockTransSerialDTO.setStateId( 1L );
        stockTransSerialDTO.setFromSerial( "1" );
        stockTransSerialDTO.setToSerial( "1" );
        lstStockTransSerial.add( stockTransSerialDTO );
        StockTotalDTO stockTotalDTOExport = new StockTotalDTO();
        stockTotalDTOExport.setAvailableQuantity( 1L );

        BaseStockService spyTemp = PowerMockito.spy( baseStockService );
        Mockito.when( stockTransSerialService.findByStockTransDetailId( Mockito.anyLong() ) ).thenReturn( lstStockTransSerial );
        PowerMockito.doReturn( 2L ).when( spyTemp, "updateSerialPairCollection", any(), any() );

        spyTemp.doSaveStockGoods( stockTransDTO, stockTransDetailDTOList, flagStockDTO );
    }

    @Test
    public void testDoSaveStockGoods_25() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );
        stockTransDTO.setExchangeStockId( 2L );
        stockTransDTO.setToOwnerType( 2L );
        stockTransDTO.setToOwnerId( 2L );
        stockTransDTO.setRegionStockId( 1L );
        stockTransDTO.setStatus( "6" );
        stockTransDTO.setPayStatus( "5" );
        stockTransDTO.setBankplusStatus( 0L );

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1L );
        stockTransDetailDTO.setTableName( null );
        stockTransDetailDTO.setQuantity( 2L );

        stockTransDetailDTO.setCheckSerial( 2L );
        stockTransDetailDTOList.add( stockTransDetailDTO );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setOldStatus( 1L );
        flagStockDTO.setNewStatus( 1L );
        flagStockDTO.setUpdateOwnerId( true );
        flagStockDTO.setStatusForAgent( "6" );
        flagStockDTO.setShopId( 7282L );
        flagStockDTO.setObjectType( "AGENT_EXPORT" );
        flagStockDTO.setUpdateSaleDate( true );
        flagStockDTO.setGpon( true );
        flagStockDTO.setUpdateAccountBalance( false );
        flagStockDTO.setUpdateDepositPrice( true );
        flagStockDTO.setStateIdForReasonId( 1L );
        flagStockDTO.setUpdateStateDemo( true );
        flagStockDTO.setUpdateProdOfferId( true );
        flagStockDTO.setUpdateDamageProduct( true );

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );

        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        lstConfigEnableBccs1.add( optionSetValueDTO );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigEnableBccs1 );

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setProductOfferTypeId( 6L );

        Mockito.when( productOfferingService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferingDTO );

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName( "STOCK_CARD" );
        Mockito.when( productOfferTypeService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferTypeDTO );

        List<OptionSetValueDTO> lstConfigStockCard = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO1 = new OptionSetValueDTO();
        optionSetValueDTO1.setValue( "1" );
        lstConfigStockCard.add( optionSetValueDTO1 );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigStockCard );

        Mockito.doNothing().when( stockCardService ).doSaveStockCard( Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any() );
        List<StockTransSerialDTO> lstStockTransSerial = Lists.newArrayList();
        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        stockTransSerialDTO.setStateId( 1L );
        stockTransSerialDTO.setFromSerial( "1" );
        stockTransSerialDTO.setToSerial( "1" );
        lstStockTransSerial.add( stockTransSerialDTO );
        StockTotalDTO stockTotalDTOExport = new StockTotalDTO();
        stockTotalDTOExport.setAvailableQuantity( 1L );

        BaseStockService spyTemp = PowerMockito.spy( baseStockService );
        Mockito.when( stockTransSerialService.findByStockTransDetailId( Mockito.anyLong() ) ).thenReturn( lstStockTransSerial );
        PowerMockito.doReturn( 2L ).when( spyTemp, "updateSerialPairCollection", any(), any() );

        spyTemp.doSaveStockGoods( stockTransDTO, stockTransDetailDTOList, flagStockDTO );
    }

    @Test
    public void testDoSaveStockGoods_26() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );
        stockTransDTO.setExchangeStockId( 2L );
        stockTransDTO.setToOwnerType( 2L );
        stockTransDTO.setToOwnerId( 2L );
        stockTransDTO.setRegionStockId( 1L );
        stockTransDTO.setStatus( "6" );
        stockTransDTO.setPayStatus( "5" );
        stockTransDTO.setBankplusStatus( 0L );

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1L );
        stockTransDetailDTO.setTableName( null );
        stockTransDetailDTO.setQuantity( 2L );

        stockTransDetailDTO.setCheckSerial( 2L );
        stockTransDetailDTOList.add( stockTransDetailDTO );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setOldStatus( 1L );
        flagStockDTO.setNewStatus( 1L );
        flagStockDTO.setUpdateOwnerId( true );
        flagStockDTO.setStatusForAgent( "6" );
        flagStockDTO.setShopId( 7282L );
        flagStockDTO.setObjectType( "AGENT_EXPORT" );
        flagStockDTO.setUpdateSaleDate( true );
        flagStockDTO.setGpon( true );
        flagStockDTO.setUpdateAccountBalance( false );
        flagStockDTO.setUpdateDepositPrice( true );
        flagStockDTO.setStateIdForReasonId( 1L );
        flagStockDTO.setUpdateStateDemo( true );
        flagStockDTO.setUpdateProdOfferId( true );
        flagStockDTO.setUpdateDamageProduct( true );

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );

        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        lstConfigEnableBccs1.add( optionSetValueDTO );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigEnableBccs1 );

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setProductOfferTypeId( 6L );

        Mockito.when( productOfferingService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferingDTO );

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName( "STOCK_KIT" );
        Mockito.when( productOfferTypeService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferTypeDTO );

        List<OptionSetValueDTO> lstConfigStockCard = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO1 = new OptionSetValueDTO();
        optionSetValueDTO1.setValue( "1" );
        lstConfigStockCard.add( optionSetValueDTO1 );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigStockCard );

        Mockito.doNothing().when( stockCardService ).doSaveStockCard( Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any() );
        List<StockTransSerialDTO> lstStockTransSerial = Lists.newArrayList();
        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        stockTransSerialDTO.setStateId( 1L );
        stockTransSerialDTO.setFromSerial( "1" );
        stockTransSerialDTO.setToSerial( "1" );
        lstStockTransSerial.add( stockTransSerialDTO );
        StockTotalDTO stockTotalDTOExport = new StockTotalDTO();
        stockTotalDTOExport.setAvailableQuantity( 1L );

        BaseStockService spyTemp = PowerMockito.spy( baseStockService );
        Mockito.when( stockTransSerialService.findByStockTransDetailId( Mockito.anyLong() ) ).thenReturn( lstStockTransSerial );
        PowerMockito.doReturn( 2L ).when( spyTemp, "updateSerialPairCollection", any(), any() );

        spyTemp.doSaveStockGoods( stockTransDTO, stockTransDetailDTOList, flagStockDTO );
    }

    @Test
    public void testDoSaveStockGoods_27() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );
        stockTransDTO.setExchangeStockId( 2L );
        stockTransDTO.setToOwnerType( 2L );
        stockTransDTO.setToOwnerId( 2L );
        stockTransDTO.setRegionStockId( 1L );
        stockTransDTO.setStatus( "6" );
        stockTransDTO.setPayStatus( "5" );
        stockTransDTO.setBankplusStatus( 0L );

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1L );
        stockTransDetailDTO.setTableName( null );
        stockTransDetailDTO.setQuantity( 2L );

        stockTransDetailDTO.setCheckSerial( 2L );
        stockTransDetailDTOList.add( stockTransDetailDTO );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setOldStatus( 1L );
        flagStockDTO.setNewStatus( 1L );
        flagStockDTO.setUpdateOwnerId( true );
        flagStockDTO.setStatusForAgent( "6" );
        flagStockDTO.setShopId( 7282L );
        flagStockDTO.setObjectType( "AGENT_EXPORT" );
        flagStockDTO.setUpdateSaleDate( true );
        flagStockDTO.setGpon( true );
        flagStockDTO.setUpdateAccountBalance( false );
        flagStockDTO.setUpdateDepositPrice( true );
        flagStockDTO.setStateIdForReasonId( 1L );
        flagStockDTO.setUpdateStateDemo( true );
        flagStockDTO.setUpdateProdOfferId( true );
        flagStockDTO.setUpdateDamageProduct( true );

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );

        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        lstConfigEnableBccs1.add( optionSetValueDTO );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigEnableBccs1 );

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setProductOfferTypeId( 6L );

        Mockito.when( productOfferingService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferingDTO );

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName( "STOCK_SIM" );
        Mockito.when( productOfferTypeService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferTypeDTO );

        List<OptionSetValueDTO> lstConfigStockCard = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO1 = new OptionSetValueDTO();
        optionSetValueDTO1.setValue( "1" );
        lstConfigStockCard.add( optionSetValueDTO1 );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigStockCard );

        Mockito.doNothing().when( stockCardService ).doSaveStockCard( Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any() );
        List<StockTransSerialDTO> lstStockTransSerial = Lists.newArrayList();
        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        stockTransSerialDTO.setStateId( 1L );
        stockTransSerialDTO.setFromSerial( "1" );
        stockTransSerialDTO.setToSerial( "1" );
        lstStockTransSerial.add( stockTransSerialDTO );
        StockTotalDTO stockTotalDTOExport = new StockTotalDTO();
        stockTotalDTOExport.setAvailableQuantity( 1L );

        BaseStockService spyTemp = PowerMockito.spy( baseStockService );
        Mockito.when( stockTransSerialService.findByStockTransDetailId( Mockito.anyLong() ) ).thenReturn( lstStockTransSerial );
        PowerMockito.doReturn( 2L ).when( spyTemp, "updateSerialPairCollection", any(), any() );

        spyTemp.doSaveStockGoods( stockTransDTO, stockTransDetailDTOList, flagStockDTO );
    }

    @Test
    public void testDoSaveStockGoods_28() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline("2");
        stockTransDTO.setExchangeStockId(2L);
        stockTransDTO.setToOwnerType(2L);
        stockTransDTO.setToOwnerId(2L);
        stockTransDTO.setRegionStockId(1L);
        stockTransDTO.setStatus("6");
        stockTransDTO.setPayStatus("5");
        stockTransDTO.setBankplusStatus(0L);

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId(1L);
        stockTransDetailDTO.setTableName(null);
        stockTransDetailDTO.setQuantity(2L);

        stockTransDetailDTO.setCheckSerial(2L);
        stockTransDetailDTOList.add(stockTransDetailDTO);

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setOldStatus(1L);
        flagStockDTO.setNewStatus(1L);
        flagStockDTO.setUpdateOwnerId(true);
        flagStockDTO.setStatusForAgent("6");
        flagStockDTO.setShopId(7282L);
        flagStockDTO.setObjectType("AGENT_EXPORT");
        flagStockDTO.setUpdateSaleDate(true);
        flagStockDTO.setGpon(true);
        flagStockDTO.setUpdateAccountBalance(false);
        flagStockDTO.setUpdateDepositPrice(true);
        flagStockDTO.setStateIdForReasonId(1L);
        flagStockDTO.setUpdateStateDemo(true);
        flagStockDTO.setUpdateProdOfferId(true);
        flagStockDTO.setUpdateDamageProduct(true);

        PowerMockito.mockStatic(DbUtil.class);
        PowerMockito.when(DbUtil.getSysDate( Mockito.any(EntityManager.class))).thenReturn( new Date());

        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue("1");
        lstConfigEnableBccs1.add(optionSetValueDTO);

        Mockito.when(optionSetValueService.getByOptionSetCode(Mockito.anyString())).thenReturn(lstConfigEnableBccs1);

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setProductOfferTypeId(6L);

        Mockito.when(productOfferingService.findOne(Mockito.anyLong())).thenReturn(productOfferingDTO);

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName("STOCK_ACCESSORIES");
        Mockito.when(productOfferTypeService.findOne(Mockito.anyLong())).thenReturn(productOfferTypeDTO);

        List<OptionSetValueDTO> lstConfigStockCard = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO1 = new OptionSetValueDTO();
        optionSetValueDTO1.setValue("1");
        lstConfigStockCard.add(optionSetValueDTO1);

        Mockito.when(optionSetValueService.getByOptionSetCode(Mockito.anyString())).thenReturn(lstConfigStockCard);

        Mockito.doNothing().when(stockCardService).doSaveStockCard( Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
        List<StockTransSerialDTO> lstStockTransSerial = Lists.newArrayList();
        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        stockTransSerialDTO.setStateId(1L);
        stockTransSerialDTO.setFromSerial("1");
        stockTransSerialDTO.setToSerial("1");
        lstStockTransSerial.add(stockTransSerialDTO);
        StockTotalDTO stockTotalDTOExport = new StockTotalDTO();
        stockTotalDTOExport.setAvailableQuantity(1L);

        BaseStockService spyTemp = PowerMockito.spy(baseStockService);
        Mockito.when(stockTransSerialService.findByStockTransDetailId(Mockito.anyLong())).thenReturn(lstStockTransSerial);
        PowerMockito.doReturn(2L).when(spyTemp, "updateSerialPairCollection", any(), any());

        spyTemp.doSaveStockGoods(stockTransDTO, stockTransDetailDTOList, flagStockDTO);
    }

    @Test
    public void testDoSaveStockGoods_29() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT );

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );
        stockTransDTO.setExchangeStockId( 2L );
        stockTransDTO.setToOwnerType(null);
        stockTransDTO.setToOwnerId( 2L );
        stockTransDTO.setRegionStockId( 1L );
        stockTransDTO.setStatus( "5" );
        stockTransDTO.setPayStatus( "5" );
        stockTransDTO.setBankplusStatus( 0L );

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1L );
        stockTransDetailDTO.setTableName( "Name" );
        stockTransDetailDTO.setQuantity( 3L );

        stockTransDetailDTO.setCheckSerial( 2L );
        stockTransDetailDTOList.add( stockTransDetailDTO );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setOldStatus( 1L );
        flagStockDTO.setNewStatus( 1L );
        flagStockDTO.setUpdateOwnerId( true );
        flagStockDTO.setStatusForAgent( "6" );
        flagStockDTO.setShopId( 7282L );
        flagStockDTO.setObjectType( "AGENT_EXPORT" );
        flagStockDTO.setUpdateSaleDate( true );
        flagStockDTO.setGpon( true );
        flagStockDTO.setUpdateAccountBalance( false );
        flagStockDTO.setUpdateDepositPrice( true );
        flagStockDTO.setStateIdForReasonId( 1L );
        flagStockDTO.setUpdateStateDemo( true );
        flagStockDTO.setUpdateProdOfferId( true );

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );

        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        lstConfigEnableBccs1.add( optionSetValueDTO );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigEnableBccs1 );

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setProductOfferTypeId( 7L );

        Mockito.when( productOfferingService.findOne( Mockito.anyLong() ) ).thenReturn( null );

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName( "STOCK_HANDSET" );
        Mockito.when( productOfferTypeService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferTypeDTO );

        List<OptionSetValueDTO> lstConfigStockCard = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO1 = new OptionSetValueDTO();
        optionSetValueDTO1.setValue( "1" );
        lstConfigStockCard.add( optionSetValueDTO1 );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigStockCard );

        Mockito.doNothing().when( stockCardService ).doSaveStockCard( Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any() );
        List<StockTransSerialDTO> lstStockTransSerial = Lists.newArrayList();
        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        stockTransSerialDTO.setStateId( 1L );
        stockTransSerialDTO.setFromSerial( "1" );
        stockTransSerialDTO.setToSerial( "1" );
        lstStockTransSerial.add( stockTransSerialDTO );
        StockTotalDTO stockTotalDTOExport = new StockTotalDTO();
        stockTotalDTOExport.setAvailableQuantity( 1L );

        BaseStockService spyTemp = PowerMockito.spy( baseStockService );
        Mockito.when( stockTransSerialService.findByStockTransDetailId( Mockito.anyLong() ) ).thenReturn( lstStockTransSerial );
        PowerMockito.doReturn( 2L ).when( spyTemp, "updateSerialPairCollection", any(), any() );

        spyTemp.doSaveStockGoods(stockTransDTO, stockTransDetailDTOList, flagStockDTO );
    }

    @Test
    public void testDoSaveStockGoods_30() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT );

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );
        stockTransDTO.setExchangeStockId( 2L );
        stockTransDTO.setToOwnerType(2L);
        stockTransDTO.setToOwnerId(null);
        stockTransDTO.setRegionStockId( 1L );
        stockTransDTO.setStatus("3");
        stockTransDTO.setPayStatus( "5" );
        stockTransDTO.setBankplusStatus( 0L );

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1L );
        stockTransDetailDTO.setTableName( "Name" );
        stockTransDetailDTO.setQuantity( 3L );

        stockTransDetailDTO.setCheckSerial( 2L );
        stockTransDetailDTOList.add( stockTransDetailDTO );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setOldStatus( 1L );
        flagStockDTO.setNewStatus( 1L );
        flagStockDTO.setUpdateOwnerId( true );
        flagStockDTO.setStatusForAgent("3");
        flagStockDTO.setShopId( 7282L );
        flagStockDTO.setObjectType( "AGENT_EXPORT" );
        flagStockDTO.setUpdateSaleDate( true );
        flagStockDTO.setGpon( true );
        flagStockDTO.setUpdateAccountBalance( false );
        flagStockDTO.setUpdateDepositPrice( true );
        flagStockDTO.setStateIdForReasonId( 1L );
        flagStockDTO.setUpdateStateDemo( true );
        flagStockDTO.setUpdateProdOfferId( true );

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );

        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        lstConfigEnableBccs1.add( optionSetValueDTO );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigEnableBccs1 );

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setProductOfferTypeId( 7L );

        Mockito.when( productOfferingService.findOne( Mockito.anyLong() ) ).thenReturn( null );

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName( "STOCK_HANDSET" );
        Mockito.when( productOfferTypeService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferTypeDTO );

        List<OptionSetValueDTO> lstConfigStockCard = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO1 = new OptionSetValueDTO();
        optionSetValueDTO1.setValue( "1" );
        lstConfigStockCard.add( optionSetValueDTO1 );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigStockCard );

        Mockito.doNothing().when( stockCardService ).doSaveStockCard( Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any() );
        List<StockTransSerialDTO> lstStockTransSerial = Lists.newArrayList();
        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        stockTransSerialDTO.setStateId( 1L );
        stockTransSerialDTO.setFromSerial( "1" );
        stockTransSerialDTO.setToSerial( "1" );
        lstStockTransSerial.add( stockTransSerialDTO );
        StockTotalDTO stockTotalDTOExport = new StockTotalDTO();
        stockTotalDTOExport.setAvailableQuantity( 1L );

        BaseStockService spyTemp = PowerMockito.spy( baseStockService );
        Mockito.when( stockTransSerialService.findByStockTransDetailId( Mockito.anyLong() ) ).thenReturn( lstStockTransSerial );
        PowerMockito.doReturn( 2L ).when( spyTemp, "updateSerialPairCollection", any(), any() );

        spyTemp.doSaveStockGoods(stockTransDTO, stockTransDetailDTOList, flagStockDTO );
    }

    @Test
    public void testDoSaveStockGoods_31() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT );

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );
        stockTransDTO.setExchangeStockId( 2L );
        stockTransDTO.setToOwnerType(null);
        stockTransDTO.setToOwnerId(null);
        stockTransDTO.setRegionStockId( null );
        stockTransDTO.setStatus("3");
        stockTransDTO.setPayStatus( "5" );
        stockTransDTO.setBankplusStatus( 0L );

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1L );
        stockTransDetailDTO.setTableName( "Name" );
        stockTransDetailDTO.setQuantity( 3L );

        stockTransDetailDTO.setCheckSerial( 2L );
        stockTransDetailDTOList.add( stockTransDetailDTO );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setOldStatus( 1L );
        flagStockDTO.setNewStatus( 1L );
        flagStockDTO.setUpdateOwnerId( false );
        flagStockDTO.setStatusForAgent("4");
        flagStockDTO.setShopId( 7282L );
        flagStockDTO.setObjectType( "AGENT_EXPORT" );
        flagStockDTO.setUpdateSaleDate( true );
        flagStockDTO.setGpon( true );
        flagStockDTO.setUpdateAccountBalance( false );
        flagStockDTO.setUpdateDepositPrice( true );
        flagStockDTO.setStateIdForReasonId( 1L );
        flagStockDTO.setUpdateStateDemo( true );
        flagStockDTO.setUpdateProdOfferId( true );

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );

        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        lstConfigEnableBccs1.add( optionSetValueDTO );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigEnableBccs1 );

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setProductOfferTypeId( 7L );

        Mockito.when( productOfferingService.findOne( Mockito.anyLong() ) ).thenReturn( null );

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName( "STOCK_HANDSET" );
        Mockito.when( productOfferTypeService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferTypeDTO );

        List<OptionSetValueDTO> lstConfigStockCard = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO1 = new OptionSetValueDTO();
        optionSetValueDTO1.setValue( "1" );
        lstConfigStockCard.add( optionSetValueDTO1 );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigStockCard );

        Mockito.doNothing().when( stockCardService ).doSaveStockCard( Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any() );
        List<StockTransSerialDTO> lstStockTransSerial = Lists.newArrayList();
        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        stockTransSerialDTO.setStateId( 1L );
        stockTransSerialDTO.setFromSerial( "1" );
        stockTransSerialDTO.setToSerial( "1" );
        lstStockTransSerial.add( stockTransSerialDTO );
        StockTotalDTO stockTotalDTOExport = new StockTotalDTO();
        stockTotalDTOExport.setAvailableQuantity( 1L );

        BaseStockService spyTemp = PowerMockito.spy( baseStockService );
        Mockito.when( stockTransSerialService.findByStockTransDetailId( Mockito.anyLong() ) ).thenReturn( lstStockTransSerial );
        PowerMockito.doReturn( 2L ).when( spyTemp, "updateSerialPairCollection", any(), any() );

        spyTemp.doSaveStockGoods(stockTransDTO, stockTransDetailDTOList, flagStockDTO );
    }

    @Test
    public void testDoSaveStockGoods_32() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT );

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );
        stockTransDTO.setExchangeStockId( 2L );
        stockTransDTO.setToOwnerType(2L);
        stockTransDTO.setToOwnerId(2L);
        stockTransDTO.setRegionStockId( null );
        stockTransDTO.setStatus("3");
        stockTransDTO.setPayStatus( "5" );
        stockTransDTO.setBankplusStatus( 0L );

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1L );
        stockTransDetailDTO.setTableName( "Name" );
        stockTransDetailDTO.setQuantity( 3L );

        stockTransDetailDTO.setCheckSerial( 2L );
        stockTransDetailDTOList.add( stockTransDetailDTO );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setOldStatus( 1L );
        flagStockDTO.setNewStatus( 1L );
        flagStockDTO.setUpdateOwnerId( false );
        flagStockDTO.setStatusForAgent("4");
        flagStockDTO.setShopId( 7282L );
        flagStockDTO.setObjectType( "AGENT_EXPORT" );
        flagStockDTO.setUpdateSaleDate( true );
        flagStockDTO.setGpon( true );
        flagStockDTO.setUpdateAccountBalance( false );
        flagStockDTO.setUpdateDepositPrice( true );
        flagStockDTO.setStateIdForReasonId( 1L );
        flagStockDTO.setUpdateStateDemo( true );
        flagStockDTO.setUpdateProdOfferId( true );

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );

        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        lstConfigEnableBccs1.add( optionSetValueDTO );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigEnableBccs1 );

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setProductOfferTypeId( 7L );

        Mockito.when( productOfferingService.findOne( Mockito.anyLong() ) ).thenReturn( null );

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName( "STOCK_HANDSET" );
        Mockito.when( productOfferTypeService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferTypeDTO );

        List<OptionSetValueDTO> lstConfigStockCard = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO1 = new OptionSetValueDTO();
        optionSetValueDTO1.setValue( "1" );
        lstConfigStockCard.add( optionSetValueDTO1 );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigStockCard );

        Mockito.doNothing().when( stockCardService ).doSaveStockCard( Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any() );
        List<StockTransSerialDTO> lstStockTransSerial = Lists.newArrayList();
        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        stockTransSerialDTO.setStateId( 1L );
        stockTransSerialDTO.setFromSerial( "1" );
        stockTransSerialDTO.setToSerial( "1" );
        lstStockTransSerial.add( stockTransSerialDTO );
        StockTotalDTO stockTotalDTOExport = new StockTotalDTO();
        stockTotalDTOExport.setAvailableQuantity( 1L );

        BaseStockService spyTemp = PowerMockito.spy( baseStockService );
        Mockito.when( stockTransSerialService.findByStockTransDetailId( Mockito.anyLong() ) ).thenReturn( lstStockTransSerial );
        PowerMockito.doReturn( 2L ).when( spyTemp, "updateSerialPairCollection", any(), any() );

        spyTemp.doSaveStockGoods(stockTransDTO, stockTransDetailDTOList, flagStockDTO );
    }

    @Test
    public void testDoSaveStockGoods_33() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );
        stockTransDTO.setExchangeStockId( 2L );
        stockTransDTO.setToOwnerType( 2L );
        stockTransDTO.setToOwnerId( 2L );
        stockTransDTO.setRegionStockId(2L);
        stockTransDTO.setStatus( "3" );
        stockTransDTO.setPayStatus( "5" );
        stockTransDTO.setBankplusStatus( 0L );

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1L );
        stockTransDetailDTO.setTableName( null );
        stockTransDetailDTO.setQuantity( 2L );

        stockTransDetailDTO.setCheckSerial( 2L );
        stockTransDetailDTOList.add( stockTransDetailDTO );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setOldStatus( 1L );
        flagStockDTO.setNewStatus( 1L );
        flagStockDTO.setUpdateOwnerId( true );
        flagStockDTO.setStatusForAgent( "4" );
        flagStockDTO.setShopId( 7283L );
        flagStockDTO.setObjectType( "AGENT_EXPORT" );
        flagStockDTO.setUpdateSaleDate( true );
        flagStockDTO.setGpon( true );
        flagStockDTO.setUpdateAccountBalance( false );
        flagStockDTO.setUpdateDepositPrice( true );
        flagStockDTO.setStateIdForReasonId( 1L );
        flagStockDTO.setUpdateStateDemo( true );
        flagStockDTO.setUpdateProdOfferId( true );
        flagStockDTO.setUpdateDamageProduct( true );

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );

        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        lstConfigEnableBccs1.add( optionSetValueDTO );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigEnableBccs1 );

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setProductOfferTypeId( 6L );

        Mockito.when( productOfferingService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferingDTO );

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName( "STOCK_SIM" );
        Mockito.when( productOfferTypeService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferTypeDTO );

        List<OptionSetValueDTO> lstConfigStockCard = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO1 = new OptionSetValueDTO();
        optionSetValueDTO1.setValue( "1" );
        lstConfigStockCard.add( optionSetValueDTO1 );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigStockCard );

        Mockito.doNothing().when( stockCardService ).doSaveStockCard( Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any() );
        List<StockTransSerialDTO> lstStockTransSerial = Lists.newArrayList();
        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        stockTransSerialDTO.setStateId( 1L );
        stockTransSerialDTO.setFromSerial( "1" );
        stockTransSerialDTO.setToSerial( "1" );
        lstStockTransSerial.add( stockTransSerialDTO );
        StockTotalDTO stockTotalDTOExport = new StockTotalDTO();
        stockTotalDTOExport.setAvailableQuantity( 1L );

        BaseStockService spyTemp = PowerMockito.spy( baseStockService );
        Mockito.when( stockTransSerialService.findByStockTransDetailId( Mockito.anyLong() ) ).thenReturn( lstStockTransSerial );
        PowerMockito.doReturn( 2L ).when( spyTemp, "updateSerialPairCollection", any(), any() );

        spyTemp.doSaveStockGoods( stockTransDTO, stockTransDetailDTOList, flagStockDTO );
    }

    @Test
    public void testDoSaveStockGoods_34() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );
        stockTransDTO.setExchangeStockId( 2L );
        stockTransDTO.setToOwnerType( 2L );
        stockTransDTO.setToOwnerId( 2L );
        stockTransDTO.setRegionStockId(2L);
        stockTransDTO.setStatus( "4" );
        stockTransDTO.setPayStatus( "5" );
        stockTransDTO.setBankplusStatus( 0L );

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1L );
        stockTransDetailDTO.setTableName( null );
        stockTransDetailDTO.setQuantity( 2L );

        stockTransDetailDTO.setCheckSerial( 2L );
        stockTransDetailDTOList.add( stockTransDetailDTO );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setOldStatus( 1L );
        flagStockDTO.setNewStatus( 1L );
        flagStockDTO.setUpdateOwnerId( true );
        flagStockDTO.setStatusForAgent( "3" );
        flagStockDTO.setShopId( 7282L );
        flagStockDTO.setObjectType( "AGENT_EXPORT" );
        flagStockDTO.setUpdateSaleDate( true );
        flagStockDTO.setGpon( true );
        flagStockDTO.setUpdateAccountBalance( false );
        flagStockDTO.setUpdateDepositPrice( true );
        flagStockDTO.setStateIdForReasonId( 1L );
        flagStockDTO.setUpdateStateDemo( true );
        flagStockDTO.setUpdateProdOfferId( true );
        flagStockDTO.setUpdateDamageProduct( true );

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );

        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        lstConfigEnableBccs1.add( optionSetValueDTO );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigEnableBccs1 );

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setProductOfferTypeId( 6L );

        Mockito.when( productOfferingService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferingDTO );
        Mockito.when( productOfferTypeService.findOne( Mockito.anyLong() ) ).thenReturn( null );

        List<OptionSetValueDTO> lstConfigStockCard = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO1 = new OptionSetValueDTO();
        optionSetValueDTO1.setValue( "1" );
        lstConfigStockCard.add( optionSetValueDTO1 );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigStockCard );

        Mockito.doNothing().when( stockCardService ).doSaveStockCard( Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any() );
        List<StockTransSerialDTO> lstStockTransSerial = Lists.newArrayList();
        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        stockTransSerialDTO.setStateId( 1L );
        stockTransSerialDTO.setFromSerial( "1" );
        stockTransSerialDTO.setToSerial( "1" );
        lstStockTransSerial.add( stockTransSerialDTO );
        StockTotalDTO stockTotalDTOExport = new StockTotalDTO();
        stockTotalDTOExport.setAvailableQuantity( 1L );

        BaseStockService spyTemp = PowerMockito.spy( baseStockService );
        Mockito.when( stockTransSerialService.findByStockTransDetailId( Mockito.anyLong() ) ).thenReturn( lstStockTransSerial );
        PowerMockito.doReturn( 2L ).when( spyTemp, "updateSerialPairCollection", any(), any() );

        spyTemp.doSaveStockGoods( stockTransDTO, stockTransDetailDTOList, flagStockDTO );
    }

    @Test
    public void testDoSaveStockGoods_35() throws Exception {
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );
        stockTransDTO.setExchangeStockId( 2L );
        stockTransDTO.setToOwnerType( 2L );
        stockTransDTO.setToOwnerId( 2L );
        stockTransDTO.setRegionStockId(2L);
        stockTransDTO.setStatus( "6" );
        stockTransDTO.setPayStatus( "5" );
        stockTransDTO.setBankplusStatus( 0L );

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1L );
        stockTransDetailDTO.setTableName("STOCK_KIT");
        stockTransDetailDTO.setQuantity( 2L );

        stockTransDetailDTO.setCheckSerial( 2L );
        stockTransDetailDTOList.add( stockTransDetailDTO );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setOldStatus( 1L );
        flagStockDTO.setNewStatus( 1L );
        flagStockDTO.setUpdateOwnerId( true );
        flagStockDTO.setStatusForAgent( "5" );
        flagStockDTO.setShopId( 7285L );
        flagStockDTO.setObjectType( "AGENT_EXPORT" );
        flagStockDTO.setUpdateSaleDate( true );
        flagStockDTO.setGpon( true );
        flagStockDTO.setUpdateAccountBalance( false );
        flagStockDTO.setUpdateDepositPrice( true );
        flagStockDTO.setStateIdForReasonId( 1L );
        flagStockDTO.setUpdateStateDemo( true );
        flagStockDTO.setUpdateProdOfferId( true );
        flagStockDTO.setUpdateDamageProduct( true );

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );

        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        lstConfigEnableBccs1.add( optionSetValueDTO );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigEnableBccs1 );

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setProductOfferTypeId( 6L );

        Mockito.when( productOfferingService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferingDTO );

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName( "STOCK_KIT" );
        Mockito.when( productOfferTypeService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferTypeDTO );

        List<OptionSetValueDTO> lstConfigStockCard = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO1 = new OptionSetValueDTO();
        optionSetValueDTO1.setValue( "1" );
        lstConfigStockCard.add( optionSetValueDTO1 );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigStockCard );

        Mockito.doNothing().when( stockCardService ).doSaveStockCard( Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any() );
        List<StockTransSerialDTO> lstStockTransSerial = Lists.newArrayList();
        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        stockTransSerialDTO.setStateId( 1L );
        stockTransSerialDTO.setFromSerial( "1" );
        stockTransSerialDTO.setToSerial( "1" );
        lstStockTransSerial.add( stockTransSerialDTO );
        StockTotalDTO stockTotalDTOExport = new StockTotalDTO();
        stockTotalDTOExport.setAvailableQuantity( 1L );

        BaseStockService spyTemp = PowerMockito.spy( baseStockService );
        Mockito.when( stockTransSerialService.findByStockTransDetailId( Mockito.anyLong() ) ).thenReturn( lstStockTransSerial );
        PowerMockito.doReturn( 2L ).when( spyTemp, "updateSerialPairCollection", any(), any() );

        spyTemp.doSaveStockGoods( stockTransDTO, stockTransDetailDTOList, flagStockDTO );
    }

    @Test
    public void testDoSaveStockGoods_36() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT );

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );
        stockTransDTO.setExchangeStockId( 2L );
        stockTransDTO.setToOwnerType(null);
        stockTransDTO.setToOwnerId( 2L );
        stockTransDTO.setRegionStockId( 1L );
        stockTransDTO.setStatus( "6" );
        stockTransDTO.setPayStatus( "1" );
        stockTransDTO.setBankplusStatus(1L);

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1L );
        stockTransDetailDTO.setTableName( null );
        stockTransDetailDTO.setCheckSerial( 1L );
        stockTransDetailDTOList.add( stockTransDetailDTO );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setOldStatus( 1L );
        flagStockDTO.setNewStatus( 1L );
        flagStockDTO.setUpdateOwnerId( true );
        flagStockDTO.setStatusForAgent( "6" );
        flagStockDTO.setShopId( 7282L );
        flagStockDTO.setObjectType(null);
        flagStockDTO.setSerialListFromTransDetail( true );
        flagStockDTO.setUpdateAccountBalance(false);

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );

        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        lstConfigEnableBccs1.add( optionSetValueDTO );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigEnableBccs1 );

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setProductOfferTypeId( 1L );

        Mockito.when( productOfferingService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferingDTO );

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName( "STOCK_NUMBER" );
        Mockito.when( productOfferTypeService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferTypeDTO );

        List<OptionSetValueDTO> lstConfigStockCard = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO1 = new OptionSetValueDTO();
        optionSetValueDTO1.setValue( "1" );
        lstConfigStockCard.add( optionSetValueDTO1 );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigStockCard );

        Mockito.doNothing().when( stockCardService ).doSaveStockCard( Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any() );

        baseStockService.doSaveStockGoods( stockTransDTO, stockTransDetailDTOList, flagStockDTO );
    }

    @Test
    public void testDoSaveStockGoods_37() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT );

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );
        stockTransDTO.setExchangeStockId( 2L );
        stockTransDTO.setToOwnerType(2L);
        stockTransDTO.setToOwnerId( null );
        stockTransDTO.setRegionStockId( 1L );
        stockTransDTO.setStatus( "6" );
        stockTransDTO.setPayStatus( "1" );
        stockTransDTO.setBankplusStatus(null);

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1L );
        stockTransDetailDTO.setTableName( null );
        stockTransDetailDTO.setCheckSerial( 1L );
        stockTransDetailDTOList.add( stockTransDetailDTO );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setOldStatus( 1L );
        flagStockDTO.setNewStatus( 1L );
        flagStockDTO.setUpdateOwnerId( true );
        flagStockDTO.setStatusForAgent( "6" );
        flagStockDTO.setShopId( 7282L );
        flagStockDTO.setObjectType("1");
        flagStockDTO.setSerialListFromTransDetail( true );
        flagStockDTO.setUpdateAccountBalance(true);

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );

        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        lstConfigEnableBccs1.add( optionSetValueDTO );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigEnableBccs1 );

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setProductOfferTypeId( 1L );

        Mockito.when( productOfferingService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferingDTO );

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName( "STOCK_NUMBER" );
        Mockito.when( productOfferTypeService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferTypeDTO );

        List<OptionSetValueDTO> lstConfigStockCard = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO1 = new OptionSetValueDTO();
        optionSetValueDTO1.setValue( "1" );
        lstConfigStockCard.add( optionSetValueDTO1 );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigStockCard );

        Mockito.doNothing().when( stockCardService ).doSaveStockCard( Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any() );

        baseStockService.doSaveStockGoods( stockTransDTO, stockTransDetailDTOList, flagStockDTO );
    }

    @Test
    public void testDoSaveStockGoods_38() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT );

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );
        stockTransDTO.setExchangeStockId( 2L );
        stockTransDTO.setToOwnerType(2L);
        stockTransDTO.setToOwnerId( 2L );
        stockTransDTO.setRegionStockId( 1L );
        stockTransDTO.setStatus( "6" );
        stockTransDTO.setPayStatus( null);
        stockTransDTO.setBankplusStatus(null);

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1L );
        stockTransDetailDTO.setTableName( null );
        stockTransDetailDTO.setCheckSerial( 1L );
        stockTransDetailDTOList.add( stockTransDetailDTO );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setOldStatus( 1L );
        flagStockDTO.setNewStatus( 1L );
        flagStockDTO.setUpdateOwnerId( false );
        flagStockDTO.setStatusForAgent( "6" );
        flagStockDTO.setShopId( 7282L );
        flagStockDTO.setObjectType("AGENT_EXPORT");
        flagStockDTO.setSerialListFromTransDetail( true );
        flagStockDTO.setUpdateAccountBalance(true);

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );

        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        lstConfigEnableBccs1.add( optionSetValueDTO );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigEnableBccs1 );

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setProductOfferTypeId( 1L );

        Mockito.when( productOfferingService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferingDTO );

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName( "STOCK_NUMBER" );
        Mockito.when( productOfferTypeService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferTypeDTO );

        List<OptionSetValueDTO> lstConfigStockCard = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO1 = new OptionSetValueDTO();
        optionSetValueDTO1.setValue( "1" );
        lstConfigStockCard.add( optionSetValueDTO1 );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigStockCard );

        Mockito.doNothing().when( stockCardService ).doSaveStockCard( Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any() );

        baseStockService.doSaveStockGoods( stockTransDTO, stockTransDetailDTOList, flagStockDTO );
    }

    @Test
    public void testDoSaveStockGoods_39() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT );

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );
        stockTransDTO.setExchangeStockId( 2L );
        stockTransDTO.setToOwnerType(2L);
        stockTransDTO.setToOwnerId( 2L );
        stockTransDTO.setRegionStockId( 1L );
        stockTransDTO.setStatus( "6" );
        stockTransDTO.setPayStatus( "2L");
        stockTransDTO.setBankplusStatus(null);

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1L );
        stockTransDetailDTO.setTableName( null );
        stockTransDetailDTO.setCheckSerial( 1L );
        stockTransDetailDTOList.add( stockTransDetailDTO );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setOldStatus( 1L );
        flagStockDTO.setNewStatus( 1L );
        flagStockDTO.setUpdateOwnerId( false );
        flagStockDTO.setStatusForAgent( "6" );
        flagStockDTO.setShopId( 7282L );
        flagStockDTO.setObjectType("AGENT_EXPORT");
        flagStockDTO.setSerialListFromTransDetail( true );
        flagStockDTO.setUpdateAccountBalance(true);

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );

        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        lstConfigEnableBccs1.add( optionSetValueDTO );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigEnableBccs1 );

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setProductOfferTypeId( 1L );

        Mockito.when( productOfferingService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferingDTO );

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName( "STOCK_NUMBER" );
        Mockito.when( productOfferTypeService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferTypeDTO );

        List<OptionSetValueDTO> lstConfigStockCard = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO1 = new OptionSetValueDTO();
        optionSetValueDTO1.setValue( "1" );
        lstConfigStockCard.add( optionSetValueDTO1 );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigStockCard );

        Mockito.doNothing().when( stockCardService ).doSaveStockCard( Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any() );

        baseStockService.doSaveStockGoods( stockTransDTO, stockTransDetailDTOList, flagStockDTO );
    }

    @Test
    public void testDoSaveStockGoods_40() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline( "2" );
        stockTransDTO.setExchangeStockId( 2L );
        stockTransDTO.setToOwnerType( 2L );
        stockTransDTO.setToOwnerId( 2L );
        stockTransDTO.setRegionStockId( 1L );
        stockTransDTO.setStatus( "6" );
        stockTransDTO.setPayStatus( "5" );
        stockTransDTO.setBankplusStatus( 0L );

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId( 1L );
        stockTransDetailDTO.setTableName( null );
        stockTransDetailDTO.setQuantity( 2L );
        stockTransDetailDTO.setProdOfferTypeId(7L);

        stockTransDetailDTO.setCheckSerial( 2L );
        stockTransDetailDTOList.add( stockTransDetailDTO );

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setOldStatus( 1L );
        flagStockDTO.setNewStatus( 1L );
        flagStockDTO.setUpdateOwnerId( true );
        flagStockDTO.setStatusForAgent( "6" );
        flagStockDTO.setShopId( 7282L );
        flagStockDTO.setObjectType( "AGENT_EXPORT" );
        flagStockDTO.setUpdateSaleDate( true );
        flagStockDTO.setGpon( true );
        flagStockDTO.setUpdateAccountBalance( false );
        flagStockDTO.setUpdateDepositPrice( true );
        flagStockDTO.setStateIdForReasonId( 1L );
        flagStockDTO.setUpdateStateDemo( true );
        flagStockDTO.setUpdateProdOfferId( true );

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );

        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue( "1" );
        lstConfigEnableBccs1.add( optionSetValueDTO );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigEnableBccs1 );

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setProductOfferTypeId( 4L );

        Mockito.when( productOfferingService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferingDTO );

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setTableName( "STOCK_HANDSET" );
        Mockito.when( productOfferTypeService.findOne( Mockito.anyLong() ) ).thenReturn( productOfferTypeDTO );

        List<OptionSetValueDTO> lstConfigStockCard = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO1 = new OptionSetValueDTO();
        optionSetValueDTO1.setValue( "1" );
        lstConfigStockCard.add( optionSetValueDTO1 );

        Mockito.when( optionSetValueService.getByOptionSetCode( Mockito.anyString() ) ).thenReturn( lstConfigStockCard );

        Mockito.doNothing().when( stockCardService ).doSaveStockCard( Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any() );
        List<StockTransSerialDTO> lstStockTransSerial = Lists.newArrayList();
        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        stockTransSerialDTO.setStateId( 1L );
        stockTransSerialDTO.setFromSerial( "1" );
        stockTransSerialDTO.setToSerial( "1" );
        lstStockTransSerial.add( stockTransSerialDTO );
        StockTotalDTO stockTotalDTOExport = new StockTotalDTO();
        stockTotalDTOExport.setAvailableQuantity( 1L );

        BaseStockService spyTemp = PowerMockito.spy( baseStockService );
        Mockito.when( stockTransSerialService.findByStockTransDetailId( Mockito.anyLong() ) ).thenReturn( lstStockTransSerial );
        PowerMockito.doReturn( 2L ).when( spyTemp, "updateSerialPairCollection", any(), any() );

        spyTemp.doSaveStockGoods( stockTransDTO, stockTransDetailDTOList, flagStockDTO );
    }


    /** -------------------------------------------------------------------------- **/
    /** --- Test for stockTransSerialService.doSignVoffice method----------------- **/
    /** -------------------------------------------------------------------------- **/
    @Test
    public void testDoSignVoffice_1() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setSignVoffice(false);

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        FlagStockDTO flagStockDTO = new FlagStockDTO();

        baseStockService.doSignVoffice(stockTransDTO, stockTransActionDTO, requiredRoleMap, flagStockDTO);
    }

    @Test
    public void testDoSignVoffice_2() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setSignVoffice(true);

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        FlagStockDTO flagStockDTO = new FlagStockDTO();

        Mockito.when(signFlowDetailService.findBySignFlowId(Mockito.anyLong())).thenReturn(null);

        baseStockService.doSignVoffice(stockTransDTO, stockTransActionDTO, requiredRoleMap, flagStockDTO);
    }

    @Test
    public void testDoSignVoffice_3() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setSignVoffice(true);

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        FlagStockDTO flagStockDTO = new FlagStockDTO();

        List<SignFlowDetailDTO> lstSignFlowDetail = Lists.newArrayList();
        SignFlowDetailDTO signFlowDetailDTO = new SignFlowDetailDTO();
        signFlowDetailDTO.setEmail(null);
        lstSignFlowDetail.add(signFlowDetailDTO);
        Mockito.when(signFlowDetailService.findBySignFlowId(Mockito.anyLong())).thenReturn(lstSignFlowDetail);

        baseStockService.doSignVoffice(stockTransDTO, stockTransActionDTO, requiredRoleMap, flagStockDTO);
    }

    @Test
    public void testDoSignVoffice_4() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setSignVoffice(true);
        stockTransDTO.setUserName("NoName");
        stockTransDTO.setPassWord("NoPass");
        stockTransDTO.setCreateDatetime(Calendar.getInstance().getTime());

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setStockTransActionId(1L);
        stockTransActionDTO.setActionCode("code");
        stockTransActionDTO.setStatus("2");
        stockTransActionDTO.setActionCodeShop("code");

        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        List<String> list = Lists.newArrayList();
        list.add(Const.PERMISION.ROLE_STOCK_NUM_SHOP);
        requiredRoleMap.setValues(list);

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setFindSerial(1L);
        flagStockDTO.setPrefixActionCode("code");

        List<SignFlowDetailDTO> lstSignFlowDetail = Lists.newArrayList();
        SignFlowDetailDTO signFlowDetailDTO = new SignFlowDetailDTO();
        SignFlowDetailDTO signFlowDetailDTO1 = new SignFlowDetailDTO();
        signFlowDetailDTO.setEmail("mailtest@test.com.vn");
        signFlowDetailDTO1.setEmail("mailtest2@test.com.vn");
        lstSignFlowDetail.add(signFlowDetailDTO);
        lstSignFlowDetail.add(signFlowDetailDTO1);

        Mockito.when(signFlowDetailService.findBySignFlowId(Mockito.anyLong())).thenReturn(lstSignFlowDetail);

        PowerMockito.mockStatic(DbUtil.class);
        when(DbUtil.getSysDate(any(EntityManager.class))).thenReturn(new Date());
        when(DbUtil.getSequence(any(EntityManager.class), anyString())).thenReturn(999L);

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(1L);
        when(staffService.findOne(anyLong())).thenReturn(staffDTO);
        when(shopService.isCenterOrBranch(anyLong())).thenReturn(true);

        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        when(stockTransVofficeService.save(any(StockTransVofficeDTO.class))).thenReturn(stockTransVofficeDTO);

        baseStockService.doSignVoffice(stockTransDTO, stockTransActionDTO, requiredRoleMap, flagStockDTO);
        Assert.assertNotNull(stockTransVofficeDTO);
    }

    @Test
    public void testDoSignVoffice_5() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setSignVoffice(true);
        stockTransDTO.setUserName("NoName");
        stockTransDTO.setPassWord("NoPass");
        stockTransDTO.setCreateDatetime(Calendar.getInstance().getTime());

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setStockTransActionId(1L);
        stockTransActionDTO.setActionCode("code");
        stockTransActionDTO.setStatus("5");
        stockTransActionDTO.setActionCodeShop("code");

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setFindSerial(1L);
        flagStockDTO.setPrefixActionCode("code");

        List<SignFlowDetailDTO> lstSignFlowDetail = Lists.newArrayList();
        SignFlowDetailDTO signFlowDetailDTO = new SignFlowDetailDTO();
        SignFlowDetailDTO signFlowDetailDTO1 = new SignFlowDetailDTO();
        signFlowDetailDTO.setEmail("mailtest@test.com.vn");
        signFlowDetailDTO1.setEmail("mailtest2@test.com.vn");
        lstSignFlowDetail.add(signFlowDetailDTO);
        lstSignFlowDetail.add(signFlowDetailDTO1);

        Mockito.when(signFlowDetailService.findBySignFlowId(Mockito.anyLong())).thenReturn(lstSignFlowDetail);

        PowerMockito.mockStatic(DbUtil.class);
        when(DbUtil.getSysDate(any(EntityManager.class))).thenReturn(new Date());
        when(DbUtil.getSequence(any(EntityManager.class), anyString())).thenReturn(999L);

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(1L);
        when(staffService.findOne(anyLong())).thenReturn(staffDTO);
        when(shopService.isCenterOrBranch(anyLong())).thenReturn(true);

        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        when(stockTransVofficeService.save(any(StockTransVofficeDTO.class))).thenReturn(stockTransVofficeDTO);

        baseStockService.doSignVoffice(stockTransDTO, stockTransActionDTO, null, flagStockDTO);
        Assert.assertNotNull(stockTransVofficeDTO);
    }

    @Test
    public void testDoSignVoffice_6() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setSignVoffice(true);
        stockTransDTO.setUserName("NoName");
        stockTransDTO.setPassWord("NoPass");
        stockTransDTO.setCreateDatetime(Calendar.getInstance().getTime());

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setStockTransActionId(1L);
        stockTransActionDTO.setActionCode("code");
        stockTransActionDTO.setStatus("34");
        stockTransActionDTO.setActionCodeShop("code");

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setFindSerial(1L);
        flagStockDTO.setPrefixActionCode("code");

        List<SignFlowDetailDTO> lstSignFlowDetail = Lists.newArrayList();
        SignFlowDetailDTO signFlowDetailDTO = new SignFlowDetailDTO();
        SignFlowDetailDTO signFlowDetailDTO1 = new SignFlowDetailDTO();
        signFlowDetailDTO.setEmail("mailtest@test.com.vn");
        signFlowDetailDTO1.setEmail("mailtest2@test.com.vn");
        lstSignFlowDetail.add(signFlowDetailDTO);
        lstSignFlowDetail.add(signFlowDetailDTO1);

        Mockito.when(signFlowDetailService.findBySignFlowId(Mockito.anyLong())).thenReturn(lstSignFlowDetail);

        PowerMockito.mockStatic(DbUtil.class);
        when(DbUtil.getSysDate(any(EntityManager.class))).thenReturn(new Date());
        when(DbUtil.getSequence(any(EntityManager.class), anyString())).thenReturn(999L);

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(1L);
        when(staffService.findOne(anyLong())).thenReturn(staffDTO);
        when(shopService.isCenterOrBranch(anyLong())).thenReturn(true);

        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        when(stockTransVofficeService.save(any(StockTransVofficeDTO.class))).thenReturn(stockTransVofficeDTO);

        baseStockService.doSignVoffice(stockTransDTO, stockTransActionDTO, null, flagStockDTO);
        Assert.assertNotNull(stockTransVofficeDTO);
    }

    /** -------------------------------------------------------------------------- **/
    /** --- Test for stockTransSerialService.doSyncLogistic method----------------- **/
    /** -------------------------------------------------------------------------- **/
    @Test
    public void testDoSyncLogistic_1() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setLogicstic("1");
        stockTransDTO.setCreateDatetime(Calendar.getInstance().getTime());
        stockTransDTO.setStockTransId(1L);

        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();

        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        List<String> list = Lists.newArrayList();
        list.add(Const.PERMISION.ROLE_SYNC_LOGISTIC);
        requiredRoleMap.setValues(list);

        StockTransLogisticDTO stockTransLogisticDTO = new StockTransLogisticDTO();
        when(stockTransLogisticService.save(any(StockTransLogisticDTO.class))).thenReturn(stockTransLogisticDTO);

        baseStockService.doSyncLogistic(stockTransDTO, lstStockTransDetail, requiredRoleMap);
        Assert.assertNotNull(stockTransLogisticDTO);
    }

    @Test
    public void testDoSyncLogistic_2() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setLogicstic("1");
        stockTransDTO.setCreateDatetime(Calendar.getInstance().getTime());
        stockTransDTO.setStockTransId(1L);

        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();

        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        List<String> list = Lists.newArrayList();
        list.add(Const.PERMISION.ROLE_SYNC_LOGISTIC);
        requiredRoleMap.setValues(list);

        StockTransLogisticDTO stockTransLogisticDTO = new StockTransLogisticDTO();
        when(stockTransLogisticService.save(any(StockTransLogisticDTO.class))).thenReturn(stockTransLogisticDTO);

        baseStockService.doSyncLogistic(stockTransDTO, lstStockTransDetail, null);
        Assert.assertNotNull(stockTransLogisticDTO);
    }

    @Test
    public void testDoSyncLogistic_3() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setLogicstic("2");
        stockTransDTO.setCreateDatetime(Calendar.getInstance().getTime());
        stockTransDTO.setStockTransId(1L);

        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();

        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        List<String> list = Lists.newArrayList();
        list.add(Const.PERMISION.ROLE_SYNC_LOGISTIC);
        requiredRoleMap.setValues(list);

        StockTransLogisticDTO stockTransLogisticDTO = new StockTransLogisticDTO();
        when(stockTransLogisticService.save(any(StockTransLogisticDTO.class))).thenReturn(stockTransLogisticDTO);

        baseStockService.doSyncLogistic(stockTransDTO, lstStockTransDetail, requiredRoleMap);
        Assert.assertNotNull(stockTransLogisticDTO);
    }

    /** -------------------------------------------------------------------------- **/
    /** --- Test for stockTransSerialService.doIncreaseStockNum method------------ **/
    /** -------------------------------------------------------------------------- **/
    @Test
    public void testDoIncreaseStockNum_1() throws Exception {

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        String prefixActionCode = null;

        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        List<String> list = Lists.newArrayList();
        list.add(Const.PERMISION.ROLE_SYNC_LOGISTIC);
        requiredRoleMap.setValues(list);

        baseStockService.doIncreaseStockNum(stockTransActionDTO, prefixActionCode, requiredRoleMap);
    }

    @Test
    public void testDoIncreaseStockNum_2() throws Exception {

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        String prefixActionCode = "PX";

        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        List<String> list = Lists.newArrayList();
        list.add(Const.PERMISION.ROLE_SYNC_LOGISTIC);
        requiredRoleMap.setValues(list);

        List<OptionSetValueDTO> valueLst = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue("1");
        valueLst.add(optionSetValueDTO);

        Mockito.when(optionSetValueService.getByOptionSetCode(Mockito.anyString())).thenReturn(valueLst);
        Mockito.doNothing().when(baseStockIm1Service).doIncreaseStockNum(stockTransActionDTO, prefixActionCode, requiredRoleMap);

        baseStockService.doIncreaseStockNum(stockTransActionDTO, prefixActionCode, requiredRoleMap);
    }

    @Test
    public void testDoIncreaseStockNum_3() throws Exception {

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        String prefixActionCode = "PX";

        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        List<String> list = Lists.newArrayList();
        list.add(Const.PERMISION.ROLE_STOCK_NUM_SHOP);
        requiredRoleMap.setValues(list);

        Mockito.when(optionSetValueService.getByOptionSetCode(Mockito.anyString())).thenReturn(null);
        Mockito.doNothing().when(baseStockIm1Service).doIncreaseStockNum(stockTransActionDTO, prefixActionCode, requiredRoleMap);

        StaffDTO staff = new StaffDTO();
        staff.setStockNum(1L);
        Mockito.when(staffService.findOne(anyLong())).thenReturn(staff);

        StaffDTO staffUpd = new StaffDTO();
        Mockito.when(staffService.save(any(StaffDTO.class))).thenReturn(staffUpd);

        BaseStockService spyTemp = Mockito.spy(baseStockService);
        Mockito.doNothing().when(spyTemp).updateStockNumShop(anyLong(), anyString(), anyLong());
        spyTemp.doIncreaseStockNum(stockTransActionDTO, prefixActionCode, requiredRoleMap);
    }

    @Test
    public void testDoIncreaseStockNum_4() throws Exception {

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        String prefixActionCode = "PN";

        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        List<String> list = Lists.newArrayList();
        list.add(Const.PERMISION.ROLE_STOCK_NUM_SHOP);
        requiredRoleMap.setValues(list);

        Mockito.when(optionSetValueService.getByOptionSetCode(Mockito.anyString())).thenReturn(null);
        Mockito.doNothing().when(baseStockIm1Service).doIncreaseStockNum(stockTransActionDTO, prefixActionCode, requiredRoleMap);

        StaffDTO staff = new StaffDTO();
        staff.setStockNum(1L);
        Mockito.when(staffService.findOne(anyLong())).thenReturn(staff);

        StaffDTO staffUpd = new StaffDTO();
        Mockito.when(staffService.save(any(StaffDTO.class))).thenReturn(staffUpd);

        BaseStockService spyTemp = Mockito.spy(baseStockService);
        Mockito.doNothing().when(spyTemp).updateStockNumShop(anyLong(), anyString(), anyLong());
        spyTemp.doIncreaseStockNum(stockTransActionDTO, prefixActionCode, requiredRoleMap);
    }

    @Test
    public void testDoIncreaseStockNum_5() throws Exception {

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        String prefixActionCode = "PX";

        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        List<String> list = Lists.newArrayList();
        list.add(Const.PERMISION.ROLE_STOCK_NUM_SHOP);
        requiredRoleMap.setValues(list);

        Mockito.when(optionSetValueService.getByOptionSetCode(Mockito.anyString())).thenReturn(null);
        Mockito.doNothing().when(baseStockIm1Service).doIncreaseStockNum(stockTransActionDTO, prefixActionCode, requiredRoleMap);

        StaffDTO staff = new StaffDTO();
        staff.setStockNum(1L);
        Mockito.when(staffService.findOne(anyLong())).thenReturn(staff);
        Mockito.when(staffService.save(any(StaffDTO.class))).thenReturn(null);

        BaseStockService spyTemp = Mockito.spy(baseStockService);
        Mockito.doNothing().when(spyTemp).updateStockNumShop(anyLong(), anyString(), anyLong());
        spyTemp.doIncreaseStockNum(stockTransActionDTO, prefixActionCode, requiredRoleMap);
    }

    @Test
    public void testDoIncreaseStockNum_6() throws Exception {

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        String prefixActionCode = "LN";

        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        List<String> list = Lists.newArrayList();
        list.add(Const.PERMISION.ROLE_STOCK_NUM_SHOP);
        requiredRoleMap.setValues(list);

        Mockito.when(optionSetValueService.getByOptionSetCode(Mockito.anyString())).thenReturn(null);
        Mockito.doNothing().when(baseStockIm1Service).doIncreaseStockNum(stockTransActionDTO, prefixActionCode, requiredRoleMap);

        StaffDTO staff = new StaffDTO();
        staff.setStockNum(1L);
        Mockito.when(staffService.findOne(anyLong())).thenReturn(staff);

        StaffDTO staffUpd = new StaffDTO();
        Mockito.when(staffService.save(any(StaffDTO.class))).thenReturn(staffUpd);

        BaseStockService spyTemp = Mockito.spy(baseStockService);
        Mockito.doNothing().when(spyTemp).updateStockNumShop(anyLong(), anyString(), anyLong());
        spyTemp.doIncreaseStockNum(stockTransActionDTO, prefixActionCode, null);
    }

    @Test
    public void testDoIncreaseStockNum_7() throws Exception {

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        String prefixActionCode = "LN";

        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        List<String> list = Lists.newArrayList();
        list.add(Const.PERMISION.ROLE_DISTRIBUTION_NICE_ISDN);
        requiredRoleMap.setValues(list);

        Mockito.when(optionSetValueService.getByOptionSetCode(Mockito.anyString())).thenReturn(null);
        Mockito.doNothing().when(baseStockIm1Service).doIncreaseStockNum(stockTransActionDTO, prefixActionCode, requiredRoleMap);

        StaffDTO staff = new StaffDTO();
        staff.setStockNum(1L);
        Mockito.when(staffService.findOne(anyLong())).thenReturn(staff);

        StaffDTO staffUpd = new StaffDTO();
        Mockito.when(staffService.save(any(StaffDTO.class))).thenReturn(staffUpd);

        BaseStockService spyTemp = Mockito.spy(baseStockService);
        Mockito.doNothing().when(spyTemp).updateStockNumShop(anyLong(), anyString(), anyLong());
        spyTemp.doIncreaseStockNum(stockTransActionDTO, prefixActionCode, requiredRoleMap);
    }

    @Test
    public void testDoIncreaseStockNum_8() throws Exception {

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        String prefixActionCode = "PX";

        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        List<String> list = Lists.newArrayList();
        list.add(Const.PERMISION.ROLE_SYNC_LOGISTIC);
        requiredRoleMap.setValues(list);

        List<OptionSetValueDTO> valueLst = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue(null);
        valueLst.add(optionSetValueDTO);

        Mockito.when(optionSetValueService.getByOptionSetCode(Mockito.anyString())).thenReturn(valueLst);
        Mockito.when(staffService.save(any(StaffDTO.class))).thenReturn(null);
        Mockito.doNothing().when(baseStockIm1Service).doIncreaseStockNum(stockTransActionDTO, prefixActionCode, requiredRoleMap);

        baseStockService.doIncreaseStockNum(stockTransActionDTO, prefixActionCode, requiredRoleMap);
    }

    @Test
    public void testDoIncreaseStockNum_9() throws Exception {

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        String prefixActionCode = "PX";

        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        List<String> list = Lists.newArrayList();
        list.add(Const.PERMISION.ROLE_SYNC_LOGISTIC);
        requiredRoleMap.setValues(list);

        List<OptionSetValueDTO> valueLst = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue("2");
        valueLst.add(optionSetValueDTO);

        Mockito.when(optionSetValueService.getByOptionSetCode(Mockito.anyString())).thenReturn(valueLst);
        Mockito.when(staffService.save(any(StaffDTO.class))).thenReturn(null);
        Mockito.doNothing().when(baseStockIm1Service).doIncreaseStockNum(stockTransActionDTO, prefixActionCode, requiredRoleMap);

        baseStockService.doIncreaseStockNum(stockTransActionDTO, prefixActionCode, requiredRoleMap);
    }

    /** -------------------------------------------------------------------------- **/
    /** --- Test for stockTransSerialService.updateStockNumShop method------------ **/
    /** -------------------------------------------------------------------------- **/
    @Test
    public void testUpdateStockNumShop_1() throws Exception {

        Long shopId = null;
        String prefixActionCode = "PN";
        Long stockTransActionId = null;

        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setStockNum(1L);
        Mockito.when(shopService.findOne(Mockito.anyLong())).thenReturn(shopDTO);

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setStockTransId(1L);
        Mockito.when(stockTransActionService.findOne(Mockito.anyLong())).thenReturn(stockTransActionDTO);

        Mockito.when(stockTransActionService.save(any(StockTransActionDTO.class))).thenReturn(stockTransActionDTO);
        Mockito.when(shopService.save(any(ShopDTO.class))).thenReturn(shopDTO);

        baseStockService.updateStockNumShop(shopId, prefixActionCode, stockTransActionId);
        Assert.assertNotNull(stockTransActionDTO);
        Assert.assertNotNull(shopDTO);
    }

    @Test
    public void testUpdateStockNumShop_2() throws Exception {

        Long shopId = null;
        String prefixActionCode = "PX";
        Long stockTransActionId = null;

        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setStockNum(1L);
        Mockito.when(shopService.findOne(Mockito.anyLong())).thenReturn(shopDTO);

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setStockTransId(1L);
        Mockito.when(stockTransActionService.findOne(Mockito.anyLong())).thenReturn(stockTransActionDTO);

        Mockito.when(stockTransActionService.save(any(StockTransActionDTO.class))).thenReturn(stockTransActionDTO);
        Mockito.when(shopService.save(any(ShopDTO.class))).thenReturn(shopDTO);

        baseStockService.updateStockNumShop(shopId, prefixActionCode, stockTransActionId);
        Assert.assertNotNull(stockTransActionDTO);
        Assert.assertNotNull(shopDTO);
    }

    @Test
    public void testUpdateStockNumShop_3() throws Exception {

        Long shopId = null;
        String prefixActionCode = "PX";
        Long stockTransActionId = null;

        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setStockNum(1L);
        Mockito.when(shopService.findOne(Mockito.anyLong())).thenReturn(null);

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setStockTransId(1L);
        Mockito.when(stockTransActionService.findOne(Mockito.anyLong())).thenReturn(stockTransActionDTO);

        Mockito.when(stockTransActionService.save(any(StockTransActionDTO.class))).thenReturn(stockTransActionDTO);
        Mockito.when(shopService.save(any(ShopDTO.class))).thenReturn(shopDTO);

        baseStockService.updateStockNumShop(shopId, prefixActionCode, stockTransActionId);
        Assert.assertNotNull(stockTransActionDTO);
        Assert.assertNotNull(shopDTO);
    }

    @Test
    public void testUpdateStockNumShop_4() throws Exception {

        Long shopId = null;
        String prefixActionCode = "PT";
        Long stockTransActionId = null;

        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setStockNum(1L);
        Mockito.when(shopService.findOne(Mockito.anyLong())).thenReturn(shopDTO);

        Mockito.when(stockTransActionService.findOne(Mockito.anyLong())).thenReturn(null);

        Mockito.when(shopService.save(any(ShopDTO.class))).thenReturn(shopDTO);

        baseStockService.updateStockNumShop(shopId, prefixActionCode, stockTransActionId);
        Assert.assertNotNull(shopDTO);
    }

    /** -------------------------------------------------------------------------- **/
    /** --- Test for stockTransSerialService.doUnlockUser method------------------ **/
    /** -------------------------------------------------------------------------- **/
    @Test
    public void testDoUnlockUser_1() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        Mockito.doNothing().when(stockTransRepo).unlockUserInfo(anyLong());

        baseStockService.doUnlockUser(stockTransDTO);
    }

    /** -------------------------------------------------------------------------- **/
    /** --- Test for stockTransSerialService.doSaveStockOffline method------------ **/
    /** -------------------------------------------------------------------------- **/
    @Test
    public void testDoSaveStockOffline_1() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline("2");
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();

        baseStockService.doSaveStockOffline(stockTransDTO, lstStockTransDetail);
    }

    @Test
    public void testDoSaveStockOffline_2() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline("1");
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();

        baseStockService.doSaveStockOffline(stockTransDTO, lstStockTransDetail);
    }

    @Test
    public void testDoSaveStockOffline_3() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline("1");

        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        lstStockTransDetail.add(stockTransDetailDTO);

        StockTransDTO oldStockTrans = new StockTransDTO();
        Mockito.when(stockTransService.findOne(anyLong())).thenReturn(null);

        baseStockService.doSaveStockOffline(stockTransDTO, lstStockTransDetail);
    }

    @Test
    public void testDoSaveStockOffline_4() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline("1");

        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        lstStockTransDetail.add(stockTransDetailDTO);

        StockTransDTO oldStockTrans = new StockTransDTO();
        oldStockTrans.setStockTransId(1L);
        Mockito.when(stockTransService.findOne(anyLong())).thenReturn(oldStockTrans);

        BaseStockService spyTemp = Mockito.spy(baseStockService);

        Mockito.doNothing().when(spyTemp).doSaveStockTransOffline(oldStockTrans);

        FilterRequest filterRequest = new FilterRequest();
        Mockito.when(stockTransDetailService.findByFilter(Lists.newArrayList(Mockito.anyCollection()))).thenReturn(null);

        baseStockService.doSaveStockOffline(stockTransDTO, lstStockTransDetail);
    }

    @Test
    public void testDoSaveStockOffline_5() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setProcessOffline("1");

        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        lstStockTransDetail.add(stockTransDetailDTO);

        StockTransDTO oldStockTrans = new StockTransDTO();
        oldStockTrans.setStockTransId(1L);
        Mockito.when(stockTransService.findOne(anyLong())).thenReturn(oldStockTrans);

        BaseStockService spyTemp = Mockito.spy(baseStockService);
        Mockito.doNothing().when(spyTemp).doSaveStockTransOffline(oldStockTrans);

        FilterRequest filterRequest = new FilterRequest();
        Mockito.when(stockTransDetailService.findByFilter(Lists.newArrayList(Mockito.anyCollection()))).thenReturn(lstStockTransDetail);
        Mockito.doNothing().when(spyTemp).doSaveStockTransDetailOffline(stockTransDTO, lstStockTransDetail);
        Mockito.doNothing().when(spyTemp).doSaveStockTransSerialOffline(stockTransDTO, lstStockTransDetail);

        spyTemp.doSaveStockOffline(stockTransDTO, lstStockTransDetail);
    }

    /** -------------------------------------------------------------------------- **/
    /** --- Test for stockTransSerialService.doSaveStockTransOffline method------- **/
    /** -------------------------------------------------------------------------- **/
    @Test
    public void testDoSaveStockTransOffline_1() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransOfflineDTO stockTransOfflineDTO = new StockTransOfflineDTO();
        Mockito.when(stockTransOfflineService.save(any(StockTransOfflineDTO.class))).thenReturn(stockTransOfflineDTO);

        baseStockService.doSaveStockTransOffline(stockTransDTO);
        Assert.assertNotNull(stockTransOfflineDTO);
    }

    /** -------------------------------------------------------------------------- **/
    /** --- Test for stockTransSerialService.doSaveStockTransSerialOffline method- **/
    /** -------------------------------------------------------------------------- **/
    @Test
    public void testDoSaveStockTransSerialOffline_1() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();

        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setCheckSerial(1L);
        lstStockTransDetail.add(stockTransDetailDTO);
        stockTransDetailDTO.setLstStockTransSerial(null);

        baseStockService.doSaveStockTransSerialOffline(stockTransDTO, lstStockTransDetail);
    }

    @Test
    public void testDoSaveStockTransSerialOffline_2() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();

        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setCheckSerial(1L);
        lstStockTransDetail.add(stockTransDetailDTO);

        List<StockTransSerialDTO> lstStockTransSerial = Lists.newArrayList();
        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        stockTransSerialDTO.setStockTransSerialId(1L);
        lstStockTransSerial.add(stockTransSerialDTO);

        stockTransDetailDTO.setLstStockTransSerial(lstStockTransSerial);
        StockTransSerialOfflineDTO stockTransSerialOfflineDTO = new StockTransSerialOfflineDTO();
        Mockito.when(stockTransSerialOfflineService.save(any(StockTransSerialOfflineDTO.class))).thenReturn(stockTransSerialOfflineDTO);
        baseStockService.doSaveStockTransSerialOffline(stockTransDTO, lstStockTransDetail);
        Assert.assertNotNull(stockTransSerialOfflineDTO);
    }

    @Test
    public void testDoSaveStockTransSerialOffline_3() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        baseStockService.doSaveStockTransSerialOffline(stockTransDTO, lstStockTransDetail);
    }

    /** -------------------------------------------------------------------------- **/
    /** --- Test for stockTransSerialService.doSaveStockTransSerialOffline method- **/
    /** -------------------------------------------------------------------------- **/
    @Test
    public void testDoSaveStockTransOffline_2() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();

        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setCheckSerial(1L);
        lstStockTransDetail.add(stockTransDetailDTO);
        stockTransDetailDTO.setLstStockTransSerial(null);

        StockTransOfflineDTO stockTransOfflineDTO = new StockTransOfflineDTO();
        Mockito.when(stockTransOfflineService.save(any(StockTransOfflineDTO.class))).thenReturn(stockTransOfflineDTO);

        baseStockService.doSaveStockTransDetailOffline(stockTransDTO, lstStockTransDetail);
    }

    /** -------------------------------------------------------------------------- **/
    /** --- Test for stockTransSerialService.copyStockTransDTO method------------- **/
    /** -------------------------------------------------------------------------- **/
    @Test
    public void testCopyStockTransDTO_1() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransOfflineDTO stockTransOfflineDTO = baseStockService.copyStockTransDTO(stockTransDTO);

        Assert.assertNotNull(stockTransOfflineDTO);
    }

    @Test
    public void testCopyStockTransDTO_2() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransOfflineDTO stockTransOfflineDTO = baseStockService.copyStockTransDTO(null);
    }

    /** -------------------------------------------------------------------------- **/
    /** --- Test for stockTransSerialService.doSaveStockTransDetailOffline method- **/
    /** -------------------------------------------------------------------------- **/
    @Test
    public void testDoSaveStockTransDetailOffline_1() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();

        baseStockService.doSaveStockTransDetailOffline(stockTransDTO, lstStockTransDetail);
    }

    /** -------------------------------------------------------------------------- **/
    /** --- Test for stockTransSerialService.copyStockTransDetailDTO method------- **/
    /** -------------------------------------------------------------------------- **/
    @Test
    public void testCopyStockTransDetailDTO_1() throws Exception {

        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        StockTransDetailOfflineDTO stockTransDetailOfflineDTO = baseStockService.copyStockTransDetailDTO(stockTransDetailDTO);

        Assert.assertNotNull(stockTransDetailOfflineDTO);
    }

    /** -------------------------------------------------------------------------- **/
    /** --- Test for stockTransSerialService.doSaveReceiptExpense method---------- **/
    /** -------------------------------------------------------------------------- **/
    @Test
    public void testDoSaveReceiptExpense_1() throws Exception {

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setInsertReceiptExpense(false);

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();

        baseStockService.doSaveReceiptExpense(flagStockDTO, stockTransDTO, stockTransActionDTO, lstStockTransDetail);

    }

    @Test
    public void testDoSaveReceiptExpense_2() throws Exception {

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setInsertReceiptExpense(true);
        flagStockDTO.setDepositStatus("2");

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();

        BaseStockService spyTemp = Mockito.spy(baseStockService);
        Mockito.doNothing().when(spyTemp).doCancelReceiptExpense(flagStockDTO, stockTransDTO, stockTransActionDTO, lstStockTransDetail);
        baseStockService.doSaveReceiptExpense(flagStockDTO, stockTransDTO, stockTransActionDTO, lstStockTransDetail);

    }

    @Test
    public void testDoSaveReceiptExpense_3() throws Exception {

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setInsertReceiptExpense(true);
        flagStockDTO.setDepositStatus("1");

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();

        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProductOfferPriceId(2L);
        lstStockTransDetail.add(stockTransDetailDTO);

        ReceiptExpenseDTO receiptExpenseDTO = new ReceiptExpenseDTO();
        Mockito.when(receiptExpenseService.create(any(ReceiptExpenseDTO.class), anyString())).thenReturn(receiptExpenseDTO);

        DepositDTO depositDTO = new DepositDTO();
        Mockito.when(depositService.create(any(DepositDTO.class))).thenReturn(depositDTO);

        DepositDetailDTO depositDetailDTO = new DepositDetailDTO();
        Mockito.when(depositDetailService.create(any(DepositDetailDTO.class))).thenReturn(depositDetailDTO);
        baseStockService.doSaveReceiptExpense(flagStockDTO, stockTransDTO, stockTransActionDTO, lstStockTransDetail);

    }

    @Test
    public void testDoSaveReceiptExpense_4() throws Exception {

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setInsertReceiptExpense(true);
        flagStockDTO.setDepositStatus("1");

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();

        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProductOfferPriceId(2L);
        lstStockTransDetail.add(stockTransDetailDTO);

        ReceiptExpenseDTO receiptExpenseDTO = new ReceiptExpenseDTO();
        Mockito.when(receiptExpenseService.create(any(ReceiptExpenseDTO.class), anyString())).thenReturn(receiptExpenseDTO);

        DepositDTO depositDTO = new DepositDTO();
        Mockito.when(depositService.create(any(DepositDTO.class))).thenReturn(depositDTO);

        DepositDetailDTO depositDetailDTO = new DepositDetailDTO();
        Mockito.when(depositDetailService.create(any(DepositDetailDTO.class))).thenReturn(depositDetailDTO);
        baseStockService.doSaveReceiptExpense(flagStockDTO, stockTransDTO, stockTransActionDTO, null);

    }

    /** -------------------------------------------------------------------------- **/
    /** --- Test for stockTransSerialService.doSaveStockTotalAudit method--------- **/
    /** -------------------------------------------------------------------------- **/
    @Test
    public void testDoSaveStockTotalAudit_1() throws Exception {

        baseStockService.doSaveStockTotalAudit(null);

    }

    @Test
    public void testDoSaveStockTotalAudit_2() throws Exception {

        StockTotalAuditDTO stockTotalAuditDTO = new StockTotalAuditDTO();
        stockTotalAuditDTO.setTransId(1L);

        Mockito.when(stockTotalAuditService.save(any(StockTotalAuditDTO.class))).thenReturn(stockTotalAuditDTO);
        baseStockService.doSaveStockTotalAudit(stockTotalAuditDTO);
        Assert.assertNotNull(stockTotalAuditDTO);

    }

    /** -------------------------------------------------------------------------- **/
    /** --- Test for stockTransSerialService.doSaveExchangeStockTrans method------ **/
    /** -------------------------------------------------------------------------- **/
    @Test
    public void testDoSaveExchangeStockTrans_1() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setExchangeStockId(0L);

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();

        baseStockService.doSaveExchangeStockTrans(stockTransDTO, stockTransActionDTO, flagStockDTO, lstStockTransDetail);

    }

    @Test
    public void testDoSaveExchangeStockTrans_2() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setExchangeStockId(3L);
        stockTransDTO.setRegionStockTransId(0L);

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();

        StockTransDTO exportDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionSave = new StockTransActionDTO();
        BaseStockService spyTemp = Mockito.spy(baseStockService);
        Mockito.doReturn(exportDTO).when(spyTemp).doSaveStockTrans(any(StockTransDTO.class));
        Mockito.doNothing().when(spyTemp).doSaveStockTransDetail(any(StockTransDTO.class), anyListOf(StockTransDetailDTO.class));
        Mockito.doReturn(stockTransActionSave).when(spyTemp).doSaveStockTransAction(any(StockTransDTO.class), any(StockTransActionDTO.class));

        spyTemp.doSaveExchangeStockTrans(stockTransDTO, stockTransActionDTO, flagStockDTO, lstStockTransDetail);

    }

    @Test
    public void testDoSaveExchangeStockTrans_3() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setExchangeStockId(3L);
        stockTransDTO.setRegionStockTransId(1L);

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();

        StockTransDTO exportDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionSave = new StockTransActionDTO();
        BaseStockService spyTemp = Mockito.spy(baseStockService);
        Mockito.doReturn(exportDTO).when(spyTemp).doSaveStockTrans(any(StockTransDTO.class));
        Mockito.doNothing().when(spyTemp).doSaveStockTransDetail(any(StockTransDTO.class), anyListOf(StockTransDetailDTO.class));
        Mockito.doReturn(stockTransActionSave).when(spyTemp).doSaveStockTransAction(any(StockTransDTO.class), any(StockTransActionDTO.class));

        spyTemp.doSaveExchangeStockTrans(stockTransDTO, stockTransActionDTO, flagStockDTO, lstStockTransDetail);

    }

    /** -------------------------------------------------------------------------- **/
    /** --- Test for stockTransSerialService.doCancelReceiptExpense method-------- **/
    /** -------------------------------------------------------------------------- **/
    @Test
    public void testDoCancelReceiptExpense_1() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setInsertReceiptExpense(false);

        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();

        baseStockService.doCancelReceiptExpense(flagStockDTO, stockTransDTO, stockTransActionDTO, lstStockTransDetail);

    }

    @Test
    public void testDoCancelReceiptExpense_2() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setExchangeStockId(0L);

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setInsertReceiptExpense(true);

        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();

        List<DepositDTO> depositDTOs = Lists.newArrayList();
        DepositDTO depositDTO = new DepositDTO();
        depositDTOs.add(depositDTO);
        Mockito.when(depositService.findByFilter(anyList())).thenReturn(depositDTOs);

        ReceiptExpenseDTO receiptExpenseDTO = new ReceiptExpenseDTO();
        Mockito.when(receiptExpenseService.findOne(anyLong())).thenReturn(receiptExpenseDTO);

        Mockito.when(depositService.update(anyLong(), anyString())).thenReturn(depositDTO);
        Mockito.when(receiptExpenseService.update(any(ReceiptExpenseDTO.class), anyString())).thenReturn(receiptExpenseDTO);
        baseStockService.doCancelReceiptExpense(flagStockDTO, stockTransDTO, stockTransActionDTO, lstStockTransDetail);

        Assert.assertNotNull(depositDTOs);
        Assert.assertNotNull(receiptExpenseDTO);
        Assert.assertNotNull(depositDTO);
        Assert.assertNotNull(receiptExpenseDTO);

    }

    @Test
    public void testDoCancelReceiptExpense_3() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setExchangeStockId(0L);

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setInsertReceiptExpense(true);

        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();

        List<DepositDTO> depositDTOs = Lists.newArrayList();
        DepositDTO depositDTO = new DepositDTO();
        depositDTOs.add(depositDTO);
        Mockito.when(depositService.findByFilter(anyList())).thenReturn(null);
        baseStockService.doCancelReceiptExpense(flagStockDTO, stockTransDTO, stockTransActionDTO, lstStockTransDetail);

    }

    /** -------------------------------------------------------------------------- **/
    /** --- Test for stockTransSerialService.doUpdateBankplusBCCS method---------- **/
    /** -------------------------------------------------------------------------- **/
    @Test
    public void testDoUpdateBankplusBCCS_1() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setUpdateAccountBalance(false);

        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();

        baseStockService.doUpdateBankplusBCCS(flagStockDTO, null);

    }

    @Test
    public void testDoUpdateBankplusBCCS_2() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setDepositPrice(2L);
        stockTransDTO.setBankplusStatus(1L);
        stockTransDTO.setAccountBalanceId(2L);
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setUpdateAccountBalance(true);
        flagStockDTO.setStatusForAgent("3");

        Mockito.when(accountBalanceService.findLock(anyLong())).thenReturn(null);
        baseStockService.doUpdateBankplusBCCS(flagStockDTO, stockTransDTO);

    }

    @Test
    public void testDoUpdateBankplusBCCS_3() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setDepositPrice(2L);
        stockTransDTO.setBankplusStatus(1L);
        stockTransDTO.setAccountBalanceId(2L);
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setUpdateAccountBalance(true);
        flagStockDTO.setStatusForAgent("6");

        Mockito.when(accountBalanceService.findLock(anyLong())).thenReturn(null);
        baseStockService.doUpdateBankplusBCCS(flagStockDTO, stockTransDTO);

    }

    @Test
    public void testDoUpdateBankplusBCCS_4() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setDepositPrice(0L);
        stockTransDTO.setBankplusStatus(1L);
        stockTransDTO.setAccountBalanceId(2L);
        stockTransDTO.setTotalAmount(0L);
        stockTransDTO.setDepositTotal(0L);
        stockTransDTO.setDepositPrice(0L);

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setUpdateAccountBalance(true);
        flagStockDTO.setStatusForAgent("6");

        AccountBalanceDTO accountBalanceDTO = new AccountBalanceDTO();
        accountBalanceDTO.setAccountId(1L);
        accountBalanceDTO.setRealBalance(0L);
        Mockito.when(accountBalanceService.findLock(anyLong())).thenReturn(accountBalanceDTO);

        PowerMockito.mockStatic(DbUtil.class);
        when(DbUtil.getSysDate(any(EntityManager.class))).thenReturn(new Date());

        AccountBookBankplusDTO accountBookBankplusDTO = new AccountBookBankplusDTO();
        Mockito.when(accountBookBankplusService.create(any(AccountBookBankplusDTO.class))).thenReturn(accountBookBankplusDTO);

        Mockito.when(accountBalanceService.create(any(AccountBalanceDTO.class))).thenReturn(accountBalanceDTO);
        baseStockService.doUpdateBankplusBCCS(flagStockDTO, stockTransDTO);

        Assert.assertNotNull(accountBookBankplusDTO);
        Assert.assertNotNull(accountBalanceDTO);
    }

    @Test
    public void testDoUpdateBankplusBCCS_5() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setBankplusStatus(null);
        stockTransDTO.setAccountBalanceId(null);
        stockTransDTO.setAccountBalanceId(null);
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setUpdateAccountBalance(false);

        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();

        baseStockService.doUpdateBankplusBCCS(flagStockDTO, stockTransDTO);

    }

    @Test
    public void testDoUpdateBankplusBCCS_6() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setBankplusStatus(null);
        stockTransDTO.setAccountBalanceId(2L);
        stockTransDTO.setAccountBalanceId(null);
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setUpdateAccountBalance(false);

        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();

        baseStockService.doUpdateBankplusBCCS(flagStockDTO, stockTransDTO);
    }
}