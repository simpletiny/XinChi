package com.xinchi.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * @author wjx
 * @date 2014年12月18日 下午1:48:05
 * @note 读取文件
 */
public class ReadTextUtil {
	public static String getText(String path) throws Exception {
		String text = "";
		File resume = new File(path);
		if (resume.exists()) {
			FileInputStream in = new FileInputStream(path);
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));
			char[] a=new char[in.available()];
			br.read(a);
			br.close();
			in.close();
			text = new String(a);
		} else {
			System.out.println("err_msg:" + path + "简历不存在");
		}
//		System.out.println("path==" + path + "|||||||||||||||| text==" + text );
		return text;
	}

	public static String getProjectPath() {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if (classLoader == null) {
			classLoader = ClassLoader.getSystemClassLoader();
		}
		java.net.URL url = classLoader.getResource("");
		String ROOT_CLASS_PATH = url.getPath() + "/";
		File rootFile = new File(ROOT_CLASS_PATH);
		String WEB_INFO_DIRECTORY_PATH = rootFile.getParent() + "/";
		File webInfoDir = new File(WEB_INFO_DIRECTORY_PATH);
		String SERVLET_CONTEXT_PATH = webInfoDir.getParent() + "/";
		return SERVLET_CONTEXT_PATH;
	}

	public static void main(String[] args) {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if (classLoader == null) {
			classLoader = ClassLoader.getSystemClassLoader();
		}
		java.net.URL url = classLoader.getResource("");
		String ROOT_CLASS_PATH = url.getPath() + "/";
		File rootFile = new File(ROOT_CLASS_PATH);
		String WEB_INFO_DIRECTORY_PATH = rootFile.getParent() + "/";
		File webInfoDir = new File(WEB_INFO_DIRECTORY_PATH);
		String SERVLET_CONTEXT_PATH = webInfoDir.getParent() + "/";
		System.out.println(SERVLET_CONTEXT_PATH);
	}
}
