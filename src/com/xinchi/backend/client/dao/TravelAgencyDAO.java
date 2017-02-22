package com.xinchi.backend.client.dao;

import java.util.List;

import com.xinchi.bean.TravelAgencyBean;
import com.xinchi.tools.Page;

public interface TravelAgencyDAO {

	public void insert(TravelAgencyBean agency);

	public List<TravelAgencyBean> getAllByPage(Page page);

	public TravelAgencyBean selectByPk(String agency_pk);

	public void update(TravelAgencyBean agency);

	public List<TravelAgencyBean> selectSameName(String content);

	public List<TravelAgencyBean> selectSameCode(String content);

}
