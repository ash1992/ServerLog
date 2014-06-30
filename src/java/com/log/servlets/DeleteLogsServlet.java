package com.log.servlets;

import com.log.models.Log;
import com.log.models.Props;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author admin
 */
public class DeleteLogsServlet extends HttpServlet
{
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {

        int total = 0;
        int deleted = 0;
        java.io.File logsPath = (java.io.File)Props.get("logsPath");
        
        Map<String, String[]> parameters = request.getParameterMap();
        Iterator<String> parametersKeys = parameters.keySet().iterator();
        while(parametersKeys.hasNext()) {
            String parameterGroupName = parametersKeys.next();
            if(!parameterGroupName.startsWith("file_")) continue;
            String[] logGroup = parameters.get(parameterGroupName);
            
            for(String logFileName : logGroup) {
                total++;
                try {
                if(new Log(logsPath, logFileName).delete()) deleted++; 
                } catch(UnsupportedOperationException ex) { System.out.println(ex.getMessage());}
            }
        }
        
        if(deleted == 0) {
            response.sendRedirect(request.getContextPath() + "/?delete=fail");
        } else if(total == deleted) {
            response.sendRedirect(request.getContextPath() + "/?delete=succcess");
        } else if(total > deleted) {
            response.sendRedirect(request.getContextPath() + "/?delete=not_completely");
        }
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
