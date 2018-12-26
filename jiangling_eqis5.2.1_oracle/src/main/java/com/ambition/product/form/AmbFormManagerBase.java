package com.ambition.product.form;


import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;

import com.ambition.util.common.CommonUtil1;
import com.ambition.util.common.DateUtil;
import com.ambition.util.common.ExcelUtil;
import com.ambition.util.exception.AmbFrameException;
import com.ambition.util.exportexcel.ExportExcelFormatter;
import com.norteksoft.mms.form.entity.TableColumn;
import com.norteksoft.mms.form.enumeration.DataType;
import com.norteksoft.product.orm.IdEntity;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;

/**
 * 类名:普通表单、台帐管理封装的基类
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  赵骏
 * @version 1.00 2015-4-20 发布
 */
public abstract class AmbFormManagerBase<T extends IdEntity>{
	private Logger log = Logger.getLogger(this.getClass());
	/**
	  * 方法名: 获取DAO
	  * <p>功能说明：</p>
	  * @return
	 */
	public abstract HibernateDao<T, Long> getHibernateDao();
	
	/**
	  * 方法名:获取实体在系统元数据中定义的列表编码 
	  * <p>功能说明：</p>
	  * @return
	 */
	public abstract String getEntityListCode();
	/**
	  * 方法名:获取实体在系统元数据中定义的列表名称 
	  * <p>功能说明：</p>
	  * @return
	 */
	public abstract String getEntityListName();
	
	/**
	  * 方法名: 获取实例的Class
	  * <p>功能说明：</p>
	  * @return
	 */
	public abstract Class<T> getEntityInstanceClass();
	
	/**
	  * 方法名: 根据表单编号查询对象
	  * <p>功能说明：</p>
	  * @param cla 对象
	  * @param formNo 编号
	  * @return
	 */
	public T findReportByFormNo(String formNo){
		return findReportByFormNo(formNo,"formNo");
	}
	
	/**
	  * 方法名: 根据表单编号查询对象
	  * <p>功能说明：</p>
	  * @param cla 对象
	  * @param formNo 编号
	  * @param formNoFieldName 编号对应的字段
	  * @return
	 */
	@SuppressWarnings("unchecked")
	public T findReportByFormNo(String formNo,String formNoFieldName){
		String hql = "from "+getEntityInstanceClass().getName()+" f where f."+formNoFieldName+" = ?";
		List<?> list = getHibernateDao().createQuery(hql,formNo).list();
		if(list.size()>0){
			return (T) list.get(0);
		}else{
			return null;
		}
	}

	/**
	  * 方法名: 设置子表的值
	  * <p>功能说明：</p>
	  * @param report
	  * @param childMaps
	  * @throws Exception
	 */
	private void setChildItems(T report,Map<String,List<JSONObject>> childMaps){
		try {
			if(childMaps != null){
				//子表中主表的字段名
				String reportFieldName = getEntityInstanceClass().getSimpleName();
				reportFieldName = reportFieldName.substring(0,1).toLowerCase() + reportFieldName.substring(1);
				for(String fieldName : childMaps.keySet()){
					List<JSONObject> arrays = childMaps.get(fieldName);
					//检查是否有对象属性
					Class<?> claType = PropertyUtils.getPropertyType(report, fieldName);
					if(claType==null){
						throw new AmbFrameException(report.getClass() + "没有名称为[" + fieldName + "]的字段!");
					}
					if(!List.class.getName().equals(claType.getName())){
						throw new AmbFrameException(report.getClass() + "名称为[" + fieldName + "]的字段必须是List类型!");
					}
					@SuppressWarnings("unchecked")
					List<Object> items = (List<Object>) PropertyUtils.getProperty(report,fieldName);
					if(items==null){
						List<Object> objs = new ArrayList<Object>();
						PropertyUtils.setProperty(report,fieldName,objs);
						items = objs;
					}else{
						items.clear();//清除
					}
					for(JSONObject json : arrays){
						String entityClass = json.getString("entityClass");
						json.remove("entityClass");
						IdEntity item = (IdEntity) Class.forName(entityClass).newInstance();
						item.setCompanyId(report.getCompanyId());
						item.setCreatedTime(report.getCreatedTime());
						item.setCreator(report.getCreator());
						item.setCreatorName(report.getCreatorName());
						item.setDepartmentId(report.getDepartmentId());
						item.setModifiedTime(new Date());
						item.setModifier(ContextUtils.getLoginName());
						item.setModifiedTime(new Date());
						item.setModifierName(ContextUtils.getUserName());
						//设置主表
						PropertyUtils.setProperty(item,reportFieldName,report);
						//设置属性
						for(Object key : json.keySet()){
							CommonUtil1.setProperty(item,key.toString(),json.get(key));
						}
						getHibernateDao().getSession().save(item);
						items.add(item);
					}
				}
			}
		} catch (Exception e) {
			if(e instanceof AmbFrameException){
				throw (AmbFrameException)e;
			}else{
				throw new AmbFrameException("设置子表的值时失败!",e);
			}
		}
	}
	
