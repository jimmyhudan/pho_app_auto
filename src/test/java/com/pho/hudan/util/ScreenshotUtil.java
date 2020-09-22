package com.pho.hudan.util;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import com.pho.hudan.base.appbase;

import io.appium.java_client.android.AndroidDriver;

/**
 * 截屏的工具类
 * 
 */
public class ScreenshotUtil {

	private static Logger logger = Logger.getLogger(ScreenshotUtil.class);

	public static void main(String[] args) {
		Date now = new Date();
		System.out.println(now.getTime());// 1522159532177
		// 1522159538925
		// 1522159546442

	}

	/**
	 * 截屏的方法
	 * 
	 * @param screenshotDir
	 *            截图要保存的位置
	 */
	public static File takeScreenshot(AndroidDriver androidDriver, String screenshotDir) {
		// 每个测试用例执行失败的的话，保存的截屏文件名应该是唯一的？？
		// 1522159546442.jpg
		Date now = new Date();
		String fileName = now.getTime() + ".jpg";
		File destFile = new File(screenshotDir + File.separator + fileName);
		File sourceFile = null;
		// 对android设备进行截屏
		sourceFile = androidDriver.getScreenshotAs(OutputType.FILE);

		try {
			// 拷贝到screenshotDir
			// 图片--》jpg--》给一个目标文件的对象
			logger.info("拷贝截图文件到surefire目录下");
			FileUtils.copyFile(sourceFile, destFile);
		} catch (IOException e) {
			logger.error("截图失败");
			e.printStackTrace();
		}

		return destFile;
	}

}
