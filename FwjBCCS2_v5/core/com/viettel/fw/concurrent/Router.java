package com.viettel.fw.concurrent;

/**
 * Created by lamnv5 on 6/21/2016.
 */
public interface Router {
    Queue route(Routable message) throws Exception;
}
