
package com.viettel.webservice.voffice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for rrFileTransferParam complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="rrFileTransferParam">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="file" type="{http://service.ws_autosign.voffice.viettel.com/}fileAttachTranfer"/>
 *         &lt;element name="result" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "rrFileTransferParam", propOrder = {
    "file",
    "result"
})
public class RrFileTransferParam {

    @XmlElement(required = true)
    protected FileAttachTranfer file;
    protected long result;

    /**
     * Gets the value of the file property.
     * 
     * @return
     *     possible object is
     *     {@link FileAttachTranfer }
     *     
     */
    public FileAttachTranfer getFile() {
        return file;
    }

    /**
     * Sets the value of the file property.
     * 
     * @param value
     *     allowed object is
     *     {@link FileAttachTranfer }
     *     
     */
    public void setFile(FileAttachTranfer value) {
        this.file = value;
    }

    /**
     * Gets the value of the result property.
     * 
     */
    public long getResult() {
        return result;
    }

    /**
     * Sets the value of the result property.
     * 
     */
    public void setResult(long value) {
        this.result = value;
    }

}
