

package com.log.context;

import com.log.context.users.User;
import com.log.context.users.UserList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.xml.sax.SAXException;


public class Security
{
    
    private static final Logger log = Logger.getLogger(Security.class.getName());
    private final File securityFile;
    private final File securitySchemaFile;
    
    private static volatile Security instance;
    private SecurityManager securityManager;
    
    public static enum SecurityStatus {
        NORMAL,
        ERROR_NO_FILE,
        ERROR_INVALID_FILE,
        UNKNOWN_ERROR
    };
    private SecurityStatus securityStatus = SecurityStatus.NORMAL;
    private String securityStatusMessage;
    
    private UserList userList;
    
    public static enum UserStatus {
        APPROWED,
        NULL_LOGIN,
        NULL_PASSWORD,
        NULL_USER,
        INCORRECT_PASSWORD,
        INCORRECT_IP,
        UNKNOWN_ERROR
    }
    
    private Security(ServletContext servletContext)
    {
        if(servletContext.getInitParameter("userList") != null) {
            securityFile = new File(servletContext.getInitParameter("userList"));
        }
        else {
            securityFile = new File(servletContext.getRealPath("/WEB-INF/security/"), "users.xml");
        }
        
        if(servletContext.getInitParameter("userListSchema") != null) {
            securitySchemaFile = new File(servletContext.getInitParameter("userListSchema"));
        }
        else {
            securitySchemaFile = new File(securityFile.getParent(), "security.xsd");
        }
        
        userList = new UserList();
        
        
        try {
            checkSecurityFiles();
        }
        catch (FileNotFoundException ex) {
            securityStatus = SecurityStatus.ERROR_NO_FILE;
            securityStatusMessage = ex.getMessage();
            log.log(Level.SEVERE, "", ex);
        }
        catch(Exception ex)  {
            securityStatus = SecurityStatus.UNKNOWN_ERROR;
            securityStatusMessage = ex.getMessage();
            log.log(Level.SEVERE, "", ex);
        }
        
        if(securityStatus == SecurityStatus.NORMAL) {
            try {
                userList.load(securityFile, securitySchemaFile);
            }
            catch (SAXException | IOException | ParserConfigurationException ex) {
                securityStatus = SecurityStatus.ERROR_INVALID_FILE;
                securityStatusMessage = ex.getMessage();
                log.log(Level.SEVERE, "", ex);
            }
        }
        
   
    }
    
    public SecurityStatus getSecurityStatus()
    {
        return securityStatus;
    }
    
    public SecurityManager getSecurityManager()
    {
        return securityManager;
    }
    
    public String getSecurityStatusMessage()
    {
        return securityStatusMessage;
    }
    
    public static synchronized Security getInstance(ServletContext servletContext)
    {
        if(instance == null)
        {
            instance = new Security(servletContext);
        }
        
        return instance;
    }
    
    public static synchronized Security getInstnace() throws NullPointerException
    {
        if(instance == null)
        {
            throw new NullPointerException("Security not initialized!");
        }
        
        return instance;
    }
    
    public UserList getUserList(){
        return userList;
    }
    
    public UserStatus checkAuth(String login, String password, String ip){
        if(login == null) {
            return UserStatus.NULL_LOGIN;
        }
        if(login.isEmpty()) {
            return UserStatus.NULL_LOGIN;
        }
        
        if(password == null) {
            return UserStatus.NULL_PASSWORD;
        }
        if(password.isEmpty()) {
            return UserStatus.NULL_PASSWORD;
        }
        
        if(ip == null) {
            return UserStatus.UNKNOWN_ERROR;
        }
        if(ip.isEmpty()) {
            return UserStatus.UNKNOWN_ERROR;
        }
        
        User user;
        try {
            user = userList.get(login);
        }
        catch(NullPointerException ex) {
            return UserStatus.NULL_USER;
        }
        
        if(!user.getIp().equals(ip)) {
            return UserStatus.INCORRECT_IP;
        }
        
        try {
            password = getMD5Hash(password);
        }
        catch(NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            return UserStatus.UNKNOWN_ERROR;
        }
        
        if(!user.getPassword().equals(password)) {
            return UserStatus.INCORRECT_PASSWORD;
        }
        
        return UserStatus.APPROWED;
    }
    
    
    
    private void checkSecurityFiles() throws FileNotFoundException, Exception {
        if(!securityFile.exists() || !securityFile.canRead()) {
            throw new FileNotFoundException("File '" + securityFile.getPath() + "' not found");
        }
        
        if(!securitySchemaFile.exists() || !securitySchemaFile.canRead()) {
            throw new Exception("XMLSchema file '" + securitySchemaFile.getPath() + "' not found");
        }
    }
    
    public void reInit(User admin) throws ParserConfigurationException, SAXException, TransformerException, NoSuchAlgorithmException, Exception {
        userList.clear();
        userList.add(admin);
        
        userList.write(securityFile, securitySchemaFile);
        
        securityStatus = SecurityStatus.NORMAL;
    }
    
    public String getMD5Hash(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException  {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte[] passwordHash = messageDigest.digest(password.getBytes("UTF-8"));
        
        StringBuilder sb = new StringBuilder(2*passwordHash.length); 
        for(byte b : passwordHash)
        {
            sb.append(String.format("%02x", b&0xff));
        }
        return sb.toString();
    }
            
    
}
