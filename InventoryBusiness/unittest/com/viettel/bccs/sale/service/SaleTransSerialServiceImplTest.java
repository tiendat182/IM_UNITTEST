package com.viettel.bccs.sale.service;

import com.google.common.collect.Lists;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.Visitor;
import com.viettel.bccs.inventory.service.BaseStock.BaseStockTransVofficeService;
import com.viettel.bccs.sale.dto.SaleTransDTO;
import com.viettel.bccs.sale.dto.SaleTransSerialDTO;
import com.viettel.bccs.sale.model.SaleTrans;
import com.viettel.bccs.sale.model.SaleTransSerial;
import com.viettel.bccs.sale.repo.SaleTransSerialRepo;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;

/**
 * Created by TruNV on 9/2/2017.
 */
@RunWith(PowerMockRunner.class)
public class SaleTransSerialServiceImplTest {

    @InjectMocks
    private SaleTransSerialServiceImpl saleTransSerialServiceImpl;

    @Mock
    private SaleTransSerialRepo repo;

    @Mock
    private SaleTransSerialRepo repository;

    @Mock
    private BaseMapper<SaleTransSerial, SaleTransSerialDTO> detailMapper;

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for saleTransSerialServiceImpl.save method ---------------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testSave_1() throws Exception {
        SaleTransSerialDTO saleTransSerial = new SaleTransSerialDTO();
        saleTransSerialServiceImpl.save(null);
    }

