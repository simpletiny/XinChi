package com.xinchi.backend.receivable.dao;

import java.util.List;

import com.xinchi.bean.AccumulateBalanceBean;

public interface AccumulateBalanceDAO {

	public void insert(AccumulateBalanceBean ab);

	public List<AccumulateBalanceBean> selectNeedInsertAccumulateBalance();

}
