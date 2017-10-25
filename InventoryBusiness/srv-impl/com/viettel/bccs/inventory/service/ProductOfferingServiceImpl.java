package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.googlecode.ehcache.annotations.Cacheable;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.controller.SolrController;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.OptionSetValue;
import com.viettel.bccs.inventory.model.ProductOffering;
import com.viettel.bccs.inventory.repo.OptionSetValueRepo;
import com.viettel.bccs.inventory.repo.ProductOfferingRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.jws.WebMethod;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductOfferingServiceImpl extends BaseServiceImpl implements ProductOfferingService {

    private final BaseMapper<ProductOffering, ProductOfferingDTO> mapper = new BaseMapper(ProductOffering.class, ProductOfferingDTO.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Autowired
    private ProductOfferingRepo repository;
    @Autowired
    OptionSetValueService optionSetValueService;
    @Autowired
    ProductOfferPriceService productOfferPriceService;
    @Autowired
    ProductWs productWs;
    @Autowired
    private OptionSetValueRepo optionSetValueRepo;

    public static final Logger logger = Logger.getLogger(ProductOfferingService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public ProductOfferingDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<ProductOfferingDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<ProductOfferingDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(ProductOfferingDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(ProductOfferingDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Override
    public List<ProductOfferingTotalDTO> getLsProductOffering(String input) throws Exception {
        StringBuilder query = new StringBuilder("");
        if (DataUtil.isNullOrEmpty(input)) {
            query.append(" *:* ");
        } else {
            query.append(" name:*").append(input).append("* OR code:*").append(input).append("*");
        }
        List<ProductOfferingTotalDTO> lstProductOffer = SolrController.doSearch(Const.SOLR_CORE.PRODUCTOFFERING_OFFERTYPE,
                query.toString(), ProductOfferingTotalDTO.class, null, "name", Const.SORT_ORDER.ASC);
        return lstProductOffer;
    }

    @WebMethod
    @Override
    public List<ProductOfferingTotalDTO> getLsProductOfferingByProductType(String input, Long prodTypeId) throws Exception {
        StringBuilder query = new StringBuilder(" *:* ");
        if (!DataUtil.isNullOrEmpty(input) && !DataUtil.isNullOrZero(prodTypeId)) {
            query.append(" AND (name:*{0}* OR code:*{1}*) AND product_offer_type_id:" + prodTypeId + " ");
            query = new StringBuilder(MessageFormat.format(query.toString(), DataUtil.replaceSpaceSolr(input), DataUtil.replaceSpaceSolr(input)));
        } else if (DataUtil.isNullOrEmpty(input) && !DataUtil.isNullOrZero(prodTypeId)) {
            query.append(" AND product_offer_type_id:" + prodTypeId + " ");
        } else if (DataUtil.isNullOrZero(prodTypeId) && !DataUtil.isNullOrEmpty(input)) {
            query.append(" AND (name:*{0}* OR code:*{1}*)");
            query = new StringBuilder(MessageFormat.format(query.toString(), DataUtil.replaceSpaceSolr(input), DataUtil.replaceSpaceSolr(input)));
        }
        query.append(" AND product_offer_type_id:(1 2 3)");
        try {
            List<ProductOfferingTotalDTO> lstProductOffer = SolrController.doSearch(Const.SOLR_CORE.PRODUCTOFFERING_OFFERTYPE, query.toString(), ProductOfferingTotalDTO.class, Const.NUMBER_SEARCH.NUMBER_ALL, "name", Const.SORT_ORDER.ASC);
            return lstProductOffer;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    public List<ProductOfferingTotalDTO> getLsProductOfferingByProductTypeAndCode(String input, Long prodTypeId, List<String> lstCodes) throws Exception {
        StringBuilder query = new StringBuilder("");
        if (!DataUtil.isNullOrEmpty(lstCodes)) {
            StringBuilder strProduct = new StringBuilder("");
            for (String proOfferCode : lstCodes) {
                if (!"".equals(strProduct.toString())) {
                    strProduct.append(" ");
                }
                strProduct.append(proOfferCode);
            }
            query.append(" code: (").append(strProduct).append(" ) ");
        }
        if (!DataUtil.isNullOrEmpty(input)) {
            query.append(" AND product_offer_type_id:" + prodTypeId + " AND ( name:*").append(input).append("* OR code:*").append(input).append("* )");
        } else {
            query.append(" AND *:* AND product_offer_type_id:" + prodTypeId + " ");
        }
        List<ProductOfferingTotalDTO> lstShop = SolrController.doSearch(Const.SOLR_CORE.PRODUCTOFFERING_OFFERTYPE, query.toString(), ProductOfferingTotalDTO.class, Const.NUMBER_SEARCH.NUMBER_ALL, "name", Const.SORT_ORDER.ASC);
        return lstShop;
    }

    @WebMethod
    @Override
    public List<ProductOfferingTotalDTO> getListProductOfferingByProductType(String input, Long prodTypeId) throws Exception {
        StringBuilder query = new StringBuilder(" *:* ");
        if (!DataUtil.isNullOrEmpty(input) && !DataUtil.isNullOrZero(prodTypeId)) {
            query.append(" AND (name:*{0}* OR code:*{1}*) AND product_offer_type_id:" + prodTypeId + " ");
            query = new StringBuilder(MessageFormat.format(query.toString(), DataUtil.replaceSpaceSolr(input), DataUtil.replaceSpaceSolr(input)));
        } else if (DataUtil.isNullOrEmpty(input) && !DataUtil.isNullOrZero(prodTypeId)) {
            query.append(" AND product_offer_type_id:" + prodTypeId + " ");
        } else if (DataUtil.isNullOrZero(prodTypeId) && !DataUtil.isNullOrEmpty(input)) {
            query.append(" AND (name:*{0}* OR code:*{1}*)");
            query = new StringBuilder(MessageFormat.format(query.toString(), DataUtil.replaceSpaceSolr(input), DataUtil.replaceSpaceSolr(input)));
        }
        try {
            List<ProductOfferingTotalDTO> lstProductOffer = SolrController.doSearch(Const.SOLR_CORE.PRODUCTOFFERING_OFFERTYPE, query.toString(), ProductOfferingTotalDTO.class, Const.NUMBER_SEARCH.NUMBER_ALL, "name", Const.SORT_ORDER.ASC);
            return lstProductOffer;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    public List<ProductOfferingTotalDTO> getListProductOfferingForUpdateSerialGpon(String input, String updateType) throws Exception {
        StringBuilder query = new StringBuilder(" *:* AND product_offer_type_id:7 ");
        if (DataUtil.safeEqual(updateType, Const.UPDATE_SERIAL_GPON.TYPE_MULTI)) {
            query.append(" AND device_type:[* TO *] ");// AND device_type:[* TO *]

        }
        if (!DataUtil.isNullOrEmpty(input)) {
            query.append(" AND (name:*{0}* OR code:*{1}*) ");
            query = new StringBuilder(MessageFormat.format(query.toString(), DataUtil.replaceSpaceSolr(input), DataUtil.replaceSpaceSolr(input)));
        }
        try {
            List<ProductOfferingTotalDTO> lstProductOffer = SolrController.doSearch(Const.SOLR_CORE.PRODUCTOFFERING_OFFERTYPE, query.toString(), ProductOfferingTotalDTO.class, Const.NUMBER_SEARCH.NUMBER_ALL, "name", Const.SORT_ORDER.ASC);
            return lstProductOffer;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    @WebMethod
    @Override
    public ProductOfferingTotalDTO getProductOffering(Long id) throws Exception {
        StringBuilder query = new StringBuilder("");
        if (!DataUtil.isNullOrZero(id)) {
            query.append(" prod_offer_id:").append(id);
        }
        List<ProductOfferingTotalDTO> lstProductOffer = SolrController.doSearch(Const.SOLR_CORE.PRODUCTOFFERING_OFFERTYPE, query.toString(), ProductOfferingTotalDTO.class, Const.NUMBER_SEARCH.NUMBER_ALL, "name", Const.SORT_ORDER.ASC);
        if (!DataUtil.isNullOrEmpty(lstProductOffer)) {
            return lstProductOffer.get(0);
        }
        return null;
    }

    @WebMethod
    @Override
    public List<ProductOfferingTotalDTO> getAllLsProductOfferingDTO(String input) throws Exception {
        StringBuilder query = new StringBuilder("");
        if (!DataUtil.isNullOrEmpty(input)) {
            query.append(" name:").append(input).append(" OR code: ").append(input);
        }
        List<ProductOfferingTotalDTO> lstShop = SolrController.doSearch(Const.SOLR_CORE.PRODUCTOFFERING, query.toString(), ProductOfferingTotalDTO.class, null, "name", Const.SORT_ORDER.ASC);
        return lstShop;
    }

    @WebMethod
    @Override
    public List<ProductOfferingTotalDTO> getAllLsProductOfferingDTOForProcessStock(String input, List<Long> lsProductOfferTypeIds) throws Exception {
        StringBuilder query = new StringBuilder("*:*");
        if (!DataUtil.isNullOrEmpty(lsProductOfferTypeIds)) {
            StringBuilder strProduct = new StringBuilder("");
            for (Long proOfferTypeId : lsProductOfferTypeIds) {
                if (!"".equals(strProduct.toString())) {
                    strProduct.append(" ");
                }
                strProduct.append(proOfferTypeId);
            }
            query.append(" AND product_offer_type_id: (").append(strProduct).append(" ) ");
        }
        if (!DataUtil.isNullOrEmpty(input)) {
            query.append(" AND (name:*{0}* OR code:*{1}*)");
            query = new StringBuilder(MessageFormat.format(query.toString(), DataUtil.replaceSpaceSolr(input), DataUtil.replaceSpaceSolr(input)));
        }
        List<ProductOfferingTotalDTO> lstShop = SolrController.doSearch(Const.SOLR_CORE.PRODUCTOFFERING_OFFERTYPE, query.toString(), ProductOfferingTotalDTO.class, Const.NUMBER_SEARCH.NUMBER_ALL, "name", Const.SORT_ORDER.ASC);
        return lstShop;
    }

    @WebMethod
    @Override
    public List<ProductOfferingDTO> getLsProductOfferDTOByType(String input, List<Long> lsProductOfferTypeIds) throws Exception {
        StringBuilder query = new StringBuilder("");
        if (!DataUtil.isNullOrEmpty(lsProductOfferTypeIds)) {
            StringBuilder strProduct = new StringBuilder("");
            for (Long proOfferTypeId : lsProductOfferTypeIds) {
                if (!"".equals(strProduct.toString())) {
                    strProduct.append(" ");
                }
                strProduct.append(proOfferTypeId);
            }
            query.append(" product_offer_type_id: (").append(strProduct).append(" ) ");
        }
        if (!DataUtil.isNullOrEmpty(input)) {
            query.append(" AND ( name:*").append(input).append("* OR code:*").append(input).append("* )");
        } else {
            query.append("*:*");
        }
        List<ProductOfferingDTO> lstShop = SolrController.doSearch(Const.SOLR_CORE.PRODUCTOFFERING_OFFERTYPE, query.toString(), ProductOfferingDTO.class, Const.NUMBER_SEARCH.NUMBER_ALL, "name", Const.SORT_ORDER.ASC);
        return lstShop;
    }

    @WebMethod
    @Override
    public List<ProductOfferingTotalDTO> getLsProductOfferingDTO(String input, List<Long> lsProductOfferTypeIds, Long ownerId, String ownerType, String stateId, List<Long> prodOfferIdList) throws Exception {
        StringBuilder query = new StringBuilder(" ");
        String inputTmp = DataUtil.safeToString(input).trim();
        if (DataUtil.isNullOrEmpty(inputTmp)) {
            query.append(" *:* ");
        } else {
            query.append(" ( ").append(" name:*").append(inputTmp).append("* OR code:*").append(inputTmp).append("*").append(" ) ");
        }
        if (!DataUtil.isNullOrEmpty(lsProductOfferTypeIds)) {
            StringBuilder strProduct = new StringBuilder("");
            for (Long proOfferTypeId : lsProductOfferTypeIds) {
                if (!"".equals(strProduct.toString())) {
                    strProduct.append(" ");
                }
                strProduct.append(proOfferTypeId);
            }
            query.append(" AND product_offer_type_id: (").append(strProduct).append(" ) ");
        }
        if (!DataUtil.isNullOrEmpty(prodOfferIdList)) {
            query.append(" AND prod_offer_id: ");
            query.append(" ( ");
            for (Long prodOfferId : prodOfferIdList) {
                query.append(prodOfferId).append(" ");
            }
            query.append(" ) ");
        }
        if (!DataUtil.isNullOrZero(ownerId)) {
            query.append(" AND owner_id: ").append(ownerId);
        }
        if (!DataUtil.isNullOrEmpty(ownerType)) {
            query.append(" AND owner_type: ").append(ownerType);
        }
        if (!DataUtil.isNullOrEmpty(stateId)) {
            query.append(" AND state_id: ").append(stateId);
        }
        if (!DataUtil.isNullOrEmpty(lsProductOfferTypeIds) && !DataUtil.isNullObject(stateId)) {
            query.append(" AND available_quantity: [1 TO *] ");
        }
        List<ProductOfferingTotalDTO> lstShop = SolrController.doSearch(Const.SOLR_CORE.PRODUCTOFFERING, query.toString(), ProductOfferingTotalDTO.class, Const.NUMBER_SEARCH.NUMBER_ALL, "name", Const.SORT_ORDER.ASC);
        return lstShop;
    }

    @WebMethod
    @Override
    public List<ProductOfferingTotalDTO> getLsProductOfferingDTOMaterial(String input, List<Long> lsProductOfferTypeIds, Long ownerId, String ownerType, String stateId, List<Long> prodOfferIdList, String stockModelType) throws Exception {
        StringBuilder query = new StringBuilder(" ");
        String inputTmp = DataUtil.safeToString(input).trim();
        if (DataUtil.isNullOrEmpty(inputTmp)) {
            query.append(" *:* ");
        } else {
            query.append(" ( ").append(" name:*").append(inputTmp).append("* OR code:*").append(inputTmp).append("*").append(" ) ");
        }
        if (!DataUtil.isNullOrEmpty(lsProductOfferTypeIds)) {
            StringBuilder strProduct = new StringBuilder("");
            for (Long proOfferTypeId : lsProductOfferTypeIds) {
                if (!"".equals(strProduct.toString())) {
                    strProduct.append(" ");
                }
                strProduct.append(proOfferTypeId);
            }
            query.append(" AND product_offer_type_id: (").append(strProduct).append(" ) ");
        }
        if (!DataUtil.isNullOrEmpty(prodOfferIdList)) {
            query.append(" AND prod_offer_id: ");
            query.append(" ( ");
            for (Long prodOfferId : prodOfferIdList) {
                query.append(prodOfferId).append(" ");
            }
            query.append(" ) ");
        }
        if (!DataUtil.isNullOrZero(ownerId)) {
            query.append(" AND owner_id: ").append(ownerId);
        }
        if (!DataUtil.isNullOrEmpty(stockModelType)) {
            query.append(" AND stock_model_type: ").append(stockModelType);
        }
        if (!DataUtil.isNullOrEmpty(ownerType)) {
            query.append(" AND owner_type: ").append(ownerType);
        }
        if (!DataUtil.isNullOrEmpty(stateId)) {
            query.append(" AND state_id: ").append(stateId);
        }
        if (!DataUtil.isNullOrEmpty(lsProductOfferTypeIds) && !DataUtil.isNullObject(stateId)) {
            query.append(" AND available_quantity: [1 TO *] ");
        }
        List<ProductOfferingTotalDTO> lstShop = SolrController.doSearch(Const.SOLR_CORE.PRODUCTOFFERING, query.toString(), ProductOfferingTotalDTO.class, Const.NUMBER_SEARCH.NUMBER_ALL, "name", Const.SORT_ORDER.ASC);
        return lstShop;
    }

    public List<ProductOfferingTotalDTO> getLsProductOfferingForChangeProduct(String input, Long prodOfferIdOld, boolean isDuplicate) throws Exception {
        StringBuilder query = new StringBuilder(" ");
        query.append(" prod_offer_id: " + prodOfferIdOld);
        List<ProductOfferingTotalDTO> lstProductOfferingTotalDTOs = SolrController.doSearch(Const.SOLR_CORE.PRODUCTOFFERING_OFFERTYPE, query.toString(), ProductOfferingTotalDTO.class, Const.NUMBER_SEARCH.NUMBER_ALL, "name", Const.SORT_ORDER.ASC);
        if (!DataUtil.isNullOrEmpty(lstProductOfferingTotalDTOs)) {
            ProductOfferingTotalDTO productOfferingTotalDTO = lstProductOfferingTotalDTOs.get(0);
            query = new StringBuilder(" ");
            String inputTmp = DataUtil.safeToString(input).trim();
            if (DataUtil.isNullOrEmpty(inputTmp)) {
                query.append(" *:* ");
            } else {
                query.append(" name:*").append(inputTmp).append("* OR code:*").append(inputTmp).append("*");
            }
            if (!DataUtil.isNullObject(productOfferingTotalDTO)) {
                query.append(" AND product_offer_type_id: ").append(productOfferingTotalDTO.getProductOfferTypeId());
            }
            if (isDuplicate) {
                query.append(" AND NOT prod_offer_id: ").append(productOfferingTotalDTO.getProductOfferingId());
                query.append(" AND accounting_model_code: ").append(productOfferingTotalDTO.getAccountingModelCode());
            }
        }
        List<ProductOfferingTotalDTO> lstShop = SolrController.doSearch(Const.SOLR_CORE.PRODUCTOFFERING_OFFERTYPE, query.toString(), ProductOfferingTotalDTO.class, Const.NUMBER_SEARCH.NUMBER_ALL, "name", Const.SORT_ORDER.ASC);
        return lstShop;
    }

    @Override
    public List<ProductOfferingTotalDTO> getLsProductOfferingDTOByPrice(String input, List<Long> lsProductOfferTypeIds, Long ownerId, String ownerType,
                                                                        String stateId, Long priceTypeId, Long branchId, Long channelTypeId) throws Exception {
        List<ProductOfferingTotalDTO> result = Lists.newArrayList();
        List<ProductOfferPriceDTO> lstPrice = Lists.newArrayList();
        if (DataUtil.safeEqual(priceTypeId, Const.PRODUCT_OFFER_PRICE.PRICE_TYPE_DEPOSIT)) {
            lstPrice = productOfferPriceService.getPriceDepositAmount(null, branchId, null, channelTypeId, DataUtil.safeToLong(ownerType), ownerId);
        } else if (DataUtil.safeEqual(priceTypeId, Const.PRODUCT_OFFER_PRICE.PRICE_TYPE_PAY)) {
            lstPrice = productOfferPriceService.getPriceSaleAmount(null, branchId, null, channelTypeId, DataUtil.safeToLong(ownerType), ownerId);
        }
        if (DataUtil.isNullOrEmpty(lstPrice)) {
            return result;
        }
        List<Long> prodOfferIdList = Lists.newArrayList();
        for (ProductOfferPriceDTO productOfferPriceDTO : lstPrice) {
            prodOfferIdList.add(productOfferPriceDTO.getProdOfferId());
        }
        return getLsProductOfferingDTO(input, lsProductOfferTypeIds, ownerId, ownerType, stateId, prodOfferIdList);
    }


    @Override
    public List<ProductOfferingDTO> getListProductOffering() throws Exception {
        return mapper.toDtoBean(repository.getListProductOffering());
    }

    /**
     * 31122015_thaont19 lay danh sach productOffering cho Tien ich xem kho
     *
     * @return
     */
    @WebMethod
    @Override
    public List<ProductOfferingDTO> getListProductOfferTypeViewer(String ownerType, String ownerId, Long
            prodOfferId, Long stateId) throws LogicException, Exception {
        //validate
        if (DataUtil.isNullObject(ownerType)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.type.not.null");
        }

        if (Const.OWNER_TYPE.SHOP.equals(ownerType) && DataUtil.isNullObject(ownerId)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.invoice.info.require.shop");
        }
        if (Const.OWNER_TYPE.STAFF.equals(ownerType) && DataUtil.isNullObject(ownerId)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.invoice.info.require.staff");
        }
        List<ProductOfferingDTO> lstProduct = repository.getListProductOfferTypeViewer(ownerType, ownerId, prodOfferId, stateId);
        return lstProduct;
    }

    @WebMethod
    @Override
    public List<ProductOfferingDTO> getListProductOfferTypeForProcessStock(String ownerType, String ownerId, Long
            productOfferId, Long stateId) throws Exception {
        //validate
        if (DataUtil.isNullObject(ownerType)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.type.not.null");
        }

        if (Const.OWNER_TYPE.SHOP.equals(ownerType) && DataUtil.isNullObject(ownerId)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.invoice.info.require.shop");
        }
        if (Const.OWNER_TYPE.STAFF.equals(ownerType) && DataUtil.isNullObject(ownerId)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.invoice.info.require.staff");
        }
        List<ProductOfferingDTO> lstProduct = repository.getListProductOfferTypeForProcessStock(ownerType, ownerId, productOfferId, stateId);
        return lstProduct;
    }

    /**
     * 31122015_thaont19 lay danh sach productOffering cho Tien ich xem kho
     *
     * @return
     */
    @WebMethod
    @Override
    public List<ProductOfferingTotalDTO> getListProductOfferingViewer(String ownerType, String ownerId, Long
            prodOfferId) throws LogicException, Exception {
        //validate
        if (DataUtil.isNullObject(ownerType)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.type.not.null");
        }

        if (Const.OWNER_TYPE.SHOP.equals(ownerType) && DataUtil.isNullObject(ownerId)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.invoice.info.require.shop");
        }
        if (Const.OWNER_TYPE.STAFF.equals(ownerType) && DataUtil.isNullObject(ownerId)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.invoice.info.require.staff");
        }
        /*List<ProductOfferingTotalDTO> listStock = repository.getListProductOfferingViewer(ownerType, ownerId, prodOfferId);

        for (ProductOfferingTotalDTO stockTotal : listStock) {
            stockTotal.setStateName(getStockStateName(stockTotal.getStateId()));
        }*/
        return repository.getListProductOfferingViewer(ownerType, ownerId, prodOfferId);
    }

    public String getStockStateName(Long stateId) {
        String stateName = "";
        if (stateId != null) {
            Map<String, String> mapState;
            try {
                List<OptionSetValueDTO> lsState = optionSetValueService.getByOptionSetCode(Const.OPTION_SET.GOODS_STATE);
                mapState = lsState.stream().collect(Collectors.toMap(OptionSetValueDTO::getValue, OptionSetValueDTO::getName));
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
                mapState = Maps.newHashMap();
            }
            stateName = mapState.get(DataUtil.safeToString(stateId));
            /*if (DataUtil.safeEqual(stateId, 1L)) {
                stateName = getText("stock.total.state.new");
            } else if (DataUtil.safeEqual(stateId, 2L)) {
                stateName = getText("stock.total.state.old");
            } else if (DataUtil.safeEqual(stateId, 3L)) {
                stateName = getText("stock.total.state.broken");
            } else if (DataUtil.safeEqual(stateId, 4L)) {
                stateName = getText("stock.total.state.retake");
            } else if (DataUtil.safeEqual(stateId, 5L)) {
                stateName = getText("stock.total.state.rescue");
            } else if (DataUtil.safeEqual(stateId, 9L)) {
                stateName = getText("stock.total.state.doa");
            }*/
        }
        return stateName;
    }


    public List<ProductOfferingTotalDTO> getListProductOfferingForProcessStock(Long ownerType, Long ownerId, Long proOfferId, Long stateId) throws Exception {
        //validate
        if (DataUtil.isNullObject(ownerType)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.type.not.null");
        }

        if (Const.OWNER_TYPE.SHOP.equals(ownerType) && DataUtil.isNullObject(ownerId)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.invoice.info.require.shop");
        }
        if (Const.OWNER_TYPE.STAFF.equals(ownerType) && DataUtil.isNullObject(ownerId)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.invoice.info.require.staff");
        }
        List<ProductOfferingTotalDTO> listStock = repository.getListProductOfferingForProcessStock(ownerType, ownerId, proOfferId, stateId);

        for (ProductOfferingTotalDTO stockTotal : listStock) {
            if (stockTotal.getStateId() != null) {
                if (stockTotal.getStateId().equals(1L)) {
                    stockTotal.setStateName(getText("stock.total.state.new"));
                } else if (stockTotal.getStateId().equals(2L)) {
                    stockTotal.setStateName(getText("stock.total.state.old"));
                } else if (stockTotal.getStateId().equals(3L)) {
                    stockTotal.setStateName(getText("stock.total.state.broken"));
                } else if (stockTotal.getStateId().equals(4L)) {
                    stockTotal.setStateName(getText("stock.total.state.retake"));
                } else if (stockTotal.getStateId().equals(5L)) {
                    stockTotal.setStateName(getText("stock.total.state.rescue"));
                }
            }
        }
        return listStock;
    }

    @Override
    public List<ProductOfferingDTO> getListProductOfferingByProOfTypeId(Long productOfferTypeId, Long ownerId, String ownerType) throws Exception {
        return mapper.toDtoBean(repository.getListProductOfferingByProOfTypeId(productOfferTypeId, ownerId, ownerType));
    }

    @Override
    public List<ProductOfferingDTO> findByProductOfferTypeId(Long productOfferTypeId, Long id) throws Exception {
        List<FilterRequest> lst = Lists.newArrayList();
        lst.add(new FilterRequest(ProductOffering.COLUMNS.PRODUCTOFFERTYPEID.name(), FilterRequest.Operator.EQ, productOfferTypeId));
        lst.add(new FilterRequest(ProductOffering.COLUMNS.PRODUCTOFFERINGID.name(), FilterRequest.Operator.EQ, id));
        return findByFilter(lst);
    }

    @Override
    @Cacheable(cacheName = "productOfferingServiceImpl.getLsProductOfferingIsdnDTO")
    public List<ProductOfferingTotalDTO> getLsProductOfferingIsdnDTO(List<Long> lsProductOfferTypeIds, Long ownerId, String ownerType, String stateId) throws Exception {
//        StringBuilder query = new StringBuilder("");
//        if (productOfferTypeId != null) {
//            query.append(" product_offer_type_id:").append(productOfferTypeId);
//        }
//        if (ownerId != null) {
//            query.append(" AND owner_id: ").append(ownerId);
//        }
//        if (!DataUtil.isNullOrEmpty(ownerType)) {
//            query.append(" AND owner_type: ").append(ownerType);
//        }
//        if (DataUtil.isNullOrEmpty(query.toString())) {
//            query.append("*:*");
//        }

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT   a.prod_offer_id,a.product_offer_type_id,b.name productOfferTypeName,a.name,a.code,a.accounting_model_code,a.accounting_model_name,a.unit,o.name unitName");
        sql.append("  FROM   Product_Offering a, Product_Offer_Type b, option_set_value o,option_set s");
        sql.append(" WHERE   a.product_offer_type_id = b.product_offer_type_id");
        sql.append(" and b.table_name='STOCK_NUMBER'");
        sql.append(" and b.status=1");
        sql.append(" and a.status=1");
        sql.append(" and b.parent_id=100");
        sql.append(" and b.product_offer_type_id " + DbUtil.createInQuery("product_offer_type_id", lsProductOfferTypeIds));
        sql.append(" and s.code = 'STOCK_MODEL_UNIT'");
        sql.append("  AND s.id = o.option_set_id");
        sql.append(" and a.unit=o.value");
        sql.append(" order by nlssort(a.name, 'nls_sort=Vietnamese') asc");
        Query hQuery = em.createNativeQuery(sql.toString());
        DbUtil.setParamInQuery(hQuery, "product_offer_type_id", lsProductOfferTypeIds);
        List<Object[]> list = hQuery.getResultList();
        List<ProductOfferingTotalDTO> lstShop = Lists.newArrayList();
        List<OptionSetValueDTO> lstValue = optionSetValueService.getByOptionsetCodeAndValue(Const.OPTION_SET.STOCK_STATE, Const.GOODS_STATE.NEW + "");
        String stateName = "";
        if (!DataUtil.isNullOrEmpty(lstValue)) {
            stateName = lstValue.get(0).getName();
        }
        for (Object[] ob : list) {
            int index = 0;
            ProductOfferingTotalDTO productOfferingTotalDTO = new ProductOfferingTotalDTO();
            productOfferingTotalDTO.setProductOfferingId(DataUtil.safeToLong(ob[index++]));
            productOfferingTotalDTO.setProductOfferTypeId(DataUtil.safeToLong(ob[index++]));
            productOfferingTotalDTO.setProductOfferTypeName(DataUtil.safeToString(ob[index++]));
            productOfferingTotalDTO.setName(DataUtil.safeToString(ob[index++]));
            productOfferingTotalDTO.setCode(DataUtil.safeToString(ob[index++]));
            productOfferingTotalDTO.setAccountingModelCode(DataUtil.safeToString(ob[index++]));
            productOfferingTotalDTO.setAccountingModelName(DataUtil.safeToString(ob[index++]));
            productOfferingTotalDTO.setUnit(DataUtil.safeToString(ob[index++]));
            productOfferingTotalDTO.setUnitName(DataUtil.safeToString(ob[index]));
            productOfferingTotalDTO.setStateId(Const.GOODS_STATE.NEW);
            productOfferingTotalDTO.setStateName(stateName);

            lstShop.add(productOfferingTotalDTO);
        }

//        List<ProductOfferingTotalDTO> lstShop = SolrController.doSearch(Const.SOLR_CORE.PRODUCTOFFERING, query.toString(), ProductOfferingTotalDTO.class, Const.NUMBER_SEARCH.NUMBER_ALL, "name", Const.SORT_ORDER.ASC);
        return lstShop;
    }

    @Override
    public ProductOfferingDTO findByProductOfferCode(String productOffderCode, String status) throws Exception {
        List<FilterRequest> requests = Lists.newArrayList();
        requests.add(new FilterRequest(ProductOffering.COLUMNS.CODE.name(), FilterRequest.Operator.EQ, productOffderCode));
        if (!DataUtil.isNullOrEmpty(status)) {
            requests.add(new FilterRequest(ProductOffering.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, status));
        }
        List<ProductOfferingDTO> lsProduct = findByFilter(requests);
        if (!DataUtil.isNullOrEmpty(lsProduct)) {
            return lsProduct.get(0);
        }
        return null;
    }

    /**
     * 31122015_thaont19 lay danh sach productOffering cho Tien ich xem kho
     *
     * @return
     */
    @WebMethod
    @Override
    public List<ProductOfferingTotalDTO> getListProductOfferingTotalDistinct(String ownerType, String ownerId, String stockCode) throws LogicException, Exception {
        //validate
        if (DataUtil.isNullObject(ownerType)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.type.not.null");
        }

        if (Const.OWNER_TYPE.SHOP.equals(ownerType) && DataUtil.isNullObject(ownerId)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.invoice.info.require.shop");
        }
        if (Const.OWNER_TYPE.STAFF.equals(ownerType) && DataUtil.isNullObject(ownerId)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.invoice.info.require.staff");
        }
        List<ProductOfferingTotalDTO> listStock = repository.getListProductOfferingTotalDistinct(ownerType, ownerId, stockCode);

        for (ProductOfferingTotalDTO stockTotal : listStock) {
            if (stockTotal.getStateId() != null) {
                if (stockTotal.getStateId().equals(1L)) {
                    stockTotal.setStateName(getText("stock.total.state.new"));
                } else if (stockTotal.getStateId().equals(2L)) {
                    stockTotal.setStateName(getText("stock.total.state.old"));
                } else if (stockTotal.getStateId().equals(3L)) {
                    stockTotal.setStateName(getText("stock.total.state.broken"));
                } else if (stockTotal.getStateId().equals(4L)) {
                    stockTotal.setStateName(getText("stock.total.state.retake"));
                } else if (stockTotal.getStateId().equals(5L)) {
                    stockTotal.setStateName(getText("stock.total.state.rescue"));
                }
            }
        }
        return listStock;
    }

    @Override
    public List<ProductOfferingTotalDTO> getListProductForTag(String input, ConfigProductTagDTO config) {
        ProductOfferingTotalDTO searchProduct = config.getSearchProduct();
        StringBuilder query = new StringBuilder("");
        query.append("*:*");

        if (!DataUtil.isNullObject(searchProduct.getProductOfferTypeId())) {
            query.append(" AND product_offer_type_id:").append(searchProduct.getProductOfferTypeId());
        }

        if (!DataUtil.isNullOrEmpty(input)) {
            query.append(" AND (name:*{0}* OR code:*{1}*)");
            query = new StringBuilder(MessageFormat.format(query.toString(), DataUtil.replaceSpaceSolr(input), DataUtil.replaceSpaceSolr(input)));
        }
        List<ProductOfferingTotalDTO> lstShop = SolrController.doSearch(Const.SOLR_CORE.PRODUCTOFFERING_OFFERTYPE, query.toString(), ProductOfferingTotalDTO.class, Const.NUMBER_SEARCH.NUMBER_ALL, "name", Const.SORT_ORDER.ASC);
        return lstShop;
    }

    @Override
    public ProfileDTO getProfileByProductId(Long productOfferId) throws Exception {
        return productWs.getProfileByProductOfferId(productOfferId);
    }

    @Override
    public List<ProductOfferingTotalDTO> getProductOfferingTotalDTO(String input, Long productOfferTypeId, Long ownerId, Long ownerType, Long stateId) throws Exception {
        StringBuilder query = new StringBuilder("");
        if (!DataUtil.isNullOrEmpty(input)) {
            query.append(" name:*").append(input).append("* OR code:*").append(input).append("*").append(" AND ");
        }
        if (productOfferTypeId != null) {
            query.append(" product_offer_type_id:").append(productOfferTypeId);
        }
        if (ownerId != null) {
            query.append(" AND owner_id: ").append(ownerId);
        }
        if (!DataUtil.isNullObject(ownerType)) {
            query.append(" AND owner_type: ").append(ownerType);
        }
        if (!DataUtil.isNullObject(stateId)) {
            query.append(" AND state_id: ").append(stateId);
        }
        if (productOfferTypeId != null) {
            query.append(" AND current_quantity: [1 TO *] ");
        }
        if (DataUtil.isNullOrEmpty(query.toString())) {
            query.append("*:*");
        }
        List<ProductOfferingTotalDTO> lstProduct = SolrController.doSearch(Const.SOLR_CORE.PRODUCTOFFERING, query.toString(), ProductOfferingTotalDTO.class, Const.NUMBER_SEARCH.NUMBER_ALL, "name", Const.SORT_ORDER.ASC);
        return lstProduct;
    }

    @Override
    public List<ProductOfferingTotalDTO> getProductOfferingMaterial(String input) throws Exception {
        StringBuilder query = new StringBuilder("");
        if (!DataUtil.isNullOrEmpty(input)) {
            query.append(" name:*").append(input.trim()).append("* OR code:*").append(input.trim()).append("*").append(" AND ");
        }
        query.append(" product_offer_type_id:").append(Const.PRODUCT_OFFER_TYPE.PRODUCT_NO_SERIAL);
        query.append(" AND stock_model_type: 2");
        if (DataUtil.isNullOrEmpty(query.toString())) {
            query.append("*:*");
        }
        List<ProductOfferingTotalDTO> lstProduct = SolrController.doSearch(Const.SOLR_CORE.PRODUCTOFFERING_OFFERTYPE, query.toString(),
                ProductOfferingTotalDTO.class, Const.NUMBER_SEARCH.NUMBER_ALL, "name", Const.SORT_ORDER.ASC);
        return lstProduct;
    }

    @Override
    public ProductOfferingDTO getProdOfferDtoByCodeAndStock(String prodOfferCode, Long ownerId, Long stateId) {
        return repository.getProdOfferDtoByCodeAndStock(prodOfferCode, ownerId, stateId);
    }

    @Override
    public ProductOfferingDTO getProdOfferDtoByCodeAndStockNew(String prodOfferCode, Long ownerId, Long ownerType, Long stateId) {
        return repository.getProdOfferDtoByCodeAndStockNew(prodOfferCode, ownerId, ownerType, stateId);
    }

    @Override
    @WebMethod
    public List<Long> getProdOfferIdForRequestBalanceExport(Long ownerId, Long ownerType, Long prodOfferId) {
        return repository.getProdOfferIdForRequestBalanceExport(ownerId, ownerType, prodOfferId);
    }

    @Override
    @WebMethod
    public List<Long> getProdOfferIdForRequestBalanceImport(Long ownerId, Long ownerType, Long prodOfferId) {
        return repository.getProdOfferIdForRequestBalanceImport(ownerId, ownerType, prodOfferId);
    }

    @Override
    public List<ProductOfferingTotalDTO> getProductOfferingByListId(List<Long> prodOfferIds) throws Exception {
        return repository.getProductOfferingByListId(prodOfferIds);
    }

    @Override
    public List<ProductOfferingDTO> getLsProductOfferDtoByTypeId(Long productOfferTypeId) throws Exception {
        return repository.getLsProductOfferDtoByTypeId(productOfferTypeId);
    }

    public List<ProductInfoDTO> getLstProductInfo(Long stockTransId) throws Exception {
        return repository.getLstProductInfo(stockTransId);
    }

    @Override
    public ProductOfferingDTO getProductByCode(String prodOfferCode) throws Exception {
        List<FilterRequest> requests = Lists.newArrayList();
        requests.add(new FilterRequest(ProductOffering.COLUMNS.CODE.name(), FilterRequest.Operator.EQ, prodOfferCode));
        requests.add(new FilterRequest(ProductOffering.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, Const.STATUS_ACTIVE));
        List<ProductOfferingDTO> lsProduct = findByFilter(requests);
        if (!DataUtil.isNullOrEmpty(lsProduct)) {
            return lsProduct.get(0);
        }
        return null;
    }

    public List<ProductOfferingLogisticDTO> getProductOfferingLogistic(Long stockTransId) throws Exception {
        return repository.getProductOfferingLogistic(stockTransId);
    }

    @Override
    public List<ProductOfferingTotalDTO> getLsRequestProductByShop(Long ownerType, Long ownerId, Long prodOfferTypeId, Long prodOfferId, Long stateId) throws Exception {
        return repository.getLsRequestProductByShop(ownerType, ownerId, prodOfferTypeId, prodOfferId, stateId);
    }

    @Override
    public List<ProductOfferingTotalDTO> getLsProOfferTotalDTORequest(String input, Long prodOfferTypeId, Long stateId) throws Exception {
        StringBuilder query = new StringBuilder("");
        String inputTmp = DataUtil.replaceSpaceSolr(input).trim();
        if (!DataUtil.isNullOrEmpty(inputTmp)) {
            query.append(" name:*").append(inputTmp).append("* OR code:*").append(inputTmp).append("*").append(" AND ");
        }
        if (!DataUtil.isNullOrZero(prodOfferTypeId)) {
            query.append(" product_offer_type_id:").append(prodOfferTypeId);
        }
        if (!DataUtil.isNullOrZero(stateId)) {
            query.append(" AND state_id: ").append(stateId);
        } else {
            query.append(" AND (state_id:4 OR state_id:12) ");
        }
        if (!DataUtil.isNullOrZero(prodOfferTypeId)) {
            query.append(" AND current_quantity: [1 TO *] ");
        }
        if (DataUtil.isNullOrEmpty(query.toString())) {
            query.append("*:*");
        }
        List<ProductOfferingTotalDTO> lstProduct = SolrController.doSearch(Const.SOLR_CORE.PRODUCTOFFERING, query.toString(), ProductOfferingTotalDTO.class, Const.NUMBER_SEARCH.NUMBER_ALL, "name", Const.SORT_ORDER.ASC);
        return lstProduct;
    }

    @Override
    public ProductOfferingDTO getProductByCodeIm1(String prodOfferCode) throws Exception {
        return repository.getProductByCodeIm1(prodOfferCode);
    }

    @Override
    public List<ProductOfferingDTO> getInventoryInfoWarranty(List<Long> listProdOfferId) throws Exception {
        return repository.getInventoryInfoWarranty(listProdOfferId);
    }

    public List<ProductOfferingTotalDTO> getProductExportDemo(String input, Long ownerId, String ownerType, String stateId) throws Exception {
        StringBuilder query = new StringBuilder("*:*");
        if (ownerId != null) {
            query.append(" AND owner_id: ").append(ownerId);
        }
        if (!DataUtil.isNullObject(ownerType)) {
            query.append(" AND owner_type: ").append(ownerType);
        }
        if (!DataUtil.isNullObject(stateId)) {
            query.append(" AND state_id: ").append(stateId);
        }
        List<OptionSetValueDTO> lstCogfigDemo = optionSetValueService.getByOptionSetCode("PRODUCT_OFFER_DEMO");
        if (!DataUtil.isNullOrEmpty(lstCogfigDemo)) {
            query.append(" AND (");
            for (OptionSetValueDTO optionSetValueDTO : lstCogfigDemo) {
                query.append(" code:*_").append(optionSetValueDTO.getValue()).append(" OR");
            }
            query.delete(query.length() - 3, query.length());
            query.append(")");
        }
        if (!DataUtil.isNullOrEmpty(input)) {
            query.append(" AND ( name:*").append(input).append("* OR code:*").append(input).append("* )");
        }
        List<ProductOfferingTotalDTO> lstProductOfferings = DataUtil.defaultIfNull(SolrController.doSearch(Const.SOLR_CORE.PRODUCTOFFERING, query.toString(), ProductOfferingTotalDTO.class, Const.NUMBER_SEARCH.NUMBER_ALL, "name", Const.SORT_ORDER.ASC), Lists.newArrayList());
        return lstProductOfferings;
    }
}
