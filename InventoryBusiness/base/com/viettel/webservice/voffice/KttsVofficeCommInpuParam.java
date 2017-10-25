
package com.viettel.webservice.voffice;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for kttsVofficeCommInpuParam complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="kttsVofficeCommInpuParam">
 *   &lt;complexContent>
 *     &lt;extension base="{http://service.ws_autosign.voffice.viettel.com/}vofficeCommInpuParam">
 *       &lt;sequence>
 *         &lt;element name="isCanVanthuXetduyet" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="isCanBanhanh" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="lstEmail" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="emailPublishGroup" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="areaId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="documentId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="isUpdateImageSignIndex" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "kttsVofficeCommInpuParam", propOrder = {
    "isCanVanthuXetduyet",
    "isCanBanhanh",
    "lstEmail",
    "emailPublishGroup",
    "areaId",
    "documentId",
    "isUpdateImageSignIndex"
})
public class KttsVofficeCommInpuParam
    extends VofficeCommInpuParam
{

    protected Boolean isCanVanthuXetduyet;
    protected Boolean isCanBanhanh;
    @XmlElement(nillable = true)
    protected List<String> lstEmail;
    protected String emailPublishGroup;
    protected Long areaId;
    protected Long documentId;
    protected Long isUpdateImageSignIndex;

    /**
     * Gets the value of the isCanVanthuXetduyet property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsCanVanthuXetduyet() {
        return isCanVanthuXetduyet;
    }

    /**
     * Sets the value of the isCanVanthuXetduyet property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsCanVanthuXetduyet(Boolean value) {
        this.isCanVanthuXetduyet = value;
    }

    /**
     * Gets the value of the isCanBanhanh property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsCanBanhanh() {
        return isCanBanhanh;
    }

    /**
     * Sets the value of the isCanBanhanh property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsCanBanhanh(Boolean value) {
        this.isCanBanhanh = value;
    }

    /**
     * Gets the value of the lstEmail property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the lstEmail property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLstEmail().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getLstEmail() {
        if (lstEmail == null) {
            lstEmail = new ArrayList<String>();
        }
        return this.lstEmail;
    }

    /**
     * Gets the value of the emailPublishGroup property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmailPublishGroup() {
        return emailPublishGroup;
    }

    /**
     * Sets the value of the emailPublishGroup property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmailPublishGroup(String value) {
        this.emailPublishGroup = value;
    }

    /**
     * Gets the value of the areaId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getAreaId() {
        return areaId;
    }

    /**
     * Sets the value of the areaId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setAreaId(Long value) {
        this.areaId = value;
    }

    /**
     * Gets the value of the documentId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getDocumentId() {
        return documentId;
    }

    /**
     * Sets the value of the documentId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setDocumentId(Long value) {
        this.documentId = value;
    }

    /**
     * Gets the value of the isUpdateImageSignIndex property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIsUpdateImageSignIndex() {
        return isUpdateImageSignIndex;
    }

    /**
     * Sets the value of the isUpdateImageSignIndex property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIsUpdateImageSignIndex(Long value) {
        this.isUpdateImageSignIndex = value;
    }

}
