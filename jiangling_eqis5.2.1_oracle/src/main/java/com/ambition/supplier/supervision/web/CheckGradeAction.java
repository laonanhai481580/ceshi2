package com.ambition.supplier.supervision.web;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.api.ApiFactory;
import com.opensymphony.xwork2.ActionContext;

/**
 * 稽查评分项目维护
 * @author 赵骏
 *
 */
@Namespace("/supplier/supervision/check-grade")
@ParentPackage("default")
public class CheckGradeAction  extends BaseAction<CheckGrade> {
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
			checkGrade.setCreatedTime(new Date());
			checkGrade.setCompanyId(ContextUtils.getCompanyId());
			checkGrade.setCreator(ContextUtils.getUserName());
			checkGrade.setLastModifiedTime(new Date());
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

	@Action("move-check-grade")
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
	
	@Action("check-grade-datas")
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
	
	@Action("store-check-grade-type")
	public String storeCheckGradeType() throws Exception {
		CheckGradeType checkGradeType = null;
		if(id == null){
			checkGradeType = new CheckGradeType();
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
	/**
	 * 删除评分项目
	 * @return
	 * @throws Exception
	 */
	@Action("delete-check-grade-type")
	public String deleteCheckGradeType() throws Exception {
		try {
			checkGradeTypeManager.deleteCheckGradeType(deleteIds);
			createMessage("删除成功!");
		} catch (Exception e) {
			createErrorMessage("删除失败:" + e.getMessage());
		}
		return null;
	}
	
	@Action("check-grade-type-datas")
	public String getCheckGradeTypeDatas() throws Exception {
		List<CheckGradeType> checkGradeTypes;
		if(id == null || id < 0){
			checkGradeTypes = checkGradeTypeManager.getTopCheckGradeTypes(CheckGradeType.TYPE_CHECK);
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
	@Action("preview-check-grade-table")
	public String getPreviewCheckGradeTable() throws Exception {
		List<Option> auditReasons = ApiFactory.getSettingService().getOptionsByGroupCode("supplier_check_grade_audit_reason");
		ActionContext.getContext().put("auditReasons",auditReasons);
		
		List<CheckGrade> checkGrades = checkGradeManager.queryAllCheckGrades(CheckGradeType.TYPE_CHECK);
		List<CheckGradeType> firstLevelTypes = new ArrayList<CheckGradeType>();
		Map<Long,Integer> firstLevelTypeTotalMap = new HashMap<Long, Integer>();
		for(CheckGrade checkGrade : checkGrades){
			CheckGradeType checkGradeType = checkGrade.getCheckGradeType();
			CheckGradeType firstType = checkGradeType.getParent();
			if(firstType == null){
				firstType = checkGradeType;
			}
			if(!firstLevelTypes.contains(firstType)){
				firstLevelTypes.add(firstType);
				firstLevelTypeTotalMap.put(firstType.getId(),1);
			}else{
				firstLevelTypeTotalMap.put(firstType.getId(),firstLevelTypeTotalMap.get(firstType.getId()) + 1);
			}
		}
		Collections.sort(firstLevelTypes,new Comparator<CheckGradeType>() {
			@Override
			public int compare(CheckGradeType o1, CheckGradeType o2) {
				o1 = getTopParent(o1);
				o2 = getTopParent(o2);
				if(o1.getOrderNum()==null||o2.getOrderNum()==null){
					return 0;
				}else if(o1.getOrderNum() < o2.getOrderNum()){
					return 0;
				}else{
					return 1;
				}
			}
		});
		int rowNumbers = 1;
		Double totalFee = 0.0;
		StringBuffer checkGradeTableStr = new StringBuffer("");
		for(CheckGradeType checkGradeType : firstLevelTypes){
			List<CheckGradeType> children = null;
			if(checkGradeType.getChildren().isEmpty()){
				children = new ArrayList<CheckGradeType>();
				children.add(checkGradeType);
			}else{
				children = checkGradeType.getChildren();
			}
			boolean isRowSpanFirst = false;
			for(CheckGradeType child : children){
				boolean isRowSpanSecond = false;
				for(CheckGrade checkGrade : child.getCheckGrades()){
					if(checkGrade.getWeight() != null){
						totalFee += checkGrade.getWeight();
					}
					checkGradeTableStr.append("<tr>");
					CheckGradeType secondType = checkGrade.getCheckGradeType();
					CheckGradeType firstType = secondType.getParent();
					if(firstType==null){
						firstType = secondType;
					}
					if(firstType == secondType){
						if(!isRowSpanFirst){
							isRowSpanFirst = true;
							checkGradeTableStr.append("<td style='text-align:center;' colspan=\"2\" rowspan=\""+firstLevelTypeTotalMap.get(firstType.getId())+"\">"+firstType.getName()+"</td>");
						}
					}else{
						if(!isRowSpanFirst){
							isRowSpanFirst = true;
							checkGradeTableStr.append("<td rowspan=\""+firstLevelTypeTotalMap.get(firstType.getId())+"\">"+firstType.getName()+"</td>");
						}
						if(!isRowSpanSecond){
							isRowSpanSecond = true;
							checkGradeTableStr.append("<td rowspan=\""+secondType.getCheckGrades().size()+"\">"+secondType.getName()+"</td>");
						}
					}
					checkGradeTableStr.append("<td style='text-align:center;'>"+(rowNumbers++)+"</td>")
					.append("<td>"+checkGrade.getName()+"</td>")
					.append("<td>"+checkGrade.getWeight()+"</td>")
					.append("<td>&nbsp;</td>")
					.append("<td>&nbsp;</td>")
					.append("<td>&nbsp;</td>")
					.append("<td>&nbsp;</td>")
					.append("<td>&nbsp;</td>")
					.append("</tr>");
				}
			}
		}
		DecimalFormat df = new DecimalFormat("0.0");
		checkGradeTableStr.append("<tr height=27>")
		.append("<td colspan=\"3\" style=\"font-weight: bold;\">评价</td>")
		.append("<td style=\"font-weight: bold;\">合计得分 </td>")
		.append("<td>"+df.format(totalFee)+"</td>")
		.append("<td>&nbsp;</td>")
		.append("<td>&nbsp;</td>")
		.append("<td>&nbsp;</td>")
		.append("<td>&nbsp;</td>")
		.append("<td>&nbsp;</td>")
		.append("</tr>")
		.append("<tr height=27>")
		.append("<td colspan=\"3\" style=\"font-weight: bold;\">监察小组成员</td>")
		.append("<td colspan=\"7\">&nbsp;</td>")
		.append("</tr>");

		ActionContext.getContext().put("checkGradeTableStr", checkGradeTableStr.toString());
		return SUCCESS;
	}
	
	private CheckGradeType getTopParent(CheckGradeType checkGradeType){
		if(checkGradeType.getParent() == null){
			return checkGradeType;
		}else{
			return getTopParent(checkGradeType.getParent());
		}
	}
	@Action("move-check-grade-type")
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
		attrMap.put("orderNum",checkGradeType.getOrderNum());
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
