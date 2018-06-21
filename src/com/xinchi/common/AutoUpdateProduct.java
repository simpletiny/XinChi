package com.xinchi.common;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.product.dao.ProductDAO;
import com.xinchi.backend.product.dao.ProductDelayDAO;
import com.xinchi.bean.ProductBean;
import com.xinchi.bean.ProductDelayBean;

@Service
@Transactional
public class AutoUpdateProduct {
	@Autowired
	private ProductDelayDAO delayDao;

	@Autowired
	private ProductDAO productDao;

	public void updateProduct(String[] param) {
		List<ProductDelayBean> delays = delayDao.selectByParam(null);
		for (ProductDelayBean delay : delays) {
			ProductBean product = productDao.selectByPrimaryKey(delay.getProduct_pk());

			product.setAdult_price(
					(null == delay.getAdult_price()) ? product.getAdult_price() : delay.getAdult_price());

			product.setChild_price(
					(null == delay.getChild_price()) ? (product.getChild_price()==null?BigDecimal.ZERO:product.getChild_price()) : delay.getChild_price());

			product.setBusiness_profit_substract(
					(null == delay.getBusiness_profit_substract()) ? product.getBusiness_profit_substract()
							: delay.getBusiness_profit_substract());
			product.setMax_profit_substract(
					(null == delay.getMax_profit_substract()) ? product.getMax_profit_substract()
							: delay.getMax_profit_substract());
			
			product.setLocal_child_cost(
					(null == product.getLocal_child_cost()) ? BigDecimal.ZERO : product.getLocal_child_cost());
			product.setAir_ticket_child_cost((null == product.getAir_ticket_child_cost()) ? BigDecimal.ZERO
					: product.getAir_ticket_child_cost());
			product.setOther_child_cost(
					(null == product.getOther_child_cost()) ? BigDecimal.ZERO : product.getOther_child_cost());
			
			
			product.setProduct_value(delay.getProduct_value());
			
			product.setProduct_child_value(delay.getProduct_child_value());

			BigDecimal product_price = product.getAdult_price().subtract(product.getBusiness_profit_substract())
					.subtract(product.getMax_profit_substract());

			BigDecimal product_child_price = product.getChild_price().subtract(product.getBusiness_profit_substract())
					.subtract(product.getMax_profit_substract());

			// 毛利
			BigDecimal gross_profit = product_price.subtract(product.getAir_ticket_cost())
					.subtract(product.getLocal_adult_cost())
					.subtract((null == product.getOther_cost()) ? BigDecimal.ZERO : product.getOther_cost());

			BigDecimal gross_child_profit = product_child_price.subtract(product.getAir_ticket_child_cost())
					.subtract(product.getLocal_child_cost()).subtract(
							(null == product.getOther_child_cost()) ? BigDecimal.ZERO : product.getOther_child_cost());

			// 毛利率
			BigDecimal denominator = product.getAdult_price().subtract(product.getBusiness_profit_substract())
					.divide(new BigDecimal(100));

			BigDecimal denominator_child = product.getChild_price().subtract(product.getBusiness_profit_substract())
					.divide(new BigDecimal(100));

			BigDecimal gross_profit_rate = BigDecimal.ZERO;
			BigDecimal gross_child_profit_rate = BigDecimal.ZERO;

			if (denominator.compareTo(BigDecimal.ZERO) != 0)
				gross_profit_rate = gross_profit.divide(denominator, 0, BigDecimal.ROUND_HALF_UP);
			if (denominator_child.compareTo(BigDecimal.ZERO) != 0)
				gross_child_profit_rate = gross_child_profit.divide(denominator_child, 0, BigDecimal.ROUND_HALF_UP);

			float rate = gross_profit_rate.floatValue();
			float rate_child = gross_child_profit_rate.floatValue();

			// 现付资金
			BigDecimal spot_cash = BigDecimal.ZERO;
			BigDecimal spot_child_cash = BigDecimal.ZERO;

			if (product.getCash_flow_air_flg().equals("Y")) {
				spot_cash = spot_cash.add(product.getAir_ticket_cost());
				spot_child_cash = spot_child_cash.add(product.getAir_ticket_child_cost());
			}

			if (product.getCash_flow_local_flg().equals("Y")) {
				spot_cash = spot_cash.add(product.getLocal_adult_cost());
				spot_child_cash = spot_child_cash.add(product.getLocal_child_cost());
			}

			if (product.getCash_flow_other_flg().equals("Y")) {
				spot_cash = spot_cash
						.add((null == product.getOther_cost()) ? BigDecimal.ZERO : product.getOther_cost());

				spot_child_cash = spot_child_cash
						.add((null == product.getOther_child_cost()) ? BigDecimal.ZERO : product.getOther_child_cost());
			}

			// 现金流
			BigDecimal cash_flow = product_price.subtract(spot_cash);
			BigDecimal cash_child_flow = product_child_price.subtract(spot_child_cash);

			product.setGross_profit(gross_profit);
			product.setGross_profit_rate(rate);
			product.setCash_flow(cash_flow);
			product.setSpot_cash(spot_cash);

			product.setGross_child_profit(gross_child_profit_rate);
			product.setGross_child_profit_rate(rate_child);
			product.setCash_child_flow(cash_child_flow);
			product.setSpot_child_cash(spot_child_cash);
			// 重新计算各种利率

			productDao.sysUpdate(product);
			delayDao.delete(delay.getPk());
		}
	}
}
