package com.hxc.httprequest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.hxc.httprequest.entity.HttpClientResult;

/**
 * @Description 发送httpClient的post get put delete 请求 
 * 2019年5月17日  上午11:05:50
 * @Author Huangxiaocong
 */
public class HttpClientUtils {
	
	//编码格式，发送编码格式统一用UTF-8
	private static final String ENCODING = "UTF-8";
	//设置连接超时时间  单位毫秒
	private static final int CONNECT_TIMEOUT = 6000;
	//请求获取数据的超时时间 单位毫秒
	private static final int REQUEST_TIMEOUT = 6000;
	
	
	public static HttpClientResult doPut(String url) throws ClientProtocolException, IOException {
		return doPut(url, null);
	}
	
	public static HttpClientResult doPut(String url, Map<String, String> params) throws ClientProtocolException, IOException {
		//创建httpClient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();
		
		HttpPut httpPut = new HttpPut(url);
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(CONNECT_TIMEOUT)
				.setSocketTimeout(REQUEST_TIMEOUT)
				.build();
		httpPut.setConfig(requestConfig);
		
		packageParam(params, httpPut);
		CloseableHttpResponse httpResponse = null;
		try {
			return getHttpClientResult(httpResponse, httpClient, httpPut);
		} finally {
			release(httpResponse, httpClient);
		}
		
	}
	
	public static HttpClientResult doDelete(String url) throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpDelete httpDelete = new HttpDelete(url);
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(CONNECT_TIMEOUT)
				.setSocketTimeout(REQUEST_TIMEOUT)
				.build();
		httpDelete.setConfig(requestConfig);
		
