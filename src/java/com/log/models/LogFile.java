

package com.log.models;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class LogFile
{
    private final File logFile;
    private final String name;
    private Date date;
    private long fileSize = 0;
    
    
    public LogFile(File logFile, long fileSize, String name, String date)
    {
        this.name = name;
        this.logFile = logFile;
        this.fileSize = fileSize;
        
        try
        {
            this.date = new SimpleDateFormat("yyyy-M-d", Locale.ENGLISH).parse(date);
        }
        catch(ParseException ex)
        {
            this.date = new Date(0);
        }
    }
    
    public void setDate(Date date)
    {
        this.date = date;
    }
    
    
    public String getName()
    {
        return name;
    }
    
    public Date getDate()
    {
        return date;
    }
    
    public Date getLastModifed()
    {
        return new Date(logFile.lastModified());
    }
    
    public long getSize()
    {
        return fileSize;
    }
    
    public String getRealName()
    {
        return logFile.getName();
    }
    
    public LogData getLogData() throws IOException, FileNotFoundException, ParseException
    {
        return new LogData(logFile);
    }
}
