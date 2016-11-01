/**************************************************
 * Android Web Server
 * Based on JavaLittleWebServer (2008)
 * <p/>
 * Copyright (c) Piotr Polak 2016-2016
 **************************************************/

package ro.polak.webserver.servlet;

import java.util.Enumeration;

/**
 * Http session
 *
 * @url https://tomcat.apache.org/tomcat-5.5-doc/servletapi/javax/servlet/http/HttpSession.html
 */
interface HttpSession {

    /**
     * Gets session attribute of the specified name.
     *
     * @param name Attribute name
     * @return
     * @throws IllegalStateException
     */
    String getAttribute(String name) throws IllegalStateException;

    /**
     * Returns enumeration representing attribute names.
     *
     * @return
     * @throws IllegalStateException
     */
    Enumeration getAttributeNames() throws IllegalStateException;

    /**
     * Returns session creation time in milliseconds.
     *
     * @return
     * @throws IllegalStateException
     */
    long getCreationTime() throws IllegalStateException;

    /**
     * Returns session id.
     *
     * @return
     */
    String getId() /*throws IllegalStateException*/;

    /**
     * Returns session last access time in milliseconds.
     *
     * @return
     * @throws IllegalStateException
     */
    long getLastAccessedTime() throws IllegalStateException;

    /**
     * Returns max inactive interval in seconds.
     *
     * @return
     */
    int getMaxInactiveInterval();

    /**
     * Returns servlet context.
     *
     * @return
     */
    ServletContext getServletContext();

    /**
     * Sets maximum inactive interval in seconds.
     *
     * @param maxInactiveInterval
     */
    void setMaxInactiveInterval(int maxInactiveInterval);

    /**
     * Invalidates session (marks for removal).
     *
     * @throws IllegalStateException
     */
    void invalidate() throws IllegalStateException;

    /**
     * Removes an attribute.
     *
     * @param name
     * @throws IllegalStateException
     */
    void removeAttribute(String name) throws IllegalStateException;

    /**
     * Tells whether the session has just been created.
     *
     * @return
     * @throws IllegalStateException
     */
    boolean isNew() throws IllegalStateException;

    /**
     * Sets session attribute.
     *
     * @param name
     * @param value
     * @throws IllegalStateException
     */
    void setAttribute(String name, String value) throws IllegalStateException;
}