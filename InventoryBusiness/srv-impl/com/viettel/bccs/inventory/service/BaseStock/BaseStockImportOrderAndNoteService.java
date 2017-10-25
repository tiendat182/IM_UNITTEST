package com.viettel.bccs.inventory.service.BaseStock;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.FlagStockDTO;
import com.viettel.bccs.inventory.dto.StockTransActionDTO;
import com.viettel.bccs.inventory.dto.StockTransDTO;
import com.viettel.bccs.inventory.dto.StockTransDetailDTO;
import com.viettel.bccs.inventory.service.StockTransService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.RequiredRoleMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by thanhnt77
 * lap lenh , lap phieu xuat tu dong
 */

@Service
public class BaseStockImportOrderAndNoteService extends BaseStockService {

    @Autowired
    BaseStockInOrderService baseStockInOrderService;
    @Autowired
    BaseStockInNoteService baseStockInNoteService;

    @Autowired
    private StockTransService stockTransService;


    @Override
    public void doPrepareData(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, FlagStockDTO flagStockDTO) throws LogicException, Exception {

    }

    @Override
    public void doValidate(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StockTransActionDTO executeStockTrans(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail, RequiredRoleMap requiredRoleMap) throws Exception {

        StockTransDTO stockTransDTOTmp = DataUtil.cloneBean(stockTransDTO);

        stockTransDTOTmp.setSignVoffice(false);//bo qua buoc ky voffice lap lenh

        //lap lenh
        StockTransActionDTO orderTransActionDTO = baseStockInOrderService.executeStockTrans(stockTransDTOTmp, stockTransActionDTO, lstStockTransDetail, requiredRoleMap);

        //lap phieu
        stockTransDTO.setStockTransId(orderTransActionDTO.getStockTransId());

        stockTransDTO.setActionCode(stockTransDTO.getActionCodeNote());
        orderTransActionDTO.setActionCode(stockTransDTO.getActionCodeNote());

        StockTransDTO transDTO = stockTransService.findOne(orderTransActionDTO.getStockTransId());
        stockTransDTO.setRegionStockId(orderTransActionDTO.getRegionOwnerId());
        stockTransDTO.setRegionStockTransId(transDTO.getRegionStockTransId());
        stockTransDTO.setTransport(transDTO.getTransport());
        stockTransDTO.setIsAutoGen(transDTO.getIsAutoGen());
        stockTransDTO.setNote(null);
        stockTransDTO.setFromStock(Const.FROM_STOCK.FROM_UNDERLYING);//fix validate kho xuat phai la cap tren lien ke kho nhan

        StockTransActionDTO noteTransActionDTO = baseStockInNoteService.executeStockTrans(stockTransDTO, orderTransActionDTO, lstStockTransDetail, requiredRoleMap);

        return noteTransActionDTO;
    }
}
