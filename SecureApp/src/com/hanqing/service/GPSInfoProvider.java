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
 * ���������ṩ�ֻ���λ����Ϣ
 * ��������Ҫ���ɵ���ģʽ����Ϊ�ֻ���ֻ��һ��gps���������ÿ�ζ��¿�һ������
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
		 * ��һ����������λ�豸�����ͣ���gps����վ��λ
		 * �ڶ����������೤ʱ�����һ�ζ�λ��Ϣ��̫Ƶ���˻�ĵ磬�����Լ�ʵ�������趨
		 * �������������û����˶�����֮�����»�ȡһ�ζ�λ��Ϣ
		 * ���һ���ǣ�λ�÷����仯�Ļص�����
		 */
		locationManager.requestLocationUpdates(provider, 60000, 50, getListener());
		//locationManager.getAllProviders()�����������֧�ֵĶ�λ��ʽ�������
		
		SharedPreferences sp=context.getSharedPreferences("config",Context.MODE_PRIVATE);
		String location=sp.getString("location", "");
		
		return location;
	}
	
	
	private String getBestProvider(){
		Criteria criteria=new Criteria();
		
		/*
		 * ���徭�ȷ�Χ
		 * Criteria.ACCURACY_COARSE�����һ�㶨λ
		 * Criteria.ACCURACY_FINE����¾�׼��λ
		 */
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		
		//�����ǲ��ǶԺ������е�
		criteria.setAltitudeRequired(false);
		
		//���ö��ֻ��õ��ˣ���λҪ��Խ�ߣ�Խ�ĵ�
		criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
		
		//���ö��ٶȱ仯�ǲ�������
		criteria.setSpeedRequired(true);
		
		//���ö�λʱ���ǲ��������������̽������ݵĿ���
		criteria.setCostAllowed(true);
		
		/*
		 * ������������õ���õĶ�λ��ʽ��������������
		 * ��һ����Criteria(������map����)����һЩ����������˵�Լ��ٶ����С��Ժ�������
		 * �ڶ����������������Ϊfalse,��ô�õ��Ŀ���Ҳ���Ѿ��ص��˵��豸�������true����ôֻ��õ��Ѿ����˵��豸
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
	
	//ֹͣgps
	public void stopGPSListener(){
		if(locationManager!=null){
			locationManager.removeUpdates(getListener());
		}
	}
	
	
	
	//λ�ü�����
	private class MyLocationListener implements LocationListener{

		@Override
		public void onLocationChanged(Location location) {
			//�ֻ�λ�÷����仯��ʱ�򣬵��õķ���
			String latitude="ά��:"+location.getLatitude();
			String longtituede="ά��:"+location.getLongitude();
			
			SharedPreferences sp=context.getSharedPreferences("config", Context.MODE_PRIVATE);
			Editor editor=sp.edit();
			
			editor.putString("location", latitude+"--"+longtituede);
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			//�豸״̬�����ı��ʱ����õķ����������û����豸�򿪻��߹رգ��ڶ������������豸״̬
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			//�豸�򿪵�ʱ����õķ���
			
		}

		@Override
		public void onProviderDisabled(String provider) {
			//�豸�رյ�ʱ����õķ���
			
		}

	}
}
