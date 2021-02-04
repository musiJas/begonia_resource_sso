package cn.begonia.sso.flt.begonia_flt.listener;

import cn.begonia.sso.flt.begonia_flt.handler.SessionMappingStorage;
import cn.begonia.sso.flt.begonia_flt.service.LocalSessionMappingStorage;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class LogoutListener implements HttpSessionListener {

    private static SessionMappingStorage sessionMappingStorage = new LocalSessionMappingStorage();

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        final HttpSession session = httpSessionEvent.getSession();
        sessionMappingStorage.removeBySessionById(session.getId());
    }

    public static SessionMappingStorage getSessionMappingStorage() {
        return sessionMappingStorage;
    }

}
