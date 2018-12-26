package com.ambition.supplier.supervision.service;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.supplier.entity.CheckGrade;
import com.ambition.supplier.entity.CheckGradeType;
import com.ambition.supplier.entity.CheckPlan;
import com.ambition.supplier.entity.CheckReport;
import com.ambition.supplier.entity.ReportGrade;
import com.ambition.supplier.entity.ReportInput;
import com.ambition.supplier.supervision.dao.CheckReportDao;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;

/**
 * 监察报告业务类
 * @author 赵骏
 *
 */
@Service
@Transactional
public class CheckReportManager {
	@Autowired
	private CheckReportDao checkReportDao;
	
	@Autowired
	private LogUtilDao logUtilDao;
	
	@Autowired
	private FormCodeGenerated formCodeGenerated;
	
	@Autowired
	private CheckGradeTypeManager checkGradeTypeManager;
	
	@Autowired
 	private CheckPlanManager checkPlanManager;
	
	@Autowired
	private ReportInputManager reportInputManager;
	
	@Autowired
 	private CheckGradeManager checkGradeManager;
	
	/**
	 * 查询监察报告
	 * @param page
	 * @return
	 */
	public Page<CheckReport> list(Page<CheckReport> page){
		return checkReportDao.list(page);
	}
	
	/**
	 * 获取监察报告
	 * @return
	 */
	public CheckReport getCheckReport(Long id){
		return checkReportDao.get(id);
	}
	
