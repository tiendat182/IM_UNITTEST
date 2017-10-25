package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QStockOrderAgentDetail is a Querydsl query type for StockOrderAgentDetail
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockOrderAgentDetail extends EntityPathBase<StockOrderAgentDetail> {

    private static final long serialVersionUID = -1710009172L;

    public static final QStockOrderAgentDetail stockOrderAgentDetail = new QStockOrderAgentDetail("stockOrderAgentDetail");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final StringPath note = createString("note");

    public final NumberPath<Long> prodOfferId = createNumber("prodOfferId", Long.class);

    public final NumberPath<Long> quantity = createNumber("quantity", Long.class);

    public final NumberPath<Long> stateId = createNumber("stateId", Long.class);

    public final NumberPath<Long> stockOrderAgentDetailId = createNumber("stockOrderAgentDetailId", Long.class);

    public final NumberPath<Long> stockOrderAgentId = createNumber("stockOrderAgentId", Long.class);

    public QStockOrderAgentDetail(String variable) {
        super(StockOrderAgentDetail.class, forVariable(variable));
    }

    public QStockOrderAgentDetail(Path<? extends StockOrderAgentDetail> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockOrderAgentDetail(PathMetadata<?> metadata) {
        super(StockOrderAgentDetail.class, metadata);
    }

}

