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
@Scope("prototype")
public class ${clazzName?cap_first}ServiceImpl implements ${clazzName?cap_first}Service{

	@Autowired
	private ${clazzName?cap_first}DAO dao;
	
	@Override
	public void insert(${vo} bo) {
		dao.insert(bo);
	}

	@Override
	public void update(${vo} bo) {
		dao.update(bo);
	}

	@Override
	public void delete(String id) {
		dao.delete(id);
	}

	@Override
	public ${vo} selectByPrimaryKey(String id) {
		return dao.selectByPrimaryKey(id);
	}

	@Override
	public List<${vo}> getAllByParam(${vo} bo) {
		return dao.getAllByParam(bo);
	}
	
}