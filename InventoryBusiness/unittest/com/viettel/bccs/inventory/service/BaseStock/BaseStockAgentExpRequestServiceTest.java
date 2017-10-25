package com.viettel.bccs.inventory.service.BaseStock;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.dto.StockOrderAgentDTO;
import com.viettel.bccs.inventory.dto.StockTransActionDTO;
import com.viettel.bccs.inventory.dto.StockTransDTO;
import com.viettel.bccs.inventory.dto.StockTransDetailDTO;
import com.viettel.bccs.inventory.service.StockOrderAgentService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.RequiredRoleMap;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.support.membermodification.MemberMatcher;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.persistence.EntityManager;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;

/**
 * Created by TruNV on 8/30/2017.
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({DbUtil.class, BaseStockService.class})
public class BaseStockAgentExpRequestServiceTest {

    @InjectMocks
    private BaseStockAgentExpRequestService baseStockAgentExpRequestService;

    @Mock
    private StockOrderAgentService stockOrderAgentService;

    @Mock
    private EntityManager em;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    /** -------------------------------------------------------------------- **/
    /** ---Test for baseStockAgentExpRequestService.executeStockTrans method **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testExecuteStockTrans_1() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( "mn.stock.agency.request.not.exist");

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockOrderAgentId(null);
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        StockTransDetailDTO stockTransDetailDTO1 = new StockTransDetailDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetailDTO, stockTransDetailDTO1);
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        StockTransActionDTO stockTransActionDTO1 = new StockTransActionDTO();
        BaseStockAgentExpRequestService spyTemp = PowerMockito.spy( baseStockAgentExpRequestService );
        PowerMockito.suppress( MemberMatcher.methodsDeclaredIn(BaseStockService.class));
        PowerMockito.doReturn(stockTransActionDTO1).when(spyTemp, "executeStockTrans", any(), any(), any(), any());

        StockOrderAgentDTO stockOrderAgentDTO = new StockOrderAgentDTO();
        Mockito.doReturn(stockOrderAgentDTO).when(stockOrderAgentService).findOne(any());
        Mockito.doReturn(stockOrderAgentDTO).when(stockOrderAgentService).update(any());
        baseStockAgentExpRequestService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetail, requiredRoleMap);
    }

    @Test
    public void testExecuteStockTrans_2() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( "mn.stock.agency.request.not.execute");

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockOrderAgentId(2L);
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        StockTransDetailDTO stockTransDetailDTO1 = new StockTransDetailDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetailDTO, stockTransDetailDTO1);
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        StockTransActionDTO stockTransActionDTO1 = new StockTransActionDTO();
        BaseStockAgentExpRequestService spyTemp = PowerMockito.spy( baseStockAgentExpRequestService );
        PowerMockito.suppress( MemberMatcher.methodsDeclaredIn(BaseStockService.class));
        PowerMockito.doReturn(stockTransActionDTO1).when(spyTemp, "executeStockTrans", any(), any(), any(), any());

        StockOrderAgentDTO stockOrderAgentDTO = new StockOrderAgentDTO();
        stockOrderAgentDTO.setStatus(2L);
        Mockito.doReturn(stockOrderAgentDTO).when(stockOrderAgentService).findOne(any());
        Mockito.doReturn(stockOrderAgentDTO).when(stockOrderAgentService).update(any());
        baseStockAgentExpRequestService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetail, requiredRoleMap);
    }

    @Test
    public void testExecuteStockTrans_3() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockOrderAgentId(2L);
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        StockTransDetailDTO stockTransDetailDTO1 = new StockTransDetailDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetailDTO, stockTransDetailDTO1);
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        StockTransActionDTO stockTransActionDTO1 = new StockTransActionDTO();
        BaseStockAgentExpRequestService spyTemp = PowerMockito.spy( baseStockAgentExpRequestService );
        PowerMockito.suppress( MemberMatcher.methodsDeclaredIn(BaseStockService.class));
        PowerMockito.doReturn(stockTransActionDTO1).when(spyTemp, "executeStockTrans", any(), any(), any(), any());

        StockOrderAgentDTO stockOrderAgentDTO = new StockOrderAgentDTO();
        stockOrderAgentDTO.setStatus(0L);
        Mockito.doReturn(stockOrderAgentDTO).when(stockOrderAgentService).findOne(any());
        Mockito.doReturn(stockOrderAgentDTO).when(stockOrderAgentService).update(any());
        baseStockAgentExpRequestService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetail, requiredRoleMap);
    }
}