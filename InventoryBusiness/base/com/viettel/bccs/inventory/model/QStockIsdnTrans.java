package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QStockIsdnTrans is a Querydsl query type for StockIsdnTrans
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockIsdnTrans extends EntityPathBase<StockIsdnTrans> {

    private static final long serialVersionUID = -2007291088L;

    public static final QStockIsdnTrans stockIsdnTrans = new QStockIsdnTrans("stockIsdnTrans");

    public final DateTimePath<java.util.Date> createdTime = createDateTime("createdTime", java.util.Date.class);

    public final StringPath createdUserCode = createString("createdUserCode");

    public final NumberPath<Long> createdUserId = createNumber("createdUserId", Long.class);

    public final StringPath createdUserIp = createString("createdUserIp");

    public final StringPath createdUserName = createString("createdUserName");

    public final StringPath fromOwnerCode = createString("fromOwnerCode");

    public final NumberPath<Long> fromOwnerId = createNumber("fromOwnerId", Long.class);

    public final StringPath fromOwnerName = createString("fromOwnerName");

    public final NumberPath<Long> fromOwnerType = createNumber("fromOwnerType", Long.class);

    public final DateTimePath<java.util.Date> lastUpdatedTime = createDateTime("lastUpdatedTime", java.util.Date.class);

    public final StringPath lastUpdatedUserCode = createString("lastUpdatedUserCode");

    public final NumberPath<Long> lastUpdatedUserId = createNumber("lastUpdatedUserId", Long.class);

    public final StringPath lastUpdatedUserIp = createString("lastUpdatedUserIp");

    public final StringPath lastUpdatedUserName = createString("lastUpdatedUserName");

    public final StringPath note = createString("note");

    public final NumberPath<Long> quantity = createNumber("quantity", Long.class);

    public final StringPath reasonCode = createString("reasonCode");

    public final NumberPath<Long> reasonId = createNumber("reasonId", Long.class);

    public final StringPath reasonName = createString("reasonName");

    public final NumberPath<Long> status = createNumber("status", Long.class);

    public final StringPath stockIsdnTransCode = createString("stockIsdnTransCode");

    public final NumberPath<Long> stockIsdnTransId = createNumber("stockIsdnTransId", Long.class);

    public final NumberPath<Long> stockTypeId = createNumber("stockTypeId", Long.class);

    public final StringPath stockTypeName = createString("stockTypeName");

    public final StringPath toOwnerCode = createString("toOwnerCode");

    public final NumberPath<Long> toOwnerId = createNumber("toOwnerId", Long.class);

    public final StringPath toOwnerName = createString("toOwnerName");

    public final NumberPath<Long> toOwnerType = createNumber("toOwnerType", Long.class);

    public QStockIsdnTrans(String variable) {
        super(StockIsdnTrans.class, forVariable(variable));
    }

    public QStockIsdnTrans(Path<? extends StockIsdnTrans> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockIsdnTrans(PathMetadata<?> metadata) {
        super(StockIsdnTrans.class, metadata);
    }

}

