package com.viettel.bccs.inventory.service.BaseStock;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.dto.FlagStockDTO;
import com.viettel.bccs.inventory.dto.StockTransActionDTO;
import com.viettel.bccs.inventory.dto.StockTransDTO;
import com.viettel.bccs.inventory.dto.StockTransDetailDTO;
import com.viettel.bccs.inventory.service.ProductOfferingService;
import com.viettel.fw.common.util.DataUtil;
import org.junit.Assert;
import org.junit.Test;
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

/**
 * Created by TruNV on 8/30/2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({DataUtil.class, BaseStockService.class})
@PowerMockIgnore("javax.management.*")
public class BaseStockDemoExportServiceTest {

    @InjectMocks
    private BaseStockDemoExportService baseStockDemoExportService;

    @Mock
    private BaseValidateService baseValidateService;

    @Mock
    private ProductOfferingService productOfferingService;

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockDemoExportService.doPrepareData method ------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoPrepareData_1() throws Exception {
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        baseStockDemoExportService.doPrepareData(stockTransDTO, stockTransActionDTO, flagStockDTO);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockDemoExportService.doValidate method ---------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoValidate_1() throws Exception {
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();

        Mockito.doNothing().when(baseValidateService).doInputSerialValidate(any(), any());
        baseStockDemoExportService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    /** -------------------------------------------------------------------- **/
    /** -- Test for baseStockDemoExportService.doSaveStockTransAction method **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoSaveStockTransAction_1() throws Exception {
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransDTO.setProcessOffline("1");
        baseStockDemoExportService.doSaveStockTransAction(stockTransDTO, stockTransActionDTO);
    }

}