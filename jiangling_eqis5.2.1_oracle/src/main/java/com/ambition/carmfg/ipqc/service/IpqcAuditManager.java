package com.ambition.carmfg.ipqc.service;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.entity.IpqcAudit;
import com.ambition.carmfg.entity.IpqcAuditWarming;
import com.ambition.carmfg.entity.IpqcProblemScore;
import com.ambition.carmfg.ipqc.dao.IpqcAuditDao;
import com.ambition.carmfg.ipqc.dao.IpqcAuditWarmingDao;
import com.ambition.carmfg.ipqc.dao.IpqcWarnUserDao;
import com.ambition.supplier.utils.DateUtil;
import com.ambition.util.common.CommonUtil1;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.acs.base.web.struts2.Struts2Utils;
import com.norteksoft.mms.form.entity.ListColumn;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.ListView;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.AsyncMailUtils;
import com.norteksoft.product.util.ContextUtils;

/**
 * 
 * 类名:IPQC稽核Manager
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年8月31日 发布
 */
@Service
@Transactional
public class IpqcAuditManager {
	@Autowired
	private IpqcAuditWarmingDao ipqcAuditWarmingDao;	
	@Autowired
	private IpqcAuditDao ipqcAuditDao;
	@Autowired
	private IpqcProblemScoreManager ipqcProblemScoreManager;
	@Autowired
	private IpqcWarnUserDao ipqcWarnUserDao;	
	public IpqcAudit getIpqcAudit(Long id){
		return ipqcAuditDao.get(id);
	}
	
	public void deleteIpqcAudit(IpqcAudit ipqcAudit){
		ipqcAuditDao.delete(ipqcAudit);
	}

	public Page<IpqcAudit> search(Page<IpqcAudit>page){
		/*List<IpqcAudit> list=ipqcAuditDao.getAllIpqcAudit();
		for (IpqcAudit ipqcAudit : list) {
			ipqcAudit.setBusinessUnitName(ipqcAudit.getFactory());
			ipqcAudit.setFactory(ipqcAudit.getProcessSection());
			ipqcAudit.setIsMiss("否");
		}*/
		
		return ipqcAuditDao.search(page);
	}

	public List<IpqcAudit> listAll(){
		return ipqcAuditDao.getAllIpqcAudit();
	}
	public Page<IpqcAudit> searchMiss(Page<IpqcAudit>page){
		return ipqcAuditDao.searchMiss(page);
	}	
	public void deleteIpqcAudit(Long id){
		ipqcAuditDao.delete(id);
	}
	public String deleteIpqcAudit(String ids) {
		StringBuilder sb = new StringBuilder("");
		String[] deleteIds = ids.split(",");
		for (String id : deleteIds) {
			IpqcAudit  ipqcAudit = ipqcAuditDao.get(Long.valueOf(id));
			if(ipqcAudit.getId() != null){
				ipqcAuditDao.delete(ipqcAudit);
				sb.append(ipqcAuditDao.get(Long.valueOf(id)).getAuditDate() + ",");
			}
		}
		return sb.toString();
	}
	
