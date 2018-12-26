package com.ambition.gsm.base.web;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.gsm.base.service.IntransitedEquipmentBaseManager;
import com.ambition.gsm.entity.IntransitedEquipmentBase;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;

/**
 * 量检具基类(ACTION)
 * @author 张顺治
 *
 */
@Namespace("/gsm/base")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "/gsm/base", type = "redirectAction") })
public class IntransitedEquipmentBaseAction  extends com.ambition.product.base.CrudActionSupport<IntransitedEquipmentBase> {
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(this.getClass());
	private Long id;
	private String ids;
	private String deleteIds;
	private IntransitedEquipmentBase intransitedEquipmentBase;
	private Page<IntransitedEquipmentBase> page;
	private String exportIds;
	
	@Autowired
	private IntransitedEquipmentBaseManager intransitedEquipmentBaseManager;
	
	/**
	 * 新建页面
	 */
	@Override
	@Action("gsmEquipmentBase-input")
	public String input() throws Exception {
		return SUCCESS;
	}

	/**
	 * 列表页面
	 */
	@Override
	@Action("list")
	public String list() throws Exception {
		return SUCCESS;
	}
	
	/**
	 * 列表数据
	 */
	@Action("list-datas")
	public String getListDatas() throws Exception {
		page = intransitedEquipmentBaseManager.list(page);
		renderText(PageUtils.pageToJson(page));
		return null;
	}

	/**
	 * 预处理
	 */
	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			intransitedEquipmentBase=new IntransitedEquipmentBase();
			intransitedEquipmentBase.setCompanyId(ContextUtils.getCompanyId());
			intransitedEquipmentBase.setCreatedTime(new Date());
			intransitedEquipmentBase.setCreator(ContextUtils.getUserName());
			intransitedEquipmentBase.setModifiedTime(new Date());
			intransitedEquipmentBase.setModifier(ContextUtils.getUserName());
			intransitedEquipmentBase.setBusinessUnitName(ContextUtils.getSubCompanyName());
			intransitedEquipmentBase.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			intransitedEquipmentBase=intransitedEquipmentBaseManager.getIntransitedEquipmentBase(id);
		}
		
	}
	
	/**
	 * 获取对象
	 */
	@Override
	public IntransitedEquipmentBase getModel() {
		return intransitedEquipmentBase;
	}
	
	/**
	 * 预处理编辑保存
	 * @throws Exception
	 */
	public void prepareEditSave() throws Exception{
		prepareModel();
	}
	
	/**
	 * 保存（增加和修改）
	 */
	@Action("save")
	@LogInfo(optType="保存",message="基础设置信息")
	@Override
	public String save() throws Exception {
		try {
			intransitedEquipmentBaseManager.save(intransitedEquipmentBase);
			addActionMessage("保存成功");
			renderText(JsonParser.getRowValue(intransitedEquipmentBase));
		} catch (Exception e) {
			logger.error("保存失败：", e);
			createErrorMessage("保存失败");
		}
		return null;
	}
	
	/**
	 * 删除对象
	 */
	@Action("delete")
	@Override
	public String delete() throws Exception {
		try {
			intransitedEquipmentBaseManager.deleteGsm(deleteIds);
		} catch (Exception e) {
			logger.error("删除计量器具编号规则失败"+e.getMessage());
			renderText("删除失败");
		}
		return null;
	}
	
	public String getExportIds() {
		return exportIds;
	}

	public void setExportIds(String exportIds) {
		this.exportIds = exportIds;
	}

	public String getDeleteIds() {
		return deleteIds;
	}

	public void setDeleteIds(String deleteIds) {
		this.deleteIds = deleteIds;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public IntransitedEquipmentBase getIntransitedEquipmentBase() {
		return intransitedEquipmentBase;
	}

	public void setGsmEquipmentBase(IntransitedEquipmentBase intransitedEquipmentBase) {
		this.intransitedEquipmentBase = intransitedEquipmentBase;
	}

	public Page<IntransitedEquipmentBase> getPage() {
		return page;
	}

	public void setPage(Page<IntransitedEquipmentBase> page) {
		this.page = page;
	}
}

