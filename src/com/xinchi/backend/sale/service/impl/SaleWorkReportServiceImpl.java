package com.xinchi.backend.sale.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.client.dao.AccurateSaleDAO;
import com.xinchi.backend.client.dao.ClientVisitDAO;
import com.xinchi.backend.sale.dao.SaleWorkReportDAO;
import com.xinchi.backend.sale.service.SaleWorkReportService;
import com.xinchi.bean.AccurateSaleBean;
import com.xinchi.bean.ClientVisitBean;
import com.xinchi.bean.SaleWorkReportDto;
import com.xinchi.tools.Page;

@Service
@Transactional
public class SaleWorkReportServiceImpl implements SaleWorkReportService {
	@Autowired
	private SaleWorkReportDAO dao;

	@Autowired
	private ClientVisitDAO clientVisitDao;

	@Autowired
	private AccurateSaleDAO accurateSaleDao;

	@Override
	public List<SaleWorkReportDto> selectSwrByPage(Page<SaleWorkReportDto> page) {
		List<SaleWorkReportDto> reports = new ArrayList<SaleWorkReportDto>();
		reports = dao.selectSwrByPage(page);
		// 查询拜访和精推
		for (SaleWorkReportDto report : reports) {
			ClientVisitBean option1 = new ClientVisitBean();
			option1.setDate(report.getDate());
			option1.setClient_employee_pk(report.getClient_employee_pk());
			List<ClientVisitBean> visits = clientVisitDao.selectByParam(option1);
			report.setVisits(visits);

			AccurateSaleBean option2 = new AccurateSaleBean();
			option2.setDate(report.getDate());
			option2.setClient_employee_pk(report.getClient_employee_pk());
			List<AccurateSaleBean> accurates = accurateSaleDao.selectByParam(option2);
			report.setAccurates(accurates);
		}

		return reports;
	}
}