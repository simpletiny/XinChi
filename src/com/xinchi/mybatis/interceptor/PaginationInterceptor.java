package com.xinchi.mybatis.interceptor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.xwork.StringUtils;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.apache.log4j.Logger;

import com.xinchi.tools.Page;

/**
 * 分页拦截器
 */
@Intercepts({ @Signature(method = "prepare", type = StatementHandler.class, args = { Connection.class }) })
public class PaginationInterceptor implements Interceptor {

	private Logger logger = Logger.getLogger(PaginationInterceptor.class);

	/**
	 * 拦截后要执行的方法
	 */
	public Object intercept(Invocation invocation) throws Throwable {
		StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
		BoundSql boundSql = statementHandler.getBoundSql();
		Object obj = boundSql.getParameterObject();
		// 判断方言
		MetaObject metaStatementHandler = MetaObject.forObject(statementHandler, new DefaultObjectFactory(),
				new DefaultObjectWrapperFactory());
		Configuration configuration = (Configuration) metaStatementHandler.getValue("delegate.configuration");
		Dialect.Type databaseType = null;
		try {
			databaseType = Dialect.Type.valueOf(configuration.getVariables().getProperty("dialect").toUpperCase());
		} catch (Exception e) {

		}
		if (databaseType == null) {
			logger.warn("数据库方言未设置！");
			databaseType = Dialect.Type.MYSQL;
		}
		Dialect dialect = null;
		switch (databaseType) {
		case MYSQL:
			dialect = new MySqlDialect();
			break;
		case ORACLE:
			break;
		case SQLSERVER:
			break;
		}
		String pageSqlId = configuration.getVariables().getProperty("pageSqlId");
		if (StringUtils.isEmpty(pageSqlId))
			pageSqlId = ".*ByPage$";
		MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");
		if (obj instanceof Page<?> && mappedStatement.getId().matches(pageSqlId)) {
			Page<?> page = (Page<?>) obj;
			String originalSql = (String) metaStatementHandler.getValue("delegate.boundSql.sql");
			String pageSql = dialect.getLimitString(originalSql, page);

			metaStatementHandler.setValue("delegate.boundSql.sql", pageSql);
			metaStatementHandler.setValue("delegate.rowBounds.offset", RowBounds.NO_ROW_OFFSET);
			metaStatementHandler.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT);
			// 重设分页参数里的总页数等
			Connection connection = (Connection) invocation.getArgs()[0];
			this.setTotalRecord(page, mappedStatement, connection);
		}
		return invocation.proceed();
	}

	/**
	 * 拦截器对应的封装原始对象的方法
	 */
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	/**
	 * 给当前的参数对象page设置总记录数
	 * 
	 * @param page
	 *            Mapper映射语句对应的参数对象
	 * @param mappedStatement
	 *            Mapper映射语句
	 * @param connection
	 *            当前的数据库连接
	 */
	private void setTotalRecord(Page<?> page, MappedStatement mappedStatement, Connection connection) {
		BoundSql boundSql = mappedStatement.getBoundSql(page);
		String sql = boundSql.getSql();
		String countSql = this.getCountSql(sql);
		// 通过BoundSql获取对应的参数映射
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		// 利用Configuration、查询记录数的Sql语句countSql、参数映射关系parameterMappings和参数对象page建立查询记录数对应的BoundSql对象。
		BoundSql countBoundSql = new BoundSql(mappedStatement.getConfiguration(), countSql, parameterMappings, page);
		// 通过mappedStatement、参数对象page和BoundSql对象countBoundSql建立一个用于设定参数的ParameterHandler对象
		ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, page, countBoundSql);
		// 通过connection建立一个countSql对应的PreparedStatement对象。
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = connection.prepareStatement(countSql);
			// 通过parameterHandler给PreparedStatement对象设置参数
			parameterHandler.setParameters(pstmt);
			// 之后就是执行获取总记录数的Sql语句和获取结果了。
			rs = pstmt.executeQuery();
			if (rs.next()) {
				int totalRecord = rs.getInt(1);
				// 给当前的参数page对象设置总记录数
				page.setTotal(totalRecord);
			}
		} catch (SQLException e) {
			logger.error("Ignore this exception", e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				logger.error("Ignore this exception", e);
			}
		}
	}

	/**
	 * 根据原Sql语句获取对应的查询总记录数的Sql语句
	 * 
	 * @param sql
	 * @return
	 */
	private String getCountSql(String sql) {
		int index = sql.indexOf("from");
		if (index == -1) {
			index = sql.indexOf("FROM");
		}
		String result = "select count(*) " + sql.substring(index);
		if (sql.indexOf("GROUP BY") > -1 || sql.indexOf("group by") > -1) {
			result = "select count(*) from (" + result + ") AA;";
		}
		return result;
	}

	public void setProperties(Properties arg0) {
		// TODO Auto-generated method stub

	}

}