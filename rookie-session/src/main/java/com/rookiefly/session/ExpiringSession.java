package com.rookiefly.session;

public interface ExpiringSession extends Session {

	long getCreationTime();

	long getLastAccessedTime();

	void setMaxInactiveIntervalInSeconds(int interval);

	int getMaxInactiveIntervalInSeconds();

	boolean isExpired();

}
