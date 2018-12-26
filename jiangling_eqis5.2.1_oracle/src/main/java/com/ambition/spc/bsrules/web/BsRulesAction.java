package com.ambition.spc.bsrules.web;

import java.util.Date;

import net.sf.json.JSONObject;

import org.apache.commons.lang.xwork.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.spc.bsrules.service.BsRulesManager;
import com.ambition.spc.entity.BsRules;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.mms.base.utils.ParseJsonUtil;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;

/**
 * BsRulesAction.java
 * 
 * @authorBy wanglf
 * 
 */

@SuppressWarnings("deprecation")
@Namespace("/spc/base-info/bs-rules")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "spc/base-info/bs-rules", type = "redirectAction") })
public class BsRulesAction extends CrudActionSupport<BsRules> {

	private static final long serialVersionUID = 1L;
	private Long id;
	private BsRules bsRules;
	@Autowired
	private BsRulesManager bsRulesManager;
	private Page<BsRules> page = new Page<BsRules>(Page.EACH_PAGE_TEN, true);
	@Autowired
	private LogUtilDao logUtilDao;
	private String deleteIds;
	private JSONObject params;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setPage(Page<BsRules> page) {
		this.page = page;
	}

	public Page<BsRules> getPage() {
		return page;
	}

	public BsRules getModel() {
		return bsRules;
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
	protected void prepareModel() throws Exception {
		if (id == null) {
			bsRules = new BsRules();
			bsRules.setCompanyId(ContextUtils.getCompanyId());
			bsRules.setCreatedTime(new Date());
			bsRules.setCreator(ContextUtils.getUserName());
			bsRules.setModifiedTime(new Date());
			bsRules.setModifier(ContextUtils.getUserName());
		} else {
			bsRules = bsRulesManager.getBsRules(id);
		}
	}

	@Action("input")
	@Override
	public String input() throws Exception {
		if (id != null) {
			bsRules = bsRulesManager.getBsRules(id);
		}
		ActionContext.getContext().put("bsRules", bsRules);
		ActionContext.getContext().put("id", id);
		return SUCCESS;
	}

	@Action("save")
	@Override
	public String save() throws Exception {
		bsRulesManager.saveBsRules(bsRules);
		renderText(ParseJsonUtil.getRowValue(bsRules));
		logUtilDao.debugLog("保存", bsRules.toString());
		return null;
	}

	@SuppressWarnings("unused")
	@Action("save-detail")
	public String saveDetail() throws Exception {
		String name = Struts2Utils.getParameter("name");
		String autoWriteName = Struts2Utils.getParameter("autoWriteName");
		String no = Struts2Utils.getParameter("no");
		String type = Struts2Utils.getParameter("type");
		String model = Struts2Utils.getParameter("model");
		String number0 = Struts2Utils.getParameter("number0");
		String number11 = Struts2Utils.getParameter("number11");
		String number12 = Struts2Utils.getParameter("number12");
		String number2 = Struts2Utils.getParameter("number2");
		String number3 = Struts2Utils.getParameter("number3");
		String number4 = Struts2Utils.getParameter("number4");
		String range0 = Struts2Utils.getParameter("range0");
		String range1 = Struts2Utils.getParameter("range1");
		BsRules br = new BsRules();
		String id = Struts2Utils.getParameter("id");
		if (StringUtils.isNotEmpty(id)) {
			br = bsRulesManager.getBsRules(Long.parseLong(id));
		}
		br.setCompanyId(ContextUtils.getCompanyId());
		br.setCreatedTime(new Date());
		br.setCreator(ContextUtils.getUserName());
		br.setModifiedTime(new Date());
		br.setModifier(ContextUtils.getUserName());
		// 自动生成名称
//		if (autoWriteName.equals("是")) {
//			String typeName = "";
//			if (type.equals("0")) {
//				typeName = "主控制图";
//			} else {
//				typeName = "副控制图";
//			}
//			String autoName = "";
//			if (model.equals("0")) {
//				String rangeName = "";
//				if (range0.equals("1")) {
//					rangeName = "上升";
//				} else if (range0.equals("2")) {
//					rangeName = "下降";
//				} else if (range0.equals("3")) {
//					rangeName = "不变";
//				} else if (range0.equals("4")) {
//					rangeName = "上下交替";
//				}
//				autoName = typeName + "为趋势图连续" + number0 + "点" + rangeName;
//			} else if (model.equals("1")) {
//				String rangeName = "";
//				if (range1.equals("5")) {
//					rangeName = "位于A区";
//				} else if (range1.equals("6")) {
//					rangeName = "位于B区";
//				} else if (range1.equals("7")) {
//					rangeName = "位于C区";
//				} else if (range1.equals("8")) {
//					rangeName = "在控制限以外";
//				} else if (range1.equals("9")) {
//					rangeName = "在规格线以外";
//				} else if (range1.equals("10")) {
//					rangeName = "在中心线同侧B区以外";
//				} else if (range1.equals("11")) {
//					rangeName = "在中心线同侧C区以外";
//				} else if (range1.equals("12")) {
//					rangeName = "在中心线同侧";
//				} else if (range1.equals("13")) {
//					rangeName = "在中心线两侧B区以外";
//				} else if (range1.equals("14")) {
//					rangeName = "在中心线两侧C区以外";
//				}
//				autoName = typeName + "为运行图连续" + number11 + "点中有" + number12
//						+ "点" + rangeName;
//			}
			// else if(model.equals("2")){
			// autoName=typeName+"为交替图连续"+number2+"点上下交替";
			// }else if(model.equals("3")){
			// autoName=typeName+"为其他连续"+number3+"点落在中心限两侧且无一落在C区内";
			// }else if(model.equals("4")){
			// autoName=typeName+"为其他子组内有"+number4+"个样本值超出规格限";
			// }
//			br.setName(autoName);
//		} else {
			br.setName(name);
//		}
		// 设置名字结束
		br.setType(type);
		br.setModel(model);
		if (model.equals("0")) {
			String expression = type + "." + model + "." + number0 + "."
					+ range0;
			br.setExpression(expression);
			br.setNo("SN."+expression);
		} else if (model.equals("1")) {
			String expression = type + "." + model + "." + number11 + "."
					+ number12 + "." + range1;
			br.setExpression(expression);
			br.setNo("SN."+expression);
		} else if (model.equals("2")) {
			String expression = type + "." + model + "." + number2;
			br.setExpression(expression);
			br.setNo("SN."+expression);
		} else if (model.equals("3")) {
			String expression = type + "." + model + "." + number3;
			br.setExpression(expression);
			br.setNo("SN."+expression);
		} else if (model.equals("4")) {
			String expression = type + "." + model + "." + number4;
			// br.setModel("3");
			br.setExpression(expression);
			br.setNo("SN."+expression);
		}else{
			br.setNo(no);
		}
		bsRulesManager.saveBsRules(br);
		logUtilDao.debugLog("保存", "SPC判定规则");
		return "input";
	}

	@Action("bs-rules-select")
	public String bsRulesSelect() throws Exception {
		Boolean multiselect = Boolean.parseBoolean(Struts2Utils
				.getParameter("multiselect"));
		ActionContext.getContext().put("multiselect", multiselect);
		return SUCCESS;
	}

	@Action("delete")
	@Override
	public String delete() throws Exception {
		bsRulesManager.deleteBsRulesDao(deleteIds);
		return null;
	}

	@Action("list")
	@Override
	public String list() throws Exception {
		return SUCCESS;
	}

	@Action("list-datas")
	public String getListDatas() throws Exception {
		page = bsRulesManager.list(page);
		renderText(PageUtils.pageToJson(page));
		logUtilDao.debugLog("查询", "SPC：判断规则");
		return null;
	}

	@Action("export")
	public String export() throws Exception {
		return null;
	}

}
