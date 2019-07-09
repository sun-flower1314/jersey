package com.hxc.httprequest.entity;

import java.io.Serializable;

public class HttpClientResult implements Serializable {

	private static final long serialVersionUID = 1314L;
	/**
	 * 响应状态码
	 */
	private int code;
	/**
	 * 响应数据
	 */
	private String content;
	
	public HttpClientResult(int code) {
		super();
		this.code = code;
	}
	
	public HttpClientResult(int code, String content) {
		super();
		this.code = code;
		this.content = content;
	}

	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
}
