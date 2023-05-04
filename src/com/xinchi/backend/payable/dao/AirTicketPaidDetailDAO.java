package com.xinchi.backend.payable.dao;

import java.util.List;

import com.xinchi.bean.AirTicketPaidDetailBean;
import com.xinchi.bean.AirTicketPaidDto;
import com.xinchi.tools.Page;

public interface AirTicketPaidDetailDAO {

	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public void insert(AirTicketPaidDetailBean bean);

	/**
	 * 修改
	 * 
	 * @param bean
	 */
	public void update(AirTicketPaidDetailBean bean);

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
	public AirTicketPaidDetailBean selectByPrimaryKey(String id);

	public List<AirTicketPaidDetailBean> selectByPayablePk(String payable_pk);

	/**
	 * 根据条件查找
	 * 
	 * @param bean
	 */
	public List<AirTicketPaidDetailBean> selectByParam(AirTicketPaidDetailBean bean);

	public List<AirTicketPaidDto> selectByPage(Page<AirTicketPaidDto> page);

	public List<AirTicketPaidDetailBean> selectByRelatedPk(String related_pk);

	public AirTicketPaidDetailBean selectGroupDetailByRelatedPk(String related_pk);
}