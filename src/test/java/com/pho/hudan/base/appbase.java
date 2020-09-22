package com.pho.hudan.base;


import static org.testng.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import com.pho.hudan.listener.TestngRetry;
import com.pho.hudan.pojo.ActivityPage;
import com.pho.hudan.pojo.Locator;
import com.pho.hudan.util.XmlUtil;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.functions.ExpectedCondition;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.MobileCapabilityType;


public class appbase {
	//private getAndroidDriver()<WebElement> getAndroidDriver();
		// 得到logger对象
		private Logger logger = Logger.getLogger(appbase.class);

		// 使用ThreadLocal来保证线程的安全，以免多线程TestNG并发测试时候访问getAndroidDriver()对象线程安全问题
		public static ThreadLocal<AndroidDriver> threadLocal = new ThreadLocal<AndroidDriver>();

		@Parameters(value = { "udid", "appium_port", "uiautomator2_port" })
		@BeforeTest
		public void setUp(String udid, String appiumPort, String uiautomator2Port)
				throws MalformedURLException, InterruptedException {
			// 创建一个desiredCapabilities配置对象
			// 休眠的目的是为了等待appium server完全开启
			Thread.sleep(8000);

			logger.info("------------------------初始化android驱动------------------------");
			DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
			// desiredCapabilities添加对应的配置platformName
			desiredCapabilities.setCapability("platformName", XmlUtil.configInfo.getPlatformName());
			// 添加对应设备的名字
			desiredCapabilities.setCapability("deviceName", udid);
			// 添加app的包名，根据app包名就可以找到对应的测试app
			desiredCapabilities.setCapability("appPackage", XmlUtil.configInfo.getAppPackage());
			// 添加app的类名，根据activity找到对应app的页面
			desiredCapabilities.setCapability("appActivity", XmlUtil.configInfo.getAppActivity());
			// 加上noReset属性：不清除我们的应用数据进行测试
			// desiredCapabilities.setCapability("noReset", true);
			// 加上SystemPort指定uiautomator2的端口，防止多个android driver通讯的时候冲突
			desiredCapabilities.setCapability("systemPort", uiautomator2Port);
			// 指定automationName为uiautomator2支持toast获取
			desiredCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.ANDROID_UIAUTOMATOR2);

			String accessUrl = "http://127.0.0.1:" + appiumPort + "/wd/hub";
			logger.info("配置:" + desiredCapabilities.toString());
			
