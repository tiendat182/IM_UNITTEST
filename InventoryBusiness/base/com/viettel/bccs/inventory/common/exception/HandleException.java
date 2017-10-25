package com.viettel.bccs.inventory.common.exception;

import com.google.common.collect.Lists;
import com.viettel.fw.Exception.LogicException;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created by thanhnt77 on 24/8/2016.
 */
public class HandleException extends LogicException {

    private static final Logger logger = Logger.getLogger(HandleException.class);

    private List<String> lsErrorData = Lists.newArrayList();

    public HandleException() {
        super();
    }

    public HandleException(String errorCode, String keyMsg, List<String> lsErrorData, String...params) {
        super(errorCode, keyMsg, params);
        this.lsErrorData = lsErrorData;
    }

    public List<String> getLsErrorData() {
        return lsErrorData;
    }

    public void setLsErrorData(List<String> lsErrorData) {
        this.lsErrorData = lsErrorData;
    }
}
