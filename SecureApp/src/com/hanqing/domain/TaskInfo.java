package com.hanqing.domain;

import android.graphics.drawable.Drawable;

/**
 * �����Ǵ����ˣ����̹����domain�б���ʾ����Ϣ
 * @author baohanqing
 *
 */
public class TaskInfo {

	private String name;
	private Drawable icon;
	
	//����id
	private int id;
	
	//�ڴ��С
	private int memory;
	
	private boolean isChecked;
	
	private String packageName;
	
	private boolean isSysApp;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMemory() {
		return memory;
	}

	public void setMemory(int memory) {
		this.memory = memory;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public boolean isSysApp() {
		return isSysApp;
	}

	public void setSysApp(boolean isSysApp) {
		this.isSysApp = isSysApp;
	}
	
	
	
	
	
}
