package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.BaseStock.BaseStockService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.*;
import com.viettel.fw.dto.BaseMessage;
import org.junit.Assert;
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
import org.powermock.reflect.Whitebox;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by TruNV on 9/9/2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({DataUtil.class, BaseStockService.class, DbUtil.class, ChangeModelTransBaseService.class, EntityManager.class, DateUtil.class})
@PowerMockIgnore("javax.management.*")
public class ChangeModelTransBaseServiceTest {

    @InjectMocks
    private ChangeModelTransBaseService changeModelTransBaseService;

    @Mock
    private EntityManager em;

    @Mock
    private ChangeModelTransService changeModelTransService;

    @Mock
    private ChangeModelTransDetailService changeModelTransDetailService;

    @Mock
    private ChangeModelTransSerialService changeModelTransSerialService;

    @Mock
    private StaffService staffService;

    @Mock
    private ShopService shopService;

    @Mock
    private StockTransService stockTransService;

    @Mock
    private StockTransActionService stockTransActionService;

    @Mock
    private StockTransVofficeService stockTransVofficeService;

    @Mock
    private StockTransDetailService stockTransDetailService;

    @Mock
    private StockTransSerialService stockTransSerialService;

    @Mock
    private ProductOfferTypeService productOfferTypeService;

    @Mock
    private ProductOfferingService productOfferingService;

    @Mock
    private ActiveKitVofficeService activeKitVofficeService;

    @Mock
    private SignFlowDetailService signFlowDetailService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for changeModelTransBaseService.approveChangeProduct method **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testApproveChangeProduct_1() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage("mn.stock.change.product.offering.approve.not.found.transaction");

