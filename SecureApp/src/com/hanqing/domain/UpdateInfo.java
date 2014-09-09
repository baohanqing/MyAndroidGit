package com.hanqing.domain;

/**
 * 更新信息的domain类
 * get/set方法
 */


public class UpdateInfo {

	private String version;
	private String description;
	private String apkUrl;
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getApkUrl() {
		return apkUrl;
	}
	public void setApkUrl(String apkUrl) {
		this.apkUrl = apkUrl;
	}

}
