package com.bimuo.easy.collection.personposition.core.util;

import java.io.File;
import java.io.IOException;

import com.google.common.io.Files;

public class LicenseUtil {
	public static void init(){
		LicenseUtil.copyLibToJavaPath();
	}
	
	/**
	 * 注册
	 * @param licenseKey
	 * @return
	 */
	public static boolean register(String licenseKey){
		return false;
	}
	
	/**
	 * 获取本地认证key字符
	 * @return
	 */
	public static String getLocationLicenseKey(){
		return "";
	}
	
	/**
	 * 获取认证编码
	 * @return
	 */
	@SuppressWarnings("unused")
	private static String getLicenseCode(){
		return "";
	}
	
	/**
	 * 拷贝库文件到java库路
	 * @throws IOException 
	 */
	private static void copyLibToJavaPath() {
		String libName = LicenseUtil.getLibFileName();
		System.out.println("libName:"+libName);
		if(libName=="") return;
		String srcLibPath = System.getProperty("user.dir")+"/libs/"+libName;
		String sysLibPath = System.getProperty("java.library.path");
		String[] dirs = sysLibPath.split("[;,:]");
		
		File srcFile = new File(srcLibPath);
		for(String dir : dirs){
			try{
				File destFile =  new File(dir+"/"+libName);
				if(!destFile.exists()){
					Files.copy(srcFile,destFile);
				}
			}catch(Exception e){}
		}
	}
	
	/**
	 * 根据不作系统获取�?要拷贝的动文�?
	 * @return
	 */
	private static String getLibFileName(){
		String osName = System.getProperty("os.name");
		String osBit = System.getProperty("os.arch");
		String libName = "";
		if(osName.toLowerCase().indexOf("mac")>=0){
			if(osBit.indexOf("64")>=0){
				libName = "libsigar-universal64-macosx.dylib";
			}else if(osBit.indexOf("x86")>=0){
				libName = "libsigar-universal-macosx.dylib";
			}
		}else if(osName.toLowerCase().indexOf("linux")>=0){
			if(osBit.indexOf("64")>=0){
				libName = "libsigar-amd64-linux.so";
			}else if(osBit.indexOf("x86")>=0){
				libName = "libsigar-x86-linux.so";
			}
		}else if(osName.toLowerCase().indexOf("windows")>=0){
			if(osBit.indexOf("64")>=0){
				libName = "sigar-amd64-winnt.dll";
			}else if(osBit.indexOf("x86")>=0){
				libName = "sigar-x86-winnt.dll";
			}
		}
		return libName;
	}
}
