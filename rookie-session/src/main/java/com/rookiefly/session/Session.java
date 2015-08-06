package com.rookiefly.session;

import java.io.Serializable;
import java.util.Set;

public interface Session extends Serializable {

	String getId();

	<T> T getAttribute(String attributeName);

	Set<String> getAttributeNames();

	void setAttribute(String attributeName, Object attributeValue);

	void removeAttribute(String attributeName);
}