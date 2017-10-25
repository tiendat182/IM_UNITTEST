package com.viettel.ws.security;

/**
 * Created by vtsoft on 4/15/2015.
 */

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLResolver;
import javax.xml.stream.XMLStreamException;

/**
 * thiendn1: prevent XXE attack
 */
public class ParserFactory {
    public static XMLInputFactory createFactory() {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, Boolean.TRUE);
        factory.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
        factory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES,
                Boolean.FALSE);
        factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES,
                Boolean.FALSE);
        factory.setXMLResolver(new XMLResolver() {
            public Object resolveEntity(String publicID, String systemID,
                                        String baseURI, String namespace)
                    throws XMLStreamException {
                throw new XMLStreamException("Reading external entities is disabled");
            }
        });
        return factory;
    }
}

