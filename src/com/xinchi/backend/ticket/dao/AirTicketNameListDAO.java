package com.xinchi.backend.ticket.dao;

import java.util.List;

import com.xinchi.bean.AirTicketNameListBean;
import com.xinchi.bean.PassengerAllotDto;
import com.xinchi.tools.Page;

public interface AirTicketNameListDAO {

	/**
	 * 新增
	 * 
	 * @param bean
	 */
	public void insert(AirTicketNameListBean bean);

	/**
	 * 修改
	 * 
	 * @param bean
	 */
	public void update(AirTicketNameListBean bean);

	/**
	 * 删除
	 * 
	 * @param id
	 */
	public void delete(String id);

	public void deleteByOrderNumber(String order_number);

	/**
	 * 根据主键查找
	 * 
	 * @param id
	 */
	public AirTicketNameListBean selectByPrimaryKey(String id);

	/**
	 * 根据条件查找
	 * 
	 * @param bean
	 */
	public List<AirTicketNameListBean> selectByParam(AirTicketNameListBean bean);

	public List<AirTicketNameListBean> selectByPage(Page<AirTicketNameListBean> page);

	public List<AirTicketNameListBean> selectByPks(String[] pks);

	public List<PassengerAllotDto> selectByPassengerPks(List<String> passenger_pks);

	public List<PassengerAllotDto> selectPassengerAllotByPassengerPk(String passenger_pk);

	public List<PassengerAllotDto> selectPassengerAllotByOrderPk(String order_pk);

	public List<AirTicketNameListBean> selectByPayablePk(String payable_pk);

	public List<AirTicketNameListBean> selectByTeamNumber(String team_number);

	public List<AirTicketNameListBean> selectByOrderNumber(String order_number);

	public List<AirTicketNameListBean> selectDoneByPage(Page<AirTicketNameListBean> page);

	public List<AirTicketNameListBean> selectByTeamNumbers(List<String> t_ns);

	public List<AirTicketNameListBean> selectByChangePk(String ticket_change_pk);

	public List<AirTicketNameListBean> selectWithInfoByTeamNumbers(List<String> t_ns);

	public AirTicketNameListBean selectByBasePk(String pk);
}