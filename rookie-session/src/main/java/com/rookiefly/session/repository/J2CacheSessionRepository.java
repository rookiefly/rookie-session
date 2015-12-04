package com.rookiefly.session.repository;

import com.rookiefly.session.MapSession;
import com.rookiefly.session.Session;
import net.oschina.j2cache.CacheChannel;
import net.oschina.j2cache.J2Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * J2Cache操作自定义session实现
 * http://git.oschina.net/ld/J2Cache
 */
public class J2CacheSessionRepository implements SessionRepository {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private String cacheRegion = "SESSION";

    private CacheChannel cache = J2Cache.getChannel();

    public void save(Session session) {
        try {
            logger.debug("Adding session {}", session);
            cache.set(cacheRegion, session.getId(), session);
        } catch (Exception e) {
            logger.debug("Failed adding {}", session, e);
        }
    }

    public Session getSession(String sessionId) {
        Session session = null;
        try {
            session = (Session) cache.get(cacheRegion, sessionId);
        } catch (Exception e) {
            logger.error("Failed fetching {} ", sessionId, e);
        }
        return session;
    }

    public void delete(String sessionId) {
        logger.debug("Deleting session {}", sessionId);
        try {
            cache.evict(cacheRegion, sessionId);
        } catch (Exception e) {
            logger.error("Failed deleting {}", sessionId, e);
        }
    }

    public Session createSession() {
        Session session = new MapSession();
        return session;
    }

    public Session createSession(String sessionId) {
        Session session = new MapSession(sessionId);
        return session;
    }

    public String getCacheRegion() {
        return cacheRegion;
    }

    public void setCacheRegion(String cacheRegion) {
        this.cacheRegion = cacheRegion;
    }

    public CacheChannel getCache() {
        return cache;
    }

    public void setCache(CacheChannel cache) {
        this.cache = cache;
    }
}
