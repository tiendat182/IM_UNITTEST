package com.viettel.bccs.fw.converter;

import com.viettel.fw.common.util.GetTextFromBundleHelper;
import org.omnifaces.util.Faces;

import javax.faces.convert.FacesConverter;
import javax.faces.convert.NumberConverter;

@FacesConverter("currencyFormat")
public class DefaultCurrencyConverter extends NumberConverter {

    public DefaultCurrencyConverter() {
        setType("currency");
//        setCurrencySymbol(getText("common.currency.code"));
        setCurrencySymbol("");
        setMaxFractionDigits(Integer.valueOf(GetTextFromBundleHelper.getText("common.currency.maxFractionDigit")));
//        setLocale(Locale.ENGLISH);
        setLocale(Faces.getLocale());
    }
}