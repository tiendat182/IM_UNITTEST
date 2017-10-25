package com.viettel.bccs.inventory.service.BaseStock;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.dto.FlagStockDTO;
import com.viettel.bccs.inventory.dto.StockTransActionDTO;
import com.viettel.bccs.inventory.dto.StockTransDTO;
import com.viettel.bccs.inventory.dto.StockTransDetailDTO;
import com.viettel.bccs.inventory.service.StaffService;
import com.viettel.bccs.inventory.service.StockTransActionService;
import com.viettel.bccs.inventory.service.StockTransService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.RequiredRoleMap;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.support.membermodification.MemberMatcher;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyLong;

/**
 * Created by TruNV on 8/25/2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({DataUtil.class, BaseStockService.class})
@PowerMockIgnore("javax.management.*")
public class BaseStockInNoteServiceTest {

    @InjectMocks
    private BaseStockInNoteService baseStockInNoteService;

    @Mock
    private StockTransService stockTransService;

    @Mock
    private StockTransActionService stockTransActionService;

    @Mock
    private BaseValidateService baseValidateService;

    @Mock
    private StaffService staffService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockService.doPrepareData method ----------------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoPrepareData_1() throws Exception {
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setActionCode(null);
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        baseStockInNoteService.doPrepareData(stockTransDTO, stockTransActionDTO, flagStockDTO);
    }

    @Test
    public void testDoPrepareData_2() throws Exception {
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setActionCode("2");
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        baseStockInNoteService.doPrepareData(stockTransDTO, stockTransActionDTO, flagStockDTO);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockService.doValidate method -------------------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoValidate_1() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT );

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        lstStockTransDetail.add(stockTransDetailDTO);

        Mockito.doNothing().when(baseValidateService).doOrderValidateCommon(Mockito.any(), Mockito.any());

        stockTransActionDTO.setActionCode("123456789012345678901234567890123456789012345678901234567890");
        baseStockInNoteService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    @Test
    public void testDoValidate_2() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT );

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setActionCode(null);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        lstStockTransDetail.add(stockTransDetailDTO);

        Mockito.doNothing().when(baseValidateService).doOrderValidateCommon(any(), any());
        Mockito.when(staffService.validateTransCode(any(), any(), any())).thenReturn(false);

        baseStockInNoteService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    @Test
    public void testDoValidate_3() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setActionCode("123");
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        lstStockTransDetail.add(stockTransDetailDTO);

        Mockito.doNothing().when(baseValidateService).doOrderValidateCommon(any(), any());
        Mockito.when(staffService.validateTransCode(any(), any(), any())).thenReturn(true);

        StockTransDTO oldStockTransDTO = new StockTransDTO();
        Mockito.when(stockTransService.findOne(any())).thenReturn(null);

        baseStockInNoteService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    @Test
    public void testDoValidate_4() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setActionCode("123");
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        lstStockTransDetail.add(stockTransDetailDTO);

        Mockito.doNothing().when(baseValidateService).doOrderValidateCommon(any(), any());
        Mockito.when(staffService.validateTransCode(any(), any(), any())).thenReturn(true);

        StockTransDTO oldStockTransDTO = new StockTransDTO();
        oldStockTransDTO.setStatus("1");
        Mockito.when(stockTransService.findOne(any())).thenReturn(oldStockTransDTO);

        baseStockInNoteService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    @Test
    public void testDoValidate_5() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setActionCode("123");
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        lstStockTransDetail.add(stockTransDetailDTO);

        Mockito.doNothing().when(baseValidateService).doOrderValidateCommon(any(), any());
        Mockito.when(staffService.validateTransCode(any(), any(), any())).thenReturn(true);

        StockTransDTO oldStockTransDTO = new StockTransDTO();
        oldStockTransDTO.setStatus("4");
        Mockito.when(stockTransService.findOne(any())).thenReturn(oldStockTransDTO);

        baseStockInNoteService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    @Test
    public void testDoValidate_6() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setActionCode("123");
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        lstStockTransDetail.add(stockTransDetailDTO);

        Mockito.doNothing().when(baseValidateService).doOrderValidateCommon(any(), any());
        Mockito.when(staffService.validateTransCode(any(), any(), any())).thenReturn(true);

        StockTransDTO oldStockTransDTO = new StockTransDTO();
        oldStockTransDTO.setStatus("3");
        Mockito.when(stockTransService.findOne(any())).thenReturn(oldStockTransDTO);

        baseStockInNoteService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    @Test
    public void testDoValidate_7() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setActionCode("123");
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        lstStockTransDetail.add(stockTransDetailDTO);

        Mockito.doNothing().when(baseValidateService).doOrderValidateCommon(any(), any());
        Mockito.when(staffService.validateTransCode(any(), any(), any())).thenReturn(true);

        StockTransDTO oldStockTransDTO = new StockTransDTO();
        oldStockTransDTO.setStatus("3");
        Mockito.when(stockTransService.findOne(any())).thenReturn(oldStockTransDTO);

        baseStockInNoteService.doValidate(stockTransDTO, stockTransActionDTO, null);
    }

    @Test
    public void testDoValidate_8() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setActionCode("123");
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setTableName("2");
        lstStockTransDetail.add(stockTransDetailDTO);

        Mockito.doNothing().when(baseValidateService).doOrderValidateCommon(any(), any());
        Mockito.when(staffService.validateTransCode(any(), any(), any())).thenReturn(true);

        StockTransDTO oldStockTransDTO = new StockTransDTO();
        oldStockTransDTO.setStatus("3");
        Mockito.when(stockTransService.findOne(any())).thenReturn(oldStockTransDTO);

        Mockito.doNothing().when(baseValidateService).doSignVofficeValidate(any());
        Mockito.doNothing().when(baseValidateService).doDebitValidate(any(), anyList());

        baseStockInNoteService.doValidate(stockTransDTO, stockTransActionDTO, null);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockService.doSaveStockTrans method -------------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoSaveStockTrans_1() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransId(2L);
        stockTransDTO.setStatus("2");
        StockTransDTO stockTransToUpdate = new StockTransDTO();
        stockTransToUpdate.setStatus("2");
        Mockito.when(stockTransService.findOneLock(anyLong())).thenReturn(stockTransToUpdate);

        baseStockInNoteService.doSaveStockTrans(stockTransDTO);
    }

    @Test
    public void testDoSaveStockTrans_2() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransId(2L);
        stockTransDTO.setImportReasonId(0L);
        stockTransDTO.setStatus("2");
        StockTransDTO stockTransToUpdate = new StockTransDTO();
        stockTransToUpdate.setStatus("3");
        Mockito.when(stockTransService.findOneLock(anyLong())).thenReturn(stockTransToUpdate);
        Mockito.when(stockTransService.save(any())).thenReturn(stockTransDTO);

        baseStockInNoteService.doSaveStockTrans(stockTransDTO);
    }

    @Test
    public void testDoSaveStockTrans_3() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransId(null);
        StockTransDTO stockTransToUpdate = new StockTransDTO();
        stockTransToUpdate.setStatus("1");
        Mockito.when(stockTransService.findOneLock(anyLong())).thenReturn(stockTransDTO);
        Mockito.when(stockTransService.save(any())).thenReturn(stockTransDTO);

        baseStockInNoteService.doSaveStockTrans(stockTransDTO);
    }

    @Test
    public void testDoSaveStockTrans_4() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransId(2L);
        stockTransDTO.setImportReasonId(2L);
        stockTransDTO.setStatus("2");
        StockTransDTO stockTransToUpdate = new StockTransDTO();
        stockTransToUpdate.setStatus("3");
        Mockito.when(stockTransService.findOneLock(anyLong())).thenReturn(stockTransToUpdate);
        Mockito.when(stockTransService.save(any())).thenReturn(stockTransDTO);

        baseStockInNoteService.doSaveStockTrans(stockTransDTO);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockService.doSaveStockTransDetail method -------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoSaveStockTransDetail_1() throws Exception {

        baseStockInNoteService.doSaveStockTransDetail(null, null);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockService.doSaveStockTransSerial method -------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoSaveStockTransSerial_1() throws Exception {

        baseStockInNoteService.doSaveStockTransSerial(null, null);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockService.doSaveStockTotal method -------------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoSaveStockTotal_1() throws Exception {

        baseStockInNoteService.doSaveStockTotal(null, null, null, null);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockService.doSaveStockGoods method -------------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoSaveStockGoods_1() throws Exception {

        baseStockInNoteService.doSaveStockGoods(null, null, null);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockService.doSignVoffice method ----------------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoSignVoffice_1() throws Exception {
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setSignVoffice(false);
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        PowerMockito.suppress( MemberMatcher.methodsDeclaredIn(BaseStockService.class));
        BaseStockInNoteService spyService = PowerMockito.spy(baseStockInNoteService);
        PowerMockito.doNothing().when(spyService, "doSignVoffice", any(), any(), any(), any());
        baseStockInNoteService.doSignVoffice(stockTransDTO, stockTransActionDTO, requiredRoleMap, flagStockDTO);
    }

    @Test
    public void testDoSignVoffice_2() throws Exception {
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setSignVoffice(true);
        stockTransDTO.setExchangeStockId(2L);
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        StockTransActionDTO stockTransActionExchange = new StockTransActionDTO();
        Mockito.when(stockTransActionService.findOne(any())).thenReturn(stockTransActionExchange);

        PowerMockito.suppress( MemberMatcher.methodsDeclaredIn(BaseStockService.class));
        BaseStockInNoteService spyService = PowerMockito.spy(baseStockInNoteService);
        PowerMockito.doNothing().when(spyService, "doSignVoffice", any(), any(), any(), any());
        baseStockInNoteService.doSignVoffice(stockTransDTO, stockTransActionDTO, requiredRoleMap, flagStockDTO);
    }

    @Test
    public void testDoSignVoffice_3() throws Exception {
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setSignVoffice(true);
        stockTransDTO.setExchangeStockId(2L);
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        //StockTransActionDTO stockTransActionExchange = new StockTransActionDTO();
        Mockito.when(stockTransActionService.findOne(any())).thenReturn(null);

        PowerMockito.suppress( MemberMatcher.methodsDeclaredIn(BaseStockService.class));
        BaseStockInNoteService spyService = PowerMockito.spy(baseStockInNoteService);
        PowerMockito.doNothing().when(spyService, "doSignVoffice", any(), any(), any(), any());
        baseStockInNoteService.doSignVoffice(stockTransDTO, stockTransActionDTO, requiredRoleMap, flagStockDTO);
    }

    @Test
    public void testDoSignVoffice_4() throws Exception {
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setSignVoffice(true);
        stockTransDTO.setExchangeStockId(0L);
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        //StockTransActionDTO stockTransActionExchange = new StockTransActionDTO();
        Mockito.when(stockTransActionService.findOne(any())).thenReturn(null);

        PowerMockito.suppress( MemberMatcher.methodsDeclaredIn(BaseStockService.class));
        BaseStockInNoteService spyService = PowerMockito.spy(baseStockInNoteService);
        PowerMockito.doNothing().when(spyService, "doSignVoffice", any(), any(), any(), any());
        baseStockInNoteService.doSignVoffice(stockTransDTO, stockTransActionDTO, requiredRoleMap, flagStockDTO);
    }

}