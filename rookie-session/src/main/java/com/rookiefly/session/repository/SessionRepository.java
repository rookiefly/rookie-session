package com.rookiefly.session.repository;

import com.rookiefly.session.Session;

/**
 * session操作的接口类
 * @param <S>
 */
public interface SessionRepository<S extends Session> {

    S createSession();

    void save(S session);

    S getSession(String id);

    void delete(String id);
}