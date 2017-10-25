package com.viettel.bccs.fw.common;


import com.viettel.bccs.inventory.dto.StaffDTO;
import com.viettel.bccs.inventory.service.StaffService;
import com.viettel.fw.bean.ApplicationContextProvider;
import com.viettel.fw.common.util.Const;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.dto.UserDTO;
import com.viettel.fw.passport.CustomConnector;
import com.viettel.web.common.security.CustomPrincipal;
import com.viettel.web.common.security.URLBean;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import viettel.passport.client.UserToken;

import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by vtsoft on 4/15/2015.
 */
public class BccsLoginSuccessHandler implements AuthenticationSuccessHandler {

    public static final Logger logger = Logger.getLogger(BccsLoginSuccessHandler.class);

    public static String IP_PORT_WEB = DataUtil.getEndPoint();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication auth)
            throws IOException, ServletException {
        URLBean urlBean = getURLBean(request, response, auth);
        if (urlBean != null) {
            response.sendRedirect(urlBean.getTargetUrl());
            return;
        }
        response.sendRedirect(request.getContextPath() + "/home");

        //Luon goi ham nay de truyen sang service
        getStaffDTO();
    }

    private URLBean getURLBean(HttpServletRequest request,
                               HttpServletResponse response, Authentication auth) {
        URLBean urlBean = (URLBean) request.getSession().getAttribute(com.viettel.fw.common.util.Const.URL_BEAN);
        if (urlBean == null) {
            CustomPrincipal principal = (CustomPrincipal) auth.getPrincipal();
            if (principal != null) {
                urlBean = (URLBean) principal.getPrincipals().get(1);
            }
        }

        return urlBean;
    }

    public static String getUserName() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        UserToken userToken = (UserToken) request.getSession().getAttribute(CustomConnector.VSA_USER_TOKEN);
        return userToken.getUserName();
    }

    /**
     * Sua nhanh loi mat staffDTO trong truong hop session keo dai
     * @return
     */
    public static StaffDTO getStaffDTO() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        StaffDTO staffDTO = (StaffDTO) request.getSession().getAttribute(Const.STAFF_TOKEN);
        try {
            if (staffDTO == null || DataUtil.isNullOrEmpty(staffDTO.getStaffCode())) {
                UserToken userToken = (UserToken) request.getSession().getAttribute(CustomConnector.VSA_USER_TOKEN);


                ApplicationContext context = ApplicationContextProvider.getApplicationContext();
                //staffDTO = testStaffDto();

                StaffService staffServiceIn = (StaffService) context.getBean(StaffService.class);
                staffDTO = staffServiceIn.getStaffByStaffCode(userToken.getUserName());
                request.getSession().setAttribute(Const.STAFF_TOKEN, staffDTO);

                //UserDTO ch?a c�c th�ng tin c? b?n ?? g?i qua webservice
                if (staffDTO != null) {
                    UserDTO userDTO = new UserDTO(staffDTO.getStaffId(), staffDTO.getStaffCode(), staffDTO.getShopId(), staffDTO.getShopCode());
                    request.getSession().setAttribute(com.viettel.fw.common.util.Const.USER_DTO_TOKEN, userDTO);
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        return staffDTO;
    }

    public static String getIpAddress() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

    public static Long getUserCode() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        UserToken userToken = (UserToken) request.getSession().getAttribute(CustomConnector.VSA_USER_TOKEN);
        return userToken.getUserID();
    }
}
