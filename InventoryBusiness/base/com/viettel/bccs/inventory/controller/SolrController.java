package com.viettel.bccs.inventory.controller;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.fw.bean.ApplicationContextProvider;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.solrj.SolrDao;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.codehaus.jackson.JsonNode;
import org.springframework.web.context.support.XmlWebApplicationContext;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author nhannt34
 */
public final class SolrController {
    public static final Logger logger = Logger.getLogger(SolrController.class);

    private static ConcurrentHashMap<Const.SOLR_CORE, SolrDao> coreMapping = new ConcurrentHashMap<>();


    private static SolrDao getCore(Const.SOLR_CORE core) {
        String endpoint =
                ((XmlWebApplicationContext) ApplicationContextProvider.getApplicationContext()).getBeanFactory().resolveEmbeddedValue("${solr.url}");
        String username =
                ((XmlWebApplicationContext) ApplicationContextProvider.getApplicationContext()).getBeanFactory().resolveEmbeddedValue("${solr.username}");
        String password =
                ((XmlWebApplicationContext) ApplicationContextProvider.getApplicationContext()).getBeanFactory().resolveEmbeddedValue("${solr.password}");
        String httpUrl = endpoint + core.getSelectEndpoint();
        if (coreMapping.containsKey(core)) {
            return coreMapping.get(core);
        }
        coreMapping.put(core, new SolrDao(httpUrl, username, password));
        return coreMapping.get(core);
    }


    /**
     * Tim kiem tren solr dung filter request
     *
     * @param core     core tim kiem
     * @param filters  filter
     * @param dtoClass class de hung doi tuong tra ve
     * @return
     */
    public static <T> List<T> doSearch(@Nonnull Const.SOLR_CORE core, @Nonnull List<FilterRequest> filters, @Nonnull Class<T> dtoClass) {
        return getCore(core).doSearch(filters, dtoClass, null, null, null);

    }

    public static <T> List<T> doSearch(@Nonnull Const.SOLR_CORE core, @Nonnull List<FilterRequest> filters, @Nonnull Class<T> dtoClass, Long limit, String sortField, String sortOrder) {
        return getCore(core).doSearch(filters, dtoClass, limit, sortField, sortOrder);

    }

    public static JsonNode doSearch(@Nonnull Const.SOLR_CORE core, @Nonnull List<FilterRequest> filters) {
        return getCore(core).doFullSearch(filters, null);

    }

    /**
     * Tim kiem tren solr dung query
     *
     * @param core     core tim kiem
     * @param dtoClass class de hung doi tuong tra ve
     * @return
     */
    public static <T> List<T> doSearch(@Nonnull Const.SOLR_CORE core, @Nonnull String query, @Nonnull Class<T> dtoClass) {
        return doSearch(core, query, dtoClass, null, null, null);

    }

    /**
     * Tim kiem tren solr dung query
     *
     * @param core     core tim kiem
     * @param dtoClass class de hung doi tuong tra ve
     * @return
     */
    public static <T> List<T> doSearch(@Nonnull Const.SOLR_CORE core, @Nonnull String query, String filterQuery, @Nonnull Class<T> dtoClass) {
        return doSearch(core, query, filterQuery, dtoClass, null, null, null);

    }

    /**
     * Tim kiem tren solr dung query
     *
     * @param core     core tim kiem
     * @param dtoClass class de hung doi tuong tra ve
     * @return
     */
    public static <T> List<T> doSearch(@Nonnull Const.SOLR_CORE core, @Nonnull String query, @Nonnull Class<T> dtoClass, Long limit, String sortField, String sortOrder) {
        return getCore(core).doSearch(query, dtoClass, limit, sortField, sortOrder, null);

    }

    public static <T> List<T> doSearch(@Nonnull Const.SOLR_CORE core, @Nonnull String query, String filterQuery, @Nonnull Class<T> dtoClass, Long limit, String sortField, String sortOrder) {
        return getCore(core).doSearch(query, filterQuery, null, dtoClass, limit, sortField, sortOrder, null);

    }

    public static <T> List<T> doSearch(@Nonnull Const.SOLR_CORE core, @Nonnull String query, String filterQuery, String[] resultSets, @Nonnull Class<T> dtoClass, Long limit, String sortField, String sortOrder) {
        return getCore(core).doSearch(query, filterQuery, resultSets, dtoClass, limit, sortField, sortOrder, null);

    }

    public static QueryResponse getResponse(@Nonnull Const.SOLR_CORE core, String query, String filterQuery, String[] resultSets, Long limit, String sortField, String sortOrder) throws IOException, SolrServerException {
        return getCore(core).getResponse(query, filterQuery, resultSets, limit, sortField, sortOrder);

    }

    public static QueryResponse getResponse(@Nonnull Const.SOLR_CORE core, String query, boolean facet, String facetField, Long limit) throws IOException, SolrServerException {
        return getCore(core).getResponse(query, facet, facetField, limit);

    }

    //hung doi tuong tra ve paginator
    public static <T> List<T> doSearch(@Nonnull Const.SOLR_CORE core, @Nonnull String query, @Nonnull Class<T> dtoClass, Long limit, String sortField, String sortOrder, Long start) {
        return getCore(core).doSearch(query, dtoClass, limit, sortField, sortOrder, start);

    }

}
