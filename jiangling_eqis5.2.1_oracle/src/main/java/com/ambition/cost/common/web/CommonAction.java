package com.ambition.cost.common.web;

import java.util.ArrayList;
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

import com.ambition.cost.composingdetail.service.ComposingManager;
import com.ambition.cost.entity.Composing;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;

@Namespace("/cost/common")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "/cost/common", type = "redirectAction") })
public class CommonAction extends CrudActionSupport<Composing> {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long nodeid;
	private Long parentId;
	private Boolean multiselect = false;//是否多选
 	@Autowired
	private ComposingManager composingManager;


	public Boolean getMultiselect() {
		return multiselect;
	}

	public void setMultiselect(Boolean multiselect) {
		this.multiselect = multiselect;
	}

	public Long getNodeid() {
		return nodeid;
	}

	public void setNodeid(Long nodeid) {
		this.nodeid = nodeid;
	}

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

	@Override
	protected void prepareModel() throws Exception {
		
	}

	@Override
	public Composing getModel() {
		return null;
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
	
	@Action("delete")
	@Override
	public String delete() throws Exception {
		return null;
	}

	@Action("composing-select")
	@Override
	public String list() throws Exception {
		return SUCCESS;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Action("composing-list-datas")
	public String productBomSelect() throws Exception {
		Page page = new Page();
		List<Composing> composingParents = null;
		if(nodeid == null){
			composingParents = composingManager.getTopComposingList(Struts2Utils.getParameter("topCodes"));
		}else{
			Composing parent = composingManager.getComposing(nodeid);
			if(parent != null){
				composingParents = parent.getChildren();
			}else{
				composingParents = new ArrayList<Composing>();
			}
		}
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(Composing composing : composingParents){
			convertComposing(composing,list);
		}
		page.setResult(list);
//		renderText(PageUtils.pageToJson(page));
		String result = composingManager.getResultJson(page);
		renderText(JSONObject.fromObject(result).toString());
		return null;
	}
	/**
	 * 转换质量成本至json对象
	 * @param composing
	 * @return
	 */
	private void convertComposing(Composing composing,List<Map<String,Object>> list){
		Boolean isLeaf = composing.getChildren()==null||composing.getChildren().isEmpty()?true:false;
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("id",composing.getId());
		map.put("name",composing.getName());
		map.put("code",composing.getCode());
		if(composing.getParent()==null){
			map.put("levelTwoName",composing.getName());
			map.put("levelTwoCode",composing.getCode());
		}else{
			map.put("levelTwoName",composing.getParent().getName());
			map.put("levelTwoCode",composing.getParent().getCode());
		}
		map.put("remark",composing.getRemark());
		map.put("checkDepartment",composing.getCheckDepartment());
		map.put("cooperateDepartment",composing.getCooperateDepartment());
		map.put("level",composing.getDengji()-1);
		map.put("parent",composing.getParent()==null?"":composing.getParent().getId());
		map.put("isLeaf",isLeaf);
		list.add(map);
		if(!isLeaf){
			map.put("expanded",false);
			map.put("loaded",false);
		}else{
			map.put("loaded",true);
		}
	}
    @Action("select-by-level-two")
    public String selectByProcess() throws Exception {
    	JSONObject result = new JSONObject();
        try{
            String levelTwo = Struts2Utils.getParameter("levelTwo");
            List<Composing> composings = composingManager.listThree();
            String leavlThrees = "";
            for (Composing composing : composings) {
            	if(composing.getParent().getName().equals(levelTwo)){
	            	composing.getName();
	            	if(leavlThrees.length()==0){
	            		leavlThrees = composing.getName();
					}else{
						leavlThrees += "," +  composing.getName();
					}
			}
            }
            result.put("leavlThrees",  leavlThrees);
            result.put("error", false);
        }catch(Exception e){
            result.put("error", true);
            result.put("message", "查找三级成本失败");
        }
        this.renderText(JsonParser.object2Json(result));
        return null;
    }
}