

package com.log.context.users;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.SchemaFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class UserList {
    
    private final ConcurrentHashMap<String, User> userList;
    
    public UserList() {
        userList = new ConcurrentHashMap<>();
    }
    
    public void load(File securityFile, File schemaFile) throws SAXException, ParserConfigurationException, IOException, NullPointerException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        documentBuilderFactory.setSchema(schemaFactory.newSchema(schemaFile));
        documentBuilderFactory.setIgnoringElementContentWhitespace(true);
        
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(securityFile);
        document.normalize();
        
        NodeList usersNodeList = document.getElementsByTagName("users").item(0).getChildNodes();
        
        for(int i = 0; i<usersNodeList.getLength(); i++) {
            
            Node userNode = usersNodeList.item(i);
            NodeList userNodeList = userNode.getChildNodes();
            String login = null, password = null, level = null, description = null;
            for(int k = 0;  k<userNodeList.getLength(); k++) {
                
                Node userDataNode = userNodeList.item(k);
                
                switch(userDataNode.getNodeName()) {
                    case "login":
                        login = userDataNode.getTextContent();
                        break;
                    case "password":
                        password = userDataNode.getTextContent();
                        break;
                    case "level":
                        level = userDataNode.getTextContent();
                        break;
                    case "description":
                        description = userDataNode.getTextContent();
                        break;
                }
            }
            
            if(login!= null && password!= null && level != null) {
                User user = new User(login, password, level, description);
                add(user);
            }
        }
    }
    
    public void add(User user) {
        userList.put(user.getName(), user);
    }
    
    public User get(String login) {
        if(login == null) {
            throw new NullPointerException("login is null!");
        }
        
        User user = userList.get(login);
        if(user == null) {
            throw new NullPointerException("User not found!");
        }
        
        return user;
    }
    
    public void clear(){
        userList.clear();
    }
    
    public void write(File securityFile, File schemaFile) 
            throws IOException, ParserConfigurationException, SAXException, TransformerException, NoSuchAlgorithmException, Exception {
        
        if(userList.isEmpty()) {
            throw new Exception("Empty userList!");
        }
        
        securityFile.delete();
        securityFile.createNewFile();
        
        
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        docFactory.setSchema(schemaFactory.newSchema(schemaFile));
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // root elements
        Document doc = docBuilder.newDocument();
        Element users = doc.createElement("users");
        users.setAttribute("xmlns:xsi", XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI);
        users.setAttribute("xsi:noNamespaceSchemaLocation", schemaFile.getName());
        doc.appendChild(users);
        
        Iterator<User> usersIterator = userList.values().iterator();
        
        while(usersIterator.hasNext())
        {
            User user = usersIterator.next();
            String login = user.getName();
            String password = user.getPassword();
            String level = user.getLevelName();
            String description = user.getDescription();
            
            Element userNode = doc.createElement("user");
            users.appendChild(userNode);

            Element loginNode = doc.createElement("login");
            loginNode.appendChild(doc.createTextNode(login));
            userNode.appendChild(loginNode);

            Element passwordNode = doc.createElement("password");
            passwordNode.appendChild(doc.createTextNode(password));
            userNode.appendChild(passwordNode);

            Element levelNode = doc.createElement("level");
            levelNode.appendChild(doc.createTextNode(level));
            userNode.appendChild(levelNode);

            if(description != null)
            {
                Element descriptionNode = doc.createElement("description");
                descriptionNode.appendChild(doc.createTextNode(description));
                userNode.appendChild(descriptionNode);
            }
        }
        
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(securityFile);

        transformer.transform(source, result);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        java.util.Enumeration<User> userEnum = userList.elements();
        
        while(userEnum.hasMoreElements()) {
            User user = userEnum.nextElement();
            sb.append("------------------------------\n");
            sb.append(user.toString());
        }
        
        return sb.toString();
    }
}
