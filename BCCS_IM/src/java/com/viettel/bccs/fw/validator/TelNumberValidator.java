package com.viettel.bccs.fw.validator;

import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.GetTextFromBundleHelper;
import org.apache.commons.lang3.StringUtils;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * @author nhannt34
 * @since 26/01/2016
 */
@FacesValidator("validator.telnum")
public class TelNumberValidator implements Validator {
    @Override
    public void validate(FacesContext context, UIComponent component,
                         Object value) throws ValidatorException {

        String telNum = DataUtil.safeToString(value);
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, GetTextFromBundleHelper.getText("common.validate.telnum.sum"), "");
        if (!DataUtil.isNullOrEmpty(telNum)) {
            if (StringUtils.length(telNum) > 20) {
                msg.setDetail(GetTextFromBundleHelper.getText("common.validate.telnum.detail.length"));
            }

            if (!StringUtils.isNumeric(telNum)) {
                msg.setDetail(GetTextFromBundleHelper.getText("common.validate.telnum.detail.number"));
            }
        }
        if (!DataUtil.isNullOrEmpty(msg.getDetail())) {
            throw new ValidatorException(msg);
        }
    }
}
