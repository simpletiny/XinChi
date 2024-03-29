package com.xinchi.backend.receivable.service;

import java.math.BigDecimal;
import java.util.List;

import com.xinchi.bean.ClientReceivedDetailBean;
import com.xinchi.bean.ReceivedDetailDto;
import com.xinchi.common.BaseService;
import com.xinchi.common.LogDescription;
import com.xinchi.tools.Page;

@LogDescription(des = "收入详表")
public interface ReceivedService extends BaseService {

	@LogDescription(des = "收入申请")
	public void insert(ClientReceivedDetailBean detail);

	@LogDescription(des = "收入申请")
	public void insertWithPk(ClientReceivedDetailBean detail);

	@LogDescription(des = "搜索收入详表")
	public List<ClientReceivedDetailBean> getAllReceivedsByPage(Page<ClientReceivedDetailBean> page);

	@LogDescription(des = "打回重报")
	public String rollBackReceived(String received_pks);

	@LogDescription(ignore = true)
	public List<ClientReceivedDetailBean> selectByRelatedPks(String related_pks);

	@LogDescription(ignore = true)
	public void update(ClientReceivedDetailBean detail);

	@LogDescription(ignore = true)
	public ClientReceivedDetailBean selectByPk(String received_pk);

	public ClientReceivedDetailBean selectReceivedDetailByRelatedPk(String related_pk);

	public List<ClientReceivedDetailBean> selectByParam(ClientReceivedDetailBean bean);

	public String applySum(ClientReceivedDetailBean detail, String allot_json);

	public String applyRidTail(ClientReceivedDetailBean detail);

	public String applyCollect(ClientReceivedDetailBean detail);

	public String applyTail98(ClientReceivedDetailBean detail);

	public String applyReceive(ClientReceivedDetailBean detail);

	public String applyIfMorePay(ClientReceivedDetailBean detail, String allot_json);

	public String applyStrike(ClientReceivedDetailBean detail, String strike_out_json, String strike_in_json);

	public String applyFly(ClientReceivedDetailBean detail);

	public String checkIs98(String team_number);

	public BigDecimal selectSumReceivedByTeamNumber(String team_number);

	public String rejectRecived(String related_pks);

	public List<ReceivedDetailDto> searchAllAboutReceivedByRelatedPks(String related_pk, String from_where);
}
