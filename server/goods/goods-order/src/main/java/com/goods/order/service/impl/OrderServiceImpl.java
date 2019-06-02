package com.goods.order.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.goods.manager.mapper.TbDatasMapper;
import com.goods.manager.mapper.TbGoodsRankMapper;
import com.goods.manager.mapper.TbItemCatMapper;
import com.goods.manager.mapper.TbItemMapper;
import com.goods.manager.mapper.TbOrderFinishMapper;
import com.goods.manager.mapper.TbOrderItemMapper;
import com.goods.manager.mapper.TbOrderMapper;
import com.goods.manager.mapper.TbOrderMsgMapper;
import com.goods.manager.mapper.TbOrderShippingMapper;
import com.goods.manager.pojo.TbDatas;
import com.goods.manager.pojo.TbGoodsRank;
import com.goods.manager.pojo.TbGoodsRankExample;
import com.goods.manager.pojo.TbGoodsRankExample.Criteria;
import com.goods.manager.pojo.TbItem;
import com.goods.manager.pojo.TbItemCatExample;
import com.goods.manager.pojo.TbOrder;
import com.goods.manager.pojo.TbOrderExample;
import com.goods.manager.pojo.TbOrderFinish;
import com.goods.manager.pojo.TbOrderItem;
import com.goods.manager.pojo.TbOrderItemExample;
import com.goods.manager.pojo.TbOrderMsg;
import com.goods.manager.pojo.TbOrderMsgExample;
import com.goods.manager.pojo.TbOrderShipping;
import com.goods.manager.pojo.TbUser;
import com.goods.order.dao.JedisClient;
import com.goods.order.mapper.TbOrderEntityMapper;
import com.goods.order.pojo.Order;
import com.goods.order.pojo.OrderItem;
import com.goods.order.service.OrderService;
import com.goods.tools.commom.pojo.GoodsListItem;
import com.goods.tools.common.util.ExceptionUtil;
import com.goods.tools.common.util.JsonUtils;
import com.goods.tools.common.util.TaotaoResult;

@Service
public class OrderServiceImpl implements OrderService {
	@Value("${ORDER_GEN_KEY}")
	private String ORDER_GEN_KEY;// redis中的key
	@Value("${ORDER_INIT_ID}")
	private String ORDER_INIT_ID;// 初始化订单id开始值
	@Value("${ORDER_DETAIL_GEN_KEY}")
	private String ORDER_DETAIL_GEN_KEY;
	@Autowired
	private JedisClient jedisClient;
	@Autowired
	private TbOrderMapper tbOrderMapper;
	@Autowired
	private TbOrderEntityMapper tbOrderEntityMapper;
	@Autowired
	private TbGoodsRankMapper tbGoodsRankMapper;

	@Autowired
	private TbOrderItemMapper tbOrderItemMapper;// 订单中的商品
	@Autowired
	private TbOrderShippingMapper tbOrderShippingMapper;// 邮寄地址

	@Autowired
	private TbItemMapper tbItemMapper;

	@Autowired
	private TbOrderFinishMapper tbOrderFinishMapper;

	@Autowired
	private JmsTemplate jmsTemplate;// activemq

	@Autowired
	private TbGoodsRankMapper tbGoodsRankExample;

	@Autowired
	private TbOrderMsgMapper tbOrderMsgMapper;// 保存消息，方便用户查看消息

	// 数据展示表
	@Autowired
	private TbDatasMapper tbDatasMapper;

