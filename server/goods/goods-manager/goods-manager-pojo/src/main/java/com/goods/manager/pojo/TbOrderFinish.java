package com.goods.manager.pojo;

import java.sql.Date;

public class TbOrderFinish {
	private String title;// ��Ʒ����
	private long price;// ��Ʒ�۸�
	private long total_fee;// ��Ʒ�ܼ۸�
	private String pic_path;//��ƷͼƬ
	private long num;// ��������
	private Date end_time;// ���ʱ��
	private String buyer_nick;// �û��ǳ�
	private long user_id;// �û�id
	private String receiver_name;// �ռ�������
	private String receiver_mobile;// �ռ����ֻ�
	private String receiver_city;// ����
	private String receiver_district;// ��
	private String receiver_address;// ��ϸ��ַ
	private String receiver_zip;// �ʱ�
	private String item_id;//��Ʒid
	

	public String getPic_path() {
		return pic_path;
	}
	public void setPic_path(String pic_path) {
		this.pic_path = pic_path;
	}
	public String getItem_id() {
		return item_id;
	}
	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public long getPrice() {
		return price;
	}
	public void setPrice(long price) {
		this.price = price;
	}
	public long getTotal_fee() {
		return total_fee;
	}
	public void setTotal_fee(long total_fee) {
		this.total_fee = total_fee;
	}
	public long getNum() {
		return num;
	}
	public void setNum(long num) {
		this.num = num;
	}
	public Date getEnd_time() {
		return end_time;
	}
	public void setEnd_time(Date end_time) {
		this.end_time = end_time;
	}
	public String getBuyer_nick() {
		return buyer_nick;
	}
	public void setBuyer_nick(String buyer_nick) {
		this.buyer_nick = buyer_nick;
	}
	public long getUser_id() {
		return user_id;
	}
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
	public String getReceiver_name() {
		return receiver_name;
	}
	public void setReceiver_name(String receiver_name) {
		this.receiver_name = receiver_name;
	}
	public String getReceiver_mobile() {
		return receiver_mobile;
	}
	public void setReceiver_mobile(String receiver_mobile) {
		this.receiver_mobile = receiver_mobile;
	}
	public String getReceiver_city() {
		return receiver_city;
	}
	public void setReceiver_city(String receiver_city) {
		this.receiver_city = receiver_city;
	}
	public String getReceiver_district() {
		return receiver_district;
	}
	public void setReceiver_district(String receiver_district) {
		this.receiver_district = receiver_district;
	}
	public String getReceiver_address() {
		return receiver_address;
	}
	public void setReceiver_address(String receiver_address) {
		this.receiver_address = receiver_address;
	}
	public String getReceiver_zip() {
		return receiver_zip;
	}
	public void setReceiver_zip(String receiver_zip) {
		this.receiver_zip = receiver_zip;
	}


}
