package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.QPartner;
import com.viettel.bccs.inventory.service.DeviceConfigService;
import com.viettel.bccs.inventory.service.ImsiMadeService;
import com.viettel.bccs.inventory.service.PartnerService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.BundleUtil;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.web.common.controller.BaseController;
import org.apache.commons.lang.math.NumberUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.social.FacebookAutoConfiguration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by vanho on 29/05/2017.
 */
@Component
@Scope("view")
@ManagedBean
public class ChooseProduceImsiRangeController extends BaseController {

    private List<OutputImsiProduceDTO> resultSearch;
    private List<ImsiMadeDTO> resultSearchDetail;
    private List<PartnerDTO> partnerDTOS;
    private ImsiMadeDTO currentImsiMade;
    private String fromImsi;
    private String toImsi;
    private String docCode;
    private Date fromDate;
    private Date toDate;
    private Long status;
    private Date searchDetailFromDate;
    private Date searchDetailToDate;

    private String fileContent;

    private final String prefixSerialNo = "898404800";
    private final String prefixImsi = "45204";

    private ProductOfferingTotalDTO productOfferingTotalDTO = new ProductOfferingTotalDTO();
    private List<ProductOfferingTotalDTO> lsProductOfferingTotalDTO = Lists.newArrayList();

    @Autowired
    private ImsiMadeService imsiMadeService;
    @Autowired
    private DeviceConfigService deviceConfigService;

    @PostConstruct
    public void init(){
        resultSearch = new ArrayList<>();
        currentImsiMade = new ImsiMadeDTO();
        partnerDTOS = new ArrayList<>();
    }

