package com.xinchi.backend.sys.dao;

import java.util.List;

import com.xinchi.bean.BaseDataBean;

public interface BaseDataDAO {

	public void insert(BaseDataBean base);

	public BaseDataBean selectByPk(String pk);

	public void update(BaseDataBean base);

	public void delete(String pk);

	public List<BaseDataBean> selectByParam(BaseDataBean base);

	public List<BaseDataBean> selectByFatherLevelPk(String father_level_pk);

}
