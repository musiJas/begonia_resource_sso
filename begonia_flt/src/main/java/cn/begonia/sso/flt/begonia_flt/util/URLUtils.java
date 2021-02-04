package cn.begonia.sso.flt.begonia_flt.util;

import cn.begonia.sso.common.begonia_cmn.common.Result;
import cn.begonia.sso.common.begonia_cmn.util.CookieUtils;
import cn.begonia.sso.flt.begonia_flt.common.Oauth2Constant;
import cn.begonia.sso.flt.begonia_flt.common.SsoConstants;
import com.alibaba.fastjson.JSON;

import javax.net.ssl.SNIHostName;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class URLUtils {

    /**
     * 跳转至服务端登录
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public static  void redirectLogin(HttpServletRequest request, HttpServletResponse response,String   serviceUrl,String clientId) throws IOException {
        if (isAjaxRequest(request)) {
            responseJson(response, Result.isFail("未登录或已超时"));
        } else {
            String loginUrl = new StringBuilder().append(serviceUrl).append(SsoConstants.LOGIN_URL).append("?")
                    .append(Oauth2Constant.CLIENT_ID).append("=").append(clientId).append("&")
                    .append(SsoConstants.REDIRECT_URI).append("=")
                    .append(URLEncoder.encode(getCurrentUrl(request), "utf-8")).toString();
            response.sendRedirect(loginUrl);
        }
    }

    /**
     * 注销session
     */
    public static  void  sspLogout(HttpServletRequest request, String  serviceUrl) throws IOException {
        String tgt = CookieUtils.getCookie(request, SsoConstants.TGC);
        Map<String,String>  paramMap=new HashMap<>(16);
        paramMap.put("tgc",tgt);
        HttpUtils.post(serviceUrl+SsoConstants.LOGOUT_URL,paramMap);
    }


    /**
     * 跳转至服务端登录后跳转到首页
     * @param request
     * @param response
     * @throws IOException
     */
    public static  void redirectLogout(HttpServletRequest request, HttpServletResponse response,String   serviceUrl,String clientId) throws IOException {
        if (isAjaxRequest(request)) {
            responseJson(response, Result.isFail("未登录或已超时"));
        } else {
            /** 访问服务端 注销session */
            String loginUrl = new StringBuilder().append(serviceUrl).append(SsoConstants.LOGIN_URL).append("?")
                    .append(Oauth2Constant.CLIENT_ID).append("=").append(clientId).append("&")
                    .append(SsoConstants.REDIRECT_URI).append("=")
                    .append(URLEncoder.encode(getRedirectIndexUrl(request), "utf-8")).toString();
            response.sendRedirect(loginUrl);
        }
    }


    protected static boolean isAjaxRequest(HttpServletRequest request) {
        String requestedWith = request.getHeader("X-Requested-With");
        return requestedWith != null ? "XMLHttpRequest".equals(requestedWith) : false;
    }


    protected static void responseJson(HttpServletResponse response,Result result) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(200);
        PrintWriter writer = response.getWriter();
        writer.write(JSON.toJSONString(result));
        writer.flush();
        writer.close();
    }

    /**
     * 获取当前请求地址
     *
     * @param request
     * @return
     */
    public  static String getCurrentUrl(HttpServletRequest request) {
        StringBuffer sb=new StringBuffer();
        String uri= request.getRequestURI();
        sb.append(request.getScheme()).append("://");
        sb.append(request.getServerName());
        if (request.getServerPort() != 80 && request.getServerPort() != 443) {
            sb.append(":").append(request.getServerPort());
        }
        sb.append(uri);
        sb.append(request.getQueryString() == null ? "" : "?" + request.getQueryString());
        return sb.toString();
    }

    public  static String getRedirectIndexUrl(HttpServletRequest request) {
        StringBuffer sb=new StringBuffer();
        String appName= request.getContextPath();
        sb.append(request.getScheme()).append("://");
        sb.append(request.getServerName()).append(":").append(request.getServerPort()).append(appName);
        return sb.toString();
    }


    /**
     * 设置request的redirectUri和appId参数，跳转到登录页
     *
     * @param redirectUri
     * @param request
     * @return
     */
    public  static String goLoginPath(String redirectUri, String appId, HttpServletRequest request) {
        request.setAttribute(SsoConstants.REDIRECT_URI, redirectUri);
        request.setAttribute(Oauth2Constant.CLIENT_ID, appId);
        return SsoConstants.LOGIN_URL;
    }


    /**
     * 将授权码拼接到回调redirectUri中
     *
     * @param redirectUri
     * @param code
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String authRedirectUri(String redirectUri, String code) throws UnsupportedEncodingException {
        StringBuilder sbf = new StringBuilder(redirectUri);
        if (redirectUri.indexOf("?") > -1) {
            sbf.append("&");
        }
        else {
            sbf.append("?");
        }
        sbf.append(Oauth2Constant.AUTH_CODE).append("=").append(code);
        return URLDecoder.decode(sbf.toString(), "utf-8");
    }

}