			//从threadLocal里面去取的androidDriver，如果为空表示当前线程没有创建androidDriver
			AndroidDriver androidDriver = threadLocal.get();
			if (androidDriver == null) {
				// 开始初始化android驱动对象，将配置传入进去，通过appium的URL可以和appium建立通讯连接
				androidDriver = new AndroidDriver(new URL(accessUrl), desiredCapabilities);
				threadLocal.set(androidDriver);
			}
			//在每条测试用例运行开始之前，就把重试次数retryCount置为1，相当于初始化
			TestngRetry.setRetryCount(1);
		}

		// 取得getAndroidDriver()对象,从threadLocal去取得
		public static AndroidDriver getAndroidDriver() {
			AndroidDriver androidDriver = threadLocal.get();
			return androidDriver;
		}

		@AfterTest
		public void tearDown() {
			// 关闭驱动对象
			getAndroidDriver().quit();
			logger.info("-----------------------------关闭android驱动对象---------------------------");
		}

		/**
		 * 执行adb命令
		 * 
		 * @param adbCmd
		 *            adb命令
		 */
		public void execAdbCmd(String adbCmd) {
			Runtime runtime = Runtime.getRuntime();
			try {
				runtime.exec(adbCmd);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void click(String pageKeyword, String elementKeyword) {
			getElement(pageKeyword, elementKeyword).click();
			logger.info("在页面【" + pageKeyword + "】点击元素【" + elementKeyword + "】");
		}

		public void inputData(String pageKeyword, String elementKeyword, String data) {
			getElement(pageKeyword, elementKeyword).sendKeys(data);
			logger.info("在页面【" + pageKeyword + "】查找元素【" + elementKeyword + "】" + "输入【" + data + "】");
		}

		/**
		 * 获取toast元素文本信息
		 * 
		 * @param tips
		 *            toast内容
		 * @return
		 */
		public WebElement getToast(String tips) {
			logger.info("查找toast【" + tips + "】");
			final String toastTips = tips;
			WebDriverWait webDriverWait = new WebDriverWait(getAndroidDriver(), 30);
			WebElement webElement = webDriverWait.until(new ExpectedCondition<WebElement>() {

				@Override
				public WebElement apply(WebDriver input) {
					// TODO Auto-generated method stub
					return getAndroidDriver().findElement(By.xpath("//*[contains(@text, '" + toastTips + "')]"));
				}
			});
			// 获取toast元素的文本信息
			return webElement;
		}

		/**
		 * 通过关键字解析UI库进行匹配查找元素
		 * 
		 * @param by
		 *            by对象
		 * @return
		 */
		public WebElement getElement(String pageKeyword, String elmentKeyWord) {
			// String activityName = "";
			// String activityDesc = "";
			List<Locator> listLocator = null;
			String locatorBy = "";
			String locatorValue = "";
			String locatorDesc = "";
			// 循环遍历listPages得到页面和元素属性
			for (ActivityPage activityPage : XmlUtil.listPages) {
				if (activityPage.getActivityDesc().equals(pageKeyword)) {
					// activityName = activityPage.getActivityName();
					// activityDesc = activityPage.getActivityDesc();
					listLocator = activityPage.getListLocator();
					for (Locator locator : listLocator) {
						if (locator.getDesc().equals(elmentKeyWord)) {
							locatorBy = locator.getBy();
							locatorValue = locator.getValue();
						}
					}
				}
			}
			// 使用中间final类型的变量进行接收，解决内部类不能使用普通变量的问题
			final String locatorBy2 = locatorBy;
			final String locatorValue2 = locatorValue;
			// 创建webdriverwait对象
			WebElement webElement = null;
			try {
				WebDriverWait webDriverWait = new WebDriverWait(getAndroidDriver(), 30);
				webElement = webDriverWait.until(new ExpectedCondition<WebElement>() {

					@Override
					public WebElement apply(WebDriver input) {
						// 可以使用反射技术得到by对象
						// 得到By对应的字节码对象
						Class<By> clazz = By.class;
						Method method;
						try {
							// 解析得到对应的方法对象
							method = clazz.getMethod(locatorBy2, String.class);
							// 通过method对象执行对应的方法,返回对应的by对象
							By by = (By) method.invoke(null, locatorValue2);
							return getAndroidDriver().findElement(by);
						} catch (NoSuchMethodException e) {
							logger.error("找不到对应方法:" + locatorBy2 + "，请检查你UILibrary配置文件");
							// e.printStackTrace();
						} catch (Exception e) {
							// logger.error("查找元素失败,"+"方法为:"+locatorBy2+",值为:"+locatorValue2);
							// e.printStackTrace();
						}
						return null;

					}
				});

			} catch (Exception e) {
				logger.error("查找元素失败," + "方法为:" + locatorBy2 + ",值为:" + locatorValue2);
			}
			return webElement;
		}

		/**
		 * 自定义向左进行滑动的方法
		 * 
		 * @param num
		 *            滑动次数
		 */
		public void swipeLeft(int num) {
			// 得到X轴长度：分辨率的宽度
			int width = getAndroidDriver().manage().window().getSize().getWidth();
			// 得到Y轴长度：分辨率的高度
			int height = getAndroidDriver().manage().window().getSize().getHeight();
			// x轴起始点的坐标
			int startx = width * 7 / 8;
			// 由于是向左进行滑动，所以y轴的值不需要进行变化
			int starty = height * 1 / 2;
			// x轴终止点的坐标
			int endx = width * 1 / 8;
			int endy = height * 1 / 2;
			// for循环实现滑动几次
			for (int i = 0; i < num; i++) {
				// 创建touchAction对象实现滑动
				TouchAction touchAction = new TouchAction(getAndroidDriver());
				// 起始点到终止点的滑动时间
				Duration delay = Duration.ofMillis(300);
				touchAction.press(startx, starty).waitAction(delay).moveTo(endx, endy).release();
				// 滑动配置生效
				touchAction.perform();
			}
		}

		/**
		 * 向上进行滑动
		 * 
		 * @param num
		 *            滑动次数
		 */
		public void swipeUp(int num) {
			// 得到X轴长度
			int width = getAndroidDriver().manage().window().getSize().getWidth();
			// 得到Y轴长度
			int height = getAndroidDriver().manage().window().getSize().getHeight();
			int startx = width * 1 / 2;
			int starty = height * 7 / 8;
			int endx = width * 1 / 2;
			int endy = height * 1 / 8;
			for (int i = 0; i < num; i++) {
				TouchAction touchAction = new TouchAction(getAndroidDriver());
				Duration delay = Duration.ofMillis(300);
				// appium converts press-wait-moveto-release to a swipe action
				touchAction.press(startx, starty).waitAction(delay).moveTo(endx, endy).release();
				touchAction.perform();
			}

		}

		/**
		 * 向右进行滑动
		 * 
		 * @param num
		 *            滑动次数
		 */
		public void swipeRight(int num) {
			// 得到屏幕的X轴像素点/宽度
			int width = getAndroidDriver().manage().window().getSize().getWidth();
			// 得到屏幕的Y轴像素点/高度
			int height = getAndroidDriver().manage().window().getSize().getHeight();
			int startx = width * 2 / 8;
			int endx = width * 7 / 8;
			int starty = height / 2;
			int endy = height / 2;
			for (int i = 0; i < num; i++) {
				TouchAction touchAction = new TouchAction(getAndroidDriver());
				Duration dd = Duration.ofMillis(500);
				touchAction.press(startx, starty).waitAction(dd).moveTo(endx, endy).release().perform();
			}
		}

		/**
		 * 向下进行滑动
		 * 
		 * @param num
		 *            滑动次数
		 */
		public void swipeDown(int num) {
			// 得到屏幕的X轴像素点/宽度
			int width = getAndroidDriver().manage().window().getSize().getWidth();
			// 得到屏幕的Y轴像素点/高度
			int height = getAndroidDriver().manage().window().getSize().getHeight();
			int startx = width / 2;
			int endx = width / 2;
			int starty = height / 5;
			int endy = height * 4 / 5;
			for (int i = 0; i < num; i++) {
				TouchAction touchAction = new TouchAction(getAndroidDriver());
				Duration dd = Duration.ofMillis(500);
				touchAction.press(startx, starty).waitAction(dd).moveTo(endx, endy).release().perform();
			}
		}

		/**
		 * 开启某一个页面
		 * 
		 * @param packageName
		 *            包名
		 * @param activityName
		 *            类名
		 */
		public void startPage(String packageName, String activityName) {
			logger.info("启动了界面:" + activityName);
			Activity activity = new Activity(packageName, activityName);
			getAndroidDriver().startActivity(activity);
		}
}
