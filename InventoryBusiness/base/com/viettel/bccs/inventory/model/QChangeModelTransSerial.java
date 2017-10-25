package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QChangeModelTransSerial is a Querydsl query type for ChangeModelTransSerial
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QChangeModelTransSerial extends EntityPathBase<ChangeModelTransSerial> {

    private static final long serialVersionUID = 973540213L;

    public static final QChangeModelTransSerial changeModelTransSerial = new QChangeModelTransSerial("changeModelTransSerial");

    public final NumberPath<Long> changeModelTransDetailId = createNumber("changeModelTransDetailId", Long.class);

    public final NumberPath<Long> changeModelTransSerialId = createNumber("changeModelTransSerialId", Long.class);

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final StringPath fromSerial = createString("fromSerial");

    public final NumberPath<Long> quantity = createNumber("quantity", Long.class);

    public final StringPath toSerial = createString("toSerial");

    public QChangeModelTransSerial(String variable) {
        super(ChangeModelTransSerial.class, forVariable(variable));
    }

    public QChangeModelTransSerial(Path<? extends ChangeModelTransSerial> path) {
        super(path.getType(), path.getMetadata());
    }

    public QChangeModelTransSerial(PathMetadata<?> metadata) {
        super(ChangeModelTransSerial.class, metadata);
    }

}

