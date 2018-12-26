package com.ambition.supplier.admittance.web;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.product.BaseAction;
import com.ambition.supplier.entity.CheckGrade;
import com.ambition.supplier.entity.CheckGradeType;
import com.ambition.supplier.supervision.service.CheckGradeManager;
import com.ambition.supplier.supervision.service.CheckGradeTypeManager;
import com.ambition.util.common.CommonUtil;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;

/**
 * 考察评分项目维护
 * @author 赵骏
 *
 */
@Namespace("/supplier/admittance/survey-grade")
@ParentPackage("default")
public class SurveyGradeAction  extends BaseAction<CheckGrade> {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long parentId;
	private Long checkGradeTypeId;
	private String name;
	private Integer orderNum;
	private String position;
	private String deleteIds;//删除的编号 
	
	private CheckGrade checkGrade;//评分项目
	
	private Page<CheckGrade> page;
	
 	private JSONObject params;

 	@Autowired
 	private CheckGradeTypeManager checkGradeTypeManager;

 	@Autowired
 	private CheckGradeManager checkGradeManager;
 	
	public Long getCheckGradeTypeId() {
		return checkGradeTypeId;
	}

	public void setCheckGradeTypeId(Long checkGradeTypeId) {
		this.checkGradeTypeId = checkGradeTypeId;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

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

	public CheckGrade getCheckGrade() {
		return checkGrade;
	}

	public void setCheckGrade(CheckGrade checkGrade) {
		this.checkGrade = checkGrade;
	}

	public Page<CheckGrade> getPage() {
		return page;
	}

	public void setPage(Page<CheckGrade> page) {
		this.page = page;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}

	public CheckGrade getModel() {
		return checkGrade;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			checkGrade = new CheckGrade();
			checkGrade.setType(CheckGradeType.TYPE_SURVEY);
			checkGrade.setCreatedTime(new Date());
			checkGrade.setCompanyId(ContextUtils.getCompanyId());
			checkGrade.setCreator(ContextUtils.getUserName());
			checkGrade.setLastModifiedTime(new Date());
			checkGrade.setLastModifier(ContextUtils.getUserName());
			checkGrade.setLastModifier(ContextUtils.getUserName());
			checkGrade.setBusinessUnitName(ContextUtils.getSubCompanyName());
			checkGrade.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
			if(checkGradeTypeId != null){
				checkGrade.setCheckGradeType(checkGradeTypeManager.getCheckGradeType(checkGradeTypeId));
			}
		}else {
			checkGrade = checkGradeManager.getCheckGrade(id);
		}
	}
	
	@Action("save")
	@Override
	public String save() throws Exception {
		try{
			checkGradeManager.storeCheckGrade(checkGrade);
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("id",checkGrade.getId());
			map.put("orderNum", checkGrade.getOrderNum());
			createMessage("保存成功!",map);
		}catch(Exception e){
			createErrorMessage("保存失败：" + e.getMessage());
		}
		return null;
	}
	
	@Action("delete")
	@Override
	public String delete() throws Exception {
		if(StringUtils.isEmpty(deleteIds)){
			createErrorMessage("删除的对象不存在!");
		}else{
			try {
				checkGradeManager.deleteCheckGrade(deleteIds);
				createMessage("删除成功!");
			} catch (Exception e) {
				createErrorMessage("删除失败:" + e.getMessage());
			}
		}
		return null;
	}

