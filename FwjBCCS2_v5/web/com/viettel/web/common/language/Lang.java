package com.viettel.web.common.language;

import com.viettel.fw.bundle.UTF8ResourceBundle;

import javax.faces.context.FacesContext;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

@Deprecated
public class Lang extends ResourceBundle {

    public Lang() {
        String bundleName = "com.viettel.language.lang";//ResourceBundle.getBundle("cas").getString("language_folder");
        setParent(new UTF8ResourceBundle(bundleName, FacesContext.getCurrentInstance().getViewRoot().getLocale()));

    }

    public Lang(Locale newLocal) {
        String bundleName = "com.viettel.language.lang";//ResourceBundle.getBundle("cas").getString("language_folder");
        setParent(new UTF8ResourceBundle(bundleName, newLocal));

    }

    @Override
    protected Object handleGetObject(String key) {
        return parent.getObject(key);
    }

    @Override
    public Enumeration<String> getKeys() {
        return parent.getKeys();
    }

}