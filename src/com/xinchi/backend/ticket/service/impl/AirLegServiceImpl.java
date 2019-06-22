package com.xinchi.backend.ticket.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.ticket.dao.AirLegDAO;
import com.xinchi.backend.ticket.service.AirLegService;
import com.xinchi.bean.AirLegBean;
import com.xinchi.tools.Page;

@Service
@Transactional
public class AirLegServiceImpl implements AirLegService {

	@Autowired
	private AirLegDAO dao;

	@Override
	public void insert(AirLegBean bean) {
		dao.insert(bean);
	}

	@Override
	public void update(AirLegBean bean) {
		dao.update(bean);
	}

	@Override
	public void delete(String id) {
		dao.delete(id);
	}

	@Override
	public AirLegBean selectByPrimaryKey(String id) {
		return dao.selectByPrimaryKey(id);
	}

	@Override
	public List<AirLegBean> selectByParam(AirLegBean bean) {
		return dao.selectByParam(bean);
	}

	@Override
	public List<AirLegBean> selectByPage(Page<AirLegBean> page) {
		return dao.selectByPage(page);
	}

	@Override
	public String createLeg(AirLegBean leg) {
		AirLegBean option = new AirLegBean();
		option.setFrom_city(leg.getFrom_city());
		option.setTo_city(leg.getTo_city());
		List<AirLegBean> olds = dao.selectByParam(option);
		if (null != olds && olds.size() > 0) {
			return EXISTS;
		}
		dao.insert(leg);
		return SUCCESS;
	}

	@Override
	public String updateLeg(AirLegBean leg) {
		AirLegBean option = new AirLegBean();
		option.setFrom_city(leg.getFrom_city());
		option.setTo_city(leg.getTo_city());
		List<AirLegBean> olds = dao.selectByParam(option);
		if (null != olds && olds.size() > 0) {
			for (AirLegBean l : olds) {
				if (!l.getPk().equals(leg.getPk())) {
					return EXISTS;
				}
			}
		}
		dao.update(leg);
		return SUCCESS;
	}

}