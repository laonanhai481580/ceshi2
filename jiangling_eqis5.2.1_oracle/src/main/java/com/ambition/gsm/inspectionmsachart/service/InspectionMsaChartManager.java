package com.ambition.gsm.inspectionmsachart.service;

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

import com.ambition.gsm.entity.InspectionMsaplan;
import com.ambition.gsm.inspectionmsachart.dao.InspectionMsaChartDao;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.JsonParser;

/**
 * 
 * 类名: InspectionChartManager
 * <p>
 * amb
 * </p>
 * <p>
 * 厦门安必兴信息科技有限公司
 * </p>
 * <p>
 * 功能说明：设备设施service
 * </p>
 * 
 * @author 刘承斌
 * @version 1.00 2014-5-21 下午2:45:40 发布
 */

@Service
@Transactional
public class InspectionMsaChartManager {
	@Autowired
	private InspectionMsaChartDao inspectionMsaChartDao;

	public InspectionMsaplan getInspectionPlan(Long id) {
		return inspectionMsaChartDao.get(id);
	}

	public void saveInspectionPlan(InspectionMsaplan inspectionRecord) {
		inspectionMsaChartDao.save(inspectionRecord);
	}

	public void InspectionMsaplan(InspectionMsaplan inspectionMsaPlan) {
		inspectionMsaChartDao.delete(inspectionMsaPlan);
	}

	public void deleteInspectionMsaPlan(String deleteIds) {
		String[] ids = deleteIds.split(",");
		for (String id : ids) {
			inspectionMsaChartDao.delete(Long.valueOf(id));
		}
	}

	public List<InspectionMsaplan> listActual(Date startDate, Date endDate) {
		return inspectionMsaChartDao.getActualInspectionPlan(startDate, endDate);
	}
	public Page<InspectionMsaplan> listActual(Page<InspectionMsaplan> page,Date startDate, Date endDate) {
		return inspectionMsaChartDao.getActualInspectionPlan(page,startDate, endDate);
	}
	// 封装结果数据集的JSON格式
	public String getResultJson(Page<InspectionMsaplan> page) {
		List<JSONObject> list = new ArrayList<JSONObject>();
		for (InspectionMsaplan gs : page.getResult()) {
			StringBuffer sb = new StringBuffer();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			HashMap<String, Object> hs = new HashMap<String, Object>();
			sb.append(JsonParser.object2Json(gs));
			sb.delete(sb.length() - 1, sb.length());
			sb.append(",");
			/*sb.append(JsonParser.object2Json(gs.getMsaLastedDate()).substring(1,JsonParser.object2Json(gs.getMsaLastedDate()).length()));*/
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

	public List<InspectionMsaplan> listPlan(Date startdate, Date enddate1) {
		return inspectionMsaChartDao.getPlanInspectionPlan(startdate, enddate1);
	}
	public  Page<InspectionMsaplan> listPlan( Page<InspectionMsaplan> page,Date startDate,Date endDate){
		return inspectionMsaChartDao.getPlanInspectionRecord(page,startDate,endDate);
	}
	public List<InspectionMsaplan> listInspectionPlan(Date startDate, Date endDate) {
		return inspectionMsaChartDao.getPlanInspectionPlan(startDate, endDate);
	}

	public List<InspectionMsaplan> listQualifiedActual(Date startDate, Date endDate ) {
		return inspectionMsaChartDao.getQualifiedActualInspectionRecord(startDate,endDate);
	}
	public Page<InspectionMsaplan> listQualifiedActual(Page<InspectionMsaplan> page,Date startDate, Date endDate) {
		return inspectionMsaChartDao.getPlanInspectionRecord(page,startDate,endDate);
	}

}
