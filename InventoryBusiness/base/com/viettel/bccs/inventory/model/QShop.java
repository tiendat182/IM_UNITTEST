package com.viettel.bccs.inventory.model;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.DateTimePath;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * QShop is a Querydsl query type for Shop
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QShop extends EntityPathBase<Shop> {

    private static final long serialVersionUID = 1503401800L;

    public static final QShop shop1 = new QShop("shop1");

    public final StringPath account = createString("account");

    public final StringPath address = createString("address");

    public final StringPath areaCode = createString("areaCode");

    public final StringPath bankName = createString("bankName");

    public final StringPath bankplusMobile = createString("bankplusMobile");

    public final StringPath businessLicence = createString("businessLicence");

    public final StringPath centerCode = createString("centerCode");

    public final NumberPath<Long> channelTypeId = createNumber("channelTypeId", Long.class);

    public final StringPath company = createString("company");

    public final StringPath contactName = createString("contactName");

    public final StringPath contactTitle = createString("contactTitle");

    public final DateTimePath<java.util.Date> contractFromDate = createDateTime("contractFromDate", java.util.Date.class);

    public final StringPath contractNo = createString("contractNo");

    public final DateTimePath<java.util.Date> contractToDate = createDateTime("contractToDate", java.util.Date.class);

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final NumberPath<Long> depositValue = createNumber("depositValue", Long.class);

    public final StringPath description = createString("description");

    public final StringPath discountPolicy = createString("discountPolicy");

    public final StringPath district = createString("district");

    public final StringPath email = createString("email");

    public final StringPath fax = createString("fax");

    public final StringPath fileName = createString("fileName");

    public final StringPath home = createString("home");

    public final DateTimePath<java.util.Date> idIssueDate = createDateTime("idIssueDate", java.util.Date.class);

    public final StringPath idIssuePlace = createString("idIssuePlace");

    public final StringPath idNo = createString("idNo");

    public final StringPath name = createString("name");

    public final StringPath oldShopCode = createString("oldShopCode");

    public final NumberPath<Long> parentShopId = createNumber("parentShopId", Long.class);

    public final StringPath parShopCode = createString("parShopCode");

    public final StringPath payComm = createString("payComm");

    public final StringPath precinct = createString("precinct");

    public final StringPath pricePolicy = createString("pricePolicy");

    public final StringPath province = createString("province");

    public final StringPath provinceCode = createString("provinceCode");

    public final StringPath shop = createString("shop");

    public final StringPath shopCode = createString("shopCode");

    public final NumberPath<Long> shopId = createNumber("shopId", Long.class);

    public final StringPath shopPath = createString("shopPath");

    public final StringPath shopType = createString("shopType");

    public final StringPath status = createString("status");

    public final NumberPath<Long> stockNum = createNumber("stockNum", Long.class);

    public final NumberPath<Long> stockNumImp = createNumber("stockNumImp", Long.class);

    public final StringPath street = createString("street");

    public final StringPath streetBlock = createString("streetBlock");

    public final StringPath tel = createString("tel");

    public final StringPath telNumber = createString("telNumber");

    public final StringPath tin = createString("tin");

    public QShop(String variable) {
        super(Shop.class, forVariable(variable));
    }

    public QShop(Path<? extends Shop> path) {
        super(path.getType(), path.getMetadata());
    }

    public QShop(PathMetadata<?> metadata) {
        super(Shop.class, metadata);
    }

}

