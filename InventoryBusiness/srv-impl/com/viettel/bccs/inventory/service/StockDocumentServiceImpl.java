package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.StockDocumentDTO;
import com.viettel.bccs.inventory.model.StockDocument;
import com.viettel.bccs.inventory.repo.StockDocumentRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
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
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class StockDocumentServiceImpl extends BaseServiceImpl implements StockDocumentService {

    private final BaseMapper<StockDocument, StockDocumentDTO> mapper = new BaseMapper<>(StockDocument.class, StockDocumentDTO.class);

    @Autowired
    private StockDocumentRepo repository;
    public static final Logger logger = Logger.getLogger(StockDocumentService.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public StockDocumentDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<StockDocumentDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<StockDocumentDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(StockDocumentDTO dto) throws Exception, LogicException {
        BaseMessage result = new BaseMessage(true);
        try {
            StockDocument stockDocument = mapper.toPersistenceBean(dto);
            repository.save(stockDocument);
            result.setSuccess(true);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            result.setSuccess(false);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "payNoteAndReport.errorCreate.stockDocument");
        }
        return result;
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(StockDocumentDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    public List<StockDocumentDTO> getListStockDocumentDTOByActionId(Long actionId) throws Exception {

        List lstParam = new ArrayList();
        StringBuilder strQuery = new StringBuilder("");
        strQuery.append("SELECT   stock_document_id, action_id, action_code, ")
                .append(" user_create, create_date, from_shop_code, to_shop_code, ")
                .append(" export_note_name, export_note, delivery_records_name, delivery_records ")
                .append(" FROM   stock_document where  ")
                .append(" (status is null or status <> 4)  ");
        if (!DataUtil.isNullObject(actionId)) {
            strQuery.append(" and action_id =  ? ");
            lstParam.add(actionId);
        }
        List<StockDocumentDTO> result = Lists.newArrayList();
        Query query = em.createNativeQuery(strQuery.toString());
        if (query != null) {
            for (int i = 0; i < lstParam.size(); i++) {
                query.setParameter(i + 1, lstParam.get(i));
            }

            List queryResult = query.getResultList();
            for (Object obj : queryResult) {
                Object[] objects = (Object[]) obj;
                StockDocumentDTO stockDocumentDTO = new StockDocumentDTO();
                stockDocumentDTO.setStockDocumentId(DataUtil.safeToLong(objects[0]));
                stockDocumentDTO.setActionId(DataUtil.safeToLong(objects[1]));
                stockDocumentDTO.setActionCode(DataUtil.safeToString(objects[2]));
                stockDocumentDTO.setUserCreate(DataUtil.safeToString(objects[3]));
                stockDocumentDTO.setCreateDate((Date) objects[4]);
                stockDocumentDTO.setFromShopCode(DataUtil.safeToString(objects[5]));
                stockDocumentDTO.setToShopCode(DataUtil.safeToString(objects[6]));
                stockDocumentDTO.setExportNoteName(DataUtil.safeToString(objects[7]));
                stockDocumentDTO.setExportNote(getFileContent(objects[8]));
                stockDocumentDTO.setDeliveryRecordsName(DataUtil.safeToString(objects[9]));
                stockDocumentDTO.setDeliveryRecords(getFileContent(objects[10]));
                result.add(stockDocumentDTO);
            }
        }
        return result;
    }

    private byte[] getFileContent(Object object) throws Exception {
        if (!DataUtil.isNullObject(object)) {
            return (byte[]) object;
        }
        return new byte[]{};
    }

}
