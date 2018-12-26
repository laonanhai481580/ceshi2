<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>选择部件</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript">
		function contentResize(){
			$("#positionCodeList").jqGrid("setGridWidth",$(".opt-btn").width()+12);
			$("#positionCodeList").jqGrid('setGridHeight',$(document).height() - $(".opt-btn").height() - 60);
		}
	</script>
	<script type="text/javascript" src="${ctx}/js/common-layout.js"></script>
	<script type="text/javascript">
		var topMenu = 'aaa';
		$(document).ready(function(){
			createBom();
		});
		//创建不良部件
		function createBom(){
			$("#positionCodeList").jqGrid({
				url:'${mfgctx}/common/component-list-datas.htm',
// 				postData : {componentId : initially_select},
				datatype: 'json',
				prmNames:{
					rows:'comPage.pageSize',
					page:'comPage.pageNo',
					sort:'comPage.orderBy',
					order:'comPage.order'
				},
				rowNum: 20,
				rownumbers: true,
				pager: '#pager',
				height: 450,
				colNames:['','部件代码', '部件名称'],
				colModel:[
					{name:'id', index:'id', width:1, hidden:true,align:'center'},
		            {name:'componentNo', index:'componentNo', width: 100,align:'left',editable:false},
		            {name:'componentName', index:'componentName', width: 180,align:'center',editable:false}
		        ],
				multiselect: true,
				gridComplete: function(){}
			});
			contentResize();
		}
		//确定
		function realSelect(){
			var ids = jQuery("#positionCodeList").getGridParam('selarrrow');
			if(ids.length == 0){
				alert("请选择部件!");
				return;
			}
			if($.isFunction(window.parent.setComponentValue)){
				var data = [];
				for(var i=0;i < ids.length;i++){
					var objs = $("#positionCodeList").jqGrid('getRowData',ids[i]);					
					if(objs){
						data.push({key:objs.componentNo,value:objs.componentName});
					}
				}	
				if(data.length > 0){
					//选择部件后调用父窗口的setComponentValue（data）方法，将部件信息返回到打开窗口的父页面
					window.parent.setComponentValue(data);
				}else{
					alert("选择的值不存在!");
				}
				window.parent.$.colorbox.close();
			}else{
				alert("页面还没有 setComponentValue方法!");
			}
		}
		//取消
		function cancel(){
			window.parent.$.colorbox.close();
		}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn">
			<button class="sexybutton" onclick="realSelect();"><span class="ok">确定 </span></button>	
			<button class="sexybutton" onclick="cancel();"><span class="cancel">取消</span></button>
			</div>
			<form id="contentForm" name="contentForm" method="post"  action="">
				<table id="positionCodeList"></table>
				<div id="pager"></div>	
			</form>
		</div>
	</div>
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
</html>