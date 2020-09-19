package com.xinchi.backend.accounting.dao;

import java.util.List;

import com.xinchi.bean.ReimbursementBean;
import com.xinchi.tools.Page;

public interface ReimbursementDAO {

	public String insert(ReimbursementBean reimbursement);

	public ReimbursementBean selectByPk(String reimbursement_pk);

	public void update(ReimbursementBean reimbursement);

	public void deleteByPk(String pk);

	public List<ReimbursementBean> selectByPage(Page page);

}
