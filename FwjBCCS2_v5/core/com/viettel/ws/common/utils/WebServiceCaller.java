package com.viettel.ws.common.utils;

import com.viettel.ws.provider.WebServiceSchema;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.*;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.net.ssl.SSLContext;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

/**
 * Created by thiendn1 on 1/5/2015.
 */
//x_1704_1: fix bug khi co exception thi ko dong HttpClient
//x_1704_2: thiet lap timeout tung request

public class WebServiceCaller {

    protected final int CONNECTION_TIMEOUT = 30000;
    protected final int RECEIVE_TIMEOUT = 60000;

    protected final int DEFAULT_KEEP_ALIVE = 1;
    protected final WebServiceSchema webServiceSchema = new WebServiceSchema();
    protected int keepAlive = 1;
    private static final Logger logger = Logger.getLogger("HttpClient");

    protected final CookieStore cookieStore;
    protected final CredentialsProvider credentialsProvider;
    protected final PoolingHttpClientConnectionManager connManager;

    public WebServiceCaller() {
        KeyStore trustStore = null;
        try {
            trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            logger.error(e);
        }
        // Trust own CA and all self-signed certs
        SSLContext sslcontext = null;
        try {
            sslcontext = SSLContexts.custom()
                    .loadTrustMaterial(trustStore, new TrustSelfSignedStrategy())
                    .build();
            // Allow TLSv1 protocol only
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            logger.error(e);
        }
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", sslsf)
                .build();
        // Create a connection manager with custom configuration.
        connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        // Create socket configuration
        SocketConfig socketConfig = SocketConfig.custom()
                .setTcpNoDelay(true)
                .build();
        // Configure the connection manager to use socket configuration either
        // by default or for a specific host.
        connManager.setDefaultSocketConfig(socketConfig);
        // Create message constraints
        MessageConstraints messageConstraints = MessageConstraints.custom()
                .setMaxHeaderCount(200)
                .setMaxLineLength(2000)
                .build();

        // Create connection configuration
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
                .setMalformedInputAction(CodingErrorAction.IGNORE)
                .setUnmappableInputAction(CodingErrorAction.IGNORE)
                .setCharset(Consts.UTF_8)
                .setMessageConstraints(messageConstraints)
                .build();
        // Configure the connection manager to use connection configuration either
        // by default or for a specific host.
        connManager.setDefaultConnectionConfig(connectionConfig);
        // Configure total max or per route limits for persistent connections
        // that can be kept in the pool or leased by the connection manager.
        connManager.setMaxTotal(300);
        connManager.setDefaultMaxPerRoute(300);

        // Use custom cookie store if necessary.
        cookieStore = new BasicCookieStore();
        // Use custom credentials provider if necessary.
        credentialsProvider = new BasicCredentialsProvider();

    }


    public String webServiceCaller(WsRequestCreator wsConfig) throws Exception {
        validateWs(wsConfig);
        return soapSecurityServiceCallerByConfig(wsConfig);

    }

    public String webServiceCaller(WsRequestCreator wsConfig, String header) throws Exception {
        validateWs(wsConfig);
        return soapSecurityServiceCallerByConfig(wsConfig, header);

    }

    private SOAPMessage getSoapMessageFromString(String xml) throws SOAPException, IOException {
        MessageFactory factory = MessageFactory.newInstance();
        SOAPMessage message = factory.createMessage(new MimeHeaders(), new ByteArrayInputStream(xml.getBytes(Charset.forName("UTF-8"))));
        return message;
    }

    private String getText(SOAPMessage soapMessage) throws IOException, SOAPException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        soapMessage.writeTo(out);
        return new String(out.toByteArray());
    }


    public String execute(String requestTxt, WsRequestCreator wsConfig)
            throws Exception {
        String urlAddress = wsConfig.getWsAddress();
        boolean closeOnResponse = wsConfig.isCloseOnResponse();
        String username = wsConfig.getUsername();
        String password = wsConfig.getPassword();
        URL aURL = new URL(urlAddress);
        int timeout = (wsConfig.getConnectionTimeout() == 0) ? CONNECTION_TIMEOUT : wsConfig.getConnectionTimeout();
        int receiveTimeout = (wsConfig.getReceiveTimeout() == 0) ? RECEIVE_TIMEOUT : wsConfig.getReceiveTimeout();
        this.keepAlive = DEFAULT_KEEP_ALIVE;

        CloseableHttpClient httpClient = null;
        CredentialsProvider credsProvider = null;

        if (username != null || password != null) {
            credsProvider = new BasicCredentialsProvider();
            credsProvider.setCredentials(
                    new AuthScope(aURL.getHost(), aURL.getPort()),
                    new UsernamePasswordCredentials(username, password));

        }
        // Create global request configuration
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setConnectTimeout(timeout)
                .setSocketTimeout(receiveTimeout)
                .build();

        // Create an HttpClient with the given custom dependencies and configuration.
        if (credsProvider == null) {
            httpClient = HttpClients.custom()
                    .setConnectionManager(connManager)
                    .setDefaultCookieStore(cookieStore)
                    .setDefaultCredentialsProvider(credentialsProvider)
                    .setProxy(wsConfig.getProxy())
                    .build();

        } else {
            httpClient = HttpClients.custom()
                    .setConnectionManager(connManager)
                    .setDefaultCookieStore(cookieStore)
                    .setDefaultCredentialsProvider(credsProvider)
                    .setDefaultRequestConfig(defaultRequestConfig)
                    .setProxy(wsConfig.getProxy())
                    .build();
        }

        HttpPost httpPost = new HttpPost(urlAddress);
        StringEntity reqEntity = new StringEntity(requestTxt, ContentType.create(ContentType.TEXT_XML.getMimeType(), "utf-8"));
        httpPost.setEntity(reqEntity);
        // get response
        HttpResponse httpResponse = httpClient.execute(httpPost);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        String response = EntityUtils.toString(httpResponse.getEntity());
        logger.info(response);
        //x_1704_1
        httpPost.releaseConnection();
        if (closeOnResponse) {
            httpClient.close();
        }

        if (statusCode != 200) {
            throw new Exception(response);
        }

        return response;

    }

    public String soapSecurityServiceCallerByConfig(WsRequestCreator wsConfig) throws Exception {
        wsConfig.setBodyParameterTxt(XmlSchema.formatParameters(wsConfig));
        wsConfig.setHeaderParameterTxt(XmlSchema.formatHeaderParameters(wsConfig));
        String request = webServiceSchema.parseParameters(wsConfig);
        logger.info(request);
        return execute(request, wsConfig);
    }

    public String soapSecurityServiceCallerByConfig(WsRequestCreator wsConfig, String header) throws Exception {
        wsConfig.setBodyParameterTxt(XmlSchema.formatParameters(wsConfig));
        wsConfig.setHeaderParameterTxt(header);
        String request = webServiceSchema.parseParameters(wsConfig);
        return execute(request, wsConfig);
    }


    private void validateWs(WsRequestCreator wsConfig) throws Exception {
        if (wsConfig.getWsAddress() == null) {
            throw new Exception("WebService caller has no address");
        }
        if (wsConfig.getTargetNameSpace() == null) {
            throw new Exception("WebService caller has no targetNameSpace");
        }
        if (wsConfig.getServiceName() == null) {
            throw new Exception("WebService caller has no method");
        }
        if (wsConfig.getBodyParameters() != null && wsConfig.getBodyParameterTxt() != null) {
            throw new Exception("WebService caller does not allowed to have both parameter list and text");
        }
    }

}
