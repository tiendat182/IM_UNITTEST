package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.im1.dto.StockModelIm1DTO;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.service.BaseServiceImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.powermock.api.support.membermodification.MemberMatcher.method;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.reflect.Whitebox;

import javax.persistence.EntityManager;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * @author DatLT
 * @date 9/25/2017
 */
@RunWith(MockitoJUnitRunner.class)
@PrepareForTest({BaseServiceImpl.class, BankPlusService.class, BankPlusServiceImpl.class, DataUtil.class})
public class BankPlusServiceImplTest {

    @InjectMocks
    BankPlusServiceImpl bankPlusService;

    @Mock
    private EntityManager em;

    @Mock
    private StockKitService stockKitService;

    @Mock
    private StaffService staffService;

    @Mock
    private ProductOfferingService productOfferingService;

    @Mock
    private ShopService shopService;

    @Mock
    private BankPlusServiceExecute bankPlusServiceExecute;

    @Mock
    private OptionSetValueService optionSetValueService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method BankPlusServiceImpl.checkSerial
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCheckSerial_1() throws  Exception{
        String staffCode = "";
        String serial = "serial";
        String prodOfferCode = "prodOfferCode";
        bankPlusService.checkSerial(staffCode, serial, prodOfferCode);
    }

    @Test
    public void testCheckSerial_2() throws  Exception{
        String staffCode = "staffCode";
        String serial = "serial";
        String prodOfferCode = "prodOfferCode";
        when(staffService.getStaffByStaffCode(any())).thenReturn(null);
        bankPlusService.checkSerial(staffCode, serial, prodOfferCode);
    }

    @Test
    public void testCheckSerial_3() throws  Exception{
        String staffCode = "staffCode";
        String serial = "serial";
        String prodOfferCode = "prodOfferCode";
        StaffDTO staffDTO = new StaffDTO();
        when(staffService.getStaffByStaffCode(any())).thenReturn(staffDTO);
        when(stockKitService.getStockKitBySerial(any())).thenReturn(null);
        bankPlusService.checkSerial(staffCode, serial, prodOfferCode);
    }

    @Test
    public void testCheckSerial_4() throws  Exception{
        String staffCode = "staffCode";
        String serial = "serial";
        String prodOfferCode = "prodOfferCode";
        StaffDTO staffDTO = new StaffDTO();
        StockKitDTO stockKitDTO = new StockKitDTO();
        when(staffService.getStaffByStaffCode(any())).thenReturn(staffDTO);
        when(stockKitService.getStockKitBySerial(any())).thenReturn(stockKitDTO);
        when(productOfferingService.getProductByCode(any())).thenReturn(null);
        bankPlusService.checkSerial(staffCode, serial, prodOfferCode);
    }

    @Test
    public void testCheckSerial_5() throws  Exception{
        String staffCode = "staffCode";
        String serial = "serial";
        String prodOfferCode = "prodOfferCode";
        StaffDTO staffDTO = new StaffDTO();
        StockKitDTO stockKitDTO = new StockKitDTO();
        ProductOfferingDTO offeringDTO = new ProductOfferingDTO();
        stockKitDTO.setProdOfferId(1L);
        offeringDTO.setProductOfferingId(2L);
        when(staffService.getStaffByStaffCode(any())).thenReturn(staffDTO);
        when(stockKitService.getStockKitBySerial(any())).thenReturn(stockKitDTO);
        when(productOfferingService.getProductByCode(any())).thenReturn(offeringDTO);
        bankPlusService.checkSerial(staffCode, serial, prodOfferCode);
    }

    @Test
    public void testCheckSerial_6() throws  Exception{
        String staffCode = "staffCode";
        String serial = "serial";
        String prodOfferCode = "prodOfferCode";
        StaffDTO staffDTO = new StaffDTO();
        StockKitDTO stockKitDTO = new StockKitDTO();
        ProductOfferingDTO offeringDTO = new ProductOfferingDTO();
        stockKitDTO.setProdOfferId(1L);
        stockKitDTO.setOwnerType(1L);
        offeringDTO.setProductOfferingId(1L);
        when(staffService.getStaffByStaffCode(any())).thenReturn(staffDTO);
        when(productOfferingService.getProductByCode(any())).thenReturn(offeringDTO);
        when(stockKitService.getStockKitBySerial(any())).thenReturn(stockKitDTO);
        bankPlusService.checkSerial(staffCode, serial, prodOfferCode);
    }

    @Test
    public void testCheckSerial_7() throws  Exception{
        String staffCode = "staffCode";
        String serial = "serial";
        String prodOfferCode = "prodOfferCode";
        StaffDTO staffDTO = new StaffDTO();
        StockKitDTO stockKitDTO = new StockKitDTO();
        ProductOfferingDTO offeringDTO = new ProductOfferingDTO();
        stockKitDTO.setOwnerType(2L);
        stockKitDTO.setProdOfferId(1L);
        offeringDTO.setProductOfferingId(1L);
        stockKitDTO.setOwnerId(1L);
        staffDTO.setStaffId(1L);
        stockKitDTO.setStatus(2L);
        when(staffService.getStaffByStaffCode(any())).thenReturn(staffDTO);
        when(stockKitService.getStockKitBySerial(any())).thenReturn(stockKitDTO);
        when(productOfferingService.getProductByCode(any())).thenReturn(offeringDTO);
        bankPlusService.checkSerial(staffCode, serial, prodOfferCode);
    }

    @Test
    public void testCheckSerial_8() throws  Exception{
        String staffCode = "staffCode";
        String serial = "serial";
        String prodOfferCode = "prodOfferCode";
        StaffDTO staffDTO = new StaffDTO();
        StockKitDTO stockKitDTO = new StockKitDTO();
        ProductOfferingDTO offeringDTO = new ProductOfferingDTO();
        stockKitDTO.setOwnerType(2L);
        stockKitDTO.setProdOfferId(1L);
        offeringDTO.setProductOfferingId(1L);
        stockKitDTO.setOwnerId(1L);
        staffDTO.setStaffId(1L);
        stockKitDTO.setStatus(1L);
        stockKitDTO.setStateId(2L);
        when(staffService.getStaffByStaffCode(any())).thenReturn(staffDTO);
        when(stockKitService.getStockKitBySerial(any())).thenReturn(stockKitDTO);
        when(productOfferingService.getProductByCode(any())).thenReturn(offeringDTO);
        bankPlusService.checkSerial(staffCode, serial, prodOfferCode);
    }

    @Test
    public void testCheckSerial_9() throws  Exception{
        String staffCode = "staffCode";
        String serial = "serial";
        String prodOfferCode = "prodOfferCode";
        StaffDTO staffDTO = new StaffDTO();
        StockKitDTO stockKitDTO = new StockKitDTO();
        ProductOfferingDTO offeringDTO = new ProductOfferingDTO();
        stockKitDTO.setOwnerType(2L);
        stockKitDTO.setProdOfferId(1L);
        offeringDTO.setProductOfferingId(1L);
        stockKitDTO.setOwnerId(1L);
        staffDTO.setStaffId(1L);
        stockKitDTO.setStateId(1L);
        stockKitDTO.setStatus(1L);
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setCreatedBy("datlt");
        optionSetValueDTO.setValue("1");
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        when(staffService.getStaffByStaffCode(any())).thenReturn(staffDTO);
        when(stockKitService.getStockKitBySerial(any())).thenReturn(stockKitDTO);
        when(productOfferingService.getProductByCode(any())).thenReturn(offeringDTO);
        bankPlusService.checkSerial(staffCode, serial, prodOfferCode);
    }

