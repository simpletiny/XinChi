package com.xinchi.backend.sale.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.order.service.OrderService;
import com.xinchi.bean.SaleScoreDto;
import com.xinchi.common.BaseAction;
import com.xinchi.common.ResourcesConstants;
import com.xinchi.common.UserSessionBean;
import com.xinchi.common.XinChiApplicationContext;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SaleDashBoardAction extends BaseAction {
	private static final long serialVersionUID = -5067051588429574578L;
	@Autowired
	private OrderService orderService;

	private List<SaleScoreDto> scores;

	private SaleScoreDto score;

	public String searchSaleScoreByPage() {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bo", score);
		page.setParams(params);

		scores = orderService.searchSaleScoreByPage(page);
		return SUCCESS;
	}

	public String searchBackMoneyScoreByPage() {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bo", score);
		page.setParams(params);

		scores = orderService.searchBackMoneyScoreByPage(page);
		return SUCCESS;
	}

	public String search3MonthSaleScore() {
		UserSessionBean sessionBean = (UserSessionBean) XinChiApplicationContext
				.getSession(ResourcesConstants.LOGIN_SESSION_KEY);
		String user_number = sessionBean.getUser_number();
		scores = orderService.search3MonthScoreByUserNumber(user_number);
		return SUCCESS;
	}

	public List<SaleScoreDto> getScores() {
		return scores;
	}

	public void setScores(List<SaleScoreDto> scores) {
		this.scores = scores;
	}

	public SaleScoreDto getScore() {
		return score;
	}

	public void setScore(SaleScoreDto score) {
		this.score = score;
	}

}
