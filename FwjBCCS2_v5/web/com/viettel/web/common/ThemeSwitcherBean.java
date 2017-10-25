package com.viettel.web.common;

import org.primefaces.component.themeswitcher.ThemeSwitcher;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

/*import org.primefaces.examples.domain.Theme;*/

/**
 * thiendn1 sua
 */
@Component
@ManagedBean
public class ThemeSwitcherBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Map<String, String> themes;
    private String theme = "sentinel";

    public Map<String, String> getThemes() {
        return themes;
    }

    @PostConstruct
    public void init() {
        themes = new TreeMap<String, String>();
        themes.put("Galaxy", "galaxy");
        themes.put("Afterdark", "afterdark");
        themes.put("Afternoon", "afternoon");
        themes.put("Afterwork", "afterwork");
        themes.put("Aristo", "aristo");
        themes.put("Black-Tie", "black-tie");
        themes.put("Blitzer", "blitzer");
        themes.put("Bluesky", "bluesky");
        themes.put("Bootstrap", "bootstrap");
        themes.put("Casablanca", "casablanca");
        themes.put("Cupertino", "cupertino");
        themes.put("Cruze", "cruze");
        themes.put("Dark-Hive", "dark-hive");
        themes.put("Delta", "delta");
        themes.put("Dot-Luv", "dot-luv");
        themes.put("Eggplant", "eggplant");
        themes.put("Excite-Bike", "excite-bike");
        themes.put("Flick", "flick");
        themes.put("Glass-X", "glass-x");
        themes.put("Home", "home");
        themes.put("Hot-Sneaks", "hot-sneaks");
        themes.put("Humanity", "humanity");
        themes.put("Le-Frog", "le-frog");
        themes.put("Midnight", "midnight");
        themes.put("Mint-Choc", "mint-choc");
        themes.put("Overcast", "overcast");
        themes.put("Pepper-Grinder", "pepper-grinder");
        themes.put("Redmond", "redmond");
        themes.put("Rocket", "rocket");
        themes.put("Sam", "sam");
        themes.put("Smoothness", "smoothness");
        themes.put("South-Street", "south-street");
        themes.put("Start", "start");
        themes.put("Sunny", "sunny");
        themes.put("Swanky-Purse", "swanky-purse");
        themes.put("Trontastic", "trontastic");
        themes.put("UI-Darkness", "ui-darkness");
        themes.put("UI-Lightness", "ui-lightness");
        themes.put("Vader", "vader");
        themes.put("Sentinel", "sentinel");
        themes.put("Adamantium", "adamantium");

    }

    public void saveTheme(AjaxBehaviorEvent ajax) {
        setTheme((String) ((ThemeSwitcher) ajax.getSource()).getValue());
        //System.out.print("------------------------------ "+(String) ((ThemeSwitcher)ajax.getSource()).getValue());
    }

    public void changeTheme() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map map = context.getExternalContext().getRequestParameterValuesMap();
        theme = ((String[]) map.get("theme"))[0];
        setTheme(theme);
    }
    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }
}
