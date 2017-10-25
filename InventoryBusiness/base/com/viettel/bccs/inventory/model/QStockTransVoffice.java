package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QStockTransVoffice is a Querydsl query type for StockTransVoffice
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockTransVoffice extends EntityPathBase<StockTransVoffice> {

    private static final long serialVersionUID = -1362322930L;

    public static final QStockTransVoffice stockTransVoffice = new QStockTransVoffice("stockTransVoffice");

    public final StringPath accountName = createString("accountName");

    public final StringPath accountPass = createString("accountPass");

    public final StringPath actionCode = createString("actionCode");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final NumberPath<Long> createUserId = createNumber("createUserId", Long.class);

    public final DateTimePath<java.util.Date> deniedDate = createDateTime("deniedDate", java.util.Date.class);

    public final StringPath deniedUser = createString("deniedUser");

    public final StringPath errorCode = createString("errorCode");

    public final StringPath errorCodeDescription = createString("errorCodeDescription");

    public final NumberPath<Long> findSerial = createNumber("findSerial", Long.class);

    public final DateTimePath<java.util.Date> lastModify = createDateTime("lastModify", java.util.Date.class);

    public final StringPath note = createString("note");

    public final StringPath prefixTemplate = createString("prefixTemplate");

    public final StringPath receiptNo = createString("receiptNo");

    public final StringPath responseCode = createString("responseCode");

    public final StringPath responseCodeDescription = createString("responseCodeDescription");

    public final NumberPath<Long> retry = createNumber("retry", Long.class);

    public final StringPath signUserList = createString("signUserList");

    public final StringPath status = createString("status");

    public final NumberPath<Long> stockTransActionId = createNumber("stockTransActionId", Long.class);

    public final StringPath stockTransVofficeId = createString("stockTransVofficeId");

    public QStockTransVoffice(String variable) {
        super(StockTransVoffice.class, forVariable(variable));
    }

    public QStockTransVoffice(Path<? extends StockTransVoffice> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockTransVoffice(PathMetadata<?> metadata) {
        super(StockTransVoffice.class, metadata);
    }

}

