
package com.viettel.webservice.voffice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for staffBO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="staffBO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://service.ws_autosign.voffice.viettel.com/}basicBO">
 *       &lt;sequence>
 *         &lt;element name="index" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="staffID" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="loginName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="passWord" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="fullName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="jobTitle" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="phone" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="email" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dateOfBirth" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="strDateOfBirth" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="cardID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="mobile" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="isEnable" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="caSimPhoneNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="identification" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="aliasName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="messageLanguage" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="groupID" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="groupName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="roleID" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="roleName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "staffBO", propOrder = {
    "index",
    "staffID",
    "loginName",
    "passWord",
    "fullName",
    "jobTitle",
    "phone",
    "email",
    "dateOfBirth",
    "strDateOfBirth",
    "cardID",
    "mobile",
    "isEnable",
    "caSimPhoneNumber",
    "identification",
    "aliasName",
    "messageLanguage",
    "groupID",
    "groupName",
    "roleID",
    "roleName"
})
public class StaffBO
    extends BasicBO
{

    protected int index;
    protected long staffID;
    @XmlElement(required = true)
    protected String loginName;
    @XmlElement(required = true)
    protected String passWord;
    @XmlElement(required = true)
    protected String fullName;
    @XmlElement(required = true)
    protected String jobTitle;
    @XmlElement(required = true)
    protected String phone;
    @XmlElement(required = true)
    protected String email;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateOfBirth;
    @XmlElement(required = true)
    protected String strDateOfBirth;
    @XmlElement(required = true)
    protected String cardID;
    @XmlElement(required = true)
    protected String mobile;
    protected long isEnable;
    @XmlElement(required = true)
    protected String caSimPhoneNumber;
    @XmlElement(required = true)
    protected String identification;
    @XmlElement(required = true)
    protected String aliasName;
    protected long messageLanguage;
    protected long groupID;
    @XmlElement(required = true)
    protected String groupName;
    protected long roleID;
    @XmlElement(required = true)
    protected String roleName;

    /**
     * Gets the value of the index property.
     * 
     */
    public int getIndex() {
        return index;
    }

    /**
     * Sets the value of the index property.
     * 
     */
    public void setIndex(int value) {
        this.index = value;
    }

    /**
     * Gets the value of the staffID property.
     * 
     */
    public long getStaffID() {
        return staffID;
    }

    /**
     * Sets the value of the staffID property.
     * 
     */
    public void setStaffID(long value) {
        this.staffID = value;
    }

    /**
     * Gets the value of the loginName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLoginName() {
        return loginName;
    }

    /**
     * Sets the value of the loginName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLoginName(String value) {
        this.loginName = value;
    }

    /**
     * Gets the value of the passWord property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPassWord() {
        return passWord;
    }

    /**
     * Sets the value of the passWord property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassWord(String value) {
        this.passWord = value;
    }

    /**
     * Gets the value of the fullName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Sets the value of the fullName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFullName(String value) {
        this.fullName = value;
    }

    /**
     * Gets the value of the jobTitle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJobTitle() {
        return jobTitle;
    }

    /**
     * Sets the value of the jobTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJobTitle(String value) {
        this.jobTitle = value;
    }

    /**
     * Gets the value of the phone property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the value of the phone property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhone(String value) {
        this.phone = value;
    }

    /**
     * Gets the value of the email property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the value of the email property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmail(String value) {
        this.email = value;
    }

    /**
     * Gets the value of the dateOfBirth property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Sets the value of the dateOfBirth property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateOfBirth(XMLGregorianCalendar value) {
        this.dateOfBirth = value;
    }

    /**
     * Gets the value of the strDateOfBirth property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrDateOfBirth() {
        return strDateOfBirth;
    }

    /**
     * Sets the value of the strDateOfBirth property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrDateOfBirth(String value) {
        this.strDateOfBirth = value;
    }

    /**
     * Gets the value of the cardID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCardID() {
        return cardID;
    }

    /**
     * Sets the value of the cardID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCardID(String value) {
        this.cardID = value;
    }

    /**
     * Gets the value of the mobile property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * Sets the value of the mobile property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMobile(String value) {
        this.mobile = value;
    }

    /**
     * Gets the value of the isEnable property.
     * 
     */
    public long getIsEnable() {
        return isEnable;
    }

    /**
     * Sets the value of the isEnable property.
     * 
     */
    public void setIsEnable(long value) {
        this.isEnable = value;
    }

    /**
     * Gets the value of the caSimPhoneNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCaSimPhoneNumber() {
        return caSimPhoneNumber;
    }

    /**
     * Sets the value of the caSimPhoneNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCaSimPhoneNumber(String value) {
        this.caSimPhoneNumber = value;
    }

    /**
     * Gets the value of the identification property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentification() {
        return identification;
    }

    /**
     * Sets the value of the identification property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentification(String value) {
        this.identification = value;
    }

    /**
     * Gets the value of the aliasName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAliasName() {
        return aliasName;
    }

    /**
     * Sets the value of the aliasName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAliasName(String value) {
        this.aliasName = value;
    }

    /**
     * Gets the value of the messageLanguage property.
     * 
     */
    public long getMessageLanguage() {
        return messageLanguage;
    }

    /**
     * Sets the value of the messageLanguage property.
     * 
     */
    public void setMessageLanguage(long value) {
        this.messageLanguage = value;
    }

    /**
     * Gets the value of the groupID property.
     * 
     */
    public long getGroupID() {
        return groupID;
    }

    /**
     * Sets the value of the groupID property.
     * 
     */
    public void setGroupID(long value) {
        this.groupID = value;
    }

    /**
     * Gets the value of the groupName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * Sets the value of the groupName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGroupName(String value) {
        this.groupName = value;
    }

    /**
     * Gets the value of the roleID property.
     * 
     */
    public long getRoleID() {
        return roleID;
    }

    /**
     * Sets the value of the roleID property.
     * 
     */
    public void setRoleID(long value) {
        this.roleID = value;
    }

    /**
     * Gets the value of the roleName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * Sets the value of the roleName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRoleName(String value) {
        this.roleName = value;
    }

}
