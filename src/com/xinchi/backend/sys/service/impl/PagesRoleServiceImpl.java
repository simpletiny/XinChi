package com.xinchi.backend.sys.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.sys.dao.BaseDataDAO;
import com.xinchi.backend.sys.dao.PagesDAO;
import com.xinchi.backend.sys.dao.PagesRoleDAO;
import com.xinchi.backend.sys.service.PagesRoleService;
import com.xinchi.bean.BaseDataBean;
import com.xinchi.bean.PagesBean;
import com.xinchi.bean.PagesRoleBean;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
@Transactional
public class PagesRoleServiceImpl implements PagesRoleService {

	@Autowired
	private PagesRoleDAO dao;

	@Override
	public void insert(PagesRoleBean bean) {
		dao.insert(bean);
	}

	@Override
	public void update(PagesRoleBean bean) {
		dao.update(bean);
	}

	@Override
	public void delete(String id) {
		dao.delete(id);
	}

	@Override
	public PagesRoleBean selectByPrimaryKey(String id) {
		return dao.selectByPrimaryKey(id);
	}

	@Override
	public List<PagesRoleBean> selectByParam(PagesRoleBean bean) {
		return dao.selectByParam(bean);
	}

	@Autowired
	private BaseDataDAO baseDataDao;

	@Autowired
	private PagesDAO pagesDao;

	@Override
	public String updateRolePages(String json) {
		JSONObject obj = JSONObject.fromObject(json);
		String role = obj.getString("role");

		// 保存角色优先级
		JSONArray role_order_arr = obj.getJSONArray("role_order");
		if (role_order_arr != null && role_order_arr.size() > 0) {
			for (int i = 0; i < role_order_arr.size(); i++) {
				JSONObject current_obj = role_order_arr.getJSONObject(i);
				String pk = current_obj.getString("pk");
				int order_index = current_obj.getInt("order_index");
				BaseDataBean bd = new BaseDataBean();
				bd.setPk(pk);
				bd.setOrder_index(order_index);
				baseDataDao.update(bd);
			}
		}

		// 保存首页指向
		JSONArray first_pages = obj.getJSONArray("first_pages");
		if (first_pages != null && first_pages.size() > 0) {
			PagesRoleBean option = new PagesRoleBean();
			option.setRole(role);
			for (int i = 0; i < first_pages.size(); i++) {
				JSONObject current_obj = first_pages.getJSONObject(i);
				int pk = Integer.valueOf(current_obj.getString("pk"));
				int child_pk = current_obj.getInt("child_pk");
				option.setPage_pk(pk);
				List<PagesRoleBean> existPages = dao.selectByParam(option);
				PagesBean page = pagesDao.selectByPrimaryKey(String.valueOf(child_pk));
				if (null != existPages && existPages.size() > 0) {
					PagesRoleBean pagesRole = existPages.get(0);
					pagesRole.setPage_url(page.getPage_url());
					dao.update(pagesRole);
				} else {
					PagesRoleBean pagesRole = new PagesRoleBean();
					pagesRole.setRole(role);
					pagesRole.setPage_pk(pk);
					pagesRole.setPage_url(page.getPage_url());
					pagesRole.setIs_father("Y");
					dao.insert(pagesRole);
				}
			}
		}

		// 保存添加的页面
		JSONArray added_pages = obj.getJSONArray("added_pages");
		if (added_pages != null && added_pages.size() > 0) {
			for (int i = 0; i < added_pages.size(); i++) {
				int page_pk = added_pages.getInt(i);
				PagesBean page = pagesDao.selectByPrimaryKey(String.valueOf(page_pk));
				PagesRoleBean pagesRole = new PagesRoleBean();
				pagesRole.setRole(role);
				pagesRole.setPage_pk(page_pk);
				pagesRole.setPage_url(page.getPage_url());
				dao.insert(pagesRole);
			}
		}

		// 保存删除的页面
		JSONArray removed_pages = obj.getJSONArray("removed_pages");
		if (removed_pages != null && removed_pages.size() > 0) {
			PagesRoleBean option = new PagesRoleBean();
			option.setRole(role);
			for (int i = 0; i < removed_pages.size(); i++) {
				int page_pk = removed_pages.getInt(i);
				option.setPage_pk(page_pk);
				List<PagesRoleBean> existPages = dao.selectByParam(option);
				for (PagesRoleBean pages_role : existPages) {
					dao.delete(pages_role.getPk());
				}
			}
		}
		return SUCCESS;
	}

	@Override
	public List<PagesRoleBean> searchRolePages(String role) {
		PagesRoleBean option = new PagesRoleBean();
		option.setRole(role);
		return dao.selectByParam(option);
	}

}