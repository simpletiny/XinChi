package com.xinchi.backend.product.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.product.dao.ProductGroupDAO;
import com.xinchi.backend.product.dao.ProductGroupSupplierDAO;
import com.xinchi.backend.product.service.ProductGroupService;
import com.xinchi.backend.supplier.dao.SupplierDAO;
import com.xinchi.bean.ProductGroupBean;
import com.xinchi.bean.ProductGroupSupplierBean;
import com.xinchi.bean.SupplierBean;
import com.xinchi.common.SimpletinyString;
import com.xinchi.tools.Page;

@Service
@Transactional
public class ProductGroupServiceImpl implements ProductGroupService {

	@Autowired
	private ProductGroupDAO dao;

	@Autowired
	private ProductGroupSupplierDAO pgsdao;

	@Override
	public String insert(ProductGroupBean bean) {
		ProductGroupBean options = new ProductGroupBean();
		options.setGroup_name(bean.getGroup_name());
		List<ProductGroupBean> exists = dao.selectByParam(bean);
		if (null != exists && exists.size() > 0) {
			return "exists";
		}

		String group_pk = dao.insert(bean);
		for (String supplier_pk : bean.getSupplier_pks()) {
			if (SimpletinyString.isEmpty(supplier_pk))
				continue;
			ProductGroupSupplierBean pgsb = new ProductGroupSupplierBean();
			pgsb.setGroup_pk(group_pk);
			pgsb.setSupplier_pk(supplier_pk);
			pgsdao.insert(pgsb);
		}
		return SUCCESS;
	}

	@Override
	public String update(ProductGroupBean bean) {
		// 判断是否重名
		ProductGroupBean options = new ProductGroupBean();
		options.setGroup_name(bean.getGroup_name());
		List<ProductGroupBean> exists = dao.selectByParam(bean);
		if (null != exists && exists.size() > 0) {
			if (!exists.get(0).getPk().equals(bean.getPk()))
				return "exists";
		}

		// 更新用户组
		dao.update(bean);
		// 删除之前的供应商映射
		pgsdao.deleteByGroupPk(bean.getPk());
		// 插入重新映射的供应商
		for (String supplier_pk : bean.getSupplier_pks()) {
			if (SimpletinyString.isEmpty(supplier_pk))
				continue;
			ProductGroupSupplierBean pgsb = new ProductGroupSupplierBean();
			pgsb.setGroup_pk(bean.getPk());
			pgsb.setSupplier_pk(supplier_pk);
			pgsdao.insert(pgsb);
		}
		return SUCCESS;
	}

	@Override
	public String delete(String id) {
		dao.delete(id);
		pgsdao.deleteByGroupPk(id);
		return SUCCESS;

	}

	@Override
	public ProductGroupBean selectByPrimaryKey(String id) {
		return dao.selectByPrimaryKey(id);
	}

	@Override
	public List<ProductGroupBean> getAllByParam(ProductGroupBean bean) {
		return dao.selectByParam(bean);
	}

	@Override
	public List<ProductGroupBean> selectByPage(Page page) {

		return dao.selectByPage(page);
	}

	@Autowired
	private SupplierDAO supplierDao;

	@Override
	public List<SupplierBean> selectByGroupPk(String group_pk) {
		ProductGroupSupplierBean options = new ProductGroupSupplierBean();
		options.setGroup_pk(group_pk);
		List<ProductGroupSupplierBean> pgsb = pgsdao.selectByParam(options);
		List<String> supplier_pks = new ArrayList<String>();
		for (ProductGroupSupplierBean bean : pgsb) {
			supplier_pks.add(bean.getSupplier_pk());
		}

		return supplierDao.selectByPks(supplier_pks);
	}

	@Override
	public String delete(ProductGroupBean group) {
		dao.delete(group.getPk());
		pgsdao.deleteByGroupPk(group.getPk());
		return SUCCESS;
	}

}