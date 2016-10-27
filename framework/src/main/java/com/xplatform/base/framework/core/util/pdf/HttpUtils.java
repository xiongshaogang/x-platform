package com.xplatform.base.framework.core.util.pdf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.CertificateException;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.xplatform.base.framework.core.util.FileUtils;

/**
 * Http请求工具类
 * @author Administrator
 *
 */
public class HttpUtils {
	private static ThreadSafeClientConnManager cm = null; // 多连接的线程安全的管理器
	private static int MAX_TOTAL = 500; // 最大连接数
	private static int defaultMaxConnection = 100; // 默认最大 主机连接数
	public final static int CONNECT_TIMEOUT = 10000; // 连接超时时间
	public final static int SOCKET_TIMEOUT = 10000; // 读取数据超时时间
	public final static String USER_AGENT = "Mozilla/5.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)";
	public final static String ERROR_RESULT = null;
	public final static int RETRY_NUMS = 1;// 网络请求次数

	static {
		// 设置访问协议
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
		schemeRegistry.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));
		cm = new ThreadSafeClientConnManager(schemeRegistry);
		try {
//			cm.setMaxTotal(MAX_TOTAL);
			// 每条通道的并发连接数设置（连接池）
//			cm.setDefaultMaxPerRoute(defaultMaxConnection);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	public static HttpClient getHttpClient() {
		HttpParams params = new BasicHttpParams();
		// HTTP 协议的版本,1.1/1.0/0.9
		params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		/* 连接超时 */
		HttpConnectionParams.setConnectionTimeout(params, CONNECT_TIMEOUT);
		/* 请求超时 */
		HttpConnectionParams.setSoTimeout(params, SOCKET_TIMEOUT);

		return new DefaultHttpClient(cm, params);
	}

	/**
     * Trust every server - dont check for any certificate
     */
	public static void trustAllHosts() {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[] {};
			}

			public void checkClientTrusted(X509Certificate[] chain, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] chain, String authType) {
			}
		} };

		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
	/**
	 * @author xiaqiang
	 * @createtime 2014年8月21日 上午9:23:54
	 * @Decription 为了解决中文乱码问题,将InputStream流转化为String
	 *
	 * @return
	 * @throws IOException
	 */
	public static String getResponseBodyAsString(InputStream inputStream) throws IOException {
		String reqBody = "";
		BufferedReader read = null;
		try {
			read = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			String b = "";
			StringBuffer sb = new StringBuffer();
			while ((b = read.readLine()) != null) {
				sb.append(b);
			}
			reqBody = sb.toString();
		} catch (IOException e) {
		} finally {
			if (read != null) {
				try {
					read.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return reqBody;
	}

	/**
	 * 通用Httpclient请求方法
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static String request(String url, List<NameValuePair> params) {
		// 从1开始进行请求重试
		return request(url, params, 0);
	}

	public static String request(String url) {
		// 从1开始进行请求重试
		return request(url, null, 0);
	}

	public static String request(String url, List<NameValuePair> params, int retryNums) {
		try {
			// 超过请求次数返回
			if (retryNums >= RETRY_NUMS) {
				return ERROR_RESULT;
			}
			String result = "";
			HttpClient client = getHttpClient();
			HttpPost post = new HttpPost(url);
			post.setHeader("User-Agent", USER_AGENT);

			if (params == null) {
				// params = new ArrayList<NameValuePair>();
			} else {
				HttpEntity httpEntity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
				post.setEntity(httpEntity);
			}
			HttpResponse httpResponse = client.execute(post);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(httpResponse.getEntity());
			} else {
				result = ERROR_RESULT;
			}
			System.out.println(result.toString());
			return result;
		} catch (Exception e) {
			retryNums++;
			return request(url, params, retryNums);
		}
	}
	
	public static String requestByString(String url, String stringParams) {
		// 只请求1次
		return requestByString(url, stringParams, 0);
	}
	
	/**
	 * url+字符串参数的形式发起请求
	 * @param url
	 * @param stringParams
	 * @param retryNums
	 * @return
	 */
	public static String requestByString(String url, String stringParams, int retryNums) {
		try {
			// 超过请求次数返回
			if (retryNums >= RETRY_NUMS) {
				return ERROR_RESULT;
			}
			String result = "";
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);
			post.setHeader("User-Agent", USER_AGENT);

			StringEntity se = new StringEntity(stringParams, "UTF-8");
			post.setEntity(se);

			HttpResponse httpResponse = client.execute(post);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(httpResponse.getEntity());
			} else {
				result = ERROR_RESULT;
			}
			System.out.println(result.toString());
			return result;
		} catch (Exception e) {
			retryNums++;
			return requestByString(url, stringParams, retryNums);
		}
	}
	
	
	public static String requestWithParams(String url, Map<String, Object> params, int retryNums) {
		try {
			// 超过请求次数返回
			if (retryNums >= RETRY_NUMS) {
				return ERROR_RESULT;
			}
			String result = "";
			HttpClient client = getHttpClient();
			HttpPost post = new HttpPost(url);
			post.setHeader("User-Agent", USER_AGENT);

			if (params == null) {
				// params = new ArrayList<NameValuePair>();
			} else {
				List<NameValuePair> nameValuePairParams=parseMapToNameValuePair(params);
				HttpEntity httpEntity = new UrlEncodedFormEntity(nameValuePairParams, HTTP.UTF_8);
				post.setEntity(httpEntity);
			}
			HttpResponse httpResponse = client.execute(post);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(httpResponse.getEntity());
			} else {
				result = ERROR_RESULT;
			}
			System.out.println(result.toString());
			return result;
		} catch (Exception e) {
			retryNums++;
			return requestWithParams(url, params, retryNums);
		}
	}
	
	/**
	 * DownLoadFile whit Jersey
	 * 
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 * @throws IOException
	 */
	public static File downLoadFile(URL url, List<NameValuePair> headers, String localPath) {
		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpGet httpGet = new HttpGet(url.toURI());
			if (headers != null) {
				for (NameValuePair header : headers) {
					httpGet.addHeader(header.getName(), header.getValue());
				}
			}
			trustAllHosts();
			HttpResponse response = httpClient.execute(httpGet);

			HttpEntity entity = response.getEntity();
			InputStream in = entity.getContent();

			FileUtils.createFolder(localPath, true);
			FileOutputStream fos = new FileOutputStream(localPath);

			byte[] buffer = new byte[1024];
			int len1 = 0;
			while ((len1 = in.read(buffer)) != -1) {
				fos.write(buffer, 0, len1);
			}

			fos.close();
		} catch (Exception e) {
			throw new RuntimeException();
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		return new File(localPath);
	}

	public static void release() {
		if (cm != null) {
			cm.shutdown();
		}
	}

	public static Boolean isMoblie(HttpServletRequest request) {
		String agent = (request.getHeader("user-agent")).toLowerCase().trim();

		if (agent.indexOf("micromessenger") != -1) {
			return false;
		}
		if (agent == "" || agent.indexOf("mobile") != -1 || agent.indexOf("mobi") != -1 || agent.indexOf("nokia") != -1 || agent.indexOf("samsung") != -1
				|| agent.indexOf("sonyericsson") != -1 || agent.indexOf("mot") != -1 || agent.indexOf("blackberry") != -1 || agent.indexOf("lg") != -1
				|| agent.indexOf("htc") != -1 || agent.indexOf("j2me") != -1 || agent.indexOf("ucweb") != -1 || agent.indexOf("opera mini") != -1
				|| agent.indexOf("mobi") != -1 || agent.indexOf("android") != -1 || agent.indexOf("iphone") != -1|| agent.indexOf("ios") != -1|| agent.indexOf("ipad") != -1) {
			// 终端可能是手机
			return true;
		}
		return false;
	}

	/**
	 * 将map转换为List<BasicNameValuePair>数据
	 * 
	 * @param map
	 * @return
	 */
	public static List<NameValuePair> parseMapToNameValuePair(Map<String, Object> map) {
		List<NameValuePair> postData = new ArrayList<NameValuePair>();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			postData.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
		}
		return postData;
	}

	public static void main(String[] args) {
		String string1 = URLEncoder.encode("http://192.168.0.104:8080/jdy_client/product/getProductByProid/402880ea4b0b8a12014b0b9039c80007-bank-50");
		System.out.println(string1);
	}
}