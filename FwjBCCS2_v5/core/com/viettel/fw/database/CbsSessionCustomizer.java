package com.viettel.fw.database;

import org.eclipse.persistence.sessions.DatabaseLogin;
import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.sessions.factories.SessionCustomizer;

/**
 * Created by lamnv5 on 5/17/2016.
 */
public class CbsSessionCustomizer implements SessionCustomizer {
    @Override
    public void customize(Session session) throws Exception {
        DatabaseLogin login = (DatabaseLogin)session.getDatasourceLogin();
        login.setQueryRetryAttemptCount(0);
    }
}