	/**
	 * 保存监察报告
	 * @param checkReport
	 */
	public void storeCheckReport(CheckReport checkReport,JSONObject params,JSONArray jsonArray){
		Long checkPlanId=null;
		//保存checkReport
		String totalrealFee=Struts2Utils.getParameter("totalrealFee");
		String totalFee=Struts2Utils.getParameter("totalFee");
		String groupMembers=Struts2Utils.getParameter("groupMembers");
		if(totalFee!=null&&!totalFee.equals("")){
			checkReport.setTotalFee(Double.parseDouble(totalFee));
		}
		if(totalrealFee!=null&&!totalrealFee.equals("")){
			checkReport.setRealFee(Double.parseDouble(totalrealFee));
		}
		if(groupMembers!=null&&!groupMembers.equals("")){
			checkReport.setGroupMembers(groupMembers);
		}
		if(Struts2Utils.getParameter("checkPlanId")!=null){
			if(!Struts2Utils.getParameter("checkPlanId").equals("")){
				checkPlanId=Long.parseLong(Struts2Utils.getParameter("checkPlanId"));
			}
		}
		CheckPlan checkPlan=null;
		if(checkPlanId!=null){
		 checkPlan=checkPlanManager.getCheckPlan(checkPlanId);
		}
		if(checkPlan!=null){
		if(checkPlan.getPlanDate()!=null&&checkReport.getCheckDate()!=null){
		if(checkPlan.getPlanDate().getTime()>=checkReport.getCheckDate().getTime()){
			checkPlan.setIsOntime("是");
		}else{
			checkPlan.setIsOntime("否");
		}
		}
		}
		if(checkReport.getId() == null){
			//创建最新的表单号
//			checkReport.setCode(formCodeGenerated.generateCheckReportCode());
		}
		if(checkReport.getReportGrades() == null){
			checkReport.setReportGrades(new ArrayList<ReportGrade>());
		}else{
			checkReport.getReportGrades().clear();
		}
		Map<Long,Double> parentTotalFeeMap = new HashMap<Long, Double>();
		Map<Long,Double> parentTotalrealFeeMap = new HashMap<Long, Double>();
		Map<Long,String> problemMap = new HashMap<Long, String>();
		Map<Long,ReportInput> existReportInputMap = new HashMap<Long,ReportInput>();
		//保存评分表
		for(int i=0;i<jsonArray.size()-2;i++){
			JSONObject jsonObject=(JSONObject) jsonArray.get(i);
			ReportGrade reportGrade=new ReportGrade();
			reportGrade.setCreatedTime(new Date());
			reportGrade.setCompanyId(ContextUtils.getCompanyId());
			reportGrade.setCreator(ContextUtils.getUserName());
			reportGrade.setLastModifiedTime(new Date());
			reportGrade.setLastModifier(ContextUtils.getUserName());
			reportGrade.setTopTdHtml(jsonObject.getString("topTdHtml"));
			reportGrade.setName(jsonObject.getString("name"));
			reportGrade.setWeight(jsonObject.getDouble("weight"));
			Long parentid=null;
			if(jsonObject.getString("parentid")!=null){
				parentid=Long.parseLong(jsonObject.getString("parentid"));
			}
			if(parentid!=null){
			reportGrade.setParentId(parentid);
			}
			reportGrade.setOrderNum(i);
			reportGrade.setType(jsonObject.getString("type"));
			reportGrade.setParentName(jsonObject.getString("parentname"));
			if(!jsonObject.getString("realFee").equals("")){
				reportGrade.setRealFee(jsonObject.getDouble("realFee"));	
			}
			reportGrade.setProblem(jsonObject.getString("problem"));
			reportGrade.setRemark(jsonObject.getString("remark"));
			reportGrade.setCheckReport(checkReport);
			checkReport.getReportGrades().add(reportGrade);
			if(parentid != null){
				if(parentTotalFeeMap.containsKey(parentid)){
					parentTotalFeeMap.put(parentid,reportGrade.getWeight() + parentTotalFeeMap.get(parentid));
				}else{
					parentTotalFeeMap.put(parentid,reportGrade.getWeight());
				}
				if(parentTotalrealFeeMap.containsKey(parentid)){
					parentTotalrealFeeMap.put(parentid,reportGrade.getRealFee() + parentTotalrealFeeMap.get(parentid));
				}else{
					parentTotalrealFeeMap.put(parentid,reportGrade.getRealFee());
				}
				if(problemMap.containsKey(parentid)){
					if(!reportGrade.getProblem().equals("")){
					problemMap.put(parentid,reportGrade.getProblem() +","+ problemMap.get(parentid));
					}
				}else{
					problemMap.put(parentid,reportGrade.getProblem());
				}
			}
		}
		
		//保存监察报告
		if(checkReport.getReportInputs() == null){
			checkReport.setReportInputs(new ArrayList<ReportInput>());
			logUtilDao.debugLog("保存", checkReport.toString());
		}else{
			checkReport.getReportInputs().clear();
			logUtilDao.debugLog("修改", checkReport.toString());
		}
		checkReport.setCheckPlan(checkPlan);
		if(checkPlan!=null){
			checkPlan.setCheckReport(checkReport);
		}
		checkReportDao.save(checkReport);
		for(Long id : parentTotalFeeMap.keySet()){
			CheckGradeType checkGradeType=checkGradeTypeManager.getCheckGradeType(id);
			ReportInput reportInput=new ReportInput();
			reportInput.setCreatedTime(new Date());
			reportInput.setCompanyId(ContextUtils.getCompanyId());
			reportInput.setCreator(ContextUtils.getUserName());
			reportInput.setLastModifiedTime(new Date());
			reportInput.setLastModifier(ContextUtils.getUserName());
			copyCheckGradeType(checkReport,checkGradeType,existReportInputMap,parentTotalFeeMap,parentTotalrealFeeMap,problemMap);
		}
	}

