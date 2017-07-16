package com.xinchi.backend.client.service;

import java.util.List;

import com.xinchi.bean.AccurateSaleBean;
import com.xinchi.common.LogDescription;
import com.xinchi.tools.Page;

@LogDescription(des = "精推")
public interface AccurateSaleService {

	/**
	 * 新增
	 * 
	 * @param bean
	 */
	@LogDescription(des = "新增精推")
	public void insert(AccurateSaleBean bean);

	/**
	 * 修改
	 * 
	 * @param bean
	 */
	@LogDescription(ignore = true)
	public void update(AccurateSaleBean bean);

	/**
	 * 删除
	 * 
	 * @param id
	 */
	@LogDescription(ignore = true)
	public void delete(String id);

	/**
	 * 根据主键查找
	 * 
	 * @param id
	 */
	@LogDescription(ignore = true)
	public AccurateSaleBean selectByPrimaryKey(String id);

	/**
	 * 根据条件查找
	 * 
	 * @param bean
	 */
	@LogDescription(ignore = true)
	public List<AccurateSaleBean> getAllByParam(AccurateSaleBean bean);

	@LogDescription(des = "查看精推")
	public List<AccurateSaleBean> selectByPage(Page page);
}