package com.xinchi.backend.payable.service.impl;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.accounting.dao.AccPaidDAO;
import com.xinchi.backend.finance.dao.PaymentDetailDAO;
import com.xinchi.backend.finance.service.PaymentDetailService;
import com.xinchi.backend.payable.dao.AirTicketPaidDetailDAO;
import com.xinchi.backend.payable.dao.AirTicketPayableDAO;
import com.xinchi.backend.payable.service.AirTicketPaidDetailService;
import com.xinchi.bean.AirTicketPaidDetailBean;
import com.xinchi.bean.AirTicketPaidDto;
import com.xinchi.bean.AirTicketPayableBean;
import com.xinchi.bean.PaymentDetailBean;
import com.xinchi.bean.WaitingForPaidBean;
import com.xinchi.common.SimpletinyString;
import com.xinchi.tools.Page;
import com.xinchi.tools.PropertiesUtil;

@Service
@Transactional
public class AirTicketPaidDetailServiceImpl implements AirTicketPaidDetailService {

	@Autowired
	private AirTicketPaidDetailDAO dao;

	@Override
	public void insert(AirTicketPaidDetailBean bean) {
		dao.insert(bean);
	}

	@Override
	public void update(AirTicketPaidDetailBean bean) {
		dao.update(bean);
	}

	@Override
	public void delete(String id) {
		dao.delete(id);
	}

	@Override
	public AirTicketPaidDetailBean selectByPrimaryKey(String id) {
		return dao.selectByPrimaryKey(id);
	}

	@Override
	public List<AirTicketPaidDetailBean> selectByParam(AirTicketPaidDetailBean bean) {
		return dao.selectByParam(bean);
	}

	@Override
	public List<AirTicketPaidDto> selectByPage(Page<AirTicketPaidDto> page) {
		return dao.selectByPage(page);
	}

	@Override
	public List<AirTicketPaidDetailBean> selectByRelatedPk(String related_pk) {
		return dao.selectByRelatedPk(related_pk);
	}

	@Autowired
	private AccPaidDAO accPaidDao;

	@Autowired
	private PaymentDetailDAO paymentDetailDao;

	@Autowired
	private AirTicketPayableDAO airTicketPayableDao;

	@Autowired
	private PaymentDetailService paymentDetailService;

	@Override
	public String rollBackPayApply(String related_pk) {

		List<AirTicketPaidDetailBean> details = dao.selectByRelatedPk(related_pk);

		for (AirTicketPaidDetailBean detail : details) {
			// 更新应付款的尾款和已付款
			String payable_pk = detail.getBase_pk();
			AirTicketPayableBean airTicketPayable = airTicketPayableDao.selectByPrimaryKey(payable_pk);
			airTicketPayable.setPaid(airTicketPayable.getPaid().subtract(detail.getMoney()));

			airTicketPayable.setBudget_balance(airTicketPayable.getBudget_balance().add(detail.getMoney()));
			if (airTicketPayable.getFinal_flg().equals("Y")) {
				airTicketPayable.setFinal_balance(airTicketPayable.getFinal_balance().add(detail.getMoney()));
			}
			airTicketPayableDao.update(airTicketPayable);

			// 删除要打回的机票往来详情
			dao.delete(detail.getPk());

		}

		// 删除待支付信息
		List<WaitingForPaidBean> wfps = accPaidDao.selectByRelatedPk(related_pk);
		for (WaitingForPaidBean wfp : wfps) {
			accPaidDao.deleteByPk(wfp.getPk());
		}

		// 删除银行流水的支出信息
		String voucher_number = details.get(0).getVoucher_number();
		if (SimpletinyString.isEmpty(voucher_number))
			return SUCCESS;
		String[] voucher_numbers = voucher_number.split(",");
		String fileFolder = PropertiesUtil.getProperty("voucherFileFolder");

		for (String v_n : voucher_numbers) {
			List<PaymentDetailBean> paymentDetails = paymentDetailDao.selectByVoucherNumber(v_n);
			for (PaymentDetailBean paymentDetail : paymentDetails) {

				// 删除支付凭证
				File destfile = new File(fileFolder + File.separator + paymentDetail.getAccount_pk() + File.separator
						+ paymentDetail.getVoucher_file_name());
				if (destfile.exists()) {
					destfile.delete();
				}

				paymentDetailService.deleteDetail(paymentDetail.getPk());
			}
		}

		return SUCCESS;
	}

}