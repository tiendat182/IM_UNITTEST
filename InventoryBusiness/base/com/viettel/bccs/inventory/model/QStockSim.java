package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QStockSim is a Querydsl query type for StockSim
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockSim extends EntityPathBase<StockSim> {

    private static final long serialVersionUID = -302730957L;

    public static final QStockSim stockSim = new QStockSim("stockSim");

    public final StringPath contractCode = createString("contractCode");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final StringPath createUser = createString("createUser");

    public final StringPath depositPrice = createString("depositPrice");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> ownerId = createNumber("ownerId", Long.class);

    public final NumberPath<Long> ownerType = createNumber("ownerType", Long.class);

    public final StringPath poCode = createString("poCode");

    public final NumberPath<Long> prodOfferId = createNumber("prodOfferId", Long.class);

    public final DateTimePath<java.util.Date> saleDate = createDateTime("saleDate", java.util.Date.class);

    public final StringPath serial = createString("serial");

    public final NumberPath<Long> stateId = createNumber("stateId", Long.class);

    public final NumberPath<Long> status = createNumber("status", Long.class);

    public final NumberPath<Long> stockSimInfoId = createNumber("stockSimInfoId", Long.class);

    public final NumberPath<Long> telecomServiceId = createNumber("telecomServiceId", Long.class);

    public final DateTimePath<java.util.Date> updateDatetime = createDateTime("updateDatetime", java.util.Date.class);

    public QStockSim(String variable) {
        super(StockSim.class, forVariable(variable));
    }

    public QStockSim(Path<? extends StockSim> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockSim(PathMetadata<?> metadata) {
        super(StockSim.class, metadata);
    }

}

