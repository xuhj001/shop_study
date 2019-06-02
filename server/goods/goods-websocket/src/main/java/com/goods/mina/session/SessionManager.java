package com.goods.mina.session;

import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.session.IoSession;

//mina��ܵ�session����͹���
public class SessionManager {
	private static SessionManager sessionManager;

	private SessionManager() {
	}

	private Map<Long, IoSession> map = new HashMap<Long, IoSession>();// �����������ӵĿͻ��˵�session

	public static SessionManager getInstance() {
		if (sessionManager == null) {
			synchronized (SessionManager.class) {
				if (sessionManager == null) {
					sessionManager = new SessionManager();
				}
			}
		}
		return sessionManager;
	}

	public void setSession(IoSession ioSession, long userId) {
		map.put(userId, ioSession);
	}

	// ��ͻ���д��Ϣ
	public void writeToClient(Object o, long userId) {
		if (map.get(userId) == null) {
			// ���������ݿ���
		} else {
			map.get(userId).write(o);
		}
	}

	// �Ƴ�session
	public void deleteSession(long userId) {
		map.remove(userId);
	}
}
