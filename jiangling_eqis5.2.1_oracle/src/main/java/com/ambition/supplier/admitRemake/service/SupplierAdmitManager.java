package com.ambition.supplier.admitRemake.service;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
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

import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.supplier.admitRemake.dao.SupplierAdmitDao;
import com.ambition.supplier.entity.SupplierAdmit;
import com.ambition.util.common.CommonUtil;
import com.norteksoft.acs.base.enumeration.MailboxDeploy;
import com.norteksoft.acs.entity.authorization.Role;
import com.norteksoft.acs.entity.authorization.RoleUser;
import com.norteksoft.acs.entity.organization.Company;
import com.norteksoft.acs.entity.organization.Department;
import com.norteksoft.acs.entity.organization.DepartmentUser;
import com.norteksoft.acs.entity.organization.User;
import com.norteksoft.acs.entity.organization.UserInfo;
import com.norteksoft.mms.form.entity.ListColumn;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.ListView;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.AsyncMailUtils;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.Md5;
/**
 * 
 * 类名:材料承认Services
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  Janam
 * @version 1.00 2017年9月11日 发布
 */
@Service
@Transactional
public class SupplierAdmitManager extends AmbWorkflowManagerBase<SupplierAdmit>{
	Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private SupplierAdmitDao supplierAdmitDao;
	@Override
	public Class<SupplierAdmit> getEntityInstanceClass() {
		return SupplierAdmit.class;
	}

	@Override
	public String getEntityListCode() {
		return SupplierAdmit.ENTITY_LIST_CODE;
	}

	@Override
	public HibernateDao<SupplierAdmit, Long> getHibernateDao() {
		return supplierAdmitDao;
	}

	@Override
	public String getWorkflowDefinitionCode() {
		return "supplier_supplier_admit";
	}

