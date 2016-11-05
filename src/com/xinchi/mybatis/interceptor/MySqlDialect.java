package com.xinchi.mybatis.interceptor;

import com.xinchi.tools.Page;

public class MySqlDialect extends Dialect {

	@Override
	public String getLimitString(String sql, Page<?> page) {
		StringBuffer sqlBuffer = new StringBuffer(sql);
		int offset = page.getStart();
		sqlBuffer.append(" limit ").append(offset).append(",")
				.append(page.getCount());
		return sqlBuffer.toString();
	}
}
