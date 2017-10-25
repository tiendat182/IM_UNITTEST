
package com.viettel.webservice.voffice;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for treeParam complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="treeParam">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="lstStaffGroup" type="{http://service.ws_autosign.voffice.viettel.com/}staffGroupBO" maxOccurs="unbounded"/>
 *         &lt;element name="lstStaff" type="{http://service.ws_autosign.voffice.viettel.com/}staffBO" maxOccurs="unbounded"/>
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
@XmlType(name = "treeParam", propOrder = {
    "lstStaffGroup",
    "lstStaff",
    "strResult"
})
public class TreeParam {

    @XmlElement(required = true)
    protected List<StaffGroupBO> lstStaffGroup;
    @XmlElement(required = true)
    protected List<StaffBO> lstStaff;
    protected long strResult;

    /**
     * Gets the value of the lstStaffGroup property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the lstStaffGroup property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLstStaffGroup().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StaffGroupBO }
     * 
     * 
     */
    public List<StaffGroupBO> getLstStaffGroup() {
        if (lstStaffGroup == null) {
            lstStaffGroup = new ArrayList<StaffGroupBO>();
        }
        return this.lstStaffGroup;
    }

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
     * {@link StaffBO }
     * 
     * 
     */
    public List<StaffBO> getLstStaff() {
        if (lstStaff == null) {
            lstStaff = new ArrayList<StaffBO>();
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
