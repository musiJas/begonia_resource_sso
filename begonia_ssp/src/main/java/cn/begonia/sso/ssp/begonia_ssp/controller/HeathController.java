package cn.begonia.sso.ssp.begonia_ssp.controller;

import cn.begonia.sso.common.begonia_cmn.common.Result;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class HeathController {

    @RequestMapping("/isOk")
    public Result isOk(HttpServletRequest request, HttpServletResponse response){
        System.out.println(request.getRemoteAddr());
        System.out.println(request.getServerName());
        System.out.println(request.getServletPath());
        System.out.println(request.getRequestURL());
        System.out.println(request.getScheme());
        return  Result.isOk();
    }

}
