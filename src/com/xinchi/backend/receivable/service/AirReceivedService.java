package com.xinchi.backend.receivable.service;

import java.util.List;

import com.xinchi.bean.AirReceivedDetailBean;
import com.xinchi.common.BaseService;
import com.xinchi.tools.Page;

public interface AirReceivedService extends BaseService {

	public void insert(AirReceivedDetailBean detail);

	public List<AirReceivedDetailBean> selectByPage(Page<AirReceivedDetailBean> page);

	public String rollBackReceived(String related_pk);

	public void update(AirReceivedDetailBean detail);

	public AirReceivedDetailBean selectByPk(String detail_pk);

	public List<AirReceivedDetailBean> selectByParam(AirReceivedDetailBean option);

}
