package com.viettel.bccs.inventory.validator;

import nl.captcha.Captcha;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpSession;

/**
 * Created by ThanhNT77 on 6/14/2015.
 */
@FacesValidator("capchaValidator")
public class CapchaValidator implements Validator {
    @Override
    public void validate(FacesContext context, UIComponent uiComponent, Object o) throws ValidatorException {
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        Captcha secretcaptcha = (Captcha) session.getAttribute(Captcha.NAME);

        if (secretcaptcha.isCorrect(o.toString()))
            return;
        // optional: clear field
        ((HtmlInputText) uiComponent).setSubmittedValue("");
        throw new ValidatorException(new FacesMessage());
    }
}
