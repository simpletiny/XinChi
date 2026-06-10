package com.xinchi.backend.sys.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.sys.dao.PagesDAO;
import com.xinchi.backend.sys.service.PagesService;
import com.xinchi.bean.PagesBean;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
@Transactional
public class PagesServiceImpl implements PagesService {

	@Autowired
	private PagesDAO dao;

	@Override
	public void insert(PagesBean bean) {
		dao.insert(bean);
	}

	@Override
	public void update(PagesBean bean) {
		dao.update(bean);
	}

	@Override
	public void delete(String id) {
		dao.delete(id);
	}

	@Override
	public PagesBean selectByPrimaryKey(String id) {
		return dao.selectByPrimaryKey(id);
	}

	@Override
	public List<PagesBean> selectByParam(PagesBean bean) {
		return dao.selectByParam(bean);
	}

	@Override
	public String updatePages(String json) {
		JSONObject obj = JSONObject.fromObject(json);
		JSONArray order_arr = obj.getJSONArray("order");
		JSONArray title_arr = obj.getJSONArray("title");
		JSONArray father_pk_arr = obj.getJSONArray("father_pk");

		Map<String, String[]> map_pages = new HashMap<>();
		processJSONArray(order_arr, map_pages, "order_index", 0);
		processJSONArray(title_arr, map_pages, "page_title", 1);
		processJSONArray(father_pk_arr, map_pages, "father_pk", 2);
		List<PagesBean> update_pages = new ArrayList<>();

		for (Map.Entry<String, String[]> entry : map_pages.entrySet()) {
			PagesBean page = new PagesBean();
			String[] data = entry.getValue();
			page.setPk(entry.getKey());

			Integer order_index = data[0] == null ? null : Integer.valueOf(data[0]);
			String father_pk = data[2] == null ? null : String.valueOf(data[2]);
			page.setOrder_index(order_index);
			page.setPage_title(data[1]);
			page.setFather_pk(father_pk);
			update_pages.add(page);
		}

		dao.batchUpdate(update_pages);
		return SUCCESS;
	}

	private void processJSONArray(JSONArray jsonArray, Map<String, String[]> map, String field, int index) {
		if (null == jsonArray || jsonArray.size() < 1)
			return;
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject obj = jsonArray.getJSONObject(i);
			String pk = String.valueOf(obj.getInt("pk")); // 确保 pk 作为字符串存储
			String value = obj.optString(field, ""); // 获取目标字段值，默认空字符串

			// 获取 pk 对应的 String[]，如果不存在，则创建一个新数组
			String[] data = map.getOrDefault(pk, new String[3]);

			// 将值存入数组的对应索引
			data[index] = value;

			// 更新 Map
			map.put(pk, data);
		}
	}

}