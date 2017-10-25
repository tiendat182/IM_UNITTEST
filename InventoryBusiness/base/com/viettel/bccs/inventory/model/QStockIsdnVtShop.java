package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QStockIsdnVtShop is a Querydsl query type for StockIsdnVtShop
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockIsdnVtShop extends EntityPathBase<StockIsdnVtShop> {

    private static final long serialVersionUID = -2037799092L;

    public static final QStockIsdnVtShop stockIsdnVtShop = new QStockIsdnVtShop("stockIsdnVtShop");

    public final StringPath address = createString("address");

    public final StringPath areaCode = createString("areaCode");

    public final StringPath contactIsdn = createString("contactIsdn");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final StringPath customerName = createString("customerName");

    public final StringPath idNo = createString("idNo");

    public final StringPath isdn = createString("isdn");

    public final DateTimePath<java.util.Date> lastModify = createDateTime("lastModify", java.util.Date.class);

    public final StringPath otp = createString("otp");

    public final NumberPath<Long> staffUpdate = createNumber("staffUpdate", Long.class);

    public final StringPath status = createString("status");

    public final StringPath viettelshopId = createString("viettelshopId");

    public QStockIsdnVtShop(String variable) {
        super(StockIsdnVtShop.class, forVariable(variable));
    }

    public QStockIsdnVtShop(Path<? extends StockIsdnVtShop> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockIsdnVtShop(PathMetadata<?> metadata) {
        super(StockIsdnVtShop.class, metadata);
    }

}

