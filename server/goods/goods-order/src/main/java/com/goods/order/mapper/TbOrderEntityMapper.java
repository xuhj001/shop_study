package com.goods.order.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.goods.order.pojo.OrderItem;


public interface TbOrderEntityMapper {
	//卖家
	List<OrderItem> getItemList(long id);
	List<OrderItem> getItemListByType(@Param("id")long id,@Param("type")int type);
	//买家
	List<OrderItem> getUserItem(@Param("userId")long userId,@Param("page")long page,@Param("rows")long rows);
	List<OrderItem> getUserItemByType(@Param("userId")long userId,@Param("type")int type,@Param("page")long page,@Param("rows")long rows);

	void deleteOrder(long orderId);
}
