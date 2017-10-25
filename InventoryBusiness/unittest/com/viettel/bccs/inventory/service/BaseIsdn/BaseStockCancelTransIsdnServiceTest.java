package com.viettel.bccs.inventory.service.BaseIsdn;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.FlagStockDTO;
import com.viettel.bccs.inventory.dto.StockTransActionDTO;
import com.viettel.bccs.inventory.dto.StockTransDTO;
import com.viettel.bccs.inventory.dto.StockTransDetailDTO;
import com.viettel.bccs.inventory.service.BaseStock.BaseStockService;
import com.viettel.bccs.inventory.service.BaseStock.BaseValidateService;
import com.viettel.bccs.inventory.service.StockTransActionService;
import com.viettel.bccs.inventory.service.StockTransDetailService;
import com.viettel.bccs.inventory.service.StockTransService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.RequiredRoleMap;
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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DbUtil.class,BaseStockCancelTransIsdnService.class, BaseStockService.class})
@PowerMockIgnore("javax.management.*")
public class BaseStockCancelTransIsdnServiceTest {

    @InjectMocks
    private BaseStockCancelTransIsdnService expService;

    @Mock
    private BaseStockService baseStockService;

    @Mock
    private BaseValidateService baseValidateService;

    @Mock
    private StockTransService stockTransService;

    @Mock
    private StockTransActionService stockTransActionService;

    @Mock
    private StockTransDetailService stockTransDetailService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    /** --------------------------------------------------------------------- **/
    /** --- Test for BaseStockCancelTransIsdnService.doPrepareData method --- **/
    /** --------------------------------------------------------------------- **/
    @Test
    public void doPrepareData() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        expService.doPrepareData(stockTransDTO, stockTransActionDTO, flagStockDTO);
    }

    /** --------------------------------------------------------------------- **/
    /** --- Test for BaseStockCancelTransIsdnService.doValidate method --- **/
    /** --------------------------------------------------------------------- **/
    @Test
    public void testDoValidate_1() throws Exception {
        expectedException.expect(LogicException.class);
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransId(1L);
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        List< StockTransDetailDTO > lstStockTransDetail = new ArrayList<>();

        Mockito.when(stockTransService.findOne(1L)).thenReturn(null);

        expService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    @Test
    public void testDoValidate_2() throws Exception {

        expectedException.expect(LogicException.class);
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransId(1L);
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        List< StockTransDetailDTO > lstStockTransDetail = new ArrayList<>();

        StockTransDTO stockCheck = new StockTransDTO();
        stockCheck.setStatus(null);

        Mockito.when(stockTransService.findOne(1L)).thenReturn(stockCheck);

        expService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    @Test
    public void testDoValidate_3() throws Exception {

        expectedException.expect(LogicException.class);
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransId(1L);
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        List< StockTransDetailDTO > lstStockTransDetail = new ArrayList<>();

        StockTransDTO stockCheck = new StockTransDTO();
        stockCheck.setStatus("status");

        Mockito.when(stockTransService.findOne(1L)).thenReturn(stockCheck);

        expService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    @Test
    public void testDoValidate_4() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransId(1L);
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        List< StockTransDetailDTO > lstStockTransDetail = new ArrayList<>();

        StockTransDTO stockCheck = new StockTransDTO();
        stockCheck.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_ORDER);

        Mockito.when(stockTransService.findOne(1L)).thenReturn(stockCheck);

        expService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    @Test
    public void testDoValidate_5() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransId(1L);
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        List< StockTransDetailDTO > lstStockTransDetail = new ArrayList<>();

        StockTransDTO stockCheck = new StockTransDTO();
        stockCheck.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);

        Mockito.when(stockTransService.findOne(1L)).thenReturn(stockCheck);

        expService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    /** --------------------------------------------------------------------- **/
    /** --- Test for BaseStockCancelTransIsdnService.doSaveRegionStockTrans method --- **/
    /** --------------------------------------------------------------------- **/
    @Test
    public void testDoSaveRegionStockTrans_1() throws Exception {
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        List<StockTransDetailDTO> lstStockTransDetail = new ArrayList<>();

        expService.doSaveRegionStockTrans(stockTransDTO, stockTransActionDTO, flagStockDTO, lstStockTransDetail);
    }

    /** --------------------------------------------------------------------- **/
    /** --- Test for BaseStockCancelTransIsdnService.doSaveStockGoods method --- **/
    /** --------------------------------------------------------------------- **/
    @Test
    public void testDoSaveStockGoods() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        List<StockTransDetailDTO> lstStockTransDetail = new ArrayList<>();
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        expService.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    /** --------------------------------------------------------------------- **/
    /** --- Test for BaseStockCancelTransIsdnService.doSaveStockTransDetail method --- **/
    /** --------------------------------------------------------------------- **/
    @Test
    public void testDoSaveStockTransDetail() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        List<StockTransDetailDTO> lstStockTransDetail = new ArrayList<>();

        expService.doSaveStockTransDetail(stockTransDTO, lstStockTransDetail);
    }

    /** --------------------------------------------------------------------- **/
    /** --- Test for BaseStockCancelTransIsdnService.doSaveStockTransSerial method --- **/
    /** --------------------------------------------------------------------- **/
    @Test
    public void testDoSaveStockTransSerial() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        List<StockTransDetailDTO> lstStockTransDetail = new ArrayList<>();

        expService.doSaveStockTransSerial(stockTransDTO, lstStockTransDetail);
    }

    /** --------------------------------------------------------------------- **/
    /** --- Test for BaseStockCancelTransIsdnService.doSaveStockTotal method --- **/
    /** --------------------------------------------------------------------- **/
    @Test
    public void testDoSaveStockTotal() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        List<StockTransDetailDTO> lstStockTransDetailDTO = new ArrayList<>();
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();

        expService.doSaveStockTotal(stockTransDTO, lstStockTransDetailDTO, flagStockDTO, stockTransActionDTO);
    }

    /** --------------------------------------------------------------------- **/
    /** --- Test for BaseStockCancelTransIsdnService.doSyncLogistic method --- **/
    /** --------------------------------------------------------------------- **/
    @Test
    public void testDoSyncLogistic_1() throws Exception {
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setLogicstic("test");
        List<StockTransDetailDTO> lstStockTransDetail = new ArrayList<>();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        expService.doSyncLogistic(stockTransDTO, lstStockTransDetail, requiredRoleMap);
    }

    @Test
    public void testDoSyncLogistic_2() throws Exception {
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setLogicstic(Const.STOCK_TRANS.IS_LOGISTIC);
        List<StockTransDetailDTO> lstStockTransDetail = new ArrayList<>();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        BaseStockCancelTransIsdnService spyTemp = PowerMockito.spy(expService);
        setFinalStatic(BaseStockService.class.getDeclaredField("oldStockTransStatus"), "4", spyTemp);

        spyTemp.doSyncLogistic(stockTransDTO, lstStockTransDetail, requiredRoleMap);
    }

    private static void setFinalStatic(Field field, Object newValue, Object temp) throws Exception {
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.PROTECTED);
        field.set(temp, newValue);
    }
}