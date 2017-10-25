package com.viettel.solrj;

/**
 * Created by thiendn1 on 5/11/2015.
 */

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;


public class SolrDao {
    public static final Logger logger = Logger.getLogger("HttpClient");

    public static final String QUERY = "query";
    public static final String FILTER_QUERY = "fq";
    public static final String START = "start";
    public static final String LIMIT = "rows";
    public static final String FIELD_RESPONSES = "fl";
    public static final String TIME_ALLOWED = "timeAllowed";
    public static final String SORT = "sort";

    private static final List<String> commonParams = new ArrayList<>();

    static {
        commonParams.add(QUERY);
        commonParams.add(FILTER_QUERY);
        commonParams.add(START);
        commonParams.add(LIMIT);
        commonParams.add(FIELD_RESPONSES);
        commonParams.add(TIME_ALLOWED);
        commonParams.add(SORT);

    }

    HttpSolrClient server = null;
    private String username;
    private String password;
    private BasicCredentialsProvider credsProvider;


    public SolrDao(String solrURL, String username, String password) {
        this.username = username;
        this.password = password;
        server = (HttpSolrClient) SolrServerFactory.getInstance().createServer(solrURL, username, password);
        if (username != null && password != null) {
            credsProvider = new BasicCredentialsProvider();
            credsProvider.setCredentials(
                    AuthScope.ANY,
                    new UsernamePasswordCredentials(username, password));
        }
    }

