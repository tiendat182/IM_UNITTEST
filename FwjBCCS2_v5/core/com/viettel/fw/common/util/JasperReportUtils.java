/*
 * Copyright 2014 Viettel Software. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.fw.common.util;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;

import com.viettel.fw.dto.PdfDTO;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.OrientationEnum;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlDigesterFactory;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import org.omnifaces.util.Faces;


/**
 *
 * @author Toancx
 * @version
 * @since Jun 16, 2015
 */
public class JasperReportUtils {

    private static final Logger logger = Logger.getLogger(JasperReportUtils.class);

    /**
     * exportPdfByte
     *
     * @param beans
     * @param pathTemplate
     * @return
     * @throws Exception
     */
    public static byte[] exportPdfByte(Map beans, String pathTemplate) throws Exception {
        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(new FileInputStream(pathTemplate));
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, beans, new JREmptyDataSource());            
            jasperPrint.setOrientation(OrientationEnum.PORTRAIT);
//            jasperPrint.setOrientation(OrientationEnum.LANDSCAPE);
            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (FileNotFoundException ex) {
            logger.error("File " + pathTemplate + " is not existed....... ");
            throw ex;
        } catch (JRException ex) {
            logger.error("Have error when exportPdfByte ..................");
            throw ex;
        }
    }

    public static JRXmlLoader xmlLoader() throws JRException, ParserConfigurationException, SAXException {
        JasperReportsContext context = DefaultJasperReportsContext.getInstance();
        JRPropertiesUtil.getInstance(context).setProperty("net.sf.jasperreports.xpath.executer.factory",
                "net.sf.jasperreports.engine.util.xml.JaxenXPathExecuterFactory");
       return new JRXmlLoader(context, JRXmlDigesterFactory.createDigester(context));
    }

    public static JasperDesign loadJasperDesign(String pathJasperDesign) throws Exception {
        JRXmlLoader xml = xmlLoader();
        InputStream is = new FileInputStream(pathJasperDesign);
        return xml.loadXML(is);
    }

    public static void exportPdf(Map beans, String pathTemplateDesign, String pathTemplateJasper, String pathFileOutput) throws Exception {
        try {
            long start = Calendar.getInstance().getTimeInMillis();
            JasperReport jasperReport = null;
            if (!DataUtil.isNullOrEmpty(pathTemplateJasper)) {
                jasperReport = (JasperReport) JRLoader.loadObject(new FileInputStream(pathTemplateJasper));
            } else if (!DataUtil.isNullOrEmpty(pathTemplateDesign)) {
                JasperDesign jasperDesign = loadJasperDesign(pathTemplateDesign);
                jasperReport = JasperCompileManager.compileReport(jasperDesign);
                JasperCompileManager.compileReportToFile(pathTemplateDesign);
            }
            if(jasperReport==null){
                return;
            }
            // 22-02-2016 08:37:13   TuyenNT17  them thong tin de view duoi dang pdf
            ByteArrayOutputStream pdfOut = new ByteArrayOutputStream();
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, beans, new JREmptyDataSource());
            JasperExportManager.exportReportToPdfStream(jasperPrint, pdfOut);
            PdfDTO invoicePrinterDTO = new PdfDTO();
            invoicePrinterDTO.setPdfBytes(pdfOut.toByteArray());
            jasperPrint.setOrientation(OrientationEnum.PORTRAIT);
//            jasperPrint.setOrientation(OrientationEnum.LANDSCAPE);
            OutputStream os = new FileOutputStream(pathFileOutput);
            JasperExportManager.exportReportToPdfStream(jasperPrint, os);
            // put data to beans
            beans.put("DATA_OUT_PUT",invoicePrinterDTO);
            os.flush();
            os.close();
            logger.info("exportPdf: duration = " + (Calendar.getInstance().getTimeInMillis() - start));
        } catch (FileNotFoundException ex) {
            logger.error("File " + pathTemplateJasper + " is not existed....... ");
            throw ex;
        } catch (JRException ex) {
            logger.error("Have error when exportPdf ..................");
            throw ex;
        }
    }
}
