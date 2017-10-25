
package com.viettel.webservice.voffice;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for vofficeCommInpuParam complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="vofficeCommInpuParam">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="accountName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="accountPass" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="appCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="appPass" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="transCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="docTitle" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="sender" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="lstFileAttach" type="{http://service.ws_autosign.voffice.viettel.com/}fileAttachTranfer" maxOccurs="unbounded"/>
 *         &lt;element name="createDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="registerNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="lstStaff" type="{http://service.ws_autosign.voffice.viettel.com/}inforStaff" maxOccurs="unbounded"/>
 *         &lt;element name="issueUnit" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="hinhthucVanban" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="moneyTransfer" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="moneyUnitID" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "vofficeCommInpuParam", propOrder = {
    "accountName",
    "accountPass",
    "appCode",
    "appPass",
    "transCode",
    "docTitle",
    "sender",
    "lstFileAttach",
    "createDate",
    "registerNumber",
    "lstStaff",
    "issueUnit",
    "hinhthucVanban",
    "moneyTransfer",
    "moneyUnitID"
})
@XmlSeeAlso({
    KttsVofficeCommInpuParam.class
})
public class VofficeCommInpuParam {

    @XmlElement(required = true)
    protected String accountName;
    @XmlElement(required = true)
    protected String accountPass;
    @XmlElement(required = true)
    protected String appCode;
    @XmlElement(required = true)
    protected String appPass;
    @XmlElement(required = true)
    protected String transCode;
    @XmlElement(required = true)
    protected String docTitle;
    @XmlElement(required = true)
    protected String sender;
    @XmlElement(required = true)
    protected List<FileAttachTranfer> lstFileAttach;
    @XmlElement(required = true)
    protected String createDate;
    @XmlElement(required = true)
    protected String registerNumber;
    @XmlElement(required = true)
    protected List<InforStaff> lstStaff;
    protected long issueUnit;
    protected Long hinhthucVanban;
    protected Long moneyTransfer;
    protected Long moneyUnitID;

    /**
     * Gets the value of the accountName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountName() {
        return accountName;
    }

    /**
     * Sets the value of the accountName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountName(String value) {
        this.accountName = value;
    }

    /**
     * Gets the value of the accountPass property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountPass() {
        return accountPass;
    }

    /**
     * Sets the value of the accountPass property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountPass(String value) {
        this.accountPass = value;
    }

    /**
     * Gets the value of the appCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAppCode() {
        return appCode;
    }

    /**
     * Sets the value of the appCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAppCode(String value) {
        this.appCode = value;
    }

    /**
     * Gets the value of the appPass property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAppPass() {
        return appPass;
    }

    /**
     * Sets the value of the appPass property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAppPass(String value) {
        this.appPass = value;
    }

    /**
     * Gets the value of the transCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransCode() {
        return transCode;
    }

    /**
     * Sets the value of the transCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransCode(String value) {
        this.transCode = value;
    }

    /**
     * Gets the value of the docTitle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocTitle() {
        return docTitle;
    }

    /**
     * Sets the value of the docTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocTitle(String value) {
        this.docTitle = value;
    }

    /**
     * Gets the value of the sender property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSender() {
        return sender;
    }

    /**
     * Sets the value of the sender property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSender(String value) {
        this.sender = value;
    }

    /**
     * Gets the value of the lstFileAttach property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the lstFileAttach property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLstFileAttach().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FileAttachTranfer }
     * 
     * 
     */
    public List<FileAttachTranfer> getLstFileAttach() {
        if (lstFileAttach == null) {
            lstFileAttach = new ArrayList<FileAttachTranfer>();
        }
        return this.lstFileAttach;
    }

    /**
     * Gets the value of the createDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreateDate() {
        return createDate;
    }

    /**
     * Sets the value of the createDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreateDate(String value) {
        this.createDate = value;
    }

    /**
     * Gets the value of the registerNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegisterNumber() {
        return registerNumber;
    }

    /**
     * Sets the value of the registerNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegisterNumber(String value) {
        this.registerNumber = value;
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
     * Gets the value of the issueUnit property.
     * 
     */
    public long getIssueUnit() {
        return issueUnit;
    }

    /**
     * Sets the value of the issueUnit property.
     * 
     */
    public void setIssueUnit(long value) {
        this.issueUnit = value;
    }

    /**
     * Gets the value of the hinhthucVanban property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getHinhthucVanban() {
        return hinhthucVanban;
    }

    /**
     * Sets the value of the hinhthucVanban property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setHinhthucVanban(Long value) {
        this.hinhthucVanban = value;
    }

    /**
     * Gets the value of the moneyTransfer property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getMoneyTransfer() {
        return moneyTransfer;
    }

    /**
     * Sets the value of the moneyTransfer property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setMoneyTransfer(Long value) {
        this.moneyTransfer = value;
    }

    /**
     * Gets the value of the moneyUnitID property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getMoneyUnitID() {
        return moneyUnitID;
    }

    /**
     * Sets the value of the moneyUnitID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setMoneyUnitID(Long value) {
        this.moneyUnitID = value;
    }

}
