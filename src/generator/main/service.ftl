package ${pac}.service;

import java.util.List;

import seentao.xhsn.bean.${vo};


public interface ${clazzName?cap_first}Service{
	
	/**
	 * 新增
	 * @param bo
	 */
	public void insert(${vo} bo);
	
	/**
	 * 修改
	 * @param bo
	 */
	public void update(${vo} bo);
	
	/**
	 * 删除
	 * @param id
	 */
	public void delete(String id);
	
	/**
	 * 根据主键查找
	 * @param id
	 */
	public ${vo} selectByPrimaryKey(String id);
	
	/**
	 * 根据条件查找
	 * @param bo
	 */
	public List<${vo}> getAllByParam(${vo} bo);
}