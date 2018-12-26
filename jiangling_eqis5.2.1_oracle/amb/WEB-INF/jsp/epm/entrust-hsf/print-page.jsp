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
	.Tbr_Wp{padding:6px;}
	.Tbr_Top{border-bottom:2px solid #bbb; background:#e4e4e4;}
	.Tbr_Btm{border-top:2px solid #bbb; background:#e4e4e4;}
	.AtcItem{color:#777;}
	-->
	tr{
		height:40px;
	}
	</style>
	<style type="text/css"  media="print">
		.unPrint{display:none;}
	</style>
</head>
<body style="font-size:5px;">
	<div class="Tbr_Wp Tbr_Btm unPrint" style="width:890px;margin:auto;">
		<button class='btn' onclick="window.print();"><span><span><b class="btn-icons btn-icons-print"></b>打印</span></span></button>
		<button class='btn' onclick="window.close();"><span><span><b class="btn-icons btn-icons-cancel"></b>关闭页面</span></span></button>
	</div>
	<table class="form-table-without-border" style="width:100%;margin: auto;">
		<caption><h2>HSF试验委托单</h2></caption>
		<caption style="text-align:left;padding-bottom:4px;">表单.NO:${formNo}</caption>
	</table>
	<table class="form-table-border-left" style="border:0px;width:100%;">
		<tr>
			<td style="width:10%;text-align:center;">申请人</td>
			<td style="width:10%">${consignor }</td>
			<td style="width:10%;text-align:center;">申请部门</td>
			<td>${consignorDept}</td>
			<td style="width:10%;text-align:center;">申请日期</td>
			<td ><s:date name='consignableDate' format="yyyy-MM-dd"/></td>
			<td style="width:10%;text-align:center;">样品数量</td>
			<td>${quantity }</td>
		</tr>
		<tr>
			<td style="width:10%;text-align:center;">实验目的:</td>
			<td colspan="7">${purpose}</td>
		</tr>
		<tr>
			<td style="width:10%;text-align:center;">附件</td>
			<td >${aimFile }</td>
			<td style="width:15%;text-align: center;">是否留样:</td>
			<td>${sampleHandling}</td>
			<td style="width:15%;text-align: center;">管理编号:</td>
			<td>${managerAssets}</td>
			<td colspan="2"></td>
		</tr>
		<tr>
			<td colspan="8" style="text-align:center;">样品描述 </td>
		</tr>
		<tr>
			<td colspan="8" style="padding:0px;" id="checkItemsParent">
			<div style="overflow-x:auto;overflow-y:hidden;">
				<table class="form-table-border-left" style="border:0px;table-layout:fixed;">
					<tbody>
						<tr >
							<td style="width:100px;text-align:center;border-top:0px;">样品名称</td>
							<td style="width:100px;text-align:center;border-top:0px;">规格型号</td>
							<td style="width:100px;text-align:center;border-top:0px;">批号</td>
							<td style="width:30px;text-align:center;border-top:0px;">数量</td>
							<td style="width:200px;text-align:center;border-top:0px;">供应商</td>
							<td style="width:120px;text-align:center;border-top:0px;">客户</td>
							<td style="width:150px;text-align:center;border-top:0px;">测试项目</td>
							<td style="width:120px;text-align:center;border-top:0px;">测试结果</td>
							<td style="width:150px;text-align:center;border-top:0px;">备注</td>
						</tr>
						<s:iterator value="_entrustHsfSublists" id="item" var="item">
							<tr class="entrustHsfSublists">
								<td style="text-align: center;">${sampleName }</td>
								<td style="text-align: center;">${model }</td>
								<td style="text-align: center;">${lotNo }</td>
								<td style="text-align: center;">${amount }</td>
								<td style="text-align: center;" >${supplier }</td>
								<td style="text-align: center;">${client }</td>
								<td>${testItem }</td>
								<td style="text-align: center;">${testAfter }</td>
								<td style="text-align: center;">${remark }</td>
							</tr>
						</s:iterator>
					</tbody>
				</table>
		</div>
			</td>
		</tr>
		<tr>
			<td >确认部门</td>
			<td colspan="3">${confirmDept }
			</td>
			<td >实验室排程</td>
			<td colspan="3">
				${schedule }
			</td>
		</tr>
		<tr>
			<td >实验室测试员</td>
			<td colspan="3">
				${tester }
			</td>
			<td >报告审核</td>
			<td colspan="3">
				${reportAudit }
			</td>
		</tr>
	</table>
</body>
</html>