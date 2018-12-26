package com.ambition.spc.jlanalyse.web;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.spc.entity.BsRules;
import com.ambition.spc.entity.ControlLimit;
import com.ambition.spc.entity.QualityFeature;
import com.ambition.spc.jlanalyse.entity.JLOriginalData;
import com.ambition.spc.jlanalyse.service.ControlLimitManager;
import com.ambition.spc.jlanalyse.service.JlanalyseDrawManager;
import com.ambition.spc.jlanalyse.util.JLcalculator;
import com.ambition.spc.processdefine.service.QualityFeatureManager;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;

/**    
 * JlanalyseDrawAction.java(画计量图形ACTION)
 * @authorBy wanglf
 *
 */
@Namespace("/spc/jl-analyse")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "spc/jl-analyse", type = "redirectAction") })
public class JlanalyseDrawAction extends CrudActionSupport<BsRules> {

	private static final long serialVersionUID = 1L;
	private Long id;
	private BsRules bsRules;
	private Page<BsRules> page = new Page<BsRules>(Page.EACH_PAGE_TEN, true);
	private String deleteIds;
	private JSONObject params;
	@Autowired
	private QualityFeatureManager qualityFeatureManager;
	@Autowired
	private ControlLimitManager controlLimitManager;
	@Autowired
	private JlanalyseDrawManager jlanalyseDrawManager;
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setPage(Page<BsRules> page) {
		this.page = page;
	}
	
	public Page<BsRules> getPage() {
		return page;
	}
	
	public BsRules getModel() {
		return bsRules;
	}
	
	public String getDeleteIds() {
		return deleteIds;
	}

