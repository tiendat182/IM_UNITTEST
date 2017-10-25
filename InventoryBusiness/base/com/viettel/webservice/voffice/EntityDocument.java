
package com.viettel.webservice.voffice;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for entityDocument complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="entityDocument">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="alternatedDocumentCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="alternatingDocumentCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="alternativeDocumentId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="appliedPoint" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="area" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="areaId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="attachment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="builtGroup" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="chiefId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="commentContent" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="confirmTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="content" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="createDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="creatorGroupId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="creatorGroupId2" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="creatorId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="creatorId2" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="deadlineDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="deadlineDateFrom" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="deadlineDateTo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="docType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="documentExtra" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="documentId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="endEffectDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="incomingNumber" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="industryId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="industryName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="isArrive" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="isConfidential" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="isForwardAgent" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="isFromCoporation" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="isKeepOfficalBook" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="isLock" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="isMark" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="isProcessing" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="isRead" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="isTransferedMoney" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="keepCopyPlace" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="leaderComment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="listEmployeeHasPermission" type="{http://www.w3.org/2001/XMLSchema}long" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="numEffectiveDay" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numberOfCopy" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="officeSender" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="oldCreatorId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="priority" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="priorityId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="processTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="processType" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="promulgateDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="promulgateDateFrom" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="promulgateDateTo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="publishedGroup" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="publisherId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="quickSearch" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="rangePublished" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="receiveDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="receiveDateFrom" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="receiveDateTo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="receivedPlace" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="receiverId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="registerNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sender" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="senderEmail" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="senderEmail2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="senderId2" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="senderMobile" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="senderMobile2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="senderName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="senderName2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="signedOnPaper" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="signedOnPaperDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="signedPaper" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="signer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="staffEmail" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="staffId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="staffMobile" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="staffName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="startEffectDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="stateNumber" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="statusNumber" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="storage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="stype" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="stypeId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="stypeName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="summary" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="textCreatorId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="title" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="toDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="total" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="tranferMoney" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="transferDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="typeId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="unitMoney" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="unitMoneyId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="url" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "entityDocument", propOrder = {
    "alternatedDocumentCode",
    "alternatingDocumentCode",
    "alternativeDocumentId",
    "appliedPoint",
    "area",
    "areaId",
    "attachment",
    "builtGroup",
    "chiefId",
    "code",
    "commentContent",
    "confirmTime",
    "content",
    "createDate",
    "creatorGroupId",
    "creatorGroupId2",
    "creatorId",
    "creatorId2",
    "deadlineDate",
    "deadlineDateFrom",
    "deadlineDateTo",
    "docType",
    "documentExtra",
    "documentId",
    "endEffectDate",
    "id",
    "incomingNumber",
    "industryId",
    "industryName",
    "isArrive",
    "isConfidential",
    "isForwardAgent",
    "isFromCoporation",
    "isKeepOfficalBook",
    "isLock",
    "isMark",
    "isProcessing",
    "isRead",
    "isTransferedMoney",
    "keepCopyPlace",
    "leaderComment",
    "listEmployeeHasPermission",
    "numEffectiveDay",
    "numberOfCopy",
    "officeSender",
    "oldCreatorId",
    "priority",
    "priorityId",
    "processTime",
    "processType",
    "promulgateDate",
    "promulgateDateFrom",
    "promulgateDateTo",
    "publishedGroup",
    "publisherId",
    "quickSearch",
    "rangePublished",
    "receiveDate",
    "receiveDateFrom",
    "receiveDateTo",
    "receivedPlace",
    "receiverId",
    "registerNumber",
    "sender",
    "senderEmail",
    "senderEmail2",
    "senderId2",
    "senderMobile",
    "senderMobile2",
    "senderName",
    "senderName2",
    "signedOnPaper",
    "signedOnPaperDate",
    "signedPaper",
    "signer",
    "staffEmail",
    "staffId",
    "staffMobile",
    "staffName",
    "startEffectDate",
    "stateNumber",
    "status",
    "statusNumber",
    "storage",
    "stype",
    "stypeId",
    "stypeName",
    "summary",
    "textCreatorId",
    "title",
    "toDate",
    "total",
    "tranferMoney",
    "transferDate",
    "type",
    "typeId",
    "unitMoney",
    "unitMoneyId",
    "url"
})
public class EntityDocument {

