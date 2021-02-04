package cn.begonia.sso.client.begonia_client.config;

import cn.begonia.sso.flt.begonia_flt.common.SsoConstants;
import cn.begonia.sso.flt.begonia_flt.filter.ClientFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@SuppressWarnings("all")
public class SsoFilter   implements Filter {
    private  String  clientId;
    private  String  clientSecret;
    private  String  serverUrl;

    public  SsoFilter(){

    }

    public  void  initial(String clientId, String clientSecret, String serverUrl){
        this.clientId=clientId;
        this.clientSecret=clientSecret;
        this.serverUrl=serverUrl;
    }


    /** 排除URL */
    protected String excludeUrls;

    private ClientFilter[] filters;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        if (filters == null || filters.length == 0) {
            throw new IllegalArgumentException("filters不能为空");
        }
        for (ClientFilter filter : filters) {
            filter.setClientId(clientId);
            filter.setClientSecret(clientSecret);
            filter.setServiceUrl(serverUrl);
            filter.init(filterConfig);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        if (isExcludeUrl(httpRequest.getServletPath())) {
            chain.doFilter(request, response);
            return;
        }

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        for (ClientFilter filter : filters) {
            if (!filter.isAccessAllowed(httpRequest, httpResponse)) {
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private boolean isExcludeUrl(String url) {
        if (excludeUrls == null || excludeUrls.isEmpty())
            return false;

        Map<Boolean, List<String>> map = Arrays.stream(excludeUrls.split(","))
                .collect(Collectors.partitioningBy(u -> u.endsWith(SsoConstants.URL_FUZZY_MATCH)));
        List<String> urlList = map.get(false);
        if (urlList.contains(url)) { // 优先精确匹配
            return true;
        }
        urlList = map.get(true);
        for (String matchUrl : urlList) { // 再进行模糊匹配
            if (url.startsWith(matchUrl.replace(SsoConstants.URL_FUZZY_MATCH, ""))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void destroy() {
        if (filters == null || filters.length == 0)
            return;
        for (ClientFilter filter : filters) {
            filter.destroy();
        }
    }

    public void setExcludeUrls(String excludeUrls) {
        this.excludeUrls = excludeUrls;
    }

    public void setFilters(ClientFilter... filters) {
        this.filters = filters;
    }
}