package com.viettel.web.common;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.security.access.AccessDeniedException;

import javax.faces.FacesException;
import javax.faces.application.NavigationHandler;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by LamNV5 on 4/16/2015.
 */
public class CustomExceptionHandlerWrapper extends ExceptionHandlerWrapper {
    private static final Logger log = Logger.getLogger(CustomExceptionHandlerWrapper.class.getCanonicalName());
    private ExceptionHandler wrapped;

    public CustomExceptionHandlerWrapper(ExceptionHandler exception) {
        this.wrapped = exception;
    }

    @Override
    public ExceptionHandler getWrapped() {
        return wrapped;
    }

    @Override
    public void handle() throws FacesException {

        final Iterator<ExceptionQueuedEvent> i = getUnhandledExceptionQueuedEvents().iterator();
        while (i.hasNext()) {
            ExceptionQueuedEvent event = i.next();
            ExceptionQueuedEventContext context =
                    (ExceptionQueuedEventContext) event.getSource();

            // get the exception from context
            Throwable t = context.getException();

            final FacesContext fc = FacesContext.getCurrentInstance();
            final Map<String, Object> requestMap = fc.getExternalContext().getRequestMap();
            final NavigationHandler nav = fc.getApplication().getNavigationHandler();

            //here you do what ever you want with exception
            try {

                //log error ?
                log.fatal("Critical Exception!", t);

                //redirect error page
                Throwable root = ExceptionUtils.getRootCause(t);
                if (root instanceof AccessDeniedException) {
                    requestMap.put("exceptionMessage", t.getMessage());
                    nav.handleNavigation(fc, null, "/securityError?faces-redirect=true");
                    fc.renderResponse();
                }

            } catch (Throwable ex) {
                ex.printStackTrace();
            } finally {
                //remove it from queue
                i.remove();
            }
        }

        //parent hanle
        getWrapped().handle();
    }
}