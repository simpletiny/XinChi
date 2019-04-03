package com.xinchi.backend.culture.dao;

import java.util.List;

import com.xinchi.bean.ProductResearchLabelBean;

public interface ProductResearchLabelDAO {

	public void insert(ProductResearchLabelBean bean);

	public List<ProductResearchLabelBean> selectByParam(ProductResearchLabelBean bean);

	public ProductResearchLabelBean selectByPk(String pk);

	public void update(ProductResearchLabelBean bean);

	public void delete(String pk);

	public void deleteByName(String label_name);
}
