package com.xinchi.backend.receivable.dao;

import java.math.BigDecimal;
import java.util.List;

import com.xinchi.bean.ClientReceivedDetailBean;
import com.xinchi.tools.Page;

public interface ReceivedDAO {

	public ClientReceivedDetailBean selectByPk(String pk);

	public void insert(ClientReceivedDetailBean detail);

	public void insertWithPk(ClientReceivedDetailBean detail);

	public List<ClientReceivedDetailBean> getAllByPage(Page<ClientReceivedDetailBean> page);

	public void deleteByPk(String pk);

	public List<ClientReceivedDetailBean> selectByRelatedPks(String related_pks);

	public void update(ClientReceivedDetailBean detail);

	public ClientReceivedDetailBean selectReceivedDetailByRelatedPk(String related_pk);

	public List<ClientReceivedDetailBean> selectByParam(ClientReceivedDetailBean bean);

	public BigDecimal selectSumReceivedByTeamNumber(String team_number);
}
