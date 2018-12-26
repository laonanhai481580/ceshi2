package com.ambition.gp.gpmaterial.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.gp.entity.GpMaterial;
import com.ambition.gp.gpmaterial.dao.GpMaterialDao;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.supplier.admitRemake.dao.SupplierAdmitDao;
import com.ambition.supplier.admitRemake.service.SupplierAdmitManager;
import com.ambition.supplier.approval.dao.SupplierApprovalDao;
import com.ambition.supplier.approval.service.SupplierApprovalManager;
import com.ambition.supplier.entity.SupplierAdmit;
import com.ambition.supplier.entity.SupplierApproval;
import com.norteksoft.acs.base.enumeration.MailboxDeploy;
import com.norteksoft.acs.base.web.struts2.Struts2Utils;
import com.norteksoft.acs.entity.authorization.Role;
import com.norteksoft.acs.entity.authorization.RoleUser;
import com.norteksoft.acs.entity.organization.Company;
import com.norteksoft.acs.entity.organization.Department;
import com.norteksoft.acs.entity.organization.DepartmentUser;
import com.norteksoft.acs.entity.organization.User;
import com.norteksoft.acs.entity.organization.UserInfo;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.AsyncMailUtils;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.Md5;
import com.norteksoft.product.util.PropUtils;

@Service
@Transactional
public class GpMaterialManager extends AmbWorkflowManagerBase<GpMaterial> {
	@Autowired
	private FormCodeGenerated formCodeGenerated;
	@Autowired
	private SupplierApprovalManager supplierApprovalManager;
	@Autowired
	private SupplierAdmitManager supplierAdmitManager;
	@Autowired
	private GpMaterialDao gpMaterialDao;
	@Autowired
	private SupplierApprovalDao supplierApprovalDao;
	@Autowired
	private SupplierAdmitDao supplierAdmitDao;

	@Override
	public HibernateDao<GpMaterial, Long> getHibernateDao() {
		// TODO Auto-generated method stub
		return gpMaterialDao;
	}

	@Override
	public String getEntityListCode() {
		// TODO Auto-generated method stub
		return GpMaterial.ENTITY_LIST_CODE;
	}

	@Override
	public Class<GpMaterial> getEntityInstanceClass() {
		// TODO Auto-generated method stub
		return GpMaterial.class;
	}

	@Override
	public String getWorkflowDefinitionCode() {
		// TODO Auto-generated method stub
		return "gp_GpMaterial";
	}

	@Override
	public String getWorkflowDefinitionName() {
		// TODO Auto-generated method stub
		return "GP资料流程2.0";
	}

	/**
	 * 删除实体，流程相关文件都删除
	 * 
	 * @param ids
	 */
	public String deleteEntity(String ids) {
		String[] deleteIds = ids.split(",");
		StringBuilder sb = new StringBuilder("");
		String message = "";
		for (String id : deleteIds) {
			GpMaterial report = getEntity(Long.valueOf(id));
			deleteEntity(report);
			sb.append(report.getFormNo() + ",");
		}
		message = sb.toString();
		return message;
	}

	public void deleteEntity(GpMaterial entity) {
		if (entity.getWorkflowInfo() != null) {
			String workflowId = entity.getWorkflowInfo().getWorkflowId();
			// 先删除子表
			Long reportId = entity.getId();
			String sql31 = "delete from GP_MATERIAL_SUB where GP_MATERIAL_SUB = ?";
			getHibernateDao().getSession().createSQLQuery(sql31)
					.setParameter(0, reportId).executeUpdate();
			String sql = "delete from product_task_all_his where execution_id = ?";
			getHibernateDao().getSession().createSQLQuery(sql)
					.setParameter(0, workflowId).executeUpdate();
			getHibernateDao().delete(entity);
			ApiFactory.getInstanceService().deleteInstance(entity);
		} else {
			getHibernateDao().delete(entity);
		}
	}

