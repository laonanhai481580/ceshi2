package com.ambition.supplier.improve.services;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.iqc.entity.IncomingInspectionActionsReport;
import com.ambition.iqc.inspectionreport.service.IncomingInspectionActionsReportManager;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.supplier.entity.SupplierImprove;
import com.ambition.supplier.improve.dao.SupplierImproveDao;
import com.norteksoft.acs.base.enumeration.MailboxDeploy;
import com.norteksoft.acs.entity.authorization.Role;
import com.norteksoft.acs.entity.organization.Company;
import com.norteksoft.acs.entity.organization.Department;
import com.norteksoft.acs.entity.organization.DepartmentUser;
import com.norteksoft.acs.entity.organization.User;
import com.norteksoft.acs.entity.organization.UserInfo;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.Md5;
import com.norteksoft.wf.base.enumeration.CompleteTaskTipType;

/**
 * 类名:
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  linshaowei
 * @version 1.00 2016年11月4日 发布
 */
@Service
@Transactional
public class SupplierImproveManager extends AmbWorkflowManagerBase<SupplierImprove>{
	@Autowired
	private IncomingInspectionActionsReportManager incomingInspectionActionsReportManager;
	@Autowired
	private SupplierImproveDao supplierImproveDao;
	@Override
	public HibernateDao<SupplierImprove, Long> getHibernateDao() {
		// TODO Auto-generated method stub
		return supplierImproveDao;
	}

	@Override
	public String getEntityListCode() {
		// TODO Auto-generated method stub
		return "SUPPLIER_IMPROVE";
	}

	@Override
	public Class<SupplierImprove> getEntityInstanceClass() {
		// TODO Auto-generated method stub
		return SupplierImprove.class;
	}

