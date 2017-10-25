package com.viettel.web.common;


import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;


public class CustomExceptionHandlerFactory extends ExceptionHandlerFactory {

    private ExceptionHandlerFactory exceptionHandlerFactory;

    public CustomExceptionHandlerFactory(ExceptionHandlerFactory exceptionHandlerFactory) {
        this.exceptionHandlerFactory = exceptionHandlerFactory;
    }

    @Override
    public ExceptionHandler getExceptionHandler() {
        ExceptionHandler result = exceptionHandlerFactory.getExceptionHandler();
        result = new CustomExceptionHandlerWrapper(result);
        return result;
    }

}
