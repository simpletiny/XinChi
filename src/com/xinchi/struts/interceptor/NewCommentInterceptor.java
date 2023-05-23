package com.xinchi.struts.interceptor;

import java.util.Map;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class NewCommentInterceptor extends AbstractInterceptor {
	private static final long serialVersionUID = 5488182358021203336L;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {

		Map<String, Object> parameters = invocation.getInvocationContext().getParameters();
		// 遍历请求参数
		for (Map.Entry<String, Object> entry : parameters.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();

			if (!key.toLowerCase().contains("comment"))
				continue;
			// 如果是字符串类型的参数
			if (value instanceof String[]) {
				String[] stringValues = (String[]) value;

				// 将换行符替换为其他格式
				for (int i = 0; i < stringValues.length; i++) {
					String newValue = stringValues[i].replace("\r\n", "\n");
					stringValues[i] = newValue;
				}
				parameters.put(key, stringValues);
			}
		}
		return invocation.invoke();
	}
}
