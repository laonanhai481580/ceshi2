<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="com.norteksoft.product.api.entity.Option"%>
<%@page import="com.opensymphony.xwork2.util.ValueStack"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
<script type="text/javascript">
	$(function(){
		initForm();
	});
	function initForm(){
		//初始化数据表
		initDatatableGrid();
	}
	
	//初始化数据表
	<%
		String isSession = request.getParameter("isSession");
		isSession = StringUtils.isEmpty(isSession)?"":isSession;
	%>
	function initDatatableGrid(){
		var systemWidth = $("#opt-content").width()-4;
		var systemHeight = $("#opt-content").height()-52;
		$("#dataTable").jqGrid({
			rownumbers : true,
			url:'${chartdesignctx}/base-info/datasource/view-grid-datas.htm?id=${id}&isSession=<%=isSession%>',
			width : systemWidth,
			pager : '#dataTablePager',
			rowNum : 15,
			prmNames:{
				rows:'dataTablePage.pageSize',
				page:'dataTablePage.pageNo',
				sort:'dataTablePage.orderBy',
				order:'dataTablePage.order'
			},
			colModel: ${colModel},
		    multiselect: true,
		   	autowidth: true,
			forceFit : true,
		   	shrinkToFit: false,
			viewrecords: true, 
			sortorder: "desc",
			gridComplete : function(){}
		});
		$("#dataTable").jqGrid("setGridHeight",systemHeight);
	}
	function search(){
		$("#dataTable").jqGrid("setGridParam",{
			postData:{
				searchName : $(":input[name=searchName]").val()
			},
			page:1
		}).trigger("reloadGrid");
	}
	//取消
	function cancel(){
		window.parent.$.colorbox.close();
	}
</script>
</head>
<body>
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn">
				<button class='btn' type="button" onclick="cancel();"><span><span><b class="btn-icons btn-icons-cancel"></b>关闭窗口</span></span></button>
				<span id="message" style="color:red;">
					<s:actionmessage theme="mytheme" />
				</span>
			</div>
			<div id="opt-content">
				<table id="dataTable"></table>
				<div id="dataTablePager"></div>
			</div>
		</div>
	</div>
</body>
</html>