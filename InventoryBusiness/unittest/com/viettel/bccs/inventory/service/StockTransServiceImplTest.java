package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.im1.service.BaseStockIm1Service;
import com.viettel.bccs.im1.service.StockTotalAuditIm1Service;
import com.viettel.bccs.im1.service.StockTotalIm1Service;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.*;
import com.viettel.bccs.inventory.repo.*;
import com.viettel.bccs.sale.service.SaleTransService;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.service.BaseServiceImpl;
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

import javax.persistence.EntityManager;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author DatLT
 * @date 11/10/2017
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({DbUtil.class, StockTransServiceImpl.class, BaseServiceImpl.class, StockTransService.class})
public class StockTransServiceImplTest {

    @InjectMocks
    StockTransServiceImpl stockTransService;

    @Mock
    private BaseMapper<StockTrans, StockTransDTO> mapper;

    @Mock
    private StockTransRepo repository;
    @Mock
    private final BaseMapper<StockTransDetail, StockTransDetailDTO> detailMapper = new BaseMapper<>(StockTransDetail.class, StockTransDetailDTO.class);
    @Mock
    private final BaseMapper<StockTransSerial, StockTransSerialDTO> serialMapper = new BaseMapper<>(StockTransSerial.class, StockTransSerialDTO.class);
    @Mock
    private final BaseMapper<StockTransAction, StockTransActionDTO> actionMapper = new BaseMapper<>(StockTransAction.class, StockTransActionDTO.class);
    @Mock
    private final BaseMapper<ProductOffering, ProductOfferingDTO> offerMapper = new BaseMapper<>(ProductOffering.class, ProductOfferingDTO.class);
    @Mock
    private final BaseMapper<StockTransExt, StockTransExtDTO> extMapper = new BaseMapper<>(StockTransExt.class, StockTransExtDTO.class);
    @Mock
    private StockTransActionRepo actionRepo;
    @Mock
    private StockTransDetailRepo stockTransDetailRepo;
    @Mock
    private StockTransActionRepo stockTransActionRepo;
    @Mock
    private StockTransSerialRepo stockTransSerialRepo;
    @Mock
    private StockTotalService stockTotalService;
    @Mock
    private StockTransOfflineService stockTransOfflineService;
    @Mock
    private StockTransDetailOfflineService stockTransDetailOfflineService;
    @Mock
    private StockTransSerialOfflineService stockTransSerialOfflineService;
    @Mock
    private StockTotalIm1Service stockTotalIm1Service;
    @Mock
    private BaseStockIm1Service baseStockIm1Service;
    @Mock
    private StockTotalAuditIm1Service stockTotalAuditIm1Service;
    @Mock
    private OptionSetValueService optionSetValueService;
    @Mock
    private InventoryService inventoryService;
    @Mock
    private StockTransExtRepo stockTransExtRepo;
    @Mock
    private EntityManager em;
    @Mock
    private SaleTransService saleTransService;
    @Mock
    private ReasonService reasonService;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }



    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransServiceImpl.count
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCount_1() throws Exception {
        List<FilterRequest> filters = Lists.newArrayList();
        Mockito.when(repository.count(any())).thenReturn(1L);
        Assert.assertEquals(1L, stockTransService.count(filters).longValue());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransServiceImpl.findOne
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindOne_1() throws  Exception {
        StockTransServiceImpl spyService = PowerMockito.spy( stockTransService);
        StockTrans stockTrans = mock(StockTrans.class);
        StockTransDTO stockTransDTO = new StockTransDTO();
        Mockito.when(repository.findOne(anyLong())).thenReturn(stockTrans);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(mapper.toDtoBean(stockTrans)).thenReturn(stockTransDTO);
        spyService.findOne(1L);
        Assert.assertNotNull(stockTransDTO);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransServiceImpl.findOneLock
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindOneLock_1() throws  Exception {
        StockTransServiceImpl spyService = PowerMockito.spy( stockTransService);
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTrans stockTrans = new StockTrans();
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(mapper.toDtoBean(stockTrans)).thenReturn(stockTransDTO);
        spyService.findOneLock(1L);
    }

    @Test(expected = Exception.class)
    public void testFindOneLock_2() throws  Exception {
        stockTransService.findOneLock(1L);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransServiceImpl.findAll
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindAll_1() throws  Exception {
        StockTransServiceImpl spyService = PowerMockito.spy( stockTransService);
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTrans stockTrans = mock(StockTrans.class);
        List<StockTransDTO> stockTransDTOList = Lists.newArrayList(stockTransDTO);
        List<StockTrans> stockTransList = Lists.newArrayList(stockTrans);
        Mockito.when(repository.findAll()).thenReturn(stockTransList);
        Mockito.when(mapper.toDtoBean(stockTransList)).thenReturn(stockTransDTOList);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findAll();
        Assert.assertNotNull(stockTransDTOList);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransServiceImpl.findByFilter
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindByFilter_1() throws  Exception {
        FilterRequest filterRequest = new FilterRequest();
        List<FilterRequest> filterRequestList = Lists.newArrayList(filterRequest);
        StockTransServiceImpl spyService = PowerMockito.spy( stockTransService);
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTrans stockTrans = mock(StockTrans.class);
        List<StockTransDTO> stockTransDTOList = Lists.newArrayList(stockTransDTO);
        List<StockTrans> stockTransList = Lists.newArrayList(stockTrans);
        Mockito.when(mapper.toDtoBean(stockTransList)).thenReturn(stockTransDTOList);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findByFilter(filterRequestList);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransServiceImpl.save
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testSave_1() throws  Exception {
        FilterRequest filterRequest = new FilterRequest();
        List<FilterRequest> filterRequestList = Lists.newArrayList(filterRequest);
        StockTransServiceImpl spyService = PowerMockito.spy( stockTransService);
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTrans stockTrans = mock(StockTrans.class);
        List<StockTransDTO> stockTransDTOList = Lists.newArrayList(stockTransDTO);
        List<StockTrans> stockTransList = Lists.newArrayList(stockTrans);
        Mockito.when(mapper.toDtoBean(stockTransList)).thenReturn(stockTransDTOList);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);

        spyService.save(stockTransDTO);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransServiceImpl.searchVStockTrans
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testSearchVStockTrans_1() throws  Exception {
        VStockTransDTO dto = null;
        stockTransService.searchVStockTrans(dto);
    }

    @Test(expected = Exception.class)
    public void testSearchVStockTrans_2() throws  Exception {
        VStockTransDTO dto = new VStockTransDTO();
        stockTransService.searchVStockTrans(dto);
    }

    @Test(expected = Exception.class)
    public void testSearchVStockTrans_3() throws  Exception {
        VStockTransDTO dto = new VStockTransDTO();
        dto.setStartDate(new Date());
        stockTransService.searchVStockTrans(dto);
    }

    @Test(expected = Exception.class)
    public void testSearchVStockTrans_4() throws  Exception {
        VStockTransDTO dto = new VStockTransDTO();
        dto.setStartDate(new Date());
        dto.setEndDate(new Date(17, 10, 9));
        stockTransService.searchVStockTrans(dto);
    }

    @Test
    public void testSearchVStockTrans_5() throws  Exception {
        VStockTransDTO dto = new VStockTransDTO();
        dto.setStartDate(new Date());
        dto.setEndDate(new Date());
        stockTransService.searchVStockTrans(dto);
    }

    @Test(expected = Exception.class)
    public void testSearchVStockTrans_6() throws  Exception {
        VStockTransDTO dto = new VStockTransDTO();
        dto.setEndDate(new Date());
        dto.setStartDate(new Date(16, 10, 9));
        stockTransService.searchVStockTrans(dto);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransServiceImpl.searchMaterialVStockTrans
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testSearchMaterialVStockTrans_1() throws  Exception {
        VStockTransDTO dto = null;
        stockTransService.searchMaterialVStockTrans(dto);
    }

    @Test(expected = Exception.class)
    public void testSearchMaterialVStockTrans_2() throws  Exception {
        VStockTransDTO dto = new VStockTransDTO();
        stockTransService.searchMaterialVStockTrans(dto);
    }

    @Test(expected = Exception.class)
    public void testSearchMaterialVStockTrans_3() throws  Exception {
        VStockTransDTO dto = new VStockTransDTO();
        dto.setStartDate(new Date());
        stockTransService.searchMaterialVStockTrans(dto);
    }

    @Test(expected = Exception.class)
    public void testSearchMaterialVStockTrans_4() throws  Exception {
        VStockTransDTO dto = new VStockTransDTO();
        dto.setStartDate(new Date());
        dto.setEndDate(new Date());
        stockTransService.searchMaterialVStockTrans(dto);
    }

    @Test(expected = Exception.class)
    public void testSearchMaterialVStockTrans_5() throws  Exception {
        VStockTransDTO dto = new VStockTransDTO();
        dto.setStartDate(new Date());
        dto.setEndDate(new Date(17, 10, 9));
        ReasonDTO reasonDTO = new ReasonDTO();
        when( reasonService.getReason(Const.REASON_CODE.EXP_MATERIAL_DEPLOY, Const.REASON_CODE.EXP_MATERIAL_DEPLOY)).thenReturn(reasonDTO);
        stockTransService.searchMaterialVStockTrans(dto);
    }

    @Test(expected = Exception.class)
    public void testSearchMaterialVStockTrans_6() throws  Exception {
        VStockTransDTO dto = new VStockTransDTO();
        dto.setEndDate(new Date());
        dto.setStartDate(new Date(16, 10, 9));
        ReasonDTO reasonDTO = new ReasonDTO();
        when( reasonService.getReason(Const.REASON_CODE.EXP_MATERIAL_DEPLOY, Const.REASON_CODE.EXP_MATERIAL_DEPLOY)).thenReturn(reasonDTO);
        reasonDTO.setReasonId(1L);
        stockTransService.searchMaterialVStockTrans(dto);
    }

    @Test
    public void testSearchMaterialVStockTrans_7() throws  Exception {
        VStockTransDTO dto = new VStockTransDTO();
        dto.setEndDate(new Date());
        dto.setStartDate(new Date());
        ReasonDTO reasonDTO = new ReasonDTO();
        when( reasonService.getReason(Const.REASON_CODE.EXP_MATERIAL_DEPLOY, Const.REASON_CODE.EXP_MATERIAL_DEPLOY)).thenReturn(reasonDTO);
        reasonDTO.setReasonId(1L);
        stockTransService.searchMaterialVStockTrans(dto);
    }
    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransServiceImpl.searchStockTransDetail
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void testSearchStockTransDetail_1() throws  Exception {
        List<Long> stockTransActionIds = Lists.newArrayList();
        stockTransService.searchStockTransDetail(stockTransActionIds);
    }

    @Test(expected = Exception.class)
    public void testSearchStockTransDetail_2() throws  Exception {
        List<Long> stockTransActionIds = Lists.newArrayList();
        stockTransActionIds.add(null);
        stockTransService.searchStockTransDetail(stockTransActionIds);
    }

    @Test(expected = Exception.class)
    public void testSearchStockTransDetail_3() throws  Exception {
        List<Long> stockTransActionIds = Lists.newArrayList();
        stockTransActionIds.add(1L);

        when(actionRepo.findOne(anyLong())).thenReturn(null);
        stockTransService.searchStockTransDetail(stockTransActionIds);
    }

    @Test
    public void testSearchStockTransDetail_4() throws  Exception {
        List<Long> stockTransActionIds = Lists.newArrayList();
        stockTransActionIds.add(1L);

        StockTransAction stockTransAction = new StockTransAction();
        when(actionRepo.findOne(anyLong())).thenReturn(stockTransAction);
        stockTransService.searchStockTransDetail(stockTransActionIds);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransServiceImpl.getTotalValueStockSusbpendByOwnerId
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testGetTotalValueStockSusbpendByOwnerId_1() throws  Exception {
        stockTransService.getTotalValueStockSusbpendByOwnerId(1L, "1");
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransServiceImpl.findStockTransByActionId
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindStockTransByActionId_1() throws  Exception {
        stockTransService.findStockTransByActionId(1L);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransServiceImpl.findStockSuspendDetail
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void testFindStockSuspendDetail_1() throws  Exception {
        stockTransService.findStockSuspendDetail(1L, "");
    }

    @Test(expected = Exception.class)
    public void testFindStockSuspendDetail_2() throws  Exception {
        stockTransService.findStockSuspendDetail(null, "1");
    }

    @Test
    public void testFindStockSuspendDetail_3() throws  Exception {
        stockTransService.findStockSuspendDetail(1L, "1");
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransServiceImpl.findStockTrans
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindStockTrans_1() throws Exception {
        ManageTransStockDto transStockDto = new ManageTransStockDto();
        stockTransService.findStockTrans(transStockDto);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransServiceImpl.viewDetailStockTrans
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testViewDetailStockTrans_1() throws Exception {
        stockTransService.viewDetailStockTrans(1L);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransServiceImpl.checkAndLockReceipt
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCheckAndLockReceipt_1() throws Exception {
        stockTransService.checkAndLockReceipt(1L);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransServiceImpl.updateDepositStatus
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void testUpdateDepositStatus_1() throws Exception {
        StockTransServiceImpl spyService = PowerMockito.spy( stockTransService);
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTrans stockTrans = new StockTrans();
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(mapper.toDtoBean(stockTrans)).thenReturn(stockTransDTO);
        when(spyService.findOne(anyLong())).thenReturn(null);
        spyService.updateDepositStatus(1L, "1");
    }

    @Test
    public void testUpdateDepositStatus_2() throws Exception {
        StockTransServiceImpl spyService = PowerMockito.spy( stockTransService);
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTrans stockTrans = new StockTrans();
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(mapper.toDtoBean(stockTrans)).thenReturn(stockTransDTO);
        when(spyService.findOne(anyLong())).thenReturn(stockTransDTO);
        spyService.updateDepositStatus(1L, "1");
    }

    @Test(expected = Exception.class)
    public void testUpdateDepositStatus_3() throws Exception {
        stockTransService.updateDepositStatus(null, "1");
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransServiceImpl.searchVStockTransAgent
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testSearchVStockTransAgent_1() throws Exception {
        stockTransService.searchVStockTransAgent(null);
    }

    @Test(expected = Exception.class)
    public void testSearchVStockTransAgent_2() throws Exception {
        stockTransService.searchVStockTransAgent(new VStockTransDTO());
    }

    @Test(expected = Exception.class)
    public void testSearchVStockTransAgent_3() throws Exception {
        VStockTransDTO dto = new VStockTransDTO();
        dto.setStartDate(new Date());
        stockTransService.searchVStockTransAgent(dto);
    }

    @Test(expected = Exception.class)
    public void testSearchVStockTransAgent_4() throws Exception {
        VStockTransDTO dto = new VStockTransDTO();
        dto.setStartDate(new Date());
        dto.setEndDate(new Date(17, 10, 9));
        stockTransService.searchVStockTransAgent(dto);
    }

    @Test(expected = Exception.class)
    public void testSearchVStockTransAgent_5() throws Exception {
        VStockTransDTO dto = new VStockTransDTO();
        dto.setStartDate(new Date());
        dto.setEndDate(new Date());
        stockTransService.searchVStockTransAgent(dto);
    }

    @Test
    public void testSearchVStockTransAgent_6() throws Exception {
        VStockTransDTO dto = new VStockTransDTO();
        dto.setStartDate(new Date());
        dto.setEndDate(new Date());
        dto.setToOwnerID(1L);
        stockTransService.searchVStockTransAgent(dto);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransServiceImpl.searchVStockTransAgent
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testGetSequence_1() throws Exception {
        stockTransService.getSequence();
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransServiceImpl.searchStockTransForTransfer
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testSearchStockTransForTransfer_1() throws Exception {
        stockTransService.searchStockTransForTransfer(new TranferIsdnInfoSearchDto());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransServiceImpl.checkStatusRetrieveExpense
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCheckStatusRetrieveExpense_1() throws Exception {
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        stockTransService.checkStatusRetrieveExpense(1L, "1", "1");
    }

    @Test(expected = Exception.class)
    public void testCheckStatusRetrieveExpense_2() throws Exception {
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenThrow(new Exception());
        stockTransService.checkStatusRetrieveExpense(1L, "1", "1");
    }

    @Test
    public void testCheckStatusRetrieveExpense_3() throws Exception {
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        stockTransService.checkStatusRetrieveExpense(1L, "", "1");
    }

    @Test(expected = Exception.class)
    public void testCheckStatusRetrieveExpense_4() throws Exception {
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenThrow(new Exception());
        stockTransService.checkStatusRetrieveExpense(1L, "1", "");
    }
    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransServiceImpl.mmSearchVStockTrans
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testMmSearchVStockTrans_1() throws Exception {
        stockTransService.mmSearchVStockTrans(new VStockTransDTO());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransServiceImpl.checkExistTransByfieldExportIsdnDTO
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCheckExistTransByfieldExportIsdnDTO_1() throws Exception {
        stockTransService.checkExistTransByfieldExportIsdnDTO(new FieldExportFileDTO());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransServiceImpl.createStockTrans
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void testCreateStockTrans_1() throws Exception {
        Date createDate=new Date();
        Long stockTransId=1L;
        ShopDTO pos= new ShopDTO();
        StockWarrantyDTO stockWarrantyDTO = new StockWarrantyDTO();
        stockWarrantyDTO.setRevokedSerial("1");
        List<StockWarrantyDTO> stockList = Lists.newArrayList(stockWarrantyDTO);
        StaffDTO staff= new StaffDTO();
        Long revokeType = 1L;

        StockTransDetailDTO std = new StockTransDetailDTO();
        StockTransSerialDTO sts = new StockTransSerialDTO();
        StockTransSerial stockTransSerial = new StockTransSerial();
        StockTransAction stockTransAction = new StockTransAction();
        StockTransDetail stockTransDetail = new StockTransDetail();
        StockTransDetailDTO stockTransDetailToSave = new StockTransDetailDTO();
        stockTransDetailToSave.setStockTransDetailId(1L);
        StockTransActionDTO transActionImport = new StockTransActionDTO();
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransId(1L);
        StockTotalMessage msg = mock(StockTotalMessage.class);

        StockTransServiceImpl spyService = PowerMockito.spy( stockTransService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("actionMapper"), actionMapper, spyService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("detailMapper"), detailMapper, spyService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("serialMapper"), serialMapper, spyService);
        Mockito.when(mapper.toDtoBean(repository.save(mapper.toPersistenceBean(stockTransDTO)))).thenReturn(stockTransDTO);
        Mockito.when(actionMapper.toPersistenceBean(transActionImport)).thenReturn(stockTransAction);
        Mockito.when(stockTransDetailRepo.save(detailMapper.toPersistenceBean(std))).thenReturn(stockTransDetail);
        Mockito.when( detailMapper.toDtoBean(stockTransDetail)).thenReturn(stockTransDetailToSave);
        Mockito.when(serialMapper.toPersistenceBean(sts)).thenReturn(stockTransSerial);
        Mockito.when(inventoryService.checkExistVirtualShop(anyLong())).thenReturn(true);
        Mockito.when(stockTotalService.changeStockTotal(any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any())).thenReturn(msg);
        Mockito.when(msg.isSuccess()).thenReturn(false);

        spyService.createStockTrans(createDate, stockTransId, pos, stockList, staff, revokeType);
    }

    @Test(expected = Exception.class)
    public void testCreateStockTrans_2() throws Exception {
        Date createDate=new Date();
        Long stockTransId=1L;
        ShopDTO pos= new ShopDTO();
        StockWarrantyDTO stockWarrantyDTO = new StockWarrantyDTO();
        stockWarrantyDTO.setRevokedSerial("1");
        List<StockWarrantyDTO> stockList = Lists.newArrayList(stockWarrantyDTO);
        StaffDTO staff= new StaffDTO();
        Long revokeType = 2L;

        StockTransDetailDTO std = new StockTransDetailDTO();
        StockTransSerialDTO sts = new StockTransSerialDTO();
        StockTransSerial stockTransSerial = new StockTransSerial();
        StockTransAction stockTransAction = new StockTransAction();
        StockTransDetail stockTransDetail = new StockTransDetail();
        StockTransDetailDTO stockTransDetailToSave = new StockTransDetailDTO();
        stockTransDetailToSave.setStockTransDetailId(1L);
        StockTransActionDTO transActionImport = new StockTransActionDTO();
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransId(1L);
        StockTotalMessage msg = mock(StockTotalMessage.class);

        StockTransServiceImpl spyService = PowerMockito.spy( stockTransService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("actionMapper"), actionMapper, spyService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("detailMapper"), detailMapper, spyService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("serialMapper"), serialMapper, spyService);
        Mockito.when(mapper.toDtoBean(repository.save(mapper.toPersistenceBean(stockTransDTO)))).thenReturn(stockTransDTO);
        Mockito.when(actionMapper.toPersistenceBean(transActionImport)).thenReturn(stockTransAction);
        Mockito.when(stockTransDetailRepo.save(detailMapper.toPersistenceBean(std))).thenReturn(stockTransDetail);
        Mockito.when( detailMapper.toDtoBean(stockTransDetail)).thenReturn(stockTransDetailToSave);
        Mockito.when(serialMapper.toPersistenceBean(sts)).thenReturn(stockTransSerial);
        Mockito.when(inventoryService.checkExistVirtualShop(anyLong())).thenReturn(true);
        Mockito.when(stockTotalService.changeStockTotal(any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any())).thenReturn(msg);
        Mockito.when(msg.isSuccess()).thenReturn(true);

        spyService.createStockTrans(createDate, stockTransId, pos, stockList, staff, revokeType);
    }

    @Test(expected = Exception.class)
    public void testCreateStockTrans_3() throws Exception {
        Date createDate=new Date();
        Long stockTransId=1L;
        ShopDTO pos= new ShopDTO();
        StockWarrantyDTO stockWarrantyDTO = new StockWarrantyDTO();
        stockWarrantyDTO.setRevokedSerial("1");
        List<StockWarrantyDTO> stockList = Lists.newArrayList(stockWarrantyDTO);
        StaffDTO staff= new StaffDTO();
        Long revokeType = 1L;

        StockTransDetailDTO std = new StockTransDetailDTO();
        StockTransSerialDTO sts = new StockTransSerialDTO();
        StockTransSerial stockTransSerial = new StockTransSerial();
        StockTransAction stockTransAction = new StockTransAction();
        StockTransDetail stockTransDetail = new StockTransDetail();
        StockTransDetailDTO stockTransDetailToSave = new StockTransDetailDTO();
        stockTransDetailToSave.setStockTransDetailId(1L);
        StockTransActionDTO transActionImport = new StockTransActionDTO();
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransId(1L);
        StockTotalMessage msg = mock(StockTotalMessage.class);

        StockTransServiceImpl spyService = PowerMockito.spy( stockTransService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("actionMapper"), actionMapper, spyService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("detailMapper"), detailMapper, spyService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("serialMapper"), serialMapper, spyService);
        Mockito.when(mapper.toDtoBean(repository.save(mapper.toPersistenceBean(stockTransDTO)))).thenReturn(stockTransDTO);
        Mockito.when(actionMapper.toPersistenceBean(transActionImport)).thenReturn(stockTransAction);
        Mockito.when(stockTransDetailRepo.save(detailMapper.toPersistenceBean(std))).thenReturn(stockTransDetail);
        Mockito.when( detailMapper.toDtoBean(stockTransDetail)).thenReturn(stockTransDetailToSave);
        Mockito.when(serialMapper.toPersistenceBean(sts)).thenReturn(stockTransSerial);
        Mockito.when(inventoryService.checkExistVirtualShop(anyLong())).thenReturn(false);
        Mockito.when(stockTotalService.changeStockTotal(any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any())).thenReturn(msg);
        Mockito.when(msg.isSuccess()).thenReturn(true);

        spyService.createStockTrans(createDate, stockTransId, pos, stockList, staff, revokeType);
    }

    @Test(expected = Exception.class)
    public void testCreateStockTrans_4() throws Exception {
        Date createDate=new Date();
        Long stockTransId=1L;
        ShopDTO pos= new ShopDTO();
        StockWarrantyDTO stockWarrantyDTO = new StockWarrantyDTO();
        stockWarrantyDTO.setRevokedSerial("1");
        List<StockWarrantyDTO> stockList = Lists.newArrayList(stockWarrantyDTO);
        StaffDTO staff= new StaffDTO();
        Long revokeType = 1L;

        StockTransDetailDTO std = new StockTransDetailDTO();
        StockTransSerialDTO sts = new StockTransSerialDTO();
        StockTransSerial stockTransSerial = new StockTransSerial();
        StockTransAction stockTransAction = new StockTransAction();
        StockTransDetail stockTransDetail = new StockTransDetail();
        StockTransDetailDTO stockTransDetailToSave = new StockTransDetailDTO();
        stockTransDetailToSave.setStockTransDetailId(1L);
        StockTransActionDTO transActionImport = new StockTransActionDTO();
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransId(1L);
        StockTotalMessage msg = mock(StockTotalMessage.class);
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue("1");
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);

        StockTransServiceImpl spyService = PowerMockito.spy( stockTransService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("actionMapper"), actionMapper, spyService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("detailMapper"), detailMapper, spyService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("serialMapper"), serialMapper, spyService);
        Mockito.when(mapper.toDtoBean(repository.save(mapper.toPersistenceBean(stockTransDTO)))).thenReturn(stockTransDTO);
        Mockito.when(actionMapper.toPersistenceBean(transActionImport)).thenReturn(stockTransAction);
        Mockito.when(stockTransDetailRepo.save(detailMapper.toPersistenceBean(std))).thenReturn(stockTransDetail);
        Mockito.when( detailMapper.toDtoBean(stockTransDetail)).thenReturn(stockTransDetailToSave);
        Mockito.when(serialMapper.toPersistenceBean(sts)).thenReturn(stockTransSerial);
        Mockito.when(inventoryService.checkExistVirtualShop(anyLong())).thenReturn(false);
        Mockito.when(stockTotalService.changeStockTotal(any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any())).thenReturn(msg);
        Mockito.when(msg.isSuccess()).thenReturn(true);
        Mockito.when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        Mockito.when(repository.updateSerialForRevoke(any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(true);

        spyService.createStockTrans(createDate, stockTransId, pos, stockList, staff, revokeType);
    }

    @Test(expected = Exception.class)
    public void testCreateStockTrans_5() throws Exception {
        Date createDate=new Date();
        Long stockTransId=1L;
        ShopDTO pos= new ShopDTO();
        StockWarrantyDTO stockWarrantyDTO = new StockWarrantyDTO();
        stockWarrantyDTO.setRevokedSerial("1");
        List<StockWarrantyDTO> stockList = Lists.newArrayList(stockWarrantyDTO);
        StaffDTO staff= new StaffDTO();
        Long revokeType = 1L;

        StockTransDetailDTO std = new StockTransDetailDTO();
        StockTransSerialDTO sts = new StockTransSerialDTO();
        StockTransSerial stockTransSerial = new StockTransSerial();
        StockTransAction stockTransAction = new StockTransAction();
        StockTransDetail stockTransDetail = new StockTransDetail();
        StockTransDetailDTO stockTransDetailToSave = new StockTransDetailDTO();
        stockTransDetailToSave.setStockTransDetailId(1L);
        StockTransActionDTO transActionImport = new StockTransActionDTO();
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransId(1L);
        StockTotalMessage msg = mock(StockTotalMessage.class);
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue("1");
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);

        StockTransServiceImpl spyService = PowerMockito.spy( stockTransService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("actionMapper"), actionMapper, spyService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("detailMapper"), detailMapper, spyService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("serialMapper"), serialMapper, spyService);
        Mockito.when(mapper.toDtoBean(repository.save(mapper.toPersistenceBean(stockTransDTO)))).thenReturn(stockTransDTO);
        Mockito.when(actionMapper.toPersistenceBean(transActionImport)).thenReturn(stockTransAction);
        Mockito.when(stockTransDetailRepo.save(detailMapper.toPersistenceBean(std))).thenReturn(stockTransDetail);
        Mockito.when( detailMapper.toDtoBean(stockTransDetail)).thenReturn(stockTransDetailToSave);
        Mockito.when(serialMapper.toPersistenceBean(sts)).thenReturn(stockTransSerial);
        Mockito.when(inventoryService.checkExistVirtualShop(anyLong())).thenReturn(true);
        Mockito.when(stockTotalService.changeStockTotal(any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any())).thenReturn(msg);
        Mockito.when(msg.isSuccess()).thenReturn(true);
        Mockito.when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        Mockito.when(repository.updateSerialForRevoke(any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(true);

        spyService.createStockTrans(createDate, stockTransId, pos, stockList, staff, revokeType);
    }

    @Test(expected = Exception.class)
    public void testCreateStockTrans_6() throws Exception {
        Date createDate=new Date();
        Long stockTransId=1L;
        ShopDTO pos= new ShopDTO();
        StockWarrantyDTO stockWarrantyDTO = new StockWarrantyDTO();
        stockWarrantyDTO.setRevokedSerial("");
        List<StockWarrantyDTO> stockList = Lists.newArrayList(stockWarrantyDTO);
        StaffDTO staff= new StaffDTO();
        Long revokeType = 2L;

        StockTransDetailDTO std = new StockTransDetailDTO();
        StockTransSerialDTO sts = new StockTransSerialDTO();
        StockTransSerial stockTransSerial = new StockTransSerial();
        StockTransAction stockTransAction = new StockTransAction();
        StockTransDetail stockTransDetail = new StockTransDetail();
        StockTransDetailDTO stockTransDetailToSave = new StockTransDetailDTO();
        stockTransDetailToSave.setStockTransDetailId(1L);
        StockTransActionDTO transActionImport = new StockTransActionDTO();
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransId(1L);
        StockTotalMessage msg = mock(StockTotalMessage.class);

        StockTransServiceImpl spyService = PowerMockito.spy( stockTransService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("actionMapper"), actionMapper, spyService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("detailMapper"), detailMapper, spyService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("serialMapper"), serialMapper, spyService);
        Mockito.when(mapper.toDtoBean(repository.save(mapper.toPersistenceBean(stockTransDTO)))).thenReturn(stockTransDTO);
        Mockito.when(actionMapper.toPersistenceBean(transActionImport)).thenReturn(stockTransAction);
        Mockito.when(stockTransDetailRepo.save(detailMapper.toPersistenceBean(std))).thenReturn(stockTransDetail);
        Mockito.when( detailMapper.toDtoBean(stockTransDetail)).thenReturn(stockTransDetailToSave);
        Mockito.when(serialMapper.toPersistenceBean(sts)).thenReturn(stockTransSerial);
        Mockito.when(inventoryService.checkExistVirtualShop(anyLong())).thenReturn(true);
        Mockito.when(stockTotalService.changeStockTotal(any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any())).thenReturn(msg);
        Mockito.when(msg.isSuccess()).thenReturn(true);

        spyService.createStockTrans(createDate, stockTransId, pos, stockList, staff, revokeType);
    }

    @Test(expected = Exception.class)
    public void testCreateStockTrans_7() throws Exception {
        Date createDate=new Date();
        Long stockTransId=1L;
        ShopDTO pos= new ShopDTO();
        StockWarrantyDTO stockWarrantyDTO = new StockWarrantyDTO();
        stockWarrantyDTO.setRevokedSerial("");
        List<StockWarrantyDTO> stockList = Lists.newArrayList(stockWarrantyDTO);
        StaffDTO staff= null;
        Long revokeType = 1L;

        StockTransDetailDTO std = new StockTransDetailDTO();
        StockTransSerialDTO sts = new StockTransSerialDTO();
        StockTransSerial stockTransSerial = new StockTransSerial();
        StockTransAction stockTransAction = new StockTransAction();
        StockTransDetail stockTransDetail = new StockTransDetail();
        StockTransDetailDTO stockTransDetailToSave = new StockTransDetailDTO();
        stockTransDetailToSave.setStockTransDetailId(1L);
        StockTransActionDTO transActionImport = new StockTransActionDTO();
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransId(1L);
        StockTotalMessage msg = mock(StockTotalMessage.class);

        StockTransServiceImpl spyService = PowerMockito.spy( stockTransService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("actionMapper"), actionMapper, spyService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("detailMapper"), detailMapper, spyService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("serialMapper"), serialMapper, spyService);
        Mockito.when(mapper.toDtoBean(repository.save(mapper.toPersistenceBean(stockTransDTO)))).thenReturn(stockTransDTO);
        Mockito.when(actionMapper.toPersistenceBean(transActionImport)).thenReturn(stockTransAction);
        Mockito.when(stockTransDetailRepo.save(detailMapper.toPersistenceBean(std))).thenReturn(stockTransDetail);
        Mockito.when( detailMapper.toDtoBean(stockTransDetail)).thenReturn(stockTransDetailToSave);
        Mockito.when(serialMapper.toPersistenceBean(sts)).thenReturn(stockTransSerial);
        Mockito.when(inventoryService.checkExistVirtualShop(anyLong())).thenReturn(true);
        Mockito.when(stockTotalService.changeStockTotal(any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any())).thenReturn(msg);
        Mockito.when(msg.isSuccess()).thenReturn(true);

        spyService.createStockTrans(createDate, stockTransId, pos, stockList, staff, revokeType);
    }

    @Test(expected = Exception.class)
    public void testCreateStockTrans_8() throws Exception {
        Date createDate=new Date();
        Long stockTransId=1L;
        ShopDTO pos= new ShopDTO();
        StockWarrantyDTO stockWarrantyDTO = new StockWarrantyDTO();
        stockWarrantyDTO.setRevokedSerial("");
        List<StockWarrantyDTO> stockList = Lists.newArrayList(stockWarrantyDTO);
        StaffDTO staff= new StaffDTO();
        staff.setStaffId(1L);
        Long revokeType = 1L;

        StockTransDetailDTO std = new StockTransDetailDTO();
        StockTransSerialDTO sts = new StockTransSerialDTO();
        StockTransSerial stockTransSerial = new StockTransSerial();
        StockTransAction stockTransAction = new StockTransAction();
        StockTransDetail stockTransDetail = new StockTransDetail();
        StockTransDetailDTO stockTransDetailToSave = new StockTransDetailDTO();
        stockTransDetailToSave.setStockTransDetailId(1L);
        StockTransActionDTO transActionImport = new StockTransActionDTO();
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransId(1L);
        StockTotalMessage msg = mock(StockTotalMessage.class);

        StockTransServiceImpl spyService = PowerMockito.spy( stockTransService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("actionMapper"), actionMapper, spyService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("detailMapper"), detailMapper, spyService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("serialMapper"), serialMapper, spyService);
        Mockito.when(mapper.toDtoBean(repository.save(mapper.toPersistenceBean(stockTransDTO)))).thenReturn(stockTransDTO);
        Mockito.when(actionMapper.toPersistenceBean(transActionImport)).thenReturn(stockTransAction);
        Mockito.when(stockTransDetailRepo.save(detailMapper.toPersistenceBean(std))).thenReturn(stockTransDetail);
        Mockito.when( detailMapper.toDtoBean(stockTransDetail)).thenReturn(stockTransDetailToSave);
        Mockito.when(serialMapper.toPersistenceBean(sts)).thenReturn(stockTransSerial);
        Mockito.when(inventoryService.checkExistVirtualShop(anyLong())).thenReturn(true);
        Mockito.when(stockTotalService.changeStockTotal(any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any())).thenReturn(msg);
        Mockito.when(msg.isSuccess()).thenReturn(true);

        spyService.createStockTrans(createDate, stockTransId, pos, stockList, staff, revokeType);
    }

    @Test
    public void testCreateStockTrans_9() throws Exception {
        Date createDate=new Date();
        Long stockTransId=1L;
        ShopDTO pos= new ShopDTO();
        StockWarrantyDTO stockWarrantyDTO = new StockWarrantyDTO();
        stockWarrantyDTO.setRevokedSerial("1");
        List<StockWarrantyDTO> stockList = Lists.newArrayList(stockWarrantyDTO);
        StaffDTO staff= new StaffDTO();
        staff.setStaffId(1L);
        Long revokeType = 2L;

        StockTransDetailDTO std = new StockTransDetailDTO();
        StockTransSerialDTO sts = new StockTransSerialDTO();
        StockTransSerial stockTransSerial = new StockTransSerial();
        StockTransAction stockTransAction = new StockTransAction();
        StockTransDetail stockTransDetail = new StockTransDetail();
        StockTransDetailDTO stockTransDetailToSave = new StockTransDetailDTO();
        stockTransDetailToSave.setStockTransDetailId(1L);
        StockTransActionDTO transActionImport = new StockTransActionDTO();
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransId(1L);
        StockTotalMessage msg = mock(StockTotalMessage.class);
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        optionSetValueDTO.setValue("1");
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);

        StockTransServiceImpl spyService = PowerMockito.spy( stockTransService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("actionMapper"), actionMapper, spyService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("detailMapper"), detailMapper, spyService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("serialMapper"), serialMapper, spyService);
        Mockito.when(baseStockIm1Service.updateSerialForRevoke(any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(true);
        Mockito.when(mapper.toDtoBean(repository.save(mapper.toPersistenceBean(stockTransDTO)))).thenReturn(stockTransDTO);
        Mockito.when(actionMapper.toPersistenceBean(transActionImport)).thenReturn(stockTransAction);
        Mockito.when(stockTransDetailRepo.save(detailMapper.toPersistenceBean(std))).thenReturn(stockTransDetail);
        Mockito.when( detailMapper.toDtoBean(stockTransDetail)).thenReturn(stockTransDetailToSave);
        Mockito.when(serialMapper.toPersistenceBean(sts)).thenReturn(stockTransSerial);
//        Mockito.when(inventoryService.checkExistVirtualShop(anyLong())).thenReturn(true);
        Mockito.when(stockTotalService.changeStockTotal(any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any())).thenReturn(msg);
        Mockito.when(msg.isSuccess()).thenReturn(true);
        Mockito.when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        Mockito.when(repository.updateSerialForRevoke(any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(true);

        spyService.createStockTrans(createDate, stockTransId, pos, stockList, staff, revokeType);
    }

    @Test
    public void testCreateStockTrans_10() throws Exception {
        Date createDate=new Date();
        Long stockTransId=1L;
        ShopDTO pos= new ShopDTO();
        StockWarrantyDTO stockWarrantyDTO = new StockWarrantyDTO();
        stockWarrantyDTO.setRevokedSerial("1");
        List<StockWarrantyDTO> stockList = Lists.newArrayList(stockWarrantyDTO);
        StaffDTO staff= new StaffDTO();
        staff.setStaffId(1L);
        Long revokeType = 2L;

        StockTransDetailDTO std = new StockTransDetailDTO();
        StockTransSerialDTO sts = new StockTransSerialDTO();
        StockTransSerial stockTransSerial = new StockTransSerial();
        StockTransAction stockTransAction = new StockTransAction();
        StockTransDetail stockTransDetail = new StockTransDetail();
        StockTransDetailDTO stockTransDetailToSave = new StockTransDetailDTO();
        stockTransDetailToSave.setStockTransDetailId(1L);
        StockTransActionDTO transActionImport = new StockTransActionDTO();
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransId(1L);
        StockTotalMessage msg = mock(StockTotalMessage.class);
        OptionSetValueDTO optionSetValueDTO = mock(OptionSetValueDTO.class);
        when(optionSetValueDTO.getValue()).thenReturn("2");
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);

        StockTransServiceImpl spyService = PowerMockito.spy( stockTransService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("actionMapper"), actionMapper, spyService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("detailMapper"), detailMapper, spyService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("serialMapper"), serialMapper, spyService);
        Mockito.when(baseStockIm1Service.updateSerialForRevoke(any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(true);
        Mockito.when(mapper.toDtoBean(repository.save(mapper.toPersistenceBean(stockTransDTO)))).thenReturn(stockTransDTO);
        Mockito.when(actionMapper.toPersistenceBean(transActionImport)).thenReturn(stockTransAction);
        Mockito.when(stockTransDetailRepo.save(detailMapper.toPersistenceBean(std))).thenReturn(stockTransDetail);
        Mockito.when( detailMapper.toDtoBean(stockTransDetail)).thenReturn(stockTransDetailToSave);
        Mockito.when(serialMapper.toPersistenceBean(sts)).thenReturn(stockTransSerial);
//        Mockito.when(inventoryService.checkExistVirtualShop(anyLong())).thenReturn(true);
        Mockito.when(stockTotalService.changeStockTotal(any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any())).thenReturn(msg);
        Mockito.when(msg.isSuccess()).thenReturn(true);
        Mockito.when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        Mockito.when(repository.updateSerialForRevoke(any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(true);

        spyService.createStockTrans(createDate, stockTransId, pos, stockList, staff, revokeType);
    }

    @Test
    public void testCreateStockTrans_11() throws Exception {
        Date createDate=new Date();
        Long stockTransId=1L;
        ShopDTO pos= new ShopDTO();
        StockWarrantyDTO stockWarrantyDTO = new StockWarrantyDTO();
        stockWarrantyDTO.setRevokedSerial("1");
        List<StockWarrantyDTO> stockList = Lists.newArrayList(stockWarrantyDTO);
        StaffDTO staff= new StaffDTO();
        staff.setStaffId(1L);
        Long revokeType = 2L;

        StockTransDetailDTO std = new StockTransDetailDTO();
        StockTransSerialDTO sts = new StockTransSerialDTO();
        StockTransSerial stockTransSerial = new StockTransSerial();
        StockTransAction stockTransAction = new StockTransAction();
        StockTransDetail stockTransDetail = new StockTransDetail();
        StockTransDetailDTO stockTransDetailToSave = new StockTransDetailDTO();
        stockTransDetailToSave.setStockTransDetailId(1L);
        StockTransActionDTO transActionImport = new StockTransActionDTO();
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransId(1L);
        StockTotalMessage msg = mock(StockTotalMessage.class);
        OptionSetValueDTO optionSetValueDTO = mock(OptionSetValueDTO.class);
        when(optionSetValueDTO.getValue()).thenReturn("2");
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList();

        StockTransServiceImpl spyService = PowerMockito.spy( stockTransService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("actionMapper"), actionMapper, spyService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("detailMapper"), detailMapper, spyService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("serialMapper"), serialMapper, spyService);
        Mockito.when(baseStockIm1Service.updateSerialForRevoke(any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(true);
        Mockito.when(mapper.toDtoBean(repository.save(mapper.toPersistenceBean(stockTransDTO)))).thenReturn(stockTransDTO);
        Mockito.when(actionMapper.toPersistenceBean(transActionImport)).thenReturn(stockTransAction);
        Mockito.when(stockTransDetailRepo.save(detailMapper.toPersistenceBean(std))).thenReturn(stockTransDetail);
        Mockito.when( detailMapper.toDtoBean(stockTransDetail)).thenReturn(stockTransDetailToSave);
        Mockito.when(serialMapper.toPersistenceBean(sts)).thenReturn(stockTransSerial);
        Mockito.when(stockTotalService.changeStockTotal(any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any())).thenReturn(msg);
        Mockito.when(msg.isSuccess()).thenReturn(true);
        Mockito.when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        Mockito.when(repository.updateSerialForRevoke(any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(true);

        spyService.createStockTrans(createDate, stockTransId, pos, stockList, staff, revokeType);
    }

    @Test
    public void testCreateStockTrans_12() throws Exception {
        Date createDate=new Date();
        Long stockTransId=1L;
        ShopDTO pos= new ShopDTO();
        StockWarrantyDTO stockWarrantyDTO = new StockWarrantyDTO();
        stockWarrantyDTO.setRevokedSerial("1");
        List<StockWarrantyDTO> stockList = Lists.newArrayList(stockWarrantyDTO);
        StaffDTO staff= new StaffDTO();
        staff.setStaffId(1L);
        Long revokeType = 2L;

        StockTransDetailDTO std = new StockTransDetailDTO();
        StockTransSerialDTO sts = new StockTransSerialDTO();
        StockTransSerial stockTransSerial = new StockTransSerial();
        StockTransAction stockTransAction = new StockTransAction();
        StockTransDetail stockTransDetail = new StockTransDetail();
        StockTransDetailDTO stockTransDetailToSave = new StockTransDetailDTO();
        stockTransDetailToSave.setStockTransDetailId(1L);
        StockTransActionDTO transActionImport = new StockTransActionDTO();
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransId(1L);
        StockTotalMessage msg = mock(StockTotalMessage.class);
        OptionSetValueDTO optionSetValueDTO = mock(OptionSetValueDTO.class);
        when(optionSetValueDTO.getValue()).thenReturn(null);
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);

        StockTransServiceImpl spyService = PowerMockito.spy( stockTransService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("actionMapper"), actionMapper, spyService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("detailMapper"), detailMapper, spyService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("serialMapper"), serialMapper, spyService);
        Mockito.when(baseStockIm1Service.updateSerialForRevoke(any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(true);
        Mockito.when(mapper.toDtoBean(repository.save(mapper.toPersistenceBean(stockTransDTO)))).thenReturn(stockTransDTO);
        Mockito.when(actionMapper.toPersistenceBean(transActionImport)).thenReturn(stockTransAction);
        Mockito.when(stockTransDetailRepo.save(detailMapper.toPersistenceBean(std))).thenReturn(stockTransDetail);
        Mockito.when( detailMapper.toDtoBean(stockTransDetail)).thenReturn(stockTransDetailToSave);
        Mockito.when(serialMapper.toPersistenceBean(sts)).thenReturn(stockTransSerial);
        Mockito.when(stockTotalService.changeStockTotal(any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any())).thenReturn(msg);
        Mockito.when(msg.isSuccess()).thenReturn(true);
        Mockito.when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        Mockito.when(repository.updateSerialForRevoke(any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(true);

        spyService.createStockTrans(createDate, stockTransId, pos, stockList, staff, revokeType);
    }
    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransServiceImpl.getStockTransIdByCodeExist
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testGetStockTransIdByCodeExist_1() throws Exception {
        stockTransService.getStockTransIdByCodeExist("1","1",1L,1L);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransServiceImpl.saveStockTransOffline
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testSaveStockTransOffline_1() throws Exception {
        StockTransDTO stockTransDTO = mock(StockTransDTO.class);
        StockTransSerialDTO stockTransSerialDTO =new StockTransSerialDTO();
        List<StockTransSerialDTO> lstSerial = Lists.newArrayList(stockTransSerialDTO);
        StockTransDetailDTO stockTransDetailDTO = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lsStockTransDetailDTOList = Lists.newArrayList(stockTransDetailDTO);
        stockTransDTO.setStockTransId(1L);
        stockTransDTO.setLsStockTransDetailDTOList(lsStockTransDetailDTOList);

        List<StockTransDTO> lsStockTransDTOs = Lists.newArrayList(stockTransDTO);

        StockTransServiceImpl spyService = PowerMockito.spy( stockTransService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);

        Mockito.when(mapper.toDtoBean(repository.findOne(anyLong()))).thenReturn(stockTransDTO);
        Mockito.when(stockTransDetailDTO.getLstSerial()).thenReturn(lstSerial);
        Mockito.when(stockTransDTO.getLsStockTransDetailDTOList()).thenReturn(lsStockTransDetailDTOList);
        Mockito.when(stockTransDetailDTO.getStateId()).thenReturn(4L);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.saveStockTransOffline(lsStockTransDTOs);
    }

    @Test
    public void testSaveStockTransOffline_2() throws Exception {
        StockTransDTO stockTransDTO = mock(StockTransDTO.class);
        StockTransSerialDTO stockTransSerialDTO =new StockTransSerialDTO();
        List<StockTransSerialDTO> lstSerial = Lists.newArrayList(stockTransSerialDTO);
        StockTransDetailDTO stockTransDetailDTO = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lsStockTransDetailDTOList = Lists.newArrayList(stockTransDetailDTO);
        stockTransDTO.setStockTransId(1L);
        stockTransDTO.setLsStockTransDetailDTOList(lsStockTransDetailDTOList);

        List<StockTransDTO> lsStockTransDTOs = Lists.newArrayList();

        StockTransServiceImpl spyService = PowerMockito.spy( stockTransService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);

        Mockito.when(mapper.toDtoBean(repository.findOne(anyLong()))).thenReturn(stockTransDTO);
        Mockito.when(stockTransDetailDTO.getLstSerial()).thenReturn(lstSerial);
        Mockito.when(stockTransDTO.getLsStockTransDetailDTOList()).thenReturn(lsStockTransDetailDTOList);
        Mockito.when(stockTransDetailDTO.getStateId()).thenReturn(4L);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.saveStockTransOffline(lsStockTransDTOs);
    }

    @Test
    public void testSaveStockTransOffline_3() throws Exception {
        StockTransDTO stockTransDTO = mock(StockTransDTO.class);
        StockTransSerialDTO stockTransSerialDTO =new StockTransSerialDTO();
        List<StockTransSerialDTO> lstSerial = Lists.newArrayList(stockTransSerialDTO);
        StockTransDetailDTO stockTransDetailDTO = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lsStockTransDetailDTOList = Lists.newArrayList(stockTransDetailDTO);
        stockTransDTO.setStockTransId(1L);
        stockTransDTO.setLsStockTransDetailDTOList(lsStockTransDetailDTOList);

        List<StockTransDTO> lsStockTransDTOs = Lists.newArrayList(stockTransDTO);

        StockTransServiceImpl spyService = PowerMockito.spy( stockTransService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);

        Mockito.when(mapper.toDtoBean(repository.findOne(anyLong()))).thenReturn(null);
        Mockito.when(stockTransDetailDTO.getLstSerial()).thenReturn(lstSerial);
        Mockito.when(stockTransDTO.getLsStockTransDetailDTOList()).thenReturn(lsStockTransDetailDTOList);
        Mockito.when(stockTransDetailDTO.getStateId()).thenReturn(4L);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.saveStockTransOffline(lsStockTransDTOs);
    }

    @Test
    public void testSaveStockTransOffline_4() throws Exception {
        StockTransDTO stockTransDTO = mock(StockTransDTO.class);
        StockTransSerialDTO stockTransSerialDTO =new StockTransSerialDTO();
        List<StockTransSerialDTO> lstSerial = Lists.newArrayList(stockTransSerialDTO);
        StockTransDetailDTO stockTransDetailDTO = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lsStockTransDetailDTOList = Lists.newArrayList();
        stockTransDTO.setStockTransId(1L);
        stockTransDTO.setLsStockTransDetailDTOList(lsStockTransDetailDTOList);

        List<StockTransDTO> lsStockTransDTOs = Lists.newArrayList(stockTransDTO);

        StockTransServiceImpl spyService = PowerMockito.spy( stockTransService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);

        Mockito.when(mapper.toDtoBean(repository.findOne(anyLong()))).thenReturn(stockTransDTO);
        Mockito.when(stockTransDetailDTO.getLstSerial()).thenReturn(lstSerial);
        Mockito.when(stockTransDTO.getLsStockTransDetailDTOList()).thenReturn(lsStockTransDetailDTOList);
        Mockito.when(stockTransDetailDTO.getStateId()).thenReturn(4L);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.saveStockTransOffline(lsStockTransDTOs);
    }


    @Test
    public void testSaveStockTransOffline_5() throws Exception {
        StockTransDTO stockTransDTO = mock(StockTransDTO.class);
        StockTransSerialDTO stockTransSerialDTO =new StockTransSerialDTO();
        List<StockTransSerialDTO> lstSerial = Lists.newArrayList(stockTransSerialDTO);
        StockTransDetailDTO stockTransDetailDTO = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lsStockTransDetailDTOList = Lists.newArrayList(stockTransDetailDTO);
        stockTransDTO.setStockTransId(1L);
        stockTransDTO.setLsStockTransDetailDTOList(lsStockTransDetailDTOList);

        List<StockTransDTO> lsStockTransDTOs = Lists.newArrayList(stockTransDTO);

        StockTransServiceImpl spyService = PowerMockito.spy( stockTransService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);

        Mockito.when(mapper.toDtoBean(repository.findOne(anyLong()))).thenReturn(stockTransDTO);
        Mockito.when(stockTransDetailDTO.getLstSerial()).thenReturn(lstSerial);
        Mockito.when(stockTransDTO.getLsStockTransDetailDTOList()).thenReturn(lsStockTransDetailDTOList);
        Mockito.when(stockTransDetailDTO.getStateId()).thenReturn(3L);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.saveStockTransOffline(lsStockTransDTOs);
    }

    @Test
    public void testSaveStockTransOffline_6() throws Exception {
        StockTransDTO stockTransDTO = mock(StockTransDTO.class);
        StockTransSerialDTO stockTransSerialDTO =new StockTransSerialDTO();
        List<StockTransSerialDTO> lstSerial = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lsStockTransDetailDTOList = Lists.newArrayList(stockTransDetailDTO);
        stockTransDTO.setStockTransId(1L);
        stockTransDTO.setLsStockTransDetailDTOList(lsStockTransDetailDTOList);

        List<StockTransDTO> lsStockTransDTOs = Lists.newArrayList(stockTransDTO);

        StockTransServiceImpl spyService = PowerMockito.spy( stockTransService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);

        Mockito.when(mapper.toDtoBean(repository.findOne(anyLong()))).thenReturn(stockTransDTO);
        Mockito.when(stockTransDetailDTO.getLstSerial()).thenReturn(lstSerial);
        Mockito.when(stockTransDTO.getLsStockTransDetailDTOList()).thenReturn(lsStockTransDetailDTOList);
        Mockito.when(stockTransDetailDTO.getStateId()).thenReturn(3L);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.saveStockTransOffline(lsStockTransDTOs);
    }
    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransServiceImpl.getListStockFullDTOByTransIdAndStatus
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testGetListStockFullDTOByTransIdAndStatus_1() throws Exception {
        List<Long> lsStockTransId = Lists.newArrayList();
        List<Long> lsStatusAction= Lists.newArrayList();
        stockTransService.getListStockFullDTOByTransIdAndStatus(lsStockTransId,lsStatusAction);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransServiceImpl.checkStockSusbpendForProcessStock
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCheckStockSusbpendForProcessStock_1() throws Exception {
        stockTransService.checkStockSusbpendForProcessStock(1L, "1", 1L);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransServiceImpl.getSaleTransFromStockTrans
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testGetSaleTransFromStockTrans_1() throws Exception {
        stockTransService.getSaleTransFromStockTrans(1L);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransServiceImpl.saveAPDeployment
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void testSaveAPDeployment_1() throws Exception {
        StaffDTO staffDTO = new StaffDTO();
        String account = "1";
        String troubleType = "2";
        StockTransDetailDTO stockTransDetailDTO = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetailDTO);
        String transId = "2";
        StockTotalMessage msg = mock(StockTotalMessage.class);
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransId(1L);
        StockTransExt stockTransExt = new StockTransExt();
        StockTransExtDTO stockTransExtDTO = new StockTransExtDTO();
        StockTransActionDTO transAction = new StockTransActionDTO();
        StockTransAction stockTransAction = new StockTransAction();
        StockTransDetailDTO std = new StockTransDetailDTO();
        StockTransDetail stockTransDetail = new StockTransDetail();

        StockTransServiceImpl spyService = PowerMockito.spy( stockTransService);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("extMapper"), extMapper, spyService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("actionMapper"), actionMapper, spyService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("detailMapper"), detailMapper, spyService);
        Mockito.when( actionMapper.toPersistenceBean(transAction)).thenReturn(stockTransAction);
        Mockito.when(extMapper.toPersistenceBean(stockTransExtDTO)).thenReturn(stockTransExt);
        Mockito.when(mapper.toDtoBean(repository.save(mapper.toPersistenceBean(stockTransDTO)))).thenReturn(stockTransDTO);
        Mockito.when(stockTransDetailRepo.save(detailMapper.toPersistenceBean(std))).thenReturn(stockTransDetail);
        Mockito.when(stockTotalService.changeStockTotal(any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any())).thenReturn(msg);
        when(msg.isSuccess()).thenReturn(false);
        when(stockTransDetailDTO.getProdOfferId()).thenReturn(1L);
        when(stockTransDetailDTO.getQuantity()).thenReturn(1L);
        spyService.saveAPDeployment( staffDTO,  account,  troubleType,  lstStockTransDetail,  transId);
    }

    @Test
    public void testSaveAPDeployment_2() throws Exception {
        StaffDTO staffDTO = new StaffDTO();
        String account = "1";
        String troubleType = "2";
        StockTransDetailDTO stockTransDetailDTO = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetailDTO);
        String transId = "2";
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransId(1L);
        StockTransExt stockTransExt = new StockTransExt();
        StockTransExtDTO stockTransExtDTO = new StockTransExtDTO();
        StockTransActionDTO transAction = new StockTransActionDTO();
        StockTransAction stockTransAction = new StockTransAction();
        StockTransDetailDTO std = new StockTransDetailDTO();
        StockTotalMessage msg = mock(StockTotalMessage.class);
        StockTransDetail stockTransDetail = new StockTransDetail();

        StockTransServiceImpl spyService = PowerMockito.spy( stockTransService);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("extMapper"), extMapper, spyService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("actionMapper"), actionMapper, spyService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("detailMapper"), detailMapper, spyService);
        Mockito.when( actionMapper.toPersistenceBean(transAction)).thenReturn(stockTransAction);
        Mockito.when(extMapper.toPersistenceBean(stockTransExtDTO)).thenReturn(stockTransExt);
        Mockito.when(mapper.toDtoBean(repository.save(mapper.toPersistenceBean(stockTransDTO)))).thenReturn(stockTransDTO);
        Mockito.when(stockTransDetailRepo.save(detailMapper.toPersistenceBean(std))).thenReturn(stockTransDetail);
        Mockito.when(stockTotalService.changeStockTotal(any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any())).thenReturn(msg);
        when(msg.isSuccess()).thenReturn(true);
        when(stockTransDetailDTO.getProdOfferId()).thenReturn(1L);
        when(stockTransDetailDTO.getQuantity()).thenReturn(1L);
        spyService.saveAPDeployment( staffDTO,  account,  troubleType,  lstStockTransDetail,  transId);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransServiceImpl.restoreAPDeployment
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testRestoreAPDeployment_1() throws Exception {
        StaffDTO staffDTO = new StaffDTO();
        String account = "1";
        String troubleType = "2";
        StockTransDetailDTO stockTransDetailDTO = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetailDTO);
        String transId = "2";
        StockTransExtDTO stockTransExtDTO = mock(StockTransExtDTO.class);
        stockTransExtDTO.setStockTransId(1L);
        StockTransExt stockTransExt = new StockTransExt();
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransId(1L);
        StockTransActionDTO transAction = new StockTransActionDTO();
        StockTransAction stockTransAction = new StockTransAction();
        StockTransDetailDTO std = new StockTransDetailDTO();
        StockTotalMessage msg = mock(StockTotalMessage.class);
        StockTransDetail stockTransDetail = new StockTransDetail();

        StockTransServiceImpl spyService = PowerMockito.spy( stockTransService);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        PowerMockito.when(  stockTransExtRepo.getStockTransId("TROUBLE_ID", transId) ).thenReturn( stockTransExtDTO);
        Mockito.when(mapper.toDtoBean(repository.save(mapper.toPersistenceBean(stockTransDTO)))).thenReturn(stockTransDTO);
        Mockito.when( actionMapper.toPersistenceBean(transAction)).thenReturn(stockTransAction);
        PowerMockito.when( extMapper.toPersistenceBean(stockTransExtDTO) ).thenReturn( stockTransExt);
        Mockito.when(stockTransDetailRepo.save(detailMapper.toPersistenceBean(std))).thenReturn(stockTransDetail);
        Mockito.when(stockTotalService.changeStockTotal(any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any())).thenReturn(msg);
        when(msg.isSuccess()).thenReturn(true);
        when(stockTransDetailDTO.getProdOfferId()).thenReturn(1L);
        when(stockTransDetailDTO.getQuantity()).thenReturn(1L);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("extMapper"), extMapper, spyService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("actionMapper"), actionMapper, spyService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("detailMapper"), detailMapper, spyService);
        spyService.restoreAPDeployment( staffDTO,  account,  troubleType,  lstStockTransDetail,  transId);
    }

    @Test(expected = Exception.class)
    public void testRestoreAPDeployment_2() throws Exception {
        StaffDTO staffDTO = new StaffDTO();
        String account = "1";
        String troubleType = "2";
        StockTransDetailDTO stockTransDetailDTO = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetailDTO);
        String transId = "2";
        StockTransExtDTO stockTransExtDTO = mock(StockTransExtDTO.class);
        stockTransExtDTO.setStockTransId(1L);
        StockTransExt stockTransExt = new StockTransExt();
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransId(1L);
        StockTransActionDTO transAction = new StockTransActionDTO();
        StockTransAction stockTransAction = new StockTransAction();
        StockTransDetailDTO std = new StockTransDetailDTO();
        StockTotalMessage msg = mock(StockTotalMessage.class);
        StockTransDetail stockTransDetail = new StockTransDetail();

        StockTransServiceImpl spyService = PowerMockito.spy( stockTransService);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        PowerMockito.when(  stockTransExtRepo.getStockTransId("TROUBLE_ID", transId) ).thenReturn( stockTransExtDTO);
        Mockito.when(mapper.toDtoBean(repository.save(mapper.toPersistenceBean(stockTransDTO)))).thenReturn(stockTransDTO);
        Mockito.when( actionMapper.toPersistenceBean(transAction)).thenReturn(stockTransAction);
        PowerMockito.when( extMapper.toPersistenceBean(stockTransExtDTO) ).thenReturn( stockTransExt);
        Mockito.when(stockTransDetailRepo.save(detailMapper.toPersistenceBean(std))).thenReturn(stockTransDetail);
        Mockito.when(stockTotalService.changeStockTotal(any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any())).thenReturn(msg);
        when(msg.isSuccess()).thenReturn(false);
        when(stockTransDetailDTO.getProdOfferId()).thenReturn(1L);
        when(stockTransDetailDTO.getQuantity()).thenReturn(1L);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("extMapper"), extMapper, spyService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("actionMapper"), actionMapper, spyService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("detailMapper"), detailMapper, spyService);
        spyService.restoreAPDeployment( staffDTO,  account,  troubleType,  lstStockTransDetail,  transId);
    }

    @Test
    public void testRestoreAPDeployment_3() throws Exception {
        StaffDTO staffDTO = new StaffDTO();
        String account = "1";
        String troubleType = "2";
        StockTransDetailDTO stockTransDetailDTO = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(stockTransDetailDTO);
        String transId = "2";
        StockTransExtDTO stockTransExtDTO = mock(StockTransExtDTO.class);
        stockTransExtDTO.setStockTransId(1L);
        StockTransExt stockTransExt = new StockTransExt();
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransId(1L);
        StockTransActionDTO transAction = new StockTransActionDTO();
        StockTransAction stockTransAction = new StockTransAction();
        StockTransDetailDTO std = new StockTransDetailDTO();
        StockTotalMessage msg = mock(StockTotalMessage.class);
        StockTransDetail stockTransDetail = new StockTransDetail();

        StockTransServiceImpl spyService = PowerMockito.spy( stockTransService);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        PowerMockito.when(  stockTransExtRepo.getStockTransId("TROUBLE_ID", transId) ).thenReturn(null);
        Mockito.when(mapper.toDtoBean(repository.save(mapper.toPersistenceBean(stockTransDTO)))).thenReturn(stockTransDTO);
        Mockito.when( actionMapper.toPersistenceBean(transAction)).thenReturn(stockTransAction);
        PowerMockito.when( extMapper.toPersistenceBean(stockTransExtDTO) ).thenReturn( stockTransExt);
        Mockito.when(stockTransDetailRepo.save(detailMapper.toPersistenceBean(std))).thenReturn(stockTransDetail);
        Mockito.when(stockTotalService.changeStockTotal(any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any())).thenReturn(msg);
        when(msg.isSuccess()).thenReturn(true);
        when(stockTransDetailDTO.getProdOfferId()).thenReturn(1L);
        when(stockTransDetailDTO.getQuantity()).thenReturn(1L);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("extMapper"), extMapper, spyService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("actionMapper"), actionMapper, spyService);
        setFinalStatic(StockTransServiceImpl.class.getDeclaredField("detailMapper"), detailMapper, spyService);
        spyService.restoreAPDeployment( staffDTO,  account,  troubleType,  lstStockTransDetail,  transId);
    }
    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransServiceImpl.checkHaveProductOffering
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCheckHaveProductOffering_1() throws Exception {
        stockTransService.checkHaveProductOffering(1L,1L, false);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransServiceImpl.checkStockTransSuppend
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCheckStockTransSuppend_1() throws Exception {
        stockTransService.checkStockTransSuppend(1L,1L);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransServiceImpl.getTransAmount
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testGetTransAmount_1() throws Exception {
        stockTransService.getTransAmount(1L);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransServiceImpl.getStockTransForLogistics
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testGetStockTransForLogistics_1() throws Exception {
        stockTransService.getStockTransForLogistics(1L);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransServiceImpl.checkSaleTrans
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCheckSaleTrans_1() throws Exception {
        stockTransService.checkSaleTrans(1L, new Date());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransServiceImpl.getContentFileDOA
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testGetContentFileDOA_1() throws Exception {
        stockTransService.getContentFileDOA(1L);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransServiceImpl.findVStockTransDTO
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindVStockTransDTO_1() throws Exception {
        stockTransService.findVStockTransDTO(1L,1L);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransServiceImpl.getLstRequestOrderExported
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testGetLstRequestOrderExported_1() throws Exception {
        stockTransService.getLstRequestOrderExported();
    }



    static void setFinalStatic(Field field, Object newValue, final Object targetObject) throws Exception {
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(targetObject, newValue);
    }
}