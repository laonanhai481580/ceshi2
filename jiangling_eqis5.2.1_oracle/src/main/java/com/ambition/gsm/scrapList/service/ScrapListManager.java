package com.ambition.gsm.scrapList.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.gsm.entity.ScrapList;
import com.ambition.gsm.scrapList.dao.ScrapListDao;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.norteksoft.product.orm.hibernate.HibernateDao;

@Service
@Transactional
public class ScrapListManager extends AmbWorkflowManagerBase<ScrapList>{
	@Autowired
	private ScrapListDao scrapListDao;
	
	
	@Override
	public HibernateDao<ScrapList, Long> getHibernateDao() {
		// TODO Auto-generated method stub
		return scrapListDao;
	}
	
	@Override
	public String getEntityListCode() {
		// TODO Auto-generated method stub
		return ScrapList.ENTITY_LIST_CODE;
	}

	@Override
	public Class<ScrapList> getEntityInstanceClass() {
		// TODO Auto-generated method stub
		return ScrapList.class;
	}

	@Override
	public String getWorkflowDefinitionCode() {
		// TODO Auto-generated method stub
		return "gsm_scraplist";
	}

	@Override
	public String getWorkflowDefinitionName() {
		// TODO Auto-generated method stub
		return "报废单流程";
	}
	 /**
     * 方法名: 导出报告
     * <p>功能说明：</p>
     * @return
    */
   public void exportReport(Long entityId) throws IOException {
       exportReport(entityId,"gsm_scraplist.xls","检测设备报废单");
   }

}
