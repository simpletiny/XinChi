package com.xinchi.backend.receivable.service;

import java.util.List;

import com.xinchi.bean.ClientReceivedDetailBean;
import com.xinchi.tools.Page;

public interface ReceivedService {

	public void insert(ClientReceivedDetailBean detail);

	public void insertWithPk(ClientReceivedDetailBean detail);

	public List<ClientReceivedDetailBean> getAllReceivedsByPage(Page page);

	public String rollBackReceived(String received_pks);

	public List<ClientReceivedDetailBean> selectByRelatedPks(String related_pks);
}
