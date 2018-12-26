package com.ambition.epm.exception.service;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.epm.entity.EntrustHsfSublist;
import com.ambition.epm.entity.EntrustOrtSublist;
import com.ambition.epm.entity.ExceptionSingle;
import com.ambition.epm.entrustHsf.services.EntrustHsfSublistManager;
import com.ambition.epm.entrustOrt.services.EntrustOrtSublistManager;
import com.ambition.epm.exception.dao.ExceptionDao;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.util.tools.StringUtils;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Service
@Transactional
public class ExceptionManager extends AmbWorkflowManagerBase<ExceptionSingle>{
	@Autowired
	private ExceptionDao exceptionDao;
	@Override
	public HibernateDao<ExceptionSingle, Long> getHibernateDao() {
		return exceptionDao;
	}
	@Autowired
	private EntrustHsfSublistManager entrustHsfSublistManager;
	@Autowired
	private EntrustOrtSublistManager entrustOrtSublistManager;
	@Override
	public String getEntityListCode() {
		return ExceptionSingle.ENTITY_LIST_CODE;
	}

	@Override
	public Class<ExceptionSingle> getEntityInstanceClass() {
		return ExceptionSingle.class;
	}

	@Override
	public String getWorkflowDefinitionCode() {
		return "epm_exception-single";
	}