    public JsonNode doFullSearch(List<FilterRequest> filters, Long limit) {
        try {

            SolrQuery solrQuery = new SolrQuery();
            solrQuery.setQuery(buildSolrQuery(filters));
            if (limit != null) {
                solrQuery.setRows(limit.intValue());
            }
            solrQuery.set("wt", "json");
            solrQuery.set("omitHeader", true);
            HttpClient httpclient = createSolrHttpClient();
            HttpGet httpget = new HttpGet(server.getBaseURL() + "/select?" + solrQuery);
            Date date = new Date();
            logger.info("start " + format.format(date) + ": " + server.getBaseURL() + "/select?" + solrQuery);
            HttpResponse response = httpclient.execute(httpget);
            String soapResponse = EntityUtils.toString(response.getEntity());
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(soapResponse);
            logger.info("end " + format.format(date) + " : " + (new Date().getTime() - date.getTime()) + ": " + server.getBaseURL() + "/select?" + solrQuery);

            return json;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * Tim kiem tren solr
     *
     * @param dtoClass class de hung doi tuong tra ve
     * @return
     */
    public <T> List<T> doSearch(List<FilterRequest> filters, Class<T> dtoClass, Long limit, String sortField, String sortOrder) {
        String query = null;
        try {
            query = buildSolrQuery(filters);
            return doSearch(query, dtoClass, limit, sortField, sortOrder, null);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return Lists.newArrayList();
    }


    /**
     * Tim kiem tren solr
     *
     * @param dtoClass class de hung doi tuong tra ve
     * @return
     */
    public <T> List<T> doSearch(String query, Class<T> dtoClass, Long limit, String sortField, String sortOrder, Long start) {
        return doSearch(query, null, null, dtoClass, limit, sortField, sortOrder, start);
    }

    public <T> List<T> doSearch(String query, String filterQuery, String[] resultSets, Class<T> dtoClass, Long limit, String sortField, String sortOrder, Long start) {
        try {

            JsonNode rsp = doSearch(query, filterQuery, resultSets, limit, sortField, sortOrder, start);
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(rsp.get("response").get("docs").toString(), mapper.getTypeFactory().constructCollectionType(List.class, dtoClass));

        } catch (SolrServerException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return Lists.newArrayList();
    }

    SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    public JsonNode doSearch(String query, String filterQuery, String[] resultSets, Long limit, String sortField, String sortOrder, Long start) throws IOException, SolrServerException {
        SolrQuery solrQuery = parseQuery(query, filterQuery, resultSets, limit, sortField, sortOrder, start);
        HttpClient httpclient = createSolrHttpClient();
        HttpGet httpget = new HttpGet(server.getBaseURL() + "/select?" + solrQuery);
        Date date = new Date();
        logger.info("start " + format.format(date) + ": " + server.getBaseURL() + "/select?" + solrQuery);
        HttpResponse response = httpclient.execute(httpget);
        String soapResponse = EntityUtils.toString(response.getEntity());
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(soapResponse);
        logger.info("end " + format.format(date) + " : " + (new Date().getTime() - date.getTime()) + ": " + server.getBaseURL() + "/select?" + solrQuery);

        return json;

    }

    public QueryResponse getResponse(String query, String filterQuery, String[] resultSets, Long limit, String sortField, String sortOrder) throws IOException, SolrServerException {
        return doSearch(parseQuery(query, filterQuery, resultSets, limit, sortField, sortOrder, null));

    }

    public QueryResponse getResponse(String query, boolean facet, String facetField, Long limit) throws IOException, SolrServerException {
        server.setConnectionTimeout(5000); // 5 seconds to establish TCP
        server.setSoTimeout(300000);  // socket read timeout
        return doSearch(parseQuery(query, facet, facetField, limit));

    }

    private SolrQuery parseQuery(String query, String filterQuery, String[] resultSets, Long limit, String sortField, String sortOrder, Long start) {
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery(query);
        solrQuery.setFilterQueries(filterQuery);
        if (resultSets != null) {
            StringBuilder returnResult = new StringBuilder();
            for (String result : resultSets) {
                //append char ','
                returnResult.append(result + ",");
            }
            returnResult.setLength(returnResult.length() - ",".length());
            solrQuery.setParam("fl", returnResult.toString());
        }
        if (!DataUtil.isNullOrEmpty(sortField) && sortOrder != null) {
            solrQuery.setSort(sortField, getSortOrder(sortOrder));
        }
        if (limit != null) {
            solrQuery.setRows(limit.intValue());
        }
        if (start != null) {
            solrQuery.setStart(start.intValue());
        }
        solrQuery.setParam("wt", "json");
        return solrQuery;
    }

    private SolrQuery parseQuery(String query, boolean facet, String facetField, Long limit) {
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery(query);
        solrQuery.setFacetLimit(1000000);
        if (facet) {
            solrQuery.setFacet(facet);
            solrQuery.addFacetField(facetField);
        }

        if (limit != null) {
            solrQuery.setRows(limit.intValue());
        }

        solrQuery.setParam("wt", "json");
        return solrQuery;
    }

    public QueryResponse doSearch(SolrQuery solrQuery) throws IOException, SolrServerException {
        return server.query(solrQuery);
    }


    private SolrQuery.ORDER getSortOrder(String sortOrder) {
        if (sortOrder.equals(SolrQuery.ORDER.desc)) {
            return SolrQuery.ORDER.desc;
        }
        return SolrQuery.ORDER.asc;
    }

    private String buildSolrQuery(List<FilterRequest> filters) throws UnsupportedEncodingException {
        List<String> query = Lists.newArrayList();
        for (FilterRequest filterRequest : filters) {
            FilterRequest.Operator operator = filterRequest.getOperator();
            if (operator == null) {
                query.add(filterRequest.getProperty() + ":*" + filterRequest.getValue() + "*");
            } else if (operator == FilterRequest.Operator.EQ) {
                query.add(filterRequest.getProperty() + ":\"" + filterRequest.getValue() + "\"");
            } else if (operator == FilterRequest.Operator.EXACT) {
                query.add(filterRequest.getProperty() + ":" + filterRequest.getValue());
            } else if (operator == FilterRequest.Operator.BETWEEN) {
                query.add(filterRequest.getProperty() + ":(" + filterRequest.getValue() + ")");
            } else if (operator == FilterRequest.Operator.RANGE) {
                query.add(filterRequest.getProperty() + ":[" + filterRequest.getValue() + "]");
            } else if (operator == FilterRequest.Operator.NE) {
                query.add("NOT " + filterRequest.getProperty() + ":" + filterRequest.getValue());
            } else if (operator == FilterRequest.Operator.LIKE_BEGIN) {
                query.add(filterRequest.getProperty() + ":*" + filterRequest.getValue());
            } else if (operator == FilterRequest.Operator.LIKE_END) {
                query.add(filterRequest.getProperty() + ":" + filterRequest.getValue() + "*");
            } else if (operator == FilterRequest.Operator.LIKE) {
                query.add(filterRequest.getProperty() + ":*" + filterRequest.getValue() + "*");
            } else if (operator == FilterRequest.Operator.IN) {
                query.add(filterRequest.getProperty() + ":(" + filterRequest.getValue() + ")");
            } else if (operator == FilterRequest.Operator.NOTIN) {
                query.add("NOT " + filterRequest.getProperty() + ":(" + filterRequest.getValue() + ")");
            } else {
                query.add(filterRequest.getProperty() + ":*" + filterRequest.getValue() + "*");
            }
        }
        String parameter = Joiner.on(" AND ").skipNulls().join(query);
        return parameter;
    }


    public void put(Collection dao) {
        try {
            server.addBeans(dao);
            server.commit();
        } catch (SolrServerException | IOException e) {
            logger.error(e);
        }
    }

    public void put(Object dao) {
        try {
            server.addBean(dao);
            server.commit();
        } catch (SolrServerException | IOException e) {
            logger.error(e);
        }
    }

    public void delete(List<String> ids) {
        try {
            UpdateResponse rsp = server.deleteById(ids);
            server.commit();
        } catch (SolrServerException | IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void deleteById(String id) {
        try {
            UpdateResponse rsp = server.deleteById(id);
            server.commit();
        } catch (SolrServerException | IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void putDoc(SolrInputDocument doc) {
        putDoc(createSingletonSet(doc));
    }

    public void putDoc(Collection<SolrInputDocument> docs) {
        try {
            long startTime = System.currentTimeMillis();
            UpdateRequest req = new UpdateRequest();
            req.setAction(UpdateRequest.ACTION.COMMIT, false, false);
            req.add(docs);
            UpdateResponse rsp = req.process(server);
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private <U> Collection<U> createSingletonSet(U dao) {
        if (dao == null)
            return Collections.emptySet();
        return Collections.singleton(dao);
    }

    private HttpClient createSolrHttpClient() {
        if (credsProvider == null) {
            return HttpClientBuilder.create().build();

        } else {
            return HttpClients.custom()
                    .setDefaultCredentialsProvider(credsProvider)
                    .build();
        }
    }

    private SolrQuery parseQuery(Map<String, Object> parameteres) throws Exception {
        String query = (String) parameteres.get(QUERY);
        if (query == null) {
            throw new Exception("Cau lenh truy van khong duoc phep de rong!");
        }
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery(query);
        //check filter query
        String[] filterQuery = (String[]) parameteres.get(FILTER_QUERY);
        if (filterQuery != null) {
            solrQuery.setFilterQueries(filterQuery);
        }

        // check start
        Integer start = (Integer) parameteres.get(START);
        if (start != null) {
            solrQuery.setStart(start);
        }
        // check rows
        Integer rows = (Integer) parameteres.get(LIMIT);
        if (rows != null) {
            solrQuery.setRows(rows);
        }

        String sort = (String) parameteres.get(SORT);
        if (sort != null) {
            solrQuery.setParam("sort", sort);
        }

        // check result sets
        String[] fieldList = (String[]) parameteres.get(FIELD_RESPONSES);
        if (fieldList != null) {
            solrQuery.setFields(fieldList);
        }

        //check timeAllowed
        Integer timeAllowed = (Integer) parameteres.get(TIME_ALLOWED);
        if (timeAllowed != null) {
            solrQuery.setTimeAllowed(timeAllowed);
        }
        solrQuery.setParam("wt", "json");

        //check other params
        for (Map.Entry<String, Object> entry : parameteres.entrySet()) {
            String key = entry.getKey();
            if (!commonParams.contains(key)) {
                String value = (String) entry.getValue();
                solrQuery.setParam(key, value);
            }
        }

        return solrQuery;
    }


    public <T> List<T> doSearch(Class<T> dtoClass, Map<String, Object> parameters) {
        try {
            SolrQuery solrQuery = parseQuery(parameters);
            HttpClient httpclient = createSolrHttpClient();
            HttpGet httpget = new HttpGet(server.getBaseURL() + "/select?" + solrQuery);
            Date date = new Date();
            logger.info("start " + format.format(date) + ": " + server.getBaseURL() + "/select?" + solrQuery);
            HttpResponse response = httpclient.execute(httpget);
            String soapResponse = EntityUtils.toString(response.getEntity());
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rsp = mapper.readTree(soapResponse);
            logger.info("end " + format.format(date) + " : " + (new Date().getTime() - date.getTime()) + ": " + server.getBaseURL() + "/select?" + solrQuery);
            mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(rsp.get("response").get("docs").toString(), mapper.getTypeFactory().constructCollectionType(List.class, dtoClass));
        } catch (SolrServerException e) {
            logger.equals(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return Lists.newArrayList();
    }


}