	@Override
	public String getWorkflowDefinitionCode() {
		// TODO Auto-generated method stub
		return "supplier-improve";
	}
	public String getWorkflowDefinitionCodeNew() {
		return "supplier-improve-new";
	}
	@Override
	public String getWorkflowDefinitionName() {
		// TODO Auto-generated method stub
		return "进料异常纠正措施流程";
	}
	@Override
	public void exportReport(Long entityId) throws IOException{
		exportReport(entityId, "supplier-improve.xlsx", "进料异常纠正措施单");
	}

	
	
	
	
	
	public Department searchSupplierDept() {
		String hql = "from Department d where  d.deleted=? and d.name=?";
		@SuppressWarnings("unchecked")
		List<Department> depts = supplierImproveDao.getSession()
				.createQuery(hql).setParameter(0, false).setParameter(1, "供应商").list();
		if(depts.size()==0){
			return null;
		}else{
			return depts.get(0);
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
	public void saveEntity(SupplierImprove report,Map<String,List<JSONObject>> childMaps){
		//数据处理
		if(report.getTempCountermeasures()!=null){
			report.setTempCountermeasures(report.getTempCountermeasures().replaceAll("", "").replaceAll("", ""));
		}
		if(report.getTrueReasonCheck()!=null){
			report.setTrueReasonCheck(report.getTrueReasonCheck().replaceAll("", "").replaceAll("", ""));
		}
		if(report.getCountermeasures()!=null){
			report.setCountermeasures(report.getCountermeasures().replaceAll("", "").replaceAll("", ""));
		}
		if(report.getPreventHappen()!=null){
			report.setPreventHappen(report.getPreventHappen().replaceAll("", "").replaceAll("", ""));
		}
		report.setModifiedTime(new Date());
		report.setModifier(ContextUtils.getLoginName());
		report.setModifierName(ContextUtils.getUserName());
		getHibernateDao().save(report);
		//设置子表的值
		setChildItems(report,childMaps);
		
	}	
	
	/**
	  * 方法名:提交流程
	  * <p>功能说明：</p>
	  * @param report
	  * @return
	 */
	public CompleteTaskTipType submitProcess(SupplierImprove report,Map<String,List<JSONObject>> childMaps){
		//设置子表的值
		setChildItems(report,childMaps);
		this.saveEntity(report);//zsz
		Long processId=null;
		if(report.getIsNewData()!=null&&report.getIsNewData().equals("是")){
			processId= ApiFactory.getDefinitionService().getWorkflowDefinitionsByCode(getWorkflowDefinitionCodeNew()).get(0).getId();
		}else{
			processId= ApiFactory.getDefinitionService().getWorkflowDefinitionsByCode(getWorkflowDefinitionCode()).get(0).getId();
		}	
		CompleteTaskTipType taskTipType = submitProcess(report,"发起",processId);
		//流程提交时间
		report.getWorkflowInfo().setSubmitTime(new Date());
		//更新新任务的环节状态
		updateTaskStage(report);
		return taskTipType;
	}
	//验证并保存记录
	public boolean isExistIqcSupplierImprove(Long id){
		String hql = "select count(*) from SupplierImprove d where d.companyId =? and d.inspectionId=?   ";
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());		
		params.add(id);
		Query query = supplierImproveDao.getSession().createQuery(hql);
		for(int i = 0;i < params.size(); i++){
			query.setParameter(i, params.get(i));
		}
		@SuppressWarnings("rawtypes")
		List list = query.list();
		if(Integer.valueOf(list.get(0).toString()) > 0){
			return true;
		}else{
			return false;
		}		
	}
	
	@SuppressWarnings("unchecked")
	public void saveUser(SupplierImprove report, Department department) {
		// TODO Auto-generated method stub
			Company company = null;
			// 取到第一个公司
			if (company == null) {
				String hql = "from Company c";
				List<?> list = supplierImproveDao.getSession().createQuery(hql).list();
				company = (Company) list.get(0);
			}
			List<Role> defaultRoles = new ArrayList<Role>();
			String rolehql = "from Role r where r.deleted = ? and r.name = ?";
			defaultRoles = supplierImproveDao.getSession().createQuery(rolehql).setParameter(0, false).setParameter(1, "普通用户").list();
			String hql = "select user from User user where user.deleted=false and user.loginName=?";
			List<?> users = supplierImproveDao.getSession().createQuery(hql).setParameter(0, report.getSupplierCode())
					.list();
			User user = new User();
			boolean isNew = false;
			if (users.size() <= 0) {
				user = new User();
				user.setCompanyId(company.getId());
				user.setSubCompanyName(company.getName());
				isNew = true;
				user.setLoginName(report.getSupplierCode());
				user.setName(report.getSupplierName());
				user.setPassword(Md5.toMessageDigest(report.getSupplierCode()));
				if(StringUtils.isNotEmpty(report.getSupplierEmail())){
					user.setEmail(report.getSupplierEmail().split("/")[0]);
				}
				user.setMailSize(10.0f);
				user.setHonorificName("");
				user.setMainDepartmentId(department == null ? null : department.getId());
				user.setMainDepartmentName(department == null ? null : department
						.getName());
				user.setMailboxDeploy(MailboxDeploy.INSIDE);
				supplierImproveDao.getSession().save(user);
			}else {
				user = (User) users.get(0);
			}
			/*if (isNew) {
				// 判断用户角色是否存在,RoleUser的在user,role建立了索引，不能重复
				for(Role role : defaultRoles){
					hql = "from RoleUser r where r.user = ? and r.role = ? ";
					List<?> roleUserList = supplierImproveDao.getSession().createQuery(hql).setParameter(0, user).setParameter(1, role).list();
					if (roleUserList.isEmpty()) {
						RoleUser roleUser = new RoleUser();
						roleUser.setAllUser("所有用户");
						roleUser.setCompanyId(company.getId());
						roleUser.setDeleted(false);
						roleUser.setRole(role);
						roleUser.setUser(user);
						roleUser.setTs(new Date());
						supplierImproveDao.getSession().save(roleUser);
					}
				}
			}*/
			// 添加兼职部门
			List<?> departmentUsers = new ArrayList<DepartmentUser>();
			if (department != null) {
				hql = "from DepartmentUser d where d.user = ? and d.department = ?";
				departmentUsers = supplierImproveDao.getSession().createQuery(hql).setParameter(0, user)
						.setParameter(1, department).list();
			} else {
				hql = "from DepartmentUser d where d.user = ? and d.department is null";
				departmentUsers = supplierImproveDao.getSession().createQuery(hql).setParameter(0, user)
						.list();
			}
			if (departmentUsers.size() <= 0) {
				DepartmentUser departmentUser = new DepartmentUser();
				departmentUser.setCompanyId(company.getId());
				departmentUser.setUser(user);
				departmentUser.setDepartment(department);
				departmentUser.setDeleted(false);
				supplierImproveDao.getSession().save(departmentUser);
			}
			// 保存用户基础信息
			hql = "from UserInfo u where u.user.id=?";
			List<UserInfo> usersInfos = supplierImproveDao.getSession().createQuery(hql)
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
			supplierImproveDao.getSession().save(userInfo);
	}
	public Page<SupplierImprove> searchSingle(Page<SupplierImprove>page){
		return supplierImproveDao.searchSingle(page);		
	}
	public Page<SupplierImprove> searchSupplierSingle(Page<SupplierImprove>page){
		return supplierImproveDao.searchSupplierSingle(page);		
	}
	
	public Page<SupplierImprove> searchOkSingle(Page<SupplierImprove>page){
		return supplierImproveDao.searchOkSingle(page);		
	}
	public Page<SupplierImprove> searchOkSupplierSingle(Page<SupplierImprove>page){
		return supplierImproveDao.searchOkSupplierSingle(page);		
	}
	public Page<SupplierImprove> oklist(Page<SupplierImprove>page){
		return supplierImproveDao.oklist(page);
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
			SupplierImprove report=getEntity(Long.valueOf(id));
			deleteEntity(report);
			sb.append(report.getFormNo() + ",");
		}
		message =sb.toString();
		return   message;
	}	
	
	/**
	 * 删除实体，流程相关文件都删除
	 * @param entity 删除的对象
	 */
	public void deleteEntity(SupplierImprove entity){
		if(entity.getWorkflowInfo()!=null){
			String workflowId =  entity.getWorkflowInfo().getWorkflowId();
			ApiFactory.getInstanceService().deleteInstance(entity);
		}else{
			getHibernateDao().delete(entity);
		}
		//删掉对应IQC报告中的编号
		if(entity.getInspectionId()!=null){
			IncomingInspectionActionsReport report=incomingInspectionActionsReportManager.getIncomingInspectionActionsReport(entity.getInspectionId());
			report.setExceptionId("");
			report.setExceptionNo("");
		}
		
	}	
}
