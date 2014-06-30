

package com.log.context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.SchemaFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;


public class SecurityManager {
    
    
    protected SecurityManager(File securityFile, File schemaFile) throws FileNotFoundException, ParserConfigurationException, Exception {
        if(!securityFile.exists() || !securityFile.canRead()) {
            throw new FileNotFoundException("File '" + securityFile.getPath() + "' not found");
        }
        
        if(!schemaFile.exists() || !schemaFile.canRead()) {
            throw new Exception("XMLSchema file '" + schemaFile.getPath() + "' not found");
        }
        
        try {
            load(securityFile, schemaFile);
        }
        catch(ParserConfigurationException | SAXException | IOException ex) {
            throw new ParserConfigurationException("File '" + securityFile.getPath() + "' invalid!\n<br/>" + ex.getMessage());
        }
    }
    
    protected SecurityManager(File securityFile) throws FileNotFoundException, ParserConfigurationException, Exception
    {
        this(securityFile, new File(securityFile.getParent(), "security.xsd"));
    }
    
    
    private void load(File securityFile, File schemaFile) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        documentBuilderFactory.setSchema(schemaFactory.newSchema(schemaFile));
        documentBuilderFactory.setIgnoringElementContentWhitespace(true);
        
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(securityFile);
        
        
    }
    
}
