package com.viettel.bccs.inventory.service.BaseStock;

import com.viettel.bccs.inventory.dto.FlagStockDTO;
import com.viettel.bccs.inventory.dto.StockTransActionDTO;
import com.viettel.bccs.inventory.dto.StockTransDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;

/**
 * Created by TruNV on 9/2/2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({BaseStockAgentRetrieveCancelService.class, BaseStockService.class})
public class BaseStockAgentRetrieveCancelServiceTest {

    @InjectMocks
    private BaseStockAgentRetrieveCancelService baseStockAgentRetrieveCancelService;

    @Before
    public void initMocks() throws NoSuchFieldException {
        MockitoAnnotations.initMocks(this);
    }

    /** -------------------------------------------------------------------- **/
    /** - Test for baseStockAgentRetrieveCancelService.doPrepareData method  **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoPrepareData_1() throws Exception {
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        baseStockAgentRetrieveCancelService.doPrepareData(stockTransDTO, stockTransActionDTO, flagStockDTO);
    }

    @Test
    public void doValidate() throws Exception {
    }

    @Test
    public void doSaveRegionStockTrans() throws Exception {
    }

    @Test
    public void doSaveStockTransDetail() throws Exception {
    }

    @Test
    public void doSaveStockGoods() throws Exception {
    }

    @Test
    public void doSaveStockTransSerial() throws Exception {
    }

    /** -------------------------------------------------------------------- **/
    /** - Test for baseStockAgentRetrieveCancelService.doSaveStockTransAction method  **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoSaveStockTransAction_1() throws Exception {
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransDTO.setProcessOffline("1");
        baseStockAgentRetrieveCancelService.doSaveStockTransAction(stockTransDTO, stockTransActionDTO);
    }

}