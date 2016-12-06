package com.xinchi.backend.receivable.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.receivable.dao.ReceivedDAO;
import com.xinchi.backend.receivable.service.ReceivableService;
import com.xinchi.backend.receivable.service.ReceivedService;
import com.xinchi.bean.ClientReceivedDetailBean;
import com.xinchi.tools.Page;

@Service
@Transactional
public class ReceivedServiceImpl implements ReceivedService {

	@Autowired
	private ReceivedDAO dao;

	@Autowired
	private ReceivableService receivableService;

	@Override
	public void insert(ClientReceivedDetailBean detail) {

		dao.insert(detail);
	}

	@Override
	public void insertWithPk(ClientReceivedDetailBean detail) {

		dao.insertWithPk(detail);
	}

	@Override
	public List<ClientReceivedDetailBean> getAllReceivedsByPage(Page page) {
		return dao.getAllByPage(page);
	}

	@Override
	public String rollBackReceived(String received_pks) {
		String[] pks = received_pks.split(",");
		for (String pk : pks) {
			ClientReceivedDetailBean detail = dao.selectByPk(pk);
			detail.setReceived(BigDecimal.ZERO.subtract(detail.getReceived()));
			receivableService.updateReceivableReceived(detail);

			dao.deleteByPk(pk);
		}
		
		return "OK";
	}
}
