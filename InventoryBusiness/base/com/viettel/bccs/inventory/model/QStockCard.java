package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QStockCard is a Querydsl query type for StockCard
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockCard extends EntityPathBase<StockCard> {

    private static final long serialVersionUID = -795209164L;

    public static final QStockCard stockCard = new QStockCard("stockCard");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final StringPath createUser = createString("createUser");

    public final StringPath fromSerial = createString("fromSerial");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> ownerId = createNumber("ownerId", Long.class);

    public final NumberPath<Long> ownerType = createNumber("ownerType", Long.class);

    public final StringPath poCode = createString("poCode");

    public final NumberPath<Long> prodOfferId = createNumber("prodOfferId", Long.class);

    public final DateTimePath<java.util.Date> saleDate = createDateTime("saleDate", java.util.Date.class);

    public final NumberPath<Long> stateId = createNumber("stateId", Long.class);

    public final NumberPath<Long> status = createNumber("status", Long.class);

    public final NumberPath<Long> telecomServiceId = createNumber("telecomServiceId", Long.class);

    public final StringPath toSerial = createString("toSerial");

    public final NumberPath<Long> vcStatus = createNumber("vcStatus", Long.class);

    public QStockCard(String variable) {
        super(StockCard.class, forVariable(variable));
    }

    public QStockCard(Path<? extends StockCard> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockCard(PathMetadata<?> metadata) {
        super(StockCard.class, metadata);
    }

}

