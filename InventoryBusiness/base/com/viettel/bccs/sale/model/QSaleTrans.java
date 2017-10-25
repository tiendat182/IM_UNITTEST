package com.viettel.bccs.sale.model;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * QSaleTrans is a Querydsl query type for SaleTrans
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QSaleTrans extends EntityPathBase<SaleTrans> {

    private static final long serialVersionUID = -1295910128L;

    public static final QSaleTrans saleTrans = new QSaleTrans("saleTrans");

    public final StringPath actionCode = createString("actionCode");

    public final StringPath address = createString("address");

    public final NumberPath<Long> amountModel = createNumber("amountModel", Long.class);

    public final NumberPath<Long> amountNotTax = createNumber("amountNotTax", Long.class);

    public final NumberPath<Long> amountService = createNumber("amountService", Long.class);

    public final NumberPath<Long> amountTax = createNumber("amountTax", Long.class);

    public final DateTimePath<java.util.Date> approverDate = createDateTime("approverDate", java.util.Date.class);

    public final StringPath approverUser = createString("approverUser");

    public final StringPath checkStock = createString("checkStock");

    public final StringPath company = createString("company");

    public final StringPath contractNo = createString("contractNo");

    public final NumberPath<Long> createStaffId = createNumber("createStaffId", Long.class);

    public final StringPath custName = createString("custName");

    public final DateTimePath<java.util.Date> destroyDate = createDateTime("destroyDate", java.util.Date.class);

    public final StringPath destroyUser = createString("destroyUser");

    public final NumberPath<Long> discount = createNumber("discount", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.util.Date> invoiceCreateDate = createDateTime("invoiceCreateDate", java.util.Date.class);

    public final NumberPath<Long> invoiceUsedId = createNumber("invoiceUsedId", Long.class);

    public final StringPath isdn = createString("isdn");

    public final SimplePath<java.io.Serializable> lstStockIsdn = createSimple("lstStockIsdn", java.io.Serializable.class);

    public final SimplePath<java.io.Serializable> lstStockSim = createSimple("lstStockSim", java.io.Serializable.class);

    public final StringPath note = createString("note");

    public final StringPath payMethod = createString("payMethod");

    public final NumberPath<java.math.BigDecimal> promotion = createNumber("promotion", java.math.BigDecimal.class);

    public final NumberPath<Long> reasonId = createNumber("reasonId", Long.class);

    public final NumberPath<Long> saleServiceId = createNumber("saleServiceId", Long.class);

    public final NumberPath<Long> saleServicePriceId = createNumber("saleServicePriceId", Long.class);

    public final StringPath saleTransCode = createString("saleTransCode");

    public final DateTimePath<java.util.Date> saleTransDate = createDateTime("saleTransDate", java.util.Date.class);

    public final NumberPath<Long> saleTransId = createNumber("saleTransId", Long.class);

    public final StringPath saleTransType = createString("saleTransType");

    public final StringPath serial = createString("serial");

    public final StringPath shopCode = createString("shopCode");

    public final NumberPath<Long> shopId = createNumber("shopId", Long.class);

    public final NumberPath<Long> staffId = createNumber("staffId", Long.class);

    public final StringPath status = createString("status");

    public final NumberPath<Long> stockTransId = createNumber("stockTransId", Long.class);

    public final NumberPath<Long> subId = createNumber("subId", Long.class);

    public final NumberPath<Long> tax = createNumber("tax", Long.class);

    public final NumberPath<Long> telecomServiceId = createNumber("telecomServiceId", Long.class);

    public final StringPath telNumber = createString("telNumber");
    public final StringPath email = createString("email");

    public final StringPath tin = createString("tin");

    public final StringPath transferGoods = createString("transferGoods");

    public final NumberPath<Long> transResult = createNumber("transResult", Long.class);

    public final NumberPath<Long> vat = createNumber("vat", Long.class);

    public final StringPath requestType = createString("requestType");

    public QSaleTrans(String variable) {
        super(SaleTrans.class, forVariable(variable));
    }

    public QSaleTrans(Path<? extends SaleTrans> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSaleTrans(PathMetadata<?> metadata) {
        super(SaleTrans.class, metadata);
    }

}

