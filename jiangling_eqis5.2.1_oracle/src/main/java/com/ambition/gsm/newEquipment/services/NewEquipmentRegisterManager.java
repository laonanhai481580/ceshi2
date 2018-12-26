package com.ambition.gsm.newEquipment.services;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.entity.MfgCheckInspectionReport;
import com.ambition.gsm.entity.BorrowRecordSublist;
import com.ambition.gsm.entity.Entrust;
import com.ambition.gsm.entity.NewEquipmentRegister;
import com.ambition.gsm.entity.NewEquipmentSublist;
import com.ambition.gsm.newEquipment.dao.NewEquipmentRegisterDao;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.util.common.ExcelUtil;
import com.ambition.util.exportexcel.ExportExcelFormatter;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;


/**
 * 类名:新进检测设备申请登记Manager
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  xmh
 * @version 1.00 2016-11-16 发布
 */
@Service
@Transactional
public class NewEquipmentRegisterManager extends AmbWorkflowManagerBase<NewEquipmentRegister>{
	@Autowired
	private NewEquipmentRegisterDao newEquipmentRegisterDao;
	
	@Override
	public HibernateDao<NewEquipmentRegister, Long> getHibernateDao() {
		// TODO Auto-generated method stub
		return newEquipmentRegisterDao;
	}

	@Override
	public String getEntityListCode() {
		// TODO Auto-generated method stub
		return NewEquipmentRegister.ENTITY_LIST_CODE;
	}

	@Override
	public Class<NewEquipmentRegister> getEntityInstanceClass() {
		// TODO Auto-generated method stub
		return NewEquipmentRegister.class;
	}

	@Override
	public String getWorkflowDefinitionCode() {
		// TODO Auto-generated method stub
		return "gsm_newEquipment";
	}

	@Override
	public String getWorkflowDefinitionName() {
		// TODO Auto-generated method stub
		return "新进检测设备申请登记表流程";
	}
	 /**
     * 方法名: 导出报告
     * <p>功能说明：</p>
     * @return
    */
   public void exportReport(Long entityId) throws IOException {
       exportReport(entityId,"gsm-new-equipment.xls","新进检测设备申请登记表");
   }
	public void deleteEntity(NewEquipmentRegister entity){
		if(entity.getWorkflowInfo()!=null){
			String workflowId =  entity.getWorkflowInfo().getWorkflowId();
			//先删除子表
			Long reportId = entity.getId();
			String sql31 = "delete from GSM_EQUIPMENT_SUBLIST where GSM_SUBLIST_ID = ?";	
			getHibernateDao().getSession().createSQLQuery(sql31).setParameter(0,reportId).executeUpdate();
			String sql = "delete from product_task_all_his where execution_id = ?";
			getHibernateDao().getSession().createSQLQuery(sql).setParameter(0,workflowId).executeUpdate();
			getHibernateDao().delete(entity);
		}else{
			getHibernateDao().delete(entity);
		}
	}

	public void saveChild(NewEquipmentRegister report, String childParams) {
		JSONArray itemStrArray=null;
        if(!childParams.isEmpty()){
            itemStrArray=JSONArray.fromObject(childParams);
			if (!itemStrArray.isEmpty()) {
				if (report.getNewEquipmentSublists() == null) {
					report.setNewEquipmentSublists(new ArrayList<NewEquipmentSublist>());
				} else {
					report.getNewEquipmentSublists().clear();
				}
				for (int i = 0; i < itemStrArray.size(); i++) {
					JSONObject jso = itemStrArray.getJSONObject(i);
					NewEquipmentSublist item = new NewEquipmentSublist();
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

					item.setNewEquipmentRegister(report);
					newEquipmentRegisterDao.getSession().save(item);
					report.getNewEquipmentSublists().add(item);
				}
			}
        }
        newEquipmentRegisterDao.save(report);
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
