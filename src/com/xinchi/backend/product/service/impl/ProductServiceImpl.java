package com.xinchi.backend.product.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.product.dao.ProductDAO;
import com.xinchi.backend.product.service.ProductService;
import com.xinchi.backend.util.service.NumberService;
import com.xinchi.bean.ProductBean;
import com.xinchi.tools.Page;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDAO dao;

	@Autowired
	private NumberService numberService;

	@Override
	public String insert(ProductBean bean) {
		ProductBean option = new ProductBean();
		option.setName(bean.getName());
		List<ProductBean> products = dao.selectByParam(option);
		if (null != products && products.size() > 0) {
			return "exists";
		}
		String product_number = numberService.generateProductNumber();
		bean.setProduct_number(product_number);
		dao.insert(bean);
		return "success";
	}

	@Override
	public String update(ProductBean bean) {
		ProductBean option = new ProductBean();
		option.setName(bean.getName());
		List<ProductBean> products = dao.selectByParam(option);
		if (null != products && products.size() > 0) {
			for (ProductBean product : products) {
				if (!product.getPk().equals(bean.getPk())) {
					return "exists";
				}
			}
		}
		dao.update(bean);
		return "success";
	}

	@Override
	public void delete(String id) {
		dao.delete(id);
	}

	@Override
	public ProductBean selectByPrimaryKey(String id) {
		return dao.selectByPrimaryKey(id);
	}

	@Override
	public List<ProductBean> getAllByParam(ProductBean bean) {
		return dao.selectByParam(bean);
	}

	@Override
	public List<ProductBean> selectByPage(Page<ProductBean> page) {

		return dao.selectByPage(page);
	}

	@Override
	public String onSale(String product_pks, String sale_flg) {
		String[] pks = product_pks.split(",");
		List<ProductBean> products = dao.selectByPks(pks);
		for (ProductBean product : products) {
			product.setSale_flg(sale_flg);
			dao.update(product);
		}
		return "success";
	}

}