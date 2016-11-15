package com.xinchi.backend.receivable.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Joiner;
import com.xinchi.backend.receivable.dao.ReceivableDAO;
import com.xinchi.backend.receivable.service.ReceivableService;
import com.xinchi.bean.ReceivableBean;
import com.xinchi.bean.ReceivableSummaryBean;
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
	public ReceivableSummaryBean searchReceivableSummary(String sales) {
		return dao.selectReceivableSummary(sales);

	}

	@Autowired
	private SimpletinySolr solr;

	@Override
	public List<ReceivableBean> searchReceivableByPage(Page<ReceivableBean> page) {
		SolrClient solrClient = solr.getSolr(PropertiesUtil
				.getProperty("solr.receivableUrl"));

		// 计算合计
		String qStr = buildQuery((ReceivableBean) page.getParams().get("bo"));
		if (qStr.equals("")) {
			qStr = "*:*";
		}
		SolrQuery query = new SolrQuery(qStr);
		query.setStart(page.getStart());
		query.setRows(page.getCount());
		List<ReceivableBean> receivables = new ArrayList<ReceivableBean>();

		try {
			QueryResponse response = solrClient.query(query);

			SolrDocumentList list = response.getResults();
			page.setTotal((int) list.getNumFound());

			for (SolrDocument doc : list) {
				ReceivableBean receivable = new ReceivableBean();
				receivable.setTeam_number(safeGet(doc, "team_number"));
				receivable.setPk(safeGet(doc, "id"));
				receivable.setFinal_flg((null==safeGet(doc, "final_flg")?"N":"Y"));
				receivable.setClient_employee_name(safeGet(doc,
						"client_employee_name"));
				receivable.setClient_employee_pk(safeGet(doc,
						"client_employee_pk"));
				receivable.setDeparture_date(DateUtil.castDate2Str((Date) doc
						.get("departure_date")));
				receivable.setReturn_date(DateUtil.castDate2Str((Date) doc
						.get("return_date")));
				receivable.setProduct(safeGet(doc, "product"));
				receivable.setPeople_count(SimpletinyString.str2Int(safeGet(
						doc, "people_count")));
				receivable.setSales(safeGet(doc, "sales"));
				receivable.setSales_name(safeGet(doc, "sales_name"));
				receivable.setBudget_receivable(SimpletinyString
						.str2Decimal(safeGet(doc, "budget_receivable")));
				receivable.setFinal_receivable(SimpletinyString
						.str2Decimal(safeGet(doc, "final_receivable")));
				receivable.setReceived(SimpletinyString.str2Decimal(safeGet(
						doc, "received")));
				receivable.setBudget_balance(SimpletinyString
						.str2Decimal(safeGet(doc, "budget_balance")));
				receivable.setFinal_balance(SimpletinyString
						.str2Decimal(safeGet(doc, "final_balance")));
				receivable.setBack_days(getBackDays(
						receivable.getDeparture_date(),
						receivable.getReturn_date()));
				receivables.add(receivable);
			}
			return receivables;
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// SimpleOrderedMap
		return null;
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
			queryParts.add("client_employee_name:\""
					+ options.getClient_employee_name() + "\"");
		}

		if (!SimpletinyString.isEmpty(options.getSales_name())) {
			queryParts.add("sales_name:\"" + options.getSales_name() + "\"");
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
			} else if (team_status
					.equals(ResourcesConstants.TEAM_STATUS_RETURN)) {
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
}
