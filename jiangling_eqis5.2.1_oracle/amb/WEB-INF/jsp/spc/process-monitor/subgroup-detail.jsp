<%@page import="com.norteksoft.product.web.struts2.query.StringUtil"%>
<%@page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@page import="com.norteksoft.acs.base.web.struts2.Struts2Utils"%>
<%@page import="com.ambition.spc.jlanalyse.entity.JLSampleData"%>
<%@page import="org.apache.commons.lang.xwork.StringUtils"%>
<%@page import="com.opensymphony.xwork2.util.ValueStack"%>
<%@page import="com.ambition.spc.entity.SpcSgTag"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript">
		var isUsingComonLayout=false;
		//关闭
		function cancel(){
			window.parent.$.colorbox.close();
		}
	</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn" id="btnDiv">
				<button class='btn' type="button" onclick="cancel();"><span><span><b class="btn-icons btn-icons-cancel"></b>关闭</span></span></button>
			</div>
			<s:if test="error">
				<div id="opt-content">
					<h2>对不起，没有符合条件的子组信息！</h2>
				</div>
			</s:if><s:else>
    		<div>
    		<%
				ValueStack valueStack = (ValueStack)request.getAttribute("struts.valueStack");
				int effectiveCapacity = (Integer)valueStack.findValue("effectiveCapacity");	
				List groupDatalist = (List)valueStack.findValue("groupDatalist");
				int k = (Integer)valueStack.findValue("groudNum");
				List<SpcSgTag> sgTagDatas = (List)valueStack.findValue("sgTagDatas");
			%>
    		<fieldset>
					<legend style="font-size: 20px;">子组信息</legend>
				    <table class="form-table-border" style="width: 98%;margin-left: 10px;margin-top: 10px;" >
					   	<tr style="height:24px;background:#99CCFF;font-weight:bold;height:29px;">
					   		<td>子组号</td>
					   		<%
								int a=0;
								for(int i=0;i<effectiveCapacity;i++){
									a++;
							%>
							<td><%="x"+a %></td>
							<%}%>
							<td>平均值</td>
							<td>最大值</td>
							<td>最小值</td>
					   	</tr>
					   	<%for(int i=0;i<groupDatalist.size();i++){ 
								JLSampleData jl = (JLSampleData)groupDatalist.get(i);
								double[] datas = jl.getData();
						%>
					   	<tr>
							<td><%=k%></td>
							<%for(int h=0;h<datas.length;h++){
								double data = datas[h];
							%>
							<td><%=data%></td>
							<%}%>
							<td><%=jl.getAverage()%></td>
							<td><%=jl.getMax()%></td>
							<td><%=jl.getMin()%></td>
						</tr>
						<%}%>
				    </table>
				</fieldset>
    		</div>
    		<div>
				<fieldset>
					<legend style="font-size: 20px;">附属信息</legend>
					<table class="form-table-border-left" style="width: 98%;margin-left: 10px;margin-top: 10px;">
            			<tr style="height:24px;background:#99CCFF;font-weight:bold;height:29px;">
	            			<td>层别</td>
	            			<td>取值</td>
	            		</tr>
	            		<%for(SpcSgTag sgTag:sgTagDatas){ %>
	            		<tr>
	            			<td><%=sgTag.getTagName() %></td>
	            			<td><%=sgTag.getTagValue() %></td>
	            		</tr>
	            		<%}%>
	            	</table>
				</fieldset>
	    	</div>
	    	</s:else>
		</div>
	</div>
</body>
</html>