    @Test
    public void testSave_2() throws Exception {
        SaleTransSerialServiceImpl spyTemp = PowerMockito.spy(saleTransSerialServiceImpl);
        SaleTransSerialDTO saleTransSerial = new SaleTransSerialDTO();
        SaleTransSerial saleTransSerial1 = new SaleTransSerial();
        SaleTransSerial saleTransSerial2 = new SaleTransSerial();
        Mockito.when(detailMapper.toPersistenceBean(saleTransSerial)).thenReturn(saleTransSerial1);
        setFinalStatic(SaleTransSerialServiceImpl.class.getDeclaredField("mapper"), detailMapper, spyTemp);
        Mockito.when(repository.save(saleTransSerial1)).thenReturn(saleTransSerial2);
        spyTemp.save(saleTransSerial);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for saleTransSerialServiceImpl.count method --------------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testCount_1() throws Exception {

        List<FilterRequest> filters = Lists.newArrayList();
        Mockito.when(repository.count(any())).thenReturn(1L);
        long result = saleTransSerialServiceImpl.count(filters);

        Assert.assertEquals(0L, result);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for saleTransSerialServiceImpl.findOne method ------------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testFindOne_1() throws Exception {
        SaleTransSerialServiceImpl spyTemp = PowerMockito.spy(saleTransSerialServiceImpl);
        SaleTransSerial saleTransSerial = new SaleTransSerial();
        Mockito.when(repository.findOne(anyLong())).thenReturn(saleTransSerial);
        SaleTransSerialDTO saleTransSerialDTO = new SaleTransSerialDTO();
        Mockito.when(detailMapper.toDtoBean(saleTransSerial)).thenReturn(saleTransSerialDTO);
        setFinalStatic(SaleTransSerialServiceImpl.class.getDeclaredField("mapper"), detailMapper, spyTemp);
        spyTemp.findOne(1L);
        Assert.assertNotNull(saleTransSerialDTO);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for saleTransSerialServiceImpl.findAll method ------------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testFindAll_1() throws Exception {

        SaleTransSerialServiceImpl spyTemp = PowerMockito.spy(saleTransSerialServiceImpl);
        SaleTransSerial saleTransSerial = new SaleTransSerial();
        List<SaleTransSerial> saleTransSerials = Lists.newArrayList();
        List<SaleTransSerialDTO> saleTransSerials2 = Lists.newArrayList();
        SaleTransSerialDTO saleTransSerialDTO = new SaleTransSerialDTO();
        Mockito.when(detailMapper.toDtoBean(saleTransSerials)).thenReturn(saleTransSerials2);
        setFinalStatic(SaleTransSerialServiceImpl.class.getDeclaredField("mapper"), detailMapper, spyTemp);
        spyTemp.findAll();
        Assert.assertNotNull(saleTransSerials2);

    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for saleTransSerialServiceImpl.findByFilter method -------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testFindByFilter_1() throws Exception {
        List<FilterRequest> filters = Lists.newArrayList();
        SaleTransSerialServiceImpl spyTemp = PowerMockito.spy(saleTransSerialServiceImpl);
        SaleTransSerial saleTransSerial = new SaleTransSerial();
        List<SaleTransSerial> saleTransSerials = Lists.newArrayList();
        List<SaleTransSerialDTO> saleTransSerials2 = Lists.newArrayList();
        SaleTransSerialDTO saleTransSerialDTO = new SaleTransSerialDTO();
        Mockito.when(detailMapper.toDtoBean(saleTransSerials)).thenReturn(saleTransSerials2);
        setFinalStatic(SaleTransSerialServiceImpl.class.getDeclaredField("mapper"), detailMapper, spyTemp);
        spyTemp.findByFilter(filters);
        Assert.assertNotNull(saleTransSerials2);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for saleTransSerialServiceImpl.createNewSaleTransSerialDTO  **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testCreateNewSaleTransSerialDTO_1() throws Exception {
        SaleTransSerialDTO saleTransDetailDTO = new SaleTransSerialDTO();
        SaleTransSerialServiceImpl spyTemp = PowerMockito.spy(saleTransSerialServiceImpl);
        SaleTransSerial saleTransSerial = new SaleTransSerial();
        SaleTransSerialDTO saleTransSerialDTO = new SaleTransSerialDTO();
        Mockito.when(detailMapper.toDtoBean(saleTransSerial)).thenReturn(saleTransSerialDTO);
        setFinalStatic(SaleTransSerialServiceImpl.class.getDeclaredField("mapper"), detailMapper, spyTemp);
        spyTemp.createNewSaleTransSerialDTO(saleTransDetailDTO);
        Assert.assertNotNull(saleTransSerialDTO);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for saleTransSerialServiceImpl.findByDetailId  ------------ **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testFindByDetailId_1() throws Exception {

        List<FilterRequest> filters = Lists.newArrayList();
        SaleTransSerialServiceImpl spyTemp = PowerMockito.spy(saleTransSerialServiceImpl);
        SaleTransSerial saleTransSerial = new SaleTransSerial();
        List<SaleTransSerial> saleTransSerials = Lists.newArrayList();
        List<SaleTransSerialDTO> saleTransSerials2 = Lists.newArrayList();
        SaleTransSerialDTO saleTransSerialDTO = new SaleTransSerialDTO();
        Mockito.when(detailMapper.toDtoBean(saleTransSerials)).thenReturn(saleTransSerials2);
        setFinalStatic(SaleTransSerialServiceImpl.class.getDeclaredField("mapper"), detailMapper, spyTemp);
        spyTemp.findByDetailId(1L, new Date());
        Assert.assertNotNull(saleTransSerials2);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for saleTransSerialServiceImpl.update  -------------------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testUpdate_1() throws Exception {

        SaleTransSerialDTO saleTransDetailDTO = new SaleTransSerialDTO();
        SaleTransSerialServiceImpl spyTemp = PowerMockito.spy(saleTransSerialServiceImpl);
        SaleTransSerial saleTransSerial = new SaleTransSerial();
        SaleTransSerialDTO saleTransSerialDTO = new SaleTransSerialDTO();
        Mockito.when(detailMapper.toDtoBean(saleTransSerial)).thenReturn(saleTransSerialDTO);
        setFinalStatic(SaleTransSerialServiceImpl.class.getDeclaredField("mapper"), detailMapper, spyTemp);
        spyTemp.update(saleTransDetailDTO);
        Assert.assertNotNull(saleTransSerialDTO);
    }

    static void setFinalStatic(Field field, Object newValue, final Object targetObject) throws Exception {
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(targetObject, newValue);
    }
}