	public void saveIpqcAuditMiss(IpqcAudit ipqcAudit){
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd");
		String businessUnitName=Struts2Utils.getParameter("businessUnitName");
		if (StringUtils.isEmpty(businessUnitName)) {
	      throw new RuntimeException("厂区不能为空!");
	    }
		String auditDate=Struts2Utils.getParameter("auditDate");
		if (StringUtils.isEmpty(auditDate)) {
		      throw new RuntimeException("稽核日期不能为空!");
		    }
		String factory=Struts2Utils.getParameter("factory");
		if (StringUtils.isEmpty(factory)) {
		      throw new RuntimeException("制程区段不能为空!");
		    }
		String station=Struts2Utils.getParameter("station");
	    if (StringUtils.isEmpty(station)) {
	      throw new RuntimeException("站别不能为空!");
	    }
	    String classGroup=Struts2Utils.getParameter("classGroup");
	    if (StringUtils.isEmpty(classGroup)) {
		      throw new RuntimeException("班别不能为空!");
		    }
	    String auditCount=Struts2Utils.getParameter("auditCount");
	    if (StringUtils.isEmpty(auditCount)) {
		      throw new RuntimeException("稽核件数不能为空!");
		  }
	    ipqcAudit.setBusinessUnitName(businessUnitName);
	    try {
			ipqcAudit.setAuditDate(sdf.parse(auditDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	    ipqcAudit.setFactory(factory);
	    ipqcAudit.setStation(station);
	    ipqcAudit.setClassGroup(classGroup);
	    ipqcAudit.setAuditCount(Integer.valueOf(auditCount));
	    ipqcAudit.setIsMiss("是");
	    ipqcAudit.setBadCount(0);
	    ipqcAudit.getBadCount();
	    validateAuditCount(ipqcAudit);
		ipqcAuditDao.save(ipqcAudit);
    }	
	
	  @SuppressWarnings("unused")
	private boolean validateAuditCount(Date auditDate,String businessUnitName,String factory, String station,String classGroup,Integer auditCount){
		  List<IpqcAudit> getIpqcAudits=ipqcAuditDao.getIpqcAudits(auditDate, businessUnitName, factory, station, classGroup);
		  boolean flag=true;
		  for (IpqcAudit ipqcAudit : getIpqcAudits) {
			  if(auditCount==null){
				  auditCount= ipqcAudit.getAuditCount();
			  }else{
				  if(auditCount-ipqcAudit.getAuditCount()!=0){
					  flag=false;
					  break;
				  }
			  }			 
		  }
	    return flag;
	  }
	  private void validateAuditCount(IpqcAudit ipqcAudit){
		  List<IpqcAudit> getIpqcAudits=ipqcAuditDao.getIpqcAudits(ipqcAudit.getAuditDate(), ipqcAudit.getBusinessUnitName(), ipqcAudit.getFactory(), ipqcAudit.getStation(), ipqcAudit.getClassGroup());
		  for (IpqcAudit ipqc : getIpqcAudits) {
			  ipqc.setAuditCount(ipqcAudit.getAuditCount());
		  }
	  }	 	
	public void saveIpqcAudit(IpqcAudit ipqcAudit){
			if (StringUtils.isEmpty(ipqcAudit.getBusinessUnitName())) {
		      throw new RuntimeException("厂区不能为空!");
		    }
			if (StringUtils.isEmpty(ipqcAudit.getFactory())) {
			      throw new RuntimeException("制程区段不能为空!");
			    }
			if (ipqcAudit.getAuditDate()==null) {
			      throw new RuntimeException("稽核日期不能为空!");
			    }
		    if (StringUtils.isEmpty(ipqcAudit.getStation())) {
		      throw new RuntimeException("站别不能为空!");
		    }
		    if (StringUtils.isEmpty(ipqcAudit.getProblemDegree())) {
		      throw new RuntimeException("问题严重度不能为空!");
		    }
		    if (StringUtils.isEmpty(ipqcAudit.getMissingItems())) {
		      throw new RuntimeException("缺失项目不能为空!");
		    }
		    //根据问题严重度获取问题分数
		    IpqcProblemScore ipqcProblemScore=ipqcProblemScoreManager.serach(ipqcAudit.getProblemDegree());
		    if(ipqcProblemScore!=null){
		    	ipqcAudit.setProblemScore(ipqcProblemScore.getProblemScore());
		    }		    
		    if (StringUtils.isEmpty(ipqcAudit.getAuditType())) {
		      throw new RuntimeException("稽核类别不能为空!");
		    }
		    if (ipqcAudit.getAuditCount()==null) {
			      throw new RuntimeException("稽核件数不能为空!");
			 }
		    validateAuditCount(ipqcAudit);
		    
			ipqcAuditDao.save(ipqcAudit);
			//稽核预警
			//auditWarming(ipqcAudit);
	}
	
	public void auditWarming(IpqcAudit ipqcAudit) {
		IpqcAuditWarming ipqcAuditWarming=ipqcAuditWarmingDao.serachWarming(ipqcAudit.getBusinessUnitName(), ipqcAudit.getStation(), ipqcAudit.getProblemDegree(), ipqcAudit.getMissingItems());
		String warmingCycle=null;
		if(ipqcAuditWarming!=null){
			warmingCycle=ipqcAuditWarming.getWarmingCycle();
			String hql = "select count(*) from IpqcAudit d where d.companyId = ? and d.businessUnitName = ? and d.station =? and d.problemDegree =? and d.missingItems =? ";
			    List<Object> params = new ArrayList<Object>();
			    params.add(ContextUtils.getCompanyId());
			    params.add(ipqcAudit.getBusinessUnitName());
			    params.add(ipqcAudit.getStation());
			    params.add(ipqcAudit.getProblemDegree());
			    params.add(ipqcAudit.getMissingItems());
			    Date startDate=ipqcAudit.getAuditDate();
			    Calendar calendar = Calendar.getInstance(); //得到日历
			    calendar.setTime(startDate);//把当前时间赋给日历
			if(warmingCycle!=null&&warmingCycle.equals("日")){
				hql = hql + "and d.auditDate=? ";
				 params.add(ipqcAudit.getActualDate());
			}
			if(warmingCycle!=null&&warmingCycle.equals("周")){
				hql = hql + "and  d.auditDate between ? and ? ";
				calendar.add(Calendar.DAY_OF_MONTH, -7);  //设置为前beforeNum天
				params.add(calendar.getTime());
				params.add(ipqcAudit.getAuditDate());
			}
			if(warmingCycle!=null&&warmingCycle.equals("月")){
				hql = hql + "and  d.auditDate between ? and ? ";
				calendar.add(Calendar.DAY_OF_MONTH, -30);  //设置为前beforeNum天
				params.add(calendar.getTime());
				params.add(ipqcAudit.getAuditDate());
			}
		    Query query = this.ipqcAuditDao.getSession().createQuery(hql);
		    for (int i = 0; i < params.size(); i++) {
		      query.setParameter(i, params.get(i));
		    }
		
		    List<?> list = query.list();
		    String message = "";
		    Integer repeatCount=ipqcAuditWarming.getRepeatCount();
		    String warmingManLogin=ipqcAuditWarming.getWarmingManLogin();
		    if (repeatCount!=null&&Integer.valueOf(list.get(0).toString()).intValue() >=repeatCount){
		    	String a[]=warmingManLogin.split(",");
		    	for (int i = 0; i < a.length; i++) {
					String userId=a[i].toString();
					//发送邮件
					message = "*最新预警信息:【"+ipqcAudit.getBusinessUnitName()+"】事业部、【"+ipqcAudit.getStation()+"】工站、问题严重度为【"+ipqcAudit.getProblemDegree()+"】的缺失项目【"+ipqcAudit.getMissingItems()+"】出现频率已超过预警线，请注意！！！" ;
					String email = ApiFactory.getAcsService().getUserByLoginName(userId).getEmail();
					if(StringUtils.isNotEmpty(email)){
						AsyncMailUtils.sendMail(email,"制造过程异常预警",message);
					}
				}
		    }	
		}
	}	
	
	/**
	 * 方法名: saveGsmMailSettings 
	 * <p>功能说明：保存检定邮件提醒规则</p>
	 * @return void
	 * @throws
	 */
	public String saveGsmMailSettings(){
		String message = "";
		Date myDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
		String dateStr = sdf.format(myDate);
		List<IpqcAudit> ipqcAudits =  new ArrayList<IpqcAudit>();
			String[] ids = Struts2Utils.getParameter("ids").split(",");
			for(String id:ids){
				if(StringUtils.isNotEmpty(id)){
					ipqcAudits.add(ipqcAuditDao.get(Long.valueOf(id)));
				}
			}
		for(IpqcAudit ipqcAudit :ipqcAudits){			
			if(Struts2Utils.getParameter("personStrs")!=null && !Struts2Utils.getParameter("personStrs").equals("")){
				String personStrs = Struts2Utils.getParameter("personStrs");
				JSONArray personArray = null;
				if(StringUtils.isNotEmpty(personStrs)){
					personArray = JSONArray.fromObject(personStrs);											
						for(int i = 0;i<personArray.size();i++){
							//String name=personArray.getJSONObject(i).get("name").toString();
							String userId=personArray.getJSONObject(i).get("userId").toString();
							//发送邮件
							message = "*"+dateStr+"最新预警信息:"+ipqcAudit.getBusinessUnitName()+"事业部"+ipqcAudit.getAuditDate()+"IPQC稽核问题信息已录入，问题严重度："+ipqcAudit.getProblemDegree()+",稽核员："+ipqcAudit.getAuditMan() ;
							String email = ApiFactory.getAcsService().getUserByLoginName(userId).getEmail();
							if(StringUtils.isNotEmpty(email)){
								AsyncMailUtils.sendMail(email,"制造过程异常预警",message);
							}
						}
					}else{
						ipqcAudit.getIpqcWarnUsers().clear();
					}
				}
			}							
		return null;
	}
	public String importDatas(File file) throws Exception{
		StringBuffer sb = new StringBuffer("");
		//表单字段
		Map<String,String> fieldMap = this.getFieldMap("MFG_IPQC_AUDIT");
		Workbook book = WorkbookFactory.create(new FileInputStream(file));
		
		Sheet sheet = book.getSheetAt(0);
		Row row = sheet.getRow(0);
		if(row == null){
			throw new RuntimeException("第一行不能为空!");
		}
		
		Map<String,Integer> columnMap = new HashMap<String,Integer>();
		for(int i=0;;i++){
			Cell cell = row.getCell(i);
			if(cell==null){
				break;
			}
			String value = cell.getStringCellValue();
			if(fieldMap.containsKey(value)){
				columnMap.put(value,i);
			}
		}
		/*if(columnMap.keySet().size() != fieldMap.keySet().size()){
			throw new AmbFrameException("Excel格式不正确!请重新导出台账数据模板!");
		}*/
		
		DecimalFormat df = new DecimalFormat("#.##");
		Iterator<Row> rows = sheet.rowIterator();
		rows.next();//标题行
		int i = 0;
		Map<String,Integer> map=new HashMap<String, Integer>();
		List<IpqcAudit> list= new ArrayList<IpqcAudit>();
		boolean flag=true;
		while(rows.hasNext()){
			row = rows.next();
			try {
				Map<String,Object> objMap = new HashMap<String, Object>();
				for(String columnName : columnMap.keySet()){
					Cell cell = row.getCell(columnMap.get(columnName));
					if(cell != null){
						Object value = null;
						if(Cell.CELL_TYPE_STRING == cell.getCellType()){
							value = cell.getStringCellValue();
						}else if(Cell.CELL_TYPE_NUMERIC == cell.getCellType()){
							if (HSSFDateUtil.isCellDateFormatted(cell)) {
								value = cell.getDateCellValue();
							} else {
								value = df.format(cell.getNumericCellValue());
							}
						}else if(Cell.CELL_TYPE_FORMULA == cell.getCellType()){
							value = cell.getCellFormula();
						}
						objMap.put(fieldMap.get(columnName),value);
					}
				}
				IpqcAudit ipqcAudit = new IpqcAudit();
				ipqcAudit.setCompanyId(ContextUtils.getCompanyId());
				ipqcAudit.setCreatedTime(new Date());
				ipqcAudit.setCreator(ContextUtils.getUserName());
				ipqcAudit.setModifiedTime(new Date());
				ipqcAudit.setModifier(ContextUtils.getUserName());
				for(String key : objMap.keySet()){
					CommonUtil1.setProperty(ipqcAudit,key, objMap.get(key));
				}
				//根据问题严重度获取问题分数
				if(ipqcAudit.getProblemDegree()!=null&&!"".equals(ipqcAudit.getProblemDegree())){
					IpqcProblemScore ipqcProblemScore=ipqcProblemScoreManager.serach(ipqcAudit.getProblemDegree());
				    if(ipqcProblemScore!=null){
				    	ipqcAudit.setProblemScore(ipqcProblemScore.getProblemScore());
				    }
				}
				if(ipqcAudit.getAuditDate()==null){
					throw new AmbFrameException("日期不能为空！");
				}
				if(ipqcAudit.getFactory()==null||"".equals(ipqcAudit.getFactory())){
					throw new AmbFrameException("工厂不能为空！");
				}
				if(ipqcAudit.getStation()==null||"".equals(ipqcAudit.getStation())){
					throw new AmbFrameException("工序不能为空！");
				}
				if(ipqcAudit.getClassGroup()==null||"".equals(ipqcAudit.getClassGroup())){
					throw new AmbFrameException("班别不能为空！");
				}
				if(ipqcAudit.getAuditCount()==null||"".equals(ipqcAudit.getAuditCount())){
					throw new AmbFrameException("稽核件数不能为空！");
				}
				String str=ipqcAudit.getAuditDate()+"_"+ipqcAudit.getBusinessUnitName()+"_"+ipqcAudit.getFactory()+"_"+ipqcAudit.getStation()+"_"+ipqcAudit.getClassGroup();
				if(map.containsKey(str)){
					if(ipqcAudit.getAuditCount()-map.get(str)!=0){
						throw new AmbFrameException("同日期、厂区、制程区段、站别、班别的稽核件数必须相同！");
					}
				}else{
					map.put(str, ipqcAudit.getAuditCount());
				}
				list.add(ipqcAudit);
			} catch (Exception e) {
				flag=false;
				e.printStackTrace();
				sb.append("第" + (i+1) + "行导入失败:<font color=red>" + e.getMessage() + "</font><br/>");
			}
			i++;
		}
		if(flag){
			int j=2;
			for (IpqcAudit ipqcAudit : list) {
				sb.append("第" + j+ "行导入成功!<br/>");
				ipqcAuditDao.save(ipqcAudit);
				validateAuditCount(ipqcAudit);
				//auditWarming(ipqcAudit);
				j++;
			}
		}
		file.delete();
		return sb.toString();
	}
	/**
	  * 方法名:获取字段映射 
	  * <p>功能说明：</p>
	  * @return
	 */
	public Map<String,String> getFieldMap(String listCode){
		Map<String,String> fieldMap = new HashMap<String, String>();
		ListView columns = ApiFactory.getMmsService().getListViewByCode(listCode);
		for(ListColumn column: columns.getColumns()){
			if(column.getVisible()){
				fieldMap.put(column.getHeaderName(), column.getTableColumn().getName());
			}
		}
		return fieldMap;
	}
	
	/**
	 * 转换json
	 * @param params
	 * @return
	 */
	private JSONObject convertJsonObject(JSONObject params){
		JSONObject resultJson = new JSONObject();
		for(Object key : params.keySet()){
			resultJson.put(key,params.getJSONArray(key.toString()).get(0));
		}
		return resultJson;
	}
	
	public Map<String,Object> getObserveRateDatas(JSONObject params) throws Exception{
		params = convertJsonObject(params);
		String businessUnitName="";
		String factory="";
		String station="";
		String classGroup="";
		String auditType="";
		String department="";
		String auditMan="";
		String group=params.get("group").toString();
		if(params.containsKey("businessUnitName")){
			businessUnitName=params.get("businessUnitName").toString();
		}
		if(params.containsKey("factory")){
			factory=params.get("factory").toString();
		}	
		if(params.containsKey("station")){
			station=params.get("station").toString();
		}
		if(params.containsKey("classGroup")){
			classGroup=params.get("classGroup").toString();
		}
		if(params.containsKey("auditType")){
			auditType=params.get("auditType").toString();
		}
		if(params.containsKey("department")){
			department=params.get("department").toString();
		}
		if(params.containsKey("auditMan")){
			auditMan=params.get("auditMan").toString();
		}
		Map<String,Object> result = new HashMap<String, Object>();		
		Date startDate = DateUtil.parseDate(params.getString("startDate"));
		Date endDate = DateUtil.parseDate(params.getString("endDate"));		
		//表格的表头
		//List<Integer> data = new ArrayList<Integer>();
		//稽核件数
		Map<String,Object> series1 = new HashMap<String, Object>();
		series1.put("name", "稽核件数");
		List<Map<String,Object>> data1 = new ArrayList<Map<String,Object>>();
		
		//问题件数
		Map<String,Object> series2 = new HashMap<String, Object>();
		series2.put("name", "问题件数");
		List<Map<String,Object>> data2 = new ArrayList<Map<String,Object>>();		
		
		//问题件数
		Map<String,Object> series3 = new HashMap<String, Object>();
		series3.put("name", "遵守度");
		List<Map<String,Object>> data3 = new ArrayList<Map<String,Object>>();
		
		//问题分数
		Map<String,Object> series4 = new HashMap<String, Object>();
		series4.put("name", "问题分数");
		List<Map<String,Object>> data4 = new ArrayList<Map<String,Object>>();
				
		//稽核分数
		Map<String,Object> series5 = new HashMap<String, Object>();
		series5.put("name", "稽核分数");
		List<Map<String,Object>> data5 = new ArrayList<Map<String,Object>>();
		
		result.put("title", "IPQC稽核遵守度统计图");
		List<Object> categories = new ArrayList<Object>();
		result.put("categories", categories);
		result.put("yAxisTitle1","数<br/>据");
		//result.put("max", 100);
		//查询问题件数
		String dateField = "";
		String totalSql = "sum(q.bad_count) as value,sum(q.problem_score) as score";	
		if(group.equals("year")){
			dateField=" to_char(q.audit_date,'yyyy') ";
		}else if(group.equals("month")){
			dateField=" to_char(q.audit_date,'yyyy-MM') ";
		}else if(group.equals("day")){
			dateField=" to_char(q.audit_date,'yyyyMMdd') ";
		}else if(group.equals("week")){
			dateField=" to_char(q.audit_date,'yyyy_iw') ";
		}else if(group.equals("station")){
			dateField=" q.station ";
		}else if(group.equals("classGroup")){
			dateField=" q.class_group ";
		}else if(group.equals("auditType")){
			dateField=" q.audit_type ";
		}else if(group.equals("problemDegree")){
			dateField=" q.problem_degree ";
		}
		String hql = "select "+dateField+"," +totalSql+" from MFG_IPQC_AUDIT q  where  q.audit_date between ? and ? ";								
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(startDate);
		searchParams.add(endDate);
		if(!businessUnitName.equals("")){
			hql=hql+" and q.business_unit_name=? ";
			searchParams.add(businessUnitName);
		}
		if(!factory.equals("")){
			hql=hql+" and q.factory=? ";
			searchParams.add(factory);
		}
		if(!station.equals("")){
			hql=hql+" and q.station=? ";
			searchParams.add(station);
		}
		if(!classGroup.equals("")){
			hql=hql+" and q.class_group=? ";
			searchParams.add(classGroup);
		}
		if(!auditType.equals("")){
			hql=hql+" and q.audit_type=? ";
			searchParams.add(auditType);
		}
		if(!department.equals("")){
			hql=hql+" and q.department like ? ";
			searchParams.add("%"+department+"%");
		}
		if(!auditMan.equals("")){
			hql=hql+" and q.audit_man like ? ";
			searchParams.add("%"+auditMan+"%");
		}
		hql += " group by " + dateField + "  order by " + dateField+" ";
		List<?> list =null;
		try {
			list= ipqcAuditDao.findBySql(hql,searchParams.toArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		for(Object obj : list){
			
			Object objs[] = (Object[])obj;
			Map<String,Object> map = new HashMap<String, Object>();			
			String groupBy=null;
			if(objs[0]!=null){
				groupBy=objs[0].toString();
			}
			categories.add(groupBy);
			//问题件数
			String auditAmount=null;
			if(objs[1]!=null){
				auditAmount=objs[1].toString();
			}
			map.put("name","问题件数");
			map.put("xvalue",groupBy);
			map.put("y", auditAmount);
			data2.add(map);
			
			
			//问题分数
			String problemScore=null;
			if(objs[2]!=null){
				problemScore=objs[2].toString();
			}
			map = new HashMap<String, Object>();
			map.put("name","问题分数");
			map.put("xvalue",groupBy);
			map.put("y", problemScore);
			data4.add(map);
			
			
			//稽核件数
			String totalhql = "select q from IpqcAudit q  where  q.auditDate between ? and ? ";
			List<Object> totalsearchParams = new ArrayList<Object>();
			totalsearchParams.add(startDate);
			totalsearchParams.add(endDate);
			if(!businessUnitName.equals("")){
				totalhql=totalhql+" and q.businessUnitName=? ";
				totalsearchParams.add(businessUnitName);
			}
			if(!factory.equals("")){
				totalhql=totalhql+" and q.factory=? ";
				totalsearchParams.add(factory);
			}
			if(!station.equals("")){
				totalhql=totalhql+" and q.station=? ";
				totalsearchParams.add(station);
			}
			if(!classGroup.equals("")){
				totalhql=totalhql+" and q.classGroup=? ";
				totalsearchParams.add(classGroup);
			}
			if(!auditType.equals("")){
				totalhql=totalhql+" and q.auditType=? ";
				totalsearchParams.add(auditType);
			}
			if(!department.equals("")){
				totalhql=totalhql+" and q.department like ? ";
				totalsearchParams.add("%"+department+"%");
			}
			if(!auditMan.equals("")){
				totalhql=totalhql+" and q.auditMan like ? ";
				totalsearchParams.add("%"+auditMan+"%");
			}
			if(group.equals("year")){
				totalhql=totalhql+" and to_char(q.auditDate,'yyyy')=? ";				
			}else if(group.equals("month")){
				totalhql=totalhql+" and to_char(q.auditDate,'yyyy-MM')=? ";
			}else if(group.equals("day")){
				totalhql=totalhql+" and to_char(q.auditDate,'yyyyMMdd')=? ";
			}else if(group.equals("week")){
				totalhql=totalhql+" and to_char(q.auditDate,'yyyy_iw')=? ";
			}else if(group.equals("station")){
				totalhql=totalhql+" and q.station=? ";
			}else if(group.equals("classGroup")){
				totalhql=totalhql+" and q.classGroup=? ";
			}else if(group.equals("auditType")){
				totalhql=totalhql+" and q.auditType=? ";
			}else if(group.equals("problemDegree")){
				totalhql=totalhql+" and q.problemDegree=? ";
			}			
			totalsearchParams.add(groupBy);			
			List<IpqcAudit> totallist =null;
			try {
				totallist = ipqcAuditDao.find(totalhql,totalsearchParams.toArray());
			} catch (Exception e) {
				e.printStackTrace();
			}
			int totalAmount=0,totalScore=0;
			Map<String, Integer> totalmap=new HashMap<String, Integer>();
			Map<String, Integer> totalScoreMap=new HashMap<String, Integer>();
			for (IpqcAudit ipqcAudit : totallist) {
				String str=ipqcAudit.getAuditDate()+"-"+ipqcAudit.getBusinessUnitName()+"-"+ipqcAudit.getFactory()+"-"+ipqcAudit.getStation()+"-"+ipqcAudit.getClassGroup();
				int count=0,score=0;
				if(ipqcAudit.getAuditCount()!=null){
					count=ipqcAudit.getAuditCount();
				}
				if(ipqcAudit.getAuditScore()!=null){
					score=ipqcAudit.getAuditScore();
				}
				if(!totalmap.containsKey(str)){					
					totalmap.put(str,count );
				}
				if(!totalScoreMap.containsKey(str)){					
					totalScoreMap.put(str,score );
				}
			}
			for (Integer count : totalmap.values()) {
				totalAmount+=count;
			}
			for (Integer score : totalScoreMap.values()) {
				totalScore+=score;
			}
			map = new HashMap<String, Object>();
			map.put("name","稽核件数");
			map.put("xvalue",groupBy);
			map.put("y", totalAmount);
			data1.add(map);
			
			map = new HashMap<String, Object>();
			map.put("name","稽核分数");
			map.put("xvalue",groupBy);
			if(totalScore==0){
				map.put("y", "");
			}else{
				map.put("y", totalScore);	
			}			
			data5.add(map);
			
			//遵守度
			map=new HashMap<String, Object>();
			DecimalFormat   decimalFormat   =new   DecimalFormat("0.00");
			String 	observeRate=null;
			if(totalAmount==0){
				observeRate="0.00";
			}else if(Integer.valueOf(auditAmount)==0){
				observeRate="100.00";
			}else if(totalAmount<Integer.valueOf(auditAmount)){
				observeRate="0.00";
			}else{
				observeRate=decimalFormat.format((float)((totalAmount-Integer.valueOf(auditAmount))*100)/(float)totalAmount);					
			}						
			map.put("name","遵守度");			
			map.put("y", observeRate);
			data3.add(map);
			
		}
		result.put("tableHeaderList", categories);
		result.put("firstColName","数据");
		result.put("yAxisTitle1","件<br/>数");
		result.put("yAxisTitle2","遵<br/>守<br/>度");
		series1.put("data",data1);
		result.put("series1", series1);
		//result.put("count", amount);
		series2.put("data",data2);
		result.put("series2", series2);
		
		series3.put("data",data3);
		result.put("series3", series3);

		series4.put("data",data4);
		result.put("series4", series4);
		
		series5.put("data",data5);
		result.put("series5", series5);
				
		//result.put("max", 100);
		return result;	
	}

	public Page<IpqcAudit> listDetail(Page<IpqcAudit> page, String params) throws ParseException {
		String  startDateStr = params.split(",")[0];
		String  endDateStr = params.split(",")[1];
		String businessUnitName=params.split(",")[2];
		String factory=params.split(",")[3];
		String station=params.split(",")[4];
		String classGroup=params.split(",")[5];
		String auditType=params.split(",")[6];
		String department=params.split(",")[7];
		String auditMan=params.split(",")[8];
		String name=params.split(",")[9];
		String group=params.split(",")[10];
		String groupValue=params.split(",")[11];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = sdf.parse(startDateStr);
		Date endDate = sdf.parse(endDateStr);
		return ipqcAuditDao.listDetail(page, businessUnitName,factory,station,classGroup,auditType,department,auditMan,name,group,groupValue,startDate,endDate);
	}		
	
}
