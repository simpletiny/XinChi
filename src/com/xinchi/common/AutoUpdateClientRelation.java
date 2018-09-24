package com.xinchi.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.client.dao.ClientRelationDAO;
import com.xinchi.backend.util.dao.CommonDAO;
import com.xinchi.bean.ClientRelationBean;
import com.xinchi.bean.ClientRelationSummaryBean;
import com.xinchi.bean.SqlBean;

@Service
@Transactional
public class AutoUpdateClientRelation {
	@Autowired
	private ClientRelationDAO clientRelationDao;

	@Autowired
	private CommonDAO commonDao;
	
	public void updateRelation(String[] param) {
		String sql = "truncate table client_relation;";
		SqlBean ss = new SqlBean();
		ss.setSql(sql);
		commonDao.exeBySql(ss);

		List<ClientRelationSummaryBean> views = clientRelationDao.selectByParam(null);

		for (ClientRelationSummaryBean one : views) {
			ClientRelationBean two = new ClientRelationBean();
			two.setClient_employee_name(one.getClient_employee_name());
			two.setNick_name(one.getNick_name());
			two.setClient_employee_pk(one.getClient_employee_pk());
			two.setSales(one.getSales());
			two.setSales_name(one.getSales_name());
			two.setDelete_flg(one.getDelete_flg());
			two.setRelation_level(one.getRelation_level());
			two.setBack_level(one.getBack_level());
			two.setMarket_level(one.getMarket_level());
			two.setMonth_order_count(one.getMonth_order_count());
			two.setYear_order_count(one.getYear_order_count());
			two.setLast_confirm_date(one.getLast_confirm_date());
			two.setLast_order_period(one.getLast_order_period());
			two.setReceivable(one.getReceivable());
			two.setLast_receivable_period(one.getLast_receivable_period());
			two.setConnect_date(one.getConnect_date());
			two.setType(one.getType());
			two.setExtra_info(one.getExtra_info());

			clientRelationDao.insertClientRelation(two);
		}

	}
}
