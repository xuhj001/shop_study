package com.goods.search.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Item {

	private String id;
	private String title;
	private String sellPoint;
	private long price;
	private String image;
	private String cidname;
	private String item_des;
	private int num;
	private Byte status;
	private long muser_id;
	
	
	public long getMuser_id() {
		return muser_id;
	}
	public void setMuser_id(long muser_id) {
		this.muser_id = muser_id;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getSellPoint() {
		return sellPoint;
	}
	public void setSellPoint(String sellPoint) {
		this.sellPoint = sellPoint;
	}
	public long getPrice() {
		return price;
	}
	public void setPrice(long price) {
		this.price = price;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	
	
	public String getCidname() {
		return cidname;
	}
	public void setCidname(String cidname) {
		this.cidname = cidname;
	}
	public String getItem_des() {
		return item_des;
	}
	public void setItem_des(String item_des) {
		this.item_des = item_des;
	}
	
	@JsonIgnore
	public String[] getImages() {
		if (image != null) {
			String[] images = image.split(",");
			return images;
		}
		return null;
	}
	
	
}