	public Page<GpMaterial> listState(Page<GpMaterial> page, String code,
			String type) {
		String hql = " from GpMaterial e where e.companyId=?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		if (code != null) {
			hql = hql + " and e.supplierCode=?";
			searchParams.add(code);
		}
		if (type != null) {
			hql = hql + " and e.isHarmful=?";
			searchParams.add(type);
		} else {
			hql = hql + " and e.isHarmful='4'";
			// searchParams.add(type);
		}
		return gpMaterialDao.searchPageByHql(page, hql, searchParams.toArray());
	}

	public Department searchSupplierDept() {
		// TODO Auto-generated method stub
		String hql = "from Department d where  d.deleted=? and d.name=?";
		@SuppressWarnings("unchecked")
		List<Department> depts = gpMaterialDao.getSession().createQuery(hql)
				.setParameter(0, false).setParameter(1, "供应商").list();
		if (depts.size() == 0){
			return null;
		} else {
			return depts.get(0);
		}
	}

	@SuppressWarnings("unchecked")
	public void saveUser(GpMaterial report, Department department) {
		// TODO Auto-generated method stub
		Company company = null;
		// 取到第一个公司
		if (company == null) {
			String hql = "from Company c";
			List<?> list = gpMaterialDao.getSession().createQuery(hql).list();
			company = (Company) list.get(0);
		}
		List<Role> defaultRoles = new ArrayList<Role>();
		String rolehql = "from Role r where r.deleted = ? and r.name = ?";
		defaultRoles = gpMaterialDao.getSession().createQuery(rolehql)
				.setParameter(0, false).setParameter(1, "普通用户").list();
		String hql = "select user from User user where user.deleted=false and user.loginName=?";
		List<?> users = gpMaterialDao.getSession().createQuery(hql)
				.setParameter(0, report.getSupplierLoginName()).list();
		User user = new User();
		boolean isNew = false;
		if (users.size() <= 0) {
			user = new User();
			user.setCompanyId(company.getId());
			isNew = true;
			user.setLoginName(report.getSupplierLoginName());
			user.setName(report.getSupplierName().trim());
			user.setPassword(Md5.toMessageDigest(report.getSupplierLoginName()
					.trim()));
			if (StringUtils.isNotEmpty(report.getSupplierEmail())) {
				user.setEmail(report.getSupplierEmail());
			}
			user.setMailSize(10.0f);
			user.setHonorificName("");
			user.setMainDepartmentId(department == null ? null : department
					.getId());
			user.setMainDepartmentName(department == null ? null : department
					.getName());
			user.setMailboxDeploy(MailboxDeploy.INSIDE);
			gpMaterialDao.getSession().save(user);
		} else {
			user = (User) users.get(0);
		}
		if (isNew) {
			// 判断用户角色是否存在,RoleUser的在user,role建立了索引，不能重复
			for (Role role : defaultRoles) {
				hql = "from RoleUser r where r.user = ? and r.role = ? ";
				List<?> roleUserList = gpMaterialDao.getSession()
						.createQuery(hql).setParameter(0, user)
						.setParameter(1, role).list();
				if (roleUserList.isEmpty()) {
					RoleUser roleUser = new RoleUser();
					roleUser.setAllUser("所有用户");
					roleUser.setCompanyId(company.getId());
					roleUser.setDeleted(false);
					roleUser.setRole(role);
					roleUser.setUser(user);
					roleUser.setTs(new Date());
					gpMaterialDao.getSession().save(roleUser);
				}
			}
		}
		// 添加兼职部门
		List<?> departmentUsers = new ArrayList<DepartmentUser>();
		if (department != null) {
			hql = "from DepartmentUser d where d.user = ? and d.department = ?";
			departmentUsers = gpMaterialDao.getSession().createQuery(hql)
					.setParameter(0, user).setParameter(1, department).list();
		} else {
			hql = "from DepartmentUser d where d.user = ? and d.department is null";
			departmentUsers = gpMaterialDao.getSession().createQuery(hql)
					.setParameter(0, user).list();
		}
		if (departmentUsers.size() <= 0) {
			DepartmentUser departmentUser = new DepartmentUser();
			departmentUser.setCompanyId(company.getId());
			departmentUser.setUser(user);
			departmentUser.setDepartment(department);
			departmentUser.setDeleted(false);
			gpMaterialDao.getSession().save(departmentUser);
		}
		// 保存用户基础信息
		hql = "from UserInfo u where u.user.id=?";
		List<UserInfo> usersInfos = gpMaterialDao.getSession().createQuery(hql)
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
		gpMaterialDao.getSession().save(userInfo);
	}

	@SuppressWarnings("static-access")
	public String releaseEmail(String params, String formNo)
			throws InterruptedException {
		String emailContent = "您有一份产品成份宣告表任务需要提交! 单号:" + formNo;
		Set<String> names = new HashSet<String>();
		String[] sendMails = params.split(",");
		for (int i = 0; i < sendMails.length; i++) {
			names.add(sendMails[i]);
		}
		for (Iterator<String> nameIte = names.iterator(); nameIte.hasNext();) {
			String name = nameIte.next().toString();
			List<com.norteksoft.product.api.entity.User> user = ApiFactory
					.getAcsService().getUsersByName(name);
			String email = user.get(0).getEmail();
			if (StringUtils.isNotEmpty(email)) {
				AsyncMailUtils.sendMail(email, "欧菲科技QIS系统产品成分宣告表任务通知!",
						emailContent.toString());
				Thread.currentThread().sleep(10000);// 毫秒
			}
		}
		return null;
	}

	@SuppressWarnings("static-access")
	public String releaseEmail1(String params, String url, String formNo,
			String id) throws InterruptedException {
		String emailContent = "您有一份任务报告待查看  单号:" + formNo;
		if (url != null) {
			emailContent = emailContent + "http://" + url;
		}
		GpMaterial gpMaterial = null;
		if (id != null) {
			gpMaterial = gpMaterialDao.get(Long.valueOf(id));
			emailContent = emailContent + '\n' + " 物料名称:"
					+ gpMaterial.getMaterialName() + '\n' + " 物料编码:"
					+ gpMaterial.getMaterialCode() + '\n' + " 回复交期:"
					+ gpMaterial.getRevertDate();
		}
		Set<String> names = new HashSet<String>();
		params = emailFormat(params);
		String[] sendMails = params.split(",");
		for (int i = 0; i < sendMails.length; i++) {
			names.add(sendMails[i]);
		}
		for (Iterator<String> nameIte = names.iterator(); nameIte.hasNext();) {
			String email = nameIte.next().toString();
			if (StringUtils.isNotEmpty(email)) {
				AsyncMailUtils.sendMail(email, "欧菲科技QIS系统产品成分宣告表任务通知！",
						emailContent.toString());
				Thread.currentThread().sleep(6000);// 毫秒
			}
		}
		return null;
	}

	public GpMaterial searchByApproveId(Long id) {
		List<GpMaterial> list = gpMaterialDao.searchByApproveId(id);
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public String findGpMaterial(Long id) {

		SupplierAdmit supplierAdmit = supplierAdmitManager.getEntity(id);
		String sql = "select g.form_No from GP_MATERIAL g where g.supplier_Code = ? and g.material_Code = ? ";
		Query query = gpMaterialDao.getSession().createSQLQuery(sql);
		String companyName = PropUtils.getProp("companyName");
		if ("CCM".equals(companyName) || "FPM".equals(companyName)
				|| "SQ".equals(companyName)) {
			SupplierApproval supplierApproval = supplierApprovalManager
					.getEntity(id);
			query.setParameter(0, supplierApproval.getSupplierCode())
					.setParameter(1, supplierApproval.getMaterialCode().trim());
		} else {
			query.setParameter(0, supplierAdmit.getSupplierCode())
					.setParameter(1, supplierAdmit.getMaterialCode().trim());
		}
		List<?> list = query.list();
		if (list.size() > 0) {
			return list.get(0).toString();
		}
		return null;
	}

	public String findSupplierMaterial(Long id) {
		GpMaterial gpMaterial = gpMaterialDao.get(id);
		String companyName = PropUtils.getProp("companyName");
		if ("CCM".equals(companyName) || "FPM".equals(companyName)
				|| "SQ".equals(companyName)) {
			supplierApprovalManager.findSupplierMaterial(gpMaterial);
		} else {
			supplierAdmitManager.findSupplierMaterial(gpMaterial);
		}
		return null;
	}

	public String emailFormat(String email) {
		if (email.indexOf("；") != -1) {
			email = email.replaceAll("；", ",");
		}
		if (email.indexOf(";") != -1) {
			email = email.replaceAll(";", ",");
		}
		if (email.indexOf("，") != -1) {
			email = email.replaceAll("，", ",");
		}
		return email;
	}

	public void setGpMaterialState(String id) {
		String companyName = PropUtils.getProp("companyName");
		String supplierId = Struts2Utils.getParameter("approvalId");
		String formNo = Struts2Utils.getParameter("formNo");
		if (supplierId == null || "".equals(supplierId)) {
			return;
		}
		if ("CCM".equals(companyName) || "FPM".equals(companyName)
				|| "SQ".equals(companyName)) {
			SupplierApproval supplierApproval = supplierApprovalManager
					.getEntity(Long.valueOf(supplierId));
			supplierApproval.setGpMaterialState("已完结");
			supplierApproval.setGpMaterialId(id);
			supplierApproval.setGpMaterialNo(formNo);
			supplierApprovalManager.saveEntity(supplierApproval);
		} else {
			SupplierAdmit supplierAdmit = supplierAdmitManager.getEntity(Long
					.valueOf(supplierId));
			supplierAdmit.setGpMaterialState("已完结");
			supplierAdmit.setGpMaterialId(id);
			supplierAdmit.setGpMaterialNo(formNo);
			supplierAdmitManager.saveEntity(supplierAdmit);
		}
	}
}
