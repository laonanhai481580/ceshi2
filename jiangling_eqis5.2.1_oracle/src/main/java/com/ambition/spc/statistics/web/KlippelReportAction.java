package com.ambition.spc.statistics.web;

import java.util.List;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.spc.entity.KlippelCheckRecord;
import com.ambition.spc.statistics.service.KlippelCheckRecordManager;
import com.ambition.util.annotation.LogInfo;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.opensymphony.xwork2.ActionContext;

/**
 * 类名:Klippel数据采集报表
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  赵骏
 * @version 1.00 2013-7-3 发布
 */
@Namespace("/spc/statistics-analysis")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "spc/statistics-analysis", type = "redirectAction") })
public class KlippelReportAction extends com.ambition.product.base.CrudActionSupport<KlippelCheckRecord> {
	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(this.getClass());
	private String deleteIds;
	private JSONObject params;
	private KlippelCheckRecord klippelCheckRecord;
	@Autowired
	private KlippelCheckRecordManager klippelCheckRecordManager;
	
	public KlippelCheckRecord getKlippelCheckRecord() {
		return klippelCheckRecord;
	}

	public void setKlippelCheckRecord(KlippelCheckRecord klippelCheckRecord) {
		this.klippelCheckRecord = klippelCheckRecord;
	}

	public KlippelCheckRecord getModel() {
		return klippelCheckRecord;
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
	public String input() throws Exception {
		return SUCCESS;
	}
	
	@Override
	public String save() throws Exception {
		return null;
	}
	
	@Override
	public String delete() throws Exception {
		return null;
	}

	
	@Action("klippel-check-report")
	@LogInfo(optType="页面",message="Klippel数据采集合格率")
	public String list() throws Exception {
		List<Option> options = ApiFactory.getSettingService().getOptionsByGroupCode("spc_machine_no");
		ActionContext.getContext().put("machineNos",options);
		return SUCCESS;
	}
	/**
	 * 进货检验一次合格率图表页面数据
	 */
	@Action("klippel-check-report-datas")
	@LogInfo(optType="数据",message="Klippel数据采集合格率数据")
	public String getKlippelCheckReportDatas() throws Exception {
		params = convertJsonObject(params);
		try {
			this.renderText(JSONObject.fromObject(klippelCheckRecordManager.getKlippelDatas(params)).toString());
		} catch (Exception e) {
			log.error("Klippel数据采集报表失败",e);
			JSONObject result = new JSONObject();
			result.put("error",true);
			result.put("message","取数失败," + e.getMessage());
			this.renderText(result.toString());
		}
		return null;	
	}
	/**
	 * 转换json
	 * @param params
	 * @return
	 */
	private JSONObject convertJsonObject(JSONObject params){
		JSONObject resultJson = new JSONObject();
		for(Object key : params.keySet()){
			resultJson.put(key,params.getJSONArray(key.toString()).get(0));
		}
		return resultJson;
	}
}
