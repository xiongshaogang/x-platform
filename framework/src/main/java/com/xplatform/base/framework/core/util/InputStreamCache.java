package com.xplatform.base.framework.core.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 缓存InputStream，以便InputStream的重复利用
 */
public class InputStreamCache {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(InputStreamCache.class);

	/**
	 * 将InputStream中的字节保存到ByteArrayOutputStream中。
	 */
	private ByteArrayOutputStream byteArrayOutputStream = null;

	public InputStreamCache(InputStream inputStream) {
		if (BeanUtils.isEmpty(inputStream))
			return;

		byteArrayOutputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len;
		try {
			while ((len = inputStream.read(buffer)) > -1) {
				byteArrayOutputStream.write(buffer, 0, len);
			}
			byteArrayOutputStream.flush();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public InputStream getInputStream() {
		if (BeanUtils.isEmpty(byteArrayOutputStream))
			return null;

		return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
	}
}
