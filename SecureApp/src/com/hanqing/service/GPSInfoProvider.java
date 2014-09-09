package com.hanqing.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

/**
 * 该类用来提供手机的位置信息
 * 这里我们要做成单利模式，因为手机里只有一个gps，所以免得每次都新开一个对象
 * @author baohanqing
 */
public class GPSInfoProvider {

	private static GPSInfoProvider gpsInfoProvider;
	private static Context context;
	private LocationManager locationManager;
	private static MyLocationListener listener;
	
	
	
	private GPSInfoProvider(){
		
	}
	
	public static synchronized GPSInfoProvider getInstance(Context context){
		
		if(gpsInfoProvider==null){
			gpsInfoProvider=new GPSInfoProvider();
			GPSInfoProvider.context=context;
		}
		return gpsInfoProvider;
	}
	
	public String getLocation(){
		
		locationManager=(LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		
		String provider=getBestProvider();
		
		/**
		 * 第一个参数，定位设备的类型，如gps，基站定位
		 * 第二个参数，多长时间更新一次定位信息，太频繁了会耗电，根据自己实际需求设定
		 * 第三个参数，用户移了多少米之后，重新获取一次定位信息
		 * 最后一个是，位置发生变化的回调方法
		 */
		locationManager.requestLocationUpdates(provider, 60000, 50, getListener());
		//locationManager.getAllProviders()，他会把所有支持的定位方式都打出来
		
		SharedPreferences sp=context.getSharedPreferences("config",Context.MODE_PRIVATE);
		String location=sp.getString("location", "");
		
		return location;
	}
	
	
	private String getBestProvider(){
		Criteria criteria=new Criteria();
		
		/*
		 * 定义经度范围
		 * Criteria.ACCURACY_COARSE这个事一般定位
		 * Criteria.ACCURACY_FINE这个事精准定位
		 */
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		
		//设置是不是对海波敏感的
		criteria.setAltitudeRequired(false);
		
		//设置对手机好点了，定位要求越高，越耗电
		criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
		
		//设置对速度变化是不是敏感
		criteria.setSpeedRequired(true);
		
		//设置定位时候，是不是允许与运行商交换数据的开销
		criteria.setCostAllowed(true);
		
		/*
		 * 这个方法用来得到最好的定位方式，他有两个参数
		 * 第一个是Criteria(累死于map集合)就是一些条件，比如说对加速度敏感、对海拔敏感
		 * 第二个参数，如果设置为false,那么得到的可能也有已经关掉了的设备，如果是true，那么只会得到已经打开了的设备
		 */
		
		Log.e("locationManager.getBestProvider(criteria, true)", locationManager.getBestProvider(criteria, true));
		
		return locationManager.getBestProvider(criteria, true);
	}
	
	
	private synchronized MyLocationListener getListener(){
		
		if(listener==null){
			listener=new MyLocationListener();
		}
		return listener;
		
	}
	
	//停止gps
	public void stopGPSListener(){
		if(locationManager!=null){
			locationManager.removeUpdates(getListener());
		}
	}
	
	
	
	//位置监听类
	private class MyLocationListener implements LocationListener{

		@Override
		public void onLocationChanged(Location location) {
			//手机位置发生变化的时候，调用的方法
			String latitude="维度:"+location.getLatitude();
			String longtituede="维度:"+location.getLongitude();
			
			SharedPreferences sp=context.getSharedPreferences("config", Context.MODE_PRIVATE);
			Editor editor=sp.edit();
			
			editor.putString("location", latitude+"--"+longtituede);
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			//设备状态发生改变的时候调用的方法，比如用户把设备打开或者关闭，第二个参数就是设备状态
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			//设备打开的时候调用的方法
			
		}

		@Override
		public void onProviderDisabled(String provider) {
			//设备关闭的时候调用的方法
			
		}

	}
}
