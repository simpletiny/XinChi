package com.xinchi.backend.ticket.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.ticket.service.AirLegService;
import com.xinchi.bean.AirLegBean;
import com.xinchi.common.BaseAction;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AirLegAction extends BaseAction {
	private static final long serialVersionUID = -6221029472225939064L;

	@Autowired
	private AirLegService service;

	private AirLegBean leg;

	private List<AirLegBean> legs;

	public String searchAirLegsByPage() {

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("bo", leg);
		page.setParams(params);

		legs = service.selectByPage(page);
		return SUCCESS;
	}

	public String createAirLeg() {
		resultStr = service.createLeg(leg);
		return SUCCESS;
	}

	private String leg_pk;
	private List<String> leg_pks;

	public String deleteAirLeg() {
		for (String pk : leg_pks) {
			service.delete(pk);
		}
		resultStr = SUCCESS;
		return SUCCESS;
	}

	public String updateAirLeg() {
		resultStr = service.updateLeg(leg);
		return SUCCESS;
	}

	public String switchHot() {
		service.update(leg);
		resultStr = SUCCESS;
		return SUCCESS;
	}

	public String searchAirLegByPk() {

		leg = service.selectByPrimaryKey(leg_pk);
		return SUCCESS;
	}

	public AirLegBean getLeg() {
		return leg;
	}

	public void setLeg(AirLegBean leg) {
		this.leg = leg;
	}

	public String getLeg_pk() {
		return leg_pk;
	}

	public void setLeg_pk(String leg_pk) {
		this.leg_pk = leg_pk;
	}

	public List<AirLegBean> getLegs() {
		return legs;
	}

	public void setLegs(List<AirLegBean> legs) {
		this.legs = legs;
	}

	public List<String> getLeg_pks() {
		return leg_pks;
	}

	public void setLeg_pks(List<String> leg_pks) {
		this.leg_pks = leg_pks;
	}
}