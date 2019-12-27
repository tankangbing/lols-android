package com.example.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.StringSignature;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {



	/**
	 * 将多个换行符替换成一个换行符
	 *
	 * @param str
	 * @return
	 */
	public static String replaceLineBlanks(String str) {
		String result = "";
		if (str != null) {
			Pattern p = Pattern.compile("(\r?\n(\\s*\r?\n)+)");
			Matcher m = p.matcher(str);
			result = m.replaceAll("\r\n");
		}
		return result;


	}

	/**
	 * 获取当前app版本
	 * @return
	 */
	public static String getVersionName(Context context){
		PackageManager packageManager = context.getPackageManager();
		PackageInfo packInfo;
		String version = "未知";
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
			version = packInfo.versionName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return version;
	}

	/**
	 * 获取应用程序名称
	 */
	public static String getAppName(Context context)
	{
		try
		{
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			int labelRes = packageInfo.applicationInfo.labelRes;
			return context.getResources().getString(labelRes);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 是否存在sd卡
	 */
	public static boolean isExistSD()
	{
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}


	/**
	 * 判断服务是否开启
	 * @param mContext
	 * @param className
	 * 是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
	 * @return
	 */
	public static boolean isServiceRunning(Context mContext,String className) {
		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager)
				mContext.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList
				= activityManager.getRunningServices(30);
		if (!(serviceList.size()>0)) {
			return false;
		}
		for (int i=0; i<serviceList.size(); i++) {
			if (serviceList.get(i).service.getClassName().equals(className) == true) {
				isRunning = true;
				break;
			}
		}
		return isRunning;
	}

	/**
	 * 加载图片
	 * @param context
	 * @param imageUrl
	 * @param errorImageId
	 * @param imageView
	 */
	public static void loadIntoUseFitWidth(Context context, final String imageUrl, int errorImageId, final ImageView imageView, String sign) {
		Glide.with(context)
				.load(imageUrl)
				.signature(new StringSignature(sign))
				.diskCacheStrategy(DiskCacheStrategy.SOURCE)
				.listener(new RequestListener<String, GlideDrawable>() {
					@Override
					public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
						return false;
					}

					@Override
					public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
						if (imageView == null) {
							return false;
						}
						if (imageView.getScaleType() != ImageView.ScaleType.FIT_XY) {
							imageView.setScaleType(ImageView.ScaleType.FIT_XY);
						}
						ViewGroup.LayoutParams params = imageView.getLayoutParams();
						int vw = imageView.getWidth() - imageView.getPaddingLeft() - imageView.getPaddingRight();
						float scale = (float) vw / (float) resource.getIntrinsicWidth();
						int vh = Math.round(resource.getIntrinsicHeight() * scale);
						params.height = vh + imageView.getPaddingTop() + imageView.getPaddingBottom();
						imageView.setLayoutParams(params);
						return false;
					}
				})
				.placeholder(errorImageId)
				.error(errorImageId)
				.into(imageView);
	}

}
