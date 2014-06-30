
package com.log.context;

import com.log.models.Props;
import com.log.urlaccess.URLAccess;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;


public class Context {
    
    private static final Logger log  = Logger.getLogger(Context.class.getName());
    
    private volatile static Context instance;
    private final ServletContext servletContext;
    private final ContextStatus contextStatus;
    
    private Context(ServletContext servletContext)
    {
        this.servletContext = servletContext;
        contextStatus = new ContextStatus();
        
        
        URLAccess.getInstance(servletContext);
        
        try
        {
            File logsPath = getLogPath();
            Props.add("logsPath", logsPath);
        }
        catch(Exception ex)
        {
            log.log(Level.SEVERE, null, ex);
            return;
        }
        
        try
        {
            Security.getInstance(servletContext);
        }
        catch(Exception ex)
        {
            contextStatus.setStatus(ContextStatus.NORMAL, ex.getMessage());
            log.log(Level.SEVERE, null, ex);
            
        }
    }
    
    
    protected static synchronized Context getInstance(ServletContext sc)
    {
        if(instance == null)
        {
            instance = new Context(sc);
        }
        
        
        return instance;
    }
    
    private File getLogPath() throws Exception
    {
        //Имя папки с логами
        String logsFolderName;
        
        if(servletContext.getInitParameter("logsPath") != null)
        {
            logsFolderName = servletContext.getInitParameter("logsPath");
        }
        else if(System.getProperty("catalina.base") != null)
        {
            logsFolderName = System.getProperty("catalina.base") + File.separator + "logs";
        }
        else
        {
            contextStatus.setStatus(ContextStatus.NULL_LOG_PATH);
            throw new Exception(contextStatus.getDefaultStatusText(ContextStatus.NULL_LOG_PATH));
        }
        
        //Папка с логами
        File logsPath = new File(logsFolderName);
        
        if(!logsPath.isDirectory())
        {
            contextStatus.setStatus(ContextStatus.INVALID_LOG_PATH, "Folder '" + logsPath + "' not found");
            throw new Exception("Folder '" + logsPath + "' not found");
        }

        return logsPath;
    }
    
    
    
    
    public ContextStatus getContextStatus()
    {
        return contextStatus;
    }
}
