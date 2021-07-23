package com.mvnforum.jaxb.util;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.Validator;

import com.mvnforum.jaxb.db.Mvnforum;
import com.mvnforum.jaxb.db.ObjectFactory;

public class XMLUtil {
    
    public static JAXBContext jaxbContext = null;
    public static ObjectFactory objectFactory = null;
    public static Marshaller marshaller = null;
    public static Validator validator = null;
    public static Unmarshaller unmarshaller = null;
    public static Mvnforum mvnforum = null;
    
    static {
        try {
            
            jaxbContext = JAXBContext.newInstance("com.mvnforum.jaxb.db");
            marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));
            objectFactory = new ObjectFactory();
            validator = jaxbContext.createValidator();
            unmarshaller = jaxbContext.createUnmarshaller();
            
        } catch (JAXBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public static Mvnforum getMvnforum () throws JAXBException {
        
        if (mvnforum == null) {

            mvnforum = (Mvnforum) unmarshaller.unmarshal(new File ("xml/mvnforum.xml"));
            
        }
        return mvnforum;
    }

    public static JAXBContext getJaxbContext() {
        return jaxbContext;
    }

    public static Marshaller getMarshaller() {
        return marshaller;
    }

    public static ObjectFactory getObjectFactory() {
        return objectFactory;
    }

    public static Validator getValidator() {
        return validator;
    }

    public static Unmarshaller getUnmarshaller() {
        return unmarshaller;
    }

    public static void setUnmarshaller(Unmarshaller unmarshaller) {
        XMLUtil.unmarshaller = unmarshaller;
    }
    
    
   
}
