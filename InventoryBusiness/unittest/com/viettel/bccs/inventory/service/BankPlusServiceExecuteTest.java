package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.dto.OptionSetValueDTO;
import com.viettel.bccs.inventory.dto.StaffDTO;
import com.viettel.bccs.inventory.dto.StockTransDTO;
import com.viettel.bccs.inventory.dto.StockTransDetailDTO;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.service.BaseServiceImpl;
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

import static org.mockito.Mockito.*;


import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author DatLT
 * @date 29/09/2017
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({BankPlusServiceExecute.class, BaseServiceImpl.class, DbUtil.class, EntityManager.class, DataUtil.class, DateUtil.class})
@PowerMockIgnore({"javax.management.*", "org.apache.http.conn.ssl.*", "javax.net.ssl.*" , "javax.crypto.*"})
public class BankPlusServiceExecuteTest {
    @InjectMocks
    BankPlusServiceExecute bankPlusServiceExecute;

    @Mock
    private EntityManager em;

    @Mock
    private EntityManager emLib;

    @Mock
    private StaffService staffService;

    @Mock
    private StockTransService stockTransService;

    @Mock
    private StockTransActionService stockTransActionService;

    @Mock
    private StockTransDetailService stockTransDetailService;

    @Mock
    private StockTransSerialService stockTransSerialService;

    @Mock
    private OptionSetValueService optionSetValueService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    /** ------------------------------------------------------------------------------ **/
    /** --- Test for bankPlusServiceExecute.createTransBankplus method ------- **/
    /*** -----------------------------------------------------------------------------**/
    @Test(expected = Exception.class)
    public void testCreateTransBankplus_1() throws  Exception{
        Long fromOwnerId=1L;
        Long fromOwnerType = 1L;
        Long prodOfferId = 1L;
        String serial = "serial";
        String fromOwnerCode = "fromOwnerCode";
        Long staffId = 1L;
        Date currentDate = new Date();
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDTO.setStockTransId(1L);
        BankPlusServiceExecute spyService = PowerMockito.spy(bankPlusServiceExecute);
        Mockito.doReturn(currentDate).when(spyService).getSysDate(em);
        when(stockTransService.save(any())).thenReturn(stockTransDTO);
        when(stockTransDetailService.save(any())).thenReturn(stockTransDetailDTO);
        spyService.createTransBankplus(fromOwnerId, fromOwnerType, prodOfferId, serial, fromOwnerCode, staffId);
    }

    @Test(expected = Exception.class)
    public void testCreateTransBankplus_2() throws  Exception{
        Long fromOwnerId=1L;
        Long fromOwnerType = 1L;
        Long prodOfferId = 1L;
        String serial = "serial";
        String fromOwnerCode = "fromOwnerCode";
        Long staffId = 1L;
        Date currentDate = new Date();
        StockTransDTO stockTransDTO = new StockTransDTO();
        StaffDTO staffDTO = new StaffDTO();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDTO.setStockTransId(1L);
        BankPlusServiceExecute spyService = PowerMockito.spy(bankPlusServiceExecute);
        Mockito.doReturn(currentDate).when(spyService).getSysDate(em);
        when(staffService.findOne(any())).thenReturn(staffDTO);
        when(stockTransService.save(any())).thenReturn(stockTransDTO);
        when(stockTransDetailService.save(any())).thenReturn(stockTransDetailDTO);
        spyService.createTransBankplus(fromOwnerId, fromOwnerType, prodOfferId, serial, fromOwnerCode, staffId);
    }

    @Test
    public void testCreateTransBankplus_3() throws  Exception{
        Long fromOwnerId=1L;
        Long fromOwnerType = 1L;
        Long prodOfferId = 1L;
        String serial = "serial";
        String fromOwnerCode = "fromOwnerCode";
        Long staffId = 1L;
        Date currentDate = new Date();
        StockTransDTO stockTransDTO = new StockTransDTO();
        StaffDTO staffDTO = new StaffDTO();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        stockTransDTO.setStockTransId(1L);
        optionSetValueDTO.setValue("1");
        BankPlusServiceExecute spyService = PowerMockito.spy(bankPlusServiceExecute);
        Mockito.doReturn(currentDate).when(spyService).getSysDate(em);
        when(staffService.findOne(any())).thenReturn(staffDTO);
        when(stockTransService.save(any())).thenReturn(stockTransDTO);
        when(stockTransDetailService.save(any())).thenReturn(stockTransDetailDTO);
       PowerMockito.doNothing().when(spyService,"updateStockKit", any());
        when(optionSetValueService.getByOptionSetCode(any())).thenReturn(lstConfigEnableBccs1);
        PowerMockito.doNothing().when(spyService,"debitTotal", any(), any(), any(), any());
        PowerMockito.doNothing().when(spyService,"debitTotalIm1", any(), any(), any(), any());
        PowerMockito.doNothing().when(spyService, "updateStockKitIm1", any());
        spyService.createTransBankplus(fromOwnerId, fromOwnerType, prodOfferId, serial, fromOwnerCode, staffId);
    }

