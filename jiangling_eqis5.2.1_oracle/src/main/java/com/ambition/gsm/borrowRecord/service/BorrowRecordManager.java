package com.ambition.gsm.borrowRecord.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.entity.OrtTestItem;
import com.ambition.gsm.borrowRecord.dao.BorrowRecordDao;
import com.ambition.gsm.entity.BorrowRecord;
import com.ambition.gsm.entity.BorrowRecordSublist;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;


@Service
@Transactional
public class BorrowRecordManager extends AmbWorkflowManagerBase<BorrowRecord>{
	@Autowired
	private BorrowRecordDao borrowRecordDao;
	@Override
	public HibernateDao<BorrowRecord, Long> getHibernateDao() {
		// TODO Auto-generated method stub
		return borrowRecordDao;
	}

	@Override
	public String getEntityListCode() {
		// TODO Auto-generated method stub
		return BorrowRecord.ENTITY_LIST_CODE;
	}

	@Override
	public Class<BorrowRecord> getEntityInstanceClass() {
		// TODO Auto-generated method stub
		return BorrowRecord.class;
	}

	@Override
	public String getWorkflowDefinitionCode() {
		// TODO Auto-generated method stub
		return "gsm_borrowRecord";
	}

	@Override
	public String getWorkflowDefinitionName() {
		// TODO Auto-generated method stub
		return "仪器借调申请流程";
	}
	 /**
     * 方法名: 导出报告
     * <p>功能说明：</p>
     * @return
    */
   public void exportReport(Long entityId) throws IOException {
       exportReport(entityId,"gsm_borrow-record.xls","仪器借调申请单");
   }
	public void deleteEntity(BorrowRecord entity){
		if(entity.getWorkflowInfo()!=null){
			String workflowId =  entity.getWorkflowInfo().getWorkflowId();
			//先删除子表
			Long reportId = entity.getId();
			String sql31 = "delete from GSM_BORROW_RECORD_SUBLIST where GSM_SUBLIST_ID = ?";	
			getHibernateDao().getSession().createSQLQuery(sql31).setParameter(0,reportId).executeUpdate();
			String sql = "delete from product_task_all_his where execution_id = ?";
			getHibernateDao().getSession().createSQLQuery(sql).setParameter(0,workflowId).executeUpdate();
			getHibernateDao().delete(entity);
		}else{
			getHibernateDao().delete(entity);
		}
	}

	public void saveChild(BorrowRecord report, String childParams) {
		// TODO Auto-generated method stub
		 JSONArray itemStrArray=null;
         if(!childParams.isEmpty()){
             itemStrArray=JSONArray.fromObject(childParams);
			if (!itemStrArray.isEmpty()) {
				if (report.getBorrowRecordSublists() == null) {
					report.setBorrowRecordSublists(new ArrayList<BorrowRecordSublist>());
				} else {
					report.getBorrowRecordSublists().clear();
				}
				for (int i = 0; i < itemStrArray.size(); i++) {
					JSONObject jso = itemStrArray.getJSONObject(i);
					BorrowRecordSublist item = new BorrowRecordSublist();
					item.setCompanyId(ContextUtils.getCompanyId());
					item.setCreatedTime(new Date());
					item.setCreatorName(ContextUtils.getUserName());
					item.setCreator(ContextUtils.getLoginName());
					for (Object key : jso.keySet()) {
						String value = jso.getString(key.toString());
						try {
							setProperty(item, key.toString(), value);
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					}

					item.setBorrowRecord(report);
//					borrowRecordDao.getSession().save(item);
					report.getBorrowRecordSublists().add(item);
				}
			}
         }
         borrowRecordDao.save(report);
	}
	/**
     * 方法名: setProperty 
     * <p>功能说明：设置属性</p>
     * @return void
     * @throws   
      */
     private void setProperty(Object obj,String property,Object value) throws Exception{
         Class<?> type = PropertyUtils.getPropertyType(obj,property);
         if(type != null){
             if(value==null||StringUtils.isEmpty(value.toString())){
                 PropertyUtils.setProperty(obj,property,null);
             }else{
                  if(String.class.getName().equals(type.getName())){
                     PropertyUtils.setProperty(obj,property,value.toString());
                 }else if(Integer.class.getName().equals(type.getName())){
                     PropertyUtils.setProperty(obj,property,Integer.valueOf(value.toString()));
                 }else if(Double.class.getName().equals(type.getName())){
                     PropertyUtils.setProperty(obj,property,Double.valueOf(value.toString()));
                 }else if(Float.class.getName().equals(type.getName())){
                     PropertyUtils.setProperty(obj,property,Float.valueOf(value.toString()));
                 }else if(Boolean.class.getName().equals(type.getName())){
                     PropertyUtils.setProperty(obj,property,Boolean.valueOf(value.toString()));
                 }else if(Date.class.getName().equals(type.getName())){
                     if(Date.class.getName().equals(value.getClass().getName())){
                         PropertyUtils.setProperty(obj,property,value);
                     }else if(String.class.getName().equals(value.getClass().getName())&&value.toString().length()==10){
                         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                         PropertyUtils.setProperty(obj,property,sdf.parse(value.toString()));
                     }
                 }else{
                     PropertyUtils.setProperty(obj,property,value);
                 }
             }
         }
     }
}
