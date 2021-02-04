package cn.begonia.sso.flt.begonia_flt.util;

import cn.begonia.sso.common.begonia_cmn.entity.SsoUser;
import cn.begonia.sso.flt.begonia_flt.common.SessionAccessToken;
import cn.begonia.sso.flt.begonia_flt.common.SsoConstants;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class SessionUtils {
    public  static SessionAccessToken  getAccessToken(HttpServletRequest request){
        return (SessionAccessToken) request.getSession().getAttribute(SsoConstants.SESSION_ACCESS_TOKEN);
    }
    public  static SsoUser  getUser(HttpServletRequest request){
        return Optional.ofNullable(getAccessToken(request)).map(u->u.getUser()).orElse(null);
    }
    /*public static  Long  getUserId(HttpServletRequest request){
        return  Optional.ofNullable(getUser(request)).map(u->u.getId()).orElse(null);
    }*/
    public  static    void   setAccessToken(HttpServletRequest  request,SessionAccessToken token){
         request.getSession().setAttribute(SsoConstants.SESSION_ACCESS_TOKEN, token);
    }
    public  static  void  invalidate(HttpServletRequest request){
        setAccessToken(request, null);
        request.getSession().invalidate();
    }

}
