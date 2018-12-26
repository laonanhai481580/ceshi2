package com.ambition.supplier.materialadmit.admit.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.supplier.entity.SupplierMaterialAdmit;
import com.ambition.supplier.entity.SupplierMaterialEvaluate;
import com.ambition.supplier.materialadmit.admit.dao.SupplierMaterialAdmitDao;
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
 * @version 1.00 2016年10月19日 发布
 */
@Service
@Transactional
public class SupplierMaterialAdmitManager extends AmbWorkflowManagerBase<SupplierMaterialAdmit>{

	@Autowired
	private SupplierMaterialAdmitDao supplierMaterialAdmitDao;
	@Autowired
	private AcsUtils acsUtils;
	@Override
	public HibernateDao<SupplierMaterialAdmit, Long> getHibernateDao() {
		// TODO Auto-generated method stub
		return supplierMaterialAdmitDao;
	}

	@Override
	public String getEntityListCode() {
		// TODO Auto-generated method stub
		return "SUPPLIER_MATERIAL_ADMIT";
	}

	@Override
	public Class<SupplierMaterialAdmit> getEntityInstanceClass() {
		// TODO Auto-generated method stub
		return SupplierMaterialAdmit.class;
	}

	@Override
	public String getWorkflowDefinitionCode() {
		// TODO Auto-generated method stub
		return "supplier-material-admit";
	}

	@Override
	public String getWorkflowDefinitionName() {
		// TODO Auto-generated method stub
		return "材料承认申请流程";
	}
	@Override
	public void exportReport(Long entityId) throws IOException{
		exportReport(entityId, "supplier-material-admit.xlsx", "材料承认申请表");
	}
	/**
	  * 方法名: 分页查询 是否延期
	  * <p>功能说明：</p>
	  * @param page
	  * @return
	 */
	public Page<SupplierMaterialAdmit> search(Page<SupplierMaterialAdmit> page){
		String hql = "from "+getEntityInstanceClass().getName()+" t";
		Department departMent =  acsUtils.getDepartmentById(ContextUtils.getDepartmentId());
		try {
			if("供应商".equals(departMent)){
				hql += " where t.supplierCode=?";
				page = getHibernateDao().searchPageByHql(page,hql,ContextUtils.getLoginName());
			}else{
				page = getHibernateDao().searchPageByHql(page,hql);
			}
			
		} catch (Exception e) {
			throw new AmbFrameException("分页查询失败!",e);
		}
		//节点超期
		//searchExceedStatistics(page);
		return page;
	}

	public Department searchSupplierDept() {
		// TODO Auto-generated method stub
		String hql = "from Department d where  d.deleted=? and d.name=?";
		@SuppressWarnings("unchecked")
		List<Department> depts = supplierMaterialAdmitDao.getSession()
				.createQuery(hql).setParameter(0, false).setParameter(1, "供应商").list();
		if(depts.size()==0){
			return null;
		}else{
			return depts.get(0);
		}
	}
	@SuppressWarnings("unchecked")
	public void saveUser(SupplierMaterialAdmit report, Department department) {
		// TODO Auto-generated method stub
		Company company = null;
		// 取到第一个公司
		if (company == null) {
			String hql = "from Company c";
			List<?> list = supplierMaterialAdmitDao.getSession().createQuery(hql).list();
			company = (Company) list.get(0);
		}
		List<Role> defaultRoles = new ArrayList<Role>();
		String rolehql = "from Role r where r.deleted = ? and r.name = ?";
		defaultRoles = supplierMaterialAdmitDao.getSession().createQuery(rolehql).setParameter(0, false).setParameter(1, "普通用户").list();
		String hql = "select user from User user where user.deleted=false and user.loginName=?";
		List<?> users = supplierMaterialAdmitDao.getSession().createQuery(hql).setParameter(0, report.getSupplierLoginName())
				.list();
		User user = new User();
		boolean isNew = false;
		if (users.size() <= 0) {
			user = new User();
			user.setCompanyId(company.getId());
			isNew = true;
			user.setLoginName(report.getSupplierLoginName());
			user.setName(report.getSupplierName());
			user.setPassword(Md5.toMessageDigest(report.getSupplierLoginName()));
			if(StringUtils.isNotEmpty(report.getSupplierEmail())){
				user.setEmail(report.getSupplierEmail());
			}
			user.setMailSize(10.0f);
			user.setHonorificName("");
			user.setMainDepartmentId(department == null ? null : department.getId());
			user.setMainDepartmentName(department == null ? null : department
					.getName());
			user.setMailboxDeploy(MailboxDeploy.INSIDE);
			supplierMaterialAdmitDao.getSession().save(user);
		}else {
			user = (User) users.get(0);
		}
		if (isNew) {
			// 判断用户角色是否存在,RoleUser的在user,role建立了索引，不能重复
			for(Role role : defaultRoles){
				hql = "from RoleUser r where r.user = ? and r.role = ? ";
				List<?> roleUserList = supplierMaterialAdmitDao.getSession().createQuery(hql).setParameter(0, user).setParameter(1, role).list();
				if (roleUserList.isEmpty()) {
					RoleUser roleUser = new RoleUser();
					roleUser.setAllUser("所有用户");
					roleUser.setCompanyId(company.getId());
					roleUser.setDeleted(false);
					roleUser.setRole(role);
					roleUser.setUser(user);
					roleUser.setTs(new Date());
					supplierMaterialAdmitDao.getSession().save(roleUser);
				}
			}
		}
		// 添加兼职部门
		List<?> departmentUsers = new ArrayList<DepartmentUser>();
		if (department != null) {
			hql = "from DepartmentUser d where d.user = ? and d.department = ?";
			departmentUsers = supplierMaterialAdmitDao.getSession().createQuery(hql).setParameter(0, user)
					.setParameter(1, department).list();
		} else {
			hql = "from DepartmentUser d where d.user = ? and d.department is null";
			departmentUsers = supplierMaterialAdmitDao.getSession().createQuery(hql).setParameter(0, user)
					.list();
		}
		if (departmentUsers.size() <= 0) {
			DepartmentUser departmentUser = new DepartmentUser();
			departmentUser.setCompanyId(company.getId());
			departmentUser.setUser(user);
			departmentUser.setDepartment(department);
			departmentUser.setDeleted(false);
			supplierMaterialAdmitDao.getSession().save(departmentUser);
		}
		// 保存用户基础信息
		hql = "from UserInfo u where u.user.id=?";
		List<UserInfo> usersInfos = supplierMaterialAdmitDao.getSession().createQuery(hql)
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
		supplierMaterialAdmitDao.getSession().save(userInfo);
	}

}
