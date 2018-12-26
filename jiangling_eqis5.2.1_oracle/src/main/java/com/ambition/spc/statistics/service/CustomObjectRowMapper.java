package com.ambition.spc.statistics.service;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import oracle.sql.TIMESTAMP;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.jdbc.core.RowMapper;

import com.ambition.util.exception.AmbFrameException;
/**
 * 类名:质量特性转换
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  赵骏
 * @version 1.00 2015-9-16 发布
 */
public class CustomObjectRowMapper implements RowMapper {
	private Map<String,String> fieldNameMap = new HashMap<String, String>();
	private Class<?> cla = null;
	private CustomRowMapperService customRowMapperService;
	private void init(Class<?> cla){
		Field[] fields = cla.getDeclaredFields();
		for(Field f : fields){
			if(f.getModifiers() == 2){
				String fieldName = f.getName();
				fieldNameMap.put(camelToUnderline(fieldName),fieldName);
			}
		}
		fieldNameMap.put("company_id","companyId");
		fieldNameMap.put("id","id");
	}
	public CustomObjectRowMapper(Class<?> cla){
		this.cla = cla;
		init(cla);
	}
	public CustomObjectRowMapper(Class<?> cla,CustomRowMapperService customRowMapperService){
		this.cla = cla;
		this.customRowMapperService = customRowMapperService;
		init(cla);
	}
	private Map<String,Integer> sqlFieldMap = new HashMap<String,Integer>();
	@Override
	public Object mapRow(ResultSet rs, int index) throws SQLException {
		if(sqlFieldMap.isEmpty()){
			ResultSetMetaData metaData =  rs.getMetaData();
			int count = metaData.getColumnCount();
			for(int i=0;i<count;i++){
				String columnName = metaData.getColumnName(i+1);
				sqlFieldMap.put(columnName.toUpperCase(),i+1);
			}
		}
		Object selObj = null;
		try {
			selObj = cla.newInstance();
		} catch (Exception e1) {
			throw new AmbFrameException("初始化对象失败!",e1);
		}
		for(String sqlFieldName : fieldNameMap.keySet()){
			if(sqlFieldMap.containsKey(sqlFieldName.toUpperCase())){
				Object val = rs.getObject(sqlFieldName);
				if(val != null){
					try {
						String fieldName = fieldNameMap.get(sqlFieldName);
						Class<?> editCla = PropertyUtils.getPropertyType(selObj,fieldName);
						if(Boolean.class.getName().equals(editCla.getName()) || boolean.class.getName().equals(editCla.getName())){
							val = Boolean.valueOf(val.toString());
						}else if(Integer.class.getName().equals(editCla.getName()) || int.class.getName().equals(editCla.getName())){
							val = Integer.valueOf(val.toString());
						}else if(Long.class.getName().equals(editCla.getName()) || long.class.getName().equals(editCla.getName())){
							val = Long.valueOf(val.toString());
						}else if(Float.class.getName().equals(editCla.getName()) || float.class.getName().equals(editCla.getName())){
							val = Float.valueOf(val.toString());
						}else if(Double.class.getName().equals(editCla.getName()) || double.class.getName().equals(editCla.getName())){
							val = Double.valueOf(val.toString());
						}else if(java.util.Date.class.getName().equals(editCla.getName())){
							if(val.getClass().getName().equals(TIMESTAMP.class.getName())){
								TIMESTAMP timestamp = (TIMESTAMP)val;
								java.sql.Date date = timestamp.dateValue();
								java.util.Date newDate = new java.util.Date();
								newDate.setTime(date.getTime());
								val = newDate;
							}
						}
						PropertyUtils.setProperty(selObj,fieldName,val);
					} catch (Exception e) {
						throw new AmbFrameException("转换对象失败!",e);
					}
				}
			}
		}
		if(customRowMapperService != null){
			customRowMapperService.customMapRow(rs, selObj);
		}
		return selObj;
	}
	
	public static final char UNDERLINE='_';  
   /**
     * 方法名: 骆驼转下划线写法
     * <p>功能说明：</p>
     * @param param
     * @return
    */
   public static String camelToUnderline(String param){  
       if (param==null||"".equals(param.trim())){  
           return "";  
       }  
       int len=param.length();  
       StringBuilder sb=new StringBuilder(len);  
       for (int i = 0; i < len; i++) {  
           char c=param.charAt(i);  
           if (Character.isUpperCase(c)){  
               sb.append(UNDERLINE);  
               sb.append(Character.toLowerCase(c));  
           }else{  
               sb.append(c);  
           }  
       }  
       return sb.toString();  
   }  
   /**
    * 方法名: 下划线转骆驼写法
    * <p>功能说明：</p>
    * @param param
    * @return
   */
   public static String underlineToCamel(String param){  
       if (param==null||"".equals(param.trim())){  
           return "";  
       }  
       int len=param.length();  
       StringBuilder sb=new StringBuilder(len);  
       for (int i = 0; i < len; i++) {  
           char c=param.charAt(i);  
           if (c==UNDERLINE){  
              if (++i<len){  
                  sb.append(Character.toUpperCase(param.charAt(i)));  
              }  
           }else{  
               sb.append(c);  
           }  
       }  
       return sb.toString();  
   }  
//   public static String underlineToCamel2(String param){  
//       if (param==null||"".equals(param.trim())){  
//           return "";  
//       }  
//       StringBuilder sb=new StringBuilder(param);  
//       Matcher mc= Pattern.compile("_").matcher(param);  
//       int i=0;  
//       while (mc.find()){  
//           int position=mc.end()-(i++);  
//           //String.valueOf(Character.toUpperCase(sb.charAt(position)));  
//           sb.replace(position-1,position+1,sb.substring(position,position+1).toUpperCase());  
//       }  
//       return sb.toString();  
//   }
}
