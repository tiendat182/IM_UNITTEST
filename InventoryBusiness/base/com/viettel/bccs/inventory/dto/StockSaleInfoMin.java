package com.viettel.bccs.inventory.dto;

import java.io.Serializable;

/**
 * Created by DungPT16 on 1/15/2016.
 */
public class StockSaleInfoMin implements Serializable {
    private Long productOfferingId;
    protected Long quantity = 0L;
    private Long productOfferPriceId;
    private Long prodPackageId;//ban theo bo

    public Long getProdPackageId() {
        return prodPackageId;
    }

    public void setProdPackageId(Long prodPackageId) {
        this.prodPackageId = prodPackageId;
    }

    public Long getProductOfferPriceId() {
        return productOfferPriceId;
    }

    public void setProductOfferPriceId(Long productOfferPriceId) {
        this.productOfferPriceId = productOfferPriceId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getProductOfferingId() {
        return productOfferingId;
    }

    public void setProductOfferingId(Long productOfferingId) {
        this.productOfferingId = productOfferingId;
    }
}
