package com.viettel.fw.logging;

import com.viettel.fw.Exception.LogicException;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.log4j.Logger;
import org.springframework.aop.interceptor.CustomizableTraceInterceptor;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Extends {@link CustomizableTraceInterceptor} to provide custom logging levels
 */
public class TraceInterceptor extends CustomizableTraceInterceptor {

    private static final long serialVersionUID = 287162721460370957L;
    protected static Logger logger4J = Logger.getLogger("performanceLogger");
    protected static Logger logger4Jerr = Logger.getLogger("errorLogger");
    protected static Logger loggerBusinessErr = Logger.getLogger("errBusinessLogger");

    private static final Pattern PATTERN = Pattern.compile("\\$\\[\\p{Alpha}+\\]");

    private int exceedQtyWarring;
    private int exceedTimeWarring;
    private int exceedExtraWarring = 60000;
    private String exceedQtyWarringMsg;
    private String exceedTimeWarringMsg;
    private static ThreadLocal callIdLocal = new ThreadLocal<String>();

    public static void resetCallId() {
        String callId = String.valueOf(System.currentTimeMillis());
        callIdLocal.set(callId);
    }

    @Override
    protected void writeToLog(Log logger, String message, Throwable ex) {
        if (ex != null) {
            message += "|" + ex.getMessage();
            if (ex instanceof LogicException) {
                loggerBusinessErr.error(message, ex);
            } else {
                logger4Jerr.error(message, ex);
            }
        } else {
            message += "|";
        }

        if (!message.contains(".Proxy")) {
            logger4J.info(message);
        }
    }

    @Override
    protected boolean isInterceptorEnabled(MethodInvocation invocation,
                                           Log logger) {
        return true;
    }

    protected String replacePlaceholders(String message, MethodInvocation methodInvocation,
                                         Object returnValue, Throwable throwable, long invocationTime) {

        Matcher matcher = PATTERN.matcher(message);

        StringBuffer output = new StringBuffer();
        while (matcher.find()) {
            String match = matcher.group();
            if (PLACEHOLDER_METHOD_NAME.equals(match)) {
                matcher.appendReplacement(output, Matcher.quoteReplacement(methodInvocation.getMethod().getName()));
            } else if (PLACEHOLDER_TARGET_CLASS_NAME.equals(match)) {
                String className = getClassForLogging(methodInvocation.getThis()).getName();
                matcher.appendReplacement(output, Matcher.quoteReplacement(className));
            } else if (PLACEHOLDER_TARGET_CLASS_SHORT_NAME.equals(match)) {
                String shortName = ClassUtils.getShortName(getClassForLogging(methodInvocation.getThis()));
                matcher.appendReplacement(output, Matcher.quoteReplacement(shortName));
            } else if (PLACEHOLDER_ARGUMENTS.equals(match)) {
                matcher.appendReplacement(output,
                        Matcher.quoteReplacement(StringUtils.arrayToCommaDelimitedString(methodInvocation.getArguments())));
            } else if (PLACEHOLDER_ARGUMENT_TYPES.equals(match)) {
                appendArgumentTypes(methodInvocation, matcher, output);
            } else if (PLACEHOLDER_RETURN_VALUE.equals(match)) {
                appendReturnValue(methodInvocation, matcher, output, returnValue);
            } else if (throwable != null && PLACEHOLDER_EXCEPTION.equals(match)) {
                matcher.appendReplacement(output, Matcher.quoteReplacement(throwable.toString()));
            } else if (PLACEHOLDER_INVOCATION_TIME.equals(match)) {
                matcher.appendReplacement(output, Long.toString(invocationTime));
            } else {
                // Should not happen since placeholders are checked earlier.
                throw new IllegalArgumentException("Unknown placeholder [" + match + "]");
            }
        }
        matcher.appendTail(output);

        if (invocationTime > exceedTimeWarring) {
            if (invocationTime > exceedExtraWarring) {
                output.append(exceedTimeWarringMsg + "-EXTRA");
            } else {
                output.append(exceedTimeWarringMsg);
            }
        } else {
            output.append("|");
        }

        return output.toString();
    }

    /**
     * Adds the {@code String} representation of the method return value
     * to the supplied {@code StringBuffer}. Correctly handles
     * {@code null} and {@code void} results.
     *
     * @param methodInvocation the {@code MethodInvocation} that returned the value
     * @param matcher          the {@code Matcher} containing the matched placeholder
     * @param output           the {@code StringBuffer} to write output to
     * @param returnValue      the value returned by the method invocation.
     */
    private void appendReturnValue(
            MethodInvocation methodInvocation, Matcher matcher, StringBuffer output, Object returnValue) {

        if (methodInvocation.getMethod().getReturnType() == void.class) {
            matcher.appendReplacement(output, "|"); //"void"
        } else if (returnValue == null) {
            matcher.appendReplacement(output, "|"); //null
        } else {
            //Chi fill so luong ban ghi khi return type la list
            if (methodInvocation.getMethod().getReturnType() == List.class) {
                int result = ((List) returnValue).size();
                String returnMsg = result + ((result <= exceedQtyWarring) ? "|" : exceedQtyWarringMsg);
                matcher.appendReplacement(output, Matcher.quoteReplacement(returnMsg));
            } else {
                matcher.appendReplacement(output, Matcher.quoteReplacement("|")); //returnValue.toString()
            }
        }
    }

    /**
     * Adds a comma-separated list of the short {@code Class} names of the
     * method argument types to the output. For example, if a method has signature
     * {@code put(java.lang.String, java.lang.Object)} then the value returned
     * will be {@code String, Object}.
     *
     * @param methodInvocation the {@code MethodInvocation} being logged.
     *                         Arguments will be retrieved from the corresponding {@code Method}.
     * @param matcher          the {@code Matcher} containing the state of the output
     * @param output           the {@code StringBuffer} containing the output
     */
    private void appendArgumentTypes(MethodInvocation methodInvocation, Matcher matcher, StringBuffer output) {
        Class<?>[] argumentTypes = methodInvocation.getMethod().getParameterTypes();
        String[] argumentTypeShortNames = new String[argumentTypes.length];
        for (int i = 0; i < argumentTypeShortNames.length; i++) {
            argumentTypeShortNames[i] = ClassUtils.getShortName(argumentTypes[i]);
        }
        matcher.appendReplacement(output,
                Matcher.quoteReplacement(StringUtils.arrayToCommaDelimitedString(argumentTypeShortNames)));
    }

    public int getExceedQtyWarring() {
        return exceedQtyWarring;
    }

    public void setExceedQtyWarring(int exceedQtyWarring) {
        this.exceedQtyWarring = exceedQtyWarring;
    }

    public int getExceedTimeWarring() {
        return exceedTimeWarring;
    }

    public void setExceedTimeWarring(int exceedTimeWarring) {
        this.exceedTimeWarring = exceedTimeWarring;
    }

    public String getExceedQtyWarringMsg() {
        return exceedQtyWarringMsg;
    }

    public void setExceedQtyWarringMsg(String exceedQtyWarringMsg) {
        this.exceedQtyWarringMsg = exceedQtyWarringMsg;
    }

    public String getExceedTimeWarringMsg() {
        return exceedTimeWarringMsg;
    }

    public void setExceedTimeWarringMsg(String exceedTimeWarringMsg) {
        this.exceedTimeWarringMsg = exceedTimeWarringMsg;
    }

    public int getExceedExtraWarring() {
        return exceedExtraWarring;
    }

    public void setExceedExtraWarring(int exceedExtraWarring) {
        this.exceedExtraWarring = exceedExtraWarring;
    }
}