package com.ambition.spc.abnormal.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.spc.abnormal.service.AbnormalInfoManager;
import com.ambition.spc.dataacquisition.service.SpcSubGroupManager;
import com.ambition.spc.entity.AbnormalInfo;
import com.ambition.spc.entity.QualityFeature;
import com.ambition.spc.entity.SpcSubGroup;
import com.ambition.spc.importutil.service.SpcImportManager;
import com.ambition.spc.processdefine.service.QualityFeatureManager;
import com.ambition.util.common.DateUtil;
import com.norteksoft.mms.base.MmsUtil;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;

/**    
 * AbnormalInfoAction.java
 * @authorBy YUKE
 *
 */
@Namespace("/spc/process-monitor")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "spc/process-monitor", type = "redirectAction") })
public class AbnormalInfoAction extends CrudActionSupport<AbnormalInfo>{

	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private AbnormalInfo abnormalInfo;
	private QualityFeature qualityFeature;
	private Page<AbnormalInfo> page;
	private JSONObject params;
	@Autowired
	private SpcImportManager spcImportManager;
	@Autowired
	private AbnormalInfoManager abnormalInfoManager;
	@Autowired
	private QualityFeatureManager qualityFeatureManager;
	@Autowired
	private SpcSubGroupManager spcSubGroupManager;
	@Autowired
	private MmsUtil mmsUtil;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDeleteIds() {
		return deleteIds;
	}

	public void setDeleteIds(String deleteIds) {
		this.deleteIds = deleteIds;
	}

	public AbnormalInfo getAbnormalInfo() {
		return abnormalInfo;
	}

	public void setAbnormalInfo(AbnormalInfo abnormalInfo) {
		this.abnormalInfo = abnormalInfo;
	}

	public QualityFeature getQualityFeature() {
		return qualityFeature;
	}

	public void setQualityFeature(QualityFeature qualityFeature) {
		this.qualityFeature = qualityFeature;
	}

	public Page<AbnormalInfo> getPage() {
		return page;
	}

	public void setPage(Page<AbnormalInfo> page) {
		this.page = page;
	}

