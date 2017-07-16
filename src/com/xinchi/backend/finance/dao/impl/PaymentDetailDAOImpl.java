package com.xinchi.backend.finance.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.finance.dao.PaymentDetailDAO;
import com.xinchi.bean.PaymentDetailBean;
import com.xinchi.common.DaoUtil;
import com.xinchi.tools.Page;

@Repository
public class PaymentDetailDAOImpl extends SqlSessionDaoSupport implements PaymentDetailDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(PaymentDetailBean bo) {
		daoUtil.insertBO("com.xinchi.bean.mapper.PaymentDetailMapper.insert", bo);
	}

	@Override
	public List<PaymentDetailBean> selectAllDetailsByParam(PaymentDetailBean bean) {
		return daoUtil.selectByBOParamT("com.xinchi.bean.mapper.PaymentDetailMapper.selectByParam", bean);
	}

	@Override
	public List<PaymentDetailBean> selectAllDetailsByPage(Page<PaymentDetailBean> page) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.PaymentDetailMapper.selectByPage", page);
	}

	@Override
	public PaymentDetailBean selectById(String pk) {
		return (PaymentDetailBean) daoUtil.selectByPK("com.xinchi.bean.mapper.PaymentDetailMapper.selectByPrimaryKey", pk);
	}

	@Override
	public List<PaymentDetailBean> selectAfterByParam(PaymentDetailBean detail) {
		return daoUtil.selectByBOParamT("com.xinchi.bean.mapper.PaymentDetailMapper.selectAfterByParam", detail);
	}

	@Override
	public void updateDetails(List<PaymentDetailBean> afterDetails) {
		daoUtil.updateBOList("com.xinchi.bean.mapper.PaymentDetailMapper.updateByPrimaryKey", afterDetails);
	}

	@Override
	public void delete(String pk) {
		daoUtil.deleteByPK("com.xinchi.bean.mapper.PaymentDetailMapper.deleteByPrimaryKey", pk);
	}

	@Override
	public void updateDetail(PaymentDetailBean newDetail) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.PaymentDetailMapper.updateByPrimaryKey", newDetail);
	}

	@Override
	public PaymentDetailBean selectPreDetail(PaymentDetailBean detail) {
		return daoUtil.selectOneValueByParam("com.xinchi.bean.mapper.PaymentDetailMapper.selectPreDetail", detail);
	}

	@Override
	public void insertDetails(List<PaymentDetailBean> details) {
		daoUtil.insertBOList("com.xinchi.bean.mapper.PaymentDetailMapper.insert", details);
	}

	@Override
	public List<PaymentDetailBean> selectByVoucherNumber(String voucher_number) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.PaymentDetailMapper.selectByVoucherNumber", voucher_number);
	}
}
