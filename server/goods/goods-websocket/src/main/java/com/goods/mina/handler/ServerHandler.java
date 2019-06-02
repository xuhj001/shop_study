package com.goods.mina.handler;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.goods.mina.session.SessionManager;

public class ServerHandler extends IoHandlerAdapter {
	@Override
	public void sessionCreated(IoSession session) throws Exception {
		super.sessionCreated(session);
		System.out.println("sessionCreated");
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		super.sessionOpened(session);
		System.out.println("sessionOpened");
	}

	// ��Ϣ����
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		super.messageReceived(session, message);
		System.out.println("mina:" + message.toString());
		// �ͻ������ӷ���˺󷵻�һ��userId,�����������ض��ͻ��˷�����Ϣ
		// ����session
		String id = (String) message;
		Long userId = Long.parseLong(id);
		SessionManager.getInstance().setSession(session, userId);// ����
	}

	// ��Ϣ����
	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		super.messageSent(session, message);

	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		super.sessionClosed(session);
		System.out.println("sessionClosed");

	}
}
