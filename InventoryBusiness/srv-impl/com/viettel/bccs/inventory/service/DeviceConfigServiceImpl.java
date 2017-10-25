package com.viettel.bccs.inventory.service;
import com.google.common.collect.Lists;
import com.mysema.query.types.expr.BooleanExpression;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.controller.SolrController;
import com.viettel.bccs.inventory.dto.DeviceConfigDTO;
import com.viettel.bccs.inventory.dto.DeviceConfigDetailDTO;
import com.viettel.bccs.inventory.dto.ProductOfferingDTO;
import com.viettel.bccs.inventory.dto.ProductOfferingTotalDTO;
import com.viettel.bccs.inventory.model.DeviceConfig;
import com.viettel.bccs.inventory.model.QDeviceConfig;
import com.viettel.bccs.inventory.model.QStockDeviceTransfer;
import com.viettel.bccs.inventory.repo.DeviceConfigRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.text.MessageFormat;
import java.util.*;

import com.viettel.fw.dto.BaseMessage;
import org.apache.log4j.Logger;
import org.opensaml.xml.encryption.Q;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.QSort;
import org.springframework.stereotype.Service;
import com.viettel.fw.service.BaseServiceImpl;
import javax.jws.WebMethod;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

@Service
public class DeviceConfigServiceImpl extends BaseServiceImpl implements DeviceConfigService {


