package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QStockInspectSys is a Querydsl query type for StockInspectSys
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockInspectSys extends EntityPathBase<StockInspectSys> {

    private static final long serialVersionUID = 971597885L;

    public static final QStockInspectSys stockInspectSys = new QStockInspectSys("stockInspectSys");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final StringPath fromSerial = createString("fromSerial");

    public final NumberPath<Long> quantity = createNumber("quantity", Long.class);

    public final NumberPath<Long> stockInspectId = createNumber("stockInspectId", Long.class);

    public final NumberPath<Long> stockInspectSysId = createNumber("stockInspectSysId", Long.class);

    public final StringPath toSerial = createString("toSerial");

    public QStockInspectSys(String variable) {
        super(StockInspectSys.class, forVariable(variable));
    }

    public QStockInspectSys(Path<? extends StockInspectSys> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockInspectSys(PathMetadata<?> metadata) {
        super(StockInspectSys.class, metadata);
    }

}

