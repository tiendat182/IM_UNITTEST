package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QImportPartnerDetail is a Querydsl query type for ImportPartnerDetail
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QImportPartnerDetail extends EntityPathBase<ImportPartnerDetail> {

    private static final long serialVersionUID = 206311682L;

    public static final QImportPartnerDetail importPartnerDetail = new QImportPartnerDetail("importPartnerDetail");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final NumberPath<Long> importPartnerDetailId = createNumber("importPartnerDetailId", Long.class);

    public final NumberPath<Long> importPartnerRequestId = createNumber("importPartnerRequestId", Long.class);

    public final NumberPath<Long> prodOfferId = createNumber("prodOfferId", Long.class);

    public final NumberPath<Long> quantity = createNumber("quantity", Long.class);

    public final NumberPath<Long> stateId = createNumber("stateId", Long.class);

    public QImportPartnerDetail(String variable) {
        super(ImportPartnerDetail.class, forVariable(variable));
    }

    public QImportPartnerDetail(Path<? extends ImportPartnerDetail> path) {
        super(path.getType(), path.getMetadata());
    }

    public QImportPartnerDetail(PathMetadata<?> metadata) {
        super(ImportPartnerDetail.class, metadata);
    }

}

