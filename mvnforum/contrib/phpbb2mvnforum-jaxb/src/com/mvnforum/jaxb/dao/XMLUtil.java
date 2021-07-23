package com.mvnforum.jaxb.dao;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Validator;

import com.mvnforum.jaxb.db.ObjectFactory;

public class XMLUtil {
    
    public static JAXBContext jaxbContext = null;
    public static ObjectFactory objectFactory = null;
    public static Marshaller marshaller = null;
    public static Validator validator = null;
    
    static {
        
        try {
            
            jaxbContext = JAXBContext.newInstance("com.mvnforum.jaxb.db");
            marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));
            objectFactory = new ObjectFactory();
            validator = jaxbContext.createValidator();
            
        } catch (JAXBException e) {
            
            e.printStackTrace();
            
        }
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
   
}
