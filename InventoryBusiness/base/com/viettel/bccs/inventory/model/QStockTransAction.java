package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QStockTransAction is a Querydsl query type for StockTransAction
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockTransAction extends EntityPathBase<StockTransAction> {

    private static final long serialVersionUID = -1487104166L;

    public static final QStockTransAction stockTransAction = new QStockTransAction("stockTransAction");

    public final StringPath actionCode = createString("actionCode");

    public final NumberPath<Long> actionStaffId = createNumber("actionStaffId", Long.class);

    public final DateTimePath<java.util.Date> createDatetime = createDateTime("createDatetime", java.util.Date.class);

    public final StringPath createUser = createString("createUser");

    public final StringPath documentStatus = createString("documentStatus");

    public final StringPath logCmdCode = createString("logCmdCode");

    public final StringPath logNoteCode = createString("logNoteCode");

    public final StringPath note = createString("note");

    public final NumberPath<Long> regionOwnerId = createNumber("regionOwnerId", Long.class);

    public final StringPath status = createString("status");

    public final StringPath actionCodeShop = createString("actionCodeShop");

    public final NumberPath<Long> stockTransActionId = createNumber("stockTransActionId", Long.class);

    public final NumberPath<Long> stockTransId = createNumber("stockTransId", Long.class);

    public QStockTransAction(String variable) {
        super(StockTransAction.class, forVariable(variable));
    }

    public QStockTransAction(Path<? extends StockTransAction> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockTransAction(PathMetadata<?> metadata) {
        super(StockTransAction.class, metadata);
    }

}

