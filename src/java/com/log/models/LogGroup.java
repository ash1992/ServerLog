package com.log.models;

import java.util.ArrayList;


public class LogGroup
{
    private final ArrayList<LogFile> logFiles = new ArrayList<>();
    private final String logName;
    
    public LogGroup(String logName)
    {
        this.logName = logName;
    }
    
    public void add(LogFile logEntity)
    {
        logFiles.add(logEntity);
    }
    
    public String getName()
    {
        return logName;
    }
    
    public ArrayList<LogFile> getLogFiles()
    {
        return logFiles;
    }
    
    public int count()
    {
        return logFiles.size();
    }
    
    public void remove(int id)
    {
        logFiles.remove(id);
    }
}
