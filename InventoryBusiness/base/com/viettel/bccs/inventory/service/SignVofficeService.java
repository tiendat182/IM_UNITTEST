package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockTransVofficeDTO;
import com.viettel.fw.Exception.LogicException;

import javax.jws.WebService;

/**
 * Created by hoangnt14 on 12/5/2016.
 */
@WebService
public interface SignVofficeService {

    public void signVoffice(String mode, String prefixTemplate, StockTransVofficeDTO stockTransVofficeDTO) throws LogicException, Exception;

}
