
package com.log.models;

import java.util.Date;


public class LogEntry
{
    public enum Type
    {
        SEVERE,
        WARNING,
        INFO,
        DEBUG,
        UNKNOWN
    }
    
    private Date date;
    private Enum type = Type.UNKNOWN;
    private String content;
    
    public LogEntry()
    {
        
    }
    
    protected void setDate(Date date)
    {
        this.date = date;
    }
    
    protected void setType(Type type)
    {
        this.type = type;
    }
    
    protected void setType(String type)
    {
        switch(type)
        {
            case "SEVERE":
                this.type = Type.SEVERE;
            break;
            case "INFO":
                this.type = Type.INFO;
            break;
            case "DEBUG":
                this.type = Type.DEBUG;
            break;
            case "WARNING":
                this.type = Type.WARNING;
            break;
            default:
                this.type = Type.UNKNOWN;
        }
    }
    
    protected void setContent(String content)
    {
        this.content = content;
    }
    
    public String getContent()
    {
        return content;
    }
    
    public Enum getType()
    {
        return type;
    }
    
    public Date getDate()
    {
        return date;
    }
}
