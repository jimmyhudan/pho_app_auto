package com.pho.hudan.listener;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.IRetryAnalyzer;
import org.testng.annotations.ITestAnnotation;

/**
 * 在TestNG执行过程中动态的改变测试类中的注解的参数，这个主要针对于@Test这个注解
 *  
 *
 */
public class RetryListener implements IAnnotationTransformer {

	@Override
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
		IRetryAnalyzer iRetryAnalyzer = annotation.getRetryAnalyzer();
		if(iRetryAnalyzer == null){
			annotation.setRetryAnalyzer(TestngRetry.class);
		}
		
	}
	

}
