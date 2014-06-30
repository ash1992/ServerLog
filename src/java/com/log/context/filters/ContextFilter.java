

package com.log.context.filters;

import com.log.context.ContextStatus;
import com.log.urlaccess.URLAccess;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ContextFilter implements Filter
{

    private FilterConfig filterConfig;
    private ServletContext servletContext;
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {
        this.filterConfig = filterConfig;
        servletContext = filterConfig.getServletContext();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        if(!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse))
        {
            chain.doFilter(request, response);
            return;
        }
        
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpServletResponse httpResponse = (HttpServletResponse)response;
        
        
            

        URLAccess urlAccess = URLAccess.getInstance();
        URLAccess.ACCESS_TYPE accessType = urlAccess.getStatus(httpRequest);
        httpRequest.setAttribute("accessType", accessType);
        
        if(accessType == URLAccess.ACCESS_TYPE.FORBIDDEN)
        {
            httpResponse.sendError(404);
            return;
        }
        else if(accessType == URLAccess.ACCESS_TYPE.NO_FILTER)
        {
            chain.doFilter(httpRequest,httpResponse);
            return;
        }
        
        if(ContextStatus.getStatus() != ContextStatus.NORMAL)
        {
            RequestDispatcher requestDispatcher = httpRequest.getRequestDispatcher("/contextError.jsp"); 
            httpRequest.setAttribute("errorCode", ContextStatus.getStatus());    
            httpRequest.setAttribute("errorText", ContextStatus.getStatusText());   
            requestDispatcher.forward(httpRequest,httpResponse);
        }
        else
        {
            chain.doFilter(httpRequest, httpResponse);
        }
    }

    @Override
    public void destroy()
    {
        
    }
    
}
