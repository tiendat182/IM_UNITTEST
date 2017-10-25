package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.ReportChangeGoodsDTO;
import com.viettel.bccs.inventory.service.ReportChangeGoodsService;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
@Service("WsReportChangeGoodsServiceImpl")
public class WsReportChangeGoodsServiceImpl implements ReportChangeGoodsService {

    public static final Logger logger = Logger.getLogger(WsReportChangeGoodsServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private ReportChangeGoodsService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(ReportChangeGoodsService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @Override
    public ReportChangeGoodsDTO findOne(Long id) throws Exception  {
        return client.findOne(id);
    }

    @Override
    public List<ReportChangeGoodsDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<ReportChangeGoodsDTO> findByFilter(List<FilterRequest> filterList) throws Exception  {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public ReportChangeGoodsDTO create(ReportChangeGoodsDTO dto) throws Exception  {
        return client.create(dto);
    }

    @Override
    public BaseMessage update(ReportChangeGoodsDTO dto) throws Exception  {
        return client.update(dto);
    }

}