package com.log.servlets;

import com.log.context.Security;
import com.log.context.users.User;
import com.log.models.Globals;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class CreateUserServlet extends HttpServlet {
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/createUser.jsp");
        requestDispatcher.forward(request, response);
    }
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        Globals globals = new Globals(request);
        
        String login;
        String password;
        
        request.setAttribute("login", request.getParameter("login"));
        request.setAttribute("password",request.getParameter("pasword"));
        
        try {
            login = globals.getLogin();
            password = globals.getPassword();
        }
        catch(IllegalArgumentException ex) {
            showForm(request, response, ex.getMessage());
            return;
        }
        
        
        Security security = Security.getInstnace();

        User user;
        try {
            user = new User(login, security.getMD5Hash(password), User.Level.ADMIN, null);
            user.setIp(request.getRemoteAddr());
        }
        catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            showForm(request, response, ex.getMessage());
            return;
        }
        
        try
        {
            security.reInit(user);
        }
        catch (Exception ex)
        {
            showForm(request, response, ex.getMessage());
            return;
        }
        
        HttpSession httpSession = request.getSession(true);
        httpSession.setAttribute("login", login);
        httpSession.setAttribute("password", password);
        httpSession.setAttribute("ip", request.getRemoteAddr());
        response.sendRedirect(request.getContextPath() + "/");
    }
    
    public void showForm(HttpServletRequest request, HttpServletResponse response, String error) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/createUser.jsp");
        request.setAttribute("error", error);
        requestDispatcher.forward(request, response);
    }
}
