package com.viettel.bccs.sale.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QSaleTransSerial is a Querydsl query type for SaleTransSerial
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QSaleTransSerial extends EntityPathBase<SaleTransSerial> {

    private static final long serialVersionUID = 2078562596L;

    public static final QSaleTransSerial saleTransSerial = new QSaleTransSerial("saleTransSerial");

    public final DateTimePath<java.util.Date> dateDeliver = createDateTime("dateDeliver", java.util.Date.class);

    public final StringPath fromSerial = createString("fromSerial");

    public final NumberPath<Long> quantity = createNumber("quantity", Long.class);

    public final DateTimePath<java.util.Date> saleTransDate = createDateTime("saleTransDate", java.util.Date.class);

    public final NumberPath<Long> saleTransDetailId = createNumber("saleTransDetailId", Long.class);

    public final NumberPath<Long> saleTransId = createNumber("saleTransId", Long.class);

    public final NumberPath<Long> saleTransSerialId = createNumber("saleTransSerialId", Long.class);

    public final NumberPath<Long> stockModelId = createNumber("stockModelId", Long.class);

    public final StringPath toSerial = createString("toSerial");

    public final NumberPath<Long> userDeliver = createNumber("userDeliver", Long.class);

    public final NumberPath<Long> userUpdate = createNumber("userUpdate", Long.class);

    public QSaleTransSerial(String variable) {
        super(SaleTransSerial.class, forVariable(variable));
    }

    public QSaleTransSerial(Path<? extends SaleTransSerial> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSaleTransSerial(PathMetadata<?> metadata) {
        super(SaleTransSerial.class, metadata);
    }

}

