package com.viettel.solrj;

import com.viettel.fw.bean.ApplicationContextProvider;

/**
 * Created by thiendn1 on 20/2/2016.
 */
public class SolrCore {

    private String core;
    private String url;
    private String username;
    private String password;

    public SolrCore(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }
    public SolrCore(String core, String url, String username, String password) {
        this.core = core;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public SolrCore(String core) {
        this.core = core;
    }

    public String getCore() {
        return core;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String toString() {
        return core + "|" + url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCore(String core) {
        this.core = core;
    }

    public static SolrCore createSolrCore(String solrBean) {
        SolrCore solrCore = (SolrCore) ApplicationContextProvider.getApplicationContext().getBean(solrBean);
        return solrCore;
    }
}
