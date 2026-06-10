package com.xinchi.backend.finance.service;

import java.util.List;

import com.xinchi.bean.ReceivedMatchBean;
import com.xinchi.common.LogDescription;

@LogDescription(des = "收入匹配")
public interface ReceivedMatchService {
	@LogDescription(des = "收入匹配")
	public String insert(ReceivedMatchBean bo);

	@LogDescription(ignore = true)
	public String update(ReceivedMatchBean bo);

	@LogDescription(des = "打回重报")
	public String delete(String pk);

	@LogDescription(ignore = true)
	public List<ReceivedMatchBean> selectByDetailPk(String detailId);

	public List<ReceivedMatchBean> selectByReceivedPk(String received_pk);

}
