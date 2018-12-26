package com.ambition.util.exportexcel;

import java.util.Map;

/**
 * 类名:导出台帐时获取动态列对象
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  赵骏
 * @version 1.00 2015-1-12 发布
 */
public interface ExcelListDynamicColumnValue {
	public Map<String,String> getDynamicColumnValue(Object value,int rowNum,Map<String,String> valueMap);
}
