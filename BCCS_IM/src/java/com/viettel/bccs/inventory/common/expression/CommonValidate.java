package com.viettel.bccs.inventory.common.expression;

import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.BundleUtil;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.GetTextFromBundleHelper;
import org.apache.commons.lang.StringUtils;
import org.primefaces.event.FileUploadEvent;

import javax.faces.application.FacesMessage;
import java.io.IOException;

/**
 * Created by tuydv1 on 12/24/2015.
 */
public class CommonValidate {
    public static void validatePreImport(FileUploadEvent event) throws IOException, LogicException, Exception {
        if (event.getFile() == null) {
            BundleUtil.addFacesMsg(FacesMessage.SEVERITY_FATAL, "", "common.msg.upload", "common.msg.fail", "");
        }
        String fileExtension = StringUtils.substringAfterLast(event.getFile().getFileName(), ".");
        if (DataUtil.isNullOrEmpty(fileExtension)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT,"common.error.happened");
        } else {
            if (!StringUtils.equals(fileExtension, "xls") && !StringUtils.equals(fileExtension, "xlsx")) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT,"common.file.allow.excel");
            }
        }

        if (event.getFile().getSize() > Long.valueOf(GetTextFromBundleHelper.getText("common.file.allow.excel.max.size"))) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT,"common.file.allow.excel.max.size.desc");
        }
        if (DataUtil.isNullOrZero(event.getFile().getSize())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT,"common.file.allow.excel.min.size.desc");
        }
    }
}
