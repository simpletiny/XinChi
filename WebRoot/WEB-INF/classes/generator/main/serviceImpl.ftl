package ${pac}.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ${vo};
import ${pac}.dao.${clazzName?cap_first}DAO;
import ${pac}.service.${clazzName?cap_first}Service;

@Service
@Transactional
public class ${clazzName?cap_first}ServiceImpl implements ${clazzName?cap_first}Service{

	@Autowired
	private ${clazzName?cap_first}DAO dao;
	
	@Override
	public void insert(${voName} bean) {
		dao.insert(bean);
	}

	@Override
	public void update(${voName} bean) {
		dao.update(bean);
	}

	@Override
	public void delete(String id) {
		dao.delete(id);
	}

	@Override
	public ${voName} selectByPrimaryKey(String id) {
		return dao.selectByPrimaryKey(id);
	}

	@Override
	public List<${voName}> selectByParam(${voName} bean) {
		return dao.selectByParam(bean);
	}
	
}