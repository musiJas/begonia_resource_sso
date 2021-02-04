package cn.begonia.sso.client.begonia_client.config;

import cn.begonia.sso.flt.begonia_flt.filter.LoginFilter;
import cn.begonia.sso.flt.begonia_flt.filter.LogoutFilter;
import cn.begonia.sso.flt.begonia_flt.listener.LogoutListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpSessionListener;
@SuppressWarnings("all")
@Configuration
public class SsoConfig {

    @Value("${begonia.sso.url}")
    private  String  serviceUrl;

    @Value("${begonia.client.id}")
    private  String  clientId;

    @Value("${begonia.sso.secret}")
    private  String  secret;

    /**
     * 单实例方式单点登出Listener
     *
     * @return
     */
    @Bean
    public ServletListenerRegistrationBean<HttpSessionListener> LogoutListener() {
        ServletListenerRegistrationBean<HttpSessionListener> listenerRegBean = new ServletListenerRegistrationBean<>();
        LogoutListener logoutListener = new LogoutListener();
        listenerRegBean.setListener(logoutListener);
        return listenerRegBean;
    }


    /**
     * Web支持单点登录Filter容器
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean  smartContainer() {
        SsoFilter filter = new SsoFilter();
        filter.initial(clientId,secret,serviceUrl);

        // 忽略拦截URL,多个逗号分隔
//		smartContainer.setExcludeUrls("/app/*");

        filter.setFilters(new LogoutFilter(), new LoginFilter());

        FilterRegistrationBean  registration = new FilterRegistrationBean ();
        registration.setFilter(filter);
        registration.addUrlPatterns("/*");
        registration.setOrder(1);
        registration.setName("SsoFilter");
        return registration;
    }

}
