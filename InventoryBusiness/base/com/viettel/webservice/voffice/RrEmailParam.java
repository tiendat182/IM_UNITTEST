
package com.viettel.webservice.voffice;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for rrEmailParam complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="rrEmailParam">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="lstStaff" type="{http://service.ws_autosign.voffice.viettel.com/}inforStaff" maxOccurs="unbounded"/>
 *         &lt;element name="strResult" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "rrEmailParam", propOrder = {
    "lstStaff",
    "strResult"
})
public class RrEmailParam {

    @XmlElement(required = true)
    protected List<InforStaff> lstStaff;
    protected long strResult;

    /**
     * Gets the value of the lstStaff property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the lstStaff property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLstStaff().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InforStaff }
     * 
     * 
     */
    public List<InforStaff> getLstStaff() {
        if (lstStaff == null) {
            lstStaff = new ArrayList<InforStaff>();
        }
        return this.lstStaff;
    }

    /**
     * Gets the value of the strResult property.
     * 
     */
    public long getStrResult() {
        return strResult;
    }

    /**
     * Sets the value of the strResult property.
     * 
     */
    public void setStrResult(long value) {
        this.strResult = value;
    }

}
