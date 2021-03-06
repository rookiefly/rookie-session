package com.rookiefly.session.filter;

import com.rookiefly.session.Session;
import com.rookiefly.session.repository.J2CacheSessionRepository;
import com.rookiefly.session.repository.SessionRepository;
import com.rookiefly.session.strategy.CookieHttpSessionStrategy;
import com.rookiefly.session.strategy.HttpSessionStrategy;
import com.rookiefly.session.strategy.MultiHttpSessionStrategy;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;

/**
 * filter拦截请求，包装request和response
 */
public class SessionRepositoryFilter extends OncePerRequestFilter {
    public static final String SESSION_REPOSITORY_ATTR = SessionRepository.class.getName();

    private SessionRepository sessionRepository = new J2CacheSessionRepository();

    private ServletContext servletContext;

    /**
     * http session生成策略
     */
    private MultiHttpSessionStrategy httpSessionStrategy = new CookieHttpSessionStrategy();

    public void setSessionRepository(SessionRepository sessionRepository) {
        if (sessionRepository == null) {
            throw new IllegalArgumentException("sessionRepository cannot be null");
        }
        this.sessionRepository = sessionRepository;
    }

    public void setHttpSessionStrategy(HttpSessionStrategy httpSessionStrategy) {
        if (httpSessionStrategy == null) {
            throw new IllegalArgumentException("httpSessionStrategy cannot be null");
        }
        this.httpSessionStrategy = new MultiHttpSessionStrategyAdapter(httpSessionStrategy);
    }

    public void setHttpSessionStrategy(MultiHttpSessionStrategy httpSessionStrategy) {
        if (httpSessionStrategy == null) {
            throw new IllegalArgumentException("httpSessionStrategy cannot be null");
        }
        this.httpSessionStrategy = httpSessionStrategy;
    }

    /**
     * 拦截器主要功能
     *
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException,
            IOException {
        request.setAttribute(SESSION_REPOSITORY_ATTR, sessionRepository);

        SessionRepositoryRequestWrapper wrappedRequest = new SessionRepositoryRequestWrapper(request, response,
                servletContext);
        SessionRepositoryResponseWrapper wrappedResponse = new SessionRepositoryResponseWrapper(wrappedRequest,
                response);

        HttpServletRequest strategyRequest = httpSessionStrategy.wrapRequest(wrappedRequest, wrappedResponse);
        HttpServletResponse strategyResponse = httpSessionStrategy.wrapResponse(wrappedRequest, wrappedResponse);

        try {
            filterChain.doFilter(strategyRequest, strategyResponse);
        } finally {
            wrappedRequest.commitSession();
        }
    }

    /**
     * response的包装类
     */
    private final class SessionRepositoryResponseWrapper extends OnCommittedResponseWrapper {

        private final SessionRepositoryRequestWrapper request;

        /**
         * @param response the response to be wrapped
         */
        public SessionRepositoryResponseWrapper(SessionRepositoryRequestWrapper request, HttpServletResponse response) {
            super(response);
            if (request == null) {
                throw new IllegalArgumentException("request cannot be null");
            }
            this.request = request;
        }

        @Override
        protected void onResponseCommitted() {
            request.commitSession();
        }
    }

    /**
     * request包装类，替换session获取方式
     */
    private final class SessionRepositoryRequestWrapper extends HttpServletRequestWrapper {
        private final String CURRENT_SESSION_ATTR = HttpServletRequestWrapper.class.getName();
        private Boolean requestedSessionIdValid;
        private boolean requestedSessionInvalidated;
        private final HttpServletResponse response;
        private final ServletContext servletContext;

        private SessionRepositoryRequestWrapper(HttpServletRequest request, HttpServletResponse response,
                                                ServletContext servletContext) {
            super(request);
            this.response = response;
            this.servletContext = servletContext;
        }

        /**
         * Uses the HttpSessionStrategy to write the session id to response and persist the Session.
         */
        private void commitSession() {
            HttpSessionWrapper wrappedSession = getCurrentSession();
            if (wrappedSession == null) {
                if (isInvalidateClientSession()) {
                    httpSessionStrategy.onInvalidateSession(this, response);
                }
            } else {
                Session session = wrappedSession.session;
                sessionRepository.save(session);
                if (!isRequestedSessionIdValid() || !session.getId().equals(getRequestedSessionId())) {
                    httpSessionStrategy.onNewSession(session, this, response);
                }
            }
        }

        @SuppressWarnings("unchecked")
        private HttpSessionWrapper getCurrentSession() {
            return (HttpSessionWrapper) getAttribute(CURRENT_SESSION_ATTR);
        }

        private void setCurrentSession(HttpSessionWrapper currentSession) {
            if (currentSession == null) {
                removeAttribute(CURRENT_SESSION_ATTR);
            } else {
                setAttribute(CURRENT_SESSION_ATTR, currentSession);
            }
        }

        @SuppressWarnings({"unused", "unchecked"})
        public String changeSessionId() {
            HttpSession session = getSession(false);

            if (session == null) {
                throw new IllegalStateException(
                        "Cannot change session ID. There is no session associated with this request.");
            }

            // eagerly get session attributes in case implementation lazily loads them
            Map<String, Object> attrs = new HashMap<String, Object>();
            Enumeration<String> iAttrNames = session.getAttributeNames();
            while (iAttrNames.hasMoreElements()) {
                String attrName = iAttrNames.nextElement();
                Object value = session.getAttribute(attrName);

                attrs.put(attrName, value);
            }

            sessionRepository.delete(session.getId());
            HttpSessionWrapper original = getCurrentSession();
            setCurrentSession(null);

            HttpSession newSession = getSession();
            original.session = ((HttpSessionWrapper) newSession).session;

            newSession.setMaxInactiveInterval(session.getMaxInactiveInterval());
            for (Map.Entry<String, Object> attr : attrs.entrySet()) {
                String attrName = attr.getKey();
                Object attrValue = attr.getValue();
                newSession.setAttribute(attrName, attrValue);
            }
            return newSession.getId();
        }

