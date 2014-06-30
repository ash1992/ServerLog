package com.log.context;


public class ContextStatus {
    
    public static final int NULL_LOG_PATH = 1;
    public static final int INVALID_LOG_PATH = 2;
    public static final int NORMAL = 100;
    
    private static int contextStatus = NORMAL;
    private static String statusText = "OK!";

    
    protected ContextStatus()
    {
        
    }
    
    public void setStatus(int status, String text)
    {
        contextStatus = status;
        statusText = text;
    }
    
    public void setStatus(int status)
    {
        contextStatus = status;
        statusText = getDefaultStatusText(status);
    }
    
    public static int getStatus()
    {
        return contextStatus;
    }
    
    
    public static String getStatusText()
    {
        return statusText;
    }
    
    public String getDefaultStatusText(int status)
    {
        switch(status)
        {
            default:
                return "Null";
            case 1:
                return "Parameter 'logsPath' not found!";
            case 2:
                return "Logs folder not found";
        }
    }
}
