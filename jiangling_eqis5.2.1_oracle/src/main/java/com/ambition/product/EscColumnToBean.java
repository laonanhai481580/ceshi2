package com.ambition.product;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.transform.ResultTransformer;

import com.ambition.util.exception.AmbFrameException;
public class EscColumnToBean implements ResultTransformer {
	private static final long serialVersionUID = -2759846471180582799L;
	private Class<?> cla;
	private Map<String,String> sqlFieldMap;
	public EscColumnToBean(Class<?> cla){
		this.cla = cla;
	}
	public EscColumnToBean(Class<?> cla,Map<String,String> sqlFieldMap){
		this.cla = cla;
		this.sqlFieldMap = sqlFieldMap;
	}
	@SuppressWarnings("rawtypes")
	@Override
	public List<?> transformList(List list) {
		return list;
	}

	@Override
	public Object transformTuple(Object[] objs, String[] sqlColumns) {
		try {
			Object obj = cla.newInstance();
			StringBuffer field = new StringBuffer();
			for(int i=0;i<sqlColumns.length;i++){
				field.delete(0,field.length());
				if(sqlFieldMap != null){
					String fieldName = sqlFieldMap.get(sqlColumns[i]);
					if(fieldName==null){
						continue;
					}
					field.append(fieldName);
				}else{
					String strs[] = sqlColumns[i].split("_");
					for(int j=0;j<strs.length;j++){
						if(j>0){
							field.append(strs[j].substring(0,1).toUpperCase());
							field.append(strs[j].substring(1).toLowerCase());
						}else{
							field.append(strs[j].toLowerCase());
						}
					}
				}
				setObjValue(obj,field.toString(),objs[i]);
			}
			return obj;
		} catch (Exception e) {
			throw new AmbFrameException("SQL查询转换出错!",e);
		}
	}
	private static void setObjValue(Object obj,String fieldName,Object value) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		Class<?> typeClass = PropertyUtils.getPropertyType(obj,fieldName);
		if(typeClass != null){
			if(value == null){
				PropertyUtils.setProperty(obj,fieldName,null);
			}else if(value.getClass().toString().equals(typeClass.toString())){
				PropertyUtils.setProperty(obj,fieldName,value);
			}else if(typeClass.toString().equals(String.class.toString())){
				PropertyUtils.setProperty(obj,fieldName,value+"");
			}else if(typeClass.toString().equals(Double.class.toString())){
				PropertyUtils.setProperty(obj,fieldName,Double.valueOf(value.toString()));
			}else if(typeClass.toString().equals(Integer.class.toString())){
				PropertyUtils.setProperty(obj,fieldName,Integer.valueOf(value.toString()));
			}else if(typeClass.toString().equals(Long.class.toString())){
				PropertyUtils.setProperty(obj,fieldName,Long.valueOf(value.toString()));
			}else{
				PropertyUtils.setProperty(obj,fieldName,value);
			}
		}
	}
}
