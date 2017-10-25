package com.viettel.bccs.sale.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.sale.repo.PaymentDebitRepo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


/**
 * Created by Lamnv5 on 8/9/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class PaymentDebitServiceImplTest {

    @InjectMocks
    PaymentDebitServiceImpl debitService;
    @Mock
    PaymentDebitRepo repository;
    @Test
    public void testCount() throws Exception {
        Mockito.when(repository.count(Mockito.any())).thenReturn(1L);
        Assert.assertEquals(debitService.count(Lists.newArrayList()).longValue(), 1L);
    }

}