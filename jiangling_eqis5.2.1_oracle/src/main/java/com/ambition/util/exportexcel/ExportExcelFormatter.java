package com.ambition.util.exportexcel;

import org.apache.poi.ss.usermodel.Cell;

/**
 * 类名:导出对象到Excel的格式化
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  赵骏
 * @version 1.00 2014-7-27 发布
 */
public interface ExportExcelFormatter {
	/**是否设置值得标志*/
	public static final String HASSET_FLAG = "_HASSETVALUE";
	public String format(Object value,int rowNum,String fieldName,Cell cell);
}
