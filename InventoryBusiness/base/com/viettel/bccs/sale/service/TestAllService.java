package com.viettel.bccs.sale.service;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * Created by PM2-LAMNV5 on 11/16/2016.
 */
@WebService
public interface TestAllService {
    @WebMethod
    String test_executeStockTrans() throws Exception;
}
