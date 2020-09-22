package com.pho.hudan.util;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.pho.hudan.pojo.ActivityPage;
import com.pho.hudan.pojo.ConfigInfo;
import com.pho.hudan.pojo.Locator;


/**
 */
public class XmlUtil {
	
	public static ConfigInfo configInfo = null;
	public static List<ActivityPage> listPages = new ArrayList<ActivityPage>();

	static {
		//取出UILibrary.xml的值
		readUILibrary();
		readConfigInfo();
	}

	
	
	/**
	 * 解析ConfigInfos.xml文件
	 */
	public static void readConfigInfo(){
		SAXReader saxReader = new SAXReader();
		try {
			//通过saxRead读取对应的xml文件
			Document document = saxReader.read(XmlUtil.class.getResourceAsStream("/ConfigInfos.xml"));
			//得到根节点
			Element rootElement = document.getRootElement();
			
			//得到platformName标签
			Element platformNameElement = rootElement.element("platformName");
			String platformName = platformNameElement.attributeValue("value");
			
			//得到appPackage标签
			Element appPackageElement = rootElement.element("appPackage");
			String appPackage = appPackageElement.attributeValue("value");
			
			//得到appActivity标签
			Element appActivityElement = rootElement.element("appActivity");
			String appActivity = appActivityElement.attributeValue("value");
			
			//得到classes标签
			Element classesElement = rootElement.element("classes");
			
			//得到classes的子标签class
			List<Element> classElement = classesElement.elements("class");
			
			//保存class
			List<String> csList = new ArrayList<String>();
			for (Element element : classElement) {
				String csName = element.attributeValue("name");
				csList.add(csName);
			}
			configInfo  = new ConfigInfo(platformName, appPackage, appActivity, csList);
			
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	


	public static List<ActivityPage> readUILibrary() {
		// 创建SaxReader对象解析xml
		SAXReader reader = new SAXReader();
		// 获得xml文档
		Document document;
		try {
			document = reader.read(XmlUtil.class.getResourceAsStream("/UILibrary.xml"));
			// 获得根元素
			Element rootElement = document.getRootElement();
			// 获得所有子元素

			List<Element> pageElements = rootElement.elements("activity");
			// 遍历每一个页面
			for (Element page : pageElements) {
				// 页面的名称--》类名
				String pageName = page.attributeValue("activityName");
				// 页面的描述
				String pageDesc = page.attributeValue("activityDesc");
				List<Locator> listLocator = new ArrayList<Locator>();
				// 解析list locator
				List<Element> listElement = page.elements("locator");
				for (Element element : listElement) {
					String locatorBy = element.attributeValue("by");
					String locatorValue = element.attributeValue("value");
					String locatorDesc = element.attributeValue("desc");
					// 创建locator对象
					Locator locator = new Locator(locatorBy, locatorValue, locatorDesc);
					// 添加到list locator集合中
					listLocator.add(locator);
				}
				// 创建ActivityPage 页面pojo对象
				ActivityPage activityPage = new ActivityPage(pageName, pageDesc, listLocator);
				// 添加到listpages集合去
				listPages.add(activityPage);
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listPages;

	}

	
	public  static void main(String args[]) {
		List<ActivityPage>  ss=readUILibrary();
		for(ActivityPage dssd:ss)
		{
			System.out.println(dssd.getActivityName()+":"+dssd.getActivityDesc());
			List<Locator> listLocator=dssd.getListLocator();
			for(Locator ddaalor:listLocator)
			{
				System.out.println(ddaalor.getBy());
				System.out.println(ddaalor.getValue()); 
				System.out.println(ddaalor.getDesc());

			}
		}
	}
	

	/**
	 * 创建testng.xml 文件
	 * @param deviceNum 设备数量
	 * @param classNames 测试用例的集合
	 */
	public static void createTestngXml(int deviceNum,List<String> classNames){
		//创建document对象
		Document document = DocumentHelper.createDocument();
		//得到根节点元素的对象 
		Element root = DocumentHelper.createElement("suite");
		//设置根节点
		document.setRootElement(root);
		//添加name属性
		root.addAttribute("name", "Suite");
		//添加parallel属性支持多线程 
		root.addAttribute("parallel", "tests");
		//添加thread-count(根据连接到PC端的设备数量决定)
		root.addAttribute("thread-count", deviceNum+"");
		//在test下创建listeners元素
		Element listeners = root.addElement("listeners");

		//创建listener HTMLReporter标签
		Element listenerReport = listeners.addElement("listener");
		listenerReport.addAttribute("class-name", "org.uncommons.reportng.HTMLReporter");
		
		//创建listener TestListener标签
		Element listenerTest = listeners.addElement("listener");
		listenerTest.addAttribute("class-name", "com.pho.hudan.listener.TestListener");
		
		//创建listener TestListener标签
		Element listenerRetry = listeners.addElement("listener");
		listenerRetry.addAttribute("class-name", "com.pho.hudan.listener.RetryListener");
		
		//循环添加对应的test
		for (int i = 0; i < deviceNum; i++) {
			//在suite标签下添加test标签
			Element test= root.addElement("test");
			//添加test的name属性
			test.addAttribute("name", AppiumServerUtil.udidList.get(i));
			//在test标签下添加udid parameter标签
			Element udidParameter = test.addElement("parameter");
			udidParameter.addAttribute("name", "udid");
			udidParameter.addAttribute("value", AppiumServerUtil.udidList.get(i));
			//在test标签下添加appiumPort parameter标签
			Element appiumPortParameter = test.addElement("parameter");
			appiumPortParameter.addAttribute("name", "appium_port");
			appiumPortParameter.addAttribute("value", AppiumServerUtil.appiumPortList.get(i));
			
			//在test标签下添加uiautomator2Port parameter标签
			Element uiautomator2PortParameter = test.addElement("parameter");
			uiautomator2PortParameter.addAttribute("name", "uiautomator2_port");
			uiautomator2PortParameter.addAttribute("value", AppiumServerUtil.uiautomator2PortList.get(i));
			
			//创建classes标签 执行用例
			Element csElement = test.addElement("classes");
			for (String className : classNames) {
				Element classElement = csElement.addElement("class");
				classElement.addAttribute("name", className);
			}
			
		}
		//创建format格式对象
		OutputFormat format = OutputFormat.createPrettyPrint();
		try {
			//创建输出流对象
			FileOutputStream fileOutputStream = new FileOutputStream(System.getProperty("user.dir") + "\\testng_app.xml");
			//创建xmlWriter对象写入到文档中
			XMLWriter xmlWriter = new XMLWriter(fileOutputStream,format);
			xmlWriter.write(document);
			System.out.println("生成testng_app.xml文件成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("生成testng_app.xml文件失败");
		}
		
		
		
	}

}