    protected String alternatedDocumentCode;
    protected String alternatingDocumentCode;
    protected Long alternativeDocumentId;
    protected String appliedPoint;
    protected String area;
    protected Long areaId;
    protected String attachment;
    protected String builtGroup;
    protected Long chiefId;
    protected String code;
    protected String commentContent;
    protected String confirmTime;
    protected String content;
    protected String createDate;
    protected Long creatorGroupId;
    protected Long creatorGroupId2;
    protected Long creatorId;
    protected Long creatorId2;
    protected String deadlineDate;
    protected String deadlineDateFrom;
    protected String deadlineDateTo;
    protected String docType;
    protected String documentExtra;
    protected Long documentId;
    protected String endEffectDate;
    protected Long id;
    protected Long incomingNumber;
    protected Long industryId;
    protected String industryName;
    protected Integer isArrive;
    protected Long isConfidential;
    protected Integer isForwardAgent;
    protected Integer isFromCoporation;
    protected Long isKeepOfficalBook;
    protected Integer isLock;
    protected Integer isMark;
    protected Integer isProcessing;
    protected Integer isRead;
    protected Integer isTransferedMoney;
    protected String keepCopyPlace;
    protected String leaderComment;
    @XmlElement(nillable = true)
    protected List<Long> listEmployeeHasPermission;
    protected String numEffectiveDay;
    protected Long numberOfCopy;
    protected String officeSender;
    protected Long oldCreatorId;
    protected String priority;
    protected Long priorityId;
    protected String processTime;
    protected Long processType;
    protected String promulgateDate;
    protected String promulgateDateFrom;
    protected String promulgateDateTo;
    protected String publishedGroup;
    protected Long publisherId;
    protected Boolean quickSearch;
    protected String rangePublished;
    protected String receiveDate;
    protected String receiveDateFrom;
    protected String receiveDateTo;
    protected String receivedPlace;
    protected Long receiverId;
    protected String registerNumber;
    protected String sender;
    protected String senderEmail;
    protected String senderEmail2;
    protected Long senderId2;
    protected String senderMobile;
    protected String senderMobile2;
    protected String senderName;
    protected String senderName2;
    protected Integer signedOnPaper;
    protected String signedOnPaperDate;
    protected Integer signedPaper;
    protected String signer;
    protected String staffEmail;
    protected Long staffId;
    protected String staffMobile;
    protected String staffName;
    protected String startEffectDate;
    protected Long stateNumber;
    protected Long status;
    protected Long statusNumber;
    protected String storage;
    protected String stype;
    protected Long stypeId;
    protected String stypeName;
    protected String summary;
    protected Long textCreatorId;
    protected String title;
    protected String toDate;
    protected Integer total;
    protected Long tranferMoney;
    protected String transferDate;
    protected String type;
    protected Long typeId;
    protected String unitMoney;
    protected Long unitMoneyId;
    protected String url;

