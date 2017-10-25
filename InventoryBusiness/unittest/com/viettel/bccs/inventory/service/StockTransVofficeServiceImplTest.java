package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.ReportUtil;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockTransVoffice;
import com.viettel.bccs.inventory.repo.StockTransVofficeRepo;
import com.viettel.bccs.inventory.service.BaseStock.BaseStockTransVofficeService;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.JasperReportUtils;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.service.BaseServiceImpl;
import com.viettel.voffice.autosign.Ver3AutoSign;
import com.viettel.voffice.autosign.ws.FileAttachTranfer;
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

import javax.persistence.EntityManager;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author DatLT
 * @date 14/10/2017
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({DbUtil.class, StockTransVofficeServiceImpl.class, BaseServiceImpl.class, StockTransVofficeService.class, JasperReportUtils.class})
public class StockTransVofficeServiceImplTest {
    @InjectMocks
    StockTransVofficeServiceImpl stockTransVofficeService;
    @Mock
    private final BaseMapper<StockTransVoffice, StockTransVofficeDTO> mapper = new BaseMapper(StockTransVoffice.class, StockTransVofficeDTO.class);
    @Mock
    private EntityManager em;
    @Mock
    private CommonService commonService;
    @Mock
    private StockTransService stockTransService;
    @Mock
    private BaseReportService baseReportService;
    @Mock
    private Ver3AutoSign ver3AutoSign;
    @Mock
    private PartnerContractService partnerContractService;
    @Mock
    private StockBalanceSerialService stockBalanceSerialService;
    @Mock
    private StockBalanceDetailService stockBalanceDetailService;
    @Mock
    private StockBalanceRequestService stockBalanceRequestService;
    @Mock
    private StockTransActionService stockTransActionService;
    @Mock
    private StaffService staffService;
    @Mock
    private ReportUtil fileUtil;
    @Mock
    private BaseStockTransVofficeService baseStockTransVofficeService;
    @Mock
    private ShopService shopService;
    @Mock
    private StockRequestOrderService stockRequestOrderService;
    @Mock
    private StockRequestOrderDetailService stockRequestOrderDetailService;
    @Mock
    private StockDeliverService stockDeliverService;
    @Mock
    private StockDeliverDetailService stockDeliverDetailService;
    @Mock
    private StockTransSerialService stockTransSerialService;
    @Mock
    private DebitRequestService debitRequestService;
    @Mock
    private DebitRequestDetailService debitRequestDetailService;
    @Mock
    private AreaService areaService;
    @Mock
    private StockTransVofficeRepo repository;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransVofficeServiceImpl.count
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Test
    public void testCount_1() throws Exception {
        List<FilterRequest> filters = Lists.newArrayList();
        Mockito.when(repository.count(any())).thenReturn(1L);
        Assert.assertEquals(1L, stockTransVofficeService.count(filters).longValue());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransVofficeServiceImpl.findOne
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Test
    public void testFindOne_1() throws  Exception {
        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        StockTransVoffice stockTransVoffice = mock(StockTransVoffice.class);
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        Mockito.when(repository.findOne(anyLong())).thenReturn(stockTransVoffice);
        setFinalStatic(StockTransVofficeServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(mapper.toDtoBean(stockTransVoffice)).thenReturn(stockTransVofficeDTO);
        spyService.findOne(1L);
        Assert.assertNotNull(stockTransVofficeDTO);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransVofficeServiceImpl.findAll
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Test
    public void testFindAll_1() throws  Exception {
        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        StockTransVoffice stockTransVoffice = mock(StockTransVoffice.class);
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        List<StockTransVofficeDTO> stockTransVofficeDTOList = Lists.newArrayList(stockTransVofficeDTO);
        List<StockTransVoffice> stockTransVofficeList = Lists.newArrayList(stockTransVoffice);
        Mockito.when(repository.findAll()).thenReturn(stockTransVofficeList);
        Mockito.when(mapper.toDtoBean(stockTransVofficeList)).thenReturn(stockTransVofficeDTOList);
        setFinalStatic(StockTransVofficeServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findAll();
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransVofficeServiceImpl.findByFilter
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Test
    public void testFindByFilter_1() throws  Exception {
        FilterRequest filterRequest = new FilterRequest();
        List<FilterRequest> filterRequestList = Lists.newArrayList(filterRequest);
        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        StockTransVoffice stockTransVoffice = mock(StockTransVoffice.class);
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        List<StockTransVofficeDTO> stockTransVofficeDTOList = Lists.newArrayList(stockTransVofficeDTO);
        List<StockTransVoffice> stockTransVofficeList = Lists.newArrayList(stockTransVoffice);
        Mockito.when(mapper.toDtoBean(stockTransVofficeList)).thenReturn(stockTransVofficeDTOList);
        setFinalStatic(StockTransVofficeServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findByFilter(filterRequestList);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransVofficeServiceImpl.create
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Test(expected = Exception.class)
    public void testCreate_1() throws  Exception {
        stockTransVofficeService.create(new StockTransVofficeDTO());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransVofficeServiceImpl.update
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Test(expected = Exception.class)
    public void testUpdate_1() throws  Exception {
        stockTransVofficeService.update(new StockTransVofficeDTO());
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for StockTransVofficeServiceImpl.save method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testSave_1() throws Exception {
        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        setFinalStatic(StockTransVofficeServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(mapper.toDtoBean(repository.save(mapper.toPersistenceBean(stockTransVofficeDTO)))).thenReturn(stockTransVofficeDTO);
        spyService.save(stockTransVofficeDTO);

    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for StockTransVofficeServiceImpl.doSignedVofficeValidate method --- **/
    /** -------------------------------------------------------------------- **/
    @Test(expected = Exception.class)
    public void testDoSignedVofficeValidate_1() throws Exception {
        stockTransVofficeService.doSignedVofficeValidate(null);
    }

    @Test
    public void testDoSignedVofficeValidate_2() throws Exception {
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setSignCaStatus("1");
        stockTransVofficeService.doSignedVofficeValidate(stockTransActionDTO);
    }

    @Test(expected = Exception.class)
    public void testDoSignedVofficeValidate_3() throws Exception {
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setSignCaStatus("2");
        stockTransActionDTO.setStockTransActionId(1L);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        StockTransVoffice stockTransVoffice = mock(StockTransVoffice.class);
        List<StockTransVofficeDTO> stockTransVofficeDTOList = Lists.newArrayList(stockTransVofficeDTO);
        List<StockTransVoffice> stockTransVofficeList = Lists.newArrayList(stockTransVoffice);

        Mockito.when(mapper.toDtoBean(stockTransVofficeList)).thenReturn(stockTransVofficeDTOList);
        setFinalStatic(StockTransVofficeServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(spyService.findByFilter(anyList())).thenReturn(null);
        spyService.doSignedVofficeValidate(stockTransActionDTO);
    }

    @Test(expected = Exception.class)
    public void testDoSignedVofficeValidate_4() throws Exception {
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setSignCaStatus("2");
        stockTransActionDTO.setStockTransActionId(1L);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        StockTransVofficeDTO stockTransVofficeDTO = mock(StockTransVofficeDTO.class);
        StockTransVoffice stockTransVoffice = mock(StockTransVoffice.class);
        List<StockTransVofficeDTO> stockTransVofficeDTOList = Lists.newArrayList(stockTransVofficeDTO);
        List<StockTransVoffice> stockTransVofficeList = Lists.newArrayList(stockTransVoffice);

        List<StockTransVofficeDTO> lstStockTransVoffice = Lists.newArrayList(stockTransVofficeDTO);
        when(stockTransVofficeDTO.getStatus()).thenReturn("-1");

        Mockito.when(mapper.toDtoBean(stockTransVofficeList)).thenReturn(stockTransVofficeDTOList);
        setFinalStatic(StockTransVofficeServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(spyService.findByFilter(anyList())).thenReturn(lstStockTransVoffice);
        spyService.doSignedVofficeValidate(stockTransActionDTO);
    }

    @Test(expected = Exception.class)
    public void testDoSignedVofficeValidate_5() throws Exception {
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setSignCaStatus("2");
        stockTransActionDTO.setStockTransActionId(1L);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        StockTransVofficeDTO stockTransVofficeDTO = mock(StockTransVofficeDTO.class);
        StockTransVoffice stockTransVoffice = mock(StockTransVoffice.class);
        List<StockTransVofficeDTO> stockTransVofficeDTOList = Lists.newArrayList(stockTransVofficeDTO);
        List<StockTransVoffice> stockTransVofficeList = Lists.newArrayList(stockTransVoffice);

        List<StockTransVofficeDTO> lstStockTransVoffice = Lists.newArrayList(stockTransVofficeDTO);
        when(stockTransVofficeDTO.getStatus()).thenReturn("0");

        Mockito.when(mapper.toDtoBean(stockTransVofficeList)).thenReturn(stockTransVofficeDTOList);
        setFinalStatic(StockTransVofficeServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(spyService.findByFilter(anyList())).thenReturn(lstStockTransVoffice);
        spyService.doSignedVofficeValidate(stockTransActionDTO);
    }

    @Test(expected = Exception.class)
    public void testDoSignedVofficeValidate_6() throws Exception {
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setSignCaStatus("2");
        stockTransActionDTO.setStockTransActionId(1L);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        StockTransVofficeDTO stockTransVofficeDTO = mock(StockTransVofficeDTO.class);
        StockTransVoffice stockTransVoffice = mock(StockTransVoffice.class);
        List<StockTransVofficeDTO> stockTransVofficeDTOList = Lists.newArrayList(stockTransVofficeDTO);
        List<StockTransVoffice> stockTransVofficeList = Lists.newArrayList(stockTransVoffice);

        List<StockTransVofficeDTO> lstStockTransVoffice = Lists.newArrayList(stockTransVofficeDTO);
        when(stockTransVofficeDTO.getStatus()).thenReturn("2");

        Mockito.when(mapper.toDtoBean(stockTransVofficeList)).thenReturn(stockTransVofficeDTOList);
        setFinalStatic(StockTransVofficeServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(spyService.findByFilter(anyList())).thenReturn(lstStockTransVoffice);
        spyService.doSignedVofficeValidate(stockTransActionDTO);
    }

    @Test(expected = Exception.class)
    public void testDoSignedVofficeValidate_7() throws Exception {
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setSignCaStatus("2");
        stockTransActionDTO.setStockTransActionId(1L);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        StockTransVofficeDTO stockTransVofficeDTO = mock(StockTransVofficeDTO.class);
        StockTransVoffice stockTransVoffice = mock(StockTransVoffice.class);
        List<StockTransVofficeDTO> stockTransVofficeDTOList = Lists.newArrayList(stockTransVofficeDTO);
        List<StockTransVoffice> stockTransVofficeList = Lists.newArrayList(stockTransVoffice);

        List<StockTransVofficeDTO> lstStockTransVoffice = Lists.newArrayList(stockTransVofficeDTO);
        when(stockTransVofficeDTO.getStatus()).thenReturn("3");

        Mockito.when(mapper.toDtoBean(stockTransVofficeList)).thenReturn(stockTransVofficeDTOList);
        setFinalStatic(StockTransVofficeServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(spyService.findByFilter(anyList())).thenReturn(lstStockTransVoffice);
        spyService.doSignedVofficeValidate(stockTransActionDTO);
    }

    @Test(expected = Exception.class)
    public void testDoSignedVofficeValidate_8() throws Exception {
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setSignCaStatus("2");
        stockTransActionDTO.setStockTransActionId(1L);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        StockTransVofficeDTO stockTransVofficeDTO = mock(StockTransVofficeDTO.class);
        StockTransVoffice stockTransVoffice = mock(StockTransVoffice.class);
        List<StockTransVofficeDTO> stockTransVofficeDTOList = Lists.newArrayList(stockTransVofficeDTO);
        List<StockTransVoffice> stockTransVofficeList = Lists.newArrayList(stockTransVoffice);

        List<StockTransVofficeDTO> lstStockTransVoffice = Lists.newArrayList(stockTransVofficeDTO);
        when(stockTransVofficeDTO.getStatus()).thenReturn("4");

        Mockito.when(mapper.toDtoBean(stockTransVofficeList)).thenReturn(stockTransVofficeDTOList);
        setFinalStatic(StockTransVofficeServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(spyService.findByFilter(anyList())).thenReturn(lstStockTransVoffice);
        spyService.doSignedVofficeValidate(stockTransActionDTO);
    }

    @Test
    public void testDoSignedVofficeValidate_9() throws Exception {
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setSignCaStatus("2");
        stockTransActionDTO.setStockTransActionId(1L);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        StockTransVofficeDTO stockTransVofficeDTO = mock(StockTransVofficeDTO.class);
        StockTransVoffice stockTransVoffice = mock(StockTransVoffice.class);
        List<StockTransVofficeDTO> stockTransVofficeDTOList = Lists.newArrayList(stockTransVofficeDTO);
        List<StockTransVoffice> stockTransVofficeList = Lists.newArrayList(stockTransVoffice);

        List<StockTransVofficeDTO> lstStockTransVoffice = Lists.newArrayList(stockTransVofficeDTO);
        when(stockTransVofficeDTO.getStatus()).thenReturn("5");

        Mockito.when(mapper.toDtoBean(stockTransVofficeList)).thenReturn(stockTransVofficeDTOList);
        setFinalStatic(StockTransVofficeServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(spyService.findByFilter(anyList())).thenReturn(lstStockTransVoffice);
        spyService.doSignedVofficeValidate(stockTransActionDTO);
    }

    @Test
    public void testDoSignedVofficeValidate_10() throws Exception {
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setSignCaStatus("2");
        stockTransActionDTO.setStockTransActionId(1L);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        StockTransVofficeDTO stockTransVofficeDTO = mock(StockTransVofficeDTO.class);
        StockTransVoffice stockTransVoffice = mock(StockTransVoffice.class);
        List<StockTransVofficeDTO> stockTransVofficeDTOList = Lists.newArrayList(stockTransVofficeDTO);
        List<StockTransVoffice> stockTransVofficeList = Lists.newArrayList(stockTransVoffice);

        List<StockTransVofficeDTO> lstStockTransVoffice = Lists.newArrayList(stockTransVofficeDTO);
        when(stockTransVofficeDTO.getStatus()).thenReturn("1");

        Mockito.when(mapper.toDtoBean(stockTransVofficeList)).thenReturn(stockTransVofficeDTOList);
        setFinalStatic(StockTransVofficeServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(spyService.findByFilter(anyList())).thenReturn(lstStockTransVoffice);
        spyService.doSignedVofficeValidate(stockTransActionDTO);
    }
    /** -------------------------------------------------------------------- **/
    /** --- Test for StockTransVofficeServiceImpl.findStockTransVofficeByRequestId method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testFindStockTransVofficeByRequestId_1() throws Exception {
        stockTransVofficeService.findStockTransVofficeByRequestId("");
    }

    @Test
    public void testFindStockTransVofficeByRequestId_2() throws Exception {
        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        StockTransVofficeDTO stockTransVofficeDTO = mock(StockTransVofficeDTO.class);
        StockTransVoffice stockTransVoffice = mock(StockTransVoffice.class);
        List<StockTransVofficeDTO> stockTransVofficeDTOList = Lists.newArrayList(stockTransVofficeDTO);
        List<StockTransVoffice> stockTransVofficeList = Lists.newArrayList(stockTransVoffice);

        Mockito.when(mapper.toDtoBean(stockTransVofficeList)).thenReturn(stockTransVofficeDTOList);
        setFinalStatic(StockTransVofficeServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findStockTransVofficeByRequestId("1");
    }

/** -------------------------------------------------------------------- **/
    /** --- Test for StockTransVofficeServiceImpl.findStockTransVofficeByActionId method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testFindStockTransVofficeByActionId_1() throws Exception {
        stockTransVofficeService.findStockTransVofficeByActionId(null);
    }

    @Test
    public void testFindStockTransVofficeByActionId_2() throws Exception {
        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        StockTransVofficeDTO stockTransVofficeDTO = mock(StockTransVofficeDTO.class);
        StockTransVoffice stockTransVoffice = mock(StockTransVoffice.class);
        List<StockTransVofficeDTO> stockTransVofficeDTOList = Lists.newArrayList(stockTransVofficeDTO);
        List<StockTransVoffice> stockTransVofficeList = Lists.newArrayList(stockTransVoffice);

        Mockito.when(mapper.toDtoBean(stockTransVofficeList)).thenReturn(stockTransVofficeDTOList);
        setFinalStatic(StockTransVofficeServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findStockTransVofficeByActionId(1L);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for StockTransVofficeServiceImpl.searchStockTransVoffice method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testSearchStockTransVoffice_1() throws Exception {
        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        StockTransVofficeDTO stockTransVofficeDTO = mock(StockTransVofficeDTO.class);
        StockTransVoffice stockTransVoffice = mock(StockTransVoffice.class);
        List<StockTransVofficeDTO> stockTransVofficeDTOList = Lists.newArrayList(stockTransVofficeDTO);
        List<StockTransVoffice> stockTransVofficeList = Lists.newArrayList(stockTransVoffice);

        Mockito.when(mapper.toDtoBean(stockTransVofficeList)).thenReturn(stockTransVofficeDTOList);
        setFinalStatic(StockTransVofficeServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.searchStockTransVoffice(new StockTransVofficeDTO());
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for StockTransVofficeServiceImpl.getLstVofficeSigned method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testGetLstVofficeSigned_1() throws Exception {
        stockTransVofficeService.getLstVofficeSigned(1L, "1");
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for StockTransVofficeServiceImpl.updateVofficeDOA method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testUpdateVofficeDOA_1() throws Exception {
        stockTransVofficeService.updateVofficeDOA(new StockTransVoffice());
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for StockTransVofficeServiceImpl.doSignVOffice method --- **/
    /** -------------------------------------------------------------------- **/
    @Test(expected = Exception.class)
    public void testDoSignVOffice_1() throws Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        stockTransVofficeDTO.setSignUserList("tiendat");
        stockTransVofficeDTO.setPrefixTemplate("STOCK_BALANCE");
        stockTransVofficeDTO.setErrorCode("");

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        PowerMockito.doReturn(null).when(spyService, "doCreateStockBalanceFileAttach", any(), any());
        PowerMockito.doNothing().when(spyService, "doValidate", any(), any());
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.doSignVOffice(stockTransVofficeDTO);
    }

    @Test(expected = Exception.class)
    public void testDoSignVOffice_2() throws Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        stockTransVofficeDTO.setSignUserList("tiendat");
        stockTransVofficeDTO.setPrefixTemplate("STOCK_BALANCE");
        stockTransVofficeDTO.setErrorCode("");

        FileAttachTranfer fileAttachTranfer = new FileAttachTranfer();
        List<FileAttachTranfer> listFile = Lists.newArrayList(fileAttachTranfer);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        PowerMockito.doReturn(listFile).when(spyService, "doCreateStockBalanceFileAttach", any(), any());
        PowerMockito.doNothing().when(spyService, "doValidate", any(), any());
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.doSignVOffice(stockTransVofficeDTO);
    }

    @Test(expected = Exception.class)
    public void testDoSignVOffice_3() throws Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        stockTransVofficeDTO.setSignUserList("tiendat");
        stockTransVofficeDTO.setPrefixTemplate("DOA_TRANSFER");
        stockTransVofficeDTO.setErrorCode("");

        FileAttachTranfer fileAttachTranfer = new FileAttachTranfer();
        List<FileAttachTranfer> listFile = Lists.newArrayList(fileAttachTranfer);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        PowerMockito.doReturn(null).when(spyService, "doCreateStockBalanceFileAttach", any(), any());
        PowerMockito.doNothing().when(spyService, "doValidate", any(), any());
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.doSignVOffice(stockTransVofficeDTO);
    }

    @Test(expected = Exception.class)
    public void testDoSignVOffice_4() throws Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        stockTransVofficeDTO.setSignUserList("tiendat");
        stockTransVofficeDTO.setPrefixTemplate("DOA_TRANSFER");
        stockTransVofficeDTO.setErrorCode("");

        FileAttachTranfer fileAttachTranfer = new FileAttachTranfer();
        List<FileAttachTranfer> listFile = Lists.newArrayList(fileAttachTranfer);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        PowerMockito.doReturn(listFile).when(spyService, "doCreateDOATransferFileAttach", any(), any());
        PowerMockito.doNothing().when(spyService, "doValidate", any(), any());
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.doSignVOffice(stockTransVofficeDTO);
    }

    @Test(expected = Exception.class)
    public void testDoSignVOffice_5() throws Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        stockTransVofficeDTO.setSignUserList("tiendat");
        stockTransVofficeDTO.setPrefixTemplate("GOODS_REVOKE");
        stockTransVofficeDTO.setErrorCode("");

        FileAttachTranfer fileAttachTranfer = new FileAttachTranfer();
        List<FileAttachTranfer> listFile = Lists.newArrayList(fileAttachTranfer);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        PowerMockito.doReturn(null).when(spyService, "doCreateGoodRevokeFileAttach", any(), any());
        PowerMockito.doNothing().when(spyService, "doValidate", any(), any());
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.doSignVOffice(stockTransVofficeDTO);
    }

    @Test(expected = Exception.class)
    public void testDoSignVOffice_6() throws Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        stockTransVofficeDTO.setSignUserList("tiendat");
        stockTransVofficeDTO.setPrefixTemplate("GOODS_REVOKE");
        stockTransVofficeDTO.setErrorCode("");

        FileAttachTranfer fileAttachTranfer = new FileAttachTranfer();
        List<FileAttachTranfer> listFile = Lists.newArrayList(fileAttachTranfer);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        PowerMockito.doReturn(listFile).when(spyService, "doCreateGoodRevokeFileAttach", any(), any());
        PowerMockito.doNothing().when(spyService, "doValidate", any(), any());
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.doSignVOffice(stockTransVofficeDTO);
    }

    @Test(expected = Exception.class)
    public void testDoSignVOffice_7() throws Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        stockTransVofficeDTO.setSignUserList("tiendat");
        stockTransVofficeDTO.setPrefixTemplate("STOCK_DELIVER");
        stockTransVofficeDTO.setErrorCode("");

        FileAttachTranfer fileAttachTranfer = new FileAttachTranfer();
        List<FileAttachTranfer> listFile = Lists.newArrayList(fileAttachTranfer);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        PowerMockito.doReturn(null).when(spyService, "doCreateStockDeliver", any(), any());
        PowerMockito.doNothing().when(spyService, "doValidate", any(), any());
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.doSignVOffice(stockTransVofficeDTO);
    }

    @Test(expected = Exception.class)
    public void testDoSignVOffice_8() throws Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        stockTransVofficeDTO.setSignUserList("tiendat");
        stockTransVofficeDTO.setPrefixTemplate("STOCK_DELIVER");
        stockTransVofficeDTO.setErrorCode("");

        FileAttachTranfer fileAttachTranfer = new FileAttachTranfer();
        List<FileAttachTranfer> listFile = Lists.newArrayList(fileAttachTranfer);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        PowerMockito.doReturn(listFile).when(spyService, "doCreateStockDeliver", any(), any());
        PowerMockito.doNothing().when(spyService, "doValidate", any(), any());
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.doSignVOffice(stockTransVofficeDTO);
    }

    @Test(expected = Exception.class)
    public void testDoSignVOffice_9() throws Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        stockTransVofficeDTO.setSignUserList("tiendat");
        stockTransVofficeDTO.setPrefixTemplate("DEVICE_TRANSFER");
        stockTransVofficeDTO.setErrorCode("");

        FileAttachTranfer fileAttachTranfer = new FileAttachTranfer();
        List<FileAttachTranfer> listFile = Lists.newArrayList(fileAttachTranfer);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        PowerMockito.doReturn(null).when(spyService, "doCreateStockDeviceTemplate", any(), any());
        PowerMockito.doNothing().when(spyService, "doValidate", any(), any());
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.doSignVOffice(stockTransVofficeDTO);
    }

    @Test(expected = Exception.class)
    public void testDoSignVOffice_10() throws Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        stockTransVofficeDTO.setSignUserList("tiendat");
        stockTransVofficeDTO.setPrefixTemplate("DEVICE_TRANSFER");
        stockTransVofficeDTO.setErrorCode("");

        FileAttachTranfer fileAttachTranfer = new FileAttachTranfer();
        List<FileAttachTranfer> listFile = Lists.newArrayList(fileAttachTranfer);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        PowerMockito.doReturn(listFile).when(spyService, "doCreateStockDeviceTemplate", any(), any());
        PowerMockito.doNothing().when(spyService, "doValidate", any(), any());
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.doSignVOffice(stockTransVofficeDTO);
    }

    @Test(expected = Exception.class)
    public void testDoSignVOffice_11() throws Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        stockTransVofficeDTO.setSignUserList("tiendat");
        stockTransVofficeDTO.setPrefixTemplate("DEBIT_REQUEST");
        stockTransVofficeDTO.setErrorCode("");

        FileAttachTranfer fileAttachTranfer = new FileAttachTranfer();
        List<FileAttachTranfer> listFile = Lists.newArrayList(fileAttachTranfer);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        PowerMockito.doReturn(null).when(spyService, "doCreateStockDebitRequestTemplate", any(), any());
        PowerMockito.doNothing().when(spyService, "doValidate", any(), any());
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.doSignVOffice(stockTransVofficeDTO);
    }

    @Test
    public void testDoSignVOffice_12() throws Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        stockTransVofficeDTO.setSignUserList("tiendat");
        stockTransVofficeDTO.setPrefixTemplate("DEBIT_REQUEST");
        stockTransVofficeDTO.setErrorCode("");
        stockTransVofficeDTO.setStockTransVofficeId("g");
        StockTransVoffice stockTransVoffice = new StockTransVoffice();

        FileAttachTranfer fileAttachTranfer = new FileAttachTranfer();
        List<FileAttachTranfer> listFile = Lists.newArrayList(fileAttachTranfer);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        PowerMockito.doReturn(listFile).when(spyService, "doCreateStockDebitRequestTemplate", any(), any());
        PowerMockito.doNothing().when(spyService, "doValidate", any(), any());
        Mockito.when(mapper.toPersistenceBean(stockTransVofficeDTO)).thenReturn(stockTransVoffice);
        setFinalStatic(StockTransVofficeServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.doSignVOffice(stockTransVofficeDTO);
    }

    @Test(expected = Exception.class)
    public void testDoSignVOffice_13() throws Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        stockTransVofficeDTO.setSignUserList("tiendat");
        stockTransVofficeDTO.setPrefixTemplate("HANDLE_PREFIX_TEMPLATE");
        stockTransVofficeDTO.setErrorCode("");

        FileAttachTranfer fileAttachTranfer = new FileAttachTranfer();
        List<FileAttachTranfer> listFile = Lists.newArrayList(fileAttachTranfer);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        PowerMockito.doReturn(null).when(spyService, "doCreateFileAttach", any(), any());
        PowerMockito.doNothing().when(spyService, "doValidate", any(), any());
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.doSignVOffice(stockTransVofficeDTO);
    }

    @Test(expected = Exception.class)
    public void testDoSignVOffice_14() throws Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        stockTransVofficeDTO.setSignUserList("tiendat");
        stockTransVofficeDTO.setPrefixTemplate("else");
        stockTransVofficeDTO.setErrorCode("");

        FileAttachTranfer fileAttachTranfer = new FileAttachTranfer();
        List<FileAttachTranfer> listFile = Lists.newArrayList(fileAttachTranfer);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        PowerMockito.doReturn(null).when(spyService, "doCreateFileAttach", any(), any(), any());
        PowerMockito.doNothing().when(spyService, "doValidate", any(), any());
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.doSignVOffice(stockTransVofficeDTO);
    }

    @Test(expected = Exception.class)
    public void testDoSignVOffice_15() throws Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        stockTransVofficeDTO.setSignUserList("tiendat");
        stockTransVofficeDTO.setPrefixTemplate("else");
        stockTransVofficeDTO.setErrorCode("");

        FileAttachTranfer fileAttachTranfer = new FileAttachTranfer();
        List<FileAttachTranfer> listFile = Lists.newArrayList(fileAttachTranfer);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        PowerMockito.doReturn(listFile).when(spyService, "doCreateFileAttach", any(), any(), any());
        PowerMockito.doNothing().when(spyService, "doValidate", any(), any());
        PowerMockito.doReturn(1L).when(spyService, "doAuthenticate", any(), any(), any(), any());
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.doSignVOffice(stockTransVofficeDTO);
    }

    @Test(expected = Exception.class)
    public void testDoSignVOffice_16() throws Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        stockTransVofficeDTO.setSignUserList("tiendat");
        stockTransVofficeDTO.setPrefixTemplate("else");
        stockTransVofficeDTO.setErrorCode("");

        FileAttachTranfer fileAttachTranfer = new FileAttachTranfer();
        List<FileAttachTranfer> listFile = Lists.newArrayList(fileAttachTranfer);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        PowerMockito.doReturn(listFile).when(spyService, "doCreateFileAttach", any(), any(), any());
        PowerMockito.doNothing().when(spyService, "doValidate", any(), any());
        PowerMockito.doReturn(0L).when(spyService, "doAuthenticate", any(), any(), any(), any());
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.doSignVOffice(stockTransVofficeDTO);
    }

    @Test(expected = Exception.class)
    public void testDoSignVOffice_17() throws Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        stockTransVofficeDTO.setSignUserList("tiendat");
        stockTransVofficeDTO.setPrefixTemplate("STOCK_BALANCE");
        stockTransVofficeDTO.setErrorCode("404");

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        PowerMockito.doReturn(null).when(spyService, "doCreateStockBalanceFileAttach", any(), any());
        PowerMockito.doNothing().when(spyService, "doValidate", any(), any());
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.doSignVOffice(stockTransVofficeDTO);
    }
    /** -------------------------------------------------------------------- **/
    /** --- Test for StockTransVofficeServiceImpl.doAuthenticate method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testDoAuthenticate_1() throws  Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        stockTransVofficeDTO.setAccountName("dat");
        stockTransVofficeDTO.setAccountPass("123");
        stockTransVofficeDTO.setStockTransVofficeId("1");
        stockTransVofficeDTO.setStockTransVofficeId("2");
        stockTransVofficeDTO.setCreateDate(new Date());
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        FileAttachTranfer fileAttachTranfer = new FileAttachTranfer();
        List<FileAttachTranfer> listFileAttach = Lists.newArrayList(fileAttachTranfer);
        List<String> listSign = Lists.newArrayList();

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        Whitebox.invokeMethod(spyService, "doAuthenticate", stockTransVofficeDTO, vStockTransDTO, listFileAttach, listSign);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for StockTransVofficeServiceImpl.getListInfoStaff method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testGetListInfoStaff_1() throws  Exception {
        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        Whitebox.invokeMethod(spyService, "getListInfoStaff", "tieulonglanh@gmail.com");
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for StockTransVofficeServiceImpl.doValidate method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testDoValidate_1() throws  Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        stockTransVofficeDTO.setPrefixTemplate("GOODS_REVOKE");

        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        Whitebox.invokeMethod(spyService, "doValidate", vStockTransDTO, stockTransVofficeDTO);
    }

    @Test
    public void testDoValidate_2() throws  Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        stockTransVofficeDTO.setPrefixTemplate("");

        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        Whitebox.invokeMethod(spyService, "doValidate", vStockTransDTO, stockTransVofficeDTO);
    }

    @Test
    public void testDoValidate_3() throws  Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        stockTransVofficeDTO.setPrefixTemplate("STOCK_BALANCE");
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        StockBalanceRequestDTO stockBalanceRequestDTO = new StockBalanceRequestDTO();
        List<StockBalanceRequestDTO> lstStockBalanceRequest = Lists.newArrayList(stockBalanceRequestDTO);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        when(stockBalanceRequestService.searchStockBalanceRequest(new StockBalanceRequestDTO(stockTransVofficeDTO.getStockTransActionId()))).thenReturn(null);
        Whitebox.invokeMethod(spyService, "doValidate", vStockTransDTO, stockTransVofficeDTO);
    }

    @Test
    public void testDoValidate_4() throws  Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        stockTransVofficeDTO.setPrefixTemplate("STOCK_BALANCE");
        stockTransVofficeDTO.setStockTransActionId(1L);
        VStockTransDTO vStockTransDTO = new VStockTransDTO();

        StockBalanceRequestDTO stockBalanceRequestDTO = mock(StockBalanceRequestDTO.class);
        List<StockBalanceRequestDTO> lstStockBalanceRequest = Lists.newArrayList(stockBalanceRequestDTO);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        when(stockBalanceRequestService.searchStockBalanceRequest(any())).thenReturn(lstStockBalanceRequest);
        when(stockBalanceRequestDTO.getStatus()).thenReturn(2L);
        Whitebox.invokeMethod(spyService, "doValidate", vStockTransDTO, stockTransVofficeDTO);
    }

    @Test
    public void testDoValidate_5() throws  Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        stockTransVofficeDTO.setPrefixTemplate("STOCK_BALANCE");
        stockTransVofficeDTO.setStockTransActionId(1L);
        VStockTransDTO vStockTransDTO = new VStockTransDTO();

        StockBalanceRequestDTO stockBalanceRequestDTO = mock(StockBalanceRequestDTO.class);
        List<StockBalanceRequestDTO> lstStockBalanceRequest = Lists.newArrayList(stockBalanceRequestDTO);
        StockBalanceDetailDTO stockBalanceDetailDTO = new StockBalanceDetailDTO();
        List<StockBalanceDetailDTO> stockBalanceDetailDTOs = Lists.newArrayList(stockBalanceDetailDTO);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        when(stockBalanceRequestService.searchStockBalanceRequest(any())).thenReturn(lstStockBalanceRequest);
        when(stockBalanceRequestDTO.getStatus()).thenReturn(1L);
        PowerMockito.doReturn(null).when(spyService, "getListStockBalanceDetail", any());
        Whitebox.invokeMethod(spyService, "doValidate", vStockTransDTO, stockTransVofficeDTO);
    }

    @Test
    public void testDoValidate_6() throws  Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        stockTransVofficeDTO.setPrefixTemplate("STOCK_BALANCE");
        stockTransVofficeDTO.setStockTransActionId(1L);
        VStockTransDTO vStockTransDTO = new VStockTransDTO();

        StockBalanceRequestDTO stockBalanceRequestDTO = mock(StockBalanceRequestDTO.class);
        List<StockBalanceRequestDTO> lstStockBalanceRequest = Lists.newArrayList(stockBalanceRequestDTO);
        StockBalanceDetailDTO stockBalanceDetailDTO = new StockBalanceDetailDTO();
        List<StockBalanceDetailDTO> stockBalanceDetailDTOs = Lists.newArrayList(stockBalanceDetailDTO);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        when(stockBalanceRequestService.searchStockBalanceRequest(any())).thenReturn(lstStockBalanceRequest);
        when(stockBalanceRequestDTO.getStatus()).thenReturn(1L);
        PowerMockito.doReturn(stockBalanceDetailDTOs).when(spyService, "getListStockBalanceDetail", any());
        Whitebox.invokeMethod(spyService, "doValidate", vStockTransDTO, stockTransVofficeDTO);
    }

    @Test
    public void testDoValidate_7() throws  Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        stockTransVofficeDTO.setPrefixTemplate("datLT");
        stockTransVofficeDTO.setStockTransActionId(1L);
        VStockTransDTO vStockTransDTO = null;

        StockBalanceRequestDTO stockBalanceRequestDTO = mock(StockBalanceRequestDTO.class);
        List<StockBalanceRequestDTO> lstStockBalanceRequest = Lists.newArrayList(stockBalanceRequestDTO);
        StockBalanceDetailDTO stockBalanceDetailDTO = new StockBalanceDetailDTO();
        List<StockBalanceDetailDTO> stockBalanceDetailDTOs = Lists.newArrayList(stockBalanceDetailDTO);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        when(stockBalanceRequestService.searchStockBalanceRequest(any())).thenReturn(lstStockBalanceRequest);
        when(stockBalanceRequestDTO.getStatus()).thenReturn(1L);
        PowerMockito.doReturn(stockBalanceDetailDTOs).when(spyService, "getListStockBalanceDetail", any());
        Whitebox.invokeMethod(spyService, "doValidate", vStockTransDTO, stockTransVofficeDTO);
    }

    @Test
    public void testDoValidate_8() throws  Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        stockTransVofficeDTO.setPrefixTemplate("datLT");
        stockTransVofficeDTO.setStockTransActionId(1L);
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        vStockTransDTO.setActionCode("");

        StockBalanceRequestDTO stockBalanceRequestDTO = mock(StockBalanceRequestDTO.class);
        List<StockBalanceRequestDTO> lstStockBalanceRequest = Lists.newArrayList(stockBalanceRequestDTO);
        StockBalanceDetailDTO stockBalanceDetailDTO = new StockBalanceDetailDTO();
        List<StockBalanceDetailDTO> stockBalanceDetailDTOs = Lists.newArrayList(stockBalanceDetailDTO);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        when(stockBalanceRequestService.searchStockBalanceRequest(any())).thenReturn(lstStockBalanceRequest);
        when(stockBalanceRequestDTO.getStatus()).thenReturn(1L);
        PowerMockito.doReturn(stockBalanceDetailDTOs).when(spyService, "getListStockBalanceDetail", any());
        Whitebox.invokeMethod(spyService, "doValidate", vStockTransDTO, stockTransVofficeDTO);
    }

    @Test
    public void testDoValidate_9() throws  Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        stockTransVofficeDTO.setPrefixTemplate("datLT");
        stockTransVofficeDTO.setStockTransActionId(1L);
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        vStockTransDTO.setActionCode("1");

        StockBalanceRequestDTO stockBalanceRequestDTO = mock(StockBalanceRequestDTO.class);
        List<StockBalanceRequestDTO> lstStockBalanceRequest = Lists.newArrayList(stockBalanceRequestDTO);
        StockBalanceDetailDTO stockBalanceDetailDTO = new StockBalanceDetailDTO();
        List<StockBalanceDetailDTO> stockBalanceDetailDTOs = Lists.newArrayList(stockBalanceDetailDTO);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        when(stockBalanceRequestService.searchStockBalanceRequest(any())).thenReturn(lstStockBalanceRequest);
        when(stockBalanceRequestDTO.getStatus()).thenReturn(1L);
        PowerMockito.doReturn(stockBalanceDetailDTOs).when(spyService, "getListStockBalanceDetail", any());
        Whitebox.invokeMethod(spyService, "doValidate", vStockTransDTO, stockTransVofficeDTO);
    }

    @Test
    public void testDoValidate_10() throws  Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        stockTransVofficeDTO.setPrefixTemplate("datLT");
        stockTransVofficeDTO.setStockTransActionId(1L);
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        vStockTransDTO.setActionCode("1");
        vStockTransDTO.setActionType("1");

        StockBalanceRequestDTO stockBalanceRequestDTO = mock(StockBalanceRequestDTO.class);
        List<StockBalanceRequestDTO> lstStockBalanceRequest = Lists.newArrayList(stockBalanceRequestDTO);
        StockBalanceDetailDTO stockBalanceDetailDTO = new StockBalanceDetailDTO();
        List<StockBalanceDetailDTO> stockBalanceDetailDTOs = Lists.newArrayList(stockBalanceDetailDTO);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        when(stockBalanceRequestService.searchStockBalanceRequest(any())).thenReturn(lstStockBalanceRequest);
        when(stockBalanceRequestDTO.getStatus()).thenReturn(1L);
        PowerMockito.doReturn(stockBalanceDetailDTOs).when(spyService, "getListStockBalanceDetail", any());
        Whitebox.invokeMethod(spyService, "doValidate", vStockTransDTO, stockTransVofficeDTO);
    }

    @Test
    public void testDoValidate_11() throws  Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        stockTransVofficeDTO.setPrefixTemplate("datLT");
        stockTransVofficeDTO.setStockTransActionId(1L);
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        vStockTransDTO.setActionCode("1");
        vStockTransDTO.setActionType("1");
        vStockTransDTO.setActionID(1L);

        StockBalanceRequestDTO stockBalanceRequestDTO = mock(StockBalanceRequestDTO.class);
        List<StockBalanceRequestDTO> lstStockBalanceRequest = Lists.newArrayList(stockBalanceRequestDTO);
        StockBalanceDetailDTO stockBalanceDetailDTO = new StockBalanceDetailDTO();
        List<StockBalanceDetailDTO> stockBalanceDetailDTOs = Lists.newArrayList(stockBalanceDetailDTO);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        when(stockBalanceRequestService.searchStockBalanceRequest(any())).thenReturn(lstStockBalanceRequest);
        when(stockBalanceRequestDTO.getStatus()).thenReturn(1L);
        PowerMockito.doReturn(stockBalanceDetailDTOs).when(spyService, "getListStockBalanceDetail", any());
        Whitebox.invokeMethod(spyService, "doValidate", vStockTransDTO, stockTransVofficeDTO);
    }

    @Test
    public void testDoValidate_12() throws  Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        stockTransVofficeDTO.setPrefixTemplate("datLT");
        stockTransVofficeDTO.setStockTransActionId(1L);
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        vStockTransDTO.setActionCode("1");
        vStockTransDTO.setActionType("1");
        vStockTransDTO.setFromOwnerType(3L);
        vStockTransDTO.setActionID(1L);

        StockBalanceRequestDTO stockBalanceRequestDTO = mock(StockBalanceRequestDTO.class);
        List<StockBalanceRequestDTO> lstStockBalanceRequest = Lists.newArrayList(stockBalanceRequestDTO);
        StockBalanceDetailDTO stockBalanceDetailDTO = new StockBalanceDetailDTO();
        List<StockBalanceDetailDTO> stockBalanceDetailDTOs = Lists.newArrayList(stockBalanceDetailDTO);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        when(stockBalanceRequestService.searchStockBalanceRequest(any())).thenReturn(lstStockBalanceRequest);
        when(stockBalanceRequestDTO.getStatus()).thenReturn(1L);
        PowerMockito.doReturn(stockBalanceDetailDTOs).when(spyService, "getListStockBalanceDetail", any());
        Whitebox.invokeMethod(spyService, "doValidate", vStockTransDTO, stockTransVofficeDTO);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for StockTransVofficeServiceImpl.getListStockBalanceDetail method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testGetListStockBalanceDetail_1() throws  Exception {
        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        Whitebox.invokeMethod(spyService, "getListStockBalanceDetail",1L);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for StockTransVofficeServiceImpl.doCreateGoodRevokeFileAttach method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testDoCreateGoodRevokeFileAttach_1() throws  Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        List<String> lstEmail = Lists.newArrayList();

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        when(stockRequestOrderService.findOne(anyLong())).thenReturn(null);
        Whitebox.invokeMethod(spyService, "doCreateGoodRevokeFileAttach",stockTransVofficeDTO, lstEmail);
    }

    @Test
    public void testDoCreateGoodRevokeFileAttach_2() throws  Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        List<String> lstEmail = Lists.newArrayList();
        StockRequestOrderDTO stockRequestOrderDTO = mock(StockRequestOrderDTO.class);

        StockRequestOrderDetailDTO stockRequestOrderDetailDTO = new StockRequestOrderDetailDTO();

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        when(stockRequestOrderService.findOne(anyLong())).thenReturn(stockRequestOrderDTO);
        when(stockRequestOrderDetailService.getLstDetailTemplate(anyLong())).thenReturn(null);
        Whitebox.invokeMethod(spyService, "doCreateGoodRevokeFileAttach",stockTransVofficeDTO, lstEmail);
    }

    @Test
    public void testDoCreateGoodRevokeFileAttach_3() throws  Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        List<String> lstEmail = Lists.newArrayList();
        StockRequestOrderDTO stockRequestOrderDTO = mock(StockRequestOrderDTO.class);

        StockRequestOrderDetailDTO stockRequestOrderDetailDTO = new StockRequestOrderDetailDTO();
        List<StockRequestOrderDetailDTO> lstDetail = Lists.newArrayList(stockRequestOrderDetailDTO) ;

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        PowerMockito.mockStatic(JasperReportUtils.class);
        when(JasperReportUtils.exportPdfByte(any(), anyString())).thenReturn( new byte[10]);
        when(stockRequestOrderService.findOne(anyLong())).thenReturn(stockRequestOrderDTO);
        when(stockRequestOrderDetailService.getLstDetailTemplate(anyLong())).thenReturn(lstDetail);
        Whitebox.invokeMethod(spyService, "doCreateGoodRevokeFileAttach",stockTransVofficeDTO, lstEmail);
    }

    @Test
    public void testDoCreateGoodRevokeFileAttach_4() throws  Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        List<String> lstEmail = Lists.newArrayList();
        StockRequestOrderDTO stockRequestOrderDTO = mock(StockRequestOrderDTO.class);

        StockRequestOrderDetailDTO stockRequestOrderDetailDTO = new StockRequestOrderDetailDTO();
        List<StockRequestOrderDetailDTO> lstDetail = Lists.newArrayList(stockRequestOrderDetailDTO) ;

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        PowerMockito.mockStatic(JasperReportUtils.class);
        when(JasperReportUtils.exportPdfByte(any(), anyString())).thenReturn( new byte[0]);
        when(stockRequestOrderService.findOne(anyLong())).thenReturn(stockRequestOrderDTO);
        when(stockRequestOrderDetailService.getLstDetailTemplate(anyLong())).thenReturn(lstDetail);
        Whitebox.invokeMethod(spyService, "doCreateGoodRevokeFileAttach",stockTransVofficeDTO, lstEmail);
    }

    @Test
    public void testDoCreateGoodRevokeFileAttach_5() throws  Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        List<String> lstEmail = Lists.newArrayList();
        StockRequestOrderDTO stockRequestOrderDTO = mock(StockRequestOrderDTO.class);

        StockRequestOrderDetailDTO stockRequestOrderDetailDTO = new StockRequestOrderDetailDTO();
        List<StockRequestOrderDetailDTO> lstDetail = Lists.newArrayList(stockRequestOrderDetailDTO) ;

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        when(stockRequestOrderService.findOne(anyLong())).thenReturn(stockRequestOrderDTO);
        when(stockRequestOrderDetailService.getLstDetailTemplate(anyLong())).thenReturn(lstDetail);
        Whitebox.invokeMethod(spyService, "doCreateGoodRevokeFileAttach",stockTransVofficeDTO, lstEmail);
    }

    @Test
    public void testDoCreateGoodRevokeFileAttach_6() throws  Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        List<String> lstEmail = Lists.newArrayList();
        StockRequestOrderDTO stockRequestOrderDTO = mock(StockRequestOrderDTO.class);

        StockRequestOrderDetailDTO stockRequestOrderDetailDTO = new StockRequestOrderDetailDTO();
        List<StockRequestOrderDetailDTO> lstDetail = Lists.newArrayList(stockRequestOrderDetailDTO) ;

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        PowerMockito.mockStatic(JasperReportUtils.class);
        when(JasperReportUtils.exportPdfByte(any(), anyString())).thenReturn( null);
        when(stockRequestOrderService.findOne(anyLong())).thenReturn(stockRequestOrderDTO);
        when(stockRequestOrderDetailService.getLstDetailTemplate(anyLong())).thenReturn(lstDetail);
        Whitebox.invokeMethod(spyService, "doCreateGoodRevokeFileAttach",stockTransVofficeDTO, lstEmail);
    }
    /** -------------------------------------------------------------------- **/
    /** --- Test for StockTransVofficeServiceImpl.doCreateStockDebitRequestTemplate method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testDoCreateStockDebitRequestTemplate_1() throws  Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        List<String> lstEmail = Lists.newArrayList();
        StaffDTO staffDTO = new StaffDTO();
        DebitRequestReportDTO debitRequestReportDTO = new DebitRequestReportDTO();
        List<DebitRequestReportDTO> lstDetail = Lists.newArrayList(debitRequestReportDTO);
        DebitRequestDTO debitRequestDTO = new DebitRequestDTO();

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        when(staffService.getStaffByStaffCode(anyString())).thenReturn(null);
        when(debitRequestDetailService.getLstDetailForReport(stockTransVofficeDTO.getStockTransActionId())).thenReturn(lstDetail);
        when(debitRequestService.findOne(anyLong())).thenReturn(debitRequestDTO);
        Whitebox.invokeMethod(spyService, "doCreateStockDebitRequestTemplate",stockTransVofficeDTO, lstEmail);
    }

    @Test
    public void testDoCreateStockDebitRequestTemplate_2() throws  Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        List<String> lstEmail = Lists.newArrayList();
        StaffDTO staffDTO = mock(StaffDTO.class);
        DebitRequestReportDTO debitRequestReportDTO = mock(DebitRequestReportDTO.class);
        List<DebitRequestReportDTO> lstDetail = Lists.newArrayList(debitRequestReportDTO);
        DebitRequestDTO debitRequestDTO = mock(DebitRequestDTO.class);
        ShopDTO shopDTO = mock(ShopDTO.class);
        AreaDTO province = areaService.findByCode(shopDTO.getProvince());
        AreaDTO district = areaService.findByCode(shopDTO.getProvince() + shopDTO.getDistrict());

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        when(staffService.getStaffByStaffCode(anyString())).thenReturn(staffDTO);
        when(debitRequestDetailService.getLstDetailForReport(stockTransVofficeDTO.getStockTransActionId())).thenReturn(lstDetail);
        when(debitRequestService.findOne(anyLong())).thenReturn(debitRequestDTO);
        when(debitRequestDTO.getRequestObjectType()).thenReturn("1");
        when(staffDTO.getShopId()).thenReturn(1L);
        when(shopService.findOne(anyLong())).thenReturn(shopDTO);
        when(shopDTO.getProvince()).thenReturn("HN");
        when(shopDTO.getDistrict()).thenReturn("HH");
        when(areaService.findByCode(anyString())).thenReturn(province);
        when( areaService.findByCode(anyString())).thenReturn(district);
        Whitebox.invokeMethod(spyService, "doCreateStockDebitRequestTemplate",stockTransVofficeDTO, lstEmail);
    }

    @Test
    public void testDoCreateStockDebitRequestTemplate_3() throws  Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        List<String> lstEmail = Lists.newArrayList();
        StaffDTO staffDTO = mock(StaffDTO.class);
        DebitRequestReportDTO debitRequestReportDTO = mock(DebitRequestReportDTO.class);
        List<DebitRequestReportDTO> lstDetail = Lists.newArrayList(debitRequestReportDTO);
        DebitRequestDTO debitRequestDTO = mock(DebitRequestDTO.class);
        ShopDTO shopDTO = mock(ShopDTO.class);
        AreaDTO province = areaService.findByCode(shopDTO.getProvince());
        AreaDTO district = areaService.findByCode(shopDTO.getProvince() + shopDTO.getDistrict());

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        when(staffService.getStaffByStaffCode(anyString())).thenReturn(staffDTO);
        when(debitRequestDetailService.getLstDetailForReport(stockTransVofficeDTO.getStockTransActionId())).thenReturn(lstDetail);
        when(debitRequestService.findOne(anyLong())).thenReturn(debitRequestDTO);
        when(debitRequestDTO.getRequestObjectType()).thenReturn("2");
        when(staffDTO.getShopId()).thenReturn(1L);
        when(shopService.findOne(anyLong())).thenReturn(shopDTO);
        when(shopDTO.getProvince()).thenReturn("HN");
        when(shopDTO.getDistrict()).thenReturn("HH");
        when(areaService.findByCode(anyString())).thenReturn(province);
        when( areaService.findByCode(anyString())).thenReturn(district);
        PowerMockito.mockStatic(JasperReportUtils.class);
        when(JasperReportUtils.exportPdfByte(any(), anyString())).thenReturn( new byte[0]);
        Whitebox.invokeMethod(spyService, "doCreateStockDebitRequestTemplate",stockTransVofficeDTO, lstEmail);
    }

    @Test
    public void testDoCreateStockDebitRequestTemplate_4() throws  Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        List<String> lstEmail = Lists.newArrayList();
        StaffDTO staffDTO = mock(StaffDTO.class);
        DebitRequestReportDTO debitRequestReportDTO = mock(DebitRequestReportDTO.class);
        List<DebitRequestReportDTO> lstDetail = Lists.newArrayList(debitRequestReportDTO);
        DebitRequestDTO debitRequestDTO = mock(DebitRequestDTO.class);
        ShopDTO shopDTO = mock(ShopDTO.class);
        AreaDTO province = areaService.findByCode(shopDTO.getProvince());
        AreaDTO district = areaService.findByCode(shopDTO.getProvince() + shopDTO.getDistrict());

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        when(staffService.getStaffByStaffCode(anyString())).thenReturn(staffDTO);
        when(debitRequestDetailService.getLstDetailForReport(stockTransVofficeDTO.getStockTransActionId())).thenReturn(lstDetail);
        when(debitRequestService.findOne(anyLong())).thenReturn(debitRequestDTO);
        when(debitRequestDTO.getRequestObjectType()).thenReturn("2");
        when(staffDTO.getShopId()).thenReturn(1L);
        when(shopService.findOne(anyLong())).thenReturn(shopDTO);
        when(shopDTO.getProvince()).thenReturn("HN");
        when(shopDTO.getDistrict()).thenReturn("HH");
        when(areaService.findByCode(anyString())).thenReturn(province);
        when( areaService.findByCode(anyString())).thenReturn(district);
        PowerMockito.mockStatic(JasperReportUtils.class);
        when(JasperReportUtils.exportPdfByte(any(), anyString())).thenReturn( new byte[1]);
        Whitebox.invokeMethod(spyService, "doCreateStockDebitRequestTemplate",stockTransVofficeDTO, lstEmail);
    }

    @Test
    public void testDoCreateStockDebitRequestTemplate_5() throws  Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        List<String> lstEmail = Lists.newArrayList();
        StaffDTO staffDTO = mock(StaffDTO.class);
        DebitRequestReportDTO debitRequestReportDTO = mock(DebitRequestReportDTO.class);
        List<DebitRequestReportDTO> lstDetail = Lists.newArrayList(debitRequestReportDTO);
        DebitRequestDTO debitRequestDTO = mock(DebitRequestDTO.class);
        ShopDTO shopDTO = mock(ShopDTO.class);
        AreaDTO province = areaService.findByCode(shopDTO.getProvince());
        AreaDTO district = areaService.findByCode(shopDTO.getProvince() + shopDTO.getDistrict());

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        when(staffService.getStaffByStaffCode(anyString())).thenReturn(staffDTO);
        when(debitRequestDetailService.getLstDetailForReport(stockTransVofficeDTO.getStockTransActionId())).thenReturn(lstDetail);
        when(debitRequestService.findOne(anyLong())).thenReturn(debitRequestDTO);
        when(debitRequestDTO.getRequestObjectType()).thenReturn("2");
        when(staffDTO.getShopId()).thenReturn(1L);
        when(shopService.findOne(anyLong())).thenReturn(shopDTO);
        when(shopDTO.getProvince()).thenReturn("HN");
        when(shopDTO.getDistrict()).thenReturn("HH");
        when(areaService.findByCode(anyString())).thenReturn(province);
        when( areaService.findByCode(anyString())).thenReturn(district);
        Whitebox.invokeMethod(spyService, "doCreateStockDebitRequestTemplate",stockTransVofficeDTO, lstEmail);
    }


    @Test
    public void testDoCreateStockDebitRequestTemplate_6() throws  Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        List<String> lstEmail = Lists.newArrayList();
        StaffDTO staffDTO = mock(StaffDTO.class);
        DebitRequestReportDTO debitRequestReportDTO = mock(DebitRequestReportDTO.class);
        List<DebitRequestReportDTO> lstDetail = Lists.newArrayList(debitRequestReportDTO);
        DebitRequestDTO debitRequestDTO = mock(DebitRequestDTO.class);
        ShopDTO shopDTO = mock(ShopDTO.class);
        AreaDTO province = areaService.findByCode(shopDTO.getProvince());
        AreaDTO district = areaService.findByCode(shopDTO.getProvince() + shopDTO.getDistrict());

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        when(staffService.getStaffByStaffCode(anyString())).thenReturn(staffDTO);
        when(debitRequestDetailService.getLstDetailForReport(stockTransVofficeDTO.getStockTransActionId())).thenReturn(lstDetail);
        when(debitRequestService.findOne(anyLong())).thenReturn(debitRequestDTO);
        when(debitRequestDTO.getRequestObjectType()).thenReturn("2");
        when(staffDTO.getShopId()).thenReturn(1L);
        when(shopService.findOne(anyLong())).thenReturn(shopDTO);
        when(shopDTO.getProvince()).thenReturn("HN");
        when(shopDTO.getDistrict()).thenReturn("HH");
        when(areaService.findByCode(anyString())).thenReturn(province);
        when( areaService.findByCode(anyString())).thenReturn(district);
        PowerMockito.mockStatic(JasperReportUtils.class);
        when(JasperReportUtils.exportPdfByte(any(), anyString())).thenReturn(null);
        Whitebox.invokeMethod(spyService, "doCreateStockDebitRequestTemplate",stockTransVofficeDTO, lstEmail);
    }
    /** -------------------------------------------------------------------- **/
    /** --- Test for StockTransVofficeServiceImpl.doCreateStockDeviceTemplate method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testDoCreateStockDeviceTemplate_1() throws  Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        stockTransVofficeDTO.setStockTransActionId(1L);
        List<String> lstEmail = Lists.newArrayList();

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        when(stockTransSerialService.getLstStockDevice(anyLong())).thenReturn(null);
        Whitebox.invokeMethod(spyService, "doCreateStockDeviceTemplate",stockTransVofficeDTO, lstEmail);
    }

    @Test
    public void testDoCreateStockDeviceTemplate_2() throws  Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        stockTransVofficeDTO.setStockTransActionId(1L);
        List<String> lstEmail = Lists.newArrayList();
        StockDeviceDTO stockDeviceDTO = mock(StockDeviceDTO.class);

        List<StockDeviceDTO> lstDetail = Lists.newArrayList(stockDeviceDTO);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        when(stockTransSerialService.getLstStockDevice(anyLong())).thenReturn(lstDetail);
        when(stockDeviceDTO.getRequestShopId()).thenReturn(1L);
        when(stockDeviceDTO.getRequestShopName()).thenReturn("nm");
        when(commonService.getProvince(anyLong())).thenReturn("");
        PowerMockito.mockStatic(JasperReportUtils.class);
        when(JasperReportUtils.exportPdfByte(any(), anyString())).thenReturn( new byte[0]);
        Whitebox.invokeMethod(spyService, "doCreateStockDeviceTemplate",stockTransVofficeDTO, lstEmail);
    }

    @Test
    public void testDoCreateStockDeviceTemplate_3() throws  Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        stockTransVofficeDTO.setStockTransActionId(1L);
        List<String> lstEmail = Lists.newArrayList();
        StockDeviceDTO stockDeviceDTO = mock(StockDeviceDTO.class);

        List<StockDeviceDTO> lstDetail = Lists.newArrayList(stockDeviceDTO);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        when(stockTransSerialService.getLstStockDevice(anyLong())).thenReturn(lstDetail);
        when(stockDeviceDTO.getRequestShopId()).thenReturn(1L);
        when(stockDeviceDTO.getRequestShopName()).thenReturn("nm");
        when(commonService.getProvince(anyLong())).thenReturn("");
        PowerMockito.mockStatic(JasperReportUtils.class);
        when(JasperReportUtils.exportPdfByte(any(), anyString())).thenReturn( new byte[1]);
        Whitebox.invokeMethod(spyService, "doCreateStockDeviceTemplate",stockTransVofficeDTO, lstEmail);
    }

    @Test
    public void testDoCreateStockDeviceTemplate_4() throws  Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        stockTransVofficeDTO.setStockTransActionId(1L);
        List<String> lstEmail = Lists.newArrayList();
        StockDeviceDTO stockDeviceDTO = mock(StockDeviceDTO.class);

        List<StockDeviceDTO> lstDetail = Lists.newArrayList(stockDeviceDTO);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        when(stockTransSerialService.getLstStockDevice(anyLong())).thenReturn(lstDetail);
        when(stockDeviceDTO.getRequestShopId()).thenReturn(1L);
        when(stockDeviceDTO.getRequestShopName()).thenReturn("nm");
        when(commonService.getProvince(anyLong())).thenReturn("HN");
        Whitebox.invokeMethod(spyService, "doCreateStockDeviceTemplate",stockTransVofficeDTO, lstEmail);
    }

    @Test
    public void testDoCreateStockDeviceTemplate_5() throws  Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        stockTransVofficeDTO.setStockTransActionId(1L);
        List<String> lstEmail = Lists.newArrayList();
        StockDeviceDTO stockDeviceDTO = mock(StockDeviceDTO.class);

        List<StockDeviceDTO> lstDetail = Lists.newArrayList(stockDeviceDTO);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        when(stockTransSerialService.getLstStockDevice(anyLong())).thenReturn(lstDetail);
        when(stockDeviceDTO.getRequestShopId()).thenReturn(1L);
        when(stockDeviceDTO.getRequestShopName()).thenReturn("nm");
        when(commonService.getProvince(anyLong())).thenReturn("");
        PowerMockito.mockStatic(JasperReportUtils.class);
        when(JasperReportUtils.exportPdfByte(any(), anyString())).thenReturn( new byte[0]);
        Whitebox.invokeMethod(spyService, "doCreateStockDeviceTemplate",stockTransVofficeDTO, lstEmail);
    }

    @Test
    public void testDoCreateStockDeviceTemplate_6() throws  Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        stockTransVofficeDTO.setStockTransActionId(1L);
        List<String> lstEmail = Lists.newArrayList();
        StockDeviceDTO stockDeviceDTO = mock(StockDeviceDTO.class);

        List<StockDeviceDTO> lstDetail = Lists.newArrayList(stockDeviceDTO);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        when(stockTransSerialService.getLstStockDevice(anyLong())).thenReturn(lstDetail);
        when(stockDeviceDTO.getRequestShopId()).thenReturn(1L);
        when(stockDeviceDTO.getRequestShopName()).thenReturn("nm");
        when(commonService.getProvince(anyLong())).thenReturn("");
        PowerMockito.mockStatic(JasperReportUtils.class);
        when(JasperReportUtils.exportPdfByte(any(), anyString())).thenReturn( null);
        Whitebox.invokeMethod(spyService, "doCreateStockDeviceTemplate",stockTransVofficeDTO, lstEmail);
    }
    /** -------------------------------------------------------------------- **/
    /** --- Test for StockTransVofficeServiceImpl.doCreateStockDeliver method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testDoCreateStockDeliver_1() throws  Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        stockTransVofficeDTO.setStockTransActionId(1L);
        List<String> lstEmail = Lists.newArrayList();
        StockDeliverDTO stockDeliverDTO = mock(StockDeliverDTO.class);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        when(stockDeliverService.findOne(anyLong())).thenReturn(null);
        Whitebox.invokeMethod(spyService, "doCreateStockDeliver",stockTransVofficeDTO, lstEmail);
    }

    @Test
    public void testDoCreateStockDeliver_2() throws  Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        stockTransVofficeDTO.setStockTransActionId(1L);
        List<String> lstEmail = Lists.newArrayList();
        StockDeliverDTO stockDeliverDTO = mock(StockDeliverDTO.class);
        StockDeliverDetailDTO stockDeliverDetailDTO = mock(StockDeliverDetailDTO.class);
        List<StockDeliverDetailDTO> lstDetail = Lists.newArrayList(stockDeliverDetailDTO);
        ShopDTO shopDTO = mock(ShopDTO.class);
        StaffDTO staffDTO = mock(StaffDTO.class);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        when(stockDeliverService.findOne(anyLong())).thenReturn(stockDeliverDTO);
        when( stockDeliverDetailService.getLstDetailByStockDeliverId(anyLong())).thenReturn(null);
        when( shopService.findOne(anyLong())).thenReturn(shopDTO);
        when( shopDTO.getAddress()).thenReturn("Namdinh");
        when( stockDeliverDTO.getOldStaffId()).thenReturn(1L);
        when( staffService.findOne(anyLong())).thenReturn(staffDTO);
        when( staffDTO.getEmail()).thenReturn("mail");
        when( staffDTO.getTel()).thenReturn("00-01");
        PowerMockito.mockStatic(JasperReportUtils.class);
        when(JasperReportUtils.exportPdfByte(any(), anyString())).thenReturn( new byte[0]);
        Whitebox.invokeMethod(spyService, "doCreateStockDeliver",stockTransVofficeDTO, lstEmail);
    }

    @Test
    public void testDoCreateStockDeliver_3() throws  Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        stockTransVofficeDTO.setStockTransActionId(1L);
        List<String> lstEmail = Lists.newArrayList();
        StockDeliverDTO stockDeliverDTO = mock(StockDeliverDTO.class);
        StockDeliverDetailDTO stockDeliverDetailDTO = mock(StockDeliverDetailDTO.class);
        List<StockDeliverDetailDTO> lstDetail = Lists.newArrayList(stockDeliverDetailDTO);
        ShopDTO shopDTO = mock(ShopDTO.class);
        StaffDTO staffDTO = mock(StaffDTO.class);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        when(stockDeliverService.findOne(anyLong())).thenReturn(stockDeliverDTO);
        when( stockDeliverDetailService.getLstDetailByStockDeliverId(anyLong())).thenReturn(null);
        when( shopService.findOne(anyLong())).thenReturn(shopDTO);
        when( shopDTO.getAddress()).thenReturn("Namdinh");
        when( stockDeliverDTO.getOldStaffId()).thenReturn(1L);
        when( staffService.findOne(anyLong())).thenReturn(staffDTO);
        when( staffDTO.getEmail()).thenReturn("mail");
        when( staffDTO.getTel()).thenReturn("00-01");
        PowerMockito.mockStatic(JasperReportUtils.class);
        when(JasperReportUtils.exportPdfByte(any(), anyString())).thenReturn( new byte[1]);
        Whitebox.invokeMethod(spyService, "doCreateStockDeliver",stockTransVofficeDTO, lstEmail);
    }

    @Test
    public void testDoCreateStockDeliver_4() throws  Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        stockTransVofficeDTO.setStockTransActionId(1L);
        List<String> lstEmail = Lists.newArrayList();
        StockDeliverDTO stockDeliverDTO = mock(StockDeliverDTO.class);
        StockDeliverDetailDTO stockDeliverDetailDTO = mock(StockDeliverDetailDTO.class);
        List<StockDeliverDetailDTO> lstDetail = Lists.newArrayList(stockDeliverDetailDTO);
        ShopDTO shopDTO = mock(ShopDTO.class);
        StaffDTO staffDTO = mock(StaffDTO.class);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        when(stockDeliverService.findOne(anyLong())).thenReturn(stockDeliverDTO);
        when( stockDeliverDetailService.getLstDetailByStockDeliverId(anyLong())).thenReturn(null);
        when( shopService.findOne(anyLong())).thenReturn(shopDTO);
        when( shopDTO.getAddress()).thenReturn("Namdinh");
        when( stockDeliverDTO.getOldStaffId()).thenReturn(1L);
        when( staffService.findOne(anyLong())).thenReturn(staffDTO);
        when( staffDTO.getEmail()).thenReturn("mail");
        when( staffDTO.getTel()).thenReturn("00-01");
        Whitebox.invokeMethod(spyService, "doCreateStockDeliver",stockTransVofficeDTO, lstEmail);
    }

    @Test
    public void testDoCreateStockDeliver_5() throws  Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        stockTransVofficeDTO.setStockTransActionId(1L);
        List<String> lstEmail = Lists.newArrayList();
        StockDeliverDTO stockDeliverDTO = mock(StockDeliverDTO.class);
        StockDeliverDetailDTO stockDeliverDetailDTO = mock(StockDeliverDetailDTO.class);
        List<StockDeliverDetailDTO> lstDetail = Lists.newArrayList(stockDeliverDetailDTO);
        ShopDTO shopDTO = mock(ShopDTO.class);
        StaffDTO staffDTO = mock(StaffDTO.class);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        when(stockDeliverService.findOne(anyLong())).thenReturn(stockDeliverDTO);
        when( stockDeliverDetailService.getLstDetailByStockDeliverId(anyLong())).thenReturn(lstDetail);
        when( shopService.findOne(anyLong())).thenReturn(shopDTO);
        when( shopDTO.getAddress()).thenReturn("Namdinh");
        when( stockDeliverDTO.getOldStaffId()).thenReturn(1L);
        when( staffService.findOne(anyLong())).thenReturn(staffDTO);
        when( staffDTO.getEmail()).thenReturn("mail");
        when( staffDTO.getTel()).thenReturn("00-01");
        PowerMockito.mockStatic(JasperReportUtils.class);
        when(JasperReportUtils.exportPdfByte(any(), anyString())).thenReturn(null);
        Whitebox.invokeMethod(spyService, "doCreateStockDeliver",stockTransVofficeDTO, lstEmail);
    }
    /** -------------------------------------------------------------------- **/
    /** --- Test for StockTransVofficeServiceImpl.doCreateDOATransferFileAttach method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testDoCreateDOATransferFileAttach_1() throws  Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        List<String> lstStaff = Lists.newArrayList();

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        when(stockTransService.findStockTransByActionId(anyLong())).thenReturn(null);
        Whitebox.invokeMethod(spyService, "doCreateDOATransferFileAttach",stockTransVofficeDTO, lstStaff);
    }

    @Test
    public void testDoCreateDOATransferFileAttach_2() throws  Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        List<String> lstStaff = Lists.newArrayList();
        StockTransDTO stockTransDTO = new StockTransDTO();
        DOATransferDTO doaTransferDTO = mock(DOATransferDTO.class);
        List<DOATransferDTO> lstContent = Lists.newArrayList(doaTransferDTO);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        when(stockTransService.findStockTransByActionId(anyLong())).thenReturn(stockTransDTO);
        when(stockTransService.getContentFileDOA(anyLong())).thenReturn(null);
        Whitebox.invokeMethod(spyService, "doCreateDOATransferFileAttach",stockTransVofficeDTO, lstStaff);
    }

    @Test
    public void testDoCreateDOATransferFileAttach_3() throws  Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        List<String> lstStaff = Lists.newArrayList();
        StockTransDTO stockTransDTO = new StockTransDTO();
        DOATransferDTO doaTransferDTO = mock(DOATransferDTO.class);
        List<DOATransferDTO> lstContent = Lists.newArrayList(doaTransferDTO);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        when(stockTransService.findStockTransByActionId(anyLong())).thenReturn(stockTransDTO);
        when(stockTransService.getContentFileDOA(anyLong())).thenReturn(lstContent);
        when(doaTransferDTO.getOwnerType()).thenReturn(1L);
        Whitebox.invokeMethod(spyService, "doCreateDOATransferFileAttach",stockTransVofficeDTO, lstStaff);
    }

    @Test
    public void testDoCreateDOATransferFileAttach_4() throws  Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        List<String> lstStaff = Lists.newArrayList();
        StockTransDTO stockTransDTO = new StockTransDTO();
        DOATransferDTO doaTransferDTO = mock(DOATransferDTO.class);
        List<DOATransferDTO> lstContent = Lists.newArrayList(doaTransferDTO);
        ShopDTO shopDTO = mock(ShopDTO.class);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        when(stockTransService.findStockTransByActionId(anyLong())).thenReturn(stockTransDTO);
        when(stockTransService.getContentFileDOA(anyLong())).thenReturn(lstContent);
        when(doaTransferDTO.getOwnerType()).thenReturn(2L);
        when(shopService.findOne(anyLong())).thenReturn(null);
        Whitebox.invokeMethod(spyService, "doCreateDOATransferFileAttach",stockTransVofficeDTO, lstStaff);
    }

    @Test
    public void testDoCreateDOATransferFileAttach_5() throws  Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        List<String> lstStaff = Lists.newArrayList();
        StockTransDTO stockTransDTO = new StockTransDTO();
        DOATransferDTO doaTransferDTO = mock(DOATransferDTO.class);
        List<DOATransferDTO> lstContent = Lists.newArrayList(doaTransferDTO);
        ShopDTO shopDTO = mock(ShopDTO.class);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        when(stockTransService.findStockTransByActionId(anyLong())).thenReturn(stockTransDTO);
        when(stockTransService.getContentFileDOA(anyLong())).thenReturn(lstContent);
        when(doaTransferDTO.getOwnerType()).thenReturn(2L);
        when(doaTransferDTO.getCreateDate()).thenReturn(new Date());
        when(shopService.findOne(anyLong())).thenReturn(shopDTO);
        when(commonService.getProvince(anyLong())).thenReturn("");
        PowerMockito.mockStatic(JasperReportUtils.class);
        when(JasperReportUtils.exportPdfByte(any(), anyString())).thenReturn( new byte[0]);
        Whitebox.invokeMethod(spyService, "doCreateDOATransferFileAttach",stockTransVofficeDTO, lstStaff);
    }

    @Test
    public void testDoCreateDOATransferFileAttach_6() throws  Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        List<String> lstStaff = Lists.newArrayList();
        StockTransDTO stockTransDTO = new StockTransDTO();
        DOATransferDTO doaTransferDTO = mock(DOATransferDTO.class);
        List<DOATransferDTO> lstContent = Lists.newArrayList(doaTransferDTO);
        ShopDTO shopDTO = mock(ShopDTO.class);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        when(stockTransService.findStockTransByActionId(anyLong())).thenReturn(stockTransDTO);
        when(stockTransService.getContentFileDOA(anyLong())).thenReturn(lstContent);
        when(doaTransferDTO.getOwnerType()).thenReturn(2L);
        when(doaTransferDTO.getCreateDate()).thenReturn(new Date());
        when(shopService.findOne(anyLong())).thenReturn(shopDTO);
        when(commonService.getProvince(anyLong())).thenReturn("");
        PowerMockito.mockStatic(JasperReportUtils.class);
        when(JasperReportUtils.exportPdfByte(any(), anyString())).thenReturn( new byte[1]);
        Whitebox.invokeMethod(spyService, "doCreateDOATransferFileAttach",stockTransVofficeDTO, lstStaff);
    }

    @Test
    public void testDoCreateDOATransferFileAttach_7() throws  Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        List<String> lstStaff = Lists.newArrayList();
        StockTransDTO stockTransDTO = new StockTransDTO();
        DOATransferDTO doaTransferDTO = mock(DOATransferDTO.class);
        List<DOATransferDTO> lstContent = Lists.newArrayList(doaTransferDTO);
        ShopDTO shopDTO = mock(ShopDTO.class);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        when(stockTransService.findStockTransByActionId(anyLong())).thenReturn(stockTransDTO);
        when(stockTransService.getContentFileDOA(anyLong())).thenReturn(lstContent);
        when(doaTransferDTO.getOwnerType()).thenReturn(2L);
        when(doaTransferDTO.getCreateDate()).thenReturn(new Date());
        when(shopService.findOne(anyLong())).thenReturn(shopDTO);
        when(commonService.getProvince(anyLong())).thenReturn("");
        Whitebox.invokeMethod(spyService, "doCreateDOATransferFileAttach",stockTransVofficeDTO, lstStaff);
    }

    @Test
    public void testDoCreateDOATransferFileAttach_8() throws  Exception {
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        List<String> lstStaff = Lists.newArrayList();
        StockTransDTO stockTransDTO = new StockTransDTO();
        DOATransferDTO doaTransferDTO = mock(DOATransferDTO.class);
        List<DOATransferDTO> lstContent = Lists.newArrayList(doaTransferDTO);
        ShopDTO shopDTO = mock(ShopDTO.class);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        when(stockTransService.findStockTransByActionId(anyLong())).thenReturn(stockTransDTO);
        when(stockTransService.getContentFileDOA(anyLong())).thenReturn(lstContent);
        when(doaTransferDTO.getOwnerType()).thenReturn(2L);
        when(doaTransferDTO.getCreateDate()).thenReturn(new Date());
        when(shopService.findOne(anyLong())).thenReturn(shopDTO);
        when(commonService.getProvince(anyLong())).thenReturn("ND");
        PowerMockito.mockStatic(JasperReportUtils.class);
        when(JasperReportUtils.exportPdfByte(any(), anyString())).thenReturn(null);
        Whitebox.invokeMethod(spyService, "doCreateDOATransferFileAttach",stockTransVofficeDTO, lstStaff);
    }
    /** -------------------------------------------------------------------- **/
    /** --- Test for StockTransVofficeServiceImpl.getTemplateName method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testGetTemplateName_1() throws  Exception {
        String prefixTemplate = "";
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        vStockTransDTO.setActionCode("action");
        vStockTransDTO.setFromOwnerType(1L);
        vStockTransDTO.setFromOwnerID(7282L);
        vStockTransDTO.setStockTransStatus("4");

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        Whitebox.invokeMethod(spyService, "getTemplateName",vStockTransDTO, prefixTemplate);
    }

    @Test
    public void testGetTemplateName_2() throws  Exception {
        String prefixTemplate = "_TTH_CN";
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        vStockTransDTO.setActionCode("action");
        vStockTransDTO.setFromOwnerType(2L);
        vStockTransDTO.setFromOwnerID(2L);
        vStockTransDTO.setStockTransStatus("1");

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        Whitebox.invokeMethod(spyService, "getTemplateName",vStockTransDTO, prefixTemplate);
    }

    @Test
    public void testGetTemplateName_3() throws  Exception {
        String prefixTemplate = "_CN";
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        vStockTransDTO.setActionCode("action");
        vStockTransDTO.setFromOwnerType(2L);
        vStockTransDTO.setFromOwnerID(2L);
        vStockTransDTO.setStockTransStatus("2");
        vStockTransDTO.setStockTransType(12L);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        Whitebox.invokeMethod(spyService, "getTemplateName",vStockTransDTO, prefixTemplate);
    }

    @Test
    public void testGetTemplateName_4() throws  Exception {
        String prefixTemplate = "_CN";
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        vStockTransDTO.setActionCode("action");
        vStockTransDTO.setFromOwnerType(2L);
        vStockTransDTO.setFromOwnerID(2L);
        vStockTransDTO.setStockTransStatus("4");
        vStockTransDTO.setStockTransType(11L);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        Whitebox.invokeMethod(spyService, "getTemplateName",vStockTransDTO, prefixTemplate);
    }

    @Test
    public void testGetTemplateName_5() throws  Exception {
        String prefixTemplate = "_CN";
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        vStockTransDTO.setActionCode("action");
        vStockTransDTO.setFromOwnerType(2L);
        vStockTransDTO.setFromOwnerID(2L);
        vStockTransDTO.setStockTransStatus("5");
        vStockTransDTO.setStockTransType(3L);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        Whitebox.invokeMethod(spyService, "getTemplateName",vStockTransDTO, prefixTemplate);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for StockTransVofficeServiceImpl.getFileName method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testGetFileName_1() throws  Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        vStockTransDTO.setActionCode("action");

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        Whitebox.invokeMethod(spyService, "getFileName",vStockTransDTO);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for StockTransVofficeServiceImpl.getSubPrefix method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testGetSubPrefix_1() throws  Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        vStockTransDTO.setFromOwnerType(1L);
        vStockTransDTO.setActionType("4");

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        Whitebox.invokeMethod(spyService, "getSubPrefix",vStockTransDTO);
    }

    @Test
    public void testGetSubPrefix_2() throws  Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        vStockTransDTO.setFromOwnerType(2L);
        vStockTransDTO.setActionType("3");

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        Whitebox.invokeMethod(spyService, "getSubPrefix",vStockTransDTO);
    }

    @Test
    public void testGetSubPrefix_3() throws  Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        vStockTransDTO.setFromOwnerType(3L);
        vStockTransDTO.setActionType("5");

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        Whitebox.invokeMethod(spyService, "getSubPrefix",vStockTransDTO);
    }

    @Test
    public void testGetSubPrefix_4() throws  Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        vStockTransDTO.setFromOwnerType(3L);
        vStockTransDTO.setActionType("6");

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        Whitebox.invokeMethod(spyService, "getSubPrefix",vStockTransDTO);
    }
    /** -------------------------------------------------------------------- **/
    /** --- Test for StockTransVofficeServiceImpl.getPrefix method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testGetPrefix_1() throws  Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        vStockTransDTO.setActionType("2");
        vStockTransDTO.setStockTransType(6L);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        Whitebox.invokeMethod(spyService, "getPrefix",vStockTransDTO);
    }

    @Test
    public void testGetPrefix_2() throws  Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        vStockTransDTO.setActionType("2");
        vStockTransDTO.setStockTransType(5L);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        Whitebox.invokeMethod(spyService, "getPrefix",vStockTransDTO);
    }

    @Test
    public void testGetPrefix_3() throws  Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        vStockTransDTO.setActionType("5");
        vStockTransDTO.setStockTransType(5L);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        Whitebox.invokeMethod(spyService, "getPrefix",vStockTransDTO);
    }

    @Test
    public void testGetPrefix_4() throws  Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        vStockTransDTO.setActionType("4");
        vStockTransDTO.setStockTransType(5L);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        Whitebox.invokeMethod(spyService, "getPrefix",vStockTransDTO);
    }

    @Test
    public void testGetPrefix_5() throws  Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        vStockTransDTO.setActionType("2");

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        Whitebox.invokeMethod(spyService, "getPrefix",vStockTransDTO);
    }

    @Test
    public void testGetPrefix_6() throws  Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        vStockTransDTO.setActionType("1");
        vStockTransDTO.setStockTransType(5L);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        Whitebox.invokeMethod(spyService, "getPrefix",vStockTransDTO);
    }

    @Test
    public void testGetPrefix_7() throws  Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        vStockTransDTO.setActionType("1");
        vStockTransDTO.setStockTransType(5L);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        Whitebox.invokeMethod(spyService, "getPrefix",vStockTransDTO);
    }

    @Test
    public void testGetPrefix_8() throws  Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        vStockTransDTO.setActionType("2");

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        Whitebox.invokeMethod(spyService, "getPrefix",vStockTransDTO);
    }
    /** -------------------------------------------------------------------- **/
    /** --- Test for StockTransVofficeServiceImpl.getFileAttachName method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testGetFileAttachName_1() throws  Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        vStockTransDTO.setActionCode("");

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        Whitebox.invokeMethod(spyService, "getFileAttachName",vStockTransDTO);
    }

    @Test
    public void testGetFileAttachName_2() throws  Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        vStockTransDTO.setActionCode("404");

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        Whitebox.invokeMethod(spyService, "getFileAttachName",vStockTransDTO);
    }
    /** -------------------------------------------------------------------- **/
    /** --- Test for StockTransVofficeServiceImpl.getReceiptNO method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testGetReceiptNO_1() throws  Exception {
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setActionCodeShop("");
        stockTransActionDTO.setActionCode("PN PX");
        stockTransActionDTO.setStatus("5");

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        Whitebox.invokeMethod(spyService, "getReceiptNO",stockTransActionDTO);
    }

    @Test
    public void testGetReceiptNO_2() throws  Exception {
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setActionCodeShop("");
        stockTransActionDTO.setActionCode("PN PX");
        stockTransActionDTO.setStatus("1");

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        Whitebox.invokeMethod(spyService, "getReceiptNO",stockTransActionDTO);
    }

    @Test
    public void testGetReceiptNO_3() throws  Exception {
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setActionCodeShop("as");
        stockTransActionDTO.setActionCode("PN PX");
        stockTransActionDTO.setStatus("4");

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        Whitebox.invokeMethod(spyService, "getReceiptNO",stockTransActionDTO);
    }

    @Test
    public void testGetReceiptNO_4() throws  Exception {
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setActionCodeShop("");
        stockTransActionDTO.setActionCode("PN PX");
        stockTransActionDTO.setStatus("1");

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        Whitebox.invokeMethod(spyService, "getReceiptNO",stockTransActionDTO);
    }
/** -------------------------------------------------------------------- **/
    /** --- Test for StockTransVofficeServiceImpl.getData method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testGetData_1() throws  Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        vStockTransDTO.setFromOwnerType(4L);
        vStockTransDTO.setStockTransID(1L);
        PartnerContractDTO partnerContractDTO = mock(PartnerContractDTO.class);

        StockTransFullDTO stockTransFullDTO = mock(StockTransFullDTO.class);
        List<StockTransFullDTO> stockTransFullDTOs = Lists.newArrayList(stockTransFullDTO);


        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        when(partnerContractService.findByStockTransID(anyLong())).thenReturn(partnerContractDTO);
        when(stockTransFullDTO.getProdOfferName()).thenReturn("nm");
        when(stockTransFullDTO.getUnit()).thenReturn("nm");
        spyService.getData(stockTransFullDTOs ,vStockTransDTO);
    }

    @Test
    public void testGetData_2() throws  Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        vStockTransDTO.setFromOwnerType(4L);
        vStockTransDTO.setStockTransID(1L);
        PartnerContractDTO partnerContractDTO = mock(PartnerContractDTO.class);

        StockTransFullDTO stockTransFullDTO = mock(StockTransFullDTO.class);
        List<StockTransFullDTO> stockTransFullDTOs = Lists.newArrayList();


        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        when(partnerContractService.findByStockTransID(anyLong())).thenReturn(partnerContractDTO);
        when(stockTransFullDTO.getProdOfferName()).thenReturn("nm");
        when(stockTransFullDTO.getUnit()).thenReturn("nm");
        spyService.getData(stockTransFullDTOs ,vStockTransDTO);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for StockTransVofficeServiceImpl.initContractProperties method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testInitContractProperties_1() throws  Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        vStockTransDTO.setFromOwnerType(4L);
        vStockTransDTO.setStockTransID(1L);
        PartnerContractDTO partnerContractDTO = mock(PartnerContractDTO.class);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        when(partnerContractService.findByStockTransID(anyLong())).thenReturn(partnerContractDTO);
        spyService.initContractProperties(new HashMap(), vStockTransDTO);
    }

    @Test
    public void testInitContractProperties_2() throws  Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        vStockTransDTO.setFromOwnerType(4L);
        vStockTransDTO.setStockTransID(1L);
        PartnerContractDTO partnerContractDTO = mock(PartnerContractDTO.class);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        when(partnerContractService.findByStockTransID(anyLong())).thenReturn(partnerContractDTO);
        spyService.initContractProperties(new HashMap(), null);
    }

    @Test
    public void testInitContractProperties_3() throws  Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        vStockTransDTO.setFromOwnerType(1L);
        vStockTransDTO.setStockTransID(1L);
        PartnerContractDTO partnerContractDTO = mock(PartnerContractDTO.class);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        when(partnerContractService.findByStockTransID(anyLong())).thenReturn(partnerContractDTO);
        spyService.initContractProperties(new HashMap(), vStockTransDTO);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for StockTransVofficeServiceImpl.updateVofficeDeliver method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testUpdateVofficeDeliver_1() throws  Exception {
        stockTransVofficeService.updateVofficeDeliver(new StockTransVoffice());
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for StockTransVofficeServiceImpl.updateVofficeDevice method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testUpdateVofficeDevice_1() throws  Exception {
        stockTransVofficeService.updateVofficeDevice(new StockTransVoffice());
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for StockTransVofficeServiceImpl.updateVofficeDebit method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testUpdateVofficeDebit_1() throws  Exception {
        stockTransVofficeService.updateVofficeDebit(new StockTransVoffice());
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for StockTransVofficeServiceImpl.getOwnerName method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testGetOwnerName_1() throws  Exception {
        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        Whitebox.invokeMethod(spyService, "getOwnerName",1L,1L);
    }

    @Test
    public void testGetOwnerName_2() throws  Exception {
        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        when(commonService.getOwnerName(anyString(), anyString())).thenThrow(new Exception());
        Whitebox.invokeMethod(spyService, "getOwnerName",1L,1L);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for StockTransVofficeServiceImpl.getAddress method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testGetAddress_1() throws  Exception {
        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        Whitebox.invokeMethod(spyService, "getAddress",1L,1L);
    }

    @Test
    public void testGetAddress_2() throws  Exception {
        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        when(commonService.getOwnerAddress(anyString(), anyString())).thenThrow(new Exception());
        Whitebox.invokeMethod(spyService, "getAddress",1L,1L);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for StockTransVofficeServiceImpl.createReportConfig method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testCreateReportConfig_1() throws  Exception {
        VStockTransDTO vStockTransDTO= new VStockTransDTO();
        vStockTransDTO.setActionType("0");
        String prefixTemplate ="";
        StockTransActionDTO stockTransActionDTO = mock(StockTransActionDTO.class);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        when(stockTransActionService.findOne(anyLong())).thenReturn(stockTransActionDTO);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        Whitebox.invokeMethod(spyService, "createReportConfig",vStockTransDTO, prefixTemplate);
    }

    @Test
    public void testCreateReportConfig_2() throws  Exception {
        VStockTransDTO vStockTransDTO= new VStockTransDTO();
        vStockTransDTO.setActionType("1");
        String prefixTemplate ="";
        StockTransActionDTO stockTransActionDTO = mock(StockTransActionDTO.class);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        when(stockTransActionService.findOne(anyLong())).thenReturn(stockTransActionDTO);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        Whitebox.invokeMethod(spyService, "createReportConfig",vStockTransDTO, prefixTemplate);
    }

    @Test
    public void testCreateReportConfig_3() throws  Exception {
        VStockTransDTO vStockTransDTO= new VStockTransDTO();
        vStockTransDTO.setActionType("0");
        String prefixTemplate ="";
        StockTransActionDTO stockTransActionDTO = mock(StockTransActionDTO.class);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        when(stockTransActionService.findOne(anyLong())).thenReturn(null);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        Whitebox.invokeMethod(spyService, "createReportConfig",vStockTransDTO, prefixTemplate);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for StockTransVofficeServiceImpl.doCreateFileAttach method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testDoCreateFileAttach_1() throws  Exception {
        VStockTransDTO vStockTransDTO= new VStockTransDTO();
        vStockTransDTO.setActionType("0");
        StockTransVofficeDTO stockTransVofficeDTO = mock(StockTransVofficeDTO.class);
        List<String> lstStaff = Lists.newArrayList();
        StockTransActionDTO stockTransActionDTO = mock(StockTransActionDTO.class);
        ReportDTO reportDTO = new ReportDTO();
        StockTransFullDTO StockTransFullDTO = mock(StockTransFullDTO.class);
        List<StockTransFullDTO> data = Lists.newArrayList(StockTransFullDTO);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        when(stockTransActionService.findOne(anyLong())).thenReturn(stockTransActionDTO);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        PowerMockito.when( stockTransService.searchStockTransDetail(anyList())).thenReturn( data);
        PowerMockito.when( baseReportService.exportWithDataSource(any(), any())).thenReturn( new byte[0]);
        PowerMockito.doReturn(reportDTO).when(spyService, "createReportConfig", any(),anyString());
        Whitebox.invokeMethod(spyService, "doCreateFileAttach",vStockTransDTO, stockTransVofficeDTO, lstStaff);
    }

    @Test
    public void testDoCreateFileAttach_2() throws  Exception {
        VStockTransDTO vStockTransDTO= new VStockTransDTO();
        vStockTransDTO.setActionType("0");
        StockTransVofficeDTO stockTransVofficeDTO = mock(StockTransVofficeDTO.class);
        List<String> lstStaff = Lists.newArrayList();
        ReportDTO reportDTO = new ReportDTO();
        StockTransActionDTO stockTransActionDTO = mock(StockTransActionDTO.class);
        StockTransFullDTO StockTransFullDTO = mock(StockTransFullDTO.class);
        List<StockTransFullDTO> data = Lists.newArrayList(StockTransFullDTO);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        when(stockTransActionService.findOne(anyLong())).thenReturn(stockTransActionDTO);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        PowerMockito.when( stockTransService.searchStockTransDetail(anyList())).thenReturn( data);
        PowerMockito.when( baseReportService.exportWithDataSource(any(), any())).thenReturn( new byte[1]);
        PowerMockito.doReturn(reportDTO).when(spyService, "createReportConfig", any(),anyString());
        Whitebox.invokeMethod(spyService, "doCreateFileAttach",vStockTransDTO, stockTransVofficeDTO, lstStaff);
    }

    @Test
    public void testDoCreateFileAttach_3() throws  Exception {
        VStockTransDTO vStockTransDTO= new VStockTransDTO();
        vStockTransDTO.setActionType("0");
        StockTransVofficeDTO stockTransVofficeDTO = mock(StockTransVofficeDTO.class);
        List<String> lstStaff = Lists.newArrayList();
        StockTransActionDTO stockTransActionDTO = mock(StockTransActionDTO.class);
        ReportDTO reportDTO = new ReportDTO();
        StockTransFullDTO StockTransFullDTO = mock(StockTransFullDTO.class);
        List<StockTransFullDTO> data = Lists.newArrayList(StockTransFullDTO);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        when(stockTransActionService.findOne(anyLong())).thenReturn(stockTransActionDTO);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        PowerMockito.when( stockTransService.searchStockTransDetail(anyList())).thenReturn( data);
        PowerMockito.when( baseReportService.exportWithDataSource(any(), any())).thenReturn(null);
        PowerMockito.doReturn(reportDTO).when(spyService, "createReportConfig", any(),anyString());
        Whitebox.invokeMethod(spyService, "doCreateFileAttach",vStockTransDTO, stockTransVofficeDTO, lstStaff);
    }
    /** -------------------------------------------------------------------- **/
    /** --- Test for StockTransVofficeServiceImpl.layPhuLucSerial method --- **/
    /** -------------------------------------------------------------------- **/
    @Test(expected = Exception.class)
    public void testLayPhuLucSerial_1() throws  Exception {
        StockBalanceRequestDTO selectedStockBalanceRequestDTO = new StockBalanceRequestDTO();
        selectedStockBalanceRequestDTO.setOwnerId(1L);
        selectedStockBalanceRequestDTO.setStockRequestId(1L);
        StockBalanceDetailDTO stockBalanceDetailDTO = mock(StockBalanceDetailDTO.class);
        List<StockBalanceDetailDTO> listStockBalanceDetailDTO = Lists.newArrayList(stockBalanceDetailDTO);

        StockBalanceSerialDTO stockBalanceSerialDTO = mock(StockBalanceSerialDTO.class);
        List<StockBalanceSerialDTO> subs = Lists.newArrayList(stockBalanceSerialDTO);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        PowerMockito.doReturn(listStockBalanceDetailDTO).when(spyService, "getListStockBalanceDetail", anyLong());
        when(stockBalanceSerialService.getListStockBalanceSerialDTO(stockBalanceDetailDTO.getStockBalanceDetailId())).thenReturn(subs);
        Whitebox.invokeMethod(spyService, "layPhuLucSerial", selectedStockBalanceRequestDTO);
    }

    @Test
    public void testLayPhuLucSerial_2() throws  Exception {
        StockBalanceRequestDTO selectedStockBalanceRequestDTO = new StockBalanceRequestDTO();
        selectedStockBalanceRequestDTO.setOwnerId(1L);
        selectedStockBalanceRequestDTO.setStockRequestId(1L);
        StockBalanceDetailDTO stockBalanceDetailDTO = mock(StockBalanceDetailDTO.class);
        List<StockBalanceDetailDTO> listStockBalanceDetailDTO = Lists.newArrayList();

        StockBalanceSerialDTO stockBalanceSerialDTO = mock(StockBalanceSerialDTO.class);
        List<StockBalanceSerialDTO> subs = Lists.newArrayList(stockBalanceSerialDTO);

        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        PowerMockito.doReturn(listStockBalanceDetailDTO).when(spyService, "getListStockBalanceDetail", anyLong());
        when(stockBalanceSerialService.getListStockBalanceSerialDTO(stockBalanceDetailDTO.getStockBalanceDetailId())).thenReturn(subs);
        Whitebox.invokeMethod(spyService, "layPhuLucSerial", selectedStockBalanceRequestDTO);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for StockTransVofficeServiceImpl.buildVofficeDate method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testBuildVofficeDate_1() throws  Exception {
        Map bean = mock(Map.class);
        StockBalanceRequestDTO stockBalanceRequestDTO = new StockBalanceRequestDTO();

        when(commonService.getProvince(anyLong())).thenReturn("");
        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        Whitebox.invokeMethod(spyService, "buildVofficeDate", bean,stockBalanceRequestDTO);

    }
    @Test
    public void testBuildVofficeDate_2() throws  Exception {
        Map bean = mock(Map.class);
        StockBalanceRequestDTO stockBalanceRequestDTO = new StockBalanceRequestDTO();

        when(commonService.getProvince(anyLong())).thenReturn("HN");
        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        Whitebox.invokeMethod(spyService, "buildVofficeDate", bean,stockBalanceRequestDTO);

    }
    /** -------------------------------------------------------------------- **/
    /** --- Test for StockTransVofficeServiceImpl.doCreateStockBalanceFileAttach method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testDoCreateStockBalanceFileAttach_1() throws  Exception {
        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        List<String> lstStaff = Lists.newArrayList();
        StockBalanceDetailDTO stockBalanceDetailDTO = mock(StockBalanceDetailDTO.class);
        List<StockBalanceDetailDTO> listStockBalanceDetailDTO = Lists.newArrayList(stockBalanceDetailDTO);
        StaffDTO staffDTO = mock(StaffDTO.class) ;

        StockBalanceRequestDTO stockBalanceRequestDTO = mock(StockBalanceRequestDTO.class);
        List<StockBalanceRequestDTO> lstStockBalanceRequest = Lists.newArrayList(stockBalanceRequestDTO);

        PowerMockito.doReturn(listStockBalanceDetailDTO).when(spyService, "getListStockBalanceDetail", anyLong());
        when(staffService.getStaffByStaffCode(anyString())).thenReturn(staffDTO);
        when(stockBalanceRequestService.searchStockBalanceRequest(any())).thenReturn(lstStockBalanceRequest);
        PowerMockito.mockStatic(JasperReportUtils.class);
        when(JasperReportUtils.exportPdfByte(any(), anyString())).thenReturn( new byte[0]);

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        Whitebox.invokeMethod(spyService, "doCreateStockBalanceFileAttach", stockTransVofficeDTO,lstStaff);
    }

    @Test
    public void testDoCreateStockBalanceFileAttach_2() throws  Exception {
        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        List<String> lstStaff = Lists.newArrayList();
        StockBalanceDetailDTO stockBalanceDetailDTO = mock(StockBalanceDetailDTO.class);
        List<StockBalanceDetailDTO> listStockBalanceDetailDTO = Lists.newArrayList(stockBalanceDetailDTO);
        StaffDTO staffDTO = mock(StaffDTO.class) ;

        StockBalanceRequestDTO stockBalanceRequestDTO = mock(StockBalanceRequestDTO.class);
        List<StockBalanceRequestDTO> lstStockBalanceRequest = Lists.newArrayList(stockBalanceRequestDTO);

        PowerMockito.doReturn(listStockBalanceDetailDTO).when(spyService, "getListStockBalanceDetail", anyLong());
        when(staffService.getStaffByStaffCode(anyString())).thenReturn(null);
        when(stockBalanceRequestService.searchStockBalanceRequest(any())).thenReturn(lstStockBalanceRequest);
        PowerMockito.mockStatic(JasperReportUtils.class);
        when(JasperReportUtils.exportPdfByte(any(), anyString())).thenReturn( new byte[0]);

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        Whitebox.invokeMethod(spyService, "doCreateStockBalanceFileAttach", stockTransVofficeDTO,lstStaff);
    }

    @Test
    public void testDoCreateStockBalanceFileAttach_3() throws  Exception {
        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        List<String> lstStaff = Lists.newArrayList();
        StockBalanceDetailDTO stockBalanceDetailDTO = mock(StockBalanceDetailDTO.class);
        List<StockBalanceDetailDTO> listStockBalanceDetailDTO = Lists.newArrayList(stockBalanceDetailDTO);
        StaffDTO staffDTO = mock(StaffDTO.class) ;

        StockBalanceRequestDTO stockBalanceRequestDTO = mock(StockBalanceRequestDTO.class);
        List<StockBalanceRequestDTO> lstStockBalanceRequest = Lists.newArrayList(stockBalanceRequestDTO);

        PowerMockito.doReturn(listStockBalanceDetailDTO).when(spyService, "getListStockBalanceDetail", anyLong());
        PowerMockito.doReturn(new byte[1]).when(spyService, "layPhuLucSerial", any());
        when(staffService.getStaffByStaffCode(anyString())).thenReturn(staffDTO);
        when(stockBalanceRequestService.searchStockBalanceRequest(any())).thenReturn(lstStockBalanceRequest);
        PowerMockito.mockStatic(JasperReportUtils.class);
        when(JasperReportUtils.exportPdfByte(any(), anyString())).thenReturn( new byte[1]);

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        Whitebox.invokeMethod(spyService, "doCreateStockBalanceFileAttach", stockTransVofficeDTO,lstStaff);
    }

    @Test
    public void testDoCreateStockBalanceFileAttach_4() throws  Exception {
        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        List<String> lstStaff = Lists.newArrayList();
        StockBalanceDetailDTO stockBalanceDetailDTO = mock(StockBalanceDetailDTO.class);
        List<StockBalanceDetailDTO> listStockBalanceDetailDTO = Lists.newArrayList(stockBalanceDetailDTO);
        StaffDTO staffDTO = mock(StaffDTO.class) ;

        StockBalanceRequestDTO stockBalanceRequestDTO = mock(StockBalanceRequestDTO.class);
        List<StockBalanceRequestDTO> lstStockBalanceRequest = Lists.newArrayList(stockBalanceRequestDTO);

        PowerMockito.doReturn(listStockBalanceDetailDTO).when(spyService, "getListStockBalanceDetail", anyLong());
        PowerMockito.doReturn(new byte[1]).when(spyService, "layPhuLucSerial", any());
        when(staffService.getStaffByStaffCode(anyString())).thenReturn(staffDTO);
        when(stockBalanceRequestService.searchStockBalanceRequest(any())).thenReturn(lstStockBalanceRequest);

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        Whitebox.invokeMethod(spyService, "doCreateStockBalanceFileAttach", stockTransVofficeDTO,lstStaff);
    }

    @Test
    public void testDoCreateStockBalanceFileAttach_5() throws  Exception {
        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        List<String> lstStaff = Lists.newArrayList();
        StockBalanceDetailDTO stockBalanceDetailDTO = mock(StockBalanceDetailDTO.class);
        List<StockBalanceDetailDTO> listStockBalanceDetailDTO = Lists.newArrayList(stockBalanceDetailDTO);
        StaffDTO staffDTO = mock(StaffDTO.class) ;

        StockBalanceRequestDTO stockBalanceRequestDTO = mock(StockBalanceRequestDTO.class);
        List<StockBalanceRequestDTO> lstStockBalanceRequest = Lists.newArrayList(stockBalanceRequestDTO);

        PowerMockito.doReturn(listStockBalanceDetailDTO).when(spyService, "getListStockBalanceDetail", anyLong());
        when(staffService.getStaffByStaffCode(anyString())).thenReturn(staffDTO);
        when(stockBalanceRequestService.searchStockBalanceRequest(any())).thenReturn(lstStockBalanceRequest);
        PowerMockito.mockStatic(JasperReportUtils.class);
        when(JasperReportUtils.exportPdfByte(any(), anyString())).thenReturn( null);

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        Whitebox.invokeMethod(spyService, "doCreateStockBalanceFileAttach", stockTransVofficeDTO,lstStaff);
    }


    @Test
    public void testDoCreateStockBalanceFileAttach_6() throws  Exception {
        StockTransVofficeServiceImpl spyService = PowerMockito.spy(stockTransVofficeService);
        StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
        List<String> lstStaff = Lists.newArrayList();
        StockBalanceDetailDTO stockBalanceDetailDTO = mock(StockBalanceDetailDTO.class);
        List<StockBalanceDetailDTO> listStockBalanceDetailDTO = Lists.newArrayList(stockBalanceDetailDTO);
        StaffDTO staffDTO = mock(StaffDTO.class) ;

        StockBalanceRequestDTO stockBalanceRequestDTO = mock(StockBalanceRequestDTO.class);
        List<StockBalanceRequestDTO> lstStockBalanceRequest = Lists.newArrayList(stockBalanceRequestDTO);

        PowerMockito.doReturn(listStockBalanceDetailDTO).when(spyService, "getListStockBalanceDetail", anyLong());
        PowerMockito.doReturn(null).when(spyService, "layPhuLucSerial", any());
        when(staffService.getStaffByStaffCode(anyString())).thenReturn(staffDTO);
        when(stockBalanceRequestService.searchStockBalanceRequest(any())).thenReturn(lstStockBalanceRequest);

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        Whitebox.invokeMethod(spyService, "doCreateStockBalanceFileAttach", stockTransVofficeDTO,lstStaff);
    }


    static void setFinalStatic(Field field, Object newValue, final Object targetObject) throws Exception {
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(targetObject, newValue);
    }
}