        Long changeModelTransId = 0L;
        StaffDTO approveStaffDTO = new StaffDTO();
        StockTransDTO stockTransVoficeDTO = new StockTransDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        ChangeModelTransDTO changeModelTransDTO = new ChangeModelTransDTO();
        Mockito.doReturn(null).when(changeModelTransService).findOne(any());
        changeModelTransBaseService.approveChangeProduct(changeModelTransId, approveStaffDTO, stockTransVoficeDTO, requiredRoleMap);
    }

    @Test
    public void testApproveChangeProduct_2() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage("mn.stock.change.product.offering.approve.not.found.transaction");

        Long changeModelTransId = 0L;
        StaffDTO approveStaffDTO = new StaffDTO();
        StockTransDTO stockTransVoficeDTO = new StockTransDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        ChangeModelTransDTO changeModelTransDTO = new ChangeModelTransDTO();
        changeModelTransDTO.setStatus(2L);
        Mockito.doReturn(changeModelTransDTO).when(changeModelTransService).findOne(any());
        changeModelTransBaseService.approveChangeProduct(changeModelTransId, approveStaffDTO, stockTransVoficeDTO, requiredRoleMap);
    }

    @Test
    public void testApproveChangeProduct_3() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage("utilities.exchange.card.bankplus.not.transaction");

        Long changeModelTransId = 0L;
        StaffDTO approveStaffDTO = new StaffDTO();
        StockTransDTO stockTransVoficeDTO = new StockTransDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        ChangeModelTransDTO changeModelTransDTO = new ChangeModelTransDTO();
        changeModelTransDTO.setStatus(0L);
        Mockito.doReturn(changeModelTransDTO).when(changeModelTransService).findOne(any());

        StaffDTO createStaff = new StaffDTO();
        Mockito.doReturn(createStaff).when(staffService).findOne(any());

        StockTransDTO originalStockTrans = new StockTransDTO();
        Mockito.doReturn(null).when(stockTransService).findOne(any());

        changeModelTransBaseService.approveChangeProduct(changeModelTransId, approveStaffDTO, stockTransVoficeDTO, requiredRoleMap);
    }

    @Test
    public void testApproveChangeProduct_4() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage("utilities.exchange.card.bankplus.not.transaction");

        Long changeModelTransId = 0L;
        StaffDTO approveStaffDTO = new StaffDTO();
        StockTransDTO stockTransVoficeDTO = new StockTransDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        ChangeModelTransDTO changeModelTransDTO = new ChangeModelTransDTO();
        changeModelTransDTO.setStatus(0L);
        Mockito.doReturn(changeModelTransDTO).when(changeModelTransService).findOne(any());

        StaffDTO createStaff = new StaffDTO();
        Mockito.doReturn(createStaff).when(staffService).findOne(any());

        StockTransDTO originalStockTrans = new StockTransDTO();
        originalStockTrans.setNote("2");
        Mockito.doReturn(originalStockTrans).when(stockTransService).findOne(any());

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        Mockito.doReturn(null).when(stockTransActionService).getStockTransActionByIdAndStatus(any(), any());

        changeModelTransBaseService.approveChangeProduct(changeModelTransId, approveStaffDTO, stockTransVoficeDTO, requiredRoleMap);
    }

    @Test
    public void testApproveChangeProduct_5() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage("utilities.exchange.card.bankplus.not.transaction");

        Long changeModelTransId = 0L;
        StaffDTO approveStaffDTO = new StaffDTO();
        StockTransDTO stockTransVoficeDTO = new StockTransDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        ChangeModelTransDTO changeModelTransDTO = new ChangeModelTransDTO();
        changeModelTransDTO.setStatus(0L);
        Mockito.doReturn(changeModelTransDTO).when(changeModelTransService).findOne(any());

        StaffDTO createStaff = new StaffDTO();
        Mockito.doReturn(createStaff).when(staffService).findOne(any());

        StockTransDTO originalStockTrans = new StockTransDTO();
        originalStockTrans.setNote("2");
        Mockito.doReturn(originalStockTrans).when(stockTransService).findOne(any());

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setStockTransActionId(0L);
        Mockito.doReturn(stockTransActionDTO).when(stockTransActionService).getStockTransActionByIdAndStatus(any(), any());

        changeModelTransBaseService.approveChangeProduct(changeModelTransId, approveStaffDTO, stockTransVoficeDTO, requiredRoleMap);
    }

    @Test
    public void testApproveChangeProduct_6() throws Exception {
        Long changeModelTransId = 0L;
        StaffDTO approveStaffDTO = new StaffDTO();
        StockTransDTO stockTransVoficeDTO = new StockTransDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        ChangeModelTransDTO changeModelTransDTO = new ChangeModelTransDTO();
        changeModelTransDTO.setStatus(0L);
        changeModelTransDTO.setFromOwnerId(7283L);
        Mockito.doReturn(changeModelTransDTO).when(changeModelTransService).findOne(any());

        StaffDTO createStaff = new StaffDTO();
        Mockito.doReturn(createStaff).when(staffService).findOne(any());

        StockTransDTO originalStockTrans = new StockTransDTO();
        originalStockTrans.setNote("2");
        Mockito.doReturn(originalStockTrans).when(stockTransService).findOne(any());

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setStockTransActionId(1L);
        Mockito.doReturn(stockTransActionDTO).when(stockTransActionService).getStockTransActionByIdAndStatus(any(), any());

        Mockito.doNothing().when(stockTransVofficeService).doSignedVofficeValidate(any());
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> lstDetail = Lists.newArrayList(stockTransDetailDTO);
        Mockito.doReturn(lstDetail).when(stockTransDetailService).findByStockTransId(any());
        Date currentDate = new Date();

        ChangeModelTransBaseService spy = PowerMockito.spy(changeModelTransBaseService);
        Mockito.doReturn(currentDate).when(spy).getSysDate(em);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransId(1L);
        Mockito.doReturn(stockTransDTO).when(stockTransService).save(any());

        Mockito.doReturn("String").when(staffService).getTransCode(any(), any(), any());

        StockTransActionDTO stockTransActionNewSave = new StockTransActionDTO();
        Mockito.doReturn(stockTransActionNewSave).when(stockTransActionService).save(any());

        Mockito.doNothing().when(spy).doIncreaseStockNum(any(), any(), any());
        Mockito.doNothing().when(spy).doSignVoffice(any(), any(), any(), any());

        StockTransDTO transVTExpToPartner = new StockTransDTO();
        Mockito.doReturn(transVTExpToPartner).when(spy).getStockTransVT(any(), any(), any(), any(), any(), any());

        StockTransActionDTO stockTransActionVTExpToPartner = new StockTransActionDTO();
        PowerMockito.doReturn(stockTransActionVTExpToPartner).when(spy, "getStockTransActionVT", any(), any(), any());

        Mockito.doReturn(true).when(spy).saveStockTransDetailAndSerial(any(), any(), any(), any());
        Mockito.doNothing().when(spy).doSignVoffice(any(), any(), any(), any());
        Mockito.doNothing().when(spy).doSaveStockGoods(any(), any(), any());
        Mockito.doNothing().when(spy).doSaveStockTotal(any(), any(), any(), any());

        Mockito.doReturn(1L).when(changeModelTransDetailService).getNewProdOfferId(any(), any(), any());

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        Mockito.doReturn(productOfferingDTO).when(productOfferingService).findOne(any());

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        Mockito.doReturn(productOfferTypeDTO).when(productOfferTypeService).findOne(any());

        ChangeModelTransDTO changeModelTransDTO1 = new ChangeModelTransDTO();
        Mockito.doReturn(changeModelTransDTO1).when(changeModelTransService).save(any());

        spy.approveChangeProduct(changeModelTransId, approveStaffDTO, stockTransVoficeDTO, requiredRoleMap);
    }

    @Test
    public void testApproveChangeProduct_7() throws Exception {
        Long changeModelTransId = 0L;
        StaffDTO approveStaffDTO = new StaffDTO();
        StockTransDTO stockTransVoficeDTO = new StockTransDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        ChangeModelTransDTO changeModelTransDTO = new ChangeModelTransDTO();
        changeModelTransDTO.setStatus(0L);
        changeModelTransDTO.setFromOwnerId(7282L);
        Mockito.doReturn(changeModelTransDTO).when(changeModelTransService).findOne(any());

        StaffDTO createStaff = new StaffDTO();
        Mockito.doReturn(createStaff).when(staffService).findOne(any());

        StockTransDTO originalStockTrans = new StockTransDTO();
        originalStockTrans.setNote("2");
        Mockito.doReturn(originalStockTrans).when(stockTransService).findOne(any());

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setStockTransActionId(1L);
        Mockito.doReturn(stockTransActionDTO).when(stockTransActionService).getStockTransActionByIdAndStatus(any(), any());

        Mockito.doNothing().when(stockTransVofficeService).doSignedVofficeValidate(any());
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> lstDetail = Lists.newArrayList(stockTransDetailDTO);
        Mockito.doReturn(lstDetail).when(stockTransDetailService).findByStockTransId(any());
        Date currentDate = new Date();

        ChangeModelTransBaseService spy = PowerMockito.spy(changeModelTransBaseService);
        Mockito.doReturn(currentDate).when(spy).getSysDate(em);

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransId(1L);
        Mockito.doReturn(stockTransDTO).when(stockTransService).save(any());

        Mockito.doReturn("String").when(staffService).getTransCode(any(), any(), any());

        StockTransActionDTO stockTransActionNewSave = new StockTransActionDTO();
        Mockito.doReturn(stockTransActionNewSave).when(stockTransActionService).save(any());

        Mockito.doNothing().when(spy).doIncreaseStockNum(any(), any(), any());
        Mockito.doNothing().when(spy).doSignVoffice(any(), any(), any(), any());

        StockTransDTO transVTExpToPartner = new StockTransDTO();
        Mockito.doReturn(transVTExpToPartner).when(spy).getStockTransVT(any(), any(), any(), any(), any(), any());

        StockTransActionDTO stockTransActionVTExpToPartner = new StockTransActionDTO();
        PowerMockito.doReturn(stockTransActionVTExpToPartner).when(spy, "getStockTransActionVT", any(), any(), any());

        Mockito.doReturn(true).when(spy).saveStockTransDetailAndSerial(any(), any(), any(), any());
        Mockito.doNothing().when(spy).doSignVoffice(any(), any(), any(), any());
        Mockito.doNothing().when(spy).doSaveStockGoods(any(), any(), any());
        Mockito.doNothing().when(spy).doSaveStockTotal(any(), any(), any(), any());

        Mockito.doReturn(1L).when(changeModelTransDetailService).getNewProdOfferId(any(), any(), any());

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        Mockito.doReturn(productOfferingDTO).when(productOfferingService).findOne(any());

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        Mockito.doReturn(productOfferTypeDTO).when(productOfferTypeService).findOne(any());

        ChangeModelTransDTO changeModelTransDTO1 = new ChangeModelTransDTO();
        Mockito.doReturn(changeModelTransDTO1).when(changeModelTransService).save(any());

        spy.approveChangeProduct(changeModelTransId, approveStaffDTO, stockTransVoficeDTO, requiredRoleMap);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for changeModelTransBaseService.cancelChangeProduct method  **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testCancelChangeProduct_1() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage("mn.stock.change.product.offering.approve.not.found.transaction");

        Long changeModelTransId = 0L;
        StaffDTO approveStaffDTO = new StaffDTO();
        ChangeModelTransDTO changeModelTransDTO = new ChangeModelTransDTO();
        changeModelTransDTO.setStatus(1L);

        Mockito.doReturn(null).when(changeModelTransService).findOne(any());

        changeModelTransBaseService.cancelChangeProduct(changeModelTransId, approveStaffDTO);
    }

    @Test
    public void testCancelChangeProduct_2() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage("mn.stock.change.product.offering.approve.not.found.transaction");

        Long changeModelTransId = 0L;
        StaffDTO approveStaffDTO = new StaffDTO();
        ChangeModelTransDTO changeModelTransDTO = new ChangeModelTransDTO();
        changeModelTransDTO.setStatus(1L);

        Mockito.doReturn(null).when(changeModelTransService).findOne(any());

        changeModelTransBaseService.cancelChangeProduct(changeModelTransId, approveStaffDTO);
    }

    @Test
    public void testCancelChangeProduct_3() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage("utilities.exchange.card.bankplus.not.transaction");

        Long changeModelTransId = 0L;
        StaffDTO approveStaffDTO = new StaffDTO();
        ChangeModelTransDTO changeModelTransDTO = new ChangeModelTransDTO();
        changeModelTransDTO.setStatus(0L);

        Mockito.doReturn(changeModelTransDTO).when(changeModelTransService).findOne(any());
        Date currentDate = new Date();
        ChangeModelTransBaseService spy = PowerMockito.spy(changeModelTransBaseService);
        Mockito.doReturn(currentDate).when(spy).getSysDate(em);

        ChangeModelTransDTO changeModelTransDTO1 = new ChangeModelTransDTO();
        Mockito.doReturn(changeModelTransDTO1).when(changeModelTransService).save(any());

        StockTransDTO originalStockTrans = new StockTransDTO();
        Mockito.doReturn(null).when(stockTransService).findOne(any());

        spy.cancelChangeProduct(changeModelTransId, approveStaffDTO);
    }

    @Test
    public void testCancelChangeProduct_4() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage("utilities.exchange.card.bankplus.not.transaction");

        Long changeModelTransId = 0L;
        StaffDTO approveStaffDTO = new StaffDTO();
        ChangeModelTransDTO changeModelTransDTO = new ChangeModelTransDTO();
        changeModelTransDTO.setStatus(0L);

        Mockito.doReturn(changeModelTransDTO).when(changeModelTransService).findOne(any());
        Date currentDate = new Date();
        ChangeModelTransBaseService spy = PowerMockito.spy(changeModelTransBaseService);
        Mockito.doReturn(currentDate).when(spy).getSysDate(em);

        ChangeModelTransDTO changeModelTransDTO1 = new ChangeModelTransDTO();
        Mockito.doReturn(changeModelTransDTO1).when(changeModelTransService).save(any());

        StockTransDTO originalStockTrans = new StockTransDTO();
        originalStockTrans.setStockTransId(1L);
        Mockito.doReturn(originalStockTrans).when(stockTransService).findOne(any());

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        Mockito.doReturn(null).when(stockTransActionService).getStockTransActionByIdAndStatus(any(), any());

        spy.cancelChangeProduct(changeModelTransId, approveStaffDTO);
    }

    @Test
    public void testCancelChangeProduct_5() throws Exception {

        Long changeModelTransId = 0L;
        StaffDTO approveStaffDTO = new StaffDTO();
        ChangeModelTransDTO changeModelTransDTO = new ChangeModelTransDTO();
        changeModelTransDTO.setStatus(0L);

        Mockito.doReturn(changeModelTransDTO).when(changeModelTransService).findOne(any());
        Date currentDate = new Date();
        ChangeModelTransBaseService spy = PowerMockito.spy(changeModelTransBaseService);
        Mockito.doReturn(currentDate).when(spy).getSysDate(em);

        ChangeModelTransDTO changeModelTransDTO1 = new ChangeModelTransDTO();
        Mockito.doReturn(changeModelTransDTO1).when(changeModelTransService).save(any());

        StockTransDTO originalStockTrans = new StockTransDTO();
        originalStockTrans.setStockTransId(1L);
        Mockito.doReturn(originalStockTrans).when(stockTransService).findOne(any());

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setStatus("2");
        Mockito.doReturn(stockTransActionDTO).when(stockTransActionService).getStockTransActionByIdAndStatus(any(), any());

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransId(1L);
        Mockito.doReturn(stockTransDTO).when(stockTransService).save(any());

        StockTransActionDTO stockTransActionNewSave = new StockTransActionDTO();
        Mockito.doReturn(stockTransActionNewSave).when(stockTransActionService).save(any());

        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> lstDetail = Lists.newArrayList(stockTransDetailDTO);
        Mockito.doReturn(lstDetail).when(stockTransDetailService).findByStockTransId(any());

        Mockito.doNothing().when(spy).doSaveStockTotal(any(), any(), any(), any());

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        Mockito.doReturn(productOfferingDTO).when(productOfferingService).findOne(any());

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        Mockito.doReturn(productOfferTypeDTO).when(productOfferTypeService).findOne(any());

        Mockito.doNothing().when(spy).doSaveStockGoods(any(), any(), any());

        spy.cancelChangeProduct(changeModelTransId, approveStaffDTO);
    }


    /** -------------------------------------------------------------------- **/
    /** --- Test for changeModelTransBaseService.getStockTransVT method  --- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testGetStockTransVT_1() throws Exception {
        StockTransDTO stockTransVoffice = new StockTransDTO();
        Long fromOwnerId = 1L;
        Long fromOwnerType = 1L;
        Long toOwnerId = 1L;
        Long toOwnerType = 1L;
        Long reasonId = 1L;
        changeModelTransBaseService.getStockTransVT(stockTransVoffice, fromOwnerId, fromOwnerType, toOwnerId, toOwnerType, reasonId);
    }

    /** -------------------------------------------------------------------- **/
    /** - Test for changeModelTransBaseService.getStockTransActionVT method  **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testGetStockTransActionVT_1() throws Exception {
        StaffDTO staffDTO = new StaffDTO();
        Date currentDate = new Date();
        Long stockTransId = 1L;
        ChangeModelTransBaseService spyService = PowerMockito.spy(changeModelTransBaseService);
        Whitebox.invokeMethod(spyService, "getStockTransActionVT", staffDTO, currentDate, stockTransId);
    }

    /** -------------------------------------------------------------------- **/
    /** -Test for changeModelTransBaseService.saveStockTransDetailAndSerial  **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testSaveStockTransDetailAndSerial_1() throws Exception {

        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> lstTransDetail = Lists.newArrayList(stockTransDetailDTO);

        Long stockTransId = 1L;
        Date createDate = new Date();
        Long changeModelTransId = 1L;

        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        List<StockTransSerialDTO> lstSerial = Lists.newArrayList(stockTransSerialDTO);

        Mockito.doReturn(lstSerial).when(stockTransSerialService).findByStockTransDetailId(any());
        Mockito.doReturn(1L).when(changeModelTransDetailService).getNewProdOfferId(any(), any(), any());

        StockTransDetailDTO stockTransDetailSave = new StockTransDetailDTO();
        Mockito.doReturn(stockTransDetailSave).when(stockTransDetailService).save(any());

        StockTransSerialDTO stockTransSerialDTO1 = new StockTransSerialDTO();
        Mockito.doReturn(stockTransSerialDTO1).when(stockTransSerialService).save(any());

        boolean result = changeModelTransBaseService.saveStockTransDetailAndSerial(lstTransDetail, stockTransId, createDate, changeModelTransId);
        Assert.assertTrue(result);
    }

    @Test
    public void testSaveStockTransDetailAndSerial_2() throws Exception {

        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> lstTransDetail = Lists.newArrayList(stockTransDetailDTO);

        Long stockTransId = 1L;
        Date createDate = new Date();
        Long changeModelTransId = 1L;

        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        List<StockTransSerialDTO> lstSerial = Lists.newArrayList();

        Mockito.doReturn(lstSerial).when(stockTransSerialService).findByStockTransDetailId(any());
        Mockito.doReturn(1L).when(changeModelTransDetailService).getNewProdOfferId(any(), any(), any());

        StockTransDetailDTO stockTransDetailSave = new StockTransDetailDTO();
        Mockito.doReturn(stockTransDetailSave).when(stockTransDetailService).save(any());

        StockTransSerialDTO stockTransSerialDTO1 = new StockTransSerialDTO();
        Mockito.doReturn(stockTransSerialDTO1).when(stockTransSerialService).save(any());

        boolean result = changeModelTransBaseService.saveStockTransDetailAndSerial(lstTransDetail, stockTransId, createDate, null);
        Assert.assertTrue(result);
    }

    @Test
    public void testSaveStockTransDetailAndSerial_3() throws Exception {

        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> lstTransDetail = Lists.newArrayList(stockTransDetailDTO);

        Long stockTransId = 1L;
        Date createDate = new Date();
        Long changeModelTransId = 1L;

        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        List<StockTransSerialDTO> lstSerial = Lists.newArrayList();

        Mockito.doReturn(lstSerial).when(stockTransSerialService).findByStockTransDetailId(any());
        Mockito.doThrow(new Exception()).when(changeModelTransDetailService).getNewProdOfferId(any(), any(), any());

        StockTransDetailDTO stockTransDetailSave = new StockTransDetailDTO();
        Mockito.doReturn(stockTransDetailSave).when(stockTransDetailService).save(any());

        StockTransSerialDTO stockTransSerialDTO1 = new StockTransSerialDTO();
        Mockito.doReturn(stockTransSerialDTO1).when(stockTransSerialService).save(any());

        boolean result = changeModelTransBaseService.saveStockTransDetailAndSerial(lstTransDetail, stockTransId, createDate, changeModelTransId);
        Assert.assertFalse(result);
    }

    /** -------------------------------------------------------------------- **/
    /** ---Test for changeModelTransBaseService.processApproveRequest method **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testProcessApproveRequest_1() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( "thread.toolkit.cancel.request.not.found");

        Long changeModelTransId = 1L;
        ChangeModelTransDTO changeModelTransDTO = new ChangeModelTransDTO();
        Mockito.doReturn(null).when(changeModelTransService).findOne(any());

        changeModelTransBaseService.processApproveRequest(changeModelTransId);
    }

    @Test
    public void testProcessApproveRequest_2() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( "thread.toolkit.cancel.request.not.found");

        Long changeModelTransId = 1L;
        ChangeModelTransDTO changeModelTransDTO = new ChangeModelTransDTO();
        changeModelTransDTO.setStatus(6L);
        Mockito.doReturn(changeModelTransDTO).when(changeModelTransService).findOne(any());

        changeModelTransBaseService.processApproveRequest(changeModelTransId);
    }

    @Test
    public void testProcessApproveRequest_3() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( "thread.toolkit.cancel.request.stock.trans");
        ChangeModelTransBaseService spyService = PowerMockito.spy(changeModelTransBaseService);
        Long changeModelTransId = 1L;
        ChangeModelTransDTO changeModelTransDTO = new ChangeModelTransDTO();
        changeModelTransDTO.setStatus(5L);
        Mockito.doReturn(changeModelTransDTO).when(changeModelTransService).findOne(any());

        StaffDTO createStaff = new StaffDTO();
        Mockito.doReturn(createStaff).when(staffService).findOne(any());

        Date currentDate = new Date();
        PowerMockito.mockStatic(DbUtil.class);
        when(DbUtil.getSysDate(any(EntityManager.class))).thenReturn(currentDate);


        StockTransDTO originalStockTrans = new StockTransDTO();
        Mockito.doReturn(null).when(stockTransService).findOne(any());

        spyService.processApproveRequest(changeModelTransId);
    }

    @Test
    public void testProcessApproveRequest_4() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( "thread.toolkit.cancel.request.stock.trans.action");
        ChangeModelTransBaseService spyService = PowerMockito.spy(changeModelTransBaseService);
        Long changeModelTransId = 1L;
        ChangeModelTransDTO changeModelTransDTO = new ChangeModelTransDTO();
        changeModelTransDTO.setStatus(5L);
        Mockito.doReturn(changeModelTransDTO).when(changeModelTransService).findOne(any());

        StaffDTO createStaff = new StaffDTO();
        Mockito.doReturn(createStaff).when(staffService).findOne(any());

        Date currentDate = new Date();
        PowerMockito.mockStatic(DbUtil.class);
        when(DbUtil.getSysDate(any(EntityManager.class))).thenReturn(currentDate);

        StockTransDTO originalStockTrans = new StockTransDTO();
        originalStockTrans.setStockTransId(1L);
        Mockito.doReturn(originalStockTrans).when(stockTransService).findOne(any());

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        Mockito.doReturn(null).when(stockTransActionService).getStockTransActionByIdAndStatus(any(), any());

        spyService.processApproveRequest(changeModelTransId);
    }

    @Test
    public void testProcessApproveRequest_5() throws Exception {
        ChangeModelTransBaseService spyService = PowerMockito.spy(changeModelTransBaseService);

        Long changeModelTransId = 1L;
        ChangeModelTransDTO changeModelTransDTO = new ChangeModelTransDTO();
        changeModelTransDTO.setStatus(5L);
        changeModelTransDTO.setFromOwnerId(7283L);

        Mockito.doReturn(changeModelTransDTO).when(changeModelTransService).findOne(any());

        StaffDTO createStaff = new StaffDTO();
        Mockito.doReturn(createStaff).when(staffService).findOne(any());

        Date currentDate = new Date();
        PowerMockito.mockStatic(DbUtil.class);
        when(DbUtil.getSysDate(any(EntityManager.class))).thenReturn(currentDate);

        StockTransDTO originalStockTrans = new StockTransDTO();
        originalStockTrans.setStockTransId(1L);
        Mockito.doReturn(originalStockTrans).when(stockTransService).findOne(any());

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setStatus("1");
        Mockito.doReturn(stockTransActionDTO).when(stockTransActionService).getStockTransActionByIdAndStatus(any(), any());

        StockTransDetailDTO  stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> lstDetail = Lists.newArrayList(stockTransDetailDTO);
        Mockito.doReturn(lstDetail).when(stockTransDetailService).findByStockTransId(any());

        StaffDTO approveStaffDTO = new StaffDTO();
        Mockito.doReturn(approveStaffDTO).when(staffService).findOne(any());

        ShopDTO approveStaffShopDTO = new ShopDTO();
        Mockito.doReturn(approveStaffShopDTO).when(shopService).findOne(any());

        StockTransDTO stockTransDTO = new StockTransDTO();
        Mockito.doReturn(stockTransDTO).when(stockTransService).save(any());

        Mockito.doReturn("String").when(staffService).getTransCode(any(), any(), any());

        StockTransActionDTO stockTransActionNewSave = new StockTransActionDTO();
        Mockito.doReturn(stockTransActionNewSave).when(stockTransActionService).save(any());

        Mockito.doNothing().when(spyService).doIncreaseStockNum(any(), any(), any());

        StockTransDTO transVTExpToPartner = new StockTransDTO();
        Mockito.doReturn(transVTExpToPartner).when(spyService).getStockTransVT(any(), any(), any(), any(), any(), any());

        StockTransActionDTO stockTransActionVTExpToPartner = new StockTransActionDTO();
        PowerMockito.doReturn(stockTransActionVTExpToPartner).when(spyService, "getStockTransActionVT", any(), any(), any());

        Mockito.doReturn(true).when(spyService).saveStockTransDetailAndSerial(any(), any(), any(), any());

        Mockito.doReturn(1L).when(changeModelTransDetailService).getNewProdOfferId(any(), any(), any());

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        Mockito.doReturn(productOfferingDTO).when(productOfferingService).findOne(any());

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        Mockito.doReturn(productOfferTypeDTO).when(productOfferTypeService).findOne(any());

        Mockito.doNothing().when(spyService).doSaveStockTotal(any(), any(), any(), any());

        ChangeModelTransDTO changeModelTransDTO1 = new ChangeModelTransDTO();
        Mockito.doReturn(changeModelTransDTO1).when(changeModelTransService).save(any());

        ActiveKitVofficeDTO activeKitVofficeDTO = new ActiveKitVofficeDTO();
        activeKitVofficeDTO.setActionCode("PX");
        Mockito.doReturn(activeKitVofficeDTO).when(activeKitVofficeService).findByChangeModelTransId(any());
        activeKitVofficeDTO.setStatus("2");

        PowerMockito.mockStatic(DateUtil.class);
        PowerMockito.when(DateUtil.dateToStringWithPattern(any(), any())).thenReturn("12");

        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        Mockito.doReturn(stockTransVofficeDTO).when(stockTransVofficeService).save(any());

        ActiveKitVofficeDTO activeKitVofficeDTO1 = new ActiveKitVofficeDTO();
        Mockito.doReturn(activeKitVofficeDTO1).when(activeKitVofficeService).save(any());

        Mockito.doNothing().when(spyService).doSaveStockGoods(any(), any(), any());
        spyService.processApproveRequest(changeModelTransId);
    }


    @Test
    public void testProcessApproveRequest_6() throws Exception {
        ChangeModelTransBaseService spyService = PowerMockito.spy(changeModelTransBaseService);

        Long changeModelTransId = 1L;
        ChangeModelTransDTO changeModelTransDTO = new ChangeModelTransDTO();
        changeModelTransDTO.setStatus(5L);
        changeModelTransDTO.setFromOwnerId(7282L);

        Mockito.doReturn(changeModelTransDTO).when(changeModelTransService).findOne(any());

        StaffDTO createStaff = new StaffDTO();
        Mockito.doReturn(createStaff).when(staffService).findOne(any());

        Date currentDate = new Date();
        PowerMockito.mockStatic(DbUtil.class);
        when(DbUtil.getSysDate(any(EntityManager.class))).thenReturn(currentDate);

        StockTransDTO originalStockTrans = new StockTransDTO();
        originalStockTrans.setStockTransId(1L);
        Mockito.doReturn(originalStockTrans).when(stockTransService).findOne(any());

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setStatus("1");
        Mockito.doReturn(stockTransActionDTO).when(stockTransActionService).getStockTransActionByIdAndStatus(any(), any());

        StockTransDetailDTO  stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> lstDetail = Lists.newArrayList(stockTransDetailDTO);
        Mockito.doReturn(lstDetail).when(stockTransDetailService).findByStockTransId(any());

        StaffDTO approveStaffDTO = new StaffDTO();
        Mockito.doReturn(approveStaffDTO).when(staffService).findOne(any());

        ShopDTO approveStaffShopDTO = new ShopDTO();
        Mockito.doReturn(approveStaffShopDTO).when(shopService).findOne(any());

        StockTransDTO stockTransDTO = new StockTransDTO();
        Mockito.doReturn(stockTransDTO).when(stockTransService).save(any());

        Mockito.doReturn("String").when(staffService).getTransCode(any(), any(), any());

        StockTransActionDTO stockTransActionNewSave = new StockTransActionDTO();
        Mockito.doReturn(stockTransActionNewSave).when(stockTransActionService).save(any());

        Mockito.doNothing().when(spyService).doIncreaseStockNum(any(), any(), any());

        StockTransDTO transVTExpToPartner = new StockTransDTO();
        Mockito.doReturn(transVTExpToPartner).when(spyService).getStockTransVT(any(), any(), any(), any(), any(), any());

        StockTransActionDTO stockTransActionVTExpToPartner = new StockTransActionDTO();
        PowerMockito.doReturn(stockTransActionVTExpToPartner).when(spyService, "getStockTransActionVT", any(), any(), any());

        Mockito.doReturn(true).when(spyService).saveStockTransDetailAndSerial(any(), any(), any(), any());

        Mockito.doReturn(1L).when(changeModelTransDetailService).getNewProdOfferId(any(), any(), any());

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        Mockito.doReturn(productOfferingDTO).when(productOfferingService).findOne(any());

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        Mockito.doReturn(productOfferTypeDTO).when(productOfferTypeService).findOne(any());

        Mockito.doNothing().when(spyService).doSaveStockTotal(any(), any(), any(), any());

        ChangeModelTransDTO changeModelTransDTO1 = new ChangeModelTransDTO();
        Mockito.doReturn(changeModelTransDTO1).when(changeModelTransService).save(any());

        ActiveKitVofficeDTO activeKitVofficeDTO = new ActiveKitVofficeDTO();
        activeKitVofficeDTO.setActionCode("PX");
        Mockito.doReturn(activeKitVofficeDTO).when(activeKitVofficeService).findByChangeModelTransId(any());
        activeKitVofficeDTO.setStatus("2");

        PowerMockito.mockStatic(DateUtil.class);
        PowerMockito.when(DateUtil.dateToStringWithPattern(any(), any())).thenReturn("12");

        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        Mockito.doReturn(stockTransVofficeDTO).when(stockTransVofficeService).save(any());

        ActiveKitVofficeDTO activeKitVofficeDTO1 = new ActiveKitVofficeDTO();
        Mockito.doReturn(activeKitVofficeDTO1).when(activeKitVofficeService).save(any());

        Mockito.doNothing().when(spyService).doSaveStockGoods(any(), any(), any());
        spyService.processApproveRequest(changeModelTransId);
    }

    @Test
    public void testProcessApproveRequest_7() throws Exception {
        ChangeModelTransBaseService spyService = PowerMockito.spy(changeModelTransBaseService);

        Long changeModelTransId = 1L;
        ChangeModelTransDTO changeModelTransDTO = new ChangeModelTransDTO();
        changeModelTransDTO.setStatus(5L);
        changeModelTransDTO.setFromOwnerId(7283L);

        Mockito.doReturn(changeModelTransDTO).when(changeModelTransService).findOne(any());

        StaffDTO createStaff = new StaffDTO();
        Mockito.doReturn(createStaff).when(staffService).findOne(any());

        Date currentDate = new Date();
        PowerMockito.mockStatic(DbUtil.class);
        when(DbUtil.getSysDate(any(EntityManager.class))).thenReturn(currentDate);

        StockTransDTO originalStockTrans = new StockTransDTO();
        originalStockTrans.setStockTransId(1L);
        Mockito.doReturn(originalStockTrans).when(stockTransService).findOne(any());

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setStatus("1");
        Mockito.doReturn(stockTransActionDTO).when(stockTransActionService).getStockTransActionByIdAndStatus(any(), any());

        StockTransDetailDTO  stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> lstDetail = Lists.newArrayList(stockTransDetailDTO);
        Mockito.doReturn(lstDetail).when(stockTransDetailService).findByStockTransId(any());

        StaffDTO approveStaffDTO = new StaffDTO();
        Mockito.doReturn(approveStaffDTO).when(staffService).findOne(any());

        ShopDTO approveStaffShopDTO = new ShopDTO();
        Mockito.doReturn(approveStaffShopDTO).when(shopService).findOne(any());

        StockTransDTO stockTransDTO = new StockTransDTO();
        Mockito.doReturn(stockTransDTO).when(stockTransService).save(any());

        Mockito.doReturn("String").when(staffService).getTransCode(any(), any(), any());

        StockTransActionDTO stockTransActionNewSave = new StockTransActionDTO();
        Mockito.doReturn(stockTransActionNewSave).when(stockTransActionService).save(any());

        Mockito.doNothing().when(spyService).doIncreaseStockNum(any(), any(), any());

        StockTransDTO transVTExpToPartner = new StockTransDTO();
        Mockito.doReturn(transVTExpToPartner).when(spyService).getStockTransVT(any(), any(), any(), any(), any(), any());

        StockTransActionDTO stockTransActionVTExpToPartner = new StockTransActionDTO();
        PowerMockito.doReturn(stockTransActionVTExpToPartner).when(spyService, "getStockTransActionVT", any(), any(), any());

        Mockito.doReturn(true).when(spyService).saveStockTransDetailAndSerial(any(), any(), any(), any());

        Mockito.doReturn(1L).when(changeModelTransDetailService).getNewProdOfferId(any(), any(), any());

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        Mockito.doReturn(productOfferingDTO).when(productOfferingService).findOne(any());

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        Mockito.doReturn(productOfferTypeDTO).when(productOfferTypeService).findOne(any());

        Mockito.doNothing().when(spyService).doSaveStockTotal(any(), any(), any(), any());

        ChangeModelTransDTO changeModelTransDTO1 = new ChangeModelTransDTO();
        Mockito.doReturn(changeModelTransDTO1).when(changeModelTransService).save(any());

        ActiveKitVofficeDTO activeKitVofficeDTO = new ActiveKitVofficeDTO();
        activeKitVofficeDTO.setActionCode("PX");
        Mockito.doReturn(null).when(activeKitVofficeService).findByChangeModelTransId(any());
        activeKitVofficeDTO.setStatus("2");

        PowerMockito.mockStatic(DateUtil.class);
        PowerMockito.when(DateUtil.dateToStringWithPattern(any(), any())).thenReturn("12");

        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        Mockito.doReturn(stockTransVofficeDTO).when(stockTransVofficeService).save(any());

        ActiveKitVofficeDTO activeKitVofficeDTO1 = new ActiveKitVofficeDTO();
        Mockito.doReturn(activeKitVofficeDTO1).when(activeKitVofficeService).save(any());

        Mockito.doNothing().when(spyService).doSaveStockGoods(any(), any(), any());
        spyService.processApproveRequest(changeModelTransId);
    }

    /** -------------------------------------------------------------------- **/
    /** ---Test for changeModelTransBaseService.processCancelRequest method **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testProcessCancelRequest_1() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( "thread.toolkit.cancel.request.not.found");

        ChangeModelTransDTO changeModelTransDTO = new ChangeModelTransDTO();
        Mockito.doReturn(null).when(changeModelTransService).findOne(any());

        changeModelTransBaseService.processCancelRequest(1L );
    }

    @Test
    public void testProcessCancelRequest_2() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( "thread.toolkit.cancel.request.not.found");

        ChangeModelTransDTO changeModelTransDTO = new ChangeModelTransDTO();
        changeModelTransDTO.setStatus(7L);
        Mockito.doReturn(changeModelTransDTO).when(changeModelTransService).findOne(any());

        changeModelTransBaseService.processCancelRequest(1L );
    }

    @Test
    public void testProcessCancelRequest_3() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( "thread.toolkit.cancel.request.stock.trans");

        ChangeModelTransDTO changeModelTransDTO = new ChangeModelTransDTO();
        changeModelTransDTO.setStatus(6L);
        Mockito.doReturn(changeModelTransDTO).when(changeModelTransService).findOne(any());

        Date currentDate = new Date();
        PowerMockito.mockStatic(DbUtil.class);
        when(DbUtil.getSysDate(any(EntityManager.class))).thenReturn(currentDate);

        ChangeModelTransDTO changeModelTransDTO1 = new ChangeModelTransDTO();
        Mockito.doReturn(changeModelTransDTO1).when(changeModelTransService).save(any());

        StockTransDTO originalStockTrans = new StockTransDTO();
        Mockito.doReturn(null).when(stockTransService).findOne(any());

        changeModelTransBaseService.processCancelRequest(1L );
    }

    @Test
    public void testProcessCancelRequest_4() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( "thread.toolkit.cancel.request.stock.trans.action");

        ChangeModelTransDTO changeModelTransDTO = new ChangeModelTransDTO();
        changeModelTransDTO.setStatus(6L);
        Mockito.doReturn(changeModelTransDTO).when(changeModelTransService).findOne(any());

        Date currentDate = new Date();
        PowerMockito.mockStatic(DbUtil.class);
        when(DbUtil.getSysDate(any(EntityManager.class))).thenReturn(currentDate);

        ChangeModelTransDTO changeModelTransDTO1 = new ChangeModelTransDTO();
        Mockito.doReturn(changeModelTransDTO1).when(changeModelTransService).save(any());

        StockTransDTO originalStockTrans = new StockTransDTO();
        originalStockTrans.setNote("12");
        Mockito.doReturn(originalStockTrans).when(stockTransService).findOne(any());

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        Mockito.doReturn(null).when(stockTransActionService).getStockTransActionByIdAndStatus(any(), any());

        changeModelTransBaseService.processCancelRequest(1L );
    }

    @Test
    public void testProcessCancelRequest_5() throws Exception {

        ChangeModelTransDTO changeModelTransDTO = new ChangeModelTransDTO();
        changeModelTransDTO.setStatus(6L);
        Mockito.doReturn(changeModelTransDTO).when(changeModelTransService).findOne(any());

        Date currentDate = new Date();
        PowerMockito.mockStatic(DbUtil.class);
        when(DbUtil.getSysDate(any(EntityManager.class))).thenReturn(currentDate);

        ChangeModelTransDTO changeModelTransDTO1 = new ChangeModelTransDTO();
        Mockito.doReturn(changeModelTransDTO1).when(changeModelTransService).save(any());

        StockTransDTO originalStockTrans = new StockTransDTO();
        originalStockTrans.setNote("12");
        Mockito.doReturn(originalStockTrans).when(stockTransService).findOne(any());

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setStatus("2");
        Mockito.doReturn(stockTransActionDTO).when(stockTransActionService).getStockTransActionByIdAndStatus(any(), any());

        StockTransDTO stockTransDTO = new StockTransDTO();
        Mockito.doReturn(stockTransDTO).when(stockTransService).save(any());

        StockTransActionDTO stockTransActionSave = new StockTransActionDTO();
        Mockito.doReturn(stockTransActionDTO).when(stockTransActionService).save(any());

        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> lstDetail = Lists.newArrayList(stockTransDetailDTO);
        Mockito.doReturn(lstDetail).when(stockTransDetailService).findByStockTransId(any());

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        Mockito.doReturn(productOfferingDTO).when(productOfferingService).findOne(any());

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        Mockito.doReturn(productOfferTypeDTO).when(productOfferTypeService).findOne(any());

        ChangeModelTransBaseService spyService = PowerMockito.spy(changeModelTransBaseService);
        Mockito.doNothing().when(spyService).doSaveStockGoods(any(), any(), any());
        Mockito.doNothing().when(spyService).doSaveStockTotal(any(), any(), any(), any());

        spyService.processCancelRequest(1L );
    }

    /** -------------------------------------------------------------------- **/
    /** ---Test for changeModelTransBaseService.approveChangeProductKit method **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testApproveChangeProductKit_1() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( "mn.approve.change.model.trans.validate.err");

        StockTransDTO stockTransVoficeDTO = new StockTransDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        Date currentDate = new Date();
        PowerMockito.mockStatic(DbUtil.class);
        when(DbUtil.getSysDate(any(EntityManager.class))).thenReturn(currentDate);

        ChangeModelTransDTO changeModelTransDTO = new ChangeModelTransDTO();
        Mockito.doReturn(null).when(changeModelTransService).findOne(any());

        changeModelTransBaseService.approveChangeProductKit(1L, 1L, stockTransVoficeDTO, requiredRoleMap);
    }

    @Test
    public void testApproveChangeProductKit_2() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( "mn.approve.change.model.trans.validate.err");

        StockTransDTO stockTransVoficeDTO = new StockTransDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        Date currentDate = new Date();
        PowerMockito.mockStatic(DbUtil.class);
        when(DbUtil.getSysDate(any(EntityManager.class))).thenReturn(currentDate);

        ChangeModelTransDTO changeModelTransDTO = new ChangeModelTransDTO();
        changeModelTransDTO.setChangeModelTransId(0L);
        changeModelTransDTO.setRequestType(3L);
        changeModelTransDTO.setStatus(1L);
        Mockito.doReturn(changeModelTransDTO).when(changeModelTransService).findOne(any());

        changeModelTransBaseService.approveChangeProductKit(1L, 1L, stockTransVoficeDTO, requiredRoleMap);
    }

    @Test
    public void testApproveChangeProductKit_3() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( "mn.approve.change.model.trans.validate.err");

        StockTransDTO stockTransVoficeDTO = new StockTransDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        Date currentDate = new Date();
        PowerMockito.mockStatic(DbUtil.class);
        when(DbUtil.getSysDate(any(EntityManager.class))).thenReturn(currentDate);

        ChangeModelTransDTO changeModelTransDTO = new ChangeModelTransDTO();
        changeModelTransDTO.setChangeModelTransId(1L);
        changeModelTransDTO.setRequestType(3L);
        changeModelTransDTO.setStatus(1L);
        Mockito.doReturn(changeModelTransDTO).when(changeModelTransService).findOne(any());

        changeModelTransBaseService.approveChangeProductKit(1L, 1L, stockTransVoficeDTO, requiredRoleMap);
    }

    @Test
    public void testApproveChangeProductKit_4() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( "mn.approve.change.model.trans.validate.err");

        StockTransDTO stockTransVoficeDTO = new StockTransDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        Date currentDate = new Date();
        PowerMockito.mockStatic(DbUtil.class);
        when(DbUtil.getSysDate(any(EntityManager.class))).thenReturn(currentDate);

        ChangeModelTransDTO changeModelTransDTO = new ChangeModelTransDTO();
        changeModelTransDTO.setChangeModelTransId(1L);
        changeModelTransDTO.setRequestType(2L);
        changeModelTransDTO.setStatus(1L);
        Mockito.doReturn(changeModelTransDTO).when(changeModelTransService).findOne(any());

        changeModelTransBaseService.approveChangeProductKit(1L, 1L, stockTransVoficeDTO, requiredRoleMap);
    }

    @Test
    public void testApproveChangeProductKit_5() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( "utilities.exchange.card.bankplus.not.transaction");

        StockTransDTO stockTransVoficeDTO = new StockTransDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        Date currentDate = new Date();
        PowerMockito.mockStatic(DbUtil.class);
        when(DbUtil.getSysDate(any(EntityManager.class))).thenReturn(currentDate);

        ChangeModelTransDTO changeModelTransDTO = new ChangeModelTransDTO();
        changeModelTransDTO.setChangeModelTransId(1L);
        changeModelTransDTO.setRequestType(2L);
        changeModelTransDTO.setStatus(0L);
        Mockito.doReturn(changeModelTransDTO).when(changeModelTransService).findOne(any());

        ChangeModelTransDTO changeModelTransDTO1 = new ChangeModelTransDTO();
        Mockito.doReturn(changeModelTransDTO1).when(changeModelTransService).save(any());

        StockTransDTO originalStockTrans = new StockTransDTO();
        Mockito.doReturn(null).when(stockTransService).findOne(any());

        changeModelTransBaseService.approveChangeProductKit(1L, 1L, stockTransVoficeDTO, requiredRoleMap);
    }

    @Test
    public void testApproveChangeProductKit_6() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( "utilities.exchange.card.bankplus.not.transaction");

        StockTransDTO stockTransVoficeDTO = new StockTransDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        Date currentDate = new Date();
        PowerMockito.mockStatic(DbUtil.class);
        when(DbUtil.getSysDate(any(EntityManager.class))).thenReturn(currentDate);

        ChangeModelTransDTO changeModelTransDTO = new ChangeModelTransDTO();
        changeModelTransDTO.setChangeModelTransId(1L);
        changeModelTransDTO.setRequestType(2L);
        changeModelTransDTO.setStatus(0L);
        Mockito.doReturn(changeModelTransDTO).when(changeModelTransService).findOne(any());

        ChangeModelTransDTO changeModelTransDTO1 = new ChangeModelTransDTO();
        Mockito.doReturn(changeModelTransDTO1).when(changeModelTransService).save(any());

        StockTransDTO originalStockTrans = new StockTransDTO();
        originalStockTrans.setNote("12");
        Mockito.doReturn(originalStockTrans).when(stockTransService).findOne(any());

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setStatus("2");
        Mockito.doReturn(null).when(stockTransActionService).getStockTransActionByIdAndStatus(any(), any());

        ChangeModelTransBaseService spyService = PowerMockito.spy(changeModelTransBaseService);
        Mockito.doNothing().when(spyService).doSignVoffice(any(), any(), any(), any());
        Mockito.doNothing().when(spyService).doInsertVoffice(any(), any(), any(), any(), any());

        spyService.approveChangeProductKit(1L, 1L, stockTransVoficeDTO, requiredRoleMap);
    }

    @Test
    public void testApproveChangeProductKit_7() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( "utilities.exchange.card.bankplus.not.transaction");

        StockTransDTO stockTransVoficeDTO = new StockTransDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        Date currentDate = new Date();
        PowerMockito.mockStatic(DbUtil.class);
        when(DbUtil.getSysDate(any(EntityManager.class))).thenReturn(currentDate);

        ChangeModelTransDTO changeModelTransDTO = new ChangeModelTransDTO();
        changeModelTransDTO.setChangeModelTransId(1L);
        changeModelTransDTO.setRequestType(2L);
        changeModelTransDTO.setStatus(0L);
        Mockito.doReturn(changeModelTransDTO).when(changeModelTransService).findOne(any());

        ChangeModelTransDTO changeModelTransDTO1 = new ChangeModelTransDTO();
        Mockito.doReturn(changeModelTransDTO1).when(changeModelTransService).save(any());

        StockTransDTO originalStockTrans = new StockTransDTO();
        originalStockTrans.setNote("12");
        Mockito.doReturn(originalStockTrans).when(stockTransService).findOne(any());

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setStockTransActionId(0L);
        Mockito.doReturn(null).when(stockTransActionService).getStockTransActionByIdAndStatus(any(), any());

        ChangeModelTransBaseService spyService = PowerMockito.spy(changeModelTransBaseService);
        Mockito.doNothing().when(spyService).doSignVoffice(any(), any(), any(), any());
        Mockito.doNothing().when(spyService).doInsertVoffice(any(), any(), any(), any(), any());

        spyService.approveChangeProductKit(1L, 1L, stockTransVoficeDTO, requiredRoleMap);
    }

    @Test
    public void testApproveChangeProductKit_8() throws Exception {

        StockTransDTO stockTransVoficeDTO = new StockTransDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

        Date currentDate = new Date();
        PowerMockito.mockStatic(DbUtil.class);
        when(DbUtil.getSysDate(any(EntityManager.class))).thenReturn(currentDate);

        ChangeModelTransDTO changeModelTransDTO = new ChangeModelTransDTO();
        changeModelTransDTO.setChangeModelTransId(1L);
        changeModelTransDTO.setRequestType(2L);
        changeModelTransDTO.setStatus(0L);
        Mockito.doReturn(changeModelTransDTO).when(changeModelTransService).findOne(any());

        ChangeModelTransDTO changeModelTransDTO1 = new ChangeModelTransDTO();
        Mockito.doReturn(changeModelTransDTO1).when(changeModelTransService).save(any());

        StockTransDTO originalStockTrans = new StockTransDTO();
        originalStockTrans.setNote("12");
        Mockito.doReturn(originalStockTrans).when(stockTransService).findOne(any());

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setStockTransActionId(1L);
        Mockito.doReturn(stockTransActionDTO).when(stockTransActionService).getStockTransActionByIdAndStatus(any(), any());

        ChangeModelTransBaseService spyService = PowerMockito.spy(changeModelTransBaseService);
        Mockito.doNothing().when(spyService).doSignVoffice(any(), any(), any(), any());
        Mockito.doNothing().when(spyService).doInsertVoffice(any(), any(), any(), any(), any());

        spyService.approveChangeProductKit(1L, 1L, stockTransVoficeDTO, requiredRoleMap);
    }

    /** -------------------------------------------------------------------- **/
    /** ---Test for changeModelTransBaseService.doInsertVoffice method ----- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoInsertVoffice_1() throws Exception {
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        ChangeModelTransDTO changeModelTransDTO = new ChangeModelTransDTO();

        stockTransDTO.setSignVoffice(false);
        changeModelTransBaseService.doInsertVoffice(stockTransDTO, stockTransActionDTO, requiredRoleMap, flagStockDTO, changeModelTransDTO);
    }

    @Test
    public void testDoInsertVoffice_2() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( "stockTrans.validate.signFlow.emptyDetail");

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        ChangeModelTransDTO changeModelTransDTO = new ChangeModelTransDTO();

        stockTransDTO.setSignVoffice(true);

        SignFlowDetailDTO signFlowDetailDTO = new SignFlowDetailDTO();
        List<SignFlowDetailDTO> lstSignFlowDetail = Lists.newArrayList(signFlowDetailDTO);

        Mockito.doReturn(null).when(signFlowDetailService).findBySignFlowId(any());

                changeModelTransBaseService.doInsertVoffice(stockTransDTO, stockTransActionDTO, requiredRoleMap, flagStockDTO, changeModelTransDTO);
    }

    @Test
    public void testDoInsertVoffice_3() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( "stockTrans.validate.signFlow.emptyEmail");

        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        ChangeModelTransDTO changeModelTransDTO = new ChangeModelTransDTO();

        stockTransDTO.setSignVoffice(true);

        SignFlowDetailDTO signFlowDetailDTO = new SignFlowDetailDTO();
        signFlowDetailDTO.setEmail(null);
        List<SignFlowDetailDTO> lstSignFlowDetail = Lists.newArrayList(signFlowDetailDTO);

        Mockito.doReturn(lstSignFlowDetail).when(signFlowDetailService).findBySignFlowId(any());

        changeModelTransBaseService.doInsertVoffice(stockTransDTO, stockTransActionDTO, requiredRoleMap, flagStockDTO, changeModelTransDTO);
    }

    @Test
    public void testDoInsertVoffice_4() throws Exception {

        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setCreateDatetime(new Date());
        stockTransDTO.setUserName("2");

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        ChangeModelTransDTO changeModelTransDTO = new ChangeModelTransDTO();

        stockTransDTO.setSignVoffice(true);

        SignFlowDetailDTO signFlowDetailDTO = new SignFlowDetailDTO();
        SignFlowDetailDTO signFlowDetailDTO1 = new SignFlowDetailDTO();
        signFlowDetailDTO.setEmail("gmail@test");
        signFlowDetailDTO1.setEmail("gmail1@test");
        List<SignFlowDetailDTO> lstSignFlowDetail = Lists.newArrayList(signFlowDetailDTO, signFlowDetailDTO1);

        Mockito.doReturn(lstSignFlowDetail).when(signFlowDetailService).findBySignFlowId(any());

        PowerMockito.mockStatic(DbUtil.class);
        when(DbUtil.getSequence(any(EntityManager.class), any())).thenReturn(1L);

        PowerMockito.mockStatic(DateUtil.class);
        PowerMockito.when(DateUtil.dateToStringWithPattern(any(), any())).thenReturn("12");

        StaffDTO staff = new StaffDTO();
        Mockito.doReturn(staff).when(staffService).findOne(any());

        Mockito.doReturn(true).when(shopService).isCenterOrBranch(any());

        BaseMessage baseMessage = new BaseMessage();
        Mockito.doReturn(baseMessage).when(activeKitVofficeService).create(any());

        changeModelTransBaseService.doInsertVoffice(stockTransDTO, stockTransActionDTO, requiredRoleMap, flagStockDTO, changeModelTransDTO);
    }

    /** -------------------------------------------------------------------- **/
    /** ---Test for changeModelTransBaseService.doPrepareData method ------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoPrepareData_1() throws Exception {
    }

    /** -------------------------------------------------------------------- **/
    /** ---Test for changeModelTransBaseService.doValidate method ---------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoValidate_1() throws Exception {

    }

}