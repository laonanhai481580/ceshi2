<%@page import="com.ambition.iqc.entity.InspectingItem"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.norteksoft.bs.options.entity.Option"%>
<%@page import="com.ambition.supplier.utils.DateUtil"%>
<%@page import="com.ambition.carmfg.entity.MfgPatrolItem"%>
<%@page import="com.ambition.carmfg.entity.MfgCheckItem"%>
<%@page import="com.ambition.carmfg.checkinspection.web.MfgPatrolInspectionReportAction"%>
<%@page import="com.norteksoft.product.util.PropUtils"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/print-meta.jsp" %>
	<script type="text/javascript">
		$(document).ready(function(){
			checkEmpty();
			window.print();
		});
		function checkEmpty(){
	 		//补全表格
			var pWidth = $("#checkItemsParent").width(),itemWidth = $("#checkItemsParent").find("table").width();
			if(itemWidth<pWidth){
				var width = pWidth - itemWidth;
				var total = parseInt(width/140);
				if(width%140>0){
					total++;
				}
				$("#checkItemsParent").find("tr").each(function(index,obj){
					for(var i=0;i<total;i++){
						var rCss = "";
						if(i+1==total){
							rCss = "border-right:0px;";
						}
						$(obj).append("<td style='padding:0px;width:140px;text-align:center;border-top:0px;"+rCss+"'>&nbsp;</td>");						
					}
				});
			}else{
				$("#badItemTable").find("tr").each(function(index,obj){
					$(obj).find("td").last().css("border-right:0px");
				});
			}
	 	}
	</script>
</head>
<body>
	<table class="form-table-border-left" style="width:800px;margin:auto;border:0px;">
		<caption style="height:25px"><h2>巡检报告</h2></caption>
		<caption style="text-align:right;padding-bottom:4px;">编号:${inspectionNo}</caption>
		<tr>
			<td style="width:40%;padding:0px;text-align:center;position:relative;" valign="middle" rowspan="6" colspan="2">
				<img id="img" alt='' style="max-width:120px;max-height:130px;" src="${mfgctx}/common/download.htm?id=${imgFileId}"/>
			</td>
			<td style="width:10%;">车间</td>
			<td style="width:20%">
				${workshop}
			</td>
			<td style="width:10%">件号</td>
			<td style="width:20%">
				${checkBomCode}
			</td>
		</tr>
		<tr>
			<td>件名</td>
			<td>
				${checkBomName}
			</td>
			<td>工序</td>
			<td>
				${workProcedure}
			</td>
		</tr>
		<tr>
			<td>客户编号</td>
			<td>
				${customerCode}
			</td>
			<td>客户名称</td>
			<td>
				${customerName}
			</td>
		</tr>
		<tr>
			<td>生产制令号</td>
			<td>${produceNo}</td>
			<td>批次号</td>
			<td>${batchNo}</td>
		</tr>
		<tr>
			<td>出货日期</td>
			<td>
				<s:date name="sendDate" format="yyyy-MM-dd" />	
			</td>
			<td>重要度</td>
			<td>
				${importance}
			</td>
		</tr>
		<tr>
			<td>出货数量</td>
			<td>
				${stockAmount}
			</td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="6" style="padding:1px;border-bottom:0px;" id="checkItemsParent">
<%
	List<MfgCheckItem> checkItems = (List<MfgCheckItem>)request.getAttribute("checkItems");
	//处理旧的统计记录
	List<MfgPatrolItem> patrolItems = (List<MfgPatrolItem>)request.getAttribute("patrolItems");
	List<String> patrolStrs = new ArrayList<String>();
	Map<String,String> patrolMap = new HashMap<String,String>();//保存对应的结果
	Map<String,Long> patrolItemMap = new HashMap<String,Long>();//保存spcSamples
	if(patrolItems == null){
		patrolItems = new ArrayList<MfgPatrolItem>();
	}
	Map<String,Boolean> existMap = new HashMap<String,Boolean>();
	Map<String,String> remarkMap = new HashMap<String,String>();
	Date lastDate = null;
	String lastStr = "";
	for(MfgPatrolItem item : patrolItems){
		String dateTimeStr = DateUtil.formateTimeStr(item.getInspectionDate());
		remarkMap.put(dateTimeStr,item.getRemark());
		lastStr = dateTimeStr;
		String name = item.getCheckItemName() + "_" + dateTimeStr;
		if(!existMap.containsKey(dateTimeStr)){
			patrolStrs.add(dateTimeStr);
			existMap.put(dateTimeStr,true);
			lastDate = item.getInspectionDate();
		}
		patrolMap.put(name,item.getResult());
		patrolItemMap.put(name,item.getId());
	}
	//检验数量
	Integer patrolNumber = 3;
	try{
		patrolNumber = Integer.valueOf(PropUtils.getProp("mfg.patrol.number"));
	}catch(Exception e){}
