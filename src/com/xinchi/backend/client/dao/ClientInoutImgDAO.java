package com.xinchi.backend.client.dao;

import java.util.List;

import com.xinchi.bean.ClientInoutImgBean;

public interface ClientInoutImgDAO {

	public void insert(ClientInoutImgBean bean);

	public ClientInoutImgBean selectByPk(String pk);

	public void update(ClientInoutImgBean bean);

	public List<ClientInoutImgBean> selectByParam(ClientInoutImgBean bean);

	public List<ClientInoutImgBean> selectByClientPk(String pk);

	public void delete(String pk);
}
