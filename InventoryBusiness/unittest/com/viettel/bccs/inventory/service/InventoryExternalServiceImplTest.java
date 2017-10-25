package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.Staff;
import com.viettel.bccs.inventory.repo.InvoiceListRepo;
import com.viettel.bccs.inventory.repo.InvoiceTemplateRepo;
import com.viettel.bccs.inventory.repo.StockHandsetRepo;
import com.viettel.bccs.inventory.repo.StockTransRepo;
import com.viettel.bccs.sale.dto.SaleTransBankplusDTO;
import com.viettel.bccs.sale.dto.SaleTransDTO;
import com.viettel.bccs.sale.repo.SaleTransRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.dto.BaseMessage;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import static org.mockito.Matchers.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InventoryExternalServiceImplTest {


    @InjectMocks
    private InventoryExternalServiceImpl inventoryExternalService;

    @Mock
    private StockSimService stockSimService;
    @Mock
    private StockHandsetService stockHandsetService;
    @Mock
    private StockCardService stockCardService;
    @Mock
    private ProductOfferingService productOfferingService;
    @Mock
    private StockTransActionService stockTransActionService;
    @Mock
    private InvoiceTemplateService invoiceTemplateService;
    @Mock
    private StockTransService stockTransService;
    @Mock
    private StaffService staffService;
    @Mock
    private OptionSetValueService optionSetValueService;
    @Mock
    private StockHandsetRepo stockHandsetRepo;
    @Mock
    private ShopService shopService;
    @Mock
    private StockTotalService stockTotalService;
    @Mock
    private StockDeliverService stockDeliverService;
    @Mock
    private StockTransRepo stockTransRepo;
    @Mock
    private InvoiceListRepo invoiceListRepo;
    @Mock
    private InvoiceTemplateRepo invoiceTemplateRepo;
    @Mock
    private SaleWs saleWs;
    @Mock
    private SaleTransRepo saleTransRepo;

//    @Before
//    public void setUp() throws Exception {
//    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method InventoryExternalServiceImpl.getSimInfor
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testGetSimInfor_01() throws Exception {
        String msisdn = "";
        Assert.assertNull(inventoryExternalService.getSimInfor(msisdn).getErrorCode());
    }

    @Test
    public void testGetSimInfor_02() throws Exception {
        String msisdn = "";
        when(stockSimService.getSimInfor(msisdn)).thenThrow(new LogicException());
        Assert.assertNull(inventoryExternalService.getSimInfor(msisdn).getErrorCode());
    }

    @Test
    public void testGetSimInfor_03() throws Exception {
        String msisdn = "";
        when(stockSimService.getSimInfor(msisdn)).thenThrow(new Exception());
        Assert.assertNull(inventoryExternalService.getSimInfor(msisdn).getErrorCode());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method InventoryExternalServiceImpl.isCaSim
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testIsCaSim_01() throws Exception {
        String serial = "";
        when(stockSimService.isCaSim(serial)).thenReturn(true);
        Assert.assertTrue(inventoryExternalService.isCaSim(serial));
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method InventoryExternalServiceImpl.getStockModelSoPin
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testGetStockModelSoPin_01() throws Exception {
        String serial = "";
        Assert.assertNull(inventoryExternalService.getStockModelSoPin(serial));
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method InventoryExternalServiceImpl.getQuantityInEcomStock
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testGetQuantityInEcomStock_01() throws Exception {
        String prodOfferCode = "";
        Assert.assertEquals("-1", inventoryExternalService.getQuantityInEcomStock(prodOfferCode).getResponseCode());
    }

    @Test
    public void testGetQuantityInEcomStock_02() throws Exception {
        String prodOfferCode = "1";
        ProductOfferingDTO productOffering = null;
        when(productOfferingService.findByProductOfferCode(prodOfferCode.trim(), Const.STATUS_ACTIVE)).thenReturn(productOffering);
        Assert.assertEquals("-1", inventoryExternalService.getQuantityInEcomStock(prodOfferCode).getResponseCode());
    }

    @Test
    public void testGetQuantityInEcomStock_03() throws Exception {
        String prodOfferCode = "1";
        ProductOfferingDTO productOffering = new ProductOfferingDTO();
        when(productOfferingService.findByProductOfferCode(prodOfferCode.trim(), Const.STATUS_ACTIVE)).thenReturn(productOffering);
        when(stockCardService.getQuantityInEcomStock(prodOfferCode.trim())).thenReturn(new StockTotalResultDTO());
        Assert.assertNotNull(inventoryExternalService.getQuantityInEcomStock(prodOfferCode));
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method InventoryExternalServiceImpl.verifyUnlockG6
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testVerifyUnlockG6_01() throws Exception {
        String imei = "";
        BaseMessage baseMessage = inventoryExternalService.verifyUnlockG6(imei);

        Assert.assertFalse(baseMessage.isSuccess());
        Assert.assertEquals(Const.UNLOCK_G6.FAILED, baseMessage.getErrorCode());
    }

    @Test
    public void testVerifyUnlockG6_02() throws Exception {
        String imei = "1";

        boolean checkExist = false;
        when(stockHandsetService.checkExistSerial(imei.trim())).thenReturn(checkExist);
        BaseMessage baseMessage = inventoryExternalService.verifyUnlockG6(imei);
        Assert.assertFalse(baseMessage.isSuccess());
        Assert.assertEquals(Const.UNLOCK_G6.FAILED, baseMessage.getErrorCode());
    }

    @Test
    public void testVerifyUnlockG6_03() throws Exception {
        String imei = "1";

        boolean checkExist = true;
        when(stockHandsetService.checkExistSerial(anyString())).thenReturn(checkExist);
        boolean checkUnlock = false;
        when(stockHandsetService.checkUnlockSerial(anyString())).thenReturn(checkUnlock);
        BaseMessage baseMessage = inventoryExternalService.verifyUnlockG6(imei);
        assertFalse(baseMessage.isSuccess());
        Assert.assertEquals(Const.UNLOCK_G6.FAILED, baseMessage.getErrorCode());
    }

    @Test
    public void testVerifyUnlockG6_04() throws Exception {
        String imei = "1";

        boolean checkExist = true;
        when(stockHandsetService.checkExistSerial(anyString())).thenReturn(checkExist);
        boolean checkUnlock = true;
        when(stockHandsetService.checkUnlockSerial(anyString())).thenReturn(checkUnlock);
        BaseMessage baseMessage = inventoryExternalService.verifyUnlockG6(imei);
        Assert.assertTrue(baseMessage.isSuccess());
        Assert.assertEquals(Const.UNLOCK_G6.SUCCESS, baseMessage.getErrorCode());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method InventoryExternalServiceImpl.unlockG6
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testUnlockG6_01() throws Exception {
        String imei = "1";
        BaseMessage baseMessage = new BaseMessage(true);
        when(stockCardService.unlockG6(imei)).thenReturn(baseMessage);
        Assert.assertTrue(inventoryExternalService.unlockG6(imei).isSuccess());
    }

    @Test
    public void testUnlockG6_02() throws Exception {
        String imei = "1";
        when(stockCardService.unlockG6(imei)).thenThrow(new Exception());
        Assert.assertFalse(inventoryExternalService.unlockG6(imei).isSuccess());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method InventoryExternalServiceImpl.getLstStockDeliveringBill
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void tesGetLstStockDeliveringBill_01() throws Exception {
        Date startTime = null;
        Date endTime = new Date();
        Assert.assertEquals(Const.DELIVERING_STOCK.FAILED,
                inventoryExternalService.getLstStockDeliveringBill(startTime, endTime).getResponseCode());
    }

    @Test
    public void tesGetLstStockDeliveringBill_02() throws Exception {
        Date startTime = new Date();
        Date endTime = null;
        Assert.assertEquals(Const.DELIVERING_STOCK.FAILED,
                inventoryExternalService.getLstStockDeliveringBill(startTime, endTime).getResponseCode());
    }

    @Test
    public void tesGetLstStockDeliveringBill_03() throws Exception {
        Date startTime = new Date();
        Date endTime = new Date();

        List<StockDeliveringBillDetailDTO> lstStockDeliveringBill = null;
        when(stockTransActionService.getStockDeliveringResult(startTime, endTime)).thenReturn(lstStockDeliveringBill);
        Assert.assertEquals(Const.DELIVERING_STOCK.FAILED,
                inventoryExternalService.getLstStockDeliveringBill(startTime, endTime).getResponseCode());
    }

    @Test
    public void tesGetLstStockDeliveringBill_04() throws Exception {
        Date startTime = new Date();
        Date endTime = new Date();

        List<StockDeliveringBillDetailDTO> lstStockDeliveringBill = new ArrayList<>();
        StockDeliveringBillDetailDTO detailDTO1 = new StockDeliveringBillDetailDTO();
        lstStockDeliveringBill.add(detailDTO1);
        when(stockTransActionService.getStockDeliveringResult(startTime, endTime)).thenReturn(lstStockDeliveringBill);
        Assert.assertEquals(Const.DELIVERING_STOCK.SUCCESS,
                inventoryExternalService.getLstStockDeliveringBill(startTime, endTime).getResponseCode());
    }

    @Test
    public void tesGetLstStockDeliveringBill_05() throws Exception {
        Date startTime = new Date();
        Date endTime = new Date();

        List<StockDeliveringBillDetailDTO> lstStockDeliveringBill = new ArrayList<>();
        StockDeliveringBillDetailDTO detailDTO1 = new StockDeliveringBillDetailDTO();
        lstStockDeliveringBill.add(detailDTO1);
        when(stockTransActionService.getStockDeliveringResult(startTime, endTime)).thenThrow(new Exception());
        Assert.assertEquals(Const.DELIVERING_STOCK.FAILED,
                inventoryExternalService.getLstStockDeliveringBill(startTime, endTime).getResponseCode());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method InventoryExternalServiceImpl.serialCardIsSaledOnBCCS
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testSerialCardIsSaledOnBCCS_01() throws Exception {
        String serial = "";
        Assert.assertFalse(inventoryExternalService.serialCardIsSaledOnBCCS(serial).isSuccess());
        Assert.assertEquals(Const.DELIVERING_STOCK.FAILED, inventoryExternalService.serialCardIsSaledOnBCCS(serial).getErrorCode());
    }

    @Test
    public void testSerialCardIsSaledOnBCCS_02() throws Exception {
        String serial = "aa";
        Assert.assertFalse(inventoryExternalService.serialCardIsSaledOnBCCS(serial).isSuccess());
        Assert.assertEquals(Const.DELIVERING_STOCK.FAILED, inventoryExternalService.serialCardIsSaledOnBCCS(serial).getErrorCode());
    }

    @Test
    public void testSerialCardIsSaledOnBCCS_03() throws Exception {
        String serial = "1";
        List<Long> lstStatus = null;
        when(stockCardService.getStatusSerialCardSale(serial.trim())).thenReturn(lstStatus);

        Assert.assertFalse(inventoryExternalService.serialCardIsSaledOnBCCS(serial).isSuccess());
        Assert.assertEquals(Const.DELIVERING_STOCK.FAILED, inventoryExternalService.serialCardIsSaledOnBCCS(serial).getErrorCode());
    }

    @Test
    public void testSerialCardIsSaledOnBCCS_04() throws Exception {
        String serial = "1";
        List<Long> lstStatus = Lists.newArrayList(1L, 2L);
        when(stockCardService.getStatusSerialCardSale(serial.trim())).thenReturn(lstStatus);

        Assert.assertFalse(inventoryExternalService.serialCardIsSaledOnBCCS(serial).isSuccess());
        Assert.assertEquals(Const.DELIVERING_STOCK.FAILED, inventoryExternalService.serialCardIsSaledOnBCCS(serial).getErrorCode());
    }

    @Test
    public void testSerialCardIsSaledOnBCCS_05() throws Exception {
        String serial = "1";
        List<Long> lstStatus = Lists.newArrayList(1L);
        when(stockCardService.getStatusSerialCardSale(serial.trim())).thenReturn(lstStatus);

        Assert.assertFalse(inventoryExternalService.serialCardIsSaledOnBCCS(serial).isSuccess());
        Assert.assertEquals(Const.DELIVERING_STOCK.FAILED, inventoryExternalService.serialCardIsSaledOnBCCS(serial).getErrorCode());
    }

    @Test
    public void testSerialCardIsSaledOnBCCS_06() throws Exception {
        String serial = "1";
        List<Long> lstStatus = Lists.newArrayList(0L);
        when(stockCardService.getStatusSerialCardSale(serial.trim())).thenReturn(lstStatus);

        Assert.assertTrue(inventoryExternalService.serialCardIsSaledOnBCCS(serial).isSuccess());
        Assert.assertEquals(Const.DELIVERING_STOCK.SUCCESS, inventoryExternalService.serialCardIsSaledOnBCCS(serial).getErrorCode());
    }

    @Test
    public void testSerialCardIsSaledOnBCCS_07() throws Exception {
        String serial = "1";
        List<Long> lstStatus = Lists.newArrayList(0L);
        when(stockCardService.getStatusSerialCardSale(serial.trim())).thenThrow(new Exception());

        Assert.assertFalse(inventoryExternalService.serialCardIsSaledOnBCCS(serial).isSuccess());
        Assert.assertEquals(Const.DELIVERING_STOCK.FAILED, inventoryExternalService.serialCardIsSaledOnBCCS(serial).getErrorCode());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method InventoryExternalServiceImpl.checkTransferCondition
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCheckTransferCondition_01() throws Exception {
        Long staffId = 1L;
        boolean checkCollaborator = false;
        StaffDTO staffDTO = null;
        when(staffService.findOne(staffId)).thenReturn(staffDTO);

        Assert.assertFalse(inventoryExternalService.checkTransferCondition(staffId, checkCollaborator).isSuccess());
    }

    @Test
    public void testCheckTransferCondition_02() throws Exception {
        Long staffId = 1L;
        boolean checkCollaborator = false;
        StaffDTO staffDTO = new StaffDTO();
        when(staffService.findOne(staffId)).thenReturn(staffDTO);
        List<InvoiceTemplateDTO> lst = Lists.newArrayList();
        when(invoiceTemplateService.getInvoiceTemplateExsitByOwnerType(staffId, null, Const.OWNER_TYPE.STAFF)).thenReturn(lst);
        Assert.assertTrue(inventoryExternalService.checkTransferCondition(staffId, checkCollaborator).isSuccess());
    }

    @Test
    public void testCheckTransferCondition_03() throws Exception {
        Long staffId = 1L;
        boolean checkCollaborator = false;
        StaffDTO staffDTO = new StaffDTO();
        when(staffService.findOne(staffId)).thenReturn(staffDTO);
        InvoiceTemplateDTO dto = new InvoiceTemplateDTO();
        dto.setAmount(1L);
        List<InvoiceTemplateDTO> lst = Lists.newArrayList(dto);
        when(invoiceTemplateService.getInvoiceTemplateExsitByOwnerType(staffId, null, Const.OWNER_TYPE.STAFF)).thenReturn(lst);
        Assert.assertFalse(inventoryExternalService.checkTransferCondition(staffId, checkCollaborator).isSuccess());
    }

    @Test
    public void testCheckTransferCondition_04() throws Exception {
        Long staffId = 1L;
        boolean checkCollaborator = false;
        StaffDTO staffDTO = new StaffDTO();
        when(staffService.findOne(staffId)).thenReturn(staffDTO);
        InvoiceTemplateDTO dto = new InvoiceTemplateDTO();
        dto.setAmount(0L);
        List<InvoiceTemplateDTO> lst = Lists.newArrayList(dto);
        when(invoiceTemplateService.getInvoiceTemplateExsitByOwnerType(staffId, null, Const.OWNER_TYPE.STAFF)).thenReturn(lst);

        List<StockTotalDTO> lstStockTotal = Lists.newArrayList(new StockTotalDTO());
        when(stockTransService.checkHaveProductOffering(staffId, Const.OWNER_TYPE.STAFF_LONG, checkCollaborator)).thenReturn(lstStockTotal);

        List<String> lstSuspend = Lists.newArrayList("111");
        when(stockTransService.checkStockTransSuppend(staffId, Const.OWNER_TYPE.STAFF_LONG)).thenReturn(lstSuspend);
        Assert.assertFalse(inventoryExternalService.checkTransferCondition(staffId, checkCollaborator).isSuccess());
    }

    @Test
    public void testCheckTransferCondition_05() throws Exception {
        Long staffId = 1L;
        boolean checkCollaborator = false;
        StaffDTO staffDTO = new StaffDTO();
        when(staffService.findOne(staffId)).thenReturn(staffDTO);
        InvoiceTemplateDTO dto = new InvoiceTemplateDTO();
        dto.setAmount(0L);
        List<InvoiceTemplateDTO> lst = Lists.newArrayList(dto);
        when(invoiceTemplateService.getInvoiceTemplateExsitByOwnerType(staffId, null, Const.OWNER_TYPE.STAFF)).thenReturn(lst);

        List<StockTotalDTO> lstStockTotal = Lists.newArrayList();
        when(stockTransService.checkHaveProductOffering(staffId, Const.OWNER_TYPE.STAFF_LONG, checkCollaborator)).thenReturn(lstStockTotal);

        List<String> lstSuspend = Lists.newArrayList("1");
        when(stockTransService.checkStockTransSuppend(staffId, Const.OWNER_TYPE.STAFF_LONG)).thenReturn(lstSuspend);
        Assert.assertFalse(inventoryExternalService.checkTransferCondition(staffId, checkCollaborator).isSuccess());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method InventoryExternalServiceImpl.getStampInformation
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testGetStampInformation_01() throws Exception {
        List<StampListDTO> lstStampInput = Lists.newArrayList();
        StampInforDTO expected = inventoryExternalService.getStampInformation(lstStampInput);
        Assert.assertFalse(expected.isSuccess());
        Assert.assertEquals("ERR_IM", expected.getErrorCode());
        Assert.assertEquals("ext.stamp.lst.stamp.is.null", expected.getDescription());
    }

    @Test
    public void testGetStampInformation_02() throws Exception {
        StampListDTO stampListDTO = new StampListDTO();
        stampListDTO.setStockModelCode(null);
        List<StampListDTO> lstStampInput = Lists.newArrayList(stampListDTO);
        StampInforDTO expected = inventoryExternalService.getStampInformation(lstStampInput);
        Assert.assertTrue(expected.isSuccess());
        Assert.assertEquals("00", expected.getErrorCode());
        Assert.assertEquals("ext.stamp.success", expected.getDescription());
        Assert.assertEquals("ext.stamp.stock.code.is.null", expected.getLstStamps().get(0).getDescription());
    }

    @Test
    public void testGetStampInformation_03() throws Exception {
        StampListDTO stampListDTO = new StampListDTO();
        stampListDTO.setStockModelCode("modelCode");
        stampListDTO.setFromSerial("");
        List<StampListDTO> lstStampInput = Lists.newArrayList(stampListDTO);
        StampInforDTO expected = inventoryExternalService.getStampInformation(lstStampInput);
        Assert.assertTrue(expected.isSuccess());
        Assert.assertEquals("00", expected.getErrorCode());
        Assert.assertEquals("ext.stamp.success", expected.getDescription());
        Assert.assertEquals("ext.stamp.from.serial.is.null", expected.getLstStamps().get(0).getDescription());
    }

    @Test
    public void testGetStampInformation_04() throws Exception {
        StampListDTO stampListDTO = new StampListDTO();
        stampListDTO.setStockModelCode("modelCode");
        stampListDTO.setFromSerial("setFromSerial");
        List<StampListDTO> lstStampInput = Lists.newArrayList(stampListDTO);
        StampInforDTO expected = inventoryExternalService.getStampInformation(lstStampInput);
        String lstOptionSetModelCode = "";
        when(optionSetValueService.getValueByTwoCodeOption(Const.STAMP.STAMP_STOCK_MODEL_CODE, Const.STAMP.STAMP_STOCK_MODEL_CODE)).thenThrow(new Exception());

        Assert.assertTrue(expected.isSuccess());
        Assert.assertEquals("00", expected.getErrorCode());
        Assert.assertEquals("ext.stamp.success", expected.getDescription());
        Assert.assertEquals("ext.stamp.error", expected.getLstStamps().get(0).getDescription());
    }

    @Test
    public void testGetStampInformation_05() throws Exception {
        StampListDTO stampListDTO = new StampListDTO();
        stampListDTO.setStockModelCode("modelCode");
        stampListDTO.setFromSerial("setFromSerial");
        List<StampListDTO> lstStampInput = Lists.newArrayList(stampListDTO);
        String lstOptionSetModelCode = stampListDTO.getStockModelCode();
        when(optionSetValueService.getValueByTwoCodeOption(any(), any())).thenReturn(lstOptionSetModelCode);

        StampInforDTO expected = inventoryExternalService.getStampInformation(lstStampInput);
        Assert.assertTrue(expected.isSuccess());
        Assert.assertEquals("00", expected.getErrorCode());
        Assert.assertEquals("ext.stamp.success", expected.getDescription());
        Assert.assertEquals("ext.stamp.stock.code.is.not.found", expected.getLstStamps().get(0).getDescription());
    }

    @Test
    public void testGetStampInformation_06() throws Exception {
        StampListDTO stampListDTO = new StampListDTO();
        stampListDTO.setStockModelCode("modelCode");
        stampListDTO.setFromSerial("setFromSerial");
        List<StampListDTO> lstStampInput = Lists.newArrayList(stampListDTO);
        String lstOptionSetModelCode = "11";
        when(optionSetValueService.getValueByTwoCodeOption(Mockito.anyString(), Mockito.anyString())).thenReturn(lstOptionSetModelCode);

        StampInforDTO expected = inventoryExternalService.getStampInformation(lstStampInput);
        Assert.assertTrue(expected.isSuccess());
        Assert.assertEquals("00", expected.getErrorCode());
        Assert.assertEquals("ext.stamp.success", expected.getDescription());
        Assert.assertEquals("ext.stamp.stock.code.is.not.cofig", expected.getLstStamps().get(0).getDescription());
    }

    @Test
    public void testGetStampInformation_07() throws Exception {
        StampListDTO stampListDTO = new StampListDTO();
        stampListDTO.setStockModelCode("modelCode");
        stampListDTO.setFromSerial("setFromSerial");
        List<StampListDTO> lstStampInput = Lists.newArrayList(stampListDTO);
        String lstOptionSetModelCode = stampListDTO.getStockModelCode();
        when(optionSetValueService.getValueByTwoCodeOption(any(), any())).thenReturn(lstOptionSetModelCode);
        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        when(productOfferingService.findByProductOfferCode(stampListDTO.getStockModelCode().trim(), Const.STATUS_ACTIVE)).thenReturn(productOfferingDTO);

        StampInforDTO expected = inventoryExternalService.getStampInformation(lstStampInput);
        Assert.assertTrue(expected.isSuccess());
        Assert.assertEquals("00", expected.getErrorCode());
        Assert.assertEquals("ext.stamp.success", expected.getDescription());
        Assert.assertEquals("ext.stamp.serial.result.not.found", expected.getLstStamps().get(0).getDescription());
    }

    @Test
    public void testGetStampInformation_08() throws Exception {
        StampListDTO stampListDTO = new StampListDTO();
        stampListDTO.setStockModelCode("modelCode");
        stampListDTO.setFromSerial("setFromSerial");
        List<StampListDTO> lstStampInput = Lists.newArrayList(stampListDTO);
        String lstOptionSetModelCode = stampListDTO.getStockModelCode();
        when(optionSetValueService.getValueByTwoCodeOption(any(), any())).thenReturn(lstOptionSetModelCode);
        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        when(productOfferingService.findByProductOfferCode(stampListDTO.getStockModelCode().trim(), Const.STATUS_ACTIVE)).thenReturn(productOfferingDTO);
        StampDTO stampDTO = new StampDTO();
        when(stockHandsetRepo.getStampInformation(productOfferingDTO.getProductOfferingId(), stampListDTO.getFromSerial())).thenReturn(stampDTO);

        StampInforDTO expected = inventoryExternalService.getStampInformation(lstStampInput);
        Assert.assertTrue(expected.isSuccess());
        Assert.assertEquals("00", expected.getErrorCode());
        Assert.assertEquals("ext.stamp.success", expected.getDescription());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method InventoryExternalServiceImpl.checkStaffHaveStockDeliver
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCheckStaffHaveStockDeliver_01() throws Exception {
        List<String> lstStaffCode = Lists.newArrayList();
        StockDeliverResultDTO expected = inventoryExternalService.checkStaffHaveStockDeliver(lstStaffCode);
        Assert.assertFalse(expected.isSuccess());
        Assert.assertEquals("ERR_IM", expected.getErrorCode());
        Assert.assertEquals("ext.stock.deliver.staff.null", expected.getDescription());
    }

    @Test
    public void testCheckStaffHaveStockDeliver_02() throws Exception {
        List<String> lstStaffCode = Lists.newArrayList("code1");
        StaffDTO staffDTO = null;
        when(staffService.getStaffByStaffCode(anyString())).thenReturn(staffDTO);

        StockDeliverResultDTO expected = inventoryExternalService.checkStaffHaveStockDeliver(lstStaffCode);
        Assert.assertTrue(expected.isSuccess());
        Assert.assertNull(expected.getErrorCode());
        Assert.assertEquals("ext.stock.deliver.success", expected.getDescription());
    }

    @Test
    public void testCheckStaffHaveStockDeliver_03() throws Exception {
        List<String> lstStaffCode = Lists.newArrayList("code1");
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStaffId(1L);
        when(staffService.getStaffByStaffCode(anyString())).thenReturn(staffDTO);

        boolean checkStaffHaveStockTotal = false;
        when(stockTotalService.checkHaveProductInStockTotal(staffDTO.getStaffId(), Const.OWNER_TYPE.STAFF_LONG)).thenReturn(checkStaffHaveStockTotal);
        boolean checkSuspend = false;
        when(stockTransRepo.checkSuspendStock(Const.OWNER_TYPE.STAFF_LONG, staffDTO.getStaffId())).thenReturn(checkSuspend);
        boolean checkStaffHaveInvoiceList = false;
        when(invoiceListRepo.checkStaffHaveInvoiceList(staffDTO.getStaffId())).thenReturn(checkStaffHaveInvoiceList);
        boolean checkObjectHaveInvoiceTemplate = false;
        when(invoiceTemplateRepo.checkObjectHaveInvoiceTemplate(staffDTO.getStaffId(), Const.OWNER_TYPE.STAFF_LONG)).thenReturn(checkObjectHaveInvoiceTemplate);
        List<Staff> listStaff = Lists.newArrayList();
        when(staffService.getStaffList(staffDTO.getStaffId())).thenReturn(listStaff);
        List<SaleTransDTO> listSaleTransDTO = Lists.newArrayList();
        when(saleTransRepo.getListSaleTransByStaffId(staffDTO.getStaffId())).thenReturn(listSaleTransDTO);
        List<SaleTransBankplusDTO> listSaleTransBankplusDTO = Lists.newArrayList();
        when(saleTransRepo.getListSaleTransBankplusByStaffId(staffDTO.getStaffId())).thenReturn(listSaleTransBankplusDTO);
        ShopDTO shopDTO = null;
        when(shopService.getShopByShopKeeper(staffDTO.getStaffId())).thenReturn(shopDTO);


        StockDeliverResultDTO expected = inventoryExternalService.checkStaffHaveStockDeliver(lstStaffCode);
        Assert.assertTrue(expected.isSuccess());
        Assert.assertNull(expected.getErrorCode());
        Assert.assertEquals("ext.stock.deliver.success", expected.getDescription());
    }

    @Test
    public void testCheckStaffHaveStockDeliver_04() throws Exception {
        List<String> lstStaffCode = Lists.newArrayList("code1");
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStaffId(1L);
        when(staffService.getStaffByStaffCode(anyString())).thenReturn(staffDTO);

        boolean checkStaffHaveStockTotal = true;
        when(stockTotalService.checkHaveProductInStockTotal(staffDTO.getStaffId(), Const.OWNER_TYPE.STAFF_LONG)).thenReturn(checkStaffHaveStockTotal);
        boolean checkSuspend = true;
        when(stockTransRepo.checkSuspendStock(Const.OWNER_TYPE.STAFF_LONG, staffDTO.getStaffId())).thenReturn(checkSuspend);
        boolean checkStaffHaveInvoiceList = true;
        when(invoiceListRepo.checkStaffHaveInvoiceList(staffDTO.getStaffId())).thenReturn(checkStaffHaveInvoiceList);
        boolean checkObjectHaveInvoiceTemplate = true;
        when(invoiceTemplateRepo.checkObjectHaveInvoiceTemplate(staffDTO.getStaffId(), Const.OWNER_TYPE.STAFF_LONG)).thenReturn(checkObjectHaveInvoiceTemplate);
        List<Staff> listStaff = Lists.newArrayList(new Staff());
        when(staffService.getStaffList(staffDTO.getStaffId())).thenReturn(listStaff);
        List<SaleTransDTO> listSaleTransDTO = Lists.newArrayList(new SaleTransDTO());
        when(saleTransRepo.getListSaleTransByStaffId(staffDTO.getStaffId())).thenReturn(listSaleTransDTO);
        List<SaleTransBankplusDTO> listSaleTransBankplusDTO = Lists.newArrayList(new SaleTransBankplusDTO());
        when(saleTransRepo.getListSaleTransBankplusByStaffId(staffDTO.getStaffId())).thenReturn(listSaleTransBankplusDTO);
        ShopDTO shopDTO = new ShopDTO();
        when(shopService.getShopByShopKeeper(staffDTO.getStaffId())).thenReturn(shopDTO);

        StockDeliverResultDTO expected = inventoryExternalService.checkStaffHaveStockDeliver(lstStaffCode);
        Assert.assertTrue(expected.isSuccess());
        Assert.assertNull(expected.getErrorCode());
        Assert.assertEquals("ext.stock.deliver.success", expected.getDescription());
    }

    @Test
    public void testCheckStaffHaveStockDeliver_05() throws Exception {
        List<String> lstStaffCode = Lists.newArrayList("code1");
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStaffId(1L);
        when(staffService.getStaffByStaffCode(anyString())).thenReturn(staffDTO);

        boolean checkStaffHaveStockTotal = true;
        when(stockTotalService.checkHaveProductInStockTotal(staffDTO.getStaffId(), Const.OWNER_TYPE.STAFF_LONG)).thenReturn(checkStaffHaveStockTotal);
        boolean checkSuspend = true;
        when(stockTransRepo.checkSuspendStock(Const.OWNER_TYPE.STAFF_LONG, staffDTO.getStaffId())).thenReturn(checkSuspend);
        boolean checkStaffHaveInvoiceList = true;
        when(invoiceListRepo.checkStaffHaveInvoiceList(staffDTO.getStaffId())).thenReturn(checkStaffHaveInvoiceList);
        boolean checkObjectHaveInvoiceTemplate = true;
        when(invoiceTemplateRepo.checkObjectHaveInvoiceTemplate(staffDTO.getStaffId(), Const.OWNER_TYPE.STAFF_LONG)).thenReturn(checkObjectHaveInvoiceTemplate);
        List<Staff> listStaff = Lists.newArrayList(new Staff());
        when(staffService.getStaffList(staffDTO.getStaffId())).thenReturn(listStaff);
        List<SaleTransDTO> listSaleTransDTO = Lists.newArrayList(new SaleTransDTO());
        when(saleTransRepo.getListSaleTransByStaffId(staffDTO.getStaffId())).thenReturn(listSaleTransDTO);
        List<SaleTransBankplusDTO> listSaleTransBankplusDTO = Lists.newArrayList(new SaleTransBankplusDTO());
        when(saleTransRepo.getListSaleTransBankplusByStaffId(staffDTO.getStaffId())).thenReturn(listSaleTransBankplusDTO);
        ShopDTO shopDTO = null;
        when(shopService.getShopByShopKeeper(staffDTO.getStaffId())).thenReturn(shopDTO);


        StockDeliverResultDTO expected = inventoryExternalService.checkStaffHaveStockDeliver(lstStaffCode);
        Assert.assertTrue(expected.isSuccess());
        Assert.assertNull(expected.getErrorCode());
        Assert.assertEquals("ext.stock.deliver.success", expected.getDescription());
    }

    @Test
    public void testCheckStaffHaveStockDeliver_06() throws Exception {
        List<String> lstStaffCode = Lists.newArrayList("code1");
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStaffId(1L);
        when(staffService.getStaffByStaffCode(anyString())).thenReturn(staffDTO);

        boolean checkStaffHaveStockTotal = true;
        when(stockTotalService.checkHaveProductInStockTotal(staffDTO.getStaffId(), Const.OWNER_TYPE.STAFF_LONG)).thenReturn(checkStaffHaveStockTotal);
        boolean checkSuspend = true;
        when(stockTransRepo.checkSuspendStock(Const.OWNER_TYPE.STAFF_LONG, staffDTO.getStaffId())).thenReturn(checkSuspend);
        boolean checkStaffHaveInvoiceList = true;
        when(invoiceListRepo.checkStaffHaveInvoiceList(staffDTO.getStaffId())).thenReturn(checkStaffHaveInvoiceList);
        boolean checkObjectHaveInvoiceTemplate = true;
        when(invoiceTemplateRepo.checkObjectHaveInvoiceTemplate(staffDTO.getStaffId(), Const.OWNER_TYPE.STAFF_LONG)).thenReturn(checkObjectHaveInvoiceTemplate);
        List<Staff> listStaff = Lists.newArrayList(new Staff());
        when(staffService.getStaffList(staffDTO.getStaffId())).thenReturn(listStaff);
        List<SaleTransDTO> listSaleTransDTO = Lists.newArrayList(new SaleTransDTO());
        when(saleTransRepo.getListSaleTransByStaffId(staffDTO.getStaffId())).thenReturn(listSaleTransDTO);
        List<SaleTransBankplusDTO> listSaleTransBankplusDTO = Lists.newArrayList(new SaleTransBankplusDTO());
        when(saleTransRepo.getListSaleTransBankplusByStaffId(staffDTO.getStaffId())).thenReturn(listSaleTransBankplusDTO);
        ShopDTO shopDTO = new ShopDTO();
        when(shopService.getShopByShopKeeper(staffDTO.getStaffId())).thenReturn(shopDTO);
        boolean haveStockTotal = true;
        when(stockTotalService.checkHaveProductInStockTotal(shopDTO.getShopId(), Const.OWNER_TYPE.SHOP_LONG)).thenReturn(haveStockTotal);

        StockDeliverResultDTO expected = inventoryExternalService.checkStaffHaveStockDeliver(lstStaffCode);
        Assert.assertTrue(expected.isSuccess());
        Assert.assertNull(expected.getErrorCode());
        Assert.assertEquals("ext.stock.deliver.success", expected.getDescription());
    }

    @Test
    public void testCheckStaffHaveStockDeliver_07() throws Exception {
        List<String> lstStaffCode = Lists.newArrayList("code1");
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStaffId(1L);
        when(staffService.getStaffByStaffCode(anyString())).thenReturn(staffDTO);

        boolean checkStaffHaveStockTotal = false;
        when(stockTotalService.checkHaveProductInStockTotal(staffDTO.getStaffId(), Const.OWNER_TYPE.STAFF_LONG)).thenReturn(checkStaffHaveStockTotal);
        boolean checkSuspend = false;
        when(stockTransRepo.checkSuspendStock(Const.OWNER_TYPE.STAFF_LONG, staffDTO.getStaffId())).thenReturn(checkSuspend);
        boolean checkStaffHaveInvoiceList = false;
        when(invoiceListRepo.checkStaffHaveInvoiceList(staffDTO.getStaffId())).thenReturn(checkStaffHaveInvoiceList);
        boolean checkObjectHaveInvoiceTemplate = false;
        when(invoiceTemplateRepo.checkObjectHaveInvoiceTemplate(staffDTO.getStaffId(), Const.OWNER_TYPE.STAFF_LONG)).thenReturn(checkObjectHaveInvoiceTemplate);
        List<Staff> listStaff = Lists.newArrayList();
        when(staffService.getStaffList(staffDTO.getStaffId())).thenReturn(listStaff);
        List<SaleTransDTO> listSaleTransDTO = Lists.newArrayList();
        when(saleTransRepo.getListSaleTransByStaffId(staffDTO.getStaffId())).thenReturn(listSaleTransDTO);
        List<SaleTransBankplusDTO> listSaleTransBankplusDTO = Lists.newArrayList();
        when(saleTransRepo.getListSaleTransBankplusByStaffId(staffDTO.getStaffId())).thenReturn(listSaleTransBankplusDTO);
        ShopDTO shopDTO = new ShopDTO();
        when(shopService.getShopByShopKeeper(staffDTO.getStaffId())).thenReturn(shopDTO);
        boolean haveStockTotal = true;
        when(stockTotalService.checkHaveProductInStockTotal(shopDTO.getShopId(), Const.OWNER_TYPE.SHOP_LONG)).thenReturn(haveStockTotal);
        StockDeliverDTO stockDeliverDTO = new StockDeliverDTO();
        when(stockDeliverService.getStockDeliverByOwnerIdAndStatus(shopDTO.getShopId(), Const.STOCK_DELIVER.STOCK_DELIVER_STATUS_SIGN)).thenReturn(stockDeliverDTO);

        StockDeliverResultDTO expected = inventoryExternalService.checkStaffHaveStockDeliver(lstStaffCode);
        Assert.assertTrue(expected.isSuccess());
        Assert.assertNull(expected.getErrorCode());
        Assert.assertEquals("ext.stock.deliver.success", expected.getDescription());
    }

    @Test
    public void testCheckStaffHaveStockDeliver_08() throws Exception {
        List<String> lstStaffCode = Lists.newArrayList("code1");
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStaffId(1L);
        when(staffService.getStaffByStaffCode(anyString())).thenReturn(staffDTO);

        boolean checkStaffHaveStockTotal = false;
        when(stockTotalService.checkHaveProductInStockTotal(staffDTO.getStaffId(), Const.OWNER_TYPE.STAFF_LONG)).thenReturn(checkStaffHaveStockTotal);
        boolean checkSuspend = false;
        when(stockTransRepo.checkSuspendStock(Const.OWNER_TYPE.STAFF_LONG, staffDTO.getStaffId())).thenReturn(checkSuspend);
        boolean checkStaffHaveInvoiceList = false;
        when(invoiceListRepo.checkStaffHaveInvoiceList(staffDTO.getStaffId())).thenReturn(checkStaffHaveInvoiceList);
        boolean checkObjectHaveInvoiceTemplate = false;
        when(invoiceTemplateRepo.checkObjectHaveInvoiceTemplate(staffDTO.getStaffId(), Const.OWNER_TYPE.STAFF_LONG)).thenReturn(checkObjectHaveInvoiceTemplate);
        List<Staff> listStaff = Lists.newArrayList();
        when(staffService.getStaffList(staffDTO.getStaffId())).thenReturn(listStaff);
        List<SaleTransDTO> listSaleTransDTO = Lists.newArrayList();
        when(saleTransRepo.getListSaleTransByStaffId(staffDTO.getStaffId())).thenReturn(listSaleTransDTO);
        List<SaleTransBankplusDTO> listSaleTransBankplusDTO = Lists.newArrayList();
        when(saleTransRepo.getListSaleTransBankplusByStaffId(staffDTO.getStaffId())).thenReturn(listSaleTransBankplusDTO);
        ShopDTO shopDTO = new ShopDTO();
        when(shopService.getShopByShopKeeper(staffDTO.getStaffId())).thenReturn(shopDTO);
        boolean haveStockTotal = false;
        when(stockTotalService.checkHaveProductInStockTotal(shopDTO.getShopId(), Const.OWNER_TYPE.SHOP_LONG)).thenReturn(haveStockTotal);
        StockDeliverDTO stockDeliverDTO = new StockDeliverDTO();
        when(stockDeliverService.getStockDeliverByOwnerIdAndStatus(shopDTO.getShopId(), Const.STOCK_DELIVER.STOCK_DELIVER_STATUS_SIGN)).thenReturn(stockDeliverDTO);

        StockDeliverResultDTO expected = inventoryExternalService.checkStaffHaveStockDeliver(lstStaffCode);
        Assert.assertTrue(expected.isSuccess());
        Assert.assertNull(expected.getErrorCode());
        Assert.assertEquals("ext.stock.deliver.success", expected.getDescription());
    }

    @Test
    public void testCheckStaffHaveStockDeliver_09() throws Exception {
        List<String> lstStaffCode = Lists.newArrayList("code1");
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStaffId(1L);
        when(staffService.getStaffByStaffCode(anyString())).thenReturn(staffDTO);

        boolean checkStaffHaveStockTotal = false;
        when(stockTotalService.checkHaveProductInStockTotal(staffDTO.getStaffId(), Const.OWNER_TYPE.STAFF_LONG)).thenReturn(checkStaffHaveStockTotal);
        boolean checkSuspend = false;
        when(stockTransRepo.checkSuspendStock(Const.OWNER_TYPE.STAFF_LONG, staffDTO.getStaffId())).thenReturn(checkSuspend);
        boolean checkStaffHaveInvoiceList = false;
        when(invoiceListRepo.checkStaffHaveInvoiceList(staffDTO.getStaffId())).thenReturn(checkStaffHaveInvoiceList);
        boolean checkObjectHaveInvoiceTemplate = true;
        when(invoiceTemplateRepo.checkObjectHaveInvoiceTemplate(staffDTO.getStaffId(), Const.OWNER_TYPE.STAFF_LONG)).thenReturn(checkObjectHaveInvoiceTemplate);
        List<Staff> listStaff = Lists.newArrayList();
        when(staffService.getStaffList(staffDTO.getStaffId())).thenReturn(listStaff);
        List<SaleTransDTO> listSaleTransDTO = Lists.newArrayList();
        when(saleTransRepo.getListSaleTransByStaffId(staffDTO.getStaffId())).thenReturn(listSaleTransDTO);
        List<SaleTransBankplusDTO> listSaleTransBankplusDTO = Lists.newArrayList();
        when(saleTransRepo.getListSaleTransBankplusByStaffId(staffDTO.getStaffId())).thenReturn(listSaleTransBankplusDTO);
        ShopDTO shopDTO = new ShopDTO();
        when(shopService.getShopByShopKeeper(staffDTO.getStaffId())).thenReturn(shopDTO);
        boolean haveStockTotal = true;
        when(stockTotalService.checkHaveProductInStockTotal(shopDTO.getShopId(), Const.OWNER_TYPE.SHOP_LONG)).thenReturn(haveStockTotal);
        StockDeliverDTO stockDeliverDTO = new StockDeliverDTO();
        when(stockDeliverService.getStockDeliverByOwnerIdAndStatus(shopDTO.getShopId(), Const.STOCK_DELIVER.STOCK_DELIVER_STATUS_SIGN)).thenReturn(stockDeliverDTO);

        StockDeliverResultDTO expected = inventoryExternalService.checkStaffHaveStockDeliver(lstStaffCode);
        Assert.assertTrue(expected.isSuccess());
        Assert.assertNull(expected.getErrorCode());
        Assert.assertEquals("ext.stock.deliver.success", expected.getDescription());
    }

    @Test
    public void testCheckStaffHaveStockDeliver_10() throws Exception {
        List<String> lstStaffCode = Lists.newArrayList("code1");
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStaffId(1L);
        when(staffService.getStaffByStaffCode(anyString())).thenReturn(staffDTO);

        boolean checkStaffHaveStockTotal = false;
        when(stockTotalService.checkHaveProductInStockTotal(staffDTO.getStaffId(), Const.OWNER_TYPE.STAFF_LONG)).thenReturn(checkStaffHaveStockTotal);
        boolean checkSuspend = false;
        when(stockTransRepo.checkSuspendStock(Const.OWNER_TYPE.STAFF_LONG, staffDTO.getStaffId())).thenReturn(checkSuspend);
        boolean checkStaffHaveInvoiceList = false;
        when(invoiceListRepo.checkStaffHaveInvoiceList(staffDTO.getStaffId())).thenReturn(checkStaffHaveInvoiceList);
        boolean checkObjectHaveInvoiceTemplate = true;
        when(invoiceTemplateRepo.checkObjectHaveInvoiceTemplate(staffDTO.getStaffId(), Const.OWNER_TYPE.STAFF_LONG)).thenReturn(checkObjectHaveInvoiceTemplate);
        List<Staff> listStaff = Lists.newArrayList();
        when(staffService.getStaffList(staffDTO.getStaffId())).thenReturn(listStaff);
        List<SaleTransDTO> listSaleTransDTO = Lists.newArrayList();
        when(saleTransRepo.getListSaleTransByStaffId(staffDTO.getStaffId())).thenReturn(listSaleTransDTO);
        List<SaleTransBankplusDTO> listSaleTransBankplusDTO = Lists.newArrayList();
        when(saleTransRepo.getListSaleTransBankplusByStaffId(staffDTO.getStaffId())).thenReturn(listSaleTransBankplusDTO);
        ShopDTO shopDTO = new ShopDTO();
        when(shopService.getShopByShopKeeper(staffDTO.getStaffId())).thenReturn(shopDTO);
        boolean haveStockTotal = true;
        when(stockTotalService.checkHaveProductInStockTotal(shopDTO.getShopId(), Const.OWNER_TYPE.SHOP_LONG)).thenReturn(haveStockTotal);
        StockDeliverDTO stockDeliverDTO = new StockDeliverDTO();
        when(stockDeliverService.getStockDeliverByOwnerIdAndStatus(shopDTO.getShopId(), Const.STOCK_DELIVER.STOCK_DELIVER_STATUS_SIGN)).thenThrow(new Exception());

        StockDeliverResultDTO expected = inventoryExternalService.checkStaffHaveStockDeliver(lstStaffCode);
        Assert.assertTrue(expected.isSuccess());
        Assert.assertNull(expected.getErrorCode());
        Assert.assertEquals("ext.stock.deliver.success", expected.getDescription());
    }
}