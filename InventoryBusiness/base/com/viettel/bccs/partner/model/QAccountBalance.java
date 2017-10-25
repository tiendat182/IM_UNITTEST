package com.viettel.bccs.partner.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QAccountBalance is a Querydsl query type for AccountBalance
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QAccountBalance extends EntityPathBase<AccountBalance> {

    private static final long serialVersionUID = -1281385023L;

    public static final QAccountBalance accountBalance = new QAccountBalance("accountBalance");

    public final NumberPath<Long> accountId = createNumber("accountId", Long.class);

    public final NumberPath<Long> balanceId = createNumber("balanceId", Long.class);

    public final NumberPath<Long> balanceType = createNumber("balanceType", Long.class);

    public final DateTimePath<java.util.Date> dateCreated = createDateTime("dateCreated", java.util.Date.class);

    public final DateTimePath<java.util.Date> endDate = createDateTime("endDate", java.util.Date.class);

    public final DateTimePath<java.util.Date> lastUpdateTime = createDateTime("lastUpdateTime", java.util.Date.class);

    public final NumberPath<Long> realBalance = createNumber("realBalance", Long.class);

    public final NumberPath<Long> realCredit = createNumber("realCredit", Long.class);

    public final NumberPath<Long> realDebit = createNumber("realDebit", Long.class);

    public final DateTimePath<java.util.Date> startDate = createDateTime("startDate", java.util.Date.class);

    public final NumberPath<Long> status = createNumber("status", Long.class);

    public final StringPath userCreated = createString("userCreated");

    public QAccountBalance(String variable) {
        super(AccountBalance.class, forVariable(variable));
    }

    public QAccountBalance(Path<? extends AccountBalance> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAccountBalance(PathMetadata<?> metadata) {
        super(AccountBalance.class, metadata);
    }

}

