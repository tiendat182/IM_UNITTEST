package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.ProductOfferPriceDTO;
import com.viettel.bccs.inventory.dto.ProfileDTO;
import com.viettel.bccs.inventory.dto.ShopMapDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.service.BaseServiceImpl;
import com.viettel.ws.common.utils.*;
import com.viettel.ws.provider.WsCallerFactory;
import com.viettel.ws.provider.WsDtoContainer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author thanhnt77
 */
@Service
public class ProductWsImpl extends BaseServiceImpl implements ProductWs {
    public static final Logger logger = Logger.getLogger(ProductWs.class);

    @Autowired
    private WsCallerFactory wsCallerFactory;

    @Override
    public ShopMapDTO findByShopCodeVTN(String areaCodeVTN) throws Exception {
        try {
            WsRequestCreator wsConfig = wsCallerFactory.createWsCaller(Const.PRODUCT_WS.ENDPOINT_KEY, "ShopMapService", Const.PRODUCT_WS.FUNCTION_FIND_SHOP_CODE_VTN);
            ShopMapDTO product = (ShopMapDTO) WebServiceHandler.defaultWebServiceHandler(wsConfig, ShopMapDTO.class, false, getGenericWebInfo(), areaCodeVTN);
            return product;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_CONNNECT.ERROR_SYSTEM_PRODUCT, "common.ws.product.error", ex);
        }
    }

    @Override
    public ShopMapDTO findByShopCodeVTNOrig(String areaCodeVTN) throws Exception {
        try {
            WsRequestCreator wsConfig = wsCallerFactory.createWsCaller(Const.PRODUCT_WS.ENDPOINT_KEY, "ShopMapService", Const.PRODUCT_WS.FUNCTION_FIND_SHOP_CODE_VTN_ORIG);
            ShopMapDTO product = (ShopMapDTO) WebServiceHandler.defaultWebServiceHandler(wsConfig, ShopMapDTO.class, false, getGenericWebInfo(), areaCodeVTN);
            return product;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_CONNNECT.ERROR_SYSTEM_PRODUCT, "common.ws.product.error", ex);
        }
    }

    @Override
    public ProfileDTO getProfileByProductOfferId(Long productOfferId) throws LogicException, Exception {

        try {
            WsRequestCreator wsConfig = wsCallerFactory.createWsCaller(Const.PRODUCT_WS.ENDPOINT_KEY, "ProductOfferingService", Const.PRODUCT_WS.FN_GET_PROFILE_BY_PRODUCT_ID);
            ProfileDTO profileDTO = (ProfileDTO) WebServiceHandler.defaultWebServiceHandler(wsConfig, ProfileDTO.class, false, getGenericWebInfo(), productOfferId);
            return profileDTO;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_CONNNECT.ERROR_SYSTEM_PRODUCT, "common.ws.product.error", ex);
        }
    }

    @Override
    public List<ProductOfferPriceDTO> getListPriceByStaff(Long staffId, Long shopId, Long productOfferId) throws LogicException, Exception {
        try {
             List<ProductOfferPriceDTO> lsPrice = Lists.newArrayList();

            WsRequestCreator wsConfig = wsCallerFactory.createWsCaller(Const.PRODUCT_WS.ENDPOINT_KEY_FOR_IM, "ExternalServiceForIM", Const.PRODUCT_WS.FUNCTION_FIND_PRICE);
            Map<String, Object> bodyParamMap = new HashMap<>();
            bodyParamMap.put("staffId", staffId);
            bodyParamMap.put("shopId", shopId);
            bodyParamMap.put("productOfferingid", productOfferId);
            wsConfig.setBodyArgAlias(bodyParamMap);
            String response = WebServiceHandler.webServiceCaller(wsConfig);
            logger.info(response);
            if (!DataUtil.isNullOrEmpty(response)) {
                List<XStreamStorage> parseObject = new ArrayList<>();
                Mapper.alias(parseObject, WsDtoContainer.class, "ns2:findPriceBySaleProgramShopStaffAndProductOfferingResponse");
                Mapper.addImplicitCollection(parseObject, WsDtoContainer.class, "list");
                Mapper.alias(parseObject, ProductOfferPriceDTO.class, "return");
                XmlStream xmlStream = new XmlStream();
                xmlStream.config(parseObject);
                xmlStream.isSingleType = false;
                WsDtoContainer lst = (WsDtoContainer) WebServiceHandler.wsServiceHandler(response, xmlStream);
                lsPrice = (java.util.List) lst.getList();
            }
            return lsPrice;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_CONNNECT.ERROR_SYSTEM_PRODUCT, "common.ws.product.error", ex);
        }
    }

    @Override
    public String findTeamCodeByShopCode(String shopCode) throws LogicException, Exception {
        try {
            WsRequestCreator wsConfig = wsCallerFactory.createWsCaller(Const.PRODUCT_WS.ENDPOINT_KEY_FOR_IM, "ExternalServiceForIM", Const.PRODUCT_WS.FUNCTION_FIND_TEAMCODE);
            Map<String, Object> bodyParamMap = new HashMap<>();
            bodyParamMap.put("shopCode", shopCode);
            wsConfig.setBodyArgAlias(bodyParamMap);
            String response = WebServiceHandler.webServiceCaller(wsConfig);
            logger.info(response);
            if (!DataUtil.isNullOrEmpty(response)) {
                List<XStreamStorage> parseObject = new ArrayList<>();
                Mapper.alias(parseObject, String.class, "return");
                XmlStream xmlStream = new XmlStream();
                xmlStream.config(parseObject);
                xmlStream.isSingleType = true;
                return  (String) WebServiceHandler.wsServiceHandler(response, xmlStream);
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_CONNNECT.ERROR_SYSTEM_PRODUCT, "common.ws.product.error", ex);
        }
        return "";
    }
}