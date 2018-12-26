<%@page import="com.opensymphony.xwork2.ActionContext"%>
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
<style>
  .sortable { list-style-type: none; margin: 0; padding: 0; }
  .sortable li { margin: 0 3px 3px 3px; padding: 0.4em; padding-left: 1.5em; font-size:14px; height: 18px; }
  .sortable li span { position: absolute; margin-left: -1.3em; }
</style>
<script type="text/javascript">
	var tableJoinJson = ${tableJoinJson};
	$(function(){
		initForm();
	});
	function initForm(){
		var tabHeight = 200;
		$("#tabs-can-sel-data-set").height(tabHeight).tabs();
		$("#tabs-sel-data-set").height(tabHeight).tabs();
		//初始化可选数据集
		initCanSelDataSet();
		//初始化已选数据集
		initSelDataSet();
		
		//连接条件
		$( "ul.sortable").sortable({
			stop: function( event, ui ) {
				resetJoinColumns($(ui.item).closest("ul"));
			}
		});
	    //初始化连接列字段
	    searchTableChange();
	}
	function searchTableChange(){
		$(".sortable").html("");
		$("#legendTableName").html("");
		
		var searchTableId = $("#searchTable").val();
		$("#canSelDataSet").jqGrid("setGridParam",{
			postData:{tableId:searchTableId}
		}).trigger("reloadGrid");
		
		var searchTableName = $("#searchTable").find("option[value="+searchTableId+"]").html();
		if(searchTableName){
			$("#legendTableName").html(searchTableName);
			var joinJson = tableJoinJson[searchTableName];
			if(joinJson){
				var leftColumns = joinJson.leftColumns?joinJson.leftColumns:[];
				for(var pro in leftColumns){
					var column = leftColumns[pro];
					var $li = $("<li></li>").attr("class","ui-state-default")
									.attr("tableName",column.tableName)
									.attr("columnName",column.columnName)
									.attr("columnAlias",column.columnAlias);
					$li.append(column.tableName + "|" + column.columnAlias)
						.bind("dblclick",function(event){
							removeJoinField(this);
				    	});
					$("#join-left").append($li);
				}
				var mainColumns = joinJson.mainColumns?joinJson.mainColumns:[];
				for(var pro in mainColumns){
					var column = mainColumns[pro];
					var $li = $("<li></li>").attr("class","ui-state-default")
									.attr("tableName",column.tableName)
									.attr("columnName",column.columnName)
									.attr("columnAlias",column.columnAlias);
					$li.append(column.tableName + "|" + column.columnAlias)
						.bind("dblclick",function(event){
							removeJoinField(this);
				    	});
					$("#join-right").append($li);
				}
			}
		}
	}
	
	function resetJoinColumns($ul){
		var searchTableId = $("#searchTable").val();
		var searchTableName = $("#searchTable").find("option[value="+searchTableId+"]").html();
		if(!tableJoinJson[searchTableName]){
			tableJoinJson[searchTableName] = {};
		}
		var json = tableJoinJson[searchTableName];
		
		var ulId = $ul.attr("id");
		var columns = [];
		$ul.find("li").each(function(index,obj){
			var $li = $(obj);
			columns.push({
				tableName:$li.attr("tableName"),
				columnName:$li.attr("columnName"),
				columnAlias:$li.attr("columnAlias")
			});
		});
		var columnName = 'mainColumns';
		if(ulId == 'join-left'){
			columnName = 'leftColumns';
		}
		json[columnName] = columns;
	}
	
	function addJoinColumn(ulId,json){
		var isExist = false;
		$("#" + ulId + " li").each(function(index,obj){
			var $li = $(obj);
			var tableName = $li.attr("tableName");
			var columnName = $li.attr("columnName");
			var columnAlias = $li.attr("columnAlias");
			if(tableName == json.tableName && columnName == json.columnName && columnAlias == json.columnAlias){
				isExist = true;
				return false;
			}
		});
		if(!isExist){
			var $li = $("<li></li>").attr("class","ui-state-default")
			.attr("tableName",json.tableName)
			.attr("columnName",json.columnName)
			.attr("columnAlias",json.columnAlias);
			$li.append(json.tableName + "|" + json.columnAlias)
				.bind("dblclick",function(event){
					removeJoinField(this);
		    	});
			$("#" + ulId).append($li);
			
			resetJoinColumns($("#" + ulId));
		}
	}
	
	function removeJoinField(liObj){
		var $li = $(liObj);
		var $ul = $li.closest("ul");
		
		$(liObj).remove();
		resetJoinColumns($ul);
	}
	
	//初始化可选数据集
	function initCanSelDataSet(){
		var systemWidth = $("#tabs-can-sel-data-set").width()+4;
		var systemHeight = $("#tabs-can-sel-data-set").height()-70-$("#searchDiv1").height();
		var tableId = $("#searchTable").val();
		$("#canSelDataSet").jqGrid({
			url : '${chartdesignctx}/custom-search/table-column-datas.htm',
			postData:{
				tableId:tableId
			},
			rownumbers : true,
			width : systemWidth,
			//pager : '#canSelDataSetPager',
			rowNum : 1000,
			prmNames:{
				rows:'datasourcePage.pageSize',
				page:'datasourcePage.pageNo',
				sort:'datasourcePage.orderBy',
				order:'datasourcePage.order'
			},
			colModel: [
				{name:'id',index:'id',label:'ID',hidden:true},
				{name:'columnName',index:'columnName',label:'字段名',hidden:true},
				{name:'columnAlias',index:'columnAlias',label:'字段别名',width:160},
				{name:'tableName',index:'tableName',label:'数据源'}
			],
		    multiselect: true,
		   	autowidth: true,
			forceFit : true,
		   	shrinkToFit: false,
			viewrecords: true, 
			sortorder: "desc",
			ondblClickRow: function(rowId){
				var rowData = $("#canSelDataSet").jqGrid("getRowData",rowId);
				addJoinColumn("join-left",rowData);
			},
			gridComplete : function(){}
		});
		$("#canSelDataSet").jqGrid("setGridHeight",systemHeight);
	}
	function search(){
		$("#canSelDataSet").jqGrid("setGridParam",{
			postData:{
				name : $(":input[name=searchName]").val()
			}
		}).trigger("reloadGrid");
	}
	//初始化数据集
	function initSelDataSet(){
		var systemWidth = $("#tabs-sel-data-set").width()+4;
		var systemHeight = $("#tabs-sel-data-set").height()-70;
		$("#selDataSet").jqGrid({
			rownumbers : true,
			datatype:'local',
			width : systemWidth,
			pager : '#selDataSetPager',
			rowNum : 1000,
			colModel: [
				{name:'id',index:'id',label:'ID',hidden:true},
				{name:'columnName',index:'columnName',label:'字段名',hidden:true},
				{name:'columnAlias',index:'columnAlias',label:'字段别名',width:160},
				{name:'tableName',index:'tableName',label:'数据源'}
			],
			data:${selDatasets},
		    multiselect: true,
		   	autowidth: true,
			forceFit : true,
		   	shrinkToFit: false,
			viewrecords: true, 
			sortorder: "desc",
			ondblClickRow: function(rowId){
				var rowData = $("#selDataSet").jqGrid("getRowData",rowId);
				addJoinColumn("join-right",rowData);
			},
			gridComplete : function(){}
		});
		$("#selDataSet").jqGrid("setGridHeight",systemHeight);
	}
	function submitForm(url){
		var ids = $("#selDataSet").jqGrid("getDataIDs");
		if(ids.length==0){
			alert("选择的字段不能为空!");
			return;
		}
		var tableJoinJsonStr = JSON.stringify(tableJoinJson);
		$("#tableJoinJsonStr").val(tableJoinJsonStr);
		
		var columnData = $("#selDataSet").jqGrid("getRowData");
		var tableColumnStr = JSON.stringify(columnData);
		$("#tableColumnStr").val(tableColumnStr);
		if($("#form").valid()){
			if(url.indexOf("?")>1){
				url += "&";
			}else{
				url += "?";
			}
			$('#form').attr('action',url);
			$("button").attr("disabled","disabled");
			$("#message").html("正在执行操作,请稍候... ...");
			$('#form').submit();
		}
	}
	//添加数据集
	function addDataSet(){
		var ids = $("#canSelDataSet").jqGrid("getGridParam","selarrrow");
		if(ids.length==0){
			alert("请先在左边表格中选择数据集!");
			return;
		}
		for(var i=0;i<ids.length;i++){
			var d = $("#canSelDataSet").jqGrid("getRowData",ids[i]);
			if($("#selDataSet tr[id="+ids[i]+"]").length==0){
				$("#selDataSet").jqGrid("addRowData",ids[i],d);			
			}
		}
	}
	//移除数据集
	function removeDataSet(){
		var ids = $("#selDataSet").jqGrid("getGridParam","selarrrow");
		if(ids.length==0){
			alert("请先在右边表格中选择数据集!");
			return;
		}
		ids = ids.join(",").split(",");
		for(var i=0;i<ids.length;i++){
			$("#selDataSet").jqGrid("delRowData",ids[i]);		
		}
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
			<div class="opt-btn" style="text-align:right;">
				<span id="message" style="color:red;position:absolute;left:4px;top:8px;">
					<s:actionmessage theme="mytheme" />
				</span>
				<button class='btn' type="button"
					onclick="javascript:window.location.href='${chartdesignctx}/custom-search/input.htm?isBack=true';">
					<span><span><b class="btn-icons btn-icons-prev"></b>上一步</span></span>
				</button>
				<button class='btn' type="button"
					onclick="submitForm('${chartdesignctx}/custom-search/step3.htm')">
					<span><span><b class="btn-icons btn-icons-next"></b>下一步</span></span>
				</button>
				<button class='btn' type="button" onclick="cancel();"><span><span><b class="btn-icons btn-icons-cancel"></b>取消</span></span></button>
			</div>
			<div id="opt-content">
				<div id="info" style="background:white;padding:4px;margin:0px 0px 8px 0px;font-weight:bold;font-size:14px;line-height:30px;">
					2/6.定义组合条件
					<span style="font-size:10px;font-weight:normal;">双击字段名设置为连接字段</span>
					<div style="float:right;margin-right:4px;">
						数据源:
						<s:select list="searchTables" id="searchTable"
							onchange="searchTableChange(this)"
							listKey="id" listValue="name" theme="simple"></s:select>
					</div>
				</div>
				<form id="form" name="form"
					method="post" action="">
					<input type="hidden" name="tableJoinJsonStr" id="tableJoinJsonStr"/>
					<input type="hidden" name="tableColumnStr" id="tableColumnStr"/>
					<table style="width: 100%;height:100%;">
						<tr>
							<td style="width:40%;">
								<div id="tabs-can-sel-data-set">
									<ul>
										<li><a href="#tabs-can-sel-data-set-1">可选字段</a>
										</li>
									</ul>
									<div id="tabs-can-sel-data-set-1" style="padding:4px 0px 0px 10px;">
										<table id="canSelDataSet"></table>
									</div>
								</div>
							</td>
							<td style="width:10%;" valign="middle" align="center">
								<button style="cursor:pointer;" type="button"
									onclick="addDataSet()">添加 >>
								</button>
								<button style="margin-top:20px;cursor:pointer;"  type="button"
									onclick="removeDataSet()"><< 移除
								</button>
							</td>
							<td style="width:40%;">
								<div id="tabs-sel-data-set">
									<ul>
										<li><a href="#tabs-sel-data-set-1">已选字段</a>
										</li>
									</ul>
									<div id="tabs-sel-data-set-1" style="padding:4px 0px 0px 10px;">
										<table id="selDataSet"></table>
									</div>
								</div>
							</td>
						</tr>
					</table>
						<table style="width: 100%;height:100%;">
							<tr>
								<td style="width:40%;" valign="top">
									<fieldset style="margin-top:4px;">
									<legend><span id="legendTableName"></span>连接字段</legend>
										<ul id="join-left" class="sortable">
										</ul>
									</fieldset>
								</td>
								<td style="width:10%;">
								</td>
								<td style="width:40%;" valign="top">
									<fieldset style="margin-top:4px;">
									<legend>主表连接字段</legend>
										<ul id="join-right" class="sortable">
										</ul>
									</fieldset>
								</td>
							</tr>
						</table>
					</fieldset>
				</form>
			</div>
		</div>
	</div>
</body>
</html>