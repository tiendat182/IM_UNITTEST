package com.viettel.bccs.inventory.model;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.DateTimePath;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * QInvoiceList is a Querydsl query type for InvoiceList
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QInvoiceList extends EntityPathBase<InvoiceList> {

    private static final long serialVersionUID = 1092201689L;

    public static final QInvoiceList invoiceList = new QInvoiceList("invoiceList");

    public final DateTimePath<java.util.Date> approveDatetime = createDateTime("approveDatetime", java.util.Date.class);

    public final StringPath approveUser = createString("approveUser");

    public final StringPath blockNo = createString("blockNo");

    public final DateTimePath<java.util.Date> createDatetime = createDateTime("createDatetime", java.util.Date.class);

    public final StringPath createUser = createString("createUser");

    public final NumberPath<Long> currInvoice = createNumber("currInvoice", Long.class);

    public final DateTimePath<java.util.Date> destroyDate = createDateTime("destroyDate", java.util.Date.class);

    public final StringPath destroyReasonId = createString("destroyReasonId");

    public final StringPath destroyUser = createString("destroyUser");

    public final NumberPath<Long> fromInvoice = createNumber("fromInvoice", Long.class);

    public final NumberPath<Long> invoiceListId = createNumber("invoiceListId", Long.class);

    public final NumberPath<Long> invoiceSerialId = createNumber("invoiceSerialId", Long.class);

    public final StringPath serialNo = createString("serialNo");

    public final NumberPath<Long> shopId = createNumber("shopId", Long.class);

    public final NumberPath<Long> staffId = createNumber("staffId", Long.class);

    public final StringPath status = createString("status");

    public final NumberPath<Long> toInvoice = createNumber("toInvoice", Long.class);

    public QInvoiceList(String variable) {
        super(InvoiceList.class, forVariable(variable));
    }

    public QInvoiceList(Path<? extends InvoiceList> path) {
        super(path.getType(), path.getMetadata());
    }

    public QInvoiceList(PathMetadata<?> metadata) {
        super(InvoiceList.class, metadata);
    }

}

