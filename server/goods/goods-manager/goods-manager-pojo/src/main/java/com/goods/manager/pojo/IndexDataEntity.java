package com.goods.manager.pojo;

import java.util.List;

public class IndexDataEntity {
	private int ordernum;// ����������
	private int finish;// �����������
	private int close;// �����ر�����
	private long income;// ������
	private int lastmoneyorder;// ��һ���µĶ�����
	private List<TbCommentsResult> list;// �������۵�ǰʮ��
	private List<TbGoodsRank> ranks;// ������

	public List<TbGoodsRank> getRanks() {
		return ranks;
	}

	public void setRanks(List<TbGoodsRank> ranks) {
		this.ranks = ranks;
	}

	public int getLastmoneyorder() {
		return lastmoneyorder;
	}

	public void setLastmoneyorder(int lastmoneyorder) {
		this.lastmoneyorder = lastmoneyorder;
	}

	public int getOrdernum() {
		return ordernum;
	}

	public void setOrdernum(int ordernum) {
		this.ordernum = ordernum;
	}

	public int getFinish() {
		return finish;
	}

	public void setFinish(int finish) {
		this.finish = finish;
	}

	public int getClose() {
		return close;
	}

	public void setClose(int close) {
		this.close = close;
	}

	public long getIncome() {
		return income;
	}

	public void setIncome(long income) {
		this.income = income;
	}

	public List<TbCommentsResult> getList() {
		return list;
	}

	public void setList(List<TbCommentsResult> list) {
		this.list = list;
	}

}