		CloseableHttpResponse httpResponse = null;
		try {
			return getHttpClientResult(httpResponse, httpClient, httpDelete);
		} finally {
			release(httpResponse, httpClient);
		}
	}
	/**
	 * 发送delete请求；带请求参数
	 * @param url
	 * @param params
	 * @return
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @Author Huangxiaocong 2019年5月17日 上午11:29:19
	 */
	public static HttpClientResult doDelete(String url, Map<String, String> params)
			throws ClientProtocolException, URISyntaxException, IOException {
		if(params == null) {
			params = new HashMap<>();
		}
		params.put("_method", "delete");
		return doPost(url, params);
	}
	/**
	 * 根据post请求 不带请求头和请求参数
	 * @param url
	 * @return
	 * @throws IOException 
	 * @throws URISyntaxException 
	 * @throws ClientProtocolException 
	 * @Author Huangxiaocong 2019年5月17日 上午10:36:20
	 */
	public static HttpClientResult doPost(String url) throws ClientProtocolException, URISyntaxException, IOException {
		return doPost(url, null, null);
	}
	/**
	 * 根据url和参数的post请求 不带请求头
	 * @param url
	 * @param params
	 * @return
	 * @throws IOException 
	 * @throws URISyntaxException 
	 * @throws ClientProtocolException 
	 * @Author Huangxiaocong 2019年5月17日 上午10:36:54
	 */
	public static HttpClientResult doPost(String url, Map<String, String> params) throws ClientProtocolException, URISyntaxException, IOException {
		return doPost(url, null, params);
	}
	/**
	 * 根据地址、请求头、参数的post请求 
	 * @param url
	 * @param headers
	 * @param params
	 * @return
	 * @throws URISyntaxException 
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @Author Huangxiaocong 2019年5月17日 上午10:36:58
	 */
	public static HttpClientResult doPost(String url, Map<String, String> headers, Map<String, String> params) 
			throws URISyntaxException, ClientProtocolException, IOException {
		//1.创建httpClient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();
		
		HttpPost httpPost = new HttpPost(url);
		//设置请求连接的参数
		RequestConfig requestConfig = RequestConfig.custom().
				setConnectTimeout(CONNECT_TIMEOUT)
				.setSocketTimeout(REQUEST_TIMEOUT)
//				.setConnectionRequestTimeout(REQUEST_TIMEOUT) //从连接池中获取资源
				.build();
		httpPost.setConfig(requestConfig);
		
		//设置请求头
		packageHeader(headers, httpPost);
		//设置请求参数
		packageParam(params, httpPost);
		//执行请求
		CloseableHttpResponse httpResponse = null;
		try {
			return getHttpClientResult(httpResponse, httpClient, httpPost);
		} finally {
			//释放资源
			release(httpResponse, httpClient);
		}
		
	}
	public static HttpClientResult doGet(String url) throws URISyntaxException, ClientProtocolException, IOException {
		return doGet(url, null, null);
	}

	public static HttpClientResult doGet(String url, Map<String, String> params) 
			throws URISyntaxException, ClientProtocolException, IOException {
		return doGet(url, null, params);
	}
	/**
	 * 发送get请求，带请求头和请求参数
	 * @param url 请求地址
	 * @param headers 请求头
	 * @param params 请求参数
	 * @return
	 * @throws URISyntaxException 
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @Author Huangxiaocong 2019年5月16日 下午5:51:16
	 */
	public static HttpClientResult doGet(String url, Map<String, String> headers, Map<String, String> params) throws URISyntaxException, ClientProtocolException, IOException {
		//1.创建httpClient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();
		//2.创建访问地址
		URIBuilder uriBuilder = new URIBuilder(url);
		if(params != null) {
			Set<Entry<String, String>> paramEntry = params.entrySet();
			for(Entry<String, String> entry : paramEntry) {
				uriBuilder.setParameter(entry.getKey(), entry.getValue());
			}
		}
		//3.创建httpGet对象 并设置相应的值
		HttpGet httpGet = new HttpGet(uriBuilder.build());
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(CONNECT_TIMEOUT)	//设置连接超时时间，单位毫秒。
				.setSocketTimeout(REQUEST_TIMEOUT)	//请求获取数据的超时时间（即响应时间），如果访问一个接口，多少时间内无人返回数据，就直接放弃此次调用。
//				.setConnectionRequestTimeout() //设置从connect Manager(连接池）获取Connection超时时间
				.build();
		httpGet.setConfig(requestConfig);
		//4.设置请求头
		packageHeader(headers, httpGet);
		//5.创建httpResponse对象
		CloseableHttpResponse httpResponse = null;
		//6.执行请求并获得响应结果
		try {
			return getHttpClientResult(httpResponse, httpClient, httpGet);
		} finally {
			//7.释放资源
			release(httpResponse, httpClient);
		}
	}
	/**
	 * 封装请求头 
	 * @param params
	 * @param httpMethod
	 * @Author Huangxiaocong 2019年5月17日 上午9:01:01
	 */
	public static void packageHeader(Map<String, String> headers, HttpRequestBase httpMethod) {
		if(headers != null) {
			Set<Entry<String, String>> entrySet = headers.entrySet();
			for(Entry<String, String> entry : entrySet) {
				httpMethod.setHeader(entry.getKey(), entry.getValue());
			}
		}
	}
	/**
	 * 封装请求参数
	 * @param params
	 * @param httpMethod
	 * @throws UnsupportedEncodingException 
	 * @Author Huangxiaocong 2019年5月17日 上午9:07:50
	 */
	public static void packageParam(Map<String, String> params, HttpEntityEnclosingRequestBase httpMethod) throws UnsupportedEncodingException {
		//封装请求参数
		if(params != null) {
			List<NameValuePair> nvps = new ArrayList<>();
			Set<Entry<String, String>> entrySet = params.entrySet();
			for(Entry<String, String> entry : entrySet) {
				nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			httpMethod.setEntity(new UrlEncodedFormEntity(nvps, ENCODING));
		}
	}
	/**
	 * 获得响应结果
	 * @param httpResponse
	 * @param httpClient
	 * @param httpMethod
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @Author Huangxiaocong 2019年5月17日 上午9:37:16
	 */
	public static HttpClientResult getHttpClientResult(CloseableHttpResponse httpResponse,
			CloseableHttpClient httpClient, HttpRequestBase httpMethod) throws ClientProtocolException, IOException {
		//执行请求
		httpResponse = httpClient.execute(httpMethod);
		//获取返回结果
		if(httpResponse != null && httpResponse.getStatusLine() != null) {
			String content = "";
			if(httpResponse.getEntity() != null) {
				content = EntityUtils.toString(httpResponse.getEntity(), ENCODING);
			}
			return new HttpClientResult(httpResponse.getStatusLine().getStatusCode(), content);
		}
		return new HttpClientResult(HttpStatus.SC_INTERNAL_SERVER_ERROR);
	}
	/**
	 * 释放资源
	 * @param httpResponse
	 * @param httpClient
	 * @Author Huangxiaocong 2019年5月17日 上午9:50:07
	 */
	public static void release(CloseableHttpResponse httpResponse, CloseableHttpClient httpClient) {
		try {
			if(httpResponse != null) {
				httpResponse.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(httpClient != null) {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
