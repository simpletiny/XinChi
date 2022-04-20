package com.xinchi.backend.accounting.dao;

import java.util.List;

import com.xinchi.bean.ReceivedDetailDto;
import com.xinchi.tools.Page;

public interface AccountingDAO {

	public List<ReceivedDetailDto> selectByPage(Page<ReceivedDetailDto> page);

}
