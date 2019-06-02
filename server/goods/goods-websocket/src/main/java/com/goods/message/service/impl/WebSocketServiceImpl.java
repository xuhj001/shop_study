package com.goods.message.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.goods.manager.mapper.TbMessageMapper;
import com.goods.manager.pojo.TbMessage;
import com.goods.manager.pojo.TbMessageExample;
import com.goods.manager.pojo.TbMessageExample.Criteria;
import com.goods.message.service.WebSocketService;
import com.goods.tools.common.util.TaotaoResult;

@Service
public class WebSocketServiceImpl implements WebSocketService {

	@Autowired
	private TbMessageMapper tbMessageMapper;

	// ��������
	public TaotaoResult save(TbMessage tbMessage) {
		try {
			tbMessageMapper.insert(tbMessage);
			return TaotaoResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return TaotaoResult.build(400, "����ʧ�ܣ�����������");
	}

	// ��������
	public TbMessage get(Long userId) {
		try {
			TbMessageExample tbMessageExample = new TbMessageExample();
			Criteria createCriteria = tbMessageExample.createCriteria();
			createCriteria.andMuseridEqualTo(userId);
			List<TbMessage> list = tbMessageMapper.selectByExample(tbMessageExample);
			if (list != null && list.size() != 0) {
				return list.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// ɾ��
	public TaotaoResult delete(Long userId) {
		try {
			TbMessageExample tbMessageExample = new TbMessageExample();
			Criteria criteria = tbMessageExample.createCriteria();
			criteria.andMuseridEqualTo(userId);
			tbMessageMapper.deleteByExample(tbMessageExample);
			return TaotaoResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return TaotaoResult.build(400, "ɾ��ʧ�ܣ����Ժ�����");
	}

	// ��������
	public TaotaoResult update(TbMessage tbMessage) {
		try {
			tbMessageMapper.updateByPrimaryKey(tbMessage);
			return TaotaoResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return TaotaoResult.build(400, "����ʧ�ܣ����Ժ�����");
	}
}
