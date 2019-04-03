package com.xinchi.backend.culture.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.culture.dao.ProductResearchLabelDAO;
import com.xinchi.backend.culture.dao.ProductResearchViewDAO;
import com.xinchi.backend.culture.service.ProductResearchViewService;
import com.xinchi.bean.ProductResearchLabelBean;
import com.xinchi.bean.ProductResearchViewBean;
import com.xinchi.tools.Page;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
@Transactional
public class ProductResearchViewServiceImpl implements ProductResearchViewService {

	@Autowired
	private ProductResearchViewDAO dao;

	@Autowired
	private ProductResearchLabelDAO labelDao;

	@Override
	public void insert(ProductResearchViewBean view) {
		dao.insert(view);
	}

	@Override
	public List<ProductResearchViewBean> getAllViewsByPage(Page<ProductResearchViewBean> page) {
		return dao.getAllViewsByPage(page);
	}

	@Override
	public ProductResearchViewBean selectViewByPk(String view_pk) {

		return dao.selectByPk(view_pk);
	}

	@Override
	public void update(ProductResearchViewBean view) {
		dao.update(view);
	}

	@Override
	public void delete(String view_pk) {
		dao.delete(view_pk);
	}

	@Override
	public String createLabel(ProductResearchLabelBean label) {
		labelDao.insert(label);
		return SUCCESS;
	}

	@Override
	public List<ProductResearchLabelBean> selectLabelsByParam(ProductResearchLabelBean label) {
		return labelDao.selectByParam(label);
	}

	@Override
	public String deleteLabelByName(String label_name) {
		labelDao.deleteByName(label_name);
		return SUCCESS;
	}

	@Override
	public String sortLabels(String sort_json) {
		JSONArray array = JSONArray.fromObject(sort_json);

		if (null != array && array.size() > 0) {
			for (int i = 0; i < array.size(); i++) {
				JSONObject obj = array.getJSONObject(i);
				ProductResearchLabelBean prl = new ProductResearchLabelBean();

				String pk = obj.getString("pk");
				int label_index = obj.getInt("label_index");

				prl.setPk(pk);
				prl.setLabel_index(label_index);

				labelDao.update(prl);
			}
		}
		return SUCCESS;
	}

}
