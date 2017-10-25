package com.viettel.bccs.inventory.common;


import java.io.Serializable;

/**
 * @author nhannt34
 * Custom generator cho Hibernate dua tren SequenceGenerator
 * Mac dinh SequenceGenerator se tu lay id tu sequence tren database de lam id bat ke object model da set san id
 * hay chua, generator nay se check id co san chua, neu chua thi moi lay sequence thi db
 */
//public class UseExistingOrSequenceGenerator extends SequenceStyleGenerator {
//    @Override
//    public Serializable generate(SessionImplementor session, Object object)
//            throws HibernateException {
//        Serializable id = session.getEntityPersister(null, object)
//                .getClassMetadata().getIdentifier(object, session);
//        return id != null ? id : super.generate(session, object);
//    }
//}
