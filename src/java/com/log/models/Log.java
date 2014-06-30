package com.log.models;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.regex.Pattern;

/**
 * @author Александр Кузнецов
 */
public class Log
{
    private final Pattern fileNamePattern = Pattern.compile("^([a-zA-Z0-9-]*\\.\\d{4}-\\d{2}-\\d{2}\\.log)+$");
    private final File logFile;
    private final LogFile log;
    
    public Log(String path, String logName) throws UnsupportedOperationException
    {
        this(new File(path), logName);
    }
    
    public Log(File path, String logName) throws UnsupportedOperationException {
        
        if(!fileNamePattern.matcher(logName).matches()) {
            throw new UnsupportedOperationException("log file name use unsupported format");
        }
        
        logFile = new File(path, logName);
        
        if(!logFile.exists() || !logFile.isFile() || !logFile.canRead()) {
            throw new UnsupportedOperationException("log file not found");
        }
        
        String[] logFileData = logName.split("\\.");
        if(logFileData.length != 3) {
            throw new UnsupportedOperationException("log file not supported");
        }
        
        log = new LogFile(logFile, logFile.length(), logFileData[0], logFileData[1]);
    }
    
    public LogFile getLog()
    {
        return log;
    }
    
    public boolean delete()
    {
        return logFile.delete();
    }
    
    public BufferedInputStream read() throws FileNotFoundException
    {
        FileInputStream fis = new FileInputStream(logFile);
        BufferedInputStream bfis = new BufferedInputStream(fis);
        
        return bfis;
    }

}
