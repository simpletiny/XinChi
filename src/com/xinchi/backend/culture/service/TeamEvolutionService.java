package com.xinchi.backend.culture.service;

import java.util.List;

import com.xinchi.bean.TeamEvolutionBean;
import com.xinchi.tools.Page;

public interface TeamEvolutionService {
	public void insert(TeamEvolutionBean view);

	public List<TeamEvolutionBean> getAllViewsByPage(Page<TeamEvolutionBean> page);

	public TeamEvolutionBean selectViewByPk(String view_pk);

	public void update(TeamEvolutionBean view);

	public void delete(String view_pk);

}
