package com.viettel.fw.dto;

import java.util.Map;

/**
 * @author nhannt34
 * @since 04/02/2016
 */
public class QueryParam {
    private final String query;
    private final Map<String, Object> params;

    public QueryParam(String query, Map<String, Object> params) {
        this.query = query;
        this.params = params;
    }
    public String getQuery() {
        return query;
    }

    public Map<String, Object> getParams() {
        return params;
    }
}
