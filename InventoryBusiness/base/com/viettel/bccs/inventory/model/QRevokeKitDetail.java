package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;

import javax.annotation.Generated;

import com.mysema.query.types.Path;


/**
 * QRevokeKitDetail is a Querydsl query type for RevokeKitDetail
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QRevokeKitDetail extends EntityPathBase<RevokeKitDetail> {

    private static final long serialVersionUID = -2059473073L;

    public static final QRevokeKitDetail revokeKitDetail = new QRevokeKitDetail("revokeKitDetail");

    public final NumberPath<Long> accountBookBankplusId = createNumber("accountBookBankplusId", Long.class);

    public final StringPath actStatus = createString("actStatus");

    public final StringPath addMoneyResult = createString("addMoneyResult");

    public final NumberPath<Long> addMoneyStatus = createNumber("addMoneyStatus", Long.class);

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final StringPath dataProductCode = createString("dataProductCode");

    public final DateTimePath<java.util.Date> dataRegisterDate = createDateTime("dataRegisterDate", java.util.Date.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath isdn = createString("isdn");

    public final DateTimePath<java.util.Date> lastModify = createDateTime("lastModify", java.util.Date.class);

    public final NumberPath<Long> mainBalance = createNumber("mainBalance", Long.class);

    public final StringPath orgOwnerCode = createString("orgOwnerCode");

    public final NumberPath<Long> orgOwnerId = createNumber("orgOwnerId", Long.class);

    public final NumberPath<Long> orgOwnerType = createNumber("orgOwnerType", Long.class);

    public final NumberPath<Long> price = createNumber("price", Long.class);

    public final NumberPath<Long> prodOfferId = createNumber("prodOfferId", Long.class);

    public final StringPath productCode = createString("productCode");

    public final NumberPath<Long> promotionBalance = createNumber("promotionBalance", Long.class);

    public final NumberPath<Long> registerStatus = createNumber("registerStatus", Long.class);

    public final DateTimePath<java.util.Date> revokeDate = createDateTime("revokeDate", java.util.Date.class);

    public final StringPath revokeDescription = createString("revokeDescription");

    public final NumberPath<Long> revokeKitTransId = createNumber("revokeKitTransId", Long.class);

    public final NumberPath<Long> revokeStatus = createNumber("revokeStatus", Long.class);

    public final NumberPath<Long> revokeType = createNumber("revokeType", Long.class);

    public final DateTimePath<java.util.Date> saleDate = createDateTime("saleDate", java.util.Date.class);

    public final NumberPath<Long> saleDiscount = createNumber("saleDiscount", Long.class);

    public final NumberPath<Long> salePrice = createNumber("salePrice", Long.class);

    public final NumberPath<Long> saleTransDetailId = createNumber("saleTransDetailId", Long.class);

    public final NumberPath<Long> saleTransId = createNumber("saleTransId", Long.class);

    public final NumberPath<Long> saleTransRevokeId = createNumber("saleTransRevokeId", Long.class);

    public final StringPath serial = createString("serial");

    public final DateTimePath<java.util.Date> staDatetime = createDateTime("staDatetime", java.util.Date.class);

    public final NumberPath<Long> stage = createNumber("stage", Long.class);

    public final NumberPath<Long> stockTransId = createNumber("stockTransId", Long.class);

    public final NumberPath<Long> subId = createNumber("subId", Long.class);

    public final StringPath subStatus = createString("subStatus");

    public final NumberPath<Long> tk10 = createNumber("tk10", Long.class);

    public final NumberPath<Long> tk27 = createNumber("tk27", Long.class);

    public final NumberPath<Long> tk34 = createNumber("tk34", Long.class);

    public final NumberPath<Long> tk46 = createNumber("tk46", Long.class);

    public final NumberPath<Long> tkData = createNumber("tkData", Long.class);

    public final DateTimePath<java.util.Date> verifyDate = createDateTime("verifyDate", java.util.Date.class);

    public final StringPath verifyDescription = createString("verifyDescription");

    public final NumberPath<Long> verifyStatus = createNumber("verifyStatus", Long.class);

    public final NumberPath<Long> verifyType = createNumber("verifyType", Long.class);

    public final StringPath reasonCode = createString("reasonCode");

    public QRevokeKitDetail(String variable) {
        super(RevokeKitDetail.class, forVariable(variable));
    }

    public QRevokeKitDetail(Path<? extends RevokeKitDetail> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRevokeKitDetail(PathMetadata<?> metadata) {
        super(RevokeKitDetail.class, metadata);
    }

}

