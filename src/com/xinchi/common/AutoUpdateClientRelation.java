package com.xinchi.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.client.dao.ClientEmployeeTypeCountDAO;
import com.xinchi.backend.client.dao.ClientEmployeeUserDAO;
import com.xinchi.backend.client.dao.ClientRelationDAO;
import com.xinchi.backend.util.dao.CommonDAO;
import com.xinchi.bean.ClientEmployeeTypeCountBean;
import com.xinchi.bean.PotentialDto;
import com.xinchi.bean.SqlBean;

@Service
@Transactional
public class AutoUpdateClientRelation {

	@Autowired
	private CommonDAO commonDao;

	@Autowired
	private ClientRelationDAO clientRelationDao;

	@Autowired
	private ClientEmployeeUserDAO employeeUserDao;

	@Autowired
	private ClientEmployeeTypeCountDAO clientEmployeeTypeCountDao;

	/*
	 * 1、新增客户。没有产生订单。 2、产生订单，成为尝试客户。（所有尝试客户都是只有一单的客户）
	 * 3、尝试客户再增加一单，既只要客户给过两单就自动成为市场客户（目前没做时间限制，没做多长时间给两单。后期再再说吧，初步就是一年）
	 * 4、市场客户，在21天内够4单（出团日计算，当天出团和未出团也计入）。 5、主力客户，一但不满足条件，自动划归为市场客户。
	 * 6、市场客户到忽略，签单期间（以出团日计算）超过30天，化作忽略。 7、忽略客户，如果产生订单，重新划归市场客户。
	 * 8、忽略客户，如果63天没有产生订单，强制公开。
	 */
	public void updateRelation(String[] param) {
		String sql1 = "update client_relation A  LEFT JOIN year_order_count B ON A.client_employee_pk = B.client_employee_pk set A.year_order_count = B.year_order_count;";
		String sql2 = "update client_relation A  LEFT JOIN receivable B ON A.client_employee_pk = B.client_employee_pk set A.receivable = ifnull(B.final_balance,B.budget_balance);";
		String sql3 = "update client_relation A  LEFT JOIN view_order_count B ON A.client_employee_pk = B.client_employee_pk set A.relation_level='尝试级' where B.order_count = 1;";
		String sql4 = "update client_relation A  LEFT JOIN view_order_count B ON A.client_employee_pk = B.client_employee_pk set A.relation_level='主力级' where B.day90_count >= 6 and B.day30_count<5;";
		String sql5 = "update client_relation A  LEFT JOIN view_order_count B ON A.client_employee_pk = B.client_employee_pk set A.relation_level='市场级' where B.order_count>=2 and B.day90_count < 6 and B.day30_count<5;";
		// 新增核心级
		String sql14 = "update client_relation A  LEFT JOIN view_order_count B ON A.client_employee_pk = B.client_employee_pk set A.relation_level='核心级' where B.day30_count>=5;";
		String sql6 = "update client_relation A  LEFT JOIN view_order_count B ON A.client_employee_pk = B.client_employee_pk set A.relation_level='忽略级' where (B.last_confirm_date is null OR datediff(now(),B.last_confirm_date)>60) and B.order_count!=0;";
		String sql7 = "UPDATE client_relation A LEFT JOIN view_order_count B ON A.client_employee_pk = B.client_employee_pk SET A.last_confirm_date = B.last_confirm_date, A.last_order_period = IF(B.last_confirm_date is null,null,DATEDIFF(NOW(), B.last_confirm_date));";

		String sql8 = "update client_employee A  LEFT JOIN view_order_count B ON A.pk = B.client_employee_pk set A.relation_level='尝试级' where B.order_count = 1;";
		String sql9 = "update client_employee A  LEFT JOIN view_order_count B ON A.pk = B.client_employee_pk set A.relation_level='主力级' where B.day90_count >= 6 and B.day30_count<5;";
		String sql10 = "update client_employee A  LEFT JOIN view_order_count B ON A.pk = B.client_employee_pk set A.relation_level='市场级' where B.order_count>=2 and B.day90_count < 6 and B.day30_count<5;";
		String sql11 = "update client_employee A  LEFT JOIN view_order_count B ON A.pk = B.client_employee_pk set A.relation_level='忽略级' where(B.last_confirm_date is null OR datediff(now(),B.last_confirm_date)>60);";
		// 新增核心级
		String sql15 = "update client_employee A  LEFT JOIN view_order_count B ON A.pk = B.client_employee_pk set A.relation_level='核心级' where B.day30_count>=5;";

		String sql12 = "update client_relation A  LEFT JOIN view_order_count B ON A.client_employee_pk = B.client_employee_pk set A.relation_level='新增级' where B.order_count = 0 or B.order_count is null;";
		String sql13 = "update client_employee A  LEFT JOIN view_order_count B ON A.pk = B.client_employee_pk set A.relation_level='新增级' where B.order_count = 0 or B.order_count is null;";
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
		ss.setSql(sql14);
		commonDao.exeBySql(ss);
		ss.setSql(sql15);
		commonDao.exeBySql(ss);
		// 公开客户，条件签单期间大于63天的。
		// 暂时不公开客户
		// List<ClientRelationBean> crs = clientRelationDao.selectNeedPublic();
		// for (ClientRelationBean cr : crs) {
		// // 删除之前存在的对应关系
		// employeeUserDao.deleteByEmployeePk(cr.getClient_employee_pk());
		// // 保存新的对应关系
		// ClientEmployeeUserBean ceub = new ClientEmployeeUserBean();
		// ceub.setEmployee_pk(cr.getClient_employee_pk());
		// ceub.setUser_pk(ResourcesConstants.USER_PUBLIC);
		// ceub.setCreate_user(ResourcesConstants.USER_ADMIN_NUMBER);
		// employeeUserDao.insertWithoutLogin(ceub);
		//
		// String sqlx = "update client_employee set public_flg='Y',update_time='" +
		// DateUtil.getTimeMillis()
		// + "',update_user='N00000' where pk='" + cr.getClient_employee_pk() + "';";
		// String sqly = "update client_relation set
		// sales='public',sales_name='公开',update_time='"
		// + DateUtil.getTimeMillis() + "',update_user='N00000' where
		// client_employee_pk='"
		// + cr.getClient_employee_pk() + "';";
		// ss.setSql(sqlx);
		// commonDao.exeBySql(ss);
		// ss.setSql(sqly);
		// commonDao.exeBySql(ss);
		// }

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

		// 更新月末数据
		if (DateUtil.todayOfMonth() == 1) {
			List<PotentialDto> lasts = clientRelationDao.selectTypeCount();
			String month = DateUtil.lastMonth();
			for (PotentialDto last : lasts) {
				ClientEmployeeTypeCountBean cetc = new ClientEmployeeTypeCountBean();
				cetc.setUser_pk(last.getUser_pk());
				cetc.setCore_count(last.getCore_all());
				cetc.setMain_count(last.getMain_all());
				cetc.setMarket_count(last.getMarket_all());
				cetc.setTry_count(last.getTry_all());
				cetc.setIgnore_count(last.getIgnore_all());
				cetc.setNew_count(last.getNew_all());
				cetc.setMonth(month);

				clientEmployeeTypeCountDao.insert(cetc);
			}
		}
	}
}
