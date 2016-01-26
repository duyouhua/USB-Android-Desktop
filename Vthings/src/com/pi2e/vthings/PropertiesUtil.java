package com.pi2e.vthings;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class PropertiesUtil {

	Properties prop;
	
	private static final String PROFILE="vthings.properties";
	
	public static final String WIFI_NAME ="wifi_name";
	public static final String WIFI_PASSWORD ="wifi_password";
	public static final String WIFI_MAX ="wifi_max";
	public static final String WIFI_MIN ="wifi_min";
	public static final String IMG ="img_info";
	public static final String APK ="apk_info";
	public static final String ADBPATH ="adbpath";

	private PropertiesUtil() {
		prop = new Properties();
	}

	private static PropertiesUtil propertiesUtil = null;

	public static PropertiesUtil getInstance() {
		if (propertiesUtil == null) {
			propertiesUtil = new PropertiesUtil();
		}
		return propertiesUtil;
	}

	public void setValue(String k, String v) {
		
		if (v==null||v.length()==0) {
			v="";
		}
		 prop.setProperty(k, v);   
		 try {
			 FileOutputStream fos = new FileOutputStream(PROFILE); 
			  prop.store(fos, "Vthings");     
		        fos.close();// 关闭流     
		} catch (Exception e) {
			// TODO: handle exception
		}
		    
	}
	public void setValueEnd(){
		
	}

	public String getValue(String k) {
		try {
			 FileInputStream fis = new FileInputStream(PROFILE);// 属性文件输入流     
		     prop.load(fis);// 将属性文件流装载到Properties对象中     
		     fis.close();// 关闭流  
		     
		     if (k.equals(PropertiesUtil.WIFI_MAX)||k.equals(PropertiesUtil.WIFI_MIN)) {
		    	 return  prop.getProperty(k, "0");
			}
		     
		   return  prop.getProperty(k, "");
		} catch (Exception e) {
			// TODO: handle exception
			return "";
		}
		
		
	}
}
