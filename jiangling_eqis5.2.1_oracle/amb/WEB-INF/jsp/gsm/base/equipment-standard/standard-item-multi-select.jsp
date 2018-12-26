<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>选择标准件</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript">
	var topMenu = 'aaa';
	$(document).ready(function(){			
		createBom();
	});
	//创建不良部件
	function createBom(){
		var measurementName=$("#measurementName").val();
		$("#standardItemList").jqGrid({
			url:'${gsmctx}/base/equipment-standard/select-list-datas.htm?measurementName='+measurementName,
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
			colNames:['','标准件名称', '标准件编号','有效期','证书编号',],
			colModel:[
				{name:'id', index:'id', width:1, hidden:true,align:'center'},
	            {name:'standardName', index:'standardName', width: 100,align:'left',editable:false},
	            {name:'standardNo', index:'standardNo', width: 180,align:'left',editable:false},
	            {name:'validityDate', index:'validityDate', width: 180,align:'left',editable:false},
	            {name:'certificateNo', index:'certificateNo', width: 180,align:'left',editable:false},
	        ],
			multiselect: true,
			gridComplete: function(){}
		});
		contentResize();
	}
	//确定
	function realSelect(){
		var ids = jQuery("#standardItemList").getGridParam('selarrrow');
		if(ids.length == 0){
			alert("请选择测试项目!");
			return;
		}
/* 		if(ids.length > 1){
			alert("只能选择一条测试项目!");
			return;
		} */
		if($.isFunction(window.parent.setItemValue)){
			var data = [];
			for(var i=0;i < ids.length;i++){
				var objs = $("#standardItemList").jqGrid('getRowData',ids[i]);					
				if(objs){
					data.push({standardName:objs.standardName,standardNo:objs.standardNo,validityDate:objs.validityDate,certificateNo:objs.certificateNo});
				}
			}	
			if(data.length > 0){
				//选择部件后调用父窗口的setProblemValue（data）方法，将部件信息返回到打开窗口的父页面
				window.parent.setItemValue(data);
			}else{
				alert("选择的值不存在!");
			}
			window.parent.$.colorbox.close();
		}else{
			alert("页面还没有 setItemValue方法!");
		}
	}
	//取消
	function cancel(){
		window.parent.$.colorbox.close();
	}	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn" id="btnDiv">
				<button  class='btn' type="button" onclick="realSelect();"><span><span><b class="btn-icons btn-icons-ok"></b>确定</span></span></button>	
				<button  class='btn' type="button" onclick="cancel();"><span><span><b class="btn-icons btn-icons-cancel"></b>取消</span></span></button>
				<button  class='btn' onclick="javascript:$('#customerSearchDiv').toggle();contentResize();" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
				<input type="hidden" name="measurementName" id="measurementName" value="${measurementName}"/>
			</div>
			<div style="padding:6px;">
				<table id="standardItemList"></table>
				<div id="pager"></div>
			</div>
		</div>
	</div>
</body>



</html>