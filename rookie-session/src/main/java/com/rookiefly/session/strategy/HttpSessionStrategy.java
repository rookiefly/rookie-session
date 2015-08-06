package com.rookiefly.session.strategy;

import com.rookiefly.session.Session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HttpSessionStrategy {

	String getRequestedSessionId(HttpServletRequest request);

	void onNewSession(Session session, HttpServletRequest request, HttpServletResponse response);

	void onInvalidateSession(HttpServletRequest request, HttpServletResponse response);
}