	private ReportInput copyCheckGradeType(CheckReport checkReport,CheckGradeType checkGradeType,Map<Long,ReportInput> existReportInputMap,Map<Long,Double> parentTotalFeeMap,Map<Long,Double> parentTotalrealFeeMap,Map<Long,String> problemMap){
		try {
			if(checkGradeType == null){
				return null;
			}else if(existReportInputMap.containsKey(checkGradeType.getId())){
				return existReportInputMap.get(checkGradeType.getId());
			}else{
				ReportInput reportInput = new ReportInput();
				reportInput.setLevel(checkGradeType.getLevel());
				reportInput.setOrderNum(checkGradeType.getOrderNum());
				reportInput.setTotalFee(parentTotalFeeMap.get(checkGradeType.getId()));
				reportInput.setRealFee(parentTotalrealFeeMap.get(checkGradeType.getId()));
				reportInput.setProblem(problemMap.get(checkGradeType.getId()));
				reportInput.setName(checkGradeType.getName());
				existReportInputMap.put(checkGradeType.getId(),reportInput);
				reportInput.setParent(copyCheckGradeType(checkReport,checkGradeType.getParent(), existReportInputMap, parentTotalFeeMap, parentTotalrealFeeMap, problemMap));
				if(reportInput.getParent()==null){
					reportInput.setCheckReport(checkReport);
				}
				reportInputManager.saveReportInput(reportInput);
				return reportInput;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	/**
	 * 删除监察报告
	 * @param deleteIds
	 */
	public void deleteCheckReport(String deleteIds) {
		for(String id : deleteIds.split(",")){
			if(StringUtils.isNotEmpty(id)){
				CheckReport checkReport = checkReportDao.get(Long.valueOf(id));
				if(checkReport != null){
					if(checkReport.getCheckPlan() != null){
						checkReport.getCheckPlan().setCheckReport(null);
						checkReport.getReportGrades().clear();
						checkReportDao.getSession().update(checkReport.getCheckPlan());
					}
					checkReport.getReportGrades().clear();
					checkReport.getReportInputs().clear();
					logUtilDao.debugLog("删除", checkReport.toString());
					checkReportDao.delete(checkReport);
				}
			}
		}
	}
	
//	//复制稽查指标
//	private ReportGradeType copyCheckGradeType(CheckGradeType checkGradeType,ReportGradeType parent) throws Exception{
//		ReportGradeType reportGradeType = new ReportGradeType();
//		reportGradeType.setParent(parent);
//		reportGradeType.setName(checkGradeType.getName());
//		reportGradeType.setLevel(checkGradeType.getLevel());
//		reportGradeType.setOrderNum(checkGradeType.getOrderNum());
//		reportGradeType.setTotalFee(checkGradeType.getTotalFee());
//		reportGradeType.setId(checkGradeType.getId());
//		reportGradeType.setType(checkGradeType.getType());
//		List<ReportGradeType> children = new ArrayList<ReportGradeType>();
//		if(checkGradeType.getChildren().isEmpty()){
//			List<ReportGrade> reportGrades = new ArrayList<ReportGrade>();
//			for(CheckGrade checkGrade : checkGradeType.getCheckGrades()){
//				ReportGrade reportGrade = new ReportGrade();
//				reportGrade.setId(checkGrade.getId());
////				reportGrade.setReportGradeType(reportGradeType);
//				reportGrade.setName(checkGrade.getName());
//				reportGrade.setWeight(checkGrade.getWeight());
//				reportGrade.setType(checkGrade.getType());
//				reportGrade.setOrderNum(checkGrade.getOrderNum());
//				reportGrades.add(reportGrade);
//			}
//			reportGradeType.setReportGrades(reportGrades);
//		}else{
//			for(CheckGradeType child : checkGradeType.getChildren()){
//				children.add(copyCheckGradeType(child,reportGradeType));
//			}
//		}
//		reportGradeType.setChildren(children);
//		return reportGradeType;
//	}
	/**
	 * 根据稽查评分报告创建评分表
	 * @param checkReport
	 * @return
	 * @throws Exception 
	 */
	public String createCheckGradeTable(CheckReport checkReport) throws Exception{
		//上次得分情况
		Map<String,ReportGrade> lastFeeMap = new HashMap<String, ReportGrade>();
		if(checkReport.getSupplierId() != null && checkReport.getCheckDate() != null){
			List<Object> params = new ArrayList<Object>();
			String hql = "from CheckReport c where c.companyId = ? and c.supplierId = ? and c.checkDate <= ?";
			params.add(ContextUtils.getCompanyId());
			params.add(checkReport.getSupplierId());
			params.add(checkReport.getCheckDate());
			if(checkReport.getId() != null){
				hql += " and c.id <> ?";
				params.add(checkReport.getId());
			}
			hql += " order by c.checkDate desc";
			List<CheckReport> list = checkReportDao.find(hql,params.toArray());
			if(!list.isEmpty()){
				List<ReportGrade> reportGrades = list.get(0).getReportGrades();
				for(ReportGrade reportGrade : reportGrades){
					lastFeeMap.put(reportGrade.getParentName(),reportGrade);
				}
			}
		}
		if(checkReport.getId()==null){
			checkReport.setReportGrades(createReportGradesByCheckGrade());
		}
		
//		for(ReportGrade reportGrade : checkReport.getReportGrades()){
//			ReportGradeType reportGradeType = reportGrade.getReportGradeType();
//			ReportGradeType firstType = reportGradeType.getParent();
//			if(firstType == null){
//				firstType = reportGradeType;
//			}
//			if(!firstLevelTypes.contains(firstType)){
//				firstLevelTypes.add(firstType);
//				firstLevelTypeTotalMap.put(firstType.getId(),1);
//			}else{
//				firstLevelTypeTotalMap.put(firstType.getId(),firstLevelTypeTotalMap.get(firstType.getId()) + 1);
//			}
//		}
//		Collections.sort(firstLevelTypes,new Comparator<ReportGradeType>() {
//			@Override
//			public int compare(ReportGradeType o1, ReportGradeType o2) {
//				o1 = getTopParent(o1);
//				o2 = getTopParent(o2);
//				if(o1.getOrderNum()==null||o2.getOrderNum()==null){
//					return 0;
//				}else if(o1.getOrderNum() < o2.getOrderNum()){
//					return 0;
//				}else{
//					return 1;
//				}
//			}
//		});
		int rowNumbers = 1;
		
		Double totalFee = 0.0,lastFee=0.0;
		StringBuffer checkGradeTableStr = new StringBuffer("");
		for(ReportGrade reportGrade : checkReport.getReportGrades()){
			String problem="";
			if(reportGrade.getProblem()!=null){
				problem=reportGrade.getProblem();
			}
			if(reportGrade.getWeight() != null){
				totalFee += reportGrade.getWeight();
			}
			if(reportGrade.getRealFee() != null){
				lastFee += reportGrade.getRealFee();
			}
			String remark=reportGrade.getRemark()==null?"":reportGrade.getRemark();
			checkGradeTableStr.append("<tr>")
			.append(reportGrade.getTopTdHtml())
			.append("<td>"+(rowNumbers++))
			.append("<input type='hidden' name='topTdHtml' id='topTdHtml' value=\""+reportGrade.getTopTdHtml()+"\"></input>")
			.append("</td>")
			.append("<td>"+reportGrade.getName())
			.append("<input type='hidden' name='name' id='name' value="+reportGrade.getName()+"></input>")
			.append("</td>")
			.append("<td style='text-align:center;'>"+reportGrade.getWeight())
			.append("<input type='hidden' name='weight' id='weight' value="+reportGrade.getWeight()+"></input>")
			.append("</td>")
			.append("<td style='text-align:center;'>"+reportGrade.getRealFee())
			.append("<input type='hidden' name='orderNum' id='orderNum' value="+reportGrade.getOrderNum()+"></input>")
			.append("<input type='hidden' name='type' id='type' value="+reportGrade.getType()+"></input>")
			.append("<input type='hidden' name='parentname' id='parentname' value="+reportGrade.getParentName()+"></input>")
			.append("<input type='hidden' name='parentid' id='parentid' value="+reportGrade.getParentId()+"></input>")
			.append("</td>")
			.append("<td style='text-align:center;'>"+problem+"</td>")
			.append("<td style='text-align:center;'><input type='text' id='realFee' name='realFee' onkeyup='caculateFees();' value='"+reportGrade.getRealFee()+"'></td>")
			.append("<td style='text-align:center;'><textarea  id='problem' name='problem'></textarea></td>")
			.append("<td style='text-align:center;'><textarea  id='remark' name='remark'>"+remark+"</textarea></td>")
			.append("</tr>");
		}
		

		DecimalFormat df = new DecimalFormat("0.00");
		checkGradeTableStr.append("<tr height=27>")
		.append("<td colspan=\"3\" style=\"font-weight: bold;\">评价</td>")
		.append("<td style=\"font-weight: bold;\">合计得分 </td>")
		.append("<td style='text-align:center;'>"+df.format(totalFee)+"</td><input type='hidden' id='totalFee' name='totalFee' value='"+df.format(totalFee)+"'></input></td>")
		.append("<td style='text-align:center;'>"+df.format(lastFee)+"</td>")
		.append("<td>&nbsp;</td>")
		.append("<td style='text-align:center;'><span id='realFeeSpan'>"+df.format(lastFee)+"</span><input type='hidden' id='totalrealFee' name='totalrealFee' value='"+df.format(lastFee)+"'></input></td>")
		.append("<td>&nbsp;</td>")
		.append("<td>&nbsp;</td>")
		.append("</tr>")
		.append("<tr height=27>")
		.append("<td colspan=\"3\" style=\"font-weight: bold;\">监察小组成员</td>")
		.append("<td colspan=\"7\"><input type='text' name='groupMembers' id='groupMembers' value='"+(checkReport.getGroupMembers()==null?"":checkReport.getGroupMembers())+"' style='width:99%;' onclick='selectObj();'></input></td>")
		.append("</tr>");
		return checkGradeTableStr.toString();
	}
	
	
	/**
	 * 根据监察评分表创建评监察报告
	 * @param checkReport
	 * @return
	 * @throws Exception 
	 */
	public String createCheckReportTable(CheckReport checkReport) throws Exception{
		//上次得分情况
		List<ReportInput> firstreportInputList=null;
		StringBuffer checkGradeTableStr = new StringBuffer("");
		if(checkReport.getReportInputs() != null ){
			firstreportInputList=checkReport.getReportInputs();
		}
		int rowNumbers = 0;
		Map<String,Double> feeMap=new HashMap<String,Double>();
		for(ReportInput reportInput:firstreportInputList){
			rowNumbers++;
			feeMap=createCheckTypeTr(feeMap,checkGradeTableStr, reportInput, "",rowNumbers);
		}
		if(firstreportInputList.size()!=0){
		DecimalFormat df = new DecimalFormat("0.#");
		checkGradeTableStr.append("<tr>")
		.append("<td style=\"font-style: italic;\"> 总分为:"+df.format(feeMap.get("alltotalFee")*10)+"</td>")
		.append("<td style=\"font-style: italic;\">实际总得分为:"+df.format(feeMap.get("allrealFee")*10)+"</td>")
		.append("<td style=\"font-style: italic;\"></td>")
		.append("</tr>");
		}else{
			checkGradeTableStr.append("<tr><td colspan='6'><span><font color='red' font-size=100px>未进行监察评分!!</font></span></tr></td>");
		}
		return checkGradeTableStr.toString();
	}
	
	private Map<String,Double> createCheckTypeTr(Map<String,Double> feeMap,StringBuffer sb,ReportInput reportInput,String parentIndex,int index){
		String indexStr = getIndexStr(reportInput.getLevel(),parentIndex,index);
		String totalFee="";
		String realFee="";
		String problem="";
		DecimalFormat df = new DecimalFormat("0.00");
		if(reportInput.getTotalFee()!=null){
			totalFee="("+df.format(reportInput.getTotalFee()*10)+")";
		}
		if(reportInput.getRealFee()!=null){
			realFee=df.format(reportInput.getRealFee()*10);
			realFee=String.valueOf(realFee);
		}
		if(reportInput.getProblem()!=null){
			problem=reportInput.getProblem();
		}
		sb.append("<tr>")
		.append("<td><b>" + indexStr + "</b>" +reportInput.getName()+totalFee+"</td>")
		.append("<td>"+realFee+"</td>")
		.append("<td>"+problem+"</td>")
		.append("</tr>");
		//加父节点的总分和实际分数
		if(reportInput.getLevel()==1){
		if(feeMap.containsKey("alltotalFee")){
			if(reportInput.getTotalFee()!=null){
			feeMap.put("alltotalFee",reportInput.getTotalFee() + feeMap.get("alltotalFee"));
			}
		}else{
			if(reportInput.getTotalFee()!=null){
			feeMap.put("alltotalFee",reportInput.getTotalFee());
			}
		}
		if(feeMap.containsKey("allrealFee")){
			if(reportInput.getRealFee()!=null){
				feeMap.put("allrealFee",reportInput.getRealFee() + feeMap.get("allrealFee"));
			}
		}else{
			if(reportInput.getRealFee()!=null){
				feeMap.put("allrealFee",reportInput.getRealFee());
			}
		}
		}
		if(!reportInput.getChildren().isEmpty()){
			int i=1;
			for(ReportInput child : reportInput.getChildren()){
				createCheckTypeTr(feeMap,sb,child,indexStr,i);
				//加子点的总分和实际分数
					if(feeMap.containsKey("alltotalFee")){
						if(child.getTotalFee()!=null){
						feeMap.put("alltotalFee",child.getTotalFee() + feeMap.get("alltotalFee"));
						}
					}else{
						if(child.getTotalFee()!=null){
						feeMap.put("alltotalFee",child.getTotalFee());
						}
					}
					if(feeMap.containsKey("allrealFee")){
						if(child.getRealFee()!=null){
							feeMap.put("allrealFee",child.getRealFee() + feeMap.get("allrealFee"));
						}
					}else{
						if(child.getRealFee()!=null){
							feeMap.put("allrealFee",child.getRealFee());
						}
					}
				i++;
			}
		}
		return feeMap;
	}
	private String getIndexStr(int level,String parentIndex,int index){
		StringBuffer sb = new StringBuffer("");
		for(int i=1;i<level+1;i++){
			sb.append("&nbsp;");
		}
		sb.append(parentIndex + index + ".");
		return sb.toString();
	}
	
	private CheckGradeType getTopParent(CheckGradeType checkGradeType){
		if(checkGradeType.getParent() == null){
			return checkGradeType;
		}else{
			return getTopParent(checkGradeType.getParent());
		}
	}
	
	/**
	 * 根据配置生成稽查评分表模型
	 * @return
	 */
	public List<ReportGrade> createReportGradesByCheckGrade(){
		List<ReportGrade> reportGrades = new ArrayList<ReportGrade>();
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
					ReportGrade reportGrade = new ReportGrade();
					reportGrade.setOrderNum(checkGrade.getOrderNum());
					reportGrade.setWeight(checkGrade.getWeight());
					reportGrade.setName(checkGrade.getName());
					CheckGradeType secondType = checkGrade.getCheckGradeType();
					reportGrade.setParentName(secondType.getId() + secondType.getName());
					reportGrade.setParentId(secondType.getId());
					CheckGradeType firstType = secondType.getParent();
					if(firstType==null){
						firstType = secondType;
					}
					StringBuffer topTdHtml = new StringBuffer("");
					if(firstType == secondType){
						if(!isRowSpanFirst){
							isRowSpanFirst = true;
							topTdHtml.append("<td style='text-align:center;' colspan=2 rowspan="+firstLevelTypeTotalMap.get(firstType.getId())+">"+firstType.getName()+"</td>");
						}
					}else{
						if(!isRowSpanFirst){
							isRowSpanFirst = true;
							topTdHtml.append("<td rowspan="+firstLevelTypeTotalMap.get(firstType.getId())+">"+firstType.getName()+"</td>");
						}
						if(!isRowSpanSecond){
							isRowSpanSecond = true;
							topTdHtml.append("<td rowspan="+secondType.getCheckGrades().size()+">"+secondType.getName()+"</td>");
						}
					}
					reportGrade.setTopTdHtml(topTdHtml.toString());
					reportGrades.add(reportGrade);
				}
			}
		}
		return reportGrades;
	}
	/**
	 * 转换json
	 * @param params
	 * @return
	 */
	public JSONObject convertJsonObject(JSONObject params){
		JSONObject resultJson = new JSONObject();
		if(params == null){
			return resultJson;
		}
		for(Object key : params.keySet()){
			resultJson.put(key,params.getJSONArray(key.toString()).get(0));
		}
		return resultJson;
	}
}
