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
 * QInvoiceTemplateLog is a Querydsl query type for InvoiceTemplateLog
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QInvoiceTemplateLog extends EntityPathBase<InvoiceTemplateLog> {

    private static final long serialVersionUID = 317009871L;

    public static final QInvoiceTemplateLog invoiceTemplateLog = new QInvoiceTemplateLog("invoiceTemplateLog");

    public final StringPath afterAmount = createString("afterAmount");

    public final DateTimePath<java.util.Date> createDatetime = createDateTime("createDatetime", java.util.Date.class);

    public final StringPath createUser = createString("createUser");

    public final NumberPath<Long> fromShopId = createNumber("fromShopId", Long.class);

    public final NumberPath<Long> invoiceTemplateId = createNumber("invoiceTemplateId", Long.class);

    public final NumberPath<Long> invoiceTemplateLogId = createNumber("invoiceTemplateLogId", Long.class);

    public final StringPath logType = createString("logType");

    public final NumberPath<Long> orgAmount = createNumber("orgAmount", Long.class);

    public final NumberPath<Long> ownerId = createNumber("ownerId", Long.class);

    public final StringPath ownerType = createString("ownerType");

    public final NumberPath<Long> reasonId = createNumber("reasonId", Long.class);

    public QInvoiceTemplateLog(String variable) {
        super(InvoiceTemplateLog.class, forVariable(variable));
    }

    public QInvoiceTemplateLog(Path<? extends InvoiceTemplateLog> path) {
        super(path.getType(), path.getMetadata());
    }

    public QInvoiceTemplateLog(PathMetadata<?> metadata) {
        super(InvoiceTemplateLog.class, metadata);
    }

}

