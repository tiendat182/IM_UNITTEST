package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QStockTransDocument is a Querydsl query type for StockTransDocument
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockTransDocument extends EntityPathBase<StockTransDocument> {

    private static final long serialVersionUID = -660065985L;

    public static final QStockTransDocument stockTransDocument = new QStockTransDocument("stockTransDocument");

    public final ArrayPath<byte[], Byte> fileAttach = createArray("fileAttach", byte[].class);

    public final NumberPath<Long> stockTransDocumentId = createNumber("stockTransDocumentId", Long.class);

    public final NumberPath<Long> stockTransId = createNumber("stockTransId", Long.class);

    public final NumberPath<Long> transType = createNumber("transType", Long.class);

    public QStockTransDocument(String variable) {
        super(StockTransDocument.class, forVariable(variable));
    }

    public QStockTransDocument(Path<? extends StockTransDocument> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockTransDocument(PathMetadata<?> metadata) {
        super(StockTransDocument.class, metadata);
    }

}

