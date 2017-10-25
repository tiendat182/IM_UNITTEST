package com.viettel.web.common;


import com.viettel.fw.SystemConfig;
import com.viettel.fw.common.util.Const;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.passport.CustomConnector;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import viettel.passport.client.ObjectToken;
import viettel.passport.client.UserToken;
import viettel.passport.util.Connector;

import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.*;


/**
 * thiendn1
 */

@Component
@ManagedBean
@Scope("request")
public class LoginController implements Serializable {

    @Autowired
    SystemConfig systemConfig;

    private String vsaTicket = null;

    public String getVsaTicket() {
        return vsaTicket;
    }


    public void setVsaTicket(String vsaTicket) {
        this.vsaTicket = vsaTicket;
    }

    private static final Logger logger = Logger.getLogger(LoginController.class);

    public void login() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext extenalContext = facesContext.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) extenalContext.getRequest();
        try {
            if (DataUtil.isNullOrEmpty(vsaTicket)) {
                String redirectUrl = CustomConnector.passportLoginURL
                        + "?appCode="
                        + CustomConnector.domainCode
                        + "&service="
                        + URLEncoder.encode(CustomConnector.serviceURL, "UTF-8");
                request.setAttribute("VSA-IsPassedVSAFilter", "False");
                request.setAttribute("VSA-Flag", "InPageRedirect");
                request.setAttribute("VSA-Location", redirectUrl);
                request.setAttribute("VSA-Redirect", "1");
                extenalContext.redirect(redirectUrl);
            } else {
                RequestDispatcher dispatcher = request.getRequestDispatcher("/j_spring_security_check?ticket=" + vsaTicket);
                dispatcher.forward(request, (ServletResponse) extenalContext.getResponse());
                SecurityContextHolder.getContext().getAuthentication();
                facesContext.responseComplete();
                UserToken userToken = (UserToken) request.getSession().getAttribute(CustomConnector.VSA_USER_TOKEN);
                if (userToken != null) {
                    List<String> vsaAllowedURL = new ArrayList<>();
                    for (ObjectToken ot : userToken.getObjectTokens()) {
                        String servletPath = ot.getObjectUrl();
                        if (!("#".equals(servletPath))) {
                            vsaAllowedURL.add(servletPath.split("\\?")[0]);
                        }
                    }
                    request.getSession().setAttribute(Const.VSA_ALLOW_URL, vsaAllowedURL);
                    request.getSession().setAttribute(Const.DEFAULT_SYS, systemConfig.DEFAULT_SYS);
                    request.getSession().setAttribute("userName", userToken.getUserName());
                    request.getSession().setAttribute(CustomConnector.LOGIN_DATE, new Date(System.currentTimeMillis()));

                }

            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            this.vsaTicket = null;
        }

    }

    public void logout() throws Exception {

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        SecurityContextHolder.clearContext();
        /**
         * Delete Cookies
         */
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
        Cookie cookie = new Cookie("SPRING_SECURITY_REMEMBER_ME_COOKIE", null);
        Cookie jsessionId = new Cookie("JSESSIONID", null);
        cookie.setMaxAge(0);
        cookie.setPath(request.getContextPath().length() > 0 ? request
                .getContextPath() : "/");
        jsessionId.setMaxAge(0);
        jsessionId.setPath(request.getContextPath().length() > 0 ? request
                .getContextPath() : "/");
        response.addCookie(cookie);
        response.addCookie(jsessionId);

        Locale locale = new Locale("en", "US");
        ResourceBundle rb1 = ResourceBundle.getBundle("cas", locale);

        StringBuilder redirectUrl = new StringBuilder();
        UserToken userToken = (UserToken) request.getSession().getAttribute(CustomConnector.VSA_USER_TOKEN);
        if (userToken != null) {
            redirectUrl.append(rb1.getString("logoutUrl")).append("?service=").append(URLEncoder.encode(rb1.getString("service"), "UTF-8"))
                    .append("&userName=").append(userToken.getUserName())
                    .append("&appCode=").append(CustomConnector.domainCode);
        }

        externalContext.invalidateSession();
        externalContext.redirect(redirectUrl.toString());
    }

    public void handleExpiredSession() throws IOException {

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Locale locale = new Locale("en", "US");
        ResourceBundle rb1 = ResourceBundle.getBundle("cas", locale);
        StringBuilder redirectUrl = new StringBuilder();
        try {
            redirectUrl.append(MessageFormat.format("{0}?service={1}&appCode={2}?faces-redirect=true",
                    rb1.getString("logoutUrl"), URLEncoder.encode(rb1.getString("service"), "UTF-8"), CustomConnector.domainCode));
        } catch (UnsupportedEncodingException e) {
            logger.error(e);
        }
        try {
            externalContext.redirect(redirectUrl.toString());
        } catch (IOException e) {
            logger.error(e);
        }
    }
}
