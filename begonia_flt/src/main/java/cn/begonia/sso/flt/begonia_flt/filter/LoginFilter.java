package cn.begonia.sso.flt.begonia_flt.filter;

import cn.begonia.sso.common.begonia_cmn.common.Result;
import cn.begonia.sso.flt.begonia_flt.common.Oauth2Constant;
import cn.begonia.sso.flt.begonia_flt.common.SessionAccessToken;
import cn.begonia.sso.flt.begonia_flt.common.SsoConstants;
import cn.begonia.sso.flt.begonia_flt.util.Oauth2Utils;
import cn.begonia.sso.flt.begonia_flt.util.SessionUtils;
import cn.begonia.sso.flt.begonia_flt.util.URLUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter extends  ClientFilter{
    private Logger  logger= LoggerFactory.getLogger(LoginFilter.class);

    @Override
    public boolean isAccessAllowed(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SessionAccessToken  token= SessionUtils.getAccessToken(request);
        if(token!=null && (!token.isExpired() || refreshToken(token.getRefreshToken(),request))){
            return true;
        }
        String  code=request.getParameter(Oauth2Constant.AUTH_CODE);
        /** 表示支持ajax访问**/
        if(code!=null){
            getAccessToken(code,request);
            redirectLocalRemoveCode(request,response);
        }else {
            /**表示失败跳到登录页上去*/
            URLUtils.redirectLogin(request,response,getServiceUrl(),getClientId());
        }
        return false;
    }









    /**
     * 去除返回地址中的票据参数
     *
     * @param request
     * @return
     * @throws IOException
     */
    private void redirectLocalRemoveCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String currentUrl = URLUtils.getCurrentUrl(request);
        currentUrl = currentUrl.substring(0, currentUrl.indexOf(Oauth2Constant.AUTH_CODE) - 1);
        response.sendRedirect(currentUrl);
    }


    private  void   getAccessToken(String  code ,HttpServletRequest  request){
        Result  result= Oauth2Utils.getAccessToken(getServiceUrl(),getClientId(),getClientSecret(),code);
        if(result.getCode()!=200){
            logger.error("refresh token is  error...");
            return ;
        }
        String obj=  String.valueOf(result.getObj());
        setAccessTokenInSession( JSONObject.parseObject(obj,SessionAccessToken.class),request);
    }



    public   boolean   refreshToken(String  refreshToken,HttpServletRequest  request){
        Result result=Oauth2Utils.refreshToken(getServiceUrl(),getClientId(),refreshToken);
        if(result.getCode()!=200){
            logger.error("refresh token is  error...");
            return false;
        }
        String obj=  String.valueOf(result.getObj());
        return setAccessTokenInSession(JSONObject.parseObject(obj,SessionAccessToken.class),request);
    }

    public   boolean  setAccessTokenInSession(SessionAccessToken token,HttpServletRequest  request){
        if (token == null) {
            return false;
        }
        // 记录accessToken到本地session
        SessionUtils.setAccessToken(request, token);
        // 记录本地session和accessToken映射
        recordSession(request, token.getAccessToken());
        return  true;
    }

    private void recordSession(final HttpServletRequest request, String accessToken) {
        final HttpSession session = request.getSession();
        getSessionMappingStorage().removeBySessionById(session.getId());
        getSessionMappingStorage().addSessionById(accessToken, session);
    }


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        super.doFilter(servletRequest, servletResponse, filterChain);
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
