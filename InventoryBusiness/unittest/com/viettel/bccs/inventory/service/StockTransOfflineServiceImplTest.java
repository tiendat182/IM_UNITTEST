package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.dto.PartnerContractDTO;
import com.viettel.bccs.inventory.dto.StockTransOfflineDTO;
import com.viettel.bccs.inventory.model.PartnerContract;
import com.viettel.bccs.inventory.model.StockTrans;
import com.viettel.bccs.inventory.model.StockTransOffline;
import com.viettel.bccs.inventory.repo.*;
import com.viettel.bccs.sale.repo.SaleTransDetailRepo;
import com.viettel.bccs.sale.repo.SaleTransRepo;
import com.viettel.fw.common.util.DateUtil;
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
import java.util.List;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author DatLT
 * @date 15/10/2017
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({DbUtil.class, StockTransOfflineServiceImpl.class, BaseServiceImpl.class, StockTransOfflineService.class, DateUtil.class})
public class StockTransOfflineServiceImplTest {
    @InjectMocks
    StockTransOfflineServiceImpl stockTransOfflineService;
    @Mock
    private final BaseMapper<StockTransOffline, StockTransOfflineDTO> mapper = new BaseMapper<>(StockTransOffline.class, StockTransOfflineDTO.class);
    @Mock
    private final BaseMapper<PartnerContract, PartnerContractDTO> mapperPartnerContract = new BaseMapper<>(PartnerContract.class, PartnerContractDTO.class);
    @Mock
    private StockTransOfflineRepo repository;
    @Mock
    private StockTransRepo repoStockTrans;
    @Mock
    private StockTransActionRepo repoStockTransAction;
    @Mock
    private StockTransFullOfflineRepo repoStockTransFullOfflineRepo;
    @Mock
    private StockTransSerialOfflineRepo repoStockTransSerialOffline;
    @Mock
    private ProductOfferingRepo repoProductOffering;
    @Mock
    private StockTransDetailRepo repoStockTransDetail;
    @Mock
    private StaffRepo repoStaff;
    @Mock
    private ShopRepo repoShop;
    @Mock
    private SaleTransRepo repoSaleTransRepo;
    @Mock
    private SaleTransDetailRepo repoSaleTransDetail;
    @Mock
    private ChannelTypeRepo repoChannelType;
    @Mock
    private EntityManager em;
    @Mock
    private EntityManager emSale;
    @Mock
    private EntityManager emAnypay;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransOfflineServiceImpl.count
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCount_1() throws Exception {
        List<FilterRequest> filters = Lists.newArrayList();
        Mockito.when(repository.count(any())).thenReturn(1L);
        Assert.assertEquals(1L, stockTransOfflineService.count(filters).longValue());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransOfflineServiceImpl.findOne
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindOne_1() throws  Exception {
        StockTransOfflineServiceImpl spyService = PowerMockito.spy( stockTransOfflineService);
        StockTransOffline stockTransOffline = mock(StockTransOffline.class);
        StockTransOfflineDTO stockTransOfflineDTO = new StockTransOfflineDTO();
        Mockito.when(repository.findOne(anyLong())).thenReturn(stockTransOffline);
        setFinalStatic(StockTransOfflineServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(mapper.toDtoBean(stockTransOffline)).thenReturn(stockTransOfflineDTO);
        spyService.findOne(1L);
        Assert.assertNotNull(stockTransOfflineDTO);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransOfflineServiceImpl.findAll
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Test
    public void testFindAll_1() throws  Exception {
        StockTransOfflineServiceImpl spyService = PowerMockito.spy( stockTransOfflineService);
        StockTransOffline stockTransOffline = mock(StockTransOffline.class);
        StockTransOfflineDTO stockTransOfflineDTO = new StockTransOfflineDTO();
        List<StockTransOfflineDTO> stockTransOfflineDTOList = Lists.newArrayList(stockTransOfflineDTO);
        List<StockTransOffline> stockTransOfflineList = Lists.newArrayList(stockTransOffline);
        Mockito.when(repository.findAll()).thenReturn(stockTransOfflineList);
        Mockito.when(mapper.toDtoBean(stockTransOfflineList)).thenReturn(stockTransOfflineDTOList);
        setFinalStatic(StockTransOfflineServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findAll();
        Assert.assertNotNull(stockTransOfflineDTOList);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransOfflineServiceImpl.findByFilter
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Test
    public void testFindByFilter_1() throws  Exception {
        FilterRequest filterRequest = new FilterRequest();
        List<FilterRequest> filterRequestList = Lists.newArrayList(filterRequest);
        StockTransOfflineServiceImpl spyService = PowerMockito.spy( stockTransOfflineService);
        StockTransOffline stockTransOffline = mock(StockTransOffline.class);
        StockTransOfflineDTO stockTransOfflineDTO = new StockTransOfflineDTO();
        List<StockTransOfflineDTO> stockTransOfflineDTOList = Lists.newArrayList(stockTransOfflineDTO);
        List<StockTransOffline> stockTransOfflineList = Lists.newArrayList(stockTransOffline);
        Mockito.when(mapper.toDtoBean(stockTransOfflineList)).thenReturn(stockTransOfflineDTOList);
        setFinalStatic(StockTransOfflineServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findByFilter(filterRequestList);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for StockTransOfflineServiceImpl.save method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testSave_1() throws Exception {
        StockTransOfflineServiceImpl spyService = PowerMockito.spy( stockTransOfflineService);
        StockTransOffline stockTransOffline = mock(StockTransOffline.class);
        StockTransOfflineDTO stockTransOfflineDTO = new StockTransOfflineDTO();
        setFinalStatic(StockTransOfflineServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(mapper.toDtoBean(repository.save(mapper.toPersistenceBean(stockTransOfflineDTO)))).thenReturn(stockTransOfflineDTO);
        spyService.save(stockTransOfflineDTO);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for StockTransOfflineServiceImpl.processExpStockOffine method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testProcessExpStockOffine_1() throws Exception {
        stockTransOfflineService.processExpStockOffine(new StockTrans());
    }


    /** -------------------------------------------------------------------- **/
    /** --- Test for StockTransOfflineServiceImpl.getStockTransOfflineById method --- **/
    /** -------------------------------------------------------------------- **/
    @Test
    public void testGetStockTransOfflineById_1() throws Exception {
        StockTransOfflineServiceImpl spyService = PowerMockito.spy( stockTransOfflineService);
        StockTransOffline stockTransOffline = mock(StockTransOffline.class);
        StockTransOfflineDTO stockTransOfflineDTO = new StockTransOfflineDTO();
        List<StockTransOfflineDTO> stockTransOfflineDTOList = Lists.newArrayList(stockTransOfflineDTO);
        List<StockTransOffline> stockTransOfflineList = Lists.newArrayList(stockTransOffline);
        Mockito.when(mapper.toDtoBean(stockTransOfflineList)).thenReturn(stockTransOfflineDTOList);
        setFinalStatic(StockTransOfflineServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);

        when(spyService.findByFilter(anyList())).thenReturn(stockTransOfflineDTOList);
        spyService.getStockTransOfflineById(1L);
    }

    @Test
    public void testGetStockTransOfflineById_2() throws Exception {
        StockTransOfflineServiceImpl spyService = PowerMockito.spy( stockTransOfflineService);
        StockTransOffline stockTransOffline = mock(StockTransOffline.class);
        StockTransOfflineDTO stockTransOfflineDTO = new StockTransOfflineDTO();
        List<StockTransOfflineDTO> stockTransOfflineDTOList = Lists.newArrayList(stockTransOfflineDTO);
        List<StockTransOffline> stockTransOfflineList = Lists.newArrayList(stockTransOffline);
        Mockito.when(mapper.toDtoBean(stockTransOfflineList)).thenReturn(stockTransOfflineDTOList);
        setFinalStatic(StockTransOfflineServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);

        when(spyService.findByFilter(anyList())).thenReturn(null);
        spyService.getStockTransOfflineById(1L);
    }

    static void setFinalStatic(Field field, Object newValue, final Object targetObject) throws Exception {
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(targetObject, newValue);
    }
}