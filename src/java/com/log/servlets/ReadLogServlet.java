
package com.log.servlets;

import com.log.models.Log;
import com.log.models.LogData;
import com.log.models.LogEntry;
import com.log.models.LogFile;
import com.log.models.Props;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ReadLogServlet extends HttpServlet
{

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        RequestDispatcher reqDispatcher = request.getRequestDispatcher("/log.jsp");

        String logName;
        if(request.getParameter("log") != null)
        {
            logName = request.getParameter("log");
        }
        else
        {
            System.out.println("log not found");
            response.sendRedirect("/");
            return;
        }

        File logsPath = (File)Props.get("logsPath");
        Log log = new Log(logsPath.getAbsolutePath(), logName);
        
        
        LogFile logFile = log.getLog();
        System.out.println(logFile.getName());
        try
        {
            LogData logData = logFile.getLogData();
            ArrayList<LogEntry> logEntries = logData.getLogEntries();
            Collections.reverse(logEntries);
            request.setAttribute("log", logEntries);
        }
        catch (FileNotFoundException | ParseException ex)
        {
            Logger.getLogger(ReadLogServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        request.setAttribute("logFile", logFile);


        reqDispatcher.forward(request, response);
    }
}
