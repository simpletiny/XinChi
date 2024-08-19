package com.xinchi.backend.sale.action;

import java.util.ArrayList;
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

	public String searchSaleScoreByParam() {

		List<SaleScoreDto> scores_a = orderService.searchSaleScoreByPage(score);
		List<SaleScoreDto> scores_b = orderService.searchNonStandardSaleData(score);
		List<SaleScoreDto> scores_c = orderService.searchSaleCost(score);
		scores = mergeScores(scores_a, scores_b, scores_c);
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

	private List<SaleScoreDto> mergeScores(List<SaleScoreDto> scores_a, List<SaleScoreDto> scores_b,
			List<SaleScoreDto> scores_c) {
		Map<String, SaleScoreDto> scoreMap = new HashMap<>();

		for (SaleScoreDto dto : scores_a) {
			String key = dto.getSale_number() + "-" + dto.getConfirm_month();
			scoreMap.put(key, dto);
		}

		for (SaleScoreDto dto : scores_b) {
			String key = dto.getSale_number() + "-" + dto.getConfirm_month();
			if (scoreMap.containsKey(key)) {
				SaleScoreDto dto_a = scoreMap.get(key);
				dto_a.setNon_standard_people(dto.getNon_standard_people());
				dto_a.setOnly_ticket_people(dto.getOnly_ticket_people());
			} else {
				scoreMap.put(key, dto);
			}
		}

		for (SaleScoreDto dto : scores_c) {
			String key = dto.getSale_number() + "-" + dto.getConfirm_month();
			if (scoreMap.containsKey(key)) {
				SaleScoreDto dto_a = scoreMap.get(key);
				dto_a.setSale_cost(dto.getSale_cost());
				dto_a.setSale_salary(dto.getSale_salary());
			} else {
				scoreMap.put(key, dto);
			}
		}
		return new ArrayList<>(scoreMap.values());
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
