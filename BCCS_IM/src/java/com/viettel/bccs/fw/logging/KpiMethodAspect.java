package com.viettel.bccs.fw.logging;

import com.ocpsoft.pretty.PrettyContext;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.fw.log.KpiLog;
import com.viettel.web.common.controller.BaseController;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.omnifaces.util.Faces;
import org.springframework.beans.factory.annotation.Value;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

/**
 * @author nhannt34
 * @since 14/02/2017
 */
@Aspect
public class KpiMethodAspect {

    @Value("${defaultSys:null}")
    private String defaultSys;

    private static final Logger logger = Logger.getLogger("KPI_LOGGER");
    private static DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");

    @Around("@annotation(com.viettel.bccs.fw.logging.Kpi)")
    public Object logKpi(ProceedingJoinPoint pjp) throws Throwable {
        Kpi kpi = null;
        BaseController base = null;
        try {
            base = (BaseController) pjp.getTarget();
            MethodSignature signature = (MethodSignature) pjp.getSignature();
            Method method = signature.getMethod();
            Annotation[] annotations = method.getAnnotations();

            kpi = Arrays.stream(annotations)
                    .filter(x -> x instanceof Kpi)
                    .findFirst()
                    .map(x -> (Kpi) x)
                    .orElse(null);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        Object result = pjp.proceed();
        try {
            if (kpi != null && base != null) {
                long lastRun = base.getLastRun();
                long currRun = Instant.now().toEpochMilli();
                if (Faces.isValidationFailed()) {
                    // Tam thoi off khong xu li gi o day
                    // logger.info(Joiner.on(" - ").skipNulls().join(kpi.value(), base.getUuid(), "FAILED", df.format(currRun)));
                } else {
                    String className = pjp.getSignature().getDeclaringTypeName();
                    String methodName = pjp.getSignature().getName();
                    KpiLog kpiLog = new KpiLog();
                    kpiLog.setDefaultSys(defaultSys);
                    kpiLog.setEndTime(new Date(currRun));
                    kpiLog.setUser(BccsLoginSuccessHandler.getStaffDTO().getStaffCode());
                    kpiLog.setIpRemote(BccsLoginSuccessHandler.getIpAddress() + ":" + BccsLoginSuccessHandler.IP_PORT_WEB);
                    kpiLog.setPath(PrettyContext.getCurrentInstance().getRequestURL().toURL());
                    kpiLog.setUri(className + "." + methodName);
                    kpiLog.setParams(null);
                    kpiLog.setClassName(pjp.getSignature().getDeclaringTypeName());
                    kpiLog.setDuration(currRun - lastRun);
                    kpiLog.setMethodName(pjp.getSignature().getName());
                    kpiLog.setCallId(base.getUuid());
                    kpiLog.setKpiHeader(kpi.value());
                    logger.info(kpiLog.toString());
                    base.setUuid(UUID.randomUUID().toString());
                    base.setLastRun(currRun);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return result;
    }
}
