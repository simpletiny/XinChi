package com.xinchi.backend.payable.service.impl;

import static com.xinchi.common.SimpletinyString.isEmpty;
import static com.xinchi.common.SimpletinyString.str2Decimal;
import static com.xinchi.common.SimpletinyString.str2Int;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Joiner;
import com.xinchi.backend.payable.dao.PayableDAO;
import com.xinchi.backend.payable.service.PayableService;
import com.xinchi.backend.sale.service.FinalOrderService;
import com.xinchi.backend.sale.service.SaleOrderService;
import com.xinchi.backend.supplier.dao.SupplierEmployeeDAO;
import com.xinchi.bean.BudgetOrderBean;
import com.xinchi.bean.BudgetOrderSupplierBean;
import com.xinchi.bean.FinalOrderBean;
import com.xinchi.bean.FinalOrderSupplierBean;
import com.xinchi.bean.PayableBean;
import com.xinchi.bean.PayableSummaryBean;
import com.xinchi.bean.SupplierEmployeeBean;
import com.xinchi.bean.SupplierPaidDetailBean;
import com.xinchi.common.DateUtil;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.solr.service.SimpletinySolr;
import com.xinchi.tools.Page;
import com.xinchi.tools.PropertiesUtil;

@Service
@Transactional
public class PayableServiceImpl implements PayableService {
	@Autowired
	private PayableDAO dao;

	@Override
	public void insert(PayableBean payable) {
		dao.insert(payable);
	}

	@Autowired
	private SimpletinySolr solr;

	@Autowired
	private SaleOrderService saleOrderService;

	@Autowired
	private FinalOrderService finalOrderService;

	@Autowired
	private SupplierEmployeeDAO supplierEmployeeDAO;

	@Override
	@Async
	public void updateByTeamNumber(String team_number) {
		SolrClient solrClient = solr.getSolr(PropertiesUtil.getProperty("solr.payableUrl"));

		List<BudgetOrderSupplierBean> budgets = saleOrderService.searchBudgetSupplier(team_number);
		BudgetOrderBean bob = saleOrderService.searchBudgetOrderByTeamNumber(team_number);

		if (null != budgets) {
			for (BudgetOrderSupplierBean budget : budgets) {

				PayableBean bo = new PayableBean();
				bo.setTeam_number(team_number);
				bo.setSupplier_employee_pk(budget.getSupplier_employee_pk());

				SupplierEmployeeBean sb = supplierEmployeeDAO.selectByPrimaryKey(budget.getSupplier_employee_pk());

				List<PayableBean> payables = dao.selectByParam(bo);
				PayableBean payable = null;
				if (null != payables && payables.size() > 0)
					payable = payables.get(0);

				if (null == payable) {
					payable = new PayableBean();
					payable.setTeam_number(team_number);

					payable.setSupplier_employee_name(budget.getSupplier_employee_name());
					payable.setSupplier_employee_pk(budget.getSupplier_employee_pk());

					payable.setDeparture_date(bob.getDeparture_date());
					payable.setReturn_date(bob.getReturn_date());

					payable.setProduct(bob.getProduct());
					payable.setPeople_count(bob.getPeople_count());
					payable.setBudget_payable(budget.getPayable());
					payable.setSales(bob.getCreate_user());

					payable.setSales_name(String.valueOf(ResourcesConstants.MAP_USER_NO.get(bob.getCreate_user())));

					payable.setPaid(BigDecimal.ZERO);
					payable.setBudget_balance(budget.getPayable());
					insert(payable);

				} else {
					FinalOrderBean finalOrder = finalOrderService.getFinalOrderByTeamNo(team_number);

					payable.setSupplier_employee_name(budget.getSupplier_employee_name());
					payable.setSupplier_employee_pk(budget.getSupplier_employee_pk());

					payable.setDeparture_date(bob.getDeparture_date());
					payable.setReturn_date(bob.getReturn_date());

					payable.setProduct(bob.getProduct());
					payable.setPeople_count(bob.getPeople_count());
					payable.setBudget_payable(budget.getPayable());
					payable.setBudget_balance(budget.getPayable().subtract(payable.getPaid()));

					if (null != finalOrder) {
						payable.setFinal_flg("Y");

						FinalOrderSupplierBean fosb = new FinalOrderSupplierBean();

						fosb.setTeam_number(team_number);
						fosb.setSupplier_employee_pk(budget.getSupplier_employee_pk());

						List<FinalOrderSupplierBean> fosbs = finalOrderService.searchFinalSupplierByParam(fosb);
						fosb = fosbs.get(0);

						payable.setFinal_payable(fosb.getPayable());
						payable.setFinal_balance(fosb.getPayable().subtract(payable.getPaid()));

					} else {
						payable.setFinal_flg("N");
						payable.setFinal_payable(null);
						payable.setFinal_balance(null);
					}

					update(payable);
				}

				// payable.setSupplier_name(sb.getFinancial_body_name());
				// payable.setSupplier_pk(sb.getFinancial_body_pk());
				//
				// SolrInputDocument document = castP2D(payable);
				//
				// try {
				// solrClient.add(document);
				// solrClient.commit();
				// } catch (SolrServerException e) {
				// e.printStackTrace();
				// } catch (IOException e) {
				// e.printStackTrace();
				// }

			}
		}
	}

