package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QRevokeTransDetail is a Querydsl query type for RevokeTransDetail
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QRevokeTransDetail extends EntityPathBase<RevokeTransDetail> {

    private static final long serialVersionUID = -1599562847L;

    public static final QRevokeTransDetail revokeTransDetail = new QRevokeTransDetail("revokeTransDetail");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final StringPath description = createString("description");

    public final StringPath errCode = createString("errCode");

    public final NumberPath<Long> oldOwnerId = createNumber("oldOwnerId", Long.class);

    public final NumberPath<Short> oldOwnerType = createNumber("oldOwnerType", Short.class);

    public final StringPath oldSerial = createString("oldSerial");

    public final NumberPath<Long> prodOfferId = createNumber("prodOfferId", Long.class);

    public final NumberPath<Long> prodOfferTypeId = createNumber("prodOfferTypeId", Long.class);

    public final NumberPath<Long> quantity = createNumber("quantity", Long.class);

    public final StringPath revokedSerial = createString("revokedSerial");

    public final NumberPath<Long> revokePrice = createNumber("revokePrice", Long.class);

    public final NumberPath<Long> revokeTransDetailId = createNumber("revokeTransDetailId", Long.class);

    public final NumberPath<Long> revokeTransId = createNumber("revokeTransId", Long.class);

    public QRevokeTransDetail(String variable) {
        super(RevokeTransDetail.class, forVariable(variable));
    }

    public QRevokeTransDetail(Path<? extends RevokeTransDetail> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRevokeTransDetail(PathMetadata<?> metadata) {
        super(RevokeTransDetail.class, metadata);
    }

}