        public boolean isRequestedSessionIdValid() {
            if (requestedSessionIdValid == null) {
                String sessionId = getRequestedSessionId();
                Session session = sessionId == null ? null : sessionRepository.getSession(sessionId);
                return isRequestedSessionIdValid(session);
            }

            return requestedSessionIdValid;
        }

        private boolean isRequestedSessionIdValid(Session session) {
            if (requestedSessionIdValid == null) {
                requestedSessionIdValid = session != null;
            }
            return requestedSessionIdValid;
        }

        private boolean isInvalidateClientSession() {
            return getCurrentSession() == null && requestedSessionInvalidated;
        }

        /**
         * request包装类获取session
         *
         * @param create
         * @return
         */
        @Override
        public HttpSession getSession(boolean create) {
            HttpSessionWrapper currentSession = getCurrentSession();
            if (currentSession != null) {
                return currentSession;
            }
            String requestedSessionId = getRequestedSessionId();
            if (requestedSessionId != null) {
                Session session = sessionRepository.getSession(requestedSessionId);
                if (session != null) {
                    this.requestedSessionIdValid = true;
                    currentSession = new HttpSessionWrapper(session, getServletContext());
                    currentSession.setNew(false);
                    setCurrentSession(currentSession);
                    return currentSession;
                }
            }
            if (!create) {
                return null;
            }
            Session session = sessionRepository.createSession();
            currentSession = new HttpSessionWrapper(session, getServletContext());
            setCurrentSession(currentSession);
            return currentSession;
        }

        public ServletContext getServletContext() {
            if (servletContext != null) {
                return servletContext;
            }
            // Servlet 3.0+
            return super.getServletContext();
        }

        @Override
        public HttpSession getSession() {
            return getSession(true);
        }

        @Override
        public String getRequestedSessionId() {
            return httpSessionStrategy.getRequestedSessionId(this);
        }

        /**
         * session包装类
         */
        private final class HttpSessionWrapper implements HttpSession {
            private Session session;
            private final ServletContext servletContext;
            private boolean invalidated;
            private boolean old;

            public HttpSessionWrapper(Session session, ServletContext servletContext) {
                this.session = session;
                this.servletContext = servletContext;
            }

            public long getCreationTime() {
                checkState();
                return session.getCreationTime();
            }

            public String getId() {
                return session.getId();
            }

            public long getLastAccessedTime() {
                checkState();
                return session.getLastAccessedTime();
            }

            public ServletContext getServletContext() {
                return servletContext;
            }

            public void setMaxInactiveInterval(int interval) {
                session.setMaxInactiveIntervalInSeconds(interval);
            }

            public int getMaxInactiveInterval() {
                return session.getMaxInactiveIntervalInSeconds();
            }

            @SuppressWarnings("deprecation")
            public HttpSessionContext getSessionContext() {
                return NOOP_SESSION_CONTEXT;
            }

            public Object getAttribute(String name) {
                checkState();
                return session.getAttribute(name);
            }

            public Object getValue(String name) {
                return getAttribute(name);
            }

            public Enumeration<String> getAttributeNames() {
                checkState();
                return Collections.enumeration(session.getAttributeNames());
            }

            public String[] getValueNames() {
                checkState();
                Set<String> attrs = session.getAttributeNames();
                return attrs.toArray(new String[0]);
            }

            public void setAttribute(String name, Object value) {
                checkState();
                session.setAttribute(name, value);
            }

            public void putValue(String name, Object value) {
                setAttribute(name, value);
            }

            public void removeAttribute(String name) {
                checkState();
                session.removeAttribute(name);
            }

            public void removeValue(String name) {
                removeAttribute(name);
            }

            public void invalidate() {
                checkState();
                this.invalidated = true;
                requestedSessionInvalidated = true;
                setCurrentSession(null);
                sessionRepository.delete(getId());
            }

            public void setNew(boolean isNew) {
                this.old = !isNew;
            }

            public boolean isNew() {
                checkState();
                return !old;
            }

            private void checkState() {
                if (invalidated) {
                    throw new IllegalStateException("The HttpSession has already be invalidated.");
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    private static final HttpSessionContext NOOP_SESSION_CONTEXT = new HttpSessionContext() {
        public HttpSession getSession(String sessionId) {
            return null;
        }

        public Enumeration<String> getIds() {
            return EMPTY_ENUMERATION;
        }
    };

    private static final Enumeration<String> EMPTY_ENUMERATION = new Enumeration<String>() {
        public boolean hasMoreElements() {
            return false;
        }

        public String nextElement() {
            throw new NoSuchElementException("a");
        }
    };

    static class MultiHttpSessionStrategyAdapter implements MultiHttpSessionStrategy {
        private HttpSessionStrategy delegate;

        public MultiHttpSessionStrategyAdapter(HttpSessionStrategy delegate) {
            this.delegate = delegate;
        }

        public String getRequestedSessionId(HttpServletRequest request) {
            return delegate.getRequestedSessionId(request);
        }

        public void onNewSession(Session session, HttpServletRequest request, HttpServletResponse response) {
            delegate.onNewSession(session, request, response);
        }

        public void onInvalidateSession(HttpServletRequest request, HttpServletResponse response) {
            delegate.onInvalidateSession(request, response);
        }

        public HttpServletRequest wrapRequest(HttpServletRequest request, HttpServletResponse response) {
            return request;
        }

        public HttpServletResponse wrapResponse(HttpServletRequest request, HttpServletResponse response) {
            return response;
        }
    }
}
