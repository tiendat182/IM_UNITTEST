//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.sf.jasperreports.engine.util;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.fill.JRMeasuredText;
import net.sf.jasperreports.engine.fill.JRTextMeasurer;
import net.sf.jasperreports.engine.fill.JRTextMeasurerFactory;

import java.util.ArrayList;
import java.util.List;
/*
20151217: thiendn1: them phuong thuc de danh gia do dai cua text truoc khi in report
 */
public final class JRTextMeasurerUtilExtends {
    private final JasperReportsContext jasperReportsContext;
    private final JRStyledTextAttributeSelector noBackcolorSelector;
    private final JRStyledTextUtil styledTextUtil;
    public static final String PROPERTY_TEXT_MEASURER_FACTORY = "net.sf.jasperreports.text.measurer.factory";
    private static final JRSingletonCache<JRTextMeasurerFactory> cache = new JRSingletonCache(JRTextMeasurerFactory.class);

    private JRTextMeasurerUtilExtends(JasperReportsContext jasperReportsContext) {
        this.jasperReportsContext = jasperReportsContext;
        this.noBackcolorSelector = JRStyledTextAttributeSelector.getNoBackcolorSelector(jasperReportsContext);
        this.styledTextUtil = JRStyledTextUtil.getInstance(jasperReportsContext);
    }

    public static JRTextMeasurerUtilExtends getInstance(JasperReportsContext jasperReportsContext) {
        return new JRTextMeasurerUtilExtends(jasperReportsContext);
    }

    public JRTextMeasurer createTextMeasurer(JRCommonText text) {
        JRPropertiesHolder propertiesHolder = text instanceof JRPropertiesHolder?(JRPropertiesHolder)text:null;
        return this.createTextMeasurer(text, propertiesHolder);
    }

    public JRTextMeasurer createTextMeasurer(JRCommonText text, JRPropertiesHolder propertiesHolder) {
        net.sf.jasperreports.engine.util.JRTextMeasurerFactory factory = this.getFactory(propertiesHolder);
        return factory.createMeasurer(this.jasperReportsContext, text);
    }

    /** @deprecated */
    public JRTextMeasurerFactory getTextMeasurerFactory(JRPropertiesHolder propertiesHolder) {
        return this.getFactory(propertiesHolder);
    }

    public net.sf.jasperreports.engine.util.JRTextMeasurerFactory getFactory(JRPropertiesHolder propertiesHolder) {
        String factoryClass = this.getTextMeasurerFactoryClass(propertiesHolder);

        try {
            JRTextMeasurerFactory e = (JRTextMeasurerFactory)cache.getCachedInstance(factoryClass);
            return (net.sf.jasperreports.engine.util.JRTextMeasurerFactory)(e instanceof net.sf.jasperreports.engine.util.JRTextMeasurerFactory?(net.sf.jasperreports.engine.util.JRTextMeasurerFactory)e:new WrappingTextMeasurerFactory(e));
        } catch (JRException var4) {
            throw new JRRuntimeException(var4);
        }
    }

    protected String getTextMeasurerFactoryClass(JRPropertiesHolder propertiesHolder) {
        String factory = JRPropertiesUtil.getInstance(this.jasperReportsContext).getProperty(propertiesHolder, "net.sf.jasperreports.text.measurer.factory");
        String factoryClassProperty = "net.sf.jasperreports.text.measurer.factory." + factory;
        String factoryClass = JRPropertiesUtil.getInstance(this.jasperReportsContext).getProperty(propertiesHolder, factoryClassProperty);
        if(factoryClass == null) {
            factoryClass = factory;
        }

        return factoryClass;
    }

    public void measureTextElement(JRPrintText printText) {

        String text = this.styledTextUtil.getTruncatedText(printText);
        JRTextMeasurer textMeasurer = this.createTextMeasurer(printText);
        if(text == null) {
            text = "";
        }

        JRStyledText styledText = JRStyledTextParser.getInstance().getStyledText(this.noBackcolorSelector.getStyledTextAttributes(printText), text, "styled".equals(printText.getMarkup()), JRStyledTextAttributeSelector.getTextLocale(printText));
        JRMeasuredText measuredText = textMeasurer.measure(styledText, 0, 0, false);
        printText.setTextHeight(measuredText.getTextHeight() < (float)printText.getHeight()?measuredText.getTextHeight():(float)printText.getHeight());
        printText.setLeadingOffset(measuredText.getLeadingOffset());
        printText.setLineSpacingFactor(measuredText.getLineSpacingFactor());
        int textEnd = measuredText.getTextOffset();
        String printedText;
        if("styled".equals(printText.getMarkup())) {
            printedText = JRStyledTextParser.getInstance().write(styledText, 0, textEnd);
        } else {
            printedText = text.substring(0, textEnd);
        }

        printText.setText(printedText);
    }

    //thiendn1: phuong thuc ghi de
    public List<String> measureText(JRPrintText printText) {
        String text = this.styledTextUtil.getTruncatedText(printText);
        List<String> arrayText = new ArrayList<>();
        JRTextMeasurer textMeasurer = this.createTextMeasurer(printText);

        while(text.length()>0){

            JRStyledText styledText = JRStyledTextParser.getInstance().getStyledText(this.noBackcolorSelector.getStyledTextAttributes(printText), text, "styled".equals(printText.getMarkup()), JRStyledTextAttributeSelector.getTextLocale(printText));
            JRMeasuredText measuredText = textMeasurer.measure(styledText, 0, 0, false);
            int textEnd = measuredText.getTextOffset();
            if(textEnd==text.length()){
                arrayText.add(text);
                return arrayText;
            }
            ((JRStyledText.Run)styledText.getRuns().get(0)).endIndex = textEnd;
            String printedText;
            if("styled".equals(printText.getMarkup())) {
                printedText = JRStyledTextParser.getInstance().write(styledText, 0, textEnd);
            } else {
                printedText = text.substring(0, textEnd);
            }

            arrayText.add(text.substring(textEnd,text.trim().length()));
            text = printedText.trim();

            printText.setTextHeight(measuredText.getTextHeight() < (float)printText.getHeight()?measuredText.getTextHeight():(float)printText.getHeight());
            printText.setTextHeight(printText.getTextHeight()-printText.getFontSize());
            printText.setHeight((int)printText.getTextHeight());

        }
        if(arrayText.isEmpty()){
            arrayText.add(text);
        }
        return arrayText;
    }

    /** @deprecated */
    public static class WrappingTextMeasurerFactory implements net.sf.jasperreports.engine.util.JRTextMeasurerFactory {
        private JRTextMeasurerFactory factory;

        public WrappingTextMeasurerFactory(JRTextMeasurerFactory factory) {
            this.factory = factory;
        }

        public JRTextMeasurer createMeasurer(JRCommonText text) {
            return this.factory.createMeasurer(text);
        }

        public JRTextMeasurer createMeasurer(JasperReportsContext jasperReportsContext, JRCommonText text) {
            return this.factory.createMeasurer(text);
        }
    }
}
