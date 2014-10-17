package com.sosgps.wzt.util;

import java.io.InputStream;

/**
 * <p>Title: Toten</p>
 * <p>Description:
 * A utility class to assist with loading classes by name. Many application servers use
 * custom classloaders, which will break uses of:
 * <pre>
 *    Class.forName(className);
 * </pre>
 *
 * This utility attempts to load the class using a number of different mechanisms to work
 * around this problem.
 */
public class ClassUtility {

    private static ClassUtility instance = new ClassUtility();

    /**
     * Loads the class with the specified name.
     *
     * @param className the name of the class
     * @return the resulting <code>Class</code> object
     * @exception ClassNotFoundException if the class was not found
     */
    public static Class forName(String className) throws ClassNotFoundException {
        return instance.loadClass(className);
    }

    public static InputStream getResourceAsStream(String name) {
        return instance.loadResource(name);
    }

    private ClassUtility() {}

    public Class loadClass(String className) throws ClassNotFoundException {
        Class theClass = null;
        try {
            theClass = Class.forName(className);
        }
        catch (ClassNotFoundException e1) {
            try {
                theClass = Thread.currentThread().getContextClassLoader().loadClass(className);
            }
            catch (ClassNotFoundException e2) {
                theClass = getClass().getClassLoader().loadClass(className);
            }
        }
        return theClass;
    }

    public InputStream loadResource(String name) {
        InputStream in = getClass().getResourceAsStream(name);
        if (in == null) {
            in = Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
            if (in == null) {
                in = getClass().getClassLoader().getResourceAsStream(name);
            }
        }
        return in;
    }
}
