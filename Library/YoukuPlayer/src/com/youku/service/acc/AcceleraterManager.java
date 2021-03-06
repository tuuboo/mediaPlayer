package com.youku.service.acc;

import java.util.List;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;

import com.baseproject.utils.Logger;


public class AcceleraterManager {

	private static final String TAG = "Accelerater_Manager";
	
	private Context mContext;

	private static AcceleraterManager mInstance;
	
	private IAcceleraterService mIAcc;
	
	private Intent mIntent;
	
	private ServiceConnection sConnection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			Logger.d(TAG, "onServiceDisconnected() called");
			mIAcc = null;
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Logger.d(TAG, "onServiceConnected() called");
			mIAcc = IAcceleraterService.Stub.asInterface(service);
		}
	};
	
	public synchronized static AcceleraterManager getInstance(Context context) {
		
		if (mInstance == null) {
			Logger.d(TAG, "getInstance()");
			mInstance = new AcceleraterManager(context);
		}
		
		return mInstance;
	}
	
	private AcceleraterManager(Context context) {
		this.mContext = context;
	}
	
	public void bindService() {
		Logger.d(TAG, "bindService()");
		Intent intent = new Intent(mContext, AcceleraterService.class);
		mContext.bindService(intent, sConnection, Context.BIND_AUTO_CREATE);
	}
	
	public void unbindService() {
		Logger.d(TAG, "unbindService()");
		mContext.unbindService(sConnection);
	}
	
	public void startService() {
		Logger.d(TAG, "startService()");
		
		mIntent = new Intent(mContext, AcceleraterService.class);
		mIntent.setAction(AcceleraterService.ACTION_START_SERVER);
		mContext.startService(mIntent);
	}
	
	public void stopService() {
		Logger.d(TAG, "stopService()");
		
		if (isServiceRunning(mContext, "com.youku.service.acc.AcceleraterService")) {
			Logger.d(TAG, "AcceleraterService is running!");
			Intent intent = new Intent(mContext, AcceleraterService.class);
			intent.setAction(AcceleraterService.ACTION_STOP_SERVER);
			mContext.startService(intent);
		} else {
			Logger.d(TAG, "AcceleraterService is not run!");
		}
		
	}
	
	public void stop() {
		Logger.d(TAG, "stop()");
		
		try {
			if (mIAcc != null) {
				mIAcc.stop();
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public int pause() {
		Logger.d(TAG, "pause()");
		int flag = -1;
		try {
			if (mIAcc != null) {
				flag = mIAcc.pause();
			}
		} catch (RemoteException e) {
			e.printStackTrace();
			return -1;
		}
		
		return flag;
	}
	
	public int resume() {
		Logger.d(TAG, "resume()");
		int flag = -1;
		try {
			if (mIAcc != null) {
				flag = mIAcc.resume();
			}
		} catch (RemoteException e) {
			e.printStackTrace();
			return -1;
		}
		
		return flag;
	}
	
	public int getHttpProxyPort() {
		Logger.d(TAG, "getHttpProxyPort()");
		int port = -1;
		try {
			if (mIAcc != null) {
				port = mIAcc.getHttpProxyPort();
			}
		} catch (RemoteException e) {
			e.printStackTrace();
			return -1;
		}
		
		return port;
	}
	
	public String getAccPort() {
		Logger.d(TAG, "getAccPort()");
		String accPort = "";
		try {
			if (mIAcc != null) {
				accPort = mIAcc.getAccPort();
			}
		} catch (RemoteException e) {
			e.printStackTrace();
			return "";
		}
		
		return accPort;
	}
	
	public String getAccVersionName() {
		Logger.d(TAG, "getAccVersionName()");
		String versionName = "";
		try {
			if (mIAcc != null) {
				versionName = mIAcc.getVersionName();
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		if (TextUtils.isEmpty(versionName))
			versionName = "0.0.0.0";
		return versionName;
	}
	
	public int getAccVersionCode() {
		Logger.d(TAG, "getAccVersionCode()");
		int versionCode = -1;
		try {
			if (mIAcc != null) {
				versionCode = mIAcc.getVersionCode();
			}
		} catch (RemoteException e) {
			e.printStackTrace();
			return versionCode;
		}
		
		return versionCode;
	}
	
	
	public int isAvailable() {
		Logger.d(TAG, "isAvailable()");
		int available = -1;
		
		try {
			if (mIAcc != null) {
				available = mIAcc.isAvailable();
			}
		} catch (RemoteException e) {
			e.printStackTrace();
			return -1;
		}
		
		return available;
	}

	public boolean canPlayWithP2P() {
		Logger.d(TAG, "canPlayWithP2P()");
		try {
			if (mIAcc != null) {
				return mIAcc.canPlayWithP2P();
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean getDownloadSwitch() {
		Logger.d(TAG, "getDownloadSwitch()");
		boolean downloadSwitch = false;
		
		try {
			if (mIAcc != null) {
				downloadSwitch = mIAcc.getDownloadSwitch();
			}
		} catch (RemoteException e) {
			e.printStackTrace();
			return false;
		}
		
		return downloadSwitch;
	}
	
	public boolean getPlaySwitch() {
		Logger.d(TAG, "getDownloadSwitch()");
		boolean playSwitch = false;
		
		try {
			if (mIAcc != null) {
				playSwitch = mIAcc.getPlaySwitch();
			}
		} catch (RemoteException e) {
			e.printStackTrace();
			return false;
		}
		
		return playSwitch;
	}
	
	/*public static boolean isACCEnable() {
		Logger.d(TAG, "isACCEnable()");
		if (Build.VERSION.SDK_INT >= 9 && MediaPlayerProxy.isUplayerSupported()
				&& Utils.getMemoryClass(com.baseproject.utils.Profile.mContext) >= 47
				&& hasSDCard()) {
			return true;
		}
		return false;
	}
	
	*//** Returns 是否有SD卡 *//*
	public static boolean hasSDCard() {
		Logger.d(TAG, "hasSDCard()");
		return Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}*/
	
	/**
     * 用来判断服务是否运行.
     * @param context
     * @param className 判断的服务名字
     * @return true 在运行 false 不在运行
     */
	public static boolean isServiceRunning(Context mContext, String className) {
		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager
				.getRunningServices(100);
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
	
}
