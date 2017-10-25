
package com.viettel.webservice.voffice;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for resultFieldDocument complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="resultFieldDocument">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="listArea" type="{http://service.ws_autosign.voffice.viettel.com/}fieldDocument" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="listDocumentType" type="{http://service.ws_autosign.voffice.viettel.com/}fieldDocument" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="listSecurity" type="{http://service.ws_autosign.voffice.viettel.com/}fieldDocument" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="listUrgency" type="{http://service.ws_autosign.voffice.viettel.com/}fieldDocument" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "resultFieldDocument", propOrder = {
    "listArea",
    "listDocumentType",
    "listSecurity",
    "listUrgency"
})
public class ResultFieldDocument {

    @XmlElement(nillable = true)
    protected List<FieldDocument> listArea;
    @XmlElement(nillable = true)
    protected List<FieldDocument> listDocumentType;
    @XmlElement(nillable = true)
    protected List<FieldDocument> listSecurity;
    @XmlElement(nillable = true)
    protected List<FieldDocument> listUrgency;

    /**
     * Gets the value of the listArea property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listArea property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListArea().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FieldDocument }
     * 
     * 
     */
    public List<FieldDocument> getListArea() {
        if (listArea == null) {
            listArea = new ArrayList<FieldDocument>();
        }
        return this.listArea;
    }

    /**
     * Gets the value of the listDocumentType property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listDocumentType property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListDocumentType().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FieldDocument }
     * 
     * 
     */
    public List<FieldDocument> getListDocumentType() {
        if (listDocumentType == null) {
            listDocumentType = new ArrayList<FieldDocument>();
        }
        return this.listDocumentType;
    }

    /**
     * Gets the value of the listSecurity property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listSecurity property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListSecurity().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FieldDocument }
     * 
     * 
     */
    public List<FieldDocument> getListSecurity() {
        if (listSecurity == null) {
            listSecurity = new ArrayList<FieldDocument>();
        }
        return this.listSecurity;
    }

    /**
     * Gets the value of the listUrgency property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listUrgency property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListUrgency().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FieldDocument }
     * 
     * 
     */
    public List<FieldDocument> getListUrgency() {
        if (listUrgency == null) {
            listUrgency = new ArrayList<FieldDocument>();
        }
        return this.listUrgency;
    }

}
