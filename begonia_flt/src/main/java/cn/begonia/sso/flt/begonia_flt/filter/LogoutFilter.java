package cn.begonia.sso.flt.begonia_flt.filter;

import cn.begonia.sso.flt.begonia_flt.common.SsoConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LogoutFilter extends   ClientFilter {

    @Override
    public boolean isAccessAllowed(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String  token =getLogoutParam(request);
        if(token!=null){
            destroySession(token);
            return  false;
        }
        return true;
    }

    public  String  getLogoutParam(HttpServletRequest  request){
        return request.getHeader(SsoConstants.LOGOUT_PARAMETER_NAME);
    }

    private void destroySession(String accessToken) {
        final HttpSession session = getSessionMappingStorage().removeSessionByMappingId(accessToken);
        if (session != null) {
            session.invalidate();
        }
    }
}
