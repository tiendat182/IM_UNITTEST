package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QDeposit is a Querydsl query type for Deposit
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QDeposit extends EntityPathBase<Deposit> {

    private static final long serialVersionUID = -588472052L;

    public static final QDeposit deposit = new QDeposit("deposit");

    public final StringPath address = createString("address");

    public final NumberPath<Long> amount = createNumber("amount", Long.class);

    public final StringPath branchId = createString("branchId");

    public final StringPath contractNo = createString("contractNo");

    public final StringPath createBy = createString("createBy");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final StringPath custName = createString("custName");

    public final NumberPath<Long> deliverId = createNumber("deliverId", Long.class);

    public final NumberPath<Long> depositId = createNumber("depositId", Long.class);

    public final NumberPath<Long> depositTypeId = createNumber("depositTypeId", Long.class);

    public final StringPath description = createString("description");

    public final StringPath idNo = createString("idNo");

    public final StringPath isdn = createString("isdn");

    public final StringPath primaryAccount = createString("primaryAccount");

    public final NumberPath<Long> reasonId = createNumber("reasonId", Long.class);

    public final NumberPath<Long> receiptId = createNumber("receiptId", Long.class);

    public final StringPath requestId = createString("requestId");

    public final NumberPath<Long> shopId = createNumber("shopId", Long.class);

    public final NumberPath<Long> staffId = createNumber("staffId", Long.class);

    public final StringPath status = createString("status");

    public final NumberPath<Long> stockTransId = createNumber("stockTransId", Long.class);

    public final NumberPath<Long> subId = createNumber("subId", Long.class);

    public final NumberPath<Long> telecomServiceId = createNumber("telecomServiceId", Long.class);

    public final StringPath tin = createString("tin");

    public final StringPath type = createString("type");

    public QDeposit(String variable) {
        super(Deposit.class, forVariable(variable));
    }

    public QDeposit(Path<? extends Deposit> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDeposit(PathMetadata<?> metadata) {
        super(Deposit.class, metadata);
    }

}