	// 展示与该卖家有关的所有订单
	// 参数type表示未处理或者交易完成
	public TaotaoResult showorder(long itemId, long page, long rows, Integer type) {

		try {
			GoodsListItem goodsListItem = new GoodsListItem();
			List<OrderItem> list = null;
			if (type != null) {
				list = tbOrderEntityMapper.getItemListByType(itemId, type);
			} else {
				list = tbOrderEntityMapper.getItemList(itemId);
			}
			PageHelper.startPage((int) page, (int) rows);
			// 查找订单中商品的信息
			if (list != null && list.size() != 0) {
				goodsListItem.setRows(list);
				goodsListItem.setTotal((int) new PageInfo<OrderItem>(list).getTotal());
				String json = JsonUtils.objectToJson(goodsListItem);
				return TaotaoResult.ok(json);
			}
			return TaotaoResult.build(400, "暂无订单");

		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}

	// 展示买家的订单
	public TaotaoResult showUserOrder(long itemId, long page, long rows, Integer type) {
		try {
			GoodsListItem goodsListItem = new GoodsListItem();
			List<OrderItem> list = null;
			if (type != 1) {
				list = tbOrderEntityMapper.getUserItemByType(itemId, type, (page - 1) * rows, type);// 根据订单状态获取订单情况
			} else {
				list = tbOrderEntityMapper.getUserItem(itemId, (page - 1) * rows, rows);// 显示全部订单
			}
			// 查找订单中商品的信息
			if (list != null && list.size() != 0) {
				goodsListItem.setRows(list);
				goodsListItem.setTotal(list.size());
				String json = JsonUtils.objectToJson(goodsListItem);
				return TaotaoResult.ok(json);
			}
			return TaotaoResult.build(400, "暂无订单");

		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}

	// 创建订单
	public TaotaoResult createorder(TbOrder tbOrder, TbOrderItem tbOrderItem, TbOrderShipping tbOrderShipping) {
		try {
			// 利用redis创建无重复的订单id
			String string = jedisClient.get(ORDER_GEN_KEY);
			if (StringUtils.isEmpty(string)) {
				jedisClient.set(ORDER_GEN_KEY, ORDER_INIT_ID);
			}
			long orderId = jedisClient.incr(ORDER_GEN_KEY);
			tbOrder.setTouser(0);
			tbOrder.setOrderId(orderId + "");
			tbOrder.setCreateTime(new Date());
			tbOrder.setUpdateTime(new Date());
			// 状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
			tbOrder.setStatus(2);
			tbOrder.setPaymentTime(new Date());// 支付时间
			tbOrder.setConsignTime(new DateTime().plusDays(1).toDate());// 发货时间暂定为一天后
			tbOrderMapper.insert(tbOrder);

			// 插入数据

			// 设置自己的id
			// 同样利用redis
			long orderDetailId = jedisClient.incr(ORDER_DETAIL_GEN_KEY);
			tbOrderItem.setId(orderDetailId + "");
			tbOrderItem.setOrderId(orderId + "");
			tbOrderItemMapper.insert(tbOrderItem);

			// 插入邮编地址ַ
			tbOrderShipping.setOrderId(orderId + "");// 设置订单id
			tbOrderShipping.setCreated(new Date());
			tbOrderShipping.setUpdated(new Date());
			tbOrderShippingMapper.insert(tbOrderShipping);

			// 修改商品的数量
			int buynum = tbOrderItem.getNum();
			// 获取该商品
			TbItem tbItem = tbItemMapper.selectByPrimaryKey(Long.parseLong(tbOrderItem.getItemId()));
			if (tbItem != null)
				tbItem.setNum(tbItem.getNum() - buynum);
			tbItemMapper.updateByPrimaryKey(tbItem);

			try {
				// 获取卖家id
				final long muserId = tbOrder.getMuserId();
				// 创建成功之后利用activemq发送信息给manager管理后台，以实现实时通知
				jmsTemplate.send(new MessageCreator() {

					public Message createMessage(Session arg0) throws JMSException {
						return arg0.createTextMessage(muserId + ":2");
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
			return TaotaoResult.ok();

		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, "创建订单失败");
		}
	}

	// 获取排行榜的数据
	public TbGoodsRank getTbGoodsRank(String name, long orderId, long userId) {
		// 通过id获取
		TbGoodsRankExample tbGoodsRankExample = new TbGoodsRankExample();
		Criteria criteria = tbGoodsRankExample.createCriteria();
		criteria.andGoodnameEqualTo(name);
		criteria.andUseridEqualTo(userId);
		List<TbGoodsRank> list = tbGoodsRankMapper.selectByExample(tbGoodsRankExample);
		if (list != null && list.size() != 0)
			return list.get(0);
		return null;
	}

	public TbOrderItem geTbOrderItem(String orderId) {
		TbOrderItemExample tbOrderItemExample = new TbOrderItemExample();
		com.goods.manager.pojo.TbOrderItemExample.Criteria criteria = tbOrderItemExample.createCriteria();
		criteria.andOrderIdEqualTo(orderId);
		List<TbOrderItem> list = tbOrderItemMapper.selectByExample(tbOrderItemExample);
		TbOrderItem tbOrderItem = null;
		if (list != null && list.size() != 0) {
			return tbOrderItem = list.get(0);
		}
		return null;
	}

	public TbOrderMsg geTbOrderMsg(long orderId) {
		TbOrderMsgExample tbOrderMsgExample = new TbOrderMsgExample();
		com.goods.manager.pojo.TbOrderMsgExample.Criteria criteria = tbOrderMsgExample.createCriteria();
		criteria.andOrderidEqualTo(orderId);
		List<TbOrderMsg> list = tbOrderMsgMapper.selectByExample(tbOrderMsgExample);
		if (list != null && list.size() != 0) {
			return list.get(0);
		}
		return null;
	}

	// 编辑订单
	public TaotaoResult editorder(TbOrder tbOrder) {
		try {
			// 更新数据
			if (tbOrder != null) {
				// 更新时间
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date date = new Date();
				tbOrder.setUpdateTime(date);
				// 如果是交易完成或交易关闭，还有设置这两个时间
				String time = null;// 保存完成时间或保存关闭时间
				if (tbOrder.getStatus() == 3) {
					// 接受订单
					// 发货之后保存该消息，方便用户查看
					// 新增
					// 获取订单中的商品信息
					try {
						TbOrderMsg tbOrderMsg = new TbOrderMsg();
						tbOrderMsg.setMsg(new String("订单接受".getBytes("gbk"), "utf-8"));
						tbOrderMsg.setMuserid(tbOrder.getMuserId());
						tbOrderMsg.setTime(new Date());
						tbOrderMsg.setUserid(tbOrder.getUserId());
						tbOrderMsg.setOrderid(Long.parseLong(tbOrder.getOrderId()));
						TbOrderItem tbOrderItem = geTbOrderItem(tbOrder.getOrderId());
						if (tbOrderItem != null) {
							tbOrderMsg.setTitle(tbOrderItem.getTitle());
							tbOrderMsg.setPic(tbOrderItem.getPicPath().split(",")[0]);
						}
						// 保存
						tbOrderMsgMapper.insert(tbOrderMsg);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (tbOrder.getStatus() == 4) {
					// 发货
					Date sendtime = new Date();
					tbOrder.setConsignTime(sendtime);
					time = simpleDateFormat.format(sendtime);

					// 修改消息
					TbOrderMsg tbOrderMsg = geTbOrderMsg(Long.parseLong(tbOrder.getOrderId()));
					tbOrderMsg.setMsg(new String("订单发货".getBytes("gbk"), "utf-8"));
					tbOrderMsg.setLogisticsid(Long.parseLong(tbOrder.getShippingCode()));
					tbOrderMsg.setCompany(tbOrder.getShippingName());

					// 更新
					tbOrderMsgMapper.updateByPrimaryKey(tbOrderMsg);
				} else if (tbOrder.getStatus() == 5) {
					// 交易完成
					Date finishtime = new Date();
					tbOrder.setEndTime(finishtime);
					time = simpleDateFormat.format(finishtime);
					// 设置货物排行榜
					TbGoodsRank rank = getTbGoodsRank(tbOrder.getGoodcidname()==null?"其他":tbOrder.getGoodcidname(), Long.parseLong(tbOrder.getOrderId()),
							tbOrder.getMuserId());
					TbOrderItemExample tbOrderItemExample = new TbOrderItemExample();
					com.goods.manager.pojo.TbOrderItemExample.Criteria criteria = tbOrderItemExample.createCriteria();
					criteria.andOrderIdEqualTo(tbOrder.getOrderId());
					TbOrderItem orderItem = tbOrderItemMapper.selectByExample(tbOrderItemExample).get(0);
					if (rank == null) {
						// 插入新数据
						try {
							TbGoodsRank tbGoodsRank = new TbGoodsRank();
							tbGoodsRank.setGoodname(tbOrder.getGoodcidname()==null?"其他":tbOrder.getGoodcidname());
							tbGoodsRank.setOrdernum((long) 1);
							tbGoodsRank.setUserid(tbOrder.getMuserId());
							if (orderItem != null) {
								tbGoodsRank.setIncome(orderItem.getTotalFee());
							}
							tbGoodsRankMapper.insert(tbGoodsRank);
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						// 修改订单数量
						try {
							rank.setOrdernum(rank.getOrdernum() + 1);
							if (orderItem != null) {
								rank.setIncome(rank.getIncome() + orderItem.getTotalFee());
							}
							tbGoodsRankMapper.updateByPrimaryKey(rank);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				} else if (tbOrder.getStatus() == 6) {
					// 交易关闭
					Date closetime = new Date();
					tbOrder.setCloseTime(closetime);
					time = simpleDateFormat.format(closetime);
				}
				tbOrderMapper.updateByPrimaryKey(tbOrder);
				return TaotaoResult.ok(simpleDateFormat.format(date) + (time == null ? "" : "," + time));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return TaotaoResult.build(400, "编辑失败");
	}

	// 删除订单
	public TaotaoResult deleteorder(long itemId) {
		try {
			TbOrder tbOrder = tbOrderMapper.selectByPrimaryKey(itemId + "");
			if (tbOrder != null) {
				tbOrder.setTouser(1);// 设置对用户不可见
				tbOrderMapper.updateByPrimaryKey(tbOrder);
			}
			return TaotaoResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, "删除失败");
		}
	}

	// 根据id获取某一个订单
	public TbOrder getOrder(long itemId) {
		try {

			TbOrder tbOrder = tbOrderMapper.selectByPrimaryKey(itemId + "");
			return tbOrder;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 展示已经完成交易的订单
	public TaotaoResult showgoodssold(long itemId) {
		try {
			List<TbOrderFinish> list = tbOrderFinishMapper.getAllItem(itemId);
			if (list != null && list.size() != 0) {
				String json = JsonUtils.objectToJson(list);
				return TaotaoResult.ok(json);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return TaotaoResult.build(400, "暂无完成订单");
	}

}
