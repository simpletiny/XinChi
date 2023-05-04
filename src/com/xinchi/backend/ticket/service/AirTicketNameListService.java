package com.xinchi.backend.ticket.service;

import java.util.List;

import com.xinchi.bean.AirTicketNameListBean;
import com.xinchi.bean.PassengerAllotDto;
import com.xinchi.common.BaseService;
import com.xinchi.tools.Page;

public interface AirTicketNameListService extends BaseService {

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

	public String insertList(List<AirTicketNameListBean> airTicketNameList);

	public List<AirTicketNameListBean> selectByPage(Page<AirTicketNameListBean> page);

	public List<AirTicketNameListBean> selectByPks(String[] pks);

	public List<PassengerAllotDto> selectPassengerAllotByPassengerPks(List<String> pks);

	public List<AirTicketNameListBean> selectByOrderNumber(String order_number);

	public List<AirTicketNameListBean> selectDoneByPage(Page<AirTicketNameListBean> page);

	public List<AirTicketNameListBean> selectByChangePk(String ticket_change_pk);

	public String toggleLockName(List<String> passenger_pks, String lock_flg);

	public String deletePassengerByPassengerPks(List<String> passenger_pks);
}