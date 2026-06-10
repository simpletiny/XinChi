package com.xinchi.backend.ticket.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.ticket.dao.TicketNameTempletDAO;
import com.xinchi.backend.ticket.service.TicketNameTempletService;
import com.xinchi.bean.TicketNameTempletBean;
import com.xinchi.common.FileFolder;
import com.xinchi.common.FileUtil;
import com.xinchi.tools.Page;

@Service
@Transactional
public class TicketNameTempletServiceImpl implements TicketNameTempletService {

	@Autowired
	private TicketNameTempletDAO dao;

	@Override
	public void insert(TicketNameTempletBean bean) {
		dao.insert(bean);
	}

	@Override
	public void update(TicketNameTempletBean bean) {
		dao.update(bean);
	}

	@Override
	public void delete(String id) {
		dao.delete(id);
	}

	@Override
	public TicketNameTempletBean selectByPrimaryKey(String id) {
		return dao.selectByPrimaryKey(id);
	}

	@Override
	public List<TicketNameTempletBean> selectByParam(TicketNameTempletBean bean) {
		return dao.selectByParam(bean);
	}

	@Override
	public List<TicketNameTempletBean> selectByPage(Page<TicketNameTempletBean> page) {
		return dao.selectByPage(page);
	}

	@Override
	public String createTicketNameTemplet(TicketNameTempletBean templet) {
		// 检查名称是否重复
		TicketNameTempletBean option = new TicketNameTempletBean();
		option.setTemplet_name(templet.getTemplet_name());
		List<TicketNameTempletBean> exists = dao.selectByParam(option);

		if (exists != null && exists.size() > 0) {
			return "existname";
		}

		// 保存文件
		FileUtil.saveFile(templet.getFile_name(), FileFolder.TICKET_NAME_TEMPLET.value());

		dao.insert(templet);
		return SUCCESS;
	}

	@Override
	public String deleteTicketNameTemplet(String templet_pk) {
		TicketNameTempletBean templet = dao.selectByPrimaryKey(templet_pk);
		// 删除模板文件
		FileUtil.deleteFile(templet.getFile_name(), FileFolder.TICKET_NAME_TEMPLET.value());
		// 删除模板
		dao.delete(templet_pk);
		return SUCCESS;
	}

	@Override
	public String updateTicketNameTemplet(TicketNameTempletBean templet) {
		TicketNameTempletBean old = dao.selectByPrimaryKey(templet.getPk());
		if (old.getTemplet_name().equals(templet.getFile_name()) && old.getFile_name().equals(templet.getFile_name()))
			return SUCCESS;

		if (!old.getFile_name().equals(templet.getFile_name())) {
			// 删除之前的文件，保存现在的文件
			FileUtil.deleteFile(old.getFile_name(), FileFolder.TICKET_NAME_TEMPLET.value());
			FileUtil.saveFile(templet.getFile_name(), FileFolder.TICKET_NAME_TEMPLET.value());
		}

		dao.update(templet);
		return SUCCESS;
	}
}