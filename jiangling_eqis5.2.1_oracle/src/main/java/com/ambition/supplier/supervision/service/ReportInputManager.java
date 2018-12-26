package com.ambition.supplier.supervision.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.supplier.entity.ReportInput;
import com.ambition.supplier.supervision.dao.ReportInputDao;
import com.norteksoft.acs.base.utils.log.LogUtilDao;

/**
 * 稽查计划台帐
 * @author 赵骏
 *
 */
@Service
@Transactional
public class ReportInputManager {

	@Autowired
	private ReportInputDao reportInputDao;
	
	@Autowired
	private LogUtilDao logUtilDao;
	
	/**
	 * 保存评价项目
	 * @param checkPlan
	 */
	public void saveReportInput(ReportInput reportInput){
		reportInputDao.save(reportInput);
		logUtilDao.debugLog("保存", reportInput.toString());
	}

}
