package com.xinchi.backend.finance.dao.impl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.xinchi.backend.finance.dao.CardDAO;
import com.xinchi.bean.CardBean;
import com.xinchi.common.DaoUtil;

@Repository
public class CardDAOImpl extends SqlSessionDaoSupport implements CardDAO {

	private SqlSession sqlSession;
	private DaoUtil daoUtil;

	public void initDao() {
		if (daoUtil == null) {
			sqlSession = sqlSession == null ? getSqlSession() : sqlSession;
			daoUtil = new DaoUtil(sqlSession);
		}
	}

	@Override
	public void insert(CardBean bo) {
		daoUtil.insertBO("com.xinchi.bean.mapper.CardMapper.insert", bo);
	}

	@Override
	public List<CardBean> getAllByParam(CardBean bo) {
		List<CardBean> list = daoUtil.selectByBOParamT("com.xinchi.bean.mapper.CardMapper.selectByParam", bo);
		return list;
	}

	@Override
	public List<String> getAllAccounts() {
		List<String> list = (List) daoUtil.selectList("com.xinchi.bean.mapper.CardMapper.selectAllAccounts");
		return list;
	}

	@Override
	public String getAccountBalance(String account) {
		String balance = daoUtil.selectOneValueByParam(("com.xinchi.bean.mapper.CardMapper.selectBalanceByAccount"),
				account);
		return balance;
	}

	@Override
	public CardBean getCardByAccount(String account) {
		return daoUtil.selectOneValueByParam(("com.xinchi.bean.mapper.CardMapper.selectCardByAccount"), account);
	}

	@Override
	public void update(CardBean card) {
		daoUtil.updateByPK("com.xinchi.bean.mapper.CardMapper.updateByPrimaryKey", card);

	}

	@Override
	public CardBean getCardByNumber(String card_number) {
		return daoUtil.selectOneValueByParam(("com.xinchi.bean.mapper.CardMapper.selectCardByNumber"), card_number);
	}

	@Override
	public CardBean selectByPk(String pk) {
		return (CardBean) daoUtil.selectByPK("com.xinchi.bean.mapper.CardMapper.selectByPk", pk);
	}

	@Override
	public List<CardBean> selectByPurpose(String purpose) {
		return daoUtil.selectByParam("com.xinchi.bean.mapper.CardMapper.selectByPurpose", purpose);
	}

	@Override
	public BigDecimal selectSumBalance(List<String> accounts) {
		return daoUtil.selectOneValueByParam("com.xinchi.bean.mapper.CardMapper.selectSumBalance",accounts);
	}
}
