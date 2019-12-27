package com.example.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import android.annotation.SuppressLint;

@SuppressLint("UseValueOf")
public class SysUtil{
	
	public static boolean isBlank(String str) {
		if(StringUtils.isEmpty(str) || "null".equals(str) || StringUtils.isBlank(str) || null == str){
			return true;
		}else{
			return false;
		}
	}
	
	public static Boolean isTheSame (String str,String str1){
		if(str.trim().equals(str1.trim())){
			return true;
		}else{
			return false;
		}
	}
	
	public static Boolean isEmpty(Map<String,Object> map){
		if(null != map && map.size() > 0){
			return false;
		}else{
			return true;
		}
	}
	
	@SuppressLint("SimpleDateFormat")
	public static String getSYStIME(){
		Date d = new Date();
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
		return fmt.format(d);
	}
	
	public static boolean isListStringElement(List<String> list,String param){
		boolean rt = false;
		for (String str : list) {
			if (str.equals(param)) {
				rt =  true;
			}
		} 
		return rt;
	}
	
	public static boolean stringToBoolean(String str){
		if(isBlank(str)){
			return false;
		}else{
			Boolean Bl = new Boolean(str);
			return Bl.booleanValue();
		}
	}

}
