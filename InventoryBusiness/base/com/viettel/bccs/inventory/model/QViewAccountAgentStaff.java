package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QViewAccountAgentStaff is a Querydsl query type for ViewAccountAgentStaff
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QViewAccountAgentStaff extends EntityPathBase<ViewAccountAgentStaff> {

    private static final long serialVersionUID = -1206799695L;

    public static final QViewAccountAgentStaff viewAccountAgentStaff = new QViewAccountAgentStaff("viewAccountAgentStaff");

    public final NumberPath<Long> accountId = createNumber("accountId", Long.class);

    public final StringPath accountType = createString("accountType");

    public final StringPath address = createString("address");

    public final NumberPath<Long> agentId = createNumber("agentId", Long.class);

    public final StringPath bankplusMobile = createString("bankplusMobile");

    public final DateTimePath<java.util.Date> birthDate = createDateTime("birthDate", java.util.Date.class);

    public final NumberPath<Long> centreId = createNumber("centreId", Long.class);

    public final NumberPath<Long> channelTypeId = createNumber("channelTypeId", Long.class);

    public final NumberPath<Long> checkIsdn = createNumber("checkIsdn", Long.class);

    public final NumberPath<Long> checkVat = createNumber("checkVat", Long.class);

    public final StringPath contactNo = createString("contactNo");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final DateTimePath<java.util.Date> dateCreated = createDateTime("dateCreated", java.util.Date.class);

    public final StringPath district = createString("district");

    public final StringPath email = createString("email");

    public final StringPath fax = createString("fax");

    public final NumberPath<Long> hasTin = createNumber("hasTin", Long.class);

    public final StringPath iccid = createString("iccid");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath idNo = createString("idNo");

    public final StringPath idNoAccount = createString("idNoAccount");

    public final StringPath isdn = createString("isdn");

    public final DateTimePath<java.util.Date> issueDate = createDateTime("issueDate", java.util.Date.class);

    public final StringPath issuePlace = createString("issuePlace");

    public final DateTimePath<java.util.Date> lastModified = createDateTime("lastModified", java.util.Date.class);

    public final NumberPath<Long> loginFailureCount = createNumber("loginFailureCount", Long.class);

    public final StringPath mpin = createString("mpin");

    public final DateTimePath<java.util.Date> mpinExpireDate = createDateTime("mpinExpireDate", java.util.Date.class);

    public final StringPath mpinStatus = createString("mpinStatus");

    public final StringPath msisdn = createString("msisdn");

    public final StringPath name = createString("name");

    public final NumberPath<Long> numAddMoney = createNumber("numAddMoney", Long.class);

    public final NumberPath<Long> numPosHpn = createNumber("numPosHpn", Long.class);

    public final NumberPath<Long> numPosMob = createNumber("numPosMob", Long.class);

    public final NumberPath<Long> numPreHpn = createNumber("numPreHpn", Long.class);

    public final NumberPath<Long> numPreMob = createNumber("numPreMob", Long.class);

    public final StringPath outletAddress = createString("outletAddress");

    public final StringPath ownerName = createString("ownerName");

    public final NumberPath<Long> parentId = createNumber("parentId", Long.class);

    public final StringPath password = createString("password");

    public final StringPath pointOfSale = createString("pointOfSale");

    public final StringPath precinct = createString("precinct");

    public final StringPath province = createString("province");

    public final StringPath sapCode = createString("sapCode");

    public final StringPath secureQuestion = createString("secureQuestion");

    public final StringPath serial = createString("serial");

    public final StringPath sex = createString("sex");

    public final NumberPath<Long> shopId = createNumber("shopId", Long.class);

    public final StringPath staffCode = createString("staffCode");

    public final StringPath staffCodeAgent = createString("staffCodeAgent");

    public final NumberPath<Long> staffId = createNumber("staffId", Long.class);

    public final NumberPath<Long> staffOwnerId = createNumber("staffOwnerId", Long.class);

    public final NumberPath<Long> status = createNumber("status", Long.class);

    public final NumberPath<Long> statusChannel = createNumber("statusChannel", Long.class);

    public final StringPath tel = createString("tel");

    public final StringPath tin = createString("tin");

    public final StringPath tinStaff = createString("tinStaff");

    public final StringPath tradeName = createString("tradeName");

    public final StringPath ttnsCode = createString("ttnsCode");

    public QViewAccountAgentStaff(String variable) {
        super(ViewAccountAgentStaff.class, forVariable(variable));
    }

    public QViewAccountAgentStaff(Path<? extends ViewAccountAgentStaff> path) {
        super(path.getType(), path.getMetadata());
    }

    public QViewAccountAgentStaff(PathMetadata<?> metadata) {
        super(ViewAccountAgentStaff.class, metadata);
    }

}

