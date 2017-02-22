package com.xinchi.backend.client.dao;

import java.util.List;

import com.xinchi.bean.AgencyFileBean;

public interface AgencyFileDAO {
	/**
	 * 新增
	 * 
	 * @param bo
	 */
	public void insert(AgencyFileBean bo);

	/**
	 * 修改
	 * 
	 * @param bo
	 */
	public void update(AgencyFileBean bo);

	/**
	 * 删除
	 * 
	 * @param id
	 */
	public void delete(String id);

	public List<AgencyFileBean> searchAgencyFilesByAgencyPk(String supplier_pk);

	public List<AgencyFileBean> selectByParam(AgencyFileBean sfb);

	public void deleteFileByParam(AgencyFileBean sfb);
}
