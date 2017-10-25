package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QDepositDetail is a Querydsl query type for DepositDetail
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QDepositDetail extends EntityPathBase<DepositDetail> {

    private static final long serialVersionUID = 1003072637L;

    public static final QDepositDetail depositDetail = new QDepositDetail("depositDetail");

    public final NumberPath<java.math.BigDecimal> amount = createNumber("amount", java.math.BigDecimal.class);

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final NumberPath<Long> depositDetailId = createNumber("depositDetailId", Long.class);

    public final NumberPath<Long> depositId = createNumber("depositId", Long.class);

    public final StringPath depositType = createString("depositType");

    public final DateTimePath<java.util.Date> expiredDate = createDateTime("expiredDate", java.util.Date.class);

    public final NumberPath<Long> posId = createNumber("posId", Long.class);

    public final NumberPath<java.math.BigDecimal> price = createNumber("price", java.math.BigDecimal.class);

    public final NumberPath<Long> priceId = createNumber("priceId", Long.class);

    public final NumberPath<Long> quantity = createNumber("quantity", Long.class);

    public final StringPath serial = createString("serial");

    public final NumberPath<Long> stockModelId = createNumber("stockModelId", Long.class);

    public final NumberPath<Long> supplyMonth = createNumber("supplyMonth", Long.class);

    public final StringPath supplyProgram = createString("supplyProgram");

    public QDepositDetail(String variable) {
        super(DepositDetail.class, forVariable(variable));
    }

    public QDepositDetail(Path<? extends DepositDetail> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDepositDetail(PathMetadata<?> metadata) {
        super(DepositDetail.class, metadata);
    }

}

