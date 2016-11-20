package com.xinchi.backend.receivable.dao;

import com.xinchi.bean.ClientReceivedDetailBean;

public interface ReceivedDAO {

	public void insert(ClientReceivedDetailBean detail);

	public void insertWithPk(ClientReceivedDetailBean detail);
}
