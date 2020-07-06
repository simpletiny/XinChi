package com.xinchi.backend.payable.dao;

import java.util.List;

import com.xinchi.bean.PayableOrderBean;

public interface PayableOrderDAO {

	public void insert(PayableOrderBean payable);

	public List<PayableOrderBean> selectByParam(PayableOrderBean payable);

	public void update(PayableOrderBean payable);

	public void deleteByPk(String pk);

	public void deleteByTeamNumber(String t_n);

}
