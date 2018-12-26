package com.ambition.gsm.inspectionchart.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.gsm.entity.InspectionPlan;
import com.ambition.gsm.inspectionchart.dao.InspectionChartDao;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.JsonParser;

/**
 * 量检具统计分析(SERVICE)
 * @author 刘承斌
 *
 */
@Service
@Transactional
public class InspectionChartManager {
	@Autowired
	private InspectionChartDao inspectionChartDao;

	/**
	 * 获取对象
	 * @param id
	 * @return
	 */
	public InspectionPlan getInspectionPlan(Long id) {
		return inspectionChartDao.get(id);
	}

	/**
	 * 保存对象
	 * @param inspectionRecord
	 */
	public void saveInspectionPlan(InspectionPlan inspectionRecord) {
		inspectionChartDao.save(inspectionRecord);
	}

	/**
	 * 删除对象
	 * @param inspectionPlan
	 */
	public void InspectionPlan(InspectionPlan inspectionPlan) {
		inspectionChartDao.delete(inspectionPlan);
	}

	/**
	 * 删除多对象
	 * @param deleteIds
	 */
	public void deleteInspectionPlan(String deleteIds) {
		String[] ids = deleteIds.split(",");
		for (String id : ids) {
			inspectionChartDao.delete(Long.valueOf(id));
		}
	}

	/**
	 * 所有对象
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<InspectionPlan> listActual(Date startDate, Date endDate) {
		return inspectionChartDao.getActualInspectionPlan(startDate, endDate);
	}
	
	/**
	 * 分页对象
	 * @param page
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public Page<InspectionPlan> listActual(Page<InspectionPlan> page,Date startDate, Date endDate) {
		return inspectionChartDao.getActualInspectionPlan(page,startDate, endDate);
	}
	
	/**
	 * 封装结果数据集的JSON格式
	 * @param page
	 * @return
	 */
	public String getResultJson(Page<InspectionPlan> page) {
		List<JSONObject> list = new ArrayList<JSONObject>();
		for (InspectionPlan gs : page.getResult()) {
			StringBuffer sb = new StringBuffer();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			HashMap<String, Object> hs = new HashMap<String, Object>();
			if (gs.getInspectionPlanDate() != null) {
				hs.put("inspectionPlanDate", sdf.format(gs.getInspectionPlanDate()));
			}
			if (gs.getActualInspectionDate() != null) {
				hs.put("actualInspectionDate", sdf.format(gs.getActualInspectionDate()));
			}
			sb.append(JsonParser.object2Json(gs));
			sb.delete(sb.length() - 1, sb.length());
			sb.append(",");
			sb.append(JsonParser.object2Json(gs.getInspectionLastedDate()).substring(1,JsonParser.object2Json(gs.getInspectionLastedDate()).length()));
			sb.delete(sb.length() - 1, sb.length());
			sb.append(",");
			sb.append(JsonParser.object2Json(hs).substring(1,JsonParser.object2Json(hs).length()));
			JSONObject jObject = JSONObject.fromObject(sb.toString());
			list.add(jObject);
		}
		// 添加jqGrid所需的页信息
		StringBuilder json = new StringBuilder();
		json.append("{\"page\":\"");
		json.append(page.getPageNo());
		json.append("\",\"total\":");
		json.append(page.getTotalPages());
		json.append(",\"records\":\"");
		json.append(page.getTotalCount());
		json.append("\",\"rows\":");
		json.append(JSONArray.fromObject(list).toString());
		json.append("}");
		return json.toString();
	}

	/**
	 * 所有对象
	 * @param startdate
	 * @param enddate1
	 * @return
	 */
	public List<InspectionPlan> listPlan(Date startdate, Date enddate1) {
		return inspectionChartDao.getPlanInspectionPlan(startdate, enddate1);
	}
	
	/**
	 * 分页对象
	 * @param page
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public  Page<InspectionPlan> listPlan( Page<InspectionPlan> page,Date startDate,Date endDate){
		return inspectionChartDao.getPlanInspectionRecord(page,startDate,endDate);
	}
	
	/**
	 * 所有对象
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<InspectionPlan> listInspectionPlan(Date startDate, Date endDate) {
		return inspectionChartDao.getPlanInspectionPlan(startDate, endDate);
	}

	/**
	 * 所有对象
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<InspectionPlan> listQualifiedActual(Date startDate, Date endDate ) {
		return inspectionChartDao.getQualifiedActualInspectionRecord(startDate,endDate);
	}
	
	/**
	 * 分页对象
	 * @param page
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public Page<InspectionPlan> listQualifiedActual(Page<InspectionPlan> page,Date startDate, Date endDate) {
		return inspectionChartDao.getQualifiedActualInspectionRecord(page,startDate,endDate);
	}

}
