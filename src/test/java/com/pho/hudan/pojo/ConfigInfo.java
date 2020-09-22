package com.pho.hudan.pojo;

import java.util.List;

/**
 * 保存配置信息（包名,activity名，测试用例类，平台版本）  
 *
 */
public class ConfigInfo {
	private String platformName;
	private String appPackage;
	private String appActivity;
	private List<String> classList;

	public String getPlatformName() {
		return platformName;
	}

	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}

	public String getAppPackage() {
		return appPackage;
	}

	public void setAppPackage(String appPackage) {
		this.appPackage = appPackage;
	}

	public String getAppActivity() {
		return appActivity;
	}

	public void setAppActivity(String appActivity) {
		this.appActivity = appActivity;
	}

	public List<String> getClassList() {
		return classList;
	}

	public void setClassList(List<String> classList) {
		this.classList = classList;
	}

	@Override
	public String toString() {
		return "ConfigInfo [platformName=" + platformName + ", appPackage=" + appPackage + ", appActivity="
				+ appActivity + ", classList=" + classList + "]";
	}

	public ConfigInfo(String platformName, String appPackage, String appActivity, List<String> classList) {
		super();
		this.platformName = platformName;
		this.appPackage = appPackage;
		this.appActivity = appActivity;
		this.classList = classList;
	}

}
