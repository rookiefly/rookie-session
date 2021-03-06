package com.rookiefly.session.repository;

import com.rookiefly.session.MapSession;
import com.rookiefly.session.Session;
import com.rookiefly.session.redis.RedisService;
import org.apache.commons.lang.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * redis操作自定义session实现
 */
public class RedisSessionRepository implements SessionRepository {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    public static final int DEFAULT_MAX_INACTIVE_INTERVAL_SECONDS = 7200;                                 //默认redis TTL时间，单位秒

    private Integer defaultMaxInactiveInterval = DEFAULT_MAX_INACTIVE_INTERVAL_SECONDS;

    /**
     * redis client
     */
    private RedisService cacheRedis;

    public RedisSessionRepository() {
    }

    public RedisSessionRepository(RedisService cacheRedis, Integer defaultMaxInactiveInterval) {
        if (cacheRedis == null) {
            throw new IllegalArgumentException("cacheRedis cannot be null");
        }
        this.cacheRedis = cacheRedis;
        this.defaultMaxInactiveInterval = defaultMaxInactiveInterval;
    }

    public void setDefaultMaxInactiveInterval(int defaultMaxInactiveInterval) {
        this.defaultMaxInactiveInterval = Integer.valueOf(defaultMaxInactiveInterval);
    }

    public void save(Session session) {
        try {
            logger.debug("Adding session {}", session);
            byte[] bs = SerializationUtils.serialize(session);
            cacheRedis.set(session.getId().getBytes(), bs, getDefaultMaxInactiveInterval());
        } catch (Exception e) {
            logger.debug("Failed adding {}", session, e);
        }
    }

    public Session getSession(String sessionId) {
        Session session = null;
        try {
            byte[] bs = cacheRedis.get(sessionId.getBytes());
            session = (Session) SerializationUtils.deserialize(bs);
        } catch (Exception e) {
            logger.error("Failed fetching {} ", sessionId, e);
        }
        return session;
    }

    public void delete(String sessionId) {
        logger.debug("Deleting session {}", sessionId);
        try {
            cacheRedis.del(sessionId.getBytes());
        } catch (Exception e) {
            logger.error("Failed deleting {}", sessionId, e);
        }
    }

    public Session createSession() {
        Session session = new MapSession();
        if (defaultMaxInactiveInterval != null) {
            session.setMaxInactiveIntervalInSeconds(defaultMaxInactiveInterval);
        }
        return session;
    }

    public Session createSession(String sessionId) {
        Session session = new MapSession(sessionId);
        if (defaultMaxInactiveInterval != null) {
            session.setMaxInactiveIntervalInSeconds(defaultMaxInactiveInterval);
        }
        return session;
    }

    public Integer getDefaultMaxInactiveInterval() {
        return defaultMaxInactiveInterval;
    }

    public void setDefaultMaxInactiveInterval(Integer defaultMaxInactiveInterval) {
        this.defaultMaxInactiveInterval = defaultMaxInactiveInterval;
    }

    public RedisService getCacheRedis() {
        return cacheRedis;
    }

    public void setCacheRedis(RedisService cacheRedis) {
        this.cacheRedis = cacheRedis;
    }
}