	public void setDeleteIds(String deleteIds) {
		this.deleteIds = deleteIds;
	}

	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}

	@Override
	protected void prepareModel() throws Exception {
	}
	
	@Action("input")
	@Override
	public String input() throws Exception {
		return SUCCESS;
	}
	
	@Action("save")
	@Override
	public String save() throws Exception {
		return null;
	}
	
	@Override
	public String delete() throws Exception {
		return null;
	}

	@Override
	public String list() throws Exception {
		return null;
	}
	
	
	@Action("list-datas")
	public String getListDatas() throws Exception {
		return null;
	}
	
	/**
	 * 画控制图
	 * @return
	 * @throws Exception
	 */
	@Action("draw-control")
	public String drawControlPic() throws Exception {
		try{
			String featureId=Struts2Utils.getParameter("featureId");
			//封装显示规格线的参数
			Map<String,Object> showParamMap = new HashMap<String,Object>();
			String showGuiGe = Struts2Utils.getParameter("showGuiGe");
			if("1".equals(showGuiGe)){
				showParamMap.put("showGuiGe","1");
			}
			String showMaxAndMin = Struts2Utils.getParameter("showMaxAndMin");
			if("1".equals(showMaxAndMin)){
				showParamMap.put("showMaxAndMin","1");
			}
			jlanalyseDrawManager.drawControlPic(featureId,30,showParamMap);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 控制线页面
	 * @return
	 * @throws Exception
	 */
	@Action("update-cl")
	public String updateCl() throws Exception {
		String featureId=Struts2Utils.getParameter("featureId");
		QualityFeature qualityFeature=qualityFeatureManager.getQualityFeature(Long.parseLong(featureId));
		List<ControlLimit> controlLimits=controlLimitManager.getControlLimitDesc(Long.parseLong(featureId),null);
		JLcalculator jLcalculator=new JLcalculator();
		JLOriginalData originalData=new JLOriginalData();
		jlanalyseDrawManager.calculatJl(jLcalculator, featureId,null,null,originalData,"acquisition","30",new JSONArray(),null);
		//精度控制
		int n = 0;
		if(qualityFeature.getPrecs()!=null&&!qualityFeature.getPrecs().equals("")){
			n = Integer.parseInt(qualityFeature.getPrecs());
		}
		String digit="#.";
		for(int i=0;i<n;i++){
			digit=digit+"#";
		}
		DecimalFormat de=new DecimalFormat(digit);
		String isAutoCl="";
		if(qualityFeature.getIsAutoCl()!=null){
			isAutoCl=qualityFeature.getIsAutoCl();
		}
		if(isAutoCl.equals("Y")){//分析阶段需要控制精度
			if(qualityFeature.getControlChart()!=null){
			if(qualityFeature.getControlChart().equals("1")){
				ActionContext.getContext().put("scl", de.format(jLcalculator.getjLResult().getRCL()));
				ActionContext.getContext().put("sucl", de.format(jLcalculator.getjLResult().getRUCL()));
				ActionContext.getContext().put("slcl", de.format(jLcalculator.getjLResult().getRLCL()));
			}else if(qualityFeature.getControlChart().equals("2")){
				ActionContext.getContext().put("scl", de.format(jLcalculator.getjLResult().getSCL()));
				ActionContext.getContext().put("sucl", de.format(jLcalculator.getjLResult().getSUCL()));
				ActionContext.getContext().put("slcl", de.format(jLcalculator.getjLResult().getSLCL()));
			}else if(qualityFeature.getControlChart().equals("4")){
				ActionContext.getContext().put("scl", de.format(jLcalculator.getjLResult().getRCL()));
				ActionContext.getContext().put("sucl", de.format(jLcalculator.getjLResult().getRUCL()));
				ActionContext.getContext().put("slcl", de.format(jLcalculator.getjLResult().getRLCL()));
			}
			ActionContext.getContext().put("xcl", de.format(jLcalculator.getjLResult().getXCL()));
			ActionContext.getContext().put("xlcl", de.format(jLcalculator.getjLResult().getXLCL()));
			ActionContext.getContext().put("xucl", de.format(jLcalculator.getjLResult().getXUCL()));	
			}
		}else{//控制阶段不需要控制精度
			if(qualityFeature.getControlChart()!=null){
		if(qualityFeature.getControlChart().equals("1")){
			ActionContext.getContext().put("scl", jLcalculator.getjLResult().getRCL());
			ActionContext.getContext().put("sucl", jLcalculator.getjLResult().getRUCL());
			ActionContext.getContext().put("slcl", jLcalculator.getjLResult().getRLCL());
		}else if(qualityFeature.getControlChart().equals("2")){
			ActionContext.getContext().put("scl", jLcalculator.getjLResult().getSCL());
			ActionContext.getContext().put("sucl", jLcalculator.getjLResult().getSUCL());
			ActionContext.getContext().put("slcl", jLcalculator.getjLResult().getSLCL());
		}else if(qualityFeature.getControlChart().equals("4")){
			ActionContext.getContext().put("scl", jLcalculator.getjLResult().getRCL());
			ActionContext.getContext().put("sucl", jLcalculator.getjLResult().getRUCL());
			ActionContext.getContext().put("slcl", jLcalculator.getjLResult().getRLCL());
		}
		ActionContext.getContext().put("xcl", jLcalculator.getjLResult().getXCL());
		ActionContext.getContext().put("xlcl", jLcalculator.getjLResult().getXLCL());
		ActionContext.getContext().put("xucl", jLcalculator.getjLResult().getXUCL());
			}
		}
		ActionContext.getContext().put("isAuto", qualityFeature.getIsAutoCl());
		ActionContext.getContext().put("featureId", featureId);
		ActionContext.getContext().put("controlLimits", controlLimits);
		return "SUCCESS";
	}
	
	/**
	 * 保存控制线
	 * @return
	 * @throws Exception
	 */
	@Action("save-cl")
	public String saveCl() throws Exception {
		params=jlanalyseDrawManager.convertJsonObject(params);
		String featureId=params.getString("featureId");
		QualityFeature qualityFeature=qualityFeatureManager.getQualityFeature(Long.parseLong(featureId));
		qualityFeature.setIsAutoCl(params.getString("isAuto"));
		if(params.getString("isAuto").equals("N")){
		ControlLimit cl=new ControlLimit();
		cl.setCreatedTime(new Date());
		cl.setCompanyId(ContextUtils.getCompanyId());
		cl.setCreator(ContextUtils.getUserName());
		cl.setModifiedTime(new Date());
		cl.setModifier(ContextUtils.getUserName());
		cl.setXcl(params.getDouble("xcl"));
		cl.setXucl(params.getDouble("xucl"));
		cl.setXlcl(params.getDouble("xlcl"));
		cl.setScl(params.getDouble("scl"));
		cl.setSlcl(params.getDouble("slcl"));
		cl.setSucl(params.getDouble("sucl"));
	/*	List<ControlLimit> controlLimits=new ArrayList<ControlLimit>();
		controlLimits.add(cl);
		qualityFeature.setControlLimits(controlLimits);*/
		cl.setQualityFeature(qualityFeature);
		controlLimitManager.saveControlLimit(cl);
		}else{
			qualityFeatureManager.saveQualityFeature(qualityFeature);
		}
		return null;
	}
	
}
