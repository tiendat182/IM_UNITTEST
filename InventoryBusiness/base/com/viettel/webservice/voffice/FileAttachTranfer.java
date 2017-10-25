
package com.viettel.webservice.voffice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for fileAttachTranfer complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="fileAttachTranfer">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="fileName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="attachBytes" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *         &lt;element name="fileSign" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="path" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fileAttachTranfer", propOrder = {
    "fileName",
    "attachBytes",
    "fileSign",
    "path"
})
public class FileAttachTranfer {

    @XmlElement(required = true)
    protected String fileName;
    @XmlElement(required = true)
    protected byte[] attachBytes;
    protected long fileSign;
    protected String path;

    /**
     * Gets the value of the fileName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the value of the fileName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFileName(String value) {
        this.fileName = value;
    }

    /**
     * Gets the value of the attachBytes property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getAttachBytes() {
        return attachBytes;
    }

    /**
     * Sets the value of the attachBytes property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setAttachBytes(byte[] value) {
        this.attachBytes = value;
    }

    /**
     * Gets the value of the fileSign property.
     * 
     */
    public long getFileSign() {
        return fileSign;
    }

    /**
     * Sets the value of the fileSign property.
     * 
     */
    public void setFileSign(long value) {
        this.fileSign = value;
    }

    /**
     * Gets the value of the path property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets the value of the path property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPath(String value) {
        this.path = value;
    }

}
