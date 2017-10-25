package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QStockCheckReport is a Querydsl query type for StockCheckReport
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockCheckReport extends EntityPathBase<StockCheckReport> {

    private static final long serialVersionUID = -1986127688L;

    public static final QStockCheckReport stockCheckReport = new QStockCheckReport("stockCheckReport");

    public final DateTimePath<java.util.Date> checkDate = createDateTime("checkDate", java.util.Date.class);

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final StringPath createUser = createString("createUser");

    public final SimplePath<java.io.Serializable> fileContent = createSimple("fileContent", java.io.Serializable.class);

    public final StringPath fileName = createString("fileName");

    public final StringPath ownerCode = createString("ownerCode");

    public final NumberPath<Long> ownerId = createNumber("ownerId", Long.class);

    public final StringPath ownerName = createString("ownerName");

    public final NumberPath<Short> ownerType = createNumber("ownerType", Short.class);

    public final NumberPath<Long> stockCheckReportId = createNumber("stockCheckReportId", Long.class);

    public QStockCheckReport(String variable) {
        super(StockCheckReport.class, forVariable(variable));
    }

    public QStockCheckReport(Path<? extends StockCheckReport> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockCheckReport(PathMetadata<?> metadata) {
        super(StockCheckReport.class, metadata);
    }

}

