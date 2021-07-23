/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/thirdparty/commonj/util/AbstractFactory.java,v 1.4 2008/05/21 07:13:16 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.4 $
 * $Date: 2008/05/21 07:13:16 $
 */
package net.myvietnam.mvncore.thirdparty.commonj.util;

import java.util.HashMap;
import java.util.Map;

import javax.naming.NamingException;
import javax.naming.spi.ObjectFactory;

/**
 * Abstract factory class.
 *
 * @author  Andreas Keldenich
 * @version 1.0
 */
public abstract class AbstractFactory implements ObjectFactory {
    
    /** max number of threads in the pool */
    public static final String CONFIG_MAX_THREADS = "maxThreads";
    /** min number of threads in the pool */
    public static final String CONFIG_MIN_THREADS = "minThreads";
    /** length of the queue */
    public static final String CONFIG_QUEUE_LENGTH = "queueLength";
    
    protected Map managers = new HashMap();
    
    /**
     * Get an integer config value.
     * 
     * @param name config value name
     * @param value config value
     * @return integer value
     */
    protected int getValue(String name, String value) throws NamingException {
        int x = 0;
        try {
            x = Integer.parseInt(value);
        }
        catch (NumberFormatException e) {
            throw new NamingException("Value " + name + " must be an integer.");
        }
        if (x < 0 || x > 100) {
            throw new NamingException("Value " + name + " out of range [0..100]");
        }
        return x;
    }


}
