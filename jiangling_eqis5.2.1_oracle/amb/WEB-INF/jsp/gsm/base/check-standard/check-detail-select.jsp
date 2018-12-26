<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>选择部件</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript">
		function contentResize(){
			$("#checkDetailList").jqGrid("setGridWidth",$(".opt-btn").width()+12);
			$("#checkDetailList").jqGrid('setGridHeight',$(document).height() - $(".opt-btn").height() - 60);
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
			var measurementName=$("#measurementName").val();
			var measurementSpecification=$("#measurementSpecification").val();
			var manufacturer=$("#manufacturer").val();
			$("#checkDetailList").jqGrid({
				url:'${gsmctx}/base/check-standard/select-list-datas.htm?measurementName='+measurementName+'&measurementSpecification='+measurementSpecification+'&manufacturer='+manufacturer,
				datatype: 'json',
				prmNames:{
					rows:'detailPage.pageSize',
					page:'detailPage.pageNo',
					sort:'detailPage.orderBy',
					order:'detailPage.order'
				},

				rowNum: 20,
				rownumbers: true,
				pager: '#pager',
				height: 450,
				colNames:['','项目名称', '标准值', '允许误差'],
				colModel:[
					{name:'id', index:'id', width:1, hidden:true,align:'center'},
		            {name:'itemName', index:'itemName', width: 100,align:'left',editable:false},
		            {name:'standardValue', index:'standardValue', width: 180,align:'center',editable:false},
		            {name:'allowableError', index:'allowableError', width: 180,align:'center',editable:false}
		        ],
				multiselect: true,
				gridComplete: function(){}
			});
			contentResize();
		}
		//确定
		function realSelect(){
			var ids = jQuery("#checkDetailList").getGridParam('selarrrow');
			if(ids.length == 0){
				alert("请选择问题点!");
				return;
			}
/* 			if(ids.length > 1){
				alert("只能选择一条校准项目!");
				return;
			} */
			if($.isFunction(window.parent.setDetailValue)){
				var data = [];
				for(var i=0;i < ids.length;i++){
					var objs = $("#checkDetailList").jqGrid('getRowData',ids[i]);					
					if(objs){
						data.push({itemName:objs.itemName,standardValue:objs.standardValue,allowableError:objs.allowableError});
					}
				}	
				if(data.length > 0){
					//选择部件后调用父窗口的setDetailValue（data）方法，将部件信息返回到打开窗口的父页面
					window.parent.setDetailValue(data);
				}else{
					alert("选择的值不存在!");
				}
				window.parent.$.colorbox.close();
			}else{
				alert("页面还没有 setDetailValue方法!");
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
			<input type="hidden" name="measurementName" id="measurementName" value="${measurementName}"/>
			<input type="hidden" name="measurementSpecification" id="measurementSpecification" value="${measurementSpecification}"/>
			<input type="hidden" name="manufacturer" id="manufacturer" value="${manufacturer }"/>
			</div>
			<form id="contentForm" name="contentForm" method="post"  action="">
				<table id="checkDetailList"></table>
				<div id="pager"></div>	
			</form>
		</div>
	</div>
</body>
</html>