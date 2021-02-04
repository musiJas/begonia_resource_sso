package cn.begonia.sso.flt.begonia_flt.handler;

import javax.servlet.http.HttpSession;

public interface SessionMappingStorage {
    HttpSession removeSessionByMappingId(String accessToken);

    void removeBySessionById(String sessionId);

    void addSessionById(String accessToken, HttpSession session);


}
