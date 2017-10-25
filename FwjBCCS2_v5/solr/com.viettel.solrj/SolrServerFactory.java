package com.viettel.solrj;

/**
 * Created by thiendn1 on 5/11/2015.
 */

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpClientUtil;
import org.apache.solr.client.solrj.impl.HttpSolrClient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class SolrServerFactory
{
    Map <String, SolrClient> urlToServer = new ConcurrentHashMap <> ();
    static SolrServerFactory instance = new SolrServerFactory ();

    public static SolrServerFactory getInstance()
    {
        return instance;
    }

    private SolrServerFactory ()
    {
    }

    public SolrClient createServer (String solrURL,String username,String password)
    {
        if (urlToServer.containsKey(solrURL))
            return urlToServer.get(solrURL);
        /*
        HttpSolrServer is thread-safe and if you are using the following constructor,
        you *MUST* re-use the same instance for all requests.  If instances are created on
        the fly, it can cause a connection leak. The recommended practice is to keep a
        static instance of HttpSolrServer per solr server url and share it for all requests.
        See https://issues.apache.org/jira/browse/SOLR-861 for more details
      */
        HttpSolrClient server = new HttpSolrClient( solrURL );
        if(username!=null&&password!=null){
            HttpClientUtil.setBasicAuth((DefaultHttpClient) server.getHttpClient(),username,password);
        }
        configureSolr(server);
        urlToServer.put(solrURL, server);
        return server;
    }

    private void configureSolr(HttpSolrClient server) {
        server.setConnectionTimeout(5000); // 5 seconds to establish TCP
        // The following settings are provided here for completeness.
        // They will not normally be required, and should only be used
        // after consulting javadocs to know whether they are truly required.
        server.setSoTimeout(10000);  // socket read timeout
        server.setDefaultMaxConnectionsPerHost(100);
        server.setMaxTotalConnections(100);
        server.setFollowRedirects(false);  // defaults to false
        // allowCompression defaults to false.
        // Server side must support gzip or deflate for this to have any effect.
        server.setAllowCompression(false);
    }

}