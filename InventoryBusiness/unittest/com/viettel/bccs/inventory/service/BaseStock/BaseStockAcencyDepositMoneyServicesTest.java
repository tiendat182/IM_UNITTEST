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

/**
 * Created by TruNV on 9/2/2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({BaseStockAcencyDepositMoneyServices.class, BaseStockService.class})
public class BaseStockAcencyDepositMoneyServicesTest {

    @InjectMocks
    private BaseStockAcencyDepositMoneyServices baseStockAcencyDepositMoneyServices;

    @Before
    public void initMocks() throws NoSuchFieldException {
        MockitoAnnotations.initMocks( this );
    }

    /** -------------------------------------------------------------------- **/
    /** -- Test for baseStockAcencyDepositMoneyServices.doPrepareData method **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void doPrepareData() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        baseStockAcencyDepositMoneyServices.doPrepareData(stockTransDTO, stockTransActionDTO, flagStockDTO);
    }

    @Test
    public void doValidate() throws Exception {

    }

    /** -------------------------------------------------------------------- **/
    /** Test for baseStockAcencyDepositMoneyServices.doSaveStockTransAction method -- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void doSaveStockTransAction() throws Exception {
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        baseStockAcencyDepositMoneyServices.doSaveStockTransAction(stockTransDTO, stockTransActionDTO);
    }

    @Test
    public void doSaveStockTransDetail() throws Exception {
    }

    @Test
    public void doSaveStockTransSerial() throws Exception {
    }

    @Test
    public void doSaveStockTotal() throws Exception {
    }

    @Test
    public void doSignVoffice() throws Exception {
    }

    @Test
    public void doSyncLogistic() throws Exception {
    }

    @Test
    public void doIncreaseStockNum() throws Exception {
    }

    @Test
    public void updateStockNumShop() throws Exception {
    }

    @Test
    public void doSaveStockTotalAudit() throws Exception {
    }

    @Test
    public void doSaveExchangeStockTrans() throws Exception {
    }

}