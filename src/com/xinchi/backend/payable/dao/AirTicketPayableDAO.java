package com.xinchi.backend.payable.dao;

import java.util.List;

import com.xinchi.bean.AirServiceFeeDto;
import com.xinchi.bean.AirTicketPayableBean;
import com.xinchi.tools.Page;

public interface AirTicketPayableDAO {

	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public void insert(AirTicketPayableBean bean);

	public void insertWithPk(AirTicketPayableBean bean);

	/**
	 * 修改
	 * 
	 * @param bean
	 */
	public void update(AirTicketPayableBean bean);

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
	public AirTicketPayableBean selectByPrimaryKey(String id);

	/**
	 * 根据条件查找
	 * 
	 * @param bean
	 */
	public List<AirTicketPayableBean> selectByParam(AirTicketPayableBean bean);

	public List<AirTicketPayableBean> selectByPage(Page<AirTicketPayableBean> page);

	public List<AirTicketPayableBean> selectByPks(List<String> pks);

	public List<AirTicketPayableBean> selectByRelatedPk(String related_pk);

	public void deleteByRelatedPk(String related_pk);

	public List<AirServiceFeeDto> searchServiceFees(AirServiceFeeDto summary_option);

	public List<AirServiceFeeDto> selectServiceFeeSummary(AirServiceFeeDto summary_option);

	public List<AirServiceFeeDto> selectAirTicketDeductSummary(AirServiceFeeDto summary_option);

}