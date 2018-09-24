package com.xinchi.backend.receivable.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Joiner;
import com.xinchi.backend.receivable.dao.ReceivableDAO;
import com.xinchi.backend.receivable.service.ReceivableService;
import com.xinchi.backend.sale.dao.SaleOrderDAO;
import com.xinchi.backend.sale.service.FinalOrderService;
import com.xinchi.backend.user.dao.UserDAO;
import com.xinchi.bean.BudgetOrderBean;
import com.xinchi.bean.ClientReceivedDetailBean;
import com.xinchi.bean.FinalOrderBean;
import com.xinchi.bean.ReceivableBean;
import com.xinchi.bean.ReceivableSummaryBean;
import com.xinchi.bean.UserBaseBean;
import com.xinchi.common.DateUtil;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.SimpletinyString;
import com.xinchi.solr.service.SimpletinySolr;
import com.xinchi.tools.Page;
import com.xinchi.tools.PropertiesUtil;

@Service
@Transactional
public class ReceivableServiceImpl implements ReceivableService {
	@Autowired
	private ReceivableDAO dao;

	@Override
	public void insert(ReceivableBean receivable) {
		dao.insert(receivable);
	}

	@Override
	public void update(ReceivableBean receivable) {
		dao.update(receivable);
	}

	@Override
	public ReceivableSummaryBean searchReceivableSummary(String sales) {
		return dao.selectReceivableSummary(sales);

	}

	@Autowired
	private SimpletinySolr solr;

	@Override
	public List<ReceivableBean> searchReceivableByPage(Page<ReceivableBean> page) {
		return dao.selectByPage(page);

		// SolrClient solrClient =
		// solr.getSolr(PropertiesUtil.getProperty("solr.receivableUrl"));
		//
		// // 计算合计
		// ReceivableBean rb = (ReceivableBean) page.getParams().get("bo");
		// String qStr = buildQuery(rb);
		// if (qStr.equals("")) {
		// qStr = "*:*";
		// }
		// SolrQuery query = new SolrQuery(qStr);
		// if (rb.getSort_type().equals("倒序")) {
		// query.add("sort", "departure_date desc");
		// } else if (rb.getSort_type().equals("正序")) {
		// query.add("sort", "departure_date asc");
		// }
		//
		// query.setStart(page.getStart());
		// query.setRows(page.getCount());
		// List<ReceivableBean> receivables = new ArrayList<ReceivableBean>();
		//
		// try {
		// QueryResponse response = solrClient.query(query);
		//
		// SolrDocumentList list = response.getResults();
		// page.setTotal((int) list.getNumFound());
		//
		// for (SolrDocument doc : list) {
		// ReceivableBean receivable = new ReceivableBean();
		// receivable.setTeam_number(safeGet(doc, "team_number"));
		// receivable.setPk(safeGet(doc, "id"));
		// receivable.setFinal_flg((null == safeGet(doc, "final_flg")) ? "N" :
		// safeGet(doc, "final_flg"));
		// receivable.setClient_employee_name(safeGet(doc,
		// "client_employee_name"));
		// receivable.setClient_employee_pk(safeGet(doc, "client_employee_pk"));
		// receivable.setDeparture_date(DateUtil.castDate2Str((Date)
		// doc.get("departure_date")));
		// receivable.setReturn_date(DateUtil.castDate2Str((Date)
		// doc.get("return_date")));
		// receivable.setProduct(safeGet(doc, "product"));
		// receivable.setPeople_count(SimpletinyString.str2Int(safeGet(doc,
		// "people_count")));
		// receivable.setFinancial_body_name(safeGet(doc,
		// "financial_body_name"));
		// receivable.setSales(safeGet(doc, "sales"));
		// receivable.setSales_name(safeGet(doc, "sales_name"));
		// receivable.setBudget_receivable(SimpletinyString.str2Decimal(safeGet(doc,
		// "budget_receivable")));
		// receivable.setFinal_receivable(SimpletinyString.str2Decimal(safeGet(doc,
		// "final_receivable")));
		// receivable.setReceived(SimpletinyString.str2Decimal(safeGet(doc,
		// "received")));
		// receivable.setBudget_balance(SimpletinyString.str2Decimal(safeGet(doc,
		// "budget_balance")));
		// receivable.setFinal_balance(SimpletinyString.str2Decimal(safeGet(doc,
		// "final_balance")));
		// receivable.setBack_days(getBackDays(receivable.getDeparture_date(),
		// receivable.getReturn_date()));
		// receivables.add(receivable);
		// }
		// return receivables;
		// } catch (SolrServerException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

		// SimpleOrderedMap
		// return null;
	}

