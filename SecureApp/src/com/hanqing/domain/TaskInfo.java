package com.hanqing.domain;

import android.graphics.drawable.Drawable;

/**
 * 该类是代表了，进程管理的domain列表显示的信息
 * @author baohanqing
 *
 */
public class TaskInfo {

	private String name;
	private Drawable icon;
	
	//进程id
	private int id;
	
	//内存大小
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
