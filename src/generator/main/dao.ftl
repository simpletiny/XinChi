package ${pac}.dao;

import java.util.List;

import ${vo};


public interface ${clazzName?cap_first}DAO{
	
	/**
	 * 新增
	 * @param bean
	 */
	public void insert(${voName} bean);
	
	/**
	 * 修改
	 * @param bean
	 */
	public void update(${voName} bean);
	
	/**
	 * 删除
	 * @param id
	 */
	public void delete(String id);
	
	/**
	 * 根据主键查找
	 * @param id
	 */
	public ${voName} selectByPrimaryKey(String id);
	
	/**
	 * 根据条件查找
	 * @param bean
	 */
	public List<${voName}> getAllByParam(${voName} bean);
}