package cn.begonia.sso.ssp.begonia_ssp.controller;

import cn.begonia.sso.common.begonia_cmn.util.CookieUtils;
import cn.begonia.sso.flt.begonia_flt.common.Oauth2Constant;
import cn.begonia.sso.flt.begonia_flt.common.SsoConstants;
import cn.begonia.sso.flt.begonia_flt.util.URLUtils;
import cn.begonia.sso.ssp.begonia_ssp.common.CodeManager;
import cn.begonia.sso.ssp.begonia_ssp.common.TicketGrantingTicketManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Controller
public class PageController {

    @Autowired
    private CodeManager codeManager;

    @Autowired
    private TicketGrantingTicketManager ticketGrantingTicketManager;


    @RequestMapping("/")
    public ModelAndView  index( ){
        ModelAndView  model=new ModelAndView();
        model.setViewName("index");
        return  model;
    }

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public ModelAndView login(
            @RequestParam(value = SsoConstants.REDIRECT_URI, required = true) String redirectUri,
            @RequestParam(value = Oauth2Constant.CLIENT_ID, required = true) String clientId,
            HttpServletRequest request) throws UnsupportedEncodingException {
        String tgt = CookieUtils.getCookie(request, SsoConstants.TGC);
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.addObject("redirectUri",redirectUri);
        modelAndView.addObject("clientId",clientId);
        if (StringUtils.isEmpty(tgt) || ticketGrantingTicketManager.get(tgt) == null) {
            modelAndView.setViewName(URLUtils.goLoginPath(redirectUri, clientId, request));
            return  modelAndView;
        }
        modelAndView.setViewName(generateCodeAndRedirect(redirectUri, tgt));
        return  modelAndView;
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
        return "redirect:" + URLUtils.authRedirectUri(redirectUri, code);
    }
}
