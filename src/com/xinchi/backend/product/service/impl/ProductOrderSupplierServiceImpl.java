package com.xinchi.backend.product.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.product.dao.ProductOrderDAO;
import com.xinchi.backend.product.dao.ProductOrderSupplierDAO;
import com.xinchi.backend.product.dao.ProductOrderSupplierSaleOrderDAO;
import com.xinchi.backend.product.service.ProductOrderSupplierService;
import com.xinchi.bean.OrderSupplierBean;
import com.xinchi.bean.OrderSupplierInfoBean;
import com.xinchi.bean.OrderSupplierSaleOrderBean;
import com.xinchi.bean.ProductOrderBean;
import com.xinchi.common.DateUtil;

@Service
@Transactional
public class ProductOrderSupplierServiceImpl implements ProductOrderSupplierService {

	@Autowired
	private ProductOrderSupplierDAO dao;

	@Override
	public void insert(OrderSupplierBean bean) {
		dao.insert(bean);
	}

	@Override
	public void update(OrderSupplierBean bean) {
		dao.update(bean);
	}

	@Override
	public void delete(String id) {
		dao.delete(id);
	}

	@Override
	public OrderSupplierBean selectByPrimaryKey(String id) {
		return dao.selectByPrimaryKey(id);
	}

	@Override
	public List<OrderSupplierBean> selectByParam(OrderSupplierBean bean) {
		return dao.selectByParam(bean);
	}

	@Override
	public void deleteByProductPk(String product_pk) {
		dao.deleteByProductPk(product_pk);
	}

	@Override
	public List<OrderSupplierBean> selectByProductPk(String product_pk) {
		return dao.selectByProductPk(product_pk);
	}

	@Autowired
	private ProductOrderDAO productOrderDao;

	@Autowired
	private ProductOrderSupplierSaleOrderDAO productOrderSupplierSaleOrderDao;

	@Override
	public List<OrderSupplierBean> selectByProductOrderNumber(String product_order_number) {
		ProductOrderBean productOrder = productOrderDao.selectByOrderNumber(product_order_number);
		OrderSupplierBean option = new OrderSupplierBean();
		option.setOrder_pk(productOrder.getPk());

		List<OrderSupplierBean> orderSuppliers = dao.selectByParam(option);
		for (OrderSupplierBean supplier : orderSuppliers) {
			List<OrderSupplierSaleOrderBean> saleOrderInfos = productOrderSupplierSaleOrderDao
					.selectByBasePk(supplier.getPk());
			supplier.setSale_order_infos(saleOrderInfos);
		}
		return orderSuppliers;
	}

	@Override
	public OrderSupplierBean searchOneOrderSupplier(OrderSupplierBean option) {
		List<OrderSupplierBean> orderSuppliers = dao.selectByParam(option);

		for (OrderSupplierBean supplier : orderSuppliers) {
			List<OrderSupplierSaleOrderBean> saleOrderInfos = productOrderSupplierSaleOrderDao
					.selectByBasePk(supplier.getPk());
			supplier.setSale_order_infos(saleOrderInfos);
		}

		return (orderSuppliers != null && orderSuppliers.size() > 0) ? orderSuppliers.get(0) : null;
	}

	@Override
	public OrderSupplierBean selectByOrderPkAndEmployeePk(String order_pk, String supplier_employee_pk) {
		OrderSupplierBean option = new OrderSupplierBean();
		option.setOrder_pk(order_pk);
		option.setSupplier_employee_pk(supplier_employee_pk);

		List<OrderSupplierBean> orderSuppliers = dao.selectByParam(option);
		if (null != orderSuppliers && orderSuppliers.size() == 1) {
			OrderSupplierBean supplier = orderSuppliers.get(0);

			List<OrderSupplierInfoBean> osInfos = supplier.getInfos();
			String pick_date = supplier.getPick_date();

			for (OrderSupplierInfoBean info : osInfos) {
				info.setOperate_date(DateUtil.addDate(pick_date, info.getPick_day() - 1));
				info.setPick_date(DateUtil.addDate(pick_date, info.getPick_day() - 1));
				info.setSend_date(DateUtil.addDate(pick_date, info.getSend_day() - 1));
			}

			List<OrderSupplierSaleOrderBean> saleOrderInfos = productOrderSupplierSaleOrderDao
					.selectByBasePk(supplier.getPk());
			supplier.setSale_order_infos(saleOrderInfos);
			return supplier;
		}
		return null;
	}

}