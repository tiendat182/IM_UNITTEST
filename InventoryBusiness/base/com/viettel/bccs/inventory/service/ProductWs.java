package com.viettel.bccs.inventory.service;


import com.viettel.bccs.inventory.dto.ProductOfferPriceDTO;
import com.viettel.bccs.inventory.dto.ProfileDTO;
import com.viettel.bccs.inventory.dto.ShopMapDTO;
import com.viettel.fw.Exception.LogicException;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

/**
 * @author thanhnt77
 */
@WebService
public interface ProductWs {


//    @WebMethod
//    public ShopMapDTO findByShopCodeVTNOrig(String shopCodeVTN) throws Exception;

    @WebMethod
    public ShopMapDTO findByShopCodeVTN(String areaCodeVTN) throws Exception;

    @WebMethod
    public ShopMapDTO findByShopCodeVTNOrig(String areaCodeVTN) throws Exception;

    @WebMethod
    public ProfileDTO getProfileByProductOfferId(Long productOfferId) throws LogicException, Exception;

    /**
     * lam lay ve danh sach gia mat hang ben product
     * @author thanhnt77
     * @param staffId
     * @param shopId
     * @param productOfferId
     * @return
     * @throws LogicException
     * @throws Exception
     */
    @WebMethod
    public List<ProductOfferPriceDTO> getListPriceByStaff(Long staffId, Long shopId, Long productOfferId) throws LogicException, Exception;

    /**
     *
     * @param shopCode
     * @return
     */
    String findTeamCodeByShopCode(String shopCode) throws LogicException, Exception;
}