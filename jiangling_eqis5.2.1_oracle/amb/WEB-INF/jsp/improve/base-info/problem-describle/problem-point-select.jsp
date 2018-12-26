<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>选择部件</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript">
		function contentResize(){
			$("#problemPointList").jqGrid("setGridWidth",$(".opt-btn").width()+12);
			$("#problemPointList").jqGrid('setGridHeight',$(document).height() - $(".opt-btn").height() - 60);
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
			var model=$("#model").val();
			var problemType=$("#problemType").val();
			var businessUnitName=$("#businessUnitName").val();
			$("#problemPointList").jqGrid({
				url:'${improvectx}/base-info/problem-describle/problem-list-datas.htm?problemType='+problemType+'&businessUnitName='+businessUnitName+'&model='+model,
				datatype: 'json',
				prmNames:{
					rows:'page.pageSize',
					page:'page.pageNo',
					sort:'page.orderBy',
					order:'page.order'
				},

				rowNum: 20,
				rownumbers: true,
				pager: '#pager',
				height: 450,
				colNames:['','不良现象名称', '描述'],
				colModel:[
					{name:'id', index:'id', width:1, hidden:true,align:'center'},
		            {name:'defectionPhenomenonName', index:'defectionPhenomenonName', width: 100,align:'left',editable:false},
		            {name:'remark', index:'remark', width: 180,align:'center',editable:false}
		        ],
				multiselect: true,
				gridComplete: function(){}
			});
			contentResize();
		}
		//确定
		function realSelect(){
			var ids = jQuery("#problemPointList").getGridParam('selarrrow');
			if(ids.length == 0){
				alert("请选择问题点!");
				return;
			}
			if(ids.length > 1){
				alert("只能选择一条问题点!");
				return;
			}
			if($.isFunction(window.parent.setProblemValue)){
				var data = [];
				for(var i=0;i < ids.length;i++){
					var objs = $("#problemPointList").jqGrid('getRowData',ids[i]);					
					if(objs){
						data.push({key:objs.defectionPhenomenonName,value:objs.remark});
					}
				}	
				if(data.length > 0){
					//选择部件后调用父窗口的setProblemValue（data）方法，将部件信息返回到打开窗口的父页面
					window.parent.setProblemValue(data);
				}else{
					alert("选择的值不存在!");
				}
				window.parent.$.colorbox.close();
			}else{
				alert("页面还没有 setProblemValue方法!");
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
			<button class='btn' type="button" onclick="realSelect();"><span><span><b class="btn-icons btn-icons-ok"></b>确定 </span></span></button>	
			<button class='btn' type="button" onclick="cancel();"><span><span><b class="btn-icons btn-icons-cancel"></b>取消</span></span></button>
			<input type="hidden" name="model" id="model" value="${model}"/>
			<input type="hidden" name="problemType" id="problemType" value="${problemType}"/>
			<input type="hidden" name="businessUnitName" id="businessUnitName" value="${businessUnitName }"/>
			</div>
			<form id="contentForm" name="contentForm" method="post"  action="">
				<table id="problemPointList"></table>
				<div id="pager"></div>	
			</form>
		</div>
	</div>
</body>
</html>