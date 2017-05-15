package com.xinchi.backend.sys.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinchi.backend.sys.dao.SystemGuideDAO;
import com.xinchi.backend.sys.service.SystemGuideService;
import com.xinchi.bean.SystemGuideBean;
import com.xinchi.common.Utils;
import com.xinchi.tools.Page;

@Service
@Transactional
public class SystemGuideServiceImpl implements SystemGuideService {

	@Autowired
	private SystemGuideDAO dao;

	@Override
	public void insert(SystemGuideBean view) {
		dao.insert(view);
	}

	@Override
	public List<SystemGuideBean> getAllViewsByPage(Page page) {
		return dao.getAllViewsByPage(page);
	}

	@Override
	public SystemGuideBean selectViewByPk(String view_pk) {

		return dao.selectByPk(view_pk);
	}

	@Override
	public void update(SystemGuideBean view) {
		dao.update(view);
	}

	@Override
	public void delete(String view_pk) {
		SystemGuideBean view = selectViewByPk(view_pk);
		if (null == view)
			return;
		
		//删除保存在磁盘中的图片
		String imgEx = "<img (src=\"/XinChi/file/getFileStream\\?)(.*?)\".*? \\/\\>";
		Pattern imgPattern = Pattern.compile(imgEx);
		Matcher matcher = imgPattern.matcher(view.getContent());
		List<String> imgs = new ArrayList<String>();

		while (matcher.find()) {
			imgs.add(matcher.group(2));
		}

		List<Map<String, String>> deletes = new ArrayList<Map<String, String>>();
		for (String img : imgs) {
			String[] properties = img.split("&");
			Map<String, String> map = new HashMap<String, String>();
			for (String property : properties) {

				String[] names = property.split("=");
				if (names.length > 1) {
					map.put(names[0], names[1]);
				}
			}
			deletes.add(map);
		}

		for (Map<String, String> map : deletes) {
			Utils.deleteFileFromDisk(map.get("fileType"), map.get("subFolder"), map.get("fileFileName"));
		}

		dao.delete(view_pk);
	}

}
