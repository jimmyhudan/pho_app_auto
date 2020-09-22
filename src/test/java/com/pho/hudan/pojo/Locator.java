package com.pho.hudan.pojo;

/**
 * 元素的封装
 *  
 */
public class Locator {
	private String By;
	private String value;
	private String desc;

	public String getBy() {
		return By;
	}

	public void setBy(String by) {
		By = by;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public String toString() {
		return "Locator [By=" + By + ", value=" + value + ", desc=" + desc + "]";
	}

	public Locator(String by, String value, String desc) {
		super();
		By = by;
		this.value = value;
		this.desc = desc;
	}
	
	
	

}
