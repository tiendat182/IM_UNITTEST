package com.viettel.bccs.inventory.dto;

import com.google.common.collect.Lists;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.dto.BaseDTO;

import java.util.Date;
import java.util.List;

/**
 * dto hung data tu webservice cua KTTS
 * @author thanhnt77
 */
public class PartnerShipmentWsDTO extends BaseDTO {

    private Date cntSignDate;
    private String contractCode;
    private String currencyCode;
    @XStreamImplicit(itemFieldName = "merEntityBo")
    private List<PartnerShipmentMerEntityWsDTO> lsMerEntityBo;
    private String partnerName;
    private String reportKCSCode;
    private Date reportKCSDate;
    private String reportKCSStatus;
    private String reportKCSName;
    private Date shipCreateDate;
    private String shipmentCode;
    private String nameApprove;
    private String statusApprove;
    private String statusBccs;
    private String statusBccsName;

    public boolean isShowDetailBccs() {
        return "1".equals(statusApprove) && "1".equals(reportKCSStatus) && (!"1".equals(statusBccs) && !"2".equals(statusBccs)  && !"3".equals(statusBccs));
    }
    public PartnerShipmentWsDTO() {
    }

    /**
     * ham xu ly convert kieu du lieu loai mat hang cua KTTS sang kieu dieu lieu offring
     * @author thanhnt77
     * @return
     */
    public List<ProductOfferingDTO> convertToLsProductOffering() {
//        Su ly code tam
        if (!DataUtil.isNullOrEmpty(lsMerEntityBo)) {
            List<ProductOfferingDTO> lsResult = Lists.newArrayList();
            for (PartnerShipmentMerEntityWsDTO dto : lsMerEntityBo) {
                ProductOfferingDTO offerDto = new ProductOfferingDTO();
                offerDto.setProductOfferingId(dto.getMerId());
                offerDto.setCode(dto.getShipmentNo());
                offerDto.setPrice(DataUtil.safeToDouble(dto.getUnitPrice()).longValue());
                offerDto.setStatus(DataUtil.safeToString(dto.getStatusId()));
                offerDto.setProductOfferTypeId(Const.PRODUCT_OFFER_TYPE.KIT);
                offerDto.setQuantity(DataUtil.safeToDouble(dto.getCount()).longValue());
                offerDto.setName(dto.getShipmentNo());
                lsResult.add(offerDto);
            }
            return lsResult;
        }
        return Lists.newArrayList();
    }

    public Date getCntSignDate() {
        return cntSignDate;
    }

    public void setCntSignDate(Date cntSignDate) {
        this.cntSignDate = cntSignDate;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public List<PartnerShipmentMerEntityWsDTO> getLsMerEntityBo() {
        return lsMerEntityBo;
    }

    public void setLsMerEntityBo(List<PartnerShipmentMerEntityWsDTO> lsMerEntityBo) {
        this.lsMerEntityBo = lsMerEntityBo;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getReportKCSCode() {
        return reportKCSCode;
    }

    public void setReportKCSCode(String reportKCSCode) {
        this.reportKCSCode = reportKCSCode;
    }

    public String getReportKCSStatus() {
        return reportKCSStatus;
    }

    public void setReportKCSStatus(String reportKCSStatus) {
        this.reportKCSStatus = reportKCSStatus;
    }

    public Date getShipCreateDate() {
        return shipCreateDate;
    }

    public void setShipCreateDate(Date shipCreateDate) {
        this.shipCreateDate = shipCreateDate;
    }

    public String getShipmentCode() {
        return shipmentCode;
    }

    public void setShipmentCode(String shipmentCode) {
        this.shipmentCode = shipmentCode;
    }

    public String getNameApprove() {
        return nameApprove;
    }

    public void setNameApprove(String nameApprove) {
        this.nameApprove = nameApprove;
    }

    public String getStatusBccs() {
        return statusBccs;
    }

    public void setStatusBccs(String statusBccs) {
        this.statusBccs = statusBccs;
    }

    public String getStatusApprove() {
        return statusApprove;
    }

    public void setStatusApprove(String statusApprove) {
        this.statusApprove = statusApprove;
    }

    public String getReportKCSName() {
        return reportKCSName;
    }

    public void setReportKCSName(String reportKCSName) {
        this.reportKCSName = reportKCSName;
    }

    public Date getReportKCSDate() {
        return reportKCSDate;
    }

    public void setReportKCSDate(Date reportKCSDate) {
        this.reportKCSDate = reportKCSDate;
    }

    public String getStatusBccsName() {
        return statusBccsName;
    }

    public void setStatusBccsName(String statusBccsName) {
        this.statusBccsName = statusBccsName;
    }
}
