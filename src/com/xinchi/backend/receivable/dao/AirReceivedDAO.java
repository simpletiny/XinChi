package com.xinchi.backend.receivable.dao;

import java.util.List;

import com.xinchi.bean.AirReceivedDetailBean;
import com.xinchi.tools.Page;

public interface AirReceivedDAO {

	public AirReceivedDetailBean selectByPk(String pk);

	public void insert(AirReceivedDetailBean detail);

	public List<AirReceivedDetailBean> selectByPage(Page<AirReceivedDetailBean> page);

	public void deleteByPk(String pk);

	public void update(AirReceivedDetailBean detail);

	public List<AirReceivedDetailBean> selectByParam(AirReceivedDetailBean option);

	public List<AirReceivedDetailBean> selectByRelatedPk(String related_pk);

	public List<AirReceivedDetailBean> selectByBusinessNumber(String business_number);

}
