package com.viettel.bccs.im1.model;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.DateTimePath;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * QStockTransIm1 is a Querydsl query type for StockTransIm1
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockTransIm1 extends EntityPathBase<StockTransIm1> {

    private static final long serialVersionUID = 1092333801L;

    public static final QStockTransIm1 stockTransIm1 = new QStockTransIm1("stockTransIm1");

    public final StringPath account = createString("account");

    public final NumberPath<Long> accType = createNumber("accType", Long.class);

    public final DateTimePath<java.util.Date> approveDate = createDateTime("approveDate", java.util.Date.class);

    public final NumberPath<Long> approveReasonId = createNumber("approveReasonId", Long.class);

    public final NumberPath<Long> approveUserId = createNumber("approveUserId", Long.class);

    public final NumberPath<Long> bankplusStatus = createNumber("bankplusStatus", Long.class);

    public final NumberPath<Long> checkErp = createNumber("checkErp", Long.class);

    public final DateTimePath<java.util.Date> createDatetime = createDateTime("createDatetime", java.util.Date.class);

    public final NumberPath<Long> depositStatus = createNumber("depositStatus", Long.class);

    public final NumberPath<Long> drawStatus = createNumber("drawStatus", Long.class);

    public final NumberPath<Long> fromOwnerId = createNumber("fromOwnerId", Long.class);

    public final NumberPath<Long> fromOwnerType = createNumber("fromOwnerType", Long.class);

    public final NumberPath<Long> fromStockTransId = createNumber("fromStockTransId", Long.class);

    public final NumberPath<Long> goodsWeight = createNumber("goodsWeight", Long.class);

    public final NumberPath<Long> isAutoGen = createNumber("isAutoGen", Long.class);

    public final StringPath note = createString("note");

    public final StringPath partnerContractNo = createString("partnerContractNo");

    public final NumberPath<Long> payStatus = createNumber("payStatus", Long.class);

    public final DateTimePath<java.util.Date> processEnd = createDateTime("processEnd", java.util.Date.class);

    public final NumberPath<Long> processOffline = createNumber("processOffline", Long.class);

    public final StringPath processResult = createString("processResult");

    public final DateTimePath<java.util.Date> processStart = createDateTime("processStart", java.util.Date.class);

    public final DateTimePath<java.util.Date> realTransDate = createDateTime("realTransDate", java.util.Date.class);

    public final NumberPath<Long> realTransUserId = createNumber("realTransUserId", Long.class);

    public final NumberPath<Long> reasonId = createNumber("reasonId", Long.class);

    public final NumberPath<Long> regionStockTransId = createNumber("regionStockTransId", Long.class);

    public final DateTimePath<java.util.Date> rejectDate = createDateTime("rejectDate", java.util.Date.class);

    public final StringPath rejectNote = createString("rejectNote");

    public final NumberPath<Long> rejectReasonId = createNumber("rejectReasonId", Long.class);

    public final NumberPath<Long> rejectUserId = createNumber("rejectUserId", Long.class);

    public final StringPath requestId = createString("requestId");

    public final StringPath stockBase = createString("stockBase");

    public final NumberPath<Long> stockTransId = createNumber("stockTransId", Long.class);

    public final NumberPath<Long> stockTransStatus = createNumber("stockTransStatus", Long.class);

    public final NumberPath<Long> stockTransType = createNumber("stockTransType", Long.class);

    public final StringPath synStatus = createString("synStatus");

    public final NumberPath<Long> toOwnerId = createNumber("toOwnerId", Long.class);

    public final NumberPath<Long> toOwnerType = createNumber("toOwnerType", Long.class);

    public final NumberPath<Long> totalDebit = createNumber("totalDebit", Long.class);

    public final NumberPath<Long> transport = createNumber("transport", Long.class);

    public final StringPath troubleType = createString("troubleType");

    public final NumberPath<Long> valid = createNumber("valid", Long.class);

    public final NumberPath<Long> warrantyStock = createNumber("warrantyStock", Long.class);

    public QStockTransIm1(String variable) {
        super(StockTransIm1.class, forVariable(variable));
    }

    public QStockTransIm1(Path<? extends StockTransIm1> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockTransIm1(PathMetadata<?> metadata) {
        super(StockTransIm1.class, metadata);
    }

}

