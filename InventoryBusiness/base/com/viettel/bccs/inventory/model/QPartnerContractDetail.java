package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QPartnerContractDetail is a Querydsl query type for PartnerContractDetail
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QPartnerContractDetail extends EntityPathBase<PartnerContractDetail> {

    private static final long serialVersionUID = -2076910375L;

    public static final QPartnerContractDetail partnerContractDetail = new QPartnerContractDetail("partnerContractDetail");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final StringPath currencyType = createString("currencyType");

    public final StringPath orderCode = createString("orderCode");

    public final NumberPath<Long> partnerContractDetailId = createNumber("partnerContractDetailId", Long.class);

    public final NumberPath<Long> partnerContractId = createNumber("partnerContractId", Long.class);

    public final NumberPath<Long> prodOfferId = createNumber("prodOfferId", Long.class);

    public final NumberPath<Long> quantity = createNumber("quantity", Long.class);

    public final DateTimePath<java.util.Date> startDateWarranty = createDateTime("startDateWarranty", java.util.Date.class);

    public final NumberPath<Long> stateId = createNumber("stateId", Long.class);

    public final NumberPath<Long> unitPrice = createNumber("unitPrice", Long.class);

    public QPartnerContractDetail(String variable) {
        super(PartnerContractDetail.class, forVariable(variable));
    }

    public QPartnerContractDetail(Path<? extends PartnerContractDetail> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPartnerContractDetail(PathMetadata<?> metadata) {
        super(PartnerContractDetail.class, metadata);
    }

}

