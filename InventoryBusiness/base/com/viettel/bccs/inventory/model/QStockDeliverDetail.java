package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QStockDeliverDetail is a Querydsl query type for StockDeliverDetail
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockDeliverDetail extends EntityPathBase<StockDeliverDetail> {

    private static final long serialVersionUID = -1231483086L;

    public static final QStockDeliverDetail stockDeliverDetail = new QStockDeliverDetail("stockDeliverDetail");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final StringPath note = createString("note");

    public final NumberPath<Long> prodOfferId = createNumber("prodOfferId", Long.class);

    public final NumberPath<Long> prodOfferTypeId = createNumber("prodOfferTypeId", Long.class);

    public final NumberPath<Long> quantityReal = createNumber("quantityReal", Long.class);

    public final NumberPath<Long> quantitySys = createNumber("quantitySys", Long.class);

    public final NumberPath<Long> stateId = createNumber("stateId", Long.class);

    public final NumberPath<Long> stockDeliverDetailId = createNumber("stockDeliverDetailId", Long.class);

    public final NumberPath<Long> stockDeliverId = createNumber("stockDeliverId", Long.class);

    public QStockDeliverDetail(String variable) {
        super(StockDeliverDetail.class, forVariable(variable));
    }

    public QStockDeliverDetail(Path<? extends StockDeliverDetail> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockDeliverDetail(PathMetadata<?> metadata) {
        super(StockDeliverDetail.class, metadata);
    }

}

