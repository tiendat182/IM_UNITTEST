package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QStockHandsetLend is a Querydsl query type for StockHandsetLend
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockHandsetLend extends EntityPathBase<StockHandsetLend> {

    private static final long serialVersionUID = 717053310L;

    public static final QStockHandsetLend stockHandsetLend = new QStockHandsetLend("stockHandsetLend");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final NumberPath<Long> createStaffId = createNumber("createStaffId", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.util.Date> lastModify = createDateTime("lastModify", java.util.Date.class);

    public final NumberPath<Long> ownerId = createNumber("ownerId", Long.class);

    public final NumberPath<Long> ownerType = createNumber("ownerType", Long.class);

    public final NumberPath<Long> prodOfferId = createNumber("prodOfferId", Long.class);

    public final StringPath serial = createString("serial");

    public final NumberPath<Long> status = createNumber("status", Long.class);

    public QStockHandsetLend(String variable) {
        super(StockHandsetLend.class, forVariable(variable));
    }

    public QStockHandsetLend(Path<? extends StockHandsetLend> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockHandsetLend(PathMetadata<?> metadata) {
        super(StockHandsetLend.class, metadata);
    }

}

