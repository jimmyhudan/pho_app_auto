package com.pho.hudan.util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 端口相关工具类
 */
public class PortUtil {

	/**
	 * 检测端口是否有被占用
	 * @param port
	 * @return
	 */
	public static boolean checkPortUsed(String port){
		List<String> results = CmdUtil.execCmd("netstat -ano | findstr "+port);
		//如果执行结果返回有结果的话，那么端口被占用
		 if (results.size() > 0){
			System.out.println("端口:"+port+"被占用");
			return true;
		}else {
			System.out.println("端口:"+port+"没有被占用");
			return false;
		}
	}
	
	/**
	 * 根据端口号得到对应进程的ID（pid）
	 * @param port 端口
	 */
	public static String getPidbyPort(String port){
		List<String> results = CmdUtil.execCmd("netstat -ano | findstr "+port);
		if(!results.isEmpty()){
			for (String portNum : results) {
				//先去编译，创建匹配模式
				Pattern p = Pattern.compile("(\\d{1,5})$");
				//Mather类匹配刚才所创建的匹配模式
				Matcher matcher = p.matcher(portNum);
				if(matcher.find()){
					String pid = matcher.group(matcher.groupCount());
					return pid;
				}
			}
		}
		return null;
	}
	
	/**
	 * 根据进程ID杀死对应进程
	 * @param pid
	 */
	public static void killProcessByPid(String pid){
		CmdUtil.execCmd("taskkill -f -pid "+pid);
		System.out.println("杀死对应进程:"+pid+"成功");
	}
	
	/**
	 * 检查端口是否被占用，如果有被占用的话，杀死对应的进程进行释放
	 * @param port
	 */
	public static void checkPort(String port){
		if(checkPortUsed(port)){
			killProcessByPid(getPidbyPort(port));
		}
	}
	
	public static void main(String []args) {
		checkPort(4723+"");
		
	}
}
