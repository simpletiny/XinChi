package com.xinchi.backend.culture.service;

import java.util.List;

import com.xinchi.bean.XinChiPromiseBean;
import com.xinchi.tools.Page;

public interface XinChiPromiseService {
	public void insert(XinChiPromiseBean view);

	public List<XinChiPromiseBean> getAllViewsByPage(Page<XinChiPromiseBean> page);

	public XinChiPromiseBean selectViewByPk(String view_pk);

	public void update(XinChiPromiseBean view);

	public void delete(String view_pk);

}