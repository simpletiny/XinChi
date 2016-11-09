package com.xinchi.backend.util.dao;

import com.xinchi.bean.TeamNumberBean;

public interface TeamNumberDAO {

	public TeamNumberBean selectTeamNumberBySalePk(String salePk);

	public void insert(TeamNumberBean tb);

	public void update(TeamNumberBean tb);

}
