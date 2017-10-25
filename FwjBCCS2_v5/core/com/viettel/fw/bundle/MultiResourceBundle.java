package com.viettel.fw.bundle;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Created by thiendn1 on 9/3/2016.
 */
public class MultiResourceBundle extends ResourceBundle {

    private Properties properties;
    private static Locale locale;


    public MultiResourceBundle(String baseName, Locale locale) {
        this.locale = locale;
        setParent(ResourceBundle.getBundle(baseName, locale, new MultiResourceBundleControl(baseName)));
    }

    protected MultiResourceBundle(Properties properties) {
        this.properties = properties;
    }

    @Override
    protected Object handleGetObject(String key) {
        return properties != null ? properties.get(key) : parent.getObject(key);
    }

    @Override
    public Enumeration<String> getKeys() {
        return properties != null ? (Enumeration<String>) properties.propertyNames() : parent.getKeys();
    }

    public static class MultiResourceBundleControl extends Control {
        String baseName;

        public MultiResourceBundleControl(String baseName) {
            this.baseName = baseName;
        }


        @Override
        public ResourceBundle newBundle(
                String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
                throws IllegalAccessException, InstantiationException, IOException {
            Properties properties = null;
            String include = null;
            if (baseName.contains(this.baseName)) {
                properties = load(this.baseName, Locale.ROOT, loader, reload);
            } else {
                locale = MultiResourceBundle.locale;
                properties = load(baseName, locale, loader, reload);
            }
            include = properties.getProperty("includeResource");
            if (include != null) {
                for (String includeBaseName : include.split("\\s*,\\s*")) {
                    Properties includeProperties = load(includeBaseName, locale, loader, reload);
                    if (includeProperties != null) {
                        properties.putAll(includeProperties);
                    }
                }
            }
            return new MultiResourceBundle(properties);
        }

        private Properties load(String baseName, Locale locale, ClassLoader loader, boolean reload) throws IOException {
            String bundleName = toBundleName(baseName, locale);
            String resourceName = toResourceName(bundleName, "properties");
            InputStream stream = null;
            if (reload) {
                URL url = loader.getResource(resourceName);
                if (url != null) {
                    URLConnection connection = url.openConnection();
                    if (connection != null) {
                        connection.setUseCaches(false);
                        stream = connection.getInputStream();
                    }
                }
            } else {
                stream = loader.getResourceAsStream(resourceName);
            }
            if (stream != null) {
                Properties properties = new Properties();
                properties.load(new InputStreamReader(stream, "UTF-8"));
                return properties;
            }
            return null;
        }
    }


}