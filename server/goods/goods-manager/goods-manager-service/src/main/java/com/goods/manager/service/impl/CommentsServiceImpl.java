package com.goods.manager.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.goods.manager.mapper.TbCommentsMapper;
import com.goods.manager.pojo.CommentsResult;
import com.goods.manager.pojo.TbComments;
import com.goods.manager.pojo.TbCommentsExample;
import com.goods.manager.pojo.TbCommentsExample.Criteria;
import com.goods.manager.pojo.TbCommentsReply;
import com.goods.manager.pojo.TbCommentsResult;
import com.goods.manager.pojo.TbUser;
import com.goods.manager.service.CommentsService;
import com.goods.tools.commom.pojo.CommentsEntity;
import com.goods.tools.commom.pojo.GoodsListItem;
import com.goods.tools.common.util.HttpClientUtil;
import com.goods.tools.common.util.JsonUtils;
import com.goods.tools.common.util.TaotaoResult;

//评论管理
@Service
public class CommentsServiceImpl implements CommentsService {

	@Value("${COMMENTS_BASE_URL}")
	private String COMMENTS_BASE_URL;
	@Value("${COMMENTS_SHOW}")
	private String COMMENTS_SHOW;
	@Value("${COMMENTS_CREATE_REPLY}")
	private String COMMENTS_CREATE_REPLY;
	@Value("${COMMENTS_DELETE_REPLY}")
	private String COMMENTS_DELETE_REPLY;
	@Autowired
	private TbCommentsMapper tbCommentsMapper;

	// 调用评论服务
	public TaotaoResult getCommentsEntity(long page, long rows, HttpServletRequest request) {
		try {
			TbUser tbUser = (TbUser) request.getAttribute("user");
			Long userId = tbUser.getId();
			Map<String, String> param = new HashMap<String, String>();
			param.put("userId", String.valueOf(userId));
			String result = HttpClientUtil.doGet(COMMENTS_BASE_URL + COMMENTS_SHOW, param);
			TaotaoResult taotaoResult = TaotaoResult.format(result);
			if (taotaoResult.getStatus() == 200) {
				return taotaoResult;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return TaotaoResult.build(400, "暂无评论");
	}

	// 创建回复
	public TaotaoResult createComments(TbCommentsReply tbCommentsReply, HttpServletRequest request) {
		try {

			// 调用评论服务创建回复
			// 补全属性
			TbUser user = (TbUser) request.getAttribute("user");
			Long id = user.getId();
			tbCommentsReply.setMuserid(id);
			tbCommentsReply.setUserid(id);// 设置自己的id
			tbCommentsReply.setName(user.getUsername());// 设置自己的昵称
			tbCommentsReply.setReplytime(new Date());// 创建回复时间
			String result = HttpClientUtil.doPostJson(COMMENTS_BASE_URL + COMMENTS_CREATE_REPLY,
					JsonUtils.objectToJson(tbCommentsReply));
			TaotaoResult taotaoResult = TaotaoResult.format(result);
			if (taotaoResult.getStatus() == 200) {
				return TaotaoResult.ok();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return TaotaoResult.build(400, "回复失败，请稍后重试");
	}

	// 删除评论
	public TaotaoResult deleteComments(long id) {
		try {
			// 调用评论服务进行删除评论(注意这里只删除回复的评论)
			Map<String, String> param = new HashMap<String, String>();
			param.put("id", id + "");
			String result = HttpClientUtil.doGet(COMMENTS_BASE_URL + COMMENTS_DELETE_REPLY, param);
			TaotaoResult taotaoResult = TaotaoResult.format(result);
			if (taotaoResult.getStatus() == 200) {
				return taotaoResult;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return TaotaoResult.build(400, "删除失败，请稍后重试");
	}

}
