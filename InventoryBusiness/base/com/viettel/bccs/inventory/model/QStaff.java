package com.viettel.bccs.inventory.model;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * QStaff is a Querydsl query type for Staff
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStaff extends EntityPathBase<Staff> {

    private static final long serialVersionUID = -1978206513;

    public static final QStaff staff = new QStaff("staff");

    public final StringPath address = createString("address");

    public final StringPath areaCode = createString("areaCode");

    public final StringPath bankplusMobile = createString("bankplusMobile");

    public final DateTimePath<java.util.Date> birthday = createDateTime("birthday", java.util.Date.class);

    public final StringPath businessLicence = createString("businessLicence");

    public final NumberPath<Long> businessMethod = createNumber("businessMethod", Long.class);

    public final NumberPath<Long> channelTypeId = createNumber("channelTypeId", Long.class);

    public final DateTimePath<java.util.Date> contractFromDate = createDateTime("contractFromDate", java.util.Date.class);

    public final NumberPath<Long> contractMethod = createNumber("contractMethod", Long.class);

    public final StringPath contractNo = createString("contractNo");

    public final DateTimePath<java.util.Date> contractToDate = createDateTime("contractToDate", java.util.Date.class);

    public final NumberPath<Long> depositValue = createNumber("depositValue", Long.class);

    public final StringPath discountPolicy = createString("discountPolicy");

    public final StringPath district = createString("district");

    public final StringPath email = createString("email");

    public final SimplePath<java.io.Serializable> fileAttach = createSimple("fileAttach", java.io.Serializable.class);

    public final StringPath fileName = createString("fileName");

    public final NumberPath<Long> hasEquipment = createNumber("hasEquipment", Long.class);

    public final NumberPath<Long> hasTin = createNumber("hasTin", Long.class);

    public final StringPath home = createString("home");

    public final DateTimePath<java.util.Date> idIssueDate = createDateTime("idIssueDate", java.util.Date.class);

    public final StringPath idIssuePlace = createString("idIssuePlace");

    public final StringPath idNo = createString("idNo");

    public final StringPath isdn = createString("isdn");

    public final DateTimePath<java.util.Date> lastLockTime = createDateTime("lastLockTime", java.util.Date.class);

    public final DateTimePath<java.util.Date> lastModified = createDateTime("lastModified", java.util.Date.class);

    public final NumberPath<Long> lockStatus = createNumber("lockStatus", Long.class);

    public final StringPath name = createString("name");

    public final StringPath note = createString("note");

    public final StringPath pin = createString("pin");

    public final StringPath pointOfSale = createString("pointOfSale");

    public final NumberPath<Long> pointOfSaleType = createNumber("pointOfSaleType", Long.class);

    public final StringPath precinct = createString("precinct");

    public final StringPath pricePolicy = createString("pricePolicy");

    public final StringPath province = createString("province");

    public final StringPath serial = createString("serial");

    public final NumberPath<Long> shopId = createNumber("shopId", Long.class);

    public final NumberPath<Long> shopOwnerId = createNumber("shopOwnerId", Long.class);

    public final StringPath staffCode = createString("staffCode");

    public final NumberPath<Long> staffId = createNumber("staffId", Long.class);

    public final NumberPath<Long> staffOwnerId = createNumber("staffOwnerId", Long.class);

    public final StringPath staffOwnType = createString("staffOwnType");

    public final StringPath status = createString("status");

    public final NumberPath<Long> stockNum = createNumber("stockNum", Long.class);

    public final NumberPath<Long> stockNumImp = createNumber("stockNumImp", Long.class);

    public final StringPath street = createString("street");

    public final StringPath streetBlock = createString("streetBlock");

    public final NumberPath<Long> subOwnerId = createNumber("subOwnerId", Long.class);

    public final NumberPath<Long> subOwnerType = createNumber("subOwnerType", Long.class);

    public final StringPath tel = createString("tel");

    public final StringPath tin = createString("tin");

    public final StringPath ttnsCode = createString("ttnsCode");

    public final NumberPath<Long> type = createNumber("type", Long.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QStaff(String variable) {
        super(Staff.class, forVariable(variable));
    }

    public QStaff(Path<? extends Staff> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QStaff(PathMetadata<?> metadata) {
        super(Staff.class, metadata);
    }

}

