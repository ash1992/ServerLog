

package com.log.models;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;


public class LogsList {
    private File logPath;
    private FilenameFilter filter;
    
    public LogsList(String logPath)
    {
        this(new File(logPath));
    }
    
    public LogsList(File logPath)
    {
        this.logPath = logPath;
        
        filter = new FilenameFilter()
        {
            @Override
            public boolean accept(File dir, String name)
            {
                return name.endsWith(".log");
            }
        };
    }
    
    public LogGroup[] getLogGroups()
    {
        HashMap<String, LogGroup> logGroups = new HashMap<>();
        
        
        String[] logFiles = logPath.list(filter);
        String[] logGroupNames = getLogGroupNames(logFiles);
        
        for (String logGroupName : logGroupNames)
        {
            logGroups.put(logGroupName, new LogGroup(logGroupName));
        }
        
        for (String logFileName : logFiles)
        {
            String[] logFileData = logFileName.split("\\.");
            if(logFileData.length != 3)
            {
                continue;
            }
            if(!logGroups.containsKey(logFileData[0]))
            {
                continue;
            }
            
            File logFile = new File(logFileName);
            LogFile logEntity = new LogFile(logFile, logFile.length(), logFileData[0], logFileData[1]);
            logGroups.get(logFileData[0]).add(logEntity);
        }
        
        return logGroups.values().toArray(new LogGroup[logGroups.size()]);
    }
    
    private String[] getLogGroupNames(String[] names)
    {
        ArrayList<String> groupNames = new ArrayList<>();
        
        for (String name : names)
        {
            String[] nameData = name.split("\\.");
            
            if(!groupNames.contains(nameData[0]))
            {
                groupNames.add(nameData[0]);
            }
        }
        
        return groupNames.toArray(new String[0]);
    }
}

