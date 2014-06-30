package com.log.context.users;

import com.log.context.users.User.Level;


public class User
{
    
    public static enum Level
    {
        USER,
        MODERATOR,
        ADMIN
    };
    
    
    private final String name;
    private final String password;
    private final Level level;
    private final String description;
    private String ip;
    
    public User(String name, String password, Level type, String description)
    {
        this.name = name;
        this.password = password;
        this.level = type;
        this.description = description;
    }
    
    public User(String name, String password, String level, String description)
    {
        this.name = name;
        this.password = password;
        this.level = getLevelByName(level);
        this.description = description;
    }
    
    public void setIp(String ip)
    {
        this.ip = ip;
    }
    
    public String getIp()
    {
        return ip;
    }
    
    public String getName()
    {
        return name;
    }
    
    public String getPassword()
    {
        return password;
    }
    
    public Level getLevel()
    {
        return level;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean comparePassword(String password)
    {
        return this.password.equals(password);
    }
    
    public String getLevelName() {
        switch(level)
        {
            case ADMIN:
                return "Admin";
            case MODERATOR:
                return "Moderator";
            case USER:
            default:
                return "User";
        }
        
    }
    
    private Level getLevelByName(String levelName) {
        levelName = levelName.toUpperCase();
        switch(levelName)
        {
            case "ADMIN":
                return Level.ADMIN;
            case "MODERATOR":
                return Level.MODERATOR;
            case "USER":
            default:
                return Level.USER;
        }
    }
        
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("login: ").append(getName()).append("\n");
        sb.append("password: ").append(getPassword()).append("\n");
        sb.append("level: ").append(getLevelName()).append("\n");
        if(description != null)  sb.append("description: ").append(getDescription()).append("\n");
        
        return sb.toString();
    }
    
}
