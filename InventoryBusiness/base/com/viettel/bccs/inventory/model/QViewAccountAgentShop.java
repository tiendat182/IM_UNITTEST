package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QViewAccountAgentShop is a Querydsl query type for ViewAccountAgentShop
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QViewAccountAgentShop extends EntityPathBase<ViewAccountAgentShop> {

    private static final long serialVersionUID = -1424413435L;

    public static final QViewAccountAgentShop viewAccountAgentShop = new QViewAccountAgentShop("viewAccountAgentShop");

    public final NumberPath<Long> accountId = createNumber("accountId", Long.class);

    public final StringPath accountType = createString("accountType");

    public final StringPath address = createString("address");

    public final NumberPath<Long> agentId = createNumber("agentId", Long.class);

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

    public final StringPath iccid = createString("iccid");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath idNo = createString("idNo");

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

    public final StringPath outletAddress = createString("outletAddress");

    public final StringPath ownerName = createString("ownerName");

    public final NumberPath<Long> parentId = createNumber("parentId", Long.class);

    public final NumberPath<Long> parentShopId = createNumber("parentShopId", Long.class);

    public final StringPath password = createString("password");

    public final StringPath precinct = createString("precinct");

    public final StringPath province = createString("province");

    public final StringPath sapCode = createString("sapCode");

    public final StringPath secureQuestion = createString("secureQuestion");

    public final StringPath serial = createString("serial");

    public final StringPath sex = createString("sex");

    public final StringPath shopCode = createString("shopCode");

    public final NumberPath<Long> shopId = createNumber("shopId", Long.class);

    public final StringPath staffCode = createString("staffCode");

    public final NumberPath<Long> status = createNumber("status", Long.class);

    public final NumberPath<Long> statusChannel = createNumber("statusChannel", Long.class);

    public final StringPath telNumber = createString("telNumber");

    public final StringPath tin = createString("tin");

    public final StringPath tradeName = createString("tradeName");

    public QViewAccountAgentShop(String variable) {
        super(ViewAccountAgentShop.class, forVariable(variable));
    }

    public QViewAccountAgentShop(Path<? extends ViewAccountAgentShop> path) {
        super(path.getType(), path.getMetadata());
    }

    public QViewAccountAgentShop(PathMetadata<?> metadata) {
        super(ViewAccountAgentShop.class, metadata);
    }

}

