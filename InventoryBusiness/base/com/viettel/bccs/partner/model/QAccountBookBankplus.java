package com.viettel.bccs.partner.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QAccountBookBankplus is a Querydsl query type for AccountBookBankplus
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QAccountBookBankplus extends EntityPathBase<AccountBookBankplus> {

    private static final long serialVersionUID = -2031086566L;

    public static final QAccountBookBankplus accountBookBankplus = new QAccountBookBankplus("accountBookBankplus");

    public final NumberPath<Long> accountId = createNumber("accountId", Long.class);

    public final NumberPath<Long> amountRequest = createNumber("amountRequest", Long.class);

    public final StringPath appName = createString("appName");

    public final StringPath bankCode = createString("bankCode");

    public final NumberPath<Long> closingBalance = createNumber("closingBalance", Long.class);

    public final StringPath cmRequest = createString("cmRequest");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final NumberPath<Long> debitRequest = createNumber("debitRequest", Long.class);

    public final StringPath description = createString("description");

    public final StringPath descriptionBankplus = createString("descriptionBankplus");

    public final StringPath ipAddress = createString("ipAddress");

    public final StringPath isdn = createString("isdn");

    public final StringPath isdnBankPlus = createString("isdnBankPlus");

    public final NumberPath<Long> openingBalance = createNumber("openingBalance", Long.class);

    public final DateTimePath<java.util.Date> realTransDate = createDateTime("realTransDate", java.util.Date.class);

    public final NumberPath<Long> receiptId = createNumber("receiptId", Long.class);

    public final NumberPath<Long> requestId = createNumber("requestId", Long.class);

    public final NumberPath<Long> requestType = createNumber("requestType", Long.class);

    public final StringPath responseCode = createString("responseCode");

    public final DateTimePath<java.util.Date> returnDate = createDateTime("returnDate", java.util.Date.class);

    public final NumberPath<Long> status = createNumber("status", Long.class);

    public final NumberPath<Long> stockTransId = createNumber("stockTransId", Long.class);

    public final StringPath transactionCp = createString("transactionCp");

    public final StringPath userRequest = createString("userRequest");

    public QAccountBookBankplus(String variable) {
        super(AccountBookBankplus.class, forVariable(variable));
    }

    public QAccountBookBankplus(Path<? extends AccountBookBankplus> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAccountBookBankplus(PathMetadata<?> metadata) {
        super(AccountBookBankplus.class, metadata);
    }

}

