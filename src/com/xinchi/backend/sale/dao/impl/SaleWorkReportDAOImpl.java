package com.xinchi.backend.sale.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.sale.dao.SaleWorkReportDAO;
import com.xinchi.bean.SaleWorkReportDto;
import com.xinchi.common.DaoUtil;
import com.xinchi.tools.Page;

@Repository
public class SaleWorkReportDAOImpl extends SqlSessionDaoSupport implements SaleWorkReportDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public List<SaleWorkReportDto> selectSwrByPage(Page<SaleWorkReportDto> page) {

		return daoUtil.selectByParam("com.xinchi.bean.mapper.SaleWorkReportMapper.selectSwrByPage", page);
	}

}