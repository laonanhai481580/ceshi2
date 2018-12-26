package com.ambition.supplier.materialadmit.datasupply.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.supplier.entity.SupplierDataSupply;
import com.ambition.supplier.entity.SupplierMaterialAdmit;
import com.ambition.supplier.materialadmit.datasupply.dao.SupplierDataSupplyDao;
import com.ambition.supplier.materialadmit.evaluate.dao.SupplierMaterialEvaluateDao;
import com.norteksoft.acs.base.enumeration.MailboxDeploy;
import com.norteksoft.acs.entity.authorization.Role;
import com.norteksoft.acs.entity.organization.Company;
import com.norteksoft.acs.entity.organization.Department;
import com.norteksoft.acs.entity.organization.DepartmentUser;
import com.norteksoft.acs.entity.organization.User;
import com.norteksoft.acs.entity.organization.UserInfo;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.hibernate.HibernateDao;
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
public class SupplierDataSupplyManager extends AmbWorkflowManagerBase<SupplierDataSupply>{

	@Autowired
	private SupplierDataSupplyDao supplierDataSupplyDao;
	@Override
	public HibernateDao<SupplierDataSupply, Long> getHibernateDao() {
		// TODO Auto-generated method stub
		return supplierDataSupplyDao;
	}

	@Override
	public String getEntityListCode() {
		// TODO Auto-generated method stub
		return "SUPPLIER_DATA_SUPPLY";
	}

	@Override
	public Class<SupplierDataSupply> getEntityInstanceClass() {
		// TODO Auto-generated method stub
		return SupplierDataSupply.class;
	}

	@Override
	public String getWorkflowDefinitionCode() {
		// TODO Auto-generated method stub
		return "supplier-data-supply";
	}

	@Override
	public String getWorkflowDefinitionName() {
		// TODO Auto-generated method stub
		return "GP资料提供流程";
	}
	@Override
	public void exportReport(Long entityId) throws IOException{
		exportReport(entityId, "supplier-data-supply.xlsx", "GP资料提供申请表");
	}
	public Department searchSupplierDept() {
		// TODO Auto-generated method stub
		String hql = "from Department d where  d.deleted=? and d.name=?";
		@SuppressWarnings("unchecked")
		List<Department> depts = supplierDataSupplyDao.getSession()
				.createQuery(hql).setParameter(0, false).setParameter(1, "供应商").list();
		if(depts.size()==0){
			return null;
		}else{
			return depts.get(0);
		}
	}
	@SuppressWarnings("unchecked")
	public void saveUser(SupplierDataSupply report, Department department) {
		// TODO Auto-generated method stub
		Company company = null;
		// 取到第一个公司
		if (company == null) {
			String hql = "from Company c";
			List<?> list = supplierDataSupplyDao.getSession().createQuery(hql).list();
			company = (Company) list.get(0);
		}
		List<Role> defaultRoles = new ArrayList<Role>();
		String rolehql = "from Role r where r.deleted = ? and r.name = ?";
		defaultRoles = supplierDataSupplyDao.getSession().createQuery(rolehql).setParameter(0, false).setParameter(1, "普通用户").list();
		String hql = "select user from User user where user.deleted=false and user.loginName=?";
		List<?> users = supplierDataSupplyDao.getSession().createQuery(hql).setParameter(0, report.getSupplierLoginName())
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
			if(StringUtils.isNotEmpty(report.getSupplierMails())){
				user.setEmail(report.getSupplierMails().split("/")[0]);
			}
			user.setMailSize(10.0f);
			user.setHonorificName("");
			user.setMainDepartmentId(department == null ? null : department.getId());
			user.setMainDepartmentName(department == null ? null : department
					.getName());
			user.setMailboxDeploy(MailboxDeploy.INSIDE);
			supplierDataSupplyDao.getSession().save(user);
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
			departmentUsers = supplierDataSupplyDao.getSession().createQuery(hql).setParameter(0, user)
					.setParameter(1, department).list();
		} else {
			hql = "from DepartmentUser d where d.user = ? and d.department is null";
			departmentUsers = supplierDataSupplyDao.getSession().createQuery(hql).setParameter(0, user)
					.list();
		}
		if (departmentUsers.size() <= 0) {
			DepartmentUser departmentUser = new DepartmentUser();
			departmentUser.setCompanyId(company.getId());
			departmentUser.setUser(user);
			departmentUser.setDepartment(department);
			departmentUser.setDeleted(false);
			supplierDataSupplyDao.getSession().save(departmentUser);
		}
		// 保存用户基础信息
		hql = "from UserInfo u where u.user.id=?";
		List<UserInfo> usersInfos = supplierDataSupplyDao.getSession().createQuery(hql)
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
		supplierDataSupplyDao.getSession().save(userInfo);
	}
	/**
     * 删除实体，流程相关文件都删除
     * @param entity 删除的对象
     */
    @Override
    public void deleteEntity(SupplierDataSupply entity){
        if(entity.getWorkflowInfo()!=null){
            String workflowId =  entity.getWorkflowInfo().getWorkflowId();
            //先删除子表
            
            Long reportId = entity.getId();
            String sql31 = "delete from SUPPLIER_PRODUCT_PART_REPORT where FK_SUPPLIER_DATA_SUPPLY_ID = ?";
            getHibernateDao().getSession().createSQLQuery(sql31)
            .setParameter(0,reportId)
            .executeUpdate();
            ApiFactory.getInstanceService().deleteInstance(entity);
            //删除关联的办理期限
            String sql = "delete from product_task_all_his where execution_id = ?";
            getHibernateDao().getSession().createSQLQuery(sql)
            .setParameter(0,workflowId)
            .executeUpdate();
            //删除对象
            //getHibernateDao().delete(entity);
        }else{
            getHibernateDao().delete(entity);
        }
    }
}
