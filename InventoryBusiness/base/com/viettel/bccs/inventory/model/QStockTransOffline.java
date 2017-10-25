package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QStockTransOffline is a Querydsl query type for StockTransOffline
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockTransOffline extends EntityPathBase<StockTransOffline> {

    private static final long serialVersionUID = 757602623L;

    public static final QStockTransOffline stockTransOffline = new QStockTransOffline("stockTransOffline");

    public final NumberPath<Short> checkErp = createNumber("checkErp", Short.class);

    public final DateTimePath<java.util.Date> createDatetime = createDateTime("createDatetime", java.util.Date.class);

    public final NumberPath<Short> depositStatus = createNumber("depositStatus", Short.class);

    public final NumberPath<Short> drawStatus = createNumber("drawStatus", Short.class);

    public final NumberPath<Long> fromOwnerId = createNumber("fromOwnerId", Long.class);

    public final NumberPath<Short> fromOwnerType = createNumber("fromOwnerType", Short.class);

    public final NumberPath<Long> fromStockTransId = createNumber("fromStockTransId", Long.class);

    public final NumberPath<java.math.BigDecimal> goodsWeight = createNumber("goodsWeight", java.math.BigDecimal.class);

    public final StringPath logInputType = createString("logInputType");

    public final StringPath logOrderType = createString("logOrderType");

    public final StringPath logOutputType = createString("logOutputType");

    public final StringPath note = createString("note");

    public final NumberPath<Short> payStatus = createNumber("payStatus", Short.class);

    public final DateTimePath<java.util.Date> realTransDate = createDateTime("realTransDate", java.util.Date.class);

    public final NumberPath<Long> realTransUserId = createNumber("realTransUserId", Long.class);

    public final NumberPath<Long> reasonId = createNumber("reasonId", Long.class);

    public final DateTimePath<java.util.Date> rejectDate = createDateTime("rejectDate", java.util.Date.class);

    public final NumberPath<Long> rejectReasonId = createNumber("rejectReasonId", Long.class);

    public final NumberPath<Long> rejectUserId = createNumber("rejectUserId", Long.class);

    public final StringPath requestId = createString("requestId");

    public final StringPath stockBase = createString("stockBase");

    public final NumberPath<Long> stockTransId = createNumber("stockTransId", Long.class);

    public final NumberPath<Short> stockTransStatus = createNumber("stockTransStatus", Short.class);

    public final NumberPath<Short> stockTransType = createNumber("stockTransType", Short.class);

    public final StringPath synStatus = createString("synStatus");

    public final NumberPath<Long> toOwnerId = createNumber("toOwnerId", Long.class);

    public final NumberPath<Short> toOwnerType = createNumber("toOwnerType", Short.class);

    public final NumberPath<Long> totalDebit = createNumber("totalDebit", Long.class);

    public QStockTransOffline(String variable) {
        super(StockTransOffline.class, forVariable(variable));
    }

    public QStockTransOffline(Path<? extends StockTransOffline> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockTransOffline(PathMetadata<?> metadata) {
        super(StockTransOffline.class, metadata);
    }

}