	@Override
	public String getWorkflowDefinitionName() {
		return "实验异常处理流程";
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
			ExceptionSingle report=getEntity(Long.valueOf(id));
			deleteEntity(report);
			sb.append(report.getFormNo() + ",");
		}
		message =sb.toString();
		return   message;
	}
	public void deleteFormNo(String ids){
		ExceptionSingle exceptionSingle = exceptionDao.get(Long.valueOf(ids));
		String hsfNo=exceptionSingle.getHsfNo();
		String ortNo=exceptionSingle.getOrtNo();
		if(hsfNo!=null&&!"".equals(hsfNo)){
			List<EntrustHsfSublist> list=entrustHsfSublistManager.getByHsfId(Long.valueOf(hsfNo));
			if(list.size()>0){
				String e=list.get(0).getExceptionNo();
				String r=exceptionSingle.getFormNo()+",";
				if(e.equals(exceptionSingle.getFormNo())){
					list.get(0).setExceptionNo(StringUtils.remove(e,exceptionSingle.getFormNo()));
				}else{
					list.get(0).setExceptionNo(StringUtils.remove(e, r));
				}
			}
		}
		if(ortNo!=null&&!"".equals(ortNo)){
			List<EntrustOrtSublist> list=entrustOrtSublistManager.getByOrtId(Long.valueOf(ortNo));
			if(list.size()>0){
				String e=list.get(0).getExceptionNo();
				String r=exceptionSingle.getFormNo()+",";
				if(e==null){
					
				}else if(e.equals(exceptionSingle.getFormNo())){
					list.get(0).setExceptionNo(StringUtils.remove(e,exceptionSingle.getFormNo()));
				}else{
					list.get(0).setExceptionNo(StringUtils.remove(e, r));
				}
			}		
		}
	
	}
	//回写单号
	public void writeNo(ExceptionSingle report){
		String hsfNo=report.getHsfNo();
		String ortNo=report.getOrtNo();
		EntrustHsfSublist entrustHsfSublist = null;
		EntrustOrtSublist entrustOrtSublist = null;
		if(hsfNo!=null&&!"".equals(hsfNo)){
			List<EntrustHsfSublist> list=entrustHsfSublistManager.getByHsfId(Long.valueOf(hsfNo));
			if(list.size()>0){
				entrustHsfSublist = list.get(0);
				if(entrustHsfSublist.getExceptionNo()!=null){
					String e=entrustHsfSublist.getExceptionNo();
					entrustHsfSublist.setExceptionNo(e+","+report.getFormNo());
				}
//				if(list.get(0).getExceptionNo()==report.getFormNo()){
//					list.get(0).setExceptionNo(report.getFormNo());
//				}
				if(entrustHsfSublist.getExceptionNo()==null){
					list.get(0).setExceptionNo(report.getFormNo());
				}
				exceptionDao.getSession().save(entrustHsfSublist);
			}
		}
		if(ortNo!=null&&!"".equals(ortNo)){
			List<EntrustOrtSublist> list=entrustOrtSublistManager.getByOrtId(Long.valueOf(ortNo));
			if(list.size()>0){
				entrustOrtSublist = list.get(0);
				if(entrustOrtSublist.getExceptionNo()!=null){
					String e=entrustOrtSublist.getExceptionNo();
					entrustOrtSublist.setExceptionNo(e+","+report.getFormNo());
				}
//				if(list.get(0).getExceptionNo()==report.getFormNo()){
//					list.get(0).setExceptionNo(report.getFormNo());
//				}
				if(entrustOrtSublist.getExceptionNo()==null){
					entrustOrtSublist.setExceptionNo(report.getFormNo());
				}
				exceptionDao.getSession().save(entrustOrtSublist);
			}			
		}
	}
	//隐藏单号
	public void hideWriteNo(ExceptionSingle report,String state){
		String hsfNo=report.getHsfNo();
		String ortNo=report.getOrtNo();
		EntrustHsfSublist entrustHsfSublist = null;
		EntrustOrtSublist entrustOrtSublist = null;
		if(hsfNo!=null&&!"".equals(hsfNo)){
			List<EntrustHsfSublist> list=entrustHsfSublistManager.getByHsfId(Long.valueOf(hsfNo));
			if(list.size()>0){
				entrustHsfSublist = list.get(0);
					if("N".equals(state)){
						if(entrustHsfSublist.getExceptionNo()!=null){
							String e=entrustHsfSublist.getExceptionNo();
							entrustHsfSublist.setExceptionNoHide(e);
							entrustHsfSublist.setExceptionNo("");
						}
					}else{
						entrustHsfSublist.setExceptionNo(entrustHsfSublist.getExceptionNoHide());
					}
				
//				if(list.get(0).getExceptionNo()==report.getFormNo()){
//					list.get(0).setExceptionNo(report.getFormNo());
//				}
				exceptionDao.getSession().save(entrustHsfSublist);
			}
		}
		if(ortNo!=null&&!"".equals(ortNo)){
			List<EntrustOrtSublist> list=entrustOrtSublistManager.getByOrtId(Long.valueOf(ortNo));
			if(list.size()>0){
				entrustOrtSublist = list.get(0);
					if("N".equals(state)){
						if(entrustOrtSublist.getExceptionNo()!=null){
							String e=entrustOrtSublist.getExceptionNo();
							entrustOrtSublist.setExceptionNoHide(e);
							entrustOrtSublist.setExceptionNo("");
						}
					}else{
						entrustOrtSublist.setExceptionNo(entrustOrtSublist.getExceptionNoHide());
					}
				
				exceptionDao.getSession().save(entrustOrtSublist);
			}			
		}
	}
	/**
     * 方法名: 导出报告
     * <p>功能说明：</p>
     * @return
    */
   public void exportReport(Long entityId) throws IOException {
       exportReport(entityId,"epm_exception-single.xls","异常处理单");
   }
   public void deleteEntrust(Long entrustId){
	   String hql = "delete from ExceptionSingle where entrustId = ?";
	   exceptionDao.createQuery(hql, entrustId).executeUpdate();
	   
	}

	public Boolean findUnCompleted(String type) {
		// TODO Auto-generated method stub
		String sql = "select count(*) from EPM_EXCEPTION_SINGLE s where s.completion_Time<sysdate and s.complete_Date is null and s.laboratory_Login=? and s.completion_Time is not null and s.item_no like ? ";
		Query query = exceptionDao.getSession().createSQLQuery(sql).setParameter(0, ContextUtils.getLoginName()).setParameter(1, type+'%');
		List<?> list = query.list();
		Integer mount = Integer.valueOf(list.get(0).toString());
		if(mount>=1){
			return false;
		}else{
			return true;
		}
	}
	public Page<ExceptionSingle> listState(Page<ExceptionSingle> page,String state ,String facy,String type){
		String hql = " from ExceptionSingle e where e.hiddenState=? ";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(state);
		if(type!=null ){ 
			hql=hql+" and e.itemNo like ?";
			searchParams.add('%'+type+'%');
		}
		if(facy!=null ){ 
			hql=hql+" and e.factoryClassify=?";
			searchParams.add(facy);
		}
		return exceptionDao.searchPageByHql(page, hql, searchParams.toArray());
		
	}
	public void hide(String hideId,String state){
		String[] ids = hideId.split(",");
		for(String id : ids){
			ExceptionSingle exceptionSingle = exceptionDao.get(Long.valueOf(id));
			if("Y".equals(state)){
				exceptionSingle.setHiddenState("N");
				hideWriteNo(exceptionSingle,"Y");
			}else{
				exceptionSingle.setHiddenState("Y");
				hideWriteNo(exceptionSingle,"N");
			}
			exceptionDao.save(exceptionSingle);
		}
	}
}
