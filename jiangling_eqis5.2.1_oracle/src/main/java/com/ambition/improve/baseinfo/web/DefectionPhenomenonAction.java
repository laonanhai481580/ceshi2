package com.ambition.improve.baseinfo.web;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.improve.baseinfo.service.DefectionPhenomenonManager;
import com.ambition.improve.baseinfo.service.ProblemDescribleManager;
import com.ambition.improve.entity.DefectionPhenomenon;
import com.ambition.improve.entity.ProblemDescrible;
import com.ambition.product.BaseAction;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;

@Namespace("/improve/base-info/problem-describle")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/improve/base-info/problem-describle", type = "redirectAction") })
public class DefectionPhenomenonAction extends BaseAction<DefectionPhenomenon>{

	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private DefectionPhenomenon defectionPhenomenon;
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private DefectionPhenomenonManager defectionPhenomenonManager;
	private Page<DefectionPhenomenon> page;
	private List<DefectionPhenomenon> list;
	private JSONObject params;
	private String problemType;// 
	private String businessUnitName;
	private String model;
	private File myFile;
 	//不良类别Id
	private Long problemDescribleId;
	@Autowired
	private ProblemDescribleManager problemDescribleManager;
	private ProblemDescrible problemDescrible;
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

	public DefectionPhenomenon getDefectionPhenomenon() {
		return defectionPhenomenon;
	}

	public void setDefectionPhenomenon(DefectionPhenomenon defectionPhenomenon) {
		this.defectionPhenomenon = defectionPhenomenon;
	}

	public Page<DefectionPhenomenon> getPage() {
		return page;
	}

	public void setPage(Page<DefectionPhenomenon> page) {
		this.page = page;
	}

	public List<DefectionPhenomenon> getList() {
		return list;
	}

	public void setList(List<DefectionPhenomenon> list) {
		this.list = list;
	}

	public Long getProblemDescribleId() {
		return problemDescribleId;
	}

	public void setProblemDescribleId(Long problemDescribleId) {
		this.problemDescribleId = problemDescribleId;
	}

	public ProblemDescrible getProblemDescrible() {
		return problemDescrible;
	}

	public void setProblemDescrible(ProblemDescrible problemDescrible) {
		this.problemDescrible = problemDescrible;
	}

