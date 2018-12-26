<%@page import="com.ambition.carmfg.entity.MfgCheckInspectionReport"%>
<%@page import="com.ambition.carmfg.entity.MfgInspectingItem"%>
<%@page import="com.ambition.carmfg.entity.MfgCheckItem"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="com.ambition.iqc.entity.SampleScheme"%>
<%@page import="org.apache.commons.beanutils.PropertyUtils"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.ambition.iqc.entity.InspectingItem"%>
<%@page import="com.ambition.iqc.entity.SampleCodeLetter"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.util.Map"%>
<%@page import="com.norteksoft.product.api.entity.Option"%>
<%@page import="java.util.List"%>
<%@ page language="java"  pageEncoding="UTF-8"%>
<%
    DecimalFormat dec = new DecimalFormat("0.00000");
	String sampleSchemeType = SampleScheme.ORDINARY_TYPE;
	List<MfgCheckItem> checkItems = (List<MfgCheckItem>)request.getAttribute("checkItems");
	int max = 10,defaultColumns = 5;
	Map<Long,Map<String,Double>> resultMap = new HashMap<Long,Map<String,Double>>();
	long idFlag = 0;
	StringBuffer flagIds = new StringBuffer("");
	for(MfgCheckItem checkItem : checkItems){
		if(checkItem.getId()==null){
			idFlag++;
			checkItem.setId(idFlag);
		}
		if(!MfgInspectingItem.COUNTTYPE_COUNT.equals(checkItem.getCountType())){
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
%>
 <table class="form-table-border-left" style="border:0px;table-layout:fixed;">
	<thead>
		<tr>
			<td style="width:20px;text-align:center;border-top:0px;border-left:0px;">NO</td>
<!-- 			<td style="width:80px;text-align:center;border-top:0px;">分类</td> -->
			<td style="width:100px;text-align:center;border-top:0px;">检查项目</td>
			<td style="width:100px;text-align:center;border-top:0px;">检查方法</td>
			<td style="width:45px;text-align:center;border-top:0px;">检验数量</td>
			<td style="width:80px;text-align:center;border-top:0px;">规格</td>
			<td style="width:45px;text-align:center;border-top:0px;">上限</td>
			<td style="width:45px;text-align:center;border-top:0px;">下限</td>
			<td style="width:45px;text-align:center;border-top:0px;">单位</td>
			<td style="width:<%=max*35 + 50%>px;padding:0px;border-top:0px;text-align: center;" id="checkItemsHeader">
				检验记录
			</td>
			<td style="width:80px;text-align:center;border-top:0px;">仪器编号</td>
			<td style="width:40px;text-align: center;border-top:0px;">合格数</td>
			<td style="width:40px;text-align: center;border-top:0px;">不良数</td>
			<td style="width:60px;text-align: center;border-top:0px;">合格率(%)</td>
			<td style="width:40px;text-align: center;border-top:0px;">判定</td>
		 	<td style="width:100px;text-align: center;border-top:0px;">附件</td> 
			<td style="width:100px;text-align: center;border-top:0px;">测试大项</td>
			<td style="width:80px;text-align: center;border-top:0px;border-right:0px;">机台编号</td>
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
			boolean isShow=false;
			for(MfgCheckItem checkItem : checkItems){
				flag++;
				if(flag==checkItems.size()){
					isLast = true;
				}
				if(checkItem.getItemStatus().equals("已领取")){
					isShow=true;
				}else{
					isShow=false;
				}
				flagIds.append(",a" + flag);
		%>
				<tr name="checkItemTr">
					<td style="text-align: center;border-left:0px;" name="<%=checkItem.getCheckItemName()%>"  itemName="<%=checkItem.getCheckItemName()%>" tdShow="<%=isShow%>"><%=i++ %></td>
 				<%-- <%  
 					if(checkItem.getParentItemName() != null && !checkItem.getParentRowSpan().equals(1)){ 
 				%>  
 				<td rowspan="<%=checkItem.getParentRowSpan() %>" style="text-align: center;"><%=checkItem.getParentItemName()==null?"": checkItem.getParentItemName()%></td> 
 				<%
 					} else if(checkItem.getParentItemName() != null && checkItem.getParentRowSpan().equals(1)){
 				%>
				<td style="text-align: center;"><%=checkItem.getParentItemName()==null?"": checkItem.getParentItemName()%></td>
				<%} %> --%>
				<td style="text-align: center;" itemName="<%=checkItem.getCheckItemName()%>"  tdShow="<%=isShow%>">			
					<input type="hidden" fieldName="itemStatus"  name="a<%=flag %>_itemStatus" itemStatus="<%=checkItem.getCheckItemName()%>" value="<%=checkItem.getItemStatus()==null?"":checkItem.getItemStatus()%>"/>
					<input type="hidden" fieldName="inspector" name="a<%=flag %>_inspector" inspector="<%=checkItem.getCheckItemName()%>" value="<%=checkItem.getInspector()==null?"":checkItem.getInspector()%>"/>
					<input type="hidden" fieldName="unit" name="a<%=flag %>_unit" value="<%=checkItem.getUnit()==null?"":checkItem.getUnit()%>"/>
					<input type="hidden" fieldName="minlimit" name="a<%=flag %>_minlimit" value="<%=checkItem.getMinlimit()==null?"":checkItem.getMinlimit()%>"/>
					<input type="hidden" fieldName="maxlimit" name="a<%=flag %>_maxlimit" value="<%=checkItem.getMaxlimit()==null?"":checkItem.getMaxlimit()%>"/>
					<input type="hidden" fieldName="parentRowSpan" name="a<%=flag %>_parentRowSpan" value="<%=checkItem.getParentRowSpan()==null?"":checkItem.getParentRowSpan()%>"/>
					<input type="hidden" fieldName="parentItemName" name="a<%=flag %>_parentItemName" value="<%=checkItem.getParentItemName()==null?"":checkItem.getParentItemName()%>"/>
					<input type="hidden" fieldName="checkItemName" name="a<%=flag %>_checkItemName" value="<%=checkItem.getCheckItemName()==null?"":checkItem.getCheckItemName().replaceAll("\n","")%>"/>
					<input type="hidden" fieldName="codeLetter" name="a<%=flag %>_codeLetter" value="<%=checkItem.getCodeLetter()==null?"":checkItem.getCodeLetter()%>"/>
					<input type="hidden" fieldName="inspectionType" name="a<%=flag %>_inspectionType" value="<%=checkItem.getInspectionType()==null?"":checkItem.getInspectionType()%>"/>
					<input type="hidden" fieldName="countType" name="a<%=flag %>_countType" value="<%=checkItem.getCountType()==null?"":checkItem.getCountType()%>"/>
					<input type="hidden" fieldName="aql" name="a<%=flag %>_aql" value="<%=checkItem.getAql()==null?"":checkItem.getAql()%>"/>
					<input type="hidden" fieldName="aqlAc" name="a<%=flag %>_aqlAc" value="<%=checkItem.getAqlAc()==null?"":checkItem.getAqlAc()%>"/>
					<input type="hidden" fieldName="aqlRe" name="a<%=flag %>_aqlRe" value="<%=checkItem.getAqlRe()==null?"":checkItem.getAqlRe()%>"/>
					<input type="hidden" fieldName="isJnUnit" name="a<%=flag %>_isJnUnit" value="<%=checkItem.getIsJnUnit()==null?"":checkItem.getIsJnUnit()%>"/>
					<input type="hidden" name="spcSampleIds" value="<%=checkItem.getSpcSampleIds()==null?"":checkItem.getSpcSampleIds()%>"/>
					<input type="hidden" fieldName="remark" name="a<%=flag %>_remark" value="<%=checkItem.getRemark()==null?"":checkItem.getRemark()%>"/>
					<%-- <%=checkItem.getCheckItemName()==null?"":checkItem.getCheckItemName().replaceAll("\n","")%> --%>
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
				<td style="text-align: center;" itemName="<%=checkItem.getCheckItemName()%>"  tdShow="<%=isShow%>">
					<input type="hidden" fieldName="inspectionAmount" name="a<%=flag %>_inspectionAmount" value="<%=checkItem.getInspectionAmount()==null?"":checkItem.getInspectionAmount()%>"/>
					<%=checkItem.getInspectionAmount()==null?"":checkItem.getInspectionAmount() %>
				</td>
				<td style="text-align: center;word-wrap:break-word;word-break:break-all;" itemName="<%=checkItem.getCheckItemName()%>"  tdShow="<%=isShow%>">
					<input type="hidden" fieldName="specifications" name="a<%=flag %>_specifications" value="<%=checkItem.getSpecifications()==null?"":checkItem.getSpecifications()%>"/>
					<%=checkItem.getSpecifications()==null?"":checkItem.getSpecifications() %>
				</td>
				<td style="text-align: center;" itemName="<%=checkItem.getCheckItemName()%>"  tdShow="<%=isShow%>">
<%-- 					<%=checkItem.getFeatureId()==null?"":checkItem.getFeatureId() %> --%>
					<%=checkItem.getMaxlimit()==null?"":checkItem.getMaxlimit() %>
				</td>
				<td style="text-align: center;" itemName="<%=checkItem.getCheckItemName()%>"  tdShow="<%=isShow%>">
					<%=checkItem.getMinlimit()==null?"":checkItem.getMinlimit() %>
				</td>
				<td style="text-align: center;" itemName="<%=checkItem.getCheckItemName()%>"  tdShow="<%=isShow%>">
					<%=checkItem.getUnit()==null?"":checkItem.getUnit() %>
				</td>
				<td style="text-align: center;display: none;">
					<input type="hidden" fieldName="featureId" name="a<%=flag %>_featureId" value="<%=checkItem.getFeatureId()==null?"":checkItem.getFeatureId()%>"/>
					<%=checkItem.getFeatureId()==null?"":checkItem.getFeatureId() %>
				</td>
				<td style="padding:0px;" class="checkItemsClass" itemName="<%=checkItem.getCheckItemName()%>"  tdShow="<%=isShow%>">
					<%
						if(InspectingItem.COUNTTYPE_COUNT.equals(checkItem.getCountType())){
					%>
							<textarea style="width:98%;height:100%;" rows=3 fieldName="results" name="a<%=flag %>_results" class="{maxlength:1000}"><%=checkItem.getResults()==null?"":checkItem.getResults() %></textarea>	
					<%}else{ 
							Map<String,Double> tempMap = resultMap.get(checkItem.getId());
						 	int count = checkItem.getInspectionAmount()==null?tempMap.keySet().size():checkItem.getInspectionAmount();
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
								<input results=true onchange="this.value=this.value.replace(' ','');" canSee="<%=checkItem.getCheckItemName()%>" color="<%=color %>" style="width:50px;float:left;margin-left:2px;color:<%=color %>" title="<%=checkItem.getCheckItemName() + "样品" + (j+1) %>" fieldName="<%=name%>" name="a<%=flag %>_<%=name%>" value="<%=value==null?"":new BigDecimal(new Double(value).toString())%>" ></input>
						<%
							}
							if(count<30){
						%>
								<a class="small-button-bg" style="margin-left:2px;float:left;" onclick="addResultInput(this,'<%=checkItem.getCheckItemName() %>')" href="#" title="添加检验项"><span class="ui-icon ui-icon-plus" style='cursor:pointer;'></span></a>
								<a class="small-button-bg" style="margin-left:2px;float:left;" onclick="removeResultInput(this)" href="#" title="删除最后的检验项"><span class="ui-icon ui-icon-minus" style='cursor:pointer;'></span></a>
						<%} %>
					<%} %>
				</td>
				<td style="text-align: center;" itemName="<%=checkItem.getCheckItemName()%>"  tdShow="<%=isShow%>">
					<input style="width:95%;padding:0px;text-align:center;" fieldName="testEquipmentNo" name="a<%=flag %>_testEquipmentNo" value="<%=checkItem.getTestEquipmentNo()==null?"": checkItem.getTestEquipmentNo()%>"/>
				</td>
				<td style="text-align: center;" itemName="<%=checkItem.getCheckItemName()%>"  tdShow="<%=isShow%>">
					<input style="width:95%;padding:0px;text-align:center;" fieldName="qualifiedAmount"  name="a<%=flag %>_qualifiedAmount" value="<%=checkItem.getQualifiedAmount() %>" class="{required:true,digits:true,min:0}"></input>
				</td>
				<td style="text-align: center;" itemName="<%=checkItem.getCheckItemName()%>"  tdShow="<%=isShow%>">
					<input style="width:95%;padding:0px;text-align:center;" fieldName="unqualifiedAmount" name="a<%=flag %>_unqualifiedAmount" value="<%=checkItem.getUnqualifiedAmount() %>" class="{required:true,digits:true}"></input>
				</td>
				<td style="text-align: center;" itemName="<%=checkItem.getCheckItemName()%>"  tdShow="<%=isShow%>">
					<input style="width:95%;padding:0px;text-align:center;float: left;" fieldName="qualifiedRate" name="a<%=flag %>_qualifiedRate" value="<%=checkItem.getQualifiedRate()%>" ></input>
				</td>
				<td style="text-align: center;border-right:0px;" itemName="<%=checkItem.getCheckItemName()%>"  tdShow="<%=isShow%>">
					<input type="hidden" fieldName="conclusion" name="a<%=flag %>_conclusion" value="<%=checkItem.getConclusion()%>"/>
					<%
							if(checkItem.getConclusion().equals("NG")){
						%>
					<span style="color: red;" fieldName="conclusionSpan" name="conclusionSpan"><%=checkItem.getConclusion() %></span>
					<%}else{ %>
					<span  fieldName="conclusionSpan" name="conclusionSpan"><%=checkItem.getConclusion() %></span>
					<%}%>
				</td>
			 	<td style="text-align: center;" itemName="<%=checkItem.getCheckItemName()%>"  tdShow="<%=isShow%>" name="attachmentSon">
			 		<span  class="ui-icon ui-icon-image uploadBtn" style="cursor:pointer;float: left;" id="a<%=flag %>_attachmentFiles_uploadBtn" onclick="uploadFiles('a<%=flag %>_showAttachment','a<%=flag %>_attachmentFiles');"></span>
					<%-- <button uploadBtn=true type="button" class='btn' id="a<%=flag %>_attachmentFiles_uploadBtn" onclick="uploadFiles('a<%=flag %>_showAttachment','a<%=flag %>_attachmentFiles');"><span><span><b class="btn-icons btn-icons-upload"></b></span></span></button> --%>	
					<input type="hidden" name="a<%=flag %>_attachmentFiles" id="a<%=flag %>_attachmentFiles" value="<%=checkItem.getAttachmentFiles()%>"></input>
					<span id="a<%=flag %>_showAttachment"><%=checkItem.getAttachmentFiles()==null?"":checkItem.getAttachmentFiles()%></span>
				</td> 
				<td style="text-align: center;" itemName="<%=checkItem.getCheckItemName()%>"  tdShow="<%=isShow%>">
					<%=checkItem.getRemark()==null?"":checkItem.getRemark()%>
				</td>
				<td style="text-align: center;" itemName="<%=checkItem.getCheckItemName()%>"  tdShow="<%=isShow%>">
					<input style="width:95%;padding:0px;text-align:center;" fieldName="equipmentNo" name="a<%=flag %>_equipmentNo" value="<%=checkItem.getEquipmentNo()==null?"": checkItem.getEquipmentNo()%>"/>
				</td>
			</tr>
		<%} %>
	</tbody>
</table> 
<input type="hidden" name="flagIds" value="<%=flagIds%>">
<script type="text/javascript">
	$("td[tdShow=true]").show();
	$("td[tdShow=false]").hide();
<%-- 	$(document).ready(
			function(){
				if('${id}'!=""&&'${reportState}'!='<%=MfgCheckInspectionReport.STATE_AUDIT%>'&&'${reportState}'!='<%=MfgCheckInspectionReport.STATE_AUDIT%>'){
					choiceWaitCheckItem();
				}
			}
		); --%>
</script>