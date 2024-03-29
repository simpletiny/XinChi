package com.xinchi.common;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.xinchi.bean.SqlBean;

/**
 * 访问数据库Dao工具类,要求所有访问数据库都使用此工具类
 * 
 * @author simpletiny
 * 
 */
public class DaoUtil {
	private SqlSession sqlSession = null;

	public SqlSession getSqlSession() {
		return sqlSession;
	}

	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	public DaoUtil(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	/**
	 * 插入对象
	 * 
	 * @param sqlSession
	 * @param supperBO
	 * @return
	 */
	public String insertBO(String mapper, SupperBO supperBO) {
		String pk = DBCommonUtil.genPk();
		supperBO.setPk(pk);
		supperBO.setCreate_time(DateUtil.getTimeMillis());

		UserSessionBean ub = ((UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY));
		if (null != ub) {
			supperBO.setCreate_user(ub.getUser_number());
		}
		sqlSession.insert(mapper, supperBO);
		return pk;
	}

	public String insertBOWithCreateTime(String mapper, SupperBO supperBO) {
		String pk = DBCommonUtil.genPk();
		supperBO.setPk(pk);
		UserSessionBean ub = ((UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY));
		if (null != ub) {
			supperBO.setCreate_user(ub.getUser_number());
		}
		sqlSession.insert(mapper, supperBO);
		return pk;
	}

	public String insertWithoutLogin(String mapper, SupperBO supperBO) {
		String pk = DBCommonUtil.genPk();
		supperBO.setPk(pk);
		supperBO.setCreate_time(DateUtil.getTimeMillis());

		sqlSession.insert(mapper, supperBO);
		return pk;
	}

	public String insertBOWithPk(String mapper, SupperBO supperBO) {
		supperBO.setCreate_time(DateUtil.getTimeMillis());
		UserSessionBean ub = ((UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY));
		if (null != ub) {
			supperBO.setCreate_user(ub.getUser_number());
		}
		sqlSession.insert(mapper, supperBO);
		return supperBO.getPk();
	}

	public String insertObject(String mapper, Object object) {
		sqlSession.insert(mapper, object);
		return "success";
	}

	/**
	 * 插入对象列表
	 * 
	 * @param sqlSession
	 * @param supperBO
	 * @return
	 */
	public String[] insertBOList(String mapper, List<? extends SupperBO> bOList) {
		if (null == bOList || bOList.size() == 0)
			return null;
		String[] rtnPks = new String[bOList.size()];
		String date = DateUtil.getTimeMillis();
		String[] pks = DBCommonUtil.genPks(bOList.size());
		UserSessionBean ub = ((UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY));
		for (int i = 0; i < bOList.size(); i++) {
			rtnPks[i] = pks[i];
			bOList.get(i).setPk(pks[i]);
			bOList.get(i).setCreate_time(date);
			bOList.get(i).setCreate_user(ub.getUser_number());
			sqlSession.insert(mapper, bOList.get(i));
		}

		return rtnPks;
	}

	/**
	 * 直接通过sql插入数据
	 * 
	 * @param insertSql
	 * @return
	 */
	public void insertBySql(String insertSql) {
		sqlSession.insert("com.xinchi.bean.mapper.CommonMapper.insertBySql", insertSql);
	}

	/**
	 * 动态批量删除sql
	 * 
	 * @param sql
	 * @return
	 */
	public void deleteBySql(String sql) {
		sqlSession.delete("com.xinchi.bean.mapper.CommonMapper.executeBySql", sql);
	}

	/**
	 * 执行sql
	 * 
	 * @param sql
	 */
	public void executeBySql(SqlBean sql) {
		sqlSession.update("com.xinchi.bean.mapper.CommonMapper.executeBySql", sql);
	}

	/*
	 * 根据主键删除记录
	 * 
	 * @param mapper：ibatis配置文件的namespace+方法id
	 * 
	 * @param id：删除记录的主键
	 */
	public int deleteByPK(String mapper, String id) {
		return sqlSession.delete(mapper, id);
	}

	/**
	 * 根据参数删除记录(参数为Bo对象)
	 * 
	 * @author wy
	 * @param mapper
	 * @param supperBO
	 * @return
	 */
	public int deleteByBOParam(String mapper, SupperBO supperBO) {
		return sqlSession.delete(mapper, supperBO);
	}

	/**
	 * 根据参数删除记录(参数为object对象)
	 * 
	 * @author czy
	 * @param mapper
	 * @param param
	 * @return
	 */
	public int deleteByParam(String mapper, Object param) {
		return sqlSession.delete(mapper, param);
	}

	/**
	 * 根据参数查询结果集(参数为Bo对象)
	 * 
	 * @param mapper
	 * @param id
	 * @return
	 */
	public SupperBO selectByPK(String mapper, String id) {
		SupperBO supperBO = sqlSession.selectOne(mapper, id);
		return supperBO;
	}

	/**
	 * 根据参数查询结果集(参数为Bo对象)
	 * 
	 * @param mapper
	 * @param Map
	 *            :Bo对象参数
	 * @return：BO的list对象
	 */
	public List<SupperBO> selectByBOParam(String mapper, SupperBO supperBO) {
		List<SupperBO> listBo = sqlSession.selectList(mapper, supperBO);
		return listBo;
	}

	/**
	 * 根据参数查询结果集(参数为Bo对象)
	 * 
	 * @param mapper
	 * @param Map
	 *            :Bo对象参数
	 * @return：BO的list对象
	 */
	public <T extends SupperBO> List<T> selectByBOParamT(String mapper, T t) {
		List<T> listBo = sqlSession.selectList(mapper, t);
		return listBo;
	}

	/**
	 * 根据参数查询结果集(参数为object对象)
	 * 
	 * @param mapper
	 * @param Object
	 *            :param对象参数
	 * @return：BO的list对象
	 */
	/*
	 * public List<SupperBO> selectByParam(String mapper,Object param){
	 * List<SupperBO> listBo = sqlSession.selectList(mapper,param); return listBo; }
	 */
	public <T extends SupperBO> List<T> selectByParam(String mapper, Object param) {
		List<T> listBo = sqlSession.selectList(mapper, param);
		return listBo;
	}

	public <T extends Object> List<T> selectObjectsByParam(String mapper, Object param) {
		List<T> listBo = sqlSession.selectList(mapper, param);
		return listBo;
	}

	/**
	 * 根据参数查询结果集(参数为Map对象)
	 * 
	 * @param mapper
	 * @param Map
	 *            :键值对参数对象
	 * @return：List<Map>
	 */
	public List<Map> selectByMapParam(String mapper, Map map) {
		List<Map> listMap = sqlSession.selectList(mapper, map);
		return listMap;
	}

	/**
	 * 根据参数查询结果集(参数为Map对象)
	 * 
	 * @param mapper
	 * @param Map
	 *            :键值对参数对象
	 * @param keyColumn
	 *            :指定map key的列名
	 * @return：BO的Map键值对对象
	 */
	public Map<String, Object> selectByMapParam(String mapper, Map map, String keyColumn) {
		Map<String, Object> rtnMap = sqlSession.selectMap(mapper, map, keyColumn);
		return rtnMap;
	}

	/**
	 * 根据sql语句查询结果集(此方法尽量少用，因为传入sql是在程序中写的，不可修改)
	 * 
	 * @param sql
	 * @return
	 */
	public List<SupperBO> selectBySql(String sql) {
		List<SupperBO> listBo = sqlSession.selectList("commonMapper.selectRecords", sql);
		return listBo;
	}

	/**
	 * 查询结果集
	 * 
	 * @author wy
	 * @param mapper
	 * @return
	 */
	public <T extends SupperBO> List<T> selectAll(String mapper) {
		List<T> listBo = sqlSession.selectList(mapper);
		return listBo;
	}

	public <T extends Object> List<T> selectAllOut(String mapper) {
		List<T> listBo = sqlSession.selectList(mapper);
		return listBo;
	}

	/**
	 * 根据主键修改记录
	 * 
	 * @param mapper
	 * @param supperBO
	 * @return
	 */
	public int updateByPK(String mapper, SupperBO supperBO) {
		supperBO.setUpdate_time(DateUtil.getTimeMillis());
		UserSessionBean ub = ((UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY));
		if (null != ub) {
			supperBO.setUpdate_user(ub.getUser_number());
		}

		return sqlSession.update(mapper, supperBO);
	}

	public int sysUpdateByPK(String mapper, SupperBO supperBO) {
		supperBO.setUpdate_time(DateUtil.getTimeMillis());
		return sqlSession.update(mapper, supperBO);
	}

	/**
	 * 根据BO修改记录
	 * 
	 * @author wy
	 * @param mapper
	 * @param supperBO
	 * @return
	 */
	public int updateByBOParam(String mapper, SupperBO supperBO) {
		supperBO.setUpdate_time(DateUtil.getTimeMillis());
		return sqlSession.update(mapper, supperBO);
	}

	/**
	 * 根据条件修改记录(必须配置updateByPK的mapper.xml文件)
	 * 
	 * @param mapper
	 * @param List
	 *            <SupperBO>：Bo对象数组
	 * @return
	 */
	public void updateBOList(String mapper, List<? extends SupperBO> listBo) {
		for (SupperBO supperBO : listBo) {
			this.updateByPK(mapper, supperBO);
		}
	}

	/**
	 * 查询单个结果集合
	 * 
	 * @param mapper
	 * @return
	 */
	public List<Object> selectList(String mapper) {
		return sqlSession.selectList(mapper);
	}

	/**
	 * 查询集合
	 * 
	 * @param mapper
	 * @param obj
	 * @return
	 */
	public <T extends Object> List<T> selectListByParam(String mapper, Object obj) {
		return sqlSession.selectList(mapper, obj);
	}

	/**
	 * 根据sql语句修改记录
	 * 
	 * @author wy
	 * @param sql
	 */
	public void updateBySql(String sql) {
		sqlSession.update("commonMapper.executeBySql", sql);
	}

	public void updateByParam(String mapper, Object arg0) {
		sqlSession.update(mapper, arg0);
	}

	/**
	 * 根据参数取得一个值
	 * 
	 * @param mapper
	 * @param supperBO
	 * @return
	 */
	public <T extends Object> T selectOneValueByParam(String mapper, SupperBO supperBO) {
		return (T) sqlSession.selectOne(mapper, supperBO);
	}

	public <T extends Object> T selectOneValueByParam(String mapper, Object object) {
		return (T) sqlSession.selectOne(mapper, object);
	}

	public <T extends Object> T selectOneValue(String mapper) {
		return (T) sqlSession.selectOne(mapper, null);
	}
}
