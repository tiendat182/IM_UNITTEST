package com.viettel.bccs.inventory.service.BaseIsdn;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.dto.FlagStockDTO;
import com.viettel.bccs.inventory.dto.StockTransActionDTO;
import com.viettel.bccs.inventory.dto.StockTransDTO;
import com.viettel.bccs.inventory.dto.StockTransDetailDTO;
import com.viettel.bccs.inventory.service.BaseStock.BaseStockService;
import com.viettel.bccs.inventory.service.BaseStock.BaseValidateService;
import com.viettel.fw.common.util.DataUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;

/**
 * Created by TruNV on 9/8/2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({BaseStockService.class})
@PowerMockIgnore("javax.management.*")
public class BaseIsdnNoteServiceTest {

    @InjectMocks
    private BaseIsdnNoteService baseIsdnNoteService;

    @Mock
    private BaseValidateService baseValidateService;

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }


    /** -------------------------------------------------------------------- **/
    /** --- Test for baseIsdnNoteService.doPrepareData method -------------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoPrepareData_1() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        baseIsdnNoteService.doPrepareData(stockTransDTO, stockTransActionDTO, flagStockDTO);

    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseIsdnNoteService.doValidate method ----------------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void doValidate() throws Exception {
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();

        Mockito.doNothing().when(baseValidateService).doOrderValidate(any());
        Mockito.doNothing().when(baseValidateService).doSignVofficeValidate(any());
        baseIsdnNoteService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    @Test
    public void doSaveStockTransSerial() throws Exception {
    }

    @Test
    public void doSaveStockGoods() throws Exception {
    }

    @Test
    public void doSaveRegionStockTrans() throws Exception {
    }

    @Test
    public void doSaveStockTotal() throws Exception {
    }

    @Test
    public void doSyncLogistic() throws Exception {
    }

    @Test
    public void doIncreaseStockNum() throws Exception {
    }

}