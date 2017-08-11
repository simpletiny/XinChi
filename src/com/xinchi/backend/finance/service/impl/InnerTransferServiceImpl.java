package com.xinchi.backend.finance.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.finance.dao.InnerTransferDAO;
import com.xinchi.backend.finance.service.InnerTransferService;
import com.xinchi.bean.InnerTransferBean;
import com.xinchi.tools.Page;

@Service
@Transactional
public class InnerTransferServiceImpl implements InnerTransferService {

	@Autowired
	private InnerTransferDAO dao;

	@Override
	public InnerTransferBean selectByPrimaryKey(String id) {
		return dao.selectByPrimaryKey(id);
	}

	@Override
	public List<InnerTransferBean> selectByParam(InnerTransferBean bean) {
		return dao.selectByParam(bean);
	}

	@Override
	public List<InnerTransferBean> selectByPage(Page<InnerTransferBean> page) {

		return dao.selectByPage(page);
	}

}