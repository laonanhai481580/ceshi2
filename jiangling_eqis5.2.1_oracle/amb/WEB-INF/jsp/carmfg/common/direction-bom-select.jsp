<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>选择部件</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript">
		function contentResize(){
			$("#list").jqGrid("setGridWidth",$(".opt-btn").width()+12);
			$("#list").jqGrid('setGridHeight',$(document).height() - $(".opt-btn").height() - 60);
		}
	</script>
	<script type="text/javascript" src="${ctx}/js/common-layout.js"></script>
	<script type="text/javascript">
		var topMenu = 'aaa';
		$(document).ready(function(){
			createBom();
		});
		//创建方位
		function createBom(){
			$("#list").jqGrid({
				url:'${mfgctx}/common/direction-list-datas.htm',
// 				postData : {componentId : initially_select},
				datatype: 'json',
				prmNames:{
					rows:'dirPage.pageSize',
					page:'dirPage.pageNo',
					sort:'dirPage.orderBy',
					order:'dirPage.order'
				},
				rowNum: 20,
				rownumbers: true,
				pager: '#pager',
				height: 450,
				colNames:['','方位代码', '方位名称'],
				colModel:[
					{name:'id', index:'id', width:1, hidden:true,align:'center'},
		            {name:'directionCodeNo', index:'directionCodeNo', width: 100,align:'left',editable:false},
		            {name:'directionCodeName', index:'directionCodeName', width: 180,align:'center',editable:false}
		        ],
				multiselect: true,
				gridComplete: function(){}
			});
			contentResize();
		}
		//确定
		function realSelect(){
			var ids = jQuery("#list").getGridParam('selarrrow');
			if(ids.length == 0){
				alert("请选择方位!");
				return;
			}
			if($.isFunction(window.parent.setDirectionValue)){
				var data = [];
				for(var i=0;i < ids.length;i++){
					var objs = $("#list").jqGrid('getRowData',ids[i]);					
					if(objs){
						data.push({key:objs.directionCodeNo,value:objs.directionCodeName});
					}
				}	
				if(data.length > 0){
					//选择方位后调用父窗口的setDirectionValue（data）方法，将部件信息返回到打开窗口的父页面
					window.parent.setDirectionValue(data);
				}else{
					alert("选择的值不存在!");
				}
				window.parent.$.colorbox.close();
			}else{
				alert("页面还没有 setDirectionValue方法!");
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
			<button class="btn" onclick="realSelect();"><span><span><b class="btn-icons btn-icons-ok"></b>确定</span></span></button>	
				<button class="btn" onclick="cancel();"><span><span><b class="btn-icons btn-icons-cancel"></b>取消</span></span></button>
			</div>
			<form id="contentForm" name="contentForm" method="post"  action="">
				<table id="list"></table>
				<div id="pager"></div>	
			</form>
		</div>
	</div>
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
</html>