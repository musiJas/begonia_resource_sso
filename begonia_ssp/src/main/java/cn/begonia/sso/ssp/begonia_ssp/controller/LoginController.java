package cn.begonia.sso.ssp.begonia_ssp.controller;

import cn.begonia.sso.common.begonia_cmn.common.Result;
import cn.begonia.sso.common.begonia_cmn.entity.SsoUser;
import cn.begonia.sso.common.begonia_cmn.util.CookieUtils;
import cn.begonia.sso.flt.begonia_flt.common.Oauth2Constant;
import cn.begonia.sso.flt.begonia_flt.common.SsoConstants;
import cn.begonia.sso.ssp.begonia_ssp.common.CodeManager;
import cn.begonia.sso.ssp.begonia_ssp.common.TicketGrantingTicketManager;
import cn.begonia.sso.ssp.begonia_ssp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@Controller
public class LoginController {

    @Autowired
    private UserService  userService;

    @Autowired
    private CodeManager  codeManager;

    @Autowired
    private TicketGrantingTicketManager  ticketGrantingTicketManager;

    @RequestMapping("/toLogin")
    public void   toLogin(
            @RequestParam(value = SsoConstants.REDIRECT_URI, required = true) String redirectUri,
            @RequestParam(value = Oauth2Constant.CLIENT_ID, required = true) String clientId,
            @RequestParam(required = true) String userNo,
            @RequestParam(required = true) String password,
            HttpServletRequest request, HttpServletResponse response
    ) throws IOException {
        /**校验账户 **/
      /*  if(!appService.exists(appId)) {
            request.setAttribute("errorMessage", "非法应用");
            return goLoginPath(redirectUri, appId, request);
        }*/

        Result result = userService.login(userNo, password);
        if (result.getCode()!=200) {
            request.setAttribute("errorMessage", result.getMsg());
            response.sendRedirect(goLoginPath(redirectUri, clientId, request));
        }

        String tgt = CookieUtils.getCookie(request, SsoConstants.TGC);
        if (StringUtils.isEmpty(tgt) || ticketGrantingTicketManager.get(tgt) == null) {
            tgt = ticketGrantingTicketManager.generate((SsoUser) result.getObj());
            // TGT存cookie，和Cas登录保存cookie中名称一致为：TGC
            CookieUtils.addCookie(SsoConstants.TGC, tgt, "/", request, response);
        }

        response.sendRedirect(generateCodeAndRedirect(redirectUri,tgt));
    }
    /**
     * 生成授权码，跳转到redirectUri
     *
     * @param redirectUri
     * @param tgt
     * @return
     * @throws UnsupportedEncodingException
     */
    private String generateCodeAndRedirect(String redirectUri, String tgt) throws UnsupportedEncodingException {
        // 生成授权码
        String code = codeManager.generate(tgt, true, redirectUri);
        return  authRedirectUri(redirectUri, code);
    }



    /**
     * 将授权码拼接到回调redirectUri中
     *
     * @param redirectUri
     * @param code
     * @return
     * @throws UnsupportedEncodingException
     */
    private String authRedirectUri(String redirectUri, String code) throws UnsupportedEncodingException {
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

    /**
     * 设置request的redirectUri和appId参数，跳转到登录页
     *
     * @param redirectUri
     * @param request
     * @return
     */
    private String goLoginPath(String redirectUri, String appId, HttpServletRequest request) {
        request.setAttribute(SsoConstants.REDIRECT_URI, redirectUri);
        request.setAttribute(Oauth2Constant.CLIENT_ID, appId);
        return "redirect:"+SsoConstants.LOGIN_URL;
    }

}
