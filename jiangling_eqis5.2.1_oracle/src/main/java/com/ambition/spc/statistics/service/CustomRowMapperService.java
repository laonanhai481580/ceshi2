package com.ambition.spc.statistics.service;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface CustomRowMapperService {
	/**
	  * 方法名: 自定义的值设置接口
	  * <p>功能说明：</p>
	  * @param rs
	  * @param obj
	 */
	public void customMapRow(ResultSet rs,Object obj) throws SQLException;
}
