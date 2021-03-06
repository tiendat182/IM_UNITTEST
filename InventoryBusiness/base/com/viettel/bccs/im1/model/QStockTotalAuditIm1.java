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
 * QStockTotalAudit is a Querydsl query type for StockTotalAudit
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockTotalAuditIm1 extends EntityPathBase<StockTotalAuditIm1> {

    private static final long serialVersionUID = -1513298149L;

    public static final QStockTotalAuditIm1 stockTotalAudit = new QStockTotalAuditIm1("stockTotalAudit");

    public final NumberPath<Long> stockModelId = createNumber("stockModelId", Long.class);
    public final StringPath stockModelName = createString("stockModelName");
    public final NumberPath<Long> qty = createNumber("qty", Long.class);
    public final NumberPath<Long> qtyIssue = createNumber("qtyIssue", Long.class);
    public final NumberPath<Long> qtyHang = createNumber("qtyHang", Long.class);
    public final NumberPath<Long> qtyBf = createNumber("qtyBf", Long.class);
    public final NumberPath<Long> qtyIssueBf = createNumber("qtyIssueBf", Long.class);
    public final NumberPath<Long> qtyHangBf = createNumber("qtyHangBf", Long.class);
    public final NumberPath<Long> qtyAf = createNumber("qtyAf", Long.class);
    public final NumberPath<Long> qtyIssueAf = createNumber("qtyIssueAf", Long.class);
    public final NumberPath<Long> qtyHangAf = createNumber("qtyHangAf", Long.class);
    public final StringPath stockModelCode = createString("stockModelCode");
    public final NumberPath<Long> stockTypeId = createNumber("stockTypeId", Long.class);
    public final NumberPath<Long> valid = createNumber("valid", Long.class);

    public final NumberPath<Long> availableQuantity = createNumber("availableQuantity", Long.class);

    public final NumberPath<Long> availableQuantityAf = createNumber("availableQuantityAf", Long.class);

    public final NumberPath<Long> availableQuantityBf = createNumber("availableQuantityBf", Long.class);

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final NumberPath<Long> currentQuantity = createNumber("currentQuantity", Long.class);

    public final NumberPath<Long> currentQuantityAf = createNumber("currentQuantityAf", Long.class);

    public final NumberPath<Long> avaiableQuantityAf = createNumber("currentQuantityAf", Long.class);

    public final NumberPath<Long> currentQuantityBf = createNumber("currentQuantityBf", Long.class);

    public final StringPath description = createString("description");

    public final NumberPath<Long> hangQuantity = createNumber("hangQuantity", Long.class);

    public final NumberPath<Long> hangQuantityAf = createNumber("hangQuantityAf", Long.class);

    public final NumberPath<Long> hangQuantityBf = createNumber("hangQuantityBf", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath ownerCode = createString("ownerCode");

    public final NumberPath<Long> ownerId = createNumber("ownerId", Long.class);

    public final StringPath ownerName = createString("ownerName");

    public final NumberPath<Long> ownerType = createNumber("ownerType", Long.class);

    public final StringPath prodOfferCode = createString("prodOfferCode");

    public final NumberPath<Long> prodOfferId = createNumber("prodOfferId", Long.class);

    public final StringPath prodOfferName = createString("prodOfferName");

    public final NumberPath<Long> prodOfferTypeId = createNumber("prodOfferTypeId", Long.class);

    public final NumberPath<Long> reasonId = createNumber("reasonId", Long.class);

    public final StringPath reasonName = createString("reasonName");

    public final StringPath shopCode = createString("shopCode");

    public final StringPath shopCodeLv1 = createString("shopCodeLv1");

    public final StringPath shopCodeLv2 = createString("shopCodeLv2");

    public final StringPath shopCodeLv3 = createString("shopCodeLv3");

    public final StringPath shopCodeLv4 = createString("shopCodeLv4");

    public final StringPath shopCodeLv5 = createString("shopCodeLv5");

    public final StringPath shopCodeLv6 = createString("shopCodeLv6");

    public final NumberPath<Long> shopId = createNumber("shopId", Long.class);

    public final NumberPath<Long> shopIdLv1 = createNumber("shopIdLv1", Long.class);

    public final NumberPath<Long> shopIdLv2 = createNumber("shopIdLv2", Long.class);

    public final NumberPath<Long> shopIdLv3 = createNumber("shopIdLv3", Long.class);

    public final NumberPath<Long> shopIdLv4 = createNumber("shopIdLv4", Long.class);

    public final NumberPath<Long> shopIdLv5 = createNumber("shopIdLv5", Long.class);

    public final NumberPath<Long> shopIdLv6 = createNumber("shopIdLv6", Long.class);

    public final StringPath shopName = createString("shopName");

    public final StringPath shopNameLv1 = createString("shopNameLv1");

    public final StringPath shopNameLv2 = createString("shopNameLv2");

    public final StringPath shopNameLv3 = createString("shopNameLv3");

    public final StringPath shopNameLv4 = createString("shopNameLv4");

    public final StringPath shopNameLv5 = createString("shopNameLv5");

    public final StringPath shopNameLv6 = createString("shopNameLv6");

    public final NumberPath<Long> sourceType = createNumber("sourceType", Long.class);

    public final NumberPath<Long> stateId = createNumber("stateId", Long.class);

    public final NumberPath<Long> status = createNumber("status", Long.class);

    public final StringPath stickCode = createString("stickCode");

    public final NumberPath<Long> synStatus = createNumber("synStatus", Long.class);

    public final StringPath transCode = createString("transCode");

    public final NumberPath<Long> transId = createNumber("transId", Long.class);

    public final NumberPath<Long> transType = createNumber("transType", Long.class);

    public final StringPath userCode = createString("userCode");

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public final StringPath userName = createString("userName");

    public QStockTotalAuditIm1(String variable) {
        super(StockTotalAuditIm1.class, forVariable(variable));
    }

    public QStockTotalAuditIm1(Path<? extends StockTotalAuditIm1> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockTotalAuditIm1(PathMetadata<?> metadata) {
        super(StockTotalAuditIm1.class, metadata);
    }

}

