package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QInvoiceTemplate is a Querydsl query type for InvoiceTemplate
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QInvoiceTemplate extends EntityPathBase<InvoiceTemplate> {

    private static final long serialVersionUID = -243059915L;

    public static final QInvoiceTemplate invoiceTemplate = new QInvoiceTemplate("invoiceTemplate");

    public final NumberPath<Long> amount = createNumber("amount", Long.class);

    public final DateTimePath<java.util.Date> createDatetime = createDateTime("createDatetime", java.util.Date.class);

    public final StringPath createUser = createString("createUser");

    public final StringPath description = createString("description");

    public final NumberPath<Long> fromShopId = createNumber("fromShopId", Long.class);

    public final NumberPath<Long> invoiceTemplateId = createNumber("invoiceTemplateId", Long.class);

    public final NumberPath<Long> invoiceTemplateTypeId = createNumber("invoiceTemplateTypeId", Long.class);

    public final NumberPath<Long> ownerId = createNumber("ownerId", Long.class);

    public final StringPath ownerType = createString("ownerType");

    public final DateTimePath<java.util.Date> updateDatetime = createDateTime("updateDatetime", java.util.Date.class);

    public final StringPath updateUser = createString("updateUser");

    public final NumberPath<Long> usedAmount = createNumber("usedAmount", Long.class);

    public QInvoiceTemplate(String variable) {
        super(InvoiceTemplate.class, forVariable(variable));
    }

    public QInvoiceTemplate(Path<? extends InvoiceTemplate> path) {
        super(path.getType(), path.getMetadata());
    }

    public QInvoiceTemplate(PathMetadata<?> metadata) {
        super(InvoiceTemplate.class, metadata);
    }

}

