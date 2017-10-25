package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QStockSimInfo is a Querydsl query type for StockSimInfo
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockSimInfo extends EntityPathBase<StockSimInfo> {

    private static final long serialVersionUID = -1792690047L;

    public static final QStockSimInfo stockSimInfo = new QStockSimInfo("stockSimInfo");

    public final StringPath a3a8 = createString("a3a8");

    public final StringPath adm1 = createString("adm1");

    public final DateTimePath<java.util.Date> aucRegDate = createDateTime("aucRegDate", java.util.Date.class);

    public final DateTimePath<java.util.Date> aucRemoveDate = createDateTime("aucRemoveDate", java.util.Date.class);

    public final NumberPath<Long> aucStatus = createNumber("aucStatus", Long.class);

    public final DateTimePath<java.util.Date> connectionDate = createDateTime("connectionDate", java.util.Date.class);

    public final NumberPath<Long> connectionStatus = createNumber("connectionStatus", Long.class);

    public final StringPath connectionType = createString("connectionType");

    public final StringPath eki = createString("eki");

    public final StringPath hlrId = createString("hlrId");

    public final DateTimePath<java.util.Date> hlrRegDate = createDateTime("hlrRegDate", java.util.Date.class);

    public final DateTimePath<java.util.Date> hlrRemoveDate = createDateTime("hlrRemoveDate", java.util.Date.class);

    public final NumberPath<Long> hlrStatus = createNumber("hlrStatus", Long.class);

    public final StringPath iccid = createString("iccid");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imsi = createString("imsi");

    public final StringPath kind = createString("kind");

    public final StringPath pin = createString("pin");

    public final StringPath pin2 = createString("pin2");

    public final StringPath puk = createString("puk");

    public final StringPath puk2 = createString("puk2");

    public final StringPath simType = createString("simType");

    public QStockSimInfo(String variable) {
        super(StockSimInfo.class, forVariable(variable));
    }

    public QStockSimInfo(Path<? extends StockSimInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockSimInfo(PathMetadata<?> metadata) {
        super(StockSimInfo.class, metadata);
    }

}

