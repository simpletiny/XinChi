package com.xinchi.backend.receivable.service;

import java.util.List;

import com.xinchi.bean.ClientReceivedDetailBean;
import com.xinchi.common.LogDescription;
import com.xinchi.tools.Page;

@LogDescription(des = "收入详表")
public interface ReceivedService {

	@LogDescription(des = "收入申请")
	public void insert(ClientReceivedDetailBean detail);

	@LogDescription(des = "收入申请")
	public void insertWithPk(ClientReceivedDetailBean detail);

	@LogDescription(des = "搜索收入详表")
	public List<ClientReceivedDetailBean> getAllReceivedsByPage(Page page);

	@LogDescription(des = "打回重报")
	public String rollBackReceived(String received_pks);

	@LogDescription(ignore = true)
	public List<ClientReceivedDetailBean> selectByRelatedPks(String related_pks);

	@LogDescription(ignore = true)
	public void update(ClientReceivedDetailBean detail);

	@LogDescription(ignore = true)
	public ClientReceivedDetailBean selectByPk(String received_pk);

	public ClientReceivedDetailBean selectReceivedDetailByRelatedPk(String related_pk);
}
