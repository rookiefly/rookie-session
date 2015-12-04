package com.rookiefly.session.repository;

import com.rookiefly.session.Session;

/**
 * session操作的接口类
 */
public interface SessionRepository {

    Session createSession();

    void save(Session session);

    Session getSession(String id);

    void delete(String id);
}