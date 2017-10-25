package com.viettel.bccs.inventory.service.BaseStock;

import com.google.common.collect.Lists;
import com.viettel.bccs.im1.dto.StockTransIm1DTO;
import com.viettel.bccs.im1.service.StockTransIm1Service;
import com.viettel.bccs.inventory.dto.OptionSetValueDTO;
import com.viettel.bccs.inventory.dto.StockTransActionDTO;
import com.viettel.bccs.inventory.dto.StockTransDTO;
import com.viettel.bccs.inventory.dto.StockTransDetailDTO;
import com.viettel.bccs.inventory.service.OptionSetValueService;
import com.viettel.bccs.inventory.service.StockTransService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.ErrorCode;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class BaseStockExpNoteAgentServiceTest {

    @InjectMocks
    private BaseStockExpNoteAgentService baseStockExpNoteAgentService;

    @Mock
    private StockTransService stockTransService;

    @Mock
    private OptionSetValueService optionSetValueService;

    @Mock
    private StockTransIm1Service stockTransServiceIm1;

    @Mock
    private BaseValidateService baseValidateService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockExpNoteAgentService.doSaveStockTrans method -- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testDoSaveStockTrans_1() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransId(1L);

        StockTransDTO stockTransToUpdate = new StockTransDTO();
        stockTransToUpdate.setStatus("1");

        Mockito.when(stockTransService.findOneLock(Mockito.anyLong())).thenReturn(null);
        baseStockExpNoteAgentService.doSaveStockTrans(stockTransDTO);
    }

    @Test
    public void testDoSaveStockTrans_2() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransId(1L);
        stockTransDTO.setStatus("1");

        StockTransDTO stockTransToUpdate = new StockTransDTO();
        stockTransToUpdate.setStatus("1");

        Mockito.when(stockTransService.findOneLock(Mockito.anyLong())).thenReturn(stockTransToUpdate);
        baseStockExpNoteAgentService.doSaveStockTrans(stockTransDTO);
    }

    @Test
    public void testDoSaveStockTrans_3() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransId(1L);
        stockTransDTO.setStatus("6");

        StockTransDTO stockTransToUpdate = new StockTransDTO();
        stockTransToUpdate.setStatus("2");

        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue("1");
        lstConfigEnableBccs1.add(optionSetValueDTO);

        Mockito.when(stockTransService.findOneLock(Mockito.anyLong())).thenReturn(stockTransToUpdate);
        Mockito.when(optionSetValueService.getByOptionSetCode(Mockito.anyString())).thenReturn(lstConfigEnableBccs1);

        StockTransIm1DTO stockTransToUpdateIM1 = new StockTransIm1DTO();
        stockTransToUpdateIM1.setStockTransStatus(3L);
        Mockito.when(stockTransServiceIm1.findOneLock(Mockito.anyLong())).thenReturn(stockTransToUpdateIM1);

        baseStockExpNoteAgentService.doSaveStockTrans(stockTransDTO);
    }

    @Test
    public void testDoSaveStockTrans_4() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransId(1L);
        stockTransDTO.setStatus("6");
        stockTransDTO.setProcessOffline("1");
        stockTransDTO.setDepositStatus("1");
        stockTransDTO.setPayStatus("1");
        stockTransDTO.setBankplusStatus(1L);
        stockTransDTO.setCreateUserIpAdress("1");
        stockTransDTO.setDepositPrice(1L);

        StockTransDTO stockTransToUpdate = new StockTransDTO();
        stockTransToUpdate.setStatus("2");

        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue("1");
        lstConfigEnableBccs1.add(optionSetValueDTO);

        Mockito.when(stockTransService.findOneLock(Mockito.anyLong())).thenReturn(stockTransToUpdate);
        Mockito.when(optionSetValueService.getByOptionSetCode(Mockito.anyString())).thenReturn(lstConfigEnableBccs1);

        StockTransIm1DTO stockTransToUpdateIM1 = new StockTransIm1DTO();
        stockTransToUpdateIM1.setStockTransStatus(1L);
        Mockito.when(stockTransServiceIm1.findOneLock(Mockito.anyLong())).thenReturn(stockTransToUpdateIM1);
        Mockito.when(stockTransServiceIm1.updateStatusStockTrans(Mockito.any(StockTransIm1DTO.class))).thenReturn(1);
        Mockito.when(stockTransService.save(Mockito.any(StockTransDTO.class))).thenReturn(stockTransDTO);

        baseStockExpNoteAgentService.doSaveStockTrans(stockTransDTO);
        Assert.assertNotNull(stockTransDTO);
    }

    @Test
    public void testDoSaveStockTrans_5() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransId(null);
        stockTransDTO.setStockTransType(null);

        StockTransDTO stockTransDTOUpd = new StockTransDTO();
        stockTransDTOUpd.setStockTransId(1L);
        Mockito.when(stockTransService.save(Mockito.any(StockTransDTO.class))).thenReturn(stockTransDTOUpd);

        baseStockExpNoteAgentService.doSaveStockTrans(stockTransDTO);
        Assert.assertNotNull(stockTransDTOUpd);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockExpNoteAgentService.doValidate method -------- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testDoValidate_1() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setPayStatus(null);

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        lstStockTransDetail.add(stockTransDetailDTO);

        Mockito.doNothing().when(baseValidateService).doSignVofficeValidate(Mockito.any(StockTransDTO.class));
        Mockito.when(baseValidateService.doAccountBankplusValidate(Mockito.any(StockTransDTO.class))).thenReturn(false);
        Mockito.doNothing().when(baseValidateService).doDebitValidate(Mockito.any(StockTransDTO.class), Mockito.anyList());
        Mockito.doNothing().when(baseValidateService).doCurrentQuantityValidate(Mockito.any(StockTransDTO.class), Mockito.anyList());

        BaseStockExpNoteAgentService spyTemp = Mockito.spy(baseStockExpNoteAgentService);

        Mockito.doNothing().when(spyTemp).exportValidate(stockTransDTO, lstStockTransDetail);
        spyTemp.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }
}