    private final BaseMapper<DeviceConfig,DeviceConfigDTO> mapper = new BaseMapper<>(DeviceConfig.class,DeviceConfigDTO.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;
    @Autowired
    private DeviceConfigRepo repo;
    @Autowired
    private ProductOfferingService productOfferingService;

    public static final Logger logger = Logger.getLogger(DeviceConfigService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repo.count(repo.toPredicate(filters));
    }

    @WebMethod
    public DeviceConfigDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repo.findOne(id));
    }

    @WebMethod
    public List<DeviceConfigDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        return mapper.toDtoBean(repo.findAll(repo.toPredicate(filters)));
    }

    @WebMethod
    @Override
    public List<DeviceConfigDTO> searchDeviceByProductOfferIdAndStateId(Long productOfferId, Long stateId) throws Exception {
        return repo.searchDeviceByProductOfferIdAndStateId(productOfferId, stateId);
    }


    @WebMethod
    @Override
    public List<ProductOfferingTotalDTO> getLsProductOfferingByProductTypeAndState(String input, Long prodTypeId, Long stateId) throws Exception {
        StringBuilder query = new StringBuilder(" *:* ");
        if(!DataUtil.isNullOrEmpty(input) && !DataUtil.isNullOrZero(prodTypeId)) {
            query.append(" AND (name:*{0}* OR code:*{1}*) AND product_offer_type_id:" + prodTypeId + " ");
            query = new StringBuilder(MessageFormat.format(query.toString(), new Object[]{DataUtil.replaceSpaceSolr(input), DataUtil.replaceSpaceSolr(input)}));
        } else if(DataUtil.isNullOrEmpty(input) && !DataUtil.isNullOrZero(prodTypeId)) {
            query.append(" AND product_offer_type_id:" + prodTypeId + " ");
        }

        if(prodTypeId == Const.DEVICE_CONFIG.ITEM_OFFER_TYPE_ID){
            query.append(" AND (name:*BH_* OR code:*BH_*) ");
        }

        if(stateId != null) {
            query.append(" AND state_id: ").append(stateId);
        }

        try {
            return SolrController.doSearch(Const.SOLR_CORE.PRODUCTOFFERING_OFFERTYPE, query.toString(), ProductOfferingTotalDTO.class, Const.NUMBER_SEARCH.NUMBER_ALL, "name", "ASC");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @WebMethod
    @Override
    public BaseMessage createDeviceConfig(String username, DeviceConfigDTO addDeviceConfig, List<DeviceConfigDetailDTO> listAddDeviceConfigDetail) throws Exception, LogicException {

        if(addDeviceConfig == null){
            throw new LogicException("","deviceConfig.require.product");
        }

        if(DataUtil.isNullOrEmpty(listAddDeviceConfigDetail)){
            throw new LogicException("","deviceConfig.require.item");
        }

        checkProduct(addDeviceConfig.getProdOfferId()); //check product is invalid or not
        for(DeviceConfigDetailDTO deviceConfigDetailDTO : listAddDeviceConfigDetail) {
            if(deviceConfigDetailDTO.getProductOfferingTotalDTO() == null || deviceConfigDetailDTO.getProductOfferingTotalDTO().getProductOfferingId() == null)
                continue;
            checkProduct(deviceConfigDetailDTO.getProductOfferingTotalDTO().getProductOfferingId());// the same above
        }
        checkDuplicateItem(listAddDeviceConfigDetail); // check duplicate item
        BaseMessage baseMessage = checkProductIsConfigWithState(addDeviceConfig.getProdOfferId(), addDeviceConfig.getStateId());//checkProductIdConfig(addDeviceConfig, listAddDeviceConfigDetail); // check config before
        if(baseMessage.isSuccess()) {
            baseMessage.setSuccess(false);
            baseMessage.setKeyMsg("deviceConfig.config.duplicate.product");
            baseMessage.setParamsMsg(new String[]{addDeviceConfig.getProductOfferName(), getStateStr(addDeviceConfig.getStateId())});
            return baseMessage;
        }
        Date sysDate = DbUtil.getSysDate(em);
        for(DeviceConfigDetailDTO deviceConfigDetailDTO : listAddDeviceConfigDetail){
            if(deviceConfigDetailDTO.getProductOfferingTotalDTO().getProductOfferingId() == null)
                continue;
            DeviceConfigDTO deviceConfigDTO = new DeviceConfigDTO();
            deviceConfigDTO.setProdOfferId(addDeviceConfig.getProdOfferId());
            deviceConfigDTO.setStateId(addDeviceConfig.getStateId());
            deviceConfigDTO.setStatus(Const.STATUS_ACTIVE);
            deviceConfigDTO.setNewProdOfferId(deviceConfigDetailDTO.getProductOfferingTotalDTO().getProductOfferingId());
            deviceConfigDTO.setCreateDate(sysDate);
            deviceConfigDTO.setUpdateDate(sysDate);
            deviceConfigDTO.setCreateUser(username);
            deviceConfigDTO.setUpdateUser(username);

            repo.saveAndFlush(mapper.toPersistenceBean(deviceConfigDTO));
        }

        return new BaseMessage(true);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseMessage createDeviceConfigImport(String username, List<DeviceConfigDTO> addDeviceConfig) throws Exception {

        for(DeviceConfigDTO deviceConfigDTO : addDeviceConfig){
            BaseMessage baseMessage = createDeviceConfig(username, deviceConfigDTO, deviceConfigDTO.getListAddDeviceConfigDetail());
            if(!baseMessage.isSuccess())
                return baseMessage;
        }

        return new BaseMessage(true);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseMessage updateDeviceConfig(String username, DeviceConfigDTO addDeviceConfig, List<DeviceConfigDetailDTO> listAddDeviceConfigDetail) throws Exception {

        if(addDeviceConfig == null){
            throw new LogicException("","deviceConfig.require.product");
        }

        if(DataUtil.isNullOrEmpty(listAddDeviceConfigDetail)){
            throw new LogicException("","deviceConfig.require.item");
        }

        checkProduct(addDeviceConfig.getProdOfferId()); //check product is invalid or not
        for(DeviceConfigDetailDTO deviceConfigDetailDTO : listAddDeviceConfigDetail) {
            if(deviceConfigDetailDTO.getProductOfferingTotalDTO() == null || deviceConfigDetailDTO.getProductOfferingTotalDTO().getProductOfferingId() == null)
                continue;
            checkProduct(deviceConfigDetailDTO.getProductOfferingTotalDTO().getProductOfferingId());// the same above
        }
        checkDuplicateItem(listAddDeviceConfigDetail); // check duplicate item
        BaseMessage baseMessage = checkProductIdConfig(addDeviceConfig, listAddDeviceConfigDetail); // check config before
        if(!baseMessage.isSuccess())
            return baseMessage;
        Date sysDate = DbUtil.getSysDate(em);

        boolean canDelete = repo.checkCanDelete(addDeviceConfig.getProdOfferId(), addDeviceConfig.getStateId());

        for(DeviceConfigDetailDTO deviceConfigDetailDTO : listAddDeviceConfigDetail){
            if(deviceConfigDetailDTO.getProductOfferingTotalDTO().getProductOfferingId() == null)
                continue;

            if(!canDelete && Const.STATUS_DELETE.equals(deviceConfigDetailDTO.getStatus()))
                throw new LogicException("", "deviceConfig.msg.delete.fail.pendingApproach");

            if(deviceConfigDetailDTO.getDeviceConfigDTO() != null){
                deviceConfigDetailDTO.getDeviceConfigDTO().setUpdateDate(sysDate);
                deviceConfigDetailDTO.getDeviceConfigDTO().setUpdateUser(username);
                deviceConfigDetailDTO.getDeviceConfigDTO().setNewProdOfferId(deviceConfigDetailDTO.getProductOfferingTotalDTO().getProductOfferingId());
                repo.saveAndFlush(mapper.toPersistenceBean(deviceConfigDetailDTO.getDeviceConfigDTO()));
            } else {
                DeviceConfigDTO deviceConfigDTO = new DeviceConfigDTO();
                deviceConfigDTO.setProdOfferId(addDeviceConfig.getProdOfferId());
                deviceConfigDTO.setStateId(addDeviceConfig.getStateId());
                deviceConfigDTO.setStatus(Const.STATUS_ACTIVE);
                deviceConfigDTO.setNewProdOfferId(deviceConfigDetailDTO.getProductOfferingTotalDTO().getProductOfferingId());
                deviceConfigDTO.setCreateDate(sysDate);
                deviceConfigDTO.setUpdateDate(sysDate);
                deviceConfigDTO.setCreateUser(username);
                deviceConfigDTO.setUpdateUser(username);

                repo.saveAndFlush(mapper.toPersistenceBean(deviceConfigDTO));
            }
        }

        return new BaseMessage(true);
    }

    @WebMethod
    @Override
    public BaseMessage checkProductIdConfigIsConfig(Long probOfferId, Long stateId, Long newProbOfferId) throws Exception {
        return checkProductIdConfigIsConfig(null, probOfferId ,stateId, newProbOfferId);
    }

    @Override
    public BaseMessage checkProductIsConfigWithState(Long probOfferId, Long stateId) throws Exception {
        return new BaseMessage(repo.checkProductIsConfigWithState(probOfferId, stateId));
    }

    @Override
    public BaseMessage checkProductIsConfigWithStateForUpdate(Long probOfferId, Long stateId) throws Exception, LogicException {
        return new BaseMessage(repo.checkProductIsConfigWithState(probOfferId, stateId, true));
    }

    public BaseMessage checkProductIdConfig(DeviceConfigDTO addDeviceConfig, List<DeviceConfigDetailDTO> listAddDeviceConfigDetail) throws Exception {
        for(DeviceConfigDetailDTO deviceConfigDetailDTO : listAddDeviceConfigDetail){
            BaseMessage isConfig;
            if(deviceConfigDetailDTO.getDeviceConfigDTO() == null)
                isConfig = checkProductIdConfigIsConfig(addDeviceConfig.getProdOfferId(), addDeviceConfig.getStateId(), deviceConfigDetailDTO.getProductOfferingTotalDTO().getProductOfferingId());
            else
                isConfig = checkProductIdConfigIsConfig(deviceConfigDetailDTO.getDeviceConfigDTO().getId(), addDeviceConfig.getProdOfferId(), addDeviceConfig.getStateId(), deviceConfigDetailDTO.getProductOfferingTotalDTO().getProductOfferingId());
            if(isConfig.isSuccess()){
                BaseMessage baseMessage = new BaseMessage(false);
                baseMessage.setKeyMsg("deviceConfig.msg.configDuplicate", addDeviceConfig.getProductOfferName(), repo.getStateStr(addDeviceConfig.getStateId()),deviceConfigDetailDTO.getProductOfferingTotalDTO().getName());
                return baseMessage;
            }
        }
        return new BaseMessage(true);
    }

    @Override
    public String getStateStr(Long stateId) {
        return repo.getStateStr(stateId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<DeviceConfigDTO> listIds, String staffCode) throws Exception, LogicException {

        if(DataUtil.isNullOrEmpty(listIds)){
            throw new LogicException("", "deviceConfig.msg.delete.empty");
        }

        for(DeviceConfigDTO deviceConfigDTO : listIds){
            if(!repo.checkCanDelete(deviceConfigDTO.getProdOfferId(), deviceConfigDTO.getStateId()))
                throw new LogicException("", "deviceConfig.msg.delete.fail.pendingApproach");
        }

        Date sysdate = DbUtil.getSysDate(em);
        for(DeviceConfigDTO deviceConfigDTO : listIds){
            BooleanExpression predicate = QDeviceConfig.deviceConfig.prodOfferId.eq(deviceConfigDTO.getProdOfferId());
            predicate = predicate.and(QDeviceConfig.deviceConfig.stateId.eq(deviceConfigDTO.getStateId()));

            List<DeviceConfig> deviceConfigs = Lists.newArrayList(repo.findAll(predicate));
            if(DataUtil.isNullOrEmpty(deviceConfigs))
                throw new LogicException("", "deviceConfig.msg.delete.fail.product", deviceConfigDTO.getOfferTypeName(), getStateStr(deviceConfigDTO.getStateId()));

            for(DeviceConfig deviceConfig : deviceConfigs){
                deviceConfig.setStatus(Const.STATUS_DELETE);
                deviceConfig.setUpdateUser(staffCode);
                deviceConfig.setUpdateDate(sysdate);

                repo.saveAndFlush(deviceConfig);
            }
        }

    }

    @Override
    public List<DeviceConfigDTO> getDeviceConfigByProductAndState(Long probOfferId, Long stateId) {
        BooleanExpression predicate = QDeviceConfig.deviceConfig.prodOfferId.eq(probOfferId);
        predicate = predicate.and(QDeviceConfig.deviceConfig.stateId.eq(stateId));
        predicate = predicate.and(QDeviceConfig.deviceConfig.status.ne(Const.STATUS_DELETE));
        return mapper.toDtoBean(repo.findAll(predicate));
    }

    @Override
    public List<DeviceConfigDTO> getDeviceConfigInfo() {
        return repo.getDeviceConfigInfo();
    }

    public BaseMessage checkProductIdConfigIsConfig(Long id, Long probOfferId, Long stateId, Long newProbOfferId) throws Exception {
        return new BaseMessage(repo.checkProductIdConfig(id, probOfferId, stateId, newProbOfferId));
    }

    private String checkDuplicateItem(List<DeviceConfigDetailDTO> listAddDeviceConfigDetail) throws LogicException {
        Map<Long, DeviceConfigDetailDTO> map2DeviceConfig = new HashMap<>();
        for(DeviceConfigDetailDTO deviceConfigDetailDTO : listAddDeviceConfigDetail){

            if(deviceConfigDetailDTO.getProductOfferingTotalDTO().getProductOfferingId() == null)
                continue;

            if(map2DeviceConfig.get(deviceConfigDetailDTO.getProductOfferingTotalDTO().getProductOfferingId()) == null)
                map2DeviceConfig.put(deviceConfigDetailDTO.getProductOfferingTotalDTO().getProductOfferingId(), deviceConfigDetailDTO);
            else
                throw new LogicException("", "deviceConfig.duplicateItem");
        }
        return "";
    }

    private void checkProduct(Long id) throws LogicException{
        ProductOfferingDTO productOfferingDTO;
        try {
            productOfferingDTO = productOfferingService.findOne(id);
            if(productOfferingDTO == null || productOfferingDTO.getProductOfferTypeId() == null || productOfferingDTO.getStatus() == null || !"1".equals(productOfferingDTO.getStatus())) {
                throw new LogicException("105", "balance.valid.prodInfo");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new LogicException("105", "balance.valid.prodInfo");
        }
    }

}
