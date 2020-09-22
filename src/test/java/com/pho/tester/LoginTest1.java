package com.pho.tester;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.Set;

import javax.annotation.concurrent.ThreadSafe;
import javax.swing.plaf.synth.SynthSeparatorUI;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.internal.annotations.ExpectedExceptionsAnnotation;


import com.pho.hudan.base.appbase;
import com.pho.hudan.util.AssertUtil;
import com.pho.hudan.util.ExcelUtil;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinUser.INPUT;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.functions.ExpectedCondition;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.MobileCapabilityType;

public class LoginTest1 extends appbase {
	
	@Test(priority=1)
	public void mainPage(){
		// 找到我的柠檬元素并且点击
		click("主页页面", "我的柠檬");
		// 找到点击头像登录控件并且完成点击
		click("主页页面", "头像登录");
		System.out.println("current thread:::"+Thread.currentThread());
	}
	
	@Test(dataProvider = "getDatas",priority=2)
	public void login(String mobilephone, String pwd, String tips) throws MalformedURLException, InterruptedException {
		//
		//startPage("com.lemon.lemonban", "com.lemon.lemonban.MainActivity");
		// 找到手机号码输入框输入对应的数据库
		inputData("登录页面", "手机号码输入框", mobilephone);
		// 找到密码输入框输入对应的数据库
		inputData("登录页面", "密码输入框", pwd);
		// 找到对应的登录按钮，实现点击
		click("登录页面", "登录按钮");
		// 通过xpath的方式得到对应toast信息
		// 断言
		AssertUtil.assertTextPresent(getToast(tips), tips);

	}

	@DataProvider
	public Object[][] getDatas() {
		Object[][] datas = ExcelUtil.readExcel("/lemon_testcase.xlsx", 2, 6, 1, 3);
		return datas;
	}

}
