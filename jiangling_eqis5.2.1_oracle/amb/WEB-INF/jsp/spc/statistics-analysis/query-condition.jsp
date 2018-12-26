<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="com.opensymphony.xwork2.util.ValueStack"%>
<%@page import="java.util.List"%>
<%@page import="com.ambition.spc.jlanalyse.entity.JLResult"%>
<%@page import="com.ambition.spc.entity.CPKMoudle"%>
<%@page import="com.ambition.spc.entity.FeatureLayer"%>
<%@page import="com.ambition.spc.entity.LayerType"%>
<%@page import="com.ambition.spc.entity.LayerDetail"%>
<%@ page language="java" pageEncoding="UTF-8"%>
 <%
	ValueStack valueStack1=ActionContext.getContext().getValueStack();
	List<LayerType> layerTypes=(List)valueStack1.findValue("layerTypes");
	int k=1;%>
	<%if(layerTypes!=null){
	for(int i=0;i<layerTypes.size();i++){ 
		LayerType layerType=(LayerType)layerTypes.get(i);
		if("0".equals(layerType.getSampleMethod())){
	%>
	<li class="li">
		<span class="liSpan"><input name="params.group" type="radio" value="<%=layerType.getTypeCode() %>" onclick="targetSelect(this,'select_<%=layerType.getId()%>')" /><%=layerType.getTypeName()%></span>
		<%-- <input type="hidden" name="tag_code" value="<%=layerType.getTypeCode() %>" id="tag_code"/> --%>
		<input type="text" name="params.tag_value" id="select_<%=layerType.getId()%>" class="targerSelect" disabled="disabled"/>
	</li>
	<%}else if("2".equals(layerType.getSampleMethod())||"1".equals(layerType.getSampleMethod())){%>
		<li class="li">
		<%-- <input type="hidden" name="params.tag_code" value="<%=layerType.getTypeCode() %>" id="tag_code"/> --%>
		<span class="liSpan"><input name="params.group" type="radio" value="<%=layerType.getTypeCode() %>" onclick="targetSelect(this,'select_<%=layerType.getId()%>')" /><%=layerType.getTypeName()%></span>
		<select name="params.tag_value" id="select_<%=layerType.getId()%>" class="targerSelect" disabled="disabled">
		<%List<LayerDetail> layerDetails=layerType.getLayerDetails();
		for(int j=0;j<layerDetails.size();j++){
			LayerDetail layerDetail=layerDetails.get(j);%>
		<option value="<%=layerDetail.getDetailCode()%>"><%=layerDetail.getDetailName()%></option>
		<%}%>
		</select>
		<script type="text/javascript">
		$(document).ready(function(){
			setFormWidth();
				$("#select_"+<%=layerType.getId()%>).multiselect({
					selectedList:1
				});	
		});
		</script>
		</li>
		<%}}}%>
		
<%-- <table style="width: 98%; margin-left: 10px; margin-top: 0px;" class="form-table-border-left" id="queryConditionTable">
	<%
		ValueStack valueStack1=(ValueStack)request.getAttribute("struts.valueStack");
		List featureLayers=(List)valueStack1.findValue("featureLayers");
		int k=1;
	%>
	<%if(featureLayers!=null){
	for(int i=0;i<featureLayers.size();i++){ 
		FeatureLayer featureLayer=(FeatureLayer)featureLayers.get(i);
		if(layerType.getSampleMethod().equals("0")){%>
	<li>
			<span class="label"><%=layerType.getTypeName()%></span>
			<input type="hidden" name="tag_code" value="<%=layerType.getTypeCode() %>" id="tag_code"/>
			<input type="text" name="tag_value" id="tag_value"/>
		</li>
		<%}else if(layerType.getSampleMethod().equals("2")||layerType.getSampleMethod().equals("1")){%>
			<li>
			<input type="hidden" name="tag_code" value="<%=layerType.getTypeCode() %>" id="tag_code"/>
			<span class="label"><%=layerType.getTypeName()%></span>
			<select name="tag_value">
			<%List<LayerDetail> layerDetails=layerType.getLayerDetails();
			for(int j=0;j<layerDetails.size();j++){
				LayerDetail layerDetail=layerDetails.get(j);%>
			<option value="<%=layerDetail.getDetailCode()%>"><%=layerDetail.getDetailName()%></option>
			<%}%>
			</select>
			</li>
			<%}}}%>
	<li>
		<td><%=featureLayer.getDetailName()%></td>
		<input type="hidden" name="tag_code" value="<%=featureLayer.getDetailCode() %>" id="tag_code"/>
		<td><input type="text" name="tag_value" id="tag_value"/></td>
	</li>
	<%}}%>
</table> --%>