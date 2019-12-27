package com.example.util;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class DateUtil {

	public final static String FMTSTR = "yyyy/MM/dd HH:mm:ss";
	public final static String FMTSTR_CH = "yyyy年MM月dd日 HH时mm分ss秒";
	public final static String FMTSTR_2 = "yyyy-MM-dd HH:mm:ss";
	public final static String FMTSTR_3 = "yyyy-MM-dd";
	public static SimpleDateFormat GetSimpleDateFormat(String fmt){
		return new SimpleDateFormat(fmt);
	}

	/**
	 * 当前时间
	 * @param d
	 * @return
	 */
	public static String getCurrentTime(Date d,SimpleDateFormat f){
		String rttime = "";
		try {
			rttime = f.format(d);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rttime;
	}

	/**
	 * 判断当前时间是这年份的第几周
	 * @param d
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String getCurrentWeek(Date d){
		int day 	= 	d.getDay();//日
		int year 	= 	d.getYear();//年
		int month 	= 	d.getMonth();//月
		int week = 0;
		if(0 < day && day < 8){
			week = 1;
		}else if(8 < day && day < 16){
			week = 2;
		}else if(16 < day && day < 24){
			week = 3;
		}else if(24 < day && day < 32){
			week = 4;
		}
		return year+"年"+month+"月"+"第"+week+"周";
	}


	/**
	 * 毫秒转换时
	 * @param time
	 * @return
	 */
	public static String timeToDate(long time){
		Date d = new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(d);
	}

}
