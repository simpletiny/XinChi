package com.xinchi.backend.finance.dao;

import java.util.List;

import com.xinchi.bean.InnerTransferBean;
import com.xinchi.tools.Page;

public interface InnerTransferDAO {

	/**
	 * 根据主键查找
	 * 
	 * @param id
	 */
	public InnerTransferBean selectByPrimaryKey(String id);

	/**
	 * 根据条件查找
	 * 
	 * @param bean
	 */
	public List<InnerTransferBean> selectByParam(InnerTransferBean bean);
	
	
	public List<InnerTransferBean> selectByPage(Page<InnerTransferBean> page);
}