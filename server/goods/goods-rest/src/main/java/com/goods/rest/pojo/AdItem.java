package com.goods.rest.pojo;

public class AdItem {
	private Long id;
	private String dc;
	private String title;
	private Long price;
	private String image;
	private Long muserId;

	public Long getMuserId() {
		return muserId;
	}

	public void setMuserId(Long muserId) {
		this.muserId = muserId;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDc() {
		return dc;
	}

	public void setDc(String dc) {
		this.dc = dc;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}
}
