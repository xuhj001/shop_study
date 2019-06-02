package com.goods.comments.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.servlet.http.HttpServletRequest;

import org.apache.zookeeper.server.FinalRequestProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.goods.comments.pojo.CommentsResult;
import com.goods.comments.service.CommentService;
import com.goods.manager.mapper.TbCommentsMapper;
import com.goods.manager.mapper.TbCommentsMsgMapper;
import com.goods.manager.mapper.TbCommentsReplyMapper;
import com.goods.manager.pojo.TbComments;
import com.goods.manager.pojo.TbCommentsExample;
import com.goods.manager.pojo.TbCommentsExample.Criteria;
import com.goods.manager.pojo.TbCommentsMsg;
import com.goods.manager.pojo.TbCommentsReply;
import com.goods.manager.pojo.TbCommentsReplyExample;
import com.goods.manager.pojo.TbCommentsResult;
import com.goods.manager.pojo.TbOrderFinish;
import com.goods.tools.common.util.HttpClientUtil;
import com.goods.tools.common.util.JsonUtils;
import com.goods.tools.common.util.TaotaoResult;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private TbCommentsMapper tbCommentsMapper;
	@Autowired
	private TbCommentsReplyMapper tbCommentsReplyMapper;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Autowired
	private TbCommentsMsgMapper tbCommentsMsgMapper;
	@Value("${ORDER_BASE_URL}")
	private String ORDER_BASE_URL;
	@Value("${ORDER_SHOW_SOLD_URL}")
	private String ORDER_SHOW_SOLD_URL;
	@Value("${ORDER_EDIT_URL}")
	private String ORDER_EDIT_URL;

	public TaotaoResult getGoodsComments(long userId) {
		try {
			Map<String, Long> map = new HashMap<String, Long>();// 存放id，用于判断是否存在
			// 调用order服务查询已经完成交易的商品
			Map<String, String> param = new HashMap<String, String>();
			param.put("itemId", userId + "");
			String get = HttpClientUtil.doGet(ORDER_BASE_URL + ORDER_SHOW_SOLD_URL, param);

			TaotaoResult taotaoResult = TaotaoResult.format(get);
			if (taotaoResult.getStatus() == 200) {
				List<CommentsResult> results = new ArrayList<CommentsResult>();// 存放返回结果

				String json = (String) taotaoResult.getData();// 完成交易的订单，即已经评论过的商品
				List<TbOrderFinish> list = JsonUtils.jsonToList(json, TbOrderFinish.class);
				for (int i = 0; i < list.size(); i++) {
					TbOrderFinish tbOrderFinish = list.get(i);
					CommentsResult commentsResult = new CommentsResult();

					// 设置商品信息
					String item_id = tbOrderFinish.getItem_id();// 获取商品id
					if (map.get(item_id) != null)
						continue;// 该商品已经查询过了

					// 设置商品信息
					commentsResult.setItemId(Long.parseLong(item_id));// 设置商品id
					commentsResult.setItemPic(tbOrderFinish.getPic_path());// 设置商品图片
					commentsResult.setTitle(tbOrderFinish.getTitle());// 商品名称
					// 每个商品对应0到多条评论
					// 搜索该商品的第一条评论
					TbCommentsExample tbCommentsExample = new TbCommentsExample();
					tbCommentsExample.setOrderByClause("time desc");// 按时间进行搜索
					Criteria criteria = tbCommentsExample.createCriteria();
					criteria.andItemidEqualTo(Long.parseLong(item_id));// 根据商品id进行搜索
					List<TbCommentsResult> comments = tbCommentsMapper.selectByExample(tbCommentsExample);
					if (comments != null && comments.size() != 0) {
						for (TbCommentsResult tbComment : comments) {
							// 每条评论中有0到多条回复
							TbCommentsReplyExample tbCommentsReplyExample = new TbCommentsReplyExample();
							tbCommentsReplyExample.setOrderByClause("replytime asc");
							com.goods.manager.pojo.TbCommentsReplyExample.Criteria commentscriteria = tbCommentsReplyExample
									.createCriteria();
							// 查找条件
							commentscriteria.andItemIdEqualTo(Long.parseLong(item_id));
							commentscriteria.andParentidEqualTo(tbComment.getId());
							List<TbCommentsReply> replylist = tbCommentsReplyMapper
									.selectByExample(tbCommentsReplyExample);
							if (replylist != null && replylist.size() != 0) {
								tbComment.setReplylist(replylist);
							} else {
								tbComment.setReplylist(new ArrayList<TbCommentsReply>());
							}
						}
						commentsResult.setUserlist(comments);// 设置第一条评论
					} else {
						commentsResult.setUserlist(new ArrayList<TbCommentsResult>());
					}
					map.put(item_id, Long.parseLong(item_id));// 防止重复筛选到商品

					results.add(commentsResult);
				}
				String result = JsonUtils.objectToJson(results);
				return TaotaoResult.ok(result);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return TaotaoResult.build(400, "暂无评论");
	}

	// 创建第一条评论
	public TaotaoResult createComments(final TbComments tbComments, long orderId) {
		try {
			// 补全
			tbComments.setCreated(new Date());
			tbComments.setTime(new Date());
			tbComments.setUpdated(new Date());
			tbCommentsMapper.insert(tbComments);

			// 修改订单状态为交易完成
			Map<String, String> map = new HashMap<String, String>();
			map.put("itemId", orderId + "");
			map.put("status", "5");
			HttpClientUtil.doGet(ORDER_BASE_URL + ORDER_EDIT_URL, map);

			// 通知websocket服务
			jmsTemplate.send(new MessageCreator() {

				public Message createMessage(Session arg0) throws JMSException {
					return arg0.createTextMessage(tbComments.getSoldid() + ":0");
				}
			});
			return TaotaoResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return TaotaoResult.build(400, "评论失败，请稍后重试");
	}

	// 创建回复
	public TaotaoResult createReply(final TbCommentsReply tbCommentsReply, Integer type) {
		try {
			// 补全
			tbCommentsReply.setReplytime(new Date());
			tbCommentsReply.setUpdated(new Date());
			tbCommentsReplyMapper.insert(tbCommentsReply);
			switch (type) {
			case 1:
				// 买家回复卖家
				// 通知websocket服务
				jmsTemplate.send(new MessageCreator() {
					public Message createMessage(Session arg0) throws JMSException {
						return arg0.createTextMessage(tbCommentsReply.getMuserid() + ":1");
					}
				});
				break;
			default:
				// 买家回复买家
				// 卖家回复买家
				// 保存消息
				try {
					TbCommentsMsg tbCommentsMsg = new TbCommentsMsg();
					tbCommentsMsg.setComment(tbCommentsReply.getReplycomments());
					tbCommentsMsg.setItemid(tbCommentsReply.getItemId());
					tbCommentsMsg.setPic(tbCommentsReply.getPic());
					tbCommentsMsg.setTime(new Date());
					tbCommentsMsg.setUserid(tbCommentsReply.getReplyid());
					tbCommentsMsg.setUsername(tbCommentsReply.getName());
					tbCommentsMsg.setMuserid(tbCommentsReply.getMuserid());
					TbComments tbComments = tbCommentsMapper.selectByPrimaryKey(tbCommentsReply.getParentid());
					if (tbComments != null) {
						tbCommentsMsg.setSelfcomment(tbComments.getComments());
					}

					// 保存
					tbCommentsMsgMapper.insert(tbCommentsMsg);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}

			return TaotaoResult.ok();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return TaotaoResult.build(400, "评论失败，请稍后重试");
	}

	// 删除评论
	public TaotaoResult deleteReply(long id) {
		try {
			tbCommentsReplyMapper.deleteByPrimaryKey(id);
			return TaotaoResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return TaotaoResult.build(400, "删除失败，请稍后重试");
	}

	// 根据商品id获取评论
	public TaotaoResult getComments(long itemId, long page, long rows) {
		try {
			CommentsResult commentsResult = new CommentsResult();
			TbCommentsExample tbCommentsExample = new TbCommentsExample();
			tbCommentsExample.setOrderByClause("time desc");// 按时间进行搜索
			PageHelper.startPage((int) page, (int) rows);
			Criteria criteria = tbCommentsExample.createCriteria();
			criteria.andItemidEqualTo(itemId);// 根据商品id进行搜索
			List<TbCommentsResult> comments = tbCommentsMapper.selectByExample(tbCommentsExample);
			if (comments != null && comments.size() != 0) {
				for (TbCommentsResult tbComment : comments) {
					// 每条评论中有0到多条回复
					TbCommentsReplyExample tbCommentsReplyExample = new TbCommentsReplyExample();
					tbCommentsReplyExample.setOrderByClause("replytime asc");
					com.goods.manager.pojo.TbCommentsReplyExample.Criteria commentscriteria = tbCommentsReplyExample
							.createCriteria();
					// 查找条件
					commentscriteria.andItemIdEqualTo(itemId);
					commentscriteria.andParentidEqualTo(tbComment.getId());
					List<TbCommentsReply> replylist = tbCommentsReplyMapper.selectByExample(tbCommentsReplyExample);
					if (replylist != null && replylist.size() != 0) {
						tbComment.setReplylist(replylist);
					} else {
						tbComment.setReplylist(new ArrayList<TbCommentsReply>());
					}
				}
				commentsResult.setUserlist(comments);// 设置第一条评论
			} else {
				commentsResult.setUserlist(new ArrayList<TbCommentsResult>());
			}
			return TaotaoResult.ok(JsonUtils.objectToJson(commentsResult));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return TaotaoResult.build(400, "暂无评论");
	}

}