	@Override
	public void update(PayableBean payable) {
		dao.update(payable);
	}

	private SolrInputDocument castP2D(PayableBean payable) {
		SolrInputDocument document = new SolrInputDocument();
		document.addField("id", payable.getPk());
		document.addField("team_number", payable.getTeam_number());
		document.addField("final_flg", payable.getFinal_flg());
		document.addField("supplier_employee_name", payable.getSupplier_employee_name());
		document.addField("supplier_employee_pk", payable.getSupplier_employee_pk());
		document.addField("supplier_name", payable.getSupplier_name());
		document.addField("supplier_pk", payable.getSupplier_pk());
		document.addField("departure_date", DateUtil.castStr2Date(payable.getDeparture_date()));
		document.addField("return_date", DateUtil.castStr2Date(payable.getReturn_date()));
		document.addField("product", payable.getProduct());
		document.addField("people_count", payable.getPeople_count());
		document.addField("budget_payable", (null == payable.getBudget_payable() ? 0 : payable.getBudget_payable().doubleValue()));

		document.addField("final_payable", (null == payable.getFinal_payable() ? 0 : payable.getFinal_payable().doubleValue()));

		document.addField("paid", (null == payable.getPaid() ? 0 : payable.getPaid().doubleValue()));

		document.addField("budget_balance", (null == payable.getBudget_balance() ? 0 : payable.getBudget_balance().doubleValue()));

		document.addField("final_balance", (null == payable.getFinal_balance() ? 0 : payable.getFinal_balance().doubleValue()));

		document.addField("sales", payable.getSales());
		document.addField("sales_name", payable.getSales_name());

		return document;
	}

