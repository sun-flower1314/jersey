package com.hxc.httprequest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class HttpClientTest {
	
	public static void main(String[] args) {
		
	}
	
	@Test
	public void doPostWay() throws UnsupportedEncodingException {
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("name=" + URLEncoder.encode("&", "utf-8"));
		sBuffer.append("&");
		sBuffer.append("age=24");
		HttpPost httpPost = new HttpPost("" + "?" + sBuffer);
		httpPost.setHeader("Content-Type", "application/json;charset=utf-8");
		
//		User user = new User();
//		user.setName("潘晓婷");
//		user.setAge(18);
//		user.setGender("女");
//		user.setMotto("姿势要优雅~");
		// 我这里利用阿里的fastjson，将Object转换为json字符串;
		// (需要导入com.alibaba.fastjson.JSON包)
//		String jsonString = JSON.toJSONString(user);
//		StringEntity stringEntity = new StringEntity(jsonString, "utf-8");
//		httpPost.setEntity(stringEntity);
		CloseableHttpResponse httpResponse = null;
		try {
			httpResponse = httpClient.execute(httpPost);
		} catch (IOException e) {
			e.printStackTrace();
		}
		HttpEntity entity = httpResponse.getEntity();
		//对数据的处理
		try {
			System.out.println(EntityUtils.toString(entity));
		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void doGetTestWayTwo() throws URISyntaxException {
		//
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		//参数
		URI uri = null;
		
		//将参数放入键值对中，再放入集合中
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("name", "&"));
		params.add(new BasicNameValuePair("age", "18"));
		//设置url信息，并将参数集合放入url；
		uri = new URIBuilder().setScheme("http").setHost("localhost")
				.setPort(8080).setPath("").setParameters(params)
				.build();
		
		//...
	}
	
	@Test
	public void doGetTestOne() throws Exception {
		//获得http客户端 
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		//如果存在参数，则对参数的传递和处理
		StringBuffer params = new StringBuffer();
		//params.append("name=" + URLEncoder.encode("&", "utf-8"));
		//params.append("&");
		params.append("id=841-757-83,841-757-133");
		//创建get请求
		HttpGet httpGet = new HttpGet("http://localhost:8888/aibizhall-plat/playControl/getEndPlayTime" + "?" + params);
		//配置信息
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(5000)	//设置连接超时时间（单位毫秒）
				.setConnectionRequestTimeout(5000)	//设置连接超时时间（单位毫秒）
				.setSocketTimeout(5000)	//socket读写超时时间
				.setRedirectsEnabled(true)	//设置是否允许重定向（默认为true）
				.build();
		httpGet.setConfig(requestConfig);
		//响应模型
		CloseableHttpResponse response = null;
		//由客户端执行Get请求
		response = httpClient.execute(httpGet);
		//
		try {
			HttpEntity entity = response.getEntity();
			System.out.println("响应状态为：" + response.getStatusLine());
			if(entity != null) {
				System.out.println("响应内容长度为：" + entity.getContentLength());
				String responseContent = EntityUtils.toString(entity);
				System.out.println("响应内容为：" + responseContent);
				JSONObject parseObject = JSON.parseObject(responseContent);
				Set<Entry<String, Object>> entrySet = parseObject.entrySet();
				for(Entry<String, Object> entry : entrySet) {
					System.out.println(entry.getKey() + " ===> " + entry.getValue());
				}
				
			}
		} finally {
			HttpClientUtils.release(response, httpClient);
		}
		
		
		
	}
}
