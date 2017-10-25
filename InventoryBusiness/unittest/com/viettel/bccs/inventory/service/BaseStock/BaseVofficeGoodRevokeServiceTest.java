package com.viettel.bccs.inventory.service.BaseStock;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.ReportUtil;
import com.viettel.bccs.inventory.dto.StockRequestOrderDTO;
import com.viettel.bccs.inventory.dto.StockRequestOrderDetailDTO;
import com.viettel.bccs.inventory.dto.StockTransVofficeDTO;
import com.viettel.bccs.inventory.dto.VStockTransDTO;
import com.viettel.bccs.inventory.service.StockRequestOrderDetailService;
import com.viettel.bccs.inventory.service.StockRequestOrderService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.GetTextFromBundleHelper;
import com.viettel.fw.common.util.JasperReportUtils;
import com.viettel.webservice.voffice.FileAttachTranfer;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.*;

/**
 * Created by TruNV on 8/26/2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({DataUtil.class, BaseStockService.class, GetTextFromBundleHelper.class, JasperReportUtils.class})
@PowerMockIgnore("javax.management.*")
public class BaseVofficeGoodRevokeServiceTest {

    @InjectMocks
    private BaseVofficeGoodRevokeService baseVofficeGoodRevokeService;

    @Mock
    private StockRequestOrderService stockRequestOrderService;

    @Mock
    private StockRequestOrderDetailService stockRequestOrderDetailService;

    @Mock
    private ReportUtil fileUtil;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseVofficeGoodRevokeService.doValidate method -------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoValidate_1() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);

        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();

        StockRequestOrderDTO stockRequestOrderDTO = new StockRequestOrderDTO();
        Mockito.doReturn(null).when(stockRequestOrderService).findOne(any());
        baseVofficeGoodRevokeService.doValidate(stockTransVofficeDTO);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseVofficeGoodRevokeService.doValidate method -------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoValidate_2() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);

        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();

        StockRequestOrderDTO stockRequestOrderDTO = new StockRequestOrderDTO();
        stockRequestOrderDTO.setStatus("2");
        Mockito.doReturn(stockRequestOrderDTO).when(stockRequestOrderService).findOne(any());

        List<StockRequestOrderDetailDTO> lstDetail = Lists.newArrayList();
        Mockito.doReturn(null).when(stockRequestOrderDetailService).getLstDetailTemplate(any());
        baseVofficeGoodRevokeService.doValidate(stockTransVofficeDTO);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseVofficeGoodRevokeService.doValidate method -------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoValidate_3() throws Exception {

        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();

        StockRequestOrderDTO stockRequestOrderDTO = new StockRequestOrderDTO();
        stockRequestOrderDTO.setStatus("2");
        Mockito.doReturn(stockRequestOrderDTO).when(stockRequestOrderService).findOne(any());

        List<StockRequestOrderDetailDTO> lstDetail = Lists.newArrayList();
        StockRequestOrderDetailDTO stockRequestOrderDetailDTO = new StockRequestOrderDetailDTO();
        stockRequestOrderDetailDTO.setApprovedQuantity(2L);
        lstDetail.add(stockRequestOrderDetailDTO);
        Mockito.doReturn(lstDetail).when(stockRequestOrderDetailService).getLstDetailTemplate(any());
        baseVofficeGoodRevokeService.doValidate(stockTransVofficeDTO);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseVofficeGoodRevokeService.doCreateFileAttach method  **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoCreateFileAttach_1() throws Exception {
        List<FileAttachTranfer> lstFile = Lists.newArrayList();
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        List<String> lstEmail = Lists.newArrayList();
        lstEmail.add("2");

        StockRequestOrderDTO stockRequestOrderDTO = new StockRequestOrderDTO();
        stockRequestOrderDTO.setOrderCode("2");
        Mockito.doReturn(stockRequestOrderDTO).when(stockRequestOrderService).findOne(any());

        List<StockRequestOrderDetailDTO> lstDetail = Lists.newArrayList();
        StockRequestOrderDetailDTO stockRequestOrderDetailDTO = new StockRequestOrderDetailDTO();
        stockRequestOrderDetailDTO.setToOwnerName("name");
        lstDetail.add(stockRequestOrderDetailDTO);
        Mockito.doReturn(lstDetail).when(stockRequestOrderDetailService).getLstDetailTemplate(any());

        Mockito.doReturn("Text").when(fileUtil).getTemplatePath();
        byte[] bytes = {1,2};
        PowerMockito.mockStatic(JasperReportUtils.class);
        PowerMockito.when(JasperReportUtils.exportPdfByte(anyMap(), anyString())).thenReturn(bytes);

        PowerMockito.mockStatic(GetTextFromBundleHelper.class);
        PowerMockito.when(GetTextFromBundleHelper.getText(anyString())).then(returnsFirstArg());

        lstFile = baseVofficeGoodRevokeService.doCreateFileAttach(stockTransVofficeDTO, vStockTransDTO, lstEmail);

        Assert.assertEquals(1, lstFile.size());
    }

    @Test
    public void testDoCreateFileAttach_2() throws Exception {
        List<FileAttachTranfer> lstFile = Lists.newArrayList();
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        List<String> lstEmail = Lists.newArrayList();
        lstEmail.add("2");

        StockRequestOrderDTO stockRequestOrderDTO = new StockRequestOrderDTO();
        stockRequestOrderDTO.setOrderCode("2");
        Mockito.doReturn(stockRequestOrderDTO).when(stockRequestOrderService).findOne(any());

        List<StockRequestOrderDetailDTO> lstDetail = Lists.newArrayList();
        StockRequestOrderDetailDTO stockRequestOrderDetailDTO = new StockRequestOrderDetailDTO();
        stockRequestOrderDetailDTO.setToOwnerName("name");
        lstDetail.add(stockRequestOrderDetailDTO);
        Mockito.doReturn(lstDetail).when(stockRequestOrderDetailService).getLstDetailTemplate(any());

        Mockito.doReturn("Text").when(fileUtil).getTemplatePath();
        PowerMockito.mockStatic(JasperReportUtils.class);
        PowerMockito.when(JasperReportUtils.exportPdfByte(anyMap(), anyString())).thenReturn(null);

        PowerMockito.mockStatic(GetTextFromBundleHelper.class);
        PowerMockito.when(GetTextFromBundleHelper.getText(anyString())).then(returnsFirstArg());

        lstFile = baseVofficeGoodRevokeService.doCreateFileAttach(stockTransVofficeDTO, vStockTransDTO, lstEmail);

        Assert.assertEquals(0, lstFile.size());
    }

    @Test
    public void testDoCreateFileAttach_3() throws Exception {
        List<FileAttachTranfer> lstFile = Lists.newArrayList();
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        List<String> lstEmail = Lists.newArrayList();
        lstEmail.add("2");

        StockRequestOrderDTO stockRequestOrderDTO = new StockRequestOrderDTO();
        stockRequestOrderDTO.setOrderCode("2");
        Mockito.doReturn(stockRequestOrderDTO).when(stockRequestOrderService).findOne(any());

        List<StockRequestOrderDetailDTO> lstDetail = Lists.newArrayList();
        StockRequestOrderDetailDTO stockRequestOrderDetailDTO = new StockRequestOrderDetailDTO();
        stockRequestOrderDetailDTO.setToOwnerName("name");
        lstDetail.add(stockRequestOrderDetailDTO);
        Mockito.doReturn(lstDetail).when(stockRequestOrderDetailService).getLstDetailTemplate(any());

        Mockito.doReturn("Text").when(fileUtil).getTemplatePath();

        byte[] bytes = new byte[]{};
        PowerMockito.mockStatic(JasperReportUtils.class);
        PowerMockito.when(JasperReportUtils.exportPdfByte(anyMap(), anyString())).thenReturn(bytes);

        PowerMockito.mockStatic(GetTextFromBundleHelper.class);
        PowerMockito.when(GetTextFromBundleHelper.getText(anyString())).then(returnsFirstArg());

        lstFile = baseVofficeGoodRevokeService.doCreateFileAttach(stockTransVofficeDTO, vStockTransDTO, lstEmail);

        Assert.assertEquals(0, lstFile.size());
    }

    @Test
    public void testDoCreateFileAttach_4() throws Exception {
        List<FileAttachTranfer> lstFile = Lists.newArrayList();
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        List<String> lstEmail = Lists.newArrayList();
        lstEmail.add("2");

        StockRequestOrderDTO stockRequestOrderDTO = new StockRequestOrderDTO();
        stockRequestOrderDTO.setOrderCode("2");
        Mockito.doReturn(stockRequestOrderDTO).when(stockRequestOrderService).findOne(any());

        List<StockRequestOrderDetailDTO> lstDetail = Lists.newArrayList();
        StockRequestOrderDetailDTO stockRequestOrderDetailDTO = new StockRequestOrderDetailDTO();
        stockRequestOrderDetailDTO.setToOwnerName("name");
        lstDetail.add(stockRequestOrderDetailDTO);
        Mockito.doReturn(null).when(stockRequestOrderDetailService).getLstDetailTemplate(any());

        Mockito.doReturn("Text").when(fileUtil).getTemplatePath();

        byte[] bytes = new byte[]{};
        PowerMockito.mockStatic(JasperReportUtils.class);
        PowerMockito.when(JasperReportUtils.exportPdfByte(anyMap(), anyString())).thenReturn(bytes);

        PowerMockito.mockStatic(GetTextFromBundleHelper.class);
        PowerMockito.when(GetTextFromBundleHelper.getText(anyString())).then(returnsFirstArg());
        try {
            lstFile = baseVofficeGoodRevokeService.doCreateFileAttach(stockTransVofficeDTO, vStockTransDTO, lstEmail);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NullPointerException);
        }
    }

}