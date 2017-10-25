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
 * QInvoiceActionLog is a Querydsl query type for InvoiceActionLog
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QInvoiceActionLog extends EntityPathBase<InvoiceActionLog> {

    private static final long serialVersionUID = -728372781L;

    public static final QInvoiceActionLog invoiceActionLog = new QInvoiceActionLog("invoiceActionLog");

    public final StringPath actionType = createString("actionType");

    public final DateTimePath<java.util.Date> approveDate = createDateTime("approveDate", java.util.Date.class);

    public final StringPath approveUser = createString("approveUser");

    public final StringPath blockNo = createString("blockNo");

    public final DateTimePath<java.util.Date> createDatetime = createDateTime("createDatetime", java.util.Date.class);

    public final StringPath createUser = createString("createUser");

    public final NumberPath<Long> curInvoice = createNumber("curInvoice", Long.class);

    public final StringPath description = createString("description");

    public final NumberPath<Long> fromInvoice = createNumber("fromInvoice", Long.class);

    public final NumberPath<Long> fromShopId = createNumber("fromShopId", Long.class);

    public final NumberPath<Long> fromStaffId = createNumber("fromStaffId", Long.class);

    public final NumberPath<Long> invoiceActionId = createNumber("invoiceActionId", Long.class);

    public final NumberPath<Long> invoiceListId = createNumber("invoiceListId", Long.class);

    public final StringPath serialNo = createString("serialNo");

    public final NumberPath<Long> toInvoice = createNumber("toInvoice", Long.class);

    public final NumberPath<Long> toShopId = createNumber("toShopId", Long.class);

    public final NumberPath<Long> toStaffId = createNumber("toStaffId", Long.class);

    public QInvoiceActionLog(String variable) {
        super(InvoiceActionLog.class, forVariable(variable));
    }

    public QInvoiceActionLog(Path<? extends InvoiceActionLog> path) {
        super(path.getType(), path.getMetadata());
    }

    public QInvoiceActionLog(PathMetadata<?> metadata) {
        super(InvoiceActionLog.class, metadata);
    }

}

