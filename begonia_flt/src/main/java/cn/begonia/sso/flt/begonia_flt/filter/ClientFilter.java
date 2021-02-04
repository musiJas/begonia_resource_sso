package cn.begonia.sso.flt.begonia_flt.filter;

import cn.begonia.sso.flt.begonia_flt.handler.SessionMappingStorage;
import cn.begonia.sso.flt.begonia_flt.listener.LogoutListener;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Data
public abstract class ClientFilter implements Filter {

    private  String  clientId;
    private  String  serviceUrl;
    private  String  clientSecret;

    private SessionMappingStorage sessionMappingStorage;

    public abstract boolean isAccessAllowed(HttpServletRequest request, HttpServletResponse response)
            throws IOException;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

    }

    @Override
    public void destroy() {

    }

    protected SessionMappingStorage getSessionMappingStorage() {
        if (sessionMappingStorage == null) {
            sessionMappingStorage = LogoutListener.getSessionMappingStorage();
        }
        return sessionMappingStorage;
    }
}
