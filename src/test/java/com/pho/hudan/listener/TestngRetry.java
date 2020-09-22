package com.pho.hudan.listener;

import org.apache.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import com.pho.hudan.base.appbase;


/**
 * 自定义testngRetry类实现IRetryAnalyzer接口，来完成重试机制
 
 */
public class TestngRetry implements IRetryAnalyzer {
	// 最大重试次数的限制
	private int maxRetryCount = 2;
	// 初始化log4j日志对象
	private Logger logger = Logger.getLogger(TestngRetry.class);
	
	private static ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>();

	@Override
	public boolean retry(ITestResult result) {
		//从ThreadLocal里面取得retryCount
		if (getRetryCount() <= maxRetryCount) {
			logger.info("开始第" + getRetryCount()  + "次重试" + "测试方法为:" + result.getName());
			int retryCount = getRetryCount();
			retryCount++;
			setRetryCount(retryCount);
			// 返回为true表示执行重试机制
			return true;
		}
		// 返回为false表示不执行重试机制
		return false;
	}
	
	public static int getRetryCount(){
		return threadLocal.get();
	}
	
	public static void setRetryCount(int retryCount){
		threadLocal.set(retryCount);
	}

}