	/**
	  * 方法名: 保存对象
	  * <p>功能说明：</p>
	  * @param report
	  * @return
	 */
	public void saveEntity(T report) {
		try {
			saveEntity(report,null);
		} catch (Exception e) {
			throw new AmbFrameException("保存对象失败!",e);
		}
	}

	/**
	  * 方法名: 保存对象
	  * <p>功能说明：</p>
	  * @param report
	  * @param childMaps 子表对象
	  * @return
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws ClassNotFoundException 
	 * @throws InstantiationException 
	 */
	public void saveEntity(T report,Map<String,List<JSONObject>> childMaps){
		report.setModifiedTime(new Date());
		report.setModifier(ContextUtils.getLoginName());
		report.setModifierName(ContextUtils.getUserName());
		//设置子表的值
		setChildItems(report,childMaps);
		
		getHibernateDao().save(report);
	}
	
	/**
	  * 方法名: 分页查询
	  * <p>功能说明：</p>
	  * @param page
	  * @return
	 */
	public Page<T> search(Page<T> page){
		String hql = "from "+getEntityInstanceClass().getName()+" t";
		page = getHibernateDao().searchPageByHql(page,hql);
		return page;
	}
	/**
	  * 方法名: 根据ID获取对象
	  * <p>功能说明：</p>
	  * @param id 
	  * @return
	 */
	public T getEntity(Long id) {
		return getHibernateDao().get(id);
	}
	
	/**
	 * 删除实体，流程相关文件都删除
	 * @param entity 删除的对象
	 */
	public void deleteEntity(T entity){
		//删除对象
		getHibernateDao().delete(entity);
	}
	
	/**
	 * 删除实体，流程相关文件都删除
	 * @param ids
	 */
	public String deleteEntity(String ids) {
		String[] deleteIds = ids.split(",");
		int deleteNum=0,failNum=0;
		for (String id : deleteIds) {
			T report = getEntity(Long.valueOf(id));
			deleteEntity(report);
			deleteNum++;
		}
		return deleteNum+" 条数据成功删除，"+failNum+" 条数据没有权限删除！";
	}
	
	/**
	  * 方法名:导出Excel文件 
	  * <p>功能说明：</p>
	  * @param qrqcReport
	  * @throws IOException
	 */
	public void exportReport(Long entityId) throws IOException{}
	
	/**
	  * 方法名:导出Excel文件 
	  * <p>功能说明：</p>
	  * @param qrqcReport
	  * @throws IOException
	 */
	public void exportReport(Long entityId,String templateName,String exportFileName) throws IOException{
		exportReport(entityId, templateName, exportFileName,null);
	}
	
	/**
	  * 方法名:导出Excel文件 
	  * <p>功能说明：</p>
	  * @param qrqcReport
	  * @throws IOException
	 */
	public void exportReport(Long entityId,String templateName,String exportFileName,Map<String,ExportExcelFormatter> formatterMap) throws IOException{
		InputStream inputStream = null;
		try {
			T report = getEntity(entityId);
			inputStream = this.getClass().getClassLoader().getResourceAsStream("template/report/" + templateName);
			ExcelUtil.exportToExcel(inputStream, exportFileName, report,formatterMap);
		}catch (Exception e) {
			log.error("导出失败!",e);
		} finally{
			if(inputStream != null){
				inputStream.close();
			}
		}
	}
	
