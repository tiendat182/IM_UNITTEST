package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QStockKit is a Querydsl query type for StockKit
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockKit extends EntityPathBase<StockKit> {

    private static final long serialVersionUID = -302738638L;

    public static final QStockKit stockKit = new QStockKit("stockKit");

    public final DateTimePath<java.util.Date> aucRegDate = createDateTime("aucRegDate", java.util.Date.class);

    public final DateTimePath<java.util.Date> aucRemoveDate = createDateTime("aucRemoveDate", java.util.Date.class);

    public final ComparablePath<Character> aucStatus = createComparable("aucStatus", Character.class);

    public final NumberPath<Long> bankplusStatus = createNumber("bankplusStatus", Long.class);

    public final NumberPath<Long> checkDial = createNumber("checkDial", Long.class);

    public final DateTimePath<java.util.Date> connectionDate = createDateTime("connectionDate", java.util.Date.class);

    public final NumberPath<Long> connectionStatus = createNumber("connectionStatus", Long.class);

    public final NumberPath<Long> connectionType = createNumber("connectionType", Long.class);

    public final StringPath contractCode = createString("contractCode");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final StringPath createUser = createString("createUser");

    public final NumberPath<Long> depositPrice = createNumber("depositPrice", Long.class);

    public final NumberPath<Long> dialStatus = createNumber("dialStatus", Long.class);

    public final StringPath hlrId = createString("hlrId");

    public final DateTimePath<java.util.Date> hlrRegDate = createDateTime("hlrRegDate", java.util.Date.class);

    public final DateTimePath<java.util.Date> hlrRemoveDate = createDateTime("hlrRemoveDate", java.util.Date.class);

    public final NumberPath<Long> hlrStatus = createNumber("hlrStatus", Long.class);

    public final StringPath iccid = createString("iccid");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imsi = createString("imsi");

    public final StringPath isdn = createString("isdn");

    public final NumberPath<Long> oldOwnerId = createNumber("oldOwnerId", Long.class);

    public final NumberPath<Long> oldOwnerType = createNumber("oldOwnerType", Long.class);

    public final StringPath orderCode = createString("orderCode");

    public final NumberPath<Long> ownerId = createNumber("ownerId", Long.class);

    public final NumberPath<Long> ownerReceiverId = createNumber("ownerReceiverId", Long.class);

    public final NumberPath<Long> ownerReceiverType = createNumber("ownerReceiverType", Long.class);

    public final NumberPath<Long> ownerType = createNumber("ownerType", Long.class);

    public final StringPath pin = createString("pin");

    public final StringPath pin2 = createString("pin2");

    public final StringPath poCode = createString("poCode");

    public final NumberPath<Long> prodOfferId = createNumber("prodOfferId", Long.class);

    public final StringPath puk = createString("puk");

    public final StringPath puk2 = createString("puk2");

    public final StringPath receiverName = createString("receiverName");

    public final DateTimePath<java.util.Date> saleDate = createDateTime("saleDate", java.util.Date.class);

    public final StringPath serial = createString("serial");

    public final NumberPath<Long> serialReal = createNumber("serialReal", Long.class);

    public final StringPath simType = createString("simType");

    public final DateTimePath<java.util.Date> startDateWarranty = createDateTime("startDateWarranty", java.util.Date.class);

    public final NumberPath<Long> stateId = createNumber("stateId", Long.class);

    public final NumberPath<Long> status = createNumber("status", Long.class);

    public final NumberPath<Long> stockTransId = createNumber("stockTransId", Long.class);

    public final NumberPath<Long> telecomServiceId = createNumber("telecomServiceId", Long.class);

    public final DateTimePath<java.util.Date> updateDatetime = createDateTime("updateDatetime", java.util.Date.class);

    public final NumberPath<Long> updateNumber = createNumber("updateNumber", Long.class);

    public final StringPath userSessionId = createString("userSessionId");

    public QStockKit(String variable) {
        super(StockKit.class, forVariable(variable));
    }

    public QStockKit(Path<? extends StockKit> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockKit(PathMetadata<?> metadata) {
        super(StockKit.class, metadata);
    }

}

