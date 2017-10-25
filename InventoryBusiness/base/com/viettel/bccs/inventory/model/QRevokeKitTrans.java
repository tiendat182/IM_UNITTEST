package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;

import javax.annotation.Generated;

import com.mysema.query.types.Path;


/**
 * QRevokeKitTrans is a Querydsl query type for RevokeKitTrans
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QRevokeKitTrans extends EntityPathBase<RevokeKitTrans> {

    private static final long serialVersionUID = -328383510L;

    public static final QRevokeKitTrans revokeKitTrans = new QRevokeKitTrans("revokeKitTrans");

    public final DateTimePath<java.util.Date> addMoneyDate = createDateTime("addMoneyDate", java.util.Date.class);

    public final NumberPath<Long> addMoneyStatus = createNumber("addMoneyStatus", Long.class);

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final NumberPath<Long> createStaffId = createNumber("createStaffId", Long.class);

    public final NumberPath<Long> createShopId = createNumber("createShopId", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QRevokeKitTrans(String variable) {
        super(RevokeKitTrans.class, forVariable(variable));
    }

    public QRevokeKitTrans(Path<? extends RevokeKitTrans> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRevokeKitTrans(PathMetadata<?> metadata) {
        super(RevokeKitTrans.class, metadata);
    }

}

