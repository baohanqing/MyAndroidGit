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
	//应用名字
	private TextView appName;
	
	//更新程序对象
	UpdateInfo info;
	
	//下载进度
	ProgressDialog progressDialog;
	
	
	//判断是否需要更新
	//更新Bundle key
	private String UPDATE_KEY="isNeed";
	private boolean isNeedUpdate=false;
	
	
	
	//创建一个Handler对象
	 private Handler handler=new Handler(){			
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
				case 0x123:
					Bundle bundle=msg.getData();
					isNeedUpdate=bundle.getBoolean(UPDATE_KEY);
					break;
				case 0x124:
					//表示要进行跳转操作
					Intent jump=new Intent(LoadingPage.this,MainUI.class);
					startActivity(jump);
					overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
					finish();
			}
			
			//如果需要更新，显示更新窗口
			if(isNeedUpdate){
					showUpdateDialog();
			}
			 
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		//设置不显示标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
			
		//设置全屏显示
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		//setContentView(R.layout.load_activity);	
		
		setContentView(R.layout.load_activity);	
		
		final String version=getVersion();
		
		tv_version=(TextView) super.findViewById(R.id.version);
		
		tv_version.setText("@2014 All Rights Resverd "+version);
		
		appName=(TextView) super.findViewById(R.id.app_name);
		//将appName的字体设置为我们导入的字体
		Typeface tf=Typeface.createFromAsset(getAssets(), "font/DroidSansFallback.ttf");
		appName.setTypeface(tf);
		
		
		ll=(LinearLayout) super.findViewById(R.id.linear);
		
		AlphaAnimation aa=new AlphaAnimation(0.0f, 1.0f);
		aa.setDuration(2000);
		ll.startAnimation(aa);
		
		
		//下载进度对话框
		progressDialog=new ProgressDialog(LoadingPage.this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		//下载时候的文字提示内容
		progressDialog.setMessage("正在下载......");
		
		//避免在主线程当中启用耗时的操作，因此我们将判断是否需要更新这个耗时操作单开一个线程
		new Thread(){

			@Override
			public void run() {
				
				Message msg1=new Message();
				msg1.what=0x123;
				
				Bundle bundle=new Bundle();
				bundle.putBoolean(UPDATE_KEY, isNeedUpdate(version));
				
				msg1.setData(bundle);
				//发送信息是否需要更新
				handler.sendMessage(msg1);
				
			}
		}.start();
		
		
		//为了能够再不更新的时候显示启动界面，这里加一个延时的操作
		new Thread(){

			@Override
			public void run() {
				
				try {
					Thread.sleep(3000);
					//发送跳转的消息
					Message msg2=new Message();
					msg2.what=0x124;
					handler.sendMessage(msg2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
		

	}
	
	//更新窗口的显示
	public void showUpdateDialog(){
		
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.download_icon);
		builder.setTitle("升级提醒!");
		builder.setMessage(info.getDescription());
		//默认为true，为true的时候，按返回键能退出，这里我们不让他退出
		builder.setCancelable(false);
		
		builder.setPositiveButton("更新", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//确定更新
				
				//检测是否存在存储卡
				if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
					
					//创建一个文件
					File dir=new File(Environment.getExternalStorageDirectory(),"/SecureApp/update");
					
					if(!dir.exists()){
						//如果不存在该目录，自动创建
						dir.mkdirs();
					}
					
					//将要存到本地手机的apk路径
					String apkPath=Environment.getExternalStorageDirectory()+"/SecureApp/update/new.apk";
					
					Log.e("test", Environment.getExternalStorageDirectory()+"");
					
					//开始下载线程
					UpdateTask task=new UpdateTask(info.getApkUrl(), apkPath);
					//显示下载进度对话框
					progressDialog.show();
					
					new Thread(task).start();
					
				}
				else{
					
					Toast.makeText(LoadingPage.this, "SD卡不可用，请插入SD卡", Toast.LENGTH_LONG).show();
					loadMainUI();
				}
				
			}
		});
		
		builder.setNegativeButton("取消", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				loadMainUI();
			}
		});
		
		builder.create().show();
		
	}
	
	
	//判断是否需要更新
	private boolean isNeedUpdate(String version){
		
		
		UpdateInfoService service=new UpdateInfoService(this);
		
		try {
			
			//根据我们String.xml当中的id获取UpdateInfo对象
			info=service.getUpdateInfo(R.string.updateServerUrl);
			
			String v=info.getVersion();
			
			Log.e("v", v+"haha"+version);
			
			if(v.equals(version)){
				System.out.println("当前已经是最新版本！");
				return false;
			}
			else{
				System.out.println("发现新版本！");
				return true;
			}
		} catch (Exception e) {
			System.out.println("更新失败，请检查网络");
			e.printStackTrace();
			
		}
		return false;
	}
	
	//获取版本号的代码
	private String getVersion(){
		
		try {
			PackageManager pm=getPackageManager();
			PackageInfo info=pm.getPackageInfo(getPackageName(), 0);
			
			return info.versionName;
			
		} catch (Exception e) {
			e.printStackTrace();
			return "未知版本";
		}
	
	}
	
	//调到主界面
	private void loadMainUI(){
		Intent intent=new Intent(LoadingPage.this,MainUI.class);
		startActivity(intent);
		finish();
	}
	
	//安装下载完成的apk文件
	private void install(File file){
		Intent intent=new Intent();
		//ACTION_MAIN会根据用户的数据类型自动打开文件
		intent.setAction(Intent.ACTION_VIEW);
		//指定打开文件类型为apk
		intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
		finish();
		startActivity(intent);
	}
	
	
	/*
	 * 下载安装的线程
	 * 调用DownloadVersionTask当中的getFile方法，将升级程序下载到手机当中,并安装
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
				//下载完成之后关闭显示的对话框
				progressDialog.dismiss();
				
				Log.e("file",file+"");
				
				//开始安装文件
				install(file);
				
			}
			catch(Exception e){
				e.printStackTrace();
				progressDialog.dismiss();
				System.out.println("下载更新失败");
				loadMainUI();
			}
		}

	}	
}
