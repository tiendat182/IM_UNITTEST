package com.viettel.web.common.language;


import com.viettel.fw.bundle.MultiResourceBundle;

import javax.faces.context.FacesContext;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

public class MultiResourceLang extends ResourceBundle {
    private final String BUNDLE_ROOT = "com.viettel.language.root";
    private MultiResourceBundle multiResourceBundle;

    public MultiResourceLang() {
        multiResourceBundle = new MultiResourceBundle(BUNDLE_ROOT, FacesContext.getCurrentInstance().getViewRoot().getLocale());
        setParent(multiResourceBundle);

    }

    public MultiResourceLang(Locale newLocal) {
        multiResourceBundle = new MultiResourceBundle(BUNDLE_ROOT, newLocal);
        setParent(multiResourceBundle);

    }

    @Override
    protected Object handleGetObject(String key) {
        return multiResourceBundle.getObject(key);
    }

    @Override
    public Enumeration<String> getKeys() {
        return multiResourceBundle.getKeys();
    }

}