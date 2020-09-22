package com.pho.tester;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import io.appium.java_client.android.AndroidDriver;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
public class LoginTest {
	public AndroidDriver<WebElement> anriodDriver;
	@Parameters(value= {"platformName","deviceName","appPackage","appActivity","accessUrl"})
	@BeforeClass
	public void setUp(String platformName,String deviceName,String appPackage,String appActivity,String  accessUrl) throws MalformedURLException {
		//创建一个配置对象
		DesiredCapabilities desiredcapabilitis=new DesiredCapabilities();
		//DesiredCapabilities 添加对应的配置 
		desiredcapabilitis.setCapability("platformName", platformName);
		//添加对应设备的名字
		desiredcapabilitis.setCapability("deviceName", deviceName);
		//添加APP的包名，因为根据app包名就可以找到对应的测试APP
		desiredcapabilitis.setCapability("appPackage",appPackage);
		//添加APP的类名，根据activity找到对应APP的页面
		desiredcapabilitis.setCapability("appActivity",appActivity); 
		//加上noRest属性：不清楚我们的应用数据进行测试
		desiredcapabilitis.setCapability("noRest", true);
		//开始初始化android驱动对象,将配置传入进去，通过appium的Url可以和appium建立通讯连接
		anriodDriver=new AndroidDriver(new URL(accessUrl),desiredcapabilitis);
	}



	@Test(dataProvider="getDatas")
	public void test01(String mobile,String pwd) {
		//appium初始化有做了什么事情
		//1.获取设备很多的参数，型号、制造商、系统版本
		//2.检测对应的APP有没有在运行，如果有运行，会杀掉重启
		//3.安装Appium setting 这个APK,实现和bootstrap.jar的通讯
		//4.push了bootstrap.jar这个文件
		//找到我的柠檬元素并且点击  XPATH  速度特别慢
		anriodDriver.findElement(By.id("com.lenmon.lemonban:id/navigation_my")).click();
		//找到点击头像登录控件并且完成点击
		anriodDriver.findElement(By.id("com.lenmon.lemonban:id/fragment_my_lemon_avator_layout")).click();
		//找到手机号码输入框输入对应的数据库
		anriodDriver.findElement(By.id("com.lenmon.lemonban:id/et_mobile")).sendKeys(mobile);
		//找到密码输入框输入对应的数据库
		anriodDriver.findElement(By.id("com.lenmon.lemonban:id/et_password")).sendKeys(pwd);
		//找到对应的登录按钮，实现点击
		anriodDriver.findElement(By.id("com.lenmon.lemonban:id/btn_login")).click();
		//哪几种不同的方式来实现等待
		//1.显示等待
		//2.隐式等待
		//3.固定等待
		//		Thread.sleep(3000);
		
	}
	
	@DataProvider
	public Object [] [] getDatas()
	{
		Object [] [] datas= {{"",""},{"13761042305","123456"},{"13761042305","223453"}};
		return datas;
	}
	@AfterClass
	public void tearDown()
	{
		//关闭驱动对象
		anriodDriver.quit();
	}
}
