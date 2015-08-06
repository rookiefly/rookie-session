package com.rookiefly.session.strategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface RequestResponsePostProcessor {

	HttpServletRequest wrapRequest(HttpServletRequest request,
								   HttpServletResponse response);

	HttpServletResponse wrapResponse(HttpServletRequest request,
									 HttpServletResponse response);
}
