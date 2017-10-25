package com.viettel.web.log;

import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.data.ErrorType;
import com.viettel.fw.common.data.MsgType;
import com.viettel.fw.common.util.Const;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.log.MessageLog;
import org.apache.cxf.binding.soap.SoapFault;

import java.io.FileNotFoundException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.util.concurrent.TimeoutException;

/**
 * Created by tuyennt17 on 6/20/2016.
 */
public class LoggingUtils {
    public static String getErrorCode(Throwable e, String idError) {
        return getMsgType(e, idError).toString();
    }

    public static MessageLog getMsgType(Throwable e, String idError) {
        MessageLog loggingMessageDTO = new MessageLog();
        if (e instanceof Exception) {
            if (e instanceof LogicException) {
                String rootCause = ((LogicException) e).getErrorCode();
                loggingMessageDTO = new MessageLog(MsgType.E, ErrorType.U, rootCause, idError);
            } else if (e instanceof NullPointerException) {
                loggingMessageDTO = new MessageLog(MsgType.E, ErrorType.S, ErrorCode.ERROR_STANDARD.ERROR_NULL_POINTER_EX, idError);
            } else if (e instanceof ArrayIndexOutOfBoundsException) {
                loggingMessageDTO = new MessageLog(MsgType.E, ErrorType.S, ErrorCode.ERROR_STANDARD.ERROR_ARRAY_OUT_OF_BOUNDS_EX, idError);
            } else if (e instanceof ClassNotFoundException) {
                loggingMessageDTO = new MessageLog(MsgType.E, ErrorType.S, ErrorCode.ERROR_STANDARD.ERROR_CLASS_NOT_FOUND, idError);
            } else if (e instanceof NumberFormatException) {
                loggingMessageDTO = new MessageLog(MsgType.E, ErrorType.S, ErrorCode.ERROR_STANDARD.ERROR_NUMBER_FORMAT_EX, idError);
            } else if (e instanceof ParseException) {
                loggingMessageDTO = new MessageLog(MsgType.E, ErrorType.S, ErrorCode.ERROR_STANDARD.ERROR_PARSE_EX, idError);
            } else if (e instanceof ClassCastException) {
                loggingMessageDTO = new MessageLog(MsgType.E, ErrorType.S, ErrorCode.ERROR_STANDARD.ERROR_CLASS_NOT_FOUND, idError);
            } else if (e instanceof TimeoutException) {
                loggingMessageDTO = new MessageLog(MsgType.E, ErrorType.S, ErrorCode.ERROR_CONNNECT.ERROR_TIMOUT_EX, idError);
            } else if (e instanceof SocketTimeoutException) {
                loggingMessageDTO = new MessageLog(MsgType.E, ErrorType.S, ErrorCode.ERROR_CONNNECT.ERROR_TIMOUT_EX, idError);
            } else if (e instanceof ConnectException || e instanceof SoapFault) {
                loggingMessageDTO = new MessageLog(MsgType.E, ErrorType.S, ErrorCode.ERROR_CONNNECT.ERROR_CONNECT_EX, idError);
            } else if (e instanceof FileNotFoundException) {
                loggingMessageDTO = new MessageLog(MsgType.E, ErrorType.S, ErrorCode.ERROR_HANDLE_FILE.ERROR_FILE_NOT_FOUND_EX, idError);
            } else {
                loggingMessageDTO = new MessageLog(MsgType.E, ErrorType.S, ErrorCode.ERROR_NOT_DEFINE, idError);
            }
        } else if (e instanceof Error) {
            if (e instanceof VirtualMachineError) {
                loggingMessageDTO = new MessageLog(MsgType.E, ErrorType.S, Const.ERROR_CODE_NUMBER.ERROR_VIR_MACHINE_ERROR, idError);
            } else if (e instanceof AssertionError) {
                loggingMessageDTO = new MessageLog(MsgType.E, ErrorType.S, Const.ERROR_CODE_NUMBER.ERROR_ASSERT_ERROR, idError);
            }
        }
        return loggingMessageDTO;
    }
}
