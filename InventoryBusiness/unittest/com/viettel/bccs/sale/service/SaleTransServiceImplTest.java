package com.viettel.bccs.sale.service;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.service.BaseStock.BaseStockService;
import com.viettel.bccs.sale.dto.SaleTransDTO;
import com.viettel.bccs.sale.model.SaleTrans;
import com.viettel.bccs.sale.model.SaleTransSerial;
import com.viettel.bccs.sale.repo.SaleTransRepo;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.mapper.BaseMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;

import java.util.*;

import static com.viettel.bccs.sale.service.SaleTransSerialServiceImplTest.setFinalStatic;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;

/**
 * Created by TruNV on 9/8/2017.
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
public class SaleTransServiceImplTest {

    @InjectMocks
    private SaleTransServiceImpl saleTransServiceImpl;

    @Mock
    private BaseMapper<SaleTrans, SaleTransDTO> mapper;

    @Mock
    private EntityManager em;

    @Mock
    private EntityManager emAnypay;

    @Mock
    private SaleTransRepo repository;

    @Mock
    Query query;

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for saleTransServiceImpl.findOne method ----------------------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testFindOne_1() throws Exception {
        SaleTransServiceImpl spyTemp = PowerMockito.spy(saleTransServiceImpl);
        SaleTrans saleTrans = new SaleTrans();
        SaleTransDTO saleTransDTO = new SaleTransDTO();
        Mockito.when(mapper.toDtoBean(saleTrans)).thenReturn(saleTransDTO);
        setFinalStatic(SaleTransServiceImpl.class.getDeclaredField("mapper"), mapper, spyTemp);
        spyTemp.findOne(1L);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for saleTransServiceImpl.save method -------------------------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testSave_1() throws Exception {

        SaleTransServiceImpl spyTemp = PowerMockito.spy(saleTransServiceImpl);
        SaleTransDTO saleTransDTO1 = new SaleTransDTO();
        SaleTrans saleTrans = new SaleTrans();
        SaleTransDTO saleTransDTO = new SaleTransDTO();
        Mockito.when(mapper.toDtoBean(saleTrans)).thenReturn(saleTransDTO);
        setFinalStatic(SaleTransServiceImpl.class.getDeclaredField("mapper"), mapper, spyTemp);
        spyTemp.save(saleTransDTO1);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for saleTransServiceImpl.update method ------------------------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testUpdate_1() throws Exception {
        SaleTransServiceImpl spyTemp = PowerMockito.spy(saleTransServiceImpl);
        SaleTransDTO saleTransDTO1 = new SaleTransDTO();
        SaleTrans saleTrans = new SaleTrans();
        SaleTransDTO saleTransDTO = new SaleTransDTO();
        Mockito.when(mapper.toDtoBean(saleTrans)).thenReturn(saleTransDTO);
        setFinalStatic(SaleTransServiceImpl.class.getDeclaredField("mapper"), mapper, spyTemp);
        spyTemp.update(saleTransDTO1);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for saleTransServiceImpl.findSaleTransByStockTransId method --- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testFindSaleTransByStockTransId_1() throws Exception {
        SaleTransServiceImpl spyTemp = PowerMockito.spy(saleTransServiceImpl);
        SaleTransDTO saleTransDTO1 = new SaleTransDTO();
        SaleTrans saleTrans = new SaleTrans();
        SaleTransDTO saleTransDTO = new SaleTransDTO();
        Mockito.when(mapper.toDtoBean(saleTrans)).thenReturn(saleTransDTO);
        setFinalStatic(SaleTransServiceImpl.class.getDeclaredField("mapper"), mapper, spyTemp);
        spyTemp.findSaleTransByStockTransId(1L, new Date());
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for saleTransServiceImpl.updateInTransIdById method ----------- **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testUpdateInTransIdById_1() throws Exception {
        Query query = new Query() {
            @Override
            public List getResultList() {
                return null;
            }

            @Override
            public Object getSingleResult() {
                return null;
            }

            @Override
            public int executeUpdate() {
                return 0;
            }

            @Override
            public Query setMaxResults(int i) {
                return null;
            }

            @Override
            public int getMaxResults() {
                return 0;
            }

            @Override
            public Query setFirstResult(int i) {
                return null;
            }

            @Override
            public int getFirstResult() {
                return 0;
            }

            @Override
            public Query setHint(String s, Object o) {
                return null;
            }

            @Override
            public Map<String, Object> getHints() {
                return null;
            }

            @Override
            public <T> Query setParameter(Parameter<T> parameter, T t) {
                return null;
            }

            @Override
            public Query setParameter(Parameter<Calendar> parameter, Calendar calendar, TemporalType temporalType) {
                return null;
            }

            @Override
            public Query setParameter(Parameter<Date> parameter, Date date, TemporalType temporalType) {
                return null;
            }

            @Override
            public Query setParameter(String s, Object o) {
                return null;
            }

            @Override
            public Query setParameter(String s, Calendar calendar, TemporalType temporalType) {
                return null;
            }

            @Override
            public Query setParameter(String s, Date date, TemporalType temporalType) {
                return null;
            }

            @Override
            public Query setParameter(int i, Object o) {
                return null;
            }

            @Override
            public Query setParameter(int i, Calendar calendar, TemporalType temporalType) {
                return null;
            }

            @Override
            public Query setParameter(int i, Date date, TemporalType temporalType) {
                return null;
            }

            @Override
            public Set<Parameter<?>> getParameters() {
                return null;
            }

            @Override
            public Parameter<?> getParameter(String s) {
                return null;
            }

            @Override
            public <T> Parameter<T> getParameter(String s, Class<T> aClass) {
                return null;
            }

            @Override
            public Parameter<?> getParameter(int i) {
                return null;
            }

            @Override
            public <T> Parameter<T> getParameter(int i, Class<T> aClass) {
                return null;
            }

            @Override
            public boolean isBound(Parameter<?> parameter) {
                return false;
            }

            @Override
            public <T> T getParameterValue(Parameter<T> parameter) {
                return null;
            }

            @Override
            public Object getParameterValue(String s) {
                return null;
            }

            @Override
            public Object getParameterValue(int i) {
                return null;
            }

            @Override
            public Query setFlushMode(FlushModeType flushModeType) {
                return null;
            }

            @Override
            public FlushModeType getFlushMode() {
                return null;
            }

            @Override
            public Query setLockMode(LockModeType lockModeType) {
                return null;
            }

            @Override
            public LockModeType getLockMode() {
                return null;
            }

            @Override
            public <T> T unwrap(Class<T> aClass) {
                return null;
            }
        };
        Mockito.when(em.createNativeQuery(any())).thenReturn(query);
        //TODO không vào được line 71 vì không mock trả về được thằng query
        saleTransServiceImpl.updateInTransIdById(1L, 1L);
    }

}