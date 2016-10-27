package com.xplatform.base.framework.core.util;

import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.regex.Pattern;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;

/**
 * 
 * description :反射工具类.
 * 
 * 提供调用getter/setter方法, 访问私有变量, 调用私有方法, 获取泛型类型Class, 被AOP过的真实类等工具函数.
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月13日 上午9:02:43
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年5月13日 上午9:02:43
 *
 */
public class ReflectHelper {

	private Class cls;
	/**
	 * 传过来的对象
	 */
	private Object obj;

	/**
	 * 存放get方法
	 */
	private Hashtable<String, Method> getMethods = null;
	/**
	 * 存放set方法
	 */
	private Hashtable<String, Method> setMethods = null;

	/**
	 * 定义构造方法 -- 一般来说是个pojo
	 * 
	 * @param o
	 *            目标对象
	 */

	/**
	 * 日志
	 */
	private static Logger logger = LoggerFactory.getLogger(ReflectHelper.class);

	/**
	 * 私有构造器，工具类无需实例化。
	 */
	private ReflectHelper() {
	}

	public ReflectHelper(Object o) {
		obj = o;
		initMethods();
	}

	/**
	 * 
	 * @desc 初始化
	 */
	public void initMethods() {
		getMethods = new Hashtable<String, Method>();
		setMethods = new Hashtable<String, Method>();
		cls = obj.getClass();
		Method[] methods = cls.getMethods();
		// 定义正则表达式，从方法中过滤出getter / setter 函数.
		String gs = "get(\\w+)";
		Pattern getM = Pattern.compile(gs);
		String ss = "set(\\w+)";
		Pattern setM = Pattern.compile(ss);
		// 把方法中的"set" 或者 "get" 去掉
		String rapl = "$1";
		String param;
		for (int i = 0; i < methods.length; ++i) {
			Method m = methods[i];
			String methodName = m.getName();
			if (Pattern.matches(gs, methodName)) {
				param = getM.matcher(methodName).replaceAll(rapl).toLowerCase();
				getMethods.put(param, m);
			} else if (Pattern.matches(ss, methodName)) {
				param = setM.matcher(methodName).replaceAll(rapl).toLowerCase();
				setMethods.put(param, m);
			} else {
				// com.xplatform.base.framework.core.util.LogUtil.info(methodName + " 不是getter,setter方法！");
			}
		}
	}

	/**
	 * 
	 * @desc 调用set方法
	 */
	public boolean setMethodValue(String property, Object object) {
		Method m = setMethods.get(property.toLowerCase());
		if (m != null) {
			try {
				// 调用目标类的setter函数
				m.invoke(obj, object);
				return true;
			} catch (Exception ex) {
				com.xplatform.base.framework.core.util.LogUtil.info("invoke getter on " + property + " error: "
						+ ex.toString());
				return false;
			}
		}
		return false;
	}

	/**
	 * 
	 * @desc 调用set方法
	 */
	public Object getMethodValue(String property) {
		Object value = null;
		Method m = getMethods.get(property.toLowerCase());
		if (m != null) {
			try {
				/**
				 * 调用obj类的setter函数
				 */
				value = m.invoke(obj, new Object[] {});

			} catch (Exception ex) {
				com.xplatform.base.framework.core.util.LogUtil.info("invoke getter on " + property + " error: "
						+ ex.toString());
			}
		}
		return value;
	}

	/**
	 * 调用Getter方法.
	 * 
	 * @param obj 代理对象
	 * @param propertyName 属性名
	 * @return 代理后的对象
	 */
	public static Object invokeGetter(Object obj, String propertyName) {
		String getterMethodName = "get" + StringUtils.capitalize(propertyName);
		return invokeMethod(obj, getterMethodName, new Class[] {}, new Object[] {});
	}

	/**
	 * 调用Setter方法.使用value的Class来查找Setter方法.
	 * 
	 * @param obj 代理对象
	 * @param propertyName 属性名
	 * @param value 属性值
	 */
	public static void invokeSetter(Object obj, String propertyName, Object value) {
		invokeSetter(obj, propertyName, value, null);
	}

