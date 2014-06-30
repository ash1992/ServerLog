package com.log.models;

import java.util.Enumeration;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;


public class Props
{
    private static final ConcurrentHashMap properties = new ConcurrentHashMap<>();
    
    public Props()
    {
    }
    
    public synchronized static void add(String name, Object value)
    {
        properties.put(name, value);
    }
    
    public static Object get(String name)
    {
        return properties.get(name);
    }
    
    public static int getInt(String name)
    {
        return (int)properties.get(name);
    }
    
    public static int getInt(String name, int defaultValue)
    {
        Object value = properties.get(name);
        if(value == null)
        {
            return defaultValue;
        }
        
        if(value instanceof Integer)
        {
            return (int)value;
        }
        else if(value instanceof String)
        {
            try
            {
                return Integer.parseInt((String)value);
            }
            catch(NumberFormatException ex)
            {
                return defaultValue;
            }
        }
        else
        {
            return defaultValue;
        }
    }
    
    public void set(Properties props)
    {
        Enumeration keys = props.keys();
        while(keys.hasMoreElements())
        {
            String key = (String)keys.nextElement();
            String value = props.getProperty(key);
            
            add(key, value);
        }
    }
}
