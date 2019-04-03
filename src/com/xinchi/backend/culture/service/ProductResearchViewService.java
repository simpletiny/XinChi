package com.xinchi.backend.culture.service;

import java.util.List;

import com.xinchi.bean.ProductResearchLabelBean;
import com.xinchi.bean.ProductResearchViewBean;
import com.xinchi.common.BaseService;
import com.xinchi.tools.Page;

public interface ProductResearchViewService extends BaseService {
	public void insert(ProductResearchViewBean view);

	public List<ProductResearchViewBean> getAllViewsByPage(Page<ProductResearchViewBean> page);

	public ProductResearchViewBean selectViewByPk(String view_pk);

	public void update(ProductResearchViewBean view);

	public void delete(String view_pk);

	public String createLabel(ProductResearchLabelBean label);

	public List<ProductResearchLabelBean> selectLabelsByParam(ProductResearchLabelBean label);

	public String deleteLabelByName(String label_name);

	public String sortLabels(String sort_json);

}
