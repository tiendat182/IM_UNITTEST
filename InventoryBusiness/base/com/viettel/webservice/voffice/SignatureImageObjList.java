
package com.viettel.webservice.voffice;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for signatureImageObjList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="signatureImageObjList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="lstSignatureImageObj" type="{http://VO.voffice.viettel.com/}signatureImageObj" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "signatureImageObjList", namespace = "http://VO.voffice.viettel.com/", propOrder = {
    "lstSignatureImageObj"
})
public class SignatureImageObjList {

    @XmlElement(nillable = true)
    protected List<SignatureImageObj> lstSignatureImageObj;

    /**
     * Gets the value of the lstSignatureImageObj property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the lstSignatureImageObj property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLstSignatureImageObj().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SignatureImageObj }
     * 
     * 
     */
    public List<SignatureImageObj> getLstSignatureImageObj() {
        if (lstSignatureImageObj == null) {
            lstSignatureImageObj = new ArrayList<SignatureImageObj>();
        }
        return this.lstSignatureImageObj;
    }

}
