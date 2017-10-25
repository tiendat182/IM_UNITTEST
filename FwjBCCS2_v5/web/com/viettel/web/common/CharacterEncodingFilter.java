package com.viettel.web.common;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * @author quangkm
 */

import com.ocpsoft.pretty.PrettyContext;
import com.ocpsoft.pretty.faces.config.mapping.UrlMapping;
import com.viettel.fw.common.util.Const;
import org.apache.log4j.Logger;
import viettel.passport.util.Connector;
import viettel.passport.util.ModifyHeaderUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
thiendn1 sua
 */
public class CharacterEncodingFilter
        implements Filter {

    private static final Logger logger = Logger.getLogger(CharacterEncodingFilter.class);
    private List<String> _casAllowedURL = new ArrayList<>();
    private boolean timeoutToErrorPage;
    private boolean ignoreAjaxRequest = true;

    public CharacterEncodingFilter() {
        this.timeoutToErrorPage = false;
    }


    @Override
    public void init(FilterConfig config) throws ServletException {
        try {
            logger.debug("Lay danh sach AllowUrl tu file config 'cas_en_US.properties'");
            String[] casAllowedURLs = Connector.getAllowedUrls();
            if (casAllowedURLs != null) {
                _casAllowedURL.addAll(Arrays.asList(casAllowedURLs));
            }
        } catch (Exception ex) {
            logger.error("Loi lay danh sach AllowUrl tu file config:'cas_en_US.properties'", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        logger.debug("VSAFilter process request");
        HttpServletRequest req = null;
        HttpServletResponse res = null;

        if ((request instanceof HttpServletRequest)) {
            req = (HttpServletRequest) request;
        }
        String linkFull = req.getRequestURI() + req.getServletPath().substring(2);

        String returnURL = req.getParameter("return");
        if ((response instanceof HttpServletResponse)) {
            res = (HttpServletResponse) response;
        }
        req.setCharacterEncoding("UTF-8");
        Connector cnn = new Connector(req, res);

        if ((returnURL != null) && (returnURL.trim().length() > 0)) {
            cnn.returnUrl = returnURL;
        }

        req.setAttribute("VSA-IsPassedVSAFilter", "True");

        if ((allowURL(req.getRequestURI(), _casAllowedURL)) || (allowURL(linkFull, _casAllowedURL))) {
            logger.debug("request in allow urls: " + linkFull);
            chain.doFilter(request, response);
        }
        else if (req.getRequestURI().contains("primepush") && req.getRequestURI().indexOf("?") < 0) {
            String redirectUrl = req.getRequestURI().substring(0, req.getRequestURI().indexOf("primepush"));
            res.sendRedirect(redirectUrl + "home");
        } else {
            logger.debug("request not in allow urls. Check if session authen?");
            if (!cnn.isAuthenticate()) {
                logger.debug("session is not authen. Check if have ticket in request?");
                if (cnn.hadTicket()) {
                    logger.debug("have ticket from passport. Validate this ticket");
                    if (!cnn.getAuthenticate()) {
                        logger.warn("Redirect to error page by authenticate failure.");
                        String redirectUrl = Connector.getErrorUrl() + "?errorCode=" + "AuthenticateFailure";
                        req.setAttribute("VSA-IsPassedVSAFilter", "False");
                        res.setHeader("VSA-Flag", "InPageRedirect");
                        res.setHeader("VSA-Location", redirectUrl);
                        if ((this.ignoreAjaxRequest) && (req.getHeader("X-Requested-With") != null) && (req.getHeader("X-Requested-With").length() > 0)) {
                            logger.debug("ajax request! Fw request to next filter");
                            chain.doFilter(request, response);
                        } else {
                            logger.debug("redirect browser to error page: " + redirectUrl);
                            res.sendRedirect(redirectUrl);
                        }
                    } else {
                        logger.debug("validate successfull. Fw request to next filter");
                        chain.doFilter(request, response);
                    }
                } else {
                    logger.debug("Request have no ticket. Redirect to passport");
                    String serviceUrl = cnn.getServiceURL();

                    if ((returnURL != null) && (returnURL.trim().length() > 0)) {
                        serviceUrl = serviceUrl + "?return=" + returnURL;
                    }

                    String redirectUrl = cnn.getPassportLoginURL() + "?appCode=" + cnn.getDomainCode() + "&service=" + URLEncoder.encode(serviceUrl, "UTF-8");

                    req.setAttribute("VSA-IsPassedVSAFilter", "False");
                    res.setHeader("VSA-Flag", "InPageRedirect");
                    res.setHeader("VSA-Location", redirectUrl);
                    if ((this.ignoreAjaxRequest) && (req.getHeader("X-Requested-With") != null) && (req.getHeader("X-Requested-With").length() > 0)) {
                        logger.debug("ajax request! Fw request to next filter");
                        chain.doFilter(request, response);
                    } else {
                        if (this.timeoutToErrorPage) {
                            redirectUrl = Connector.getErrorUrl() + "?errorCode=" + "SessionTimeout";
                        }
                        logger.debug("redirect to passport login: " + redirectUrl);
                        res.sendRedirect(redirectUrl);
                    }
                }
            } else {
                logger.debug("session already authen. Check session hijacking");
                HttpSession session = req.getSession();
                logger.debug("get ip,mac stored in session");
                String macSavedInSession = (String) session.getAttribute("VTS-MAC");
                logger.debug("mac store: " + macSavedInSession);
                String ipSavedInSession = (String) session.getAttribute("VTS-IP");
                logger.debug("ip lan sotre: " + ipSavedInSession);
                String ipWanSavedInSession = (String) session.getAttribute("VTS-IPWAN");
                logger.debug("ipwan stored: " + ipWanSavedInSession);
                logger.debug("get ip,mac in request");
                String mac = req.getHeader("VTS-MAC");
                String ip = req.getHeader("VTS-IP");
                String ipWan = req.getRemoteAddr();
                try {
                    logger.debug("decode ip");
                    if ((ip != null) && (ip.length() > 0)) {
                        ip = ModifyHeaderUtils.parseIP(ip);
                    } else {
                        ip = null;
                    }
                    logger.debug("decode mac");
                    if ((mac != null) && (mac.length() > 0)) {
                        mac = ModifyHeaderUtils.parseMAC(mac);
                    } else {
                        mac = null;
                    }
                } catch (Exception e) {
                    ip = null;
                    mac = null;
                    logger.error("Giai ma modify header that bai " + e.getMessage(), e);
                }
                logger.debug("ip in request: " + ip);
                logger.debug("mac in request: " + mac);

                String fakeSessionRedirectUrl = Connector.getErrorUrl() + "?errorCode=" + "NotPermissionAction";

                boolean fakeSession = false;
                logger.debug("compare ip wan");
                if (!ipWanSavedInSession.equalsIgnoreCase(ipWan)) {
                    logger.fatal(String.format("User: %s dang nhap tu 2 ipwan!!! ipwan dau tien %s, ipwan sau %s", new Object[]{(String) session.getAttribute("netID"), ipWanSavedInSession, ipWan}));
                    res.sendRedirect(fakeSessionRedirectUrl);
                    session.invalidate();
                    fakeSession = true;
                }
                logger.debug("compate ip lan");
                if ((!fakeSession) && (ipSavedInSession != null) && (!ipSavedInSession.equalsIgnoreCase(ip))) {
                    logger.fatal(String.format("User: %s dang nhap tu 2 ip!!! ip dau tien %s, ip sau %s", new Object[]{(String) session.getAttribute("netID"), ipSavedInSession, ip}));
                    res.sendRedirect(fakeSessionRedirectUrl);
                    session.invalidate();
                    fakeSession = true;
                }
                logger.debug("compare mac");
                if ((!fakeSession) && (macSavedInSession != null) && (!macSavedInSession.equalsIgnoreCase(mac))) {
                    logger.fatal(String.format("User: %s dang nhap tu 2 mac!!! mac dau tien %s, mac sau %s", new Object[]{(String) session.getAttribute("netID"), macSavedInSession, mac}));

                    res.sendRedirect(fakeSessionRedirectUrl);
                    session.invalidate();
                    fakeSession = true;
                }

                if (!fakeSession) {
                    logger.debug("url already declare in VSA. Check rights excute url?");
                    UrlMapping mapping = PrettyContext.getCurrentInstance(req).getCurrentMapping();
                    if(mapping.getPassThrough()!=null&&mapping.getPassThrough().trim().equals("true")){
                        logger.debug("url have rights to excute. Fw to next filter");
                        chain.doFilter(request, response);
                    }
                    else{
                        List<String> vsaAllowedURL = getVsaAllowedServletPath(req);
                        if (mapping != null && vsaAllowedURL != null && vsaAllowedURL.contains(mapping.getPattern().split("/#", 0)[0])) {
                            logger.debug("url have rights to excute. Fw to next filter");
                            chain.doFilter(request, response);
                        } else {
                            logger.error("Khai bao chua phan quyen: " + req.getServletPath() + "\n");
                            String redirectUrl = Connector.getErrorUrl() + "?errorCode=" + "NotPermissionAction";
                            req.setAttribute("VSA-IsPassedVSAFilter", "False");
                            res.setHeader("VSA-Flag", "NewPageRedirect");
                            res.setHeader("VSA-Location", redirectUrl);
                            if ((this.ignoreAjaxRequest) && (req.getHeader("X-Requested-With") != null) && (req.getHeader("X-Requested-With").length() > 0)) {
                                logger.debug("bypass ajax request. next to filter");
                                chain.doFilter(request, response);
                            } else {
                                logger.debug("redirect to error page: " + redirectUrl);
                                res.sendRedirect(redirectUrl);
                            }
                        }
                    }


                }
            }
        }

    }

    @Override
    public void destroy() {
    }

    private boolean allowURL(String url, List<String> listAllowUrl) {
        if (url.indexOf("javax.faces.resource") > 0) {
            return true;
        }
        return listAllowUrl.contains(url);
    }


    private List<String> getVsaAllowedServletPath(HttpServletRequest request) {
        List<String> vsaAllowedURL = (List<String>) request.getSession().getAttribute(Const.VSA_ALLOW_URL);
        return vsaAllowedURL;
    }

}
