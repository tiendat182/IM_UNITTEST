package com.viettel.fw.gencode;

import com.viettel.fw.common.util.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.Arrays;

/**
 * Created by vtsoft on 4/13/2015.
 */
public class WsEndpointWriter implements SourceWriter {
    //Quangkm FIX-ATTT:16-5-2016
    public static String FEATURE_DISALLOW_DOCTYPE_DECl = "http://apache.org/xml/features/disallow-doctype-decl";
    public static String FEATURE_EXTERNAL_GENERAL_ENTITIES = "http://xml.org/sax/features/external-general-entities";
    public static String FEATURE_EXTERNAL_PARAMETER_ENTITIES = "http://xml.org/sax/features/external-parameter-entities";
    //End Quangkm

    @Override
    public void writeSource(String filePath, Object fileContent) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                throw new Exception("Khong tim thay file " + filePath);
            }

            //List<WsEndPoint> endPoints = (List<WsEndPoint>) fileContent;
            EndPointInfo endPoint = (EndPointInfo) fileContent;

            String[] lines = FileUtils.readTextFile(filePath);
            int length = lines.length;

            lines[length-1] = "";
            String[] nLines = Arrays.copyOf(lines, length + 5);
            nLines[length++] = "    <jaxws:endpoint";
            nLines[length++] = "            id=\"" + endPoint.getId() + "\"";
            nLines[length++] = "            implementor=\"" + endPoint.getImplementor() + "\"";
            nLines[length++] = "            address=\"" + endPoint.getAddress() + "\"/>";
            nLines[length++] = "</beans>";
            FileUtils.writeTextFile(file, nLines);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void writeSourceX(String filePath, Object fileContent) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                throw new Exception("Khong tim thay file " + filePath);
            }

            //List<WsEndPoint> endPoints = (List<WsEndPoint>) fileContent;
            EndPointInfo endPoint = (EndPointInfo) fileContent;

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            //Quangkm FIX-ATTT:16-5-2016
            documentBuilderFactory.setFeature(FEATURE_DISALLOW_DOCTYPE_DECl, true);
            documentBuilderFactory.setFeature(FEATURE_EXTERNAL_GENERAL_ENTITIES, false);
            documentBuilderFactory.setFeature(FEATURE_EXTERNAL_PARAMETER_ENTITIES, false);
            documentBuilderFactory.setXIncludeAware(false);
            documentBuilderFactory.setExpandEntityReferences(false);
            //End Quangkm
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(filePath);
            Element root = document.getDocumentElement();

            // Root Element
            Element rootElement = document.getDocumentElement();

            //for (WsEndPoint endPoint : endPoints) {
            // server elements
            Element endpoint = document.createElement("jaxws:endpoint");
            endpoint.setAttribute("id", endPoint.getId());
            endpoint.setAttribute("implementor", endPoint.getImplementor());
            endpoint.setAttribute("address", endPoint.getAddress());

            rootElement.appendChild(endpoint);
            root.appendChild(endpoint);
            //}

            DOMSource source = new DOMSource(document);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StreamResult result = new StreamResult(filePath);
            transformer.transform(source, result);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
