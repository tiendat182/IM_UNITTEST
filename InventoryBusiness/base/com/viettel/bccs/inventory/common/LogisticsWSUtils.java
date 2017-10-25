package com.viettel.bccs.inventory.common;


import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.dto.ProductOfferingLogisticDTO;
import com.viettel.bccs.inventory.dto.StockTransDTO;
import com.viettel.fw.common.util.Const;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.logistic.cms.service.StockManagementService;
import com.viettel.logistic.cms.service.StockManagementServiceImplService;
import org.apache.log4j.Logger;
import vn.viettel.cms.OrderGoodsDetailDTO;
import vn.viettel.cms.OrdersDTO;
import vn.viettel.cms.ResultBCCS;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by hoangnt14 on 7/28/2016.
 */
public class LogisticsWSUtils {
    private static final Logger logger = Logger.getLogger(LogisticsWSUtils.class);

    public static BaseMessage createOrder(StockTransDTO stockTransDTO, List<ProductOfferingLogisticDTO> logisticDTOList) {
        BaseMessage output = new BaseMessage();
        try {
            logger.info("Bat dau day lenh voi stockTransId = " + stockTransDTO.getStockTransId());
            ResourceBundle resource = ResourceBundle.getBundle("spring");

            StockManagementServiceImplService stockManagementServiceImplService = new StockManagementServiceImplService(new URL(resource.getString("logistic.ws.url")));
            StockManagementService stockManagementService = stockManagementServiceImplService.getStockManagementServiceImplPort();

            //getOrdersDTO
            OrdersDTO ordersDTO = getOrdersDTO(stockTransDTO);

            //getOrderGoodsDetailDTO
            List<OrderGoodsDetailDTO> lstOrderGoodsDetailDTO = getOrderGoodsDetailDTO(logisticDTOList);

            //set timeout webservice
            setTimeOutWebservice(resource);

            ResultBCCS resultBCCS = stockManagementService.createOrder(ordersDTO, lstOrderGoodsDetailDTO);
            if ("1".equals(resultBCCS.getStatus())) {
                output.setErrorCode(Const.LOGISTIC_WS_STATUS.SUCCESS);
                output.setDescription("Gui yeu cau Logistic thanh cong");
            } else {
                output.setErrorCode(Const.LOGISTIC_WS_STATUS.REJECT);
                output.setDescription(resultBCCS.getReason());
            }

        } catch (Exception ex) {
            logger.error("Error when createOrder: ", ex);
            output.setErrorCode(Const.LOGISTIC_WS_STATUS.CALL_WS);
            output.setDescription("Loi khi goi webservice logistic");
        } finally {
            logger.info("ket thuc day lenh voi stockTransId = " + stockTransDTO.getStockTransId());
        }
        return output;
    }

    private static OrdersDTO getOrdersDTO(StockTransDTO stockTransDTO) throws Exception {
        OrdersDTO ordersDTO = new OrdersDTO();
        ordersDTO.setChangedTime(0);
        ordersDTO.setOrderType(stockTransDTO.getOrderType());
        if (DataUtil.safeEqual(stockTransDTO.getOrderType(), "2")) {
            ordersDTO.setOutputType(String.valueOf(stockTransDTO.getOutputType()));
        } else {
            ordersDTO.setInputType(String.valueOf(stockTransDTO.getInputType()));
        }
        ordersDTO.setCustCode(stockTransDTO.getCustCode());
        ordersDTO.setOrderCode(String.valueOf(stockTransDTO.getStockTransId()));
//        ordersDTO.setOrderStockId(stockTransBean.getStockCode());
        ordersDTO.setStockCode(stockTransDTO.getStockCode());
        ordersDTO.setIeExpectDate(DateUtil.dateTime2Stringddmmyyyy(stockTransDTO.getIeExpectDate()));
        ordersDTO.setAreaCode(stockTransDTO.getAreaCode());
        ordersDTO.setTypeReceive(stockTransDTO.getTypeReceive());
        ordersDTO.setOrderCommandCode(stockTransDTO.getActionCode());
        return ordersDTO;
    }

    private static List<OrderGoodsDetailDTO> getOrderGoodsDetailDTO(List<ProductOfferingLogisticDTO> productOfferingLogisticDTOs) throws Exception {
        List<OrderGoodsDetailDTO> lstOrderGoodsDetailDTO = Lists.newArrayList();

        for (ProductOfferingLogisticDTO productOfferingLogisticDTO : productOfferingLogisticDTOs) {

            OrderGoodsDetailDTO orderGoodsDetailDTO = new OrderGoodsDetailDTO();
            orderGoodsDetailDTO.setAmount(String.valueOf(productOfferingLogisticDTO.getAmount()));
            orderGoodsDetailDTO.setChangedTime(0);
            orderGoodsDetailDTO.setGoodsCode(productOfferingLogisticDTO.getGoodCode());
            orderGoodsDetailDTO.setGoodsName(productOfferingLogisticDTO.getGoodName());
            orderGoodsDetailDTO.setGoodsState(String.valueOf(productOfferingLogisticDTO.getGoodState()));
            orderGoodsDetailDTO.setGoodsUnitName(productOfferingLogisticDTO.getGoodUnitName());
            orderGoodsDetailDTO.setTransfersGoodsCode(productOfferingLogisticDTO.getTransferGoodCode());

            lstOrderGoodsDetailDTO.add(orderGoodsDetailDTO);

        }
        return lstOrderGoodsDetailDTO;
    }

    /**
     * setTimeOutWS
     */
    private static void setTimeOutWebservice(ResourceBundle resource) {
        System.setProperty("sun.net.client.defaultConnectTimeout", resource.getString("logistic.ws.timeout"));
        System.setProperty("sun.net.client.defaultReadTimeout", resource.getString("logistic.ws.timeout"));
    }
}
