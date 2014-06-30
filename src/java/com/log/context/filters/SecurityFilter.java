

package com.log.context.filters;

import com.log.context.Security;
import com.log.context.Security.SecurityStatus;
import com.log.urlaccess.URLAccess;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class SecurityFilter implements Filter {

    private static final Logger log = Logger.getLogger(SecurityFilter.class.getName());
    
    private Security security;
    
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        security = Security.getInstance(filterConfig.getServletContext());
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        
        if(!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse))
        {
            return;
        }
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpServletResponse httpResponse = (HttpServletResponse)response;
        
        if((URLAccess.ACCESS_TYPE)httpRequest.getAttribute("accessType") == URLAccess.ACCESS_TYPE.NO_FILTER)
        {
            chain.doFilter(request, response);
            return;
        }
        
        SecurityStatus securityStatus = security.getSecurityStatus();
        if(securityStatus == SecurityStatus.ERROR_INVALID_FILE ||  securityStatus == SecurityStatus.UNKNOWN_ERROR)
        {
            RequestDispatcher requestDispatcher = httpRequest.getRequestDispatcher("/contextError.jsp");    
            httpRequest.setAttribute("errorText", security.getSecurityStatusMessage());   
            requestDispatcher.forward(httpRequest,httpResponse);
        }
        else if(securityStatus == SecurityStatus.ERROR_NO_FILE)
        {
            RequestDispatcher requestDispatcher = httpRequest.getRequestDispatcher("/createUser/");    
            requestDispatcher.forward(httpRequest,httpResponse);
        }
        else {
            HttpSession session = httpRequest.getSession(true);
            String ip = (String)session.getAttribute("ip");
            if(ip == null) {
                RequestDispatcher requestDispatcher = httpRequest.getRequestDispatcher("/login/");    
                requestDispatcher.forward(httpRequest,httpResponse);
                return;
            } else if(!ip.equals(httpRequest.getRemoteAddr())) {
                session.invalidate();
                RequestDispatcher requestDispatcher = httpRequest.getRequestDispatcher("/login/");    
                requestDispatcher.forward(httpRequest,httpResponse);
                return;
            }
            
            String login = (String)session.getAttribute("login");
            String password = (String)session.getAttribute("password");
            Security.UserStatus userStatus;
            
            try {
             userStatus = security.checkAuth(login, password, ip);
            }
            catch(Exception ex) {
                log.log(Level.SEVERE, null, ex.getMessage());
                session.invalidate();
                RequestDispatcher requestDispatcher = httpRequest.getRequestDispatcher("/login/");    
                requestDispatcher.forward(httpRequest,httpResponse);
                return;
            }
            
            if(userStatus != Security.UserStatus.APPROWED) {
                session.invalidate();
                RequestDispatcher requestDispatcher = httpRequest.getRequestDispatcher("/login/");    
                requestDispatcher.forward(httpRequest,httpResponse);
            }
            else {
                chain.doFilter(request, response);
            }
        }      
    }

    @Override
    public void destroy() {
        
    }

    
}
