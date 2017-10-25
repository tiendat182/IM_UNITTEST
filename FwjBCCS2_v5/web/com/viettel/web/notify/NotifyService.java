package com.viettel.web.notify;

/**
 * Created by vtsoft on 12/10/2014.
 */

import com.viettel.fw.SystemConfig;
import com.viettel.fw.common.util.Const;
import com.viettel.solrj.SolrDao;
import com.viettel.solrj.SolrServerFactory;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.codehaus.jackson.map.ObjectMapper;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class NotifyService {
    public static final Logger logger = Logger.getLogger(NotifyService.class);

    @Autowired
    SystemConfig systemConfig;
    @Autowired
    ObjectMapper objectMapper;

    public void makePush(String to, String subject, String content) {
        if (to != null) {
            NotifyContent notifyContent = new NotifyContent();
            notifyContent.setFrom("System");
            notifyContent.setTo(to);
            notifyContent.setSubject(subject);
            notifyContent.setText(content);
            notifyContent.setCreatedDate(new Date());
            notifyContent.setStatus(1);
            notifyContent.setId(RandomStringUtils.randomNumeric(30));
            makePush(notifyContent);
        }
    }

    public void deleteNotify(String id) {
        SolrDao solrDao = new SolrDao(systemConfig.NOTIFY_LINK, systemConfig.NOTIFY_USERNAME, systemConfig.NOTIFY_PASSWORD);
        solrDao.deleteById(id);
    }


    private void makePush(NotifyContent content) {
        EventBus eventBus = EventBusFactory.getDefault().eventBus();
        if (eventBus != null) {
            try {
                eventBus.publish(Const.NOTIFY.CHANNEL, objectMapper.writeValueAsBytes(content));
            } catch (IOException e) {
                logger.error(e);
            }
            saveNotifies(content);
        }
    }

    public Object[] initGetUnreadNotifies(String toUser, Integer limit) throws IOException, SolrServerException {
        QueryResponse queryResponse = queryNotify(toUser, limit, Const.NOTIFY.STATE_UNREAD);
        return new Object[]{queryResponse.getResults().getNumFound(), queryResponse.getBeans(NotifyContent.class)};
    }


    public List<NotifyContent> getUnreadNotifies(String toUser, Integer limit) throws IOException, SolrServerException {
        QueryResponse queryResponse = queryNotify(toUser, limit, Const.NOTIFY.STATE_UNREAD);
        return queryResponse.getBeans(NotifyContent.class);
    }

    public List<NotifyContent> getNotifies(String toUser, Integer limit, int type) throws IOException, SolrServerException {
        QueryResponse queryResponse = queryNotify(toUser, limit, type);
        return queryResponse.getBeans(NotifyContent.class);
    }

    private QueryResponse queryNotify(String toUser, Integer limit, int type) throws IOException, SolrServerException {
        HttpSolrClient server = null;
        server = getHttpSolrClient();
        SolrQuery solrQuery = new SolrQuery();
        String query = "to:" + toUser + " AND status:" + type;
        solrQuery.setSort("date_create", SolrQuery.ORDER.desc);
        if (limit != null) {
            solrQuery.setRows(limit.intValue());
        }
        solrQuery.setQuery(query);
        return server.query(solrQuery);
    }

    //create notification
    private void saveNotifies(NotifyContent content) {
        SolrDao solrDao = new SolrDao(systemConfig.NOTIFY_LINK, systemConfig.NOTIFY_USERNAME, systemConfig.NOTIFY_PASSWORD);
        solrDao.put(content);
    }

    private HttpSolrClient getHttpSolrClient() {
        return (HttpSolrClient) SolrServerFactory.getInstance().createServer(systemConfig.NOTIFY_LINK, systemConfig.NOTIFY_USERNAME, systemConfig.NOTIFY_PASSWORD);
    }

}