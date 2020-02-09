package com.xinchi.backend.data.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.data.dao.OrderCountDAO;
import com.xinchi.bean.DataOrderCountDto;
import com.xinchi.common.DaoUtil;

@Repository
public class OrderCountDAOImpl extends SqlSessionDaoSupport implements OrderCountDAO {
	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public List<DataOrderCountDto> selectMonthOrderCount(DataOrderCountDto order_count) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.ViewDataOrderCountMapper.selectMonthOrderCount",
				order_count);
	}

	@Override
	public List<DataOrderCountDto> selectDayOrderCount(DataOrderCountDto order_count) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.ViewDataOrderCountMapper.selectDayOrderCount",
				order_count);
	}

	@Override
	public List<DataOrderCountDto> selectWeekOrderCount(DataOrderCountDto order_count) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.ViewDataOrderCountMapper.selectWeekOrderCount",
				order_count);
	}

}
