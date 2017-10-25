package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.message.BaseExtMessage;
import com.viettel.bccs.inventory.message.BasePartnerMessage;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.RequiredRoleMap;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

/**
 * Description: Service thuc hien xuat nhap kho
 *
 * @author TheTM1
 * @date 09/12/2015
 */

@WebService
public interface ExecuteStockTransService {

    /**
     * Description: Ham thuc hien xuat nhap kho
     *
     * @param mode bien 3 bit cho lap lenh, phieu, xuat/nhap kho
     * @param type 1 xuat kho, 2 nhap kho
     * @return
     * @throws Exception
     * @author TheTM1
     */
    @WebMethod
    public BaseMessageStockTrans executeStockTrans(String mode, String type, StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO,
                                         List<StockTransDetailDTO> lstStockTransDetail, RequiredRoleMap requiredRoleMap) throws Exception;


    /**
     * Description: Ham thuc hien xuat nhap kho theo file
     *
     * @param mode bien 3 bit cho lap lenh, phieu, xuat/nhap kho
     * @param type 1 xuat kho, 2 nhap kho
     * @return
     * @throws Exception
     * @author TheTM1
     */
    public List<BaseExtMessage> executeStockTransList(String mode, String type, List<StockTransFileDTO> lsStockTransFileDTOs, RequiredRoleMap requiredRoleMap);

    /**
     * xuat hang cho doi tac
     * @author thanhnt
     * @param mode
     * @param type
     * @param stockTransFullDTO
     * @param staffDTO
     * @param typeExport
     * @param serialList
     * @throws LogicException
     * @throws Exception
     */
    public BasePartnerMessage createExportToPartner(String mode, String type, StockTransFullDTO stockTransFullDTO, StaffDTO staffDTO,
                                             String typeExport, String serialList, RequiredRoleMap requiredRoleMap);
}