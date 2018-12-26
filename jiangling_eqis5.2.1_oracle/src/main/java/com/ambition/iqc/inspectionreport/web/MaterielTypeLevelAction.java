package com.ambition.iqc.inspectionreport.web;

import java.util.ArrayList;
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

import com.ambition.iqc.entity.MaterielTypeLevel;
import com.ambition.iqc.inspectionreport.service.MaterielTypeLevelManager;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.web.struts2.CrudActionSupport;

/**
 * 类名:MaterielTypeLevelAction.java
 * 中文类名:
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：物料大类树结构</p>
 * <p>
 * @author 作者 :wuxuming
 * @version 1.0 创建时间：2015-3-3 上午10:51:29
 * </p>
 */
@Namespace("/iqc/inspection-report")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "/iqc/inspection-report", type = "redirectAction") })
public class MaterielTypeLevelAction extends CrudActionSupport<MaterielTypeLevel>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private Long id;
    private Long parentId;
    private Long checkGradeTypeId;
    private String name;
    private Integer orderNum;
    private String position;
    private String deleteIds;//删除的编号 
    @Autowired
    private MaterielTypeLevelManager materielTypeLevelManager;
    
    private MaterielTypeLevel materielTypeLevel;//评分项目
    
    private Page<MaterielTypeLevel> page;
    
    private JSONObject params;
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Long getCheckGradeTypeId() {
		return checkGradeTypeId;
	}

	public void setCheckGradeTypeId(Long checkGradeTypeId) {
		this.checkGradeTypeId = checkGradeTypeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
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
	 @Action("materiel-node-load")
	public String getMaterielTypeLevel(){
		return SUCCESS;
	}
    /**
     * 方法名: 
     * <p>功能说明：查询物料清单数据</p>
     * 创建人:wuxuming 日期： 2015-3-3 version 1.0
     * @param 
     * @return
     */
    @Action("materiel-node-load-datas")
    public String getMaterielTypeLevelDatas() {
    	try{
	        List<MaterielTypeLevel> materielTypeLevels=null;
	        if(id == null || id < 0){
	        	materielTypeLevels = materielTypeLevelManager.getTopMaterielTypeLevel();
	        }else{
	        	materielTypeLevels = materielTypeLevelManager.getMaterielTypeLevel(id).getChildMaterielType();
	        }
	        List<Object> results = new ArrayList<Object>();
	        for(MaterielTypeLevel materielTypeLevel : materielTypeLevels){
	            results.add(convertCheckReviewTypeToMap(materielTypeLevel));
	        }
	        renderText(JSONArray.fromObject(results).toString());
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
        return null;
    }
    /**
     * 方法名: 
     * <p>功能说明：转换物料级别为map</p>
     * 创建人:wuxuming 日期： 2015-3-3 version 1.0
     * @param 
     * @return
     */
    private Map<String,Object> convertCheckReviewTypeToMap(MaterielTypeLevel materielTypeLevel){
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("data",materielTypeLevel.getMaterielTypeName());
        Map<String,Object> attrMap = new HashMap<String, Object>();
        attrMap.put("id",materielTypeLevel.getId());
        attrMap.put("name",materielTypeLevel.getMaterielTypeName());
        attrMap.put("orderNum",materielTypeLevel.getMaterielLevel());
        attrMap.put("isLeaf",true);
        map.put("attr",attrMap);
        if(!materielTypeLevel.getChildMaterielType().isEmpty()){
            attrMap.put("isLeaf",false);
            map.put("state","closed");
        }
        return map;
    }
	@Override
	public MaterielTypeLevel getModel() {
		return materielTypeLevel;
	}

	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String list() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
