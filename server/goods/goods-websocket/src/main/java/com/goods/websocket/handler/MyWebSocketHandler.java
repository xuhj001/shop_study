package com.goods.websocket.handler;

import static org.hamcrest.CoreMatchers.endsWith;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.goods.manager.pojo.TbMessage;
import com.goods.message.service.WebSocketService;
import com.goods.mina.session.SessionManager;
import com.goods.tools.common.util.JsonUtils;

public class MyWebSocketHandler extends TextWebSocketHandler {
	@Autowired
	private WebSocketService webSocketService;

	public static final Map<String, WebSocketSession> userSocketSessionMap;

	static {
		userSocketSessionMap = new HashMap<String, WebSocketSession>();
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		super.handleTextMessage(session, message);
		// Long userId = Long.parseLong(message.getPayload());// �û�id
		System.out.println(message.getPayload());
		// ��ͷ��˷�����Ϣ
		String[] meStrings = message.getPayload().split(",");
		String mString = null;
		if (meStrings[1].equals("�ظ�")) {
			mString = "������Ϣ";
		} else {
			mString = "������Ϣ" + "," + meStrings[1];
		}
		SessionManager.getInstance().writeToClient(mString, Long.parseLong(meStrings[0]));
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {

		if (session.getAttributes().get("userId") != null) {
			// ��ȡ�û�key
			String key = (String) session.getAttributes().get("userId");
			if (userSocketSessionMap.get(key) == null) {
				userSocketSessionMap.put(key, session);
				TbMessage message = webSocketService.get(Long.parseLong(key));
				// �״η��ʿ����Ƿ��������ݱ��������ݿ���
				if (message != null) {
					// ��������
					// ��װ��json��ʽ�������ڷ���
					String json = JsonUtils.objectToJson(message);
					session.sendMessage(new TextMessage(json));
					// ɾ����¼
					webSocketService.delete(Long.parseLong(key));
				}
			}
		}
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		if (session.isOpen()) {
			session.close();
		}
		System.out.println("����:"+exception.getMessage());
		Iterator<Entry<String, WebSocketSession>> it = userSocketSessionMap.entrySet().iterator();
		// �Ƴ�Socket�Ự
		while (it.hasNext()) {
			Entry<String, WebSocketSession> entry = it.next();
			if (entry.getValue().getId().equals(session.getId())) {
				userSocketSessionMap.remove(entry.getKey());
				System.out.println("Socket�Ự�Ѿ��Ƴ�:�û�ID" + entry.getKey());
				break;
			}
		}
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		System.out.println("Websocket:" + session.getId() + "�Ѿ��ر�");
		Iterator<Entry<String, WebSocketSession>> it = userSocketSessionMap.entrySet().iterator();
		// �Ƴ�Socket�Ự
		while (it.hasNext()) {
			Entry<String, WebSocketSession> entry = it.next();
			if (entry.getValue().getId().equals(session.getId())) {
				userSocketSessionMap.remove(entry.getKey());
				System.out.println("Socket�Ự�Ѿ��Ƴ�:�û�ID" + entry.getKey());
				break;
			}
		}
	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}

	// ����
	public void broadcaseTopersonal(final TextMessage message, final String userId) throws IOException {
		if (userSocketSessionMap.get(userId) != null) {
			final WebSocketSession session = userSocketSessionMap.get(userId);
			if (session.isOpen()) {
				new Thread(new Runnable() {

					public void run() {
						// ����û�������Ϣ
						try {
							session.sendMessage(message);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}).start();
			}
		} else {
			// �û�û�н�������
			// ����Ϣ������ݿ��У��Ա�����ʱ��������
			TbMessage tbm = webSocketService.get(Long.parseLong(userId));
			if (tbm != null) {
				// ����
				tbm.setCount(tbm.getCount() + 1);
				if (message.getPayload().equals("����")) {
					// ���ö���
					if (tbm.getOrdernum() == null) {
						tbm.setOrdernum(1);
					} else {
						tbm.setOrdernum(tbm.getOrdernum() + 1);
					}
				} else if (message.getPayload().equals("����")) {
					// ��������
					if (tbm.getCommentsnum() == null) {
						tbm.setCommentsnum(1);
					} else {
						tbm.setCommentsnum(tbm.getCommentsnum() + 1);
					}
				} else {
					// ���ûظ�
					if (tbm.getReplynum() == null) {
						tbm.setReplynum(1);
					} else {
						tbm.setReplynum(tbm.getReplynum() + 1);
					}
				}
				webSocketService.update(tbm);
			} else {
				// ����
				TbMessage tbMessage = new TbMessage();
				tbMessage.setMuserid(Long.parseLong(userId));
				if (tbMessage.getCount() == null) {
					tbMessage.setCount(1);
				} else {
					tbMessage.setCount(tbMessage.getCount() + 1);// ����������
				}
				if (message.getPayload().equals("����")) {
					// ���ö���
					tbMessage.setOrdernum(1);
				} else if (message.getPayload().equals("����")) {
					// ��������
					tbMessage.setCommentsnum(1);
				} else {
					// ���ûظ�
					tbMessage.setReplynum(1);
				}
				webSocketService.save(tbMessage);// ���浽���ݿ��У���������ʱ��ʾ
			}

		}
	}

	// Ⱥ��
	public void broadcast(final TextMessage message) throws IOException {
		Iterator<Entry<String, WebSocketSession>> it = userSocketSessionMap.entrySet().iterator();

		// ���߳�Ⱥ��
		while (it.hasNext()) {

			final Entry<String, WebSocketSession> entry = it.next();

			if (entry.getValue().isOpen()) {
				new Thread(new Runnable() {

					public void run() {
						try {
							if (entry.getValue().isOpen()) {
								entry.getValue().sendMessage(message);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

				}).start();
			}

		}
	}

	/**
	 * �����������û���ʵʱ���̼��ҳ�淢����Ϣ
	 * 
	 * @param message
	 * @throws IOException
	 */
	public void sendMessageToJsp(final TextMessage message, String type) throws IOException {
		Iterator<Entry<String, WebSocketSession>> it = userSocketSessionMap.entrySet().iterator();

		// ���߳�Ⱥ��
		while (it.hasNext()) {

			final Entry<String, WebSocketSession> entry = it.next();
			if (entry.getValue().isOpen() && entry.getKey().contains(type)) {
				new Thread(new Runnable() {

					public void run() {
						try {
							if (entry.getValue().isOpen()) {
								entry.getValue().sendMessage(message);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

				}).start();
			}

		}
	}
}
