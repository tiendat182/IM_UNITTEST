package com.viettel.bccs.inventory.service.BaseStock;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.StockOrderAgentDTO;
import com.viettel.bccs.inventory.dto.StockTransActionDTO;
import com.viettel.bccs.inventory.dto.StockTransDTO;
import com.viettel.bccs.inventory.dto.StockTransDetailDTO;
import com.viettel.bccs.inventory.service.StockOrderAgentService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.RequiredRoleMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by sinhHIV on 13/01/2016.
 */

@Service
public class BaseStockAgentExpRequestService extends BaseStockAgentExpService {
    @Autowired
    private StockOrderAgentService stockOrderAgentService;
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StockTransActionDTO executeStockTrans(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail, RequiredRoleMap requiredRoleMap) throws Exception {
        //validate quyen ban dut hoac quyen dat coc
        checkPermissionOrder(stockTransDTO, requiredRoleMap);

        super.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetail, requiredRoleMap);

        //cap nhat stock_order_agent
        StockOrderAgentDTO stockOrderAgentDTO = stockOrderAgentService.findOne(stockTransDTO.getStockOrderAgentId());
        stockOrderAgentDTO.setStatus(1L);
        stockOrderAgentDTO.setUpdateStaffId(stockTransDTO.getStaffId());
        stockOrderAgentDTO.setStockTransId(lstStockTransDetail.get(0).getStockTransId());
        stockOrderAgentDTO.setLastModify(getSysDate(em));
        stockOrderAgentService.update(stockOrderAgentDTO);
        return stockTransActionDTO;
    }

    private void checkPermissionOrder(StockTransDTO stockTransDTO, RequiredRoleMap requiredRoleMap) throws LogicException, Exception {
//        if (stockTransDTO.getStockAgent().equals(Const.STOCK_TRANS.STOCK_TRANS_DEPOSIT)) {
//            if (!requiredRoleMap.hasRole(Const.PERMISION.ROLE_XUAT_DAT_COC)) {
//                throw new LogicException("", "mn.stock.agency.not.permission.deposit");
//            }
//        } else if (stockTransDTO.getStockAgent().equals(Const.STOCK_TRANS.STOCK_TRANS_PAY)) {
//            if (!requiredRoleMap.hasRole(Const.PERMISION.ROLE_XUAT_BAN_DUT)) {
//                throw new LogicException("", "mn.stock.agency.not.permission.pay");
//            }
//        }

        if(DataUtil.isNullObject(stockTransDTO.getStockOrderAgentId())){
            throw new LogicException("", "mn.stock.agency.request.not.exist");
        }

        StockOrderAgentDTO stockOrderAgentDTO = stockOrderAgentService.findOne(stockTransDTO.getStockOrderAgentId());
        if(!DataUtil.safeEqual(stockOrderAgentDTO.getStatus(),Const.STOCK_ORDER_AGENT.STATUS_CREATE_REQUEST)){
            throw new LogicException("", "mn.stock.agency.request.not.execute");
        }
    }
}