	/**
	 * 调用Setter方法.
	 * 
	 * @param propertyType 用于查找Setter方法,为空时使用value的Class替代.
	 * @param obj 代理对象
	 * @param propertyName 属性名称
	 * @param value 属性值
	 */
	public static void invokeSetter(Object obj, String propertyName, Object value, Class<?> propertyType) {
		Class<?> type = propertyType != null ? propertyType : value.getClass();
		String setterMethodName = "set" + StringUtils.capitalize(propertyName);
		invokeMethod(obj, setterMethodName, new Class[] { type }, new Object[] { value });
	}

	/**
	 * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数.
	 * 
	 * @param obj 目标对象
	 * @param fieldName 属性名称
	 * @return 属性值
	 */
	public static Object getFieldValue(final Object obj, final String fieldName) {
		Field field = getAccessibleField(obj, fieldName);

		if (field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
		}

		Object result = null;
		try {
			result = field.get(obj);
		} catch (IllegalAccessException e) {
			logger.error("不可能抛出的异常{}", e.getMessage());
		}
		return result;
	}

	/**
	 * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
	 * 
	 * @param obj 目标对象
	 * @param fieldName 属性名称
	 * @param value 属性值
	 */
	public static void setFieldValue(final Object obj, final String fieldName, final Object value) {
		Field field = getAccessibleField(obj, fieldName);

		if (field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
		}

		try {
			field.set(obj, value);
		} catch (IllegalAccessException e) {
			logger.error("不可能抛出的异常:{}", e.getMessage());
		}
	}

	/**
	 * 对于被cglib AOP过的对象, 取得真实的Class类型.
	 * 
	 * @param clazz 转换过的Class
	 * @return 原型Class
	 */
	public static Class<?> getUserClass(Class<?> clazz) {
		if (clazz != null && clazz.getName().contains("$$")) {
			Class<?> superClass = clazz.getSuperclass();
			if (superClass != null && !Object.class.equals(superClass)) {
				return superClass;
			}
		}
		return clazz;
	}

	/**
	 * 直接调用对象方法, 无视private/protected修饰符.
	 * 用于一次性调用的情况(单个参数方法).
	 * 
	 * @param obj 目标对象
	 * @param methodName 方法名称
	 * @param parameterTypes 参数类型
	 * @param args 参数值
	 * @return 方法返回值
	 */
	public static Object invokeMethod(final Object obj, final String methodName, final Class<?> parameterType,
			final Object arg) {
		Method method = getAccessibleMethod(obj, methodName, parameterType);
		if (method == null) {
			throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
		}

		try {
			return method.invoke(obj, arg);
		} catch (Exception e) {
			throw convertReflectionExceptionToUnchecked(e);
		}
	}

	/**
	 * 直接调用对象方法, 无视private/protected修饰符.
	 * 用于一次性调用的情况.
	 * 
	 * @param obj 目标对象
	 * @param methodName 方法名称
	 * @param parameterTypes 参数类型列表
	 * @param args 参数值数组
	 * @return 方法返回值
	 */
	public static Object invokeMethod(final Object obj, final String methodName, final Class<?>[] parameterTypes,
			final Object[] args) {
		Method method = getAccessibleMethod(obj, methodName, parameterTypes);
		if (method == null) {
			throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
		}

		try {
			return method.invoke(obj, args);
		} catch (Exception e) {
			throw convertReflectionExceptionToUnchecked(e);
		}
	}