	@Action("move-survey-grade")
	public String moveCheckGrade() throws Exception {
		try {
			checkGrade = checkGradeManager.getCheckGrade(id);
			checkGradeManager.moveCheckGrade(checkGrade,position,orderNum);
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("orderNum",checkGrade.getOrderNum());
			createMessage("操作成功!",map);
		} catch (Exception e) {
			createErrorMessage("保存失败:" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	@Action("input")
	public String input() throws Exception {
		createErrorMessage("操作失败!");
		return null;
	}
	
	@Action("list")
	@Override
	public String list() throws Exception {
		return SUCCESS;
	}
	
	@Action("survey-grade-datas")
	public String getCheckGradeDatas() throws Exception {
		Map<String,Object> result = new HashMap<String, Object>();
		try {
			CheckGradeType checkGradeType = checkGradeTypeManager.getCheckGradeType(id);
			List<Object> rows = new ArrayList<Object>();
			for(CheckGrade checkGrade : checkGradeType.getCheckGrades()){
				Map<String,Object> obj = new HashMap<String, Object>();
				obj.put("id",checkGrade.getId());
				obj.put("name",checkGrade.getName());
				obj.put("weight",checkGrade.getWeight());
				obj.put("orderNum",checkGrade.getOrderNum());
				obj.put("checkGradeTypeId",checkGradeType.getId());
				obj.put("checkGradeTypeName",checkGradeType.getName());
				rows.add(obj);
			}
			result.put("rows",rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		renderText(JSONObject.fromObject(result).toString());
		return null;
	}
	
	@Action("store-survey-grade-type")
	public String storeCheckGradeType() throws Exception {
		CheckGradeType checkGradeType = null;
		if(id == null){
			checkGradeType = new CheckGradeType();
			checkGradeType.setType(CheckGradeType.TYPE_SURVEY);
			checkGradeType.setCreatedTime(new Date());
			checkGradeType.setCompanyId(ContextUtils.getCompanyId());
			checkGradeType.setCreator(ContextUtils.getUserName());
			checkGradeType.setLastModifiedTime(new Date());
			checkGradeType.setLastModifier(ContextUtils.getUserName());
			if(parentId != null){
				checkGradeType.setParent(checkGradeTypeManager.getCheckGradeType(parentId));
				checkGradeType.setLevel(checkGradeType.getParent().getLevel()+1);
			}
		}else{
			checkGradeType = checkGradeTypeManager.getCheckGradeType(id);
			checkGradeType.setLastModifiedTime(new Date());
			checkGradeType.setLastModifier(ContextUtils.getUserName());
		}
		checkGradeType.setName(name);
		try {
			checkGradeTypeManager.storeCheckGradeType(checkGradeType);
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("id",checkGradeType.getId());
			map.put("orderNum",checkGradeType.getOrderNum());
			createMessage("操作成功!",map);
		} catch (Exception e) {
			createErrorMessage("保存失败:" + e.getMessage());
		}
		return null;
	}
	@Action("set-survey-grade-type-weight")
	public String setSurveyGradeTypeWeight() throws Exception {
		try {
			checkGradeTypeManager.setCheckGradeTypeWeight(checkGradeTypeManager.getCheckGradeType(id),Struts2Utils.getParameter("weight"));
			createMessage("操作成功!");
		} catch (Exception e) {
			createErrorMessage("保存失败:" + e.getMessage());
		}
		return null;
	}
	/**
	 * 删除评分项目
	 * @return
	 * @throws Exception
	 */
	@Action("delete-survey-grade-type")
	public String deleteCheckGradeType() throws Exception {
		try {
			checkGradeTypeManager.deleteCheckGradeType(deleteIds);
			createMessage("删除成功!");
		} catch (Exception e) {
			createErrorMessage("删除失败:" + e.getMessage());
		}
		return null;
	}
	
	@Action("survey-grade-type-datas")
	public String getCheckGradeTypeDatas() throws Exception {
		List<CheckGradeType> checkGradeTypes;
		if(id == null || id < 0){
			checkGradeTypes = checkGradeTypeManager.getTopCheckGradeTypes(CheckGradeType.TYPE_SURVEY);
		}else{
			checkGradeTypes = checkGradeTypeManager.getCheckGradeType(id).getChildren();
		}
		List<Object> results = new ArrayList<Object>();
		for(CheckGradeType checkGradeType : checkGradeTypes){
			results.add(convertCheckGradeTypeToMap(checkGradeType));
		}
		renderText(JSONArray.fromObject(results).toString());
		return null;
	}
	
	/**
	 * 预览稽查评分表
	 * @return
	 * @throws Exception
	 */
	@Action("preview-survey-grade-table")
	public String getPreviewCheckGradeTable() throws Exception {
		try {
			List<CheckGradeType> firstLevelTypes = checkGradeTypeManager.getTopCheckGradeTypes(CheckGradeType.TYPE_SURVEY);
			int rowNumbers = 0;
			double totalFee = 0.0;
			StringBuffer surveyGradeTableStr = new StringBuffer("");
			for(CheckGradeType checkGradeType : firstLevelTypes){
				if(!hasCheckGrades(checkGradeType)){
					continue;
				}
				if(checkGradeType.getTotalFee() != null){
					totalFee += checkGradeType.getTotalFee();
				}
				if(surveyGradeTableStr.length() > 0){
					surveyGradeTableStr.append("<tr height=12><td><hr style='width:99%;BORDER-TOP-STYLE: dotted' size=1/></td></tr>");
				}
				rowNumbers++;
				createCheckTypeTr(surveyGradeTableStr, checkGradeType, "",rowNumbers);
			}
			if(rowNumbers > 0){
				surveyGradeTableStr.append("<tr height=12><td><hr style='width:99%;BORDER-TOP-STYLE: dotted' size=1/></td></tr>");
			}
			DecimalFormat df = new DecimalFormat("0.#");
			surveyGradeTableStr.append("<tr>")
			.append("<td style=\"font-style: italic;\">备注</td>")
			.append("</tr>")
			.append("<tr>")
			.append("<td style=\"font-style: italic;\">1.总共分为"+rowNumbers+"大项，总分为"+df.format(totalFee)+"分；</td>")
			.append("</tr>")
			.append("<tr>")
			.append("<td style=\"font-style: italic;\">2.供应商评价得分（原始得分/总分（"+df.format(totalFee)+"）×100），S大于等于80分时，此供应商列入进一步考察对象。若低于80分时，此供应商判为不合格。</td>")
			.append("</tr>");

			ActionContext.getContext().put("surveyGradeTableStr", surveyGradeTableStr.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	private boolean hasCheckGrades(CheckGradeType checkGradeType){
		if(!checkGradeType.getChildren().isEmpty()){
			for(CheckGradeType child : checkGradeType.getChildren()){
				if(hasCheckGrades(child)){
					return true;
				}
			}
			return false;
		}else{
			return !checkGradeType.getCheckGrades().isEmpty();
		}
	}
	private void createCheckTypeTr(StringBuffer sb,CheckGradeType checkGradeType,String parentIndex,int index){
		String indexStr = getIndexStr(checkGradeType.getLevel(),parentIndex,index);
		sb.append("<tr>")
		.append("<td><b>" + indexStr + "</b>" +checkGradeType.getName()+"</td>")
		.append("</tr>");
		if(!checkGradeType.getChildren().isEmpty()){
			int i=1;
			for(CheckGradeType child : checkGradeType.getChildren()){
				createCheckTypeTr(sb,child,indexStr,i);
				i++;
			}
		}else{
			DecimalFormat df = new DecimalFormat("0.#");
			sb.append("<tr><td><ul style='list-style:none;margin:0px;'>");
			for(CheckGrade checkGrade : checkGradeType.getCheckGrades()){
				sb.append("<li style='float:left;width:260px;line-height:24px;'><input type=\"radio\">"+checkGrade.getName()+"（"+df.format(checkGrade.getWeight())+"）</input></li>");
			}
			sb.append("</ul></td></tr>");
		}
	}
	private String getIndexStr(int level,String parentIndex,int index){
		StringBuffer sb = new StringBuffer("");
		for(int i=1;i<level+1;i++){
			sb.append("&nbsp;");
		}
		sb.append(parentIndex + index + ".");
		return sb.toString();
	}
	
	@Action("move-survey-grade-type")
	public String moveCheckGradeType() throws Exception {
		CheckGradeType checkGradeType = checkGradeTypeManager.getCheckGradeType(id);
		CheckGradeType newParent = null;
		if(parentId > 0 ){
			newParent = checkGradeTypeManager.getCheckGradeType(parentId);
		}
		try {
			checkGradeTypeManager.moveCheckGradeType(checkGradeType,newParent,position,orderNum);
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("orderNum",checkGradeType.getOrderNum());
			createMessage("操作成功!",map);
		} catch (Exception e) {
			createErrorMessage("保存失败:" + e.getMessage());
		}
		return null;
	}
	
	/**
	 * 转换评分项目类型至map
	 * @param checkGradeType
	 * @return
	 */
	private Map<String,Object> convertCheckGradeTypeToMap(CheckGradeType checkGradeType){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("data",checkGradeType.getName());
		Map<String,Object> attrMap = new HashMap<String, Object>();
		attrMap.put("id",checkGradeType.getId());
		attrMap.put("name",checkGradeType.getName());
		attrMap.put("level",checkGradeType.getLevel());
		attrMap.put("orderNum",checkGradeType.getOrderNum());
		if(checkGradeType.getLevel()==1){
			if(checkGradeType.getWeight() != null){
				map.put("data",checkGradeType.getName() + "(" + checkGradeType.getWeight() + "%)");
				attrMap.put("weight",checkGradeType.getWeight());
			}
		}
		attrMap.put("isLeaf",true);
		map.put("attr",attrMap);
		if(!checkGradeType.getChildren().isEmpty()){
			attrMap.put("isLeaf",false);
			map.put("state","closed");
//			List<Map<String,Object>> children = new ArrayList<Map<String,Object>>();
//			for(EstimateModel child : checkGradeType.getChildren()){
//				if(EstimateModel.STATE_ISUSE.equals(estimateModel.getState())){
//					attrMap.put("isLeaf",false);
//					map.put("state","open");
//					children.add(convertEstimateModelToMap(child));	
//				}
//			}
//			map.put("children",children);
		}
		return map;
	}
	
	/**
	 * 创建返回消息
	 * @param error
	 * @param message
	 * @return
	 */
	private void createMessage(String message){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("error",false);
		map.put("message",message);
		renderText(JSONObject.fromObject(map).toString());
	}
	/**
	 * 创建返回消息
	 * @param error
	 * @param message
	 * @return
	 */
	private void createMessage(String message,Object obj){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("error",false);
		map.put("message",message);
		map.put("obj",obj);
		renderText(JSONObject.fromObject(map).toString());
	}
}
