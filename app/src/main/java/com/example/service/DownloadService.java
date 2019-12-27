package com.example.service;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.example.entity.FileInfo;
import com.example.jdbc.Dao.FileDAO;
import com.example.jdbc.Dao.impl.FileDAOImpl;
import com.example.jsonReturn.RideoFlagReturn;
import com.example.onlinelearnActivity.download.DownloadXxxwActivity;
import com.example.util.ApplicationUtil;
import com.example.util.FinalStr;
import com.example.util.JsonUtil;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 下载服务类，用于执行下载任务，并且将下载进度传递到Activty中
 */
public class DownloadService extends Service {

	private final String TAG ="DownloadService";
	private final String TAG_JSON ="JSON";
	//通讯类
	private LocalBinder mBinder = new LocalBinder();

	public static final String ACTION_STOP = "ACTION_STOP";
	public static final String ACTION_UPDATE = "ACTION_UPDATE";
	public static final String ACTION_FINISHED = "ACTION_FINISHED";
	public static final String ACTION_ERROR ="ACTION_ERROR";

	//最大并行下载量
    private final int MAX_COUNT = 2;
	//初始化消息
	public static final int MSG_INIT = 0;
	//出错消息
	public static final int MSG_INIT_ERROR = 1;
	//所有任务数
	private Map<Integer, DownloadTask> mTasks = new LinkedHashMap<Integer, DownloadTask>();
	//等待下载的list
	private List<FileInfo> downloadList = new ArrayList<>();
	//正在下载的list
	private Map<Integer, FileInfo> downloadingList = new HashMap<Integer, FileInfo>();
	//数据库操作接口
	private FileDAO fileDao ;

	@Override
	public IBinder onBind(Intent intent) {
		Log.d(TAG,"DownloadService onBind");
		return mBinder;
	}

