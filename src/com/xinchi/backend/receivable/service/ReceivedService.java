package com.xinchi.backend.receivable.service;

import com.xinchi.bean.ClientReceivedDetailBean;

public interface ReceivedService {

	public void insert(ClientReceivedDetailBean detail);

	public void insertWithPk(ClientReceivedDetailBean detail);
}
