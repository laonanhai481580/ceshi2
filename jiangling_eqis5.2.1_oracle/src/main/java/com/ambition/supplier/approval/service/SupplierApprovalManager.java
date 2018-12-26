package com.ambition.supplier.approval.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.supplier.approval.dao.SupplierApprovalDao;
import com.ambition.supplier.entity.SupplierApproval;
import com.norteksoft.acs.base.enumeration.MailboxDeploy;
import com.norteksoft.acs.entity.authorization.Role;
import com.norteksoft.acs.entity.authorization.RoleUser;
import com.norteksoft.acs.entity.organization.Company;
import com.norteksoft.acs.entity.organization.Department;
import com.norteksoft.acs.entity.organization.DepartmentUser;
import com.norteksoft.acs.entity.organization.User;
import com.norteksoft.acs.entity.organization.UserInfo;
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
public class SupplierApprovalManager extends AmbWorkflowManagerBase<SupplierApproval>{
	Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private SupplierApprovalDao supplierApprovalDao;
	@Override
	public Class<SupplierApproval> getEntityInstanceClass() {
		return SupplierApproval.class;
	}

	@Override
	public String getEntityListCode() {
		return SupplierApproval.ENTITY_LIST_CODE;
	}

	@Override
	public HibernateDao<SupplierApproval, Long> getHibernateDao() {
		return supplierApprovalDao;
	}

	@Override
	public String getWorkflowDefinitionCode() {
		return "supplier_supplier_approval";
	}

	@Override
	public String getWorkflowDefinitionName() {
		return "材料承认管理流程";
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
			SupplierApproval report=getEntity(Long.valueOf(id));
			deleteEntity(report);
			sb.append(report.getFormNo() + ",");
		}
		message =sb.toString();
		return   message;
	}
	public Department searchSupplierDept() {
		// TODO Auto-generated method stub
		String hql = "from Department d where  d.deleted=? and d.name=?";
		@SuppressWarnings("unchecked")
		List<Department> depts = supplierApprovalDao.getSession()
				.createQuery(hql).setParameter(0, false).setParameter(1, "供应商").list();
		if(depts.size()==0){
			return null;
		}else{
			return depts.get(0);
		}
	}
	@SuppressWarnings("unchecked")
	public void saveUser(SupplierApproval report, Department department) {
		// TODO Auto-generated method stub
		Company company = null;
		// 取到第一个公司
		if (company == null) {
			String hql = "from Company c";
			List<?> list = supplierApprovalDao.getSession().createQuery(hql).list();
			company = (Company) list.get(0);
		}
		List<Role> defaultRoles = new ArrayList<Role>();
		String rolehql = "from Role r where r.deleted = ? and r.name = ?";
		defaultRoles = supplierApprovalDao.getSession().createQuery(rolehql).setParameter(0, false).setParameter(1, "普通用户").list();
		String hql = "select user from User user where user.deleted=false and user.loginName=?";
		List<?> users = supplierApprovalDao.getSession().createQuery(hql).setParameter(0, report.getSupplierLoginName())
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
			supplierApprovalDao.getSession().save(user);
		}else {
			user = (User) users.get(0);
		}
		if (isNew) {
			// 判断用户角色是否存在,RoleUser的在user,role建立了索引，不能重复
			for(Role role : defaultRoles){
				hql = "from RoleUser r where r.user = ? and r.role = ? ";
				List<?> roleUserList = supplierApprovalDao.getSession().createQuery(hql).setParameter(0, user).setParameter(1, role).list();
				if (roleUserList.isEmpty()) {
					RoleUser roleUser = new RoleUser();
					roleUser.setAllUser("所有用户");
					roleUser.setCompanyId(company.getId());
					roleUser.setDeleted(false);
					roleUser.setRole(role);
					roleUser.setUser(user);
					roleUser.setTs(new Date());
					supplierApprovalDao.getSession().save(roleUser);
				}
			}
		}
		// 添加兼职部门
		List<?> departmentUsers = new ArrayList<DepartmentUser>();
		if (department != null) {
			hql = "from DepartmentUser d where d.user = ? and d.department = ?";
			departmentUsers = supplierApprovalDao.getSession().createQuery(hql).setParameter(0, user)
					.setParameter(1, department).list();
		} else {
			hql = "from DepartmentUser d where d.user = ? and d.department is null";
			departmentUsers = supplierApprovalDao.getSession().createQuery(hql).setParameter(0, user)
					.list();
		}
		if (departmentUsers.size() <= 0) {
			DepartmentUser departmentUser = new DepartmentUser();
			departmentUser.setCompanyId(company.getId());
			departmentUser.setUser(user);
			departmentUser.setDepartment(department);
			departmentUser.setDeleted(false);
			supplierApprovalDao.getSession().save(departmentUser);
		}
		// 保存用户基础信息
		hql = "from UserInfo u where u.user.id=?";
		List<UserInfo> usersInfos = supplierApprovalDao.getSession().createQuery(hql)
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
		supplierApprovalDao.getSession().save(userInfo);
	}
	public Page<SupplierApproval> listState(Page<SupplierApproval> page,String code){
		String hql = " from SupplierApproval e where e.companyId=?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		if(code!=null ){
			hql=hql+" and e.supplierCode=?";
			searchParams.add(code);
		}
		return supplierApprovalDao.searchPageByHql(page, hql, searchParams.toArray());
	}
	public String getMaterialsFile(String name){
		String sql= "select materials_File from SUPPLIER_MATERIALS_FILE t where t.file_Name =?";
		Query query = supplierApprovalDao.getSession().createSQLQuery(sql).setParameter(0, name);
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
