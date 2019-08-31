package com.xinchi.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.util.dao.CommonDAO;
import com.xinchi.bean.SqlBean;

@Service
@Transactional
public class AutoUpdateClientRelation {

	@Autowired
	private CommonDAO commonDao;

	public void updateRelation(String[] param) {
		String sql1 = "update client_relation A  LEFT JOIN year_order_count B ON A.client_employee_pk = B.client_employee_pk set A.year_order_count = B.year_order_count;";
		String sql2 = "update client_relation A  LEFT JOIN receivable B ON A.client_employee_pk = B.client_employee_pk set A.receivable = ifnull(B.final_balance,B.budget_balance);";
		String sql3 = "update client_relation A  LEFT JOIN view_order_count B ON A.client_employee_pk = B.client_employee_pk set A.relation_level='尝试级' where B.order_count = 1;";
		String sql4 = "update client_relation A  LEFT JOIN view_order_count B ON A.client_employee_pk = B.client_employee_pk set A.relation_level='主力级' where B.day21_count >= 4;";
		String sql5 = "update client_relation A  LEFT JOIN view_order_count B ON A.client_employee_pk = B.client_employee_pk set A.relation_level='市场级' where B.order_count>=2 and B.day21_count < 4;";
		String sql6 = "update client_relation A  LEFT JOIN view_order_count B ON A.client_employee_pk = B.client_employee_pk set A.relation_level='忽略级' where datediff(now(),B.last_confirm_date)>30;";
		String sql7 = "UPDATE client_relation A LEFT JOIN view_order_count B ON A.client_employee_pk = B.client_employee_pk SET A.last_confirm_date = B.last_confirm_date, A.last_order_period = DATEDIFF(NOW(), B.last_confirm_date) WHERE B.last_confirm_date IS NOT NULL;";

		String sql8 = "update client_employee A  LEFT JOIN view_order_count B ON A.pk = B.client_employee_pk set A.relation_level='尝试级' where B.order_count = 1;";
		String sql9 = "update client_employee A  LEFT JOIN view_order_count B ON A.pk = B.client_employee_pk set A.relation_level='主力级' where B.day21_count >= 4;";
		String sql10 = "update client_employee A  LEFT JOIN view_order_count B ON A.pk = B.client_employee_pk set A.relation_level='市场级' where B.order_count>=2 and B.day21_count < 4;";
		String sql11 = "update client_employee A  LEFT JOIN view_order_count B ON A.pk = B.client_employee_pk set A.relation_level='忽略级' where datediff(now(),B.last_confirm_date)>30;";

		String sql12 = "update client_relation A  LEFT JOIN view_order_count B ON A.client_employee_pk = B.client_employee_pk set A.relation_level='尝试级' where B.order_count = 0;";
		String sql13 = "update client_employee A  LEFT JOIN view_order_count B ON A.pk = B.client_employee_pk set A.relation_level='尝试级' where B.order_count = 0;";
		SqlBean ss = new SqlBean();
		ss.setSql(sql1);
		commonDao.exeBySql(ss);
		ss.setSql(sql2);
		commonDao.exeBySql(ss);
		ss.setSql(sql3);
		commonDao.exeBySql(ss);
		ss.setSql(sql4);
		commonDao.exeBySql(ss);
		ss.setSql(sql5);
		commonDao.exeBySql(ss);
		ss.setSql(sql6);
		commonDao.exeBySql(ss);
		ss.setSql(sql7);
		commonDao.exeBySql(ss);
		ss.setSql(sql8);
		commonDao.exeBySql(ss);
		ss.setSql(sql9);
		commonDao.exeBySql(ss);
		ss.setSql(sql10);
		commonDao.exeBySql(ss);
		ss.setSql(sql11);
		commonDao.exeBySql(ss);
		ss.setSql(sql12);
		commonDao.exeBySql(ss);
		ss.setSql(sql13);
		commonDao.exeBySql(ss);

		// List<ClientRelationSummaryBean> views =
		// clientRelationDao.selectByParam(null);

		// for (ClientRelationSummaryBean one : views) {
		// ClientRelationBean two = new ClientRelationBean();
		// two.setClient_employee_name(one.getClient_employee_name());
		// two.setNick_name(one.getNick_name());
		// two.setClient_employee_pk(one.getClient_employee_pk());
		// two.setSales(one.getSales());
		// two.setSales_name(one.getSales_name());
		// two.setDelete_flg(one.getDelete_flg());
		// two.setRelation_level(one.getRelation_level());
		// two.setBack_level(one.getBack_level());
		// two.setMarket_level(one.getMarket_level());
		// two.setMonth_order_count(one.getMonth_order_count());
		// two.setYear_order_count(one.getYear_order_count());
		// two.setLast_confirm_date(one.getLast_confirm_date());
		// two.setLast_order_period(one.getLast_order_period());
		// two.setReceivable(one.getReceivable());
		// two.setLast_receivable_period(one.getLast_receivable_period());
		// two.setConnect_date(one.getConnect_date());
		// two.setType(one.getType());
		// two.setExtra_info(one.getExtra_info());
		//
		// clientRelationDao.insert(two);
		// }

	}
}
