package com.viettel.ws.common.utils;

import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;

import java.io.Writer;

/**
 * Created by thiendn1 on 5/25/2015.
 */
public class CDATAWriter extends PrettyPrintWriter {
    public CDATAWriter(Writer writer) {
        super(writer);
    }

    protected void writeText(QuickWriter writer, String text) {
        if (text.indexOf('<') < 0) {
            writer.write(text);
        } else {
            writer.write("<![CDATA[");
            writer.write(text);
            writer.write("]]>");
        }
    }
}


