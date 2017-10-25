
package com.viettel.webservice.voffice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for staffGroupBO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="staffGroupBO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://service.ws_autosign.voffice.viettel.com/}basicBO">
 *       &lt;sequence>
 *         &lt;element name="groupID" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="parentID" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="parentGroup" type="{http://service.ws_autosign.voffice.viettel.com/}staffGroupBO"/>
 *         &lt;element name="groupLevel" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="parentName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="isEnable" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="STT" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="isChoice" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="path" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="isAgent" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="groupCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="fullName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="parentFullName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="groupType" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="pathname" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "staffGroupBO", propOrder = {
    "groupID",
    "name",
    "description",
    "parentID",
    "parentGroup",
    "groupLevel",
    "parentName",
    "isEnable",
    "stt",
    "isChoice",
    "path",
    "isAgent",
    "groupCode",
    "fullName",
    "parentFullName",
    "groupType",
    "pathname"
})
public class StaffGroupBO
    extends BasicBO
{

    protected long groupID;
    @XmlElement(required = true)
    protected String name;
    @XmlElement(required = true)
    protected String description;
    protected long parentID;
    @XmlElement(required = true)
    protected StaffGroupBO parentGroup;
    protected long groupLevel;
    @XmlElement(required = true)
    protected String parentName;
    protected long isEnable;
    @XmlElement(name = "STT", required = true)
    protected String stt;
    protected long isChoice;
    @XmlElement(required = true)
    protected String path;
    protected long isAgent;
    @XmlElement(required = true)
    protected String groupCode;
    @XmlElement(required = true)
    protected String fullName;
    @XmlElement(required = true)
    protected String parentFullName;
    protected long groupType;
    @XmlElement(required = true)
    protected String pathname;

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
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the parentID property.
     * 
     */
    public long getParentID() {
        return parentID;
    }

    /**
     * Sets the value of the parentID property.
     * 
     */
    public void setParentID(long value) {
        this.parentID = value;
    }

    /**
     * Gets the value of the parentGroup property.
     * 
     * @return
     *     possible object is
     *     {@link StaffGroupBO }
     *     
     */
    public StaffGroupBO getParentGroup() {
        return parentGroup;
    }

    /**
     * Sets the value of the parentGroup property.
     * 
     * @param value
     *     allowed object is
     *     {@link StaffGroupBO }
     *     
     */
    public void setParentGroup(StaffGroupBO value) {
        this.parentGroup = value;
    }

    /**
     * Gets the value of the groupLevel property.
     * 
     */
    public long getGroupLevel() {
        return groupLevel;
    }

    /**
     * Sets the value of the groupLevel property.
     * 
     */
    public void setGroupLevel(long value) {
        this.groupLevel = value;
    }

    /**
     * Gets the value of the parentName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParentName() {
        return parentName;
    }

    /**
     * Sets the value of the parentName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParentName(String value) {
        this.parentName = value;
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
     * Gets the value of the stt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSTT() {
        return stt;
    }

    /**
     * Sets the value of the stt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSTT(String value) {
        this.stt = value;
    }

    /**
     * Gets the value of the isChoice property.
     * 
     */
    public long getIsChoice() {
        return isChoice;
    }

    /**
     * Sets the value of the isChoice property.
     * 
     */
    public void setIsChoice(long value) {
        this.isChoice = value;
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

    /**
     * Gets the value of the isAgent property.
     * 
     */
    public long getIsAgent() {
        return isAgent;
    }

    /**
     * Sets the value of the isAgent property.
     * 
     */
    public void setIsAgent(long value) {
        this.isAgent = value;
    }

    /**
     * Gets the value of the groupCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGroupCode() {
        return groupCode;
    }

    /**
     * Sets the value of the groupCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGroupCode(String value) {
        this.groupCode = value;
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
     * Gets the value of the parentFullName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParentFullName() {
        return parentFullName;
    }

    /**
     * Sets the value of the parentFullName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParentFullName(String value) {
        this.parentFullName = value;
    }

    /**
     * Gets the value of the groupType property.
     * 
     */
    public long getGroupType() {
        return groupType;
    }

    /**
     * Sets the value of the groupType property.
     * 
     */
    public void setGroupType(long value) {
        this.groupType = value;
    }

    /**
     * Gets the value of the pathname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPathname() {
        return pathname;
    }

    /**
     * Sets the value of the pathname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPathname(String value) {
        this.pathname = value;
    }

}
