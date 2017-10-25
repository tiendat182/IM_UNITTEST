package com.viettel.bccs.inventory.service.BaseStock;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.dto.FlagStockDTO;
import com.viettel.bccs.inventory.dto.StockTransActionDTO;
import com.viettel.bccs.inventory.dto.StockTransDTO;
import com.viettel.bccs.inventory.dto.StockTransDetailDTO;
import com.viettel.bccs.inventory.service.StaffService;
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
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;

/**
 * Created by TruNV on 8/26/2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({DataUtil.class})
@PowerMockIgnore("javax.management.*")
public class BaseStockOutNoteServiceTest {

    @InjectMocks
    private BaseStockOutNoteService baseStockOutNoteService;

    @Mock
    private BaseValidateService baseValidateService;

    @Mock
    private StaffService staffService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockOutNoteService.doPrepareData method ---------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoPrepareData_1() throws Exception {
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        baseStockOutNoteService.doPrepareData(stockTransDTO, stockTransActionDTO, flagStockDTO);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockOutNoteService.doValidate method --------------**/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoValidate_1() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        Mockito.when(staffService.validateTransCode(any(), any(), any())).thenReturn(false);
        baseStockOutNoteService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockOutNoteService.doValidate method --------------**/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoValidate_2() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        Mockito.when(staffService.validateTransCode(any(), any(), any())).thenReturn(true);
        Mockito.doNothing().when(baseValidateService).doOrderValidate(any());
        Mockito.doNothing().when(baseValidateService).doSignVofficeValidate(any());
        Mockito.doNothing().when(baseValidateService).doDebitValidate(any(), anyList());
        Mockito.doNothing().when(baseValidateService).doQuantityAvailableValidate(any(), anyList());
        baseStockOutNoteService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockOutNoteService.doSaveStockTransSerial method - **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoSaveStockTransSerial_1() throws Exception {
        baseStockOutNoteService.doSaveStockTransSerial(null, null);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockOutNoteService.doSaveStockGoods method ------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoSaveStockGoods_1() throws Exception {
        baseStockOutNoteService.doSaveStockGoods(null, null, null);
    }

}