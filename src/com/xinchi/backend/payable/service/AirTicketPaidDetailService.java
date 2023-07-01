package com.xinchi.backend.payable.service;

import java.util.List;

import com.xinchi.bean.AirTicketPaidDetailBean;
import com.xinchi.bean.AirTicketPaidDto;
import com.xinchi.bean.PaymentDetailBean;
import com.xinchi.common.BaseService;
import com.xinchi.tools.Page;

public interface AirTicketPaidDetailService extends BaseService {

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

	/**
	 * 根据条件查找
	 * 
	 * @param bean
	 */
	public List<AirTicketPaidDetailBean> selectByParam(AirTicketPaidDetailBean bean);

	public List<AirTicketPaidDto> selectByPage(Page<AirTicketPaidDto> page);

	public List<AirTicketPaidDetailBean> selectByRelatedPk(String related_pk);

	public String rollBackPayApply(String related_pk);

	public String backRecive(AirTicketPaidDetailBean detail, String json);

	public String businessStrike(String json);

	public String depositStrike(String json);

	public String createPaymentDetail(PaymentDetailBean payment_detail);

	public String createDeduct(String json);

	public String addProductManger(String detail_pk, String product_manager_number, String belong_month);

	public List<AirTicketPaidDetailBean> selectByBasePk(String base_pk);
}