    /**
     * Gets the value of the alternatedDocumentCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlternatedDocumentCode() {
        return alternatedDocumentCode;
    }

    /**
     * Sets the value of the alternatedDocumentCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlternatedDocumentCode(String value) {
        this.alternatedDocumentCode = value;
    }

    /**
     * Gets the value of the alternatingDocumentCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlternatingDocumentCode() {
        return alternatingDocumentCode;
    }

    /**
     * Sets the value of the alternatingDocumentCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlternatingDocumentCode(String value) {
        this.alternatingDocumentCode = value;
    }

    /**
     * Gets the value of the alternativeDocumentId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getAlternativeDocumentId() {
        return alternativeDocumentId;
    }

    /**
     * Sets the value of the alternativeDocumentId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setAlternativeDocumentId(Long value) {
        this.alternativeDocumentId = value;
    }

    /**
     * Gets the value of the appliedPoint property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAppliedPoint() {
        return appliedPoint;
    }

    /**
     * Sets the value of the appliedPoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAppliedPoint(String value) {
        this.appliedPoint = value;
    }

    /**
     * Gets the value of the area property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArea() {
        return area;
    }

    /**
     * Sets the value of the area property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArea(String value) {
        this.area = value;
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
     * Gets the value of the attachment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAttachment() {
        return attachment;
    }

    /**
     * Sets the value of the attachment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAttachment(String value) {
        this.attachment = value;
    }

    /**
     * Gets the value of the builtGroup property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBuiltGroup() {
        return builtGroup;
    }

    /**
     * Sets the value of the builtGroup property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBuiltGroup(String value) {
        this.builtGroup = value;
    }

    /**
     * Gets the value of the chiefId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getChiefId() {
        return chiefId;
    }

    /**
     * Sets the value of the chiefId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setChiefId(Long value) {
        this.chiefId = value;
    }

    /**
     * Gets the value of the code property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the value of the code property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCode(String value) {
        this.code = value;
    }

    /**
     * Gets the value of the commentContent property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCommentContent() {
        return commentContent;
    }

    /**
     * Sets the value of the commentContent property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCommentContent(String value) {
        this.commentContent = value;
    }

    /**
     * Gets the value of the confirmTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConfirmTime() {
        return confirmTime;
    }

    /**
     * Sets the value of the confirmTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConfirmTime(String value) {
        this.confirmTime = value;
    }

    /**
     * Gets the value of the content property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the value of the content property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContent(String value) {
        this.content = value;
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
     * Gets the value of the creatorGroupId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCreatorGroupId() {
        return creatorGroupId;
    }

    /**
     * Sets the value of the creatorGroupId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCreatorGroupId(Long value) {
        this.creatorGroupId = value;
    }

    /**
     * Gets the value of the creatorGroupId2 property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCreatorGroupId2() {
        return creatorGroupId2;
    }

    /**
     * Sets the value of the creatorGroupId2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCreatorGroupId2(Long value) {
        this.creatorGroupId2 = value;
    }

    /**
     * Gets the value of the creatorId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCreatorId() {
        return creatorId;
    }

    /**
     * Sets the value of the creatorId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCreatorId(Long value) {
        this.creatorId = value;
    }

    /**
     * Gets the value of the creatorId2 property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCreatorId2() {
        return creatorId2;
    }

    /**
     * Sets the value of the creatorId2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCreatorId2(Long value) {
        this.creatorId2 = value;
    }

    /**
     * Gets the value of the deadlineDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeadlineDate() {
        return deadlineDate;
    }

    /**
     * Sets the value of the deadlineDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeadlineDate(String value) {
        this.deadlineDate = value;
    }

    /**
     * Gets the value of the deadlineDateFrom property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeadlineDateFrom() {
        return deadlineDateFrom;
    }

    /**
     * Sets the value of the deadlineDateFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeadlineDateFrom(String value) {
        this.deadlineDateFrom = value;
    }

    /**
     * Gets the value of the deadlineDateTo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeadlineDateTo() {
        return deadlineDateTo;
    }

    /**
     * Sets the value of the deadlineDateTo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeadlineDateTo(String value) {
        this.deadlineDateTo = value;
    }

    /**
     * Gets the value of the docType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocType() {
        return docType;
    }

    /**
     * Sets the value of the docType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocType(String value) {
        this.docType = value;
    }

    /**
     * Gets the value of the documentExtra property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocumentExtra() {
        return documentExtra;
    }

    /**
     * Sets the value of the documentExtra property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocumentExtra(String value) {
        this.documentExtra = value;
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
     * Gets the value of the endEffectDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEndEffectDate() {
        return endEffectDate;
    }

    /**
     * Sets the value of the endEffectDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEndEffectDate(String value) {
        this.endEffectDate = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setId(Long value) {
        this.id = value;
    }

    /**
     * Gets the value of the incomingNumber property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIncomingNumber() {
        return incomingNumber;
    }

    /**
     * Sets the value of the incomingNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIncomingNumber(Long value) {
        this.incomingNumber = value;
    }

    /**
     * Gets the value of the industryId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIndustryId() {
        return industryId;
    }

    /**
     * Sets the value of the industryId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIndustryId(Long value) {
        this.industryId = value;
    }

    /**
     * Gets the value of the industryName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIndustryName() {
        return industryName;
    }

    /**
     * Sets the value of the industryName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIndustryName(String value) {
        this.industryName = value;
    }

    /**
     * Gets the value of the isArrive property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIsArrive() {
        return isArrive;
    }

    /**
     * Sets the value of the isArrive property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIsArrive(Integer value) {
        this.isArrive = value;
    }

    /**
     * Gets the value of the isConfidential property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIsConfidential() {
        return isConfidential;
    }

    /**
     * Sets the value of the isConfidential property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIsConfidential(Long value) {
        this.isConfidential = value;
    }

    /**
     * Gets the value of the isForwardAgent property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIsForwardAgent() {
        return isForwardAgent;
    }

    /**
     * Sets the value of the isForwardAgent property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIsForwardAgent(Integer value) {
        this.isForwardAgent = value;
    }

    /**
     * Gets the value of the isFromCoporation property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIsFromCoporation() {
        return isFromCoporation;
    }

    /**
     * Sets the value of the isFromCoporation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIsFromCoporation(Integer value) {
        this.isFromCoporation = value;
    }

    /**
     * Gets the value of the isKeepOfficalBook property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIsKeepOfficalBook() {
        return isKeepOfficalBook;
    }

    /**
     * Sets the value of the isKeepOfficalBook property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIsKeepOfficalBook(Long value) {
        this.isKeepOfficalBook = value;
    }

    /**
     * Gets the value of the isLock property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIsLock() {
        return isLock;
    }

    /**
     * Sets the value of the isLock property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIsLock(Integer value) {
        this.isLock = value;
    }

    /**
     * Gets the value of the isMark property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIsMark() {
        return isMark;
    }

    /**
     * Sets the value of the isMark property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIsMark(Integer value) {
        this.isMark = value;
    }

    /**
     * Gets the value of the isProcessing property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIsProcessing() {
        return isProcessing;
    }

    /**
     * Sets the value of the isProcessing property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIsProcessing(Integer value) {
        this.isProcessing = value;
    }

    /**
     * Gets the value of the isRead property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIsRead() {
        return isRead;
    }

    /**
     * Sets the value of the isRead property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIsRead(Integer value) {
        this.isRead = value;
    }

    /**
     * Gets the value of the isTransferedMoney property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIsTransferedMoney() {
        return isTransferedMoney;
    }

    /**
     * Sets the value of the isTransferedMoney property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIsTransferedMoney(Integer value) {
        this.isTransferedMoney = value;
    }

    /**
     * Gets the value of the keepCopyPlace property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKeepCopyPlace() {
        return keepCopyPlace;
    }

    /**
     * Sets the value of the keepCopyPlace property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKeepCopyPlace(String value) {
        this.keepCopyPlace = value;
    }

    /**
     * Gets the value of the leaderComment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLeaderComment() {
        return leaderComment;
    }

    /**
     * Sets the value of the leaderComment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLeaderComment(String value) {
        this.leaderComment = value;
    }

    /**
     * Gets the value of the listEmployeeHasPermission property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listEmployeeHasPermission property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListEmployeeHasPermission().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Long }
     * 
     * 
     */
    public List<Long> getListEmployeeHasPermission() {
        if (listEmployeeHasPermission == null) {
            listEmployeeHasPermission = new ArrayList<Long>();
        }
        return this.listEmployeeHasPermission;
    }

