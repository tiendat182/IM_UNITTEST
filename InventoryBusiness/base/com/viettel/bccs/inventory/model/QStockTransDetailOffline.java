package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QStockTransDetailOffline is a Querydsl query type for StockTransDetailOffline
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockTransDetailOffline extends EntityPathBase<StockTransDetailOffline> {

    private static final long serialVersionUID = -437004626L;

    public static final QStockTransDetailOffline stockTransDetailOffline = new QStockTransDetailOffline("stockTransDetailOffline");

    public final NumberPath<Short> checkDial = createNumber("checkDial", Short.class);

    public final DateTimePath<java.util.Date> createDatetime = createDateTime("createDatetime", java.util.Date.class);

    public final NumberPath<Short> dialStatus = createNumber("dialStatus", Short.class);

    public final NumberPath<Long> modelProgramId = createNumber("modelProgramId", Long.class);

    public final StringPath modelProgramName = createString("modelProgramName");

    public final StringPath note = createString("note");

    public final NumberPath<Long> quantityReal = createNumber("quantityReal", Long.class);

    public final NumberPath<Long> quantityRes = createNumber("quantityRes", Long.class);

    public final NumberPath<Long> stateId = createNumber("stateId", Long.class);

    public final NumberPath<Long> stockModelId = createNumber("stockModelId", Long.class);

    public final NumberPath<Long> stockTransDetailId = createNumber("stockTransDetailId", Long.class);

    public final NumberPath<Long> stockTransId = createNumber("stockTransId", Long.class);

    public QStockTransDetailOffline(String variable) {
        super(StockTransDetailOffline.class, forVariable(variable));
    }

    public QStockTransDetailOffline(Path<? extends StockTransDetailOffline> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockTransDetailOffline(PathMetadata<?> metadata) {
        super(StockTransDetailOffline.class, metadata);
    }

}

