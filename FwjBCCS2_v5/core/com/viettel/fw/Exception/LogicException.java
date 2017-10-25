/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.fw.Exception;

import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.GetTextFromBundleHelper;
import com.viettel.fw.dto.BaseMessage;
import org.apache.log4j.Logger;

import java.text.MessageFormat;

/**
 * @author lamnv5
 */
public class LogicException extends Exception {
    private static final Logger logger = Logger.getLogger(LogicException.class);

    private String errorCode;
    private String description;
    private String keyMsg;

    private String outputObject;
    private String outputObject2;
    //MinhNH
    private String system;//mo ta he thong bi loi
    private String elementStyleClass;// Thuoc tinh dung de focus vao
    private String cssSelectorForFocus;
    private BaseMessage baseMessage;
    private String[] paramsMsg;

    public LogicException() {
        super();
    }

    @Deprecated
    public LogicException(String keyMsg) {
        super();
        this.keyMsg = keyMsg;
        if (keyMsg != null && !keyMsg.equals("")) {
            this.description = GetTextFromBundleHelper.getText(keyMsg);
        } else {
            this.description = null;
        }
    }


    public LogicException(BaseMessage baseMessage) {
        super();
        if (baseMessage != null) {
            this.baseMessage = baseMessage;
            this.errorCode = baseMessage.getErrorCode();
            this.keyMsg = baseMessage.getKeyMsg();
            this.paramsMsg = baseMessage.getParamsMsg();
            this.description = baseMessage.getDescription();
        }
    }

    public LogicException(String errorCode, String keyMsg) {
        super();
        this.errorCode = errorCode;
        this.keyMsg = keyMsg;
        if (!DataUtil.isNullOrEmpty(keyMsg)) {
            this.description = GetTextFromBundleHelper.getText(keyMsg);
        } else {
            this.description = null;
        }
    }

    public LogicException(String errorCode, String keyMsg, BaseMessage baseMessage) {
        super();
        this.errorCode = errorCode;
        this.keyMsg = keyMsg;
        this.baseMessage = baseMessage;
        if (!DataUtil.isNullOrEmpty(keyMsg)) {
            this.description = GetTextFromBundleHelper.getText(keyMsg);
        } else {
            this.description = null;
        }
    }

    public LogicException(String errorCode, String keyMsg, Throwable cause, Object... params) {
        super(cause);
        this.errorCode = errorCode;
        this.keyMsg = keyMsg;
        if (!DataUtil.isNullOrEmpty(keyMsg)) {
            this.description = GetTextFromBundleHelper.getText(keyMsg);
        } else {
            this.description = null;
        }
    }

    public LogicException(String errorCode, String keyMsg, Object... params) {
        super();
        this.errorCode = errorCode;
        this.keyMsg = keyMsg;
        this.paramsMsg = convertParamsToStringArray(params);
        if (!DataUtil.isNullOrEmpty(keyMsg)) {
            try {
                this.description = MessageFormat.format(GetTextFromBundleHelper.getText(keyMsg), params);
            } catch (Exception ex) {
                logger.error(ex.getMessage());
            }
        } else {
            this.description = null;
        }
    }


    @Override
    public String getMessage() {
        return MessageFormat.format("{0}:{1}", errorCode, description);
    }

    @Override
    public String toString() {
        return MessageFormat.format("{0}:{1}", errorCode, description);
    }

    private String[] convertParamsToStringArray(Object... params) {
        if (DataUtil.isNullOrEmpty(params)) return null;
        String[] lst = new String[params.length];
        for (int i = 0; i < params.length; i++) {
            lst[i] = DataUtil.safeToString(params[i]);
        }
        return lst;
    }


    public String toStringFull() {
        return "LogicException{" +
                "errorCode='" + errorCode + '\'' +
                ", description='" + description + '\'' +
                ", keyMsg='" + keyMsg + '\'' +
                ", outputObject='" + outputObject + '\'' +
                ", outputObject2='" + outputObject2 + '\'' +
                ", system='" + system + '\'' +
                ", elementStyleClass='" + elementStyleClass + '\'' +
                ", cssSelectorForFocus='" + cssSelectorForFocus + '\'' +
                ", baseMessage=" + baseMessage +
                '}';

    }

    public String[] getParamsMsg() {
        return paramsMsg;
    }

    public void setParamsMsg(String[] paramsMsg) {
        this.paramsMsg = paramsMsg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getDescription() {
        if (description != null && !description.isEmpty()) return description;
        return null;
    }

    public String getKeyMsg() {
        return keyMsg;
    }

    public void setKeyMsg(String keyMsg) {
        this.keyMsg = keyMsg;
    }

    public String getOutputObject() {
        return outputObject;
    }

    public void setOutputObject(String outputObject) {
        this.outputObject = outputObject;
    }

    public String getOutputObject2() {
        return outputObject2;
    }

    public void setOutputObject2(String outputObject2) {
        this.outputObject2 = outputObject2;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getElementStyleClass() {
        return elementStyleClass;
    }

    public String getCssSelectorForFocus() {
        return cssSelectorForFocus;
    }

    public void setCssSelectorForFocus(String cssSelectorForFocus) {
        this.cssSelectorForFocus = cssSelectorForFocus;
    }

    public void setElementStyleClass(String elementStyleClass) {
        this.elementStyleClass = elementStyleClass;
    }

    public BaseMessage getBaseMessage() {
        return baseMessage;
    }

    public void setBaseMessage(BaseMessage baseMessage) {
        this.baseMessage = baseMessage;
    }


}
