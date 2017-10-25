package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;

import javax.annotation.Generated;

import com.mysema.query.types.Path;


/**
 * QImportPartnerRequest is a Querydsl query type for ImportPartnerRequest
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QImportPartnerRequest extends EntityPathBase<ImportPartnerRequest> {

    private static final long serialVersionUID = 1638666238L;

    public static final QImportPartnerRequest importPartnerRequest = new QImportPartnerRequest("importPartnerRequest");

    public final NumberPath<Long> approveStaffId = createNumber("approveStaffId", Long.class);

    public final StringPath contractCode = createString("contractCode");

    public final DateTimePath<java.util.Date> contractDate = createDateTime("contractDate", java.util.Date.class);

    public final DateTimePath<java.util.Date> contractImportDate = createDateTime("contractImportDate", java.util.Date.class);

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final NumberPath<Long> createShopId = createNumber("createShopId", Long.class);

    public final NumberPath<Long> createStaffId = createNumber("createStaffId", Long.class);

    public final StringPath currencyType = createString("currencyType");

    public final StringPath deliveryLocation = createString("deliveryLocation");

    public final StringPath documentContent = createString("documentContent");

    public final StringPath documentName = createString("documentName");

    public final DateTimePath<java.util.Date> importDate = createDateTime("importDate", java.util.Date.class);

    public final NumberPath<Long> importPartnerRequestId = createNumber("importPartnerRequestId", Long.class);

    public final NumberPath<Long> importStaffId = createNumber("importStaffId", Long.class);

    public final DateTimePath<java.util.Date> lastModify = createDateTime("lastModify", java.util.Date.class);

    public final NumberPath<Long> partnerId = createNumber("partnerId", Long.class);

    public final StringPath poCode = createString("poCode");

    public final DateTimePath<java.util.Date> poDate = createDateTime("poDate", java.util.Date.class);

    public final StringPath reason = createString("reason");

    public final StringPath requestCode = createString("requestCode");

    public final DateTimePath<java.util.Date> requestDate = createDateTime("requestDate", java.util.Date.class);

    public final NumberPath<Long> status = createNumber("status", Long.class);

    public final NumberPath<Long> toOwnerId = createNumber("toOwnerId", Long.class);

    public final NumberPath<Long> toOwnerType = createNumber("toOwnerType", Long.class);

    public final NumberPath<Long> unitPrice = createNumber("unitPrice", Long.class);

    public QImportPartnerRequest(String variable) {
        super(ImportPartnerRequest.class, forVariable(variable));
    }

    public QImportPartnerRequest(Path<? extends ImportPartnerRequest> path) {
        super(path.getType(), path.getMetadata());
    }

    public QImportPartnerRequest(PathMetadata<?> metadata) {
        super(ImportPartnerRequest.class, metadata);
    }

}