    /**
     * Gets the value of the numEffectiveDay property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumEffectiveDay() {
        return numEffectiveDay;
    }

    /**
     * Sets the value of the numEffectiveDay property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumEffectiveDay(String value) {
        this.numEffectiveDay = value;
    }

    /**
     * Gets the value of the numberOfCopy property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getNumberOfCopy() {
        return numberOfCopy;
    }

    /**
     * Sets the value of the numberOfCopy property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setNumberOfCopy(Long value) {
        this.numberOfCopy = value;
    }

    /**
     * Gets the value of the officeSender property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOfficeSender() {
        return officeSender;
    }

    /**
     * Sets the value of the officeSender property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOfficeSender(String value) {
        this.officeSender = value;
    }

    /**
     * Gets the value of the oldCreatorId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getOldCreatorId() {
        return oldCreatorId;
    }

    /**
     * Sets the value of the oldCreatorId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setOldCreatorId(Long value) {
        this.oldCreatorId = value;
    }

    /**
     * Gets the value of the priority property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPriority() {
        return priority;
    }

    /**
     * Sets the value of the priority property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPriority(String value) {
        this.priority = value;
    }

    /**
     * Gets the value of the priorityId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getPriorityId() {
        return priorityId;
    }

    /**
     * Sets the value of the priorityId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setPriorityId(Long value) {
        this.priorityId = value;
    }

    /**
     * Gets the value of the processTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProcessTime() {
        return processTime;
    }

    /**
     * Sets the value of the processTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProcessTime(String value) {
        this.processTime = value;
    }

    /**
     * Gets the value of the processType property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getProcessType() {
        return processType;
    }

    /**
     * Sets the value of the processType property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setProcessType(Long value) {
        this.processType = value;
    }

    /**
     * Gets the value of the promulgateDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPromulgateDate() {
        return promulgateDate;
    }

    /**
     * Sets the value of the promulgateDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPromulgateDate(String value) {
        this.promulgateDate = value;
    }

    /**
     * Gets the value of the promulgateDateFrom property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPromulgateDateFrom() {
        return promulgateDateFrom;
    }

    /**
     * Sets the value of the promulgateDateFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPromulgateDateFrom(String value) {
        this.promulgateDateFrom = value;
    }

    /**
     * Gets the value of the promulgateDateTo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPromulgateDateTo() {
        return promulgateDateTo;
    }

    /**
     * Sets the value of the promulgateDateTo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPromulgateDateTo(String value) {
        this.promulgateDateTo = value;
    }

    /**
     * Gets the value of the publishedGroup property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPublishedGroup() {
        return publishedGroup;
    }

    /**
     * Sets the value of the publishedGroup property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPublishedGroup(String value) {
        this.publishedGroup = value;
    }

    /**
     * Gets the value of the publisherId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getPublisherId() {
        return publisherId;
    }

    /**
     * Sets the value of the publisherId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setPublisherId(Long value) {
        this.publisherId = value;
    }

    /**
     * Gets the value of the quickSearch property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isQuickSearch() {
        return quickSearch;
    }

    /**
     * Sets the value of the quickSearch property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setQuickSearch(Boolean value) {
        this.quickSearch = value;
    }

    /**
     * Gets the value of the rangePublished property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRangePublished() {
        return rangePublished;
    }

    /**
     * Sets the value of the rangePublished property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRangePublished(String value) {
        this.rangePublished = value;
    }

    /**
     * Gets the value of the receiveDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReceiveDate() {
        return receiveDate;
    }

    /**
     * Sets the value of the receiveDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReceiveDate(String value) {
        this.receiveDate = value;
    }

    /**
     * Gets the value of the receiveDateFrom property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReceiveDateFrom() {
        return receiveDateFrom;
    }

    /**
     * Sets the value of the receiveDateFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReceiveDateFrom(String value) {
        this.receiveDateFrom = value;
    }

    /**
     * Gets the value of the receiveDateTo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReceiveDateTo() {
        return receiveDateTo;
    }

    /**
     * Sets the value of the receiveDateTo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReceiveDateTo(String value) {
        this.receiveDateTo = value;
    }

    /**
     * Gets the value of the receivedPlace property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReceivedPlace() {
        return receivedPlace;
    }

    /**
     * Sets the value of the receivedPlace property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReceivedPlace(String value) {
        this.receivedPlace = value;
    }

    /**
     * Gets the value of the receiverId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getReceiverId() {
        return receiverId;
    }

    /**
     * Sets the value of the receiverId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setReceiverId(Long value) {
        this.receiverId = value;
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
     * Gets the value of the senderEmail property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSenderEmail() {
        return senderEmail;
    }

    /**
     * Sets the value of the senderEmail property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSenderEmail(String value) {
        this.senderEmail = value;
    }

    /**
     * Gets the value of the senderEmail2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSenderEmail2() {
        return senderEmail2;
    }

    /**
     * Sets the value of the senderEmail2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSenderEmail2(String value) {
        this.senderEmail2 = value;
    }

    /**
     * Gets the value of the senderId2 property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getSenderId2() {
        return senderId2;
    }

    /**
     * Sets the value of the senderId2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setSenderId2(Long value) {
        this.senderId2 = value;
    }

    /**
     * Gets the value of the senderMobile property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSenderMobile() {
        return senderMobile;
    }

    /**
     * Sets the value of the senderMobile property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSenderMobile(String value) {
        this.senderMobile = value;
    }

    /**
     * Gets the value of the senderMobile2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSenderMobile2() {
        return senderMobile2;
    }

    /**
     * Sets the value of the senderMobile2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSenderMobile2(String value) {
        this.senderMobile2 = value;
    }

    /**
     * Gets the value of the senderName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSenderName() {
        return senderName;
    }

    /**
     * Sets the value of the senderName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSenderName(String value) {
        this.senderName = value;
    }

    /**
     * Gets the value of the senderName2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSenderName2() {
        return senderName2;
    }

    /**
     * Sets the value of the senderName2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSenderName2(String value) {
        this.senderName2 = value;
    }

    /**
     * Gets the value of the signedOnPaper property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSignedOnPaper() {
        return signedOnPaper;
    }

    /**
     * Sets the value of the signedOnPaper property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSignedOnPaper(Integer value) {
        this.signedOnPaper = value;
    }

    /**
     * Gets the value of the signedOnPaperDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSignedOnPaperDate() {
        return signedOnPaperDate;
    }

    /**
     * Sets the value of the signedOnPaperDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSignedOnPaperDate(String value) {
        this.signedOnPaperDate = value;
    }

    /**
     * Gets the value of the signedPaper property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSignedPaper() {
        return signedPaper;
    }

    /**
     * Sets the value of the signedPaper property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSignedPaper(Integer value) {
        this.signedPaper = value;
    }

    /**
     * Gets the value of the signer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSigner() {
        return signer;
    }

    /**
     * Sets the value of the signer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSigner(String value) {
        this.signer = value;
    }

    /**
     * Gets the value of the staffEmail property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStaffEmail() {
        return staffEmail;
    }

    /**
     * Sets the value of the staffEmail property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStaffEmail(String value) {
        this.staffEmail = value;
    }

    /**
     * Gets the value of the staffId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getStaffId() {
        return staffId;
    }

    /**
     * Sets the value of the staffId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setStaffId(Long value) {
        this.staffId = value;
    }

    /**
     * Gets the value of the staffMobile property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStaffMobile() {
        return staffMobile;
    }

    /**
     * Sets the value of the staffMobile property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStaffMobile(String value) {
        this.staffMobile = value;
    }

    /**
     * Gets the value of the staffName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStaffName() {
        return staffName;
    }

    /**
     * Sets the value of the staffName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStaffName(String value) {
        this.staffName = value;
    }

    /**
     * Gets the value of the startEffectDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStartEffectDate() {
        return startEffectDate;
    }

    /**
     * Sets the value of the startEffectDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStartEffectDate(String value) {
        this.startEffectDate = value;
    }

    /**
     * Gets the value of the stateNumber property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getStateNumber() {
        return stateNumber;
    }

    /**
     * Sets the value of the stateNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setStateNumber(Long value) {
        this.stateNumber = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setStatus(Long value) {
        this.status = value;
    }

    /**
     * Gets the value of the statusNumber property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getStatusNumber() {
        return statusNumber;
    }

    /**
     * Sets the value of the statusNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setStatusNumber(Long value) {
        this.statusNumber = value;
    }

    /**
     * Gets the value of the storage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStorage() {
        return storage;
    }

    /**
     * Sets the value of the storage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStorage(String value) {
        this.storage = value;
    }

    /**
     * Gets the value of the stype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStype() {
        return stype;
    }

    /**
     * Sets the value of the stype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStype(String value) {
        this.stype = value;
    }

    /**
     * Gets the value of the stypeId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getStypeId() {
        return stypeId;
    }

    /**
     * Sets the value of the stypeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setStypeId(Long value) {
        this.stypeId = value;
    }

    /**
     * Gets the value of the stypeName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStypeName() {
        return stypeName;
    }

    /**
     * Sets the value of the stypeName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStypeName(String value) {
        this.stypeName = value;
    }

    /**
     * Gets the value of the summary property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSummary() {
        return summary;
    }

    /**
     * Sets the value of the summary property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSummary(String value) {
        this.summary = value;
    }

    /**
     * Gets the value of the textCreatorId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getTextCreatorId() {
        return textCreatorId;
    }

    /**
     * Sets the value of the textCreatorId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setTextCreatorId(Long value) {
        this.textCreatorId = value;
    }

    /**
     * Gets the value of the title property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Gets the value of the toDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getToDate() {
        return toDate;
    }

    /**
     * Sets the value of the toDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setToDate(String value) {
        this.toDate = value;
    }

    /**
     * Gets the value of the total property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTotal() {
        return total;
    }

    /**
     * Sets the value of the total property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTotal(Integer value) {
        this.total = value;
    }

    /**
     * Gets the value of the tranferMoney property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getTranferMoney() {
        return tranferMoney;
    }

    /**
     * Sets the value of the tranferMoney property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setTranferMoney(Long value) {
        this.tranferMoney = value;
    }

    /**
     * Gets the value of the transferDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransferDate() {
        return transferDate;
    }

    /**
     * Sets the value of the transferDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransferDate(String value) {
        this.transferDate = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the typeId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getTypeId() {
        return typeId;
    }

    /**
     * Sets the value of the typeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setTypeId(Long value) {
        this.typeId = value;
    }

    /**
     * Gets the value of the unitMoney property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnitMoney() {
        return unitMoney;
    }

    /**
     * Sets the value of the unitMoney property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnitMoney(String value) {
        this.unitMoney = value;
    }

    /**
     * Gets the value of the unitMoneyId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getUnitMoneyId() {
        return unitMoneyId;
    }

    /**
     * Sets the value of the unitMoneyId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setUnitMoneyId(Long value) {
        this.unitMoneyId = value;
    }

    /**
     * Gets the value of the url property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the value of the url property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUrl(String value) {
        this.url = value;
    }

}
