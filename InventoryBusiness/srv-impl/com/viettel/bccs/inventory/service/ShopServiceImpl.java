package com.viettel.bccs.inventory.service;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.controller.SolrController;
import com.viettel.bccs.inventory.dto.ChannelTypeDTO;
import com.viettel.bccs.inventory.dto.MvShopStaffDTO;
import com.viettel.bccs.inventory.dto.ShopDTO;
import com.viettel.bccs.inventory.dto.VShopStaffDTO;
import com.viettel.bccs.inventory.model.Shop;
import com.viettel.bccs.inventory.repo.ShopRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.bouncycastle.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.jws.WebMethod;
import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShopServiceImpl extends BaseServiceImpl implements ShopService {

    private final BaseMapper<Shop, ShopDTO> mapper = new BaseMapper(Shop.class, ShopDTO.class);

    @Autowired
    private ShopRepo repository;
    public static final Logger logger = Logger.getLogger(ShopService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public ShopDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<ShopDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<ShopDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(ShopDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    public ShopDTO save(ShopDTO shopDTO) throws Exception {
        return mapper.toDtoBean(repository.save(mapper.toPersistenceBean(shopDTO)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(ShopDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @Override
    public List<ShopDTO> getListShopByParentShopId(Long parentShopId) throws Exception, LogicException {
        return repository.getListShopByParentShopId(parentShopId);
    }

    @Override
    public List<ShopDTO> getListAllBranch() throws Exception {
        return repository.getListAllBranch();
    }

    @Override
    public List<VShopStaffDTO> getListShop(String shopPath, String txtSearch, String parentShopId, boolean isCurrentAndChildShop, boolean isDismissType) throws LogicException, Exception {

        String query = "owner_type:" + Const.OWNER_TYPE.SHOP + " AND status:1 " + " AND shop_path:" + shopPath + "* AND channel_type_id:(1 8 22) ";
        if (!DataUtil.isNullOrEmpty(txtSearch)) {
            query += " AND (owner_code:*{0}* OR owner_name:*{1}*)";
            query = MessageFormat.format(query, DataUtil.replaceSpaceSolr(txtSearch), DataUtil.replaceSpaceSolr(txtSearch));
        }
        if (isCurrentAndChildShop) {
            query += " AND (owner_id: " + parentShopId + " OR parent_shop_id:" + parentShopId + ") ";
            if (!isDismissType) {
                query += " AND shop_type:2 AND NOT owner_id:(210451 OR"
                        + "                     105501 OR"
                        + "                     19404 OR"
                        + "                     8136 OR"
                        + "                     8138 OR"
                        + "                     210149 OR"
                        + "                     105540 OR"
                        + "                     107794) AND parent_shop_id:" + Const.VT_SHOP_ID;
            }
        } else if (!DataUtil.isNullOrEmpty(parentShopId)) {
            query += " AND parent_shop_id:" + parentShopId;
        }

        List<VShopStaffDTO> lstShop = SolrController.doSearch(Const.SOLR_CORE.VSHOPSTAFF, query, VShopStaffDTO.class, Const.NUMBER_SEARCH.NUMBER_ALL, "owner_name", Const.SORT_ORDER.ASC);
        return lstShop;
    }

    @Override
    public List<VShopStaffDTO> getListShopTransfer(String txtSearch, String parrentShopId) throws LogicException, Exception {

        List<VShopStaffDTO> lstShopResult = Lists.newArrayList();

        String query = "owner_type:" + Const.OWNER_TYPE.SHOP + " AND status:1 ";
        if (!DataUtil.isNullOrEmpty(txtSearch)) {
            query += " AND (owner_code:*{0}* OR owner_name:*{1}*)";
            query = MessageFormat.format(query, DataUtil.replaceSpaceSolr(txtSearch), DataUtil.replaceSpaceSolr(txtSearch));
        }
        //1. neu la VT thi lay ban than no va con
        if (Const.SHOP.SHOP_VTT_ID.equals(DataUtil.safeToLong(parrentShopId))) {
            query += " AND (owner_id: " + parrentShopId + " OR parent_shop_id:" + parrentShopId + ") ";
            List<VShopStaffDTO> lstShopCurrent = SolrController.doSearch(Const.SOLR_CORE.VSHOPSTAFF, query, VShopStaffDTO.class, Const.NUMBER_SEARCH.NUMBER_ALL, "owner_name", Const.SORT_ORDER.ASC);
            if (!DataUtil.isNullOrEmpty(lstShopCurrent)) {
                lstShopResult.addAll(lstShopCurrent);
            }
        } else {
            //lay ra thang hien tai
            String solrQueryCurrent = query + " AND owner_id: " + parrentShopId;
            List<VShopStaffDTO> lsShopCurrent = SolrController.doSearch(Const.SOLR_CORE.VSHOPSTAFF, solrQueryCurrent, VShopStaffDTO.class, Const.NUMBER_SEARCH.NUMBER_ALL, "owner_name", Const.SORT_ORDER.ASC);

            if (!DataUtil.isNullOrEmpty(lsShopCurrent) && DataUtil.safeEqual(lsShopCurrent.get(0).getParentShopId(), Const.SHOP.SHOP_VTT_ID)) {
                String solrQueryParrent = query + " AND owner_id: " + lsShopCurrent.get(0).getParentShopId();
                List<VShopStaffDTO> lsShopParrent = SolrController.doSearch(Const.SOLR_CORE.VSHOPSTAFF, solrQueryParrent, VShopStaffDTO.class, Const.NUMBER_SEARCH.NUMBER_ALL, "owner_name", Const.SORT_ORDER.ASC);
                if (!DataUtil.isNullOrEmpty(lsShopParrent)) {
                    lstShopResult.addAll(lsShopParrent);
                }
            }

            String solrQueryChild = query + " AND parent_shop_id: " + parrentShopId;
            List<VShopStaffDTO> lsShopChild = SolrController.doSearch(Const.SOLR_CORE.VSHOPSTAFF, solrQueryChild, VShopStaffDTO.class, Const.NUMBER_SEARCH.NUMBER_ALL, "owner_name", Const.SORT_ORDER.ASC);

            if (!DataUtil.isNullOrEmpty(lsShopChild)) {
                lstShopResult.addAll(lsShopChild);
            }
        }
        return lstShopResult;
    }

    public List<VShopStaffDTO> getListShopBranchAndVTT(String txtSearch, String parrentShopId) throws LogicException, Exception {

        String query = "owner_type:" + Const.OWNER_TYPE.SHOP + " AND status:1 ";
        if (!DataUtil.isNullOrEmpty(txtSearch)) {
            query += " AND (owner_code:*{0}* OR owner_name:*{1}*)";
            query = MessageFormat.format(query, DataUtil.replaceSpaceSolr(txtSearch), DataUtil.replaceSpaceSolr(txtSearch));
        }
        query += (" AND (( parent_shop_id:7282 AND shop_type:2 AND NOT owner_id:(210451 OR"
                + "                     105501 OR"
                + "                     19404 OR"
                + "                     8136 OR"
                + "                     8138 OR"
                + "                     210149 OR"
                + "                     105540 OR"
                + "                     107794)) OR owner_id:7282)");
        List<VShopStaffDTO> lstShop = SolrController.doSearch(Const.SOLR_CORE.VSHOPSTAFF, query.toString(), VShopStaffDTO.class, Const.NUMBER_SEARCH.NUMBER_ALL, "owner_name", Const.SORT_ORDER.ASC);
        return lstShop;
    }

    public List<VShopStaffDTO> getListShopKV(String txtSearch) throws LogicException, Exception {

        String query = "owner_type:" + Const.OWNER_TYPE.SHOP + " AND status:1 AND owner_code:KV_* AND channel_type_id:1 ";
        if (!DataUtil.isNullOrEmpty(txtSearch)) {
            query += " AND (owner_code:*{0}* OR owner_name:*{1}*)";
            query = MessageFormat.format(query, DataUtil.replaceSpaceSolr(txtSearch), DataUtil.replaceSpaceSolr(txtSearch));
        }

        List<VShopStaffDTO> lstShop = SolrController.doSearch(Const.SOLR_CORE.VSHOPSTAFF, query.toString(), VShopStaffDTO.class, Const.NUMBER_SEARCH.NUMBER_ALL, "owner_name", Const.SORT_ORDER.ASC);
        return lstShop;
    }

    @Override
    public List<VShopStaffDTO> getListShopAndChildShop(String shopPath, String parentShopId) throws LogicException, Exception {
        String query = "owner_type:" + Const.OWNER_TYPE.SHOP;
        query += " AND owner_id: " + parentShopId;
        query += " AND status:" + Const.STATUS_ACTIVE;
        List<VShopStaffDTO> lstShop = SolrController.doSearch(Const.SOLR_CORE.VSHOPSTAFF, query, VShopStaffDTO.class, Const.NUMBER_SEARCH.NUMBER_ALL, "owner_name", Const.SORT_ORDER.ASC);
        String queryChild = "owner_type:" + Const.OWNER_TYPE.SHOP + " AND NOT shop_path:" + shopPath;
        queryChild += " AND parent_shop_id:" + parentShopId + " AND shop_type:2 AND channel_type_id:1";
        queryChild += " AND status:" + Const.STATUS_ACTIVE;

        List<VShopStaffDTO> lstShopChild = SolrController.doSearch(Const.SOLR_CORE.VSHOPSTAFF, queryChild, VShopStaffDTO.class, Const.NUMBER_SEARCH.NUMBER_ALL, "owner_name", Const.SORT_ORDER.ASC);
        lstShop.addAll(lstShopChild);
        // Neu dang nhap cap VT. Thi lay them chi nhanh.
        if (DataUtil.safeEqual(parentShopId, Const.SHOP.SHOP_PARENT_VTT_ID)) {
            String queryChildVTT = "owner_type:" + Const.OWNER_TYPE.SHOP + " AND NOT shop_path:" + shopPath;
            queryChildVTT += " AND parent_shop_id:" + Const.SHOP.SHOP_VTT_ID + " AND shop_type:2 AND channel_type_id:1";
            queryChildVTT += " AND status:" + Const.STATUS_ACTIVE;
            List<VShopStaffDTO> lstShopChildVTT = SolrController.doSearch(Const.SOLR_CORE.VSHOPSTAFF, queryChildVTT, VShopStaffDTO.class, Const.NUMBER_SEARCH.NUMBER_ALL, "owner_name", Const.SORT_ORDER.ASC);
            lstShop.addAll(lstShopChildVTT);
        }
        return lstShop;
    }

    @Override
    public VShopStaffDTO getShopByOwnerId(String ownerId, String ownerType) throws LogicException, Exception {
        String query = "owner_id:" + ownerId + " AND status:" + Const.STATUS.ACTIVE + " AND owner_type:" + ownerType;
        List<VShopStaffDTO> lstShop = SolrController.doSearch(Const.SOLR_CORE.VSHOPSTAFF, query, VShopStaffDTO.class, Const.NUMBER_SEARCH.NUMBER_ALL, "owner_name", Const.SORT_ORDER.ASC);
        return DataUtil.isNullOrEmpty(lstShop) ? null : lstShop.get(0);
    }

    @Override
    public ShopDTO getShopByCodeAndActiveStatus(String shopCode) throws Exception {
        List<FilterRequest> filters = Lists.newArrayList();
        filters.add(new FilterRequest(Shop.COLUMNS.SHOPCODE.name(), FilterRequest.Operator.EQ, shopCode, false));
        filters.add(new FilterRequest(Shop.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, Const.STATUS_ACTIVE));
        List<ShopDTO> result = findByFilter(filters);
        return DataUtil.isNullOrEmpty(result) ? null : result.get(0);
    }

    @Override
    public List<ShopDTO> getListShopByCodeAndActiveStatus(List<String> shopCodes) throws Exception {
        List<String> lsShopUper = shopCodes.stream().map(x -> Strings.toUpperCase(x.trim())).collect(Collectors.toList());
        return repository.getListShopByCodeAndActiveStatus(lsShopUper);
    }

    @Override
    public ShopDTO getShopByShopCode(String shopCode) throws Exception {
        List<FilterRequest> filters = Lists.newArrayList();
        filters.add(new FilterRequest(Shop.COLUMNS.SHOPCODE.name(), FilterRequest.Operator.EQ, shopCode));
        List<ShopDTO> result = findByFilter(filters);
        return DataUtil.isNullOrEmpty(result) ? null : result.get(0);
    }

    @Override
    public List<VShopStaffDTO> getListShopStaff(Long ownerId, String ownerType, String vtUnit) throws Exception, LogicException {
        return repository.getListShopStaff(ownerId, ownerType, vtUnit);
    }

    @Override
    public boolean isCenterOrBranch(Long shopId) throws LogicException, Exception {
        return repository.isCenterOrBranch(shopId);
    }

    @Override
    public List<ShopDTO> getListShopByObject(ShopDTO shopDTO) throws Exception {
        List<FilterRequest> lstRq = Lists.newArrayList();
        if (!DataUtil.isNullObject(shopDTO.getParentShopId())) {
            FilterRequest rq1 = new FilterRequest(Shop.COLUMNS.PARENTSHOPID.name(), FilterRequest.Operator.EQ, shopDTO.getParentShopId());
            lstRq.add(rq1);
        }

        if (!DataUtil.isNullObject(shopDTO.getChannelTypeId())) {
            FilterRequest rq2 = new FilterRequest(Shop.COLUMNS.CHANNELTYPEID.name(), FilterRequest.Operator.EQ, shopDTO.getChannelTypeId());
            lstRq.add(rq2);
        }
        lstRq.add(new FilterRequest(Shop.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, Const.STATUS.ACTIVE));
        return findByFilter(lstRq);
    }

    @Override
    public List<ShopDTO> getListShopByCodeOption(String code) throws Exception {
        return repository.getListShopByCodeOption(code);
    }

    @Override
    public List<VShopStaffDTO> getListAllShop() throws LogicException, Exception {
        String query = "owner_type:" + Const.OWNER_TYPE.SHOP + " AND status:" + Const.STATUS_ACTIVE + "*";
        List<VShopStaffDTO> lstShop = SolrController.doSearch(Const.SOLR_CORE.VSHOPSTAFF, query, VShopStaffDTO.class, Const.NUMBER_SEARCH.NUMBER_ALL, "owner_name", Const.SORT_ORDER.ASC);
        System.out.print(lstShop.size());
        return lstShop;
    }

    private VShopStaffDTO findOneVShopDTO(Long shopId, String ownerType) {
        StringBuilder queBuilder = new StringBuilder("owner_type:").append(ownerType);
        queBuilder.append(" AND status:").append(Const.STATUS_ACTIVE);
        queBuilder.append(" AND owner_id:").append(shopId);
        List<VShopStaffDTO> lstShop = SolrController.doSearch(Const.SOLR_CORE.VSHOPSTAFF, queBuilder.toString(), VShopStaffDTO.class, Const.NUMBER_SEARCH.NUMBER_ALL, "owner_name", Const.SORT_ORDER.ASC);
        if (DataUtil.isNullOrEmpty(lstShop)) {
            return null;
        } else {
            return lstShop.get(0);
        }
    }

    @Override
    public List<VShopStaffDTO> getListShopAndAllChildShop(String txtSearch, Long shopId, boolean isDismissType, boolean currentShop, List<Long> channelTypeIDs, String ownerType) throws Exception {
        StringBuilder builder = new StringBuilder("owner_type:").append(ownerType).append(" AND status:").append(Const.STATUS_ACTIVE);
        if (!isDismissType) {
            builder.append(" AND shop_type:2 AND NOT owner_id:(210451 OR"
                    + "                     105501 OR"
                    + "                     19404 OR"
                    + "                     8136 OR"
                    + "                     8138 OR"
                    + "                     210149 OR"
                    + "                     105540 OR"
                    + "                     107794) AND parent_shop_id:(7282 7281) ");
        }
        if (!DataUtil.isNullOrEmpty(channelTypeIDs)) {
            builder.append(" AND (");
            for (Long localChannelType : channelTypeIDs) {
                builder.append(" channel_type_id:").append(localChannelType).append(" OR");
            }
            builder.delete(builder.length() - 3, builder.length());
            builder.append(")");
        }
        if (!DataUtil.isNullObject(shopId)) {
            VShopStaffDTO shopDTO = findOneVShopDTO(shopId, ownerType);
            if (DataUtil.isNullObject(shopDTO)) {
                return Lists.newArrayList();
            }
            if (currentShop) {
                builder.append(" AND (owner_id:").append(shopId);
                builder.append(" OR shop_path:").append(shopDTO.getShopPath()).append("_*").append(")");
            } else {
                builder.append("AND (NOT owner_id:").append(shopId);
                builder.append(" AND shop_path:").append(shopDTO.getShopPath()).append("_*").append(")");
            }
        }


        if (!DataUtil.isNullOrEmpty(txtSearch)) {
            builder.append(" AND (owner_code:*{0}* OR owner_name:*{1}*)");
            builder = new StringBuilder(MessageFormat.format(builder.toString(), DataUtil.replaceSpaceSolr(txtSearch), DataUtil.replaceSpaceSolr(txtSearch)));
        }
        List<VShopStaffDTO> lstShop = SolrController.doSearch(Const.SOLR_CORE.VSHOPSTAFF, builder.toString(), VShopStaffDTO.class, Const.NUMBER_SEARCH.NUMBER_ALL, "owner_name", Const.SORT_ORDER.ASC);
        return lstShop;
    }

    @Override
    public List<VShopStaffDTO> getListShopHierachy(String shopId, String channelTypeId, String txtSearch) throws Exception {
        if (DataUtil.isNullOrEmpty(shopId)) {
            throw new LogicException("", "");
        }

        StringBuilder builder = new StringBuilder(" status:1 AND owner_type:").append(Const.OWNER_TYPE.SHOP);
        if (!DataUtil.isNullOrEmpty(channelTypeId)) {
            builder.append(" AND (channel_type_id:" + channelTypeId + " OR owner_id:" + shopId + " OR shop_path:*_" + shopId + "_* )");
//            builder = new StringBuilder(MessageFormat.format(builder.toString(), DataUtil.replaceSpaceSolr(txtSearch), DataUtil.replaceSpaceSolr(txtSearch)));
        } else {
            builder.append(" AND (owner_id:" + shopId + " OR shop_path:*_" + shopId + "_* )");
        }
        if (!DataUtil.isNullOrEmpty(txtSearch)) {
            builder.append(" AND (owner_code:*{0}* OR owner_name:*{1}*)");
            builder = new StringBuilder(MessageFormat.format(builder.toString(), DataUtil.replaceSpaceSolr(txtSearch), DataUtil.replaceSpaceSolr(txtSearch)));
        }

        List<VShopStaffDTO> lstShop = SolrController.doSearch(Const.SOLR_CORE.VSHOPSTAFF, builder.toString(), VShopStaffDTO.class, Const.NUMBER_SEARCH.NUMBER_ALL, "owner_name", Const.SORT_ORDER.ASC);
        return lstShop;
    }

    @Override
    public List<VShopStaffDTO> getListNotChanelTypeId(String shopId, String channelTypeId, String txtSearch) throws Exception {
        if (DataUtil.isNullOrEmpty(shopId)) {
            throw new LogicException("", "");
        }

        StringBuilder builder = new StringBuilder(" status:1 AND owner_type:").append(Const.OWNER_TYPE.SHOP);
        if (!DataUtil.isNullOrEmpty(channelTypeId)) {
            builder.append(" AND -channel_type_id:" + channelTypeId + " AND ( owner_id:" + shopId + " OR shop_path:*_" + shopId + "_* )");
        } else {
            builder.append(" AND (owner_id:" + shopId + " OR shop_path:*_" + shopId + "_* )");
        }
        if (!DataUtil.isNullOrEmpty(txtSearch)) {
            builder.append(" AND (owner_code:*{0}* OR owner_name:*{1}*)");
            builder = new StringBuilder(MessageFormat.format(builder.toString(), DataUtil.replaceSpaceSolr(txtSearch), DataUtil.replaceSpaceSolr(txtSearch)));
        }

        List<VShopStaffDTO> lstShop = SolrController.doSearch(Const.SOLR_CORE.VSHOPSTAFF, builder.toString(), VShopStaffDTO.class, Const.NUMBER_SEARCH.NUMBER_ALL, "owner_name", Const.SORT_ORDER.ASC);
        return lstShop;
    }

    @Override
    public List<VShopStaffDTO> getAllShop(String txtSearch, String channelTypeId) throws Exception {
        StringBuilder sql = new StringBuilder(" status:1 AND owner_type:").append(Const.OWNER_TYPE.SHOP);
        if (!DataUtil.isNullOrEmpty(txtSearch)) {
            sql.append(" AND (owner_code:*{0}* OR owner_name:*{1}*)");
            sql = new StringBuilder(MessageFormat.format(sql.toString(), DataUtil.replaceSpaceSolr(txtSearch), DataUtil.replaceSpaceSolr(txtSearch)));
        }
        List<VShopStaffDTO> lstShop = SolrController.doSearch(Const.SOLR_CORE.VSHOPSTAFF, sql.toString(), VShopStaffDTO.class, Const.NUMBER_SEARCH.NUMBER_ALL, "owner_name", Const.SORT_ORDER.ASC);
        return lstShop;
    }


    @WebMethod
    public Long checkParentShopIsBranch(Long shopId) throws Exception {
        return repository.checkParentShopIsBranch(shopId);
    }

    @Override
    public List<VShopStaffDTO> getListAgent(String txtSearch, Long parentId) throws Exception {
        StringBuilder builder = new StringBuilder("owner_type:").append(Const.OWNER_TYPE.SHOP).append(" AND status:").append(Const.STATUS_ACTIVE);
        builder.append(" AND parent_shop_id:").append(parentId);
        List<ChannelTypeDTO> channelTypes = repository.getListChannelTypeParam(null, Const.CHANNEL_TYPE.OBJECT_TYPE_SHOP, Const.VT_UNIT.AGENT);
        if (!DataUtil.isNullOrEmpty(channelTypes)) {
            builder.append(" AND (");
            for (ChannelTypeDTO channelType : channelTypes) {
                builder.append(" channel_type_id:").append(channelType.getChannelTypeId()).append(" OR");
            }
            builder.delete(builder.length() - 3, builder.length());
            builder.append(")");
        }

        if (!DataUtil.isNullOrEmpty(txtSearch)) {
            builder.append(" AND (owner_code:*{0}* OR owner_name:*{1}*)");
            builder = new StringBuilder(MessageFormat.format(builder.toString(), DataUtil.replaceSpaceSolr(txtSearch), DataUtil.replaceSpaceSolr(txtSearch)));
        }
        List<VShopStaffDTO> lstShop = SolrController.doSearch(Const.SOLR_CORE.VSHOPSTAFF, builder.toString(), VShopStaffDTO.class, Const.NUMBER_SEARCH.NUMBER_ALL, "owner_name", Const.SORT_ORDER.ASC);
        return lstShop;
    }

    @Override
    public List<VShopStaffDTO> getListCollaboratorAndPointOfSale(String txtSearch, Long shopId, List<Long> lstChannelType, Long staffOwnerId) throws Exception {
        StringBuilder builder = new StringBuilder("owner_type:").append(Const.OWNER_TYPE.STAFF).append(" AND status:").append(Const.STATUS_ACTIVE);
        builder.append(" AND parent_shop_id:").append(shopId);
        if (!DataUtil.isNullOrEmpty(lstChannelType)) {
            builder.append(" AND (");
            for (Long localChannelType : lstChannelType) {
                builder.append(" channel_type_id:").append(localChannelType).append(" OR");
            }
            builder.delete(builder.length() - 3, builder.length());
            builder.append(")");
        }
        if (!DataUtil.isNullObject(staffOwnerId)) {
            builder.append(" AND staff_owner_id:").append(staffOwnerId);
        }

        if (!DataUtil.isNullOrEmpty(txtSearch)) {
            builder.append(" AND (owner_code:*{0}* OR owner_name:*{1}*)");
            builder = new StringBuilder(MessageFormat.format(builder.toString(), DataUtil.replaceSpaceSolr(txtSearch), DataUtil.replaceSpaceSolr(txtSearch)));
        }
        List<VShopStaffDTO> lstShop = SolrController.doSearch(Const.SOLR_CORE.VSHOPSTAFF, builder.toString(), VShopStaffDTO.class, Const.NUMBER_SEARCH.NUMBER_ALL, "owner_name", Const.SORT_ORDER.ASC);
        return lstShop;
    }

    @Override
    public boolean checkChannelIsAgent(Long channelTypeId) throws Exception {
        List<ChannelTypeDTO> channelTypes = repository.getListChannelTypeParam(channelTypeId, Const.CHANNEL_TYPE.OBJECT_TYPE_SHOP, Const.VT_UNIT.AGENT);
        if (!DataUtil.isNullOrEmpty(channelTypes)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean checkChannelIsCollAndPointOfSale(Long channelTypeId) throws Exception {
        List<ChannelTypeDTO> channelTypes = repository.getListChannelTypeParam(channelTypeId, Const.CHANNEL_TYPE.OBJECT_TYPE_STAFF, Const.VT_UNIT.AGENT);
        if (!DataUtil.isNullOrEmpty(channelTypes)) {
            return true;
        }
        return false;
    }

    @Override
    public List<ChannelTypeDTO> getChannelTypeCollaborator() throws Exception {
        return repository.getListChannelTypeParam(null, Const.CHANNEL_TYPE.OBJECT_TYPE_STAFF, Const.VT_UNIT.AGENT);
    }

    @Override
    public VShopStaffDTO getParentShopByShopId(Long shopId) throws LogicException, Exception {
        StringBuilder builder = new StringBuilder("owner_type:")
                .append(Const.OWNER_TYPE.SHOP)
                .append(" AND owner_id:{0}")
                .append(" AND status:").append(Const.STATUS_ACTIVE);

        String thisQuery = MessageFormat.format(builder.toString(), DataUtil.safeToString(shopId));
        List<VShopStaffDTO> lstShop = SolrController.doSearch(Const.SOLR_CORE.VSHOPSTAFF, thisQuery, VShopStaffDTO.class, Const.NUMBER_SEARCH.NUMBER_ALL, "owner_name", Const.SORT_ORDER.ASC);
        VShopStaffDTO owner = DataUtil.isNullOrEmpty(lstShop) ? null : lstShop.get(0);
        if (owner == null || DataUtil.isNullOrEmpty(owner.getParentShopId())) {
            return null;
        }

        String pQuery = MessageFormat.format(builder.toString(), DataUtil.replaceSpaceSolr(owner.getParentShopId()));
        List<VShopStaffDTO> lstParent = SolrController.doSearch(Const.SOLR_CORE.VSHOPSTAFF, pQuery, VShopStaffDTO.class, Const.NUMBER_SEARCH.NUMBER_ALL, "owner_name", Const.SORT_ORDER.ASC);

        return DataUtil.isNullOrEmpty(lstParent) ? null : lstParent.get(0);
    }

    @Override
    public List<VShopStaffDTO> getListShopForIsdn(String input, String parentShopId, boolean isCurrentAndChildShop, List<String> lstChanelType) {
        StringBuilder builder = new StringBuilder("owner_type:").append(Const.OWNER_TYPE.SHOP).append(" AND status:").append(Const.STATUS_ACTIVE);

        if (!DataUtil.isNullOrEmpty(lstChanelType)) {
            builder.append(" AND (");
            for (String channelType : lstChanelType) {
                builder.append(" channel_type_id:").append(channelType).append(" OR");
            }
            builder.delete(builder.length() - 3, builder.length());
            builder.append(")");
        }

        if (!DataUtil.isNullObject(parentShopId)) {
            VShopStaffDTO shopDTO = findOneVShopDTO(DataUtil.safeToLong(parentShopId), Const.OWNER_TYPE.SHOP);
            if (DataUtil.isNullObject(shopDTO)) {
                return Lists.newArrayList();
            }
            if (isCurrentAndChildShop) {
                builder.append(" AND (owner_id:").append(parentShopId);
                builder.append(" OR shop_path:").append(shopDTO.getShopPath()).append("_*").append(")");
            } else {
                builder.append("AND (NOT owner_id:").append(parentShopId);
                builder.append(" AND shop_path:").append(shopDTO.getShopPath()).append("_*").append(")");
            }
        }


        if (!DataUtil.isNullOrEmpty(input)) {
            builder.append(" AND (owner_code:*{0}* OR owner_name:*{1}*)");
            builder = new StringBuilder(MessageFormat.format(builder.toString(), DataUtil.replaceSpaceSolr(input), DataUtil.replaceSpaceSolr(input)));
        }
        List<VShopStaffDTO> lstShop = SolrController.doSearch(Const.SOLR_CORE.VSHOPSTAFF, builder.toString(), VShopStaffDTO.class, Const.NUMBER_SEARCH.NUMBER_ALL, "owner_name", Const.SORT_ORDER.ASC);
        return lstShop;
    }

    @Override
    public Long getBranchId(Long shopId) throws LogicException, Exception {
        if (DataUtil.isNullOrZero(shopId)) {
            return null;
        }
        ShopDTO shopDTO = findOne(shopId);
        if (DataUtil.isNullObject(shopDTO)) {
            return null;
        }
        String shopPath = shopDTO.getShopPath();
        if (DataUtil.isNullObject(shopPath)) {
            return null;
        }
        String[] arrShopPath = shopPath.split("_");
        if (DataUtil.isNullObject(arrShopPath) || arrShopPath.length < 4) {
            return null;
        }
        ShopDTO shopBranch = findOne(DataUtil.safeToLong(arrShopPath[3]));
        if (!DataUtil.isNullObject(shopBranch) && DataUtil.safeEqual(shopBranch.getShopType(), Const.MODE_SHOP.SHOP_TYPE_BRANCH)) {
            return shopBranch.getShopId();
        }
        return null;
    }

    public Long findBranchId(Long shopId) throws LogicException, Exception {
        if (DataUtil.isNullOrZero(shopId)) {
            return null;
        }

        if (DataUtil.safeEqual(shopId, Const.L_VT_SHOP_ID)) {
            return shopId;
        }

        ShopDTO shopDTO = findOne(shopId);
        if (DataUtil.isNullObject(shopDTO)) {
            return null;
        }

        //Neu la dai ly thuoc VT --> lay chi nhanh la VT
        if (DataUtil.safeEqual(shopDTO.getParentShopId(), Const.L_VT_SHOP_ID) && checkChannelIsAgent(shopDTO.getChannelTypeId())) {
            return Const.L_VT_SHOP_ID;
        }

        String shopPath = shopDTO.getShopPath();
        if (DataUtil.isNullObject(shopPath)) {
            return null;
        }
        String[] arrShopPath = shopPath.split("_");
        if (DataUtil.isNullObject(arrShopPath) || arrShopPath.length < 4) {
            return null;
        }
        ShopDTO shopBranch = findOne(DataUtil.safeToLong(arrShopPath[3]));
        if (!DataUtil.isNullObject(shopBranch) && DataUtil.safeEqual(shopBranch.getShopType(), Const.MODE_SHOP.SHOP_TYPE_BRANCH)) {
            return shopBranch.getShopId();
        }
        return null;
    }

    @Override
    public VShopStaffDTO getShopByCodeForDistribute(String shopCode, Long parentShopId, String ownerType) throws LogicException, Exception {
        return repository.getShopByCodeForDistribute(shopCode, parentShopId, ownerType);
    }

    @Override
    public List<ShopDTO> getListShopByStaffShopId(Long shopId, String shopCode) throws LogicException, Exception {
        return repository.getListShopByStaffShopId(shopId, shopCode);
    }

    @Override
    public List<VShopStaffDTO> getListShopForPay(String input, String parentShopId) {
        StringBuilder builder = new StringBuilder("owner_type:").append(Const.OWNER_TYPE.SHOP).append(" AND status:").append(Const.STATUS_ACTIVE);

        if (!DataUtil.isNullObject(parentShopId)) {
            builder.append(" AND (owner_id:").append(parentShopId);
            builder.append(" OR shop_path:*_").append(parentShopId).append("_*").append(")");
        }


        if (!DataUtil.isNullOrEmpty(input)) {
            builder.append(" AND (owner_code:*{0}* OR owner_name:*{1}*)");
            builder = new StringBuilder(MessageFormat.format(builder.toString(), DataUtil.replaceSpaceSolr(input), DataUtil.replaceSpaceSolr(input)));
        }
        List<VShopStaffDTO> lstShop = SolrController.doSearch(Const.SOLR_CORE.VSHOPSTAFF, builder.toString(), VShopStaffDTO.class, Const.NUMBER_SEARCH.NUMBER_ALL, "owner_name", Const.SORT_ORDER.ASC);
        return lstShop;
    }

    @Override
    public VShopStaffDTO getShopByShopId(Long shopId) throws LogicException, Exception {
        StringBuilder builder = new StringBuilder("owner_type:")
                .append(Const.OWNER_TYPE.SHOP)
                .append(" AND owner_id:").append(shopId)
                .append(" AND status:").append(Const.STATUS_ACTIVE);


        List<VShopStaffDTO> lstShop = SolrController.doSearch(Const.SOLR_CORE.VSHOPSTAFF, builder.toString(), VShopStaffDTO.class, Const.NUMBER_SEARCH.NUMBER_ALL, "owner_name", Const.SORT_ORDER.ASC);
        return DataUtil.isNullOrEmpty(lstShop) ? null : lstShop.get(0);
    }

    @Override
    public List<Long> getListChannelType() throws Exception {
        return repository.getListChannelType(null);
    }

    @Override
    public ChannelTypeDTO getChannelTypeById(Long channelTypeId) throws LogicException, Exception {
        List<ChannelTypeDTO> channelTypeDTOs = repository.getListChannelTypeParam(channelTypeId, null, null);
        if (!DataUtil.isNullOrEmpty(channelTypeDTOs)) {
            return channelTypeDTOs.get(0);
        }
        return null;
    }

    @Override
    public List<VShopStaffDTO> getListShopByListShopCode(String input, String shopPathUserLogin, List<String> lsShopCodes) {
        StringBuilder builder = new StringBuilder(" owner_type:").append(Const.OWNER_TYPE.SHOP).append(" AND status:").append(Const.STATUS_ACTIVE);
        if (!DataUtil.isNullOrEmpty(lsShopCodes)) {
            builder.append(" AND owner_code : (");
            builder.append(Joiner.on(" OR ").skipNulls().join(lsShopCodes));
            builder.append(")");
        }
        if (!DataUtil.isNullOrEmpty(shopPathUserLogin)) {
            builder.append(" AND shop_path:").append(shopPathUserLogin).append("* ");
        }
        if (!DataUtil.isNullOrEmpty(input)) {
            builder.append(" AND (owner_code:*{0}* OR owner_name:*{1}*)");
            builder = new StringBuilder(MessageFormat.format(builder.toString(), DataUtil.replaceSpaceSolr(input), DataUtil.replaceSpaceSolr(input)));
        }
        List<VShopStaffDTO> lstShop = SolrController.doSearch(Const.SOLR_CORE.VSHOPSTAFF, builder.toString(), VShopStaffDTO.class, Const.NUMBER_SEARCH.NUMBER_ALL, "owner_name", Const.SORT_ORDER.ASC);
        return lstShop;
    }

    public boolean checkExsitShopStaff(Long ownerId, Long ownerType) throws Exception {
        return repository.checkExsitShopStaff(ownerId, ownerType);
    }

    public MvShopStaffDTO getMViewShopStaff(Long ownerType, Long ownerId) throws Exception {
        return repository.getMViewShopStaff(ownerType, ownerId);
    }

    public ShopDTO getShopByShopKeeper(Long staffId) throws Exception {
        return repository.getShopByShopKeeper(staffId);
    }

    public boolean checkShopIsBranch(Long shopId) throws Exception {
        return repository.checkShopIsBranch(shopId);
    }

}
