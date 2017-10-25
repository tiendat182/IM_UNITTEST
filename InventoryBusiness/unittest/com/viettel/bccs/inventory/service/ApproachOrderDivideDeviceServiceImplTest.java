package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.im1.service.StockTotalAuditIm1Service;
import com.viettel.bccs.im1.service.StockTotalIm1Service;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockRequest;
import com.viettel.bccs.inventory.repo.ApproachOrderDivideDeviceRepo;
import com.viettel.bccs.inventory.repo.OptionSetValueRepo;
import com.viettel.bccs.inventory.repo.StockRequestRepo;
import com.viettel.bccs.inventory.service.BaseStock.BaseStockService;
import com.viettel.bccs.sale.dto.SaleTransSerialDTO;
import com.viettel.bccs.sale.model.SaleTransSerial;
import com.viettel.bccs.sale.service.SaleTransSerialServiceImpl;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.service.BaseServiceImpl;
import com.viettel.ws.common.utils.WebServiceHandler;
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
import org.springframework.beans.factory.annotation.Autowired;

import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by TruNV on 9/9/2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ApproachOrderDivideDeviceServiceImpl.class, BaseServiceImpl.class, ApproachOrderDivideDeviceService.class, DbUtil.class, DateUtil.class})
@PowerMockIgnore("javax.management.*")
public class ApproachOrderDivideDeviceServiceImplTest {

    @InjectMocks
    private ApproachOrderDivideDeviceServiceImpl approachOrderDivideDeviceService;

    @Mock
    private BaseMapper<StockRequest, StockRequestDTO> mapper;

    @Mock
    private EntityManager em;

    @Mock
    private EntityManager emLib;

    @Mock
    private ApproachOrderDivideDeviceRepo approachOrderDivideDeviceRepo;

    @Mock
    private StockRequestService stockRequestService;

    @Mock
    private ShopService shopService;

    @Mock
    private StockRequestRepo repository;

    @Mock
    private StockTransActionService stockTransActionService;

    @Mock
    private StockTransVofficeService stockTransVofficeService;

    @Mock
    private StockTransService stockTransService;

    @Mock
    private OptionSetValueService optionSetValueService;

    @Mock
    private ProductOfferingService productOfferingService;

    @Mock
    private StockTotalService stockTotalService;

    @Mock
    private StockTotalAuditService stockTotalAuditService;

    @Mock
    private StockTotalIm1Service stockTotalIm1Service;

    @Mock
    private StaffService staffService;

    @Mock
    private StockTotalAuditIm1Service stockTotalAuditIm1Service;

