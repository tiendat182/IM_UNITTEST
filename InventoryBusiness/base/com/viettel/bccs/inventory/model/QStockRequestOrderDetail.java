package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QStockRequestOrderDetail is a Querydsl query type for StockRequestOrderDetail
 */
@Generated("com.mysema.query.codegen.SupertypeSerializer")
public class QStockRequestOrderDetail extends EntityPathBase<StockRequestOrderDetail> {

    private static final long serialVersionUID = -1369278636L;

    public static final QStockRequestOrderDetail stockRequestOrderDetail = new QStockRequestOrderDetail("stockRequestOrderDetail");

    public final NumberPath<Long> approvedQuantity = createNumber("approvedQuantity", Long.class);

    public final NumberPath<Long> fromOwnerId = createNumber("fromOwnerId", Long.class);

    public final NumberPath<Long> fromOwnerType = createNumber("fromOwnerType", Long.class);

    public final NumberPath<Long> fromStockTransId = createNumber("fromStockTransId", Long.class);

    public final StringPath note = createString("note");

    public final NumberPath<Long> prodOfferId = createNumber("prodOfferId", Long.class);

    public final NumberPath<Long> requestQuantity = createNumber("requestQuantity", Long.class);

    public final NumberPath<Long> stateId = createNumber("stateId", Long.class);

    public final NumberPath<Long> status = createNumber("status", Long.class);

    public final NumberPath<Long> stockRequestOrderDetailId = createNumber("stockRequestOrderDetailId", Long.class);

    public final NumberPath<Long> stockRequestOrderId = createNumber("stockRequestOrderId", Long.class);

    public final NumberPath<Long> stockTransId = createNumber("stockTransId", Long.class);

    public final NumberPath<Long> toOwnerId = createNumber("toOwnerId", Long.class);

    public final NumberPath<Long> toOwnerType = createNumber("toOwnerType", Long.class);

    public final NumberPath<Long> exportTransId = createNumber("exportTransId", Long.class);

    public final NumberPath<Long> importTransId = createNumber("importTransId", Long.class);

    public QStockRequestOrderDetail(String variable) {
        super(StockRequestOrderDetail.class, forVariable(variable));
    }

    public QStockRequestOrderDetail(Path<? extends StockRequestOrderDetail> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockRequestOrderDetail(PathMetadata<?> metadata) {
        super(StockRequestOrderDetail.class, metadata);
    }

}

