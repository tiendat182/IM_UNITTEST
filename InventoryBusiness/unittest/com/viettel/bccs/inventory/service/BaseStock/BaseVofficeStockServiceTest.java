package com.viettel.bccs.inventory.service.BaseStock;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.ReportUtil;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.*;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.GetTextFromBundleHelper;
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

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;

/**
 * Created by TruNV on 9/2/2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({GetTextFromBundleHelper.class, BaseVofficeStockService.class})
public class BaseVofficeStockServiceTest {

    @InjectMocks
    private BaseVofficeStockService baseVofficeStockService;

    @Mock
    private StockTransService stockTransService;

    @Mock
    private StockTransActionService stockTransActionService;

    @Mock
    private BaseReportService baseReportService;

    @Mock
    private CommonService commonService;

    @Mock
    private ReportUtil fileUtil;

    @Mock
    private PartnerContractService partnerContractService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseVofficeStockService.doValidate method ------------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoValidate_1() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( "stock.trans.voffice.valid.trans");

        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        Mockito.doReturn(null).when(stockTransActionService).findOne(any());
        baseVofficeStockService.doValidate(stockTransVofficeDTO);
    }

    @Test
    public void testDoValidate_2() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( "stock.trans.voffice.status.not.valid");

        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setSignCaStatus("3");
        Mockito.doReturn(stockTransActionDTO).when(stockTransActionService).findOne(any());
        baseVofficeStockService.doValidate(stockTransVofficeDTO);
    }

    @Test
    public void testDoValidate_3() throws Exception {

        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setSignCaStatus("2");
        Mockito.doReturn(stockTransActionDTO).when(stockTransActionService).findOne(any());
        baseVofficeStockService.doValidate(stockTransVofficeDTO);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseVofficeStockService.doCreateFileAttach method ----- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoCreateFileAttach_1() throws Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        List<String> lstEmail = Lists.newArrayList();

        BaseVofficeStockService spy = PowerMockito.spy(baseVofficeStockService);
        PowerMockito.doReturn("String").when(spy, "getFileName", any());

        ReportDTO reportDTO = new ReportDTO();
        PowerMockito.doReturn(reportDTO).when(spy, "createReportConfig", any(), any());

        List<StockTransFullDTO> data = Lists.newArrayList();
        Mockito.doReturn(data).when(stockTransService).searchStockTransDetail(anyList());

        List<StockTransDetailDTO> lstStockTransDetailDTOs = Lists.newArrayList();
        PowerMockito.doReturn(lstStockTransDetailDTOs).when(spy, "getData", anyList(), any());
        byte[] fileContent = new byte[]{};
        Mockito.doReturn(fileContent).when(baseReportService).exportWithDataSource(any(), any());
        spy.doCreateFileAttach(stockTransVofficeDTO, vStockTransDTO, lstEmail);
    }

    @Test
    public void testDoCreateFileAttach_2() throws Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        List<String> lstEmail = Lists.newArrayList();

        BaseVofficeStockService spy = PowerMockito.spy(baseVofficeStockService);
        PowerMockito.doReturn("String").when(spy, "getFileName", any());

        ReportDTO reportDTO = new ReportDTO();
        PowerMockito.doReturn(reportDTO).when(spy, "createReportConfig", any(), any());

        List<StockTransFullDTO> data = Lists.newArrayList();
        Mockito.doReturn(data).when(stockTransService).searchStockTransDetail(anyList());

        List<StockTransDetailDTO> lstStockTransDetailDTOs = Lists.newArrayList();
        PowerMockito.doReturn(lstStockTransDetailDTOs).when(spy, "getData", anyList(), any());
        byte[] fileContent = new byte[]{};
        Mockito.doReturn(null).when(baseReportService).exportWithDataSource(any(), any());
        spy.doCreateFileAttach(stockTransVofficeDTO, vStockTransDTO, lstEmail);
    }

    @Test
    public void testDoCreateFileAttach_3() throws Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        List<String> lstEmail = Lists.newArrayList();

        BaseVofficeStockService spy = PowerMockito.spy(baseVofficeStockService);
        PowerMockito.doReturn("String").when(spy, "getFileName", any());

        ReportDTO reportDTO = new ReportDTO();
        PowerMockito.doReturn(reportDTO).when(spy, "createReportConfig", any(), any());

        List<StockTransFullDTO> data = Lists.newArrayList();
        Mockito.doReturn(data).when(stockTransService).searchStockTransDetail(anyList());

        List<StockTransDetailDTO> lstStockTransDetailDTOs = Lists.newArrayList();
        PowerMockito.doReturn(lstStockTransDetailDTOs).when(spy, "getData", anyList(), any());
        byte[] fileContent = new byte[]{'1', '2'};
        Mockito.doReturn(fileContent).when(baseReportService).exportWithDataSource(any(), any());

        Mockito.doReturn(fileContent).when(fileUtil).insertComment(any(), any());
        spy.doCreateFileAttach(stockTransVofficeDTO, vStockTransDTO, lstEmail);
        Assert.assertNotNull(fileContent);
    }

    @Test
    public void testDoCreateFileAttach_4() throws Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        vStockTransDTO.setActionCode("2");
        List<String> lstEmail = Lists.newArrayList();

        BaseVofficeStockService spy = PowerMockito.spy(baseVofficeStockService);
        PowerMockito.doReturn("String").when(spy, "getFileName", any());

        ReportDTO reportDTO = new ReportDTO();
        PowerMockito.doReturn(reportDTO).when(spy, "createReportConfig", any(), any());

        List<StockTransFullDTO> data = Lists.newArrayList();
        Mockito.doReturn(data).when(stockTransService).searchStockTransDetail(anyList());

        List<StockTransDetailDTO> lstStockTransDetailDTOs = Lists.newArrayList();
        PowerMockito.doReturn(lstStockTransDetailDTOs).when(spy, "getData", anyList(), any());
        byte[] fileContent = new byte[]{'1', '2'};
        Mockito.doReturn(fileContent).when(baseReportService).exportWithDataSource(any(), any());

        Mockito.doReturn(fileContent).when(fileUtil).insertComment(any(), any());
        spy.doCreateFileAttach(stockTransVofficeDTO, vStockTransDTO, lstEmail);
        Assert.assertNotNull(fileContent);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseVofficeStockService.initContractProperties method-- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testInitContractProperties_1() throws Exception {
        HashMap bean = new HashMap();
        VStockTransDTO stockTransDTO = new VStockTransDTO();
        stockTransDTO.setFromOwnerType(4L);

        PartnerContractDTO partnerContractDTO = new PartnerContractDTO();
        Mockito.doReturn(partnerContractDTO).when(partnerContractService).findByStockTransID(any());
        baseVofficeStockService.initContractProperties(bean, stockTransDTO);
    }

    @Test
    public void testInitContractProperties_2() throws Exception {
        HashMap bean = new HashMap();
        VStockTransDTO stockTransDTO = new VStockTransDTO();
        stockTransDTO.setFromOwnerType(4L);

        PartnerContractDTO partnerContractDTO = new PartnerContractDTO();
        Mockito.doReturn(partnerContractDTO).when(partnerContractService).findByStockTransID(any());
        baseVofficeStockService.initContractProperties(bean, null);
    }

    @Test
    public void testInitContractProperties_3() throws Exception {
        HashMap bean = new HashMap();
        VStockTransDTO stockTransDTO = new VStockTransDTO();
        stockTransDTO.setFromOwnerType(3L);

        PartnerContractDTO partnerContractDTO = new PartnerContractDTO();
        Mockito.doReturn(partnerContractDTO).when(partnerContractService).findByStockTransID(any());
        baseVofficeStockService.initContractProperties(bean, stockTransDTO);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseVofficeStockService.getData method----------------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testGetData_1() throws Exception {
        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        List<StockTransFullDTO> stockTransFullDTOs = Lists.newArrayList(stockTransFullDTO);

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        stockTransDetailDTOList = baseVofficeStockService.getData(stockTransFullDTOs, vStockTransDTO);
        Assert.assertEquals(1L, stockTransDetailDTOList.size());
    }

    @Test
    public void testGetData_2() throws Exception {
        StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        List<StockTransFullDTO> stockTransFullDTOs = Lists.newArrayList();

        List<StockTransDetailDTO> stockTransDetailDTOList = Lists.newArrayList();
        stockTransDetailDTOList = baseVofficeStockService.getData(stockTransFullDTOs, vStockTransDTO);
        Assert.assertEquals(0L, stockTransDetailDTOList.size());
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseVofficeStockService.getCurrentDateString method---- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testGetCurrentDateString() throws Exception {
        PowerMockito.mockStatic(GetTextFromBundleHelper.class);

        String vOfficeDate = "String";
        PowerMockito.when(GetTextFromBundleHelper.getTextParam(any(), any(), any(), any())).then(returnsFirstArg());
        Whitebox.invokeMethod(baseVofficeStockService, "getCurrentDateString");
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseVofficeStockService.createReportConfig method------ **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testCreateReportConfig_1() throws Exception {

        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        vStockTransDTO.setActionType("1");
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        Mockito.doReturn("Path").when(fileUtil).getTemplatePath();

        BaseVofficeStockService spy = PowerMockito.spy(baseVofficeStockService);
        PowerMockito.doReturn("Name").when(spy, "getTemplateName", any(), any());

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setStatus("1");
        Mockito.doReturn(stockTransActionDTO).when(stockTransActionService).findOne(any());

        StockTransActionDTO stockTransActionOrder = new StockTransActionDTO();
        stockTransActionOrder.setStockTransActionId(1L);
        Mockito.doReturn(stockTransActionOrder).when(stockTransActionService).getStockTransActionByIdAndStatus(any(), anyList());
        PowerMockito.doReturn("Name").when(spy, "getTextParam", any(), any(), any());
        PowerMockito.doNothing().when(spy, "initContractProperties", any(), any());

        Whitebox.invokeMethod(baseVofficeStockService, "createReportConfig", vStockTransDTO, stockTransVofficeDTO);
    }

    @Test
    public void testCreateReportConfig_2() throws Exception {

        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        vStockTransDTO.setActionType("1");
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        Mockito.doReturn("Path").when(fileUtil).getTemplatePath();

        BaseVofficeStockService spy = PowerMockito.spy(baseVofficeStockService);
        PowerMockito.doReturn("Name").when(spy, "getTemplateName", any(), any());

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setStatus("2");
        Mockito.doReturn(stockTransActionDTO).when(stockTransActionService).findOne(any());

        StockTransActionDTO stockTransActionOrder = new StockTransActionDTO();
        stockTransActionOrder.setStockTransActionId(1L);
        Mockito.doReturn(null).when(stockTransActionService).getStockTransActionByIdAndStatus(any(), anyList());
        PowerMockito.doReturn("Name").when(spy, "getTextParam", any(), any(), any());
        PowerMockito.doNothing().when(spy, "initContractProperties", any(), any());

        Whitebox.invokeMethod(baseVofficeStockService, "createReportConfig", vStockTransDTO, stockTransVofficeDTO);
    }

    @Test
    public void testCreateReportConfig_3() throws Exception {

        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        vStockTransDTO.setActionType("100");
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        Mockito.doReturn("Path").when(fileUtil).getTemplatePath();

        BaseVofficeStockService spy = PowerMockito.spy(baseVofficeStockService);
        PowerMockito.doReturn("Name").when(spy, "getTemplateName", any(), any());

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setStatus("4");
        Mockito.doReturn(stockTransActionDTO).when(stockTransActionService).findOne(any());

        StockTransActionDTO stockTransActionOrder = new StockTransActionDTO();
        stockTransActionOrder.setStockTransActionId(null);
        Mockito.doReturn(stockTransActionOrder).when(stockTransActionService).getStockTransActionByIdAndStatus(any(), anyList());
        PowerMockito.doReturn("Name").when(spy, "getTextParam", any(), any(), any());
        PowerMockito.doNothing().when(spy, "initContractProperties", any(), any());

        Whitebox.invokeMethod(baseVofficeStockService, "createReportConfig", vStockTransDTO, stockTransVofficeDTO);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseVofficeStockService.getTemplateName method--------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testGetTemplateName_1() throws Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        String prefixTemplate = null;

        vStockTransDTO.setFromOwnerType(1L);
        vStockTransDTO.setFromOwnerID(7282L);
        vStockTransDTO.setStockTransStatus("4");
        vStockTransDTO.setToOwnerID(7282L);
        vStockTransDTO.setFromOwnerType(1L);
        vStockTransDTO.setStockTransType(3L);

        Mockito.doReturn("String").when(commonService).getStockReportTemplate(any(), any());
        Whitebox.invokeMethod(baseVofficeStockService, "getTemplateName", vStockTransDTO, prefixTemplate);
    }

    @Test
    public void testGetTemplateName_2() throws Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        String prefixTemplate = null;

        vStockTransDTO.setFromOwnerType(1L);
        vStockTransDTO.setFromOwnerID(7283L);
        vStockTransDTO.setStockTransStatus("5");
        vStockTransDTO.setToOwnerID(7283L);
        vStockTransDTO.setStockTransType(3L);

        Mockito.doReturn("String").when(commonService).getStockReportTemplate(any(), any());
        Whitebox.invokeMethod(baseVofficeStockService, "getTemplateName", vStockTransDTO, prefixTemplate);
    }

    @Test
    public void testGetTemplateName_3() throws Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        String prefixTemplate = null;

        vStockTransDTO.setFromOwnerType(1L);
        vStockTransDTO.setFromOwnerID(null);
        vStockTransDTO.setStockTransStatus("6");
        vStockTransDTO.setToOwnerID(null);
        vStockTransDTO.setStockTransType(6L);

        Mockito.doReturn("String").when(commonService).getStockReportTemplate(any(), any());
        Whitebox.invokeMethod(baseVofficeStockService, "getTemplateName", vStockTransDTO, prefixTemplate);
    }

    @Test
    public void testGetTemplateName_4() throws Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        String prefixTemplate = null;

        vStockTransDTO.setFromOwnerType(6L);
        vStockTransDTO.setFromOwnerID(null);
        vStockTransDTO.setStockTransStatus("6");
        vStockTransDTO.setToOwnerID(null);
        vStockTransDTO.setStockTransType(6L);

        Mockito.doReturn("String").when(commonService).getStockReportTemplate(any(), any());
        Whitebox.invokeMethod(baseVofficeStockService, "getTemplateName", vStockTransDTO, prefixTemplate);
    }

    @Test
    public void testGetTemplateName_5() throws Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        String prefixTemplate = "_TTH_CN";

        vStockTransDTO.setFromOwnerType(4L);
        vStockTransDTO.setFromOwnerID(null);
        vStockTransDTO.setStockTransStatus("1");
        vStockTransDTO.setToOwnerID(null);
        vStockTransDTO.setStockTransType(6L);

        Mockito.doReturn("String").when(commonService).getStockReportTemplate(any(), any());
        Whitebox.invokeMethod(baseVofficeStockService, "getTemplateName", vStockTransDTO, prefixTemplate);
    }

    @Test
    public void testGetTemplateName_6() throws Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        String prefixTemplate = "Name";

        vStockTransDTO.setFromOwnerType(4L);
        vStockTransDTO.setFromOwnerID(null);
        vStockTransDTO.setStockTransStatus("2");
        vStockTransDTO.setToOwnerID(null);
        vStockTransDTO.setStockTransType(6L);

        Mockito.doReturn("String").when(commonService).getStockReportTemplate(any(), any());
        Whitebox.invokeMethod(baseVofficeStockService, "getTemplateName", vStockTransDTO, prefixTemplate);
    }

    @Test
    public void testGetTemplateName_7() throws Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        String prefixTemplate = "Name";

        vStockTransDTO.setFromOwnerType(4L);
        vStockTransDTO.setFromOwnerID(null);
        vStockTransDTO.setStockTransStatus("3");
        vStockTransDTO.setToOwnerID(null);
        vStockTransDTO.setStockTransType(6L);

        Mockito.doReturn("String").when(commonService).getStockReportTemplate(any(), any());
        Whitebox.invokeMethod(baseVofficeStockService, "getTemplateName", vStockTransDTO, prefixTemplate);
    }

    @Test
    public void testGetTemplateName_8() throws Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        String prefixTemplate = "Name";

        vStockTransDTO.setFromOwnerType(4L);
        vStockTransDTO.setFromOwnerID(null);
        vStockTransDTO.setStockTransStatus("3");
        vStockTransDTO.setToOwnerID(null);
        vStockTransDTO.setStockTransType(6L);

        Mockito.doReturn("String").when(commonService).getStockReportTemplate(any(), any());
        Whitebox.invokeMethod(baseVofficeStockService, "getTemplateName", vStockTransDTO, null);
    }

    @Test
    public void testGetTemplateName_9() throws Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        String prefixTemplate = null;

        vStockTransDTO.setFromOwnerType(1L);
        vStockTransDTO.setFromOwnerID(null);
        vStockTransDTO.setStockTransStatus("5");
        vStockTransDTO.setToOwnerID(7283L);
        vStockTransDTO.setStockTransType(3L);

        Mockito.doReturn("String").when(commonService).getStockReportTemplate(any(), any());
        Whitebox.invokeMethod(baseVofficeStockService, "getTemplateName", vStockTransDTO, prefixTemplate);
    }

    @Test
    public void testGetTemplateName_10() throws Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        String prefixTemplate = null;

        vStockTransDTO.setFromOwnerType(2L);
        vStockTransDTO.setFromOwnerID(null);
        vStockTransDTO.setStockTransStatus("5");
        vStockTransDTO.setToOwnerID(7283L);
        vStockTransDTO.setStockTransType(3L);

        Mockito.doReturn("String").when(commonService).getStockReportTemplate(any(), any());
        Whitebox.invokeMethod(baseVofficeStockService, "getTemplateName", vStockTransDTO, prefixTemplate);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseVofficeStockService.getReceiptNO method------------ **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testGetReceiptNO_1() throws Exception {
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setActionCodeShop(null);
        stockTransActionDTO.setActionCode("1");
        stockTransActionDTO.setStatus("2");
        Whitebox.invokeMethod(baseVofficeStockService, "getReceiptNO", stockTransActionDTO);
    }

    @Test
    public void testGetReceiptNO_2() throws Exception {
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setActionCodeShop(null);
        stockTransActionDTO.setActionCode("1");
        stockTransActionDTO.setStatus("5");
        Whitebox.invokeMethod(baseVofficeStockService, "getReceiptNO", stockTransActionDTO);
    }

    @Test
    public void testGetReceiptNO_3() throws Exception {
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setActionCodeShop(null);
        stockTransActionDTO.setActionCode("1");
        stockTransActionDTO.setStatus("123");
        Whitebox.invokeMethod(baseVofficeStockService, "getReceiptNO", stockTransActionDTO);
    }

    @Test
    public void testGetReceiptNO_4() throws Exception {
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setActionCodeShop(null);
        stockTransActionDTO.setActionCode(null);
        stockTransActionDTO.setStatus("1234");
        Whitebox.invokeMethod(baseVofficeStockService, "getReceiptNO", stockTransActionDTO);
    }

    @Test
    public void testGetReceiptNO_5() throws Exception {
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setActionCodeShop("2");
        stockTransActionDTO.setActionCode(null);
        stockTransActionDTO.setStatus("PCX");
        Whitebox.invokeMethod(baseVofficeStockService, "getReceiptNO", stockTransActionDTO);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseVofficeStockService.getFileName method------------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testGetFileName_1() throws Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        vStockTransDTO.setActionCode("w//aaaaaaaaaaaa");
        BaseVofficeStockService spy = PowerMockito.spy(baseVofficeStockService);
        PowerMockito.doReturn("2").when(spy, "getPrefix", any());
        PowerMockito.doReturn("2").when(spy, "getSubPrefix", any());
        Whitebox.invokeMethod(baseVofficeStockService, "getFileName", vStockTransDTO);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseVofficeStockService.getSubPrefix method------------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testGetSubPrefix_1() throws Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        vStockTransDTO.setFromOwnerType(1L);
        vStockTransDTO.setActionType("5");

        Mockito.doReturn("2").when(commonService).getStockReportTemplate(any(), any());
        Whitebox.invokeMethod(baseVofficeStockService, "getSubPrefix", vStockTransDTO);
    }

    @Test
    public void testGetSubPrefix_2() throws Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        vStockTransDTO.setFromOwnerType(2L);
        vStockTransDTO.setActionType("6");

        Mockito.doReturn("2").when(commonService).getStockReportTemplate(any(), any());
        Whitebox.invokeMethod(baseVofficeStockService, "getSubPrefix", vStockTransDTO);
    }

    @Test
    public void testGetSubPrefix_3() throws Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        vStockTransDTO.setFromOwnerType(2L);
        vStockTransDTO.setActionType("4");

        Mockito.doReturn("2").when(commonService).getStockReportTemplate(any(), any());
        Whitebox.invokeMethod(baseVofficeStockService, "getSubPrefix", vStockTransDTO);
    }

    @Test
    public void testGetSubPrefix_4() throws Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        vStockTransDTO.setFromOwnerType(2L);
        vStockTransDTO.setActionType("100");

        Mockito.doReturn("2").when(commonService).getStockReportTemplate(any(), any());
        Whitebox.invokeMethod(baseVofficeStockService, "getSubPrefix", vStockTransDTO);
    }

    @Test
    public void testGetSubPrefix_5() throws Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        vStockTransDTO.setFromOwnerType(200L);
        vStockTransDTO.setActionType("100");

        Mockito.doReturn("2").when(commonService).getStockReportTemplate(any(), any());
        Whitebox.invokeMethod(baseVofficeStockService, "getSubPrefix", vStockTransDTO);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseVofficeStockService.getPrefix method--------------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testGetPrefix_1() throws Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        vStockTransDTO.setActionType("2");
        vStockTransDTO.setStockTransType(6L);

        Whitebox.invokeMethod(baseVofficeStockService, "getPrefix", vStockTransDTO);
    }

    @Test
    public void testGetPrefix_2() throws Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        vStockTransDTO.setActionType("2");
        vStockTransDTO.setStockTransType(600L);

        Whitebox.invokeMethod(baseVofficeStockService, "getPrefix", vStockTransDTO);
    }

    @Test
    public void testGetPrefix_3() throws Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        vStockTransDTO.setActionType("3");
        vStockTransDTO.setStockTransType(600L);

        Whitebox.invokeMethod(baseVofficeStockService, "getPrefix", vStockTransDTO);
    }

    @Test
    public void testGetPrefix_4() throws Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        vStockTransDTO.setActionType("5");
        vStockTransDTO.setStockTransType(600L);

        Whitebox.invokeMethod(baseVofficeStockService, "getPrefix", vStockTransDTO);
    }

    @Test
    public void testGetPrefix_5() throws Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        vStockTransDTO.setActionType("6");
        vStockTransDTO.setStockTransType(600L);

        Whitebox.invokeMethod(baseVofficeStockService, "getPrefix", vStockTransDTO);
    }

    @Test
    public void testGetPrefix_6() throws Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        vStockTransDTO.setActionType("4");
        vStockTransDTO.setStockTransType(600L);

        Whitebox.invokeMethod(baseVofficeStockService, "getPrefix", vStockTransDTO);
    }

    @Test
    public void testGetPrefix_7() throws Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        vStockTransDTO.setActionType("3");
        vStockTransDTO.setStockTransType(6L);

        Whitebox.invokeMethod(baseVofficeStockService, "getPrefix", vStockTransDTO);
    }

    @Test
    public void testGetPrefix_8() throws Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        vStockTransDTO.setActionType("2");
        vStockTransDTO.setStockTransType(7L);

        Whitebox.invokeMethod(baseVofficeStockService, "getPrefix", vStockTransDTO);
    }

    @Test
    public void testGetPrefix_9() throws Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        vStockTransDTO.setActionType("3");
        vStockTransDTO.setStockTransType(7L);

        Whitebox.invokeMethod(baseVofficeStockService, "getPrefix", vStockTransDTO);
    }

    @Test
    public void testGetPrefix_10() throws Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        vStockTransDTO.setActionType("2");
        vStockTransDTO.setStockTransType(27L);

        Whitebox.invokeMethod(baseVofficeStockService, "getPrefix", vStockTransDTO);
    }
}