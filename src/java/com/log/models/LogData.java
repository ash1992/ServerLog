

package com.log.models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LogData
{
    
    private final Pattern LOG_PATTERN = Pattern.compile("(?s)"
            + "(?<date>\\d{2}-[a-zA-Z]{3}-\\d{4}\\s\\d{2}:\\d{2}:\\d{2}\\.\\d{3})\\s"
            + "(?<type>[a-zA-Z]*)(?<content>.*?)"
            + "(?=(\\d{2}-[a-zA-Z]{3}-\\d{4}\\s\\d{2}:\\d{2}:\\d{2}\\.\\d{3}))");
    
    private final ArrayList<LogEntry> logEntries = new ArrayList<>();

    
    public LogData(File logFile) throws FileNotFoundException, IOException, ParseException
    {
        StringBuilder stringBuilder;
        try (BufferedReader reader = new BufferedReader(new FileReader(logFile)))
        {
            String line;
            stringBuilder = new StringBuilder();
            while((line = reader.readLine()) != null)
            {
                stringBuilder.append(line).append("<br/>");
            }
            reader.close();
        }
        
        Matcher m = LOG_PATTERN.matcher(stringBuilder.toString());
        
        while(m.find())
        {
            LogEntry logEntry = new LogEntry();
            
            logEntry.setDate(new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss.SSS", Locale.ENGLISH).parse(m.group("date")));
            logEntry.setType(m.group("type"));
            logEntry.setContent(m.group("content"));
            
            logEntries.add(logEntry);
        }
        
    }
    
    public ArrayList<LogEntry> getLogEntries()
    {
        return logEntries;
    }
        
        
}
