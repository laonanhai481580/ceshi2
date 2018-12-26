package com.ambition.gsm.base.web;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.gsm.base.service.GsmCheckItemManager;
import com.ambition.gsm.entity.GsmCheckItem;
import com.ambition.product.BaseAction;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.web.struts2.CrudActionSupport;

/**
 * 
 * 类名:检验项目Action
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年11月24日 发布
 */

@Namespace("/gsm/base/check-item")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "/gsm/base/check-item", type = "redirectAction") })
public class GsmCheckItemAction extends BaseAction<GsmCheckItem>{

		/**
		  *GsmCheckItemAction.java2016年11月24日
		 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;//删除的编号 
	private JSONObject params;//检验项目指标对象
	private File myFile;
	private String myFileFileName;//导入文件名称
	private Page<GsmCheckItem> page;
	private GsmCheckItem checkItem;
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private GsmCheckItemManager checkItemManager;
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
	public JSONObject getParams() {
		return params;
	}
	public void setParams(JSONObject params) {
		this.params = params;
	}
	public File getMyFile() {
		return myFile;
	}
	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}
	public String getMyFileFileName() {
		return myFileFileName;
	}
	public void setMyFileFileName(String myFileFileName) {
		this.myFileFileName = myFileFileName;
	}
	public Page<GsmCheckItem> getPage() {
		return page;
	}
	public void setPage(Page<GsmCheckItem> page) {
		this.page = page;
	}
	public GsmCheckItem getGsmCheckItem() {
		return checkItem;
	}
	public void setGsmCheckItem(GsmCheckItem checkItem) {
		this.checkItem = checkItem;
	}
	@Override
	public GsmCheckItem getModel() {
		return checkItem;
	}
	
	@Override
	protected void prepareModel() throws Exception {
		if(id == null){
			checkItem = new GsmCheckItem();
			checkItem.setCreatedTime(new Date());
			checkItem.setCompanyId(ContextUtils.getCompanyId());
			checkItem.setCreator(ContextUtils.getUserName());
			checkItem.setLastModifiedTime(new Date());
			checkItem.setLastModifier(ContextUtils.getUserName());
			checkItem.setBusinessUnitName(ContextUtils.getSubCompanyName());
			checkItem.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else {
			checkItem = checkItemManager.getGsmCheckItem(id);
		}
	}
	
	@Action("input")
	@Override
	public String input() throws Exception {
		return null;
	}
	@Action("save")
	@Override
	public String save() throws Exception {
		try {
			if(id == null){
				checkItemManager.saveGsmCheckItem(checkItem);
				logUtilDao.debugLog("保存", checkItem.toString());
			}else{
				checkItem.setLastModifiedTime(new Date());
				checkItem.setLastModifier(ContextUtils.getUserName());
				checkItemManager.saveGsmCheckItem(checkItem);
				logUtilDao.debugLog("修改", checkItem.toString());
			}
			this.renderText(JsonParser.getRowValue(checkItem));
		} catch (Exception e) {
			createErrorMessage("保存失败:" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	@Action("delete")
	@Override
	public String delete() throws Exception {
			try {
				checkItemManager.deleteGsmCheckItem(deleteIds);
			} catch (Exception e) {
				createErrorMessage("删除失败:" + e.getMessage());
			}
		return null;
	}

	@Action("list")
	@Override
	public String list() throws Exception {
		renderMenu();
		return SUCCESS;
	}
	private String getValue(Object obj){
		if(obj==null){
			return "";
		}else{
			return obj+"";
		}
	}
	private String formatGsmCheckItem(GsmCheckItem checkItem){
		StringBuffer row = new StringBuffer("{");
		row.append("\"itemName\":\"" + getValue(checkItem.getItemName()) + "\"")
		.append(",\"id\":\"" + getValue(checkItem.getId()) + "\"")
		.append(",\"standardValue\":\"" + getValue(checkItem.getStandardValue()) + "\"")
		.append(",\"allowableError\":\"" + getValue(checkItem.getAllowableError()) + "\"")
		.append("}");
		return row.toString();
	}
	@Action("list-datas")
	public String getBomListByParent() throws Exception {
		try {
			page = checkItemManager.search(page);
			StringBuilder json = new StringBuilder();
			json.append("{\"page\":\"");
			json.append(getValue(page.getPageNo()));
			json.append("\",\"total\":\"");
			json.append(getValue(page.getTotalPages()));
			json.append("\",\"records\":\"");
			json.append(getValue(page.getTotalCount()));
			json.append("\",\"rows\":");
			StringBuffer rows = new StringBuffer("[");
			for(GsmCheckItem checkItem : page.getResult()){
				if(rows.length()>1){
					rows.append(",");
				}
				rows.append(formatGsmCheckItem(checkItem));
			}
			json.append(rows.append("]"));
			json.append("}");
			renderText(json.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@Action("export")
	public String export() throws Exception {
		try {
			Page<GsmCheckItem> page = new Page<GsmCheckItem>(Integer.MAX_VALUE);
			page = checkItemManager.search(page);
			renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"GSM_CHECK_ITEM"),"检验项目"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
