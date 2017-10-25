package com.viettel.bccs.inventory.service.BaseStock;

import com.google.common.collect.Lists;
import com.opensymphony.xwork2.inject.Inject;
import com.sun.faces.facelets.util.ReflectionUtil;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.FlagStockDTO;
import com.viettel.bccs.inventory.dto.StockTransActionDTO;
import com.viettel.bccs.inventory.dto.StockTransDTO;
import com.viettel.bccs.inventory.dto.StockTransDetailDTO;
import com.viettel.bccs.inventory.service.StockTransActionService;
import com.viettel.bccs.inventory.service.StockTransDetailService;
import com.viettel.bccs.inventory.service.StockTransService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.GetTextFromBundleHelper;
import com.viettel.fw.common.util.RequiredRoleMap;
import org.codehaus.jackson.map.Serializers;
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
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.mock;
import static sun.reflect.misc.FieldUtil.getField;

/**
 * Created by TruNV on 9/2/2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({GetTextFromBundleHelper.class, BaseStockCancelTransService.class, BaseStockService.class})
public class BaseStockCancelTransServiceTest {

    @InjectMocks
    private BaseStockCancelTransService baseStockCancelTransService;

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

    @Before
    public void initMocks() throws NoSuchFieldException {
        MockitoAnnotations.initMocks(this);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockCancelTransService.doPrepareData method ------ **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoPrepareData_1() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setRegionStockTransId(1L);
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        StockTransDTO regionStockTransDTO = new StockTransDTO();
        regionStockTransDTO.setStatus("2");
        Mockito.doReturn(regionStockTransDTO).when(stockTransService).findOne(any());
        baseStockCancelTransService.doPrepareData(stockTransDTO, stockTransActionDTO, flagStockDTO);
    }

    @Test
    public void testDoPrepareData_2() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setRegionStockTransId(1L);
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        StockTransDTO regionStockTransDTO = new StockTransDTO();
        regionStockTransDTO.setStatus("2");
        Mockito.doReturn(null).when(stockTransService).findOne(any());
        baseStockCancelTransService.doPrepareData(stockTransDTO, stockTransActionDTO, flagStockDTO);
    }

    @Test
    public void testDoPrepareData_3() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setRegionStockTransId(null);
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        StockTransDTO regionStockTransDTO = new StockTransDTO();
        regionStockTransDTO.setStatus("2");
        Mockito.doReturn(null).when(stockTransService).findOne(any());
        baseStockCancelTransService.doPrepareData(stockTransDTO, stockTransActionDTO, flagStockDTO);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseStockCancelTransService.doValidate method --------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoValidate_1() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setStatus("2");
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        baseStockCancelTransService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    @Test
    public void testDoValidate_2() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setNote("2");
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        baseStockCancelTransService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    @Test
    public void testDoValidate_3() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setNote("2");
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setStatus("2");
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();

        StockTransDTO oldStockTransDTO = new StockTransDTO();
        Mockito.doReturn(null).when(stockTransService).findOne(any());

        baseStockCancelTransService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    @Test
    public void testDoValidate_4() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setNote("2");
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setStatus("2");
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();

        StockTransDTO oldStockTransDTO = new StockTransDTO();
        oldStockTransDTO.setStatus("2");
        Mockito.doReturn(oldStockTransDTO).when(stockTransService).findOne(any());

        StockTransActionDTO stockTransActionDTO1 = new StockTransActionDTO();
        Mockito.doReturn(stockTransActionDTO1).when(stockTransActionService).findOne(any());
        baseStockCancelTransService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    @Test
    public void testDoValidate_5() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setNote("2");
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setStatus("2");
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();

        StockTransDTO oldStockTransDTO = new StockTransDTO();
        oldStockTransDTO.setStatus("2");
        Mockito.doReturn(oldStockTransDTO).when(stockTransService).findOne(any());

        StockTransActionDTO stockTransActionDTO1 = new StockTransActionDTO();
        stockTransActionDTO1.setStatus("2");
        Mockito.doReturn(stockTransActionDTO1).when(stockTransActionService).findOne(any());

        List<StockTransDetailDTO> lst = Lists.newArrayList();
        Mockito.doReturn(lst).when(stockTransDetailService).findByFilter(any());
        baseStockCancelTransService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    @Test
    public void testDoValidate_6() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setNote("2");
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setStatus("2");
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();

        StockTransDTO oldStockTransDTO = new StockTransDTO();
        oldStockTransDTO.setStatus("3");
        Mockito.doReturn(oldStockTransDTO).when(stockTransService).findOne(any());

        StockTransActionDTO stockTransActionDTO1 = new StockTransActionDTO();
        stockTransActionDTO1.setStatus("2");
        Mockito.doReturn(stockTransActionDTO1).when(stockTransActionService).findOne(any());
        StockTransDetailDTO stockTransDetailDTO1 = new StockTransDetailDTO();
        stockTransDetailDTO1.setTableName("2");
        List<StockTransDetailDTO> lst = Lists.newArrayList(stockTransDetailDTO1);
        Mockito.doReturn(lst).when(stockTransDetailService).findByFilter(any());
        Mockito.doNothing().when(baseValidateService).doOrderValidate(any());

        baseStockCancelTransService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    @Test
    public void testDoValidate_7() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setNote("2");
        stockTransDTO.setIsAutoGen(3L);
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setStatus("2");
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();

        StockTransDTO oldStockTransDTO = new StockTransDTO();
        oldStockTransDTO.setStatus("2");
        Mockito.doReturn(oldStockTransDTO).when(stockTransService).findOne(any());

        StockTransActionDTO stockTransActionDTO1 = new StockTransActionDTO();
        stockTransActionDTO1.setStatus("3");
        Mockito.doReturn(stockTransActionDTO1).when(stockTransActionService).findOne(any());
        StockTransDetailDTO stockTransDetailDTO1 = new StockTransDetailDTO();
        stockTransDetailDTO1.setTableName("2");
        List<StockTransDetailDTO> lst = Lists.newArrayList(stockTransDetailDTO1);
        Mockito.doReturn(lst).when(stockTransDetailService).findByFilter(any());
        Mockito.doNothing().when(baseValidateService).doOrderValidate(any());

        baseStockCancelTransService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    @Test
    public void testDoValidate_8() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setNote("2");
        stockTransDTO.setIsAutoGen(3L);
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setStatus("2");
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();

        StockTransDTO oldStockTransDTO = new StockTransDTO();
        oldStockTransDTO.setStatus("1");
        Mockito.doReturn(oldStockTransDTO).when(stockTransService).findOne(any());

        StockTransActionDTO stockTransActionDTO1 = new StockTransActionDTO();
        stockTransActionDTO1.setStatus("3");
        Mockito.doReturn(stockTransActionDTO1).when(stockTransActionService).findOne(any());
        StockTransDetailDTO stockTransDetailDTO1 = new StockTransDetailDTO();
        stockTransDetailDTO1.setTableName("2");
        List<StockTransDetailDTO> lst = Lists.newArrayList(stockTransDetailDTO1);
        Mockito.doReturn(lst).when(stockTransDetailService).findByFilter(any());
        Mockito.doNothing().when(baseValidateService).doOrderValidate(any());

        baseStockCancelTransService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    @Test
    public void testDoValidate_9() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setStatus("2");
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        baseStockCancelTransService.doValidate(null, stockTransActionDTO, lstStockTransDetail);
    }

    @Test
    public void testDoValidate_10() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setNote("2");
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setStatus("2");
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        baseStockCancelTransService.doValidate(stockTransDTO, null, lstStockTransDetail);
    }

    @Test
    public void testDoValidate_11() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setNote("2");
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setStatus("2");
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        baseStockCancelTransService.doValidate(null, null, lstStockTransDetail);
    }

    @Test
    public void testDoValidate_12() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setNote("2");
        stockTransDTO.setIsAutoGen(3L);
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setStatus("2");
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();

        StockTransDTO oldStockTransDTO = new StockTransDTO();
        oldStockTransDTO.setStatus("1");
        Mockito.doReturn(oldStockTransDTO).when(stockTransService).findOne(any());

        StockTransActionDTO stockTransActionDTO1 = new StockTransActionDTO();
        stockTransActionDTO1.setStatus("3");
        Mockito.doReturn(null).when(stockTransActionService).findOne(any());
        StockTransDetailDTO stockTransDetailDTO1 = new StockTransDetailDTO();
        stockTransDetailDTO1.setTableName("2");
        List<StockTransDetailDTO> lst = Lists.newArrayList(stockTransDetailDTO1);
        Mockito.doReturn(lst).when(stockTransDetailService).findByFilter(any());
        Mockito.doNothing().when(baseValidateService).doOrderValidate(any());

        baseStockCancelTransService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    @Test
    public void testDoValidate_13() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setNote("2");
        stockTransDTO.setIsAutoGen(4L);
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setStatus("2");
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();

        StockTransDTO oldStockTransDTO = new StockTransDTO();
        oldStockTransDTO.setStatus("1");
        Mockito.doReturn(oldStockTransDTO).when(stockTransService).findOne(any());

        StockTransActionDTO stockTransActionDTO1 = new StockTransActionDTO();
        stockTransActionDTO1.setStatus("3");
        Mockito.doReturn(stockTransActionDTO1).when(stockTransActionService).findOne(any());
        StockTransDetailDTO stockTransDetailDTO1 = new StockTransDetailDTO();
        stockTransDetailDTO1.setTableName("2");
        List<StockTransDetailDTO> lst = Lists.newArrayList(stockTransDetailDTO1);
        Mockito.doReturn(lst).when(stockTransDetailService).findByFilter(any());
        Mockito.doNothing().when(baseValidateService).doOrderValidate(any());

        baseStockCancelTransService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    /** -------------------------------------------------------------------- **/
    /** -Test for baseStockCancelTransService.doSaveStockTransDetail method  **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoSaveStockTransDetail_1() throws Exception {
    }

    /** -------------------------------------------------------------------- **/
    /** -Test for baseStockCancelTransService.doSaveStockTransSerial method  **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoSaveStockTransSerial_1() throws Exception {

    }

    /** -------------------------------------------------------------------- **/
    /** -Test for baseStockCancelTransService.doSyncLogistic method  ------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoSyncLogistic_1() throws Exception {
        BaseStockService spyTemp = PowerMockito.spy(baseStockService);
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setLogicstic("1");
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetailDTO);
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        setFinalStatic(BaseStockService.class.getDeclaredField("oldStockTransStatus"), "4", spyTemp);

        baseStockCancelTransService.doSyncLogistic(stockTransDTO, lstStockTransDetail, requiredRoleMap);
    }

    /** -------------------------------------------------------------------- **/
    /** -Test for baseStockCancelTransService.doSaveRegionStockTrans method  **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoSaveRegionStockTrans_1() throws Exception {
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setRegionStockId(0L);
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        baseStockCancelTransService.doSaveRegionStockTrans(stockTransDTO, stockTransActionDTO, flagStockDTO, lstStockTransDetail );
    }

    @Test
    public void testDoSaveRegionStockTrans_2() throws Exception {
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setRegionStockId(2L);
        stockTransDTO.setCreateDatetime(Calendar.getInstance().getTime());
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        StockTransActionDTO stockTransActionDTO1 = new StockTransActionDTO();
        StockTransDTO exportDTO = new StockTransDTO();
        BaseStockCancelTransService spy = PowerMockito.spy(baseStockCancelTransService);
        PowerMockito.doReturn(exportDTO).when(spy).doSaveStockTrans(any());
        PowerMockito.doReturn(stockTransActionDTO1).when(spy).doSaveStockTransAction(any(), any());
        spy.doSaveRegionStockTrans(stockTransDTO, stockTransActionDTO, flagStockDTO, lstStockTransDetail );
    }

    private static void setFinalStatic(Field field, Object newValue, Object temp) throws Exception {
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.PROTECTED);
        field.set(temp, newValue);
    }
}