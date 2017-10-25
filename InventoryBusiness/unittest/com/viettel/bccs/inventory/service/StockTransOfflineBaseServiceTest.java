package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockTrans;
import com.viettel.bccs.inventory.service.BaseStock.BaseStockService;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.DbUtil;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.persistence.EntityManager;
import java.util.Date;

import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author DatLT
 * @date 15/10/2017
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({DbUtil.class, StockTransOfflineBaseService.class, BaseStockService.class, DateUtil.class})
public class StockTransOfflineBaseServiceTest {
    @InjectMocks
    StockTransOfflineBaseService stockTransOfflineBaseService;
    @Mock
    private EntityManager em;
    @Mock
    private StockTransActionService stockTransActionService;
    @Mock
    private PartnerContractService partnerContractService;
    @Mock
    private StockTransOfflineService stockTransOfflineService;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransOfflineBaseService.processExpStockOffine
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testProcessExpStockOffine_1() throws Exception {
        StockTrans stockTrans = new StockTrans();
        stockTrans.setStockTransId(1L);
        StockTransActionDTO stockTransActionDTO = mock(StockTransActionDTO.class);

        when(stockTransActionService.getStockTransActionByIdAndStatus(anyLong(), anyList())).thenReturn(stockTransActionDTO);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        when(stockTransActionDTO.getStatus()).thenReturn("5");
        when(partnerContractService.findByStockTransID(anyLong())).thenReturn(null);
        stockTransOfflineBaseService.processExpStockOffine(stockTrans);
    }

    @Test
    public void testProcessExpStockOffine_2() throws Exception {
        StockTrans stockTrans = new StockTrans();
        stockTrans.setStockTransId(1L);
        StockTransActionDTO stockTransActionDTO = mock(StockTransActionDTO.class);
        PartnerContractDTO partnerContractDTO = new PartnerContractDTO();

        when(stockTransActionService.getStockTransActionByIdAndStatus(anyLong(), anyList())).thenReturn(stockTransActionDTO);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        when(stockTransActionDTO.getStatus()).thenReturn("5");
        when(partnerContractService.findByStockTransID(anyLong())).thenReturn(partnerContractDTO);
        stockTransOfflineBaseService.processExpStockOffine(stockTrans);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransOfflineBaseService.ImportStock
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testImportStock_1() throws Exception {
        StockTrans stockTrans = new StockTrans();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        SendSmsDTO sendSmsDTO = new SendSmsDTO();

        when(stockTransOfflineService.getStockTransOfflineById(anyLong())).thenReturn(null);
        stockTransOfflineBaseService.ImportStock(stockTrans, stockTransActionDTO, sendSmsDTO);
    }

    @Test
    public void testImportStock_2() throws Exception {
        StockTrans stockTrans = new StockTrans();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        SendSmsDTO sendSmsDTO = new SendSmsDTO();
        StockTransOfflineDTO stockTransOfflineDTO = new StockTransOfflineDTO();

        when(stockTransOfflineService.getStockTransOfflineById(anyLong())).thenReturn(stockTransOfflineDTO);
        stockTransOfflineBaseService.ImportStock(stockTrans, stockTransActionDTO, sendSmsDTO);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransOfflineBaseService.doPrepareData
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testDoPrepareData_1() throws Exception {
        stockTransOfflineBaseService.doPrepareData(new StockTransDTO(), new StockTransActionDTO(), new FlagStockDTO());
    }
    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransOfflineBaseService.doPrepareData
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testDoValidate_1() throws Exception {
        stockTransOfflineBaseService.doPrepareData(new StockTransDTO(), new StockTransActionDTO(), new FlagStockDTO());
    }
}