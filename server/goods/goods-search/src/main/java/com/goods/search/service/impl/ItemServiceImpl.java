package com.goods.search.service.impl;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.goods.search.mapper.ItemMapper;
import com.goods.search.pojo.Item;
import com.goods.search.service.ItemService;
import com.goods.tools.common.util.ExceptionUtil;
import com.goods.tools.common.util.TaotaoResult;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private ItemMapper itemMapper;

	@Autowired
	private SolrServer solrServer;

	public TaotaoResult importAllItems() {
		try {

			// 从数据库中获取数据
			List<Item> list = itemMapper.getItemList();
			for (Item item : list) {

				SolrInputDocument document = new SolrInputDocument();
				document.setField("id", item.getId());
				document.setField("item_title", item.getTitle());
				document.setField("item_sellPoint", item.getSellPoint());
				document.setField("item_price", item.getPrice());
				document.setField("item_image", item.getImage());
				document.setField("item_cid", item.getCidname());
				document.setField("item_desc", item.getItem_des());
				document.setField("item_num", item.getNum());
				document.setField("item_status", item.getStatus());
				document.setField("item_muserid", item.getMuser_id());
				// 加入
				solrServer.add(document);
			}
			// 提交
			solrServer.commit();
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
		return TaotaoResult.ok();
	}

	// 上架商品
	public TaotaoResult addItem(long itemId) {
		try {
			// 搜索该商品
			Item item = itemMapper.selectByPrimaryKey(itemId);
			SolrInputDocument document = new SolrInputDocument();
			document.setField("id", item.getId());
			document.setField("item_title", item.getTitle());
			document.setField("item_sellPoint", item.getSellPoint());
			document.setField("item_price", item.getPrice());
			document.setField("item_image", item.getImage());
			document.setField("item_cid", item.getCidname());
			document.setField("item_desc", item.getItem_des());
			document.setField("item_num", item.getNum());
			document.setField("item_status", item.getStatus());
			document.setField("item_muserid", item.getMuser_id());
			// 加入
			solrServer.add(document);
			// 提交
			solrServer.commit();
			return TaotaoResult.ok();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return TaotaoResult.build(400, "上架失败");
	}

	// 下架商品
	public TaotaoResult removeItem(long itemId) {
		try {
			solrServer.deleteByQuery("id:" + itemId);
			solrServer.commit();
			return TaotaoResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return TaotaoResult.build(400, "下架失败");
	}

}