    @Test
    public void testCheckSerial_10() throws  Exception{
        String staffCode = "staffCode";
        String serial = "serial";
        String prodOfferCode = "prodOfferCode";
        StaffDTO staffDTO = new StaffDTO();
        StockKitDTO stockKitDTO = new StockKitDTO();
        ProductOfferingDTO offeringDTO = new ProductOfferingDTO();
        stockKitDTO.setOwnerType(2L);
        stockKitDTO.setProdOfferId(1L);
        offeringDTO.setProductOfferingId(1L);
        stockKitDTO.setOwnerId(1L);
        staffDTO.setStaffId(1L);
        stockKitDTO.setStatus(1L);
        stockKitDTO.setStateId(1L);
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setCreatedBy("datlt");
        optionSetValueDTO.setValue("1");
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        when(staffService.getStaffByStaffCode(any())).thenReturn(staffDTO);
        when(stockKitService.getStockKitBySerial(any())).thenReturn(stockKitDTO);
        when(productOfferingService.getProductByCode(any())).thenReturn(offeringDTO);
        bankPlusService.checkSerial(staffCode, serial, prodOfferCode);
    }

    @Test
    public void testCheckSerial_11() throws  Exception{
        String staffCode = "1";
        String serial = "";
        String prodOfferCode = "prodOfferCode";
        bankPlusService.checkSerial(staffCode, serial, prodOfferCode);
    }

    @Test
    public void testCheckSerial_12() throws  Exception{
        String staffCode = "1";
        String serial = "serial";
        String prodOfferCode = "";
        bankPlusService.checkSerial(staffCode, serial, prodOfferCode);
    }

    @Test
    public void testCheckSerial_13() throws  Exception{
        String staffCode = "staffCode";
        String serial = "serial";
        String prodOfferCode = "prodOfferCode";
        StaffDTO staffDTO = new StaffDTO();
        StockKitDTO stockKitDTO = new StockKitDTO();
        ProductOfferingDTO offeringDTO = new ProductOfferingDTO();
        stockKitDTO.setProdOfferId(1L);
        stockKitDTO.setOwnerType(1L);
        staffDTO.setStaffId(3L);
        stockKitDTO.setOwnerId(4L);
        offeringDTO.setProductOfferingId(1L);
        when(staffService.getStaffByStaffCode(any())).thenReturn(staffDTO);
        when(productOfferingService.getProductByCode(any())).thenReturn(offeringDTO);
        when(stockKitService.getStockKitBySerial(any())).thenReturn(stockKitDTO);
        bankPlusService.checkSerial(staffCode, serial, prodOfferCode);
    }

    @Test
    public void testCheckSerial_14() throws  Exception{
        String staffCode = "staffCode";
        String serial = "serial";
        String prodOfferCode = "prodOfferCode";
        StaffDTO staffDTO = new StaffDTO();
        StockKitDTO stockKitDTO = new StockKitDTO();
        ProductOfferingDTO offeringDTO = new ProductOfferingDTO();
        stockKitDTO.setOwnerType(2L);
        stockKitDTO.setProdOfferId(1L);
        offeringDTO.setProductOfferingId(1L);
        stockKitDTO.setOwnerId(1L);
        staffDTO.setStaffId(1L);
        stockKitDTO.setStatus(1L);
        stockKitDTO.setStateId(1L);
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setCreatedBy("datlt");
        optionSetValueDTO.setValue("1");
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(null);
        when(staffService.getStaffByStaffCode(any())).thenReturn(staffDTO);
        when(stockKitService.getStockKitBySerial(any())).thenReturn(stockKitDTO);
        when(productOfferingService.getProductByCode(any())).thenReturn(offeringDTO);
        bankPlusService.checkSerial(staffCode, serial, prodOfferCode);
    }

    @Test
    public void testCheckSerial_15() throws  Exception{
        String staffCode = "staffCode";
        String serial = "serial";
        String prodOfferCode = "prodOfferCode";
        StaffDTO staffDTO = new StaffDTO();
        StockKitDTO stockKitDTO = new StockKitDTO();
        ProductOfferingDTO offeringDTO = new ProductOfferingDTO();
        stockKitDTO.setOwnerType(2L);
        stockKitDTO.setProdOfferId(1L);
        offeringDTO.setProductOfferingId(1L);
        stockKitDTO.setOwnerId(1L);
        staffDTO.setStaffId(1L);
        stockKitDTO.setStatus(1L);
        stockKitDTO.setStateId(1L);
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setCreatedBy("datlt");
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        when(staffService.getStaffByStaffCode(any())).thenReturn(staffDTO);
        when(stockKitService.getStockKitBySerial(any())).thenReturn(stockKitDTO);
        when(productOfferingService.getProductByCode(any())).thenReturn(offeringDTO);
        bankPlusService.checkSerial(staffCode, serial, prodOfferCode);
    }

    @Test
    public void testCheckSerial_16() throws  Exception{
        String staffCode = "staffCode";
        String serial = "serial";
        String prodOfferCode = "prodOfferCode";
        StaffDTO staffDTO = new StaffDTO();
        StockKitDTO stockKitDTO = new StockKitDTO();
        ProductOfferingDTO offeringDTO = new ProductOfferingDTO();
        stockKitDTO.setOwnerType(2L);
        stockKitDTO.setProdOfferId(1L);
        offeringDTO.setProductOfferingId(1L);
        stockKitDTO.setOwnerId(1L);
        staffDTO.setStaffId(1L);
        stockKitDTO.setStatus(1L);
        stockKitDTO.setStateId(1L);
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setCreatedBy("datlt");
        optionSetValueDTO.setValue("2");
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        when(staffService.getStaffByStaffCode(any())).thenReturn(staffDTO);
        when(stockKitService.getStockKitBySerial(any())).thenReturn(stockKitDTO);
        when(productOfferingService.getProductByCode(any())).thenReturn(offeringDTO);
        bankPlusService.checkSerial(staffCode, serial, prodOfferCode);
    }
    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method BankPlusServiceImpl.validStockKitProduct
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testValidStockKitProduct_1() throws Exception {
        String serial = "";
        String stockModeCode = "";
        Long ownerId = 1L;
        Long ownerType = 1L;
        when(bankPlusServiceExecute.getStockKitIm1BySerial(any())).thenReturn(null);
        BankPlusServiceImpl spyService = PowerMockito.spy(bankPlusService);
        Whitebox.invokeMethod(spyService, "validStockKitProduct", serial, stockModeCode, ownerId, ownerType);
    }

    @Test
    public void testValidStockKitProduct_2() throws Exception {
        String serial = "";
        String stockModeCode = "";
        Long ownerId = 1L;
        Long ownerType = 1L;
        StockKitIm1DTO stockKitIm1DTO = new StockKitIm1DTO();
        when(bankPlusServiceExecute.getStockKitIm1BySerial(any())).thenReturn(stockKitIm1DTO);
        when(bankPlusServiceExecute.getStockModelIm1DTO(any())).thenReturn(null);
        BankPlusServiceImpl spyService = PowerMockito.spy(bankPlusService);
        Whitebox.invokeMethod(spyService, "validStockKitProduct", serial, stockModeCode, ownerId, ownerType);
    }

