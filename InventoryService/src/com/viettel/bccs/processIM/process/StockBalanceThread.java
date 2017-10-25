package com.viettel.bccs.processIM.process;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.processIM.common.InventoryThread;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.CommonService;
import com.viettel.bccs.inventory.service.StaffService;
import com.viettel.bccs.inventory.service.StockBalanceRequestService;
import com.viettel.bccs.inventory.service.StockTransVofficeService;
import com.viettel.fw.common.util.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author luannt23.
 * @comment
 * @date 3/4/2016.
 */
public class StockBalanceThread extends InventoryThread {

    @Autowired
    private StockBalanceRequestService service;

    @Autowired
    private CommonService commonService;

    @Autowired
    private StaffService staffService;

    @Autowired
    private StockTransVofficeService stockTransVofficeService;


    public StockBalanceThread(String name, int numActorLimit, int type) {
        super(name, numActorLimit, type);
    }

    @Override
    public void execute() {
        try {
            List<StockTransVofficeDTO> listStockBalanceSigned = DataUtil.defaultIfNull(stockTransVofficeService.searchStockTransVoffice(new StockTransVofficeDTO(Const.STOCK_BALANCE_PREFIX_TEMPLATE, Const.STOCK_TRANS_VOFFICE.V_SIGNED.toString()))
                    , Lists.newArrayList());
            logger.error("Bat dau quet can kho : " + listStockBalanceSigned.size());
            for (StockTransVofficeDTO stockTransVofficeDTO : listStockBalanceSigned) {
                logger.error("actionId : " + stockTransVofficeDTO.getStockTransActionId());
                List<StockBalanceRequestDTO> listStockBalanceRequestDTOs = service.searchStockBalanceRequest(new StockBalanceRequestDTO(stockTransVofficeDTO.getStockTransActionId()));
                if (!DataUtil.isNullOrEmpty(listStockBalanceRequestDTOs)) {
                    StockBalanceRequestDTO stockBalanceRequestDTO = listStockBalanceRequestDTOs.get(0);

                    logger.error("Can kho cho de xuat: " + stockBalanceRequestDTO.getRequestCode());
                    if (!DataUtil.safeEqual(Const.BalanceRequestStatus.approved, stockBalanceRequestDTO.getStatus())) {
                        logger.error("da can kho");
                        continue;
                    }
                    try {
                        StaffDTO staffDTO = staffService.getStaffByStaffCode(stockBalanceRequestDTO.getCreateUser());
                        if (staffDTO != null) {
                            stockBalanceRequestDTO.setCreateStaffId(staffDTO.getStaffId());
                        }
                        //bo xung staff_id truoc khi can kho
                        service.updateRequest(stockBalanceRequestDTO);
                        stockBalanceRequestDTO.setStatus(Const.BalanceRequestStatus.balanced.toLong());
                        stockBalanceRequestDTO.setUpdateUser(Const.SYSTEM);
                        stockBalanceRequestDTO.setUpdateDatetime(new Date());
                        service.update(stockBalanceRequestDTO);
                        //update lai stock_trans_voffice
                        stockTransVofficeDTO.setStatus(Const.STOCK_TRANS_VOFFICE.V_BALANCED.toString());
                        stockTransVofficeDTO.setLastModify(new Date());
                        stockBalanceRequestDTO.setUpdateUser(Const.SYSTEM);
                        stockTransVofficeService.save(stockTransVofficeDTO);
                    } catch (Exception ex) {
                        logger.error("Can kho cho de xuat: " + stockBalanceRequestDTO.getRequestCode() + " that bai. Ly do:");
                        logger.error(ex.getMessage(), ex);
                    }
                    logger.error("thanh cong!");
                } else {
                    logger.error(" khong thay de xuat tuong ung");
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    public StockBalanceRequestDTO initStockBalanceRequestDTO() {
        StockBalanceRequestDTO stockBalanceRequestDTO = new StockBalanceRequestDTO();
        stockBalanceRequestDTO.setOwnerId(7282L);
        stockBalanceRequestDTO.setOwnerType(1L);
        stockBalanceRequestDTO.setStockTransActionId(999999999L);
        stockBalanceRequestDTO.setCreateStaffId(998877L);
        stockBalanceRequestDTO.setCreateUser("HAINT41");
        stockBalanceRequestDTO.setType(2L);

        List<StockBalanceDetailDTO> lstStockBalanceDetailDTO = new ArrayList();
        StockBalanceDetailDTO stockBalanceDetailDTO = new StockBalanceDetailDTO();
        stockBalanceDetailDTO.setProdOfferId(5200L);
        stockBalanceDetailDTO.setQuantity(2L);

        List<StockBalanceSerialDTO> lstStockBalanceSerialDTO = new ArrayList();
        StockBalanceSerialDTO stockBalanceSerialDTO = new StockBalanceSerialDTO();
        stockBalanceSerialDTO.setProdOfferId(5200L);
        stockBalanceSerialDTO.setFromSerial("654123");
        stockBalanceSerialDTO.setToSerial("654124");

        lstStockBalanceSerialDTO.add(stockBalanceSerialDTO);
        stockBalanceDetailDTO.setLstStockBalanceSerialDTO(lstStockBalanceSerialDTO);
        lstStockBalanceDetailDTO.add(stockBalanceDetailDTO);
        stockBalanceRequestDTO.setLstStockBalanceDetailDTO(lstStockBalanceDetailDTO);

        return stockBalanceRequestDTO;
    }

}
