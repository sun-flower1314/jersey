package com.hxc.upload;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class JerseyDemo {
	
	
	public void test() throws IOException {
		Client client = new Client();
		String url = "";
		WebResource resource = client.resource(url);
		String path = "";
		byte[] bt = FileUtils.readFileToByteArray(new File(path));
		resource.put(String.class, bt);
		System.out.println("===========");
		
		
	}
	
	public static void main(String[] args) throws IOException {
		
		Client client = new Client();
		//服务器的地址 + 生成的图片的名
		String url = "http://localhost:8090/image-web/upload/bbb.jpg";
		
		WebResource resource = client.resource(url);
		
		String path = "D:\\Workspace\\upload\\1659a802882e346fe8f955e01c620c4b.png";
		
		byte[] readFileToByteArray = FileUtils.readFileToByteArray(new File(path));
		resource.put(String.class, readFileToByteArray);
		System.out.println("发送成功");
		
		
		
	}
}
