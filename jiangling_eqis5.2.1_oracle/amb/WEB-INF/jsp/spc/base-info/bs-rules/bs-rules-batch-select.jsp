<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<style>
<!--
	#searchUl{
		margin:0px;
		padding:0px;
	}
	#searchUl li{
		float:left;
		width:260px;
		height:24px;
		line-height:24px;
		list-style:none;
	}
	#searchUl li select{
		width:178px;
	}
	.input{
		width:170px;
	}
	.label{
		float:left;
		width:80px;
		text-align:right;
		padding-right:2px;
	}
	#groupUl{
		margin:0px;
		padding:0px;
	}
	#groupUl li{
		float:left;
		width:95px;
		height:24px;
		line-height:24px;
		list-style:none;
	}
	#groupUl li.last{
		padding:0px;
		width:280px;
		margin-bottom:2px;
		text-align:right;
	}
-->
</style>
<script type="text/javascript">
/******************** 页面使用方法 ******************/
	//选择判断规则
	function selectBsRules(){
		var url='${spcctx}/bs-rules/bs-rules-select.htm';
		$.colorbox({href:url,iframe:true, innerWidth:1000, innerHeight:600,
			overlayClose:false,
			title:"选择判断规则"
		});
	}
 	//选择之后的方法 data格式{key:'a',value:'a'}
	function setBsBatchRules(data){
		alert("select is key:" + data.key + ",value:" + data.value);
	}

/********************* 页面使用方法结束 ********************/
	isUsingComonLayout=false;
	var multiselect = "${multiselect}";
	var params = '';
	$(document).ready(function(){
		contentResize();
	});
	
	function modelFormatter(cellvalue, options, rowObject){
		if(cellvalue=='0'){
			return '趋势型';
		}else if(cellvalue=='1'){
			return '运行型';
		}else if(cellvalue=='2'){
			return '交替型';
		}else if(cellvalue=='3'){
			return '其他';
		}
		
	}
	
	function typeFormatter(cellvalue, options, rowObject){
		if(cellvalue=='0'){
			return '主控制图';
		}else if(cellvalue=='1'){
			return '副控制图';
		}
	}
	
	function contentResize(){
		var height = $(window).height() - 115;
		var display = $("#searchDiv").css("display");
		if(display=='block'){
			height -= $("#searchDiv").height();
			$(".search").html("隐藏查询");
		}else{
			height += 10;
			$(".search").html("显示查询");
		}
		$("#bsRules").jqGrid("setGridHeight",height);
	}
	
	function toggleSearchBtn(){
		var display = $("#searchDiv").css("display");
		if(display=='block'){
			$("#searchDiv").css("display","none");
		}else{
			$("#searchDiv").css("display","block");
		}
		contentResize();
	}
	//确定
	function realSelect(){
		var ids = $("#bsRules").jqGrid("getGridParam","selarrrow");
		if(ids.length == 0){
			alert("请选择判断规则!");
			return;
		}
		if(!multiselect&&ids.length > 1){
			alert("只能选择一条请选择判断规则!");
			return;
		}
		var objs = [];
		for(var i=0;i<ids.length;i++){
			var data = $("#bsRules").jqGrid('getRowData',ids[i]);
			if(data){
				objs.push(data);
			}
		}
		if($.isFunction(window.parent.setBsBatchRules)){
			var objs = [];
			for(var i=0;i<ids.length;i++){
				var data = $("#bsRules").jqGrid('getRowData',ids[i]);
				if(data){
					objs.push(data);
				}
			}
			if(objs.length>0){
				window.parent.setBsBatchRules(objs);
				window.parent.$.colorbox.close();
			}else{
				alert("选择的值不存在!");
			}
		}else{
			alert("页面还没有 setBsRules 方法!");
		}
	}
	//取消
	function cancel(){
		window.parent.$.colorbox.close();
	}
		
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<div class="opt-body">
		<div class="opt-btn" id="btnDiv">
			<button class='btn' onclick="realSelect();">
				<span><span><b class="btn-icons btn-icons-ok"></b>确定</span></span>
			</button>
			<button class='btn' onclick="cancel();">
				<span><span><b class="btn-icons btn-icons-cancel"></b>取消</span></span>
			</button>
			<button class='btn' onclick="showSearchDIV(this);">
				<span><span><b class="btn-icons btn-icons-search"></b>查询</span></span>
			</button>
		</div>
		<div id="opt-content" style="padding-top:6px;margin:0px;">
			<form id="contentForm" name="contentForm" method="post" action="">
				<grid:jqGrid gridId="bsRules"  url="${spcctx}/base-info/bs-rules/list-datas.htm" code="SPC_BS_RULES" pageName="page"></grid:jqGrid>
			</form>
		</div>
	</div>
</body>
</html>