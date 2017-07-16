package com.xinchi.backend.accounting.dao;

import com.xinchi.bean.ReimbursementBean;

public interface ReimbursementDAO {

	public String insert(ReimbursementBean reimbursement);

	public ReimbursementBean selectByPk(String reimbursement_pk);

	public void update(ReimbursementBean reimbursement);

	public void deleteByPk(String pk);

}
