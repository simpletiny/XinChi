package com.xinchi.backend.finance.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.finance.dao.ReceivedMatchDAO;
import com.xinchi.backend.finance.service.ReceivedMatchService;
import com.xinchi.bean.ReceivedMatchBean;

@Service
@Transactional
public class ReceivedMatchServiceImpl implements ReceivedMatchService {
	@Autowired
	private ReceivedMatchDAO dao;

	@Override
	public String insert(ReceivedMatchBean bo) {

		return dao.insert(bo);
	}

	@Override
	public String update(ReceivedMatchBean bo) {

		dao.update(bo);
		return "success";
	}

	@Override
	public String delete(String pk) {
		dao.delete(pk);
		return "success";
	}

	@Override
	public List<ReceivedMatchBean> selectByDetailPk(String detailId) {
		return dao.selectByDetailPk(detailId);
	}

	@Override
	public List<ReceivedMatchBean> selectByReceivedPk(String received_pk) {

		return dao.selectByReceivedPk(received_pk);
	}

}