    /** ------------------------------------------------------------------------------ **/
    /** --------- Test for bankPlusServiceExecute.debitTotal method ------------ **/
    /*** -----------------------------------------------------------------------------**/
    @Test(expected = Exception.class)
    public void testDebitTotal_1() throws  Exception {
        Long ownerId = 1L;
        Long ownerType=2L;
        Long prodOfferId = 3L;
        Long stateId =4L;
        BankPlusServiceExecute spyService = PowerMockito.spy(bankPlusServiceExecute);
        Query queryObject = PowerMockito.mock(Query.class);
        when(em.createNativeQuery(any())).thenReturn(queryObject);
        when(queryObject.executeUpdate()).thenReturn(0);
        Whitebox.invokeMethod(spyService, "debitTotal", ownerId, ownerType, prodOfferId, stateId);
    }

    /** ------------------------------------------------------------------------------ **/
    /** --------- Test for bankPlusServiceExecute.debitTotalIm1 method ------------ **/
    /*** -----------------------------------------------------------------------------**/
    @Test(expected = Exception.class)
    public void testDebitTotalIm1_1() throws  Exception {
        Long ownerId = 1L;
        Long ownerType=2L;
        Long prodOfferId = 2L;
        Long stateId =4L;
        BankPlusServiceExecute spyService = PowerMockito.spy(bankPlusServiceExecute);
        Query queryObject = PowerMockito.mock(Query.class);
        when(emLib.createNativeQuery(any())).thenReturn(queryObject);
        when(queryObject.executeUpdate()).thenReturn(0);
        Whitebox.invokeMethod(spyService, "debitTotalIm1", ownerId, ownerType, prodOfferId, stateId);
    }

    /** ------------------------------------------------------------------------------ **/
    /** --------- Test for bankPlusServiceExecute.getStockModelIm1DTO method ------------ **/
    /*** -----------------------------------------------------------------------------**/
    @Test
    public void testGetStockModelIm1DTO_1() throws  Exception {
        String stockModelCode = "stockModelCode";
        Object[] objects = new Object[6];
        List<Object[]> lsData = new ArrayList<>();
        lsData.add(objects);
        Query queryObject = PowerMockito.mock(Query.class);
        when(emLib.createNativeQuery(any())).thenReturn(queryObject);
        when(queryObject.getResultList()).thenReturn(lsData);
        bankPlusServiceExecute.getStockModelIm1DTO(stockModelCode);
    }

    @Test
    public void testGetStockModelIm1DTO_2() throws  Exception {
        String stockModelCode = "stockModelCode";
        Object[] objects = new Object[6];
        List<Object[]> lsData = new ArrayList<>();
        lsData.add(objects);
        Query queryObject = PowerMockito.mock(Query.class);
        when(emLib.createNativeQuery(any())).thenReturn(queryObject);
        when(queryObject.getResultList()).thenReturn(null);
        bankPlusServiceExecute.getStockModelIm1DTO(stockModelCode);
    }

    /** ------------------------------------------------------------------------------ **/
    /** --------- Test for bankPlusServiceExecute.getStockKitIm1BySerial method ------------ **/
    /*** -----------------------------------------------------------------------------**/
    @Test
    public void testGetStockKitIm1BySerial_1() throws  Exception {
        String serial = "serial";
        Object[] objects = new Object[7];
        List<Object[]> list = new ArrayList<>();
        list.add(objects);
        Query queryObject = PowerMockito.mock(Query.class);
        when(emLib.createNativeQuery(any())).thenReturn(queryObject);
        when(queryObject.getResultList()).thenReturn(list);
        bankPlusServiceExecute.getStockKitIm1BySerial(serial);
    }

    @Test
    public void testGetStockKitIm1BySerial_2() throws  Exception {
        String serial = "serial2";
        Query queryObject = PowerMockito.mock(Query.class);
        when(emLib.createNativeQuery(any())).thenReturn(queryObject);
        when(queryObject.getResultList()).thenReturn(null);
        bankPlusServiceExecute.getStockKitIm1BySerial(serial);
    }

    /** ------------------------------------------------------------------------------ **/
    /** --------- Test for bankPlusServiceExecute.updateStockKit method ------------ **/
    /*** -----------------------------------------------------------------------------**/
    @Test(expected = Exception.class)
    public void testUpdateStockKit_1() throws  Exception {
        String serial = "serial";
        BankPlusServiceExecute spyService = PowerMockito.spy(bankPlusServiceExecute);
        Query query = PowerMockito.mock(Query.class);
        when(em.createNativeQuery(any())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(0);
        Whitebox.invokeMethod(spyService, "updateStockKit", serial);
    }

    /** ------------------------------------------------------------------------------ **/
    /** --------- Test for bankPlusServiceExecute.updateStockKitIm1 method ------------ **/
    /*** -----------------------------------------------------------------------------**/
    @Test(expected = Exception.class)
    public void testUpdateStockKitIm1_1() throws  Exception {
        String serial = "serial1";
        BankPlusServiceExecute spyService = PowerMockito.spy(bankPlusServiceExecute);
        Query query = PowerMockito.mock(Query.class);
        when(emLib.createNativeQuery(any())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(0);
        Whitebox.invokeMethod(spyService, "updateStockKitIm1", serial);
    }
}