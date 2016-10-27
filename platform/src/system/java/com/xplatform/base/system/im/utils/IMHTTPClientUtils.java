package com.xplatform.base.system.im.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xplatform.base.platform.common.def.ConfigConst;
import com.xplatform.base.system.im.vo.Credential;
import com.xplatform.base.system.im.vo.Token;

/**
 * HTTPClient 工具类
 * 
 * @author Lynch 2014-09-15
 *
 */
public class IMHTTPClientUtils {
	/** METHOD_DELETE value:GET */
	public static String METHOD_GET = "GET";

	/** METHOD_DELETE value:POST */
	public static String METHOD_POST = "POST";

	/** METHOD_DELETE value:PUT */
	public static String METHOD_PUT = "PUT";

	/** METHOD_DELETE value:DELETE */
	public static String METHOD_DELETE = "DELETE";

	private static final JsonNodeFactory factory = new JsonNodeFactory(false);

	/**
	 * Send SSL Request
	 * 
	 * @return
	 * @throws Exception 
	 */
	public static ObjectNode sendHTTPRequest(String urlStr, Credential credential, Object dataBody, String method) throws Exception {

		HttpClient httpClient = getClient(true);

		ObjectNode resObjectNode = factory.objectNode();

		try {
			URL url = new URL(urlStr);
			HttpResponse response = null;

			if (method.equals(METHOD_POST)) {
				HttpPost httpPost = new HttpPost(url.toURI());

				if (credential != null) {
					Token.applyAuthentication(httpPost, credential);
				}
				if (dataBody != null) {
					httpPost.setEntity(new StringEntity(dataBody.toString(), "UTF-8"));
				}

				response = httpClient.execute(httpPost);
			} else if (method.equals(METHOD_PUT)) {
				HttpPut httpPut = new HttpPut(url.toURI());
				if (credential != null) {
					Token.applyAuthentication(httpPut, credential);
				}
				if (dataBody != null) {
					httpPut.setEntity(new StringEntity(dataBody.toString(), "UTF-8"));
				}

				response = httpClient.execute(httpPut);
			} else if (method.equals(METHOD_GET)) {

				HttpGet httpGet = new HttpGet(url.toURI());
				if (credential != null) {
					Token.applyAuthentication(httpGet, credential);
				}

				response = httpClient.execute(httpGet);

			} else if (method.equals(METHOD_DELETE)) {
				HttpDelete httpDelete = new HttpDelete(url.toURI());

				if (credential != null) {
					Token.applyAuthentication(httpDelete, credential);
				}

				response = httpClient.execute(httpDelete);
			}

			HttpEntity entity = response.getEntity();
			if (null != entity) {
				String responseContent = EntityUtils.toString(entity, "UTF-8");
				EntityUtils.consume(entity);

				ObjectMapper mapper = new ObjectMapper();
				JsonFactory factory = mapper.getJsonFactory();
				JsonParser jp = factory.createJsonParser(responseContent);

				resObjectNode = mapper.readTree(jp);
				resObjectNode.put("statusCode", response.getStatusLine().getStatusCode());
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			httpClient.getConnectionManager().shutdown();
		}

		return resObjectNode;
	}

	/**
	 * DownLoadFile whit Jersey
	 * 
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 * @throws IOException
	 */
	public static File downLoadFile(String urlStr, Credential credential, List<NameValuePair> headers, File localPath) {

		HttpClient httpClient = getClient(true);

		try {
			URL url = new URL(urlStr);
			HttpGet httpGet = new HttpGet(url.toURI());

			if (credential != null) {
				Token.applyAuthentication(httpGet, credential);
			}
			for (NameValuePair header : headers) {
				httpGet.addHeader(header.getName(), header.getValue());
			}

			HttpResponse response = httpClient.execute(httpGet);

			HttpEntity entity = response.getEntity();
			InputStream in = entity.getContent();
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

		return localPath;
	}

	/**
	 * UploadFile whit Jersey
	 * 
	 * @return
	 */
	public static ObjectNode uploadFile(String urlStr, File file, Credential credential, List<NameValuePair> headers) throws RuntimeException {
		HttpClient httpClient = getClient(true);

		ObjectNode resObjectNode = factory.objectNode();

		try {
			URL url = new URL(urlStr);
			HttpPost httpPost = new HttpPost(url.toURI());

			if (credential != null) {
				Token.applyAuthentication(httpPost, credential);
			}
			for (NameValuePair header : headers) {
				httpPost.addHeader(header.getName(), header.getValue());
			}

			MultipartEntity mpEntity = new MultipartEntity();
			ContentBody cbFile = new FileBody(file, MediaType.APPLICATION_OCTET_STREAM_VALUE);
			mpEntity.addPart("file", cbFile);
			httpPost.setEntity(mpEntity);

			HttpResponse response = httpClient.execute(httpPost);

			HttpEntity entity = response.getEntity();

			if (null != entity) {
				String responseContent = EntityUtils.toString(entity, "UTF-8");
				EntityUtils.consume(entity);

				ObjectMapper mapper = new ObjectMapper();
				JsonFactory factory = mapper.getJsonFactory();
				JsonParser jp = factory.createJsonParser(responseContent);

				resObjectNode = mapper.readTree(jp);
				resObjectNode.put("statusCode", response.getStatusLine().getStatusCode());
			}
		} catch (Exception e) {
			throw new RuntimeException();
		} finally {
			httpClient.getConnectionManager().shutdown();
		}

		return resObjectNode;
	}

	/**
	 * Create a httpClient instance
	 * 
	 * @param isSSL
	 * @return HttpClient instance
	 */
	public static HttpClient getClient(boolean isSSL) {

		HttpClient httpClient = new DefaultHttpClient();
		if (isSSL) {
			X509TrustManager xtm = new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};

			try {
				SSLContext ctx = SSLContext.getInstance("TLS");

				ctx.init(null, new TrustManager[] { xtm }, null);

				SSLSocketFactory socketFactory = new SSLSocketFactory(ctx);

				httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, socketFactory));

			} catch (Exception e) {
				throw new RuntimeException();
			}
		}

		return httpClient;
	}

	//
	// public static URL getURL(String path) {
	// URL url = null;
	//
	// try {
	// url = new URL(ConfigConst.API_HTTP_SCHEMA, ConfigConst.API_SERVER_HOST,
	// "/" + path);
	// } catch (MalformedURLException e) {
	// e.printStackTrace();
	// }
	//
	// return url;
	// }
	//
	// public static String getAppKeyURL() {
	// URL url = null;
	//
	// try {
	// url = new URL(ConfigConst.API_HTTP_SCHEMA, ConfigConst.API_SERVER_HOST,
	// "/" + path);
	// } catch (MalformedURLException e) {
	// e.printStackTrace();
	// }
	//
	// return url;
	// }

	/**
	 * Check illegal String
	 * 
	 * @param regex
	 * @param str
	 * @return
	 */
	public static boolean match(String regex, String str) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);

		return matcher.lookingAt();
	}
}
