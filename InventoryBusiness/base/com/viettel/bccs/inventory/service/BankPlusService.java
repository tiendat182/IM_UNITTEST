package com.viettel.bccs.inventory.service;

import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface BankPlusService {

    /**
     * ham check serial cho stockit
     *
     * @param staffCode
     * @param serial
     * @param prodOfferCode
     * @return
     * @throws Exception
     * @author thanhnt77
     */
    public String checkSerial(@WebParam(name = "staffCode") String staffCode,
                              @WebParam(name = "serial") String serial,
                              @WebParam(name = "prodOfferCode") String prodOfferCode) throws Exception;

    /**
     * ham check checkSerial trong cua hang
     *
     * @param shopCode
     * @param serial
     * @param prodOfferCode
     * @return
     * @throws Exception
     * @author thanhnt77
     */
    public String checkSerialInShop(@WebParam(name = "shopCode") String shopCode,
                                    @WebParam(name = "serial") String serial,
                                    @WebParam(name = "prodOfferCode") String prodOfferCode) throws Exception;

    /**
     * ham tao giao dich cua hang
     *
     * @param shopCode
     * @param serial
     * @param prodOfferCode
     * @param staffCode
     * @return
     * @throws Exception
     */
    public String debitStockTotalInShop(@WebParam(name = "shopCode") String shopCode,
                                        @WebParam(name = "serial") String serial,
                                        @WebParam(name = "prodOfferCode") String prodOfferCode,
                                        @WebParam(name = "staffCode") String staffCode) throws Exception;

    /**
     * ham tao giao dich nhan vien
     *
     * @param staffCode
     * @param serial
     * @param prodOfferCode
     * @return
     * @throws Exception
     * @author thanhnt77
     */
    public String debitStockTotal(@WebParam(name = "staffCode") String staffCode,
                                  @WebParam(name = "serial") String serial,
                                  @WebParam(name = "prodOfferCode") String prodOfferCode) throws Exception;

}