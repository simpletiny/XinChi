package ${pac}.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import ${vo};
import com.xinchi.common.DaoUtil;
import ${pac}.dao.${clazzName?cap_first}DAO;


@Repository
public class ${clazzName?cap_first}DAOImpl extends SqlSessionDaoSupport implements ${clazzName?cap_first}DAO{

	private SqlSession sqlSession;
	private DaoUtil daoUtil;
	public void initDao(){
		if(daoUtil==null){
			sqlSession=sqlSession==null?getSqlSession():sqlSession;
			daoUtil=new DaoUtil(sqlSession);
		}
	}
	
	@Override
	public void insert(${voName} bean) {
		daoUtil.insertBO("com.xinchi.bean.mapper.${voName}Mapper.insert", bean);
	}

	@Override
	public void update(${voName} bean) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.${voName}Mapper.updateByPrimaryKey", bean);
	}

	@Override
	public void delete(String id) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.${voName}Mapper.deleteByPrimaryKey", id);
	}

	@Override
	public ${voName} selectByPrimaryKey(String id) {
		return (${voName}) daoUtil.selectByPK("com.xinchi.bean.mapper.${voName}Mapper.selectByPrimaryKey", id);
	}

	@Override
	public List<${voName}> selectByParam(${voName} bean) {
		List<${voName}> list=daoUtil.selectByParam("com.xinchi.bean.mapper.${voName}Mapper.selectByParam", bean);
		return list;
	}
	
}