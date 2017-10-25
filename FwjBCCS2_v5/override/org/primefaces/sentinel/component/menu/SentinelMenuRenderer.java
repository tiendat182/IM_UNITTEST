package org.primefaces.sentinel.component.menu;

import org.primefaces.component.api.AjaxSource;
import org.primefaces.component.api.UIOutcomeTarget;
import org.primefaces.component.menu.AbstractMenu;
import org.primefaces.component.menu.BaseMenuRenderer;
import org.primefaces.model.menu.MenuElement;
import org.primefaces.model.menu.MenuItem;
import org.primefaces.model.menu.Separator;
import org.primefaces.model.menu.Submenu;
import org.primefaces.util.ComponentUtils;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.http.Cookie;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/*
thiendn1:
- override padding-right:20px for right-menu-icon
- draggable and droppable for menu-item
 */
public class SentinelMenuRenderer
        extends BaseMenuRenderer {
    protected void encodeMarkup(FacesContext context, AbstractMenu abstractMenu)
            throws IOException {
        SentinelMenu menu = (SentinelMenu) abstractMenu;
        ResponseWriter writer = context.getResponseWriter();
        String clientId = menu.getClientId(context);
        String style = menu.getStyle();
        String styleClass = menu.getStyleClass();
        styleClass = "layout-menubar-container " + styleClass;

        writer.startElement("ul", menu);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute("class", styleClass, "styleClass");
        if (style != null) {
            writer.writeAttribute("style", style, "style");
        }
        if (menu.getElementsCount() > 0) {
            encodeElements(context, menu, menu.getElements(), 0);
        }
        writer.endElement("ul");
    }

    protected void encodeElements(FacesContext context, AbstractMenu menu, List<MenuElement> elements, int marginLevel)
            throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String menuClientId = menu.getClientId(context);
        Cookie cookie = (Cookie) context.getExternalContext().getRequestCookieMap().get("sentinel_menumode");
        String sentinelMenuMode = cookie==null?null:cookie.getValue();
        for (MenuElement element : elements) {
            if (element.isRendered()) {
                if ((element instanceof MenuItem)) {
                    MenuItem menuItem = (MenuItem) element;
                    String menuItemClientId = menuClientId + "_" + menuItem.getClientId();
                    String containerStyle = menuItem.getContainerStyle();
                    String containerStyleClass = null;
                    if (sentinelMenuMode != null && sentinelMenuMode.equals("slim")) {
                        containerStyleClass = menuItem.getContainerStyleClass() == null ? "sentinelMenu slim" : menuItem.getContainerStyleClass() + " sentinelMenu slim";

                    } else {
                        containerStyleClass = menuItem.getContainerStyleClass() == null ? "sentinelMenu large" : menuItem.getContainerStyleClass() + " sentinelMenu large";
                    }
                    writer.startElement("li", null);
                    writer.writeAttribute("id", menuItemClientId, null);
                    writer.writeAttribute("role", "menuitem", null);
                    if (containerStyle != null) {
                        writer.writeAttribute("style", containerStyle, null);
                    }
                    if (containerStyleClass != null) {
                        writer.writeAttribute("class", containerStyleClass, null);
                    }

                    encodeMenuItem(context, menu, menuItem, marginLevel);
                    writer.endElement("li");
                } else if ((element instanceof Submenu)) {
                    Submenu submenu = (Submenu) element;
                    String submenuClientId = menuClientId + "_" + submenu.getId();
                    String style = submenu.getStyle();
                    String styleClass = submenu.getStyleClass();

                    writer.startElement("li", null);
                    writer.writeAttribute("id", submenuClientId, null);
                    writer.writeAttribute("role", "menuitem", null);
                    if (style != null) {
                        writer.writeAttribute("style", style, null);
                    }
                    if (styleClass != null) {
                        writer.writeAttribute("class", styleClass, null);
                    }
                    encodeSubmenu(context, menu, submenu, marginLevel);

                    writer.endElement("li");
                } else if ((element instanceof Separator)) {
                    encodeSeparator(context, (Separator) element);
                }
            }
        }
    }

    protected void encodeMenuItem(FacesContext context, AbstractMenu menu, MenuItem menuitem, int marginLevel)
            throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String title = menuitem.getTitle();
        boolean disabled = menuitem.isDisabled();
        String style = menuitem.getStyle();
        writer.startElement("a", null);
        if (title != null) {
            writer.writeAttribute("title", title, null);
        }
        if (style != null) {
            writer.writeAttribute("style", style, null);
        }
        if (marginLevel > 0) {
            writer.writeAttribute("class", "marginLevel-" + marginLevel + " menuHref", null);
        }
        if (disabled) {
            writer.writeAttribute("href", "#", null);
            writer.writeAttribute("onclick", "return false;", null);
        } else {
            String onclick = menuitem.getOnclick();
            if (marginLevel == 0) {
                onclick = "Sentinel.toggleSubMenu(this);" + onclick;
            }
            if ((menuitem.getUrl() != null) || (menuitem.getOutcome() != null)) {
                String targetURL = getTargetURL(context, (UIOutcomeTarget) menuitem);
                writer.writeAttribute("href", targetURL, null);
                if (menuitem.getTarget() != null) {
                    writer.writeAttribute("target", menuitem.getTarget(), null);
                }
            } else {
                writer.writeAttribute("href", "#", null);

                UIComponent form = ComponentUtils.findParentForm(context, menu);
                if (form == null) {
                    throw new FacesException("MenuItem must be inside a form element");
                }
                String command;
                if (menuitem.isDynamic()) {
                    String menuClientId = menu.getClientId(context);
                    Map<String, List<String>> params = menuitem.getParams();
                    if (params == null) {
                        params = new LinkedHashMap();
                    }
                    List<String> idParams = new ArrayList();
                    idParams.add(menuitem.getId());
                    params.put(menuClientId + "_menuid", idParams);

                    command = menuitem.isAjax() ? buildAjaxRequest(context, menu, (AjaxSource) menuitem, form, params) : buildNonAjaxRequest(context, menu, form, menuClientId, params, true);
                } else {
                    command = menuitem.isAjax() ? buildAjaxRequest(context, (AjaxSource) menuitem, form) : buildNonAjaxRequest(context, (UIComponent) menuitem, form, ((UIComponent) menuitem).getClientId(context), true);
                }
                onclick = onclick + ";" + command;
            }
            if (onclick != null) {
                writer.writeAttribute("onclick", onclick, null);
            }
        }
        //thiendn1: add right menu icon
        writer.startElement("i", null);
        String script = "itemFavorite([{name: 'href', value: 'valueHref'}]); return false;".replace("valueHref",menuitem.getUrl());
        writer.writeAttribute("onclick", script, null);
        writer.writeAttribute("title", "Bookmark", null);
        writer.writeAttribute("style", "float:right;margin:-4px;display:none", null);
        writer.writeAttribute("class", "bookmark  icon-bookmark-1 Fs16", null);
        writer.endElement("i");
        encodeMenuItemContent(context, menu, menuitem);

        writer.endElement("a");

    }

    protected void encodeMenuItemContent(FacesContext context, AbstractMenu menu, MenuItem menuitem)
            throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String icon = menuitem.getIcon();
        Object value = menuitem.getValue();
        if (icon != null) {
            writer.startElement("i", null);
            writer.writeAttribute("class", icon + " yellow i", null);
            writer.endElement("i");
        }
        if (value != null) {
            writer.writeText(" " + value, "value");
        }
    }

    protected void encodeSubmenu(FacesContext context, AbstractMenu menu, Submenu submenu, int marginLevel)
            throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String icon = submenu.getIcon();
        String label = submenu.getLabel();

        writer.startElement("a", null);
        writer.writeAttribute("href", "#", null);
        writer.writeAttribute("onclick", "Sentinel.toggleSubMenu(this);return false;", null);
        if (marginLevel > 0) {
            writer.writeAttribute("class", "marginLevel-" + marginLevel, null);
        }
        writer.startElement("i", null);
        writer.writeAttribute("class", "icon-angle-down Fright", null);
        writer.writeAttribute("style", "float:right;padding: 0px 20px 0px 0px;", null);
        writer.endElement("i");
        if (icon != null) {
            writer.startElement("i", null);
            writer.writeAttribute("class", icon + " yellow i", null);
            writer.endElement("i");
        }
        if (label != null) {
            writer.writeText(label, null);
        }


        writer.endElement("a");
        if (submenu.getElementsCount() > 0) {
            writer.startElement("ul", null);
            writer.writeAttribute("class", "layout-menubar-submenu-container", null);
            writer.writeAttribute("role", "menu", null);
            encodeElements(context, menu, submenu.getElements(), ++marginLevel);
            writer.endElement("ul");
        }
    }

    protected void encodeScript(FacesContext context, AbstractMenu abstractMenu)
            throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("script", null);
        writer.writeAttribute("type", "text/javascript", null);
        writer.write("Sentinel.restoreMenuState();");
        writer.endElement("script");
    }
}