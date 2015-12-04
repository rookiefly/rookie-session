package com.rookiefly.session;

import java.io.Serializable;
import java.util.Set;

/**
 * 自定义session接口类
 */
public interface Session extends Serializable {

    String getId();

    <T> T getAttribute(String attributeName);

    Set<String> getAttributeNames();

    void setAttribute(String attributeName, Object attributeValue);

    void removeAttribute(String attributeName);

    long getCreationTime();

    long getLastAccessedTime();

    void setMaxInactiveIntervalInSeconds(int interval);

    int getMaxInactiveIntervalInSeconds();

    boolean isExpired();
}