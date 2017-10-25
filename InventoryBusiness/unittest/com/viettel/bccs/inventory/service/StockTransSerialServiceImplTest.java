package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockTransSerial;
import com.viettel.bccs.inventory.repo.StockTransSerialRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.common.util.mapper.BaseMapper;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.groups.ConvertGroup;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;

/**
 * Created by TruNV on 8/17/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class StockTransSerialServiceImplTest {
    @InjectMocks
    private StockTransSerialServiceImpl stockTransSerialService;

    @Mock
    private BaseMapper<StockTransSerial, StockTransSerialDTO> mapper;

    @Mock
    private StockTransSerialRepo repository;

    @Mock
    private StockTransService transService;

    @Mock
    private StockTransActionService actionService;

    @Mock
    private ProductOfferingService productOfferingService;

    @Mock
    private OptionSetValueService optionSetValueService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    /** -------------------------------------------------------------------- **/
    /** --- Test for stockTransSerialService.findStockTransSerial method --- **/
    /** -------------------------------------------------------------------- **/
    @Test(expected = LogicException.class)
    public void testFindStockTransSerial_2() throws Exception {

    }

    @Test(expected = LogicException.class)
    public void testFindStockTransSerial_1() throws Exception {
        List<Long> stockActionIds = new ArrayList<>();
        stockActionIds.add(1L);

        Mockito.when(actionService.findOne(1L)).thenReturn(null);
        stockTransSerialService.findStockTransSerial(stockActionIds);
    }

    @Test(expected = LogicException.class)
    public void testFindStockTransSerial_3() throws Exception {
        List<Long> stockActionIds = new ArrayList<>();
        stockActionIds.add(1L);

        StockTransActionDTO stockTransAction = new StockTransActionDTO();
        stockTransAction.setStockTransId(null);

        Mockito.when(actionService.findOne(1L)).thenReturn(stockTransAction);
        stockTransSerialService.findStockTransSerial(stockActionIds);
    }

    @Test(expected = LogicException.class)
    public void testFindStockTransSerial_4() throws Exception {
        List<Long> stockActionIds = new ArrayList<>();
        stockActionIds.add(1L);

        StockTransActionDTO stockTransAction = new StockTransActionDTO();
        stockTransAction.setStockTransId(1L);
        Mockito.when(actionService.findOne(1L)).thenReturn(stockTransAction);

        Mockito.when(transService.findStockTransByActionId(1L)).thenReturn(null);

        stockTransSerialService.findStockTransSerial(stockActionIds);
    }

    @Test(expected = LogicException.class)
    public void testFindStockTransSerial_5() throws Exception {
        List<Long> stockActionIds = new ArrayList<>();
        stockActionIds.add(1L);

        StockTransActionDTO stockTransAction = new StockTransActionDTO();
        stockTransAction.setStockTransId(1L);
        Mockito.when(actionService.findOne(1L)).thenReturn(stockTransAction);

        StockTransDTO stockTrans  = new StockTransDTO();
        stockTrans.setStatus(null);
        Mockito.when(transService.findStockTransByActionId(1L)).thenReturn(stockTrans);

        stockTransSerialService.findStockTransSerial(stockActionIds);
    }

    @Test(expected = LogicException.class)
    public void testFindStockTransSerial_6() throws Exception {
        List<Long> stockActionIds = new ArrayList<>();
        stockActionIds.add(1L);

        StockTransActionDTO stockTransAction = new StockTransActionDTO();
        stockTransAction.setStockTransId(1L);
        Mockito.when(actionService.findOne(1L)).thenReturn(stockTransAction);

        StockTransDTO stockTrans  = new StockTransDTO();
        stockTrans.setStatus("1");
        Mockito.when(transService.findStockTransByActionId(1L)).thenReturn(stockTrans);

        stockTransSerialService.findStockTransSerial(stockActionIds);
    }

    @Test(expected = LogicException.class)
    public void testFindStockTransSerial_7() throws Exception {
        List<Long> stockActionIds = new ArrayList<>();
        stockActionIds.add(1L);

        StockTransActionDTO stockTransAction = new StockTransActionDTO();
        stockTransAction.setStockTransId(1L);
        Mockito.when(actionService.findOne(1L)).thenReturn(stockTransAction);

        StockTransDTO stockTrans  = new StockTransDTO();
        stockTrans.setStatus("2");
        Mockito.when(transService.findStockTransByActionId(1L)).thenReturn(stockTrans);

        stockTransSerialService.findStockTransSerial(stockActionIds);
    }

    @Test(expected = LogicException.class)
    public void testFindStockTransSerial_8() throws Exception {
        List<Long> stockActionIds = new ArrayList<>();
        stockActionIds.add(1L);

        StockTransActionDTO stockTransAction = new StockTransActionDTO();
        stockTransAction.setStockTransId(1L);
        Mockito.when(actionService.findOne(1L)).thenReturn(stockTransAction);

        StockTransDTO stockTrans  = new StockTransDTO();
        stockTrans.setStatus("3");
        Mockito.when(transService.findStockTransByActionId(1L)).thenReturn(stockTrans);

        Mockito.when(repository.getListStockTransSerial(1L)).thenReturn(null);

        stockTransSerialService.findStockTransSerial(stockActionIds);
    }

    @Test
    public void testFindStockTransSerial_9() throws Exception {
        List<Long> stockActionIds = new ArrayList<>();
        stockActionIds.add(1L);

        StockTransActionDTO stockTransAction = new StockTransActionDTO();
        stockTransAction.setStockTransId(1L);
        Mockito.when(actionService.findOne(1L)).thenReturn(stockTransAction);

        StockTransDTO stockTrans  = new StockTransDTO();
        stockTrans.setStatus("3");
        Mockito.when(transService.findStockTransByActionId(1L)).thenReturn(stockTrans);

        StockTransSerialDTO transSerialDTO = new StockTransSerialDTO();
        StockTransSerialDTO transSerialDTO1 = new StockTransSerialDTO();
        List<StockTransSerialDTO> serialDTOs = Lists.newArrayList(transSerialDTO, transSerialDTO1);

        Mockito.when(repository.getListStockTransSerial(1L)).thenReturn(serialDTOs);

        Assert.assertEquals(serialDTOs, stockTransSerialService.findStockTransSerial(stockActionIds));
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for stockTransSerialService.findStockTransSerialByProductOfferType method --- **/
    /** -------------------------------------------------------------------- **/
    @Test(expected = LogicException.class)
    public void testFindStockTransSerialByProductOfferType_1() throws Exception {
        WareHouseInfoDTO infoDTO = new WareHouseInfoDTO();
        infoDTO.setOwnerType(null);

        stockTransSerialService.findStockTransSerialByProductOfferType(infoDTO);
    }

    @Test(expected = LogicException.class)
    public void testFindStockTransSerialByProductOfferType_2() throws Exception {
        WareHouseInfoDTO infoDTO = new WareHouseInfoDTO();
        infoDTO.setOwnerType(Const.OWNER_TYPE.SHOP);
        infoDTO.setOwnerId(null);

        stockTransSerialService.findStockTransSerialByProductOfferType(infoDTO);
    }

    @Test(expected = LogicException.class)
    public void testFindStockTransSerialByProductOfferType_3() throws Exception {
        WareHouseInfoDTO infoDTO = new WareHouseInfoDTO();
        infoDTO.setOwnerType(Const.OWNER_TYPE.STAFF);
        infoDTO.setOwnerId(null);

        stockTransSerialService.findStockTransSerialByProductOfferType(infoDTO);
    }

    @Test(expected = LogicException.class)
    public void testFindStockTransSerialByProductOfferType_4() throws Exception {
        WareHouseInfoDTO infoDTO = new WareHouseInfoDTO();
        infoDTO.setOwnerType("1");
        infoDTO.setOwnerId("1");
        infoDTO.setProductOfferingId(null);

        stockTransSerialService.findStockTransSerialByProductOfferType(infoDTO);
    }

    @Test
    public void testFindStockTransSerialByProductOfferType_5() throws Exception {
        WareHouseInfoDTO infoDTO = new WareHouseInfoDTO();
        infoDTO.setOwnerType("1");
        infoDTO.setOwnerId("1");
        infoDTO.setProductOfferingId("1");

        StockTransSerialDTO transSerialDTO = new StockTransSerialDTO();
        StockTransSerialDTO transSerialDTO1 = new StockTransSerialDTO();
        List<StockTransSerialDTO> serialDTOs = Lists.newArrayList(transSerialDTO, transSerialDTO1);
        Mockito.when(repository.findStockTransSerialByProductOfferType(infoDTO)).thenReturn(serialDTOs);

        Assert.assertEquals(serialDTOs, stockTransSerialService.findStockTransSerialByProductOfferType(infoDTO));
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for stockTransSerialService.getListGatherSerial method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testGetListGatherSerial_1() throws Exception {
        StockInspectCheckDTO stockInspectCheckDTO = new StockInspectCheckDTO();
        StockInspectCheckDTO stockInspectCheckDTO1 = new StockInspectCheckDTO();
        List<StockInspectCheckDTO> lstSerial = Lists.newArrayList(stockInspectCheckDTO, stockInspectCheckDTO1);
        Mockito.when(repository.getListGatherSerial(anyLong(), anyLong(), anyLong(), anyLong(), anyLong(), anyString(), anyString(), anyString())).thenReturn(lstSerial);

        Assert.assertEquals(lstSerial, stockTransSerialService.getListGatherSerial(1L, 1L, 1L,1L, 1L, "1", "A", "B"));
    }

    @Test
    public void testGetListGatherSerial_2() throws Exception {
        StockInspectCheckDTO stockInspectCheckDTO = new StockInspectCheckDTO();
        stockInspectCheckDTO.setQuantity(10L);
        StockInspectCheckDTO stockInspectCheckDTO1 = new StockInspectCheckDTO();
        stockInspectCheckDTO1.setQuantity(10L);
        List<StockInspectCheckDTO> lstSerial = Lists.newArrayList(stockInspectCheckDTO, stockInspectCheckDTO1);
        Mockito.when(repository.getListGatherSerial(anyLong(), anyLong(), anyLong(), anyLong(), anyLong(), anyString(), anyString(), anyString())).thenReturn(lstSerial);

        List<StockInspectCheckDTO> result = stockTransSerialService.getListGatherSerial(1L, 1L, 1L,1L, 1L, "1", "11", "20");
        Assert.assertEquals(lstSerial.size(), result.size());
        Assert.assertEquals(lstSerial.get(0).getQuantity(), result.get(0).getQuantity());
        Assert.assertEquals(lstSerial.get(1).getQuantity(), result.get(1).getQuantity());
    }

    @Test
    public void testGetListGatherSerial_3() throws Exception {
        List<StockInspectCheckDTO> result = Lists.newArrayList();
        Mockito.when(repository.getListGatherSerial(anyLong(), anyLong(), anyLong(), anyLong(), anyLong(), anyString(), anyString(), anyString())).thenReturn(null);

        Assert.assertEquals(result, stockTransSerialService.getListGatherSerial(1L, 1L, 1L,1L, 1L, "1", "11", "20"));
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for stockTransSerialService.getRangeSerialStockCardValid method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testGetRangeSerialStockCardValid_1() throws Exception {
        List<String> testList = Lists.newArrayList("A", "B");
        Mockito.when(repository.getRangeSerialStockCardValid(1L, 1L, 1L, 1L, "1", "1", 1L, "1")).thenReturn(testList);

        List<String> result = stockTransSerialService.getRangeSerialStockCardValid(1L, 1L, 1L,1L, "1", "1", 1L, "1");
        Assert.assertEquals(testList.size(), result.size());
        Assert.assertEquals(testList.get(0), result.get(0));
        Assert.assertEquals(testList.get(1), result.get(1));
    }

    @Test
    public void testGetRangeSerialStockCardValid_2() throws Exception {
        List<String> testList = Lists.newArrayList("A", "B");
        Mockito.when(repository.getRangeSerialStockCardValidWithoutOwner(1L, 2L, "1","1", 1L, "1")).thenReturn(testList);

        List<String> result = stockTransSerialService.getRangeSerialStockCardValid(1L, 1L, 1L,2L, "1", "1", 1L, "1");
        Assert.assertEquals(testList.size(), result.size());
        Assert.assertEquals(testList.get(0), result.get(0));
        Assert.assertEquals(testList.get(1), result.get(1));
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for stockTransSerialService.getListSerialSelect method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testGetListSerialSelect_1() throws Exception {
        List<StockTransSerialDTO> listSerialSelect = stockTransSerialService.getListSerialSelect(null);
        Assert.assertEquals(0, listSerialSelect.size());
    }

    @Test(expected = LogicException.class)
    public void testGetListSerialSelect_2() throws Exception {
        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial(null);

        stockTransSerialService.getListSerialSelect(stockTransFullDTO);
    }

    @Test(expected = LogicException.class)
    public void testGetListSerialSelect_3() throws Exception {
        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setToSerial(null);

        stockTransSerialService.getListSerialSelect(stockTransFullDTO);
    }

    @Test(expected = LogicException.class)
    public void testGetListSerialSelect_4() throws Exception {
        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("1");
        stockTransFullDTO.setToSerial("2");
        stockTransFullDTO.setRegionStockTransId(1L);
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_HANDSET);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setFromOwnerId(1L);
        Mockito.when(transService.findOne(1L)).thenReturn(stockTransDTO);

        stockTransSerialService.getListSerialSelect(stockTransFullDTO);
    }

    @Test(expected = LogicException.class)
    public void testGetListSerialSelect_5() throws Exception {
        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("1");
        stockTransFullDTO.setToSerial("1");
        stockTransFullDTO.setRegionStockTransId(1L);
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_HANDSET);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setFromOwnerId(1L);
        Mockito.when(transService.findOne(1L)).thenReturn(stockTransDTO);

        Mockito.when(repository.getListSerialValid(1L, 1L, "1", 1L, 1L, "1", "1", 1L, 1L)).thenReturn(null);

        stockTransSerialService.getListSerialSelect(stockTransFullDTO);
    }

    @Test
    public void testGetListSerialSelect_6() throws Exception {
        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromOwnerType("1");
        stockTransFullDTO.setTableName("1");
        stockTransFullDTO.setProdOfferId(1L);
        stockTransFullDTO.setStateId(1L);
        stockTransFullDTO.setQuantityRemain(1L);
        stockTransFullDTO.setSerialStatus(1L);
        stockTransFullDTO.setFromSerial("1");
        stockTransFullDTO.setToSerial("1");
        stockTransFullDTO.setRegionStockTransId(1L);
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_HANDSET);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setFromOwnerId(1L);
        Mockito.when(transService.findOne(1L)).thenReturn(stockTransDTO);

        List<String> lsSerialValid = Lists.newArrayList("A");
        Mockito.when(repository.getListSerialValid(1L, 1L, "1", 1L, 1L, "1", "1", 1L, 1L)).thenReturn(lsSerialValid);

        List<StockTransSerialDTO> result = stockTransSerialService.getListSerialSelect(stockTransFullDTO);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(stockTransFullDTO.getFromSerial(), result.get(0).getFromSerial());
        Assert.assertEquals(stockTransFullDTO.getToSerial(), result.get(0).getToSerial());
        Assert.assertEquals(new Long(1), result.get(0).getQuantity());
    }

    @Test(expected = LogicException.class)
    public void testGetListSerialSelect_7() throws Exception {
        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("AAAAAAAAAA");
        stockTransFullDTO.setToSerial("1");
        stockTransFullDTO.setRegionStockTransId(1L);
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_CARD);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setFromOwnerId(1L);
        Mockito.when(transService.findOne(1L)).thenReturn(stockTransDTO);

        stockTransSerialService.getListSerialSelect(stockTransFullDTO);
    }

    @Test(expected = LogicException.class)
    public void testGetListSerialSelect_8() throws Exception {
        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("1");
        stockTransFullDTO.setToSerial("BBBBBBBB");
        stockTransFullDTO.setRegionStockTransId(1L);
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_CARD);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setFromOwnerId(1L);
        Mockito.when(transService.findOne(1L)).thenReturn(stockTransDTO);

        stockTransSerialService.getListSerialSelect(stockTransFullDTO);
    }

    @Test(expected = LogicException.class)
    public void testGetListSerialSelect_9() throws Exception {
        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("1");
        stockTransFullDTO.setToSerial("0");
        stockTransFullDTO.setRegionStockTransId(1L);
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_CARD);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setFromOwnerId(1L);
        Mockito.when(transService.findOne(1L)).thenReturn(stockTransDTO);

        stockTransSerialService.getListSerialSelect(stockTransFullDTO);
    }

    @Test(expected = LogicException.class)
    public void testGetListSerialSelect_10() throws Exception {
        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial("1");
        stockTransFullDTO.setToSerial("1000000");
        stockTransFullDTO.setRegionStockTransId(1L);
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_CARD);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setFromOwnerId(1L);
        Mockito.when(transService.findOne(1L)).thenReturn(stockTransDTO);

        stockTransSerialService.getListSerialSelect(stockTransFullDTO);
    }

    @Test
    public void testGetListSerialSelect_11() throws Exception {
        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromOwnerType("1");
        stockTransFullDTO.setFromOwnerId(1L);
        stockTransFullDTO.setProdOfferId(1L);
        stockTransFullDTO.setStateId(Const.GOODS_STATE.RESCUE);
        stockTransFullDTO.setQuantityRemain(1L);
        stockTransFullDTO.setTableName(Const.STOCK_TYPE.STOCK_CARD_NAME);
        stockTransFullDTO.setFromSerial("1");
        stockTransFullDTO.setToSerial("1");
        stockTransFullDTO.setRegionStockTransId(1L);
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_CARD);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setFromOwnerId(1L);
        Mockito.when(transService.findOne(1L)).thenReturn(stockTransDTO);

        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue("1");
        List<OptionSetValueDTO> lstConfigStockCard = Lists.newArrayList(optionSetValueDTO);
        Mockito.when(optionSetValueService.getByOptionSetCode("STOCK_CARD_STRIP")).thenReturn(lstConfigStockCard);

        List<String> lsSerialNumber = Lists.newArrayList("1");
        Mockito.when(repository.getRangeSerialStockCardValid(1L, 1L, 1L, Const.GOODS_STATE.RESCUE, "1", "1", 1L, Const.STATUS.NO_ACTIVE)).thenReturn(lsSerialNumber);

        StockTransSerialDTO transSerialDTO = new StockTransSerialDTO();
        transSerialDTO.setActionCode("1");
        StockTransSerialDTO transSerialDTO1 = new StockTransSerialDTO();
        transSerialDTO.setActionCode("2");
        List<StockTransSerialDTO> serialDTOList = Lists.newArrayList(transSerialDTO, transSerialDTO1);

        StockTransSerialServiceImpl spyTemp = Mockito.spy(stockTransSerialService);
        Mockito.doReturn(serialDTOList).when(spyTemp).getListSerialGroup(lsSerialNumber);

        List<StockTransSerialDTO> result = spyTemp.getListSerialSelect(stockTransFullDTO);
        Assert.assertEquals(serialDTOList.size(), result.size());
        Assert.assertEquals(serialDTOList.get(0).getActionCode(), result.get(0).getActionCode());
        Assert.assertEquals(serialDTOList.get(1).getActionCode(), result.get(1).getActionCode());
    }

    @Test(expected = LogicException.class)
    public void testGetListSerialSelect_12() throws Exception {
        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromOwnerType("1");
        stockTransFullDTO.setFromOwnerId(1L);
        stockTransFullDTO.setProdOfferId(1L);
        stockTransFullDTO.setStateId(Const.GOODS_STATE.RESCUE);
        stockTransFullDTO.setQuantityRemain(1L);
        stockTransFullDTO.setTableName(Const.STOCK_TYPE.STOCK_KIT_NAME);
        stockTransFullDTO.setFromSerial("1");
        stockTransFullDTO.setToSerial("1");
        stockTransFullDTO.setRegionStockTransId(1L);
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_CARD);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setFromOwnerId(1L);
        Mockito.when(transService.findOne(1L)).thenReturn(stockTransDTO);

        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue("1");
        List<OptionSetValueDTO> lstConfigStockCard = Lists.newArrayList(optionSetValueDTO);
        Mockito.when(optionSetValueService.getByOptionSetCode("STOCK_CARD_STRIP")).thenReturn(lstConfigStockCard);

        StockTransSerialServiceImpl spyTemp = Mockito.spy(stockTransSerialService);
        Mockito.doReturn(null).when(spyTemp).getListSerialValid(anyLong(), anyLong(), anyString(), anyLong(), anyLong(), anyString(), anyString(), anyLong(), anyLong());

        spyTemp.getListSerialSelect(stockTransFullDTO);
    }

    @Test
    public void testGetListSerialSelect_13() throws Exception {
        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromOwnerType("1");
        stockTransFullDTO.setFromOwnerId(1L);
        stockTransFullDTO.setProdOfferId(1L);
        stockTransFullDTO.setStateId(Const.GOODS_STATE.RESCUE);
        stockTransFullDTO.setQuantityRemain(1L);
        stockTransFullDTO.setTableName(Const.STOCK_TYPE.STOCK_KIT_NAME);
        stockTransFullDTO.setFromSerial("1");
        stockTransFullDTO.setToSerial("1");
        stockTransFullDTO.setRegionStockTransId(1L);
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_CARD);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setFromOwnerId(1L);
        Mockito.when(transService.findOne(1L)).thenReturn(stockTransDTO);

        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue("1");
        List<OptionSetValueDTO> lstConfigStockCard = Lists.newArrayList(optionSetValueDTO);
        Mockito.when(optionSetValueService.getByOptionSetCode("STOCK_CARD_STRIP")).thenReturn(lstConfigStockCard);

        List<String> lsSerialValid = Lists.newArrayList("1");
        StockTransSerialServiceImpl spyTemp = Mockito.spy(stockTransSerialService);
        Mockito.doReturn(lsSerialValid).when(spyTemp).getListSerialValid(anyLong(), anyLong(), anyString(), anyLong(), anyLong(), anyString(), anyString(), anyLong(), anyLong());

        StockTransSerialDTO transSerialDTO = new StockTransSerialDTO();
        transSerialDTO.setActionCode("1");
        StockTransSerialDTO transSerialDTO1 = new StockTransSerialDTO();
        transSerialDTO.setActionCode("2");
        List<StockTransSerialDTO> serialDTOList = Lists.newArrayList(transSerialDTO, transSerialDTO1);
        Mockito.doReturn(serialDTOList).when(spyTemp).getListSerialGroup(lsSerialValid);

        List<StockTransSerialDTO> result = spyTemp.getListSerialSelect(stockTransFullDTO);
        Assert.assertEquals(serialDTOList.size(), result.size());
        Assert.assertEquals(serialDTOList.get(0).getActionCode(), result.get(0).getActionCode());
        Assert.assertEquals(serialDTOList.get(1).getActionCode(), result.get(1).getActionCode());
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for stockTransSerialService.getListSerialGroup method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testGetListSerialGroup_1() throws Exception {
        List<String> listSerials = Lists.newArrayList();

        List<StockTransSerialDTO> result = stockTransSerialService.getListSerialGroup(listSerials);
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testGetListSerialGroup_2() throws Exception {
        List<String> listSerials = Lists.newArrayList("1", "2");

        List<StockTransSerialDTO> result = stockTransSerialService.getListSerialGroup(listSerials);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals("1", result.get(0).getFromSerial());
        Assert.assertEquals("2", result.get(0).getToSerial());
        Assert.assertEquals(new Long(2), result.get(0).getQuantity());
    }

    @Test
    public void testGetListSerialGroup_3() throws Exception {
        List<String> listSerials = Lists.newArrayList("1", "3");

        List<StockTransSerialDTO> result = stockTransSerialService.getListSerialGroup(listSerials);
        Assert.assertEquals(2, result.size());
        Assert.assertEquals("1", result.get(0).getFromSerial());
        Assert.assertEquals("1", result.get(0).getToSerial());
        Assert.assertEquals(new Long(1), result.get(0).getQuantity());
        Assert.assertEquals("3", result.get(1).getFromSerial());
        Assert.assertEquals("3", result.get(1).getToSerial());
        Assert.assertEquals(new Long(1), result.get(1).getQuantity());
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for stockTransSerialService.count method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testCount_1() throws Exception {
        Mockito.when(repository.toPredicate(Mockito.any())).thenReturn(null);
        Mockito.when(repository.count(Mockito.any())).thenReturn(1L);
        Long result = stockTransSerialService.count(Lists.newArrayList());
        Assert.assertEquals(new Long(1), result);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for stockTransSerialService.findOne method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testFindOne_1() throws Exception {
        StockTransSerialDTO transSerialDTO = new StockTransSerialDTO();
        StockTransSerial stockTransSerial = new StockTransSerial();
        Mockito.when(repository.findOne(1L)).thenReturn(stockTransSerial);
        Mockito.when(mapper.toDtoBean(stockTransSerial)).thenReturn(transSerialDTO);
        StockTransSerialDTO result = stockTransSerialService.findOne(1L);
        Assert.assertNotNull(result);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for stockTransSerialService.findAll method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testFindAll_1() throws Exception {
        List<StockTransSerial> transSerialList = Lists.newArrayList();
        List<StockTransSerialDTO> serialDTOList = Lists.newArrayList();
        Mockito.when(repository.findAll()).thenReturn(transSerialList);
        Mockito.when(mapper.toDtoBean(transSerialList)).thenReturn(serialDTOList);
        List<StockTransSerialDTO> result = stockTransSerialService.findAll();
        Assert.assertNotNull(result);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for stockTransSerialService.findByFilter method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testFindByFilter_1() throws Exception {
        List<StockTransSerial> transSerialList = Lists.newArrayList();
        List<StockTransSerialDTO> serialDTOList = Lists.newArrayList();
        Mockito.when(repository.findAll()).thenReturn(transSerialList);
        Mockito.when(mapper.toDtoBean(transSerialList)).thenReturn(serialDTOList);
        List<StockTransSerialDTO> result = stockTransSerialService.findByFilter(Lists.newArrayList());
        Assert.assertNotNull(result);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for stockTransSerialService.save method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testSave_1() throws Exception {
        StockTransSerialDTO transSerialDTO = new StockTransSerialDTO();
        StockTransSerial stockTransSerial = new StockTransSerial();
        Mockito.when(mapper.toPersistenceBean(transSerialDTO)).thenReturn(stockTransSerial);
        Mockito.when(repository.save(stockTransSerial)).thenReturn(stockTransSerial);
        Mockito.when(mapper.toDtoBean(stockTransSerial)).thenReturn(transSerialDTO);
        StockTransSerialDTO result = stockTransSerialService.save(transSerialDTO);
        Assert.assertNotNull(result);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for stockTransSerialService.findByStockTransDetailId method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testFindByStockTransDetailId_1() throws Exception {
        List<StockTransSerial> transSerialList = Lists.newArrayList();
        List<StockTransSerialDTO> serialDTOList = Lists.newArrayList();
        Mockito.when(repository.findAll()).thenReturn(transSerialList);
        Mockito.when(mapper.toDtoBean(transSerialList)).thenReturn(serialDTOList);
        List<StockTransSerialDTO> result = stockTransSerialService.findByStockTransDetailId(1L);
        Assert.assertNotNull(result);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for stockTransSerialService.findByStockTransId method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testFindByStockTransId_1() throws Exception {
        List<StockTransSerial> transSerialList = Lists.newArrayList();
        List<StockTransSerialDTO> serialDTOList = Lists.newArrayList();
        Mockito.when(repository.getSerialByStockTransId(1L)).thenReturn(transSerialList);
        Mockito.when(mapper.toDtoBean(transSerialList)).thenReturn(serialDTOList);
        List<StockTransSerialDTO> result = stockTransSerialService.findByStockTransId(1L);
        Assert.assertNotNull(result);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for stockTransSerialService.getRangeSerial method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testGetRangeSerial_1() throws Exception {
        List<StockTransSerialDTO> serialDTOList = Lists.newArrayList();
        Mockito.when(repository.getRangeSerial(anyLong(), anyLong(), anyString(), anyLong(), anyLong(), anyString(), anyLong())).thenReturn(serialDTOList);
        List<StockTransSerialDTO> result = stockTransSerialService.getRangeSerial(anyLong(), anyLong(), anyLong(), anyString(), anyLong(), anyString(), anyLong());
        Assert.assertNotNull(result);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for stockTransSerialService.getRangeSerialFifo method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testGetRangeSerialFifo_1() throws Exception {
        List<StockTransSerialDTO> serialDTOList = Lists.newArrayList();
        Mockito.when(repository.getRangeSerialFifo(anyLong(), anyLong(), anyString(), anyLong(), anyLong(), anyString(), anyLong())).thenReturn(serialDTOList);
        List<StockTransSerialDTO> result = stockTransSerialService.getRangeSerialFifo(anyLong(), anyLong(), anyLong(), anyString(), anyLong(), anyString(), anyLong());
        Assert.assertNotNull(result);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for stockTransSerialService.findStockTransSerialByDTO method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testFindStockTransSerialByDTO_1() throws Exception {
        List<StockTransSerialDTO> serialDTOList = Lists.newArrayList();
        StockTransSerialDTO transSerialDTO = new StockTransSerialDTO();
        Mockito.when(repository.findStockTransSerial(transSerialDTO)).thenReturn(serialDTOList);
        List<StockTransSerialDTO> result = stockTransSerialService.findStockTransSerialByDTO(transSerialDTO);
        Assert.assertNotNull(result);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for stockTransSerialService.getListSerialFromTable method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testGetListSerialFromTable_1() throws Exception {
        List<StockTransSerialDTO> serialDTOList = Lists.newArrayList();
        Mockito.when(repository.getListSerialFromTable(anyLong(), anyLong(), anyLong(), anyLong(), anyString())).thenReturn(serialDTOList);
        List<StockTransSerialDTO> result = stockTransSerialService.getListSerialFromTable(anyLong(), anyLong(), anyLong(), anyLong(), anyString());
        Assert.assertNotNull(result);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for stockTransSerialService.getListSerialFromList method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testGetListSerialFromList_1() throws Exception {
        List<StockInspectCheckDTO> inspectCheckDTOList = Lists.newArrayList();
        Mockito.when(repository.getListSerialFromList(anyLong(), anyLong(), anyLong(), anyLong(), anyLong(), anyString(), any())).thenReturn(inspectCheckDTOList);
        List<StockInspectCheckDTO> result = stockTransSerialService.getListSerialFromList(anyLong(), anyLong(), anyLong(), anyLong(), anyLong(), anyString(), any());
        Assert.assertNotNull(result);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for stockTransSerialService.getListSerialValid method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testGetListSerialValid_1() throws Exception {
        List<String> inspectCheckDTOList = Lists.newArrayList();
        Mockito.when(repository.getListSerialValid(anyLong(), anyLong(), anyString(), anyLong(), anyLong(), anyString(), anyString(), anyLong(), anyLong())).thenReturn(inspectCheckDTOList);
        List<String> result = stockTransSerialService.getListSerialValid(anyLong(), anyLong(), anyString(), anyLong(), anyLong(), anyString(), anyString(), anyLong(), anyLong());
        Assert.assertNotNull(result);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for stockTransSerialService.updateStatusStockSerial method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testUpdateStatusStockSerial_1() throws Exception {
        Mockito.when(repository.updateStatusStockSerial(anyLong(), anyLong(), anyLong(), anyString(), anyString(), anyLong(), anyLong())).thenReturn(0);
        int result = stockTransSerialService.updateStatusStockSerial(anyLong(), anyLong(), anyLong(), anyString(), anyString(), anyLong(), anyLong());
        Assert.assertEquals(0, result);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for stockTransSerialService.getDepostiPriceFromStockX method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testGetDepostiPriceFromStockX_1() throws Exception {
        Mockito.when(repository.getDepostiPriceFromStockX(anyLong(), anyLong(), anyLong(), anyString())).thenReturn(0L);
        Long result = stockTransSerialService.getDepostiPriceFromStockX(anyLong(), anyLong(), anyLong(), anyString());
        Assert.assertEquals(new Long(0), result);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for stockTransSerialService.getDepostiPriceForRetrieve method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testGetDepostiPriceForRetrieve_1() throws Exception {
        Mockito.when(repository.getDepostiPriceForRetrieve(anyLong(), anyLong(), anyLong(), anyLong(), anyString())).thenReturn(0L);
        Long result = stockTransSerialService.getDepostiPriceForRetrieve(anyLong(), anyLong(), anyLong(), anyLong(), anyString());
        Assert.assertEquals(new Long(0), result);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for stockTransSerialService.getDepostiPriceByRangeSerial method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testGetDepostiPriceByRangeSerial_1() throws Exception {
        Mockito.when(repository.getDepostiPriceByRangeSerial(anyLong(), anyLong(), anyLong(), anyLong(), anyString(), anyString())).thenReturn(0L);
        Long result = stockTransSerialService.getDepostiPriceByRangeSerial(anyLong(), anyLong(), anyLong(), anyLong(), anyString(), anyString());
        Assert.assertEquals(new Long(0), result);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for stockTransSerialService.getListSerialQuantity method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testGetListSerialQuantity_1() throws Exception {
        List<StockTransSerialDTO> serialDTOList = Lists.newArrayList();
        Mockito.when(repository.getListSerialQuantity(anyLong(), anyLong(), anyLong(), anyLong(), anyString(), anyString(), anyString(), anyString())).thenReturn(serialDTOList);
        List<StockTransSerialDTO> result = stockTransSerialService.getListSerialQuantity(anyLong(), anyLong(), anyLong(), anyLong(), anyString(), anyString(), anyString(), anyString());
        Assert.assertNotNull(result);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for stockTransSerialService.searchSerialExact method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testSearchSerialExact_1() throws Exception {
        List<LookupSerialByFileDTO> lookupSerialByFileDTOList = Lists.newArrayList();
        Mockito.when(repository.searchSerialExact(anyLong(), any(), anyString(), anyBoolean())).thenReturn(lookupSerialByFileDTOList);
        List<LookupSerialByFileDTO> result = stockTransSerialService.searchSerialExact(anyLong(), any(), anyString(), anyBoolean());
        Assert.assertNotNull(result);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for stockTransSerialService.listLookUpSerialHistory method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testListLookUpSerialHistory_1() throws Exception {
        List<ViewSerialHistoryDTO> serialHistoryDTOList = Lists.newArrayList();
        Mockito.when(repository.listLookUpSerialHistory(anyString(), anyLong(), any() , any(), anyBoolean())).thenReturn(serialHistoryDTOList);
        List<ViewSerialHistoryDTO> result = stockTransSerialService.listLookUpSerialHistory(anyString(), anyLong(), any() , any(), anyBoolean());
        Assert.assertNotNull(result);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for stockTransSerialService.getListSerialProduct method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testGetListSerialProduct_1() throws Exception {
        List<ChangeDamagedProductDTO> damagedProductDTOList = Lists.newArrayList();
        Mockito.when(repository.getListSerialProduct(anyString(), anyLong(), anyLong())).thenReturn(damagedProductDTOList);
        List<ChangeDamagedProductDTO> result = stockTransSerialService.getListSerialProduct(anyString(), anyLong(), anyLong());
        Assert.assertNotNull(result);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for stockTransSerialService.getListBySerial method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testGetListBySerial_1() throws Exception {
        List<StockTransSerialDTO> stockTransSerialDTOList = Lists.newArrayList();
        Mockito.when(repository.getListBySerial(anyLong(), anyString(), anyLong(), anyLong(), anyLong(), anyString())).thenReturn(stockTransSerialDTOList);
        List<StockTransSerialDTO> result = stockTransSerialService.getListBySerial(anyLong(), anyString(), anyLong(), anyLong(), anyLong(), anyString());
        Assert.assertNotNull(result);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for stockTransSerialService.getListStockCardSaledBySerial method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testGetListStockCardSaledBySerial_1() throws Exception {
        List<StockTransSerialDTO> stockTransSerialDTOList = Lists.newArrayList();
        Mockito.when(repository.getListStockCardSaledBySerial(anyLong(), anyString())).thenReturn(stockTransSerialDTOList);
        List<StockTransSerialDTO> result = stockTransSerialService.getListStockCardSaledBySerial(anyLong(), anyString());
        Assert.assertNotNull(result);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for stockTransSerialService.getStockTransSerialByStockTransId method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testGetStockTransSerialByStockTransId_1() throws Exception {
        List<StockTransSerial> transSerialArrayList = Lists.newArrayList();
        Mockito.when(repository.getStockTransSerialByStockTransId(anyLong(), anyLong(), anyLong())).thenReturn(transSerialArrayList);
        List<StockTransSerialDTO> result = stockTransSerialService.getStockTransSerialByStockTransId(anyLong(), anyLong(), anyLong());
        Assert.assertNotNull(result);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for stockTransSerialService.updateStockBalance method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testUpdateStockBalance_1() throws Exception {
        Mockito.when(repository.updateStockBalance(anyLong(), anyLong(), anyLong(), anyLong(), anyString(), anyString(), anyLong(), anyLong(), anyLong())).thenReturn(0);
        int result = stockTransSerialService.updateStockBalance(anyLong(), anyLong(), anyLong(), anyLong(), anyString(), anyString(), anyLong(), anyLong(), anyLong());
        Assert.assertEquals(0, result);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for stockTransSerialService.getLstStockDevice method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testGetLstStockDevice_1() throws Exception {
        List<StockDeviceDTO> stockDeviceDTOList = Lists.newArrayList();
        Mockito.when(repository.getLstStockDevice(anyLong())).thenReturn(stockDeviceDTOList);
        List<StockDeviceDTO> result = stockTransSerialService.getLstStockDevice(anyLong());
        Assert.assertNotNull(result);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for stockTransSerialService.getLstDeviceTransfer method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testGetLstDeviceTransfer_1() throws Exception {
        List<StockDeviceTransferDTO> deviceTransferDTOList = Lists.newArrayList();
        Mockito.when(repository.getLstDeviceTransfer(anyLong(), anyLong(), anyLong())).thenReturn(deviceTransferDTOList);
        List<StockDeviceTransferDTO> result = stockTransSerialService.getLstDeviceTransfer(anyLong(), anyLong(), anyLong());
        Assert.assertNotNull(result);
    }

    @Test(expected = LogicException.class)
    public void testGetListSerialSelect_19() throws Exception {
        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        List<StockTransSerialDTO> listSerialSelect = stockTransSerialService.getListSerialSelect(stockTransFullDTO);
        Assert.assertEquals( 0,  listSerialSelect.size());
    }

    /**
     * Case FromSerial is null
     * @throws Exception
     */
    @Test(expected = LogicException.class)
    public void testGetListSerialSelect_20() throws Exception {
        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setFromSerial(null);
        stockTransSerialService.getListSerialSelect(stockTransFullDTO);
    }

    /**
     * Case ToSerial is null
     * @throws Exception
     */
    @Test(expected = LogicException.class)
    public void testGetListSerialSelect_21() throws Exception {
        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setToSerial(null);
        stockTransSerialService.getListSerialSelect(stockTransFullDTO);
    }

    /**
     * Case RegionStockTransId is null
     * @throws Exception
     */
    @Test(expected = LogicException.class)
    public void testGetListSerialSelect_22() throws Exception {
        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setRegionStockTransId(null);
        stockTransSerialService.getListSerialSelect(stockTransFullDTO);
    }

    /**
     * Case fromSerial toSerial is different
     * @throws Exception
     */
    @Test(expected = LogicException.class)
    public void testGetListSerialSelect_23() throws Exception {

        Long stockTransId = 1l;
        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setRegionStockTransId(1L);
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_HANDSET);
        stockTransFullDTO.setFromSerial("FromSerial");
        stockTransFullDTO.setToSerial("ToSerial");

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setFromOwnerId(stockTransId);
        Mockito.when(transService.findOne(stockTransId)).thenReturn(stockTransDTO);
        stockTransSerialService.getListSerialSelect(stockTransFullDTO);
    }

    /**
     * Case fromSerial toSerial is different
     * @throws Exception
     */
    @Test(expected = LogicException.class)
    public void testGetListSerialSelect_24() throws Exception {

        Long stockTransId = 1l;
        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        stockTransFullDTO.setRegionStockTransId(1L);
        stockTransFullDTO.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_HANDSET);
        stockTransFullDTO.setFromSerial("Serial");
        stockTransFullDTO.setToSerial("Serial");

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setFromOwnerId(stockTransId);
        Mockito.when(transService.findOne(stockTransId)).thenReturn(stockTransDTO);
        stockTransSerialService.getListSerialSelect(stockTransFullDTO);
    }
}