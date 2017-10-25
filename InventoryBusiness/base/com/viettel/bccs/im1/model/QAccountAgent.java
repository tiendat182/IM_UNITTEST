package com.viettel.bccs.im1.model;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.DateTimePath;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;

import javax.annotation.Generated;
import java.util.Date;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * QAccountAgent is a Querydsl query type for AccountAgent
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QAccountAgent extends EntityPathBase<AccountAgent> {

    private static final long serialVersionUID = 1179867545L;

    public static final QAccountAgent accountAgent = new QAccountAgent("accountAgent");

    public final NumberPath<Long> accountId = createNumber("accountId", Long.class);

    public final StringPath accountType = createString("accountType");

    public final NumberPath<Long> agentId = createNumber("agentId", Long.class);

    public final DateTimePath<Date> birthDate = createDateTime("birthDate", Date.class);

    public final NumberPath<Long> centreId = createNumber("centreId", Long.class);

    public final NumberPath<Long> checkIsdn = createNumber("checkIsdn", Long.class);

    public final NumberPath<Long> checkVat = createNumber("checkVat", Long.class);

    public final NumberPath<Long> commBalance = createNumber("commBalance", Long.class);

    public final DateTimePath<Date> commBalanceDate = createDateTime("commBalanceDate", Date.class);

    public final StringPath contactNo = createString("contactNo");

    public final DateTimePath<Date> createDate = createDateTime("createDate", Date.class);

    public final DateTimePath<Date> creditExpireTime = createDateTime("creditExpireTime", Date.class);

    public final NumberPath<Long> creditTimeLimit = createNumber("creditTimeLimit", Long.class);

    public final NumberPath<Long> currentDebtPayment = createNumber("currentDebtPayment", Long.class);

    public final DateTimePath<Date> dateCreated = createDateTime("dateCreated", Date.class);

    public final StringPath district = createString("district");

    public final StringPath email = createString("email");

    public final StringPath fax = createString("fax");

    public final StringPath iccid = createString("iccid");

    public final StringPath idNo = createString("idNo");

    public final StringPath isdn = createString("isdn");

    public final DateTimePath<Date> issueDate = createDateTime("issueDate", Date.class);

    public final StringPath issuePlace = createString("issuePlace");

    public final DateTimePath<Date> lastModified = createDateTime("lastModified", Date.class);

    public final DateTimePath<Date> lastTimeRecover = createDateTime("lastTimeRecover", Date.class);

    public final StringPath lastUpdateIpAddress = createString("lastUpdateIpAddress");

    public final StringPath lastUpdateKey = createString("lastUpdateKey");

    public final DateTimePath<Date> lastUpdateTime = createDateTime("lastUpdateTime", Date.class);

    public final StringPath lastUpdateUser = createString("lastUpdateUser");

    public final NumberPath<Long> limitDebtPayment = createNumber("limitDebtPayment", Long.class);

    public final NumberPath<Long> loginFailureCount = createNumber("loginFailureCount", Long.class);

    public final NumberPath<Long> maxCreditNumber = createNumber("maxCreditNumber", Long.class);

    public final NumberPath<Long> maxCreditValue = createNumber("maxCreditValue", Long.class);

    public final NumberPath<Long> minPayPerMonth = createNumber("minPayPerMonth", Long.class);

    public final StringPath mpin = createString("mpin");

    public final DateTimePath<Date> mpinExpireDate = createDateTime("mpinExpireDate", Date.class);

    public final StringPath mpinStatus = createString("mpinStatus");

    public final StringPath msisdn = createString("msisdn");

    public final NumberPath<Long> numAddMoney = createNumber("numAddMoney", Long.class);

    public final NumberPath<Long> numPosHpn = createNumber("numPosHpn", Long.class);

    public final NumberPath<Long> numPosMob = createNumber("numPosMob", Long.class);

    public final NumberPath<Long> numPreHpn = createNumber("numPreHpn", Long.class);

    public final NumberPath<Long> numPreMob = createNumber("numPreMob", Long.class);

    public final NumberPath<Long> objectDestroy = createNumber("objectDestroy", Long.class);

    public final StringPath outletAddress = createString("outletAddress");

    public final StringPath ownerCode = createString("ownerCode");

    public final NumberPath<Long> ownerId = createNumber("ownerId", Long.class);

    public final StringPath ownerName = createString("ownerName");

    public final NumberPath<Long> ownerType = createNumber("ownerType", Long.class);

    public final NumberPath<Long> parentId = createNumber("parentId", Long.class);

    public final StringPath password = createString("password");

    public final StringPath precinct = createString("precinct");

    public final StringPath province = createString("province");

    public final StringPath sapCode = createString("sapCode");

    public final StringPath secureQuestion = createString("secureQuestion");

    public final StringPath serial = createString("serial");

    public final StringPath sex = createString("sex");

    public final StringPath staffCode = createString("staffCode");

    public final NumberPath<Long> status = createNumber("status", Long.class);

    public final NumberPath<Long> statusAnypay = createNumber("statusAnypay", Long.class);

    public final NumberPath<Long> statusAnypay2 = createNumber("statusAnypay2", Long.class);

    public final StringPath tin = createString("tin");

    public final StringPath tradeName = createString("tradeName");

    public final StringPath transType = createString("transType");

    public final StringPath userCreated = createString("userCreated");

    public final NumberPath<Long> userRecover = createNumber("userRecover", Long.class);

    public final StringPath useSalt = createString("useSalt");

    public final DateTimePath<Date> workingTime = createDateTime("workingTime", Date.class);

    public QAccountAgent(String variable) {
        super(AccountAgent.class, forVariable(variable));
    }

    public QAccountAgent(Path<? extends AccountAgent> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAccountAgent(PathMetadata<?> metadata) {
        super(AccountAgent.class, metadata);
    }

}

