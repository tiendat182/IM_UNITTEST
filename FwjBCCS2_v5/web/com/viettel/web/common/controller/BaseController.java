package com.viettel.web.common.controller;


import com.ocpsoft.pretty.PrettyContext;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.BundleUtil;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.log.LogNotify;
import com.viettel.fw.passport.CustomConnector;
import com.viettel.web.common.LocateBean;
import com.viettel.web.contextmenu.*;
import com.viettel.web.log.ReportLogService;
import com.viettel.ws.common.utils.Locate;
import nl.captcha.Captcha;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.log4j.Logger;
import org.omnifaces.util.Faces;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.ClassUtils;
import viettel.passport.client.UserToken;

import javax.annotation.Nonnull;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.SocketTimeoutException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public abstract class BaseController implements Serializable {

    public static final Logger logger = Logger.getLogger(BaseController.class);
    private static final String POS_PAID = "1";
    private static final String PRE_PAID = "2";
    private static final String BLOCKONEWAY = "1";
    private static final String BLOCKTWOWAY = "2";
    private static final String BIT_2_ACT_STATUS_ACTIVE = "0";
    private static final Long DEFAULT_LONG_VALUE = 0L;
    private String capcha;
    private String uuid;
    private long lastRun;


    @Autowired(required = false)
    ReportLogService reportLogService;
    @Autowired
    ApplicationContext context;
    @Autowired
    protected LocateBean locateBean;

    public String getText(String key) {
        return BundleUtil.getText(new Locate(locateBean.getSelectedLocale().getLanguage(),
                locateBean.getSelectedLocale().getCountry()), key);
    }

    /* added by nhannt34*/
    public String convertNumberUsingCurrentLocate(Number number) {
        return DataUtil.convertNumberUsingCurrentLocale(number);
    }

    public String getTextParam(String key, String... params) {
        return MessageFormat.format(getText(key), params);
    }

    //thiendn1: make code shorter
    private void baseReportSuccess(String displayArea, String msg) {
        try {
            if (reportLogService != null) {
                reportLogService.successMessage(new LogNotify(msg, null));
            }
            BundleUtil.sendSuccessMsg(displayArea, "", msg);
        } catch (Exception ex) {
            logger.error("", ex);
        }
    }

    private void baseReportError(String displayArea, String errorMsg, String logMsg, Exception exc) {
        try {
            if (reportLogService != null) {
                reportLogService.errorMessage(new LogNotify(logMsg, exc));
            }

            String keyMsg;
            if (exc instanceof LogicException) {
                keyMsg = ((LogicException) exc).getErrorCode();
            } else {
                keyMsg = getText("common.error.happened");
                logger.error(exc.getMessage(), exc);
//                keyMsg = exc.getMessage();
            }

            BundleUtil.sendErrorMsg(displayArea, keyMsg, errorMsg);
            FacesContext.getCurrentInstance().validationFailed();
        } catch (Exception ex) {
            logger.error("", ex);
        }
    }

    public void reportSuccess(String displayArea, String keyMsg, Object... params) {
        String msg = MessageFormat.format(getText(keyMsg), params);
        baseReportSuccess(displayArea, msg);
    }

    public void reportSuccess(String displayArea, String keyMsg) {
        baseReportSuccess(displayArea, getText(keyMsg));
    }

    public void reportError(String displayArea, String errCode, String keyMsg) {
        String errorMsg = null;
        //thiendn1: khong su dung errorCode cu
//        if (!DataUtil.isNullOrEmpty(errCode)) {
//            errorMsg = errCode + ": " + getText(keyMsg);
//
//        } else {
//            errorMsg = getText(keyMsg);
//        }
        errorMsg = getText(keyMsg);
        baseReportError(displayArea, errorMsg, getMsgByKey(keyMsg), new LogicException(errCode, keyMsg));

    }

    public void reportError(String displayArea, String keyMsg, Exception exc) {
        baseReportError(displayArea, getText(keyMsg), getMsgByKey(keyMsg), exc);
    }

    public void reportErrorMsg(String displayArea, String errorCode, String msg) {
        baseReportError(displayArea, msg, msg, new LogicException(errorCode, msg));
    }

    private String getErrorMsg(@Nonnull BaseMessage baseMessage) {
        return getErrorMsg(baseMessage.getErrorCode(), baseMessage.getKeyMsg(), baseMessage.getParamsMsg());
    }

    private String getErrorMsg(String errorCode, String keyMsg, Object... params) {
        String msg = "";
        if (!DataUtil.isNullOrEmpty(keyMsg)) {
            if (DataUtil.isNullOrEmpty(params)) {
                msg = getMsgByKey(keyMsg);
            } else {
                msg = MessageFormat.format(getMsgByKey(keyMsg), params);
            }
        }

        String errorMsg = "";
        if (!DataUtil.isNullOrEmpty(errorCode)) {
            errorMsg = errorCode + ": " + msg;
        } else {
            errorMsg = msg;
        }
        return errorMsg;
    }

    public void reportError(String displayArea, @Nonnull BaseMessage baseMessage) {
        String errorMsg = getErrorMsg(baseMessage);
        baseReportError(displayArea, errorMsg, getMsgByKey(baseMessage.getKeyMsg()), new LogicException(baseMessage.getErrorCode(), baseMessage.getKeyMsg()));
    }

    public void reportError(String displayArea, String errCode, String keyMsg, Object... params) {
        String errorMsg = getErrorMsg(errCode, keyMsg, params);
        baseReportError(displayArea, errorMsg, getMsgByKey(keyMsg), new LogicException(errCode, keyMsg));
    }


    public void reportError(String displayArea, LogicException ex) {
        String errorMsg = getErrorMsg(ex.getErrorCode(), ex.getDescription());
        baseReportError(displayArea, errorMsg, errorMsg, ex);
    }

    private String getMsgByKey(String keyMsg) {
        String getTextKey = getText(keyMsg);
        return DataUtil.isNullOrEmpty(getTextKey) ? keyMsg : getTextKey;
    }

    @Deprecated
    public void reportErrorMsg(String displayArea, String msg) {
        try {
            BundleUtil.sendErrorMsg(displayArea, "", msg);
            FacesContext.getCurrentInstance().validationFailed();
        } catch (Exception ex) {
            logger.error("", ex);
        }

    }

    @Deprecated
    public void reportError(String displayArea, String keyMsg) {
        try {
            BundleUtil.sendErrorMsg(displayArea, "", getText(keyMsg));
        } catch (Throwable e) {
            logger.error(e.getMessage());
        }
    }

    @Deprecated
    public void reportError(String keyMsg, Object... params) {

    }

    public void reportWarn(String displayArea, String keyMsg, Object... params) {
        try {
            String msg = MessageFormat.format(getText(keyMsg), params);
            BundleUtil.sendWarnMsg(displayArea, "", msg);
        } catch (Exception ex) {
            logger.error("", ex);
        }

    }

    public UserToken getUserToken() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext extenalContext = facesContext.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) extenalContext.getRequest();
        return (UserToken) request.getSession().getAttribute(CustomConnector.VSA_USER_TOKEN);
    }

    public HttpServletRequest getRequest() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext extenalContext = facesContext.getExternalContext();
        return (HttpServletRequest) extenalContext.getRequest();
    }

    public boolean validatePattern(String regex, String value) {
        if (DataUtil.isNullOrEmpty(regex) || DataUtil.isNullOrEmpty(value)) {
            return false;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }


    public void reportErrorValidateFail(String displayArea, String errCode, String keyMsg) {
        reportError(displayArea, errCode, keyMsg);
        FacesContext.getCurrentInstance().validationFailed();
    }

    public void reportErrorValidateFail(String displayArea, @Nonnull BaseMessage baseMessage) {
        reportError(displayArea, baseMessage);
        FacesContext.getCurrentInstance().validationFailed();
    }

    @Deprecated
    public void reportErrorValidateFail(String displayArea, String keyMsg) {
        try {
            BundleUtil.sendErrorMsg(displayArea, "", getText(keyMsg));
            FacesContext.getCurrentInstance().validationFailed();
        } catch (Exception ex) {
            logger.error("", ex);
        }
    }

    public void reportErrorValidateFail(String displayArea, String errCode, String keyMsg, Object... params) {
        try {
            String msg = MessageFormat.format(getText(keyMsg), params);
            BundleUtil.sendErrorMsg(displayArea, "", errCode + " " + msg);
            FacesContext.getCurrentInstance().validationFailed();
        } catch (Exception ex) {
            logger.error("", ex);
        }
    }

    public void reportErrorValidateFail(String displayArea, LogicException ex) {
        try {
            if (!DataUtil.isNullOrEmpty(ex.getErrorCode())) {
                BundleUtil.sendErrorMsg(displayArea, "", ex.getErrorCode() + " " + ex.getDescription());
            } else {
                BundleUtil.sendErrorMsg(displayArea, "", ex.getDescription());
            }
            FacesContext.getCurrentInstance().validationFailed();
        } catch (Exception ex1) {
            logger.error("", ex1);
        }
    }

    public void reportErrorValidateFail(String displayArea, String errCode, Exception ex) {
        try {
            BundleUtil.sendErrorMsg(displayArea, "", errCode + " " + ex.getMessage());
            FacesContext.getCurrentInstance().validationFailed();
        } catch (Exception ex1) {
            logger.error("", ex1);
        }
    }

    public void reportWarnValidateFail(String displayArea, String keyMsg, Object... params) {
        try {
            String msg = MessageFormat.format(getText(keyMsg), params);
            BundleUtil.sendWarnMsg(displayArea, "", msg);
            FacesContext.getCurrentInstance().validationFailed();
        } catch (Exception ex) {
            logger.error("", ex);
        }
    }

    public void showDialog(String keyInfo, String keyMesg) {
        try {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, getText(keyInfo), getText(keyMesg));
            RequestContext.getCurrentInstance().showMessageInDialog(facesMessage);
        } catch (Exception ex) {
            logger.error("", ex);
        }
    }

    /**
     * Bien cac ki tu dac biet ve dang ascii
     *
     * @param input
     * @return
     */
    public String convertCharacter(String input) {
        if (DataUtil.isNullOrEmpty(input)) {
            return "";
        }
        return DataUtil.stripAccents(input);
    }

    public String trimAndLowercase(String input) {
        String temp = convertCharacter(DataUtil.trim(input));
        return StringUtils.lowerCase(temp);
    }


    /**
     * @param widgetVar
     * @author KhuongDV
     * @datecreate Nov 24th, 2014
     */
    public void focusElement(String widgetVar) {
        RequestContext.getCurrentInstance().execute("focusElement('" + widgetVar + "')");
    }

    public void focusElementWithCursor(String cssSelector) {
        RequestContext.getCurrentInstance().execute("focusElementWithCursor('" + cssSelector + "')");
    }

    /**
     * @param selector
     * @author KhuongDV
     * @datecreate Nov 24th, 2014
     */
    public void focusElementByRawCSSSlector(String selector) {
        RequestContext.getCurrentInstance().execute("focusElementByRawCSSSlector('" + selector + "')");
    }

    /**
     * @param selector
     * @author KhuongDV
     * @datecreate Nov 24th, 2014
     */
    public void showErrorToElement(String selector) {
        RequestContext.getCurrentInstance().execute("showErrorToElement('" + selector + "')");
    }

    public void showDialog(String widgetVar) {
        RequestContext.getCurrentInstance().execute("PF('" + widgetVar + "').show()");
    }

    public void hideDialog(String widgetVar) {
        RequestContext.getCurrentInstance().execute("PF('" + widgetVar + "').hide()");
    }


    /**
     * Phan tich content cua warning message/email
     *
     * @param content
     * @param firstSign
     * @param lastSign
     * @return
     * @author KhuongDV
     */
    public int countParameter(String content, String firstSign, String lastSign) {
        int count = 0;
        firstSign = Pattern.quote(firstSign);
        lastSign = Pattern.quote(lastSign);
        Pattern pattern = Pattern.compile(firstSign + "([a-zA-Z_][a-zA-Z0-9_]*)" + lastSign);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    public Set<String> getParameterInText(String text, String firstSign, String lastSign) {
        Set<String> set = new java.util.HashSet<>();
        String QfirstSign = Pattern.quote(firstSign);
        String QlastSign = Pattern.quote(lastSign);
        Pattern pattern = Pattern.compile(QfirstSign + "([a-zA-Z_][a-zA-Z0-9_]*)" + QlastSign);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            set.add(matcher.group().replace(firstSign, "").replace(lastSign, "").toLowerCase());
        }
        return set;
    }

    int count(String src, String sub) {
        int lastIndex = 0;
        int count = 0;
        while (lastIndex != -1) {

            lastIndex = src.indexOf(sub, lastIndex);

            if (lastIndex != -1) {
                count++;
                lastIndex += sub.length();
            }
        }
        return count;
    }

    public void topPage() {
        RequestContext.getCurrentInstance().execute("topPage()");
    }

    public void focusElementError(String elementId) {
        RequestContext.getCurrentInstance().execute("focusElementError('" + elementId + "')");
    }

    public void removeFocusElementError(String elementId) {
        RequestContext.getCurrentInstance().execute("removeFocusElementError('" + elementId + "')");
    }

    protected void resetTable(String tableClientId) {
        DataTable tableIsdn = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent(tableClientId);
        if (tableIsdn != null) {
            tableIsdn.setRows(Integer.parseInt(getText("common.paging.rows.default")));
            tableIsdn.setFirst(0);
        }
    }

    protected void invokeMethod(Object controller, String action) {
        try {
            if (StringUtils.isNotBlank(action) && controller != null) {
                Method method = controller.getClass().getMethod(action);
                method.invoke(controller);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    protected void reportError(String areaUpdate, Exception e) {
        if (e instanceof LogicException) {
            LogicException lg = (LogicException) e;
            reportError(areaUpdate, lg.getErrorCode(), lg.getKeyMsg(), lg.getParamsMsg());
        } else if (e instanceof SocketTimeoutException) {
            logger.error(e.getMessage(), e);
            reportError(areaUpdate, "common.error.service", e);
        } else {
            logger.error(e.getMessage(), e);
            reportError(areaUpdate, e.getMessage(), e);
        }
    }

    protected void reportError(Exception e) {
        reportError("", e);
    }

    protected void reportErrorValidateFail(Exception e) {
        reportError(e);
    }

    /**
     * ham tong quat invoke toi noi dung
     *
     * @param methodName
     * @param param1
     * @param param2
     * @author ThanhNT
     */
    public void callBackMethod(Object objectModel, String methodName, Object param1, Object param2) throws LogicException, Exception {
        if (objectModel == null || DataUtil.isNullOrEmpty(methodName)) {
            return;
        }
        Class<?> classModel = objectModel.getClass();
        if (param1 != null && param2 != null) {
            Method method = classModel.getMethod(methodName, param1.getClass(), param2.getClass());
            if (method != null) {
                method.invoke(objectModel, param1, param2);
            }
        } else if (param1 != null) {
            Method method = classModel.getMethod(methodName, param1.getClass());
            if (method != null) {
                method.invoke(objectModel, param1);
            }
        } else if (param2 != null) {
            Method method = classModel.getMethod(methodName, param2.getClass());
            if (method != null) {
                method.invoke(objectModel, param2);
            }
        } else {
            Method method = classModel.getMethod(methodName);
            if (method != null) {
                method.invoke(objectModel);
            }
        }
    }

    /**
     * update component
     *
     * @param elementId
     * @author ThanhNT
     */
    public void updateElemetId(String elementId) {
        RequestContext.getCurrentInstance().update(elementId);
    }

    public void loadMenuContext(Object thisClass) {
        Map<String, Object> map = (Map) Faces.getSessionAttribute("CONTEXTMENU_CONTROLLER_SOURCE");
        MenuItem contextMenuItem = (MenuItem) Faces.getSessionAttribute("CONTEXTMENU_CONTROLLER_MENU_ITEM");
        if (contextMenuItem == null) {
            Faces.validationFailed();
            return;
        }
        try {
            Object targetObject = new CglibHelper(thisClass).getTargetObject();
            List<FieldDTO> setterFields = contextMenuItem.getFields();
            for (FieldDTO fieldDTO : contextMenuItem.getFields()) {
                String[] names = fieldDTO.getName().split("\\.");
                String fieldName = names[0];
                String subFieldName = names.length > 1 ? names[1] : null;
                Field field = FieldUtils.getField(targetObject.getClass(), fieldName, true);
                if (subFieldName != null) {
                    FieldUtils.writeField(field.get(targetObject), subFieldName, map.get(fieldDTO.getNameMap()), true);
                } else {
                    FieldUtils.writeField(targetObject, fieldName, map.get(fieldDTO.getNameMap()), true);
                }
            }
            for (MethodDTO methodDTO : contextMenuItem.getMethods()) {
                String methodName = methodDTO.getName();
                List<ParameterDTO> parameterNames = methodDTO.getParameteres();

                Class[] parameterTypes = null;
                Method method = null;
                Object[] parameters = null;
                if (parameterNames != null) {
                    parameterTypes = new Class[parameterNames.size()];
                    parameters = new Object[parameterNames.size()];

                    int i = 0;
                    for (ParameterDTO ob : parameterNames) {
                        parameterTypes[i] = ClassUtils.forName(ob.getType(), null);
                        i++;
                    }
                    i = 0;
                    for (ParameterDTO ob : parameterNames) {
                        if (ob.isMapping()) {
                            parameters[i] = map.get(ob);
                        } else {
                            parameters[i] = ob.getTrueValue();
                        }
                        i++;
                    }
                    method = thisClass.getClass().getMethod(methodName, parameterTypes);
                } else {
                    method = thisClass.getClass().getMethod(methodName, new Class[0]);
                }
                method.invoke(thisClass, parameters);
            }
        } catch (Exception e) {
            logger.error(e);
        }
        Faces.removeSessionAttribute("CONTEXTMENU_CONTROLLER_SOURCE");
        Faces.removeSessionAttribute("CONTEXTMENU_CONTROLLER_MENU_ITEM");
    }


    @Secured("@")
    public String getPayTypeView(String payType) {
        switch (payType) {
            case POS_PAID:
                return getText("lb.pay.type.pos");
            case PRE_PAID:
                return getText("lb.pay.type.pre");
            default:
                return "unknow";
        }
    }

    /**
     * kiem tra thue bao da duoc chan 1 chieu chua
     *
     * @param actStatus
     * @return true: dang bi chan 1 chieu
     * @author quangkm
     */
    @Secured("@")
    public boolean checkBlock1Way(String actStatus) {
        if (DataUtil.isNullObject(actStatus)) {
            return false;
        }
        String bitOne = DataUtil.safeToString(actStatus.toCharArray()[0]).trim();
        return BLOCKONEWAY.equals(bitOne);
    }

    /**
     * kiem tra thue bao da duoc chan 2 chieu chua
     *
     * @param actStatus
     * @return true: dang bi chan 2 chieu
     * @author quangkm
     */
    @Secured("@")
    public boolean checkBlock2Way(String actStatus) {
        if (DataUtil.isNullObject(actStatus)) {
            return false;
        }
        String bitOne = DataUtil.safeToString(actStatus.toCharArray()[0]).trim();
        return BLOCKTWOWAY.equals(bitOne);
    }


    /**
     * @param numberOne
     * @param numberTwo
     * @return
     * @author phuvk
     */
    @Secured("@")
    public boolean surplus(Long numberOne, Long numberTwo) {
        if (numberOne == null || numberTwo == null) {
            return false;
        }
        Long division = numberOne % numberTwo;
        if (!division.equals(DEFAULT_LONG_VALUE)) {
            return false;
        }
        return true;
    }

    /**
     * Lay pretty id cua controller hien tai
     *
     * @return
     */
    protected String getPrettyIdOfCurrentPage() {
        return PrettyContext.getCurrentInstance().getCurrentMapping().getId();
    }

    /**
     * reset lai khi F5
     *
     * @param command
     */
    public void executeCommand(String command) {
        StringBuilder bCommand = new StringBuilder(command);
        if (!DataUtil.isNullOrEmpty(command)) {
            if (!command.endsWith(")") && !command.endsWith(");")) {
                bCommand.append("();");
            }
            RequestContext.getCurrentInstance().execute(bCommand.toString());
        }
    }

    public void validateCapCha() throws LogicException, Exception {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        Captcha secretcaptcha = (Captcha) session.getAttribute(Captcha.NAME);
        if (DataUtil.isNullOrEmpty(this.capcha)) {
            executeCommand("resetCapcha()");
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "validate.capcha.require");
        }
        if (secretcaptcha != null && !secretcaptcha.isCorrect(this.capcha)) {
            executeCommand("resetCapcha()");
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "validate.capcha.validatorMessage");
        }
        this.capcha = "";
        executeCommand("resetCapcha()");
    }

    public String getCapcha() {
        return capcha;
    }

    public void setCapcha(String capcha) {
        this.capcha = capcha;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public long getLastRun() {
        return lastRun;
    }

    public void setLastRun(long lastRun) {
        this.lastRun = lastRun;
    }
}
