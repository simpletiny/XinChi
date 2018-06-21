package com.xinchi.common;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.opensymphony.xwork2.ActionContext;

/**
 * 工具类
 * 
 * @author
 * 
 */
public class ToolsUtil {
	static ApplicationContext ctx = null;
	static String springConfigPath = "exam/spring.xml";

	public static ApplicationContext getCtx() {
		return ctx;
	}

	public static void setCtx(ApplicationContext ctx) {
		ToolsUtil.ctx = ctx;
	}

	public static String getSpringConfigPath() {
		return springConfigPath;
	}

	public static void setSpringConfigPath(String springConfigPath) {
		ToolsUtil.springConfigPath = springConfigPath;
	}

	/**
	 * web方式下根据名称获取bean
	 * 
	 * @param beanName
	 * @return
	 */
	/*
	 * public static Object getBean(String beanName){ HttpServletRequest request =
	 * ServletActionContext.getRequest(); ServletContext
	 * sc=request.getSession().getServletContext(); ApplicationContext ac =
	 * WebApplicationContextUtils.getRequiredWebApplicationContext(sc); Object obj =
	 * ac.getBean(beanName); return obj; }
	 */
	/**
	 * 根据名称获取bean(通过BeanFactory获得)
	 * 
	 * @param beanName
	 * @return
	 */
	public static Object getSpringBean(String beanName) {
		return getBeanFactory().getBean(beanName);
	}

	/**
	 * 根据类型获取bean
	 * 
	 * @param beanName
	 * @return
	 */
	public static Map<String, Object> getSpringBeansOfType(Class className) {
		return getBeanFactory().getBeansOfType(className);
	}

	/**
	 * 根据类型获取所有bean的名称
	 * 
	 * @param beanName
	 * @return
	 */
	public static String[] getSpringBeanNamesOfType(Class className) {
		return getBeanFactory().getBeanNamesForType(className);
	}

	/**
	 * 根据配置文件返回BeanFactory
	 * 
	 * @param xmlfile
	 * @return
	 */
	public static ApplicationContext getBeanFactory() {
		ActionContext actionContext = ActionContext.getContext();
		if (ctx == null) {
			if (actionContext == null) {
				// 从接口过来，没有request
				ctx = ctx == null ? new ClassPathXmlApplicationContext(springConfigPath) : ctx;
			} else {
				// 从action过来，有request
				if (ctx == null) {
					HttpServletRequest request = ServletActionContext.getRequest();
					ServletContext sc = request.getSession().getServletContext();
					ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(sc);
				}
			}
		}
		return ctx;
	}

