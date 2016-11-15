package com.xinchi.backend.util.action;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.receivable.service.ReceivableService;
import com.xinchi.backend.sale.service.FinalOrderService;
import com.xinchi.backend.sale.service.SaleOrderService;
import com.xinchi.backend.user.dao.UserDAO;
import com.xinchi.backend.user.service.UserService;
import com.xinchi.bean.BudgetOrderBean;
import com.xinchi.bean.FinalOrderBean;
import com.xinchi.bean.ReceivableBean;
import com.xinchi.bean.UserBaseBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.DateUtil;
import com.xinchi.common.SimpletinyString;
import com.xinchi.solr.service.SimpletinySolr;
import com.xinchi.tools.PropertiesUtil;

@Controller
@Scope("prototype")
public class SimpletinyAction extends BaseAction {

	private static final long serialVersionUID = 5764501280171459444L;

	@Autowired
	private UserService userService;

	public String changeAllPasswordToMD5() {
		List<UserBaseBean> users = new ArrayList<UserBaseBean>();
		users = userService.getAllByParam(null);
		for (UserBaseBean user : users) {
			String password = SimpletinyString.MD5(user.getPassword());
			user.setPassword(password);

			userService.update(user);
		}

		return SUCCESS;
	}

	@Autowired
	private SaleOrderService saleOrderService;

	@Autowired
	private FinalOrderService finalOrderService;

	@Autowired
	private ReceivableService receivableService;

	@Autowired
	private UserDAO userDao;

	@Autowired
	private SimpletinySolr solrService;

	public String autoGenReceivable() {
		SolrClient solr = solrService.getSolr(PropertiesUtil
				.getProperty("solr.receivableUrl"));
		List<BudgetOrderBean> budgets = saleOrderService.searchOrders(null);
		if (null != budgets) {
			for (BudgetOrderBean budget : budgets) {

				ReceivableBean receivable = new ReceivableBean();
				receivable.setTeam_number(budget.getTeam_number());
				receivable.setClient_employee_name(budget
						.getClient_employee_name());
				receivable
						.setClient_employee_pk(budget.getClient_employee_pk());
				receivable.setDeparture_date(budget.getDeparture_date());
				receivable.setReturn_date(budget.getReturn_date());
				receivable.setProduct(budget.getProduct());
				receivable.setPeople_count(budget.getPeople_count());
				receivable.setBudget_receivable(budget.getReceivable());
				receivable.setSales(budget.getCreate_user());

				UserBaseBean userBase = userDao.getByUserNumber(budget
						.getCreate_user());

				receivable.setSales_name(userBase.getUser_name());
				receivable.setReceived(BigDecimal.ZERO);

				FinalOrderBean finalOrder = finalOrderService
						.getFinalOrderByTeamNo(budget.getTeam_number());

				if (null != finalOrder) {
					receivable.setFinal_flg("Y");
					receivable.setFinal_receivable(finalOrder.getReceivable());
				}
				receivable.setBudget_balance(receivable.getBudget_receivable());

				if (null != receivable.getFinal_flg()
						&& receivable.getFinal_flg().equals("Y")) {
					receivable.setFinal_balance(receivable.getFinal_receivable());
				}
				
				receivableService.insert(receivable);

				SolrInputDocument document = castR2D(receivable);

				try {
					solr.add(document);
					solr.commit();
				} catch (SolrServerException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
		return SUCCESS;
	}

	private SolrInputDocument castR2D(ReceivableBean receivable) {
		SolrInputDocument document = new SolrInputDocument();
		document.addField("id", receivable.getPk());
		document.addField("team_number", receivable.getTeam_number());
		document.addField("final_flg", receivable.getFinal_flg());
		document.addField("client_employee_name",
				receivable.getClient_employee_name());
		document.addField("client_employee_pk",
				receivable.getClient_employee_pk());

		document.addField("departure_date",
				DateUtil.castStr2Date(receivable.getDeparture_date()));
		document.addField("return_date",
				DateUtil.castStr2Date(receivable.getReturn_date()));
		document.addField("product", receivable.getProduct());
		document.addField("people_count", receivable.getPeople_count());
		document.addField("budget_receivable", (null == receivable
				.getBudget_receivable() ? 0 : receivable.getBudget_receivable()
				.doubleValue()));

		document.addField("final_receivable", (null == receivable
				.getFinal_receivable() ? 0 : receivable.getFinal_receivable()
				.doubleValue()));

		document.addField("received", (null == receivable.getReceived() ? 0
				: receivable.getReceived().doubleValue()));

		document.addField("budget_balance", (null == receivable.getBudget_balance() ? 0
				: receivable.getBudget_balance().doubleValue()));
	
		document.addField("final_balance", (null == receivable.getFinal_balance() ? 0
				: receivable.getFinal_balance().doubleValue()));

		document.addField("sales", receivable.getSales());
		document.addField("sales_name", receivable.getSales_name());
		return document;
	}
}
