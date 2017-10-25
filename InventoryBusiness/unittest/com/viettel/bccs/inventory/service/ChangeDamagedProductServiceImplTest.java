package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.im1.service.ReqActivateKitService;
import com.viettel.bccs.im1.service.StockTotalIm1Service;
import com.viettel.bccs.im1.service.VcRequestService;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.BaseStock.BaseStockService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.ErrorCode;
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

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.any;

/**
 * Created by TruNV on 9/16/2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ChangeDamagedProductServiceImpl.class, BaseStockService.class, ChangeDamagedProductService.class, DbUtil.class})
@PowerMockIgnore("javax.management.*")
public class ChangeDamagedProductServiceImplTest {

    @InjectMocks
    private ChangeDamagedProductServiceImpl changeDamagedProductService;

    @Mock
    private EntityManager em;

    @Mock
    private EntityManager emIm1;

    @Mock
    private StockTransSerialService stockTransSerialService;

    @Mock
    private StaffService staffService;

    @Mock
    private ShopService shopService;

    @Mock
    private ProductOfferTypeService productOfferTypeService;

    @Mock
    private ProductOfferingService productOfferingService;

    @Mock
    private StockTotalService stockTotalService;

    @Mock
    private StockTotalIm1Service stockTotalIm1Service;

    @Mock
    private ReasonService reasonService;

    @Mock
    private StockCardService stockCardService;

    @Mock
    private StockTransActionService stockTransActionService;

    @Mock
    private ProductOfferPriceService productOfferPriceService;

    @Mock
    private ReportChangeGoodsService reportChangeGoodsService;

    @Mock
    private VcRequestService vcRequestService;

    @Mock
    private ReqActivateKitService reqActivateKitService;

    @Mock
    private OptionSetValueService optionSetValueService;

    @Mock
    private CustomerCareWs customerCareWs;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for changeDamagedProductService.checkNewSerialInput method **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testCheckNewSerialInput_1() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);

        String newSerial = "String";
        Long staffId = 1L;
        StaffDTO staffDTO = new StaffDTO();
        Mockito.doReturn(null).when(staffService).findOne(any());

        changeDamagedProductService.checkNewSerialInput(newSerial, staffId);
    }

    @Test
    public void testCheckNewSerialInput_2() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);

        String newSerial = "String";
        Long staffId = 1L;
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStatus("2");
        Mockito.doReturn(staffDTO).when(staffService).findOne(any());

        changeDamagedProductService.checkNewSerialInput(newSerial, staffId);
    }

    @Test
    public void testCheckNewSerialInput_3() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);

        String newSerial = "String";
        Long staffId = 1L;
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStatus("1");
        Mockito.doReturn(staffDTO).when(staffService).findOne(any());

        List<ChangeDamagedProductDTO> listSerial = Lists.newArrayList();
        Mockito.doReturn(null).when(stockTransSerialService).getListSerialProduct(any(), any(), any());

        changeDamagedProductService.checkNewSerialInput(newSerial, staffId);
    }

    @Test
    public void testCheckNewSerialInput_4() throws Exception {

        String newSerial = "String";
        Long staffId = 1L;
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStatus("1");
        Mockito.doReturn(staffDTO).when(staffService).findOne(any());
        ChangeDamagedProductDTO changeDamagedProductDTO = new ChangeDamagedProductDTO();
        changeDamagedProductDTO.setProductOfferTypeId(2L);
        List<ChangeDamagedProductDTO> listSerial = Lists.newArrayList(changeDamagedProductDTO);
        Mockito.doReturn(listSerial).when(stockTransSerialService).getListSerialProduct(any(), any(), any());

        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue("2");
        List<OptionSetValueDTO> optionSetValueDTOs = Lists.newArrayList(optionSetValueDTO);
        Mockito.doReturn(optionSetValueDTOs).when(optionSetValueService).getByOptionSetCode(any());

        changeDamagedProductService.checkNewSerialInput(newSerial, staffId);
    }

    @Test
    public void testCheckNewSerialInput_5() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);

        String newSerial = "String";
        Long staffId = 1L;
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStatus("1");
        Mockito.doReturn(staffDTO).when(staffService).findOne(any());
        ChangeDamagedProductDTO changeDamagedProductDTO = new ChangeDamagedProductDTO();
        changeDamagedProductDTO.setProductOfferTypeId(2L);
        List<ChangeDamagedProductDTO> listSerial = Lists.newArrayList(changeDamagedProductDTO);
        Mockito.doReturn(listSerial).when(stockTransSerialService).getListSerialProduct(any(), any(), any());

        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue("3");
        List<OptionSetValueDTO> optionSetValueDTOs = Lists.newArrayList(optionSetValueDTO);
        Mockito.doReturn(optionSetValueDTOs).when(optionSetValueService).getByOptionSetCode(any());

        changeDamagedProductService.checkNewSerialInput(newSerial, staffId);
    }

    @Test
    public void testCheckNewSerialInput_6() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);

        String newSerial = "String";
        Long staffId = 1L;
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStatus("1");
        Mockito.doReturn(staffDTO).when(staffService).findOne(any());
        ChangeDamagedProductDTO changeDamagedProductDTO = new ChangeDamagedProductDTO();
        changeDamagedProductDTO.setProductOfferTypeId(2L);
        List<ChangeDamagedProductDTO> listSerial = Lists.newArrayList(changeDamagedProductDTO);
        Mockito.doReturn(listSerial).when(stockTransSerialService).getListSerialProduct(any(), any(), any());

        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue(null);
        List<OptionSetValueDTO> optionSetValueDTOs = Lists.newArrayList(optionSetValueDTO);
        Mockito.doReturn(optionSetValueDTOs).when(optionSetValueService).getByOptionSetCode(any());

        changeDamagedProductService.checkNewSerialInput(newSerial, staffId);
    }

    @Test
    public void testCheckNewSerialInput_7() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);

        String newSerial = "String";
        Long staffId = 1L;
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStatus("1");
        Mockito.doReturn(staffDTO).when(staffService).findOne(any());
        ChangeDamagedProductDTO changeDamagedProductDTO = new ChangeDamagedProductDTO();
        changeDamagedProductDTO.setProductOfferTypeId(2L);
        List<ChangeDamagedProductDTO> listSerial = Lists.newArrayList(changeDamagedProductDTO);
        Mockito.doReturn(listSerial).when(stockTransSerialService).getListSerialProduct(any(), any(), any());

        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue(" ");
        List<OptionSetValueDTO> optionSetValueDTOs = Lists.newArrayList(optionSetValueDTO);
        Mockito.doReturn(optionSetValueDTOs).when(optionSetValueService).getByOptionSetCode(any());

        changeDamagedProductService.checkNewSerialInput(newSerial, staffId);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for changeDamagedProductService.saveChangeDamagedProduct method **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testSaveChangeDamagedProduct_1() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);

        Long productOfferTypeId = 1L;
        Long prodOfferId = 1L;
        Long reasonId = 1L;
        ChangeDamagedProductDTO changeDamagedProductDTO = new ChangeDamagedProductDTO();
        List<ChangeDamagedProductDTO> lstChangeProduct = Lists.newArrayList(changeDamagedProductDTO);
        Long staffId = 1L;

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStatus("1");
        Mockito.doReturn(null).when(staffService).findOne(any());

        changeDamagedProductService.saveChangeDamagedProduct(productOfferTypeId, prodOfferId, reasonId, lstChangeProduct, staffId);
    }

    @Test
    public void testSaveChangeDamagedProduct_2() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);

        Long productOfferTypeId = 1L;
        Long prodOfferId = 1L;
        Long reasonId = 1L;
        ChangeDamagedProductDTO changeDamagedProductDTO = new ChangeDamagedProductDTO();
        List<ChangeDamagedProductDTO> lstChangeProduct = Lists.newArrayList(changeDamagedProductDTO);
        Long staffId = 1L;

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStatus("2");
        Mockito.doReturn(staffDTO).when(staffService).findOne(any());

        changeDamagedProductService.saveChangeDamagedProduct(productOfferTypeId, prodOfferId, reasonId, lstChangeProduct, staffId);
    }

    @Test
    public void testSaveChangeDamagedProduct_3() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);

        Long productOfferTypeId = 1L;
        Long prodOfferId = 1L;
        Long reasonId = 1L;
        ChangeDamagedProductDTO changeDamagedProductDTO = new ChangeDamagedProductDTO();
        List<ChangeDamagedProductDTO> lstChangeProduct = Lists.newArrayList(changeDamagedProductDTO);
        Long staffId = 1L;

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStatus("1");
        Mockito.doReturn(staffDTO).when(staffService).findOne(any());

        changeDamagedProductService.saveChangeDamagedProduct(productOfferTypeId, prodOfferId, reasonId, null, staffId);
    }

    @Test
    public void testSaveChangeDamagedProduct_4() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);

        Long productOfferTypeId = 1L;
        Long prodOfferId = 1L;
        Long reasonId = 1L;
        ChangeDamagedProductDTO changeDamagedProductDTO = new ChangeDamagedProductDTO();
        List<ChangeDamagedProductDTO> lstChangeProduct = Lists.newArrayList(changeDamagedProductDTO);
        Long staffId = 1L;

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStatus("1");
        Mockito.doReturn(staffDTO).when(staffService).findOne(any());
        changeDamagedProductService.saveChangeDamagedProduct(productOfferTypeId, prodOfferId, reasonId, lstChangeProduct, staffId);
    }

    @Test
    public void testSaveChangeDamagedProduct_5() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);

        Long productOfferTypeId = 1L;
        Long prodOfferId = 1L;
        Long reasonId = 1L;
        ChangeDamagedProductDTO changeDamagedProductDTO = new ChangeDamagedProductDTO();
        changeDamagedProductDTO.setProdOfferId(1L);
        List<ChangeDamagedProductDTO> lstChangeProduct = Lists.newArrayList(changeDamagedProductDTO);
        Long staffId = 1L;

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStatus("1");
        Mockito.doReturn(staffDTO).when(staffService).findOne(any());

        changeDamagedProductService.saveChangeDamagedProduct(0L, prodOfferId, reasonId, lstChangeProduct, staffId);
    }

    @Test
    public void testSaveChangeDamagedProduct_6() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);

        Long productOfferTypeId = 1L;
        Long prodOfferId = 1L;
        Long reasonId = 1L;
        ChangeDamagedProductDTO changeDamagedProductDTO = new ChangeDamagedProductDTO();
        changeDamagedProductDTO.setProdOfferId(1L);
        List<ChangeDamagedProductDTO> lstChangeProduct = Lists.newArrayList(changeDamagedProductDTO);
        Long staffId = 1L;

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStatus("1");
        Mockito.doReturn(staffDTO).when(staffService).findOne(any());

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        Mockito.doReturn(null).when(productOfferTypeService).findOne(any());

        changeDamagedProductService.saveChangeDamagedProduct(productOfferTypeId, prodOfferId, reasonId, lstChangeProduct, staffId);
    }

    @Test
    public void testSaveChangeDamagedProduct_7() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);

        Long productOfferTypeId = 1L;
        Long prodOfferId = 1L;
        Long reasonId = 1L;
        ChangeDamagedProductDTO changeDamagedProductDTO = new ChangeDamagedProductDTO();
        changeDamagedProductDTO.setProdOfferId(1L);
        List<ChangeDamagedProductDTO> lstChangeProduct = Lists.newArrayList(changeDamagedProductDTO);
        Long staffId = 1L;

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStatus("1");
        Mockito.doReturn(staffDTO).when(staffService).findOne(any());

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setStatus("1");
        Mockito.doReturn(productOfferTypeDTO).when(productOfferTypeService).findOne(any());

        changeDamagedProductService.saveChangeDamagedProduct(productOfferTypeId, 0L, reasonId, lstChangeProduct, staffId);
    }

    @Test
    public void testSaveChangeDamagedProduct_8() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage( ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);

        Long productOfferTypeId = 1L;
        Long prodOfferId = 1L;
        Long reasonId = 1L;
        ChangeDamagedProductDTO changeDamagedProductDTO = new ChangeDamagedProductDTO();
        changeDamagedProductDTO.setProdOfferId(1L);
        List<ChangeDamagedProductDTO> lstChangeProduct = Lists.newArrayList(changeDamagedProductDTO);
        Long staffId = 1L;

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStatus("1");
        Mockito.doReturn(staffDTO).when(staffService).findOne(any());

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setStatus("1");
        Mockito.doReturn(productOfferTypeDTO).when(productOfferTypeService).findOne(any());

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus("1");
        Mockito.doReturn(null).when(productOfferingService).findOne(any());

        changeDamagedProductService.saveChangeDamagedProduct(productOfferTypeId, prodOfferId, reasonId, lstChangeProduct, staffId);
    }

    @Test
    public void testSaveChangeDamagedProduct_9() throws Exception {

        Long productOfferTypeId = 1L;
        Long prodOfferId = 1L;
        Long reasonId = 1L;
        ChangeDamagedProductDTO changeDamagedProductDTO = new ChangeDamagedProductDTO();
        changeDamagedProductDTO.setProdOfferId(1L);
        List<ChangeDamagedProductDTO> lstChangeProduct = Lists.newArrayList(changeDamagedProductDTO);
        Long staffId = 1L;

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStatus("1");
        Mockito.doReturn(staffDTO).when(staffService).findOne(any());

        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        productOfferTypeDTO.setStatus("1");
        Mockito.doReturn(productOfferTypeDTO).when(productOfferTypeService).findOne(any());

        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setStatus("1");
        Mockito.doReturn(productOfferingDTO).when(productOfferingService).findOne(any());

        ChangeDamagedProductServiceImpl spyService = PowerMockito.spy(changeDamagedProductService);
        PowerMockito.doReturn(null).when(spyService, "validateChangeProduct", any(), any(), any(), any(), any(), any());
        PowerMockito.doNothing().when(spyService, "saveData", any(), any(), any(), any());


        spyService.saveChangeDamagedProduct(productOfferTypeId, prodOfferId, reasonId, lstChangeProduct, staffId);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for changeDamagedProductService.saveChangeDamagedProductFile method **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testSaveChangeDamagedProductFile_1() throws Exception {
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for changeDamagedProductService.doSaveStockTransAction method **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoSaveStockTransAction_1() throws Exception {
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for changeDamagedProductService.doPrepareData method **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoPrepareData_1() throws Exception {
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for changeDamagedProductService.doValidate method **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoValidate_1() throws Exception {
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for changeDamagedProductService.executeStockTrans method **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testExecuteStockTrans_1() throws Exception {
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for changeDamagedProductService.normalSerialCard method **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testNormalSerialCard_1() throws Exception {
    }


    /** -------------------------------------------------------------------- **/
    /** --- Test for changeDamagedProductService.saveData method **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testSaveData_1() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);

        StaffDTO staffDTO = new StaffDTO();
        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        ChangeDamagedProductDTO changeDamagedProductDTO = new ChangeDamagedProductDTO();
        List<ChangeDamagedProductDTO> lstChangeProduct = Lists.newArrayList(changeDamagedProductDTO);
        ChangeDamagedProductServiceImpl spyService = PowerMockito.spy(changeDamagedProductService);

        PowerMockito.doReturn(null).when(spyService).getText(any());

        ReasonDTO reasonDTO = new ReasonDTO();
        List<ReasonDTO> lstReasonExp = Lists.newArrayList(reasonDTO);
        PowerMockito.doReturn(null).when(reasonService).getLsReasonByCode(any());

        Whitebox.invokeMethod(spyService, "saveData", staffDTO, productOfferTypeDTO, productOfferingDTO, lstChangeProduct);
    }

    @Test
    public void testSaveData_2() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);

        StaffDTO staffDTO = new StaffDTO();
        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        ChangeDamagedProductDTO changeDamagedProductDTO = new ChangeDamagedProductDTO();
        List<ChangeDamagedProductDTO> lstChangeProduct = Lists.newArrayList(changeDamagedProductDTO);
        ChangeDamagedProductServiceImpl spyService = PowerMockito.spy(changeDamagedProductService);

        PowerMockito.doReturn("String").when(spyService).getText(any());

        ReasonDTO reasonDTO = new ReasonDTO();
        List<ReasonDTO> lstReasonExp = Lists.newArrayList();
        PowerMockito.doReturn(lstReasonExp).when(reasonService).getLsReasonByCode(any());

        Whitebox.invokeMethod(spyService, "saveData", staffDTO, productOfferTypeDTO, productOfferingDTO, lstChangeProduct);
    }

    @Test
    public void testSaveData_3() throws Exception {

        StaffDTO staffDTO = new StaffDTO();
        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        ChangeDamagedProductDTO changeDamagedProductDTO = new ChangeDamagedProductDTO();
        List<ChangeDamagedProductDTO> lstChangeProduct = Lists.newArrayList();
        ChangeDamagedProductServiceImpl spyService = PowerMockito.spy(changeDamagedProductService);
        String strReasonCodeExp = "String";
        String strReasonCodeImp = "String";
        PowerMockito.doReturn(strReasonCodeExp).when(spyService).getText("REASON_EXP_CHANGE_DEMAGED_GOODS");
        PowerMockito.doReturn(strReasonCodeImp).when(spyService).getText("REASON_IMP_CHANGE_DEMAGED_GOODS");

        ReasonDTO reasonDTO = new ReasonDTO();
        reasonDTO.setReasonId(1L);
        List<ReasonDTO> lstReasonExp = Lists.newArrayList(reasonDTO);
        PowerMockito.doReturn(lstReasonExp).when(reasonService).getLsReasonByCode(any());

        ReasonDTO reasonDTO1 = new ReasonDTO();
        reasonDTO.setReasonId(1L);
        List<ReasonDTO> lstReasonImp = Lists.newArrayList(reasonDTO1);
        PowerMockito.doReturn(null).when(reasonService).getLsReasonByCode(strReasonCodeImp);

        PowerMockito.when(spyService.getSysDate(em)).thenReturn(new Date());
        StockTransDTO stockTransExport = new StockTransDTO();
        PowerMockito.doReturn(stockTransExport).when(spyService, "getStockTransExport", any(), any(), any());

        StockTransDTO stockTransExportNew = new StockTransDTO();
        PowerMockito.doReturn(stockTransExportNew).when(spyService).doSaveStockTrans(any());

        StockTransActionDTO stockTransActionExpNote = new StockTransActionDTO();
        PowerMockito.doReturn(stockTransActionExpNote).when(spyService, "getStockTransActionExpNote", any());

        PowerMockito.doReturn(stockTransActionExpNote).when(spyService).doSaveStockTransAction(any(), any());

        PowerMockito.doNothing().when(spyService).doSaveStockTransDetail(any(), any());
        PowerMockito.doNothing().when(spyService).doSaveStockTransSerial(any(), any());
        PowerMockito.doNothing().when(spyService).doSaveStockGoods(any(), any(), any());
        PowerMockito.doNothing().when(spyService).doSaveStockTotal(any(), any(), any(), any());

        StockTransDTO stockTransImport = new StockTransDTO();
        PowerMockito.doReturn(stockTransImport).when(spyService, "getStockTransImport", any(), any(), any());

        StockTransActionDTO stockTransActionImpNote = new StockTransActionDTO();
        PowerMockito.doReturn(stockTransActionImpNote).when(spyService, "getStockTransActionImpNote", any());


        Whitebox.invokeMethod(spyService, "saveData", staffDTO, productOfferTypeDTO, productOfferingDTO, lstChangeProduct);
    }

    @Test
    public void testSaveData_4() throws Exception {
        expectedException.expect(LogicException.class);
        expectedException.expectMessage(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);

        StaffDTO staffDTO = new StaffDTO();
        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        ChangeDamagedProductDTO changeDamagedProductDTO = new ChangeDamagedProductDTO();
        List<ChangeDamagedProductDTO> lstChangeProduct = Lists.newArrayList(changeDamagedProductDTO);
        ChangeDamagedProductServiceImpl spyService = PowerMockito.spy(changeDamagedProductService);
        String strReasonCodeExp = "String";
        String strReasonCodeImp = null;
        PowerMockito.doReturn(strReasonCodeExp).when(spyService).getText("REASON_EXP_CHANGE_DEMAGED_GOODS");
        PowerMockito.doReturn(strReasonCodeImp).when(spyService).getText("REASON_IMP_CHANGE_DEMAGED_GOODS");

        ReasonDTO reasonDTO = new ReasonDTO();
        reasonDTO.setReasonId(1L);
        List<ReasonDTO> lstReasonExp = Lists.newArrayList(reasonDTO);
        PowerMockito.doReturn(lstReasonExp).when(reasonService).getLsReasonByCode(any());

        ReasonDTO reasonDTO1 = new ReasonDTO();
        List<ReasonDTO> lstReasonImp = Lists.newArrayList(reasonDTO1);
        PowerMockito.doReturn(lstReasonImp).when(reasonService).getLsReasonByCode(strReasonCodeImp);

        Whitebox.invokeMethod(spyService, "saveData", staffDTO, productOfferTypeDTO, productOfferingDTO, lstChangeProduct);
    }

}