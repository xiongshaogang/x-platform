package com.xplatform.base.framework.core.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.Validate;

/**
 * 
 * description :* 封装各种格式的编码解码工具类.
 * 
 * 1.Commons-Codec的 hex/base64 编码
 * 2.自行编写的，将long进行base62编码以缩短其长度
 * 3.Commons-Lang的xml/html escape
 * 4.JDK提供的URLEncoder
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月30日 上午9:50:08
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年5月30日 上午9:50:08
 *
 */
public class EncodeUtil {
	
	/**
	 * 转换参数字符串
	 */
	private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

	
	
	/**
	 * Base62编码转换参数
	 */
	private static final int BASE62_CODE_DIGIT = 62;

	/**
	 * 私有构造器，工具类无需实例化。
	 */
	private EncodeUtil() {
	}

	/**
	 * Hex编码, byte[] -> String.
	 * 
	 * @param input byte[]
	 * @return 字符串
	 */
	public static char[] encodeHex(byte[] input) {
		return Hex.encodeHex(input);
	}

	/**
	 * Hex解码, String -> byte[].
	 * 
	 * @param input 需要解码的字符串
	 * @return byte[]
	 */
	public static byte[] decodeHex(String input) {
		try {
			return Hex.decodeHex(input.toCharArray());
		} catch (DecoderException e) {
			throw new IllegalStateException("Hex Decoder exception", e);
		}
	}

	/**
	 * Base64编码, byte[] -> String.
	 * 
	 * @param input byte[]
	 * @return 字符串
	 */
	public static String encodeBase64(byte[] input) {
		//return Base64.encodeBase64String(input);
		return null;
	}

	/**
	 * Base64编码, URL安全(将Base64中的URL非法字符'+'和'/'转为'-'和'_', 见RFC3548).
	 * 
	 * @param input byte[]
	 * @return 字符串
	 */
	public static String encodeUrlSafeBase64(byte[] input) {
		//return Base64.encodeBase64URLSafeString(input);
		return null;
	}

	/**
	 * Base64解码, String->byte[].
	 * 
	 * @param input 需要解码的字符串
	 * @return byte[]
	 */
	public static byte[] decodeBase64(byte[] input) {
		return Base64.decodeBase64(input);
	}

	/**
	 * Base62(0_9A_Za_z)编码数字, long -> String.
	 * 
	 * @param num long
	 * @return 字符串
	 */
	public static String encodeBase62(long num) {
		return alphabetEncode(num, BASE62_CODE_DIGIT);
	}

	/**
	 * Base62(0_9A_Za_z)解码数字, String -> long.
	 * 
	 * @param str 需要解码的字符串
	 * @return long
	 */
	public static long decodeBase62(String str) {
		return alphabetDecode(str, BASE62_CODE_DIGIT);
	}

	/**
	 * 设置编码规则
	 * 
	 * @param num 数字
	 * @param base 规则
	 * @return 字符串
	 */
	private static String alphabetEncode(long num, int base) {
		num = Math.abs(num);
		StringBuilder sb = new StringBuilder();
		for (; num > 0; num /= base) {
			sb.append(ALPHABET.charAt((int) (num % base)));
		}

		return sb.toString();
	}

	/**
	 * 解码规则
	 * 
	 * @param str 需要解码的字符串
	 * @param base 字符串的解码规则
	 * @return long
	 */
	private static long alphabetDecode(String str, int base) {
		Validate.notBlank(str);

		long result = 0;
		for (int i = 0; i < str.length(); i++) {
			result += ALPHABET.indexOf(str.charAt(i)) * Math.pow(base, i);
		}

		return result;
	}

	/**
	 *  Html 转码.
	 * 
	 * @param html Html格式的字符串
	 * @return 转码后的Html字符串
	 */
	public static String escapeHtml(String html) {
		return StringEscapeUtils.escapeHtml4(html);
	}

	/**
	 * Html 解码.
	 * 
	 * @param htmlEscaped 编码后的Html字符串
	 * @return 解码后的Html字符串
	 */
	public static String unescapeHtml(String htmlEscaped) {
		return StringEscapeUtils.unescapeHtml4(htmlEscaped);
	}

	/**
	 * Xml 转码.
	 * 
	 * @param xml xml字符串
	 * @return 转码后的Xml字符串
	 */
	public static String escapeXml(String xml) {
		return StringEscapeUtils.escapeXml(xml);
	}

	/**
	 * Xml 解码.
	 * 
	 * @param xmlEscaped 需要解码的Xml字符串
	 * @return 解码后的Xml字符串
	 */
	public static String unescapeXml(String xmlEscaped) {
		return StringEscapeUtils.unescapeXml(xmlEscaped);
	}

	/**
	 * Csv 转码.
	 * 
	 * @param csv Csv字符串
	 * @return 转码后的Csv字符串
	 */
	public static String escapeCsv(String csv) {
		return StringEscapeUtils.escapeCsv(csv);
	}

	/**
	 * Csv 解码.
	 * 
	 * @param csvEscaped 需要解码的Csv字符串
	 * @return 解码后的Csv字符串
	 */
	public static String unescapeCsv(String csvEscaped) {
		return StringEscapeUtils.unescapeCsv(csvEscaped);
	}

	/**
	 * URL 编码, Encode默认为UTF-8. 
	 * 
	 * @param part URL字符串
	 * @return 编码后的URL字符串
	 */
	public static String urlEncode(String part) {
		String str="";
		try {
			 str=URLEncoder.encode(part, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * URL 解码, Encode默认为UTF-8. 
	 * 
	 * @param part 需要解码的URL字符串
	 * @return 解码后的URL字符串
	 */
	public static String urlDecode(String part) {
		String str="";
		try {
			 str=URLDecoder.decode(part, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}
}