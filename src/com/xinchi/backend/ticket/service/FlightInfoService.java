package com.xinchi.backend.ticket.service;

import java.util.List;

import com.xinchi.bean.FlightInfoBean;
import com.xinchi.common.BaseService;

public interface FlightInfoService extends BaseService {

	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public void insert(FlightInfoBean bean);

	/**
	 * 修改
	 * 
	 * @param bean
	 */
	public void update(FlightInfoBean bean);

	/**
	 * 删除
	 * 
	 * @param id
	 */
	public void delete(String id);

	/**
	 * 根据主键查找
	 * 
	 * @param id
	 */
	public FlightInfoBean selectByPrimaryKey(String id);

	/**
	 * 根据条件查找
	 * 
	 * @param bean
	 */
	public List<FlightInfoBean> selectByParam(FlightInfoBean bean);
}