	/**
	  * 方法名: 根据SQL查询对象
	  * <p>功能说明：</p>
	  * @param page
	  * @param sql
	  * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 */
	@SuppressWarnings("unchecked")
	public <K extends Object> Page<K> executeTaskSql(Page<K> page,Class<K> objClass,String sql,Object... searchValues) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		List<Object> searchParams = new ArrayList<Object>();
		if(searchValues != null){
			for(Object val : searchValues){
				searchParams.add(val);
			}
		}
		StringBuffer sqlSb = new StringBuffer("from (" + sql + ") t ");
		String searchParameters = Struts2Utils.getParameter("searchParameters");
		if(StringUtils.isNotEmpty(searchParameters)){
			JSONArray jsonParams = JSONArray.fromObject(searchParameters);
			boolean isFirst = true;
			for(Object obj : jsonParams){
				JSONObject p = (JSONObject)obj;
				String dbName = p.getString("dbName");
				String propValue = p.getString("propValue");
				String dataType = p.getString("dataType");
				String optSign = p.getString("optSign");
				Object value = propValue;
				if(DataType.DATE.getCode().equals(dataType)){
					value = DateUtil.parseDate(propValue,"yyyy-MM-dd HH:mm:ss");
				}else if(DataType.INTEGER.getCode().equals(dataType)){
					if(CommonUtil1.isInteger(propValue)){
						continue;
					}
					value = Integer.valueOf(propValue);
				}
				if(isFirst){
					isFirst = false;
					sqlSb.append(" where ");
				}else{
					sqlSb.append(" and ");
				}
				sqlSb.append("t." + dbName);
				if("like".equals(optSign)){
					sqlSb.append( " like ?");
					searchParams.add("%" + value + "%");
				}else{
					sqlSb.append(" " + optSign + " ?");
					searchParams.add(value);
				}
			}
		}
		
		//查询对应的字段映射
		String tableColumnSql = "select c.db_column_name,c.name,c.data_type from mms_table_column c "
					+ " inner join mms_data_table t "
					+ " on c.fk_data_table_id = t.id and t.name = ?";
		List<?> columns = getHibernateDao().getSession().createSQLQuery(tableColumnSql)
					.setParameter(0, Struts2Utils.getParameter("_list_code"))
					.list();
		Map<String,TableColumn> columnMap = new HashMap<String, TableColumn>();
		String orderBy = null;
		for(Object obj : columns){
			Object[] objs = (Object[])obj;
			String columnName = objs[0]+"";
			String fieldName = objs[1]+"";
			String dataType = objs[2]+"";
			TableColumn tableColumn = new TableColumn();
			tableColumn.setDataType(DataType.valueOf(dataType));
			tableColumn.setDbColumnName(columnName);
			tableColumn.setName(fieldName);
			columnMap.put(columnName,tableColumn);
			//转换Order by
			if(fieldName.equals(page.getOrderBy())){
				orderBy = columnName;
			}
		}
		//统计总数
		Query query = getHibernateDao().getSession().createSQLQuery("select count(*) " + sqlSb.toString());
		for(int index=0;index<searchParams.size();index++){
			query.setParameter(index,searchParams.get(index));
		}
		page.setTotalCount(Long.valueOf(query.list().get(0).toString()));
		//统计当前页
		if(StringUtils.isNotEmpty(page.getOrder())&&StringUtils.isNotEmpty(orderBy)){
			sqlSb.append(" order by " + orderBy + " " + page.getOrder());
		}
		query = getHibernateDao().getSession().createSQLQuery("select * " + sqlSb.toString());
		for(int index=0;index<searchParams.size();index++){
			query.setParameter(index,searchParams.get(index));
		}
		query.setFirstResult(page.getFirst()-1);
		query.setMaxResults(page.getPageSize());
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<?> list = query.list();
		List<K> results = new ArrayList<K>();
		for(Object obj : list){
			K entity = objClass.newInstance();
			Map<String,Object> objMap = (Map<String,Object>)obj;
			for(String columnName : objMap.keySet()){
				TableColumn tableColumn = columnMap.get(columnName);
				if(tableColumn==null){
					continue;
				}
				Object val = objMap.get(columnName);
				if(val==null){
					continue;
				}
				String fieldName = tableColumn.getName();
				Class<?> fieldClass = PropertyUtils.getPropertyType(entity, fieldName);
				if(fieldClass==null){
					continue;
				}
				if(fieldClass.getName().equals(Long.class.getName())){
					val = Long.valueOf(val.toString());
				}else if(fieldClass.getName().equals(Integer.class.getName())){
					val = Integer.valueOf(val.toString());
				}
				PropertyUtils.setProperty(entity, fieldName,val);
			}
			results.add(entity);
		}
		page.setResult(results);
		return page;
	}
}