    @Test
    public void testValidStockKitProduct_3() throws Exception {
        String serial = "";
        String stockModeCode = "";
        Long ownerId = 1L;
        Long ownerType = 1L;
        StockKitIm1DTO stockKitIm1DTO = new StockKitIm1DTO();
        StockModelIm1DTO stockModelIm1DTO = new  StockModelIm1DTO();
        stockModelIm1DTO.setStockModelId(2L);
        stockKitIm1DTO.setStockModelId(1L);
        when(bankPlusServiceExecute.getStockKitIm1BySerial(any())).thenReturn(stockKitIm1DTO);
        when(bankPlusServiceExecute.getStockModelIm1DTO(any())).thenReturn(stockModelIm1DTO);
        BankPlusServiceImpl spyService = PowerMockito.spy(bankPlusService);
        Whitebox.invokeMethod(spyService, "validStockKitProduct", serial, stockModeCode, ownerId, ownerType);
    }

    @Test
    public void testValidStockKitProduct_4() throws Exception {
        String serial = "";
        String stockModeCode = "";
        Long ownerId = 1L;
        Long ownerType = 1L;
        StockModelIm1DTO stockModelIm1DTO = new  StockModelIm1DTO();
        StockKitIm1DTO stockKitIm1DTO = new StockKitIm1DTO();
        stockKitIm1DTO.setStockModelId(2L);
        stockModelIm1DTO.setStockModelId(2L);
        stockKitIm1DTO.setOwnerId(3L);
        when(bankPlusServiceExecute.getStockKitIm1BySerial(any())).thenReturn(stockKitIm1DTO);
        when(bankPlusServiceExecute.getStockModelIm1DTO(any())).thenReturn(stockModelIm1DTO);
        BankPlusServiceImpl spyService = PowerMockito.spy(bankPlusService);
        Whitebox.invokeMethod(spyService, "validStockKitProduct", serial, stockModeCode, ownerId, ownerType);
    }

    @Test
    public void testValidStockKitProduct_5() throws Exception {
        String stockModeCode = "";
        String serial = "";
        Long ownerId = 3L;
        Long ownerType = 1L;
        StockKitIm1DTO stockKitIm1DTO = new StockKitIm1DTO();
        StockModelIm1DTO stockModelIm1DTO = new  StockModelIm1DTO();
        stockKitIm1DTO.setStockModelId(2L);
        stockModelIm1DTO.setStockModelId(2L);
        stockKitIm1DTO.setOwnerId(3L);
        stockKitIm1DTO.setOwnerType(1L);
        stockKitIm1DTO.setStatus(2L);
        when(bankPlusServiceExecute.getStockKitIm1BySerial(any())).thenReturn(stockKitIm1DTO);
        when(bankPlusServiceExecute.getStockModelIm1DTO(any())).thenReturn(stockModelIm1DTO);
        BankPlusServiceImpl spyService = PowerMockito.spy(bankPlusService);
        Whitebox.invokeMethod(spyService, "validStockKitProduct", serial, stockModeCode, ownerId, ownerType);
    }

    @Test
    public void testValidStockKitProduct_6() throws Exception {
        String serial = "";
        String stockModeCode = "";
        Long ownerId = 3L;
        Long ownerType = 1L;
        StockKitIm1DTO stockKitIm1DTO = new StockKitIm1DTO();
        StockModelIm1DTO stockModelIm1DTO = new  StockModelIm1DTO();
        stockKitIm1DTO.setStockModelId(2L);
        stockModelIm1DTO.setStockModelId(2L);
        stockKitIm1DTO.setOwnerId(3L);
        stockKitIm1DTO.setOwnerType(1L);
        stockKitIm1DTO.setStatus(1L);
        stockKitIm1DTO.setStateId(2L);
        when(bankPlusServiceExecute.getStockKitIm1BySerial(any())).thenReturn(stockKitIm1DTO);
        when(bankPlusServiceExecute.getStockModelIm1DTO(any())).thenReturn(stockModelIm1DTO);
        BankPlusServiceImpl spyService = PowerMockito.spy(bankPlusService);
        Whitebox.invokeMethod(spyService, "validStockKitProduct", serial, stockModeCode, ownerId, ownerType);
    }

    @Test
    public void testValidStockKitProduct_7() throws Exception {
        String serial = "";
        String stockModeCode = "";
        Long ownerId = 3L;
        Long ownerType = 1L;
        StockKitIm1DTO stockKitIm1DTO = new StockKitIm1DTO();
        StockModelIm1DTO stockModelIm1DTO = new  StockModelIm1DTO();
        stockKitIm1DTO.setStockModelId(2L);
        stockModelIm1DTO.setStockModelId(2L);
        stockKitIm1DTO.setOwnerId(3L);
        stockKitIm1DTO.setOwnerType(1L);
        stockKitIm1DTO.setStatus(1L);
        stockKitIm1DTO.setStateId(1L);
        when(bankPlusServiceExecute.getStockKitIm1BySerial(any())).thenReturn(stockKitIm1DTO);
        when(bankPlusServiceExecute.getStockModelIm1DTO(any())).thenReturn(stockModelIm1DTO);
        BankPlusServiceImpl spyService = PowerMockito.spy(bankPlusService);
        Whitebox.invokeMethod(spyService, "validStockKitProduct", serial, stockModeCode, ownerId, ownerType);
    }

