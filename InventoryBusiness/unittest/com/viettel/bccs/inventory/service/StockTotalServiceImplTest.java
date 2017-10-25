package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.im1.dto.StockTotalIm1DTO;
import com.viettel.bccs.im1.service.StockTotalIm1Service;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.dto.ws.StockTotalWsDTO;
import com.viettel.bccs.inventory.model.StockTotal;
import com.viettel.bccs.inventory.model.StockTotalAudit;
import com.viettel.bccs.inventory.repo.InventoryWsRepo;
import com.viettel.bccs.inventory.repo.StockTotalRepo;
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
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author DatLT
 * @date 22/10/2017
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({DbUtil.class, BaseServiceImpl.class, StockTotalService.class})
public class StockTotalServiceImplTest {
    @InjectMocks
    StockTotalServiceImpl stockTotalService;
    @Mock
    private final BaseMapper<StockTotal, StockTotalDTO> mapper = new BaseMapper(StockTotal.class, StockTotalDTO.class);
    @Mock
    private final BaseMapper<StockTotalAudit, StockTotalAuditDTO> mapperStockTotalAudit = new BaseMapper(StockTotalAudit.class, StockTotalAuditDTO.class);
    @Mock
    private StockTotalRepo repository;
    @Mock
    private InventoryWsRepo repositoryInventory;
    @Mock
    private EntityManager em;
    @Mock
    private StockTotalAuditService stockTotalAuditService;
    @Mock
    private OptionSetValueService optionSetValueService;
    @Mock
    private ActionLogService actionLogService;
    @Mock
    private StockTotalIm1Service stockTotalIm1Service;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTotalServiceImpl.count
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCount_1() throws Exception {
        List<FilterRequest> filters = new ArrayList<>();
        when(repository.count(any())).thenReturn(1L);
        Assert.assertEquals(1L, stockTotalService.count(filters).longValue());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTotalServiceImpl.findOne
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindOne_1() throws  Exception {
        StockTotalServiceImpl spyService = PowerMockito.spy( stockTotalService);
        StockTotal stockTotal = mock(StockTotal.class);
        StockTotalDTO stockTotalDTO = new StockTotalDTO();
        Mockito.when(repository.findOne(anyLong())).thenReturn(stockTotal);
        setFinalStatic(StockTotalServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(mapper.toDtoBean(stockTotal)).thenReturn(stockTotalDTO);
        spyService.findOne(1L);
        Assert.assertNotNull(stockTotalDTO);
    }


    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTotalServiceImpl.findAll
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindAll_1() throws  Exception {
        StockTotalServiceImpl spyService = PowerMockito.spy( stockTotalService);
        StockTotal stockTotal = mock(StockTotal.class);
        StockTotalDTO stockTotalDTO = new StockTotalDTO();
        List<StockTotal> stockTotalList = Lists.newArrayList(stockTotal);
        List<StockTotalDTO> stockTotalDTOList = Lists.newArrayList(stockTotalDTO);
        Mockito.when(repository.findAll()).thenReturn(stockTotalList);
        Mockito.when(mapper.toDtoBean(stockTotalList)).thenReturn(stockTotalDTOList);
        setFinalStatic(StockTotalServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findAll();
        Assert.assertNotNull(stockTotalDTOList);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTotalServiceImpl.findByFilter
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindByFilter_1() throws  Exception {
        FilterRequest filterRequest = new FilterRequest();
        List<FilterRequest> filterRequestList = Lists.newArrayList(filterRequest);
        StockTotalServiceImpl spyService = PowerMockito.spy( stockTotalService);
        StockTotal stockTotal = mock(StockTotal.class);
        StockTotalDTO stockTotalDTO = new StockTotalDTO();
        List<StockTotal> stockTotalList = Lists.newArrayList(stockTotal);
        List<StockTotalDTO> stockTotalDTOList = Lists.newArrayList(stockTotalDTO);
        Mockito.when(mapper.toDtoBean(stockTotalList)).thenReturn(stockTotalDTOList);
        setFinalStatic(StockTotalServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findByFilter(filterRequestList);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTotalServiceImpl.save
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testSave_1() throws  Exception {
        StockTotalServiceImpl spyService = PowerMockito.spy( stockTotalService);
        StockTotal stockTotal = mock(StockTotal.class);
        StockTotalDTO stockTotalDTO = new StockTotalDTO();
        List<StockTotal> stockTotalList = Lists.newArrayList(stockTotal);
        List<StockTotalDTO> stockTotalDTOList = Lists.newArrayList(stockTotalDTO);
        Mockito.when(mapper.toDtoBean(stockTotalList)).thenReturn(stockTotalDTOList);
        Mockito.when(mapper.toPersistenceBean(stockTotalDTO)).thenReturn(stockTotal);
        setFinalStatic(StockTotalServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.save(new StockTotalDTO());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTotalServiceImpl.saveForProcessStockSerial
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void testSaveForProcessStockSerial_1() throws  Exception {
        StockTotalServiceImpl spyService = PowerMockito.spy( stockTotalService);
        ActionLogDTO dto = new ActionLogDTO();
        List<ActionLogDetailDTO> lstDetail = Lists.newArrayList();

        StockTotal stockTotal = mock(StockTotal.class);
        StockTotalDTO stockTotalDTO = new StockTotalDTO();
        List<StockTotal> stockTotalList = Lists.newArrayList(stockTotal);
        List<StockTotalDTO> stockTotalDTOList = Lists.newArrayList(stockTotalDTO);
        Mockito.when(mapper.toDtoBean(stockTotalList)).thenReturn(stockTotalDTOList);
        Mockito.when(mapper.toPersistenceBean(stockTotalDTO)).thenReturn(stockTotal);
        setFinalStatic(StockTotalServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenThrow(new Exception());
        spyService.saveForProcessStockSerial(stockTotalDTO, dto, lstDetail);
    }

    @Test
    public void testSaveForProcessStockSerial_2() throws  Exception {
        StockTotalServiceImpl spyService = PowerMockito.spy( stockTotalService);
        ActionLogDTO dto = new ActionLogDTO();
        List<ActionLogDetailDTO> lstDetail = Lists.newArrayList();

        StockTotal stockTotal = mock(StockTotal.class);
        StockTotalDTO stockTotalDTO = new StockTotalDTO();


        Mockito.when(mapper.toDtoBean(stockTotal)).thenReturn(stockTotalDTO);
        Mockito.when(mapper.toPersistenceBean(stockTotalDTO)).thenReturn(stockTotal);
        Mockito.when(repository.save(stockTotal)).thenReturn(stockTotal);
        setFinalStatic(StockTotalServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        stockTotalDTO.setOwnerType(1L);
        stockTotalDTO.setOwnerId(1L);
        stockTotalDTO.setStateId(1L);
        stockTotalDTO.setProdOfferId(1L);

        PowerMockito.mockStatic(MessageFormat.class);
        PowerMockito.when(MessageFormat.format(anyString(), anyLong(), anyLong(), anyLong(), anyLong())).thenReturn("params");
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn(new Date());
        spyService.saveForProcessStockSerial(stockTotalDTO, dto, lstDetail);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTotalServiceImpl.saveForProcessStockNoSerial
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testSaveForProcessStockNoSerial_1() throws  Exception {
        StockTotalServiceImpl spyService = PowerMockito.spy( stockTotalService);

        ActionLogDetailDTO actionLogDetailDTO = new ActionLogDetailDTO();
        List<ActionLogDetailDTO> actionLogDetailDTOList = Lists.newArrayList(actionLogDetailDTO);
        actionLogDetailDTOList.add(actionLogDetailDTO);
        spyService.saveForProcessStockNoSerial(1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L
                , new Date(), "1", "1", Const.SourceType.STOCK_TRANS, new ActionLogDTO(), actionLogDetailDTOList);
    }

    @Test(expected = Exception.class)
    public void testSaveForProcessStockNoSerial_2() throws  Exception {
        StockTotalServiceImpl spyService = PowerMockito.spy( stockTotalService);

        ActionLogDetailDTO actionLogDetailDTO = new ActionLogDetailDTO();
        List<ActionLogDetailDTO> actionLogDetailDTOList = Lists.newArrayList(actionLogDetailDTO);

        spyService.saveForProcessStockNoSerial(1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L
                , new Date(), "1", "1", Const.SourceType.STOCK_TRANS, new ActionLogDTO(), actionLogDetailDTOList);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTotalServiceImpl.searchStockTotal
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testSearchStockTotal_1() throws  Exception {
        StockTotalServiceImpl spyService = PowerMockito.spy( stockTotalService);
        StockTotalDTO stockTotalDTO = new StockTotalDTO();
        StockTotal stockTotal = new StockTotal();

        List<StockTotalDTO> stockTotalDTOList = Lists.newArrayList(stockTotalDTO);
        List<StockTotal> stockTotalList = Lists.newArrayList(stockTotal);
        stockTotalDTO.setOwnerId(1L);
        stockTotalDTO.setOwnerType(1L);
        stockTotalDTO.setProdOfferId(1L);
        stockTotalDTO.setStateId(1L);
        stockTotalDTO.setStatus(1L);

        Mockito.when(mapper.toDtoBean(stockTotalList)).thenReturn(stockTotalDTOList);

        spyService.searchStockTotal(stockTotalDTO);
    }

    @Test
    public void testSearchStockTotal_2() throws  Exception {
        StockTotalServiceImpl spyService = PowerMockito.spy( stockTotalService);
        StockTotalDTO stockTotalDTO = new StockTotalDTO();
        StockTotal stockTotal = new StockTotal();

        List<StockTotalDTO> stockTotalDTOList = Lists.newArrayList(stockTotalDTO);
        List<StockTotal> stockTotalList = Lists.newArrayList(stockTotal);
        stockTotalDTO.setOwnerId(1L);
        stockTotalDTO.setOwnerType(1L);
        stockTotalDTO.setProdOfferId(1L);
        stockTotalDTO.setStateId(1L);
        stockTotalDTO.setStatus(null);

        Mockito.when(mapper.toDtoBean(stockTotalList)).thenReturn(stockTotalDTOList);

        spyService.searchStockTotal(stockTotalDTO);
    }

    @Test
    public void testSearchStockTotal_3() throws  Exception {
        StockTotalServiceImpl spyService = PowerMockito.spy( stockTotalService);
        StockTotalDTO stockTotalDTO = new StockTotalDTO();
        StockTotal stockTotal = new StockTotal();

        List<StockTotalDTO> stockTotalDTOList = Lists.newArrayList(stockTotalDTO);
        List<StockTotal> stockTotalList = Lists.newArrayList(stockTotal);
        stockTotalDTO.setOwnerId(1L);
        stockTotalDTO.setOwnerType(1L);
        stockTotalDTO.setProdOfferId(1L);
        stockTotalDTO.setStateId(null);
        stockTotalDTO.setStatus(null);

        Mockito.when(mapper.toDtoBean(stockTotalList)).thenReturn(stockTotalDTOList);

        spyService.searchStockTotal(stockTotalDTO);
    }

    @Test
    public void testSearchStockTotal_4() throws  Exception {
        StockTotalServiceImpl spyService = PowerMockito.spy( stockTotalService);
        StockTotalDTO stockTotalDTO = new StockTotalDTO();
        StockTotal stockTotal = new StockTotal();

        List<StockTotalDTO> stockTotalDTOList = Lists.newArrayList(stockTotalDTO);
        List<StockTotal> stockTotalList = Lists.newArrayList(stockTotal);
        stockTotalDTO.setOwnerId(1L);
        stockTotalDTO.setOwnerType(1L);
        stockTotalDTO.setProdOfferId(null);
        stockTotalDTO.setStateId(null);
        stockTotalDTO.setStatus(null);

        Mockito.when(mapper.toDtoBean(stockTotalList)).thenReturn(stockTotalDTOList);

        spyService.searchStockTotal(stockTotalDTO);
    }

    @Test
    public void testSearchStockTotal_5() throws  Exception {
        StockTotalServiceImpl spyService = PowerMockito.spy( stockTotalService);
        StockTotalDTO stockTotalDTO = new StockTotalDTO();
        StockTotal stockTotal = new StockTotal();

        List<StockTotalDTO> stockTotalDTOList = Lists.newArrayList(stockTotalDTO);
        List<StockTotal> stockTotalList = Lists.newArrayList(stockTotal);
        stockTotalDTO.setOwnerId(1L);
        stockTotalDTO.setOwnerType(null);
        stockTotalDTO.setProdOfferId(null);
        stockTotalDTO.setStateId(null);
        stockTotalDTO.setStatus(null);

        Mockito.when(mapper.toDtoBean(stockTotalList)).thenReturn(stockTotalDTOList);

        spyService.searchStockTotal(stockTotalDTO);
    }

    @Test
    public void testSearchStockTotal_6() throws  Exception {
        StockTotalServiceImpl spyService = PowerMockito.spy( stockTotalService);
        StockTotalDTO stockTotalDTO = new StockTotalDTO();
        StockTotal stockTotal = new StockTotal();

        List<StockTotalDTO> stockTotalDTOList = Lists.newArrayList(stockTotalDTO);
        List<StockTotal> stockTotalList = Lists.newArrayList(stockTotal);
        stockTotalDTO.setOwnerId(null);
        stockTotalDTO.setOwnerType(null);
        stockTotalDTO.setProdOfferId(null);
        stockTotalDTO.setStateId(null);
        stockTotalDTO.setStatus(null);

        Mockito.when(mapper.toDtoBean(stockTotalList)).thenReturn(stockTotalDTOList);

        spyService.searchStockTotal(stockTotalDTO);
    }

    @Test
    public void testSearchStockTotal_7() throws  Exception {
        StockTotalServiceImpl spyService = PowerMockito.spy( stockTotalService);
        StockTotalDTO stockTotalDTO = null;
        StockTotal stockTotal = new StockTotal();

        List<StockTotalDTO> stockTotalDTOList = Lists.newArrayList(stockTotalDTO);
        List<StockTotal> stockTotalList = Lists.newArrayList(stockTotal);

        Mockito.when(mapper.toDtoBean(stockTotalList)).thenReturn(stockTotalDTOList);

        spyService.searchStockTotal(stockTotalDTO);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTotalServiceImpl.getTotalValueStock
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testGetTotalValueStock_1() throws  Exception {
        stockTotalService.getTotalValueStock(1L, "1");
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTotalServiceImpl.checkAction
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCheckAction_1() throws  Exception {
        stockTotalService.checkAction(new StockTotalDTO());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTotalServiceImpl.changeStockTotal
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testChangeStockTotal_1() throws  Exception {
        StockTotalServiceImpl spyService = PowerMockito.spy( stockTotalService);

        when(repository.findOneStockTotal(any())).thenReturn(null);
        spyService.changeStockTotal(1L, 1L, 1L, 1L, -1L, 1L, 1L,
                1L, 1L, 1L  , new Date(), "1", "1", Const.SourceType.STOCK_TRANS);
    }

    @Test
    public void testChangeStockTotal_2() throws  Exception {
        StockTotalServiceImpl spyService = PowerMockito.spy( stockTotalService);

        OptionSetValueDTO optionSetValueDTO = mock(OptionSetValueDTO.class);
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);

        when(optionSetValueDTO.getValue()).thenReturn("1");
        when(optionSetValueService.getByOptionSetCode(anyString())).thenReturn(lstConfigEnableBccs1);
        when(repository.findOneStockTotal(any())).thenReturn(null);
        spyService.changeStockTotal(1L, 1L, 1L, 1L, 1L, 1L, 1L,
                1L, 1L, 1L  , new Date(), "1", "1", Const.SourceType.STOCK_TRANS);
    }

    @Test
    public void testChangeStockTotal_3() throws  Exception {
        StockTotalServiceImpl spyService = PowerMockito.spy( stockTotalService);

        OptionSetValueDTO optionSetValueDTO = mock(OptionSetValueDTO.class);
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList();

        when(optionSetValueDTO.getValue()).thenReturn("1");
        when(optionSetValueService.getByOptionSetCode(anyString())).thenReturn(lstConfigEnableBccs1);
        when(repository.findOneStockTotal(any())).thenReturn(null);
        spyService.changeStockTotal(1L, 1L, 1L, 1L, 1L, 1L, 1L,
                1L, 1L, 1L  , new Date(), "1", "1", Const.SourceType.STOCK_TRANS);
    }

    @Test
    public void testChangeStockTotal_4() throws  Exception {
        StockTotalServiceImpl spyService = PowerMockito.spy( stockTotalService);

        OptionSetValueDTO optionSetValueDTO = mock(OptionSetValueDTO.class);
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);

        when(optionSetValueDTO.getValue()).thenReturn(null);
        when(optionSetValueService.getByOptionSetCode(anyString())).thenReturn(lstConfigEnableBccs1);
        when(repository.findOneStockTotal(any())).thenReturn(null);
        spyService.changeStockTotal(1L, 1L, 1L, 1L, 1L, 1L, 1L,
                1L, 1L, 1L  , new Date(), "1", "1", Const.SourceType.STOCK_TRANS);
    }

    @Test
    public void testChangeStockTotal_5() throws  Exception {
        StockTotalServiceImpl spyService = PowerMockito.spy( stockTotalService);

        OptionSetValueDTO optionSetValueDTO = mock(OptionSetValueDTO.class);
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);

        when(optionSetValueDTO.getValue()).thenReturn("2");
        when(optionSetValueService.getByOptionSetCode(anyString())).thenReturn(lstConfigEnableBccs1);
        when(repository.findOneStockTotal(any())).thenReturn(null);
        spyService.changeStockTotal(1L, 1L, 1L, 1L, 1L, 1L, 1L,
                1L, 1L, 1L  , new Date(), "1", "1", Const.SourceType.STOCK_TRANS);
    }

    @Test
    public void testChangeStockTotal_6() throws  Exception {
        //out 247
        StockTotalServiceImpl spyService = PowerMockito.spy( stockTotalService);

        OptionSetValueDTO optionSetValueDTO = mock(OptionSetValueDTO.class);
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);

        StockTotalDTO stockTotalDTO = new StockTotalDTO();
        List<StockTotalDTO> lsStockTotalDto = Lists.newArrayList(stockTotalDTO);

        when(optionSetValueDTO.getValue()).thenReturn("2");
        when(optionSetValueService.getByOptionSetCode(anyString())).thenReturn(lstConfigEnableBccs1);
        when(repository.findOneStockTotal(any())).thenReturn(lsStockTotalDto);
        spyService.changeStockTotal(1L, 1L, 1L, 1L, -10L, 1L, 1L,
                1L, 1L, 1L  , new Date(), "1", "1", Const.SourceType.STOCK_TRANS);
    }

    @Test
    public void testChangeStockTotal_7() throws  Exception {
        //out 247
        StockTotalServiceImpl spyService = PowerMockito.spy( stockTotalService);

        OptionSetValueDTO optionSetValueDTO = mock(OptionSetValueDTO.class);
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);

        StockTotalDTO stockTotalDTO = new StockTotalDTO();
        List<StockTotalDTO> lsStockTotalDto = Lists.newArrayList(stockTotalDTO);

        when(optionSetValueDTO.getValue()).thenReturn("2");
        when(optionSetValueService.getByOptionSetCode(anyString())).thenReturn(lstConfigEnableBccs1);
        when(repository.findOneStockTotal(any())).thenReturn(null);
        spyService.changeStockTotal(1L, 1L, 1L, 1L, 0L, 0L, 0L,
                1L, 1L, 1L  , new Date(), "1", "1", Const.SourceType.STOCK_TRANS);
    }

    @Test
    public void testChangeStockTotal_8() throws  Exception {
        //in 247
        StockTotalServiceImpl spyService = PowerMockito.spy( stockTotalService);

        OptionSetValueDTO optionSetValueDTO = mock(OptionSetValueDTO.class);
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);

        StockTotalDTO stockTotalDTO = new StockTotalDTO();
        List<StockTotalDTO> lsStockTotalDto = Lists.newArrayList(stockTotalDTO);

        when(optionSetValueDTO.getValue()).thenReturn("2");
        when(optionSetValueService.getByOptionSetCode(anyString())).thenReturn(lstConfigEnableBccs1);
        when(repository.findOneStockTotal(any())).thenReturn(null);
        spyService.changeStockTotal(1L, 1L, 1L, 1L, -1L, 1L, 1L,
                1L, 1L, 1L  , new Date(), "1", "1", Const.SourceType.STOCK_TRANS);
    }

    @Test
    public void testChangeStockTotal_9() throws  Exception {
        //in 247
        StockTotalServiceImpl spyService = PowerMockito.spy( stockTotalService);

        OptionSetValueDTO optionSetValueDTO = mock(OptionSetValueDTO.class);
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);

        StockTotalDTO stockTotalDTO = new StockTotalDTO();
        List<StockTotalDTO> lsStockTotalDto = Lists.newArrayList(stockTotalDTO);

        when(optionSetValueDTO.getValue()).thenReturn("2");
        when(optionSetValueService.getByOptionSetCode(anyString())).thenReturn(lstConfigEnableBccs1);
        when(repository.findOneStockTotal(any())).thenReturn(null);
        spyService.changeStockTotal(1L, 1L, 1L, 1L, 1L, -1L, 1L,
                1L, 1L, 1L  , new Date(), "1", "1", Const.SourceType.STOCK_TRANS);
    }

    @Test
    public void testChangeStockTotal_10() throws  Exception {
        //in 247
        StockTotalServiceImpl spyService = PowerMockito.spy( stockTotalService);

        OptionSetValueDTO optionSetValueDTO = mock(OptionSetValueDTO.class);
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);

        StockTotalDTO stockTotalDTO = new StockTotalDTO();
        List<StockTotalDTO> lsStockTotalDto = Lists.newArrayList(stockTotalDTO);

        when(optionSetValueDTO.getValue()).thenReturn("2");
        when(optionSetValueService.getByOptionSetCode(anyString())).thenReturn(lstConfigEnableBccs1);
        when(repository.findOneStockTotal(any())).thenReturn(null);
        spyService.changeStockTotal(1L, 1L, 1L, 1L, 1L, 1L, -1L,
                1L, 1L, 1L  , new Date(), "1", "1", Const.SourceType.STOCK_TRANS);
    }

    @Test
    public void testChangeStockTotal_11() throws  Exception {
        //in 247
        StockTotalServiceImpl spyService = PowerMockito.spy( stockTotalService);

        OptionSetValueDTO optionSetValueDTO = mock(OptionSetValueDTO.class);
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);

        StockTotalDTO stockTotalDTO = new StockTotalDTO();
        List<StockTotalDTO> lsStockTotalDto = Lists.newArrayList(stockTotalDTO);

        when(optionSetValueDTO.getValue()).thenReturn("2");
        when(optionSetValueService.getByOptionSetCode(anyString())).thenReturn(lstConfigEnableBccs1);
        when(repository.findOneStockTotal(any())).thenReturn(null);
        spyService.changeStockTotal(1L, 1L, 1L, 1L, 1L, 0L, 0L,
                1L, 1L, 1L  , new Date(), "1", "1", Const.SourceType.STOCK_TRANS);
    }

    @Test
    public void testChangeStockTotal_12() throws  Exception {
        //in 247
        StockTotalServiceImpl spyService = PowerMockito.spy( stockTotalService);

        OptionSetValueDTO optionSetValueDTO = mock(OptionSetValueDTO.class);
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);

        StockTotalDTO stockTotalDTO = new StockTotalDTO();
        List<StockTotalDTO> lsStockTotalDto = Lists.newArrayList(stockTotalDTO);

        when(optionSetValueDTO.getValue()).thenReturn("2");
        when(optionSetValueService.getByOptionSetCode(anyString())).thenReturn(lstConfigEnableBccs1);
        when(repository.findOneStockTotal(any())).thenReturn(null);
        spyService.changeStockTotal(1L, 1L, 1L, 1L, 0L, 1L, 0L,
                1L, 1L, 1L  , new Date(), "1", "1", Const.SourceType.STOCK_TRANS);
    }

    @Test
    public void testChangeStockTotal_13() throws  Exception {
        //in 247
        StockTotalServiceImpl spyService = PowerMockito.spy( stockTotalService);

        OptionSetValueDTO optionSetValueDTO = mock(OptionSetValueDTO.class);
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);

        StockTotalDTO stockTotalDTO = new StockTotalDTO();
        List<StockTotalDTO> lsStockTotalDto = Lists.newArrayList(stockTotalDTO);

        when(optionSetValueDTO.getValue()).thenReturn("2");
        when(optionSetValueService.getByOptionSetCode(anyString())).thenReturn(lstConfigEnableBccs1);
        when(repository.findOneStockTotal(any())).thenReturn(null);
        spyService.changeStockTotal(1L, 1L, 1L, 1L, 0L, 0L, 1L,
                1L, 1L, 1L  , new Date(), "1", "1", Const.SourceType.STOCK_TRANS);
    }

    @Test
    public void testChangeStockTotal_14() throws  Exception {
        //in 299
        //out 295
        StockTotalServiceImpl spyService = PowerMockito.spy( stockTotalService);

        OptionSetValueDTO optionSetValueDTO = mock(OptionSetValueDTO.class);
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);

        StockTotalDTO stockTotalDTO = mock(StockTotalDTO.class);
        List<StockTotalDTO> lsStockTotalDto = Lists.newArrayList(stockTotalDTO);

        when(stockTotalDTO.getCurrentQuantity()).thenReturn(3L);
        when(stockTotalDTO.getAvailableQuantity()).thenReturn(-3L);
        when(optionSetValueDTO.getValue()).thenReturn("2");
        when(optionSetValueService.getByOptionSetCode(anyString())).thenReturn(lstConfigEnableBccs1);
        when(repository.findOneStockTotal(any())).thenReturn(lsStockTotalDto);
        spyService.changeStockTotal(1L, 1L, 1L, 1L, 1L, 1L, 1L,
                1L, 1L, 1L  , new Date(), "1", "1", Const.SourceType.STOCK_TRANS);
    }

    @Test
    public void testChangeStockTotal_15() throws  Exception {
        //out 299
        StockTotalServiceImpl spyService = PowerMockito.spy( stockTotalService);

        OptionSetValueDTO optionSetValueDTO = mock(OptionSetValueDTO.class);
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);

        StockTotalDTO stockTotalDTO = mock(StockTotalDTO.class);
        List<StockTotalDTO> lsStockTotalDto = Lists.newArrayList(stockTotalDTO);

        when(stockTotalDTO.getCurrentQuantity()).thenReturn(3L);
        when(stockTotalDTO.getAvailableQuantity()).thenReturn(3L);
        when(optionSetValueDTO.getValue()).thenReturn("2");
        when(optionSetValueService.getByOptionSetCode(anyString())).thenReturn(lstConfigEnableBccs1);
        when(repository.findOneStockTotal(any())).thenReturn(lsStockTotalDto);
        spyService.changeStockTotal(1L, 1L, 1L, 1L, 1L, 1L, 1L,
                1L, 1L, 1L  , new Date(), "1", "1", Const.SourceType.STOCK_TRANS);
    }

    @Test
    public void testChangeStockTotal_16() throws  Exception {
        //in 241
        StockTotalServiceImpl spyService = PowerMockito.spy( stockTotalService);

        OptionSetValueDTO optionSetValueDTO = mock(OptionSetValueDTO.class);
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);

        StockTotalDTO stockTotalDTO = mock(StockTotalDTO.class);
        List<StockTotalDTO> lsStockTotalDto = Lists.newArrayList(stockTotalDTO);

        when(stockTotalDTO.getCurrentQuantity()).thenReturn(3L);
        when(stockTotalDTO.getAvailableQuantity()).thenReturn(3L);
        when(optionSetValueDTO.getValue()).thenReturn("2");
        when(optionSetValueService.getByOptionSetCode(anyString())).thenReturn(lstConfigEnableBccs1);
        when(repository.findOneStockTotal(any())).thenThrow(new Exception());
        spyService.changeStockTotal(1L, 1L, 1L, 1L, 1L, 1L, 1L,
                1L, 1L, 1L  , new Date(), "1", "1", Const.SourceType.STOCK_TRANS);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTotalServiceImpl.getStockTotalDetail
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void testGetStockTotalDetail_1() throws  Exception {
        stockTotalService.getStockTotalDetail(null, 1L, "1");
    }

    @Test
    public void testGetStockTotalDetail_2() throws  Exception {
        StockTotalWsDTO stockTotalWsDTO = new StockTotalWsDTO();
        StockTransSerialDTO stockTransSerialDTO = mock(StockTransSerialDTO.class);
        List<StockTotalWsDTO> stockTotalDTOList = Lists.newArrayList(stockTotalWsDTO);
        List<StockTransSerialDTO> lstSerial = Lists.newArrayList(stockTransSerialDTO);

        when(repositoryInventory.getStockTotalShop(anyLong(), anyLong(), anyLong(), anyString())).thenReturn(stockTotalDTOList);
        when(repositoryInventory.getStockTransSerial(stockTotalWsDTO, 100)).thenReturn(lstSerial);
        when(stockTransSerialDTO.getQuantity()).thenReturn(1L);
        stockTotalService.getStockTotalDetail(1L, 1L, "1");
    }

    @Test
    public void testGetStockTotalDetail_3() throws  Exception {
        StockTotalWsDTO stockTotalWsDTO = new StockTotalWsDTO();
        List<StockTotalWsDTO> stockTotalDTOList = Lists.newArrayList(stockTotalWsDTO);

        when(optionSetValueService.getValueByNameFromOptionSetValue(anyString())).thenReturn(10L);
        when(repositoryInventory.getStockTotalShop(anyLong(), anyLong(), anyLong(), anyString())).thenReturn(stockTotalDTOList);
        stockTotalService.getStockTotalDetail(1L, 1L, "1");
    }

    @Test
    public void testGetStockTotalDetail_4() throws  Exception {
        //out 466
        StockTotalWsDTO stockTotalWsDTO = new StockTotalWsDTO();
        StockTransSerialDTO stockTransSerialDTO = mock(StockTransSerialDTO.class);
        List<StockTotalWsDTO> stockTotalDTOList = Lists.newArrayList(stockTotalWsDTO);
        List<StockTransSerialDTO> lstSerial = Lists.newArrayList(stockTransSerialDTO);

        when(repositoryInventory.getStockTotalShop(anyLong(), anyLong(), anyLong(), anyString())).thenReturn(stockTotalDTOList);
        when(repositoryInventory.getStockTransSerial(stockTotalWsDTO, 100)).thenReturn(lstSerial);
        when(stockTransSerialDTO.getQuantity()).thenReturn(null);
        stockTotalService.getStockTotalDetail(1L, 1L, "1");
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTotalServiceImpl.getStockTotalForProcessStock
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testGetStockTotalForProcessStock_1() throws  Exception {
        //in 485
        StockTotalServiceImpl spyService = PowerMockito.spy( stockTotalService);
        StockTotal stockTotal = mock(StockTotal.class);
        StockTotalDTO stockTotalDTO = new StockTotalDTO();
        List<StockTotal> stockTotalList = Lists.newArrayList(stockTotal);
        List<StockTotalDTO> stockTotalDTOList = Lists.newArrayList(stockTotalDTO);
        Mockito.when(mapper.toDtoBean(stockTotalList)).thenReturn(stockTotalDTOList);
        setFinalStatic(StockTotalServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        List<StockTotalDTO> lstResult = Lists.newArrayList(stockTotalDTO);

        when(spyService.findByFilter(any())).thenReturn(lstResult);
        spyService.getStockTotalForProcessStock(1L,1L,1L,1L);
    }

    @Test
    public void testGetStockTotalForProcessStock_2() throws  Exception {
        //out 485
        StockTotalServiceImpl spyService = PowerMockito.spy( stockTotalService);
        StockTotal stockTotal = mock(StockTotal.class);
        StockTotalDTO stockTotalDTO = new StockTotalDTO();
        List<StockTotal> stockTotalList = Lists.newArrayList(stockTotal);
        List<StockTotalDTO> stockTotalDTOList = Lists.newArrayList(stockTotalDTO);
        Mockito.when(mapper.toDtoBean(stockTotalList)).thenReturn(stockTotalDTOList);
        setFinalStatic(StockTotalServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        List<StockTotalDTO> lstResult = Lists.newArrayList(stockTotalDTO);

        when(spyService.findByFilter(any())).thenReturn(null);
        spyService.getStockTotalForProcessStock(1L,1L,1L,1L);
    }

    @Test
    public void testGetStockTotalForProcessStock_3() throws  Exception {
        //out 485
        StockTotalServiceImpl spyService = PowerMockito.spy( stockTotalService);
        StockTotal stockTotal = mock(StockTotal.class);
        StockTotalDTO stockTotalDTO = new StockTotalDTO();
        List<StockTotal> stockTotalList = Lists.newArrayList(stockTotal);
        List<StockTotalDTO> stockTotalDTOList = Lists.newArrayList(stockTotalDTO);
        Mockito.when(mapper.toDtoBean(stockTotalList)).thenReturn(stockTotalDTOList);
        setFinalStatic(StockTotalServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        List<StockTotalDTO> lstResult = Lists.newArrayList();

        when(spyService.findByFilter(any())).thenReturn(lstResult);
        spyService.getStockTotalForProcessStock(1L,1L,1L,1L);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTotalServiceImpl.getStockTotalAvailable
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testGetStockTotalAvailable_1() throws  Exception {
        stockTotalService.getStockTotalAvailable(2L,1L);
    }

    @Test
    public void testGetStockTotalAvailable_2() throws  Exception {
        stockTotalService.getStockTotalAvailable(1L,1L);
    }

    @Test
    public void testGetStockTotalAvailable_3() throws  Exception {
        stockTotalService.getStockTotalAvailable(3L,1L);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTotalServiceImpl.getTotalQuantityStockTotal
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testGetTotalQuantityStockTotal_1() throws  Exception {
        stockTotalService.getTotalQuantityStockTotal(1L,1L);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTotalServiceImpl.checkStockTotal
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCheckStockTotal_1() throws  Exception {
        StockTotalServiceImpl spyService = PowerMockito.spy( stockTotalService);
        StockTotal stockTotal = mock(StockTotal.class);
        StockTotalDTO stockTotalDTO = new StockTotalDTO();
        List<StockTotal> stockTotalList = Lists.newArrayList(stockTotal);
        List<StockTotalDTO> stockTotalDTOList = Lists.newArrayList(stockTotalDTO);
        Mockito.when(mapper.toDtoBean(stockTotalList)).thenReturn(stockTotalDTOList);
        setFinalStatic(StockTotalServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        List<StockTotalDTO> lstResult = Lists.newArrayList(stockTotalDTO);

        when(spyService.findByFilter(any())).thenReturn(null);

        spyService.checkStockTotal(1L,1L,1L);
    }

    @Test
    public void testCheckStockTotal_2() throws  Exception {
        StockTotalServiceImpl spyService = PowerMockito.spy( stockTotalService);
        StockTotal stockTotal = mock(StockTotal.class);
        StockTotalDTO stockTotalDTO = mock(StockTotalDTO.class);
        List<StockTotal> stockTotalList = Lists.newArrayList(stockTotal);
        List<StockTotalDTO> stockTotalDTOList = Lists.newArrayList(stockTotalDTO);
        Mockito.when(mapper.toDtoBean(stockTotalList)).thenReturn(stockTotalDTOList);
        setFinalStatic(StockTotalServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        List<StockTotalDTO> lstResult = Lists.newArrayList(stockTotalDTO);

        when(spyService.findByFilter(any())).thenReturn(lstResult);
        when(stockTotalDTO.getAvailableQuantity()).thenReturn(0L);

        spyService.checkStockTotal(1L,1L,1L);
    }

    @Test
    public void testCheckStockTotal_3() throws  Exception {
        StockTotalServiceImpl spyService = PowerMockito.spy( stockTotalService);
        StockTotal stockTotal = mock(StockTotal.class);
        StockTotalDTO stockTotalDTO = mock(StockTotalDTO.class);
        List<StockTotal> stockTotalList = Lists.newArrayList(stockTotal);
        List<StockTotalDTO> stockTotalDTOList = Lists.newArrayList(stockTotalDTO);
        Mockito.when(mapper.toDtoBean(stockTotalList)).thenReturn(stockTotalDTOList);
        setFinalStatic(StockTotalServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        List<StockTotalDTO> lstResult = Lists.newArrayList(stockTotalDTO);

        when(spyService.findByFilter(any())).thenReturn(lstResult);
        when(stockTotalDTO.getAvailableQuantity()).thenReturn(1L);
        when(stockTotalDTO.getCurrentQuantity()).thenReturn(0L);

        spyService.checkStockTotal(1L,1L,1L);
    }

    @Test
    public void testCheckStockTotal_4() throws  Exception {
        StockTotalServiceImpl spyService = PowerMockito.spy( stockTotalService);
        StockTotal stockTotal = mock(StockTotal.class);
        StockTotalDTO stockTotalDTO = mock(StockTotalDTO.class);
        List<StockTotal> stockTotalList = Lists.newArrayList(stockTotal);
        List<StockTotalDTO> stockTotalDTOList = Lists.newArrayList(stockTotalDTO);
        Mockito.when(mapper.toDtoBean(stockTotalList)).thenReturn(stockTotalDTOList);
        setFinalStatic(StockTotalServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        List<StockTotalDTO> lstResult = Lists.newArrayList(stockTotalDTO);

        when(spyService.findByFilter(any())).thenReturn(lstResult);
        when(stockTotalDTO.getAvailableQuantity()).thenReturn(2L);
        when(stockTotalDTO.getCurrentQuantity()).thenReturn(2L);

        spyService.checkStockTotal(1L,1L,1L);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTotalServiceImpl.checkStockTotalIm1
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCheckStockTotalIm1_1() throws  Exception {
        StockTotalServiceImpl spyService = PowerMockito.spy( stockTotalService);
        StockTotal stockTotal = mock(StockTotal.class);
        StockTotalDTO stockTotalDTO = new StockTotalDTO();
        List<StockTotal> stockTotalList = Lists.newArrayList(stockTotal);
        List<StockTotalDTO> stockTotalDTOList = Lists.newArrayList(stockTotalDTO);
        Mockito.when(mapper.toDtoBean(stockTotalList)).thenReturn(stockTotalDTOList);
        setFinalStatic(StockTotalServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        List<StockTotalDTO> lstResult = Lists.newArrayList(stockTotalDTO);

        when(spyService.findByFilter(any())).thenReturn(null);

        spyService.checkStockTotalIm1(1L,1L,1L);
    }

    @Test
    public void testCheckStockTotalIm1_2() throws  Exception {
        StockTotalServiceImpl spyService = PowerMockito.spy( stockTotalService);

        StockTotalIm1DTO stockTotalIm1DTO = mock(StockTotalIm1DTO.class);
        when(stockTotalIm1DTO.getQuantity()).thenReturn(0L);
        when(stockTotalIm1Service.getStockTotalForProcessStock(anyLong(), anyLong(), anyLong(), anyLong())).thenReturn(stockTotalIm1DTO);

        spyService.checkStockTotalIm1(1L,1L,1L);
    }

    @Test
    public void testCheckStockTotalIm1_3() throws  Exception {
        StockTotalServiceImpl spyService = PowerMockito.spy( stockTotalService);

        StockTotalIm1DTO stockTotalIm1DTO = mock(StockTotalIm1DTO.class);
        when(stockTotalIm1DTO.getQuantity()).thenReturn(1L);
        when(stockTotalIm1DTO.getQuantityIssue()).thenReturn(0L);
        when(stockTotalIm1Service.getStockTotalForProcessStock(anyLong(), anyLong(), anyLong(), anyLong())).thenReturn(stockTotalIm1DTO);

        spyService.checkStockTotalIm1(1L,1L,1L);
    }

    @Test
    public void testCheckStockTotalIm1_4() throws  Exception {
        StockTotalServiceImpl spyService = PowerMockito.spy( stockTotalService);

        StockTotalIm1DTO stockTotalIm1DTO = mock(StockTotalIm1DTO.class);
        when(stockTotalIm1DTO.getQuantity()).thenReturn(2L);
        when(stockTotalIm1DTO.getQuantityIssue()).thenReturn(2L);
        when(stockTotalIm1Service.getStockTotalForProcessStock(anyLong(), anyLong(), anyLong(), anyLong())).thenReturn(stockTotalIm1DTO);

        spyService.checkStockTotalIm1(1L,1L,1L);
    }


    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTotalServiceImpl.checkHaveProductInStockTotal
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCheckHaveProductInStockTotal_1() throws  Exception {
        stockTotalService.checkHaveProductInStockTotal(1L, 1L);
    }

    static void setFinalStatic(Field field, Object newValue, final Object targetObject) throws Exception {
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(targetObject, newValue);
    }
}