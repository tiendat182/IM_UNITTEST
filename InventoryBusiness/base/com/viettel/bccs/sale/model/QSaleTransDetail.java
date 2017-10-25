package com.viettel.bccs.sale.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import javax.persistence.ManyToOne;

import com.mysema.query.types.Path;
import com.viettel.bccs.inventory.model.OptionSetValue;


/**
 * QSaleTransDetail is a Querydsl query type for SaleTransDetail
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QSaleTransDetail extends EntityPathBase<SaleTransDetail> {

    private static final long serialVersionUID = 1649177473L;

    public static final QSaleTransDetail saleTransDetail = new QSaleTransDetail("saleTransDetail");

    public final StringPath account = createString("account");

    public final StringPath accountingModelCode = createString("accountingModelCode");

    public final StringPath accountingModelName = createString("accountingModelName");

    public final NumberPath<Long> amount = createNumber("amount", Long.class);

    public final DateTimePath<java.util.Date> deliverDate = createDateTime("deliverDate", java.util.Date.class);

    public final StringPath deliverStatus = createString("deliverStatus");

    public final NumberPath<Long> discountAmount = createNumber("discountAmount", Long.class);

    public final NumberPath<Long> discountId = createNumber("discountId", Long.class);

    public final DateTimePath<java.util.Date> expiredDate = createDateTime("expiredDate", java.util.Date.class);

    public final StringPath inTransId = createString("inTransId");

    public final NumberPath<Long> modelProgramId = createNumber("modelProgramId", Long.class);

    public final StringPath modelProgramName = createString("modelProgramName");

    public final StringPath note = createString("note");

    public final NumberPath<Long> posId = createNumber("posId", Long.class);

    public final NumberPath<Long> price = createNumber("price", Long.class);

    public final NumberPath<Long> priceId = createNumber("priceId", Long.class);

    public final NumberPath<Long> priceVat = createNumber("priceVat", Long.class);

    public final NumberPath<Long> promotionAmount = createNumber("promotionAmount", Long.class);

    public final NumberPath<Long> promotionId = createNumber("promotionId", Long.class);

    public final NumberPath<Long> quantity = createNumber("quantity", Long.class);

    public final StringPath revenueType = createString("revenueType");

    public final StringPath saleServicesCode = createString("saleServicesCode");

    public final NumberPath<Long> saleServicesId = createNumber("saleServicesId", Long.class);

    public final StringPath saleServicesName = createString("saleServicesName");

    public final NumberPath<Long> saleServicesPrice = createNumber("saleServicesPrice", Long.class);

    public final NumberPath<Long> saleServicesPriceId = createNumber("saleServicesPriceId", Long.class);

    public final NumberPath<Long> saleServicesPriceVat = createNumber("saleServicesPriceVat", Long.class);

    public final NumberPath<Long> saleServicesProgramId = createNumber("saleServicesProgramId", Long.class);

    public final StringPath saleServicesProgramName = createString("saleServicesProgramName");

    public final DateTimePath<java.util.Date> saleTransDate = createDateTime("saleTransDate", java.util.Date.class);

    public final NumberPath<Long> saleTransDetailId = createNumber("saleTransDetailId", Long.class);

    public final NumberPath<Long> saleTransId = createNumber("saleTransId", Long.class);

    public final NumberPath<Long> stateId = createNumber("stateId", Long.class);

    public final StringPath stockModelCode = createString("stockModelCode");

    public final NumberPath<Long> stockModelId = createNumber("stockModelId", Long.class);

    public final StringPath stockModelName = createString("stockModelName");

    public final StringPath stockTypeCode = createString("stockTypeCode");

    public final NumberPath<Long> stockTypeId = createNumber("stockTypeId", Long.class);

    public final StringPath stockTypeName = createString("stockTypeName");

    public final StringPath supplyMethod = createString("supplyMethod");

    public final NumberPath<Long> supplyMonth = createNumber("supplyMonth", Long.class);

    public final StringPath supplyProgram = createString("supplyProgram");

    public final StringPath transferGood = createString("transferGood");

    public final StringPath updateStockType = createString("updateStockType");

    public final NumberPath<Long> userDeliver = createNumber("userDeliver", Long.class);

    public final NumberPath<Long> userUpdate = createNumber("userUpdate", Long.class);

    public final NumberPath<Long> vat = createNumber("vat", Long.class);

    public final NumberPath<Long> vatAmount = createNumber("vatAmount", Long.class);

    @ManyToOne
    OptionSetValue optionSetValue;

    public QSaleTransDetail(String variable) {
        super(SaleTransDetail.class, forVariable(variable));
    }

    public QSaleTransDetail(Path<? extends SaleTransDetail> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSaleTransDetail(PathMetadata<?> metadata) {
        super(SaleTransDetail.class, metadata);
    }



}

