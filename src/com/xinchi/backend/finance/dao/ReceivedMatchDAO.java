package com.xinchi.backend.finance.dao;

import java.util.List;

import com.xinchi.bean.ReceivedMatchBean;

public interface ReceivedMatchDAO {

	public String insert(ReceivedMatchBean bo);

	public void update(ReceivedMatchBean bo);

	public void delete(String pk);

	public List<ReceivedMatchBean> selectByDetailPk(String detailId);

	public List<ReceivedMatchBean> selectByReceivedPk(String received_pk);

}
