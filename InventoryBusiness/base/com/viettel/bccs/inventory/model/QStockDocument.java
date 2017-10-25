package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QStockDocument is a Querydsl query type for StockDocument
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockDocument extends EntityPathBase<StockDocument> {

    private static final long serialVersionUID = 673199679L;

    public static final QStockDocument stockDocument = new QStockDocument("stockDocument");

    public final StringPath actionCode = createString("actionCode");

    public final NumberPath<Long> actionId = createNumber("actionId", Long.class);

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final SimplePath<java.io.Serializable> deliveryRecords = createSimple("deliveryRecords", java.io.Serializable.class);

    public final StringPath deliveryRecordsName = createString("deliveryRecordsName");

    public final DateTimePath<java.util.Date> destroyDate = createDateTime("destroyDate", java.util.Date.class);

    public final StringPath destroyUser = createString("destroyUser");

    public final SimplePath<java.io.Serializable> exportNote = createSimple("exportNote", java.io.Serializable.class);

    public final StringPath exportNoteName = createString("exportNoteName");

    public final StringPath fromShopCode = createString("fromShopCode");

    public final StringPath reason = createString("reason");

    public final NumberPath<Short> status = createNumber("status", Short.class);

    public final NumberPath<Long> stockDocumentId = createNumber("stockDocumentId", Long.class);

    public final StringPath toShopCode = createString("toShopCode");

    public final StringPath userCreate = createString("userCreate");

    public QStockDocument(String variable) {
        super(StockDocument.class, forVariable(variable));
    }

    public QStockDocument(Path<? extends StockDocument> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockDocument(PathMetadata<?> metadata) {
        super(StockDocument.class, metadata);
    }

}

