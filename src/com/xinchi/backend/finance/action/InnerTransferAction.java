package com.xinchi.backend.finance.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.finance.service.InnerTransferService;
import com.xinchi.bean.InnerTransferBean;
import com.xinchi.common.BaseAction;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InnerTransferAction extends BaseAction {
	private static final long serialVersionUID = -6550630411784336019L;

	@Autowired
	private InnerTransferService service;

	private List<InnerTransferBean> inners;

	private InnerTransferBean options;

	public String searchInnerTransferByPage() {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bo", options);
		page.setParams(params);
		inners = service.selectByPage(page);
		return SUCCESS;
	}

	public List<InnerTransferBean> getInners() {
		return inners;
	}

	public void setInners(List<InnerTransferBean> inners) {
		this.inners = inners;
	}

	public InnerTransferBean getOptions() {
		return options;
	}

	public void setOptions(InnerTransferBean options) {
		this.options = options;
	}
}