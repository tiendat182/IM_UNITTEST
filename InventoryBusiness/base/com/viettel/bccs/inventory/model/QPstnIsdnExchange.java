package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QPstnIsdnExchange is a Querydsl query type for PstnIsdnExchange
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QPstnIsdnExchange extends EntityPathBase<PstnIsdnExchange> {

    private static final long serialVersionUID = 520261414L;

    public static final QPstnIsdnExchange pstnIsdnExchange = new QPstnIsdnExchange("pstnIsdnExchange");

    public final DateTimePath<java.util.Date> createdate = createDateTime("createdate", java.util.Date.class);

    public final NumberPath<Long> exchangeId = createNumber("exchangeId", Long.class);

    public final StringPath poCode = createString("poCode");

    public final StringPath prefix = createString("prefix");

    public final NumberPath<Long> pstnIsdnExchangeId = createNumber("pstnIsdnExchangeId", Long.class);

    public final StringPath type = createString("type");

    public QPstnIsdnExchange(String variable) {
        super(PstnIsdnExchange.class, forVariable(variable));
    }

    public QPstnIsdnExchange(Path<? extends PstnIsdnExchange> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPstnIsdnExchange(PathMetadata<?> metadata) {
        super(PstnIsdnExchange.class, metadata);
    }

}

