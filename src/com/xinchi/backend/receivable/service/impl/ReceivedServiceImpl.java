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
import com.xinchi.common.ResourcesConstants;
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
			if (detail.getType().equals(ResourcesConstants.RECEIVED_TYPE_SUM)) {
				String[] related_pks = detail.getRelated_pk().split(",");
				for (String related : related_pks) {
					if (related.equals(detail.getPk()))
						continue;
					ClientReceivedDetailBean related_detail = dao.selectByPk(related);
					doRollBack(related_detail);
				}

			}
			doRollBack(detail);

		}

		return "OK";
	}

	private void doRollBack(ClientReceivedDetailBean detail) {
		detail.setReceived(BigDecimal.ZERO.subtract(detail.getReceived()));
		receivableService.updateReceivableReceived(detail);
		dao.deleteByPk(detail.getPk());
	}

	@Override
	public List<ClientReceivedDetailBean> selectByRelatedPks(String related_pks) {
		return dao.selectByRelatedPks(related_pks);
	}

	@Override
	public void update(ClientReceivedDetailBean detail) {
		dao.update(detail);
	}

	@Override
	public ClientReceivedDetailBean selectByPk(String received_pk) {
		return dao.selectByPk(received_pk);
	}
}
