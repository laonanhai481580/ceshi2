package com.ambition.supplier.materialadmit.evaluate.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.qsm.entity.AuditPlan;
import com.ambition.supplier.entity.SupplierMaterialEvaluate;
import com.ambition.supplier.materialadmit.evaluate.dao.SupplierMaterialEvaluateDao;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.acs.base.enumeration.MailboxDeploy;
import com.norteksoft.acs.entity.authorization.Role;
import com.norteksoft.acs.entity.authorization.RoleUser;
import com.norteksoft.acs.entity.organization.Company;
import com.norteksoft.acs.entity.organization.Department;
import com.norteksoft.acs.entity.organization.DepartmentUser;
import com.norteksoft.acs.entity.organization.User;
import com.norteksoft.acs.entity.organization.UserInfo;
import com.norteksoft.acs.service.AcsUtils;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.Md5;

/**
 * 类名:
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  linshaowei
 * @version 1.00 2016年10月17日 发布
 */
@Service
@Transactional
public class SupplierMaterialEvaluateManager extends AmbWorkflowManagerBase<SupplierMaterialEvaluate>{

	@Autowired
	private SupplierMaterialEvaluateDao supplierMaterialEvaluateDao;
	@Override
	public HibernateDao<SupplierMaterialEvaluate, Long> getHibernateDao() {
		// TODO Auto-generated method stub
		return supplierMaterialEvaluateDao;
	}

	@Override
	public String getEntityListCode() {
		// TODO Auto-generated method stub
		return "SUPPLIER_MATERIAL_EVALUATE";
	}

	@Override
	public Class<SupplierMaterialEvaluate> getEntityInstanceClass() {
		// TODO Auto-generated method stub
		return SupplierMaterialEvaluate.class;
	}

	@Override
	public String getWorkflowDefinitionCode() {
		// TODO Auto-generated method stub
		return "supplier-material_evaluate";
	}

	@Override
	public String getWorkflowDefinitionName() {
		// TODO Auto-generated method stub
		return "样件评估流程";
	}
	@Override
	public void exportReport(Long entityId) throws IOException{
		exportReport(entityId, "supplier-material-evaluate.xlsx", "样件评估表");
	}

	public Department searchSupplierDept() {
		// TODO Auto-generated method stub
		String hql = "from Department d where  d.deleted=? and d.name=?";
		@SuppressWarnings("unchecked")
		List<Department> depts = supplierMaterialEvaluateDao.getSession()
				.createQuery(hql).setParameter(0, false).setParameter(1, "供应商").list();
		if(depts.size()==0){
			return null;
		}else{
			return depts.get(0);
		}
	}
	@SuppressWarnings("unchecked")
	public void saveUser(SupplierMaterialEvaluate report, Department department) {
		// TODO Auto-generated method stub
		Company company = null;
		// 取到第一个公司
		if (company == null) {
			String hql = "from Company c";
			List<?> list = supplierMaterialEvaluateDao.getSession().createQuery(hql).list();
			company = (Company) list.get(0);
		}
		List<Role> defaultRoles = new ArrayList<Role>();
		String rolehql = "from Role r where r.deleted = ? and r.name = ?";
		defaultRoles = supplierMaterialEvaluateDao.getSession().createQuery(rolehql).setParameter(0, false).setParameter(1, "普通用户").list();
		String hql = "select user from User user where user.deleted=false and user.loginName=?";
		List<?> users = supplierMaterialEvaluateDao.getSession().createQuery(hql).setParameter(0, report.getSupplierCode())
				.list();
		User user = new User();
		boolean isNew = false;
		if (users.size() <= 0) {
			user = new User();
			user.setCompanyId(company.getId());
			isNew = true;
			user.setLoginName(report.getSupplierCode());
			user.setName(report.getSupplier());
			user.setPassword(Md5.toMessageDigest(report.getSupplierCode()));
			if(StringUtils.isNotEmpty(report.getSupplierEmail())){
				user.setEmail(report.getSupplierEmail().split(";")[0]);
			}
			user.setMailSize(10.0f);
			user.setHonorificName("");
			user.setMainDepartmentId(department == null ? null : department.getId());
			user.setMainDepartmentName(department == null ? null : department
					.getName());
			user.setMailboxDeploy(MailboxDeploy.INSIDE);
			supplierMaterialEvaluateDao.getSession().save(user);
		}else {
			user = (User) users.get(0);
		}
		/*if (isNew) {
			// 判断用户角色是否存在,RoleUser的在user,role建立了索引，不能重复
			for(Role role : defaultRoles){
				hql = "from RoleUser r where r.user = ? and r.role = ? ";
				List<?> roleUserList = supplierMaterialEvaluateDao.getSession().createQuery(hql).setParameter(0, user).setParameter(1, role).list();
				if (roleUserList.isEmpty()) {
					RoleUser roleUser = new RoleUser();
					roleUser.setAllUser("所有用户");
					roleUser.setCompanyId(company.getId());
					roleUser.setDeleted(false);
					roleUser.setRole(role);
					roleUser.setUser(user);
					roleUser.setTs(new Date());
					supplierMaterialEvaluateDao.getSession().save(roleUser);
				}
			}
		}*/
		// 添加兼职部门
		List<?> departmentUsers = new ArrayList<DepartmentUser>();
		if (department != null) {
			hql = "from DepartmentUser d where d.user = ? and d.department = ?";
			departmentUsers = supplierMaterialEvaluateDao.getSession().createQuery(hql).setParameter(0, user)
					.setParameter(1, department).list();
		} else {
			hql = "from DepartmentUser d where d.user = ? and d.department is null";
			departmentUsers = supplierMaterialEvaluateDao.getSession().createQuery(hql).setParameter(0, user)
					.list();
		}
		if (departmentUsers.size() <= 0) {
			DepartmentUser departmentUser = new DepartmentUser();
			departmentUser.setCompanyId(company.getId());
			departmentUser.setUser(user);
			departmentUser.setDepartment(department);
			departmentUser.setDeleted(false);
			supplierMaterialEvaluateDao.getSession().save(departmentUser);
		}
		// 保存用户基础信息
		hql = "from UserInfo u where u.user.id=?";
		List<UserInfo> usersInfos = supplierMaterialEvaluateDao.getSession().createQuery(hql)
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
		supplierMaterialEvaluateDao.getSession().save(userInfo);
	}
	/**
	 * 查询所有部门
	 * @return
	 */
	public List<Department> queryAllDepartments(){
		String hql = "from Department d where d.deleted = 0 order by d.parent desc,weight";
		List<Department> list = supplierMaterialEvaluateDao.createQuery(hql).list();
		return list;
	}
	
	/**
	 * 查询所有用户
	 * @return
	 */
	public List<User> queryAllUsers(){
		String hql = "from User d where d.deleted = 0 order by d.weight";
		List<User> list = supplierMaterialEvaluateDao.createQuery(hql).list();
		return list;
	}
}
