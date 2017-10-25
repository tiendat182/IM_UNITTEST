package com.viettel.bccs.inventory.service;

import com.viettel.fw.Exception.LogicException;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

/**
 * @author luannt23 on 1/7/2016.
 */
@WebService
public interface CommonService {
    @WebMethod
    public String getStockReportTemplate(Long ownerId, String ownerType) throws LogicException, Exception;

    @WebMethod
    public List<String> getChannelTypes(String ownerType) throws LogicException, Exception;

    @WebMethod
    public String getOwnerName(String ownerID, String ownerType) throws Exception;

    @WebMethod
    public List<Long> getChannelTypeByVtUnit(String ownerType, Long vtUnit) throws LogicException, Exception;

    @WebMethod
    public boolean checkIsdnVietel(String sNumber) throws LogicException, Exception;

    public String getProvince(Long shopId) throws Exception;

    public String getOwnerAddress(String ownerID, String ownerType) throws Exception;
}