	private String getBackDays(String departure_date, String return_date) {
		String today = DateUtil.getDateStr("yyyy-MM-dd");

		if (departure_date.compareTo(today) > 0) {
			return ResourcesConstants.TEAM_STATUS_BEFORE;
		}

		if (return_date.compareTo(today) <= 0) {
			return String.valueOf(DateUtil.dateDiff(return_date));
		}

		if (return_date.compareTo(today) > 0) {
			return ResourcesConstants.TEAM_STATUS_NOT_RETURN;
		}

		return "ERROR";
	}

	private String buildQuery(ReceivableBean options) {
		String separator = " AND ";
		List<String> queryParts = new ArrayList<String>();

		if (!SimpletinyString.isEmpty(options.getClient_employee_name())) {
			queryParts.add("client_employee_name:\"" + options.getClient_employee_name() + "\"");
		}

		if (!SimpletinyString.isEmpty(options.getSales_name())) {
			queryParts.add("sales_name:\"" + options.getSales_name() + "\"");
		}

		if (!SimpletinyString.isEmpty(options.getFinancial_body_name())) {
			queryParts.add("financial_body_name:\"" + options.getFinancial_body_name() + "\"");
		}

		if (!SimpletinyString.isEmpty(options.getSales())) {
			queryParts.add("sales:\"" + options.getSales() + "\"");
		}

		String team_status = options.getTeam_status();
		String from = "";
		String to = "";
		if (!SimpletinyString.isEmpty(team_status)) {
			if (team_status.equals(ResourcesConstants.TEAM_STATUS_BEFORE)) {
				from = DateUtil.getUTC();
				to = "*";
				queryParts.add("departure_date:[" + from + " TO " + to + "]");
			} else if (team_status.equals(ResourcesConstants.TEAM_STATUS_AFTER)) {
				from = "*";
				to = DateUtil.getUTC();
				queryParts.add("departure_date:[" + from + " TO " + to + "]");
			} else if (team_status.equals(ResourcesConstants.TEAM_STATUS_RETURN)) {
				from = "*";
				to = DateUtil.getUTC();
				queryParts.add("return_date:[" + from + " TO " + to + "]");
			}
		}

		String departure_month = options.getDeparture_month();
		if (!SimpletinyString.isEmpty(departure_month)) {
			from = DateUtil.getUTC(departure_month + "-01");
			to = DateUtil.getUTC(DateUtil.getLastDay(departure_month));
			queryParts.add("departure_date:[" + from + " TO " + to + "]");
		}
		return Joiner.on(separator).join(queryParts);
	}

	private String safeGet(SolrDocument doc, String key) {
		return doc.get(key) != null ? doc.get(key).toString() : null;
	}

	@Autowired
	private UserDAO userDao;
	@Autowired
	private SaleOrderDAO budgetDao;
	@Autowired
	private FinalOrderService finalOrderService;

