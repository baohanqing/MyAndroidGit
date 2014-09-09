package com.hanqing.activity;



import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.secureapp.R;
import com.hanqing.domain.UpdateInfo;
import com.hanqing.service.DownloadVersionTask;
import com.hanqing.service.UpdateInfoService;

public class LoadingPage extends Activity{
	
	private TextView tv_version;
	private LinearLayout ll;
	//Ӧ������
	private TextView appName;
	
	//���³������
	UpdateInfo info;
	
	//���ؽ���
	ProgressDialog progressDialog;
	
	
	//�ж��Ƿ���Ҫ����
	//����Bundle key
	private String UPDATE_KEY="isNeed";
	private boolean isNeedUpdate=false;
	
	
	
	//����һ��Handler����
	 private Handler handler=new Handler(){			
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
				case 0x123:
					Bundle bundle=msg.getData();
					isNeedUpdate=bundle.getBoolean(UPDATE_KEY);
					break;
				case 0x124:
					//��ʾҪ������ת����
					Intent jump=new Intent(LoadingPage.this,MainUI.class);
					startActivity(jump);
					overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
					finish();
			}
			
			//�����Ҫ���£���ʾ���´���
			if(isNeedUpdate){
					showUpdateDialog();
			}
			 
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		//���ò���ʾ������
		requestWindowFeature(Window.FEATURE_NO_TITLE);
			
		//����ȫ����ʾ
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		//setContentView(R.layout.load_activity);	
		
		setContentView(R.layout.load_activity);	
		
		final String version=getVersion();
		
		tv_version=(TextView) super.findViewById(R.id.version);
		
		tv_version.setText("@2014 All Rights Resverd "+version);
		
		appName=(TextView) super.findViewById(R.id.app_name);
		//��appName����������Ϊ���ǵ��������
		Typeface tf=Typeface.createFromAsset(getAssets(), "font/DroidSansFallback.ttf");
		appName.setTypeface(tf);
		
		
		ll=(LinearLayout) super.findViewById(R.id.linear);
		
		AlphaAnimation aa=new AlphaAnimation(0.0f, 1.0f);
		aa.setDuration(2000);
		ll.startAnimation(aa);
		
		
		//���ؽ��ȶԻ���
		progressDialog=new ProgressDialog(LoadingPage.this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		//����ʱ���������ʾ����
		progressDialog.setMessage("��������......");
		
		//���������̵߳������ú�ʱ�Ĳ�����������ǽ��ж��Ƿ���Ҫ���������ʱ��������һ���߳�
		new Thread(){

			@Override
			public void run() {
				
				Message msg1=new Message();
				msg1.what=0x123;
				
				Bundle bundle=new Bundle();
				bundle.putBoolean(UPDATE_KEY, isNeedUpdate(version));
				
				msg1.setData(bundle);
				//������Ϣ�Ƿ���Ҫ����
				handler.sendMessage(msg1);
				
			}
		}.start();
		
		
		//Ϊ���ܹ��ٲ����µ�ʱ����ʾ�������棬�����һ����ʱ�Ĳ���
		new Thread(){

			@Override
			public void run() {
				
				try {
					Thread.sleep(3000);
					//������ת����Ϣ
					Message msg2=new Message();
					msg2.what=0x124;
					handler.sendMessage(msg2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
		

	}
	
	//���´��ڵ���ʾ
	public void showUpdateDialog(){
		
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.download_icon);
		builder.setTitle("��������!");
		builder.setMessage(info.getDescription());
		//Ĭ��Ϊtrue��Ϊtrue��ʱ�򣬰����ؼ����˳����������ǲ������˳�
		builder.setCancelable(false);
		
		builder.setPositiveButton("����", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//ȷ������
				
				//����Ƿ���ڴ洢��
				if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
					
					//����һ���ļ�
					File dir=new File(Environment.getExternalStorageDirectory(),"/SecureApp/update");
					
					if(!dir.exists()){
						//��������ڸ�Ŀ¼���Զ�����
						dir.mkdirs();
					}
					
					//��Ҫ�浽�����ֻ���apk·��
					String apkPath=Environment.getExternalStorageDirectory()+"/SecureApp/update/new.apk";
					
					Log.e("test", Environment.getExternalStorageDirectory()+"");
					
					//��ʼ�����߳�
					UpdateTask task=new UpdateTask(info.getApkUrl(), apkPath);
					//��ʾ���ؽ��ȶԻ���
					progressDialog.show();
					
					new Thread(task).start();
					
				}
				else{
					
					Toast.makeText(LoadingPage.this, "SD�������ã������SD��", Toast.LENGTH_LONG).show();
					loadMainUI();
				}
				
			}
		});
		
		builder.setNegativeButton("ȡ��", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				loadMainUI();
			}
		});
		
		builder.create().show();
		
	}
	
	
	//�ж��Ƿ���Ҫ����
	private boolean isNeedUpdate(String version){
		
		
		UpdateInfoService service=new UpdateInfoService(this);
		
		try {
			
			//��������String.xml���е�id��ȡUpdateInfo����
			info=service.getUpdateInfo(R.string.updateServerUrl);
			
			String v=info.getVersion();
			
			Log.e("v", v+"haha"+version);
			
			if(v.equals(version)){
				System.out.println("��ǰ�Ѿ������°汾��");
				return false;
			}
			else{
				System.out.println("�����°汾��");
				return true;
			}
		} catch (Exception e) {
			System.out.println("����ʧ�ܣ���������");
			e.printStackTrace();
			
		}
		return false;
	}
	
	//��ȡ�汾�ŵĴ���
	private String getVersion(){
		
		try {
			PackageManager pm=getPackageManager();
			PackageInfo info=pm.getPackageInfo(getPackageName(), 0);
			
			return info.versionName;
			
		} catch (Exception e) {
			e.printStackTrace();
			return "δ֪�汾";
		}
	
	}
	
	//����������
	private void loadMainUI(){
		Intent intent=new Intent(LoadingPage.this,MainUI.class);
		startActivity(intent);
		finish();
	}
	
	//��װ������ɵ�apk�ļ�
	private void install(File file){
		Intent intent=new Intent();
		//ACTION_MAIN������û������������Զ����ļ�
		intent.setAction(Intent.ACTION_VIEW);
		//ָ�����ļ�����Ϊapk
		intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
		finish();
		startActivity(intent);
	}
	
	
	/*
	 * ���ذ�װ���߳�
	 * ����DownloadVersionTask���е�getFile�������������������ص��ֻ�����,����װ
	 */
	class UpdateTask implements Runnable{

		private String path;
		private String filePath;
		
		public UpdateTask(String path,String filePath){
			this.path=path;
			this.filePath=filePath;
		}
		
		@Override
		public void run() {
			
			try{
				File file=DownloadVersionTask.getFile(path, filePath, progressDialog);
				//�������֮��ر���ʾ�ĶԻ���
				progressDialog.dismiss();
				
				Log.e("file",file+"");
				
				//��ʼ��װ�ļ�
				install(file);
				
			}
			catch(Exception e){
				e.printStackTrace();
				progressDialog.dismiss();
				System.out.println("���ظ���ʧ��");
				loadMainUI();
			}
		}

	}	
}
