package findboom.android.com.findboom.service;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.RemoteViews;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import findboom.android.com.findboom.R;

public class DownLoadService extends IntentService {
	public DownLoadService() {
		super("");

	}

	private static final String TAG = DownLoadService.class.getSimpleName();

	private NotificationManager notificationManager;
	private Notification notification;
	private RemoteViews rViews;
	private File file;

	protected void onHandleIntent(Intent intent) {
		Bundle bundle = intent.getExtras();

		// 获得下载文件的url
		final String downloadUrl = bundle.getString("url");
		// 设置文件下载后的保存路径，保存在SD卡根目录的Download文件夹
		File dirs = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download");
		// 检查文件夹是否存在，不存在则创建
		if (!dirs.exists()) {
			dirs.mkdir();
		}
		file = new File(dirs, "FindBoom.apk");
		// 设置Notification
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notification = new Notification(R.mipmap.ic_logo, "版本更新下载", System.currentTimeMillis());

		// 加载Notification的布局文件
		rViews = new RemoteViews(getPackageName(), R.layout.downloadfile_layout);
		// 设置下载进度条
		rViews.setProgressBar(R.id.downloadFile_pb, 100, 0, false);
		rViews.setTextViewText(R.id.progress_percent_text, "扫雷高手正在下载0%");
		notification.contentView = rViews;
		notificationManager.notify(100, notification);
		// 开始下载
		new Thread() {
			public void run() {
				downloadFile(downloadUrl, file);
			};
		}.start();

	}

	private int fileLength, downloadLength;

	private void downloadFile(String downloadUrl, File file) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			Log.e(TAG, "找不到保存下载文件的目录");
			e.printStackTrace();
		}
		InputStream ips = null;
		try {
			URL url = new URL(downloadUrl);
			HttpURLConnection huc = (HttpURLConnection) url.openConnection();
			huc.setRequestMethod("GET");
			huc.setReadTimeout(10000);
			huc.setConnectTimeout(3000);
			fileLength = Integer.valueOf(huc.getHeaderField("Content-Length"));
			ips = huc.getInputStream();
			// 拿到服务器返回的响应码
			int hand = huc.getResponseCode();
			if (hand == 200) {
				// 开始检查下载进度
				handler.post(run);
				// 建立一个byte数组作为缓冲区，等下把读取到的数据储存在这个数组
				byte[] buffer = new byte[8192];
				int len = 0;
				while ((len = ips.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
					downloadLength = downloadLength + len;
				}
			} else {
				Log.e(TAG, "服务器返回码" + hand);
			}

		} catch (Exception e) {
			notificationManager.cancel(100);
			handler.removeCallbacks(run);

			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
				if (ips != null) {
					ips.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 判断该服务是否运行
	 * 
	 * @param mContext
	 * @param className
	 * @return
	 */
	public static boolean isServiceRunning(Context mContext, String className) {
		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(30);
		if (!(serviceList.size() > 0)) {
			return false;
		}
		for (int i = 0; i < serviceList.size(); i++) {
			if (serviceList.get(i).service.getClassName().equals(className) == true) {
				isRunning = true;
				break;
			}
		}
		return isRunning;
	}

	public void onDestroy() {
		// 移除定時器
		handler.removeCallbacks(run);
		// 取消状态栏中的进度
		notificationManager.cancel(100);
		super.onDestroy();
	}

	// 定时器，每隔一段时间检查下载进度，然后更新Notification上的ProgressBar
	private Handler handler = new Handler();
	private Runnable run = new Runnable() {
		public void run() {
			rViews = new RemoteViews(getPackageName(), R.layout.downloadfile_layout);
			rViews.setProgressBar(R.id.downloadFile_pb, 100, downloadLength * 100 / fileLength, false);
			rViews.setTextViewText(R.id.progress_percent_text, "夜嗅正在下载" + (downloadLength * 100 / fileLength) + "%");
			notification.contentView = rViews;
			notificationManager.notify(100, notification);
			handler.postDelayed(run, 1000);
			if (downloadLength >= fileLength) {
				// 移除通知栏
				notificationManager.cancel(100);

				// 打开安装的界面
				/*Intent intent = new Intent();

				Bundle bundle = new Bundle();
				bundle.putString("versionOnServer", newVersion);// 最新的版本
				bundle.putString("versionContent", newVersionContent);// 更新的内容
				bundle.putString("downloadFileLength", newFileLength);// APK文件的大小
				bundle.putString("filePath", file.toString());// APK文件的路径
				intent.putExtras(bundle);

				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				// intent.setClass(getApplicationContext(),InstallApkActivity.class);
				startActivity(intent);*/

				// 广播出去，由广播接收器来处理下载完成的文件
				/*
				 * Intent sendIntent = new Intent(DOWN_SERVICE); //
				 * 把下载好的文件的保存地址加进Intent sendIntent.putExtra("downloadFile",
				 * file.getPath()); sendBroadcast(sendIntent);
				 */

				/**
				 * 安装APK界面
				 * 
				 */
				// 通过Intent安装APK文件

				Intent installIntent = new Intent(Intent.ACTION_VIEW);
				installIntent.setDataAndType(Uri.parse("file://" + file.toString()),
						"application/vnd.android.package-archive");
				installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(installIntent);

				handler.removeCallbacks(run);
			}
		}
	};

}
