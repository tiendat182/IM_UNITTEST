
package com.viettel.webservice.voffice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for rrParam complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="rrParam">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="transCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="signResult" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="lastSign" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="commentSign" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="actionDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="textId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="issueCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="issueDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="appCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="issueUnit" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="documentCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "rrParam", propOrder = {
    "transCode",
    "signResult",
    "lastSign",
    "commentSign",
    "actionDate",
    "textId",
    "issueCode",
    "issueDate",
    "appCode",
    "issueUnit",
    "documentCode"
})
public class RrParam {

    @XmlElement(required = true)
    protected String transCode;
    protected long signResult;
    @XmlElement(required = true)
    protected String lastSign;
    @XmlElement(required = true)
    protected String commentSign;
    @XmlElement(required = true)
    protected String actionDate;
    protected long textId;
    @XmlElement(required = true)
    protected String issueCode;
    @XmlElement(required = true)
    protected String issueDate;
    @XmlElement(required = true)
    protected String appCode;
    protected long issueUnit;
    @XmlElement(required = true)
    protected String documentCode;

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
     * Gets the value of the signResult property.
     * 
     */
    public long getSignResult() {
        return signResult;
    }

    /**
     * Sets the value of the signResult property.
     * 
     */
    public void setSignResult(long value) {
        this.signResult = value;
    }

    /**
     * Gets the value of the lastSign property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastSign() {
        return lastSign;
    }

    /**
     * Sets the value of the lastSign property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastSign(String value) {
        this.lastSign = value;
    }

    /**
     * Gets the value of the commentSign property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCommentSign() {
        return commentSign;
    }

    /**
     * Sets the value of the commentSign property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCommentSign(String value) {
        this.commentSign = value;
    }

    /**
     * Gets the value of the actionDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActionDate() {
        return actionDate;
    }

    /**
     * Sets the value of the actionDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActionDate(String value) {
        this.actionDate = value;
    }

    /**
     * Gets the value of the textId property.
     * 
     */
    public long getTextId() {
        return textId;
    }

    /**
     * Sets the value of the textId property.
     * 
     */
    public void setTextId(long value) {
        this.textId = value;
    }

    /**
     * Gets the value of the issueCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIssueCode() {
        return issueCode;
    }

    /**
     * Sets the value of the issueCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssueCode(String value) {
        this.issueCode = value;
    }

    /**
     * Gets the value of the issueDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIssueDate() {
        return issueDate;
    }

    /**
     * Sets the value of the issueDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssueDate(String value) {
        this.issueDate = value;
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
     * Gets the value of the documentCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocumentCode() {
        return documentCode;
    }

    /**
     * Sets the value of the documentCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocumentCode(String value) {
        this.documentCode = value;
    }

}