    public void validateBeforeCreate(){
        boolean isOK = true;

        if(DataUtil.isNullOrEmpty(currentImsiMade.getPo())){
            FacesContext.getCurrentInstance().addMessage("frmAddImsiRange:docCode", new FacesMessage(FacesMessage.SEVERITY_ERROR, null, getText("imsiRangeProceduce.imsiRange.requireField")));
            isOK = false;
        } else {
            Pattern pattern = Pattern.compile("[\\w\\d_\\-\\/\\\\]+");
            if(!pattern.matcher(DataUtil.convertCharacter(currentImsiMade.getPo())).matches()){
                FacesContext.getCurrentInstance().addMessage("frmAddImsiRange:docCode", new FacesMessage(FacesMessage.SEVERITY_ERROR, null, getText("imsiRangeProceduce.imsiRange.specialCharaterErr")));
                isOK = false;
            }
        }

        if(currentImsiMade.getProdOfferId() == null){
            FacesContext.getCurrentInstance().addMessage("frmAddImsiRange:productOfferAdd", new FacesMessage(FacesMessage.SEVERITY_ERROR, null, getText("imsiRangeProceduce.imsiRange.requireField")));
            isOK = false;
        }

        if(DataUtil.isNullOrEmpty(currentImsiMade.getSimType())){
            FacesContext.getCurrentInstance().addMessage("frmAddImsiRange:cbxTypeInput", new FacesMessage(FacesMessage.SEVERITY_ERROR, null, getText("imsiRangeProceduce.imsiRange.requireField")));
            isOK = false;
        }

        if(DataUtil.isNullOrEmpty(currentImsiMade.getFromImsi())){
            FacesContext.getCurrentInstance().addMessage("frmAddImsiRange:fromImsi", new FacesMessage(FacesMessage.SEVERITY_ERROR, null, getText("imsiRangeProceduce.imsiRange.requireField")));
            isOK = false;
        } else {
            if(!NumberUtils.isNumber(currentImsiMade.getFromImsi()) || (NumberUtils.isNumber(currentImsiMade.getFromImsi()) && NumberUtils.toLong(currentImsiMade.getFromImsi()) < 0)){
                FacesContext.getCurrentInstance().addMessage("frmAddImsiRange:fromImsi", new FacesMessage(FacesMessage.SEVERITY_ERROR, null, getText("imsiRangeProceduce.imsiRange.requireNumber")));
                isOK = false;
            }
        }

        /*if(DataUtil.isNullOrEmpty(currentImsiMade.getToImsi())){
            FacesContext.getCurrentInstance().addMessage("frmAddImsiRange:toImsi", new FacesMessage(FacesMessage.SEVERITY_ERROR, null, getText("imsiRangeProceduce.imsiRange.requireField")));
            isOK = false;
        } else {
            if(!NumberUtils.isNumber(currentImsiMade.getToImsi())){
                FacesContext.getCurrentInstance().addMessage("frmAddImsiRange:toImsi", new FacesMessage(FacesMessage.SEVERITY_ERROR, null, getText("imsiRangeProceduce.imsiRange.requireNumber")));
                isOK = false;
            }
        }*/

        if(currentImsiMade.getQuantity() == null){
            FacesContext.getCurrentInstance().addMessage("frmAddImsiRange:quantity", new FacesMessage(FacesMessage.SEVERITY_ERROR, null, getText("imsiRangeProceduce.imsiRange.requireField")));
            isOK = false;
        } else if(currentImsiMade.getQuantity() < 1){
            FacesContext.getCurrentInstance().addMessage("frmAddImsiRange:quantity", new FacesMessage(FacesMessage.SEVERITY_ERROR, null, getText("imsiRangeProceduce.imsiRange.inValidQuantity")));
            isOK = false;
        }

        if(DataUtil.isNullOrEmpty(currentImsiMade.getOpKey())){
            FacesContext.getCurrentInstance().addMessage("frmAddImsiRange:partner", new FacesMessage(FacesMessage.SEVERITY_ERROR, null, getText("imsiRangeProceduce.imsiRange.requireField")));
            isOK = false;
        }

        if(isOK){
            try {
                Long quantity = imsiMadeService.checkImsiRange(currentImsiMade.getFromImsi(), currentImsiMade.getToImsi());
                if(!currentImsiMade.getQuantity().equals(quantity)){
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, getText("imsiRangeProceduce.imsiRange.imsiRangeInvalid")));
                    isOK = false;
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, getText("common.error.process.happened")));
                isOK = false;
            }
        }

        if(!isOK) {
            FacesContext.getCurrentInstance().validationFailed();
            topPage();
        } else {
            currentImsiMade.setToImsi((NumberUtils.toLong(currentImsiMade.getFromImsi()) + currentImsiMade.getQuantity()) - 1 + "");
        }
    }

    public void calQuantity(){

        /*if(!DataUtil.isNullOrEmpty(currentImsiMade.getFromImsi())){
            if(DataUtil.isNullOrEmpty(currentImsiMade.getToImsi()) && !DataUtil.isNullOrEmpty())
        }*/

        if(!DataUtil.isNullOrEmpty(currentImsiMade.getFromImsi()) && NumberUtils.isNumber(currentImsiMade.getFromImsi())
                && NumberUtils.toLong(currentImsiMade.getFromImsi()) >=0 && currentImsiMade.getQuantity() != null
                && currentImsiMade.getQuantity() > 0)
            currentImsiMade.setToImsi((NumberUtils.toLong(currentImsiMade.getFromImsi()) + currentImsiMade.getQuantity() - 1) + "");
    }

    @Secured("@")
    public StreamedContent downloadFileAttach(ImsiMadeDTO imsiMadeDTO) {
        try {
            InputStream is = new ByteArrayInputStream(imsiMadeDTO.getContent());
            return new DefaultStreamedContent(is, "application/txt",
                    getTextParam("create.imsi.ranges.fileName", imsiMadeDTO.getId() + "")+".txt");
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
        }
        return null;
    }

    @Secured("@")
    public StreamedContent getDownloadFileAttachWhenCreateSuccess() {
        try {
            InputStream is = new ByteArrayInputStream(fileContent.getBytes("UTF-8"));
            return new DefaultStreamedContent(is, "application/txt",
                    getTextParam("create.imsi.ranges.fileName", "1.txt"));
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
        }
        return null;
    }

    public void clickDownloadFile(){
        RequestContext.getCurrentInstance().execute("clickLinkByClass('downloadFileImsi')");
    }

    public void doCreate(){

        try{
            currentImsiMade.setSerialNo(currentImsiMade.getFromImsi().replace(prefixImsi, prefixSerialNo));
            fileContent = getTextParam("create.imsi.ranges.content", currentImsiMade.getQuantity().toString(),currentImsiMade.getProdOfferName(), Const.PRODUCE_IMSI_RANGE.GSM_TYPE.equals(currentImsiMade.getSimType()) ? Const.PRODUCE_IMSI_RANGE.GSM_TRANSPORT_KEY : Const.PRODUCE_IMSI_RANGE.USIM_TRANSPORT_KEY, currentImsiMade.getOpKey(), currentImsiMade.getPo(), currentImsiMade.getFromImsi(), currentImsiMade.getSerialNo());
            currentImsiMade.setContent(fileContent.getBytes("UTF-8"));
            imsiMadeService.doCreate(currentImsiMade, BccsLoginSuccessHandler.getUserName());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null, getText("mn.invoice.invoiceType.success.add")));
            RequestContext.getCurrentInstance().execute("PF('dlgAddImsiRange').hide()");
            doSearch();
            RequestContext.getCurrentInstance().update("frmSearchImsi:pnlResult");
        } catch (LogicException ex){
            fileContent = "";
            logger.error(ex.getMessage(), ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, getText(ex.getKeyMsg())));
            topPage();
            FacesContext.getCurrentInstance().validationFailed();
        } catch (Exception e){
            fileContent = "";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, getText("common.error.happened")));
            logger.error(e.getMessage(), e);
            topPage();
            FacesContext.getCurrentInstance().validationFailed();
        }

    }

    public void doSearch(){
        try {
            boolean isOK = true;
            if(!DataUtil.isNullOrEmpty(fromImsi) && !NumberUtils.isNumber(fromImsi)){
                FacesContext.getCurrentInstance().addMessage("frmSearchImsi:firstImsi", new FacesMessage(FacesMessage.SEVERITY_ERROR, null, getText("imsiRangeProceduce.imsiRange.requireNumber")));
                isOK = false;
            }

            if(!DataUtil.isNullOrEmpty(toImsi) && !NumberUtils.isNumber(toImsi)){
                FacesContext.getCurrentInstance().addMessage("frmSearchImsi:endImsi", new FacesMessage(FacesMessage.SEVERITY_ERROR, null, getText("imsiRangeProceduce.imsiRange.requireNumber")));
                isOK = false;
            }

            Pattern pattern = Pattern.compile("[\\w\\d_\\-\\/\\\\]+");
            if(!DataUtil.isNullOrEmpty(docCode) && !pattern.matcher(DataUtil.convertCharacter(docCode)).matches()){
                FacesContext.getCurrentInstance().addMessage("frmSearchImsi:docCode", new FacesMessage(FacesMessage.SEVERITY_ERROR, null, getText("imsiRangeProceduce.imsiRange.specialCharaterErr")));
                isOK = false;
            }

            if (fromDate != null && toDate != null) {
                if (fromDate.getTime() > toDate.getTime()) {
                    isOK = false;
                    reportError("", "", "imsi.info.from.end.date.invalid");
                }
            }

            if(!isOK)
                return;

            resultSearch = imsiMadeService.getListImsiRange(fromImsi, toImsi, docCode, fromDate, toDate, status);
        } catch (Exception e) {
            reportError("", "common.error.happened", e);
            logger.error(e.getMessage(), e);
            topPage();
        }
    }

    public String getStatusName(Long status) {
        if (status == 1) {
            return BundleUtil.getText("imsi.info.status.1");
        }
        if (status == 2) {
            return BundleUtil.getText("imsi.info.status.2");
        }
        if (status == 3) {
            return BundleUtil.getText("imsi.info.status.3");
        }
        if (status == 4) {
            return BundleUtil.getText("imsi.info.status.4");
        }
        return "";
    }

    public boolean renderChooseRange(OutputImsiProduceDTO outputImsiProduceDTO){
        if(outputImsiProduceDTO.getStatus() != 1L)
            return false;
        Date currentDate = new Date();
        return (DateUtil.compareDateToDate(currentDate, outputImsiProduceDTO.getStartDate()) >= 0 && DateUtil.compareDateToDate(currentDate, outputImsiProduceDTO.getEndDate()) <= 0);
    }

    public void doSearchImsiDetail(){
        try {
            if (searchDetailFromDate == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.from.date.not.blank");
            }
            if (searchDetailToDate == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.to.date.not.blank");
            }
            if ((DateUtil.compareDateToDate(searchDetailFromDate, searchDetailToDate) > 0)
                    || DateUtil.daysBetween2Dates(searchDetailToDate, searchDetailFromDate) > 90L) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "view.stock.offer.cycel.fromDate.endDate", 90);
            }
            resultSearchDetail = imsiMadeService.getImsiRangeByDate(searchDetailFromDate, searchDetailToDate);
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, getTextParam(ex.getKeyMsg(), ex.getParamsMsg())));
            topPage();
        } catch (Exception e) {
            reportError("", "common.error.happened", e);
            logger.error(e.getMessage(), e);
            topPage();
        }
    }

    public void resetFormSearch(){
        toImsi = "";
        fromImsi = "";
        docCode = "";
        fromDate = null;
        toDate = null;
        status = null;
    }

    public void resetFormAdd(){
        currentImsiMade = new ImsiMadeDTO();
        productOfferingTotalDTO = new ProductOfferingTotalDTO();
    }

    public void preToShowAdd(OutputImsiProduceDTO outputImsiProduceDTO){
        fileContent = "";
        currentImsiMade = new ImsiMadeDTO();
        currentImsiMade.setFromImsi(outputImsiProduceDTO.getStartImsi());
        currentImsiMade.setToImsi(outputImsiProduceDTO.getEndImsi());
        currentImsiMade.setQuantity(Long.valueOf(currentImsiMade.getToImsi()) - Long.valueOf(currentImsiMade.getFromImsi()) + 1);
        productOfferingTotalDTO = new ProductOfferingTotalDTO();
        try {
            partnerDTOS = imsiMadeService.getListPartnerA4keyNotNull();
        } catch (Exception e) {
            reportError("", "common.error.happened", e);
            logger.error(e.getMessage(), e);
            topPage();
        }
    }

    public void preToShowSearchDetail(){
        Calendar calendar = Calendar.getInstance();
        resultSearchDetail = new ArrayList<>();
        searchDetailToDate = calendar.getTime();
        calendar.add(Calendar.DATE, -90);
        searchDetailFromDate = calendar.getTime();
    }

    @Secured("@")
    public List<ProductOfferingTotalDTO> doChangeOffering(String inputProduct) {
        try {
            String input = inputProduct != null ? inputProduct.trim() : "";
            lsProductOfferingTotalDTO = DataUtil.defaultIfNull(deviceConfigService.getLsProductOfferingByProductTypeAndState(input, Const.PRODUCE_IMSI_RANGE.PROB_OFFER_TYPE_ID_FOR_SIM, null), new ArrayList<>());
            return lsProductOfferingTotalDTO;
        } catch (Exception e) {
            reportError("", "common.error.happened", e);
            logger.error(e.getMessage(), e);
            topPage();
        }
        return Lists.newArrayList();
    }

    public void resetProductAdd() {
        productOfferingTotalDTO = new ProductOfferingTotalDTO();
        currentImsiMade.setProdOfferId(null);
        currentImsiMade.setProdOfferName(null);
    }

    @Secured("@")
    public void receiveProductAdd(SelectEvent event) {
        String s = ((ProductOfferingTotalDTO)event.getObject()).getProductOfferingId() + "";
        if (lsProductOfferingTotalDTO != null)
            for (ProductOfferingTotalDTO c : lsProductOfferingTotalDTO) {
                if (c != null && c.getProductOfferingId() != null && DataUtil.safeToString(c.getProductOfferingId()).equals(s)) {
                    productOfferingTotalDTO = c;
                    currentImsiMade.setProdOfferId(c.getProductOfferingId());
                    currentImsiMade.setProdOfferName(c.getName());
                    break;
                }
            }
        if (productOfferingTotalDTO == null) {
            productOfferingTotalDTO = new ProductOfferingTotalDTO();
            productOfferingTotalDTO.setProductOfferingId(DataUtil.safeToLong(s));
        }
    }

    public List<OutputImsiProduceDTO> getResultSearch() {
        return resultSearch;
    }

    public void setResultSearch(List<OutputImsiProduceDTO> resultSearch) {
        this.resultSearch = resultSearch;
    }

    public String getFromImsi() {
        return fromImsi;
    }

    public void setFromImsi(String fromImsi) {
        this.fromImsi = fromImsi;
    }

    public String getToImsi() {
        return toImsi;
    }

    public void setToImsi(String toImsi) {
        this.toImsi = toImsi;
    }

    public String getDocCode() {
        return docCode;
    }

    public void setDocCode(String docCode) {
        this.docCode = docCode;
    }

    public ImsiMadeDTO getCurrentImsiMade() {
        return currentImsiMade;
    }

    public void setCurrentImsiMade(ImsiMadeDTO currentImsiMade) {
        this.currentImsiMade = currentImsiMade;
    }

    public Date getSearchDetailFromDate() {
        return searchDetailFromDate;
    }

    public void setSearchDetailFromDate(Date searchDetailFromDate) {
        this.searchDetailFromDate = searchDetailFromDate;
    }

    public Date getSearchDetailToDate() {
        return searchDetailToDate;
    }

    public void setSearchDetailToDate(Date searchDetailToDate) {
        this.searchDetailToDate = searchDetailToDate;
    }

    public List<ImsiMadeDTO> getResultSearchDetail() {
        return resultSearchDetail;
    }

    public void setResultSearchDetail(List<ImsiMadeDTO> resultSearchDetail) {
        this.resultSearchDetail = resultSearchDetail;
    }

    public ProductOfferingTotalDTO getProductOfferingTotalDTO() {
        return productOfferingTotalDTO;
    }

    public void setProductOfferingTotalDTO(ProductOfferingTotalDTO productOfferingTotalDTO) {
        this.productOfferingTotalDTO = productOfferingTotalDTO;
    }

    public List<ProductOfferingTotalDTO> getLsProductOfferingTotalDTO() {
        return lsProductOfferingTotalDTO;
    }

    public void setLsProductOfferingTotalDTO(List<ProductOfferingTotalDTO> lsProductOfferingTotalDTO) {
        this.lsProductOfferingTotalDTO = lsProductOfferingTotalDTO;
    }

    public List<PartnerDTO> getPartnerDTOS() {
        return partnerDTOS;
    }

    public void setPartnerDTOS(List<PartnerDTO> partnerDTOS) {
        this.partnerDTOS = partnerDTOS;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }
}
