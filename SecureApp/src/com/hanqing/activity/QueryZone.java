package com.hanqing.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.secureapp.R;
import com.hanqing.service.NumberAddressService;

/**
 * 该页面为查询页面
 * @author baohanqing
 *
 */
public class QueryZone extends Activity{

	private EditText et_phone_number;
	private Button but_search;
	private TextView tv_query_result;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.query_zone);
		
		et_phone_number=(EditText) super.findViewById(R.id.et_phone_number);
		but_search=(Button) super.findViewById(R.id.but_search);
		tv_query_result=(TextView) super.findViewById(R.id.tv_query_result);
		
		but_search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				query(v);
				
			}
		});

	}

	private void query(View v){
		
		String number=et_phone_number.getText().toString().trim();
		//如果查询的内容为空，就抖动输入框
		if(number.equals("")){
				Toast.makeText(this, "请输入电话号码", Toast.LENGTH_LONG).show();
		}
		else {
			
			String address=NumberAddressService.getAddress(number);
			tv_query_result.setText("归属地:"+address);
			
		}
		
		


	}
	
	
	
}
