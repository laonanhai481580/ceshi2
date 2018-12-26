package com.ambition.spc.statistics.service;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
/**
 * 类名:数组映射
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  赵骏
 * @version 1.00 2015-9-16 发布
 */
public class CustomArrowRowMapper implements RowMapper {
	@Override
	public Object mapRow(ResultSet rs, int index) throws SQLException {
		int columnCount = rs.getMetaData().getColumnCount();
		Object[] objs = new Object[columnCount];
		for(int i=0;i<columnCount;i++){
			objs[i] = rs.getObject(i+1);
		}
		return objs;
	}
}
