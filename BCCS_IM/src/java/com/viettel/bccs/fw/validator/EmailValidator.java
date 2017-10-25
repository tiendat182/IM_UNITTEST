package com.viettel.bccs.fw.validator;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.GetTextFromBundleHelper;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author nhannt34
 * @since 26/01/2016
 */
@FacesValidator("validator.email")
public class EmailValidator implements Validator {
    @Override
    public void validate(FacesContext context, UIComponent component,
                         Object value) throws ValidatorException {

        String email = DataUtil.safeToString(value);
        if (!DataUtil.isNullOrEmpty(email)) {
            if (email.length() > 200) {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, GetTextFromBundleHelper.getText("common.validate.email.detail.length"), GetTextFromBundleHelper.getText("common.validate.email.detail.length")));
            }
            Pattern pattern = Pattern.compile(Const.EMAIL_PATTERN);
            Matcher matcher = pattern.matcher(email);
            if (!matcher.matches()) {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, GetTextFromBundleHelper.getText("common.validate.email.sum"), GetTextFromBundleHelper.getText("common.validate.email.sum")));
            }
        }
    }
}
