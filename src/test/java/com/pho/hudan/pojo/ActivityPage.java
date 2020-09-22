package com.pho.hudan.pojo;

import java.util.List;

/**
 * 页面的封装
 *
 */
public class ActivityPage {
	private String activityName;
	private String activityDesc;
	private List<Locator> listLocator;
	
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public String getActivityDesc() {
		return activityDesc;
	}
	public void setActivityDesc(String activityDesc) {
		this.activityDesc = activityDesc;
	}
	public List<Locator> getListLocator() {
		return listLocator;
	}
	public void setListLocator(List<Locator> listLocator) {
		this.listLocator = listLocator;
	}
	@Override
	public String toString() {
		return "ActivityPage [activityName=" + activityName + ", activityDesc=" + activityDesc + ", listLocator="
				+ listLocator + "]";
	}
	public ActivityPage(String activityName, String activityDesc, List<Locator> listLocator) {
		super();
		this.activityName = activityName;
		this.activityDesc = activityDesc;
		this.listLocator = listLocator;
	}
	
	


}
