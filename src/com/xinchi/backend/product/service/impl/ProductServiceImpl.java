package com.xinchi.backend.product.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.order.dao.BudgetStandardOrderDAO;
import com.xinchi.backend.product.dao.ProductDAO;
import com.xinchi.backend.product.dao.ProductDelayDAO;
import com.xinchi.backend.product.service.ProductService;
import com.xinchi.backend.util.service.NumberService;
import com.xinchi.bean.BudgetStandardOrderBean;
import com.xinchi.bean.ProductBean;
import com.xinchi.bean.ProductDelayBean;
import com.xinchi.common.DateUtil;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.SimpletinyString;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;
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
		dao.insert(bean);
		return "success";
	}

	@Autowired
	private ProductDelayDAO delayDao;

	@Override
	public String update(ProductBean bean, ProductDelayBean delay) {
		ProductBean oldProduct = dao.selectByPrimaryKey(bean.getPk());
		ProductDelayBean exist_delay = delayDao.selectByProductPk(bean.getPk());

		if (oldProduct.getSale_flg().equals("Y")) {
			if (null == exist_delay) {
				delay.setProduct_pk(bean.getPk());
				delayDao.insert(delay);
			} else {
				if (exist_delay.getUpdate_cnt() >= 3) {
					return "more_update";
				} else {
					delay.setPk(exist_delay.getPk());
					delay.setUpdate_cnt(exist_delay.getUpdate_cnt() + 1);
					delayDao.update(delay);
				}
			}
		} else {
			// 检测是否之前架上有过修改,如果有删除
			if (null != exist_delay)
				delayDao.delete(exist_delay.getPk());

			bean.setAdult_price(delay.getAdult_price());
			bean.setChild_price(delay.getChild_price());
			bean.setBusiness_profit_substract(delay.getBusiness_profit_substract());
			bean.setMax_profit_substract(delay.getMax_profit_substract());
			bean.setProduct_value(delay.getProduct_value());
			bean.setProduct_child_value(delay.getProduct_child_value());
			bean.setGross_profit(delay.getGross_profit());
			bean.setGross_profit_rate(delay.getGross_profit_rate());
			bean.setCash_flow(delay.getCash_flow());
			bean.setSpot_cash(delay.getSpot_cash());
		}
		dao.update(bean);
		return "success";
	}

	@Override
	public String updateProductValue(ProductBean bean, ProductDelayBean delay) {
		ProductBean product = dao.selectByPrimaryKey(bean.getPk());
		ProductDelayBean exist_delay = delayDao.selectByProductPk(bean.getPk());

		if (product.getSale_flg().equals("Y")) {
			if (null == exist_delay) {
				delay.setProduct_pk(bean.getPk());
				delayDao.insert(delay);
			} else {
				if (exist_delay.getUpdate_cnt() >= 3) {
					return "more_update";
				} else {
					delay.setPk(exist_delay.getPk());
					delay.setUpdate_cnt(exist_delay.getUpdate_cnt() + 1);
					delayDao.update(delay);
				}
			}
		} else {
			// 检测是否之前架上有过修改,如果有删除
			if (null != exist_delay) {
				exist_delay.setProduct_value(delay.getProduct_value());
				exist_delay.setProduct_child_value(delay.getProduct_child_value());
				delayDao.update(exist_delay);
			}
			bean.setProduct_value(delay.getProduct_value());
			bean.setProduct_child_value(delay.getProduct_child_value());
			dao.update(bean);
		}

		return SUCCESS;
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

	@Autowired
	private BudgetStandardOrderDAO bsoDao;

	@Override
	public String onSale(String product_pks, String sale_flg, String force_flg) {
		String[] pks = product_pks.split(",");
		List<ProductBean> products = dao.selectByPks(pks);

		// 如果是上架产品
		if (sale_flg.equals("Y")) {
			for (ProductBean product : products) {
				if (product.getAir_ticket_charge().equals("NO")) {
					return "请绑定产品：" + product.getProduct_number() + "机票信息";
				}
				// 如果没有产品编号（即新建产品，创建你妈的产品编号）
				if (SimpletinyString.isEmpty(product.getProduct_number())) {
					String product_number = numberService.generateProductNumber();
					product.setProduct_number(product_number);
				}

				// 判断架上是否有存在(名称+产品型号)相同的产品
				ProductBean option = new ProductBean();
				option.setName(product.getName());
				option.setProduct_model(product.getProduct_model());
				List<ProductBean> pros = dao.selectByParam(option);

				if (null != pros && pros.size() > 0) {
					for (ProductBean pro : pros) {
						if (!pro.getPk().equals(product.getPk()))
							return "存在产品名称+产品型号相同的架上产品：" + product.getName() + "--" + product.getProduct_model();
					}

				}
				// 判断是否为当日下架产品
				if (!SimpletinyString.isEmpty(product.getOff_shelves_date())) {
					if (DateUtil.compare(product.getOff_shelves_date()) == 2) {
						product.setSale_flg(sale_flg);
						dao.update(product);
					} else {
						return "当日下架的产品：" + product.getProduct_number() + "，不能重新上架";
					}
				} else {
					product.setSale_flg(sale_flg);
					dao.update(product);
				}

			}
		}
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String user_roles = sessionBean.getUser_roles();

		// 如果是下架产品
		if (sale_flg.equals("N")) {
			for (ProductBean product : products) {
				// 判断是否存在待确认订单
				BudgetStandardOrderBean option = new BudgetStandardOrderBean();
				option.setProduct_pk(product.getPk());
				option.setConfirm_flg("N");
				List<BudgetStandardOrderBean> orders = bsoDao.selectByParam(option);
				if (null != orders && orders.size() > 0) {
					// 判断是否有经理权限
					if (!user_roles.contains(ResourcesConstants.USER_ROLE_ADMIN)
							&& !user_roles.contains(ResourcesConstants.USER_ROLE_MANAGER)) {
						return product.getProduct_number() + product.getName() + "有待确认订单，请沟通后下架！";
					} else {
						if (!SimpletinyString.isEmpty(force_flg) && force_flg.equals("Y")) {
							// 设定下架日期
							product.setOff_shelves_date(DateUtil.today());
							product.setSale_flg(sale_flg);
							dao.update(product);
							// 删除待确认订单
							for (BudgetStandardOrderBean order : orders) {
								bsoDao.delete(order.getPk());
							}
						} else {
							return "second&&" + product.getProduct_number() + product.getName() + "有待确认订单！";
						}
					}
				} else {
					// 设定下架日期
					product.setOff_shelves_date(DateUtil.today());
					product.setSale_flg(sale_flg);
					dao.update(product);
				}
			}
		}
		// 如果是废弃产品
		if (sale_flg.equals("D")) {
			for (ProductBean product : products) {
				// 有产品编号的废弃，没有产品编号的删除
				if (SimpletinyString.isEmpty(product.getProduct_number())) {
					dao.delete(product.getPk());
				} else {
					product.setSale_flg(sale_flg);
					dao.update(product);
				}

			}
		}
		return "success";
	}

	@Override
	public void updateStraight(ProductBean product) {
		dao.update(product);
	}

	@Override
	public String updateProductDirectly(ProductBean product) {
		dao.update(product);
		return SUCCESS;
	}
}