	/**
	 * 循环向上转型, 获取对象的DeclaredField,并强制设置为可访问.
	 * 如向上转型到Object仍无法找到, 返回null.
	 * 
	 * @param obj 目标对象
	 * @param fieldName 属性名称
	 * @return 转型后的类型
	 */
	public static Field getAccessibleField(final Object obj, final String fieldName) {
		Validate.notNull(obj, "object can't be null");
		Validate.notBlank(fieldName, "fieldName can't be blank");
		for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				Field field = superClass.getDeclaredField(fieldName);
				field.setAccessible(true);
				return field;
			} catch (NoSuchFieldException e) {
				//logger.info(e.getMessage());
				//NOSONAR
				// Field不在当前类定义,继续向上转型
			}
		}
		return null;
	}

	/**
	 * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问.
	 * 如向上转型到Object仍无法找到, 返回null.
	 * 
	 * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object... args)
	 * 
	 * @param obj 目标对象
	 * @param methodName 方法名称
	 * @param parameterTypes 参数列表
	 * @return Method对象
	 */
	public static Method getAccessibleMethod(final Object obj, final String methodName,
			final Class<?>... parameterTypes) {
		Validate.notNull(obj, "object can't be null");

		for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				Method method = superClass.getDeclaredMethod(methodName, parameterTypes);

				method.setAccessible(true);

				return method;

			} catch (NoSuchMethodException e) {
				//logger.info(e.getMessage());
				//NOSONAR
				// Method不在当前类定义,继续向上转型
			}
		}
		return null;
	}

	/**
	 * 通过反射, 获得Class定义中声明的父类的泛型参数的类型.
	 * 如无法找到, 返回Object.class.
	 * eg.
	 * public UserDao extends HibernateDao<User>
	 *
	 * @param <T> 泛型类型参数
	 * @param clazz The class to introspect
	 * @return the first generic declaration, or Object.class if cannot be determined
	 */
	public static <T> Class<T> getSuperClassGenricType(final Class clazz) {
		return getSuperClassGenricType(clazz, 0);
	}

	/**
	 * 通过反射, 获得Class定义中声明的父类的泛型参数的类型.
	 * 如无法找到, 返回Object.class.
	 * 
	 * 如public UserDao extends HibernateDao<User,Long>
	 *
	 * @param clazz clazz The class to introspect
	 * @param index the Index of the generic ddeclaration,start from 0.
	 * @return the index generic declaration, or Object.class if cannot be determined
	 */
	public static Class getSuperClassGenricType(final Class clazz, final int index) {

		Type genType = clazz.getGenericSuperclass();

		if (!(genType instanceof ParameterizedType)) {
			logger.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
			return Object.class;
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if (index >= params.length || index < 0) {
			logger.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: "
					+ params.length);
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			logger.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
			return Object.class;
		}

		return (Class) params[index];
	}

	/**
	 * 将反射时的checked exception转换为unchecked exception.
	 * 
	 * @param e checked exception
	 * @return unchecked exception
	 */
	public static RuntimeException convertReflectionExceptionToUnchecked(Exception e) {
		if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException
				|| e instanceof NoSuchMethodException) {
			return new IllegalArgumentException(e);
		} else if (e instanceof InvocationTargetException) {
			return new RuntimeException(((InvocationTargetException) e).getTargetException());
		} else if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		}
		return new RuntimeException("Unexpected Checked Exception.", e);
	}

	public static Class getClass(String className) {
		Class c = null;
		try {
			c = Class.forName(className);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return c;
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年8月4日 下午4:19:37
	 * @Decription 传入任意对象,将对象中String类型的字段,从""空字符串变为null
	 *
	 * @param obj
	 */
	public static void iteratorStringFieldsFromBlankToNull(Object obj) {
		Field[] fields = obj.getClass().getDeclaredFields();

		for (int i = 0; i < fields.length; i++) {
			// 获取属性的名字
			String fieldName = fields[i].getName();
			// 获取属性的类型
			String fieldType = fields[i].getGenericType().toString();

			// 如果属性是字符类型
			if (("class java.lang.String").equals(fieldType)) {
				Object fieldValue = getFieldValue(obj, fieldName);
				if (fieldValue != null) {
					String strValue = fieldValue.toString();
					if ("".equals(strValue)) {
						setFieldValue(obj, fieldName, null);
					}
				}
			}
		}

	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年8月6日 下午4:02:35
	 * @Decription 获得被AOP后动态代理对象的真实对象
	 *
	 * @param proxy
	 * @return
	 * @throws Exception
	 */
	public static <T> T getTargetObject(Object proxy, Class<T> clz) throws Exception {
		if (AopUtils.isJdkDynamicProxy(proxy)) {
			return (T) ((Advised) proxy).getTargetSource().getTarget();
		} else {
			return (T) proxy; // expected to be cglib proxy then, which is simply a specialized class
		}
	}
}