package com.ambition.util.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

import com.norteksoft.mms.form.entity.ListColumn;
import com.norteksoft.mms.form.entity.TableColumn;
import com.norteksoft.mms.form.enumeration.DataType;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.ListView;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.web.struts2.Struts2Utils;


/**
 * 类名:自定义页面处理工具类
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * @author  赵骏
 * @version 1.00 2016-2-29 发布
 */
public class CustomPageUtil {
	/**
	 *  把page对象转换为前台显示用的json page对象
	 * @param page
	 * @return
	 * @throws Exception 
	 */
	public static JSONObject pageToJson(Page<?> page) throws Exception{
		return pageToJson(page,Struts2Utils.getParameter("_list_code"));
	}
	
	/**
	 *  把page对象转换为前台显示用的json page对象
	 * @param page
	 * @return
	 * @throws Exception 
	 */
	public static JSONObject pageToJson(Page<?> page,String listViewCode) throws Exception{
		JSONObject pageJson = new JSONObject();
		pageJson.put("page",page.getPageNo());
		pageJson.put("total",page.getTotalPages());
		pageJson.put("records", page.getTotalCount());
		//查询listView
		List<TableColumn> columnNames = getListColumns(listViewCode);
		JSONArray rows = new JSONArray();
		for(Object obj : page.getResult()){
			rows.add(convertObjectToJson(obj,columnNames));
		}
		pageJson.put("rows",rows);
		return pageJson;
	}
	
	/**
	  * 方法名: 获取有对应字段的所有的列
	  * <p>功能说明：</p>
	  * @param listCode
	  * @return
	 */
	public static List<TableColumn> getListColumns(String listCode){
		List<TableColumn> columnNames = new ArrayList<TableColumn>();
		ListView listView = ApiFactory.getMmsService().getListViewByCode(listCode);
		Map<String,Boolean> existMap = new HashMap<String,Boolean>();
		if(listView != null){
			List<ListColumn> columns = listView.getColumns();
			for(ListColumn column : columns){
				if(column.getTableColumn() != null && StringUtils.isNotEmpty(column.getTableColumn().getName())){
					String fieldName = column.getTableColumn().getName();
					if(!existMap.containsKey(fieldName)){
						columnNames.add(column.getTableColumn());
					}
				}
			}
		}
		if(!existMap.containsKey("id")){
			TableColumn idColumn = new TableColumn();
			idColumn.setName("id");
			idColumn.setDbColumnName("id");
			idColumn.setDataType(DataType.LONG);
			columnNames.add(idColumn);
		}
		return columnNames;
	}
	
	public static JSONObject convertObjectToJson(Object obj,List<TableColumn> columnNames) throws Exception{
		JSONObject json = new JSONObject();
		for(TableColumn tableColumn : columnNames){
			Object value = PropertyUtils.getProperty(obj,tableColumn.getName());
			if(value != null){
				//日期时,强制转化为字符串
				if(value instanceof Date){
					if(DataType.DATE.getCode().equals(tableColumn.getDataType().getCode())){
						value = DateUtil.formateDateStr((Date)value);
					}else{
						value = DateUtil.formateDateStr((Date)value,"yyyy-MM-dd HH:mm:ss");
					}
				}
				json.put(tableColumn.getName(), value);
			}
		}
		return json;
	}
	
	public static JSONObject convertObjectToJson(Object obj,String listCode) throws Exception{
		List<TableColumn> tableColumns = getListColumns(listCode);
		return convertObjectToJson(obj,tableColumns);
	}
}	
