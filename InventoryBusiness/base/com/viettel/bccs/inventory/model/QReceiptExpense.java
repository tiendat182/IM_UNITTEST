package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QReceiptExpense is a Querydsl query type for ReceiptExpense
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QReceiptExpense extends EntityPathBase<ReceiptExpense> {

    private static final long serialVersionUID = 984939730L;

    public static final QReceiptExpense receiptExpense = new QReceiptExpense("receiptExpense");

    public final StringPath address = createString("address");

    public final NumberPath<Long> amount = createNumber("amount", Long.class);

    public final DateTimePath<java.util.Date> approverDate = createDateTime("approverDate", java.util.Date.class);

    public final NumberPath<Long> approverStaffId = createNumber("approverStaffId", Long.class);

    public final DateTimePath<java.util.Date> createDatetime = createDateTime("createDatetime", java.util.Date.class);

    public final StringPath debt = createString("debt");

    public final StringPath deliverer = createString("deliverer");

    public final NumberPath<Long> delivererShopId = createNumber("delivererShopId", Long.class);

    public final NumberPath<Long> delivererStaffId = createNumber("delivererStaffId", Long.class);

    public final DateTimePath<java.util.Date> destroyDate = createDateTime("destroyDate", java.util.Date.class);

    public final NumberPath<Long> destroyStaffId = createNumber("destroyStaffId", Long.class);

    public final StringPath notes = createString("notes");

    public final StringPath own = createString("own");

    public final StringPath payMethod = createString("payMethod");

    public final NumberPath<Long> reasonId = createNumber("reasonId", Long.class);

    public final DateTimePath<java.util.Date> receiptDate = createDateTime("receiptDate", java.util.Date.class);

    public final NumberPath<Long> receiptId = createNumber("receiptId", Long.class);

    public final StringPath receiptNo = createString("receiptNo");

    public final NumberPath<Long> receiptTypeId = createNumber("receiptTypeId", Long.class);

    public final NumberPath<Long> shopId = createNumber("shopId", Long.class);

    public final NumberPath<Long> staffId = createNumber("staffId", Long.class);

    public final StringPath status = createString("status");

    public final NumberPath<Long> telecomServiceId = createNumber("telecomServiceId", Long.class);

    public final StringPath type = createString("type");

    public final StringPath username = createString("username");

    public QReceiptExpense(String variable) {
        super(ReceiptExpense.class, forVariable(variable));
    }

    public QReceiptExpense(Path<? extends ReceiptExpense> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReceiptExpense(PathMetadata<?> metadata) {
        super(ReceiptExpense.class, metadata);
    }

}

