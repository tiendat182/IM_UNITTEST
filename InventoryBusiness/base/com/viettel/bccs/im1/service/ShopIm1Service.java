package com.viettel.bccs.im1.service;

import com.viettel.bccs.im1.dto.ShopIm1DTO;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface ShopIm1Service {

    @WebMethod
    public ShopIm1DTO findOne(Long id) throws Exception;

    @WebMethod
    public ShopIm1DTO save(ShopIm1DTO shopDTO) throws Exception;

    public void deleteStock(List<Long> lstShopId) throws Exception;
}