	@Override
	public List<PayableBean> searchPayableByPage(Page<PayableBean> page) {

		return dao.selectByPage(page);
		// SolrClient solrClient =
		// solr.getSolr(PropertiesUtil.getProperty("solr.payableUrl"));
		// // 计算合计
		// PayableBean pb = (PayableBean) page.getParams().get("bo");
		// String qStr = buildQuery(pb);
		// if (qStr.equals("")) {
		// qStr = "*:*";
		// }
		// SolrQuery query = new SolrQuery(qStr);
		// if (pb.getSort_type().equals("倒序")) {
		// query.add("sort", "departure_date desc");
		// } else if (pb.getSort_type().equals("正序")) {
		// query.add("sort", "departure_date asc");
		// }
		//
		// query.setStart(page.getStart());
		// query.setRows(page.getCount());
		// List<PayableBean> payables = new ArrayList<PayableBean>();
		//
		// try {
		// QueryResponse response = solrClient.query(query);
		//
		// SolrDocumentList list = response.getResults();
		// page.setTotal((int) list.getNumFound());
		//
		// for (SolrDocument doc : list) {
		// PayableBean payable = new PayableBean();
		// payable.setTeam_number(safeGet(doc, "team_number"));
		// payable.setPk(safeGet(doc, "id"));
		// payable.setFinal_flg((null == safeGet(doc, "final_flg")) ? "N" :
		// safeGet(doc, "final_flg"));
		// payable.setSupplier_employee_name(safeGet(doc,
		// "supplier_employee_name"));
		// payable.setSupplier_employee_pk(safeGet(doc,
		// "supplier_employee_pk"));
		//
		// payable.setSupplier_pk(safeGet(doc, "supplier_pk"));
		// payable.setSupplier_name(safeGet(doc, "supplier_name"));
		//
		// payable.setDeparture_date(DateUtil.castDate2Str((Date)
		// doc.get("departure_date")));
		// payable.setReturn_date(DateUtil.castDate2Str((Date)
		// doc.get("return_date")));
		// payable.setProduct(safeGet(doc, "product"));
		// payable.setPeople_count(str2Int(safeGet(doc, "people_count")));
		// payable.setSales(safeGet(doc, "sales"));
		// payable.setSales_name(safeGet(doc, "sales_name"));
		// payable.setBudget_payable(str2Decimal(safeGet(doc,
		// "budget_payable")));
		// payable.setFinal_payable(str2Decimal(safeGet(doc, "final_payable")));
		// payable.setPaid(str2Decimal(safeGet(doc, "paid")));
		// payable.setBudget_balance(str2Decimal(safeGet(doc,
		// "budget_balance")));
		// payable.setFinal_balance(str2Decimal(safeGet(doc, "final_balance")));
		// payable.setBack_days(getBackDays(payable.getDeparture_date(),
		// payable.getReturn_date()));
		//
		// payables.add(payable);
		// }
		// return payables;
		// } catch (SolrServerException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		//
		// // SimpleOrderedMap
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

	private String buildQuery(PayableBean options) {
		String separator = " AND ";
		List<String> queryParts = new ArrayList<String>();

		if (!isEmpty(options.getSupplier_employee_name())) {
			queryParts.add("supplier_employee_name:\"" + options.getSupplier_employee_name() + "\"");
		}

		if (!isEmpty(options.getSupplier_name())) {
			queryParts.add("supplier_name:\"" + options.getSupplier_name() + "\"");
		}

		if (!isEmpty(options.getTeam_number())) {
			queryParts.add("team_number:\"" + options.getTeam_number() + "\"");
		}

		if (!isEmpty(options.getSales_name())) {
			queryParts.add("sales_name:\"" + options.getSales_name() + "\"");
		}

		if (!isEmpty(options.getSales())) {
			queryParts.add("sales:\"" + options.getSales() + "\"");
		}

		String team_status = options.getTeam_status();
		String from = "";
		String to = "";
		if (!isEmpty(team_status)) {
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
		if (!isEmpty(departure_month)) {
			from = DateUtil.getUTC(departure_month + "-01");
			to = DateUtil.getUTC(DateUtil.getLastDay(departure_month));
			queryParts.add("departure_date:[" + from + " TO " + to + "]");
		}
		return Joiner.on(separator).join(queryParts);
	}

	private String safeGet(SolrDocument doc, String key) {
		return doc.get(key) != null ? doc.get(key).toString() : null;
	}

	@Override
	public PayableSummaryBean searchPayableSummary(String user_number) {
		return dao.selectPayableSummary(user_number);
	}

	@Override
	public void updatePayablePaid(SupplierPaidDetailBean detail) {
		PayableBean options = new PayableBean();
		options.setTeam_number(detail.getTeam_number());
		options.setSupplier_employee_pk(detail.getSupplier_employee_pk());

		List<PayableBean> payables = dao.selectByParam(options);
		PayableBean payable = new PayableBean();
		if (null != payables && payables.size() > 0) {
			payable = payables.get(0);
		} else {
			return;
		}

		payable.setPaid(payable.getPaid().add(detail.getMoney()));
		payable.setBudget_balance(payable.getBudget_balance().subtract(detail.getMoney()));

		if (payable.getFinal_flg().equals("Y")) {
			payable.setFinal_balance(payable.getFinal_balance().subtract(detail.getMoney()));
		}
		dao.update(payable);

		// SolrClient solrClient =
		// solr.getSolr(PropertiesUtil.getProperty("solr.payableUrl"));
		//
		// SolrInputDocument document = castP2D(payable);
		// try {
		// solrClient.add(document);
		// solrClient.commit();
		// } catch (SolrServerException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
	}

	@Override
	public void deletePayableByTeamNumber(String team_number) {
		SolrClient solrClient = solr.getSolr(PropertiesUtil.getProperty("solr.payableUrl"));
		PayableBean options = new PayableBean();
		options.setTeam_number(team_number);
		List<PayableBean> payables = dao.selectByParam(options);
		List<String> ids = new ArrayList<String>();
		for (PayableBean payable : payables) {
			ids.add(payable.getPk());
		}
		try {
			if (ids.size() > 0) {
				solrClient.deleteById(ids);
				solrClient.commit();
			}
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
		dao.deleteByTeamNumber(team_number);
	}

	@Override
	public List<PayableBean> selectByParam(PayableBean payable) {

		return dao.selectByParam(payable);
	}

	@Override
	public void deleteByPk(String pk) {
		dao.deleteByPk(pk);
	}
}
