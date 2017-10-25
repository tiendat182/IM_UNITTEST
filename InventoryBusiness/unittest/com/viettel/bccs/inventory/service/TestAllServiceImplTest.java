package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.BaseMessageStockTrans;
import com.viettel.bccs.inventory.dto.StockTransActionDTO;
import com.viettel.bccs.inventory.dto.StockTransDTO;
import com.viettel.bccs.sale.service.TestAllService;
import com.viettel.fw.common.util.DbUtil;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author DatLT
 * @date 15/10/2017
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({DbUtil.class, TestAllServiceImpl.class, TestAllService.class})
public class TestAllServiceImplTest {
    @InjectMocks
    TestAllServiceImpl testAllService;
    @Mock
    ExecuteStockTransServiceImpl transService;
    @Mock
    StockTransService stockTransService;
    @Mock
    StockTransActionService stockTransActionService;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method TestAllServiceImpl.test_executeStockTrans
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Test
    public void testTest_executeStockTrans_1() throws Exception {
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        BaseMessageStockTrans msg = new BaseMessageStockTrans() ;

        when(stockTransService.findOne(58591L)).thenReturn(stockTransDTO);
        when( stockTransActionService.findOne(590014L)).thenReturn(stockTransActionDTO);
        when(transService.executeStockTrans(anyString(), any(), any(), any(), any(), any())).thenReturn(msg);
        testAllService.test_executeStockTrans();
    }
}