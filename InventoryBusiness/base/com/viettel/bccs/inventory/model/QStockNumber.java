package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;

import javax.annotation.Generated;

import com.mysema.query.types.Path;


/**
 * QStockNumber is a Querydsl query type for StockNumber
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockNumber extends EntityPathBase<StockNumber> {
    public final NumberPath<Long> ownerId = createNumber("ownerId", Long.class);

    public final NumberPath<Short> ownerType = createNumber("ownerId", Short.class);
    private static final long serialVersionUID = 641415533L;

    public static final QStockNumber stockNumber = new QStockNumber("stockNumber");

    public final StringPath areaCode = createString("areaCode");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final NumberPath<Long> filterRuleId = createNumber("filterRuleId", Long.class);

    public final ComparablePath<Character> hlrStatus = createComparable("hlrStatus", Character.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imsi = createString("imsi");

    public final StringPath isdn = createString("isdn");

    public final ComparablePath<Character> isdnType = createComparable("isdnType", Character.class);

    public final StringPath lastUpdateIpAddress = createString("lastUpdateIpAddress");

    public final DateTimePath<java.util.Date> lastUpdateTime = createDateTime("lastUpdateTime", java.util.Date.class);

    public final StringPath lastUpdateUser = createString("lastUpdateUser");

    public final NumberPath<Long> prodOfferId = createNumber("prodOfferId", Long.class);

    public final StringPath serial = createString("serial");

    public final StringPath serviceType = createString("serviceType");

    public final StringPath stationCode = createString("stationCode");

    public final NumberPath<Long> stationId = createNumber("stationId", Long.class);

    public final StringPath status = createString("status");

    public final NumberPath<Long> stockId = createNumber("stockId", Long.class);

    public final NumberPath<Long> lockByStaff = createNumber("lockByStaff", Long.class);

    public final NumberPath<Long> telecomServiceId = createNumber("telecomServiceId", Long.class);

    public final DateTimePath<java.util.Date> updateDatetime = createDateTime("updateDatetime", java.util.Date.class);

    public final StringPath userCreate = createString("userCreate");

    public QStockNumber(String variable) {
        super(StockNumber.class, forVariable(variable));
    }

    public QStockNumber(Path<? extends StockNumber> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockNumber(PathMetadata<?> metadata) {
        super(StockNumber.class, metadata);
    }


}