%>
				<table class="form-table-border-left" style="border:0px;width:300px;table-layout:fixed;">
					<thead>
						<tr>
							<td style="width:20px;text-align:center;border-top:0px;border-left:0px;">NO</td>
							<!-- <td style="width:20px;text-align:center;border-top:0px;">分类</td> -->
							<td style="width:220px;text-align:center;border-top:0px;">检查项目</td>
							<td style="width:70px;text-align:center;border-top:0px;">检查方法</td>
							<td style="width:60px;text-align:center;border-top:0px;">统计类型</td>
							<!-- <td style="width:60px;text-align:center;border-top:0px;">检验类别</td>
							<td style="width:45px;text-align:center;border-top:0px;">抽检数量</td>
							<td style="width:90px;text-align:center;border-top:0px;">规格</td>
							<td style="width:60px;text-align:center;border-top:0px;">培训资料</td> -->
							<%
								for(String patrolStr : patrolStrs){
							%>
								<td style="padding:0px;width:140px;text-align:center;border-top:0px;" class="checkItemsHeader" valign="middle">
									<span><%=patrolStr%></span>
								</td>
							<%} %>
						</tr>
					</thead>
					<tbody>
						<%
							List<Option> inspectionLevels = (List<Option>)request.getAttribute("inspectionLevels");
							if(inspectionLevels == null){
								inspectionLevels = new ArrayList<Option>();
							}
							Map<String,String> levelMap = new HashMap<String,String>();
							for(Option option : inspectionLevels){
								levelMap.put(option.getValue(),option.getName());
							}
							int i=1,flag = 0;
							boolean isLast = false;
							String inspectionDateStr = DateUtil.formateDateStr(lastDate);
							Map<String,String> conclusionMap = new HashMap<String,String>();
							Map<String,Boolean> editMap = new HashMap<String,Boolean>();
							for(MfgCheckItem checkItem : checkItems){
								flag++;
								if(flag==checkItems.size()){
									isLast = true;
								}
								String checkItemName = checkItem.getCheckItemName()==null?"":checkItem.getCheckItemName().replaceAll("\n","");
								String countType = StringUtils.isEmpty(checkItem.getCountType())?InspectingItem.COUNTTYPE_COUNT:checkItem.getCountType();
						%>
								<tr class="patrolItemTr">
								<td style="text-align: center;border-left:0px;"><%=i++ %></td>
								<td style="text-align: center;" valign="middle">
									<%=checkItem.getCheckItemName()==null?"":checkItem.getCheckItemName().replaceAll("\n","")%>
								</td>
								<td style="text-align:center;">
									<%=checkItem.getCheckMethod()==null?"":checkItem.getCheckMethod() %>
								</td>
								<td style="text-align: center;">
									<%=countType%>
								</td>
								<!-- <td style="text-align:center;">
									<%=checkItem.getInspectionLevel()==null?"":(levelMap.containsKey(checkItem.getInspectionLevel())?levelMap.get(checkItem.getInspectionLevel()):checkItem.getInspectionLevel()) %>
								</td>
								<td style="text-align: center;">
									<%=checkItem.getInspectionAmount()==null?"":checkItem.getInspectionAmount() %>
								</td>
								<td style="text-align: center;">
									<%=checkItem.getSpecifications()==null?"":checkItem.getSpecifications() %>
								</td>
								<td style="text-align: center;" valign="middle" class="attachmentFiles" >
								</td> -->
								<%
									for(String patrolStr : patrolStrs){
										String patrolName = checkItemName + "_" + patrolStr;
										boolean isInput = true;
										editMap.put(patrolStr,isInput);
								%>
									<td style="padding:0px;text-align:center;" class="checkItemsClass">
										<%=patrolMap.get(patrolName)==null?"":patrolMap.get(patrolName)%>
									</td>
								<%} %>
							</tr>
						<%} 
							//如果检验项目不为空,增加检验判定
							if(!checkItems.isEmpty()){
						%>
							<tr class="patrolItemConclusion">
							<td colspan="4" style="border-left:0px;text-align:center;">检验判断</td>
							<%
								for(String patrolStr : patrolStrs){
									String conclusion = conclusionMap.get(patrolStr);
									if(StringUtils.isEmpty(conclusion)){
										conclusion = "合格";
									}
							%>
								<td style="padding:0px;text-align:center;" class="checkItemConclusion">
									<input type="hidden" value="<%=conclusion %>" patrolConclusion="<%=patrolStr%>"/>
									<span patrolConclusionSpan="<%=patrolStr%>">
										<%=conclusion %>
									</span>
								</td>
							<%} %>
							</tr>
							<tr class="patrolItemRemarkTr">
							<td colspan="4" style="border-left:0px;text-align:center;">备注</td>
							<%
								for(String patrolStr : patrolStrs){
									String conclusion = conclusionMap.get(patrolStr);
									if(StringUtils.isEmpty(conclusion)){
										conclusion = "合格";
									}
							%>
								<td style="padding:0px;text-align:center;" class="patrolItemRemark">
									<%
										String remark = remarkMap.get(patrolStr);
										remark = remark==null?"":remark;
									%>
										<span><%=remark %></span>
								</td>
							<%} %>
							</tr>
						<%} %>
					</tbody>
				</table>
			</td>
		</tr>
		<tr>
			<td style="border-top:0px;">检验次数</td>
			<td style="border-top:0px;">
				<span>${patrolTimes}</span>
			</td>
			<td style="border-top:0px;">合格次数</td>
			<td style="border-top:0px;">
				<span>${patrolQualifiedTimes}</span>
			</td>
			<td style="border-top:0px;">不合格次数</td>
			<td style="border-top:0px;">
				<span>${patrolUnqualifiedTimes}</span>
			</td>
		</tr>
		<tr>
			<td style="border-left:0px;">检验员</td>
			<td>
				${inspector}
			</td>
			<td>
				审核员
			</td>
			<td colspan="3">
				${auditMan}
			</td>
		</tr>
	</table>
</body>
</html>