    @Test
    public void testValidStockKitProduct_8() throws Exception {
        String serial = "";
        String stockModeCode = "";
        Long ownerId = 3L;
        Long ownerType = 1L;
        StockModelIm1DTO stockModelIm1DTO = new  StockModelIm1DTO();
        StockKitIm1DTO stockKitIm1DTO = new StockKitIm1DTO();
        stockKitIm1DTO.setStockModelId(2L);
        stockModelIm1DTO.setStockModelId(2L);
        stockKitIm1DTO.setOwnerId(3L);
        stockKitIm1DTO.setOwnerType(2L);
        when(bankPlusServiceExecute.getStockKitIm1BySerial(any())).thenReturn(stockKitIm1DTO);
        when(bankPlusServiceExecute.getStockModelIm1DTO(any())).thenReturn(stockModelIm1DTO);
        BankPlusServiceImpl spyService = PowerMockito.spy(bankPlusService);
        Whitebox.invokeMethod(spyService, "validStockKitProduct", serial, stockModeCode, ownerId, ownerType);
    }
    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method BankPlusServiceImpl.debitStockTotal
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testDebitStockTotal_1() throws  Exception {
        String staffCode = "staffCode";
        String serial = "";
        String prodOfferCode = "";
        BankPlusServiceImpl spyService = PowerMockito.spy(bankPlusService);
        PowerMockito.doReturn("testDebitStockTotal_1").when(spyService, "createDebitBankPlus", staffCode, Const.OWNER_TYPE.STAFF_LONG, serial, prodOfferCode, null);
        spyService.debitStockTotal(staffCode, serial, prodOfferCode);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method BankPlusServiceImpl.debitStockTotalInShop
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testDebitStockTotalInShop_1() throws  Exception {
        String staffCode = "";
        String serial = "";
        String prodOfferCode = "";
        String shopCode = "";
        BankPlusServiceImpl spyService = PowerMockito.spy(bankPlusService);
        when(spyService.getText(any())).thenReturn("");
        spyService.debitStockTotalInShop(shopCode, serial, prodOfferCode, staffCode);
    }

    @Test
    public void testDebitStockTotalInShop_2() throws  Exception {
        String staffCode = "staffCode";
        String serial = "";
        String prodOfferCode = "";
        String shopCode = "";
        BankPlusServiceImpl spyService = PowerMockito.spy(bankPlusService);
        when(staffService.getStaffByStaffCode(any())).thenReturn(null);
        when(spyService.getText(any())).thenReturn("");
        spyService.debitStockTotalInShop(shopCode, serial, prodOfferCode, staffCode);
    }

    @Test
    public void testDebitStockTotalInShop_3() throws  Exception {
        String staffCode = "staffCode";
        String serial = "";
        String prodOfferCode = "";
        String shopCode = "";
        StaffDTO staffDTO = new StaffDTO();
        when(staffService.getStaffByStaffCode(any())).thenReturn(staffDTO);
        BankPlusServiceImpl spyService = PowerMockito.spy(bankPlusService);
        PowerMockito.when(spyService, "getText", any()).thenReturn("");
        spyService.debitStockTotalInShop(shopCode, serial, prodOfferCode, staffCode);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method BankPlusServiceImpl.createDebitBankPlus
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCreateDebitBankPlus_1() throws  Exception {
        Long ownerType = 1L;
        String ownerCode= "";
        String serial = "";
        String prodOfferCode = "";
        Long actionStaffId = 1L;
        BankPlusServiceImpl spyService = PowerMockito.spy(bankPlusService);
        Whitebox.invokeMethod(spyService, "createDebitBankPlus",  ownerCode, ownerType, serial, prodOfferCode, actionStaffId);
    }

    @Test
    public void testCreateDebitBankPlus_2() throws  Exception {
        String ownerCode= "ownerCode";
        Long ownerType = 3L;
        String serial = "serial";
        String prodOfferCode = "prodOfferCode";
        Long actionStaffId = 1L;
        BankPlusServiceImpl spyService = PowerMockito.spy(bankPlusService);
        Whitebox.invokeMethod(spyService, "createDebitBankPlus",  ownerCode, ownerType, serial, prodOfferCode, actionStaffId);
    }

    @Test
    public void testCreateDebitBankPlus_3() throws  Exception {
        String ownerCode= "ownerCode";
        Long ownerType = 2L;
        String serial = "serial1";
        String prodOfferCode = "prodOfferCode";
        Long actionStaffId = 1L;
        BankPlusServiceImpl spyService = PowerMockito.spy(bankPlusService);
        Whitebox.invokeMethod(spyService, "createDebitBankPlus",  ownerCode, ownerType, serial, prodOfferCode, actionStaffId);
    }

    @Test
    public void testCreateDebitBankPlus_4() throws  Exception {
        String ownerCode= "ownerCode";
        Long ownerType = 2L;
        String serial = "serial1";
        String prodOfferCode = "prodOfferCode";
        Long actionStaffId = 1L;
        BankPlusServiceImpl spyService = PowerMockito.spy(bankPlusService);
        when(staffService.getStaffByStaffCode(any())).thenReturn(null);
        Whitebox.invokeMethod(spyService, "createDebitBankPlus",  ownerCode, ownerType, serial, prodOfferCode, actionStaffId);
    }

    @Test
    public void testCreateDebitBankPlus_5() throws  Exception {
        String ownerCode= "ownerCode";
        Long ownerType = 1L;
        String serial = "serial1";
        String prodOfferCode = "prodOfferCode";
        Long actionStaffId = 1L;
        BankPlusServiceImpl spyService = PowerMockito.spy(bankPlusService);
        when( shopService.getShopByShopCode(any())).thenReturn(null);
        Whitebox.invokeMethod(spyService, "createDebitBankPlus",  ownerCode, ownerType, serial, prodOfferCode, actionStaffId);
    }

    @Test
    public void testCreateDebitBankPlus_6() throws  Exception {
        String ownerCode= "ownerCode";
        Long ownerType = 2L;
        String serial = "serial6";
        String prodOfferCode = "prodOfferCode";
        Long actionStaffId = 1L;
        ShopDTO shopDTO = new ShopDTO();
        StaffDTO staffDTO = new StaffDTO();
        BankPlusServiceImpl spyService = PowerMockito.spy(bankPlusService);
        when( shopService.getShopByShopCode(any())).thenReturn(shopDTO);
        when( stockKitService.getStockKitBySerial(any())).thenReturn(null);
        when( staffService.getStaffByStaffCode(anyString())).thenReturn(staffDTO);
        Whitebox.invokeMethod(spyService, "createDebitBankPlus",  ownerCode, ownerType, serial, prodOfferCode, actionStaffId);
    }

    @Test
    public void testCreateDebitBankPlus_7() throws  Exception {
        String ownerCode= "ownerCode";
        Long ownerType = 1L;
        String serial = "serial7";
        String prodOfferCode = "prodOfferCode";
        Long actionStaffId = 1L;
        ShopDTO shopDTO = new ShopDTO();
        StockKitDTO stockKitDTO = new StockKitDTO();
        BankPlusServiceImpl spyService = PowerMockito.spy(bankPlusService);
        when( shopService.getShopByShopCode(any())).thenReturn(shopDTO);
        when( stockKitService.getStockKitBySerial(any())).thenReturn(stockKitDTO);
        when( productOfferingService.getProductByCode(any())).thenReturn(null);
        Whitebox.invokeMethod(spyService, "createDebitBankPlus",  ownerCode, ownerType, serial, prodOfferCode, actionStaffId);
    }

    @Test
    public void testCreateDebitBankPlus_8() throws  Exception {
        String ownerCode= "ownerCode";
        Long ownerType = 1L;
        String serial = "serial8";
        String prodOfferCode = "prodOfferCode";
        Long actionStaffId = 1L;
        ShopDTO shopDTO = new ShopDTO();
        StockKitDTO stockKitDTO = new StockKitDTO();
        ProductOfferingDTO offeringDTO = new  ProductOfferingDTO();
        stockKitDTO.setStatus(2L);
        BankPlusServiceImpl spyService = PowerMockito.spy(bankPlusService);
        when( shopService.getShopByShopCode(any())).thenReturn(shopDTO);
        when( stockKitService.getStockKitBySerial(any())).thenReturn(stockKitDTO);
        when( productOfferingService.getProductByCode(any())).thenReturn(offeringDTO);
        Whitebox.invokeMethod(spyService, "createDebitBankPlus",  ownerCode, ownerType, serial, prodOfferCode, actionStaffId);
    }

    @Test
    public void testCreateDebitBankPlus_9() throws  Exception {
        String ownerCode= "ownerCode";
        Long ownerType = 1L;
        String serial = "serial8";
        String prodOfferCode = "prodOfferCode";
        Long actionStaffId = 1L;
        ShopDTO shopDTO = new ShopDTO();
        StockKitDTO stockKitDTO = new StockKitDTO();
        ProductOfferingDTO offeringDTO = new  ProductOfferingDTO();
        stockKitDTO.setStatus(1L);
        stockKitDTO.setStateId(2L);
        BankPlusServiceImpl spyService = PowerMockito.spy(bankPlusService);
        when( shopService.getShopByShopCode(any())).thenReturn(shopDTO);
        when( stockKitService.getStockKitBySerial(any())).thenReturn(stockKitDTO);
        when( productOfferingService.getProductByCode(any())).thenReturn(offeringDTO);
        Whitebox.invokeMethod(spyService, "createDebitBankPlus",  ownerCode, ownerType, serial, prodOfferCode, actionStaffId);
    }

    @Test
    public void testCreateDebitBankPlus_10() throws  Exception {
        String ownerCode= "ownerCode";
        Long ownerType = 1L;
        String serial = "serial8";
        String prodOfferCode = "prodOfferCode";
        Long actionStaffId = 1L;
        ShopDTO shopDTO = new ShopDTO();
        StockKitDTO stockKitDTO = new StockKitDTO();
        ProductOfferingDTO offeringDTO = new  ProductOfferingDTO();
        stockKitDTO.setStatus(1L);
        stockKitDTO.setStateId(1L);
        stockKitDTO.setProdOfferId(1L);
        offeringDTO.setProductOfferingId(2L);
        BankPlusServiceImpl spyService = PowerMockito.spy(bankPlusService);
        when( shopService.getShopByShopCode(any())).thenReturn(shopDTO);
        when( stockKitService.getStockKitBySerial(any())).thenReturn(stockKitDTO);
        when( productOfferingService.getProductByCode(any())).thenReturn(offeringDTO);
        Whitebox.invokeMethod(spyService, "createDebitBankPlus",  ownerCode, ownerType, serial, prodOfferCode, actionStaffId);
    }

    @Test
    public void testCreateDebitBankPlus_11() throws  Exception {
        String ownerCode= "ownerCode";
        Long ownerType = 2L;
        String serial = "serial8";
        String prodOfferCode = "prodOfferCode";
        Long actionStaffId = 1L;
        ShopDTO shopDTO = new ShopDTO();
        StockKitDTO stockKitDTO = new StockKitDTO();
        ProductOfferingDTO offeringDTO = new  ProductOfferingDTO();
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStaffId(3L);
        stockKitDTO.setStatus(1L);
        stockKitDTO.setStateId(1L);
        stockKitDTO.setProdOfferId(2L);
        offeringDTO.setProductOfferingId(2L);
        stockKitDTO.setOwnerType(20L);
        stockKitDTO.setOwnerId(3L);
        BankPlusServiceImpl spyService = PowerMockito.spy(bankPlusService);
        when( shopService.getShopByShopCode(any())).thenReturn(shopDTO);
        when( stockKitService.getStockKitBySerial(any())).thenReturn(stockKitDTO);
        when( productOfferingService.getProductByCode(any())).thenReturn(offeringDTO);
        when(staffService.getStaffByStaffCode(any())).thenReturn(staffDTO);
        Whitebox.invokeMethod(spyService, "createDebitBankPlus",  ownerCode, ownerType, serial, prodOfferCode, actionStaffId);
    }

    @Test
    public void testCreateDebitBankPlus_12() throws  Exception {
        String ownerCode= "ownerCode";
        Long ownerType = 2L;
        String serial = "serial8";
        String prodOfferCode = "prodOfferCode";
        Long actionStaffId = 1L;
        ShopDTO shopDTO = new ShopDTO();
        StockKitDTO stockKitDTO = new StockKitDTO();
        ProductOfferingDTO offeringDTO = new  ProductOfferingDTO();
        StaffDTO staffDTO = new StaffDTO();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue("1");
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        staffDTO.setStaffId(3L);
        stockKitDTO.setStatus(1L);
        stockKitDTO.setStateId(1L);
        stockKitDTO.setProdOfferId(2L);
        offeringDTO.setProductOfferingId(2L);
        stockKitDTO.setOwnerType(2L);
        stockKitDTO.setOwnerId(3L);
        BankPlusServiceImpl spyService = PowerMockito.spy(bankPlusService);
        PowerMockito.when( shopService.getShopByShopCode(any())).thenReturn(shopDTO);
        PowerMockito.when( stockKitService.getStockKitBySerial(any())).thenReturn(stockKitDTO);
        PowerMockito.when( productOfferingService.getProductByCode(any())).thenReturn(offeringDTO);
        PowerMockito.when(staffService.getStaffByStaffCode(any())).thenReturn(staffDTO);
        PowerMockito.when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        PowerMockito.when(spyService, "validStockKitProduct", serial, prodOfferCode, 0L, ownerType).thenReturn("Cancel");
        Whitebox.invokeMethod(spyService, "createDebitBankPlus",  ownerCode, ownerType, serial, prodOfferCode, actionStaffId);
    }

    @Test
    public void testCreateDebitBankPlus_13() throws  Exception {
        String ownerCode= "ownerCode";
        Long ownerType = 2L;
        String serial = "serial8";
        String prodOfferCode = "prodOfferCode";
        Long actionStaffId = 1L;
        ShopDTO shopDTO = new ShopDTO();
        StockKitDTO stockKitDTO = new StockKitDTO();
        ProductOfferingDTO offeringDTO = new  ProductOfferingDTO();
        StaffDTO staffDTO = new StaffDTO();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue("1");
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        staffDTO.setStaffId(3L);
        stockKitDTO.setStatus(1L);
        stockKitDTO.setStateId(1L);
        stockKitDTO.setProdOfferId(2L);
        offeringDTO.setProductOfferingId(2L);
        stockKitDTO.setOwnerType(2L);
        stockKitDTO.setOwnerId(3L);
        BankPlusServiceImpl spyService = PowerMockito.spy(bankPlusService);
        PowerMockito.when( shopService.getShopByShopCode(any())).thenThrow(new Exception());
        PowerMockito.when( stockKitService.getStockKitBySerial(any())).thenReturn(stockKitDTO);
        PowerMockito.when( productOfferingService.getProductByCode(any())).thenReturn(offeringDTO);
        PowerMockito.when(staffService.getStaffByStaffCode(any())).thenThrow(new Exception());
        PowerMockito.when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        PowerMockito.when(spyService, "validStockKitProduct", serial, prodOfferCode, 0L, ownerType).thenReturn("Cancel");
        Whitebox.invokeMethod(spyService, "createDebitBankPlus",  ownerCode, ownerType, serial, prodOfferCode, actionStaffId);
    }

    @Test
    public void testCreateDebitBankPlus_14() throws  Exception {
        String ownerCode= "ownerCode";
        Long ownerType = 2L;
        String serial = "serial8";
        String prodOfferCode = "prodOfferCode";
        Long actionStaffId = 1L;
        ShopDTO shopDTO = new ShopDTO();
        StockKitDTO stockKitDTO = new StockKitDTO();
        ProductOfferingDTO offeringDTO = new  ProductOfferingDTO();
        StaffDTO staffDTO = new StaffDTO();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue("1");
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        staffDTO.setStaffId(3L);
        stockKitDTO.setStatus(1L);
        stockKitDTO.setStateId(1L);
        stockKitDTO.setProdOfferId(2L);
        offeringDTO.setProductOfferingId(2L);
        stockKitDTO.setOwnerType(2L);
        stockKitDTO.setOwnerId(3L);
        BankPlusServiceImpl spyService = PowerMockito.spy(bankPlusService);
        PowerMockito.when( shopService.getShopByShopCode(any())).thenReturn(shopDTO);
        PowerMockito.when( stockKitService.getStockKitBySerial(any())).thenReturn(stockKitDTO);
        PowerMockito.when( productOfferingService.getProductByCode(any())).thenReturn(offeringDTO);
        PowerMockito.when(staffService.getStaffByStaffCode(any())).thenReturn(staffDTO);
        PowerMockito.when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        PowerMockito.when(spyService, "validStockKitProduct", serial, prodOfferCode, 0L, ownerType).thenReturn("OK");
        Whitebox.invokeMethod(spyService, "createDebitBankPlus",  ownerCode, ownerType, serial, prodOfferCode, actionStaffId);
    }

    @Test
    public void testCreateDebitBankPlus_15() throws  Exception {
        String ownerCode= "ownerCode";
        Long ownerType = 2L;
        String serial = "serial8";
        String prodOfferCode = "prodOfferCode";
        Long actionStaffId = 1L;
        ShopDTO shopDTO = new ShopDTO();
        StockKitDTO stockKitDTO = new StockKitDTO();
        ProductOfferingDTO offeringDTO = new  ProductOfferingDTO();
        StaffDTO staffDTO = new StaffDTO();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue("1");
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        staffDTO.setStaffId(3L);
        stockKitDTO.setStatus(1L);
        stockKitDTO.setStateId(1L);
        stockKitDTO.setProdOfferId(2L);
        offeringDTO.setProductOfferingId(2L);
        stockKitDTO.setOwnerType(2L);
        stockKitDTO.setOwnerId(3L);
        BankPlusServiceImpl spyService = PowerMockito.spy(bankPlusService);
        PowerMockito.when( shopService.getShopByShopCode(any())).thenThrow(new Exception());
        PowerMockito.when( stockKitService.getStockKitBySerial(any())).thenReturn(stockKitDTO);
        PowerMockito.when( productOfferingService.getProductByCode(any())).thenReturn(offeringDTO);
        PowerMockito.when(staffService.getStaffByStaffCode(any())).thenThrow(new LogicException());
        PowerMockito.when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        PowerMockito.when(spyService, "validStockKitProduct", serial, prodOfferCode, 0L, ownerType).thenReturn("Cancel");
        Whitebox.invokeMethod(spyService, "createDebitBankPlus",  ownerCode, ownerType, serial, prodOfferCode, actionStaffId);
    }

    @Test
    public void testCreateDebitBankPlus_16() throws  Exception {
        Long ownerType = 1L;
        String ownerCode= "";
        String serial = "1";
        String prodOfferCode = "1";
        Long actionStaffId = 1L;
        BankPlusServiceImpl spyService = PowerMockito.spy(bankPlusService);
        Whitebox.invokeMethod(spyService, "createDebitBankPlus",  ownerCode, ownerType, serial, prodOfferCode, actionStaffId);
    }

    @Test
    public void testCreateDebitBankPlus_17() throws  Exception {
        Long ownerType = 1L;
        String ownerCode= "1";
        String serial = "";
        String prodOfferCode = "1";
        Long actionStaffId = 1L;
        BankPlusServiceImpl spyService = PowerMockito.spy(bankPlusService);
        Whitebox.invokeMethod(spyService, "createDebitBankPlus",  ownerCode, ownerType, serial, prodOfferCode, actionStaffId);
    }

    @Test
    public void testCreateDebitBankPlus_18() throws  Exception {
        Long ownerType = 1L;
        String ownerCode= "1";
        String serial = "1";
        String prodOfferCode = "";
        Long actionStaffId = 1L;
        BankPlusServiceImpl spyService = PowerMockito.spy(bankPlusService);
        Whitebox.invokeMethod(spyService, "createDebitBankPlus",  ownerCode, ownerType, serial, prodOfferCode, actionStaffId);
    }

    @Test
    public void testCreateDebitBankPlus_19() throws  Exception {
        String ownerCode= "ownerCode";
        Long ownerType = 20L;
        String serial = "serial8";
        String prodOfferCode = "prodOfferCode";
        Long actionStaffId = 1L;
        ShopDTO shopDTO = new ShopDTO();
        StockKitDTO stockKitDTO = new StockKitDTO();
        ProductOfferingDTO offeringDTO = new  ProductOfferingDTO();
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStaffId(3L);
        stockKitDTO.setStatus(1L);
        stockKitDTO.setStateId(1L);
        stockKitDTO.setProdOfferId(2L);
        offeringDTO.setProductOfferingId(2L);
        stockKitDTO.setOwnerType(20L);
        stockKitDTO.setOwnerId(3L);
        shopDTO.setShopId(1L);
        BankPlusServiceImpl spyService = PowerMockito.spy(bankPlusService);
        when( shopService.getShopByShopCode(any())).thenReturn(shopDTO);
        when( stockKitService.getStockKitBySerial(any())).thenReturn(stockKitDTO);
        when( productOfferingService.getProductByCode(any())).thenReturn(offeringDTO);
        when(staffService.getStaffByStaffCode(any())).thenReturn(staffDTO);
        Whitebox.invokeMethod(spyService, "createDebitBankPlus",  ownerCode, ownerType, serial, prodOfferCode, actionStaffId);
    }

    @Test
    public void testCreateDebitBankPlus_20() throws  Exception {
        String ownerCode= "ownerCode";
        Long ownerType = 1L;
        String serial = "serial8";
        String prodOfferCode = "prodOfferCode";
        Long actionStaffId = 1L;
        ShopDTO shopDTO = new ShopDTO();
        StockKitDTO stockKitDTO = new StockKitDTO();
        ProductOfferingDTO offeringDTO = new  ProductOfferingDTO();
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStaffId(3L);
        stockKitDTO.setStatus(1L);
        stockKitDTO.setStateId(1L);
        stockKitDTO.setProdOfferId(2L);
        offeringDTO.setProductOfferingId(2L);
        stockKitDTO.setOwnerType(2L);
        stockKitDTO.setOwnerId(2L);
        shopDTO.setShopId(1L);
        BankPlusServiceImpl spyService = PowerMockito.spy(bankPlusService);
        when( shopService.getShopByShopCode(any())).thenReturn(shopDTO);
        when( stockKitService.getStockKitBySerial(any())).thenReturn(stockKitDTO);
        when( productOfferingService.getProductByCode(any())).thenReturn(offeringDTO);
        when(staffService.getStaffByStaffCode(any())).thenReturn(staffDTO);
        Whitebox.invokeMethod(spyService, "createDebitBankPlus",  ownerCode, ownerType, serial, prodOfferCode, actionStaffId);
    }

    @Test
    public void testCreateDebitBankPlus_21() throws  Exception {
        String ownerCode= "ownerCode";
        Long ownerType = 2L;
        String serial = "serial8";
        String prodOfferCode = "prodOfferCode";
        Long actionStaffId = 1L;
        ShopDTO shopDTO = new ShopDTO();
        StockKitDTO stockKitDTO = new StockKitDTO();
        ProductOfferingDTO offeringDTO = new  ProductOfferingDTO();
        StaffDTO staffDTO = new StaffDTO();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue("2");
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        staffDTO.setStaffId(3L);
        stockKitDTO.setStatus(1L);
        stockKitDTO.setStateId(1L);
        stockKitDTO.setProdOfferId(2L);
        offeringDTO.setProductOfferingId(2L);
        stockKitDTO.setOwnerType(2L);
        stockKitDTO.setOwnerId(3L);
        BankPlusServiceImpl spyService = PowerMockito.spy(bankPlusService);
        PowerMockito.when( shopService.getShopByShopCode(any())).thenReturn(shopDTO);
        PowerMockito.when( stockKitService.getStockKitBySerial(any())).thenReturn(stockKitDTO);
        PowerMockito.when( productOfferingService.getProductByCode(any())).thenReturn(offeringDTO);
        PowerMockito.when(staffService.getStaffByStaffCode(any())).thenReturn(staffDTO);
        PowerMockito.when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        PowerMockito.when(spyService, "validStockKitProduct", serial, prodOfferCode, 0L, ownerType).thenReturn("Cancel");
        Whitebox.invokeMethod(spyService, "createDebitBankPlus",  ownerCode, ownerType, serial, prodOfferCode, actionStaffId);
    }

    @Test
    public void testCreateDebitBankPlus_22() throws  Exception {
        String ownerCode= "ownerCode";
        Long ownerType = 2L;
        String serial = "serial8";
        String prodOfferCode = "prodOfferCode";
        Long actionStaffId = 1L;
        ShopDTO shopDTO = new ShopDTO();
        StockKitDTO stockKitDTO = new StockKitDTO();
        ProductOfferingDTO offeringDTO = new  ProductOfferingDTO();
        StaffDTO staffDTO = new StaffDTO();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue("2");
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList();
        staffDTO.setStaffId(3L);
        stockKitDTO.setStatus(1L);
        stockKitDTO.setStateId(1L);
        stockKitDTO.setProdOfferId(2L);
        offeringDTO.setProductOfferingId(2L);
        stockKitDTO.setOwnerType(2L);
        stockKitDTO.setOwnerId(3L);
        BankPlusServiceImpl spyService = PowerMockito.spy(bankPlusService);
        PowerMockito.when( shopService.getShopByShopCode(any())).thenReturn(shopDTO);
        PowerMockito.when( stockKitService.getStockKitBySerial(any())).thenReturn(stockKitDTO);
        PowerMockito.when( productOfferingService.getProductByCode(any())).thenReturn(offeringDTO);
        PowerMockito.when(staffService.getStaffByStaffCode(any())).thenReturn(staffDTO);
        PowerMockito.when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        PowerMockito.when(spyService, "validStockKitProduct", serial, prodOfferCode, 0L, ownerType).thenReturn("Cancel");
        Whitebox.invokeMethod(spyService, "createDebitBankPlus",  ownerCode, ownerType, serial, prodOfferCode, actionStaffId);
    }

    @Test
    public void testCreateDebitBankPlus_23() throws  Exception {
        String ownerCode= "ownerCode";
        Long ownerType = 2L;
        String serial = "serial8";
        String prodOfferCode = "prodOfferCode";
        Long actionStaffId = 1L;
        ShopDTO shopDTO = new ShopDTO();
        StockKitDTO stockKitDTO = new StockKitDTO();
        ProductOfferingDTO offeringDTO = new  ProductOfferingDTO();
        StaffDTO staffDTO = new StaffDTO();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
//        optionSetValueDTO.setValue("2");
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        staffDTO.setStaffId(3L);
        stockKitDTO.setStatus(1L);
        stockKitDTO.setStateId(1L);
        stockKitDTO.setProdOfferId(2L);
        offeringDTO.setProductOfferingId(2L);
        stockKitDTO.setOwnerType(2L);
        stockKitDTO.setOwnerId(3L);
        BankPlusServiceImpl spyService = PowerMockito.spy(bankPlusService);
        PowerMockito.when( shopService.getShopByShopCode(any())).thenReturn(shopDTO);
        PowerMockito.when( stockKitService.getStockKitBySerial(any())).thenReturn(stockKitDTO);
        PowerMockito.when( productOfferingService.getProductByCode(any())).thenReturn(offeringDTO);
        PowerMockito.when(staffService.getStaffByStaffCode(any())).thenReturn(staffDTO);
        PowerMockito.when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        PowerMockito.when(spyService, "validStockKitProduct", serial, prodOfferCode, 0L, ownerType).thenReturn("Cancel");
        Whitebox.invokeMethod(spyService, "createDebitBankPlus",  ownerCode, ownerType, serial, prodOfferCode, actionStaffId);
    }
    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method BankPlusServiceImpl.checkSerialInShop
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCheckSerialInShop_1() throws  Exception {
        String shopCode ="";
        String serial ="";
        String prodOfferCode ="";
        bankPlusService.checkSerialInShop(shopCode, serial, prodOfferCode);
    }

    @Test
    public void testCheckSerialInShop_2() throws  Exception {
        String shopCode ="shopCode";
        String serial ="serial";
        String prodOfferCode ="prodOfferCode";
        when(shopService.getShopByShopCode(shopCode)).thenReturn(null);
        bankPlusService.checkSerialInShop(shopCode, serial, prodOfferCode);
    }

    @Test
    public void testCheckSerialInShop_3() throws  Exception {
        String shopCode ="shopCode3";
        String serial ="serial3";
        String prodOfferCode ="prodOfferCode3";
        ShopDTO shopDTO = new ShopDTO();
        when(shopService.getShopByShopCode(shopCode)).thenReturn(shopDTO);
        when(stockKitService.getStockKitBySerial(serial)).thenReturn(null);
        bankPlusService.checkSerialInShop(shopCode, serial, prodOfferCode);
    }

    @Test
    public void testCheckSerialInShop_4() throws  Exception {
        String shopCode ="shopCode4";
        String serial ="serial3";
        String prodOfferCode ="prodOfferCode3";
        ShopDTO shopDTO = new ShopDTO();
        StockKitDTO stockKitDTO = new StockKitDTO();
        when(shopService.getShopByShopCode(shopCode)).thenReturn(shopDTO);
        when(stockKitService.getStockKitBySerial(serial)).thenReturn(stockKitDTO);
        when(productOfferingService.getProductByCode(prodOfferCode)).thenReturn(null);
        bankPlusService.checkSerialInShop(shopCode, serial, prodOfferCode);
    }

    @Test
    public void testCheckSerialInShop_5() throws  Exception {
        String shopCode ="shopCode5";
        String serial ="serial3";
        String prodOfferCode ="prodOfferCode5";
        ShopDTO shopDTO = new ShopDTO();
        StockKitDTO stockKitDTO = new StockKitDTO();
        ProductOfferingDTO offeringDTO = new ProductOfferingDTO();
        stockKitDTO.setProdOfferId(1L);
        offeringDTO.setProductOfferingId(2L);
        when(shopService.getShopByShopCode(shopCode)).thenReturn(shopDTO);
        when(stockKitService.getStockKitBySerial(serial)).thenReturn(stockKitDTO);
        when(productOfferingService.getProductByCode(prodOfferCode)).thenReturn(offeringDTO);
        bankPlusService.checkSerialInShop(shopCode, serial, prodOfferCode);
    }

    @Test
    public void testCheckSerialInShop_6() throws  Exception {
        String shopCode ="shopCode5";
        String serial ="serial3";
        String prodOfferCode ="prodOfferCode5";
        ShopDTO shopDTO = new ShopDTO();
        StockKitDTO stockKitDTO = new StockKitDTO();
        ProductOfferingDTO offeringDTO = new ProductOfferingDTO();
        stockKitDTO.setProdOfferId(1L);
        offeringDTO.setProductOfferingId(1L);
        stockKitDTO.setOwnerId(2L);
        shopDTO.setShopId(3L);
        when(shopService.getShopByShopCode(shopCode)).thenReturn(shopDTO);
        when(productOfferingService.getProductByCode(prodOfferCode)).thenReturn(offeringDTO);
        when(stockKitService.getStockKitBySerial(serial)).thenReturn(stockKitDTO);
        bankPlusService.checkSerialInShop(shopCode, serial, prodOfferCode);
    }

    @Test
    public void testCheckSerialInShop_7() throws  Exception {
        String shopCode ="shopCode5";
        String serial ="serial3";
        String prodOfferCode ="prodOfferCode5";
        ShopDTO shopDTO = new ShopDTO();
        StockKitDTO stockKitDTO = new StockKitDTO();
        ProductOfferingDTO offeringDTO = new ProductOfferingDTO();
        stockKitDTO.setProdOfferId(1L);
        offeringDTO.setProductOfferingId(1L);
        stockKitDTO.setOwnerId(2L);
        shopDTO.setShopId(2L);
        stockKitDTO.setOwnerType(1L);
        stockKitDTO.setStatus(2L);
        when(shopService.getShopByShopCode(shopCode)).thenReturn(shopDTO);
        when(stockKitService.getStockKitBySerial(serial)).thenReturn(stockKitDTO);
        when(productOfferingService.getProductByCode(prodOfferCode)).thenReturn(offeringDTO);
        bankPlusService.checkSerialInShop(shopCode, serial, prodOfferCode);
    }

    @Test
    public void testCheckSerialInShop_8() throws  Exception {
        String shopCode ="shopCode5";
        String serial ="serial3";
        String prodOfferCode ="prodOfferCode8";
        ShopDTO shopDTO = new ShopDTO();
        StockKitDTO stockKitDTO = new StockKitDTO();
        ProductOfferingDTO offeringDTO = new ProductOfferingDTO();
        stockKitDTO.setProdOfferId(1L);
        offeringDTO.setProductOfferingId(1L);
        stockKitDTO.setOwnerId(2L);
        shopDTO.setShopId(2L);
        stockKitDTO.setOwnerType(1L);
        stockKitDTO.setStatus(1L);
        stockKitDTO.setStateId(2L);
        when(shopService.getShopByShopCode(shopCode)).thenReturn(shopDTO);
        when(stockKitService.getStockKitBySerial(serial)).thenReturn(stockKitDTO);
        when(productOfferingService.getProductByCode(prodOfferCode)).thenReturn(offeringDTO);
        bankPlusService.checkSerialInShop(shopCode, serial, prodOfferCode);
    }

    @Test
    public void testCheckSerialInShop_9() throws  Exception {
        String shopCode ="shopCode5";
        String serial ="serial3";
        String prodOfferCode ="prodOfferCode9";
        ShopDTO shopDTO = new ShopDTO();
        StockKitDTO stockKitDTO = new StockKitDTO();
        ProductOfferingDTO offeringDTO = new ProductOfferingDTO();
        stockKitDTO.setProdOfferId(1L);
        offeringDTO.setProductOfferingId(1L);
        stockKitDTO.setOwnerId(2L);
        shopDTO.setShopId(2L);
        stockKitDTO.setOwnerType(1L);
        stockKitDTO.setStatus(1L);
        stockKitDTO.setStateId(1L);
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue("1");
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        when(shopService.getShopByShopCode(shopCode)).thenReturn(shopDTO);
        when(stockKitService.getStockKitBySerial(serial)).thenReturn(stockKitDTO);
        when(productOfferingService.getProductByCode(prodOfferCode)).thenReturn(offeringDTO);
        when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        bankPlusService.checkSerialInShop(shopCode, serial, prodOfferCode);
    }

    @Test
    public void testCheckSerialInShop_10() throws  Exception {
        String shopCode ="shopCode5";
        String serial ="serial3";
        String prodOfferCode ="prodOfferCode9";
        ShopDTO shopDTO = new ShopDTO();
        StockKitDTO stockKitDTO = new StockKitDTO();
        ProductOfferingDTO offeringDTO = new ProductOfferingDTO();
        stockKitDTO.setProdOfferId(1L);
        offeringDTO.setProductOfferingId(1L);
        stockKitDTO.setOwnerId(2L);
        shopDTO.setShopId(2L);
        stockKitDTO.setOwnerType(1L);
        stockKitDTO.setStatus(1L);
        stockKitDTO.setStateId(1L);
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue("2");
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        when(shopService.getShopByShopCode(shopCode)).thenReturn(shopDTO);
        when(stockKitService.getStockKitBySerial(serial)).thenReturn(stockKitDTO);
        when(productOfferingService.getProductByCode(prodOfferCode)).thenReturn(offeringDTO);
        when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        bankPlusService.checkSerialInShop(shopCode, serial, prodOfferCode);
    }

    @Test
    public void testCheckSerialInShop_11() throws  Exception {
        String shopCode ="";
        String serial ="1";
        String prodOfferCode ="1";
        bankPlusService.checkSerialInShop(shopCode, serial, prodOfferCode);
    }

    @Test
    public void testCheckSerialInShop_12() throws  Exception {
        String shopCode ="1";
        String serial ="";
        String prodOfferCode ="1";
        bankPlusService.checkSerialInShop(shopCode, serial, prodOfferCode);
    }

    @Test
    public void testCheckSerialInShop_13() throws  Exception {
        String shopCode ="1";
        String serial ="1";
        String prodOfferCode ="";
        bankPlusService.checkSerialInShop(shopCode, serial, prodOfferCode);
    }

    @Test
    public void testCheckSerialInShop_14() throws  Exception {
        String shopCode ="shopCode5";
        String serial ="serial3";
        String prodOfferCode ="prodOfferCode5";
        ShopDTO shopDTO = new ShopDTO();
        StockKitDTO stockKitDTO = new StockKitDTO();
        ProductOfferingDTO offeringDTO = new ProductOfferingDTO();
        stockKitDTO.setProdOfferId(1L);
        offeringDTO.setProductOfferingId(1L);
        stockKitDTO.setOwnerType(2L);
        stockKitDTO.setOwnerId(2L);
        shopDTO.setShopId(2L);
        when(shopService.getShopByShopCode(shopCode)).thenReturn(shopDTO);
        when(productOfferingService.getProductByCode(prodOfferCode)).thenReturn(offeringDTO);
        when(stockKitService.getStockKitBySerial(serial)).thenReturn(stockKitDTO);
        bankPlusService.checkSerialInShop(shopCode, serial, prodOfferCode);
    }

    @Test
    public void testCheckSerialInShop_15() throws  Exception {
        String shopCode = "shopCode5";
        String serial = "serial3";
        String prodOfferCode = "prodOfferCode9";
        ShopDTO shopDTO = new ShopDTO();
        StockKitDTO stockKitDTO = new StockKitDTO();
        ProductOfferingDTO offeringDTO = new ProductOfferingDTO();
        stockKitDTO.setProdOfferId(1L);
        offeringDTO.setProductOfferingId(1L);
        stockKitDTO.setOwnerId(2L);
        shopDTO.setShopId(2L);
        stockKitDTO.setOwnerType(1L);
        stockKitDTO.setStatus(1L);
        stockKitDTO.setStateId(1L);
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue("2");
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList();
        when(shopService.getShopByShopCode(shopCode)).thenReturn(shopDTO);
        when(stockKitService.getStockKitBySerial(serial)).thenReturn(stockKitDTO);
        when(productOfferingService.getProductByCode(prodOfferCode)).thenReturn(offeringDTO);
        when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        bankPlusService.checkSerialInShop(shopCode, serial, prodOfferCode);
    }

    @Test
    public void testCheckSerialInShop_16() throws  Exception {
        String shopCode = "shopCode5";
        String serial = "serial3";
        String prodOfferCode = "prodOfferCode9";
        ShopDTO shopDTO = new ShopDTO();
        StockKitDTO stockKitDTO = new StockKitDTO();
        ProductOfferingDTO offeringDTO = new ProductOfferingDTO();
        stockKitDTO.setProdOfferId(1L);
        offeringDTO.setProductOfferingId(1L);
        stockKitDTO.setOwnerId(2L);
        shopDTO.setShopId(2L);
        stockKitDTO.setOwnerType(1L);
        stockKitDTO.setStatus(1L);
        stockKitDTO.setStateId(1L);
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        when(shopService.getShopByShopCode(shopCode)).thenReturn(shopDTO);
        when(stockKitService.getStockKitBySerial(serial)).thenReturn(stockKitDTO);
        when(productOfferingService.getProductByCode(prodOfferCode)).thenReturn(offeringDTO);
        when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        bankPlusService.checkSerialInShop(shopCode, serial, prodOfferCode);
    }
}