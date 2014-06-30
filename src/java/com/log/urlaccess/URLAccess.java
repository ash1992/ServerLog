package com.log.urlaccess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;


public class URLAccess
{
    private final ServletContext servletContext;
    private final String realPath;
    private volatile static URLAccess instance;
    private final ConcurrentHashMap<File,ACCESS_TYPE> access = new ConcurrentHashMap<>();
    
    public static enum ACCESS_TYPE
    {
        ALLOWED,
        FORBIDDEN,
        NO_FILTER
    };
    
    private URLAccess(ServletContext context)
    {
        servletContext = context;
        realPath = servletContext.getRealPath("/");
        
        try
        {
            scanner(new File(realPath));
        }
        catch(IOException  ex)
        {
            Logger.getLogger(URLAccess.class.getName()).log(Level.SEVERE, "", ex);
        }
    }
    
    public  static  URLAccess  getInstance()
    {
        if(instance == null)
        {
            throw new NullPointerException("URLAcceess not initialized");
        }

        return instance;
    }
    
    public synchronized static URLAccess  getInstance(ServletContext context)
    {
        if(instance == null)
        {
            instance = new URLAccess(context);
        }

        return instance;
    }
    
    
    private void scanner(File folder) throws IOException
    {
        File accessFile = new File(folder, ".access");
        if(accessFile.isFile())
        {
            String type;
            try (FileReader fileReader = new FileReader(accessFile); BufferedReader bufferedReader = new BufferedReader(fileReader))
            {
                type = bufferedReader.readLine();
                bufferedReader.close();
                fileReader.close();
            }
            
            if(type == null) return;
            
            type = type.toUpperCase();
            
            switch(type)
            {
                default:
                    return;

                case "FORBIDDEN":
                    access.put(folder, ACCESS_TYPE.FORBIDDEN);
                break;
                case "NO_FILTER":
                    access.put(folder, ACCESS_TYPE.NO_FILTER);
                break;
            }
        }
        else
        {
            File[] files = folder.listFiles();
            for(File file : files)
            {
                if(file.isDirectory())
                {
                    scanner(file);
                }
            }
        }
    }
    
    
    public ACCESS_TYPE getStatus(HttpServletRequest request)
    {
        String url = request.getRequestURI().replace(servletContext.getContextPath(), "");
        if(url.contains(".jsp"))
        {
            return ACCESS_TYPE.FORBIDDEN;
        }
        
        String[] paths = url.split("/");
        
        String fullPath = "";
        
        for(String path : paths)
        {
            fullPath += path + "/";
            
            File file = new File(realPath, fullPath);
            
            if(access.containsKey(file))
            {
                return access.get(file);
            }
            
        }
        
        return ACCESS_TYPE.ALLOWED;
    }
    
}
