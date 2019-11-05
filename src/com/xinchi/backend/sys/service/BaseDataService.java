package com.xinchi.backend.sys.service;

import java.util.List;

import com.xinchi.bean.BaseDataBean;
import com.xinchi.common.BaseService;

public interface BaseDataService extends BaseService {

	public void insert(BaseDataBean base);

	public BaseDataBean selectByPk(String pk);

	public void update(BaseDataBean base);

	public void delete(String pk);

	public List<BaseDataBean> selectByParam(BaseDataBean base);

	public List<BaseDataBean> selectByFatherLevelPk(String father_level_pk);

	public String sortData(String json);

	public String createBaseData(BaseDataBean baseData);

	public String updateBaseData(BaseDataBean baseData);

	public String deleteBaseData(BaseDataBean baseData);
}
