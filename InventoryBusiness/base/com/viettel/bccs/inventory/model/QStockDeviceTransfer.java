package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QStockDeviceTransfer is a Querydsl query type for StockDeviceTransfer
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockDeviceTransfer extends EntityPathBase<StockDeviceTransfer> {

    private static final long serialVersionUID = 773252229L;

    public static final QStockDeviceTransfer stockDeviceTransfer = new QStockDeviceTransfer("stockDeviceTransfer");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final StringPath createUser = createString("createUser");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> newProdOfferId = createNumber("newProdOfferId", Long.class);

    public final NumberPath<Short> newStateId = createNumber("newStateId", Short.class);

    public final NumberPath<Long> prodOfferId = createNumber("prodOfferId", Long.class);

    public final NumberPath<Short> stateId = createNumber("stateId", Short.class);

    public final NumberPath<Long> stockRequestId = createNumber("stockRequestId", Long.class);

    public QStockDeviceTransfer(String variable) {
        super(StockDeviceTransfer.class, forVariable(variable));
    }

    public QStockDeviceTransfer(Path<? extends StockDeviceTransfer> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockDeviceTransfer(PathMetadata<?> metadata) {
        super(StockDeviceTransfer.class, metadata);
    }

}

