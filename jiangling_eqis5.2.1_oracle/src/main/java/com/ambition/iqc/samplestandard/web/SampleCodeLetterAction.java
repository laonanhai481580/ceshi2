package com.ambition.iqc.samplestandard.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.iqc.entity.SampleCodeLetter;
import com.ambition.iqc.samplestandard.service.SampleCodeLetterManager;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;

/**    
 * SampleCodeLetterAction.java
 * @authorBy YUKE
 *
 */
@Namespace("/iqc/sample-standard/code-letter")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "iqc/sample-standard/code-letter", type = "redirectAction") })
public class SampleCodeLetterAction extends CrudActionSupport<SampleCodeLetter> {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private SampleCodeLetter sampleCodeLetter;
	private Page<SampleCodeLetter> page;
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private SampleCodeLetterManager sampleCodeLetterManager;
	
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

	public SampleCodeLetter getSampleCodeLetter() {
		return sampleCodeLetter;
	}

	public void setSampleCodeLetter(SampleCodeLetter sampleCodeLetter) {
		this.sampleCodeLetter = sampleCodeLetter;
	}

	public Page<SampleCodeLetter> getPage() {
		return page;
	}

	public void setPage(Page<SampleCodeLetter> page) {
		this.page = page;
	}

	@Override
	public SampleCodeLetter getModel() {
		return sampleCodeLetter;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id == null){
			sampleCodeLetter = new SampleCodeLetter();
			sampleCodeLetter.setCompanyId(ContextUtils.getCompanyId());
			sampleCodeLetter.setCreatedTime(new Date());
			sampleCodeLetter.setCreator(ContextUtils.getUserName());
			sampleCodeLetter.setLastModifiedTime(new Date());
			sampleCodeLetter.setLastModifier(ContextUtils.getUserName());
			sampleCodeLetter.setBusinessUnitName(ContextUtils.getSubCompanyName());
			sampleCodeLetter.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			sampleCodeLetter = sampleCodeLetterManager.getSampleCodeLetter(id);
		}
	}
	
	@Action("save")
	@LogInfo(optType="保存",message="样本量字码表")
	@Override
	public String save() throws Exception {
		if(id != null && id != 0){
			sampleCodeLetter.setLastModifiedTime(new Date());
			sampleCodeLetter.setLastModifier(ContextUtils.getUserName());
			logUtilDao.debugLog("修改", sampleCodeLetter.toString());
		}else{
			logUtilDao.debugLog("保存", sampleCodeLetter.toString());
		}
		try {
			sampleCodeLetterManager.saveSampleCodeLetter(sampleCodeLetter);
			this.renderText(JsonParser.getRowValue(sampleCodeLetter));
		} catch (Exception e) {
			createErrorMessage("保存失败：" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	@Action("delete")
	@LogInfo(optType="删除",message="样本量字码表")
	@Override
	public String delete() throws Exception {
		try{
			sampleCodeLetterManager.deleteSampleCodeLetter(deleteIds);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	@Action("input")
	@Override
	public String input() throws Exception {
		return SUCCESS;
	}

	@Action("list")
	@Override
	public String list() throws Exception {
		return SUCCESS;
	}
	
	@Action("list-datas")
	public String getListDatas() throws Exception {
		try {
			page = sampleCodeLetterManager.getListDatas(page,SampleCodeLetter.GB_TYPE);
			renderText(PageUtils.pageToJson(page));
		} catch (Exception e) {
			e.printStackTrace();
		}
		logUtilDao.debugLog("查询", "物料检验标准维护：样本量字码表");
		return null;
	}
	
	//样本代字对照表
	@Action("cl-list")
	public String getList() throws Exception {
		return SUCCESS;
	}
	
	@Action("cl-list-datas")
	public String getCLListDatas() throws Exception {
		try {
			page = sampleCodeLetterManager.getListDatas(page,SampleCodeLetter.MIL_TYPE);
			renderText(PageUtils.pageToJson(page));
		} catch (Exception e) {
			e.printStackTrace();
		}
		logUtilDao.debugLog("查询", "物料检验标准维护：样本代字对照表");
		return null;
	}
	
	/**
	 * 创建返回消息
	 * @param error
	 * @param message
	 * @return
	 */
	public void createErrorMessage(String message){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("error",true);
		map.put("message",message);
		renderText(JSONObject.fromObject(map).toString());
	}
}
