package com.viettel.fw.common.util;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by LamNV5 on 5/14/2015.
 */
public class SimpleLangMap implements KeyValueMap {
    private List<String> values = Lists.newArrayList();
    public static final String SPLITTER = "@@@";

    @Override
    public String getText(String key) {
        for (String value : values) {
            String[] arrs = value.split(SPLITTER);
            if (arrs.length == 2 && StringUtils.equalsIgnoreCase(arrs[0], key)) {
                return arrs[1];
            }
        }

        return key;
    }

    public void add(String key, String value) {
        values.add(key + SPLITTER + value);
    }
}
