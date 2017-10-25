package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QStockHandset is a Querydsl query type for StockHandset
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockHandset extends EntityPathBase<StockHandset> {

    private static final long serialVersionUID = 1102370799L;

    public static final QStockHandset stockHandset = new QStockHandset("stockHandset");

    public final DateTimePath<java.util.Date> connectionDate = createDateTime("connectionDate", java.util.Date.class);

    public final NumberPath<Long> connectionStatus = createNumber("connectionStatus", Long.class);

    public final StringPath connectionType = createString("connectionType");

    public final StringPath contractCode = createString("contractCode");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final StringPath createUser = createString("createUser");

    public final StringPath depositPrice = createString("depositPrice");

    public final DateTimePath<java.util.Date> exportToCollDate = createDateTime("exportToCollDate", java.util.Date.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> ownerId = createNumber("ownerId", Long.class);

    public final NumberPath<Long> ownerType = createNumber("ownerType", Long.class);

    public final NumberPath<Long> partnerId = createNumber("partnerId", Long.class);

    public final StringPath poCode = createString("poCode");

    public final NumberPath<Long> prodOfferId = createNumber("prodOfferId", Long.class);

    public final DateTimePath<java.util.Date> saleDate = createDateTime("saleDate", java.util.Date.class);

    public final StringPath serial = createString("serial");

    public final NumberPath<Long> stateId = createNumber("stateId", Long.class);

    public final NumberPath<Long> status = createNumber("status", Long.class);

    public final NumberPath<Long> telecomServiceId = createNumber("telecomServiceId", Long.class);

    public final StringPath tvmsCadId = createString("tvmsCadId");

    public final StringPath tvmsMacId = createString("tvmsMacId");

    public final DateTimePath<java.util.Date> updateDatetime = createDateTime("updateDatetime", java.util.Date.class);

    public QStockHandset(String variable) {
        super(StockHandset.class, forVariable(variable));
    }

    public QStockHandset(Path<? extends StockHandset> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockHandset(PathMetadata<?> metadata) {
        super(StockHandset.class, metadata);
    }

}

