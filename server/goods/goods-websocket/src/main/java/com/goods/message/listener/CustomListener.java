package com.goods.message.listener;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;

import com.goods.websocket.handler.MyWebSocketHandler;

public class CustomListener implements MessageListener {

	@Autowired
	private MyWebSocketHandler webSocketHandler;

	public void onMessage(Message message) {
		ActiveMQTextMessage activeMQTextMessage = (ActiveMQTextMessage) message;
		// 0��ʾ�����ۣ�1��ʾ�ظ���2��ʾ�¶���
		try {
			TextMessage textMessage = null;
			String text = activeMQTextMessage.getText();
			// ��ȡ������id
			String[] userId = text.split(":");
			if (userId[1].equals("0")) {
				textMessage = new TextMessage("����");
			} else if (userId[1].equals("1")) {
				textMessage = new TextMessage("�ظ�");
			} else {
				textMessage = new TextMessage("����");
			}
			try {
				webSocketHandler.broadcaseTopersonal(textMessage, userId[0]);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (JMSException e1) {
			e1.printStackTrace();
		}

	}

}
