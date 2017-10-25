package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QApTransInfo is a Querydsl query type for ApTransInfo
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QApTransInfo extends EntityPathBase<ApTransInfo> {

    private static final long serialVersionUID = 1969959797L;

    public static final QApTransInfo apTransInfo = new QApTransInfo("apTransInfo");

    public final NumberPath<Long> createShopId = createNumber("createShopId", Long.class);

    public final NumberPath<Long> createStaffId = createNumber("createStaffId", Long.class);

    public final StringPath extendList = createString("extendList");

    public final StringPath feeList = createString("feeList");

    public final NumberPath<Short> goodLocked = createNumber("goodLocked", Short.class);

    public final StringPath ipList = createString("ipList");

    public final DateTimePath<java.util.Date> logDate = createDateTime("logDate", java.util.Date.class);

    public final StringPath posId = createString("posId");

    public final StringPath pstnList = createString("pstnList");

    public final NumberPath<Short> qtyIssueDecrease = createNumber("qtyIssueDecrease", Short.class);

    public final NumberPath<Short> qtySupplyIssueDecrease = createNumber("qtySupplyIssueDecrease", Short.class);

    public final StringPath requestId = createString("requestId");

    public final StringPath response = createString("response");

    public final NumberPath<Long> shopId = createNumber("shopId", Long.class);

    public final NumberPath<Long> staffId = createNumber("staffId", Long.class);

    public final NumberPath<Short> status = createNumber("status", Short.class);

    public final StringPath stockList = createString("stockList");

    public final StringPath toString = createString("toString");

    public final NumberPath<Short> transType = createNumber("transType", Short.class);

    public final StringPath version = createString("version");

    public QApTransInfo(String variable) {
        super(ApTransInfo.class, forVariable(variable));
    }

    public QApTransInfo(Path<? extends ApTransInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QApTransInfo(PathMetadata<?> metadata) {
        super(ApTransInfo.class, metadata);
    }

}

