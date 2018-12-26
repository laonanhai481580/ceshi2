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
	function initDatatableGrid(){
		var systemWidth = $("#opt-content").width()-4;
		var systemHeight = $("#opt-content").height()-52;
		var formatterFuncJson = ${formatterFuncJson};
		var colModel = ${colModel};
		for(var i=0;i<colModel.length;i++){
			var name = colModel[i].name;
			if(formatterFuncJson[name]){
				colModel[i].formatter = formatters[formatterFuncJson[name]];
			}
		}
		var url = '${chartdesignctx}/list-definition/view-grid-datas.htm';
		var postData = {
			id : '${goalDataSourceId}',
			seriesId : '${seriesId}',
			chartDefinitionCode : '${chartDefinitionCode}',
			_compareType:'${_compareType}',
			_compareValues:'${_compareValues}'
		};
		if(window.parent
			&&window.parent.window.chartView
			&&window.parent.window.chartView.getParams){
			var paramArrs = window.parent.window.chartView.getParams();
			if(paramArrs){
				postData.searchParams = '{' + paramArrs.join(",") + "}";
			};
		}
		$("#dataTable").jqGrid({
			rownumbers : true,
			url:url,
			width : systemWidth,
			postData:postData,
			pager : '#dataTablePager',
			rowNum : 15,
			prmNames:{
				rows:'dataTablePage.pageSize',
				page:'dataTablePage.pageNo',
				sort:'dataTablePage.orderBy',
				order:'dataTablePage.order'
			},
			colModel: colModel,
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
	//附件格式化
	var formatters = {};
	formatters['_attachmentFormatter'] = function(value,o,rowObject){
		return value;
	};
	//数字格式化
	var numWidthJson = ${numWidthJson};
	formatters['_numberFormatter'] = function(value,o,rowObject){
		if(isNaN(value)||value==''){
			return value;
		}else{
			value = parseFloat(value);
			if(value != undefined){
				return value.toFixed(numWidthJson[o.colModel.name]);
			}else{
				return "";
			}
		}
	}
	//自定义的格式化
	<s:iterator value="customFormatterColumns" id="column">
	formatters['_${column.name}CustomFormatter'] = function(value,o,rowObject){
		${column.formatterValue};
	}
	</s:iterator>
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