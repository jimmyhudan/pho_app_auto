package com.pho.hudan.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CmdUtil {

	/**
	 * 执行dos命令
	 * @param cmd 命令
	 * @return cmd命令运行结果 
	 */
	public static List<String> execCmd(String cmd) {
		Runtime runtime = Runtime.getRuntime();
		BufferedReader bufferedReader = null;
		try {
			Process process = runtime.exec("cmd /c " + cmd);
			bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
			String line = "";
			List<String> content = new ArrayList<String>();
			while ((line = bufferedReader.readLine()) != null) {
				if (!line.isEmpty()) {
					if(line.contains("[Appium] Appium REST http interface listener started on")){
						System.out.println("appium success started!!!");
					}
					content.add(line);
				}
			}
			return content;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}


}
