package com.goods.manager.service;

import java.util.List;

import com.goods.tools.commom.pojo.EUTreeNode;

//��Ʒ��Ŀ
public interface CatService {
	List<EUTreeNode> getCatList(long parentId);
}