	@Override
	public String getWorkflowDefinitionName() {
		return "材料承认New管理流程";
	}
	/**
	 * 删除实体，流程相关文件都删除
	 * @param ids
	 */
	public String deleteEntity(String ids) {
		String[] deleteIds = ids.split(",");
		StringBuilder sb = new StringBuilder("");
	     String message = "";
		for (String id : deleteIds) {
			SupplierAdmit report=getEntity(Long.valueOf(id));
			deleteEntity(report);
			sb.append(report.getFormNo() + ",");
		}
		message =sb.toString();
		return   message;
	}
	public void deleteEntity(SupplierAdmit entity){
		if(entity.getWorkflowInfo()!=null){
			String workflowId =  entity.getWorkflowInfo().getWorkflowId();
			//先删除子表
			Long reportId = entity.getId();
			String sql31 = "delete from SUPPLIER_ADMIT_FILE where SUPPLIER_SUBLIST_ID = ?";	
			getHibernateDao().getSession().createSQLQuery(sql31).setParameter(0,reportId).executeUpdate();
			String sql = "delete from product_task_all_his where execution_id = ?";
			getHibernateDao().getSession().createSQLQuery(sql).setParameter(0,workflowId).executeUpdate();
			getHibernateDao().delete(entity);
			ApiFactory.getInstanceService().deleteInstance(entity);
		}else{
			getHibernateDao().delete(entity);
		}
	}
	public Department searchSupplierDept() {
		// TODO Auto-generated method stub
		String hql = "from Department d where  d.deleted=? and d.name=?";
		@SuppressWarnings("unchecked")
		List<Department> depts = supplierAdmitDao.getSession()
				.createQuery(hql).setParameter(0, false).setParameter(1, "供应商").list();
		if(depts.size()==0){
			return null;
		}else{
			return depts.get(0);
		}
	}
	@SuppressWarnings("unchecked")
	public void saveUser(SupplierAdmit report, Department department) {
		// TODO Auto-generated method stub
		Company company = null;
		// 取到第一个公司
		if (company == null) {
			String hql = "from Company c";
			List<?> list = supplierAdmitDao.getSession().createQuery(hql).list();
			company = (Company) list.get(0);
		}
		List<Role> defaultRoles = new ArrayList<Role>();
		String rolehql = "from Role r where r.deleted = ? and r.name = ?";
		defaultRoles = supplierAdmitDao.getSession().createQuery(rolehql).setParameter(0, false).setParameter(1, "普通用户").list();
		String hql = "select user from User user where user.deleted=false and user.loginName=?";
		List<?> users = supplierAdmitDao.getSession().createQuery(hql).setParameter(0, report.getSupplierLoginName())
				.list();
		User user = new User();
		boolean isNew = false;
		if (users.size() <= 0) {
			user = new User();
			user.setCompanyId(company.getId());
			isNew = true;
			user.setLoginName(report.getSupplierLoginName().trim());
			user.setName(report.getSupplierName());
			user.setPassword(Md5.toMessageDigest(report.getSupplierLoginName().trim()));
			if(StringUtils.isNotEmpty(report.getSupplierEmail())){
				user.setEmail(report.getSupplierEmail());
			}
			user.setMailSize(10.0f);
			user.setHonorificName("");
			user.setMainDepartmentId(department == null ? null : department.getId());
			user.setMainDepartmentName(department == null ? null : department
					.getName());
			user.setMailboxDeploy(MailboxDeploy.INSIDE);
			supplierAdmitDao.getSession().save(user);
		}else {
			user = (User) users.get(0);
		}
		if (isNew) {
			// 判断用户角色是否存在,RoleUser的在user,role建立了索引，不能重复
			for(Role role : defaultRoles){
				hql = "from RoleUser r where r.user = ? and r.role = ? ";
				List<?> roleUserList = supplierAdmitDao.getSession().createQuery(hql).setParameter(0, user).setParameter(1, role).list();
				if (roleUserList.isEmpty()) {
					RoleUser roleUser = new RoleUser();
					roleUser.setAllUser("所有用户");
					roleUser.setCompanyId(company.getId());
					roleUser.setDeleted(false);
					roleUser.setRole(role);
					roleUser.setUser(user);
					roleUser.setTs(new Date());
					supplierAdmitDao.getSession().save(roleUser);
				}
			}
		}
		// 添加兼职部门
		List<?> departmentUsers = new ArrayList<DepartmentUser>();
		if (department != null) {
			hql = "from DepartmentUser d where d.user = ? and d.department = ?";
			departmentUsers = supplierAdmitDao.getSession().createQuery(hql).setParameter(0, user)
					.setParameter(1, department).list();
		} else {
			hql = "from DepartmentUser d where d.user = ? and d.department is null";
			departmentUsers = supplierAdmitDao.getSession().createQuery(hql).setParameter(0, user)
					.list();
		}
		if (departmentUsers.size() <= 0) {
			DepartmentUser departmentUser = new DepartmentUser();
			departmentUser.setCompanyId(company.getId());
			departmentUser.setUser(user);
			departmentUser.setDepartment(department);
			departmentUser.setDeleted(false);
			supplierAdmitDao.getSession().save(departmentUser);
		}
		// 保存用户基础信息
		hql = "from UserInfo u where u.user.id=?";
		List<UserInfo> usersInfos = supplierAdmitDao.getSession().createQuery(hql)
				.setParameter(0, user.getId()).list();
		UserInfo userInfo = new UserInfo();
		if (usersInfos.size() > 0) {
			userInfo = usersInfos.get(0);
		} else {
			userInfo = new UserInfo();
		}
		// userInfo.setTelephone(telephone);
		userInfo.setUser(user);
		userInfo.setCompanyId(user.getCompanyId());
		userInfo.setBirthday("");
		userInfo.setBloodGroup("");
		userInfo.setCityArea("");
		userInfo.setTelephone("");
		userInfo.setDegree("");
		userInfo.setEducationGrade("");
		userInfo.setFatherName("");
		userInfo.setFirstForeignLanguage("");
		userInfo.setGraduatedSchool("");
		userInfo.setPasswordUpdatedTime(new Date());
		userInfo.setTreatment("");
		// userInfo.setWeight("");
		supplierAdmitDao.getSession().save(userInfo);
	}
	public Page<SupplierAdmit> listState(Page<SupplierAdmit> page,String code,String state){
		String hql = " from SupplierAdmit e where  e.companyId=?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		if(code!=null ){
			hql=hql+" and e.supplierCode=?";
			searchParams.add(code);
		}
		if(state!=null ){
			hql=hql+" and e.adminState=?";
			searchParams.add(state);
		}
		return supplierAdmitDao.searchPageByHql(page, hql, searchParams.toArray());
	}
	public String getMaterialsFile(String name){
		String sql= "select materials_File from SUPPLIER_MATERIALS_FILE t where t.file_Name =?";
		Query query = supplierAdmitDao.getSession().createSQLQuery(sql).setParameter(0, name);
		List<?> list = query.list();
		if(!list.isEmpty() && list.get(0)!=null){
			String materialsFile = list.get(0).toString();
			String file = getStringFormat(materialsFile);
			return file;
		}
		return null;
	}
	public String getStringFormat(String str){
		int index = str.indexOf("|");
		index = str.indexOf("|", index+1);
		String date1=str.substring(index+1);
		return date1;
	}
	
	public SupplierAdmit getSupplierAdmit(String supplierCode,String checkBomCode){
		String hql = "from SupplierAdmit s where  s.materialCode = ? and s.supplierCode=? order by s.id desc ";
		List<SupplierAdmit> supplierAdmits = supplierAdmitDao.find(hql,checkBomCode,supplierCode);
		if(supplierAdmits.isEmpty()){
			return null;
		}else{
			return supplierAdmits.get(0);
		}
	}	
	public String importDatas(File file) throws Exception{
		StringBuffer sb = new StringBuffer("");
		//表单字段
		Map<String,String> fieldMap = this.getFieldMap("SUPPLIER_SUPPLIER_ADMIT");
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
				SupplierAdmit supplierAdmit = null;
				String materialCode=(String)objMap.get("materialCode");
				String supplierCode=(String)objMap.get("supplierCode");
				if(materialCode==null||supplierCode==null){
					throw new RuntimeException("供应商编码或者物料编码为空!");
				}
				List<SupplierAdmit> list=supplierAdmitDao.getSupplierAdmitMaterialCode(materialCode,supplierCode,null);
				if(list.isEmpty()){
					supplierAdmit = new SupplierAdmit();
					supplierAdmit.setCompanyId(ContextUtils.getCompanyId());
					supplierAdmit.setCreatedTime(new Date());
					supplierAdmit.setCreator(ContextUtils.getUserName());
					supplierAdmit.setModifiedTime(new Date());
					supplierAdmit.setModifier(ContextUtils.getUserName());
					supplierAdmit.setHiddenState("N");
					supplierAdmit.setSkillLeadStatus("承认");
					supplierAdmit.setAdminState("DL");
				}else{
					supplierAdmit=list.get(0);
				}
//				com.norteksoft.product.api.entity.User user= ApiFactory.getAcsService().getUserByLoginName(ContextUtils.getLoginName());
//				String subName=user.getSubCompanyName();
//				supplierAdmit.setFactoryClassify(subName);
				for(String key : objMap.keySet()){
					CommonUtil.setProperty(supplierAdmit,key, objMap.get(key));
				}
				supplierAdmitDao.save(supplierAdmit);
			   sb.append("第" + (i+1) + "行导入成功!<br/>");
			} catch (Exception e) {
				e.printStackTrace();
				sb.append("第" + (i+1) + "行导入失败:<font color=red>" + e.getMessage() + "</font><br/>");
			}
			i++;
		}
		file.delete();
		return sb.toString();
	}
	public List<SupplierAdmit> SupplierAdmitMaterialCode(String materialCode ,String supplierCode,Long id){
		return supplierAdmitDao.getSupplierAdmitMaterialCode(materialCode,supplierCode,id);
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
	@SuppressWarnings("static-access")
	public String releaseEmail(String params,String formNo,String url) throws InterruptedException{
		params=emailFormat(params);
		String[] emails = params.split(";");
		String emailContent = "您有一份任务报告待处理! 单号:"+formNo+","+url;
		for(String email:emails){
			if(StringUtils.isNotEmpty(email)){
				AsyncMailUtils.sendMail(email,"材料承认！",emailContent.toString());
				Thread.currentThread().sleep(10000);//毫秒
			}
		}
		return null;
	}
	public String emailFormat(String email) {  
		if(email.indexOf("；")!=-1){
			email=email.replaceAll("；", ";");
		} 
		if(email.indexOf(",")!=-1){
			email=email.replaceAll(",", ";");
		}
		if(email.indexOf("，")!=-1){
			email=email.replaceAll("，", ";");
		}
	       return email;
	 }
}
