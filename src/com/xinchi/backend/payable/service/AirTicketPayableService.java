package com.xinchi.backend.payable.service;

import java.util.List;

import com.xinchi.bean.AirTicketPayableBean;
import com.xinchi.common.BaseService;
import com.xinchi.tools.Page;

public interface AirTicketPayableService extends BaseService{
	
	/**
	 * 新增
	 * @param bean
	 */
	public void insert(AirTicketPayableBean bean);
	
	/**
	 * 修改
	 * @param bean
	 */
	public void update(AirTicketPayableBean bean);
	
	/**
	 * 删除
	 * @param id
	 */
	public void delete(String id);
	
	/**
	 * 根据主键查找
	 * @param id
	 */
	public AirTicketPayableBean selectByPrimaryKey(String id);
	
	/**
	 * 根据条件查找
	 * @param bean
	 */
	public List<AirTicketPayableBean> selectByParam(AirTicketPayableBean bean);

	public List<AirTicketPayableBean> selectByPage(Page<AirTicketPayableBean> page);

	public List<AirTicketPayableBean> selectByPks(List<String> pks);
}