	@Override
	@Async
	public void updateByTeamNumber(String teamNumber) {
		SolrClient solrClient = solr.getSolr(PropertiesUtil.getProperty("solr.receivableUrl"));
		ReceivableBean receivable = dao.selectReceivableByTeamNumber(teamNumber);

		if (null == receivable) {
			receivable = new ReceivableBean();
			BudgetOrderBean budget = budgetDao.selectBudgetOrderByTeamNumber(teamNumber);

			receivable.setTeam_number(budget.getTeam_number());
			receivable.setClient_employee_name(budget.getClient_employee_name());
			receivable.setClient_employee_pk(budget.getClient_employee_pk());
			receivable.setDeparture_date(budget.getDeparture_date());
			receivable.setReturn_date(budget.getReturn_date());
			receivable.setProduct(budget.getProduct());
			receivable.setPeople_count(budget.getPeople_count());
			receivable.setBudget_receivable(budget.getReceivable());
			receivable.setSales(budget.getCreate_user());

			UserBaseBean userBase = userDao.getByUserNumber(budget.getCreate_user());

			receivable.setSales_name(userBase.getUser_name());
			receivable.setReceived(BigDecimal.ZERO);

			receivable.setBudget_balance(receivable.getBudget_receivable());

			insert(receivable);
		} else {
			FinalOrderBean finalOrder = finalOrderService.getFinalOrderByTeamNo(teamNumber);
			BudgetOrderBean budget = budgetDao.selectBudgetOrderByTeamNumber(teamNumber);

			receivable.setClient_employee_name(budget.getClient_employee_name());
			receivable.setClient_employee_pk(budget.getClient_employee_pk());
			receivable.setDeparture_date(budget.getDeparture_date());
			receivable.setReturn_date(budget.getReturn_date());
			receivable.setProduct(budget.getProduct());
			receivable.setPeople_count(budget.getPeople_count());
			receivable.setBudget_receivable(budget.getReceivable());
			receivable.setBudget_balance(budget.getReceivable().subtract(receivable.getReceived()));

			if (null != finalOrder) {
				receivable.setFinal_flg("Y");
				receivable.setFinal_receivable(finalOrder.getReceivable());
				receivable.setFinal_balance(finalOrder.getReceivable().subtract(receivable.getReceived()));
			} else {
				receivable.setFinal_flg("N");
				receivable.setFinal_receivable(null);
				receivable.setFinal_balance(null);
			}

			update(receivable);

		}
		// SolrInputDocument document = castR2D(receivable);
		// try {
		// solrClient.add(document);
		// solrClient.commit();
		// } catch (SolrServerException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
	}

	private SolrInputDocument castR2D(ReceivableBean receivable) {
		SolrInputDocument document = new SolrInputDocument();

		document.addField("id", receivable.getPk());
		document.addField("team_number", receivable.getTeam_number());
		document.addField("final_flg", receivable.getFinal_flg());
		document.addField("client_employee_name", receivable.getClient_employee_name());
		document.addField("client_employee_pk", receivable.getClient_employee_pk());

		document.addField("departure_date", DateUtil.castStr2Date(receivable.getDeparture_date()));
		document.addField("return_date", DateUtil.castStr2Date(receivable.getReturn_date()));
		document.addField("product", receivable.getProduct());
		document.addField("people_count", receivable.getPeople_count());
		document.addField("budget_receivable",
				(null == receivable.getBudget_receivable() ? 0 : receivable.getBudget_receivable().doubleValue()));

		document.addField("final_receivable",
				(null == receivable.getFinal_receivable() ? 0 : receivable.getFinal_receivable().doubleValue()));

		document.addField("received", (null == receivable.getReceived() ? 0 : receivable.getReceived().doubleValue()));

		document.addField("budget_balance",
				(null == receivable.getBudget_balance() ? 0 : receivable.getBudget_balance().doubleValue()));

		document.addField("final_balance",
				(null == receivable.getFinal_balance() ? 0 : receivable.getFinal_balance().doubleValue()));
		document.addField("financial_body_name", receivable.getFinancial_body_name());
		document.addField("financial_body_pk", receivable.getFinancial_body_pk());
		document.addField("sales", receivable.getSales());
		document.addField("sales_name", receivable.getSales_name());
		return document;
	}

	@Override
	public void updateReceivableReceived(ClientReceivedDetailBean detail) {
		ReceivableBean receivable = dao.selectReceivableByTeamNumber(detail.getTeam_number());

		receivable.setReceived(receivable.getReceived().add(detail.getReceived()));
		receivable.setBudget_balance(receivable.getBudget_balance().subtract(detail.getReceived()));

		if (receivable.getFinal_flg().equals("Y")) {
			receivable.setFinal_balance(receivable.getFinal_balance().subtract(detail.getReceived()));
		}

		dao.update(receivable);
	}

	@Override
	public ReceivableBean selectByTeamNumber(String team_number) {
		return dao.selectReceivableByTeamNumber(team_number);
	}

	@Override
	public BigDecimal fetchEmployeeBalance(String client_employee_pk) {
		return dao.fetchEmployeeBalance(client_employee_pk);
	}
}