	/**
	 * 数据比较
	 * 
	 * @param confData
	 *            ：配置文件中取出的数据
	 * @param dbData
	 *            ：数据库查询出来的数据
	 * @return:true:数据相等 false:数据不相等
	 */
	public static boolean compare(String confData, Object dbData) {
		if (confData.toLowerCase().indexOf("int") != -1) {// 整型
			confData = confData.substring(4, confData.length() - 1);
			// dbData中本身就是Integer类型，如果先转成String类型再转为Integer类型会报异常
			if (Integer.valueOf(confData).compareTo((Integer) dbData) == 0) {
				return true;
			} else {
				return false;
			}
		} else if (confData.toLowerCase().indexOf("cur") != -1) {// 货币
			confData = confData.substring(4, confData.length() - 1);
			// 因为dbData是BigDecimal类型，所以强制转化为String类型时会爆出异常，所以改成以下代码
			if (new BigDecimal(confData).compareTo((BigDecimal) dbData) == 0) {
				return true;
			} else {
				return false;
			}
		} else if (confData.toLowerCase().indexOf("num") != -1) {// 数字
			confData = confData.substring(4, confData.length() - 1);
			if (new BigDecimal(confData).compareTo((BigDecimal) dbData) == 0) {
				return true;
			} else {
				return false;
			}
		} else {
			// 因为有时候从数据库中取出的数据是null，这个时候如果进行trim操作会抛出空指针异常。
			if (dbData != null) {
				dbData = ((String) dbData).trim();
			}
			if (confData.equals(dbData)) {
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * 数据比较
	 * 
	 * @param confData
	 *            ：配置文件中取出的数据
	 * @param dbData
	 *            ：数据库查询出来的数据
	 * @param query_type
	 *            ：dbData与confData比较方式
	 * @return:true:数据相等 false:数据不相等
	 * @throws Exception
	 */
	public static boolean compare(String confData, Object dbData, String query_type) throws Exception {
		confData = confData.trim();
		// 匹配数据类型
		compareDataType(confData, dbData);
		if (confData.toLowerCase().indexOf("int") != -1) {// 整型
			// 数据库中数据不为空，则调用方法正常比较
			if (dbData != null && !"".equals(dbData)) {
				confData = confData.substring(4, confData.length() - 1);
				// dbData中本身就是Integer类型，如果先转成String类型再转为Integer类型会报异常
				return int_compare(Integer.parseInt(confData), (Integer) dbData, query_type);
			} else {
				// 数据库中取出的数据为空，如果正确答案也是空字符串则判断正确
				if (confData == null || "".equals(confData)) {
					return true;
				} else {
					return false;
				}
			}
		} else if (confData.toLowerCase().indexOf("cur") != -1) {// 货币
			// 数据库中数据不为空，则调用方法正常比较
			if (dbData != null && !"".equals(dbData)) {
				confData = confData.substring(4, confData.length() - 1);
				return cur_compare(new BigDecimal(confData), (BigDecimal) dbData, query_type);
			} else {
				// 数据库中取出的数据为空，如果正确答案也是空字符串则判断正确
				if (confData == null || "".equals(confData)) {
					return true;
				} else {
					return false;
				}
			}
		} else if (confData.toLowerCase().indexOf("num") != -1) {// 数字
			// 数据库中数据不为空，则调用方法正常比较
			if (dbData != null && !"".equals(dbData)) {
				confData = confData.substring(4, confData.length() - 1);
				return num_compare(new BigDecimal(confData), (BigDecimal) dbData, query_type);
			} else {
				// 数据库中取出的数据为空，如果正确答案也是空字符串则判断正确
				if (confData == null || "".equals(confData)) {
					return true;
				} else {
					return false;
				}
			}
		} else {
			// 数据库中数据不为空，则调用方法正常比较
			if (dbData != null && !"".equals(dbData)) {
				dbData = ((String) dbData).trim();
				return str_compare(confData, (String) dbData, query_type);
				// 数据库中取出的数据为空，如果正确答案也是空字符串则判断正确
			} else {
				if ("".equals(confData) || confData == null) {
					return true;
				} else {
					return false;
				}
			}
		}
	}

	/**
	 * 字符串数据比较
	 * 
	 * @param query_type
	 *            ：1 等于、2大于、3小于、4大于等于、5小于等于、6不等于、7like、8like and、9like or
	 * @return:true:数据相等 false:数据不相等
	 */
	public static boolean str_compare(String confData, String dbData, String query_type) {

		if (query_type.equals("1")) {
			if (dbData.equals(confData)) {
				return true;
			} else {
				return false;
			}
		} else if (query_type.equals("7")) {
			// 问好为特殊符号，需用正则表达式
			String[] confDataArr = confData.split("[?]");
			for (String confData2 : confDataArr) {
				if (!dbData.contains(confData2)) {
					return false;
				}
			}
			if (!dbData.contains(confData.trim())) {
				return false;
			}
			return true;
		} else if (query_type.equals("8")) {
			// 问好为特殊符号，需用正则表达式
			String[] confDataArr = confData.split("[?]");
			for (String confData2 : confDataArr) {
				if (!dbData.contains(confData2)) {
					return false;
				}
			}
			return true;
		} else if (query_type.equals("9")) {
			// 问好为特殊符号，需用正则表达式
			String[] confDataArr = confData.split("[?]");
			for (String confData2 : confDataArr) {
				if (dbData.contains(confData2)) {
					return true;
				}
			}
			return false;
		} else {
			// 字符串类型数据只能进行等值和模糊查询，其它查询方式为错误
			return false;
		}
	}

	/**
	 * num数据比较
	 * 
	 * @param query_type
	 *            ：1 等于、2大于、3小于、4大于等于、5小于等于、6不等于、7like
	 * @return:true:数据相等 false:数据不相等
	 */
	public static boolean num_compare(BigDecimal confData, BigDecimal dbData, String query_type) {

		// 判断相等
		if (query_type.equals("1")) {
			if (dbData.compareTo(confData) == 0) {
				return true;
			} else {
				return false;
			}
		} else if (query_type.equals("2")) {
			if (dbData.compareTo(confData) > 0) {
				return true;
			} else {
				return false;
			}
		} else if (query_type.equals("3")) {
			if (dbData.compareTo(confData) < 0) {
				return true;
			} else {
				return false;
			}
		} else if (query_type.equals("4")) {
			if (dbData.compareTo(confData) >= 0) {
				return true;
			} else {
				return false;
			}
		} else if (query_type.equals("5")) {
			if (dbData.compareTo(confData) <= 0) {
				return true;
			} else {
				return false;
			}
		} else if (query_type.equals("6")) {
			if (dbData.compareTo(confData) != 0) {
				return true;
			} else {
				return false;
			}
		} else {
			// 数字类型不能进行模糊查询
			return false;
		}

	}

	/**
	 * cur数据比较
	 * 
	 * @param query_type
	 *            ：1 等于、2大于、3小于、4大于等于、5小于等于、6不等于、7like
	 * @return:true:数据相等 false:数据不相等
	 */
	public static boolean cur_compare(BigDecimal confData, BigDecimal dbData, String query_type) {
		// 判断相等
		if (query_type.equals("1")) {
			if (dbData.compareTo(confData) == 0) {
				return true;
			} else {
				return false;
			}
		} else if (query_type.equals("2")) {
			if (dbData.compareTo(confData) > 0) {
				return true;
			} else {
				return false;
			}
		} else if (query_type.equals("3")) {
			if (dbData.compareTo(confData) < 0) {
				return true;
			} else {
				return false;
			}
		} else if (query_type.equals("4")) {
			if (dbData.compareTo(confData) >= 0) {
				return true;
			} else {
				return false;
			}
		} else if (query_type.equals("5")) {
			if (dbData.compareTo(confData) <= 0) {
				return true;
			} else {
				return false;
			}
		} else if (query_type.equals("6")) {
			if (dbData.compareTo(confData) != 0) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}

	}

	/**
	 * int数据比较
	 * 
	 * @param query_type
	 *            ：1 等于、2大于、3小于、4大于等于、5小于等于、6不等于、7like
	 * @return:true:数据相等 false:数据不相等
	 */
	public static boolean int_compare(Integer confData, Integer dbData, String query_type) {

		if (query_type.equals("1")) {
			if (dbData.compareTo(confData) == 0) {
				return true;
			} else {
				return false;
			}
		} else if (query_type.equals("2")) {
			if (dbData.compareTo(confData) > 0) {
				return true;
			} else {
				return false;
			}
		} else if (query_type.equals("3")) {
			if (dbData.compareTo(confData) < 0) {
				return true;
			} else {
				return false;
			}
		} else if (query_type.equals("4")) {
			if (dbData.compareTo(confData) >= 0) {
				return true;
			} else {
				return false;
			}
		} else if (query_type.equals("5")) {
			if (dbData.compareTo(confData) <= 0) {
				return true;
			} else {
				return false;
			}
		} else if (query_type.equals("6")) {
			if (dbData.compareTo(confData) != 0) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}

	}

	/**
	 * @author wy 数据库中数据的数据类型与评分脚本中正确答案的数据类型匹配
	 * @throws Exception
	 */
	public static void compareDataType(String confData, Object dbData) throws Exception {
		confData = confData.trim();
		if (confData.toLowerCase().indexOf("int") != -1) {// 整型
			if (!(dbData instanceof Integer || dbData == null)) {
				throw new RuntimeException(
						"Data type mismatch,script data type: int--" + confData + "db data type not int--" + dbData);
			}
		} else if (confData.toLowerCase().indexOf("cur") != -1) {// 货币
			if (!(dbData instanceof BigDecimal || dbData == null)) {
				throw new RuntimeException("Data type mismatch,script data type: BigDecimal--" + confData
						+ "db data type not Decimal--" + dbData);
			}
		} else if (confData.toLowerCase().indexOf("num") != -1) {// 数字
			if (!(dbData instanceof BigDecimal || dbData == null)) {
				throw new RuntimeException("Data type mismatch,script data type: BigDecimal--" + confData
						+ "db data type not Decimal--" + dbData);
			}
		} else {
			if (!(dbData instanceof String || dbData == null)) {
				throw new RuntimeException("Data type mismatch,script data type: String:" + confData
						+ "db data type not String:" + dbData);
			}
		}
	}

	/**
	 * 获取项目根路径
	 */
	public static String getProjectRoot() {
		String PropjectRoot = ToolsUtil.class.getResource("/").getPath();
		PropjectRoot = PropjectRoot.substring(1, PropjectRoot.length() - 16);
		if (PropjectRoot != null && !PropjectRoot.endsWith("/"))
			PropjectRoot = PropjectRoot + "/";
		return PropjectRoot;
	}

	/**
	 * 获取配置文件exam_sys.properties属性值
	 * 
	 * @param key
	 *            ：属性名
	 */
	public static String getExamProperty(String key) {
		InputStream is = ToolsUtil.class.getClassLoader().getResourceAsStream("exam/exam_sys.properties");
		Properties properties = new Properties();
		try {
			properties.load(is);
			String property = properties.getProperty(key);
			return property;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取当前时间(yyyy-MM-dd HH:mm:ss)
	 */
	public static String getNowTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(new Date());
	}

	/**
	 * 根据value获取key
	 */
	public static String getKeyByValue(Map<String, String> schoolMap, String value) {
		Iterator itor = schoolMap.entrySet().iterator();
		while (itor.hasNext()) {
			Map.Entry entry = (Map.Entry) itor.next();
			if (value.equals(entry.getValue().toString())) {
				return entry.getKey().toString();
			}
		}
		return null;
	}

	public final static String NVL(Object obj) {
		String r = "";
		try {
			r = obj.toString();
		} catch (Throwable e) {

		}
		return r;
	}

	public final static int toInt(Object obj) {
		int r = -1;
		try {
			r = (Integer) obj;
		} catch (Throwable e) {

		}
		return r;
	}

	public final static double toDouble(Object obj) {
		double r = -1.0;
		try {
			r = (Double) obj;
		} catch (Throwable e) {

		}
		return r;
	}

	public final static boolean toBoolean(Object obj) {
		boolean r = false;
		try {
			r = (Boolean) obj;
		} catch (Throwable e) {

		}
		return r;
	}

	private static final String userglobalsessionprefix = "userglobalsessionprefix_";

	public static final <T> void setSession(String key, T t) {
		ActionContext actionContext = ActionContext.getContext();
		Map session = actionContext.getSession();
		session.put(userglobalsessionprefix + key, t);
	}

	public static final <T> T getSession(String key, Class<T> t) {
		ActionContext actionContext = ActionContext.getContext();
		Map session = actionContext.getSession();
		if (session.containsKey(userglobalsessionprefix + key)) {
			return (T) session.get(userglobalsessionprefix + key);
		}
		return null;
	}

	/**
	 * 半角转全角
	 * 
	 * @param input
	 *            String.
	 * @return 全角字符串.
	 */
	public static String ToSBC(String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == ' ') {
				c[i] = '\u3000';
			} else if (c[i] < '\177') {
				c[i] = (char) (c[i] + 65248);

			}
		}
		return new String(c);
	}

	/**
	 * 全角转半角
	 * 
	 * @param input
	 *            String.
	 * @return 半角字符串
	 */
	public static String ToDBC(String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == '\u3000') {
				c[i] = ' ';
			} else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
				c[i] = (char) (c[i] - 65248);

			}
		}
		String returnString = new String(c);
		return returnString;
	}

	public static void main(String[] args) {
		String str = "49349055@QQ.com";
		System.out.println(ToDBC(str));
	}

	public static int sumArray(List<Integer> source) {
		int result = 0;
		for (int i : source) {
			result += i;
		}
		return result;
	}

	/**
	 * 合并对象，以main为主，main中为空的属性赋assist的值
	 * 
	 * @param main
	 * @param assist
	 * @param exceptions
	 * @return
	 */
	public static Object combineObject(Object main, Object assist, List<String> exceptions) {
		if (assist == null) {
			return main;
		}
		if (!assist.getClass().getName().equals(main.getClass().getName()))
			return main;

		try {
			Field[] fields = main.getClass().getDeclaredFields();
			for (Field field : fields) {
//				field.setAccessible(true);// 修改访问权限
				if (Modifier.isFinal(field.getModifiers()))
					continue;
				// 排除例外
				if (exceptions.contains(field.getName()))
					continue;

				if (isWrapType(field)) {
					String firstLetter = field.getName().substring(0, 1).toUpperCase(); // 首字母大写
					String getMethodName = "get" + firstLetter + field.getName().substring(1);
					String setMethodName = "set" + firstLetter + field.getName().substring(1);
					Method mainGetMethod = main.getClass().getMethod(getMethodName); // 从源对象获取get方法
					Method assistGetMethod = assist.getClass().getMethod(getMethodName);

					Method mainSetMethod = main.getClass().getMethod(setMethodName, new Class[] { field.getType() }); // 从目标对象获取set方法

					// 获取main中的值
					Object mainValue = mainGetMethod.invoke(main);
					if (null == mainValue || mainValue.toString().equals("")) {
						Object assistValue = assistGetMethod.invoke(assist);
						mainSetMethod.invoke(main, new Object[] { assistValue }); // set 设置的是目标对象的值
					}
				}
			}
			return main;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return main;
	}

	private static boolean isWrapType(Field field) {
		String[] types = { "java.lang.Integer", "java.lang.Double", "java.lang.Float", "java.lang.Long",
				"java.lang.Short", "java.lang.Byte", "java.lang.Boolean", "java.lang.Char", "java.lang.String", "int",
				"double", "long", "short", "byte", "boolean", "char", "float" };
		List<String> typeList = Arrays.asList(types);
		return typeList.contains(field.getType().getName()) ? true : false;
	}
}
