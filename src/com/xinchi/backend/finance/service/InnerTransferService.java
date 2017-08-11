package com.xinchi.backend.finance.service;

import java.util.List;

import com.xinchi.bean.InnerTransferBean;
import com.xinchi.common.BaseService;
import com.xinchi.tools.Page;

public interface InnerTransferService extends BaseService {

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