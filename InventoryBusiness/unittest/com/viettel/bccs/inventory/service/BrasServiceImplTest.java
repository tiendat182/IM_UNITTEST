package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.BrasDTO;
import com.viettel.bccs.inventory.dto.EquipmentDTO;
import com.viettel.bccs.inventory.model.Bras;
import com.viettel.bccs.inventory.repo.BrasRepo;
import com.viettel.bccs.inventory.repo.BrasRepoCustom;
import com.viettel.bccs.inventory.service.BaseStock.BaseStockService;
import com.viettel.bccs.sale.dto.SaleTransSerialDTO;
import com.viettel.bccs.sale.model.SaleTransSerial;
import com.viettel.bccs.sale.service.SaleTransSerialServiceImpl;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.service.BaseServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;

/**
 * Created by TruNV on 9/9/2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({BrasServiceImpl.class, BaseServiceImpl.class, BrasService.class, BrasRepoCustom.class})
@PowerMockIgnore("javax.management.*")
public class BrasServiceImplTest {

    @InjectMocks
    private BrasServiceImpl brasService;

    @Mock
    private BaseMapper<Bras, BrasDTO> mapper;

    @Mock
    private EntityManager em;

    @Mock
    private BrasRepo repository;

    @Mock
    private EquipmentService equipmentService;

    /** -------------------------------------------------------------------- **/
    /** --- Test for brasService.count method ------------------------------ **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testCount() throws Exception {

        List<FilterRequest> filters = Lists.newArrayList();
        Mockito.when(repository.count(any())).thenReturn(1L);
        brasService.count(filters);

    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for brasService.findOne method----------------------------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testFindOne() throws Exception {

        BrasServiceImpl spyTemp = PowerMockito.spy(brasService);
        SaleTransSerial saleTransSerial = new SaleTransSerial();
        Bras bras = new Bras();
        Mockito.when(repository.findOne(anyLong())).thenReturn(bras);
        BrasDTO brasDTO = new BrasDTO();
        Mockito.when(mapper.toDtoBean(bras)).thenReturn(brasDTO);
        setFinalStatic(BrasServiceImpl.class.getDeclaredField("mapper"), mapper, spyTemp);
        spyTemp.findOne(1L);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for brasService.findAll method ---------------------------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testFindAll() throws Exception {
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for brasService.findByFilter method ----------------------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testFindByFilter() throws Exception {
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for brasService.create method ----------------------------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testCreate() throws Exception {
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for brasService.update method ----------------------------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testUpdate() throws Exception {
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for brasService.createNewBras method ---------------------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testCreateNewBras() throws Exception {
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for brasService.delete method ----------------------------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testDelete() throws Exception {
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for brasService.search method ----------------------------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testSearch() throws Exception {
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for brasService.findByBrasIp method ----------------------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testFindByBrasIp() throws Exception {
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for brasService.findAllAction method ---------------------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testFindAllAction() throws Exception {
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for brasService.findByBrasId method ----------------------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testFindByBrasId() throws Exception {
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for brasService.createBras method ------------------------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testCreateBras() throws Exception {
    }

    static void setFinalStatic(Field field, Object newValue, final Object targetObject) throws Exception {
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(targetObject, newValue);
    }
}