package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QPartnerContract is a Querydsl query type for PartnerContract
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QPartnerContract extends EntityPathBase<PartnerContract> {

    private static final long serialVersionUID = -534100376L;

    public static final QPartnerContract partnerContract = new QPartnerContract("partnerContract");

    public final StringPath actionCode = createString("actionCode");

    public final StringPath contractCode = createString("contractCode");

    public final DateTimePath<java.util.Date> contractDate = createDateTime("contractDate", java.util.Date.class);

    public final NumberPath<Short> contractStatus = createNumber("contractStatus", Short.class);

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final NumberPath<Long> currencyRate = createNumber("currencyRate", Long.class);

    public final StringPath currencyType = createString("currencyType");

    public final StringPath deliveryLocation = createString("deliveryLocation");

    public final DateTimePath<java.util.Date> importDate = createDateTime("importDate", java.util.Date.class);

    public final DateTimePath<java.util.Date> kcsDate = createDateTime("kcsDate", java.util.Date.class);

    public final StringPath kcsNo = createString("kcsNo");

    public final DateTimePath<java.util.Date> lastModified = createDateTime("lastModified", java.util.Date.class);

    public final StringPath orderCode = createString("orderCode");

    public final NumberPath<Long> partnerContractId = createNumber("partnerContractId", Long.class);

    public final NumberPath<Long> partnerId = createNumber("partnerId", Long.class);

    public final StringPath poCode = createString("poCode");

    public final DateTimePath<java.util.Date> poDate = createDateTime("poDate", java.util.Date.class);

    public final NumberPath<Long> prodOfferId = createNumber("prodOfferId", Long.class);

    public final DateTimePath<java.util.Date> requestDate = createDateTime("requestDate", java.util.Date.class);

    public final DateTimePath<java.util.Date> startDateWarranty = createDateTime("startDateWarranty", java.util.Date.class);

    public final NumberPath<Long> stockTransId = createNumber("stockTransId", Long.class);

    public final NumberPath<Long> unitPrice = createNumber("unitPrice", Long.class);

    public QPartnerContract(String variable) {
        super(PartnerContract.class, forVariable(variable));
    }

    public QPartnerContract(Path<? extends PartnerContract> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPartnerContract(PathMetadata<?> metadata) {
        super(PartnerContract.class, metadata);
    }

}

