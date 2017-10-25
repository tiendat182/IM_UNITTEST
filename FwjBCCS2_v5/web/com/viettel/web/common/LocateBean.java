package com.viettel.web.common;

import com.google.common.collect.Lists;
import com.viettel.web.common.language.MultiResourceLang;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.text.Collator;
import java.util.*;

/**
 * @author thiendn1
 */
@Component
@ManagedBean
@Scope("session")
public class LocateBean implements Serializable {
    @Autowired
    LoginBean loginBean;
    @Autowired
    MenuBean menuBean;

    public static Collator collator;

    // Language
    private Locale selectedLocale;
    private List<Locale> locales = new ArrayList<>();

    @PostConstruct
    public void init() {
        selectedLocale = FacesContext.getCurrentInstance().getApplication().getDefaultLocale();
        collator = Collator.getInstance(selectedLocale);
        List<Locale> supportLocale = Lists.newArrayList(FacesContext.getCurrentInstance().getApplication().getSupportedLocales());
        locales.add(selectedLocale);
        locales.addAll(supportLocale);
    }


    public void changeLanguage() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map map = context.getExternalContext().getRequestParameterValuesMap();
        String language = ((String[]) map.get("language"))[0];
        String country = ((String[]) map.get("country"))[0];
        changeLanguage(language, country);
    }

    public void changeLanguage(String language, String country) {
        selectedLocale = new Locale(language, country);
        FacesContext.getCurrentInstance().getViewRoot().setLocale(selectedLocale);
        ResourceBundle bundle = new MultiResourceLang(FacesContext.getCurrentInstance().getViewRoot().getLocale());
        Enumeration<String> enm = bundle.getKeys();
        List<String> lstKey = new ArrayList<>();
        while (enm.hasMoreElements()) {
            lstKey.add(enm.nextElement());
        }
        loginBean.createAuthoCache(bundle, lstKey);
        menuBean.init(selectedLocale);
        collator = Collator.getInstance(selectedLocale);
    }

    public List<Locale> getLocales() {
        return locales;
    }

    public void setLocales(List<Locale> locales) {
        this.locales = locales;
    }

    public Locale getSelectedLocale() {
        return selectedLocale;
    }

    public void setSelectedLocale(Locale selectedLocale) {
        this.selectedLocale = selectedLocale;
    }


}


