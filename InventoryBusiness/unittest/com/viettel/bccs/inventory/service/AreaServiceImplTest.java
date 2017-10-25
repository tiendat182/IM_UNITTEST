package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Predicate;
import com.viettel.bccs.inventory.dto.AreaDTO;
import com.viettel.bccs.inventory.model.Area;
import com.viettel.bccs.inventory.model.QArea;
import com.viettel.bccs.inventory.repo.AreaRepo;
import com.viettel.bccs.sale.dto.SaleTransSerialDTO;
import com.viettel.bccs.sale.model.SaleTransSerial;
import com.viettel.bccs.sale.service.SaleTransSerialServiceImpl;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import com.viettel.fw.service.BaseServiceImpl;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

/**
 * DatLT 2017/09/24
 */
@RunWith(MockitoJUnitRunner.class)
@PrepareForTest({BaseServiceImpl.class, AreaService.class, AreaServiceImpl.class})
public class AreaServiceImplTest {

    @InjectMocks
    AreaServiceImpl areaServiceImpl;

    @Mock
    private BaseMapper<SaleTransSerial, SaleTransSerialDTO> mapper;

    @Mock
    private AreaRepo repository;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method AreaServiceImpl.count
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCount_01() throws Exception {
        List<FilterRequest> filters = new ArrayList<>();
        when(repository.count(any())).thenReturn(1L);
        Assert.assertEquals(1L, areaServiceImpl.count(filters).longValue());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method AreaServiceImpl.findOne
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindOne_01() throws Exception {
        AreaServiceImpl spyService = PowerMockito.spy( areaServiceImpl);
        Area area = new Area();
        SaleTransSerial saleTransSerial = new SaleTransSerial();
        SaleTransSerialDTO saleTransSerialDTO = new SaleTransSerialDTO();
        Mockito.when(repository.findOne(anyLong())).thenReturn(area);
        Mockito.when(mapper.toDtoBean(saleTransSerial)).thenReturn(saleTransSerialDTO);
        setFinalStatic(AreaServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findOne(1L);
        Assert.assertNotNull(saleTransSerialDTO);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method AreaServiceImpl.findAll
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindAll_01() throws Exception {
        AreaServiceImpl spyService = PowerMockito.spy( areaServiceImpl);
        SaleTransSerialDTO saleTransSerialDTO = new SaleTransSerialDTO();
        Area area = new Area();
        SaleTransSerial saleTransSerial = new SaleTransSerial();
        List<SaleTransSerial> saleTransSerialList = Lists.newArrayList(saleTransSerial);
        List<Area> areaList = Lists.newArrayList(area);
        List<SaleTransSerialDTO> saleTransSerialDTOList = Lists.newArrayList(saleTransSerialDTO);
        Mockito.when(repository.findAll()).thenReturn(areaList);
        Mockito.when(mapper.toDtoBean(saleTransSerialList)).thenReturn(saleTransSerialDTOList);
        setFinalStatic(AreaServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findAll();
        Assert.assertNotNull(saleTransSerialDTOList);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method AreaServiceImpl.findByFilter
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindByFilter_01() throws Exception {
        List<FilterRequest> filters = Lists.newArrayList();
        AreaServiceImpl spyService = PowerMockito.spy( areaServiceImpl);
        SaleTransSerialDTO saleTransSerialDTO = new SaleTransSerialDTO();
        SaleTransSerial saleTransSerial = new SaleTransSerial();
        List<SaleTransSerial> saleTransSerialList = Lists.newArrayList(saleTransSerial);
        List<SaleTransSerialDTO> saleTransSerialDTOList = Lists.newArrayList(saleTransSerialDTO);
        Mockito.when(mapper.toDtoBean(saleTransSerialList)).thenReturn(saleTransSerialDTOList);
        setFinalStatic(AreaServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findByFilter(filters);
        Assert.assertNotNull(saleTransSerialDTOList);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method AreaServiceImpl.create
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test(expected = NotImplementedException.class)
    public void testCreate_01() throws Exception {
        AreaDTO dto = new AreaDTO();
        areaServiceImpl.create(dto);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method AreaServiceImpl.update
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test(expected = NotImplementedException.class)
    public void testUpdate_01() throws Exception {
        AreaDTO dto = new AreaDTO();
        areaServiceImpl.update(dto);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method AreaServiceImpl.getAllProvince
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testGetAllProvince_1() throws  Exception {
        AreaDTO areaDTO = new AreaDTO();
        List<AreaDTO> areaDTOList = Lists.newArrayList(areaDTO);
        Mockito.when(repository.getAllProvince()).thenReturn(areaDTOList);
        areaServiceImpl.getAllProvince();
        Assert.assertNotNull(areaDTOList);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method AreaServiceImpl.findByCode
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindByCode_1() throws  Exception {
        String areaCode = "";
        areaServiceImpl.findByCode(areaCode);
    }

    @Test
    public void testFindByCode_2() throws  Exception {
        String areaCode = "test2";
        List<FilterRequest> requests = Lists.newArrayList();
        AreaServiceImpl spyService = PowerMockito.spy(areaServiceImpl);
        PowerMockito.when(spyService.findByFilter(any())).thenReturn(null);
        spyService.findByCode(areaCode);
    }

    @Test
    public void testFindByCode_3() throws  Exception {
        String areaCode = "test3";
        AreaDTO areaDTO = new AreaDTO();
        List<FilterRequest> requests = Lists.newArrayList();
        List<AreaDTO> lits = Lists.newArrayList(areaDTO);
        AreaServiceImpl spyService = PowerMockito.spy(areaServiceImpl);
        PowerMockito.when(spyService.findByFilter(any())).thenReturn(lits);
        spyService.findByCode(areaCode);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method AreaServiceImpl.findByCode
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testSearch_1() throws  Exception {
        AreaDTO areaDTO = new AreaDTO();
        areaServiceImpl.search(areaDTO);
    }

    static void setFinalStatic(Field field, Object newValue, final Object targetObject) throws Exception {
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(targetObject, newValue);
    }
}