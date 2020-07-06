package com.xinchi.backend.util.dao;

import com.xinchi.bean.TeamNumberBean;

public interface TeamNumberDAO {

	public TeamNumberBean selectNextNumber(TeamNumberBean option);

	public void insert(TeamNumberBean tb);

	public void update(TeamNumberBean tb);

}
