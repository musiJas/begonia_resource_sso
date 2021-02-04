package cn.begonia.sso.client.begonia_client;

import cn.begonia.sso.client.begonia_client.config.ServerStartUp;
import cn.begonia.sso.common.begonia_cmn.entity.SsoUser;
import cn.begonia.sso.common.begonia_cmn.util.CookieUtils;
import cn.begonia.sso.flt.begonia_flt.common.Oauth2Constant;
import cn.begonia.sso.flt.begonia_flt.common.SsoConstants;
import cn.begonia.sso.flt.begonia_flt.util.SessionUtils;
import cn.begonia.sso.flt.begonia_flt.util.URLUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

@SuppressWarnings("all")
@Controller
public class PageController {


    @RequestMapping("/")
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView  modelAndView=new ModelAndView();
        try {
            /**清除会话**/
            String clientId = ServerStartUp.getApplicationContext().getEnvironment().getProperty("begonia.client.id");
            String serviceUrl = ServerStartUp.getApplicationContext().getEnvironment().getProperty("begonia.sso.url");
            StringBuffer  sb=new StringBuffer();
            sb.append(serviceUrl).append(SsoConstants.LOGOUT_URL);
            sb.append("?").append(SsoConstants.REDIRECT_URI).append("=");
            sb.append(URLEncoder.encode(URLUtils.getCurrentUrl(request), "utf-8"));
            SsoUser user = SessionUtils.getUser(request);
            modelAndView.addObject("logout",sb.toString());
            modelAndView.addObject("userNo",user.getUserName());
            modelAndView.setViewName("index");
        } catch (IOException e) {
            e.printStackTrace();
        }
    return modelAndView;
    }

}