	@Override
	public void onCreate() {
		Log.d(TAG,"DownloadService onCreate");
		super.onCreate();
		fileDao =new FileDAOImpl(this);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG,"DownloadService onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * 获取文件信息
	 */
	public void getDownloadMessage(ApiService serverApi ,String accountId,final FileInfo fileInfo){
		//调用接口方法
		Call<ResponseBody> call = serverApi.getFileMessage(accountId,fileInfo.getClass_id(),fileInfo.getBehavior_id());
		call.enqueue(new Callback<ResponseBody>() {
			@Override
			public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {

				if (response.isSuccessful()) { //请求视频信息成功
					try {
						String showRideoResult =response.body().string();
						Log.d(TAG_JSON,"获取文件信息返回" + showRideoResult);
						//gson解析
						RideoFlagReturn rideoReturn = JsonUtil.parserString(showRideoResult,RideoFlagReturn.class);

						if (rideoReturn.isSuccess()){
							//更新文件信息
							getDownloadMessageSuccess(fileInfo,rideoReturn);
						}else{
							getDownloadMessageFial(fileInfo);
						}

					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			@Override
			public void onFailure(Call<ResponseBody> call, Throwable t) {
				getDownloadMessageFial(fileInfo);
			}
		});
	}


	/**
	 * 更新文件信息
	 * @param fileInfo
	 * @param rideoReturn
	 */
	private void getDownloadMessageSuccess(FileInfo fileInfo, RideoFlagReturn rideoReturn){
		String webPath =rideoReturn.getResourceWebPath();
        String fileUrl;
        //得到url
        if (webPath.contains("http")){
            fileUrl =webPath;
        }else{
            fileUrl =rideoReturn.getResourceIPAddress()+webPath;
        }
		int index =webPath.lastIndexOf("/");
		//保存地址
		String savePath = FinalStr.DOWNLOAD_PATH;
		int index2 =webPath.lastIndexOf(".");
		//文件类型
		String fileType = webPath.substring(index2,webPath.length());
		//如果是文档就替换后缀
		if (fileInfo.getFile_code().equals("1")){
			fileUrl =fileUrl.replace(".swf",".pdf");
			fileType =".pdf";
		}
		fileInfo.setUrl(fileUrl);
		fileInfo.setFile_time(Integer.parseInt(rideoReturn.getVideoTotalTime()));
		fileInfo.setSavepath(savePath);
		fileInfo.setFile_type(fileType);
		fileInfo.setAttach_name("."+fileInfo.getBehavior_id()+fileType);

		fileDao.updateFile(fileInfo);//更新数据库

		//加入等待队列
		prepare(fileInfo);
	}

	/**
	 * 获取下载信息失败
	 * @param fileInfo
	 */
	private void getDownloadMessageFial(FileInfo fileInfo){
		//更新数据库
		fileInfo.setProgress_status(4);
		//更新数据库
		fileDao.updateFile(fileInfo);
	}


	/**
	 * 准备中
	 * @param fileInfo
	 */
	public void prepare(FileInfo fileInfo) {
		//加入等待下载list
		downloadList.add(fileInfo);
		//开始
		start();

		//发送增加文件消息
		Intent intent = new Intent();
		intent.setAction(DownloadXxxwActivity.ACTION_ADD_FILE);
		sendBroadcast(intent);
	}

	/**
	 * 开始下载
	 */
	public void start(){

		if (downloadingList.size() < MAX_COUNT) {
			//没有正在等待的就返回
			if (downloadList.size()<=0)
				return;

			List<FileInfo> deleteList = new ArrayList<>();
			for (Map.Entry<Integer, FileInfo> entry : downloadingList.entrySet()) {
				DownloadTask task = mTasks.get(entry.getKey());
				if (task!=null && task.mIsPause) {
					deleteList.add(entry.getValue());
				}
			}
			FileInfo fileInfo = downloadList.get(0);
			downloadingList.put(fileInfo.getId(), fileInfo);

			Log.d(TAG, "初始化下载-->" + " 当前下载中任务数：" + downloadingList.size() );
			Log.d(TAG, "初始化下载-->" + fileInfo.toString() );
			downloadList.remove(fileInfo);
			InitThread initThread = new InitThread(fileInfo);
			DownloadTask.sExecutorService.execute(initThread);



		}
		else {
			Log.d(TAG,"等待下载-->" + " 当前等待任务数："+downloadList.size());
			for (FileInfo fileInfo :downloadList){
				Log.d(TAG,fileInfo.toString());
			}
		}
	}

	/**
	 * 停止
	 * @param fileInfo
	 */
	public void stop(FileInfo fileInfo){
		Log.d(TAG, "task数目：" + mTasks.size() + "");

		DownloadTask task = mTasks.get(fileInfo.getId());
		//如果是下载中的任务
		if (downloadingList.containsKey(fileInfo.getId())){
			if (task != null) {
				// 停止下载任务
				task.mIsPause = true;
			}
		}else {	//如果是在等待列的话
			downloadList.remove(fileInfo);
			Log.d(TAG, "当前等待数：" + downloadList.size());
		}
	}

	/**
	 * 全部开始
	 * @param serverApi
	 * @param student_id
	 */
	public void startAll(ApiService serverApi,String student_id){
		List<FileInfo> fileInfos = fileDao.queryFilesInStatus(student_id);
		for (FileInfo fileInfo :fileInfos){

			//未下载或者等待下载的才能开始
			if (fileInfo.getProgress_status() ==0 || fileInfo.getProgress_status() ==3){
				fileInfo.setProgress_status(3);//设置文件状态为开始
				fileDao.updateFile(fileInfo);
				//如果没有下载信息
				if ("".equals(fileInfo.getUrl())){
					//获取下载信息
					getDownloadMessage(serverApi,student_id,fileInfo);
				}else{
					//加入等待队列
					prepare(fileInfo);
				}
			}

		}
	}

	/**
	 * 停止全部
	 */
	public void stopAll(String student_id){

		//停止正在下载的
		for (Map.Entry<Integer, FileInfo> entry : downloadingList.entrySet()) {
			DownloadTask task = mTasks.get(entry.getKey());
			if (task != null) {
				// 停止下载任务
				task.mIsPause = true;
			}
		}
		downloadingList.clear();
		Log.d(TAG,"初始化下载失败/完成/暂停-->"+"当前下载中任务数:"+downloadingList.size());

		//情况等待的
		downloadList.clear();
		Log.d(TAG, "当前正在等待数：" + downloadList.size());
		List<FileInfo> fileInfos = fileDao.queryFilesInStatus(student_id);
		for (FileInfo fileInfo :fileInfos){
			if (fileInfo.getProgress_status() ==1 || fileInfo.getProgress_status() ==3){
				fileInfo.setProgress_status(0);//设置文件状态为准备
			}
		}
		//批量更新
		fileDao.updateFiles(fileInfos);
	}

	/**
	 * 删除单个文件
	 * @param fileInfo
	 */
	public void delete(FileInfo fileInfo){
		stop(fileInfo);//停止
		downloadingList.remove(fileInfo.getId());
	}

	/**
	 * 是否正在下载
	 */
	public boolean isDownloading(FileInfo fileInfo){
		return downloadingList.containsKey(fileInfo.getId());
	}
	/**
	 * 移除正在下载的任务
	 * @param fileInfo
	 */
	public void removeDownloading(FileInfo fileInfo){
		downloadingList.remove(fileInfo.getId());
		Log.d(TAG,"初始化下载失败/完成/暂停-->"+"当前下载中任务数:"+downloadingList.size());
		start();

	}


	/**
	 * 	從InitThread綫程中獲取FileInfo信息，然後開始下載任務
	 */
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case MSG_INIT:
					FileInfo fileInfo = (FileInfo) msg.obj;
					Log.i(TAG, "INIT:" + fileInfo.toString());
					// 獲取FileInfo對象，開始下載任務
					DownloadTask task = new DownloadTask(DownloadService.this, fileInfo, 2);
					task.download();
					// 把下载任务添加到集合中
					mTasks.put(fileInfo.getId(), task);
					//设置为下载中状态
					fileInfo.setProgress_status(1);
					fileDao.updateFile(fileInfo);
					break;
				case MSG_INIT_ERROR:
					FileInfo fileInfoError = (FileInfo) msg.obj;
					//删除正在下载list的该任务
					removeDownloading(fileInfoError);

					//发送错误信息修改UI
					Intent intent = new Intent();
					intent.setAction(DownloadService.ACTION_ERROR);
					Bundle mBundle = new Bundle();
					mBundle.putSerializable("fileInfo_error",fileInfoError);
					intent.putExtras(mBundle);
					sendBroadcast(intent);

					break;
			}
		};
	};

	/**
	 * 初始化下載綫程，獲得下載文件的信息
	 */
	class InitThread extends Thread {
		private FileInfo mFileInfo = null;

		public InitThread(FileInfo mFileInfo) {
			super();
			this.mFileInfo = mFileInfo;
		}

		@Override
		public void run() {
			HttpURLConnection conn = null;
			RandomAccessFile raf = null;
			try {
				URL url = new URL(mFileInfo.getUrl());
				conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(5 * 1000);
				conn.setRequestMethod("GET");
				int code = conn.getResponseCode();

				int length = -1;
				if (code == HttpURLConnection.HTTP_OK) {	//如果返回成功
					length = conn.getContentLength();
				}else{	//返回失败
					Log.d(TAG,"错误代码："+code);
					Message msg = Message.obtain();
					msg.obj = mFileInfo;
					msg.what = MSG_INIT_ERROR;
					mHandler.sendMessage(msg);
				}
				//如果文件长度为小于0，表示获取文件失败，直接返回
				if (length <= 0) {
					return;
				}
				// 判斷文件路徑是否存在，不存在這創建
				File dir = new File(FinalStr.DOWNLOAD_PATH);
				if (!dir.exists()) {
					dir.mkdir();
				}
				// 創建本地文件
				File file = new File(dir, mFileInfo.getAttach_name());
				raf = new RandomAccessFile(file, "rwd");
				raf.setLength(length);
				// 設置文件長度
				mFileInfo.setFile_size(length);
				// 將FileInfo對象傳遞給Handler
				Message msg = Message.obtain();
				msg.obj = mFileInfo;
				msg.what = MSG_INIT;
				mHandler.sendMessage(msg);
//				msg.setTarget(mHandler);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (conn != null) {
					conn.disconnect();
				}
				try {
					if (raf != null) {
						raf.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			super.run();
		}
	}


	public class LocalBinder extends Binder {
		// 声明一个方法，getService。（提供给客户端调用）
		public DownloadService getService() {
			// 返回当前对象LocalService,这样我们就可在客户端端调用Service的公共方法了
			return DownloadService.this;
		}
	}


	@Override
	public void onDestroy()
	{
		Log.d(TAG,"DownloadService onDestroy");
		stopAll(((ApplicationUtil)getApplication()).getStuid());
		mTasks = null;
		downloadList = null;
		downloadingList = null;
	}
}