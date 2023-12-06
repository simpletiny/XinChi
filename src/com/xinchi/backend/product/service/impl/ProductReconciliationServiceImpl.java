package com.xinchi.backend.product.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.product.dao.ProductAccountingFirmDAO;
import com.xinchi.backend.product.dao.ProductReconciliationDAO;
import com.xinchi.backend.product.service.ProductReconciliationService;
import com.xinchi.bean.AirServiceFeeDto;
import com.xinchi.bean.ProductAccountingFirmBean;
import com.xinchi.bean.ProductReconciliationBean;
import com.xinchi.common.DateUtil;
import com.xinchi.common.SimpletinyUser;
import com.xinchi.tools.Page;

@Service
@Transactional
public class ProductReconciliationServiceImpl implements ProductReconciliationService {

	@Autowired
	private ProductReconciliationDAO dao;

	@Override
	public void insert(ProductReconciliationBean bean) {
		dao.insert(bean);
	}

	@Override
	public void update(ProductReconciliationBean bean) {
		dao.update(bean);
	}

	@Override
	public String delete(String id) {
		dao.delete(id);
		return SUCCESS;
	}

	@Override
	public ProductReconciliationBean selectByPrimaryKey(String id) {
		return dao.selectByPrimaryKey(id);
	}

	@Override
	public List<ProductReconciliationBean> selectByParam(ProductReconciliationBean bean) {
		return dao.selectByParam(bean);
	}

	@Override
	public String addReconciliation(ProductReconciliationBean reconciliation) {
		reconciliation.setProduct_manager_number(SimpletinyUser.user().getUser_number());
		dao.insert(reconciliation);
		return SUCCESS;
	}

	@Autowired
	private ProductAccountingFirmDAO productAccountingFirmDao;

	@Override
	public String confirmProductAccounting(String product_manager_number, String belong_month) {
		String[] user_numbers = product_manager_number.split(",");
		String confirm_user = SimpletinyUser.user().getUser_number();
		String confirm_time = DateUtil.getTimeStr();

		for (String user_number : user_numbers) {
			ProductAccountingFirmBean paf = new ProductAccountingFirmBean();
			paf.setBelong_month(belong_month);
			paf.setProduct_manager_number(user_number);
			paf.setConfirm_time(confirm_time);
			paf.setConfirm_user(confirm_user);
			productAccountingFirmDao.insert(paf);
		}

		return SUCCESS;
	}

	@Override
	public List<ProductReconciliationBean> selectByPage(Page<ProductReconciliationBean> page) {

		return dao.selectByPage(page);
	}

	@Override
	public List<ProductReconciliationBean> selectSumReconciliation(AirServiceFeeDto bean) {
		return dao.selectSumReconciliation(bean);
	}
}