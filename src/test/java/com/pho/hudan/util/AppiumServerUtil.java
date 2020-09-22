package com.pho.hudan.util;

import java.util.ArrayList;
import java.util.List;
import com.pho.hudan.util.CmdUtil;

import org.testng.TestNG;

/**
 * 管理appium server工具类
 * 
 *
 */
public class AppiumServerUtil {

	// 保存udid的集合
	public static List<String> udidList = new ArrayList<String>();
	// 保存appiumPort端口的集合
	public static List<String> appiumPortList = new ArrayList<String>();
	// 保存bootstrap端口的集合
	public static List<String> bootstrapPortList = new ArrayList<String>();
	//保存UIAutomator2的端口集合
	public static List<String> uiautomator2PortList = new ArrayList<String>();

	//

	/**
	 * 得到当前连接到PC端所有的android设备udid集合
	 * 
	 * @return
	 */
	public static List<String> getUdidList() {
		// 执行adb devices命令
		List<String> results = CmdUtil.execCmd("adb devices");

		for (int i = 0; i < results.size(); i++) {
			// 跳过第一行的内容再去进行解析：List of devices attached
			if (i != 0) {
				try {
					// 把每一行信息进行分割提取到字符串数组里面去
					String[] deviceInfos = results.get(i).split("\t");
					if (deviceInfos[1].equals("device")) {
						System.out.println("成功获取到设备:" + deviceInfos[0].toString());
						// System.out.println(deviceInfos[0]);
						udidList.add(deviceInfos[0]);
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					// 如果adb 服务没有运行的话，通过adb device命令会输出下面的结果
					// * daemon not running. starting it now on port 5037 *
					// * daemon started successfully *
					// 索引加2表示跳过这两行
					i = i + 2;
				}

			}

		}
		if(udidList.size() ==0){
			System.out.println("没有设备连接到PC端，终止脚本运行");
			System.exit(0);
		}
		return udidList;

	}

	/**
	 * 创建端口列表
	 * 
	 * @param startPort
	 *            端口的开始值
	 * @param deviceTotal
	 *            设备总数
	 * @return 端口号列表
	 */
	public static List<String> createPortList(int startPort, int deviceTotal) {
		// 4723 4725 4727
		List<String> portList = new ArrayList<String>();
		for (int i = 0; i < deviceTotal; i++) {
			portList.add(startPort + "");
			startPort = startPort + 2;
		}
		return portList;
	}

	/**
	 * 得到开启appium命令参数的列表
	 * 
	 * @return命令参数列表
	 */
	public static List<String> getStartAppiumCommand() {
		appiumPortList = createPortList(4723, udidList.size());
		bootstrapPortList = createPortList(4724, udidList.size());
		uiautomator2PortList= createPortList(8200, udidList.size());

		List<String> commandList = new ArrayList<String>();
		for (int j = 0; j < udidList.size(); j++) {
			// 命令格式
			String cmd = "node "
					+ "C:\\Users\\Administrator\\AppData\\Local\\appium-desktop\\app-1.5.0\\resources\\app\\node_modules\\appium\\build\\lib\\main.js "
					+ " -a " + " 127.0.0.1" + " -p " + appiumPortList.get(j) + " -bp " + bootstrapPortList.get(j)
					+ " -U " + udidList.get(j) + " --session-override";

			commandList.add(cmd);
		}
		return commandList;
	}

	public static void main(String[] args) {
		//udid集合
		List<String> uList = getUdidList();

		//命令参数集合
		List<String> commandList = getStartAppiumCommand();
		
		//检测appium port列表端口号情况
		for(String appiumPort : appiumPortList){
			PortUtil.checkPort(appiumPort);
		}
		//检测bootstrap port端口列表情况
		for(String bootstrap: bootstrapPortList){
			PortUtil.checkPort(bootstrap);
		}
		
		//存放测试用例的集合
		List<String> classLists = XmlUtil.configInfo.getClassList();
		
		//生成testng_app.xml
		XmlUtil.createTestngXml(udidList.size(),classLists);
		
		// 进程阻塞 
		// 循环创建线程启动appium server，如果用进程，可能会造成阻塞
		for (final String cmd : commandList) { 
			new Thread(new Runnable() {
				@Override
				public void run() {
					System.out.println(cmd);
					CmdUtil.execCmd(cmd);

				}
			}).start();

		}
		runTestng();

	}
	
	/**
	 * 实现通过代码方式来运行testng.xml配置文件
	 */
	public static void runTestng(){
		//创建testng对象
		TestNG testNG = new TestNG();
		//想要运行testng xml文件加入到list集合中去
		List<String> testngFiles = new ArrayList<String>();
		testngFiles.add(System.getProperty("user.dir")+"\\testng_app.xml");
		//输出报告保存在target
		testNG.setOutputDirectory(System.getProperty("user.dir")+"\\target");
		testNG.setTestSuites(testngFiles);
		testNG.run();
		
	}
}