    @Mock
    private OptionSetValueRepo optionSetValueRepo;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for approachOrderDivideDeviceService.getListOrderDivideDevicePendingApproach method **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testGetListOrderDivideDevicePendingApproach_1() throws Exception {
        String shopPath = "1";
        Date fromDate = new Date();
        Date toDate = new Date();
        Long ownerId = 1L;
        String requestCode = "1";
        String status = "1";

        StockRequestDTO stockRequestDTO = new StockRequestDTO();
        List<StockRequestDTO> stockRequestDTOS = Lists.newArrayList(stockRequestDTO);
        Mockito.doReturn(stockRequestDTOS).when(approachOrderDivideDeviceRepo).getListOrderDivideDevicePendingApproach(any(), any(), any(), any(), any(), any());
        approachOrderDivideDeviceService.getListOrderDivideDevicePendingApproach(shopPath, fromDate, toDate, ownerId, requestCode, status);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for approachOrderDivideDeviceService.approachOrderDivide method **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testApproachOrderDivide_1() throws Exception {
        StockRequestDTO stockRequestDTOSelected = new StockRequestDTO();
        StockRequestDTO stockRequestDTO = new StockRequestDTO();

        ApproachOrderDivideDeviceServiceImpl spyService = PowerMockito.spy(approachOrderDivideDeviceService);
        PowerMockito.doReturn(stockRequestDTO).when(spyService, "validateBeforePerform", any());

        Date currentDate = new Date();
        PowerMockito.mockStatic(DbUtil.class);
        when(DbUtil.getSysDate(any(EntityManager.class))).thenReturn(currentDate);

        StockRequestDTO stockRequestDTO1 = new StockRequestDTO();
        StockRequest stockRequest = new StockRequest();
        StockRequest stockRequest1 = new StockRequest();
        Mockito.when(mapper.toPersistenceBean(stockRequestDTO1)).thenReturn(stockRequest);
        setFinalStatic(ApproachOrderDivideDeviceServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        Mockito.when(repository.save(stockRequest)).thenReturn(stockRequest1);

        PowerMockito.mockStatic(DbUtil.class);
        when(DbUtil.getSequence(any(EntityManager.class), any())).thenReturn(1L);

        PowerMockito.mockStatic(DateUtil.class);
        PowerMockito.when(DateUtil.dateToStringWithPattern(any(), any())).thenReturn("12");

        StockTransActionDTO resultSearch = new StockTransActionDTO();
        Mockito.doReturn(resultSearch).when(stockTransActionService).findStockTransActionDTO(any());

        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        Mockito.doReturn(stockTransVofficeDTO).when(stockTransVofficeService).save(any());

        spyService.approachOrderDivide(stockRequestDTOSelected);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for approachOrderDivideDeviceService.rejectOrderDivide method **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testRejectOrderDivide_1() throws Exception {
        StockRequestDTO stockRequestDTOSelected = new StockRequestDTO();

        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        List<StockTransSerialDTO> stockTransSerialDTOS = Lists.newArrayList(stockTransSerialDTO);
        stockTransFullDTO.setLstSerial(stockTransSerialDTOS);
        List<StockTransFullDTO> stockTransFullDTOList = Lists.newArrayList(stockTransFullDTO);
        Long userRejectId = 1L;

        StockRequestDTO stockRequestDTO = new StockRequestDTO();
        ApproachOrderDivideDeviceServiceImpl spyService = PowerMockito.spy(approachOrderDivideDeviceService);
        PowerMockito.doReturn(stockRequestDTO).when(spyService, "validateBeforePerform", any());

        Date currentDate = new Date();
        PowerMockito.mockStatic(DbUtil.class);
        when(DbUtil.getSysDate(any(EntityManager.class))).thenReturn(currentDate);

        StockRequestDTO stockRequestDTO1 = new StockRequestDTO();
        StockRequest stockRequest = new StockRequest();
        StockRequest stockRequest1 = new StockRequest();
        Mockito.when(mapper.toPersistenceBean(stockRequestDTO1)).thenReturn(stockRequest);
        setFinalStatic(ApproachOrderDivideDeviceServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        Mockito.when(repository.save(stockRequest)).thenReturn(stockRequest1);

        StockTransDTO stockTransDTO = new StockTransDTO();
        Mockito.doReturn(stockTransDTO).when(stockTransService).findOne(any());
        Mockito.doReturn(stockTransDTO).when(stockTransService).save(any());

        StockTransActionDTO stockTransActionDTO1 = new StockTransActionDTO();
        Mockito.doReturn(stockTransActionDTO1).when(stockTransActionService).save(any());

        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue("2");
        List<OptionSetValueDTO> optionSetValueDTOList = Lists.newArrayList(optionSetValueDTO);
        Mockito.doReturn(optionSetValueDTOList).when(optionSetValueService).getByOptionSetCode(any());

        StockTransActionDTO resultSearch = new StockTransActionDTO();
        Mockito.doReturn(resultSearch).when(stockTransActionService).findStockTransActionDTO(any());

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setProductOfferTypeId(1L);
        productOfferingDTO.setStatus("1");
        Mockito.doReturn(productOfferingDTO).when(productOfferingService).findOne(any());
        StockTotalDTO totalDTOSearch = new StockTotalDTO();
        StockTotalAuditDTO totalAuditInput = new StockTotalAuditDTO();
        PowerMockito.doNothing().when(spyService, "refundTotal", totalDTOSearch, totalAuditInput, stockTransDTO.getFromOwnerType(), stockTransDTO.getFromOwnerId(), stockTransFullDTO.getProdOfferId(), stockTransFullDTO.getStateId(), stockTransFullDTO.getQuantity(), new Date(), true);
        PowerMockito.doNothing().when(spyService, "updateStockX", 1L, "1", "1", new Date(), 1L, 1L, 1L, 1L, true);

        spyService.rejectOrderDivide(stockRequestDTOSelected, stockTransFullDTOList, userRejectId);

    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for approachOrderDivideDeviceService.updateStockX method -- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testUpdateStockX_1() throws Exception {

    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for approachOrderDivideDeviceService.refundTotal method --- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testRefundTotal_1() throws Exception {
    }

    static void setFinalStatic(Field field, Object newValue, final Object targetObject) throws Exception {
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(targetObject, newValue);
    }

}