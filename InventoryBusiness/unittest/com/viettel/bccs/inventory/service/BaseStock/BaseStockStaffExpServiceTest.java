package com.viettel.bccs.inventory.service.BaseStock;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.ReasonService;
import com.viettel.bccs.inventory.service.ShopService;
import com.viettel.bccs.inventory.service.StockTransService;
import com.viettel.fw.common.util.RequiredRoleMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Lamnv5 on 8/4/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class BaseStockStaffExpServiceTest {
    @InjectMocks
    BaseStockStaffExpService expService;
    @Mock
    private StockTransService stockTransService;
    @Spy
    BaseValidateService baseValidateService;
    @Mock
    ShopService shopService;
    @Mock
    ReasonService reasonService;

    @Test
    public void testDoValidate_1() throws Exception {
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        StockTransDetailDTO detailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(detailDTO);

        stockTransDTO.setStockTransId(1L);
        stockTransDTO.setFromOwnerId(1L);
        stockTransDTO.setFromOwnerType(1L);
        stockTransDTO.setToOwnerId(2L);
        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setReasonId(1L);

        VShopStaffDTO vss1 = new VShopStaffDTO();
        List<VShopStaffDTO> exportStaffList = Lists.newArrayList(vss1);

        VShopStaffDTO vss2 = new VShopStaffDTO();
        List<VShopStaffDTO> importShopList = Lists.newArrayList(vss2);
        Mockito.when(shopService.getListShopStaff(stockTransDTO.getFromOwnerId(), stockTransDTO.getFromOwnerType().toString(), Const.VT_UNIT.VT)).thenReturn(exportStaffList);
        Mockito.when(shopService.getListShopStaff(stockTransDTO.getToOwnerId(), stockTransDTO.getToOwnerType().toString(), Const.VT_UNIT.VT)).thenReturn(importShopList);

        ReasonDTO reason = new ReasonDTO();
        reason.setReasonId(1L);
        reason.setReasonStatus(Const.STATUS_ACTIVE);
        Mockito.when(reasonService.findOne(1L)).thenReturn(reason);
        Answer tmpAnswer = new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }};
        Mockito.doAnswer(tmpAnswer).when(baseValidateService).doDebitValidate(Mockito.any(), Mockito.anyList());
        Mockito.doAnswer(tmpAnswer).when(baseValidateService).doCurrentQuantityValidate(Mockito.any(), Mockito.anyList());
        Mockito.doAnswer(tmpAnswer).when(baseValidateService).doQuantityAvailableValidate(Mockito.any(), Mockito.anyList());
        Mockito.doAnswer(tmpAnswer).when(baseValidateService).findTransExpiredPay(Mockito.anyLong());

        expService.doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }

    @Test
    public void testExecuteStockTrans_1() throws Exception {
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();

        StockTransDetailDTO detailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(detailDTO);
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        stockTransDTO.setStockTransId(1L);
        Mockito.when(stockTransService.findOneLock(1L)).thenReturn(stockTransDTO);
        Mockito.when(stockTransService.save(stockTransDTO)).thenReturn(stockTransDTO);
        expService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetail, requiredRoleMap);

    }
}