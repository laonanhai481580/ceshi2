package com.ambition.si.checkinspection.service;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.si.checkinspection.dao.SiCheckInspectionReportDao;
import com.ambition.si.entity.SiCheckInspectionReport;
import com.ambition.si.entity.SiCheckItem;
import com.ambition.util.common.CommonUtil1;
import com.ambition.util.common.ExcelUtil;
import com.ambition.util.exportexcel.ExportExcelFormatter;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.JsonParser;

/**    
* 检验报告SERVICE
* @authorBy lpf
*
*/
@Service
@Transactional
public class SiCheckInspectionReportManager {
	@Autowired
	private  SiCheckInspectionReportDao siCheckInspectionReportDao;
	Logger log = Logger.getLogger(this.getClass());
	public int deleteSiCheckInspectionReport(String deleteIds) {
		int delCount = 0;
		String[] ids = deleteIds.split(",");
		for(String id :ids){
			if(id != null){
				SiCheckInspectionReport siCheckInspectionReport = getSiCheckInspectionReport(Long.valueOf(id));
				if(siCheckInspectionReport.getCreator()!=null&&siCheckInspectionReport.getCreator().equals(ContextUtils.getLoginName())){
					siCheckInspectionReportDao.delete(siCheckInspectionReport);
					delCount++;
				}
				
			}
		}
		return delCount;
	}
	
	public SiCheckInspectionReport getSiCheckInspectionReport(Long id){
		return siCheckInspectionReportDao.get(id);
	}
	
	public void saveSiCheckInspectionReport(SiCheckInspectionReport siCheckInspectionReport){
		siCheckInspectionReportDao.save(siCheckInspectionReport);
	}
	public Page<SiCheckInspectionReport> list(Page<SiCheckInspectionReport> page){
		return siCheckInspectionReportDao.search(page);
	}
	//封装不良细项结果数据集的JSON格式
	public String getResultJson(Page<SiCheckInspectionReport> page,String countNamePrex) {
		List<JSONObject> list = new ArrayList<JSONObject>();
		for(SiCheckInspectionReport cp : page.getResult()){
			JSONObject json = JSONObject.fromObject(JsonParser.object2Json(cp));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(cp.getInspectionDate()!=null){
				String inspectionDate=sdf.format(cp.getInspectionDate());
				json.put("inspectionDate", inspectionDate);
			}
			list.add(json);
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

	// 封装保存后的信息(处理保存后日期为空)
	public String getOneResultJson(SiCheckInspectionReport siCheckInspectionReport) {
		HashMap<String, Object> hs = new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (siCheckInspectionReport.getInspectionDate() != null) {
			String inspectionDate = sdf.format(siCheckInspectionReport.getInspectionDate());
			hs.put("inspectionDate", inspectionDate);
		}
		StringBuffer sb = new StringBuffer();
		sb.append(JsonParser.object2Json(siCheckInspectionReport));
		sb.delete(sb.length() - 1, sb.length());
		sb.append(",");
		sb.append(JsonParser.object2Json(hs).substring(1, JsonParser.object2Json(hs).length()));
		return sb.toString();
	}
	public SiCheckInspectionReport save(SiCheckInspectionReport report,String zb){
		if(StringUtils.isNotEmpty(zb)){
			JSONArray childrenInfo = JSONArray.fromObject(zb);
			if(report.getCheckItems()==null){
				report.setCheckItems(new ArrayList<SiCheckItem>());
			}else{
				report.getCheckItems().clear();
			}
			for (int i = 0; i < childrenInfo.size(); i++) {
				JSONObject js = childrenInfo.getJSONObject(i);
				SiCheckItem tr = new SiCheckItem();
				tr.setCompanyId(ContextUtils.getCompanyId());
				tr.setCreatedTime(new Date());
				tr.setCreator(ContextUtils.getLoginName());
				tr.setCreatorName(ContextUtils.getUserName());
				tr.setModifiedTime(new Date());
				tr.setModifier(ContextUtils.getLoginName());
				tr.setModifierName(ContextUtils.getUserName());
				tr.setInspectionDate(report.getInspectionDate());
				tr.setSiCheckInspectionReport(report);
				for(Object key : js.keySet()){
					if(key != null && js.get(key) != null){
						try{
							CommonUtil1.setProperty(tr, key.toString(), js.get(key));
						}catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				report.getCheckItems().add(tr);
			}
		}
		return report;
	}
	 /**
     * 方法名:导出表单
     * <p>功能说明：</p>
     * @param incomingInspectionActionsReport
     * @throws IOException
    */
   public void exportReport(SiCheckInspectionReport s) throws IOException{
       InputStream inputStream = null;
       try {
    	   SiCheckInspectionReport report = s;                                                
           inputStream = this.getClass().getClassLoader().getResourceAsStream("template/report/si-report.xls");
           Map<String,ExportExcelFormatter> formatterMap = new HashMap<String, ExportExcelFormatter>();
           //单号格式化
           formatterMap.put("code",new ExportExcelFormatter() {
			@Override
			public String format(Object value, int rowNum, String fieldName,
					Cell cell) {
				SiCheckInspectionReport siCheckInspectionReport = (SiCheckInspectionReport)value;
                 return "编号:" + siCheckInspectionReport.getFormNo();
			}
           });
        
           String exportFileName = "现场检验报告";
           ExcelUtil.exportToExcel(inputStream, exportFileName, report, formatterMap);
       }catch (Exception e) {
           log.error("导出失败!",e);
       } finally{
           if(inputStream != null){
               inputStream.close();
           }
       }
   }
}
