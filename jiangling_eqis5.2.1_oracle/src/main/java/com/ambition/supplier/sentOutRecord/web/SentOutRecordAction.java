package com.ambition.supplier.sentOutRecord.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import net.sf.json.JSONObject;

import com.ambition.carmfg.entity.SentOutRecord;
import com.ambition.supplier.entity.SupplierAbnormal;
import com.ambition.supplier.sentOutRecord.services.SentOutRecordManager;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;

@Namespace("/supplier/sent-out-record")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location ="/supplier/sent-out-record", type = "redirectAction") })
public class SentOutRecordAction extends CrudActionSupport<SentOutRecord>{
	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private SentOutRecord sentOutRecord;
	private Page<SentOutRecord> page;
	private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private SentOutRecordManager sentOutRecordManager;
	@Autowired
	private LogUtilDao logUtilDao;
	
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
	public SentOutRecord getSentOutRecord() {
		return sentOutRecord;
	}
	public void setSentOutRecord(SentOutRecord sentOutRecord) {
		this.sentOutRecord = sentOutRecord;
	}
	public Page<SentOutRecord> getPage() {
		return page;
	}
	public void setPage(Page<SentOutRecord> page) {
		this.page = page;
	}
	@Override
	public SentOutRecord getModel() {
		// TODO Auto-generated method stub
		return sentOutRecord;
	}
	@Action("save")
	@LogInfo(optType="保存",message="保存成本数据")
	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		try {
			sentOutRecordManager.saveSentOutRecord(sentOutRecord);
			renderText(JsonParser.getRowValue(sentOutRecord));
			logUtilDao.debugLog("保存",sentOutRecord.toString());
		} catch (Exception e) {
			// TODO: handle exception
			createErrorMessage("保存失败："+e.getMessage());
		}
		return null;
	}

	@Override
	@Action("delete")
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		try {
			sentOutRecordManager.delete(deleteIds);
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "删除数据，编号："+deleteIds);
		} catch (Exception e) {
			// TODO: handle exception
			renderText("删除失败:" + e.getMessage());
			log.error("删除数据信息失败",e);
		}
		return null;
	}

	@Override
	@Action("list")
	public String list() throws Exception {
		// TODO Auto-generated method stub
		return SUCCESS;
	}
	
	@Action("list-datas")
	@LogInfo(optType="数据",message="成本数据")
	public String listDates(){
		try {
			page = sentOutRecordManager.search(page);
			renderText(PageUtils.pageToJson(page));
		} catch (Exception e) {
			// TODO: handle exception
			log.error("台账获取例表失败", e);
		}
		return null;
	}
	/**
	 * 导出台账
	 * @return
	 * @throws Exception
	 */
	@Action("export")
	@LogInfo(optType="导出",message="导出数据")
	public String export() throws Exception {
		Page<SentOutRecord> page = new Page<SentOutRecord>(65535);
		page = sentOutRecordManager.search(page);
		this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"MFG_SENT_RECORD"),"发料记录"));
		return null;
	}
	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
		if(id == null){
			sentOutRecord = new SentOutRecord();
			sentOutRecord.setCompanyId(ContextUtils.getCompanyId());
			sentOutRecord.setCreatedTime(new Date());
			sentOutRecord.setCreator(ContextUtils.getUserName());
			sentOutRecord.setModifiedTime(new Date());
			sentOutRecord.setModifier(ContextUtils.getUserName());
			sentOutRecord.setBusinessUnitName(ContextUtils.getSubCompanyName());
			sentOutRecord.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else {
			sentOutRecord = sentOutRecordManager.getSentOutRecord(id);
		}
	}
	//创建返回消息
			public void createErrorMessage(String message){
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("error",true);
				map.put("message",message);
				renderText(JSONObject.fromObject(map).toString());
			}

}
