package com.viettel.bccs.inventory.model;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.DateTimePath;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;

import javax.annotation.Generated;
import java.util.Date;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * QStockTransFullOffline is a Querydsl query type for StockTransFullOffline
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockTransFullOffline extends EntityPathBase<StockTransFullOffline> {

    private static final long serialVersionUID = -570338896L;

    public static final QStockTransFullOffline stockTransFullOffline = new QStockTransFullOffline("stockTransFullOffline");

    public final StringPath actionCode = createString("actionCode");

    public final NumberPath<Long> actionStaffId = createNumber("actionStaffId", Long.class);

    public final StringPath actionStatus = createString("actionStatus");

    public final NumberPath<Long> basisPrice = createNumber("basisPrice", Long.class);

    public final NumberPath<Long> checkDeposit = createNumber("checkDeposit", Long.class);

    public final NumberPath<Long> checkSerial = createNumber("checkSerial", Long.class);

    public final DateTimePath<Date> createDatetime = createDateTime("createDatetime", Date.class);

    public final StringPath createUser = createString("createUser");

    public final StringPath depositStatus = createString("depositStatus");

    public final NumberPath<Long> fromOwnerId = createNumber("fromOwnerId", Long.class);

    public final NumberPath<Long> fromOwnerType = createNumber("fromOwnerType", Long.class);

    public final StringPath note = createString("note");

    public final StringPath payStatus = createString("payStatus");

    public final StringPath prodOfferCode = createString("prodOfferCode");

    public final NumberPath<Long> prodOfferId = createNumber("prodOfferId", Long.class);

    public final StringPath prodOfferName = createString("prodOfferName");

    public final NumberPath<Long> productOfferTypeId = createNumber("productOfferTypeId", Long.class);

    public final StringPath productOfferTypeName = createString("productOfferTypeName");

    public final NumberPath<Long> quantity = createNumber("quantity", Long.class);

    public final NumberPath<Long> reasonId = createNumber("reasonId", Long.class);

    public final NumberPath<Long> stateId = createNumber("stateId", Long.class);

    public final StringPath stateName = createString("stateName");

    public final StringPath status = createString("status");

    public final NumberPath<Long> stockTransActionId = createNumber("stockTransActionId", Long.class);

    public final NumberPath<Long> stockTransDetailId = createNumber("stockTransDetailId", Long.class);

    public final NumberPath<Long> stockTransId = createNumber("stockTransId", Long.class);

    public final StringPath stockTransType = createString("stockTransType");

    public final NumberPath<Long> toOwnerId = createNumber("toOwnerId", Long.class);

    public final NumberPath<Long> toOwnerType = createNumber("toOwnerType", Long.class);

    public final StringPath unit = createString("unit");

    public QStockTransFullOffline(String variable) {
        super(StockTransFullOffline.class, forVariable(variable));
    }

    public QStockTransFullOffline(Path<? extends StockTransFullOffline> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockTransFullOffline(PathMetadata<?> metadata) {
        super(StockTransFullOffline.class, metadata);
    }

}

