

package com.log.models;

import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;


public class Globals {
    
    private final HttpServletRequest request;
    private final Pattern loginPattern = Pattern.compile("^[a-zA-Z0-9_-]{2,120}+$");
    private final Pattern passwordPattern = Pattern.compile("^[a-zA-Z0-9_-]{4,120}+$");
    
    public Globals(HttpServletRequest request) {
        this.request = request;
    }
    
    public String getLogin(String name) throws IllegalArgumentException {
        String login = getParameter(name);
        if(!loginPattern.matcher(login).matches()) {
            throw new IllegalArgumentException("Parameter '" + name + "' is invalid!");
        }
        
        return login;
    }
    public String getLogin() throws IllegalArgumentException {
        return getLogin("login");
    }
    
    
    public String getPassword() throws IllegalArgumentException {
        return getPassword("password");
    }
    
    public String getPassword(String name) throws IllegalArgumentException {
        String password = getParameter(name);
        if(!passwordPattern.matcher(password).matches()) {
            throw new IllegalArgumentException("Parameter '" + name + "' is invalid!");
        }
        
        return password;
    }
    
    
    
    private String getParameter(String name) throws IllegalArgumentException {
        String value = request.getParameter(name);
        if(value == null) {
            throw new NullPointerException("Parameter '" + name + "' is null!");
        }
        
        return value;
    }
    
}