	@Override
	public AbnormalInfo getModel() {
		return abnormalInfo;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			abnormalInfo = new AbnormalInfo();
			abnormalInfo.setCompanyId(ContextUtils.getCompanyId());
			abnormalInfo.setCreator(ContextUtils.getUserName());
			abnormalInfo.setCreatedTime(new Date());
			abnormalInfo.setModifier(ContextUtils.getUserName());
			abnormalInfo.setModifiedTime(new Date());
		}else{
			abnormalInfo = abnormalInfoManager.getAbnormalInfo(id);
		}
	}

	@Action("info-delete")
	@Override
	public String delete() throws Exception {
		try {
			abnormalInfoManager.deleteAbnormalInfo(deleteIds);
		} catch (Exception e) {
			e.printStackTrace();
			addActionMessage("删除失败:" + e.getMessage());
		}
		return null;
	}

	@Override
	public String input() throws Exception {
		return SUCCESS;
	}

	@Override
	public String list() throws Exception {
		
		return SUCCESS;
	}

	@Override
	public String save() throws Exception {
		return null;
	}
	
	@Action("list-datas")
	public String getListDatas() throws Exception {
		try {
			page = abnormalInfoManager.searchByPage(page,params);
			this.renderText(PageUtils.pageToJson(page,"SPC_ABNORMAL_INFO"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Action("feature-exception-message")
	public String getFeatureExceptionMessage() throws Exception {
		ActionContext.getContext().put("featureId",Struts2Utils.getParameter("featureId"));
		ActionContext.getContext().put("startDateStr",Struts2Utils.getParameter("startDateStr"));
		ActionContext.getContext().put("endDateStr",Struts2Utils.getParameter("endDateStr"));
		ActionContext.getContext().put("lastAmout",Struts2Utils.getParameter("lastAmout"));
		return SUCCESS;
	}
	
	@Action("feature-list-datas")
	public String getFeatureListDatas() throws Exception {
		try {
			String featureIdStr = Struts2Utils.getParameter("featureId");
			String startDateStr = Struts2Utils.getParameter("startDateStr");
			String endDateStr = Struts2Utils.getParameter("endDateStr");
			String lastAmoutStr =  Struts2Utils.getParameter("lastAmout");
			page = abnormalInfoManager.getPage(page,StringUtils.isEmpty(featureIdStr)?null:Long.valueOf(featureIdStr),
					StringUtils.isEmpty(startDateStr)?null:DateUtil.parseDate(startDateStr + " 00:00:00","yyyy-MM-dd HH:mm:ss"),
					StringUtils.isEmpty(endDateStr)?null:DateUtil.parseDate(endDateStr + " 23:23:59","yyyy-MM-dd HH:mm:ss"),
					StringUtils.isEmpty(lastAmoutStr)?null:Integer.valueOf(lastAmoutStr));
			this.renderText(PageUtils.pageToJson(page,"SPC_ABNORMAL_INFO"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@Action("lanch-abnormal")
	public String lanchAbnormal() throws Exception {
		//测试自动保存数据
//		testValues();
		return null;
//		String featureId = Struts2Utils.getParameter("featureId");
//		String number = Struts2Utils.getParameter("num");
//		String queryHist = Struts2Utils.getParameter("queryHist");//查询标识
//		qualityFeature = qualityFeatureManager.getQualityFeature(Long.valueOf(featureId));
//		
//		//根据条件查询10天前至今采集的数据/历史所有数据
//		List<SpcSubGroup> list = abnormalInfoManager.querySpcSubGroupByQuality(qualityFeature);
//		if(StringUtils.isNotEmpty(queryHist)){
//			for (int i = 0; i < list.size(); i++) {
//				List<SpcSubGroup> listAll=abnormalInfoManager.querySpcSubGroupByQualityTime(qualityFeature,list.get(i).getSubGroupOrderNum());
//				//根据规则检测所查询的数据  异常报警
//				abnormalInfoManager.lanchAbnormal(list.get(i).getSubGroupOrderNum()+"", qualityFeature,listAll,null);
//				//更改处理状态
//				SpcSubGroup group = (SpcSubGroup)abnormalInfoManager.findSpcSubGroupByNum(qualityFeature,Integer.valueOf(list.get(i).getSubGroupOrderNum()));
//				group.setJudgeState(1);
//				spcSubGroupManager.saveSpcSubGroup(group);
//			}
//		}else{
//			List<SpcSubGroup> listAll=abnormalInfoManager.querySpcSubGroupByQualityTime(qualityFeature,Integer.valueOf(number));
//			//根据规则检测所查询的数据  异常报警
//			abnormalInfoManager.lanchAbnormal(number, qualityFeature, listAll,null);
//			//更改处理状态
//			SpcSubGroup group = (SpcSubGroup)abnormalInfoManager.findSpcSubGroupByNum(qualityFeature,Integer.valueOf(number));
//			group.setJudgeState(1);
//			spcSubGroupManager.saveSpcSubGroup(group);
//		}
//		
//		
//		return null;
	}
	

	@Action("exports")
	public String exports() throws Exception{
		page = new Page<AbnormalInfo>(65535);
		String featureIdStr = Struts2Utils.getParameter("featureId");
		String startDateStr = Struts2Utils.getParameter("startDateStr");
		String endDateStr = Struts2Utils.getParameter("endDateStr");
		String lastAmoutStr =  Struts2Utils.getParameter("lastAmout");
		page = abnormalInfoManager.getPage(page,StringUtils.isEmpty(featureIdStr)?null:Long.valueOf(featureIdStr),
				StringUtils.isEmpty(startDateStr)?null:DateUtil.parseDate(startDateStr + " 00:00:00","yyyy-MM-dd HH:mm:ss"),
				StringUtils.isEmpty(endDateStr)?null:DateUtil.parseDate(endDateStr + " 23:23:59","yyyy-MM-dd HH:mm:ss"),
				StringUtils.isEmpty(lastAmoutStr)?null:Integer.valueOf(lastAmoutStr));
		this.renderText(ExcelExporter.export(mmsUtil.getExportData(page, "SPC_ABNORMAL_INFO"),"异常消息"));
		return null;
	}

	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}
	
	private void testValues(){
		List<Object[]> testParams = new ArrayList<Object[]>();
		testParams.add(new Object[]{17085548,10,10.1});
//		testParams.add(new Object[]{17085659,9.8,10.2});
//		testParams.add(new Object[]{17085872,191.5,194.5});
//		testParams.add(new Object[]{17085874,191.5,194.5});
//		testParams.add(new Object[]{17085955,2.5,3.5});
//		testParams.add(new Object[]{17199183,14,16});
		for(final Object[] objs : testParams){
			Thread thread = new Thread(new Runnable() {
				public void run() {
					Object[] tempObjs = objs;
					Long featueId = Long.valueOf(tempObjs[0].toString());
					Double min = Double.valueOf(objs[1].toString());
					Double max = Double.valueOf(objs[2].toString());
					
					Integer between = Double.valueOf((max - min)*100).intValue();
					Map<String,String> layerMap = new HashMap<String,String>();
					for(int i=0;i<10000;i++){
						Double randValue = RandomUtils.nextInt(between)/100.0;
						layerMap.put("info1",RandomUtils.nextInt(10)+"");
						layerMap.put("info2",RandomUtils.nextInt(10)+"");
						layerMap.put("info3",RandomUtils.nextInt(10)+"");
						spcImportManager.backImportValues(featueId,null, randValue,layerMap);
//						System.out.println(Thread.currentThread().getId() + "test:" + i);
					}
				}
			});
			thread.start();
		}
	}
}
