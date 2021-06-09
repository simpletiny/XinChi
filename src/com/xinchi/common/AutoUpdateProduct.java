package com.xinchi.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.product.dao.ProductDAO;
import com.xinchi.backend.product.dao.ProductDelayDAO;
import com.xinchi.backend.util.dao.CommonDAO;
import com.xinchi.backend.util.service.NumberService;
import com.xinchi.bean.ProductBean;
import com.xinchi.bean.ProductDelayBean;
import com.xinchi.bean.SqlBean;

@Service
@Transactional
public class AutoUpdateProduct {
	@Autowired
	private ProductDelayDAO delayDao;

	@Autowired
	private ProductDAO productDao;

	@Autowired
	private CommonDAO commonDao;

	@Autowired
	private NumberService numberService;

	public void updateProduct(String[] param) {

		// 强制上架或下架产品
		String sql1 = "UPDATE product SET sale_flg = 'N' WHERE keep_flg = 'N' AND sale_flg = 'Y';";
		SqlBean ss = new SqlBean();
		ss.setSql(sql1);
		commonDao.exeBySql(ss);

		String sql2 = "UPDATE product SET sale_flg = 'Y',keep_flg='N' WHERE keep_flg = 'Y'  AND product_number is not null;";
		ss.setSql(sql2);
		commonDao.exeBySql(ss);

		String sql3 = "UPDATE product SET analysis_flg = 'N' WHERE analysis_flg = 'Y'";
		ss.setSql(sql3);
		commonDao.exeBySql(ss);

		ProductBean option = new ProductBean();
		option.setKeep_flg("Y");
		List<ProductBean> products = productDao.selectByParam(option);
		for (ProductBean product : products) {
			if (SimpletinyString.isEmpty(product.getProduct_number())) {
				String product_number = numberService.generateProductNumber();
				product.setProduct_number(product_number);
			}
			product.setKeep_flg("N");
			product.setSale_flg("Y");

			productDao.sysUpdate(product);
		}

		List<ProductDelayBean> delays = delayDao.selectByParam(null);
		for (ProductDelayBean delay : delays) {
			ProductBean product = productDao.selectByPrimaryKey(delay.getProduct_pk());

			product.setProduct_value(delay.getProduct_value());
			product.setProduct_child_value(delay.getProduct_child_value());

			productDao.sysUpdate(product);
			delayDao.delete(delay.getPk());
		}
	}
}
