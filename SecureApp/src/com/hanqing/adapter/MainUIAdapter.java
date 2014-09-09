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
 * ����Ϊ�������ṩ������ʾ��Adapter
 * @author baohanqing
 *
 */
public class MainUIAdapter extends BaseAdapter{

	private Context context;
	private LayoutInflater inflater;
	private SharedPreferences sp;
	
	
	//����������ļ���Tab������
		private static final String[] NAMES=new String[]{
			"�ֻ�����","ͨѶ��ʿ","�������","��������",
			"�������","�ֻ�ɱ��","ϵͳ�Ż�","�߼�����","��������"
		};
		
		//������Tab��ͼƬ
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
			 * convertView�൱�ڻ��棬ֻҪ�����ж�һ�����ǲ���Null,��֪���������View��û�л��Ƴ���
			 * ���û�У���ô�����»���
			 * ����У���ô�Ϳ���ʹ�û�����
			 * �����Ϳ��Խ�ʡView���Ƶ�ʱ��
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
				//�������ļ�editName���ж�ȡ���µ�����
				String name=sp.getString("editName", "");
				if(!name.equals("")){
					views.textView.setText(name);
				}
			}
			
			return view;
		}
		
		//һ���������Ҫ���ƵĿؼ�����
		private class MainViews{
			ImageView imageView;
			TextView textView;
		}
		
}
