package cn.begonia.sso.flt.begonia_flt.common;

public class SsoConstants {
    /**
     * 服务端登录地址
     */
    public static final String LOGIN_URL = "/login";

    /**
     * 服务端登出地址
     */
    public static final String LOGOUT_URL = "/logout";

    /**
     * 服务端回调客户端地址参数名称
     */
    public static final String REDIRECT_URI = "redirectUri";

    public  static  final  String  SESSION_ACCESS_TOKEN="_session_access_token";
    /**
     * 服务端单点登出回调客户端登出参数名称
     */
    public static final String LOGOUT_PARAMETER_NAME = "logoutRequest";

    public static final String TGC = "TGC";
    /**
     * 模糊匹配后缀
     */
    public static final String URL_FUZZY_MATCH = "/*";

}
