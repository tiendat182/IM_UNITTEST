package com.viettel.web.log;

import akka.actor.ActorSystem;
import com.viettel.fw.SystemConfig;
import com.viettel.fw.common.util.Const;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.GetTextFromBundleHelper;
import com.viettel.fw.dto.UserDTO;
import com.viettel.fw.log.LogCollectorActor;
import com.viettel.ws.common.utils.GenericWebInfo;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by thiendn1 on 4/12/2015.
 */
public class LoggingFilter extends OncePerRequestFilter {

    @Autowired(required = false)
    ActorSystem actorSystem;

    @Autowired
    SystemConfig systemConfig;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
        String sessionId = request.getSession().getId();
        String callId = DataUtil.getRandomKpiId();
        request.setAttribute(Const.CALL_ID_TOKEN, callId);
        request.setAttribute(Const.KPI_ID_TOKEN, 0);

        ThreadContext.put("username", "UNKNOWN");
        ThreadContext.put("remoteip", "");

        HttpSession session = request.getSession();
        if (session != null) {
            ThreadContext.put("username", (String) session.getAttribute("netID"));
            ThreadContext.put("remoteip", (String) session.getAttribute("VTS-IPWAN"));
        }

        GenericWebInfo genericWebInfo = new GenericWebInfo();
        FacesContext context = FacesContext.getCurrentInstance();
        if (context != null) {
            UIViewRoot viewRoot = context.getViewRoot();
            if (viewRoot != null) {
                genericWebInfo.setLanguage(viewRoot.getLocale().getLanguage());
                genericWebInfo.setCountry(viewRoot.getLocale().getCountry());
            }
        }

        genericWebInfo.setReqId(String.valueOf(request.getAttribute(Const.CALL_ID_TOKEN)));
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        genericWebInfo.setIpAddress(ipAddress);
        UserDTO userDTO = (UserDTO) request.getSession().getAttribute(com.viettel.fw.common.util.Const.USER_DTO_TOKEN);
        if (userDTO != null) {
            genericWebInfo.setStaffCode(userDTO.getStaffCode());
            genericWebInfo.setStaffId(userDTO.getStaffId());
            genericWebInfo.setShopCode(userDTO.getShopCode());
            genericWebInfo.setShopId(userDTO.getShopId());
            genericWebInfo.setUserId(userDTO.getUserId());
        }

        try {
            genericWebInfo.setServerPort(String.valueOf(request.getServerPort()));
            genericWebInfo.setServerAddress(String.valueOf(request.getServerName()));
        } catch (Exception e) {
            logger.error("Exception line 103 ", e);
        }

        //Day vao ThreadContext cua Log4j2
        ThreadContext.put("kpi", genericWebInfo.getReqId());
        //Day vao ThreadLocal dung khi GetTextFromBundleHelper.getGenericWebInfo
        GetTextFromBundleHelper.setGenericWebInfo(genericWebInfo);
        //Do filter
        chain.doFilter(request, res);

        if (actorSystem != null) {
            if (systemConfig.IS_RUN_SYS_LOG) {
                actorSystem.actorSelection("user/" + Const.LOGGING_COLLECTOR_ACTOR + sessionId)
                        .tell(new LogCollectorActor.ANALYTIC(callId), null);
            }
            if (systemConfig.IS_RUN_KPI_LOG) {
                String kpiHeader = request.getHeader("VTS-KPIID");
                if (DataUtil.isNullOrEmpty(kpiHeader)) {
                    return;
                }
                actorSystem.actorSelection("user/" + Const.LOGGING_COLLECTOR_ACTOR + sessionId)
                        .tell(new LogCollectorActor.ANALYTIC_KPI(callId), null);
            }
        }
    }

    @Override
    public void destroy() {

    }
}