	public File getMyFile() {
		return myFile;
	}

	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}
	

	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}

	
	public String getProblemType() {
		return problemType;
	}

	public void setProblemType(String problemType) {
		this.problemType = problemType;
	}

	public String getBusinessUnitName() {
		return businessUnitName;
	}

	public void setBusinessUnitName(String businessUnitName) {
		this.businessUnitName = businessUnitName;
	}

	public void setModel(String model) {
		this.model = model;
	}

	@Override
	public DefectionPhenomenon getModel() {
		return defectionPhenomenon;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id == null){
			defectionPhenomenon = new DefectionPhenomenon();
			if(problemDescribleId != null && problemDescribleId != 0){
				problemDescrible = problemDescribleManager.getProblemDescrible(problemDescribleId);
			}else{
				problemDescrible = null;
			}
			defectionPhenomenon.setCompanyId(ContextUtils.getCompanyId());
			defectionPhenomenon.setCreatedTime(new Date());
			defectionPhenomenon.setCreator(ContextUtils.getUserName());
			defectionPhenomenon.setProblemDescrible(problemDescrible);
			defectionPhenomenon.setLastModifiedTime(new Date());
			defectionPhenomenon.setLastModifier(ContextUtils.getUserName());
			defectionPhenomenon.setBusinessUnitName(ContextUtils.getSubCompanyName());
			defectionPhenomenon.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			defectionPhenomenon = defectionPhenomenonManager.getDefectionPhenomenon(id);
		}
	}
	
	@Action("input")
	@Override
	public String input() throws Exception {
		return SUCCESS;
	}
	@Action("save")
	@LogInfo(optType="保存",message="不良现象")
	@Override
	public String save() throws Exception {
		if(id != null && id != 0){
			defectionPhenomenon.setLastModifiedTime(new Date());
			defectionPhenomenon.setLastModifier(ContextUtils.getUserName());
			logUtilDao.debugLog("修改", defectionPhenomenon.toString());
		}else{
			logUtilDao.debugLog("保存", defectionPhenomenon.toString());
		}
		try {
			defectionPhenomenonManager.saveDefectionPhenomenon(defectionPhenomenon);
			renderText(JsonParser.object2Json(defectionPhenomenon));
		} catch (Exception e) {
			e.printStackTrace();
			createErrorMessage(e.getMessage());
		}
		return null;
	}
	
	@Action("delete")
	@LogInfo(optType="删除",message="不良现象")
	@Override
	public String delete() throws Exception {
		defectionPhenomenonManager.deleteDefectionPhenomenon(deleteIds);
		return null;
	}

	@Action("delete-subs")
	@LogInfo(optType="删除子级",message="不良现象")
	public String deleteSubs() throws Exception {
		if(problemDescribleId != null && problemDescribleId != 0){
			problemDescrible = problemDescribleManager.getProblemDescrible(problemDescribleId);
		}else{
			problemDescrible = null;
		}
		list = defectionPhenomenonManager.listAll(problemDescrible);
		for(DefectionPhenomenon defectionPhenomenon: list){
			defectionPhenomenonManager.deleteDefectionPhenomenon(defectionPhenomenon);
		}
		return null;
	}
	
	@Action("search-subs")
	public String searchSubs() throws Exception {
		if(problemDescribleId != null && problemDescribleId != 0){
			problemDescrible = problemDescribleManager.getProblemDescrible(problemDescribleId);
		}else{
			problemDescrible = null;
		}
		list = defectionPhenomenonManager.listAll(problemDescrible);
		if(list.size() >= 1){
			renderText("have");
		}else{
			renderText("no");
		}
		return null;
	}
	
	@Action("list")
	@Override
	public String list() throws Exception {
		renderMenu();
		if(problemDescribleId != null && problemDescribleId != 0){
			problemDescrible = problemDescribleManager.getProblemDescrible(problemDescribleId);
		}else{
			problemDescrible = null;
		}
		return SUCCESS;
	}
	
	@Action("list-datas")
	public String getListDatas() throws Exception{
		String myId = Struts2Utils.getParameter("problemDescribleId");
		if(myId != null && !myId.equals("")){
			problemDescribleId = Long.valueOf(myId);
			problemDescrible = problemDescribleManager.getProblemDescrible(problemDescribleId);
		}
		String code = null;
		try {
			code = Struts2Utils.getParameter("defectionPhenomenon");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(code != null && !code.equals("")){
			page = defectionPhenomenonManager.list(page, code);
		}else{
			page = defectionPhenomenonManager.list(page, problemDescrible);
		}		
		renderText(PageUtils.pageToJson(page));
		logUtilDao.debugLog("查询", "质量改进管理：基础设置-不良现象维护");
		return null;
	}
	@Action("exportCode2")
	@LogInfo(optType="导出",message="不良现象")
	public String export() throws Exception {
		Page<DefectionPhenomenon> page = new Page<DefectionPhenomenon>(100000);
		String myId = Struts2Utils.getParameter("problemDescribleId");
		if(myId != null && !myId.equals("")){
			problemDescribleId = Long.valueOf(myId);
			problemDescrible = problemDescribleManager.getProblemDescrible(problemDescribleId);
		}
		page = defectionPhenomenonManager.list(page, problemDescrible);
		renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"IMP_DEFECTION_PHENOMENON"),"defectionPhenomenon"));
		logUtilDao.debugLog("导出", "质量改进管理：基础设置-不良现象维护");
		return null;
	}
	@Action("imports")
	public String imports() throws Exception {
		return "imports";
	}
	
	@Action("import-excel-datas")
	@LogInfo(optType="导入",message="不良现象")
	public String importDatas() throws Exception {
		try {
			if(myFile != null){
				renderHtml(defectionPhenomenonManager.importFile(myFile));
			}
		} catch (Exception e) {
			renderHtml("导入失败:<font color=red>" + e.getMessage() + "</font>");
		}
		return null;
	}
	/**
	 * 问题点弹框选择
	 * @param component
	 * @return
	 */
	@Action("problem-point-select")
	public String postionBomSelect() throws Exception {
		String businessUnitName=Struts2Utils.getParameter("businessUnitName");
		String problemType=Struts2Utils.getParameter("problemType");
		String model=Struts2Utils.getParameter("model");
		this.setBusinessUnitName(businessUnitName);
		this.setProblemType(problemType);
		this.setModel(model);
		return SUCCESS;
	}
	@Action("problem-list-datas")
	public String getPositionCodeByParent() throws Exception {
		params = convertJsonObject(params);
		String businessUnitName=Struts2Utils.getParameter("businessUnitName");
		String problemType=Struts2Utils.getParameter("problemType");
		String model=Struts2Utils.getParameter("model");
		page = defectionPhenomenonManager.listByParams(page,businessUnitName,problemType,model);
		try {
			Page<Object> resultPage = new Page<Object>();
			resultPage.setOrder(page.getOrder());
			resultPage.setOrderBy(page.getOrderBy());
			resultPage.setPageNo(page.getPageNo());
			resultPage.setPageSize(page.getPageSize());
			resultPage.setTotalCount(page.getTotalCount());
			List<Object> list = new ArrayList<Object>();
			for(DefectionPhenomenon defectionPhenomenon : page.getResult()){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("id",defectionPhenomenon.getId());
				map.put("defectionPhenomenonName",defectionPhenomenon.getDefectionPhenomenonName());
				map.put("remark",defectionPhenomenon.getRemark());
				list.add(map);
			}
			resultPage.setResult(list);
			renderText(PageUtils.pageToJson(resultPage));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
     * 转换json格式
     */
    private JSONObject convertJsonObject(JSONObject params){
        JSONObject resultJson = new JSONObject();
        if(params == null){
            return resultJson;
        }else{
            for(Object key : params.keySet()){
                resultJson.put(key,params.getJSONArray(key.toString()).get(0));
            }
            return resultJson;
        }
    }
}
