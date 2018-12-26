<%@page import="com.ambition.iqc.entity.IncomingInspectionActionsReport"%>
<%@ include file="/common/taglibs.jsp"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.norteksoft.product.util.ContextUtils"%>
<%@page import="com.ambition.iqc.entity.SampleScheme"%>
<%@page import="org.apache.commons.beanutils.PropertyUtils"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="com.ambition.iqc.entity.InspectingItem"%>
<%@page import="com.ambition.iqc.entity.SampleCodeLetter"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.norteksoft.product.api.entity.Option"%>
<%@page import="java.util.List"%>
<%@page import="com.ambition.iqc.entity.CheckItem"%>
<%@ page language="java"  pageEncoding="UTF-8"%>
<%
	String sampleSchemeType = "",schemeStartDateStr = "";
	List<CheckItem> checkItems = (List<CheckItem>)request.getAttribute("checkItems");
	int max = 6,defaultColumns = 6;
	Map<Long,Map<String,Double>> resultMap = new HashMap<Long,Map<String,Double>>();
	long idFlag = 0;
	for(CheckItem checkItem : checkItems){
		sampleSchemeType = checkItem.getSampleSchemeType();
		if("".equals(schemeStartDateStr)){
			schemeStartDateStr = checkItem.getSchemeStartDateStr();
		}
		if(SampleScheme.EXEMPTION_TYPE.equals(sampleSchemeType)){
			break;
		}
		if(checkItem.getId()==null){
			idFlag++;
			checkItem.setId(idFlag);
		}
		if(!InspectingItem.COUNTTYPE_COUNT.equals(checkItem.getCountType())){
			Map<String,Double> tempMap = new HashMap<String,Double>();
			int tempMax = defaultColumns;
			checkItem.getResult3();
			for(int i=1;i<=80;i++){
				Double value = (Double)PropertyUtils.getProperty(checkItem,"result"+i);
				if(value != null){
					tempMap.put("result" + i, value);
					if(i>tempMax){
						tempMax=i;
					}
				}
			}
			if(tempMax>max){
				max = tempMax;
			}
			for(int i=1; i<=tempMax; i++){
				if(!tempMap.containsKey("result" + i)){
					tempMap.put("result" + i,null);
				}
			}
			resultMap.put(checkItem.getId(), tempMap);
		}
	}
	StringBuffer flagIds = new StringBuffer("");
	if(!SampleScheme.EXEMPTION_TYPE.equals(sampleSchemeType)){
%>
<table class="form-table-border-left" style="border:0px;table-layout:fixed;">
	<thead>
		<tr>
			<td style="width:20px;text-align:center;border-top:0px;border-left:0px;">NO</td>
<!-- 			<td style="width:80px;text-align:center;border-top:0px;">分类</td> -->
<!-- 			<td style="width:80px;text-align:center;border-top:0px;">操作</td> -->
			<td style="width:220px;text-align:center;border-top:0px;">检查项目</td>
			<td style="width:70px;text-align:center;border-top:0px;">检查方法</td>
			<td style="width:60px;text-align:center;border-top:0px;">检验水平</td>
			<td style="width:60px;text-align:center;border-top:0px;">AQL</td>
			<td style="width:60px;text-align:center;border-top:0px;">抽检数量</td>
			<td style="width:90px;text-align:center;border-top:0px;">规格<%=max%></td>
			<td style="width:<%=max*37 + 70%>px;padding:0px;border-top:0px;text-align: center;" id="checkItemsHeader">
				检验记录
			</td>
			<td style="width:100px;text-align: center;border-top:0px;">检测设备编号</td>
			<td style="width:40px;text-align: center;border-top:0px;">合格数</td>
			<td style="width:40px;text-align: center;border-top:0px;">不良数</td>
			<td style="width:60px;text-align: center;border-top:0px;border-right:0px;">结论</td>
			<%if(!"欧菲科技-神奇工场".equals(ContextUtils.getCompanyName())){%>
			  <td style="width:100px;text-align: center;border-top:0px;">附件</td>
			<%}%>
			<td style="width:100px;text-align: center;border-top:0px;border-right:0px;">标准备注</td>
			<td style="width:100px;text-align: center;border-top:0px;border-right:0px;">检验员</td>
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
			int i=0,flag = 0;
			boolean isLast = false;
			boolean isShow=false;
			for(CheckItem checkItem : checkItems){
				flag++;
				if(flag==checkItems.size()){
					isLast = true;
					
				}
				if(checkItem.getItemStatus().equals("已领取")){
					isShow=true;
					i++;
				}else{
					isShow=false;
				}
				flagIds.append(",a" + flag);
		%>
			<tr name="checkItemTr">
				<td style="text-align:center;border-left:0px;" name="<%=checkItem.getCheckItemName()%>"  itemName="<%=checkItem.getCheckItemName()%>" tdShow="<%=isShow%>"><%=i %></td>
				<%-- <%
					if(checkItem.getParentRowSpan() != null){
				%>
				<td rowspan="<%=checkItem.getParentRowSpan() %>"  style="text-align: left;"  parentName="<%=checkItem.getParentItemName()%>"   tdShow="<%=isShow%>"><%=checkItem.getParentItemName() %></td>
				<%} %> --%>
				<td style="text-align: left;" itemName="<%=checkItem.getCheckItemName()%>"  tdShow="<%=isShow%>">
					<input type="hidden" fieldName="itemStatus"  name="a<%=flag %>_itemStatus" itemStatus="<%=checkItem.getCheckItemName()%>" value="<%=checkItem.getItemStatus()==null?"":checkItem.getItemStatus()%>"/>
<%-- 					<input type="hidden" fieldName="inspectionMan" name="a<%=flag %>_inspectionMan" inspectionMan="<%=checkItem.getCheckItemName()%>" value="<%=checkItem.getInspectionMan()==null?"":checkItem.getInspectionMan()%>"/> --%>
					<input type="hidden" fieldName="classification"  name="a<%=flag %>_classification" value="<%=checkItem.getClassification()==null?"":checkItem.getClassification()%>"/>
					<input type="hidden" fieldName="unit"  name="unit" value="<%=checkItem.getUnit()==null?"":checkItem.getUnit()%>"/>
					<input type="hidden" fieldName="minlimit"  name="a<%=flag %>_minlimit" value="<%=checkItem.getMinlimit()==null?"":checkItem.getMinlimit()%>"/>
					<input type="hidden" fieldName="maxlimit"  name="a<%=flag %>_maxlimit" value="<%=checkItem.getMaxlimit()==null?"":checkItem.getMaxlimit()%>"/>
					<input type="hidden" fieldName="parentRowSpan"  name="a<%=flag %>_parentRowSpan" value="<%=checkItem.getParentRowSpan()==null?"":checkItem.getParentRowSpan()%>"/>
					<input type="hidden" fieldName="parentItemName"  name="a<%=flag %>_parentItemName" value="<%=checkItem.getParentItemName()==null?"":checkItem.getParentItemName()%>"/>
					<input type="hidden" fieldName="checkItemName"  name="a<%=flag %>_checkItemName" value="<%=checkItem.getCheckItemName()==null?"":checkItem.getCheckItemName().replaceAll("\n","")%>"/>
					<input type="hidden" fieldName="codeLetter"  name="a<%=flag %>_codeLetter" value="<%=checkItem.getCodeLetter()==null?"":checkItem.getCodeLetter()%>"/>
					<input type="hidden" fieldName="inspectionType"  name="a<%=flag %>_inspectionType" value="<%=checkItem.getInspectionType()==null?"":checkItem.getInspectionType()%>"/>
					<input type="hidden" fieldName="countType"  name="a<%=flag %>_countType" value="<%=checkItem.getCountType()==null?"":checkItem.getCountType()%>"/>
					<input type="hidden" fieldName="aql"  name="a<%=flag %>_aql" value="<%=checkItem.getAql()==null?"":checkItem.getAql()%>"/>
					<input type="hidden" fieldName="aqlAc"  name="a<%=flag %>_aqlAc" value="<%=checkItem.getAqlAc()==null?"":checkItem.getAqlAc()%>"/>
					<input type="hidden" fieldName="aqlRe"  name="a<%=flag %>_aqlRe" value="<%=checkItem.getAqlRe()==null?"":checkItem.getAqlRe()%>"/>
					<input type="hidden" fieldName="sampleSchemeType"  name="a<%=flag %>_sampleSchemeType" value="<%=checkItem.getSampleSchemeType()==null?"":checkItem.getSampleSchemeType()%>"/>
					<input type="hidden" fieldName="schemeStartDate_timestamp"  name="a<%=flag %>_schemeStartDate_timestamp" value="<%=checkItem.getSchemeStartDateStr()%>"/>
					<input type="hidden" fieldName="standardRemark"  name="a<%=flag %>_standardRemark" value="<%=checkItem.getStandardRemark()==null?"":checkItem.getStandardRemark()%>"/>
					<input type="hidden" fieldName="flag"  name="a<%=flag %>_flag" value="<%=checkItem.getFlag()==null?"":checkItem.getFlag()%>"/>
					<%-- <%if(checkItem.getFlag()!=null && checkItem.getFlag().equals("1")) {%> 
						<font style="color:red"><%=checkItem.getCheckItemName()==null?"":checkItem.getCheckItemName().replaceAll("\n","")%></font>
					<%}else{ %>
					 	<%=checkItem.getCheckItemName()==null?"":checkItem.getCheckItemName().replaceAll("\n","")%>
					<%} %> --%>
					<%if(checkItem.getConclusion()!=null && checkItem.getConclusion().equals("NG")) {%> 
						<font style="color:red"><%=checkItem.getCheckItemName()==null?"":checkItem.getCheckItemName().replaceAll("\n","")%></font>
					<%}else{ %>
					 	<%=checkItem.getCheckItemName()==null?"":checkItem.getCheckItemName().replaceAll("\n","")%>
					<%} %>
				</td>
				<td style="text-align:center;" itemName="<%=checkItem.getCheckItemName()%>"  tdShow="<%=isShow%>">
					<input type="hidden" fieldName="checkMethod" name="a<%=flag %>_checkMethod" value="<%=checkItem.getCheckMethod()==null?"":checkItem.getCheckMethod()%>"/>
					<%=checkItem.getCheckMethod()==null?"":checkItem.getCheckMethod() %>
				</td>
				<td style="text-align:center;" itemName="<%=checkItem.getCheckItemName()%>"  tdShow="<%=isShow%>">
					<input type="hidden" fieldName="inspectionLevel" name="a<%=flag %>_inspectionLevel" value="<%=checkItem.getInspectionLevel()%>"/>
					<%=checkItem.getInspectionLevel()==null?"":(levelMap.containsKey(checkItem.getInspectionLevel())?levelMap.get(checkItem.getInspectionLevel()):checkItem.getInspectionLevel()) %>
				</td>
				<td style="text-align: center;word-wrap:break-word;word-break:break-all;" itemName="<%=checkItem.getCheckItemName()%>"  tdShow="<%=isShow%>">
					<%=checkItem.getAql()==null?"":checkItem.getAql() %>
				</td>
				<td style="text-align: center;" itemName="<%=checkItem.getCheckItemName()%>"  tdShow="<%=isShow%>">
					<input type="hidden"  fieldName="inspectionAmount" hisParent="<%=checkItem.getInspectionType() %>" name="a<%=flag %>_inspectionAmount" value="<%=checkItem.getInspectionAmount()==null?"":checkItem.getInspectionAmount()%>"/>
					<%=checkItem.getInspectionAmount()==null?"":checkItem.getInspectionAmount() %>
				</td>
				<td style="text-align: center;overflow:hidden; word-wrap:break-word;word-break:break-all;"  tdShow="<%=isShow%>" itemName="<%=checkItem.getCheckItemName()%>" title="<%=checkItem.getSpecifications()==null?"":checkItem.getSpecifications() %>">
					<input type="hidden" fieldName="specifications" name="a<%=flag %>_specifications" value="<%=checkItem.getSpecifications()==null?"":checkItem.getSpecifications()%>"/>
					<%=checkItem.getSpecifications()==null?"":checkItem.getSpecifications() %>
				</td>
				<td id="check-items-td" style="padding:0px;"  tdShow="<%=isShow%>" class="checkItemsClass" itemName="<%=checkItem.getCheckItemName()%>">
					<%
						if(InspectingItem.COUNTTYPE_COUNT.equals(checkItem.getCountType())){
					%> <textarea style="width:85%;height:95%;" rows=3 fieldName="results" name="a<%=flag %>_results"  class="{maxlength:1000}"><%=checkItem.getResults()==null||checkItem.getResults()==""?"合格":checkItem.getResults() %></textarea>
						<a class="small-button-bg" style="margin-left:2px;float:right;" onclick="removeBadNum(this)" href="#" title="减少不合格数"><span class="ui-icon ui-icon-minus" style='cursor:pointer;'></span></a>
						<a class="small-button-bg" style="margin-left:2px;float:right;" onclick="addBadNum(this)" href="#" title="增加不合格数"><span class="ui-icon ui-icon-plus" style='cursor:pointer;'></span></a>
					<%}else{ 
							Map<String,Double> tempMap = resultMap.get(checkItem.getId());
							int count=0;
							if(!"欧菲科技-神奇工场".equals(ContextUtils.getCompanyName())){
								count = tempMap.keySet().size();
							}else{
								count = checkItem.getInspectionAmount()==null?tempMap.keySet().size():checkItem.getInspectionAmount();
							}
// 							int count = checkItem.getInspectionAmount()==null?tempMap.keySet().size():checkItem.getInspectionAmount();
							Double maxlimit = checkItem.getMaxlimit(),minlimit = checkItem.getMinlimit();
							for(int j=0;j<count;j++){
								String name = "result"+(j+1);
								Double value = tempMap.get(name);
								String color = "black";
								if(maxlimit!=null&&minlimit!=null&&value!=null){
									if(value<minlimit||value>maxlimit){
										color="red";
									}
								}
						%>
								<input    color="<%=color %>" isData="true" onchange="this.value=this.value.replace(' ','');" style="width:50px;float:left;margin-left:2px;color:<%=color %>" title="<%=checkItem.getCheckItemName() + "样品" + (j+1) %>" results=true fieldName="<%=name%>" name="a<%=flag %>_<%=name%>" value="<%=value==null?"":value>100000?new BigDecimal(value):new BigDecimal(new Double(value).toString())%>" class="{number:true}" ></input>
						<%
							} 
						%>
								<a class="small-button-bg"  style="margin-left:2px;float:left;" onclick="addResultInput(this,'<%=checkItem.getCheckItemName() %>')" href="#" title="添加检验项"><span class="ui-icon ui-icon-plus" style='cursor:pointer;'></span></a>
								<a class="small-button-bg" style="margin-left:2px;float:left;" onclick="removeResultInput(this)" href="#" title="删除最后的检验项"><span class="ui-icon ui-icon-minus" style='cursor:pointer;'></span></a>
					<%} %>
				</td>
				<td style="text-align: center;" tdShow="<%=isShow%>" itemName="<%=checkItem.getCheckItemName()%>">
					<%-- <s:select list="equipment_numbers" 
						  theme="simple"
						  listKey="name" 
						  listValue="name" 
						  labelSeparator=""
						  cssStyle="width:100px;text-align:center;"
						  name="equipmentNumber"
						  fieldName="equipmentNumber"
						  emptyOption="true"></s:select> --%>
						  <input style="width:95%;padding:0px;text-align:center;" fieldName="equipmentNumber" name="a<%=flag %>_equipmentNumber" value="<%=checkItem.getEquipmentNumber()==null?"":checkItem.getEquipmentNumber() %>" ></input>
				</td>
				<td style="text-align: center;"  tdShow="<%=isShow%>" itemName="<%=checkItem.getCheckItemName()%>">
					<input style="width:95%;padding:0px;text-align:center;" hisParent="<%=checkItem.getInspectionType() %>" fieldName="qualifiedAmount" name="a<%=flag %>_qualifiedAmount" value="<%=checkItem.getQualifiedAmount() %>" hisamount="<%=checkItem.getInspectionAmount()==null?"0":checkItem.getInspectionAmount() %>" class="{required:true,digits:true,min:0}"></input>
				</td>
				<td style="text-align: center;" tdShow="<%=isShow%>"   itemName="<%=checkItem.getCheckItemName()%>">
					<input style="width:95%;padding:0px;text-align:center;" hisParent="<%=checkItem.getInspectionType() %>" fieldName="unqualifiedAmount" name="a<%=flag %>_unqualifiedAmount" value="<%=checkItem.getUnqualifiedAmount() %>" readonly="true"></input>
				</td> 
				<td  tdShow="<%=isShow%>" style="text-align: center;border-right:0px;" itemName="<%=checkItem.getCheckItemName()%>">
					<input type="hidden" fieldName="conclusion" name="a<%=flag %>_conclusion" value="<%=checkItem.getConclusion()%>" itemName="<%=checkItem.getCheckItemName()%>" hisParent="<%=checkItem.getInspectionType() %>"/>
					<span name="conclusionSpan" fieldName="conclusionSpan">
					<%if(checkItem.getConclusion().equals("NG")) {%> 
					<font color="red">不合格</font><%}else{ %>
					 合格<%} %> </span> 
				</td>
				<%if(!"欧菲科技-神奇工场".equals(ContextUtils.getCompanyName())){%>
				    <td style="text-align: center;" itemName="<%=checkItem.getCheckItemName()%>"  tdShow="<%=isShow%>" name="attachmentSon">
			 		<span  class="ui-icon ui-icon-image uploadBtn" style="cursor:pointer;float: left;" id="a<%=flag %>_attachmentFiles_uploadBtn" onclick="uploadFiles('a<%=flag %>_showAttachment','a<%=flag %>_attachmentFiles');"></span>
<%-- 					<button uploadBtn=true type="button" class='btn' id="a<%=flag %>_attachmentFiles_uploadBtn" onclick="uploadFiles('a<%=flag %>_showAttachment','a<%=flag %>_attachmentFiles');"><span><span><b class="btn-icons btn-icons-upload"></b></span></span></button>	 --%>
					<input type="hidden" name="a<%=flag %>_attachmentFiles" id="a<%=flag %>_attachmentFiles" value="<%=checkItem.getAttachmentFiles()%>"></input>
					<span id="a<%=flag %>_showAttachment"><%=checkItem.getAttachmentFiles()==null?"":checkItem.getAttachmentFiles()%></span>
				   </td>
				<%}%>
				
				<td  tdShow="<%=isShow%>" style="text-align: center;" itemName="<%=checkItem.getCheckItemName()%>">
					<%=checkItem.getStandardRemark()==null?"":checkItem.getStandardRemark()%>
				</td>
				<td  tdShow="<%=isShow%>" style="text-align: center;" itemName="<%=checkItem.getCheckItemName()%>">					
					<input style="width: 66%;float: left;text-align:center;" type="text" fieldName="inspectionMan" id="a<%=flag %>_inspectionMan" name="a<%=flag %>_inspectionMan" value="<%=checkItem.getInspectionMan()==null?"":checkItem.getInspectionMan()%>" />
					<a style="margin-left:2px;float:left;" href="javascript:void(0);" class="small-button-bg"  onclick='selectObj("选择检验员","a<%=flag %>_inspectionMan","a<%=flag %>_inspectionMan");' title="选择检验员"><span class="ui-icon ui-icon-search" style='cursor:pointer;'></span></a>

<%-- 					<%=checkItem.getInspectionMan()==null?"":checkItem.getInspectionMan()%> --%>
				</td>
			</tr>
		<%} %>
	</tbody>
</table>
<input type="hidden" name="flagIds" value="<%=flagIds%>">
<%}else{ %>
<div style="line-height:24px;padding-left:4px;font-weight:bold;">
	免检物料,没有检验项目
</div>
<%} %>
<script type="text/javascript">
	var sampleSchemeType = '<%=sampleSchemeType%>',schemeStartDate = '<%=schemeStartDateStr%>';
	<%
		String html = sampleSchemeType;
	    String readpicture="";
		//if(checkBomCode!=null&&checkBomCode.length()>0){
			//String iqcctx=request.contextPath+"/iqc";
			//readpicture="<a href=\\\"#\\\" onclick=\\\"showPicture(\\'"+checkBomCode+"\\\')\\\">图纸查看</a>";
			//readpicture="<a href='"+request.getContextPath()+"/iqc/inspection-report/show-indicator-picture.htm?mid="+checkBomCode+"' target='_blank1'>图纸查看</a>";
		//}else{
		//	readpicture="";
		//}
		if(SampleScheme.TIGHTEN_TYPE.equals(sampleSchemeType)){
			html = "<font color=red><b>" + sampleSchemeType + "</font></b>&nbsp;&nbsp;&nbsp;&nbsp;"+readpicture;
		}else if(SampleScheme.ORDINARY_TYPE.equals(sampleSchemeType)){
			html = "<b>" + sampleSchemeType + "</b>&nbsp;&nbsp;&nbsp;&nbsp;"+readpicture;
		}
	%>
	$("input[name=sampleSchemeType]").val(sampleSchemeType)
									.parent().find("span")
									.html("<%=html%>");
	$("input[name=schemeStartDate]").val(schemeStartDate); 
	$("td[tdShow=true]").show();
	$("td[tdShow=false]").hide();
	$(document).ready(
	<%-- 	function(){
			if('${id}'!=""&&'${inspectionState}'!='<%=IncomingInspectionActionsReport.INPECTION_STATE_AUDIT%>'&&'${inspectionState}'!='<%=IncomingInspectionActionsReport.INPECTION_STATE_SUBMIT%>'&&'${inspectionState}'!='<%=IncomingInspectionActionsReport.INPECTION_STATE_LAST_SUBMIT%>'){
				choiceWaitCheckItem();
			}
		} --%>
	);
</script>