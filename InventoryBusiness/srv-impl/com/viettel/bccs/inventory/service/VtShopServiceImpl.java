package com.viettel.bccs.inventory.service;

import com.google.common.base.Joiner;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.controller.SolrController;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VtShopServiceImpl extends BaseServiceImpl implements VtShopService {
    public static final Logger logger = Logger.getLogger(VtShopService.class);

    @Autowired
    private StockIsdnVtShopService stockIsdnVtShopService;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private ProductWs productWs;
    @Autowired
    private ShopService shopService;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private AreaService areaService;


    @Override
    public BaseMessage saveOrderIsdn(String isdnOrder, String otp, String idNo, String viettelshopId, String customerName, String contactIsdn,
                                     String address, Date expiredDate, String payStatus, List<VtShopFeeDTO> vtShopFeeeDTOs, List<String> lstShopVtPost) {
        BaseMessage baseMessage = new BaseMessage();

        try {
            stockIsdnVtShopService.saveOrderIsdn(isdnOrder, otp, idNo, viettelshopId, customerName, contactIsdn, address, expiredDate, payStatus, vtShopFeeeDTOs, lstShopVtPost);
            baseMessage.setSuccess(true);
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            baseMessage.setErrorCode(ex.getErrorCode());
            baseMessage.setDescription(ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            baseMessage.setDescription(getText("common.error.happen"));
        }
        return baseMessage;

    }

    @Override
    public BaseMessage increaseExpiredDateOrder(String isdnOrder, Date expiredDate) {
        BaseMessage baseMessage = new BaseMessage();

        try {
            stockIsdnVtShopService.increaseExpiredDateOrder(isdnOrder, expiredDate);
            baseMessage.setSuccess(true);
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            baseMessage.setErrorCode(ex.getErrorCode());
            baseMessage.setDescription(ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            baseMessage.setDescription(getText("common.error.happen"));
        }
        return baseMessage;

    }

    public List<StockIsdnVtShopFeeDTO> findVtShopFeeByIsdn(String isdn) {

        List<StockIsdnVtShopFeeDTO> listFee = new ArrayList<>();

        try {
            return stockIsdnVtShopService.findVtShopFeeByIsdn(isdn);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        return listFee;
    }

    @Override
    public BaseMessage cancelOrderIsdn(String isdnOrder) {
        BaseMessage baseMessage = new BaseMessage();

        try {
            stockIsdnVtShopService.cancelOrderIsdn(isdnOrder);
            baseMessage.setSuccess(true);
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            baseMessage.setErrorCode(ex.getErrorCode());
            baseMessage.setDescription(ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            baseMessage.setDescription(getText("common.error.happen"));
        }
        return baseMessage;

    }

    @Override
    public StockIsdnVtShopDTO findOrderIsdn(String isdn) {
        StockIsdnVtShopDTO stockIsdnVtShopDTO = new StockIsdnVtShopDTO();

        if (DataUtil.isNullOrEmpty(isdn)) {
            return stockIsdnVtShopDTO;
        }

        try {
            stockIsdnVtShopDTO = stockIsdnVtShopService.findOne(isdn);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return stockIsdnVtShopDTO;
    }

    public List<StockTotalFullDTO> findStockByArea(String province, String district, String productOfferCode) {

        List<StockTotalFullDTO> lstStockTotal = new ArrayList<>();

        try {

            if (DataUtil.isNullOrEmpty(province) || DataUtil.isNullOrEmpty(district) || DataUtil.isNullOrEmpty(productOfferCode)) {
                return lstStockTotal;
            }

            ProductOfferingDTO productOffer = productOfferingService.findByProductOfferCode(productOfferCode, Const.STATUS_ACTIVE);

            if (productOffer == null || DataUtil.isNullOrZero(productOffer.getProductOfferingId())) {
                return lstStockTotal;
            }

            ShopMapDTO shopMapDTO = productWs.findByShopCodeVTN(DataUtil.safeToString(province).trim() + DataUtil.safeToString(district).trim());

            if (shopMapDTO == null) {
                return lstStockTotal;
            }

            ShopDTO shopDTO = shopService.findOne(shopMapDTO.getShopId());

            if (shopDTO == null) {
                return lstStockTotal;
            }

            lstStockTotal = stockIsdnVtShopService.findStockByArea(shopMapDTO.getShopId(), shopDTO.getParentShopId(), productOffer.getProductOfferingId());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return lstStockTotal;

    }

    @Override
    public BaseMessage editOrderIsdn(String isdnOrder, String oldIdNo, String newIdNo) {
        BaseMessage baseMessage = new BaseMessage();

        try {
            stockIsdnVtShopService.editOrderIsdn(isdnOrder, oldIdNo, newIdNo);
            baseMessage.setSuccess(true);
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            baseMessage.setErrorCode(ex.getErrorCode());
            baseMessage.setDescription(ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            baseMessage.setDescription(getText("common.error.happen"));
        }
        return baseMessage;

    }

    @Override
    public List<StockTotalFullDTO> findStockDigital(String shopCode, String productOfferCode) throws Exception {
        List<StockTotalFullDTO> lstStockTotal = new ArrayList<>();
        try {
            if (DataUtil.isNullOrEmpty(shopCode) || DataUtil.isNullOrEmpty(productOfferCode)) {
                return lstStockTotal;
            }
            ProductOfferingDTO productOffer = productOfferingService.findByProductOfferCode(productOfferCode, Const.STATUS_ACTIVE);

            if (productOffer == null || DataUtil.isNullOrZero(productOffer.getProductOfferingId())) {
                return lstStockTotal;
            }
            ShopDTO shopDTO = shopService.getShopByCodeAndActiveStatus(DataUtil.safeToString(shopCode).trim());

            if (shopDTO == null) {
                return lstStockTotal;
            }

            lstStockTotal = stockIsdnVtShopService.findStockDigital(shopDTO.getShopId(), productOffer.getProductOfferingId());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return lstStockTotal;
    }

    public SearchIsdnDTO searchIsdn(String isdn, Long telecomServiceId, String areaCode, Long startRow, Long maxRows) {

        List<VStockNumberDTO> lstIsdn = null;
        try {
            if (DataUtil.isNullOrEmpty(isdn)) {
                return new SearchIsdnDTO(lstIsdn, false, "ERR_IM", "sp.searchIsdn.isdn.isNull");
            }
            if (DataUtil.isNullOrZero(telecomServiceId)) {
                return new SearchIsdnDTO(lstIsdn, false, "ERR_IM", "sp.searchIsdn.telecomServiceId.isNull");
            }
            if (DataUtil.isNullObject(startRow) || DataUtil.safeToLong(startRow) < 0L) {
                return new SearchIsdnDTO(lstIsdn, false, "ERR_IM", "sp.searchIsdn.startRow.isNull");
            }
            if (DataUtil.isNullObject(maxRows) || DataUtil.safeToLong(maxRows) < 0L || DataUtil.safeToLong(maxRows) > 50L) {
                return new SearchIsdnDTO(lstIsdn, false, "ERR_IM", "sp.searchIsdn.maxRows.isNull");
            }

            List<OptionSetValueDTO> optionSetValueDTOs = DataUtil.defaultIfNull(optionSetValueService.getByOptionSetCode("VSDDTT_STOCK_CODE_PRICE"), new ArrayList<>());
            List<String> lsShopCodes = optionSetValueDTOs.stream().map(x -> x.getName()).collect(Collectors.toList());

            String query = "*:*";
            query += " AND isdn:" + isdn.trim();
            query += " AND status:(1 3)";
            query += " AND owner_code:(";
            query += Joiner.on(" ").skipNulls().join(lsShopCodes);
            query += ") ";

            if (DataUtil.safeEqual(telecomServiceId, Const.TELECOM_SERVICE.PSTN)) {

                if (DataUtil.isNullOrEmpty(areaCode)) {
                    return new SearchIsdnDTO(lstIsdn, false, "ERR_IM", "stock.total.product.ws.area.empty");
                }

                AreaDTO areaDTO = areaService.findByCode(areaCode);

                if (DataUtil.isNullObject(areaDTO)) {
                    return new SearchIsdnDTO(lstIsdn, false, "ERR_IM", "stock.total.product.ws.area.notfound", areaCode);
                }

                query += " AND isdn:" + areaDTO.getPstnCode() + "*";
            }

            if (DataUtil.safeEqual(telecomServiceId, Const.TELECOM_SERVICE.ISDN)) {
                lstIsdn = SolrController.doSearch(Const.SOLR_CORE.ISDN_MYVIETTEL, query, VStockNumberDTO.class, maxRows, "isdn", Const.SORT_ORDER.ASC, startRow);
            } else if (DataUtil.safeEqual(telecomServiceId, Const.TELECOM_SERVICE.HOMEPHONE)) {
                lstIsdn = SolrController.doSearch(Const.SOLR_CORE.ISDN_HOMEPHONE, query, VStockNumberDTO.class, maxRows, "isdn", Const.SORT_ORDER.ASC, startRow);
            } else {
                lstIsdn = SolrController.doSearch(Const.SOLR_CORE.ISDN_PSTN, query, VStockNumberDTO.class, maxRows, "isdn", Const.SORT_ORDER.ASC, startRow);
            }
            if (DataUtil.isNullOrEmpty(lstIsdn)) {
                return new SearchIsdnDTO(lstIsdn, false, "ERR_IM", "sp.searchIsdn.lst.isdn.not.found");
            }
            return new SearchIsdnDTO(lstIsdn, true, "", "");
        } catch (Exception ex) {
            logger.error("Error :", ex);
            return new SearchIsdnDTO(lstIsdn, false, "ERR_IM", "sp.searchIsdn.error");
        }
    }

}
