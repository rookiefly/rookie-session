package com.rookiefly.session;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * map实现session
 */
public final class MapSession implements Session, Serializable {

    public static final int DEFAULT_MAX_INACTIVE_INTERVAL_SECONDS = 1800;

    private String id = UUID.randomUUID().toString();
    private Map<String, Object> sessionAttrs = new HashMap<String, Object>();
    private long creationTime = System.currentTimeMillis();
    private long lastAccessedTime = creationTime;

    private int maxInactiveInterval = DEFAULT_MAX_INACTIVE_INTERVAL_SECONDS;

    public MapSession() {
    }

    public MapSession(String sessionId) {
        this.id = sessionId;
    }

    public MapSession(Session session) {
        if (session == null) {
            throw new IllegalArgumentException("session cannot be null");
        }
        this.id = session.getId();
        this.sessionAttrs = new HashMap<String, Object>(session.getAttributeNames().size());
        for (String attrName : session.getAttributeNames()) {
            Object attrValue = session.getAttribute(attrName);
            this.sessionAttrs.put(attrName, attrValue);
        }
        this.lastAccessedTime = session.getLastAccessedTime();
        this.creationTime = session.getCreationTime();
        this.maxInactiveInterval = session.getMaxInactiveIntervalInSeconds();
    }

    public void setLastAccessedTime(long lastAccessedTime) {
        this.lastAccessedTime = lastAccessedTime;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public String getId() {
        return id;
    }

    public long getLastAccessedTime() {
        return lastAccessedTime;
    }

    public void setMaxInactiveIntervalInSeconds(int interval) {
        this.maxInactiveInterval = interval;
    }

    public int getMaxInactiveIntervalInSeconds() {
        return maxInactiveInterval;
    }

    public boolean isExpired() {
        return isExpired(System.currentTimeMillis());
    }

    boolean isExpired(long now) {
        if (maxInactiveInterval < 0) {
            return false;
        }
        return now - TimeUnit.SECONDS.toMillis(maxInactiveInterval) >= lastAccessedTime;
    }

    public Object getAttribute(String attributeName) {
        return sessionAttrs.get(attributeName);
    }

    public Set<String> getAttributeNames() {
        return sessionAttrs.keySet();
    }

    public void setAttribute(String attributeName, Object attributeValue) {
        if (attributeValue == null) {
            removeAttribute(attributeName);
        } else {
            sessionAttrs.put(attributeName, attributeValue);
        }
    }

    public void removeAttribute(String attributeName) {
        sessionAttrs.remove(attributeName);
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean equals(Object obj) {
        return obj instanceof Session && id.equals(((Session) obj).getId());
    }

    public int hashCode() {
        return id.hashCode();
    }

    private static final long serialVersionUID = 7160779239673823561L;
}