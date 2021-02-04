package cn.begonia.sso.ssp.begonia_ssp.controller;

import cn.begonia.sso.common.begonia_cmn.util.CookieUtils;
import cn.begonia.sso.flt.begonia_flt.common.SsoConstants;
import cn.begonia.sso.ssp.begonia_ssp.common.AccessTokenManager;
import cn.begonia.sso.ssp.begonia_ssp.common.TicketGrantingTicketManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LogoutController {
    @Autowired
    private AccessTokenManager accessTokenManager;
    @Autowired
    private TicketGrantingTicketManager  ticketGrantingTicketManager;

    /**
     *  注销session token
     * @return
     */
    @RequestMapping(value = "/logout")
    public String logout(
            HttpServletRequest  request, HttpServletResponse response) {
         String tgc = CookieUtils.getCookie(request, SsoConstants.TGC);
         String  redirectUrl=request.getParameter("redirectUri");
        if (!StringUtils.isEmpty(tgc)) {
            // 删除登录凭证
            ticketGrantingTicketManager.remove(tgc);
            // 删除凭证Cookie
            CookieUtils.removeCookie(SsoConstants.TGC, "/", response);
            // 删除所有tgt对应的调用凭证，并通知客户端登出注销本地session
            accessTokenManager.remove(tgc);
            return "redirect:"+redirectUrl;
        }
        return "redirect:"+redirectUrl;
    }


}
