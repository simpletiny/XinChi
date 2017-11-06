package ${pac}.service;

import java.util.List;

import ${vo};
import com.xinchi.common.BaseService;

public interface ${clazzName?cap_first}Service extends BaseService{
	
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
	public List<${voName}> selectByParam(${voName} bean);
}