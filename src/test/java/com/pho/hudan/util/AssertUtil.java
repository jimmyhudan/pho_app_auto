package com.pho.hudan.util;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.pho.hudan.base.appbase;

/**
 * 检查点工具类
 */
public class AssertUtil {
	// assertTextPresent：断言页面元素文本值为某文本
	
	//初始化logger对象
	private static Logger logger = Logger.getLogger(AssertUtil.class);
	
	public static void assertTextPresent(WebElement element, String expectedText) {
		String actualText = element.getText();
		logger.info("断言"+"【实际值】"+"【"+actualText+"】"+"【期望值】"+"【"+expectedText+"】");
		Assert.assertEquals(actualText, expectedText);
	}

	// assertPartialTextPresent：断言页面元素包含某文本
	public static void assertPartialTextPresent(WebElement element, String expectedText) {
		String actualText = element.getText();
		Assert.assertTrue(actualText.contains(expectedText));
	}

	// assertElementEditable：断言某元素可编辑
	public static void assertElementEditable(WebElement element) {
		Assert.assertTrue(element.isEnabled());
	}

	// assertElementNotEditable：断言某元素不可编辑
	public static void assertURLContains(WebElement element) {
		Assert.assertFalse(element.isEnabled());
	}
	// assertURLContains：断言当前URL包含
	// assertTextNotPresent
	// assertTextNotPresentPrecesion
	// assertElementAttributeValueEquals
	// assertElementAttributeValueNotEquals
	// assertAlertTextContains

}
