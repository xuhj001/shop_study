package com.goods.order.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.goods.manager.pojo.TbOrder;
import com.goods.order.pojo.Order;
import com.goods.order.service.OrderService;
import com.goods.tools.common.util.TaotaoResult;

@Controller
public class OrderController {
	@Autowired
	private OrderService orderService;

	// 显示订单
	@RequestMapping(value = "/showorder/{itemId}", method = RequestMethod.GET)
	@ResponseBody
	public TaotaoResult showorder(@PathVariable("itemId") long itemId,
			@RequestParam(value = "page", defaultValue = "1") long page,
			@RequestParam(value = "rows", defaultValue = "15") long rows,
			@RequestParam(value = "type", required = false) Integer type) {
		TaotaoResult taotaoResult = orderService.showorder(itemId, page, rows, type);
		return taotaoResult;
	}

	// 创建订单
	@RequestMapping(value = "/create/", method = RequestMethod.POST)
	@ResponseBody
	public TaotaoResult createorder(@RequestBody Order order) {
		TaotaoResult taotaoResult = orderService.createorder(order, order.getOrderItems(), order.getOrderShipping());
		return taotaoResult;
	}

	@ModelAttribute
	public void getModel(@RequestParam(value = "itemId", required = false) Long itemId,
			@RequestParam(value = "status", required = false) Integer status,
			@RequestParam(value = "shippingname", required = false) String shippingname,
			@RequestParam(value = "shippingcode", required = false) String shippingcode, Model model) {
		if (itemId != null && status != null) {
			// 找出该itemid对应的订单，修改状态
			TbOrder order = orderService.getOrder(itemId);
			if (order != null)
				order.setStatus(status);// 修改状态
			if (shippingname != null) {
				// 解决乱码情况
				try {
					shippingname = new String(shippingname.getBytes("iso-8859-1"), "utf-8");
					order.setShippingName(shippingname);// 物流名称
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			if (shippingcode != null) {
				order.setShippingCode(shippingcode);// 物流单号
			}
			model.addAttribute("tbOrder", order);
		}
	}

	// 修改订单的状态
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	@ResponseBody
	public TaotaoResult editorder(TbOrder tbOrder) {
		TaotaoResult taotaoResult = orderService.editorder(tbOrder);
		return taotaoResult;
	}

	// 显示已经完成的订单
	@RequestMapping("/sold")
	@ResponseBody
	public TaotaoResult showsold(long itemId) {
		TaotaoResult taotaoResult = orderService.showgoodssold(itemId);
		return taotaoResult;
	}

	// 显示某一个买家的订单情况
	@RequestMapping("/show/user/{userId}")
	@ResponseBody
	public TaotaoResult showUserOrder(@PathVariable("userId") long userId,
			@RequestParam(value = "page", defaultValue = "1") long page,
			@RequestParam(value = "rows", defaultValue = "15") long rows,
			@RequestParam(value = "type", required = false) Integer type) {
		TaotaoResult taotaoResult = orderService.showUserOrder(userId, page, rows, type);
		return taotaoResult;

	}
	
	//删除订单
	@RequestMapping("/delete/order/{orderId}")
	@ResponseBody
	public TaotaoResult deleteOrder(@PathVariable("orderId")long orderId){
		TaotaoResult taotaoResult = orderService.deleteorder(orderId);
		return taotaoResult;
	}
}
