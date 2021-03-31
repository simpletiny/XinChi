package com.xinchi.backend.sys.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.product.dao.ProductDAO;
import com.xinchi.backend.sys.dao.BaseDataDAO;
import com.xinchi.backend.sys.service.BaseDataService;
import com.xinchi.backend.util.dao.CommonDAO;
import com.xinchi.bean.BaseDataBean;
import com.xinchi.bean.ProductBean;
import com.xinchi.bean.SqlBean;
import com.xinchi.common.ResourcesConstants;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
@Transactional
public class BaseDataServiceImpl implements BaseDataService {

	@Autowired
	private BaseDataDAO dao;

	@Override
	public void insert(BaseDataBean base) {
		dao.insert(base);
	}

	@Override
	public BaseDataBean selectByPk(String pk) {

		return dao.selectByPk(pk);
	}

	@Override
	public void update(BaseDataBean base) {
		dao.update(base);
	}

	@Override
	public void delete(String pk) {
		dao.delete(pk);
	}

	@Override
	public List<BaseDataBean> selectByParam(BaseDataBean base) {
		return dao.selectByParam(base);
	}

	@Override
	public List<BaseDataBean> selectByFatherLevelPk(String father_level_pk) {

		return dao.selectByFatherLevelPk(father_level_pk);
	}

	@Override
	public String sortData(String json) {
		try {
			JSONArray array = JSONArray.fromObject(json);
			for (int i = 0; i < array.size(); i++) {
				JSONObject obj = JSONObject.fromObject(array.get(i));

				String pk = obj.getString("pk");
				int order_index = obj.getInt("order_index");

				BaseDataBean bd = new BaseDataBean();
				bd.setPk(pk);
				bd.setOrder_index(order_index);

				dao.update(bd);
			}

			return SUCCESS;
		} catch (Exception e) {
			return FAIL;
		}
	}

	@Override
	public String createBaseData(BaseDataBean baseData) {
		if (baseData.getType().equals(ResourcesConstants.BASE_DATA_TYPE_LINE)) {
			BaseDataBean option = new BaseDataBean();
			option.setName(baseData.getName());
			option.setType(baseData.getType());
			option.setLevel(baseData.getLevel());
			List<BaseDataBean> exists = dao.selectByParam(option);

			if (null != exists && exists.size() > 0) {
				return EXISTS;
			}

			dao.insert(baseData);
		}

		return SUCCESS;
	}

	@Autowired
	private CommonDAO commonDao;

	@Override
	public String updateBaseData(BaseDataBean baseData) {
		if (baseData.getType().equals(ResourcesConstants.BASE_DATA_TYPE_LINE)) {
			BaseDataBean option = new BaseDataBean();
			option.setName(baseData.getName());
			option.setType(baseData.getType());
			option.setLevel(baseData.getLevel());
			List<BaseDataBean> exists = dao.selectByParam(option);
			BaseDataBean old = dao.selectByPk(baseData.getPk());

			if (null != exists && exists.size() > 0 && !exists.get(0).getPk().equals(baseData.getPk())) {
				return EXISTS;
			}

			// 更新涉及到此产品线的数据
			String str1 = "UPDATE product set location='" + baseData.getName() + "' WHERE location='" + old.getName()
					+ "';";
			SqlBean sql = new SqlBean();
			sql.setSql(str1);
			commonDao.exeBySql(sql);
			dao.update(baseData);

		} else if (baseData.getType().equals(ResourcesConstants.BASE_DATA_TYPE_BAD_CONFIG)) {
			dao.update(baseData);
		} else if (baseData.getType().equals(ResourcesConstants.BASE_DATA_TYPE_TEAM_CONFIG)) {
			dao.update(baseData);
		}

		return SUCCESS;
	}

	@Autowired
	private ProductDAO productDao;

	@Override
	public String deleteBaseData(BaseDataBean baseData) {
		if (baseData.getType().equals(ResourcesConstants.BASE_DATA_TYPE_LINE)) {
			ProductBean pOption = new ProductBean();
			pOption.setLocation(baseData.getName());
			List<ProductBean> products = productDao.selectByParam(pOption);

			if (null != products && products.size() > 0) {
				return EXISTS;
			}

			dao.delete(baseData.getPk());
		}
		return SUCCESS;
	}

}
