package com.goods.comments.service;

import javax.servlet.http.HttpServletRequest;

import com.goods.manager.pojo.TbComments;
import com.goods.manager.pojo.TbCommentsReply;
import com.goods.tools.common.util.TaotaoResult;

public interface CommentService {
	TaotaoResult getGoodsComments(long itemId);//获取对商品的评论
	TaotaoResult createComments(final TbComments tbComments,long orderId);//创建评论
	TaotaoResult createReply(final TbCommentsReply tbCommentsReply,Integer type);//创建回复
	TaotaoResult deleteReply(long id);//删除评论(回复表)
	TaotaoResult getComments(long itemId,long page,long rows);//根据商品id获取评论
}
