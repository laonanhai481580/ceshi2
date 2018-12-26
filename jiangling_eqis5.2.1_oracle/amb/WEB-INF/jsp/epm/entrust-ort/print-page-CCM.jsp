<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="com.ambition.epm.entity.EntrustOrt"%>

<%@page import="com.norteksoft.product.util.ContextUtils"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/print-meta.jsp" %>
	<script type="text/javascript">
	<%
		String backgroundColor = "#CCFFCC";
	%>
	$(document).ready(function(){
	
	});
	</script>
	<style type="text/css">
	<!--
	.Tbr_Wp{padding:1px;}
	.Tbr_Top{border-bottom:1px solid #bbb; background:#e4e4e4;}
	.Tbr_Btm{border-top:1px solid #bbb; background:#e4e4e4;}
	.AtcItem{color:#777;}
	-->
	tr{
		height:1px;
	}
	</style>
	<style type="text/css"  media="print">
		.unPrint{display:none;}
	</style>
</head>
<body style="font-size:1px;">
	<div class="Tbr_Wp Tbr_Btm unPrint" style="width:800px;margin:auto;">
		<button class='btn' onclick="window.print();"><span><span><b class="btn-icons btn-icons-print"></b>打印</span></span></button>
		<button class='btn' onclick="window.close();"><span><span><b class="btn-icons btn-icons-cancel"></b>关闭页面</span></span></button>
	</div>
	<table class="form-table-without-border" style="width:100%;margin: auto;">
		<caption><h2>可靠性试验委托单</h2></caption>
		<caption style="text-align:left;padding-bottom:1px;">表单.NO:${formNo}</caption>
	</table>
	<table style="width:100%;margin: auto;font-size:1px;" class="form-table-border-left" id="default-table">
		<tr>
			<td style="text-align:center;width:10%;">申请人</td>
 			<td style="width:10%;">${consignor }</td>
			<td style="text-align:center;">申请部门</td>
			<td >${consignorDept}</td>
			<td style="text-align:center;">申请日期</td>
			<td style="width:13%;"><s:date name='consignableDate' format="yyyy-MM-dd"/></td>
			<td style="text-align:center;width:10%;">希望完成时间</td>
			<td style="width:13%;"><s:date name='deadline' format="yyyy-MM-dd"/></td>
			<td style="width:10%;text-align:center;">样品数量</td>
			<td style="width:5%;">${quantity}</td>
		</tr>
		<tr>
			<td style="text-align:center;">客户编号</td>
			<td>${customerNo}</td>
			<td style="text-align:center;">机种</td>
			<td >${productNo}</td>
			<td style="text-align:center;">样品类别</td>
			<td >${sampleType}</td>
			<td style="text-align:center;">批号</td>
			<td >${lotNo}</td>
			<td style="text-align: center;">是否留样:</td>
			<td style="width:5%;">${sampleHandling}</td>
		</tr>
		<tr>
			<td style="text-align:center;">产品阶段</td>
			<td colspan="9">${category}</td>
		</tr>
		<tr>
			<td style="text-align:center;"><h2>试验明细</h2></td>
			<td  style="text-align:center;">目的:</td>
			<td colspan="3">${purpose}</td>
			<td  style="text-align:center;">样品描述</td>
			<td colspan="4">${sampleDiscription }</td>
		</tr>
		<tr>
			<td>测试项目</td>
			<td colspan="10">${entrustOrtSublistse}</td>
		</tr>
		<tr>
			<td >确认部门</td>
			<td >${confirmDept }</td>
			<td >实验室排程</td>
			<td >${schedule }</td>
			<td >实验室测试员</td>
			<td >${tester }</td>
			<td >报告审核</td>
			<td >${reportAudit }</td>
			<td colspan="2"></td>
		</tr>
		
	</table>
</body>
</html>