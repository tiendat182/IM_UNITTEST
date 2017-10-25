package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QStockIsdnTransDetail is a Querydsl query type for StockIsdnTransDetail
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockIsdnTransDetail extends EntityPathBase<StockIsdnTransDetail> {

    private static final long serialVersionUID = -1459024991L;

    public static final QStockIsdnTransDetail stockIsdnTransDetail = new QStockIsdnTransDetail("stockIsdnTransDetail");

    public final DateTimePath<java.util.Date> createdTime = createDateTime("createdTime", java.util.Date.class);

    public final StringPath fromIsdn = createString("fromIsdn");

    public final NumberPath<Long> lengthIsdn = createNumber("lengthIsdn", Long.class);

    public final NumberPath<Long> quantity = createNumber("quantity", Long.class);

    public final NumberPath<Long> stockIsdnTransDetailId = createNumber("stockIsdnTransDetailId", Long.class);

    public final NumberPath<Long> stockIsdnTransId = createNumber("stockIsdnTransId", Long.class);

    public final StringPath toIsdn = createString("toIsdn");

    public final NumberPath<Long> typeIsdn = createNumber("typeIsdn", Long.class);

    public QStockIsdnTransDetail(String variable) {
        super(StockIsdnTransDetail.class, forVariable(variable));
    }

    public QStockIsdnTransDetail(Path<? extends StockIsdnTransDetail> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockIsdnTransDetail(PathMetadata<?> metadata) {
        super(StockIsdnTransDetail.class, metadata);
    }

}

