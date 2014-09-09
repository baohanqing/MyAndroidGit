package com.hanqing.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.secureapp.R;
/**
 * 该类为主界面提供数据显示的Adapter
 * @author baohanqing
 *
 */
public class MainUIAdapter extends BaseAdapter{

	private Context context;
	private LayoutInflater inflater;
	private SharedPreferences sp;
	
	
	//定义主界面的几个Tab的内容
		private static final String[] NAMES=new String[]{
			"手机防盗","通讯卫士","软件管理","流量管理",
			"任务管理","手机杀毒","系统优化","高级工具","设置中心"
		};
		
		//主界面Tab的图片
		private static final int[] TAB_IMAGE=new int[]{
			R.drawable.fangdao_icon,R.drawable.tongxun,R.drawable.ruanjian_icon,R.drawable.liuliang_icon,
			R.drawable.renwu_icon,R.drawable.shandu_icon,R.drawable.youhua,R.drawable.gaoji_icon,R.drawable.shezhi_icon
		};

		public MainUIAdapter(Context context){
			this.context=context;
			this.inflater=LayoutInflater.from(this.context);
			sp=context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		
		
		
		@Override
		public int getCount() {
			
			return NAMES.length;
		}

		@Override
		public Object getItem(int position) {
			
			return NAMES[position];
		}

		@Override
		public long getItemId(int position) {
			
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			/**
			 * convertView相当于缓存，只要我们判断一下它是不是Null,就知道现在这个View有没有绘制出来
			 * 如果没有，那么就重新绘制
			 * 如果有，那么就可以使用缓存了
			 * 这样就可以节省View绘制的时间
			 */
			
			View view;
			MainViews views;
			
			if(convertView==null){
				
				views=new MainViews();
				
				view=inflater.inflate(R.layout.activity_main_item, null);
				views.imageView=(ImageView) view.findViewById(R.id.img_item);
				views.textView=(TextView) view.findViewById(R.id.text_item);
				
				views.imageView.setImageResource(TAB_IMAGE[position]);
				views.textView.setText(NAMES[position]);
				
				view.setTag(views);
				
			}
			else{
				view=convertView;
				views=(MainViews) view.getTag();
				views.imageView=(ImageView) view.findViewById(R.id.img_item);
				views.textView=(TextView) view.findViewById(R.id.text_item);
				
				views.imageView.setImageResource(TAB_IMAGE[position]);
				views.textView.setText(NAMES[position]);
				
			}
			
			if(position==0){
				//从配置文件editName当中读取最新的名字
				String name=sp.getString("editName", "");
				if(!name.equals("")){
					views.textView.setText(name);
				}
			}
			
			return view;
		}
		
		//一个存放所有要绘制的控件的类
		private class MainViews{
			ImageView imageView;
			TextView textView;
		}
		
}
