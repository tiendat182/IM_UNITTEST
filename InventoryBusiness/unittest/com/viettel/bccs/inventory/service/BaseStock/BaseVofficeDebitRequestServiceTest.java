package com.viettel.bccs.inventory.service.BaseStock;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.ReportUtil;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.DebitRequestDetailService;
import com.viettel.bccs.inventory.service.DebitRequestService;
import com.viettel.bccs.inventory.service.ShopService;
import com.viettel.bccs.inventory.service.StaffService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.GetTextFromBundleHelper;
import com.viettel.fw.common.util.JasperReportUtils;
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
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;

/**
 * Created by TruNV on 9/2/2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({GetTextFromBundleHelper.class, JasperReportUtils.class, BaseVofficeDebitRequestService.class, BaseStockService.class})
public class BaseVofficeDebitRequestServiceTest {

    @InjectMocks
    private BaseVofficeDebitRequestService baseVofficeDebitRequestService;

    @Mock
    private DebitRequestService debitRequestService;

    @Mock
    private DebitRequestDetailService debitRequestDetailService;

    @Mock
    private ShopService shopService;

    @Mock
    private StaffService staffService;

    @Mock
    private ReportUtil fileUtil;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void initMocks() throws NoSuchFieldException {
        MockitoAnnotations.initMocks(this);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseVofficeDebitRequestService.doValidate method ------ **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoValidate_1() throws Exception {

        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);

        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();

        BaseVofficeDebitRequestService spy = Mockito.spy(baseVofficeDebitRequestService);
        Mockito.doReturn("2").when(spy).getText(any());

        DebitRequestDTO debitRequestDTO = new DebitRequestDTO();
        Mockito.doReturn(null).when(debitRequestService).findOne(any());
        spy.doValidate(stockTransVofficeDTO);
    }

    @Test
    public void testDoValidate_2() throws Exception {
        expectedException.expect( LogicException.class );
        expectedException.expectMessage( ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);

        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();

        BaseVofficeDebitRequestService spy = Mockito.spy(baseVofficeDebitRequestService);
        Mockito.doReturn("2").when(spy).getText(any());

        DebitRequestDTO debitRequestDTO = new DebitRequestDTO();
        Mockito.doReturn(debitRequestDTO).when(debitRequestService).findOne(any());

        DebitRequestDetailDTO debitRequestDetailDTO = new DebitRequestDetailDTO();
        List<DebitRequestDetailDTO> lstDetail = Lists.newArrayList();
        Mockito.doReturn(null).when(debitRequestDetailService).getLstDetailByRequestId(any());
        spy.doValidate(stockTransVofficeDTO);
    }

    @Test
    public void testDoValidate_3() throws Exception {

        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();

        BaseVofficeDebitRequestService spy = Mockito.spy(baseVofficeDebitRequestService);
        Mockito.doReturn("2").when(spy).getText(any());

        DebitRequestDTO debitRequestDTO = new DebitRequestDTO();
        Mockito.doReturn(debitRequestDTO).when(debitRequestService).findOne(any());

        DebitRequestDetailDTO debitRequestDetailDTO = new DebitRequestDetailDTO();
        List<DebitRequestDetailDTO> lstDetail = Lists.newArrayList(debitRequestDetailDTO);
        Mockito.doReturn(lstDetail).when(debitRequestDetailService).getLstDetailByRequestId(any());
        spy.doValidate(stockTransVofficeDTO);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for baseVofficeDebitRequestService.doCreateFileAttach method ------ **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDoCreateFileAttach_1() throws Exception {

        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        List<String> lstEmail = Lists.newArrayList();

        DebitRequestDTO debitRequestDTO = new DebitRequestDTO();
        Mockito.doReturn(debitRequestDTO).when(debitRequestService).findOne(any());

        DebitRequestReportDTO debitRequestReportDTO = new DebitRequestReportDTO();
        List<DebitRequestReportDTO> lstDetail = Lists.newArrayList(debitRequestReportDTO);
        Mockito.doReturn(lstDetail).when(debitRequestDetailService).getLstDetailForReport(any());

        StaffDTO staffDTO = new StaffDTO();
        Mockito.doReturn(null).when(staffService).getStaffByStaffCode(any());
        baseVofficeDebitRequestService.doCreateFileAttach(stockTransVofficeDTO, vStockTransDTO, lstEmail);
    }

    @Test
    public void testDoCreateFileAttach_2() throws Exception {

        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        List<String> lstEmail = Lists.newArrayList();

        DebitRequestDTO debitRequestDTO = new DebitRequestDTO();
        Mockito.doReturn(debitRequestDTO).when(debitRequestService).findOne(any());

        DebitRequestReportDTO debitRequestReportDTO = new DebitRequestReportDTO();
        List<DebitRequestReportDTO> lstDetail = Lists.newArrayList(debitRequestReportDTO);
        Mockito.doReturn(lstDetail).when(debitRequestDetailService).getLstDetailForReport(any());

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(1L);
        Mockito.doReturn(staffDTO).when(staffService).getStaffByStaffCode(any());

        MvShopStaffDTO mvShopStaffDTO = new MvShopStaffDTO();
        Mockito.doReturn(mvShopStaffDTO).when(shopService).getMViewShopStaff(any(), any());

        PowerMockito.mockStatic(GetTextFromBundleHelper.class);
        PowerMockito.when(GetTextFromBundleHelper.getTextParam(any(), any(), any(), any())).then(returnsFirstArg());

        Mockito.doReturn("2").when(fileUtil).getTemplatePath();

        byte[] bytes = new byte[]{'2','2'};
        PowerMockito.mockStatic(JasperReportUtils.class);
        PowerMockito.when(JasperReportUtils.exportPdfByte(any(), any())).thenReturn(null);

        Mockito.doReturn(bytes).when(fileUtil).insertComment(any(), any());
        baseVofficeDebitRequestService.doCreateFileAttach(stockTransVofficeDTO, vStockTransDTO, lstEmail);
    }

    @Test
    public void testDoCreateFileAttach_3() throws Exception {

        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        List<String> lstEmail = Lists.newArrayList();

        DebitRequestDTO debitRequestDTO = new DebitRequestDTO();
        Mockito.doReturn(debitRequestDTO).when(debitRequestService).findOne(any());

        DebitRequestReportDTO debitRequestReportDTO = new DebitRequestReportDTO();
        List<DebitRequestReportDTO> lstDetail = Lists.newArrayList(debitRequestReportDTO);
        Mockito.doReturn(lstDetail).when(debitRequestDetailService).getLstDetailForReport(any());

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(1L);
        Mockito.doReturn(staffDTO).when(staffService).getStaffByStaffCode(any());

        MvShopStaffDTO mvShopStaffDTO = new MvShopStaffDTO();
        Mockito.doReturn(mvShopStaffDTO).when(shopService).getMViewShopStaff(any(), any());

        PowerMockito.mockStatic(GetTextFromBundleHelper.class);
        PowerMockito.when(GetTextFromBundleHelper.getTextParam(any(), any(), any(), any())).then(returnsFirstArg());

        Mockito.doReturn("2").when(fileUtil).getTemplatePath();

        byte[] bytes = new byte[]{};
        PowerMockito.mockStatic(JasperReportUtils.class);
        PowerMockito.when(JasperReportUtils.exportPdfByte(any(), any())).thenReturn(bytes);

        Mockito.doReturn(bytes).when(fileUtil).insertComment(any(), any());
        baseVofficeDebitRequestService.doCreateFileAttach(stockTransVofficeDTO, vStockTransDTO, lstEmail);
    }

    @Test
    public void testDoCreateFileAttach_4() throws Exception {

        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        List<String> lstEmail = Lists.newArrayList();

        DebitRequestDTO debitRequestDTO = new DebitRequestDTO();
        Mockito.doReturn(debitRequestDTO).when(debitRequestService).findOne(any());

        DebitRequestReportDTO debitRequestReportDTO = new DebitRequestReportDTO();
        List<DebitRequestReportDTO> lstDetail = Lists.newArrayList(debitRequestReportDTO);
        Mockito.doReturn(lstDetail).when(debitRequestDetailService).getLstDetailForReport(any());

        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(1L);
        Mockito.doReturn(staffDTO).when(staffService).getStaffByStaffCode(any());

        MvShopStaffDTO mvShopStaffDTO = new MvShopStaffDTO();
        Mockito.doReturn(mvShopStaffDTO).when(shopService).getMViewShopStaff(any(), any());

        PowerMockito.mockStatic(GetTextFromBundleHelper.class);
        PowerMockito.when(GetTextFromBundleHelper.getTextParam(any(), any(), any(), any())).then(returnsFirstArg());

        Mockito.doReturn("2").when(fileUtil).getTemplatePath();

        byte[] bytes = new byte[]{'2','2'};
        PowerMockito.mockStatic(JasperReportUtils.class);
        PowerMockito.when(JasperReportUtils.exportPdfByte(any(), any())).thenReturn(bytes);

        Mockito.doReturn(bytes).when(fileUtil).insertComment(any(), any());
        baseVofficeDebitRequestService.doCreateFileAttach(stockTransVofficeDTO, vStockTransDTO, lstEmail);
    }

}