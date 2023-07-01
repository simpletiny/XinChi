package com.xinchi.backend.receivable.service.impl;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.receivable.dao.AirReceivedDAO;
import com.xinchi.backend.receivable.service.AirReceivedService;
import com.xinchi.backend.supplier.dao.SupplierDepositDAO;
import com.xinchi.bean.AirReceivedDetailBean;
import com.xinchi.bean.SupplierDepositBean;
import com.xinchi.common.FileFolder;
import com.xinchi.common.FileUtil;
import com.xinchi.tools.Page;

@Service
@Transactional
public class AirReceivedServiceImpl implements AirReceivedService {

	@Autowired
	private AirReceivedDAO dao;

	@Override
	public void insert(AirReceivedDetailBean detail) {
		dao.insert(detail);
	}

	@Override
	public List<AirReceivedDetailBean> selectByPage(Page<AirReceivedDetailBean> page) {
		return dao.selectByPage(page);
	}

	@Autowired
	private SupplierDepositDAO supplierDepositDao;

	@Override
	public String rollBackReceived(String related_pk) {
		List<AirReceivedDetailBean> details = dao.selectByRelatedPk(related_pk);

		for (AirReceivedDetailBean detail : details) {
			dao.deleteByPk(detail.getPk());
			String deposit_number = detail.getBusiness_number();
			SupplierDepositBean deposit = supplierDepositDao.selectByDepositNumber(deposit_number);
			List<AirReceivedDetailBean> exists = dao.selectByBusinessNumber(deposit_number);
			deposit.setBalance(deposit.getBalance().add(detail.getReceived()));
			deposit.setReceived(deposit.getReceived().subtract(detail.getReceived()));
			if (null == exists || exists.size() < 1) {
				deposit.setReturn_way(
						deposit.getReturn_way() != null ? deposit.getReturn_way().replaceAll(",T|T", "") : "");
			}
			supplierDepositDao.update(deposit);
			// 删除凭证文件
			String[] split_str = detail.getReceived_time().split("-");
			if (split_str.length > 1) {
				String sub_folder = split_str[0] + File.separator + split_str[1];
				FileUtil.deleteFile(detail.getVoucher_file(), FileFolder.SUPPLIER_RECEIVED_VOUCHER.value(), sub_folder);
			}
		}
		return SUCCESS;
	}

	@Override
	public void update(AirReceivedDetailBean detail) {
		dao.update(detail);
	}

	@Override
	public AirReceivedDetailBean selectByPk(String detail_pk) {
		return dao.selectByPk(detail_pk);
	}

	@Override
	public List<AirReceivedDetailBean> selectByParam(AirReceivedDetailBean option) {
		return dao.selectByParam(option);
	}

	@Override
	public List<AirReceivedDetailBean> selectByBusinessNumber(String business_number) {
		return dao.selectByBusinessNumber(business_number);
	}

}
