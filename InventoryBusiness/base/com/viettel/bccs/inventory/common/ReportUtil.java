package com.viettel.bccs.inventory.common;

import com.google.common.collect.Lists;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import com.viettel.fw.common.util.DataUtil;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.pdfwriter.ContentStreamWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.util.PDFOperator;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.pdfbox.util.TextPosition;
import org.springframework.beans.factory.annotation.Required;

import java.io.*;
import java.util.Collections;
import java.util.List;

/**
 * @author luannt23.
 * @comment
 * @date 3/8/2016.
 */

public class ReportUtil {
    private String[] keyAddAnnotations;
    private String templatePath;
    private String outPath;
    private String temp;
    private String contextPath;
    private String imCode;

    public ReportUtil() {
        contextPath = "";
    }

    public void saveFile(byte[] fileContent, String fileName) throws Exception {
        String fullName = getOutPath() + fileName;
        File file = new File(fullName);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream outputStream = new FileOutputStream(file, false);
        outputStream.write(fileContent);
        outputStream.close();
    }

    public byte[] insertComment(byte[] pdfContent, List<String> listStaff) throws Exception {
        int totalSign = listStaff.size();
        PdfReader reader = new PdfReader(pdfContent);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfStamper stamper = new PdfStamper(reader, out);
        int page = reader.getNumberOfPages();
        Rectangle rec = reader.getPageSize(page);
        List<TextPosition> lstTextPosition = findPositions(pdfContent);
        int i = 0;
        for (TextPosition textPosition : lstTextPosition) {
            String content = getContentAnnotation(textPosition, totalSign);
            if (!(DataUtil.isNullOrEmpty(content))) {
                long x = Math.round(textPosition.getX());
                long y = Math.round(rec.getHeight() - textPosition.getY());
                PdfAnnotation annotation = PdfAnnotation.createSquareCircle(
                        stamper.getWriter(), new Rectangle(x, y, x, y), content, true);
                annotation.setTitle(listStaff.get(i++));
                annotation.setColor(BaseColor.YELLOW);
                annotation.setFlags(PdfAnnotation.FLAGS_PRINT);
                annotation.setBorder(new PdfBorderArray(0, 0, 2, new PdfDashPattern()));

                stamper.addAnnotation(annotation, page);
            }
        }
        stamper.close();
        reader.close();
        return removeSignMark(out.toByteArray());
    }

    public List<TextPosition> findPositions(byte[] pdfContent) throws IOException {
        PDDocument document = PDDocument.load(new ByteArrayInputStream(pdfContent));
        final StringBuffer extractedText = new StringBuffer();
        final List<TextPosition> positions = Lists.newArrayList();
        PDFTextStripper textStripper = new PDFTextStripper() {
            @Override
            protected void processTextPosition(TextPosition text) {
                extractedText.append(text.getCharacter());
                for (String key : keyAddAnnotations) {
                    if (extractedText.toString().endsWith(key)) {
                        positions.add(text);
                    }
                }
            }
        };

        List lstPage = document.getDocumentCatalog().getAllPages();
        PDPage page = (PDPage) lstPage.get(lstPage.size() - 1);
        textStripper.processStream(page, page.findResources(), page.getContents().getStream());
        document.close();
        return Collections.unmodifiableList(positions);
    }

    public byte[] removeSignMark(byte[] pdfContent) throws IOException, COSVisitorException {

        PDDocument doc = PDDocument.load(new ByteArrayInputStream(pdfContent));
        List pages = doc.getDocumentCatalog().getAllPages();
        int i = pages.size() - 1;
        PDPage page = (PDPage) pages.get(i);
        PDStream contents = page.getContents();
        PDFStreamParser parser = new PDFStreamParser(contents.getStream());
        parser.parse();
        List tokens = parser.getTokens();
        for (int j = 0; j < tokens.size(); j++) {
            Object next = tokens.get(j);
            if (next instanceof PDFOperator) {
                PDFOperator op = (PDFOperator) next;
                if (DataUtil.safeEqual(op.getOperation(), "Tj")) {
                    COSString previous = (COSString) tokens.get(j - 1);
                    String string = previous.getString();
                    if (string.contains(keyAddAnnotations[0])
                            || string.contains(keyAddAnnotations[1])
                            || string.contains(keyAddAnnotations[2])
                            || string.contains(keyAddAnnotations[3])
                            || string.contains(keyAddAnnotations[4])) {
                        string = string.replaceAll(keyAddAnnotations[0], "")
                                .replaceAll(keyAddAnnotations[1], "")
                                .replaceAll(keyAddAnnotations[2], "")
                                .replaceAll(keyAddAnnotations[3], "")
                                .replaceAll(keyAddAnnotations[4], "");
                        previous.reset();
                        previous.append(string.getBytes("ISO-8859-1"));
                    }
                } else if (DataUtil.safeEqual(op.getOperation(), "TJ")) {
                    COSArray previous = (COSArray) tokens.get(j - 1);
                    for (int k = 0; k < previous.size(); k++) {
                        Object arrElement = previous.getObject(k);
                        if (arrElement instanceof COSString) {
                            COSString cosString = (COSString) arrElement;
                            String string = cosString.getString();
                            if (string.contains(keyAddAnnotations[0])
                                    || string.contains(keyAddAnnotations[1])
                                    || string.contains(keyAddAnnotations[2])
                                    || string.contains(keyAddAnnotations[3])
                                    || string.contains(keyAddAnnotations[4])) {
                                string = string.replaceAll(keyAddAnnotations[0], "")
                                        .replaceAll(keyAddAnnotations[1], "")
                                        .replaceAll(keyAddAnnotations[2], "")
                                        .replaceAll(keyAddAnnotations[3], "")
                                        .replaceAll(keyAddAnnotations[4], "");
                                cosString.reset();
                                cosString.append(string.getBytes("ISO-8859-1"));
                            }
                        }
                    }
                }
            }
        }
        PDStream updatedStream = new PDStream(doc);
        OutputStream os = updatedStream.createOutputStream();
        ContentStreamWriter tokenWriter = new ContentStreamWriter(os);

        tokenWriter.writeTokens(tokens);

        page.setContents(updatedStream);


        ByteArrayOutputStream out = new ByteArrayOutputStream();
        doc.save(out);
        parser.close();
        doc.close();
        return out.toByteArray();
    }

    public static String getContentAnnotation(TextPosition textPosition, int totalSign) {
        String content = "";
        int value = Integer.parseInt(textPosition.getCharacter());
        if (value <= totalSign) {
            content = String.valueOf(totalSign - value + 1);
        }
        return content;
    }


    public String getOutPath() {
        return contextPath + outPath;
    }

    @Required
    public void setOutPath(String outPath) {
        this.outPath = outPath;
    }

    public String getTemplatePath() {
        return contextPath + templatePath;
    }

    @Required
    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public String getTemp() {
        return temp;
    }

    @Required
    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String[] getKeyAddAnnotations() {
        return keyAddAnnotations;
    }

    @Required
    public void setKeyAddAnnotations(String[] keyAddAnnotations) {
        this.keyAddAnnotations = keyAddAnnotations;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public String getImCode() {
        return imCode;
    }

    public void setImCode(String imCode) {
        this.imCode = imCode;
    }
}
